//
//  FBTextShowView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/6/28.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBTextShowView : UIView
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) UITextView *textView;
@property (nonatomic, strong) VULButton *btnCopy;

@end

NS_ASSUME_NONNULL_END
