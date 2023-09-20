//
//  UIResponder+FirstResponder.h
//  VideoULimit
//
//  Created by ZCc on 2019/7/4.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIResponder (FirstResponder)

// 找到第一事件响应者
+ (id)currentFirstResponder;

@end

NS_ASSUME_NONNULL_END
