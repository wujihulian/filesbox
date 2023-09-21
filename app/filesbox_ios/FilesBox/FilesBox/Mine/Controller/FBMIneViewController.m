//
//  FBMIneViewController.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/20.
//

#import "FBMIneViewController.h"
#import "VULUpdateTool.h"
#import "VULMineHeaderView.h"
#import "VULMineCell.h"
#import "FBSettingViewController.h"
#import "VULScanningVC.h"
#import "VULLoginAuthView.h"
#import "VULWebSocket.h"
#import "FBAutomaticFilesVC.h"
#import "FBMineInfoVC.h"
#import "FBLogViewController.h"
#import "FBTakePhotoVC.h"
@interface FBMIneViewController ()<UITableViewDelegate, UITableViewDataSource,SKStoreProductViewControllerDelegate>

@property (nonatomic, strong) VULResponseUserInfoModel *userInfoModel; /**< 个人信息 model */
@property (nonatomic, strong) VULMineHeaderView *headerView;
@property (nonatomic, strong) BaseTableView *tableView;
@property (nonatomic, strong) UIView *tableFooterView;
@property (nonatomic, copy) NSArray *titleArray;
@property (nonatomic, strong) zhPopupController *popupController;
@end


@implementation FBMIneViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    _titleArray = @[KLanguage(@"站点"),
                    KLanguage(@"设置"),
                    KLanguage(@"扫码"),
                    KLanguage(@"备份"),
                    KLanguage(@"日志"),
                    KLanguage(@"升级"),
                    KLanguage(@"退出"),
  ];
    [self setNavigationViewHide:YES];
    self.view.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.tableView];
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.top.right.mas_equalTo(0);
        make.bottom.mas_equalTo(-K_TabBar_Height);
    }];
    self.tableView.tableHeaderView = self.headerView;
//    self.tableView.tableFooterView = self.tableFooterView;
    
    // Do any additional setup after loading the view.
}

#pragma mark - UITableViewDelegate\UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return self.titleArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    VULMineCell *cell = [VULMineCell dequeueReusableCellWithTableView:tableView reuseIdentifier:@"cell"];
    cell.backgroundColor = [UIColor whiteColor];
    cell.briefLabel.hidden = NO;
    cell.switchh.hidden = YES;
    cell.titleLabel.text = [_titleArray objectAtIndex:indexPath.section];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    NSArray *iconArray ;
    iconArray = @[@"icon_mine_site",@"icon_mine_set",@"icon_mine_scan",@"icon_backUp",@"icon_mine_log",@"icon_mine_update",@"icon_mine_exit"];

    cell.cellImageView.image = [UIImage imageNamed:[iconArray objectAtIndex:indexPath.section]];
    
//    cell.separatorLine.hidden = indexPath.section == iconArray.count - 1;
    cell.separatorLine.hidden = YES;
    cell.redView.hidden = YES;

    cell.briefLabel.text  = @"";
 if ([[_titleArray objectAtIndex:indexPath.section] isEqualToString:KLanguage(@"升级")]) {
        cell.briefLabel.text =  [NSString stringWithFormat:@"v%@", kCurrentVersion];
    }
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return fontAuto(51);
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
//    if (section == 2) {
//        return fontAuto(10);
//    }
    return 0.001;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
//    if (section == 0) {
//        return fontAuto(10);
//    }
    return 0;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
//    if (section == 0) {
//        UIView *sectionHeader = [UIView new];
//        sectionHeader.backgroundColor = HEXCOLOR(0xf6f6f6);
//        return sectionHeader;
//    }
    return nil;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section {
    UIView *footView = [UIView new];
    footView.backgroundColor = [UIColor whiteColor];
    return footView;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    WeakSelf(self);
    NSString *title = [_titleArray objectAtIndex:indexPath.section];
    if ([title isEqualToString: KLanguage(@"站点")]) {
        UIAlertController *alertController = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
        
        UIAlertAction *action1 = [UIAlertAction actionWithTitle:KLanguage(@"文件管理") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            VULBaseWebViewVC *vc = [VULBaseWebViewVC new];
            vc.currentURL = ChooseUrl;
            [self.navigationController pushViewController:vc animated:YES];
        }];
        UIAlertAction *action2 = [UIAlertAction actionWithTitle:KLanguage(@"我的桌面") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            VULBaseWebViewVC *vc = [VULBaseWebViewVC new];
//            NSString *encodedString = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(
//                kCFAllocatorDefault,
//                (CFStringRef),
//                NULL,
//                CFSTR(":/?#[]@!$&'()*+,;="),
//                kCFStringEncodingUTF8
//            ));
            vc.currentURL =[NSString stringWithFormat:@"%@#/desktop?isApp=1",ChooseUrl]  ;
            [self.navigationController pushViewController:vc animated:YES];
            // 点击按钮2后的操作
        }];
        UIAlertAction *action3 = [UIAlertAction actionWithTitle:KLanguage(@"取消") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
     
        }];
        
        [alertController addAction:action1];
        [alertController addAction:action2];
        [alertController addAction:action3];
        
        UIPopoverPresentationController *popoverPresentationController = alertController.popoverPresentationController;
        if (popoverPresentationController) {
            popoverPresentationController.sourceView = self.view;
        }
        
        [self presentViewController:alertController animated:YES completion:nil];
        
