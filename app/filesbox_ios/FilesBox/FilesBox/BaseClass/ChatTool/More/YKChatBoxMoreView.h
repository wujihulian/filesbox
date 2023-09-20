//
//  YKChatBoxMoreView.h
//  VideoULimit
//
//  Created by yuekewei on 2020/6/22.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YKChatBoxMoreItem.h"

NS_ASSUME_NONNULL_BEGIN

#define kMoreViewHeight 260

@class YKChatBoxMoreView;

@protocol YKChatBoxMoreViewDelegate <NSObject>
/**
 *  点击更多的类型
 *
 *  @param chatBoxMoreView ICChatBoxMoreView
 *  @param item        类型
 */
- (void)chatBoxMoreView:(YKChatBoxMoreView *)chatBoxMoreView didSelectItem:(YKChatBoxMoreItem *)item;

@end

@interface YKChatBoxMoreView : UIView

@property (nonatomic, weak) id<YKChatBoxMoreViewDelegate> delegate;
@property (nonatomic, strong) NSMutableArray *items;
@end

NS_ASSUME_NONNULL_END
