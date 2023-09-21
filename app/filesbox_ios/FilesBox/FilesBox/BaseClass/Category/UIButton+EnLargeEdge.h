//
//  UIButton+EnLargeEdge.h
//  PictureEdit
//
//  Created by ZCc on 2018/8/28.
//  Copyright © 2018年 zcc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <objc/runtime.h>
@interface UIButton (EnLargeEdge)

/**
 同时向四周延伸面积

 @param size size 四周延伸的距离
 */
- (void)setEnlargeEdge:(CGFloat)size;


/**
 分别向四周延伸响应面积

 @param top 上
 @param left 左
 @param bottom 下
 @param right 右
 */
-(void)setEnlargeWithTop:(CGFloat)top left:(CGFloat)left bottom:(CGFloat)bottom right:(CGFloat)right;

@end
