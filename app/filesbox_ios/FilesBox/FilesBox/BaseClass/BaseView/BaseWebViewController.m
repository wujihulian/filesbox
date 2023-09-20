//
//  BaseWebViewController.m
//  FilesBox
//
//  Created by 无极互联 on 2023/6/13.
//

#import "BaseWebViewController.h"
#import "VULShareSpecialCardView.h"
#import "VULCreateLinkCode.h"
@interface BaseWebViewController ()<WKNavigationDelegate,WKScriptMessageHandler>
@property (nonatomic, strong) WKWebViewConfiguration *webConfig;
@property (nonatomic, strong) VULButton *addButton;


@end

@implementation BaseWebViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationTitle = self.nvaTitle;
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    self.webConfig = [[WKWebViewConfiguration alloc] init];

    self.webView = [[WKWebView alloc] initWithFrame:CGRectMake(0, K_NavBar_Height, VULSCREEN_WIDTH, VULSCREEN_HEIGHT-K_NavBar_Height) configuration:self.webConfig];
    self.webView.navigationDelegate = self;
    [self.view addSubview:self.webView];
    NSURL *URL = [NSURL URLWithString:self.currentURL];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:URL];
    [request setCachePolicy:NSURLRequestReloadIgnoringLocalCacheData];
    [self.webView loadRequest:request];
 

    // 提供方法给js调用
    [self.webConfig.userContentController addScriptMessageHandler:self name:@"informationShareForIOS"];

    if(self.model){
        NSDictionary *roleDic = [[NSUserDefaults standardUserDefaults] objectForKey:@"role"];
        NSString *roleStr = [NSString stringWithFormat:@"%@",roleDic[@"explorer.informationView"]];
        NSString *roleStr1 = [NSString stringWithFormat:@"%@",roleDic[@"explorer.share"]];
        if(roleStr.boolValue && roleStr1.boolValue){
            [self.view addSubview:self.addButton];
//            [self baseAddNavRightBtnWithImage:VULGetImage(@"icon_info_share") selector:@selector(clickShareModel)];
        }
    }
    // Do any additional setup after loading the view.
}
-(void)addButtonAction:(UIButton *)sender{
    VULShareSpecialCardView *add = [[VULShareSpecialCardView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, fontAuto(300))];
     zhPopupController *popup = [[zhPopupController alloc] initWithView:add size:CGSizeMake(VULSCREEN_WIDTH, add.height)];
    add.isNews = YES;
     popup.layoutType = zhPopupLayoutTypeBottom;
     popup.presentationStyle = zhPopupSlideStyleFromBottom;
     popup.maskAlpha = 0.35;
     [popup showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
 
  add.menuViewBtnClickedBlock = ^(NSInteger index) {
      [popup dismiss];
      NSString *string;
      if(self.model.infoUrl.length>0){
          string = self.model.infoUrl;
      }else{
          string = [NSString stringWithFormat:@"%@pubinfo/%@.shtml",ChooseUrl,self.model.infoID];
      }
      if (index ==0) {
          [[WXApiManger shareInstance] shareInfoWithModel:self.model scene:WXSceneSession];
          return;

      }
      if (index ==1) {
          [[WXApiManger shareInstance] shareInfoWithModel:self.model scene:WXSceneTimeline];
          return;

      }
      if (index ==2) {
          [[WXApiManger shareInstance] shareDingdingWithModel:self.model];
          return;

      }
      if (index ==3) {
          [[WXApiManger shareInstance] shareWeiboWithModel:self.model];
          return;

      }
      if (index ==4) {
          VULCreateLinkCode *linkCode = [[VULCreateLinkCode alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH * 0.9, VULSCREEN_WIDTH * 0.9)];
          zhPopupController *popup = [[zhPopupController alloc] initWithView:linkCode size:CGSizeMake(VULSCREEN_WIDTH * 0.9, linkCode.height)];
          popup.layoutType = zhPopupLayoutTypeCenter;
          popup.presentationStyle = zhPopupSlideStyleFromBottom;
          popup.maskAlpha = 0.35;
          linkCode.url =string;
          linkCode.dismiss = ^{
              [popup dismiss];
          };
          [popup showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
  

          return;

      }
      if (index ==5) {
          [[UIPasteboard generalPasteboard] setString:string];
          [kWindow makeToast:KLanguage(@"复制成功")];

          return;
      }
      if (index ==6) {
          //
          
          NSDictionary *roleDic = [[NSUserDefaults standardUserDefaults] objectForKey:@"role"];
          NSString *roleStr = [NSString stringWithFormat:@"%@",roleDic[@"explorer.informationView"]];
          if(!roleStr.boolValue){
              [kWindow makeToast:KLanguage(@"暂无权限")];
              return;
          }
          VULInfoModel *model = self.model;
          BaseWebViewController *webVc = [[BaseWebViewController alloc] init];
          webVc.model = model;
          webVc.nvaTitle = model.title;
          if(model.infoUrl.length>0){
              webVc.currentURL = model.infoUrl;
          }else{
              webVc.currentURL = [NSString stringWithFormat:@"%@/pubinfo/%@.shtml?isApp=1",ChooseUrl,model.infoID];
          }
          [self.navigationController pushViewController:webVc animated:YES];

          return;

      }
      if (index ==7) {
          NSString *conl =self.model.title ;
          NSString *string ;
          if(self.model.infoUrl.length>0){
              string = self.model.infoUrl;
          }else{
              string = [NSString stringWithFormat:@"%@/pubinfo/%@.shtml",ChooseUrl,self.model.infoID];
          }
          NSURL *shareURL = [NSURL URLWithString:string];
          UIActivityViewController *activity = [[UIActivityViewController alloc] initWithActivityItems:@[shareURL,conl] applicationActivities:nil];
          UIPopoverPresentationController *popover = activity.popoverPresentationController;
          if (popover) {
//              popover.sourceView = sheetItem;
//              popover.sourceRect = CGRectMake(VULSCREEN_WIDTH_VARIABLE / 2, VULSCREEN_HEIGHT_VARIABLE - sheetItem.frame.size.height - 70, 1, 1);
//              popover.permittedArrowDirections = UIPopoverArrowDirectionAny;
          }
          [self presentViewController:activity animated:YES completion:NULL];

          return;
      }
     };
    
    // 发送到聊天界面  WXSceneSession
    // 发送到朋友圈    WXSceneTimeline
    // 发送到微信收藏  WXSceneFavorite
}

#pragma mark - WKNavigationDelegate
// 页面开始加载时调用
- (void)webView:(WKWebView *)webView didStartProvisionalNavigation:(WKNavigation *)navigation {
    NSLog(@"页面开始加载");
    [self showWebLoadingWait];
    NSString *URL = webView.URL.absoluteString;
    NSLog(@"截取到URL：%@", URL);
    NSString *dominStr;
    if (NSStringIsNotEmpty(self.currentURL)) {
        NSArray *arr = [self.currentURL componentsSeparatedByString:@"?"];
        dominStr = arr.firstObject;
    }
}

- (void)webView:(WKWebView *)webView decidePolicyForNavigationAction:(WKNavigationAction *)navigationAction decisionHandler:(void (^)(WKNavigationActionPolicy))decisionHandler {
    WKNavigationActionPolicy actionPolicy = WKNavigationActionPolicyAllow;
    NSURL *URL = navigationAction.request.URL;
    NSLog(@"URL:->>>>>>>>: %@", URL.absoluteString);

    if (NSStringIsNotEmpty([VULRealmDBManager getLocalToken])) {
        [webView evaluateJavaScript:[NSString stringWithFormat:@"document.cookie ='token=%@';", [VULRealmDBManager getLocalToken]] completionHandler:^(id result, NSError *error) {
            if (error) {
                NSLog(@"error %@", error);
            } else {
                NSLog(@"token注入成功");
            }

        }];
    }
    if (navigationAction.targetFrame == nil) {
        [webView loadRequest:navigationAction.request];
    }
    

    decisionHandler(WKNavigationActionPolicyAllow);
    
}

// 当内容开始返回时调用
- (void)webView:(WKWebView *)webView didCommitNavigation:(WKNavigation *)navigation {
}

// 页面加载完成之后调用
- (void)webView:(WKWebView *)webView didFinishNavigation:(WKNavigation *)navigation {
    NSLog(@"页面加载完成");
    [webView evaluateJavaScript:@"document.title" completionHandler:^(id object, NSError * error) {
        if (!self.nvaTitle) {
            if ([object isAvailable]) {
                self.navigationTitle = object;
            }
        }
    }];
    self.webView.hidden = NO;
    [self endWebLoading];
//    NSString *injectionJSString = @"var script = document.createElement('meta');"
//        "script.name = 'viewport';" "script.content=\"width=device-width, initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\";" "document.getElementsByTagName('head')[0].appendChild(script);localStorage.setItem('appAdapt', 'true');var comHead = document.getElementsByClassName('head comHead')[0]; comHead.style.display = 'none';";
//    [webView evaluateJavaScript:injectionJSString completionHandler:nil];
}

// 页面加载失败时调用
- (void)webView:(WKWebView *)webView didFailProvisionalNavigation:(WKNavigation *)navigation {
    NSLog(@"加载出现错误");
    [self endWebLoading];
}

- (void)userContentController:(WKUserContentController *)userContentController didReceiveScriptMessage:(WKScriptMessage *)message {
    NSLog(@"js调用的方法:%@",message.name);
    NSLog(@"js传过来的数据:%@",message.body);
    if([message.name isEqualToString:@"informationShareForIOS"]){
        [self addButtonAction:nil];
    }
}
- (VULButton *)addButton {
    if (!_addButton) {
        _addButton = [VULButton new];
        _addButton.frame = CGRectMake(self.view.bounds.size.width - 70, self.view.bounds.size.height - VULSCREEN_HEIGHT / 5 - 30, 55, 55);
        [_addButton addTarget:self action:@selector(addButtonAction:) forControlEvents:UIControlEventTouchUpInside];
        [_addButton setImage:VULGetImage(@"icon_info_share1") forState:UIControlStateNormal];
        UIPanGestureRecognizer *panRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(buttonMoving:)];
                panRecognizer.cancelsTouchesInView = YES;
        [_addButton addGestureRecognizer:panRecognizer];

    }
    return _addButton;
}
- (void)buttonMoving:(UIPanGestureRecognizer *)recognizer {
    UIButton *button = (UIButton *)recognizer.view;
    CGPoint translation = [recognizer translationInView:button];
    NSLog(@"x====%.2f",button.center.x + translation.x);
    NSLog(@"y====%.2f", button.center.y + translation.y);
    button.center = CGPointMake(button.center.x + translation.x, button.center.y + translation.y);

    if (button.center.x + translation.x <35  ){
        button.center = CGPointMake(35, button.center.y + translation.y);
    }
    if (button.center.x + translation.x >VULSCREEN_WIDTH-35  ){
        button.center = CGPointMake(VULSCREEN_WIDTH-35 , button.center.y + translation.y);
    }
    if (button.center.y + translation.y <K_NavBar_Height  ){
        button.center = CGPointMake(button.center.x + translation.x, K_NavBar_Height);
    }
    if (button.center.y + translation.y >VULSCREEN_HEIGHT-35  ){
        button.center = CGPointMake(button.center.x + translation.x, VULSCREEN_HEIGHT-35);
    }
    [recognizer setTranslation:CGPointZero inView:button];

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
