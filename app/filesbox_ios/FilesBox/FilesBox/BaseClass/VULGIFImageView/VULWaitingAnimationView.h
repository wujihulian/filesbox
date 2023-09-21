//
//  VULWaitingAnimationView.h
//  VideoULimit
//
//  Created by svnlan on 2019/11/13.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VULGIFImageView.h"
#import "VULGIFImage.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULWaitingAnimationView : UIView

@property (nonatomic, strong) VULGIFImageView *loadWaitView;

-(void)endWaitingView;

@end

NS_ASSUME_NONNULL_END
