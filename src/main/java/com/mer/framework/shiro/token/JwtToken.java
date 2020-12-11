package com.mer.framework.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
