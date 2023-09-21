//
//  WKProcessPool+SharedProcessPool.m
//  VideoULimit
//
//  Created by ZCc on 2019/4/9.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import "WKProcessPool+SharedProcessPool.h"

@implementation WKProcessPool (SharedProcessPool)

+ (WKProcessPool *)shareProcessPool {
    static WKProcessPool *sharedProcessPool;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedProcessPool = [[WKProcessPool alloc] init];
    });
    return sharedProcessPool;
}

@end
