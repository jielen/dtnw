package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcQxFM {
  public static Map fieldMap = new HashMap();

  static {

    fieldMap.putAll(ZcBaseBillFM.fieldMap);

    fieldMap.put("QX_NAME", "qxName");

    fieldMap.put("CO_NAME", "coName");

    fieldMap.put("CO_CODE", "coCode");

    fieldMap.put("ZHE_RANG_LV", "zheRangLv");

    fieldMap.put("SU_LINK_MAN", "suLinkMan");

    fieldMap.put("SU_LINK_TEL", "suLinkTel");

    fieldMap.put("WX_DATE", "wxDate");

    fieldMap.put("SU_BANK_ACCOUNT", "suBankAccount");

    fieldMap.put("SU_BANK", "suBank");

    fieldMap.put("SU_BANK_SHOUKUANREN", "suBankShoukuanren");
  }

}
