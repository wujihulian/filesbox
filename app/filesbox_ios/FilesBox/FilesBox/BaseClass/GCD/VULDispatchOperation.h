//
//  VULDispatchOperation.h
//  VideoULimit
//
//  Created by ZCc on 2018/10/30.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@class VULDispatchOperation;
typedef void (^VULCancelableBlock)(VULDispatchOperation *operation);

@interface VULDispatchOperation : NSObject

@property (nonatomic, readonly) BOOL isCanceled;
@property (nonatomic, readonly) dispatch_queue_t queue;

+ (instancetype)dispatchOperationWithBlock:(dispatch_block_t)block;
+ (instancetype)dispatchOperationWithBlock:(dispatch_block_t)block inQoS:(NSQualityOfService)qos;

+ (instancetype)dispatchOperationWithCancelableBlock:(VULCancelableBlock)block;
+ (instancetype)dispatchOperationWithCancelableBlock:(VULCancelableBlock)block inQos:(NSQualityOfService)qos;

- (void)start;
- (void)cancel;

@end

NS_ASSUME_NONNULL_END
