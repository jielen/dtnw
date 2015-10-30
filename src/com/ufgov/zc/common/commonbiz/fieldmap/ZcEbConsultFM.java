package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcEbConsultFM {

  public static Map fieldMap = new HashMap();

  static {
    
    fieldMap.putAll(ZcBaseBillFM.fieldMap);
    
    fieldMap.put("ID", "id");
    fieldMap.put("TITLE", "title");
    fieldMap.put("STATUS", "status");
    fieldMap.put("TYPE", "type");
    fieldMap.put("EXECUTOR", "executor");
    fieldMap.put("EXECUTE_DATE", "executeDate");
    fieldMap.put("PROBLEM", "problem");
    fieldMap.put("REPLY", "reply");
    fieldMap.put("REPLY_DATE", "replyDate");

  }
}
