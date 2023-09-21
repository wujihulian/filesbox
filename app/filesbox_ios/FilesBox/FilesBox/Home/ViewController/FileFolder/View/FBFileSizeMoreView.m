//
//  FBFileSizeMoreView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/20.
//

#import "FBFileSizeMoreView.h"
@interface FBFileSizeMoreView ()
@property (nonatomic, strong) VULLabel *leftLabel;
@property (nonatomic, strong) VULLabel *leftEndLabel;
@property (nonatomic, strong) UITextField *leftTextField;
@property (nonatomic, strong) VULLabel *rightLabel;
@property (nonatomic, strong) VULLabel *rightEndLabel;
@property (nonatomic, strong) UITextField *rightTextField;
@property (nonatomic, strong) VULLabel *bottomLabel;
@property (nonatomic, strong) UIView *leftView;
@property (nonatomic, strong) UIView *rightView;

@end
@implementation FBFileSizeMoreView
- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        [self addSubview:self.leftView];
        [self addSubview:self.rightView];
        [self addSubview:self.bottomLabel];
        [self.leftView addSubview:self.leftLabel];
        [self.leftView addSubview:self.leftEndLabel];
        [self.leftView addSubview:self.leftTextField];
        [self.rightView addSubview:self.rightLabel];
        [self.rightView addSubview:self.rightEndLabel];
        [self.rightView addSubview:self.rightTextField];
        CGFloat width = (VULSCREEN_WIDTH-fontAuto(44))/2;
        [self.leftView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.left.mas_equalTo(0);
            make.bottom.mas_equalTo(-fontAuto(14));
            make.width.mas_equalTo(width);
        }];
        [self.rightView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(0);
            make.right.mas_equalTo(0);
            make.bottom.mas_equalTo(-fontAuto(14));
            make.width.mas_equalTo(width);
        }];
        [self.bottomLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(0);
            make.top.mas_equalTo(self.leftView.mas_bottom);
            make.bottom.mas_equalTo(-0);
        }];
        [self.leftLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.left.top.mas_equalTo(0);
            make.width.mas_equalTo(40);
        }];
        
        [self.leftTextField mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.top.mas_equalTo(0);
            make.width.mas_equalTo(width-80);
            make.left.mas_equalTo(self.leftLabel.mas_right);
        }];
        [self.leftEndLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.bottom.top.mas_equalTo(0);
            make.width.mas_equalTo(40);
        }];
        [self.rightLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.left.top.mas_equalTo(0);
            make.width.mas_equalTo(40);
        }];
        
        [self.rightTextField mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.top.mas_equalTo(0);
            make.left.mas_equalTo(self.rightLabel.mas_right);
            make.width.mas_equalTo(width-80);

        }];
        [self.rightEndLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.bottom.top.mas_equalTo(0);
            make.width.mas_equalTo(40);
        }];
        //注册键盘出现通知
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardDidShow:) name:UIKeyboardDidShowNotification object:nil];
        // 注册键盘隐藏通知
        [[NSNotificationCenter defaultCenter] addObserver:self selector: @selector(keyboardDidHide:) name:UIKeyboardDidHideNotification object:nil];
    
    }
    return  self;
    
}
-(void)keyboardDidShow:(NSNotification *) notification{
    NSLog(@"键盘打开了");
}
-(void)keyboardDidHide: (NSNotification *) notification{
    NSLog(@"键盘关闭了");
    if (self.clickViewBlock) {
        self.clickViewBlock(self.leftTextField.text,self.rightTextField.text);    }
}
-(UITextField *)leftTextField{
    if (!_leftTextField) {
        _leftTextField = [UITextField new];
        _leftTextField.backgroundColor  =UIColor.whiteColor;
        _leftTextField.layer.borderColor = HEXCOLOR(0xd9d9d9).CGColor;
        _leftTextField.layer.borderWidth = 1;
        _leftTextField.keyboardType = UIKeyboardTypeDecimalPad;
        _leftTextField.textAlignment = NSTextAlignmentCenter;

    }
    return _leftTextField;
    
}
-(UITextField *)rightTextField{
    if (!_rightTextField) {
        _rightTextField = [UITextField new];
        _rightTextField.backgroundColor  =UIColor.whiteColor;
        _rightTextField.layer.borderColor = HEXCOLOR(0xd9d9d9).CGColor;
        _rightTextField.layer.borderWidth = 1;
        _rightTextField.keyboardType = UIKeyboardTypeDecimalPad;
        _rightTextField.textAlignment = NSTextAlignmentCenter;
    }
    return _rightTextField;
    
}
- (VULLabel *)rightLabel {
    if (!_rightLabel) {
        _rightLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"小于") TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _rightLabel;
}
- (VULLabel *)rightEndLabel {
    if (!_rightEndLabel) {
        _rightEndLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"MB" TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
//        1px solid #d9d9d9
    }
    return _rightEndLabel;
}

- (VULLabel *)leftLabel {
    if (!_leftLabel) {
        _leftLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"大于") TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _leftLabel;
}
- (VULLabel *)leftEndLabel {
    if (!_leftEndLabel) {
        _leftEndLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"MB" TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
//        1px solid #d9d9d9
    }
    return _leftEndLabel;
}
- (VULLabel *)bottomLabel {
    if (!_bottomLabel) {
        _bottomLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"不填则代表大于或小于某个值,可以为小数") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:10] Color:UIColorHex(#999999) BgColor:nil];
//        1px solid #d9d9d9
    }
    return _bottomLabel;
}
   

-(UIView *)leftView{
    if (!_leftView) {
        _leftView = [UIView new];
        _leftView.backgroundColor = [HEXCOLOR(0x333333) colorWithAlphaComponent:0.02];;
        _leftView.layer.masksToBounds = YES;
        _leftView.layer.cornerRadius = 5;
        _leftView.layer.borderColor = HEXCOLOR(0xd9d9d9).CGColor;
        _leftView.layer.borderWidth = 1;
    }
    return _leftView;
}
-(UIView *)rightView{
    if (!_rightView) {
        _rightView = [UIView new];
        _rightView.backgroundColor = [HEXCOLOR(0x333333) colorWithAlphaComponent:0.02];;
        _rightView.layer.masksToBounds = YES;
        _rightView.layer.cornerRadius = 5;
        _rightView.layer.borderColor = HEXCOLOR(0xd9d9d9).CGColor;
        _rightView.layer.borderWidth = 1;
    }
    return _rightView;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
