// pages/user-info/user-info.js
import {imageUrl} from '../../utils/image';
import {req} from "../../utils/service";
import {STATIC_DOMAIN,} from '../../utils/config';
import{renderSize} from '../../utils/util';
const {globalData} = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Page({

    /**
     * 页面的初始数据
     */
    data: {
        lang:globalData.lang,imageUrl
    },
    
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        // if(wx.getStorageSync('accountLogin')){
        //     let userInfo = wx.getStorageSync('accountLogin'),DOMAIN = wx.getStorageSync('DOMAIN')
        //     this.setData({
        //         userInfo,
        //         sizeUse:renderSize(userInfo.sizeUse),
        //         progress:userInfo.sizeUse/(userInfo.sizeMax*1024*1024*1024),
        //         avatar:userInfo.avatar?(/http|https/.test(userInfo.avatar) ? userInfo.avatar : DOMAIN + userInfo.avatar):'/images/icons/files_avatar.png',
        //     })
        // }
    },
    goToEdit(){
        wx.navigateTo({
            url: '/pages/accountSet/accountSet',
        })
    },
    goToPage(e){
        let {index} = e.currentTarget.dataset
        if(index==0){
            wx.navigateTo({
              url: '/pages/link/link?url='+wx.getStorageSync('DOMAIN'),
            })
        }else if(index==1){
            wx.navigateTo({
                url: '/pages/setting/setting',
            })
        }else if(index==2){
            wx.navigateTo({
                url: '/pages/loginLogs/loginLogs',
            })
        }else{
            wx.showModal({
                content:"确定要退出吗？",
                success:res=>{
                    if(res.confirm){
                        wx.removeStorageSync('token')
                        wx.removeStorageSync('userID')
                        wx.removeStorageSync('accountLogin')
                        req("GET",'/api/disk/logout', {}, {}).then(res => {
                          wx.setStorageSync('needRefresh', 1)
                            wx.redirectTo({
                                url: '/pages/accountLogin/accountLogin',
                            })
                        }, err => {
                            console.log(err);
                        });
                    }
                }
            }) 
        }
           
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
        if(wx.getStorageSync('accountLogin')){
            let userInfo = wx.getStorageSync('accountLogin'),DOMAIN = wx.getStorageSync('DOMAIN')
            this.setData({
                userInfo,
                sizeUse:renderSize(userInfo.sizeUse),
                progress:userInfo.sizeUse/(userInfo.sizeMax*1024*1024*1024),
                avatar:userInfo.avatar?(/http|https/.test(userInfo.avatar) ? userInfo.avatar : DOMAIN + userInfo.avatar):'/images/icons/files_avatar.png',
            })
        }
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