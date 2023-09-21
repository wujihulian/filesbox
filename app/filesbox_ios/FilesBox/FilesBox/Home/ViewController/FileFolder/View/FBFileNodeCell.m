//
//  FBFileNodeCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/21.
//

#import "FBFileNodeCell.h"
@interface FBFileNodeCell ()

@property (nonatomic, strong) UIImageView *typeImage;
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULButton *moreBtn;
@property (nonatomic, strong) VULLabel *sizeLabel;

@end
@implementation FBFileNodeCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self.contentView addSubview:self.upOrDownBtn];
        [self.contentView addSubview:self.typeImage];
        [self.contentView addSubview:self.titleLabel];
        [self.contentView addSubview:self.moreBtn];
        [self.contentView addSubview:self.sizeLabel];

        [self.upOrDownBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(10));
            make.height.width.mas_equalTo(16);
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        [self.typeImage mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.upOrDownBtn.mas_right).offset(fontAuto(5));
            make.height.width.mas_equalTo(20);
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.typeImage.mas_right).offset(fontAuto(5));
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
            
        }];
        UITapGestureRecognizer *tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTap:)];
        self.contentView.userInteractionEnabled = YES;
        [self.contentView addGestureRecognizer:tapGestureRecognizer];

    }
    return self;
    
}

- (void)handleTap:(UITapGestureRecognizer *)tapGestureRecognizer {
    CGPoint tapLocation = [tapGestureRecognizer locationInView:self.contentView];
 
        if (tapLocation.x < self.typeImage.left) { // 判断点击位置是否在单元格左侧 20.0 点内
            // 在单元格左侧区域内进行了点击
    //         执行您想要执行的操作
            if (self.upOrDownBtn.imageView.image) {
            if ([self.delegate respondsToSelector:@selector(fileNodeCell:didTapExpandButton:)]) {
                [self.delegate fileNodeCell:self didTapExpandButton:self.upOrDownBtn];
            }}
        }else{
            if ([self.delegate respondsToSelector:@selector(selectCell:)]) {
                [self.delegate selectCell:self];
            }
        }

  
}
-(void)setZipModel:(VULFileZIPObjectModel *)node{
    _zipModel = node;
    self.titleLabel.text = KLanguage(node.originName.length>0?node.originName:node.fileName) ;
    self.indentationLevel = node.level; // 缩进级别
    self.indentationWidth = 20; // 缩进宽度
    [self.titleLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.typeImage.mas_right).offset(fontAuto(5));
        make.centerY.mas_equalTo(self.contentView.mas_centerY);
        make.right.mas_equalTo(-fontAuto(112));

    }];
    [self.sizeLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(fontAuto(50));
        make.centerY.mas_equalTo(self.contentView.mas_centerY);
        make.right.mas_equalTo(-fontAuto(52));

    }];
    [self.moreBtn  mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(-fontAuto(12));
        make.width.height.mas_equalTo(fontAuto(30));
        make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
    }];
    [self.upOrDownBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(node.level*20);
        make.height.width.mas_equalTo(20);
        make.centerY.mas_equalTo(self.contentView.mas_centerY);
    }];
//    icon_folder_main
    NSArray *iconArr = [node.originName componentsSeparatedByString:@"."];
