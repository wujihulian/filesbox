module.exports = {
    DOMAIN: "", // 主域名
    STATIC_DOMAIN: "", // 静态资源域名
    URL:{
        getDiskList:'/api/disk/list/path',//获取文件夹、文件列表
        getShareList:'/api/disk/userShare/list',//获取分享列表
        shareToMeList:'/api/disk/userShare/shareToMe',//我的协作列表
        getPreviewInfo:'/api/disk/preview', //预览/下载
        diskOperation:'/api/disk/operation', //文件操作
        addDirectory:'/api/disk/createFolder',//创建文件夹
        getPath:'/api/disk/list/path',//获取目录 
        uploadCheck:'/api/disk/upload/check',//文件是否存在验证 (秒传)
        uploadFile:'/api/disk/upload',//文件上传
        getUserList:'/api/disk/user/list',//获取用户列表
        getTagList:'/api/disk/tag/list',//获取标签列表
        chooseTag:'/api/disk/tag/fileTag',//选中/取消标签
        delTag:'/api/disk/tag/del',//删除标签
        addTag:'/api/disk/tag/add',//增加标签
        editTag:'/api/disk/tag/edit',//编辑标签
        getPathLog:'/api/disk/pathLog',//文件动态
        zipFile:'/api/disk/zip',//压缩文件
        checkZip:'/api/disk/zip/taskAction',//查看压缩是否完成
        unzipFile:'/api/disk/unZip',//解压文件
        getHistoryList:'/api/disk/history/get',//获取历史版本
        deleteVersion:'/api/disk/history/delete',//删除历史版本
        setVersion:'/api/disk/history/setVersion',//设置为当前版本
        setVersionIntro:'/api/disk/history/setDetail',//设置历史版本描述
        getShareInfo:'/api/disk/userShare/get',//获取分享的内容
        saveShare:'/api/disk/userShare/save',//添加外链分享
        cancelShare:'/api/disk/userShare/cancel',//取消分享
        unzipList:'/api/disk/unzipList',//压缩预览
        checkIsEncrypted:'/api/disk/checkIsEncrypted',//检查压缩包是否需要解密
        getNoticeList:'/api/notice/read/list/page',//获取消息列表
        getNoticeInfo:'/api/notice/info',//获取消息详情
        unreadNotice:'/api/notice/unread',//获取未读数
        getInfoList:'/api/disk/getInfoList',//获取资讯列表
        getInfoTypeList:'/api/disk/getInfoTypeList',//获取资讯分类
        convert:'/api/disk/doc/convert',//格式转换
        getLogList:'/api/disk/log/get',//获取日志
        setUserInfo:'/api/disk/user/setUserInfo',//设置账号信息
        userProportion:'/api/disk/userProportion',//用户空间分布
        getComments:'/api/disk/comment/list',//获取评论列表
        saveComment:'/api/disk/comment/save',//保存评论
        delComment:'/api/disk/comment/del',//删除评论
    }
}