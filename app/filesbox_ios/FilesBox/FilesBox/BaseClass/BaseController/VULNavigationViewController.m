//
//  VULNavigationViewController.m
//  VideoULimit
//
//  Created by ZCc on 2018/9/4.
//  Copyright © 2018年 zcc. All rights reserved.
//

#import "VULNavigationViewController.h"
#import "VULBaseViewController.h"
//#import "VULTeacherLivePushVC.h"
//#import "VULFolderVideoPlayVC.h"

@interface VULNavigationViewController ()<UINavigationControllerDelegate, UIGestureRecognizerDelegate>

@property (nonatomic, weak)id PopDelegate;

@end

@implementation VULNavigationViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    if (iOS7Later) {
        self.automaticallyAdjustsScrollViewInsets = NO;
    }
    
    UIGestureRecognizer *gesture = self.interactivePopGestureRecognizer;
    gesture.enabled = NO;
    UIView *gestureView = gesture.view;
    
    FullscreenPopGestureRecognizer *popRecognizer = [[FullscreenPopGestureRecognizer alloc] init];
    popRecognizer.delegate = self;
    popRecognizer.maximumNumberOfTouches = 1;
    [gestureView addGestureRecognizer:popRecognizer];
    
    NSMutableArray *_targets = [gesture valueForKey:@"_targets"];
    
    id gestureRecoginzerTarget = _targets.firstObject;
    id navigationInteractiveTransition = [gestureRecoginzerTarget valueForKey:@"_target"];
    
    SEL handleTransition = NSSelectorFromString(@"handleNavigationTransition:");
    [popRecognizer addTarget:navigationInteractiveTransition action:handleTransition];
}

- (void)enablePopGesture:(BOOL)enable {
    UIGestureRecognizer *gesture = self.interactivePopGestureRecognizer;
    UIView *gestureView = gesture.view;
    NSArray *array = gestureView.gestureRecognizers;
    UIPanGestureRecognizer *viewGesture = array.lastObject;
    if (viewGesture.enabled != enable) {
        viewGesture.enabled = enable;
    }
}

- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer {
    
//    if ([self.viewControllers.lastObject isKindOfClass:[VULTeacherLivePushVC class]] || VULSCREEN_WIDTH_VARIABLE > VULSCREEN_HEIGHT_VARIABLE) {
//        return NO;
//    }
    
    if ([gestureRecognizer isKindOfClass:[FullscreenPopGestureRecognizer class]]) {
        FullscreenPopGestureRecognizer *pan = (FullscreenPopGestureRecognizer *)gestureRecognizer;
        CGPoint translation = [pan translationInView:gestureRecognizer.view];
        if ([pan locationInView:gestureRecognizer.view].x > 40 || translation.x <= 0) {
            return NO;
        }
    }
    /// 这里有两个条件不允许手势执行 1.当前控制器为根控制器； 2.如果这个push pop 动画正在执行 （私有属性）
    return self.viewControllers.count != 1 && ![[self valueForKey:@"_isTransitioning"] boolValue];
}

- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated{
    
    if (viewController == self.viewControllers[0]) {
        self.interactivePopGestureRecognizer.delegate = self.PopDelegate;
    } else {
        self.interactivePopGestureRecognizer.delegate = nil;
    }
}

- (void)pushViewController:(UIViewController *)viewController animated:(BOOL)animated {
    if (self.childViewControllers.count) {
        // 隐藏导航栏
        viewController.hidesBottomBarWhenPushed = YES;
        // 自定义返回按钮
        if([viewController isKindOfClass:[VULBaseViewController class]]){
            VULBaseViewController *baseVC = (VULBaseViewController *)viewController;
            [baseVC defaultNavBackItemIcon];
        }

        // 如果自定义返回按钮后, 滑动返回可能失效, 需要添加下面的代码
        __weak typeof(viewController)Weakself = viewController;
        self.interactivePopGestureRecognizer.delegate = (id)Weakself;
    }
    [super pushViewController:viewController animated:animated];
}



- (UIStatusBarStyle)preferredStatusBarStyle {
    return UIStatusBarStyleLightContent;
}

#pragma mark - 所有控制器的旋转方向交给控制器自己处理
- (BOOL)shouldAutorotate {
    return self.topViewController.shouldAutorotate;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return self.topViewController.supportedInterfaceOrientations;
}

- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation {
    return self.topViewController.preferredInterfaceOrientationForPresentation;
}

@end


@implementation FullscreenPopGestureRecognizer


@end


@implementation UINavigationController (Extension)

- (void)popToNewController:(UIViewController *)newController
                  animated:(BOOL)animated {
    
    NSMutableArray < UIViewController *> *controllers = [[NSMutableArray alloc] initWithArray:self.viewControllers];
    [controllers replaceObjectAtIndex:controllers.count - 2 withObject:newController];

    [self setViewControllers:controllers animated:animated];
}

- (void)pushViewController:(UIViewController *)viewController
          backToController:(UIViewController *)backController
                  animated:(BOOL)animated {
    
    if (self.childViewControllers.count != 0 &&
        [viewController respondsToSelector:@selector(setHidesBottomBarWhenPushed:)]) {
        
        viewController.hidesBottomBarWhenPushed = YES;
    }
    
    NSMutableArray < UIViewController *> *controllers = [NSMutableArray new];
    if ([self.viewControllers containsObject:backController]) {
        for (NSInteger i = 0; i <= [self.viewControllers indexOfObject:backController]; i++) {
            UIViewController *vc = [self.viewControllers objectAtIndex:i];
            [controllers addObject:vc];
        }
        [controllers addObject:viewController];
    }
    else {
        [controllers addObjectsFromArray:self.viewControllers];
        [controllers removeLastObject];
        [controllers addObject:backController];
        [controllers addObject:viewController];
    }

    [self setViewControllers:controllers animated:animated];
}

- (void)pushViewController:(UIViewController *)viewController
     backToControllerIndex:(NSInteger)controllerIndex
                  animated:(BOOL)animated {
    
    [self pushViewController:viewController backToController:[self.viewControllers objectAtIndex:controllerIndex] animated:animated];
}


@end
