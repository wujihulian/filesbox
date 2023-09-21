// packageB/pages/files/files-move/files-move.js
import { imageUrl } from '../../../utils/image';
import { req,uploadFile } from '../../../utils/service';
import { STATIC_DOMAIN, URL } from '../../../utils/config';
import {uploadLargeFile} from '../../../utils/uploadFile';
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    number:{
      type:Number,
      value:0
    },
    moveType:{
      type:String,
      value:''
    },
    isUnzip:{
      type:Boolean,
      value:false
    },
    rootCatalogue:{
      type:Array,
      value:[],
      observer:function(n,o){
        if(n!=o){
          this.setData({
            catalogue:n,
            sourceID:n[0].sourceID
          })
        }
      }
    }
  },
  lifetimes:{
    attached(){
      let PAGES = getCurrentPages();
      this.currentPage = PAGES[PAGES.length-1];
      this.setData({systemInfo:wx.getSystemInfoSync()})
      this.getDiskList();
      this.getPath()
    }
  },
  /**
   * 组件的初始数据
   */
  data: {
    catalogue:[],
    sourceID:0,
    loadingState: 1,
  },
  /**
   * 组件的方法列表
   */
  methods: {
     // 选择根目录
    choseCatelogue(e){
      let { index } = e.currentTarget.dataset, { catalogue } = this.data;
      if(index == catalogue.length-1) return;
      catalogue = catalogue.splice(0,index+1);
      this.setData({
        sourceID:catalogue[index].sourceID,
        catalogue
      })
      this.getDiskList();
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
        let arr = data.folderList.slice(0,1)
        arr[0].children.folderList.map((c,i)=>{
          if(c.name=='收藏夹'||c.name=='资讯'){
            arr[0].children.folderList.splice(i,1)
          }
        })
        this.setData({
          moreArr:arr,
        })
      }).catch(err => {
          console.log(err)
          wx.showToast({
              title: err.message,
              icon: 'none'
          })
      })
    },
    showSlide(e){
      this.setData({
          slideShow:!this.data.slideShow,
      })
      this.getList(e)
    },
    getList(e){
      if(e.detail.item){
          let {sourceID,name,icon,labelId,ext} = e.detail.item, 
          catalogue = [{
              label: name,
              sourceID,
              type:icon,
              labelId:labelId,
              fileType:ext
          }]
          this.setData({sourceID,catalogue})
          this.getDiskList() 
      }
    },
    getDiskList(reachBottom = false){
      let { sourceID } = this.data;
      let param = {
        sourceID: sourceID,
        currentPage: 1,
        pageSize: 500,
        keyword:''
      }
      req('GET',URL.getDiskList,param,{},false).then(data=>{
        data.fileList.map((c,i)=>{
          let isImage = /jpeg|jpg|gif|png/,DOMAIN = wx.getStorageSync('DOMAIN')
          if(isImage.test(c.fileType)){
            c.isSend = true
            c.thumb = c.path
            if(c.thumb&&isImage.test(c.fileType)){
              c.thumb = c.thumb?(/http|https/.test(c.thumb) ? c.thumb : DOMAIN + c.thumb):c.path;
              let index = c.thumb.lastIndexOf('.');
              c.thumb = `${c.thumb.substring(0,index)}${c.thumb.substring(index,c.thumb.length)}&nameSuffix=small`;
            }   
          }else{
            c.isSend = false
          }
        })
        data.folderList.map(c => {
          if(!data.current)c.thumb = '/images/rootIcon/folder.png'
          c.checked = false;
        })
        this.setData({
          dataList:data,
          loadingState:0
        })
      }).catch(err=>{

      })
    },
    //选择发送图片
    choseImage(e){
      let {index} = e.currentTarget.dataset, {dataList} = this.data,
      checkedSum = dataList.fileList.filter(c => c.checked).length
      if(checkedSum==1){
        return
      }
      dataList.fileList[index].checked = !dataList.fileList[index].checked;
      this.setData({
        dataList
      })
    },
    // 选择文件夹打开
    choseFolder(e){
      let { sourceID, name } = e.currentTarget.dataset.item, { catalogue } = this.data;
      catalogue.push({
        label:name,
        sourceID
      })
      this.setData({sourceID,catalogue});
      this.getDiskList();
    },
    createNewFolder(){
      this.triggerEvent('createNewFolder')
    },
    uploadWechatFile(){
      // let {forwardMaterials} = wx.getLaunchOptionsSync(),{ sourceID } = this.data;
      let forwardMaterials = wx.getStorageSync('forwardMaterials'),{ sourceID } = this.data;
      this.triggerEvent('uploadWechatFile',{file:forwardMaterials[0],sourceID})
    },
    moveFileToFolder(){
      let { sourceID } = this.data;
      this.triggerEvent('moveFileToFolder',{
        sourceID
      })
    },
    sendPicture(){
      let { dataList } = this.data,one = dataList.fileList.filter(c => c.checked)
      this.triggerEvent('sendPicture',{one})
    },
    unzipFile(){
      let { sourceID } = this.data;
      this.triggerEvent('unzipFileTo',{
        sourceID
      })
    },
    cancelMove(){
      // this.setData({
      //   sourceID:0,
      //   catalogue:[{ label:'根目录', sourceID:'0' } ],
      // })
      // this.getDiskList();
      this.triggerEvent('cancelMove');
    },
    uploadLargeFile
  }
})
