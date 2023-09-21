//
//  FBMessageCell.h
//  FilesBox
//
//  Created by 无极互联 on 2023/6/13.
//

#import "BaseTableViewCell.h"
#import "FBMessageModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBMessageCell : BaseTableViewCell
@property (nonatomic, strong) FBMessageModel *model;

@end

NS_ASSUME_NONNULL_END
