package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcQbFM {
  public static Map fieldMap = new HashMap();

  static {

    fieldMap.putAll(ZcBaseBillFM.fieldMap);

    fieldMap.put("QB_NAME", "qbName");

    fieldMap.put("CO_NAME", "coName");

    fieldMap.put("CO_CODE", "coCode");

    fieldMap.put("SU_LINK_MAN", "suLinkMan");

    fieldMap.put("SU_LINK_TEL", "suLinkTel");

    fieldMap.put("SU_BANK_ACCOUNT", "suBankAccount");

    fieldMap.put("SU_BANK", "suBank");

    fieldMap.put("SU_BANK_SHOUKUANREN", "suBankShoukuanren");
  }

}


