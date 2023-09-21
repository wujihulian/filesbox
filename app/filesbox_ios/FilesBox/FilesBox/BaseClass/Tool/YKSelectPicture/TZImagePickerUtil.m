//
//  TZImagePickerUtil.m
//  Tairong-Product
//
//  Created by yuekewei on 2019/8/12.
//

#import "TZImagePickerUtil.h"

@interface TZImagePickerUtil()<UIImagePickerControllerDelegate, UINavigationControllerDelegate, TZImagePickerControllerDelegate>


@property (nonatomic, strong) CLLocation *location;

@end

@implementation TZImagePickerUtil

- (instancetype)init {
    self = [super init];
    if (self) {
        _selectedPhotos = [NSMutableArray new];
        _selectedAssets = [NSMutableArray new];
    }
    return self;
}

+ (UIViewController * __nullable)topmostViewController {
    UIViewController *topViewController = [[[UIApplication sharedApplication] keyWindow] rootViewController];
    
    if (topViewController == nil) {
        return nil;
    }
    
    while (true) {
        if (topViewController.presentedViewController != nil) {
            topViewController = topViewController.presentedViewController;
        } else if ([topViewController isKindOfClass:[UINavigationController class]]) {
            UINavigationController *navi = (UINavigationController *)topViewController;
            topViewController = navi.topViewController;
        } else if ([topViewController isKindOfClass:[UITabBarController class]]) {
            UITabBarController *tab = (UITabBarController *)topViewController;
            topViewController = tab.selectedViewController;
        } else {
            break;
        }
    }
    
    return topViewController;
}

#pragma mark - 弹框
- (void)showAlertController {
    UIAlertController *alertVc = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:[UIDevice currentDevice].userInterfaceIdiom == UIUserInterfaceIdiomPad ? UIAlertControllerStyleAlert : UIAlertControllerStyleActionSheet];
    [alertVc addAction:[UIAlertAction actionWithTitle:@"拍照" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        // 去拍照
        [self takePhoto];
    }]];
    [alertVc addAction:[UIAlertAction actionWithTitle:@"去相册选择" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        // 相册选择图片
        [self pushTZImagePickerController];
    }]];
    [alertVc addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消" ) style:UIAlertActionStyleCancel handler:nil]];
    [[TZImagePickerUtil topmostViewController] presentViewController:alertVc animated:YES completion:nil];
}

- (void)configTZImagePickerController {
    _tZImagePicker = [[TZImagePickerController alloc] initWithMaxImagesCount:1 delegate:self];
    if (@available(iOS 15.0, *)) {
        UINavigationBarAppearance *appearance = [UINavigationBarAppearance new];
        [appearance configureWithOpaqueBackground];
        appearance.backgroundColor = [UIColor colorWithRed:(34/255.0) green:(34/255.0)  blue:(34/255.0) alpha:1.0];
        _tZImagePicker.navigationBar.standardAppearance = appearance;
        _tZImagePicker.navigationBar.scrollEdgeAppearance= _tZImagePicker.navigationBar.standardAppearance;
    }

    _tZImagePicker.allowTakeVideo = NO;
    _tZImagePicker.allowTakePicture = NO;
    _tZImagePicker.selectedAssets = _selectedAssets;
    if (_imagePickerCofing) {
        _imagePickerCofing(_tZImagePicker);
    }
}

#pragma mark - 相册
- (void)pushTZImagePickerController  {
    [self configTZImagePickerController];
    _tZImagePicker.modalPresentationStyle = UIModalPresentationFullScreen;
    [[TZImagePickerUtil topmostViewController] presentViewController:_tZImagePicker animated:YES completion:nil];
}

#pragma mark  TZImagePickerControllerDelegate
// 这个照片选择器会自己dismiss，当选择器dismiss的时候，会执行下面的代理方法
// 你也可以设置autoDismiss属性为NO，选择器就不会自己dismis了
// 如果isSelectOriginalPhoto为YES，表明用户选择了原图
// 你可以通过一个asset获得原图，通过这个方法：[[TZImageManager manager] getOriginalPhotoWithAsset:completion:]
// photos数组里的UIImage对象，默认是828像素宽，你可以通过设置photoWidth属性的值来改变它
- (void)imagePickerController:(TZImagePickerController *)picker didFinishPickingPhotos:(NSArray *)photos sourceAssets:(NSArray *)assets isSelectOriginalPhoto:(BOOL)isSelectOriginalPhoto {
    _selectedPhotos = [NSMutableArray arrayWithArray:photos];
    _selectedAssets = [NSMutableArray arrayWithArray:assets];
    
    [self selectImageCompletion];
}

