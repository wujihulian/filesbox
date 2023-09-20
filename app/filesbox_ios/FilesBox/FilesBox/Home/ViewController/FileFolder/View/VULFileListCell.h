//
//  VULFileListCell.h
//  UnlimitedBusiness
//
//  Created by zuoyi on 2021/12/14.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VULFileModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULFileListCell : UICollectionViewCell

@property (nonatomic,strong) VULFileObjectModel *model;
@property (nonatomic,strong) UIImageView *selectImageV ;

@end

NS_ASSUME_NONNULL_END
