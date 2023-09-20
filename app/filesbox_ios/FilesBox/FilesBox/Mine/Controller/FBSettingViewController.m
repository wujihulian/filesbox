//
//  FBSettingViewController.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/31.
//

#import "FBSettingViewController.h"
#import "FBChangeLanguage.h"
#import "FBTabBarControllerConfig.h"
#import "FBMineInfoVC.h"

@interface FBSettingViewController ()<UITableViewDelegate, UITableViewDataSource>
@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic, strong) NSArray *dataArray;
@property (nonatomic, strong) zhPopupController *popupController;
@property (nonatomic, strong) NSFileManager *fileManager;
@property (nonatomic, copy) NSString *totleStr;



@end

@implementation FBSettingViewController
@synthesize fileManager,totleStr;
- (void)viewDidLoad {
    [super viewDidLoad];
    fileManager = [NSFileManager defaultManager];

    self.navigationTitle = KLanguage(@"设置");
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    [self.view addSubview:self.tableView];
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.offset(K_NavBar_Height);
        make.left.right.bottom.offset(0);
    }];
    
    
    //NSDocumentDirectory、NSCachesDirectory、NSLibraryDirectory 沙盒的document、caches、library
    unsigned long long fileSize = [self sizeAtPath:filePath(@"upload")];
    unsigned long long fileSize1 = [self sizeAtPath:filePath(@"specialData")];
    unsigned long long fileSize2 = [self sizeAtPath:filePath(@"downLoad")];
    //将文件夹大小转换为 M/KB/B
    unsigned long long totleSize = fileSize+ fileSize1+ fileSize2;
    
    if (totleSize > 1000 * 1000)
    {
        totleStr = [NSString stringWithFormat:@"%.1fM",totleSize / 1000.0f /1000.0f];
    }else if (totleSize > 1000)
    {
        totleStr = [NSString stringWithFormat:@"%.1fKB",totleSize / 1000.0f ];
        
    }else
    {
        totleStr = [NSString stringWithFormat:@"%.1fB",totleSize / 1.0f];
    }
    
    
    self.dataArray = @[KLanguage(@"个人中心"),KLanguage(@"清除缓存"),KLanguage(@"语言设置"),KLanguage(@"服务条款"), KLanguage(@"隐私协议"),];
}


