//
//  VULModuleWebVC.m
//  VideoULimit
//
//  Created by svnlan on 2019/3/26.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import "VULModuleWebVC.h"
#import <WebKit/WebKit.h>
////#import <UShareUI/UShareUI.h>
#import "VULShareSheetController.h"
#import "AppDelegate.h"
#import "CYLTabBarController.h"
#import "VULSendInfoWebVC.h"
#import "VULRadarDetailTrafficController.h"
#import "VULFillFormViewController.h"
#import "VULVisitorModel.h"
#import "VULRadarDetailClueController.h"
#import "VULRadarDetailCustomerController.h"
#import "VULAddCustomerController.h"
@interface VULModuleWebVC ()

///<WKNavigationDelegate, WKUIDelegate, WKScriptMessageHandler>
//{
//    BOOL _authenticated;
//}
//@property (weak, nonatomic) CALayer *progressLayer;
//@property (nonatomic, strong) NSURLConnection *urlConnection;
//@property (nonatomic, strong) NSURLRequest *request;
//@property (nonatomic, strong) NSURL *currentUrl;
//@property (nonatomic, strong) NSURL *startUrl;
@property (nonatomic, strong) VULButton *addButton;
@property (nonatomic, strong) VULButton *finishBtn;//预览
@property (nonatomic, strong) NSString *formUserSubmitId;//预览
@property (nonatomic, strong) UIButton *rightDetial;

@end

@implementation VULModuleWebVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.navigationView.hidden = NO;
    self.navigationTitle = self.naviTitle;
    [self baseAddNavLeftBtnWithImage:VULGetImage(@"back") selector:@selector(navigationBack)];
    if (self.association) {
      [self baseAddNavRightBtnWithImage:VULGetImage(@"关联设置") selector:@selector(gotoAssociation)];
    }
    if (self.isUser) {
        [self.view addSubview:self.rightDetial];
        [self.rightDetial mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-10);
            make.top.mas_equalTo(K_StatusBar_Height);
            make.centerY.mas_equalTo(self.leftButton.mas_centerY);
        }];
    }
    if (self.formId) {
        [self.view addSubview:self.finishBtn];
    }
    [self.webView.view mas_updateConstraints:^(MASConstraintMaker *make) {
        make.top.offset(K_NavBar_Height);
        make.left.right.bottom.offset(0);
    }];
    NSString *newUserAgent =[NSString getCurrentDeviceUserAgent];
    [self.webView.webView setCustomUserAgent:newUserAgent];

    [self loadWebViewWithURLString:self.currentURL];
    
    WeakSelf(self)
    [self.webView addScriptMessageHandlerWithName:@[@"shareAction",@"addInfoSuccess",@"associatedSuccess",@"InformationAssociatedTextForIOS",@"addPhotoFormSuccessForIOS",@"TaskForIOSAssociated",@"AssociatedForIOSCancel",@"appCallTeleForIOS",@"appLoadFileForIOS",@"getProtocolTemplatePathForIOS",@"delAllEditFormForIOS",@"getShopDetailPathForIOS"] observeValue:^(WKUserContentController *userContentController, WKScriptMessage *message) {
        NSString *messageName = message.name;
        if ([messageName isEqualToString:@"shareAction"]) {
            VULSaveUserInfo *userModel = [[VULAccountManager sharedInstance] getAccountInfo];
            NSString *nameStr;
            if (NSStringIsNotEmpty(userModel.realName)) {
                nameStr = userModel.realName;
            } else {
                nameStr = @"我";
            }
            NSString *titleStr = weakself.webView.webView.title;
            NSString *busTypeStr = @"0";
            NSString *busIdStr = @"0";
            if ([weakself.currentURL containsString:@"/shop/item.html"]) { //商品
                titleStr = [NSString stringWithFormat:@"%@向你推荐——%@,快去看看吧~", nameStr, weakself.webView.webView.title];
                busTypeStr = @"4";
                NSArray *array = [weakself.currentURL componentsSeparatedByString:@"?itemid="];
                busIdStr = array[1];
            }
            [[VULShareManager sharedInstance] setShareVC:weakself title:titleStr content:@"" image:VULGetImage(@"AppIcon") url:weakself.currentURL busId:busIdStr busType:busTypeStr isTopItem:YES];
        } else if ([messageName isEqualToString:@"addInfoSuccess"]) { //发布资讯or短视频
            [weakself makeToast:@"发布成功"];
            NSString *name = message.body;
            if (name.integerValue == 1) {
                if (self.backBlock) {
                    self.backBlock();
                }
            }else{
                [VULNotificationCenter postNotificationName:@"addInfoNotification" object:nil];
            }
        
            [weakself.navigationController popViewControllerAnimated:YES];
        }else if ([messageName isEqualToString:@"associatedSuccess"]) {
            NSLog(@"%@",message.body);
            [weakself makeToast:@"关联成功"];
            [weakself.navigationController popViewControllerAnimated:YES];
            if (self.backMessBlock) {
                self.backMessBlock();
            }
        }else if ([messageName isEqualToString:@"addPhotoFormSuccessForIOS"]) {
            NSLog(@"%@",message.body);
            
            if (self.photoSucessBlock) {
                self.photoSucessBlock([NSString stringWithFormat:@"%@",message.body]);
            }
            [weakself.navigationController popViewControllerAnimated:YES];
        }else if ([messageName isEqualToString:@"getProtocolTemplatePathForIOS"]) {
            NSLog(@"%@",message.body);
            
            if (self.photoSucessBlock) {
                self.photoSucessBlock([NSString stringWithFormat:@"%@",message.body]);
            }
            [weakself.navigationController popViewControllerAnimated:YES];
        }
        else if ([messageName isEqualToString:@"InformationAssociatedTextForIOS"]) {
            NSLog(@"%@",message.body);
            ;
            [[NSUserDefaults standardUserDefaults] setObject:message.body forKey:weakself.listModel.commonHomepageInfoId];
        }
        else if ([messageName isEqualToString:@"TaskForIOSAssociated"]) {
            NSLog(@"%@",message.body);
            ;
            NSData *jsonData = [message.body dataUsingEncoding:NSUTF8StringEncoding];
            NSError *error = nil;
            NSArray *arr = [NSJSONSerialization JSONObjectWithData:jsonData
                        options:NSJSONReadingMutableContainers
                            error:&error];
            NSDictionary *dic = arr[0];
            if (self.contentIdBack) {
                self.contentIdBack(dic);
                [weakself.navigationController popViewControllerAnimated:YES];

            }
//            [[NSUserDefaults standardUserDefaults] setObject:message.body forKey:weakself.listModel.commonHomepageInfoId];
        }else if([messageName isEqualToString:@"AssociatedForIOSCancel"]){
            [weakself.navigationController popViewControllerAnimated:YES];

        }else if([messageName isEqualToString:@"delAllEditFormForIOS"]){
            [weakself.navigationController popViewControllerAnimated:YES];

        }else if([messageName isEqualToString:@"appCallTeleForIOS"]){
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"tel://%@", message.body]]];
        }else if([messageName isEqualToString:@"appLoadFileForIOS"]){
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@", message.body]]];
        }else if ([messageName isEqualToString:@"getShopDetailPathForIOS"]) {
            NSLog(@"%@",message.body);
            
            if (self.photoSucessBlock) {
                self.photoSucessBlock([NSString stringWithFormat:@"%@",message.body]);
            }
            [weakself.navigationController popViewControllerAnimated:YES];
        }
        
    }];
    
    NSString *injectionJSString = @"var script = document.createElement('meta');"
        "script.name = 'viewport';" "script.content=\"width=device-width, initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\";" "document.getElementsByTagName('head')[0].appendChild(script);localStorage.setItem('appAdapt', 'true');var comHead = document.getElementsByClassName('head comHead')[0]; comHead.style.display = 'none'; var topHead = document.getElementsByClassName('hed_top')[0]; topHead.style.display = 'none';";
    [self.webView callJS:injectionJSString];
    
    
