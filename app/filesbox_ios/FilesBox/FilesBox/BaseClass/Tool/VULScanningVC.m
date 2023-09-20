//
//  VULScanningVC.m
//  VideoULimit
//
//  Created by svnlan on 2019/8/20.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import "VULScanningVC.h"
#import "VULQRView.h"
#import <AVFoundation/AVFoundation.h>
#import <AssetsLibrary/AssetsLibrary.h>
#import <Photos/PHPhotoLibrary.h>

@interface VULScanningVC ()<AVCaptureMetadataOutputObjectsDelegate,UIAlertViewDelegate,UIGestureRecognizerDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate>
{
    int num;
    BOOL upOrdown;
    NSTimer * timer;
    UIImageView * imageView;
    BOOL isBegin;
}

@property (nonatomic, strong) UILabel *labIntroudction;
@property (nonatomic, strong) VULQRView *qrRectView;
@property (strong, nonatomic) AVCaptureDevice * device;
@property (strong, nonatomic) AVCaptureDeviceInput * input;
@property (strong, nonatomic) AVCaptureMetadataOutput * output;
@property (strong, nonatomic) AVCaptureSession * session;
@property (strong, nonatomic) AVCaptureVideoPreviewLayer * preview;
@property (strong, nonatomic) UIImageView *line;
@property (strong, nonatomic) UIActivityIndicatorView *activityView;
@property (strong, nonatomic) UILabel *labelReadying;

@end

@implementation VULScanningVC

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor blackColor];
  
    [self addViews];
    [self startDeviceReadyingWithText:@"相机启动中"];
}


- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    if (timer != nil) {
        [timer fire];
        [_preview removeFromSuperlayer];
    }
    
    [self setupCamera];
}
- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    if ([self.navigationController respondsToSelector:@selector(interactivePopGestureRecognizer)]) {
        self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    }
    
}

- (void)viewWillDisappear:(BOOL)animated {
    [_session stopRunning];
    [timer invalidate];
    [super viewWillDisappear:YES];
    if([self.navigationController respondsToSelector:@selector(interactivePopGestureRecognizer)]) {
        self.navigationController.interactivePopGestureRecognizer.enabled = YES;
    }
}

- (void)addViews {
    self.navigationTitle = KLanguage(@"扫一扫") ;
    self.navigationView.line.hidden = YES;
    [self.view addSubview:self.qrRectView];
    [self.view addSubview:self.labIntroudction];
    [self.qrRectView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.bottom.offset(0);
        make.top.offset(K_NavBar_Height);
    }];
    [self.labIntroudction mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view.mas_centerX);
        make.top.offset(K_NavBar_Height + VULSCREEN_HEIGHT/6-2*kSpace);
        make.height.equalTo(@25);
    }];
    imageView = [[UIImageView alloc] initWithFrame:CGRectMake(60,K_NavBar_Height + VULSCREEN_HEIGHT/6+30,kScreenWidth-120,kScreenWidth-120)];
    imageView.image = [UIImage imageNamed:@""];
    [self.view addSubview:imageView];
    upOrdown = NO;
    
    _line = [[UIImageView alloc] initWithFrame:CGRectMake(10,10,CGRectGetWidth(imageView.frame)-20, 2)];
    _line.image = VULGetImage(@"scanf_line");
    
    [self defaultNavBackItemIconSelector:@selector(backAction:)];
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    [self baseAddNavRightBtnWithTitle:@"相册" selector:@selector(photoAction:)];
    [self setNavRightBtnWithColor:[UIColor blackColor] WithState:UIControlStateNormal];
}

- (void)backAction:(id)sender {
    [self.navigationController popViewControllerAnimated:NO];
}

