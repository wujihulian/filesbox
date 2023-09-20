//
//  FBSearchView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/18.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBSearchView : UIView
@property (nonatomic,copy) void (^ clickViewBlock)(NSDictionary *dic);
@property (nonatomic,copy) void (^ dismiss)();

-(void)show;
-(void)hide;
@end

NS_ASSUME_NONNULL_END
