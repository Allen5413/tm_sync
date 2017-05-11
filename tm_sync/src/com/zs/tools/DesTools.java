package com.zs.tools;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * DES加解密
 * Created by Allen on 2016/1/13.
 */
public class DesTools {

    private final static String DES = "DES";

    public static void main(String[] args) throws Exception {
        String data = "spot02";
        String key = "eduwest";
        System.err.println(encrypt(data, key));
        System.err.println(decrypt("Dnjg+PD431Q=", key));

    }

    /**
     * Description 根据键值进行加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        //获取当前时间
        String year = DateTools.getThisYear();
        String month = Integer.parseInt(DateTools.getThisMonth()) < 10 ? "0"+DateTools.getThisMonth() : DateTools.getThisMonth();
        String day = Integer.parseInt(DateTools.getThisDay()) < 10 ? "0"+DateTools.getThisDay() : DateTools.getThisDay();
        key += year+month+day;
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }

    /**
     * Description 根据键值进行解密
     * @param data
     * @return
     * @throws java.io.IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        //获取当前时间
        String year = DateTools.getThisYear();
        String month = Integer.parseInt(DateTools.getThisMonth()) < 10 ? "0"+DateTools.getThisMonth() : DateTools.getThisMonth();
        String day = Integer.parseInt(DateTools.getThisDay()) < 10 ? "0"+DateTools.getThisDay() : DateTools.getThisDay();
        key += year+month+day;
        System.out.println("KEY:  "+key);
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf,key.getBytes());
        System.out.println("bt:  "+new String(bt));
        return new String(bt);
    }



    /**
     * Description 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }


    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }
}
