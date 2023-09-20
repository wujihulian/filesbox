//
//  BaseTableViewController.h
//  VideoULTeacher
//
//  Created by yuekewei on 2020/4/9.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "VULBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface BaseTableViewController : VULBaseViewController<UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong) BaseTableView *tableView;
@property (nonatomic, assign) NSInteger pageIndex;

- (void)setupRefreshHeader;

- (void)setupRefreshFooter;

- (void)topRefreshing NS_REQUIRES_SUPER;

- (void)bottomRefreshing;

- (void)endRefreshing;
@end

NS_ASSUME_NONNULL_END
