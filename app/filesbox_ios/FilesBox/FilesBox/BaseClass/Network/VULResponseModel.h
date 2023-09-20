//
//  VULResponseModel.h
//  VideoULimit
//
//  Created by ZCc on 2018/9/10.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface VULResponseModel : NSObject

@end

#pragma mark - token
@interface VULResponseToken : NSObject

@property (nonatomic, copy) NSString *token;              /**< 登录后的token 值 */

@end

#pragma mark - 用户登录信息
@interface VULResponseLoginModel : NSObject

@property (nonatomic, copy) NSString *administrator;       /**< 是否系统管理员    */
@property (nonatomic, copy) NSString *avatar;       /**< 头像    */

@property (nonatomic, copy) NSString *ignoreFileSize;       /**< 上传文件大小(GB) 限制    */
@property (nonatomic, copy) NSString *label;       /**< css标签（颜色标签       */
@property (nonatomic, copy) NSString *lastLogin;       /**< 最后登录时间（时间戳        */
@property (nonatomic, copy) NSString *name;       /**< 登录名          */
@property (nonatomic, copy) NSString *nickname;       /**< 昵称         */
@property (nonatomic, copy) NSString *phone;       /**< 手机         */
@property (nonatomic, copy) NSString *roleID;       /**< 权限ID             */

@property (nonatomic, copy) NSString *roleName;       /**< 角色名称                 */
@property (nonatomic, copy) NSString *sex;       /**< 性别 (0女1男)                 */
@property (nonatomic, copy) NSString *userID;       /**< 用户ID*/

@property (nonatomic, copy) NSString *alipayOpenId;
@property (nonatomic, copy) NSString *code;
@property (nonatomic, copy) NSString *description;
@property (nonatomic, copy) NSString *dingOpenId;
@property (nonatomic, copy) NSString *email;
@property (nonatomic, copy) NSString *enWechatOpenId;
@property (nonatomic, copy) NSString *isSystem;
@property (nonatomic, copy) NSString *password;
@property (nonatomic, copy) NSString *sizeMax;
@property (nonatomic, copy) NSString *sizeUse;
@property (nonatomic, copy) NSString *wechatOpenId;


@end

#pragma mark - 个人信息
@class VULAnswerModel, VULHobbysModel, VULUserDetailModel;
@interface VULResponseUserInfoModel : NSObject
@property (nonatomic, strong) NSArray<VULAnswerModel *> *askAnswers;
@property (nonatomic, copy) NSString *creditAccount; /**<学分 */
@property (nonatomic, copy) NSString *detail; /**<用户详细信息 */
@property (nonatomic, copy) NSString *headPortrait; /**<头像 */
@property (nonatomic, strong) VULHobbysModel *hobbys;
@property (nonatomic, copy) NSString *loginName; /**<登录名 */
@property (nonatomic, copy) NSString *realName; /**<真实名字 */
@property (nonatomic, copy) NSString *scoreAccount; /**<可用积分 */
@property (nonatomic, copy) NSString *sex; /**<性别 1=男 0=女 */
@property (nonatomic, strong) VULUserDetailModel *userDetail;
@property (nonatomic, copy) NSString *nickname; /**<昵称 */
@property (nonatomic, copy) NSString *wechat;
@property (nonatomic, copy) NSString *qq;
@property (nonatomic, copy) NSString *weibo;
@property (nonatomic, copy) NSString *mobilePhone;


@end

@interface VULAnswerModel : NSObject
@property (nonatomic, copy) NSString *askAnswer; /**<问题答案 */
@property (nonatomic, copy) NSString *askId; /**<问题id */
@property (nonatomic, copy) NSString *detail; /**<问题内容 （中文）*/
@property (nonatomic, copy) NSString *detailEn; /**<问题id  （英文）*/
@end

@interface VULHobbysModel : NSObject
@property (nonatomic, copy) NSString *book;
@property (nonatomic, copy) NSString *food;
@property (nonatomic, copy) NSString *movie;
@property (nonatomic, copy) NSString *music;
@property (nonatomic, copy) NSString *sport;
@property (nonatomic, copy) NSString *travel;
@end

@interface VULUserDetailModel : NSObject
@property (nonatomic, copy) NSString *address;
@property (nonatomic, copy) NSString *age;
@property (nonatomic, copy) NSString *blood; /**<血型 */
@property (nonatomic, copy) NSString *constellation; /**<生肖星座 */
@property (nonatomic, copy) NSString *other_phone;
@property (nonatomic, copy) NSString *introduce; /**<自我介绍 */
@property (nonatomic, copy) NSString *myLabel;
@property (nonatomic, copy) NSArray<NSString *> *myLabels; /**<标签 */
@property (nonatomic, copy) NSArray<NSString *> *myPics; /**<我的照骗 */
@property (nonatomic, copy) NSString *sign; /**<个性签名 */
@property (nonatomic, copy) NSString *descStr;
@end

#pragma mark - 最新课程
@interface VULResponseCourseWareModel : NSObject
@property (nonatomic, copy) NSString *allowBackView; /**<是否回看 */
@property (nonatomic, copy) NSString *assistantTeacher; /**<助教老师 */
@property (nonatomic, copy) NSString *courseId; /**<课程id */
@property (nonatomic, copy) NSString *courseWareId; /**<课件id */
@property (nonatomic, copy) NSString *courseWareName; /**<课件名称 */
@property (nonatomic, copy) NSString *courseWareType; /**<课件类型，0视频课，1软件直播，2小班化直播，3手机直播，4设备直播，5伪直播，6PPT文档，7WO */
@property (nonatomic, copy) NSString *cover;/**<封面 */
@property (nonatomic, copy) NSString *createTime;/**<课件创建时间 */
@property (nonatomic, assign) BOOL isAssistantTeacher;/**<是否是助教 */
@property (nonatomic, copy) NSString *endTime; /**<结束时间 */
@property (nonatomic, copy) NSString *endTimeStamp; /**<结束时间戳 */
@property (nonatomic, copy) NSString *headPortrait; /**<教师头像 */
@property (nonatomic, copy) NSString *introduce; /**<课件介绍 */
@property (nonatomic, copy) NSString *loginName; /**<教师账号 */
@property (nonatomic, copy) NSString *realName; /**<教师姓名 */
@property (nonatomic, copy) NSString *sex; /**<教师性别 */
@property (nonatomic, copy) NSString *startState; /**<1正在上课，2今天未开课，3已结束，4今天以后开课 5回看*/
@property (nonatomic, copy) NSString *startTime; /**开始时间 */
@property (nonatomic, copy) NSString *startTimeStamp; /**开始时间戳 */
@property (nonatomic, copy) NSString *userId; /**课件创建者id */
@property (nonatomic, assign) double percentAvg; //学习进度
@property (nonatomic, assign) NSInteger playLength; /**< 视频时长 */
@property (nonatomic, copy) NSString *liveType; /**直播类型 */
@property (nonatomic, copy) NSString *sourceSuffix;

/// 是否开启弹幕
@property (nonatomic, assign) BOOL isOpenBarrage;
@property (nonatomic, assign) BOOL isCollectEnter;/**<是否是收藏夹进入 */

@property (nonatomic, copy) NSString *systemTimeStamp; /**<系统时间戳 */

@end

#pragma mark - 学习-我的单课-我购买的单课课件列表
@interface VULResponseMyCourseWareListModel : NSObject
@property (nonatomic, copy) NSString *cover;/**<封面 */
@property (nonatomic, copy) NSString *courseId;/**<课程id */
@property (nonatomic, copy) NSString *courseWareId;/**<课件id */
@property (nonatomic, copy) NSString *videoPicAddress;
@property (nonatomic, copy) NSString *courseWareName; /**课件名称 */
@property (nonatomic, copy) NSString *courseWareType; /**课件类型 */

@end

#pragma mark - 购买的课程列表
@class VULCourseListModel;
@interface VULResponseMyClassifyListModel : NSObject
@property (nonatomic, copy) NSString *classifyName;/**<分类名称 */
@property (nonatomic, copy) NSString *courseClassifyId;/**<分类id */
@property (nonatomic, strong) NSArray<VULCourseListModel *> *courseSubList; /**<课程列表 */
@end

@class VULTeacherListModel;
@interface VULCourseListModel : NSObject
@property (nonatomic, copy) NSString *courseId;/**<课程id */
@property (nonatomic, copy) NSString *courseName;/**<课程名 */
@property (nonatomic, copy) NSString *courseWareCount;/**<课件数 */
@property (nonatomic, copy) NSString *cover;/**<封面 */
@property (nonatomic, copy) NSString *introduce;/**<简介 */
@property (nonatomic, copy) NSString *playLengthCount;/**<课时数 */
@property (nonatomic, strong) NSArray<VULTeacherListModel *> *teacherList; /**<授课老师列表 */
@property (nonatomic, assign) BOOL isOnlineLive; /**<是否有直播课权限，0关闭，1开启*/
@end

@interface VULTeacherListModel : NSObject
@property (nonatomic, copy) NSString *headPortrait; /**<头像 */
@property (nonatomic, copy) NSString *realName; /**<名字 */
@property (nonatomic, copy) NSString *userId; /**<id */
@end

#pragma mark - 课程详情
@class VULCourseWareStatisticsModel;
@interface VULResponseCourseInfoModel : NSObject
@property (nonatomic, copy) NSString *childClassifyName;/**<子分类名称 */
@property (nonatomic, copy) NSString *courseId;/**<课程id */
@property (nonatomic, copy) NSString *courseName;/**<课程名字 */
@property (nonatomic, strong) NSArray<VULCourseWareStatisticsModel *> *courseWareStatistics; /**<课件类型统计 */
@property (nonatomic, copy) NSString *coursewareCount;/**<课件总数 */
@property (nonatomic, copy) NSString *cover;/**<封面 */
@property (nonatomic, copy) NSString *detail;/**<课件详情 */
@property (nonatomic, assign) BOOL isFavorite;/**<是否收藏 */
@property (nonatomic, assign) BOOL isOpen;/**<是否有问卷调查 */
@property (nonatomic, copy) NSString *mainClassifyName;/**<主分类名称 */
@property (nonatomic, copy) NSString *playLengthCount;/**<课时 */
@property (nonatomic, copy) NSString *popularityCount;/**<人气计数 */

@end

@interface VULCourseWareStatisticsModel : NSObject
@property (nonatomic, copy) NSString *count;/**<数量 */
@property (nonatomic, copy) NSString *rate;/**<占比 */
@property (nonatomic, copy) NSString *type;/**<课件类型 1视频 2直播 3文档 4其他 */

@end

