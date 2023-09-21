//
//  FBDownloadFileManage.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/6.
//

#import "FBDownloadFileManage.h"
@interface FBDownloadFileManage ()

@property (nonatomic, strong) UBUploadModel *model; // 单例对象

@end
@implementation FBDownloadFileManage

/**
 * manager的懒加载
 */
- (AFURLSessionManager *)manager {
    if (!_manager) {
        NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
        // 1. 创建会话管理者
        _manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:configuration];
    }
    return _manager;
}
/**
 * downloadTask的懒加载
 */
- (NSURLSessionDataTask *)downloadTask {
    if (!_downloadTask) {
        // 创建下载URL
        NSURL *url = self.model.fileUrl;
        
        // 2.创建request请求
        NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
        // 沙盒文件路径
        NSFileManager *manager = [NSFileManager defaultManager];
        BOOL isDir = NO;
        BOOL existed = [manager fileExistsAtPath:filePath(@"downLoad") isDirectory:&isDir];
        if (!existed) {
            NSLog(@"Removed successfully");
            [manager createDirectoryAtPath:filePath(@"downLoad") withIntermediateDirectories:YES attributes:nil error:nil];
        }
        NSString *path = [NSString stringWithFormat:@"%@/%@.%@",filePath(@"downLoad"),_model.fileID,_model.fileType];

        NSInteger currentLength = [self fileLengthForPath:path];
            if (currentLength > 0) {  // [继续下载]
                  self.currentLength = currentLength;
            }
              
        if (_model.isOffline) {
            _model.isOffline = NO;
        }
        // 设置HTTP请求头中的Range
        NSString *range = [NSString stringWithFormat:@"bytes=%zd-", self.currentLength];
        [request setValue:range forHTTPHeaderField:@"Range"];
    
        __weak typeof(self) weakSelf = self;
        _downloadTask = [self.manager dataTaskWithRequest:request completionHandler:^(NSURLResponse * _Nonnull response, id  _Nullable responseObject, NSError * _Nullable error) {
            NSLog(@"dataTaskWithRequest");
                // 清空长度
                if (((weakSelf.currentLength == weakSelf.fileLength)&&weakSelf.fileLength>0)|| ((self.currentLength>_model.fileSize)&&_model.fileSize>0)) {
                    weakSelf.model.isSucess = YES;
                    [kWindow makeToast:KLanguage(@"下载成功")]; 
                    saveUploadFileWithModel(_model);
                    NSArray *picArray = @[@"bmp",@"jpg",@"jpeg",@"png",@"gif",@"arw", @"mrw", @"erf", @"raf",@"cr2", @"nrw", @"nef", @"orf", @"rw2", @"pef", @"srf", @"dcr", @"kdc", @"dng",@"psd",@"webp"];
                    if([picArray containsObject:_model.fileType]){
                        [self saveImage];
                    
                    }
                    if([_model.fileType isEqualToString:@"mp4"]){
                        [self saveVedioImage];
                    }
                }
                weakSelf.currentLength = 0;
                weakSelf.fileLength = 0;
                // 关闭fileHandle
                [weakSelf.fileHandle closeFile];
                weakSelf.fileHandle = nil;
                      
            }];
                  
         [self.manager setDataTaskDidReceiveResponseBlock:^NSURLSessionResponseDisposition(NSURLSession * _Nonnull session, NSURLSessionDataTask * _Nonnull dataTask, NSURLResponse * _Nonnull response) {
                NSLog(@"NSURLSessionResponseDisposition");
                // 获得下载文件的总长度：请求下载的文件长度 + 当前已经下载的文件长度
                weakSelf.fileLength = response.expectedContentLength + self.currentLength;
                NSLog(@"File downloaded to: %ld", weakSelf.fileLength );
                // 创建一个空的文件到沙盒中
             if(self.fileLength <0){
                 self.fileLength = self.model.fileSize;
             }
                NSFileManager *manager = [NSFileManager defaultManager];
                if (![manager fileExistsAtPath:path]) {
                // 如果没有下载文件的话，就创建一个文件。如果有下载文件的话，则不用重新创建(不然会覆盖掉之前的文件)
                    [manager createFileAtPath:path contents:nil attributes:nil];
                }
                // 创建文件句柄
                weakSelf.fileHandle = [NSFileHandle fileHandleForWritingAtPath:path];
                // 允许处理服务器的响应，才会继续接收服务器返回的数据
                return NSURLSessionResponseAllow;
         }];
                        
       [self.manager setDataTaskDidReceiveDataBlock:^(NSURLSession * _Nonnull session, NSURLSessionDataTask * _Nonnull dataTask, NSData * _Nonnull data) {
            NSLog(@"setDataTaskDidReceiveDataBlock");
            // 指定数据的写入位置 -- 文件内容的最后面
            [weakSelf.fileHandle seekToEndOfFile];
             // 向沙盒写入数据
            [weakSelf.fileHandle writeData:data];
            // 拼接文件总长度
           weakSelf.currentLength += data.length;
           NSLog(@"File downloaded11 to: %ld", weakSelf.currentLength );

            // 获取主线程，不然无
            CGFloat downLoadPercent = 0;
              downLoadPercent = 1.0 * weakSelf.currentLength / weakSelf.fileLength;
             if (downLoadPercent > 1.0) {
                downLoadPercent = 1.f;
             }
             weakSelf.model.speed = downLoadPercent;
            NSLog(@"%f",_model.speed);
            saveUploadFileWithModel(_model);
       }];
    }
     return _downloadTask;
 }
