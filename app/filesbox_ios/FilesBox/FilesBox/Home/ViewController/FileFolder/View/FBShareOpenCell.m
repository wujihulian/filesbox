//
//  FBShareOpenCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/29.
//

#import "FBShareOpenCell.h"
@interface FBShareOpenCell ()

@property (nonatomic, strong) UISwitch *openSwitch;
@property (nonatomic, strong) VULLabel *titleLabel;

@end
@implementation FBShareOpenCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.contentView.backgroundColor = [UIColor whiteColor];
        [self.contentView addSubview:self.openSwitch];
        [self.contentView addSubview:self.titleLabel];

        [self.openSwitch  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(12));
            make.top.mas_equalTo(fontAuto(10));
            make.bottom.mas_equalTo(-fontAuto(10));

        }];
    
        [self.titleLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.openSwitch.mas_right).offset(fontAuto(10));
            make.height.mas_equalTo(fontAuto(30));
            make.width.mas_greaterThanOrEqualTo(fontAuto(30));
            make.centerY.mas_equalTo(self.openSwitch.mas_centerY);
        }];
    }
    return self;
    
}

-(UISwitch *)openSwitch{
    if (!_openSwitch) {
        _openSwitch = [UISwitch new];
        _openSwitch.tintColor =BtnColor;
        _openSwitch.onTintColor =BtnColor;

        
    }
    return _openSwitch;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"创建链接之后，可以通过微信分享给其他人") TxtAlignment:0 Font:[UIFont yk_pingFangRegular:fontAuto(14)] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _titleLabel;
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
