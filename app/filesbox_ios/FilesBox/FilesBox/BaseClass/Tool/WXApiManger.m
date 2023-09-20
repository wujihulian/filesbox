//
//  WXApiManger.m
//  VideoULimit
//
//  Created by yuekewei on 2020/4/29.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "WXApiManger.h"
#import <DTShareKit/DTOpenKit.h>
#import "WeiboSDK.h"

@interface WXApiManger ()
@property (nonatomic, retain) VULFileObjectModel *shareModel ;

@end

@implementation WXApiManger

+ (instancetype)shareInstance {
    static WXApiManger *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[WXApiManger alloc] init];
    });
    return instance;
}

- (void)oauth_authorization {
    //[WXApi registerApp:WechatAppID
      //   universalLink:WeChatUniversalLink];
    SendAuthReq *req = [[SendAuthReq alloc] init];
    req.state = @"wx_oauth_authorization_state";
    req.scope = @"snsapi_userinfo";
    [WXApi sendReq:req completion:^(BOOL success) {
        if (!success) {
            
        }
    }];
    
    
}
-(void)shareMinInfo:(NSString *)path webpageUrl:(id)webpageUrl hdImageData:(id) hdImageData  title:(NSString *)title des:(NSString *)des{
 
    WXMiniProgramObject *object = [WXMiniProgramObject object];
//    到时会要换
    if ([kBaseServiceUrl isEqualToString:@"https://test.1x.cn"]) {
        object.userName = @"gh_9703393594ca";
        object.miniProgramType = WXMiniProgramTypePreview; //拉起小程序的类型

    }else{
        object.userName = @"gh_9703393594ca";
        object.miniProgramType = WXMiniProgramTypeRelease; //拉起小程序的类型
    }
    object.webpageUrl = webpageUrl;
    object.path = path;
    UIImage *image = [UIImage imageWithData:hdImageData];
    object.hdImageData = UIImageJPEGRepresentation(image, 0.2);
    object.withShareTicket = YES;
    
    WXMediaMessage *message = [WXMediaMessage message];
    message.title = title;
    
    message.description = des;
    message.thumbData = nil;  //兼容旧版本节点的图片，小于32KB，新版本优先
                              //使用WXMiniProgramObject的hdImageData属性
    message.mediaObject = object;
    SendMessageToWXReq *req = [[SendMessageToWXReq alloc] init];
    req.bText = NO;
    req.message = message;
    req.scene = WXSceneSession;  //目前只支持会话
    [WXApi sendReq:req completion:^(BOOL success) {
        
    }];
    

}
-(void)shareFileWithModel:(VULFileObjectModel *)model{
    if ([WXApi isWXAppInstalled] && [WXApi isWXAppSupportApi]) {
        _shareModel = model;
        NSArray *videoArray = @[@"mp4",@"rm",@"rmvb",@"3gp",@"mov",@"m4v",@"wmv",@"asf",@"asx",@"avi",@"dat",@"mkv",@"flv",@"vob",@"webm",@"mpg"];

        NSArray *picArray = @[@"bmp",@"jpg",@"jpeg",@"png",@"gif",@"arw", @"mrw", @"erf", @"raf",@"cr2", @"nrw", @"nef", @"orf", @"rw2", @"pef", @"srf", @"dcr", @"kdc", @"dng",@"psd",@"webp"];
        if ([picArray containsObject:model.fileType]) {
            NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(model.path,0,model.fileType)];

            //1.创建多媒体消息结构体
            WXMediaMessage *mediaMsg = [WXMediaMessage message];
            //2.创建多媒体消息中包含的图片数据对象
            WXImageObject *imgObj = [WXImageObject object];
            //图片真实数据
            imgObj.imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:url]];
            //多媒体数据对象
            mediaMsg.mediaObject = imgObj;
            
            //3.创建发送消息至微信终端程序的消息结构体
            SendMessageToWXReq *req = [[SendMessageToWXReq alloc] init];
            //多媒体消息的内容
            req.message = mediaMsg;
            //指定为发送多媒体消息（不能同时发送文本和多媒体消息，两者只能选其一）
            req.bText = NO;
            //指定发送到会话(聊天界面)
            req.scene = WXSceneSession;
            [WXApi sendReq:req completion:^(BOOL success) {
                
            }];
            
        }else  if ([videoArray containsObject:model.fileType]) {
            if ([model.fileType isEqualToString:@"mp3"]) {
                NSDictionary *dic;
                dic = @{
                    @"busType":@"cloud",
                    @"path":@"info",
                    @"sourceID":model.sourceID
                };
                [VULBaseRequest requestWithUrl:@"/api/disk/preview" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
                    if (request.success) {
                        NSString *downloadUrl = request.data[@"downloadUrl"];
                        NSString *urlString = [NSString stringWithFormat:@"%@%@",ChooseUrl,downloadUrl];
                        urlString = [urlString stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
                        
                        //2.创建多媒体消息中包含的图片数据对象
                        WXMusicObject *music = [WXMusicObject object];
                        //图片真实数据
                        music.musicUrl = urlString;
                        
                        music.musicDataUrl = urlString;
                        NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(model.thumb,3,model.fileType)];

                        music.songAlbumUrl = url;
                        //多媒体数据对象
                        [VULQueue executeInMainQueue:^{
                            [self shareMusicWithUrl:music];
                        }];
                    }
                        
                    }];
            }
        }

    }else{
        [kWindow makeToast:KLanguage(@"请安装微信")];
    }

    
}
-(void)shareMusicWithUrl:( id) mediaObject{
    //1.创建多媒体消息结构体
    WXMediaMessage *mediaMsg = [WXMediaMessage message];
    mediaMsg.title = _shareModel.name;
    mediaMsg.thumbData = [self getImageData];
    mediaMsg.mediaObject = mediaObject;
    
    //3.创建发送消息至微信终端程序的消息结构体
    SendMessageToWXReq *req = [[SendMessageToWXReq alloc] init];
    //多媒体消息的内容
    req.message = mediaMsg;
    //指定为发送多媒体消息（不能同时发送文本和多媒体消息，两者只能选其一）
    req.bText = NO;
    //指定发送到会话(聊天界面)
    req.scene = WXSceneSession;
    [WXApi sendReq:req completion:^(BOOL success) {
        
    }];
}
-(void)shareFileWithString:(NSString *)string scene:(int)scene{
    //1.创建多媒体消息结构体
    SendMessageToWXReq *req = [[SendMessageToWXReq alloc] init];
    req.text                = string;
    req.bText               = YES;
    // 目标场景
    // 发送到聊天界面  WXSceneSession
    // 发送到朋友圈    WXSceneTimeline
    // 发送到微信收藏  WXSceneFavorite
    req.scene               = scene;
    [WXApi sendReq:req completion:^(BOOL success) {
        
    }];
}
-(void)shareDingdingWithString:(NSString *)string{
    if ([WXApi isWXAppInstalled] && [WXApi isWXAppSupportApi]) {
        DTSendMessageToDingTalkReq *sendMessageReq = [[DTSendMessageToDingTalkReq alloc] init];
        DTMediaMessage *mediaMessage = [[DTMediaMessage alloc] init];
        DTMediaTextObject *textObject = [[DTMediaTextObject alloc] init];
        textObject.text = string;
        mediaMessage.mediaObject = textObject;
        sendMessageReq.message = mediaMessage;
        
        BOOL result = [DTOpenAPI sendReq:sendMessageReq];
        if (result)
        {
            NSLog(@"Text 发送成功.");
        }
        else
        {
            NSLog(@"Text 发送失败.");
        }
        
    }else{
        [kWindow makeToast:KLanguage(@"请安装微信")];
    }
  
}
//分享资讯
-(void)shareInfoWithModel:(VULInfoModel *)model scene:(int)scene{
    if ([WXApi isWXAppInstalled] && [WXApi isWXAppSupportApi]) {
        
        
        NSString *string;
        if(model.infoUrl.length>0){
            string = model.infoUrl;
        }else{
            string = [NSString stringWithFormat:@"%@/pubinfo/%@.shtml",ChooseUrl,model.infoID];
        }
        WXWebpageObject *mediaObject = [WXWebpageObject object];
        mediaObject.webpageUrl = string;
        NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.thumb];
        //1.创建多媒体消息结构体
        WXMediaMessage *mediaMsg = [WXMediaMessage message];
        mediaMsg.title = model.title;
        mediaMsg.description = model.introduce;
        
        mediaMsg.thumbData = model.thumb.length>0 ?UIImageJPEGRepresentation([UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:url]]], 0.2):UIImageJPEGRepresentation(VULGetImage(@"icon_news_nodata"), 0.2);
        mediaMsg.mediaObject = mediaObject;
        
        //3.创建发送消息至微信终端程序的消息结构体
        SendMessageToWXReq *req = [[SendMessageToWXReq alloc] init];
        //多媒体消息的内容
        req.message = mediaMsg;
        //指定为发送多媒体消息（不能同时发送文本和多媒体消息，两者只能选其一）
        req.bText = NO;
        //指定发送到会话(聊天界面)
        req.scene = scene;
        [WXApi sendReq:req completion:^(BOOL success) {
            
        }];
        
    }else{
        [kWindow makeToast:KLanguage(@"请安装微信")];
    }
}
-(void)shareDingdingWithModel:(VULInfoModel *)model{
    
    // 检查是否安装了钉钉客户端
       if ([DTOpenAPI isDingTalkInstalled]) {
           NSString *string;
           if(model.infoUrl.length>0){
               string = model.infoUrl;
           }else{
               string = [NSString stringWithFormat:@"%@/pubinfo/%@.shtml",ChooseUrl,model.infoID];
           }
           DTSendMessageToDingTalkReq *sendMessageReq = [[DTSendMessageToDingTalkReq alloc] init];
           
           DTMediaMessage *mediaMessage = [[DTMediaMessage alloc] init];
           DTMediaWebObject *webObject = [[DTMediaWebObject alloc] init];
           webObject.pageURL = string;
           mediaMessage.title = model.title;
           NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.thumb];
           mediaMessage.thumbURL = url;
           
           // Or Set a image data which less than 32K.
           // mediaMessage.thumbData = UIImagePNGRepresentation([UIImage imageNamed:@"open_icon"]);
           
           mediaMessage.messageDescription =  model.introduce;
           mediaMessage.mediaObject = webObject;
           sendMessageReq.message = mediaMessage;
           
           BOOL result = [DTOpenAPI sendReq:sendMessageReq];
           if (result)
           {
               NSLog(@"Link 发送成功.");
           }
           else
           {
               NSLog(@"Link 发送失败.");
           }
       } else {
           [kWindow makeToast:[NSString stringWithFormat:@"%@%@",KLanguage(@"请安装"),KLanguage(@"钉钉")]];

       }

}
-(void)shareWeiboWithModel:(VULInfoModel *)model{
    // 检查是否安装了微博客户端
       if ([WeiboSDK isWeiboAppInstalled]) {
           NSString *string;
           if(model.infoUrl.length>0){
               string = model.infoUrl;
           }else{
               string = [NSString stringWithFormat:@"%@/pubinfo/%@.shtml",ChooseUrl,model.infoID];
           }
           
           // 创建分享消息对象
              WBMessageObject *message = [WBMessageObject message];
              NSString *randomIdentifier = [NSString stringWithFormat:@"%08X%08X", arc4random(), arc4random()];

              // 创建网页对象
              WBWebpageObject *webpage = [WBWebpageObject object];
              webpage.objectID = randomIdentifier;
              webpage.title =  model.title;
              webpage.description = model.introduce;
              webpage.webpageUrl =string;
              NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.thumb];
             webpage.thumbnailData =model.thumb.length>0 ?[NSData dataWithContentsOfURL:[NSURL URLWithString:url]]:UIImageJPEGRepresentation(VULGetImage(@"icon_news_nodata"), 0.2);
              message.mediaObject = webpage;
              
              // 创建请求对象
              WBSendMessageToWeiboRequest *request = [WBSendMessageToWeiboRequest requestWithMessage:message];
              
              // 发送请求
           [WeiboSDK sendRequest:request completion:^(BOOL success) {
           }];
       
       } else {
           // 提示用户没有安装微博客户端
           [kWindow makeToast:[NSString stringWithFormat:@"%@%@",KLanguage(@"请安装"),KLanguage(@"微博")]];

       }
}

