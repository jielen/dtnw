package com.ufgov.zc.server.zc.service.impl;

import java.util.List;
import java.util.Map;

import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.NumLimConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcEbJieXiang;
import com.ufgov.zc.server.system.dao.ibatis.WorkflowDao;
import com.ufgov.zc.server.system.util.NumLimUtil;
import com.ufgov.zc.server.system.workflow.WFEngineAdapter;
import com.ufgov.zc.server.zc.dao.IBaseDao;
import com.ufgov.zc.server.zc.service.IZcEbJieXiangService;

public class ZcEbJieXiangService implements IZcEbJieXiangService {

  private IBaseDao baseDao;

  private WorkflowDao workflowDao;

  private WFEngineAdapter wfEngineAdapter;
  
  public IBaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(IBaseDao baseDao) {
    this.baseDao = baseDao;
  }

  public WorkflowDao getWorkflowDao() {
    return workflowDao;
  }

  public void setWorkflowDao(WorkflowDao workflowDao) {
    this.workflowDao = workflowDao;
  }

  public WFEngineAdapter getWfEngineAdapter() {
    return wfEngineAdapter;
  }

  public void setWfEngineAdapter(WFEngineAdapter wfEngineAdapter) {
    this.wfEngineAdapter = wfEngineAdapter;
  }

   
  public List getList(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {
    // TODO Auto-generated method stub

    elementConditionDto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(elementConditionDto.getWfcompoId(), NumLimConstants.FWATCH));

    return baseDao.query("ZcEbAuditSheet.listJieXiang", elementConditionDto);
  }

   
  public ZcEbJieXiang readJieXiang(Map para, RequestMeta requestMeta) {
    // TODO Auto-generated method stub
     return (ZcEbJieXiang)baseDao.read("ZcEbAuditSheet.readJieXiang", para);
     
  }

   
  public void cancelCaiGou(ZcEbJieXiang jiexiang, RequestMeta requestMeta) {
    // TODO Auto-generated method stub
    baseDao.insert("ZcEbEntrustCancel.insertZcEbEntrustCancel", jiexiang.getEntrustCancel());
    baseDao.update("ZcEbAuditSheet.update", jiexiang);
  }

   
  public void unFinishCaiGou(ZcEbJieXiang jiexiang, RequestMeta requestMeta) {
    // TODO Auto-generated method stub
    baseDao.update("ZcEbAuditSheet.update", jiexiang);    
  }

   
  public void finishCaiGou(ZcEbJieXiang jiexiang, RequestMeta requestMeta) {
    // TODO Auto-generated method stub
    baseDao.update("ZcEbAuditSheet.update", jiexiang);    
  }

}
