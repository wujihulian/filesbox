//
//  FBTakePhotoVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/8/8.
//

#import "FBTakePhotoVC.h"
#import <CoreLocation/CoreLocation.h>
#import <CoreMotion/CoreMotion.h>
#import <AVFoundation/AVFoundation.h>
#import <ImageIO/ImageIO.h>

@interface FBTakePhotoVC ()<CLLocationManagerDelegate>
{
    CLLocationDegrees  latitude;
    CLLocationDegrees  longitude;

}
@property (nonatomic, strong) CLLocationManager *locationManager;
@property (nonatomic, strong) UILabel *timeLabel;
@property (nonatomic, strong) UILabel *locationLabel;
@property (nonatomic, strong) AVCaptureSession *captureSession;
@property (nonatomic, strong) AVCaptureStillImageOutput *stillImageOutput;
@property (nonatomic, strong) AVCaptureVideoPreviewLayer *previewLayer;
@property (nonatomic, assign) BOOL isCapturing;
@property (nonatomic, assign) BOOL islocation;
@property (nonatomic, retain) UIView *topView;
@property (nonatomic, retain) UIButton *takePhotoButton;
@property (nonatomic, assign) BOOL isUsingFrontCamera;
@property (nonatomic, retain) UIButton *changeCameraButton;
@property (nonatomic, assign) AVCaptureDevicePosition currentCameraPosition;
@property (nonatomic, assign) BOOL isTorchOn; // 新增散光灯状态
@property (nonatomic, retain) UIButton *flashButton;
@property (nonatomic, retain) UIView *lineV;
@property (nonatomic, retain) UIImageView *locationImageV;
@property (nonatomic, retain) UIButton *cancelBtn;
@property (nonatomic, retain) UIImage *capturedImage;
@property (nonatomic, assign) NSInteger index;


@end

@implementation FBTakePhotoVC {
    CMMotionManager *_motionManager;
}


- (void)viewDidLoad {
    [super viewDidLoad];
    self.index = 0;
    self.currentCameraPosition = AVCaptureDevicePositionBack;
    AVAuthorizationStatus cameraAuthorizationStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];

    if (cameraAuthorizationStatus == AVAuthorizationStatusAuthorized) {
        // 已经授权可以使用相机
        NSLog(@"已授权使用相机");
    } else if (cameraAuthorizationStatus == AVAuthorizationStatusDenied || cameraAuthorizationStatus == AVAuthorizationStatusRestricted) {
        // 无相机权限 做一个友好的提示
        UIAlertController *alertVc = [UIAlertController alertControllerWithTitle:KLanguage(@"无法使用相机") message:KLanguage(@"请在iPhone的‘设置-隐私-相机’中允许访问相机") preferredStyle:UIAlertControllerStyleAlert];
        [alertVc addAction:[UIAlertAction actionWithTitle:KLanguage(@"设置") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            // 去设置界面，开启相机访问权限
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];
        }]];
        [alertVc addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消" ) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            [VULQueue executeInMainQueue:^{
                [self.navigationController popViewControllerAnimated:YES];
          
            }];
        
        }]];
        [self presentViewController:alertVc animated:YES completion:nil];
        return;
    } else if (cameraAuthorizationStatus == AVAuthorizationStatusNotDetermined) {
        // 用户还未作出授权选择
        NSLog(@"用户未决定");
        [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo completionHandler:^(BOOL granted) {
            if (granted) {
                // 用户授权使用相机
                NSLog(@"用户授权使用相机");
                [VULQueue executeInMainQueue:^{
                    [self startLocationUpdates];
                    [self startCameraPreview];
                    [self setupUI];
                    
                    // 初始化 Core Motion 管理器
                    _motionManager = [[CMMotionManager alloc] init];
                    _motionManager.deviceMotionUpdateInterval = 0.24; // 更新频率，可根据需求调整
                    // 开始监听设备方向变化
                    [_motionManager startDeviceMotionUpdatesToQueue:[NSOperationQueue mainQueue] withHandler:^(CMDeviceMotion *motion, NSError *error) {
                        if (error) {
//                            NSLog(@"Error: %@", error);
                        } else {
                            [self handleDeviceOrientation:motion.attitude];
                        }
                    }];
                }];
                
            
            } else {
                // 用户拒绝授权使用相机
                NSLog(@"用户拒绝授权使用相机");
                [VULQueue executeInMainQueue:^{
                    [self.navigationController popViewControllerAnimated:YES];
              
                }];
                
            }
        }];
        return;
    }
    [self startLocationUpdates];
    [self startCameraPreview];
    [self setupUI];
    
    // 初始化 Core Motion 管理器
    _motionManager = [[CMMotionManager alloc] init];
    _motionManager.deviceMotionUpdateInterval = 0.24; // 更新频率，可根据需求调整
    // 开始监听设备方向变化
    [_motionManager startDeviceMotionUpdatesToQueue:[NSOperationQueue mainQueue] withHandler:^(CMDeviceMotion *motion, NSError *error) {
        if (error) {
            NSLog(@"Error: %@", error);
        } else {
            [self handleDeviceOrientation:motion.attitude];
        }
    }];
    
}

