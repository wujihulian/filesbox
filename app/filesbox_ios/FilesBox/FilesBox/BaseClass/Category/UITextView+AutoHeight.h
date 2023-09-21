//
//  UITextView+AutoHeight.h
//  VideoULimit
//
//  Created by svnlan on 2019/8/8.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN
static NSString * const wj_layout_frame = @"wj_layout_frame";
static NSString * const wj_auto_layout = @"wj_auto_layout";

@interface UITextView (AutoHeight)
/**
 是否自适应高度
 */

@property (nonatomic, assign)IBInspectable BOOL isAutoHeightEnable;

/**
 设置最大高度
 */
@property (nonatomic, assign)IBInspectable CGFloat wj_maxHeight;

/**
 最小高度
 */
@property (nonatomic, assign) CGFloat wj_minHeight;

/**
 占位符
 */
@property (nonatomic, copy)IBInspectable NSString * wj_placeHolder;
/**
 占位符颜色
 */
@property (nonatomic, strong) UIColor * wj_placeHolderColor;

/**
 占位Label
 */
@property (nonatomic, strong) UITextView * wj_placeHolderLabel;

/**
 行间距
 */
@property (nonatomic, assign)IBInspectable CGFloat wj_lineSpacing;
@property (nonatomic, strong) NSLayoutConstraint *heightConstraint;
@property (nonatomic, copy) NSString *layout_key;

/**
 高度变化回调
 */
@property (nonatomic, copy) void(^textViewHeightDidChangedHandle)(CGFloat textViewHeight);

@end

NS_ASSUME_NONNULL_END
