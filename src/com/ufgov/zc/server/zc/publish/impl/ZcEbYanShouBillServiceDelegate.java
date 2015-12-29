/** *  */package com.ufgov.zc.server.zc.publish.impl;import java.util.List;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.model.ZcEbYanShouBill;import com.ufgov.zc.common.zc.publish.IZcEbYanShouBillServiceDelegate;import com.ufgov.zc.server.zc.service.IZcEbYanShouBillService;/** * @author Administrator * */public class ZcEbYanShouBillServiceDelegate implements IZcEbYanShouBillServiceDelegate {  private IZcEbYanShouBillService zcEbYanShouBillService;  public IZcEbYanShouBillService getZcEbYanShouBillService() {    return zcEbYanShouBillService;  }  public void setZcEbYanShouBillService(IZcEbYanShouBillService zcEbYanShouBillService) {    this.zcEbYanShouBillService = zcEbYanShouBillService;  }  public ZcEbYanShouBill auditFN(ZcEbYanShouBill zcEbYanShouBill, RequestMeta requestMeta) {    return zcEbYanShouBillService.auditFN(zcEbYanShouBill, requestMeta);  }  public ZcEbYanShouBill callbackFN(ZcEbYanShouBill zcEbYanShouBill, RequestMeta requestMeta) {    return zcEbYanShouBillService.callbackFN(zcEbYanShouBill, requestMeta);  }  public void deleteZcEbYanShouBillFN(ZcEbYanShouBill zcEbYanShouBill, RequestMeta requestMeta) {    zcEbYanShouBillService.deleteZcEbYanShouBill(zcEbYanShouBill);  }  public void deleteZcEbYanShouBillListFN(List zcEbYanShouBillList, RequestMeta requestMeta) {    zcEbYanShouBillService.deleteZcEbYanShouBill(zcEbYanShouBillList);  }  public List getEbYanShouBillList(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {    return zcEbYanShouBillService.getEbYanShouBillList(elementConditionDto, requestMeta);  }  public ZcEbYanShouBill getZcEbYanShouBill(String billCode, RequestMeta requestMeta) {    return zcEbYanShouBillService.getZcEbYanShouBill(billCode);  }  public ZcEbYanShouBill newCommitFN(ZcEbYanShouBill zcEbYanShouBill, RequestMeta requestMeta) {    return zcEbYanShouBillService.newCommitFN(zcEbYanShouBill, requestMeta);  }  public ZcEbYanShouBill providerNewCommitFN(ZcEbYanShouBill zcEbYanShouBill, RequestMeta requestMeta) {    return zcEbYanShouBillService.providerNewCommitFN(zcEbYanShouBill, requestMeta);  }  public ZcEbYanShouBill providerAuditFN(ZcEbYanShouBill zcEbYanShouBill, RequestMeta requestMeta) {    return zcEbYanShouBillService.providerAuditFN(zcEbYanShouBill, requestMeta);  }  public ZcEbYanShouBill CaiGouCommitFN(ZcEbYanShouBill zcEbYanShouBill, RequestMeta requestMeta) {    return zcEbYanShouBillService.CaiGouCommitFN(zcEbYanShouBill, requestMeta);  }  public ZcEbYanShouBill saveZcEbYanShouBillFN(ZcEbYanShouBill zcEbYanShouBill, RequestMeta requestMeta) {    return zcEbYanShouBillService.saveZcEbYanShouBill(zcEbYanShouBill, requestMeta);  }  public ZcEbYanShouBill unAuditFN(ZcEbYanShouBill zcEbYanShouBill, RequestMeta requestMeta) {    return zcEbYanShouBillService.unAuditFN(zcEbYanShouBill, requestMeta);  }  public ZcEbYanShouBill untreadFN(ZcEbYanShouBill zcEbYanShouBill, RequestMeta requestMeta) {    return zcEbYanShouBillService.untreadFN(zcEbYanShouBill, requestMeta);  }  public void sendToProcurementUnitFN(ZcEbYanShouBill bill, RequestMeta requestMeta) {    // TCJLODO Auto-generated method stub    zcEbYanShouBillService.sendToProcurementUnit(bill, requestMeta);  }}