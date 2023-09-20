//
//  VULFileTierView.h
//  UnlimitedBusiness
//
//  Created by zuoyi on 2021/12/14.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULFileTierCell : UICollectionViewCell

@end

typedef void (^ FileTierViewBlock)(NSInteger tag);

@interface VULFileTierView : UIView

@property (nonatomic,strong) UICollectionView *collectionView;
@property (nonatomic,strong) NSMutableArray *dataArray;
@property (nonatomic,copy) FileTierViewBlock fileTierViewBlock;
@property (nonatomic,copy) void (^ selectImageWithRow)(NSInteger tag,UIImageView *rightArrow);

@end

NS_ASSUME_NONNULL_END
