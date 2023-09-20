//
//  VULLabel.h
//  VideoULimit
//
//  Created by svnlan on 2018/10/12.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum {
    VerticalAlignmentTop = 0, // default
    VerticalAlignmentMiddle,
    VerticalAlignmentBottom,
} VerticalAlignment;

@interface VULLabel : UILabel

@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *textLabel;
@property (nonatomic) VerticalAlignment verticalAlignment;

- (instancetype)initWithFrame:(CGRect)frame Title:(NSString *)title Text:(NSString *)text  TxtAlignment:(NSTextAlignment)txtAlignment TextFont:(UIFont *)font width:(CGFloat)titleWidth offSet:(CGFloat)offSet;



+ (instancetype)getLabelWithFrame:(CGRect)frame Text:(NSString *)text TxtAlignment:(NSTextAlignment)txtAlignment Font:(UIFont *)font Color:(UIColor*)textColor BgColor:(UIColor *)color;


@end
