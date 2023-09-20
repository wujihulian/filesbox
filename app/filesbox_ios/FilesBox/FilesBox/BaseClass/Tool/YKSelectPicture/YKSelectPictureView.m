//
//  YKSelectPictureView.m
//  xiaolancang
//
//  Created by yuekewei on 2019/12/23.
//  Copyright © 2019 yeqiang. All rights reserved.
//

#import "YKSelectPictureView.h"

#define RowCount  4 //每行显示图片数

typedef void(^DeletePictureHandle)(void);

@interface YKPictureCell : UICollectionViewCell

@property (nonatomic, strong) UIImageView *mainImageView;
@property (nonatomic, strong) UIButton *deleteButton;
@property (nonatomic, assign) BOOL canDelete;
@property (nonatomic, copy) DeletePictureHandle deleteBlock;
@end

@implementation YKPictureCell

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];
        [self.contentView addSubview:self.mainImageView];
        [self.contentView addSubview:self.deleteButton];
        
        [self.deleteButton mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.right.mas_equalTo(0);
            make.size.mas_equalTo(self.deleteButton.size);
        }];
        
        [self.mainImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.deleteButton.mas_bottom).multipliedBy(0.5);
            make.right.mas_equalTo(self.deleteButton.mas_centerX);
            make.left.mas_equalTo(0);
            make.height.mas_equalTo(self.mainImageView.mas_width);
        }];
    }
    return self;
}

- (void)setCanDelete:(BOOL)canDelete {
    _canDelete = canDelete;
    self.deleteButton.hidden = !canDelete;
    if (canDelete) {
        _mainImageView.backgroundColor = [UIColor colorWithRed:216/255.0 green:216/255.0 blue:216/255.0 alpha:1.0];
    }
    else {
        _mainImageView.backgroundColor = [UIColor clearColor];
    }
}

- (void)deleteAction {
    if (_deleteBlock) {
        _deleteBlock();
        _deleteBlock = nil;
    }
}

#pragma mark - Lazy
- (UIImageView *)mainImageView {
    if (!_mainImageView) {
        _mainImageView = [UIImageView new];
        _mainImageView.userInteractionEnabled = YES;
        _mainImageView.contentMode = UIViewContentModeScaleAspectFill;
        _mainImageView.backgroundColor = [UIColor colorWithRed:238/255.0 green:238/255.0 blue:238/255.0 alpha:1.0];
        _mainImageView.layer.cornerRadius = 4.0;
        _mainImageView.layer.masksToBounds = YES;
    }
    return _mainImageView;
}

- (UIButton *)deleteButton {
    if (!_deleteButton) {
        _deleteButton = [UIButton buttonWithType:UIButtonTypeCustom];
        UIImage *image = [UIImage imageNamed:@"icon_del"];
        _deleteButton.bounds = CGRectMake(0, 0, image.size.width + 6, image.size.width + 6);
        _deleteButton.contentEdgeInsets = UIEdgeInsetsMake(3, 3, 3, 3);
        [_deleteButton setImage:image forState:UIControlStateNormal];
        [_deleteButton addTarget:self action:@selector(deleteAction) forControlEvents:UIControlEventTouchUpInside];
    }
    return _deleteButton;
}
@end



@interface YKSelectPictureView ()<UICollectionViewDelegate, UICollectionViewDataSource>

@property (nonatomic, assign) NSInteger maxImagesCount;
@property (nonatomic, strong) UIImage *addImage;
@end

@implementation YKSelectPictureView

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        _maxImagesCount = 1;
        _maxShowRowCount = 4;
        [self addSubview:self.collectionView];
    }
    return self;
}

