// packageB/pages/files/files.js
import { imageUrl } from '../../../utils/image';
import { req, addShareLog } from '../../../utils/service';
import { STATIC_DOMAIN, URL } from '../../../utils/config';
import { timeFormat, twoWayBinding, shareAddRetailParam, getFileSize }  from '../../../utils/util';
import { fileTypeActions, filterTypeActions, showFileTypeSheet, showFilterTypeSheet, catalogue, modalFuns, panelBar, sharePanel, initIconPath, requestSreId, getLogRouteId } from './config';
import { choseFiles, choseImageConfirm, choseVideoConfirm, uploadLargeFile, choseFilesConfirm } from '../../../utils/uploadFile';
const { globalData } = getApp();
Page({
  renameAfterUpload:false,
  renameFileId:'',
  newSreId:'',
  /**
   * 页面的初始数据
   */
  data: {
    imageUrl, fileTypeActions, filterTypeActions, sharePanel,
    currentPage:1,
    fileTypeIndex:0,
    filterTypeIndex:1,
    directoryId:'0', //父文件夹id, 最外层传0
    keyword:'',//搜索关键字
    loadingState:1,
    dataList:[],
    catalogue,
    showSearch:false, //是否显示筛选
    uploadProgress:0,
    showModal:false,
    directoryName:'',
    isEdit:false,
    modalFunIndex:0,
    modalFuns,
    panelBar,
    renameType:0, //0文件夹 1 文件
    renameData:'', //重命名的文件，或文件夹
    showMoveFile:false, //是否显示移动文件
    newFileName:'',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getDirectoryList();
    this.getUserAuth();
  },
  getUserAuth:function(){
    let accountLogin = wx.getStorageSync('accountLogin');
    if(accountLogin.userType == 2 && accountLogin.schoolManager){
      this.setData({isSchoolManager:true})
    }
  },
  initIconPath(suffix){
    let { fileIcons } = imageUrl;
    const isVideo = /mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv/,
          isAudio = /avi|wmv|mpg|mpeg|mov|rm|ram|swf|flv|mp4|mp3|wma|avi|rm|rmvb|flv|mpg|mkv|m4a/,
          isImage = /jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg/,
          isFile = /pptx|ppt|xlsx|xls|doc|docx|pdf|wps/,
          isDocx = /doc|docx/,
          isPptx = /ppt|pptx/,
          isXlsx = /xlsx|xls/,
          isPdf = /pdf/;
    if(isDocx.test(suffix)) return `${fileIcons}doc.png`;
    if(suffix == 'fbx') return `${fileIcons}fbx.png`;
    if(suffix == 'folder') return `${fileIcons}folder.png`;
    if(isAudio.test(suffix)) return `${fileIcons}music.png`;
    if(isPptx.test(suffix)) return `${fileIcons}pptx.png`;
    if(isXlsx.test(suffix)) return `${fileIcons}xlsx.png`;
    if(isPdf.test(suffix)) return `${fileIcons}pdf.png`;
    return imageUrl.fileDefault
  },
  getDirectoryList(reachBottom = false){
    let { directoryId, keyword } = this.data, { dataList, fileTypeActions, filterTypeActions, fileTypeIndex, filterTypeIndex } = this.data;
    let param = {
      busType:'document_cloud',
      directoryId,
      keyword,
      fileType:fileTypeActions[fileTypeIndex].type,
      withDirectory:true,
      currentPage:this.data.currentPage,
      pageSize:30
    }
    req('GET',URL.getFiles,param,{},false).then(data=>{
      data.directoryList.map(c=>{
        c.gmtCreate = timeFormat('yyyy-MM-dd hh:mm',c.gmtCreate);
        c.thumb = `${imageUrl.fileIcons}folder.png`;
        c.checked = false;
      })
      data.fileList.map(c=>{
        c.gmtCreate = timeFormat('yyyy-MM-dd hh:mm',c.gmtCreate);
        c.fileSize = getFileSize(c.fileSize);
        c.checked = false;
        if(c.thumb){
          c.thumb = /http|https/.test(c.thumb)?c.thumb:STATIC_DOMAIN + c.thumb;
          let index = c.thumb.lastIndexOf('.');
          if( /jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg/.test(c.suffix)) c.thumb = `${c.thumb.substring(0,index)}!small${c.thumb.substring(index,c.thumb.length)}`;
        }else{
          c.thumb =  initIconPath(c.suffix);
        }
      })
      if(reachBottom){
        let { directoryList, fileList } = dataList;
        directoryList = [...directoryList,...data.directoryList];
        fileList = [...fileList,...data.fileList];
        dataList = {directoryList,fileList}
      }else{
        dataList = data;
      }
      
      this.setData({
        dataList,
        loadingState:0
      })
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
    if(this.data.timer) clearInterval(this.data.timer)
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
    this.getDirectoryList(true)
  },
  // 关闭筛选模态框
  onClickHide(){
    this.setData({
      showFilterTypeModal:false,
      showFileTypeModal:false
    })
  },
  //选择筛选类型
  choseType(e){
    let { index, cate } = e.currentTarget.dataset;
    this.setData({[cate]:index})
    this.onClickHide();
    if(cate == 'fileTypeIndex'){
      this.setData({currentPage:1})
      this.getDirectoryList();
    }
    if(cate == 'filterTypeIndex'){
      this.setData({
        isTile:index == 0 ? true : false
      })
    }
  },
  // 选择根目录
  choseCatelogue(e){
    let { index } = e.currentTarget.dataset, { catalogue } = this.data;
    if(index == catalogue.length-1) return;
    catalogue = catalogue.splice(0,index+1);
    this.setData({
      directoryId:catalogue[index].directoryId,
      catalogue
    })
    this.setData({currentPage:1})
    this.getDirectoryList();
  },
  // 折叠搜索
  foldSearch(){
    let { showSearch } = this.data;
    this.setData({showSearch:!showSearch})
    if(showSearch){
      this.setData({currentPage:1})
      this.getDirectoryList();
    }
  },
  // 输入内容
  inputSearchWord(e){
    this.setData({keyword:e.detail})
  },
  // 搜索
  onSearchBegin(e){
    this.setData({currentPage:1})
    this.getDirectoryList();
  },
  // 点击按钮
  showActionSheet(){
    let { directoryId } = this.data;
    wx.showActionSheet({
      itemList: ['新建文件夹','图片','视频','文件'],
      success:res=>{
        switch(res.tapIndex){
          case 0:
            this.setData({
              modalFunIndex:0
            })
            this.foldModal();
            break;
          case 1:
            this.choseFiles('image',directoryId,'document_cloud');
            break;
          case 2:
            this.choseFiles('video',directoryId,'document_cloud');
            break;
          case 3:
            this.choseFiles('fiels',directoryId,'document_cloud');
            break;
          default:
            break;
        }
      }
    })
  },
  foldModal(){
    let { showModal } = this.data;
    this.setData({
      showModal:!showModal
    })
    if(showModal) this.cancelEdit();
  },
  // 显示重命名
  showRename(){
    let { dataList, renameType, modalFuns, modalFunIndex } = this.data, total = 0, one = '';
    dataList.directoryList.map(c=>{
      if(c.checked){
        total ++ ;
        one = c;
        renameType = 0
      }
    })
    dataList.fileList.map(c=>{
      if(c.checked){
        total ++ ;
        one = c;
        renameType = 1
      }
    })
    if(total == 0){
      wx.showToast({
        title: '未选择文件',
        icon:'none'
      })
      return;
    }
    if(total > 1){
      wx.showToast({
        title: '重命名不支持多个文件修改',
        icon:"none"
      })
      return;
    }
    modalFunIndex = 1;
    modalFuns[modalFunIndex].value = renameType != 1 ? one.directoryName : one.fileName;
    this.setData({modalFunIndex,renameData:one,renameType, modalFuns})
    this.foldModal();
  },
  // 取消管理
  cancelEdit(){
    let { dataList } = this.data;
    dataList.directoryList.map(c=>c.checked = false);
    dataList.fileList.map(c=>c.checked = false);
    this.setData({
      isEdit:false,
      dataList
    })
  },
  createNewFolderConfirm(){
    let { directoryId, directoryName } = this.data;
    if(!directoryName || !directoryName.trim()){
      wx.showToast({
        title: '文件夹名不能为空！',
        icon:'none'
      })
      return;
    }
    req('POST',URL.addDirectory,{},{busType:'document_cloud',directoryId,directoryName},false).then(data=>{
      this.foldModal();
      this.setData({currentPage:1})
      this.getDirectoryList();
      if(this.data.showMoveFile) this.selectComponent('#moveFiles').getDirectoryList();
    }).catch(err=>{
      console.log(err)
      wx.showToast({
        title: err.message,
        icon:'none'
      })
    })
  },
  // 重命名
  renameFile(){
    let { newFileName, renameData, renameType } = this.data;
    if(typeof(newFileName) == 'object'){
      wx.showToast({
        title: '请输入文件名!',
        icon:'none'
      })
      return;
    }
    if(newFileName.trim() == ''){
      wx.showToast({
        title: '请输入文件名!',
        icon:'none'
      })
      return;
    }
    let param = renameType != 1 ? {"busType": "document_cloud","directoryName": newFileName,directoryId:renameData.directoryId} : {"busType": "document_cloud",fileName:newFileName,fileId:renameData.fileId},
      url = renameType != 1 ? URL.renameDirectory : URL.renameFile;
    wx.showLoading({
      title: '加载中…',
      mask:true
    })
    req('POST',url,{},param,false).then(data=>{
      wx.hideLoading();
      wx.showToast({
        title: '修改成功！',
        icon:'none'
      })
      this.foldModal();
      this.setData({currentPage:1})
      this.getDirectoryList();
    }).catch(err=>{
      console.log(err)
      wx.hideLoading();
      wx.showToast({
        title: '修改失败，请重试！',
        icon:'none'
      })
    })
  },
  // 删除文件
  deleteFile(e){
    let { dataList } = this.data,param = {
      busType:'document_cloud',
      directoryIds:[],
      fileIds:[]
    },toastText = '确定要删除所选文件吗？';
    dataList.directoryList.map(c=>c.checked ? param.directoryIds.push(c.directoryId):'');
    dataList.fileList.map(c=> c.checked ? param.fileIds.push(c.fileId):'' );
    if(param.directoryIds.length+param.fileIds.length == 0){
      wx.showToast({
        title: '请选择要删除的文件！',
        icon:'none'
      })
      return;
    }
    if(param.directoryIds.length && !param.fileIds.length){
      toastText = '确定要删除已选文件夹吗？文件夹内文件也会一并删除！';
    }
    if(param.directoryIds.length && param.fileIds.length){
      toastText = '确定要删除已选文件和文件夹吗？文件夹内文件也会一并删除！'
    }
    wx.showModal({
      title:'提示',
      content:toastText,
      success:res=>{
        if(res.confirm){
          req('POST',URL.delFile,{},param,false).then(data=>{
            wx.showToast({
              title: '删除成功！',
              icon:'none'
            })
            this.setData({currentPage:1})
            this.getDirectoryList();
            this.cancelEdit();
          }).catch(err=>{
            this.cancelEdit();
            wx.showToast({
              title: err.message,
              icon:'none'
            })
          })
        }
      }
    })
  },
  returnUploadPath(data){
    // let { modalFuns } = this.data;
    // modalFuns[1].value = data.sourceName;
    // let renameData = {
    //   fileId:data.commonSourceId
    // }
    // this.setData({modalFunIndex:1,renameData,renameType:1, modalFuns,showModal:true})
    this.setData({currentPage:1})
    this.getDirectoryList();
  },
  // 点击移动文件
  moveFile(){
    let { dataList } = this.data, moveNumber = 0, param = {
      busType: "document_cloud",
      directoryIds: [],
      fileIds: [],
      targetDirectoryId: ''
    };
    dataList.directoryList.map(c=>c.checked ? param.directoryIds.push(c.directoryId):'');
    dataList.fileList.map(c=> c.checked ? param.fileIds.push(c.fileId):'' );
    moveNumber = param.directoryIds.length + param.fileIds.length;
    if(moveNumber < 1){
      wx.showToast({
        title: '请选择文件',
        icon:'none'
      })
      return;
    }
    this.setData({showMoveFile:true,moveNumber,moveFileParam:param})
  },
  // 移动文件到文件夹
  moveFileToFolder(directoryId){
    let { moveFileParam } = this.data;
    moveFileParam.targetDirectoryId = directoryId;
    req('POST',URL.moveFile,{},moveFileParam,false).then(data=>{
      wx.showToast({
        title: '移动成功！',
        icon:'none'
      })
      this.setData({showMoveFile:false,isEdit:false})
      this.setData({currentPage:1})
      this.getDirectoryList();
    }).catch(err=>{
      wx.showToast({
        title: err.message,
        icon:'none'
      })
    })
  },
  // 长按显示操作框
  showEditActionSheet(e){
    let { cate } = e.currentTarget.dataset,isOnlyShare = false;
    console.log(e)
    let itemList = cate == 'directoryList' ? ['重命名','移动至','删除'] : ['重命名','移动至','删除','分享']
    let { isSchoolManager } = this.data;
    if(!isSchoolManager){
      if(cate == 'directoryList') return;
      itemList = ['分享']
      isOnlyShare = true;
    }
    wx.showActionSheet({
      itemList,
      success:res=>{
        if(isOnlyShare) this[panelBar[3].fn]();
        else this[panelBar[res.tapIndex].fn]();
      },
      fail:err=>{
        console.log(err)
        this.cancelEdit();
      },
      complete:res=>{
        console.log(res)
      }
    })
  },
  // 显示分享框
  showSharePanel(e){
    this.setData({
      'sharePanel.isShow':true
    })
  },
  // 关闭分享框
  onClose(e){
    this.setData({
      'sharePanel.isShow':false
    })
    this.cancelEdit();
  },
  // 点击分享选项
  onSelect(e){
    console.log(e)
    if(e.detail.icon  == 'link'){
      let { dataList } = this.data;
      let one = dataList.fileList.filter(c=>c.checked);
      one = one[0];
      let url = `${wx.getStorageSync('otherHttpsecondLevelDomain')}${one.downloadUrl}&tr=1`;
      wx.setClipboardData({
        data: url,
        success:res=>{
          wx.showToast({
            title: '链接已复制，快去分享给朋友吧！',
            icon:'none'
          })
        }
      })
    }
  },
  onShareAppMessage:function(e) {
    console.log(e)
    if(e.from === 'button'){
      let param = this.getShareFileParam();
      console.log(param)
      this.setData({'sharePanel.isShow':false})
      
      return param;
    }
    if(e.from === 'menu'){
      let { hisImageUrl, otherVCardInfo, cardIdStr, visitingCardIdStr, navBar } = globalData,basePath = '';
      basePath = '/pages/cardDetail/cardDetail'
      let path = `${basePath}?sourceType=share&cardIdStr=${encodeURIComponent(cardIdStr)}&shareCardIdStr=${encodeURIComponent(visitingCardIdStr)}&serverName=${wx.getStorageSync('otherSecondLevelDomain')}`;
      let title = otherVCardInfo.shareTitle || `这是我的电子名片，分享给您！`;
      path = shareAddRetailParam(path)
      path += `&sreId=${encodeURIComponent(this.newSreId)}&routeId=${this.newRouteId}`;
      return {
        path,
        imageUrl:hisImageUrl,
        title
      }
    }
    
  },
 
  getShareFileParam:function(e) {
    let { dataList } = this.data;
    let one = dataList.fileList.filter(c=>c.checked);
    one = one[0];
    console.log(one)
    let path = `/packageB/pages/file-share/file-share?fileId=${one.fileId}&serverName=${wx.getStorageSync('otherSecondLevelDomain')}&cardIdStr=${encodeURIComponent(wx.getStorageSync('cardIdStr'))}`,
        imageUrl = one.thumb,
        title = one.fileName;
    path = shareAddRetailParam(path)
    // requestSreId(one.fileId).then(data=>{
    //   this.newSreId = data;
    //   addShareLog(title,25,one.fileId,'',1,3,this.newSreId,'');
    // })
    
    return { path, imageUrl, title }
  },
  showFileTypeSheet, showFilterTypeSheet, twoWayBinding,
  choseFiles, choseImageConfirm, choseVideoConfirm, uploadLargeFile, choseFilesConfirm, initIconPath
})