- (void)dealloc {
    // 停止监听设备方向变化
    [_motionManager stopDeviceMotionUpdates];
}
-(void)viewDidDisappear:(BOOL)animated{
    [_motionManager stopDeviceMotionUpdates];
    
}

- (void)handleDeviceOrientation:(CMAttitude *)attitude {
    // 根据设备姿态进行判断，attitude.roll 代表左右倾斜，attitude.pitch 代表前后倾斜
    if (attitude.roll > -M_PI_4 && attitude.roll < M_PI_4) {
        NSLog(@"设备方向：竖直向上");
        [self.topView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(K_StatusBar_Height);
            make.left.right.mas_equalTo(0);
            make.height.mas_equalTo(fontAuto(80));
        }];
        self.topView.layer.anchorPoint = CGPointMake(0.5, 0.5);
        
        CGAffineTransform rotationTransform = CGAffineTransformMakeRotation(0); // 90度
        self.topView.transform = rotationTransform;
        self.index = 1;
        
    } else if (attitude.roll > M_PI_4 && attitude.roll < 3 * M_PI_4) {
        NSLog(@"设备方向：左侧横向");
        self.index = 2;
        
        // 执行旋转变换
        // 设置旋转中心为左上角
        //        self.topView.layer.anchorPoint = CGPointMake(0, 0);
        [self.topView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.bottom.mas_equalTo(-fontAuto(90)-K_BottomBar_Height-VULSCREEN_WIDTH/2+fontAuto(80));
            make.left.mas_equalTo(- VULSCREEN_WIDTH/2+fontAuto(40));
            make.width.mas_equalTo(VULSCREEN_WIDTH);
            make.height.mas_equalTo(fontAuto(80));
            
        }];
        
        CGAffineTransform rotationTransform = CGAffineTransformMakeRotation(-M_PI_2); // 90度
        self.topView.transform = rotationTransform;
        
        
    } else if (attitude.roll < -M_PI_4 && attitude.roll > -3 * M_PI_4) {
        NSLog(@"设备方向：右侧横向");
        // 执行旋转变换
        self.index = 3;
        
        [self.topView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(VULSCREEN_WIDTH/2-fontAuto(40)+K_StatusBar_Height);
            make.left.mas_equalTo(VULSCREEN_WIDTH/2-fontAuto(40));
            make.width.mas_equalTo(VULSCREEN_WIDTH);
            make.height.mas_equalTo(fontAuto(80));
            
        }];
        CGAffineTransform rotationTransform = CGAffineTransformMakeRotation(M_PI_2); // 90度
        self.topView.transform = rotationTransform;
    } else {
        self.index = 4;
        
        NSLog(@"设备方向：竖直向下");
    }
}
- (void)setupUI {
    // 添加时间标签
    [self.view addSubview:self.topView];
    [self.topView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(K_StatusBar_Height);
        make.left.right.mas_equalTo(0);
        make.height.mas_equalTo(fontAuto(80));
    }];
    
    [self.topView addSubview:self.timeLabel];
    [self.topView addSubview:self.lineV];
    [self.topView addSubview:self.locationImageV];
    [self.lineV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(fontAuto(5));
        make.bottom.mas_equalTo(-fontAuto(5));
        make.left.mas_equalTo(fontAuto(10));
        make.width.mas_equalTo(2);
    }];
    [self.timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(fontAuto(0));
        make.left.mas_equalTo(self.lineV.mas_right).offset(10);
        make.right.mas_equalTo(-fontAuto(10));
        make.height.mas_equalTo(fontAuto(40));
        
    }];
    [self.topView addSubview:self.locationLabel];
    
    [self.locationImageV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self.locationLabel.mas_centerY);
        make.left.mas_equalTo(self.lineV.mas_right).offset(10);
        make.width.mas_equalTo(14);
    }];
    [self.locationLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.timeLabel .mas_bottom).offset(fontAuto(0));
        make.left.mas_equalTo(self.locationImageV.mas_right).offset(5);
        make.right.mas_equalTo(-fontAuto(10));
        make.height.mas_equalTo(fontAuto(40));
    }];
    
    // 添加拍照按钮
    _takePhotoButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [_takePhotoButton setImage:VULGetImage(@"icon_photo") forState:UIControlStateNormal];
    [_takePhotoButton addTarget:self action:@selector(takePhotoButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    _takePhotoButton.frame = CGRectMake(100, CGRectGetMaxY(self.view.bounds) - 60, 120, 40);
    [self.view addSubview:_takePhotoButton];
    [self.takePhotoButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.mas_equalTo(-(K_BottomBar_Height+20));
        make.centerX.mas_equalTo(self.view.mas_centerX);
        make.height.width.mas_equalTo(fontAuto(50));
    }];
    
    // 添加拍照按钮
    _flashButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [_flashButton setImage:VULGetImage(@"icon_photo_turn") forState:UIControlStateNormal];
    
    [_flashButton addTarget:self action:@selector(changeCameraButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:_flashButton];
    
    [self.flashButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(-fontAuto(10));
        make.centerY.mas_equalTo(self.takePhotoButton.mas_centerY);
        make.width.mas_equalTo(fontAuto(58));
        make.height.mas_equalTo(fontAuto(50));
    }];
    // 添加拍照按钮
    _changeCameraButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [_changeCameraButton setImage:VULGetImage(@"icon_light_close") forState:UIControlStateNormal];
    [_changeCameraButton setImage:VULGetImage(@"icon_light_open") forState:UIControlStateSelected];
    
    [_changeCameraButton addTarget:self action:@selector(toggleFlash:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:_changeCameraButton];
    [self.changeCameraButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(self.flashButton.mas_left).offset(-fontAuto(0));
        make.centerY.mas_equalTo(self.takePhotoButton.mas_centerY);
        make.width.mas_equalTo(fontAuto(72));
        make.height.mas_equalTo(fontAuto(75));
    }];
    _cancelBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [_cancelBtn setTitle:KLanguage(@"取消") forState:UIControlStateNormal];
    
    [_cancelBtn addTarget:self action:@selector(clickCancel) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:_cancelBtn];
    [self.cancelBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(fontAuto(10));
        make.centerY.mas_equalTo(self.takePhotoButton.mas_centerY);
        make.width.mas_equalTo(fontAuto(80));
        make.height.mas_equalTo(fontAuto(40));
    }];
    
}
-(void)clickCancel{
    if([self.cancelBtn.titleLabel.text isEqualToString:KLanguage(@"取消") ]){
        [self.navigationController popViewControllerAnimated:YES];
        
    }else{
        [_takePhotoButton setImage:VULGetImage(@"icon_photo") forState:UIControlStateNormal];
        [_cancelBtn setTitle:KLanguage(@"取消") forState:UIControlStateNormal];
        self.isCapturing = NO;
        _changeCameraButton.hidden =_flashButton.hidden = NO;
        [_motionManager startDeviceMotionUpdatesToQueue:[NSOperationQueue mainQueue] withHandler:^(CMDeviceMotion *motion, NSError *error) {
            if (error) {
                NSLog(@"Error: %@", error);
            } else {
                [self handleDeviceOrientation:motion.attitude];
            }
        }];
        [self.captureSession startRunning]; // 暂停预览
        
    }
}
- (void)toggleFlash:(UIButton *)sender {
    sender.selected = !sender.selected;
    
    AVCaptureDevice *device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    if (device.hasTorch) {
        [device lockForConfiguration:nil];
        if (self.isTorchOn) {
            [device setTorchMode:AVCaptureTorchModeOff];
        } else {
            [device setTorchMode:AVCaptureTorchModeOn];
        }
        [device unlockForConfiguration];
        self.isTorchOn = !self.isTorchOn;
    }
}
-(void)changeCameraButtonTapped:(UIButton *)sender{
    [self switchCamera];
}

