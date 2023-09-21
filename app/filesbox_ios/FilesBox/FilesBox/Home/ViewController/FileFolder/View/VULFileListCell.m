//
//  VULFileListCell.m
//  UnlimitedBusiness
//
//  Created by zuoyi on 2021/12/14.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULFileListCell.h"
#import "FBTagView.h"
@interface VULFileListCell ()

@property (nonatomic,strong) UIImageView *img;
@property (nonatomic,strong) VULLabel *titleLabel;
@property (nonatomic,strong) VULLabel *dateLabel;
@property (nonatomic,strong) UIImageView *playIcon;
@property (nonatomic,strong) UIView *timeBg;
@property (nonatomic,strong) VULLabel *timeLabel;
@property (nonatomic,strong) FBTagView *tagView;
@property (nonatomic,strong) UIImageView *topIcon;
@property (nonatomic,strong) UIImageView *collectIcon;
@property (nonatomic,strong) UIImageView *isShare;
@property (nonatomic,strong) UIImageView *isLink;

@end

@implementation VULFileListCell

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        UIView *selectedBackgroundView = [[UIView alloc] init];
        selectedBackgroundView.backgroundColor = UIColorHex(#F3FAFF);
        self.selectedBackgroundView = selectedBackgroundView;
        self.backgroundColor = UIColor.whiteColor;
        [self.contentView addSubview:self.img];
        [self.contentView addSubview:self.titleLabel];
        [self.contentView addSubview:self.dateLabel];
        [self.img addSubview:self.playIcon];
        [self.contentView addSubview:self.selectImageV];
        [self.img addSubview:self.tagView];
        [self.contentView addSubview:self.timeBg];
        [self.timeBg addSubview:self.timeLabel];
        [self.contentView addSubview:self.topIcon];
        [self.contentView addSubview:self.collectIcon];
        [self.contentView addSubview:self.isShare];
        [self.contentView addSubview:self.isLink];

            
        
        [self.img mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(self);
            make.left.mas_equalTo(15);
            make.size.mas_equalTo(CGSizeMake(45, 45));
            
        }];
        [self.tagView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.bottom.mas_equalTo(0);
            make.height.mas_equalTo(12);
            make.right.mas_equalTo(-0);

        }];
       
        [self.timeBg mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-12);
            make.width.mas_equalTo(34);
            make.centerY.mas_equalTo(self.dateLabel.mas_centerY);
        }];
        [self.topIcon mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.titleLabel.mas_right).offset(5);;
            make.width.height.mas_equalTo(0);
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);

        }];
        [self.collectIcon mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.topIcon.mas_right).offset(5);
            make.width.height.mas_equalTo(0);
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
        }];
        [self.isShare mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.collectIcon.mas_right).offset(5);
            make.width.height.mas_equalTo(0);

            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
        }];
        [self.isLink mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.isShare.mas_right).offset(5);
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
        }];
        [self.timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.left.right.bottom.mas_equalTo(0);
        }];
        [self.playIcon mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.centerX.mas_equalTo(self.img);
            make.left.mas_equalTo(15);
            make.size.mas_equalTo(CGSizeMake(20, 20));
        }];
        CGFloat width = VULSCREEN_WIDTH - 54-45-30;
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(12);
            make.left.mas_equalTo(self.img.mas_right).offset(10);
            make.width.mas_lessThanOrEqualTo(width);
            make.height.mas_equalTo(18);
        }];
        [self.dateLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(5);
            make.left.mas_equalTo(self.img.mas_right).offset(10);
            make.height.mas_equalTo(15);
        }];
        [self.selectImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-10);
            make.size.mas_equalTo(CGSizeMake(14, 14));
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
    }
    return self;
}

