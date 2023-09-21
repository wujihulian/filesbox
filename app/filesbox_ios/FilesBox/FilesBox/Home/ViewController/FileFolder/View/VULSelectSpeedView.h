//
//  VULSelectSpeedView.h
//  VideoULimit
//
//  Created by yuekewei on 2020/4/20.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULSelectSpeedView : UIView

- (instancetype)initWithSelectIndex:(NSInteger)selectIndex;

@property (nonatomic, copy) void(^selectSpeedBlock)(NSInteger selectIndex, NSString *speed);
@end

NS_ASSUME_NONNULL_END
