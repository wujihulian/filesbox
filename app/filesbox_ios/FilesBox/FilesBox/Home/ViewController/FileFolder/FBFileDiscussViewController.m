//
//  FBFileDiscussViewController.m
//  FilesBox
//
//  Created by 无极互联 on 2023/4/13.
//

#import "FBFileDiscussViewController.h"
#import "FBDiscussCell.h"
@interface FBFileDiscussViewController ()
@property (nonatomic, strong) NSMutableArray *dataArr; //

@end

@implementation FBFileDiscussViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationView.hidden = YES;
    [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.bottom.left.right.mas_equalTo(0);
        make.top.mas_equalTo(fontAuto(fontAuto(10)));
    }];
    [self.tableView registerClass:[FBDiscussCell class] forCellReuseIdentifier:@"FBDiscussCell"];
    self.tableView.estimatedRowHeight = fontAuto(120);
    self.dataArr = [NSMutableArray array];
    [self gethistory];
    [self setupRefreshHeader];
    [self setupRefreshFooter];

    // Do any additional setup after loading the view.
}
-(void)topRefreshing{
    [super topRefreshing];
    [self.dataArr removeAllObjects];
    [self gethistory];
}
- (void)bottomRefreshing {
    self.pageIndex ++;
    [self gethistory];
}
-(void)gethistory{
    [VULBaseRequest requestWithUrl:@"/api/disk/pathLog" params:@{@"sourceID":self.model.sourceID,@"pageSize":@(30),@"currentPage":@(self.pageIndex)} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        [self.tableView.mj_header endRefreshing];
        [self.tableView.mj_footer endRefreshing];
        [self dissmissHudView];
        if (request.success) {
            NSArray *fileList = request.data[@"list"];
            for (NSDictionary *dic in fileList) {
                VULFileObjectModel *mode = [VULFileObjectModel modelWithDictionary:dic];
                [self.dataArr addObject:mode];
            }
            
            [self.tableView reloadData];
            [self.tableView configEmptyViewWithHasData:self.dataArr.count > 0 hasError:!request.success insets:UIEdgeInsetsMake(0, 0, 0, 0) image:VULGetImage(@"no_data") info:nil reloadBlock:^{
                
            }];
        } else
            [self makeToast:request.message];
    }];
}
#pragma mark - tableviewDelegate , tableviewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
  
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return self.dataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    FBDiscussCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FBDiscussCell" forIndexPath:indexPath];
    cell.separatorLine.hidden = YES;
    VULFileObjectModel *model = self.dataArr[indexPath.row];
    cell.model = model;
    cell.lineV.hidden = indexPath.row ==self.dataArr.count-1;
    return cell;
}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    UIView *headerView = [UIView new];
    return headerView;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 0.01;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];

}

- (UITableViewStyle )tableViewStyle {
    return UITableViewStyleGrouped;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.01;

}

#pragma mark - JXCategoryListContentViewDelegate
- (UIView *)listView {
    return self.view;
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