- (void)setModel:(VULFileObjectModel *)model {
    _model = model;
    if (!model.isFolder.boolValue && !model.oexeIsFolder.boolValue) {  //文件
        
        
        if (model.thumb &&model.thumb.length>0&&isVideoOrMusic(model.fileType)) {
            if ([model.fileType isEqualToString:@"gif"]) {
                NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.downloadUrl];
                [self.img sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];
        
            }else{
                
                NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(model.thumb,3,model.fileType)];
                [self.img sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];
            }
        }else if ( model.path&&model.path.length>0) {
            if ([model.fileType isEqualToString:@"gif"]) {
                NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.downloadUrl];
                [self.img sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];
        
            }else{
                
                NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(model.path,3,model.fileType)];
                [self.img sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];
            }
       
        }else{
            self.img.image = nil;

            if(model.oexeContent.length>0){
                NSDictionary *dic = turnStringToDictionary(model.oexeContent);
                NSString *type =  dic[@"type"];
                NSString *fileType =  dic[@"fileType"];

                
                if([model.fileType isEqualToString:@"oexe"] && [type isEqualToString:@"lnk"]){
                    self.img.image =getLocalImageWithFileType(fileType);
                }else{
                    self.img.image =getLocalImageWithFileType(model.fileType);
                    
                }
                
            }else{
                self.img.image =getLocalImageWithFileType(model.fileType);

            }
            

        }

        if([model.fileType isEqualToString:@"oexe"]){
            NSArray *arr = [model.name componentsSeparatedByString:@"."];
            self.titleLabel.text = arr[0];

        }else{
            self.titleLabel.text = model.name;

        }
       
    } else { //文件夹
    
        self.img.image =VULGetImage(@"icon_folder_main");
       if([model.fileType isEqualToString:@"oexe"]){
            NSArray *arr = [model.name componentsSeparatedByString:@"."];
            self.titleLabel.text = arr[0];

        }else{
            self.titleLabel.text = model.name;

        }//        self.dateLabel.text = [NSDate timeWithTimeIntervalString:getTimeWithTime(model.createTime?model.createTime:model.modifyTime) Format:@"yyyy-MM-dd HH:mm"];
    }
    NSString *fileSize = model.size ? [self stringWithbytes:model.size.intValue] : @"";
    
    
    self.dateLabel.text = [NSString stringWithFormat:@"%@ %@",[NSDate timeWithTimeIntervalString:getTimeWithTime(model.createTime?model.createTime:model.modifyTime) Format:@"yyyy-MM-dd HH:mm"],fileSize];

    self.img.layer.masksToBounds = NO;
    self.dateLabel.hidden = NO;
    if (model.labelId.length>0) {
        self.img.image = getImageWithColor(getColorWithGgb(model.style));
        self.img.layer.masksToBounds = YES;
        self.img.layer.cornerRadius = 45/2;
        self.dateLabel.hidden = YES;

    }

//    
//    if (NSStringIsNotEmpty(model.thumb)) {
//        NSString *thumb = model.thumb;
//        if ([model.suffix isEqualToString:@"jpg"] || [model.suffix isEqualToString:@"jpeg"] || [model.suffix isEqualToString:@"png"]) {
//            thumb = [thumb stringByReplacingOccurrencesOfString:@".jpg" withString:@"!small.jpg"];
//            thumb = [thumb stringByReplacingOccurrencesOfString:@".jpeg" withString:@"!small.jpeg"];
//            thumb = [thumb stringByReplacingOccurrencesOfString:@".png" withString:@"!small.png"];
//        }
//        NSString *coverUrl = [NSString stringWithFormat:@"%@%@",kSchoolServiceUrl, thumb];
//        [self.img sd_setImageWithURL:[NSURL URLWithString:coverUrl] placeholderImage:self.img.image];
//    }
    NSArray *videoArray = @[@"mp4",@"rm",@"rmvb",@"3gp",@"mov",@"m4v",@"wmv",@"asf",@"asx",@"avi",@"dat",@"mkv",@"flv",@"vob",@"webm",@"mpg"];

    if ([videoArray containsObject:model.fileType]) {
        self.playIcon.hidden = NO;
    } else
        self.playIcon.hidden = YES;

   
    self.timeBg.hidden = YES;

    NSArray *mp3Array = @[@"mp3",@"wav",@"wma",@"m4a",@"ogg",@"omf",@"amr",@"aa3",@"flac",@"aac",@"cda",@"aif",@"aiff",@"mid",@"ra",@"ape"];

    if (([videoArray containsObject:model.fileType] ||  [mp3Array containsObject:model.fileType])&& self.model.length.integerValue>0) {
        self.timeBg.hidden = NO;
        self.timeLabel.text = [NSString stringWithFormat:@"%.2ld:%.2ld",self.model.length.integerValue / 60, self.model.length.integerValue % 60];
      
    }
    [self.tagView removeAllSubviews];
    if (model.tagList.count>0) {
        NSMutableArray *arr = [NSMutableArray array];
        for (NSDictionary *dic in model.tagList ) {
            NSString *style = dic[@"style"];
            [arr addObject:getColorWithGgb(style)];
        }
        [self.tagView addTagWithColorArr:arr];
    }
    self.topIcon.hidden = model.sort==0;
    self.collectIcon.hidden = !model.isFav;
    self.isShare.hidden = !model.isShare;
    if(model.sort==0){
        [self.topIcon mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.titleLabel.mas_right).offset(5);;
            make.width.height.mas_equalTo(0);
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);

        }];
    }else{
        [self.topIcon mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.titleLabel.mas_right).offset(5);;
            make.width.height.mas_equalTo(16);
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);

        }];
    }
    if(model.isFav){
        [self.collectIcon mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.topIcon.mas_right).offset(5);
            make.width.height.mas_equalTo(16);
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
        }];
    }else{
        [self.collectIcon mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.topIcon.mas_right).offset(5);
            make.width.height.mas_equalTo(0);
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
        }];
    }
    
    if(model.isShare){
        [self.isShare mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.collectIcon.mas_right).offset(5);
            make.width.height.mas_equalTo(16);
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
        }];
    }else{
        [self.isShare mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.collectIcon.mas_right).offset(5);
            make.width.height.mas_equalTo(0);
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
        }];
    }
    
    if(model.oexeContent.length>0){
        NSDictionary *dic = turnStringToDictionary(model.oexeContent);
        NSString *type =  dic[@"type"];
        if([model.fileType isEqualToString:@"oexe"] && [type isEqualToString:@"lnk"]){
            self.isLink.hidden = NO;
        }else{
            self.isLink.hidden = YES;;

        }
    }else{
        self.isLink.hidden = YES;;

    }
 
    
