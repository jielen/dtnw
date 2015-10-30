package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcHtPrePayBillFM {

  public static Map fieldMap = new HashMap();

  static {

    fieldMap.putAll(ZcBaseBillFM.fieldMap);

    fieldMap.put("BILL_CODE", "billCode");

    fieldMap.put("ZC_MAKE_CODE", "zcMakeCode");

    fieldMap.put("ZC_HT_CODE", "zcHtCode");

    fieldMap.put("CO_CODE", "coCode");

    fieldMap.put("ND", "nd");

    fieldMap.put("ORG_CODE", "orgCode");

    fieldMap.put("REMARK", "remark");

    fieldMap.put("EXECUTOR", "executor");

    fieldMap.put("EXECUTE_DATE", "executeDate");

    fieldMap.put("EXECUTOR_NAME", "executorName");
    fieldMap.put("ZC_MAKE_NAME", "zcMakeName");

    fieldMap.put("ZC_HT_NAME", "zcHtName");

  }

}
