package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcDingDianFM {
  public static Map fieldMap = new HashMap();

  static {

    fieldMap.putAll(ZcBaseBillFM.fieldMap);
    fieldMap.put("BI_SUM","biSum");
    fieldMap.put("CO_CODE","coCode");
    fieldMap.put("CO_NAME","coName");
    fieldMap.put("DD_CODE","ddCode");
    fieldMap.put("DD_NAME","ddName");
    fieldMap.put("DD_SUM","ddSum");
    fieldMap.put("DD_TYPE","ddType");
    fieldMap.put("EXCUTOR","excutor");
    fieldMap.put("EXCUTOR_NAME","excutorName");
    fieldMap.put("INPUT_DATE","inputDate"); 
    fieldMap.put("REMARK","remark");
    fieldMap.put("STATUS","status");
    fieldMap.put("SUPPLIER","supplier");
    fieldMap.put("SUPPLIER_NAME","supplierName");
    fieldMap.put("SU_BANK","suBank");
    fieldMap.put("SU_BANK_ACCOUNT","suBankAccount");
    fieldMap.put("SU_BANK_SHOUKUANREN","suBankShoukuanren");
    fieldMap.put("SU_LINK_MAN","suLinkMan");
    fieldMap.put("SU_LINK_TEL","suLinkTel");
    fieldMap.put("HT_SAOMIAO_FILE","htSaomiaoFile");
    fieldMap.put("HT_SAOMIAO_FILE_BLOBID","htSaomiaoFileId");
  }

}
