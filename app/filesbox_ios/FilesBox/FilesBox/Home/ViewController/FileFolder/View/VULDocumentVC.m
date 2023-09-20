//
//  VULDocumentVC.m
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/9/18.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULDocumentVC.h"
#import "VULDocumentTabbarView.h"
#import "NSObject+CYLTabBarControllerExtention.h"

@interface VULDocumentVC ()<DocumentTabbarViewDelegate, WKUIDelegate, WKNavigationDelegate, WKScriptMessageHandler, NSURLSessionDelegate>
@property (nonatomic, strong) VULDocumentTabbarView *tabbarView;
@property (nonatomic, assign) NSInteger currentPage;
@property (nonatomic, assign) NSInteger totalPage;
@property (nonatomic, strong) WKWebView *myWebView;

@end

@implementation VULDocumentVC

- (void)viewDidLoad {
    [super viewDidLoad];

    
    if(self.isZip){
        [self baseAddNavRightBtnWithImage:VULGetImage(@"file_attribute_close") selector:@selector(gotoBack)];

    }
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    [self.view addSubview:self.tabbarView];
    self.navigationTitle = self.title;
    [self.tabbarView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.equalTo(@(-K_BottomBar_Height));
        make.left.right.equalTo(@(0));
        make.height.equalTo(@(50));
    }];
    self.tabbarView.expandBtn.hidden = YES;
    WeakSelf(self);
    WKWebViewConfiguration *webviewConfig = [[WKWebViewConfiguration alloc] init];
    webviewConfig.preferences = [WKPreferences new];
    webviewConfig.preferences.javaScriptEnabled = YES;
    webviewConfig.processPool = [WKProcessPool shareProcessPool];
//    [webviewConfig.userContentController addScriptMessageHandler:self name:@"sendTotalPage"];
    
    WKWebView *webView = [[WKWebView alloc] initWithFrame:CGRectZero configuration:webviewConfig];
    webView.UIDelegate = self;
    webView.navigationDelegate = self;
    [self.view addSubview:webView];
    [self.view bringSubviewToFront:self.tabbarView];
    [webView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(K_NavBar_Height);
        make.left.right.bottom.mas_equalTo(0);

    }];
    self.tabbarView.hidden = YES;

    NSString *urlStr = self.pptPreviewUrl;
    if (NSStringIsNotEmpty(urlStr)) {
        //NSArray *arr = [urlStr componentsSeparatedByString:@"busId"];
        NSString *headurl = [urlStr stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
        if(self.isZip){
            headurl = self.pptPreviewUrl;
        }
        //NSString *result = [NSString stringWithFormat:@"%@busId%@", headurl, arr[1]];
        //NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:urlStr]];
        NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:headurl]];//result]];
        [webView loadRequest:request];
        self.myWebView = webView;
    } else {
        UILabel *tipsLabel = [[UILabel alloc] initWithFrame:CGRectZero];
        tipsLabel.text = @"暂无内容";
        tipsLabel.textAlignment = NSTextAlignmentCenter;
        tipsLabel.textColor = HEXCOLOR(0x666666);
        [self.view addSubview:tipsLabel];
        [tipsLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerX.centerY.mas_equalTo(self.view);
        }];
    }
    // Do any additional setup after loading the view.
}
-(void)gotoBack{
    [self dismissViewControllerAnimated:YES completion:^{
            
    }];
}
- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration

{

    if (UIInterfaceOrientationIsLandscape(toInterfaceOrientation)) {// 横屏
        self.navigationView.hidden = YES;
    } else {//竖屏
        self.navigationView.hidden = NO;

    }

}
- (void)documentNavLeftClick {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)webView:(WKWebView *)webView didFinishNavigation:(WKNavigation *)navigation {
    WeakSelf(self);
    [VULQueue executeInMainQueue:^{
        NSString *jsStr = @"setControl(0)";
        [webView evaluateJavaScript:jsStr completionHandler:^(NSNumber *_Nullable totalCount, NSError *_Nullable error) {
            NSLog(@"didFinishNavigation   获取总页数：%@ error?= %@", totalCount, error);
            if (totalCount > 0) {
                self.tabbarView.hidden = NO;
                weakself.totalPage = [totalCount integerValue];
                if (!weakself.currentPage) {
                    weakself.currentPage = 1;
                    [weakself.tabbarView.leftBtn setImage:VULGetImage(@"上一页-禁用") forState:(UIControlStateNormal)];
                }
                if (weakself.totalPage == 1) {
                    [weakself.tabbarView.rightBtn setImage:VULGetImage(@"下一页-禁用") forState:(UIControlStateNormal)];
                }
                weakself.tabbarView.pageLabel.text = [NSString stringWithFormat:@"%ld / %ld", weakself.currentPage, weakself.totalPage];
            }else{
                self.tabbarView.hidden = YES;
            }
        }];
    } delay:1];
}
#pragma mark tabbarViewDelegate


