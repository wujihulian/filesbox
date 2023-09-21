//
//  FBDownOrUploadVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/2.
//

#import "FBDownOrUploadVC.h"
#import "ChunkUploader.h"
#import "FBUploadCell.h"
#import "YCMenuView.h"
#import "FBFromTopView.h"
#import "FBBackUpView.h"
#import "FBAutomaticFilesVC.h"

@interface FBDownOrUploadVC ()<UIDocumentInteractionControllerDelegate,UICollectionViewDelegate,UICollectionViewDataSource,UIImagePickerControllerDelegate,DZNEmptyDataSetSource,DZNEmptyDataSetDelegate,UIDocumentPickerDelegate>
{
    NSInteger sucessCount;
}
@property (nonatomic, strong) NSMutableArray *listArr; // 单例对象
@property (nonatomic, strong) UIDocumentInteractionController *documentController;
@property (nonatomic, strong) FBBackUpView *backUpView; // 单例对象

@end

@implementation FBDownOrUploadVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationTitle = KLanguage(@"传输");
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    [self baseAddNavRightBtnWithImage:VULGetImage(@"icon_more_color") selector:@selector(operationChangeFile:)];
    // 获取单例对象
    //    self.navigationViewBackgroundColor = HEXCOLOR(0x4191ff);
    //    self.navigationViewBackgroundImageView.image = VULGetImage(@"image_radarHeader");
    [self.view addSubview:self.backUpView];
    
    [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.bottom.left.right.mas_equalTo(0);
        make.top.mas_equalTo(self.backUpView.mas_bottom).offset(fontAuto(10));
    }];
    self.tableView.emptyDataSetSource = self;
    self.tableView.emptyDataSetDelegate = self;
    
    [VULNotificationCenter addObserver:self selector:@selector(uploadChange) name:@"uploadChangeNotificationCenter" object:nil];
    [VULNotificationCenter addObserver:self selector:@selector(changeView) name:@"backUpViewNotificationCenter" object:nil];
    
    [self.tableView registerClass:[FBUploadCell class] forCellReuseIdentifier:@"FBUploadCell"];
    self.tableView.estimatedRowHeight = fontAuto(150);
    
    [self reload];
 
    

    UITapGestureRecognizer *sender = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickBackUp)];
    [self.backUpView addGestureRecognizer:sender];
    self.backUpView.switchh.userInteractionEnabled =             [[ChunkUploader sharedManager] isBackUp];
    BOOL isCose =  [[NSUserDefaults standardUserDefaults]boolForKey:@"isClose"];
    if(![self isPhoto]){
        isCose = YES;
    }
    
    [self.backUpView.switchh setOn:[[ChunkUploader sharedManager] isBackUp]?!isCose:[[ChunkUploader sharedManager] isBackUp]];
    [[self.backUpView.switchh rac_signalForControlEvents:UIControlEventValueChanged] subscribeNext:^(__kindof UIControl *_Nullable x) {
        BOOL value = ((UISwitch *)x).on;
        if(value){
            if(![self isPhoto]){
                
                UIAlertController  *alertControl = [UIAlertController alertControllerWithTitle:@"无法访问系统相册" message:@"请在iPhone的\"设置-隐私-相册\"选项中,允许FilesBox访问你的相册" preferredStyle:UIAlertControllerStyleAlert];
                
                UIAlertAction *okAction = [UIAlertAction actionWithTitle:KLanguage(@"确定")  style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//                    NSString *downloadStr = [NSString stringWithFormat:@"https://itunes.apple.com/cn/app/id%@?mt=8",kAppID];
                    NSURL *url = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
                    [[UIApplication sharedApplication] openURL:url];
                }];
                UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:KLanguage(@"取消")  style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
                    
                }];
                [alertControl addAction:okAction];
                [alertControl addAction:cancelAction];
                
                [self presentViewController:alertControl animated:YES completion:nil];
                
                [self.backUpView.switchh setOn:NO];
            }else{
                [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"isClose"];
                self.backUpView.model =  [UBUploadModel new];
                [[ChunkUploader sharedManager] backupFile];
            }
     
            
        }else{
            
            [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"isClose"];
            self.backUpView.model =  [UBUploadModel new];
        }
    }];
    if([ChunkUploader sharedManager].isUploadModel.isOpen){
        [[ChunkUploader sharedManager] backupFile];
    }else{
        self.backUpView.model = [ChunkUploader sharedManager].isUploadModel;

    }

}
-(BOOL)isPhoto{
    PHAuthorizationStatus authorizationStatus = [PHPhotoLibrary authorizationStatus];
    // 判断授权状态
    if (authorizationStatus == PHAuthorizationStatusAuthorized) {
        return YES;
      
    } else if (authorizationStatus == PHAuthorizationStatusNotDetermined) { // 如果没决定, 弹出指示框, 让用户选择
        [PHPhotoLibrary requestAuthorization:^(PHAuthorizationStatus status) {
            // 如果用户选择授权, 则保存图片
            if (status == PHAuthorizationStatusAuthorized) {
            }
        }];
        return NO;
    } else {
        return NO;

    }
}

