package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcEbPackFM {

  public static Map fieldMap = new HashMap();

  static {

    fieldMap.putAll(ZcBaseBillFM.fieldMap);

    fieldMap.put("PACK_NAME", "packName");

    fieldMap.put("PACK_BUDGET", "packBudget");

    fieldMap.put("ENTRUST_CODE", "entrustCode");

    fieldMap.put("PUR_TYPE", "purType");

    fieldMap.put("STATUS", "status");

    fieldMap.put("PACK_DESC", "packDesc");

  }

}
