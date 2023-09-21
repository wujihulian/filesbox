// pages/messages/messages.js
import {req} from "../../utils/service";
import {URL} from '../../utils/config';
const {globalData} = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        lang:globalData.lang,
        currentPage:1,
        systemInfo:globalData.systemInfo
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        let { systemInfo} = this.data,scrollHeight;
        scrollHeight = systemInfo.windowHeight - systemInfo.statusBarHeight - 44
        this.setData({
            scrollHeight
        })
    },
    getNoticeList(reachBottom = false){
        let param = {
            currentPage: this.data.currentPage,
            pageSize: 10
        }
        req("GET",URL.getNoticeList, param, {}).then(res => {
            res.list.map(c=>{
                c.sendTime = c.sendTime.split('T')[0]+' '+c.sendTime.split('T')[1]
            })
            this.setData({
                messageList:reachBottom?[...this.data.messageList,...res.list]:res.list
            })
        }, err => {
            showErrMsg(err);
        });
    },
    getNoticeInfo(e){
        let {id} = e.currentTarget.dataset,DOMAIN = wx.getStorageSync('DOMAIN'),token = wx.getStorageSync('token'),
        url = DOMAIN +'/#/noticePage/'+id+`/${token}`
        wx.setStorageSync('needRefresh', 1)
        wx.navigateTo({
          url: '/pages/link/link?url='+url
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
            currentPage:1
        })
        this.getNoticeList()
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
        this.setData({
            currentPage:1,
        })
        this.getNoticeList()
        this.setData({isPullDownRefresh:true})
        setTimeout(() => {
            this.setData({isPullDownRefresh:false})
        }, 1000);
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {
        this.setData({currentPage:this.data.currentPage+1})
        this.getNoticeList(true)
    },

    /**
     * 用户点击右上角分享
     */
     
})