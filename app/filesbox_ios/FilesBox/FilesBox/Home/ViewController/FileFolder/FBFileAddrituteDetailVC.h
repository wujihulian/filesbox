//
//  FBFileAddrituteDetailVC.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/8.
//

#import "BaseTableViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBFileAddrituteDetailVC : BaseTableViewController<JXCategoryListContentViewDelegate>
@property (nonatomic, strong) VULFileObjectModel *model;
@property (nonatomic, copy) void (^ gotoTagManageMent)();

@end

NS_ASSUME_NONNULL_END
