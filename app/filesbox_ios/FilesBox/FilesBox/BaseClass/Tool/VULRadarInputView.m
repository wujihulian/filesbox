//
//  VULRadarInputView.m
//  UnlimitedBusiness
//
//  Created by yuekewei on 2021/3/29.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULRadarInputView.h"

@interface VULRadarInputView()<UITextFieldDelegate>

@end

@implementation VULRadarInputView

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillChangeFrame:) name:UIKeyboardWillChangeFrameNotification object:nil];
        self.frame = [UIScreen mainScreen].bounds;
        self.backgroundColor = [UIColorHex(#000000) colorWithAlphaComponent:0.05];
        [self addSubview:self.toolBar];
        [self.toolBar addSubview:self.textField];
        [self.toolBar addSubview:self.sendButton];
        
        self.toolBar.frame = CGRectMake(0, 0, self.width, 49);
        self.toolBar.top = self.height;
        
        self.sendButton.frame = CGRectMake(0, 0, 54, 30);
        self.sendButton.centerY = self.toolBar.height / 2.0;
        self.sendButton.right = self.toolBar.width - 10;
        
        self.textField.frame = CGRectMake(20, 7, 0, self.toolBar.height - 14);
        self.textField.width = self.sendButton.left - self.textField.left - 10;
        
    }
    return self;
}

//- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event {
//    UIView *view = [super hitTest:point withEvent:event];
//    if (view == self) {
//        [self hide];
//        return nil;
//    }
//    return view;
//}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self hide];
}

- (void)show {
    if (!self.superview) {
        [[UIApplication sharedApplication].keyWindow addSubview:self];
    }
    self.hidden = NO;
    self.toolBar.top = self.height;
    [self.textField becomeFirstResponder];
}

- (void)hide {
    [self.textField resignFirstResponder];
    self.hidden = YES;
}

- (BOOL)sendButtonClick {
    if (self.searchCallBack) {
        self.searchCallBack(self.textField.text);
    }
    [self hide];
    return YES;
}

#pragma mark - UITextFieldDelegate
- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    return [self sendButtonClick];
}


#pragma mark - 键盘通知
- (void)keyboardWillChangeFrame:(NSNotification *)notify {
    NSDictionary    * info = notify.userInfo;
    CGFloat animationDuration = [info[UIKeyboardAnimationDurationUserInfoKey] floatValue];
    CGRect  keyboardAimFrame = [info[UIKeyboardFrameEndUserInfoKey] CGRectValue];
    
    
    [UIView animateWithDuration:animationDuration delay:0 options:UIViewAnimationOptionCurveEaseIn animations:^{
        if (keyboardAimFrame.origin.y == self.height) {
            self.toolBar.top = keyboardAimFrame.origin.y;
        }
        else {
            self.toolBar.bottom = keyboardAimFrame.origin.y;
        }
    } completion:^(BOOL finished) {
        if (keyboardAimFrame.origin.y == self.height) {
            [self hide];
        }
    }];
}

#pragma mrk - Lazy
- (UIView *)toolBar {
    if (!_toolBar) {
        _toolBar = [UIView new];
        _toolBar.backgroundColor = [UIColor whiteColor];
    }
    return _toolBar;
}

- (UITextField *)textField {
    if (!_textField) {
        _textField = ({
            UITextField *field = [UITextField new];
            field.delegate = self;
            field.backgroundColor = UIColorHex(#F6F6F6);
            field.font = [UIFont yk_pingFangRegular:16];
            field.textColor = UIColorHex(#333333);
            field.attributedPlaceholder = [[NSMutableAttributedString alloc] initWithString:KLanguage(@"请输入次数")  attributes:@{ NSForegroundColorAttributeName: UIColorHex(#999999)}];
            field.clearButtonMode = UITextFieldViewModeWhileEditing;
            field.layer.cornerRadius = 4.0;
            field.layer.masksToBounds = YES;
            field;
        });
    }
    return _textField;
}

- (UIButton *)sendButton {
    if (!_sendButton) {
        _sendButton = ({
            UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
            [button setTitleColor:UIColorHex(#2F82FF) forState:UIControlStateNormal];
            [button setTitleColor:[UIColorHex(#2F82FF) colorWithAlphaComponent:0.5] forState:UIControlStateHighlighted];
            [button.titleLabel setFont:[UIFont yk_pingFangMedium:16]];
            [button setTitle:KLanguage(@"确定") forState:UIControlStateNormal];
            [button addTarget:self action:@selector(sendButtonClick) forControlEvents:UIControlEventTouchUpInside];
            button;
        });
    }
    return _sendButton;
}

@end
