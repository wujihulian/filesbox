//
//  UIButton+VULEdgeInsets.h
//  VideoULimit
//
//  Created by svnlan on 2018/10/11.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, VULImagePositionType) {
    VULImagePositionTypeLeft,   //图片在左，标题在右，默认风格
    VULImagePositionTypeRight,  //图片在右，标题在左
    VULImagePositionTypeTop,    //图片在上，标题在下
    VULImagePositionTypeBottom  //图片在下，标题在上
};

typedef NS_ENUM(NSInteger, VULEdgeInsetsType) {
    VULEdgeInsetsTypeTitle,//标题
    VULEdgeInsetsTypeImage//图片
};

typedef NS_ENUM(NSInteger, VULMarginType) {
    VULMarginTypeTop         ,
    VULMarginTypeBottom      ,
    VULMarginTypeLeft        ,
    VULMarginTypeRight       ,
    VULMarginTypeTopLeft     ,
    VULMarginTypeTopRight    ,
    VULMarginTypeBottomLeft  ,
    VULMarginTypeBottomRight
};

typedef NS_ENUM(NSInteger, ButtonImageTitleStyle ) {
    ButtonImageTitleStyleDefault = 0,       //图片在左，文字在右，整体居中。
    ButtonImageTitleStyleLeft  = 0,         //图片在左，文字在右，整体居中。
    ButtonImageTitleStyleRight     = 2,     //图片在右，文字在左，整体居中。
    ButtonImageTitleStyleTop  = 3,          //图片在上，文字在下，整体居中。
    ButtonImageTitleStyleBottom    = 4,     //图片在下，文字在上，整体居中。
    ButtonImageTitleStyleCenterTop = 5,     //图片居中，文字在上距离按钮顶部。
    ButtonImageTitleStyleCenterBottom = 6,  //图片居中，文字在下距离按钮底部。
    ButtonImageTitleStyleCenterUp = 7,      //图片居中，文字在图片上面。
    ButtonImageTitleStyleCenterDown = 8,    //图片居中，文字在图片下面。
    ButtonImageTitleStyleRightLeft = 9,     //图片在右，文字在左，距离按钮两边边距
    ButtonImageTitleStyleLeftRight = 10,    //图片在左，文字在右，距离按钮两边边距
};

@interface UIButton (VULEdgeInsets)

- (void)setImagePositionWithType:(VULImagePositionType)type spacing:(CGFloat)spacing;
- (void)setEdgeInsetsWithType:(VULEdgeInsetsType)edgeInsetsType marginType:(VULMarginType)marginType margin:(CGFloat)margin;
- (void)setImageUpTitleDownWithSpacing:(CGFloat)spacing __deprecated_msg("Method deprecated. Use `setImagePositionWithType:spacing:`");
- (void)setImageRightTitleLeftWithSpacing:(CGFloat)spacing __deprecated_msg("Method deprecated. Use `setImagePositionWithType:spacing:`");


- (void)setButtonImageTitleStyle:(ButtonImageTitleStyle)style padding:(CGFloat)padding;

@end
