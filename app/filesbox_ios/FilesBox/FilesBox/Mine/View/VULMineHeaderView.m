//
//  VULMineHeaderView.m
//  VideoULimit
//
//  Created by svnlan on 2018/10/12.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULMineHeaderView.h"

#import "YLProgressBar.h"

@interface VULMineHeaderView ()

@property (nonatomic, strong) UIImageView *topImgView;
@property (nonatomic, strong) YLProgressBar *completProgress;
@property (nonatomic, strong) VULLabel *beatRateLabel; ///完善度、击败率
@property (nonatomic, strong) UIImageView *sexImageV;
@property (nonatomic, strong) UIImageView *rightImage;



@end

@implementation VULMineHeaderView

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];
        [self createUI];
    }
    return self;
}

- (void)createUI {
    [self addSubview:self.topImgView];
    [self addSubview:self.bgImgView];
;
    [self.bgImgView addSubview:self.portraitImgView];
    [self.bgImgView addSubview:self.sexImageV];
    [self.bgImgView addSubview:self.realNameLabel];
    [self.bgImgView addSubview:self.completProgress];
    [self.bgImgView addSubview:self.beatRateLabel];
    [self.bgImgView addSubview:self.userNameLabel];
    [self.bgImgView addSubview:self.rightImage];

    
    [self.topImgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.right.offset(0);
        make.height.mas_equalTo(fontAuto(140));
    }];
    
    [self.bgImgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.offset((VUL_IS_NOTCH ? fontAuto(20) : fontAuto(50)) + K_StatusBar_Height);
        make.left.offset(fontAuto(15));
        make.right.offset(-fontAuto(15));
        make.height.mas_equalTo(fontAuto(110));
    }];
    [self.portraitImgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.height.mas_equalTo(fontAuto(70));
        make.left.top.offset(fontAuto(20));
//        make.centerY.mas_equalTo(self.bgImgView.mas_centerY);
    }];

    [self.sexImageV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.portraitImgView.mas_right).offset(-15);
        make.top.mas_equalTo(self.portraitImgView.mas_bottom).offset(-15);
        make.height.mas_equalTo(15);
    }];
    [self.realNameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.portraitImgView.mas_right).offset(kSpace);
//        make.height.equalTo(@(3 * kSpace));
//        make.width.mas_lessThanOrEqualTo(VULSCREEN_WIDTH * 0.75);
        make.right.mas_equalTo(-25);
        make.top.mas_equalTo(fontAuto(20));
    }];
   
    [self.userNameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.portraitImgView.mas_right).offset(kSpace);
        make.right.mas_equalTo(-25);
        make.top.mas_equalTo(self.realNameLabel.mas_bottom).offset(0);

    }];
    [self.rightImage mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self.realNameLabel.mas_centerY);
        make.right.mas_equalTo(-15);
        make.width.mas_equalTo(16);
    }];
    
    [self.beatRateLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.realNameLabel.mas_left);
        make.right.mas_equalTo(-fontAuto(15));
        make.top.mas_equalTo(self.userNameLabel.mas_bottom).offset(fontAuto(0));
    }];
   
    [self.completProgress mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.mas_equalTo(fontAuto(6));
        make.left.mas_equalTo(self.realNameLabel.mas_left);
        make.right.offset(-fontAuto(15));
        make.top.mas_equalTo(self.beatRateLabel.mas_bottom).offset(fontAuto(4));
    }];

    VULSaveUserInformation *userInfo = [VULRealmDBManager getLocalUserInformational];
    self.realNameLabel.text = userInfo.nickname.length>0?userInfo.nickname:userInfo.name;
    self.userNameLabel.text = userInfo.nickname.length>0?userInfo.name:@"";
    
    if(userInfo.nickname.length>0){
        [self.realNameLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.equalTo(self.portraitImgView.mas_right).offset(kSpace);
    //        make.height.equalTo(@(3 * kSpace));
    //        make.width.mas_lessThanOrEqualTo(VULSCREEN_WIDTH * 0.75);
            make.right.mas_equalTo(-25);
            make.top.mas_equalTo(fontAuto(16));
        }];
    }else{
        [self.realNameLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.equalTo(self.portraitImgView.mas_right).offset(kSpace);
    //        make.height.equalTo(@(3 * kSpace));
    //        make.width.mas_lessThanOrEqualTo(VULSCREEN_WIDTH * 0.75);
            make.right.mas_equalTo(-25);
            make.top.mas_equalTo(fontAuto(20));
        }];
    }
    
    
    
    [self.portraitImgView sd_setImageWithURL:[NSURL URLWithString:resultsUrl(userInfo.avatar)] placeholderImage:VULGetImage(@"placeholder_face")];
    [[NSUserDefaults standardUserDefaults] setValue:resultsUrl(userInfo.avatar) forKey:@"loginImg"];
