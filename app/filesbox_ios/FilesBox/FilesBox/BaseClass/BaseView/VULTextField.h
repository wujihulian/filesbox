//
//  VULTextField.h
//  VideoULimit
//
//  Created by svnlan on 2018/12/19.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class VULTextField;

typedef void(^ VULTextFieldHandler)(VULTextField *textField);

typedef NS_ENUM(NSInteger, VULTextFieldType) {
    /**
     *  默认
     */
    VULTextFieldTypeDefault             = 0,
    /**
     *  左边空隙 10pt
     */
    VULTextFieldTypeSpace            = 1,
    /**
    *  底部细线
    */
    VULTextFieldTypeUnderLine       = 2
};

@interface VULTextField : UITextField

@property (nonatomic,strong) NSString *leftImageName;

/**
 最大限制文本长度, 默认为无穷大, 即不限制, 如果被设为 0 也同样表示不限制字符数.
 */
@property (nonatomic, assign) IBInspectable NSUInteger maxLength;

/**
 价格(只有一个"."，小数点后保留2位小数)
 */
@property (nonatomic, assign) BOOL isPrice;

/**
 设定文本改变Block回调.
 */
- (void)addTextDidChangeHandler:(VULTextFieldHandler)eventHandler;

/**
 设定达到最大长度Block回调. 
 */
- (void)addTextLengthDidMaxHandler:(VULTextFieldHandler)maxHandler;


- (void)addLimitMaxLength:(NSInteger)num ChangeBlock:(VULTextFieldHandler)changeBlock MaxBlock:(VULTextFieldHandler)maxBlock;


+ (VULTextField *)getTextFieldWithFrame:(CGRect)frame leftImageName:(NSString *)imgName placeholder:(NSString *)placeholder borderWidth:(NSInteger)borderWidth cornerRadius:(NSInteger)cornerRadius type:(VULTextFieldType)type;

+ (VULTextField *)getTextFieldWithFrame:(CGRect)frame leftImageName:(NSString *)imgName placeholder:(NSString *)placeholder;

@end

NS_ASSUME_NONNULL_END
