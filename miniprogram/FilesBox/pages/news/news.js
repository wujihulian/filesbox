// pages/messages/messages.js
import {req} from "../../utils/service";
import {URL} from '../../utils/config';
import { imageUrl } from '../../utils/image'; 
import{timeFromNow,clipImage} from '../../utils/util';
var QRCode = require('../../utils/qrcode.js');
let qrcode = null;
const {globalData} = getApp();
let leftHeight = 0, rightHeight = 0; //分别定义左右两边的高度
let query,timers={};
Page({

    /**
     * 页面的初始数据
     */
    data: {
      lang:globalData.lang,
      loadingState: 1,
      currentPage:1,
      systemInfo:globalData.systemInfo,
      imageUrl,
      leftList:[], 
      rightList:[],
      keyword:'',
      filterItems: [], //资讯\短视频三级分类
      threeIndexs: {
          one: '',
          two: '',
          three: ''
      },
      threeIds: {
          one: '',
          two: '',
          three: ''
      },
      infoTypeID:'',
      sortShow:false,
      sharePanel: [
        { name: '微信', icon: imageUrl.shareWechat, openType: 'share' },
        { name: '朋友圈', icon: imageUrl.shareWechatMoments },
        { name: '复制链接', icon: imageUrl.shareConnection },
        { name: '二维码', icon: imageUrl.shareQRcode }
      ],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
      if(options.url){
        wx.redirectTo({
          url:`/pages/link/link?url=${options.url}&needToken=1`
        })
        return
      }
      let { systemInfo} = this.data,scrollHeight;
      scrollHeight = systemInfo.windowHeight - systemInfo.statusBarHeight - 44
      // console.log(scrollHeight)
    //   console.log(wx.getStorageSync('options').role)
      this.setData({
        scrollHeight,
        informationView:wx.getStorageSync('options').role['explorer.informationView'],
        informationShare:wx.getStorageSync('options').role['explorer.share'],
      })
      this.getInfoTypeList()
      this.getNewsList()
    },
    async isLeft(goods,dataType) {
	    let list = goods,
	      leftList = this.data.leftList,
	      rightList = this.data.rightList;
        query = wx.createSelectorQuery().in(this)
	    // 倒计时更新数据列表状态
	    if (dataType == 'djs') {
	      await this.getBoxHeight(leftList, rightList);
	    }
	    else{
	      for (const item of list) {
	        leftHeight <= rightHeight ? leftList.push(item) : rightList.push(item); //判断两边高度，来觉得添加到那边
	        await this.getBoxHeight(leftList, rightList);
	      }
        }
    },
    getBoxHeight(leftList, rightList) { //获取左右两边高度
        return new Promise((resolve, reject) => {
          this.setData({ 
            leftList, 
            rightList 
          }, () => {
            query.select('#left').boundingClientRect();
            query.select('#right').boundingClientRect();
            query.exec((res) => {
              if (res[0] ){
                leftHeight = res[0].height; //获取左边列表的高度
                rightHeight = res[1].height; //获取右边列表的高度
              }
              resolve();
            });
          });
        })
    },
    getNewsList(reachBottom = false){
        let DOMAIN = wx.getStorageSync('DOMAIN'),{keyword} = this.data
        let param = {
            keyword: keyword,
            currentPage: this.data.currentPage,
            pageSize:10,
            sourceID: 0,
            isinfo: 1,
            status: 1,     
        }
        if(this.data.infoTypeID)param.infoTypeID = this.data.infoTypeID
        req("GET",URL.getInfoList, param, {}).then(data => {
            // console.log(data.list)
            data.list.map(c=>{
                c.thumb = c.thumb?(/http|https/.test(c.thumb) ? c.thumb : DOMAIN + c.thumb):'';
                c.gmtTime = timeFromNow(c.modifyTime*1000)
            })
            if(!this.data.isTile){
                let resArr = [];
                for(let i in data.list){
                    resArr.push(data.list[i])
                }
                this.isLeft(resArr, '')
            }
            this.setData({          
                loadingState: 0,
                newsList:reachBottom?[...this.data.newsList,...data.list]:data.list
            })
        }, err => {
            console.log(err);
        });
    },
    getInfoTypeList(reachBottom = false){
        let param = {
            currentPage: 1,
            pageSize: 500,
            fileType:"folder"
        }
        req("GET",URL.getInfoTypeList, param, {}).then(data => {
            // console.log(data)
            let obj = {
                infoTypeID:'',
                typeName:'全部',
                children:[]
            }
            data.unshift(obj)
            this.setData({
                filterItems:data
            })
        }, err => {
            showErrMsg(err);
        });
    },
    showFilter(){
        this.setData({
            sortShow:!this.data.sortShow
        })
    },
    choseCate: function (e) {
        // console.log(e)
        let {
            type,
            index,
            id
        } = e.currentTarget.dataset, {
            threeIndexs,
            threeIds
        } = this.data;
        threeIndexs[type] = index;
        threeIds[type] = id;
        if (type == 'one') {
            threeIndexs.two = '';
            threeIndexs.three = '';
            threeIds.two = '';
            threeIds.three = '';
        }
        if (type == 'two') {
            threeIndexs.three = '';
            threeIds.three = '';
        }
        this.setData({
            threeIndexs,
            threeIds,
            infoTypeID:id,
            currentPage:1,
            leftList:[],
            rightList:[]
        })
        this.getNewsList()
    },
    choseType(e){
        let {type} = e.currentTarget.dataset
        this.setData({
            isTile:type==1?1:0,
        })
    },
    searchFiles(e){
        let keyword = e.detail.value
        this.setData({
            currentPage:1,
            keyword,
            leftList:[],
            rightList:[]
        })
        this.getNewsList()
    },
    goToInfoDetail(e){
        let {infoID,infoUrl} = e.currentTarget.dataset.item,DOMAIN = wx.getStorageSync('DOMAIN'),{informationView} = this.data,
        url = DOMAIN +'/pubinfo/'+infoID+'.shtml'
        if(!informationView){
          wx.showToast({
            title: '暂无权限',
            icon:'none'
          })
          return
        }
        if(infoUrl){
            wx.showModal({
              title: '小程序暂不支持打开外链资讯，请复制链接在浏览器打开',
              confirmText:'复制',
              complete: (res) => {         
                if (res.confirm) {
                    console.log(infoUrl)
                    wx.setClipboardData({
                        data: infoUrl,
                        success:res => {
                            wx.showToast({
                                title:'复制成功'
                            });
                        }
                    })
                }
              }
            })
            return
        }
        wx.setStorageSync('needRefresh', 1)
        wx.navigateTo({
          url: '/pages/newsLink/newsLink?url='+url+'&needToken=1'
        })
        wx.setStorageSync('news',e.currentTarget.dataset.item)
    },
    getQrcode(){
      let {shareLink} = this.data,token = encodeURIComponent(wx.getStorageSync('token')),
      str = shareLink
      qrcode = new QRCode('myQrcode', {
        usingIn: this,
        text: str,
        width: 200,
        height: 200,
        padding: 20,
        colorDark: "#000000",
        colorLight: "#ffffff",
        correctLevel: QRCode.CorrectLevel.H,
      })
      setTimeout(() => {
        wx.canvasToTempFilePath({
          canvasId: 'myQrcode',
          success:res=>{
            this.setData({
              shareQrcode:res.tempFilePath
            })
          },fail(err){
            console.log(err)
          }
        })
      }, 1000);
    },
    showShare(e){
      let {infoID,title,thumb} = e.currentTarget.dataset.item,DOMAIN = wx.getStorageSync('DOMAIN'),
        url = DOMAIN +'/pubinfo/'+infoID+'.shtml',that = this
      wx.getImageInfo({
        src: thumb?thumb:this.data.imageUrl.newsEmpty,  // 这里填写网络图片路径 
        success: (res) => {
          // 这个是我封装的裁剪图片方法（下面将会说到）
          that.clipImage(res.path, res.width, res.height, (img) => {
            that.setData({
              shareImage:img
            })
            // console.log(img);  // img为最终裁剪后生成的图片路径，我们可以用来做为转发封面图
          }); 
        }
      });

      this.setData({
        shareShow:true,
        shareLink:url,
        shareTitle:title,
        shareCover:thumb?thumb:this.data.imageUrl.newsEmpty
      })
      this.getQrcode()
    },
    closeShare(){
      this.setData({
        shareShow:false
      })
    },
    onSelect(e){
      let {index} = e.currentTarget.dataset
      if(index == 1){
        this.setData({
            shareShow:false,
            showTips:true
        })
      }else if(index==2){
          this.copyLink()
      }else if(index==3){
        this.setData({
          shareShow:false,
          showQrcode:true
        })
      }
    },
    showTips(){
      this.setData({
        showTips:false
      })
    },
    closeShareQrcode(){
      this.setData({
        showQrcode:false,
        shareShow:false
      })
    },
    saveShareQrcode:function(e){
        wx.saveImageToPhotosAlbum({
          filePath: this.data.shareQrcode,
          success: () => {
            wx.showToast({
              title: '保存成功！',
            })
            this.closeShareQrcode();
          },
          fail:res=>{
            openSaveImageSetting();
          }
        });
    },
    copyLink(){
      let {shareLink} = this.data,token = encodeURIComponent(wx.getStorageSync('token')),
      str = shareLink
      wx.setClipboardData({
          data: str,
          success:res => {
            wx.showToast({
                title:'复制成功'
            });
            this.setData({
                shareShow:false
            })
          }
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
        currentPage:1,
        leftList:[],
        rightList:[]
      })
      // this.getNewsList()
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
        if(!this.data.isTile){
            this.setData({
                leftList:[],
                rightList:[]
            })
        }
        this.getNewsList()
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
        this.getNewsList(true)
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function (e) {
      console.log(e)
      if(e.from=='menu'){
        return{
            path:`/pages/news/news`,
            title:'资讯列表',
            imageUrl:this.data.imageUrl.newsEmpty
        }
      }
      let {shareTitle,shareImage} = this.data
      this.setData({
          shareShow:false
      })
      let path = `/pages/link/link?url=${this.data.shareLink}&needToken=1`;
      return{
          path,
          title:shareTitle,
          imageUrl:shareImage
      }
    },
    onShareTimeline:function (e) {
      let path = `url=${this.data.shareLink}`;
      let {shareTitle,shareCover} = this.data
      if(e.from=='menu'&&!shareTitle){
        return{
          path:`/pages/news/news`,
          title:'资讯列表',
          imageUrl:this.data.imageUrl.newsEmpty
        }
      }
      return{
        query:path,
        title:shareTitle,
        imageUrl:shareCover
      }
    },clipImage
})