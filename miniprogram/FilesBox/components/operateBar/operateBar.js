import {imageUrl} from '../../utils/image';
import {listArr,moreArr,recycleArr,favArr,moreCheckedArr,linkArr} from './config';
import {req} from '../../utils/service';
import {URL} from '../../utils/config';
const DOMAIN = wx.getStorageSync('DOMAIN')
const {globalData} = getApp();
Component({
  properties:{
    operateShow:{
      type:Boolean,
      value:false,
      observer:function(n,o){
        if(n){
          this.authCheck()
        }
      }
    },
    isFav:{
      type:Boolean,
      value:false
    },
    isTop:{
      type:Boolean,
      value:false
    },
    isUpload:{
      type:Boolean,
      value:false
    },
    isZip:{
      type:Boolean,
      value:false
    },
    fileAuth:{
      type:String,
      value:'',
      observer:function(n,o){
        if(n!=o){
          this.setData({
            adminView:n.indexOf("3") != -1?1:0,
            adminDownload:n.indexOf("4") != -1?1:0,
            adminEdit:n.indexOf("8") != -1?1:0,
            adminMove:n.indexOf("9") != -1?1:0,
            adminRemove:n.indexOf("10") != -1?1:0,
            adminShare:n.indexOf("11") != -1?1:0,
          })
        }
      }
    },
    targetType:{
      type:Number,
      value:2,
    },
    checkedTagList:{
      type:Array,
      value:[],
    },
    checkedSum:{
      type:Number,
      value:0
    },
    checkedFile:{
      type:Object,
      value:{},
    },
    noSelectAll:{
      type:Boolean,
      value:false
    },
    filesSum:{
      type:Number,
      value:0
    },
    formatConvert:{
      type:Boolean,
      value:false,
    }
  },
  data: {
    imageUrl,
    listArr,
    moreArr,
    recycleArr,
    favArr,
    moreCheckedArr,
    linkArr,
    lang:globalData.lang,
    statusBarHeight:globalData.systemInfo.statusBarHeight,
    tagListShow:false,
    pressedList:['压缩','打包'],
    unzipList:['解压到当前','解压到文件夹','解压到...'],
    isSelectAll:false
  },
  lifetimes:{
    attached(){
      let role = wx.getStorageSync('options').role
      this.setData({
        role:role,
        opreateArr:this.data.listArr,
        lang:globalData.lang,
        shareLinkAllow:wx.getStorageSync('options')?wx.getStorageSync('options').markConfig.shareLinkAllow:0
      })
      this.getTagList()
    }
  },
  methods:{
    authCheck: function () {
      let role = wx.getStorageSync('options').role,{opreateArr,moreArr,folderSum} = this.data,{fileAuth} = this.properties
    //   console.log(role,this.properties.fileAuth,this.properties.checkedFile)
      // 个人文件权限判断，只需考虑角色权限
      opreateArr.map(c=>{
        if((c.text=='复制'||c.text=='剪切')&&role['explorer.move']==0){
          c.show = false
        }else if((c.text=='删除'||c.text=='彻底删除')&&role['explorer.remove']==0){
          c.show = false
        }else if((c.text=='重命名')&&role['explorer.edit']==0){
          c.show = false
        }else if((c.text=='打开'||c.text=='属性')&&role['explorer.view']==0){
          c.show = false
        }else if((c.text=='收藏'||c.text=='取消收藏')&&role['user.fav']==0){
          c.show = false
        }else if((c.text=='压缩')&&role['explorer.zip']==0){
          c.show = false
        }else{
          c.show = true
        }
      })
      // console.log(moreArr)
      moreArr.map(n=>{
        if((n.text=='打开'||n.text=='属性')&&role['explorer.view']==0){
          n.show = false
        }else if((n.text=='下载')&&role['explorer.download']==0){
          n.show = false
        }else if((n.text=='分享'||n.text=='取消分享')&&role['explorer.share']==0&&!this.data.shareLinkAllow){
          n.show = false
        }else if((n.text=='置顶'||n.text=='取消置顶')&&role['explorer.edit']==0){
          n.show = false
        }else if(((n.text=='上传新版本')&&role['explorer.edit']==0)||(n.text=='上传新版本'&&!this.properties.isUpload)){
          n.show = false
        }else if((n.text=='创建快捷方式'||n.text=='发送到桌面快捷方式')&&role['explorer.add']==0){
          n.show = false
        }else if((n.text=='创建快捷方式'||n.text=='发送到桌面快捷方式')&&role['explorer.add']==0){
          n.show = false
        }else if(((n.text=='格式转换')&&role['explorer.upload']==0)||(n.text=='格式转换'&&!this.properties.formatConvert)){
          n.show = false
        }else if((n.text=='创建压缩包')&&role['explorer.zip']==0){
          n.show = false
        }else if((n.text=='解压到...')&&role['explorer.unzip']==0){
          n.show = false
        }else if((n.text=='收藏'||n.text=='取消收藏'||n.text=='标签')&&role['user.fav']==0){
          n.show = false
        }else if((n.text=='标签')&&role['explorer.upload']==0){
          n.show = false
        }else if((n.text=='文件查重'||n.text=='文件名查重')&&folderSum.length>0){
          n.show = false
        }else{
          n.show = true
        }
      })
      //企业文件增加文档权限判断
      if(this.properties.targetType==2){
        // console.log(fileAuth,opreateArr,moreArr)
        opreateArr.map(c=>{
          if((c.text=='复制'||c.text=='剪切')&&fileAuth.indexOf("9") == -1){
            c.show = false
          }else if((c.text=='删除')&&fileAuth.indexOf("10") == -1){
            c.show = false
          }else if((c.text=='重命名')&&fileAuth.indexOf("8") == -1){
            c.show = false
          }else if((c.text=='打开'||c.text=='属性')&&fileAuth.indexOf("3") == -1){
            c.show = false
          }
        })
        moreArr.map(n=>{
          if((n.text=='复制'||n.text=='剪切'||n.text=='创建快捷方式')&&fileAuth.indexOf("9") == -1){
            n.show = false
          }else if((n.text=='删除')&&fileAuth.indexOf("10") == -1){
            n.show = false
          }else if((n.text=='重命名'||n.text=='置顶'||n.text=='上传新版本'||n.text=='取消置顶')&&fileAuth.indexOf("8") == -1){
            n.show = false
          }else if((n.text=='打开'||n.text=='属性')&&fileAuth.indexOf("3") == -1){
            n.show = false
          }else if((n.text=='下载')&&fileAuth.indexOf("4") == -1){
            n.show = false
          }else if((n.text=='格式转换')&&fileAuth.indexOf("5") == -1){
            n.show = false
          }else if((n.text=='创建压缩包')&&fileAuth.indexOf("6") == -1){
            n.show = false
          }else if((n.text=='解压到...')&&fileAuth.indexOf("7") == -1){
            n.show = false
          }else if((n.text=='分享'||n.text=='取消分享')&&fileAuth.indexOf("11") == -1){
            n.show = false
          }
        })
      }
      if(this.data.checkedFile.name=='桌面'){
        opreateArr.map(c=>{
          if((c.text=='剪切'||c.text=='删除'||c.text=='重命名')){
            c.show = false
          }
        })
      }
      this.setData({
        opreateArr,
        moreArr
      })
    },
    getTagList(){
      req('GET',URL.getTagList, {}, {}, false).then(data => {
        data.map(c=>{c.checked = false})
        let tagList = data
        if(wx.getStorageSync('checkedTag').length>0){
          let checkedTag = wx.getStorageSync('checkedTag')
          tagList.map(c=>{
            checkedTag.map(o=>{
              if(c.labelId==o.tagID){
                c.checked = true
              }
            })
          })
          wx.removeStorageSync('checkedTag')
        }
        this.setData({
          tagList:tagList
        })
      })
    },
    showPressedWay(){
      this.setData({
        pressedWayShow:!this.data.pressedWayShow,
        operateMoreShow:false
      })
    },
    zipFile(e){
      let {index} = e.currentTarget.dataset,suffix = ''
      // if(index==0){
      //   suffix = 'zip'
      // }else{
      //   suffix = 'tar'
      // }
      suffix = 'zip'
      this.setData({
        pressedWayShow:false,
      })
      this.triggerEvent('onZipFile',{suffix:suffix,index})
    },
    setOperate(e){
      let {catalogue,checkedSum,folderSum,formatConvert} = e,opreateArr = [],showRecyle = false ,isLinkShare = false,{moreArr} = this.data
      if(catalogue.length>1){
        if(checkedSum>1){
          if(catalogue[1].label=='我的分享'){
            isLinkShare = true
            opreateArr = [{
              text:'取消分享',
              image:'/images/list_icon/icon_file_del.png',
              fn:'cancelShare'
            }]
          }else{
            opreateArr = this.data.moreCheckedArr
          }
        }else{
          if(catalogue[1].label=='我的分享'){
            isLinkShare = true
            opreateArr = this.data.linkArr
          }else{
            opreateArr = this.data.listArr
          }
        }
      }else{
        if(checkedSum>1){
          if(catalogue[0].type=='recycle'){
            showRecyle = true
            opreateArr = this.data.recycleArr
          }else if(catalogue[0].type=='fav'){
            opreateArr = [{
              text:'取消收藏',
              image:'/images/operateMore/icon_file_collect.png',
              fn:'collectFiles'
            }]
          }else if(catalogue[0].type=='shareLink'){
            isLinkShare = true
            opreateArr = [{
              text:'取消分享',
              image:'/images/list_icon/icon_file_del.png',
              fn:'cancelShare'
            }]
          }else{
            opreateArr = this.data.moreCheckedArr
          }
        }else{
          if(catalogue[0].type=='recycle'){
            showRecyle = true
            opreateArr = this.data.recycleArr
          }else if(catalogue[0].type=='fav'){
            opreateArr = this.data.favArr
          }else if(catalogue[0].type=='shareLink'){
            isLinkShare = true
            opreateArr = this.data.linkArr
          }else{
            opreateArr = this.data.listArr
          }
        }
      }
      // if(folderSum.length>0){
      //   moreArr.map((c,i)=>{
      //     if(c.text=='上传新版本'||c.text=='文件查重'||c.text=='文件名查重'||c.text=='格式转换'){
      //       c.show = false
      //     }
      //   })
      // }else{
      //   moreArr.map((c,i)=>{
      //     c.show = true
      //   })
      // }
      this.setData({
        opreateArr,
        showRecyle,
        isLinkShare,
        moreArr,
        formatConvert,
        folderSum
      })
      if(checkedSum>1){
        this.authCheck()
      }  
    },
    cancelShare(){
      this.triggerEvent('cancelShare')
    },
    shareLink(){
      this.triggerEvent('shareLink')
    },
    selectAllFile(){
      let {isSelectAll,showFav} = this.data
      isSelectAll = !isSelectAll
      this.setData({
        isSelectAll,
        moreFav:isSelectAll&&showFav?true:false
      })
      this.triggerEvent('selectAllFile',{isSelectAll})
    },
    cancelPressed(){
      this.triggerEvent('onCancelEdit')
      this.setData({
        pressedWayShow:false,
      })
    },
    showZipList(){
      this.setData({
        unzipListShow:!this.data.unzipListShow,
        operateMoreShow:false
      })
    },
    unzipFile(e){
      let {index} = e.currentTarget.dataset
      this.setData({
        unzipListShow:false,
      })
      this.triggerEvent('onUnzipFile',{index:index})
    },
    cancelUnzip(){
      this.triggerEvent('onCancelEdit')
      this.setData({
        unzipListShow:false,
      })
    },
    showTagList(){
      // this.getTagList()
      this.setData({
        operateMoreShow:false,
        tagListShow:!this.data.tagListShow
      })
    },
    chooseTag(e){
      let {item,index} = e.currentTarget.dataset,{tagList} = this.data
      tagList[index].checked = !tagList[index].checked
      this.setData({
        tagList
      })
      this.triggerEvent('onChooseTag',{block:tagList[index].checked?'add':'cancel',labelId:item.labelId})
    },
    cancelChoose(){
      wx.setStorageSync('needReget', true)
      this.triggerEvent('onCancelEdit')
      this.setData({
        operateMoreShow:false,
        tagListShow:false
      })
    },
    goToTagManage(){
      this.setData({
        tagListShow:false
      })
      this.triggerEvent('onCancelEdit')
      wx.navigateTo({
        url: '/pages/tagManage/tagManage',
      })
    },
    cancel(){
      this.triggerEvent('onCancelEdit')
      // this.triggerEvent('onOperateeShow',{operateShow:false})
    },
    getMoreOperate(){
      this.getTagList()
      this.setData({operateMoreShow:true})
    },
    onCloseMore(){
      this.setData({operateMoreShow:false})
    },
    showRename(){
      this.triggerEvent('onShowRename')
    },
    moveFile(e){
      let {type} = e.currentTarget.dataset
      this.triggerEvent('onMoveFile',{type:type})
    },
    removeFile(){
      this.triggerEvent('onRemoveFile')
    },
    reductionFile(){
      this.triggerEvent('onReductionFile')
    },
    deleteFile(){
      this.triggerEvent('onDeleteFile')
    },
    openFile(){
      this.triggerEvent('onOpenFile')
    },
    topFile(){
      this.triggerEvent('onTopFile')
    },
    collectFiles(e){
      let {text} = e.currentTarget.dataset
      this.triggerEvent('onCollectFile',{isDisFav:(text=='取消收藏'?true:false)})
    },
    uploadNewFile(){
      this.triggerEvent('uploadNewFile')
    },
    downloadFiles(){
      this.triggerEvent('onDownloadFiles')
    },
    showAttribute(){
      this.triggerEvent('onShowAttribute')
    },
    showFormat(){
      this.triggerEvent('onShowFormat')
    },
    repeatFile(){
      this.triggerEvent('onRepeatFile')
    },
    repeatFileName(){
      this.triggerEvent('onRepeatFileName')
    },
    sendDesktop(){
      this.triggerEvent('onSendDesktop')
    },
    creatShortcut(){
      this.triggerEvent('onCreatShortcut')
    },
  }
})