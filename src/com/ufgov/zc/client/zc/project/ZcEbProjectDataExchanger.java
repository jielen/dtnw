/**
 * 
 */
package com.ufgov.zc.client.zc.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.component.zc.dataexchange.DataExchangeListPanel;
import com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData;
import com.ufgov.zc.client.component.zc.dataexchange.model.AttachmentFile;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.DataExchangeLog;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.model.ZcEbAuditSheet;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.model.ZcEbFormula;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbPackReq;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.model.ZcEbProjFile;
import com.ufgov.zc.common.zc.model.ZcEbProjZbFile;
import com.ufgov.zc.common.zc.model.ZcEbRequirementDetail;
import com.ufgov.zc.common.zc.model.ZcEbXunJia;
import com.ufgov.zc.common.zc.publish.IZcEbAuditSheetServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbEntrustServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbFormulaServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbProjServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbXjbjServiceDelegate;

/**
 * @author Administrator
 *
 */
public class ZcEbProjectDataExchanger extends ABaseData {

  /**
   * 
   */
  private static final long serialVersionUID = 5587037162075163104L;

  private static final String PRJOECT = "PRJOECT";

  private static final String ENTRUST = "ENTRUST";

  private static final String ADUTYSHEET = "ADUTYSHEET";

  private static final String FORMULA = "FORMULA";
  
  private static final String XUNJIA = "XUNJIA";

  public transient IZcEbProjServiceDelegate serviceDelegate = null;

  public transient IZcEbEntrustServiceDelegate entrustService = null;

  public transient IZcEbAuditSheetServiceDelegate auditserviceDelegate = null;

  public transient IZcEbFormulaServiceDelegate zcEbFormulaServiceDelegate = null;

  public transient IZcEbXjbjServiceDelegate zcEbXjbjServiceDelegate = null;

  public IZcEbFormulaServiceDelegate getZcEbFormulaServiceDelegate() {
    if (zcEbFormulaServiceDelegate == null) {

      zcEbFormulaServiceDelegate = (IZcEbFormulaServiceDelegate) ServiceFactory.create(IZcEbFormulaServiceDelegate.class,
        "zcEbFormulaServiceDelegate");
    }

    return zcEbFormulaServiceDelegate;
  }

  public void setZcEbFormulaServiceDelegate(IZcEbFormulaServiceDelegate zcEbFormulaServiceDelegate) {
    this.zcEbFormulaServiceDelegate = zcEbFormulaServiceDelegate;
  }

  public IZcEbXjbjServiceDelegate getZcEbXjbjServiceDelegate() {

    if (zcEbXjbjServiceDelegate == null) {

      zcEbXjbjServiceDelegate = (IZcEbXjbjServiceDelegate) ServiceFactory.create(IZcEbXjbjServiceDelegate.class, "zcebXjbjServiceDelegate");

    }

    return zcEbXjbjServiceDelegate;
  }

  public void setZcEbXjbjServiceDelegate(IZcEbXjbjServiceDelegate zcEbXjbjServiceDelegate) {
    this.zcEbXjbjServiceDelegate = zcEbXjbjServiceDelegate;
  }

  public IZcEbEntrustServiceDelegate getIZcEbEntrustServiceDelegate() {
    if (entrustService == null) {

      entrustService = (IZcEbEntrustServiceDelegate) ServiceFactory.create(IZcEbEntrustServiceDelegate.class, "zcEbEntrustServiceDelegate");

    }

    return entrustService;
  }

  public void setIZcEbEntrustServiceDelegate(IZcEbEntrustServiceDelegate entrustService) {
    this.entrustService = entrustService;
  }

  public IZcEbAuditSheetServiceDelegate getAuditserviceDelegate() {
    if (auditserviceDelegate == null) {

      auditserviceDelegate = (IZcEbAuditSheetServiceDelegate) ServiceFactory.create(IZcEbAuditSheetServiceDelegate.class,
        "zcEbAuditSheetServiceDelegate");

    }

    return auditserviceDelegate;
  }

