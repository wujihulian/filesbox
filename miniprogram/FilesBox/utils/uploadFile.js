import {
    req,
    uploadFile
} from './service';
import {
    URL,
} from './config';
import{timeFormat} from './util'
const UPLOAD_URL = URL.uploadFile;
const MERGE_URL = URL.uploadCheck;
/**
 * 
 * @param { String } type image | video | fiels
 * @param { String } sourceID 云盘文件夹ID
 */
const choseFiles = function (type, sourceID, busType,path,name) {
    let filesType = ['image', 'video', 'fiels'],
        actions = ['图片', '视频', '文件'],
        choseTypeIndex = 0,
        fns = ['choseImageConfirm', 'choseVideoConfirm', 'choseFilesConfirm'];
    if (type) {
        choseTypeIndex = filesType.findIndex(c => c == type);
        this[fns[choseTypeIndex]](sourceID, busType,path,name);
    } else {
        wx.showActionSheet({
            itemList: ['图片', '视频', '文件'],
            success: res => {
                this[fns[res.tapIndex]](sourceID, busType,path,name);
            }
        })
    }
}

// 选择图片
const choseImageConfirm = function (sourceID, busType,path,name) {
    wx.chooseImage({
        count: 1,
        success: res => {
            wx.setStorageSync('tempFilePaths', res.tempFilePaths[0])
            this.uploadLargeFile(path,res.tempFiles[0].path, res.tempFiles[0].size, busType, UPLOAD_URL, MERGE_URL, sourceID,name).then(data => {}).catch(err => {
                wx.hideLoading();
                console.log(err)
            })
        }
    })
}

// 选择视频
const choseVideoConfirm = function (sourceID, busType,path,name) {
    wx.chooseVideo({
        sourceType: ['album', 'camera'],
        maxDuration: 60,
        compressed: false,
        success: res => {
            wx.setStorageSync('tempFilePaths', res.tempFilePaths)
            this.uploadLargeFile(path,res.tempFilePath, res.size, busType, UPLOAD_URL, MERGE_URL, sourceID,name).then(data => {}).catch(err => {
                wx.hideLoading();
                console.log(err)
            })
        },
        fail: err => {
            console.log(err)
        }
    })
}

// 选择文件
const choseFilesConfirm = function (sourceID,busType,path,name) {
    wx.chooseMessageFile({
        count: 1,
        type: 'all',
        success: res => {
            console.log(res)
            wx.setStorageSync('tempFilePaths', res.tempFiles[0].path)
            this.uploadLargeFile(path,res.tempFiles[0].path, res.tempFiles[0].size,busType, UPLOAD_URL, MERGE_URL, sourceID, res.tempFiles[0].name).then(data => {}).catch(err => {
                wx.hideLoading();
            })
        }
    })
}

/**
 * 
 * @param {*} tempFilePath 文件本地路径
 * @param {*} totalSize 文件总大小
 * @param {*} busType 业务类型
 * @param {*} uploadUrl 上传路径
 * @param {*} verifyUrl 验证秒传路径
 * @param {*} sourceID 云盘文件夹id, 云盘业务使用
 */

// 上传文件，验证文件是否秒传
async function uploadLargeFile(path,tempFilePath, totalSize, busType, uploadUrl = UPLOAD_URL, verifyUrl = MERGE_URL, sourceID = '', name = '') {
    const PAGES = getCurrentPages();
    let currentPage = PAGES[PAGES.length - 1];
    wx.showLoading({
        title: '上传中…',
    })
    let result = await getFileMD5(tempFilePath);
    let index = tempFilePath.lastIndexOf('.');
    let fileSuffix = tempFilePath.substring(index, tempFilePath.length);
    fileSuffix = fileSuffix.toLowerCase();
    let date = new Date()
    let fileName = 'WX'+timeFormat('yyyyMMdd_hhmm_ss',date)+fileSuffix
    let verifyResult = await checkFile(busType, result.data, sourceID, name || fileName, totalSize, verifyUrl,path);
    if (verifyResult.fileExists) {
        verifyResult.state = 1;
        wx.hideLoading();
        wx.showToast({
            title: '上传成功！',
        })
        if (currentPage.selectComponent('#files').returnUploadPath) {
            currentPage.selectComponent('#files').returnUploadPath(verifyResult);
        }
        if(path){
            currentPage.selectComponent('#files').selectComponent('#attribute').returnUploadPath(verifyResult);
        }
        return;
    }
    uploadChunk(tempFilePath, result.data, totalSize, busType, uploadUrl, verifyUrl, sourceID, verifyResult.chunkList, name || fileName,path);
}

// 获取文件MD5
async function getFileMD5(tempFilePath) {
    return new Promise((resolve, reject) => {
        wx.getFileInfo({
            filePath: tempFilePath,
            success: res => {
                resolve({
                    isSuccess: true,
                    data: res.digest
                })
            },
            fail: err => {
                reject({
                    isSuccess: false,
                    data: err
                })
            }
        })
    })
}
/**
 * 验证文件是否可以秒传
 * @param {*} busType 业务类型
 * @param {*} checksum 文件md5
 * @param {*} sourceID 云盘文件夹id, 云盘业务使用
 * @param {*} fileName 文件名
 * @param {*} size 文件大小
 * @param {*} verifyUrl 验证文件地址
 */
