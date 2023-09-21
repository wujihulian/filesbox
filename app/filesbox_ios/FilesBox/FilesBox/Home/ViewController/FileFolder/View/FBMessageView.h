//
//  FBMessageView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/18.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBMessageView : UIView
@property (nonatomic,retain) UIImage *iconName;
@property (nonatomic,copy) NSString *messageCount;
@property (nonatomic, strong) UIImageView *iconImage;
@property (nonatomic, strong) UILabel *messageLabel;
@end

NS_ASSUME_NONNULL_END
