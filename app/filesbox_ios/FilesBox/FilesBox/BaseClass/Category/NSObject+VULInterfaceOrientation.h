//
//  NSObject+VULInterfaceOrientation.h
//  VideoULimit
//
//  Created by ZCc on 2019/5/29.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSObject (VULInterfaceOrientation)

@property (nonatomic, assign, getter=isVideoVC, setter=setIsVideoVC:) BOOL isVideoVC;

- (UIResponder<UIApplicationDelegate> *)vul_shareAppDelegate;

- (void)vul_forceUpdateInterfaceOrientation:(UIInterfaceOrientationMask)orientation;

- (void)vul_convertNotification:(NSNotification *)notification completion:(void (^ _Nullable)(CGFloat, UIViewAnimationOptions, CGFloat))completion;

@end

NS_ASSUME_NONNULL_END
