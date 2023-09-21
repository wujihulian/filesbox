//
//  VULImageViewSeviceCell.m
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/6/1.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULImageViewSeviceCell.h"
#import "XLPhotoBrowser.h"
#import "VULGIFImageView.h"
#import "VULGIFImage.h"
#import "FLAnimatedImageView.h"
#import "FLAnimatedImage.h"
@interface VULImageViewSeviceCell ()
@property (nonatomic, strong) UILabel *nameLabel;
@property (nonatomic, strong) VULSvgImageView *iconImageView;
@property (nonatomic, strong) UIImageView *imageV;
@property (nonatomic, strong) FLAnimatedImageView *imageGifV;


@end
@implementation VULImageViewSeviceCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
       
        [self setView];
    }
    return self;
}
-(void)setView{
    [self.contentView addSubview:self.iconImageView];
    [self.contentView addSubview:self.nameLabel];
    [self.contentView addSubview:self.imageV];
    [self.contentView addSubview:self.imageGifV];

    [self.iconImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(fontAuto(14));
        make.top.mas_equalTo(fontAuto(10));
        make.width.height.mas_equalTo(fontAuto(40));
    }];
    
    [self.nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.iconImageView.mas_right).offset(9);
        make.top.mas_equalTo(fontAuto(5));
        make.height.mas_equalTo(fontAuto(15));
        make.right.mas_equalTo(-fontAuto(14));
    }];
    [self.imageV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.nameLabel.mas_bottom).offset(5);
        make.left.mas_equalTo(self.nameLabel.mas_left);
        make.width.height.mas_equalTo(fontAuto(160));
        make.bottom.mas_equalTo(-fontAuto(5)).priorityHigh();
    }];
    [self.imageGifV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.nameLabel.mas_bottom).offset(5);
        make.left.mas_equalTo(self.nameLabel.mas_left);
        make.width.height.mas_equalTo(fontAuto(160));
        make.bottom.mas_equalTo(-fontAuto(5)).priorityHigh();
    }];
    self.imageGifV.hidden = YES;
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickImage)];
    self.imageV .userInteractionEnabled = YES;
    [self.imageV addGestureRecognizer:tap];
    // 添加长按手势
     UILongPressGestureRecognizer *longPressGesture = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPress:)];
     [self addGestureRecognizer:longPressGesture];
    
}
- (void)handleLongPress:(UILongPressGestureRecognizer *)gestureRecognizer {
    if (gestureRecognizer.state == UIGestureRecognizerStateBegan) {
        NSArray *  titleArr = @[KLanguage(@"删除")];

        NSMutableArray *array = [[NSMutableArray alloc] init];
        for (NSInteger i = 0; i < titleArr.count; i++) {
            NSString *title1 = titleArr[i] ;
            UIImage *rightImage = nil;
            UIImage *image = nil;
     
            
            
            YCMenuAction *action = [YCMenuAction actionWithTitle:titleArr[i] image:image rightImage:rightImage handler:^(YCMenuAction *action) {

                
            
                if ([action.title isEqualToString:KLanguage(@"删除")]) {
                    if(self.deletCommentWithModel){
                        self.deletCommentWithModel(self.model);
                    }
                }else{
                }
               
            }];
            [array addObject:action];
        }
        CGFloat width = 120;

        YCMenuView *menuView = [YCMenuView menuWithActions:array width:width relyonView:self.iconImageView];
        menuView.menuColor = [UIColor whiteColor];
        menuView.separatorIndexArray = @[@(3)];
        menuView. isNoPre = YES;
        menuView.separatorColor = [UIColor redColor];
        menuView.maxDisplayCount = 20;
        menuView.cornerRaius = 0;
        //menuView.offset = 150;
        menuView.textColor = UIColorHex(333333);
        menuView.textFont = [UIFont yk_pingFangRegular:14];
        menuView.menuCellHeight = 35;
        menuView.dismissOnselected = YES;
        menuView.dismissOnTouchOutside = YES;
        menuView.backgroundColor = [UIColor whiteColor];
        [menuView show];
    }
}

