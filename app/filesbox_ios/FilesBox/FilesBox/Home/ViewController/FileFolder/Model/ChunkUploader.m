//
//  UploadManager.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/28.
//

#import "ChunkUploader.h"
#import "ICFaceManager.h"
@implementation DownloadProgress

+ (instancetype)sharedInstance {
    static DownloadProgress *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
        instance.progressDict = [NSMutableDictionary dictionary];
        instance.progressData =  [NSMutableDictionary dictionary];;
        NSArray *emotions = [ICFaceManager emotions];
        NSMutableDictionary *dic = [NSMutableDictionary new];
        for (XZEmotion *emotion in emotions) {
            NSString *imagePath = nil;
            NSBundle *imageBundle =  [NSBundle bundleWithPath:[[NSBundle mainBundle] pathForResource:@"Emotion" ofType:@"bundle"]];
            NSArray *exts = @[@"png", @"jpeg", @"jpg", @"gif", @"webp", @"apng"];
            for (NSString *type in exts) {
                imagePath = [imageBundle pathForScaledResource:[emotion.face_name substringWithRange:NSMakeRange(1, emotion.face_name.length - 2)] ofType:type];
                if (imagePath) {
                    break;
                }
            }
            if (imagePath) {
                [dic setObject:[YYImage imageWithContentsOfFile:imagePath] forKey:[emotion.face_name substringWithRange:NSMakeRange(1, emotion.face_name.length - 2)]];
            }
        }
        instance.emotionDic = [NSDictionary dictionaryWithDictionary:dic];
    });
    return instance;
}
- (void)setProgressDict:(NSMutableDictionary<NSString *,NSNumber *> *)progressDict {
    _progressDict = progressDict;
    
}
-(void)setProgressData:(NSMutableDictionary *)progressData{
    _progressData= progressData;
}




@end
@implementation UBUploadModel
@end
// ChunkUploader.m
@interface ChunkUploader ()

@property (nonatomic, assign) UploadStatus uploadStatus;

@end

@implementation ChunkUploader
+ (instancetype)sharedManager {
    static ChunkUploader *sharedManager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedManager = [[self alloc] init];
        sharedManager.unuploadedPhotos = [NSMutableArray array];
        NSMutableArray *arr = [NSMutableArray arrayWithArray:[NSArray bg_arrayWithName:@"uploadedIdentifiers"]];
        sharedManager.uploadedIdentifiers = [NSMutableArray array];
        for(NSDictionary *dic in arr){
            [sharedManager.uploadedIdentifiers addObject:dic[@"localIdentifier"]];
        }
    });
    return sharedManager;
}
-(NSMutableDictionary *)changeModelDic{
    if (!_changeModelDic) {
        _changeModelDic = [NSMutableDictionary dictionary];
    }
    return _changeModelDic;
}