- (void)switchCamera {
    [self.captureSession beginConfiguration];
    
    AVCaptureInput *currentCameraInput = [self.captureSession.inputs firstObject];
    [self.captureSession removeInput:currentCameraInput];
    
    AVCaptureDevicePosition newCameraPosition = AVCaptureDevicePositionUnspecified;
    if (self.currentCameraPosition == AVCaptureDevicePositionBack) {
        newCameraPosition = AVCaptureDevicePositionFront;
    } else if (self.currentCameraPosition == AVCaptureDevicePositionFront) {
        newCameraPosition = AVCaptureDevicePositionBack;
    }
    
    AVCaptureDevice *newCameraDevice = [self cameraWithPosition:newCameraPosition];
    if (newCameraDevice) {
        AVCaptureDeviceInput *newCameraInput = [AVCaptureDeviceInput deviceInputWithDevice:newCameraDevice error:nil];
        [self.captureSession addInput:newCameraInput];
        self.currentCameraPosition = newCameraPosition;
    }
    
    [self.captureSession commitConfiguration];
}

- (AVCaptureDevice *)cameraWithPosition:(AVCaptureDevicePosition)position {
    NSArray *devices = [AVCaptureDevice devicesWithMediaType:AVMediaTypeVideo];
    for (AVCaptureDevice *device in devices) {
        if (device.position == position) {
            return device;
        }
    }
    return nil;
}

