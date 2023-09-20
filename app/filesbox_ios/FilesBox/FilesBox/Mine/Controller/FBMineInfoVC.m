//
//  FBMineInfoVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/7/17.
//

#import "FBMineInfoVC.h"
#import "FBUserInfoModel.h"
#import "FBMineInfoCell.h"
#import "FBMineEditVC.h"
#import "FBSpaceVC.h"
#import "FBFromTopView.h"
@interface FBMineInfoVC ()
@property (nonatomic, retain) NSMutableArray *titleArray;
@property (nonatomic, assign) NSInteger selectIndex;


@end

@implementation FBMineInfoVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationTitle = KLanguage(@"账号管理");
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    
    self.titleArray = [NSMutableArray array];
    [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(K_NavBar_Height);
            make.left.bottom.right.mas_offset(0);
    }];;
    [self.tableView registerClass:[FBMineInfoCell class] forCellReuseIdentifier:@"FBMineInfoCell"];
    [self getFileBoxInfo];

    // Do any additional setup after loading the view.
}
-(void)getFileBoxInfo{

    [VULBaseRequest requestWithUrl:@"/api/disk/options" params:nil requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest * _Nonnull request) {
        [self dissmissHudView];
        NSString *success =request.responseObject[@"success"];

        if (success.boolValue) {
            VULResponseLoginModel *loginModel = [VULResponseLoginModel modelWithDictionary:request.responseObject[@"data"][@"user"]];
            [VULRealmDBManager updateLocaPersonalInformation:loginModel];
            if(self.saveAndRefreshBlock){
                self.saveAndRefreshBlock();
            }

            [self getHome];
        }
    }];
}
-(void)getHome{
    [VULBaseRequest requestWithUrl:@"/api/disk/home" params:nil requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest * _Nonnull request) {
        [self dissmissHudView];
        NSString *success =request.responseObject[@"success"];
        if (success.boolValue) {
            NSString *thirdLoginConfig = request.data[@"thirdLoginConfig"];
            NSData *data = [thirdLoginConfig dataUsingEncoding:NSUTF8StringEncoding];
            NSError *error = nil;
            NSArray *jsonArray = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error]; // 转换为数组
            [self.titleArray removeAllObjects];
            VULSaveUserInformation *userInfo = [VULRealmDBManager getLocalUserInformational];
            self.selectIndex = userInfo.sex.integerValue;
            [self.titleArray addObject:[FBUserInfoModel getArrayWithModel:userInfo]];
            NSMutableArray *secondArr = [NSMutableArray array];
        

            for(NSDictionary *dic in  jsonArray){
                NSString *thirdName = dic[@"thirdName"];
                if([thirdName isEqualToString:@"wechat"]){
                    FBUserInfoModel *model = [FBUserInfoModel new];
                    model.title = KLanguage(@"微信");
                    model.detail = userInfo.wechatOpenId.boolValue?KLanguage(@"已绑定"):KLanguage(@"未绑定");
                    model.rightBtn = YES;
                    model.isBtn = YES;
                    model.imageShow = NO;
                    [secondArr addObject:model];
                }
                if([thirdName isEqualToString:@"dingding"]){
                    FBUserInfoModel *model = [FBUserInfoModel new];
                    model.title = KLanguage(@"钉钉");
                    model.detail = userInfo.dingOpenId.boolValue?KLanguage(@"已绑定"):KLanguage(@"未绑定");
                    model.rightBtn = YES;
                    model.isBtn = NO;
                    model.imageShow = NO;
                    [secondArr addObject:model];
                }
                if([thirdName isEqualToString:@"enWechat"]){
                    FBUserInfoModel *model = [FBUserInfoModel new];
                    model.title = KLanguage(@"企业微信");
                    model.detail = userInfo.enWechatOpenId.boolValue?KLanguage(@"已绑定"):KLanguage(@"未绑定");
                    model.rightBtn = YES;
                    model.isBtn = NO;
                    model.imageShow = NO;
                    [secondArr addObject:model];
                }
                
            }
            [self.titleArray addObject:secondArr];
            [self.tableView reloadData];
        }
    }];
}
#pragma mark -- Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return self.titleArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSArray *arr = self.titleArray[section];
    return arr.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    FBMineInfoCell *cell = [FBMineInfoCell dequeueReusableCellWithTableView:tableView reuseIdentifier:@"FBMineInfoCell"];
    FBUserInfoModel *model = self.titleArray[indexPath.section][indexPath.row];
    cell.titleLabel.text = model.title;
    cell.rightLabel.text = model.detail;
    cell.bingBtn.hidden = YES;
    cell.rightImageV.hidden = !model.imageShow;
    cell.iconRight.hidden = !model.rightBtn;
    cell.rightLabel.hidden = model.imageShow;
    if(model.imageShow){
        [cell.rightImageV sd_setImageWithURL:[NSURL URLWithString:resultsUrl(model.detail)] placeholderImage:VULGetImage(@"placeholder_face") ];
    }
    if(model.isBtn){
        if([model.detail isEqualToString:KLanguage(@"已绑定")]){
            [cell.bingBtn setTitle:KLanguage(@"取消绑定") forState:UIControlStateNormal];
        }else{
            [cell.bingBtn setTitle:KLanguage(@"立即绑定") forState:UIControlStateNormal];

        }
        [cell.titleLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));
            make.bottom.mas_equalTo(-fontAuto(10));
            make.width.mas_equalTo(fontAuto(80));
        }];
    }else{
        [cell.titleLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));
            make.bottom.mas_equalTo(-fontAuto(10));
        }];
    }

  
    return cell;
}
-(void)bingWithCode:(NSString *)code{
    [VULBaseRequest requestWithUrl:@"/api/wechat/bind" params:@{@"code":code,@"type":@12,@"state":@"12",@"sig":@"11"} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest * _Nonnull request) {
        [self dissmissHudView];
        NSString *success =request.responseObject[@"success"];
        if (success.boolValue) {
            [self getFileBoxInfo];
        }
    }];
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return fontAuto(50);
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {

    return 0.001;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    if (section ==1) {
        return fontAuto(30);
    }
    return 0;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    if (section == 1) {
        UIView *sectionHeader = [UIView new];
        sectionHeader.backgroundColor = HEXCOLOR(0xf6f6f6);
        UILabel *label = [UILabel new];
        label.text = KLanguage(@"第三方登录");
        label.font = [UIFont yk_pingFangRegular:fontAuto(14)];
        label.textColor = HEXCOLOR(0x999999);
        [sectionHeader addSubview:label];
        [label mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.bottom.mas_equalTo(0);
            make.left.mas_offset(fontAuto(12));
        }];
        return sectionHeader;
    }
    return [UIView new];
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section {
    UIView *footView = [UIView new];
    footView.backgroundColor = [UIColor whiteColor];
    return footView;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    WeakSelf(self);
    FBUserInfoModel *model = self.titleArray[indexPath.section][indexPath.row];
    if([model.title isEqualToString:KLanguage(@"微信")]){
        if([model.detail isEqualToString:KLanguage(@"已绑定")]){
            [self vul_showAlertWithTitle:KLanguage(@"确定要解除绑定吗？") message:nil appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
                alertMaker.
                addActionCancelTitle(KLanguage(@"取消")).
                addActionDestructiveTitle(KLanguage(@"确定"));
            } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
                if (buttonIndex == 1) {
                    [VULBaseRequest requestWithUrl:@"api/wechat/unbind" params:@{@"type":@12} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest * _Nonnull request) {
                        [self dissmissHudView];
                        NSString *success =request.responseObject[@"success"];
                        if (success.boolValue) {
                            [self getFileBoxInfo];
                        }
                    }];
                }
            }];
        }else{
            //         由于登录
            WXApiManger * weixin = [WXApiManger shareInstance];
            [weixin oauth_authorization];
            [weixin setGetWeChatCodeCompletion:^(NSString * _Nonnull code) {
                
                if (NSStringIsNotEmpty(code)) {
                    
                    [self bingWithCode:code];
           }}];

        }
        return;
    }
    
    if([model.title isEqualToString:KLanguage(@"钉钉")] || [model.title isEqualToString:KLanguage(@"企业微信")]){
        [self vul_showAlertWithTitle:KLanguage(@"app暂时不支持绑定取消绑定，请到WEB进行相应操作") message:nil appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
            alertMaker.
            addActionDestructiveTitle(KLanguage(@"确定"));
        } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
            if (buttonIndex == 1) {
            
            }
        }];
        return;
    }
    if([model.title isEqualToString:KLanguage(@"头像")]){
        [self pushImagePickerController];
        return;
    }
    if([model.title isEqualToString:KLanguage(@"空间")]){
        FBSpaceVC *vc = [FBSpaceVC new];
        [self.navigationController pushViewController:vc animated:YES];        return;
    }
    if([model.title isEqualToString:KLanguage(@"昵称")]||[model.title isEqualToString:KLanguage(@"邮箱")]||[model.title isEqualToString:KLanguage(@"手机")]||[model.title isEqualToString:KLanguage(@"密码")]){
        FBMineEditVC *vc = [FBMineEditVC new];
        vc.model = model;
        vc.saveAndRefreshBlock = ^{
           
            [self getFileBoxInfo];

        };
        [self.navigationController pushViewController:vc animated:YES];
        return;
    }
    if([model.title isEqualToString:KLanguage(@"性别")]){
        FBFromTopView *top = [[FBFromTopView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, 2*BtnCell+K_BottomBar_Height)];
        top.index =0;
        top.selectIndex = [NSString stringWithFormat:@"%ld",self.selectIndex] ;
        top.titleArr = @[KLanguage(@"女"),KLanguage(@"男")];
        zhPopupController *popup2 =[[zhPopupController alloc] initWithView:top size:CGSizeMake(VULSCREEN_WIDTH, top.height)];
        popup2.layoutType = zhPopupLayoutTypeBottom;
        popup2.presentationStyle = zhPopupSlideStyleFromBottom;
        popup2.maskAlpha = 0.35;
        top.clickViewWithRowBlock = ^(NSString * _Nonnull title, NSInteger row) {
            self.selectIndex =row;
            [self showWaitHudWithString:nil];
            [self uploadWithDic:@{@"key":@"sex",@"value":@(self.selectIndex)}];

            [popup2 dismiss];
        };
        popup2.didDismissBlock = ^(zhPopupController * _Nonnull popupController) {

        };
        [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
        return;
    }
}

