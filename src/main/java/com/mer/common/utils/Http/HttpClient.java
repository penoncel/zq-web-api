package com.mer.common.utils.Http;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * @Program: zq-admin
 * @Description: HTTP请求
 * @Author: 赵旗
 * @Create: 2021-04-21 10:23
 * <p>
 * conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset= UTF-8"); 参数形式: name1=value1&name2=value2
 * <p>
 * conn.setRequestProperty("Content-Type", "application/json;charset= UTF-8"); 参数形式: {key1:value1,key2:value2}
 * <p>
 * conn.setRequestProperty("Content-Type", "multipart/form-data;charset= UTF-8");
 * multipart/form-data; boundary= b o u n d / / 其 中 {bound} // 其中 bound//其中{bound}是一个占位符，代表我们规定的分割符，可以自己任意规定，但为了避免和正常文本重复了，尽量要使用复杂一点的内容。
 */
@SuppressWarnings("/all")
@Slf4j
public class HttpClient {
    /**
     * 目标地址
     */
    private URL url;

    /**
     * 通信连接超时时间
     */
    private int connectionTimeout;

    /**
     * 通信读超时时间
     */
    private int readTimeOut;

    /**
     * 通信结果
     */
    private String result;

    /**
     * 获取通信结果
     *
     * @return
     */
    public String getResult() {
        return result;
    }