// 如果用户选择了一个视频且allowPickingMultipleVideo是NO，下面的代理方法会被执行
// 如果allowPickingMultipleVideo是YES，
// 将会调用imagePickerController:didFinishPickingPhotos:sourceAssets:isSelectOriginalPhoto:
- (void)imagePickerController:(TZImagePickerController *)picker didFinishPickingVideo:(UIImage *)coverImage sourceAssets:(PHAsset *)asset {
    [_selectedPhotos addObject:coverImage];
    [_selectedAssets addObject:asset];
    [self selectImageCompletion];
}

// 如果用户选择了一个gif图片且allowPickingMultipleVideo是NO，下面的代理方法会被执行
// 如果allowPickingMultipleVideo是YES，
// 将会调用imagePickerController:didFinishPickingPhotos:sourceAssets:isSelectOriginalPhoto:
- (void)imagePickerController:(TZImagePickerController *)picker didFinishPickingGifImage:(UIImage *)animatedImage sourceAssets:(PHAsset *)asset {
    [_selectedPhotos addObject:animatedImage];
    [_selectedAssets addObject:asset];
    [self selectImageCompletion];
    
    [self selectImageCompletion];
}

#pragma mark - 相机
+ (void)authorizationForPhotoAtCompletionHandler:(void (^)(BOOL granted))handler {
    AVAuthorizationStatus authStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
    if (authStatus == AVAuthorizationStatusRestricted || authStatus == AVAuthorizationStatusDenied) {
        // 无相机权限 做一个友好的提示
        UIAlertController *alertVc = [UIAlertController alertControllerWithTitle:KLanguage(@"无法使用相机") message:KLanguage(@"请在iPhone的‘设置-隐私-相机’中允许访问相机") preferredStyle:UIAlertControllerStyleAlert];
        [alertVc addAction:[UIAlertAction actionWithTitle:KLanguage(@"设置") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            // 去设置界面，开启相机访问权限
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];
        }]];
        [alertVc addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消" ) style:UIAlertActionStyleCancel handler:nil]];
        [[TZImagePickerUtil topmostViewController] presentViewController:alertVc animated:YES completion:nil];
    }
    else if (authStatus == AVAuthorizationStatusNotDetermined) {
        // fix issue 466, 防止用户首次拍照拒绝授权时相机页黑屏
        [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo completionHandler:^(BOOL granted) {
            if (granted) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    dispatch_async(dispatch_get_main_queue(), ^{
                        if (handler) {
                            handler(YES);
                        }
                    });
                });
            }
        }];
    }
    else if ([PHPhotoLibrary authorizationStatus] == 2) {
        // 已被拒绝，没有相册权限，将无法保存拍的照片
        UIAlertController *alertVc = [UIAlertController alertControllerWithTitle:KLanguage(@"无法访问相册") message:KLanguage(@"请在iPhone的‘设置-隐私-相册’中允许访问相册") preferredStyle:UIAlertControllerStyleAlert];
        [alertVc addAction:[UIAlertAction actionWithTitle:KLanguage(@"设置") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            // 去设置界面，开启相机访问权限
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];
        }]];
        [alertVc addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消" ) style:UIAlertActionStyleCancel handler:nil]];
        [[TZImagePickerUtil topmostViewController] presentViewController:alertVc animated:YES completion:nil];
    }
    else if ([PHPhotoLibrary authorizationStatus] == 0) {
        // 未请求过相册权限
        [[TZImageManager manager] requestAuthorizationWithCompletion:^{
            dispatch_async(dispatch_get_main_queue(), ^{
                if (handler) {
                    handler(YES);
                }
            });
        }];
    }
    else {
        dispatch_async(dispatch_get_main_queue(), ^{
            if (handler) {
                handler(YES);
            }
        });
    }
}

- (void)takePhoto {
    [TZImagePickerUtil authorizationForPhotoAtCompletionHandler:^(BOOL granted) {
        if (granted) {
            dispatch_async(dispatch_get_main_queue(), ^{
                [self pushImagePickerController];
            });
        }
    }];
}

// 调用相机
- (void)pushImagePickerController {
     [self configTZImagePickerController];
    // 提前定位
    __weak typeof(self) weakSelf = self;
//    [[TZLocationManager manager] startLocationWithSuccessBlock:^(NSArray<CLLocation *> *locations) {
//        __strong typeof(weakSelf) strongSelf = weakSelf;
//        strongSelf.location = [locations firstObject];
//    } failureBlock:^(NSError *error) {
//        __strong typeof(weakSelf) strongSelf = weakSelf;
//        strongSelf.location = nil;
//    }];
    
    UIImagePickerControllerSourceType sourceType = UIImagePickerControllerSourceTypeCamera;
    if ([UIImagePickerController isSourceTypeAvailable: UIImagePickerControllerSourceTypeCamera]) {
        self.imagePickerVc.sourceType = sourceType;
        if (self.imagePickerVc.mediaTypes.count == 0) {
            NSMutableArray *mediaTypes = [NSMutableArray arrayWithObjects:(NSString *)kUTTypeImage, nil];
            if (_tZImagePicker.allowTakeVideo) {
                // 允许拍视频
                [mediaTypes addObject:(NSString *)kUTTypeMovie];
            }
            self.imagePickerVc.mediaTypes = mediaTypes;
        }        
        self.imagePickerVc.modalPresentationStyle = UIModalPresentationFullScreen;
        [[TZImagePickerUtil topmostViewController] presentViewController:self.imagePickerVc animated:YES completion:nil];
    } else {
        NSLog(@"模拟器中无法打开照相机,请在真机中使用");
    }
}

