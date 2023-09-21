//
//  AuthorizationTool.m
//  VideoULimit
//
//  Created by yuekewei on 2020/5/6.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "AuthorizationTool.h"
#import <AVFoundation/AVFoundation.h>
#import <Photos/Photos.h>

@implementation AuthorizationTool

+ (UIViewController * __nullable)topmostViewController {
    
    UIViewController *topViewController = [[[UIApplication sharedApplication] keyWindow] rootViewController];    
    if (topViewController == nil) {
        return nil;
    }
    
    while (true) {
        if (topViewController.presentedViewController != nil) {
            topViewController = topViewController.presentedViewController;
        } else if ([topViewController isKindOfClass:[UINavigationController class]]) {
            UINavigationController *navi = (UINavigationController *)topViewController;
            topViewController = navi.topViewController;
        } else if ([topViewController isKindOfClass:[UITabBarController class]]) {
            UITabBarController *tab = (UITabBarController *)topViewController;
            topViewController = tab.selectedViewController;
        } else {
            break;
        }
    }
    
    return topViewController;
}

/// 判断麦克风权限
/// @param completion 授权回调
+ (void)authorizationForMic:(void (^)(BOOL granted))completion {
    
    AVAuthorizationStatus videoAuthStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeAudio];
    if (videoAuthStatus == AVAuthorizationStatusNotDetermined) {
        // 未询问用户是否授权
        //询问用户是否进行授权
        [[AVAudioSession sharedInstance] requestRecordPermission:^(BOOL granted) {
            
            dispatch_async(dispatch_get_main_queue(), ^{
                if (completion) {
                    completion(granted);
                }
            });
        }];
    }
    else if(videoAuthStatus == AVAuthorizationStatusRestricted ||
            videoAuthStatus == AVAuthorizationStatusDenied) {
                
        // 未授权
        dispatch_async(dispatch_get_main_queue(), ^{
            UIAlertController *alert = [UIAlertController alertControllerWithTitle:KLanguage(@"无法使用麦克风") message:KLanguage(@"请在iPhone的“设置-隐私-麦克风”选项中，允许访问麦克风。") preferredStyle:UIAlertControllerStyleAlert];
            [alert addAction:[UIAlertAction actionWithTitle:KLanguage(@"设置") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];
            }]];
            [alert addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消" ) style:UIAlertActionStyleCancel handler:nil]];
            
            [[self topmostViewController] presentViewController:alert animated:YES completion:nil];
                        
            if (completion) {
                completion(NO);
            }
        });
        
    }
    else {
        dispatch_async(dispatch_get_main_queue(), ^{
            if (completion) {
                completion(YES);
            }
        });
        
    }
}

/// 相机授权
/// @param completion 授权回调
+ (void)authorizationForCamera:(void (^)(BOOL granted))completion {
    
    AVAuthorizationStatus authStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
    if (authStatus == AVAuthorizationStatusNotDetermined) {
        [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo completionHandler:^(BOOL granted) {
            dispatch_async(dispatch_get_main_queue(), ^{
                if (granted) {
                    [self authorizationForPhotoLibrary:completion];
                }
                else {
                    if (completion) {
                        completion(NO);
                    }
                }
            });
        }];
    }
    else if (authStatus == AVAuthorizationStatusRestricted ||
             authStatus == AVAuthorizationStatusDenied) {
        
        // 无相机权限 做一个友好的提示
        dispatch_async(dispatch_get_main_queue(), ^{
            UIAlertController *alertVc = [UIAlertController alertControllerWithTitle:KLanguage(@"无法使用相机") message:KLanguage(@"请在iPhone的‘设置-隐私-相机’中允许访问相机") preferredStyle:UIAlertControllerStyleAlert];
            [alertVc addAction:[UIAlertAction actionWithTitle:@"设置" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];
            }]];
            
            [alertVc addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消" ) style:UIAlertActionStyleCancel handler:nil]];
            
            [[self topmostViewController] presentViewController:alertVc animated:YES completion:nil];
            
            if (completion) {
                completion(NO);
            }
        });
    }
    else {
        [self authorizationForPhotoLibrary:completion];
    }
}


/// 相册授权
/// @param completion 授权回调
+ (void)authorizationForPhotoLibrary:(void (^)(BOOL granted))completion {
    
    PHAuthorizationStatus authStatus = [PHPhotoLibrary authorizationStatus];
    if (authStatus == PHAuthorizationStatusNotDetermined) {
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            [PHPhotoLibrary requestAuthorization:^(PHAuthorizationStatus status) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    if (completion) {
                        completion(status == PHAuthorizationStatusAuthorized);
                    }
                });
            }];
        });
    }
    else if (authStatus == PHAuthorizationStatusRestricted ||
             authStatus == PHAuthorizationStatusDenied) {
        
        dispatch_async(dispatch_get_main_queue(), ^{
            UIAlertController *alertVc = [UIAlertController alertControllerWithTitle:KLanguage(@"无法访问相册") message:KLanguage(@"请在iPhone的‘设置-隐私-相册’中允许访问相册") preferredStyle:UIAlertControllerStyleAlert];
            [alertVc addAction:[UIAlertAction actionWithTitle:KLanguage(@"设置") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];
            }]];
            [alertVc addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消" ) style:UIAlertActionStyleCancel handler:nil]];
            [[self topmostViewController] presentViewController:alertVc animated:YES completion:nil];
            
            if (completion) {
                completion(NO);
            }
        });
        
    }
    else {
        if (completion) {
            completion(YES);
        }
    }
}

@end