-(UILabel *)locationLabel{
    if(!_locationLabel){
        _locationLabel = [UILabel new];
        _locationLabel.textColor = [UIColor whiteColor];
        _locationLabel.numberOfLines = 2;
        _locationLabel.font =[UIFont systemFontOfSize:14];
        //        _locationLabel.text = [self getCurrentTime];
    }
    return _locationLabel;
}
-(UILabel *)timeLabel{
    if(!_timeLabel){
        _timeLabel = [UILabel new];
        _timeLabel.textColor = [UIColor whiteColor];
        NSString *labelText = [self getCurrentTime];
        NSMutableAttributedString *attributedText = [[NSMutableAttributedString alloc] initWithString:labelText];
        // 设置前面几个字体大一点
        UIFont *largeFont = [UIFont systemFontOfSize:25]; // 设置大号字体大小
        [attributedText addAttribute:NSFontAttributeName value:largeFont range:NSMakeRange(0, 5)]; // 根据需要调整范围
        // 设置后面字体小一点
        UIFont *smallFont = [UIFont systemFontOfSize:14]; // 设置小号字体大小
        [attributedText addAttribute:NSFontAttributeName value:smallFont range:NSMakeRange(5, labelText.length - 5)]; // 根据需要调整范围
        
        _timeLabel.attributedText = attributedText;
    }
    return _timeLabel;
}
-(UIView *)topView{
    if(!_topView){
        _topView = [UIView new];
        _topView.backgroundColor = UIColor.clearColor;
    }
    return _topView;
}
-(UIView *)lineV{
    if(!_lineV){
        _lineV = [UIView new];
        _lineV.backgroundColor = UIColor.whiteColor;
    }
    return _lineV;
}
-(UIImageView *)locationImageV{
    if(!_locationImageV){
        _locationImageV = [UIImageView new];
        _locationImageV.image = VULGetImage(@"icon_location");
    }
    return _locationImageV;
}

