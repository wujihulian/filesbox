//
//  FBUploadCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/3.
//

#import "FBUploadCell.h"
#import "VULAiProgressView.h"
@interface FBUploadCell ()

@property (nonatomic, strong) UIImageView *typeImage;
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULAiProgressView *speedLabel;
@property (nonatomic, strong) VULButton *startBtn;
@property (nonatomic, strong) VULButton *deleteBtn;

@end
@implementation FBUploadCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self.contentView addSubview:self.typeImage];
        [self.contentView addSubview:self.titleLabel];
        [self.contentView addSubview:self.speedLabel];
        [self.contentView addSubview:self.startBtn];
        [self.contentView addSubview:self.deleteBtn];

        
        [self.typeImage mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(10));
            make.width.mas_equalTo(fontAuto(45));
            make.height.mas_equalTo(fontAuto(70));
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10));
            make.left.mas_equalTo(self.typeImage.mas_right).offset(fontAuto(5));
            make.width.mas_lessThanOrEqualTo(VULSCREEN_WIDTH-fontAuto(130+40));
        }];
        
        [self.startBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(fontAuto(25));
            make.width.mas_equalTo(fontAuto(40));
            make.right.mas_equalTo(self.deleteBtn.mas_left).offset(-10);
            make.top.mas_equalTo(fontAuto(15));
        }];
        [self.deleteBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(fontAuto(25));
            make.width.mas_equalTo(fontAuto(40));
            make.right.mas_equalTo(-fontAuto(10));
            make.top.mas_equalTo(fontAuto(15));
        }];
        
        
    }
    return self;
    
}
-(void)setModel:(UBUploadModel *)model{
   

    _model = model;
    if (model.isDown) {
        if (model.path) {
            NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(model.path,3,model.fileType)];
            [self.typeImage sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:getLocalImageWithFileType(model.fileType)];

        }else{
            self.typeImage.image =getLocalImageWithFileType(model.fileType);
            
        }
    }else{
        if ([model.fileType isEqualToString:@"jpg"] ) {
            NSURL *fileURL = [NSURL fileURLWithPath:filePath(model.filePath)];
            NSData *fileData =model.data?model.data: [NSData dataWithContentsOfURL:fileURL];
            self.typeImage.image = [UIImage imageWithData:fileData];
        }else if([model.fileType isEqualToString:@"mp4"]){
            self.typeImage.image = [self getVideoPreViewImage:filePath(model.filePath)];

        }else{
            
            self.typeImage.image =getLocalImageWithFileType(model.fileType);

        }
    }
  

    self.titleLabel.text = model.fileName;
    self.speedLabel.titleLabel.text = [NSString stringWithFormat:@"%@/%@(%.2f%%)",[self formattedFileSize:model.speed*model.fileSize],[self formattedFileSize:model.fileSize],model.speed*100];
    [self.speedLabel setProgressWithValue:model.speed];
    [_startBtn setTitle:_model.status == UploadStatusPaused?KLanguage(@"开始") :KLanguage(@"暂停") forState:UIControlStateNormal];
    if (_model.isSucess) {
        self.speedLabel.defultView.hidden = YES;
        self.speedLabel.percentView.hidden = YES;
        self.speedLabel.titleLabel.text = [self formattedFileSize:model.fileSize];
//        _startBtn.hidden = YES;
//        [_startBtn setTitle:KLanguage(@"删除")forState:UIControlStateNormal];
        _startBtn.userInteractionEnabled = NO;
        _startBtn.hidden = YES;
    }else{
        _startBtn.hidden = NO;
        _startBtn.userInteractionEnabled = YES;
        self.speedLabel.defultView.hidden = NO;
        self.speedLabel.percentView.hidden = NO;
    }

    [self.speedLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(5);
        make.left.mas_equalTo(self.typeImage.mas_right).offset(fontAuto(5));
        make.width.mas_equalTo(VULSCREEN_WIDTH-fontAuto(75));
        make.height.mas_equalTo(30);
        make.bottom.mas_equalTo(-fontAuto(10));

    }];

    if (model.isStop && _model.status ==  UploadStatusPaused) {
       self.startBtn.userInteractionEnabled = YES;
    }
}


