import { imageUrl } from '../../../utils/image'; 
import { URL } from '../../../utils/config';
import { req } from '../../../utils/service';
import { genUuid } from '../../../utils/util'; 

const fileTypeActions = [ 
  { 
    label:'全部', 
    icon:imageUrl.fileIcons+'all.png', 
    type:'' 
  }, 
  { 
    label:'文档', 
    icon:imageUrl.fileIcons+'word.png', 
    type:'doc' 
  }, 
  { 
    label:'图片', 
    icon:imageUrl.fileIcons+'picture.png', 
    type:'image' 
  }, 
  { 
    label:'视频', 
    icon:imageUrl.fileIcons+'video.png', 
    type:'video' 
  }, 
  { 
    label:'音频', 
    icon:imageUrl.fileIcons+'audio.png', 
    type:'audio' 
  }, 
  { 
    label:'其他', 
    icon:imageUrl.fileIcons+'other1.png', 
    type:'other' 
  } 
] 
 
const filterTypeActions = [ 
  { 
    label:'列表排列', 
    icon:imageUrl.fileIcons+'tile.png'
  }, 
  { 
    label:'图标排列', 
    icon:imageUrl.fileIcons+'list.png' 
  } 
]
 
// 目录 
const catalogue = [ 
  { 
    label:'根目录', 
    directoryId:'0' 
  } 
] 
 
const showFileTypeSheet = function(params) { 
  this.setData({ 
    showFileTypeModal:!this.data.showFileTypeModal 
  }) 
} 
 
const showFilterTypeSheet = function(params) { 
  this.setData({ 
    showFilterTypeModal:!this.data.showFilterTypeModal 
  }) 
} 

const modalFuns = [
  {
    title:'新建文件夹',
    cate:'directoryName',
    cancelText:'取消',
    value:'',
    confirmText:'确定',
    closeFn:'foldModal',
    confirmFn:'createNewFolderConfirm'
  },
  {
    title:'重命名',
    cate:'newFileName',
    value:'',
    cancelText:'取消',
    confirmText:'确定',
    closeFn:'foldModal',
    confirmFn:'renameFile'
  },
  {
    title:'新建文件夹',
    cate:'directoryName',
    cancelText:'取消',
    confirmText:'确定',
    closeFn:'foldModal',
    confirmFn:'createNewFolderConfirm'
  }
]

const panelBar = [
  {
    label:'重命名',
    icon:imageUrl.fileEditIcons+'rename.png',
    fn:'showRename'
  },
  {
    label:'移动至',
    icon:imageUrl.fileEditIcons+'move.png',
    fn:'moveFile'
  },
  {
    label:'删除',
    icon:imageUrl.fileEditIcons+'delete.png',
    fn:'deleteFile'
  },
  {
    label:'分享',
    icon:imageUrl.fileEditIcons+'delete.png',
    fn:'showSharePanel'
  },
  {
    label:'取消',
    icon:imageUrl.fileEditIcons+'cancel.png',
    fn:'cancelEdit'
  },
]



// 分享框

const sharePanel = {
  isShow:false,
  options:[
    { name: '微信', icon: 'wechat', openType: 'share' },
    // { name: '复制链接', icon: 'link' }
  ]
}

const initIconPath = function (suffix){
  let { fileIcons } = imageUrl;
  const isVideo = /mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv/,
        isAudio = /avi|wmv|mpg|mpeg|mov|rm|ram|swf|flv|mp4|mp3|wma|avi|rm|rmvb|flv|mpg|mkv/,
        isImage = /jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg/,
        isFile = /pptx|ppt|xlsx|xls|doc|docx|pdf|wps/,
        isDocx = /doc|docx/,
        isPptx = /ppt|pptx/,
        isXlsx = /xlsx|xls/;
  if(isDocx.test(suffix)) return `${fileIcons}doc.png`;
  if(suffix == 'fbx') return `${fileIcons}fbx.png`;
  if(suffix == 'pdf') return `${fileIcons}pdf.png`;
  if(suffix == 'txt') return `${fileIcons}txt.png`;
  if(suffix == 'folder') return `${fileIcons}folder.png`;
  if(suffix == 'zip') return `${fileIcons}zip.png`;
  if(suffix == 'rar') return `${fileIcons}rar.png`;
  if(suffix == '7z') return `${fileIcons}7z.png`;
  if(suffix == 'tar') return `${fileIcons}tar.png`;
  if(suffix == 'gz') return `${fileIcons}gz.png`;
  if(isAudio.test(suffix)) return `${fileIcons}music.png`;
  if(isPptx.test(suffix)) return `${fileIcons}pptx.png`;
  if(isXlsx.test(suffix)) return `${fileIcons}xlsx.png`;
  return imageUrl.fileDefault
}

 // 获取分享路径参数
const requestSreId = function(id) {
  return new Promise((resolve,reject)=>{
    req('POST',URL.requestSreId,{},{busId:id,busType:25,sreId:'',url:'',noNeedVcToken:wx.getStorageSync('accountLogin') ? true : false},false).then(data=>{
      resolve(data)
    }).catch(err=>{
      console.log(err)
      reject(err)
    })
  })
 
}

module.exports = { 
  fileTypeActions, filterTypeActions, showFileTypeSheet, showFilterTypeSheet, catalogue, modalFuns, panelBar, sharePanel, initIconPath, requestSreId
}