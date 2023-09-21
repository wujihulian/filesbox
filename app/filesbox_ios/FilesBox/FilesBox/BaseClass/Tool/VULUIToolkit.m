//  TPLUIToolkit.m
//
//  Created by 李新波 on 21-3-15.
//  Copyright (c) 2021年 Tai Ping. All rights reserved.
//

#import "VULUIToolkit.h"
#import <sys/xattr.h>
#include <objc/runtime.h>
#import "FBTabBarControllerConfig.h"
#define FileHashDefaultChunkSizeForReadingData 1024*8

//用于UIImage的imageNamed函数载入resource.bundle下的图片
NSMutableAttributedString * setRichNumberWithLabel(NSString *allStr, UIColor *color)
{
    //将Label的text转化为NSMutalbeAttributedString
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc]initWithString:allStr];
    //定义空字符串
    NSString *temp = nil;
    //根据属属性字符串的长度循环
    for (int i = 0; i < [attributedString length]; i++) {
        //每次取1个长度的字符串
        temp = [allStr substringWithRange:NSMakeRange(i, 1)];
        //判读这个长度的字符串中是否包含数字以及标点符号
        if ([temp isEqualToString:@"0"] || [temp isEqualToString:@"1"] || [temp isEqualToString:@"2"] || [temp isEqualToString:@"3"] || [temp isEqualToString:@"4"] || [temp isEqualToString:@"5"] || [temp isEqualToString:@"6"] || [temp isEqualToString:@"7"] || [temp isEqualToString:@"8"] || [temp isEqualToString:@"9"] || [temp isEqualToString:@"."] || [temp isEqualToString:@"％"] || [temp isEqualToString:@"k"]) {
            //给符合条件的属性字符串添加颜色,字体
            [attributedString setAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                             color, NSForegroundColorAttributeName,
                                             [UIFont yk_pingFangRegular:FontAuto(14)],
                                             NSFontAttributeName, nil] range:NSMakeRange(i, 1)];
        }
        if ([temp isEqualToString:@"-"]) {
            //给符合条件的属性字符串添加颜色,字体
            [attributedString setAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                             UIColorHex(#E60012), NSForegroundColorAttributeName,
                                             [UIFont yk_pingFangRegular:FontAuto(14)],
                                             NSFontAttributeName, nil] range:NSMakeRange(i, 1)];
        }
    }
    //重新给Label的text传递处理好的属性字符串
    return attributedString;
}

NSString * distanceTimeWithBeforeTime(double beTime)
{
    NSTimeInterval now = [[NSDate date]timeIntervalSince1970];
    beTime = beTime / 1000;
    double distanceTime = now - beTime;
    NSString *distanceStr;

    NSDate *beDate = [NSDate dateWithTimeIntervalSince1970:beTime];
    NSDateFormatter *df = [[NSDateFormatter alloc]init];
    [df setDateFormat:@"HH:mm"];
    NSString *timeStr = [df stringFromDate:beDate];

    [df setDateFormat:@"dd"];
    NSString *nowDay = [df stringFromDate:[NSDate date]];
    NSString *lastDay = [df stringFromDate:beDate];

    if (distanceTime < 60) {   //小于一分钟
        distanceStr = @"刚刚";
    } else if (distanceTime < 60 * 60) {//时间小于一个小时
        distanceStr = [NSString stringWithFormat:@"%ld分钟前", (long)distanceTime / 60];
    } else if (distanceTime < 24 * 60 * 60 && [nowDay integerValue] == [lastDay integerValue]) {//时间小于一天
        distanceStr = [NSString stringWithFormat:@"今天 %@", timeStr];
    } else if (distanceTime < 24 * 60 * 60 * 2 && [nowDay integerValue] != [lastDay integerValue]) {
        if ([nowDay integerValue] - [lastDay integerValue] == 1 || ([lastDay integerValue] - [nowDay integerValue] > 10 && [nowDay integerValue] == 1)) {
            distanceStr = [NSString stringWithFormat:@"昨天 %@", timeStr];
        } else {
            [df setDateFormat:@"MM-dd HH:mm"];
            distanceStr = [df stringFromDate:beDate];
        }
    } else if (distanceTime < 24 * 60 * 60 * 365) {
        [df setDateFormat:@"MM-dd HH:mm"];
        distanceStr = [df stringFromDate:beDate];
    } else {
        [df setDateFormat:@"yyyy-MM-dd HH:mm"];
        distanceStr = [df stringFromDate:beDate];
    }
    return distanceStr;
}

NSString *  imageNameWithSourceSuffix(NSString *fileType)
{
    if ([fileType hasSuffix:@"pdf"]) {
        return @"annexPDF";
    } else if ([fileType hasSuffix:@"jpg"] || [fileType hasSuffix:@"png"]  || [fileType hasSuffix:@"jpeg"] || [fileType hasSuffix:@"gif"]) {
        return @"annexJPEG";
    } else if ([fileType hasSuffix:@"ppt"] || [fileType hasSuffix:@"pptx"]) {
        return @"annexPPT";
    } else if ([fileType hasSuffix:@"excel"] || [fileType hasSuffix:@"xls"] || [fileType hasSuffix:@"xlsx"]) {
        return @"annexXLS";
    } else if ([fileType hasSuffix:@"zip"] || [fileType hasSuffix:@"rar"]) {
        return @"annexZIP";
    } else {
        return @"annexDOC";
    }
}
NSString *  imageUrlNameWithSourceSuffix(NSString *fileType)
{
    
//    /appstatic/images/brochure/word.png
//    /appstatic/images/brochure/jpeg.png
//    /appstatic/images/brochure/pdf.png
//    /appstatic/images/brochure/ppt.png
//    /appstatic/images/brochure/xls.png
//    /appstatic/images/brochure/zip.png
    if ([fileType hasSuffix:@"annexPDF"]) {
        return @"/appstatic/images/brochure/pdf.png";
    } else if ([fileType hasSuffix:@"annexJPEG"]) {
        return @"/appstatic/images/brochure/jpeg.png";
    } else if ([fileType hasSuffix:@"annexPPT"]) {
        return @"/appstatic/images/brochure/ppt.png";
    } else if ([fileType hasSuffix:@"annexXLS"]) {
        return @"/appstatic/images/brochure/xls.png";
    } else if ([fileType hasSuffix:@"annexZIP"]) {
        return @"/appstatic/images/brochure/zip.png";
    } else if ([fileType hasSuffix:@"annexDOC"]) {
        return @"/appstatic/images/brochure/word.png";
    } else{
        return fileType;
    }
}
NSString *  imageNameWithUrl(NSString *fileType)
{
    if ([fileType hasSuffix:@"/appstatic/images/brochure/pdf.png"]) {
        return @"annexPDF";
    } else if ([fileType hasSuffix:@"/appstatic/images/brochure/jpeg.png"]) {
        return @"annexJPEG";
    } else if ([fileType hasSuffix:@"/appstatic/images/brochure/ppt.png"]) {
        return @"annexPPT";
    } else if ([fileType hasSuffix:@"/appstatic/images/brochure/xls.png"]) {
        return @"annexXLS";
    } else if ([fileType hasSuffix:@"/appstatic/images/brochure/zip.png"]) {
        return @"annexZIP";
    } else if ([fileType hasSuffix:@"/appstatic/images/brochure/word.png"]) {
        return @"annexDOC";
    } else{
        return resultsUrl(fileType);
    }
}
BOOL  isLocalImageNameWithUrl(NSString *fileType)
{
    if ([fileType hasSuffix:@"/appstatic/images/brochure/pdf.png"]) {
        return YES;
    } else if ([fileType hasSuffix:@"/appstatic/images/brochure/jpeg.png"]) {
        return YES;
    } else if ([fileType hasSuffix:@"/appstatic/images/brochure/ppt.png"]) {
        return YES;
    } else if ([fileType hasSuffix:@"/appstatic/images/brochure/xls.png"]) {
        return YES;
    } else if ([fileType hasSuffix:@"/appstatic/images/brochure/zip.png"]) {
        return YES;
    } else if ([fileType hasSuffix:@"/appstatic/images/brochure/word.png"]) {
        return YES;
    } else{
        return NO;
    }
}