#pragma mark - 课件收藏列表
@interface VULCollectCourseWareModel : NSObject
@property (nonatomic, copy) NSString *courseId; /**<课程id */
@property (nonatomic, copy) NSString *courseWareId; /**<课件id */
@property (nonatomic, copy) NSString *courseWareName; /**<课件名称 */
@property (nonatomic, copy) NSString *courseWareType; /**<课件类型 */
@property (nonatomic, copy) NSString *cover; /**<课件封面 */
@property (nonatomic, copy) NSString *detail; /**<详细介绍 */
@property (nonatomic, copy) NSString *introduce; /**<课件介绍 */
@property (nonatomic, copy) NSString *userId; /**<发布课件老师 uid */
/// 收藏时间
@property (nonatomic, assign) NSInteger collectTime;
@property (nonatomic, copy) NSString *teacherName;

@end

#pragma mark - 课程收藏列表
@class VULCollectCourseModel;
@interface VULResponseCollectCourseListModel : NSObject
@property (nonatomic, strong) NSArray<VULCollectCourseModel *> *list;
@end

@class VULTeacherModel;
@interface VULCollectCourseModel : NSObject
@property (nonatomic, copy) NSString *courseId; /**<课程id */
@property (nonatomic, copy) NSString *courseName; /**<课程名称 */
@property (nonatomic, copy) NSString *courseWareCount; /**<课件数 */
@property (nonatomic, copy) NSString *cover; /**<课程封面 */
@property (nonatomic, copy) NSString *detail; /**<详细介绍 */
@property (nonatomic, copy) NSString *introduce; /**<课程介绍 */
@property (nonatomic, strong) NSArray<VULTeacherModel *> *teacherList;
@end

@interface VULTeacherModel : NSObject
@property (nonatomic, copy) NSString *courseId; /**<课程id */
@property (nonatomic, copy) NSString *headPortrait; /**<头像 */
@property (nonatomic, copy) NSString *realName; /**<老师姓名 */
@property (nonatomic, copy) NSString *userId; /**<老师id */

@end

@interface VULAssistantTeacherModel : NSObject
@property (nonatomic, copy) NSString *img; /**<头像 */
@property (nonatomic, copy) NSString *name; /**<老师姓名 */
@property (nonatomic, copy) NSString *userId; /**<老师id */

@end

#pragma mark - 上传聊天图片 返回url
@interface VULResponseChatPicture : NSObject

@property (nonatomic, copy) NSString *checksum;
@property (nonatomic, assign) NSInteger commonSourceId;
@property (nonatomic, copy) NSString *sourceName;
@property (nonatomic, copy) NSString *sourcePath;
@property (nonatomic, assign) NSInteger state;

@end

#pragma mark - 课件详情
@interface VULResponseCourseWareInfoModel : NSObject
@property (nonatomic, copy) NSString *allowBackView; /**<直播回看 json格式(开始时间，结束时间)，空为不允许回看 */
@property (nonatomic, copy) NSString *assistantTeacher; /**<助教 json (教师id，姓名，头像) */
@property (nonatomic, copy) NSString *cameraSourceId; /**<摄像头资源id */
@property (nonatomic, copy) NSString *camPlay; /**<摄像头拉流 */
@property (nonatomic, copy) NSString *camPub; /**<摄像头推流 */
@property (nonatomic, copy) NSString *commonSourceId; /**<文件来源，文件管理表id */
@property (nonatomic, copy) NSString *courseDirectoryId; /**<所属目录章节 */
@property (nonatomic, copy) NSString *courseId; /**<课程id */
@property (nonatomic, copy) NSString *courseSource; /**<视频来源 默认0为自己新建，大于0为来源课件id */
@property (nonatomic, copy) NSString *courseWareId; /**<课件id */
@property (nonatomic, copy) NSString *courseWareName; /**<课件名称 */
/// 课件类型
/// 0视频课，1软件直播，2小班化直播，3手机直播，4设备直播，5伪直播，6PPT文档，7其他，8音频（根据视频类型与后缀名判断），9文档课，10图文课
/// 新直播 0云视频、1 教学直播、2小组会议、3手机直播、4 设备直播、 5 类教学直播
@property (nonatomic, copy) NSString *courseWareType;
/// 是否是新直播
@property (nonatomic, assign) BOOL isNewCourseWare;
@property (nonatomic, copy) NSString *cover; /**<封面 */
@property (nonatomic, copy) NSString *coverId; /**<封面id */
@property (nonatomic, copy) NSString *detail; /**<详细介绍 */
@property (nonatomic, copy) NSString *docPlay; /**<白板拉流地址 */
@property (nonatomic, copy) NSString *docPub; /**<白板推流地址 */
@property (nonatomic, copy) NSString *fileName; /**<文件名 */
@property (nonatomic, copy) NSString *fileSize; /**<文件大小 */
@property (nonatomic, copy) NSString *gmtCourseEnd; /**<结束时间 */
@property (nonatomic, copy) NSString *gmtCourseEndTimeStamp; /**<结束时间戳 */
@property (nonatomic, copy) NSString *gmtCourseStart; /**<开课时间 */
@property (nonatomic, copy) NSString *gmtCourseStartTimeStamp; /**<开课时间戳 */
@property (nonatomic, copy) NSString *gmtVideoFileUpload; /**<视频文件上传时间 */
@property (nonatomic, copy) NSString *groupChatRight; /**<群聊权限 00关闭，10仅开启群聊，01仅开启私聊，11全部开启 */
@property (nonatomic, copy) NSString *introduce; /**<课件简介 */
@property (nonatomic, assign) BOOL isAllowBackView; /**<是否可直播回看 */
@property (nonatomic, copy) NSString *isTranscoding; /**<是否转码成功 0未开始，1成功，-1失败 */
@property (nonatomic, assign) BOOL isViewRight; /**<是否有观看权限 */
@property (nonatomic, copy) NSString *liveId; /**<直播id */
@property (nonatomic, copy) NSString *liveType; /**直播类型 */
@property (nonatomic, copy) NSString *playLength; /**<课件播放时长 单位：s */
@property (nonatomic, assign) NSInteger liveLength; /**<文档课预设时间 单位：s */
@property (nonatomic, copy) NSString *previewUrl; /**<预览地址 可为视频的m3u8地址或图片的存储地址 */
@property (nonatomic, copy) NSString *price; /**<课件费用 */
@property (nonatomic, copy) NSString *pushUrl; /**<推流地址 */
@property (nonatomic, copy) NSString *realName; /**<真实姓名 */
@property (nonatomic, copy) NSString *rtmpPullUrl; /**<rtmp拉流地址 */
@property (nonatomic, copy) NSString *sourcePath; /**<文件路径 */
@property (nonatomic, copy) NSString *sourceSuffix; /**<文件后缀名 */
@property (nonatomic, copy) NSString *supplierType; /**<服务商类型，1阿里云，2网易云，3直播CDN */
@property (nonatomic, copy) NSString *thumb; /**<文档封面logo */
@property (nonatomic, copy) NSString *userId; /**<id */
@property (nonatomic, copy) NSString *videoFileAddress; /**<视频文件地址 mp4*/
@property (nonatomic, copy) NSString *videoM3u8FileAddress; /**<视频地址 m3u8 */
@property (nonatomic, copy) NSString *videoPicAddress; /**<视频截图地址 */
@property (nonatomic, copy) NSString *viewRight; /**<观看权限 json格式 (班级id，班级名称) */
@property (nonatomic, copy) NSString *timeStamp; /**<时间戳 */
@property (nonatomic, copy) NSString *notice;/**< 课程公告 */
@property (nonatomic, copy) NSString *appPreviewUrl; /**<APP上的文档预览，指doc转成pdf后的html5预览 */
@property (nonatomic, copy) NSString *isFavorite; /**<是否已收藏 */
@property (nonatomic, assign) BOOL favorite; /**<是否已收藏 */
@property (nonatomic, copy) NSString *systemStamp;

@property (nonatomic, copy) NSString *httpDocPlay; /**< flv 白板拉流路径 */
@property (nonatomic, copy) NSString *httpCamPlay; /**< flv 摄像头拉流路径 */
@property (nonatomic, copy) NSString *hlsDocPlay; /**< m3u8 白板拉流路径 */
@property (nonatomic, copy) NSString *hlsCamPlay; /**< m3u8 摄像头拉流路径 */

@property (nonatomic, copy) NSString *rtcPlay; /**<声网RTC拉流地址 */
@property (nonatomic, copy) NSString *rtcPub; /**<声网RTC推流地址 */
@property (nonatomic, copy) NSString *rtcType; /**<rtc线路，1网易，2声网 */

//modified 2019年08月05日 新的m3u8地址
@property (nonatomic, copy) NSString *picSourceUrl; /**<白板m3u8地址 */
@property (nonatomic, copy) NSString *cameraSourceUrl; /**<摄像头m3u8地址 */
@property (nonatomic, copy) NSString *sourceUrl; /**<普通m3u8地址 */
@property (nonatomic, copy) NSString *attSourceUrl; /**<附件m3u8地址 */

//modified 2019年10月10日 文档的预览地址
@property (nonatomic, copy) NSString *pptPreviewUrl;
@property (nonatomic, copy) NSString *downloadUrl;

//modified 2019年12月11日 伪直播指定的答疑教师
@property (nonatomic, copy) NSString *answerTeacherId; /**< 指定的答疑教师id*/
@property (nonatomic, copy) NSString *answerTeacherImg; /**< 指定的答疑教师头像*/
@property (nonatomic, copy) NSString *answerTeacherName; /**< 指定的答疑教师名字*/

//modified 2019年12月19日 是否开启答疑
@property (nonatomic, assign) BOOL isPraise; /**<是否点赞 */
@property (nonatomic, copy) NSString *praiseCount; /**<点赞数 */
@property (nonatomic, assign) BOOL isOpenStudent; /**<课件是否开启作品，0关闭1开启*/
@property (nonatomic, assign) BOOL isOpenAnswer; /**<课件是否开启答疑，0关闭1开启*/
@property (nonatomic, assign) BOOL isPay;

/// 直播带货开关
@property (nonatomic, assign) BOOL isLiveShop;
/// 是否开启弹幕
@property (nonatomic, assign) BOOL isOpenBarrage;
@property (nonatomic, copy) NSString *studyWarn; /**< 课中提醒*/
@property (nonatomic, copy) NSString *remindState; /**< 提醒状态，1.弹出 2、确认 0、未有记录*/
@property (nonatomic, copy) NSString *remindTime; /**< 提醒时间*/
/// 试看
@property (nonatomic, copy) NSString *videoAuditionSet;

/// 设备JSON
@property (nonatomic, copy) NSString *cameraDevice;

/// 设备列表
@property (nonatomic, strong) NSArray *cameraDeviceList;

