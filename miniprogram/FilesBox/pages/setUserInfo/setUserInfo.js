// pages/setting/setting.js
import languageUtils from '../../utils/languageUtils';
import {req} from "../../utils/service";
import {URL} from '../../utils/config';
import {encrypt} from "../../utils/util";
const {globalData} = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        lang:globalData.lang,
        languageList:['简体中文','English'],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        let {type,title} = options
        wx.setNavigationBarTitle({
            title: decodeURIComponent(title),
        })
        this.setData({
            type,
            title
        })
        this.getOption()
    },
    getOption(){
        req("GET",'/api/disk/options', {}, {}).then(res => {
            let account = res.user,{type} = this.data
            this.setData({
                value:account[type]
            })
        }, err => {
            console.log(err);
        });
    },
    onChange(e){
        this.setData({
            value:e.detail
        })
    },
    convertEye(e){
        let {type} = e.target.dataset,showText = ''
        if(type=='newPassword'){
            showText = 'newShow'
        }else if(type=='oldPassword'){
            showText = 'oldShow'
        }else{
            showText = 'againShow'
        }
        this.setData({
            [showText]:!this.data[showText]
        })
    },
    inputPassword(e){
        let {type} = e.target.dataset
        this.setData({
            [type]:e.detail
        })
    },
    forgetPassword(){
        wx.showToast({
            title: '请联系系统管理员重置密码',
            icon:'none'
          })
        return
    },
    setUserInfo(){
        let {type,value,title,oldPassword,newPassword,againPassword} = this.data,param = {
            key:type,
            value:value
        },emailReg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,phoneReg = /^1[3456789]\d{9}$/
        if(value==''){
            wx.showToast({
                title: '请输入'+title,
                icon:'none'
              })
            return
        }
        if(type=='email'){
            if (!emailReg.test(value)){
                wx.showToast({
                    title: '请输入有效的邮箱',
                    icon:'none'
                  })
                return
            }
        }
        if(type=='phone'){
            if (!phoneReg.test(value)){
                wx.showToast({
                    title: '请输入有效的手机号',
                    icon:'none'
                  })
                return
            }
        }
        if(type=='password'){
            if(oldPassword&&newPassword&&againPassword){
                if(newPassword.length<6){
                    wx.showToast({
                        title: '密码不能少于6位数',
                        icon:'none'
                      })
                    return
                }
                if(newPassword!=againPassword){
                    wx.showToast({
                        title: '两次密码不一致！',
                        icon:'none'
                      })
                    return
                }
                param.password = encrypt(oldPassword)
                param.value = encrypt(newPassword)
            }else{
                if(!oldPassword){
                    wx.showToast({
                        title: '请输入原密码',
                        icon:'none'
                      })
                    return
                }else{
                    if(!newPassword){
                        wx.showToast({
                            title: '请输入新密码',
                            icon:'none'
                          })
                        return
                    }else{
                        if(!againPassword){
                            wx.showToast({
                                title: '请再次输入密码',
                                icon:'none'
                              })
                            return
                        }
                    }
                }
            }    
        }
        req("POST",URL.setUserInfo,{} , param).then(res => {
            if(type=='password'){
                wx.showToast({
                    title: '修改密码成功！请重新登录系统！',
                    icon:'none'
                })
                setTimeout(function(){
                    wx.removeStorageSync('token')
                    wx.removeStorageSync('userID')
                    wx.removeStorageSync('accountLogin')
                    req("GET",'/api/disk/logout', {}, {}).then(res => {
                        wx.reLaunch({
                            url: '/pages/accountLogin/accountLogin',
                        })
                    }, err => {
                        console.log(err);
                    });
                },500)     
            }else{
                wx.showToast({
                    title: '操作成功',
                    icon:'none'
                })
                wx.navigateBack({
                    delta:1
                })
            }    
        }, err => {
            wx.showToast({
                title: err.message,
                icon:'none'
              })
            console.log(err);
        });
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
})