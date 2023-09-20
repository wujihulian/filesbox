//
//  VULRadarInputView.h
//  UnlimitedBusiness
//
//  Created by yuekewei on 2021/3/29.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULRadarInputView : UIView

@property (nonatomic, copy) void(^searchCallBack)(NSString *keyword);

/** 输入框 */
@property (nonatomic, strong) UITextField *textField;

/** 发送按钮 */
@property (nonatomic, strong) UIButton *sendButton;

@property (nonatomic, strong) UIView *toolBar;

- (void)show;

- (void)hide;
@end

NS_ASSUME_NONNULL_END
