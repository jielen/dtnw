/**
 * 
 */
package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class ZcEbJieXiangFM {

  public static Map fieldMap = new HashMap();

  static {

    fieldMap.putAll(ZcEbAuditSheetFM.fieldMap);

    fieldMap.put("zhongbiaoSum", "zhongbiaoSum");

    fieldMap.put("cancel_reason", "entrustCancel.remark");
  }
}
