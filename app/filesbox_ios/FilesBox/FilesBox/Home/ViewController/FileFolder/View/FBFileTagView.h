//
//  FBFileTagView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/22.
//

#import <UIKit/UIKit.h>
#import <TTGTagCollectionView/TTGTextTagCollectionView.h>

NS_ASSUME_NONNULL_BEGIN
@interface FBTagModel:NSObject
@property (nonatomic, copy) NSString *createTime;
@property (nonatomic, copy) NSString *enNameSimple;
@property (nonatomic, copy) NSString *labelEnName;
@property (nonatomic, copy) NSString *labelId;
@property (nonatomic, copy) NSString *labelName;
@property (nonatomic, copy) NSString *modifyTime;
@property (nonatomic, copy) NSString *sort;
@property (nonatomic, copy) NSString *style;
@property (nonatomic, copy) NSString *image;

@property (nonatomic, copy) NSString *userID;
@property (nonatomic, assign) BOOL isEdit;

@end

@interface FBFileTagView : UIView
@property (nonatomic, strong) TTGTagCollectionView *tagCollectionView;
@property (nonatomic, copy) void (^ selectCollectType)(NSInteger index);
@property (nonatomic, copy) void (^ deleteWithIndex)(NSInteger index);

- (void)reload;

// Add tag with detalt config
- (void)addTag:(FBTagModel *)tag;

- (void)addTags:(NSArray <FBTagModel *> *)tags;

- (void)removeTag:(FBTagModel *)tag;

- (void)removeTagAtIndex:(NSUInteger)index;

- (void)removeAllTags;

-(void)selectCellWtihIndex:(NSInteger) index;
-(void)selectOneCellWtihIndex:(NSInteger) index;

@end

NS_ASSUME_NONNULL_END
