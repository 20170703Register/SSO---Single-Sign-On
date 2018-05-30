package org.sso.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Reids工具类
 * */
@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static StringRedisTemplate redis;

    @PostConstruct
    private void init(){
        this.redis = redisTemplate;
    }

    /**
     * 保存字符串
     * */
    public static void setV(String key, String value){
        try {
            redis.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取字符串
     * */
    public static String getV(String key){
        String value = null;
        try {
            value = redis.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 使用set集合保存字符串
     * */
    public static long addSet(String key, String... value){
        long num = 0;
        try {
            num = redis.opsForSet().add(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * 获取set集合的所有字符串
     * */
    public static Cursor<String> getScanSet(String key){
        Cursor<String> scan = null;
        try {
            scan = redis.opsForSet().scan(key, ScanOptions.NONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scan;
    }

    /**
     * 通过key删除set集合中的一个字符串
     * */
    public static long removeSet(String key, String... value){
        long num = 0;
        try {
            num = redis.opsForSet().remove(key, value);
            redis.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * 判断key是否存在
     * */
    public static boolean hasKey(String key){
        boolean isExist = false;
        try{
            isExist = redis.hasKey(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return isExist;
    }

    /**
     * 通过key删除key
     * */
    public static void delKey(String key){
        redis.delete(key);
    }
}