#pragma mark - 打开相册
- (void)pushImagePickerController {
    TZImagePickerController *imagePickerVc = [[TZImagePickerController alloc] initWithMaxImagesCount:1 columnNumber:4 delegate:self pushPhotoPickerVc:YES];
//    [imagePickerVc.navigationBar setBackgroundImage:[UIImage createImageWithColor:LAUIMainColor] forBarMetrics:UIBarMetricsDefault];
    if (@available(iOS 15.0, *)) {
        UINavigationBarAppearance *appearance = [UINavigationBarAppearance new];
        [appearance configureWithOpaqueBackground];
        appearance.backgroundColor = [UIColor colorWithRed:(34/255.0) green:(34/255.0)  blue:(34/255.0) alpha:1.0];
        imagePickerVc.navigationBar.standardAppearance = appearance;
        imagePickerVc.navigationBar.scrollEdgeAppearance= imagePickerVc.navigationBar.standardAppearance;
    }
    imagePickerVc.view.alpha = 1;
    imagePickerVc.isSelectOriginalPhoto = NO;
    imagePickerVc.allowTakePicture = YES; // 在内部显示拍照按钮
    imagePickerVc.allowPickingVideo = NO;
    imagePickerVc.allowPickingImage = YES;
    imagePickerVc.allowPickingOriginalPhoto = YES;
    imagePickerVc.allowPickingGif = NO;
    imagePickerVc.sortAscendingByModificationDate = YES;
    imagePickerVc.showSelectBtn = NO;
    imagePickerVc.needCircleCrop = NO;
    imagePickerVc.allowCrop = YES;
    imagePickerVc.circleCropRadius = VULSCREEN_WIDTH/2;
    imagePickerVc.modalPresentationStyle = UIModalPresentationFullScreen;

    if ([[NSString getDeviceModelName] hasPrefix:@"iPad"]) {
        [[NSOperationQueue mainQueue] addOperationWithBlock:^{
            [self presentViewController:imagePickerVc animated:YES completion:nil];
        }];
    } else {
        [self presentViewController:imagePickerVc animated:YES completion:nil];
    }
}

