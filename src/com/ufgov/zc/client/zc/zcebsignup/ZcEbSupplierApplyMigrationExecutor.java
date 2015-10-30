package com.ufgov.zc.client.zc.zcebsignup;import com.ufgov.zc.client.common.ServiceFactory;import com.ufgov.zc.client.component.zc.dataexchange.DataExchangeListPanel;import com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.model.DataExchangeLog;import com.ufgov.zc.common.zc.model.DataExchangeRedo;import com.ufgov.zc.common.zc.model.ZcEbSignup;import com.ufgov.zc.common.zc.publish.IZcEbSignupServiceDelegate;import java.util.ArrayList;import java.util.Date;import java.util.List;import java.util.Map;public class ZcEbSupplierApplyMigrationExecutor extends ABaseData {  private transient IZcEbSignupServiceDelegate zcEbSignupServiceDelegate;  public ZcEbSupplierApplyMigrationExecutor() {  }  public int doExportData(ElementConditionDto dto, RequestMeta meta, String saveRootPath) {    //获得所有的投标书数据    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在查询需要导出的记录...");    if (this.getNeedExportDataRedoList() != null && this.getNeedExportDataRedoList().size() > 0) {      List whereList = this.getNeedExportDataIDList();      List supplierApplyList = new ArrayList();      List exportDataList = new ArrayList();      DataExchangeLog log = null;      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "共查询出记录【"      + this.getNeedExportDataRedoList().size() + "】...");      for (int i = 0; i < whereList.size(); i++) {        DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在查询并导出第【" + (i + 1) + "】条记录...");        String whereStr = (String) whereList.get(i);        String[] whereArr = whereStr.split("@");        String signupId = whereArr[2];        ZcEbSignup curObj = new ZcEbSignup();        curObj.setSignupId(signupId);        meta.setCompoId("ZC_EB_SIGNUP");        ZcEbSignup zcEbSignup = this.getZcEbSignupServiceDelegate().getZcEbSignupById(curObj, meta);        supplierApplyList.add(zcEbSignup);        DataExchangeRedo dataExchangeRedo = getDataExchangeRedo(whereStr);        this.successRecordMap.put(dataExchangeRedo.getId(), dataExchangeRedo);        //写日志        log = new DataExchangeLog();        this.makeDataExchangeLog(log, meta.getSvUserID(), "导出成功", "", "", "OUT", zcEbSignup);        exportDataList.add(log);      }      this.getExchangeDataLogModel().setExportDataList(exportDataList);      this.setDataList(supplierApplyList);    } else {      this.setDataList(new ArrayList());    }    if (this.getDataList() == null || this.getDataList().size() == 0) {      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "当前没有需要导出的记录...");      return 0;    }    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "查询到" + this.getDataList().size()    + "条记录...");    return this.dataList.size();  }  /**   * 填写日志   * @param log   * @param userID   * @param succFail   * @param exceptionMsg   * @param detail   * @param type   * @param bid   */  private void makeDataExchangeLog(DataExchangeLog log, String userID, String succFail, String exceptionMsg,  String detail, String type, ZcEbSignup zcEbSignup) {    log.setDataTypeID(this.dataTypeID);    log.setDataTypeName(this.dataTypeName);    log.setUserID(userID);    log.setRecStatus(succFail);    log.setDetailInfo(detail);    log.setExceptText(exceptionMsg);    log.setGentType(type);    log.setOptDateTime(new Date());    log.setRecSrcID(zcEbSignup.getProviderCode());    //    log.setRecSrcName(bid.getZcMakeName());    log.setRecSrcTab(this.mainTableName);  }  public int doImportData(ElementConditionDto dto, RequestMeta meta, String rootPath) {    List supplierApplyList = this.getDataList();    int i = 0;    DataExchangeLog log = null;    List importDataList = new ArrayList();    int size = supplierApplyList.size();    for (i = 0; i < size; i++) {      ZcEbSignup zcEbSignup = (ZcEbSignup) supplierApplyList.get(i);      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在发送数据到服务器..." + (i + 1) + "/" + size);      meta.setCompoId("ZC_EB_SIGNUP");      this.getZcEbSignupServiceDelegate().insertZcEbSignupFN(zcEbSignup, meta);      //插入日志      log = new DataExchangeLog();      this.makeDataExchangeLog(log, meta.getSvUserID(), "导入成功", "", "", "IN", zcEbSignup);      importDataList.add(log);    }    this.getExchangeDataLogModel().setImportDataList(importDataList);    return i;  }  public Map getAttachmentDataMap() {    return null;  }  public IZcEbSignupServiceDelegate getZcEbSignupServiceDelegate() {    if (this.zcEbSignupServiceDelegate == null) {      zcEbSignupServiceDelegate = (IZcEbSignupServiceDelegate) ServiceFactory.create(      IZcEbSignupServiceDelegate.class, "zcEbSignupServiceDelegate");    }    return zcEbSignupServiceDelegate;  }}