+(void)uploadFileAtModel:(UBUploadModel *)model
                progress:(ChunkUploaderProgressBlock)progressBlock
                   speed:(ChunkUploaderSpeedBlock)speedBlock
                 success:(ChunkUploaderSuccessBlock)successBlock
                 failure:(ChunkUploaderFailureBlock)failureBlock {
    NSURL *fileURL = [NSURL fileURLWithPath:filePath(model.filePath)];
    NSData *fileData =model.data?model.data: [NSData dataWithContentsOfURL:fileURL];
    
    __block NSInteger fileSize =  fileData.length;
    model.fileSize = fileSize;
    
    //    /api/disk/upload/check
    NSString *md5Str = [NSString getMD5WithFilePath:fileURL];
    
    NSString *posturl = [NSString stringWithFormat:@"%@%@",ChooseUrl,@"api/disk/upload"];
    NSMutableArray *dataArray = [NSMutableArray arrayWithArray:[ChunkUploader getDataArrWithModel:model]];
    
    NSString *fileMD5Path = [NSString stringWithFormat:@"upload/%@",md5Str];
    
    
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        __block NSError *error;
        __block id result;
        __block BOOL success = NO;
        dispatch_semaphore_t subSemaphore = dispatch_semaphore_create(0);
        for (int i =   model.yetChunk; i < dataArray.count; i++) {
            dispatch_async(dispatch_get_global_queue(0, 0), ^{
                model.yetChunk = i;
                if (model.status == UploadStatusPaused) {
                    model.isStop = YES;
                    saveUploadFileWithModel(model);
                    return;
                }
                model.isStop = NO;
                NSString* str = filePath([NSString stringWithFormat:@"%@/%@%d",fileMD5Path,md5Str,i]);
                NSData *resultData = [NSData dataWithContentsOfFile:str];
                if ([resultData isKindOfClass:[NSData class]]) {
                    
                    NSMutableDictionary *params = [NSMutableDictionary dictionary];
                    [params setObject:model.busType forKey:@"busType"];
                    [params setObject:md5Str forKey:@"hashMd5"];
                    [params setObject:[NSString stringWithFormat:@"%d", i] forKey:@"chunk"];
                    if (model.pathId.length>0 && model.pathId) {
                        [params setObject:model.pathId forKey:@"path"];
                    }
                    
                    [params setObject:model.sourceID forKey:@"sourceID"];
                    [params setObject:@"2097152" forKey:@"chunkSize"];
                    [params setObject:[NSString stringWithFormat:@"%zd", dataArray.count] forKey:@"chunks"];
                    
                    
                    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
                    //设置响应文件类型为JSON类型
                    manager.responseSerializer = [AFJSONResponseSerializer serializer];
                    manager.requestSerializer = [AFHTTPRequestSerializer serializer];
                    manager.responseSerializer.acceptableContentTypes = nil;
                    //设置timeout
                    [manager.requestSerializer setTimeoutInterval:40.0];
                    //设置请求头类型
                    [manager.requestSerializer setValue:@"form/data" forHTTPHeaderField:@"Content-Type"];
                    NSString *token = [VULRealmDBManager getLocalToken];
                    [manager.requestSerializer setValue:token forHTTPHeaderField:@"token"];
                    
                    [manager POST:posturl parameters:params constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
                        [formData appendPartWithFileData:resultData name:@"file" fileName:  model.fileName mimeType:[ChunkUploader mimeTypeForFileAtPath:model.filePath]];
                        
                    } progress:^(NSProgress * _Nonnull uploadProgress) {
                        NSLog(@"上传进度: %f", uploadProgress.fractionCompleted);
                        CGFloat downLoadPercent = 0;
                        downLoadPercent = (uploadProgress.completedUnitCount + 2048 * i * 1024) * 1.0 / fileSize;
                        if (downLoadPercent > 1.0) {
                            downLoadPercent = 1.f;
                        }
                        
                        model.speed = downLoadPercent;
                        saveUploadFileWithModel(model);
                        if (progressBlock) {
                            progressBlock(uploadProgress.fractionCompleted);
                        }
                        
                    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
                        BOOL isCompletion = NO;
                        NSString *success1 = responseObject[@"success"];
                        if (![responseObject[@"code"] isEqualToString:@"200"]) {
                            result = responseObject;
                            isCompletion = YES;
                        }
                        if (success1.boolValue) {
                            success = YES;
                            result = responseObject;
                            isCompletion = YES;
                        }
                        if (isCompletion) {
                            NSLog(@"上传结果====%@",responseObject);
                            if (success) {
                                model.dataDic  = [NSMutableDictionary dictionaryWithDictionary:responseObject[@"data"]];
                                if (model.pathId.length>0 && model.pathId) {
                                    [[DownloadProgress sharedInstance].progressData setValue:[NSMutableDictionary dictionaryWithDictionary:responseObject[@"data"]] forKey:model.pathId];
                                }
                                
                            }
                            saveUploadFileWithModel(model);
                            NSLog(@"%@",task.currentRequest.allHTTPHeaderFields);
                            NSLog(@"%@",params);
                        }
                        
                        
                        dispatch_semaphore_signal(subSemaphore);
                    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
                        error = error;
                        dispatch_semaphore_signal(subSemaphore);
                        NSLog(@"上传失败");
                        NSLog(@"%@",task.currentRequest.allHTTPHeaderFields);
                        NSLog(@"%@",params);
                        NSLog(@"error--%@",error);
                    }];
                }
            });
            
            
            dispatch_semaphore_wait(subSemaphore, DISPATCH_TIME_FOREVER);
        }
        dispatch_sync(dispatch_get_main_queue(), ^{
            NSLog(@"%ld",success);
            
            if (success ) {
                if (successBlock) {
                    successBlock(success);
                }
                NSFileManager *fileManager = [NSFileManager defaultManager];
                BOOL isDir = NO;
                BOOL existed = [fileManager fileExistsAtPath:filePath(md5Str) isDirectory:&isDir];
                if ([fileManager removeItemAtPath:filePath(md5Str) error:NULL]) {
                    NSLog(@"Removed successfully");
                }
            }else{
                if (failureBlock) {
                    failureBlock(error);
                }
            }
            model.isSucess = success ;
            saveUploadFileWithModel(model);
            
        });
    });
}
+(NSMutableArray *)getDataArrWithModel:(UBUploadModel *)model{
    NSURL *fileURL = [NSURL fileURLWithPath:filePath(model.filePath)];
    NSData *fileData =model.data?model.data: [NSData dataWithContentsOfURL:fileURL];
    
    __block NSInteger fileSize =  fileData.length;
    //总片数
    NSInteger chunks = (fileSize % 1024 == 0) ? ((int)(fileSize / (1024 * 1024 * 2))) : ((int)(fileSize / (1024 * 1024 * 2) + 1));
    
    
    
    //    /api/disk/upload/check
    NSString *md5Str = [NSString getMD5WithFilePath:fileURL];
    
    NSString *posturl = [NSString stringWithFormat:@"%@%@",ChooseUrl,@"api/disk/upload"];
    int offset = 2 * 1024 * 1024;//（每一片的大小是2M）
    
    if (chunks ==0) {
        //        小于2M直接上传
        chunks =1;
        offset = fileSize;
    }
    
    NSData *data;
    NSFileHandle *readHandle = [NSFileHandle fileHandleForReadingFromURL:fileURL error:nil];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    BOOL isDir = NO;
    
    BOOL existed = [fileManager fileExistsAtPath:filePath(@"upload") isDirectory:&isDir];
    if (!existed) {
        NSLog(@"Removed successfully");
        [fileManager createDirectoryAtPath:filePath(@"upload") withIntermediateDirectories:YES attributes:nil error:nil];
    }
    
    
    NSString *fileMD5Path = [NSString stringWithFormat:@"upload/%@",md5Str];
    BOOL existed1 = [fileManager fileExistsAtPath:filePath(fileMD5Path) isDirectory:&isDir];
    NSMutableArray *dataArray = [NSMutableArray arrayWithCapacity:0];
    
    if (!existed1) {
        NSLog(@"Removed successfully");
        
        [fileManager createDirectoryAtPath:filePath(fileMD5Path) withIntermediateDirectories:YES attributes:nil error:nil];
        
        for (int i = 0; i < chunks; i++) {
            @autoreleasepool {
                [readHandle seekToFileOffset:offset * i];
                data = [readHandle readDataOfLength:offset];
                NSString* path = filePath([NSString stringWithFormat:@"%@/%@%d",fileMD5Path,md5Str,i]);
                
                BOOL flag =  [data writeToFile:path atomically:NO];
                if (!flag) {
                    NSLog(@"写入失败");
                }else{
                    NSLog(@"写入成功");
                    
                }
                [dataArray addObject:@(i)];
            }
        }
    }else{
        for (int i = 0; i < chunks; i++) {
            @autoreleasepool {
                [dataArray addObject:@(i)];
            }
        }
        
    }
    return dataArray;
}

