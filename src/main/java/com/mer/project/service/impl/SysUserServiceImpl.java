package com.mer.project.service.impl;

import com.mer.common.enums.LoginTypeEnum;
import com.mer.common.enums.RoleTypeEnum;
import com.mer.common.enums.SysMsgEnum;
import com.mer.common.redis.key.AppLoginKey;
import com.mer.common.redis.val.LoginUser;
import com.mer.common.utils.ComUtils;
import com.mer.common.utils.JwtUtil;
import com.mer.framework.config.redis.redisservice.RedisService;
import com.mer.framework.shiro.service.TokenService;
import com.mer.framework.shiro.token.CustomizedToken;
import com.mer.framework.web.domain.Result;
import com.mer.project.dao.SysUserDao;
import com.mer.project.pojo.SysUser;
import com.mer.project.service.SysPermissionsServer;
import com.mer.project.service.SysUserService;
import com.mer.project.vo.resp.LoginTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Program: zq-web-api
 * @Description: 用户Service实现类
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserDao userDao;

    @Autowired
    RedisService redisService;

    @Autowired
    SysPermissionsServer permissionsServer;

    @Autowired
    TokenService tokenService;

    /**
     * 手机号，密码  登入
     *
     * @param phone    手机号
     * @param password 密码
     * @return
     */
    @Override
    public Result loginByPassword(String phone, String password) {
        try {
            // 封装用户数据
            CustomizedToken token = new CustomizedToken(phone, password, LoginTypeEnum.PASSWORD_LOGIN_TYPE.toString());
            // 执行登录方法
            SecurityUtils.getSubject().login(token);
            // 返回登录后初始化参数
            return Result.success(returnLoginInitParam(phone));
        } catch (UnknownAccountException e) {
            return Result.error(SysMsgEnum.USERNAME_NOT_EXIST);
        } catch (IncorrectCredentialsException e) {
            return Result.error(SysMsgEnum.PASSWORD_ERROR);
        }
    }

    /**
     * 手机号，验证码  快捷登入
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    @Override
    public Result loginByCode(String phone, String code) {
        try {
            // 封装用户数据
            CustomizedToken token = new CustomizedToken(phone, code, LoginTypeEnum.CODE_LOGIN_TYPE.toString());
            // 执行登录方法
            SecurityUtils.getSubject().login(token);
            // 返回登录后初始化参数
            return Result.success(returnLoginInitParam(phone));
        } catch (UnknownAccountException e) {
            return Result.error(SysMsgEnum.USERNAME_NOT_EXIST);
        } catch (ExpiredCredentialsException e) {
            return Result.error(SysMsgEnum.CODE_EXPIRE);
        } catch (IncorrectCredentialsException e) {
            return Result.error(SysMsgEnum.CODE_ERROR);
        }

    }


    /**
     * 通过短信验证码进行 修改密码
     *
     * @param phone    手机号
     * @param code     验证码
     * @param password 密码
     * @return
     */
    @Override
    public Result modifyPassword(String phone, String code, String password) {
        // 判断redis中是否存在验证码
        String modifyCode = redisService.getString(AppLoginKey.modifyPassWordCode, phone);
        if (Objects.isNull(modifyCode)) {
            return Result.error(SysMsgEnum.CODE_EXPIRE);
        }

        // 判断redis中code与传递过来的code 是否相等
        if (!Objects.equals(ComUtils.encryptPassword(code), modifyCode)) {
            return Result.error(SysMsgEnum.CODE_ERROR);
        }

        // 如果用户不存在，执行注册
        SysUser user = this.findByUserPhone(phone);
        if (Objects.isNull(user)) {
            try {
                Boolean flag = this.registerLoginUser(phone, password);
                if (flag) {
                    return Result.success(this.returnLoginInitParam(phone));
                } else {
                    return Result.error();
                }
            } catch (Exception e) {
                log.error(ComUtils.writerEx(e));
                redisService.delete(AppLoginKey.loginUserIinfo, phone);
                throw e;
            }
        }

        user.setPhone(phone);
        user.setSalt(ComUtils.uuid());
        user.setPassword(ComUtils.encryptPassword(password));

        // 删除缓存用户信息
        redisService.delete(AppLoginKey.loginUserIinfo, phone);
        boolean flag = userDao.updatePassWorld(user);
        if (flag) {
            // 修改完后重新设置用户缓存信息
            redisService.set(AppLoginKey.loginUserIinfo, phone, user);
            return Result.success(this.returnLoginInitParam(phone));
        } else {
            return Result.error();
        }

    }


    /**
     * 用户注册
     *
     * @param phone 手机号
     * @param args  密码
     * @return
     */
    @Override
    public Boolean registerLoginUser(String phone, String... args) {
        SysUser user = new SysUser();
        user.setPhone(phone);
        // 如果有密码，则使用用户输入的密码
        String encryptPassword;
        if (args.length > 0) {
            encryptPassword = ComUtils.encryptPassword(args[0]);
        } else {
            // 默认手机号后6未作为密码
            encryptPassword = ComUtils.encryptPassword(phone.substring(5, 11));
        }
        user.setSalt(ComUtils.uuid());
        user.setPassword(encryptPassword);
        user.setIcon("/test.jpg");
        user.setName(null);
        user.setAddress(null);

        // 用户注册
        userDao.insertLoginUserMsg(user);
        // 权限配置
        permissionsServer.addRole(user.getId(), RoleTypeEnum.ADMIN.getCode(), RoleTypeEnum.COMMON.getCode());
        //缓存LoginUser对象
        tokenService.getLoginUser(phone);
        return Boolean.TRUE;

    }


    /**
     * 从redis 中获取 信息 返回登录后初始化参数
     *
     * @param phone phone
     * @return Map<String, Object>
     */
    private Map<String, Object> returnLoginInitParam(String phone) {
        // 按钮权限，角色权限，角色信息
        LoginUser loginUser = tokenService.getLoginUser(phone);
        // 登入成功时，刷新旧的token。
        if (redisService.exists(AppLoginKey.userToken, phone)) {
            redisService.delete(AppLoginKey.userToken, phone);
        }
        //生成新的token信息
        LoginTokenVo loginTokenVo = JwtUtil.createToken(phone);
        // 设置登入返回的模型
        Map<String, Object> data = new HashMap<>(2);
        data.put("userInfo", userToMap(loginUser));
        data.put("userToken", loginTokenVo);
        redisService.set(AppLoginKey.userToken, phone, loginTokenVo);
        return data;
    }

    @Override
    public SysUser findByUserPhone(String phone) {
        return userDao.findByUserPhone(phone);
    }


    /**
     * 封装 回传的 用户信息
     *
     * @param loginUser 用户对象
     * @return
     */
    public Map userToMap(LoginUser loginUser) {
        if (loginUser != null) {
            Map map = new HashMap(6);
            map.put("phone", loginUser.getUser().getPhone());
            map.put("name", loginUser.getUser().getName());
            map.put("age", loginUser.getUser().getAge());
            map.put("sex", loginUser.getUser().getSex());
            map.put("icon", loginUser.getUser().getIcon());
            map.put("address", loginUser.getUser().getAddress());
            return map;
        }
        return null;
    }


}