  public void setAuditserviceDelegate(IZcEbAuditSheetServiceDelegate auditserviceDelegate) {
    this.auditserviceDelegate = auditserviceDelegate;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#doExportData(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta, java.lang.String)
   */
  @Override
  public int doExportData(ElementConditionDto dto, RequestMeta meta, String saveRootPath) {

    // 准备附件列表
    if (this.attachmentDataMap == null) {

      this.attachmentDataMap = new HashMap<String, Map<String, AttachmentFile>>();

    } else {

      this.attachmentDataMap.clear();

    }

    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在查询需要导出的记录...");

    HashMap dataMap = new HashMap();

    //获取立项分包信息
    getProjectInfo(dto, meta, saveRootPath, dataMap);
    //获取评标方法
    getFormula(dto, meta, saveRootPath, dataMap);
    //获取询价单
    getXunJiaInfo(dto, meta, saveRootPath, dataMap);
    //获取批办单
    getDutySheetLst(dto, meta, saveRootPath, dataMap);
    //获取任务单
    getEntrustLst(dto, meta, saveRootPath, dataMap);
    //获取采购需求
    getRequirementLst(dto, meta, saveRootPath, dataMap);

    this.getDataList().clear();
    this.getDataList().add(dataMap);

    DataExchangeLog log = null;

    List<DataExchangeLog> exportDataList = new ArrayList<DataExchangeLog>();
    List projLst = (List) dataMap.get(this.PRJOECT);
    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "查询到" + projLst.size() + "条记录...");
    for (int i = 0; i < projLst.size(); i++) {

      ZcEbProj proj = (ZcEbProj) projLst.get(i);

      if (proj == null) {

        ZcEbProj tmp = new ZcEbProj();

        String id = this.getNeedExportDataIDList().get(i);

        log = new DataExchangeLog();

        this.makeDataExchangeLog(log, meta.getSvUserID(), "导出失败", "", "未在对应的表中找相应的记录", EXPORT, tmp);

        this.getExchangeDataLogModel().getExportDataList().add(log);

        this.successRecordMap.put(id, getDataExchangeRedo(id));

      } else {
        String recordID = proj.getProjCode();
        String successInfo = "导出成功";
        this.successRecordMap.put(recordID, getDataExchangeRedo(recordID));

        log = new DataExchangeLog();

        this.makeDataExchangeLog(log, meta.getSvUserID(), successInfo, "", "", EXPORT, proj);

        exportDataList.add(log);
      }

    }

    this.getExchangeDataLogModel().setExportDataList(exportDataList);
    return projLst.size();
  }

  private void getXunJiaInfo(ElementConditionDto dto, RequestMeta meta, String saveRootPath, HashMap dataMap) {
    // TCJLODO Auto-generated method stub
    List<ZcEbProj> projLst = (List<ZcEbProj>) dataMap.get(this.PRJOECT);
    if (projLst == null || projLst.size() == 0)
      return;
    List projIdLst = new ArrayList();
    for (ZcEbProj zcEbProj : projLst) {
      projIdLst.add(zcEbProj.getProjCode());
    }
    if (projIdLst.size() > 0) {
      List xunjiaLst = getZcEbXjbjServiceDelegate().getXunJiaByProjLst(projIdLst, meta);
      if(xunjiaLst==null || xunjiaLst.size()==0)return;
      dataMap.put(XUNJIA, xunjiaLst);
      //获取附件
      String makeFileDirName = saveRootPath.substring(saveRootPath.lastIndexOf(File.separator)) + File.separator + "attach_files";
      String makeFilePath = saveRootPath + File.separator + "attach_files";
      makeDirs(makeFilePath);
      String path = makeFilePath + File.separator;
      for(int i=0;i<xunjiaLst.size();i++){
        ZcEbXunJia xj=(ZcEbXunJia)xunjiaLst.get(i);
        try {
          downFile(makeFileDirName, xj.getSpTechFileId(), xj.getSpTechFileName(), "" + xj.getXjCode(), path, meta);
        } catch (IOException e) {
          // TCJLODO Auto-generated catch block
          e.printStackTrace();
          DataExchangeListPanel.setProgressText(this.getDataTypeName() + "获取询价单附件出错...\n" + e.getMessage());
        }        
      }
    }

  }

