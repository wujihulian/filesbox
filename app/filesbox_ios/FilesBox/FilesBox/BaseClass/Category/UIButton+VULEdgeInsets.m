//
//  UIButton+VULEdgeInsets.m
//  VideoULimit
//
//  Created by svnlan on 2018/10/11.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "UIButton+VULEdgeInsets.h"

#if __IPHONE_OS_VERSION_MIN_REQUIRED >= 70000
#define VUL_SINGLELINE_TEXTSIZE(text, font) [text length] > 0 ? [text \
                                                                 sizeWithAttributes:@{ NSFontAttributeName: font }] : CGSizeZero;
#else
#define VUL_SINGLELINE_TEXTSIZE(text, font) [text length] > 0 ? [text sizeWithFont:font] : CGSizeZero;
#endif

@implementation UIButton (VULEdgeInsets)

- (void)setImagePositionWithType:(VULImagePositionType)type spacing:(CGFloat)spacing {
    CGSize imageSize = [self imageForState:UIControlStateNormal].size;
    CGSize titleSize = VUL_SINGLELINE_TEXTSIZE([self titleForState:UIControlStateNormal], self.titleLabel.font);

    switch (type) {
        case VULImagePositionTypeLeft: {
            self.titleEdgeInsets = UIEdgeInsetsMake(0, spacing, 0, 0);
            self.imageEdgeInsets = UIEdgeInsetsMake(0, 0, 0, spacing);
            break;
        }
        case VULImagePositionTypeRight: {
            self.titleEdgeInsets = UIEdgeInsetsMake(0, -imageSize.width, 0, imageSize.width + spacing);
            self.imageEdgeInsets = UIEdgeInsetsMake(0, titleSize.width + spacing, 0, -titleSize.width);
            break;
        }
        case VULImagePositionTypeTop: {
            // lower the text and push it left so it appears centered
            //  below the image
            self.titleEdgeInsets = UIEdgeInsetsMake(0, -imageSize.width, -(imageSize.height + spacing), 0);

            // raise the image and push it right so it appears centered
            //  above the text
            self.imageEdgeInsets = UIEdgeInsetsMake(-(titleSize.height + spacing), 0, 0, -titleSize.width);
            break;
        }
        case VULImagePositionTypeBottom: {
            self.titleEdgeInsets = UIEdgeInsetsMake(-(imageSize.height + spacing), -imageSize.width, 0, 0);
            self.imageEdgeInsets = UIEdgeInsetsMake(0, 0, -(titleSize.height + spacing), -titleSize.width);
            break;
        }
    }
}

- (void)setImageUpTitleDownWithSpacing:(CGFloat)spacing {
    [self setImagePositionWithType:VULImagePositionTypeTop spacing:spacing];
}

- (void)setImageRightTitleLeftWithSpacing:(CGFloat)spacing {
    [self setImagePositionWithType:VULImagePositionTypeRight spacing:spacing];
}

- (void)setEdgeInsetsWithType:(VULEdgeInsetsType)edgeInsetsType marginType:(VULMarginType)marginType margin:(CGFloat)margin {
    CGSize itemSize = CGSizeZero;
    if (edgeInsetsType == VULEdgeInsetsTypeTitle) {
        itemSize = VUL_SINGLELINE_TEXTSIZE([self titleForState:UIControlStateNormal], self.titleLabel.font);
    } else {
        itemSize = [self imageForState:UIControlStateNormal].size;
    }

    CGFloat horizontalDelta = (CGRectGetWidth(self.frame) - itemSize.width) / 2.f - margin;
    CGFloat vertivalDelta = (CGRectGetHeight(self.frame) - itemSize.height) / 2.f - margin;

    NSInteger horizontalSignFlag = 1;
    NSInteger verticalSignFlag = 1;

    switch (marginType) {
        case VULMarginTypeTop: {
            horizontalSignFlag = 0;
            verticalSignFlag = -1;
            break;
        }
        case VULMarginTypeBottom: {
            horizontalSignFlag = 0;
            verticalSignFlag = 1;
            break;
        }
        case VULMarginTypeLeft: {
            horizontalSignFlag = -1;
            verticalSignFlag = 0;
            break;
        }
        case VULMarginTypeRight: {
            horizontalSignFlag = 1;
            verticalSignFlag = 0;
            break;
        }
        case VULMarginTypeTopLeft: {
            horizontalSignFlag = -1;
            verticalSignFlag = -1;
            break;
        }
        case VULMarginTypeTopRight: {
            horizontalSignFlag = 1;
            verticalSignFlag = -1;
            break;
        }
        case VULMarginTypeBottomLeft: {
            horizontalSignFlag = -1;
            verticalSignFlag = 1;
            break;
        }
        case VULMarginTypeBottomRight: {
            horizontalSignFlag = 1;
            verticalSignFlag = 1;
            break;
        }
    }

    UIEdgeInsets edgeInsets = UIEdgeInsetsMake(vertivalDelta * verticalSignFlag, horizontalDelta * horizontalSignFlag, -vertivalDelta * verticalSignFlag, -horizontalDelta * horizontalSignFlag);
    if (edgeInsetsType == VULEdgeInsetsTypeTitle) {
        self.titleEdgeInsets = edgeInsets;
    } else {
        self.imageEdgeInsets = edgeInsets;
    }
}

