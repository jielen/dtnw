package com.snca.financebidding;

public class SignEncry {
  static {
    // System.loadLibrary("CZTSignEncry");
    // System.loadLibrary("yblib");
    // System.loadLibrary("log4cppD");
    System.loadLibrary("TestDllJni2");
  }

  /**
   * encrypt file
   * @param sourceFilePath the file path is string
   * @param produceKeyAlgorithm get key's algorithm
   * @param encryptAlgorithm the encrypt algorithm
   * @param needDelFile need del the source file or not
   * @return byte[] is the key
   */
  public native static byte[] encryptFile(String sourceFilePath, String aimFilePath, String produceKeyAlgorithm, String encryptAlgorithm,
    String needDelFile);

  /**
   * decrypt file by key
   * @param sourceFilePath the file path is string
   * @param decryptKey used for decrypt Key's bytes
   * @param decryptAlgorithm the decrypt algorithm
   * @return
   */
  public native static String decryptFile(String sourceFilePath, String aimFilePath, byte[] decryptKey, String decryptAlgorithm);

  public static void main(String[] args) {
    encryptFile("D:\\svs.rar", "D:\\svs.rar.en", "123456789", "3DES", "1");
    // decryptFile("D:\\svs.rar.en", "D:\\svs1.rar", "123456789".getBytes(), "3DES");
  }
}
