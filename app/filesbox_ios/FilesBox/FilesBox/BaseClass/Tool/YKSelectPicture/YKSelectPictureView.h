//
//  YKSelectPictureView.h
//  xiaolancang
//
//  Created by yuekewei on 2019/12/23.
//  Copyright © 2019 yeqiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TZImagePickerUtil.h"

NS_ASSUME_NONNULL_BEGIN

@interface YKSelectPictureView : UIView

/**展示图片collectionView*/
@property (nonatomic, strong) UICollectionView *collectionView;
@property (nonatomic, strong) TZImagePickerUtil *imagePicker;
@property (nonatomic, copy) void(^selectedBolck)(void);

@property (nonatomic, copy) void(^updateHeightBolck)(CGFloat collectionHeight);

@property (nonatomic, assign) CGSize itemSize;

@property (nonatomic, assign) NSInteger maxShowRowCount;

@property (nonatomic, assign) BOOL hideAddWhenMax;


- (instancetype)initWithFrame:(CGRect)frame
                     addImage:(UIImage *)addImage
                     maxCount:(NSInteger )maxCount;

- (CGFloat)collectionHeight:(CGFloat)collectionWidth;
@end

NS_ASSUME_NONNULL_END
