//
//  VULCommonTool.h
//  VideoULimit
//
//  Created by ZCc on 2018/9/4.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface VULCommonTool : NSObject

/**
 麦克风权限
 
 @param handler 回调
 */
+ (void)requestAuidoAccessWithHandler:(void (^)(NSError *))handler;

/**
 摄像头权限
 
 @param handler 回调
 */
+ (void)requestVideoAccessWithHandler:(void (^)(NSError *))handler;


@end
