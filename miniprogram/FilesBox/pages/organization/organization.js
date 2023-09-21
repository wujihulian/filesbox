// pages/organization/organization.js
import {req} from "../../utils/service";
import {STATIC_DOMAIN,URL} from '../../utils/config';
import{renderSize} from '../../utils/util';
const {globalData} = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Page({

    /**
     * 页面的初始数据
     */
    data: {
        lang:globalData.lang,
        loadingState:1
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        if(!wx.getStorageSync('accountLogin')){
            wx.redirectTo({
              url: '/pages/accountLogin/accountLogin',
            })
        }else{
            let userInfo = wx.getStorageSync('accountLogin')
            userInfo.progress = userInfo.sizeUse/(userInfo.sizeMax*1024*1024*1024),
            userInfo.sizeUse = renderSize(userInfo.sizeUse),
            this.setData({
                userInfo,
            })
            this.setData({
                lang:globalData.lang,
            })
            this.selectComponent('#tabNav').getRecycle()
            this.selectComponent('#tabNav').getFavNum()
            this.selectComponent('#tabNav').getNoticeNum()
            this.getPath()
        }
    },
    getPath(){
        req('GET', URL.getPath, {block:'root'}, {}, false).then(data => {
          data.folderList.map(c=>{
            if(c.children.folderList.length>0){
              c.show = true
              c.children.folderList.map(o=>{
                if(o.icon){
                  if(o.icon=='box'){
                    o.url = '/images/rootIcon/folder.png'
                  }else{
                    o.url = '/images/rootIcon/'+o.icon+'.png'
                  }
                }  
              })
            }
          })
        //   let listArr = []
        //   listArr.push(data.folderList[0].children.folderList[0])
        //   listArr.push(data.folderList[1].children.folderList[0])
        //   listArr.push(data.folderList[1].children.folderList[2])
          this.setData({
            moreArr:data.folderList,
            // listArr,
            listArr2:data.folderList[2].children.folderList,
            rootCatalogue:[{
                label:'共享',
                sourceID:0,
                type:'files'
            }],
            sourceID:0,
            catalogue:[{
                label:'共享',
                sourceID:0,
                type:'files'
            }],
          })
          let a = {
                detail:{
                    sourceID:0,
                    catalogue:[{
                        label:'共享',
                        sourceID:0,
                        type:'files'
                    }]
                }
            }
            this.selectComponent('#files').onCatalogueChange(a) 
        }).catch(err => {
            console.log(err)
            wx.showToast({
                title: err.message,
                icon: 'none'
            })
        })
    },
    choseType(){
        this.selectComponent("#files").choseType()
    },
    choseSortField(e){
        let {sortField} = e.detail
        this.selectComponent("#files").choseSortField(sortField)
    },
    choseSortType(e){
        let {sortType} = e.detail
        this.selectComponent("#files").choseSortType(sortType)
    },
    searchFiles(e){
        let {keyword} = e.detail
        this.selectComponent("#files").onSearch(keyword)
    },
    setSearchFilter(e){
        this.setData({
            sourceID:e.detail.sourceID,
            catalogue:e.detail.catalogue
        })
    },
    filterFile(e){
        let {searchFilter} = e.detail,catalogue = [{label:'全部',sourceID:0}]
        searchFilter.catalogue = searchFilter.sourceID==0?catalogue:this.data.catalogue
        this.selectComponent("#files").onFilter(searchFilter)
    },
    showSlide(e){
        this.setData({
            slideShow:e.detail.slideShow,
        })
        this.selectComponent('#tabNav').setIndex() 
        this.getList(e)
    },
    getList(e){
        if(e.detail.item){
            let {sourceID,name,icon,labelId,ext} = e.detail.item, {isAddShow} = e.detail,{catalogue} = this.data      
            if(labelId){
                catalogue[0].type = 'fileTag'
            }
            if(sourceID){
                catalogue = [{
                    label:'共享',
                    sourceID:0,
                    type:'files'
                },{
                    label: name,
                    sourceID,
                    type:icon,
                    labelId:labelId,
                    fileType:ext
                }]
            }else{
                catalogue = [{
                    label: name,
                    sourceID,
                    type:icon,
                    labelId:labelId,
                    fileType:ext
                }]
            }
            let a = {
                detail:{
                    sourceID,
                    catalogue,
                    isAddShow
                }
            }
            this.setData({sourceID,catalogue})
            this.selectComponent("#tabNav").resetFile(1)
            this.selectComponent('#files').onCatalogueChange(a) 
        }
        if(e.detail.indexSet){
            this.selectComponent('#slideBar').setIndex() 
        }
    },
    getRecycleNum(){
        this.selectComponent('#tabNav').getRecycle()
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
        if(this.data.sourceID&&wx.getStorageSync('needRefresh')){
            this.selectComponent('#tabNav').getRecycle()
            this.selectComponent('#tabNav').getNoticeNum()
            this.getPath()
            // this.selectComponent("#files").getDiskList()
            wx.removeStorageSync('needRefresh')
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