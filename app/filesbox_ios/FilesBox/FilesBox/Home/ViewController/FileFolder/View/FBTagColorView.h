//
//  FBTagColorView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/22.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBTagColorView : UIView
@property (nonatomic,strong) NSArray *tagColorList;
@property (nonatomic, copy) void (^ selectTagColorWithString)(NSString *rgbString);

@end

NS_ASSUME_NONNULL_END
