//
//  VULInputTitleView.h
//  UnlimitedBusiness
//
//  Created by zuoyi on 2021/12/20.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void (^ YXAlertInputViewBlock)(NSString *text);

@interface VULInputTitleView : UIView

@property (nonatomic,strong) UITextField *textField;
@property (nonatomic,copy) YXAlertInputViewBlock alertInputViewBlock;
@property (nonatomic , assign) int  num;

- (instancetype)initWithFrame:(CGRect)frame title:(NSString *)title;
- (void)showInView;
- (void)hiddenView;

@end

NS_ASSUME_NONNULL_END
