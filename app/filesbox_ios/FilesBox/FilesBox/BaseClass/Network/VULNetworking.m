//
//  VULNetworking.m
//  VideoULimit
//
//  Created by ZCc on 2018/9/3.
//  Copyright © 2018年 zcc. All rights reserved.
//

#import "VULNetworking.h"
#import "UIImage+Compression.h"
#import <AVFoundation/AVFoundation.h>
#import "VULRealmDBManager.h"
#import "NSData+EXTENSION.h"
@implementation VULNetworking


static VULNetworking *default_ShareManager = nil;

/**
 单例

 @return 网络请求实例
 */
+ (instancetype)shareManager{
    
//    static dispatch_once_t onceToken;
//    dispatch_once(&onceToken, ^{
//        if(default_ShareManager == nil){
//        }
//    });
//
//   解决 切换账号token变化
    default_ShareManager = [[self alloc] initWithBaseURL:[NSURL URLWithString:@""]];

    return default_ShareManager;
}

- (instancetype)initWithBaseURL:(NSURL *)url{
    
    self = [super initWithBaseURL:url];
    if(self){
        //设置请求以及相应的序列化器
        self.requestSerializer = [AFJSONRequestSerializer serializer];
        //设置相应的缓存策略 -- URL应该加载源端数据，不使用本地缓存数据，忽略缓存直接从原始地址下载
        self.requestSerializer.cachePolicy = NSURLRequestReloadIgnoringLocalCacheData;
        //设置超时时间
        self.requestSerializer.timeoutInterval = 30;
        //设置请求内容类型 -- 复杂的参数模型需要使用json传值 - 设置请求内容的类型
        [self.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
        
        NSLog(@"%@", [VULRealmDBManager getLocalToken]);
        [self.requestSerializer setValue:[VULRealmDBManager getLocalToken] forHTTPHeaderField:@"token"];
        
        //默认Response为json
        AFJSONResponseSerializer *responseSerializer = [AFJSONResponseSerializer serializer];
        //在服务器返回json数据的时候经常会出现null数据 json解析的时候 可能会将null 解析成nsnull的对象 我们遇到nsnull对象发送消息的时候就会遇到crash问题
        responseSerializer.removesKeysWithNullValues = YES;
        self.responseSerializer = responseSerializer;
        
#warning 此处可以根据自己的应用需要设置相应的值
        //allowInvalidCertificates 是否允许无效证书（也就是自建证书）， 默认为NO
        //如果是需要自建证书 则设YES
//        self.securityPolicy.allowInvalidCertificates = YES;
        
        //设置apiKey--类似于自己应用中的token -- 此处仅仅作为测试使用
//        [self.requestSerializer setValue:apikey forHTTPHeaderField:@"apiKey"]
        
//        self.responseSerializer.acceptableContentTypes
        //设置接受的类型
//        [self.responseSerializer setAcceptableContentTypes:[NSSet setWithObjects:@"application/json", @"text/json", @"text/javascript",@"text/html",@"text/plain;charset=UTF-8",nil]];
        [self.responseSerializer setAcceptableContentTypes:[NSSet setWithObjects:@"text/plain", @"application/json", @"text/json", @"text/javascript", @"text/html", nil]];
    }
    return self;
}

#pragma mark -GET 请求网络数据

+ (void)requestGETDataWithPath:(NSString *)path
                withParameters:(NSDictionary *)parameters
                  withProgress:(void(^) (float progress))downLoadProgress
                       success:(void(^) (BOOL isSuccess, id responseObject))success
                       failure:(void(^) (NSError *error))failure {
    NSLog(@"%@", parameters);
    NSLog(@"%@", path);
    [[VULNetworking shareManager] GET:path parameters:parameters progress:^(NSProgress * _Nonnull downloadProgress) {
        NSLog(@"downloadProcess = %@", downloadProgress);
        
        if(downloadProgress){
            downLoadProgress(downloadProgress.completedUnitCount / downloadProgress.totalUnitCount);
        }
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"%@", responseObject);
        if(success){
            success(YES, responseObject);
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"errpr = %@", error);
        if(failure){
            failure(error);
        }
    }];
}

