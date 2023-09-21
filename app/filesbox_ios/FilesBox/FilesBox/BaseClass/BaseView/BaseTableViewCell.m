//
//  BaseTableViewCell.m
//  VideoULTeacher
//
//  Created by yuekewei on 2020/3/21.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "BaseTableViewCell.h"

@implementation BaseTableViewCell

+ (instancetype)dequeueReusableCellWithTableView:(UITableView *)tableView reuseIdentifier:(NSString *)reuseIdentifier {
    return [self dequeueReusableCellWithTableView:tableView reuseIdentifier:reuseIdentifier cellStyle:UITableViewCellStyleDefault];
}

+ (instancetype)dequeueReusableCellWithTableView:(UITableView *)tableView reuseIdentifier:(NSString *)reuseIdentifier cellStyle:(UITableViewCellStyle)cellStyle {
    BaseTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:reuseIdentifier ?: NSStringFromClass(self)];
    if (!cell) {
        cell = [[self alloc] initWithStyle:cellStyle reuseIdentifier:reuseIdentifier ?: NSStringFromClass(self)];
    }
    return cell;
}


- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.contentView.backgroundColor = [UIColor clearColor];
        self.selectionStyle = UITableViewCellSelectionStyleNone;
        self.clipsToBounds = YES;
        
        [self.contentView addSubview:self.separatorLine];
        [self.contentView addSubview:self.arrowView];
        
        [self.separatorLine mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.bottom.mas_equalTo(0);
            make.height.mas_equalTo(1 / [UIScreen mainScreen].scale);
        }];
        
        [self.arrowView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(- 15);
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
    }
    return self;
}

- (void)setSeparatorLineLeftInset:(CGFloat)separatorLineLeftInset {
    _separatorLineLeftInset = separatorLineLeftInset;
    [self.separatorLine mas_updateConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(separatorLineLeftInset);
    }];
}

- (void)setSeparatorLineRightInset:(CGFloat)separatorLineRightInset {
    _separatorLineRightInset = separatorLineRightInset;
    [self.separatorLine mas_updateConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(- separatorLineRightInset);
    }];
}


#pragma mark - Lazy
- (UIView *)separatorLine {
    if (!_separatorLine) {
        _separatorLine = ({
            UIView *view = [UIView new];
            view.backgroundColor = [UIColorHex(#000000) colorWithAlphaComponent:0.05];
            view;
        });
    }
    return _separatorLine;
}

- (UIImageView *)arrowView {
    if (!_arrowView) {
        _arrowView = [[UIImageView alloc] init];
        _arrowView.size = CGSizeMake(6, 12);
        [_arrowView setImage:({
            CGSize imgSize = CGSizeMake(6, 12);
            UIImage *image = [UIImage imageWithSize:imgSize
                                          drawBlock:^(CGContextRef  _Nonnull context) {
                CGContextSetStrokeColorWithColor(context, UIColorHex(#666666).CGColor);
                CGContextSetLineCap(context, kCGLineCapRound);
                CGContextSetLineJoin(context, kCGLineJoinMiter);
                CGContextSetLineWidth(context, 1.0);
                CGContextMoveToPoint(context, 0, 0);
                CGContextAddLineToPoint(context, imgSize.width - 0.5, imgSize.height / 2.0);
                CGContextAddLineToPoint(context, 0, imgSize.height);
                CGContextStrokePath(context);
            }];
            image;
        })];
        _arrowView.hidden = YES;
    }
    return _arrowView;
}


@end
