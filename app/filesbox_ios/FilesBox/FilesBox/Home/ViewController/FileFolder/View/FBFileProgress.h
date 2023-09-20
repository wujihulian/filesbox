//
//  FBFileProgress.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/18.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBFileProgress : UIView
@property (nonatomic,copy) NSString *title;
-(void)changeProgressWith:(float)per;
@end

NS_ASSUME_NONNULL_END
