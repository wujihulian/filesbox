//
//  FBDownloadFileManage.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/6.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBDownloadFileManage : NSObject
/** AFNetworking断点下载（支持离线）需用到的属性 **********/
/** 文件的总长度 */
@property (nonatomic, assign) NSInteger fileLength;
/** 当前下载长度 */
@property (nonatomic, assign) NSInteger currentLength;
/** 文件句柄对象 */
@property (nonatomic, strong) NSFileHandle *fileHandle;

/** 下载任务 */
@property (nonatomic, strong) NSURLSessionDataTask *downloadTask;
/* AFURLSessionManager */
@property (nonatomic, strong) AFURLSessionManager *manager;

-(void)downloadFileWithModel:(UBUploadModel *)model;
-(void)resume:(UBUploadModel *)model;
-(void)pause:(UBUploadModel *)model;
@end
@interface FBDownloadFileAllManage : NSObject
@property (nonatomic, strong) NSMutableDictionary *sessionDic; // 用于存储下载任务的进度
+ (instancetype)sharedManager;
-(void)addDownloadFileWithModel:(UBUploadModel *)model;

@end

NS_ASSUME_NONNULL_END