  private void getFormula(ElementConditionDto dto, RequestMeta meta, String saveRootPath, HashMap dataMap) {
    // TCJLODO Auto-generated method stub    List<ZcEbProj> projLst=(List<ZcEbProj>) dataMap.get(this.PRJOECT);
    List<ZcEbProj> projLst = (List<ZcEbProj>) dataMap.get(this.PRJOECT);
    if (projLst == null || projLst.size() == 0)
      return;
    List projIdLst = new ArrayList();
    for (ZcEbProj zcEbProj : projLst) {
      projIdLst.add(zcEbProj.getProjCode());
    }
    if (projIdLst.size() > 0) {
      List formulaLst = getZcEbFormulaServiceDelegate().getFormulaExportDatas(projIdLst, meta);
      dataMap.put(FORMULA, formulaLst);
    }
  }

  private void getProjectInfo(ElementConditionDto dto, RequestMeta meta, String saveRootPath, HashMap dataMap) {
    // TCJLODO Auto-generated method stub

    List<String> idLst = new ArrayList<String>();

    if (this.getNeedExportDataRedoList() != null && this.getNeedExportDataRedoList().size() > 0) {
      for (DataExchangeRedo redo : getNeedExportDataRedoList()) {
        idLst.add(redo.getRecordID());
      }

      dto.setPmAdjustCodeList(idLst);
      List projLst = this.getServiceDelegate().queryExportsDatas(dto, meta);
      dataMap.put(PRJOECT, projLst); 
      
      //获取招标文件
      List projIdLst = new ArrayList();
      for (int i = 0; i < projLst.size(); i++) {
        ZcEbProj proj = (ZcEbProj) projLst.get(i);
        projIdLst.add(proj.getProjCode());
      }
      dto.getPmAdjustCodeList().clear();
      dto.setPmAdjustCodeList(projIdLst);
      List zbFileLst =getServiceDelegate().getExportsZbFile(dto, meta);
      if (zbFileLst == null || zbFileLst.size() == 0)
        return;
      String makeFileDirName = saveRootPath.substring(saveRootPath.lastIndexOf(File.separator)) + File.separator + "attach_files";
      String makeFilePath = saveRootPath + File.separator + "attach_files";
      makeDirs(makeFilePath);
      String path = makeFilePath + File.separator;
      for (int i = 0; i < zbFileLst.size(); i++) {
        ZcEbProjZbFile f = (ZcEbProjZbFile) zbFileLst.get(i);
        try {
          downFile(makeFileDirName, f.getFileId(), f.getFileName(), f.getProjCode(), path, meta);
          downFile(makeFileDirName, f.getWordFileId(), f.getWordFileId(), f.getProjCode() + f.getWordFileId(), path, meta);
        } catch (IOException e) {
          // TCJLODO Auto-generated catch block
          e.printStackTrace();
          DataExchangeListPanel.setProgressText(this.getDataTypeName() + "获取招标文件附件出错...\n" + e.getMessage());
        }

      }

    }
  }


  //获取采购需求
  private void getRequirementLst(ElementConditionDto dto, RequestMeta meta, String saveRootPath, HashMap dataMap) {
    // TCJLODO Auto-generated method stub
    List<ZcEbProj> projLst = (List<ZcEbProj>) dataMap.get(this.PRJOECT);
    if (projLst == null || projLst.size() == 0)
      return;

    for (ZcEbProj zcEbProj : projLst) {
      if (zcEbProj.getPackList() == null || zcEbProj.getPackList().size() == 0)
        continue;
      for (int j = 0; j < zcEbProj.getPackList().size(); j++) {
        ZcEbPack pack = (ZcEbPack) zcEbProj.getPackList().get(j);
        if (pack.getRequirementDetailList() == null && pack.getRequirementDetailList().size() == 0)
          continue;
        //获取需求对应的附件
        String makeFileDirName = saveRootPath.substring(saveRootPath.lastIndexOf(File.separator)) + File.separator + "attach_files";
        String makeFilePath = saveRootPath + File.separator + "attach_files";
        makeDirs(makeFilePath);
        String path = makeFilePath + File.separator;
        for (int k = 0; k < pack.getRequirementDetailList().size(); k++) {
          ZcEbPackReq rd = (ZcEbPackReq) pack.getRequirementDetailList().get(k);
          if(rd==null || rd.getRequirementDetail()==null)continue;
          try {
            downFile(makeFileDirName, rd.getRequirementDetail().getItemAttachBlobid(), rd.getRequirementDetail().getItemAttach(), "" + rd.getDetailCode(), path, meta);
          } catch (IOException e) {
            // TCJLODO Auto-generated catch block
            e.printStackTrace();
            DataExchangeListPanel.setProgressText(this.getDataTypeName() + "获取需求文件附件出错...\n" + e.getMessage());
          }
        }
      }
    }
  }

