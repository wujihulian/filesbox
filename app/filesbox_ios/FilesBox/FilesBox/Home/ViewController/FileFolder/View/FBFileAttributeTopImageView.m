//
//  FBFileAttributeTopImageView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/8.
//

#import "FBFileAttributeTopImageView.h"
@interface FBFileAttributeTopImageView()

@property (nonatomic, strong) UIImageView *bgImageV;
@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, strong) VULLabel *titleLabel;

@end
@implementation FBFileAttributeTopImageView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {

        [self addSubview:self.bgImageV];
        [self addSubview:self.bgView];
        [self.bgView addSubview:self.titleLabel];
        
        [self.bgImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(0);
            make.left.mas_equalTo(fontAuto(0));
            make.width.mas_equalTo(VULSCREEN_WIDTH);
            make.height.mas_equalTo(fontAuto(150));
        }];
        [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.mas_equalTo(0);
            make.width.mas_equalTo(VULSCREEN_WIDTH);
            make.height.mas_equalTo(fontAuto(30));
        }];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.offset(0);
            make.left.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(10));
            make.height.mas_equalTo(fontAuto(30));
        }];
    }
    return self;
}
-(void)setModel:(VULFileObjectModel *)model{

    if ([model.fileType isEqualToString:@"gif"]) {
        NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.downloadUrl];
        url = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
        [self.bgImageV sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];

    }else{
        NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(model.path,1,model.fileType)];
        [self.bgImageV sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];

    }
    self.titleLabel.text = model.name;
}
-(UIImageView *)bgImageV{
    if (!_bgImageV) {
        _bgImageV = [UIImageView new];
        _bgImageV.contentMode = UIViewContentModeScaleAspectFill;
        _bgImageV.clipsToBounds = YES;
    }
    return _bgImageV;
}
-(UIView *)bgView{
    if (!_bgView) {
        _bgView = [UIView new];
        _bgView.backgroundColor = [HEXCOLOR(0x000000) colorWithAlphaComponent:0.2];
    }
    return _bgView;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#ffffff) BgColor:nil];
    }
    return _titleLabel;
}
@end
