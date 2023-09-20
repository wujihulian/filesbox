//
//  FBZIPLookViewController.m
//  FilesBox
//
//  Created by 无极互联 on 2023/5/4.
//

#import "FBZIPLookViewController.h"
#import "FBFileAttributeTopView.h"
#import "FBFromTopView.h"
#import "XLPhotoBrowser.h"
#import "FBFileNodeCell.h"
#import "VULInputTitleView.h"
#import "FBHomeViewController.h"
#import "VULDocumentVC.h"
static NSString *const fileNodeCell = @"FBFileNodeCell";

@interface FBZIPLookViewController ()<UITableViewDelegate,FBFileNodeCellDelegate,UITableViewDataSource>
@property (nonatomic, strong) FBFileAttributeTopView *viewTop;

@property (nonatomic, strong) VULButton *clostBtn;
@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) NSMutableArray *treeNodes;
@property (nonatomic, strong) UIView *bgView1;
@property (nonatomic, strong) VULLabel *sizeLabel;

@end

@implementation FBZIPLookViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationView.hidden = YES;
    [self.view addSubview:self.viewTop];
    [self.viewTop addSubview:self.clostBtn];
    [self.viewTop addSubview:self.bgView1];
    [self.viewTop addSubview:self.titleLabel];
    [self.viewTop addSubview:self.sizeLabel];

    
    self.viewTop.model = self.model;
    [_clostBtn setImage:VULGetImage(@"file_attribute_close") forState:UIControlStateNormal];
    [self.clostBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(K_StatusBar_Height+fontAuto(17.5));
        make.width.height.mas_equalTo(fontAuto(30));
        make.right.mas_equalTo(-fontAuto(10));
    }];
    [self.bgView1 mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.viewTop.mas_bottom);
        make.height.mas_equalTo(fontAuto(30));
        make.right.mas_equalTo(-fontAuto(0));
        make.left.mas_equalTo(fontAuto(0));

    }];
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.viewTop.mas_bottom);
        make.height.mas_equalTo(fontAuto(30));
        make.right.mas_equalTo(-fontAuto(112));
        make.left.mas_equalTo(fontAuto(10));

    }];
    [self.sizeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.viewTop.mas_bottom);
        make.height.mas_equalTo(fontAuto(30));
        make.right.mas_equalTo(-fontAuto(52));
        make.width.mas_equalTo(fontAuto(50));

    }];
    [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.titleLabel.mas_bottom);
        make.bottom.left.right.mas_equalTo(fontAuto(0));
    }];


    self.treeNodes = [NSMutableArray array];
    [self requestList];
    
    // Do any additional setup after loading the view.
}
-(void)requestList{
    
    [VULBaseRequest requestWithUrl:@"/api/disk/unzipList" params:@{@"sourceID":self.model.sourceID} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
           
           [self dissmissHudView];
           if (request.success) {
               VULFileZIPObjectModel *zipModel = [VULFileZIPObjectModel modelWithDictionary:request.data];
               [self.treeNodes addObject:zipModel];
               [self setLevel];
               [self.tableView reloadData];
               [((AppDelegate *)[UIApplication sharedApplication].delegate).watermarkView addWaterMarkView];

           } else
               [self makeToast:request.message];
           [self.tableView.mj_header endRefreshing];
           [self.tableView.mj_footer endRefreshing];
       }];
    
}
-(void)setLevel{

  for (VULFileZIPObjectModel * model in _treeNodes) {
          model.isExpanded = YES;
          model.fileType = self.model.fileType;
          [self setLevelWithModel:model withLevel:0];
      }
    [self.tableView reloadData];
}
-(void)setLevelWithModel:(VULFileZIPObjectModel *)model withLevel:(NSInteger)level{
    model.level = level;

    if (model.childList.count>0) {
        for (VULFileZIPObjectModel *model1 in model.childList) {
            model1.level = level+1;
            if (model1.childList.count>0) {
                [self setLevelWithModel:model1 withLevel:model1.level];
            }
        }
    }
}
#pragma mark - UITableViewDelegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self getVisibleNodes].count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
  
    FBFileNodeCell *cell = [FBFileNodeCell dequeueReusableCellWithTableView:tableView reuseIdentifier:fileNodeCell];
    cell.separatorLine.hidden  = YES;
    VULFileZIPObjectModel *node = [self getVisibleNodes][indexPath.row];
    cell.delegate = self;
    cell.zipModel = node;
    cell.clickMore = ^(VULFileZIPObjectModel *model) {
        [self operationWithRow:model];
    };
      return cell;
}
-(void)operationWithRow:(VULFileZIPObjectModel *)model{
    
    [self.tableView endEditing:YES];
    NSArray *titleArr = @[KLanguage(@"打开"), KLanguage(@"解压到...")];
    NSArray *imageArr = @[@"icon_operation_open", @"icon_operation_unZip"];
    if (model.level == 0) {
         titleArr  = @[KLanguage(@"解压到当前"),KLanguage(@"解压到文件夹"), KLanguage(@"解压到..."),KLanguage(@"属性")];
        imageArr = @[@"icon_operation_unZip",@"icon_operation_unZip",@"icon_operation_unZip",@"icon_operation_attribute"];

    }else{
        
        if (model.directory.boolValue) {
            titleArr = @[KLanguage(@"解压到...")];
           imageArr = @[@"icon_operation_unZip"];

        }else{
            NSArray *tarArr = @[@"tar",@"zip",@"gzip",@"bz2",@"rar",@"7z",@"gz",@"iso",@"ar",@"bz",@"xz",@"arj"];
            NSArray *iconArr = [model.originName componentsSeparatedByString:@"."];
            if([tarArr containsObject:[iconArr lastObject]]){
                titleArr = @[KLanguage(@"解压到...")];
               imageArr = @[@"icon_operation_unZip"];
            }

        }
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
        if (model.level == 0) {
            if (isPermissionWithModel(KLanguage(@"解压到..."),@[self.model])) {
                
                [self clickClostBtn];
                if(self.selectOperationWithTitle){
                    self.selectOperationWithTitle(title,self.model);
                }
                
            }else{
                [kWindow makeToast:KLanguage(@"暂无权限")];
            }
            return;

        }
        if ([title isEqualToString: KLanguage(@"解压到...")]) {
            if (isPermissionWithModel(KLanguage(@"解压到..."),@[self.model])) {
                [self getUnzipWithModel:model];
            }else{
                [kWindow makeToast:KLanguage(@"暂无权限")];
            }
            return;
        }
        if([title isEqualToString:KLanguage(@"打开")]){
//
            if(model.encrypted){
                [self reNameWithModel:model];
            }else{
                [self openWithModel:model wtihText:@"" isPass:NO];
            }
            
//            [VULBaseRequest requestWithUrl:@"api/disk/checkIsEncrypted" params:@{@"sourceID":self.model.sourceID} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
//
//                   [self dissmissHudView];
//                   if (request.success) {
//                       NSString *data = request.data;
//                       if(data.boolValue){
//                           [self reNameWithModel:model];
//                       }else{
//                           [self openWithModel:model wtihText:@"" isPass:NO];
//                       }
//
//                   } else
//                       [self makeToast:request.message];
//               }];
            return;
        }

    };
    [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
}
#pragma mark -重命名

- (void)reNameWithModel:(VULFileZIPObjectModel *)model{
    VULInputTitleView *view = [[VULInputTitleView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_HEIGHT) title:KLanguage(@"该压缩包需要密码")];
    view.textField.placeholder = KLanguage(@"请输入密码");
    view.alertInputViewBlock = ^(NSString * _Nonnull text) {
        
        [view hiddenView];
        [self openWithModel:model wtihText:text isPass:YES];

    
    };
    [view showInView];
}
-(void)openWithModel:(VULFileZIPObjectModel *)model wtihText:(NSString *)text isPass:(BOOL )flag {

//    "jpg", "jpeg", "png", "gif", "bmp", "ico", "svg", "webp", "tif", "tiff", "cdr", "svgz", "xbm", "eps", "pjepg", "heic", "raw", "psd", "ai"
    
    NSArray *picArray = @[@"bmp",@"jpg",@"jpeg",@"png",@"gif",@"BMP",@"JPG",@"JPEG",@"PNG",@"GIF"];
    NSArray *iconArr = [model.originName componentsSeparatedByString:@"."];
    NSString *fileType = [iconArr lastObject];
    if ([picArray containsObject:fileType]) {
        
        NSDictionary *dic = @{@"sourceID":self.model.sourceID,@"fullName":model.fileName,@"index":model.index};
        if(flag){
            dic=@{@"sourceID":self.model.sourceID,@"fullName":model.fileName,@"index":model.index,@"password":text};
        }
        [VULBaseRequest requestWithUrl:@"api/disk/unzipList" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
               
               [self dissmissHudView];
               if (request.success) {
                   NSString *data = request.data;
                   NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,data];
                   url = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
                    [XLPhotoBrowser showPhotoBrowserWithImages:@[url] currentImageIndex:0];

      
               } else
                   [self makeToast:request.message];
           }];
        return;
    }
  
    NSArray *docArray = @[@"doc",@"docx",@"ppt",@"pptx",@"pdf",@"xls",@"xlsx",@"pps",@"wps"];

    if ([docArray containsObject:fileType]) {
        NSDictionary *dic = @{@"sourceID":self.model.sourceID,@"fullName":model.fileName,@"index":model.index};
        if(flag){
            dic=@{@"sourceID":self.model.sourceID,@"fullName":model.fileName,@"index":model.index,@"password":text};
        }
        [VULBaseRequest requestWithUrl:@"api/disk/unzipList" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
               
               [self dissmissHudView];
               if (request.success) {
                   NSString *data = request.data;
//                   NSString *url = [NSString stringWithFormat:@"%@",data];
//                   url = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
                   VULDocumentVC *vc = [[VULDocumentVC alloc] init];
                   vc.isZip = YES;
                   vc.title = model.fullName;
                   vc.pptPreviewUrl = data;
                   [self presentViewController:vc animated:YES completion:nil];

               } else
                   [self makeToast:request.message];
           }];
        return;
    }

    [[UIApplication sharedApplication].keyWindow makeToast: KLanguage(@"无法预览当前文件") duration:1 position:@"center"];

    
}
- (NSString *)URLEncodedString:(NSString *)str
{
    // CharactersToBeEscaped = @":/?&=;+!@#$()~',*";
    // CharactersToLeaveUnescaped = @"[].";
    
    NSString *unencodedString = str;
    NSString *encodedString = (NSString *)
    CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault,
                                                              (CFStringRef)unencodedString,
                                                              NULL,
                                                              (CFStringRef)@"!*'();:@&=+$,/?%#[]",
                                                              kCFStringEncodingUTF8));
    
    return encodedString;
}
-(void)getUnzipWithModel:(VULFileZIPObjectModel *)model {
    NSArray *titleArr  = @[KLanguage(@"解压到当前"), KLanguage(@"解压到...")];
    NSArray *imageArr = @[@"icon_operation_unZip",@"icon_operation_unZip",@"icon_operation_unZip"];

    FBFromTopView *top = [[FBFromTopView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, titleArr.count*BtnCell+K_BottomBar_Height)];
    top.index =0;
    top.titleArr = titleArr;
    top.iconArr = imageArr;
    zhPopupController *popup2 =[[zhPopupController alloc] initWithView:top size:CGSizeMake(VULSCREEN_WIDTH, top.height)];
    popup2.layoutType = zhPopupLayoutTypeBottom;
    popup2.presentationStyle = zhPopupSlideStyleFromBottom;
    popup2.maskAlpha = 0.35;
    top.clickViewBlock = ^(NSString * _Nonnull title) {
        [popup2 dismiss];
        if ([title isEqualToString: KLanguage(@"解压到当前")]) {
                [self upZipWithModel:model isPassword:model.encrypted];

//            [VULBaseRequest requestWithUrl:@"api/disk/checkIsEncrypted" params:@{@"sourceID":self.model.sourceID} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
//
//                   [self dissmissHudView];
//                   if (request.success) {
//                       NSString *data = request.data;
//                       [self upZipWithModel:model isPassword:data.boolValue];
//                   } else
//                       [self makeToast:request.message];
//               }];
            return;
        }
        if ([title isEqualToString: KLanguage(@"解压到...")]) {
            
//            NSString *data = request.data;
            model.ispassWord = model.encrypted;
            model.sourceID = self.model.sourceID;
            FBHomeViewController *vc = [FBHomeViewController new];
            vc.isMove = YES;
            vc.flag = self.flag;
            vc.icon = @"ZIP";
            vc.operation = @"ZIP";
            vc.isZipModel = model;
            vc.saveAndRefreshBlock = ^{
//                    weakSelf.sourceID =   weakSelf.fitstSourceID;
                if(self.saveAndRefreshBlock){
                    self.saveAndRefreshBlock();
                }
            };
            [self presentViewController:vc animated:YES completion:nil];
//            [VULBaseRequest requestWithUrl:@"api/disk/checkIsEncrypted" params:@{@"sourceID":self.model.sourceID} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
//
//                   [self dissmissHudView];
//                   if (request.success) {
//                       NSString *data = request.data;
//                       model.ispassWord = data.boolValue;
//                       model.sourceID = self.model.sourceID;
//                       FBHomeViewController *vc = [FBHomeViewController new];
//                       vc.isMove = YES;
//                       vc.flag = self.flag;
//                       vc.icon = @"ZIP";
//                       vc.operation = @"ZIP";
//                       vc.isZipModel = model;
//                       vc.saveAndRefreshBlock = ^{
//       //                    weakSelf.sourceID =   weakSelf.fitstSourceID;
//                           if(self.saveAndRefreshBlock){
//                               self.saveAndRefreshBlock();
//                           }
//                       };
//                       [self presentViewController:vc animated:YES completion:nil];
//                   } else
//                       [self makeToast:request.message];
//               }];
            return;
        }
        
    };
    [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
}
-(void)upZipWithModel:(VULFileZIPObjectModel *)model isPassword:(BOOL)flag{
    if(flag){
        VULInputTitleView *view = [[VULInputTitleView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_HEIGHT) title:KLanguage(@"该压缩包需要密码")];
        view.textField.placeholder = KLanguage(@"请输入密码");
        view.alertInputViewBlock = ^(NSString * _Nonnull text) {
            
            [view hiddenView];
            [self unZipWtihModel:model password:text isPassword:flag];
        
        };
        [view showInView];
    }else{
        [self unZipWtihModel:model password:@"" isPassword:flag];

    }
}
-(void)unZipWtihModel:(VULFileZIPObjectModel *)model password:(NSString *)passWord isPassword:(BOOL)flag{
    NSDictionary *dic = @{@"sourceID":self.model.sourceID,@"fullName":model.fileName,@"index":model.index,@"directory":@(model.directory.boolValue),@"sourceIDTo":self.model.parentID};
    if(flag){
        dic = @{@"sourceID":self.model.sourceID,@"fullName":model.fileName,@"index":model.index,@"directory":@(model.directory.boolValue),@"sourceIDTo":self.model.parentID,@"password":passWord};
    }
    [VULBaseRequest requestWithUrl:@"/api/disk/unZip" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
           
           [self dissmissHudView];
           if (request.success) {
               if(self.saveAndRefreshBlock){
                   self.saveAndRefreshBlock();
               }
               [self clickClostBtn];
           } else
               [self makeToast:request.message];
       }];
}
- (void)fileNodeCell:(FBFileNodeCell *)cell didTapExpandButton:(UIButton *)expandButton {
    NSIndexPath *indexPath = [self.tableView indexPathForCell:cell];
    [self reloadDataWtihIndexPath:indexPath];
}
- (void)selectCell:(FBFileNodeCell *)cell {
    NSIndexPath *indexPath = [self.tableView indexPathForCell:cell];

    VULFileZIPObjectModel *model = [self getVisibleNodes][indexPath.row];


    NSArray *titleArr = @[KLanguage(@"打开"), KLanguage(@"解压到...")];
    NSArray *imageArr = @[@"icon_operation_open", @"icon_operation_unZip"];
    if (model.level == 0) {
         titleArr  = @[KLanguage(@"解压到当前"),KLanguage(@"解压到文件夹"), KLanguage(@"解压到..."),KLanguage(@"属性")];
        imageArr = @[@"icon_operation_unZip",@"icon_operation_unZip",@"icon_operation_unZip",@"icon_operation_attribute"];

    }else{
        
        if (model.directory.boolValue) {
            titleArr = @[KLanguage(@"解压到...")];
           imageArr = @[@"icon_operation_unZip"];

        }else{
            NSArray *tarArr = @[@"tar",@"zip",@"gzip",@"bz2",@"rar",@"7z",@"gz",@"iso",@"ar",@"bz",@"xz",@"arj"];
            NSArray *iconArr = [model.originName componentsSeparatedByString:@"."];
            if([tarArr containsObject:[iconArr lastObject]]){
                titleArr = @[KLanguage(@"解压到...")];
               imageArr = @[@"icon_operation_unZip"];
            }

        }
    }
    NSString *title = titleArr[0];
    
    if([title isEqualToString:KLanguage(@"打开")]){
        if(model.encrypted){
            [self reNameWithModel:model];
        }else{
            [self openWithModel:model wtihText:@"" isPass:NO];
        }
        return;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
   

}
-(void)reloadDataWtihIndexPath:(NSIndexPath *)indexPath{
    VULFileZIPObjectModel *node = [self getVisibleNodes][indexPath.row];
    if (node.childList.count > 0) {
    // 如果有子节点，则可以展开/折叠
     node.isExpanded = !node.isExpanded;
        NSMutableArray<NSIndexPath *> *indexPaths = [NSMutableArray array];
        FBFileNodeCell *cell = [self.tableView cellForRowAtIndexPath:indexPath];
        [cell.upOrDownBtn setImage:node.childList.count > 0 ? (node.isExpanded ? [UIImage imageNamed:@"icon_open"] : [UIImage imageNamed:@"icon_close"]) : nil forState:UIControlStateNormal];
        if ( node.isExpanded) {
            [indexPaths addObjectsFromArray:[self getIndexPathsForVisibleRowsFromNode:node withRow:indexPath.row]];
            [self.tableView beginUpdates];
            [self.tableView insertRowsAtIndexPaths:indexPaths withRowAnimation:UITableViewRowAnimationFade];

            [self.tableView endUpdates];

        }else{
            [self.tableView beginUpdates];
            NSArray *indexArr =[self getIndexPathsForNoVisibleRowsFromNode:node withRow:indexPath.row];
            if (indexArr.count>0) {
                [self.tableView deleteRowsAtIndexPaths:indexArr withRowAnimation:UITableViewRowAnimationFade];
                [self.tableView endUpdates];

            }
        }

    }else{
//        if ( node.isload) {
////            已经加载过了 说明没数据了
//            return;
//        }
//        node.children = [VULAllFileModel new];
//        node.children.folderList = [NSMutableArray array];
//        [VULAllFileModel getDetailConentWithSourceID:node.sourceID icon:node.icon completion:^(__kindof id  _Nonnull responseObject) {
//            node.isload = YES;
//            VULAllFileModel *model =[VULAllFileModel modelWithDictionary:responseObject[@"data"]];
//
//            [node.children.folderList addObjectsFromArray:model.folderList];
//            [self setLevelWithModel:node withLevel:node.level];
//            [self reloadDataWtihIndexPath:indexPath];
//
//        }];
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

#pragma mark - Helper methods
-(NSArray<VULFileZIPObjectModel *> *)getVisibleNodes {
    NSMutableArray<VULFileZIPObjectModel *> *visibleNodes = [NSMutableArray array];

    for (VULFileZIPObjectModel *node in self.treeNodes) {
       [visibleNodes addObject:node];
       if (node.isExpanded) {
          [visibleNodes addObjectsFromArray:[self getVisibleNodesFromNode:node]];
       }
    }

    return visibleNodes;
}
-(NSArray<VULFileZIPObjectModel *> *)getVisibleNodesFromNode:(VULFileZIPObjectModel *)node {
    NSMutableArray<VULFileZIPObjectModel *> *visibleNodes = [NSMutableArray array];

    for (VULFileZIPObjectModel *child in node.childList) {
       [visibleNodes addObject:child];
       if (child.isExpanded) {
        [visibleNodes addObjectsFromArray:[self getVisibleNodesFromNode:child]];
      }
    }
    return visibleNodes;
}



- (NSArray<NSIndexPath *> *)getIndexPathsForVisibleRowsFromNode:(VULFileZIPObjectModel *)node withRow:(NSInteger )row {
    NSMutableArray<NSIndexPath *> *indexPaths = [NSMutableArray array];
    NSInteger count = node.childList.count;
    for (int i=0 ; i<count; i++) {
        [indexPaths addObject:[NSIndexPath indexPathForRow:row+i+1 inSection:0]];
    }
    
//    for (NSInteger i = 0; i < node.children.folderList.count; i++) {
//        VULFileObjectModel *child = node.children.folderList[i];
//        NSInteger visibleIndex = [[self getVisibleNodes] indexOfObject:child];
//        NSIndexPath *indexPath = [NSIndexPath indexPathForRow:visibleIndex inSection:0];
//        [indexPaths addObject:indexPath];
////        if (child.isExpanded) {
////            [indexPaths addObjectsFromArray:[self getIndexPathsForVisibleRowsFromNode:child]];
////        }
//    }
    
    return indexPaths;
}
- (NSArray<NSIndexPath *> *)getIndexPathsForNoVisibleRowsFromNode:(VULFileZIPObjectModel *)node withRow:(NSInteger )row {
    NSMutableArray<NSIndexPath *> *indexPaths = [NSMutableArray array];
    NSInteger count = 0;
        for (NSInteger i = 0; i < node.childList.count; i++) {
            count++;
            VULFileZIPObjectModel *child = node.childList[i];
  
        if (child.childList.count>0 && child.isExpanded) {
            count =  count+ [self getArrWithFromNode:child];
        }
            child.isExpanded = NO;
        
    }
    
    for (int i=0 ; i<count; i++) {
        [indexPaths addObject:[NSIndexPath indexPathForRow:row+i+1 inSection:0]];
    }
    
    return indexPaths;
}
-(NSInteger )getArrWithFromNode:(VULFileZIPObjectModel *)node{
    NSInteger count = 0;
    for (NSInteger i = 0; i < node.childList.count; i++) {
            count++;
        VULFileZIPObjectModel *child = node.childList[i];
  
        if (child.childList.count>0 && child.isExpanded) {
            count = count+ [self getArrWithFromNode:child];
        }
        child.isExpanded = NO;

    }
    return count;
}



- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"名称") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:16] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.numberOfLines = 1;
    }
    return _titleLabel;
}
- (VULLabel *)sizeLabel {
    if (!_sizeLabel) {
        _sizeLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"大小") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:16] Color:UIColorHex(#333333) BgColor:nil];
        _sizeLabel.numberOfLines = 1;
    }
    return _sizeLabel;
}

-(UIView *)bgView{
    if (!_bgView) {
        _bgView = [UIView new];
        _bgView.backgroundColor = [HEXCOLOR(0x000000) colorWithAlphaComponent:0.2];
        _bgView.layer.masksToBounds = YES;
        _bgView.layer.cornerRadius = fontAuto(15);
    }
    return _bgView;
}
-(UIView *)bgView1{
    if (!_bgView1) {
        _bgView1 = [UIView new];
        _bgView1.backgroundColor = HEXCOLOR(0xfafafa);
 ;
    }
    return _bgView1;
}

-(VULButton *)clostBtn{
    if (!_clostBtn) {
        _clostBtn = [VULButton new];
        [_clostBtn addTarget:self action:@selector(clickClostBtn) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return _clostBtn;
}
-(void)clickClostBtn{
    [self dismissViewControllerAnimated:YES completion:^{
            
    }];
}
-(FBFileAttributeTopView *)viewTop{
    if (!_viewTop) {
        _viewTop = [[FBFileAttributeTopView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, K_StatusBar_Height+fontAuto(65))];
    }
    return _viewTop;
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
