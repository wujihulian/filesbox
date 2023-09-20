//
//  FBMineEditVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/7/17.
//

#import "FBMineEditVC.h"

@interface FBMineEditVC ()
@property (nonatomic, retain) UITextField *textField;
@property (nonatomic, retain) UITextField *passField;
@property (nonatomic, retain) UITextField *againField;
@property (nonatomic, retain) UIButton *closebtn;

@end

@implementation FBMineEditVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationTitle = self.model.title;
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    
    self.view.backgroundColor = HEXCOLOR(0xf6f6f6);
    UIView *view = [UIView new];
    view.backgroundColor = UIColor.whiteColor;
    [self.view addSubview:view];
    if(![self.model.title isEqualToString:KLanguage(@"密码")]){
        [view addSubview:self.textField];
        [view addSubview:self.closebtn];
        self.textField.text = self.model.detail;
        [view mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(K_NavBar_Height);
            make.left.right.mas_equalTo(0);
            make.height.mas_equalTo(fontAuto(50));
        }];
        [self.textField mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(0);
            make.left.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(40));
            make.height.mas_equalTo(fontAuto(50));
        }];
        [self.closebtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(0);
            make.right.mas_equalTo(-fontAuto(10));
            make.height.mas_equalTo(fontAuto(20));
            make.centerY.mas_equalTo(view.mas_centerY);
        }];
        self.closebtn.hidden = self.model.detail.length>0?NO:YES;
        [[self.textField rac_textSignal] subscribeNext:^(NSString *_Nullable x) {
            self.closebtn.hidden = x.length>0?NO:YES;
        }];
    }else{
        [view addSubview:self.textField];
        [view addSubview:self.passField];
        [view addSubview:self.againField];
        [self.textField setSecureTextEntry:YES];//设置密码形式
        [self.passField setSecureTextEntry:YES];//设置密码形式
        [self.againField setSecureTextEntry:YES];//设置密码形式

        _textField.placeholder =  KLanguage(@"原密码(忘记请联系管理员重置密码)");

        [view mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(K_NavBar_Height);
            make.left.right.mas_equalTo(0);
            make.height.mas_equalTo(fontAuto(150));
        }];
        [self.textField mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(0);
            make.left.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(10));
            make.height.mas_equalTo(fontAuto(50));
        }];
        [self.passField mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(50));
            make.left.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(10));
            make.height.mas_equalTo(fontAuto(50));
        }];
        [self.againField mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(100));
            make.left.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(10));
            make.height.mas_equalTo(fontAuto(50));
        }];
    }
    [self baseAddNavRightBtnWithTitle:KLanguage(@"保存") selector:@selector(saveModel) color:BtnColor];
    // Do any additional setup after loading the view.
}
-(void)saveModel{
    if(![self.model.title isEqualToString:KLanguage(@"密码")]){
        self.textField.text = [self.textField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        if(self.textField.text.length==0 ){
            [self makeToast:KLanguage(@"请输入")];
            return;
        }
        if([self.model.title isEqualToString:KLanguage(@"昵称")])
        {
            if(self.textField.text.length>30 ){
                [self makeToast:KLanguage(@"昵称最长为30")];
                return;
            }
            [self uploadWithDic:@{@"key":@"nickname",@"value":  self.textField.text}];
            return;
        }

        if([self.model.title isEqualToString:KLanguage(@"邮箱")])
        {
            if(![NSString validateEmail:self.textField.text]){
                [self makeToast:KLanguage(@"邮箱输入不正确")];
                return;
            }
            [self uploadWithDic:@{@"key":@"email",@"value":  self.textField.text}];
    
            return;
        }
        
        if([self.model.title isEqualToString:KLanguage(@"手机")])
        {
            if(![NSString validateMobile:self.textField.text]){
                [self makeToast:KLanguage(@"手机输入不正确")];
                return;
            }
            [self uploadWithDic:@{@"key":@"phone",@"value":  self.textField.text}];
            return;
        }
    }else{
        self.againField.text = [self.againField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        self.passField.text = [self.passField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];


        if(self.textField.text.length==0 ||self.passField.text.length==0||self.againField.text.length==0 ){
            [self makeToast:KLanguage(@"请输入")];
            return;
        }
        if(![self.passField.text isEqualToString:self.againField.text]){
            [self makeToast:KLanguage(@"密码输入不一致")];
            return;
        }
        NSString *aesPwd = [NSString encyptPKCS5:self.textField.text WithKey:AESENCRYKEY];
        NSString *aesPwd1 = [NSString encyptPKCS5:self.passField.text WithKey:AESENCRYKEY];

        [self uploadWithDic:@{@"key":@"password",@"password":  aesPwd,@"value":aesPwd1}];

    }
}

-(void)uploadWithDic:(NSDictionary *)dic{
    [VULBaseRequest requestWithUrl:@"/api/disk/user/setUserInfo" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest * _Nonnull request) {
        NSString *success =request.responseObject[@"success"];

        if (success.boolValue) {
            if(![self.model.title isEqualToString:KLanguage(@"密码")]){
                if(self.saveAndRefreshBlock){
                    self.saveAndRefreshBlock();
                }
                [self.navigationController popViewControllerAnimated:YES];
                
            }else{
                VULLoginViewController *login = [[VULLoginViewController alloc] init];
                VULNavigationViewController *nav = [[VULNavigationViewController alloc] initWithRootViewController:login];
                nav.modalPresentationStyle = UIModalPresentationOverFullScreen;
                [self.navigationController presentViewController:nav animated:YES completion:nil];
            }

        }else{
            [self dissmissHudView];
            [self makeToast:request.responseObject[@"message"]];

        }
    }];
}
-(UITextField *)textField{
    if (!_textField) {
        _textField = [UITextField new];
        _textField.font = [UIFont yk_pingFangRegular:15];
        _textField.textColor = UIColorHex(#333333);
        _textField.placeholder =  KLanguage(@"请输入");
    }
    return _textField;
}
-(UITextField *)passField{
    if (!_passField) {
        _passField = [UITextField new];
        _passField.font = [UIFont yk_pingFangRegular:15];
        _passField.textColor = UIColorHex(#333333);
        _passField.placeholder =  KLanguage(@"请输入新的密码");
    }
    return _passField;
}
-(UITextField *)againField{
    if (!_againField) {
        _againField = [UITextField new];
        _againField.font = [UIFont yk_pingFangRegular:15];
        _againField.textColor = UIColorHex(#333333);
        _againField.placeholder =  KLanguage(@"请再次输入密码");
    }
    return _againField;
}
-(UIButton *)closebtn{
    if(!_closebtn){
        _closebtn = [UIButton new];
        [_closebtn setImage:VULGetImage(@"icon_mine_delete") forState:UIControlStateNormal];
        [_closebtn addTarget:self action:@selector(clickClosebtn) forControlEvents:UIControlEventTouchUpInside];
    }
    return _closebtn;
}
-(void)clickClosebtn{
    self.textField.text = @"";
    self.closebtn.hidden = YES;
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
