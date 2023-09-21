//
//  FBFailFileVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/5/23.
//

#import "FBFailFileVC.h"
#import "FBUploadCell.h"
@interface FBFailFileVC ()<DZNEmptyDataSetSource,DZNEmptyDataSetDelegate>

@end

@implementation FBFailFileVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationTitle = KLanguage(@"备份失败列表");
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
    [self.tableView registerClass:[FBUploadCell class] forCellReuseIdentifier:@"FBUploadCell"];
    self.tableView.emptyDataSetSource = self;
    self.tableView.emptyDataSetDelegate = self;
    // Do any additional setup after loading the view.
}
#pragma mark - tableviewDelegate , tableviewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
  
    return 0;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    FBUploadCell *videoCell = [tableView dequeueReusableCellWithIdentifier:@"FBUploadCell" forIndexPath:indexPath];

    return videoCell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
   
    
    
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
- (CGFloat)verticalOffsetForEmptyDataSet:(UIScrollView *)scrollView {
    return 0.1f;
}

- (UIImage *)imageForEmptyDataSet:(UIScrollView *)scrollView {
    return [UIImage imageNamed:@"no_data"];
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
