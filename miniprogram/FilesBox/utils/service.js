import {
    STATIC_DOMAIN,
} from './config.js';
import {
    encodeReqParams,
    genUuid,
} from './util';
// 网络请求
const req = (method, url, params = {}, data = {}, noSecondLevelDomain = true, isOtherInfo = false, header = {}) => {
    const DOMAIN = wx.getStorageSync('DOMAIN')
    const isStaticDomain = /\.json/.test(url);
    url = isStaticDomain ? STATIC_DOMAIN + url : DOMAIN + url;
    if (params) url += encodeReqParams(params);
    const token = wx.getStorageSync('token')
    if (token) header.token = token;
    return new Promise((resolve, reject) => {
        wx.request({
            method,
            url,
            data,
            header,
            success: res => {
                if (isStaticDomain) { // 静态资源
                    if (res.statusCode === 200) {
                        resolve(res.data);
                    } else {
                        reject(res);
                    }
                } else { // 普通接口
                    if (res.data.success === true) {
                        resolve(res.data.data);
                    } else if (res.data.code === '401') {
                        resolve(res.data.data);
                    }else if(res.data.code === 'user.bindSignError'){
                        wx.redirectTo({
                          url: '/pages/accountLogin/accountLogin',
                        })
                    } else {
                        reject(res.data);
                        // logError(res.data.message)
                    }
                }
            },
            fail: err => {
                reject(err);
                console.log('err--', err)
                if(err.errMsg=='request:fail '||err.errMsg=='request:fail url not in domain list'){
                    wx.showToast({
                      title: '服务器地址不正确',
                      icon:'none'
                    })
                }else{
                    wx.redirectTo({
                      url: '/pages/accountLogin/accountLogin',
                    })
                }
            }
        });
    });
};
const logError = function (message) {
    let newDateTime = (new Date()).valueOf();
    let sign = `bc54f4d60f1cec0f9a6cbminiprograma@1009a3b6b428a27bb82694be337b66cc@${newDateTime}@4`
    sign = md5(sign);
    const DOMAIN = wx.getStorageSync('DOMAIN')
    wx.request({
        url: DOMAIN + URL.logError,
        method: 'put',
        header: {
            APPID: 'bc54f4d60f1cec0f9a6cbminiprograma',
            LOGTOKEN: sign
        },
        data: {
            clientTime: newDateTime,
            clientType: 4,
            message: message
        },
        success: (res) => {
            // console.log('报错日志', res.data)
        }
    })
};
// 上传文件
const uploadFile = (url, filePath, formData, name = 'file', sourceID = '') => {
    const DOMAIN = wx.getStorageSync('DOMAIN')
    let header = {};
    if (sourceID) formData.sourceID = sourceID;
    const token = wx.getStorageSync('token')
    if (token) header.token = token;
    return new Promise((resolve, reject) => {
        wx.uploadFile({
            url: DOMAIN + url,
            filePath,
            formData,
            name,
            header,
            success: res => {
                const data = JSON.parse(res.data);
                if (res.statusCode ==200) {
                    resolve(data.data);
                } else {
                    reject(res);
                    // console.log(res)
                    // logError(res.data.message)
                }
            },
            fail: err => {
                reject(err);
            }
        });
    });
};
module.exports = {
    req,uploadFile
};