- (void)startLocationUpdates {
    self.locationManager = [[CLLocationManager alloc] init];
    self.locationManager.delegate = self;
    [self.locationManager requestWhenInUseAuthorization];
    [self.locationManager startUpdatingLocation];
}


- (void)startCameraPreview {
    self.captureSession = [[AVCaptureSession alloc] init];
    AVCaptureDevice *device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    
    NSError *error = nil;
    AVCaptureDeviceInput *input = [AVCaptureDeviceInput deviceInputWithDevice:device error:&error];
    if (input) {
        [self.captureSession addInput:input];
        self.stillImageOutput = [[AVCaptureStillImageOutput alloc] init];
        [self.captureSession addOutput:self.stillImageOutput];
        
        self.previewLayer = [AVCaptureVideoPreviewLayer layerWithSession:self.captureSession];
        self.previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
        self.previewLayer.frame = self.view.bounds;
        [self.view.layer addSublayer:self.previewLayer];
        
        [self.captureSession startRunning];
    } else {
        NSLog(@"Error setting up camera input: %@", [error localizedDescription]);
    }
    
}

- (NSString *)getCurrentTime {
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"HH:mm yyyy-MM-dd";
    return [formatter stringFromDate:[NSDate date]];
}

- (void)takePhotoButtonTapped:(id)sender {
    if (!self.isCapturing) {
        self.isCapturing = YES;
        
        [_motionManager stopDeviceMotionUpdates];
        
        
        AVCaptureConnection *connection = [self.stillImageOutput connectionWithMediaType:AVMediaTypeVideo];
        if (connection) {
            [self.stillImageOutput captureStillImageAsynchronouslyFromConnection:connection completionHandler:^(CMSampleBufferRef imageDataSampleBuffer, NSError *error) {
                if (imageDataSampleBuffer) {
                    NSData *imageData = [AVCaptureStillImageOutput jpegStillImageNSDataRepresentation:imageDataSampleBuffer];
                    _capturedImage = [UIImage imageWithData:imageData];
                    
                    [self.captureSession stopRunning]; // 暂停预览
                    // 将图片保存到相册
                    [_takePhotoButton setImage:VULGetImage(@"icon_photo1") forState:UIControlStateNormal];
                    [_cancelBtn setTitle:KLanguage(@"重拍") forState:UIControlStateNormal];
                    _changeCameraButton.hidden =_flashButton.hidden = YES;
                    
                    
                } else {
                    NSLog(@"Error capturing image: %@", [error localizedDescription]);
                }
                
                //                   self.isCapturing = NO; // 拍照完成后允许继续预览
                //                   [self.captureSession startRunning]; // 恢复预览
                
            }];
        }
    }else{
        
        
        UIImageView *image = [UIImageView new];
        image.image = _capturedImage;
        image.frame = CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_HEIGHT);
        [image addSubview:self.topView];
        UIGraphicsBeginImageContextWithOptions(image.bounds.size, NO, 0.0);
        [image.layer renderInContext:UIGraphicsGetCurrentContext()];
        UIImage *image1 = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
    
        [self showWaitHudWithString:nil];
        if(self.index ==2){

            if(self.uploadImage){
                [self saveImageWithData:[self addLocationToImage: [self rotateImage:image1] latitude:latitude longitude:longitude]];

            }
        }else
        if(self.index ==3){
          
            [self saveImageWithData:[self addLocationToImage: [self rotateImageCounterclockwise:image1] latitude:latitude longitude:longitude]];


        }else{
            [self saveImageWithData:[self addLocationToImage: image1 latitude:latitude longitude:longitude]];
        }
       
       
        
//        [self.navigationController popViewControllerAnimated:YES];
    }
}
//转图片会丢失 所以直接上传图片
-(void)saveImageWithData:(NSData *)data{
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    //设置响应文件类型为JSON类型
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    manager.requestSerializer = [AFHTTPRequestSerializer serializer];
    manager.responseSerializer.acceptableContentTypes = nil;
    //设置timeout
    [manager.requestSerializer setTimeoutInterval:20.0];
    //设置请求头类型
    [manager.requestSerializer setValue:@"form/data" forHTTPHeaderField:@"Content-Type"];
    NSString *token = [VULRealmDBManager getLocalToken];
    [manager.requestSerializer setValue:token forHTTPHeaderField:@"token"];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@"cloud" forKey:@"busType"];
    [params setObject:[self calculateMD5HashForData:data] forKey:@"hashMd5"];
