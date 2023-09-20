//
//  FBMineInfoCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/7/17.
//

#import "FBMineInfoCell.h"
#import "WXApiManger.h"
@implementation FBMineInfoCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self.contentView addSubview:self.titleLabel];
        [self.contentView addSubview:self.rightLabel];
        [self.contentView addSubview:self.rightImageV];
        [self.contentView addSubview:self.iconRight];
        [self.contentView addSubview:self.bingBtn];
        
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));
            make.bottom.mas_equalTo(-fontAuto(10));
            make.width.mas_equalTo(fontAuto(80));
        }];
        [self.bingBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.titleLabel.mas_right).offset(fontAuto(10));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
            make.height.mas_equalTo(fontAuto(30));
            make.width.mas_equalTo(fontAuto(80));
        }];
        [self.iconRight mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(10));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
            make.width.mas_equalTo(16);
        }];
        [self.rightImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(30));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
            make.height.width.mas_equalTo(fontAuto(30));
        }];
        [self.rightLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(100));
            make.right.mas_equalTo(-fontAuto(30));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
            make.width.mas_greaterThanOrEqualTo(fontAuto(30));
        }];
        
    }
    return self;
    
}





- (UILabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [[UILabel alloc] init];
        _titleLabel.font = [UIFont yk_pingFangRegular:FontAuto(16)];
        _titleLabel.textColor = UIColorHex(#333333);
    }
    return _titleLabel;
}
- (UILabel *)rightLabel {
    if (!_rightLabel) {
        _rightLabel = [[UILabel alloc] init];
        _rightLabel.font = [UIFont yk_pingFangRegular:FontAuto(16)];
        _rightLabel.textColor = UIColorHex(#333333);
        _rightLabel.textAlignment = NSTextAlignmentRight;
    }
    return _rightLabel;
}


- (UIImageView *)rightImageV {
    if (!_rightImageV) {
        _rightImageV = [[UIImageView alloc] init];
        _rightImageV.layer.masksToBounds = YES;
        _rightImageV.layer.cornerRadius = fontAuto(15);
        
    }
    return _rightImageV;
}
- (UIImageView *)iconRight {
    if (!_iconRight) {
        _iconRight = [[UIImageView alloc] init];
        _iconRight.image = VULGetImage(@"icon_right");
        _iconRight.contentMode =UIViewContentModeScaleAspectFit;;

    }
    return _iconRight;
}
-(VULButton *)bingBtn{
    if(!_bingBtn){
        _bingBtn = [VULButton new];
        _bingBtn.titleLabel.textColor  = BtnColor;
        [_bingBtn setTitleColor:BtnColor forState:UIControlStateNormal];
        [_bingBtn setTitle:KLanguage(@"立即绑定") forState:UIControlStateNormal];
        [_bingBtn addTarget:self action:@selector(clickBingBtn:) forControlEvents:UIControlEventTouchUpInside];
        _bingBtn.titleLabel.font  = [UIFont yk_pingFangRegular:FontAuto(16)];
    }
    return _bingBtn;
}
-(void)clickBingBtn:(VULButton *)sender{
    
    if(self.clickBtnTitle){
        self.clickBtnTitle(self.titleLabel.text,sender.titleLabel.text);
    }

        
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