//    

}

- (NSString *)stringWithbytes:(int)bytes {
    if (bytes < 1024) { // B
        if (bytes== 0) {
            return @"";
        }
        return [NSString stringWithFormat:@"%dB", bytes];
    } else if (bytes >= 1024 && bytes < 1024 * 1024) { // KB
        return [NSString stringWithFormat:@"%.0fKB", (double)bytes / 1024];
    } else if (bytes >= 1024 * 1024 && bytes < 1024 * 1024 * 1024) { // MB
        return [NSString stringWithFormat:@"%.1fMB", (double)bytes / (1024 * 1024)];
    } else { // GB
        return [NSString stringWithFormat:@"%.1fGB", (double)bytes / (1024 * 1024 * 1024)];
    }
}

- (UIImageView *)img {
    if (!_img) {
        _img = [[UIImageView alloc] init];
        _img.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _img;
}

- (UIImageView *)playIcon {
    if (!_playIcon) {
        _playIcon = [[UIImageView alloc] initWithImage:VULGetImage(@"播放_icon")];
        _playIcon.hidden = YES;
        _playIcon.contentMode = UIViewContentModeScaleAspectFit;

    }
    return _playIcon;
}

- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:15+1] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.  lineBreakMode = NSLineBreakByTruncatingMiddle;
    }
    return _titleLabel;
}

- (VULLabel *)dateLabel {
    if (!_dateLabel) {
        _dateLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:12+2] Color:UIColorHex(#999999) BgColor:nil];
    }
    return _dateLabel;
}
- (UIImageView *)selectImageV {
    if (!_selectImageV) {
        _selectImageV = [[UIImageView alloc] init];
        _selectImageV.contentMode = UIViewContentModeScaleAspectFit;
        
    }
    return _selectImageV;
}
-(UIView *)timeBg{
    if (!_timeBg) {
        _timeBg = [UIView new];
        _timeBg.backgroundColor = [HEXCOLOR(0x333333) colorWithAlphaComponent:0.4];
        _timeBg.layer.masksToBounds = YES;
        _timeBg.layer.cornerRadius = 2;
    }
    return _timeBg;
}

- (VULLabel *)timeLabel {
    if (!_timeLabel) {
        _timeLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:1 Font:[UIFont yk_pingFangRegular:10] Color:UIColorHex(#ffffff) BgColor:nil];

    }
    return _timeLabel;
}
-(FBTagView *)tagView{
    if (!_tagView) {
        _tagView = [[FBTagView alloc] initWithFrame:CGRectZero];
    }
    return _tagView;
}
- (UIImageView *)topIcon {
    if (!_topIcon) {
        _topIcon = [[UIImageView alloc] init];
        _topIcon.contentMode = UIViewContentModeScaleAspectFit;
        _topIcon.image =VULGetImage(@"icon_top");

    }
    return _topIcon;
}
- (UIImageView *)collectIcon {
    if (!_collectIcon) {
        _collectIcon = [[UIImageView alloc] init];
        _collectIcon.contentMode = UIViewContentModeScaleAspectFit;
        _collectIcon.image =VULGetImage(@"icon_fav");

    }
    return _collectIcon;
}
- (UIImageView *)isShare {
    if (!_isShare) {
        _isShare = [[UIImageView alloc] init];
        _isShare.contentMode = UIViewContentModeScaleAspectFit;
        _isShare.image =VULGetImage(@"icon_home_share");

    }
    return _isShare;
}

- (UIImageView *)isLink {
    if (!_isLink) {
        _isLink = [[UIImageView alloc] init];
        _isLink.contentMode = UIViewContentModeScaleAspectFit;
        _isLink.image =VULGetImage(@"icon_link");

    }
    return _isLink;
}
@end
