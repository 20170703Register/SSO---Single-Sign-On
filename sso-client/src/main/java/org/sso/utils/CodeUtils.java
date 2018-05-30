package org.sso.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * 编码工具
 * */
public class CodeUtils {


    /**
     * URL编码
     * */
    public static String url(String url){
        String encodeURL = null;
        try {
            encodeURL = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeURL;
    }


    /**
     * Base64编码
     * */
    public static String base64(String code){
        String encode = null;
        try {
            byte[] bytes = (code).getBytes("UTF-8");
            encode = Base64.getEncoder().encodeToString(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encode;
    }

    /**
     * Base64解码
     * */
    public static String base64Decode(String code){
        String decode = null;
        try {
            decode = new String(Base64.getDecoder().decode(code), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }
}
