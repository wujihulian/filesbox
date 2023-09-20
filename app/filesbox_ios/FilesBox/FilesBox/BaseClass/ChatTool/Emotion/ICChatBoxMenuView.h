//
//  ICChatBoxMenuView.h
//  
//
//

#import <UIKit/UIKit.h>

typedef enum {
    ICEmotionMenuButtonTypeEmoji = 100,
    ICEmotionMenuButtonTypeCuston,
    ICEmotionMenuButtonTypeGif
    
} ICEmotionMenuButtonType;

@class ICChatBoxMenuView;

@protocol ICChatBoxMenuDelegate <NSObject>

@optional
- (void)emotionMenu:(ICChatBoxMenuView *)menu
    didSelectButton:(ICEmotionMenuButtonType)buttonType;

@end

@interface ICChatBoxMenuView : UIView

@property (nonatomic, weak)id <ICChatBoxMenuDelegate>delegate;

@end
