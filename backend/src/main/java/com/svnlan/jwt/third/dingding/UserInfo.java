package com.svnlan.jwt.third.dingding;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 钉钉用户详情
 *
 * @author lingxu 2023/03/29 13:30
 */
@NoArgsConstructor
@Data
public class UserInfo {
    /**
     * 是否激活了钉钉
     */
    @JSONField(name = "active")
    private Boolean active;
    /**
     * 是否为企业的管理员
     */
    @JSONField(name = "admin")
    private Boolean admin;
    /**
     * 头像
     */
    @JSONField(name = "avatar")
    private String avatar;
    /**
     * 是否为企业的老板
     */
    @JSONField(name = "boss")
    private Boolean boss;
    /**
     * 所属部门id列表
     */
    @JSONField(name = "dept_id_list")
    private List<Long> deptIdList;
    /**
     * 员工在对应的部门中的排序
     */
    @JSONField(name = "dept_order_list")
    private List<DeptOrderListDTO> deptOrderList;
    /**
     * 邮箱
     */
    @JSONField(name = "email")
    private String email;
    /**
     * 是否为专属帐号
     */
    @JSONField(name = "exclusive_account")
    private Boolean exclusiveAccount;
    /**
     * 是否号码隐藏
     */
    @JSONField(name = "hide_mobile")
    private Boolean hideMobile;
    /**
     * 员工工号
     */
    @JSONField(name = "job_number")
    private String jobNumber;
    /**
     * 员工所在部门信息及是否是领导
     */
    @JSONField(name = "leader_in_dept")
    private List<LeaderInDeptDTO> leaderInDept;
    /**
     * 手机号
     */
    @JSONField(name = "mobile")
    private String mobile;
    /**
     * 员工姓名
     */
    @JSONField(name = "name")
    private String name;
    /**
     * 是否完成了实名认证
     */
    @JSONField(name = "real_authed")
    private Boolean realAuthed;
    /**
     * 备注
     */
    @JSONField(name = "remark")
    private String remark;
    /**
     * 角色列表
     */
    @JSONField(name = "role_list")
    private List<RoleListDTO> roleList;
    /**
     * 是否为企业的高管
     */
    @JSONField(name = "senior")
    private Boolean senior;
    /**
     * 职位
     */
    @JSONField(name = "title")
    private String title;
    /**
     * 员工在当前开发者企业账号范围内的唯一标识
     */
    @JSONField(name = "unionid")
    private String unionId;
    /**
     * 用户id
     */
    @JSONField(name = "userid")
    private String userId;
    /**
     * 工作地
     */
    @JSONField(name = "workPlace")
    private String workPlace;

    @NoArgsConstructor
    @Data
    public static class DeptOrderListDTO {
        @JSONField(name = "dept_id")
        private Integer deptId;
        @JSONField(name = "order")
        private Long order;
    }

    @NoArgsConstructor
    @Data
    public static class LeaderInDeptDTO {
        /**
         * 员工所在部门的部门ID
         */
        @JSONField(name = "dept_id")
        private Integer deptId;
        /**
         * 员工在对应的部门中是否是领导
         */
        @JSONField(name = "leader")
        private Boolean leader;
    }

    @NoArgsConstructor
    @Data
    public static class RoleListDTO {
        @JSONField(name = "group_name")
        private String groupName;
        @JSONField(name = "id")
        private Long id;
        @JSONField(name = "name")
        private String name;
    }
}
