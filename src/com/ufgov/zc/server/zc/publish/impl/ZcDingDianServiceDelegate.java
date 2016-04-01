package com.ufgov.zc.server.zc.publish.impl;

import java.util.List;

import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.DataExchangeException;
import com.ufgov.zc.common.zc.model.ZcDingdian;
import com.ufgov.zc.common.zc.publish.IZcDingDianServiceDelegate;
import com.ufgov.zc.server.zc.service.IZcDingDianService;

public class ZcDingDianServiceDelegate implements IZcDingDianServiceDelegate {

  private IZcDingDianService dingDianService;
  
  public List getMainLst(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {
    return dingDianService.getMainLst(elementConditionDto, requestMeta);
  }

  
  public void cancelFn(ZcDingdian bill, RequestMeta requestMeta) {
    dingDianService.cancelFn(bill, requestMeta);
  }

  
  public ZcDingdian unAuditFN(ZcDingdian bill, RequestMeta requestMeta) {
    return dingDianService.unAuditFN(bill, requestMeta);
  }

  
  public ZcDingdian untreadFN(ZcDingdian bill, RequestMeta requestMeta) {
    return dingDianService.untreadFN(bill, requestMeta);
  }

  
  public ZcDingdian auditFN(ZcDingdian bill, RequestMeta requestMeta) throws Exception {
    return dingDianService.auditFN(bill, requestMeta);
  }

  
  public ZcDingdian updateFN(ZcDingdian bill, RequestMeta requestMeta) throws Exception {
    return dingDianService.updateFN(bill, requestMeta);
  }

  
  public void commitFN(List beanList, RequestMeta requestMeta) {
    dingDianService.commitFN(beanList, requestMeta);
  }

  
  public void deleteListFN(List beanList, RequestMeta requestMeta) {
    dingDianService.deleteListFN(beanList, requestMeta);
  }

  
  public void deleteFN(ZcDingdian bill, RequestMeta requestMeta) {
    dingDianService.deleteFN(bill, requestMeta);
  }

  
  public ZcDingdian selectByPrimaryKey(String ddCode, RequestMeta requestMeta) {
    return dingDianService.selectByPrimaryKey(ddCode, requestMeta);
  }

  
  public ZcDingdian callbackFN(ZcDingdian bill, RequestMeta requestMeta) {
    return dingDianService.callbackFN(bill, requestMeta);
  }

  
  public void deleteByPrimaryKeyFN(String ddCode, RequestMeta requestMeta) {
    dingDianService.deleteByPrimaryKeyFN(ddCode, requestMeta);
  }

  
  public ZcDingdian newCommitFN(ZcDingdian bill, RequestMeta requestMeta) {
    return dingDianService.newCommitFN(bill, requestMeta);
  }

  
  public ZcDingdian sendPayFN(ZcDingdian bill, RequestMeta requestMeta) throws Exception {
    return dingDianService.sendPayFN(bill, requestMeta);
  }

  
  public List queryExportsDatas(ElementConditionDto dto, RequestMeta meta) {
    return dingDianService.queryExportsDatas(dto, meta);
  }

  
  public String importTransDatasFN(ZcDingdian bill, RequestMeta meta) throws DataExchangeException {
    return dingDianService.importTransDatasFN(bill, meta);
  }

  
  public String importDelDataFN(String id, RequestMeta meta) throws DataExchangeException {
    return dingDianService.importDelDataFN(id, meta);
  }


  public IZcDingDianService getDingDianService() {
    return dingDianService;
  }


  public void setDingDianService(IZcDingDianService dingDianService) {
    this.dingDianService = dingDianService;
  }

}
