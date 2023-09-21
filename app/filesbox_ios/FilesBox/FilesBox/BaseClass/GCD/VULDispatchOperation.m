//
//  VULDispatchOperation.m
//  VideoULimit
//
//  Created by ZCc on 2018/10/30.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import "VULDispatchOperation.h"
#import "VULDispatchAsync.h"

#ifndef VULDispatchAsync_m
#define VUL_INLINE static inline
#endif

#define VUL_FUNCTION_OVERLOAD __attribute__((overloadable))

VUL_INLINE VUL_FUNCTION_OVERLOAD void __VULLockExecute(dispatch_block_t block, dispatch_time_t threshold);

VUL_INLINE VUL_FUNCTION_OVERLOAD void __VULLockExecute(dispatch_block_t block) {
    __VULLockExecute(block, dispatch_time(DISPATCH_TIME_NOW, DISPATCH_TIME_FOREVER));
}

VUL_INLINE VUL_FUNCTION_OVERLOAD void __VULLockExecute(dispatch_block_t block, dispatch_time_t threshold) {
    if(block == nil) { return ; }
    static dispatch_semaphore_t VUL_queue_semaphore;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        VUL_queue_semaphore = dispatch_semaphore_create(0);
    });
    dispatch_semaphore_wait(VUL_queue_semaphore, threshold);
    block();
    dispatch_semaphore_signal(VUL_queue_semaphore);
}

@interface VULDispatchOperation()

@property (nonatomic, assign) BOOL isCanceled;
@property (nonatomic, assign) BOOL isExcuting;
@property (nonatomic, assign) dispatch_queue_t queue;
@property (nonatomic, assign) dispatch_queue_t (*asyn)(dispatch_block_t);
@property (nonatomic, copy) VULCancelableBlock cancelableBlock;

@end

@implementation VULDispatchOperation

+ (instancetype)dispatchOperationWithBlock:(dispatch_block_t)block{
    return [self dispatchOperationWithCancelableBlock:^(VULDispatchOperation * _Nonnull operation) {
        if(!operation.isCanceled){
            block();
        }
    } inQos:NSQualityOfServiceDefault];
}

+ (instancetype)dispatchOperationWithBlock:(dispatch_block_t)block inQoS:(NSQualityOfService)qos{
    return [self dispatchOperationWithCancelableBlock:^(VULDispatchOperation * _Nonnull operation) {
        if(!operation.isCanceled){
            block();
        }
    } inQos:qos];
}

+ (instancetype)dispatchOperationWithCancelableBlock:(VULCancelableBlock)block{
    return [self dispatchOperationWithCancelableBlock:block inQos:NSQualityOfServiceDefault];
}

+ (instancetype)dispatchOperationWithCancelableBlock:(VULCancelableBlock)block inQos:(NSQualityOfService)qos{
    return [[self alloc] initWithBlock:block inQos:qos];
}

- (instancetype)initWithBlock:(VULCancelableBlock)block inQos:(NSQualityOfService)qos{
    if(block == nil){ return nil;}
    if(self = [super init]){
        switch (qos) {
            case NSQualityOfServiceUserInteractive:
                self.asyn = VULDispatchQueueAsyncBlockInUserInteractive;
                break;
            case NSQualityOfServiceUserInitiated:
                self.asyn = VULDispatchQueueAsyncBlockInUserInitiated;
                break;
            case NSQualityOfServiceDefault:
                self.asyn = VULDispatchQueueAsyncBlockInDefault;
                break;
            case NSQualityOfServiceUtility:
                self.asyn = VULDispatchQueueAsyncBlockInUtility;
                break;
            case NSQualityOfServiceBackground:
                self.asyn = VULDispatchQueueAsyncBlockInBackground;
                break;
            default:
                self.asyn = VULDispatchQueueAsyncBlockInDefault;
                break;
        }
    }
    return self;
}

- (void)dealloc{
    [self cancel];
}

- (void)start{
    __VULLockExecute(^{
        self.queue = self.asyn(^{
            self.cancelableBlock(self);
            self.cancelableBlock = nil;
        });
        self.isExcuting = YES;
    });
}

- (void)cancel{
    __VULLockExecute(^{
        self.isCanceled = YES;
        if(!self.isExcuting){
            self.asyn = NULL;
            self.cancelableBlock = nil;
        }
    });
}

@end
