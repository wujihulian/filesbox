//
//  FBShareLineCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/29.
//

#import "FBShareLineCell.h"
@interface FBShareLineCell ()

@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *linkLabel;
@property (nonatomic, strong) VULButton *shareBtn;
@property (nonatomic, strong) VULLabel *passLabel;
@property (nonatomic, strong) VULButton *passBtn;
@property (nonatomic, strong) VULButton *linkBtn;

@end
@implementation FBShareLineCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.contentView.backgroundColor = [UIColor whiteColor];
        [self.contentView addSubview:self.titleLabel];
        [self.contentView addSubview:self.linkLabel];
        [self.contentView addSubview:self.shareBtn];
        [self.contentView addSubview:self.passLabel];
        [self.contentView addSubview:self.codeTextField];
        [self.contentView addSubview:self.passBtn];
        [self.contentView addSubview:self.linkBtn];

      
        [self.titleLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));
            make.width.mas_equalTo(fontAuto(70));
            make.top.mas_equalTo(fontAuto(5));
        }];
        [self.linkLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.titleLabel.mas_right).offset(fontAuto(5));
            make.height.mas_equalTo(fontAuto(30));
            make.top.mas_equalTo(fontAuto(5));
            make.right.mas_equalTo(self.shareBtn.mas_right).offset(-fontAuto(5));
        }];
        [self.shareBtn  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(self.passBtn.mas_left).offset(-fontAuto(5));
            make.width.height.mas_equalTo(fontAuto(22));
            make.top.mas_equalTo(fontAuto(5));
        }];
        [self.passBtn  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(12));
            make.width.height.mas_equalTo(fontAuto(22));
            make.top.mas_equalTo(fontAuto(5));
        }];
        [self.passLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));
            make.width.mas_equalTo(fontAuto(70));
            make.centerY.mas_equalTo(self.linkBtn.mas_centerY);
        }];
        [self.codeTextField  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.passLabel.mas_right).offset(fontAuto(5));
            make.centerY.mas_equalTo(self.linkBtn.mas_centerY);
            make.height.mas_equalTo(fontAuto(35));
            make.right.mas_equalTo(self.linkBtn.mas_left).offset(-fontAuto(5));
            make.bottom.mas_equalTo(-fontAuto(10));
            make.width.mas_lessThanOrEqualTo(fontAuto(200));

        }];
        [self.linkBtn  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(12));
            make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(fontAuto(5));
            make.height.mas_equalTo(fontAuto(40));
            make.width.mas_equalTo(fontAuto(60));
            make.bottom.mas_equalTo(-fontAuto(10));

        }];
        
    }
    return self;
    
}
-(void)setModel:(VULFileObjectModel *)model{
    _model = model;
    self.linkLabel.text =model.shareUrl;
    self.codeTextField.text = model.passWord;
}
- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(UITextField *)codeTextField{
    if (!_codeTextField) {
        _codeTextField = [UITextField new];
        _codeTextField.textColor = HEXCOLOR(0x999999);
        _codeTextField.font = [UIFont yk_pingFangRegular:fontAuto(14)];
        _codeTextField.layer.borderColor= kLineColor.CGColor;
        _codeTextField.layer.borderWidth=1;
        _codeTextField.layer.masksToBounds = YES;
        _codeTextField.layer.cornerRadius = 5;
        _codeTextField.placeholder = KLanguage(@"为空则不设置密码");
//        左边缩进（kTextIndent为缩进距离）
            UIView *leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, fontAuto(10), 0)];
            leftView.backgroundColor = [UIColor clearColor];
               // 保证点击缩进的view，也可以调出光标
            leftView.userInteractionEnabled = NO;
        _codeTextField.leftView = leftView;
        _codeTextField.leftViewMode = UITextFieldViewModeAlways;
    }
    return _codeTextField;
}


- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"分享链接:") TxtAlignment:0 Font:[UIFont yk_pingFangRegular:fontAuto(14)] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _titleLabel;
}
- (VULLabel *)passLabel {
    if (!_passLabel) {
        _passLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"提取密码:") TxtAlignment:0 Font:[UIFont yk_pingFangRegular:fontAuto(14)] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _passLabel;
}
- (VULLabel *)linkLabel {
    if (!_linkLabel) {
        _linkLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:fontAuto(14)] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _linkLabel;
}
-(VULButton *)shareBtn{
    if (!_shareBtn) {
        _shareBtn = [VULButton new];
        [_shareBtn setImage:VULGetImage(@"icon_operation_share") forState:UIControlStateNormal];
        [_shareBtn addTarget:self action:@selector(clickShareBtn) forControlEvents:UIControlEventTouchUpInside];
    }
    return _shareBtn;
}
-(void)clickShareBtn{
    if (self.clickShareOrCopy) {
        self.clickShareOrCopy(YES);
    }
}
-(VULButton *)passBtn{
    if (!_passBtn) {
        _passBtn = [VULButton new];
        [_passBtn setImage:VULGetImage(@"icon_operation_copy") forState:UIControlStateNormal];
        [_passBtn addTarget:self action:@selector(clickPassBtn) forControlEvents:UIControlEventTouchUpInside];

    }
    return _passBtn;
}
-(void)clickPassBtn{
    if (self.clickShareOrCopy) {
        self.clickShareOrCopy(NO);
    }
  
}
-(VULButton *)linkBtn{
    if (!_linkBtn) {
        _linkBtn = [VULButton new];
        [_linkBtn setTitle:KLanguage(@"随机生成") forState:UIControlStateNormal];
        _linkBtn.titleLabel.font = [UIFont yk_pingFangRegular:fontAuto(14)];
        _linkBtn.contentHorizontalAlignment = UIControlContentHorizontalAlignmentRight;
        [_linkBtn setTitleColor:BtnColor forState:UIControlStateNormal];
        [_linkBtn addTarget:self action:@selector(clickLinkBtn) forControlEvents:UIControlEventTouchUpInside];

    }
    return _linkBtn;
}
-(void)clickLinkBtn{
   NSArray *changeArray = [[NSArray alloc]initWithObjects:@"0",@"1",@"2",@"3",@"4",@"5",@"6",@"7",@"8",@"9",@"a",@"b",@"c",@"d",@"e",@"f",@"g",@"h",@"i",@"j",@"k",@"l",@"m",@"n",@"o",@"p",@"q",@"r",@"s",@"t",@"u",@"v",@"w",@"x",@"y",@"z",@"A",@"B",@"C",@"D",@"E",@"F",@"G",@"H",@"I",@"J",@"K",@"L",@"M",@"N",@"O",@"P",@"Q",@"R",@"S",@"T",@"U",@"V",@"W",@"S",@"Y",@"Z",nil];//存放多个数，以备随机取,也可以加上大写字母和其他符号

    NSMutableString* getStr = [[NSMutableString alloc]initWithCapacity:5];

    NSMutableString *changeString= [[NSMutableString alloc]initWithCapacity:6];//申请内存空间

    for(int i =0; i<6; i++) {

    NSInteger index =arc4random()%([changeArray count]-1);//循环六次，得到一个随机数，作为下标值取数组里面的数放到一个可变字符串里，在存放到自身定义的可变字符串
    getStr =changeArray[index];
    changeString= (NSMutableString*)[changeString stringByAppendingString:getStr];

    }
    self.codeTextField.text =changeString;


}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
