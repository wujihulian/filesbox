//
//  VULFileModel.h
//  UnlimitedBusiness
//
//  Created by zuoyi on 2021/12/14.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN
@class VULFileObjectModel,VULFileInfoModel;
typedef void(^VULRequestCompletion)(__kindof VULBaseRequest * _Nonnull request);
typedef void(^BackModel)(__kindof id  _Nonnull responseObject);
typedef void(^BackPth)(NSString *pathUrl);

@interface VULAllFileModel : NSObject

@property (nonatomic, retain) VULFileInfoModel *current;
@property (nonatomic, retain) NSMutableArray <VULFileObjectModel *>*fileList;
@property (nonatomic, retain) NSMutableArray<VULFileObjectModel *> *folderList;

+(void)reNameFile:(VULFileObjectModel *)model name:(NSString *)name  type:(NSString *)fileType  completion:(VULRequestCompletion )completon;

+(void)operationFile:(NSMutableArray *)selectArr operationType:(NSString *)operationType completion:(VULRequestCompletion )completon;

+(void)copyFile:(NSMutableArray *)selectArr operationType:(NSString *)operationType sourceID:(NSString *)sourceID sourceLevel:(NSString *)sourceLevel completion:(VULRequestCompletion )completon;
+(void)getDetailConentWithSourceID:(NSString *)sourceID icon:(NSString *)icon completion:(BackModel )completon;

+(void)getPlayVideoUrlFromModel:(VULFileObjectModel *)model sucess:(BackPth)pathUrl;

+(void)getPreview:(NSDictionary *)dic completion:(VULRequestCompletion )completon;
@end
@interface VULFileObjectModel : NSObject

@property (nonatomic, assign) BOOL isload;//yes加载过了

@property (nonatomic, retain) VULAllFileModel *children;
@property (nonatomic, assign) BOOL adminHigh;
@property (nonatomic, copy) NSString *ext;
@property (nonatomic, copy) NSString *typeName;


@property (nonatomic, copy) NSString *icon;
@property (nonatomic, copy) NSString *infoTypeID;
@property (nonatomic, copy) NSString *status;
@property (nonatomic, copy) NSString *namePinyin;
@property (nonatomic, copy) NSString *namePinyinSimple;



@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *open;
@property (nonatomic, copy) NSString *parentID;
@property (nonatomic, copy) NSString *pathDisplay;
@property (nonatomic, copy) NSString *sourceID;
@property (nonatomic, copy) NSString *sourceLevel;
@property (nonatomic, copy) NSString *path;
@property (nonatomic, copy) NSString *labelId;
@property (nonatomic, copy) NSString *style;
@property (nonatomic, retain) NSMutableArray *titleArr;
@property (nonatomic, retain) NSMutableArray *sourceIDArr;
@property (nonatomic, copy) NSString *length;
@property (nonatomic, retain) NSMutableArray *tagList;
@property (nonatomic, assign) NSInteger sort;          //置顶

@property (nonatomic, assign) BOOL isFav;
@property (nonatomic, assign) BOOL canDownload;
@property (nonatomic, assign) BOOL isTruePath;
@property (nonatomic, assign) BOOL isChildren;
@property (nonatomic, assign) BOOL isShare;
@property (nonatomic, assign) BOOL isDetail;


@property (nonatomic, copy) NSString *numDownload;
@property (nonatomic, copy) NSString *numView;
@property (nonatomic, copy) NSString *nickname;
@property (nonatomic, copy) NSString *h264Path;


@property (nonatomic, copy) NSString *type;
@property (nonatomic, copy) NSString *createTime;
@property (nonatomic, copy) NSString *modifyTime;
@property (nonatomic, copy) NSString *size;
@property (nonatomic, copy) NSString *fileID;
@property (nonatomic, copy) NSString *fileType;
@property (nonatomic, copy) NSString *downloadUrl;
@property (nonatomic, copy) NSString *groupID;
@property (nonatomic, copy) NSString *thumb;
@property (nonatomic, copy) NSString *options;
@property (nonatomic, copy) NSString *passWord;
@property (nonatomic, copy) NSString *shareHash;
@property (nonatomic, copy) NSString *shareID;
@property (nonatomic, copy) NSString *userID;
@property (nonatomic, copy) NSString *shareTitle;
@property (nonatomic, copy) NSString *timeTo;
@property (nonatomic, copy) NSString *id;


