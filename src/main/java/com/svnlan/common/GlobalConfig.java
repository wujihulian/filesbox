package com.svnlan.common;

import java.time.format.DateTimeFormatter;

/**
 * @Author:
 * @Description: 常量
 */
public class GlobalConfig {
    public static final String COMMA_DELIMITER = ",";
    public static final String PWD_SALT = "PJEA4DR2WS8DF651DGT";

    public static final String JWT_REDIS_PREFIX = "jwt";
    public static final String JWT_TOKEN_HASH_PRE = "jwt_token_hash_";


    public static final String HOST_REDIS_PREFIX = "host";
    public static final String default_disk_path_pre = "/uploads";

    public static final String UPCONFIG = "upconfig.properties";

    public static final String LOGIN_PASSWORD_AES_KEY = "njsdearr8h239ay3";
    public static final String OPEN_LOGIN_PASSWORD_AES_KEY = "oi2qpe2dnwufa982";
    //记录,判断短时间内多次登录使用
    public static final String JWT_FAST_LOGIN_KEY_PRE = "jwt_fast_login_key_";
    //第三方登录, 临时权限redis key前缀
    public static final String THIRD_LOGIN_TEMP_AUTH_PRE = "thirdLoginTempAuth_";

    public static final String SEVEN_DAY_CLIENT_COUNT = "seven_day_client_count_";
	public static final String RIGHTS_API_KEY = "rights_api_hash";
    // api token redis key
    public static final String JWT_API_REDIS_PREFIX = "api_jwt_";


    /** copy验证码的cookie键名 **/
    public static final String CAPTCHA_CODE_COOKIE_KEY = "captchaCode";
    public static final int CAPTCHA_CODE_TTL = 180; // 验证码的有效时间,秒
    // 生成验证码图片方法 outCaptcha(),返回map中,对应的客户端的cookie的值的key
    public static final String CAPTCHA_CODE_COOKIE_VALUE_RETURNKEY="codeCookieValue";
    // 生成验证码图片方法 outCaptcha(),返回map中,对应的图片验证码内容的key
    public static final String CAPTCHA_CODE_CODE_VALUE_RETURNKEY = "captchaCodeValue";


    /** resources目录下验证码图片子目录名称 **/
    public static final String RESOURCES_CAPTCHA_DIRECTORY = "captcha";
    /** 验证码的cookie键名 **/
    public static final String CAPTCHA_COOKIE_KEY = "captcha";
    public static final int CAPTCHA_TTL = 180; // 验证码的有效时间,秒
    // 生成验证码图片方法 outCaptcha(),返回map中,对应的客户端的cookie的值的key
    public static final String CAPTCHA_COOKIE_VALUE_RETURNKEY="cookieValue";
    // 生成验证码图片方法 outCaptcha(),返回map中,对应的图片验证码内容的key
    public static final String CAPTCHA_CODE_VALUE_RETURNKEY = "captchaValue";


    /** copy验证码的cookie键名 图形滑动验证码 **/
    public static final String IMAGE_CAPTCHA_CODE_COOKIE_KEY = "captchaImageCode";
    public static final int IMAGE_CAPTCHA_CODE_TTL = 180; // 验证码的有效时间,秒
    // 生成验证码图片方法 outCaptcha(),返回map中,对应的客户端的cookie的值的key

    //学习心得图片类型
    public static final String[] STUDY_EXPERIENCE_IMAGE_TYPE = {"jpg", "jpeg", "gif", "bmp", "png", "svg"};

    //课程下载
    public static final String COURSE_DOWNLOAD_AES_KEY = "fj0j023rwdopqwpo";

    //m3u8 播放AES key
    public static final String M3U8_PLAY_AES_KEY = "dfjgj6kqdfvh89he";
    //m3u8 播放key的分隔符
    public static final String M3U8_PLAY_KEY_SEPARATOR = "pp&&kk&&";

