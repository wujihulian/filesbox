//
//  VULFileTierView.m
//  UnlimitedBusiness
//
//  Created by zuoyi on 2021/12/14.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULFileTierView.h"

@interface VULFileTierCell ()

@property (nonatomic,strong) VULLabel *label;
@property (nonatomic,assign) NSInteger row;

@property (nonatomic,strong) UIImageView *rightArrow;
@property (nonatomic,copy) void (^ selectTitleFromRow)(NSInteger row);
@property (nonatomic,copy) void (^ selectImageFromRow)(NSInteger row,UIImageView *rightArrow);

@end

@implementation VULFileTierCell

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
       
        [self.contentView addSubview:self.label];
        [self.contentView addSubview:self.rightArrow];
        self.label.userInteractionEnabled = YES;
        self.rightArrow.userInteractionEnabled = YES;

        UITapGestureRecognizer *sender = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickLabel)];
        [self.label addGestureRecognizer:sender];
        UITapGestureRecognizer *sender1 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickRightArrow)];
        [self.rightArrow addGestureRecognizer:sender1];
    }
    return self;
}
-(void)clickLabel{
    if (self.selectTitleFromRow) {
        self.selectTitleFromRow(self.row);
    }
    
}
-(void)clickRightArrow{
    if (self.selectImageFromRow) {
        self.selectImageFromRow(self.row,self.rightArrow);
    }
}
- (void)setData:(NSString *)string isLast:(BOOL)isLast {
    self.label.textColor = isLast ? UIColorHex(#333333) : UIColorHex(#999999);
    self.rightArrow.hidden = isLast;
    self.label.text = string;
    CGFloat width = [string sizeWithAttributes:@{NSFontAttributeName:[UIFont yk_pingFangRegular:15]}].width;
    self.label.frame = CGRectMake(0, 0, width, 35);
    self.rightArrow.frame = CGRectMake(CGRectGetMaxX(self.label.frame) , 12, 20, 11);
}

- (VULLabel *)label {
    if (!_label) {
        _label = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _label;
}

- (UIImageView *)rightArrow {
    if (!_rightArrow) {
        _rightArrow = [[UIImageView alloc] initWithImage:VULGetImage(@"icon_right_arrow")];
        _rightArrow.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _rightArrow;
}

@end

@interface VULFileTierView ()<UICollectionViewDelegate,UICollectionViewDataSource>

@end

@implementation VULFileTierView

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
       
        [self addSubview:self.collectionView];
    }
    return self;
}

- (void)setDataArray:(NSMutableArray *)dataArray {
    _dataArray = dataArray;
    [self.collectionView reloadData];
}

#pragma mark -
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    NSString *string = self.dataArray[indexPath.row];
    CGFloat width = [string sizeWithAttributes:@{NSFontAttributeName:[UIFont yk_pingFangRegular:15]}].width;//rect.size.width;
    return CGSizeMake(width + 20, 35);
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
    return self.dataArray.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    VULFileTierCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"VULFileTierCell" forIndexPath:indexPath];
    NSString *string = self.dataArray[indexPath.row];
    BOOL ret = self.dataArray.count - 1 == indexPath.row ? YES : NO;
    cell.row = indexPath.row;
    [cell setData:string isLast:ret];
    cell.selectImageFromRow = ^(NSInteger row,UIImageView *rightArrow) {
   
        if (self.selectImageWithRow) {
            self.selectImageWithRow(row,rightArrow);
        }
    };
    cell.selectTitleFromRow = ^(NSInteger row) {
        self.dataArray = [self.dataArray subarrayWithRange:NSMakeRange(0, row + 1)];
        [self.collectionView reloadData];
        if (self.fileTierViewBlock) {
            self.fileTierViewBlock(row);
        }
        
    };
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView willDisplayCell:(UICollectionViewCell *)cell forItemAtIndexPath:(NSIndexPath *)indexPath {

}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {

}

- (UICollectionView *)collectionView {
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
    if (!_collectionView) {
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(15, 0, VULSCREEN_WIDTH - 100-15, 40) collectionViewLayout:layout];
        //_collectionView.showsHorizontalScrollIndicator = NO;
        //_collectionView.alwaysBounceVertical = YES;
        _collectionView.showsHorizontalScrollIndicator = NO;
        _collectionView.alwaysBounceVertical = NO;
        //_collectionView.alwaysBounceHorizontal = NO;
        if (@available(iOS 11.0, *)) {
            _collectionView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
        }
        _collectionView.backgroundColor = [UIColor whiteColor];
        [_collectionView registerClass:[VULFileTierCell class] forCellWithReuseIdentifier:@"VULFileTierCell"];
        _collectionView.delegate = self;
        _collectionView.dataSource = self;
    }
    return _collectionView;
}

@end