@property (nonatomic, copy) NSString *auth;
@property (nonatomic, copy) NSString *avatar;
@property (nonatomic, copy) NSString *detail;
@property (nonatomic, copy) NSString *desc;
@property (nonatomic, copy) NSString *oexeContent;
@property (nonatomic, copy) NSString *oexeFileID;
@property (nonatomic, copy) NSString *oexeFileType;
@property (nonatomic, copy) NSString *oexeIsFolder;
@property (nonatomic, copy) NSString *oexeSourceID;
@property (nonatomic, copy) NSString *groupName;
@property (nonatomic, copy) NSString *sizeMax;
@property (nonatomic, copy) NSString *targetType; // 1 个人  2 企业
@property (nonatomic, copy) NSString *createUser;
@property (nonatomic, copy) NSString *hasFile;
@property (nonatomic, copy) NSString *hasFolder;
@property (nonatomic, copy) NSString *isDelete;
@property (nonatomic, copy) NSString *isFolder;
@property (nonatomic, copy) NSString *modifyUser;
@property (nonatomic, copy) NSString *parentLevel;
@property (nonatomic, copy) NSString *sourceHash;
@property (nonatomic, copy) NSString *viewTime;

@property (nonatomic, copy) NSString *targetID;
@property (nonatomic, copy) NSString *createUserJson;
@property (nonatomic, copy) NSString *modifyUserJson;
@property (nonatomic, copy) NSString *resolution;
@property (nonatomic, copy) NSString *hashMd5;
@property (nonatomic, copy) NSString *shareUrl;



@property (nonatomic,copy) NSString *description;//描述(文字描述组件中的文字内容)
@property (nonatomic,copy) NSString *DSdescription;//描述(文字描述组件中的文字内容)


@property (nonatomic, assign) BOOL isExpanded;
@property (nonatomic, assign) NSInteger level;          // 节点层级


@property (nonatomic, assign, readonly) BOOL isLeaf;    // 是否是叶子节点
@property (nonatomic, assign) BOOL isSelect;

@end
@interface VULFileInfoModel : NSObject
@property (nonatomic, copy) NSString *icon;
@property (nonatomic, assign) BOOL isChildren;
@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *pathDesc;
@property (nonatomic, copy) NSString *pathDisplay;
@property (nonatomic, copy) NSString *type;

@end

@interface VULFileModel : NSObject

@property (nonatomic, copy) NSString *busType;
@property (nonatomic, copy) NSString *description;
@property (nonatomic, assign) NSInteger directoryId;
@property (nonatomic, copy) NSString *directoryName;
@property (nonatomic, copy) NSString *fileCount;
@property (nonatomic, assign) NSInteger fileSize;
@property (nonatomic, copy) NSString *gmtCreate;
@property (nonatomic, copy) NSString *gmtModified;
@property (nonatomic, copy) NSString *isDefault;
@property (nonatomic, copy) NSString *sequence;
@property (nonatomic, copy) NSString *subDirectoryCount;
@property (nonatomic, copy) NSString *userId;
@property (nonatomic, copy) NSString *fileName;
@property (nonatomic, assign) NSInteger fileId;
@property (nonatomic, copy) NSString *suffix;
@property (nonatomic, copy) NSString *thumb;
@property (nonatomic, copy) NSString *downloadUrl;

@end

