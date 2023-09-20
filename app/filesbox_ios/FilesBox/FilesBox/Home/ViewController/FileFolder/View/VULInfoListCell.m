//
//  VULInfoListCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/6/15.
//

#import "VULInfoListCell.h"
@interface VULInfoListCell ()

@property (nonatomic,strong) UIView *bgView;

@property (nonatomic,strong) UIImageView *img;
@property (nonatomic,strong) VULLabel *titleLabel;
@property (nonatomic,strong) VULLabel *detailLabel;
@property (nonatomic,strong) VULLabel *timeLabel;
@property (nonatomic,strong) UIImageView *topIcon;
@property (nonatomic,strong) UIImageView *lookIcon;
@property (nonatomic,strong) VULLabel *lookCount;
@property (nonatomic,strong) UIImageView *praiseIcon;
@property (nonatomic,strong) VULLabel *praiseCount;
@property (nonatomic,strong) UIView *lineV;
@property (nonatomic,strong) VULButton *shareBtn;

@end
@implementation VULInfoListCell
- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        
        self.backgroundColor = UIColor.whiteColor;
        [self.contentView addSubview:self.bgView];
        [self.bgView addSubview:self.img];
        [self.img addSubview:self.shareBtn];

        [self.bgView addSubview:self.lineV];
        [self.bgView addSubview:self.titleLabel];
        [self.bgView addSubview:self.detailLabel];
        [self.bgView addSubview:self.timeLabel];
        [self.img addSubview:self.topIcon];
        [self.bgView addSubview:self.lookIcon];
        [self.bgView addSubview:self.lookCount];
        [self.bgView addSubview:self.praiseIcon];
        [self.bgView addSubview:self.praiseCount];
        [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(0);
            make.left.mas_equalTo(0);
            make.right.mas_equalTo(0);
            make.bottom.mas_equalTo(0);

        }];
        [self.img mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10));
            make.left.mas_equalTo(10);
            make.bottom.mas_equalTo(-fontAuto(10));
            make.width.mas_equalTo(fontAuto(120));
        }];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10));
            make.left.mas_equalTo(self.img.mas_right).offset(10);
            make.right.mas_equalTo(-10);
            make.height.mas_equalTo(fontAuto(20));
        }];
        [self.shareBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(2));
            make.right.mas_equalTo(fontAuto(-2));
            make.width.height.mas_equalTo(fontAuto(24));
        }];
        [self.detailLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(0);;
            make.left.mas_equalTo(self.img.mas_right).offset(10);
            make.right.mas_equalTo(-10);
            make.height.mas_equalTo(fontAuto(40));
        }];
        [self.timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.mas_equalTo(-10);
            make.left.mas_equalTo(self.img.mas_right).offset(10);
        }];
        [self.lookIcon mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(self.timeLabel.mas_centerY);
            make.left.mas_equalTo(self.timeLabel.mas_right).offset(10);
            make.width.mas_equalTo(12);
        }];
        [self.lookCount mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(self.timeLabel.mas_centerY);
            make.left.mas_equalTo(self.lookIcon.mas_right).offset(5);
        }];
        [self.praiseIcon mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(self.timeLabel.mas_centerY);
            make.left.mas_equalTo(self.lookCount.mas_right).offset(10);
            make.width.mas_equalTo(12);
        }];
        [self.praiseCount mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(self.timeLabel.mas_centerY);
            make.left.mas_equalTo(self.praiseIcon.mas_right).offset(5);
        }];
    
        [self.topIcon mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(5);
            make.left.mas_equalTo(5);
        }];
        [self.lineV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.mas_equalTo(-1);
            make.height.mas_equalTo(1);
            make.right.left.mas_equalTo(0);
        }];
    }
    return self;
    
}
-(void)setModel:(VULInfoModel *)model{
    _model = model;
    self.titleLabel.text = model.title;
    self.detailLabel.text = model.introduce;
    NSString *string = model.nickname;
    if(string.length==0){
        string = model.userName;
    }
    if(string.length>5){
        string = [string substringToIndex:5];
    }
    self.timeLabel.text = [NSString stringWithFormat:@"%@  %@",string,[NSDate updateTimeForRow:getTimeWithTime(model.modifyTime?model.modifyTime:model.createTime)]];
    self.lookCount.text = model.viewCount;
    self.praiseCount.text = model.likeCount;
    NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.thumb];
    [self.img sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:VULGetImage(@"icon_news_nodata")];
    self.topIcon.hidden = !model.isTop.boolValue;
    NSDictionary *roleDic = [[NSUserDefaults standardUserDefaults] objectForKey:@"role"];
    NSString *roleStr = [NSString stringWithFormat:@"%@",roleDic[@"explorer.informationView"]];
    NSString *roleStr1 = [NSString stringWithFormat:@"%@",roleDic[@"explorer.share"]];
    self.shareBtn.hidden = YES;
    if(roleStr.boolValue && roleStr1.boolValue){
        self.shareBtn.hidden = NO;
    }
}
-(UIView *)bgView{
    if(!_bgView){
        _bgView = [UIView new];
        _bgView.backgroundColor = [UIColor whiteColor];
    }
    return _bgView;
}
-(UIView *)lineV{
    if(!_lineV){
        _lineV = [UIView new];
        _lineV.backgroundColor = HEXCOLOR(0xececec);
    }
    return _lineV;
}

- (UIImageView *)img {
    if (!_img) {
        _img = [[UIImageView alloc] init];
        _img.contentMode = UIViewContentModeScaleAspectFill;
        _img.clipsToBounds = YES;
        _img.image = VULGetImage(@"icon_news_nodata");
        
    }
    return _img;
}
- (UIImageView *)topIcon {
    if (!_topIcon) {
        _topIcon = [[UIImageView alloc] init];
        _topIcon.contentMode = UIViewContentModeScaleAspectFit;
        _topIcon.image =VULGetImage(@"icon_top");

    }
    return _topIcon;
}
- (UIImageView *)lookIcon {
    if (!_lookIcon) {
        _lookIcon = [[UIImageView alloc] init];
        _lookIcon.contentMode = UIViewContentModeScaleAspectFit;
        _lookIcon.image =VULGetImage(@"icon_look");

    }
    return _lookIcon;
}
- (UIImageView *)praiseIcon {
    if (!_praiseIcon) {
        _praiseIcon = [[UIImageView alloc] init];
        _praiseIcon.contentMode = UIViewContentModeScaleAspectFit;
        _praiseIcon.image =VULGetImage(@"icon_praise");

    }
    return _praiseIcon;
}
-(VULButton *)shareBtn{
    if (!_shareBtn) {
        _shareBtn = [VULButton new];
        [_shareBtn addTarget:self action:@selector(clickShareBtn) forControlEvents:UIControlEventTouchUpInside];
        [_shareBtn setImage:VULGetImage(@"icon_info_share") forState:UIControlStateNormal];
    }
    return _shareBtn;
}

-(void)clickShareBtn{
    if(self.shareWithModel){
        self.shareWithModel(self.model);
    }
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:15+1] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.lineBreakMode = NSLineBreakByTruncatingMiddle;
    }
    return _titleLabel;
}
- (VULLabel *)detailLabel {
    if (!_detailLabel) {
        _detailLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:14] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _detailLabel;
}
- (VULLabel *)timeLabel {
    if (!_timeLabel) {
        _timeLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:12] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _timeLabel;
}
- (VULLabel *)lookCount {
    if (!_lookCount) {
        _lookCount = [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:12] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _lookCount;
}
- (VULLabel *)praiseCount {
    if (!_praiseCount) {
        _praiseCount = [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:12] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _praiseCount;
}
@end