- (void)goToChosePhoto {
    UIImagePickerController *picker = [[UIImagePickerController alloc] init];
    picker.sourceType = UIImagePickerControllerSourceTypeSavedPhotosAlbum;
    picker.delegate = self;
    picker.modalTransitionStyle = UIModalTransitionStyleCoverVertical;
    picker.allowsEditing = YES;
    [[UINavigationBar appearance] setTintColor:DefaultColor];
    [self presentViewController:picker animated:YES completion:nil];
}

#pragma mark -从相册选取-
- (void)photoAction:(id)sender {
    PHAuthorizationStatus authorizationStatus = [PHPhotoLibrary authorizationStatus];
    // 判断授权状态
    if (authorizationStatus == PHAuthorizationStatusAuthorized) {
        [self goToChosePhoto];
      
    } else if (authorizationStatus == PHAuthorizationStatusNotDetermined) { // 如果没决定, 弹出指示框, 让用户选择
        [PHPhotoLibrary requestAuthorization:^(PHAuthorizationStatus status) {
            // 如果用户选择授权, 则保存图片
            if (status == PHAuthorizationStatusAuthorized) {
                [self goToChosePhoto];
            }
        }];
        
    } else {
        UIAlertController  *alertControl = [UIAlertController alertControllerWithTitle:nil message:@"请在iPhone的\"设置-隐私-相册\"选项中,允许FilesBox访问你的相册" preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction *okAction = [UIAlertAction actionWithTitle:KLanguage(@"确定")  style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            NSString *downloadStr = [NSString stringWithFormat:@"https://itunes.apple.com/cn/app/id%@?mt=8",kAppID];
            NSURL *url = [NSURL URLWithString:downloadStr];
            [[UIApplication sharedApplication] openURL:url];
        }];
        UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:KLanguage(@"取消")  style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            
        }];
        [alertControl addAction:okAction];
        [alertControl addAction:cancelAction];
        
        [self presentViewController:alertControl animated:YES completion:nil];
    }
}

- (void)startDeviceReadyingWithText:(NSString*)text {
    //设备启动状态提示
    if (!_activityView) {
        self.activityView = [[UIActivityIndicatorView alloc] initWithFrame:CGRectMake(0, 0, 30, 30)];
        [_activityView setCenter:imageView.center];
        
        [_activityView setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleWhiteLarge];
        [self.view addSubview:_activityView];
        
        self.labelReadying = [[UILabel alloc] initWithFrame:CGRectMake(0,0, 100, 30)];
        self.labelReadying.center = CGPointMake(_activityView.center.x,_activityView.center.y+30);
        _labelReadying.backgroundColor = [UIColor clearColor];
        _labelReadying.textAlignment = NSTextAlignmentCenter;
        _labelReadying.textColor  = [UIColor whiteColor];
        _labelReadying.font = [UIFont systemFontOfSize:18.0];
        _labelReadying.text = text;
        [self.view addSubview:_labelReadying];
        [_activityView startAnimating];
    }
}

- (void)stopDeviceReadying {
    [_activityView stopAnimating];
    [_labelReadying removeFromSuperview];
}

- (void)animation1 {
    if (upOrdown == NO) {
        num ++;
        _line.frame = CGRectMake(10,10+2*num,CGRectGetWidth(imageView.frame)-20, 2);
        if (2*num > CGRectGetWidth(imageView.frame)-20) {
            upOrdown = YES;
        }
    } else {
        num --;
        _line.frame = CGRectMake(10,10+2*num,CGRectGetWidth(imageView.frame)-20, 2);
        if (num < 0) {
            upOrdown = NO;
        }
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 0) {
        [self backAction:nil];
    }
}

