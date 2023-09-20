//
//  NSMutableArray+ErrorHandle.h
//  VideoULimit
//
//  Created by svnlan on 2019/10/10.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSMutableArray (ErrorHandle)
/**
 数组中插入数据

 @param object 数据
 @param index 下标
 */
- (void)insertObjectVerify:(id)object atIndex:(NSInteger)index;
/**
 数组中添加数据

 @param object 数据
 */
- (void)addObjectVerify:(id)object;

@end
