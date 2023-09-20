//
//  FBFileVersionManagentVC.h
//  FilesBox
//
//  Created by 无极互联 on 2023/4/12.
//

#import "BaseTableViewController.h"
#import "SDImageCache.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBFileVersionManagentVC : BaseTableViewController
@property (nonatomic, strong) VULFileObjectModel *model;
@property (nonatomic, copy) void (^uploadRefreshBlock)(VULFileObjectModel *model);
@property (nonatomic, copy) void(^openDetialWithModel)(VULFileObjectModel *model);

@end

NS_ASSUME_NONNULL_END
