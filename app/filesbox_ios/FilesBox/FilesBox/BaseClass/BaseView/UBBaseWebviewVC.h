//
//  UBBaseWebviewVC.h
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/1/4.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface UBBaseWebviewVC : VULBaseViewController<WKUIDelegate,WKNavigationDelegate>

@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic, strong) WKWebView * webView;
@property (nonatomic, strong) NSString *currentURL;/**< 当前链接 */
@property (nonatomic, assign) BOOL noChangeTitle; /**< 不更新标题 */
@property (nonatomic, assign) BOOL isRefresh; /**< 是否需要下拉刷新 */
@property (nonatomic, assign) BOOL dynamicShowTitle; /**< 是否动态加载title */
@property (nonatomic, assign) BOOL hideNavi; /**< 是否隐藏导航栏 展示加载进度条 */
@property (nonatomic, strong) WKWebViewConfiguration *wkConfig;

- (void)loadWebViewWithURLString:(NSString *)url;

@end

NS_ASSUME_NONNULL_END
