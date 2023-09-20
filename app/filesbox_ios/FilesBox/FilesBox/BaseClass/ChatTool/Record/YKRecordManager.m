//
//  YKRecordManager.m
//  VideoULimit
//
//  Created by yuekewei on 2020/3/17.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "YKRecordManager.h"
#import "ConvertAudioFile.h"

#define kChildPath @"Chat/Recoder"
#define kAmrType @"amr"

#define ETRECORD_RATE 11025.0

#define kMinRecordDuration 1.0

@interface YKRecordManager ()
{
    NSDate *_startDate;
    NSDate *_endDate;
    void (^recordFinish)(NSString *recordPath);
}

@property (nonatomic, strong) NSDictionary *recordSetting;

@property (nonatomic,strong) NSString *mp3Path;
@property (nonatomic,strong) NSString *cafPath;
@end

@implementation YKRecordManager

+ (id)shareManager {
    static id _instance ;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _instance = [[self alloc] init];
    });
    return _instance;
}


#pragma mark - 录音
/// 判断权限
- (void)requestRecordPermission:(void (^)(BOOL granted))handler {
    AVAuthorizationStatus videoAuthStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeAudio];
    if (videoAuthStatus == AVAuthorizationStatusNotDetermined) {
        // 未询问用户是否授权
        //询问用户是否进行授权
        [[AVAudioSession sharedInstance] requestRecordPermission:^(BOOL granted) {
            if (granted) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    handler(NO);
                });
            }
            else {
                [self showAlert];
                 handler(NO);
            }
        }];
    }
    else if(videoAuthStatus == AVAuthorizationStatusRestricted || videoAuthStatus == AVAuthorizationStatusDenied) {
        // 未授权
        [self showAlert];
         handler(NO);
    }
    else{
         handler(YES);
    }
}

- (void)showAlert {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"无法录音" message:@"请在iPhone的“设置-隐私-麦克风”选项中，允许空中学堂访问你的手机麦克风。" preferredStyle:UIAlertControllerStyleAlert];
    [alert addAction:[UIAlertAction actionWithTitle:@"设置" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];
    }]];
    [alert addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil]];
    
    UIViewController * top = [self topMostController];
    [top presentViewController:alert animated:YES completion:nil];
}

- (UIViewController *) topMostController {
  UIViewController*topController = [UIApplication sharedApplication].keyWindow.rootViewController;
  while(topController.presentedViewController){
    topController=topController.presentedViewController;
  }
  return topController;
}

/// 开始录音
/// @param fileName 文件名
/// @param completion 回调
- (void)startRecordingWithFileName:(NSString *)fileName completion:(void(^)(NSError *error))completion {
    [self requestRecordPermission:^(BOOL granted) {
        NSError *error = nil;
        if (granted) {
            if ([self->_recorder isRecording]) {
                [self->_recorder stop];
                [self cancelCurrentRecording];
                return;
            } else {
                NSString *wavFilePath = [self recorderPathWithFileName:fileName];
                NSURL *wavUrl = [[NSURL alloc] initFileURLWithPath:wavFilePath];
                // 在实例化AVAudioRecorder之前，先开启会话,否则真机上录制失败
                AVAudioSession *session = [AVAudioSession sharedInstance];
                NSError *setCategoryError = nil;
                [session setCategory:AVAudioSessionCategoryPlayAndRecord error:&setCategoryError];
                if(setCategoryError){
                    NSLog(@"%@", [setCategoryError description]);
                }
                
                self->_recorder = [[AVAudioRecorder alloc] initWithURL:wavUrl settings:self.recordSetting error:&error];
                self->_recorder.meteringEnabled = YES;
                if (!self->_recorder || error) {
                    self->_recorder = nil;
                    if (completion) {
                        error = [NSError errorWithDomain:NSLocalizedString(@"error.initRecorderFail", @"Failed to initialize AVAudioRecorder") code:123 userInfo:nil];
                        completion(error);
                    }
                    return;
                }
                self->_startDate = [NSDate date];
                self->_recorder.meteringEnabled = YES;
                self->_recorder.delegate = self;
                [self->_recorder record];
                if (completion) {
                    completion(error);
                }
            }
        }
        else {
            if (completion) {
                error = [NSError errorWithDomain:NSLocalizedString(@"error", @"没权限") code:122 userInfo:nil];
                completion(error);
            }
        }
    }];
}

/// 停止录音
/// @param completion 回调
- (void)stopRecordingWithCompletion:(void(^)(NSString *recordPath))completion {
    _endDate = [NSDate date];
    if ([_recorder isRecording]) {
        if ([_endDate timeIntervalSinceDate:_startDate] < [self recordMinDuration]) {
            // 录制时间太短
            if (completion) {
                completion(shortRecord);
            }
            [self.recorder stop];
            [self cancelCurrentRecording];
            sleep(1.0);//a temporary method，let it sheep a minute,because recorder generated need time，to prevented clicked quickly situation
            NSLog(@"record time duration is too short");
            return;
        }
        recordFinish = completion;
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            [self.recorder stop];
            NSLog(@"record time duration :%f",[self->_endDate timeIntervalSinceDate:self->_startDate]);
        });
    }
}


/// 取消当前录音
- (void)cancelCurrentRecording {
    _recorder.delegate = nil;
    if (_recorder.recording) {
        [_recorder stop];
    }
    _recorder = nil;
    recordFinish = nil;
}

- (NSTimeInterval)recordMinDuration {
    return kMinRecordDuration;
}

