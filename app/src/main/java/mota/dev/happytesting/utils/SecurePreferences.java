package mota.dev.happytesting.utils;

/**
 * Created by Slaush on 15/05/2017.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;


import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import mota.dev.happytesting.Consts;


public class SecurePreferences {

    public static class SecurePreferencesException extends RuntimeException {

        public SecurePreferencesException(Throwable e) {
            super(e);
        }

    }

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String KEY_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY_HASH_TRANSFORMATION = "SHA-256";
    private static final String CHARSET = "UTF-8";
    public static final int MODE = Context.MODE_PRIVATE;

    private final Cipher writer;
    private final Cipher reader;
    private final Cipher keyWriter;
    private final SharedPreferences preferences;

    public SecurePreferences(Context context, String param2)
            throws SecurePreferencesException {
        try {
            this.writer = Cipher.getInstance(TRANSFORMATION);
            this.reader = Cipher.getInstance(TRANSFORMATION);
            this.keyWriter = Cipher.getInstance(KEY_TRANSFORMATION);

            initCiphers(param2);

            this.preferences = context.getSharedPreferences(Consts.SHARED_PREF_NAME, MODE);
        } catch (GeneralSecurityException e) {
            throw new SecurePreferencesException(e);
        } catch (UnsupportedEncodingException e) {
            throw new SecurePreferencesException(e);
        }
    }

    protected void initCiphers(String helperParam) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidAlgorithmParameterException {
        IvParameterSpec ivSpec = getIv();
        SecretKeySpec secretKey = getSecretKey(helperParam);

        writer.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        reader.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        keyWriter.init(Cipher.ENCRYPT_MODE, secretKey);
    }

    protected IvParameterSpec getIv() {
        byte[] iv = new byte[writer.getBlockSize()];
        System.arraycopy("fldsjfodasjifudslfjdsaofshaufihadsf".getBytes(), 0, iv, 0, writer.getBlockSize());
        return new IvParameterSpec(iv);
    }

    protected SecretKeySpec getSecretKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] keyBytes = createKeyBytes(key);
        return new SecretKeySpec(keyBytes, TRANSFORMATION);
    }

    protected byte[] createKeyBytes(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(SECRET_KEY_HASH_TRANSFORMATION);
        md.reset();
        byte[] keyBytes = md.digest(key.getBytes(CHARSET));
        return keyBytes;
    }

    public void putString(String key, String value) {
        if (value != null) {
            String secureValueEncoded = encrypt(value, writer);
            preferences.edit().putString(toKey(key), secureValueEncoded).commit();
        }
    }


    public void putDouble(String key, Double value) {
        putString(key, Double.toString(value));
    }

    public void putInt(String key, int value) {
        putString(key, Integer.toString(value));
    }

    public void putBoolean(String key, boolean value) {
        putString(key, Boolean.toString(value));
    }


    public void remove(String key) {
        preferences.edit().remove(toKey(key)).commit();
    }

    public String getString(String key, String defValue) throws SecurePreferencesException {
        if (preferences.contains(toKey(key))) {
            String securedEncodedValue = preferences.getString(toKey(key), defValue);
            return decrypt(securedEncodedValue);
        }
        return defValue;
    }

    public int getInt(String key, int defValue) throws SecurePreferencesException {
        if (preferences.contains(toKey(key))) {
            String strValue = getString(key, "");
            if (strValue.equals(""))
                return defValue;
            else
                return Integer.valueOf(strValue);
        }
        return defValue;
    }

    public boolean getBoolean(String key, boolean defValue) throws SecurePreferencesException {
        if (preferences.contains(toKey(key))) {
            String strValue = getString(key, "");
            if (strValue.equals(""))
                return defValue;
            else
                return Boolean.valueOf(strValue);
        }
        return defValue;
    }

    public Double getDouble(String key, Double defValue) {
        if (preferences.contains(toKey(key))) {
            String strValue = getString(key, "");
            if (strValue.equals(""))
                return defValue;
            else
                return Double.valueOf(strValue);
        }
        return defValue;
    }

    private String toKey(String key) {
        return encrypt(key, keyWriter);
    }

    protected String encrypt(String value, Cipher writer) throws SecurePreferencesException {
        byte[] secureValue;
        try {
            secureValue = convert(writer, value.getBytes(CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new SecurePreferencesException(e);
        }
        String secureValueEncoded = Base64.encodeToString(secureValue, Base64.NO_WRAP);
        return secureValueEncoded;
    }


    protected String decrypt(String securedEncodedValue) {
        byte[] securedValue = Base64.decode(securedEncodedValue, Base64.NO_WRAP);
        byte[] value = convert(reader, securedValue);
        try {
            return new String(value, CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new SecurePreferencesException(e);
        }
    }

    private static byte[] convert(Cipher cipher, byte[] bs) throws SecurePreferencesException {
        try {
            return cipher.doFinal(bs);
        } catch (Exception e) {
            throw new SecurePreferencesException(e);
        }
    }

}
