//
//  VULGIFImage.h
//  VideoULimit
//
//  Created by svnlan on 2019/11/13.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULGIFImage : UIImage

/**
 A C array containing the frame durations.

 The number of frames is defined by the count of the `images` array property.
 */
@property (nonatomic, readonly) NSTimeInterval *frameDurations;

/**
 Total duration of the animated image.
 */
@property (nonatomic, readonly) NSTimeInterval totalDuration;

/**
 Number of loops the image can do before it stops
 */
@property (nonatomic, readonly) NSUInteger loopCount;

- (UIImage *)getFrameWithIndex:(NSUInteger)idx;

@end

NS_ASSUME_NONNULL_END
