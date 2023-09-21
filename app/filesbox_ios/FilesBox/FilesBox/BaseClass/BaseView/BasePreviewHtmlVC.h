//
//  BasePreviewHtmlVC.h
//  FilesBox
//
//  Created by 无极互联 on 2023/9/7.
//

#import "VULBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface BasePreviewHtmlVC : VULBaseViewController
@property (nonatomic, strong) WKWebView *webView;
@property (nonatomic, strong) NSString *currentURL;/**< 当前链接 */
@property (nonatomic, strong) NSString *nvaTitle;/**< 当前链接 **/
@end

NS_ASSUME_NONNULL_END
