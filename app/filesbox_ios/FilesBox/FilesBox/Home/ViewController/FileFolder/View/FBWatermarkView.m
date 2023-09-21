//
//  FBWatermarkView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/4/25.
//

#import "FBWatermarkView.h"

@implementation FBWatermarkView


- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.userInteractionEnabled = NO;
    }
    return self;
}
-(void)addWaterMarkView{
//    markType  int  水印类型  0无水印 1 文字 2 图片
//    wmTitle  String  主标题文字
//    wmColor  String 水印颜色
//    wmFont  String 水印字体
//    wmSize  int  水印字体大小
//    wmSubTitle  String  副标题文字
//    wmSubColor  String 水印颜色
//    wmSubFont  String 水印字体
//    wmSubSize  int  水印字体大小
//    wmTransparency  float   水印透明度
//    wmRotate  int  水印内容旋转角度
//    wmMargin  array[int]  水印间距
//    wmPicPath  String  图片水印路径
//    wmPicSize  array[integer]  图片水印大小
//    wmPosition  array[integer]  水印的位置
    NSDictionary *roleDic = [[NSUserDefaults standardUserDefaults] objectForKey:@"markConfig"];
    NSString *markType = roleDic[@"markType"];
    NSString *wmTitle = roleDic[@"wmTitle"];
    NSString *wmColor = roleDic[@"wmColor"];
    NSString *wmSize = roleDic[@"wmSize"];
    NSString *wmTransparency = roleDic[@"wmTransparency"];
    NSString *wmRotate = roleDic[@"wmRotate"];
    NSString *wmSubTitle = roleDic[@"wmSubTitle"];
    NSString *wmSubColor = roleDic[@"wmSubColor"];
    NSString *wmSubSize = roleDic[@"wmSubSize"];
    wmSubTitle = [wmSubTitle stringByReplacingOccurrencesOfString:@"<br/>" withString:@""];
    [self removeFromSuperview];
    [kWindow addSubview:self];

    if (markType.intValue == 0) {
        self.hidden = YES;
        return;
    }
    self.hidden = NO;
    [self removeAllSubviews];
    [self.layer removeAllSublayers];

    if (markType.intValue == 1) {

        [self addWaterMarkText:wmTitle wmSubTitle:wmSubTitle WithTextColor:MSColorFromHexString(wmColor) wmSubColor:MSColorFromHexString(wmSubColor) WithFont:[UIFont yk_pingFangRegular:fontAuto(wmSize.intValue)] wmSubSize:[UIFont yk_pingFangRegular:fontAuto(wmSubSize.intValue)] WithOpacity:wmTransparency.floatValue/100 wmRotate:wmRotate.intValue];
        return;
    }
    NSString *wmPicPath = roleDic[@"wmPicPath"];

    if (markType.intValue ==2) {
        
        NSInteger row = 4; //行
        NSInteger col = 5; //列
        CGFloat height =  [UIScreen mainScreen].bounds.size.height-K_NavBar_Height-K_TabBar_Height;
        CGFloat width =  [UIScreen mainScreen].bounds.size.width-50;
        CGFloat with = 40;
        CGFloat height1 = 50;
        CGFloat textW = 80;;

        for (int i = 0; i < row; i ++) {
            for (int j = 0; j < col; j ++) {
                UIImageView *imageV = [[UIImageView alloc]init];
                [imageV sd_setImageWithURL:[NSURL URLWithString:resultsUrl(wmPicPath)]];
//                imageV.frame = CGRectMake(50+j*width/col, K_NavBar_Height+ 50+i*(height/row), 80, 50);
                
                imageV.frame = CGRectMake(with+j*(textW+with*1.5), K_NavBar_Height+height1+i*(height/row), textW, 50);
                imageV.contentMode = UIViewContentModeScaleAspectFit;
                imageV.alpha =wmTransparency.floatValue/100;
                //旋转文字
                // 将角度转换为弧度
                float angle = 180/wmRotate.intValue;
                CGFloat radians =    -M_PI/angle;
                    // 创建CGAffineTransform对象
                CGAffineTransform transform = CGAffineTransformMakeRotation(radians);
                    // 应用变换
                imageV.transform = transform;
//                imageV.transform = CATransform3DMakeRotation(-M_PI/8, 0,0,3);
                [self addSubview:imageV];
                
            }
        }
    }

    

}
- (void)addWaterMarkText:(NSString*)waterText wmSubTitle:(NSString *)wmSubTitle WithTextColor:(UIColor*)color wmSubColor:(UIColor *)wmSubColor WithFont:(UIFont*)font wmSubSize:(UIFont *)wmSubSize WithOpacity:(float)opacity  wmRotate:(float)wmRotate  {
    //计算水印文字的宽高
    NSString *waterMark = waterText;
    CGSize textSize = [waterMark sizeWithAttributes:@{NSFontAttributeName:font}];
    CGSize wmSubTitleSize = [wmSubTitle sizeWithAttributes:@{NSFontAttributeName:wmSubSize}];

    CGFloat height =  [UIScreen mainScreen].bounds.size.height-K_NavBar_Height-K_TabBar_Height;
    CGFloat width =  [UIScreen mainScreen].bounds.size.width-50;
    NSInteger row = 4; //行
    NSInteger col = 5; //列
    CGFloat with = 40;
    CGFloat height1 = 50;

    float angle = 180/wmRotate;
    CGFloat offHeight = height/row;
   
    CGFloat textW = wmSubTitleSize.width>textSize.width?wmSubTitleSize.width:textSize.width;;
    if(wmRotate == 90){
        offHeight = textW+20;
    }
    for (int i = 0; i < row; i ++) {
        for (int j = 0; j < col; j ++) {
            UIView *view = [UIView new];
            view.frame = CGRectMake(with+j*(textW+with*1.5), K_NavBar_Height+height1+i*(offHeight), textW, textSize.height+wmSubTitleSize.height);
            [self addSubview:view];
            CATextLayer *textLayer = [[CATextLayer alloc]init];
            textLayer.contentsScale = [UIScreen mainScreen].scale;//按当前屏幕分辨显示，否则会模糊
            CFStringRef fontName = (__bridge CFStringRef)font.fontName;
            CGFontRef fontRef =CGFontCreateWithFontName(fontName);
            textLayer.font = fontRef;
            textLayer.fontSize = font.pointSize;
            textLayer.foregroundColor = color.CGColor;
            textLayer.string = waterMark;
            textLayer.opacity = opacity;

            textLayer.alignmentMode = kCAAlignmentCenter;
            textLayer.frame = CGRectMake(0, 0, textW, textSize.height);
            //旋转文字
//            textLayer.transform = CATransform3DMakeRotation(-M_PI/angle, 0,0,1);
            [view.layer addSublayer:textLayer];
            CATextLayer *textLayer1 = [[CATextLayer alloc]init];
            textLayer1.contentsScale = [UIScreen mainScreen].scale;//按当前屏幕分辨显示，否则会模糊
            CFStringRef fontName1 = (__bridge CFStringRef)wmSubSize.fontName;
            CGFontRef fontRef1 =CGFontCreateWithFontName(fontName1);
            textLayer1.font = fontRef1;
            textLayer1.fontSize = wmSubSize.pointSize;
            textLayer1.foregroundColor = wmSubColor.CGColor;
            textLayer1.string = wmSubTitle;
            textLayer1.opacity = opacity;
            textLayer1.frame = CGRectMake(0,  textLayer.bottom, textW, wmSubTitleSize.height);
            textLayer1.alignmentMode = kCAAlignmentCenter;

            //旋转文字
            view.layer.transform = CATransform3DMakeRotation(-M_PI/angle, 0,0,1);
            [view.layer addSublayer:textLayer1];
          
//            CAShapeLayer *borderLayer = [CAShapeLayer layer];
//            borderLayer.frame = textLayer.bounds;
//            borderLayer.lineWidth = 2;
//            borderLayer.strokeColor = [UIColor redColor].CGColor;
//            borderLayer.fillColor = nil;
//
//            CGRect borderPathRect = CGRectInset(textLayer.bounds, -10, -10);
//            UIBezierPath *borderPath = [UIBezierPath bezierPathWithRect:borderPathRect];
//            borderLayer.path = borderPath.CGPath;
//
//            [textLayer addSublayer:borderLayer];
//
//            CAShapeLayer *borderLayer1 = [CAShapeLayer layer];
//            borderLayer1.frame = textLayer1.bounds;
//            borderLayer1.lineWidth = 2;
//            borderLayer1.strokeColor = [UIColor redColor].CGColor;
//            borderLayer1.fillColor = nil;
//
//            CGRect borderPathRect1 = CGRectInset(textLayer1.bounds, -10, -10);
//            UIBezierPath *borderPath1 = [UIBezierPath bezierPathWithRect:borderPathRect1];
//            borderLayer1.path = borderPath1.CGPath;
//
//            [textLayer addSublayer:borderLayer1];
        }
    }
}



@end