//    WKWebViewConfiguration *config = [WKWebViewConfiguration new];
//    config.preferences = [WKPreferences new];
//    //是否支持JavaScript
//    config.preferences.javaScriptEnabled = YES;
//    config.userContentController = [[WKUserContentController alloc] init];
//
//    //注册js方法
//    [config.userContentController addScriptMessageHandler:self name:@"shareAction"];
//    [config.userContentController addScriptMessageHandler:self name:@"socketAction"];
//    [config.userContentController addScriptMessageHandler:self name:@"xxBrowserSendJson"];
//
//    self.WKwebView = [[WKWebView alloc] initWithFrame:CGRectZero configuration:config];
//
//    self.WKwebView.navigationDelegate = self;
//    self.WKwebView.UIDelegate = self;
//    //开了支持滑动返回
//    self.WKwebView.allowsBackForwardNavigationGestures = NO;
//    [self.view addSubview:self.WKwebView];
//    CGFloat originY = self.hideNavi ? 0 : K_NavBar_Height;
//    CGFloat bottomOffset = self.hideNavi ? 0 : K_BottomBar_Height;
//    [self.WKwebView mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.top.offset(originY);
//        make.left.right.equalTo(self.view);
//        make.bottom.offset(-bottomOffset);
//    }];
//    self.startUrl = [NSURL URLWithString:self.currentURL];
//    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:self.currentURL]];
//    [request setValue:[VULRealmDBManager getLocalToken] forHTTPHeaderField:@"token"];
//    [request setValue:@"1" forHTTPHeaderField:@"isApp"];
//    NSString *strSysName = [[UIDevice currentDevice] systemName];
//    NSString *strSysVersion = [[UIDevice currentDevice] systemVersion];
//    [request setValue:kCurrentVersion forHTTPHeaderField:@"appVersion"];
//    [request setValue:[NSString stringWithFormat:@"%@%@", strSysName, strSysVersion] forHTTPHeaderField:@"os"];
//    [request setValue:[NSString getDeviceModelName] forHTTPHeaderField:@"deviceName"];
//    [self.WKwebView loadRequest:request];
//
//    //加载进度条
//    UIView *progress = [[UIView alloc] initWithFrame:CGRectMake(0, originY, CGRectGetWidth(self.view.frame), 2)];
//    progress.backgroundColor = [UIColor clearColor];
//    [self.view addSubview:progress];
//    CALayer *layer = [CALayer layer];
//    layer.frame = CGRectMake(0, 0, 0, 2);
//    layer.backgroundColor = HEXCOLOR(0x73B6F5).CGColor;
//    [progress.layer addSublayer:layer];
//    self.progressLayer = layer;
//    [self.WKwebView addObserver:self forKeyPath:@"estimatedProgress" options:NSKeyValueObservingOptionNew context:nil];
//    [self.WKwebView addObserver:self forKeyPath:@"title" options:NSKeyValueObservingOptionNew context:NULL];
    if (self.isUser) {
        NSMutableDictionary *dic =getURLParameters(self.currentURL);
        if ([dic.allKeys containsObject:@"formUserSubmitId"]) {
            self.formUserSubmitId = dic[@"formUserSubmitId"];
        }
    }
}
-(UIButton *)rightDetial{
    if (!_rightDetial) {
        _rightDetial = [UIButton new];
        [_rightDetial setTitle:@"用户详情" forState:UIControlStateNormal];
//        _rightDetial = [VULButton createNewBtnWithFrame:CGRectZero title:@"用户详情" imageName:@"" Target:self Sel:@selector(gotoDetail) ];
        [_rightDetial setTitleColor: HEXCOLOR(0x1976D2) forState:UIControlStateNormal];
        _rightDetial.titleLabel.font = [UIFont yk_pingFangRegular:fontAuto(14)];
        [_rightDetial addTarget:self action:@selector(gotoDetail) forControlEvents: UIControlEventTouchUpInside];
    }
    return _rightDetial;
}
-(void)gotoDetail{
    [VULBaseRequest requestWithUrl:@"/api/operate/getTrafficInfoByFusId"
                            params:@{@"formUserSubmitId":self.formUserSubmitId}
                       requestType:YTKRequestMethodGET
                        completion:^(__kindof VULBaseRequest * _Nonnull request) {
        if (request.success) {
            NSDictionary *dic = request.data;
            if (dic.allKeys.count == 0) {
                [self gotoAddClue];
                return;;
            }
            NSString *visitorType = dic [@"visitorType"];
            NSString *clueId = dic [@"clueId"];
            NSString *trafficId = dic [@"trafficId"];

             VULRadarModel *model = [VULRadarModel new];
            model.visitorType =visitorType.integerValue;
            model.clueId = clueId.integerValue;
            model.trafficId = trafficId;
             switch (model.visitorType) {
                 case 0:
                 {
                     VULRadarDetailTrafficController *vc = [VULRadarDetailTrafficController new];
                     vc.model = model;
                     [self.navigationController pushViewController:vc animated:YES];
                 }
                     break;
                     
                 case 1:
                 {
                     VULRadarDetailClueController *vc = [VULRadarDetailClueController new];
                     vc.model = model;
                     [self.navigationController pushViewController:vc animated:YES];
                 }
                     break;
                     
                 case 2:
                 {
                     VULRadarDetailCustomerController *vc = [VULRadarDetailCustomerController new];
                     vc.model = model;
                     [self.navigationController pushViewController:vc animated:YES];
                 }
                     break;
                     
                 default:
                     break;
             }
        }
        
    }];
}
-(void)gotoAddClue{
    [VULBaseRequest requestWithUrl:@"/api/common/findFormAnswerDetail"
                            params:@{@"formUserSubmitId":self.formUserSubmitId}
                       requestType:YTKRequestMethodGET
                        completion:^(__kindof VULBaseRequest * _Nonnull request) {
        if (request.success) {
            NSDictionary *dic = request.data;
            NSArray *itemList = request.data[@"itemList"];
            NSString *name   = @"";
            NSString *phone   = @"";
            NSArray *answerList = request.data[@"answerList"];
            for (NSDictionary *dic in itemList) {
                NSString *type = dic[@"type"];
                if (type.integerValue  == 34) {
                    if (name.length == 0) {
                        name =  [self answerWithNum:dic[@"num"] AllData:answerList];
                    }
                }
                if (type.integerValue  == 35) {
                    if (phone.length == 0) {
                        phone =  [self answerWithNum:dic[@"num"] AllData:answerList];
                    }
                }
            }
            VULVisitorModel *model = [VULVisitorModel new];;
            model.visitorName = name;
            if(phone.length>0){
                VULVisitorContactModel *model1 = [VULVisitorContactModel new];
                model1.contactType = @"mobile";
                model1.contactVal = phone;
                model.contactList = @[model1];

            }
            model.formUserSubmitId = self.formUserSubmitId;
            VULAddCustomerController *vc = [VULAddCustomerController new];
            vc.visitorType = 1;
            vc.sourceMethod = 0;
            vc.visitorModel = model;
            [self.navigationController pushViewController:vc animated:YES];
        }
        
    }];
}
-(NSString *)answerWithNum:(NSString *)num AllData:(NSArray *)allData{
    NSString *result = @"";
    for (NSDictionary *info in allData) {
        NSString *num1 = info[@"num"];
        if (num.integerValue == num1.integerValue ) {
            result = info[@"answer"];
            break;
        }
    }
    return result;
}
-(void)gotoAssociation{
    WeakSelf(self)
    VULSendInfoWebVC *webVc = [[VULSendInfoWebVC alloc] init];
    webVc.naviTitle = @"默认关联";
    webVc.currentURL = [NSString stringWithFormat:@"%@/business/defaultAssociation.html?type=%@&token=%@", kSchoolServiceUrl,self.infoType,[VULRealmDBManager getLocalToken]];
    webVc.backMessBlock = ^{
        [self.webView reloadFromOrigin];
    };
    [self.navigationController pushViewController:webVc animated:YES];
}
- (void)navigationBack {
    if (self.isBack) {
        [self.navigationController popViewControllerAnimated:YES];
        return;
    }
    if ([self.webView.webView canGoBack]) {
        [self.webView.webView goBack];
    } else {
//        [self webViewStopLoading];
        if (self.backBlock) {
            self.backBlock();
        }
        [self.navigationController popViewControllerAnimated:YES];
    }
}

