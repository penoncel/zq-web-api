package com.mer.common.redis.val;

import com.mer.project.pojo.SysUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
@Data
@NoArgsConstructor
public class LoginUser {

    /**
     * 角色列表
     */
    private Set<String> roleSet;

    /**
     * 权限列表
     */
    private Set<String> permissionsSet;

    /**
     * User
     */
    private SysUser user;
}