NSString * isEmpty(NSString *str){
    if(str){
        if ([(str) isKindOfClass:[NSNumber class]]) {
            return  [NSString stringWithFormat:@"%@",str];
        }
        return str;
    }
    return  @"-";
}
NSString * isEmptyPre(NSString *str){
    if(str){
        if ([(str) isKindOfClass:[NSNumber class]]) {
            return  [NSString stringWithFormat:@"%f",str.floatValue/100];
        }
        return [NSString stringWithFormat:@"%f",str.floatValue/100];
    }
    return  @"-";
}

NSString * isEmptyIp(NSString *str){
    if(str){
        if ([(str) isKindOfClass:[NSNumber class]]) {
            return  [NSString stringWithFormat:@"%@",str];
        }
        return str;
    }
    return  @"";
}

NSString * isChange(NSString *str){
    if (str.floatValue>1000) {
        return  [NSString stringWithFormat:@"%@",str];
    }else{
        return str;
    }
}


NSString * removeSuffix(NSString *numberStr){
    if (numberStr.length > 1) {
        
        if ([numberStr componentsSeparatedByString:@"."].count == 2) {
            NSString *last = [numberStr componentsSeparatedByString:@"."].lastObject;
            if ([last isEqualToString:@"00"]) {
                numberStr = [numberStr substringToIndex:numberStr.length - (last.length + 1)];
                return numberStr;
            }else{
                if ([[last substringFromIndex:last.length -1] isEqualToString:@"0"]) {
                    numberStr = [numberStr substringToIndex:numberStr.length - 1];
                    return numberStr;
                }
            }
        }
        return numberStr;
    }else{
        return nil;
    }
}

//用于UIImage的imageNamed函数载入resource.bundle下的图片
NSMutableAttributedString * setRichNumberWithLabelOrFont(NSString *allStr, UIColor *color,UIFont *font)
{
    //将Label的text转化为NSMutalbeAttributedString
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc]initWithString:allStr];
    //定义空字符串
    NSString *temp = nil;
    //根据属属性字符串的长度循环
    for (int i = 0; i < [attributedString length]; i++) {
        //每次取1个长度的字符串
        temp = [allStr substringWithRange:NSMakeRange(i, 1)];
        //判读这个长度的字符串中是否包含数字以及标点符号
        if ([temp isEqualToString:@"0"] || [temp isEqualToString:@"1"] || [temp isEqualToString:@"2"] || [temp isEqualToString:@"3"] || [temp isEqualToString:@"4"] || [temp isEqualToString:@"5"] || [temp isEqualToString:@"6"] || [temp isEqualToString:@"7"] || [temp isEqualToString:@"8"] || [temp isEqualToString:@"9"] || [temp isEqualToString:@"."] || [temp isEqualToString:@"％"] || [temp isEqualToString:@"k"]|| [temp isEqualToString:@"-"] || [temp isEqualToString:@"￥"]) {
            //给符合条件的属性字符串添加颜色,字体
            [attributedString setAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                             color, NSForegroundColorAttributeName,
                                             font,
                                             NSFontAttributeName, nil] range:NSMakeRange(i, 1)];
        }
    }
    //重新给Label的text传递处理好的属性字符串
    return attributedString;
}

NSMutableAttributedString *  setRichNumberWithLabelOrFontOrStr (NSString *allStr ,UIColor *color,UIFont *font,NSString *str){
//    NSString *allStr = [NSString stringWithFormat:@"统计课时  %@节",self.model.statisticsCount];
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc]initWithString:allStr];
    [attributedString setAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                     color, NSForegroundColorAttributeName,
                                     font,
                                     NSFontAttributeName, nil] range:NSMakeRange(0, str.length)];
    return  attributedString;
}
NSString *msTimeChange (NSInteger ms){
    if (ms == 0) {
        return [NSString stringWithFormat:@"%d秒",0];

    }
    NSInteger seconds = ms/1000;
    if (seconds<60) {
        return [NSString stringWithFormat:@"%ld秒",seconds];
    }
    NSInteger points = seconds/60;
    NSInteger point_seconds = seconds%60;

    if (points<60) {
        if (point_seconds == 0) {
            return [NSString stringWithFormat:@"%ld分",points];
        }
        return [NSString stringWithFormat:@"%ld分%ld秒",points,point_seconds];
    }
    NSInteger hours = seconds/3600;
    NSInteger last = seconds%3600;
     points = last/60;
    point_seconds = last%60;
    if (last == 0) {
        return  [NSString stringWithFormat:@"%ld小时",hours];
    }
    if (point_seconds == 0) {
        return  [NSString stringWithFormat:@"%ld小时%ld分",hours,points];
    }
    return  [NSString stringWithFormat:@"%ld小时%ld分%ld秒",hours,points,point_seconds];

}
NSString *backTimeChange (NSInteger ms){
    if (ms == 0) {
        return [NSString stringWithFormat:@"%d秒",0];

    }
    NSInteger seconds = ms;
    if (seconds<60) {
        return [NSString stringWithFormat:@"%ld秒",seconds];
    }
    NSInteger points = seconds/60;
    NSInteger point_seconds = seconds%60;

    if (points<60) {
        if (point_seconds == 0) {
            return [NSString stringWithFormat:@"%ld分",points];
        }
        return [NSString stringWithFormat:@"%ld分%ld秒",points,point_seconds];
    }
    NSInteger hours = seconds/3600;
    NSInteger last = seconds%3600;
     points = last/60;
    point_seconds = last%60;
    if (last == 0) {
        return  [NSString stringWithFormat:@"%ld小时",hours];
    }
    if (point_seconds == 0) {
        return  [NSString stringWithFormat:@"%ld小时%ld分",hours,points];
    }
    return  [NSString stringWithFormat:@"%ld小时%ld分%ld秒",hours,points,point_seconds];
}
/*
 * 字符串转字典（NSString转Dictionary）
 *   parameter
 *     turnString : 需要转换的字符串
 */

