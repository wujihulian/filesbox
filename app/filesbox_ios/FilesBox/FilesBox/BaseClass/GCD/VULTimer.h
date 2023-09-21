//
//  VULTimer.h
//  VideoULimit
//
//  Created by ZCc on 2018/10/31.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

@class VULQueue;

NS_ASSUME_NONNULL_BEGIN

@interface VULTimer : NSObject

@property (nonatomic, readonly, strong) dispatch_source_t executeSource;

#pragma mark - 构造器
- initWithExecuteQueue:(VULQueue *)queue;

#pragma mark - 操作

- (void)execute:(dispatch_block_t)block interval:(NSTimeInterval)interval;
- (void)execute:(dispatch_block_t)block interval:(NSTimeInterval)interval delay:(NSTimeInterval)delay;
- (void)start;
- (void)destory;

@end

NS_ASSUME_NONNULL_END