+(void)updateUploadFileAtModel:(UBUploadModel *)model{
    [VULQueue executeInGlobalQueue:^{
        [ChunkUploader uploadFileAtModel:model progress:^(CGFloat progress) {
            
        } speed:^(CGFloat speed) {
            
        } success:^(bool sucess) {
            
        } failure:^(NSError *error) {
            
        }];
    }];
}

+(void)writeDataWithModel:(NSURL *)newURL
                 sourceID:(NSString *)sourceID
                   pathId:(NSString *)pathId
                    isPic:(BOOL)pic
                  success:(ChunkUploaderSuccessBlock)successBlock{
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSString * fileName = [newURL lastPathComponent];
        NSData * data = [NSData dataWithContentsOfURL:newURL];
        NSFileManager *fileManager = [NSFileManager defaultManager];
        BOOL isDir = NO;
        BOOL existed = [fileManager fileExistsAtPath:filePath(@"specialData") isDirectory:&isDir];
        if (!existed) {
            NSLog(@"Removed successfully");
            [fileManager createDirectoryAtPath:filePath(@"specialData") withIntermediateDirectories:YES attributes:nil error:nil];
        }
        // 将数据写入文件
        BOOL isPic = pic;
        if (isPic) {
            if ([fileName containsString:@"jpg"]) {
                isPic = NO;
            }
        }
        
        NSString *md5Str = [NSString getMD5WithFilePath:newURL];
        NSArray *fileNameArr = [fileName componentsSeparatedByString:@"."];
        
        BOOL success  = [data writeToFile:filePath([NSString stringWithFormat:@"%@/%@.%@",@"specialData",md5Str,(isPic?@"jpg":[fileNameArr lastObject])]) atomically:YES];
        if (success) {
            //                    先把文件写到沙河
            ChunkUploader *upload = [ChunkUploader sharedManager];
            UBUploadModel *uploadModel = [UBUploadModel new];
            uploadModel.fileType =[fileNameArr lastObject];
            NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
            [formatter setDateFormat:@"yyyy_MM_dd_HH_mm_ss"];
            NSString *key = [formatter stringFromDate:[NSDate date]];
            uploadModel.fileName = isPic? [NSString stringWithFormat:@"%@.jpg",key]:fileName;
            uploadModel.filePath =[NSString stringWithFormat:@"%@/%@.%@",@"specialData",md5Str,(isPic?@"jpg":[fileNameArr lastObject])] ;
            uploadModel.isPic = isPic;
            uploadModel.busType = @"cloud";
            uploadModel.fileSize = data.length;
            uploadModel.sourceID = sourceID;
            uploadModel.pathId = pathId;
            uploadModel.fileType = (isPic?@"jpg":[fileNameArr lastObject]);
            
            saveUploadFileWithModel(uploadModel);
            [upload checkFileAtModel:uploadModel progress:^(CGFloat progress) {} speed:^(CGFloat speed) {
            } success:^(BOOL sus) {
                if (successBlock) {
                    successBlock(YES);
                }
                
            } failure:^(NSError *error) {
                if (successBlock) {
                    successBlock(NO);
                }
            }];
        } else {
            NSLog(@"数据写入文件失败！");
        }
    });
}
+(void)writeDataWithModel:(NSURL *)newURL
                 sourceID:(NSString *)sourceID
                    isPic:(BOOL)pic
                  success:(ChunkUploaderSuccessBlock)successBlock{
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSString * fileName = [newURL lastPathComponent];
        NSData * data = [NSData dataWithContentsOfURL:newURL];
    NSFileManager *fileManager = [NSFileManager defaultManager];
        BOOL isDir = NO;
        BOOL existed = [fileManager fileExistsAtPath:filePath(@"specialData") isDirectory:&isDir];
        if (!existed) {
            NSLog(@"Removed successfully");
            [fileManager createDirectoryAtPath:filePath(@"specialData") withIntermediateDirectories:YES attributes:nil error:nil];
        }
        // 将数据写入文件
        BOOL isPic = pic;
        if (isPic) {
            if ([fileName containsString:@"jpg"]) {
                isPic = NO;
            }
        }
        
        NSString *md5Str = [NSString getMD5WithFilePath:newURL];
        NSArray *fileNameArr = [fileName componentsSeparatedByString:@"."];
        
        BOOL success  = [data writeToFile:filePath([NSString stringWithFormat:@"%@/%@.%@",@"specialData",md5Str,(isPic?@"jpg":[fileNameArr lastObject])]) atomically:YES];
        if (success) {
            //                    先把文件写到沙河
            ChunkUploader *upload = [ChunkUploader sharedManager];
            UBUploadModel *uploadModel = [UBUploadModel new];
            uploadModel.fileType =[fileNameArr lastObject];
            NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
            [formatter setDateFormat:@"yyyy_MM_dd_HH_mm_ss"];
            NSString *key = [formatter stringFromDate:[NSDate date]];
            uploadModel.fileName = isPic? [NSString stringWithFormat:@"%@.jpg",key]:fileName;
            uploadModel.filePath =[NSString stringWithFormat:@"%@/%@.%@",@"specialData",md5Str,(isPic?@"jpg":[fileNameArr lastObject])] ;
            uploadModel.isPic = isPic;
            uploadModel.busType = @"cloud";
            uploadModel.fileSize = data.length;
            uploadModel.sourceID = sourceID;
            uploadModel.fileType = (isPic?@"jpg":[fileNameArr lastObject]);
            
            saveUploadFileWithModel(uploadModel);
            [upload checkFileAtModel:uploadModel progress:^(CGFloat progress) {} speed:^(CGFloat speed) {
            } success:^(BOOL sus) {
                if (successBlock) {
                    successBlock(YES);
                }
                
            } failure:^(NSError *error) {
                if (successBlock) {
                    successBlock(NO);
                }
            }];
        } else {
            NSLog(@"数据写入文件失败！");
        }
    });
}