//    云存储 8G/100g
    self.sexImageV.hidden = YES;
    NSString *sizeMax = [[NSUserDefaults standardUserDefaults] objectForKey:@"sizeMax"];
    NSString *sizeUse = [[NSUserDefaults standardUserDefaults] objectForKey:@"sizeUse"];
    if (sizeMax.intValue > 0) {
        self.beatRateLabel.text = [NSString stringWithFormat:@"%@/%@GB",[self formattedFileSize:sizeUse.longLongValue],sizeMax];
        CGFloat pre = sizeUse.longLongValue/ (sizeMax.longLongValue* 1024.0 * 1024.0 * 1024.0);
        [self.completProgress setProgress:pre animated:YES];

    }else{
        self.beatRateLabel.text = [NSString stringWithFormat:@" %@/%@",[self formattedFileSize:sizeUse.longLongValue],KLanguage(@"无限制") ];
    }
    
    self.sexImageV.image =  [UIImage imageNamed:userInfo.sex == 0 ? @"colleague_女" : @"colleague_男"];
    self.userInteractionEnabled = YES;
    self.bgImgView.userInteractionEnabled = YES;
    UITapGestureRecognizer *sender = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickBg)];
    [self.bgImgView addGestureRecognizer:sender];
}
-(void)clickBg{
    if(self.clickBgImg){
        self.clickBgImg();
    }
}

- (NSString *)formattedFileSize:(long long)bytes {
CGFloat convertedValue;
NSString *sizeSuffix;

if (bytes >= 1024*1024*1024) {
    convertedValue = bytes / (1024.0 * 1024.0 * 1024.0);
    sizeSuffix = @"GB";
} else {
    convertedValue = bytes / (1024.0 * 1024.0);
    sizeSuffix = @"MB";
}

NSString *result = [NSString stringWithFormat:@"%.2f %@", convertedValue, sizeSuffix];
return result;
}







#pragma mark -getter
- (UIImageView *)topImgView {
    if (!_topImgView) {
        _topImgView = [[UIImageView alloc] init];
        _topImgView.image = VULGetImage(@"背景");
    }
    return _topImgView;
}

- (UIImageView *)bgImgView {
    if (!_bgImgView) {
        _bgImgView = [[UIImageView alloc] init];
        _bgImgView.image = VULGetImage(@"minecard_bg");
        _bgImgView.contentMode = UIViewContentModeScaleAspectFill;
        _bgImgView.clipsToBounds = YES;
        _bgImgView.layer.cornerRadius = 8.f;
        _bgImgView.layer.masksToBounds = YES;
        _bgImgView.userInteractionEnabled = YES;
    }
    return _bgImgView;
}

- (UIImageView *)portraitImgView {
    if (!_portraitImgView) {
        _portraitImgView = [[UIImageView alloc] init];
        _portraitImgView.layer.masksToBounds = YES;
    _portraitImgView.layer.cornerRadius = fontAuto(35);
        _portraitImgView.contentMode = UIViewContentModeScaleAspectFill;
        _portraitImgView.clipsToBounds = YES;
      ;
    }
    return _portraitImgView;
}

- (UIImageView *)sexImageV {
    if (!_sexImageV) {
        _sexImageV = [[UIImageView alloc] init];
        _sexImageV.contentMode =UIViewContentModeScaleAspectFit;;

        
      ;
    }
    return _sexImageV;
}
- (UIImageView *)rightImage {
    if (!_rightImage) {
        _rightImage = [[UIImageView alloc] init];
        _rightImage.contentMode =UIViewContentModeScaleAspectFit;;
        UIImage *image = VULGetImage(@"icon_right");
        image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
        _rightImage.image =image;
        _rightImage.tintColor = BtnColor;
        
      ;
    }
    return _rightImage;
}



- (VULLabel *)realNameLabel {
    if (!_realNameLabel) {
        _realNameLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont boldSystemFontOfSize:28 * SCALE_FONT] Color:HEXCOLOR(0x7459E3) BgColor:nil];
        _realNameLabel.numberOfLines = 1;
    }
    return _realNameLabel;
}






- (YLProgressBar *)completProgress {
    if (!_completProgress) {
        _completProgress = [[YLProgressBar alloc] initWithFrame:CGRectZero];
        _completProgress.height = fontAuto(6.0);
        _completProgress.type = YLProgressBarTypeRounded;
        _completProgress.progressTintColors = @[UIColorHex(#BB60FF), UIColorHex(#7459E3)];
        _completProgress.hideStripes  = YES;
        _completProgress.hideTrack = YES;
        _completProgress.behavior = YLProgressBarBehaviorDefault;
        _completProgress.backgroundColor =  [UIColorHex(#ffffff) colorWithAlphaComponent:0.2];
        _completProgress.layer.cornerRadius = _completProgress.height / 2.0;
        _completProgress.layer.masksToBounds = YES;
        _completProgress.progressBarInset = 0;
        _completProgress.hideInnerWhiteShadow = YES;
        _completProgress.progress = 0;

    }
    return _completProgress;
}

- (VULLabel *)beatRateLabel {
    if (!_beatRateLabel) {
        _beatRateLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"名片完成度 50% 超过20%用户" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:FontAuto(15)] Color:HEXCOLOR(0x7459E3) BgColor:nil];
    }
    return _beatRateLabel;
}
- (VULLabel *)userNameLabel {
    if (!_userNameLabel) {
        _userNameLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"名片完成度 50% 超过20%用户" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:FontAuto(15)] Color:HEXCOLOR(0x7459E3) BgColor:nil];
    }
    return _userNameLabel;
}

@end
