//
//  VULSvgImageView.m
//  UnlimitedBusiness
//
//  Created by yuekewei on 2021/3/30.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULSvgImageView.h"

@interface VULSvgImageView ()

@property (nonatomic, strong) UIImage *placeholderImage;
@property (nonatomic, strong) NSURL   *url;
@end

@implementation VULSvgImageView

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self.contentMode = UIViewContentModeScaleAspectFill;
        self.placeholderImage = VULGetImage(@"placeholder_face");
    }
    return self;
}

- (void)svg_setImageWithURL:(nullable NSURL *)url {
    [self svg_setImageWithURL:url placeholderImage:self.placeholderImage];
}

- (void)svg_setImageWithURL:(nullable NSURL *)url
           placeholderImage:(nullable UIImage *)placeholder
{
    NSURL *imgURL = url;
    self.placeholderImage = placeholder;
    self.url = imgURL;
    
    NSString *separat = ChooseUrl;
    if ([url.absoluteString containsString:kBaseServiceUrl]) {
        separat = ChooseUrl;
    }
    
    if ([url.absoluteString containsString:kSchoolServiceUrl]) {
        separat = ChooseUrl;
    }
    
    if ([url.absoluteString containsString:separat]) {
        NSString *string = [url.absoluteString componentsSeparatedByString:separat].lastObject;
        if ([string hasPrefix:@"https://"] || [string hasPrefix:@"http://"] ) {
            imgURL = [NSURL URLWithString:string];
        }
    }
    
    if (!imgURL) {
        if (self.svgImgView) {
            [self.svgImgView removeFromSuperview];
            self.svgImgView = nil;
        }
        self.image = placeholder;
        return;
    }
    if (![imgURL.absoluteString hasSuffix:@"svg"]) {
        if (self.svgImgView) {
            [self.svgImgView removeFromSuperview];
            self.svgImgView = nil;
        }
        [self sd_setImageWithURL:imgURL placeholderImage:VULGetImage(@"placeholder_face")];
        return;

    }

    dispatch_async(dispatch_get_main_queue(), ^{
        if ([imgURL.absoluteString hasSuffix:@"svg"]) {
            YYCache *cache = [YYCache cacheWithName:@"VULCache"];
            id data = [cache objectForKey:imgURL.absoluteString];
            if (data && [data isKindOfClass:[NSData class]]) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    self.image = nil;
                });
                [self updateSvg:data];
            }
            else {
                dispatch_async(dispatch_get_global_queue(0,0), ^{
                    
                    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:imgURL cachePolicy:NSURLRequestReloadRevalidatingCacheData timeoutInterval:30];
                    [request setHTTPShouldHandleCookies:NO];
                    request.HTTPMethod = @"GET";
                    [request setValue:[NSString stringWithFormat:@"%@/ios/",kSchoolServiceUrl] forHTTPHeaderField:@"Referer"];
                    [[[NSURLSession sharedSession] dataTaskWithRequest:request
                                                     completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
                        if (!error) {
                            [cache setObject:data forKey:imgURL.absoluteString];
                            dispatch_async(dispatch_get_main_queue(), ^{
                                self.image = nil;
                            });
                            [self updateSvg:data];
                        }
                        else {
                            dispatch_async(dispatch_get_main_queue(), ^{
                                self.image = [UIImage imageNamed:@"placeholder_face"];
                            });
                            [self updateSvg:nil];
                        }
                    }] resume];
                });
            }
            
        }
        else {
            if (self.svgImgView) {
                [self.svgImgView removeFromSuperview];
                self.svgImgView = nil;
            }
            [self sd_setImageWithURL:imgURL placeholderImage:VULGetImage(@"placeholder_face")];
        }
    });
}

