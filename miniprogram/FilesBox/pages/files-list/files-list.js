// pages/personal/personal.js
import {STATIC_DOMAIN} from '../../utils/config';
import {imageUrl} from '../../utils/image';
import {req} from "../../utils/service";
const urls = STATIC_DOMAIN + '/appstatic';
const {globalData} = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Page({

    /**
     * 页面的初始数据
     */
    data: {
        lang:globalData.lang,
        statusBarHeight:globalData.systemInfo.statusBarHeight,
        addArr:[
            {
                url:urls+'/images/video_files_icon.png',
                text:'视频'
            },{
                url:urls+'/images/pictures_files_icon.png ',
                text:'图片'
            },{
                url:urls+'/images/word_files_icon.png',
                text:'文档'
            },{
                url:imageUrl.folder_icon,
                text:'文件夹'
            },
        ],
        isAddShow:false,
        sourceID:0,
        showList:[
            {
                image:'/images/icons/reduction.png',
                name:'还原所有'
            },
            {
                image:'/images/icons/empty.png',
                name:'清空回收站'
            }
        ],
        morelListShow:false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        let item = JSON.parse(options.item)
        let {name,type,fileType} = item,
        catalogue = [{
            label: name,
            sourceID:0,
            type:type,
            fileType:fileType
        }]
        let a = {
            detail:{
                sourceID:0,
                catalogue,
                isAddShow:false
            }
        }
        this.setData({
            catalogue,
            title:name,
            showRecyle:type=='recycle'?true:false
        })
        if(type!='news'){
            this.selectComponent('#files').onCatalogueChange(a) 
        }
    },
    //回收站显示更多
    showMoreRecyle(){
        this.setData({
            morelListShow:!this.data.morelListShow
        })
    },
    choseType(e){
        let {index} = e.currentTarget.dataset
        this.setData({
            morelListShow:!this.data.morelListShow
        })
        if(index==0){
            this.selectComponent('#files').reductionFile({isAll:1})
        }else{
            this.selectComponent('#files').removeFile({isAll:1})
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