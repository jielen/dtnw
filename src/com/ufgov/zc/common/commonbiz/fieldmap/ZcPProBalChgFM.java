package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcPProBalChgFM {

  public static Map fieldMap = new HashMap();

  static {

    fieldMap.putAll(ZcBaseBillFM.fieldMap);

    fieldMap.put("ZC_P_PRO_BAL_CHG_AGENCY", "agency");
    fieldMap.put("ZC_P_PRO_BAL_CHG_AGENCY_NAME", "agencyName");

    fieldMap.put("ZC_P_PRO_BAL_CHG_STATUS", "status");

    fieldMap.put("ZC_P_PRO_BAL_CHG_CO_CODE", "coCode");

    fieldMap.put("ZC_P_PRO_BAL_CHG_ORG_CODE", "orgCode");

    fieldMap.put("ZC_P_PRO_BAL_CHG_ORG_NAME", "orgName");

    fieldMap.put("ZC_P_PRO_BAL_CHG_ND", "nd");

    fieldMap.put("ZC_P_PRO_BAL_CHG_ZC_MAKE_CODE", "zcMakeCode");

    fieldMap.put("ZC_P_PRO_BAL_CHG_ZC_MAKE_NAME", "zcMakeName");

    fieldMap.put("ZC_P_PRO_BAL_CHG_ZC_MONEY_BI_SUM", "zcMoneyBiSum");

    fieldMap.put("ZC_P_PRO_BAL_CHG_ZC_INPUT_CODE", "zcInputCode");

    fieldMap.put("ZC_P_PRO_BAL_CHG_ZC_INPUT_NAME", "zcInputName");

    fieldMap.put("ZC_INPUT_DATE", "zcInputDate");

  }

}
