//
//  UIView+TapedBlock.m
//  Tairong
//
//  Created by yuekewei on 2019/8/5.
//

#import "UIView+TapedBlock.h"
#import <objc/runtime.h>

@interface UIView (TappedBlock_Private)

- (void)runBlockForKey:(void *)blockKey;
- (void)setBlock:(TapedBlock)block forKey:(void *)blockKey;

- (UITapGestureRecognizer*)addTapGestureRecognizerWithTaps:(NSUInteger) taps touches:(NSUInteger) touches selector:(SEL) selector;
- (void) addRequirementToSingleTapsRecognizer:(UIGestureRecognizer*) recognizer;
- (void) addRequiredToDoubleTapsRecognizer:(UIGestureRecognizer*) recognizer;

@end

@implementation UIView (TapedBlock)

static char tappedBlockKey;
static char doubleTappedBlockKey;
static char twoFingerTappedBlockKey;
static char touchedDownBlockKey;
static char touchedCancelBlockKey;
static char touchedUpBlockKey;

#pragma mark -
#pragma mark Set blocks

- (void)runBlockForKey:(void *)blockKey {
    TapedBlock block = objc_getAssociatedObject(self, blockKey);
    if (block) block();
}

- (void)setBlock:(TapedBlock)block forKey:(void *)blockKey {
    self.userInteractionEnabled = YES;
    objc_setAssociatedObject(self, blockKey, block, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

#pragma mark -
#pragma mark When Tapped

- (void)tapped:(TapedBlock)block {
    UITapGestureRecognizer* gesture = [self addTapGestureRecognizerWithTaps:1 touches:1 selector:@selector(viewWasTapped)];
    [self addRequiredToDoubleTapsRecognizer:gesture];
    
    [self setBlock:block forKey:&tappedBlockKey];
}

- (void)doubleTapped:(TapedBlock)block {
    UITapGestureRecognizer* gesture = [self addTapGestureRecognizerWithTaps:2 touches:1 selector:@selector(viewWasDoubleTapped)];
    [self addRequirementToSingleTapsRecognizer:gesture];
    
    [self setBlock:block forKey:&doubleTappedBlockKey];
}

- (void)twoFingerTapped:(TapedBlock)block {
    [self addTapGestureRecognizerWithTaps:1 touches:2 selector:@selector(viewWasTwoFingerTapped)];
    
    [self setBlock:block forKey:&twoFingerTappedBlockKey];
}

- (void)touchedDown:(TapedBlock)block {
    [self setBlock:block forKey:&touchedDownBlockKey];
}

- (void)touchedCancel:(TapedBlock)block {
    [self setBlock:block forKey:&touchedCancelBlockKey];
}

- (void)touchedUp:(TapedBlock)block {
    [self setBlock:block forKey:&touchedUpBlockKey];
}

#pragma mark -
#pragma mark Callbacks

- (void)viewWasTapped {
    [self runBlockForKey:&tappedBlockKey];
}

- (void)viewWasDoubleTapped {
    [self runBlockForKey:&doubleTappedBlockKey];
}

- (void)viewWasTwoFingerTapped {
    [self runBlockForKey:&twoFingerTappedBlockKey];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    [super touchesBegan:touches withEvent:event];
    [self runBlockForKey:&touchedDownBlockKey];
}

- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event {
    [super touchesCancelled:touches withEvent:event];
    [self runBlockForKey:&touchedCancelBlockKey];
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
    [super touchesEnded:touches withEvent:event];
    [self runBlockForKey:&touchedUpBlockKey];
}

#pragma mark -
#pragma mark Helpers

- (UITapGestureRecognizer*)addTapGestureRecognizerWithTaps:(NSUInteger)taps touches:(NSUInteger)touches selector:(SEL)selector {
    UITapGestureRecognizer* tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:selector];
    tapGesture.delegate = self;
    tapGesture.numberOfTapsRequired = taps;
    tapGesture.numberOfTouchesRequired = touches;
    [self addGestureRecognizer:tapGesture];
    
    return tapGesture;
}

- (void) addRequirementToSingleTapsRecognizer:(UIGestureRecognizer*) recognizer {
    for (UIGestureRecognizer* gesture in [self gestureRecognizers]) {
        if ([gesture isKindOfClass:[UITapGestureRecognizer class]]) {
            UITapGestureRecognizer* tapGesture = (UITapGestureRecognizer*) gesture;
            if (tapGesture.numberOfTouchesRequired == 1 && tapGesture.numberOfTapsRequired == 1) {
                [tapGesture requireGestureRecognizerToFail:recognizer];
            }
        }
    }
}

- (void) addRequiredToDoubleTapsRecognizer:(UIGestureRecognizer*) recognizer {
    for (UIGestureRecognizer* gesture in [self gestureRecognizers]) {
        if ([gesture isKindOfClass:[UITapGestureRecognizer class]]) {
            UITapGestureRecognizer* tapGesture = (UITapGestureRecognizer*) gesture;
            if (tapGesture.numberOfTouchesRequired == 2 && tapGesture.numberOfTapsRequired == 1) {
                [recognizer requireGestureRecognizerToFail:tapGesture];
            }
        }
    }
}

- (void)touchedBgColorWithDownColor:(UIColor *)downColor  upColor:(UIColor *)upColor cancelColor:(UIColor *)cancelColor  {
    __weak typeof(self)weakSelf = self;
    [self touchedDown:^{
        weakSelf.backgroundColor = downColor;
    }];
    [self touchedUp:^{
        weakSelf.backgroundColor = upColor;
    }];
    [self touchedCancel:^{
        weakSelf.backgroundColor = cancelColor;
    }];
}
@end