NSDictionary *turnStringToDictionary(NSString *turnString){

    NSData *turnData = [turnString dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary *turnDic = [NSJSONSerialization JSONObjectWithData:turnData options:NSJSONReadingMutableLeaves error:nil];
    return turnDic;
}

NSString *resultsUrl(NSString *str){
    NSString *url = str;
    [url stringByReplacingOccurrencesOfString:@".mp4" withString:@".jpg"];
    if ([url hasPrefix:@"https://"] || [url hasPrefix:@"http://"] ) {
        return  url;
    } else if([url hasPrefix:@"//"]){
        return [NSString stringWithFormat:@"http:%@",url];

    }else{
        NSLog(@"%@", [NSString stringWithFormat:@"%@%@",ChooseUrl,url]);
        return [NSString stringWithFormat:@"%@%@",ChooseUrl,url];
        
    }
}
NSString *resultsVdieoUrl(NSString *str){
    NSString *url = str;
    if ([url hasPrefix:@"https://"] || [url hasPrefix:@"http://"] ) {
        return  url;
    } else if([url hasPrefix:@"//"]){
        return [NSString stringWithFormat:@"http:%@",url];

    }else{
        return [NSString stringWithFormat:@"%@%@",kCDNHostUrl,url];
    }
}
NSString *changeImageUrl(NSString *str){
    if ([str hasSuffix:@"132"]) {
        NSString *url = [str substringToIndex:str.length-3];
        return [NSString stringWithFormat:@"%@0",url];
    }
    return str;
    
}


NSString  *getbusType(NSString *infotype){
    NSString *mBusType = @"";
       switch (infotype.intValue) {
           case 0:
               mBusType = @"1";
               break;
           case 1:
               mBusType = @"2";
               break;
           case 2:
               mBusType = @"8";
               break;
           case 3:
               mBusType = @"13";
               break;
           case 4:
               mBusType = @"12";
               break;
           case 5:
               mBusType = @"11";
               break;
           case 6:
               mBusType = @"10";
               break;
           case 7:
               mBusType = @"9";
               break;
           case 8:
               mBusType = @"19";
               break;
           case 9:
               mBusType = @"18";
               break;
           case 10:
               mBusType = @"17";
               break;
           case 11:
               mBusType = @"16";
               break;
           case 13:
               mBusType = @"21";
               break;
           case 14:
               mBusType = @"22";
               break;
           case 15:
               mBusType = @"23";
               break;
           case 21:
               mBusType = @"31";
               break;
           default:
               mBusType = infotype;
               break;
       }

       return mBusType;
   }
NSString  *getInfoType(NSString *busType){
    NSString *mInfoType = @"";
       switch (busType.intValue) {
           case 1:
               mInfoType = @"0";
               break;
           case 2:
               mInfoType = @"1";
               break;
           case 8:
               mInfoType = @"2";
               break;
           case 13:
               mInfoType = @"3";
               break;
           case 12:
               mInfoType = @"4";
               break;
           case 11:
               mInfoType = @"5";
               break;
           case 20:
               mInfoType = @"6";
               break;
           case 9:
               mInfoType = @"7";
               break;
           case 19:
               mInfoType = @"8";
               break;
           case 18:
               mInfoType = @"9";
               break;
           case 17:
               mInfoType = @"10";
               break;
           case 16:
               mInfoType = @"11";
               break;
           case 21:
               mInfoType = @"13";
               break;
           case 22:
               mInfoType = @"14";
               break;
           case 23:
               mInfoType = @"15";
               break;
           default:
               mInfoType = busType;
               break;
       }

       return mInfoType;
}


NSString * changeUrl(NSString *url)
{
    if ([url containsString:@"&w=1"]) {
        return   changeClearKF([url stringByReplacingOccurrencesOfString:@"&w=1" withString:@""]);
    }else{
        return changeClearKF(url);
    }
}

NSString * changeClearKF(NSString *url){
    if ([url containsString:@"&kf=1"]) {
        return   [url stringByReplacingOccurrencesOfString:@"&kf=1" withString:@""];
    }else{
        return url;
    }
}


NSString *getFileNewMD5WithPath(NSString* path,size_t chunkSizeForReadingData)
{
    CFStringRef filePath = (__bridge CFStringRef)path;
    // Declare needed variables
    CFStringRef result = NULL;
    CFReadStreamRef readStream = NULL;
    // Get the file URL
    CFURLRef fileURL =
    CFURLCreateWithFileSystemPath(kCFAllocatorDefault,
                                  (CFStringRef)filePath,
                                  kCFURLPOSIXPathStyle,
                                  (Boolean)false);
    if (!fileURL) goto done;
    // Create and open the read stream
    readStream = CFReadStreamCreateWithFile(kCFAllocatorDefault,
                                            (CFURLRef)fileURL);
    if (!readStream) goto done;
    bool didSucceed = (bool)CFReadStreamOpen(readStream);
    if (!didSucceed) goto done;
    // Initialize the hash object
    CC_MD5_CTX hashObject;
    CC_MD5_Init(&hashObject);
    // Make sure chunkSizeForReadingData is valid
    if (!chunkSizeForReadingData) {
        chunkSizeForReadingData = FileHashDefaultChunkSizeForReadingData;
    }
    // Feed the data to the hash object
    bool hasMoreData = true;
    while (hasMoreData) {
        uint8_t buffer[chunkSizeForReadingData];
        CFIndex readBytesCount = CFReadStreamRead(readStream,(UInt8 *)buffer,(CFIndex)sizeof(buffer));
        if (readBytesCount == -1) break;
        if (readBytesCount == 0) {
            hasMoreData = false;
            continue;
        }
        CC_MD5_Update(&hashObject,(const void *)buffer,(CC_LONG)readBytesCount);
    }
    // Check if the read operation succeeded
    didSucceed = !hasMoreData;
    // Compute the hash digest
    unsigned char digest[CC_MD5_DIGEST_LENGTH];
    CC_MD5_Final(digest, &hashObject);
    // Abort if the read operation failed
    if (!didSucceed) goto done;
    // Compute the string result
    char hash[2 * sizeof(digest) + 1];
    for (size_t i = 0; i < sizeof(digest); ++i) {
        snprintf(hash + (2 * i), 3, "%02x", (int)(digest[i]));
    }
    result = CFStringCreateWithCString(kCFAllocatorDefault,(const char *)hash,kCFStringEncodingUTF8);
done:
    if (readStream) {
        CFReadStreamClose(readStream);
        CFRelease(readStream);
    }
    if (fileURL) {
        CFRelease(fileURL);
    }
    return (__bridge_transfer NSString *)result;
   
    
}

NSString *filePath(NSString *fileName){
    NSArray* myPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString* myDocPath = [myPaths objectAtIndex:0];
    NSString* filePath = [myDocPath stringByAppendingPathComponent:fileName];
    return filePath;
}

NSString *backBaseUrl(NSString *baseUrl){
    if ([baseUrl hasPrefix:@"https://"]) {
        return [baseUrl substringFromIndex:8];
    }else if([baseUrl hasPrefix:@"http://"]){
        return [baseUrl substringFromIndex:7];
    }
    return baseUrl;
}
NSString *backImageBgUrl(NSInteger layout,NSInteger index){
    NSArray *arr;
    if (layout ==5) {
        return @"/appstatic/images/card-bg-simple.png";
    }else if(layout ==3){
        return @"/appstatic/images/card-bg-brief.png";
    }else{
        return @"/appstatic/images/cardBg.png";

    }
}
UIColor *backColorWithIndex(NSInteger index){
    NSArray *backArr = @[@"0",@"1",@"5",@"7",@"8",@"9",@"23",@"24",@"27",@"28",@"30",@"31",@"36",@"40",@"41",@"49",@"50"];
    NSArray *yelloArr = @[@"32",@"33",@"34",@"35"];
    if ([backArr containsObject:[NSString stringWithFormat:@"%ld",index]]) {
        return HEXCOLOR(0x333333);
    }
    if ([yelloArr containsObject:[NSString stringWithFormat:@"%ld",index]]) {
        return HEXCOLOR(0xfff3b9);
    }
    return HEXCOLOR(0xffffff);

//    NSArray *whiteArr = @ [2,3,4,6,9,10,14,15,16,18,19,20,23,24,25,26,27,28,29,30,31,32,33,35,36,39,40,43,44,46,49,50],


}

NSString *backPreviewString(NSString *type,NSInteger count){
    if (type.integerValue == 1 || type.integerValue == 17 ) {
        return changeName( [NSString stringWithFormat:@"已有%ld人查看过该企业简介",count]);
    }else if(type.integerValue == 5 || type.integerValue == 6 ){
        return changeName( [NSString stringWithFormat:@"已递给%ld个客户",count]);
    }else if(type.integerValue ==7){
        return changeName( [NSString stringWithFormat:@"被%ld个客户了解过",count]);
   }else if(type.integerValue ==15  || type.integerValue ==16 ||type.integerValue ==20){
        return  [NSString stringWithFormat:@"已传递%ld次",count];
    }else if(type.integerValue ==18){
        return  changeName([NSString stringWithFormat:@"被%ld位客户浏览过",count]);
    }else{
        return  [NSString stringWithFormat:@"%ld次浏览",count];

    }
}

NSString *changeName (NSString *name){
    return name;
}
NSString *hexadecimalFromUIColor(UIColor *color){
    const CGFloat *components = CGColorGetComponents(color.CGColor);

      CGFloat r = components[0];
      CGFloat g = components[1];
      CGFloat b = components[2];

      return [NSString stringWithFormat:@"#%02lX%02lX%02lX",
              lroundf(r * 255),
              lroundf(g * 255),
              lroundf(b * 255)];
}



UIImage * backCaptureImageFromView(UIView *view){
    CGRect screenRect = [view bounds];
    if ([[UIScreen mainScreen] respondsToSelector:@selector(scale)]) {
        UIGraphicsBeginImageContextWithOptions(screenRect.size, NO, [UIScreen mainScreen].scale);
    } else {
        UIGraphicsBeginImageContext(screenRect.size);
    }
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    [view.layer renderInContext:ctx];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return image;
}


//压缩图片
 UIImage  *compressionSmallImage(UIImage*image ,CGSize newSize)
{
    // Create a graphics image context
    UIGraphicsBeginImageContext(newSize);
    // Tell the old image to draw in this new context, with the desired
    // new size
    [image drawInRect:CGRectMake(0,0,newSize.width,newSize.height)];
    // Get the new image from the context
    UIImage* newImage =UIGraphicsGetImageFromCurrentImageContext();
    // End the context
    UIGraphicsEndImageContext();
    // Return the new image.
    return newImage;
}

UIImage  *createImageWithColor(UIColor*color){
    CGRect  rect=CGRectMake(0,0,1,1);UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;
}

 NSMutableDictionary *getURLParameters(NSString *url){
     
     // 查找参数
     NSRange range = [url rangeOfString:@"?"];
     if (range.location == NSNotFound) {
         return nil;
     }

     NSMutableDictionary *params = [NSMutableDictionary dictionary];

     // 截取参数
     NSString *parametersString = [url substringFromIndex:range.location + 1];

     // 判断参数是单个参数还是多个参数
     if ([parametersString containsString:@"&"]) {

         // 多个参数，分割参数
         NSArray *urlComponents = [parametersString componentsSeparatedByString:@"&"];

         for (NSString *keyValuePair in urlComponents) {
             // 生成Key/Value
             NSArray *pairComponents = [keyValuePair componentsSeparatedByString:@"="];
             NSString *key = [pairComponents.firstObject stringByRemovingPercentEncoding];
             NSString *value = [pairComponents.lastObject stringByRemovingPercentEncoding];

             // Key不能为nil
             if (key == nil || value == nil) {
                 continue;
             }

             id existValue = [params valueForKey:key];

             if (existValue != nil) {

                 // 已存在的值，生成数组
                 if ([existValue isKindOfClass:[NSArray class]]) {
                     // 已存在的值生成数组
                     NSMutableArray *items = [NSMutableArray arrayWithArray:existValue];
                     [items addObject:value];

                     [params setValue:items forKey:key];
                 } else {

                     // 非数组
                     [params setValue:@[existValue, value] forKey:key];
                 }

             } else {

                 // 设置值
                 [params setValue:value forKey:key];
             }
         }
     } else {
         // 单个参数

         // 生成Key/Value
         NSArray *pairComponents = [parametersString componentsSeparatedByString:@"="];

         // 只有一个参数，没有值
         if (pairComponents.count == 1) {
             return nil;
         }

         // 分隔值
         NSString *key = [pairComponents.firstObject stringByRemovingPercentEncoding];
         NSString *value = [pairComponents.lastObject stringByRemovingPercentEncoding];

         // Key不能为nil
         if (key == nil || value == nil) {
             return nil;
         }

         // 设置值
         [params setValue:value forKey:key];
     }

     return params;
 }
NSString *zeroWtihString(NSString *str){
    if (str.floatValue == 0) {
        return @"-";
    }
    return str;
}
NSString *typeWtihSignState(NSString *str){
//    客户签约状态：0-潜在、1-签约、2-复购、3-失效
    
    if (str.integerValue == 0) {
        return @"潜在";
    }else if(str.integerValue == 1){
        return @"签约";
    }else if(str.integerValue == 2){
        return @"复购";
    }else if(str.integerValue == 3){
        return @"失效";
    }
    return @"";
}

float chooseScreenWithType(NSInteger index){
    
    if (index ==1 ) {
//        2/5;
        return 0.4;
    }
    if (index ==2 ) {
//        float size =3.0/4.0;
        return 4.0/3.0;
    }
    if (index ==3 ) {
        float size =(VULSCREEN_HEIGHT-K_NavBar_Height-K_TabBar_Height-K_BottomBar_Height)/(VULSCREEN_WIDTH);
        return size;
    }
    return 0.4;
}
UIImage *getLocalImageWithFileType(NSString *icon){
    
  
    if ([icon isEqualToString:@"avi"]) {
        return VULGetImage(@"annexAVI");
    }
    if ([icon isEqualToString:@"doc"]) {
        return VULGetImage(@"annexDOC");
    }
    if ([icon isEqualToString:@"docx"]) {
        return VULGetImage(@"annexDOCX");
    }
    if ([icon isEqualToString:@"flv"]) {
        return VULGetImage(@"annexFLV");
    }
    if ([icon isEqualToString:@"html"]) {
        return VULGetImage(@"annexHTML");
    }
    if ([icon isEqualToString:@"mov"]) {
        return VULGetImage(@"annexMOV");
    }
    if ([icon isEqualToString:@"mp3"]) {
        return VULGetImage(@"annexMP3");
    }
    if ([icon isEqualToString:@"mp4"]) {
        return VULGetImage(@"annexMP4");
    }
    if ([icon isEqualToString:@"mpg"]) {
        return VULGetImage(@"annexMPG");
    }
    if ([icon isEqualToString:@"pdf"]) {
        return VULGetImage(@"annexPDF");
    }
    if ([icon isEqualToString:@"ppt"]) {
        return VULGetImage(@"annexPPT");
    }
    if ([icon isEqualToString:@"pptx"]) {
        return VULGetImage(@"annexPPTX");
    }
    if ([icon isEqualToString:@"rmve"]) {
        return VULGetImage(@"annexRMVE");
    }
    if ([icon isEqualToString:@"swf"]) {
        return VULGetImage(@"annexSWF");
    }
    if ([icon isEqualToString:@"torrent"]) {
        return VULGetImage(@"annexTORRENT");
    }
    if ([icon isEqualToString:@"txt"]) {
        return VULGetImage(@"annexTXT");
    }
    if ([icon isEqualToString:@"wmv"]) {
        return VULGetImage(@"annexWMV");
    }
    if ([icon isEqualToString:@"xls"]) {
        return VULGetImage(@"annexXLS");
    }
    if ([icon isEqualToString:@"xlsx"]) {
        return VULGetImage(@"annexXLSX");
    }
    if ([icon isEqualToString:@"zip"]) {
        return VULGetImage(@"annexZIP");
    }
    if ([icon isEqualToString:@"bmp"]) {
        return VULGetImage(@"annexBMP");
    }
    if ([icon isEqualToString:@"gif"]) {
        return VULGetImage(@"annexGIF");
    }
    if ([icon isEqualToString:@"png"]) {
        return VULGetImage(@"annexPNG");
    }
    if ([icon isEqualToString:@"jpeg"]) {
        return VULGetImage(@"annexJPEG");
    }
    if ([icon isEqualToString:@"jpg"]) {
        return VULGetImage(@"annexJPG");
    }
    if ([icon isEqualToString:@"movie"]) {
        return VULGetImage(@"annexMOVIE");
    }
    if ([icon isEqualToString:@"mkv"]) {
        return VULGetImage(@"annexMKV");
    }
    if ([icon isEqualToString:@"rar"]) {
        return VULGetImage(@"annex_rar");
    }
    NSString *name  = [NSString stringWithFormat:@"annex_%@",icon];
    if(VULGetImage(name)){
        return VULGetImage(name);
    }
    NSArray *mp3Array = @[@"mp3",@"wav",@"wma",@"m4a",@"ogg",@"omf",@"amr",@"aa3",@"flac",@"aac",@"cda",@"aif",@"aiff",@"mid",@"ra",@"ape"];

    if ([mp3Array containsObject:icon]) {
        return VULGetImage(@"annexMP3");
    }
    return VULGetImage(@"icon_unknow_file");
}
NSString *fileImageWithUrl(NSString *url,NSInteger size,NSString *fileType){
    NSArray *stringArr = [url componentsSeparatedByString:@"."];
    NSString *type = [stringArr lastObject];
    NSArray *typeArr = @[@"png",@"jpg",@"jpeg",@"bmp"];
    NSArray *sizeArr = @[@"!marked",@"!medium",@"!secondary",@"!nameSuffix",@"!tiny"];


    if ([typeArr containsObject:fileType] ) {
        if(size ==3){
            return [NSString stringWithFormat:@"%@&nameSuffix=small",url];
        }
        return url;
//            return  [NSString stringWithFormat:@"%@%@.%@",stringArr[0],sizeArr[size],type];

    }else{
        return url;
    }

}
void saveUploadFileWithModel(UBUploadModel *uploadModel){
        NSMutableArray *uploadFileList = [NSMutableArray array];
    [[DownloadProgress sharedInstance].progressDict setValue:uploadModel forKey:uploadModel.filePath];
    [NSArray bg_clearArrayWithName:@"uploadFileList"];
    [uploadFileList addObject:[DownloadProgress sharedInstance].progressDict];
    [uploadFileList bg_saveArrayWithName:@"uploadFileList"];
    // 手动发送KVO通知
    [VULNotificationCenter postNotificationName:@"uploadChangeNotificationCenter" object:nil];

}
void getUploadFileWithModel(){
    NSMutableArray *uploadFileList = [NSMutableArray arrayWithArray:[NSArray bg_arrayWithName:@"uploadFileList"]];

    if (uploadFileList.count == 0) {
    }else{
        [DownloadProgress sharedInstance].progressDict = [NSMutableDictionary dictionaryWithDictionary:uploadFileList[0]];
        NSMutableDictionary *progressDict = [DownloadProgress sharedInstance].progressDict;
        NSArray *arr =  progressDict.allKeys;
        
        for (NSString *filePath in arr) {
            UBUploadModel *model = progressDict[filePath];
            model.isOffline = YES;
            model.status = UploadStatusPaused;
        }
       
    }

    [FBDownloadFileAllManage sharedManager].sessionDic = [[NSUserDefaults standardUserDefaults] objectForKey:@"sessionDic"];

}
 UIColor *getColorWithGgb(NSString *tgb){
     
     if (!tgb) {
         return getColorWithGgb(@"rgb(193, 28, 123)");
     }
  
//     / 获取括号中的数字，并将其转换为 CGFloat 类型
     tgb = [tgb stringByReplacingOccurrencesOfString:@"" withString:@" "];
     NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:@"\\((\\d+),\\s*(\\d+),\\s*(\\d+)\\)" options:0 error:nil];
     NSTextCheckingResult *result = [regex firstMatchInString:tgb options:0 range:NSMakeRange(0, [tgb length])];
     CGFloat red = [[tgb substringWithRange:[result rangeAtIndex:1]] floatValue] / 255.0f;
     CGFloat green = [[tgb substringWithRange:[result rangeAtIndex:2]] floatValue] / 255.0f;
     CGFloat blue = [[tgb substringWithRange:[result rangeAtIndex:3]] floatValue] / 255.0f;

     // 创建 UIColor 对象
     UIColor *color = [UIColor colorWithRed:red green:green blue:blue alpha:1.0f];
    return color;
}
UIImage *getImageWithColor(UIColor *color){

    // 定义图片大小
    CGSize imageSize = CGSizeMake(1, 1);

    // 开始绘制图片
    UIGraphicsBeginImageContextWithOptions(imageSize, NO, 0);

    // 获取上下文
    CGContextRef context = UIGraphicsGetCurrentContext();

    // 设置填充颜色
    CGContextSetFillColorWithColor(context, color.CGColor);

    // 填充矩形
    CGContextFillRect(context, CGRectMake(0, 0, imageSize.width, imageSize.height));

    // 从上下文中获取图片
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();

    // 结束绘制图片
    UIGraphicsEndImageContext();
    return  image;
}
NSMutableDictionary *backBlockAndFileType(NSString *title){
    NSString *block;
    NSString *fileType;

    if ([title isEqualToString:KLanguage(@"收藏夹")]) {
        block = @"fav";
        fileType = @"all";
    }else if([title isEqualToString:KLanguage(@"资讯")]){
        block = @"info";
        fileType = @"all";
    }else if([title isEqualToString:KLanguage(@"我分享的")] ||[title isEqualToString:KLanguage(@"分享的")] ){
        block = @"shareLink";
        fileType = @"all";
    }
    else if([title isEqualToString:KLanguage(@"最近打开的")]||[title isEqualToString:KLanguage(@"最近的")]){
        block = @"recentDoc";
        fileType = @"all";

    }
    else if([title isEqualToString:KLanguage(@"视频")]){
        block = @"movie";
        fileType = @"mp4,flv,rm,rmvb,avi,mkv,mov,f4v,mpeg,mpg,vob,wmv,ogv,webm,3gp,mts,m2ts,m4v,mpe,3g2,asf,dat,asx,wvx,mpa";

    }
    else if([title isEqualToString:KLanguage(@"音乐")]){
        block =@"music";
        fileType = @"mp3,wav,wma,m4a,ogg,omf,amr,aa3,flac,aac,cda,aif,aiff,mid,ra,ape";

    }
    else if([title isEqualToString:KLanguage(@"文档")]){
        block = @"doc";
        fileType = @"txt,md,pdf,ofd,doc,docx,xls,xlsx,ppt,pptx,xps,pps,ppsx,ods,odt,odp,docm,dot,dotm,xlsb,xlsm,mht,djvu,wps,dpt,csv,et,ett,pages,numbers,key,dotx,vsd,vsdx,mpp";
    }
    else if([title isEqualToString:KLanguage(@"图片")]){
        block = @"image";
        fileType = @"jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai";
    }else if([title isEqualToString:KLanguage(@"压缩")]){
        block = @"zip";
        fileType = @"zip,gz,rar,iso,tar,7z,gz,ar,bz,bz2,xz,arj";
    }
    else if([title isEqualToString:KLanguage(@"其他")]){
        block = @"other";
        fileType = @"swf,html,exe,msi";
    }else if([title isEqualToString:KLanguage(@"标签")]){
        block = @"tag";
        fileType = @"all";
    }else if([title isEqualToString:KLanguage(@"回收站")]){
        block = @"recycle";
        fileType = @"all";
    }
    
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    [dic setValue:block forKey:@"block"];
    [dic setValue:fileType forKey:@"fileType"];

    return dic;
}
NSString *getTypeWithTitle(NSString *title){
    NSString *fileType;
    if([title isEqualToString:KLanguage(@"视频")]){
       fileType = @"mp4,flv,rm,rmvb,avi,mkv,mov,f4v,mpeg,mpg,vob,wmv,ogv,webm,3gp,mts,m2ts,m4v,mpe,3g2,asf,dat,asx,wvx,mpa";

   }
   else if([title isEqualToString:KLanguage(@"音乐")]){
       fileType = @"mp3,wav,wma,m4a,ogg,omf,amr,aa3,flac,aac,cda,aif,aiff,mid,ra,ape";

   }
   else if([title isEqualToString:KLanguage(@"文档")]){
       fileType = @"txt,md,pdf,ofd,doc,docx,xls,xlsx,ppt,pptx,xps,pps,ppsx,ods,odt,odp,docm,dot,dotm,xlsb,xlsm,mht,djvu,wps,dpt,csv,et,ett,pages,numbers,key,dotx,vsd,vsdx,mpp";
   }
   else if([title isEqualToString:KLanguage(@"图片")]){
       fileType = @"jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai";
   }else if([title isEqualToString:KLanguage(@"压缩包")]){
       fileType = @"zip,gz,rar,iso,tar,7z,gz,ar,bz,bz2,xz,arj";
   }
   else if([title isEqualToString:KLanguage(@"不限类型")]){
       fileType = @"all";
   }else if([title isEqualToString:KLanguage(@"任意文件")]){
       fileType = @"allFile";
   }else if([title isEqualToString:KLanguage(@"文件夹")]){
       fileType = @"folder";
   }
    return fileType;
    
}

