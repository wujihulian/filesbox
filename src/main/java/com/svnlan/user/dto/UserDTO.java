package com.svnlan.user.dto;

import com.svnlan.enums.SecurityTypeEnum;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.utils.PageQuery;
import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/9 11:09
 */
@Data
public class UserDTO extends PageQuery {

    private Long userID;
    private Long groupID;
    private Long parentID;
    private String keyword;
    /** 排序字段 */
    private String sortField = "sizeUse";
    /** 排序方式  倒序desc  正序asc */
    private String sortType = "desc";

    private String opType;

    /** 登陆用户名*/
    private String name;
    /** 用户角色 */
    private Integer roleID;
    /** 邮箱 */
    private String email;
    /** 手机 */
    private String phone;
    /** 昵称 */
    private String nickname;
    /** 头像 */
    private String avatar;
    /** 性别 (0女1男) */
    private Integer sex;
    /** 密码 */
    private String password;
    /** 群组存储空间大小(GB) 0-不限制 */
    private Double sizeMax;
    /** 已使用大小(byte) */
    private Long sizeUse;
    /** 用户启用状态 0-未启用 1-启用 */
    private Integer status;

    private List<UserGroupVo> groupInfo;

    private String key;

    private String value;
    private String code;
    /** 创建者id */
    private Long createUser;

    private String userIDStr;

    private String openId;

    /**
     * 1手机号，2微信，3 qq，4 email，5微博，6 支付宝 7 企业微信 8 钉钉
     * @see <a href="{@link SecurityTypeEnum}">SecurityTypeEnum</a>
     */
    private Integer openIdType;

    private List<String> groupLevelList;

    private String sig;

}
