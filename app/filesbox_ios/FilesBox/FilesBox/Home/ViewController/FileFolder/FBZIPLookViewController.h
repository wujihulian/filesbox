//
//  FBZIPLookViewController.h
//  FilesBox
//
//  Created by 无极互联 on 2023/5/4.
//

#import "BaseTableViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBZIPLookViewController : BaseTableViewController
@property (nonatomic, strong) VULFileObjectModel *model;
@property (nonatomic, copy) void (^unZipWtihModel)(VULFileZIPObjectModel *model);
@property (nonatomic, assign) BOOL flag;//yes 是组织
@property (nonatomic,copy) void (^ selectOperationWithTitle)(NSString *title,VULFileObjectModel *model);

@end

NS_ASSUME_NONNULL_END