@interface VULFileZIPObjectModel : NSObject
@property (nonatomic, retain) NSMutableArray<VULFileZIPObjectModel *> *childList;
@property (nonatomic, retain) NSString *directory;
@property (nonatomic, retain) NSString *fileName;
@property (nonatomic, retain) NSString *originName;
@property (nonatomic, retain) NSString *parentFileName;
@property (nonatomic, retain) NSString *size;
@property (nonatomic, assign) NSInteger level;          // 节点层级
@property (nonatomic, assign) BOOL isExpanded;
@property (nonatomic, copy) NSString *icon;
@property (nonatomic, copy) NSString *fileType;
@property (nonatomic, retain) NSString *fullName;
@property (nonatomic, retain) NSString *index;
@property (nonatomic, assign) BOOL ispassWord;
@property (nonatomic, retain) NSString *sourceID;
@property (nonatomic, assign) BOOL encrypted;

@end
@interface VULInfoModel : NSObject
@property (nonatomic, retain) NSString *attachmentCount;
@property (nonatomic, retain) NSString *avatar;
@property (nonatomic, retain) NSString *computerPicPath;
@property (nonatomic, retain) NSString *computerPicPathVertical;
@property (nonatomic, retain) NSString *createTime;
@property (nonatomic, retain) NSString *createUser;
@property (nonatomic, retain) NSString *detail;
@property (nonatomic, retain) NSString *fileDetail;
@property (nonatomic, retain) NSString *gmtPage;
@property (nonatomic, retain) NSString *infoID;
@property (nonatomic, retain) NSString *infoSource;
@property (nonatomic, retain) NSString *infoType;
@property (nonatomic, retain) NSString *infoTypeID;
@property (nonatomic, retain) NSString *infoUrl;
@property (nonatomic, retain) NSString *introduce;
@property (nonatomic, retain) NSString *isApplyOriginal;
@property (nonatomic, retain) NSString *isHide;
@property (nonatomic, retain) NSString *isLogin;
@property (nonatomic, retain) NSString *isTop;
@property (nonatomic, retain) NSString *isTransport;
@property (nonatomic, retain) NSString *isUrlInfo;
@property (nonatomic, retain) NSString *isVertical;
@property (nonatomic, retain) NSString *isVideoExists;
@property (nonatomic, retain) NSString *likeCount;
@property (nonatomic, retain) NSString *mobilePicPath;
@property (nonatomic, retain) NSString *modifyTime;
@property (nonatomic, retain) NSString *nickname;
@property (nonatomic, retain) NSString *previewUrl;
@property (nonatomic, retain) NSString *remark;
@property (nonatomic, retain) NSString *rightFlag;
@property (nonatomic, retain) NSString *seo;
@property (nonatomic, retain) NSString *showAttachment;
@property (nonatomic, assign) NSInteger sort;
@property (nonatomic, retain) NSString *sourceID;
@property (nonatomic, retain) NSString *status;
@property (nonatomic, retain) NSString *thumb;
@property (nonatomic, retain) NSString *thumbVertical;
@property (nonatomic, retain) NSString *title;
@property (nonatomic, retain) NSString *topTime;
@property (nonatomic, retain) NSString *userAgent;
@property (nonatomic, retain) NSString *userIp;
@property (nonatomic, retain) NSString *userName;
@property (nonatomic, retain) NSString *videoID;
@property (nonatomic, retain) NSString *viewCount;

@end
@interface VULInfoTypeModel : NSObject
@property (nonatomic, retain) NSArray<VULInfoTypeModel *> *children;
@property (nonatomic, retain) NSString *createTime;
@property (nonatomic, retain) NSString *infoTypeID;
@property (nonatomic, retain) NSString *modifyTime;
@property (nonatomic, retain) NSString *namePinyin;
@property (nonatomic, retain) NSString *namePinyinSimple;
@property (nonatomic, retain) NSString *parentID;
@property (nonatomic, retain) NSString *parentLevel;
@property (nonatomic, retain) NSString *sort;
@property (nonatomic, retain) NSString *status;
@property (nonatomic, retain) NSString *typeName;

@end

NS_ASSUME_NONNULL_END
