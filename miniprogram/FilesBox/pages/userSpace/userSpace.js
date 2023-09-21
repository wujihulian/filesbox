// pages/setting/setting.js
import languageUtils from '../../utils/languageUtils';
import{renderSize} from '../../utils/util';
import {req} from "../../utils/service";
import {URL} from '../../utils/config';
const {globalData} = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        lang:globalData.lang,
        languageList:['简体中文','English'],
        messarr: [{
            color: '#464af8',
            num: '20',
            flownum: '20',
        },
        {
            color: '#ff6262',
            num: '50',
            flownum: '50',
        },
        {
            color: '#f7c11b',
            num: '60',
            flownum: '60',
        },
        {
            color: '#ff8015',
            num: '80',
            flownum: '80',
        },
        {
            color: '#17d0bc',
            num: '20',
            flownum: '20',
        }]
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.getOption()
        this.getUserProportion()
    },
    getOption(){
        req("GET",'/api/disk/options', {}, {}).then(res => {
            let account = res.user
            console.log(account.sizeMax)
            this.setData({
                sizeUse:renderSize(account.sizeUse),
                progress:account.sizeUse/(account.sizeMax*1024*1024*1024),
                account
            })
        }, err => {
            console.log(err);
        });
    },
    getUserProportion(){
        req("GET",'/api/disk/userProportion', {}, {}).then(res => {
            console.log(res.fileTypeProportion)
            let messarr = res.fileTypeProportion,total = 0
            messarr.map((c,i)=>{
                total = total+c.count
            })
            messarr.map((c,i)=>{
                c.percent = ((c.count/total)*100).toFixed(2)
                if(c.type==2){
                    c.color = '#9C74D2',
                    c.text = '图片'   
                }else if(c.type==1){
                    c.color = '#37A7FF'
                    c.text = '文档'
                }else if(c.type==3){
                    c.color = '#FD9FC1'
                    c.text = '音乐'
                }else if(c.type==5){
                    c.color = '#FEECAF'
                    c.text = '压缩包'
                }else if(c.type==4){
                    c.color = '#FAAEA4'
                    c.text = '视频'
                }else if(c.type==6){
                    c.color = '#E2E2E2'
                    c.text = '其他'
                }else if(c.type==0){
                    messarr.splice(i,1)
                }
            })
            console.log(total)
            this.setData({
                messarr:res.fileTypeProportion
            })
            this.drawImage()

        }, err => {
            console.log(err);
        });
    },
    drawImage(){
         // 初始化
		const ctx = wx.createCanvasContext('Canvas');
		// 设置圆点 x  y   中心点
		let number = {
			x: 150,
			y: 150
		};
		// 获取数据 各类项的个数
		let term = this.data.messarr;
		let termarr = [];
		for (let t = 0; t < term.length; t++) {
			// flownum
			let thisterm = Number(term[t].count)
			let thiscolor = term[t].color
			termarr.push({
				data: thisterm,
				color: thiscolor
			})
		}
		console.log(termarr)
		// 设置总数
		let sign = 0;
		for (var s = 0; s < termarr.length; s++) {
			sign += termarr[s].data
		}
		//设置半径 
		let radius = 130;
		for (var i = 0; i < termarr.length; i++) {
			var start = 0;
			// 开始绘制
			ctx.beginPath()
			if (i > 0) {
				for (var j = 0; j < i; j++) {
					start += termarr[j].data / sign * 2 * Math.PI
				}
			}
			var end = start + termarr[i].data / sign * 2 * Math.PI
			ctx.arc(number.x, number.y, radius, start, end);
			ctx.setLineWidth(1);
			ctx.lineTo(number.x, number.y);
			ctx.setStrokeStyle('#fff');
			ctx.setFillStyle(termarr[i].color);
			ctx.fill();
			ctx.closePath();
			ctx.stroke();
		}
		ctx.draw()
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