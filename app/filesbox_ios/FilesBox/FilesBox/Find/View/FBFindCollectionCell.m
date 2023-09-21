//
//  FBFindCollectionCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/20.
//

#import "FBFindCollectionCell.h"
@interface FBFindCollectionCell ()<UITextFieldDelegate>


@end
@implementation FBFindCollectionCell
- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.contentView.backgroundColor = [UIColor whiteColor];
        [self createUI];

    }
    return self;
}
-(void)createUI{
    [self.contentView addSubview:self.bgView];
    [self.bgView addSubview:self.titleLabel];
    [self.bgView addSubview:self.imageV];
    
    [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(0);
    }];
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(0);
        make.height.mas_equalTo(fontAuto(75));
        make.width.mas_equalTo((VULSCREEN_WIDTH-30)/2-fontAuto(50)-fontAuto(15)-fontAuto(20));
    }];
    [self.imageV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(-fontAuto(15));
        make.width.mas_equalTo(fontAuto(60));
        make.centerY.mas_equalTo(self.bgView.mas_centerY);
    }];
}
-(UIView *)bgView{
    if (!_bgView) {
        _bgView = [UIView new];
        _bgView.backgroundColor = HEXCOLOR(0xFAFAFA);
    }
    return _bgView;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:14+2] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _titleLabel;
}
-(UIImageView *)imageV{
    if (!_imageV) {
        _imageV = [UIImageView new];
        _imageV.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _imageV;
}

@end
