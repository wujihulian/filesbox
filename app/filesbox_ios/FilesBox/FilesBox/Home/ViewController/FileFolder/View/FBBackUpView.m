//
//  FBBackUpView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/5/23.
//

#import "FBBackUpView.h"
@interface FBBackUpView ()

@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *detialLabel;
@property (nonatomic, strong) VULLabel *speedLabel;
@property (nonatomic, strong) UIImageView *leftImage;
@property (nonatomic, strong) UIImageView *rightImageV;



@end
@implementation FBBackUpView

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
        self.layer.masksToBounds = YES;
        self.layer.cornerRadius = 5;
        //阴影颜色
        self.layer.shadowColor = UIColor.lightGrayColor.CGColor;
        //阴影偏移
        self.layer.shadowOffset = CGSizeMake(0,0.5);
        self.layer.shadowOpacity = 0.6;
        self.layer.shadowRadius = 1.0;
        self.clipsToBounds = NO;

        [self setView];
    }
    return self;
    
}
-(void)setView{
    [self addSubview:self.leftImage];
    [self addSubview:self.titleLabel];
    [self addSubview:self.speedLabel];
    [self addSubview:self.detialLabel];
    [self addSubview:self.rightImageV];
    [self addSubview:self.switchh];

    [self.leftImage mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_offset(fontAuto(12));
        make.width.mas_equalTo(fontAuto(40));
        make.centerY.mas_equalTo(self.mas_centerY);
    }];
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_offset(fontAuto(12));
            make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
        make.width.mas_greaterThanOrEqualTo(fontAuto(40));
    }];
    [self.detialLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(fontAuto(10));
        make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
        make.width.mas_greaterThanOrEqualTo(fontAuto(40));
    }];
    [self.speedLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.detialLabel.mas_bottom).offset(fontAuto(10));
        make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
        make.width.mas_greaterThanOrEqualTo(fontAuto(40));
    }];
    [self.rightImageV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_offset(-fontAuto(10));
        make.width.mas_equalTo(fontAuto(15));
        make.centerY.mas_equalTo(self.mas_centerY);
    }];
    [self.switchh mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(self.rightImageV.mas_left).offset(-fontAuto(5));
        make.centerY.mas_equalTo(self.mas_centerY);
    }];
}

