//
//  VULLoginViewController.m
//  VideoULimit
//
//  Created by ZCc on 2018/9/5.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULLoginViewController.h"
#import "FBChangeLanguage.h"
#import "FBHistoryLinkView.h"
#import "VULRequestModel.h"
#import "YTKNetworkConfig.h"
#import "FBTabBarControllerConfig.h"
#import "FBHomeViewController.h"
@interface VULLoginViewController ()<UITextFieldDelegate>
@property (nonatomic, strong) VULSvgImageView *appIconImg;
@property (nonatomic, strong) UIImageView *rightImg;
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *userLabelMark;

@property (nonatomic, strong) UIImageView *userLabel;
@property (nonatomic, strong) UIImageView *passLabel;

@property (nonatomic, strong) UIImageView *cloudImg;
@property (nonatomic, strong) UITextField *cloudText;//账号

@property (nonatomic, strong) YYLabel *agressLabel;
@property (nonatomic, strong) UIImageView *accountImg;
@property (nonatomic, strong) UIImageView *passImg;
@property (nonatomic, strong) UITextField *userName;//账号
@property (nonatomic, strong) UITextField *password;//密码
@property (nonatomic, strong) VULButton *hidePasswordBtn; //隐藏、显示密码
@property (nonatomic, copy) NSString *baseUrl;/**<服务器地址 */
@property (nonatomic, copy) NSString *userNameStr;/**< 用户名 */
@property (nonatomic, copy) NSString *passwordStr;/**< 密码 */
@property (nonatomic, copy) NSString *key;/**< key */

@property (nonatomic, strong) UIButton *loginBtn;   //登录按钮
@property (nonatomic, strong) UIButton *registeredBtn;   //注册按钮
@property (nonatomic, strong) UIButton *forgetPassword;   /**< 忘记密码按钮 */
@property (nonatomic, strong) UIView *otherView;
@property (nonatomic, strong) NSMutableArray *schoolListMut;/**< 网校列表 */
//@property (nonatomic, strong) UMSocialUserInfoResponse *otherLoginResponse;/**< 三方登录数据 */

//@property (nonatomic, strong) VULResponseLoginModel *login;/**< 登录后数据 */
@property (nonatomic, strong) VULButton *agreeBtn; //隐藏、显示密码

@property (nonatomic, strong) FBChangeLanguage *firstShowView;

@property (nonatomic, strong) UIButton *loginUp;
@property (nonatomic, strong) FBHistoryLinkView *linkView;

@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, assign) BOOL isLogin;

@end

@implementation VULLoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.

    [super viewDidLoad];
