// packageB/pages/files/files-component/files-component.js
import {
    imageUrl
} from '../../../utils/image';
import {
    req,
} from '../../../utils/service';
import {
    URL
} from '../../../utils/config';
import {
    timeFormat,twoWayBinding,genUuid,renderSize,
} from '../../../utils/util';
import {
    choseFiles,
    choseImageConfirm,
    choseVideoConfirm,
    uploadLargeFile,
    choseFilesConfirm
} from '../../../utils/uploadFile';
import {
    fileTypeActions,
    filterTypeActions,
    showFileTypeSheet,
    showFilterTypeSheet,
    catalogue,
    modalFuns,
    panelBar,
    sharePanel,
    initIconPath,
    addArr
} from './config';
import {sortFieldList,sortTypeList} from '../../tabNav/config';
const {globalData} = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        scrollViewTopHeight: {
            type: Number,
            value: 0
        },
        sourceID:{
            type: Number,
            value: 0
        },
        catalogue:{
            type:Array,
            value:[],
            observer:function(n,o){
                if(n!=o){
                    let PAGES = getCurrentPages(),currentPage;
                    currentPage = PAGES[PAGES.length - 1];
                    if(!currentPage||currentPage.route=='pages/accountLogin/accountLogin'){
                        // this.getDiskList();
                    }   
                }
            }
        },
        rootCatalogue:{
            type:Array,
            value:[]
        },
        showCatalogue:{
            type:Boolean,
            value:true
        },
        showCate:{
            type:Boolean,
            value:true
        },
        showSwitch:{
            type:Boolean,
            value:false
        },
        showRecyle:{
            type:Boolean,
            value:false
        },
        uploadProgress:0,
    },
    /**
     * 组件的初始数据
     */
    data: {
        statusBarHeight:globalData.systemInfo.statusBarHeight,
        systemInfo:globalData.systemInfo,
        lang:globalData.lang,
        imageUrl,
        fileTypeActions,
        filterTypeActions,
        sharePanel,
        currentPage: 1,
        fileTypeIndex: 0,
        filterTypeIndex: 1,
        // sourceID: 60182, //父文件夹id, 最外层传0
        keyword: '', //搜索关键字
        fileType:'',
        timeFrom:'',
        timeTo:'',
        maxSize:'',
        minSize:'',
        userID:'',
        loadingState: 1,
        dataList: [],
        catalogue,
        showSearch: false, //是否显示筛选
        uploadProgress: 0,
        showModal: false,
        directoryName: '',
        isEdit: false,
        modalFunIndex: 0,
        modalFuns,
        panelBar,
        renameType: 0, //0文件夹 1 文件
        renameData: '', //重命名的文件，或文件夹
        showMoveFile: false, //是否显示移动文件
        newFileName: '',
        addArr,
        isAddShow:true,
        sortField:'name',
        sortType:'asc',
        sortFieldList,
        sortTypeList,
        sortFieldIndex:0,
        sortTypeIndex:0,
    },
    lifetimes: {
        attached() {
            let { windowHeight, windowWidth, statusBarHeight, safeArea } = wx.getSystemInfoSync();
            let height = windowHeight - statusBarHeight - 64 - (windowWidth/750*100) - (windowHeight-safeArea.bottom)
            this.setData({
                rate: windowWidth / 750,
                top:statusBarHeight+54,
                height,
                width:windowWidth-20,
                y:height-65,
                x:windowWidth-20-50
            })
            let {showCatalogue,showCate,systemInfo} = this.data,scrollHeight
            if(showCatalogue){
                if(showCate){
                    scrollHeight = systemInfo.windowHeight - statusBarHeight - 204//自定义tabbar需要-50
                }else{
                    scrollHeight = systemInfo.windowHeight - statusBarHeight - 84
                }
            }else{
                scrollHeight = systemInfo.windowHeight - statusBarHeight - 44
            }
            let PAGES = getCurrentPages();
            this.currentPage = PAGES[PAGES.length - 1];
            this.setData({
                DOMAIN:wx.getStorageSync('DOMAIN'),
                role:wx.getStorageSync('options').role,
                lang:globalData.lang,
                scrollHeight
            })
        }
    },
    /**
     * 组件的方法列表
     */
    methods: {
        initIconPath(suffix) {
            let {
                fileIcons
            } = imageUrl;
            const isVideo = /mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv/,
                isAudio = /avi|wmv|mpg|mpeg|mov|rm|ram|swf|flv|mp4|mp3|wma|avi|rm|rmvb|flv|mpg|mkv/,
                isImage = /jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg/,
                isFile = /pptx|ppt|xlsx|xls|doc|docx|pdf|wps/,
                isDocx = /doc|docx/,
                isPptx = /ppt|pptx/,
                isXlsx = /xlsx|xls/,
                isPdf = /pdf/;
            if (isDocx.test(suffix)) return `${fileIcons}doc.png`;
            if (suffix == 'fbx') return `${fileIcons}fbx.png`;
            if (suffix == 'folder') return `${fileIcons}folder.png`;
            if (isAudio.test(suffix)) return `${fileIcons}music.png`;
            if (isPptx.test(suffix)) return `${fileIcons}pptx.png`;
            if (isXlsx.test(suffix)) return `${fileIcons}xlsx.png`;
            if (isPdf.test(suffix)) return `${fileIcons}pdf.png`;
            return imageUrl.fileDefault
        },
        switchList(){
            this.setData({isList:!this.data.isList})
        },
        onSearch(param){
            this.setData({
                keyword:param,
                currentPage:1
            })
            this.getDiskList()
        },
        onFilter(param){
            this.setData({
                searchFilter:param,
                catalogue:param.catalogue,
            })
            this.getDiskList()
        },
        showSlide(e){
            this.triggerEvent('onSlideShow',{slideShow:!this.data.slideShow})
        },
        setVersionReturn(){
            this.getDiskList()
        },
        getDiskList(reachBottom = false){
            // wx.showLoading({
            //   title: '加载中...',
            // })
            if (!reachBottom) this.setData({
              currentPage: 1
            })
            let sourceID = this.properties.sourceID,{catalogue,dataList,searchFilter,DOMAIN}= this.data
            let param = {
                sourceID:searchFilter?searchFilter.sourceID:sourceID,
                currentPage:this.data.currentPage,
                pageSize:500,
                keyword:this.data.keyword,
                fileType:searchFilter?(searchFilter.filesType?searchFilter.filesType:''):'',
                timeFrom:searchFilter?(searchFilter.timeFrom?searchFilter.timeFrom:''):'',
                timeTo:searchFilter?(searchFilter.timeTo?searchFilter.timeTo:''):'',
                maxSize:searchFilter?(searchFilter.maxSize?searchFilter.maxSize:''):'',
                minSize:searchFilter?(searchFilter.minSize?searchFilter.minSize:''):'',
                userID:searchFilter?(searchFilter.userID?searchFilter.userID:''):''
            }
            if(sourceID==0){
                if(catalogue[0]){
                    if(catalogue[0].type=='fav'){
                        param.block = 'fav'
                    }else if(catalogue[0].type=='files'&&catalogue.length==1){
                        param.block = 'files'
                        reachBottom = false
                    }else if(catalogue[0].type=='recentDoc'){
                        param.block = 'userRencent'
                    }else if(catalogue[0].type=='photo'||catalogue[0].type=='image'){
                        param.fileType = 'jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai'
                    }else if(catalogue[0].type=='doc'||catalogue[0].type=='music'||catalogue[0].type=='movie'||catalogue[0].type=='zip'||catalogue[0].type=='other'){
                        param.fileType = catalogue[0].fileType
                    }else if(catalogue[0].type=='recycle'){
                        param.block = 'recycle'
                    }else if(catalogue[0].type=='shareLink'){
                        param.isShare = 1
                    }else if(catalogue[0].type=='fileTag'){
                        if(catalogue.length>1){
                            param.tagID = catalogue[1].labelId
                        }else{
                            if(catalogue[0].labelId){
                                param.tagID = catalogue[0].labelId
                            }else{
                                param.block = 'fileTag'
                            }
                        }
                    }
                }
            }
            if(this.data.sortField){
                param.sortField = this.data.sortField
            }
            if(this.data.sortType){
                param.sortType = this.data.sortType
            }
            let url = URL.getDiskList
            if(catalogue[0].type=='shareLink'&&catalogue.length==1){
                url = URL.getShareList
                this.setData({
                  isLinkShare:true
                })
            }else if(catalogue[0].type=='shareToMe'){
                url = URL.getShareList
                param.isShareTo = 1
            }else if(catalogue[0].type=='shareTo'){
                url = URL.shareToMeList
                param.isSharetoMe = 1
            }else if(catalogue[0].type=='files'){
              if(catalogue.length==2&&catalogue[1].label=='我的分享'){
                url = URL.getShareList
                param.block = ''
                param.isShare = 1
              }
            }
            if(this.data.repeatSourceID){
                param.sourceID = 0
                param.fileType = 'all'
                param.repeatSourceID = this.data.repeatSourceID
            }
            if(this.data.repeatName){
                param.repeatName = this.data.repeatName
            }
            req("GET",url, param, {}).then(data => {
              // wx.hideLoading()
                if(param.block=='fav'){
                    data.folderList.map(c => {
                        if(c.createTime )c.createTime = timeFormat('yyyy/MM/dd hh:mm:ss',c.createTime*1000);
                        if(c.modifyTime)c.modifyTime = timeFormat('yyyy/MM/dd hh:mm:ss',c.modifyTime*1000);
                        c.gmtCreate = c.createTime?c.createTime:c.modifyTime
                        c.fileSize = renderSize(c.size);
                        c.checked = false;
                        if(c.createUserJson)c.createUserJson = JSON.parse(c.createUserJson)
                        if(c.modifyUserJson)c.modifyUserJson = JSON.parse(c.modifyUserJson)
                        if (/jpeg|jpg|gif|png|webp|jfif|bmp|dpg/.test(c.fileType)) c.thumb = c.path
                        if (c.thumb) {
                            c.thumb = /http|https/.test(c.thumb) ? c.thumb : DOMAIN + c.thumb;
                            let index = c.thumb.lastIndexOf('.');
                            if (/jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg/.test(c.fileType)) c.thumb = `${c.thumb.substring(0,index)}${c.thumb.substring(index,c.thumb.length)}&nameSuffix=small`
                        } else {
                            if(c.icon=='folder'){
                                c.thumb = '/images/rootIcon/folder.png'
                            }else{
                                c.thumb = initIconPath(c.fileType);
                            }   
                        }
                    })
                    data.fileList = data.folderList
                    data.folderList = []
                    this.setData({
                      favList:true
                    })
                }else{
                    if(param.block=='files'&&catalogue.length==1){
                      let arr = [
                        {
                          name:'我的分享',
                          icon: "shareLink",
                        }
                      ]
                      data.folderList.map(c=>{
                        if(c.icon=='box'){
                          arr.push(c)
                        }
                      })
                      data.folderList = arr
                      // data.folderList.splice(0,1)
                    }
                    data.fileList.map((c,i) => {
                      if(c.oexeContent)c.oexeContent = JSON.parse(c.oexeContent)
                      if(c.fileType=='oexe'&&c.oexeContent&&c.oexeContent.type=='lnk'){
                        if(c.oexeContent.icon&&c.oexeContent.icon=='colorPicker'){
                          c.fileType = c.oexeContent.icon
                          c.oexeContent.name = '桌面取色'
                        }else if(c.oexeContent.icon&&c.oexeContent.icon=='videoRecording'){
                          c.fileType = c.oexeContent.icon
                          c.oexeContent.name = '视频录制'
                        }else{
                          c.fileType = c.oexeContent.fileType
                        }
                        c.name = c.oexeContent.name
                        if(c.oexeIsFolder==1){
                          // data.fileList.splice(i,1)
                          data.folderList.push(c)
                          return
                        }
                      }
                      if(c.fileType=='oexe'&&c.oexeContent&&c.oexeContent.type=='url'){
                        c.fileType = c.oexeContent.type
                        c.name = c.name.split('.')[0]
                      }
                      if(c.createTime )c.createTime = timeFormat('yyyy/MM/dd hh:mm:ss',c.createTime*1000);
                      if(c.modifyTime)c.modifyTime = timeFormat('yyyy/MM/dd hh:mm:ss',c.modifyTime*1000);
                      c.gmtCreate = c.createTime?c.createTime:c.modifyTime
                      c.fileSize = renderSize(c.size);
                      c.checked = false;
                      if(this.data.newFileCheckedId&&c.sourceID==this.data.newFileCheckedId){
                        c.checked = true
                      }
                      if(c.createUserJson)c.createUserJson = JSON.parse(c.createUserJson)
                      if(c.modifyUserJson)c.modifyUserJson = JSON.parse(c.modifyUserJson)
                      let isVideo = /mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv|vob/,
                          isImage = /jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg|cr2|dng|erf|raf|kdc|mrw|nrw|nef|orf|rw2|pef|srf|dcr|arw/
                      if (/jpeg|jpg|gif|png|webp|jfif|bmp|dpg/.test(c.fileType)) c.thumb = c.path
                      if (c.thumb&&(isImage.test(c.fileType)||isVideo.test(c.fileType))) {
                        c.thumb = /http|https/.test(c.thumb) ? c.thumb : DOMAIN + c.thumb;
                        let index = c.thumb.lastIndexOf('.');
                        if (isImage.test(c.fileType)) c.thumb = `${c.thumb.substring(0,index)}${c.thumb.substring(index,c.thumb.length)}&nameSuffix=small`;
                      } else {
                        c.thumb = initIconPath(c.fileType);
                      }
                    })
                    data.folderList.map((c,index) => {
                        if(c.icon=='info'){
                          data.folderList.splice(index,1)
                        }
                        if(c.createTime )c.createTime = timeFormat('yyyy/MM/dd hh:mm:ss',c.createTime*1000);
                        if(c.modifyTime)c.modifyTime = timeFormat('yyyy/MM/dd hh:mm:ss',c.modifyTime*1000);
                        c.gmtCreate = c.createTime?c.createTime:c.modifyTime
                        if(!data.current)c.thumb = '/images/rootIcon/folder.png'
                        if(data.current&&param.block=='files') c.thumb = '/images/rootIcon/folder.png'
                        c.fileSize = renderSize(c.size);
                        if(c.createUserJson)c.createUserJson = JSON.parse(c.createUserJson)
                        if(c.modifyUserJson)c.modifyUserJson = JSON.parse(c.modifyUserJson)
                        c.checked = false;
                    })
                }
                if (reachBottom) {
                    let {
                        folderList,
                        fileList
                    } = dataList;
                    folderList = [...folderList, ...data.folderList];
                    fileList = [...fileList, ...data.fileList];
                    dataList = {
                        folderList,
                        fileList
                    }
                } else {
                  dataList = data;
                }
                // console.log(dataList)
                this.setData({
                    dataList:dataList,
                    loadingState: 0,
                    filesSum:data.total,
                    repeatSourceID:0,
                    repeatName:''
                })
                if(this.data.loadingState==0){
                    wx.hideLoading()
                } 
            }, err => {
                console.log(err);
            });
        },
        /**
         * 生命周期函数--监听页面卸载
         */
        onUnload: function () {
            if (this.data.timer) clearInterval(this.data.timer)
        },

        /**
         * 页面相关事件处理函数--监听用户下拉动作
         */
        onPullDownRefresh: function () {
            this.setData({
                currentPage:1,
            })
            this.cancelEdit()
            this.getDiskList()
        },
        //回收站显示更多
        showMoreRecyle(){
            this.triggerEvent('showMoreRecyle')
        },
        // 子页面组件事件
        onCatalogueChange: function (e) {
            let {
                catalogue,
                sourceID,
                labelId,
                type,
                targetType,
                auth
            } = e.detail
            let {isEdit,role} = this.data,addAuth = role['explorer.add'];
            if(targetType==2){
              if(auth.indexOf("9") == -1){
                addAuth = false
              }else{
                addAuth = true
              }
            }
            this.setData({
                catalogue,
                sourceID,
                currentPage: 1,
                block:type,
                labelId,
                searchFilter:'',
                isEdit:false,
                keyword:'',
                noSelectAll:false,
                moreFav:false,
                DOMAIN:wx.getStorageSync('DOMAIN'),
                addAuth,
                loadingState:1
            })
            this.triggerEvent('onSetSearchFilter',{sourceID,catalogue})
            this.getDiskList()
        },
        /**
         * 页面上拉触底事件的处理函数
         */
        onReachBottom: function () {
            this.setData({
                currentPage: this.data.currentPage + 1
            })
            this.getDiskList(true)
        },
        //选择排序方式
        choseSortField(e){
            let {index,item} = e.currentTarget.dataset
            this.setData({
              sortFieldIndex:index,
              sortField:item.field
            })
            this.showSortList()
            this.getDiskList()
        },
        // choseSortField(param){
        //     this.setData({
        //         sortField:param
        //     })
        //     this.getDiskList()
        // },
        //选择排序顺序
        choseSortType(e){
            let {index,item} = e.currentTarget.dataset
            this.setData({
              sortTypeIndex:index,
              sortType:item.type
            })
            this.showSortList()
            this.getDiskList()
        },
        // choseSortType(param){
        //     this.setData({
        //         sortType:param
        //     })
        //     this.getDiskList()
        // },
        // 关闭筛选模态框
        onClickHide() {
            this.setData({
                showFilterTypeModal: false,
                showFileTypeModal: false
            })
        },
        //选择筛选类型
        choseType(e) {
            let {type} = e.currentTarget.dataset
            this.setData({
                isTile: type==0?false:true
            })
        },
        //筛选框显示
        showSortList(){
            this.setData({sortListShow:!this.data.sortListShow})
        },
        // 选择根目录
        choseCatelogue(e) {
            let {
                index
            } = e.currentTarget.dataset, {
                catalogue
            } = this.data;
            if (index == catalogue.length - 1) return;
            catalogue = catalogue.splice(0, index + 1);
            this.setData({
                sourceID: catalogue[index].sourceID,
                catalogue,
                currentPage: 1,
                searchFilter:'',
                isEdit:false,
                noSelectAll:false,
            })
            this.triggerEvent('onSetSearchFilter',{sourceID:catalogue[index].sourceID,catalogue})
            this.getDiskList();
        },
        // 折叠搜索
        foldSearch() {
            let {
                showSearch
            } = this.data;
            this.setData({
                showSearch: !showSearch
            })
            if (showSearch) {
                this.setData({
                    currentPage: 1
                })
                this.getDiskList();
            }
        },
        // 输入内容
        inputSearchWord(e) {
            this.setData({
                keyword: e.detail
            })
        },
        // 搜索
        onSearchBegin(e) {
            this.setData({
                currentPage: 1
            })
            this.getDiskList();
        },
        // 点击按钮
        showActionSheet() {
            this.setData({
                addShow:true
            })
        },
        closeAddBox(){
            this.setData({
                addShow:false
            })
        },
        closeClipboard(){
          this.setData({
            clipboardShow: false
          })
        },
        clipboardTextInput(e){
          let reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|&|-)+)/g,{value} = e.detail,{isWeburl} = this.data;
          if(reg.test(value)){
            isWeburl = true
          }else{
            isWeburl = false
          }
          this.setData({
            clipboardText:value,
            isWeburl
          })
        },
        addFiles(e){
            let {index} = e.currentTarget.dataset
            let {
                sourceID
            } = this.data;
            this.closeAddBox()
            switch (index) {
                case 0:  
                    this.choseFiles('video', sourceID,'cloud');
                    break;
                case 1:
                    this.choseFiles('image', sourceID,'cloud');
                    break;
                case 2:
                    this.choseFiles('fiels', sourceID,'cloud');
                    break;
                case 3:
                    let clipboardText,that = this,reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|&|-)+)/g;
                    this.setData({
                      clipboardShow: true,
                    })
                    wx.getClipboardData({
                      success(res) {
                        clipboardText = res.data
                        that.setData({
                          clipboardText,
                          clipboardShow: true,   
                          isWeburl:reg.test(clipboardText)?true:false
                        })
                      },fail(err){
                        console.log(err)
                      }
                    })
                    break;
                case 4:
                    this.setData({
                        modalFunIndex: 0
                    })
                    this.foldModal();
                    break;
                default:
                    break;
            }
        },
        saveClipboard(e){
          let {type} = e.currentTarget.dataset,{clipboardText} = this.data,content,param,regu =/^[ ]+$/
          console.log(clipboardText,regu)
          if(!clipboardText||regu.test(clipboardText)){
            wx.showToast({
              title: '内容不能为空',
              icon:'none'
            })
            return
          }
          if(type==1){
            content = {
              type:'url',
              value:clipboardText,
              width:'80%',
              height:'70%',
            }
            param = {
              sourceID:this.data.sourceID,
              operation:'mkfile',
              content: JSON.stringify(content),
              name:'新建网址.oexe'
            }
          }else{
            param = {
              sourceID:this.data.sourceID,
              operation:'mkfile',
              content: clipboardText,
              name:'新建文件.txt'
            }
          }
          req('POST', URL.diskOperation, {}, param, false).then(data => {
              wx.hideLoading()
              this.setData({
                currentPage:1,
                clipboardShow:false,
                newFileCheckedId:data.source.sourceID
              })
              wx.showToast({
                title: '保存成功',
              })
              let that = this
              setTimeout(function(){
                that.getDiskList()
              },500)
          }).catch(err => {
              console.log(err)
              wx.showToast({
                title: err.message,
                icon: 'none'
              })
          })
        },
        createNewFolder: function () {
            this.setData({
                modalFunIndex: 0
            })
            this.foldModal();
        },
        foldModal() {
            let {
                showModal
            } = this.data;
            this.setData({
                showModal: !showModal
            })
            if (showModal) this.cancelEdit();
        },
        // 显示重命名
        showRename() {
            let {
                dataList,
                renameType,
                modalFuns,
                modalFunIndex
            } = this.data, total = 0, one = '';
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                    renameType = 0
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                    renameType = 1
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            if (total > 1) {
                wx.showToast({
                    title: '重命名不支持多个文件修改',
                    icon: "none"
                })
                return;
            }
            modalFunIndex = 1;
            modalFuns[modalFunIndex].value = one.name;
            this.setData({
                modalFunIndex,
                renameData: one,
                renameType,
                modalFuns,
                newFileName:one.name
            })
            this.foldModal();
        },
        // 取消管理
        cancelEdit(e) {
            let {
                dataList
            } = this.data;
            dataList.folderList.map(c => c.checked = false);
            dataList.fileList.map(c => c.checked = false);
            this.setData({
                isEdit: false,
                showMoveFile: false,
                dataList,
                noSelectAll:false,
                moreFav:false,
            })
            let prop = {
                detail:{
                    prop:dataList
                }
            }
            this.setCheckedFile(prop)
            if(wx.getStorageSync('needReget')){
                wx.removeStorageSync('needReget')
                this.getDiskList()
            }
        },
        createNewFolderConfirm() {
            let {
                sourceID,
                directoryName
            } = this.data;
            if (!directoryName || !directoryName.trim()) {
                wx.showToast({
                    title: '文件夹名不能为空！',
                    icon: 'none'
                })
                return;
            }
            req('POST', URL.addDirectory, {}, {
                name:directoryName,
                parentID:sourceID,
            }, false).then(data => {
                this.foldModal();
                this.setData({
                    currentPage: 1
                })
                this.getDiskList();
                if (this.data.showMoveFile) this.selectComponent('#moveFiles').getDiskList();
            }).catch(err => {
                console.log(err)
                wx.showToast({
                    title: err.message,
                    icon: 'none'
                })
            })
        },
        // 重命名
        renameFile() {
            let {
                newFileName,
                renameData,
                renameType
            } = this.data;
            console.log(newFileName)
            if (typeof (newFileName) == 'object') {
                wx.showToast({
                    title: '请输入文件名!',
                    icon: 'none'
                })
                return;
            }
            if (newFileName.trim() == '') {
                wx.showToast({
                    title: '请输入文件名!',
                    icon: 'none'
                })
                return;
            }
            let param = {
                dataArr:[{
                    name:newFileName,
                    oldName:renameData.name,
                    parentID:renameData.parentID,
                    parentLevel:renameData.parentLevel,
                    sourceID:renameData.sourceID,
                    type:renameData.fileType?'file':'folder'
                }],
                operation:'rename'    
            }
            wx.showLoading({
                title: '加载中…',
                mask: true
            })
            req('POST', URL.diskOperation, {}, param, false).then(data => {
                wx.hideLoading();
                wx.showToast({
                    title: '修改成功！',
                    icon: 'none'
                })
                this.foldModal();
                this.setData({
                    currentPage: 1
                })
                let that = this
                setTimeout(function(){
                    that.getDiskList();
                },500)
                
            }).catch(err => {
                console.log(err)
                wx.hideLoading();
                wx.showToast({
                    title: err.message,
                    icon: 'none'
                })
            })
        },
        showAttribute(e){
            let {dataList,catalogue} = this.data, total = 0, one = '';
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            if (total > 1) {
                wx.showToast({
                    title: '暂不支持同时查看多个文件属性',
                    icon: "none"
                })
                return;
            }
            this.cancelEdit();
            this.selectComponent("#operateBar").onCloseMore()
            this.selectComponent("#attribute").getAttribute(one)
            if(one.icon!='folder')this.selectComponent("#attribute").getHistory(one)
            this.selectComponent("#attribute").getTrends()
            this.selectComponent("#attribute").getComments()
        },
        showFormat(e){
            let {dataList,catalogue} = this.data, total = 0, one = '';
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            if (total > 1) {
                wx.showToast({
                    title: '暂不支持同时查看多个文件属性',
                    icon: "none"
                })
                return;
            }
            this.cancelEdit();
            this.selectComponent("#operateBar").onCloseMore()
            this.selectComponent("#formatConvert").getFormat(one)
        },
        //格式转换
        convertFile(e){
            let {suffix,sourceID} = e.detail,
            param = {
                suffix: suffix,
                sourceID:sourceID,
                sourceIDTo:this.data.sourceID,
                taskID:genUuid()
            }
            // console.log(param)
            req('POST', URL.convert, {}, param, false).then(data => {
                this.checkConvert(param.taskID)
            }).catch(err => {
                this.cancelEdit();
                this.selectComponent("#operateBar").onCloseMore()
                wx.showToast({
                    title: err.message,
                    icon: 'none'
                })
            })
        },
        //查看转换是否完成
        checkConvert(taskID){
            let param = {
                taskID:taskID
            }
            wx.showLoading({
              title: '格式转换中...',
            })
            req('GET', URL.checkZip, param, {}, false).then(data => {         
                this.cancelEdit();
                let that = this
                setTimeout(function(){
                    wx.showToast({
                        title: '转换成功',
                        icon: 'none'
                    })      
                    that.setData({
                        currentPage: 1,
                    })
                    that.getDiskList();
                },1000)
            }).catch(err => {
                wx.hideLoading({})
                this.cancelEdit();
                this.selectComponent("#operateBar").onCloseMore()
                wx.showToast({
                    title: err.message,
                    icon: 'none'
                })
            })
        },
        repeatFile(e){
            let {dataList} = this.data, total = 0, one = '';
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            if (total > 1) {
                wx.showToast({
                    title: '暂不支持同时操作多个文件',
                    icon: "none"
                })
                return;
            }
            if(one.fileType==''){
                wx.showToast({
                    title: '暂不支持文件夹查重',
                    icon: "none"
                })
                return;
            }
            this.cancelEdit();
            this.selectComponent("#operateBar").onCloseMore()
            this.setData({
                currentPage: 1,
                repeatSourceID:one.sourceID
            })
            this.getDiskList()
        },
        repeatFileName(e){
            let {dataList} = this.data, total = 0, one = '';
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            if (total > 1) {
                wx.showToast({
                    title: '暂不支持同时操作多个文件',
                    icon: "none"
                })
                return;
            }
            if(one.fileType==''){
                wx.showToast({
                    title: '暂不支持文件夹查重',
                    icon: "none"
                })
                return;
            }
            this.cancelEdit();
            this.selectComponent("#operateBar").onCloseMore()
            this.setData({
                currentPage: 1,
                repeatSourceID:one.sourceID,
                repeatName:one.name
            })
            this.getDiskList()
        },
        //发送桌面快捷方式
        sendDesktop(e){
          let {dataList} = this.data, total = 0, one = '';
          dataList.folderList.map(c => {
              if (c.checked) {
                  total++;
                  one = c;
              }
          })
          dataList.fileList.map(c => {
              if (c.checked) {
                  total++;
                  one = c;
              }
          })
          if (total == 0) {
              wx.showToast({
                  title: '未选择文件',
                  icon: 'none'
              })
              return;
          }
          if (total > 1) {
              wx.showToast({
                  title: '暂不支持同时操作多个文件',
                  icon: "none"
              })
              return;
          }
          console.log(one)
          let content = {
            type:'lnk',
            sourceID:one.sourceID,
            isFolder:one.isFolder,
            fileType:one.fileType?one.fileType:'',
            name:one.name,
            fileID:one.fileID,
            icon:one.isFolder?'folder':'file'
          },param = {
            sourceID:wx.getStorageSync('options').desktop.sourceID,
            operation:'mkfile',
            content: JSON.stringify(content),
            fileType: one.fileType?one.fileType:'',
            isFolder: one.isFolder,
            name: one.name+'.oexe'
          }
          req('POST', URL.diskOperation, {}, param, false).then(data => {
            wx.showToast({
              title: '操作成功',
              icon: 'none'
            })
            this.cancelEdit();
            this.selectComponent("#operateBar").onCloseMore()
            this.setData({
                currentPage: 1
            })
            let that = this
            setTimeout(function(){
                that.getDiskList();
            },500)    
          }).catch(err => {
              this.cancelEdit();
              this.selectComponent("#operateBar").onCloseMore()
              wx.showToast({
                  title: err.message,
                  icon: 'none'
              })
          })
        },
        //创建快捷方式
        creatShortcut(e){
          let {dataList} = this.data, total = 0, one = '';
          dataList.folderList.map(c => {
              if (c.checked) {
                  total++;
                  one = c;
              }
          })
          dataList.fileList.map(c => {
              if (c.checked) {
                  total++;
                  one = c;
              }
          })
          if (total == 0) {
              wx.showToast({
                  title: '未选择文件',
                  icon: 'none'
              })
              return;
          }
          if (total > 1) {
              wx.showToast({
                  title: '暂不支持同时操作多个文件',
                  icon: "none"
              })
              return;
          }
          console.log(one)
          let content = {
            type:'lnk',
            sourceID:one.sourceID,
            isFolder:one.isFolder,
            fileType:one.fileType?one.fileType:'',
            name:one.name,
            fileID:one.fileID,
            icon:one.isFolder?'folder':'file'
          },param = {
            sourceID:one.parentID,
            operation:'mkfile',
            content: JSON.stringify(content),
            fileType: one.fileType?one.fileType:'',
            isFolder: one.isFolder,
            name: one.name+'.oexe'
          }
          req('POST', URL.diskOperation, {}, param, false).then(data => {
            wx.showToast({
              title: '操作成功',
              icon: 'none'
            })
            this.cancelEdit();
            this.selectComponent("#operateBar").onCloseMore()
            this.setData({
                currentPage: 1
            })
            let that = this
            setTimeout(function(){
                that.getDiskList();
            },500)    
          }).catch(err => {
              this.cancelEdit();
              this.selectComponent("#operateBar").onCloseMore()
              wx.showToast({
                  title: err.message,
                  icon: 'none'
              })
          })
        },
        showZipPreview(e){
            let {item} = e.detail
            this.selectComponent("#zipPreview").getZipList(item)
        },
        //添加描述
        addDesc(e){
            let param = {
                desc: e.detail.desc,
                operation: "desc",
                sourceID: e.detail.sourceID
            }
            req('POST', URL.diskOperation, {}, param, false).then(data => {
                wx.showToast({
                    title: '添加成功！',
                    icon: 'none'
                })
                this.setData({
                    currentPage: 1
                })
                this.getDiskList();
            }).catch(err => {
                wx.showToast({
                    title: err.message,
                    icon: 'none'
                })
            })
        },
        //打开文件
        openFile(e){
            let {dataList,catalogue} = this.data, total = 0, one = '';
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            if (total > 1) {
                wx.showToast({
                    title: '暂不支持同时打开多个文件',
                    icon: "none"
                })
                return;
            }
            if(one.fileType){
                this.selectComponent("#filesList").openFile(one)
            }else{  
                catalogue.push({
                    label: one.name,
                    sourceID:one.sourceID,
                })
                let a = {
                    detail:{
                        sourceID:one.sourceID,
                        catalogue,
                    }
                }
                this.onCatalogueChange(a)
            }    
            this.cancelEdit();
            this.selectComponent("#operateBar").onCloseMore()
        },
        //置顶文件
        topFile(e){
            let {
                dataList,
            } = this.data, total = 0, one = '',param;
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            if (total > 1) {
                wx.showToast({
                    title: '暂不支持同时操作多个文件',
                    icon: "none"
                })
                return;
            }
            if(one.sort==0){
                param = {
                    parentID:one.parentID,
                    operation:'top',
                    sourceID:one.sourceID,
                }
            }else{
                param = {
                    sourceID:one.sourceID,
                    operation:'cancelTop',
                }
            }
            req('POST', URL.diskOperation, {}, param, false).then(data => {
                wx.showToast({
                    title: one.sort>0?'取消置顶':'置顶成功！',
                    icon: 'none'
                })
                this.setData({
                    currentPage: 1
                })
                this.getDiskList();
                this.cancelEdit();
                this.selectComponent("#operateBar").onCloseMore()
            }).catch(err => {
                this.cancelEdit();
                this.selectComponent("#operateBar").onCloseMore()
                wx.showToast({
                    title: err.message,
                    icon: 'none'
                })
            })
        },
        //创建压缩包
        zipFile(e){
            let {suffix,index} = e.detail,{dataList} = this.data, total = 0,dataArr = [],param={};
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    dataArr.push({name:c.name,parentID:c.parentID,parentLevel:c.parentLevel,sourceID:c.sourceID,type:'folder'})
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    dataArr.push({name:c.name,parentID:c.parentID,parentLevel:c.parentLevel,sourceID:c.sourceID,type:'file'})
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            param = {
                dataArr,
                name:dataArr.length>1?this.data.catalogue[this.data.catalogue.length-1].label:dataArr[0].name.split('.')[0]+'.'+suffix,
                sourceLevel:dataArr[0].parentLevel,
                suffix: suffix,
                sourceID:dataArr[0].parentID,
                taskID:genUuid()
            }
            if(index==1)param.level = 0
            req('POST', URL.zipFile, {}, param, false).then(data => {
                this.checkZip(param.taskID)
            }).catch(err => {
                this.cancelEdit();
                this.selectComponent("#operateBar").onCloseMore()
                wx.showToast({
                    title: err.message,
                    icon: 'none'
                })
            })
        },
        // 检查文件是否需要解密
        checkIsEncrypted(param){
            req('GET',URL.checkIsEncrypted,{sourceID:param.sourceID},{},false).then(data => {
                if(data){
                    this.setData({
                        isEncrypted:true,
                        checkedParam:param
                    })
                    this.showEncrypt()
                }else{
                    this.setData({
                        isEncrypted:false
                    })
                    this.unzip(param,false)   
                }
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
            let {password,checkedParam} = this.data
            if(password==null){
                wx.showToast({
                    title: '请输入压缩包密码',
                    icon:'none'
                })
                return
            }
            this.unzip(checkedParam,false)
        },
        //解压文件
        unzipFile(e){
            let {index} = e.detail,{dataList,sourceID} = this.data, total = 0, one = '',param;
            if(e.detail.sourceID){
                if(index==0){
                    param = {
                        sourceID:e.detail.sourceID,
                        sourceIDTo:sourceID,
                        index:e.detail.choseIndex,
                        fullName:e.detail.fullName,
                        directory:e.detail.directory
                    }
                    this.unzip(param)
                }else{
                    this.setData({
                        showMoveFile:true,
                        moveType:'',
                        unzipID:e.detail.sourceID,
                        isZip:true,
                        fullName:e.detail.fullName,
                        choseIndex:e.detail.choseIndex,
                        zipDirectory:e.detail.directory
                    })
                }  
                return
            }
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            if (total > 1) {
                wx.showToast({
                    title: '暂不支持同时操作多个文件',
                    icon: "none"
                })
                return;
            }
            if(index==0){
                param = {
                    sourceID:one.sourceID,
                    sourceIDTo:sourceID
                }
                this.unzip(param)
            }else if(index==1){
                param = {
                    pathTo:one.name.split('.')[0],
                    sourceID:one.sourceID,
                    sourceIDTo:sourceID
                }
                this.unzip(param)
            }else{
                this.setData({
                    showMoveFile:true,
                    moveType:'',
                    unzipID:one.sourceID
                })
            }         
        },
        unzipFileTo(e){
            let {sourceID} = e.detail,{unzipID,choseIndex,fullName,zipDirectory} = this.data,
            param = {
                sourceID:unzipID,
                sourceIDTo:sourceID
            }
            if(fullName){
                param.index = choseIndex
                param.fullName = fullName
                param.directory = zipDirectory
            }
            this.unzip(param)
        },
        unzip(param,checkFlag = true){
            if(checkFlag){
                this.checkIsEncrypted(param)
                return
            } 
            if(this.data.password)param.password = this.data.password
            wx.showLoading({
                title:'解压中...'
            })
            req('POST', URL.unzipFile, {}, param, false).then(data => {
                wx.showToast({
                    title: '解压成功',
                    icon: 'none'
                })
                this.setData({
                    currentPage: 1
                })
                this.getDiskList();
                this.cancelEdit();  
            }).catch(err => {
                wx.hideLoading({})
                this.cancelEdit();
                this.selectComponent("#operateBar").onCloseMore()
                this.showEncrypt()
                wx.showToast({
                    title: err.message,
                    icon: 'none'
                })
                this.setData({
                    password:''
                })
            })
        },
        //查看压缩是否完成
        checkZip(taskID){
            let param = {
                taskID:taskID
            }
            wx.showLoading({
              title: '压缩中...',
            })
            req('GET', URL.checkZip, param, {}, false).then(data => {
                wx.showToast({
                    title: '压缩成功',
                    icon: 'none'
                })
                this.setData({
                    currentPage: 1,
                })
                this.getDiskList();
                this.cancelEdit();
                // this.selectComponent("#operateBar").onCloseMore()
            }).catch(err => {
                wx.hideLoading({})
                this.cancelEdit();
                this.selectComponent("#operateBar").onCloseMore()
                wx.showToast({
                    title: err.message,
                    icon: 'none'
                })
            })
        },
        // 取消外链分享
        cancelShare(){
            let {dataList} = this.data,shareIDStr = []
            dataList.folderList.map(c => {
                if (c.checked) {
                    shareIDStr.push(c.shareID)
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    shareIDStr.push(c.shareID)
                }
            })
            wx.showModal({
                title: '提示',
                content: '确认取消该链接分享？',
                success: res => {
                    if (res.confirm) {
                        req('POST',URL.cancelShare, {}, {shareIDStr:shareIDStr.toString()}, false).then(data => {
                            console.log(data)
                            wx.showToast({
                                title: '取消成功！',
                                icon: 'none'
                            })
                            this.setData({
                                currentPage: 1
                            })
                            this.getDiskList();
                            this.cancelEdit();
                        }).catch(err => {
                            this.cancelEdit();
                            wx.showToast({
                                title: err.message,
                                icon: 'none'
                            })
                        })
                    }
                }
            })
        },
        //外链分享
        shareLink(){
            let {dataList} = this.data,total = 0, one = ''
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    one = {
                        sourceID:c.sourceID,
                        thumb:c.thumb,
                        name:c.name
                    };
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    one = {
                        sourceID:c.sourceID,
                        thumb:c.thumb,
                        name:c.name
                    };
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            if (total > 1) {
                wx.showToast({
                    title: '暂不支持同时操作多个文件',
                    icon: "none"
                })
                return;
            }
            this.cancelEdit();
            this.selectComponent("#operateBar").onCloseMore()
            // console.log(one)
            wx.setStorageSync('shareFile', one)
            wx.navigateTo({
              url: '/pages/sharePage/sharePage?item='+JSON.stringify(one),
            })
        },
        //上传新版本
        uploadNewFile(e){
            let {dataList} = this.data,that = this,
            one = dataList.fileList.filter(c => c.checked),itemList = ['视频','图片','文件'],type
            const isVideo = /mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv|vob/,
            isImage = /jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg/,
            isFile = /pptx|ppt|xlsx|xls|doc|docx|pdf|wps/;
            if(one.length>1){
                wx.showToast({
                    title:'暂不支持修改多个文件！',
                    icon:'none'
                })
                return
            }
            if(isVideo.test(one[0].fileType)){
                itemList = ['视频']
                type = 1
            }else if(isImage.test(one[0].fileType)){
                itemList = ['图片']
                type = 0
            }else if(isFile.test(one[0].fileType)){
                itemList = ['文件']
                type = 2
            }else{
                itemList = ['文件']
                type = 2
            }
            wx.showActionSheet({
                itemList:itemList,
                success(res){
                    console.log(res)
                    if (res.tapIndex === 0){
                        that.selectComponent("#attribute").addFiles(type,one[0].sourceID,one[0].parentID,one[0].name)
                    }else if (res.tapIndex === 1){
                        that.selectComponent("#attribute").addFiles(type,one[0].sourceID,one[0].parentID,one[0].name)
                    }else if (res.tapIndex === 2){
                        that.selectComponent("#attribute").addFiles(type,one[0].sourceID,one[0].parentID,one[0].name)
                    }
                    that.cancelEdit();
                    that.selectComponent("#operateBar").onCloseMore()
                }
            })
        },
        //收藏文件
        collectFile(e){
            let {isDisFav} = e.detail,{dataList} = this.data,one='', total = 0, dataArr = [],param={};
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    dataArr.push({name:c.name,favID:c.favID,id:c.sourceID,path:c.path})
                    one = c
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    dataArr.push({name:c.name,favID:c.favID,id:c.sourceID,path:c.path})
                    one = c
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            if (total > 1&&!isDisFav) {
                wx.showToast({
                    title: '暂不支持同时收藏多个文件',
                    icon: "none"
                })
                return;
            }
            if(!isDisFav){
                param = {
                    name:one.name,
                    operation:'fav',
                    sourceID:one.sourceID,
                    type:one.fileType?'file':'folder'
                }
            }else{
                param = {
                    dataArr,
                    operation:'delFav',
                }
            }
            req('POST', URL.diskOperation, {}, param, false).then(data => {
                wx.showToast({
                    title: isDisFav?'取消收藏':'收藏成功！',
                    icon: 'none'
                })
                this.cancelEdit();
                this.selectComponent("#operateBar").onCloseMore()
                this.setData({
                    currentPage: 1
                })
                let that = this
                setTimeout(function(){
                    that.getDiskList();
                },500)    
            }).catch(err => {
                this.cancelEdit();
                this.selectComponent("#operateBar").onCloseMore()
                wx.showToast({
                    title: err.message,
                    icon: 'none'
                })
            })
        },
        //添加标签
        chooseTag(e){
            let {labelId,block} = e.detail,{dataList} = this.data, total = 0, one = '';
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            if (total > 1) {
                wx.showToast({
                    title: '暂不支持同时操作多个文件',
                    icon: "none"
                })
                return;
            }
            let param = {
                block:block,
                files:one.sourceID,
                labelId:labelId
            }
            req('POST',URL.chooseTag, {}, param, false).then(data => {
                // this.getDiskList()
            })
        },
        setCheckedFile(e){
            let {prop} = e.detail,{checkedSum,catalogue} = this.data
            checkedSum = prop.fileList.filter(c => c.checked).length + prop.folderList.filter(c => c.checked).length
            let folderSum = prop.folderList.filter(c => c.checked)
            this.selectComponent('#operateBar').setOperate({catalogue,checkedSum,folderSum})
            this.setData({
                dataList:prop,
                checkedSum,
                // noSelectAll:checkedSum==0?1:0,
                isEdit:checkedSum==0?false:true
            })
        },
        //全选全不选
        selectAllFile(e){
            let{isSelectAll} = e.detail,{dataList,checkedSum} = this.data
            if(isSelectAll){
                dataList.fileList.map(c=>{c.checked=true})
                dataList.folderList.map(c=>{c.checked=true})
            }else{
                dataList.fileList.map(c=>{c.checked=false})
                dataList.folderList.map(c=>{c.checked=false})
            }
            checkedSum = dataList.fileList.filter(c => c.checked).length + dataList.folderList.filter(c => c.checked).length
            this.setData({
                dataList,
                checkedSum,
                // noSelectAll:checkedSum==0?1:0,
            })
            if(!isSelectAll){
                this.cancelEdit()
            }
        },
        //下载文件
        downloadFile(e){
            let {
                dataList,
            } = this.data, total = 0, one = '';
            dataList.folderList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            dataList.fileList.map(c => {
                if (c.checked) {
                    total++;
                    one = c;
                }
            })
            if (total == 0) {
                wx.showToast({
                    title: '未选择文件',
                    icon: 'none'
                })
                return;
            }
            if (total > 1) {
                wx.showToast({
                    title: '暂不支持同时下载多个文件',
                    icon: "none"
                })
                return;
            }   
            if(/jpeg|jpg|gif|png|webp|jfif|bmp|dpg/.test(one.fileType)){
                this.downloadImage(one.thumb)
            }else if(/mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv/.test(one.fileType)){
                req('GET',URL.getPreviewInfo, {
                    busType: 'cloud',
                    sourceID: one.sourceID
                }, {}, false).then(data => {
                    if (data.downloadUrl) data.downloadUrl = /http|https/.test(data.downloadUrl) ? data.downloadUrl :DOMAIN + data.downloadUrl;
                    this.downloadVideo(data.downloadUrl,data.name)
                })
            }else if(/pptx|ppt|xlsx|xls|doc|docx|pdf|wps/.test(one.fileType)){
                req('GET',URL.getPreviewInfo, {
                    busType: 'cloud',
                    sourceID: one.sourceID
                }, {}, false).then(data => {
                    if (data.downloadUrl) data.downloadUrl = /http|https/.test(data.downloadUrl) ? data.downloadUrl :DOMAIN + data.downloadUrl;
                    this.downloadFileOpen(data.downloadUrl)
                })
            }else{
                wx.showToast({
                    title: '暂不支持下载',
                    icon:'none'
                })
            }   
        },
        //下载图片
        downloadImage(imageUrl){
            let that = this
            var fileN=new Date().valueOf();
            var fileP = wx.env.USER_DATA_PATH+'/'+fileN+'.jpg'
            wx.downloadFile({
                url:imageUrl,
                filePath:fileP,//这里要加这个filePath属性
                success:(res)=>{
                    var filePath = res.filePath;
                    wx.saveImageToPhotosAlbum({
                        filePath,
                        success:(res)=>{
                            wx.showToast({title: '保存到相册成功'});
                            that.cancelEdit();
                            that.selectComponent("#operateBar").onCloseMore()
                        }
                    })
                }                
            })
        },
        //下载视频
        downloadVideo(videoUrl,fileName){
            let that = this 
            wx.showLoading({
                title: '下载中...',
            })
            wx.downloadFile({
                url: videoUrl, // 视频资源地址
                filePath: wx.env.USER_DATA_PATH + '/' + fileName + '.mp4',
                header: {
                  "Content-Type":"video/mp4"
                },
                success: res => {
                  wx.hideLoading()
                  let FilePath= res.filePath; // 下载到本地获取临时路径
                  let fileManager = wx.getFileSystemManager();
                  // 保存到相册
                  wx.saveVideoToPhotosAlbum({ // 保存到相册
                    filePath: FilePath,
                    success: file => {
                      // console.log('saveVideoToPhotosAlbum成功回调file:', file)
                      wx.showToast({
                        title: '视频保存成功',
                        icon:'none'
                      })
                      fileManager.unlink({ // 删除临时文件
                        filePath: wx.env.USER_DATA_PATH + '/' + fileName + '.mp4',
                      })
                        that.cancelEdit();
                        that.selectComponent("#operateBar").onCloseMore()
                    },
                    fail: err => {
                      // console.log('saveVideoToPhotosAlbum失败回调err:', err)
                      fileManager.unlink({ // 删除临时文件
                        filePath: wx.env.USER_DATA_PATH + '/' + fileName + '.mp4'
                      })
                      wx.showToast({
                        title: '视频保存失败',
                        icon:'none'
                      })
                    },
                    complete() {
                      wx.hideLoading()
                    }
                  })
                  //
                },
                fail(e) {
                  console.log('失败e', e)
                  wx.showToast({
                    title: '视频保存失败1',
                  })
                },
                complete() {
                  // wx.hideLoading();
                }
            })          
        },
        //下载文档
        downloadFileOpen(fileUrl){
            let that = this
            wx.downloadFile({
              url: fileUrl,
              success (res) {
                if (res.statusCode === 200) {
                    console.log(res)
                    const filePath = res.tempFilePath
                    wx.openDocument({
                      filePath: filePath,
                      showMenu:true, //关键点
                      success: function (res) {
                        wx.showToast({
                            title: '打开文档成功',
                        })
                        that.cancelEdit();
                        that.selectComponent("#operateBar").onCloseMore()
                      }
                    })
                }
              }
            })
        },
        // 删除文件
        deleteFile(e) {
            let {
                dataList
            } = this.data, param = {
                dataArr:[],
                operation:'recycle'
            }, toastText = '确定要删除所选文件吗？',directoryIds= [] ,fileIds = [];
            dataList.folderList.map(c => c.checked ? directoryIds.push(c.sourceID) : '');
            dataList.fileList.map(c => c.checked ? fileIds.push(c.sourceID) : '');
            dataList.folderList.map(c => c.checked ? param.dataArr.push({name:c.name,parentID:c.parentID,parentLevel:c.parentLevel,sourceID:c.sourceID,type:'folder'}) : '');
            dataList.fileList.map(c => c.checked ? param.dataArr.push({name:c.name,parentID:c.parentID,parentLevel:c.parentLevel,sourceID:c.sourceID,type:'file'}) : '');
            if (param.dataArr.length == 0) {
                wx.showToast({
                    title: '请选择要删除的文件！',
                    icon: 'none'
                })
                return;
            }
            if (directoryIds.length && !fileIds.length) {
                toastText = '确定要删除已选文件夹吗？文件夹内文件也会一并删除！';
            }
            if (directoryIds.length && fileIds.length) {
                toastText = '确定要删除已选文件和文件夹吗？文件夹内文件也会一并删除！'
            }
            wx.showModal({
                title: '提示',
                content: toastText,
                success: res => {
                    if (res.confirm) {
                        req('POST', URL.diskOperation, {}, param, false).then(data => {
                            wx.showToast({
                                title: '删除成功！',
                                icon: 'none'
                            })
                            this.setData({
                                currentPage: 1
                            })
                            this.getDiskList();
                            this.triggerEvent('getRecycleNum')
                            this.cancelEdit();
                        }).catch(err => {
                            this.cancelEdit();
                            wx.showToast({
                                title: err.message,
                                icon: 'none'
                            })
                        })
                    }
                }
            })
        },
        //回收站彻底删除
        removeFile(e){ 
            let {
                dataList
            } = this.data, param = {
                dataArr:[],
                operation:'remove'
            },directoryIds= [] ,fileIds = [],toastText = '';
            if(e.isAll){
                param = {
                    operation:'removeAll'
                }
                toastText = '确定要清空回收站吗？'
            }else{
                toastText = '确定要彻底删除吗？'
                dataList.folderList.map(c => c.checked ? directoryIds.push(c.sourceID) : '');
                dataList.fileList.map(c => c.checked ? fileIds.push(c.sourceID) : '');
                dataList.folderList.map(c => c.checked ? param.dataArr.push({name:c.name,parentID:c.parentID,parentLevel:c.parentLevel,sourceID:c.sourceID,type:'folder'}) : '');
                dataList.fileList.map(c => c.checked ? param.dataArr.push({name:c.name,parentID:c.parentID,parentLevel:c.parentLevel,sourceID:c.sourceID,type:'file'}) : '');
                if (param.dataArr.length == 0) {
                    wx.showToast({
                        title: '请选择要删除的文件！',
                        icon: 'none'
                    })
                    return;
                }
            }
            wx.showModal({
                title: '提示',
                content:toastText,
                success: res => {
                    if (res.confirm) {
                        req('POST', URL.diskOperation, {}, param, false).then(data => {
                            wx.showToast({
                                title: '删除成功！',
                                icon: 'none'
                            })
                            this.setData({
                                currentPage: 1
                            })
                            this.getDiskList();
                            this.cancelEdit();
                            wx.setStorageSync('needRefresh', 1)
                        }).catch(err => {
                            this.cancelEdit();
                            wx.showToast({
                                title: err.message,
                                icon: 'none'
                            })
                        })
                    }
                }
            })
        },
        //回收站还原文件
        reductionFile(e){
            let {
                dataList
            } = this.data, param = {
                dataArr:[],
                operation:'restore'
            },directoryIds= [] ,fileIds = [],toastText = '';
            if(e.isAll){
                param = {
                    operation:'restoreAll'
                }
                toastText = '确定要还原回收站所有文件？'
            }else {
                dataList.folderList.map(c => c.checked ? directoryIds.push(c.sourceID) : '');
                dataList.fileList.map(c => c.checked ? fileIds.push(c.sourceID) : '');
                dataList.folderList.map(c => c.checked ? param.dataArr.push({name:c.name,parentID:c.parentID,parentLevel:c.parentLevel,sourceID:c.sourceID,type:'folder'}) : '');
                dataList.fileList.map(c => c.checked ? param.dataArr.push({name:c.name,parentID:c.parentID,parentLevel:c.parentLevel,sourceID:c.sourceID,type:'file'}) : '');
                if (param.dataArr.length == 0) {
                    wx.showToast({
                        title: '请选择要还原的文件！',
                        icon: 'none'
                    })
                    return;
                }
            }
            wx.showModal({
                title: '提示',
                content:'确定要还原吗？',
                success: res => {
                    if (res.confirm) {
                        req('POST', URL.diskOperation, {}, param, false).then(data => {
                            wx.showToast({
                                title: '还原成功！',
                                icon: 'none'
                            })
                            this.setData({
                                currentPage: 1
                            })
                            this.getDiskList();
                            this.cancelEdit();
                            wx.setStorageSync('needRefresh', 1)
                        }).catch(err => {
                            this.cancelEdit();
                            wx.showToast({
                                title: err.message,
                                icon: 'none'
                            })
                        })
                    }
                }
            })
        },
        //上传回调
        returnUploadPath(data) {
            let that = this
            this.setData({
                currentPage: 1
            })
            this.getDiskList();
            setTimeout(function(){
              wx.showToast({
                title: '上传成功！',
                icon:'none'
              })
            },500)
        },
        // 点击移动文件
        moveFile(e){
            let {
                dataList
            } = this.data,{type} = e.detail,moveNumber = 0,param = {
                dataArr:[],
                operation:type  
            },directoryIds = [],fileIds = []
            dataList.folderList.map(c => c.checked ? directoryIds.push(c.directoryId) : '');
            dataList.fileList.map(c => c.checked ? fileIds.push(c.fileId) : '');
            dataList.folderList.map(c => c.checked ? param.dataArr.push({name:c.name,parentID:c.parentID,parentLevel:c.parentLevel,sourceID:c.sourceID,type:'folder'}) : '');
            dataList.fileList.map(c => c.checked ? param.dataArr.push({name:c.name,parentID:c.parentID,parentLevel:c.parentLevel,sourceID:c.sourceID,type:'file'}) : '');
            moveNumber = directoryIds.length + fileIds.length;
            if (moveNumber < 1) {
                wx.showToast({
                    title: '请选择文件',
                    icon: 'none'
                })
                return;
            }
            this.setData({
                showMoveFile: true,
                moveNumber,
                moveFileParam: param,
                moveType:type
            })
        },
        //讨论选择图片
        choosePicture(e){
          let {type} = e.detail
          this.setData({
            showMoveFile: true,
            moveType:type
          })
        },
        //微信聊天文件上传
        uploadWechatFile(e){
          let {file,sourceID} = e.detail
          this.setData({
            showMoveFile: false,
          })
          this.uploadLargeFile('',file.path, file.size, 'cloud', URL.uploadFile,URL.uploadCheck, sourceID,file.name).then(data => {
            console.log(data)
          }).catch(err => {
            wx.hideLoading();
            console.log(err)
          })  
        },
        //讨论发送图片
        sendPicture(e){
          let {path} = e.detail.one[0]
          this.selectComponent("#attribute").confirmPicture(path)
          this.setData({
            showMoveFile: false
          })
        },
        // 移动文件到文件夹
        moveFileToFolder(e) {
            let {
                moveFileParam
            } = this.data, {
                sourceID
            } = e.detail;
            moveFileParam.sourceID = sourceID;
            moveFileParam.sourceLevel = moveFileParam.dataArr[0].parentLevel;
            req('POST', URL.diskOperation, {}, moveFileParam, false).then(data => {
                wx.showToast({
                    title:moveFileParam.operation=='copy'?'复制成功！':'剪切成功！',
                    icon: 'none'
                })
                this.setData({
                    showMoveFile: false,
                    isEdit: false
                })
                this.setData({
                    currentPage: 1
                })
                let that = this
                setTimeout(function(){
                    that.getDiskList();
                },500)
            }).catch(err => {
                wx.showToast({
                    title: err.message,
                    icon: 'none'
                })
            })
        },
        getWechat(e){
          if(!this.data.wechatNew){
            this.setData({
              moveType:'upload',
              showMoveFile:true,
              wechatNew:true
            })
          }
        },
        closeWechat(e){
            this.setData({
                showMoveFile:false,
            })
        },
        // 长按显示操作框
        showEditActionSheet(e) {
            if(this.data.catalogue.length==1&&this.data.catalogue[0].type=='files'){
                return
            }
            let {
                cate,
                isEdit,
                dataList,
                isFav,
                isTop,
                isUpload,
                isZip,
                fileAuth,
                targetType,
                tagList,
                formatConvert,
                checkedFile
            } = e.detail, isOnlyShare = false,checkedSum;
            checkedSum = dataList.fileList.filter(c => c.checked).length + dataList.folderList.filter(c => c.checked).length
            let folderSum = dataList.folderList.filter(c => c.checked)
            this.selectComponent('#operateBar').setOperate({catalogue:this.data.catalogue,checkedSum,folderSum,formatConvert})
            this.setData({
                isEdit,
                dataList,
                operateShow:true,
                isFav,
                isTop,
                isUpload,
                fileAuth,
                isZip,
                targetType,
                checkedSum,
                formatConvert,
                checkedFile
            })
        },
        // 显示分享框
        showSharePanel: async function (e) {
            this.setData({
                'sharePanel.isShow': true
            })
            let {
                dataList
            } = this.data,
                one = dataList.fileList.filter(c => c.checked),
                sre_param = {
                    busId: one[0].fileId,
                    busType: 25,
                    sreId: '',
                    url: ''
                },
                route_param = {
                    busId: one[0].fileId,
                    busType: 25,
                    routeId: '',
                    uvcid: genUuid()
                };
            let sre_promise = req('post', URL.requestSreId, {}, sre_param, false),
                route_promise = req('GET', URL.getLogRouteId, route_param, {}, false);
            wx.showLoading({
                title: '加载中…',
                mask: true
            })
            Promise.all([sre_promise, route_promise]).then(data => {
                wx.hideLoading();
                this.currentPage.newSreId = data[0];
                this.currentPage.newRouteId = data[1].routeId;
                this.currentPage.setData({
                    showFilesShare: true,
                    shareType: 'files',
                    fileShareParams: this.getShareFileParam()
                })
            }).catch(err => {
                wx.hideLoading();
                this.currentPage.setData({
                    showFilesShare: true,
                    shareType: 'files',
                    fileShareParams: this.getShareFileParam()
                })
            })

        },
        // 关闭分享框
        onClose(e) {
            this.setData({
                'sharePanel.isShow': false
            })
            this.currentPage.setData({
                showFilesShare: false
            })
            this.cancelEdit();
        },
        showFileTypeSheet,
        showFilterTypeSheet,
        initIconPath,twoWayBinding, choseFiles,
        choseImageConfirm,
        choseVideoConfirm,
        uploadLargeFile,
        choseFilesConfirm
    }
})