// 移除音频
- (void)removeCurrentRecordFile:(NSString *)fileName {
    [self cancelCurrentRecording];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *path = [self recorderPathWithFileName:fileName];
    BOOL isDirExist = [fileManager fileExistsAtPath:path];
    if (isDirExist) {
        [fileManager removeItemAtPath:path error:nil];
    }
}

#pragma mark  AVAudioRecorderDelegate 录音回调
#pragma mark 录音完成
- (void)audioRecorderDidFinishRecording:(AVAudioRecorder *)recorder
                           successfully:(BOOL)flag {
    NSString *recordPath = [[_recorder url] path];
    // 音频转换
    NSString *mp3Path = [[recordPath stringByDeletingPathExtension] stringByAppendingPathExtension:@"mp3"];
    
    self.mp3Path = [recordPath stringByReplacingOccurrencesOfString:@".caf" withString:@".mp3"];
    [ConvertAudioFile conventToMp3WithCafFilePath:recordPath
                                                      mp3FilePath:self.mp3Path
                                                       sampleRate:ETRECORD_RATE
                                                         callback:^(BOOL result) {
        if (result) {
            NSLog(@"mp3 file compression sucesss");
            
            if (self->recordFinish) {
                self->recordFinish(mp3Path);
                self->_recorder = nil;
                self->recordFinish = nil;
            }
        }
    }];
}

- (void)audioRecorderEncodeErrorDidOccur:(AVAudioRecorder *)recorder error:(NSError *)error {
    NSLog(@"audioRecorderEncodeErrorDidOccur");
}

#pragma mark - Player 播放录音
- (void)startPlayRecorder:(NSString *)recorderPath {
    AVAudioSession *audioSession = [AVAudioSession sharedInstance];
    NSError *err = nil;  // 加上这两句，否则声音会很小
    [audioSession setCategory :AVAudioSessionCategoryPlayback error:&err];
    self.player = [[AVAudioPlayer alloc] initWithContentsOfURL:[NSURL fileURLWithPath:recorderPath] error:nil];
    self.player.numberOfLoops = 0;
    [self.player prepareToPlay];
    self.player.delegate = self;
    [self.player play];
}

- (void)stopPlayRecorder {
    [self.player stop];
    self.player = nil;
    self.player.delegate = nil;
}

- (void)pause {
    [self.player pause];
}

#pragma mark   AVAudioPlayerDelegate 播放回调
- (void)audioPlayerDidFinishPlaying:(AVAudioPlayer *)player
                       successfully:(BOOL)flag {
    [self.player stop];
    self.player = nil;
    if (self.playDelegate && [self.playDelegate respondsToSelector:@selector(voiceDidPlayFinished)]) {
        [self.playDelegate voiceDidPlayFinished];
    }
}


#pragma mark - 路径
- (NSString *)recorderMainPath {
    return [self createPathWithChildPath:kChildPath];
}

- (NSString *)recorderPathWithFileName:(NSString *)fileName {
    NSString *path = [self recorderMainPath];
    return [path stringByAppendingPathComponent:[NSString stringWithFormat:@"%@%@",fileName,@".caf"]];
}

- (NSString *)cacheDirectory {
    return [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) lastObject];
}

- (NSString *)createPathWithChildPath:(NSString *)childPath {
    NSString *path = [[self cacheDirectory] stringByAppendingPathComponent:childPath];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    BOOL isDirExist = [fileManager fileExistsAtPath:path];
    if (!isDirExist) {
        BOOL isCreatDir = [fileManager createDirectoryAtPath:path withIntermediateDirectories:YES attributes:nil error:nil];
        if (!isCreatDir) {
            NSLog(@"create folder failed");
            return nil;
        }
    }
    return path;
}

#pragma mark - Private
// 接收到的语音保存路径(文件以fileKey为名字)
- (NSString *)receiveVoicePathWithFileKey:(NSString *)fileKey {
    return [self recorderPathWithFileName:fileKey];
}

// 获取语音时长
- (NSUInteger)durationWithVideo:(NSURL *)voiceUrl{
    NSDictionary *opts = [NSDictionary dictionaryWithObject:@(NO) forKey:AVURLAssetPreferPreciseDurationAndTimingKey];
    AVURLAsset *urlAsset = [AVURLAsset URLAssetWithURL:voiceUrl options:opts]; // 初始化视频媒体文件
    NSUInteger second = 0;
    second = urlAsset.duration.value / urlAsset.duration.timescale; // 获取视频总时长,单位秒
    return second;
}


/// 创建录音文件名
+ (NSString *)creatRecordFileName {
    NSTimeInterval timeInterval = [[NSDate date] timeIntervalSince1970];
    NSString *fileName = [NSString stringWithFormat:@"%ld",(long)timeInterval];
    return fileName;
}

#pragma mark - Getter
/// 录音设置
- (NSDictionary *)recordSetting {
    if (!_recordSetting) {
        NSMutableDictionary *dicM = [NSMutableDictionary dictionary];
        [dicM setObject:@(kAudioFormatLinearPCM) forKey:AVFormatIDKey];
        [dicM setObject:@(ETRECORD_RATE) forKey:AVSampleRateKey];
        [dicM setObject:@(2) forKey:AVNumberOfChannelsKey];
        [dicM setObject:@(16) forKey:AVLinearPCMBitDepthKey];
        [dicM setObject:[NSNumber numberWithInt:AVAudioQualityMin] forKey:AVEncoderAudioQualityKey];
        _recordSetting = dicM;
    }
    return _recordSetting;
}
@end
