//
//  VULChatTextParser.h
//  VideoULimit
//
//  Created by yuekewei on 2020/7/2.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YYKit.h"
#import "ICFaceManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULChatTextParser : NSObject<YYTextParser>

@property (nonatomic, strong) UIFont *font;
@property (nonatomic, strong) UIColor *textColor;

+ (NSMutableAttributedString *)parserEmoticonMessage:(NSString *)message
                                                font:(UIFont *)font
                                          lineHeight:(CGFloat)lineHeight;
+ (NSRegularExpression *)regexEmoticon;
@end

NS_ASSUME_NONNULL_END
