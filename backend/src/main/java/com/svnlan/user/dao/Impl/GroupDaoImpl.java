package com.svnlan.user.dao.Impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.jooq.tables.records.GroupsRecord;
import com.svnlan.jooq.tables.records.IoSourceRecord;
import com.svnlan.tools.JSONObjectRecordMapper;
import com.svnlan.user.dao.GroupDao;
import com.svnlan.user.domain.Group;
import com.svnlan.user.dto.GroupDTO;
import com.svnlan.user.service.impl.OverviewServiceImpl;
import com.svnlan.user.vo.GroupParentPathDisplayVo;
import com.svnlan.user.vo.GroupSizeVo;
import com.svnlan.user.vo.GroupVo;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.svnlan.jooq.tables.GroupMeta.GROUP_META;
import static com.svnlan.jooq.tables.GroupSource.GROUP_SOURCE;
import static com.svnlan.jooq.tables.Groups.GROUPS;
import static com.svnlan.jooq.tables.IoSource.IO_SOURCE;
import static com.svnlan.jooq.tables.UserGroup.USER_GROUP;

@Service
public class GroupDaoImpl implements GroupDao {
    @Autowired
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    @Resource
    private JSONObjectRecordMapper jsonObjectRecordMapper;

    @Override
    public int insert(Group group, Long tenantId) {
        tenantId = ObjectUtils.isEmpty(tenantId) ? tenantUtil.getTenantIdByServerName() : tenantId;
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(GROUPS)
                .columns(GROUPS.NAME, GROUPS.PARENT_ID, GROUPS.PARENT_LEVEL, GROUPS.EXTRA_FIELD, GROUPS.SORT, GROUPS.SIZE_MAX,
                        GROUPS.SIZE_USE, GROUPS.MODIFY_TIME, GROUPS.CREATE_TIME, GROUPS.STATUS, GROUPS.TENANT_ID, GROUPS.DING_DEPT_ID, GROUPS.THIRD_DEPT_ID)
                .values(group.getName(), group.getParentID(), group.getParentLevel(), group.getExtraField()==null?"":group.getExtraField(), group.getSort(), group.getSizeMax(),
                        0L,now, now, 1, tenantId, ObjectUtils.isEmpty(group.getDingDeptId()) ? 0L : group.getDingDeptId(), group.getThirdDeptId()==null?"":group.getThirdDeptId())
                .returning(GROUPS.ID).fetchOne().getId();
        group.setGroupID(id);
        return 1;
    }

    @Override
    public int update(Group group) {
        UpdateSetMoreStep<GroupsRecord> set = context.update(GROUPS)
                .set(GROUPS.NAME, group.getName())
                .set(GROUPS.SIZE_MAX, group.getSizeMax())
                .set(GROUPS.MODIFY_TIME, LocalDateTime.now());
        if(group.getParentID() != null){
            set.set(GROUPS.PARENT_ID,group.getParentID());
        }
        if(group.getParentLevel() != null){
            set.set(GROUPS.PARENT_LEVEL,group.getParentLevel());
        }
        return  set.where(GROUPS.ID.eq(group.getGroupID())).execute();
    }

    @Override
    public int updateGroupSourceName(Map<String, Object> map) {
        UpdateSetMoreStep<IoSourceRecord> set = context.update(IO_SOURCE)
                .set(IO_SOURCE.NAME, (String) map.get("name"))
                .set(IO_SOURCE.MODIFY_TIME, LocalDateTime.now());

        if(map.get("parentID") != null){
            set.set(IO_SOURCE.PARENT_ID, (Long) map.get("parentID"));
        }
        if(map.get("parentLevel") != null){
            set.set(IO_SOURCE.PARENT_LEVEL,(String) map.get("parentLevel"));
        }
        return set.where(IO_SOURCE.TARGET_TYPE.eq(2).and(IO_SOURCE.ID.eq((Long) map.get("sourceID")))
                .and(IO_SOURCE.IS_FOLDER.eq(1)).and(IO_SOURCE.IS_DELETE.eq(0))).execute();
    }

