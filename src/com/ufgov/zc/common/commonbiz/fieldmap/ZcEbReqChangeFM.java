package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcEbReqChangeFM {

  public static Map fieldMap = new HashMap();

  static {

    fieldMap.putAll(ZcBaseBillFM.fieldMap);
    fieldMap.put("CHANGE_CODDE", "changeCode");
    fieldMap.put("ZC_MAKE_CODE", "zcMakeCode");
    fieldMap.put("CO_CODE", "coCode");
    fieldMap.put("STATUS", "status");
    fieldMap.put("SN", "sn");
    fieldMap.put("SN_CODE", "snCode");
    fieldMap.put("ZC_INPUT_CODE", "zcInputCode");

  }
}
