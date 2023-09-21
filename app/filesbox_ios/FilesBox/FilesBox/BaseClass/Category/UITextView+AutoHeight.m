//
//  UITextView+AutoHeight.m
//  VideoULimit
//
//  Created by svnlan on 2019/8/8.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import "UITextView+AutoHeight.h"
#import <objc/runtime.h>

@interface UITextView ()

@property (nonatomic, weak) id wj_observer;
@end

static CGFloat const defaultMinHeight = 40;

@implementation UITextView (AutoHeight)

+ (void)load {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        method_exchangeImplementations(class_getInstanceMethod(self, NSSelectorFromString(@"dealloc")), class_getInstanceMethod(self, @selector(wj_dealloc)));
        method_exchangeImplementations(class_getInstanceMethod(self, NSSelectorFromString(@"setText:")), class_getInstanceMethod(self, @selector(wj_setText:)));
    });
}

- (void)wj_dealloc {
    [[NSNotificationCenter defaultCenter]removeObserver:self.wj_observer];
    [self wj_dealloc];
}

- (void)wj_setText:(NSString *)text {
    [self wj_setText:text];
    if (text.length > 0) {
        self.wj_placeHolderLabel.hidden = YES;
    } else {
        self.wj_placeHolderLabel.hidden = NO;
    }
}
#pragma mark ---setters getters
- (void)setWj_observer:(id)wj_observer {
    objc_setAssociatedObject(self, @selector(wj_observer), wj_observer, OBJC_ASSOCIATION_ASSIGN);
}

- (id)wj_observer {
    id obj = objc_getAssociatedObject(self, _cmd);
    return obj;
}

- (void)setIsAutoHeightEnable:(BOOL)isAutoHeightEnable {
    objc_setAssociatedObject(self, @selector(isAutoHeightEnable), @(isAutoHeightEnable), OBJC_ASSOCIATION_ASSIGN);
}

- (BOOL)isAutoHeightEnable {
    return [objc_getAssociatedObject(self, _cmd) boolValue];
}

- (void)setWj_maxHeight:(CGFloat)wj_maxHeight {
    objc_setAssociatedObject(self, @selector(wj_maxHeight), @(wj_maxHeight), OBJC_ASSOCIATION_ASSIGN);
}

- (CGFloat)wj_maxHeight {
    return [objc_getAssociatedObject(self, _cmd) floatValue];
}

- (void)setWj_minHeight:(CGFloat)wj_minHeight {
    objc_setAssociatedObject(self, @selector(wj_minHeight), @(wj_minHeight), OBJC_ASSOCIATION_ASSIGN);
}

- (CGFloat)wj_minHeight {
    CGFloat minHeight = [objc_getAssociatedObject(self, _cmd) floatValue];
    if (minHeight == 0) {
        minHeight = defaultMinHeight;
    }
    return minHeight;
}

- (void)setWj_placeHolder:(NSString *)wj_placeHolder {
    if (!wj_placeHolder) {
        return;
    }
    if (!self.wj_observer) {
        __weak typeof(self) weakSelf = self;
        id observer = [[NSNotificationCenter defaultCenter]addObserverForName:UITextViewTextDidChangeNotification object:nil queue:[NSOperationQueue mainQueue] usingBlock:^(NSNotification * _Nonnull note) {
            __strong typeof(weakSelf) strongSelf = weakSelf;
            if (note.object == strongSelf) {
                if (strongSelf.text.length == 0) {
                    strongSelf.wj_placeHolderLabel.hidden = NO;
                } else {
                    strongSelf.wj_placeHolderLabel.hidden = YES;
                }
            }
        }];
        self.wj_observer = observer;
    }
    objc_setAssociatedObject(self, @selector(wj_placeHolder), wj_placeHolder, OBJC_ASSOCIATION_COPY);
    self.wj_placeHolderLabel.text = wj_placeHolder;
}

- (NSString *)wj_placeHolder {
    return objc_getAssociatedObject(self, _cmd);
}

- (void)setWj_placeHolderColor:(UIColor *)wj_placeHolderColor {
    objc_setAssociatedObject(self, @selector(wj_placeHolderColor), wj_placeHolderColor, OBJC_ASSOCIATION_RETAIN);
    self.wj_placeHolderLabel.textColor = wj_placeHolderColor;
}

- (UIColor *)wj_placeHolderColor {
    UIColor * placeholderColor = objc_getAssociatedObject(self, _cmd);
    if (!placeholderColor) {
        placeholderColor = [UIColor lightGrayColor];
    }
    return placeholderColor;
}

- (void)setWj_placeHolderLabel:(UILabel *)wj_placeHolderLabel {
    objc_setAssociatedObject(self, @selector(wj_placeHolderLabel), wj_placeHolderLabel, OBJC_ASSOCIATION_RETAIN);
}

- (UITextView *)wj_placeHolderLabel {
    UITextView * placeHolderLabel = objc_getAssociatedObject(self, _cmd);
    if (!placeHolderLabel) {
        placeHolderLabel = [[UITextView alloc]initWithFrame:self.bounds];
        placeHolderLabel.textContainerInset = self.textContainerInset;
        placeHolderLabel.font = VULPingFangSCMedium(20);
        placeHolderLabel.userInteractionEnabled = NO;
        placeHolderLabel.backgroundColor = [UIColor clearColor];
        placeHolderLabel.textColor = self.wj_placeHolderColor;
        placeHolderLabel.scrollEnabled = NO;
        [self addSubview:placeHolderLabel];
        objc_setAssociatedObject(self, _cmd, placeHolderLabel, OBJC_ASSOCIATION_RETAIN);
    }
    return placeHolderLabel;
}

