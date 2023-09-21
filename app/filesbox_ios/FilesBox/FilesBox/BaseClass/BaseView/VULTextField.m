//
//  VULTextField.m
//  VideoULimit
//
//  Created by svnlan on 2018/12/19.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import "VULTextField.h"

@interface VULTextField ()<UITextFieldDelegate>

@property (nonatomic,strong) UIImageView *leftImage;
@property (nonatomic,assign) VULTextFieldType type;
@property (nonatomic, copy) VULTextFieldHandler changeHandler; ///< 文本改变Block
@property (nonatomic, copy) VULTextFieldHandler maxHandler; ///< 达到最大限制字符数Block

@end
@implementation VULTextField

+ (VULTextField *)getTextFieldWithFrame:(CGRect)frame leftImageName:(NSString *)imgName placeholder:(NSString *)placeholder borderWidth:(NSInteger)borderWidth cornerRadius:(NSInteger)cornerRadius type:(VULTextFieldType)type {
    VULTextField *textField = [[VULTextField alloc] initWithFrame:frame];
    if (NSStringIsNotEmpty(imgName)) {
        textField.leftImage.image = [UIImage imageNamed:imgName];
        textField.leftViewMode = UITextFieldViewModeAlways;
    } else {
        textField.leftViewMode = UITextFieldViewModeNever;
    }
    NSMutableAttributedString *attributed = [[NSMutableAttributedString alloc] initWithString:placeholder attributes:@{NSFontAttributeName : [UIFont systemFontOfSize:15]}];
    textField.attributedPlaceholder = attributed;
    textField.layer.borderWidth = borderWidth;
    textField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    textField.layer.borderColor = VULRGBAColor(218, 218, 218, 1.0).CGColor;
    textField.layer.cornerRadius = cornerRadius;
    textField.type = type;
    [textField setNeedsDisplay];
    return textField;
}

+ (VULTextField *)getTextFieldWithFrame:(CGRect)frame leftImageName:(NSString *)imgName placeholder:(NSString *)placeholder {
    VULTextField *textField = [[VULTextField alloc] initWithFrame:frame];
//    if (NSStringIsNotEmpty(imgName)) {
//        textField.leftImage.image = [UIImage imageNamed:imgName];
//        textField.leftViewMode = UITextFieldViewModeAlways;
//    } else {
//        textField.leftViewMode = UITextFieldViewModeNever;
//    }
    UIImage *image = [UIImage imageNamed:imgName];
    UIImageView *_leftImage = [[UIImageView alloc] init];
    _leftImage.image = image;
    //_leftImage.frame = CGRectMake(0, 0, image.size.width, image.size.height);
    [textField addSubview:_leftImage];
    [_leftImage mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(0);
        make.centerY.mas_equalTo(textField);
        make.size.mas_equalTo(CGSizeMake(image.size.width, image.size.height));
    }];
   
    //textField.leftView = _leftImage;
    
    UIView *leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 30, 0)];
    textField.leftViewMode = UITextFieldViewModeAlways;
    textField.leftView = leftView;
    
    
    NSMutableAttributedString *attributed = [[NSMutableAttributedString alloc] initWithString:placeholder attributes:@{NSFontAttributeName : [UIFont systemFontOfSize:15]}];
    textField.attributedPlaceholder = attributed;
    textField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    [textField setNeedsDisplay];
    
    UIView *line = [UIView new];
    NSLog(@"ddddd---%f--%f",frame.size.width,image.size.width);
    line.frame = CGRectMake(10, frame.size.height - 1, frame.size.width-0, 1);
    line.backgroundColor = UIColorHex(#F7F7F7);
    [textField addSubview:line];
    return textField;
}

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        //设置文边框左边专属View
        _leftImage = [[UIImageView alloc] init];
        _leftImage.bounds = CGRectMake(0, 0, 20, 20);
        _leftImage.contentMode = UIViewContentModeScaleAspectFit;
        self.leftView = _leftImage;
        self.leftViewMode = UITextFieldViewModeAlways;
    }
    return self;
}

- (BOOL)becomeFirstResponder
{
    BOOL become = [super becomeFirstResponder];
    
    // 成为第一响应者时注册通知监听文本变化
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textDidChange:) name:UITextFieldTextDidChangeNotification object:nil];
    
    return become;
}

- (BOOL)resignFirstResponder
{
    BOOL resign = [super resignFirstResponder];
    
    // 注销第一响应者时移除文本变化的通知, 以免影响其它的`UITextView`对象.
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:nil];
    
    return resign;
}

#pragma mark - NSNotification
- (void)textDidChange:(NSNotification *)notification
{
    // 通知回调的实例的不是当前实例的话直接返回
    if (notification.object != self) return;
    
    // 根据字符数量显示或者隐藏 `placeholderLabel`
    //self.placeholderLabel.hidden = [@(self.text.length) boolValue];
    
    // 禁止第一个字符输入空格或者换行
    if (self.text.length == 1) {
        
        if ([self.text isEqualToString:@" "] || [self.text isEqualToString:@"\n"]) {
            
            self.text = @"";
        }
    }
    
    // 只有当maxLength字段的值不为无穷大整型也不为0时才计算限制字符数.
    if (_maxLength != NSUIntegerMax && _maxLength != 0 && self.text.length > 0) {
        
        if (!self.markedTextRange && self.text.length > _maxLength) {
            
            !_maxHandler ?: _maxHandler(self); // 回调达到最大限制的Block.
            self.text = [self.text substringToIndex:_maxLength]; // 截取最大限制字符数.
            [self.undoManager removeAllActions]; // 达到最大字符数后清空所有 undoaction, 以免 undo 操作造成crash.
        }
    }
    
    // 回调文本改变的Block.
    !_changeHandler ?: _changeHandler(self);
}