//        FBTakePhotoVC *vc= [FBTakePhotoVC new];
//    https://test.filesbox.cn/#/desktop

    }else if([title isEqualToString:KLanguage(@"日志")]){
        FBLogViewController *vc = [FBLogViewController new];
        [self.navigationController pushViewController:vc animated:YES];
    }else if([title isEqualToString:KLanguage(@"扫码")]){
        VULScanningVC *scanVC = [[VULScanningVC alloc] init];
        scanVC.scanOutQRCodeBlock = ^(NSString *_Nonnull scanQRString) {
            BOOL isUrl = [NSString isUrlAddress:scanQRString];
            if (isUrl) {
                if ([scanQRString containsString:@"/pages/download.html?key="]) {
                    NSLog(@"%@",scanQRString);
                    NSArray *itemArr = [scanQRString componentsSeparatedByString:@"?key="];
                    NSArray *itemArr1 = [scanQRString componentsSeparatedByString:@"/pages/download.html"];
                    NSString *baseUrl = itemArr1.firstObject;
                    
                    baseUrl = [NSString stringWithFormat:@"%@/",baseUrl];

                    if(![ChooseUrl isEqualToString:baseUrl]){
                       [ weakself vul_showAlertWithTitle:KLanguage(@"温馨提示") message:KLanguage(@"您扫描的Web端站点和当前App登录的站点不一致．请扫描对应Web端站点登录界面的二维码！") appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
                           alertMaker.addActionDestructiveTitle(KLanguage(@"知道了"));

                        } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
                        
                        } ];
                        return;
                    }
                    NSString *keyStr = itemArr.lastObject;
                    NSArray *itemArr2 = [baseUrl componentsSeparatedByString:@"://"];
                    NSString *baseUrl1 = itemArr2.lastObject;


                    NSString *socketUrlString = [NSString stringWithFormat:@"wss://%@websocket/webchat/scanLogin/%@",baseUrl1, keyStr];

                    [weakself connectSocketWithKey:socketUrlString];
                    
            
                    [weakself vul_showAlertWithTitle:KLanguage(@"温馨提示") message:KLanguage(@"确认您本人登录Web版FilesBox？") appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
                        alertMaker.
                        addActionCancelTitle(KLanguage(@"取消")).
                        addActionDestructiveTitle(KLanguage(@"确定"));
                    } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
                        if (buttonIndex == 1) {
    //                            VULResponseSchoolInfoModel *schoolModel = [VULRealmDBManager getCurrentSchoolInfoFromDB];
                                NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithCapacity:0];
                                [dic setValue:@"auth" forKey:@"action"];
                                [dic setValue:@"appScanLogin" forKey:@"msgType"];
                                [dic setValue:[VULRealmDBManager getLocalToken] forKey:@"token"];
                                [dic setValue:baseUrl forKey:@"serverName"];
                                NSString *jsonStr = [NSString objectToJson:dic];
                                [[VULWebSocket sharedVULWebSocket] sendData:jsonStr];
                            } else {
                                [[VULWebSocket sharedVULWebSocket] closeWebSocket];
                            }
                    }];
                    
                } else {
                    VULBaseWebViewVC *webVc = [[VULBaseWebViewVC alloc] init];
                    webVc.currentURL = scanQRString;
                    [self.navigationController pushViewController:webVc animated:YES];
                }
            } else {
                VULAlertConfig *config = [[VULAlertConfig alloc] init];
                config.title.text = @"已扫描到以下内容";
                config.content.text = scanQRString;
                config.content.font = [UIFont systemFontOfSize:15];
                
                VULAlertButtonConfig *copyBtn = [VULAlertButtonConfig configWithTitle:KLanguage(@"复制内容")  color:DefaultColor font:nil image:nil handle:^{
                    [[UIPasteboard generalPasteboard] setString:scanQRString];
                    [self makeToast:KLanguage(@"复制成功")];
                }];
                
                VULAlertButtonConfig *cancelBtn = [VULAlertButtonConfig configWithTitle:KLanguage(@"取消") color:[UIColor lightGrayColor] font:nil image:nil handle:^{
                    NSLog(@"click cancel");
                }];
                
                config.buttons = @[cancelBtn, copyBtn];
                VULCustomAlertView *alert = [[VULCustomAlertView alloc] initWithConfig:config];
                [kWindow addSubview:alert];
            }
        };
        [self.navigationController pushViewController:scanVC animated:YES];
    }else if([title isEqualToString:KLanguage(@"设置")]){
        FBSettingViewController *vc = [FBSettingViewController new];
        vc.saveAndRefreshBlock = ^{
            VULSaveUserInformation *userInfo = [VULRealmDBManager getLocalUserInformational];
            _headerView.realNameLabel.text = userInfo.nickname.length>0?userInfo.nickname:userInfo.name;
            _headerView.userNameLabel.text = userInfo.nickname.length>0?userInfo.name:@"";
            [_headerView.portraitImgView sd_setImageWithURL:[NSURL URLWithString:resultsUrl(userInfo.avatar)] placeholderImage:VULGetImage(@"placeholder_face")];

            if(userInfo.nickname.length>0){
                [_headerView.realNameLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.left.equalTo(_headerView.portraitImgView.mas_right).offset(kSpace);
            //        make.height.equalTo(@(3 * kSpace));
            //        make.width.mas_lessThanOrEqualTo(VULSCREEN_WIDTH * 0.75);
                    make.right.mas_equalTo(-10);
                    make.top.mas_equalTo(fontAuto(16));
                }];
            }else{
                [_headerView.realNameLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.left.equalTo(_headerView.portraitImgView.mas_right).offset(kSpace);
            //        make.height.equalTo(@(3 * kSpace));
            //        make.width.mas_lessThanOrEqualTo(VULSCREEN_WIDTH * 0.75);
                    make.right.mas_equalTo(-10);
                    make.top.mas_equalTo(fontAuto(20));
                }];
            }
            
        };
        [self.navigationController pushViewController:vc animated:YES];

    }else if([title isEqualToString:KLanguage(@"备份")]){
        FBAutomaticFilesVC *vc = [FBAutomaticFilesVC new];
        [self.navigationController pushViewController:vc animated:YES];

    }else if([title isEqualToString:KLanguage(@"升级")]){
        
        [self showWaitHudWithString:nil];
        [AppDelegate hasNewVersion:^(BOOL hasNewVersion) {
            [self dissmissHudView];
            // 更新检查
            if (hasNewVersion) {
                UIAlertController *alertControl = [UIAlertController alertControllerWithTitle:KLanguage(@"检测到新版本是否去更新？") message:@"" preferredStyle:UIAlertControllerStyleAlert];

                // 2.创建并添加按钮
                UIAlertAction *okAction = [UIAlertAction actionWithTitle:KLanguage(@"去更新") style:UIAlertActionStyleDefault handler:^(UIAlertAction *_Nonnull action) {
                  
                    SKStoreProductViewController *storeViewController = [[SKStoreProductViewController alloc] init];
                    [storeViewController loadProductWithParameters:@{ SKStoreProductParameterITunesItemIdentifier: kAppID } completionBlock:^(BOOL result, NSError *_Nullable error) {
                        if (error) {
                            NSString *downloadStr = [NSString stringWithFormat:@"https://itunes.apple.com/cn/app/id%@?mt=8", kAppID];
                            NSURL *url = [NSURL URLWithString:downloadStr];
                            [[UIApplication sharedApplication] openURL:url];
                        }
                    }];
                    storeViewController.delegate = self;
                    [self presentViewController:storeViewController animated:YES completion:nil];
                }];
                UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:KLanguage(@"取消") style:UIAlertActionStyleCancel handler:^(UIAlertAction *_Nonnull action) {
                }];

                [alertControl addAction:okAction];
                [alertControl addAction:cancelAction];

                // 3.呈现UIAlertContorller
                [weakself presentViewController:alertControl animated:YES completion:nil];
            } else {
                UIAlertController *alertControl = [UIAlertController alertControllerWithTitle:KLanguage(@"当前已是最新版本") message:@"" preferredStyle:UIAlertControllerStyleAlert];
                UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:KLanguage(@"确定") style:UIAlertActionStyleCancel handler:^(UIAlertAction *_Nonnull action) {
                }];
                [alertControl addAction:cancelAction];
                [weakself presentViewController:alertControl animated:YES completion:nil];
            }
        }];

    }else{
        [weakself vul_showAlertWithTitle:KLanguage(@"确定退出登录吗？") message:nil appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
            alertMaker.
            addActionCancelTitle(KLanguage(@"取消")).
            addActionDestructiveTitle(KLanguage(@"确定"));
        } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
            if (buttonIndex == 1) {
                VULLoginViewController *login = [[VULLoginViewController alloc] init];
                VULNavigationViewController *nav = [[VULNavigationViewController alloc] initWithRootViewController:login];
                nav.modalPresentationStyle = UIModalPresentationOverFullScreen;
                [self.navigationController presentViewController:nav animated:YES completion:nil];
            }
        }];
    }
}
- (void)vul_webSocketDidOpen:(SRWebSocket *)webSocket {
    NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithCapacity:0];
    NSArray *itemArr2 = [ChooseUrl componentsSeparatedByString:@"://"];
    NSString *baseUrl1 = itemArr2.lastObject;
    [dic setValue:@"confirm" forKey:@"action"];
    [dic setValue:@"appScanLogin" forKey:@"msgType"];
    [dic setValue:[VULRealmDBManager getLocalToken] forKey:@"token"];
    [dic setValue:baseUrl1 forKey:@"serverName"];
    NSString *jsonStr = [NSString objectToJson:dic];
    [[VULWebSocket sharedVULWebSocket] sendData:jsonStr];
}
#pragma mark - 扫码 socket相关
- (void)connectSocketWithKey:(NSString *)socketUrlString {
    [VULWebSocket sharedVULWebSocket].webSocketUrlStr = socketUrlString;
    [[VULWebSocket sharedVULWebSocket] openWebSocketWithcwid:@""];
    [VULWebSocket sharedVULWebSocket].webSocketDelegate = self;
}
- (void)vul_webSocket:(SRWebSocket *)webSocket didReceiveMessage:(id)message {
    NSData *receiveMessageData = [(NSString *)message dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary *receiveMessage = [NSJSONSerialization JSONObjectWithData:receiveMessageData options:NSJSONReadingMutableContainers error:nil];
    
    if ([receiveMessage containsObjectForKey:@"type"]) {
        if ([receiveMessage[@"type"] isEqualToString:@"scanLogin"]) {
            [VULQueue executeInMainQueue:^{
                if ([receiveMessage[@"code"] integerValue] == 601) { //二维码已失效
                    [[VULWebSocket sharedVULWebSocket] closeWebSocket];
                    [self vul_showAlertWithTitle:@"二维码已失效" message:nil appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
                        alertMaker.addActionDefaultTitle(@"确定");
                    } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
                    }];
                    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                        [self.popupController dismiss];
                    });
                    return;
                } else if ([receiveMessage[@"code"] integerValue] != 200) {
                    [self makeToast:receiveMessage[@"message"]];
                    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                        [self.popupController dismiss];
                    });
                }
            }];
        }
    } else {
        if ([receiveMessage[@"action"] isEqualToString:@"auth"]) {
            [VULQueue executeInMainQueue:^{
                if ([receiveMessage[@"code"] integerValue] == 200) {
                    [self makeToast:@"登录成功"];
                    [[VULWebSocket sharedVULWebSocket] closeWebSocket];
                    [self.popupController dismiss];
                } else if ([receiveMessage[@"code"] integerValue] == 603) {
                    [self vul_showAlertWithTitle:@"登录失败" message:nil appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
                        alertMaker.addActionDefaultTitle(@"确定");
                    } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
                    }];
                    [[VULWebSocket sharedVULWebSocket] closeWebSocket];
                    [self.popupController dismiss];
                } else {
                    [self makeToast:receiveMessage[@"message"]];
                }
            }];
        }
    }
}