//    self.moreBtn.hidden = node.level == 0;
    self.typeImage.image =getLocalImageWithFileType([iconArr lastObject]);
    if(node.directory.boolValue && node.level >0){
        self.typeImage.image = VULGetImage(@"icon_folder_main");
    }
    NSString *fileSize = node.size ? [self stringWithbytes:node.size.intValue] : @"";
    self.sizeLabel.text = fileSize;

    self.typeImage.hidden = NO;
    [self.typeImage mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.upOrDownBtn.mas_right).offset(fontAuto(5));
        make.height.width.mas_equalTo(20);
        make.centerY.mas_equalTo(self.contentView.mas_centerY);
    }];
   

    [self.upOrDownBtn setImage:node.childList.count > 0 ? (node.isExpanded ? [UIImage imageNamed:@"icon_open"] : [UIImage imageNamed:@"icon_close"]) :nil forState:UIControlStateNormal];

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
-(void)setModel:(VULFileObjectModel *)node{
    _model = node;
    self.moreBtn.hidden = YES;
    self.sizeLabel.hidden = YES;
    self.titleLabel.text = KLanguage(node.name.length>0?node.name:node.groupName) ;
    self.indentationLevel = node.level; // 缩进级别
    self.indentationWidth = 20; // 缩进宽度
 
    if (node.level == 0) {
        [self.typeImage mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.upOrDownBtn.mas_right).offset(fontAuto(0));
            make.height.width.mas_equalTo(0);
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        [self.upOrDownBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(10));
            make.height.width.mas_equalTo(20);
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        self.typeImage.hidden = YES;
    }else{
        [self.upOrDownBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(node.level*20);
            make.height.width.mas_equalTo(20);
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        self.typeImage.hidden = NO;
        NSString *imageName = [NSString stringWithFormat:@"icon_left_%@",_model.icon];
        self.typeImage.image = VULGetImage(imageName);
        [self.typeImage mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.upOrDownBtn.mas_right).offset(fontAuto(5));
            make.height.width.mas_equalTo(20);
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        if ([_model.icon isEqualToString:@"folder"] || [_model.icon isEqualToString:@"shareToMe"] || _model.infoTypeID.intValue>0) {
           self.typeImage.image = VULGetImage(@"icon_folder_main");
        }

    
        
        
    }
    self.typeImage.layer.masksToBounds = NO;
    if (_model.labelId.length>0) {
        self.typeImage.image = getImageWithColor(getColorWithGgb(_model.style));
        self.typeImage.layer.masksToBounds = YES;
        self.typeImage.layer.cornerRadius = 10;
    }

  
    if ([_model.icon isEqualToString:@"folder"]) {
        [self.upOrDownBtn setImage:node.hasFolder.integerValue > 0 ? (node.isExpanded ? [UIImage imageNamed:@"icon_open"] : [UIImage imageNamed:@"icon_close"]) :(node.isChildren?[UIImage imageNamed:@"icon_close"]:nil) forState:UIControlStateNormal];
    }else{
        [self.upOrDownBtn setImage:node.children.folderList.count > 0 ? (node.isExpanded ? [UIImage imageNamed:@"icon_open"] : [UIImage imageNamed:@"icon_close"]) :(node.isChildren?[UIImage imageNamed:@"icon_close"]:nil) forState:UIControlStateNormal];
    }
    if ([_model.icon isEqualToString:@"fav"] || [_model.icon isEqualToString:@"info"] ) {
        [self.upOrDownBtn setImage:nil forState:UIControlStateNormal];
    }
    if ([_model.icon isEqualToString:@"shareToMe"]) {
        [self.upOrDownBtn setImage:nil forState:UIControlStateNormal];
    }
}
-(VULButton *)upOrDownBtn{
    if (!_upOrDownBtn) {
        _upOrDownBtn = [VULButton new];
        [_upOrDownBtn addTarget:self action:@selector(arrowButtonClicked:) forControlEvents:UIControlEventTouchUpInside]; // 监听箭头点击事件

    }
    return _upOrDownBtn;
}
- (void)arrowButtonClicked:(UIButton *)button {
    if ([self.delegate respondsToSelector:@selector(fileNodeCell:didTapExpandButton:)]) {
        [self.delegate fileNodeCell:self didTapExpandButton:button];
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

-(UIImageView *)typeImage{
    if (!_typeImage) {
        _typeImage = [UIImageView new];
        _typeImage.contentMode = UIViewContentModeScaleAspectFit;

    }
    return _typeImage;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:16] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.numberOfLines = 1;
        _titleLabel.  lineBreakMode = NSLineBreakByTruncatingMiddle;

    }
    return _titleLabel;
}
- (VULLabel *)sizeLabel {
    if (!_sizeLabel) {
        _sizeLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:14] Color:UIColorHex(#333333) BgColor:nil];
        _sizeLabel.numberOfLines = 1;
        _sizeLabel.  lineBreakMode = NSLineBreakByTruncatingMiddle;

    }
    return _sizeLabel;
}
-(VULButton *)moreBtn{
    if (!_moreBtn) {
        _moreBtn = [VULButton new];
        [_moreBtn setImage:VULGetImage(@"icon_more_version") forState:UIControlStateNormal];
        [_moreBtn addTarget:self action:@selector(clickMoreSearchBtn) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return _moreBtn;
}
-(void)clickMoreSearchBtn{
    if (self.clickMore) {
        self.clickMore(self.zipModel);
    }
}
@end