#pragma mark - POST 传送网络数据

+ (void)sendPOSTDataWithPath:(NSString *)path
              withParameters:(NSDictionary *)parameters
                withProgress:(void(^) (float progress))upLoadProgress
                     success:(void(^) (BOOL isSuccess, id responseObject))success
                     failure:(void(^) (NSError *error))failure{
    [[VULNetworking shareManager] POST:path parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
        NSLog(@"uploadProgress:%@", uploadProgress);
        if(uploadProgress){
            upLoadProgress(uploadProgress.completedUnitCount / uploadProgress.totalUnitCount);
        }
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"responseObject:\n%@", responseObject);
        
        if(success){
            success(YES, responseObject);
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"error%@", error);
        if(failure){
            failure(error);
        }
    }];
}

#pragma mark = POST 上传图片

+ (void)sendPostImageWithPath:(NSString *)path
               withParameters:(NSDictionary *)parameters
               withImageArray:(NSArray *)imageArray
              withTargetWidth:(CGFloat)width
                 withProgress:(void(^) (NSProgress *progress))upLoadprogress
                      success:(void(^) (BOOL isSuccess, id responseObject))success
                      failure:(void(^) (NSError *error))failure{
    
    
    [[VULNetworking shareManager] POST:path parameters:parameters constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        NSUInteger i = 0;
        
        //上传图片时 为了用户体验或者考虑到性能 要进行压缩
        if(imageArray.count == 1){
            UIImage *image = imageArray.lastObject;
            UIImage *resizedImage = [UIImage imageCompressed:imageArray.firstObject withdefineWidth:width > 0 ? width : image.size.width];
            NSData *imageData = UIImageJPEGRepresentation(resizedImage, 0.5);
            NSLog(@"%@", [NSString stringWithFormat:@"upImage.%@",[NSData typeForImageData:imageData]]);
            [formData appendPartWithFileData:imageData name:@"file" fileName:[NSString stringWithFormat:@"upImage.%@",[NSData typeForImageData:imageData]] mimeType:@"image/png"];
        }else{
            for (UIImage *image in imageArray){
                //压缩图片，指定宽度
                UIImage *resizedImage = [UIImage imageCompressed:image withdefineWidth:width > 0 ? width : image.size.width];
                NSData *imageData = UIImageJPEGRepresentation(resizedImage, 0.5);
                [formData appendPartWithFileData:imageData name:@"file" fileName:[NSString stringWithFormat:@"picfile%ld.%@", (long)i, [NSData typeForImageData:imageData]] mimeType:@"image/png"];
                i++;
            }
        }
    } progress:^(NSProgress * _Nonnull uploadProgress) {
        NSLog(@"uploadProgress: %@", uploadProgress);
        if(uploadProgress){
            upLoadprogress(uploadProgress);
        }
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"responseObject = %@", responseObject);
        if(success){
            success(YES, responseObject);
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"error = %@", error);
        if(failure){
            failure(error);
        }
    }];
}
#pragma mark = POST 上传不压缩图片
+ (void)sendPostClearImageWithPath:(NSString *)path
               withParameters:(NSDictionary *)parameters
               withImageArray:(NSArray *)imageArray
              withTargetWidth:(CGFloat)width
                 withProgress:(void(^) (NSProgress *progress))upLoadprogress
                      success:(void(^) (BOOL isSuccess, id responseObject))success
                           failure:(void(^) (NSError *error))failure{
    [[VULNetworking shareManager] POST:path parameters:parameters constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        NSUInteger i = 0;
        
        //上传图片时 为了用户体验或者考虑到性能 要进行压缩
        if(imageArray.count == 1){
            UIImage *image = imageArray.lastObject;
//            UIImage *resizedImage = [UIImage imageCompressed:imageArray.firstObject withdefineWidth:width > 0 ? width : image.size.width];
            NSData *imageData = UIImageJPEGRepresentation(image, 1);
            NSLog(@"%@", [NSString stringWithFormat:@"upImage.%@",[NSData typeForImageData:imageData]]);
            [formData appendPartWithFileData:imageData name:@"file" fileName:[NSString stringWithFormat:@"upImage.%@",[NSData typeForImageData:imageData]] mimeType:@"image/png"];
        }else{
            for (UIImage *image in imageArray){
                //压缩图片，指定宽度
//                UIImage *resizedImage = [UIImage imageCompressed:image withdefineWidth:width > 0 ? width : image.size.width];
                NSData *imageData = UIImageJPEGRepresentation(image, 1);
                [formData appendPartWithFileData:imageData name:@"file" fileName:[NSString stringWithFormat:@"picfile%ld.%@", (long)i, [NSData typeForImageData:imageData]] mimeType:@"image/png"];
                i++;
            }
        }
    } progress:^(NSProgress * _Nonnull uploadProgress) {
        NSLog(@"uploadProgress: %@", uploadProgress);
        if(uploadProgress){
            upLoadprogress(uploadProgress);
        }
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"responseObject = %@", responseObject);
        if(success){
            success(YES, responseObject);
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"error = %@", error);
        if(failure){
            failure(error);
        }
    }];
}

