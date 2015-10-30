package com.ufgov.zc.client.zc.zcebsignup.operation;

import java.util.List;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.publish.IZcEbProjServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbSignupServiceDelegate;

public class ZcEbSignupBidOpt {
	
	private IZcEbSignupServiceDelegate zcEbSignupServiceDelegate = (IZcEbSignupServiceDelegate) ServiceFactory
			
			.create(IZcEbSignupServiceDelegate.class, "zcEbSignupServiceDelegate");
	
	private IZcEbProjServiceDelegate zcEbProjectServiceDelegate = (IZcEbProjServiceDelegate) ServiceFactory
			
			.create(IZcEbProjServiceDelegate.class, "zcEbProjServiceDelegate");
	
	public List getZcEbSignupProj(ElementConditionDto dto, RequestMeta meta){
		
		return zcEbProjectServiceDelegate.getZcEbSignupProj(dto, meta);
		
	}
	
	public List getZcEbSignup(ElementConditionDto dto, RequestMeta meta){
		
		return zcEbSignupServiceDelegate.getZcEbSignup(dto, meta);
		
	}

}
