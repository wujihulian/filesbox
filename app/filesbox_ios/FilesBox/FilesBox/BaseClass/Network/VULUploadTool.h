//
//  VULUploadTool.h
//  VideoULTeacher
//
//  Created by yuekewei on 2020/7/9.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@class VULUploadModel;

typedef void(^VULUploadCompletion)(__kindof VULUploadModel * _Nonnull uploadModel);

@interface VULUploadTool : NSObject

@end

@interface VULUploadRequest : NSObject

@end

@interface VULUploadModel : NSObject

@property (nonatomic, copy) NSString *message;
@property (nonatomic, copy) NSString *code;
@property (nonatomic, strong) id data;
@property (nonatomic, assign) BOOL success;
@end

NS_ASSUME_NONNULL_END
