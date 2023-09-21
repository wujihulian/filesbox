// pages/setting/setting.js
import {req,uploadFile} from "../../utils/service";
import{renderSize} from '../../utils/util';
import {URL} from '../../utils/config';
const {globalData} = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        lang:globalData.lang,
        languageList:['简体中文','English'],
        sexArr: [
            {
              name: '女',
              index:0,
            },
            {
              name: '男',
              index:1,
            }
        ],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.getHome()
    },  
    getOption(){
        req("GET",'/api/disk/options', {}, {}).then(res => {
            console.log(res.user)
            let account = res.user,DOMAIN = wx.getStorageSync('DOMAIN'),{thirdLoginConfig} = this.data
            account.avatar = /http|https/.test(account.avatar) ? account.avatar : DOMAIN + account.avatar
            wx.setStorageSync('options', res)
            wx.setStorageSync('accountLogin', account)
            console.log(thirdLoginConfig,account)
            thirdLoginConfig.map(c=>{
                if(c.thirdName=='dingding'){
                    c.bind = account.dingOpenId
                    c.name = '钉钉'
                }else if(c.thirdName=='wechat'){
                    c.bind = account.wechatOpenId
                    c.name = '微信'
                }else if(c.thirdName=='enWechat'){
                    c.bind = account.enWechatOpenId
                    c.name = '企业微信'
                }else{
                  c.bind = 0
                  c.name = 'QQ'
              }
            })
            // console.log(thirdLoginConfig)
            this.setData({
                account,
                sizeUse:renderSize(account.sizeUse),
                thirdLoginConfig
            })
        }, err => {
            console.log(err);
        });
    },
    getHome(){
        req("GET",'/api/disk/home', {}, {}).then(res => {
            let thirdLoginConfig = JSON.parse(res.thirdLoginConfig)
            this.setData({
                thirdLoginConfig
            })
        }, err => {
            console.log(err);
        });
    },
    // 监听：选择图片
    choseImage: function (e) {
        let {account} = this.data
        wx.chooseImage({
            count: 1,
            success: res => {
                this.uploadImage(res.tempFilePaths[0])
            }
        })
    },
    uploadImage(image){
        uploadFile(URL.uploadFile,image, {
            busType: 'avatar'
        }).then(data => {
            this.setUserInfo('avatar',data.path)
        }).catch(err => {
            console.log(err)
        })
    },
    showSex(){
        this.setData({
            sexShow:!this.data.sexShow
        })
    },
    onSelect(e) {
        console.log(e.detail);
        this.setUserInfo('sex',e.detail.index)
    },
    setUserInfo(key,value){
        let param = {
            key:key,
            value:value
        }
        req("POST",URL.setUserInfo,{} , param).then(res => {
            wx.showToast({
              title: '操作成功',
              icon:'none'
            })
            this.getOption()
        }, err => {
            console.log(err);
        });
    },
    goToSpace(){
        wx.navigateTo({
            url:'/pages/userSpace/userSpace'
        })
    },
    goToPage(e){
        let {type,title} = e.currentTarget.dataset
        wx.navigateTo({
            url:'/pages/setUserInfo/setUserInfo?type='+type+'&title='+title
        })
    },
    goToBind(){
        wx.showModal({
            title:'温馨提示',
            content: '小程序暂不支持绑定\取消绑定，请到WEB或APP进行相应操作。',
            showCancel: false,
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
        this.getOption()
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