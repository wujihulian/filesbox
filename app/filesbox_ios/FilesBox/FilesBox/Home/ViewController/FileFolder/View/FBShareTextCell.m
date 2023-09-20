//
//  FBShareTextCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/29.
//

#import "FBShareTextCell.h"
@interface FBShareTextCell ()




@end
@implementation FBShareTextCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.contentView.backgroundColor = [UIColor whiteColor];
        [self.contentView  addSubview:self.titleLabel];
        [self.contentView  addSubview:self.codeTextField];
        [self.contentView  addSubview:self.rightImageV];
        [self.contentView  addSubview:self.openSwitch];

        [self.titleLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));
            make.bottom.mas_equalTo(-fontAuto(10));

        }];
        [self.codeTextField  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(self.rightImageV.mas_left).offset(-fontAuto(5));
            make.height.mas_equalTo(fontAuto(30));
        make.width.mas_equalTo(VULSCREEN_WIDTH-fontAuto(150));
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        [self.rightImageV  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(20));
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        [self.openSwitch  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(12));
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
    }
    
    return self;
    
}
- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:fontAuto(14)] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _titleLabel;
}

-(UITextField *)codeTextField{
    if (!_codeTextField) {
        _codeTextField = [UITextField new];
        _codeTextField.textColor = HEXCOLOR(0x999999);
        _codeTextField.font = [UIFont yk_pingFangRegular:fontAuto(14)];
        _codeTextField.textAlignment =  NSTextAlignmentRight;
    }
    return _codeTextField;
}

-(UIImageView *)rightImageV{
    if (!_rightImageV) {
        _rightImageV = [UIImageView new];
    }
    return _rightImageV;
}
-(UISwitch *)openSwitch{
    if (!_openSwitch) {
        _openSwitch = [UISwitch new];
//        _openSwitch.tintColor =BtnColor;
        _openSwitch.onTintColor =BtnColor;

        
    }
    return _openSwitch;
}
@end