NSString *backTitleWithBlock(NSString *block){
    
    if ([block isEqualToString:@"fav"]) {
        return KLanguage(@"收藏夹");
    }else if([block isEqualToString:@"shareLink"] ){
        return KLanguage(@"分享的");
    }
    else if([block isEqualToString:@"recentDoc"]){
        return KLanguage(@"最近的");
    }
    else if([block isEqualToString:@"movie"]){
        return KLanguage(@"视频");
    }
    else if([block isEqualToString:@"music"]){
        return KLanguage(@"音乐");
    }
    else if([block isEqualToString:@"doc"]){
        return KLanguage(@"文档");
    }
    else if([block isEqualToString:@"image"]){
        return KLanguage(@"图片");
    }else if([block isEqualToString:@"zip"]){
        return KLanguage(@"压缩");
    }
    else if([block isEqualToString:@"other"]){
        return KLanguage(@"其他");
    }else if([block isEqualToString:@"tag"]){
        return KLanguage(@"标签");
    }else if([block isEqualToString:@"recycle"]){
        return KLanguage(@"回收站");
    }else if([block isEqualToString:@"info"]){
        return KLanguage(@"资讯");
    }
    return @"";
}
NSString *formattedFileSize(long bytes) {
    CGFloat convertedValue;
    NSString *sizeSuffix;
    
    if (bytes >= 1024*1024*1024) {
        convertedValue = bytes / (1024.0 * 1024.0 * 1024.0);
        sizeSuffix = @"GB";
    } else {
        convertedValue = bytes / (1024.0 * 1024.0);
        sizeSuffix = @"MB";
    }
    
    NSString *result = [NSString stringWithFormat:@"%.2f %@", convertedValue, sizeSuffix];
    return result;
}
NSString *getTimeWithTime(NSString *time){
    return  [NSString stringWithFormat:@"%f",time.doubleValue*1000];
}