- (void)documentRotateClick:(UIButton *)rotateBtn {
    rotateBtn.selected = !rotateBtn.selected;
    if (rotateBtn.selected) {
        [[self cyl_sharedAppDelegate] cyl_forceUpdateInterfaceOrientation:UIInterfaceOrientationLandscapeRight];
    } else {
        [[self cyl_sharedAppDelegate] cyl_forceUpdateInterfaceOrientation:UIInterfaceOrientationPortrait];
    }
    
    [self.myWebView reload];
    [VULQueue executeInMainQueue:^{
        NSString *jsStr = [NSString stringWithFormat:@"setControl(5,%ld)", self.currentPage];
        [self.myWebView evaluateJavaScript:jsStr completionHandler:^(NSString *_Nullable result, NSError *_Nullable error) {
            NSLog(@"%@  error?= %@", result, error);
        }];
    } delay:1.2];
}

- (void)documentLastPageClick:(UIButton *)lastBtn {
    if (self.currentPage <= 1) {
        return;
    }
    WeakSelf(self);
    NSString *jsStr = @"setControl(1)";
    [self.myWebView evaluateJavaScript:jsStr completionHandler:^(NSString *_Nullable result, NSError *_Nullable error) {
        NSLog(@"%@", result);
        weakself.currentPage--;
        if (weakself.currentPage == 1) {
            [lastBtn setImage:VULGetImage(@"上一页-禁用") forState:(UIControlStateNormal)];
        }
        if (weakself.currentPage != weakself.totalPage) {
            [weakself.tabbarView.rightBtn setImage:VULGetImage(@"下一页") forState:(UIControlStateNormal)];
        }
        weakself.tabbarView.pageLabel.text = [NSString stringWithFormat:@"%ld / %ld", weakself.currentPage, weakself.totalPage];
    }];
}

- (void)documentNextPageClick:(UIButton *)nextBtn {
    if (self.currentPage == self.totalPage) {
        return;
    }
    WeakSelf(self);
    NSString *jsStr = @"setControl(2)";
    [self.myWebView evaluateJavaScript:jsStr completionHandler:^(NSString *_Nullable result, NSError *_Nullable error) {
        NSLog(@"%@", result);
        weakself.currentPage++;
        if (weakself.currentPage == weakself.totalPage) {
            [nextBtn setImage:VULGetImage(@"下一页-禁用") forState:(UIControlStateNormal)];
        }
        if (weakself.currentPage != 1) {
            [weakself.tabbarView.leftBtn setImage:VULGetImage(@"上一页") forState:(UIControlStateNormal)];
        }
        weakself.tabbarView.pageLabel.text = [NSString stringWithFormat:@"%ld / %ld", weakself.currentPage, weakself.totalPage];
    }];
}

- (NSString *)readCurrentCookieWith:(NSDictionary *)dic {
    if (dic == nil) {
        return nil;
    } else {
        NSHTTPCookieStorage *cookieJar = [NSHTTPCookieStorage sharedHTTPCookieStorage];
        NSMutableString *cookieString = [[NSMutableString alloc] init];
        for (NSHTTPCookie *cookie in [cookieJar cookies]) {
            [cookieString appendFormat:@"%@=%@;", cookie.name, cookie.value];
        }
        //删除最后一个“；”
        [cookieString deleteCharactersInRange:NSMakeRange(cookieString.length - 1, 1)];
        return cookieString;
    }
}

- (VULDocumentTabbarView *)tabbarView {
    if (!_tabbarView) {
        _tabbarView = [[VULDocumentTabbarView alloc] initWithFrame:CGRectMake(0, VULSCREEN_HEIGHT - K_BottomBar_Height - 50, VULSCREEN_WIDTH, 50)];
        _tabbarView.delegate = self;
    }
    return _tabbarView;
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