  //获取任务单
  private void getEntrustLst(ElementConditionDto dto, RequestMeta meta, String saveRootPath, HashMap dataMap) {
    // TCJLODO Auto-generated method stub
    List<ZcEbProj> projLst = (List<ZcEbProj>) dataMap.get(this.PRJOECT);
    if (projLst == null || projLst.size() == 0)
      return;
    List<String> entrustIdLst = new ArrayList<String>();
    for (ZcEbProj zcEbProj : projLst) {
      if (zcEbProj.getPackList() == null || zcEbProj.getPackList().size() == 0)
        continue;
      for (int j = 0; j < zcEbProj.getPackList().size(); j++) {
        ZcEbPack pack = (ZcEbPack) zcEbProj.getPackList().get(j);
        entrustIdLst.add(pack.getEntrustCode());
      }
    }
    if (entrustIdLst.size() > 0) {
      List entrustLst = getIZcEbEntrustServiceDelegate().getZcEbEntrustExportData(entrustIdLst, meta);
      if(entrustLst!=null){
        dataMap.put(ENTRUST, entrustLst);
      }
    }
  }

  //获取批办单
  private void getDutySheetLst(ElementConditionDto dto, RequestMeta meta, String saveRootPath, HashMap dataMap) {
    // TCJLODO Auto-generated method stub List<ZcEbProj> projLst=(List<ZcEbProj>) dataMap.get(this.PRJOECT);
    List<ZcEbProj> projLst = (List<ZcEbProj>) dataMap.get(this.PRJOECT);
    if (projLst == null || projLst.size() == 0)
      return;
    List<String> dutySheetIdLst = new ArrayList<String>();
    for (ZcEbProj zcEbProj : projLst) {
      if (zcEbProj.getPackList() == null || zcEbProj.getPackList().size() == 0)
        continue;
      for (int j = 0; j < zcEbProj.getPackList().size(); j++) {
        ZcEbPack pack = (ZcEbPack) zcEbProj.getPackList().get(j);
        dutySheetIdLst.add(pack.getEntrustCode());
      }
    }
    if (dutySheetIdLst.size() > 0) {
      List auditsheetLst = getAuditserviceDelegate().getAuditSheetExportData(dutySheetIdLst, meta);
      if(auditsheetLst!=null){
        dataMap.put(ADUTYSHEET, auditsheetLst);
      }
    }

  }

  private void getAttachFile(ZcEbProj proj, String saveRootPath, RequestMeta meta) throws IOException {
    // TCJLODO Auto-generated method stub
    //    String parentDirectory = new File(saveRootPath).getParentFile().getParentFile().getAbsolutePath();

    String makeFileDirName = saveRootPath.substring(saveRootPath.lastIndexOf(File.separator)) + File.separator + "attach_files";

    String makeFilePath = saveRootPath + File.separator + "attach_files";

    makeDirs(makeFilePath);

    String path = makeFilePath + File.separator;

    List packLst = proj.getPackList();

    for (Iterator iterator2 = packLst.iterator(); iterator2.hasNext();) {

      ZcEbPack d = (ZcEbPack) iterator2.next();

      downFile(makeFileDirName, d.getReqFileId(), d.getReqFileName(), d.getPackCode(), path, meta);
      downFile(makeFileDirName, d.getFormulaFileId(), d.getFormulaFileName(), d.getPackCode(), path, meta);

    }

  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#doImportData(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta, java.lang.String)
   */
  @Override
  public int doImportData(ElementConditionDto dto, RequestMeta meta, String readRootPath) {

    String info = null;

    DataExchangeLog log = null;
    
    int rtn=0;

    List<DataExchangeLog> importDataList = new ArrayList<DataExchangeLog>();

    if(getDataList()==null || getDataList().size()==0){
      info="没有立项分包数据导入";
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);
      return 0;
    }

    // 先进行文件发送和保存，只有保存成功后，才能够往下走
    try {
      toSendFiles(readRootPath, meta);
    } catch (IOException e) {
      // TCJLODO Auto-generated catch block
      e.printStackTrace();
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "上传附件失败...\n" + e.getMessage());
    }