- (void)svg_setSpecailImageWithURL:(nullable NSURL *)url
                  placeholderImage:(nullable UIImage *)placeholder{
    NSURL *imgURL = url;
    self.placeholderImage = placeholder;
    self.url = imgURL;
    
    NSString *separat = ChooseUrl;
    if ([url.absoluteString containsString:kBaseServiceUrl]) {
        separat = ChooseUrl;
    }
    
    if ([url.absoluteString containsString:kSchoolServiceUrl]) {
        separat = ChooseUrl;
    }
    
    if ([url.absoluteString containsString:separat]) {
        NSString *string = [url.absoluteString componentsSeparatedByString:separat].lastObject;
        if ([string hasPrefix:@"https://"] || [string hasPrefix:@"http://"] ) {
            imgURL = [NSURL URLWithString:string];
        }
    }
    
    if (!imgURL) {
        if (self.svgImgView) {
            [self.svgImgView removeFromSuperview];
            self.svgImgView = nil;
        }
        self.image = placeholder;
        return;
    }
    if (![imgURL.absoluteString hasSuffix:@"svg"]) {
        if (self.svgImgView) {
            [self.svgImgView removeFromSuperview];
            self.svgImgView = nil;
        }
        [self sd_setImageWithURL:imgURL placeholderImage:VULGetImage(@"placeholder_face")];
        return;

    }

    if ([imgURL.absoluteString hasSuffix:@"svg"]) {
        dispatch_queue_t serialQueue = dispatch_queue_create("com.gcd.syncAndAsyncMix.serialQueue", NULL);
        dispatch_async(serialQueue, ^{
            NSLog(@"11");
        });
        YYCache *cache = [YYCache cacheWithName:@"VULCache"];
        id data = [cache objectForKey:imgURL.absoluteString];
        if (data && [data isKindOfClass:[NSData class]]) {
       
            [self updateSpecialSvg:data];
        }
        else {
            NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:imgURL cachePolicy:NSURLRequestReloadRevalidatingCacheData timeoutInterval:30];
            [request setHTTPShouldHandleCookies:NO];
            request.HTTPMethod = @"GET";
            [request setValue:[NSString stringWithFormat:@"%@/ios/",kSchoolServiceUrl] forHTTPHeaderField:@"Referer"];
            [[[NSURLSession sharedSession] dataTaskWithRequest:request
                                             completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
                if (!error) {
                    [cache setObject:data forKey:imgURL.absoluteString];
                    [self updateSpecialSvg:data];
                }
                else {
                    self.image = [UIImage imageNamed:@"placeholder_face"];
                    [self updateSpecialSvg:nil];
                }
            }] resume];
        }
        
    }
    else {
        if (self.svgImgView) {
            [self.svgImgView removeFromSuperview];
            self.svgImgView = nil;
        }
        [self sd_setImageWithURL:imgURL placeholderImage:VULGetImage(@"placeholder_face")];
    }
}
- (void)updateSpecialSvg:(NSData *)svgData
{
    if (self.svgImgView) {
        [self.svgImgView removeFromSuperview];
        self.svgImgView = nil;
    }
    if (svgData) {
        SVGKImage *svgImage = [SVGKImage imageWithData:svgData];
        self.svgImgView = ({
            SVGKFastImageView *newContentView = [[SVGKFastImageView alloc] initWithSVGKImage:svgImage];
            newContentView.disableAutoRedrawAtHighestResolution = YES;
            newContentView.frame = CGRectMake(0, 0, self.width, self.height);
            
            newContentView;
        });
        [self addSubview:self.svgImgView];
    }
}
- (void)updateSvg:(NSData *)svgData
{
    dispatch_async(dispatch_get_main_queue(), ^{
        if (self.svgImgView) {
            [self.svgImgView removeFromSuperview];
            self.svgImgView = nil;
        }
        if (svgData) {
            SVGKImage *svgImage = [SVGKImage imageWithData:svgData];
            self.svgImgView = ({
                SVGKFastImageView *newContentView = [[SVGKFastImageView alloc] initWithSVGKImage:svgImage];
                newContentView.disableAutoRedrawAtHighestResolution = YES;
                newContentView.frame = CGRectMake(0, 0, self.width, self.height);
                
                newContentView;
            });
            [self addSubview:self.svgImgView];
        }
    });
}

@end
