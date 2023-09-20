//
//  VULChatTextParser.m
//  VideoULimit
//
//  Created by yuekewei on 2020/7/2.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "VULChatTextParser.h"

@interface VULTextImageAttachment : YYTextAttachment

@property (nonatomic, strong) NSURL *imageURL;
@property (nonatomic, strong) YYAnimatedImageView *imageView;
@property (nonatomic, assign) CGSize size;
@end

@implementation VULTextImageAttachment

- (void)setContent:(id)content {
    _imageView = content;
}

- (id)content {
    if (pthread_main_np() == 0) return nil;
    if (_imageView) return _imageView;
    
    _imageView = [YYAnimatedImageView new];
    _imageView.size = _size;
    [_imageView setImageWithURL:_imageURL placeholder:nil];
    return _imageView;
}

@end


@implementation VULChatTextParser

- (BOOL)parseText:(NSMutableAttributedString *)text selectedRange:(NSRangePointer)selectedRange {
    text.color = _textColor;
    
    {
        NSArray<NSTextCheckingResult *> *emoticonResults = [[VULChatTextParser regexEmoticon] matchesInString:text.string options:kNilOptions range:text.rangeOfAll];
        NSUInteger clipLength = 0;
        for (NSTextCheckingResult *emo in emoticonResults) {
            if (emo.range.location == NSNotFound && emo.range.length <= 1) continue;
            NSRange range = emo.range;
            range.location -= clipLength;
            if ([text attribute:YYTextAttachmentAttributeName atIndex:range.location]) continue;
            NSString *emoString = [text.string substringWithRange:range];
            NSString *imageName = [[ICFaceManager emotionsDic] objectForKey:emoString];
            imageName = [emoString substringWithRange:NSMakeRange(1, emoString.length - 2)];
            
            NSString *imagePath = nil;
            NSBundle *imageBundle =  [NSBundle bundleWithPath:[[NSBundle mainBundle] pathForResource:@"Emotion" ofType:@"bundle"]];
            NSArray *exts = @[@"png", @"jpeg", @"jpg", @"gif", @"webp", @"apng"];
            for (NSString *type in exts) {
                imagePath = [imageBundle pathForScaledResource:imageName ofType:type];
                if (imagePath) {
                    break;
                }
            }
            
            if (!imagePath) continue;
            YYImage *image = [YYImage imageWithContentsOfFile:imagePath];
            
            
            __block BOOL containsBindingRange = NO;
            [text enumerateAttribute:YYTextBindingAttributeName inRange:range options:NSAttributedStringEnumerationLongestEffectiveRangeNotRequired usingBlock:^(id value, NSRange range, BOOL *stop) {
                if (value) {
                    containsBindingRange = YES;
                    *stop = YES;
                }
            }];
            if (containsBindingRange) continue;
            
            
            YYTextBackedString *backed = [YYTextBackedString stringWithString:emoString];
            NSMutableAttributedString *emoText = [NSAttributedString attachmentStringWithEmojiImage:image fontSize:_font.pointSize].mutableCopy;
            // original text, used for text copy
            [emoText setTextBackedString:backed range:NSMakeRange(0, emoText.length)];
            [emoText setTextBinding:[YYTextBinding bindingWithDeleteConfirm:NO] range:NSMakeRange(0, emoText.length)];
            
            [text replaceCharactersInRange:range withAttributedString:emoText];
            
            if (selectedRange) {
                *selectedRange = [self _replaceTextInRange:range withLength:emoText.length selectedRange:*selectedRange];
            }
            clipLength += range.length - emoText.length;
        }
    }
    text.font = self.font;
    return YES;
}

// correct the selected range during text replacement
- (NSRange)_replaceTextInRange:(NSRange)range withLength:(NSUInteger)length selectedRange:(NSRange)selectedRange {
    // no change
    if (range.length == length) return selectedRange;
    // right
    if (range.location >= selectedRange.location + selectedRange.length) return selectedRange;
    // left
    if (selectedRange.location >= range.location + range.length) {
        selectedRange.location = selectedRange.location + length - range.length;
        return selectedRange;
    }
    // same
    if (NSEqualRanges(range, selectedRange)) {
        selectedRange.length = length;
        return selectedRange;
    }
    // one edge same
    if ((range.location == selectedRange.location && range.length < selectedRange.length) ||
        (range.location + range.length == selectedRange.location + selectedRange.length && range.length < selectedRange.length)) {
        selectedRange.length = selectedRange.length + length - range.length;
        return selectedRange;
    }
    selectedRange.location = range.location + length;
    selectedRange.length = 0;
    return selectedRange;
}


