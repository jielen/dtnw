/** * ZcEbOpenBidTeam.java * com.ufgov.gk.common.commonbiz.fieldmap * Administrator * 2010-5-27 */package com.ufgov.zc.common.commonbiz.fieldmap;import java.util.HashMap;import java.util.Map;/** * @author Administrator * */public class ZcEbOpenBidTeamFM {  public static Map fieldMap = new HashMap();  static {    fieldMap.putAll(ZcBaseBillFM.fieldMap);    fieldMap.put("TEAM_CODE", "teamCode");    fieldMap.put("TEAM_NAME", "teamName");    fieldMap.put("PROJ_CODE", "projCode");    fieldMap.put("CREAT_DATE", "creatDate");    fieldMap.put("CREATOR", "creator");    fieldMap.put("REMARK", "remark");  }}