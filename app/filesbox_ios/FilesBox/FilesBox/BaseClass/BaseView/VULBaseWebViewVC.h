//
//  VULBaseWebViewVC.h
//  VideoULimit
//
//  Created by svnlan on 2018/10/22.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VULBaseViewController.h"
#import <WebKit/WebKit.h>
#import "PAWebView.h"

@interface VULBaseWebViewVC : VULBaseViewController<WKUIDelegate,WKNavigationDelegate>

@property (nonatomic, strong) UITableView *tableView;
//@property (nonatomic, strong) WKWebView * webView;
@property (nonatomic, strong) PAWebView *webView;
@property (nonatomic, strong) NSString *currentURL;/**< 当前链接 */
@property (nonatomic, assign) BOOL noChangeTitle; /**< 不更新标题 */
@property (nonatomic, assign) BOOL isRefresh; /**< 是否需要下拉刷新 */
@property (nonatomic, assign) BOOL dynamicShowTitle; /**< 是否动态加载title */
@property (nonatomic, assign) BOOL hideNavi; /**< 是否隐藏导航栏 展示加载进度条 */
@property (nonatomic, strong) WKWebViewConfiguration *wkConfig;
@property (nonatomic, strong) NSString *nvaTitle;/**< 当前链接 */

- (void)loadWebViewWithURLString:(NSString *)url;

@end
