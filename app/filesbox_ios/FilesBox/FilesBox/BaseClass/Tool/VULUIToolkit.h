//
//  TPLUIToolkit.h
//
//  Created by 李新波 on 21-3-15.
//  Copyright (c) 2021年 Tai Ping. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CommonCrypto/CommonCryptor.h>
#import <CommonCrypto/CommonDigest.h>
#import <ifaddrs.h>
#import <arpa/inet.h>
#import <net/if.h>
#import "ChunkUploader.h"
#import "VULRequestModel.h"
#import "VULFileModel.h"

// 数字标记颜色
extern NSMutableAttributedString *  setRichNumberWithLabel(NSString *allStr ,UIColor *color) ;
// 今天 昨天 时间
extern NSString * distanceTimeWithBeforeTime(double beTime);
// 根据类型选择本地办公图片 word ppt zip 等
extern NSString * imageNameWithSourceSuffix(NSString *fileType);

extern NSString * isEmpty(NSString *str);
extern NSString * isEmptyIp(NSString *str);
extern NSString * isEmptyPre(NSString *str);



extern NSString * isChange(NSString *str);
extern NSString * removeSuffix(NSString *str);
extern NSString * resultsVdieoUrl(NSString *str);

extern NSMutableAttributedString *  setRichNumberWithLabelOrFont(NSString *allStr ,UIColor *color,UIFont *font) ;

extern NSString *msTimeChange (NSInteger ms);
extern NSDictionary *turnStringToDictionary(NSString *turnString);

extern NSString * resultsUrl(NSString *str);


extern NSMutableAttributedString *  setRichNumberWithLabelOrFontOrStr(NSString *allStr ,UIColor *color,UIFont *font,NSString *str) ;

extern NSString *backTimeChange (NSInteger ms);


extern NSString *changeName (NSString *name);

extern NSString *backUrlWithBusType (NSString *conentId,NSString *busType);
extern NSString  *getbusType(NSString *infotype);
extern NSString  *getInfoType(NSString *busType);


extern NSString *  imageUrlNameWithSourceSuffix(NSString *fileType);
extern NSString *  imageNameWithUrl(NSString *fileType);

extern BOOL   isLocalImageNameWithUrl(NSString *fileType);

extern NSString * changeImageUrl(NSString *url);



extern NSString * changeUrl(NSString *url);
extern NSString * changeClearKF(NSString *url);
extern  NSString * getFileNewMD5WithPath(NSString* path,size_t chunkSizeForReadingData);

extern  NSString *filePath(NSString *fileName);
extern  NSString *backBaseUrl(NSString *fileName);
extern  NSString *backImageBgUrl(NSInteger layout,NSInteger index);
extern  UIColor *backColorWithIndex(NSInteger index);
extern  NSString *backPreviewString(NSString *type,NSInteger count);

extern  NSString *hexadecimalFromUIColor(UIColor *color);


extern  BOOL isPartner();

extern UIImage *backCaptureImageFromView(UIView *view);
extern UIImage  *compressionSmallImage(UIImage*image ,CGSize newSize);
extern  void setWeixForKey(NSString *appkey,NSString *appSecret);
extern UIImage  *createImageWithColor(UIColor*color);
extern NSMutableDictionary *getURLParameters(NSString *url);



extern  NSString *zeroWtihString(NSString *str);
extern  NSString *typeWtihSignState(NSString *str);
extern  float chooseScreenWithType(NSInteger index);
extern  UIImage *getLocalImageWithFileType(NSString *icon);
extern  NSString *fileImageWithUrl(NSString *url,NSInteger size,NSString *fileType);
extern  void saveUploadFileWithModel(UBUploadModel *model);
extern  void getUploadFileWithModel();
extern  UIColor *getColorWithGgb(NSString *tgb);
extern  UIImage *getImageWithColor(UIColor *color);
extern  NSMutableDictionary *backBlockAndFileType(NSString *title);
extern  NSString *backTitleWithBlock(NSString *block);
extern NSString *formattedFileSize(long bytes);
extern NSString *getTimeWithTime(NSString *time);
extern NSString *getTypeWithTitle(NSString *title);
extern NSString *getSortFieldWithTitle(NSString *title);
extern void changeToken();
extern  BOOL isPermissionWithModel(NSString *operation,NSArray *arr);
extern  NSString *backValueWithTitle(NSString *operationTitle);
extern  NSString *backKeyValueWithTitle(NSString *operationTitle);
extern  NSArray *getPermissonValue(NSArray *list,NSArray*list2);
extern  BOOL isZIP(NSString *operation);

extern UIColor * MSColorFromHexString(NSString *hexColor);
extern  BOOL isTreeOpen(NSString *operation);
extern  BOOL isVideoOrMusic(NSString *operation);