-(void)changeView{
    [VULQueue executeInMainQueue:^{
        self.backUpView.model = [ChunkUploader sharedManager].isUploadModel;
    }];
}

-(void)clickBackUp{
    FBAutomaticFilesVC *vc = [FBAutomaticFilesVC new];
    vc.saveAndRefreshBlock = ^{
        
        if(![self isPhoto]){
            [self.backUpView.switchh setOn:NO];
        }else{
            [self.backUpView.switchh setOn:[[ChunkUploader sharedManager] isBackUp]];
        }
        self.backUpView.switchh.userInteractionEnabled =             [[ChunkUploader sharedManager] isBackUp];

    };
    [self.navigationController pushViewController:vc animated:YES];
}
-(void)reload{
    NSMutableDictionary *progressDict = [DownloadProgress sharedInstance].progressDict;
    NSArray *arr =  progressDict.allKeys;
    self.listArr = [NSMutableArray array];
    NSMutableArray *arrModel = [NSMutableArray array];
    NSMutableArray *downModel = [NSMutableArray array];
    sucessCount = 0;
    for (NSString *filePath in arr) {
        UBUploadModel *model = progressDict[filePath];
        if (model.isDown) {
            [downModel addObject:model];

        }else{
            if(!model.isSucess){
                [arrModel addObject:model];
            }
        }
        if (model.isSucess) {
            sucessCount = sucessCount+1;
        }
        

    }


    self.listArr = [NSMutableArray array];
    if (arrModel.count>0) {
        [self.listArr  addObject:arrModel];
    }
    if (downModel.count>0) {
        [self.listArr  addObject:downModel];
    }
    [self.tableView reloadData];
}
-(void)operationChangeFile:(UIButton*)sender{
    
    NSArray *titleArr = @[KLanguage(@"全部开始"),KLanguage(@"全部暂停"),KLanguage(@"全部删除")];
    NSArray *imageArr = @[@"icon_all_start", @"icon_all_stop",@"icon_operation_del"];
    
    
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (NSInteger i = 0; i < titleArr.count; i++) {
        YCMenuAction *action = [YCMenuAction actionWithTitle:titleArr[i] image:VULGetImage(imageArr[i])  handler:^(YCMenuAction *action) {
            if ([action.title isEqualToString:KLanguage(@"全部开始")]) {
                // 从 DownloadProgress 单例对象中获取下载任务的进度
                NSMutableDictionary *progressDict = [DownloadProgress sharedInstance].progressDict;
                NSArray *arr =  progressDict.allKeys;
                for (NSString *filePath in arr) {
                    UBUploadModel *model = progressDict[filePath];
                    if (!model.isSucess) {
                        NSInteger row = [self getArrayWith:model.filePath withSecton:model.isDown?1:0];
                        if (row>=0) {
                            FBUploadCell *cell = [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:row inSection:model.isDown?1:0]];
                            if (model.isDown) {
                                if (model.isOffline) {
                                    model.status =  UploadStatusUploading;
                                    [FBDownloadFileAllManage.sharedManager addDownloadFileWithModel:model];

                                }else{
                                    FBDownloadFileManage *model1 = FBDownloadFileAllManage.sharedManager.sessionDic[model.filePath];
                                    [model1 resume:model];
                                }
                            }else{
                                model.status =  UploadStatusUploading;
                                [ChunkUploader updateUploadFileAtModel:model];
                            }
                            cell.model = model;

                        }
                    }
                  
                }
            }else  if ([action.title isEqualToString:KLanguage(@"全部暂停")]){
                // 从 DownloadProgress 单例对象中获取下载任务的进度
                NSMutableDictionary *progressDict = [DownloadProgress sharedInstance].progressDict;
                NSArray *arr =  progressDict.allKeys;
                for (NSString *filePath in arr) {
                    UBUploadModel *model = progressDict[filePath];
                    if (!model.isSucess) {
                        NSInteger index = self.listArr.count== 2? (model.isDown?1:0):0;

                        NSInteger row = [self getArrayWith:model.filePath withSecton:index];
                        if (row>=0) {
                            FBUploadCell *cell = [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:row inSection:index]];
                            if (model.isDown) {
                                if (model.isOffline) {

                                }else{
                                    FBDownloadFileManage *model1 = FBDownloadFileAllManage.sharedManager.sessionDic[model.filePath];
                                    [model1 pause:model];
                                }
                            }else{
                                model.status =  UploadStatusPaused;
                            }
                            cell.model = model;

                        }
                    }
                  
                }
            }
            else {
                [[DownloadProgress sharedInstance].progressDict removeAllObjects];
                [NSArray bg_clearArrayWithName:@"uploadFileList"];
                [self.listArr removeAllObjects];
                [self.tableView reloadData];
            }
        }];
        [array addObject:action];
    }
    NSString *appLanguage = [[NSUserDefaults standardUserDefaults] objectForKey:@"appLanguage"];
    CGFloat width = 200;
    if ([appLanguage isEqualToString:@"en"]) {
        width =220;
    }
    YCMenuView *menuView = [YCMenuView menuWithActions:array width:width relyonView:sender];
    menuView.menuColor = [UIColor whiteColor];
    menuView.cornerRaius = 0;
    menuView.separatorIndexArray = @[@(2)];
    menuView.separatorColor = [UIColor redColor];
    menuView.maxDisplayCount = 20;
    //menuView.offset = 150;
    menuView.textColor = UIColorHex(333333);
    menuView.textFont = [UIFont yk_pingFangRegular:14];
    menuView.menuCellHeight = 35;
    menuView.dismissOnselected = YES;
    menuView.dismissOnTouchOutside = YES;
    menuView.backgroundColor = [UIColor whiteColor];
    [menuView show];

}
-(void)uploadChange{
    [self updateProgress];

}

