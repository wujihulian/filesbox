//
//  VULUploadTool.m
//  VideoULTeacher
//
//  Created by yuekewei on 2020/7/9.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "VULUploadTool.h"

@interface VULUploadTool (){
    AFHTTPSessionManager *_manager;
    NSMutableDictionary<NSNumber *, NSURLSessionDataTask *> *_requestsRecord;
}

@end

@implementation VULUploadTool

+ (instancetype)shareInstance {
    static VULUploadTool *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[VULUploadTool alloc] init];
        
    });
    return instance;
}

- (instancetype)init {
    self = [super init];
    if (self) {
        _requestsRecord = [NSMutableDictionary dictionary];
        
        _manager = [[AFHTTPSessionManager alloc] initWithBaseURL:[NSURL URLWithString:@""]];
        _manager.requestSerializer = [AFJSONRequestSerializer serializer];
        _manager.requestSerializer.cachePolicy = NSURLRequestReloadIgnoringLocalCacheData;
        _manager.requestSerializer.timeoutInterval = 30;
        [_manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
        [_manager.requestSerializer setValue:[VULRealmDBManager getLocalToken] forHTTPHeaderField:@"token"];
        
        AFJSONResponseSerializer *responseSerializer = [AFJSONResponseSerializer serializer];
        responseSerializer.removesKeysWithNullValues = YES;
        _manager.responseSerializer = responseSerializer;

        [_manager.responseSerializer setAcceptableContentTypes:[NSSet setWithObjects:@"application/json", @"text/json", @"text/javascript",@"text/plain",@"text/html",@"text/plain",nil]];

    }
    return self;
}

+ (NSString *)mimeTypeForFileAtPath:(NSString *)path {
    //    if (![[[[NSFileManager alloc] init] autorelease] fileExistsAtPath:path]) {
    //        return nil;
    //    }
    // Borrowed from http://stackoverflow.com/questions/2439020/wheres-the-iphone-mime-type-database
    CFStringRef UTI = UTTypeCreatePreferredIdentifierForTag(kUTTagClassFilenameExtension, (__bridge CFStringRef)[path pathExtension], NULL);
    CFStringRef MIMEType = UTTypeCopyPreferredTagWithClass (UTI, kUTTagClassMIMEType);
    CFRelease(UTI);
    if (!MIMEType) {
        return @"application/octet-stream";
    }
    return (__bridge NSString *)(MIMEType);
}

- (void)uploadDatas:(NSArray *)datas
             params:(nullable NSDictionary *)params
           fileName:(NSString *)fileName
           progress:(nullable void (^)(NSProgress * _Nonnull))progress
         completion:(nullable VULUploadCompletion)completion {
    
}

- (void)uploadData:(NSData *)data
            params:(nullable NSDictionary *)params
          fileName:(NSString *)fileName
          progress:(nullable void (^)(NSProgress * _Nonnull))progress
        completion:(nullable VULUploadCompletion)completion {
    
    NSURLSessionDataTask *task =  [_manager POST:[NSString stringWithFormat:@"%@/api/common/upload", kSchoolServiceUrl]
                                      parameters:params
                       constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        
        NSString *mimeType = [VULUploadTool mimeTypeForFileAtPath:fileName];
        [formData appendPartWithFileData:data name:@"file" fileName:@"file" mimeType:mimeType];
    }
                                        progress:^(NSProgress * _Nonnull uploadProgress) {
        if(uploadProgress){
            progress(uploadProgress);
        }
    }
                                         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        [self handleDataTask:task responseObject:responseObject error:nil completion:completion];
    }
                                         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        [self handleDataTask:task responseObject:nil error:error completion:completion];
    }];
    _requestsRecord[@(task.taskIdentifier)] = task;
}

- (void)handleDataTask:(NSURLSessionDataTask *)task
        responseObject:(id)responseObject
                 error:(NSError *)error completion:(nullable VULUploadCompletion)completion  {
    
    VULUploadModel *model = [VULUploadModel new];
    
    if (!error) {
        model = [VULUploadModel modelWithJSON:responseObject];
        model.success = model.code.integerValue == 200;
    }
    else {
        model.success = NO;
        model.message = @"网络错误,请稍候再试";
    }
    
    if (error) {
        for (NSNumber *taskIdentifier in _requestsRecord) {
            NSURLSessionDataTask *task = _requestsRecord[taskIdentifier];
            [task cancel];
        }
        _requestsRecord = [NSMutableDictionary dictionary];
    }
    else {
        [_requestsRecord removeObjectForKey:@(task.taskIdentifier)];
    }
    
    if (completion) {
        completion(model);
    }
}

@end

@implementation VULUploadRequest



@end


@implementation VULUploadModel


@end
