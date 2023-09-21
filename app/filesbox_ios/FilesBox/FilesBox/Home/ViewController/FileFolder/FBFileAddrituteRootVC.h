//
//  FBFileAddrituteRootVC.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/8.
//

#import "VULBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBFileAddrituteRootVC : VULBaseViewController
@property (nonatomic, strong) VULFileObjectModel *model;
@property (nonatomic, copy) void (^ gotoTagManageMent)();
@property (nonatomic, copy) void(^openDetialWithModel)(VULFileObjectModel *model);

@end

NS_ASSUME_NONNULL_END