@property (nonatomic, assign) BOOL isHIKCamera;
@property (nonatomic, assign) BOOL isAssistantTeacher;
@end

@interface VULResponseRTCChannelInfoModel : NSObject

@property (nonatomic, copy) NSString *appID;
@property (nonatomic, copy) NSString *channelID;
@property (nonatomic, copy) NSString *channelKey;
@property (nonatomic, copy) NSString *nonce;
@property (nonatomic, assign) BOOL recovered;
@property (nonatomic, copy) NSString *requestID;
@property (nonatomic, assign) NSInteger timestamp;

@end

@interface VULResponseRTCChannelTokenModel : NSObject

@property (nonatomic, copy) NSString *token;
@property (nonatomic, copy) NSString *userId;

@end

#pragma mark - 获取通知列表
@interface VULResponseNoticeInfoModel : NSObject
@property (nonatomic, copy) NSString *browseCount;/**< 浏览量 */
@property (nonatomic, copy) NSString *commonNoticeId;/**< 通知ID */
@property (nonatomic, copy) NSString *detail;/**< 通知内容 */

@property (nonatomic, copy) NSString *fileName;/**< 附件名称 */
@property (nonatomic, copy) NSString *filePath;/**< 附件路径 */
@property (nonatomic, copy) NSString *fileType;/**< 附件类型 */
@property (nonatomic, copy) NSString *fileSize;/**< 附件大小 */
@property (nonatomic, copy) NSString *gmtSend;/**< 发布时间 */

@property (nonatomic, copy) NSString *headPortrait;/**< 头像 */
@property (nonatomic, copy) NSString *isWindow;
@property (nonatomic, copy) NSString *questionnaireId;/**< 问卷的id */
@property (nonatomic, copy) NSString *realName;/**< 发通知的人的姓名 */
@property (nonatomic, copy) NSString *schoolId;/**< 网校id */
@property (nonatomic, copy) NSString *schoolName;/**< 网校名字 */
@property (nonatomic, copy) NSString *state;/**< 是否已读 0未读 1已读 */
@property (nonatomic, copy) NSString *title;/**< 标题 */
@property (nonatomic, copy) NSString *userId;/**< 发通知的人的id */

@end

#pragma mark- 根据id获取文件列表API
@interface VULResponseNotModel : NSObject

@end

/**
 课件列表课件model
 */
@interface VULResponseCourseWareListModel : NSObject

@property (nonatomic, copy) NSString *allowBackView;
@property (nonatomic, copy) NSString *assistantTeacher;/**< 助理 */
@property (nonatomic, assign) NSInteger attachmentCount;
@property (nonatomic, assign) NSInteger commentCount;
@property (nonatomic, assign) NSInteger courseDirectoryId;
@property (nonatomic, assign) NSInteger courseId;
@property (nonatomic, assign) NSInteger courseWareId;
@property (nonatomic, copy) NSString *courseWareName;/**< 课件名 */

/// 课件类型
/// 0视频课，1软件直播，2小班化直播，3手机直播，4设备直播，5伪直播，6PPT文档，7其他，8音频（根据视频类型与后缀名判断），9文档课，10图文课
/// 新直播 0云视频、1 教学直播、2小组会议、3手机直播、4 设备直播、 5 类教学直播
@property (nonatomic, copy) NSString *courseWareType;
@property (nonatomic, copy) NSString *cwType; //新的课件类型 0视频课 1软件直播
/// 是否是新直播
@property (nonatomic, assign) BOOL isNewCourseWare;
@property (nonatomic, copy) NSString *cover;/**< 课件封面 */
@property (nonatomic, copy) NSString *files;/**< 文件名 */
@property (nonatomic, assign) NSInteger gmtCourseEnd;
@property (nonatomic, assign) NSInteger gmtCourseEndTimeStamp;
@property (nonatomic, assign) NSInteger gmtCourseStart;
@property (nonatomic, assign) NSInteger gmtCourseStartTimeStamp;
@property (nonatomic, assign) NSInteger gmtCreate;
@property (nonatomic, assign) NSInteger startTimeStamp;
@property (nonatomic, copy) NSString *headPortrait;/**< 头像 */
@property (nonatomic, copy) NSString *introduce;/**< 介绍 */
@property (nonatomic, assign) BOOL isAllowBackView;
@property (nonatomic, assign) BOOL isAssistantTeacher;
@property (nonatomic, assign) NSInteger liveLength;/**< 直播时长 */
@property (nonatomic, assign) NSInteger livePlayRate;/**< 直播进度 */
@property (nonatomic, assign) NSInteger playLength; /**< 视频时长 */
@property (nonatomic, assign) NSInteger popularityCount;
@property (nonatomic, assign) NSInteger praiseCount;
@property (nonatomic, copy) NSString *price;
@property (nonatomic, copy) NSString *realName;
@property (nonatomic, assign) NSInteger schoolId;
@property (nonatomic, assign) NSInteger sort;
@property (nonatomic, copy) NSString *sourceSuffix; /**<文件后缀名 */
@property (nonatomic, copy) NSString *state;
@property (nonatomic, assign) double studyRate;
@property (nonatomic, assign) NSInteger timeStamp;
@property (nonatomic, assign) NSInteger userId;
@property (nonatomic, copy) NSString *videoAuditionSet;
@property (nonatomic, copy) NSString *videoPicAddress;
@property (nonatomic, copy) NSString *directoryName;//章节名
@property (nonatomic, copy) NSString *commonSourceId;
@property (nonatomic, assign) NSInteger isTranscoding;
@property (nonatomic, assign) NSInteger systemStamp;
@property (nonatomic, assign) NSInteger spend;

//教师端进度
@property (nonatomic, assign) double livePercentAvg; //直播进度
@property (nonatomic, assign) double percentAvg; //学习进度

/// 1正在上课，2今天未开课，5回看，3已结束，4今天以后开课
@property (nonatomic, assign) NSInteger startState;

@property (nonatomic, copy) NSString *pushUrl; /**<推流地址 */
@property (nonatomic, assign) NSInteger liveType;
@property (nonatomic, assign) BOOL isHIKCamera;

@property (nonatomic, assign) CGFloat cellHeight;

@end

#pragma mark - 直播推流地址等信息
@interface VULResponseBuildLiveUrlModel : NSObject

@property (nonatomic, copy) NSString *hlsPullUrl;
@property (nonatomic, copy) NSString *httpPullUrl;
@property (nonatomic, copy) NSString *liveId;
@property (nonatomic, copy) NSString *pushUrl;
@property (nonatomic, copy) NSString *rtmpPullUrl;

@end

#pragma mark - 问答、问卷数量model
@interface VULResponseRelationCountModel : NSObject

@property (nonatomic, assign) NSInteger answeringQuestionCount; //问答数
@property (nonatomic, assign) NSInteger commentCount; //评论数
@property (nonatomic, assign) NSInteger fileCount; //附件数
@property (nonatomic, assign) NSInteger questionnaireCount; //问卷数

@end

#pragma mark - 章节/单元model
@interface VULResponseCourseWareUnitModel : NSObject

@property (nonatomic, assign) NSInteger courseDirectoryId;
@property (nonatomic, assign) NSInteger courseWareCount;
@property (nonatomic, strong) NSMutableArray<VULResponseCourseWareListModel *> *courseWareList;
@property (nonatomic, copy) NSString *directoryName;//章节名
@property (nonatomic, copy) NSString *state;
@property (nonatomic, assign) NSInteger courseNum;//课件数
@property (nonatomic, assign) BOOL hideUnit;/**< 隐藏章节 */
@property (nonatomic, assign) NSInteger tag; //在tableview中section位置

@end

@class VULResponseClassifySelectionListModel;
@interface VULResponseClassifyListModel : NSObject
@property (nonatomic, copy) NSString *classifyName;/**<分类名称 */
@property (nonatomic, copy) NSString *classifySort;
@property (nonatomic, copy) NSString *courseClassifyId;/**<分类id */
@property (nonatomic, strong) NSMutableArray<VULResponseClassifySelectionListModel *> *courseList; /**<课程列表 */
@end

#pragma mark - 选课列表
@class VULTeacherListModel;
@interface VULResponseClassifySelectionListModel : NSObject
@property (nonatomic, copy) NSString *courseId;/**<课程id */
@property (nonatomic, copy) NSString *courseName;/**<课程名字 */
@property (nonatomic, copy) NSString *courseType;/**<课程类型 1普通课程，2课程包，3共享课程或课程包*/
@property (nonatomic, copy) NSString *coursewareCount;/**<课件数 */
@property (nonatomic, copy) NSString *cover;/**<课程封面 */
@property (nonatomic, copy) NSString *gmtCreate; //创建时间戳
@property (nonatomic, copy) NSString *introduce;/**<课程介绍 */
@property (nonatomic, copy) NSString *isFavorite;/**<是否收藏 */
@property (nonatomic, copy) NSString *mainClassify;
@property (nonatomic, copy) NSString *mainTeacher; /**< 主讲老师*/
@property (nonatomic, copy) NSString *popularityCount; /**<人气值 */
@property (nonatomic, copy) NSString *playLengthCount;/**<时长 */
@property (nonatomic, copy) NSString *studyCount;/**<学习数量 */
@property (nonatomic, copy) NSString *price;/**<价格 */

@property (nonatomic, strong) NSArray<VULTeacherListModel *> *teacherList; /**<授课老师列表 */

+ (VULResponseClassifySelectionListModel *)getClassifyModelWithInfoModel:(VULResponseCourseWareInfoModel *)infoModel;
@end

#pragma mark - 获取分类列表
@class VULSubClassifyModel;
@interface VULResponseClassifyModel : NSObject
@property (nonatomic, copy) NSString *courseCount; /**<课件数*/
@property (nonatomic, copy) NSString *courseClassifyId; /**< 主分类id*/
@property (nonatomic, copy) NSString *classifyName; /**< 主分类名*/
@property (nonatomic, copy) NSString *childrenCount; /**< 子分类数量*/
@property (nonatomic, strong) NSArray<VULSubClassifyModel *> *children; /**<课程列表 */
@end

@interface VULSubClassifyModel : NSObject
@property (nonatomic, copy) NSString *courseCount; /**<课件数*/
@property (nonatomic, copy) NSString *classifyName; /**< 子分类名*/
@property (nonatomic, copy) NSString *courseClassifyId; /**< 子分类id*/
@end