- (void)clickImage{
    XLPhotoBrowser *browser = [XLPhotoBrowser showPhotoBrowserWithImages:@[[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", ChooseUrl, _model.msg]]] currentImageIndex:0];
        browser.browserStyle = XLPhotoBrowserStyleIndexLabel; // 微博样式
        [browser setActionSheetWithTitle:@"是否保存到相册?" delegate:self cancelButtonTitle:@"取消" deleteButtonTitle:@"" otherButtonTitles:@"保存", nil];
}
- (void)photoBrowser:(XLPhotoBrowser *)browser clickActionSheetIndex:(NSInteger)actionSheetindex currentImageIndex:(NSInteger)currentImageIndex {
    // do something yourself
    switch (actionSheetindex) {
        case 0: // 保存
            {
                PHAuthorizationStatus status = [PHPhotoLibrary authorizationStatus];
                if (status == PHAuthorizationStatusRestricted || status == PHAuthorizationStatusDenied) {
                    [VULNotificationCenter postNotificationName:@"RequestingAlbumPermissions" object:nil];
                } else if (status == PHAuthorizationStatusNotDetermined) {
                    [[TZImageManager manager] requestAuthorizationWithCompletion:^{
                        [browser saveCurrentShowImage];
                    }];
                } else {
                    [browser saveCurrentShowImage];
                }
            }
            break;
        default: {
            NSLog(@"点击了actionSheet索引是:%zd , 当前展示的图片索引是:%zd", actionSheetindex, currentImageIndex);
        }
        break;
    }
}
   

- (void)setModel:(VULServiceChatMessageModel *)model{
    _model  = model;
    self.nameLabel.text = model.timestamp ;

    [self.iconImageView svg_setImageWithURL:[NSURL URLWithString:resultsUrl(model.avatar)] placeholderImage:VULGetImage(@"placeholder_face")];
    CGFloat imageW ;
    if ([model.msg hasSuffix:@"gif"]) {
        self.imageV.hidden = YES;
        self.imageGifV.hidden = NO;
        self.imageGifV .image = nil;

        [self loadAnimatedImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", ChooseUrl, model.msg]] completion:^(FLAnimatedImage *animatedImage) {
            [VULQueue executeInMainQueue:^{
                self.imageGifV.animatedImage = animatedImage;
            }];
        } loadFaild:^(NSString *errorMsg) {
            [VULQueue executeInMainQueue:^{
            }];
        }];
        [self.imageV sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", ChooseUrl, model.msg]] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
            if (image) {
                // 获取图片的大小
                CGSize imageSize = image.size;
                
                // 获取图片的宽高比例
                CGFloat aspectRatio = imageSize.width / imageSize.height;
                CGFloat imageW ;
                imageW= fontAuto(80);
                CGFloat imageWidth = imageSize.width ;
                CGFloat imageHight =   imageSize.height;
                if (imageSize.height>0) {
                    if (imageSize.width>=imageSize.height) {
                        if (imageSize.width>imageW*2) {
                            imageWidth =  imageW*2;
                            imageHight =  (imageW*2*imageSize.height)/imageSize.width;
                        }else{
                            imageWidth = imageSize.width;
                            imageHight =  imageSize.height;
                        }

                    }else{
                        if (imageSize.height>imageW*2) {
                            imageWidth =  (imageW*2*imageSize.width)/imageSize.height;
                            imageHight =  imageW*2;
                        }else{
                            imageWidth = imageSize.width;
                            imageHight =  imageSize.height;
                        }

                    }
                }else{
                    NSString *imageUrl = [NSString stringWithFormat:@"%@%@", ChooseUrl, model.msg];

                    NSData *data = [NSData dataWithContentsOfURL:[NSURL URLWithString:resultsUrl(model.msg)]];
                                    UIImage *showimage = [UIImage imageWithData:data];
                    imageSize = showimage.size;
                    NSDictionary *dic = @{@"width":@(imageSize.width),@"height":@(imageSize.height)};
                    [[NSUserDefaults standardUserDefaults] setObject:dic forKey:imageUrl];
                    if (imageSize.width>=imageSize.height) {
                        if (imageSize.width>imageW*2) {
                            imageWidth =  imageW*2;
                            imageHight =  (imageW*2*imageSize.height)/imageSize.width;
                        }else{
                            imageWidth = imageSize.width;
                            imageHight =  imageSize.height;
                        }

                    }else{
                        if (imageSize.height>imageW*2) {
                            imageWidth =  (imageW*2*imageSize.width)/imageSize.height;
                            imageHight =  imageW*2;
                        }else{
                            imageWidth = imageSize.width;
                            imageHight =  imageSize.height;
                        }

                    }
                    
                }
                
                [self.imageV mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.top.mas_equalTo(self.nameLabel.mas_bottom).offset(5);
                    make.left.mas_equalTo(self.nameLabel.mas_left);
                    make.width.mas_equalTo(imageWidth);
                    make.height.mas_equalTo(imageHight);
                    make.bottom.mas_equalTo(-fontAuto(0)).priorityHigh();
                }];

                [self.imageGifV mas_makeConstraints:^(MASConstraintMaker *make) {
                    make.top.mas_equalTo(self.nameLabel.mas_bottom).offset(5);
                    make.left.mas_equalTo(self.nameLabel.mas_left);
                    make.width.mas_equalTo(imageWidth);
                    make.height.mas_equalTo(imageHight);
                    make.bottom.mas_equalTo(-fontAuto(0)).priorityHigh();
                }];
                // 在此可以将图片大小和比例应用到 cell 或其他操作中
            }
        }];
    }else{
        self.imageV .image = nil;
        [self.imageV sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", ChooseUrl, model.msg]] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
            if (image) {
                // 获取图片的大小
                CGSize imageSize = image.size;
                
                // 获取图片的宽高比例
                CGFloat aspectRatio = imageSize.width / imageSize.height;
                CGFloat imageW ;
                imageW= fontAuto(80);
                CGFloat imageWidth = imageSize.width ;
                CGFloat imageHight =   imageSize.height;
                if (imageSize.height>0) {
                    if (imageSize.width>=imageSize.height) {
                        if (imageSize.width>imageW*2) {
                            imageWidth =  imageW*2;
                            imageHight =  (imageW*2*imageSize.height)/imageSize.width;
                        }else{
                            imageWidth = imageSize.width;
                            imageHight =  imageSize.height;
                        }

                    }else{
                        if (imageSize.height>imageW*2) {
                            imageWidth =  (imageW*2*imageSize.width)/imageSize.height;
                            imageHight =  imageW*2;
                        }else{
                            imageWidth = imageSize.width;
                            imageHight =  imageSize.height;
                        }

                    }
                }else{
                    NSString *imageUrl = [NSString stringWithFormat:@"%@%@", ChooseUrl, model.msg];

                    NSData *data = [NSData dataWithContentsOfURL:[NSURL URLWithString:resultsUrl(model.msg)]];
                                    UIImage *showimage = [UIImage imageWithData:data];
                    imageSize = showimage.size;
                    NSDictionary *dic = @{@"width":@(imageSize.width),@"height":@(imageSize.height)};
                    [[NSUserDefaults standardUserDefaults] setObject:dic forKey:imageUrl];
                    if (imageSize.width>=imageSize.height) {
                        if (imageSize.width>imageW*2) {
                            imageWidth =  imageW*2;
                            imageHight =  (imageW*2*imageSize.height)/imageSize.width;
                        }else{
                            imageWidth = imageSize.width;
                            imageHight =  imageSize.height;
                        }

                    }else{
                        if (imageSize.height>imageW*2) {
                            imageWidth =  (imageW*2*imageSize.width)/imageSize.height;
                            imageHight =  imageW*2;
                        }else{
                            imageWidth = imageSize.width;
                            imageHight =  imageSize.height;
                        }

                    }
                    
                }
                
                [self.imageV mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.top.mas_equalTo(self.nameLabel.mas_bottom).offset(5);
                    make.left.mas_equalTo(self.nameLabel.mas_left);
                    make.width.mas_equalTo(imageWidth);
                    make.height.mas_equalTo(imageHight);
                    make.bottom.mas_equalTo(-fontAuto(0)).priorityHigh();
                }];
                // 在此可以将图片大小和比例应用到 cell 或其他操作中
            }
        }];

        self.imageV.hidden = NO;
        self.imageGifV.hidden = YES;
    }

