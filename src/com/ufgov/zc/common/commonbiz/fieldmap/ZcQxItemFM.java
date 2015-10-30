package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcQxItemFM {

  public static Map fieldMap = new HashMap();

  static {

    fieldMap.putAll(ZcBaseBillFM.fieldMap);

    fieldMap.put("CHE_PAI", "chePai");

    fieldMap.put("ITEM_CONTENT", "itemContent");

    fieldMap.put("ITEM_NUM", "itemNum");

    fieldMap.put("ITEM_PRICE", "itemPrice");

    fieldMap.put("ITEM_TYPE", "itemType");
  }

}
