//
//  NSString+YK_CH_Extension.h
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface NSString (YK_CH_Extension)

- (NSString *)emoji;

- (CGSize)sizeWithMaxWidth:(CGFloat)width andFont:(UIFont *)font;

- (NSString *)originName;

+ (NSString *)currentName;

- (NSString *)firstStringSeparatedByString:(NSString *)separeted;



@end
