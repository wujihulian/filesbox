import {STATIC_DOMAIN} from '../../utils/config';
const urls = STATIC_DOMAIN + '/appstatic';
const listArr = [
    {
        text:'复制',
        image:'/images/list_icon/icon_file_copy.png',
        fn:'moveFile',
        type:'copy'
    },
    {
        text:'剪切',
        image:'/images/list_icon/icon_file_cutting.png',
        fn:'moveFile',
        type:'move'
    },
    {
        text:'删除',
        image:'/images/list_icon/icon_file_del.png',
        fn:'deleteFile'
    },
    {
        text:'重命名',
        image:'/images/list_icon/icon_file_rename.png',
        fn:'showRename'
    },
    {
        text:'更多',
        image:'/images/list_icon/icon_file_more.png',
        fn:'getMoreOperate'
    }
]
const recycleArr = [
    {
        text:'彻底删除',
        image:'/images/list_icon/icon_file_del.png',
        fn:'removeFile'
    },
    {
        text:'还原',
        image:'/images/icons/reduction.png',
        fn:'reductionFile'
    },
]
const favArr = [
    {
        text:'打开',
        image:'/images/operateMore/icon_file_lastopen.png',
        fn:'openFile',
    },
    {
        text:'取消收藏',
        image:'/images/operateMore/icon_file_collect.png',
        fn:'collectFiles'
    },
    {
        text:'重命名',
        image:'/images/list_icon/icon_file_rename.png',
        fn:'showRename'
    },
    {
        text:'属性',
        image:'/images/operateMore/icon_file_attribute.png',
        fn:'showAttribute'
    }
]
const moreCheckedArr = [
    {
        text:'复制',
        image:'/images/list_icon/icon_file_copy.png',
        fn:'moveFile',
        type:'copy'
    },
    {
        text:'剪切',
        image:'/images/list_icon/icon_file_cutting.png',
        fn:'moveFile',
        type:'move'
    },
    {
        text:'删除',
        image:'/images/list_icon/icon_file_del.png',
        fn:'deleteFile'
    },
    {
        text:'重命名',
        image:'/images/list_icon/icon_file_rename.png',
        fn:'showRename'
    },
    {
        text:'压缩',
        image:'/images/operateMore/icon_file_zip.png',
        fn:'showPressedWay'
    }
]
const moreArr = [
    {
        text:'打开',
        image:'/images/operateMore/icon_file_lastopen.png',
        fn:'openFile',
        show:true
    },
    {
        text:'下载',
        image:'/images/operateMore/icon_file_download.png',
        fn:'downloadFiles',
        show:true
    },
    {
        text:'分享',
        image:'/images/operateMore/icon_file_share.png',
        fn:'shareLink',
        show:true
    },
    {
        text:'置顶',
        image:'/images/operateMore/icon_file_top.png',
        fn:'topFile',
        show:true
    },
    {
        text:'收藏',
        image:'/images/operateMore/icon_file_collect.png',
        fn:'collectFiles',
        show:true
    },
    {
        text:'上传新版本',
        image:'/images/icons/upload_new.png',
        fn:'uploadNewFile',
        show:true
    },
    {
        text:'格式转换',
        image:'/images/icons/format_icon.png',
        fn:'showFormat',
        show:true
    },
    {
        text:'创建压缩包',
        image:'/images/operateMore/icon_file_zip.png',
        fn:'showPressedWay',
        show:true
    },
    {
      text:'解压到...',
      image:'/images/operateMore/icon_file_zip.png',
      fn:'showPressedWay',
      show:true
    },
    {
        text:'标签',
        image:'/images/operateMore/icon_file_tag.png',
        fn:'showTagList',
        show:true
    },
    {
        text:'属性',
        image:'/images/operateMore/icon_file_attribute.png',
        fn:'showAttribute',
        show:true
    },
    {
        text:'文件查重',
        image:'/images/operateMore/filecc.png',
        fn:'repeatFile',
        show:true
    },   
    {
        text:'文件名查重',
        image:'/images/operateMore/filenamecc.png',
        fn:'repeatFileName',
        show:true
    },
    {
      text:'发送到桌面快捷方式',
      image:'/images/operateMore/sendDesktop.png',
      fn:'sendDesktop',
      show:true
    },{
      text:'创建快捷方式',
      image:'/images/operateMore/shortcut.png',
      fn:'creatShortcut',
      show:true
  }
]
const linkArr = [
    {
        text:'取消分享',
        image:'/images/list_icon/icon_file_del.png',
        fn:'cancelShare',
    },
    {
        text:'分享',
        image:'/images/operateMore/icon_file_share.png',
        fn:'shareLink'
    },
    {
        text:'属性',
        image:'/images/operateMore/icon_file_attribute.png',
        fn:'showAttribute'
    }
]
module.exports = {
    listArr,moreArr,recycleArr,favArr,moreCheckedArr,linkArr
}