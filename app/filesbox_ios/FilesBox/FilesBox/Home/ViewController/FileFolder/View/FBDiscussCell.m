//
//  FBDiscussCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/4/13.
//

#import "FBDiscussCell.h"
@interface FBDiscussCell ()
@property (nonatomic, strong) UIImageView *leftImage;
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *timeLabel;
@property (nonatomic, strong) VULLabel *detailLabel;
@property (nonatomic, strong) UIImageView *imageV;
@property (nonatomic, strong) UIView *bgView;



@end
@implementation FBDiscussCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.contentView.backgroundColor = [UIColor whiteColor];
        [self.contentView  addSubview:self.bgView];
        [self.contentView addSubview:self.lineV];
        [self.contentView addSubview:self.leftImage];
        [self.bgView  addSubview:self.imageV];
        [self.bgView  addSubview:self.titleLabel];
        [self.bgView  addSubview:self.timeLabel];
        [self.bgView  addSubview:self.detailLabel];

        [self.leftImage mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(5));
            make.top.mas_equalTo(fontAuto(2));
            make.width.height.mas_equalTo(fontAuto(20));
        }];
        [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(5));
            make.top.mas_equalTo(fontAuto(2));
            make.bottom.right.mas_equalTo(fontAuto(-10));
        }];
        [self.lineV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(14));
            make.top.mas_equalTo(fontAuto(5));
            make.width.mas_equalTo(1);
            make.bottom.mas_equalTo(fontAuto(-0));
        }];
        [self.imageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(5));
            make.top.mas_equalTo(fontAuto(5));
            make.width.height.mas_equalTo(fontAuto(20));
        }];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.imageV.mas_right).offset(fontAuto(5));
            make.centerY.mas_equalTo(self.imageV.mas_centerY);
            make.right.mas_equalTo(self.timeLabel.mas_left).offset(-fontAuto(10));
        }];
        [self.timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(5));
            make.centerY.mas_equalTo(self.imageV.mas_centerY);
            make.width.mas_greaterThanOrEqualTo(fontAuto(30));
        }];
        [self.detailLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(5));
            make.left.mas_equalTo(self.imageV.mas_right).offset(fontAuto(5));
            make.top.mas_equalTo(self.imageV.mas_bottom).offset(fontAuto(5));
            make.bottom.mas_equalTo(-fontAuto(5));
        }];
    }
    return self;
}

