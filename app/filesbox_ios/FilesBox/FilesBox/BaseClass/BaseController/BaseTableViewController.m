//
//  BaseTableViewController.m
//  VideoULTeacher
//
//  Created by yuekewei on 2020/4/9.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "BaseTableViewController.h"

@interface BaseTableViewController ()

@end

@implementation BaseTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self.view addSubview:self.tableView];
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.view);
    }];
    
    self.pageIndex = 1;
}

#pragma mark - 刷新
- (void)setupRefreshHeader {
   self.tableView.mj_header = ({
        MJRefreshNormalHeader *header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(topRefreshing)];
        header.automaticallyChangeAlpha = YES;
        header.lastUpdatedTimeLabel.hidden = YES;
        header;
    });
    
   
}

- (void)setupRefreshFooter {
    self.tableView.mj_footer = [MJRefreshBackNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(bottomRefreshing)];
}

- (void)topRefreshing {
    self.pageIndex = 1;
}

- (void)bottomRefreshing {
    
}

- (void)endRefreshing {
    if (self.tableView.mj_header.state == MJRefreshStateRefreshing ) {
        [self.tableView.mj_header endRefreshing];
    }
    else if (self.tableView.mj_footer.state == MJRefreshStateRefreshing ) {
        [self.tableView.mj_footer endRefreshing];
    }
}

#pragma mark - UITableViewDelegate\UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    return [UITableViewCell new];
}

- (UITableViewStyle )tableViewStyle {
    return UITableViewStylePlain;
}

#pragma mark - Lazy
- (BaseTableView *)tableView {
    if (!_tableView) {
        _tableView = [[BaseTableView alloc] initWithFrame:CGRectZero style:[self tableViewStyle]];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.backgroundColor = [UIColor clearColor];
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    }
    return _tableView;
}

@end
