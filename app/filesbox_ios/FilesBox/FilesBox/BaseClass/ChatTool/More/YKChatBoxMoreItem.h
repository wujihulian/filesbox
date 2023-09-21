//
//  YKChatBoxMoreItem.h
//  VideoULimit
//
//  Created by yuekewei on 2020/6/22.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YKChatBoxMoreItem : NSObject

@property (nonatomic, strong) NSString *title;
@property (nonatomic, strong) NSString *imageName;
@property (nonatomic, assign) NSInteger tag;

+ (YKChatBoxMoreItem *)itemWithTitle:(NSString *)title imageName:(NSString *)imageName tag:(NSInteger)tag;
@end

NS_ASSUME_NONNULL_END