-(void)setModel:(VULFileObjectModel *)model{
    _model = model;
    self.titleLabel.text = model.nickname;
    self.timeLabel.text =[NSDate updateTimeForRow:getTimeWithTime(model.modifyTime?model.modifyTime:model.createTime)];
    [self.imageV sd_setImageWithURL:[NSURL URLWithString:resultsUrl(model.avatar) ] placeholderImage:VULGetImage(@"placeholder_face")];
    self.detailLabel.textColor = UIColorHex(#999999);
    NSDictionary *dic = turnStringToDictionary(model.desc);

    if ([model.type isEqualToString:@"create"]) {
        self.leftImage.image = VULGetImage(@"icon_discuss_add");
        if (model.isFolder.boolValue) {
            if (model.isChildren) {
                NSString *allStr = [NSString stringWithFormat:@"%@ %@",KLanguage(@"新建了文件夹"),dic[@"name"]];
                self.detailLabel.textColor = BtnColor;
                self.detailLabel.attributedText = setRichNumberWithLabelOrFontOrStr(allStr,UIColorHex(#999999),[UIFont yk_pingFangRegular:14] ,KLanguage(@"新建了文件夹"));

            }else{
                NSString * createType = dic[@"createType"];
                if ([createType isEqualToString:@"copy"]){
                    self.detailLabel.text = KLanguage(@"粘贴创建了该文件夹") ;
                }else{
                    self.detailLabel.text = KLanguage(@"新建了该文件夹") ;
                }
            }
        }else{
            if (model.isChildren) {
                NSString *allStr = [NSString stringWithFormat:@"%@ %@",KLanguage(@"上传了文件"),model.name];
                self.detailLabel.textColor = BtnColor;
                self.detailLabel.attributedText = setRichNumberWithLabelOrFontOrStr(allStr,UIColorHex(#999999),[UIFont yk_pingFangRegular:14] ,KLanguage(@"上传了文件"));

            }else{
                NSString * createType = dic[@"createType"];
                
                if ([createType isEqualToString:@"uploadNew"]) {
                    self.leftImage.image = VULGetImage(@"icon_upload_version");
                    self.detailLabel.text = KLanguage(@"该文件上传了新版本") ;
                }else  if ([createType isEqualToString:@"upload"]){
                    self.leftImage.image = VULGetImage(@"icon_upload_version");
                    self.detailLabel.text = KLanguage(@"上传了该文件") ;
                }else  if ([createType isEqualToString:@"copy"]){
                    self.detailLabel.text = KLanguage(@"粘贴创建了该文件") ;
                }else{
                    self.detailLabel.text = KLanguage(@"新建了该文件") ;
                }
                

            }
        }
        
    }else if([model.type isEqualToString:@"rename"]){

        self.leftImage.image = VULGetImage(@"icon_edit");

        self.detailLabel.text = [NSString stringWithFormat:@"%@ %@ \n%@->%@",KLanguage(@"重命名了"),model.name,dic[@"from"],dic[@"to"]];
        
    }else if([model.type isEqualToString:@"edit"]){
        
        self.leftImage.image = VULGetImage(@"icon_edit");

        self.detailLabel.text = KLanguage(@"编辑了该文件");
        
    }else if([model.type isEqualToString:@"share"]){
        NSString *name = dic[@"content"];
        if ([name isEqualToString:@"shareLinkAdd"]) {
            self.detailLabel.text =KLanguage(@"将该文档创建了外链分享");
        }else if ([name isEqualToString:@"shareLinkRemove"]) {
            self.detailLabel.text =KLanguage(@"关闭了该文件的外链分享");
        }else if ([name isEqualToString:@"shareLinkEdit"]) {
            self.detailLabel.text =KLanguage(@"编辑了该文件的分享");
        }else{
            self.detailLabel.text = model.type ;
        }
        self.leftImage.image = VULGetImage(@"icon_discuss_share");

        
    }else if([model.type isEqualToString:@"recycle"]){
        NSString *content = dic[@"content"];
        if ([content isEqualToString:@"restore"]) {
            if (model.isFolder.boolValue) {
                self.detailLabel.text =KLanguage(@"将该文件夹从回收站还原");
            }else{
                self.detailLabel.text =KLanguage(@"将该文件从回收站还原");
            }
        }else{
            if (model.isFolder.boolValue) {
                self.detailLabel.text =KLanguage(@"将该文件夹移到了回收站");
            }else{
                self.detailLabel.text =KLanguage(@"将该文件移到了回收站");
            }
        }
        self.leftImage.image = VULGetImage(@"icon_discuss_delete");

    }else if([model.type isEqualToString:@"remove"]){
        NSString *content = dic[@"content"];
        self.leftImage.image = VULGetImage(@"icon_discuss_delete");
        self.detailLabel.text = [NSString stringWithFormat:@"%@\n%@",KLanguage(@"在此处删除了"),content] ;

    }else{
        self.leftImage.image = VULGetImage(@"icon_discuss_add");
        self.detailLabel.text = model.type ;

    }
    
}


- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
-(UIView *)bgView{
    if (!_bgView) {
        _bgView = [UIView new];
        _bgView.backgroundColor = HEXCOLOR(0xf5efff);
        _bgView.layer.masksToBounds = YES;
        _bgView.layer.cornerRadius = 5;
        _bgView.layer.borderWidth = 1;
        _bgView.layer.borderColor = HEXCOLOR(0xc7b7de).CGColor;
    }
    return _bgView;
}
-(UIView *)lineV{
    if (!_lineV) {
        _lineV = [UIView new];
        _lineV.backgroundColor = HEXCOLOR(0xececec);
    }
    return _lineV;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:14] Color:UIColorHex(#999999) BgColor:nil];
        _titleLabel.numberOfLines = 1;
    }
    return _titleLabel;
}
- (VULLabel *)detailLabel {
    if (!_detailLabel) {
        _detailLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:14] Color:UIColorHex(#999999) BgColor:nil];
        _titleLabel.numberOfLines = 2;
    }
    return _detailLabel;
}
- (VULLabel *)timeLabel {
    if (!_timeLabel) {
        _timeLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentRight Font:[UIFont yk_pingFangRegular:14] Color:UIColorHex(#999999) BgColor:nil];
        _timeLabel.numberOfLines = 2;
    }
    return _timeLabel;
}
-(UIImageView *)imageV{
    if (!_imageV) {
        _imageV = [UIImageView new];
        _imageV.layer.masksToBounds = YES;
        _imageV.layer.cornerRadius = fontAuto(10);

    }
    return _imageV;
}
-(UIImageView *)leftImage{
    if (!_leftImage) {
        _leftImage = [UIImageView new];
        _leftImage.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _leftImage;
}

@end
