package com.ufgov.zc.common.commonbiz.fieldmap;import java.util.HashMap;import java.util.Map;public class ProjectFM {  public static Map fieldMap = new HashMap();  static {    fieldMap.putAll(ZcBaseBillFM.fieldMap);    fieldMap.put("PD_YEAR", "nd");    fieldMap.put("PD_PROJECT_CODE", "code");    fieldMap.put("PD_PROJECT_NAME", "name");    fieldMap.put("PD_PARENT_PROJECT_CODE", "parentCode");    fieldMap.put("PD_CREA_CO_CODE", "pdCreaCoCode");    fieldMap.put("PD_PROJECT_TYPE_CODE", "pdProjectTypeCode");    fieldMap.put("PD_IS_LOWEST", "pdIsLowest");    fieldMap.put("PD_VERSION_ID", "pdVersionId");  }}