//
//  FBFileProgress.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/18.
//

#import "FBFileProgress.h"
@interface FBFileProgress ()
@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, strong) UIView *progressView;


@end
@implementation FBFileProgress
-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        
        [self setView];
    }
    return self;
}
-(void)setView{
    [self addSubview:self.titleLabel];
    [self addSubview:self.bgView];
    [self.bgView addSubview:self.progressView];
    
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.right.mas_equalTo(0);
        make.height.mas_equalTo(10);
    }];
    [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(2);
        make.left.right.mas_equalTo(0);
        make.height.mas_equalTo(6);
        make.width.mas_equalTo(self.mas_width);

    }];
    [self.progressView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(0);
        make.left.mas_equalTo(0);
        make.width.mas_equalTo(1);
        make.height.mas_equalTo(6);
    }];

    
}
-(void)setTitle:(NSString *)title{
    self.titleLabel.text = title;
}
-(void)changeProgressWith:(float)per{
    
//    self.progressView.width = self.width*per;

    [self.progressView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(0);
        make.left.mas_equalTo(0);
        make.height.mas_equalTo(6);
        make.width.mas_equalTo(self.width*per);
    }];
}
-(UILabel *)titleLabel{
    if (!_titleLabel) {
        _titleLabel = [UILabel new];
        _titleLabel.textColor = HEXCOLOR(0xffffff);
        _titleLabel.textAlignment = NSTextAlignmentCenter;
        _titleLabel.font = [UIFont yk_pingFangSemibold:fontAuto(8)];
    }
    return _titleLabel;
}
-(UIView*)progressView{
    if (!_progressView) {
        _progressView = [UIView new];
        _progressView.backgroundColor = [UIColor whiteColor];
        _progressView.layer.masksToBounds = YES;
        _progressView.layer.cornerRadius = 3;
    }
    return _progressView;
}
-(UIView*)bgView{
    if (!_bgView) {
        _bgView = [UIView new];
        _bgView.layer.masksToBounds = YES;
        _bgView.layer.cornerRadius = 3;
        _bgView.backgroundColor = [UIColorHex(#FFFFFF) colorWithAlphaComponent:0.2];
    }
    return _bgView;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
