//
//  VULButton.m
//  VideoULimit
//
//  Created by svnlan on 2018/10/12.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULButton.h"

@implementation VULButton

+ (VULButton *)createImgBtnWithFrame:(CGRect)frame imgNamed:(NSString *)imgName Target:(id)target Sel:(SEL)sel {
    VULButton *btn = [VULButton buttonWithType:UIButtonTypeCustom];
    btn.frame = frame;
    [btn setImage:VULGetImage(imgName) forState:UIControlStateNormal];
    [btn addTarget:target action:sel forControlEvents:UIControlEventTouchUpInside];

    return btn;
}

+ (VULButton *)getCustomBtnWithFrame:(CGRect)frame title:(NSString *)title Font:(CGFloat)font Bgcolor:(UIColor *)BgColor Target:(id)target action:(SEL)action {
    VULButton *btn = [VULButton buttonWithType:UIButtonTypeCustom];
    btn.frame = frame;
    [btn setTitle:title forState:UIControlStateNormal];
    [btn.titleLabel setFont:[UIFont systemFontOfSize:font]];
    if (BgColor == nil) {
        btn.backgroundColor = [UIColor clearColor];
    } else {
        btn.backgroundColor = BgColor;
    }
    [btn addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
    return btn;
}

+ (VULButton *)createMenuBtnWithFrame:(CGRect)frame value:(NSString *)value title:(NSString *)title Target:(id)target Sel:(SEL)sel {
    VULButton *btn = [VULButton buttonWithType:UIButtonTypeCustom];
    btn.frame = frame;
    VULLabel *valueLabel = [VULLabel getLabelWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height / 2) Text:value TxtAlignment:NSTextAlignmentCenter Font:[UIFont boldSystemFontOfSize:20] Color:[UIColor whiteColor] BgColor:nil];
    btn.valueLabel = valueLabel;

    VULLabel *titleLabel = [VULLabel getLabelWithFrame:CGRectMake(0, CGRectGetMaxY(valueLabel.frame) + 5, frame.size.width, frame.size.height / 2 - 5) Text:title TxtAlignment:NSTextAlignmentCenter Font:IS_IPAD ? VULPingFangSCLight(18) : VULPingFangSCLight(22) Color:[UIColor whiteColor] BgColor:nil];
    btn.menuTitleLabel = titleLabel;
    [btn addTarget:target action:sel forControlEvents:UIControlEventTouchUpInside];
    [btn addSubview:valueLabel];
    [btn addSubview:titleLabel];

    return btn;
}

+ (VULButton *)dataStatisticsMenuBtnWithFrame:(CGRect)frame value:(NSString *)value title:(NSString *)title Target:(id)target Sel:(SEL)sel {
    VULButton *btn = [VULButton buttonWithType:UIButtonTypeCustom];
    btn.frame = frame;
    VULLabel *valueLabel = [VULLabel getLabelWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height / 2) Text:value TxtAlignment:NSTextAlignmentCenter Font:[UIFont boldSystemFontOfSize:fontAuto(20)] Color:HEXCOLOR(0x333333) BgColor:nil];
    btn.valueLabel = valueLabel;

    VULLabel *titleLabel = [VULLabel getLabelWithFrame:CGRectMake(0, CGRectGetMaxY(valueLabel.frame) + 5, frame.size.width, frame.size.height / 2 - 5) Text:title TxtAlignment:NSTextAlignmentCenter Font:IS_IPAD ? VULPingFangSCLight(16) : VULPingFangSCLight(18) Color:HEXCOLOR(0x999999) BgColor:nil];
    btn.menuTitleLabel = titleLabel;
    [btn addTarget:target action:sel forControlEvents:UIControlEventTouchUpInside];
    [btn addSubview:valueLabel];
    [btn addSubview:titleLabel];

    [valueLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.right.offset(0);
        make.bottom.mas_equalTo(btn.mas_centerY);
    }];
    [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(valueLabel.mas_bottom).offset(0);
        make.left.right.offset(0);
        make.bottom.offset(-fontAuto(15));
    }];
    
    
    return btn;
}