- (void)loadWebViewWithURLString:(NSString *)url {
    NSString *webURL = url;
    
    NSURL *URL = [NSURL URLWithString:webURL];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:URL];
    [request setCachePolicy:NSURLRequestReloadIgnoringLocalCacheData];
    
    if ([url hasPrefix:@"<"]) {
        [self.webView.webView loadHTMLString:url baseURL:nil];
    }
    else {
        [self.webView loadRequestURL:request];
    }
   
}

//- (void)webViewStopLoading {
//    [self.WKwebView stopLoading];
//    [self.WKwebView removeObserver:self forKeyPath:@"estimatedProgress"];
//    [self.WKwebView removeObserver:self forKeyPath:@"title"];
//    [self.WKwebView.configuration.userContentController removeScriptMessageHandlerForName:@"shareAction"];
//    [self.WKwebView.configuration.userContentController removeScriptMessageHandlerForName:@"socketAction"];
//    self.WKwebView.navigationDelegate = nil;
//    self.WKwebView.UIDelegate = nil;
//}

//#pragma mark - KVO监听网页加载i进度
//- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSKeyValueChangeKey, id> *)change context:(void *)context {
//    if ([keyPath isEqualToString:@"estimatedProgress"]) {
//        self.progressLayer.opacity = 1;
//        if ([change[@"new"] floatValue] < [change[@"old"] floatValue]) {
//            return;
//        }
//        self.progressLayer.frame = CGRectMake(0, 0, self.view.bounds.size.width * [change[@"new"] floatValue], 2);
//        if ([change[@"new"] floatValue] == 1) {
//            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(.4 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//                self.progressLayer.opacity = 0;
//            });
//            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//                self.progressLayer.frame = CGRectMake(0, 0, 0, 2);
//            });
//        }
//    } else if ([keyPath isEqualToString:@"title"]) {
//        if (object == self.WKwebView) {
//            [self setNavigationTitle:self.WKwebView.title];
//        } else {
//            [super observeValueForKeyPath:keyPath ofObject:object change:change context:context];
//        }
//    }
//}

