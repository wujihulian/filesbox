//
//  NSObject+VULInterfaceOrientation.m
//  VideoULimit
//
//  Created by ZCc on 2019/5/29.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import "NSObject+VULInterfaceOrientation.h"
#import <objc/runtime.h>

@implementation NSObject (VULInterfaceOrientation)

- (BOOL)isVideoVC {
    NSNumber *isVideoObj = objc_getAssociatedObject(self, @selector(isVideoVC));
    return [isVideoObj boolValue];
}

- (void)setIsVideoVC:(BOOL)isVideoVC {
    NSNumber *isVideoObj = @(isVideoVC);
    objc_setAssociatedObject(self, @selector(isVideoVC), isVideoObj, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (UIResponder<UIApplicationDelegate> *)vul_shareAppDelegate {
    id<UIApplicationDelegate> appDelegate = [UIApplication sharedApplication].delegate;
    return (UIResponder<UIApplicationDelegate> *)appDelegate;
}

- (void)vul_forceUpdateInterfaceOrientation:(UIInterfaceOrientationMask)orientation {
    UIResponder<UIApplicationDelegate> *appDelegate = [self vul_shareAppDelegate];
    // 允许除了倒放以外所有方向
    BOOL isMaskAllButUpsideDown = (UIInterfaceOrientationMaskAllButUpsideDown == orientation);
    appDelegate.isVideoVC = isMaskAllButUpsideDown;
    if ([appDelegate respondsToSelector:@selector(application:supportedInterfaceOrientationsForWindow:)]) {
        [appDelegate application:[UIApplication sharedApplication] supportedInterfaceOrientationsForWindow:UIApplication.sharedApplication.keyWindow];
    }
}


- (void)vul_convertNotification:(NSNotification *)notification completion:(void (^ _Nullable)(CGFloat, UIViewAnimationOptions, CGFloat))completion
{
    // 按钮
    NSDictionary *userInfo = notification.userInfo;
    // 最终尺寸
    CGRect endFrame = [userInfo[UIKeyboardFrameEndUserInfoKey] CGRectValue];
    // 开始尺寸
    CGRect beginFrame = [userInfo[UIKeyboardFrameBeginUserInfoKey] CGRectValue];
    // 动画时间
    CGFloat duration = [userInfo[UIKeyboardAnimationDurationUserInfoKey] doubleValue];
    /// options
    UIViewAnimationOptions options = ([userInfo[UIKeyboardAnimationCurveUserInfoKey] integerValue] << 16 ) | UIViewAnimationOptionBeginFromCurrentState;
   
    /// keyboard height
    CGFloat keyboardH = 0;
    if (beginFrame.origin.y == [[UIScreen mainScreen] bounds].size.height){
        // up
        keyboardH = endFrame.size.height;
    }else if (endFrame.origin.y == [[UIScreen mainScreen] bounds].size.height) {
        // down
        keyboardH = 0;
    }else{
        // up
        keyboardH = endFrame.size.height;
    }
    /// 回调
    !completion?:completion(duration,options,keyboardH);
}

@end
