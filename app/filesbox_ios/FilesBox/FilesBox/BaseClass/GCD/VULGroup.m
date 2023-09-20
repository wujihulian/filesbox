//
//  VULGroup.m
//  VideoULimit
//
//  Created by ZCc on 2018/10/31.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import "VULGroup.h"

@interface VULGroup()

@property (nonatomic, strong)dispatch_group_t executeGroup;

@end

@implementation VULGroup

- (instancetype)init{
    if(self = [super init]){
        self.executeGroup = dispatch_group_create();
    }
    return self;
}

- (void)wait{
    dispatch_group_wait(self.executeGroup, DISPATCH_TIME_FOREVER);
}

- (void)center{
    dispatch_group_enter(self.executeGroup);
}

- (void)leave{
    dispatch_group_leave(self.executeGroup);
}

- (BOOL)wait:(NSTimeInterval)delay{
    return dispatch_group_wait(self.executeGroup, delay * NSEC_PER_SEC) == 0;
}

@end
