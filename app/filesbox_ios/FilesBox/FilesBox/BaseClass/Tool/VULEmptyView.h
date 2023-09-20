//
//  VULEmptyView.h
//  Tairong-Test
//
//  Created by yuekewei on 2019/8/7.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULEmptyView : UIView

@property (strong, nonatomic) UIImageView *imageView;
@property (strong, nonatomic) UILabel *infoLabel;
@property (strong, nonatomic) UIButton *reloadButton;
@end


@interface UIView (Empty)

@property (nonatomic, strong, nullable) VULEmptyView *emptyView;

- (void)configEmptyViewClass:(Class)emptyViewClass;

- (void)configEmptyViewWithHasData:(BOOL)hasData
                          hasError:(BOOL)hasError
                            insets:(UIEdgeInsets)insets
                             image:(nullable UIImage *)image
                              info:(nullable NSString *)info
                       reloadBlock:(nullable void (^)(void))reloadBlock;
@end

NS_ASSUME_NONNULL_END
