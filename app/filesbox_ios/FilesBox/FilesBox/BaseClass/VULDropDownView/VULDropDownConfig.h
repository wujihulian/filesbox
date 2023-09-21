//
//  VULDropDownConfig.h
//  VideoULimit
//
//  Created by svnlan on 2019/3/15.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

#define MenuContentMargin       15          //默认文本图片边距
#define MenuImageWidth          25          //默认图片最小尺寸
#define MenuBorderMinMargin     10          //默认下拉框边界围栏
#define MenuTitleFontSize       15          //默认文本字体大小

@interface VULDropDownConfig : NSObject
//文本图片边距
@property (nonatomic, assign) CGFloat zw_menuContentMargin;
//图片最小尺寸
@property (nonatomic, assign) CGFloat zw_menuImageWidth;
//下拉框边界围栏
@property (nonatomic, assign) CGFloat zw_menuBorderMinMargin;
//文本字体大小
@property (nonatomic, assign) CGFloat zw_menuTitleFontSize;

@end

NS_ASSUME_NONNULL_END
