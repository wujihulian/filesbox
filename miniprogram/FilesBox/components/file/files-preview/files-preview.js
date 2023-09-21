// packageB/pages/files/files-preview/files-preview.js
import { req, reportVisitLog } from '../../../../utils/service';
import { URL, REPORT_VISIT_LOG,DOMAIN } from '../../../../utils/config';
import { genUuid } from '../../../../utils/util';
import { getRouteIdAndSreId } from '../../../../pages/cardDetail/config';
let innerAudioContext = null;
const { globalData, checkAuth } = getApp();
Page({
  REPORT_VISIT_LOG,
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
    this.fileId = options.fileId;
    this.id = options.fileId;
    if(options.name){
      this.setData({
        name:options.name
      })
    }
  },
  getPreviewInfo:function(){
    let param = {busType:'cloud',fileId:this.fileId},DOMAIN = wx.getStorageSync('DOMAIN')
    req('GET',URL.getPreviewInfo,param,{},false).then(data=>{
      let { name } = data;
      name = name.split('.');
      name[0] = encodeURIComponent(name[0]);
      name = name.join('.');
      let audioPlayUrl = `${DOMAIN+data.downloadUrl}`
      let previewUrl = '',fileType = 1;
      const isVideo = /mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv|vob/,
            isAudio = /ram|swf|mp3|wma|wav/,
            isImage = /jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg/,
            isFile = /pptx|ppt|xlsx|xls|doc|docx|pdf|wps/;
      if(isImage.test(data.fileType)){
        wx.previewImage({
          urls: [`${STATIC_DOMAIN+data.downloadUrl}`],
        })
        return;
      }
      if(isFile.test(data.fileType)){
        previewUrl = data.pptPreviewUrl;
        fileType = 1;
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
      if(wx.getStorageSync('token')) {
        let token = wx.getStorageSync('token')
        previewUrl += `&token=${token}`
      }
      this.setData({
        name:data.name,
        audioPlayUrl,
        previewUrl,
        fileType
      })
      console.log(audioPlayUrl)
    }).catch(err=>{
      console.log(err)
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    let { previewUrl, fileType } = this.data;
    // if(fileType == 3){
    //   innerAudioContext = wx.createInnerAudioContext()
    //   innerAudioContext.src = previewUrl;
    //   innerAudioContext.play();
    // }
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    if(this.data.noLogin){
      this.setData({noLogin:false})
      this.getPreviewInfo();
    }
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
  getRouteIdAndSreId
})