+ (VULButton *)createDropMenuBtnWithFrame:(CGRect)frame mainTitle:(NSString *)title Target:(id)target Sel:(SEL)sel {
    VULButton *btn = [VULButton buttonWithType:UIButtonTypeCustom];
    btn.frame = frame;
    VULLabel *valueLabel = [VULLabel getLabelWithFrame:CGRectMake(0, 5, frame.size.width - 25, 30) Text:title TxtAlignment:NSTextAlignmentCenter Font:[UIFont systemFontOfSize:15] Color:HEXCOLOR(0x333333) BgColor:nil];
    btn.valueLabel = valueLabel;

    UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(frame.size.width - 20, 12, 16, 16)];
    imgView.image = VULGetImage(@"arrow_lightgray");
    btn.arrowImgView = imgView;
    [btn addTarget:target action:sel forControlEvents:UIControlEventTouchUpInside];
    [btn addSubview:valueLabel];
    [btn addSubview:imgView];
    [valueLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.offset(5);
        make.height.equalTo(@30);
        make.centerX.equalTo(btn.mas_centerX).offset(-8);
        make.width.lessThanOrEqualTo(@(frame.size.width - 25));
    }];
    [imgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.equalTo(btn.mas_centerY);
        make.height.width.equalTo(@16);
        make.left.equalTo(valueLabel.mas_right);
    }];

    return btn;
}

+ (VULButton *)createNewBtnWithFrame:(CGRect)frame title:(NSString *)value imageName:(NSString *)imgName Target:(id)target Sel:(SEL)sel {
    VULButton *btn = [VULButton buttonWithType:UIButtonTypeRoundedRect];
    btn.frame = frame;
    btn.backgroundColor = [UIColor clearColor];

    CGFloat itemWidth = IS_IPAD ? frame.size.height / 5 : frame.size.height / 4;

    UIImageView *imageView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:imgName]];
    imageView.frame = CGRectMake(0, 0, itemWidth, itemWidth);

    VULLabel *titleLabel = [VULLabel getLabelWithFrame:CGRectMake(CGRectGetMaxX(imageView.frame) + 5, 0, CGRectGetWidth(frame) - imageView.frame.size.width, CGRectGetHeight(frame)) Text:value TxtAlignment:NSTextAlignmentCenter Font:IS_IPAD ? [UIFont systemFontOfSize:17] : [UIFont systemFontOfSize:15] Color:[UIColor blackColor] BgColor:nil];
    titleLabel.backgroundColor = [UIColor clearColor];
    btn.valueLabel = titleLabel;
    [btn addSubview:imageView];
    [btn addSubview:titleLabel];
    [imageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.width.equalTo(@(itemWidth));
        make.centerX.equalTo(btn.mas_centerX);
        make.bottom.equalTo(btn.mas_centerY);
    }];
    [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(imageView.mas_bottom).offset(5);
        make.left.right.offset(0);
        make.height.equalTo(@25);
    }];

    [btn addTarget:target action:sel forControlEvents:UIControlEventTouchUpInside];
    return btn;
}