//#pragma mark - WKUIDelegate
//// 页面开始加载时调用
- (void)webView:(WKWebView *)webView didStartProvisionalNavigation:(WKNavigation *)navigation {
        NSString *jsString = [NSString stringWithFormat:@"localStorage.setItem('appAdapt', 'true')"];
        [webView evaluateJavaScript:jsString completionHandler:nil];
}
//
//// 当内容开始返回时调用
//- (void)webView:(WKWebView *)webView didCommitNavigation:(WKNavigation *)navigation {
//}
//
// 页面加载完成之后调用
- (void)webView:(WKWebView *)webView didFinishNavigation:(WKNavigation *)navigation {//这里修改导航栏的标题，动态改变

    if (self.showShare) {
        [self.view addSubview:self.addButton];
    }
    if(!self.folderTitle){
        [webView evaluateJavaScript:@"document.title" completionHandler:^(id object, NSError * error) {
            if ([object isAvailable]) {
                self.navigationTitle = object;
                self.naviTitle = object;
            }

        }];
    }
  
    self.webView.view.hidden = NO;
    [self endWebLoading];
    
//    NSString *jScript = @"var meta = document.createElement('meta'); meta.setAttribute('name', 'viewport'); meta.setAttribute('content', 'width=device-width'); document.getElementsByTagName('head')[0].appendChild(meta);";
//    [self.webView callJS:jScript];

    NSString *injectionJSString = @"var script = document.createElement('meta');"
        "script.name = 'viewport';" "script.content=\"width=device-width, initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\";" "document.getElementsByTagName('head')[0].appendChild(script);localStorage.setItem('appAdapt', 'true');"; //var comHead = document.getElementsByClassName('head comHead')[0]; comHead.style.display = 'none';
    
    [webView evaluateJavaScript:injectionJSString completionHandler:^(id result, NSError *error) {
        NSLog(@"%@ -- %@",result, error);
    }];
    
    if (self.sourceDic != nil) {
        if (self.sourceDic.allKeys.count > 0) {
            NSMutableString *jsString = [NSMutableString stringWithCapacity:0];
            [self.sourceDic.allKeys enumerateObjectsUsingBlock:^(NSString *key, NSUInteger idx, BOOL * _Nonnull stop) {
                [jsString appendString:@"sessionStorage.setItem('"];
                [jsString appendString:key];
                [jsString appendString:@"', '"];
                [jsString appendString:self.sourceDic[key]];
                [jsString appendString:@"');"];
            }];
            
            NSLog(@"线索来源:  %@", jsString);
            [self.webView callJS:jsString];
        }
    }
}



