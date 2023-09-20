//
//  ShareViewController.m
//  ImageUploadFilesBoxs
//
//  Created by 无极互联 on 2023/4/24.
//

#import "ShareViewController.h"

@interface ShareViewController ()

@end

@implementation ShareViewController
- (void)viewDidLoad {
    [super viewDidLoad];

    // 移除输入框
    self.placeholder = @"";
    self.textView.text = @"";
    self.textView.editable = NO;
    [self didSelectPost];
}
- (BOOL)isContentValid {
    // Do validation of contentText and/or NSExtensionContext attachments here
    return YES;
}

- (void)didSelectPost {
    // This is called after the user selects Post. Do the upload of contentText
    dispatch_group_t group = dispatch_group_create();

        NSExtensionItem *extensionItem = self.extensionContext.inputItems[0];
        
        for (NSItemProvider *attachment in extensionItem.attachments) {
            if ([attachment hasItemConformingToTypeIdentifier:@"public.image"]) {
                dispatch_group_enter(group);

                dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
                    [attachment loadItemForTypeIdentifier:@"public.image" options:nil completionHandler:^(UIImage *image, NSError *error) {
                                if (!error) {
                                    // 获取图片数据
                                    NSData *imageData = UIImagePNGRepresentation(image);

                                    // 将图片数据传递给应用
                                    NSUserDefaults *userDefaults = [[NSUserDefaults alloc] initWithSuiteName:@"group.com.wuji.share.filesbox"];
                                    [userDefaults setObject:imageData forKey:@"sharedImageData"];
                                    [userDefaults setObject:@"jpg" forKey:@"sharedImage"];

                                    [userDefaults synchronize];
                                } else {
                                    NSLog(@"Error: %@", error.localizedDescription);
                                }
                        

                        dispatch_group_leave(group);

                        
                    }];
                });
                
                 }
            if ([attachment hasItemConformingToTypeIdentifier:@"public.movie"]) {
                   [attachment loadItemForTypeIdentifier:@"public.movie" options:nil completionHandler:^(NSURL *videoUrl, NSError *error) {
                       if (videoUrl) {
                           // 保存文件到共享容器
                        NSString *filename = [NSString stringWithFormat:@"%@.mp4", [[NSUUID UUID] UUIDString]];
                           NSURL   *sharedContainerURL = [[NSFileManager defaultManager] containerURLForSecurityApplicationGroupIdentifier:@"group.com.wuji.share.filesbox"];
                           NSFileManager *fileManager = [[NSFileManager alloc] init];
                        NSURL *destinationURL = [sharedContainerURL URLByAppendingPathComponent:filename];
                            NSError *saveError;
                        if ([fileManager copyItemAtURL:videoUrl toURL:destinationURL error:&saveError]) {
                            NSLog(@"视频文件保存成功：%@", destinationURL);
                            // 将图片数据传递给应用
                            NSUserDefaults *userDefaults = [[NSUserDefaults alloc] initWithSuiteName:@"group.com.wuji.share.filesbox"];
                            [userDefaults setObject:filename forKey:@"sharedUrl"];
                            [userDefaults setObject:@"mp4" forKey:@"sharedImage"];

                            [userDefaults synchronize];
                            dispatch_group_leave(group);

                                    } else {
                                                 NSLog(@"保存视频文件失败：%@", saveError);
                                        dispatch_group_leave(group);

                                             }

                         
                       } else {
                           NSLog(@"获取视频数据失败: %@", error.localizedDescription);
                           dispatch_group_leave(group);

                       }
                

                   }];
               }
          
        }
    dispatch_group_notify(group, dispatch_get_main_queue(), ^{
             UIResponder* responder =self;
                
             responder = [responder nextResponder];
                
             while((responder = [responder nextResponder]) !=nil) {
                    
                 if([responder respondsToSelector:@selector(openURL:)] ==YES) {
                        
                     //打开APP
                        
                     //这里的asancloud是app的URL Schemes  ，home是自己随便定义的，用于判断
                        
                     [responder performSelector:@selector(openURL:) withObject:[NSURL URLWithString:[NSString stringWithFormat:@"FilesBox://share"]]];
                        
                     //执行分享内容处理
                        
                     [self.extensionContext completeRequestReturningItems:@[] completionHandler:NULL];
                }
            }
        });
    
}

            

- (NSArray *)configurationItems {
    // To add configuration options via table cells at the bottom of the sheet, return an array of SLComposeSheetConfigurationItem here.
    return @[];
}

@end
