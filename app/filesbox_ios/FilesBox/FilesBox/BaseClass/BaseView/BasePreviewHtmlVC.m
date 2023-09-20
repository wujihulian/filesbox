//
//  BasePreviewHtmlVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/9/7.
//

#import "BasePreviewHtmlVC.h"

@interface BasePreviewHtmlVC ()<WKNavigationDelegate>
@property (nonatomic, strong) WKWebViewConfiguration *webConfig;

@end

@implementation BasePreviewHtmlVC

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
      
      // 将WebView添加到视图中
      [self.view addSubview:self.webView];
      
      
      // 将HTML字符串加载到WebView中
      [self.webView loadHTMLString:self.currentURL baseURL:nil];
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
