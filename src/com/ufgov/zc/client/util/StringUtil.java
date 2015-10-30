package com.ufgov.zc.client.util;

public class StringUtil {

  /**
   * freemark填充word时特殊字符要转换，否则word将不能打开
   * @param str
   * @return
   */
  public static String freeMarkFillWordChar(String str){
    if(str == null){
      return "";
    }
    return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
  }
}
