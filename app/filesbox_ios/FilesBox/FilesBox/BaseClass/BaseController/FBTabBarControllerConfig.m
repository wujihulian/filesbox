//
//  FBTabBarControllerConfig.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/16.
//

#import "FBTabBarControllerConfig.h"
#import "FBHomeViewController.h"
#import "FBMIneViewController.h"
#import "FBFindViewController.h"
@interface FBTabBarControllerConfig ()<UITabBarControllerDelegate>

@property (nonatomic, readwrite, strong) CYLTabBarController *tabBarController;

@end

@implementation FBTabBarControllerConfig

#pragma mark - getter and setter
/**
 *  lazy load tabBarController
 *
 *  @return CYLTabBarController
 */
- (CYLTabBarController *)tabBarController {
    if (_tabBarController == nil) {
        CYLTabBarController *tabBarController = [CYLTabBarController tabBarControllerWithViewControllers:self.viewControllers tabBarItemsAttributes:self.tabBarItemsAttributesForController];
        tabBarController.delegate =self;
        [self customizeTabBarAppearance:tabBarController];
            _tabBarController = tabBarController;
  

    }
    return _tabBarController;
}
- (NSArray *)viewControllers {
    // 首页
    FBHomeViewController *firstViewController = [[FBHomeViewController alloc] init];
    VULNavigationViewController *firstNavViewController = [[VULNavigationViewController alloc] initWithRootViewController:firstViewController];
    FBHomeViewController *firstViewController1 = [[FBHomeViewController alloc] init];
    firstViewController1.flag = YES;
    VULNavigationViewController *firstNavViewController1 = [[VULNavigationViewController alloc] initWithRootViewController:firstViewController1];
    FBFindViewController *firstViewController3 = [[FBFindViewController alloc] init];
    VULNavigationViewController *firstNavViewController2 = [[VULNavigationViewController alloc] initWithRootViewController:firstViewController3];
    FBMIneViewController *mineVC = [[FBMIneViewController alloc] init];
    VULNavigationViewController *firstNavViewController3 = [[VULNavigationViewController alloc] initWithRootViewController:mineVC];
    
    NSArray *viewControllers;

    viewControllers=  @[
        firstNavViewController,
        firstNavViewController1,
        firstNavViewController2,
        firstNavViewController3];
    return viewControllers;
}
- (NSArray *)tabBarItemsAttributesForController {
    NSDictionary *firstTabBarItemsAttributes = @{
                                                 CYLTabBarItemTitle : KLanguage(changeName(@"主页")) ,
                                                 CYLTabBarItemImage : @"bar1_normal",
                                                 CYLTabBarItemSelectedImage : @"bar1_select",
                                                 };
    
    NSDictionary *secondTabBarItemsAttributes = @{
                                                  CYLTabBarItemTitle : KLanguage(@"共享"),
                                                  CYLTabBarItemImage : @"icon_select_data_false",
                                                  CYLTabBarItemSelectedImage : @"icon_select_data_true"
                                                  };
    NSDictionary *messageTabBarItemsAttributes = @{
                                                  CYLTabBarItemTitle : KLanguage(@"发现"),
                                                  CYLTabBarItemImage : @"icon_select_message_false",
                                                  CYLTabBarItemSelectedImage : @"icon_select_message_true"
                                                  };
    NSDictionary *fouthTabBarItemsAttributes = @{
                                                 CYLTabBarItemTitle : KLanguage(@"我的"),
                                                 CYLTabBarItemImage : @"bar4_normal",
                                                 CYLTabBarItemSelectedImage : @"bar4_select"
                                                 };
    return @[firstTabBarItemsAttributes,secondTabBarItemsAttributes,messageTabBarItemsAttributes,fouthTabBarItemsAttributes];
}
/**
 *  更多TabBar自定义设置：比如：tabBarItem 的选中和不选中文字和背景图片属性、tabbar 背景图片属性等等
 */