+ (NSMutableAttributedString *)parserEmoticonMessage:(NSString *)message
                                                font:(UIFont *)font
                                          lineHeight:(CGFloat)lineHeight {
    NSMutableAttributedString *text = [[NSMutableAttributedString alloc] initWithString:message];
    
    NSArray<NSTextCheckingResult *> *emoticonResults = [[VULChatTextParser regexEmoticon] matchesInString:text.string options:kNilOptions range:text.rangeOfAll];
    
    NSUInteger emoClipLength = 0;
    for (NSTextCheckingResult *emo in emoticonResults) {
        if (emo.range.location == NSNotFound && emo.range.length <= 1) continue;
        NSRange range = emo.range;
        range.location -= emoClipLength;
        if ([text attribute:YYTextHighlightAttributeName atIndex:range.location]) continue;
        if ([text attribute:YYTextAttachmentAttributeName atIndex:range.location]) continue;
        NSString *emoString = [text.string substringWithRange:range];
        NSString *imageName = [[ICFaceManager emotionsDic] objectForKey:emoString];
        imageName = [emoString substringWithRange:NSMakeRange(1, emoString.length - 2)];
        
        
        YYImage *image = [[DownloadProgress sharedInstance].emotionDic objectForKey:imageName];
        
        if (!image) { continue;}
        NSAttributedString *emoText = [NSAttributedString attachmentStringWithEmojiImage:image fontSize:lineHeight];
        [text replaceCharactersInRange:range withAttributedString:emoText];
        emoClipLength += range.length - 1;
    }
    
    text.font = font;
    text.color = UIColorHex(#333333);
    return text;
}

+ (NSAttributedString *)_attachmentWithFontSize:(CGFloat)fontSize imageURL:(NSString *)imageURL shrink:(BOOL)shrink {
    CGFloat ascent = fontSize * 0.86;
    CGFloat descent = fontSize * 0.14;
    CGRect bounding = CGRectMake(0, -0.14 * fontSize, fontSize, fontSize);
    UIEdgeInsets contentInsets = UIEdgeInsetsMake(ascent - (bounding.size.height + bounding.origin.y), 0, descent + bounding.origin.y, 0);
    CGSize size = CGSizeMake(fontSize, fontSize);
    
    if (shrink) {
        // 缩小~
        CGFloat scale = 1 / 10.0;
        contentInsets.top += fontSize * scale;
        contentInsets.bottom += fontSize * scale;
        contentInsets.left += fontSize * scale;
        contentInsets.right += fontSize * scale;
        contentInsets = UIEdgeInsetPixelFloor(contentInsets);
        size = CGSizeMake(fontSize - fontSize * scale * 2, fontSize - fontSize * scale * 2);
        size = CGSizePixelRound(size);
    }
    
    YYTextRunDelegate *delegate = [YYTextRunDelegate new];
    delegate.ascent = ascent;
    delegate.descent = descent;
    delegate.width = bounding.size.width;
    
    YYImage *image = [YYImage imageWithContentsOfFile:imageURL];
    YYTextAttachment *attachment = [YYTextAttachment new];
    attachment.contentMode = UIViewContentModeScaleAspectFit;
    attachment.contentInsets = contentInsets;
    attachment.content = image;
//    attachment.size = size;
//    attachment.imageURL = [NSURL URLWithString:imageURL];
    
    NSMutableAttributedString *atr = [[NSMutableAttributedString alloc] initWithString:YYTextAttachmentToken];
    [atr setTextAttachment:attachment range:NSMakeRange(0, atr.length)];
    CTRunDelegateRef ctDelegate = delegate.CTRunDelegate;
    [atr setRunDelegate:ctDelegate range:NSMakeRange(0, atr.length)];
    if (ctDelegate) CFRelease(ctDelegate);
    
    return atr;
}

+ (NSRegularExpression *)regexEmoticon {
    static NSRegularExpression *regex;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        regex = [NSRegularExpression regularExpressionWithPattern:@"\\[[^ \\[\\]]+?\\]" options:kNilOptions error:NULL];
    });
    return regex;
}
@end