//    [params setObject:@0 forKey:@"chunk"];
    [params setObject:self.sourceID forKey:@"sourceID"];
//    [params setObject:@(data.length) forKey:@"chunkSize"];
//    [params setObject:@1 forKey:@"chunks"];
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy_MM_dd_HH_mm_ss"];
    NSString *key = [formatter stringFromDate:[NSDate date]];
    NSString *posturl = [NSString stringWithFormat:@"%@%@",ChooseUrl,@"api/disk/upload"];

    [manager POST:posturl parameters:params constructingBodyWithBlock:^(id<AFMultipartFormData>  _Nonnull formData) {
        [formData appendPartWithFileData:data name:@"file" fileName: [NSString stringWithFormat:@"%@.jpg",key]  mimeType:@"jpg"];

    } progress:^(NSProgress * _Nonnull uploadProgress) {
        NSLog(@"上传进度: %f", uploadProgress.fractionCompleted);
      
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"成功返回: %@", responseObject);
        
        NSString *sucess = responseObject[@"success"];
        if(sucess.boolValue){
            if(self.saveAndRefreshBlock){
                self.saveAndRefreshBlock();
            }
            _takePhotoButton.hidden = _changeCameraButton.hidden =_takePhotoButton.hidden = YES;
            [self.navigationController popViewControllerAnimated:YES];
        }else{
            [self makeToast:responseObject[@"message"]];
        }
        [self dissmissHudView];
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"上传失败: %@", error);
        NSData *data = error.userInfo[@"com.alamofire.serialization.response.error.data"];
        NSString *str = [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding];
        NSLog(@"服务器的错误原因:%@", str);
        [self makeToast:str];
        [self dissmissHudView];

    }];
}
- (NSString *)calculateMD5HashForData:(NSData *)data {
    unsigned char md5Buffer[CC_MD5_DIGEST_LENGTH];
    CC_MD5(data.bytes, (CC_LONG)data.length, md5Buffer);

    NSMutableString *md5String = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH * 2];
    for (int i = 0; i < CC_MD5_DIGEST_LENGTH; i++) {
        [md5String appendFormat:@"%02x", md5Buffer[i]];
    }

    return md5String;
}
- (void)saveImageWithLocation:(UIImage *)image latitude:(CLLocationDegrees)latitude longitude:(CLLocationDegrees)longitude {
    // 创建一个CGImageSourceRef来从图像数据中读取原始图像
    NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
    CGImageSourceRef imageSource = CGImageSourceCreateWithData((__bridge CFDataRef)imageData, NULL);

    // 创建一个NSMutableDictionary来保存元数据
    NSMutableDictionary *metadata = [NSMutableDictionary dictionaryWithDictionary:(__bridge NSDictionary *)CGImageSourceCopyPropertiesAtIndex(imageSource, 0, NULL)];
    NSMutableDictionary *GPSDictionary = [NSMutableDictionary dictionary];

    // 设置地理定位信息
    GPSDictionary[(NSString *)kCGImagePropertyGPSLatitude] = @(latitude);
    GPSDictionary[(NSString *)kCGImagePropertyGPSLongitude] = @(longitude);
    GPSDictionary[(NSString *)kCGImagePropertyGPSAltitude] = @(latitude);
    // 其他地理定位属性设置...

    // 将地理定位信息添加到元数据中
    metadata[(NSString *)kCGImagePropertyGPSDictionary] = GPSDictionary;

    // 创建一个NSMutableData来保存新的图像数据
    NSMutableData *newImageData = [NSMutableData data];
    CGImageDestinationRef imageDestination = CGImageDestinationCreateWithData((__bridge CFMutableDataRef)newImageData, CGImageSourceGetType(imageSource), 1, NULL);

    // 向图像目标添加图像并设置元数据
    CGImageDestinationAddImageFromSource(imageDestination, imageSource, 0, (__bridge CFDictionaryRef)metadata);

    // 完成图像目标
    CGImageDestinationFinalize(imageDestination);

    // 创建包含地理定位信息的新UIImage
//    UIImage *imageWithLocation = [UIImage imageWithData:newImageData];
//    UIImageWriteToSavedPhotosAlbum(imageWithLocation, nil, nil, nil);
    NSMutableDictionary *imageMetadata = [NSMutableDictionary dictionaryWithDictionary:[self getExifDataFromImage:newImageData]];

    // 先创建一个 PHAssetCreationRequest 对象
    [[PHPhotoLibrary sharedPhotoLibrary] performChanges:^{
        PHAssetCreationRequest *request = [PHAssetCreationRequest creationRequestForAsset];
        
        // 将图片和元数据添加到请求中
        [request addResourceWithType:PHAssetResourceTypePhoto data:newImageData options:nil];
//        [request setCreationDate:[NSDate date]];
//
//        // 设置地理定位信息
//        CLLocation *location = [[CLLocation alloc] initWithLatitude:latitude longitude:longitude];
//        [request setLocation:location];
        
    } completionHandler:^(BOOL success, NSError *error) {
        if (success) {
            NSLog(@"图片保存成功，并保留了元数据");
        } else {
            NSLog(@"图片保存失败：%@", error);
        }
    }];

    

}