// 获取文件或者文件夹大小(单位：B)
- (unsigned long long)sizeAtPath:(NSString *)path {
    NSFileManager *fm = [NSFileManager defaultManager];
    BOOL isDir = YES;
    if (![fm fileExistsAtPath:path isDirectory:&isDir]) {
        return 0;
    };
    unsigned long long fileSize = 0;
    // directory
    if (isDir) {
        NSDirectoryEnumerator *enumerator = [fm enumeratorAtPath:path];
        while (enumerator.nextObject) {
           // 下面注释掉的代码作用：不递归遍历子文件夹
           // if ([enumerator.fileAttributes.fileType isEqualToString:NSFileTypeDirectory]) {
           //      [enumerator skipDescendants];
           // }
            fileSize += enumerator.fileAttributes.fileSize;
        }
    } else {
        // file
        fileSize = [fm attributesOfItemAtPath:path error:nil].fileSize;
    }
    return fileSize;
}
- (void)navigationBack {
    [self.navigationController popViewControllerAnimated:YES];
}
#pragma mark --
#pragma mark -- Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell" forIndexPath:indexPath];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cell"];
    }

    cell.textLabel.textColor = HEXCOLOR(0x333333);
    cell.textLabel.font = [UIFont systemFontOfSize:16];
    cell.textLabel.text = self.dataArray[indexPath.row];
    UILabel *label;
    if (![cell viewWithTag:1000]) {
        label= [UILabel new];
        label.tag = 1000;
        label.textColor = HEXCOLOR(0x333333);
        [cell addSubview:label];
        [label mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(30));
            make.centerY.mas_equalTo(cell.mas_centerY);
        }];
    }else{
        label= [cell viewWithTag:1000];
    }
    UIView *lineV;
    if (![cell viewWithTag:10001]) {
        lineV= [UIView new];
        lineV.backgroundColor = [HEXCOLOR(0x000000) colorWithAlphaComponent:0.05];
        [cell addSubview:lineV];
        [lineV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(10));
            make.height.mas_equalTo(1);
            make.bottom.mas_equalTo(-1);
        }];
    }else{
        lineV= [cell viewWithTag:10001];
    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    label.hidden = YES;
    if (indexPath.row==1 || indexPath.row==2 ) {
  
        label.hidden = NO;
        
        if (indexPath.row==2 ) {
        NSString *appLanguage = [[NSUserDefaults standardUserDefaults] objectForKey:@"appLanguage"];
            
            NSArray *titleArr = @[@"zh-Hans",@"en",@"zh-Hant",@"ja",@"fr",@"es",@"ru"];
            NSArray *titleArr1 = @[@"简体中文",@"English",@"繁體中文",@"日本語",@"Français",@"Español",@"русский"];

            for(int i=0 ;i<titleArr.count;i++){
                NSString *age = titleArr[i];
                if ([appLanguage isEqualToString:age]){
                    label.text = titleArr1[i];
                    break;
                }
            }
            
        }else{
            label.text = totleStr;
        }
    }

    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.row == 0) {
        FBMineInfoVC *vc = [FBMineInfoVC new];
        vc.saveAndRefreshBlock = ^{
            if(self.saveAndRefreshBlock){
                self.saveAndRefreshBlock();
            }
             
        };
        [self.navigationController pushViewController:vc animated:YES];
    }
    else if (indexPath.row == 2) {
        FBChangeLanguage   *firstShowView = [[FBChangeLanguage alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH*0.7, fontAuto(400))];
        zhPopupController *popup = [[zhPopupController alloc] initWithView:firstShowView size:CGSizeMake(VULSCREEN_WIDTH * 0.9, firstShowView.height)];
        popup.layoutType = zhPopupLayoutTypeCenter;
        popup.dismissOnMaskTouched = NO;
        popup.presentationStyle = zhPopupSlideStyleFromBottom;
        popup.maskAlpha = 0.35;

     

        firstShowView.changeLanguageWithIndex = ^(NSInteger index) {
            
            FBTabBarControllerConfig *teacherTabbarConfig = [[FBTabBarControllerConfig alloc] init];
            [self changeRootViewController:teacherTabbarConfig.tabBarController];
            [popup dismiss];
        };
        [popup showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
    }else if (indexPath.row == 3) {
        
        
//        /pages/schoolRegister/fbxAppProtocol.html
        VULBaseWebViewVC *webVc = [[VULBaseWebViewVC alloc] init];
        webVc.currentURL = [NSString stringWithFormat:@"%@/pages/schoolRegister/fbxAppPolicy.html",@"https://test.1x.cn"];
        [self.navigationController pushViewController:webVc animated:YES];
    } else if (indexPath.row == 4) {
        VULBaseWebViewVC *webVc = [[VULBaseWebViewVC alloc] init];
        webVc.currentURL = [NSString stringWithFormat:@"%@/pages/schoolRegister/fbxAppProtocol.html",@"https://test.1x.cn"];
        [self.navigationController pushViewController:webVc animated:YES];
    } else  {


        [self vul_showAlertWithTitle:KLanguage(@"确定删除缓存？含有正在上传或者下载的文件") message:nil appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
            alertMaker.
            addActionCancelTitle(KLanguage(@"取消")).
            addActionDestructiveTitle(KLanguage(@"确定"));
        } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
            if (buttonIndex == 1) {
                totleStr = @"0.0B";
                //删除旧版数据库文件
                [[DownloadProgress sharedInstance].progressDict removeAllObjects];
                [NSArray bg_clearArrayWithName:@"uploadFileList"];
                [self.tableView reloadData];
                NSFileManager *fileManager = [NSFileManager defaultManager];
                if ([fileManager removeItemAtPath:filePath(@"upload") error:NULL]) {
                NSLog(@"删除成功");
                }else{
                NSLog(@"删除失败");
                }
                
                if ([fileManager removeItemAtPath:filePath(@"specialData") error:NULL]) {
                NSLog(@"删除成功");
                }else{
                NSLog(@"删除失败");
                }
                if ([fileManager removeItemAtPath:filePath(@"downLoad") error:NULL]) {
                NSLog(@"删除成功");
                }else{
                NSLog(@"删除失败");
                }
            }
        }];
  

    }
}

// TODO: 切换根控制器
- (void)changeRootViewController:(UIViewController *)rootViewController {
    typedef void (^Animation)(void);
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    rootViewController.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
    Animation animation = ^{
        BOOL oldState = [UIView areAnimationsEnabled];
        [UIView setAnimationsEnabled:NO];
        [UIApplication sharedApplication].keyWindow.rootViewController = rootViewController;
        [UIView setAnimationsEnabled:oldState];
    };
    
    [UIView transitionWithView:window duration:0.5f options:UIViewAnimationOptionTransitionCrossDissolve animations:animation completion:nil];
}





- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.backgroundColor = UIColor.whiteColor;
        [_tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"cell"];
        _tableView.contentInset = UIEdgeInsetsMake(0, 0, 5, 0);
        _tableView.estimatedRowHeight = 125.f;
        _tableView.rowHeight = UITableViewAutomaticDimension;
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        _tableView.tableFooterView = [[UIView alloc] init];
    }
    return _tableView;
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
