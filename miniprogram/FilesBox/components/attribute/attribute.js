import{renderSize,timeFromNow,timeFormat,parseEmoji,booleanIsOnlyEmoji} from '../../utils/util';
import {req} from '../../utils/service';
import {URL} from '../../utils/config';
import {imageUrl} from '../../utils/image'
import {choseFiles,choseImageConfirm,choseVideoConfirm,uploadLargeFile,choseFilesConfirm} from '../../utils/uploadFile';
import{initIconPath} from '../file/files-component/config'
const {globalData} = getApp();
const DOMAIN = wx.getStorageSync('DOMAIN')
Component({
  properties:{
   
  },
  data: {
    systemInfo:wx.getSystemInfoSync(),
    tagStyle:{
      img:'width:200px;'
    },
    imageUrl,
    statusBarHeight:globalData.systemInfo.statusBarHeight,
    lang:globalData.lang,
    attributeShow:false,
    tabList:[{
        name:'属性',
        show:true
      },{
        name:'版本',
        show:true
      },{
        name:'讨论',
        show:true
      },{
        name:'动态',
        show:true
      }
    ],
    selectTabIndex:0,
    nowList:[
      {
        image:'/images/operateMore/icon_file_lastopen.png',
        name:'打开',
        fn:'previewFile'
      },
      {
          image:'/images/operateMore/icon_file_download.png',
          name:'下载',
          fn:'downloadFile'
      },
      {
        image:'/images/list_icon/icon_file_del.png',
        name:'删除所有版本记录',
        fn:'deleteAllVersion'
      },
      {
        name:'取消',
        fn:'onCloseShowMore'
      }
    ],
    oldList:[
      {
        image:'/images/operateMore/icon_file_lastopen.png',
        name:'打开',
        fn:'previewFile'
      },
      {
          image:'/images/operateMore/icon_file_download.png',
          name:'下载',
          fn:'downloadFile'
      },
      {
        image:'/images/icons/set_version.png',
        name:'设置为当前版本',
        fn:'setVersion'
      },
      {
        image:'/images/list_icon/icon_file_del.png',
        name:'删除该版本',
        fn:'deleteVersion'
      },
      {
        image:'/images/icons/add_version_intro.png',
        name:'添加版本说明',
        fn:'showVersionIntro'
      },
      {
        image:'/images/list_icon/icon_file_del.png',
        name:'删除所有版本记录',
        fn:'deleteAllVersion'
      },
      {
        name:'取消',
        fn:'onCloseShowMore'
      }
    ],
    versionIndex:-1,
    trendsPage:1,
    versionPage:1,
    commentsPage:1,
    comments:'',
  },
  lifetimes:{
    ready(){
      this.setData({
        DOMAIN:wx.getStorageSync('DOMAIN'),
      })
    }
  },
  methods:{
    getAttribute(param){
      param.size = renderSize(param.size)
      let {tabList} = this.data,mineEdit = true,delOther = true,role = wx.getStorageSync('options').role
      let {systemInfo} = this.data,isCover = /jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg/.test(param.fileType)?true:false,
      scrollHeight = isCover?systemInfo.windowHeight - systemInfo.statusBarHeight - 400:systemInfo.windowHeight - systemInfo.statusBarHeight - 340
      // console.log(systemInfo.statusBarHeight,scrollHeight)
      // console.log(role,param.auth)
      if(param.isFolder){
        tabList[1].show = false
      }
      if(param.targetType==1){
        tabList[3].show = role['explorer.view']
        tabList[2].show = role['explorer.view']
        mineEdit = role['explorer.edit']
        delOther = role['explorer.edit']
      }else{
        if(param.auth.indexOf("13") == -1||!role['explorer.view']){
          tabList[3].show = false
        }  
        if(param.auth.indexOf("12") == -1||!role['explorer.view']){
          tabList[2].show = false
        } 
        if(param.auth.indexOf("8") == -1||!role['explorer.edit']||param.auth.indexOf("12") == -1){
          mineEdit = false
        } 
        if(param.auth.indexOf("14") == -1||!role['explorer.edit']){
          delOther = false
        }
      }
      this.setData({
        scrollHeight,
        attributeShow:true,
        dataList:param,
        sourceID:param.sourceID,
        isFolder:param.isFolder,
        tabList,
        isCover,
        mineEdit,
        delOther
      })
    },
    getHistory(param){
      let DOMAIN = this.data.DOMAIN
      param.createUserJson.avatar = /http|https/.test(param.createUserJson.avatar) ? param.createUserJson.avatar : DOMAIN + param.createUserJson.avatar
      param.avatar = param.createUserJson.avatar
      this.setData({
        sourceID:param.sourceID,
        parentID:param.parentID,
        name:param.name
      })
      this.getHistoryList()
    },
    updateHistory(){
      this.getHistoryList()
    },
    updateNewFile(){
      let that = this,{sourceID,parentID,name,dataList} = this.data,itemList = ['视频','图片','文件'],type;
      const isVideo = /mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv|vob/,
            isImage = /jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg/,
            isFile = /pptx|ppt|xlsx|xls|doc|docx|pdf|wps/;
      if(isVideo.test(dataList.fileType)){
        itemList = ['视频']
        type = 1
      }else if(isImage.test(dataList.fileType)){
        itemList = ['图片']
        type = 0
      }else if(isFile.test(dataList.fileType)){
        itemList = ['文件']
        type = 2
      }else{
        itemList = ['文件']
        type = 2
      }
      wx.showActionSheet({
        itemList:itemList,
        success(res){
          if (res.tapIndex === 0){
            that.addFiles(type,sourceID,parentID,name)
          }else if (res.tapIndex === 1){
            that.addFiles(type,sourceID,parentID,name)
          }else if (res.tapIndex === 2){
            that.addFiles(type,sourceID,parentID,name)
          }
        }
      })
    },
    addFiles(a,sourceID,parentID,name){
      let index = a 
      console.log(a)
      switch (index) {
          case 0:  
              this.choseFiles('image', parentID,'cloud',sourceID,name);
              break;
          case 1:
              this.choseFiles('video', parentID,'cloud',sourceID,name);
              break;
          case 2:
              this.choseFiles('fiels', parentID,'cloud',sourceID,name);
              break;
          default:
              break;
      }
    },  
    //上传回调
    returnUploadPath(data) {
      if(this.data.sourceID){
        this.getHistoryList();
      }
    },
    getHistoryList(reachBottom=false){
      let {dataList,sourceID,versionPage,DOMAIN} = this.data,
      param = {
        currentPage:versionPage,
        sourceID:sourceID,
      }
      req('GET', URL.getHistoryList,param, {}, false).then(data => {
        const isImage = /jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg/
        data.current.createTime = timeFormat('yyyy/MM/dd hh:mm:ss',data.current.createTime*1000);
        data.current.avatar = data.current.avatar?(/http|https/.test(data.current.avatar) ? data.current.avatar : DOMAIN + data.current.avatar):'/images/icons/files_avatar.png'
        data.current.size = renderSize(data.current.size)
        let index = data.current.downloadUrl.lastIndexOf('.');
        if (isImage.test(data.current.fileType)) data.current.thumb = `${data.current.downloadUrl.substring(0,index)}!small${data.current.downloadUrl.substring(index,data.current.downloadUrl.length)}`;
        if(data.current.thumb){
          data.current.thumb = /http|https/.test(data.current.thumb) ? data.current.thumb : DOMAIN + data.current.thumb
        }else{
          data.current.thumb = initIconPath(data.current.fileType)
        } 
        data.fileList.map(c=>{
          c.avatar = c.avatar?(/http|https/.test(c.avatar) ? c.avatar : DOMAIN + c.avatar):'/images/icons/files_avatar.png'
          c.downloadUrl = /http|https/.test(c.downloadUrl) ? c.downloadUrl : DOMAIN + c.downloadUrl
          c.createTime = timeFormat('yyyy/MM/dd hh:mm:ss',c.createTime*1000);
          c.size = renderSize(c.size)
          if(c.thumb){
            c.thumb = /http|https/.test(c.thumb) ? c.thumb : DOMAIN + c.thumb
          }else{
            c.thumb = initIconPath(c.fileType)
          }
        })
        dataList.thumb = data.current.thumb
        this.setData({
          fileList:data.current,
          historyList:reachBottom?[...this.data.historyList, ...data.fileList]:data.fileList,
          historyTotal:reachBottom?this.data.historyTotal:data.total,
          dataList
        })
      }).catch(err => {
          console.log(err)
          wx.showToast({
              title: err.message,
              icon: 'none'
          })
      })
    },
    getTrends(reachBottom=false){
      let {trendsPage,sourceID,isFolder,DOMAIN} = this.data
      req('GET', URL.getPathLog, {}, {
        currentPage:trendsPage,
        isFolder:isFolder,
        sourceID:sourceID,
      }, false).then(data => {
        // console.log(data.list)
        data.list.map(c=>{
          c.avatar = c.avatar?(/http|https/.test(c.avatar) ? c.avatar : DOMAIN + c.avatar):'/images/icons/files_avatar.png'
          c.time = timeFromNow(c.createTime*1000)
          c.desc = JSON.parse(c.desc)
          if(c.type=='recycle'||c.type=='remove'){
            c.thumb = '/images/trends_icon/del.png'
          }else if(c.type=='rename'){
            c.thumb = '/images/icons/edit.png'
          }else if(c.type=='share'){
            c.thumb = '/images/trends_icon/link.png'
          }else if(c.type=='create'){
            if(c.desc.createType=='mkfile'||c.desc.createType=='copy'){
              c.thumb = '/images/trends_icon/add.png'
            }else if(c.desc.createType=='upload'){
              c.thumb = '/images/trends_icon/upload.png'
            }else{
              c.thumb = '/images/icons/edit.png'
            }
          }
          if(isFolder){
            if(c.type == 'recycle') {
              if(c.desc.content=='restore'){
                c.content = '将'+c.name+'从回收站还原'
              }else if(c.desc.content=='toRecycle'){
                c.content = '将'+c.name+'移到了回收站'
              }
            }else if(c.type == 'create') {
              if(c.desc.createType=='uploadNew'){
                c.content = '更新了该文件（上传新版本）'
              }else if(c.desc.createType=='copy'){
                c.content = '粘贴新建了'+c.name
              }else if(c.desc.createType=='upload'){
                c.content = '上传了文件'+c.name
              }else{
                c.content = '新建了文件'+c.name
              }  
            }else if(c.type == 'share') {
              c.content = 'share'
            }else if(c.type == 'remove') {
              c.content = '在此处删除了文件'+c.desc.content
            }else if(c.type == 'rename') {
              c.content = '重命名了该文件夹'+c.desc.from+'为'+c.desc.to
            }
          }else{
            if(c.type == 'recycle') {
              if(c.desc.content=='restore'){
                c.content = '将该文件从回收站还原'
              }else if(c.desc.content=='toRecycle'){
                c.content = '将该文件移到了回收站'
              }
            }else if(c.type == 'create') {
              if(c.desc.createType=='uploadNew'){
                c.content = '更新了该文件（上传新版本）'
              }else{
                c.content = '新建了该文件'
              }
            }else if(c.type == 'share') {
              if(c.desc.content=='shareLinkRemove'){
                c.content = '关闭了该文件的外链分享'
              }else if(c.desc.content=='shareLinkEdit'){
                c.content = '编辑了该文件的外链分享'
              }else{
                c.content = '将该文件创建了外链分享'
              }
            }else if(c.type == 'remove') {
              c.content = '在此处删除了文件'+c.desc.content
            }else if(c.type == 'rename') {
              c.content = '重命名了该文件'+c.desc.from+'为'+c.desc.to
            }
          }  
        })
        this.setData({
          trendsList:reachBottom?[...this.data.trendsList, ...data.list]:data.list,
          trendsTotal:reachBottom?this.data.trendsTotal:data.total
        })
      }).catch(err => {
          console.log(err)
          wx.showToast({
              title: err.message,
              icon: 'none'
          })
      })
    },
    getComments(reachBottom=false){
      let {commentsPage,sourceID,DOMAIN} = this.data
      req('GET', URL.getComments, {}, {
        currentPage:commentsPage,
        pageSize: 40,
        targetID: sourceID
      }, false).then(data => {
          data.list.map(c=>{
            c.avatar = c.avatar?(/http|https/.test(c.avatar) ? c.avatar : DOMAIN + c.avatar):'/images/icons/files_avatar.png'
            c.isMine = c.userID==wx.getStorageSync('userID')?true:false;
            const regex = new RegExp('src="', 'gi');
            c.content = c.content.replace(regex, 'src="'+DOMAIN);
            // c.content = c.content.replace(/\<img/gi, '<img style="max-width:96%;height:auto;"')
            c.content = parseEmoji(c.content);   
            c.isOnlyEmoji = booleanIsOnlyEmoji(c.content)
          })
          this.setData({
            commentsList:reachBottom?[...this.data.commentsList, ...data.list]:data.list,
            commentsTotal:reachBottom?this.data.commentsTotal:data.total,
            // lastView:'view_0'
            // lastView:'view_'+(data.total-1)
          })
      }).catch(err => {
          console.log(err)
          wx.showToast({
              title: err.message,
              icon: 'none'
          })
      })
    },
    imgtap(e){
      if(e.detail.src.split('.cn')[0]=='https://test-static.1x'){
        return
      }
      let content = this.data.commentsList
      let imgarr = [],text = ''; 
      let regex = new RegExp(/<img.*?(?:>|\/>)/gi); // 匹配所有图片 
      let srcReg = /src=[\'\"]?([^\'\"]*)[\'\"]?/i; // 匹配src图片 
      content.map(c=>{
        text+=c.content
      })
      let arrsImg = text.match(regex); // obj.info 后台返回的富文本数据 
      for (let a = 0; a < arrsImg.length; a++) { 
        let srcs = arrsImg[a].match(srcReg); 
        if(srcs[1].split('.cn')[0]!='https://test-static.1x'){
          imgarr.push(srcs[1]) 
        }  
      } 
      wx.previewImage({
        current:e.detail.src,
        urls: imgarr
      })
    },
    inputValue(e){
      let {value} = e.detail
      this.setData({
        comments:value
      })
    },
      // 表情
    foldEmoji:function(e){
      let flag = !this.data.showEmoji
      this.setData({
        showEmoji:flag,
        fixedBottom:flag ? 421.42 : 120,
      })
      if(flag){
        wx.hideKeyboard({
          success: (res) => {},
        })
      }
    },
    // 选择表情
    changeEmoji:function(e){
      let { value } = e.detail, { comments } = this.data;
      comments += value;
      this.setData({comments})
    },
    delEmoji:function(e){
      let { comments } = this.data;
      if(comments.length){
        comments = comments.substring(0,comments.length-1);
        this.setData({comments})
      }
    },
    showDel(e){
      let {id} = e.currentTarget.dataset,that = this
      wx.showActionSheet({
        itemList:['删除'],
        success(res){
          if (res.tapIndex === 0){
            wx.showModal({
              title: '确定要删除吗？',
              complete: (res) => {
                if (res.confirm) {
                  req('POST', URL.delComment, {}, {
                    commentID: id
                  }, false).then(data => {
                      that.setData({
                        commentsPage:1,
                        showEmoji:false,
                      })
                      wx.showToast({
                        title: '删除成功',
                        icon: 'none'
                      })
                      that.getComments()
                  }).catch(err => {
                      console.log(err)
                      wx.showToast({
                        title: err.message,
                        icon: 'none'
                      })
                  })
                }
              }
            })
          }
        }
      })
    },
    showPicture(){
      this.triggerEvent("choosePicture",{type:'comment'})
    },
    confirmPicture(path){
      this.setData({
        comments:'<img src="'+path+'">'
      })
      this.sendComent()
    },
    sendComent(){
      let {comments,sourceID} = this.data,regu =/^[ ]+$/
      if(comments==''||regu.test(comments)){
        wx.showToast({
          title: '请输入评论内容',
          icon:'none'
        })
        return
      }
      req('POST', URL.saveComment, {}, {
        content:comments,
        targetID: sourceID
      }, false).then(data => {
          this.setData({
            comments:'',
            commentsPage:1,
            showEmoji:false,
          })
          wx.showToast({
            title: '发送成功',
            icon: 'none'
          })
          this.getComments()
      }).catch(err => {
          console.log(err)
          wx.showToast({
            title: err.message,
            icon: 'none'
          })
      })
    },
    selectTab(e){
      let {index} = e.currentTarget.dataset
      this.setData({
        selectTabIndex:index,
        moreShow:false
      })
    },
    onClose(){
      this.setData({
        attributeShow:false,
        moreShow:false,
        selectTabIndex:0,
        trendsPage:1,
        versionPage:1,
        commentsPage:1
      })
    },
    addDesc(e){
      this.triggerEvent("onAddDesc",{desc:e.detail.value,sourceID:this.data.dataList.sourceID})
    },
    showMore(e){
      this.onCloseShowMore()
      let {type} = e.currentTarget.dataset,moreList = [],index
      if(type==0){
        moreList = this.data.nowList
      }else{
        moreList = this.data.oldList
        index = e.currentTarget.dataset.index
      }
      this.setData({
        moreList,
        version:type==0?this.data.fileList:this.data.historyList[index],
        versionIndex:index
      })
    },
    onCloseShowMore(){
      this.setData({
        moreShow:!this.data.moreShow
      })
    },
    //添加版本说明
    showVersionIntro(){
      let {historyList,versionIndex} = this.data
      historyList.map(c=>{c.show=false})
      historyList[versionIndex].show=true
      this.setData({
        historyList,
        moreShow:false
      })
    },
    setVersionIntro(e){
      let {value} = e.detail,{sourceID} = this.data,{id} = e.currentTarget.dataset
      let param = {
        detail:value,
        id: id
      }
      req('POST',URL.setVersionIntro,{},param,false).then(data => {
        wx.showToast({
          title: '操作成功',
        })
        this.getHistoryList()
      })
    },
    clearVersionIntro(e){
      let {sourceID} = this.data,{id} = e.currentTarget.dataset
      let param = {
        detail:'',
        id: id
      }
      req('POST',URL.setVersionIntro,{},param,false).then(data => {
        wx.showToast({
          title: '操作成功',
        })
        this.getHistoryList()
      })
    },
    //刷新文件列表
    setVersionReturn(){
      this.triggerEvent('setVersionReturn')
    },
    //设置为当前版本
    setVersion(){
      let {version,sourceID} = this.data
      wx.showModal({
        title: '提示',
        content: '确定要回滚到该版本？',
        success: res => {
          if (res.confirm) {
            req('POST',URL.setVersion,{},{id: version.id},false).then(data => {
              wx.showToast({
                title: '操作成功',
              })
              this.getHistoryList()
              this.onCloseShowMore();
              this.setVersionReturn()
            })
          }
        }
      })
    },
    //删除该版本
    deleteVersion(){
      let {version,sourceID} = this.data
      wx.showModal({
        title: '提示',
        content: '确定要删除该版本？',
        success: res => {
          if (res.confirm) {
            req('POST',URL.deleteVersion,{},{id: version.id},false).then(data => {
              wx.showToast({
                title: '删除成功',
              })
              this.getHistoryList()
              this.onCloseShowMore();
              this.setVersionReturn()
            })
          }
        }
      })
    },
    // 预览文件
    previewFile(e) {
      let {sourceID,version} = this.data,previewUrl,fileType;
      version.downloadUrl = /http|https/.test(version.downloadUrl) ? version.downloadUrl : DOMAIN + version.downloadUrl
      // wx.showLoading({
      //     title: '加载中…',
      //     mask: true
      // })
      this.onCloseShowMore();
      const isVideo = /mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv|vob/,
      isAudio = /ram|swf|mp3|wma/,
      isImage = /jpeg|jpg|gif|png|webp|jfif|bmp|dpg/,
      isFile = /pptx|ppt|xlsx|xls|doc|docx|pdf|wps/;
      if (isImage.test(version.fileType)) {
          wx.previewImage({
              urls: [`${version.downloadUrl}`],
          })
          return;
      }
      if (isFile.test(version.fileType)) {
        previewUrl = version.pptPreviewUrl;
        fileType = 1;
      } else if (isVideo.test(version.fileType)) {
          if (version.downloadUrl) previewUrl = /http|https/.test(version.downloadUrl) ? version.downloadUrl :DOMAIN + version.downloadUrl;
          fileType = 2;
      } else if (isAudio.test(version.fileType)) {
          if (version.downloadUrl) previewUrl = /http|https/.test(version.downloadUrl) ? version.downloadUrl :DOMAIN + version.downloadUrl;
          fileType = 3;
      }else{
        wx.showToast({
          title: '该文件不支持预览！',
          icon: 'none'
        })
        return;
      }
      // if (!version.fileType || ['txt', 'rar', 'zip', '7z', 'gz', 'apk','tar'].includes(version.fileType)) {
      //     wx.showToast({
      //         title: '该文件不支持预览！',
      //         icon: 'none'
      //     })
      //     return;
      // }
      wx.setStorageSync('previewUrl', previewUrl)
      wx.navigateTo({
          url: `/pages/files-preview/files-preview?sourceID=${sourceID}&fileType=${fileType}`,
      })
    },
    //下载文件
    downloadFile(e){
      let {version,sourceID} = this.data
      if(/jpeg|jpg|gif|png|webp|jfif|bmp|dpg/.test(version.fileType)){
        this.downloadImage(version.downloadUrl)
      }else if(/mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv/.test(version.fileType)){
          req('GET',URL.getPreviewInfo, {
              busType: 'cloud',
              sourceID: sourceID
          }, {}, false).then(data => {
              if (data.downloadUrl) data.downloadUrl = /http|https/.test(data.downloadUrl) ? data.downloadUrl :DOMAIN + data.downloadUrl;
              this.downloadVideo(data.downloadUrl,data.name)
          })
      }else if(/pptx|ppt|xlsx|xls|doc|docx|pdf|wps/.test(version.fileType)){
          req('GET',URL.getPreviewInfo, {
              busType: 'cloud',
              sourceID: sourceID
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
                      that.onCloseShowMore();
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
                  that.onCloseShowMore();
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
                  that.onCloseShowMore();
                }
              })
          }
        }
      })
    },
    //删除所有版本
    deleteAllVersion(){
      let {sourceID} = this.data
      wx.showModal({
        title: '提示',
        content: '确定删除所有历史版本记录？',
        success: res => {
          if (res.confirm) {
            req('POST',URL.deleteVersion,{},{sourceID: sourceID},false).then(data => {
              wx.showToast({
                title: '删除成功',
              })
              this.getHistoryList()
              this.onCloseShowMore();
            })
          }
        }
      })
    },
    onReachBottom(e){
      let {type} = e.currentTarget.dataset,{trendsPage,versionPage,commentsPage} = this.data
      if(type==1){
        trendsPage++
        this.setData({
          trendsPage:trendsPage
        })
        this.getTrends(true)
      }else if(type==0){
        versionPage++
        this.setData({
          versionPage:versionPage
        })
        this.getHistoryList(true)
      }else{
        commentsPage++
        this.setData({
          commentsPage:commentsPage
        })
        this.getComments(true)
      }
    },
    choseFiles,choseImageConfirm,choseVideoConfirm,uploadLargeFile,choseFilesConfirm,initIconPath
  },
})