#pragma mark - 表单上传视频文件
+ (void)uploadVideoWithPostUrl:(NSString *)posturl VideoData:(NSData *)videoData FileName:(NSString *)videoName Parameters:(NSDictionary *)parameters progress:(void (^)(NSProgress *progress))progressBlock  success:(void(^) (BOOL isSuccess, id responseObject))success failure:(void(^) (NSError *error))failure {
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    //设置响应文件类型为JSON类型
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    manager.requestSerializer = [AFHTTPRequestSerializer serializer];
    manager.responseSerializer.acceptableContentTypes = nil;
    //设置timeout
    [manager.requestSerializer setTimeoutInterval:20.0];
    //设置请求头类型
    [manager.requestSerializer setValue:@"form/data" forHTTPHeaderField:@"Content-Type"];
    NSString *token = [VULRealmDBManager getLocalToken];
    [manager.requestSerializer setValue:token forHTTPHeaderField:@"token"];
    
    [manager POST:posturl parameters:parameters constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        [formData appendPartWithFileData:videoData name:@"file" fileName:videoName mimeType:@"video/mp4"];
        
    } progress:^(NSProgress * _Nonnull uploadProgress) {
        NSLog(@"上传进度: %f", uploadProgress.fractionCompleted);
        if (progressBlock) {
            progressBlock(uploadProgress);
        }
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"成功返回: %@", responseObject);
        if (success) {
            success(YES, responseObject);
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"上传失败: %@", error);
        NSData *data = error.userInfo[@"com.alamofire.serialization.response.error.data"];
        NSString *str = [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding];
        NSLog(@"服务器的错误原因:%@", str);
        if (failure) {
            failure(error);
        }
    }];
    
    
