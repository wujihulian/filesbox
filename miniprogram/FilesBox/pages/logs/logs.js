// pages/setting/setting.js
import languageUtils from '../../utils/languageUtils';
const {globalData} = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        lang:globalData.lang,
        languageList:['简体中文','English']
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
       
    },
    goToPage(e){
        let {index} = e.currentTarget.dataset,url
        if(index==0){
            url = '/pages/loginLogs/loginLogs'
            
        }else if(index==1){
            url = '/pages/deviceLogs/deviceLogs'
        }else{
            url = '/pages/filesLogs/filesLogs'
        } 
        wx.navigateTo({
            url:url
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
        this.setData({
            lang:globalData.lang
        })
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
     
})