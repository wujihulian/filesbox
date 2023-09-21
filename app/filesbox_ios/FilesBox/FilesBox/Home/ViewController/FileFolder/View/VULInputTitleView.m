//
//  VULInputTitleView.m
//  UnlimitedBusiness
//
//  Created by zuoyi on 2021/12/20.
//  Copyright © 2021 svnlan. All rights reserved.
//  新建文件夹  重命名

#import "VULInputTitleView.h"

#define kViewWidth fontAuto(283)

@interface VULInputTitleView ()<UITextFieldDelegate>

typedef void(^doneBlock)(NSString *);
@property (nonatomic, strong) UIView *shadowView;
@property (nonatomic,strong)doneBlock doneBlock;
@property (nonatomic,copy) NSString *title;


@property (nonatomic,strong) UIView *bgView;
@property (nonatomic,strong) UIView *joinFreeBillView;
@property (nonatomic,strong) UILabel *titleLabel;
@property (nonatomic,strong) VULButton *cancelBtn;
@property (nonatomic,strong) VULButton *sureBtn;

@end

@implementation VULInputTitleView

- (instancetype)initWithFrame:(CGRect)frame title:(nonnull NSString *)title {
    
    self = [super initWithFrame:frame];
    if (self) {
        self.title = title;
        [self addSubview:self.bgView];
        [self addSubview:self.joinFreeBillView];
        [self.joinFreeBillView addSubview:self.titleLabel];
        [self.joinFreeBillView addSubview:self.textField];
        [self.joinFreeBillView addSubview:self.cancelBtn];
        [self.joinFreeBillView addSubview:self.sureBtn];
        
    }
    return self;
}

- (void)sureBtnClicked:(UIButton *)sender {
   // [self hiddenView];
    self.textField.text = [self.textField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    if (self.alertInputViewBlock) {
        self.alertInputViewBlock(self.textField.text);
    }
}

- (UILabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel =[VULLabel getLabelWithFrame:CGRectMake(fontAuto(0), fontAuto(15), kViewWidth, fontAuto(20)) Text:self.title TxtAlignment:0 Font:[UIFont yk_pingFangMedium:17] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _titleLabel;
}

- (UITextField *)textField {
    if (!_textField) {
        _textField = [[UITextField alloc] initWithFrame:CGRectMake((kViewWidth - fontAuto(253))/2, fontAuto(62), fontAuto(253), fontAuto(40))];
        _textField.layer.cornerRadius = 5;
        _textField.layer.borderColor = UIColorHex(#ECECEC).CGColor;
        _textField.layer.borderWidth = 1;
        UIView *leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, 0)];
        _textField.leftViewMode = UITextFieldViewModeAlways;
        _textField.leftView = leftView;
        UIView *rightView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, 0)];
        _textField.rightViewMode = UITextFieldViewModeAlways;
        _textField.rightView = rightView;
        _textField.placeholder = KLanguage(@"请输入文件夹名称");
    }
    return _textField;
}

- (VULButton *)cancelBtn {
    if (!_cancelBtn) {
        
        _cancelBtn = [VULButton new];
        _cancelBtn.frame = CGRectMake(fontAuto(19), fontAuto(138), fontAuto(115), fontAuto(38));
        [_cancelBtn setTitle:KLanguage(@"取消") forState:UIControlStateNormal];
        _cancelBtn.layer.masksToBounds = YES;
        _cancelBtn.layer.cornerRadius = fontAuto(19);
        _cancelBtn.layer.borderColor = HEXCOLOR(0xDCDEE0).CGColor;
        _cancelBtn.layer.borderWidth =1;
        [_cancelBtn setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
        [_cancelBtn addTarget:self action:@selector(hiddenView) forControlEvents:UIControlEventTouchUpInside];
//        _cancelBtn = [VULButton getCustomBtnWithFrame:CGRectMake(fontAuto(19), fontAuto(138), fontAuto(115), fontAuto(38)) title:@"取消" Font:16 Bgcolor:UIColorHex(#ECECEC) Target:self action:@selector(hiddenView)];
//        _cancelBtn.layer.cornerRadius = fontAuto(19);
//        [_cancelBtn setTitleColor:UIColorHex(#999999) forState:UIControlStateNormal];
    }
    return _cancelBtn;
}

- (VULButton *)sureBtn {
    if (!_sureBtn) {
        _sureBtn = [VULButton getCustomBtnWithFrame:CGRectMake(kViewWidth - fontAuto(19 + 115),fontAuto(138), fontAuto(115), fontAuto(38)) title:KLanguage(@"确定") Font:16 Bgcolor:BtnColor Target:self action:@selector(sureBtnClicked:)];
        _sureBtn.layer.cornerRadius = fontAuto(19);
        [_sureBtn setTitleColor:UIColor.whiteColor forState:UIControlStateNormal];
    }
    return _sureBtn;
}

- (UIView *)joinFreeBillView {
    if (!_joinFreeBillView) {
        _joinFreeBillView = [[UIView alloc] initWithFrame:CGRectMake((VULSCREEN_WIDTH - kViewWidth)/2, (VULSCREEN_HEIGHT_VARIABLE - fontAuto(189))/2 - fontAuto(100), kViewWidth, fontAuto(189))];
        _joinFreeBillView.backgroundColor = [UIColor whiteColor];
        _joinFreeBillView.layer.cornerRadius = 4;
    }
    return _joinFreeBillView;
}

- (UIView *)bgView {
    if (!_bgView) {
        _bgView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_HEIGHT)];
        _bgView.backgroundColor = [UIColor blackColor];
        _bgView.alpha = 0.5;
        _bgView.userInteractionEnabled = YES;
        UITapGestureRecognizer *gesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(hiddenView)];
        [_bgView addGestureRecognizer:gesture];
    }
    return _bgView;
}

- (void)showInView {
    //self.bgView.alpha = 0;
    self.joinFreeBillView.transform = CGAffineTransformMakeScale(0.90, 0.90);
    [UIView animateWithDuration:0.25 animations:^{
        //self.bgView.alpha = 1;
        [self.textField becomeFirstResponder];
        self.joinFreeBillView.transform = CGAffineTransformMakeScale(1.0, 1.0);
    } completion:^(BOOL finished) {
        
    }];
    [kWindow addSubview:self];
}


- (void)hiddenView {
    [UIView animateWithDuration:0.25 animations:^{
        [self removeFromSuperview];
    } completion:^(BOOL finished) {
        
    }];
}

@end