NSString *getSortFieldWithTitle(NSString *title){
    if ([title isEqualToString:KLanguage(@"名称")]) {
        return @"name";
    }
    if ([title isEqualToString:KLanguage(@"类型")]) {
        return @"fileType";
    }
    if ([title isEqualToString:KLanguage(@"大小")]) {
        return @"size";
    }
    if ([title isEqualToString:KLanguage(@"修改时间")]) {
        return @"modifyTime";
    }
    return @"";
}
void changeToken(){
    // 开始登录加载动画
    NSString *userNameStr = [[NSUserDefaults standardUserDefaults] objectForKey:@"userName"];
    NSString *passwordStr = [[NSUserDefaults standardUserDefaults] objectForKey:@"passWord"];
    if (userNameStr.length == 0 || passwordStr.length == 0 ) {
        RLMResults<VULSaveUserToken *> *userInformations = [VULSaveUserToken allObjects];
        RLMRealm *realm = [RLMRealm defaultRealm];
        
        [realm beginWriteTransaction];
        [realm deleteObjects:userInformations];
        [realm commitWriteTransaction];
        UIViewController *rootController = [UIApplication sharedApplication].delegate.window.rootViewController;
        
        if (![rootController.presentingViewController isKindOfClass:[VULLoginViewController class]]) {
            
            VULLoginViewController *login = [[VULLoginViewController alloc] init];
            VULNavigationViewController *nav = [[VULNavigationViewController alloc] initWithRootViewController:login];
            nav.modalPresentationStyle = UIModalPresentationFullScreen;
            [rootController presentViewController:nav animated:YES completion:nil];
        }
        return;
    }

    VULRequestLoginModel *loginModel = [[VULRequestLoginModel alloc] initWithUsername:userNameStr password:passwordStr encrypted:YES];
    //获取学校列表
    [loginModel startWithCompletionBlockWithSuccess:^(__kindof YTKBaseRequest *_Nonnull request) {
        NSString *success =request.responseObject[@"success"];
        if (success.boolValue) {
            [VULRealmDBManager updateLocaToken:[request.responseObject[@"data"] objectForKey:@"token"] andUserID:[request.responseObject[@"data"] objectForKey:@"userID"]];
            
            typedef void (^Animation)(void);
            FBTabBarControllerConfig *teacherTabbarConfig = [[FBTabBarControllerConfig alloc] init];
            UIViewController *rootViewController =teacherTabbarConfig.tabBarController;
            UIWindow *window = [UIApplication sharedApplication].keyWindow;
            rootViewController.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
            Animation animation = ^{
                BOOL oldState = [UIView areAnimationsEnabled];
                [UIView setAnimationsEnabled:NO];
                [UIApplication sharedApplication].keyWindow.rootViewController = rootViewController;
                [UIView setAnimationsEnabled:oldState];
            };
            
            [UIView transitionWithView:window duration:0.5f options:UIViewAnimationOptionTransitionCrossDissolve animations:animation completion:nil];
        }else{
            NSString *code =request.responseObject[@"code"];
            if([code isEqualToString:@"user.pwdError"]){
                [kWindow makeToast:KLanguage(@"密码修改请重新登陆") ];
                [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:@"userName"];
                [[NSUserDefaults standardUserDefaults] setObject:@"" forKey:@"passWord"];
                changeToken();
                return;
            }

        }
        
    } failure:^(__kindof YTKBaseRequest *_Nonnull request) {
       
    }];
}


