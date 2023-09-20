//
//  VULWebViewPool.m
//  VideoULTeacher
//
//  Created by mac on 2020/7/2.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "VULWebViewPool.h"

@interface VULWebViewPool ()

@property (nonatomic) NSUInteger initialViewsMaxCount;  //最多初始化的个数
@property (nonatomic) NSMutableArray *preloadedViews;

@end

@implementation VULWebViewPool

+ (instancetype)sharedInstance {
    static dispatch_once_t onceToken;
    static VULWebViewPool *instance = nil;
    dispatch_once(&onceToken,^{
        instance = [[super allocWithZone:NULL] init];
    });
    return instance;
}

+ (id)allocWithZone:(struct _NSZone *)zone{
    return [self sharedInstance];
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.initialViewsMaxCount = 20;
        self.preloadedViews = [NSMutableArray arrayWithCapacity:self.initialViewsMaxCount];
    }
    return self;
}

/**
 预初始化若干WKWebView
 
 @param count 个数
 */
- (void)prepareWithCount:(NSUInteger)count {
    
    NSTimeInterval start = CACurrentMediaTime();
    
    // Actually does nothing, only initialization must be called.
    while (self.preloadedViews.count < MIN(count,self.initialViewsMaxCount)) {
        id preloadedView = [self createPreloadedView];
        if (preloadedView) {
            [self.preloadedViews addObject:preloadedView];
        } else {
            break;
        }
    }
    
    NSTimeInterval delta = CACurrentMediaTime() - start;
    NSLog(@"=======初始化耗时：%f",  delta);
}

/**
 从池中获取一个WKWebView
 @return WKWebView
 */
- (WKWebView *)getWKWebViewFromPool {
    if (!self.preloadedViews.count) {
        NSLog(@"不够啦！");
        return [self createPreloadedView];
    } else {
        id preloadedView = self.preloadedViews.firstObject;
        [self.preloadedViews removeObject:preloadedView];
        return preloadedView;
    }
}

/**
 创建一个WKWebView
 @return WKWebView
 */
- (WKWebView *)createPreloadedView {
    
    WKWebView *wkWebView = [[WKWebView alloc]initWithFrame:CGRectZero configuration:self.wkConfig];
    
    return wkWebView;
}

@end
