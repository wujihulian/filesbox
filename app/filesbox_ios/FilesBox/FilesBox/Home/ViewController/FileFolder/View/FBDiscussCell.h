//
//  FBDiscussCell.h
//  FilesBox
//
//  Created by 无极互联 on 2023/4/13.
//

#import "BaseTableViewCell.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBDiscussCell : BaseTableViewCell
@property (nonatomic, strong) VULFileObjectModel *model;
@property (nonatomic, strong) UIView *lineV;

@end

NS_ASSUME_NONNULL_END
