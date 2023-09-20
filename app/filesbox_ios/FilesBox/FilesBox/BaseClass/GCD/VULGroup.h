//
//  VULGroup.h
//  VideoULimit
//
//  Created by ZCc on 2018/10/31.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULGroup : NSObject

@property (nonatomic, readonly, strong) dispatch_group_t executeGroup;

#pragma mark - 操作
- (void)wait;
- (void)center;
- (void)leave;
- (BOOL)wait:(NSTimeInterval)delay;

@end

NS_ASSUME_NONNULL_END
