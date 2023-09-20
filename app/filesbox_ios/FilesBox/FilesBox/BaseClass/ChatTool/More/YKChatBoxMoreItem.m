//
//  YKChatBoxMoreItem.m
//  VideoULimit
//
//  Created by yuekewei on 2020/6/22.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "YKChatBoxMoreItem.h"

@interface YKChatBoxMoreItem ()

@property (nonatomic, strong) UIButton *button;
@property (nonatomic, strong) UILabel *titleLabel;

@end

@implementation YKChatBoxMoreItem

+ (YKChatBoxMoreItem *)itemWithTitle:(NSString *)title imageName:(NSString *)imageName tag:(NSInteger)tag {
    YKChatBoxMoreItem *item = [[YKChatBoxMoreItem alloc] init];
    item.title = title;
    item.imageName = imageName;
    item.tag = tag;
    return item;
}

@end
