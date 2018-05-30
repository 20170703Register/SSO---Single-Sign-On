package org.sso.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 发送HTTP请求的工具
 * */
public class HTTPUtils {


    public static String request(String url){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        String data = null;
        try {
            response = httpclient.execute(httpPost);                        // 发送请求
            if (response.getStatusLine().getStatusCode() == 200) {          // 响应成功
                data = EntityUtils.toString(response.getEntity(),"UTF-8");        // 响应数据
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpclient != null){
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "".equals(data) ? null : data;
    }
}
