package com.ufgov.zc.common.zc.filemstools;import java.io.FileInputStream;import java.io.FileOutputStream;import java.io.InputStream;import java.io.OutputStream;import java.security.MessageDigest;import java.security.SecureRandom;import javax.crypto.Cipher;import javax.crypto.CipherInputStream;import javax.crypto.CipherOutputStream;import javax.crypto.SecretKey;import javax.crypto.SecretKeyFactory;import javax.crypto.spec.DESKeySpec;public class FileSign {  public static String decrypt(String srcFile, String destfile, String pwd) throws Exception {    try {      SecureRandom sr = new SecureRandom();      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");      DESKeySpec dks = new DESKeySpec(padding(pwd.getBytes()));      SecretKey securekey = keyFactory.generateSecret(dks);      Cipher cipher = Cipher.getInstance("DES");      cipher.init(2, securekey, sr);      InputStream is = new FileInputStream(srcFile);      OutputStream out = new FileOutputStream(destfile);      CipherOutputStream cos = new CipherOutputStream(out, cipher);      byte[] buffer = new byte[4096];      byte[] pwdByte = new byte[20];      int len = is.read(pwdByte);      byte[] hashByte = getHashEncode(pwd.getBytes());      if ((len != 20) || (hashByte.length != 20)) {        throw new RuntimeException("密码错误");      }      for (int i = 0; i < 20; i++)        if (pwdByte[i] != hashByte[i])          throw new RuntimeException("密码错误");      int r;      while ((r = is.read(buffer)) >= 0) {        cos.write(buffer, 0, r);      }      cos.close();      out.close();      is.close();      return "TRUE";    } catch (Exception e) {      return e.getMessage();    }  }  public static String encrypt(String srcFile, String destfile, String pwd) throws Exception {    try {      SecureRandom sr = new SecureRandom();      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");      DESKeySpec dks = new DESKeySpec(padding(pwd.getBytes()));      SecretKey securekey = keyFactory.generateSecret(dks);      Cipher cipher = Cipher.getInstance("DES");      cipher.init(1, securekey, sr);      InputStream is = new FileInputStream(srcFile);      OutputStream out = new FileOutputStream(destfile);      CipherInputStream cis = new CipherInputStream(is, cipher);      byte[] buffer = new byte[1024];      out.write(getHashEncode(pwd.getBytes()));      int r;      while ((r = cis.read(buffer)) > 0) {        out.write(buffer, 0, r);      }      cis.close();      is.close();      out.close();      return "TRUE";    } catch (Exception e) {      return e.getMessage();    }  }  private static byte[] padding(byte[] src) {    int length = src.length + src.length % 8;    byte[] rst = new byte[length];    System.arraycopy(src, 0, rst, 0, src.length);    return rst;  }  private static byte[] getHashEncode(byte[] str) {    try {      MessageDigest sha = MessageDigest.getInstance("SHA");      sha.update(str);      return sha.digest();    } catch (Exception e) {      e.printStackTrace();      throw new RuntimeException(e);    }  }}