//
//  ICFaceManager.h


#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "XZEmotion.h"

@interface ICFaceManager : NSObject

+ (NSDictionary *)emotionsDic;

+ (NSArray *)emotions;

+ (NSArray *)emojiEmotion;

+ (NSArray *)customEmotion;

+ (NSArray *)gifEmotion;

@end
