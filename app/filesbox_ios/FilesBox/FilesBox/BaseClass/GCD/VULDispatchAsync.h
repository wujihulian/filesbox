//
//  VULDispatchAsync.h
//  VideoULimit
//
//  Created by ZCc on 2018/10/30.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, VULQualityOfService) {
    VULQualityOfServiceUserInteractive = NSQualityOfServiceUserInteractive,
    VULQualityOfServiceUserInitiated = NSQualityOfServiceUserInitiated,
    VULQualityOfServiceUtility = NSQualityOfServiceUtility,
    VULQualityOfServiceBackground = NSQualityOfServiceBackground,
    VULQualityOfServiceDefault = NSQualityOfServiceDefault,
};

dispatch_queue_t VULDispatchQueueAsyncBlockInQOS(VULQualityOfService qos, dispatch_block_t block);
dispatch_queue_t VULDispatchQueueAsyncBlockInUserInteractive(dispatch_block_t block);
dispatch_queue_t VULDispatchQueueAsyncBlockInUserInitiated(dispatch_block_t block);
dispatch_queue_t VULDispatchQueueAsyncBlockInBackground(dispatch_block_t block);
dispatch_queue_t VULDispatchQueueAsyncBlockInDefault(dispatch_block_t block);
dispatch_queue_t VULDispatchQueueAsyncBlockInUtility(dispatch_block_t block);

@interface VULDispatchAsync : NSObject

@end

NS_ASSUME_NONNULL_END
