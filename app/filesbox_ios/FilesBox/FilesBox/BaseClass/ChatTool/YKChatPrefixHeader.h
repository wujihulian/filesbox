//
//  YKChatPrefixHeader.h
//  YKChat
//
//  Created by yuekewei on 2020/5/25.
//  Copyright © 2020 yuekewei. All rights reserved.
//

#ifndef YKChatPrefixHeader_h
#define YKChatPrefixHeader_h

#import "YKChatCategoryHeader.h"
#import "ICMessageConst.h"

static inline BOOL CH_isIPhoneXSeries() {
    BOOL iPhoneXSeries = NO;
    if (UIDevice.currentDevice.userInterfaceIdiom != UIUserInterfaceIdiomPhone) {
        return iPhoneXSeries;
    }
    if (@available(iOS 11.0, *)) {
        UIWindow *mainWindow = [[[UIApplication sharedApplication] delegate] window];
        if (mainWindow.safeAreaInsets.bottom > 0.0) {
            iPhoneXSeries = YES;
        }
    }
    return iPhoneXSeries;
}
#define CH_IS_IPHONEX               CH_isIPhoneXSeries()

#pragma mark ======================== 尺寸、比例 ===========================

#define k_CH_StatusBarHeight       [UIApplication sharedApplication].statusBarFrame.size.height

#define k_CH_NavBarHeight          (kStatusBarHeight + 44.0)

#define k_CH_SafeArea_Top       (CH_IS_IPHONEX ? 44 : 0)

#define k_CH_SafeArea_Bottom       (CH_IS_IPHONEX ? 34 : 0)

#define k_CH_BottomBarHeight       (CH_IS_IPHONEX ? 34 + 49 : 49)

#define HEIGHT_TABBAR       49      // 就是chatBox的高度

#define k_CH_ScreenWidth       [UIScreen mainScreen].bounds.size.width
#define k_CH_ScreenHeight      [UIScreen mainScreen].bounds.size.height

#define HEIGHT_SCREEN       [UIScreen mainScreen].bounds.size.height
#define WIDTH_SCREEN        [UIScreen mainScreen].bounds.size.width

#define     CHATBOX_BUTTON_WIDTH        44
#define     HEIGHT_TEXTVIEW             35
#define     MAX_TEXTVIEW_HEIGHT         104

#define App_Delegate ((AppDelegate*)[[UIApplication sharedApplication]delegate])

#define App_RootCtr  [UIApplication sharedApplication].keyWindow.rootViewController

#define WEAKSELF __weak typeof(self) weakSelf = self;

#define XZColor(r, g, b) [UIColor colorWithRed:(r)/255.0 green:(g)/255.0 blue:(b)/255.0 alpha:1.0]

#define IColor(r, g, b) [UIColor colorWithRed:(r)/255.0 green:(g)/255.0 blue:(b)/255.0 alpha:1.0]

#define XZRGB(rgbValue) [UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16)) / 255.0 green:((float)((rgbValue & 0xFF00) >> 8)) / 255.0 blue:((float)(rgbValue & 0xFF)) / 255.0 alpha:1.0]

#define ICRGB(rgbValue) [UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16)) / 255.0 green:((float)((rgbValue & 0xFF00) >> 8)) / 255.0 blue:((float)(rgbValue & 0xFF)) / 255.0 alpha:1.0]

#define BACKGROUNDCOLOR   XZRGB(0xf4f1f1)
#define SEARCHBACKGROUNDCOLOR  [UIColor colorWithRed:(110.0)/255.0 green:(110.0)/255.0 blue:(110.0)/255.0 alpha:0.4]


#define EMOJI_CODE_TO_SYMBOL(x) ((((0x808080F0 | (x & 0x3F000) >> 4) | (x & 0xFC0) << 10) | (x & 0x1C0000) << 18) | (x & 0x3F) << 24);



#endif /* YKChatPrefixHeader_h */
