// pages/tagManage/tagManage.js
import {req} from '../../utils/service';
import {URL} from '../../utils/config';
import {colorList} from './config';
Page({

    /**
     * 页面的初始数据
     */
    data: {
        colorList,
        selectColor:'',
        colorListShow:false,
        editShow:false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.getTagList()
    },
    getTagList(){
        req('GET',URL.getTagList, {}, {}, false).then(data => {
          data.map(c=>{c.checked = false})
          this.setData({
            tagList:data
          })
        })
    },
    showAdd(e){
        let {type,item} = e.currentTarget.dataset;
        if(type=='edit'){
            this.setData({
                editTitle:'编辑标签',
                labelId:item.labelId,
                editName:item.labelName,
                editStyle:item.style
            })
        }else{
            this.setData({
                editTitle:'新增标签',
                editName:'',
            })
        }
        this.setData({
            editShow:!this.data.editShow,
            operateTag:type=='edit'?'editTag':'addTag'
        })
    },
    delTag(e){
        let {id} = e.currentTarget.dataset
        wx.showModal({
            title: '提示',
            content: '确定要删除该标签吗？',
            success: res => {
                if (res.confirm) {
                    req('POST',URL.delTag, {}, {labelId:id}, false).then(data => {
                        wx.showToast({
                            title: '删除成功！',
                            icon: 'none'
                        })
                        this.getTagList()
                    })
                }
            }
        })
    },
    showColor(){
        this.setData({
            colorListShow:!this.data.colorListShow,        
        })
    },
    selectColor(e){
        let {item} = e.currentTarget.dataset
        this.setData({editStyle:item})
    },
    inputValue(e){
        this.setData({
            editName:e.detail.value
        })
    },  
    addTag(){
        let {editName,editStyle,colorList} = this.data
        console.log(editName)
        if(!editName){
            wx.showToast({
                title:'请输入标签名',
                icon:'none'
            })
            return
        }
        let param = {
            labelName:editName,
            style:editStyle?editStyle:colorList[colorList.length-1]
        }
        req('POST',URL.addTag, {}, param, false).then(data => {
            wx.showToast({
                title:'添加成功',
                icon:'none'
            })
            this.setData({editShow:false})
            this.getTagList()
        }).catch(data =>{
            wx.showToast({
              title: data.message,
              icon:'none'
            })
        })
    },
    editTag(e){
        let {editName,editStyle,colorList,labelId} = this.data
        if(!editName){
            wx.showToast({
                title:'请输入标签名',
                icon:'none'
            })
        }
        let param = {
            labelId:labelId,
            labelName:editName,
            style:editStyle?editStyle:colorList[colorList.length-1]
        }
        req('POST',URL.editTag, {}, param, false).then(data => {
            wx.showToast({
                title:'编辑成功',
                icon:'none'
            })
            this.setData({editShow:false})
            this.getTagList()
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
     
})