- (void)setupCamera {
    AVAuthorizationStatus authStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
    // 检测摄像头的访问权限。
    if (authStatus == AVAuthorizationStatusRestricted || authStatus == AVAuthorizationStatusDenied) {
        UIAlertController  *alertControl = [UIAlertController alertControllerWithTitle:KLanguage(@"无法使用相机") message:KLanguage(@"请在iPhone的‘设置-隐私-相机’中允许访问相机") preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *okAction = [UIAlertAction actionWithTitle:KLanguage(@"确定") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        }];
        [alertControl addAction:okAction];
        
        [self presentViewController:alertControl animated:YES completion:nil];
        return;
    }
    // Device
    _device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    //  AVCaptureFocusModeContinuousAutoFocus
    if (_device.isFocusPointOfInterestSupported &&[_device isFocusModeSupported:AVCaptureFocusModeAutoFocus]) {
        
        NSError *error = nil;
        //对cameraDevice进行操作前，需要先锁定，防止其他线程访问，
        [_device lockForConfiguration:&error];
        [self.device setFocusMode:AVCaptureFocusModeContinuousAutoFocus];
        //操作完成后，记得进行unlock。
        [self.device unlockForConfiguration];
    }
    
    // Input
    _input = [AVCaptureDeviceInput deviceInputWithDevice:self.device error:nil];
    
    // Output
    _output = [[AVCaptureMetadataOutput alloc] init];
    [_output setMetadataObjectsDelegate:self queue:dispatch_get_main_queue()];
    
    // Session
    _session = [[AVCaptureSession alloc] init];
    [_session setSessionPreset:AVCaptureSessionPresetHigh];
    if ([_session canAddInput:self.input]) {
        [_session addInput:self.input];
    }
    if ([_session canAddOutput:self.output]) {
        [_session addOutput:self.output];
    }
    // 条码类型 AVMetadataObjectTypeQRCode
    _output.metadataObjectTypes = @[AVMetadataObjectTypeCode128Code,AVMetadataObjectTypeUPCECode,AVMetadataObjectTypeCode39Code,AVMetadataObjectTypeCode39Mod43Code,AVMetadataObjectTypeEAN13Code,AVMetadataObjectTypeEAN8Code,AVMetadataObjectTypeCode93Code,AVMetadataObjectTypePDF417Code,AVMetadataObjectTypeQRCode,AVMetadataObjectTypeAztecCode];
    
    // Preview
    _preview = [AVCaptureVideoPreviewLayer layerWithSession:self.session];
    _preview.videoGravity = AVLayerVideoGravityResizeAspectFill;
    _preview.frame = self.view.bounds;
    
    AVCaptureStillImageOutput *output = (AVCaptureStillImageOutput*)[self.session.outputs objectAtIndex:0];
    AVCaptureConnection *videoConnection = [output connectionWithMediaType:AVMediaTypeVideo];
    [self.preview setAffineTransform:CGAffineTransformMakeScale(1.9,1.9)];
    videoConnection.videoScaleAndCropFactor = 1.9;
    [self.view.layer insertSublayer:self.preview atIndex:0];
    
    _output.rectOfInterest = [self getScanCrop:CGRectMake(CGRectGetMinX(imageView.frame)-30,CGRectGetMinY(imageView.frame)-40,CGRectGetWidth(imageView.frame)+60,CGRectGetHeight(imageView.frame)+60) readerViewBounds:CGRectMake(0, 0, kScreenWidth, kScreenHeight-K_NavBar_Height)];
    timer = [NSTimer scheduledTimerWithTimeInterval:.02 target:self selector:@selector(animation1) userInfo:nil repeats:YES];
    [_session startRunning];
    [imageView addSubview:_line];
    [self stopDeviceReadying];
}

- (CGRect)getScanCrop:(CGRect)rect readerViewBounds:(CGRect)readerViewBounds {
    CGFloat x,y,width,height;
    
    y = rect.origin.x / readerViewBounds.size.width;
    x = rect.origin.y / readerViewBounds.size.height;
    height = rect.size.width / readerViewBounds.size.width;
    width = rect.size.height / readerViewBounds.size.height;
    
    return CGRectMake(x, y, width, height);
}