- (void)addTextDidChangeHandler:(VULTextFieldHandler)changeHandler
{
    _changeHandler = [changeHandler copy];
}

- (void)addTextLengthDidMaxHandler:(VULTextFieldHandler)maxHandler
{
    _maxHandler = [maxHandler copy];
}

- (void)addLimitMaxLength:(NSInteger)num ChangeBlock:(VULTextFieldHandler)changeBlock MaxBlock:(VULTextFieldHandler)maxBlock {
    _maxLength = num;
    _changeHandler = [changeBlock copy];
    _maxHandler = [maxBlock copy];
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    if (_isPrice) {
//        if (range.length >= 1) { // 删除数据, 都允许删除
//            return YES;
//        }
//        if (![self checkDecimal:[textField.text stringByAppendingString:string]]) {
//            if (textField.text.length > 0 && [string isEqualToString:@"."] && ![textField.text containsString:@"."]) {
//                return YES;
//            }
//            return NO;
//        }
//        return YES;
        return [self limitPriceWithTextField:textField shouldChangeCharactersInRange:range replacementString:string];
    }
    return YES;
}

- (BOOL)limitPriceWithTextField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    BOOL hasPoint = YES;
    if ([textField.text rangeOfString:@"."].location == NSNotFound) {
        hasPoint = NO;
    }
    if ([string length] > 0){
        ///当前输入的字符
        unichar single = [string characterAtIndex:0];
        ///数据格式正确
        if ((single >= '0' && single <= '9') || single == '.') {
            if (0) {
                ///首字母可以为小数点
                if([textField.text length] == 0){
                    if(single == '.'){
                        //此处强制让textField.text = 0,然后又return YES,这样第一个字符输入`.`，显示的就是`0.`。
                        textField.text = @"0";
                        return YES;
                    }
                }
            }else{
                ///首字母不能为小数点
                if([textField.text length] == 0){
                    if(single == '.'){
                        [textField.text stringByReplacingCharactersInRange:range withString:@""];
                        return NO;
                    }
                }
            }
            
            ///如果开头是`0`，自动添加`.`，则输入框文字为`0.`
            if([textField.text length] == 1 && [textField.text isEqualToString:@"0"]){
                if(single != '.'){
                    textField.text = @"0.";
                    return YES;
                }
            }
            
            ///限制只能输入一个`.`
            if (single == '.'){
                if(hasPoint){
                    ///text中有小数点
                    [textField.text stringByReplacingCharactersInRange:range withString:@""];
                    return NO;
                }else{
                    ///text中还没有小数点
                    hasPoint = YES;
                    return YES;
                }
            }else{
                if (hasPoint){
                    ///存在小数点
                    ///判断小数点的位数
                    NSRange pointRange = [textField.text rangeOfString:@"."];
                    NSUInteger pointDigit = range.location - pointRange.location;
                    if (pointDigit <= 2){
                        return YES;
                    }else{
                        return NO;
                    }
                }else{
                    return YES;
                }
            }
        }else{
            ///输入的数据格式不正确
            [textField.text stringByReplacingCharactersInRange:range withString:@""];
            return NO;
        }
    }else{
        return YES;
    }
}

/**
 判断是否是两位小数

 @param str 字符串
 @return yes/no
 */
- (BOOL)checkDecimal:(NSString *)str {
    NSString *regex = @"^[0-9]+(\\.[0-9]{1,2})?$";
    NSPredicate *pred = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regex];
    
    if([pred evaluateWithObject: str])
    {
        return YES;
    }else{
        return NO;
    }
}

- (void)setIsPrice:(BOOL)isPrice {
    _isPrice = isPrice;
    self.delegate = self;
    if (isPrice) {
        self.keyboardType = UIKeyboardTypeDecimalPad;
    } else
        self.keyboardType = UIKeyboardTypeDefault;
}

- (void)setType:(VULTextFieldType)type {
    _type = type;
    if (type == VULTextFieldTypeUnderLine) {
        UIView *line = [UIView new];
        line.frame = CGRectMake(10, CGRectGetMaxY(self.frame)-1, self.frame.size.width-10, 0.7);
        line.backgroundColor = kLineColor;
        [self addSubview:line];
    }
}

- (void)setLeftImageName:(NSString *)leftImageName {
    _leftImageName = leftImageName;
    _leftImage.image = [UIImage imageNamed:leftImageName];
}

//  重写文字输入时的X值
- (CGRect)editingRectForBounds:(CGRect)bounds {
    CGRect editingRect = [super editingRectForBounds:bounds];
    if (_type) {
        editingRect.origin.x += 10;
    }
    return editingRect;
}

//  重写文字显示时的X值
- (CGRect)textRectForBounds:(CGRect)bounds {
    CGRect textRect = [super editingRectForBounds:bounds];
    if (_type) {
        textRect.origin.x += 10;
    }
    return textRect;
}

- (CGRect)leftViewRectForBounds:(CGRect)bounds {
    CGRect leftRect = [super leftViewRectForBounds:bounds];
    if (_type) {
        leftRect.origin.x += 15;
    }
    return leftRect;
}


@end