- (instancetype)initWithFrame:(CGRect)frame
                     addImage:(UIImage *)addImage
                     maxCount:(NSInteger )maxCount {
    
    self = [super initWithFrame:frame];
    if (self) {
        _maxImagesCount = maxCount;
        _addImage = addImage;
        _maxShowRowCount = 4;
        [self addSubview:self.collectionView];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.collectionView.frame = self.bounds;
}

- (void)setHideAddWhenMax:(BOOL)hideAddWhenMax {
    _hideAddWhenMax = hideAddWhenMax;
    
    [self.collectionView reloadData];
}


- (void)updateCollectionView {
    [self.collectionView reloadData];
    if (self.updateHeightBolck) {
        CGFloat height = self.collectionView.collectionViewLayout.collectionViewContentSize.height;
        self.updateHeightBolck(height);
    }
}

- (CGFloat)collectionHeight:(CGFloat)collectionWidth {
    CGFloat height = self.collectionView.collectionViewLayout.collectionViewContentSize.height;
    if (height == 0) {
        if (!CGSizeEqualToSize(_itemSize, CGSizeZero)) {
            height = _itemSize.height;
        }
        else {
            height = collectionWidth / (CGFloat)_maxShowRowCount;
        }
    }
    return height;
}

#pragma mark - UICollectionViewDataSource/UICollectionViewDelegate

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView
     numberOfItemsInSection:(NSInteger)section {
    
    NSInteger count = self.imagePicker.selectedPhotos.count;
    if (!_hideAddWhenMax || self.imagePicker.selectedAssets.count != _maxImagesCount) {
        count += 1;
    }
    return count ;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView
                  cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    
    YKPictureCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"cell" forIndexPath:indexPath];
    cell.canDelete = indexPath.item != self.imagePicker.selectedAssets.count;
    if (indexPath.row == self.imagePicker.selectedAssets.count) {
        cell.mainImageView.image = _addImage;
    }
    else {
        cell.mainImageView.image = [self.imagePicker.selectedPhotos objectAtIndex:indexPath.item];
        WeakSelf(self)
        [cell setDeleteBlock:^{
            [weakself.imagePicker removeImageForIndex:indexPath.item];
            if (weakself.selectedBolck) {
                weakself.selectedBolck();
            }
            [weakself updateCollectionView];
        }];
    }
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    
    CGSize itemSize = CGSizeMake(collectionView.width / (CGFloat)_maxShowRowCount, collectionView.width / (CGFloat)_maxShowRowCount);
    
    if (_itemSize.width > 0 && _itemSize.height > 0) {
        itemSize = _itemSize;
    }
    
    return itemSize;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.item == self.imagePicker.selectedAssets.count) {
        [self.imagePicker showAlertController];
    }
    else {
        id data = [_imagePicker.selectedAssets objectAtIndex:indexPath.item];
        if ([data isKindOfClass:[PHAsset class]]) {
            PHAsset *asset = (PHAsset *)data;
            if ([[asset valueForKey:@"filename"] containsString:@"GIF"]) {
                // 预览GIF
                TZGifPhotoPreviewController *vc = [[TZGifPhotoPreviewController alloc] init];
                TZAssetModel *model = [TZAssetModel modelWithAsset:asset type:TZAssetModelMediaTypePhotoGif timeLength:@""];
                vc.model = model;
                vc.modalPresentationStyle = UIModalPresentationFullScreen;
                [_collectionView.viewController presentViewController:vc animated:YES completion:nil];
            }
            else if (asset.mediaType == PHAssetMediaTypeVideo) {
                // 预览视频
                TZVideoPlayerController *vc = [[TZVideoPlayerController alloc] init];
                TZAssetModel *model = [TZAssetModel modelWithAsset:asset type:TZAssetModelMediaTypeVideo timeLength:@""];
                vc.model = model;
                vc.modalPresentationStyle = UIModalPresentationFullScreen;
                [_collectionView.viewController presentViewController:vc animated:YES completion:nil];
                
            }
            else {
                // 预览照片
                TZImagePickerController *imagePickerVc = [[TZImagePickerController alloc] initWithSelectedAssets:_imagePicker.selectedAssets selectedPhotos:_imagePicker.selectedPhotos index:indexPath.item];
                imagePickerVc.isSelectOriginalPhoto = NO;
                imagePickerVc.modalPresentationStyle = UIModalPresentationFullScreen;
                [_collectionView.viewController.navigationController presentViewController:imagePickerVc animated:YES completion:nil];
            }
        }
    }
}

#pragma mark - Lazy
- (UICollectionView *)collectionView {
    if (!_collectionView) {
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        layout.scrollDirection = UICollectionViewScrollDirectionVertical;
        layout.minimumInteritemSpacing = 0;
        layout.minimumLineSpacing = 0;
        
        _collectionView = [[UICollectionView alloc]initWithFrame:self.bounds collectionViewLayout:layout];
        _collectionView.backgroundColor = [UIColor whiteColor];
        _collectionView.delegate = self;
        _collectionView.dataSource = self;
        _collectionView.alwaysBounceVertical = YES;
        _collectionView.scrollEnabled = NO;
        _collectionView.showsVerticalScrollIndicator = NO;
        _collectionView.showsHorizontalScrollIndicator = NO;
        [_collectionView registerClass:[YKPictureCell class] forCellWithReuseIdentifier:@"cell"];
    }
    return _collectionView;
}

- (TZImagePickerUtil *)imagePicker {
    if (!_imagePicker) {
        _imagePicker = [TZImagePickerUtil new];
        WeakSelf(self)
        [_imagePicker setSelectedBolck:^{
            [weakself updateCollectionView];
            if (weakself.selectedBolck) {
                weakself.selectedBolck();
            }
        }];
        [_imagePicker setImagePickerCofing:^(TZImagePickerController * _Nonnull pickerController) {
            
            pickerController.maxImagesCount = weakself.maxImagesCount;
            pickerController.allowTakeVideo = NO;
            pickerController.allowPickingVideo = NO;
            pickerController.cropRect = CGRectMake(0, (kScreenHeight - kScreenWidth)/2.0, kScreenWidth, kScreenWidth);
        }];
    }
    return _imagePicker;
}


@end
