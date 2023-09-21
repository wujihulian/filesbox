//
//  VULLoginAuthView.m
//  VideoULimit
//
//  Created by 无极互联 on 2020/8/31.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "VULLoginAuthView.h"

@interface VULLoginAuthView()
@property (nonatomic, strong) NSString *keyString;
@property (nonatomic, strong) UIVisualEffectView *blurView;
@property (nonatomic, strong) UIImageView *deviceImgView;
@property (nonatomic, strong) VULLabel *loginTipsLabel;
@property (nonatomic, strong) VULButton *closeButton;
@property (nonatomic, strong) VULButton *loginButton;
@property (nonatomic, strong) VULButton *cancelButton;

@end

@implementation VULLoginAuthView

- (instancetype)initWithFrame:(CGRect)frame key:(nonnull NSString *)keyStr
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.keyString = keyStr;
        [self createUI];
    }
    return self;
}

- (void)createUI {
    [self addSubview:self.blurView];
    [self addSubview:self.closeButton];
    [self addSubview:self.deviceImgView];
    [self addSubview:self.loginTipsLabel];
    [self addSubview:self.loginButton];
    [self addSubview:self.cancelButton];
    
    [self.blurView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(self);
    }];
    [self.closeButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.height.mas_equalTo(44);
        make.top.offset(30);
        make.left.offset(20);
    }];
    [self.deviceImgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(VULSCREEN_WIDTH / 3 + fontAuto(40));
        make.centerX.mas_equalTo(self.mas_centerX);
        make.top.mas_equalTo(self.closeButton.mas_bottom).offset(fontAuto(120));
        make.height.mas_equalTo(self.deviceImgView.mas_width).multipliedBy(11.0f / 16.0f);
    }];
    [self.loginTipsLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.deviceImgView.mas_bottom).offset(fontAuto(20));
        make.centerX.mas_equalTo(self.mas_centerX);
    }];
    [self.loginButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.offset(VULSCREEN_HEIGHT / 4 * 3);
        make.centerX.mas_equalTo(self.mas_centerX);
        make.height.mas_equalTo(44);
        make.width.mas_equalTo(VULSCREEN_WIDTH / 3 + fontAuto(80));
    }];
    [self.cancelButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.loginButton.mas_bottom).offset(fontAuto(15));
        make.centerX.mas_equalTo(self.mas_centerX);
        make.height.mas_equalTo(40);
        make.width.mas_equalTo(fontAuto(100));
    }];
    NSString *deviceNameStr = [self.keyString substringFromIndex:self.keyString.length-1];
    if (![deviceNameStr isEqualToString:@"t"]) {
        self.loginTipsLabel.text = @"网校登录确认";
        self.deviceImgView.image = VULGetImage(@"web_login");
    }
}



- (void)handleButtonAction:(VULButton *)sender {
    if (self.handerActionBlock) {
        self.handerActionBlock(sender);
    }
}


#pragma mark - getter
- (UIVisualEffectView *)blurView {
    if (!_blurView) {
        UIBlurEffect *blurEffect = [UIBlurEffect effectWithStyle:UIBlurEffectStyleLight];
        _blurView = [[UIVisualEffectView alloc] initWithEffect:blurEffect];
        _blurView.frame = self.bounds;
    }
    return _blurView;
}

- (UIImageView *)deviceImgView {
    if (!_deviceImgView) {
        _deviceImgView = [[UIImageView alloc] init];
        _deviceImgView.image = VULGetImage(@"tv_login");
    }
    return _deviceImgView;
}

- (VULLabel *)loginTipsLabel {
    if (!_loginTipsLabel) {
        _loginTipsLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"空中课堂TV登录确认" TxtAlignment:NSTextAlignmentCenter Font:VULPingFangSCBold(25) Color:HEXCOLOR(0x333333) BgColor:nil];
    }
    return _loginTipsLabel;
}

- (VULButton *)closeButton {
    if (!_closeButton) {
        _closeButton = [VULButton getCustomBtnWithFrame:CGRectZero title:@"关闭" Font:20 Bgcolor:nil Target:self action:@selector(handleButtonAction:)];
        [_closeButton setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
        _closeButton.tag = 5831;
    }
    return _closeButton;
}

- (VULButton *)loginButton {
    if (!_loginButton) {
        _loginButton = [VULButton getCustomBtnWithFrame:CGRectZero title:@"登录" Font:20 Bgcolor:HEXCOLOR(0x289DFF) Target:self action:@selector(handleButtonAction:)];
        [_loginButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _loginButton.layer.masksToBounds = YES;
        _loginButton.layer.cornerRadius = 22.0f;
        _loginButton.tag = 5832;
    }
    return _loginButton;
}

- (VULButton *)cancelButton {
    if (!_cancelButton) {
        _cancelButton = [VULButton getCustomBtnWithFrame:CGRectZero title:@"取消登录" Font:16 Bgcolor:nil Target:self action:@selector(handleButtonAction:)];
        [_cancelButton setTitleColor:HEXCOLOR(0x666666) forState:UIControlStateNormal];
        _cancelButton.tag = 5833;
    }
    return _cancelButton;
}

@end
