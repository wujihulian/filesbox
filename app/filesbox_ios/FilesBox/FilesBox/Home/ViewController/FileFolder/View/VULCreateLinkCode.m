//
//  VULCreateLinkCode.m
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/4/16.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULCreateLinkCode.h"
@interface VULCreateLinkCode ()

@property (nonatomic, strong) UIButton *createQRCodeBtn;
@property (nonatomic, strong) UIImageView *QRCodeImgView;
@end

@implementation VULCreateLinkCode

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];
        self.layer.cornerRadius = 8.f;
        self.layer.masksToBounds = YES;
    }
    return self;
}

- (void)setUrl:(NSString *)url{
    _url= url;
    [self createUI];
}
- (void)createUI {
    

    
    
    self.QRCodeImgView = [UIImageView new];
    self.QRCodeImgView.frame =CGRectMake(self.width*0.2, self.width*0.1, self.width*0.6, self.width*0.6);
    CGFloat width =  self.QRCodeImgView.size.width;
    
 
    UIImageView *image1 = [[UIImageView alloc] init];
    image1.frame = CGRectMake(width*5/12, width*5/12, width/6, width/6);
    [self.QRCodeImgView addSubview:image1];
    image1.layer.borderColor = [UIColor whiteColor].CGColor;
    image1.layer.borderWidth = 2.f;
    image1.layer.cornerRadius = 5;
    image1.layer.masksToBounds = YES;
    if ([self.imageConent isKindOfClass:[UIImage class]] || !self.imageConent) {
        image1 .image = self.imageConent;
    }else{
        [image1 setImageURL:[NSURL URLWithString:self.imageConent]];
    }
    image1.hidden = YES;
    [self addSubview: self.QRCodeImgView];
    self.QRCodeImgView.image = [self getQrCodeImageWithInfo:self.url];
    self.createQRCodeBtn = [[UIButton alloc] initWithFrame:CGRectMake(self.width*0.2, self.QRCodeImgView.bottom +self.width*0.1, self.width*0.6, self.width*0.1)];
    [self.createQRCodeBtn setTitle:KLanguage(@"保存到手机")  forState:UIControlStateNormal];
    self.createQRCodeBtn.backgroundColor = BtnColor;
    self.createQRCodeBtn.layer.masksToBounds = YES;
    self.createQRCodeBtn.layer.cornerRadius = self.width*0.05;
    [self.createQRCodeBtn addTarget:self action:@selector(saveImage) forControlEvents:UIControlEventTouchUpInside];
    [self addSubview: self.createQRCodeBtn];
}
- (void)saveImage{
    UIImage *resultImage = [self captureImageFromView:self.QRCodeImgView];
    UIImageWriteToSavedPhotosAlbum(resultImage, self, @selector(image:didFinishSavingWithError:contextInfo:), nil);
//    [self addShareLog];
}
//必要实现的协议方法
- (void)image:(UIImage *)image didFinishSavingWithError:(NSError *)error contextInfo:(void *)contextInfo {
    NSString *toastMsg = @"保存成功";
    if (error != nil) {
        toastMsg = @"保存失败";
    }
    [self makeToast:KLanguage(toastMsg) ];
    if (self.dismiss) {
        self.dismiss();
    }
}


-(NSString*)encodeString:(NSString*)unencodedString{
    
    // CharactersToBeEscaped = @":/?&=;+!@#$()~',*";
    // CharactersToLeaveUnescaped = @"[].";
    
    NSString *encodedString = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault,
                                                              (CFStringRef)unencodedString,
                                                              NULL,
                                                              (CFStringRef)@"!*'();:@&=+$,/?%#[]",
                                                              kCFStringEncodingUTF8));
    
    return encodedString;
}
- (UIImage *)captureImageFromView:(UIView *)view {
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
- (UIImage *)getQrCodeImageWithInfo:(NSString *)info {
    CIFilter *filter = [CIFilter filterWithName:@"CIQRCodeGenerator"];
    [filter setDefaults];
    //把信息转化为NSData
    NSData *infoData = [info dataUsingEncoding:NSUTF8StringEncoding];
    //滤镜对象kvc存值
    [filter setValue:infoData forKeyPath:@"inputMessage"];
    NSLog(@"%@", filter.inputKeys);
    CIImage *outImage = [filter outputImage];

    return [self createNonInterpolatedUIImageFormCIImage:outImage withSize:fontAuto(VULSCREEN_WIDTH * 0.5)];
}

- (UIImage *)createNonInterpolatedUIImageFormCIImage:(CIImage *)image withSize:(CGFloat)size {
    CGRect extent = CGRectIntegral(image.extent);
    CGFloat scale = MIN(size / CGRectGetWidth(extent), size / CGRectGetHeight(extent));

    //1.创建bitmap;
    size_t width = CGRectGetWidth(extent) * scale;
    size_t height = CGRectGetHeight(extent) * scale;
    CGColorSpaceRef cs = CGColorSpaceCreateDeviceGray();
    CGContextRef bitmapRef = CGBitmapContextCreate(nil, width, height, 8, 0, cs, (CGBitmapInfo)kCGImageAlphaNone);
    CIContext *context = [CIContext contextWithOptions:nil];
    CGImageRef bitmapImage = [context createCGImage:image fromRect:extent];
    CGContextSetInterpolationQuality(bitmapRef, kCGInterpolationNone);
    CGContextScaleCTM(bitmapRef, scale, scale);
    CGContextDrawImage(bitmapRef, extent, bitmapImage);

    //2.保存bitmap到图片
    CGImageRef scaledImage = CGBitmapContextCreateImage(bitmapRef);
    CGContextRelease(bitmapRef);
    CGImageRelease(bitmapImage);
    return [UIImage imageWithCGImage:scaledImage];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
