//
//  FBNewHtmlView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/8/30.
//

#import "FBNewHtmlView.h"

@implementation FBNewHtmlView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];
        [self addSubview:self.titleLabel];
        [self addSubview:self.textView];
        [self addSubview:self.btnHtml];
        [self addSubview:self.btnText];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.left.right.mas_offset(0);
            make.height.mas_equalTo(fontAuto(0));
        }];
        [self.textView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(20));
            make.left.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(10));
            make.height.mas_equalTo(fontAuto(100));
        }];

        CGFloat left = (VULSCREEN_WIDTH*0.8-fontAuto(20)-fontAuto(200))/2;
        [self.btnHtml mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.textView.mas_bottom).offset(fontAuto(20));
            make.left.mas_equalTo(left);
            make.height.mas_equalTo(fontAuto(40));
            make.width.mas_equalTo(fontAuto(100));
        }];
        [self.btnText mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.textView.mas_bottom).offset(fontAuto(20));
            make.left.mas_equalTo(self.btnHtml.mas_right).offset(fontAuto(20));
            make.height.mas_equalTo(fontAuto(40));
            make.width.mas_equalTo(fontAuto(100));
        }];
        [self layoutIfNeeded];
        self.height =self.btnText.bottom+fontAuto(20);
       
        [[self.textView rac_textSignal] subscribeNext:^(NSString *_Nullable x) {
            self.textView.text = x;
            if([self isValidURL:self.textView.text]){
                self.btnHtml.enabled = YES;
                self.btnHtml.backgroundColor = [BtnColor colorWithAlphaComponent:1];

            }else{
                self.btnHtml.enabled = NO;
                self.btnHtml.backgroundColor = [BtnColor colorWithAlphaComponent:0.4];

            }
        }];

    }
    return self;
}

- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"剪切板") TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _titleLabel;
}
- (UITextView *)textView {
    if (!_textView) {
        _textView =  [UITextView new];
        _textView.textColor = HEXCOLOR(0x333333);
        _textView.backgroundColor = UIColor.whiteColor;
        _textView.layer.borderColor = HEXCOLOR(0xececec).CGColor;
        _textView.layer.borderWidth = 1;

    }
    return _textView;
}
-(VULButton *)btnHtml{
    if(!_btnHtml){
        _btnHtml = [VULButton new];
        [_btnHtml setTitle:KLanguage(@"保存网址") forState:UIControlStateNormal];
        [_btnHtml addTarget:self action:@selector(copyText) forControlEvents:UIControlEventTouchUpInside];
        _btnHtml.backgroundColor = BtnColor;
        _btnHtml.layer.masksToBounds = YES;
        _btnHtml.layer.cornerRadius = fontAuto(20);
        [_btnHtml setTitleColor:HEXCOLOR(0xffffff) forState:UIControlStateNormal];
    }
    return _btnHtml;
}
-(VULButton *)btnText{
    if(!_btnText){
        _btnText = [VULButton new];
        [_btnText setTitle:KLanguage(@"保存TXT") forState:UIControlStateNormal];
        [_btnText addTarget:self action:@selector(clickBtnText) forControlEvents:UIControlEventTouchUpInside];
        _btnText.backgroundColor = BtnColor;
        _btnText.layer.masksToBounds = YES;
        _btnText.layer.cornerRadius = fontAuto(20);
        [_btnText setTitleColor:HEXCOLOR(0xffffff) forState:UIControlStateNormal];
    }
    return _btnText;
}
-(void)clickBtnText{
    self.textView.text = [self.textView.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    if (self.textView.text.length==0) {
        [kWindow makeCenterToast:KLanguage(@"请输入")];
        return;
    }
    if(self.selectBlock){
        self.selectBlock(self.textView.text,NO);
    }
}
-(void)copyText{
    self.textView.text = [self.textView.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    if (self.textView.text.length==0) {
        [kWindow makeCenterToast:KLanguage(@"请输入")];
        return;
    }
    if(![self isValidURL:self.textView.text]){
        [kWindow makeCenterToast:KLanguage(@"请输入正确网址")];
        return;
    }
    if(self.selectBlock){
        self.selectBlock(self.textView.text,YES);
    }
}
//判断网址
- (BOOL)isValidURL:(NSString *)string {
    NSString *urlPattern = @"^(https?|ftp)://([A-Za-z0-9.-]+)(:[0-9]+)?(/.*)?$";
    NSPredicate *urlPredicate = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", urlPattern];
    return [urlPredicate evaluateWithObject:string];
}


@end
