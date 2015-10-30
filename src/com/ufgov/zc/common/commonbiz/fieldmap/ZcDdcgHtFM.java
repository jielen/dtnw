package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcDdcgHtFM {
  public static Map fieldMap = new HashMap();
  static {
    fieldMap.putAll(ZcBaseBillFM.fieldMap);
    fieldMap.put("ZC_HT_STATUS", "zcHtStatus");
    fieldMap.put("ZC_HT_CODE", "zcHtCode");
    fieldMap.put("ZC_HT_NAME", "zcHtName");
    fieldMap.put("ZC_MAKE_CODE", "zcMakeCode");
    fieldMap.put("CO_CODE", "coCode");
    fieldMap.put("ZC_ZG_CS_CODE", "zcZgCsCode");
    fieldMap.put("ZC_REQ_CODE", "zcReqCode");
    fieldMap.put("ZC_SGN_DATE", "zcSgnDate");
    fieldMap.put("ZC_SU_NAME", "zcSuName");
    fieldMap.put("ZC_SU_LINKMAN", "zcSuLinkman");
    fieldMap.put("ZC_BID_CODE", "zcBidCode");
  }
}