- (void)imagePickerController:(TZImagePickerController *)picker didFinishPickingPhotos:(NSArray<UIImage *> *)photos sourceAssets:(NSArray *)assets isSelectOriginalPhoto:(BOOL)isSelectOriginalPhoto {
    NSString *fileName;
    if ([[assets firstObject] isKindOfClass:[PHAsset class]]) {
        PHAsset *phasset = (PHAsset *)[assets firstObject];
        fileName = [phasset valueForKey:@"filename"];
    }
    NSData *dataImage = UIImageJPEGRepresentation([photos firstObject], 1.0);
    UIImage *upImage = [UIImage imageWithData:dataImage];
    NSArray *imageArray = [NSArray arrayWithObject:upImage];
    [self showWaitHudWithString:nil];
    [VULNetworking sendPostImageWithPath:[NSString stringWithFormat:@"%@api/disk/upload", ChooseUrl] withParameters: @{ @"busType": @"image" } withImageArray: imageArray withTargetWidth: upImage.size.width withProgress:^(NSProgress *progress) {
        NSLog(@"%@", progress.accessibilityValue);
    } success:^(BOOL isSuccess, id responseObject) {
        NSLog(@"%@", responseObject);
        NSString *code = responseObject[@"code"];
        if ([code isEqualToString:@"common.success"]) {
            NSDictionary *dataDic = responseObject[@"data"];
            [self uploadWithDic:@{@"key":@"avatar",@"value":dataDic[@"path"]}];
        }else{
            [self makeToast:responseObject[@"message"]];

        }
  
    } failure:^(NSError *error) {
        NSLog(@"%@", error);
    }];
    
}
-(void)uploadWithDic:(NSDictionary *)dic{
    [VULBaseRequest requestWithUrl:@"/api/disk/user/setUserInfo" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest * _Nonnull request) {
        NSString *success =request.responseObject[@"success"];

        if (success.boolValue) {
            [self getFileBoxInfo];

        }else{
            [self dissmissHudView];
            [self makeToast:request.responseObject[@"message"]];

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

@end
