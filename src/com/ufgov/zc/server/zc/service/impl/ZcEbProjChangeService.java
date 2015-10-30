package com.ufgov.zc.server.zc.service.impl;import java.math.BigDecimal;import java.text.SimpleDateFormat;import java.util.Date;import java.util.List;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.constants.NumLimConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.model.AsWfDraft;import com.ufgov.zc.common.zc.model.ZcEbProjChange;import com.ufgov.zc.common.zc.model.ZcEbProjChangePack;import com.ufgov.zc.common.zc.model.ZcEbReqChange;import com.ufgov.zc.server.system.dao.IWorkflowDao;import com.ufgov.zc.server.system.util.NumLimUtil;import com.ufgov.zc.server.system.workflow.WFEngineAdapter;import com.ufgov.zc.server.zc.dao.IBaseDao;import com.ufgov.zc.server.zc.dao.ibatis.ZcEbProjChangeDao;import com.ufgov.zc.server.zc.service.IZcEbProjChangeService;public class ZcEbProjChangeService implements IZcEbProjChangeService {  private ZcEbProjChangeDao zcEbProjChangeDao;  private IBaseDao baseDao;  private IWorkflowDao workflowDao;  private WFEngineAdapter wfEngineAdapter;  public ZcEbProjChangeDao getZcEbProjChangeDao() {    return zcEbProjChangeDao;  }  public void setZcEbProjChangeDao(ZcEbProjChangeDao zcEbProjChangeDao) {    this.zcEbProjChangeDao = zcEbProjChangeDao;  }  public IBaseDao getBaseDao() {    return baseDao;  }  public void setBaseDao(IBaseDao baseDao) {    this.baseDao = baseDao;  }  public WFEngineAdapter getWfEngineAdapter() {    return wfEngineAdapter;  }  public void setWfEngineAdapter(WFEngineAdapter wfEngineAdapter) {    this.wfEngineAdapter = wfEngineAdapter;  }  public IWorkflowDao getWorkflowDao() {    return workflowDao;  }  public void setWorkflowDao(IWorkflowDao workflowDao) {    this.workflowDao = workflowDao;  }  public List getZcEbProjChangeList(ElementConditionDto dto, RequestMeta requestMeta) {    dto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(dto.getWfcompoId(), NumLimConstants.FWATCH));    return baseDao.query("ZcEbProjChange.getZcEbProjChangeList", dto);  }  public ZcEbProjChange saveZcEbProjChange(ZcEbProjChange proj, RequestMeta requestMeta) {    if (proj.getChgId() == null || "".equals(proj.getChgId()) || proj.getProjCode().equals("自动编号")) {      insertZcEbProjChange(requestMeta, proj);    } else {      updateZcEbProjChange(proj, requestMeta);    }    return getZcEbProjChange(proj.getChgId(), requestMeta);  }  public ZcEbProjChange saveLivingPurTypeChange(ZcEbProjChange zcEbProjChange, RequestMeta requestMeta) {    /**     * 如果数据库中存在 proj_code、pack_code、pur_type 和zcEbProjChange 一样的变更记录，先把原来的变更记录删除掉，在保存     */    ZcEbProjChange oldProjChange = (ZcEbProjChange) baseDao.read("ZcEbProjChange.getLivingProjChange", zcEbProjChange);    if (oldProjChange != null) {      baseDao.delete("ZcEbProjChange.deleteZcEbProjChangeById", oldProjChange.getChgId());      zcEbProjChange.setProjSrcCode(oldProjChange.getChgId());    } else {      zcEbProjChange.setProjSrcCode(zcEbProjChange.getProjCode());    }    baseDao.insert("ZcEbProjChange.insertZcEbProjChange", zcEbProjChange);    //修改分包的采购方式    baseDao.update("ZcEbProjChange.updateZcEbPackPurType", zcEbProjChange);    return getZcEbProjChange(zcEbProjChange.getChgId(), requestMeta);  }  public ZcEbProjChange insertZcEbProjChange(ZcEbProjChange zcEbProjChange, RequestMeta requestMeta) {    baseDao.insert("ZcEbProjChange.insertZcEbProjChange", zcEbProjChange);    List list = zcEbProjChange.getPackList();    for (int i = 0; list != null && i < list.size(); i++) {      baseDao.insert("ZcEbProjChange.insertZcEbProjChangePack", list.get(i));    }    return getZcEbProjChange(zcEbProjChange.getChgId(), requestMeta);  }  public void updateZcEbProjChange(ZcEbProjChange proj, RequestMeta requestMeta) {    this.baseDao.update("ZcEbProjChange.updateZcEbProjChange", proj);    List list = proj.getPackList();    baseDao.delete("ZcEbProjChange.deleteZcEbProjChangePackById", proj.getChgId());    for (int i = 0; list != null && i < list.size(); i++) {      baseDao.insert("ZcEbProjChange.insertZcEbProjChangePack", list.get(i));    }  }  private void insertZcEbProjChange(RequestMeta requestMeta, ZcEbProjChange proj) {    SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");    String userId = requestMeta.getSvUserID();    String compoId = requestMeta.getCompoId();    proj.setChgId(proj.getProjCode() + "-变更" + "[" + dateFormate.format(new Date()) + "]");    Long draftid = workflowDao.createDraftId();    proj.setProcessInstId(draftid);    baseDao.insert("ZcEbProjChange.insertZcEbProjChange", proj);    List list = proj.getPackList();    for (int i = 0; list != null && i < list.size(); i++) {      ZcEbProjChangePack zcEbProjChangePack = (ZcEbProjChangePack) list.get(i);      zcEbProjChangePack.setChgID(proj.getChgId());      baseDao.insert("ZcEbProjChange.insertZcEbProjChangePack", zcEbProjChangePack);    }    AsWfDraft asWfDraft = new AsWfDraft();    asWfDraft.setCompoId(compoId);    asWfDraft.setUserId(userId);    asWfDraft.setMasterTabId(compoId);    asWfDraft.setWfDraftId(BigDecimal.valueOf(draftid.longValue()));    workflowDao.insertAsWfdraft(asWfDraft);  }  public ZcEbProjChange getZcEbProjChange(String chgId, RequestMeta requestMeta) {    List zcEbProjChangePackList = baseDao.query("ZcEbProjChange.getZcEbProjChangePackByPackId", chgId);    ZcEbProjChange zcEbProjChange = (ZcEbProjChange) baseDao.read("ZcEbProjChange.getZcEbProjChangeById", chgId);    zcEbProjChange.setPackList(zcEbProjChangePackList);    return zcEbProjChange;  }  public void deleteZcEbProjChange(String chgId, RequestMeta requestMeta) {    baseDao.delete("ZcEbProjChange.deleteZcEbProjChangeById", chgId);    baseDao.delete("ZcEbProjChange.deleteZcEbProjChangePackById", chgId);  }  public ZcEbProjChange newCommitFN(ZcEbProjChange zcEbProjChange, RequestMeta requestMeta) {    //进行判断是否有草稿 ，如果没有草稿，就先创建草稿    if (zcEbProjChange.getProcessInstId() == null) {      String userId = requestMeta.getSvUserID();      String compoId = requestMeta.getCompoId();      Long draftid = workflowDao.createDraftId();      zcEbProjChange.setProcessInstId(draftid);      AsWfDraft asWfDraft = new AsWfDraft();      asWfDraft.setCompoId(compoId);      asWfDraft.setUserId(userId);      asWfDraft.setMasterTabId(compoId);      asWfDraft.setWfDraftId(BigDecimal.valueOf(draftid.longValue()));      workflowDao.insertAsWfdraft(asWfDraft);      updateZcEbProjChange(zcEbProjChange, requestMeta);    }    wfEngineAdapter.newCommit(zcEbProjChange.getComment(), zcEbProjChange, requestMeta);    return (ZcEbProjChange) baseDao.read("ZcEbProjChange.getZcEbProjChangeById", zcEbProjChange.getChgId());  }  public ZcEbProjChange auditFN(ZcEbProjChange zcEbProjChange, RequestMeta requestMeta) {    wfEngineAdapter.commit(zcEbProjChange.getComment(), zcEbProjChange, requestMeta);    return (ZcEbProjChange) baseDao.read("ZcEbProjChange.getZcEbProjChangeById", zcEbProjChange.getChgId());  }  public ZcEbProjChange unAuditFN(ZcEbProjChange zcEbProjChange, RequestMeta requestMeta) {    wfEngineAdapter.rework(zcEbProjChange.getComment(), zcEbProjChange, requestMeta);    return (ZcEbProjChange) baseDao.read("ZcEbProjChange.getZcEbProjChangeById", zcEbProjChange.getChgId());  }  public ZcEbProjChange untreadFN(ZcEbProjChange zcEbProjChange, RequestMeta requestMeta) {    wfEngineAdapter.untread(zcEbProjChange.getComment(), zcEbProjChange, requestMeta);    return (ZcEbProjChange) baseDao.read("ZcEbProjChange.getZcEbProjChangeById", zcEbProjChange.getChgId());  }  public ZcEbProjChange callbackFN(ZcEbProjChange zcEbProjChange, RequestMeta requestMeta) {    wfEngineAdapter.unAudit(zcEbProjChange.getComment(), zcEbProjChange, requestMeta);    return (ZcEbProjChange) baseDao.read("ZcEbProjChange.getZcEbProjChangeById", zcEbProjChange.getChgId());  }  public void updateInvalid(String chgId, RequestMeta requestMeta) {    baseDao.update("ZcEbProjChange.updateInvalid", chgId);  }  //以下为需求变更  public List getZcEbReqChangeList(ElementConditionDto dto, RequestMeta requestMeta) {    dto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(dto.getWfcompoId(), NumLimConstants.FWATCH));    return baseDao.query("ZcEbProjChange.getZcEbReqChangeList", dto);  }  public ZcEbReqChange saveZcEbReqChange(ZcEbReqChange zcEbReqChange, RequestMeta requestMeta) {    if (zcEbReqChange.getChangeCode() == null || "".equals(zcEbReqChange.getChangeCode()) || zcEbReqChange.getChangeCode().equals("自动编号")) {      insertZcEbReqChange(requestMeta, zcEbReqChange);    } else {      updateZcEbReqChange(zcEbReqChange, requestMeta);    }    return getZcEbReqChange(zcEbReqChange.getChangeCode(), requestMeta);  }  private void insertZcEbReqChange(RequestMeta requestMeta, ZcEbReqChange proj) {    SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");    String userId = requestMeta.getSvUserID();    String compoId = requestMeta.getCompoId();    proj.setChangeCode(proj.getSn() + "-变更" + "[" + dateFormate.format(new Date()) + "]");    Long draftid = workflowDao.createDraftId();    proj.setProcessInstId(draftid);    baseDao.insert("ZcEbProjChange.insertZcEbReqChange", proj);    AsWfDraft asWfDraft = new AsWfDraft();    asWfDraft.setCompoId(compoId);    asWfDraft.setUserId(userId);    asWfDraft.setMasterTabId(compoId);    asWfDraft.setWfDraftId(BigDecimal.valueOf(draftid.longValue()));    workflowDao.insertAsWfdraft(asWfDraft);  }  public void updateZcEbReqChange(ZcEbReqChange proj, RequestMeta requestMeta) {    this.baseDao.update("ZcEbProjChange.updateZcEbReqChange", proj);  }  public ZcEbReqChange getZcEbReqChange(String chgId, RequestMeta requestMeta) {    ZcEbReqChange zcEbProjChange = (ZcEbReqChange) baseDao.read("ZcEbProjChange.seleteZcEbReqChangeById", chgId);    return zcEbProjChange;  }  public void deleteZcEbReqChange(String chgId, RequestMeta requestMeta) {    ZcEbReqChange zcEbProjChange = (ZcEbReqChange) baseDao.read("ZcEbProjChange.seleteZcEbReqChangeById", chgId);    if (zcEbProjChange != null && zcEbProjChange.getZcChangeFileBlobid() != null) {      baseDao.delete("AsFile.deleteAsFileById", zcEbProjChange.getZcChangeFileBlobid());    }    baseDao.delete("ZcEbProjChange.deleteZcEbReqChangeById", chgId);  }}