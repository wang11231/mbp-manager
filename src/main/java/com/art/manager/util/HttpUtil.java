package com.art.manager.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;

/**
 * 企业付款http请求工具
 */
@Slf4j
public class HttpUtil {
    private static final String MCH_ID  = "1551018691";//证书密码默认是商户号
    private static SSLContext wx_ssl_context = null; //微信支付ssl证书
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int CONNECT_TIME_OUT = 5000; //链接超时时间3秒
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().setConnectTimeout(CONNECT_TIME_OUT).build();
    static{
        Resource resource = new ClassPathResource("apiclient_cert.p12");//该证书名字最好改为别人猜不到的
        try {
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            char[] keyPassword = MCH_ID.toCharArray(); //证书密码
            keystore.load(resource.getInputStream(), keyPassword);
            wx_ssl_context = SSLContexts.custom().loadKeyMaterial(keystore, keyPassword).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 功能描述: post https请求，服务器双向证书验证
     * @param url 请求地址
     * @param s 参数xml
     * @return 请求失败返回null
     */
    public static String posts(String url, String s) throws UnsupportedEncodingException {
        CloseableHttpClient httpClient = null;
        log.info("request wechat transfers interface url:{}"+url);
        HttpPost httpPost = new HttpPost(url);
        String body = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.custom().setDefaultRequestConfig(REQUEST_CONFIG).setSSLSocketFactory(getSSLConnectionSocket()).build();
            httpPost.setEntity(new StringEntity(s, DEFAULT_CHARSET));
            response = httpClient.execute(httpPost);
            body = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return body;
    }

    //获取ssl connection链接
    private static SSLConnectionSocketFactory getSSLConnectionSocket() {
        return new SSLConnectionSocketFactory(wx_ssl_context, new String[] {"TLSv1", "TLSv1.1", "TLSv1.2"}, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }
}
