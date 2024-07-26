package com.svnlan.user.dao.Impl;

import com.alibaba.fastjson.JSONObject;

import static com.svnlan.jooq.Tables.USERS;
import static com.svnlan.jooq.tables.UserMeta.*;
import static com.svnlan.jooq.tables.Groups.GROUPS;
import static com.svnlan.jooq.tables.Role.*;
import static com.svnlan.jooq.tables.UserGroup.USER_GROUP;
import com.svnlan.jooq.tables.records.UsersRecord;
import com.svnlan.tools.JSONObjectRecordMapper;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.domain.User;
import com.svnlan.user.service.impl.OverviewServiceImpl;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class UserDaoImpl implements UserDao {
    @Autowired
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;

    @Resource
    private JSONObjectRecordMapper jsonObjectRecordMapper;

    @Override
    public int insert(User user) {
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(USERS)
        .set(USERS.NAME,user.getName())
        .set(USERS.ROLE_ID,user.getRoleID())
        .set(USERS.EMAIL,user.getEmail() == null ? "" : user.getEmail())
        .set(USERS.PHONE,user.getPhone() == null ? "" : user.getPhone())
        .set(USERS.NICKNAME,user.getNickname() == null ? "" : user.getNickname())
        .set(USERS.AVATAR,user.getAvatar() == null ? "" : user.getAvatar())
        .set(USERS.SEX,user.getSex())
        .set(USERS.PASSWORD,user.getPassword())
        .set(USERS.SIZE_MAX,user.getSizeMax())
        .set(USERS.SIZE_USE,0L)
        .set(USERS.STATUS,user.getStatus() == null ? 1 : user.getStatus())
        .set(USERS.LAST_LOGIN,now)
        .set(USERS.MODIFY_TIME,now)
        .set(USERS.CREATE_TIME,now)
        .set(USERS.DING_OPEN_ID,user.getDingOpenId() == null ? "" : user.getDingOpenId())
        .set(USERS.DING_UNION_ID,user.getDingUnionId() == null ? null : user.getDingUnionId())
        .set(USERS.WECHAT_OPEN_ID,user.getWechatOpenId() == null ? "" : user.getWechatOpenId())
        .set(USERS.EN_WECHAT_OPEN_ID,user.getEnWechatOpenId() == null ? "" : user.getEnWechatOpenId())
        .set(USERS.ALIPAY_OPEN_ID,user.getAlipayOpenId() == null ? "" : user.getAlipayOpenId())
        .set(USERS.THIRD_OPEN_ID,user.getThirdOpenId() == null ? "" : user.getThirdOpenId())
        .set(USERS.DING_USER_ID,user.getDingUserId() == null ? null : user.getDingUserId())
        .set(USERS.TENANT_ID,tenantUtil.getTenantIdByServerName())
                .returning(USERS.ID).fetchOne().getId();
        user.setUserID(id);
        return 1;
    }

    @Override
    public int update(User user) {
        UpdateSetMoreStep<UsersRecord> set = context.update(USERS)
                .set(USERS.MODIFY_TIME, LocalDateTime.now());
        if (user.getName() != null) {
            set.set(USERS.NAME, user.getName());
        }
        if (user.getRoleID() != null) {
            set.set(USERS.ROLE_ID, user.getRoleID());
        }
        if (user.getSizeMax() != null) {
            set.set(USERS.SIZE_MAX, user.getSizeMax());
        }
        if (user.getEmail() != null) {
            set.set(USERS.EMAIL, user.getEmail());
        }
        if (user.getAvatar() != null) {
            set.set(USERS.AVATAR, user.getAvatar());
        }
        if (user.getPhone() != null) {
            set.set(USERS.PHONE, user.getPhone());
        }
        if (user.getNickname() != null) {
            set.set(USERS.NICKNAME, user.getNickname());
        }
        if (user.getSex() != null) {
            set.set(USERS.SEX, user.getSex());
        }
        if (user.getDingOpenId() != null) {
            set.set(USERS.DING_OPEN_ID, user.getDingOpenId());
        }
        if (user.getEnWechatOpenId() != null) {
            set.set(USERS.EN_WECHAT_OPEN_ID, user.getEnWechatOpenId());
        }
        if (user.getWechatOpenId() != null) {
            set.set(USERS.WECHAT_OPEN_ID, user.getWechatOpenId());
        }
        if (user.getAlipayOpenId() != null) {
            set.set(USERS.ALIPAY_OPEN_ID, user.getAlipayOpenId());
        }
        if (user.getPassword() != null) {
            set.set(USERS.PASSWORD, user.getPassword());
        }
        if (user.getDingUnionId() != null) {
            set.set(USERS.DING_UNION_ID, user.getDingUnionId());
        }
        return set.where(USERS.ID.eq(user.getUserID())).execute();
    }

    @Override
    public UserVo getUserInfo(Long userID) {
        return context.select(USERS.ID.as("userID"), USERS.NAME, USERS.NICKNAME, USERS.LAST_LOGIN.as("lastLogin"), USERS.PHONE,
                        USERS.EMAIL, USERS.STATUS, USERS.SEX, USERS.AVATAR, USERS.ROLE_ID.as("roleID"), USERS.DING_OPEN_ID.as("dingOpenId"),
                        USERS.WECHAT_OPEN_ID.as("wechatOpenId"), USERS.ALIPAY_OPEN_ID.as("alipayOpenId"), USERS.EN_WECHAT_OPEN_ID.as("enWechatOpenId"), USERS.PASSWORD, USERS.SIZE_MAX.as("sizeMax")
                        , USERS.SIZE_USE.as("sizeUse"),
                        ROLE.ROLE_NAME.as("roleName"), ROLE.CODE, ROLE.DESCRIPTION, ROLE.LABEL, ROLE.AUTH, USERS.IS_SYSTEM.as("isSystem"), ROLE.ADMINISTRATOR,
                        ROLE.IGNORE_FILE_SIZE.as("ignoreFileSize")).from(USERS)
                .leftJoin(ROLE).on(USERS.ROLE_ID.eq(ROLE.ID).and(ROLE.STATUS.eq(1)).and(ROLE.ENABLE.eq(1)))
                .where(USERS.ID.eq(userID).and(USERS.STATUS.eq(1))).fetchOneInto(UserVo.class);
    }

    @Override
    public List<UserVo> getUserListByParam(Map<String, Object> paramMap) {
        SelectJoinStep<Record19<Long, String, String, LocalDateTime, String, String, Integer, Integer, String, Integer, String, String, String, String, Double, Long, String, String, Integer>> from =
                context.selectDistinct(USERS.ID.as("userID"), USERS.NAME, USERS.NICKNAME, USERS.LAST_LOGIN, USERS.PHONE, USERS.EMAIL, USERS.STATUS
                                , USERS.SEX, USERS.AVATAR, USERS.ROLE_ID.as("roleID"), USERS.DING_OPEN_ID.as("dingOpenId"), USERS.WECHAT_OPEN_ID.as("wechatOpenId"), USERS.ALIPAY_OPEN_ID.as("alipayOpenId")
                                , USERS.EN_WECHAT_OPEN_ID.as("enWechatOpenId"), USERS.SIZE_MAX.as("sizeMax")
                                , USERS.SIZE_USE.as("sizeUse"), ROLE.ROLE_NAME.as("roleName"), ROLE.LABEL, USERS.IS_SYSTEM.as("isSystem"))
                        .from(USERS);
        // left join role r on u.roleID = r.roleID and r.status = 1 and r.enable = 1
        from.leftJoin(ROLE).on(USERS.ROLE_ID.eq(ROLE.ID).and(ROLE.STATUS.eq(1)).and(ROLE.ENABLE.eq(1)));
        long groupId = 0;
        if (paramMap.containsKey("groupID") && !ObjectUtils.isEmpty(paramMap.get("groupID"))) {
            groupId = Long.valueOf(paramMap.get("groupID").toString());
            from.leftJoin(USER_GROUP).on(USER_GROUP.USER_ID.eq(USERS.ID));
        }
        String keyword = null;
        if (paramMap.containsKey("keyword") && !ObjectUtils.isEmpty(paramMap.get("keyword"))) {
            keyword = paramMap.get("keyword").toString();
            from.leftJoin(USER_META).on(USER_META.USER_ID.eq(USERS.ID)).and(USER_META.KEY_STRING.in("namePinyin", "namePinyinSimple"));
        }
        List<String> groupLevelList = null;
        if (paramMap.containsKey("groupLevelList") && !ObjectUtils.isEmpty(paramMap.get("groupLevelList"))) {
            groupLevelList = (List<String>) paramMap.get("groupLevelList");
            from.leftJoin(USER_GROUP).on(USER_GROUP.USER_ID.eq(USERS.ID));
            from.leftJoin(GROUPS).on(USER_GROUP.GROUP_ID.eq(GROUPS.ID));
        }

        SelectConditionStep<Record19<Long, String, String, LocalDateTime, String, String, Integer, Integer, String, Integer, String, String, String, String, Double, Long, String, String, Integer>> where = null;
        if (paramMap.containsKey("status") && !ObjectUtils.isEmpty(paramMap.get("status"))) {
            where = from.where(USERS.STATUS.eq(Integer.valueOf(paramMap.get("status").toString())));
        } else {
            where = from.where(USERS.STATUS.in(0, 1));
        }
        where.and(USERS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));
        if (groupId > 0) {
            where.and(USER_GROUP.GROUP_ID.eq(groupId));
        }
        if (keyword != null) {
            where.and(USERS.NAME.like(DSL.concat(keyword, "%")).or(USERS.NICKNAME.like(DSL.concat(keyword, "%")))
                    .or(USERS.PHONE.like(DSL.concat(keyword, "%"))).or(USERS.EMAIL.like(DSL.concat(keyword, "%")))
                    .or(USER_META.VALUE_TEXT.like(DSL.concat("%", keyword, "%"))));
        }
        if (groupLevelList != null) {
            where.and(buildGroupParentLevelCondition(groupLevelList));
        }
        from.orderBy(DSL.field((String) paramMap.get("sortField") + " " + (String) paramMap.get("sortType")), USERS.ID.asc());

        Integer startIndex = (Integer) paramMap.get("startIndex");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            from.limit(startIndex,pageSize);
        }
        return from.fetchInto(UserVo.class);
    }

    @Override
    public Long getUserListByParamCount(Map<String, Object> paramMap) {
        SelectJoinStep<Record1<Integer>> from =
                context.select(DSL.countDistinct(USERS.ID))
                        .from(USERS);
        long groupId = 0;
        if (paramMap.containsKey("groupID") && !ObjectUtils.isEmpty(paramMap.get("groupID"))) {
            groupId = Long.valueOf(paramMap.get("groupID").toString());
            from.leftJoin(USER_GROUP).on(USER_GROUP.USER_ID.eq(USERS.ID));
        }
        String keyword = null;
        if (paramMap.containsKey("keyword") && !ObjectUtils.isEmpty(paramMap.get("keyword"))) {
            keyword = paramMap.get("keyword").toString();
            from.leftJoin(USER_META).on(USER_META.USER_ID.eq(USERS.ID)).and(USER_META.KEY_STRING.in("namePinyin", "namePinyinSimple"));
        }
        List<String> groupLevelList = null;
        if (paramMap.containsKey("groupLevelList") && !ObjectUtils.isEmpty(paramMap.get("groupLevelList"))) {
            groupLevelList = (List<String>) paramMap.get("groupLevelList");
            from.leftJoin(USER_GROUP).on(USER_GROUP.USER_ID.eq(USERS.ID));
            from.leftJoin(GROUPS).on(USER_GROUP.GROUP_ID.eq(GROUPS.ID));
        }

        SelectConditionStep<Record1<Integer>> where = null;
        if (paramMap.containsKey("status") && !ObjectUtils.isEmpty(paramMap.get("status"))) {
            where = from.where(USERS.STATUS.eq(Integer.valueOf(paramMap.get("status").toString())));
        } else {
            where = from.where(USERS.STATUS.in(0, 1));
        }
        where.and(USERS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));
        if (groupId > 0) {
            where.and(USER_GROUP.GROUP_ID.eq(groupId));
        }
        if (keyword != null) {
            where.and(USERS.NAME.like(DSL.concat(keyword, "%")).or(USERS.NICKNAME.like(DSL.concat(keyword, "%")))
                    .or(USERS.PHONE.like(DSL.concat(keyword, "%"))).or(USERS.EMAIL.like(DSL.concat(keyword, "%")))
                    .or(USER_META.VALUE_TEXT.like(DSL.concat("%", keyword, "%"))));
        }
        if (groupLevelList != null) {
            where.and(buildGroupParentLevelCondition(groupLevelList));
        }

        return (Long) from.fetchOneInto(Long.class);
    }

    public Condition buildGroupParentLevelCondition(List<String> list) {
        Condition condition = DSL.falseCondition();
        for (String keyword : list) {
            condition = condition.or(GROUPS.PARENT_LEVEL.like(keyword + "%"));
        }
        return condition;
    }

    @Override
    public int updateUserState(List<Long> list, Integer status) {
        UpdateSetMoreStep<UsersRecord> set = context.update(USERS)
                .set(USERS.STATUS, status).set(USERS.MODIFY_TIME, LocalDateTime.now());
        return set.where(USERS.ID.in(list)).execute();
    }

    @Override
    public int updateNickname(String nickname, Long userID) {
        UpdateSetMoreStep<UsersRecord> set = context.update(USERS)
                .set(USERS.NICKNAME, nickname).set(USERS.MODIFY_TIME, LocalDateTime.now());
        return set.where(USERS.ID.eq(userID)).execute();
    }

    @Override
    public int updateSex(String sex, Long userID) {
        UpdateSetMoreStep<UsersRecord> set = context.update(USERS)
                .set(USERS.SEX, Integer.valueOf(sex)).set(USERS.MODIFY_TIME, LocalDateTime.now());
        return set.where(USERS.ID.eq(userID)).execute();
    }

    @Override
    public int updateAvatar(String avatar, Long userID) {
        UpdateSetMoreStep<UsersRecord> set = context.update(USERS)
                .set(USERS.AVATAR, avatar).set(USERS.MODIFY_TIME, LocalDateTime.now());
        return set.where(USERS.ID.eq(userID)).execute();
    }

    @Override
    public int updateAvatarList(List<Long> list, String avatar) {
        UpdateSetMoreStep<UsersRecord> set = context.update(USERS)
                .set(USERS.AVATAR, avatar).set(USERS.MODIFY_TIME, LocalDateTime.now());
        return set.where(USERS.ID.in(list)).execute();
    }

    @Override
    public int updateEmail(String email, Long userID) {
        UpdateSetMoreStep<UsersRecord> set = context.update(USERS)
                .set(USERS.EMAIL, email).set(USERS.MODIFY_TIME, LocalDateTime.now());
        return set.where(USERS.ID.eq(userID)).execute();
    }

    @Override
    public int updatePassword(String password, Long userID) {
        UpdateSetMoreStep<UsersRecord> set = context.update(USERS)
                .set(USERS.PASSWORD, password).set(USERS.MODIFY_TIME, LocalDateTime.now());
        return set.where(USERS.ID.eq(userID)).execute();
    }

    @Override
    public int findByEmail(String email) {
        return context.selectCount().from(USERS)
                .where(
                        USERS.EMAIL.eq(email)
                                .and(USERS.STATUS.in(Arrays.asList(0, 1)))
                                .and(USERS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                ).fetchOneInto(Integer.class);
    }

    @Override
    public int findByNickname(String nickname) {
        return context.selectCount().from(USERS)
                .where(
                        USERS.NICKNAME.eq(nickname)
                                .and(USERS.STATUS.in(Arrays.asList(0, 1)))
                                .and(USERS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                ).fetchOneInto(Integer.class);
    }

    @Override
    public UserVo getUserSimpleInfo(Long userID) {
        return context.select(USERS.ID.as("userID"), USERS.NAME, USERS.NICKNAME, USERS.LAST_LOGIN.as("lastLogin"), USERS.PHONE,
                        USERS.EMAIL, USERS.STATUS, USERS.SEX, USERS.AVATAR, USERS.ROLE_ID.as("roleID"), USERS.DING_OPEN_ID.as("dingOpenId"),
                        USERS.WECHAT_OPEN_ID.as("wechatOpenId"), USERS.ALIPAY_OPEN_ID.as("alipayOpenId"), USERS.EN_WECHAT_OPEN_ID.as("enWechatOpenId"), USERS.PASSWORD).from(USERS)
                .leftJoin(ROLE).on(USERS.ROLE_ID.eq(ROLE.ID).and(ROLE.STATUS.eq(1)).and(ROLE.ENABLE.eq(1)))
                .where(USERS.ID.eq(userID).and(USERS.STATUS.eq(1))).fetchOneInto(UserVo.class);
    }

    @Override
    public List<UserVo> findByName(String name) {
        return context.select(USERS.ID.as("userID"), USERS.NAME).from(USERS)
                .where(
                        USERS.NAME.eq(name)
                                .and(USERS.STATUS.in(Arrays.asList(0, 1)))
                                .and(USERS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                ).fetchInto(UserVo.class);
    }

    @Override
    public List<UserVo> getUserBaseInfo(List<Long> list) {
        return context.select(USERS.ID.as("userID"), USERS.NAME, USERS.NICKNAME, USERS.STATUS, USERS.SEX, USERS.AVATAR)
                .from(USERS).where(USERS.ID.in(list)).fetchInto(UserVo.class);
    }

    @Override
    public UserVo getUserBaseOneInfo(Long userID) {
        return context.select(USERS.ID.as("userID"), USERS.NAME, USERS.NICKNAME, USERS.STATUS, USERS.SEX, USERS.AVATAR, USERS.SIZE_MAX, USERS.SIZE_USE)
                .from(USERS).where(USERS.ID.eq(userID)).fetchOneInto(UserVo.class);
    }

    @Override
    public Long sumUserSpaceUsed() {
        return context.select(DSL.sum(USERS.SIZE_USE))
                .from(USERS)
                .where(USERS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(Long.class);
    }

    @Override
    public Long selectUserCount() {
        return context.selectCount()
                .from(USERS)
                .where(
                        USERS.STATUS.eq(1)
                                .and(USERS.IS_SYSTEM.eq(0))
                                .and(USERS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                )
                .fetchOneInto(Long.class);
    }

    @Override
    public List<UserVo> findByPhone(String phone) {
        return context.select(USERS.ID.as("userID"), USERS.NAME).from(USERS)
                .where(
                        USERS.PHONE.eq(phone)
                                .and(USERS.STATUS.in(Arrays.asList(0, 1)))
                                .and(USERS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                )
                .fetchInto(UserVo.class);
    }

    @Override
    public int updatePhone(String phone, Long userID) {
        UpdateSetMoreStep<UsersRecord> set = context.update(USERS)
                .set(USERS.PHONE, phone).set(USERS.MODIFY_TIME, LocalDateTime.now());
        return set.where(USERS.ID.eq(userID)).execute();
    }

    @Override
    public String getPasswordByUserName(String username) {
        return context.select(USERS.PASSWORD)
                .from(USERS)
                .where(
                        USERS.NAME.eq(username).and(USERS.STATUS.notEqual(2))
                                .and(USERS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                )
                .fetchOneInto(String.class);
    }

    @Override
    public UserVo getUserByUserName(String username) {
        return context.select(USERS.ID.as("userID"), USERS.NAME, USERS.PASSWORD, USERS.STATUS, USERS.SEX, USERS.TENANT_ID)
                .from(USERS)
                .where(USERS.NAME.eq(username))
                .and(USERS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(UserVo.class);
    }

    @Override
    public List<UserVo> queryUserInfoByOpenIdOrMobile(String openId, String mobile, String code, String unionId, String dingUserId) {
        Condition condition = DSL.falseCondition();
        if ("8".equals(code)) {
            if (!ObjectUtils.isEmpty(openId)){
                condition = condition.or(USERS.DING_OPEN_ID.eq(openId));
            }
            if (!ObjectUtils.isEmpty(unionId)){
                condition = condition.or(USERS.DING_UNION_ID.eq(unionId));
            }
            if (!ObjectUtils.isEmpty(dingUserId)){
                condition = condition.or(USERS.DING_USER_ID.eq(dingUserId));
            }
        } else if ("2".equals(code)) {
            condition = condition.or(USERS.WECHAT_OPEN_ID.eq(openId));
        } else if ("6".equals(code)) {
            condition = condition.or(USERS.ALIPAY_OPEN_ID.eq(openId));
        } else if ("7".equals(code)) {
            condition = condition.or(USERS.EN_WECHAT_OPEN_ID.eq(openId));
        } else if ("13".equals(code)) {
            condition = condition.or(USERS.THIRD_OPEN_ID.eq(openId));
            if (!ObjectUtils.isEmpty(dingUserId)){
                condition = condition.or(USERS.DING_USER_ID.eq(dingUserId));
            }
        }
        condition = condition.or(USERS.PHONE.eq(mobile)).or(USERS.NAME.eq(mobile));
        return context.select(USERS.ID.as("userID"), USERS.AVATAR, USERS.PHONE, USERS.EMAIL, USERS.NICKNAME, USERS.DING_OPEN_ID, USERS.DING_UNION_ID,
                        USERS.WECHAT_OPEN_ID.as("wechatOpenId"), USERS.ALIPAY_OPEN_ID.as("alipayOpenId"), USERS.EN_WECHAT_OPEN_ID.as("enWechatOpenId")
                ,USERS.STATUS, USERS.DING_UNION_ID, USERS.THIRD_OPEN_ID, USERS.DING_USER_ID)
                .from(USERS)
                .where(
                        USERS.STATUS.notEqual(2)
                                .and(USERS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                                .and(condition)
                )
                .fetchInto(UserVo.class);
    }

    @Override
    public UserVo queryUserByUserId(Long userId) {
        return context.select(USERS.ID.as("userID"), USERS.WECHAT_OPEN_ID.as("wechatOpenId"), USERS.EN_WECHAT_OPEN_ID.as("enWechatOpenId"), USERS.AVATAR, USERS.NICKNAME, USERS.PHONE, USERS.EMAIL, USERS.STATUS).from(USERS).where(USERS.ID.eq(userId)).fetchOneInto(UserVo.class);
    }

    @Override
    public int removeOpenId(Long userID, String value) {
        return context.update(USERS)
                .setNull(DSL.val(value + "OpenId")).where(USERS.ID.eq(userID).and(DSL.val(value + "OpenId").isNotNull())).execute();
    }

    @Override
    public List<UserVo> getUserSimpleInfoByIds(Collection<Long> userIds) {
        return context.select(USERS.ID.as("userID"), USERS.AVATAR, DSL.iif(USERS.NICKNAME.isNull().or(USERS.NICKNAME.eq("")), USERS.NAME, USERS.NICKNAME).as("name")).from(USERS)
                .where(USERS.STATUS.eq(1).and(USERS.ID.in(userIds))).fetchInto(UserVo.class);
    }

    @Override
    public Long getCreateTime(Long userId) {
        return context.select(USERS.CREATE_TIME).from(USERS).where(USERS.ID.eq(userId)).fetchOneInto(Long.class);

    }

    @Override
    public List<Long> getUserIdByRoleId(List<Long> roleIdList) {
        return context.select(USERS.ID.as("userID")).from(USERS)
                .where(USERS.ROLE_ID.in(roleIdList)).fetchInto(Long.class);
    }

    @Override
    public List<JSONObject> getUserSpaceRank(Integer topCount, Long tenantId) {
        return context.select(USERS.ID.as("uid"), DSL.iif(USERS.NICKNAME.isNull().or(USERS.NICKNAME.eq("")), USERS.NAME, USERS.NICKNAME).as("n")
                        , USERS.AVATAR.as("a"), USERS.SIZE_MAX.as("sm"), USERS.SIZE_USE.as("s")).from(USERS)
                .where(USERS.TENANT_ID.eq(tenantId))
                .orderBy(USERS.SIZE_USE.desc()).limit(topCount).fetch(jsonObjectRecordMapper);
    }

    @Override
    public User selectAvatarAndNickName(Long userId) {
        return context.select(USERS.AVATAR, DSL.iif(USERS.NICKNAME.isNull().or(USERS.NICKNAME.eq("")), USERS.NAME, USERS.NICKNAME).as("nickname"))
                .from(USERS)
                .where(USERS.ID.eq(userId))
                .fetchOneInto(User.class);
    }

    @Override
    public void setUserDingUnionid(UserVo userVo){
        UpdateSetMoreStep<UsersRecord> set = context.update(USERS)
                .set(USERS.DING_UNION_ID, userVo.getDingUnionId());
        if (!ObjectUtils.isEmpty(userVo.getDingOpenId())){
            set.set(USERS.DING_OPEN_ID, userVo.getDingOpenId());
        }
        if (!ObjectUtils.isEmpty(userVo.getDingUserId())){
            set.set(USERS.DING_USER_ID, userVo.getDingUserId());
        }
        if (!ObjectUtils.isEmpty(userVo.getPhone())){
            set.set(USERS.PHONE, userVo.getPhone());
        }
         set.where(USERS.ID.eq(userVo.getUserID())).execute();
    }

    @Override
    public Integer getAllUserCount(Long tenantId){
        tenantId = ObjectUtils.isEmpty(tenantId) ? tenantUtil.getTenantIdByServerName() : tenantId;
        return (Integer)context.selectCount().from(USERS).where(USERS.TENANT_ID.eq(tenantId).and(USERS.STATUS.in(0,1))).fetchOneInto(Integer.class);
    }

    @Override
    public List<String> getAllUserByDingUnionId(Long tenantId){
        return context.select(USERS.DING_UNION_ID).from(USERS)
                .where(USERS.TENANT_ID.eq(tenantId).and(USERS.DING_UNION_ID.isNotNull())).fetchInto(String.class);
    }
    @Override
    public List<String> getAllUserByEnWebChat(Long tenantId){
        return context.select(USERS.EN_WECHAT_OPEN_ID).from(USERS)
                .where(USERS.TENANT_ID.eq(tenantId).and(USERS.EN_WECHAT_OPEN_ID.isNotNull())).fetchInto(String.class);
    }
    @Override
    public List<String> getAllUserByCube(Long tenantId){
        return context.select(USERS.THIRD_OPEN_ID).from(USERS)
                .where(USERS.TENANT_ID.eq(tenantId).and(USERS.THIRD_OPEN_ID.isNotNull())).fetchInto(String.class);
    }
    @Override
    public Long getUserIdByPhone(String phone, Long tenantId){
        return context.select(USERS.ID).from(USERS)
                .where(USERS.PHONE.eq(phone).and(USERS.TENANT_ID.eq(tenantId))).limit(1).fetchOneInto(Long.class);
    }
    @Override
    public Long getUserIdByImUserId(String imUserId, Long tenantId){
        return context.select(USERS.ID).from(USERS)
                .where(USERS.DING_USER_ID.eq(imUserId).and(USERS.TENANT_ID.eq(tenantId))).limit(1).fetchOneInto(Long.class);
    }

    @Override
    public void setImUserId(Long userId, String imUserId){
         context.update(USERS)
                        .set(USERS.DING_USER_ID, imUserId).where(USERS.ID.eq(userId)).execute();
    }

    @Override
    public void setUserUnionidByType(Long userId, String openId, String unionId, String code){

        UpdateSetMoreStep<UsersRecord> set = context.update(USERS)
                .set(USERS.MODIFY_TIME, LocalDateTime.now());
        if (!ObjectUtils.isEmpty(openId)){
            set.set(USERS.DING_OPEN_ID, openId);
        }
        if ("8".equals(code)) {
            if (!ObjectUtils.isEmpty(openId)){
                set.set(USERS.DING_OPEN_ID, openId);
            }
            if (!ObjectUtils.isEmpty(unionId)){
                set.set(USERS.DING_UNION_ID, unionId);
            }
        } else if ("7".equals(code)) {
            if (!ObjectUtils.isEmpty(openId)){
                set.set(USERS.EN_WECHAT_OPEN_ID, openId);
            }
        } else if ("13".equals(code)) {
            if (!ObjectUtils.isEmpty(openId)){
                set.set(USERS.THIRD_OPEN_ID, openId);
            }
            if (!ObjectUtils.isEmpty(unionId)){
                set.set(USERS.DING_UNION_ID, unionId);
            }
        }
        set.where(USERS.ID.eq(userId)).execute();
    }

    @Override
    public Long getUserIdByTypeId(String code, String openId, String unionId, Long tenantId){

        Condition condition = DSL.falseCondition();
        if ("8".equals(code)) {
            if (!ObjectUtils.isEmpty(openId)){
                condition = condition.or(USERS.DING_OPEN_ID.eq(openId));
            }else if (!ObjectUtils.isEmpty(unionId)){
                condition = condition.or(USERS.DING_UNION_ID.eq(unionId));
            }
        } else if ("2".equals(code)) {
            condition = condition.or(USERS.WECHAT_OPEN_ID.eq(openId));
        } else if ("6".equals(code)) {
            condition = condition.or(USERS.ALIPAY_OPEN_ID.eq(openId));
        } else if ("7".equals(code)) {
            condition = condition.or(USERS.EN_WECHAT_OPEN_ID.eq(openId));
        } else if ("13".equals(code)) {
            condition = condition.or(USERS.THIRD_OPEN_ID.eq(openId));
        }

        return context.select(USERS.ID).from(USERS)
                .where(USERS.TENANT_ID.eq(tenantId).and(condition)).fetchOneInto(Long.class);



    }
}
