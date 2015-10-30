package com.ufgov.zc.client.zc.zcebsupplier;import java.io.File;import java.io.IOException;import java.util.ArrayList;import java.util.Date;import java.util.HashMap;import java.util.Iterator;import java.util.List;import java.util.Map;import javax.swing.JOptionPane;import com.ufgov.zc.client.common.ServiceFactory;import com.ufgov.zc.client.component.zc.dataexchange.DataExchangeListPanel;import com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData;import com.ufgov.zc.client.component.zc.dataexchange.model.AttachmentFile;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.exception.BusinessException;import com.ufgov.zc.common.zc.model.DataExchangeLog;import com.ufgov.zc.common.zc.model.ZcEbSupplier;import com.ufgov.zc.common.zc.model.ZcEbSupplierQualify;import com.ufgov.zc.common.zc.model.ZcPProMake;import com.ufgov.zc.common.zc.model.ZcPProMitem;import com.ufgov.zc.common.zc.publish.IZcEbSupplierServiceDelegate;public class ZcEbSupplierDataExchangeExecutor extends ABaseData {  private static final long serialVersionUID = 1619825736456968019L;  public transient IZcEbSupplierServiceDelegate zcEbSupplierServiceDelegate = null;  public ZcEbSupplierDataExchangeExecutor() {  }  @Override  public int doExportData(ElementConditionDto dto, RequestMeta meta, String saveRootPath) {    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在查询需要导出的记录...");    if (this.getNeedExportDataRedoList() != null && this.getNeedExportDataRedoList().size() > 0) {      dto.setPmAdjustCodeList(this.getNeedExportDataIDList());      this.setDataList(this.getZcEbSupplierServiceDelegate().getZcEbSupplierListByIDs(dto, meta));    } else {      this.setDataList(new ArrayList<ZcEbSupplier>());    }    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "查询到【" + this.getDataList().size() + "】条有效记录...");    // 将附件信息存储到map中    if (this.attachmentDataMap == null) {      this.attachmentDataMap = new HashMap<String, Map<String, AttachmentFile>>();    } else {      this.attachmentDataMap.clear();    }    List<DataExchangeLog> exportDataList = new ArrayList<DataExchangeLog>();    DataExchangeLog log = null;        for (int i = 0; i < this.getDataList().size(); i++) {      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在查询并导出第【" + (i + 1) + "】条记录...");      ZcEbSupplier su = (ZcEbSupplier) this.getDataList().get(i);      String exceptionMsg="";      String successInfo="导出成功";      try {        getAttachFile(su, saveRootPath, meta);      } catch (IOException e) {        // TODO Auto-generated catch block        e.printStackTrace();        exceptionMsg=e.getMessage();        successInfo="供应商"+su.getCode()+"导出附件失败:";//        DataExchangeListPanel.setProgressText(this.getDataTypeName() + "采购计划"+make.getZcMakeCode()+"导出附件失败:" + e.getMessage());      }            this.successRecordMap.put(su.getCode(), getDataExchangeRedo(su.getCode()));      log = new DataExchangeLog();      this.makeDataExchangeLog(log, meta.getSvUserID(), successInfo, exceptionMsg, "", "OUT", su);      exportDataList.add(log);    }    this.getExchangeDataLogModel().setExportDataList(exportDataList);    return this.getDataList().size();  }  private void getAttachFile(ZcEbSupplier su, String saveRootPath, RequestMeta meta) throws IOException {    // TODO Auto-generated method stub//    String parentDirectory = new File(saveRootPath).getParentFile().getParentFile().getAbsolutePath();    String makeFileDirName = saveRootPath.substring(saveRootPath.lastIndexOf(File.separator))+File.separator+"attach_files";    String makeFilePath = saveRootPath + File.separator + "attach_files";    makeDirs(makeFilePath);    String path = makeFilePath + File.separator;        List itemLst = su.getQualifyList();    if(itemLst==null)return;    for (Iterator iterator2 = itemLst.iterator(); iterator2.hasNext();) {      ZcEbSupplierQualify qa = (ZcEbSupplierQualify) iterator2.next();      downFile(makeFileDirName, qa.getFilesID(), qa.getFilesName(), qa.getSupplierCode(), path, meta);    }  }  @Override  public int doImportData(ElementConditionDto dto, RequestMeta meta, String readRootPath) {    String info = null;    DataExchangeLog log = null;        // 先进行文件发送和保存，只有保存成功后，才能够往下走       try {         toSendFiles(readRootPath, meta);       } catch (IOException e) {         // TODO Auto-generated catch block         e.printStackTrace();         DataExchangeListPanel.setProgressText(this.getDataTypeName() + "上传附件失败...\n" +e.getMessage());       }           List<DataExchangeLog> importLogList = new ArrayList<DataExchangeLog>();    log("getDataList()="+getDataList());    int size = this.getDataList().size();    log("getDataList().size="+size);    for (int i = 0; i < size; i++) {      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在发送数据到服务器..." + (i + 1) + "/" + size);      ZcEbSupplier su = (ZcEbSupplier) this.getDataList().get(i);      try {        info = this.getZcEbSupplierServiceDelegate().importSupplier(su, meta);      } catch (BusinessException be) {        int sel = JOptionPane.showConfirmDialog(null, be.getMessage() + "\n是否继续？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);        if (sel == JOptionPane.YES_OPTION) {          DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);          log = new DataExchangeLog();          this.makeDataExchangeLog(log, meta.getSvUserID(), "导入失败", "", info, "IN", su);          importLogList.add(log);          continue;        }      }      DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);      log = new DataExchangeLog();      this.makeDataExchangeLog(log, meta.getSvUserID(), "导入成功", "", info, "IN", su);      importLogList.add(log);    }    this.getExchangeDataLogModel().setImportDataList(importLogList);    return this.getDataList().size();  }  private void log(String msg) {    // TODO Auto-generated method stub    System.out.println(msg);  }  private void makeDataExchangeLog(DataExchangeLog log, String userID, String succFail, String exceptionMsg, String detail, String type,  ZcEbSupplier su) {    log.setDataTypeID(this.getDataTypeID());    log.setDataTypeName(this.getDataTypeName());    log.setUserID(userID);    log.setRecStatus(succFail);    log.setDetailInfo(detail);    log.setExceptText(exceptionMsg);    log.setGentType(type);    log.setOptDateTime(new Date());    log.setRecSrcID(su.getCode());    log.setRecSrcName(su.getName());    log.setRecSrcTab("ZC_B_SUPPLIER");  }  @Override  public Map<String, Map<String, AttachmentFile>> getAttachmentDataMap() {    return new HashMap<String, Map<String, AttachmentFile>>();  }  public IZcEbSupplierServiceDelegate getZcEbSupplierServiceDelegate() {    if (zcEbSupplierServiceDelegate == null) {      zcEbSupplierServiceDelegate = (IZcEbSupplierServiceDelegate) ServiceFactory.create(IZcEbSupplierServiceDelegate.class,      "zcEbSupplierServiceDelegate");    }    return zcEbSupplierServiceDelegate;  }  public void setZcEbSupplierServiceDelegate(IZcEbSupplierServiceDelegate zcEbSupplierServiceDelegate) {    this.zcEbSupplierServiceDelegate = zcEbSupplierServiceDelegate;  }}