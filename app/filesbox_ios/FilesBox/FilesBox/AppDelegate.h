//
//  AppDelegate.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/13.
//

#import <UIKit/UIKit.h>
#import "FBWatermarkView.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) FBWatermarkView *watermarkView;

@property (nonatomic, assign) NSInteger netWorkStatesCode;/**< 网络状态 */
@property (nonatomic, assign) BOOL zfplayer_allowOrentitaionRotation;
+ (void)zfplayer_allowOrentitaionRotation:(BOOL)allowOrentitaionRotation;
- (void)networkChange ;
@end

