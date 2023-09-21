//
//  VULButton.h
//  VideoULimit
//
//  Created by svnlan on 2018/10/12.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VULLabel.h"

@interface VULButton : UIButton

@property (nonatomic,strong) VULLabel *menuTitleLabel;
@property (nonatomic,strong) VULLabel *valueLabel;
@property (nonatomic,strong) VULLabel *countLabel;
@property (nonatomic,strong) UIImageView *arrowImgView;

+ (VULButton *)getCustomBtnWithFrame:(CGRect)frame title:(NSString *)title Font:(CGFloat)font Bgcolor:(UIColor *)BgColor Target:(id)target action:(SEL)action;

+ (VULButton *)createImgBtnWithFrame:(CGRect)frame imgNamed:(NSString *)imgName Target:(id)target Sel:(SEL)sel;

+(VULButton *)createMenuBtnWithFrame:(CGRect)frame value:(NSString *)value title:(NSString *)title Target:(id)target Sel:(SEL)sel;

+ (VULButton *)dataStatisticsMenuBtnWithFrame:(CGRect)frame value:(NSString *)value title:(NSString *)title Target:(id)target Sel:(SEL)sel;

+ (VULButton *)createDropMenuBtnWithFrame:(CGRect)frame mainTitle:(NSString *)title Target:(id)target Sel:(SEL)sel;

+(VULButton *)createNewBtnWithFrame:(CGRect)frame title:(NSString *)value imageName:(NSString *)imgName Target:(id)target Sel:(SEL)sel;

+ (VULButton *)getNewStudyBtnWithFrame:(CGRect)frame title:(NSString *)value imageName:(NSString *)imgName Target:(id)target Sel:(SEL)sel;

+ (VULButton *)getNewMenuBtnWithFrame:(CGRect)frame title:(NSString *)value imageName:(NSString *)imgName Target:(id)target Sel:(SEL)sel;

@end
