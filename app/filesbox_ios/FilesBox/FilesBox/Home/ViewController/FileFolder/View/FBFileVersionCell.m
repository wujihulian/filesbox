//
//  FBFileVersionCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/4/12.
//

#import "FBFileVersionCell.h"
@interface FBFileVersionCell ()

@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *timeLabel;
@property (nonatomic, strong) VULLabel *dotLabel;
@property (nonatomic, strong) VULLabel *sizeLabel;
@property (nonatomic, strong) VULLabel *currentLabel;
@property (nonatomic, strong) VULButton *moreBtn;
@property (nonatomic, strong) UIImageView *imageV;
@property (nonatomic, strong) UIImageView *leftImage;
@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, strong) UIView *bgTopView;



@end
@implementation FBFileVersionCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.contentView.backgroundColor = [UIColor whiteColor];
        [self.contentView  addSubview:self.bgView];
        [self.bgView  addSubview:self.bgTopView];
        [self.bgTopView  addSubview:self.leftImage];
        [self.bgTopView  addSubview:self.titleLabel];
        [self.bgTopView  addSubview:self.timeLabel];
        [self.bgTopView  addSubview:self.dotLabel];
        [self.bgTopView  addSubview:self.sizeLabel];
        [self.bgTopView  addSubview:self.moreBtn];
        [self.bgTopView  addSubview:self.imageV];
        [self.bgView  addSubview:self.currentLabel];
        [self.bgView  addSubview:self.textField];
        [self.bgView  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(12));
            make.right.mas_equalTo(-fontAuto(12));
            make.top.mas_equalTo(fontAuto(10));
            make.bottom.mas_equalTo(-fontAuto(0));
        }];
        [self.bgTopView  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(0));
            make.right.mas_equalTo(-fontAuto(0));
            make.top.mas_equalTo(fontAuto(0));
            make.height.mas_equalTo(fontAuto(30));
        }];
        [self.leftImage  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(0));
            make.height.width.mas_equalTo(fontAuto(0));
        }];
        [self.titleLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.leftImage.mas_right).offset(10);
            make.width.mas_equalTo(fontAuto(60));
            make.height.mas_equalTo(fontAuto(20));
            make.top.mas_equalTo(fontAuto(5));
        }];
        [self.timeLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.titleLabel.mas_right).offset(10);
            make.width.mas_greaterThanOrEqualTo(fontAuto(30));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
        }];
        [self.dotLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.timeLabel.mas_right).offset(0);
            make.width.mas_equalTo(fontAuto(20));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);

        }];
        [self.sizeLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.dotLabel.mas_right).offset(0);
            make.width.mas_greaterThanOrEqualTo(fontAuto(30));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
        }];
        [self.moreBtn  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(12));
            make.width.height.mas_equalTo(fontAuto(30));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
        }];
        [self.imageV  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(self.moreBtn.mas_left).offset(-fontAuto(10));
            make.width.height.mas_equalTo(fontAuto(20));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
        }];
        [self.currentLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(10));
            make.width.mas_equalTo(fontAuto(60));
            make.top.mas_equalTo(self.bgTopView.mas_bottom).offset(fontAuto(5));
            make.bottom.mas_offset(-fontAuto(10));
        }];
        [self.textField  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(12));
            make.left.mas_equalTo(self.currentLabel.mas_right).offset(fontAuto(10));
            make.centerY.mas_equalTo(self.currentLabel.mas_centerY);
        }];
    }
    
    return self;
    
}
-(void)setModel:(VULFileObjectModel *)model{
    _model = model;
    self.timeLabel.text = [NSString stringWithFormat:@"%@",[NSDate timeWithTimeIntervalString:getTimeWithTime(model.createTime) Format:@"yyyy-MM-dd HH:mm"]];
    NSString *fileSize = model.size ? [self stringWithbytes:model.size.intValue] : @"";
    self.sizeLabel.text = fileSize;
    [self.imageV sd_setImageWithURL:[NSURL URLWithString:resultsUrl(model.avatar) ] placeholderImage:VULGetImage(@"placeholder_face")];
    self.textField.text = model.detail;
    [self.currentLabel  mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(fontAuto(10));
        make.width.mas_equalTo(fontAuto(60));
        make.centerY.mas_equalTo(self.textField.mas_centerY);

    }];
    [self.textField  mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(-fontAuto(12));
        make.left.mas_equalTo(self.currentLabel.mas_right).offset(fontAuto(10));
        make.top.mas_equalTo(self.bgTopView.mas_bottom).offset(fontAuto(5));
        make.bottom.mas_offset(-fontAuto(10));
    }];
    self.textField.hidden =self.row==0;
    self.textField.userInteractionEnabled = model.isDetail;
