//
//  FBFileNodeChooseView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/21.
//

#import "FBFileNodeChooseView.h"
#import "FBFileNodeCell.h"
static NSString *const fileNodeCell = @"FBFileNodeCell";

@interface FBFileNodeChooseView ()<UITableViewDelegate,FBFileNodeCellDelegate,UITableViewDataSource>

@property (nonatomic, strong) UITableView *tableView;


@end
@implementation FBFileNodeChooseView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor =[UIColor whiteColor];
        [self setView];
    }
    return self;
}
-(void)setView{
    [self addSubview:self.tableView];
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(K_NavBar_Height-fontAuto(20));
        make.left.right.bottom.mas_equalTo(0);
    }];
    
}
-(void)setTreeNodes:(NSMutableArray *)treeNodes{
    _treeNodes = treeNodes;
    // 遍历数组，构建树形结构
  for (VULFileObjectModel * model in _treeNodes) {
          model.isExpanded = YES;
          [self setLevelWithModel:model withLevel:0];
         
      }
    [self.tableView reloadData];
}
-(void)setLevelWithModel:(VULFileObjectModel *)model withLevel:(NSInteger)level{
    model.level = level;

    if (model.children.folderList.count>0) {
        for (VULFileObjectModel *model1 in model.children.folderList) {
            model1.level = level+1;
            if (model.sourceID) {
                NSMutableArray *arr = [NSMutableArray array];
                [arr addObjectsFromArray:model.titleArr];
                [arr addObject:model.name.length>0?model.name:model.groupName];
                NSMutableArray *arr1 = [NSMutableArray array];
                [arr1 addObjectsFromArray:model.sourceIDArr];
                [arr1 addObject:model.sourceID];
                model1.titleArr = [NSMutableArray arrayWithArray:arr];
                model1.sourceIDArr = [NSMutableArray arrayWithArray:arr1];
            }

 
            if (model1.children.folderList.count>0) {
                [self setLevelWithModel:model1 withLevel:   model1.level];
            }
        }
    }
}
#pragma mark - UITableViewDelegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self getVisibleNodes].count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
  
    FBFileNodeCell *cell = [FBFileNodeCell dequeueReusableCellWithTableView:tableView reuseIdentifier:fileNodeCell];
    cell.separatorLine.hidden  = YES;
    VULFileObjectModel *node = [self getVisibleNodes][indexPath.row];
    cell.delegate = self;
    cell.model = node;

      return cell;
}
- (void)fileNodeCell:(FBFileNodeCell *)cell didTapExpandButton:(UIButton *)expandButton {
    NSIndexPath *indexPath = [self.tableView indexPathForCell:cell];
    [self reloadDataWtihIndexPath:indexPath];
}
- (void)selectCell:(FBFileNodeCell *)cell {
    NSIndexPath *indexPath = [self.tableView indexPathForCell:cell];

    VULFileObjectModel *node = [self getVisibleNodes][indexPath.row];
    if (node.level == 0) {
        if ([node.icon isEqualToString:@"tag"] || [node.icon isEqualToString:@"fileTag"]) {
            node.icon = [self newWithIcon:node.icon];
            if (self.backChooseWithModel) {
                self.backChooseWithModel(node);
            }
        }else{
            [self reloadDataWtihIndexPath:indexPath];

        }
    }else{
        if (self.backChooseWithModel) {
            self.backChooseWithModel(node);
        }
    }
}
-(NSString *)newWithIcon:(NSString *)icon{
    if ([icon isEqualToString:@"position"]) {
        return @"files";
    }
    if ([icon isEqualToString:@"tool"]) {
        return @"tools";
    }
    if ([icon isEqualToString:@"fileType"]) {
        return @"fileType";
    }
    if ([icon isEqualToString:@"tag"]) {
        return @"fileTag";
    }
    return icon;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];