// 将地理定位信息添加到图像的Exif数据
- (NSData *)addLocationToImage:(UIImage *)image latitude:(CLLocationDegrees)latitude longitude:(CLLocationDegrees)longitude {
    // 创建一个CGImageSourceRef来从图像数据中读取原始图像
    NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
    CGImageSourceRef imageSource = CGImageSourceCreateWithData((__bridge CFDataRef)imageData, NULL);

    // 创建一个NSMutableDictionary来保存元数据
    NSMutableDictionary *metadata = [NSMutableDictionary dictionaryWithDictionary:(__bridge NSDictionary *)CGImageSourceCopyPropertiesAtIndex(imageSource, 0, NULL)];
    NSMutableDictionary *GPSDictionary = [NSMutableDictionary dictionary];

    // 设置地理定位信息
    GPSDictionary[(NSString *)kCGImagePropertyGPSLatitude] = @(latitude);
    GPSDictionary[(NSString *)kCGImagePropertyGPSLongitude] = @(longitude);
    GPSDictionary[(NSString *)kCGImagePropertyGPSAltitude] = @(latitude);
    // 其他地理定位属性设置...

    // 将地理定位信息添加到元数据中
    metadata[(NSString *)kCGImagePropertyGPSDictionary] = GPSDictionary;

    // 创建一个NSMutableData来保存新的图像数据
    NSMutableData *newImageData = [NSMutableData data];
    CGImageDestinationRef imageDestination = CGImageDestinationCreateWithData((__bridge CFMutableDataRef)newImageData, CGImageSourceGetType(imageSource), 1, NULL);

    // 向图像目标添加图像并设置元数据
    CGImageDestinationAddImageFromSource(imageDestination, imageSource, 0, (__bridge CFDictionaryRef)metadata);

    // 完成图像目标
    CGImageDestinationFinalize(imageDestination);

    // 创建包含地理定位信息的新UIImage
//    UIImage *imageWithLocation = [UIImage imageWithData:newImageData];
//    UIImageWriteToSavedPhotosAlbum(imageWithLocation, nil, nil, nil);
    NSMutableDictionary *imageMetadata = [NSMutableDictionary dictionaryWithDictionary:[self getExifDataFromImage:newImageData]];
     return newImageData;
}



