//
//  VULLoginAuthView.h
//  VideoULimit
//
//  Created by 无极互联 on 2020/8/31.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULLoginAuthView : UIView

- (instancetype)initWithFrame:(CGRect)frame key:(NSString *)keyStr;

@property (nonatomic, copy) void (^ handerActionBlock)(VULButton *);

@end

NS_ASSUME_NONNULL_END
