//
//  FBUploadCell.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/3.
//

#import "BaseTableViewCell.h"

NS_ASSUME_NONNULL_BEGIN
typedef void(^ZFBtnClickBlock)(void);

@interface FBUploadCell : BaseTableViewCell
@property (nonatomic, copy) UBUploadModel *model;
@property (nonatomic,copy) void (^ startOrStopWithModel)( UBUploadModel *model);
@property (nonatomic,copy) void (^ deleteWithModel)( UBUploadModel *model);

@end

NS_ASSUME_NONNULL_END
