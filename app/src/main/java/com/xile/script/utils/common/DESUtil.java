package com.xile.script.utils.common;

import android.text.TextUtils;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESUtil {

    private static final String PASSWORD_CRYPT_KEY = "fzlewldsi";
    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};
    private final static String DES = "DES";

    /**
     * @param content
     * @return
     * @throws Exception
     */
    public static String encrypt(String content) {
        if (!TextUtils.isEmpty(content)) {
            Cipher cipher = null;
            try {
                IvParameterSpec zeroIv = new IvParameterSpec(iv);
                DESKeySpec dks = new DESKeySpec(PASSWORD_CRYPT_KEY.getBytes());
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
                SecretKey securekey = keyFactory.generateSecret(dks);
                cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, securekey, zeroIv);
                return Base64.encodeToString(cipher.doFinal(content.getBytes()), Base64.NO_WRAP);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * @param src
     * @return
     * @throws Exception
     */
    public static String decrypt(String src) {
        if (!TextUtils.isEmpty(src)) {
            try {
                IvParameterSpec zeroIv = new IvParameterSpec(iv);
                DESKeySpec dks = new DESKeySpec(PASSWORD_CRYPT_KEY.getBytes());
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
                SecretKey securekey = keyFactory.generateSecret(dks);
                Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, securekey, zeroIv);
                return new String(cipher.doFinal(Base64.decode(src.getBytes(), Base64.NO_WRAP)));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


}
