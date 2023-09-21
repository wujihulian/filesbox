// pages/link/link.js
import { imageUrl } from '../../utils/image'; 
import{clipImage} from '../../utils/util';
Page({

    /**
     * 页面的初始数据
     */
    data: {
      imageUrl
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        let url = decodeURIComponent(options.url)
        if(options.needToken){
            let token = encodeURIComponent(wx.getStorageSync('token'))
            url += `?token=${token}`
        }
        if(options.dcmNeedToken){
            let token = encodeURIComponent(wx.getStorageSync('token'))
            url += `&token=${token}`
        }
        let newsInfo = wx.getStorageSync('news')
        wx.getImageInfo({
          src:newsInfo.thumb?newsInfo.thumb:this.data.imageUrl.newsEmpty,  // 这里填写网络图片路径 
          success: (res) => {
            console.log(res)
            // 这个是我封装的裁剪图片方法（下面将会说到）
            this.clipImage(res.path, res.width, res.height, (img) => {
              this.setData({
                shareImage:img
              })
            }); 
          }
        });
        this.setData({
            noTokenUrl:options.url,
            url:url,
            needToken:options.needToken?options.needToken:0,
            shareTitle:newsInfo.title,
        })
    },
    onInformationShare(e){
      console.log(e)
    },
    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

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

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function (options) {
      let {noTokenUrl,needToken} = this.data,
      path = '/pages/link/link?url='+noTokenUrl+'&needToken='+needToken
      return {
          path,
          title:this.data.shareTitle,
          imageUrl:this.data.shareImage
      }
    },
    clipImage
})