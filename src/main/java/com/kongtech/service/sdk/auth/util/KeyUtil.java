package com.kongtech.service.sdk.auth.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class KeyUtil {

    private static final String TAG = "KeyUtil";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5PADDING";

    static String getHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT).trim();
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            return null;
        }
        return null;
    }

    static String encrypt(Context context, String value) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = getCipher(context);
        if (cipher == null) {
            return value;
        }
        SecretKeySpec keySpec = new SecretKeySpec(getKey(context), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(new byte[16]));
        byte[] result = cipher.doFinal(value.getBytes());
        return Base64.encodeToString(result, 0);
    }

    static String decrypt(Context context, String value) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = getCipher(context);
        if (cipher == null) {
            return value;
        }
        SecretKeySpec keySpec = new SecretKeySpec(getKey(context), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new byte[16]));
        byte[] result = cipher.doFinal(Base64.decode(value, 0));
        return new String(result);
    }

    private static byte[] getKey(Context context) {
        byte[] storeHashKey = getHashKey(context).getBytes();
        byte[] keyBytes = new byte[16];
        System.arraycopy(storeHashKey, 0, keyBytes, 0, Math.min(keyBytes.length, storeHashKey.length));
        return keyBytes;
    }

    private static Cipher getCipher(Context context) {
        try {
            return Cipher.getInstance(CIPHER_TRANSFORMATION);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        Log.w(TAG, "cipher not found");
        return null;
    }

}