    /**
     * 设置通信结果
     *
     * @param result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 构造函数
     *
     * @param url               目标地址
     * @param connectionTimeout HTTP连接超时时间
     * @param readTimeOut       HTTP读写超时时间
     */
    public HttpClient(String url, int connectionTimeout, int readTimeOut) {
        try {
            this.url = new URL(url);
            this.connectionTimeout = connectionTimeout;
            this.readTimeOut = readTimeOut;
            log.info("通信目标地址{}:" + url);
            log.info("通信连接超时时间{}:" + connectionTimeout);
            log.info("通信读超时时间{}:" + readTimeOut);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 发送信息到服务端
     *
     * @param data     请求参数
     * @param encoding 编码格式
     * @return 同步反回 状态吗 HTTP/1.0 200 OK  、HTTP/1.0 401 Unauthorized
     * @throws Exception
     */
    public int sendJson(Map<String, String> data, String encoding) throws Exception {
        try {
            // 创建连接
            HttpURLConnection httpURLConnection = createConnection(encoding);
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=" + encoding);
            if (null == httpURLConnection) {
                throw new Exception("创建联接失败");
            }
            // 将Map存储的对象，转换 json
            String sendData = JSON.toJSONString(data);
            log.info("请求报文:[" + sendData + "]");
            // HTTP Post发送消息
            this.requestServer(httpURLConnection, sendData, encoding);
            // 显示Response消息
            this.result = this.response(httpURLConnection, encoding);
            log.info("返回报文:[" + result + "]");
            return httpURLConnection.getResponseCode();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 发送信息到服务端
     *
     * @param data     请求参数
     * @param encoding 编码格式
     * @return 同步反回 状态吗 HTTP/1.0 200 OK  、HTTP/1.0 401 Unauthorized
     * @throws Exception
     */
    public int sendStr(Map<String, String> data, String encoding) throws Exception {
        try {
            // 创建连接
            HttpURLConnection httpURLConnection = createConnection(encoding);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);
            if (null == httpURLConnection) {
                throw new Exception("创建联接失败");
            }
            // 将Map存储的对象，转换为key=value&key=value的字符
            String sendData = this.getRequestParamString(data, encoding);
            log.info("请求报文:[" + sendData + "]");
            // HTTP Post发送消息
            this.requestServer(httpURLConnection, sendData, encoding);
            // 显示Response消息
            this.result = this.response(httpURLConnection, encoding);
            log.info("返回报文:[" + result + "]");
            return httpURLConnection.getResponseCode();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 创建连接
     *
     * @param encoding 编码格式
     * @return
     * @throws ProtocolException
     */
    private HttpURLConnection createConnection(String encoding) throws ProtocolException {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        // 设置通用的请求属性
        httpURLConnection.setRequestProperty("accept", "*/*");
        httpURLConnection.setRequestProperty("connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        httpURLConnection.setRequestProperty("Accept-Charset", encoding);
        // 连接超时时间
        httpURLConnection.setConnectTimeout(this.connectionTimeout);
        // 读取结果超时时间
        httpURLConnection.setReadTimeout(this.readTimeOut);
        // 发送POST请求必须设置如下两行 setDoInput  setDoOutput
        httpURLConnection.setRequestMethod("POST");
        // 可读
        httpURLConnection.setDoInput(true);
        // 可写
        httpURLConnection.setDoOutput(true);
        // 取消缓存
        httpURLConnection.setUseCaches(false);
        if ("https".equalsIgnoreCase(url.getProtocol())) {
            HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
            husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
//            husn.setHostnameVerifier(new TrustAnyHostnameVerifier());//解决由于服务器证书问题导致HTTPS无法访问的情况
            return husn;
        }
        return httpURLConnection;
    }

    /**
     * 将Map存储的对象，转换为key=value&key=value的字符
     *
     * @param requestParam 请求参数map
     * @param encoding     编码格式
     * @return
     */
    private String getRequestParamString(Map<String, String> requestParam, String encoding) {
        if (null == encoding || "".equals(encoding)) {
            encoding = "UTF-8";
        }
        StringBuffer sf = new StringBuffer("");
        String reqstr = "";
        if (null != requestParam && 0 != requestParam.size()) {
            for (Map.Entry<String, String> en : requestParam.entrySet()) {
                try {
                    sf.append(en.getKey() + "=" + (null == en.getValue() || "".equals(en.getValue()) ? "" : URLEncoder.encode(en.getValue(), encoding)) + "&");
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage(), e);
                    return "";
                }
            }
            reqstr = sf.substring(0, sf.length() - 1);
        }
        log.info("请求报文(已做过URLEncode编码):[" + reqstr + "]");
        return reqstr;
    }


    /**
     * HTTP Post发送消息
     *
     * @param connection Http连接
     * @param message    消息内容
     * @param encoder    编码格式
     * @throws IOException
     */
    private void requestServer(final URLConnection connection, String message, String encoder) throws Exception {
        PrintStream out = null;
        try {
            connection.connect();
            out = new PrintStream(connection.getOutputStream(), false, encoder);
            out.print(message);
            out.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }


    /**
     * 显示Response消息
     *
     * @param connection
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    private String response(final HttpURLConnection connection, String encoding) throws Exception {
        InputStream in = null;
        StringBuilder sb = new StringBuilder(1024);
        BufferedReader br = null;
        try {
            if (200 == connection.getResponseCode()) {
                in = connection.getInputStream();
                sb.append(new String(read(in), encoding));
            } else {
                in = connection.getErrorStream();
                sb.append(new String(read(in), encoding));
            }
            log.info("HTTP Return Status-Code:[" + connection.getResponseCode() + "]");
            return sb.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != br) {
                br.close();
            }
            if (null != in) {
                in.close();
            }
            if (null != connection) {
                connection.disconnect();
            }
        }
    }

    public static byte[] read(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
        int length = 0;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        while ((length = in.read(buf, 0, buf.length)) > 0) {
            bout.write(buf, 0, length);
        }
        bout.flush();
        return bout.toByteArray();
    }


    /**
     * 发送信息到服务端 GET方式
     *
     * @param encoding
     * @return
     * @throws Exception
     */
    public int sendGet(String encoding) throws Exception {
        try {
            HttpURLConnection httpURLConnection = createConnectionGet(encoding);
            if (null == httpURLConnection) {
                throw new Exception("创建联接失败");
            }
            this.result = this.response(httpURLConnection, encoding);
            log.error("同步返回报文:[" + result + "]");
            return httpURLConnection.getResponseCode();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 创建连接
     *
     * @return
     * @throws ProtocolException
     */
    private HttpURLConnection createConnectionGet(String encoding) throws ProtocolException {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        httpURLConnection.setConnectTimeout(this.connectionTimeout);// 连接超时时间
        httpURLConnection.setReadTimeout(this.readTimeOut);// 读取结果超时时间
        httpURLConnection.setUseCaches(false);// 取消缓存
        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=" + encoding);
        httpURLConnection.setRequestMethod("GET");
        if ("https".equalsIgnoreCase(url.getProtocol())) {
            HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
            husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
//            husn.setHostnameVerifier(new TrustAnyHostnameVerifier());//解决由于服务器证书问题导致HTTPS无法访问的情况
            return husn;
        }
        return httpURLConnection;
    }

}