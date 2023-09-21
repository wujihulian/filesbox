//
//  FBFileVersionManagentVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/4/12.
//

#import "FBFileVersionManagentVC.h"
#import "FBFileVersionCell.h"
#import "FBFromTopView.h"
#import "VULDocumentVC.h"
#import "VULPlayMp3VC.h"
#import "FBAddFileView.h"
#import "VULActionSheetView.h"
@interface FBFileVersionManagentVC ()<UIDocumentPickerDelegate,DZNEmptyDataSetSource,DZNEmptyDataSetDelegate>
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) UIImageView *lefImage;
@property (nonatomic, strong) VULButton *refreshBtn;
@property (nonatomic, strong) VULButton *upLoadBtn;
@property (nonatomic, strong) UIView *lineV;
@property (nonatomic, strong) UIView *lineV1;
@property (nonatomic, strong) NSMutableArray *dataArr; //
@property (nonatomic, strong) UIView *containerView;
@property (nonatomic, strong) TZImagePickerUtil *imagePicker;
@property (nonatomic, strong) UIDocumentPickerViewController *documentPickerView;
@property (nonatomic, strong) TZImagePickerUtil *videoPicker;

@property (nonatomic, strong) ZFPlayerController *player;
@property (nonatomic, strong) ZFAVPlayerManager *playerManager;
@property (nonatomic, strong) ZFPlayerControlView *controlView;/**< 视频播放器控制层 */
@property (nonatomic, copy) NSString *sourceID;;
@property (nonatomic,strong) NSMutableArray  *imageArray;

@end

