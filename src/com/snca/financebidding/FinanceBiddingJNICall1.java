package com.snca.financebidding;

public class FinanceBiddingJNICall1 {
  static {
    System.loadLibrary("SnCASSSignEncry");
  }

  public native static String StringEncryDES(String data, String pwd) throws Exception;

  public native static String StringDecryptDES(String data, String pwd) throws Exception;
}
