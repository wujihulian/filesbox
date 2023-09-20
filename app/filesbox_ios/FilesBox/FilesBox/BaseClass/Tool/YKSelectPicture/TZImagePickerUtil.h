//
//  TZImagePickerUtil.h
//  Tairong-Product
//
//  Created by yuekewei on 2019/8/12.
//

#import <Foundation/Foundation.h>
#import "TZImagePickerController.h"
#import "TZImageManager.h"
NS_ASSUME_NONNULL_BEGIN

@interface TZImagePickerUtil : NSObject

@property (nonatomic, copy) void(^selectedBolck)(void);
@property (nonatomic, copy) void(^imagePickerCofing)(TZImagePickerController *pickerController);
@property (nonatomic, strong) TZImagePickerController *tZImagePicker;
@property (nonatomic, strong) UIImagePickerController *imagePickerVc;
@property (nonatomic, strong) NSMutableArray *selectedPhotos;
@property (nonatomic, strong) NSMutableArray *selectedAssets;

- (void)showAlertController;

/// 相机拍照
- (void)takePhoto;

/// 手机相册
- (void)pushTZImagePickerController ;

- (void)removeImageForIndex:(NSInteger)index ;


+ (void)authorizationForPhotoAtCompletionHandler:(void (^)(BOOL granted))handler;
@end

NS_ASSUME_NONNULL_END
