package org.sso.utils;


import org.springframework.data.redis.core.Cursor;

/**
 * 注销子系统的工具
 * */
public class LogoutUtils {


    /**
     * 通知客户端子系统注销
     **/
    public static void notifyClient(String token){
        Cursor<String> scanSet = RedisUtils.getScanSet(token);          // 根据Token获取set数据类型的值，值为子系统的url
        while (scanSet.hasNext()){
            String url = scanSet.next();
            if (url == null || "".equals(url)){
                continue;
            }
            HTTPUtils.request(url);                                     // 请求子系统销毁session（登出）
        }
        RedisUtils.delKey(token);                                       // 从Redis中删除该Token
    }
}
