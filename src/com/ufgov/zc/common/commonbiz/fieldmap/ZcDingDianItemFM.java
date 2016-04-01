package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcDingDianItemFM {

  public static Map fieldMap = new HashMap();

  static {

    fieldMap.putAll(ZcBaseBillFM.fieldMap);
 
    fieldMap.put("ITEM_TYPE", "itemType");
    fieldMap.put("ITEM_DETAIL", "itemDetail");
    fieldMap.put("ITEM_CONTENT", "itemContent");
  }

}