//    65BA82
    [self.titleLabel  mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.leftImage.mas_right).offset(10);
        make.width.mas_equalTo(fontAuto(60));
        make.height.mas_equalTo(fontAuto(20));
        make.top.mas_equalTo(fontAuto(5));
    }];
    if (self.row>0) {
        [self.currentLabel  mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(0));
            make.width.mas_equalTo(fontAuto(0));
            make.top.mas_equalTo(self.bgTopView.mas_bottom).offset(fontAuto(0));
            make.bottom.mas_offset(-fontAuto(0));
        }];
        if ((model.detail.length == 0 || !model.detail)&&!model.isDetail) {
            [self.textField  mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.right.mas_equalTo(-fontAuto(12));
                make.left.mas_equalTo(self.currentLabel.mas_right).offset(fontAuto(0));
                make.height.mas_equalTo(fontAuto(0));
                make.centerY.mas_equalTo(self.currentLabel.mas_centerY);
                make.bottom.mas_offset(-fontAuto(0));
            }];
        }
        [self.titleLabel  mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.leftImage.mas_right).offset(10);
            make.width.mas_equalTo(fontAuto(30));
            make.height.mas_equalTo(fontAuto(20));
            make.top.mas_equalTo(fontAuto(5));
        }];
        _titleLabel.backgroundColor =HEXCOLOR(0x65BA82);
        _titleLabel.text = [NSString stringWithFormat:@"V%ld",self.version] ;
    }else{
        VULSaveUserInformation *userInfo = [VULRealmDBManager getLocalUserInformational];


        [self.imageV sd_setImageWithURL:[NSURL URLWithString:resultsUrl(userInfo.avatar)] placeholderImage:VULGetImage(@"placeholder_face")];
        _titleLabel.backgroundColor =BtnColor;
        _titleLabel.text = KLanguage(@"当前版本") ;

    }
//    NSArray *picArray = @[@"bmp",@"jpg",@"jpeg",@"png",@"gif"];
//    if ([picArray containsObject:model.fileType]) {
//        [self.leftImage  mas_remakeConstraints:^(MASConstraintMaker *make) {
//            make.left.mas_equalTo(fontAuto(fontAuto(5)));
//            make.width.mas_equalTo(fontAuto(40));
//            make.height.mas_equalTo(fontAuto(60));
//            make.centerY.mas_equalTo(self.bgView.mas_centerY);
//        }];
//    }else{
//        [self.leftImage  mas_remakeConstraints:^(MASConstraintMaker *make) {
//            make.left.mas_equalTo(fontAuto(0));
//            make.height.width.mas_equalTo(fontAuto(0));
//        }];
//    }

}
- (NSString *)stringWithbytes:(int)bytes {
    if (bytes < 1024) { // B
        return [NSString stringWithFormat:@"%dB", bytes];
    } else if (bytes >= 1024 && bytes < 1024 * 1024) { // KB
        return [NSString stringWithFormat:@"%.2fKB", (double)bytes / 1024];
    } else if (bytes >= 1024 * 1024 && bytes < 1024 * 1024 * 1024) { // MB
        return [NSString stringWithFormat:@"%.2fMB", (double)bytes / (1024 * 1024)];
    } else { // GB
        return [NSString stringWithFormat:@"%.2fGB", (double)bytes / (1024 * 1024 * 1024)];
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
-(UIView *)bgView{
    if (!_bgView) {
        _bgView = [UIView new];
        _bgView.backgroundColor = HEXCOLOR(0xffffff);
        _bgView.layer.masksToBounds = YES;
        _bgView.layer.cornerRadius = 5;
        _bgView.layer.borderWidth = 1;
        _bgView.layer.borderColor = HEXCOLOR(0xececec).CGColor;
    }
    return _bgView;
}
-(UIView *)bgTopView{
    if (!_bgTopView) {
        _bgTopView = [UIView new];
        _bgTopView.backgroundColor = [BtnColor colorWithAlphaComponent:0.1];
    }
    return _bgTopView;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"当前版本") TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#ffffff) BgColor:BtnColor];
        _titleLabel.numberOfLines = 1;
        _titleLabel.layer.masksToBounds = YES;
        _titleLabel.layer.cornerRadius = 2;
    }
    return _titleLabel;
}
- (VULLabel *)currentLabel {
    if (!_currentLabel) {
        _currentLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"当前版本") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:BtnColor BgColor:[BtnColor colorWithAlphaComponent:0.1]];
        _currentLabel.numberOfLines = 1;
        _currentLabel.layer.masksToBounds = YES;
        _currentLabel.layer.cornerRadius = 2;
    }
    return _currentLabel;
}

- (VULLabel *)timeLabel {
    if (!_timeLabel) {
        _timeLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#999999) BgColor:nil];
        _timeLabel.numberOfLines = 1;
    }
    return _timeLabel;
}
- (VULLabel *)dotLabel {
    if (!_dotLabel) {
        _dotLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"·" TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#999999) BgColor:nil];
        _dotLabel.numberOfLines = 1;
    }
    return _dotLabel;
}
- (VULLabel *)sizeLabel {
    if (!_sizeLabel) {
        _sizeLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
        _sizeLabel.numberOfLines = 1;
    }
    return _sizeLabel;
}
-(UITextField *)textField{
    if (!_textField) {
        _textField = [UITextField new];
        _textField.placeholder = KLanguage(@"添加版本说明");
        _textField.font = [UIFont yk_pingFangRegular:15];
        _textField.textColor = UIColorHex(#333333);
    }
    return _textField;
}
-(UIImageView *)imageV{
    if (!_imageV) {
        _imageV = [UIImageView new];
        _imageV.layer.masksToBounds = YES;
        _imageV.layer.cornerRadius = fontAuto(10);
    }
    return _imageV;
}
-(UIImageView *)leftImage{
    if (!_leftImage) {
        _leftImage = [UIImageView new];
    }
    return _leftImage;
}
-(VULButton *)moreBtn{
    if (!_moreBtn) {
        _moreBtn = [VULButton new];
        [_moreBtn setImage:VULGetImage(@"icon_more_version") forState:UIControlStateNormal];
        [_moreBtn addTarget:self action:@selector(clickMoreSearchBtn) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return _moreBtn;
}
-(void)clickMoreSearchBtn{
    if (self.clickMore) {
        self.clickMore(self.row);
    }
}
@end