#pragma mark- 答疑列表
@interface VULResponseAnswerListModel : NSObject
@property (nonatomic, copy) NSString *approvalState; /**< id*/
@property (nonatomic, copy) NSString *classId; /**< 班级id*/
@property (nonatomic, copy) NSString *collection; /**< id*/
@property (nonatomic, copy) NSString *commentType; /**< id*/
@property (nonatomic, copy) NSString *commonCommentId; /**< id*/
@property (nonatomic, copy) NSString *commonHomepageInfoId; /**< id*/
@property (nonatomic, copy) NSString *commonSourceId; /**< id*/
@property (nonatomic, copy) NSString *courseId; /**< 课程id*/
@property (nonatomic, copy) NSString *courseName; /**< 课程名*/
@property (nonatomic, copy) NSString *courseWareId; /**< 课件id*/
@property (nonatomic, copy) NSString *detail; /**< 详情*/
@property (nonatomic, copy) NSString *detailOld; /**< id*/
@property (nonatomic, copy) NSString *fkCommonCommentId; /**< id*/
@property (nonatomic, copy) NSString *gmtCreate; /**< 生产问题时间戳*/
@property (nonatomic, copy) NSString *gmtModified; /**< 时间戳*/
@property (nonatomic, copy) NSString *headPortrait; /**< 头像*/
@property (nonatomic, copy) NSString *isBestAnswer; /**< 是否是最佳回答*/
@property (nonatomic, copy) NSString *nickname; /**< 昵称*/
@property (nonatomic, copy) NSString *platformId; /**< 平台id*/
@property (nonatomic, copy) NSString *popularityCount; /**< 高质量*/
@property (nonatomic, copy) NSString *praiseCount; /**< 赞的数量*/
@property (nonatomic, copy) NSString *questionState; /**< 问题状态*/
@property (nonatomic, copy) NSString *realName; /**< 实名*/
@property (nonatomic, copy) NSString *replyCount; /**< 回复数*/
@property (nonatomic, copy) NSString *schoolId; /**< 网校id*/
@property (nonatomic, copy) NSString *state; /**< 状态*/
@property (nonatomic, copy) NSString *title; /**< 标题*/
@property (nonatomic, copy) NSString *userId; /**< 用户id*/
@property (nonatomic, copy) NSString *userIp; /**< 用户ip*/
@property (nonatomic, copy) NSString *userLevel; /**< 用户等级*/
// 该Model对应的Cell高度
@property (nonatomic, assign) CGFloat cellHeight;

//modified 2019年08月06日 图片九宫格相关
@property (nonatomic, assign) CGFloat imageContainerHeight; /**<图片容器高度 */
@property (nonatomic, strong) NSArray *imageSourceArray; /**<图片资源array */

+ (VULResponseAnswerListModel *)getAnswerModelWithDic:(NSDictionary *)dic;

@end

#pragma mark- 我的课程列表 （答疑-提问时调用）
@interface VULResponseMyCourseModel : NSObject
@property (nonatomic, copy) NSString *childClassifyName; /**< 子类名*/
@property (nonatomic, copy) NSString *courseId; /**< 课程id*/
@property (nonatomic, copy) NSString *courseName; /**< 课程名*/
@property (nonatomic, copy) NSString *cover; /**< 封面*/
@property (nonatomic, copy) NSString *introduce; /**< 课程介绍*/
@property (nonatomic, copy) NSString *mainClassifyName; /**< 主类名*/

@end

#pragma mark- 根据课程id获得课件列表
@interface VULCourseWareModel : NSObject
@property (nonatomic, copy) NSString *courseWareId; /**< 课件id*/
@property (nonatomic, copy) NSString *courseWareName; /**< 课件名*/
@property (nonatomic, copy) NSString *courseWareType; /**< 课件类型*/
@property (nonatomic, copy) NSString *timeStamp; /**< 时间戳*/

@end

#pragma mark - 获取课程关联的所有教师列表
@interface VULCourseTeachersModel : NSObject
@property (nonatomic, copy) NSString *courseId; /**< 课件id*/
@property (nonatomic, copy) NSString *headPortrait; /**< 头像*/
@property (nonatomic, copy) NSString *nickname; /**< 昵称*/
@property (nonatomic, copy) NSString *realName; /**< 实名*/
@property (nonatomic, copy) NSString *sex; /**< 性别*/
@property (nonatomic, copy) NSString *teacherType; //教师类型，1关联教师，2助教
@property (nonatomic, copy) NSString *userId; /**< 用户id*/
@end

#pragma mark - 根据id获取文件列表API 附件
@interface VULResponseSourceModel : NSObject
@property (nonatomic, copy) NSString *courseWareId; /**< 课件id*/
@property (nonatomic, copy) NSString *downloadUrl; /**< 下载地址*/
@property (nonatomic, copy) NSString *gmtCreate; /**< 创建日期*/
@property (nonatomic, copy) NSString *h5Url; /**< h5播放地址*/
@property (nonatomic, assign) BOOL isH5;/**< 是否可以h5播放 0否，1是 */
@property (nonatomic, assign) BOOL isPreview;/**< 是否可以视频m3u8播放 0否，1是 */
@property (nonatomic, assign) BOOL isSwf;/**< 文档类是否可以播放swf 0否，1是 */
@property (nonatomic, copy) NSString *platformId; /**< 平台id*/
@property (nonatomic, copy) NSString *playLength; /**< 播放时长*/
@property (nonatomic, copy) NSString *previewUrl; /**< 视频m3u8播放地址*/
@property (nonatomic, copy) NSString *schoolId; /**< 网校id*/
@property (nonatomic, copy) NSString *sourceName; /**< 附件文件名*/
@property (nonatomic, copy) NSString *sourceId; /**< 附件id*/
@property (nonatomic, copy) NSString *sourceSize; /**< 文件大小*/
@property (nonatomic, copy) NSString *sourceSuffix; /**< 后缀名*/
@property (nonatomic, copy) NSString *swfUrl; /**< 文档类播放swf地址*/
@property (nonatomic, copy) NSString *originalSuffix; /**< 原始文件后缀名*/

@end

#pragma mark -购物车商品
@interface VULResponseShoppingCarModel : NSObject
@property (nonatomic, copy) NSString *address;
@property (nonatomic, copy) NSString *couponCount;
@property (nonatomic, copy) NSString *cover;
@property (nonatomic, copy) NSString *endTime;
@property (nonatomic, copy) NSString *gmtCreate;
@property (nonatomic, copy) NSString *goodsDetailId;
@property (nonatomic, copy) NSString *goodsId;
@property (nonatomic, copy) NSString *goodsName;
@property (nonatomic, copy) NSString *goodsNum;
@property (nonatomic, copy) NSString *goodsType;
@property (nonatomic, copy) NSString *goodsUrl;
@property (nonatomic, copy) NSString *introduce;
@property (nonatomic, copy) NSString *isBuy;
@property (nonatomic, copy) NSString *operateType;
@property (nonatomic, copy) NSString *price;
@property (nonatomic, copy) NSString *sourceSchoolCourseId;

@end

#pragma mark - 作品分类列表model
@interface VULResponseAllOpusClassifyModel : NSObject
@property (nonatomic, assign) NSInteger commentsClassifyId;
@property (nonatomic, assign) NSInteger opusCount;
@property (nonatomic, copy) NSString *classifyName;

@end

#pragma mark -学生作业列表model
@interface VULResponseHomeworkModel : NSObject
@property (nonatomic, copy) NSString *examId;
@property (nonatomic, copy) NSString *gmtEnd;
@property (nonatomic, copy) NSString *gmtStart;
@property (nonatomic, copy) NSString *state; //0待提交,1待批改,2被打回, 3已批改，4已逾期，99未开放
@property (nonatomic, copy) NSString *teacherNickName;
@property (nonatomic, copy) NSString *teacherRealName;
@property (nonatomic, copy) NSString *title; //标题
@property (nonatomic, copy) NSString *detail; //内容
@property (nonatomic, assign) BOOL hasAttachment; //是否有附件
@property (nonatomic, copy) NSString *systemTimeStamp; /**< 系统时间戳*/

@end

#pragma mark -学生作业详情
@class VULExamInfoModel,VULExamUserModel;
@interface VULResponseHomeworkDetailModel : NSObject
@property (nonatomic, strong) VULExamInfoModel *examInfo;
@property (nonatomic, strong) VULExamUserModel *examUser;
@end

//作业信息
@class VULExamImageListInfoModel,VULExamFileListInfoModel;
@interface VULExamInfoModel : NSObject
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *teacherNickName;
@property (nonatomic, copy) NSString *teacherRealName;
@property (nonatomic, copy) NSString *detail; //内容
@property (nonatomic, copy) NSString *examId;
@property (nonatomic, copy) NSString *gmtEnd;
@property (nonatomic, copy) NSString *gmtStart;
@property (nonatomic, copy) NSString *fileIds; //作业文件ID，多个以英文逗号分隔
@property (nonatomic, copy) NSString *imageIds; //作业图片ID，多个以英文逗号分隔
@property (nonatomic, strong) NSArray<VULExamFileListInfoModel *> *fileList; /**<作业文件数组（视频音频文档） */
@property (nonatomic, strong) NSArray<VULExamImageListInfoModel *> *imageList; /**<作业图片数组 */

@end

@interface VULExamFileListInfoModel : NSObject
@property (nonatomic, copy) NSString *documentPreviewUrl; //预览地址
@property (nonatomic, copy) NSString *fileId;
@property (nonatomic, copy) NSString *fileSize;
@property (nonatomic, copy) NSString *fileUrl; //文件URL（相对路径）
@property (nonatomic, copy) NSString *sourceName; //文件名
@property (nonatomic, copy) NSString *transCodingState; //视频转码状态：0转码中,1转码成功,2转码失败

@end

@interface VULExamImageListInfoModel : NSObject
@property (nonatomic, copy) NSString *imageId;
@property (nonatomic, copy) NSString *imageUrl; //图片URL（相对路径）
@property (nonatomic, copy) NSString *fileSize;
@property (nonatomic, copy) NSString *sourceName; //文件名

@end


//学生作答信息
@interface VULExamUserModel : NSObject
@property (nonatomic, copy) NSString *checkFileIds; //批改文件ID，多个以英文逗号分隔
@property (nonatomic, copy) NSString *checkImageIds; //批改图片ID，多个以英文逗号分隔
@property (nonatomic, copy) NSString *fileIds; //作答文件ID，多个以英文逗号分隔
@property (nonatomic, copy) NSString *imageIds; //作业图片ID，多个以英文逗号分隔

@property (nonatomic, strong) NSArray<VULExamFileListInfoModel *> *checkFileList; /**<批改文件 */
@property (nonatomic, strong) NSArray<VULExamImageListInfoModel *> *checkImageList; /**<作业图片数组 */
@property (nonatomic, strong) NSArray<VULExamFileListInfoModel *> *fileList; /**<作答文件 */
@property (nonatomic, strong) NSArray<VULExamImageListInfoModel *> *imageList; /**<作业图片数组 */


