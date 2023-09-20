//
//  VULCleanCache.h
//  VideoULimit
//
//  Created by svnlan on 2018/10/12.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void(^cleanCacheBlock)(void);

@interface VULCleanCache : NSObject
/**
 清理缓存
 
 @param block 清理缓存回掉
 */
+ (void)cleanCache:(cleanCacheBlock)block;

/**
 整个缓存目录的大小
 
 @return 整个缓存目录的大小
 */
+ (float)folderSizeAtPath;


@end
