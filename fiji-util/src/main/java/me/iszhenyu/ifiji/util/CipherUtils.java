package me.iszhenyu.ifiji.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

/**
 * @author zhen.yu
 * @since 2017/6/8
 */
public class CipherUtils {

    public static String hmacSHA256Digest(String key, String content) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            byte[] secretByte = key.getBytes("utf-8");
            byte[] dataBytes = content.getBytes("utf-8");

            SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");
            mac.init(secret);

            byte[] doFinal = mac.doFinal(dataBytes);
            byte[] hexB = new Hex().encode(doFinal);
            return new String(hexB, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hmacSHA256Digest(String key, Map<String, String> map) {
        StringBuilder s = new StringBuilder();
        map.values().forEach(s::append);
        return hmacSHA256Digest(key, s.toString());
    }

}