@implementation FBFileVersionManagentVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationView.hidden = YES;
    [self.view addSubview:self.lefImage];
    [self.view addSubview:self.titleLabel];
    [self.view addSubview:self.refreshBtn];
    [self.view addSubview:self.upLoadBtn];
    [self.view addSubview:self.lineV];
    [self.view addSubview:self.lineV1];
    
    self.tableView.emptyDataSetSource = self;
    self.tableView.emptyDataSetDelegate = self;

    self.sourceID = self.model.parentID;
    [self.lineV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.top.left.mas_offset(0);
        make.height.mas_equalTo(0);
    }];
    [self.lefImage mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_offset(fontAuto(12));
        make.height.mas_equalTo(fontAuto(20));
        make.top.mas_equalTo(fontAuto(10));
    }];
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.lefImage.mas_right).offset(fontAuto(5));
        make.height.mas_equalTo(fontAuto(20));
        make.top.mas_equalTo(fontAuto(10));
    }];
    [self.lineV1 mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.left.mas_offset(0);
        make.height.mas_equalTo(1);
        make.top.mas_equalTo(fontAuto(39));
    }];
    [self.upLoadBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_offset(-fontAuto(12));
        make.height.mas_equalTo(fontAuto(26));
        make.width.mas_equalTo(fontAuto(110));
        make.top.mas_equalTo(fontAuto(7));
    }];
    [self.refreshBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(self.upLoadBtn.mas_left).offset(-fontAuto(10));
        make.height.mas_equalTo(fontAuto(26));
        make.width.mas_equalTo(fontAuto(26));
        make.top.mas_equalTo(fontAuto(7));
    }];
    [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.bottom.left.right.mas_equalTo(0);
        make.top.mas_equalTo(fontAuto(40));
    }];
    if (isPermissionWithModel(KLanguage(@"上传"), @[self.model])) {
        [self.upLoadBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_offset(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(26));
            make.width.mas_equalTo(fontAuto(110));
            make.top.mas_equalTo(fontAuto(7));
        }];
        [self.refreshBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(self.upLoadBtn.mas_left).offset(-fontAuto(10));
            make.height.mas_equalTo(fontAuto(26));
            make.width.mas_equalTo(fontAuto(26));
            make.top.mas_equalTo(fontAuto(7));
        }];
        self.upLoadBtn.hidden = NO;

    }else{
            self.upLoadBtn.hidden = YES;
            [self.upLoadBtn mas_makeConstraints:^(MASConstraintMaker *make) {
                make.right.mas_offset(-fontAuto(12));
                make.height.mas_equalTo(fontAuto(0));
                make.width.mas_equalTo(fontAuto(0));
                make.top.mas_equalTo(fontAuto(7));
            }];
            [self.refreshBtn mas_makeConstraints:^(MASConstraintMaker *make) {
                make.right.mas_offset(-fontAuto(12));
                make.height.mas_equalTo(fontAuto(26));
                make.width.mas_equalTo(fontAuto(26));
                make.top.mas_equalTo(fontAuto(7));
            }];
    }
    [self.tableView registerClass:[FBFileVersionCell class] forCellReuseIdentifier:@"FBFileVersionCell"];
    self.dataArr = [NSMutableArray array];
    self.tableView.estimatedRowHeight = fontAuto(120);

    [self gethistory];
    //注册键盘出现通知
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardDidShow:) name:UIKeyboardDidShowNotification object:nil];
    // 注册键盘隐藏通知
    [[NSNotificationCenter defaultCenter] addObserver:self selector: @selector(keyboardDidHide:) name:UIKeyboardDidHideNotification object:nil];
    [self setupRefreshHeader];
    // Do any additional setup after loading the view.
}
-(void)topRefreshing{
    [super topRefreshing];
    [self.dataArr removeAllObjects];
    [self gethistory];
}
    // Do any additional setup after loading the view.
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    // 注销键盘出现通知
    [[NSNotificationCenter defaultCenter]removeObserver:self name:UIKeyboardDidShowNotification object:nil];
    // 注销键盘隐藏通知
    [[NSNotificationCenter defaultCenter]removeObserver: self name:UIKeyboardDidHideNotification object: nil];
}
-(void)keyboardDidShow:(NSNotification *) notification{
    NSLog(@"键盘打开了");
}
-(void)keyboardDidHide: (NSNotification *) notification{
    NSLog(@"键盘关闭了");
    VULFileObjectModel *model1= nil;
    for (VULFileObjectModel *model in self.dataArr) {
        if (model.isDetail) {
            model1 = model;
            break;;
        }
    }
    [VULBaseRequest requestWithUrl:@"/api/disk/history/setDetail" params:@{@"detail":model1.detail,@"id":model1.id} requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
            [self clickRefreshBtn];
        }else{
           
        }
     
    }];
    
     
}
///api/disk/history/get
-(void)gethistory{
    [VULBaseRequest requestWithUrl:@"/api/disk/history/get" params:@{@"sourceID":self.model.sourceID} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        [self.tableView.mj_header endRefreshing];
        [self.tableView.mj_footer endRefreshing];
        [self dissmissHudView];
        if (request.success) {
            NSArray *fileList = request.data[@"fileList"];
            NSDictionary *current = request.data[@"current"];
            VULFileObjectModel *mode1 = [VULFileObjectModel modelWithDictionary:current];
            [self.dataArr addObject:mode1];

            
            for (NSDictionary *dic in fileList) {
                VULFileObjectModel *mode = [VULFileObjectModel modelWithDictionary:dic];
                [self.dataArr addObject:mode];
            }
            [self getImageData];
            [self.tableView reloadData];
            [self.tableView configEmptyViewWithHasData:self.dataArr.count > 0 hasError:!request.success insets:UIEdgeInsetsMake(0, 0, 0, 0) image:VULGetImage(@"no_data") info:nil reloadBlock:^{
                
            }];
        } else
            [self makeToast:request.message];
    }];
}
-(void)getImageData{
    NSArray *picArray = @[@"bmp",@"jpg",@"jpeg",@"png",@"gif",@"arw", @"mrw", @"erf", @"raf",@"cr2", @"nrw", @"nef", @"orf", @"rw2", @"pef", @"srf", @"dcr", @"kdc", @"dng",@"psd",@"webp"];

    for (VULFileObjectModel *model in  self.dataArr) {
        if ([picArray containsObject:model.fileType]) {
            [self.imageArray addObject:model];
        }
    }
}
#pragma mark - tableviewDelegate , tableviewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
  
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return self.dataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    FBFileVersionCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FBFileVersionCell" forIndexPath:indexPath];
    cell.separatorLine.hidden = YES;
    cell.version =  self.dataArr.count - indexPath.row;
    cell.row = indexPath.row;
    VULFileObjectModel *model = self.dataArr[indexPath.row];
    cell.model = model;
    [[cell.textField rac_textSignal] subscribeNext:^(NSString *_Nullable x) {
        model.detail = x;
    }];
    cell.clickMore = ^(NSInteger row) {
        [self operationWithRow:row];
    };
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
    [self openFileWithModel:self.dataArr[indexPath.row]];

}

