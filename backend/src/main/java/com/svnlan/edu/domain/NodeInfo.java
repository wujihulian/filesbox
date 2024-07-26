package com.svnlan.edu.domain;

import lombok.Data;

@Data
public class NodeInfo {
    /** /标签名称 */
    private String name;
    /** 关联课时数 */
    private Long courseNum;
    /** 1单元 2标签 */
    private Integer type;
}