#pragma mark - SKStoreProductViewControllerDelegate
- (void)productViewControllerDidFinish:(SKStoreProductViewController *)viewController {
    [viewController dismissViewControllerAnimated:YES completion:nil];
}








- (UIStatusBarStyle)preferredStatusBarStyle {
    return UIStatusBarStyleLightContent;
}


#pragma mark - getter
- (BaseTableView *)tableView {
    if (!_tableView) {
        _tableView = [[BaseTableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.backgroundColor = HEXCOLOR(0xffffff);
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
//        [_tableView setSeparatorColor:[UIColor clearColor]];

        _tableView.bounces = NO;
    }
    return _tableView;
}

- (VULMineHeaderView *)headerView {
    if (!_headerView) {
        _headerView = [[VULMineHeaderView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, fontAuto(200))];
        _headerView.clickBgImg = ^{
            FBMineInfoVC *vc = [FBMineInfoVC new];
            vc.saveAndRefreshBlock = ^{
                VULSaveUserInformation *userInfo = [VULRealmDBManager getLocalUserInformational];
                _headerView.realNameLabel.text = userInfo.nickname.length>0?userInfo.nickname:userInfo.name;
                _headerView.userNameLabel.text = userInfo.nickname.length>0?userInfo.name:@"";
                [_headerView.portraitImgView sd_setImageWithURL:[NSURL URLWithString:resultsUrl(userInfo.avatar)] placeholderImage:VULGetImage(@"placeholder_face")];

                if(userInfo.nickname.length>0){
                    [_headerView.realNameLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
                        make.left.equalTo(_headerView.portraitImgView.mas_right).offset(kSpace);
                //        make.height.equalTo(@(3 * kSpace));
                //        make.width.mas_lessThanOrEqualTo(VULSCREEN_WIDTH * 0.75);
                        make.right.mas_equalTo(-10);
                        make.top.mas_equalTo(fontAuto(16));
                    }];
                }else{
                    [_headerView.realNameLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
                        make.left.equalTo(_headerView.portraitImgView.mas_right).offset(kSpace);
                //        make.height.equalTo(@(3 * kSpace));
                //        make.width.mas_lessThanOrEqualTo(VULSCREEN_WIDTH * 0.75);
                        make.right.mas_equalTo(-10);
                        make.top.mas_equalTo(fontAuto(20));
                    }];
                }
                
            };
            [self.navigationController pushViewController:vc animated:YES];
        };
    }
    return _headerView;
}