- (UITableViewStyle )tableViewStyle {
    return UITableViewStyleGrouped;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.01;

}

-(void)operationWithRow:(NSInteger )row{
    
    [self.tableView endEditing:YES];
    NSArray *titleArr = @[KLanguage(@"打开"), KLanguage(@"下载"), KLanguage(@"设置为当前版本"),KLanguage(@"删除该版本"),KLanguage(@"添加版本说明"),KLanguage(@"删除所有版本记录")];
    NSArray *imageArr = @[@"icon_operation_open", @"icon_operation_download", @"icon_setting_version",@"icon_delete_version", @"icon_add_version",@"icon_allDelete_version"];
   
    if (row == 0) {
        titleArr = @[KLanguage(@"打开"), KLanguage(@"下载"),KLanguage(@"删除所有版本记录")];
       imageArr = @[@"icon_operation_open", @"icon_operation_download",@"icon_allDelete_version"];

    }
    FBFromTopView *top = [[FBFromTopView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, titleArr.count*BtnCell+K_BottomBar_Height)];
//    top.dataArray = [NSMutableArray arrayWithObject:self.dataArr[row]];
    top.index =0;
    top.lineArr = [NSMutableArray arrayWithArray: @[@2,@3]];
    top.titleArr = titleArr;
    top.iconArr = imageArr;
    zhPopupController *popup2 =[[zhPopupController alloc] initWithView:top size:CGSizeMake(VULSCREEN_WIDTH, top.height)];
    popup2.layoutType = zhPopupLayoutTypeBottom;
    popup2.presentationStyle = zhPopupSlideStyleFromBottom;
    popup2.maskAlpha = 0.35;
    top.clickViewBlock = ^(NSString * _Nonnull title) {
        [popup2 dismiss];
        
        if ([title isEqualToString:KLanguage(@"打开")]) {
            [self openFileWithModel:self.dataArr[row]];
            return;
        }
        if ([title isEqualToString:KLanguage(@"下载")]) {
            [self downFileWithModel:self.dataArr[row]];
            return;
        }
        if ([title isEqualToString:KLanguage(@"设置为当前版本")]) {
            [self setVersionWithModel:self.dataArr[row]];
            return;
        }
        if ([title isEqualToString:KLanguage(@"删除该版本")]) {
            [self removeVersionWithModel:self.dataArr[row]];
            return;
        }
        if ([title isEqualToString:KLanguage(@"删除所有版本记录")]) {
            [self removeAllVersionWithModel:self.dataArr[row]];
            return;
        }
        if ([title isEqualToString:KLanguage(@"添加版本说明")]) {
            VULFileObjectModel *model = self.dataArr[row];
            model.isDetail = YES;
            [self.tableView reloadData];
            FBFileVersionCell *cell = (FBFileVersionCell *)[self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:row inSection:0]];
            [cell.textField becomeFirstResponder];
            return;
        }
        
    };
    [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
}
-(void)removeAllVersionWithModel:(VULFileObjectModel *)model {
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:KLanguage(@"确定删除所有版本记录？") message:nil preferredStyle:UIAlertControllerStyleAlert];
         [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"确定") style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
             MJWeakSelf
             [VULBaseRequest requestWithUrl:@"/api/disk/history/delete" params:@{@"sourceID":model.sourceID} requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
                 if (request.success) {
                     [self clickRefreshBtn];
                     if (self.saveAndRefreshBlock) {
                         self.saveAndRefreshBlock();
                     }
                 }else{
                    
                 }
              
             }];
        }]];
         [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {

                   NSLog(@"取消");

           }]];
        [self presentViewController:alertController animated:YES completion:nil];
}
-(void)removeVersionWithModel:(VULFileObjectModel *)model {
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:KLanguage(@"删除该版本？") message:nil preferredStyle:UIAlertControllerStyleAlert];
         [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"确定") style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
             MJWeakSelf
             [VULBaseRequest requestWithUrl:@"/api/disk/history/delete" params:@{@"id":model.id} requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
                 if (request.success) {
                     [self clickRefreshBtn];
                     if (self.saveAndRefreshBlock) {
                         self.saveAndRefreshBlock();
                     }
                 }else{
                    
                 }
              
             }];
        }]];
         [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {

                   NSLog(@"取消");

           }]];
        [self presentViewController:alertController animated:YES completion:nil];
}
-(void)setVersionWithModel:(VULFileObjectModel *)model {

    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:KLanguage(@"确定要回滚到该版本？") message:nil preferredStyle:UIAlertControllerStyleAlert];
         [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"确定") style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
             MJWeakSelf
             [VULBaseRequest requestWithUrl:@"/api/disk/history/setVersion" params:@{@"id":model.id} requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
                 if (request.success) {
                     [self clickRefreshBtn];
                     if (self.saveAndRefreshBlock) {
                         self.saveAndRefreshBlock();
                     }
                 }else{
                    
                 }
              
             }];
        }]];
         [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {

                   NSLog(@"取消");

           }]];
        [self presentViewController:alertController animated:YES completion:nil];

}
-(NSMutableArray *)imageArray{
    if (!_imageArray) {
        _imageArray =[NSMutableArray array];;
    }
    return _imageArray;
}
#pragma mark -打开文件
-(void)openFileWithModel:(VULFileObjectModel *)model {
    
//    if (!isPermissionWithModel(KLanguage(@"查看"),@[model])) {
//        [kWindow makeToast:KLanguage(@"暂无权限")];
//        return;
//    }
   

    NSArray *picArray = @[@"bmp",@"jpg",@"jpeg",@"png",@"gif",@"arw", @"mrw", @"erf", @"raf",@"cr2", @"nrw", @"nef", @"orf", @"rw2", @"pef", @"srf", @"dcr", @"kdc", @"dng",@"psd",@"webp"];

    if ([picArray containsObject:model.fileType]) {
        [XLPhotoBrowser showPhotoBrowserWithImages:[self getImageUrlArr] currentImageIndex:[self backIndexWithModel:model]];

        
        return;
    }
    if (self.openDetialWithModel) {
        self.openDetialWithModel(model);
        return;
    }
    
    
    

}
//获取图片下标
-(NSInteger )backIndexWithModel:(VULFileObjectModel *)model{
    NSInteger row = 0;
    for (int i = 0;i<self.imageArray.count ;i++) {
        VULFileObjectModel *infoModel = self.imageArray[i];
        if (model.fileID.intValue == infoModel.fileID.intValue) {
            row = i;
            break;
        }
    }
    return row;
}
//获取所有图片
-(NSMutableArray *)getImageUrlArr{
    NSMutableArray *arr = [NSMutableArray array];
    for (int i = 0;i<self.imageArray.count ;i++) {
        VULFileObjectModel *model = self.imageArray[i];
        if ([model.fileType isEqualToString:@"gif"] ) {
            NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.downloadUrl];
            url = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
            [arr addObject:url];
        }else{
            
            NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(model.path,0,model.fileType)];
//            url = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
            [arr addObject:url];
        }
    }
    return arr;
}

