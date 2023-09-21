// packageB/pages/files/files-list/files-list.js
import {
    req
} from '../../../utils/service';
import {
    URL,
    STATIC_DOMAIN,
} from '../../../utils/config';
import {
    imageUrl
} from '../../../utils/image';
const {globalData} = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        prop: {
            type: Object,
            value: []
        },
        isTile: {
            type: Boolean,
            value: false
        },
        isEdit: {
            type: Boolean,
            value: false
        },
        catalogue: {
            type: Object,
            value: []
        },
        showRecyle: {
            type: Boolean,
            value: false
        },
        showShare:{
            type: Boolean,
            value: false,
            observer:function(n){
                console.log(n)
            }
        }
    },
    lifetimes: {
        attached() {
            let PAGES = getCurrentPages();
            this.currentPage = PAGES[PAGES.length - 1];
            this.setData({
                DOMAIN:wx.getStorageSync('DOMAIN'),
                lang:globalData.lang
            })
        },
    },
    /**
     * 组件的初始数据
     */
    data: {
        imageUrl
    },

    /**
     * 组件的方法列表
     */
    methods: {
        // 预览文件
        previewFile(e) {
            let {DOMAIN} = this.data
            if(this.data.showRecyle){
                wx.showToast({
                    title: '该文件在回收站中，请还原后再试！',
                    icon: 'none'
                })
                return;
            }
            let {
                pptPreviewUrl,
                sourceID,
                fileType
            } = e.currentTarget.dataset.item,{item} = e.currentTarget.dataset,role = wx.getStorageSync('options').role,
            isImage = /jpeg|jpg|gif|png|webp|jfif|bmp|dpg/;
            console.log(item)
            if(item.oexeContent&&item.oexeContent.type=='url'){
              this.openInweb(item.oexeContent.value)
              return
            }
            if(item.fileType=='videoRecording'||item.fileType=='colorPicker'){
              wx.showToast({
                title: '不支持该操作',
                icon:'none'
              })
              return
            }
            if (isImage.test(fileType)) {
              if(item.targetType==1){
                if(role['explorer.view']==0){
                  wx.showToast({
                    title: '暂无权限',
                    icon:'none'
                  })
                  return
                }
              }else if(item.targetType==2){
                if(role['explorer.view']==0||item.auth.indexOf('3')==-1){
                  wx.showToast({
                    title: '暂无权限',
                    icon:'none'
                  })
                  return
                }
              }
            }
            if(fileType=='zip'||fileType=='tar'){
                this.triggerEvent('onShowZipPreview',{item:e.currentTarget.dataset.item})
                return
            }
            if(item.oexeContent&&item.oexeContent.type=='lnk'){
              fileType = item.oexeContent.fileType
              sourceID = item.oexeContent.sourceID
            }
            wx.showLoading({
                title: '加载中…',
                mask: true
            })
            req('GET',URL.getPreviewInfo, {
                busType: 'cloud',
                sourceID: sourceID
            }, {}, false).then(data => {
                wx.hideLoading({})
                let previewUrl = '',
                    fileType = 1;
                const isVideo = /mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv|vob|webm/,
                    isAudio = /ram|swf|mp3|wma|wav/,
                    isImage = /jpeg|jpg|gif|png|webp|jfif|bmp|dpg/,
                    isFile = /pptx|ppt|xlsx|xls|doc|docx|pdf|wps|txt/;
                if (isImage.test(data.fileType)) {
                  let role = wx.getStorageSync('options').role
                  if(item.targetType==1){
                    if(role['explorer.view']==0){
                      wx.showToast({
                        title: '暂无权限',
                        icon:'none'
                      })
                      return
                    }
                  }else if(item.targetType==2){
                    if(role['explorer.view']==0||item.auth.indexOf('3')==-1){
                      wx.showToast({
                        title: '暂无权限',
                        icon:'none'
                      })
                      return
                    }
                  }
                    wx.previewImage({
                      urls: [`${DOMAIN+data.downloadUrl}`],
                    })
                    return;
                }else if (isFile.test(data.fileType)) {
                    previewUrl = data.pptPreviewUrl;
                    fileType = 1;
                } else if (data.fileType=='et') {
                  previewUrl = JSON.parse(data.yzViewData).viewUrl;
                  fileType = 1;
                }else if (data.fileType=='srt') {
                  previewUrl = data.downloadUrl;
                  fileType = 1;
                }else if (isVideo.test(data.fileType)) {
                    if (data.previewUrl) previewUrl = /http|https/.test(data.previewUrl) ? data.previewUrl :DOMAIN + data.previewUrl;
                    fileType = 2;
                } else if (isAudio.test(data.fileType)) {
                    if (data.downloadUrl) previewUrl = /http|https/.test(data.downloadUrl) ? data.downloadUrl :DOMAIN + data.downloadUrl;
                    fileType = 3;
                }else if (data.fileType=='dcm') {
                    let DOMAIN = wx.getStorageSync('DOMAIN'),
                    url = DOMAIN +'/#/filesPreview?sourceID='+sourceID
                    wx.navigateTo({
                        url:'/pages/link/link?url='+encodeURIComponent(url)+'&dcmNeedToken=1'
                    })
                    return
                }else if (data.fileType=='epub') {
                  let DOMAIN = wx.getStorageSync('DOMAIN'),
                  url = DOMAIN +'/reader/index.html?fileUrl='+encodeURIComponent(data.downloadUrl)
                  this.openInweb(url)
                  // wx.navigateTo({
                  //     url:'/pages/link/link?url='+encodeURIComponent(url)
                  // })
                  return
              }else if (data.fileType=='smm'||data.fileType=='km'||data.fileType=='mind') {
                  let DOMAIN = wx.getStorageSync('DOMAIN'),
                  url = DOMAIN +'/newmindMap/index.html?sourceID='+sourceID+'&action=&fileType='+data.fileType+'&mindUrl='+encodeURIComponent(data.downloadUrl)
                  this.openInweb(url)
                  return
                }else if (data.fileType=='drawio'||data.fileType=='vsdx') {
                  let DOMAIN = wx.getStorageSync('DOMAIN'),
                  url = DOMAIN+'/plugin/draw/?lang=zh'+'&lightbox=1&shareCode='+'&offline=1&sourceID='+sourceID +'&token=' +encodeURIComponent(wx.getStorageSync('token'))+'&domain='+encodeURIComponent(DOMAIN)
                  this.openInweb(url)
                  return
                }else if (data.fileType=='xmind'||data.fileType=='vsdx') {
                  let DOMAIN = wx.getStorageSync('DOMAIN'),
                  url = DOMAIN+'/xmindviewer.html?xmindUrl='+encodeURIComponent(data.downloadUrl)
                  this.openInweb(url)
                  return
                }
                else{
                    wx.showToast({
                        title: '该文件不支持预览！',
                        icon: 'none'
                    })
                    return;
                }
                // if (!data.fileType || ['txt', 'rar', 'zip', '7z', 'gz', 'apk','tar'].includes(data.fileType)) {
                //     wx.showToast({
                //         title: '该文件不支持预览！',
                //         icon: 'none'
                //     })
                //     return;
                // }
                wx.setStorageSync('previewUrl', previewUrl)
                wx.navigateTo({
                    url: `/pages/files-preview/files-preview?sourceID=${sourceID}&fileType=${fileType}`,
                })
            })
        },
        // 复制浏览器打开
        openInweb(url){
          wx.showModal({
            title: '小程序暂不支持打开，请复制链接在浏览器打开',
            confirmText:'复制',
            complete: (res) => {         
              if (res.confirm) {
                  wx.setClipboardData({
                      data: url,
                      success:res => {
                          wx.showToast({
                              title:'复制成功'
                          });
                      }
                  })
              }
            }
          })
        },
        // 打开文件
        openFile(param) {
            let {sourceID,fileType} = param,{DOMAIN} = this.data
            if(fileType=='zip'||fileType=='tar'){
                this.triggerEvent('onShowZipPreview',{item:param})
                return
            }
            wx.showLoading({
                title: '加载中…',
                mask: true
            })
            req('GET',URL.getPreviewInfo, {
                busType: 'cloud',
                sourceID: sourceID
            }, {}, false).then(data => {
                wx.hideLoading({})
                let previewUrl = '',
                    fileType = 1;
                const isVideo = /mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv|vob/,
                    isAudio = /ram|swf|mp3|wma|wav/,
                    isImage = /jpeg|jpg|gif|png|webp|jfif|bmp|dpg/,
                    isFile = /pptx|ppt|xlsx|xls|doc|docx|pdf|wps/;
                if (isImage.test(data.fileType)) {
                    wx.previewImage({
                        urls: [`${DOMAIN+data.downloadUrl}`],
                    })
                    return;
                }
                if (isFile.test(data.fileType)) {
                    previewUrl = data.pptPreviewUrl;
                    fileType = 1;
                } else if (isVideo.test(data.fileType)) {
                    if (data.previewUrl) previewUrl = /http|https/.test(data.previewUrl) ? data.previewUrl :DOMAIN + data.previewUrl;
                    fileType = 2;
                } else if (isAudio.test(data.fileType)) {
                    if (data.downloadUrl) previewUrl = /http|https/.test(data.downloadUrl) ? data.downloadUrl :DOMAIN + data.downloadUrl;
                    fileType = 3;
                }else{
                    wx.showToast({
                        title: '该文件不支持打开！',
                        icon: 'none'
                    })
                    return;
                }
                // if (!data.fileType || ['txt', 'rar', 'zip', '7z', 'gz', 'apk'].includes(data.fileType)) {
                //     wx.showToast({
                //         title: '该文件不支持打开！',
                //         icon: 'none'
                //     })
                //     return;
                // }
                wx.setStorageSync('previewUrl', previewUrl)
                wx.navigateTo({
                    url: `/pages/files-preview/files-preview?sourceID=${sourceID}&fileType=${fileType}`,
                })
            })
        },
        // 选择文件夹打开
        choseFolder(e) {
            if(this.data.showRecyle){
                wx.showToast({
                    title: '该文件在回收站中，请还原后再试！',
                    icon: 'none'
                })
                return;
            }
            let {
                sourceID,
                name,
                labelId,
                targetType,
                auth,
                type
            } = e.currentTarget.dataset.item,item = e.currentTarget.dataset.item, {
                catalogue
            } = this.properties;        
            catalogue.push({
                label: name,
                sourceID:item.oexeIsFolder&&item.fileType=='oexe'?item.sourceInfo.sourceID:sourceID,
                labelId
            })
            // console.log(item)
            this.triggerEvent('catalogueChange', {
                sourceID:item.oexeIsFolder&&item.fileType=='oexe'?item.sourceInfo.sourceID:sourceID,
                catalogue,
                targetType,
                auth
            })
        },
        // 编辑文件夹选择
        choseFile(e) {
            let {
                index,
                cate
            } = e.currentTarget.dataset, {
                prop
            } = this.properties;
            prop[cate][index].checked = !prop[cate][index].checked;
            this.setData({
                prop
            })
            this.triggerEvent('setCheckedFile',{prop})
            this.currentPage.setData({
                dataList: prop
            })
        },
        showEdit(e) {
            let {
                index,
                cate
            } = e.currentTarget.dataset, {
                prop
            } = this.properties,isImage = /jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg/;
            prop.fileList.map(c=>c.checked=false)
            prop.folderList.map(c=>c.checked=false)
            prop[cate][index].checked = !prop[cate][index].checked;
            this.setData({
              prop
            })
            this.triggerEvent('longPress', {
                isEdit: prop[cate][index].checked?true:false,
                dataList: prop,
                cate: e.currentTarget.dataset.cate,
                isFav:prop[cate][index].isFav,
                targetType:prop[cate][index].targetType,
                fileAuth:prop[cate][index].auth?prop[cate][index].auth:'',
                isTop:prop[cate][index].sort>0?1:0,
                isUpload:prop[cate][index].isFolder?0:1,
                isZip:prop[cate][index].fileType=='zip'||prop[cate][index].fileType=='tar'?1:0,
                formatConvert:prop[cate][index].fileType=='pdf'||prop[cate][index].fileType=='doc'||prop[cate][index].fileType=='docx'||isImage.test(prop[cate][index].fileType)||prop[cate][index].fileType=='ppt'||prop[cate][index].fileType=='pptx'?true:false,
                checkedFile:prop[cate][index]
            })
            wx.setStorageSync('checkedTag', prop[cate][index].tagList)
        }
    }
})