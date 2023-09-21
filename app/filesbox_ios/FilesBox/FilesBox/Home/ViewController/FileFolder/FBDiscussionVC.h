//
//  FBDiscussionVC.h
//  FilesBox
//
//  Created by 无极互联 on 2023/8/28.
//

#import "BaseTableViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBDiscussionVC : BaseTableViewController
@property (nonatomic, strong) VULFileObjectModel *model;
@property (nonatomic, assign) float topH;

@end

NS_ASSUME_NONNULL_END
