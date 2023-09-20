//
//  FBTextShowView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/6/28.
//

#import "FBTextShowView.h"

@implementation FBTextShowView

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
        [self addSubview:self.btnCopy];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.left.right.mas_offset(0);
            make.height.mas_equalTo(fontAuto(50));
        }];
        [self.textView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(60));
            make.left.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(10));
            make.bottom.mas_equalTo(-fontAuto(80));
        }];
        [self.btnCopy mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(fontAuto(40));
            make.width.mas_equalTo(fontAuto(120));
            make.bottom.mas_equalTo(-fontAuto(20));
            make.centerX.mas_equalTo(self.mas_centerX);
        }];
    }
    return self;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"提取文字") TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _titleLabel;
}
- (UITextView *)textView {
    if (!_textView) {
        _textView =  [UITextView new];
        _textView.editable = NO;
        _textView.textColor = HEXCOLOR(0x333333);
        _textView.backgroundColor = UIColor.whiteColor;
    }
    return _textView;
}
-(VULButton *)btnCopy{
    if(!_btnCopy){
        _btnCopy = [UIButton new];
        [_btnCopy setTitle:KLanguage(@"复制") forState:UIControlStateNormal];
        [_btnCopy addTarget:self action:@selector(copyText) forControlEvents:UIControlEventTouchUpInside];
        _btnCopy.backgroundColor = BtnColor;
        [_btnCopy setTitleColor:HEXCOLOR(0xffffff) forState:UIControlStateNormal];
    }
    return _btnCopy;
}
-(void)copyText{
    [[UIPasteboard generalPasteboard] setString:self.textView.text.length>0?self.textView.text:@""];
    [kWindow makeToast:KLanguage(@"复制成功")];
}
@end