+ (VULButton *)getNewStudyBtnWithFrame:(CGRect)frame title:(NSString *)value imageName:(NSString *)imgName Target:(id)target Sel:(SEL)sel {
    VULButton *btn = [VULButton buttonWithType:UIButtonTypeRoundedRect];
    btn.frame = frame;
    btn.backgroundColor = [UIColor clearColor];

    CGFloat itemWidth = IS_IPAD ? frame.size.height / 3 : frame.size.height / 2;

    UIImageView *imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:imgName]];
    imageView.frame = CGRectMake(0, 0, itemWidth, itemWidth);

    VULLabel *titleLabel = [VULLabel getLabelWithFrame:CGRectMake(CGRectGetMaxX(imageView.frame) + 5, 0, CGRectGetWidth(frame) - imageView.frame.size.width, CGRectGetHeight(frame)) Text:value TxtAlignment:NSTextAlignmentCenter Font:IS_IPAD ? [UIFont systemFontOfSize:17] : [UIFont systemFontOfSize:14]  Color:[UIColor blackColor] BgColor:nil];
    titleLabel.backgroundColor = [UIColor clearColor];
    btn.valueLabel = titleLabel;
    [btn addSubview:imageView];
    [btn addSubview:titleLabel];
    [imageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.width.equalTo(@(itemWidth));
        make.centerX.equalTo(btn.mas_centerX);
        make.bottom.equalTo(btn.mas_centerY).offset(kSpace);
    }];
    [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        IS_IPAD ? make.top.equalTo(imageView.mas_bottom).offset(VULPercentageHeight(5)) : make.top.equalTo(imageView.mas_bottom).offset(2);
        make.left.right.offset(0);
        make.height.equalTo(@25);
    }];

    [btn addTarget:target action:sel forControlEvents:UIControlEventTouchUpInside];
    return btn;
}

+ (VULButton *)getNewMenuBtnWithFrame:(CGRect)frame title:(NSString *)value imageName:(NSString *)imgName Target:(id)target Sel:(SEL)sel {
    VULButton *btn = [VULButton buttonWithType:UIButtonTypeCustom];
    btn.frame = frame;
    btn.backgroundColor = [UIColor clearColor];
    
    CGFloat itemWidth = IS_IPAD ? frame.size.height / 4 : frame.size.height / 3;
    
    UIImage *iconImage = [UIImage imageForIconBundle:imgName];
    if (iconImage == nil) {
        iconImage = VULGetImage(imgName);
    }
    UIImageView *imageView = [[UIImageView alloc] initWithImage:iconImage];
    imageView.frame = CGRectMake(0, 0, itemWidth, itemWidth);
    btn.arrowImgView = imageView;
    VULLabel *titleLabel = [VULLabel getLabelWithFrame:CGRectMake(CGRectGetMaxX(imageView.frame) + 5, 0, CGRectGetWidth(frame) - imageView.frame.size.width, CGRectGetHeight(frame)) Text:value TxtAlignment:NSTextAlignmentCenter Font:IS_IPAD ? [UIFont systemFontOfSize:17] : [UIFont systemFontOfSize:14]  Color:[UIColor blackColor] BgColor:nil];
    titleLabel.backgroundColor = [UIColor clearColor];
    btn.valueLabel = titleLabel;
    [btn addSubview:imageView];
    [btn addSubview:titleLabel];
    [imageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.width.equalTo(@(itemWidth));
        make.centerX.equalTo(btn.mas_centerX);
        make.bottom.equalTo(btn.mas_centerY).offset(kSpace);
    }];
    [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        IS_IPAD ? make.top.equalTo(imageView.mas_bottom).offset(VULPercentageHeight(5)) : make.top.equalTo(imageView.mas_bottom).offset(2);
        make.left.right.offset(0);
        make.height.equalTo(@25);
    }];
    
    VULLabel *countLabel = [VULLabel getLabelWithFrame:CGRectMake(CGRectGetMaxX(imageView.frame) + 5, 0, CGRectGetWidth(frame) - imageView.frame.size.width, CGRectGetHeight(frame)) Text:@"" TxtAlignment:NSTextAlignmentCenter Font:IS_IPAD ? [UIFont boldSystemFontOfSize:13] : [UIFont boldSystemFontOfSize:11]  Color:HEXCOLOR(0x108EE9) BgColor:nil];
    [btn addSubview:countLabel];
    btn.countLabel = countLabel;
    [countLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(imageView.mas_right).offset(VULPercentageHeight(3));
        make.centerY.equalTo(imageView.mas_top).offset(-VULPercentageHeight(3));
    }];
    
    [btn addTarget:target action:sel forControlEvents:UIControlEventTouchUpInside];
    return btn;
}


@end
