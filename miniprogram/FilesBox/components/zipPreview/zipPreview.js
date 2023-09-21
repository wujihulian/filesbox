import{renderSize,timeFromNow,timeFormat} from '../../utils/util';
import {req} from '../../utils/service';
import {URL} from '../../utils/config';
import {imageUrl} from '../../utils/image';
import{initIconPath} from '../file/files-component/config'
const {globalData} = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Component({
  properties:{
    // attributeShow:{
    //   type:Boolean,
    //   value:false
    // }
  },
  data: {
    imageUrl,
    statusBarHeight:globalData.systemInfo.statusBarHeight,
    lang:globalData.lang,
    zipShow:false,
    showArrow:true,
    unzipOperateList:['解压到当前','解压到...'],
  },
  methods:{
    setImage(arr){
      let {imageUrl} = this.data
      const isImage = /jpeg|jpg|gif|png|webp|jfif|bmp|dpg/,
            isFile = /pptx|ppt|xlsx|xls|doc|docx|pdf|wps/;
      arr.map(c=>{
        c.sourceID = this.data.sourceID
        if(isImage.test(c.originName.split('.')[c.originName.split('.').length-1])){
          c.fileType = c.originName.split('.')[c.originName.split('.').length-1]
          c.sort = 'img'
        }else if(isFile.test(c.originName.split('.')[c.originName.split('.').length-1])){
          c.fileType = c.originName.split('.')[c.originName.split('.').length-1]
          c.sort = 'text'
        }else{
          c.fileType = ''
        }
        if(c.size>0)c.size = renderSize(c.size)
        if(c.childList.length>0){
          this.setImage(c.childList)
          if(c.originName.split('.').length>1){
            if(isImage.test(c.originName.split('.')[c.originName.split('.').length-1])){
              c.thumb = imageUrl.fileIcons+'bmp.png'
            }else{
              c.thumb = initIconPath(c.originName.split('.')[c.originName.split('.').length-1])
            }
          }else{
            c.thumb = '/images/rootIcon/folder.png'
          }    
        }else{
          if(isImage.test(c.originName.split('.')[c.originName.split('.').length-1])){
            c.thumb = imageUrl.fileIcons+'bmp.png'
          }else{
            c.thumb = initIconPath(c.originName.split('.')[c.originName.split('.').length-1])
          }
        }
      })
      return arr
    },
    getZipList(param){
      param.size = renderSize(param.size)
      this.setData({
        sourceID:param.sourceID
      })
      req('GET',URL.unzipList,{sourceID:param.sourceID},{},false).then(data => {     
        this.setImage(data.childList)
        this.setData({
          dataList:param,
          unzipList:data.childList,
          zipShow:true,
        })
      })
    },
    showArrow(){
      this.setData({
        showArrow:!this.data.showArrow
      })
    },
    onClose(){
      this.setData({
        zipShow:false
      })
    },
    showOperate(e){
      this.setData({
        operateShow:true,
        checkedItem:e.detail.checkedItem
      })
    },
    closeOperate(){
      this.setData({
        operateShow:false
      })
    },
    showEncrypt(){
      this.setData({
        encryptShow:!this.data.encryptShow
      })
    },
    showPassword(){
      this.setData({
        showPassword:!this.data.showPassword
      })
    },
    textInput(e){
      this.setData({
        password:e.detail
      })
    },
    confirmUnzip(){
      let {password} = this.data
      if(password==null){
        wx.showToast({
          title: '请输入压缩包密码',
          icon:'none'
        })
        return
      }
      this.previewFile()
    },
    // 预览文件
    previewFile() {
      let {sourceID,fileName,index,sort,encrypted} = this.data.checkedItem,previewUrl,{password} = this.data;
      let param = {fullName:fileName,sourceID:sourceID,index:index}
      this.closeOperate()
      if(encrypted&&!password){
        this.showEncrypt()
        return
      }
      if(password){
        param.password = password
      }
      wx.showLoading({
        title:'加载中...'
      })
      req('GET',URL.unzipList,param,{},false).then(data => {
        wx.hideLoading({})
        previewUrl = /http|https/.test(data) ? data : DOMAIN + data
        if(sort=='img'){
          wx.previewImage({
            urls: [`${previewUrl}`],
          })
          return;
        }else{
          console.log(previewUrl)
          wx.navigateTo({
              url: `/pages/link/link?url=${previewUrl}`,
          })
        }
      }).catch(err=>{
        wx.hideLoading({})
        wx.showToast({
          title: err.message,
          icon:'none'
        })
        this.showEncrypt()
        this.setData({
          password:''
        })
      })
    },
    showUnzipList(){
      this.closeOperate()
      this.setData({
        unzipListShow:!this.data.unzipListShow
      })
    },
    unzipFile(e){
      let {index} = e.currentTarget.dataset,{fileName,directory} = this.data.checkedItem,choseIndex = this.data.checkedItem.index
      this.showUnzipList()
      this.setData({
        unzipListShow:false,
        zipShow:false
      })
      this.triggerEvent('onUnzipFile',{index,sourceID:this.data.sourceID,choseIndex,fullName:fileName,directory})
    },
    // unzipFile(e){
    //   let {index,zipShow,choseIndex,fullName,directory} = e.detail
    //   this.setData({
    //     zipShow
    //   })
    //   this.triggerEvent('onUnzipFile',{index,sourceID:this.data.sourceID,choseIndex,fullName,directory})
    // },
    initIconPath
  },
})