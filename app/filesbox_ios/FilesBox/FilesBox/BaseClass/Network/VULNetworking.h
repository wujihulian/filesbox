//
//  VULNetworking.h
//  VideoULimit
//
//  Created by ZCc on 2018/9/3.
//  Copyright © 2018年 zcc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AFNetworking.h"

@interface VULNetworking : AFHTTPSessionManager


/**
 单例

 @return 实例对象
 */
+ (instancetype)shareManager;

#pragma mark - GET 请求网络数据

+ (void)requestGETDataWithPath:(NSString *)path
                withParameters:(NSDictionary *)parameters
                  withProgress:(void(^) (float progress))downLoadProgress
                       success:(void(^) (BOOL isSuccess, id responseObject))success
                       failure:(void(^) (NSError *error))failure;

#pragma mark - POST 传送网络数据

+ (void)sendPOSTDataWithPath:(NSString *)path
              withParameters:(NSDictionary *)parameters
                withProgress:(void(^) (float progress))upLoadProgress
                     success:(void(^) (BOOL isSuccess, id responseObject))success
                     failure:(void(^) (NSError *error))failure;


#pragma mark = POST 上传图片

+ (void)sendPostImageWithPath:(NSString *)path
               withParameters:(NSDictionary *)parameters
               withImageArray:(NSArray *)imageArray
              withTargetWidth:(CGFloat)width
                 withProgress:(void(^) (NSProgress *progress))upLoadprogress
                      success:(void(^) (BOOL isSuccess, id responseObject))success
                      failure:(void(^) (NSError *error))failure;
#pragma mark = POST 不压缩 上传图片

+ (void)sendPostClearImageWithPath:(NSString *)path
               withParameters:(NSDictionary *)parameters
               withImageArray:(NSArray *)imageArray
              withTargetWidth:(CGFloat)width
                 withProgress:(void(^) (NSProgress *progress))upLoadprogress
                      success:(void(^) (BOOL isSuccess, id responseObject))success
                      failure:(void(^) (NSError *error))failure;

#pragma mark - 表单上传视频文件
/**
 表单上传视频文件

 @param posturl 上传url
 @param videoData 本地视频文件data
 @param videoName 上传的视频名称
 @param parameters 参数
 @param progressBlock 进度回调
 @param success 成功回调
 @param failure 失败回调
 */
+ (void)uploadVideoWithPostUrl:(NSString *)posturl VideoData:(NSData *)videoData FileName:(NSString *)videoName Parameters:(NSDictionary *)parameters progress:(void (^)(NSProgress *progress))progressBlock  success:(void(^) (BOOL isSuccess, id responseObject))success failure:(void(^) (NSError *error))failure;


#pragma mark - 表单上传音频文件
+ (void)uploadAudioWithPostUrl:(NSString *)posturl audioData:(NSData *)audioData FileName:(NSString *)fileName Parameters:(NSDictionary *)parameters progress:(void (^)(NSProgress *progress))progressBlock  success:(void(^) (BOOL isSuccess, id responseObject))success failure:(void(^) (NSError *error))failure;

#pragma mark - 表单上传文档
+ (void)uploadDocumentWithPostUrl:(NSString *)posturl fileData:(NSData *)fileData FileName:(NSString *)fileName Parameters:(NSDictionary *)parameters progress:(void (^)(NSProgress *progress))progressBlock  success:(void(^) (BOOL isSuccess, id responseObject))success failure:(void(^) (NSError *error))failure;


#pragma mark - POST 上传视频

+ (void)sendPOSTVideoWithPath:(NSString *)path
               widthVideoPath:(NSString *)videoPath
              widthParameters:(NSDictionary *)parameters
                widthProgress:(void(^) (CGFloat progress))upLoadProgress
                      success:(void(^) (BOOL isSuccess, id responseObject))success
                      failure:(void(^) (NSError *error))failure;

#pragma mark DownLoad 文件下载
+ (void)requestDownLoadDataWithPath:(NSString *)path
                     withParameters:(NSDictionary *)parameters
                       withSavePath:(NSString *)savePath
                       withProgress:(void(^) (float progress))downLoadProgress
                            success:(void(^) (BOOL isSuccess, id responstObject))success
                            failure:(void(^) (NSError *error))failure;

#pragma mark - Delete 删除资源

+ (void)requestDELETEDataWithPath:(NSString *)path
                   withParameters:(NSDictionary *)parameters
                          success:(void(^) (BOOL isSuccess, id responseObject))success
                          failure:(void(^) (NSError *error))failure;


#pragma mark - PUT 更新全部属性
+ (void)sendPUTDataWithPath:(NSString *)path
             withParameters:(NSDictionary *)parameters
                    success:(void(^) (BOOL isSuccess, id responseObject))success
                    failure:(void(^) (NSError *error))failure;


#pragma mark - PATCH 改变资源状态或更新部分属性
+ (void)sendPATCHDataWithPath:(NSString *)path
              withPaarameters:(NSDictionary *)parameters
                      success:(void(^) (BOOL isSuccess, id responseObject))success
                      failure:(void(^) (NSError *error))failure;


+ (void)cancelAllNetworkRequest;

+ (void)cancelHttpRequestWithType:(NSString *)type withPath:(NSString *)path;

@end