@property (nonatomic, copy) NSString *checkRemark; //作业打回备注
@property (nonatomic, copy) NSString *comment; //评语
@property (nonatomic, copy) NSString *detail; //内容
@property (nonatomic, copy) NSString *gmtCheck; /**< 最新批改时间戳*/
@property (nonatomic, copy) NSString *gmtCorrect; /**< 最新提交或订正时间戳*/
@property (nonatomic, copy) NSString *gmtSubmit; /**< 首次提交时间戳*/
@property (nonatomic, copy) NSString *state; //0待提交,1待批改,2被打回, 3已批改，4已逾期
@property (nonatomic, copy) NSString *userId;

@property (nonatomic, copy) NSString *userRealName;
@property (nonatomic, copy) NSString *gmtSubmitTs; /**< 首次提交时间戳*/
@property (nonatomic, copy) NSString *gmtCheckTs; /**< 最新批改时间戳*/
@property (nonatomic, copy) NSString *gmtCorrectTs; /**< 最新提交或订正时间戳*/

@end

#pragma mark - 获取商城分类列表
@class VULResponseSubShopClassifyModel;
@interface VULResponseShopClassifyModel : NSObject
@property (nonatomic, copy) NSString *checkParent;
@property (nonatomic, copy) NSString *shopClassifyId; /**< 分类id*/
@property (nonatomic, copy) NSString *classifyName; /**< 主分类名*/
@property (nonatomic, copy) NSString *shopCount;
@property (nonatomic, copy) NSString *classifySequence;
@property (nonatomic, strong) NSArray <VULResponseSubShopClassifyModel *>*children;
@end

@interface VULResponseSubShopClassifyModel : NSObject
@property (nonatomic, copy) NSString *shopCount;
@property (nonatomic, copy) NSString *classifyName; /**< 子分类名*/
@property (nonatomic, copy) NSString *shopClassifyId; /**< 子分类id*/
@property (nonatomic, copy) NSString *classifySequence;
@end

#pragma mark - 获取问卷列表model
@interface VULResponseQuestionnaireModel : NSObject
@property (nonatomic, copy) NSString *commonQuestionnaireId;
@property (nonatomic, copy) NSString *courseId;
@property (nonatomic, copy) NSString *courseWareId;
@property (nonatomic, copy) NSString *gmtClose;
@property (nonatomic, copy) NSString *gmtCreate;
@property (nonatomic, copy) NSString *gmtOpen;
@property (nonatomic, copy) NSString *headPortrait;
@property (nonatomic, copy) NSString *isAnonymous;
@property (nonatomic, copy) NSString *isDisplayStudentStatistics;
@property (nonatomic, copy) NSString *isModified;
@property (nonatomic, copy) NSString *isQuestion;
@property (nonatomic, copy) NSString *persent;
@property (nonatomic, copy) NSString *personCount;
@property (nonatomic, copy) NSString *personTotal;
@property (nonatomic, copy) NSString *questionnaireType;
@property (nonatomic, copy) NSString *realName;
@property (nonatomic, copy) NSString *state;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *systemStamp;

@end

#pragma mark - 获取资讯分类列表
@class VULSubHomepageListClassifyModel;
@interface VULHomepageListClassifyModel : NSObject

@property (nonatomic, copy) NSString *commonHomepageInfoTypeId;
@property (nonatomic, copy) NSString *schoolId;
@property (nonatomic, copy) NSString *typeName;
@property (nonatomic, copy) NSString *typeSequence;
@property (nonatomic, strong) NSArray <VULSubHomepageListClassifyModel *> *children;

@end

@interface VULSubHomepageListClassifyModel : NSObject
@property (nonatomic, copy) NSString *commonHomepageInfoTypeId;
@property (nonatomic, copy) NSString *schoolId;
@property (nonatomic, copy) NSString *typeName;
@property (nonatomic, copy) NSString *typeSequence;
@end

#pragma mark - 获取商城商品列表
@interface VULResponseShopCommodityModel : NSObject
@property (nonatomic, copy) NSString *address;
@property (nonatomic, copy) NSString *classifyName;
@property (nonatomic, copy) NSString *approvalState;
@property (nonatomic, copy) NSString *cover;
@property (nonatomic, copy) NSString *coverList;
@property (nonatomic, copy) NSString *isVipDiscount;
@property (nonatomic, copy) NSString *marketPrice;
@property (nonatomic, copy) NSString *orderCount;
@property (nonatomic, copy) NSString *paymentType;
@property (nonatomic, copy) NSString *platformId;
@property (nonatomic, copy) NSString *popularityCount;
@property (nonatomic, copy) NSString *price;
@property (nonatomic, copy) NSString *schoolId;
@property (nonatomic, copy) NSString *score;
@property (nonatomic, copy) NSString *shopClassifyId;
@property (nonatomic, copy) NSString *shopDiscount;
@property (nonatomic, copy) NSString *shopId;
@property (nonatomic, copy) NSString *shopName;
@property (nonatomic, copy) NSString *shopType;
@property (nonatomic, copy) NSString *sort;
@property (nonatomic, copy) NSString *standardsPrice;
@property (nonatomic, copy) NSString *state;
@property (nonatomic, copy) NSString *videoCover;
@property (nonatomic, assign) NSInteger activityType;//活动类型, 1拼团, 2砍价, 3秒杀
@property (nonatomic, assign) NSInteger groupLimit;  //成团人数
@property (nonatomic, assign) CGFloat groupPrice;
@property (nonatomic, assign) BOOL isSelect;   //拼团价格
//拼团价格
@property (nonatomic, assign) CGFloat bargainPrice;//砍价的底价
@property (nonatomic, assign) CGFloat secKillPrice;//最小的秒杀价。单位：分

@property (nonatomic, assign) NSInteger stock;

@end


#pragma mark - 多个问卷id查询多个问卷
@interface VULResponseQuestionairesModel : NSObject
@property (nonatomic, copy) NSString *commonQuestionnaireId; /**< 问卷id*/
@property (nonatomic, copy) NSString *courseId; /**< 课程id*/
@property (nonatomic, copy) NSString *courseWareId; /**< 课件id*/
@property (nonatomic, copy) NSString *gmtClose; /**< 结束时间*/
@property (nonatomic, copy) NSString *gmtCreate; /**< 创建时间*/
@property (nonatomic, copy) NSString *gmtOpen; /**< 开放时间*/
@property (nonatomic, copy) NSString *headPortrait; /**< 发布人头像*/
@property (nonatomic, copy) NSString *isAnonymous; /**< 是否匿名调查  1是 0否*/
@property (nonatomic, copy) NSString *isDisplayStudentStatistics; /**< 是否可查看统计 1是 0否*/
@property (nonatomic, copy) NSString *isQuestion; /**< 是否作答过 1是 0否*/
@property (nonatomic, copy) NSString *questionnaireType; /**< 问卷类型 1常规问卷，2听课反馈，3课程开通，4课间问卷，5首次登录，6通知回执*/
@property (nonatomic, copy) NSString *realName; /**< 发布人姓名*/
@property (nonatomic, copy) NSString *title; /**< 问卷标题*/
@property (nonatomic, copy) NSString *systemTimeStamp; /**< 系统时间戳*/

@end

#pragma mark - 网校基本信息
@interface VULSchoolorPlatformInfoModel : NSObject
@property (nonatomic, assign) NSInteger isLoginwindowRegister; //禁止注册，0关闭，1启用
@property (nonatomic, assign) NSInteger isOnlyDisplayOwnCourseware;
@property (nonatomic, copy) NSString *refereeState;
@property (nonatomic, copy) NSString *subhead;
@property (nonatomic, assign) NSInteger pageSize;
@property (nonatomic, assign) NSInteger isRegisterPhone; //新用户注册必须认证手机号，0关闭，1启用
@property (nonatomic, assign) NSInteger isRegisterSourceSchool; //注册时是否需要填写来源学校名称 0否1是
@property (nonatomic, assign) NSInteger isRegisterSourceGrade; //注册时是否需要填写来源年级
@property (nonatomic, assign) NSInteger isRegisterSourceClass; //注册时是否需要填写来源班级
@property (nonatomic, assign) NSInteger isRegisterRealName; //注册时需要填写真实姓名，0关闭，1启用
@property (nonatomic, copy) NSString *thirdLoginType;
@property (nonatomic, copy) NSString *type;
@property (nonatomic, assign) NSInteger isDisplayAnsweringQuestionGrade;
@property (nonatomic, assign) NSInteger isCustomBrowser;
@property (nonatomic, assign) NSInteger isDisplaySpecialLabel;
@property (nonatomic, assign) NSInteger isOtheruserLoginStudy;
@property (nonatomic, assign) NSInteger loginLimit;
@property (nonatomic, copy) NSString *logo;
@property (nonatomic, assign) NSInteger isHotCourse;
@property (nonatomic, assign) NSInteger id;
@property (nonatomic, assign) NSInteger passwordMin;
@property (nonatomic, assign) NSInteger teacherAnswer;
@property (nonatomic, copy) NSString *licence;
@property (nonatomic, copy) NSString *schoolRegisterProtocol;
@property (nonatomic, assign) NSInteger otherSettingId;
@property (nonatomic, copy) NSString *name;
@property (nonatomic, assign) NSInteger commentIntervalTime;
@property (nonatomic, copy) NSString *detail;
@property (nonatomic, assign) NSInteger isCourseSaleSmsOpen;
@property (nonatomic, assign) NSInteger isLoginnameLogin;
@property (nonatomic, assign) NSInteger gmtModified;
@property (nonatomic, copy) NSString *levelState;
@property (nonatomic, copy) NSString *loginType;
@property (nonatomic, assign) NSInteger isNewDynamic;
@property (nonatomic, copy) NSString *srProtocol;
@property (nonatomic, assign) NSInteger loginNameMin;
@property (nonatomic, copy) NSString *customBrowserExceptionStudent;
@property (nonatomic, assign) NSInteger isSendScoreOnekey;
@property (nonatomic, assign) NSInteger isOtheruserBuyCourse;
@property (nonatomic, assign) NSInteger schoolId;
@property (nonatomic, copy) NSString *courseSaleSmsTime;
@property (nonatomic, assign) NSInteger isStudyCardOpen;
@property (nonatomic, copy) NSString *schoolName;
@property (nonatomic, copy) NSString *schoolType; //'网校类型，1学校，2公司'
@property (nonatomic, copy) NSString *organizationType; //1-学校；2-企业；3-政府；4-机构；5-高校