- (void)customizeTabBarAppearance:(CYLTabBarController *)tabBarController {
    tabBarController.tabBarHeight = CYL_IS_IPHONE_X ? 83 : 49;
    NSMutableDictionary *normalAttrs = [NSMutableDictionary dictionary];
    normalAttrs[NSForegroundColorAttributeName] = UIColorHex(#333333);//[UIColor colorWithHue:0.00 saturation:0.00 brightness:0.63 alpha:1.00];
    normalAttrs[NSFontAttributeName] = [UIFont yk_pingFangMedium:11];
    
    NSMutableDictionary *selectedAttrs = [NSMutableDictionary dictionary];
    selectedAttrs[NSForegroundColorAttributeName] = UIColorHex(#7459E3);//[UIColor colorWithHue:0.57 saturation:0.88 brightness:0.77 alpha:1.00];
    selectedAttrs[NSFontAttributeName] = [UIFont yk_pingFangMedium:11];
    
    UITabBarItem *tabBar = [UITabBarItem appearance];
    [tabBar setTitleTextAttributes:normalAttrs forState:UIControlStateNormal];
    [tabBar setTitleTextAttributes:selectedAttrs forState:UIControlStateSelected];
    tabBarController.tabBar.unselectedItemTintColor = UIColorHex(#333333);

    if (@available(iOS 10.0, *)) {
        tabBarController.tabBar.unselectedItemTintColor = UIColorHex(#333333);
    } else {
        // Fallback on earlier versions
    }
   
    [UIView transitionWithView:tabBarController.tabBar duration:0.5 options:UIViewAnimationOptionTransitionCrossDissolve animations:^{
        //针对ios13 进行设置
        if(@available(iOS 15.0, *)) {
            UITabBarAppearance *appearance = [tabBarController.tabBar.standardAppearance copy];
            [appearance configureWithOpaqueBackground];
            appearance.backgroundImage = getImageWithColor(UIColor.whiteColor);
            appearance.stackedLayoutAppearance.normal.titleTextAttributes =normalAttrs;
            appearance.stackedLayoutAppearance.selected.titleTextAttributes =selectedAttrs;
            tabBarController.tabBar.standardAppearance = appearance;
            tabBarController.tabBar.scrollEdgeAppearance = appearance;
            }else
        if (@available(iOS 13.0, *)) {
             UITabBarAppearance *appearance = [tabBarController.tabBar.standardAppearance copy];
             appearance.backgroundImage = [[UIImage alloc] init];
             appearance.shadowImage = getImageWithColor(HEXCOLOR(0xf2f2f2));
            appearance.stackedLayoutAppearance.normal.titleTextAttributes =normalAttrs;
            appearance.stackedLayoutAppearance.selected.titleTextAttributes =selectedAttrs;
            tabBarController.tabBar.standardAppearance = appearance;

         } else {
             tabBarController.tabBar.backgroundImage = [[UIImage alloc] init];
             tabBarController.tabBar.shadowImage = getImageWithColor(HEXCOLOR(0xf2f2f2));
         }
    

      } completion:NULL];

    [self updateTabBarCustomizationWhenTabBarItemWidthDidUpdate];
}

- (void)updateTabBarCustomizationWhenTabBarItemWidthDidUpdate {
    void (^deviceOrientationDidChangeBlock)(NSNotification *) = ^(NSNotification *notification) {
        UIDeviceOrientation orientation = [[UIDevice currentDevice] orientation];
        if ((orientation == UIDeviceOrientationLandscapeLeft) || (orientation == UIDeviceOrientationLandscapeRight)) {
            NSLog(@"Landscape Left or Right !");
        } else if (orientation == UIDeviceOrientationPortrait) {
            NSLog(@"Landscape portrait!");
        }
        [self customizeTabBarSelectionIndicatorImage];
    };
    [[NSNotificationCenter defaultCenter] addObserverForName:CYLTabBarItemWidthDidChangeNotification object:nil queue:[NSOperationQueue mainQueue] usingBlock:deviceOrientationDidChangeBlock];
}

- (void)customizeTabBarSelectionIndicatorImage {
    ///Get initialized TabBar Height if exists, otherwise get Default TabBar Height.
    CGFloat tabBarHeight = CYL_IS_IPHONE_X ? 83 : 49;
    CGSize selectionIndicatorImageSize = CGSizeMake(CYLTabBarItemWidth, tabBarHeight);
    //Get initialized TabBar if exists.
    UITabBar *tabBar = [self cyl_tabBarController].tabBar ?: [UITabBar appearance];
    [tabBar setSelectionIndicatorImage:[[self class] imageWithColor:[UIColor colorWithRed:0.97 green:0.97 blue:0.97 alpha:1.00] size:selectionIndicatorImageSize]];
}

+ (UIImage *)scaleImage:(UIImage *)image toScale:(float)scaleSize {
    UIGraphicsBeginImageContext(CGSizeMake([UIScreen mainScreen].bounds.size.width * scaleSize, image.size.height * scaleSize));
    [image drawInRect:CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width * scaleSize, image.size.height * scaleSize)];
    UIImage *scaledImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return scaledImage;
}

+ (UIImage *)imageWithColor:(UIColor *)color size:(CGSize)size {
    if (!color || size.width <= 0 || size.height <= 0) return nil;
    CGRect rect = CGRectMake(0.0f, 0.0f, size.width + 1, size.height);
    UIGraphicsBeginImageContextWithOptions(rect.size, NO, 0);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, color.CGColor);
    CGContextFillRect(context, rect);
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return image;
}
- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController{
    [[UIApplication sharedApplication].delegate.window.rootViewController dismissViewControllerAnimated:NO completion:^{
        
    }];}
- (void)dealloc {
    [VULNotificationCenter removeObserver:self];
}

@end