-(void)checkFileAtModel:(UBUploadModel *)model  progress:(ChunkUploaderProgressBlock)progressBlock
                  speed:(ChunkUploaderSpeedBlock)speedBlock
                success:(ChunkUploaderSuccessBlock)successBlock
                failure:(ChunkUploaderFailureBlock)failureBlock{
    NSURL *fileURL = [NSURL fileURLWithPath:filePath(model.filePath)];
    NSData *fileData = [NSData dataWithContentsOfURL:fileURL];
    
    __block NSInteger fileSize = model.data?model.data.length: fileData.length;
    
    saveUploadFileWithModel(model);
    NSString *md5Str = [NSString getMD5WithFilePath: fileURL];
    NSDictionary *dic =@{
        @"busType":model.busType,
        @"hashMd5": md5Str,
        @"name": model.isPic ?    [fileURL.lastPathComponent stringByAppendingString:@".jpg"]: fileURL.lastPathComponent,
        @"size": [NSString stringWithFormat:@"%ld", fileSize],
        @"sourceID":model.sourceID
    };
    if (model.pathId && model.pathId.length>0) {
        dic =@{
            @"busType":model.busType,
            @"hashMd5": md5Str,
            @"name": model.isPic ?    [fileURL.lastPathComponent stringByAppendingString:@".jpg"]: fileURL.lastPathComponent,
            @"size": [NSString stringWithFormat:@"%ld", fileSize],
            @"sourceID":model.sourceID,
            @"path":model.pathId
            
            
        };
    }
    [VULBaseRequest requestWithUrl:@"/api/disk/upload/check"
                            params:dic
                       requestType:YTKRequestMethodGET
                        completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
            
            NSString *fileExists =request.responseObject[@"data"][@"fileExists"];
            if (fileExists.boolValue) {
                if (successBlock) {
                    successBlock(YES);
                }
                model.isSucess = YES;
                model.dataDic  = [NSMutableDictionary dictionaryWithDictionary:request.data];
                saveUploadFileWithModel(model);
            }else{
                NSMutableArray *chunkListArr =request.responseObject[@"data"][@"chunkList"];
                model.yetChunk =chunkListArr.count;
                model.status =  UploadStatusUploading;
                [ChunkUploader uploadFileAtModel:model progress:^(CGFloat progress) {
                    if (progressBlock) {
                        progressBlock(progress);
                    }
                } speed:^(CGFloat speed) {
                    if (speedBlock) {
                        speedBlock(speed);
                    }
                } success:^(bool sucess) {
                    if (successBlock) {
                        successBlock(sucess);
                    }
                } failure:^(NSError *error) {
                    if (failureBlock) {
                        failureBlock(error);
                    }
                }];
            }
        } else {
            if (successBlock) {
                successBlock(YES);
            }
            model.isSucess = YES;
            saveUploadFileWithModel(model);
        }
    }];
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

-(BOOL)isBackUp{
//    
//    BOOL isCose =  [[NSUserDefaults standardUserDefaults]boolForKey:@"isClose"];
//    if(isCose){
//        return NO;
//
//    }
    [(AppDelegate *)[UIApplication sharedApplication].delegate networkChange];
    NSMutableArray *backupArr = [NSMutableArray arrayWithArray:[NSArray bg_arrayWithName:@"backInfo"]];
    if(backupArr.count == 0){
        return NO;
    }
    NSDictionary *dic = backupArr[0];
    int netWorkStatesCode=  ((AppDelegate *)[UIApplication sharedApplication].delegate).netWorkStatesCode;
    NSString *wifi = [NSString stringWithFormat:@"%@",dic[@"wifi"]];
    if(wifi.boolValue && AFNetworkReachabilityStatusReachableViaWiFi != netWorkStatesCode){
        return NO;
    }
    NSString *sourceID = [NSString stringWithFormat:@"%@",dic[@"sourceID"]];
    if(sourceID.integerValue == 0){
        return NO;
    }
    NSString *photo = [NSString stringWithFormat:@"%@",dic[@"photo"]];
    NSString *video = [NSString stringWithFormat:@"%@",dic[@"video"]];
    if(!photo.boolValue && !video.boolValue){
        return NO;
    }
    return YES;
}
-(void)backupFile{
    
    BOOL isCose =  [[NSUserDefaults standardUserDefaults] boolForKey:@"isClose"];
    if(isCose){
        return;

    }


    [(AppDelegate *)[UIApplication sharedApplication].delegate networkChange];
    NSMutableArray *backupArr = [NSMutableArray arrayWithArray:[NSArray bg_arrayWithName:@"backInfo"]];
    if(backupArr.count == 0){
        return;
    }
    NSDictionary *dic = backupArr[0];
    int netWorkStatesCode=  ((AppDelegate *)[UIApplication sharedApplication].delegate).netWorkStatesCode;
    NSString *wifi = [NSString stringWithFormat:@"%@",dic[@"wifi"]];
    if(wifi.boolValue && AFNetworkReachabilityStatusReachableViaWiFi != netWorkStatesCode){
        return;
    }
    NSString *sourceID = [NSString stringWithFormat:@"%@",dic[@"sourceID"]];
    if(sourceID.integerValue == 0){
        return;
    }
    NSString *photo = [NSString stringWithFormat:@"%@",dic[@"photo"]];
    NSString *video = [NSString stringWithFormat:@"%@",dic[@"video"]];
    if(!photo.boolValue && !video.boolValue){
        return;
    }
    // 获取相册中的所有照片
    
    PHAssetMediaType type = PHAssetMediaTypeImage;
    if(photo.boolValue){
        type = PHAssetMediaTypeImage;
    }
    if(video.boolValue){
        type = PHAssetMediaTypeVideo;
    }
    [self.unuploadedPhotos  removeAllObjects];

    if(video.boolValue && photo.boolValue ){
        PHFetchResult *allPhotos = [PHAsset fetchAssetsWithMediaType:PHAssetMediaTypeImage options:nil];
        // 遍历所有照片
        [allPhotos enumerateObjectsUsingBlock:^(PHAsset *asset, NSUInteger idx, BOOL *stop) {
            // 判断照片是否是新的
            if (![self.uploadedIdentifiers containsObject:asset.localIdentifier]) {
                NSLog(@"New photo: %@", asset.localIdentifier);
                
                // 将照片加入到未上传的照片数组中
                [self.unuploadedPhotos addObject:asset];
            }
        }];
        
        PHFetchResult *allPhotos1 = [PHAsset fetchAssetsWithMediaType:PHAssetMediaTypeVideo options:nil];
        // 遍历所有照片
        [allPhotos1 enumerateObjectsUsingBlock:^(PHAsset *asset, NSUInteger idx, BOOL *stop) {
            // 判断照片是否是新的
            if (![self.uploadedIdentifiers containsObject:asset.localIdentifier]) {
                NSLog(@"New photo: %@", asset.localIdentifier);
                
                // 将照片加入到未上传的照片数组中
                [self.unuploadedPhotos addObject:asset];
            }
        }];
    }else{
        PHFetchResult *allPhotos = [PHAsset fetchAssetsWithMediaType:type options:nil];
        // 遍历所有照片
        [allPhotos enumerateObjectsUsingBlock:^(PHAsset *asset, NSUInteger idx, BOOL *stop) {
            // 判断照片是否是新的
            if (![self.uploadedIdentifiers containsObject:asset.localIdentifier]) {
                NSLog(@"New photo: %@", asset.localIdentifier);
                
                // 将照片加入到未上传的照片数组中
                [self.unuploadedPhotos addObject:asset];
            }
        }];
    }

    [self startUpload];
}