// 在发送请求之前，决定是否跳转
- (void)webView:(WKWebView *)webView decidePolicyForNavigationAction:(WKNavigationAction *)navigationAction decisionHandler:(void (^)(WKNavigationActionPolicy))decisionHandler {
    WKNavigationActionPolicy actionPolicy = WKNavigationActionPolicyAllow;
    NSURL *URL = navigationAction.request.URL;
    
    if (self.isUser) {
        NSMutableDictionary *dic =getURLParameters(URL.absoluteString);
        if ([dic.allKeys containsObject:@"formUserSubmitId"]) {
            self.formUserSubmitId = dic[@"formUserSubmitId"];
        }
    }
    NSLog(@"URL:->>>>>>>>: %@", URL.absoluteString);
    //如果是跳转一个新页面
    if (self.isGoin) {
        if (navigationAction.targetFrame == nil) {
            [webView loadRequest:navigationAction.request];
        }
    }
    if (navigationAction.navigationType == WKNavigationTypeBackForward) {
        //判断是返回类型
        if ([URL.absoluteString containsString:@"vscard/views"]) {
            if (webView.backForwardList.backList.count>0) {
                decisionHandler(WKNavigationActionPolicyAllow);
            }else{
                decisionHandler(WKNavigationActionPolicyCancel);

                [self navigationBack];

            }
        }else{
            decisionHandler(WKNavigationActionPolicyCancel);
            [self navigationBack];

        }

        return;
    }

    if (NSStringIsNotEmpty([VULRealmDBManager getLocalToken])) {
        [webView evaluateJavaScript:[NSString stringWithFormat:@"document.cookie ='token=%@';", [VULRealmDBManager getLocalToken]] completionHandler:^(id result, NSError *error) {
            if (error) {
                NSLog(@"error %@", error);
            } else {
                NSLog(@"token：%@ 注入成功",[VULRealmDBManager getLocalToken]);
            }
        }];
    }
    
    if ([URL.absoluteString hasPrefix:[NSString stringWithFormat:@"%@%@%@%@%@%@%@%@%@",@"w",@"e",@"i",@"x",@"i",@"n",@":",@"/",@"/"]]) {
        if ([[UIApplication sharedApplication] canOpenURL:URL]) {
            if (@available(iOS 10.0, *)) {
                [[UIApplication sharedApplication] openURL:URL options:@{ UIApplicationOpenURLOptionUniversalLinksOnly: @NO } completionHandler:^(BOOL success) {}];
            }
            else {
                [[UIApplication sharedApplication] openURL:URL];
            }
        }
        else {
            [self vul_showAlertWithTitle:@"" message:@"请安装微信" appearanceProcess:^(VULAlertController *_Nonnull alertMaker) {
                alertMaker.
                addActionCancelTitle(@"知道了");
            } actionsBlock:^(NSInteger buttonIndex, UIAlertAction *_Nonnull action, VULAlertController *_Nonnull alertSelf) {}];
        }
        decisionHandler(WKNavigationActionPolicyCancel);
        [self.navigationController popViewControllerAnimated:YES];
        return;
    }
    
    //跳转到学习中心 免费课程报名成功后转到/myroom/#/learn/detail 付费课程付费成功后转到center
    if ([URL.absoluteString containsString:@"/myroom/#/learn/center"] || [URL.absoluteString containsString:@"/myroom/#/learn/detail"]) {
        [self backToCourseList];
        decisionHandler(WKNavigationActionPolicyCancel);
        return;
    }
    
    if ([URL.absoluteString containsString:@"https://itunes.apple.com/"] || [URL.absoluteString containsString:@"itms-appss://apps.apple.com/"]) {
        //打开链接地址
        [[UIApplication sharedApplication] openURL:URL];
        [self.navigationController popToRootViewControllerAnimated:YES];
        decisionHandler(WKNavigationActionPolicyCancel);
        return;
    }
    if (navigationAction.targetFrame == nil) {
        [webView loadRequest:navigationAction.request];
    }
    decisionHandler(actionPolicy);
}

//urlEncode编码
- (NSString *)urlEncodeStr:(NSString *)input {
    NSString *charactersToEscape = @"?!@#$^&%*+,:;='\"`<>()[]{}/\\| ";
    NSCharacterSet *allowedCharacters = [[NSCharacterSet characterSetWithCharactersInString:charactersToEscape] invertedSet];
    NSString *upSign = [input stringByAddingPercentEncodingWithAllowedCharacters:allowedCharacters];
    return upSign;
}

//urlEncode解码
- (NSString *)decoderUrlEncodeStr: (NSString *) input {
    NSMutableString *outputStr = [NSMutableString stringWithString:input];
    [outputStr replaceOccurrencesOfString:@"+" withString:@"" options:NSLiteralSearch range:NSMakeRange(0,[outputStr length])];
    return [outputStr stringByRemovingPercentEncoding];
}

- (void)backToCourseList {
    if (![self.currentURL containsString:@"/shop/item.html?itemid="]) {
        AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
        CYLTabBarController *tab = (CYLTabBarController *)delegate.window.rootViewController;
        tab.selectedIndex = 0;
    }

    [VULNotificationCenter postNotificationName:@"scrollToFirst" object:nil];
    [self.navigationController popToRootViewControllerAnimated:YES];
}


- (void)catchWeixinEvent:(NSURL *)eventUrl {
    if ([eventUrl.host isEqualToString:@"wap"]) {
        if ([eventUrl.relativePath isEqualToString:@"/pay"]) {
            if ([[UIApplication sharedApplication] canOpenURL:eventUrl]) {
                if (@available(iOS 10.0, *)) {
                    [[UIApplication sharedApplication] openURL:eventUrl options:@{ UIApplicationOpenURLOptionUniversalLinksOnly: @NO } completionHandler:^(BOOL success) {}];
                } else {
                    [[UIApplication sharedApplication] openURL:eventUrl];
                }
            }
        }
    }
}