//    [self layoutIfNeeded];
//
//    UIBezierPath *maskOnePath = [UIBezierPath bezierPathWithRoundedRect:CGRectMake(0, 0, self.imageV.width, self.imageV.height) byRoundingCorners:UIRectCornerTopRight | UIRectCornerBottomLeft |  UIRectCornerBottomRight cornerRadii:CGSizeMake(10, 10)];
//
//    CAShapeLayer *maskOneLayer = [[CAShapeLayer alloc] init];
//
//    maskOneLayer.frame = self.imageV.bounds;
//
//    maskOneLayer.path = maskOnePath.CGPath;
//    self.imageV.clipsToBounds = YES;
//    self.imageV.layer.mask = maskOneLayer;
}
//TODO: gif 加载
- (void)loadAnimatedImageWithURL:(NSURL *const)url completion:(void (^)(FLAnimatedImage *animatedImage))completion loadFaild:(void (^)(NSString *errorMsg))faildAction {
    NSString *const filename = url.lastPathComponent;
    NSString *const diskPath = [NSHomeDirectory() stringByAppendingPathComponent:filename];
    NSData *__block animatedImageData = [[NSFileManager defaultManager] contentsAtPath:diskPath];
    FLAnimatedImage *__block animatedImage = [[FLAnimatedImage alloc] initWithAnimatedGIFData:animatedImageData];
    if (animatedImage) {
        if (completion) {
            completion(animatedImage);
        }
    } else {
        [[[NSURLSession sharedSession] dataTaskWithURL:url completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            [VULQueue executeInMainQueue:^{
                animatedImageData = data;
                animatedImage = [[FLAnimatedImage alloc] initWithAnimatedGIFData:animatedImageData];
                if (animatedImage) {
                    if (completion) {
                        completion(animatedImage);
                    }
                    [data writeToFile:diskPath atomically:YES];
                } else {
                    if (faildAction) {
                        faildAction(error.description);
                    }
                }
            }];
        }] resume];
    }
}


