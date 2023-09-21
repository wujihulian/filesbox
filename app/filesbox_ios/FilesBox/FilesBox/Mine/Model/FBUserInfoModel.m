//
//  FBUserInfoModel.m
//  FilesBox
//
//  Created by 无极互联 on 2023/7/17.
//

#import "FBUserInfoModel.h"

@implementation FBUserInfoModel

+(NSArray * )getArrayWithModel:(VULSaveUserInformation *)model{
    NSMutableArray *firstArr = [NSMutableArray array];
    NSMutableArray *secondArr = [NSMutableArray array];

//    self.titleArray = @[@[KLanguage(@"头像"),KLanguage(@"账号"),KLanguage(@"昵称"),KLanguage(@"空间"),KLanguage(@"性别"),KLanguage(@"邮箱"),KLanguage(@"手机"),KLanguage(@"密码")],@[KLanguage(@"企业微信"),KLanguage(@"钉钉"),KLanguage(@"微信")]];

    [firstArr addObject:@{@"title":KLanguage(@"头像"),
                          @"detail":model.avatar,
                          @"isBtn":@(0),
                          @"rightBtn":@(1),
                          @"imageShow":@(1)}];
    
    [firstArr addObject:@{@"title":KLanguage(@"账号"),
                          @"detail":model.name,
                          @"isBtn":@(0),
                          @"rightBtn":@(0),
                          @"imageShow":@(0)}];
    
    [firstArr addObject:@{@"title":KLanguage(@"昵称"),
                          @"detail":model.nickname,
                          @"isBtn":@(0),
                          @"rightBtn":@(1),
                          @"imageShow":@(0)}];
    
    NSString *detial = @"";
    if (model.sizeMax.intValue > 0) {
        detial = [NSString stringWithFormat:@"%@/%@GB",[FBUserInfoModel formattedFileSize:model.sizeUse.longLongValue],model.sizeMax];
    

    }else{
        detial = [NSString stringWithFormat:@"%@/%@",[self formattedFileSize:model.sizeUse.longLongValue],KLanguage(@"无限制") ];
    }
    [firstArr addObject:@{@"title":KLanguage(@"空间"),
                          @"detail":detial,
                          @"isBtn":@(0),
                          @"rightBtn":@(1),
                          @"imageShow":@(0)}];
    
    [firstArr addObject:@{@"title":KLanguage(@"性别"),
                          @"detail":model.sex.boolValue?KLanguage(@"男"):KLanguage(@"女"),
                          @"isBtn":@(0),
                          @"rightBtn":@(1),
                          @"imageShow":@(0)}];
    
    [firstArr addObject:@{@"title":KLanguage(@"邮箱"),
                          @"detail":model.email,
                          @"isBtn":@(0),
                          @"rightBtn":@(1),
                          @"imageShow":@(0)}];
    
    [firstArr addObject:@{@"title":KLanguage(@"手机"),
                          @"detail":model.phone,
                          @"isBtn":@(0),
                          @"rightBtn":@(1),
                          @"imageShow":@(0)}];
    
    [firstArr addObject:@{@"title":KLanguage(@"密码"),
                          @"detail":KLanguage(@"重置密码"),
                          @"isBtn":@(0),
                          @"rightBtn":@(1),
                          @"imageShow":@(0)}];
    
    for(NSDictionary *dic in firstArr){
        FBUserInfoModel *model = [FBUserInfoModel modelWithDictionary:dic];
        [secondArr addObject:model];
    }
    return secondArr;
    
}
+ (NSString *)formattedFileSize:(long long)bytes {
    CGFloat convertedValue;
    NSString *sizeSuffix;

    if (bytes >= 1024*1024*1024) {
        convertedValue = bytes / (1024.0 * 1024.0 * 1024.0);
        sizeSuffix = @"GB";
    } else {
        convertedValue = bytes / (1024.0 * 1024.0);
        sizeSuffix = @"MB";
    }
    NSString *result = [NSString stringWithFormat:@"%.2f%@", convertedValue, sizeSuffix];
    return result;
}

@end