- (void)startUpload {
    BOOL isCose =  [[NSUserDefaults standardUserDefaults]boolForKey:@"isClose"];
    if(isCose){
        return;

    }
    self.isUploadModel = [UBUploadModel new];
    self.isUploadModel.speed = 0.0;
    if (self.unuploadedPhotos.count == 0) {
        [VULNotificationCenter postNotificationName:@"backUpViewNotificationCenter" object:nil];

        
        return;
    }
    self.isUploadModel.isOpen = NO;
    [VULNotificationCenter postNotificationName:@"backUpViewNotificationCenter" object:nil];

    PHAsset *asset = [self.unuploadedPhotos firstObject];
    if (asset.mediaType == PHAssetMediaTypeImage) {
        // 获取照片的原始数据
        PHImageRequestOptions *options = [[PHImageRequestOptions alloc] init];
        options.synchronous = YES;
        options.networkAccessAllowed = YES;
        options.resizeMode = PHImageRequestOptionsResizeModeFast;
        [[PHImageManager defaultManager] requestImageDataForAsset:asset options:options resultHandler:^(NSData *imageData, NSString *dataUTI, UIImageOrientation orientation, NSDictionary *info) {
            // 将照片上传到服务器
            UIImage *image = [UIImage imageWithData:imageData];
            if(!image){
                self.isUploadModel.isOpen = YES;

                return;
            }
            [self uploadFileWithPath:asset withImage:image];
        }];
    }else if (asset.mediaType == PHAssetMediaTypeVideo) {
        [[TZImageManager manager] getVideoOutputPathWithAsset:asset success:^(NSString *outputPath) {
            if (!outputPath) {
                [self startUpload];
                return;
            }
           
            [self checkVideoFileWithPath:asset filePath:outputPath ];
            
        } failure:^(NSString *errorMessage, NSError *error) {
          
        }];
    }else{
        [self sucessWithPath:asset];
    }
    
    
}

-(void)checkVideoFileWithPath:(PHAsset *)asset filePath:(NSString *)filePath{
    NSURL *newURL = [NSURL fileURLWithPath:filePath];
    NSString *md5Str = [NSString getMD5WithFilePath:newURL];
    NSData * data = [NSData dataWithContentsOfURL:newURL];
    NSMutableArray *backupArr = [NSMutableArray arrayWithArray:[NSArray bg_arrayWithName:@"backInfo"]];
    NSDictionary *dic1= backupArr[0];
    NSString * fileName = [newURL lastPathComponent];
    self.isUploadModel.isOpen = YES;
    self.isUploadModel.fileSize = data.length;
    NSDictionary *dic =@{
        @"busType":@"cloud",
        @"hashMd5": md5Str,
        @"name": fileName,
        @"size": [NSString stringWithFormat:@"%ld", data.length],
        @"sourceID":dic1[@"sourceID"]
    };
    [VULBaseRequest requestWithUrl:@"/api/disk/upload/check"
                            params:dic
                       requestType:YTKRequestMethodGET
                        completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
            
            NSString *fileExists =request.responseObject[@"data"][@"fileExists"];
            if (fileExists.boolValue) {
                self.isUploadModel.speed = 1;
                [self sucessWithPath:asset];
              
            }else{
                [self uploadVideoFileWithPath:asset filePath:filePath md5:md5Str];
            }
        } else {

        }
    }];
}

