//
//  VULUpdatePopupView.m
//  VideoULimit
//
//  Created by svnlan on 2020/3/7.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "VULUpdatePopupView.h"

@interface VULUpdatePopupView ()<UITextViewDelegate>

@property (nonatomic, strong) VULAppstoreInfo *versionInfo; //升级文字

@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *versionLabel;
@property (nonatomic, strong) UIImageView *bgImageView;
@property (nonatomic, strong) UIImageView *rocketImageView;
@property (nonatomic, strong) VULButton *updateButton;
@property (nonatomic, strong) UITextView *tipsTextView;
@property (nonatomic, strong) VULButton *closeButton;

@end

@implementation VULUpdatePopupView

- (instancetype)initWithFrame:(CGRect)frame withUpdateInfo:(VULAppstoreInfo *)info {
    self = [super initWithFrame:frame];
    if (self) {
        self.versionInfo = info;
        [self setControl];
    }
    return self;
}

- (void)setControl {
    self.backgroundColor = [UIColor colorWithWhite:0 alpha:0.4];
    [self addSubview:self.bgImageView];
    [self addSubview:self.rocketImageView];
    [self addSubview:self.titleLabel];
    [self addSubview:self.versionLabel];
    [self addSubview:self.tipsTextView];
    [self addSubview:self.updateButton];
    [self addSubview:self.closeButton];

    [self.bgImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.centerY.equalTo(self);
        IS_IPAD ? make.width.equalTo(@(VULSCREEN_WIDTH / 2)) : make.width.equalTo(@(VULSCREEN_WIDTH * 0.75));
        make.height.equalTo(@(VULSCREEN_HEIGHT * .45));
    }];

    [self.rocketImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self.bgImageView.mas_right).offset(-VULPercentageHeight(15));
        make.top.equalTo(self.bgImageView.mas_top).offset(-60);
        make.height.equalTo(@(VULSCREEN_HEIGHT / 4));
        IS_IPAD ? make.width.equalTo(@(VULSCREEN_WIDTH / 6)) : make.width.equalTo(@(VULSCREEN_WIDTH / 5));
    }];

    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.bgImageView.mas_top).offset(2 * kSpace);
        make.left.equalTo(self.bgImageView.mas_left).offset(2 * kSpace);
        make.right.equalTo(self.rocketImageView.mas_left);
        make.height.equalTo(@(VULPercentageWidth(30)));
    }];
    [self.versionLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.titleLabel.mas_bottom);
        make.right.height.equalTo(self.titleLabel);
        make.left.equalTo(self.titleLabel.mas_left).offset(2);
    }];
    self.versionLabel.text = [NSString stringWithFormat:@"V%@", self.versionInfo.version];
    self.tipsTextView.text = self.versionInfo.releaseNotes;
    CGFloat offset = VULPercentageHeight(20);
    [self.tipsTextView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.bgImageView.mas_left).offset(offset);
        make.right.equalTo(self.bgImageView.mas_right).offset(-offset);
        make.top.equalTo(self.rocketImageView.mas_bottom).offset(3 * kSpace);
        make.bottom.equalTo(self.updateButton.mas_top).offset(-3 * kSpace);
    }];
    [self.updateButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.equalTo(self.bgImageView.mas_bottom).offset(-3 * kSpace);
        make.height.equalTo(@40);
        make.left.right.equalTo(self.tipsTextView);
    }];
    [self.closeButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.bgImageView.mas_bottom).offset(4 * kSpace);
        make.height.width.equalTo(@(50));
        make.centerX.equalTo(self.mas_centerX);
    }];

    [[self.closeButton rac_signalForControlEvents:UIControlEventTouchUpInside] subscribeNext:^(__kindof UIControl *_Nullable x) {
        [self closeUpdatePopupView];
    }];

    [[self.updateButton rac_signalForControlEvents:UIControlEventTouchUpInside] subscribeNext:^(__kindof UIControl *_Nullable x) {
        if (self.GoToUpgradeBlock) {
            self.GoToUpgradeBlock();
        }
        [self closeUpdatePopupView];
    }];
}

- (void)showUpdatePopupView {
    [UIView animateWithDuration:0.2 animations:^{
        self.backgroundColor = [UIColor colorWithWhite:0 alpha:0.6];
        self.transform = CGAffineTransformMakeScale(1, 1);
    } completion:^(BOOL finished) {
    }];
}

- (void)closeUpdatePopupView {
    [UIView animateWithDuration:0.2 animations:^{
        self.transform = CGAffineTransformMakeScale(0.01, 0.01);
        self.alpha = 0.01;
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
}


- (BOOL)textViewShouldBeginEditing:(UITextView *)textView {
    return NO;
}


#pragma mark - getter
- (UIImageView *)bgImageView {
    if (!_bgImageView) {
        _bgImageView = [[UIImageView alloc] initWithFrame:CGRectZero];
        _bgImageView.image = VULGetImage(@"update_bg_content");
    }
    return _bgImageView;
}

- (UIImageView *)rocketImageView {
    if (!_rocketImageView) {
        _rocketImageView = [[UIImageView alloc] initWithFrame:CGRectZero];
        _rocketImageView.image = VULGetImage(@"update_rocket");
    }
    return _rocketImageView;
}

- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"发现新版本" TxtAlignment:NSTextAlignmentLeft Font:VULPingFangSCMedium(35) Color:[UIColor whiteColor] BgColor:nil];
    }
    return _titleLabel;
}

- (VULLabel *)versionLabel {
    if (!_versionLabel) {
        _versionLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"V2.0" TxtAlignment:NSTextAlignmentLeft Font:VULPingFangSCMedium(35) Color:[UIColor whiteColor] BgColor:nil];
    }
    return _versionLabel;
}

- (UITextView *)tipsTextView {
    if (!_tipsTextView) {
        _tipsTextView = [[UITextView alloc] initWithFrame:CGRectZero];
        _tipsTextView.backgroundColor = [UIColor clearColor];
        _tipsTextView.textColor = HEXCOLOR(0x666666);
        _tipsTextView.font = [UIFont systemFontOfSize:16];
        _tipsTextView.delegate = self;
    }
    return _tipsTextView;
}

- (VULButton *)updateButton {
    if (!_updateButton) {
        _updateButton = [VULButton getCustomBtnWithFrame:CGRectZero title:@"立即升级" Font:22 Bgcolor:nil Target:self action:nil];
        _updateButton.backgroundColor = HEXCOLOR(0x108EE9);
        UIFont *buttonFont = IS_IPAD ? [UIFont systemFontOfSize:22] : [UIFont systemFontOfSize:20];
        [_updateButton.titleLabel setFont:buttonFont];

        [_updateButton.titleLabel setTextColor:[UIColor whiteColor]];
        _updateButton.layer.cornerRadius = 20;
        _updateButton.layer.masksToBounds = YES;
    }
    return _updateButton;
}

- (VULButton *)closeButton {
    if (!_closeButton) {
        _closeButton = [VULButton createImgBtnWithFrame:CGRectZero imgNamed:@"update_close" Target:self Sel:nil];
    }
    return _closeButton;
}

@end
