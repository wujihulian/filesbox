//
//  YKRecordTool.h
//  VideoULimit
//
//  Created by yuekewei on 2020/3/17.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YKRecordManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface YKVoiceHud : UIImageView

@property (nonatomic, assign) CGFloat progress;
@end



@interface YKRecordTool : NSObject

/** 录音文件名 */
@property (nonatomic, copy) NSString *recordName;


- (void)startRecordingWithHUDShowView:(UIView *)showView;

- (void)completionRecording:(void(^)(NSString *recordPath)) recordComplete;

- (void)cancelRecording;

// 手势向外或向里移动
- (void)voiceWillDragout:(BOOL)inside;
@end

NS_ASSUME_NONNULL_END
