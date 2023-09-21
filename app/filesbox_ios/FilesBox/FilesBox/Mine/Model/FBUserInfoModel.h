//
//  FBUserInfoModel.h
//  FilesBox
//
//  Created by 无极互联 on 2023/7/17.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBUserInfoModel : NSObject
@property (nonatomic, copy)  NSString *title;
@property (nonatomic, copy)  NSString *detail;
@property (nonatomic, assign)  BOOL isBtn;
@property (nonatomic, assign)  BOOL rightBtn;
@property (nonatomic, assign)  BOOL imageShow;
+(NSArray * )getArrayWithModel:(VULSaveUserInformation *)model;
+ (NSString *)formattedFileSize:(long long)bytes;
@end

NS_ASSUME_NONNULL_END