- (void)setWj_lineSpacing:(CGFloat)wj_lineSpacing {
    objc_setAssociatedObject(self, @selector(wj_lineSpacing), @(wj_lineSpacing), OBJC_ASSOCIATION_ASSIGN);
    
    NSMutableParagraphStyle *paragraphStyle = [NSMutableParagraphStyle new];
    paragraphStyle.lineSpacing = self.wj_lineSpacing;// 字体的行间距
    NSMutableDictionary * attributes = self.typingAttributes.mutableCopy;
    [attributes setValue:paragraphStyle forKey:NSParagraphStyleAttributeName];
    self.typingAttributes = attributes;
    if (self.text.length > 0) {
        self.text = self.text;
    }
}

- (CGFloat)wj_lineSpacing {
    CGFloat line =[objc_getAssociatedObject(self, _cmd) floatValue];
    return line;
}

- (NSString *)layout_key {
    NSString * layout_key = objc_getAssociatedObject(self, _cmd);
    if (!layout_key) {
        layout_key = wj_layout_frame;
        NSArray * sup_layouts = self.superview.constraints;
        for (NSLayoutConstraint * constraint in sup_layouts) {
            if (constraint.firstItem == self || constraint.secondItem == self) {
                NSLog(@"使用了xib");
                layout_key = wj_auto_layout;
                break;
            }else continue;
        }
        objc_setAssociatedObject(self, _cmd, layout_key, OBJC_ASSOCIATION_COPY_NONATOMIC);
    }
    return layout_key;
}

- (void)setLayout_key:(NSString *)layout_key {
    objc_setAssociatedObject(self, @selector(layout_key), layout_key, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (void)setHeightConstraint:(NSLayoutConstraint *)heightConstraint {
    objc_setAssociatedObject(self, @selector(heightConstraint), heightConstraint, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (NSLayoutConstraint *)heightConstraint {
    NSLayoutConstraint * constraint = objc_getAssociatedObject(self, _cmd);
    if (!constraint) {
        NSArray * constraints = self.constraints;
        for (NSLayoutConstraint * item in constraints) {
            if (item.firstAttribute == NSLayoutAttributeHeight) {
                constraint = item;
                objc_setAssociatedObject(self, @selector(heightConstraint), constraint, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
                break;
            }
        }
    }
    return constraint;
}

- (void)setTextViewHeightDidChangedHandle:(void (^)(CGFloat))textViewHeightDidChangedHandle {
    objc_setAssociatedObject(self, @selector(textViewHeightDidChangedHandle), textViewHeightDidChangedHandle, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (void(^)(CGFloat textViewHeight))textViewHeightDidChangedHandle {
    return objc_getAssociatedObject(self, _cmd);
}


- (void)layoutSubviews
{
    [super layoutSubviews];
    [self.superview layoutSubviews];
    self.scrollIndicatorInsets = UIEdgeInsetsZero;
    self.scrollEnabled = YES;
//    if (self.placeholder_font.ascender <= 0) {
//        UILabel *lb = (UILabel *)[self valueForKey:@"_placeholderLabel"];
//        lb.font = self.font;
//        [lb sizeToFit];
//    }
    if (self.wj_minHeight <= 0) {
        self.wj_minHeight = self.frame.size.height;
    }
    //    CGRect textFrame = [self.text boundingRectWithSize:CGSizeMake(self.frame.size.width-10,MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin attributes:[NSDictionary dictionaryWithObjectsAndKeys:self.font,NSFontAttributeName, nil] context:nil];
    NSInteger height = ceilf([self sizeThatFits:CGSizeMake(self.bounds.size.width, MAXFLOAT)].height);
    
    if (self.frame.size.height > self.wj_minHeight && self.isAutoHeightEnable && height < self.wj_minHeight) {
        self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, self.frame.size.width, self.wj_minHeight);
        if (self.textViewHeightDidChangedHandle) {
            self.textViewHeightDidChangedHandle(self.wj_minHeight);
        }
        return;
    }
    
    if (height > self.frame.size.height &&  self.wj_maxHeight > height && self.isAutoHeightEnable) {
        //0.1 防止换行 时高度变化导致顶部文字位置移动
        self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, self.frame.size.width, height);
        if (self.textViewHeightDidChangedHandle) {
            self.textViewHeightDidChangedHandle(height);
        }
        return;
    }
    
    if (height < self.frame.size.height && self.wj_minHeight < height&& self.isAutoHeightEnable) {
        self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, self.frame.size.width, height);
        if (self.textViewHeightDidChangedHandle) {
            self.textViewHeightDidChangedHandle(height);
        }
        return;
    }

//    if (self.wj_maxHeight != 0) {
//        return;
//    }
//
//    if (height > self.frame.size.height && self.isAutoHeightEnable) {
//        self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, self.frame.size.width, height + 0.1);
//        return;
//    }
    
}










@end
