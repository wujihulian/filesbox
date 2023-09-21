//
//  FBTagCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/31.
//

#import "FBTagCell.h"

@implementation FBTagCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.contentView.backgroundColor = [UIColor whiteColor];
        [self.contentView  addSubview:self.titleLabel];
        [self.contentView  addSubview:self.leftImageV];
        [self.contentView  addSubview:self.bossView];
        [self.titleLabel  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10));
            make.left.mas_equalTo(fontAuto(10));
            make.height.mas_equalTo(fontAuto(30));

        }];
        [self.leftImageV  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(10));
            make.centerY.mas_equalTo(self.titleLabel.mas_centerY);
            make.width.mas_equalTo(fontAuto(20));
        }];
   
        [self.bossView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.titleLabel.mas_bottom);
            make.left.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(10));
            make.height.mas_equalTo(_bossView.tagCollectionView.contentSize.height);
            make.bottom.mas_equalTo(-fontAuto(10));
        }];
        self.leftImageV.userInteractionEnabled = YES;
        UITapGestureRecognizer *top = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(gotoTag)];
        [self.leftImageV addGestureRecognizer:top];
    }
    
    return self;
    
}
-(void)gotoTag{
    if (self.gotoTagManageMent) {
        self.gotoTagManageMent();
    }
}
-(void)setModel:(VULFileObjectModel *)model{
    [self.bossView removeAllTags];
    
    for (NSDictionary *dic in model.tagList ) {
        FBTagModel *model1 = [FBTagModel modelWithDictionary:dic];
        [self.bossView addTag:model1];
    }
    [self.bossView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.titleLabel.mas_bottom);
        make.left.mas_equalTo(fontAuto(0));
        make.right.mas_equalTo(-fontAuto(0));
        make.height.mas_equalTo(_bossView.tagCollectionView.contentSize.height);
    }];
}
- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
-(UIImageView *)leftImageV{
    if (!_leftImageV) {
        _leftImageV = [UIImageView new];
        _leftImageV.image = VULGetImage(@"icon_operation_tag");
        _leftImageV.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _leftImageV;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"标签:") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#999999) BgColor:nil];
        _titleLabel.numberOfLines = 1;
    }
    return _titleLabel;
}
-(FBFileTagView *)bossView{
    if (!_bossView) {
        _bossView = [[FBFileTagView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth-fontAuto(12) , 0)];
    
    }
    return _bossView;
}
@end
