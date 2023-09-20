//
//  VULDropDownCell.m
//  VideoULimit
//
//  Created by svnlan on 2019/3/15.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import "VULDropDownCell.h"

@interface VULDropDownCell ()
@property (nonatomic, strong) UIView *selectedBgView;
@property (nonatomic, strong) CAShapeLayer *lineLayer;

@end

@implementation VULDropDownCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.selectedBgView = [[UIView alloc] initWithFrame:self.bounds];
        self.selectedBackgroundView = self.selectedBgView;

        self.selectedBgView.backgroundColor = [UIColor groupTableViewBackgroundColor];
        self.menuTitleLab.textColor = [UIColor blackColor];
        self.lineColor = [UIColor lightGrayColor];

        [self addSubview:self.menuImageView];
        [self addSubview:self.menuTitleLab];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
}

- (void)setMenuModel:(VULDropDownModel *)menuModel {
    _menuModel = menuModel;
    if (!menuModel.imageName.length) {
        self.menuImageView.hidden = YES;
        self.menuTitleLab.frame = CGRectMake(MenuContentMargin, 0, CGRectGetWidth(self.bounds) - MenuContentMargin * 2, CGRectGetHeight(self.bounds));
    } else {
        self.menuImageView.hidden = NO;
        UIImage *iconImage = [UIImage imageNamed:menuModel.imageName];
        if (iconImage == nil) {
            iconImage = [UIImage imageForIconBundle:menuModel.imageName];
        }
        self.menuImageView.image = iconImage;
        self.menuImageView.frame = CGRectMake(MenuContentMargin, (CGRectGetHeight(self.bounds) - MenuImageWidth) * 0.5, MenuImageWidth, MenuImageWidth);
        self.menuTitleLab.frame = CGRectMake(MenuContentMargin * 2 + MenuImageWidth, 0, CGRectGetWidth(self.bounds) - (MenuContentMargin * 3 + MenuImageWidth), CGRectGetHeight(self.bounds));
    }
    self.menuTitleLab.text = menuModel.title;
    self.menuTitleLab.font = [UIFont systemFontOfSize:MenuTitleFontSize];
}

/*
-(void)setZwPullMenuStyle:(ZWPullMenuStyle)zwPullMenuStyle{
    _zwPullMenuStyle = zwPullMenuStyle;
    switch (zwPullMenuStyle) {
        case PullMenuDarkStyle:
        {
            self.selectedBgView.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.2];
            self.menuTitleLab.textColor = [UIColor whiteColor];
            self.lineColor = [UIColor whiteColor];
        }
            break;
        case PullMenuLightStyle:
        {
            self.selectedBgView.backgroundColor = [UIColor groupTableViewBackgroundColor];
            self.menuTitleLab.textColor = [UIColor blackColor];
            self.lineColor = [UIColor lightGrayColor];
        }
            break;
        default:
            break;
    }
}
 */

- (void)setIsFinalCell:(BOOL)isFinalCell {
    _isFinalCell = isFinalCell;
    if (!isFinalCell) {
        [self drawLineSep];
    } else {
        [self.lineLayer removeFromSuperlayer];
    }
}

- (void)drawLineSep {
    CAShapeLayer *lineLayer = [CAShapeLayer new];
    lineLayer.strokeColor = self.lineColor.CGColor;
    lineLayer.frame = self.bounds;
    lineLayer.lineWidth = 0.5;
    UIBezierPath *sepline = [UIBezierPath bezierPath];
    [sepline moveToPoint:CGPointMake(0, self.bounds.size.height - lineLayer.lineWidth)];
    [sepline addLineToPoint:CGPointMake(self.bounds.size.width - 0, self.bounds.size.height - lineLayer.lineWidth)];
    lineLayer.path = sepline.CGPath;
    [self.layer addSublayer:lineLayer];
    self.lineLayer = lineLayer;
}

- (void)setLineColor:(UIColor *)lineColor {
    if (lineColor) {
        _lineColor = lineColor;
    }
}

- (UIImageView *)menuImageView {
    if (!_menuImageView) {
        _menuImageView = [[UIImageView alloc] init];
    }
    return _menuImageView;
}

- (UILabel *)menuTitleLab {
    if (!_menuTitleLab) {
        _menuTitleLab = [[UILabel alloc] init];
    }
    return _menuTitleLab;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
