import {STATIC_DOMAIN} from '../../utils/config';
const urls = STATIC_DOMAIN + '/appstatic';
const listArr = [
    {
        name:'收藏夹',
        url:'/images/rootIcon/fav.png',
        icon:'fav',
    },
    {
        name:'我分享的',
        url:'/images/rootIcon/shareLink.png',
        icon: "shareLink"
    },
    {
        name:'最近文档',
        url:'/images/rootIcon/recentDoc.png',
        icon:'recentDoc'
    },
]
const moreArr = [
    {
        name:'文档',
        image:'/images/operateMore/icon_file_attribute.png',
        fn:'getMoreOperate'
    },
    {
        name:'图片',
        image:'/images/more_icon/my_picture.png',
        fn:'getMoreOperate'
    },
    {
        name:'音乐',
        image:'/images/more_icon/music.png',
        fn:'getMoreOperate'
    },
    {
        name:'视频',
        image:'/images/more_icon/video.png',
    }, 
    {
        name:'压缩',
        image:'/images/more_icon/zip_files.png',
    },
    {
        name:'其他',
        image:'/images/list_icon/icon_file_more.png',
    }
]
const actionArr = [
    {
        name:'文件类型',
        value:'不限类型',
    },
    {
        name:'时间范围',
        value:'不限时间',
    },
    {
        name:'文件大小',
        value:'不限大小',
    },
    {
        name:'用户',
        value:'请选择',
    }
]
const fileTypeArr = [
    {
        name:'不限类型',
        type:'all'
    },
    {
        name:'任意文件',
        type:'allFile'
    },
    {
        name:'文件夹',
        type:'folder'
    },
    {
        name:'文档',
        type:'txt,md,pdf,ofd,doc,docx,xls,xlsx,ppt,pptx,xps,pps,ppsx,ods,odt,odp,docm,dot,dotm,xlsb,xlsm,mht,djvu,wps,dpt,csv,et,ett,pages,numbers,key,dotx,vsd,vsdx,mpp'
    },
    {
        name:'图片',
        type:'jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai'
    },
    {
        name:'音乐',
        type:'allFimp3,wav,wma,m4a,ogg,omf,amr,aa3,flac,aac,cda,aif,aiff,mid,ra,apele'
    },
    {
        name:'视频',
        type:'mp4,flv,rm,rmvb,avi,mkv,mov,f4v,mpeg,mpg,vob,wmv,ogv,webm,3gp,mts,m2ts,m4v,mpe,3g2,asf,dat,asx,wvx,mpa'
    },
    {
        name:'压缩包',
        type:'zip,gz,rar,iso,tar,7z,gz,ar,bz,bz2,xz,arj'
    },
    {
        name:'自定义'
    },
]
const fileTimeArr = [
    {
        name:'不限时间'
    },
    {
        name:'近一天'
    },
    {
        name:'最近7天'
    },
    {
        name:'最近30天'
    },
    {
        name:'最近一年'
    },
    {
        name:'自定义'
    },
]
const fileSizeArr = [
    {
        name:'不限大小',
    },
    {
        name:'0~100KB',
        minSize:0,
        maxSize:102400
    },
    {
        name:'100KB~1MB',
        minSize:102400,
        maxSize:1048576
    },
    {
        name:'1MB~10MB',
        minSize:1048576,
        maxSize:10485760
    },
    {
        name:'10MB~100MB',
        minSize:10485760,
        maxSize:104857600
    },
    {
        name:'100MB~1GB',
        minSize:104857600,
        maxSize:1073741824
    },
    {
        name:'1GB以上',
        minSize:1073741824,
        maxSize:0
    },
    {
        name:'自定义'
    },
]
const customFileTypeArr = [
    {
        name:'doc',
        checked:false,
    }, {
        name:'docx',
        checked:false,
    }, {
        name:'xls',
        checked:false,
    }, {
        name:'xlsx',
        checked:false,
    }, {
        name:'ppt',
        checked:false,
    }, {
        name:'pptx',
        checked:false,
    }, {
        name:'pdf',
        checked:false,
    },
]
const showList = [
    {
        image:'/images/icons/icon_rv_mode_grid.png',
        name:'宫格模式'
    },
    {
        image:'/images/icons/icon_rv_mode_list.png',
        name:'列表模式'
    }
]
const sortFieldList = [
    {
        name:'名称',
        field:'name'
    },
    {
        name:'类型',
        field:'fileType'
    },
    {
        name:'大小',
        field:'size'
    },
    {
        name:'修改时间',
        field:'modifyTime'
    }
]
const sortTypeList = [
    {
        name:'递增',
        type:'asc'
    },
    {
        name:'递减',
        type:'desc'
    },
]
module.exports = {
    listArr,moreArr,actionArr,fileTypeArr,fileTimeArr,fileSizeArr,customFileTypeArr,showList,sortFieldList,sortTypeList
}