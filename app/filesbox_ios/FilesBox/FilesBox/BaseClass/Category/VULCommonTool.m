//
//  VULCommonTool.m
//  VideoULimit
//
//  Created by ZCc on 2018/9/4.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULCommonTool.h"
#import <AVFoundation/AVFoundation.h>

@implementation VULCommonTool

+ (void)requestVideoAccessWithHandler:(void (^)(NSError *))handler {
    AVAuthorizationStatus videoAuthorStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
    
    if (AVAuthorizationStatusAuthorized == videoAuthorStatus) {
        handler(nil);
    } else {
        if (AVAuthorizationStatusRestricted == videoAuthorStatus || AVAuthorizationStatusDenied == videoAuthorStatus) {
            NSString *errMsg = NSLocalizedString(@"此应用需要访问摄像头，请设置", @"此应用需要访问摄像头，请设置");
            NSDictionary *userInfo = @{NSLocalizedDescriptionKey:errMsg};
            NSError *error = [NSError errorWithDomain:@"访问权限" code:0 userInfo:userInfo];
            handler(error);
        } else {
            [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo completionHandler:^(BOOL granted) {
                if (granted) {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        handler(nil);
                    });
                } else {
                    NSString *errMsg = NSLocalizedString(@"不允许访问摄像头", @"不允许访问摄像头");
                    NSDictionary *userInfo = @{NSLocalizedDescriptionKey:errMsg};
                    NSError *error = [NSError errorWithDomain:@"访问权限" code:0 userInfo:userInfo];
                    dispatch_async(dispatch_get_main_queue(), ^{
                        handler(error);
                    });
                }
            }];
        }
        
    }
}

+ (void)requestAuidoAccessWithHandler:(void (^)(NSError *))handler {
    AVAuthorizationStatus audioAuthorStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeAudio];
    
    if (AVAuthorizationStatusAuthorized == audioAuthorStatus) {
        handler(nil);
    } else {
        if (AVAuthorizationStatusRestricted == audioAuthorStatus || AVAuthorizationStatusDenied == audioAuthorStatus) {
            NSString *errMsg = NSLocalizedString(@"此应用需要访问麦克风，请设置", @"此应用需要访问麦克风，请设置");
            NSDictionary *userInfo = @{NSLocalizedDescriptionKey:errMsg};
            NSError *error = [NSError errorWithDomain:@"访问权限" code:0 userInfo:userInfo];
            handler(error);
        } else {
            [[AVAudioSession sharedInstance] requestRecordPermission:^(BOOL granted) {
                if (granted) {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        handler(nil);
                    });
                } else {
                    NSString *errMsg = NSLocalizedString(@"不允许访问麦克风", @"不允许访问麦克风");
                    NSDictionary *userInfo = @{NSLocalizedDescriptionKey:errMsg};
                    NSError *error = [NSError errorWithDomain:@"访问权限" code:0 userInfo:userInfo];
                    dispatch_async(dispatch_get_main_queue(), ^{
                        handler(error);
                    });
                }
            }];
        }
    }
}

+ (void)requestPhotosAlbumAccessWithHandler:(void (^)(NSError *))handler {

    
}

@end
