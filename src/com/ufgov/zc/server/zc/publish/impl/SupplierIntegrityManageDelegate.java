package com.ufgov.zc.server.zc.publish.impl;import java.util.List;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.model.SupplierIntegrityManage;import com.ufgov.zc.common.zc.publish.ISupplierIntegrityManageDelegate;import com.ufgov.zc.server.zc.service.ISupplierIntegrityManageService;public class SupplierIntegrityManageDelegate implements ISupplierIntegrityManageDelegate {  private ISupplierIntegrityManageService integrityManageService;  public ISupplierIntegrityManageService getIntegrityManageService() {    return integrityManageService;  }  public void setIntegrityManageService(  ISupplierIntegrityManageService integrityManageService) {    this.integrityManageService = integrityManageService;  }  public void delSupplierIntegrityManageList(  SupplierIntegrityManage supplierIntegrityManage, RequestMeta requestMeta) {    integrityManageService.delSupplierIntegrityManageList(supplierIntegrityManage);  }  public List getSupplierIntegrityManageList(  ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    return integrityManageService.getSupplierIntegrityManageList(elementConditionDto, requestMeta);  }  public SupplierIntegrityManage saveSupplierIntegrityManage(  SupplierIntegrityManage supplierIntegrityManage,  RequestMeta requestMeta) {    return integrityManageService.saveSupplierIntegrityManage(supplierIntegrityManage, requestMeta);  }}