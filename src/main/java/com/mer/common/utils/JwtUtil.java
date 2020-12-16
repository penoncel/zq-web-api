package com.mer.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mer.common.constant.Constant;
import com.mer.project.vo.resp.LoginTokenVo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @Program: zq-web-api
 * @Description: Jwt 工具
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Slf4j
public class JwtUtil {

    /**
     * 公共秘钥-保存在服务器端
     */
    private static String SECRET="01a1a22a6c17cafc32102357911edb48";

    /**
     * 获得 token 中的信息无需 secret解密也能获得
     * @param token token
     * @return token中包含的用户手机号
     */
    public static String getPhone(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("phone").asString();
        } catch (JWTDecodeException e) {
            throw e;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @param request HttpServletRequest
     * @return token中包含的用户id
     */
    public static String getPhone(HttpServletRequest request) {
        try {
            DecodedJWT jwt = JWT.decode(getToken(request));
            return jwt.getClaim("phone").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * 获取当前登录用户的token
     * @param request HttpServletRequest
     * @return token
     */
    public static String getToken(HttpServletRequest request){
        return request.getHeader(Constant.TOKEN_HEADER_NAME);
    }

    /**
     * 创建 访问 的 token
     * @param phone 用户名/手机号
     * @return 加密的token
     */
    public static LoginTokenVo createToken(String phone) {
        LoginTokenVo loginTokenVo = new LoginTokenVo();
        try{
            Date date = new Date(System.currentTimeMillis() + Constant.TOKEN_EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            Map<String,Object> map = new HashMap<>(2);
            map.put("alg","HS256");
            map.put("typ","Json Web Token");
            String token = JWT.create()
                    //header
                    .withHeader(map)
                    //layload 用户登入的手机号
                    .withClaim("phone", phone)
                    //签发时间
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    //过期时间 过期时间要大于签发时间
                    .withExpiresAt(date)
                    //加密
                    .sign(algorithm);
            loginTokenVo.setToken(token);
            loginTokenVo.setTokenPeriodTime(usTimeToString(date.toString()));
            createRefreshToken(map, loginTokenVo,phone);
            return loginTokenVo;
        }catch (Exception e){
            return null;
        }
    }


    /**
     * 创建 刷新 token
     * @param map         header
     * @param loginTokenVo  反回的实体类
     * @param phone       用户手机号
     * @return 用户登入模版
     */
    public static LoginTokenVo createRefreshToken(Map<String,Object> map, LoginTokenVo loginTokenVo, String phone) {
        try{
            Date date = new Date(System.currentTimeMillis() + Constant.REFRESHTOKEN_EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            String token = JWT.create()
                    //header
                    .withHeader(map)
                    //layload 用户登入的手机号
                    .withClaim("phone", phone)
                    //签发时间
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    //过期时间 过期时间要大于签发时间
                    .withExpiresAt(date)
                    //加密
                    .sign(algorithm);
            loginTokenVo.setRefreshToken(token);
            return loginTokenVo;
        }catch (Exception e){
            return null;
        }
    }


    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @return 是否正确
     */
    public static boolean verify(String token) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("phone", getPhone(token))
                    .build();
            // 效验TOKEN
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw e;
        }
        return true;
    }

    /**
     * 过期时间转换
     * @param times
     * @return
     * @throws ParseException
     */
    private static String usTimeToString(String times)throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(times));
    }

    public static void main(String[] args) throws ParseException {
//        Date date = new Date(System.currentTimeMillis() + Constant.TOKEN_EXPIRE_TIME);
//        System.out.println(date.toString());
//        Date tokenOutDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(date.toString());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        //token 过期时间
//        String tokenTimeOutTimes = dateFormat.format(tokenOutDate);
//        System.out.println(tokenTimeOutTimes);
//        tokenTimeIsNotRefsh("2020-05-31 17:25:25");
//
//        LoginToken loginToken =createToken("15701556037");
//        System.out.println(loginToken.toString());
//        System.out.println(getPhone(loginToken.getToken()));

        long a = getIssuedAt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwaG9uZSI6IjE1NzAxNTU2MDM3IiwiZXhwIjoxNjA4MTk3MTEyLCJpYXQiOjE2MDgwMjQzMTJ9.5mmb2WSNzJprG9nHVLNGkZg7aLxqDHndsL6BzDbQJng");
        long b = getIssuedAt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwaG9uZSI6IjE1NzAxNTU2MDM3IiwiZXhwIjoxNjA4MjU4Mzc3LCJpYXQiOjE2MDgwODU1Nzd9.iLGqnUu-IKMuqvLe5pkfguIMixC17McFNduR5KlaPJE");
        log.info("时间A：{},时间B：{}",a,b);
    }

    /**
     * 获取 token 签发时间
     * @param token token
     * @return
     */
    public static long getIssuedAt(String token){
//        System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(JWT.decode(token).getIssuedAt())+"---"+JWT.decode(token).getIssuedAt().getTime());
        return JWT.decode(token).getIssuedAt().getTime();
    }



    /**
     * 是否需要刷新token
     * @param refreshTime  刷新时间
     * @return
     * @throws ParseException
     */
    public static boolean tokenTimeIsNotRefsh(String refreshTime) {
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tokenUsTims = refreshTime;

            //token过期时间
            Date tokenTime = dateFormat.parse(tokenUsTims);

            //当前系统时间
            String sysTimes = dateFormat.format(new Date());
            Date nowTimes = dateFormat.parse(sysTimes);
            System.out.println("Token 过期时间 =  " + refreshTime+" ， 当前系统 时间 =  " + sysTimes);

            //token过期时间 与当前系统时间比较 差额天数，如果小于 1天 则自动刷新token
            int days = differentDaysByMillisecond(tokenTime,nowTimes);
            System.out.println("两个日期的差距：" + days);
            // days < 0 说明是负数，已经过期很久，days < 1 时间差2天 就得重新进行刷新
            if(days < 0 || days<1){
                //进行刷新创建
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1,Date date2) {
        int days = (int) ((date1.getTime() - date2.getTime()) / (1000*3600*24));
        return days;
    }


}
