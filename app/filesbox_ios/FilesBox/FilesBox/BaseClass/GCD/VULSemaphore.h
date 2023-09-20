//
//  VULSemaphore.h
//  VideoULimit
//
//  Created by ZCc on 2018/10/31.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULSemaphore : NSObject

@property (nonatomic, readonly, strong) dispatch_semaphore_t executeSemaphore;

#pragma mark - 构造器
- (instancetype)initWihtValue:(NSUInteger)value;

#pragma mark - 操作
- (void)wait;
- (BOOL)waitWithTimeout:(NSTimeInterval)timeout;
- (BOOL)signal;

@end

NS_ASSUME_NONNULL_END
