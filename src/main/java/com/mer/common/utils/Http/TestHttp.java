package com.mer.common.utils.Http;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Program: zq-admin
 * @Description: 测试
 * @Author: 赵旗
 * @Create: 2021-04-21 15:14
 */
@Slf4j
public class TestHttp {
    /**
     * 请求地址
     */
    private static String url = "http://127.0.0.1:7777/app/login/pwdLogin";

    /**
     * 通信连接超时时间
     */
    private static int connectionTimeout = 30000;

    /**
     * 通信读超时时间
     */
    private static int readTimeOut = 30000;

    /**
     * 编码格式
     */
    private static String encoding = "UTF-8";

    /**
     * 请求参数 测试 数据
     *
     * @return
     */
    public static Map getData() {
        Map<String, String> map = new HashMap();
        map.put("phone", "15701556037");
        map.put("password", "556037");
        map.put("appType", "1");
        map.put("versionCode", "1");
        return map;
    }

    /**
     * 发送： key=value&key=value
     */
    public static void sendStr() {
        HttpClient client = new HttpClient(url, connectionTimeout, readTimeOut);
        try {
            int status = client.sendStr(getData(), encoding);
            if (200 == status) {
                String resultString = client.getResult();
                if (null != resultString && !"".equals(resultString)) {
                    // 反回结果
                    log.info("结果：" + resultString);
                    // 将返回结果转换为map
                    Map<String, String> tmpRspData = (Map) JSON.parse(resultString);
                    for (Object obj : tmpRspData.entrySet()) {
                        System.out.println(((Map.Entry) obj).getKey() + "     " + ((Map.Entry) obj).getValue());
                    }
                }
            } else {
                log.error("返回http状态码[" + status + "]，请检查请求报文或者请求地址是否正确");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("连接失败。。。。" + e.getMessage(), e);
        }
    }

    /**
     * 发送： key=value&key=value
     */
    public static void sendJson() {
        url ="http://127.0.0.1:7777/app/login/pwdLogin2";
        HttpClient client = new HttpClient(url, connectionTimeout, readTimeOut);
        try {
            int status = client.sendJson(getData(), encoding);
            if (200 == status) {
                String resultString = client.getResult();
                if (null != resultString && !"".equals(resultString)) {
                    // 反回结果
                    log.info("结果：" + resultString);
                    // 将返回结果转换为map
                    Map<String, String> tmpRspData = (Map) JSON.parse(resultString);
                    for (Object obj : tmpRspData.entrySet()) {
                        System.out.println(((Map.Entry) obj).getKey() + "     " + ((Map.Entry) obj).getValue());
                    }
                }
            } else {
                log.error("返回http状态码[" + status + "]，请检查请求报文或者请求地址是否正确");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("连接失败。。。。" + e.getMessage(), e);
        }
    }


    public static void main(String[] args) {
        sendStr();
        sendJson();
    }

}