async function checkFile(busType, checksum, sourceID, fileName, size, verifyUrl,path) {
  console.log(fileName)
    return req('GET', verifyUrl, {
        busType,
        hashMd5:checksum,
        sourceID,
        name:fileName,
        size,
        path:path?path:''
    }, {}, false);
}

async function uploadChunk(tempFilePath, md5, totalSize, busType, uploadUrl, verifyUrl, sourceID, chunkArray = [], name = '',path) {
    wx.setKeepScreenOn({
        keepScreenOn: true
    })
    let chunk = 0,
        chunks = 0,
        resultNumber = 0,
        taskNumber = 0,
        PAGES = getCurrentPages();
    let currentPage = PAGES[PAGES.length - 1];
    // 文件大小小于5M使用普通上传
    if (totalSize < 5 * 1024 * 1024) {
        uploadFile(uploadUrl, tempFilePath, {
            busType,
            hashMd5:md5,
            sourceID,
            name,
            path:path?path:''
        }, 'file', sourceID).then(data => {
            currentPage.selectComponent('#files').returnUploadPath(data)
            if(path)currentPage.selectComponent('#files').selectComponent('#attribute').returnUploadPath(data);
            wx.hideLoading();
        }).catch(err => {
            wx.hideLoading();
            wx.showToast({
                title: JSON.parse(err.data).message,
                icon: 'none'
            })
            err.state = 0;
            currentPage.selectComponent('#files').returnUploadPath(err)
            if(path)currentPage.selectComponent('#files').selectComponent('#attribute').returnUploadPath(err);
        })
        return;
    }
    wx.hideLoading();
    // 计算上传总片数
    chunks = Math.ceil(totalSize / (2 * 1024 * 1024));

    // 断点内容继续上传
    if (!chunkArray.length) {
        for (let i = 0; i < chunks; i++) chunkArray.push(i);
    } else {
        let temp = [];
        for (let i = 0; i < chunks; i++) {
            if (chunkArray.indexOf(i) == -1) {
                temp.push(i)
            }
        }
        chunkArray = temp;
    }
    currentPage.selectComponent('#files').setData({
        loadingState: 99,
        timer: setInterval(() => {
            if (chunk >= chunkArray.length) {
                clearInterval(currentPage.data.timer);
                return;
            }
            if (taskNumber < 6) {
                uploadTask(tempFilePath, md5, totalSize, busType, uploadUrl, verifyUrl, sourceID, chunkArray[chunk], chunks, name,path).then(data => {
                    resultNumber++;
                    taskNumber--;
                    currentPage.selectComponent('#files').setData({
                      uploadProgress: (resultNumber / chunkArray.length * 100).toFixed(2)
                    })
                    if (data.hasOwnProperty('fileID')) {
                      currentPage.setData({
                          loadingState: 0
                      })
                      currentPage.selectComponent('#files').returnUploadPath(data);
                      if(path)currentPage.selectComponent('#files').selectComponent('#attribute').returnUploadPath(data);
                  }
                }).catch(err => {
                    console.log(err)
                    wx.showToast({
                        title: '上传失败,请重试！',
                        icon: 'none'
                    })
                    currentPage.selectComponent('#files').setData({
                        loadingState: 0
                    })
                    clearInterval(currentPage.data.timer);
                })
                chunk++;
                taskNumber++;
            }
        }, 500)
    })
}

const uploadTask = function (tempFilePath, md5, totalSize, busType, uploadUrl, verifyUrl, sourceID, chunk, chunks, name,path) {
    const fs = wx.getFileSystemManager(),
        chunkSize = 2 * 1024 * 1024;
    let fileName = tempFilePath.split('/');
    fileName = fileName[fileName.length - 1];
    fileName = fileName.split('.')[0];
    let suffix = tempFilePath.split('.');
    suffix = suffix[suffix.length - 1];
    suffix = suffix.toLowerCase();
    const res = fs.readFileSync(tempFilePath, 'binary', chunk * chunkSize, (chunk + 1) * chunkSize > totalSize ? totalSize - chunk * chunkSize : chunkSize);
    fs.writeFileSync(
        `${wx.env.USER_DATA_PATH}/${fileName}${chunk}.${suffix}`,
        res,
        "binary",
    )
    console.log(`总大小:${totalSize},当前块数:${chunk+1},总块数:${chunks},当前块大小:${chunk*chunkSize,(chunk+1)*chunkSize>totalSize ? totalSize-chunk*chunkSize : chunkSize}`);
    return new Promise((resolve, reject) => {
        uploadFile(uploadUrl, `${wx.env.USER_DATA_PATH}/${fileName}${chunk}.${suffix}`, {
            busType,
            chunks,
            chunk,
            chunkSize: chunkSize,
            hashMd5:md5,
            checksum:md5,
            sourceID,
            name,
            path:path?path:''
        }, 'file', sourceID).then(data => {
            fs.unlink({
                filePath: `${wx.env.USER_DATA_PATH}/${fileName}${chunk}.${suffix}`
            })
            resolve(data)
        }).catch(err => {
            // console.log(err)
            reject(err)
        })
    })

}

module.exports = {
    choseFiles,
    choseImageConfirm,
    choseVideoConfirm,
    uploadLargeFile,
    choseFilesConfirm
}