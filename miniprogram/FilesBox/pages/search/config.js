import {STATIC_DOMAIN} from '../../utils/config';
const urls = STATIC_DOMAIN + '/appstatic';
const imageArr1 = [
    {
        url:'/images/icons/search_music.png',
        name:'音乐',
        bgColor:'#FFFAF3',
        fileType:'mp3,wav,wma,m4a,ogg,omf,amr,aa3,flac,aac,cda,aif,aiff,mid,ra,ape',
        type:'music'
    },{
        url:'/images/icons/search_movie.png ',
        name:'视频',
        bgColor:'#F4F8FC',
        fileType:'mp4,flv,rm,rmvb,avi,mkv,mov,f4v,mpeg,mpg,vob,wmv,ogv,webm,3gp,mts,m2ts,m4v,mpe,3g2,asf,dat,asx,wvx,mpa',
        type:'movie'
    },{
        url:'/images/icons/search_word.png',
        name:'文档',
        bgColor:'#F2F4FE',
        type:'doc',
        fileType:'txt,md,pdf,ofd,doc,docx,xls,xlsx,ppt,pptx,xps,pps,ppsx,ods,odt,odp,docm,dot,dotm,xlsb,xlsm,mht,djvu,wps,dpt,csv,et,ett,pages,numbers,key,dotx,vsd,vsdx,mpp',
    },{
        url:'/images/icons/search_image.png ',
        name:'图片',
        bgColor:'#FFFAFB',
        type:'image',
        fileType:'jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai',
    },{
        url:'/images/icons/search_zip.png',
        name:'压缩',
        bgColor:'#FFFDF3',
        type:'zip',
        fileType:'zip,gz,rar,iso,tar,7z,gz,ar,bz,bz2,xz,arj',
    },{
        url:'/images/icons/search_other.png ',
        name:'其他',
        bgColor:'#F7FFF0',
        type:'other',
        fileType:'psd,swf,html,exe,msi',
    },
]
const imageArr2 = [
    {
        url:'/images/icons/search_fav.png',
        name:'收藏夹',
        bgColor:'#F2F2FE',
        block:'fav',
        type:'fav',
        show:true
    },{
        url:'/images/icons/search_news.png',
        name:'资讯',
        bgColor:'#F4F8FC',
        type:'news',
        show:true
    },
    {
        url:'/images/icons/search_recent_files_icon.png',
        name:'最近的',
        bgColor:'#F2F2FE',
        block:'userRencent',
        type:'recentDoc',
        show:true
    },{
        url:'/images/icons/search_share_files_icon.png',
        name:'分享的',
        bgColor:'#F4F8FC',
        type:'shareLink',
        show:true
    },{
        url:'/images/icons/search_tags_files_icon.png',
        name:'标签',
        bgColor:'#F4F8FC',
        block:'fileTag',
        type:'fileTag',
        show:true
    },{
        url:'/images/icons/search_recycle_bin.png',
        name:'回收站',
        bgColor:'#F2F2FE',
        block:'recycle',
        type:'recycle',
        show:true
    },
]

module.exports = {
    imageArr1,imageArr2
}