- (UIView *)tableFooterView {
    if (!_tableFooterView) {
        _tableFooterView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, 0)];
        
        WeakSelf(self)
        UIButton *turnout = [UIButton buttonWithType:UIButtonTypeCustom];
        turnout.frame = CGRectMake(0, 33, VULSCREEN_WIDTH, 34 + fontAuto(17));
        turnout.backgroundColor = [UIColor whiteColor];
        [turnout.titleLabel setFont:[UIFont yk_pingFangRegular:FontAuto(16)]];
        [turnout setTitle:@"退出登录" forState:UIControlStateNormal];
        [turnout setTitleColor:UIColorHex(#333333) forState:UIControlStateNormal];
        [turnout setTitleColor:[UIColorHex(#333333) colorWithAlphaComponent:0.5] forState:UIControlStateHighlighted];
        [_tableFooterView addSubview:turnout];
        
        _tableFooterView.height = turnout.bottom + 28;
        
        [[turnout rac_signalForControlEvents:UIControlEventTouchUpInside] subscribeNext:^(id x){
            [weakself vul_showAlertWithTitle:KLanguage(@"确定退出登录吗？") message:nil appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
                alertMaker.
                addActionCancelTitle(KLanguage(@"取消")).
                addActionDestructiveTitle(KLanguage(@"确定"));
            } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
                if (buttonIndex == 1) {
                    VULLoginViewController *login = [[VULLoginViewController alloc] init];
                    VULNavigationViewController *nav = [[VULNavigationViewController alloc] initWithRootViewController:login];
                    nav.modalPresentationStyle = UIModalPresentationOverFullScreen;
                    [self.navigationController presentViewController:nav animated:YES completion:nil];
                }
            }];
        }];
    }
    return _tableFooterView;
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