- (void)setButtonImageTitleStyle:(ButtonImageTitleStyle)style padding:(CGFloat)padding {
    if (self.imageView.image != nil && self.titleLabel.text != nil) {
        //先还原
        self.titleEdgeInsets = UIEdgeInsetsZero;
        self.imageEdgeInsets = UIEdgeInsetsZero;

        CGRect imageRect = self.imageView.frame;
        CGRect titleRect = self.titleLabel.frame;

        CGFloat totalHeight = imageRect.size.height + padding + titleRect.size.height;
        CGFloat selfHeight = self.frame.size.height;
        CGFloat selfWidth = self.frame.size.width;

        switch (style) {
            case ButtonImageTitleStyleLeft:
                if (padding != 0) {
                    self.titleEdgeInsets = UIEdgeInsetsMake(0,
                                                            padding / 2,
                                                            0,
                                                            -padding / 2);

                    self.imageEdgeInsets = UIEdgeInsetsMake(0,
                                                            -padding / 2,
                                                            0,
                                                            padding / 2);
                }
                break;
            case ButtonImageTitleStyleRight: {
                //图片在右，文字在左
                self.titleEdgeInsets = UIEdgeInsetsMake(0,
                                                        -(imageRect.size.width + padding / 2),
                                                        0,
                                                        (imageRect.size.width + padding / 2));

                self.imageEdgeInsets = UIEdgeInsetsMake(0,
                                                        (titleRect.size.width + padding / 2),
                                                        0,
                                                        -(titleRect.size.width + padding / 2));
            }
            break;
            case ButtonImageTitleStyleTop: {
                //图片在上，文字在下
                self.titleEdgeInsets = UIEdgeInsetsMake(((selfHeight - totalHeight) / 2 + imageRect.size.height + padding - titleRect.origin.y),
                                                        (selfWidth / 2 - titleRect.origin.x - titleRect.size.width / 2) - (selfWidth - titleRect.size.width) / 2,
                                                        -((selfHeight - totalHeight) / 2 + imageRect.size.height + padding - titleRect.origin.y),
                                                        -(selfWidth / 2 - titleRect.origin.x - titleRect.size.width / 2) - (selfWidth - titleRect.size.width) / 2);

                self.imageEdgeInsets = UIEdgeInsetsMake(((selfHeight - totalHeight) / 2 - imageRect.origin.y),
                                                        (selfWidth / 2 - imageRect.origin.x - imageRect.size.width / 2),
                                                        -((selfHeight - totalHeight) / 2 - imageRect.origin.y),
                                                        -(selfWidth / 2 - imageRect.origin.x - imageRect.size.width / 2));
            }
            break;
            case ButtonImageTitleStyleBottom: {
                //图片在下，文字在上。
                self.titleEdgeInsets = UIEdgeInsetsMake(((selfHeight - totalHeight) / 2 - titleRect.origin.y),
                                                        (selfWidth / 2 - titleRect.origin.x - titleRect.size.width / 2) - (selfWidth - titleRect.size.width) / 2,
                                                        -((selfHeight - totalHeight) / 2 - titleRect.origin.y),
                                                        -(selfWidth / 2 - titleRect.origin.x - titleRect.size.width / 2) - (selfWidth - titleRect.size.width) / 2);

                self.imageEdgeInsets = UIEdgeInsetsMake(((selfHeight - totalHeight) / 2 + titleRect.size.height + padding - imageRect.origin.y),
                                                        (selfWidth / 2 - imageRect.origin.x - imageRect.size.width / 2),
                                                        -((selfHeight - totalHeight) / 2 + titleRect.size.height + padding - imageRect.origin.y),
                                                        -(selfWidth / 2 - imageRect.origin.x - imageRect.size.width / 2));
            }
            break;
            case ButtonImageTitleStyleCenterTop: {
                self.titleEdgeInsets = UIEdgeInsetsMake(-(titleRect.origin.y - padding),
                                                        (selfWidth / 2 -  titleRect.origin.x - titleRect.size.width / 2) - (selfWidth - titleRect.size.width) / 2,
                                                        (titleRect.origin.y - padding),
                                                        -(selfWidth / 2 -  titleRect.origin.x - titleRect.size.width / 2) - (selfWidth - titleRect.size.width) / 2);

                self.imageEdgeInsets = UIEdgeInsetsMake(0,
                                                        (selfWidth / 2 - imageRect.origin.x - imageRect.size.width / 2),
                                                        0,
                                                        -(selfWidth / 2 - imageRect.origin.x - imageRect.size.width / 2));
            }
            break;
            case ButtonImageTitleStyleCenterBottom: {
                self.titleEdgeInsets = UIEdgeInsetsMake((selfHeight - padding - titleRect.origin.y - titleRect.size.height),
                                                        (selfWidth / 2 -  titleRect.origin.x - titleRect.size.width / 2) - (selfWidth - titleRect.size.width) / 2,
                                                        -(selfHeight - padding - titleRect.origin.y - titleRect.size.height),
                                                        -(selfWidth / 2 -  titleRect.origin.x - titleRect.size.width / 2) - (selfWidth - titleRect.size.width) / 2);

                self.imageEdgeInsets = UIEdgeInsetsMake(0,
                                                        (selfWidth / 2 - imageRect.origin.x - imageRect.size.width / 2),
                                                        0,
                                                        -(selfWidth / 2 - imageRect.origin.x - imageRect.size.width / 2));
            }
            break;
            case ButtonImageTitleStyleCenterUp: {
                self.titleEdgeInsets = UIEdgeInsetsMake(-(titleRect.origin.y + titleRect.size.height - imageRect.origin.y + padding),
                                                        (selfWidth / 2 -  titleRect.origin.x - titleRect.size.width / 2) - (selfWidth - titleRect.size.width) / 2,
                                                        (titleRect.origin.y + titleRect.size.height - imageRect.origin.y + padding),
                                                        -(selfWidth / 2 -  titleRect.origin.x - titleRect.size.width / 2) - (selfWidth - titleRect.size.width) / 2);

                self.imageEdgeInsets = UIEdgeInsetsMake(0,
                                                        (selfWidth / 2 - imageRect.origin.x - imageRect.size.width / 2),
                                                        0,
                                                        -(selfWidth / 2 - imageRect.origin.x - imageRect.size.width / 2));
            }
            break;

            case ButtonImageTitleStyleCenterDown: {
                self.titleEdgeInsets = UIEdgeInsetsMake((imageRect.origin.y + imageRect.size.height - titleRect.origin.y + padding),
                                                        (selfWidth / 2 -  titleRect.origin.x - titleRect.size.width / 2) - (selfWidth - titleRect.size.width) / 2,
                                                        -(imageRect.origin.y + imageRect.size.height - titleRect.origin.y + padding),
                                                        -(selfWidth / 2 -  titleRect.origin.x - titleRect.size.width / 2) - (selfWidth - titleRect.size.width) / 2);

                self.imageEdgeInsets = UIEdgeInsetsMake(0,
                                                        (selfWidth / 2 - imageRect.origin.x - imageRect.size.width / 2),
                                                        0,
                                                        -(selfWidth / 2 - imageRect.origin.x - imageRect.size.width / 2));
            }
            break;
            case ButtonImageTitleStyleRightLeft: {
                //图片在右，文字在左，距离按钮两边边距

                self.titleEdgeInsets = UIEdgeInsetsMake(0,
                                                        -(titleRect.origin.x - padding),
                                                        0,
                                                        (titleRect.origin.x - padding));

                self.imageEdgeInsets = UIEdgeInsetsMake(0,
                                                        (selfWidth - padding - imageRect.origin.x - imageRect.size.width),
                                                        0,
                                                        -(selfWidth - padding - imageRect.origin.x - imageRect.size.width));
            }

            break;
            case ButtonImageTitleStyleLeftRight: {
                //图片在左，文字在右，距离按钮两边边距

                self.titleEdgeInsets = UIEdgeInsetsMake(0,
                                                        (selfWidth - padding - titleRect.origin.x - titleRect.size.width),
                                                        0,
                                                        -(selfWidth - padding - titleRect.origin.x - titleRect.size.width));

                self.imageEdgeInsets = UIEdgeInsetsMake(0,
                                                        -(imageRect.origin.x - padding),
                                                        0,
                                                        (imageRect.origin.x - padding));
            }
            break;
            default:
                break;
        }
    } else {
        self.titleEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 0);
        self.imageEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 0);
    }
}

@end
