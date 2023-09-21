//
//  FBPreviewURLVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/4/17.
//

#import "FBPreviewURLVC.h"

@interface FBPreviewURLVC ()<WKUIDelegate, WKNavigationDelegate, WKScriptMessageHandler, NSURLSessionDelegate>
@property (nonatomic, strong) WKWebView *myWebView;

@end

@implementation FBPreviewURLVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    self.navigationTitle = self.title;

    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    WKWebViewConfiguration *webviewConfig = [[WKWebViewConfiguration alloc] init];
    webviewConfig.preferences = [WKPreferences new];
    webviewConfig.preferences.javaScriptEnabled = YES;
    webviewConfig.processPool = [WKProcessPool shareProcessPool];
    WKWebView *webView = [[WKWebView alloc] initWithFrame:CGRectZero configuration:webviewConfig];
    webView.UIDelegate = self;
    webView.navigationDelegate = self;
    [self.view addSubview:webView];
    [webView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(K_NavBar_Height);
        make.left.right.bottom.mas_equalTo(0);

    }];
    self.myWebView = webView;
    NSURL *path = [[NSBundle mainBundle] URLForResource:@"mardDown/html/previewHtml.html" withExtension:nil];
//    　　[self.myWebView ];



  
    NSURLRequest *request = [NSURLRequest requestWithURL:path];

    [self.myWebView loadRequest:request];

    // Do any additional setup after loading the view.
}
// 当内容开始返回时调用
- (void)webView:(WKWebView *)webView didCommitNavigation:(WKNavigation *)navigation {
}

// 页面加载完成之后调用
- (void)webView:(WKWebView *)webView didFinishNavigation:(WKNavigation *)navigation {
    NSLog(@"页面加载完成");
    [self endWebLoading];

    [self performSelector:@selector(ocCallJSFunction) afterDelay:1.0];
}
-(void)ocCallJSFunction{
    
    NSString *pathStr = [NSString stringWithFormat:@"ocCallJSFunction(\"%@\")",self.text];
    

    [self.myWebView evaluateJavaScript:pathStr completionHandler:^(id _Nullable obj, NSError * _Nullable error) {
    　　 NSLog(@"evaluateJavaScript, obj = %@, error = %@", obj, error);

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
