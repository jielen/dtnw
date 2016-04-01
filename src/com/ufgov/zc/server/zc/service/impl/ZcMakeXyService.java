package com.ufgov.zc.server.zc.service.impl;

import java.util.List;

import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.server.system.dao.IWorkflowDao;
import com.ufgov.zc.server.system.workflow.WFEngineAdapter;
import com.ufgov.zc.server.zc.dao.IZcEbBaseServiceDao;
import com.ufgov.zc.server.zc.service.IZcMakeXyService;

public class ZcMakeXyService implements IZcMakeXyService {

  private IWorkflowDao workflowDao;
  private WFEngineAdapter wfEngineAdapter;
  private IZcEbBaseServiceDao zcEbBaseServiceDao;
  
  public List getMainDataLst(ElementConditionDto elementConditionDto, RequestMeta meta) {
    return null;
  }

  
  public ZcPProMake selectByPrimaryKey(String id, RequestMeta meta) {
    return null;
  }

  
  public ZcPProMake saveFN(ZcPProMake bill, RequestMeta meta) {
    return null;
  }

  
  public void deleteByPrimaryKeyFN(String id, RequestMeta meta) {
  }

  
  public ZcPProMake unAuditFN(ZcPProMake bill, RequestMeta meta) {
    wfEngineAdapter.rework(bill.getComment(), bill, meta);
    return bill;
  }

  
  public WFEngineAdapter getWfEngineAdapter() {
    return wfEngineAdapter;
  }


  public void setWfEngineAdapter(WFEngineAdapter wfEngineAdapter) {
    this.wfEngineAdapter = wfEngineAdapter;
  }


  public ZcPProMake untreadFN(ZcPProMake bill, RequestMeta meta) {
    wfEngineAdapter.untread(bill.getComment(), bill, meta);
    return bill;
  }

  
  public ZcPProMake auditFN(ZcPProMake bill, RequestMeta meta) throws Exception {
    // TCJLODO Auto-generated method stub
    bill=saveFN(bill, meta);
    wfEngineAdapter.commit(bill.getComment(), bill, meta);
    return selectByPrimaryKey(bill.getZcMakeCode(),meta);
  }

  
  public ZcPProMake newCommitFN(ZcPProMake bill, RequestMeta meta) {
    wfEngineAdapter.newCommit(bill.getComment(), bill, meta);
    return selectByPrimaryKey(bill.getZcMakeCode(),meta);
  }

  
  public ZcPProMake callbackFN(ZcPProMake bill, RequestMeta meta) {
    wfEngineAdapter.callback(bill.getComment(), bill, meta);
    return selectByPrimaryKey(bill.getZcMakeCode(),meta);
  }


  public IZcEbBaseServiceDao getZcEbBaseServiceDao() {
    return zcEbBaseServiceDao;
  }


  public void setZcEbBaseServiceDao(IZcEbBaseServiceDao zcEbBaseServiceDao) {
    this.zcEbBaseServiceDao = zcEbBaseServiceDao;
  }


  public IWorkflowDao getWorkflowDao() {
    return workflowDao;
  }


  public void setWorkflowDao(IWorkflowDao workflowDao) {
    this.workflowDao = workflowDao;
  }

}
