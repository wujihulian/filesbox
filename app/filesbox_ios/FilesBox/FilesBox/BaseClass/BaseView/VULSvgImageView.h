//
//  VULSvgImageView.h
//  UnlimitedBusiness
//
//  Created by yuekewei on 2021/3/30.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YYAnimatedImageView.h"
#import "SVGKSourceURL.h"
#import "SVGKFastImageView.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULSvgImageView : YYAnimatedImageView

@property (nonatomic, strong, nullable) SVGKFastImageView *svgImgView;

- (void)svg_setImageWithURL:(nullable NSURL *)url;

- (void)svg_setImageWithURL:(nullable NSURL *)url
           placeholderImage:(nullable UIImage *)placeholder;

- (void)svg_setSpecailImageWithURL:(nullable NSURL *)url;

- (void)svg_setSpecailImageWithURL:(nullable NSURL *)url
           placeholderImage:(nullable UIImage *)placeholder;

@end

NS_ASSUME_NONNULL_END