#pragma mark AVCaptureMetadataOutputObjectsDelegate
- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputMetadataObjects:(NSArray *)metadataObjects fromConnection:(AVCaptureConnection *)connection
{
    if (isBegin) {
        return ;
    }
    isBegin = YES;
    [_session stopRunning];
    NSString *stringValue;
    if ([metadataObjects count] >0)
    {
        AVMetadataMachineReadableCodeObject * metadataObject = [metadataObjects objectAtIndex:0];
        stringValue = metadataObject.stringValue;
        NSLog(@"%@",stringValue);
    }
    if (NSStringIsNotEmpty(stringValue)) {
        [self scanningSucceedWithResult:stringValue];
    }
}

#pragma mark - UIImagePickerControllerDelegate -
//当选择一张图片后进入这里
-(void)imagePickerController:(UIImagePickerController*)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    
    [picker dismissViewControllerAnimated:YES completion:nil];
    
    //    NSString *content = @"" ;
    //取出选中的图片
    UIImage *pickImage = info[UIImagePickerControllerEditedImage];
    CIContext *context = [CIContext contextWithOptions:nil];
    CIDetector *detector = [CIDetector detectorOfType:CIDetectorTypeQRCode context:context options:@{CIDetectorAccuracy:CIDetectorAccuracyHigh}];
    CIImage *image = [CIImage imageWithCGImage:pickImage.CGImage];
    NSArray *features = [detector featuresInImage:image];
    CIQRCodeFeature *feature = [features firstObject];
    NSString *result = feature.messageString;
    if ([result isEqualToString:@""] || result.length == 0||result == nil) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [self makeToast:@"扫描失败,请重试"];
        });
        NSLog(@"没有扫描到");
    } else {
        NSLog(@"QRCode is %@",result);
        [self scanningSucceedWithResult:result];
    }
}

#pragma mark -识别成功后代理回调
- (void)scanningSucceedWithResult:(NSString *)resultString {
    [self.navigationController popViewControllerAnimated:NO];
    if (self.scanOutQRCodeBlock) {
        self.scanOutQRCodeBlock(resultString);
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    if ([[UIDevice currentDevice].systemVersion floatValue] >= 6.0) {
        //需要注意的是self.isViewLoaded是必不可少的，其他方式访问视图会导致它加载，在WWDC视频也忽视这一点。
        if (self.isViewLoaded && !self.view.window)// 是否是正在使用的视图
        {
            // Add code to preserve data stored in the views that might be
            // needed later.
            // Add code to clean up other strong references to the view in
            // the view hierarchy.
            self.view = nil;// 目的是再次进入时能够重新加载调用viewDidLoad函数。
        }
    }
    
}


- (VULQRView *)qrRectView {
    if (!_qrRectView) {
        _qrRectView = [[VULQRView alloc] initWithFrame:CGRectMake(0,0,kScreenWidth,kScreenHeight)];
        _qrRectView.transparentArea = CGSizeMake(kScreenWidth-120,kScreenWidth-120);
        _qrRectView.backgroundColor = [UIColor clearColor];
    }
    return _qrRectView;
}

- (UILabel *)labIntroudction {
    if (!_labIntroudction) {
        _labIntroudction = [[UILabel alloc] initWithFrame:CGRectMake(15, 20, 290, 50)];
        _labIntroudction.textAlignment = NSTextAlignmentCenter;
        _labIntroudction.center  = CGPointMake(kScreenWidth/2,45 + K_NavBar_Height);
        _labIntroudction.numberOfLines = 2;
        _labIntroudction.textColor = [UIColor whiteColor];
        _labIntroudction.text =KLanguage(@"请将二维码图像置于矩形框内") ;
    }
    return _labIntroudction;
}

- (void)dealloc
{
    if (_session) {
        [_session stopRunning];
        _session = nil;
    }
    
    if (_preview) {
        _preview = nil;
    }
    
    if (_line) {
        _line = nil;
    }
    
    if (timer)
    {
        [timer invalidate];
        timer = nil;
    }
}


@end
