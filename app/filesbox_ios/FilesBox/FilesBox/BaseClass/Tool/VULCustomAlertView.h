//
//  VULCustomAlertView.h
//  VideoULimit
//
//  Created by svnlan on 2019/12/26.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class VULCustomAlertView, VULAlertConfig, VULAlertTextConfig, VULAlertButtonConfig;

typedef void (^VULAlertViewAddCustomViewBlock)(VULCustomAlertView *alertView, CGRect contentViewRect, CGRect titleLabelRect, CGRect contentLabelRect);

@interface VULCustomAlertView : UIView

@property (nonatomic,  strong,  readonly) UIView *contentView;
@property (nonatomic,  strong,  readonly) UILabel *titleLabel;
@property (nonatomic,  strong,  readonly) UILabel *contentLabel;
@property (nonatomic,  strong,  readonly) NSArray *buttonArray;
@property (nonatomic,  strong,  readonly) UIView *titleBottomLine;

@property (nonatomic,    copy) dispatch_block_t tapOutDismissBlock;

+ (void)jh_show_title:(NSString *)title
              message:(NSString *)message
               inView:(UIView *)view;

+ (void)jh_show_title:(NSString *)title
              message:(NSString *)message
               inView:(UIView *)view
          buttonTitle:(NSString *)buttonTitle
             andBlock:(dispatch_block_t)block;

+ (void)jh_show_title:(NSString *)title
              message:(NSString *)message
               inView:(UIView *)view
          buttonTitle:(NSString *)buttonTitle
             andBlock:(dispatch_block_t)block
         buttonTitle2:(NSString *)buttonTitle2
            andBlock2:(dispatch_block_t)block2;

- (instancetype)initWithConfig:(VULAlertConfig *)config;

- (void)addCustomView:(VULAlertViewAddCustomViewBlock)block;

- (void)showInView:(UIView *)view;

- (void)dismiss;

@end

@interface VULAlertConfig : NSObject

/// title
@property (nonatomic,  strong,  readonly) VULAlertTextConfig *title;
/// content
@property (nonatomic,  strong,  readonly) VULAlertTextConfig *content;
/// the line between title and content, default is NO.
@property (assign,  nonatomic) BOOL titleBottomLineHidden;
/// buttons
@property (strong,  nonatomic) NSArray<VULAlertButtonConfig *> *buttons;
/// the alpha of black mask view, default is 0.5
@property (assign,  nonatomic) CGFloat blackViewAlpha;
/// show animation, default is YES
@property (nonatomic,  assign) BOOL showAnimation;
/// show animation duration, default is 0.25s
@property (nonatomic,  assign) CGFloat showAnimationDuration;
/// Default is: [UIScreen mainScreen].bounds.size.width - 100
@property (nonatomic,  assign) CGFloat contentViewWidth;
/// Height for a fully custom view
@property (nonatomic,  assign) CGFloat contentViewHeight;
/// Default is 10
@property (nonatomic,  assign) CGFloat contentViewCornerRadius;
/// Default is YES
@property (nonatomic,  assign) BOOL dismissWhenTapOut;
/// Button height
@property (nonatomic,  assign) CGFloat buttonHeight;

@end

@interface VULAlertTextConfig : NSObject

/// text
@property (copy,    nonatomic) NSString *text;
/// text color
@property (strong,  nonatomic) UIColor *color;
/// text font, default is 18
@property (strong,  nonatomic) UIFont *font;
/// top padding
@property (nonatomic,  assign) CGFloat topPadding;
/// left padding
@property (nonatomic,  assign) CGFloat leftPadding;
/// bottom padding
@property (nonatomic,  assign) CGFloat bottomPadding;
/// right padding
@property (nonatomic,  assign) CGFloat rightPadding;
/// auto height
@property (nonatomic,  assign) BOOL autoHeight;
/// line space
@property (nonatomic,  assign) CGFloat lineSpace;

@end

@interface VULAlertButtonConfig : NSObject

/// title
@property (copy,    nonatomic) NSString *title;
/// title color
@property (strong,  nonatomic) UIColor *color;
/// title font, default is 18
@property (strong,  nonatomic) UIFont *font;
/// image
@property (strong,  nonatomic) UIImage *image;
/// the space between image and title
@property (nonatomic,  assign) CGFloat imageTitleSpace;
/// block
@property (copy,    nonatomic) dispatch_block_t block;

+ (VULAlertButtonConfig *)configWithTitle:(NSString *)title color:(nullable UIColor *)color font:(nullable UIFont *)font image:(nullable UIImage *)image handle:(dispatch_block_t)block;

@end

NS_ASSUME_NONNULL_END
