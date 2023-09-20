//
//  UIFont+YKCategory.m
//  Tairong
//
//  Created by yuekewei on 2019/7/16.
//

#import "UIFont+YKCategory.h"
#import <objc/runtime.h>
#import <CoreText/CoreText.h>

@implementation UIFont (YKCategory)

+ (void)load {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        method_exchangeImplementations(class_getClassMethod(self, @selector(systemFontOfSize:)), class_getClassMethod(self, @selector(yk_systemFontOfSize:)));
        method_exchangeImplementations(class_getClassMethod(self, @selector(boldSystemFontOfSize:)), class_getClassMethod(self, @selector(yk_boldSystemFontOfSize:)));
    });
}

+ (UIFont *)yk_systemFontOfSize:(CGFloat)fontSize {
    return [self yk_pingFangRegular:fontSize];
}

+ (UIFont *)yk_boldSystemFontOfSize:(CGFloat)fontSize {
    return [self yk_pingFangMedium:fontSize];
}

+ (nullable UIFont *)yk_fontWithName:(NSString *)fontName size:(CGFloat)fontSize {
    UIFont *font = [UIFont fontWithName:fontName size:fontSize];
    return font ?: [UIFont fontWithName:[fontName isEqualToString:@"PingFangSC-Regular"] ? @".SFUIText" : @".SFUIText-Semibold" size:fontSize];
}

+ (UIFont *)yk_pingFangMedium:(CGFloat)fontSize {
    return [self yk_fontWithName:@"PingFangSC-Medium" size:fontSize];
}

+ (UIFont *)yk_pingFangSemibold:(CGFloat)fontSize {
    return [self yk_fontWithName:@"PingFangSC-Semibold" size:fontSize];
}

+ (UIFont *)yk_pingFangLight:(CGFloat)fontSize {
    return [self yk_fontWithName:@"PingFangSC-Light" size:fontSize];
}

+ (UIFont *)yk_pingFangUltralight:(CGFloat)fontSize {
    return [self yk_fontWithName:@"PingFangSC-Ultralight" size:fontSize];
}

+ (UIFont *)yk_pingFangRegular:(CGFloat)fontSize {
    return [self yk_fontWithName:@"PingFangSC-Regular" size:fontSize];
}

+ (UIFont *)yk_pingFangThin:(CGFloat)fontSize {
    return [self yk_fontWithName:@"PingFangSC-Thin" size:fontSize];
}

/**
 注册字体文件

 @param path 字体文件路径
 */
- (void)yk_registerFontWithFontFilePath:(NSString *)path {
    if (!path) {
        return;
    }
    [self yk_registerFontWithFontData:[NSData dataWithContentsOfFile:path]];
}

/**
 注册字体文件

 @param fontData  字体文件NSData
 */
- (void)yk_registerFontWithFontData:(NSData *)fontData {
    if (!fontData)
        return;
    CFErrorRef error;
    CGDataProviderRef providerRef = CGDataProviderCreateWithCFData((CFDataRef)fontData);
    CGFontRef fontRef = CGFontCreateWithDataProvider(providerRef);
    if (!CTFontManagerRegisterGraphicsFont(fontRef, &error)) {
        CFStringRef errorDescription = CFErrorCopyDescription(error);
        CFRelease(errorDescription);
    }
    CFRelease(fontRef);
    CFRelease(providerRef);
}

/**
 打印系统所有字体名称
 */
+ (void)yk_printAllFonts {
    NSArray *fontFamilies = [UIFont familyNames];
    for (NSString *fontFamily in fontFamilies) {
        NSArray *fontNames = [UIFont fontNamesForFamilyName:fontFamily];
        NSLog (@"%@: %@", fontFamily, fontNames);
    }
}
@end