    public static final String ATTACHMENT_AES_KEY = "sadh8as1d032r0h0";
    public static final String ATTACHMENT_KEY_SEPARATOR = "ff_&_gg";
// "jpg", "jpeg", "png", "gif", "bmp", "ico", "svg", "webp", "tif", "tiff", "cdr", "svgz", "xbm", "eps", "pjepg", "heic", "raw", "psd", "ai"
    public static final String[] IMAGE_TYPE_ARR = {"jpg", "jpeg", "png", "gif", "bmp", "ico", "svg", "webp", "tif", "tiff", "cdr", "svgz", "xbm", "eps", "pjepg", "heic", "raw", "psd", "ai"};
    public static final String[] list_path_source_type = {"jpg", "jpeg", "png", "gif", "bmp", "ico", "svg", "webp", "tif", "tiff", "cdr", "svgz", "xbm", "eps", "pjepg", "heic", "raw", "psd", "ai", "mp3"};
    public static final String[] IMAGE_NO_GIF_TYPE_ARR = {"jpg", "jpeg", "bmp", "png"};
    public static final String[] IMAGE_LITE_TYPE_ARR = {"jpg", "jpeg", "png"};
    public static final String[] IMAGE_SHOW_TYPE_ARR = {"jpg", "jpeg", "gif", "bmp", "png", "svg", "psd", "tga"};
    public static final String[] VIDEO_SHOW_TYPE_ARR = {"flv","avi","mpeg","mpg","mp4","rmvb","rm","mov","3gp","f4v","wmv", "pmg", "mkv", "m4v"};
    public static final String[] DOC_TYPE_ARR = {"doc", "docx", "ppt", "pptx", "pdf", "xls", "xlsx"};
    public static final String[] yongzhong_doc_type = {"eid","docx","dotx","doc","dot","rtf","txt","uot","htm","wps","wpt"};
    public static final String[] yongzhong_excel_type = {"eis","xlsx","xltx","xls","xlt","uos","dbf","csv","xml","et","ett"};
    public static final String[] yongzhong_ppt_type = {"eip","pptx","potx","ppt","pot","ppsx","pps","dps","dpt","uop"};
    public static final String[] yongzhong_doc_excel_ppt_type = {"eid","docx","dotx","doc","dot","rtf","txt","uot","htm","wps","wpt",
            "eis","xlsx","xltx","xls","xlt","uos","dbf","csv","xml","et","ett",
            "eip","pptx","potx","ppt","pot","ppsx","pps","dps","dpt","uop", "pdf"
            //            ,"zip","gz","rar","iso","tar","7z","ar","bz","bz2","xz", "arj"

    };
    // 相机文件 dng,nef,orf,pef,srw,x3f,srf,arw,sr2
    //public static final String[] CAMERA_TYPE_ARR = {"cr2", "dcr", "epf", "raf", "kdc", "dcr", "dng", "nef", "orf", "pef", "tiff", "cdr", "svgz", "xbm", "eps", "pjepg", "heic", "raw", "psd", "ai"};
    public static final String[] CAMERA_TYPE_ARR = {"arw", "mrw", "erf", "raf","cr2", "nrw", "nef", "orf", "rw2", "pef", "srf"
    , "dcr", "kdc", "dng"};


    public static final String[] DOC_SHOW_TYPE_ARR = {"doc", "docx", "ppt", "pptx", "pdf", "xls", "xlsx", "pps", "wps", "txt"};
    public static final String[] AUDIO_SHOW_TYPE_ARR = {"mp3", "wav", "flac", "mpa"};
    public static final String[] UNZIP_SHOW_TYPE_ARR = {"tar", "zip", "gzip", "bz2", "rar", "7z", "gz", "iso", "ar", "bz", "xz", "arj"};
    public static final String[] ZIP_SHOW_TYPE_ARR = {"zip", "tar"};
    public static final String[] VIDEO_TYPE_ARR = {"mp4", "flv", "rm", "rmvb", "avi", "mkv", "mov", "f4v", "mpeg", "mpg", "vob", "wmv", "ogv", "webm", "3gp", "mts", "m2ts", "m4v", "mpe", "3g2", "asf", "dat", "asx", "wvx"};
    public static final String[] VIDEO_AUDIO_TYPE_CONVERT = {"mp4", "wav", "flac", "flv", "rm", "rmvb", "avi", "mkv", "mov", "f4v", "mpeg", "mpg", "vob", "wmv", "ogv", "webm", "3gp", "mts", "m2ts", "m4v", "mpe", "3g2", "asf", "dat", "asx", "wvx"};
    public static final String[] AUDIO_VIDEO_SHOW_TYPE_ARR = {"mp4", "flv", "rm", "rmvb", "avi", "mkv", "mov", "f4v", "mpeg", "mpg", "vob", "wmv", "ogv", "webm", "3gp", "mts", "m2ts", "m4v", "mpe", "3g2", "asf", "dat", "asx", "wvx", "mpa", "mp3", "wav", "wma", "m4a", "ogg", "omf", "amr", "aa3", "flac", "aac", "cda", "aif", "aiff", "mid", "ra", "ape"};
    public static final String CONVERT_SOURCE_ID_H5_MAP = "convert_source_id_h5";
    public static final String CONVERT_SOURCE_ID_SWF_MAP = "convert_source_id_swf";
    public static final String M3U8_KEY_INFO_SEPARATOR = "_&_&_";
    public static final String M3U8_AES_PASSWORD = "vj90eu321fk01";

