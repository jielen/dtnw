package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcEbProjPrintPermitFM {
	public static Map fieldMap = new HashMap();
	
	static {
		
		fieldMap.putAll(ZcBaseBillFM.fieldMap);
		
		fieldMap.put("STATUS", "status");
		
		fieldMap.put("PROJ_CODE", "projCode");
		
	}
}