// 从图像数据中获取 Exif 数据
- (NSDictionary *)getExifDataFromImage:(NSData *)imageData {
    NSDictionary *imageMetadata = (__bridge_transfer NSDictionary *)(CGImageSourceCopyPropertiesAtIndex([self createImageSourceFromData:imageData], 0, NULL));
    NSMutableDictionary *mutableMetadata = [imageMetadata mutableCopy];
    return mutableMetadata;
}
// 创建图像源
- (CGImageSourceRef)createImageSourceFromData:(NSData *)data {
    CGImageSourceRef imageSource = CGImageSourceCreateWithData((__bridge CFDataRef)data, NULL);
    return imageSource;
}



-(UIImage *)rotateImageCounterclockwise:(UIImage *)image{

    CGSize rotatedSize = CGSizeMake(image.size.height, image.size.width);
      UIGraphicsBeginImageContext(rotatedSize);
      CGContextRef context = UIGraphicsGetCurrentContext();
      
      // 在新的上下文中绘制逆时针旋转的图像
      CGContextRotateCTM(context, -M_PI_2);
      CGContextTranslateCTM(context, -image.size.width, 0);
      [image drawInRect:CGRectMake(0, 0, image.size.width, image.size.height)];
      
      UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
      UIGraphicsEndImageContext();
      
      return newImage;
       
       
}
-(UIImage *)rotateImage:(UIImage *)image{
    UIImage *rotatedImage = [UIImage imageWithCGImage:image.CGImage scale:image.scale orientation:UIImageOrientationRight];
     return rotatedImage;
      
       
}
#pragma mark - CLLocationManagerDelegate

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations {
    CLLocation *latestLocation = [locations lastObject];
    if(!self.islocation){
        self.islocation = YES;
        latitude = latestLocation.coordinate.latitude;
        longitude = latestLocation.coordinate.longitude;

        [self reverseGeocodeLocation:latestLocation];

    }

}
- (void)reverseGeocodeLocation:(CLLocation *)location {
    CLGeocoder *geocoder = [[CLGeocoder alloc] init];
    [geocoder reverseGeocodeLocation:location completionHandler:^(NSArray<CLPlacemark *> *placemarks, NSError *error) {
        if (error == nil && [placemarks count] > 0) {
            CLPlacemark *placemark = [placemarks firstObject];
            NSString *address = [self getAddressFromPlacemark:placemark];
            
            // 更新界面上的定位标签
            self.locationLabel.text = address;
        } else {
            NSLog(@"Reverse geocode failed with error: %@", [error localizedDescription]);
        }
    }];
}

- (NSString *)getAddressFromPlacemark:(CLPlacemark *)placemark {
    // 根据CLPlacemark对象获取详细地址信息
    // 例如，可以使用placemark.country、placemark.locality、placemark.thoroughfare等属性来组合地址字符串
    // 返回地址字符串

    return [NSString stringWithFormat:@"%@%@%@",placemark.addressDictionary[@"City"],placemark.addressDictionary[@"SubLocality"],placemark.addressDictionary[@"Street"]];
}
- (void)saveImageView {
    // 获取屏幕大小
       CGSize screenSize = [[UIScreen mainScreen] bounds].size;
       
       // 创建图像上下文
       UIGraphicsBeginImageContextWithOptions(screenSize, NO, 0.0);
       
       // 获取当前屏幕的窗口
       UIWindow *window = [[[UIApplication sharedApplication] windows] objectAtIndex:0];
       
       // 将窗口内容绘制到图像上下文中
       [window.layer renderInContext:UIGraphicsGetCurrentContext()];
       
       // 获取生成的图像
       UIImage *screenshot = UIGraphicsGetImageFromCurrentImageContext();
       
       // 关闭图像上下文
       UIGraphicsEndImageContext();
       
    
    // 将图片保存到相册
    UIImageWriteToSavedPhotosAlbum(screenshot, self, @selector(image:didFinishSavingWithError:contextInfo:), nil);
}

- (void)image:(UIImage *)image didFinishSavingWithError:(NSError *)error contextInfo:(void *)contextInfo {

    if (error) {
        
        NSLog(@"保存图片失败：%@", error.localizedDescription);
    } else {
        NSLog(@"图片保存成功");
    }
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