- (CGSize)sizeThatFits:(CGSize)size {
    CGFloat totalHeight = 0;
//    totalHeight += [self.nameLabel sizeThatFits:size].height;
    totalHeight += [self.imageV sizeThatFits:size].height;
    totalHeight += fontAuto(40); // margins
    return CGSizeMake(size.width, totalHeight);
}
-(void)setHideTime:(BOOL)hideTime{
//    if (hideTime) {
//        [self.nameLabel mas_updateConstraints:^(MASConstraintMaker *make) {
//            make.left.mas_equalTo(self.iconImageView.mas_right).offset(9);
//            make.top.mas_equalTo(fontAuto(5));
//            make.height.mas_equalTo(fontAuto(0));
//            make.right.mas_equalTo(-fontAuto(14));
//        }];
//    }
}


#pragma mark - Lazy

- (VULSvgImageView *)iconImageView {
    if (!_iconImageView) {
        _iconImageView = [[VULSvgImageView alloc] init];
        _iconImageView.width = _iconImageView.height = FontAuto(40);
        _iconImageView.layer.cornerRadius = _iconImageView.height / 2.0;
        _iconImageView.layer.masksToBounds = YES;
        _iconImageView.image = VULGetImage(@"login_logo");
    }
    return _iconImageView;
}

- (UILabel *)nameLabel {
    if (!_nameLabel) {
        _nameLabel = ({
            UILabel *label = [[UILabel alloc] init];
            label.font = [UIFont yk_pingFangMedium:FontAuto(14)];
            label.textColor = UIColorHex(#CFCFCF);
            label;
        });
    }
    return _nameLabel;
}


- (UIImageView *)imageV {
    if (!_imageV) {
        _imageV = ({
            UIImageView *imageView = [[UIImageView alloc] init];
            imageView.layer.masksToBounds = YES;
            imageView.layer.cornerRadius = 8;
            imageView;
        });
    }
    return _imageV;
}
- (UIImageView *)imageGifV {
    if (!_imageGifV) {
        _imageGifV = ({
            FLAnimatedImageView *imageView = [[FLAnimatedImageView alloc] init];
            imageView.layer.masksToBounds = YES;
            imageView.layer.cornerRadius = 8;
            imageView;
        });
    }
    return _imageGifV;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
