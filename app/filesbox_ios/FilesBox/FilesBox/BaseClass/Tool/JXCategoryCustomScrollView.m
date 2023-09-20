//
//  JXCategoryCustomScrollView.m
//  UnlimitedBusiness
//
//  Created by yuekewei on 2021/4/1.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "JXCategoryCustomScrollView.h"
#import "VULNavigationViewController.h"

@implementation JXCategoryCustomScrollView

- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer {
    if ([gestureRecognizer isKindOfClass:[UIPanGestureRecognizer class]]) {
        CGPoint location = [gestureRecognizer locationInView:[UIApplication sharedApplication].delegate.window];
        if (location.x < 40) {
            return NO;
        }
    }    
    return YES;
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {
    if ([otherGestureRecognizer isKindOfClass:[FullscreenPopGestureRecognizer class]]) {
        CGPoint location = [otherGestureRecognizer locationInView:[UIApplication sharedApplication].delegate.window];
        if (location.x < 40 || self.contentOffset.x <= 0) {
            return YES;
        }

    }
    return NO;
}

@end