@property (nonatomic, assign) NSInteger isSmsLogin;
@property (nonatomic, assign) NSInteger isValidateWeakPassword;
@property (nonatomic, copy) NSString *introduction;
@property (nonatomic, assign) NSInteger postIntervalTime;
@property (nonatomic, copy) NSString *address;
@property (nonatomic, assign) NSInteger isThirdLogin;
@property (nonatomic, copy) NSString *platformTopLevelDomain;
@property (nonatomic, assign) NSInteger teacherAskMessage;
@property (nonatomic, assign) NSInteger platformId;
@property (nonatomic, assign) NSInteger fromCard;
@property (nonatomic, assign) NSInteger gmtCreate;
@property (nonatomic, assign) NSInteger isNewSignUp;
@property (nonatomic, assign) NSInteger isOpenSchoolRegisterProtocol;
@property (nonatomic, copy) NSString *contactNumber;

@end

//学校信息返回model
@class VULThirdSchoolInfoModel;
@interface VULResponseSchoolInfoModel : NSObject

@property (nonatomic, copy) NSString *cover;
@property (nonatomic, assign) NSInteger createTimeStamp; /**< 创建时间浩渺 */
@property (nonatomic, assign) NSInteger gmtCreate; /**< 创建时间 毫秒 */
@property (nonatomic, copy) NSString *headPortrait; /**< 网校头像 */
@property (nonatomic, copy) NSString *introduce; /**< 介绍 */
@property (nonatomic, assign) NSInteger isHttps;
@property (nonatomic, copy) NSString *logo;
@property (nonatomic, assign) NSInteger schoolId;
@property (nonatomic, copy) NSString *schoolName;
@property (nonatomic, copy) NSString *secondLevelDomain;
@property (nonatomic, copy) NSString *topLevelDomain;
@property (nonatomic, copy) NSString *contactNumber; //网校联系方式
@property (nonatomic, assign) BOOL isRegister;
@property (nonatomic, copy) NSString * address;
@property (nonatomic, assign) NSInteger commentIntervalTime;
@property (nonatomic, assign) NSInteger courseOnlyBalance;
@property (nonatomic, copy) NSString * courseSaleSmsTime;
@property (nonatomic, copy) NSString * customBrowserExceptionStudent;
@property (nonatomic, copy) NSString * detail;
@property (nonatomic, assign) NSInteger gmtModified;
@property (nonatomic, assign) NSInteger idField;
@property (nonatomic, copy) NSString * introduction;
@property (nonatomic, assign) NSInteger isCourseSaleSmsOpen;
@property (nonatomic, assign) NSInteger isCustomBrowser;
@property (nonatomic, assign) NSInteger isCustomWareSort;
@property (nonatomic, assign) NSInteger isDisplayAnsweringQuestionGrade;
@property (nonatomic, assign) NSInteger isDisplaySpecialLabel;
@property (nonatomic, assign) NSInteger isHotCourse;
@property (nonatomic, assign) NSInteger isLoginnameLogin;
@property (nonatomic, assign) NSInteger isLoginwindowRegister;
@property (nonatomic, assign) NSInteger isNewDynamic;
@property (nonatomic, assign) NSInteger isNewSignUp;
@property (nonatomic, assign) NSInteger isOnlyDisplayOwnCourseware;
@property (nonatomic, assign) NSInteger isOpenAnswer;
@property (nonatomic, assign) NSInteger isOpenSchoolRegisterProtocol;
@property (nonatomic, assign) NSInteger isOtheruserBuyCourse;
@property (nonatomic, assign) NSInteger isOtheruserLoginStudy;
@property (nonatomic, assign) NSInteger isRegisterPhone;
@property (nonatomic, assign) NSInteger isRegisterRealName;
@property (nonatomic, assign) NSInteger isRegisterSourceClass;
@property (nonatomic, assign) NSInteger isRegisterSourceGrade;
@property (nonatomic, assign) NSInteger isRegisterSourceSchool;
@property (nonatomic, assign) NSInteger isSendScoreOnekey;
@property (nonatomic, assign) NSInteger isSmsLogin;
@property (nonatomic, assign) NSInteger organizationType;
@property (nonatomic, assign) NSInteger isStudyCardOpen;
@property (nonatomic, assign) NSInteger isThirdLogin;
@property (nonatomic, assign) NSInteger isValidateWeakPassword;
@property (nonatomic, copy) NSString * levelState;
@property (nonatomic, copy) NSString * licence;
@property (nonatomic, assign) NSInteger loginLimit;
@property (nonatomic, assign) NSInteger loginNameMin;
@property (nonatomic, copy) NSString * loginType;
@property (nonatomic, copy) NSString * name;
@property (nonatomic, assign) NSInteger otherSettingId;
@property (nonatomic, assign) NSInteger pageSize;
@property (nonatomic, assign) NSInteger passwordMin;
@property (nonatomic, assign) NSInteger platformId;
@property (nonatomic, copy) NSString * platformTopLevelDomain;
@property (nonatomic, assign) NSInteger postIntervalTime;
@property (nonatomic, copy) NSString * refereeState;
@property (nonatomic, copy) NSString * schoolRegisterProtocol;
@property (nonatomic, copy) NSString * schoolType;
@property (nonatomic, copy) NSString * subhead;
@property (nonatomic, assign) NSInteger teacherAnswer;
@property (nonatomic, assign) NSInteger teacherAskMessage;
@property (nonatomic, assign) NSInteger fromCard;

@property (nonatomic, copy) NSString * thirdLoginType;

///     1公司2平台3网校
@property (nonatomic, copy) NSString * type;

+ (VULResponseSchoolInfoModel *)translateWithThirdModel:(VULThirdSchoolInfoModel *)thirdModel;

@end

//第三方登录返回的model
@interface VULThirdSchoolInfoModel : NSObject
@property (nonatomic, copy) NSString *cover;
@property (nonatomic, assign) NSInteger createTimeStamp; /**< 创建时间浩渺 */
@property (nonatomic, assign) NSInteger gmtCreate; /**< 创建时间 毫秒 */
@property (nonatomic, copy) NSString *headPortrait; /**< 网校头像 */
@property (nonatomic, copy) NSString *introduce; /**< 介绍 */
@property (nonatomic, assign) NSInteger https;
@property (nonatomic, copy) NSString *logoURL;
@property (nonatomic, assign) NSInteger schoolId;
@property (nonatomic, copy) NSString *schoolName;
@property (nonatomic, copy) NSString *secondLevelDomain;
@property (nonatomic, copy) NSString *topLevelDomain;

@end

//答疑 问题model 参数
@interface VULResponseAskInfoModel : NSObject
@property (nonatomic, copy) NSString *approvalState;
@property (nonatomic, assign) NSInteger classId; /**< 班级id */
@property (nonatomic, assign) BOOL collection; /**< 是否关注 */
@property (nonatomic, copy) NSString *commentType; /**< 类型，1互动答疑，2课件评论，3资讯评论 */
@property (nonatomic, assign) NSInteger commonCommentId; /**< 评论主键 */
@property (nonatomic, assign) NSInteger commonHomepageInfoId; /**< 资讯id */
@property (nonatomic, assign) NSInteger commonSourceId; /**< 附件 */
@property (nonatomic, assign) NSInteger courseId; /**< 课程id */
@property (nonatomic, copy) NSString *courseName; /**< 课程名 */
@property (nonatomic, assign) NSInteger courseWareId; /**< 课件id */
@property (nonatomic, copy) NSString *courseWareName; /**< 课件名 */
@property (nonatomic, copy) NSString *detail; /**< 评论或回复，过滤后的内容 */
@property (nonatomic, copy) NSString *detailOld; /**< 评论或回复，过滤前的内容 */
@property (nonatomic, assign) NSInteger fkCommonCommentId; /**< 评论父id */
@property (nonatomic, assign) NSInteger gmtCreate; /**< 创建时间 */
@property (nonatomic, assign) NSInteger gmtModified; /**< 修改时间 */
@property (nonatomic, copy) NSString *headPortrait; /**< 用户头像(urlstr) */
@property (nonatomic, assign) NSInteger isBestAnswer; /**< 是否最佳答案 1.是 2.否 */
@property (nonatomic, copy) NSString *nickname; /**< 昵称 */
@property (nonatomic, assign) NSInteger platformId; /**< 所属平台 */
@property (nonatomic, assign) NSInteger popularityCount; /**< 人气，浏览量计数器 */
@property (nonatomic, assign) NSInteger praiseCount; /**< 点赞计数器 */
@property (nonatomic, copy) NSString *questionState; /**< 问题状态，1.待回复，2.待解决，3.已解决 */
@property (nonatomic, copy) NSString *realName; /**< 用户真实姓名 */
@property (nonatomic, assign) NSInteger replyCount; /**< 回复计数器 */
@property (nonatomic, assign) NSInteger schoolId; /**< 网校id */
@property (nonatomic, copy) NSString *state; /**< 状态，1正常，8屏蔽，3删除 */
@property (nonatomic, copy) NSString *title; /**< 提问标题 */
@property (nonatomic, assign) NSInteger userId; /**< 用户id */
@property (nonatomic, copy) NSString *userIp; /**< 用户ip */
@property (nonatomic, assign) NSInteger userLevel; /**< 用户等级，几颗星 */

// 该Model对应的Cell高度
@property (nonatomic, assign) CGFloat cellHeight;
@property (nonatomic, assign) BOOL edit; //是否可编辑
@property (nonatomic, assign) BOOL selected; //是否已选中

//modified 2019年08月06日 图片九宫格相关
@property (nonatomic, assign) CGFloat imageContainerHeight; /**<图片容器高度 */
@property (nonatomic, strong) NSArray *imageSourceArray; /**<图片资源array */

+ (VULResponseAskInfoModel *)getAskModelWithDic:(NSDictionary *)dic;

@end

//评论 其他的的回答
@interface VULResponseReplyModel : NSObject

@property (nonatomic, assign) NSInteger collection; /**< 收藏数 */
@property (nonatomic, assign) NSNumber *commonCommentId; /**< 回复id */
@property (nonatomic, assign) NSInteger fkCommonCommentId;/**< 答案id */
@property (nonatomic, copy) NSString *detail; /**< 回复内容 */
@property (nonatomic, assign) NSInteger gmtCreate; /**< 创建时间 */
@property (nonatomic, copy) NSString *headPortrait; /**< 头像 */
@property (nonatomic, assign) NSInteger isPraise; /**< 是否点赞 */
@property (nonatomic, assign) NSInteger praiseCount; /**< 点赞计数器 */
@property (nonatomic, copy) NSString *nickname; /**< 昵称 */
@property (nonatomic, assign) NSInteger popularityCount;
@property (nonatomic, copy) NSString *realName;
@property (nonatomic, assign) NSInteger replyCount;

