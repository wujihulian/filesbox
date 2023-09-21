//
//  FBFileMessageViewController.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/21.
//

#import "FBFileMessageViewController.h"
#import "FBMessageCell.h"
#import "BaseWebViewController.h"
#import "FBMessageModel.h"
@interface FBFileMessageViewController ()<DZNEmptyDataSetSource,DZNEmptyDataSetDelegate>
@property (nonatomic, strong) NSMutableArray *dataArray;
@end

@implementation FBFileMessageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationTitle = KLanguage(@"消息");
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];

    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
//    [self baseAddNavRightBtnWithImage:VULGetImage(@"icon_more_color") selector:@selector(operationChangeFile:)];
    // 获取单例对象
//    self.navigationViewBackgroundColor = HEXCOLOR(0x4191ff);
//    self.navigationViewBackgroundImageView.image = VULGetImage(@"image_radarHeader");
    [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.bottom.left.right.mas_equalTo(0);
        make.top.mas_equalTo(K_NavBar_Height);
    }];
    [self.tableView registerClass:[FBMessageCell class] forCellReuseIdentifier:@"FBMessageCell"];
    self.tableView.emptyDataSetSource = self;
    self.tableView.emptyDataSetDelegate = self;
    self.dataArray = [NSMutableArray array];
    self.tableView.estimatedRowHeight = fontAuto(200);
    [self setupRefreshHeader];
    [self setupRefreshFooter];
    [self requestList];

    // Do any additional setup after loading the view.
}
- (void)topRefreshing {
    self.pageIndex = 1;
    [self.dataArray removeAllObjects];
    [self requestList];
}

- (void)bottomRefreshing {
    self.pageIndex ++;;
    [self requestList];
}
-(void)requestList{
    
    [VULBaseRequest requestWithUrl:@"/api/notice/read/list/page" params:@{@"currentPage":@(self.pageIndex),@"pageSize":@"50"} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        [self endRefreshing];
        if (request.success) {
            NSArray *arr =request.data[@"list"];
            for(NSDictionary *dic in arr){
                FBMessageModel *model = [FBMessageModel modelWithDictionary:dic];
                [self.dataArray addObject:model];
            }
            [self.tableView reloadData];
            [self.tableView configEmptyViewWithHasData:self.dataArray.count > 0 hasError:!request.success insets:UIEdgeInsetsMake(0, 0, 0, 0) image:VULGetImage(@"no_data") info:nil reloadBlock:^{
                
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

    return self.dataArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    FBMessageCell *videoCell = [tableView dequeueReusableCellWithIdentifier:@"FBMessageCell" forIndexPath:indexPath];
    videoCell.model = self.dataArray[indexPath.row];
    return videoCell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
//https://dev.filesbox.cn/#/noticePage/30073
    FBMessageModel*model = self.dataArray[indexPath.row];
    if(!model.isRead){
        model.isRead = YES;
//        
//        [VULBaseRequest requestWithUrl:@"/api/notice/info" params:@{@"id":model.id} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
//            [self endRefreshing];
//            if (request.success) {
//
//            } else
//                [self makeToast:request.message];
//            
//       
//        }];
        [self.tableView reloadData];

    }
    BaseWebViewController *webVc = [[BaseWebViewController alloc] init];
    webVc.nvaTitle = model.title;
    webVc.currentURL = [NSString stringWithFormat:@"%@#/noticePage/%@/%@",ChooseUrl,model.id,[VULRealmDBManager getLocalToken]];
    [self.navigationController pushViewController:webVc animated:YES];
    
}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    UIView *headerView = [UIView new];
  
    return headerView;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 0.01;
}



- (UITableViewStyle )tableViewStyle {
    return UITableViewStylePlain;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.01;

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
