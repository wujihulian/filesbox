// packageB/pages/files/files-preview/files-preview.js
import { req} from '../../utils/service';
import { URL } from '../../utils/config';
let innerAudioContext = null;
const { globalData } = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Page({
  shareUserId:'',
  /**
   * 页面的初始数据
   */
  data: {
    isWebView:false,
    noLogin:false
  },

  /**
   * 生命周期函数--监听页面加载
   * fileType 1 文档|图片 2视频 3音频
   */
  onLoad: function (options) {
    this.id = options.sourceID;
    if(options.name){
      this.setData({
        name:options.name,
        DOMAIN:wx.getStorageSync('DOMAIN')
      })
    }
    this.getPreviewInfo()
  },
  getPreviewInfo:function(){
    let param = {busType:'cloud',sourceID:this.id},DOMAIN = wx.getStorageSync('DOMAIN');
    req('GET',URL.getPreviewInfo,param,{},false).then(data=>{
      let { name } = data;
      name = name.split('.');
      name[0] = encodeURIComponent(name[0]);
      name = name.join('.');
      
      let audioPlayUrl = `${DOMAIN+data.downloadUrl}`

      let previewUrl = '',fileType = 1;
      const isVideo = /mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv|vob|webm/,
            isAudio = /ram|swf|mp3|wma|wav/,
            isImage = /jpeg|jpg|gif|png|webp|jfif|bmp|dpg/,
            isFile = /pptx|ppt|xlsx|xls|doc|docx|pdf|wps|txt/;
      if(isImage.test(data.fileType)){
        wx.previewImage({
          urls: [`${DOMAIN+data.downloadUrl}`],
        })
        return;
      }
      if(isFile.test(data.fileType)){
        previewUrl = data.pptPreviewUrl;
        fileType = 1;
      }else if (data.fileType=='et') {
        previewUrl = JSON.parse(data.yzViewData).viewUrl;
        fileType = 1;
      }else if (data.fileType=='srt') {
        let srtText = data.text.replace(/↵/g, '\n')
        this.setData({
          srtText
        })
        fileType = 4;
      }else if(isVideo.test(data.fileType)){
        if(data.previewUrl) previewUrl = /http|https/.test(data.previewUrl)?data.previewUrl: DOMAIN + data.previewUrl;
        fileType = 2;
      }else if(isAudio.test(data.fileType)){
        if(data.downloadUrl) previewUrl = /http|https/.test(data.downloadUrl)?data.downloadUrl: DOMAIN + data.downloadUrl;
        fileType = 3;
      }else{
        wx.showToast({
          title: '该文件不支持预览！',
          icon:'none'
        })
        return;
      }
      // if(!data.fileType || ['txt','rar','zip','7z','gz','apk','tar'].includes(data.fileType)){
      //   wx.showToast({
      //     title: '该文件不支持预览！',
      //     icon:'none'
      //   })
      //   return;
      // }
      if(wx.getStorageSync('token')&&data.fileType!='et') {
        let token = wx.getStorageSync('token')
        previewUrl += `&token=${token}`
      }
      this.setData({
        name:data.name,
        audioPlayUrl,
        previewUrl,
        fileType
      })
    }).catch(err=>{
      console.log(err)
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  onShareAppMessage: function (e) {
    
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    if(innerAudioContext) innerAudioContext.stop();
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    
  },
})