-(void)uploadVideoFileWithPath:(PHAsset *)asset filePath:(NSString *)filePath1 md5:(NSString*)md5Str{
    NSURL *newURL = [NSURL fileURLWithPath:filePath1];
    NSString *posturl = [NSString stringWithFormat:@"%@%@",ChooseUrl,@"api/disk/upload"];
    NSData *fileData = [NSData dataWithContentsOfURL:newURL];

    __block NSInteger fileSize =  fileData.length;
    //总片数
    NSInteger chunks = (fileSize % 1024 == 0) ? ((int)(fileSize / (1024 * 1024 * 2))) : ((int)(fileSize / (1024 * 1024 * 2) + 1));
        int offset = 2 * 1024 * 1024;//（每一片的大小是2M）
    
    if (chunks ==0) {
        //        小于2M直接上传
        chunks =1;
        offset = fileSize;
    }
    
    NSData *data;
    NSFileHandle *readHandle = [NSFileHandle fileHandleForReadingFromURL:newURL error:nil];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    BOOL isDir = NO;
    
    BOOL existed = [fileManager fileExistsAtPath:filePath(@"upload") isDirectory:&isDir];
    if (!existed) {
        NSLog(@"Removed successfully");
        [fileManager createDirectoryAtPath:filePath(@"upload") withIntermediateDirectories:YES attributes:nil error:nil];
    }

    NSString *fileMD5Path = [NSString stringWithFormat:@"upload/%@",md5Str];
    BOOL existed1 = [fileManager fileExistsAtPath:filePath(fileMD5Path) isDirectory:&isDir];
    NSMutableArray *dataArray = [NSMutableArray arrayWithCapacity:0];
    
    if (!existed1) {
        NSLog(@"Removed successfully");
        
        [fileManager createDirectoryAtPath:filePath(fileMD5Path) withIntermediateDirectories:YES attributes:nil error:nil];
        
        for (int i = 0; i < chunks; i++) {
            @autoreleasepool {
                [readHandle seekToFileOffset:offset * i];
                data = [readHandle readDataOfLength:offset];
                NSString* path = filePath([NSString stringWithFormat:@"%@/%@%d",fileMD5Path,md5Str,i]);
                
                BOOL flag =  [data writeToFile:path atomically:NO];
                if (!flag) {
                    NSLog(@"写入失败");
                }else{
                    NSLog(@"写入成功");
                    
                }
                [dataArray addObject:@(i)];
            }
        }
    }else{
        for (int i = 0; i < chunks; i++) {
            @autoreleasepool {
                [dataArray addObject:@(i)];
            }
        }
        
    }
    NSMutableArray *backupArr = [NSMutableArray arrayWithArray:[NSArray bg_arrayWithName:@"backInfo"]];
    NSDictionary *dic1= backupArr[0];
    NSString * fileName = [newURL lastPathComponent];
    self.isUploadModel.speed = 0;
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        __block NSError *error;
        __block id result;
        __block BOOL success = NO;
        dispatch_semaphore_t subSemaphore = dispatch_semaphore_create(0);
        for (int i =   0; i < dataArray.count; i++) {
            BOOL isCose =  [[NSUserDefaults standardUserDefaults]boolForKey:@"isClose"];
            if(isCose){
                return;

            }
            dispatch_async(dispatch_get_global_queue(0, 0), ^{
             
                NSString* str = filePath([NSString stringWithFormat:@"%@/%@%d",fileMD5Path,md5Str,i]);
                NSData *resultData = [NSData dataWithContentsOfFile:str];
                if ([resultData isKindOfClass:[NSData class]]) {
                    
                    NSMutableDictionary *params = [NSMutableDictionary dictionary];
                    [params setObject:@"cloud" forKey:@"busType"];
                    [params setObject:md5Str forKey:@"hashMd5"];
                    [params setObject:[NSString stringWithFormat:@"%d", i] forKey:@"chunk"];
                    
                    [params setObject:dic1[@"sourceID"] forKey:@"sourceID"];
                    [params setObject:@"2097152" forKey:@"chunkSize"];
                    [params setObject:[NSString stringWithFormat:@"%zd", dataArray.count] forKey:@"chunks"];
                    
                    
                    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
                    //设置响应文件类型为JSON类型
                    manager.responseSerializer = [AFJSONResponseSerializer serializer];
                    manager.requestSerializer = [AFHTTPRequestSerializer serializer];
                    manager.responseSerializer.acceptableContentTypes = nil;
                    //设置timeout
                    [manager.requestSerializer setTimeoutInterval:40.0];
                    //设置请求头类型
                    [manager.requestSerializer setValue:@"form/data" forHTTPHeaderField:@"Content-Type"];
                    NSString *token = [VULRealmDBManager getLocalToken];
                    [manager.requestSerializer setValue:token forHTTPHeaderField:@"token"];
                    
                    [manager POST:posturl parameters:params constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
                        [formData appendPartWithFileData:resultData name:@"file" fileName:  fileName mimeType:[ChunkUploader mimeTypeForFileAtPath:filePath1]];
                        
                    } progress:^(NSProgress * _Nonnull uploadProgress) {
//                        NSLog(@"上传进度: %f", uploadProgress.fractionCompleted);
                        NSLog(@"上传进度: %f", uploadProgress.fractionCompleted);
                        CGFloat downLoadPercent = 0;
                        downLoadPercent = (uploadProgress.completedUnitCount + 2048 * i * 1024) * 1.0 / fileSize;
                        if (downLoadPercent > 1.0) {
                            downLoadPercent = 1.f;
                        }
                        self.isUploadModel.speed = downLoadPercent;
                        [VULNotificationCenter postNotificationName:@"backUpViewNotificationCenter" object:nil];
                    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
                        BOOL isCompletion = NO;
                        NSString *success1 = responseObject[@"success"];
                        if (![responseObject[@"code"] isEqualToString:@"200"]) {
                            result = responseObject;
                            isCompletion = YES;
                        }
                        if (success1.boolValue) {
                            success = YES;
                            result = responseObject;
                            isCompletion = YES;
                        }
                        if (isCompletion) {
                            NSLog(@"上传结果====%@",responseObject);
                            if (success) {
                               
                                
                            }
                            NSLog(@"%@",task.currentRequest.allHTTPHeaderFields);
                            NSLog(@"%@",params);
                        }
                        
                        
                        dispatch_semaphore_signal(subSemaphore);
                    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
                        error = error;
                        dispatch_semaphore_signal(subSemaphore);
                        NSLog(@"上传失败");
                        NSLog(@"%@",task.currentRequest.allHTTPHeaderFields);
                        NSLog(@"%@",params);
                        NSLog(@"error--%@",error);
                    }];
                }
            });
            
            
            dispatch_semaphore_wait(subSemaphore, DISPATCH_TIME_FOREVER);
        }
        dispatch_sync(dispatch_get_main_queue(), ^{
            NSLog(@"%ld",success);
            
            if (success ) {
                NSFileManager *fileManager = [NSFileManager defaultManager];
                BOOL isDir = NO;
                BOOL existed = [fileManager fileExistsAtPath:filePath(md5Str) isDirectory:&isDir];
                if ([fileManager removeItemAtPath:filePath(md5Str) error:NULL]) {
                    NSLog(@"Removed successfully");
                }
                [self sucessWithPath:asset];
               
            }else{
                [self startUpload];
            }
            
        });
    });

}
-(void)uploadFileWithPath:(PHAsset *)asset withImage:(UIImage *)image{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd-HH:mm:ss"];
    NSString *key = [[formatter stringFromDate:[NSDate date]] stringByAppendingString: [NSString stringWithFormat:@"%ld.jpg",0]];
    
    // 解决图片偏转90度的问题
    if(image.imageOrientation != UIImageOrientationUp) {
        UIGraphicsBeginImageContext(image.size);
        [image drawInRect:CGRectMake(0, 0, image.size.width, image.size.height)];
        image = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
    }
    [[SDImageCache sharedImageCache] storeImage:image forKey:key toDisk:YES completion:^{
        NSString *filePath = [[SDImageCache sharedImageCache] cachePathForKey:key];
        [self checkFileWithPath:asset filePath:filePath];
    }];
}
-(void)checkFileWithPath:(PHAsset *)asset filePath:(NSString *)filePath{
    NSURL *newURL = [NSURL fileURLWithPath:filePath];
    NSString *md5Str = [NSString getMD5WithFilePath:newURL];
    NSData * data = [NSData dataWithContentsOfURL:newURL];
    NSMutableArray *backupArr = [NSMutableArray arrayWithArray:[NSArray bg_arrayWithName:@"backInfo"]];
    NSDictionary *dic1= backupArr[0];
    NSDictionary *dic =@{
        @"busType":@"cloud",
        @"hashMd5": md5Str,
        @"name": [md5Str stringByAppendingString:@".jpg"],
        @"size": [NSString stringWithFormat:@"%ld", data.length],
        @"sourceID":dic1[@"sourceID"]
    };
    self.isUploadModel.isOpen = YES;
    self.isUploadModel.fileSize = data.length;

    [VULBaseRequest requestWithUrl:@"/api/disk/upload/check"
                            params:dic
                       requestType:YTKRequestMethodGET
                        completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
            
            NSString *fileExists =request.responseObject[@"data"][@"fileExists"];
            if (fileExists.boolValue) {
                self.isUploadModel.speed = 1;

                [self sucessWithPath:asset];
              
            }else{
                [self uploadFileWithPath:asset filePath:filePath md5:md5Str];
            }
        } else {

        }
    }];
}

