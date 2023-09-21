//
//  FBChangeLanguage.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/14.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBChangeLanguage : UIView
@property(nonatomic,copy) void (^changeLanguageWithIndex)(NSInteger index);
@end

NS_ASSUME_NONNULL_END