- (void)updateProgress {
    // 从 DownloadProgress 单例对象中获取下载任务的进度
    NSMutableDictionary *progressDict = [DownloadProgress sharedInstance].progressDict;
    NSArray *arr =  progressDict.allKeys;
    NSInteger count1 = 0;
    NSMutableArray *uploadArr = [NSMutableArray array];
    for (NSString *filePath in arr) {
        UBUploadModel *model = progressDict[filePath];
        if (model.isSucess) {
            count1 = count1+1;
            if(!model.isDown){
                [uploadArr addObject:model];
            }
        }
        [VULQueue executeInMainQueue:^{
            NSInteger index = self.listArr.count== 2? (model.isDown?1:0):0;
            NSInteger row = [self getArrayWith:model.filePath withSecton:index];
            if (row>=0) {
                FBUploadCell *cell = [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:row inSection:index]];
                cell.model = model;
            }
           
        }];
    }
    
    if (count1 >sucessCount) {
        if (self.saveAndRefreshBlock) {
            self.saveAndRefreshBlock();
        }
    }
    if(uploadArr.count>0){
        for( UBUploadModel *model in uploadArr ){
            [[DownloadProgress sharedInstance].progressDict removeObjectForKey:model.filePath];
        }
        [NSArray bg_clearArrayWithName:@"uploadFileList"];
        NSMutableArray *uploadFileList = [NSMutableArray array];
        [uploadFileList addObject:[DownloadProgress sharedInstance].progressDict];
        [uploadFileList bg_saveArrayWithName:@"uploadFileList"];
        [VULNotificationCenter postNotificationName:@"uploadChangeNotificationCenter" object:nil];

        [self reload];
    }

}
-(NSInteger )getArrayWith:(NSString *)filePath withSecton:(NSInteger)section{
    NSInteger row = -1;
    NSArray *modelArr = self.listArr[section];
    for (int i= 0 ;i<modelArr.count;i++) {
        UBUploadModel *model =modelArr[i];
        if ([model.filePath isEqualToString:filePath]) {
            row = i;
            break;
        }
    }
    return row;
}
#pragma mark - tableviewDelegate , tableviewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
  
    return self.listArr.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSArray *arr = self.listArr[section];

    return arr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    FBUploadCell *videoCell = [tableView dequeueReusableCellWithIdentifier:@"FBUploadCell" forIndexPath:indexPath];
    UBUploadModel *model =self.listArr[indexPath.section][indexPath.row];
    videoCell.model = model;
    videoCell.deleteWithModel = ^(UBUploadModel * _Nonnull model) {
        [[DownloadProgress sharedInstance].progressDict removeObjectForKey:model.filePath];
        [NSArray bg_clearArrayWithName:@"uploadFileList"];
        NSMutableArray *uploadFileList = [NSMutableArray array];
        [uploadFileList addObject:[DownloadProgress sharedInstance].progressDict];
        [uploadFileList bg_saveArrayWithName:@"uploadFileList"];
        [VULNotificationCenter postNotificationName:@"uploadChangeNotificationCenter" object:nil];

        [self reload];
    };
    return videoCell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    UBUploadModel *model =self.listArr[indexPath.section][indexPath.row];
    if (self.listArr.count== 2) {
        if (indexPath.section>0 && model.isSucess) {
            NSString *path = [NSString stringWithFormat:@"%@",filePath(@"downLoad")];

            NSURL *accessURL =[[NSURL fileURLWithPath:path] URLByAppendingPathComponent:[NSString stringWithFormat:@"%@.%@",model.fileID,model.fileType]];;
            _documentController = [UIDocumentInteractionController interactionControllerWithURL:accessURL];
            [_documentController setDelegate:self];
         [_documentController presentOptionsMenuFromRect:self.view.bounds inView:self.view animated:YES];
        }
    }else{
        if ( model.isSucess) {
            NSString *path = [NSString stringWithFormat:@"%@",filePath(@"downLoad")];
            NSURL *accessURL =[[NSURL fileURLWithPath:path] URLByAppendingPathComponent:[NSString stringWithFormat:@"%@.%@",model.fileID,model.fileType]];;

            NSData * data = [NSData dataWithContentsOfURL:accessURL];
            NSFileManager *fileManager = [NSFileManager defaultManager];
            BOOL isDir = NO;
            BOOL existed = [fileManager fileExistsAtPath:filePath(@"downOpen") isDirectory:&isDir];
            if (existed) {
                NSLog(@"Removed successfully");
                [fileManager removeItemAtPath:filePath(@"downOpen") error:nil];
            }
            [fileManager createDirectoryAtPath:filePath(@"downOpen") withIntermediateDirectories:YES attributes:nil error:nil];

            BOOL success  = [data writeToFile:filePath([NSString stringWithFormat:@"%@/%@",@"downOpen",model.fileName]) atomically:YES];
            if (success) {
                NSURL *accessURL1 =[NSURL fileURLWithPath:filePath([NSString stringWithFormat:@"%@/%@",@"downOpen",model.fileName])];;
                _documentController = [UIDocumentInteractionController interactionControllerWithURL:accessURL1];
                _documentController.name = model.fileName;
                [_documentController setDelegate:self];
             [_documentController presentOptionsMenuFromRect:self.view.bounds inView:self.view animated:YES];
            }

        }
    }

   
    
    
}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    UIView *headerView = [UIView new];
    headerView.backgroundColor = HEXCOLOR(0xececec);
    UILabel *label = [UILabel new];
    label.textColor = HEXCOLOR(0x333333);
    if (self.listArr.count== 2) {
        label.text = section==0? KLanguage(@"上传") :KLanguage(@"下载");
    }else{
        UBUploadModel *model =self.listArr[section][0];
        label.text = model.isDown? KLanguage(@"下载"):KLanguage(@"上传");

    }

    [headerView addSubview:label];
    [label mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(fontAuto(10));
        make.height.mas_equalTo(fontAuto(30));
    }];
    return headerView;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 30;
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

-(UIViewController *)documentInteractionControllerViewControllerForPreview:(UIDocumentInteractionController *)controller{
   return self;
}

-(CGRect)documentInteractionControllerRectForPreview:(UIDocumentInteractionController*)controller {
   return self.view.frame;
}
-(void)dealloc{
//    uploadChangeNotificationCenter
    //移除指定的通知，不然会造成内存泄露
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"uploadChangeNotificationCenter" object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    
}
-(FBBackUpView *)backUpView{
    if(!_backUpView){
        _backUpView = [[FBBackUpView alloc] initWithFrame:CGRectMake(fontAuto(10), K_NavBar_Height+fontAuto(10), VULSCREEN_WIDTH-fontAuto(20), fontAuto(80))];
        _backUpView.userInteractionEnabled = YES;
    }
    return _backUpView;
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