//    self.navigationTitle = @"登录";
    self.navigationViewHide = YES;
    self.view.backgroundColor = HEXCOLOR(0xffffff);
        NSUserDefaults *TimeOfBootCount = [NSUserDefaults standardUserDefaults];
    NSString *baseUrl = [[NSUserDefaults standardUserDefaults] objectForKey:@"baseUrl"];
    _isLogin = baseUrl.length==0;
    [self.view addSubview:self.rightImg];
    [self.rightImg mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(K_NavBar_Height+fontAuto(10));
        make.right.mas_equalTo(0);
        make.bottom.mas_equalTo(-fontAuto(52));
        make.width.mas_equalTo(fontAuto(174));
    }];
    if (![TimeOfBootCount valueForKey:@"time"]) {
        NSLog(@"第一次启动");
        [self changeLang];
    }else{
        [self creatUI];
        [self judgeUser];
    }
    //清缓存
    [self cleanCache];
}
-(void)changeLang{
    _firstShowView = [[FBChangeLanguage alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH*0.7, fontAuto(400))];
    zhPopupController *popup = [[zhPopupController alloc] initWithView:_firstShowView size:CGSizeMake(VULSCREEN_WIDTH * 0.9, _firstShowView.height)];
    popup.layoutType = zhPopupLayoutTypeCenter;
    popup.dismissOnMaskTouched = NO;
    popup.presentationStyle = zhPopupSlideStyleFromBottom;
    popup.maskAlpha = 0.35;
    MJWeakSelf(self);

 

    _firstShowView.changeLanguageWithIndex = ^(NSInteger index) {
        NSUserDefaults *TimeOfBootCount = [NSUserDefaults standardUserDefaults];
        [TimeOfBootCount setValue:@"first" forKey:@"time"];
        [weakSelf creatUI];
        [weakSelf judgeUser];
        [popup dismiss];
    };
    [popup showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
}
-(void)viewWillAppear:(BOOL)animated{
//    [VULAccountManager sharedInstance].shareQrImage = @"";
//    [self cleanRegisteredBtn];

}
-(void)cleanRegisteredBtn{
    self.registeredBtn.hidden  = YES;

}
- (void)cleanCache {
    RLMRealm *realm = [RLMRealm defaultRealm];
    [realm beginWriteTransaction];
    [realm deleteAllObjects];
    [realm commitWriteTransaction];
    [NSArray bg_clearArrayWithName:@"backInfo"];
    [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"isClose"];
    [NSArray bg_clearArrayWithName:@"uploadedIdentifiers"];


}

//初始化登录界面
-(void)creatUI{
  
//    UBSaveCardInfoModel *cardInfoModel = [VULRealmDBManager getCurrentCardInfo];
   NSString *avatar =  [[NSUserDefaults standardUserDefaults] objectForKey:@"loginImg"];
    NSString *loginUserName =  [[NSUserDefaults standardUserDefaults] objectForKey:@"loginUserName"];
    if (loginUserName.length == 0) {
        loginUserName = @"您";
    }
    if (avatar.length >0) {
        self.appIconImg.layer.masksToBounds = YES;
        self.appIconImg.layer.cornerRadius = fontAuto(30);
        [self.appIconImg svg_setImageWithURL:[NSURL URLWithString:avatar] placeholderImage:VULGetImage(@"login_logo")];
        loginUserName = [NSString stringWithFormat:@"欢迎%@登录",loginUserName];
        self.titleLabel.text = KLanguage(loginUserName);
    }
    [self.view addSubview:self.appIconImg];
    [self.appIconImg mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(VULSCREEN_HEIGHT/2-fontAuto(200));
        make.left.mas_equalTo(fontAuto(34));
        make.height.width.mas_equalTo(fontAuto(60));
    }];
    
    UIButton *login_goin = [UIButton new];
    [login_goin setImage:VULGetImage(@"login_goin") forState:UIControlStateNormal];
    login_goin.hidden = YES;
    [self.view addSubview:login_goin];
    [login_goin mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.top.mas_equalTo(125 + K_NavBar_Height);
        make.top.mas_equalTo(K_StatusBar_Height+30);
        make.right.mas_equalTo(-30);
        make.width.and.height.mas_equalTo(24);
    }];
    [[login_goin rac_signalForControlEvents:UIControlEventTouchUpInside] subscribeNext:^(__kindof UIControl *_Nullable x) {
//        VULModuleWebVC *webVc = [[VULModuleWebVC alloc] init];
//
//        webVc.currentURL = @"https://www.xx.vip";
//
////        webVc.currentURL = @"https://www.xx.vip/xuanchuanye.shtml";
//        [self.navigationController pushViewController:webVc animated:YES];
    }];
    [self.view addSubview:self.titleLabel];
    [self.titleLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.appIconImg.mas_bottom).offset(fontAuto(15));
        make.left.mas_equalTo(fontAuto(30));
        make.width.mas_greaterThanOrEqualTo(fontAuto(32));
    }];
    [self.view addSubview:self.userLabelMark];
    [self.userLabelMark mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(fontAuto(20));
        make.left.mas_equalTo(fontAuto(30));
        make.width.mas_greaterThanOrEqualTo(fontAuto(32));
    }];
    [self.view addSubview:self.cloudImg];
    [self.cloudImg mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.userLabelMark.mas_bottom).offset(fontAuto(_isLogin?20:0));
        make.left.mas_equalTo(fontAuto(30));
        make.width.mas_equalTo(_isLogin?fontAuto(20):fontAuto(0));

    }];
    self.cloudText = [[UITextField alloc] init];
    self.cloudText.font = [UIFont systemFontOfSize:14];
    NSAttributedString *attrString3 = [[NSAttributedString alloc] initWithString:KLanguage(@"请输入您服务器地址") attributes:
                                      @{ NSForegroundColorAttributeName: [UIColor lightGrayColor] }];
    _cloudText.attributedPlaceholder = attrString3;
    _cloudText.keyboardType = UIKeyboardTypeURL;

    self.cloudText.textColor = HEXCOLOR(0x333333);