@property (nonatomic, assign) CGFloat rowHeight; /**< 行高 */

@end

//回答问题
@interface VULResponseAnswerModel : NSObject

@property (nonatomic, strong) NSString *approvalState;
@property (nonatomic, assign) NSInteger classId; /**< 班级id */
@property (nonatomic, assign) BOOL collection; /**< 收藏 */
@property (nonatomic, strong) NSNumber *commonCommentId; /**< 评论主键 */
@property (nonatomic, assign) NSInteger commonHomepageInfoId; /**< 资讯id */
@property (nonatomic, assign) NSInteger commonSourceId; /**< 附件 */
@property (nonatomic, assign) NSInteger courseId; /**< 课程id */
@property (nonatomic, assign) NSInteger courseWareId; /**< 课件id，评论哪个课件 */
@property (nonatomic, copy) NSString *detail; /**< 评论或回复，过滤后的内容     */
@property (nonatomic, assign) NSInteger fkCommonCommentId; /**< 评论父id */
@property (nonatomic, copy) NSString *headPortrait; /**< 用户头像 */
@property (nonatomic, assign) NSInteger gmtCreate; /**< 创建时间 */
@property (nonatomic, assign) NSInteger isBestAnswer; /**< 是否最佳答案 */
@property (nonatomic, assign) NSInteger isPraise; /**< 1已点赞，可能为undefined */
@property (nonatomic, copy) NSString *nickname; /**< 昵称 */
@property (nonatomic, assign) NSInteger platformId; /**< 平台id */
@property (nonatomic, assign) NSInteger popularityCount; /**< 人气，浏览量计数器     */
@property (nonatomic, assign) NSInteger praiseCount; /**< 点赞计数器     */
@property (nonatomic, copy) NSString *questionState; /**< 问题状态，1.待回复，2.待解决，3.已解决     */
@property (nonatomic, copy) NSString *realName; /**< 真实姓名     */
@property (nonatomic, assign) NSInteger replyCount; /**< 回复计数器     */
@property (nonatomic, strong) NSArray<VULResponseReplyModel *> *replyList; /**< 回复的的数据（每个答案只展示两条）     */
@property (nonatomic, assign) NSInteger schoolId; /**< 学习id     */
@property (nonatomic, copy) NSString *state; /**<  */
@property (nonatomic, copy) NSString *title; /**<  */
@property (nonatomic, assign) NSInteger userId; /**<  */

@property (nonatomic, assign) CGFloat rowHeight; /**< 行高 */

@property (nonatomic, assign) CGFloat listHeight;

@end

#pragma mark - 钱包金额 余额之类信息
@interface VULResponseWalletModel : NSObject

@property (nonatomic, assign) double balance;
@property (nonatomic, assign) double freezingAmount;
@property (nonatomic, copy) NSString *paymentSystemBank;
@property (nonatomic, assign) NSInteger schoolId;
@property (nonatomic, assign) NSInteger userId;

@end

#pragma mark - 积分历史
@interface VULResponseIntegralInfoModel : NSObject

@property (nonatomic, copy) NSString *detail;
@property (nonatomic, assign) NSInteger gmtCreate;
@property (nonatomic, copy) NSString *headPortrait;
@property (nonatomic, assign) NSInteger isReward;
@property (nonatomic, copy) NSString *loginName;
@property (nonatomic, assign) NSInteger platformId;
@property (nonatomic, copy) NSString *realName;
@property (nonatomic, assign) NSInteger ruleId;
@property (nonatomic, assign) NSInteger schoolId;
@property (nonatomic, copy) NSString *score;
@property (nonatomic, assign) NSInteger scoreRecordId;

/// 1签到，2开通课程，3第一次学习，4完成学习，5完成作业，6提问，7回答，8填写问卷，9评论，10手工增加，11手工扣除, 12抽奖消耗, 13抽奖获得, 14悬赏，15消费
@property (nonatomic, assign) NSInteger scoreType;
@property (nonatomic, strong) NSNumber *sex;
@property (nonatomic, assign) NSInteger userId;

@end

#pragma mark - 查看通知详情（主要处理回执）
@interface VULResponseNoticeDetailModel : NSObject
//业务类型，0普通，1审核提醒，2商品发货提醒，3开课提醒
@property (nonatomic, copy) NSString *businessType;
@property (nonatomic, copy) NSString *businessId; /**<业务id  （当businessType为3开课提醒时，则值为课件ID）*/
@property (nonatomic, assign) NSInteger browseCount;
@property (nonatomic, assign) NSInteger commonNoticeId;
@property (nonatomic, copy) NSString *detail;
@property (nonatomic, copy) NSString *detailState;
@property (nonatomic, copy) NSString *fileName;
@property (nonatomic, copy) NSString *filePath;
@property (nonatomic, strong) NSNumber *fileSize;
@property (nonatomic, copy) NSString *fileType;
@property (nonatomic, assign) NSInteger gmtCreate;
@property (nonatomic, assign) NSInteger gmtModified;
@property (nonatomic, assign) NSInteger gmtSend;
@property (nonatomic, copy) NSString *headPortrait;
@property (nonatomic, assign) NSInteger isAnswered;
@property (nonatomic, assign) NSInteger isWindow;
@property (nonatomic, assign) NSInteger questionnaireId;
@property (nonatomic, copy) NSString *realName;
@property (nonatomic, assign) NSInteger schoolId;
@property (nonatomic, copy) NSString *state;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, assign) NSInteger userId;
@property (nonatomic, copy) NSString *userIp;
@property (nonatomic, copy) NSString *previewUrl; /**<附件下载地址 */

@end

#pragma mark- 试卷分类列表model
@interface VULHomeworkClassifyListModel : NSObject
@property (nonatomic, copy) NSString *classifyName; /**< 分类名称*/
@property (nonatomic, copy) NSString *gmtCreate; /**< 创建时间*/
@property (nonatomic, copy) NSString *gmtModified; /**< 修改时间*/
@property (nonatomic, copy) NSString *homeworkClassifyId; /**< 分类id*/
@property (nonatomic, copy) NSString *homeworkCount; /**< 作业数量*/
@property (nonatomic, copy) NSString *schoolId; /**< 网校id*/
@property (nonatomic, copy) NSString *state; /**< 状态: 1正常 、3删除 */
@end

#pragma mark- 学生试卷列表model
@interface VULHomeworkListModel : NSObject
@property (nonatomic, copy) NSString *answerLimitTime; /**< 回答限时时间,分钟*/
@property (nonatomic, copy) NSString *answerShowTime; /**< 答案开放时间*/
@property (nonatomic, copy) NSString *checkPercent; /**< 试卷的批阅进度。0-1 ，1就是100%批阅完了*/
@property (nonatomic, copy) NSString *courseId; /**< 关联课程id*/
@property (nonatomic, copy) NSString *courseName; /**< 关联课程名*/
@property (nonatomic, copy) NSString *courseWareId; /**< 关联课件id*/
@property (nonatomic, copy) NSString *courseWareName; /**< 关联课件名*/
@property (nonatomic, copy) NSString *gmtClose; /**< 作业结束时间*/
@property (nonatomic, copy) NSString *gmtOpen; /**< 作业开始时间*/
@property (nonatomic, copy) NSString *homeworkClassifyId; /**< 作业分类id*/
@property (nonatomic, copy) NSString *homeworkId; /**< 作业id*/
@property (nonatomic, copy) NSString *homeworkName; /**< 作业名称*/
@property (nonatomic, copy) NSString *homeworkSequence; /**< 作业序列号，大于12位的就是补考的*/
@property (nonatomic, copy) NSString *homeworkType; /**< 作业类型，1普通作业，2智能作业*/
@property (nonatomic, copy) NSString *isScoreOneself; /**< 是否允许学生自己批阅， 0否1是*/
@property (nonatomic, copy) NSString *isWjBrowser; /**< 是否使用无极防作弊浏览器。0否1是*/
@property (nonatomic, copy) NSString *questionsAuthor; /**< 出题者*/
@property (nonatomic, copy) NSString *questionsAuthorId; /**< 出题者id*/
@property (nonatomic, copy) NSString *score; /**< 试卷总分*/
@property (nonatomic, copy) NSString *state; /**< 学生答题状态0未开始，1进行中，2已提交，3已批阅*/
@property (nonatomic, copy) NSString *stuScore; /**< 学生答题总分*/
@property (nonatomic, copy) NSString *userId; /**< 学生id*/
@property (nonatomic, copy) NSString *systemTimeStamp; /**< 系统时间戳*/

//modified 2019年09月30日 试卷可重做
@property (nonatomic, copy) NSString *isRepeatdo; /**< 是否能重做。0否1是*/
@property (nonatomic, copy) NSString *redoRemain; /**< 剩余可以重做的次数*/
@property (nonatomic, copy) NSString *redoStatus; /**< 是否能重做。0否1是*/
@property (nonatomic, copy) NSString *repeatdoNum; /**<当前试卷可以重做的次数*/

//modified 2020年03月16日 新版作业
@property (nonatomic, copy) NSString *questionShowWay; /**< 题目展现形式，1展开式(原题目形式)，2单题式 */
@property (nonatomic, copy) NSString *answerState; //通知详情跳转用

@end

#pragma mark - 学生端获取积分设置的配置 -modified 2019年12月13日
@interface VULResponseGetScoreConfigModel : NSObject
@property (nonatomic, copy) NSString *isOpenCredit;  /**<是否开启学分系统 0关 1开 */
@property (nonatomic, copy) NSString *isOpenScore;  /**<是否开启积分系统 0关 1开 */
@property (nonatomic, copy) NSString *scoreName;  /**<积分系统名称 */

@end

#pragma mark - 首页轮播图
@interface VULResponseGetSlideShowModel : NSObject
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *url;
@property (nonatomic, copy) NSString *slideId;

@end

#pragma mark - 学习排名（TOP99)
@interface VULResponseLearnRankModel : NSObject
@property (nonatomic, copy) NSString *headPortrait;
@property (nonatomic, copy) NSString *loginName;
@property (nonatomic, copy) NSString *nickName;
@property (nonatomic, copy) NSString *rank;
@property (nonatomic, copy) NSString *realName;
@property (nonatomic, copy) NSString *studyTimeLength;
@property (nonatomic, copy) NSString *userId;

@end

#pragma mark -学习页统计数：
@interface VULResponseStudyPageStatisticModel : NSObject
@property (nonatomic, copy) NSString *homeworkCount; //最新作业数
@property (nonatomic, copy) NSString *replyCount; //最新答疑回复数
@property (nonatomic, copy) NSString *unreadNoticeCount; //未读通知
@property (nonatomic, copy) NSString *examCount; //最新作业数