//    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
//    //设置响应文件类型为JSON类型
//    manager.responseSerializer = [AFJSONResponseSerializer serializer];
//    manager.requestSerializer = [AFHTTPRequestSerializer serializer];
//    manager.responseSerializer.acceptableContentTypes = nil;
//    //设置timeout
//    [manager.requestSerializer setTimeoutInterval:20.0];
//    //设置请求头类型
//    [manager.requestSerializer setValue:@"form/data" forHTTPHeaderField:@"Content-Type"];
//    NSString *token = [VULRealmDBManager getLocalToken];
//    [manager.requestSerializer setValue:token forHTTPHeaderField:@"token"];
//
//    [manager POST:posturl parameters:parameters constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
//        [formData appendPartWithFileData:videoData name:@"file" fileName:videoName mimeType:@"video/mp4"];
//
//    } progress:^(NSProgress * _Nonnull uploadProgress) {
//        NSLog(@"上传进度: %f", uploadProgress.fractionCompleted);
//        if (progressBlock) {
//            progressBlock(uploadProgress);
//        }
//    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
//        NSLog(@"成功返回: %@", responseObject);
//        if (success) {
//            success(YES, responseObject);
//        }
//    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
//        NSLog(@"上传失败: %@", error);
//        NSData * data = error.userInfo[@"com.alamofire.serialization.response.error.data"];
//        NSString * str = [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding];
//        NSLog(@"服务器的错误原因:%@",str);
//        if (failure) {
//            failure(error);
//        }
//    }];
}


#pragma mark - 表单上传音频文件
+ (void)uploadAudioWithPostUrl:(NSString *)posturl audioData:(NSData *)audioData FileName:(NSString *)fileName Parameters:(NSDictionary *)parameters progress:(void (^)(NSProgress *progress))progressBlock  success:(void(^) (BOOL isSuccess, id responseObject))success failure:(void(^) (NSError *error))failure {
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    //设置响应文件类型为JSON类型
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    manager.requestSerializer = [AFHTTPRequestSerializer serializer];
    manager.responseSerializer.acceptableContentTypes = nil;
    //设置timeout
    [manager.requestSerializer setTimeoutInterval:20.0];
    //设置请求头类型
    [manager.requestSerializer setValue:@"form/data" forHTTPHeaderField:@"Content-Type"];
    NSString *token = [VULRealmDBManager getLocalToken];
    [manager.requestSerializer setValue:token forHTTPHeaderField:@"token"];
    
    [manager POST:posturl parameters:parameters constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        [formData appendPartWithFileData:audioData name:@"file" fileName:fileName mimeType:@"audio/mp3"];
        
    } progress:^(NSProgress * _Nonnull uploadProgress) {
        NSLog(@"上传进度: %f", uploadProgress.fractionCompleted);
        if (progressBlock) {
            progressBlock(uploadProgress);
        }
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"成功返回: %@", responseObject);
        if (success) {
            success(YES, responseObject);
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"上传失败: %@", error);
        NSData * data = error.userInfo[@"com.alamofire.serialization.response.error.data"];
        NSString * str = [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding];
        NSLog(@"服务器的错误原因:%@",str);
        if (failure) {
            failure(error);
        }
    }];
}

#pragma mark - 表单上传文档
+ (void)uploadDocumentWithPostUrl:(NSString *)posturl fileData:(NSData *)fileData FileName:(NSString *)fileName Parameters:(NSDictionary *)parameters progress:(void (^)(NSProgress *progress))progressBlock  success:(void(^) (BOOL isSuccess, id responseObject))success failure:(void(^) (NSError *error))failure {
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    //设置响应文件类型为JSON类型
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    manager.requestSerializer = [AFHTTPRequestSerializer serializer];
    manager.responseSerializer.acceptableContentTypes = nil;
    //设置timeout
    [manager.requestSerializer setTimeoutInterval:20.0];
    //设置请求头类型
    [manager.requestSerializer setValue:@"form/data" forHTTPHeaderField:@"Content-Type"];
    NSString *token = [VULRealmDBManager getLocalToken];
    [manager.requestSerializer setValue:token forHTTPHeaderField:@"token"];
    
    
    NSString *mimeTypeStr;
    if ([fileName hasSuffix:@"docx"] || [fileName hasSuffix:@"doc"]) {
        mimeTypeStr = @"application/vnd.sealed.doc";
    } else if ([fileName hasSuffix:@"pptx"] || [fileName hasSuffix:@"ppt"]) {
        mimeTypeStr = @"application/vnd.sealed.ppt";
    } else if ([fileName hasSuffix:@"pdf"]) {
        mimeTypeStr = @"application/pdf";
    } else if ([fileName hasSuffix:@"xlsx"] || [fileName hasSuffix:@"xls"]) {
        mimeTypeStr = @"application/vnd.sealed.xls";
    }
    
    [manager POST:posturl parameters:parameters constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        [formData appendPartWithFileData:fileData name:@"file" fileName:fileName mimeType:mimeTypeStr];
        
    } progress:^(NSProgress * _Nonnull uploadProgress) {
        NSLog(@"上传进度: %f", uploadProgress.fractionCompleted);
        if (progressBlock) {
            progressBlock(uploadProgress);
        }
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"成功返回: %@", responseObject);
        if (success) {
            success(YES, responseObject);
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"上传失败: %@", error);
        NSData * data = error.userInfo[@"com.alamofire.serialization.response.error.data"];
        NSString * str = [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding];
        NSLog(@"服务器的错误原因:%@",str);
        if (failure) {
            failure(error);
        }
    }];
}




