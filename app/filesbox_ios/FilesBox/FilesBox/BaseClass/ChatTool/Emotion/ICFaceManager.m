//
//  ICFaceManager.m


#import "ICFaceManager.h"

@interface VULEmotionTextAttachment : YYTextAttachment

@property (nonatomic, strong) NSURL *imageURL;
@property (nonatomic, strong) YYAnimatedImageView *imageView;
@property (nonatomic, assign) CGSize size;
@end

@implementation VULEmotionTextAttachment

- (void)setContent:(id)content {
    _imageView = content;
}

- (id)content {
    if (pthread_main_np() == 0) return nil;
    if (_imageView) return _imageView;
    
    _imageView = [YYAnimatedImageView new];
    _imageView.size = _size;
    [_imageView setImageWithURL:_imageURL placeholder:nil];
    return _imageView;
}

@end

#define ICBundle [NSBundle mainBundle]

@implementation ICFaceManager

static NSArray * _emojiEmotions,*_custumEmotions,*gifEmotions;

+ (NSDictionary *)emotionsDic {
    static NSDictionary *emotionsDic;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        NSArray *emotions = [ICFaceManager emotions];
        NSMutableDictionary *dic = [NSMutableDictionary new];
        for (XZEmotion *emotion in emotions) {
            [dic setObject:emotion.face_id ?: @(0) forKey:emotion.face_name];
        }
        emotionsDic = [NSDictionary dictionaryWithDictionary:dic];
    });
    return emotionsDic;
}

+ (NSArray *)emotions {
    static NSArray *emotions;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{

//       NSArray *path = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
//       NSString *documentsPath = [path objectAtIndex:0];
//       NSString *plistPath = [documentsPath stringByAppendingPathComponent:@"Emotion.plist"];
//       
//       NSArray *array = [[NSBundle bundleWithPath:[[NSBundle mainBundle] pathForResource:@"Emotion" ofType:@"bundle"]] pathsForResourcesOfType:@"png" inDirectory:nil];
//       
//       NSMutableArray *emotionsArray = [NSMutableArray new];
//       for (NSInteger i = 0; i < array.count; i ++) {
//           NSString *path = [array objectAtIndex:i];
//           [emotionsArray addObject: @{@"face_name" :  [NSString stringWithFormat:@"[%@]",[[path componentsSeparatedByString:@"/"].lastObject componentsSeparatedByString:@"."].firstObject]}];
//       }
//       [emotionsArray writeToFile:plistPath atomically:YES];
         
         NSArray *plistArray = [NSArray arrayWithContentsOfURL:[NSURL fileURLWithPath:[[NSBundle mainBundle] pathForResource:@"Emotion.plist" ofType:nil]]];
        emotions =  [NSArray modelArrayWithClass:[XZEmotion class] json:plistArray];
    });
    return emotions;
}

+ (NSArray *)emojiEmotion {
    if (_emojiEmotions) {
        return _emojiEmotions;
    }
    NSString *path  = [ICBundle pathForResource:@"emoji.plist" ofType:nil];
    NSArray *array = [NSArray arrayWithContentsOfFile:path];
    
    NSMutableArray *emotionArray = [NSMutableArray new];
    for (NSDictionary *dic in array) {
        XZEmotion *emotion = [XZEmotion new];
        emotion.face_unicode = [dic objectForKey:@"code"] ?: @"";
        [emotionArray addObject:emotion];
    }
    _emojiEmotions  = emotionArray;
    return _emojiEmotions;
}

+ (NSArray *)customEmotion {
    if (_custumEmotions) {
        return _custumEmotions;
    }
    NSString *path  = [ICBundle pathForResource:@"normal_face.plist" ofType:nil];
    NSArray *array = [NSArray arrayWithContentsOfFile:path];
    
    NSMutableArray *emotionArray = [NSMutableArray new];
    for (NSDictionary *dic in array) {
        XZEmotion *emotion = [XZEmotion new];
        emotion.face_name = [dic objectForKey:@"face_name"] ?: @"";
        emotion.face_id = [dic objectForKey:@"face_id"] ?: @"";
        [emotionArray addObject:emotion];
    }
    _custumEmotions = emotionArray;
    return _custumEmotions;
}

+ (NSArray *)gifEmotion {
    return nil;
}

@end
