//
//  VULFileGridCell.m
//  UnlimitedBusiness
//
//  Created by zuoyi on 2021/12/14.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULFileGridCell.h"
#import "FBTagView.h"
#import <SDWebImage/UIImageView+WebCache.h>

@interface VULFileGridCell ()

@property (nonatomic,strong) UIImageView *img;
@property (nonatomic,strong) VULLabel *titleLabel;
@property (nonatomic,strong) UIImageView *playIcon;
@property (nonatomic,strong) UIView *timeBg;
@property (nonatomic,strong) VULLabel *timeLabel;
@property (nonatomic,strong) FBTagView *tagView;
@property (nonatomic,strong) UIImageView *topIcon;
@property (nonatomic,strong) UIImageView *collectIcon;
@property (nonatomic,strong) UIImageView *isShare;
@property (nonatomic,strong) UIImageView *isLink;

@end

@implementation VULFileGridCell

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        
        [self.contentView addSubview:self.img];
        [self.contentView addSubview:self.tagView];
        [self.contentView addSubview:self.selectImageV];

        [self.contentView addSubview:self.titleLabel];
        [self.img addSubview:self.collectIcon];
        [self.img addSubview:self.playIcon];
        [self.contentView  addSubview:self.timeBg];
        [self.timeBg addSubview:self.timeLabel];
        [self.contentView addSubview:self.topIcon];
        [self.img addSubview:self.isShare];
        [self.img addSubview:self.isLink];

        
        
        
        [self.img mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(5);
            make.centerX.mas_equalTo(self);
            make.size.mas_equalTo(CGSizeMake(45, 45));
        }];
        [self.topIcon mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(5);
            make.left.mas_equalTo(10);
//            make.size.mas_equalTo(CGSizeMake(16, 16));
        }];
        [self.isLink mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(0);
            make.bottom.mas_equalTo(self.isShare.mas_top).offset(0);
            make.size.mas_equalTo(CGSizeMake(16, 16));
        }];
        [self.isShare mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(0);
//            make.bottom.mas_equalTo(-20);
            make.bottom.mas_equalTo(self.collectIcon.mas_top).offset(0);

//            make.size.mas_equalTo(CGSizeMake(16, 16));
        }];
        [self.collectIcon mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(0);
            make.bottom.mas_equalTo(0);
//            make.size.mas_equalTo(CGSizeMake(16, 16));
        }];
        [self.timeBg mas_makeConstraints:^(MASConstraintMaker *make) {
            make.width.mas_equalTo(34);
            make.right.mas_equalTo(-9);
            make.height.mas_equalTo(14);
            make.top.mas_equalTo(31);
        }];
        [self.tagView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(0);
            make.left.mas_equalTo(10);
            make.height.mas_equalTo(12);
            make.top.mas_equalTo(35);
        }];
        [self.timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.left.right.bottom.mas_equalTo(0);
        }];
        [self.playIcon mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.centerX.mas_equalTo(self.img);
            make.left.mas_equalTo(15);
            make.size.mas_equalTo(CGSizeMake(20, 20));
        }];
        [self.selectImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(5);
            make.right.mas_equalTo(-5);
            make.size.mas_equalTo(CGSizeMake(14, 14));
        }];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerX.mas_equalTo(self);
            make.top.mas_equalTo(self.img.mas_bottom).offset(5);
            make.left.mas_equalTo(5);
            make.right.mas_equalTo(-5);
//            make.height.mas_equalTo(18);
        }];
    }
    return self;
}

- (void)setModel:(VULFileObjectModel *)model {
    _model = model;
    if (!model.isFolder.boolValue&& !model.oexeIsFolder.boolValue) { //是文件
        
        if (model.thumb &&model.thumb.length>0 && isVideoOrMusic(model.fileType)) {
            if ([model.fileType isEqualToString:@"gif"]) {
                NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.downloadUrl];
                url = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];

                [self.img sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];
        
            }else{
                
                NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(model.thumb,3,model.fileType)];
                [self.img sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];
            }
        }else if ( model.path&&model.path.length>0) {
            if ([model.fileType isEqualToString:@"gif"]) {
                NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.downloadUrl];
                url = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
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
        
        
//        self.titleLabel.text = model.name;
        if([model.fileType isEqualToString:@"oexe"]){
            NSArray *arr = [model.name componentsSeparatedByString:@"."];
            self.titleLabel.text = arr[0];

        }else{
            self.titleLabel.text = model.name;

        }
       
//        if (NSStringIsNotEmpty(model.thumb)) {
//            NSString *thumb = model.thumb;
//            if ([model.suffix isEqualToString:@"jpg"] || [model.suffix isEqualToString:@"jpeg"] || [model.suffix isEqualToString:@"png"]) {
//                thumb = [thumb stringByReplacingOccurrencesOfString:@".jpg" withString:@"!small.jpg"];
//                thumb = [thumb stringByReplacingOccurrencesOfString:@".jpeg" withString:@"!small.jpeg"];
//                thumb = [thumb stringByReplacingOccurrencesOfString:@".png" withString:@"!small.png"];
//            }
//            NSString *coverUrl = [NSString stringWithFormat:@"%@%@",kCDNHostUrl, thumb];
//            [self.img sd_setImageWithURL:[NSURL URLWithString:coverUrl]]; //placeholderImage:vull
//        }


    } else {


        self.img.image =VULGetImage(@"icon_folder_main");
//        self.titleLabel.text = model.name;
        if([model.fileType isEqualToString:@"oexe"]){
            NSArray *arr = [model.name componentsSeparatedByString:@"."];
            self.titleLabel.text = arr[0];

        }else{
            self.titleLabel.text = model.name;

        }
    }