#pragma mark - POST 上传视频

+ (void)sendPOSTVideoWithPath:(NSString *)path
               widthVideoPath:(NSString *)videoPath
              widthParameters:(NSDictionary *)parameters
                widthProgress:(void(^) (CGFloat progress))upLoadProgress
                      success:(void(^) (BOOL isSuccess, id responseObject))success
                      failure:(void(^) (NSError *error))failure{
    //获取视频资源
    AVURLAsset *avUrlAsset = [AVURLAsset assetWithURL:[NSURL URLWithString:videoPath]];
    
//    AVF_EXPORT NSString *const AVAssetExportPreset640x480           NS_AVAILABLE(10_7, 4_0);
//    AVF_EXPORT NSString *const AVAssetExportPreset960x540           NS_AVAILABLE(10_7, 4_0);
//    AVF_EXPORT NSString *const AVAssetExportPreset1280x720          NS_AVAILABLE(10_7, 4_0);
//    AVF_EXPORT NSString *const AVAssetExportPreset1920x1080         NS_AVAILABLE(10_7, 5_0);
//    AVF_EXPORT NSString *const AVAssetExportPreset3840x2160         NS_AVAILABLE(10_10, 9_0);
    
    AVAssetExportSession *avAssetExport = [[AVAssetExportSession alloc] initWithAsset:avUrlAsset presetName:AVAssetExportPreset640x480];
    //获取上传的时间
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd-HH:mm:ss"];
    //转化后直接写入Library -- caches
    NSString *videoWritePath = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES).firstObject stringByAppendingString:[NSString stringWithFormat:@"/output-%@.mp4", [formatter stringFromDate:[NSDate date]]]];
    avAssetExport.outputURL = [NSURL URLWithString:videoWritePath];
    avAssetExport.outputFileType = AVFileTypeMPEG4;
    [avAssetExport exportAsynchronouslyWithCompletionHandler:^{
        switch ([avAssetExport status]) {
            case AVAssetExportSessionStatusCompleted:{
                [[VULNetworking shareManager] POST:path parameters:parameters constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
                    [formData appendPartWithFileURL:[NSURL fileURLWithPath:videoWritePath] name:@"write you want to write" error:nil];
                } progress:^(NSProgress * _Nonnull uploadProgress) {
                    NSLog(@"upLoadprogress:%@", uploadProgress);
                    if(uploadProgress){
                        upLoadProgress(uploadProgress.completedUnitCount / uploadProgress.totalUnitCount);
                    }
                } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
                    NSLog(@"responseObject:\n%@", responseObject);
                    if(success){
                        success(YES, responseObject);
                    }
                } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
                    if(failure){
                        failure(error);
                    }
                }];
            }
                break;
            default:
                break;
        }
    }];
}

#pragma mark - DOWNLOAD 文件下载