    @Override
    public List<Group> getGroupList(GroupDTO groupDTO) {
        SelectOnConditionStep select = context.selectDistinct(GROUPS.ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, GROUPS.SORT, GROUPS.SIZE_MAX, GROUPS.SIZE_USE, GROUPS.STATUS,
                        DSL.count(DSL.field("gCh.parent_id")).as("hasChildren"))
                .from(GROUPS).leftJoin(GROUPS.as("gCh")).on(GROUPS.ID.eq(DSL.field("gCh.parent_id", Long.class)));
        if( groupDTO.getKeyword() != null && !groupDTO.getKeyword().equals("")){
            select.leftJoin(GROUP_META).on(GROUP_META.GROUP_ID.eq(GROUPS.ID).and(GROUP_META.KEY_STRING.in("namePinyin","namePinyinSimple")));
        }
        SelectConditionStep where = null;
        if(groupDTO.getStatus() != null){
            where = select.where(GROUPS.STATUS.eq(groupDTO.getStatus()));
        }else {
            where = select.where(GROUPS.STATUS.in(0,1));
        }
        if(groupDTO.getParentID() != null){
            where.and(GROUPS.PARENT_ID.eq(groupDTO.getParentID()));
        }
        if( groupDTO.getKeyword() != null && !groupDTO.getKeyword().equals("")){
            where.and(
                    ( GROUPS.NAME.like(DSL.concat("%",groupDTO.getKeyword(),"%")).or(GROUP_META.VALUE_TEXT.like(DSL.concat("%",groupDTO.getKeyword(),"%"))))
            );
        }
        where.and(GROUPS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));
        where.groupBy(GROUPS.ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, GROUPS.SORT, GROUPS.SIZE_MAX, GROUPS.SIZE_USE, GROUPS.STATUS);
        where.orderBy(GROUPS.SORT.asc(),GROUPS.CREATE_TIME.asc());
        return where.fetchInto(Group.class);
    }

    @Override
    public List<Group> searchGroupList(GroupDTO groupDTO) {
        SelectConditionStep where = context.selectDistinct(GROUPS.ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, GROUPS.SORT, GROUPS.SIZE_MAX, GROUPS.SIZE_USE, GROUPS.STATUS)
                .from(GROUPS)
                .leftJoin(GROUP_META).on(GROUP_META.GROUP_ID.eq(GROUPS.ID).and(GROUP_META.KEY_STRING.in("namePinyin", "namePinyinSimple")))
                .where(GROUPS.STATUS.in(0, 1));
        where.and(GROUPS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));
        if( groupDTO.getKeyword() != null && !groupDTO.getKeyword().equals("")){
            where.and(
                    ( GROUPS.NAME.like(DSL.concat("%",groupDTO.getKeyword(),"%")).or(GROUP_META.VALUE_TEXT.like(DSL.concat("%",groupDTO.getKeyword(),"%"))))
            );
        }
        return where.orderBy(GROUPS.SORT.asc()).fetchInto(Group.class);
    }

    @Override
    public int updateGroupState(GroupDTO groupDTO) {
        return context.update(GROUPS)
                .set(GROUPS.STATUS,groupDTO.getStatus())
                .set(GROUPS.MODIFY_TIME,LocalDateTime.now())
                .where(GROUPS.ID.eq(groupDTO.getGroupID())).execute();
    }

    @Override
    public Integer updateGroupSort(List<Group> list) {
        int size = 0;
        for (Group group:list) {
            size += context.update(GROUPS)
                    .set(GROUPS.SORT, group.getSort())
                    .where(GROUPS.ID.eq(group.getGroupID()))
                    .execute();
        }
        return size;
    }

    @Override
    public Group getTopGroup() {
        return context.select(GROUPS.ID.as("groupID"),GROUPS.NAME,GROUPS.SIZE_MAX,GROUPS.SIZE_USE,GROUPS.PARENT_ID.as("parentID"))
                .from(GROUPS)
                .where(GROUPS.PARENT_ID.eq(0L))
                .orderBy(GROUPS.ID.asc())
                .limit(1)
                .fetchOneInto(Group.class);
    }

    @Override
    public Integer getMaxSort(Long parentID) {
        return context.select(GROUPS.SORT)
                .from(GROUPS)
                .where(GROUPS.STATUS.in(0,1).and(GROUPS.PARENT_ID.eq(parentID)))
                .orderBy(GROUPS.SORT.desc(),GROUPS.CREATE_TIME.desc())
                .limit(1).fetchOneInto(Integer.class);
    }

    @Override
    public List<Group> getGroupInfoList(List<Long> list) {
        return context.select(GROUPS.ID.as("groupID"),GROUPS.NAME,GROUPS.SIZE_MAX,GROUPS.SIZE_USE,GROUPS.PARENT_ID.as("parentID"))
                .from(GROUPS)
                .where(GROUPS.ID.in(list)).fetchInto(Group.class);
    }

    @Override
    public GroupVo getGroupAndSourceByID(Long groupID) {
        return context.selectDistinct(GROUPS.ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, GROUPS.SORT, GROUPS.SIZE_MAX, GROUPS.SIZE_USE, GROUPS.STATUS,
                        GROUP_SOURCE.SOURCE_ID.as("sourceID"),IO_SOURCE.PARENT_ID.as("sourceParentID"),IO_SOURCE.PARENT_LEVEL.as("sourceParentLevel"))
                .from(GROUPS)
                .leftJoin(GROUP_SOURCE).on(GROUPS.ID.eq(GROUP_SOURCE.GROUP_ID))
                .leftJoin(IO_SOURCE).on(GROUP_SOURCE.SOURCE_ID.eq(IO_SOURCE.ID))
                .where(GROUPS.STATUS.in(0,1).and(GROUPS.ID.eq(groupID)))
                .fetchOneInto(GroupVo.class);
    }

    @Override
    public List<UserGroupVo> getGroupVoList(List<Long> list) {
        SelectConditionStep where = context.selectDistinct(GROUPS.ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, GROUPS.SORT, GROUPS.SIZE_MAX, GROUPS.SIZE_USE, GROUPS.STATUS,
                        DSL.count(DSL.field("gCh.parent_id")).as("hasChildren"))
                .from(GROUPS).leftJoin(GROUPS.as("gCh")).on(GROUPS.ID.eq(DSL.field("gCh.parent_id", Long.class)))
                .where(GROUPS.STATUS.eq(1));
        if(list != null){
            where.and(GROUPS.ID.in(list));
        }else {
            where.and(GROUPS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));
        }
        where.groupBy(GROUPS.ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, GROUPS.SORT, GROUPS.SIZE_MAX, GROUPS.SIZE_USE, GROUPS.STATUS);
        where.orderBy(GROUPS.SORT.asc(),GROUPS.CREATE_TIME.asc());
        return where.fetchInto(UserGroupVo.class);
    }

    @Override
    public List<UserGroupVo> getMainGroupVoList(HomeExplorerDTO shareDTO) {
        SelectOnConditionStep select = context.selectDistinct(GROUPS.ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, GROUPS.SORT, GROUPS.SIZE_MAX, GROUPS.SIZE_USE, GROUPS.STATUS,
                        DSL.count(DSL.field("gCh.parent_id")).as("hasChildren"))
                .from(GROUPS).leftJoin(GROUPS.as("gCh")).on(GROUPS.ID.eq(DSL.field("gCh.parent_id", Long.class)));
        if( shareDTO.getKeyword() != null && !shareDTO.getKeyword().equals("")){
            select.leftJoin(GROUP_META).on(GROUP_META.GROUP_ID.eq(GROUPS.ID).and(GROUP_META.KEY_STRING.in("namePinyin","namePinyinSimple")));
        }
        SelectConditionStep where = select.where(GROUPS.STATUS.eq(1));
        if (shareDTO.getKeyword() == null || shareDTO.getKeyword().equals("")){
            where.and(GROUPS.PARENT_ID.eq(0L));
        }
        if( shareDTO.getKeyword() != null && !shareDTO.getKeyword().equals("")){
            where.and(
                    ( GROUPS.NAME.like(DSL.concat("%",shareDTO.getKeyword(),"%")).or(GROUP_META.VALUE_TEXT.like(DSL.concat("%",shareDTO.getKeyword(),"%"))))
            );
        }
        where.and(GROUPS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));
        where.groupBy(GROUPS.ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, GROUPS.SORT, GROUPS.SIZE_MAX, GROUPS.SIZE_USE, GROUPS.STATUS);
        where.orderBy(GROUPS.SORT.asc(),GROUPS.CREATE_TIME.asc());
        return where.fetchInto(UserGroupVo.class);
    }

    @Override
    public Long sumGroupSpaceUsed() {
        return context.select(DSL.sum(GROUPS.SIZE_USE))
                .from(GROUPS)
                .where(GROUPS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(Long.class);
    }

    @Override
    public Group getGroupInfoByID(Long groupID) {
        return context.select(GROUPS.ID.as("groupID"),GROUPS.NAME,GROUPS.SIZE_MAX,GROUPS.SIZE_USE,GROUPS.PARENT_ID)
                .from(GROUPS)
                .where(GROUPS.ID.eq(groupID)).fetchOneInto(Group.class);
    }

    @Override
    public List<Group> getGroupUserCountList(List<Long> list) {
        return context.select(USER_GROUP.GROUP_ID.as("groupID"),DSL.count(USER_GROUP.GROUP_ID).as("hasChildren"))
                .from(USER_GROUP)
                .where(USER_GROUP.GROUP_ID.in(list))
                .groupBy(USER_GROUP.GROUP_ID)
                .fetchInto(Group.class);
    }

    @Override
    public List<Long> getUserIdByGroupId(List<Long> deptIdList) {
        return context.select(USER_GROUP.USER_ID).from(USER_GROUP)
                .where(USER_GROUP.GROUP_ID.in(deptIdList))
                .fetchInto(Long.class);
    }

    @Override
    public List<Long> getGroupIdByUserId(Long userId) {
        return context.select(USER_GROUP.GROUP_ID).from(USER_GROUP).where(USER_GROUP.USER_ID.eq(userId)).fetchInto(Long.class);

    }

    @Override
    public List<JSONObject> selectGroupSpaceUsage(Long tenantId) {
       return context.select(GROUPS.ID.as("gid"),GROUPS.NAME.as("n"),GROUPS.SIZE_USE.as("s"))
                .from(GROUPS)
                .where(GROUPS.STATUS.in(0,1))
                .and(GROUPS.TENANT_ID.eq(tenantId))
               .fetch(jsonObjectRecordMapper);
    }

    @Override
    public List<GroupParentPathDisplayVo> getGroupParentPathDisplay(Map<String, Object> map) {

        return
        context.select(DSL.field("gs.parent_level",String.class),DSL.groupConcat(GROUPS.ID).as("parentIDStr"),
                        DSL.groupConcat(GROUPS.NAME).as("parentLevelName"))
                .from(GROUPS)
                .join(GROUPS.as("gs")).on(DSL.field("gs.parent_level",String.class).in((List<String>) map.get("parent_level")).and(DSL.field("gs.parent_level",String.class).like(DSL.concat("%,", DSL.concat(DSL.cast(GROUPS.ID,String.class),",%")))))
                .where(GROUPS.ID.in((List<Long>)map.get("ids")))
                .groupBy(DSL.field("gs.parent_level",String.class)).fetchInto(GroupParentPathDisplayVo.class);

    }

    @Override
    public List<Object> selectParentLevel(List<Long> ids) {
        return context.select(GROUPS.PARENT_LEVEL.as("parentLevel"))
                .from(GROUPS)
                .where(GROUPS.ID.in(ids))
                .fetchInto(Object.class);
    }

    @Override
    public void syncBatchUpdateGroupMemoryList(List<GroupSizeVo> list){

        List<UpdateConditionStep<GroupsRecord>> updates = new ArrayList<>();
        for (GroupSizeVo dto : list) {
            updates.add(context.update(GROUPS)
                    .set(GROUPS.SIZE_USE, dto.getSizeUse())
                    .where(GROUPS.ID.eq(dto.getGroupID())));
        }
        context.batch(updates).execute();
    }

    @Override
    public List<GroupSizeVo> getGroupSizeList(Long groupID){

        SelectConditionStep where =
        context.select(DSL.field("gs.group_id",Long.class),DSL.field("io.size",Long.class).as("sizeUse"))
                        .from(GROUP_SOURCE.as("gs"))
                        .join(GROUPS.as("g")).on(DSL.field("gs.group_id",Long.class).eq(DSL.field("g.id",Long.class)).and(DSL.field("g.status",Integer.class).eq(1)))
                        .join(IO_SOURCE.as("io")).on(DSL.field("gs.source_id",Long.class).eq(DSL.field("io.id",Long.class))
                        .and(DSL.field("io.is_folder",Integer.class).eq(1)).and(DSL.field("io.is_delete",Integer.class).eq(0)))
                        .where(DSL.field("io.size",Long.class).gt(0L));

        if (groupID > 0){
            where.and(DSL.field("gs.group_id",Long.class).eq(groupID));
        }
        return where.fetchInto(UserGroupVo.class);
    }

    @Override
    public List<Long> getDingDeptIds(Long tenantId, String tag) {
        tenantId = ObjectUtils.isEmpty(tenantId) ? tenantUtil.getTenantIdByServerName() : tenantId;
        return context.select(GROUPS.DING_DEPT_ID)
                .from(GROUPS)
                .where(GROUPS.TENANT_ID.eq(tenantId))
                .and(GROUPS.STATUS.eq(1)).and(GROUPS.EXTRA_FIELD.eq(tag))
                .fetchInto(Long.class);
    }
    @Override
    public List<Group> getThirdGroupParent(String tag, Long groupId,String name){
        return context.select(GROUPS.ID.as("groupID"),GROUPS.NAME,GROUPS.SIZE_MAX,GROUPS.SIZE_USE,GROUPS.PARENT_ID,GROUPS.EXTRA_FIELD)
                .from(GROUPS)
                .where(GROUPS.PARENT_ID.eq(groupId)).and(GROUPS.EXTRA_FIELD.eq(tag).or(GROUPS.NAME.eq(name)))
                .fetchInto(Group.class);
    }
    @Override
    public int updateGroupThirdTag(String tag, Long groupId) {
        return context.update(GROUPS)
                .set(GROUPS.EXTRA_FIELD,tag)
                .where(GROUPS.ID.eq(groupId)).execute();
    }
    @Override
    public GroupVo getGroupAndSourceByDeptId(Long tenantId, Long deptId, String tag) {
        return context.selectDistinct(GROUPS.ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, GROUPS.SORT, GROUPS.SIZE_MAX, GROUPS.SIZE_USE, GROUPS.STATUS,
                GROUP_SOURCE.SOURCE_ID.as("sourceID"),IO_SOURCE.PARENT_ID.as("sourceParentID"),IO_SOURCE.PARENT_LEVEL.as("sourceParentLevel"))
                .from(GROUPS)
                .leftJoin(GROUP_SOURCE).on(GROUPS.ID.eq(GROUP_SOURCE.GROUP_ID))
                .leftJoin(IO_SOURCE).on(GROUP_SOURCE.SOURCE_ID.eq(IO_SOURCE.ID))
                .where(GROUPS.DING_DEPT_ID.eq(deptId).and(GROUPS.EXTRA_FIELD.eq(tag)).and(GROUPS.TENANT_ID.eq(tenantId)).and(GROUPS.STATUS.in(0,1)))
                .fetchOneInto(GroupVo.class);
    }
    /* 根据tenantId获取默认企业网盘group info */
    @Override
    public GroupVo getTopGroupByTenantId(Long tenantId) {
        return context.selectDistinct(GROUPS.ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, GROUPS.SORT, GROUPS.SIZE_MAX, GROUPS.SIZE_USE, GROUPS.STATUS,
                GROUP_SOURCE.SOURCE_ID.as("sourceID"),IO_SOURCE.PARENT_ID.as("sourceParentID"),IO_SOURCE.PARENT_LEVEL.as("sourceParentLevel"))
                .from(GROUPS)
                .leftJoin(GROUP_SOURCE).on(GROUPS.ID.eq(GROUP_SOURCE.GROUP_ID))
                .leftJoin(IO_SOURCE).on(GROUP_SOURCE.SOURCE_ID.eq(IO_SOURCE.ID))
                .where(GROUPS.TENANT_ID.eq(tenantId).and(GROUPS.STATUS.in(0,1))).orderBy(GROUPS.CREATE_TIME.asc()).limit(1)
                .fetchOneInto(GroupVo.class);
    }
    @Override
    public List<String> getThirdDeptIds(Long tenantId, String tag) {
        tenantId = ObjectUtils.isEmpty(tenantId) ? tenantUtil.getTenantIdByServerName() : tenantId;
        return context.select(GROUPS.DING_DEPT_ID)
                .from(GROUPS)
                .where(GROUPS.TENANT_ID.eq(tenantId))
                .and(GROUPS.STATUS.eq(1)).and(GROUPS.EXTRA_FIELD.eq(tag))
                .fetchInto(String.class);
    }

    @Override
    public GroupVo getGroupAndSourceByDingDeptId(Long tenantId, Long deptId, String tag) {
        return context.selectDistinct(GROUPS.ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, GROUPS.SORT, GROUPS.SIZE_MAX, GROUPS.SIZE_USE, GROUPS.STATUS,
                GROUP_SOURCE.SOURCE_ID.as("sourceID"),IO_SOURCE.PARENT_ID.as("sourceParentID"),IO_SOURCE.PARENT_LEVEL.as("sourceParentLevel"))
                .from(GROUPS)
                .leftJoin(GROUP_SOURCE).on(GROUPS.ID.eq(GROUP_SOURCE.GROUP_ID))
                .leftJoin(IO_SOURCE).on(GROUP_SOURCE.SOURCE_ID.eq(IO_SOURCE.ID))
                .where(GROUPS.DING_DEPT_ID.eq(deptId).and(GROUPS.EXTRA_FIELD.eq(tag)).and(GROUPS.TENANT_ID.eq(tenantId)).and(GROUPS.STATUS.in(0,1)))
                .fetchOneInto(GroupVo.class);
    }
}
