//
//  FBHistoryLinkView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/15.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBHistoryLinkView : UIView
@property(nonatomic,copy) void (^chooseNowUrl)(NSString *str);

@end

NS_ASSUME_NONNULL_END
