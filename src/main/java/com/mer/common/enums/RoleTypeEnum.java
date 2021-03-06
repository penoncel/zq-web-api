package com.mer.common.enums;

/**
 * @Description: 角色类型
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
public enum RoleTypeEnum {
    /**
     * 管理员
     */
    ADMIN(1, "admin", "管理员"),
    /**
     * 普通角色
     */
    COMMON(2, "common", "普通角色");

    private Integer code;

    private String name;

    private String msg;

    RoleTypeEnum(Integer code, String name, String msg) {
        this.code = code;
        this.name = name;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "RoleEnums{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
