/**    * @(#) project: ZC * @(#) file: ZcEbRfqOpenBidService.java *  * Copyright 2010 UFGOV, Inc. All rights reserved. * UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. *  */package com.ufgov.zc.server.zc.service.impl;import java.math.BigDecimal;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import com.kingdrive.workflow.context.WorkflowContext;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.constants.NumLimConstants;import com.ufgov.zc.common.system.constants.ZcSettingConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.model.AsFile;import com.ufgov.zc.common.system.model.AsWfDraft;import com.ufgov.zc.common.system.util.UUID;import com.ufgov.zc.common.zc.ZcEbBulletinConstants;import com.ufgov.zc.common.zc.model.ZcBulWModel;import com.ufgov.zc.common.zc.model.ZcEbBulletin;import com.ufgov.zc.common.zc.model.ZcEbBulletinWordMold;import com.ufgov.zc.common.zc.model.ZcEbEvalReport;import com.ufgov.zc.common.zc.model.ZcEbPackEvalResult;import com.ufgov.zc.common.zc.model.ZcEbPlan;import com.ufgov.zc.common.zc.model.ZcEbProj;import com.ufgov.zc.common.zc.model.ZcEbRfqPack;import com.ufgov.zc.common.zc.model.ZcHtModel;import com.ufgov.zc.common.zc.model.ZcReportModel;import com.ufgov.zc.common.zc.model.ZcTBchtItem;import com.ufgov.zc.common.zc.model.ZcXmcgHt;import com.ufgov.zc.common.zc.model.ZcXunJiaDetail;import com.ufgov.zc.common.zc.model.ZcXunJiaSummary;import com.ufgov.zc.server.system.dao.IWorkflowDao;import com.ufgov.zc.server.system.util.NumLimUtil;import com.ufgov.zc.server.system.util.NumUtil;import com.ufgov.zc.server.system.workflow.WFEngineAdapter;import com.ufgov.zc.server.zc.ZcSUtil;import com.ufgov.zc.server.zc.dao.ibatis.BaseDao;import com.ufgov.zc.server.zc.service.IZcEbRfqService;/** *  * @ClassName: ZcEbRfqOpenBidService *  * @Description: TODO(这里用一句话描述这个类的作用) *  * @date: 2010-9-10 下午04:07:22 *  * @version: V1.0 *  * @since: 1.0 *  * @author: fanpeile *  * @modify: */public class ZcEbRfqService implements IZcEbRfqService {  private BaseDao baseDao;  private WFEngineAdapter wfEngineAdapter;  private IWorkflowDao workflowDao;  public BaseDao getBaseDao() {    return baseDao;  }  public void setBaseDao(BaseDao baseDao) {    this.baseDao = baseDao;  }  public WFEngineAdapter getWfEngineAdapter() {    return wfEngineAdapter;  }  public void setWfEngineAdapter(WFEngineAdapter wfEngineAdapter) {    this.wfEngineAdapter = wfEngineAdapter;  }  public IWorkflowDao getWorkflowDao() {    return workflowDao;  }  public void setWorkflowDao(IWorkflowDao workflowDao) {    this.workflowDao = workflowDao;  }  public List getZcEbRfqProjList(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    elementConditionDto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(elementConditionDto.getWfcompoId(), NumLimConstants.FWATCH));    return baseDao.query("ZcEbRfq.getZcEbProjList", elementConditionDto);  }  public ZcEbRfqPack saveZcEbRfqPack(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    String creator = requestMeta.getSvUserName();    String userId = requestMeta.getSvUserID();    String compoId = requestMeta.getCompoId();    int nd = requestMeta.getSvNd();    boolean isDraft = false;    if (zcEbRfqPack.getProcessInstId() == null || zcEbRfqPack.getProcessInstId().longValue() == -1) {      Long draftid = workflowDao.createDraftId();      zcEbRfqPack.setProcessInstId(draftid);      isDraft = true;    }    if ("".equals(ZcSUtil.safeString(zcEbRfqPack.getRfqCode())) || zcEbRfqPack.getRfqCode().equals("自动编号")) {      String code =(String)baseDao.read(ZcSettingConstants.SEQUENCE_XUNJIA_OPEN_BID_RFQ, null);      zcEbRfqPack.setRfqCode(code);      zcEbRfqPack.setExecuteDate(requestMeta.getSysDate());      zcEbRfqPack.setCreator(creator);      zcEbRfqPack.setNd(new Integer(nd));      baseDao.insert("ZcEbRfq.insertZcEbRfqPack", zcEbRfqPack);      updatePakeStatus(zcEbRfqPack);    } else {      updateZcEbRfqPack(zcEbRfqPack, requestMeta);    }    if (isDraft) {      AsWfDraft asWfDraft = new AsWfDraft();      asWfDraft.setCompoId(compoId);      asWfDraft.setWfDraftName(zcEbRfqPack.getRfqCode());      asWfDraft.setUserId(userId);      asWfDraft.setMasterTabId(compoId);      asWfDraft.setWfDraftId(BigDecimal.valueOf(zcEbRfqPack.getProcessInstId().longValue()));      workflowDao.insertAsWfdraft(asWfDraft);    }    // //需要送审的状态，启动工作流    // if    // (ZcSettingConstants.XUNJIA_STATUS_OPEN_BID_AUDIT_CONSTRAINT.equals(zcEbRfqPack.getOpenBidStatus())    // && zcEbRfqPack.getProcessInstId().compareTo(0L) < 0) {    // this.newCommit(zcEbRfqPack, requestMeta);    // }    //    // //领导审核    // if    // ((ZcSettingConstants.XUNJIA_STATUS_OPEN_BID_SUCCESS.equals(zcEbRfqPack.getOpenBidStatus())    // || ZcSettingConstants.XUNJIA_STATUS_OPEN_BID_CRAP    // .equals(zcEbRfqPack.getOpenBidStatus())) &&    // zcEbRfqPack.getProcessInstId().compareTo(0L) > 0) {    // this.audit(zcEbRfqPack, requestMeta);    // }    return (ZcEbRfqPack) baseDao.read("ZcEbRfq.getZcEbRfqPack1", zcEbRfqPack);  }  public List getRfqSinupPack(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    return baseDao.query("ZcEbRfq.getRfqSinupPack", elementConditionDto);  }  public ZcEbRfqPack audit(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    wfEngineAdapter.commit(zcEbRfqPack.getComment(), zcEbRfqPack, requestMeta);    return zcEbRfqPack;  }  public ZcEbRfqPack callbackFN(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    wfEngineAdapter.callback(zcEbRfqPack.getComment(), zcEbRfqPack, requestMeta);    return zcEbRfqPack;  }  public ZcEbRfqPack newCommit(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    wfEngineAdapter.newCommit(zcEbRfqPack.getComment(), zcEbRfqPack, requestMeta);    return zcEbRfqPack;  }  public ZcEbRfqPack unaudit(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    wfEngineAdapter.unAudit(zcEbRfqPack.getComment(), zcEbRfqPack, requestMeta);    return zcEbRfqPack;  }  public ZcEbRfqPack untread(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    wfEngineAdapter.untread(zcEbRfqPack.getComment(), zcEbRfqPack, requestMeta);    return zcEbRfqPack;  }  public ZcEbRfqPack getZcEbRfqPack(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    return (ZcEbRfqPack) baseDao.read("ZcEbRfq.getZcEbRfqPack", elementConditionDto);  }  public int deleteZcEbRfqPack(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    ElementConditionDto dto=new ElementConditionDto();        dto.setZcText0(zcEbRfqPack.getRfqCode());        int rtn = baseDao.delete("ZcEbRfq.deleteZcEbRfqPack", dto);    return rtn;  }  public List getZcEbRfqPackList(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    elementConditionDto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(elementConditionDto.getWfcompoId(), NumLimConstants.FWATCH));    return baseDao.query("ZcEbRfq.getZcEbRfqPackList", elementConditionDto);  }  public ZcEbRfqPack updateZcEbRfqPack(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    baseDao.update("ZcEbRfq.updateZcEbRfqPack", zcEbRfqPack);    updatePakeStatus(zcEbRfqPack);    return zcEbRfqPack;  }  private void updatePakeStatus(ZcEbRfqPack zcEbRfqPack) {    if (ZcSettingConstants.XUNJIA_STATUS_OPEN_BID_SUCCESS.equals(zcEbRfqPack.getOpenBidStatus())      || ZcSettingConstants.XUNJIA_STATUS_OPEN_BID_CRAP.equals(zcEbRfqPack.getOpenBidStatus())) {// 成功开标，废标都需要修改分包表状态      baseDao.update("ZcEbRfq.updateZcEbPackStatus", zcEbRfqPack);    }  }  public ZcEbRfqPack updateZcEbRfqPackStatus(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    baseDao.update("ZcEbRfq.updateZcEbRfqPack", zcEbRfqPack);    return zcEbRfqPack;  }  public List getXunjiaDetaiList(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    return baseDao.query("ZcEbRfq.getXunjiaDetaiList", elementConditionDto);  }  public ZcXunJiaDetail getLowPriceXunJiaDetail(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    return (ZcXunJiaDetail) baseDao.read("ZcEbRfq.getLowPriceXunJiaDetail", elementConditionDto);  }  public ZcEbRfqPack updateZcEbRfqPackForDelay(ZcEbRfqPack rp, RequestMeta requestMeta) {    if ("".equals(ZcSUtil.safeString(rp.getRfqCode())) || rp.getRfqCode().equals("自动编号")) {      saveZcEbRfqPack(rp, requestMeta);    } else {      baseDao.update("ZcEbRfq.updateZcEbRfqPack", rp);    }    ElementConditionDto dto = new ElementConditionDto();    dto.setProjCode(rp.getProjCode());    ZcEbPlan plan = (ZcEbPlan) baseDao.read("ZcEbPlan.getZcEbPlan", dto);    plan.setOpenBidTime(rp.getBidEndTime());    plan.setBidEndTime(rp.getBidEndTime());    baseDao.update("ZcEbPlan.updateZcEbPlan", plan);    return rp;  }  public List getZcEbRfqProjListWithPackes(ElementConditionDto dto, RequestMeta requestMeta) {    List list = this.getZcEbRfqProjList(dto, requestMeta);    for (int i = 0; i < list.size(); i++) {      ZcEbProj proj = (ZcEbProj) list.get(i);      if (proj == null) {        continue;      }      List packList = this.baseDao.query("ZcEbProj.getZcEbPackListByProjCode", proj.getProjCode());      proj.setPackList(packList);    }    return list;  }  public ZcReportModel findEvalModel(String projCode, String packCode, RequestMeta requestMeta) {    // TODO Auto-generated method stub    Map param = new HashMap();    param.put("proj_code", projCode);    param.put("pack_code", packCode);    ZcReportModel model = (ZcReportModel) baseDao.read("ZcEbRfq.findEvalById", param);    if (model == null) {      return null;    }    ElementConditionDto dto = new ElementConditionDto();    if (model.getZcSuName() == null || "".equals(model.getZcSuName())) {      dto.setZcText0(ZcSettingConstants.ZC_JB_EVAL_CANCEL_MODEL_CODE);    } else {      dto.setZcText0(ZcSettingConstants.ZC_JB_EVAL_MODEL_CODE);    }    List list = baseDao.query("ZcEbBulletinWordMold.getZcEbWordMoldByType", dto);    if (list != null && list.size() > 0) {      String fileId = ((ZcEbBulletinWordMold) list.get(0)).getFileID();      if (fileId != null && fileId.length() > 0) {        model.setFile((AsFile) baseDao.read("ZC_XMCG_HT.getContractTemplateContent", fileId));      }    }    return model;  }  public void createEval(String projCode, String packCode, String fileId, List xjSummaryList, RequestMeta requestMeta) {    // TODO Auto-generated method stub    Map param = new HashMap();    param.put("proj_code", projCode);    param.put("pack_code", packCode);    ZcEbEvalReport bean = (ZcEbEvalReport) baseDao.read("ZcEbRfq.findEvalInfoById", param);    bean.setReportCode(UUID.randomUUID().toString());    bean.setStatus("0");    bean.setExecutor(requestMeta.getSvUserName());    bean.setExecuteDate(requestMeta.getSysDate());    bean.setNd(new Integer(requestMeta.getSvNd()));    bean.setReportAttachBlobid(fileId);    baseDao.insert("ZcEbEval.insertZcEbEvalReport", bean);    if (xjSummaryList != null) {      for (int i = 0; i < xjSummaryList.size(); i++) {        ZcXunJiaSummary xjs = (ZcXunJiaSummary) xjSummaryList.get(i);        ZcEbPackEvalResult rt = new ZcEbPackEvalResult();        rt.setResultCode(String.valueOf(UUID.randomUUID()));        rt.setProjCode(projCode);        rt.setPackCode(packCode);        rt.setProviderCode(xjs.getProviderCode());        rt.setProviderName(xjs.getProviderName());        rt.setProviderTotalPrice(xjs.getTotalPrice());        rt.setIsComplianceResult("Y");        if ("Y".equals(xjs.getIsClosedDeal())) {          rt.setEvalResult("4");        } else {          rt.setEvalResult("2");        }        baseDao.insert("ZcEbEval.insertZcEbPackEvalFinalSumResult", rt);      }    }  }  public ZcBulWModel findBulWModel(String projCode, String packCode, RequestMeta requestMeta) {    // TODO Auto-generated method stub    Map param = new HashMap();    param.put("proj_code", projCode);    param.put("pack_code", packCode);    param.put("bulletin_type", ZcEbBulletin.ZHAOBIAO_XJ);    ZcBulWModel model = (ZcBulWModel) baseDao.read("ZcEbRfq.findBulWById", param);    if (model == null || model.getZcSuName() == null || "".equals(model.getZcSuName())) {      return null;    }    ElementConditionDto dto = new ElementConditionDto();    dto.setZcText0(ZcSettingConstants.ZC_JB_BUL_W_MODEL_CODE);    List list = baseDao.query("ZcEbBulletinWordMold.getZcEbWordMoldByType", dto);    if (list != null && list.size() > 0) {      ZcEbBulletinWordMold bm = (ZcEbBulletinWordMold) list.get(0);      String fileId = bm.getFileID();      if (fileId != null && fileId.length() > 0) {        model.setFile((AsFile) baseDao.read("ZC_XMCG_HT.getContractTemplateContent", fileId));        model.setModCode(bm.getBulletinMoldCode());        model.setModName(bm.getBulletinMoldName());      }    }    return model;  }  public void createBulW(Map map, String fileId, RequestMeta requestMeta) {    // TODO Auto-generated method stub    ZcEbBulletin bean = new ZcEbBulletin();    Long draftid = workflowDao.createDraftId();    bean.setProcessInstId(draftid);    bean.setProjCode(map.get("projCode").toString());    String id = NumUtil.getInstance().getNo(ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_WID, "BULLETIN_ID", bean);    bean.setBulletinID(id);    AsWfDraft asWfDraft = new AsWfDraft();    asWfDraft.setCompoId(ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_WID);    asWfDraft.setWfDraftName(bean.getTitleField());// zcEbBulletin.getTitleField());    asWfDraft.setUserId(requestMeta.getSvUserID());    asWfDraft.setMasterTabId(ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_WID);    asWfDraft.setWfDraftId(BigDecimal.valueOf(draftid.longValue()));    workflowDao.insertAsWfdraft(asWfDraft);    bean.setBulletinType(ZcEbBulletinConstants.TYPE_BULLETIN_WID);    bean.setBulletinStatus("0");    bean.setExecuteDate(requestMeta.getSysDate());    bean.setExecutor(requestMeta.getEmpName());    bean.setNd(new Integer(requestMeta.getSvNd()));    bean.setFileID(fileId);    bean.setIsExported(0);    bean.setOrgCode(requestMeta.getSvOrgCode());    bean.setAgency("900");    bean.setProjectCode(map.get("packCode") == null ? null : map.get("packCode").toString());    bean.setProjName(map.get("projName") == null ? null : map.get("projName").toString());    bean.setCoCode(map.get("coCode") == null ? null : map.get("coCode").toString());    bean.setMoldCode(map.get("modCode") == null ? null : map.get("modCode").toString());    bean.setMoldName(map.get("modName") == null ? null : map.get("modName").toString());    baseDao.insert("ZcEbBulletin.insert", bean);    WorkflowContext w = wfEngineAdapter.genCommonWFC(bean.getComment(), bean, ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_WID, requestMeta);    wfEngineAdapter.newCommit(w, bean);  }  public ZcHtModel findHtModel(String projCode, String packCode, RequestMeta requestMeta) {    // TODO Auto-generated method stub    Map param = new HashMap();    param.put("proj_code", projCode);    param.put("pack_code", packCode);    ZcHtModel model = (ZcHtModel) baseDao.read("ZcEbRfq.findHtById", param);    if (model == null || model.getZcSuName() == null || "".equals(model.getZcSuName())) {      return null;    }    ElementConditionDto dto = new ElementConditionDto();    dto.setZcText0(ZcSettingConstants.ZC_JB_HT_MODEL_CODE);    List list = baseDao.query("ZcEbBulletinWordMold.getZcEbWordMoldByType", dto);    if (list != null && list.size() > 0) {      String fileId = ((ZcEbBulletinWordMold) list.get(0)).getFileID();      if (fileId != null && fileId.length() > 0) {        model.setFile((AsFile) baseDao.read("ZC_XMCG_HT.getContractTemplateContent", fileId));        ZcXmcgHt ht = new ZcXmcgHt();        ht.setZcMakeCode(projCode);        ht.setZcBidCode(packCode);        String code = NumUtil.getInstance().getNo("ZC_XMCG_HT", "ZC_HT_CODE", ht);        model.setZcHtCode(code);      }    }    return model;  }  public void createHt(ZcXmcgHt ht, RequestMeta requestMeta) {    // TODO Auto-generated method stub    Long draftid = workflowDao.createDraftId();    ht.setProcessInstId(draftid);    AsWfDraft asWfDraft = new AsWfDraft();    asWfDraft.setCompoId("ZC_XMCG_HT");    asWfDraft.setWfDraftName(ht.getTitleField());    asWfDraft.setUserId(requestMeta.getSvUserID());    asWfDraft.setMasterTabId("ZC_XMCG_HT");    asWfDraft.setWfDraftId(BigDecimal.valueOf(draftid.longValue()));    workflowDao.insertAsWfdraft(asWfDraft);    baseDao.insert("ZC_XMCG_HT.ibatorgenerated_insert", ht);    Map map = new HashMap();    map.put("purType", ZcSettingConstants.PITEM_OPIWAY_XJ);    map.put("packCode", ht.getZcBidCode());    List its = baseDao.query("ZC_T_BCHT_ITEM.selectItemForZxHt", map);    for (int i = 0; i < its.size(); i++) {      ZcTBchtItem it = (ZcTBchtItem) its.get(i);      it.setZcHtCode(ht.getZcHtCode());      it.setZcCtgryId(new BigDecimal(i));      baseDao.insert("ZC_T_BCHT_ITEM.ibatorgenerated_insert", its.get(i));    }    WorkflowContext w = wfEngineAdapter.genCommonWFC(ht.getComment(), ht, "ZC_XMCG_HT", requestMeta);    wfEngineAdapter.newCommit(w, ht);  }    public List queryExportsDatas(List reportIdLst, RequestMeta meta) {    // TODO Auto-generated method stub    List rtn=new ArrayList();    for(int i=0;i<reportIdLst.size();i++){      HashMap h= (HashMap) reportIdLst.get(i);      ElementConditionDto dto=new ElementConditionDto();      dto.setPackCode((String) h.get("PACK_CODE"));      dto.setProjCode((String) h.get("PROJ_CODE"));      Object obj=baseDao.read("ZcEbRfq.getZcEbRfqPack", dto);      if(obj!=null){        rtn.add(obj);      }    }    return rtn;  }     public String importRfqDataFN(ZcEbRfqPack bill, RequestMeta meta) {    // TODO Auto-generated method stub    deleteZcEbRfqPack(bill, meta);    baseDao.insert("ZcEbRfq.insertZcEbRfqPack", bill);    return "成功导入询价开标数据";  }  /**   * 开标成功以后的废标，这里的废标，用于生产评标报告以后，有可能已经发了中标公告、通知书等，所以要将这些都删除，如果已经有了合同，则提示先作废合同，再进行作废   * @param zcEbRfqPack   * @param requestMeta   */  public void crapBidFN(ZcEbRfqPack zcEbRfqPack, RequestMeta requestMeta) {    // TODO Auto-generated method stub    //删除中标通知书    baseDao.delete("ZcEbNotice.deleteByPackCode", zcEbRfqPack.getPackCode());    //删除中标公告,暂不实现，因为公告是以项目为单位发布的，不是以分包发布的    //baseDao.delete("", zcEbRfqPack.getPackCode());    //删除评标 报告    baseDao.delete("ZcEbEval.deleteZcEbEvalReportByPackCode", zcEbRfqPack.getPackCode());    //更新开标结果        saveZcEbRfqPack(zcEbRfqPack, requestMeta);  }}