    public static final String CLOUD_OPERATION_LOCK_KEY = "cloudOperationLock";

    public static final long CHUNK_FILE_SIZE = 2 * 1024 * 1024;

    //转码文件redis key
    public static final String CONVERT_FILE_REDIS_KEY_PRE = "convert_file_";
    //redis存2小时20分钟
    public static final Long CONVERT_FILE_REDIS_TTL = 8400L;
    //项目启动10分钟内
    public static final Long RUN_TIME_OFFSET = 600000L;
    public static final String IMAGE_LARGE_SIZE_KEY = "imageLargeSize";


    //用户发送绑定邮件验证码发送频率限制 REDIS KEY(userId)
    public static final String EMAIL_BIND_SEND_FREQUENCY_KEY = "email_bind_send_frequency_%d";
    //用户发送绑定邮件验证码每日限制次数 REDIS KEY(userId)
    public static final String EMAIL_BIND_SEND_LIMIT_KEY = "email_bind_send_limit_%d";

    //用户发送绑定 同一邮箱 邮件验证码每日限制次数 REDIS KEY(userId、email)
    public static final String SINGLE_EMAIL_BIND_SEND_LIMIT_KEY = "single_email_bind_send_limit_%d_%s";

    //用户发送绑定邮件验证码每日限制次数
    public static final Integer EMAIL_BIND_SEND_LIMIT_NUM = 10;

    //用户发送绑定 同一邮箱 邮件验证码每日限制次数
    public static final Integer SINGLE_EMAIL_BIND_SEND_LIMIT_NUM = 10;

    // 缓存，用户前缀
    public static final String REDIS_KEY_USER = "user_";

    // 缓存，用户前缀
    public static final String REDIS_KEY_USER_CODE = "user_verifyCode:";
    public static final String REDIS_KEY_USER_STATE = "user_state_";
    public static final String REDIS_KEY_USER_GROUP_AUTH = "user_group_auth";
    // 普通下载定时删除redis
    public static final String COMMON_DOWNLOAD_KEY_SET = "commonDownloadKeySet";

    public static final String my_fav_key = "my_fav_key_";
    public static final String my_tag_key = "my_tag_key_";
    public static final String my_share_key = "my_share_key_";
    public static final String SYSTEM_AUTH = "explorer.add,explorer.upload,explorer.view,explorer.download,explorer.share,explorer.remove,explorer.edit,explorer.move,explorer.serverDownload,explorer.search,explorer.unzip,explorer.zip,user.edit,user.fav,explorer.informationView,admin.index.dashboard,admin.index.setting,admin.index.loginLog,admin.index.log,admin.index.server,admin.role.list,admin.role.edit,admin.job.list,admin.job.edit,admin.member.list,admin.member.userEdit,admin.member.groupEdit,admin.auth.list,admin.auth.edit,admin.plugin.list,admin.plugin.edit,admin.storage.list,admin.storage.edit,admin.autoTask.list,admin.autoTask.edit,admin.index.information";
    public static final String SYSTEM_GROUP_AUTH = "1,2,3,4,5,6,7,8,9,10,11,12,13,14";


    public static final String oldInnerServer = "http://video.zdb9.com";
    public static final String m3u8ConvertUrlPlaceholder = "m3u8_convert_server_url_placeholder";


    public static final String async_key_zip_file = "async_key_zip_file:";
    public static final String progress_key_zip_file = "progress_key_zip_file:";
    public static final String async_key_zip_file_info = "async_key_zip_file_info:";

    public static final String CONVERT_TIME_KEY = "convert_time_set";


    public static final String file_edit_key = "file_edit_key:";
    public static final String yzwo_file_edit_key = "yzwo_file_edit_key:";
    public static final String separator = "\\\\";
    public static final String separatorTO = "/";
    public static final String systemConfig_captcha = "systemConfig_captcha";
    public static final String userRoleAuth_key = "userRoleAuth_key";
    public static final String yzwo_file_edit_user_key = "yzwo_file_edit_user_key:";

