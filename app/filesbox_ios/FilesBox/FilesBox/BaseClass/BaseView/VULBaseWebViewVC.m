//
//  VULBaseWebViewVC.m
//  VideoULimit
//
//  Created by svnlan on 2018/10/22.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULBaseWebViewVC.h"
#import "VULRequestHeaderDictionary.h"
#import "WKProcessPool+SharedProcessPool.h"
#import "VULWebViewPool.h"

@interface VULBaseWebViewVC ()<WKScriptMessageHandler>
@property (nonatomic, strong) VULRequestHeaderDictionary *header;/**< 请求头 */
@property (nonatomic, strong) CALayer *progressLayer;

@end

@implementation VULBaseWebViewVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationView.hidden = NO;
    _dynamicShowTitle = YES;
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    self.webView = [PAWebView shareInstance];
    self.webView.webView.UIDelegate = self;
    self.webView.webView.navigationDelegate = self;
    self.webView.view.hidden = YES;

    [self.view addSubview:self.webView.view];
    [self.webView.view mas_updateConstraints:^(MASConstraintMaker *make) {
        make.top.offset(K_NavBar_Height);
        make.left.right.offset(0);
        make.bottom.offset(0);
    }];
    if (self.nvaTitle) {
        self.navigationTitle = self.nvaTitle;
    }

    [self loadWebViewWithURLString:self.currentURL];
}

- (void)loadWebViewWithURLString:(NSString *)url {
    NSURL *URL = [NSURL URLWithString:url];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:URL];
    [request setCachePolicy:NSURLRequestReloadIgnoringLocalCacheData];
    [self.webView loadRequestURL:request];
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
            if(![DownloadProgress sharedInstance].isWeb){
                [DownloadProgress sharedInstance].isWeb = YES;
                [self loadWebViewWithURLString:self.currentURL];
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
    [webView evaluateJavaScript:@"document.title" completionHandler:^(id object, NSError * error) {
        if (!self.nvaTitle) {
            if ([object isAvailable]) {
                self.navigationTitle = object;
            }
        }
    }];
    self.webView.view.hidden = NO;
    [self.tableView.mj_header endRefreshing];
    [self endWebLoading];
//    NSString *injectionJSString = @"var script = document.createElement('meta');"
//        "script.name = 'viewport';" "script.content=\"width=device-width, initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\";" "document.getElementsByTagName('head')[0].appendChild(script);localStorage.setItem('appAdapt', 'true');var comHead = document.getElementsByClassName('head comHead')[0]; comHead.style.display = 'none';";
//    [webView evaluateJavaScript:injectionJSString completionHandler:nil];
}

// 页面加载失败时调用
- (void)webView:(WKWebView *)webView didFailProvisionalNavigation:(WKNavigation *)navigation {
    NSLog(@"加载出现错误");
    [self.tableView.mj_header endRefreshing];
    [self endWebLoading];
}

- (void)userContentController:(WKUserContentController *)userContentController didReceiveScriptMessage:(WKScriptMessage *)message {
    
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
    NSLog(@"VULBaseWebViewVC dealloc");
    [self.webView clearWebCacheFinish:^(BOOL finish, NSError *error) {
        NSLog(@"%@  finish?= %d", error, finish );
    }];
    [self.webView clearBackForwardList];

}

@end
