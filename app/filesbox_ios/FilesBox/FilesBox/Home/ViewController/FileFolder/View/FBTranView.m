//
//  FBTranView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/7/3.
//

#import "FBTranView.h"
#import "FBFromTopView.h"
@interface FBTranView()
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *nameLabel;
@property (nonatomic, strong) VULLabel *nameConentLabel;
@property (nonatomic, strong) VULLabel *fileLabel;
@property (nonatomic, strong) VULLabel *fileConentLabel;
@property (nonatomic, strong) VULLabel *fileTypeLabel;
@property (nonatomic, strong) VULLabel *fileTypeConentLabel;
@property (nonatomic, strong) VULLabel *tranLabel;
@property (nonatomic, strong) VULLabel *tranConentLabel;
@property (nonatomic,strong) VULButton *cancelBtn;
@property (nonatomic,strong) VULButton *sureBtn;
@property (nonatomic,strong) NSArray *typeArray;
@property (nonatomic,assign) NSInteger selectIndex;
@property (nonatomic,strong) NSString *type;

@end
@implementation FBTranView
- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];
        self.layer.masksToBounds = YES;
        self.layer.cornerRadius = 5;

        [self addSubview:self.titleLabel];
        [self addSubview:self.nameLabel];
        [self addSubview:self.nameConentLabel];
        [self addSubview:self.fileLabel];
        [self addSubview:self.fileConentLabel];
        [self addSubview:self.fileTypeLabel];
        [self addSubview:self.fileTypeConentLabel];
        [self addSubview:self.tranLabel];
        [self addSubview:self.tranConentLabel];
        [self addSubview:self.cancelBtn];
        [self addSubview:self.sureBtn];
        
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(12));
            make.right.mas_equalTo(-fontAuto(12));
            make.top.mas_equalTo(fontAuto(0));
            make.height.mas_equalTo(fontAuto(40));
        }];
        [self.nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(12));
            make.width.mas_equalTo(fontAuto(85));
            make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(fontAuto(5));
            make.height.mas_equalTo(fontAuto(30));
        }];
        [self.nameConentLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(self.nameLabel.mas_centerY);
            make.left.mas_equalTo(self.nameLabel.mas_right).offset(fontAuto(0));
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));
        }];
        
        [self.fileLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(12));
            make.width.mas_equalTo(fontAuto(85));
            make.top.mas_equalTo(self.nameConentLabel.mas_bottom).offset(fontAuto(5));
            make.height.mas_equalTo(fontAuto(30));
        }];
        [self.fileConentLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(self.fileLabel.mas_centerY);
            make.left.mas_equalTo(self.fileLabel.mas_right).offset(fontAuto(0));
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));
        }];
        
        [self.fileTypeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(12));
            make.width.mas_equalTo(fontAuto(85));
            make.top.mas_equalTo(self.fileLabel.mas_bottom).offset(fontAuto(10));
            make.height.mas_equalTo(fontAuto(30));
        }];
        [self.fileTypeConentLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(self.fileTypeLabel.mas_centerY);
            make.left.mas_equalTo(self.fileTypeLabel.mas_right).offset(fontAuto(0));
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));
        }];
        
        [self.tranLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(12));
            make.width.mas_equalTo(fontAuto(84));
            make.top.mas_equalTo(self.fileTypeLabel.mas_bottom).offset(fontAuto(10));
            make.height.mas_equalTo(fontAuto(30));
        }];
        [self.tranConentLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(self.tranLabel.mas_centerY);
            make.right.mas_equalTo(-fontAuto(12));
            make.left.mas_equalTo(self.tranLabel.mas_right).offset(fontAuto(0));
            make.height.mas_equalTo(fontAuto(30));
        }];
        
        CGFloat left = (VULSCREEN_WIDTH*0.9-fontAuto(20)-fontAuto(240))/2;
        [self.cancelBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(left);
            make.width.mas_equalTo(fontAuto(120));
            make.height.mas_equalTo(fontAuto(38));
            make.top.mas_equalTo(self.tranConentLabel.mas_bottom).offset(fontAuto(20));

        }];
        [self.sureBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.cancelBtn.mas_right).offset(fontAuto(20));
            make.width.mas_equalTo(fontAuto(120));
            make.height.mas_equalTo(fontAuto(38));
            make.centerY.mas_equalTo(self.cancelBtn.mas_centerY);

        }];
        self.selectIndex = 0;
        self.userInteractionEnabled = YES;
        self.tranConentLabel.userInteractionEnabled = YES;
        UITapGestureRecognizer *sender = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickTran)];
        [self.tranConentLabel addGestureRecognizer:sender];
        
        [self layoutIfNeeded];
 
        self.height = self.sureBtn.bottom +fontAuto(20);
    }
    return  self;
    
}
-(void)clickTran{
    self.tranConentLabel.layer.borderColor =UIColorHex(#722ed1).CGColor;
    self.tranConentLabel.textColor = HEXCOLOR(0x999999);
    NSInteger count = self.typeArray.count;
    if (self.typeArray.count >10) {
        count =10;
    }
    
    FBFromTopView *top = [[FBFromTopView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, count*BtnCell+K_BottomBar_Height)];
    top.index =0;
    top.selectIndex = [NSString stringWithFormat:@"%ld",self.selectIndex] ;
    top.titleArr = self.typeArray;
    zhPopupController *popup2 =[[zhPopupController alloc] initWithView:top size:CGSizeMake(VULSCREEN_WIDTH, top.height)];
    popup2.layoutType = zhPopupLayoutTypeBottom;
    popup2.presentationStyle = zhPopupSlideStyleFromBottom;
    popup2.maskAlpha = 0.35;
    top.clickViewWithRowBlock = ^(NSString * _Nonnull title, NSInteger row) {
        self.selectIndex = row;
        self.tranConentLabel.text = [NSString stringWithFormat:@" %@",title] ;
        self.type = title;
        [popup2 dismiss];
    };
    popup2.didDismissBlock = ^(zhPopupController * _Nonnull popupController) {

        self.tranConentLabel.layer.borderColor =UIColorHex(#d9d9d9d1).CGColor;
        self.tranConentLabel.textColor = HEXCOLOR(0x333333);
    };
    [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
-(void)setModel:(VULFileObjectModel *)model{
    _model = model;
    self.nameConentLabel.text = model.name;
    NSString *fileSize = model.size ? [self stringWithbytes:model.size.intValue] : @"";
    self.fileConentLabel.text = fileSize;
    self.fileTypeConentLabel.text = model.fileType;
    if([model.fileType isEqualToString:@"jpg"] || [model.fileType isEqualToString:@"png"]){
        self.typeArray = @[@"pdf"];
    }else if([model.fileType isEqualToString:@"doc"] || [model.fileType isEqualToString:@"docx"]){
        self.typeArray = @[@"pdf",@"jpg",@"png"];
    }else if([model.fileType isEqualToString:@"ppt"] || [model.fileType isEqualToString:@"pptx"]){
        self.typeArray = @[@"pdf",@"jpg",@"png"];
    }else if([model.fileType isEqualToString:@"pdf"]){
        self.typeArray = @[@"doc",@"docx",@"png",@"jpg"];
    }
    self.tranConentLabel.text = [NSString stringWithFormat:@" %@",self.typeArray[0]] ;
    self.type = self.typeArray[0];;
}
- (NSString *)stringWithbytes:(int)bytes {
    if (bytes < 1024) { // B
        if (bytes== 0) {
            return @"";
        }
        return [NSString stringWithFormat:@"%dB", bytes];
    } else if (bytes >= 1024 && bytes < 1024 * 1024) { // KB
        return [NSString stringWithFormat:@"%.0fKB", (double)bytes / 1024];
    } else if (bytes >= 1024 * 1024 && bytes < 1024 * 1024 * 1024) { // MB
        return [NSString stringWithFormat:@"%.1fMB", (double)bytes / (1024 * 1024)];
    } else { // GB
        return [NSString stringWithFormat:@"%.1fGB", (double)bytes / (1024 * 1024 * 1024)];
    }
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"格式转换") TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangSemibold:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _titleLabel;
}

- (VULLabel *)nameLabel {
    if (!_nameLabel) {
        _nameLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"文件名称") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _nameLabel;
}
- (VULLabel *)nameConentLabel {
    if (!_nameConentLabel) {
        _nameConentLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _nameConentLabel;
}
- (VULLabel *)fileLabel {
    if (!_fileLabel) {
        _fileLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"文件大小") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _fileLabel;
}
- (VULLabel *)fileConentLabel {
    if (!_fileConentLabel) {
        _fileConentLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _fileConentLabel;
}
- (VULLabel *)fileTypeLabel {
    if (!_fileTypeLabel) {
        _fileTypeLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"文件类型") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _fileTypeLabel;
}
- (VULLabel *)fileTypeConentLabel {
    if (!_fileTypeConentLabel) {
        _fileTypeConentLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _fileTypeConentLabel;
}
- (VULLabel *)tranLabel {
    if (!_tranLabel) {
        _tranLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"转换为...") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _tranLabel;
}
- (VULLabel *)tranConentLabel {
    if (!_tranConentLabel) {
        _tranConentLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
        _tranConentLabel.layer.borderColor = HEXCOLOR(0xececec).CGColor;
        _tranConentLabel.layer.borderWidth  =1;
        _tranConentLabel.layer.masksToBounds = YES;
        _tranConentLabel.layer.cornerRadius = 5;
    }
    return _tranConentLabel;
}
- (VULButton *)cancelBtn {
    if (!_cancelBtn) {
        
        _cancelBtn = [VULButton new];
        [_cancelBtn setTitle:KLanguage(@"取消") forState:UIControlStateNormal];
        _cancelBtn.layer.masksToBounds = YES;
        _cancelBtn.layer.cornerRadius = fontAuto(19);
        _cancelBtn.layer.borderColor = HEXCOLOR(0xDCDEE0).CGColor;
        _cancelBtn.layer.borderWidth =1;
        [_cancelBtn setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
        [_cancelBtn addTarget:self action:@selector(hiddenView) forControlEvents:UIControlEventTouchUpInside];
//        _cancelBtn = [VULButton getCustomBtnWithFrame:CGRectMake(fontAuto(19), fontAuto(138), fontAuto(115), fontAuto(38)) title:@"取消" Font:16 Bgcolor:UIColorHex(#ECECEC) Target:self action:@selector(hiddenView)];
//        _cancelBtn.layer.cornerRadius = fontAuto(19);
//        [_cancelBtn setTitleColor:UIColorHex(#999999) forState:UIControlStateNormal];
    }
    return _cancelBtn;
}
-(void)hiddenView{
    if(self.changeType){
        self.changeType(@"");
    }
}
- (VULButton *)sureBtn {
    if (!_sureBtn) {
        _sureBtn = [VULButton getCustomBtnWithFrame:CGRectZero title:KLanguage(@"确定") Font:16 Bgcolor:BtnColor Target:self action:@selector(sureBtnClicked)];
        _sureBtn.layer.cornerRadius = fontAuto(19);
        [_sureBtn setTitleColor:UIColor.whiteColor forState:UIControlStateNormal];
    }
    return _sureBtn;
}
-(void)sureBtnClicked{
    if(self.changeType){
        self.changeType(self.type);
    }
}
@end