-(void)saveVedioImage{
    NSString *path = [NSString stringWithFormat:@"%@/%@.%@",filePath(@"downLoad"),_model.fileID,_model.fileType];
    UISaveVideoAtPathToSavedPhotosAlbum(path, self, @selector(video:didFinishSavingWithError:contextInfo:), nil);

}
#pragma mark 视频保存完毕的回调
- (void)video:(NSString *)videoPath didFinishSavingWithError:(NSError *)error contextInfo:(void *)contextInf{
    if (error) {
        NSLog(@"保存视频过程中发生错误，错误信息:%@",error.localizedDescription);
    }else{
        NSLog(@"视频保存成功.");
    }
}

- (void)saveImage{
    //save to photosAlbum
    NSString *path = [NSString stringWithFormat:@"%@/%@.%@",filePath(@"downLoad"),_model.fileID,_model.fileType];
    NSData *newImageData = [NSData dataWithContentsOfFile:path];

    [[PHPhotoLibrary sharedPhotoLibrary] performChanges:^{
        PHAssetCreationRequest *request = [PHAssetCreationRequest creationRequestForAsset];

        // 将图片和元数据添加到请求中
        [request addResourceWithType:PHAssetResourceTypePhoto data:newImageData options:nil];
//        [request setCreationDate:[NSDate date]];
//
//        // 设置地理定位信息
//        CLLocation *location = [[CLLocation alloc] initWithLatitude:latitude longitude:longitude];
//        [request setLocation:location];
        
    } completionHandler:^(BOOL success, NSError *error) {
        if (success) {
            NSLog(@"图片保存成功，并保留了元数据");
        } else {
            NSLog(@"图片保存失败：%@", error);
        }
    }];

//    UIImageWriteToSavedPhotosAlbum(appleImage, self, @selector(image:didFinishSavingWithError:contextInfo:), nil);
}

- (void)image:(UIImage *)image didFinishSavingWithError:(NSError *)error contextInfo:(void *)contextInfo
{
    NSString *barItemTitle = @"保存成功";
    if (error != nil) {
        barItemTitle = @"保存失败";
    }
    NSLog(barItemTitle);

}

-(void)downloadFileWithModel:(UBUploadModel *)model{
    model.status = UploadStatusUploading;
    saveUploadFileWithModel(model);
    self.model = model;
   [self.downloadTask resume];
}
-(void)resume:(UBUploadModel *)model{
    //传入上次暂停下载返回的数据，就可以恢复下载
    model.status = UploadStatusUploading;
    [self.downloadTask resume];

}
-(void)pause:(UBUploadModel *)model{
    model.status = UploadStatusPaused;
    [self.downloadTask suspend];
  self.downloadTask = nil;
}
/**

 * 获取已下载的文件大小

 */

- (NSInteger)fileLengthForPath:(NSString *)path {
    NSInteger fileLength = 0;
    NSFileManager *fileManager = [[NSFileManager alloc] init]; // default is not thread safe
    if ([fileManager fileExistsAtPath:path]) {
        NSError *error = nil;
        NSDictionary *fileDict = [fileManager attributesOfItemAtPath:path error:&error];
        if (!error && fileDict) {
            fileLength = [fileDict fileSize];
        }
    }
    return fileLength;
}

@end
@implementation FBDownloadFileAllManage
+ (instancetype)sharedManager {
    static FBDownloadFileAllManage *sharedManager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedManager = [[self alloc] init];

    });
    return sharedManager;
}
- (NSMutableDictionary *)sessionDic {
    if (!_sessionDic) {
        _sessionDic = [NSMutableDictionary dictionary];
    }
    return _sessionDic;
}
-(void)addDownloadFileWithModel:(UBUploadModel *)model{
    [kWindow makeToast:KLanguage(@"加入下载队列")];

    FBDownloadFileManage *down = [FBDownloadFileManage new];
    [down downloadFileWithModel:model];
    [self.sessionDic setValue:down forKey:model.filePath];


}

@end
