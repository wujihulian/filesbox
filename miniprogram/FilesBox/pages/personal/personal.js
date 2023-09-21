// pages/personal/personal.js
import {STATIC_DOMAIN,URL} from '../../utils/config';
import {imageUrl} from '../../utils/image';
import {req} from "../../utils/service";
import{renderSize,timeFormat} from '../../utils/util';
const urls = STATIC_DOMAIN + '/appstatic';
const {globalData} = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Page({

    /**
     * 页面的初始数据
     */
    data: {
        lang:globalData.lang,
        addShow:false,
        loadingState:1,
        // 用于按钮节流
        isClick: true
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
          userInfo.avatar = /http|https/.test(userInfo.avatar) ? userInfo.avatar : DOMAIN + userInfo.avatar,
          this.setData({
              userInfo,
              lang:globalData.lang
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
        //   listArr.push(data.folderList[1].children.folderList[1])
          this.setData({
            moreArr:data.folderList,
            // listArr,
            listArr2:data.folderList.length>2?data.folderList[2].children.folderList:[],
            rootCatalogue:[{
                label:data.folderList[0].children.folderList[1].name,
                sourceID:data.folderList[0].children.folderList[1].sourceID,
            }],
            sourceID:data.folderList[0].children.folderList[1].sourceID,
            catalogue:[{
                label:data.folderList[0].children.folderList[1].name,
                sourceID:data.folderList[0].children.folderList[1].sourceID,
            }],
          })
            let a = {
                detail:{
                    sourceID:data.folderList[0].children.folderList[1].sourceID,
                    catalogue:[{
                        label:data.folderList[0].children.folderList[1].name,
                        sourceID:data.folderList[0].children.folderList[1].sourceID,
                    }]
                }
            }
            this.selectComponent('#files').onCatalogueChange(a) 
            let {scene,forwardMaterials} = wx.getEnterOptionsSync()
            if(scene==1173){
              wx.setStorageSync('forwardMaterials', forwardMaterials)
              // console.log(forwardMaterials)
              if(forwardMaterials[0].type=="text/html"){
                this.showWebview()
                this.setData({
                  webnameText:'新的网址',
                  weburlText:forwardMaterials[0].path
                })
              }else{
                this.selectComponent('#files').getWechat()  
              }
            }else{
              this.closeWebview()
              this.selectComponent('#files').closeWechat()
            }
        }).catch(err => {
            console.log(err)
            wx.showToast({
                title: err.message,
                icon: 'none'
            })
        })
    },
    showWebview(){
      this.setData({
        webviewShow:true,
      })
    },
    closeWebview(){
      this.setData({
        webviewShow:false
      })
    },
    webviewInput(e){
      let {type} = e.target.dataset,{value} = e.detail
      this.setData({
        [type]:value
      })
    },
     // 节流
     throttle() {
      if(this.data.isClick) {
          this.setData({isClick: false});
          setTimeout(()=>{
            this.setData({isClick: true})            
          },3000);
      }else {
          return;        
      }
    },
    saveWebview(){
      this.setData({isClick: false});
      var reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|&|-)+)/g;
      let {weburlText,webnameText} = this.data
      if(!webnameText){
        wx.showToast({
          title: '请输入应用名称',
          icon:'none'
        })
        return
      }
      if(!reg.test(weburlText)||!weburlText){
        wx.showToast({
          title: '请输入正确的网址链接',
          icon:'none'
        })
        return
      }
      let content = {
        type:'url',
        value:weburlText,
        width:'80%',
        height:'70%',
      },param = {
        sourceID:this.data.sourceID,
        operation:'mkfile',
        content: JSON.stringify(content),
        name:webnameText+'.oexe'
      }
      req('POST', URL.diskOperation, {}, param, false).then(data => {
          wx.showToast({
            title: '保存成功',
          })
          this.setData({
            webviewShow:false,
            webnameText:'',
            weburlText:'',
            isClick: true
          })
          let that = this
          setTimeout(function(){
            that.selectComponent('#files').setData({
              newFileCheckedId:data.source.sourceID
            })
            that.selectComponent('#files').getDiskList()
          },500)
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
            let {sourceID,name,icon,labelId,ext} = e.detail.item, {isAddShow} = e.detail,       
            catalogue = [{
                label: name,
                sourceID,
                type:icon,
                labelId:labelId,
                fileType:ext
            }]
            if(labelId){
                catalogue[0].type = 'fileTag'
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
    showAddBox(){
        this.setData({
            addShow:!this.data.addShow
        })
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
          this.setData({
            DOMAIN:wx.getStorageSync('DOMAIN')
          })
          wx.removeStorageSync('needRefresh')
      }
    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {
      this.selectComponent('#files').closeWechat()
      this.closeWebview()
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