//    self.cloudText.delegate = self;
    [self.view addSubview:self.cloudText];
 
    [self.cloudText mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.cloudImg.mas_right).offset(kSpace);
        make.right.mas_equalTo(-fontAuto(29));
        make.height.mas_equalTo(_isLogin?fontAuto(20):fontAuto(0));
        make.centerY.mas_equalTo(self.cloudImg.mas_centerY);

    }];
    UIView *aline3 = [UIView new];
    aline3.backgroundColor = kLineColor;
    [self.view addSubview:aline3];
    [aline3 mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.cloudImg.mas_bottom).offset(fontAuto(_isLogin?10:0));
        make.left.mas_equalTo(self.cloudText.mas_left);
        make.right.equalTo(self.cloudText);
        make.height.equalTo(@1);
    }];
    
    //忘记密码
    self.loginUp = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.loginUp setImage:VULGetImage(@"login_up") forState:UIControlStateNormal];
    [self.view addSubview:self.loginUp];
    [self.loginUp mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(-fontAuto(30));
        make.height.mas_equalTo(20);
        make.centerY.mas_equalTo(self.cloudImg.mas_centerY);

    }];
    
    [[self.loginUp rac_signalForControlEvents:UIControlEventTouchUpInside] subscribeNext:^(__kindof UIControl *_Nullable x) {
        self.linkView.hidden = NO;
        self.bgView.hidden = NO;
    }];
    self.cloudImg.hidden = self.cloudText.hidden = self.loginUp.hidden = aline3.hidden = !_isLogin;
    [self.view addSubview:self.userLabel];
    [self.userLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(aline3.mas_bottom).offset(fontAuto(13));
        make.left.mas_equalTo(fontAuto(30));
        make.width.mas_equalTo(fontAuto(20));
    }];
    self.userName = [[UITextField alloc] init];
    self.userName.font = [UIFont systemFontOfSize:14];
    NSAttributedString *attrString = [[NSAttributedString alloc] initWithString:KLanguage(@"请输入账号或手机号") attributes:
                                      @{ NSForegroundColorAttributeName: [UIColor lightGrayColor] }];
    _userName.attributedPlaceholder = attrString;
    self.userName.textColor = HEXCOLOR(0x333333);
    self.userName.delegate = self;
    [self.view addSubview:self.userName];
    [self.userName mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.userLabel.mas_right).offset(kSpace).priorityHigh();
        make.right.mas_equalTo(-fontAuto(29));
        make.height.mas_equalTo(20);
        make.centerY.mas_equalTo(self.userLabel.mas_centerY);

    }];
    UIView *aline = [UIView new];
    aline.backgroundColor = kLineColor;
    [self.view addSubview:aline];
    [aline mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.userLabel.mas_bottom).offset(fontAuto(10));
        make.left.mas_equalTo(self.cloudText.mas_left);
        make.right.equalTo(self.userName);
        make.height.equalTo(@1);
    }];
    [self.view addSubview:self.passLabel];
    [self.passLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(aline.mas_bottom).offset(fontAuto(13));
        make.left.mas_equalTo(fontAuto(30));
        make.width.mas_equalTo(fontAuto(20));

    }];
    self.password = [[UITextField alloc] init];
    self.password.font = [UIFont systemFontOfSize:14];
    NSAttributedString *attrString1 = [[NSAttributedString alloc] initWithString:KLanguage(@"输入密码") attributes:
                                       @{ NSForegroundColorAttributeName: [UIColor lightGrayColor] }];
    _password.attributedPlaceholder = attrString1;
    self.password.textColor = HEXCOLOR(0x333333);
    [self.password setSecureTextEntry:YES];//设置密码形式
    [self.view addSubview:self.password];
    [self.password mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.passLabel.mas_right).offset(kSpace);
        make.right.mas_equalTo(-fontAuto(29));
        make.height.mas_equalTo(20);
        make.centerY.mas_equalTo(self.passLabel.mas_centerY);
    }];
    
    UIView *aline1 = [UIView new];
    aline1.backgroundColor = kLineColor;
    [self.view addSubview:aline1];
    [aline1 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.passLabel.mas_bottom).offset(fontAuto(10));
        make.left.mas_equalTo(self.cloudText.mas_left);
        make.right.equalTo(self.userName);
        make.height.equalTo(@1);
    }];
    [self.view addSubview:self.hidePasswordBtn];
    [self.hidePasswordBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.height.width.equalTo(@30);
        make.right.equalTo(self.password.mas_right).offset(-kSpace);
        make.centerY.equalTo(self.password.mas_centerY);
    }];
 
    //新用户注册
    
    self.registeredBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.registeredBtn setTitle:KLanguage(@"切换站点") forState:UIControlStateNormal];
    [self.registeredBtn setTitleColor:BtnColor forState:UIControlStateNormal];
    self.registeredBtn.titleLabel.font = [UIFont systemFontOfSize:14];
    self.registeredBtn.contentHorizontalAlignment=UIControlContentHorizontalAlignmentLeft;
    self.registeredBtn.hidden =  self.isLogin;
    [self.view addSubview:self.registeredBtn];
    [self.registeredBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.userLabel.mas_left);
        make.width.mas_greaterThanOrEqualTo(fontAuto(60));
        make.height.mas_equalTo(20);
        make.top.mas_equalTo(aline1.mas_bottom).offset(kSpace);
    }];
    //忘记密码
    self.forgetPassword = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.forgetPassword setTitle:KLanguage(@"忘记密码？") forState:UIControlStateNormal];
    self.forgetPassword.hidden = YES;
    [self.forgetPassword setTitleColor:BtnColor forState:UIControlStateNormal];
    self.forgetPassword.titleLabel.font = [UIFont systemFontOfSize:14];
    [self.view addSubview:self.forgetPassword];
    [self.forgetPassword mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self.password.mas_right).offset(10);
        make.width.mas_greaterThanOrEqualTo(85);
        make.height.mas_equalTo(20);
        make.top.mas_equalTo(aline1.mas_bottom).offset(kSpace);

    }];
    
    [self.view addSubview:self.agreeBtn];

    [self.agreeBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.passLabel.mas_left);
        make.width.height.mas_equalTo(fontAuto(20));
        make.top.mas_equalTo(self.forgetPassword.mas_bottom).offset(kSpace*3);
    }];
    
    [self.view addSubview:self.agressLabel];
    [self.agressLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.agreeBtn.mas_right).offset(fontAuto(0));
        make.centerY.mas_equalTo(self.agreeBtn.mas_centerY);
    }];

    self.loginBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    self.loginBtn.titleLabel.font = [UIFont systemFontOfSize:16];
    self.loginBtn.backgroundColor = BtnColor;
    [self.loginBtn setTitle:KLanguage(@"登 录") forState:UIControlStateNormal];
    [self.loginBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    self.loginBtn.layer.masksToBounds = YES;
    self.loginBtn.layer.cornerRadius = 20;
    self.loginBtn.enabled = NO;
    self.loginBtn.backgroundColor = BtnColor;
    [self.view addSubview:self.loginBtn];
    [self.loginBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.mas_equalTo(-(fontAuto(50)+K_BottomBar_Height));
        make.width.mas_equalTo(fontAuto(120));
        make.height.mas_equalTo(40);
        make.centerX.mas_equalTo(self.view.mas_centerX);
    }];
    [self.view addSubview:self.bgView];

    [self.view addSubview:self.linkView];
    [self.bgView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_offset(0);
    }];
    [self.linkView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(aline3.mas_bottom).offset(fontAuto(0));
        make.left.mas_equalTo(self.cloudText.mas_left);
        make.height.mas_equalTo(@200);
        make.width.mas_equalTo(VULSCREEN_WIDTH-kSpace-fontAuto(29)-fontAuto(50));
    }];
    self.linkView.chooseNowUrl = ^(NSString * _Nonnull str) {
        self.cloudText.text =str;
        self.baseUrl =str;
        [self showLinkView];
    };
    self.bgView.userInteractionEnabled = YES;
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(showLinkView)];
    [self.bgView addGestureRecognizer:tap];
    
    if (IS_DEBUG) {
        self.cloudText.text = @"https://test.filesbox.cn";
        self.userName.text = @"admin";
        self.password.text = @"Wujihl12!@";
        
        self.baseUrl = @"https://test.filesbox.cn";
        self.userNameStr = @"admin";
        self.passwordStr = @"Wujihl12!@";
    }

    NSString *baseUrl = [[NSUserDefaults standardUserDefaults] objectForKey:@"baseUrl"];
    if (!_isLogin) {
        self.cloudText.text = baseUrl;
        self.baseUrl = baseUrl;

    }
    [self updateloginBtn];

}
-(void)showLinkView{
    self.bgView.hidden = YES;
    self.linkView.hidden = YES;
}
//用户登录账号密码处理
- (void)judgeUser {
    // TODO:用户名和密码
    
    [[self.cloudText rac_signalForControlEvents:UIControlEventEditingDidEndOnExit] subscribeNext:^(id x) {
        UITextField *userNameTF = x;
        self.baseUrl = [userNameTF.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        [self updateloginBtn];

    }];
    [[self.cloudText rac_signalForControlEvents:UIControlEventEditingDidEnd] subscribeNext:^(id x) {
        UITextField *userNameTF = x;
        self.baseUrl = [userNameTF.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        [self updateloginBtn];

    }];
    [[self.userName rac_signalForControlEvents:UIControlEventEditingDidEndOnExit] subscribeNext:^(id x) {
        UITextField *userNameTF = x;
        self.userNameStr = [userNameTF.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        [self updateloginBtn];

    }];

    [[self.userName rac_signalForControlEvents:UIControlEventEditingDidEnd] subscribeNext:^(id x) {
        UITextField *userNameTF = x;
        self.userNameStr = [userNameTF.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        [self updateloginBtn];

    }];

    [[self.password rac_signalForControlEvents:UIControlEventEditingDidEndOnExit] subscribeNext:^(id x) {
        UITextField *passwordTF = x;
        self.passwordStr = [passwordTF.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        [self updateloginBtn];

    }];

    [[self.password rac_signalForControlEvents:UIControlEventEditingDidEnd] subscribeNext:^(id x) {
        UITextField *passwordTF = x;
        // 过滤一下首尾空格
        self.passwordStr = [passwordTF.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        [self updateloginBtn];

    }];
    [[self.agreeBtn rac_signalForControlEvents:UIControlEventTouchUpInside] subscribeNext:^(__kindof UIControl *_Nullable x) {
        self.agreeBtn.selected = !self.agreeBtn.selected;
        [self updateloginBtn];
    }];
    [[self.hidePasswordBtn rac_signalForControlEvents:UIControlEventTouchUpInside] subscribeNext:^(__kindof UIControl *_Nullable x) {
        [self.password setSecureTextEntry:x.selected];//设置密码形式
        if (x.selected) {
            [self.hidePasswordBtn setImage:VULGetImage(@"password_hide") forState:UIControlStateNormal];
        } else {
            [self.hidePasswordBtn setImage:VULGetImage(@"password_show") forState:UIControlStateNormal];
        }
        x.selected = !x.selected;
    }];

    // TODO:登录
    [[self.loginBtn rac_signalForControlEvents:UIControlEventTouchUpInside] subscribeNext:^(id x) {
        [self.view endEditing:YES];
        
        if (!self.agreeBtn.isSelected) {
            [self makeToast:KLanguage(@"请阅读并同意《服务条款》和《隐私政策》")];
            return;
        }
        if (!self.userNameStr) {
            self.userNameStr = [self.userName.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        }
        if (!self.passwordStr) {
            self.passwordStr = [self.password.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        }
        if (!self.baseUrl) {
            self.baseUrl = [self.cloudText.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        }
        if (!NSStringIsNotEmpty(self.baseUrl)) {
            // 用户名或者密码为空 弹窗
            [self makeToast:KLanguage(@"请输入服务器IP地址或者域名如（192.168.0.200 或 demo.filesbox.cn）")];
            return;
        }
        
        if ([self.baseUrl hasPrefix:@"https://"] || [self.baseUrl hasPrefix:@"http://"] ) {
        } else if([self.baseUrl hasPrefix:@"//"]){
            self.baseUrl =[NSString stringWithFormat:@"http:%@",self.baseUrl];

        }else{
            self.baseUrl =[NSString stringWithFormat:@"https://%@",self.baseUrl];
        }
//        if ([self.baseUrl hasPrefix:@"https://"] || [self.baseUrl hasPrefix:@"http://"]  ) {
//            
//        }else{
//            [self vul_showAlertWithTitle:@"" message:KLanguage(@"请输入正确的服务器地址。注http://或者https://不可省略;") appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
//                alertMaker.addActionDestructiveTitle(@"确定");
//
//                        } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
//                            
//                        }];
//            
//        }
        
        if (!NSStringIsNotEmpty(self.userNameStr)) {
            // 用户名或者密码为空 弹窗
            [self makeToast:KLanguage(@"请填写用户名")];
            return;
        }
        if (!NSStringIsNotEmpty(self.passwordStr)) {
            [self makeToast:KLanguage(@"请填写密码")];
            return;
        }

        [DownloadProgress sharedInstance].isWeb = NO;

        // 开始登录加载动画
        YTKNetworkConfig *config = [YTKNetworkConfig sharedConfig];
        config.baseUrl = self.baseUrl;
        [self showWaitHudWithString:@"加载中..."];
        VULRequestLoginModel *loginModel = [[VULRequestLoginModel alloc] initWithUsername:self.userNameStr password:self.passwordStr encrypted:YES];
        //获取学校列表
        [loginModel startWithCompletionBlockWithSuccess:^(__kindof YTKBaseRequest *_Nonnull request) {
            NSString *success =request.responseObject[@"success"];
            if (success.boolValue) {
                [VULRealmDBManager updateLocaToken:[request.responseObject[@"data"] objectForKey:@"token"] andUserID:[request.responseObject[@"data"] objectForKey:@"userID"]];
                [[NSUserDefaults standardUserDefaults] setObject:self.userNameStr forKey:@"userName"];
                [[NSUserDefaults standardUserDefaults] setObject:self.passwordStr forKey:@"passWord"];

                NSString *b = [self.baseUrl substringFromIndex:self.baseUrl.length-1];
                if (![b isEqualToString:@"/"]) {
                    self.baseUrl = [NSString stringWithFormat:@"%@/",self.baseUrl];
                }
                [[NSUserDefaults standardUserDefaults] setObject:self.baseUrl forKey:@"baseUrl"];
                NSMutableArray *saveSelect = [NSMutableArray array];
                if (![saveSelect containsObject:self.baseUrl]) {
                    [saveSelect addObject:self.baseUrl];
                    [saveSelect bg_saveArrayWithName:@"baseUrlArr"];
                }
                [self getFileBoxInfo];
            }else{
                [self dissmissHudView];
                NSString *code =request.responseObject[@"code"];
                if([code isEqualToString:@"admin.setting.passwordErrorLock"]){
                    [ self vul_showAlertWithTitle:KLanguage(@"温馨提示") message:KLanguage(@"很抱歉，您多次密码输入错误，账号已被锁定，请10分钟之后再试。") appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
                        alertMaker.addActionDestructiveTitle(KLanguage(@"确定"));

                     } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
                     
                     } ];
                }else{
                    [self makeToast:request.responseObject[@"message"]];
                }

            }
            
        } failure:^(__kindof YTKBaseRequest *_Nonnull request) {
            [self dissmissHudView];
            NSString *status = [NSString stringWithFormat:@"%@",request.responseObject[@"status"]];
            if (!status || [status isEqualToString:@"(null)"] ) {
                [self makeToast:KLanguage(@"服务器IP地址或者域名不正确")];
            }
            if(status.integerValue == 404){
                [self makeToast:KLanguage(@"服务器IP地址或者域名不正确")];

            }
            
        }];
    }];

    // TODO:注册
    MJWeakSelf(self);

    [[self.registeredBtn rac_signalForControlEvents:UIControlEventTouchUpInside] subscribeNext:^(id x) {
        self.agreeBtn = nil;
        self.hidePasswordBtn = nil;
        [self.view removeAllSubviews];
        _isLogin = YES;
        [weakSelf creatUI];
        [weakSelf judgeUser];
    }];

    // TODO:忘记密码
    [[self.forgetPassword rac_signalForControlEvents:UIControlEventTouchUpInside] subscribeNext:^(id x) {
        // 跳转忘记密码页面
        NSLog(@"跳转忘记密码页面");
//        VULForgetPsdVC *forgetPassword = [[VULForgetPsdVC alloc] init];
//        [self.navigationController pushViewController:forgetPassword animated:YES];
    }];

    
}
-(void)updateloginBtn{
    
    if (self.userNameStr.length>0 && self.passwordStr.length>0 && self.baseUrl.length>0 && self.agreeBtn.selected) {
        self.loginBtn.enabled = YES;
        self.loginBtn.backgroundColor = BtnColor;

    }else{
        self.loginBtn.enabled = NO;
        self.loginBtn.backgroundColor = [BtnColor colorWithAlphaComponent:0.4];
    }
}

-(void)getFileBoxInfo{
    [VULBaseRequest requestWithUrl:@"/api/disk/options" params:nil requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest * _Nonnull request) {
        [self dissmissHudView];
        NSString *success =request.responseObject[@"success"];

        if (success.boolValue) {
            //TODO: 获取登录信息
//            NSString *zh_CN = request.data[@"lang"];
//            if ([zh_CN isEqualToString:@"zh_CN"]) {
//                [[NSUserDefaults standardUserDefaults] setObject:@"zh-Hans" forKey:@"appLanguage"];
//            }else{
//                [[NSUserDefaults standardUserDefaults] setObject:@"en" forKey:@"appLanguage"];
//            }
            VULResponseLoginModel *loginModel = [VULResponseLoginModel modelWithDictionary:request.responseObject[@"data"][@"user"]];
            [VULRealmDBManager updateLocaPersonalInformation:loginModel];
            
            [[NSUserDefaults standardUserDefaults] setObject:[NSString stringWithFormat:@"%@",request.responseObject[@"data"][@"targetSpace"][@"sizeMax"]] forKey:@"sizeMax"];
            [[NSUserDefaults standardUserDefaults] setObject:request.data[@"role"] forKey:@"role"];
            [[NSUserDefaults standardUserDefaults] setObject:request.data[@"markConfig"][@"shareLinkAllow"] forKey:@"shareLinkAllow"];
            [[NSUserDefaults standardUserDefaults] setObject:[NSString stringWithFormat:@"%@",request.responseObject[@"data"][@"targetSpace"][@"sizeUse"]] forKey:@"sizeUse"];
            [[NSUserDefaults standardUserDefaults] setObject:request.data[@"treeOpen"] forKey:@"treeOpen"];
            [[NSUserDefaults standardUserDefaults] setObject:request.data[@"desktop"][@"sourceID"] forKey:@"sourceID"];

            FBTabBarControllerConfig *teacherTabbarConfig = [[FBTabBarControllerConfig alloc] init];
            [self changeRootViewController:teacherTabbarConfig.tabBarController];
            if( [DownloadProgress sharedInstance].isShare){
                [DownloadProgress sharedInstance].isShare = NO;
                // 处理自定义 URL，例如打开特定的页面
                [NSObject cyl_dismissAll:^{
                    [[NSObject cyl_currentNavigationController] popToRootViewControllerAnimated:NO];
                    FBHomeViewController *vc = [FBHomeViewController new];
                    vc.isMove = YES;
                    vc.icon = @"upload";
                    vc.operation = @"upload";
                    [[NSObject cyl_currentNavigationController] presentViewController:vc animated:YES completion:nil];
                }];
            }
        }
    }];
}

#pragma mark - 网络请求



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

- (void)success:(YTKBaseRequest *)request {
    
}
-(UIImageView *)rightImg{
    if (!_rightImg) {
        _rightImg = [UIImageView new];
        _rightImg.image = VULGetImage(@"right_login_bg");
    }
    return _rightImg;
}
- (void)responseError:(YTKBaseRequest *)request {
    [self dissmissHudView];
    if ([request.responseObject[@"code"] integerValue] == 401) {
        [self makeToast:@"请重新登录~"];
        VULLoginViewController *loginVC = [VULLoginViewController new];
        [self.navigationController pushViewController:loginVC animated:YES];
        return;
    }
    NSString *errMsg = [NSString stringWithFormat:@"%@", request.responseObject[@"message"]];
    [self makeToast:errMsg];
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    if ([string containsString:@" "] || [string isEqualToString:@" "]) {
        return NO;
    }
    return YES;
}

// 是否支持自动转屏
- (BOOL)shouldAutorotate {
    return YES;
}

// 支持哪些屏幕方向
- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return UIInterfaceOrientationMaskPortrait;
}

// 默认的屏幕方向（当前ViewController必须是通过模态出来的UIViewController（模态带导航的无效）方式展现出来的，才会调用这个方法）
- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation {
    return UIInterfaceOrientationPortrait;
}


- (VULButton *)hidePasswordBtn {
    if (!_hidePasswordBtn) {
        _hidePasswordBtn = [VULButton createImgBtnWithFrame:CGRectZero imgNamed:@"password_hide" Target:nil Sel:nil];
    }
    return _hidePasswordBtn;
}
- (VULButton *)agreeBtn {
    if (!_agreeBtn) {
        _agreeBtn = [VULButton createImgBtnWithFrame:CGRectZero imgNamed:@"login_agree_no" Target:nil Sel:nil];
        
        [_agreeBtn setImage:VULGetImage(@"login_agree_no") forState:UIControlStateNormal];
        [_agreeBtn setImage:VULGetImage(@"login_agree") forState:UIControlStateSelected];

    }
    return _agreeBtn;
}


- (VULSvgImageView *)appIconImg {
    if (!_appIconImg) {
        _appIconImg = [[VULSvgImageView alloc] init];
        _appIconImg.image = VULGetImage(@"login_logo");
        _appIconImg.contentMode = UIViewContentModeScaleAspectFit;
        _appIconImg.layer.masksToBounds = YES;
        _appIconImg.layer.cornerRadius = 5;

    }
    return _appIconImg;
}

- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"欢迎登录") TxtAlignment:NSTextAlignmentLeft Font:[UIFont boldSystemFontOfSize:25] Color:[UIColor blackColor] BgColor:nil];
    }
    return _titleLabel;
}
-(FBHistoryLinkView *)linkView{
    if (!_linkView) {
        _linkView = [[FBHistoryLinkView alloc] initWithFrame:CGRectZero];
        _linkView.hidden = YES;
    }
    return _linkView;
}
-(UIView *)bgView{
    if (!_bgView) {
        _bgView = [UIView new];
        _bgView.backgroundColor = [HEXCOLOR(0x000000) colorWithAlphaComponent:0.4];
        _bgView.hidden = YES;
    }
    return _bgView;
}


- (VULLabel *)userLabelMark {
    if (!_userLabelMark) {
        _userLabelMark = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"用户登录")  TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:16] Color:[UIColor blackColor] BgColor:nil];
    }
    return _userLabelMark;
}

