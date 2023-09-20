//
//  VULFileModel.m
//  UnlimitedBusiness
//
//  Created by zuoyi on 2021/12/14.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULFileModel.h"

@implementation VULAllFileModel
+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
        @"current" : @"current",
        @"fileList":@"fileList",
        @"folderList":@"folderList",
    };
}

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{ @"folderList":[VULFileObjectModel class],@"fileList":[VULFileObjectModel class],@"current":[VULFileInfoModel class]};
}

/// @param fileName 名称
/// @param completion 回调
/// @pram fileType folder文件夹 file 文件

+(void)reNameFile:(VULFileObjectModel *)model name:(NSString *)name  type:(NSString *)fileType  completion:(VULRequestCompletion )completon{
    
    NSDictionary *dic;
    dic = @{
        @"dataArr":@[@{
            @"name":name,
            @"oldName":model.name,
            @"parentID":model.parentID,
            @"parentLevel":model.parentLevel,
            @"sourceID":model.sourceID,
            @"type":fileType,
        }
        ],
        @"operation":@"rename"
    };
    [VULBaseRequest requestWithUrl:@"/api/disk/operation" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (completon) {
            completon(request);
        }
    }];
}
+(void)operationFile:(NSMutableArray *)selectArr operationType:(NSString *)operationType completion:(VULRequestCompletion )completon{
    NSMutableArray *dicArr = [NSMutableArray array];
    for (VULFileObjectModel *model  in selectArr ) {
        [dicArr addObject:@{ @"name":model.name,
                                    @"parentID":model.parentID,
                                    @"parentLevel":model.parentLevel,
                                    @"sourceID":model.sourceID,
                             @"type":model.icon}];
    }
    NSDictionary *dic;
    dic = @{
        @"dataArr":dicArr,
        @"operation":operationType
    };
    [VULBaseRequest requestWithUrl:@"/api/disk/operation" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (completon) {
            completon(request);
        }
    }];
}
+(void)copyFile:(NSMutableArray *)selectArr operationType:(NSString *)operationType sourceID:(NSString *)sourceID sourceLevel:(NSString *)sourceLevel completion:(VULRequestCompletion )completon{
    NSMutableArray *dicArr = [NSMutableArray array];
    for (VULFileObjectModel *model  in selectArr ) {
        [dicArr addObject:@{ @"name":model.name,
                                    @"parentID":model.parentID,
                                    @"parentLevel":model.parentLevel,
                                    @"sourceID":model.sourceID,
                             @"type":model.icon}];
    }
    NSDictionary *dic;
    dic = @{
        @"dataArr":dicArr,
        @"operation":operationType,
        @"sourceID":sourceID,
        @"sourceLevel":sourceLevel
    };
    [VULBaseRequest requestWithUrl:@"/api/disk/operation" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (completon) {
            completon(request);
        }
    }];
}
+(void)getDetailConentWithSourceID:(NSString *)sourceID icon:(NSString *)icon completion:(BackModel )completon{

    NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithDictionary:@{@"fileType":@"all",@"currentPage":@(1),@"pageSize":@"1000"} ];
    if (sourceID &&sourceID.length>0){
        [dic setValue:sourceID forKey:@"sourceID"];
    }
    if (icon) {
        if ([icon isEqualToString:@"tag"]) {
            icon = @"fileTag";
        }
        [dic setValue:icon forKey:@"block"];

    }
 
    [VULBaseRequest requestWithUrl:@"/api/disk/list/path" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        
        if (completon) {
            completon(request.responseObject);
        }

    }];
 
}
+(void)getPlayVideoUrlFromModel:(VULFileObjectModel *)model sucess:(BackPth)pathUrl{
    NSDictionary *dic;
    dic = @{
        @"busType":@"cloud",
        @"path":@"info",
        @"sourceID":model.sourceID
        
    };
    if(model.id &&model.id.intValue>0){
        dic = @{
            @"busType":@"cloud",
            @"path":@"info",
            @"sourceID":model.sourceID,
            @"f":model.id
        };
    }
    [VULBaseRequest requestWithUrl:@"/api/disk/preview" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
            NSString *previewUrl = request.data[@"previewUrl"];
            NSString *downloadUrl = request.data[@"downloadUrl"];
            NSString *pptPreviewUrl = request.data[@"pptPreviewUrl"];
            NSArray *preArray = @[@"swf",@"xls",@"xlsx",@"doc",@"pdf",@"docx",@"ppt",@"pptx",@"pps"];
            if ([preArray containsObject:model.fileType]) {
                if (pathUrl) {
                    pathUrl(pptPreviewUrl);
                }
            }else{
                if (previewUrl.length>0) {
                    if (pathUrl) {
                        pathUrl(previewUrl);
                    }
                }else{
                    if (pathUrl) {
                        pathUrl(downloadUrl);
                    }
                }
            }

            

        }else{
            if (pathUrl) {
                pathUrl(model.path);
            }
        }
     
    }];
}
+(void)getPreview:(NSDictionary *)dic completion:(VULRequestCompletion )completon{
    [VULBaseRequest requestWithUrl:@"/api/disk/preview" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (completon) {
            completon(request);
        }
     
    }];
}
@end
@implementation VULFileObjectModel
- (NSString *)description
{
    if (NSStringIsNotEmpty(self.DSdescription)) {
        return[NSString stringWithFormat:@"%@", self.DSdescription];
    }
    return nil;
}
+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
        @"children" : @"children",
        @"DSdescription" : @"description"

    };
}
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{ @"children":[VULAllFileModel class]};
}
- (BOOL)isLeaf {
    return self.children.folderList.count == 0;
}
@end
@implementation VULFileInfoModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
        @"icon" : @"icon",
        @"isChildren" : @"isChildren",
        @"name" : @"name",
        @"pathDesc" : @"pathDesc",
        @"pathDisplay" : @"pathDisplay",
        @"type" : @"type"
    };
}
@end
@implementation VULFileModel
@end
@implementation VULFileZIPObjectModel
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{ @"childList":[VULFileZIPObjectModel class]};
}

@end
@implementation VULInfoModel
@end
@implementation VULInfoTypeModel
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{ @"children":[VULInfoTypeModel class]};
}
@end

