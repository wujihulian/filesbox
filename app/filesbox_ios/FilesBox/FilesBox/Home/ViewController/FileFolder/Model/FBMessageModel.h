//
//  FBMessageModel.h
//  FilesBox
//
//  Created by 无极互联 on 2023/6/13.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBMessageModel : NSObject
@property (nonatomic, copy) NSString *id;
@property (nonatomic, assign) BOOL isRead;
@property (nonatomic, copy) NSString *sendTime;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *createTime;

@end

NS_ASSUME_NONNULL_END
