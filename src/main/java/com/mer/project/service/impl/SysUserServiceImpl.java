package com.mer.project.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.mer.common.enums.ErrorStateEnum;
import com.mer.common.enums.LoginTypeEnum;
import com.mer.common.enums.RoleTypeEnum;
import com.mer.common.rediskey.AppLoginKey;
import com.mer.common.utils.CommonsUtils;
import com.mer.common.utils.JwtUtil;
import com.mer.common.utils.WriteFrom;
import com.mer.framework.config.redis.redisservice.RedisService;
import com.mer.framework.shiro.token.CustomizedToken;
import com.mer.framework.web.domain.LoginToken;
import com.mer.framework.web.domain.LoginUser;
import com.mer.framework.web.domain.Result;
import com.mer.project.service.sms.SmsSandSendPo;
import com.mer.project.service.sms.TsmsSendService;
import com.mer.project.dao.SysUserDao;
import com.mer.project.pojo.SysUser;
import com.mer.project.service.SysPermissionsServer;
import com.mer.project.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
    ThreadPoolTaskExecutor notifyServiceThreadPool;

    @Override
    public Result modifyPassword(String phone, String code, String password) {

        // 判断redis中是否存在验证码
        String modifyCode = redisService.getString(AppLoginKey.modifyPassWordCode,phone);
        if(Objects.isNull(modifyCode)){
            return Result.error(ErrorStateEnum.CODE_EXPIRE);
        }

        // 判断redis中code与传递过来的code 是否相等
        if(!Objects.equals(CommonsUtils.encryptPassword(code), modifyCode)){
            return Result.error(ErrorStateEnum.CODE_ERROR);
        }

        // 如果用户不存在，执行注册
        SysUser user = this.findByUserPhone(phone);
        if(Objects.isNull(user)){
            try{
                Boolean flag = this.registerLoginUser(phone, password);
                if(flag){
                    return Result.success(this.returnLoginInitParam(phone));
                }else {
                    return Result.error();
                }
            }catch (Exception e){
                log.error(WriteFrom.writerEx(e));
                redisService.delete(AppLoginKey.loginUserIinfo,phone);
                throw e;
            }
        }

        user.setPhone(phone);
        user.setSalt(CommonsUtils.uuid());
        user.setPassword(CommonsUtils.encryptPassword(password));

        // 删除缓存用户信息
        redisService.delete(AppLoginKey.loginUserIinfo,phone);
        boolean flag = userDao.updatePassWorld(user);
        if(flag){
            // 修改完后重新设置用户缓存信息
            redisService.set(AppLoginKey.loginUserIinfo,phone,user);
            return Result.success(this.returnLoginInitParam(phone));
        }else {
            return Result.error();
        }

    }



    @Override
    public Boolean registerLoginUser(String phone, String... args) {
        SysUser user = new SysUser();
        user.setPhone(phone);
        // 如果有密码，则使用用户输入的密码
        String encryptPassword;
        if(args.length > 0){
            encryptPassword = CommonsUtils.encryptPassword(args[0]);
        }else{
            // 默认手机号后6未作为密码
            encryptPassword = CommonsUtils.encryptPassword(phone.substring(5, 11));
        }
        user.setSalt(CommonsUtils.uuid());
        user.setPassword(encryptPassword);
        user.setIcon("/test.jpg");
        user.setName(null);
        user.setAddress(null);

        // 用户注册
        userDao.insertLoginUserMsg(user);
        // 权限配置
        permissionsServer.addRole(user.getId(), RoleTypeEnum.ADMIN.getCode(), RoleTypeEnum.COMMON.getCode());
        //缓存LoginUser对象
        redisService.setLogingUserRedis(user);
        return Boolean.TRUE;

    }

    @Override
    public Result loginByCode(String phone, String code) {
        log.info(String.format(" 手机号 [%s] 验证码登入",phone));
        String smsCode =  redisService.getString(AppLoginKey.smsCode,phone);
        if (smsCode == null) {
            return Result.error(ErrorStateEnum.CODE_EXPIRE);
        }

        // 验证码验证
        String redisCode = CommonsUtils.encryptPassword(code);
        if(!Objects.equals(redisCode, smsCode)){
            return Result.error(ErrorStateEnum.CODE_ERROR);
        }

        try{
            // 检查redis是否存在用户信息
            if(Boolean.FALSE == redisService.exists(AppLoginKey.loginUserIinfo,phone)){
                // 数据库中是否存在
                SysUser user =this.findByUserPhone(phone);
                //数据库中也没有，就自动 注册一下 然后设置到缓存
                if(Objects.isNull(user)){
                    try{
                        this.registerLoginUser(phone);
                    }catch (Exception e){
                        log.error(WriteFrom.writerEx(e));
                        // 由于redis没有事务回滚，需要在这里进行手动删除
                        redisService.delete(AppLoginKey.loginUserIinfo,phone);
                        throw e;
                    }
                }else {
                    //数据库存在则进行缓存
                    redisService.setLogingUserRedis(user);
                }
            }
            // 获取Subject
            Subject subject = SecurityUtils.getSubject();
            // 封装用户数据
            CustomizedToken token = new CustomizedToken(phone, code, LoginTypeEnum.CODE_LOGIN_TYPE.toString());
            // 执行登录方法
            subject.login(token);
            // 返回登录后初始化参数
            return Result.success(returnLoginInitParam(phone));
        }catch (UnknownAccountException e) {
            return Result.error(ErrorStateEnum.USERNAME_NOT_EXIST);
        }catch (ExpiredCredentialsException e){
            return Result.error(ErrorStateEnum.CODE_EXPIRE);
        } catch (IncorrectCredentialsException e){
            return Result.error(ErrorStateEnum.CODE_ERROR);
        }

    }

    @Override
    public Result loginByPassword(String phone, String password) {
        log.info(String.format(" 手机号 [%s] 密码登入",phone));
        try{
            // 获取Subject
            Subject subject = SecurityUtils.getSubject();
            // 封装用户数据
            CustomizedToken token = new CustomizedToken(phone, password, LoginTypeEnum.PASSWORD_LOGIN_TYPE.toString());
            // 执行登录方法
            subject.login(token);
            // 返回登录后初始化参数
            return Result.success(returnLoginInitParam(phone));
        }catch (UnknownAccountException e) {
            return Result.error(ErrorStateEnum.USERNAME_NOT_EXIST);
        } catch (IncorrectCredentialsException e){
            return Result.error(ErrorStateEnum.PASSWORD_ERROR);
        }
    }

    /**
     * 从redis 中获取 信息 返回登录后初始化参数
     * @param phone phone
     * @return Map<String, Object>
     */
    private Map<String, Object> returnLoginInitParam(String phone) {

        // 按钮权限，角色权限，角色信息
        LoginUser loginUser = redisService.get(AppLoginKey.loginUserIinfo,phone, LoginUser.class);
        if(Objects.isNull(loginUser)){
            loginUser = redisService.setLogingUserRedis(this.findByUserPhone(phone));
        }



        //登入成功时，刷新旧的token。
        if(redisService.exists(AppLoginKey.userToken,phone)){
            redisService.delete(AppLoginKey.userToken,phone);
        }
        //生成新的token信息
        LoginToken loginToken = JwtUtil.createToken(phone);

        // 设置登入返回的模型
        Map<String, Object> data = new HashMap<>(2);
        data.put("userInfo", userToMap(loginUser));
        data.put("userToken", loginToken);
        redisService.set(AppLoginKey.userToken,phone,loginToken);
        return data;
    }

    /**
     * 封装 回传的 用户信息
     * @param loginUser
     * @return
     */
    public Map userToMap(LoginUser loginUser){
        if(loginUser!=null){
            Map map = new HashMap(6);
            map.put("phone",loginUser.getUser().getPhone());
            map.put("name",loginUser.getUser().getName());
            map.put("age",loginUser.getUser().getAge());
            map.put("sex",loginUser.getUser().getSex());
            map.put("icon",loginUser.getUser().getIcon());
            map.put("address",loginUser.getUser().getAddress());
            return map;
        }
        return null;
    }


    @Override
    public SysUser findByUserPhone(String phone) {
        return userDao.findByUserPhone(phone);
    }

    @Override
    public void sendModifyPasswordCode(String phone) throws ClientException {
        // 这里使用默认值，随机验证码的方法为CommonsUtils.getCode()
        int code = 7777;
        // todo 此处为发送验证码代码

        // 将验证码加密后存储到redis中
        redisService.set(AppLoginKey.modifyPassWordCode,phone,CommonsUtils.encryptPassword(String.valueOf(code)));

        notifyServiceThreadPool.execute(new TsmsSendService(new SmsSandSendPo(phone,"短信验证码："+code)));
    }

    @Override
    public void sendLoginCode(String phone) throws ClientException {
        // 这里使用默认值，随机验证码的方法为CommonsUtils.getCode()
        int code = 6666;
        // todo 此处为发送验证码代码

        // 将验证码加密后存储到redis中
        redisService.set(AppLoginKey.smsCode,phone,CommonsUtils.encryptPassword(String.valueOf(code)));
        notifyServiceThreadPool.execute(new TsmsSendService(new SmsSandSendPo(phone,"短信验证码："+code)));
    }

}
