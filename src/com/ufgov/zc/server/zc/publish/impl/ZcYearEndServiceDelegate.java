package com.ufgov.zc.server.zc.publish.impl;

import java.util.List;

import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcYearPlan;
import com.ufgov.zc.common.zc.publish.IZcYearEndServiceDelegate;
import com.ufgov.zc.server.zc.service.IZcYearEndService;

public class ZcYearEndServiceDelegate implements IZcYearEndServiceDelegate {

	private IZcYearEndService zcYearEndService;

	public IZcYearEndService getZcYearEndService() {
		return zcYearEndService;
	}

	public void setZcYearEndService(IZcYearEndService zcYearEndService) {
		this.zcYearEndService = zcYearEndService;
	}

	public List getZcPProMake(ElementConditionDto dto, RequestMeta meta)
			throws Exception {
		// TCJLODO Auto-generated method stub
		return zcYearEndService.getZcPProMake(dto, meta);
	}

	public void updateProEndFn(String zcMakeCodes, boolean flag,	RequestMeta requestMeta) throws Exception {
		// TCJLODO Auto-generated method stub
		zcYearEndService.updateProEndFn(zcMakeCodes, flag, requestMeta);
	}

	public void updateYearEnd(String zcMakeCode, RequestMeta requestMeta)
			throws Exception {
		// TCJLODO Auto-generated method stub
		zcYearEndService.updateYearEnd(zcMakeCode, requestMeta);
	}

	/**
	 * 获取结转锁定的项目
	 */
	public List getZcCarraryMake(ElementConditionDto dto, RequestMeta meta)
			throws Exception {

		return zcYearEndService.getZcCarraryMake(dto, meta);
	}

	/**
	 * 设置项目状态
	 */
	public void jiezhuanFN(ElementConditionDto dto,
			String serverAdd, RequestMeta meta) throws Exception {
		zcYearEndService.jiezhuanFN(dto, serverAdd, meta);
	}


	public void carraryNewMakeByManual(ZcYearPlan yearPlan, String serverAdd,
			RequestMeta meta) throws Exception {
		zcYearEndService.carraryNewMakeByManual(yearPlan, serverAdd, meta);
	}

   
  public void jiexiangFn(ZcYearPlan zcYearPlan, RequestMeta requestMeta) throws Exception {
    // TCJLODO Auto-generated method stub
    zcYearEndService.jiexiangFn(zcYearPlan,requestMeta);
  }

   
  public void batchJiexiangFn(List  jieXiangLst, RequestMeta requestMeta) throws Exception {
    // TCJLODO Auto-generated method stub
    zcYearEndService.batchJiexiangFn(jieXiangLst,  requestMeta);
  }

   
  public ZcYearPlan getZcYearPlanByMakeCode(RequestMeta requestMeta, ZcYearPlan yearPlan) throws Exception {
    // TCJLODO Auto-generated method stub
    return zcYearEndService.getZcYearPlanByMakeCode(requestMeta, yearPlan);
  }

  
  public ZcYearPlan saveJieZhuanZiJinFN(ZcYearPlan yp, RequestMeta requestMeta) throws Exception {
    // TCJLODO Auto-generated method stub
    return zcYearEndService.saveJieZhuanZiJinFN(yp, requestMeta) ;
  }

}
