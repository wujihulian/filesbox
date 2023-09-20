//
//  FBNewHtmlView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/8/30.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBNewHtmlView : UIView
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) UITextView *textView;
@property (nonatomic, strong) VULButton *btnHtml;
@property (nonatomic, strong) VULButton *btnText;
@property (nonatomic, copy) void (^selectBlock)(NSString *text,bool flag);

@end

NS_ASSUME_NONNULL_END
