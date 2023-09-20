//
//  VULQueue.h
//  VideoULimit
//
//  Created by ZCc on 2018/10/31.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

@class VULGroup;
@class VULSemaphore;

typedef NS_ENUM(NSInteger, VULQueuePriority) {
    //最低优先级
    VULLowPriority = DISPATCH_QUEUE_PRIORITY_LOW,
    //高优先级
    VULHighPriority = DISPATCH_QUEUE_PRIORITY_HIGH,
    //默认优先级
    VULDefaultPriority = DISPATCH_QUEUE_PRIORITY_DEFAULT,
    //后台优先级
    VULBackgroundPriority = DISPATCH_QUEUE_PRIORITY_BACKGROUND,
};

NS_ASSUME_NONNULL_BEGIN

@interface VULQueue : NSObject

@property (nonatomic, readonly, strong)dispatch_queue_t executeQueue;

//获取主线程
+ (instancetype)mainQueue;
+ (instancetype)lowPriorityQueue;
+ (instancetype)highPriorityQueue;
+ (instancetype)defaultPriorityQueue;
+ (instancetype)backgroundPriorityQueue;

#pragma mark - 遍历方法
+ (void)executeInMainQueue:(dispatch_block_t)block;
+ (void)executeInGlobalQueue:(dispatch_block_t)block;
+ (void)executeInGlobalQueue:(dispatch_block_t)block queuePriority:(VULQueuePriority)queuePriority;

+ (void)executeInMainQueue:(dispatch_block_t)block delay:(NSTimeInterval)delay;
+ (void)executeInGlobalQueue:(dispatch_block_t)block delay:(NSTimeInterval)delay;
+ (void)executeInGlobalQueue:(dispatch_block_t)block queuePriority:(VULQueuePriority)queuePriority delay:(NSTimeInterval)delay;

#pragma mark - 创建线程
- (instancetype)init;
- (instancetype)initSerial;
- (instancetype)initSerialWithIdentifier:(NSString *)identifier;
- (instancetype)initConcurrent;
- (instancetype)initConcurrentWithIdentifier:(NSString *)identifier;

#pragma mark - 操作
- (void)execute:(dispatch_block_t)block;
- (void)execute:(dispatch_block_t)block delay:(NSTimeInterval)delay;
- (void)execute:(dispatch_block_t)block wait:(VULSemaphore *)semaphore;
- (void)execute:(dispatch_block_t)block delay:(NSTimeInterval)delay wait:(VULSemaphore *)semaphore;

- (void)resume;
- (void)suspend;
- (void)barrierExecute:(dispatch_block_t)block;

#pragma mark - 其他操作
- (void)notify:(dispatch_block_t)block inGroup:(VULGroup *)group;
- (void)execute:(dispatch_block_t)block inGroup:(VULGroup *)group;

@end

NS_ASSUME_NONNULL_END
