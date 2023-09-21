import {imageUrl} from '../../utils/image';
import {actionArr,fileTypeArr,fileTimeArr,fileSizeArr,customFileTypeArr,showList,sortFieldList,sortTypeList,listArr} from './config';
import {req} from '../../utils/service';
import {URL} from '../../utils/config';
import {getDateStr} from '../../utils/util'
const {globalData} = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Component({
  properties:{
    ShowSquare:{
      type:Boolean,
      value:true,
    },
    userInfo:{
      type:Object,
      value:{},
    },
    // listArr:{
    //   type:Array,
    //   value:[],
    //   observer:function(n,o){
    //     if(n!=o){
    //       console.log(n)
    //       // this.setListArr(n)
    //     }
    //   }
    // },
    listArr2:{
      type:Array,
      value:{},
    },
    sourceID:{
      type:Number,
      value:0
    }
  },
  data: {
    listArr,
    listArr2Show:true,
    statusBarHeight:globalData.systemInfo.statusBarHeight,
    imageUrl,
    searchScope:['全部范围','当前文件夹'],
    selectScopeIndex:1,
    filterShow:false,
    actionArr,
    fileTypeArr,fileTimeArr,fileSizeArr,customFileTypeArr,showList,sortFieldList,sortTypeList,
    checkedCustom:false,
    modelListShow:false,
    modelIndex:0,
    sortFieldIndex:0,
    sortTypeIndex:0,
    iconArr:[
      {
        url:'/images/icons/collection.png',
        text:'收藏夹',
        count:0
      },
      {
        url:'/images/icons/dustbin.png',
        text:'回收站',
        count:0
      },
      // {
      //   url:'/images/icons/cloudIcon.png',
      //   count:0
      // },
      {
        url:'/images/icons/remindFill.png',
        text:'消息',
        // count:1,
      },{
        url:'/images/icons/menu.png',
        text:'菜单',
      }
    ],
    lang:globalData.lang,
    slideShow:false,
    searchFilter:{
      sourceID:0
    },
    customFileType:'请选择',
    // minDate:new Date(2010, 0, 1).getTime(),
    // maxDate: new Date().getTime(),
    dateShow:false,
    currentPage:1,
    favShow:true,
    listArr2Show:true
  },
  lifetimes:{
    attached(){
      this.getRecycle()
      this.getFavNum()
      this.getNoticeNum()
      this.getUserList()
      this.setData({
        lang:globalData.lang,
      })
      this.getOption()
    },
    ready(){
      let that = this
      setTimeout(function(){
        that.setData({
          minDate:new Date(2010, 0, 1).getTime(),
          maxDate: new Date().getTime(),
        })
      },500)
    }
  },
  methods:{
    getOption(){
      req("GET",'/api/disk/options', {}, {}).then(res => {
          // console.log(res)
          wx.setStorageSync('options', res)
          this.setListArr()
      }, err => {
          console.log(err);
      });
    },
    setListArr(){
      let treeOpen = wx.getStorageSync('options').treeOpen,{listArr,favShow,listArr2Show} =this.data
      // console.log(treeOpen)
      if(treeOpen.indexOf('myFav')==-1){
        favShow = false
        listArr.map((c,index)=>{
          if(c.icon=='fav'){
            listArr.splice(index,1)
          } 
        })
      }
      if(treeOpen.indexOf('recentDoc')==-1){
        listArr.map((c,index)=>{
          if(c.icon=='recentDoc'){
            listArr.splice(index,1)
          } 
        })
      }
      if(treeOpen.indexOf('shareLink')==-1){
        listArr.map((c,index)=>{
          if(c.icon=='shareLink'){
            listArr.splice(index,1)
          } 
        })
      }
      if(treeOpen.indexOf('fileType')==-1){
        listArr2Show = false
      }
      this.setData({
        listArr,
        favShow,
        listArr2Show
      })
      // console.log(this.data.iconArr,listArr2Show,listArr,this.data.listArr2)
    },
    //获取回收站数量
    getRecycle(){
      let param = {
        sourceID:0,
        currentPage:1,
        pageSize: 10,
        block: 'recycle'
      }
      req("GET",URL.getDiskList, param, {}).then(data => {
        this.setData({
          recycleTotal:data.total
        })
      }, err => {
        console.log(err);
      });
    },
    //获取收藏夹数量
    getFavNum(){
      let param = {
        sourceID:0,
        currentPage:1,
        pageSize: 10,
        block: 'fav'
      }
      req("GET",URL.getDiskList, param, {}).then(data => {
        this.setData({
          favTotal:data.total
        })
      }, err => {
        console.log(err);
      });
    },
    //获取消息数量
    getNoticeNum(){
      let param = {
        currentPage: 1,
        pageSize: 10
      }
      req("GET",URL.getNoticeList, param, {}).then(res => {
        let one = res.list.filter(c => !c.isRead)
        this.setData({
          noticeTotal:one.length
        })
      }, err => {
          console.log(err);
      });
    },
    getUserList(reachBottom = false){
      let param = {
        currentPage:this.data.currentPage	
      }
      req('GET',URL.getUserList,param, {}, false).then(data => {
        data.list.map(c=>{
          c.name = c.nickname?c.nickname:c.name
        })
        this.setData({
          userList:reachBottom?[...this.data.userList, ...data.list]:data.list,
          actionList:this.data.userList
        })
      })
    },
    onReachBottom: function () {
      this.setData({
          currentPage: this.data.currentPage + 1
      })
      this.getUserList(true)
    },
    showModelList(){
      this.setData({modelListShow:!this.data.modelListShow})
    },
    choseType(e){
      let {index} = e.currentTarget.dataset
      this.triggerEvent("onChoseType")
      this.onCloseMore()
      this.showModelList()
      this.setData({
        modelIndex:index
      })
    },
    showSortList(){
      this.setData({sortListShow:!this.data.sortListShow})
    },
    choseSortField(e){
      let {index,item} = e.currentTarget.dataset
      this.setData({
        sortFieldIndex:index
      })
      this.onCloseMore()
      this.showSortList()
      this.triggerEvent("onChoseSortField",{sortField:item.field})
    },
    choseSortType(e){
      let {index,item} = e.currentTarget.dataset
      this.setData({
        sortTypeIndex:index
      })
      this.onCloseMore()
      this.showSortList()
      this.triggerEvent("onChoseSortType",{sortType:item.type})
    },
    showFilter(){
      this.setData({filterShow:!this.data.filterShow})
    },
    showActionList(e){
      let {index} = e.currentTarget.dataset,{actionList} = this.data
      if(index==0){
        actionList = this.data.fileTypeArr
      }
      else if(index==1){
        actionList = this.data.fileTimeArr
        actionList.map((c,index)=>{
          if(index>0&&index<5){
            c.timeTo = this.getDateStr(null,0)
          }
        })
        actionList[1].timeFrom = this.getDateStr(null,-1)
        actionList[2].timeFrom = this.getDateStr(null,-7)
        actionList[3].timeFrom = this.getDateStr(null,-30)
        actionList[4].timeFrom = this.getDateStr(null,-365)
      }
      else if(index==2){
        actionList = this.data.fileSizeArr
      }
      else if(index==3){
        actionList = this.data.userList
      }
      actionList.map(c=>c.actionIndex = index)
      this.setData({
        actionShow:!this.data.actionShow,
        actionList,
        isUserList:index==3?true:false
      })
    },
    selectScope(e){
      let {index} = e.currentTarget.dataset
      this.setData({
        selectScopeIndex:index,
      })
    },
    //选择类型、时间、大小、用户等
    onSelect(e){
      let {item,index} = e.currentTarget.dataset,actionIndex = item.actionIndex
      let {actionArr,customFileTypeArr} = this.data
      actionArr[actionIndex].value = item.name
      if(actionIndex==0){
        this.setData({
          ['searchFilter.filesType']:item.type,
          customFileTypeIndex:index
        })
        if(item.name!='自定义'){
          customFileTypeArr.map(c=>{c.checked = false})
          this.setData({customFileTypeArr,checkedCustom:false})
        }
      }else if(actionIndex==1){
        this.setData({
          ['searchFilter.timeFrom']:item.timeFrom,
          ['searchFilter.timeTo']:item.timeTo,
          customDateIndex:index
        })
        if(item.name!='自定义'){
          this.setData({dateFrom:'',dateTo:''})
        }
      }else if(actionIndex==2){
        this.setData({
          ['searchFilter.minSize']:item.minSize,
          ['searchFilter.maxSize']:item.maxSize,
          customSizeIndex:index
        })
        if(item.name!='自定义'){
          this.setData({minSize:'',maxSize:''})
        }
      }else if(actionIndex==3){
        this.setData({
          ['searchFilter.userID']:item.userID
        })
      }
      this.setData({
        actionArr,
        actionShow:false
      })
    },  
    //搜索文件
    filterFile(){
      let {searchFilter,selectScopeIndex,customFileTypeArr,dateFrom,dateTo,minSize,maxSize} = this.data
      searchFilter.sourceID = selectScopeIndex==0?0:this.data.sourceID
      let one = customFileTypeArr.filter(c => c.checked)
      if(one.length){
        let arr = []
        one.map(c=>{
          arr.push(c.name)
        })
        searchFilter.filesType = arr.toString()
      }
      if(dateFrom&&dateTo){
        searchFilter.timeFrom = dateFrom
        searchFilter.timeTo = dateTo
      }
      if(maxSize){
        searchFilter.maxSize = maxSize
      }
      if(minSize){
        searchFilter.minSize = minSize
      }
      this.triggerEvent('onFilterFile',{searchFilter})
      this.setData({
        filterShow:false
      })
    },
    //重置条件
    resetFile(type){
      let searchFilter = {
        sourceID:this.data.selectScopeIndex==0?0:this.data.sourceID
      },{actionArr,customFileTypeArr} = this.data
      actionArr[0].value = '不限类型'
      actionArr[1].value = '不限时间'
      actionArr[2].value = '不限大小'
      actionArr[3].value = '请选择'
      customFileTypeArr.map(c=>{c.checked = false})
      this.setData({
        filterShow:false,
        actionArr,
        customFileTypeArr,
        checkedCustom:false,
        dateFrom:'',
        dateTo:'',
        customFileTypeIndex:-1,
        customDateIndex:-1,
        searchFilter,
        customSizeIndex:'',
        maxSize:'',
        minSize:'',
        keyword:''
      })
      if(!type)this.triggerEvent('onFilterFile',{searchFilter})
    },
    showCustomFileTypeList(){
      this.setData({customFileTypeShow:!this.data.customFileTypeShow})
    },
    choseCustomType(e){
      let {index} = e.currentTarget.dataset,{customFileTypeArr} = this.data
      customFileTypeArr[index].checked = !customFileTypeArr[index].checked 
      let one = customFileTypeArr.filter(c => c.checked)
      this.setData({
        customFileTypeArr,
        customFileTypeShow:false,
        checkedCustom:one.length?true:false
      })
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
        dateFrom:this.formatDate(e.detail[0]),
        dateTo:this.formatDate(e.detail[1]),
        dateShow:false
      })
    },
    setCustomSize(e){
      let {type} = e.target.dataset
      if(type=='min'){
        this.setData({minSize:e.detail.value})
      }else{
        this.setData({maxSize:e.detail.value})
      }
    },
    onCloseMore(){
      this.setData({moreShow:!this.data.moreShow})
    },
    showSlide(e){
      this.triggerEvent('onSlideShow',{slideShow:!this.data.slideShow})
    },
    goToPage(e){
      let{index,item} = e.currentTarget.dataset,url=''
      if(index==0){
        let item = {
          name: "收藏夹",
          sourceID:0,
          type: "fav",
        }
        wx.navigateTo({
          url: '/pages/files-list/files-list?item='+JSON.stringify(item),
        })
        // this.triggerEvent('onGetList',{item,indexSet:1,isAddShow:false})
      }else if(index==1){
        let item = {
          name: "回收站",
          sourceID:0,
          type: "recycle",
        }
        wx.navigateTo({
          url: '/pages/files-list/files-list?item='+JSON.stringify(item),
        })
        // this.triggerEvent('onGetList',{item,indexSet:1,isAddShow:false})
      }else if(index==2){
        // url = '/pages/transmissionCenter/transmissionCenter'
        url = '/pages/messages/messages'
      }else if(index==3){
        this.setData({moreShow:true})
        // url = '/pages/messages/messages'
      }
      if(url){
        wx.navigateTo({
          url:url,
        })
      }
    },
    setIndex(){
      this.setData({
        selectIndex:-1
      })
    },
    getList(e){
      let {index,type,item} = e.currentTarget.dataset
      this.setData({
        selectIndex:index,
        type:type
      })
      this.onCloseMore()
      this.triggerEvent('onGetList',{item,indexSet:1,isAddShow:false})
    },
    searchFiles(e){
      this.setData({
        keyword:e.detail.value
      })
      this.triggerEvent("onSearch",{keyword:e.detail.value})
    },
    getDateStr
  }
})