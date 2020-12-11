package com.mer.framework.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
public class CustomizedToken extends UsernamePasswordToken {

    /**
     * 登录类型
     */
    private String loginType;

    public CustomizedToken(final String username, final String password, String loginType){
        super(username, password);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    @Override
    public String toString(){
        return "loginType="+ loginType +",username=" + super.getUsername()+",password="+ String.valueOf(super.getPassword());
    }
}
