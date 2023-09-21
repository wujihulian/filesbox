#import <Foundation/Foundation.h>
#import "SDImageCache.h"

typedef void(^ChunkUploaderProgressBlock)(CGFloat progress);
typedef void(^ChunkUploaderSpeedBlock)(CGFloat speed);
typedef void(^ChunkUploaderSuccessBlock)(bool sucess);
typedef void(^ChunkUploaderFailureBlock)(NSError *error);
typedef NS_ENUM(NSUInteger, UploadStatus) {
    UploadStatusIdle,        // 空闲状态
    UploadStatusUploading,   // 上传中状态
    UploadStatusPaused,      // 暂停状态
};
// 文件上传进度
@interface DownloadProgress : NSObject
@property (nonatomic, strong) NSMutableDictionary *progressDict; // 用于存储下载任务的进度
@property (nonatomic, strong) NSMutableDictionary *progressData; // 用于存储下载任务的进度

@property (nonatomic, assign) BOOL isShare; // 用于分享上传

@property (nonatomic, assign) BOOL isOther; //其它应用

@property (nonatomic, retain) NSURL *otherUrl; //其它应用
@property (nonatomic, assign) BOOL isWeb; // NO 是第一次
@property (nonatomic, strong) NSDictionary *emotionDic; // NO 是第一次



+ (instancetype)sharedInstance;
@end
@interface UBUploadModel : NSObject

@property (nonatomic, strong) NSProgress *progress; // 进度
@property (nonatomic, assign) UploadStatus status;  // 状态
@property (nonatomic, retain)  NSDate *startDate;  // 开始时间
@property (nonatomic, assign)  double speed;  // 下载速度
@property (nonatomic, assign)  bool isPic;  // 是否图片
@property (nonatomic, assign)  bool isSucess;  // 是否图片
@property (nonatomic, copy)  NSString *fileType; // 文件类型
@property (nonatomic, copy)  NSString *fileName; // 文件名字
@property (nonatomic, copy)  NSString *filePath; // 文件路径
@property (nonatomic, retain)  UIImage *image; // 图片
@property (nonatomic, copy)  NSString *busType; // 上传类型
@property (nonatomic, copy)  NSString *chunk; // 当前分段,从0开始,有分段需求时要传
@property (nonatomic, copy)  NSString *chunkSize; // 分段大小,有分段需求时要传
@property (nonatomic, copy)  NSString *chunks; // 分段数,有分段需求时要传
@property (nonatomic, copy)  NSString *fileGmtCreate; //     文件的原始创建时间（毫秒级时间戳）
@property (nonatomic, copy)  NSString *hashMd5; //     文件的原始创建时间（毫秒级时间戳）
@property (nonatomic, copy)  NSString *sourceID; //所在文件夹ID
@property (nonatomic, copy)  NSString *thumbSize; //图片缩略图大小, 图片类业务额外配置
@property (nonatomic, strong) NSURL *fileUrl; // 本地文件Url
@property (nonatomic, assign)  int yetChunk; // 已经上传片数
@property (nonatomic, strong) NSData *data; // 本地文件data
@property (nonatomic, assign)  NSInteger fileSize; // 总大小
@property (nonatomic, copy)  NSString *fileID; //文件id

@property (nonatomic, copy)  NSString *path; //封面
@property (nonatomic, assign)  bool isDown;  // 是否是下载

@property (nonatomic, assign)  bool isOffline;  // 是否是下载

@property (nonatomic, assign)  bool isStop;  // 暂停成功
@property (nonatomic, copy)  NSString *pathId; //新版本id

@property (nonatomic, retain)  NSMutableDictionary *dataDic; //上传成功的数据
@property (nonatomic, assign)  bool isOpen;  


@end
@interface ChunkUploader : NSObject
    
@property (nonatomic, assign, readonly) UploadStatus uploadStatus; // 当前上传状态
@property (nonatomic, copy) ChunkUploaderProgressBlock progressBlock; // 上传进度回调
@property (nonatomic, copy) ChunkUploaderSpeedBlock speedBlock; // 上传速度回调
@property (nonatomic, copy) ChunkUploaderSuccessBlock successBlock; // 上传成功回调
@property (nonatomic, copy) ChunkUploaderFailureBlock failureBlock; // 上传失败回调
@property (nonatomic, strong) NSMutableDictionary  *changeModelDic; // 上传失败回调
@property (nonatomic, strong) NSMutableArray  *uploadedIdentifiers; // 已经上传过
@property (nonatomic, strong) NSMutableArray *unuploadedPhotos;
@property (nonatomic, strong) UBUploadModel *isUploadModel;


+ (instancetype)sharedManager;

+(void)uploadFileAtModel:(UBUploadModel *)model
    progress:(ChunkUploaderProgressBlock)progressBlock
    speed:(ChunkUploaderSpeedBlock)speedBlock
    success:(ChunkUploaderSuccessBlock)successBlock
    failure:(ChunkUploaderFailureBlock)failureBlock;
-(void)checkFileAtModel:(UBUploadModel *)model  progress:(ChunkUploaderProgressBlock)progressBlock
    speed:(ChunkUploaderSpeedBlock)speedBlock
    success:(ChunkUploaderSuccessBlock)successBlock
    failure:(ChunkUploaderFailureBlock)failureBlock;
+ (NSString *)mimeTypeForFileAtPath:(NSString *)path;
+(void)updateUploadFileAtModel:(UBUploadModel *)model;
+(NSMutableArray *)getDataArrWithModel:(UBUploadModel *)model;
+(void)writeDataWithModel:(NSURL *)newURL
                  sourceID:(NSString *)sourceID
                  isPic:(BOOL)pic
                  success:(ChunkUploaderSuccessBlock)successBlock;
+(void)writeDataWithModel:(NSURL *)newURL
                  sourceID:(NSString *)sourceID
                  pathId:(NSString *)pathId
                  isPic:(BOOL)pic
                  success:(ChunkUploaderSuccessBlock)successBlock;
-(void)backupFile;
-(BOOL)isBackUp;
@end



