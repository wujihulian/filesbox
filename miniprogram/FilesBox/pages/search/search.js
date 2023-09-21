// pages/search/search.js
import {imageArr1,imageArr2,} from './config';
import {req} from '../../utils/service';
const {globalData} = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        lang:globalData.lang,
        imageArr1,
        imageArr2,
        fileTypeShow:true
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        let { windowHeight,statusBarHeight } = wx.getSystemInfoSync(),scrollHeight;
        scrollHeight = windowHeight - statusBarHeight - 44
        this.setData({
            isEn:wx.getStorageSync('lang')=='en'?1:0,
            scrollHeight,
        })
        this.getOption()
    },
    getOption(){
        let treeOpen = wx.getStorageSync('options').treeOpen,{imageArr2,fileTypeShow} =this.data,informationView = wx.getStorageSync('options').role['explorer.informationView']
        // console.log(treeOpen)
        if(!informationView){
          imageArr2[1].show = false
        }
        if(treeOpen.indexOf('myFav')==-1){
            imageArr2[0].show = false
        }
        if(treeOpen.indexOf('information')==-1||!informationView){
            imageArr2[1].show = false
        }
        if(treeOpen.indexOf('recentDoc')==-1){
            imageArr2[2].show = false
        }
        if(treeOpen.indexOf('shareLink')==-1){
            imageArr2[3].show = false
        }
        if(treeOpen.indexOf('fileTag')==-1){
            imageArr2[4].show = false
        }
        if(treeOpen.indexOf('fileType')==-1){
            fileTypeShow = false
        }
        this.setData({
            imageArr2,
            fileTypeShow
        })
    },
    goToList(e){
        let {item} = e.currentTarget.dataset
        // console.log(item)
        if(item.type=='news'){
            wx.navigateTo({
                url: '/pages/news/news?',
            })
            return
        }
        wx.navigateTo({
          url: '/pages/files-list/files-list?item='+JSON.stringify(item),
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