-(void)shareWeiboWithString:(NSString *)string{
    // 检查是否安装了微博客户端
       if ([WeiboSDK isWeiboAppInstalled]) {
           WBMessageObject *message = [WBMessageObject message];
           // 设置分享的标题
           message.text = string;
           WBSendMessageToWeiboRequest *request = [WBSendMessageToWeiboRequest requestWithMessage:message];
           // 发送分享请求
           [WeiboSDK sendRequest:request completion:^(BOOL success) {
           }];
           
       } else {
           // 提示用户没有安装微博客户端
           [kWindow makeToast:[NSString stringWithFormat:@"%@%@",KLanguage(@"请安装"),KLanguage(@"微博")]];

       }
}


-(NSData *)getImageData{
    if (_shareModel.thumb &&_shareModel.thumb.length>0) {
        NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(_shareModel.thumb,3,_shareModel.fileType)];
        return   [NSData dataWithContentsOfURL:[NSURL URLWithString:url]];
    }else if ( _shareModel.path&&_shareModel.path.length>0) {
        NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(_shareModel.path,3,_shareModel.fileType)];
        return   [NSData dataWithContentsOfURL:[NSURL URLWithString:url]];
   
    }else{
        UIImage *image =  getLocalImageWithFileType(_shareModel.fileType);
        return UIImageJPEGRepresentation(image, 1.0);

    }
}

#pragma mark - WXApiDelegate
-(void) onResp:(BaseResp*)resp {
    NSString *code = nil;
    if ([resp isKindOfClass:[SendAuthResp class]]) {
        SendAuthResp *authRep = (SendAuthResp *)resp;
        if (resp.errCode == 0) {
            code = authRep.code;
        }
    }

    NSLog(@"weChat---code---%@===%d",code,resp.type);
    if (code) {
        
        //static dispatch_once_t hanwanjie;
        //dispatch_once(&hanwanjie, ^{
            if (_getWeChatCodeCompletion) {
                _getWeChatCodeCompletion(code);
            }
        //});
    }
}

-(void) onReq:(BaseReq*)req {
    if (req) {
        
    }
}

@end
