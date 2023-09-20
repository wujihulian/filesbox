//
//  AuthorizationTool.h
//  VideoULimit
//
//  Created by yuekewei on 2020/5/6.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface AuthorizationTool : NSObject

/// 判断麦克风权限
/// @param completion 授权回调
+ (void)authorizationForMic:(void (^)(BOOL granted))completion;

/// 相机授权
/// @param completion 授权回调
+ (void)authorizationForCamera:(void (^)(BOOL granted))completion;

/// 相册授权
/// @param completion 授权回调
+ (void)authorizationForPhotoLibrary:(void (^)(BOOL granted))completion;
@end

NS_ASSUME_NONNULL_END