#pragma mark  UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController*)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    if (self.selectedPhotos.count >= self.tZImagePicker.maxImagesCount) {
        [picker dismissViewControllerAnimated:YES completion:^{
            UIAlertController *alertController = [UIAlertController alertControllerWithTitle:[NSString stringWithFormat:@"最多只能选择%zd张照片/视频",self.tZImagePicker.maxImagesCount] message:nil preferredStyle:UIAlertControllerStyleAlert];
            [alertController addAction:[UIAlertAction actionWithTitle:[NSBundle tz_localizedStringForKey:@"OK"] style:UIAlertActionStyleDefault handler:nil]];
            [[TZImagePickerUtil topmostViewController] presentViewController:alertController animated:YES completion:nil];
        }];
        return;
    }
    //拍照或拍视频完成
    [picker dismissViewControllerAnimated:YES completion:nil];
    NSString *type = [info objectForKey:UIImagePickerControllerMediaType];
    
    [self.tZImagePicker showProgressHUD];
    if ([type isEqualToString:@"public.image"]) {
        UIImage *originalImage = [info objectForKey:UIImagePickerControllerOriginalImage];
        UIImage *selectImage = originalImage;
        
        if (picker.allowsEditing) {
            selectImage = [info objectForKey:UIImagePickerControllerEditedImage];
        }
        // 保存图片，获取到asset
        [[TZImageManager manager] savePhotoWithImage:originalImage
                                          completion:^(PHAsset *asset, NSError *error) {
            [self.tZImagePicker hideProgressHUD];
            if (error) {
                NSLog(@"图片保存失败 %@",error);
            }
            else {
                TZAssetModel *assetModel = [[TZImageManager manager] createModelWithAsset:asset];
                [self takePhotoCompletionWithAddedAsset:assetModel.asset image:selectImage];
            }
        }];
    }
    else if ([type isEqualToString:@"public.movie"]) {
        NSURL *videoUrl = [info objectForKey:UIImagePickerControllerMediaURL];
        if (videoUrl) {
            [[TZImageManager manager] saveVideoWithUrl:videoUrl
                                              location:self.location
                                            completion:^(PHAsset *asset, NSError *error) {
                [self.tZImagePicker hideProgressHUD];
                if (!error) {
                    TZAssetModel *assetModel = [[TZImageManager manager] createModelWithAsset:asset];
                    [[TZImageManager manager] getPhotoWithAsset:assetModel.asset
                                                     completion:^(UIImage *photo, NSDictionary *info, BOOL isDegraded) {
                        if (!isDegraded && photo) {
                            [self takePhotoCompletionWithAddedAsset:assetModel.asset image:photo];
                        }
                    }];
                }
            }];
        }
    }
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    if ([picker isKindOfClass:[UIImagePickerController class]]) {
        [picker dismissViewControllerAnimated:YES completion:nil];
    }
}

#pragma mark - Other or Action
- (void)takePhotoCompletionWithAddedAsset:(PHAsset *)asset image:(UIImage *)image {
    [_selectedPhotos addObject:image];
    [_selectedAssets addObject:asset];
    [self selectImageCompletion];
}

- (void)selectImageCompletion {
    if (_selectedBolck) {
        _selectedBolck();
    }
}

- (void)removeImageForIndex:(NSInteger)index {
    if (index < self.selectedPhotos.count) {
        [self.selectedPhotos removeObjectAtIndex:index];
        [self.selectedAssets removeObjectAtIndex:index];
    }
}

#pragma mark - 懒加载
- (UIImagePickerController *)imagePickerVc {
    if (!_imagePickerVc) {
        _imagePickerVc = [[UIImagePickerController alloc] init];
        _imagePickerVc.delegate = self;
//        _imagePickerVc.navigationBar.translucent = NO;
        _imagePickerVc.automaticallyAdjustsScrollViewInsets = YES;
        _imagePickerVc.sourceType = UIImagePickerControllerSourceTypeCamera;
    }
    return _imagePickerVc;
}
@end
