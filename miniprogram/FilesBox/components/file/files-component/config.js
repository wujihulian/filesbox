import { imageUrl } from '../../../utils/image'; 
import { URL } from '../../../utils/config';
import { req } from '../../../utils/service';
import {STATIC_DOMAIN} from '../../../utils/config';
const urls = STATIC_DOMAIN + '/appstatic';
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
  // { 
  //   label:'个人空间', 
  //   sourceID:60182
  // } 
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

// 转换文件大小
const getFileSize = function(size) {
  if(size>1024*1024*1024) return `${(size/(1024*1024*1024)).toFixed(2)}GB`;
  else if(size>1024*1024) return `${(size/(1024*1024)).toFixed(2)}MB`;
  else if(size>1024) return `${parseInt(size/(1024))}KB`;
  else return `0KB`
}

// 分享框

const sharePanel = {
  isShow:false,
  options:[
    { name: '微信', icon: 'wechat', openType: 'share' },
    { name: '复制链接', icon: 'link' }
  ]
}

const initIconPath = function (suffix){
  let { fileIcons} = imageUrl;
  const isVideo = /mp4|mpg|mpeg|dat|asf|avi|rm|rmvb|mov|wmv|flv|mkv|webm/,
        isAudio = /avi|wmv|mpg|mpeg|mov|rm|ram|swf|flv|mp3|wma|avi|rm|rmvb|flv|mpg|mkv|flac|wav|m4a/,
        isImage = /jpeg|jpg|gif|png|svg|webp|jfif|bmp|dpg/,
        isFile = /pptx|ppt|xlsx|xls|doc|docx|pdf|wps/,
        isDocx = /doc|docx/,
        isPptx = /ppt|pptx/,
        isXlsx = /xlsx|xls/,
        isOther = /tiff|tga|ps|psb|dwg|dae|odp|odt|ods|js|dpt|ett|et|wpt|vsd|vsdx|dotx|dot|dotm|raw|tif|webp|dwf|skp|nwc|fbx|pos|ofd|smm|km|mind/;
  if(isDocx.test(suffix)) return `${fileIcons}doc.png`;
  if(suffix == 'fbx') return `${fileIcons}fbx.png`;
  if(suffix == 'pdf') return `${fileIcons}pdf.png`;
  if(suffix == 'txt'||suffix == 'text') return `${fileIcons}txt.png`;
  if(suffix == 'folder') return `${fileIcons}folder.png`;
  if(suffix == 'zip') return `${fileIcons}zip.png`;
  if(suffix == 'rar') return `${fileIcons}rar.png`;
  if(suffix == '7z') return `${fileIcons}7z.png`;
  if(suffix == 'tar') return `${fileIcons}tar.png`;
  if(suffix == 'gz') return `${fileIcons}gz.png`;
  if(suffix == 'mht') return `${fileIcons}mht.png`;
  if(suffix == 'mhtml') return `${fileIcons}mhtml.png`;
  if(suffix == 'eml') return `${fileIcons}eml.png`;
  if(suffix == 'epub') return `${fileIcons}epub.png`;
  if(suffix == 'ttf'||suffix == 'url') return `${fileIcons}ttf.png`;
  if(isImage.test(suffix)) return `${fileIcons}bmp.png`;
  if(isVideo.test(suffix)) return `${fileIcons}movie/movie.png`;
  if(isAudio.test(suffix)) return `${fileIcons}music.png`;
  if(isPptx.test(suffix)) return `${fileIcons}pptx.png`;
  if(isXlsx.test(suffix)) return `${fileIcons}xlsx.png`;
  if(suffix == 'html') return `${fileIcons}html.png`;
  if(suffix == 'exe') return `${fileIcons}exe.png`;
  if(suffix == 'psd') return `${fileIcons}psd.png`;
  if(suffix == 'svg') return `${fileIcons}svg.png`;
  if(suffix == 'md') return `${fileIcons}md.png`;
  if(suffix == 'apk') return `${fileIcons}apk.png`;
  if(suffix == 'ipa') return `${fileIcons}ipa.png`;
  if(suffix == 'oexe') return `${fileIcons}oexe1.png`;
  if(suffix == 'xml') return `${fileIcons}xml.png`;
  if(suffix == 'ai') return `${fileIcons}ai.png`;
  if(suffix == 'drawio') return `${fileIcons}drawlogo144.png`;
  if(suffix == 'dcm') return `${fileIcons}dcm.png`;
  if(suffix == 'vsdx') return `${fileIcons}vsdx.png`;
  if(suffix == 'xmind') return `${fileIcons}xmindIcon.png`;
  if(suffix == 'colorPicker') return `${fileIcons}colorPicker.png`;
  if(suffix == 'videoRecording') return `${fileIcons}videoRecording.png`;
  if(suffix == 'srt') return `${fileIcons}srt.png`;
  if(suffix == 'et') return `${fileIcons}et.png`;
  if(suffix == 'wps') return `${fileIcons}wps.png`;
  if(suffix == 'smm') return `${fileIcons}smm.png`;
  if(suffix == ''||isOther.test(suffix)) return `${fileIcons}file.png`;
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
const addArr = [
  {
      url:'/images/icons/video_files_icon.png',
      text:'视频'
  },{
      url:'/images/icons/pictures_files_icon.png ',
      text:'图片'
  },{
      url:'/images/icons/word_files_icon.png',
      text:'文件'
  },{
      url:'/images/icons/clipboard.png',
      text:'剪贴版'
  },{
      url:'/images/icons/folder_icon.png',
      text:'文件夹'
  }
]
module.exports = { 
  fileTypeActions, filterTypeActions, showFileTypeSheet, showFilterTypeSheet, catalogue, modalFuns, panelBar, getFileSize, sharePanel, initIconPath, requestSreId,addArr
}