//    CGFloat height = [self heightForLabel:self.titleLabel.text font:self.titleLabel.font width:self.width - 10];
//    height = height > 60 ? 63 : height;
//    [self.titleLabel mas_updateConstraints:^(MASConstraintMaker *make) {
//        make.height.mas_equalTo(height);
//    }];
    
    self.img.layer.masksToBounds = NO;
    if (model.labelId.length>0) {
        self.img.image = getImageWithColor(getColorWithGgb(model.style));
        self.img.layer.masksToBounds = YES;
        self.img.layer.cornerRadius = 45/2;
    }

    NSArray *videoArray = @[@"mp4",@"rm",@"rmvb",@"3gp",@"mov",@"m4v",@"wmv",@"asf",@"asx",@"avi",@"dat",@"mkv",@"flv",@"vob",@"webm",@"mpg"];

    if ([videoArray containsObject:model.fileType]) {
        self.playIcon.hidden = NO;
    } else
        self.playIcon.hidden = YES;
    self.collectIcon.hidden = !model.isFav;
    self.timeBg.hidden = YES;
//    [self.collectIcon mas_remakeConstraints:^(MASConstraintMaker *make) {
//        make.right.mas_equalTo(0);
//        make.bottom.mas_equalTo(0);
////        make.size.mas_equalTo(CGSizeMake(16, 16));
//    }];
//    [self.isShare mas_remakeConstraints:^(MASConstraintMaker *make) {
//        make.right.mas_equalTo(0);
//        make.bottom.mas_equalTo(-20);
////        make.size.mas_equalTo(CGSizeMake(16, 16));
//    }];
    NSArray *mp3Array = @[@"mp3",@"wav",@"wma",@"m4a",@"ogg",@"omf",@"amr",@"aa3",@"flac",@"aac",@"cda",@"aif",@"aiff",@"mid",@"ra",@"ape"];

    if (([videoArray containsObject:model.fileType] ||  [mp3Array containsObject:model.fileType])&& self.model.length.integerValue>0) {
        self.timeBg.hidden = NO;
        self.timeLabel.text = [NSString stringWithFormat:@"%.2ld:%.2ld",self.model.length.integerValue / 60, self.model.length.integerValue % 60];
//        self.collectIcon.hidden = YES;
       
        if (model.isFav) {
            [self.collectIcon mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.right.mas_equalTo(0);
                make.bottom.mas_equalTo(-18);
            }];
//            [self.isShare mas_remakeConstraints:^(MASConstraintMaker *make) {
//                make.right.mas_equalTo(0);
//                make.bottom.mas_equalTo(-36);
////                make.size.mas_equalTo(CGSizeMake(16, 16));
//            }];
           
        }else{
            [self.collectIcon mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.right.mas_equalTo(0);
                make.width.height.mas_equalTo(0);
                make.bottom.mas_equalTo(-18);
            }];
//            [self.isShare mas_remakeConstraints:^(MASConstraintMaker *make) {
//               make.right.mas_equalTo(0);
//               make.bottom.mas_equalTo(-18);
////               make.size.mas_equalTo(CGSizeMake(16, 16));
//           }];
            
        }
    }else{
      
        if (model.isFav) {
            [self.collectIcon mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.right.mas_equalTo(0);
                make.bottom.mas_equalTo(0);
                make.size.mas_equalTo(CGSizeMake(16, 16));
            }];
        }else{
            [self.collectIcon mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.right.mas_equalTo(0);
                make.bottom.mas_equalTo(0);
                make.size.mas_equalTo(CGSizeMake(0, 0));
            }];
        }
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
    self.isShare.hidden = !model.isShare;
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
 
   
}

- (CGFloat)heightForLabel:(NSString *)text font:(UIFont *)font width:(CGFloat)width {
    CGRect rect = [text boundingRectWithSize:CGSizeMake(width, 0) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:font} context:nil];
    return rect.size.height;
}

- (UIImageView *)img {
    if (!_img) {
        _img = [[UIImageView alloc] init];
        _img.contentMode = UIViewContentModeScaleAspectFit;
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
- (UIImageView *)selectImageV {
    if (!_selectImageV) {
        _selectImageV = [[UIImageView alloc] init];
        _selectImageV.contentMode = UIViewContentModeScaleAspectFit;
        
    }
    return _selectImageV;
}

-(FBTagView *)tagView{
    if (!_tagView) {
        _tagView = [[FBTagView alloc] initWithFrame:CGRectZero];
    }
    return _tagView;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:1 Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.numberOfLines = 3;
        _titleLabel.  lineBreakMode = NSLineBreakByTruncatingMiddle;

    }
    return _titleLabel;
}

- (UIImageView *)playIcon {
    if (!_playIcon) {
        _playIcon = [[UIImageView alloc] initWithImage:VULGetImage(@"播放_icon")];
        _playIcon.hidden = YES;
        _playIcon.contentMode = UIViewContentModeScaleAspectFit;

    }
    return _playIcon;
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