-(void)setModel:(UBUploadModel *)model
{
    if(self.switchh.on){
        if([ChunkUploader sharedManager].unuploadedPhotos.count == 0){
            self.titleLabel.text = KLanguage(@"备份完成");
            NSMutableArray *arr = [NSMutableArray arrayWithArray:[NSArray bg_arrayWithName:@"uploadedIdentifiers"]];
            NSMutableArray *arr1= [NSMutableArray array];
            for(NSDictionary *dic in arr){
                NSString *isVideo = [NSString stringWithFormat:@"%@",dic[@"isVideo"]];
                if(isVideo.boolValue){
                    [arr1 addObject:dic];
                }
            }
            self.detialLabel.text = [NSString stringWithFormat:@"已完成%d张照片和%d个视频",arr.count-arr1.count,arr1.count];
            NSString *appLanguage = [[NSUserDefaults standardUserDefaults] objectForKey:@"appLanguage"];
            CGFloat width = 160;
            if ([appLanguage isEqualToString:@"en"]) {
                self.detialLabel.text = [NSString stringWithFormat:@"%ld photos and %ld  videos is completed",arr.count-arr1.count,arr1.count];
                
            }
            self.speedLabel.text = @"";
            [self.detialLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(fontAuto(10));
                make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
                make.width.mas_greaterThanOrEqualTo(fontAuto(40));
            }];
            [self.speedLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.top.mas_equalTo(self.detialLabel.mas_bottom).offset(fontAuto(10));
                make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
                make.width.mas_greaterThanOrEqualTo(fontAuto(40));
            }];
            return;
        }else{
            
            if(!model.isOpen){
                self.titleLabel.text = KLanguage(@"正在备份");
                self.detialLabel.text = [NSString stringWithFormat:@"%d照片和视频正在等待上传",[ChunkUploader sharedManager].unuploadedPhotos.count];
                self.speedLabel.text = @"";
                [self.detialLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(fontAuto(10));
                    make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
                    make.width.mas_greaterThanOrEqualTo(fontAuto(40));
                }];
                [self.speedLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.top.mas_equalTo(self.detialLabel.mas_bottom).offset(fontAuto(10));
                    make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
                    make.width.mas_greaterThanOrEqualTo(fontAuto(40));
                }];
            }else{
                self.titleLabel.text = KLanguage(@"正在备份");
                self.detialLabel.text = [NSString stringWithFormat:@"%d%@",[ChunkUploader sharedManager].unuploadedPhotos.count,KLanguage(@"照片和视频正在等待上传")];
                
                self.speedLabel.text = [NSString stringWithFormat:@"%@/%@(%.2f%%)",[self formattedFileSize:model.speed*model.fileSize],[self formattedFileSize:model.fileSize],model.speed*100];
                [self.detialLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(fontAuto(0));
                    make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
                    make.width.mas_greaterThanOrEqualTo(fontAuto(40));
                }];
                [self.speedLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.top.mas_equalTo(self.detialLabel.mas_bottom).offset(fontAuto(0));
                    make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
                    make.width.mas_greaterThanOrEqualTo(fontAuto(40));
                }];
            }
            
            
         
            
        }}else{
        self.titleLabel.text = KLanguage(@"未开启自动备份");
        self.detialLabel.text = KLanguage(@"请到[我的-自动备份]中开启");
        self.speedLabel.text = @"";
        [self.detialLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(fontAuto(10));
            make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
            make.width.mas_greaterThanOrEqualTo(fontAuto(40));
        }];
        [self.speedLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.detialLabel.mas_bottom).offset(fontAuto(10));
            make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
            make.width.mas_greaterThanOrEqualTo(fontAuto(40));
        }];
    }
}
- (NSString *)formattedFileSize:(long long)bytes {
    CGFloat convertedValue;
    NSString *sizeSuffix;
    
    if (bytes >= 1024*1024*1024) {
        convertedValue = bytes / (1024.0 * 1024.0 * 1024.0);
        sizeSuffix = @"GB";
    } else {
        convertedValue = bytes / (1024.0 * 1024.0);
        sizeSuffix = @"MB";
    }
    
    NSString *result = [NSString stringWithFormat:@"%.2f %@", convertedValue, sizeSuffix];
    return result;
}
-(UIImageView *)leftImage{
    if(!_leftImage){
        _leftImage = [UIImageView new];
        _leftImage.image = VULGetImage(@"backUp_left");
        _leftImage.contentMode = UIViewContentModeScaleAspectFit;

    }
    return _leftImage;
}
-(UIImageView *)rightImageV{
    if(!_rightImageV){
        _rightImageV = [UIImageView new];
        _rightImageV.image = VULGetImage(@"icon_right");
        _rightImageV.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _rightImageV;
}

- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"未开启自动备份") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:16] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.numberOfLines = 1;
    }
    return _titleLabel;
}
- (VULLabel *)detialLabel {
    if (!_detialLabel) {
        _detialLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"请到[我的-自动备份]中开启")  TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:14] Color:UIColorHex(#999999) BgColor:nil];
        _detialLabel.numberOfLines = 1;
    }
    return _detialLabel;
}
- (VULLabel *)speedLabel {
    if (!_speedLabel) {
        _speedLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:14] Color:UIColorHex(#999999) BgColor:nil];
        _speedLabel.numberOfLines = 1;
    }
    return _speedLabel;
}
- (UISwitch *)switchh {
    if (!_switchh) {
        _switchh = [UISwitch new];
        _switchh.onTintColor = BtnColor;
    }
    return _switchh;
}
@end
