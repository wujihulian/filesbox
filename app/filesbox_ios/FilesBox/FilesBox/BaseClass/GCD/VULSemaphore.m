//
//  VULSemaphore.m
//  VideoULimit
//
//  Created by ZCc on 2018/10/31.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import "VULSemaphore.h"

@interface VULSemaphore()

@property (nonatomic, strong)dispatch_semaphore_t executeSemaphore;

@end

@implementation VULSemaphore

- (instancetype)init{
    return [self initWihtValue:1];
}

- (instancetype)initWihtValue:(NSUInteger)value{
    if(self = [super init]){
        self.executeSemaphore = dispatch_semaphore_create(value);
    }
    return self;
}

#pragma mark - 操作
- (void)wait{
    dispatch_semaphore_wait(self.executeSemaphore, DISPATCH_TIME_FOREVER);
}

- (BOOL)waitWithTimeout:(NSTimeInterval)timeout{
    return dispatch_semaphore_wait(self.executeSemaphore, dispatch_time(DISPATCH_TIME_NOW, timeout * NSEC_PER_SEC)) == 0;
}

- (BOOL)signal{
    return dispatch_semaphore_signal(self.executeSemaphore) != 0;
}

@end