-(void)downFileWithModel:(VULFileObjectModel *)model{

    NSDictionary *dic;
    dic = @{
        @"busType":@"cloud",
        @"path":@"info",
        @"sourceID":model.sourceID
    };
    MJWeakSelf
    [VULBaseRequest requestWithUrl:@"/api/disk/preview" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
            NSString *name =  [NSString stringWithFormat:@"%@",request.data[@"name"]];
            NSString *downloadUrl = request.data[@"downloadUrl"];
            NSString *fileID =  [NSString stringWithFormat:@"%@",request.data[@"fileID"]];
            NSString *fileType =  [NSString stringWithFormat:@"%@",request.data[@"fileType"]];
            NSString *size =  [NSString stringWithFormat:@"%@",request.data[@"size"]];

//            // 此处是截取的下载地址，可以自己根据服务器的视频名称来赋值
            NSString *Url = [NSString stringWithFormat:@"%@%@",ChooseUrl,downloadUrl];
            Url = [Url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
            UBUploadModel *downloadModel = [UBUploadModel new];
            downloadModel.fileUrl = [NSURL URLWithString:Url];
            downloadModel.fileName =name;
            downloadModel.isDown = YES;
            downloadModel.fileType = fileType;
            downloadModel.fileID = fileID;
            downloadModel.filePath = fileID;
            downloadModel.path = model.path;
            downloadModel.fileSize =size.integerValue;
            [FBDownloadFileAllManage.sharedManager addDownloadFileWithModel:downloadModel];
            [VULNotificationCenter postNotificationName:@"uploadChangeNotificationCenter" object:nil];

        }else{
           
        }
     
    }];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