// OC

NSArray *getPermissonValue(NSArray *nums1,NSArray*nums2){
    if (nums1.count > nums2.count) {
        return getPermissonValue(nums2,nums1);
    }
    NSMutableArray *resArray = [[NSMutableArray alloc] init];
    NSMutableDictionary *tableDict = [[NSMutableDictionary alloc] init];
    for (int i=0; i<nums1.count; i++) {
        NSString *tempkey = [NSString stringWithFormat:@"%@",nums1[i]];
        if (tableDict[tempkey] == nil) {
            tableDict[tempkey] = @1;
        }else{
            tableDict[tempkey] = [NSNumber numberWithInt:[tableDict[tempkey] intValue] + 1];
        }
    }
    for (int j=0; j<nums2.count; j++) {
        NSString *tempkey = [NSString stringWithFormat:@"%@",nums2[j]];
        if ([tableDict[tempkey] intValue] > 0) {
            tableDict[tempkey] = [NSNumber numberWithInt:[tableDict[tempkey] intValue] - 1];
            [resArray addObject:nums2[j]];
        }
    }
    return resArray;
}
BOOL isPermissionWithModel(NSString *operation,NSArray *arr){
    NSMutableArray *middArr = [NSMutableArray array];
    NSMutableArray *array = [NSMutableArray array];
    for(VULFileObjectModel *model in arr){
        NSArray *authArr = [model.auth componentsSeparatedByString:@","];
        if (array.count == 0) {
            [array addObjectsFromArray:authArr];
        }else{
            [middArr removeAllObjects];
            [middArr addObjectsFromArray:array];
            [array removeAllObjects];
            [array addObjectsFromArray:getPermissonValue(middArr,authArr)];
        }
    }
    NSDictionary *roleDic = [[NSUserDefaults standardUserDefaults] objectForKey:@"role"];
    NSString *roleStr = [NSString stringWithFormat:@"%@",roleDic[backKeyValueWithTitle(operation)]] ;
    NSString *value = backValueWithTitle(operation);
    
    VULFileObjectModel *model = arr[0];
   NSString *sourceID =  [[NSUserDefaults standardUserDefaults] objectForKey:@"sourceID"];
//桌面文件夹不能剪切\删除\重命名，移动到其它目录
    if(sourceID.integerValue == model.sourceID.integerValue ){
        if ([operation isEqualToString:KLanguage(@"剪切")] || [operation isEqualToString:KLanguage(@"删除")] || [operation isEqualToString:KLanguage(@"重命名")] ) {
            return NO;
        }
    }
    //只有角色权限
    if ([operation isEqualToString:KLanguage(@"收藏")] || [operation isEqualToString:KLanguage(@"标签")] || [operation isEqualToString:KLanguage(@"标签管理")] || [operation isEqualToString:KLanguage(@"彻底删除")]|| [operation isEqualToString:KLanguage(@"清空回收站")] || [operation isEqualToString:KLanguage(@"发送到桌面快捷方式")]) {
        return roleStr.boolValue;
    }
    NSString *shareLinkAllow = [[NSUserDefaults standardUserDefaults] objectForKey:@"shareLinkAllow"];
//    分享判断
    if ([operation isEqualToString:KLanguage(@"分享")] || [operation isEqualToString:KLanguage(@"编辑外链")] || [operation isEqualToString:KLanguage(@"取消分享")]) {
        if(!shareLinkAllow.boolValue){
            return shareLinkAllow.boolValue;
        }
    }
    if (value.integerValue == 0 ) {
        return YES;
    }
//    只要求有文档权限控制 角色权限一直是yes
    if ([operation isEqualToString:KLanguage(@"置顶")] || [operation isEqualToString:KLanguage(@"取消置顶")]) {
        roleStr = @"1";
    }
//targetType 是企业
    if (model.targetType.integerValue ==2 && roleStr.boolValue) {
        if ([array containsObject:value] || array.count == 0 ) {
            return YES;
        }
        return NO;
    }else{
        return roleStr.boolValue;
    }
    return NO;
}
NSString *backKeyValueWithTitle(NSString *operationTitle){
    if ([operationTitle isEqualToString:KLanguage(@"删除")] || [operationTitle isEqualToString:KLanguage(@"删除该版本")]  || [operationTitle isEqualToString:KLanguage(@"删除所有版本记录")] || [operationTitle isEqualToString:KLanguage(@"彻底删除")] ||  [operationTitle isEqualToString:KLanguage(@"清空回收站")]) {
        return @"explorer.remove";
    }
    if ([operationTitle isEqualToString:KLanguage(@"查看")]||[operationTitle isEqualToString:KLanguage(@"打开")] || [operationTitle isEqualToString:KLanguage(@"属性")]  || [operationTitle isEqualToString:KLanguage(@"动态")]) {
        return @"explorer.view";
    }
    if ([operationTitle isEqualToString:KLanguage(@"预览")] || [operationTitle isEqualToString:KLanguage(@"评论")]) {
        return @"explorer.view";
    }
    if ([operationTitle isEqualToString:KLanguage(@"下载")]) {
        return @"explorer.download";
    }
    if ([operationTitle isEqualToString:KLanguage(@"文件夹")] || [operationTitle isEqualToString:KLanguage(@"发送到桌面快捷方式")] ||[operationTitle isEqualToString:KLanguage(@"创建快捷方式")]) {
        return @"explorer.add";
    }
    if ([operationTitle isEqualToString:KLanguage(@"上传")] || [operationTitle isEqualToString:KLanguage(@"格式转换")] ) {
        return @"explorer.upload";
    }
    if ([operationTitle isEqualToString:KLanguage(@"创建压缩包")]) {
        return @"explorer.zip";
    }
    if ([operationTitle isEqualToString:KLanguage(@"解压到...")]) {
        return @"explorer.unzip";
    }
    if ([operationTitle isEqualToString:KLanguage(@"编辑")] || [operationTitle isEqualToString:KLanguage(@"评论")] || [operationTitle isEqualToString:KLanguage(@"管理权限")] || [operationTitle isEqualToString:KLanguage(@"重命名")]  || [operationTitle isEqualToString:KLanguage(@"上传新版本")] ) {
        return @"explorer.edit";
    }
 
    if ([operationTitle isEqualToString:KLanguage(@"复制")] || [operationTitle isEqualToString:KLanguage(@"剪切")] || [operationTitle isEqualToString:KLanguage(@"复制到此处")]||  [operationTitle isEqualToString:KLanguage(@"剪切到此处")] || [operationTitle isEqualToString:KLanguage(@"管理权限")]) {
        return @"explorer.move";
    }
    if ([operationTitle isEqualToString:KLanguage(@"分享")] || [operationTitle isEqualToString:KLanguage(@"取消分享")]) {
        return @"explorer.share";
    }
    if ([operationTitle isEqualToString:KLanguage(@"收藏")] || [operationTitle isEqualToString:KLanguage(@"标签")] || [operationTitle isEqualToString:KLanguage(@"标签管理")]) {
        return @"user.fav";
    }
    return @"";
}