-(void)uploadFileWithPath:(PHAsset *)asset filePath:(NSString *)filePath1 md5:(NSString*)md5Str{
    NSURL *newURL = [NSURL fileURLWithPath:filePath1];
    NSString *posturl = [NSString stringWithFormat:@"%@%@",ChooseUrl,@"api/disk/upload"];
    NSData *fileData = [NSData dataWithContentsOfURL:newURL];

    __block NSInteger fileSize =  fileData.length;
    //总片数
    NSInteger chunks = (fileSize % 1024 == 0) ? ((int)(fileSize / (1024 * 1024 * 2))) : ((int)(fileSize / (1024 * 1024 * 2) + 1));
        int offset = 2 * 1024 * 1024;//（每一片的大小是2M）
    
    if (chunks ==0) {
        //        小于2M直接上传
        chunks =1;
        offset = fileSize;
    }

    NSData *data;
    NSFileHandle *readHandle = [NSFileHandle fileHandleForReadingFromURL:newURL error:nil];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    BOOL isDir = NO;
    
    BOOL existed = [fileManager fileExistsAtPath:filePath(@"upload") isDirectory:&isDir];
    if (!existed) {
        NSLog(@"Removed successfully");
        [fileManager createDirectoryAtPath:filePath(@"upload") withIntermediateDirectories:YES attributes:nil error:nil];
    }
    
    
    NSString *fileMD5Path = [NSString stringWithFormat:@"upload/%@",md5Str];
    BOOL existed1 = [fileManager fileExistsAtPath:filePath(fileMD5Path) isDirectory:&isDir];
    NSMutableArray *dataArray = [NSMutableArray arrayWithCapacity:0];
    
    if (!existed1) {
        NSLog(@"Removed successfully");
        
        [fileManager createDirectoryAtPath:filePath(fileMD5Path) withIntermediateDirectories:YES attributes:nil error:nil];
        
        for (int i = 0; i < chunks; i++) {
            @autoreleasepool {
                [readHandle seekToFileOffset:offset * i];
                data = [readHandle readDataOfLength:offset];
                NSString* path = filePath([NSString stringWithFormat:@"%@/%@%d",fileMD5Path,md5Str,i]);
                
                BOOL flag =  [data writeToFile:path atomically:NO];
                if (!flag) {
                    NSLog(@"写入失败");
                }else{
                    NSLog(@"写入成功");
                    
                }
                [dataArray addObject:@(i)];
            }
        }
    }else{
        for (int i = 0; i < chunks; i++) {
            @autoreleasepool {
                [dataArray addObject:@(i)];
            }
        }
        
    }
    NSMutableArray *backupArr = [NSMutableArray arrayWithArray:[NSArray bg_arrayWithName:@"backInfo"]];
    NSDictionary *dic1= backupArr[0];
    NSString * fileName = [newURL lastPathComponent];
    self.isUploadModel.speed = 0;
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        __block NSError *error;
        __block id result;
        __block BOOL success = NO;
        dispatch_semaphore_t subSemaphore = dispatch_semaphore_create(0);
        for (int i =   0; i < dataArray.count; i++) {
            BOOL isCose =  [[NSUserDefaults standardUserDefaults]boolForKey:@"isClose"];
            if(isCose){
                return;

            }
            dispatch_async(dispatch_get_global_queue(0, 0), ^{
             
                NSString* str = filePath([NSString stringWithFormat:@"%@/%@%d",fileMD5Path,md5Str,i]);
                NSData *resultData = [NSData dataWithContentsOfFile:str];
                if ([resultData isKindOfClass:[NSData class]]) {
                    
                    NSMutableDictionary *params = [NSMutableDictionary dictionary];
                    [params setObject:@"cloud" forKey:@"busType"];
                    [params setObject:md5Str forKey:@"hashMd5"];
                    [params setObject:[NSString stringWithFormat:@"%d", i] forKey:@"chunk"];
                    
                    [params setObject:dic1[@"sourceID"] forKey:@"sourceID"];
                    [params setObject:@"2097152" forKey:@"chunkSize"];
                    [params setObject:[NSString stringWithFormat:@"%zd", dataArray.count] forKey:@"chunks"];
                    
                    
                    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
                    //设置响应文件类型为JSON类型
                    manager.responseSerializer = [AFJSONResponseSerializer serializer];
                    manager.requestSerializer = [AFHTTPRequestSerializer serializer];
                    manager.responseSerializer.acceptableContentTypes = nil;
                    //设置timeout
                    [manager.requestSerializer setTimeoutInterval:40.0];
                    //设置请求头类型
                    [manager.requestSerializer setValue:@"form/data" forHTTPHeaderField:@"Content-Type"];
                    NSString *token = [VULRealmDBManager getLocalToken];
                    [manager.requestSerializer setValue:token forHTTPHeaderField:@"token"];
                    
                    [manager POST:posturl parameters:params constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
                        [formData appendPartWithFileData:resultData name:@"file" fileName:  [NSString stringWithFormat:@"%@%@",fileName,[fileName containsString:@"jpg"]?@"":@".jpg"] mimeType:@"jpg"];
                        
                    } progress:^(NSProgress * _Nonnull uploadProgress) {
                        NSLog(@"上传进度: %f", uploadProgress.fractionCompleted);
                        CGFloat downLoadPercent = 0;
                        downLoadPercent = (uploadProgress.completedUnitCount + 2048 * i * 1024) * 1.0 / fileSize;
                        if (downLoadPercent > 1.0) {
                            downLoadPercent = 1.f;
                        }
                        
                        self.isUploadModel.speed = downLoadPercent;
                        [VULNotificationCenter postNotificationName:@"backUpViewNotificationCenter" object:nil];

                    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
                        BOOL isCompletion = NO;
                        NSString *success1 = responseObject[@"success"];
                        if (![responseObject[@"code"] isEqualToString:@"200"]) {
                            result = responseObject;
                            isCompletion = YES;
                        }
                        if (success1.boolValue) {
                            success = YES;
                            result = responseObject;
                            isCompletion = YES;
                        }
                        if (isCompletion) {
                            NSLog(@"上传结果====%@",responseObject);
                            if (success) {
                               
                                
                            }
                            NSLog(@"%@",task.currentRequest.allHTTPHeaderFields);
                            NSLog(@"%@",params);
                        }
                        
                        
                        dispatch_semaphore_signal(subSemaphore);
                    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
                        error = error;
                        dispatch_semaphore_signal(subSemaphore);
                        NSLog(@"上传失败");
                        NSLog(@"%@",task.currentRequest.allHTTPHeaderFields);
                        NSLog(@"%@",params);
                        NSLog(@"error--%@",error);
                    }];
                }
            });
            
            
            dispatch_semaphore_wait(subSemaphore, DISPATCH_TIME_FOREVER);
        }
        dispatch_sync(dispatch_get_main_queue(), ^{
            NSLog(@"%ld",success);
            
            if (success ) {
                NSFileManager *fileManager = [NSFileManager defaultManager];
                BOOL isDir = NO;
                BOOL existed = [fileManager fileExistsAtPath:filePath(md5Str) isDirectory:&isDir];
                if ([fileManager removeItemAtPath:filePath(md5Str) error:NULL]) {
                    NSLog(@"Removed successfully");
                }
                [self sucessWithPath:asset];
               
            }else{
                [self startUpload];
            }
            
        });
    });
}
-(void)sucessWithPath:(PHAsset *)asset{
    NSLog(@"Photo uploaded successfully");
    [VULNotificationCenter postNotificationName:@"backUpViewNotificationCenter" object:nil];

    // 将已上传的照片的标识符保存到本地
    [self.uploadedIdentifiers  addObject:asset.localIdentifier];
    NSMutableDictionary *idc = [NSMutableDictionary dictionary];
    [idc setValue:asset.localIdentifier forKey:@"localIdentifier"];
    if (asset.mediaType == PHAssetMediaTypeImage) {
        [idc setValue:@0 forKey:@"isVideo"];
    }else {
        [idc setValue:@1 forKey:@"isVideo"];
    }
    NSMutableArray *arr = [NSMutableArray arrayWithObject:idc];
    [arr bg_saveArrayWithName:@"uploadedIdentifiers"];

               // 从未上传的照片数组中移除已上传的照片
    [self.unuploadedPhotos removeObject:asset];
               // 继续上传未上传的照片

    [self startUpload];
}
@end
