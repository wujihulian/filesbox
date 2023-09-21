//
//  FBAddTagView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/22.
//

#import "FBAddTagView.h"
#import "FBTagColorView.h"
@interface FBAddTagView ()

@property (nonatomic, strong) VULLabel *titleLbael;
@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, strong) UIView *bgView1;

@property (nonatomic, strong) UIView *colorView;
@property (nonatomic, strong) UIImageView *rightImagev;

@property (nonatomic, strong) VULButton *cancelBtn;
@property (nonatomic, strong) VULButton *confirmBtn;
@property (nonatomic, strong) UITextField *textField;

@end
@implementation FBAddTagView
- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];
        self.layer.masksToBounds = YES;
        self.layer.cornerRadius = 5;

        [self addSubview:self.titleLbael];
        [self addSubview:self.bgView];
        [self.bgView addSubview:self.bgView1];
        [self.bgView addSubview:self.textField];
        [self.bgView1 addSubview:self.colorView];
        [self.bgView1 addSubview:self.rightImagev];

        [self addSubview:self.cancelBtn];
        [self addSubview:self.confirmBtn];
        [self.titleLbael mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(0);
            make.left.right.mas_equalTo(0);
            make.height.mas_equalTo(50);
        }];
        [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.titleLbael.mas_bottom).offset(10);
            make.left.mas_equalTo(15);
            make.right.mas_equalTo(-15);
            make.height.mas_equalTo(40);
        }];
        [self.textField mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.top.mas_equalTo(0);
            make.left.mas_equalTo(10);
            make.right.mas_equalTo(-48);
            make.height.mas_equalTo(40);
        }];
        [self.bgView1 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.top.mas_equalTo(0);
            make.width.mas_equalTo(48);
            make.right.mas_equalTo(0);
            make.height.mas_equalTo(40);
        }];
        [self.colorView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(self.bgView1.mas_centerY);
            make.left.mas_equalTo(9);
            make.height.width.mas_equalTo(14);
        }];
        [self.rightImagev mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(self.bgView1.mas_centerY);
            make.width.mas_equalTo(12);
            make.right.mas_equalTo(-6);
        }];
        [self.cancelBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.bgView.mas_bottom).offset(25);
            make.left.mas_equalTo(fontAuto(19));
            make.width.mas_equalTo(fontAuto(115));
            make.height.mas_equalTo(fontAuto(38));
        }];
        [self.confirmBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.bgView.mas_bottom).offset(25);
            make.right.mas_equalTo(-fontAuto(19));
            make.width.mas_equalTo(fontAuto(115));
            make.height.mas_equalTo(fontAuto(38));
        }];
        self.userInteractionEnabled = YES;
        self.bgView.userInteractionEnabled = YES;
        self.bgView1.userInteractionEnabled = YES;

        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickBgView1)];
        [self.bgView1 addGestureRecognizer:tap];
        [[self.textField rac_textSignal] subscribeNext:^(NSString *_Nullable x) {
            if (x.length<=16) {
            }else{
                self.textField.text = [x substringToIndex:16];
            }
        }];
    }
    return self;
}
-(void)clickBgView1{
    [self.textField resignFirstResponder];
    CGFloat left = (VULSCREEN_WIDTH-9*20)/10;
    FBTagColorView *tagV = [[FBTagColorView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, left*4+60+K_BottomBar_Height)];
    zhPopupController *popup2 =[[zhPopupController alloc] initWithView:tagV size:CGSizeMake(VULSCREEN_WIDTH, tagV.height)];

    tagV.selectTagColorWithString = ^(NSString * _Nonnull rgbString) {
        self.colorStr =rgbString;
        self.colorView.backgroundColor = getColorWithGgb(rgbString);
        [popup2 dismiss];
    };
    popup2.layoutType = zhPopupLayoutTypeBottom;
    popup2.presentationStyle = zhPopupSlideStyleFromBottom;
    popup2.maskAlpha = 0.35;
    [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
}

-(void)setModel:(FBTagModel *)model{
    _model = model;
    self.textField.text = model.labelId.integerValue<0?@"":model.labelName;
    self.titleLbael.text = model.labelId.integerValue<0?KLanguage(@"添加标签"):KLanguage(@"编辑标签");
    self.colorView.backgroundColor = model.labelId.integerValue<0?getColorWithGgb(@"rgb(193, 28, 123)"): getColorWithGgb(model.style);
    self.colorStr = model.style;
}

#pragma layZ
-(VULButton *)cancelBtn{
    if (!_cancelBtn) {
        _cancelBtn = [VULButton new];
        [_cancelBtn setTitle:KLanguage(@"取消") forState:UIControlStateNormal];
        _cancelBtn.layer.masksToBounds = YES;
        _cancelBtn.layer.cornerRadius = fontAuto(19);
        _cancelBtn.layer.borderColor = HEXCOLOR(0xDCDEE0).CGColor;
        _cancelBtn.layer.borderWidth =1;
        [_cancelBtn setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
        [_cancelBtn addTarget:self action:@selector(clickCancelBtn) forControlEvents:UIControlEventTouchUpInside];

    }
    return _cancelBtn;
}
-(void)clickCancelBtn{
    if (self.dismiss) {
        self.dismiss();
    }
}
-(VULButton *)confirmBtn{
    if (!_confirmBtn) {
        _confirmBtn = [VULButton new];
        [_confirmBtn setTitle:KLanguage(@"确定") forState:UIControlStateNormal];
        _confirmBtn.backgroundColor = BtnColor;
        [_confirmBtn setTitleColor:HEXCOLOR(0xffffff) forState:UIControlStateNormal];
        _confirmBtn.layer.masksToBounds = YES;
        _confirmBtn.layer.cornerRadius = fontAuto(19);
        _confirmBtn.layer.borderColor = BtnColor.CGColor;
        _confirmBtn.layer.borderWidth =1;
        [_confirmBtn addTarget:self action:@selector(clickConfirmBtn) forControlEvents:UIControlEventTouchUpInside];
    }
    
    return _confirmBtn;
}
-(void)clickConfirmBtn{
    self.textField.text = [self.textField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    if (self.textField.text.length==0) {
        [kWindow makeCenterToast:KLanguage(@"请输入标签名称")];
        return;
    }
    if (self.addOrEditTag) {
        self.addOrEditTag(self.colorStr,self.textField.text,_model.labelId);
    }
    
}
-(UIImageView *)rightImagev{
    if (!_rightImagev) {
        _rightImagev = [UIImageView new];
        _rightImagev.image = VULGetImage(@"icon_tag_up");
    }
    return _rightImagev;;
}

-(UIView *)bgView1{
    if (!_bgView1) {
        _bgView1 = [UIView new];
        _bgView1.backgroundColor = HEXCOLOR(0xF5F5F5);

    }
    return _bgView1;
}
-(UIView *)colorView{
    if (!_colorView) {
        _colorView = [UIView new];
        _colorView.layer.masksToBounds = YES;
        _colorView.layer.cornerRadius =  7;
        _colorView.layer.borderColor = HEXCOLOR(0x7459E3).CGColor;
        _colorView.layer.borderWidth=1;
    }
    return _colorView;
}
-(UITextField *)textField{
    if (!_textField) {
        _textField = [UITextField new];
        _textField.font = [UIFont yk_pingFangRegular:fontAuto(16)];
        _textField.placeholder = KLanguage(@"请输入标签名称");
    }
    return _textField;
}
   
//FBTagColorView
    
-(UIView *)bgView{
    if (!_bgView) {
        _bgView = [UIView new];
        _bgView.layer.borderColor = HEXCOLOR(0xDCDEE0).CGColor;
        _bgView.layer.borderWidth =1;
        _bgView.layer.masksToBounds = YES;
        _bgView.layer.cornerRadius = 5;
        _bgView.clipsToBounds = YES;
    }
    return _bgView;
}
    

- (VULLabel *)titleLbael {
    if (!_titleLbael) {
        _titleLbael = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"添加标签") TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangMedium:17] Color:UIColorHex(#333333) BgColor:nil];
//        1px solid #d9d9d9
 

    }
    return _titleLbael;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