@end

#pragma mark -获取热搜词
@interface VULResponseHotSearchModel : NSObject
@property (nonatomic, copy) NSString *detail; //热词
@property (nonatomic, copy) NSString *sort; //排名

@end

#pragma mark - 教师直播页面各个操作链接
@interface VULResponseTeacherOperateModel : NSObject
@property (nonatomic, copy) NSString *answersheet; /**< 答题卡地址 */
@property (nonatomic, copy) NSString *ask; /**< 答疑地址 */
@property (nonatomic, copy) NSString *attachment; /**< 附件 */
@property (nonatomic, copy) NSString *attachment_push; /**< 附件推送  */
@property (nonatomic, copy) NSString *camplay; /**< 摄像头拉流  */
@property (nonatomic, copy) NSString *campub; /**< 摄像头推流  */
@property (nonatomic, copy) NSString *checkin; /**< 签到 */
@property (nonatomic, copy) NSString *docplay; /**< 白板拉流     */
@property (nonatomic, copy) NSString *docpub; /**< 白板推流     */
@property (nonatomic, copy) NSString *endtime; /**< 结束时间。格式：yyyy-MM-dd HH:mm:ss     */
@property (nonatomic, copy) NSString *exam; /**< 作业     */
@property (nonatomic, copy) NSString *im; /**< 聊天室     */
@property (nonatomic, copy) NSString *interact; /**< 教师学生rtc互动的网页地址     */
@property (nonatomic, copy) NSString *name; /**< 老师姓名     */
@property (nonatomic, assign) NSInteger num;
@property (nonatomic, copy) NSString *rtcplay; /**< RTC旁路直播的拉流地址     */
@property (nonatomic, copy) NSString *rtcpub; /**< RTC旁路直播推流地址。     */
@property (nonatomic, copy) NSString *starttime; /**< 开始时间. 格式：yyyy-MM-dd HH:mm:ss     */
@property (nonatomic, copy) NSString *survey_push; /**< 调查问卷     */
@property (nonatomic, copy) NSString *systime; /**< 系统时间。 格式：yyyy-MM-dd HH:mm:ss     */
@property (nonatomic, copy) NSString *title; /**< 课件标题     */
@property (nonatomic, assign) NSInteger user; /**< 用户 */

@end

#pragma mark - 获取教师课程列表
@class VULTeacherListModel;
@interface VULResponseGetCourseListModel : NSObject
@property (nonatomic, copy) NSString *courseId;/**<课程id */
@property (nonatomic, copy) NSString *courseName;/**<课程名 */
@property (nonatomic, copy) NSString *courseType;/**<课程类型 1普通课程，2课程包，3共享课程或课程包*/
@property (nonatomic, copy) NSString *coursewareCount;/**<课件数 */
@property (nonatomic, copy) NSString *cover;/**<封面 */
@property (nonatomic, copy) NSString *introduce;/**<简介 */
@property (nonatomic, assign) BOOL isFavorite;/**<是否收藏 */
@property (nonatomic, copy) NSString *platformId;/**<平台id */
@property (nonatomic, copy) NSString *popularityCount;/**<人气计数 */
@property (nonatomic, copy) NSString *price; /**<课件费用 */
@property (nonatomic, copy) NSString *mainClassifyName;/**<主分类名称 */
@property (nonatomic, copy) NSString *childClassifyName;/**<子分类名称 */
@property (nonatomic, copy) NSString *studyCount; /**< 观看次数*/
@property (nonatomic, strong) NSArray<VULTeacherListModel *> *teacherList; /**<授课老师列表 */
@end

#pragma mark - 教师端 根据课件id获取作业列表
@interface VULResponseGetHomeworkListModel : NSObject
@property (nonatomic, assign) NSInteger answerLimitTime;  /**<答题限时,分钟 */
@property (nonatomic, assign) NSInteger classifyName;  /**<分类名 */
@property (nonatomic, assign) NSInteger checkCount;  /**< */
@property (nonatomic, assign) NSInteger courseId;  /**<课程id */
@property (nonatomic, copy) NSString *courseName;   /**<课程名 */
@property (nonatomic, assign) NSInteger courseWareId;  /**<课件id */
@property (nonatomic, copy) NSString *courseWareName;   /**<课件名 */
@property (nonatomic, assign) NSInteger gmtCloseTimeStamp;  /**<最晚提交时间 */
@property (nonatomic, assign) NSInteger gmtOpenTimeStamp;  /**<开发时间 */
@property (nonatomic, assign) NSInteger homeworkClassifyId;  /**<作业分类id */
@property (nonatomic, assign) NSInteger homeworkId;  /**<作业id */
@property (nonatomic, copy) NSString *homeworkName;   /**<作业名称 */
@property (nonatomic, copy) NSString *homeworkSequence;   /**<补考序列 */
@property (nonatomic, copy) NSString *homeworkType;   /**<作业类型，1普通作业，2智能作业 */
@property (nonatomic, assign) NSInteger isScoreOneself;  /**< */
@property (nonatomic, assign) NSInteger isWjBrowser;  /**< */
@property (nonatomic, copy) NSString *questionsAuthor;   /**<作业发布者 */
@property (nonatomic, assign) NSInteger questionsAuthorId;  /**<发布者id */
@property (nonatomic, assign) NSInteger score;  /**<总分 */
@property (nonatomic, copy) NSString *state;   /**<作业状态, 0草稿，1已发布，2已结束，3已删除*/
@property (nonatomic, assign) NSInteger sendStudentCount;
@property (nonatomic, assign) NSInteger compelteStudentCount;  /**<学生完成数 */
@property (nonatomic, assign) NSInteger detailCount;
@property (nonatomic, copy) NSString *headPortrait;   /**<头像*/
@property (nonatomic, copy) NSString *realName;   /**<实名*/
@property (nonatomic, copy) NSString *systemTimeStamp; /**< 系统时间戳*/

@end

#pragma mark - 获取网校成员列表
@interface VULResponseSchoolStudentListModel : NSObject
@property (nonatomic, strong) NSArray *classIdList; /**<学生所在的班级id */
@property (nonatomic, copy) NSString *classIds; /**<学生所在的班级id字符串 */
@property (nonatomic, copy) NSString *classNames; /**<班级名*/
@property (nonatomic, copy) NSString *email;
@property (nonatomic, copy) NSString *gmtCreate; /**<创建用户的时间戳 */
@property (nonatomic, copy) NSString *gmtLogin; /**<用户最后一次登录的时间 */
@property (nonatomic, copy) NSString *headPortrait; /**< 学生头像*/
@property (nonatomic, copy) NSString *loginFrequency; /**<用户登录次数*/
@property (nonatomic, copy) NSString *loginName; /**账号*/
@property (nonatomic, copy) NSString *mobilePhone;  /**<手机号 */
@property (nonatomic, copy) NSString *nickname;  /**<昵称 */
@property (nonatomic, copy) NSString *realName;  /**<真名 */
@property (nonatomic, copy) NSString *sex;  /**<性别 0女1男 */
@property (nonatomic, copy) NSString *state;  /**<状态，1正常，3删除，7锁定 */
@property (nonatomic, assign) NSNumber *userId;  /**<uid */
@property (nonatomic, copy) NSString *userIp;  /**<用户ip */
@property (nonatomic, copy) NSString *userSource;  /**<用户来源，1新增，2导入，3PC注册，4手机注册，5微信注册，6扫描注册 */
@property (nonatomic, copy) NSString *userType;  /**<用户类型 1学生 2老师3管理员 */

@property (nonatomic, assign) BOOL selected; //是否已选中

@end

#pragma mark - 教师端 获取网校班级列表
@interface VULResponseGetClassListModel : NSObject
@property (nonatomic, assign) BOOL allLevelLoop; //当前的班级列表层级是不是全部取出来了。 是true,否false. 班级总数过多的网校，只会取第一级
@property (nonatomic, copy) NSString *classId; /**< 班级id*/
@property (nonatomic, copy) NSString *className; /**<班级名称*/
@property (nonatomic, copy) NSString *classSequence; /* 班级序号*/
@property (nonatomic, copy) NSString *classType;  /**<类型 */
@property (nonatomic, copy) NSString *hasChildren;
@property (nonatomic, copy) NSString *studentCount;  /**<学生人数 */
@property (nonatomic, copy) NSString *studentSequenceCount;  /**<包含所有子节点的学生数*/
@property (nonatomic, copy) NSString *teacherCount;  /**<班级教师的数量*/

@end

#pragma mark - 教师端 查询所有签到的日期，已签和未签
@interface VULResponseSignDateModel : NSObject
@property (nonatomic, copy) NSString *sign;  /**<是否签到 0-未全部签到，1-全部签到 */
@property (nonatomic, copy) NSString *date;
@property (nonatomic, copy) NSString *signDate;

@end

#pragma mark - 教师端 根据日期查询某日的签到记录
@interface VULResponseSignRecordModel : NSObject
@property (nonatomic, copy) NSString *gmtSign;  /**<签到时间 */
@property (nonatomic, copy) NSString *isSign;  /**<是否签到  0-未签到，1-已签到 */
@property (nonatomic, copy) NSString *ruleName;  /**<签到规则名 */
@property (nonatomic, copy) NSString *signDate;  /**<签到日期 年月日 */
@property (nonatomic, copy) NSString *signDateId;  /**< 签到日期id*/
@property (nonatomic, copy) NSString *signTimeId;  /**<签到时间段id */
@property (nonatomic, copy) NSString *timeEnd;  /**<该规则时间段结束时间 时分秒*/
@property (nonatomic, copy) NSString *timeStart;  /**<该规则时间段开始时间 时分秒*/
@property (nonatomic, copy) NSString *systemStamp;  /**<系统时间 */
@property (nonatomic, assign) BOOL shouldSign;

@end

#pragma mark -教师作业列表model
@interface VULResponseExamModel : NSObject
@property (nonatomic, copy) NSString *checkCount;
@property (nonatomic, copy) NSString *finishedUserCount;
@property (nonatomic, copy) NSString *gmtEndTs;
@property (nonatomic, copy) NSString *gmtStartTs;
@property (nonatomic, copy) NSString *sendType;
@property (nonatomic, copy) NSString *examId;
@property (nonatomic, copy) NSString *gmtEnd;
@property (nonatomic, copy) NSString *gmtStart;
@property (nonatomic, copy) NSString *title; //标题
@property (nonatomic, copy) NSString *userCount;
@property (nonatomic, copy) NSString *userId;
@property (nonatomic, copy) NSString *nickname;
@property (nonatomic, copy) NSString *realName;

@property (nonatomic, copy) NSString *systemTimeStamp; /**< 系统时间戳*/

@end
