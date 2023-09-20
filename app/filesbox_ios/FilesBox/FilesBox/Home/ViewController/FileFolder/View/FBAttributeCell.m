//
//  FBAttributeCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/8.
//

#import "FBAttributeCell.h"
@interface FBAttributeCell()


@end
@implementation FBAttributeCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self.contentView addSubview:self.titleLabel];
        [self.contentView addSubview:self.detialLabel];
        [self.contentView addSubview:self.userImageV];
        [self.contentView addSubview:self.textField];
        [self.contentView addSubview:self.editBtn];

        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(10));
            make.height.mas_equalTo(fontAuto(50));
            make.width.mas_equalTo(fontAuto(120));
            make.top.mas_equalTo(0);
            make.bottom.mas_equalTo(-0);
        }];
        [self.userImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.titleLabel.mas_right).offset(fontAuto(10));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
            make.height.mas_equalTo(fontAuto(20));
            make.width.mas_equalTo(fontAuto(20));
        }];
        [self.detialLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.userImageV.mas_right).offset(fontAuto(5));
            make.height.mas_equalTo(fontAuto(50));
            make.right.mas_equalTo(-fontAuto(10));
            make.top.mas_equalTo(0);
            make.bottom.mas_equalTo(-0);
        }];
        [self.textField mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.userImageV.mas_right).offset(fontAuto(5));
            make.height.mas_equalTo(fontAuto(50));
            make.right.mas_equalTo(-fontAuto(30));
            make.top.mas_equalTo(0);
            make.bottom.mas_equalTo(-0);
        }];
        [self.editBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(fontAuto(20));
            make.right.mas_equalTo(-fontAuto(10));
            make.centerY.mas_equalTo(self.textField.mas_centerY);

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
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#999999) BgColor:nil];
        _titleLabel.numberOfLines = 1;
    }
    return _titleLabel;
}
- (VULLabel *)detialLabel {
    if (!_detialLabel) {
        _detialLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _detialLabel;
}
-(UIImageView *)userImageV{
    if (!_userImageV) {
        _userImageV = [UIImageView new];
    }
    return _userImageV;
}
-(UITextField *)textField{
    if (!_textField) {
        _textField = [UITextField new];
        _textField.font = [UIFont yk_pingFangRegular:15];
        _textField.textColor = UIColorHex(#999999);
        _textField.placeholder =  KLanguage(@"添加文档描述...");
    }
    return _textField;

}
-(VULButton *)editBtn{
    if (!_editBtn) {
        _editBtn = [VULButton new];
        [_editBtn setImage:VULGetImage(@"icon_edit") forState:UIControlStateNormal];
        [_editBtn addTarget:self action:@selector(clickEditBtn) forControlEvents:UIControlEventTouchUpInside];

    }
    return _editBtn;
}
-(void)clickEditBtn{
    [_textField becomeFirstResponder];
}
@end