#pragma mark - 按钮点击
- (void)addButtonAction:(VULButton *)sender {
    if(self.hiringModel){
        NSString *titleStr = self.hiringModel.title;
        NSString *descStr = self.hiringModel.introduce;
        NSString *coverUrl;
        VULSaveUserInformation *userInfo = [VULRealmDBManager getLocalUserInformational];
        NSString *aesUid = [NSString encyptPKCS5:[NSString stringWithFormat:@"%ld", userInfo.userId] WithKey:AESENCRYKEY];
        NSString *base64Uid = [[NSString stringWithFormat:@"%ld", userInfo.userId] base64EncodedString];
        NSString *codeStr = [NSString stringWithFormat:@"%@/information/%@.shtml?diliater=%@&shareId=%@", kSchoolServiceUrl, self.hiringModel.commonHomepageInfoId, aesUid, base64Uid];
        [[VULShareManager sharedInstance] setShareVC:self title:titleStr content:descStr image:coverUrl url:codeStr busId:self.hiringModel.commonHomepageInfoId busType:@"8" isTopItem:YES];
    }else if (self.webModel) {
        NSString *titleStr = self.webModel.title;
        NSString *descStr = self.webModel.introduce;
        NSString *coverUrl;
        if (self.webModel.picPathArr.count >= 1) {
            VULPictureModel *picModel = self.webModel.picPathArr.firstObject;
            coverUrl = resultsUrl( picModel.sourcePath) ;
        }
        if (!coverUrl && ![self.webModel.thumb hasSuffix:@"null"] && self.webModel.thumb.length>0) {
            coverUrl = resultsUrl(self.webModel.thumb) ;
        }
        if([self.webModel.thumb hasPrefix:@"http://"] ||[self.webModel.thumb hasPrefix:@"https://"])
        {
            coverUrl =  self.webModel.thumb;
        }
        if (!coverUrl) {
            VULResponseSchoolInfoModel *currentSchoolModel = [VULAccountManager sharedInstance].currentSchoolModel;
            coverUrl = [NSString stringWithFormat:@"%@%@", kCDNHostUrl, currentSchoolModel.logo];
        }
        VULSaveUserInformation *userInfo = [VULRealmDBManager getLocalUserInformational];
        NSString *aesUid = [NSString encyptPKCS5:[NSString stringWithFormat:@"%ld", userInfo.userId] WithKey:AESENCRYKEY];
        NSString *base64Uid = [[NSString stringWithFormat:@"%ld", userInfo.userId] base64EncodedString];
        NSString *codeStr = [NSString stringWithFormat:@"%@/information/%@.shtml?diliater=%@&shareId=%@", kSchoolServiceUrl, self.webModel.commonHomepageInfoId, aesUid, base64Uid];
        [[VULShareManager sharedInstance] setShareVC:self title:titleStr content:descStr image:coverUrl url:codeStr busId:self.webModel.commonHomepageInfoId busType:self.busTypeNew isTopItem:YES];
    }else if  (self.courseWareModel) {
        NSString *titleStr =self.courseWareModel.courseName ;
        NSString *descStr = self.courseWareModel.courseName;
        NSString *coverStr = [NSString stringWithFormat:@"%@%@", kCDNHostUrl, self.courseWareModel.cover];
        NSString *diliater = [NSString encyptPKCS5:[NSString stringWithFormat:@"%ld",[VULRealmDBManager getLocalUserInformational].userId] WithKey:AESENCRYKEY];
      NSString *url =   [NSString stringWithFormat:@"%@/pages/wap/info.html?cid=%@&diliater=%@", kSchoolServiceUrl, self.courseWareModel.courseId,diliater];
        [[VULShareManager sharedInstance] setShareVC:self title:titleStr content:descStr image:coverStr url:url busId:self.courseWareModel.courseId busType:@"3" isTopItem:YES];
    }
    else if (self.listModel) {
        
        NSString *diliater = [NSString encyptPKCS5:[NSString stringWithFormat:@"%ld",[VULRealmDBManager getLocalUserInformational].userId] WithKey:AESENCRYKEY];
        NSString *titleStr = self.listModel.title;
        NSString *descStr = self.listModel.introduce;

        NSString *coverStr;
        if (self.listModel.picPathArr.count > 0) {
            VULPictureModel *picModel = self.listModel.picPathArr[0];
            coverStr = [NSString stringWithFormat:@"%@%@", kCDNHostUrl, picModel.sourcePath];
        }
        if (!coverStr && ![self.listModel.thumb hasSuffix:@"null"] && self.listModel.thumb.length>0) {
            coverStr = [NSString stringWithFormat:@"%@%@", kCDNHostUrl, self.listModel.thumb];
        }
        if (!coverStr) {
            VULResponseSchoolInfoModel *currentSchoolModel = [VULAccountManager sharedInstance].currentSchoolModel;
            coverStr = [NSString stringWithFormat:@"%@%@", kCDNHostUrl, currentSchoolModel.logo];

        }
        NSString *associated =[[NSUserDefaults standardUserDefaults] objectForKey:self.listModel.commonHomepageInfoId];
        NSString *busType = self.listModel.infoType.intValue == 15 ?@"23":@"21";

        NSString *codeStr;
//        if ([self.listModel.infoType integerValue] == 15) {
//          codeStr = [NSString stringWithFormat:@"%@/information/%@.shtml?diliater=%@&associated=%@", kSchoolServiceUrl, self.listModel.commonHomepageInfoId,diliater,associated];
//
//        }else{
            codeStr = [NSString stringWithFormat:@"%@/pubinfo/%@.html?diliater=%@&associated=%@", kSchoolServiceUrl, self.listModel.commonHomepageInfoId,diliater,associated];
//        }
        [[VULShareManager sharedInstance] setShareVC:self title:titleStr content:descStr image:coverStr url:codeStr busId:self.listModel.commonHomepageInfoId busType:busType isTopItem:YES];
    }
    else if (self.shopModel) {
        NSString *titleStr = self.shopModel.shopName;
        NSString *descStr = self.naviTitle;
        NSArray *listArr = [NSString jsonToObject:self.shopModel.coverList];
        NSDictionary *coverItem = listArr.firstObject;
        NSString *url = [NSString stringWithFormat:@"%@%@",kCDNHostUrl,coverItem[@"sourcePath"]];
        NSString *codeStr = [NSString stringWithFormat:@"%@/shop/item.html?itemid=%@", kSchoolServiceUrl, _shopModel.shopId];
        [[VULShareManager sharedInstance] setShareVC:self title:titleStr content:descStr image:url url:codeStr busId:self.shopModel.shopId busType:@"4" isTopItem:YES];
    }
    else if (self.brochureModel) {
        NSString *titleStr = self.brochureModel.title;
        NSString *descStr = self.brochureModel.introduce;
        UBBrochureFileDetail *fileModel = self.brochureModel.fileDetailList.firstObject;
        VULSaveUserInformation *userInfo = [VULRealmDBManager getLocalUserInformational];
        NSString *base64Uid = [[NSString stringWithFormat:@"%ld", userInfo.userId] base64EncodedString];
        NSString *codeStr = [NSString stringWithFormat:@"%@/portal/preview/doc.html?aid=%@&shareId=%@", kSchoolServiceUrl, self.brochureModel.commonHomepageInfoId,base64Uid];
        NSString * sourcePath;
        if (self.brochureModel.iconCover.length>0) {
            sourcePath = self.brochureModel.iconCover;
        }else if (self.brochureModel.computerPicPath.length >0) {
            NSArray *computerPicPath = turnStringToDictionary(self.brochureModel.computerPicPath);
             sourcePath = computerPicPath[0][@"sourcePath"];
            if ([sourcePath hasPrefix:@"https://"] || [sourcePath hasPrefix:@"http://"] ) {
                sourcePath = sourcePath;
            }else{
                sourcePath = resultsUrl(sourcePath);
            }
            
        }else{
            sourcePath =imageNameWithSourceSuffix(fileModel.sourceSuffix);
        }
        [VULShareManager sharedInstance].brochureModel = self.brochureModel;
        [[VULShareManager sharedInstance] setShareVC:self title:titleStr content:descStr image: sourcePath url:codeStr busId:self.brochureModel.commonHomepageInfoId busType:@"16" isTopItem:YES];
    } else if(self.isCourseware){
        NSString *url =  self.currentURL;
        [[VULShareManager sharedInstance] setShareVC:self title:self.naviTitle  content:self.naviTitle image:nil url:url busId:self.busIdNew busType:self.busTypeNew isTopItem:YES];
    }else if (self.cePingModel) {

        NSString *url;
        NSRange loct = [kSchoolServiceUrl rangeOfString:@":"];
        NSString *baseUrl = [kSchoolServiceUrl substringFromIndex:loct.location+3 ];;
        
        if (self.cePingModel.type.integerValue == 1) {
            url = [NSString stringWithFormat:@"https://ceping.wxbig.cn/scale-h5server/openApi/gotoScaleV2?unionId=123&&channel=WJYBOY&path=%@%@%@&formappurl=%@",@"module%2FmoduleDetail%3FmoduleId%3D",self.cePingModel.relationId,@"%26channel%3D",baseUrl];
        }else if(self.cePingModel.type.integerValue == 2){
            url = [NSString stringWithFormat:@"https://ceping.wxbig.cn/scale-h5server/openApi/gotoScaleV2?unionId=123&&channel=WJYBOY&path=%@%@%@&formappurl=%@",@"plate%2flist%3fplateId%3d",self.cePingModel.relationId,@"%26type%3d1%26channel%3d",baseUrl];
            
        }else{

            url = [NSString stringWithFormat:@"https://ceping.wxbig.cn/scale-h5server/openApi/gotoScaleV2?unionId=123&&channel=WJYBOY&path=%@%@%@&formappurl=%@",@"V2.9%2fplate%2fplateDetail%3fplateId%3d",self.cePingModel.relationId,@"%26type%3d1%26channel%3d",baseUrl];
        }
        NSString *coverUrl =self.cePingModel.pictureUrl;
        if (!coverUrl) {
            VULResponseSchoolInfoModel *currentSchoolModel = [VULAccountManager sharedInstance].currentSchoolModel;
            coverUrl = [NSString stringWithFormat:@"%@%@", kCDNHostUrl, currentSchoolModel.logo];
        }else{
            coverUrl = [self.cePingModel.pictureUrl stringByReplacingOccurrencesOfString:@"http" withString:@"https"];
        }
        [[VULShareManager sharedInstance] setShareVC:self title:self.cePingModel.titleMain content:self.cePingModel.titleSub image:coverUrl url:url busId:self.cePingModel.relationId busType:@"110" isTopItem:YES];
    } else{
        UBSaveCardInfoModel *cardInfoModel = [VULRealmDBManager getCurrentCardInfo];
        NSString *titleStr =[NSString stringWithFormat:@"%@",cardInfoModel.cardName] ;
        NSString *descStr = cardInfoModel.introduce;
        NSString *coverStr = resultsUrl(cardInfoModel.avatar);
        [VULBaseRequest requestWithUrl:@"/api/vcard/showItemSetting" params:@{@"withShareImage":@(true)} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest * _Nonnull request) {

            if (request.success) {
                NSArray *listArr = request.responseObject[@"data"][@"showItemList"];
                NSMutableArray *arr = [NSMutableArray array];
                NSMutableArray *arr1 = [NSMutableArray array];
                NSInteger i =0;
                NSDictionary *shareImage = request.responseObject[@"data"][@"shareImage"];
                NSString *text= shareImage[@"text"];
                NSString *image = shareImage[@"image"];
                if (text.length==0) {
                    text = titleStr;
                }
                if (image.length==0) {
                    image = coverStr;
                }
                NSString *des = descStr;
                if (text.length>0 || image.length>0 ){
                    des = @"";
                }
                UIImage *image1;
                if (image.length>0) {
                    NSData*data=[NSData dataWithContentsOfURL:[NSURL URLWithString:resultsUrl(image)]];
                    image1 = [UIImage imageWithData:data];
                }
               
                
                [[VULShareManager sharedInstance] setShareVC:self title:text content:des image:compressionSmallImage(image1,CGSizeMake(100, 100)) url:[NSString stringWithFormat:@"%@/vscard/views.html?vcardId=%@", kSchoolServiceUrl, cardInfoModel.visitingCardIdStr] busId:cardInfoModel.visitingCardIdStr busType:@"7" isTopItem:YES];
            }else{
                [[VULShareManager sharedInstance] setShareVC:self title:titleStr content:descStr image:coverStr url:[NSString stringWithFormat:@"%@/vscard/views.html?vcardId=%@", kSchoolServiceUrl, cardInfoModel.visitingCardIdStr] busId:cardInfoModel.visitingCardIdStr busType:@"7" isTopItem:YES];
            }
            
        }];

       
    }
}

