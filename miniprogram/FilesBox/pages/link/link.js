// pages/link/link.js
Page({

    /**
     * 页面的初始数据
     */
    data: {

    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
      let url;
      if(options.url) url = decodeURIComponent(options.url)
      if(options.shareHash){
        console.log(options.shareHash)
        url = `${options.DOMAIN}/#/sharePage/${options.shareHash}`
      }
      if(options.needToken){
          let token = encodeURIComponent(wx.getStorageSync('token'))
          url += `?token=${token}`
      }
      if(options.dcmNeedToken){
          let token = encodeURIComponent(wx.getStorageSync('token'))
          url += `&token=${token}`
      }
      console.log(url)
      this.setData({
          noTokenUrl:options.url,
          url:url,
          needToken:options.needToken?options.needToken:0
      })
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
    // onShareAppMessage: function () {
    //     let {noTokenUrl,needToken} = this.data,
    //     path = '/pages/link/link?url='+noTokenUrl+'&needToken='+needToken
    //     // console.log(path)
    //     return {
    //         path,
    //         title:'您的好友分享给您一个页面'
    //     }
    // }
})