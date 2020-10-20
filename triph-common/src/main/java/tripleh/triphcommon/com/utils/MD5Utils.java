package tripleh.triphcommon.com.utils;

import org.springframework.util.DigestUtils;

/**
 * Author: zixli
 * Date: 2020/10/16 9:19
 * FileName: MD5Utils
 * Description: MD5工具类
 */
public class MD5Utils {

    /**
     * 加密
     * @param org 要加密的字符串
     * @return  加密字符串
     */
    public static String encrypt(String org) {
        // 基于spring框架中的DigestUtils工具类进行密码加密
        return DigestUtils.md5DigestAsHex((org).getBytes());
    }

}
