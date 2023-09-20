//
//  VULMineHeaderView.h
//  VideoULimit
//
//  Created by svnlan on 2018/10/12.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>



@class VULResponseUserInfoModel;
@interface VULMineHeaderView : UIView
@property (nonatomic, strong) UIImageView *bgImgView;
@property (nonatomic, strong) UIImageView *portraitImgView;
@property (nonatomic, copy) void (^clickBgImg)();
@property (nonatomic, strong) VULLabel *realNameLabel; ///实名
@property (nonatomic, strong) VULLabel *userNameLabel; ///实名




@end
