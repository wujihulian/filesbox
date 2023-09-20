//
//  FBAddFileView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/24.
//

#import "FBAddFileView.h"
@implementation FBAddFileCell

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
       
        [self.contentView addSubview:self.label];
        [self.contentView addSubview:self.iconImageV];
        [self.iconImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.width.height.mas_equalTo(50);
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
            make.centerX.mas_equalTo(self.contentView.mas_centerX);
        }];
        [self.label mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.iconImageV.mas_bottom).offset(0);
            make.height.mas_equalTo(30);
            make.centerX.mas_equalTo(self.contentView.mas_centerX);
        }];
    }
    return self;
}



- (VULLabel *)label {
    if (!_label) {
        _label = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _label;
}

- (UIImageView *)iconImageV {
    if (!_iconImageV) {
        _iconImageV = [UIImageView new];
        _iconImageV.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _iconImageV;
}
@end
@interface FBAddFileView ()<UICollectionViewDelegate,UICollectionViewDataSource>

@property (nonatomic, strong) VULButton *cancelBtn;
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) UIView *lineView;
@property (nonatomic,strong) UICollectionView *collectionView;
@property (nonatomic, strong) NSArray *titleArr;
@property (nonatomic, strong) NSArray *iconArr;
@end
@implementation FBAddFileView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
       
//        self.layer.shadowColor = [UIColor blackColor].CGColor;
//        self.layer.shadowOpacity = 0.5;
//        self.layer.shadowRadius = 10;
//        self.layer.shadowOffset = CGSizeMake(0, 0);
        self.backgroundColor =[UIColor whiteColor];
        [self setView];
    }
    return self;
}
-(void)setView{
    [self addSubview:self.collectionView];
//    [self addSubview:self.titleLabel];
    [self addSubview:self.cancelBtn];
    [self addSubview:self.lineView];
//    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.right.left.top.mas_offset(0);
//        make.height.mas_equalTo(50);
//    }];
    [self.collectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.left.mas_offset(0);
        make.top.mas_equalTo(10);
        make.height.mas_equalTo(100+20);
    }];
    [self.lineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.left.mas_offset(0);
        make.top.mas_equalTo(self.collectionView.mas_bottom);
        make.height.mas_equalTo(10);
    }];
    [self.cancelBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.left.mas_offset(0);
        make.top.mas_equalTo(self.lineView.mas_bottom);
        make.height.mas_equalTo(50+K_BottomBar_Height);
    }];
    _titleArr = @[KLanguage(@"拍摄"),KLanguage(@"图片"),KLanguage(@"本地文件"),KLanguage(@"剪贴板"),KLanguage(@"文件夹")];
    _iconArr = @[VULGetImage(@"file_add_movie"),VULGetImage(@"file_add_image"),VULGetImage(@"file_add_file"),VULGetImage(@"file_add_copy"),VULGetImage(@"file_add_folder")];
    [self.collectionView reloadData];

}
#pragma mark -
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {

    return CGSizeMake(VULSCREEN_WIDTH/5, 80);
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section {//{top, left, bottom, right};
    return UIEdgeInsetsMake(0, 0, 0, 0);
}

//item 列间距(纵)
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section {
    return fontAuto(0);
}

//item 行间距(横)
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section {
    return fontAuto(0);
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return  1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.titleArr.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    FBAddFileCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"FBAddFileCell" forIndexPath:indexPath];
    cell.label.text =_titleArr[indexPath.row];
    UIImage *img = _iconArr[indexPath.row];;

    cell.iconImageV.image = img;
    
    if (indexPath.row == 3) {
        if (isPermissionWithModel(KLanguage(@"文件夹"), @[self.parentModel?self.parentModel:[VULFileObjectModel new]])) {
            cell.label.textColor = HEXCOLOR(0x333333);
            cell.userInteractionEnabled = YES;
        }else{
            cell.label.textColor = HEXCOLOR(0x999999);
            cell.userInteractionEnabled = NO;
     
        }
    }else{
        if (isPermissionWithModel(KLanguage(@"上传"), @[self.parentModel?self.parentModel:[VULFileObjectModel new]])) {
            cell.label.textColor = HEXCOLOR(0x333333);
            cell.userInteractionEnabled = YES;

        }else{
            cell.label.textColor = HEXCOLOR(0x999999);
            cell.userInteractionEnabled = NO;
        }
        
    }
   

    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView willDisplayCell:(UICollectionViewCell *)cell forItemAtIndexPath:(NSIndexPath *)indexPath {

}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    NSString *title =    _titleArr[indexPath.row];
    if (self.addFileWithTitle) {
        self.addFileWithTitle(title);
    }
  

}

#pragma mark -setting

- (UICollectionView *)collectionView {
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    if (!_collectionView) {
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, 40) collectionViewLayout:layout];
        //_collectionView.showsHorizontalScrollIndicator = NO;
        //_collectionView.alwaysBounceVertical = YES;
        _collectionView.showsHorizontalScrollIndicator = NO;
        _collectionView.alwaysBounceVertical = NO;
        //_collectionView.alwaysBounceHorizontal = NO;
        if (@available(iOS 11.0, *)) {
            _collectionView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
        }
        _collectionView.backgroundColor = HEXCOLOR(0xffffff);
        [_collectionView registerClass:[FBAddFileCell class] forCellWithReuseIdentifier:@"FBAddFileCell"];
        _collectionView.delegate = self;
        _collectionView.dataSource = self;
    }
    return _collectionView;
}
-(UIView *)lineView{
    if (!_lineView) {
        _lineView = [UIView new];
        _lineView.backgroundColor = HEXCOLOR(0xececec);
    }
    return _lineView;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"新增" TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangSemibold:16] Color:UIColorHex(0x333333) BgColor:nil];
    }
    return _titleLabel;
}
-(VULButton *)cancelBtn{
    if (!_cancelBtn) {
        _cancelBtn = [VULButton new];
        [_cancelBtn setTitle:KLanguage(@"取消") forState:UIControlStateNormal];
        [_cancelBtn setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
        _cancelBtn.titleLabel.font = [UIFont yk_pingFangRegular:16];
        [_cancelBtn addTarget:self action:@selector(clickCancelBtn) forControlEvents:UIControlEventTouchUpInside];

    }
    return _cancelBtn;
}
-(void)clickCancelBtn{
    if (self.dismisssView) {
        self.dismisssView();
    }
}
@end
