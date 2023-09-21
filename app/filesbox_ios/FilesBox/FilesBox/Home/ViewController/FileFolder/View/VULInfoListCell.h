//
//  VULInfoListCell.h
//  FilesBox
//
//  Created by 无极互联 on 2023/6/15.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULInfoListCell : UICollectionViewCell
@property (nonatomic,strong) VULInfoModel *model;
@property (nonatomic,copy) void (^ shareWithModel)(VULInfoModel *model);

@end

NS_ASSUME_NONNULL_END
