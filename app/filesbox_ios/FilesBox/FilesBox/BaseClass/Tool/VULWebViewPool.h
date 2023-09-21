//
//  VULWebViewPool.h
//  VideoULTeacher
//
//  Created by mac on 2020/7/2.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <WebKit/WebKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULWebViewPool : NSObject

@property (nonatomic, strong) WKWebViewConfiguration *wkConfig;

+ (instancetype)sharedInstance;

/**
 预初始化若干WKWebView
 @param count 个数
 */
- (void)prepareWithCount:(NSUInteger)count;

/**
 从池中获取一个WKWebView
 
 @return WKWebView
 */

- (WKWebView *)getWKWebViewFromPool;

@end

NS_ASSUME_NONNULL_END
