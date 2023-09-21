// pages/setting/setting.js
import {req} from "../../utils/service";
import {URL} from '../../utils/config';
import {timeFormat,getDateStr} from '../../utils/util';
const {globalData} = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        lang:globalData.lang,
        navList:['名称','类型','最近登录时间','IP地址'],
        currentPage:1,
        timeFrom:getDateStr(null,-7),
        timeTo:getDateStr(null,0),
        timeIndex:2
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.setTime()
        this.getLogList()
    },
    setTime(){
        let timeArr = [
            {
                text:'今天',
                time:this.getDateStr(null,0)
            },
            {
                text:'昨天',
                time:this.getDateStr(null,-1)
            },
            {
                text:'近7天',
                time:this.getDateStr(null,-7)
            }, 
            {
                text:'近30天',
                time:this.getDateStr(null,-30)
            },
            {
                text:'自定义',
                time:''
            }
        ]
        this.setData({
            timeArr,
            minDate:new Date(2023, 0, 1).getTime(),
            maxDate: new Date().getTime(),
            timestamp:[this.data.timeFrom,this.data.timeTo]
        })
    },
    getLogList(reachBottom = false){
        let param = {
            currentPage: this.data.currentPage,
            pageSize: 20,
            logType:'user.index.loginSubmit',
            timeFrom: this.data.timeFrom,
            timeTo: this.data.timeTo,
            userID:wx.getStorageSync('userID')
        }
        req("GET",URL.getLogList, param, {}).then(res => {
            console.log(res.list)
            res.list.map(c=>{
                c.createTime = timeFormat('yyyy-MM-dd hh:mm:ss',c.createTime*1000);
            })
            this.setData({
                logList:reachBottom?[...this.data.logList,...res.list]:res.list
            })
        }, err => {
            console.log(err);
        });
    },
    selectNav(e){
        let {index} = e.currentTarget.dataset,{timeArr} = this.data
        this.setData({
            timeIndex:index
        })
        if(index==4){
            this.showDate()
        }else{
            this.setData({
                timeFrom:timeArr[index].time,
                timeTo:index==1?timeArr[index].time:timeArr[0].time,
                currentPage:1
            })
            this.getLogList()
        }
        // if
    },
    showDate(){
        this.setData({dateShow:!this.data.dateShow})
    },
    formatDate(date) {
        date = new Date(date);
        return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
    },
    selectDate(e){
        this.setData({
            timeFrom:this.formatDate(e.detail[0]),
            timeTo:this.formatDate(e.detail[1]),
            dateShow:false,
            currentPage:1
        })
        this.getLogList()
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
        this.setData({currentPage:this.data.currentPage+1})
        this.getLogList(true)
    },
    getDateStr
})