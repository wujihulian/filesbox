//
//  FBNodeModel.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/21.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBNodeModel : NSObject
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *fileId;
@property (nonatomic, assign) NSInteger number;


@end

NS_ASSUME_NONNULL_END
