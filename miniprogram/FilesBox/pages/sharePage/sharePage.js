// pages/sharePage/sharePage.js
import {req} from '../../utils/service';
import {URL} from '../../utils/config';
import {encrypt,randomNumber,isEmptyObject,getTimetamp,timeFormat,openSaveImageSetting} from "../../utils/util";
import {imageUrl} from '../../utils/image';
var QRCode = require('../../utils/qrcode.js');
let qrcode = null;
var datePicker = require('../../utils/dateSetting.js')
//设定当前的时间，将其设定为常量
const date = new Date();
const year = date.getFullYear();
const month = date.getMonth() + 1;
Page({

    /**
     * 页面的初始数据
     */
    data: {
        imageUrl,
        shareShow:false,
        timeShow:false,
        downShow:false,
        customTime: '',
        multiArray: [],
        multiIndex: [0, 0, 0, 0, 0],
        choose_year: "",
        sharePanel: [
            { name: '微信', icon: imageUrl.shareWechat, openType: 'share' },
            { name: '朋友圈', icon: imageUrl.shareWechatMoments },
            { name: '复制链接', icon: imageUrl.shareConnection },
            { name: '二维码', icon: imageUrl.shareQRcode }
        ],
        timeArr:[
            {
                name:'7天',
            },
            {
                name:'14天',
            },
            {
                name:'永久有效',
            },
            {
                name:'设定到期时间',
            }
        ],
        downArr:[
            {
                name:'不限制',
            },
            {
                name:'禁止下载',
            },
            {
                name:'自定义下载次数',
            },
        ],
        selectTimeIndex:2,
        downIndex:0,
        downNum:0,
        moreShow:true,
        isShare:0
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        if(options.shareHash){
          if(wx.getEnterOptionsSync().scene==1154){
            this.setData({
              isShare: 1,
            })
          }else if(wx.getEnterOptionsSync().scene==1155){
            this.setData({
              isShare: 2,
            })
          }
          console.log(options.shareHash)
          wx.redirectTo({
            url:`/pages/link/link?shareHash=${encodeURIComponent(options.shareHash)}&DOMAIN=${options.DOMAIN}`
          })
          return
        }
        // let dataList = JSON.parse(decodeURIComponent(options.item))
        let dataList = wx.getStorageSync('shareFile')
        let DOMAIN = wx.getStorageSync("DOMAIN"),userID = wx.getStorageSync('userID'),
        shareHash = encodeURIComponent(encrypt(dataList.sourceID+'&&'+userID)),
        shareLink = DOMAIN+'/#/sharePage/'+shareHash
        let years = [year]
        for(var i = 0;i<100;i++){
            years.push(year+i)
        }
        this.setData({
            dataList:dataList,
            shareHash,
            shareLink,
            title:dataList.name,
            timeToText:'永久有效',
            multiArray:
            [
                years,
                datePicker.determineMonth(),
                datePicker.determineDay(year, month),
                datePicker.determineHour(),
                datePicker.determineMinute()
            ],
            customTime:this.timeFormat('yyyy-MM-dd hh:mm',Date.parse(new Date())),
            shareLinkAllowGuest:wx.getStorageSync('options').markConfig.shareLinkAllowGuest
        })
        this.getShareInfo(dataList.sourceID)
    },
    //最后呈现时间的函数。
    bindMultiPickerChange: function (e) {
        //年
        var dateStr = this.data.multiArray[0][this.data.multiIndex[0]] +"-";
        //月
        if(this.data.multiArray[1][this.data.multiIndex[1]]<10)
        {
            dateStr=dateStr+"0"+this.data.multiArray[1][this.data.multiIndex[1]] +"-";
        }
        else{
            dateStr=dateStr+this.data.multiArray[1][this.data.multiIndex[1]] +"-";
        }
        //日
        if(this.data.multiArray[2][this.data.multiIndex[2]]<10)
        {
            dateStr=dateStr+"0"+this.data.multiArray[2][this.data.multiIndex[2]]+" ";
        }
        else{
            dateStr=dateStr+this.data.multiArray[2][this.data.multiIndex[2]]+" ";
        }
        //时
        if(this.data.multiArray[3][this.data.multiIndex[3]]<10)
        {
            dateStr=dateStr+"0"+this.data.multiArray[3][this.data.multiIndex[3]]+":";
        }
        else
        {
            dateStr=dateStr+this.data.multiArray[3][this.data.multiIndex[3]]+":";
        }
        if(this.data.multiArray[4][this.data.multiIndex[4]]<10)
        {
            dateStr=dateStr+"0"+this.data.multiArray[4][this.data.multiIndex[4]];
        }
        else{
            dateStr=dateStr+this.data.multiArray[4][this.data.multiIndex[4]];
        }
        this.setData({
            customTime: dateStr,
        })
    },
    //当时间选择器呈现并进行滚动选择时间时调用该函数。
    bindMultiPickerColumnChange: function (e) {
        //e.detail.column记录哪一行发生改变，e.detail.value记录改变的值（相当于multiIndex）
        switch (e.detail.column) {
        //这里case的值有0/1/2/3/4,但除了需要记录年和月来确定具体的天数外，其他的都可以暂不在switch中处理。
        case 0:
            //记录改变的年的值
            let year = this.data.multiArray[0][e.detail.value];
            this.setData({
                // choose_year: year.substring(0, year.length - 1),
                choose_year: year,
            })
            break;
        case 1:
            //根据选择的年与月，确定天数，并改变multiArray中天的具体值
            let month = this.data.multiArray[1][e.detail.value];
            let dayDates = datePicker.determineDay(this.data.choose_year, month.substring(0, month.length - 1));
            //这里需要额外注意，改变page中设定的data，且只要改变data中某一个值，可以采用下面这种方法
            this.setData({
            ['multiArray[2]']: dayDates
            })
            break;
        }
        //同上，上面改变的是二维数组中的某一个一维数组，这个是改变一个一维数组中某一个值，可供参考。
        this.setData({
            ["multiIndex[" + e.detail.column + "]"]: e.detail.value
        })
    },
    getRandomNum(){
        this.setData({
            password:randomNumber(6)
        })
    },
    getShareInfo(sourceID){
        let {dataList} = this.data
        req('GET',URL.getShareInfo, {sourceID:sourceID}, {}, false).then(data => {
            // console.log(data)
            if(!isEmptyObject(data)){
                if(data.options){
                    data.options = JSON.parse(data.options)
                }
                dataList.timeTo = data.timeTo>0?timeFormat('yyyy/MM/dd hh:mm:ss',data.timeTo*1000):'永久有效';
                dataList.options = data.options
                // dataList.title = data.title
                this.setData({
                    numDownload:data.numDownload,
                    numView:data.numView,
                    shareID:data.shareID,
                    password:data.password?data.password:'',
                    timeToText:dataList.timeTo
                    // shareHash:data.shareHash
                })
            }else{
                dataList.options = {
                    preview:1,
                    down:1,
                    login:0,
                    downNum:0
                }
            }
            if(this.data.shareLinkAllowGuest==0){
                dataList.options.login = 1
            }
            this.setData({dataList})
        })
    },
    showMore(){
        this.setData({
            moreShow:!this.data.moreShow
        })
    },
    onSwitchChange(e ){
        let{label} = e.currentTarget.dataset,{dataList} = this.data
        if(label=='preview'||label=='down'){
            dataList.options[label] = e.detail?0:1
        }else{
            dataList.options[label] = e.detail?1:0
        }
        this.setData({
            dataList
        })
    },
    closeShare(){
        this.setData({
            shareShow:false
        })
    },
    showTime(){
        this.setData({
            timeShow:!this.data.timeShow
        })
    },
    selectTime(e){
        let {index} = e.currentTarget.dataset
        this.setData({
            selectTimeIndex:index,
        })
    },
    timeConfirm(){
        let {selectTimeIndex,customTime,timeArr} = this.data,timeTo
        if(selectTimeIndex==0){
            timeTo = this.getTimetamp(7)
        }else if(selectTimeIndex==1){
            timeTo = this.getTimetamp(14)
        }else if(selectTimeIndex==2){
            timeTo = 0
        }else{
            timeTo = Date.parse(customTime)/1000
        }
        this.setData({
            timeTo,
            timeToText:selectTimeIndex<3?timeArr[selectTimeIndex].name:customTime,
            timeShow:false
        })
    },
    showDown(){
        this.setData({
            downShow:!this.data.downShow
        })
    },
    selectDown(e){
        let {index} = e.currentTarget.dataset
        this.setData({
            downIndex:index,
        })
    },
    customDownNum(e){
        this.setData({
            downNum:parseInt(e.detail.value),
            downIndex:2
        })
    },
    downConfirm(){
        let {downIndex,dataList,downNum} = this.data
        if(downIndex==0){
            dataList.options.down = 1
            dataList.options.downNum = 0
        }else if(downIndex==1){
            dataList.options.down = 0
            dataList.options.downNum = 0
        }else if(downIndex==2){
            dataList.options.down = 1
            dataList.options.downNum = downNum
        }
        this.setData({
            dataList,
            downShow:false
        })
    },
    cancelShare(){
        let{shareID} = this.data
        if(shareID){
            req('POST',URL.cancelShare, {}, {shareID:shareID}, false).then(data => {
                wx.navigateBack({
                  delta: 0,
                })   
            })
        }else{
            wx.navigateBack({
                delta: 0,
            }) 
        }
    },
    showShare(e){
        let {dataList,password,shareHash,title,timeTo} = this.data,{type} = e.currentTarget.dataset,param = {
            options:dataList.options?JSON.stringify(dataList.options):'',
            password:password,
            shareHash: decodeURIComponent(shareHash),
            sourceID: dataList.sourceID,
            timeTo: timeTo,
            title: title
        }
        req('POST',URL.saveShare, {}, param, false).then(data => {
            qrcode = new QRCode('myQrcode', {
                usingIn: this,
                text: this.data.shareLink,
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
            wx.setStorageSync('needRefresh', true)
            if(type==0){
                this.setData({
                    shareShow:true
                })
            }else if(type==1){
                this.copyLink()
            }else{
                wx.showToast({
                  title: '保存成功！',
                  icon:'none'
                })
                this.getShareInfo(this.data.dataList.sourceID)
            }     
        })
    },
    inputValue(e){
        let {value} = e.detail,{label} = e.currentTarget.dataset
        this.setData({
            [label]:value
        })
    },
    showTips(){
      this.setData({
        showTips:false
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
                showQrcode:true
            })
        }
    },
    closeShareQrcode(){
        this.setData({
            showQrcode:false
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
        let {shareLink,password,title} = this.data,
        str = password?'分享名称：'+title+'\n分享链接：'+shareLink+'\n分享密码：'+password:'分享名称：'+title+'\n分享链接：'+shareLink
        wx.setClipboardData({
            data: str,
            success:res => {
                wx.showToast({
                    title:'复制成功'
                });
                this.setData({
                    shareShow:false
                })
                this.getShareInfo(this.data.dataList.sourceID)
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
    onShareAppMessage: function () {
        let {name,thumb} = this.data.dataList,DOMAIN = wx.getStorageSync('DOMAIN')
        this.setData({
            shareShow:false
        })
        let path = `/pages/link/link?shareHash=${encodeURIComponent(this.data.shareHash)}&DOMAIN=${wx.getStorageSync('DOMAIN')}`;
        this.getShareInfo(this.data.dataList.sourceID)
        console.log(path)
        return{
            path,
            title:'好友给你分享了'+name,
            imageUrl:thumb
        }
    },
    onShareTimeline:function (params) {
      let path = `shareHash=${this.data.shareHash}&DOMAIN=${wx.getStorageSync('DOMAIN')}`,{name,thumb} = this.data.dataList;
      this.getShareInfo(this.data.dataList.sourceID)
      console.log(path)
      return{
        query:path,
        title:'好友给你分享了'+name,
        imageUrl:thumb
      }
    },
    getTimetamp,timeFormat
})