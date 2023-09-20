//
//  FBLogCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/7/19.
//

#import "FBLogCell.h"

@implementation FBLogCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self.contentView addSubview:self.bgView];
        [self.contentView addSubview:self.lineV];

        [self.contentView addSubview:self.titleLabel];
        [self.contentView addSubview:self.titleLabel1];
        [self.contentView addSubview:self.titleLabel2];
        [self.contentView addSubview:self.titleLabel3];
        
        CGFloat width = (VULSCREEN_WIDTH-fontAuto(35))/4;
        [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(10));
            make.top.bottom.mas_equalTo(0);
        }];
        [self.lineV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(10));
            make.bottom.mas_equalTo(0);
            make.height.mas_equalTo(1);

        }];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(10));
        
            make.bottom.mas_equalTo(-fontAuto(5));
            make.top.mas_equalTo(fontAuto(5));
            make.width.mas_equalTo(width);
        }];
        [self.titleLabel1 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.titleLabel.mas_right).offset(fontAuto(5));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
            make.width.mas_equalTo(width);
            make.bottom.mas_equalTo(-fontAuto(5));
            make.top.mas_equalTo(fontAuto(5));
        }];
        [self.titleLabel2 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.titleLabel1.mas_right).offset(fontAuto(5));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
            make.width.mas_equalTo(width);
            make.bottom.mas_equalTo(-fontAuto(5));
            make.top.mas_equalTo(fontAuto(5));
        }];
        [self.titleLabel3 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.titleLabel2.mas_right).offset(fontAuto(5));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
            make.width.mas_equalTo(width);
            make.bottom.mas_equalTo(-fontAuto(5));
            make.top.mas_equalTo(fontAuto(5));
        }];
  
    }
    return self;
    
}
-(UIView *)bgView{
    if(!_bgView){
        _bgView = [UIView new];
        _bgView.backgroundColor = HEXCOLOR(0xF6F6F6);
//#F6F6F6
    }
    return _bgView;

}
-(UIView *)lineV{
    if(!_lineV){
        _lineV = [UIView new];
        _lineV.backgroundColor = HEXCOLOR(0xF6F6F6);
//#F6F6F6
    }
    return _lineV;

}

- (UILabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [[UILabel alloc] init];
        _titleLabel.font = [UIFont yk_pingFangRegular:FontAuto(14)];
        _titleLabel.textColor = UIColorHex(#777777);
        _titleLabel.numberOfLines = 0;

    }
    return _titleLabel;
}
- (UILabel *)titleLabel1 {
    if (!_titleLabel1) {
        _titleLabel1 = [[UILabel alloc] init];
        _titleLabel1.font = [UIFont yk_pingFangRegular:FontAuto(14)];
        _titleLabel1.textColor = UIColorHex(#777777);
        _titleLabel1.numberOfLines = 0;

    }
    return _titleLabel1;
}

- (UILabel *)titleLabel2 {
    if (!_titleLabel2) {
        _titleLabel2 = [[UILabel alloc] init];
        _titleLabel2.font = [UIFont yk_pingFangRegular:FontAuto(14)];
        _titleLabel2.textColor = UIColorHex(#777777);
        _titleLabel2.numberOfLines = 0;
    }
    return _titleLabel2;
}
- (UILabel *)titleLabel3 {
    if (!_titleLabel3) {
        _titleLabel3 = [[UILabel alloc] init];
        _titleLabel3.font = [UIFont yk_pingFangRegular:FontAuto(14)];
        _titleLabel3.textColor = UIColorHex(#777777);
        _titleLabel3.numberOfLines = 0;

    }
    return _titleLabel3;
}
- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