- (UIImage*)getVideoPreViewImage:(NSString *)videoPath
{
    // 获取第一帧图片
    AVURLAsset *asset = [[AVURLAsset alloc] initWithURL:[NSURL fileURLWithPath:videoPath] options:nil];

       NSParameterAssert(asset);//断言

       AVAssetImageGenerator *assetImageGenerator = [[AVAssetImageGenerator alloc] initWithAsset:asset];

       assetImageGenerator.appliesPreferredTrackTransform = YES;

       assetImageGenerator.apertureMode = AVAssetImageGeneratorApertureModeEncodedPixels;

       NSTimeInterval time = 0.1;

       CGImageRef thumbnailImageRef =NULL;

       CFTimeInterval thumbnailImageTime = time;

       NSError*error =nil;
    thumbnailImageRef = [assetImageGenerator copyCGImageAtTime:CMTimeMake(thumbnailImageTime,60) actualTime:NULL error:&error];

       if( error ) {

           NSLog(@"%@", error );

       }

       if( thumbnailImageRef ) {

           return[[UIImage alloc]initWithCGImage:thumbnailImageRef];

       }
    return nil;
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
-(UIImageView *)typeImage{
    if (!_typeImage) {
        _typeImage = [UIImageView new];
        _typeImage.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _typeImage;
}
-(VULLabel *)titleLabel{
    if (!_titleLabel) {
        _titleLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"Name" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:16] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _titleLabel;
}
- (VULAiProgressView *)speedLabel {
    if (!_speedLabel ){
        _speedLabel = [[VULAiProgressView alloc] initWithFrame:CGRectMake(fontAuto(65), fontAuto(40), VULSCREEN_WIDTH-fontAuto(75), fontAuto(30)) textArray:@""];
    }
    return _speedLabel;
}
-(VULButton *)startBtn{
    if (!_startBtn) {
        _startBtn = [VULButton new];
        [_startBtn setTitle:@"开始" forState:UIControlStateNormal];
        [_startBtn setTitleColor:UIColorHex(#722ed1) forState:UIControlStateNormal];
        [_startBtn addTarget:self action:@selector(clickStartBtn:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _startBtn;
}
-(VULButton *)deleteBtn{
    if (!_deleteBtn) {
        _deleteBtn = [VULButton new];
        [_deleteBtn setTitle:KLanguage(@"删除") forState:UIControlStateNormal];
        [_deleteBtn setTitleColor:UIColorHex(#722ed1) forState:UIControlStateNormal];
        [_deleteBtn addTarget:self action:@selector(clickDeleteBtn) forControlEvents:UIControlEventTouchUpInside];
    }
    return _deleteBtn;
}
-(void)clickDeleteBtn{
    if(self.deleteWithModel){
        self.deleteWithModel(self.model);
    }
}

-(void)clickStartBtn:(VULButton *)sender{
    if([sender.titleLabel.text isEqualToString:KLanguage(@"删除")]){
        if(self.deleteWithModel){
            self.deleteWithModel(self.model);
        }
        
        return;
    }

    if (_model.isDown) {
        if (_model.isOffline) {
            _model.status =  UploadStatusUploading;
            [FBDownloadFileAllManage.sharedManager addDownloadFileWithModel:_model];

        }else{
            FBDownloadFileManage *model1 = FBDownloadFileAllManage.sharedManager.sessionDic[_model.filePath];
            if (_model.status == UploadStatusUploading) {
                [model1 pause:_model];
            }else{
                [model1 resume:_model];
            }
        }
        
    }else{
        if (_model.status == UploadStatusUploading) {
            _model.status =  UploadStatusPaused;
            sender.userInteractionEnabled = NO;
        }else{
            _model.status =  UploadStatusUploading;
            
            [ChunkUploader updateUploadFileAtModel:_model];
        }
    }

   
    [_startBtn setTitle:_model.status == UploadStatusPaused?KLanguage(@"开始") :KLanguage(@"暂停") forState:UIControlStateNormal];

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
