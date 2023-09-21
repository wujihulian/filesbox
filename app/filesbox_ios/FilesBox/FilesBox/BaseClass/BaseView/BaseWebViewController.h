//
//  BaseWebViewController.h
//  FilesBox
//
//  Created by 无极互联 on 2023/6/13.
//

#import "VULBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface BaseWebViewController : VULBaseViewController
@property (nonatomic, strong) WKWebView *webView;
@property (nonatomic, strong) NSString *currentURL;/**< 当前链接 */
@property (nonatomic, strong) NSString *nvaTitle;/**< 当前链接 */
@property (nonatomic, strong) VULInfoModel *model;/**< 当前链接 */

@end

NS_ASSUME_NONNULL_END
