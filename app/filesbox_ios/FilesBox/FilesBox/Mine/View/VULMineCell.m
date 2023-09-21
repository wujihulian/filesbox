//
//  VULMineCell.m
//  VideoULTeacher
//
//  Created by yuekewei on 2020/3/24.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "VULMineCell.h"

@interface VULMineCell ()

@end

@implementation VULMineCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self.contentView addSubview:self.titleLabel];
        [self.contentView addSubview:self.briefLabel];
        [self.contentView addSubview:self.cellImageView];
        [self.contentView addSubview:self.switchh];
        [self.contentView addSubview:self.redView];

//        _line = [[UIView alloc]initWithFrame:CGRectZero];
//        _line.backgroundColor = VULRGBAColor(234, 234, 234, 1);
//        [self addSubview:_line];
//        [_line mas_makeConstraints:^(MASConstraintMaker *make) {
//            make.left.mas_equalTo(self.titleLabel.mas_left);
//            make.right.bottom.mas_equalTo(0);
//            make.height.mas_equalTo(@(0.5));
//        }];
        
        [self.cellImageView mas_makeConstraints:^(MASConstraintMaker *make) {
                   make.left.mas_equalTo(15);
                   make.centerY.mas_equalTo(self.contentView.mas_centerY);
            make.size.mas_equalTo(CGSizeMake(20, 20));
               }];
        [self.redView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.cellImageView.mas_right).offset(-2);
            make.top.mas_equalTo(self.cellImageView.mas_top).offset(2);
            make.size.mas_equalTo(CGSizeMake(6, 6));
               }];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.cellImageView.mas_right).offset(15);
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        
        [self.briefLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo( - 10);
            make.left.mas_equalTo(90);
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        [self.switchh mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
            make.right.mas_equalTo(- 15);
        }];
        
        [self.separatorLine mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.titleLabel.mas_left);
            make.right.mas_equalTo(10);
            make.height.mas_equalTo(0.9);
            make.bottom.offset(-1);
        }];
       
    }
    return self;
}

- (UIImageView *)cellImageView {
    if (!_cellImageView) {
        _cellImageView = [UIImageView new];
        _cellImageView.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _cellImageView;
}

- (UILabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [[UILabel alloc] init];
        _titleLabel.font = [UIFont yk_pingFangRegular:FontAuto(16)];
        _titleLabel.textColor = UIColorHex(#333333);
    }
    return _titleLabel;
}

- (UILabel *)briefLabel {
    if (!_briefLabel) {
        _briefLabel = [[UILabel alloc] init];
        _briefLabel.font = [UIFont yk_pingFangRegular:FontAuto(12)];
        _briefLabel.textColor = UIColorHex(#999999);
        _briefLabel.textAlignment = NSTextAlignmentRight;
        _briefLabel.hidden = YES;
    }
    return _briefLabel;
}

- (UISwitch *)switchh {
    if (!_switchh) {
        _switchh = [UISwitch new];
        _switchh.onTintColor = BtnColor;
        _switchh.hidden = YES;
    }
    return _switchh;
}

-(UIView *)redView{
    if (!_redView) {
        _redView = [UIView new];
        _redView.backgroundColor = [UIColor redColor];
        _redView.layer.masksToBounds = YES;
        _redView.layer.cornerRadius = 3;
        _redView.hidden = YES;
    }
    return _redView;
}
- (void)hiddenImage {
    self.cellImageView.hidden = YES;
    [self.titleLabel mas_updateConstraints:^(MASConstraintMaker *make) {
           make.left.mas_equalTo(15);
           make.centerY.mas_equalTo(self.contentView.mas_centerY);
       }];
    [_line mas_updateConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.titleLabel.mas_left);
        make.right.bottom.mas_equalTo(0);
        make.height.mas_equalTo(@(0.5));
    }];
}

@end
