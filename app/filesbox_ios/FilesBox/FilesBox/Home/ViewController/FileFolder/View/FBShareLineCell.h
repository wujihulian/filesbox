//
//  FBShareLineCell.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/29.
//

#import "BaseTableViewCell.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBShareLineCell : BaseTableViewCell
@property (nonatomic, strong) VULFileObjectModel *model;
@property (nonatomic, copy) void (^ clickShareOrCopy)(BOOL flag);
@property (nonatomic, strong) UITextField *codeTextField;
@end

NS_ASSUME_NONNULL_END
