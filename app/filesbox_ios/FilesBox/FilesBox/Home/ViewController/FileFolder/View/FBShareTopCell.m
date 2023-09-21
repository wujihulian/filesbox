//
//  FBShareTopCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/29.
//

#import "FBShareTopCell.h"
@interface FBShareTopCell ()

@property (nonatomic, strong) UIImageView *iconImageV;
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *detailLabel;
@property (nonatomic, strong) VULLabel *threeLabel;

@end
@implementation FBShareTopCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.contentView.backgroundColor = [UIColor whiteColor];
        [self.contentView addSubview:self.iconImageV];
        [self.contentView addSubview:self.titleLabel];
        [self.contentView addSubview:self.detailLabel];
        [self.contentView addSubview:self.threeLabel];

        [self.iconImageV  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
            make.width.mas_equalTo(fontAuto(60));
            make.height.mas_equalTo(fontAuto(70));
            make.bottom.mas_equalTo(-fontAuto(10));

        }];
        [self.titleLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.iconImageV .mas_right).offset(fontAuto(10));
            make.top.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(10));
        }];
        
        [self.detailLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.iconImageV .mas_right).offset(fontAuto(10));
            make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(fontAuto(5));
            make.right.mas_equalTo(-fontAuto(10));
        }];
        [self.threeLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.iconImageV .mas_right).offset(fontAuto(10));
            make.top.mas_equalTo(self.detailLabel.mas_bottom).offset(fontAuto(5));          make.right.mas_equalTo(-fontAuto(10));
        }];
    }
    return self;
    
}
-(void)setModel:(VULFileObjectModel *)model{
    
    if (!model.isFolder.boolValue) { //是文件
        if (model.thumb &&model.thumb.length>0&&isVideoOrMusic(model.fileType)) {
            if ([model.fileType isEqualToString:@"gif"]) {
                NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.downloadUrl];
                [self.iconImageV sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];
        
            }else{
                
                NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(model.thumb,3,model.fileType)];
                [self.iconImageV sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];
            }
        }else if ( model.path&&model.path.length>0) {
            if ([model.fileType isEqualToString:@"gif"]) {
                NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.downloadUrl];
                [self.iconImageV sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];
        
            }else{
                
                NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(model.path,3,model.fileType)];
                [self.iconImageV sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];
            }
       

        }else{
          
            self.iconImageV.image =getLocalImageWithFileType(model.fileType);
            
        }
        

    } else {
        self.iconImageV.image =VULGetImage(@"icon_folder_main");
    }
 
    self.titleLabel.text =model.name;
    self.detailLabel.text = [NSString stringWithFormat:@"%@:%@",KLanguage(@"到期时间"),KLanguage(@"永久有效")];
    self.threeLabel.text = [NSString stringWithFormat:@"%@:%@  %@:%@",KLanguage(@"下载次数"),model.numDownload,KLanguage(@"浏览次数"),model.numView];

}

-(UIImageView *)iconImageV{
    if (!_iconImageV) {
        _iconImageV = [UIImageView new];
        _iconImageV.contentMode = UIViewContentModeScaleAspectFit;

    }
    return _iconImageV;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.numberOfLines =1;
    }
    return _titleLabel;
}
- (VULLabel *)detailLabel {
    if (!_detailLabel) {
        _detailLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:fontAuto(14)] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _detailLabel;
}
- (VULLabel *)threeLabel {
    if (!_threeLabel) {
        _threeLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:fontAuto(14)] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _threeLabel;
}
- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
