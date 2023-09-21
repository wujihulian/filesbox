//
//  FBAddFileView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/24.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN
@interface FBAddFileCell : UICollectionViewCell
@property (nonatomic,strong) VULLabel *label;
@property (nonatomic,strong) UIImageView *iconImageV;
@end
@interface FBAddFileView : UIView
@property (nonatomic,strong) VULFileObjectModel  *parentModel;
@property (nonatomic,copy) void (^ dismisssView)(void);
@property (nonatomic,copy) void (^ addFileWithTitle)(NSString *title);

@end

NS_ASSUME_NONNULL_END
