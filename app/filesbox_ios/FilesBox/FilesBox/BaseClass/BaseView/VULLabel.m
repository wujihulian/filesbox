//
//  VULLabel.m
//  VideoULimit
//
//  Created by svnlan on 2018/10/12.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULLabel.h"

@implementation VULLabel

@synthesize verticalAlignment = verticalAlignment_;

- (id)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        self.verticalAlignment = VerticalAlignmentMiddle;
    }
    return self;
}

- (void)setVerticalAlignment:(VerticalAlignment)verticalAlignment {
    verticalAlignment_ = verticalAlignment;
    [self setNeedsDisplay];
}

- (CGRect)textRectForBounds:(CGRect)bounds limitedToNumberOfLines:(NSInteger)numberOfLines {
    CGRect textRect = [super textRectForBounds:bounds limitedToNumberOfLines:numberOfLines];
    switch (self.verticalAlignment) {
        case VerticalAlignmentTop:
            textRect.origin.y = bounds.origin.y;
            break;
        case VerticalAlignmentBottom:
            textRect.origin.y = bounds.origin.y + bounds.size.height - textRect.size.height;
            break;
        case VerticalAlignmentMiddle:
        // Fall through.
        default:
            textRect.origin.y = bounds.origin.y + (bounds.size.height - textRect.size.height) / 2.0;
    }
    return textRect;
}

- (void)drawTextInRect:(CGRect)requestedRect {
    CGRect actualRect = [self textRectForBounds:requestedRect limitedToNumberOfLines:self.numberOfLines];
    [super drawTextInRect:actualRect];
}


+ (instancetype)getLabelWithFrame:(CGRect)frame Text:(NSString *)text TxtAlignment:(NSTextAlignment)txtAlignment Font:(UIFont *)font Color:(UIColor*)textColor BgColor:(UIColor *)color {
    VULLabel *baseLabel = [[VULLabel alloc] initWithFrame:frame];
    baseLabel.font = font;
    baseLabel.textColor = textColor;
    if (baseLabel != nil) {
        baseLabel.numberOfLines = 0 ;
        baseLabel.textAlignment = txtAlignment;
        baseLabel.layer.masksToBounds = YES;
        if (color == nil) {
            baseLabel.backgroundColor = [UIColor clearColor];
        } else {
            baseLabel.backgroundColor = color;
        }
        baseLabel.text = text;
    }
    return baseLabel;
}



- (instancetype)initWithFrame:(CGRect)frame Title:(NSString *)title Text:(NSString *)text  TxtAlignment:(NSTextAlignment)txtAlignment TextFont:(UIFont *)font width:(CGFloat)titleWidth offSet:(CGFloat)offSet {
    if (self = [super init]) {
        self.frame = frame;
        self.titleLabel = [[VULLabel alloc] init];
        self.textLabel = [[VULLabel alloc] init];
        self.titleLabel.text = [NSString stringWithFormat:@"%@",title];
        if (NSStringIsNotEmpty(text)) {
            self.textLabel.text = text;
        }
        self.titleLabel.numberOfLines = 0;
        self.textLabel.numberOfLines = 0;
        self.titleLabel.textColor = HEXCOLOR(0x666666);
        self.textLabel.textColor = HEXCOLOR(0x0398ff);
        self.textLabel.textAlignment = txtAlignment;
        self.titleLabel.textAlignment = NSTextAlignmentLeft;
        self.titleLabel.font = [UIFont systemFontOfSize:18];
        self.textLabel.font = font;

        [self addSubview:self.titleLabel];
        [self addSubview:self.textLabel];
        if (titleWidth == 0) {
            titleWidth = 90;
        }
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.mas_top);
            make.left.equalTo(self.mas_left);
            make.width.equalTo(@(titleWidth));
            make.height.equalTo(self.textLabel);
        }];
        
        [self.textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(self.mas_top);
            make.left.equalTo(self.titleLabel.mas_right).offset(offSet);
            make.right.equalTo(self.mas_right);
            make.bottom.equalTo(self.mas_bottom);
        }];
    }
    return self;
}


@end