+ (void)requestDownLoadDataWithPath:(NSString *)path
                    withParameters:(NSDictionary *)parameters
                       withSavePath:(NSString *)savePath
                       withProgress:(void(^) (float progress))downLoadProgress
                            success:(void(^) (BOOL isSuccess, id responstObject))success
                            failure:(void(^) (NSError *error))failure{
    [[VULNetworking shareManager] downloadTaskWithRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:path]] progress:^(NSProgress * _Nonnull downloadProgress) {
        
        NSLog(@"downloadProcess = %@", downloadProgress);
        if(downloadProgress){
            downLoadProgress(downloadProgress.completedUnitCount / downloadProgress.totalUnitCount);
        }
    } destination:^NSURL * _Nonnull(NSURL * _Nonnull targetPath, NSURLResponse * _Nonnull response) {
        return [NSURL URLWithString:savePath];
    } completionHandler:^(NSURLResponse * _Nonnull response, NSURL * _Nullable filePath, NSError * _Nullable error) {
        if(error){
            failure(error);
        }
    }];
}

#pragma mark - Delete 删除资源

+ (void)requestDELETEDataWithPath:(NSString *)path
                   withParameters:(NSDictionary *)parameters
                          success:(void(^) (BOOL isSuccess, id responseObject))success
                          failure:(void(^) (NSError *error))failure{
    [[VULNetworking shareManager] DELETE:path parameters:parameters success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"responseObject:\n%@", responseObject);
        if(success){
            success(YES, success);
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"failure : %@", error);
        if(failure){
            failure(error);
        }
    }];
}

#pragma mark - PUT 更新全部属性
+ (void)sendPUTDataWithPath:(NSString *)path
             withParameters:(NSDictionary *)parameters
                    success:(void(^) (BOOL isSuccess, id responseObject))success
                    failure:(void(^) (NSError *error))failure{
    [[VULNetworking shareManager] PUT:path parameters:parameters success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"responseObject:\n%@", responseObject);
        if(success){
            success(YES, success);
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"failure : %@", error);
        if(failure){
            failure(error);
        }
    }];
}

#pragma mark - PATCH 改变资源状态或更新部分属性
+ (void)sendPATCHDataWithPath:(NSString *)path
              withPaarameters:(NSDictionary *)parameters
                      success:(void(^) (BOOL isSuccess, id responseObject))success
                      failure:(void(^) (NSError *error))failure{
    [[VULNetworking shareManager] PATCH:path parameters:parameters success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"responseObject:\n%@", responseObject);
        if(success){
            success(YES, success);
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"failure : %@", error);
        if(failure){
            failure(error);
        }
    }];
}

+ (void)cancelAllNetworkRequest{
    [[VULNetworking shareManager].operationQueue cancelAllOperations];
}

+ (void)cancelHttpRequestWithType:(NSString *)type withPath:(NSString *)path{
    
    NSError *error;
    //根据请求类型 以及 请求的url 创建一个nsmutableurlRequest --- 通过该url去匹配请求队列中是否有该url 如果有的话 就取消这个operation
    NSString *urlToBeCanceled = [[[[VULNetworking shareManager].requestSerializer requestWithMethod:type URLString:path parameters:nil error:&error] URL] path];
    
    for (NSOperation *operation in [VULNetworking shareManager].operationQueue.operations) {
        //如果是请求队列
        if([operation isKindOfClass:[NSURLSessionTask class]]){
            //请求类型匹配
            BOOL hasMatchRequestType = [type isEqualToString:[[(NSURLSessionTask *)operation currentRequest] HTTPMethod]];
            
            //url匹配
            BOOL hasMatchRequestUrlString = [urlToBeCanceled isEqualToString:[[[(NSURLSessionTask *)operation currentRequest] URL] path]];
            
            if(hasMatchRequestType && hasMatchRequestUrlString){
                [operation cancel];
            }
        }
    }
}


@end
