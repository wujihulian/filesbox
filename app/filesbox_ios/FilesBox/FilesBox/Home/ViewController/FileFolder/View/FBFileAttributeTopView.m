//
//  FBFileAttributeTopView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/8.
//

#import "FBFileAttributeTopView.h"
@interface FBFileAttributeTopView()

@property (nonatomic, strong) UIImageView *iconImageV;
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *detialLabel;

@end
@implementation FBFileAttributeTopView
- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        // 创建渐变层
        CAGradientLayer *gradientLayer = [CAGradientLayer layer];

        // 设置渐变的颜色数组
        gradientLayer.colors = @[(__bridge id)[UIColorHex(#722ed1) colorWithAlphaComponent:0.1].CGColor, (__bridge id)[UIColorHex(#722ed1) colorWithAlphaComponent:0.01].CGColor];

        // 设置渐变的起始点和终止点
        gradientLayer.startPoint = CGPointMake(0.5, 0);
        gradientLayer.endPoint = CGPointMake(0.5, 1);

        // 设置渐变层的位置和大小
        gradientLayer.frame = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height);

        // 将渐变层添加到视图的 layer 上
        [self.layer addSublayer:gradientLayer];
        [self addSubview:self.iconImageV];
        [self addSubview:self.titleLabel];
        [self addSubview:self.detialLabel];
        
        [self.iconImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10)+K_StatusBar_Height);
            make.left.mas_equalTo(fontAuto(10));
            make.width.height.mas_equalTo(fontAuto(45));
        }];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10)+K_StatusBar_Height);
            make.left.mas_equalTo(self.iconImageV.mas_right).offset(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(40));
        }];
        [self.detialLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(fontAuto(5));
            make.left.mas_equalTo(self.iconImageV.mas_right).offset(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(40));
        }];
    }
    return self;
}

- (void)setModel:(VULFileObjectModel *)model {
    if (!model.isFolder.boolValue) { //是文件
        if (model.path) {
            NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(model.path,3,model.fileType)];
            [self.iconImageV sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];

        }else{
            self.iconImageV.image =getLocalImageWithFileType(model.fileType);
            
        }
        self.titleLabel.text = model.name;
        
    } else {
        self.iconImageV.image =VULGetImage(@"icon_folder_main");
        self.titleLabel.text = model.name;
    }
    NSString *createTime = [NSDate getRecentTimeWithTime:getTimeWithTime(model.modifyTime?model.modifyTime:model.createTime)];

    self.detialLabel.text = [NSString stringWithFormat:@"%@ %@",formattedFileSize(model.size.integerValue),createTime];
}
-(UIImageView *)iconImageV{
    if (!_iconImageV) {
        _iconImageV = [UIImageView new];
    }
    return _iconImageV;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#666666) BgColor:nil];
        _titleLabel.numberOfLines = 1;
    }
    return _titleLabel;
}
- (VULLabel *)detialLabel {
    if (!_detialLabel) {
        _detialLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _detialLabel;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