NSString *backValueWithTitle(NSString *operationTitle){
    if ([operationTitle isEqualToString:KLanguage(@"删除")] || [operationTitle isEqualToString:KLanguage(@"删除该版本")]  || [operationTitle isEqualToString:KLanguage(@"删除所有版本记录")]) {
        return @"10";
    }
    if ([operationTitle isEqualToString:KLanguage(@"查看")]) {
        return @"1";
    }
    if ([operationTitle isEqualToString:KLanguage(@"预览")] || [operationTitle isEqualToString:KLanguage(@"属性")] ||[operationTitle isEqualToString:KLanguage(@"打开")]) {
        return @"3";
    }
    if ([operationTitle isEqualToString:KLanguage(@"下载")]) {
        return @"4";
    }
    if ([operationTitle isEqualToString:KLanguage(@"上传")] || [operationTitle isEqualToString:KLanguage(@"格式转换")]) {
        return @"5";
    }
    if ([operationTitle isEqualToString:KLanguage(@"创建压缩包")]) {
        return @"6";
    }
    if ([operationTitle isEqualToString:KLanguage(@"解压到...")]) {
        return @"7";
    }
    if ([operationTitle isEqualToString:KLanguage(@"编辑")] || [operationTitle isEqualToString:KLanguage(@"重命名")] || [operationTitle isEqualToString:KLanguage(@"上传新版本")] ) {
        return @"8";
    }
    if ([operationTitle isEqualToString:KLanguage(@"新建")]||[operationTitle isEqualToString:KLanguage(@"复制")] || [operationTitle isEqualToString:KLanguage(@"剪切")] || [operationTitle isEqualToString:KLanguage(@"创建快捷方式")] ||  [operationTitle isEqualToString:KLanguage(@"复制到此处")]||  [operationTitle isEqualToString:KLanguage(@"剪切到此处")]) {
        return @"9";
    }
    if ([operationTitle isEqualToString:KLanguage(@"文件夹")]) {
        return @"9";
    }
    if ([operationTitle isEqualToString:KLanguage(@"分享")] || [operationTitle isEqualToString:KLanguage(@"取消分享")]) {
        return @"11";
    }
    if ([operationTitle isEqualToString:KLanguage(@"评论")]) {
        return @"12";
    }
    if ([operationTitle isEqualToString:KLanguage(@"动态")]) {
        return @"13";
    }
    if ([operationTitle isEqualToString:KLanguage(@"管理权限")] || [operationTitle isEqualToString:KLanguage(@"置顶")] || [operationTitle isEqualToString:KLanguage(@"取消置顶")]) {
        return @"14";
    }

    return @"";
}
BOOL isZIP(NSString *operation){
    NSArray *zipArr = @[@"tar", @"zip", @"gzip", @"bz2", @"rar", @"7z", @"gz", @"iso", @"ar", @"bz", @"xz", @"arj"];
    if ([zipArr containsObject:operation]) {
        return YES;
    }
    return NO;

}
UIColor * MSColorFromHexString(NSString *hexString)
{
    // 去掉字符串中的"#"字符
    hexString = [hexString stringByReplacingOccurrencesOfString:@"#" withString:@""];
    
    if ([hexString length] != 6) {
        // 如果字符串长度不为6，则不是有效的16进制颜色字符串
        return [UIColor grayColor];
    }
    
    // 将16进制字符串转换为RGB颜色值
    unsigned int red, green, blue;
    NSRange range;
    range.length = 2;
    
    range.location = 0;
    [[NSScanner scannerWithString:[hexString substringWithRange:range]] scanHexInt:&red];
    
    range.location = 2;
    [[NSScanner scannerWithString:[hexString substringWithRange:range]] scanHexInt:&green];
    
    range.location = 4;
    [[NSScanner scannerWithString:[hexString substringWithRange:range]] scanHexInt:&blue];
    
    // 创建UIColor对象
    UIColor *color = [UIColor colorWithRed:red/255.0 green:green/255.0 blue:blue/255.0 alpha:1.0];
    return color;
}
BOOL isTreeOpen(NSString *operation){
  NSString *treeOpen = [[NSUserDefaults standardUserDefaults] objectForKey:@"treeOpen"];
    NSArray *arr = [treeOpen componentsSeparatedByString:@","];
    
    if([operation isEqualToString:@"menu"]){
//        myFav,my,information,recentDoc,shareLink,fileType,fileTag
        //收藏夹、个人空间、资讯、最近文档、外链分享、文件类型、标签
        if(![arr containsObject:@"myFav"]&& ![arr containsObject:@"recentDoc"]&&![arr containsObject:@"fileType"]&&![arr containsObject:@"shareLink"]){
            return NO;
        }
        return YES;
    }
    if([arr containsObject:operation]){
        return YES;
    }
    return NO;
    
}


BOOL isVideoOrMusic(NSString *operation){
    NSString *videoStr = @"mp4,flv,rm,rmvb,avi,mkv,mov,f4v,mpeg,mpg,vob,wmv,ogv,webm,3gp,mts,m2ts,m4v,mpe,3g2,asf,dat,asx,wvx,mpa";
    NSString *musicStr = @"mp3,wav,wma,m4a,ogg,omf,amr,aa3,flac,aac,cda,aif,aiff,mid,ra,ape";

    NSArray *videoArr = [videoStr componentsSeparatedByString:@","];
    NSArray *musicArr = [musicStr componentsSeparatedByString:@","];
    if([videoArr containsObject:operation] || [musicArr containsObject:operation]){
        return YES;
    }

    return NO;
}