#pragma mark - JXCategoryListContentViewDelegate
- (UIView *)listView {
    return self.view;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"历史版本") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.numberOfLines = 1;
    }
    return _titleLabel;
}
-(UIImageView *)lefImage{
    if (!_lefImage) {
        _lefImage = [UIImageView new];
        _lefImage.image = VULGetImage(@"icon_history_version");
        _lefImage.contentMode = UIViewContentModeScaleAspectFit;
   }
    return _lefImage;
}
-(VULButton *)refreshBtn{
    if (!_refreshBtn) {
        _refreshBtn = [VULButton new];
        _refreshBtn.imageView.contentMode = UIViewContentModeScaleAspectFit;
        [_refreshBtn addTarget:self action:@selector(clickRefreshBtn) forControlEvents:UIControlEventTouchUpInside];
        _refreshBtn.layer.masksToBounds = YES;
        _refreshBtn.layer.cornerRadius = 5;
        _refreshBtn.layer.borderWidth = 1;
        _refreshBtn.layer.borderColor = HEXCOLOR(0xececec).CGColor;
        UIImage *image = VULGetImage(@"icon_refresh_version");
        _refreshBtn.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
        [_refreshBtn setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
        [_refreshBtn setTintColor:HEXCOLOR(0x999999)];
        _refreshBtn.imageView.width = fontAuto(20);
        _refreshBtn.imageView.height = fontAuto(20);
        [_refreshBtn setImageEdgeInsets:UIEdgeInsetsMake(fontAuto(4), fontAuto(4), fontAuto(4), fontAuto(4))];

    }
    return _refreshBtn;
}
-(void)clickRefreshBtn{
    [self.tableView.mj_header beginRefreshing];
}
-(VULButton *)upLoadBtn{
    if (!_upLoadBtn) {
        _upLoadBtn = [VULButton new];
        [_upLoadBtn setTitle:KLanguage(@"上传新版本") forState:UIControlStateNormal];
        [_upLoadBtn setTitleColor:HEXCOLOR(0x999999) forState:UIControlStateNormal];
        [_upLoadBtn addTarget:self action:@selector(clickUpLoadBtn) forControlEvents:UIControlEventTouchUpInside];
        _upLoadBtn.layer.masksToBounds = YES;
        _upLoadBtn.layer.cornerRadius = 5;
        _upLoadBtn.layer.borderWidth = 1;
        _upLoadBtn.layer.borderColor = HEXCOLOR(0xececec).CGColor;
        UIImage *image = VULGetImage(@"icon_upload_version");
        _upLoadBtn.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
        [_upLoadBtn setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
        [_upLoadBtn setTintColor:HEXCOLOR(0x999999)];
        _upLoadBtn.titleLabel.font = [UIFont yk_pingFangRegular:15];
        [_upLoadBtn setImageEdgeInsets:UIEdgeInsetsMake(fontAuto(4), fontAuto(4), fontAuto(4), fontAuto(4))];

    }
    
    return _upLoadBtn;
}
-(void)clickUpLoadBtn{
    NSArray *picArray = @[@"bmp",@"jpg",@"jpeg",@"png",@"gif",@"arw", @"mrw", @"erf", @"raf",@"cr2", @"nrw", @"nef", @"orf", @"rw2", @"pef", @"srf", @"dcr", @"kdc", @"dng",@"psd"];
    if ([picArray containsObject:self.model.fileType]) {
    
        
        VULActionSheetView *add = [[VULActionSheetView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, fontAuto(47*3)+K_BottomBar_Height) actionTitle:@[KLanguage(@"拍摄"),KLanguage(@"图片"),KLanguage(@"本地文件")] isCenter:YES];
        zhPopupController *popup = [[zhPopupController alloc] initWithView:add size:CGSizeMake(VULSCREEN_WIDTH, add.height)];
        popup.layoutType = zhPopupLayoutTypeBottom;
        popup.presentationStyle = zhPopupSlideStyleFromBottom;
        popup.maskAlpha = 0.35;
        [popup showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
        
        add.confirmBook = ^(NSInteger index) {
            NSString *title =@[KLanguage(@"拍摄"),KLanguage(@"图片"),KLanguage(@"本地文件")][index];
            if ([title isEqualToString:KLanguage(@"拍摄")]) {
                [popup dismiss];
                [self.imagePicker.selectedPhotos removeAllObjects];
                [self.imagePicker.selectedAssets removeAllObjects];
                self.imagePicker.imagePickerVc.mediaTypes = [NSArray arrayWithObjects: kUTTypeImage, nil];
                self.imagePicker.imagePickerVc.videoQuality = UIImagePickerControllerQualityTypeMedium;
                self.imagePicker.imagePickerVc.videoMaximumDuration = 90.0;
                [self.imagePicker takePhoto];
                return;
            }
            if ([title isEqualToString:KLanguage(@"图片")]) {
                [popup dismiss];
                [self.imagePicker.selectedPhotos removeAllObjects];
                [self.imagePicker.selectedAssets removeAllObjects];
                [self.imagePicker pushTZImagePickerController];
                return;
            }
            if ([title isEqualToString:KLanguage(@"本地文件")]) {
                [popup dismiss];
                if (@available(iOS 11, *)) {
                    [UIScrollView appearance].contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentAlways;
                }
                self.documentPickerView = [[UIDocumentPickerViewController alloc] initWithDocumentTypes:@[@"com.compuserve.gif",@"public.png",@"public.jpeg"] inMode:UIDocumentPickerModeOpen];
                self.documentPickerView.delegate = self;
                self.documentPickerView.modalPresentationStyle = UIModalPresentationFullScreen;
                [self presentViewController:       self.documentPickerView animated:YES completion:nil];
            }
            

        };
       
        return;
    }
    
    NSArray *videoArray = @[@"mp4",@"rm",@"rmvb",@"3gp",@"mov",@"m4v",@"wmv",@"asf",@"asx",@"avi",@"dat",@"mkv",@"flv",@"vob",@"webm",@"mpg"];
    if ([videoArray containsObject:self.model.fileType]) {
        
        
        VULActionSheetView *add = [[VULActionSheetView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, fontAuto(47*3)+K_BottomBar_Height) actionTitle:@[KLanguage(@"拍摄"),KLanguage(@"视频"),KLanguage(@"本地文件")] isCenter:YES];
        zhPopupController *popup = [[zhPopupController alloc] initWithView:add size:CGSizeMake(VULSCREEN_WIDTH, add.height)];
        popup.layoutType = zhPopupLayoutTypeBottom;
        popup.presentationStyle = zhPopupSlideStyleFromBottom;
        popup.maskAlpha = 0.35;
        [popup showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
        
        add.confirmBook = ^(NSInteger index) {
            NSString *title =@[KLanguage(@"拍摄"),KLanguage(@"视频"),KLanguage(@"本地文件")][index];
            if ([title isEqualToString:KLanguage(@"拍摄")]) {
                [popup dismiss];
                [self.imagePicker.selectedPhotos removeAllObjects];
                [self.imagePicker.selectedAssets removeAllObjects];
                self.imagePicker.imagePickerVc.mediaTypes = [NSArray arrayWithObjects: @"public.movie", nil];
                self.imagePicker.imagePickerVc.videoQuality = UIImagePickerControllerQualityTypeMedium;
                self.imagePicker.imagePickerVc.videoMaximumDuration = 90.0;
                [self.imagePicker takePhoto];
                return;
            }
            if ([title isEqualToString:KLanguage(@"视频")]) {
                [popup dismiss];
              
                [self.videoPicker.selectedPhotos removeAllObjects];
                [self.videoPicker.selectedAssets removeAllObjects];
                [self.videoPicker pushTZImagePickerController];
                return;
            }
            if ([title isEqualToString:KLanguage(@"本地文件")]) {
                [popup dismiss];
                if (@available(iOS 11, *)) {
                    [UIScrollView appearance].contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentAlways;
                }
                self.documentPickerView = [[UIDocumentPickerViewController alloc] initWithDocumentTypes:@[@"public.avi"] inMode:UIDocumentPickerModeOpen];
                self.documentPickerView.delegate = self;
                self.documentPickerView.modalPresentationStyle = UIModalPresentationFullScreen;
                [self presentViewController:       self.documentPickerView animated:YES completion:nil];
                return;
            }
            

        };
       
        return;
     

    }

    NSArray *type = @[@"public.item"];
//    "com.microsoft.word.doc",
//    "org.openxmlformats.wordprocessingml.document",
//    "com.microsoft.excel.xls",
//    "org.openxmlformats.spreadsheetml.sheet"
//    "com.microsoft.powerpoint.​ppt"
//    "org.openxmlformats.presentationml.presentation",
    if ([self.model.fileType isEqualToString:@"pdf"]) {
        type = @[@"com.adobe.pdf"];
    }else if([self.model.fileType isEqualToString:@"ppt"] || [self.model.fileType isEqualToString:@"pptx"]) {
        type = @[@"com.microsoft.powerpoint.​ppt" ,@"org.openxmlformats.presentationml.presentation"];
    }
    else if([self.model.fileType isEqualToString:@"doc"] || [self.model.fileType isEqualToString:@"docx"]) {
        type = @[@"com.microsoft.word.doc" ,@"org.openxmlformats.wordprocessingml.document"];

    }else if([self.model.fileType isEqualToString:@"xls"] ||  [self.model.fileType isEqualToString:@"xlsx"]) {
        type = @[@"com.microsoft.excel.xls" ,@"org.openxmlformats.spreadsheetml.sheet"];

    }
    if (@available(iOS 11, *)) {
        [UIScrollView appearance].contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentAlways;
    }
    self.documentPickerView = [[UIDocumentPickerViewController alloc] initWithDocumentTypes:type inMode:UIDocumentPickerModeOpen];
    self.documentPickerView.delegate = self;
    self.documentPickerView.modalPresentationStyle = UIModalPresentationFullScreen;
    [self presentViewController:       self.documentPickerView animated:YES completion:nil];

}
-(UIView *)lineV{
    if (!_lineV) {
        _lineV  = [UIView new];
        _lineV.backgroundColor = HEXCOLOR(0xececec);
    }
    return _lineV;
}
-(UIView *)lineV1{
    if (!_lineV1) {
        _lineV1  = [UIView new];
        _lineV1.backgroundColor = HEXCOLOR(0xececec);
    }
    return _lineV1;
}
#pragma mark - 视频播放器
- (ZFPlayerControlView *)controlView {
    if (!_controlView) {
        _controlView = [ZFPlayerControlView new];
        _controlView.fastViewAnimated = YES;
    }
    return _controlView;
}
- (UIView *)containerView {
    if (!_containerView) {
        _containerView = [[UIView alloc] init];
    }
    return _containerView;
}
- (TZImagePickerUtil *)imagePicker {
    if (!_imagePicker) {
        _imagePicker = [[TZImagePickerUtil alloc] init];
        _imagePicker.imagePickerCofing = ^(TZImagePickerController * _Nonnull pickerController) {
            pickerController.maxImagesCount = 1;
            pickerController.allowPickingVideo = YES;
        };
        _imagePicker.selectedBolck = ^{
            [self uploadImage];
        };
        [_imagePicker setImagePickerCofing:^(TZImagePickerController *_Nonnull pickerController) {
            pickerController.maxImagesCount = 1;
            pickerController.allowPickingVideo = NO;
        }];
    }
    return _imagePicker;
}
- (void)uploadImage {
    if (self.imagePicker.selectedAssets.count > 0) {
        PHAsset *asset = self.imagePicker.selectedAssets[0];
        if (asset.mediaType == PHAssetMediaTypeImage) {
            NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
            [formatter setDateFormat:@"yyyy-MM-dd-HH:mm:ss"];
            NSString *key = [[formatter stringFromDate:[NSDate date]] stringByAppendingString:@".jpg"];
            
            // 解决图片偏转90度的问题
            UIImage *image = self.imagePicker.selectedPhotos[0] ;
            if(image.imageOrientation != UIImageOrientationUp) {
                UIGraphicsBeginImageContext(image.size);
                [image drawInRect:CGRectMake(0, 0, image.size.width, image.size.height)];
                image = UIGraphicsGetImageFromCurrentImageContext();
                UIGraphicsEndImageContext();
            }
            [[SDImageCache sharedImageCache] storeImage:image forKey:key toDisk:YES completion:^{
                NSString *filePath = [[SDImageCache sharedImageCache] cachePathForKey:key];

                [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:filePath] sourceID:self.sourceID pathId:self.model.sourceID isPic:YES success:^(bool sucess) {
     
                    if (sucess) {
//                        self.currentPage =1;
//                        [self requestList];
                        [self getCurrentInfo];
                    }

                }];
            }];
        }
        else if (asset.mediaType == PHAssetMediaTypeVideo) {
            [[TZImageManager manager] getVideoOutputPathWithAsset:asset success:^(NSString *outputPath) {
                if (!outputPath) {
                    return;
                }
                
                [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:outputPath]
                                           sourceID:self.sourceID pathId:self.model.sourceID isPic:NO  success:^(bool sucess) {
                    if (sucess) {
                        [self getCurrentInfo];

                    }

                }];
     
                
                
            } failure:^(NSString *errorMessage, NSError *error) {
              
            }];
        }
    }
}
- (TZImagePickerUtil *)videoPicker {
    if (!_videoPicker) {
        _videoPicker = [TZImagePickerUtil new];
        WeakSelf(self)
        [_videoPicker setSelectedBolck:^{
           
            PHAsset *asset = [weakself.videoPicker.selectedAssets objectAtIndex:0];
            [[TZImageManager manager] getVideoOutputPathWithAsset:asset success:^(NSString *outputPath) {
                if (!outputPath) {
                    return;
                }
                
                [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:outputPath]
                                           sourceID:self.sourceID pathId:self.model.sourceID isPic:NO  success:^(bool sucess) {
                    if (sucess) {
                        [self getCurrentInfo];

                    }

                }];
     
                
                
            } failure:^(NSString *errorMessage, NSError *error) {
              
            }];
        }];
        [_videoPicker setImagePickerCofing:^(TZImagePickerController *_Nonnull pickerController) {
            pickerController.maxImagesCount = 1;
            pickerController.allowTakeVideo = NO;
            pickerController.allowPickingVideo = YES;
            pickerController.allowPickingImage = NO;
            pickerController.allowTakePicture = NO;
            pickerController.allowPickingMultipleVideo = YES;
        }];
    }
    return _videoPicker;
}
#pragma mark - UIDocumentPickerDelegate
- (void)documentPicker:(UIDocumentPickerViewController *)controller didPickDocumentAtURL:(NSURL *)url {
    BOOL fileUrlAuthozied = [url startAccessingSecurityScopedResource];
    if(fileUrlAuthozied) {
        NSFileCoordinator * fileCoordinator = [[NSFileCoordinator alloc] init];
        NSError * error;
        [fileCoordinator coordinateReadingItemAtURL:url options:0 error:&error byAccessor:^(NSURL *newURL) {

            [ChunkUploader writeDataWithModel:newURL
                                       sourceID:self.sourceID pathId:self.model.sourceID isPic:NO  success:^(bool sucess) {
                if (sucess) {
                    [self getCurrentInfo];

                }
                [url stopAccessingSecurityScopedResource];

            }];
           
//
        }];
    }
    else {
        //Error handling
    }
}
-(void)getCurrentInfo{
//https://dev.filesbox.cn/api/disk/list/path?keyword=&sourceID=5&currentPage=1&pageSize=500
    NSMutableDictionary *progressData  = [DownloadProgress sharedInstance].progressData;
    NSDictionary *dataDic =progressData[self.model.sourceID];
    
    self.model.fileID = [NSString stringWithFormat:@"%@",dataDic[@"fileID"]];
    self.model.path = [NSString stringWithFormat:@"%@",dataDic[@"path"]];
    self.model.thumb = [NSString stringWithFormat:@"%@",dataDic[@"thumb"]];
    if (self.uploadRefreshBlock) {
        self.uploadRefreshBlock(self.model);
    }}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
