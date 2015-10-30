/**   * @(#) project: ZC* @(#) file: ZcEbRfqOpenBidServiceDelegate.java* * Copyright 2010 UFGOV, Inc. All rights reserved.* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.* */package com.ufgov.zc.server.zc.publish.impl;import java.util.List;import java.util.Map;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.model.ZcBulWModel;import com.ufgov.zc.common.zc.model.ZcEbRfqPack;import com.ufgov.zc.common.zc.model.ZcHtModel;import com.ufgov.zc.common.zc.model.ZcReportModel;import com.ufgov.zc.common.zc.model.ZcXmcgHt;import com.ufgov.zc.common.zc.model.ZcXunJiaDetail;import com.ufgov.zc.common.zc.model.ZcXunJiaSummary;import com.ufgov.zc.common.zc.publish.IZcEbRfqServiceDelegate;import com.ufgov.zc.server.zc.service.IZcEbPlanService;import com.ufgov.zc.server.zc.service.IZcEbRfqService;/*** @ClassName: ZcEbRfqOpenBidServiceDelegate* @Description: TODO(这里用一句话描述这个类的作用)* @date: 2010-9-10 下午04:04:38* @version: V1.0 * @since: 1.0* @author: fanpeile* @modify: */public class ZcEbRfqServiceDelegate implements IZcEbRfqServiceDelegate {  private IZcEbRfqService zcEbRfqService;  private IZcEbPlanService zcEbPlanService;  public IZcEbPlanService getZcEbPlanService() {    return zcEbPlanService;  }  public void setZcEbPlanService(IZcEbPlanService zcEbPlanService) {    this.zcEbPlanService = zcEbPlanService;  }  public IZcEbRfqService getZcEbRfqService() {    return zcEbRfqService;  }  public void setZcEbRfqService(IZcEbRfqService zcEbRfqService) {    this.zcEbRfqService = zcEbRfqService;  }  public List getZcEbRfqProj(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    return zcEbRfqService.getZcEbRfqProjList(elementConditionDto, requestMeta);  }  public ZcEbRfqPack saveZcEbRfqPackFN(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    return zcEbRfqService.saveZcEbRfqPack(zcEbRfqPack, requestMeta);  }  public ZcEbRfqPack getZcEbRfqPack(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    return zcEbRfqService.getZcEbRfqPack(elementConditionDto, requestMeta);  }  public ZcEbRfqPack auditFN(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    return zcEbRfqService.audit(zcEbRfqPack, requestMeta);  }  public ZcEbRfqPack callbackFN(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    return zcEbRfqService.callbackFN(zcEbRfqPack, requestMeta);  }  public void deleteZcEbRfqPackFN(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    zcEbRfqService.deleteZcEbRfqPack(zcEbRfqPack, requestMeta);  }  public List getZcEbRfqPackList(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    return zcEbRfqService.getZcEbRfqPackList(elementConditionDto, requestMeta);  }  public ZcEbRfqPack newCommitFN(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    return zcEbRfqService.newCommit(zcEbRfqPack, requestMeta);  }  public ZcEbRfqPack unAuditFN(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    return zcEbRfqService.unaudit(zcEbRfqPack, requestMeta);  }  public ZcEbRfqPack untreadFN(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    return zcEbRfqService.untread(zcEbRfqPack, requestMeta);  }  public ZcEbRfqPack updateZcEbRfqPackFN(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    return zcEbRfqService.updateZcEbRfqPack(zcEbRfqPack, requestMeta);  }  public ZcEbRfqPack updateZcEbRfqPackStatusFN(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    return this.zcEbRfqService.updateZcEbRfqPackStatus(zcEbRfqPack, requestMeta);  }  public List getRfqSinupPack(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    return zcEbRfqService.getRfqSinupPack(elementConditionDto, requestMeta);  }  public List getXunjiaDetaiList(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    return zcEbRfqService.getXunjiaDetaiList(elementConditionDto, requestMeta);  }  public ZcXunJiaDetail getLowPriceXunJiaDetail(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    return zcEbRfqService.getLowPriceXunJiaDetail(elementConditionDto, requestMeta);  }  public ZcEbRfqPack updateZcEbRfqPackForDelay(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    return zcEbRfqService.updateZcEbRfqPackForDelay(zcEbRfqPack, requestMeta);  }  public List getZcEbRfqProjWithPackes(ElementConditionDto dto, RequestMeta requestMeta) {    return zcEbRfqService.getZcEbRfqProjListWithPackes(dto, requestMeta);  }  public ZcReportModel findEvalModel(String projCode, String packCode, RequestMeta requestMeta) {    // TODO Auto-generated method stub    return zcEbRfqService.findEvalModel(projCode, packCode, requestMeta);  }  public void createEval(String projCode, String packCode, String fileId, List xjSummaryList, RequestMeta requestMeta) {    // TODO Auto-generated method stub    zcEbRfqService.createEval(projCode, packCode, fileId, xjSummaryList, requestMeta);  }  public ZcBulWModel findBulWModel(String projCode, String packCode, RequestMeta requestMeta) {    // TODO Auto-generated method stub    return zcEbRfqService.findBulWModel(projCode, packCode, requestMeta);  }  public void createBulW(Map map, String fileId, RequestMeta requestMeta) {    // TODO Auto-generated method stub    zcEbRfqService.createBulW(map, fileId, requestMeta);  }  public ZcHtModel findHtModel(String projCode, String packCode, RequestMeta requestMeta) {    // TODO Auto-generated method stub    return zcEbRfqService.findHtModel(projCode, packCode, requestMeta);  }  public void createHt(ZcXmcgHt ht, RequestMeta requestMeta) {    // TODO Auto-generated method stub    zcEbRfqService.createHt(ht, requestMeta);  }     public List queryExportsDatas(List reportIdLst, RequestMeta meta) {    // TODO Auto-generated method stub    return zcEbRfqService.queryExportsDatas(reportIdLst, meta) ;  }     public String importRfqDataFN(ZcEbRfqPack bill, RequestMeta meta) {    // TODO Auto-generated method stub    return zcEbRfqService.importRfqDataFN(bill, meta);  }   public void crapBidFN(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    // TODO Auto-generated method stub    zcEbRfqService.crapBidFN(zcEbRfqPack, requestMeta);  }}