- (UIImageView *)userLabel {
    if (!_userLabel) {
        _userLabel = [UIImageView new];
        _userLabel.image = VULGetImage(@"login_user");
        _userLabel.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _userLabel;
}
- (UIImageView *)passLabel {
    if (!_passLabel) {
        _passLabel = [UIImageView new];
        _passLabel.image = VULGetImage(@"login_password");
        _userLabel.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _passLabel;
}

- (UIImageView *)cloudImg {
    if (!_cloudImg) {
        _cloudImg = [UIImageView new];
        _cloudImg.image = VULGetImage(@"login_cloud");
        _userLabel.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _cloudImg;
}



- (YYLabel *)agressLabel {
    if (!_agressLabel) {
        _agressLabel = [[YYLabel alloc] init];
        WeakSelf(self);
        //设置整段字符串的颜色
        UIColor *color = [UIColor blackColor];
        NSDictionary *attributes = @{ NSFontAttributeName: [UIFont fontWithName:@"PingFangSC-Regular" size:12], NSForegroundColorAttributeName: color };
        NSString *string = [NSString stringWithFormat:@"  %@%@%@",KLanguage(@"已阅读并同意"),KLanguage(@"《服务条款》"),KLanguage(@"《隐私政策》")];
        NSMutableAttributedString *text = [[NSMutableAttributedString alloc] initWithString:string attributes:attributes];
        //设置高亮色和点击事件
        [text setTextHighlightRange:[[text string] rangeOfString:KLanguage(@"《服务条款》")] color:BtnColor backgroundColor:[UIColor clearColor] tapAction:^(UIView *_Nonnull containerView, NSAttributedString *_Nonnull text, NSRange range, CGRect rect) {
            NSLog(@"点击了《服务条款》");
            NSLog(@"点击了《服务条款》");
            VULBaseWebViewVC *webVc = [[VULBaseWebViewVC alloc] init];
            webVc.currentURL = [NSString stringWithFormat:@"%@/pages/schoolRegister/fbxAppPolicy.html",@"https://test.1x.cn"];
            [self.navigationController pushViewController:webVc animated:YES];
            
        }];

        //设置高亮色和点击事件
        [text setTextHighlightRange:[[text string] rangeOfString:KLanguage(@"《隐私政策》")] color:BtnColor backgroundColor:[UIColor clearColor] tapAction:^(UIView *_Nonnull containerView, NSAttributedString *_Nonnull text, NSRange range, CGRect rect) {
            NSLog(@"点击了《隐私政策》");
            VULBaseWebViewVC *webVc = [[VULBaseWebViewVC alloc] init];
            webVc.currentURL = [NSString stringWithFormat:@"%@/pages/schoolRegister/fbxAppProtocol.html",@"https://test.1x.cn"];
            [self.navigationController pushViewController:webVc animated:YES];
        }];

        _agressLabel.attributedText = text;
        //居中显示一定要放在这里，放在viewDidLoad不起作用
        _agressLabel.textAlignment = NSTextAlignmentLeft;
    }
    return _agressLabel;
}



//- (YYLabel *)agressLabel {
//    if (!_agressLabel) {
//        _agressLabel = [[YYLabel alloc] init];
//        WeakSelf(self);
//        //设置整段字符串的颜色
//        UIColor *color = [UIColor blackColor];
//        NSDictionary *attributes = @{ NSFontAttributeName: [UIFont fontWithName:@"PingFangSC-Regular" size:12], NSForegroundColorAttributeName: color };
//
//        NSMutableAttributedString *text = [[NSMutableAttributedString alloc] initWithString:@"  登录代表您已阅读并同意《服务条款》《隐私政策》" attributes:attributes];
//        //设置高亮色和点击事件
//        [text setTextHighlightRange:[[text string] rangeOfString:@"《服务条款》"] color:[UIColor orangeColor] backgroundColor:[UIColor clearColor] tapAction:^(UIView *_Nonnull containerView, NSAttributedString *_Nonnull text, NSRange range, CGRect rect) {
//            NSLog(@"点击了《服务条款》");
//            VULModuleWebVC *webVc = [[VULModuleWebVC alloc] init];
//            webVc.naviTitle = @"服务条款";
//            webVc.currentURL = [NSString stringWithFormat:@"%@/pages/schoolRegister/company/serviceAgreement.html",kSchoolServiceUrl];
//            [weakself.navigationController pushViewController:webVc animated:YES];
//        }];
//
//        //设置高亮色和点击事件
//        [text setTextHighlightRange:[[text string] rangeOfString:@"《隐私政策》"] color:[UIColor orangeColor] backgroundColor:[UIColor clearColor] tapAction:^(UIView *_Nonnull containerView, NSAttributedString *_Nonnull text, NSRange range, CGRect rect) {
//            NSLog(@"点击了《隐私政策》");
//            VULModuleWebVC *webVc = [[VULModuleWebVC alloc] init];
//            webVc.naviTitle = @"隐私政策";
//            webVc.currentURL = [NSString stringWithFormat:@"%@/pages/schoolRegister/company/privacyProtocol.html",kSchoolServiceUrl];
//            [weakself.navigationController pushViewController:webVc animated:YES];
//        }];
//
//        _agressLabel.attributedText = text;
//        //居中显示一定要放在这里，放在viewDidLoad不起作用
//        _agressLabel.textAlignment = NSTextAlignmentCenter;
//    }
//    return _agressLabel;
//}

@end