//    VULFileObjectModel *node = [self getVisibleNodes][indexPath.row];
//    if (node.level == 0) {
//        [self reloadDataWtihIndexPath:indexPath];
//    }else{
//        if (self.backChooseWithModel) {
//            self.backChooseWithModel(node);
//        }
//    }
   

}
-(void)reloadDataWtihIndexPath:(NSIndexPath *)indexPath{
    VULFileObjectModel *node = [self getVisibleNodes][indexPath.row];
    if (node.children.folderList.count > 0) {
    // 如果有子节点，则可以展开/折叠
     node.isExpanded = !node.isExpanded;
        NSMutableArray<NSIndexPath *> *indexPaths = [NSMutableArray array];
        FBFileNodeCell *cell = [self.tableView cellForRowAtIndexPath:indexPath];
        [cell.upOrDownBtn setImage:node.children.folderList.count > 0 ? (node.isExpanded ? [UIImage imageNamed:@"icon_open"] : [UIImage imageNamed:@"icon_close"]) : nil forState:UIControlStateNormal];
        if ( node.isExpanded) {
            [indexPaths addObjectsFromArray:[self getIndexPathsForVisibleRowsFromNode:node withRow:indexPath.row]];
            [self.tableView beginUpdates];
            [self.tableView insertRowsAtIndexPaths:indexPaths withRowAnimation:UITableViewRowAnimationFade];

            [self.tableView endUpdates];

        }else{
            [self.tableView beginUpdates];
            NSArray *indexArr =[self getIndexPathsForNoVisibleRowsFromNode:node withRow:indexPath.row];
            if (indexArr.count>0) {
                [self.tableView deleteRowsAtIndexPaths:indexArr withRowAnimation:UITableViewRowAnimationFade];
                [self.tableView endUpdates];

            }
        }

    }else{
        if ( node.isload) {
//            已经加载过了 说明没数据了
            return;
        }
        node.children = [VULAllFileModel new];
        node.children.folderList = [NSMutableArray array];
        if([node.icon isEqualToString:@"info"]){
//            [VULBaseRequest requestWithUrl:@"/api/disk/getInfoTypeList" params:@{@"currentPage":@(1),@"fileType":@"folder",@"pageSize":@(500)} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
//                node.isload = YES;
//
//                NSArray *arr = request.data;
//                NSMutableArray *folderList = [NSMutableArray array];
//                for(NSDictionary *dic in arr){
//                    VULFileObjectModel *model = [VULFileObjectModel modelWithDictionary:dic];
////                    folder
//                    model.icon = @"info";
//                    model.name = model.typeName;
//                    model.sourceID = model.infoTypeID;
//                    [folderList addObject:model];
//                }
//                node.sourceID = @"10000";
//                [node.children.folderList addObjectsFromArray:folderList];
//                [self setLevelWithModel:node withLevel:node.level];
//                [self reloadDataWtihIndexPath:indexPath];
//            }];
        }else{
           
            [VULAllFileModel getDetailConentWithSourceID:node.sourceID icon:node.icon completion:^(__kindof id  _Nonnull responseObject) {
                node.isload = YES;
                VULAllFileModel *model =[VULAllFileModel modelWithDictionary:responseObject[@"data"]];

                [node.children.folderList addObjectsFromArray:model.folderList];
                [self setLevelWithModel:node withLevel:node.level];
                [self reloadDataWtihIndexPath:indexPath];
                        
            }];
        }

        
     
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

#pragma mark - Helper methods
-(NSArray<VULFileObjectModel *> *)getVisibleNodes {
    NSMutableArray<VULFileObjectModel *> *visibleNodes = [NSMutableArray array];

    for (VULFileObjectModel *node in self.treeNodes) {
       [visibleNodes addObject:node];
       if (node.isExpanded) {
          [visibleNodes addObjectsFromArray:[self getVisibleNodesFromNode:node]];
       }
    }
    //隐藏工具箱
    NSMutableArray *arr =  [NSMutableArray array];
    for(VULFileObjectModel *model in  visibleNodes){
        if(![model.icon isEqualToString:@"toolbox"]){
            [arr addObject:model];
        }
    }

    return arr;
}
-(NSArray<VULFileObjectModel *> *)getVisibleNodesFromNode:(VULFileObjectModel *)node {
    NSMutableArray<VULFileObjectModel *> *visibleNodes = [NSMutableArray array];

    for (VULFileObjectModel *child in node.children.folderList) {
       [visibleNodes addObject:child];
       if (child.isExpanded) {
        [visibleNodes addObjectsFromArray:[self getVisibleNodesFromNode:child]];
      }
    }
    return visibleNodes;
}



- (NSArray<NSIndexPath *> *)getIndexPathsForVisibleRowsFromNode:(VULFileObjectModel *)node withRow:(NSInteger )row {
    NSMutableArray<NSIndexPath *> *indexPaths = [NSMutableArray array];
    NSInteger count = node.children.folderList.count;
    NSInteger num = 0;
    for (int i=0 ; i<count; i++) {
        VULFileObjectModel *child = node.children.folderList[i];
        //隐藏工具箱

        if(![child.icon isEqualToString:@"toolbox"]){
      
            [indexPaths addObject:[NSIndexPath indexPathForRow:row+num+1 inSection:0]];
            num = num+1;
        }
    }
    
//    for (NSInteger i = 0; i < node.children.folderList.count; i++) {
//        VULFileObjectModel *child = node.children.folderList[i];
//        NSInteger visibleIndex = [[self getVisibleNodes] indexOfObject:child];
//        NSIndexPath *indexPath = [NSIndexPath indexPathForRow:visibleIndex inSection:0];
//        [indexPaths addObject:indexPath];
////        if (child.isExpanded) {
////            [indexPaths addObjectsFromArray:[self getIndexPathsForVisibleRowsFromNode:child]];
////        }
//    }
    
    return indexPaths;
}
- (NSArray<NSIndexPath *> *)getIndexPathsForNoVisibleRowsFromNode:(VULFileObjectModel *)node withRow:(NSInteger )row {
    NSMutableArray<NSIndexPath *> *indexPaths = [NSMutableArray array];
    NSInteger count = 0;
        for (NSInteger i = 0; i < node.children.folderList.count; i++) {
          
        VULFileObjectModel *child = node.children.folderList[i];
            //隐藏工具箱

            if(![child.icon isEqualToString:@"toolbox"]){
                count++;
            }
        if (child.children.folderList.count>0 && child.isExpanded) {
            count =  count+ [self getArrWithFromNode:child];
        }
            child.isExpanded = NO;
        
    }
    
    for (int i=0 ; i<count; i++) {
        [indexPaths addObject:[NSIndexPath indexPathForRow:row+i+1 inSection:0]];
    }
    
    return indexPaths;
}
-(NSInteger )getArrWithFromNode:(VULFileObjectModel *)node{
    NSInteger count = 0;
    for (NSInteger i = 0; i < node.children.folderList.count; i++) {
            count++;
        VULFileObjectModel *child = node.children.folderList[i];
  
        if (child.children.folderList.count>0 && child.isExpanded) {
            count = count+ [self getArrWithFromNode:child];
        }
        child.isExpanded = NO;

    }
    return count;
}


#pragma mark - Setting&&Getting
- (UITableView *)tableView{
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_HEIGHT) style:UITableViewStylePlain];
        _tableView.backgroundColor = [UIColor whiteColor];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        
        _tableView.tableFooterView = [UIView new];
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        [_tableView registerClass:[FBFileNodeCell class] forCellReuseIdentifier:fileNodeCell];
    }
    return _tableView;
}
@end
