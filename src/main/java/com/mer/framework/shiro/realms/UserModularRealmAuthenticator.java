package com.mer.framework.shiro.realms;

import com.mer.framework.shiro.token.CustomizedToken;
import com.mer.framework.shiro.token.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zhaoqi
 * @date 2020/5/20 17:20
 * <p>
 * 当配置了多个Realm时，我们通常使用的认证器是shiro自带的
 * org.apache.shiro.authc.pam.ModularRealmAuthenticator，
 * 其中决定使用的Realm的是doAuthenticate()方法
 */
@Slf4j
public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {


    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        assertRealmsConfigured();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        Collection<Realm> typeRealms = new ArrayList<>();
        // 强制转换回自定义的Token
        try {
            JwtToken jwtToken = (JwtToken) authenticationToken;
            for (Realm realm : realms) {
                if (realm.getName().contains("Jwt")) {
                    typeRealms.add(realm);
                }
            }
            return doSingleRealmAuthentication(typeRealms.iterator().next(), jwtToken);
        } catch (ClassCastException e) {
            typeRealms.clear();
            CustomizedToken customizedToken = (CustomizedToken) authenticationToken;
            // 登录类型
            String loginType = customizedToken.getLoginType();
            for (Realm realm : realms) {
                if (realm.getName().contains(loginType)) {
                    typeRealms.add(realm);
                }
            }
            // 判断是单Realm还是多Realm
            if (typeRealms.size() == 1) {
                return doSingleRealmAuthentication(typeRealms.iterator().next(), customizedToken);
            } else {
                return doMultiRealmAuthentication(typeRealms, customizedToken);
            }
        }

    }
}
