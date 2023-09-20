//
//  BasePreviewTextVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/9/7.
//

#import "BasePreviewTextVC.h"

@interface BasePreviewTextVC ()
@property (nonatomic, strong) UITextView *textView;/**< 当前链接 **/

@end

@implementation BasePreviewTextVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationTitle = self.nvaTitle;
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    self.textView.text = self.text;
    [self.view addSubview:self.textView];
    [self.textView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(K_NavBar_Height+10);
        make.left.mas_equalTo(10);
        make.right.mas_equalTo(-10);
        make.bottom.mas_equalTo(-10);
    }];
    // Do any additional setup after loading the view.
}
-(UITextView *)textView{
    if(!_textView){
        _textView = [UITextView new];
        _textView.font = [UIFont yk_pingFangRegular:fontAuto(15)];
        _textView.textColor = HEXCOLOR(0x333333);
    }
    return _textView;
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
