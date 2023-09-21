//
//  UBBaseWebviewVC.m
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/1/4.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "UBBaseWebviewVC.h"
#import "VULRequestHeaderDictionary.h"
#import "WKProcessPool+SharedProcessPool.h"
#import "VULWebViewPool.h"

@interface UBBaseWebviewVC ()
@property (nonatomic, strong) VULRequestHeaderDictionary *header;/**< 请求头 */
@property (nonatomic, strong) CALayer *progressLayer;

@end

@implementation UBBaseWebviewVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationView.hidden = YES;
    _dynamicShowTitle = YES;
    
    WKWebViewConfiguration *webviewConfig;
    if (_wkConfig) {
        webviewConfig = _wkConfig;
    } else {
        webviewConfig = [WKWebViewConfiguration new];
        webviewConfig.preferences = [WKPreferences new];
        webviewConfig.preferences.javaScriptEnabled = YES;
    }
    webviewConfig.processPool = [WKProcessPool shareProcessPool];
    [webviewConfig.userContentController addScriptMessageHandler:self name:@"answerVoiceCodingAction"];
    [webviewConfig.userContentController addScriptMessageHandler:self name:@"deleteSuccessAction"];
    [webviewConfig.userContentController addScriptMessageHandler:self name:@"homeworkVoiceCodingAction"];

    VULWebViewPool *webViewPool = [VULWebViewPool sharedInstance];
    webViewPool.wkConfig = webviewConfig;
    [webViewPool prepareWithCount:20];
    self.webView = [webViewPool getWKWebViewFromPool];
    self.webView.UIDelegate = self;
    self.webView.navigationDelegate = self;
    //开启支持滑动返回
    self.webView.allowsBackForwardNavigationGestures = YES;

    if (_isRefresh) {
        [self.view addSubview:self.tableView];
        [self.tableView addSubview:self.webView];
        [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self.view);
        }];
        [self.webView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self.tableView);
        }];
        self.tableView.hidden = NO;

        __weak __typeof(self) weakSelf = self;
        self.tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
            __strong __typeof(weakSelf) strongSelf = weakSelf;
            [strongSelf loadWebViewWithURLString:strongSelf.currentURL];
        }];
    } else {
        self.tableView.hidden = YES;
        [self.view addSubview:self.webView];
        [self.webView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self.view);
        }];
    }

    CGFloat originY = self.hideNavi ? 0 : K_NavBar_Height;
    UIView *progress = [[UIView alloc] initWithFrame:CGRectMake(0, originY, CGRectGetWidth(self.view.frame), 2)];
    progress.backgroundColor = [UIColor clearColor];
    [self.view addSubview:progress];
    CALayer *layer = [CALayer layer];
    layer.frame = CGRectMake(0, 0, 0, 2);
    layer.backgroundColor = HEXCOLOR(0x73B6F5).CGColor;
    [progress.layer addSublayer:layer];
    self.progressLayer = layer;

    [self.webView addObserver:self forKeyPath:@"estimatedProgress" options:NSKeyValueObservingOptionNew context:nil];
    if (!_noChangeTitle) {
        [self.webView addObserver:self forKeyPath:@"title" options:NSKeyValueObservingOptionNew context:NULL];
    }
}

- (void)loadWebViewWithURLString:(NSString *)url {
    NSURL *URL = [NSURL URLWithString:url];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:URL];
    [request setCachePolicy:NSURLRequestUseProtocolCachePolicy];
//    NSMutableDictionary *properties = [NSMutableDictionary dictionary];
//    [properties setObject:@"token" forKey:NSHTTPCookieName];
//    [properties setObject:[VULRealmDBManager getLocalToken] forKey:NSHTTPCookieValue];
//    [properties setObject:[URL host] forKey:NSHTTPCookieDomain];
//    [properties setObject:[URL path] forKey:NSHTTPCookiePath];
//    [properties setValue:[NSDate dateWithTimeIntervalSinceNow:60 * 60] forKey:NSHTTPCookieExpires];
//
//    NSHTTPCookie *ebhcookie = [NSHTTPCookie cookieWithProperties:properties];
//    [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookie:ebhcookie];
//    [request addValue:[NSString readCurrentCookieWith:properties] forHTTPHeaderField:@"Cookie"];
//    [request setValue:[VULRealmDBManager getLocalToken] forHTTPHeaderField:@"token"];
    [self.webView loadRequest:request];
}

#pragma mark - KVO监听网页加载i进度
- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSKeyValueChangeKey, id> *)change context:(void *)context {
    if ([keyPath isEqualToString:@"estimatedProgress"]) {
        self.progressLayer.opacity = 1;
        if ([change[@"new"] floatValue] < [change[@"old"] floatValue]) {
            return;
        }
        self.progressLayer.frame = CGRectMake(0, 0, self.view.bounds.size.width * [change[@"new"] floatValue], 2);
        if ([change[@"new"] floatValue] == 1) {
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(.4 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                self.progressLayer.opacity = 0;
            });
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                self.progressLayer.frame = CGRectMake(0, 0, 0, 2);
            });
        }
    } else if ([keyPath isEqualToString:@"title"]) {
        if (object == self.webView) {
            if (_dynamicShowTitle) {
                [self setNavigationTitle:self.webView.title];
            }
        } else {
            [super observeValueForKeyPath:keyPath ofObject:object change:change context:context];
        }
    }
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

    decisionHandler(actionPolicy);
}

// 当内容开始返回时调用
- (void)webView:(WKWebView *)webView didCommitNavigation:(WKNavigation *)navigation {
}

// 页面加载完成之后调用
- (void)webView:(WKWebView *)webView didFinishNavigation:(WKNavigation *)navigation {
    NSLog(@"页面加载完成");
    [self.tableView.mj_header endRefreshing];
    [self endWebLoading];
    NSString *injectionJSString = @"var script = document.createElement('meta');"
        "script.name = 'viewport';" "script.content=\"width=device-width, initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\";" "document.getElementsByTagName('head')[0].appendChild(script);localStorage.setItem('appAdapt', 'true');";
    [webView evaluateJavaScript:injectionJSString completionHandler:nil];
}

// 页面加载失败时调用
- (void)webView:(WKWebView *)webView didFailProvisionalNavigation:(WKNavigation *)navigation {
    NSLog(@"加载出现错误");
    [self.tableView.mj_header endRefreshing];
    [self endWebLoading];
}

#pragma mark - getter and setter
- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_HEIGHT  - self.tabBarHeight - K_StatusBar_Height - 44)]; //ninaPageView头部高度44
        _tableView.backgroundColor = [UIColor whiteColor];
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    }
    return _tableView;
}

- (VULRequestHeaderDictionary *)header {
    if (!_header) {
        _header = [[VULRequestHeaderDictionary alloc] init];
    }
    return _header;
}

- (void)dealloc {
    [self.webView removeObserver:self forKeyPath:@"estimatedProgress"];
    if (!_noChangeTitle) {
        [self.webView removeObserver:self forKeyPath:@"title"];
    }
    self.webView.navigationDelegate = nil;
    self.webView.UIDelegate = nil;
}

@end