- (VULButton *)addButton {
    if (!_addButton) {
        _addButton = [VULButton createImgBtnWithFrame:CGRectMake(self.view.bounds.size.width - 70, self.view.bounds.size.height - VULSCREEN_HEIGHT / 6 - 55, 55, 55) imgNamed:@"s-分享" Target:self Sel:@selector(addButtonAction:)];
    }
    return _addButton;
}
- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    
    // 解决返回后语音一直播放的问题
    [self pausePlay];
}
 
/// 暂停播放网页内的音频、视频
- (void)pausePlay {
    NSString *videoPauseJSStr = @"document.documentElement.getElementsByTagName(\"video\")[0].pause()";
    [self.webView callJS:videoPauseJSStr];

}
-(void)finishBtnClicked{
    VULFillFormViewController *vc = [VULFillFormViewController new];
    vc.formId = self.formId;
    vc.isCopy = YES;
    [self.navigationController pushViewController:vc animated:YES];
}
-(VULButton *)finishBtn{
    if (!_finishBtn) {
        _finishBtn = [VULButton createImgBtnWithFrame:CGRectMake((VULSCREEN_WIDTH - 120)/2, VULSCREEN_HEIGHT - 60, 120, 40) imgNamed:@"" Target:self Sel:@selector(finishBtnClicked)];
        [_finishBtn setTitle:@"使用该模板" forState:UIControlStateNormal];
        [_finishBtn setTitleColor:HEXCOLOR(0xffffff) forState:UIControlStateNormal];
        _finishBtn.backgroundColor = HEXCOLOR(0x1976D2);
        _finishBtn.layer.masksToBounds= YES;
        _finishBtn.layer.cornerRadius  =20;

    }
    return _finishBtn;
}
- (void)dealloc {
    NSLog(@"moudleWebView dealloc 销毁");
   
    [self.webView removeScriptMessageHandlerForName:@"shareAction"];
    [self.webView removeScriptMessageHandlerForName:@"addInfoSuccess"];
    [self.webView removeScriptMessageHandlerForName:@"associatedSuccess"];
    [self.webView removeScriptMessageHandlerForName:@"InformationAssociatedTextForIOS"];
    [self.webView removeScriptMessageHandlerForName:@"addPhotoFormSuccessForIOS"];
    [self.webView removeScriptMessageHandlerForName:@"TaskForIOSAssociated"];
    [self.webView removeScriptMessageHandlerForName:@"AssociatedForIOSCancel"];
    [self.webView removeScriptMessageHandlerForName:@"appCallTeleForIOS"];
    [self.webView removeScriptMessageHandlerForName:@"appLoadFileForIOS"];
    [self.webView removeScriptMessageHandlerForName:@"getProtocolTemplatePathForIOS"];
    [self.webView removeScriptMessageHandlerForName:@"delAllEditFormForIOS"];
    [self.webView removeScriptMessageHandlerForName:@"getShopDetailPathForIOS"];
    
    

}

@end

@implementation WeakScriptMessageDelegate

- (instancetype)initWithDelegate:(id<WKScriptMessageHandler>)scriptDelegate
{
    self = [super init];
    if (self) {
        _scriptDelegate = scriptDelegate;
    }
    return self;
}

- (void)userContentController:(WKUserContentController *)userContentController didReceiveScriptMessage:(WKScriptMessage *)message
{
    [self.scriptDelegate userContentController:userContentController didReceiveScriptMessage:message];
}


- (BOOL)shouldAutorotate {
    return NO;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return UIInterfaceOrientationMaskLandscape;
}


- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation {
    return UIInterfaceOrientationLandscapeLeft;
}

@end