    HashMap map=(HashMap) getDataList().get(0);
    List dataLst=(List) map.get(this.PRJOECT);
    if(dataLst!=null && dataLst.size()>0){
      rtn=dataLst.size();
      for (int i = 0; i < dataLst.size(); i++) {

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在将立项分包发送到服务器..." + (i + 1) + "/" + dataLst.size());

        ZcEbProj bill = (ZcEbProj) dataLst.get(i);

        info = this.getServiceDelegate().importTransDatasFN(bill, meta);

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);

        log = new DataExchangeLog();

        this.makeDataExchangeLog(log, meta.getSvUserID(), "导入业务数据成功", "", info, IMPORT, bill);

        importDataList.add(log);

      }
    }
    
     dataLst=(List) map.get(this.ENTRUST);
    if(dataLst!=null && dataLst.size()>0){
      for (int i = 0; i < dataLst.size(); i++) {

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在将采购任务发送到服务器..." + (i + 1) + "/" + dataLst.size());

        ZcEbEntrust bill = (ZcEbEntrust) dataLst.get(i);

        info =getIZcEbEntrustServiceDelegate().importTransDatasFN(bill, meta);

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);

      }
    }
    
    dataLst=(List) map.get(this.FORMULA);
   if(dataLst!=null && dataLst.size()>0){
     for (int i = 0; i < dataLst.size(); i++) {

       DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在将采购任务发送到服务器..." + (i + 1) + "/" + dataLst.size());

       ZcEbFormula bill = (ZcEbFormula) dataLst.get(i);

       info =getZcEbFormulaServiceDelegate().importTransDatasFN(bill, meta);

       DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);

     }
   }
   
   dataLst=(List) map.get(this.XUNJIA);
  if(dataLst!=null && dataLst.size()>0){
    for (int i = 0; i < dataLst.size(); i++) {

      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在将询价单发送到服务器..." + (i + 1) + "/" + dataLst.size());

      ZcEbXunJia bill = (ZcEbXunJia) dataLst.get(i);

      info =getZcEbXjbjServiceDelegate().importXunjiaTransDatasFN(bill, meta);

      DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);

    }
  }
  
  dataLst=(List) map.get(this.ADUTYSHEET);
 if(dataLst!=null && dataLst.size()>0){
   for (int i = 0; i < dataLst.size(); i++) {

     DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在将批办单发送到服务器..." + (i + 1) + "/" + dataLst.size());

     ZcEbAuditSheet bill = (ZcEbAuditSheet) dataLst.get(i);

     info =getAuditserviceDelegate().importTransDatasFN(bill, meta);

     DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);

   }
 }


    this.getExchangeDataLogModel().setImportDataList(importDataList);

    return rtn;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#getAttachmentDataMap()
   */
  @Override
  public Map<String, Map<String, AttachmentFile>> getAttachmentDataMap() {
    // TCJLODO Auto-generated method stub
    return attachmentDataMap;
  }

  private void makeDataExchangeLog(DataExchangeLog log, String userID, String succFail, String exceptionMsg, String detail, String type, ZcEbProj atc) {

    log.setDataTypeID(this.getDataTypeID());

    log.setDataTypeName(this.getDataTypeName());

    log.setUserID(userID);

    log.setRecStatus(succFail);

    log.setDetailInfo(detail);

    log.setExceptText(exceptionMsg);

    log.setGentType(type);

    log.setOptDateTime(new Date());

    log.setRecSrcID(atc.getProjCode());

    log.setRecSrcName(atc.getProjName());

    log.setRecSrcTab("ZC_EB_PROJ");

  }

  public IZcEbProjServiceDelegate getServiceDelegate() {

    if (serviceDelegate == null) {

      serviceDelegate = (IZcEbProjServiceDelegate) ServiceFactory.create(IZcEbProjServiceDelegate.class, "zcEbProjServiceDelegate");

    }

    return serviceDelegate;

  }

  public void setServiceDelegate(IZcEbProjServiceDelegate serviceDelegate) {

    this.serviceDelegate = serviceDelegate;

  }

}