    // 压缩预览
    public static final String FILE_PREVIEW_COMPRESS_KEY = "compress_preview_file_key:";
    public static final String[] DESIGN_WEB_CLIENT_TYPE_ARR = {"pc", "mb"};

    // 视频编辑redis
    public static final String video_edit_getVideoShearList = "video_edit_getVideoShearList_key_";

    // 文件夹
    public static final String dir_user_pathDisplay = "dir_user_pathDisplay_";
    public static final String dir_group_pathDisplay = "dir_group_pathDisplay";

    // 资讯导入
    public static final String IMPORT_ARTICLE_LOCK = "importArticleLock_";

    //转成图片的缓存KEY(其中%d为文件ID)
    public static String infoConvertToJPGKey = "infoConvertToJPG_%d";
    //转成图片的防并发的缓存KEY(其中%d为文件ID)
    public static String concurrentConvertToJPGKey = "concurrentConvertToJPG_%d";


    public static final DateTimeFormatter dateTimeStandardFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter dateStandardFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String BAN_WORDS_LIB_FILE_NAME = "banWords.txt";

    //（非小程序）其他最多装扮数量限制
    public static final Integer MaxDesignCount = 100;

    public static final String SEARCH_PERMISSION_KEY = "searchComponentPermission";
    //主页，二级页后缀
    public static final String GEN_PAGE_SUFFIX = ".shtml";

    //favicon
    public static final String FAV_ICON_FILENAME = "favicon.png";
    public static final String FAV_ICON_PATH = "/common/";
    //装扮url防并发
    public static final String DESIGN_URL_LOCK_PRE = "design_url_lock";
    public static final Long DESIGN_URL_LOCK_TTL = 5 * 1000L;


    //扫码登录加解密的密钥
    public static final String ScanLoginSalt = "OPjanpEaoE7ZGl9v";


    public static final Integer scanLoginSignatureExpire = 1;
    public static final Integer scanLoginCodeExpire = 60;
    //扫码登录校验KEY
    public static final String SCAN_LOGIN_CODE_REDIS_KEY = "scanLogin_";
    public static final String logAuthUrl = "/pages/download.html";

    //防并发加锁的REDIS KEY（其中%s为tempAuth）
    public static final String ScanLoginAuth = "sla_%s";
    public static final String mesWarning_cache_key = "mesWarning_cache_key";
    public static final int mesWarning_cache_ttl = 8; // 消息预警缓存有效时间
    // 消息预警判断持续redis
    public static final String mesWarning_cpu_key = "mesWarning_cpu_key";
    public static final String mesWarning_mem_key = "mesWarning_mem_key";
    public static final String mesWarning_du_key = "mesWarning_du_key";
    public static final String mesWarning_send_temp_key = "【消息预警】%s %s %s使用占比超过%s，请注意！（您可在管理后台>消息预警，关闭此提醒）";

    //扫码登录的 channelId与标识ID(loginId)的关联关系 HASH REDIS KEY（%s为对应的房间roomName）
    public static final String ScanLoginIdChannelRelation = "SLIdChannelRelation_%s";
    //扫码登录的 APP token与 生成的临时授权码的关联关系 HASH REDIS KEY（%s为对应的房间roomName）
    public static final String ScanLoginTempAuthRelation = "SLTempAuthRelation_%s";
    //扫码登录的临时授权码REDIS KEY 前缀
    public static final String ScanLoginTempAuthRedisKeyPrefix = "scanLg";
    public static final String private_replace_key = "/{dir}/{path}/";

    public static final String async_key_convert_doc_file = "async_key_convert_doc_file_";
    public static final String progress_key_convert_doc_file = "progress_key_convert_doc_file_";

    public static final String show_img_api_key = "/api/disk/video/img/";


    public static final String async_key_unzip_file = "async_key_unzip_file_";
    public static final String progress_key_unzip_file = "progress_key_unzip_file_";
    public static final String temp_img_video_key = "temp_img_video_key_";
    public static final String homeExplorerRedisKey = "homeExplorerRedisKey_";
    public static final String homeExplorerOneRedisKey = "homeExplorerOneRedisKey_";
    public static final String async_key_convert_img_video = "async_key_convert_img_video_";
    public static final String progress_key_convert_img_video = "progress_key_convert_img_video_";



}
