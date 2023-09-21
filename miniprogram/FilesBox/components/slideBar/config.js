import {STATIC_DOMAIN} from '../../utils/config';
const urls = STATIC_DOMAIN + '/appstatic';
const moreArr = [
    {
        text:'位置',
        children:[
            {
                url:'/images/more_icon/collect.png',
                text:'收藏夹'
            },{
                url:'/images/more_icon/personal_space.png ',
                text:'个人空间'
            },{
                url:'/images/more_icon/company_space.png',
                text:'企业网盘'
            }
        ],
        show:true
    },
    {
        text:'工具',
        children:[
            {
                url:'/images/more_icon/recent_files.png ',
                text:'最近文档'
            },{
                url:'/images/more_icon/my_work.png',
                text:'我的协作'
            },{
                url:'/images/more_icon/share_link.png ',
                text:'外链分享'
            },
            {
                url:'/images/more_icon/my_picture.png ',
                text:'我的相册'
            },{
                url:'/images/more_icon/delete_files.png',
                text:'回收站'
            }
        ],
        show:true
    },
    {
        text:'文件类型',
        children:[
            {
                url:'/images/more_icon/words.png ',
                text:'文档'
            },{
                url:'/images/more_icon/picture.png',
                text:'图片'
            },{
                url:'/images/more_icon/music.png ',
                text:'音乐'
            },
            {
                url:'/images/more_icon/video.png ',
                text:'视频'
            },{
                url:'/images/more_icon/zip_files.png',
                text:'压缩包'
            },{
                url:'/images/more_icon/others.png',
                text:'其他'
            }
        ],
        show:true
    },
    {
        text:'标签',
        children:[],
        show:false
    }
]

module.exports = {
    moreArr
}