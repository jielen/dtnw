package com.ufgov.zc.client.zc.zcqb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.component.zc.dataexchange.DataExchangeListPanel;
import com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData;
import com.ufgov.zc.client.component.zc.dataexchange.model.AttachmentFile;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.DataExchangeException;
import com.ufgov.zc.common.zc.model.DataExchangeLog;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDingdian;
import com.ufgov.zc.common.zc.publish.IZcDingDianServiceDelegate;

public class ZcDingDianDataExchangeExecutor  extends ABaseData {

  /**
   * 
   */
  private static final long serialVersionUID = -8920662835791028727L;
  private static final String ZC_DINGDIAN = "ZC_DINGDIAN";
  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#doExportData(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  public int doExportData(ElementConditionDto dto, RequestMeta meta, String saveRootPath) {
    // TCJLODO Auto-generated method stub
    
    int rtn=0;
    this.getDataList().clear();    
    //获取定点采购及相关数据
    HashMap dataMap = new HashMap();
    List qbLst=getQbDatas(dto,meta); 
    if(getDelDataIdlst()!=null){
      dataMap.put(ZcBaseBill.OPERTATE_TYPE_DEL, getDelDataIdlst());
      rtn+=getDelDataIdlst().size();
    }
    if(qbLst!=null){
      
      //定点采购业务数据
      dataMap.put(this.ZC_DINGDIAN, qbLst); 
      
      //工作流审批信息
      List<String> processInstIdLst=new ArrayList<String>();
      for(int i=0;i<qbLst.size();i++){
        ZcBaseBill bill=(ZcBaseBill)qbLst.get(i);
        processInstIdLst.add(""+bill.getProcessInstId());
      }      
      exportWFinfos(dataMap,processInstIdLst,meta);
      
      //获取定点采购附件      
      getAttachFiles(meta, saveRootPath, qbLst);      
      
      this.getDataList().add(dataMap);
      rtn+=qbLst.size();      
    }
    if(dataMap.size()>0){
      this.getDataList().add(dataMap);
    }
   
    return rtn;
  }

  /**
   * @param meta
   * @param saveRootPath
   * @param htLst
   */
  private void getAttachFiles(RequestMeta meta, String saveRootPath, List htLst) {

    // 准备附件列表
       if (this.attachmentDataMap == null) {
         this.attachmentDataMap = new HashMap<String, Map<String, AttachmentFile>>();
       } else {
         this.attachmentDataMap.clear();
       }
       String makeFileDirName = saveRootPath.substring(saveRootPath.lastIndexOf(File.separator)) + File.separator + "attach_files";

       String makeFilePath = saveRootPath + File.separator + "attach_files";

       makeDirs(makeFilePath);

       String path = makeFilePath + File.separator;
       
       for (int i = 0; i < htLst.size(); i++) {
         ZcDingdian f = (ZcDingdian) htLst.get(i);
         try {
           downFile(makeFileDirName, f.getHtSaomiaoFileId(), f.getHtSaomiaoFile(), null, path, meta); 
         } catch (IOException e) {
           // TCJLODO Auto-generated catch block
           e.printStackTrace();
           DataExchangeListPanel.setProgressText(this.getDataTypeName() + "获取采购合同附件出错...\n" + e.getMessage());
         }

       }
  }

  private List getQbDatas(ElementConditionDto dto, RequestMeta meta) {
    // TCJLODO Auto-generated method stub

    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在查询需要导出的记录...");
    List<String> idLst = new ArrayList<String>();
    List<ZcDingdian> billLst=new ArrayList<ZcDingdian>();
    if (this.getNeedExportDataRedoList() != null && this.getNeedExportDataRedoList().size() > 0) {
      int i=0;
      for (DataExchangeRedo redo : getNeedExportDataRedoList()) {
        if(ZcBaseBill.OPERTATE_TYPE_DEL.equalsIgnoreCase(redo.getOperateType())){
          getDelDataIdlst().add(redo.getRecordID());
          i++;
          continue;
        }
        idLst.add(redo.getRecordID());
        i++;
      }
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "查询到"+i+"条记录需要导出...");
      dto.setPmAdjustCodeList(idLst);
      IZcDingDianServiceDelegate qbService=(IZcDingDianServiceDelegate) ServiceFactory.create(IZcDingDianServiceDelegate.class,"zcDingDianServiceDelegate");
      billLst = qbService.queryExportsDatas(dto, meta);
    }
    createExportLog(idLst,billLst,meta);
    return billLst;
  }

  private void createExportLog(List<String> idLst, List<ZcDingdian> billLst,RequestMeta meta) {
    // TCJLODO Auto-generated method stub
    if(billLst==null || billLst.size()==0){
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "业务数据中没有查到对应的业务数据...");
    }else{
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "导出"+billLst.size()+"条业务数据...");
    }
    
    DataExchangeLog log=null;
    List<DataExchangeLog> exportDataList = new ArrayList<DataExchangeLog>();
    
    for (int i = 0; i < billLst.size(); i++) {
      ZcDingdian qb=(ZcDingdian)billLst.get(i);
      String recordID = qb.getDdCode();
      String successInfo = "导出成功";
      this.successRecordMap.put(recordID, getDataExchangeRedo(recordID));

      log = new DataExchangeLog();

      this.makeDataExchangeLog(log, meta.getSvUserID(), successInfo, "", "", EXPORT, qb);

      exportDataList.add(log);
    }
    this.getExchangeDataLogModel().setExportDataList(exportDataList);
  }
  private void makeDataExchangeLog(DataExchangeLog log, String userID, String succFail, String exceptionMsg, String detail, String type, ZcDingdian atc) {

    log.setDataTypeID(this.getDataTypeID());

    log.setDataTypeName(this.getDataTypeName());

    log.setUserID(userID);

    log.setRecStatus(succFail);

    log.setDetailInfo(detail);

    log.setExceptText(exceptionMsg);

    log.setGentType(type);

    log.setOptDateTime(new Date());

    log.setRecSrcID(atc.getDdCode());

    log.setRecSrcName(atc.getDdName());

    log.setRecSrcTab("ZC_DINGDIAN");

  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#doImportData(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta, java.lang.String)
   */
  @Override
  public int doImportData(ElementConditionDto dto, RequestMeta meta, String readRootPath) {
    // TCJLODO Auto-generated method stub

    String info = null;
    DataExchangeLog log = null;
    
    if(getDataList()==null || getDataList().size()==0){
      info="没有定点采购数据可以导入";
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
    
    //同步删除数据
    List delDataLst=(List)map.get(ZcBaseBill.OPERTATE_TYPE_DEL);
    if(delDataLst!=null && delDataLst.size()>0){
      importDelData(meta,delDataLst);
    }
  //导入定点采购业务数据
    List htDataLst=(List) map.get(this.ZC_DINGDIAN);
    
    if(htDataLst==null || htDataLst.size()==0)return 0;
    
    importQb(meta, htDataLst);
    
  //导入工作流数据  
    importWfinfos(meta);

    return htDataLst.size();
  }
//同步删除数据
  private void importDelData(RequestMeta meta,List delDataLst) {
    // TCJLODO Auto-generated method stub
    String info=null;
    DataExchangeLog log;
    List<DataExchangeLog> importDataList = new ArrayList<DataExchangeLog>();
    
    if(delDataLst!=null && delDataLst.size()>0){
      IZcDingDianServiceDelegate qbService=(IZcDingDianServiceDelegate) ServiceFactory.create(IZcDingDianServiceDelegate.class,"zcDingDianServiceDelegate");
      
      for (int i = 0; i < delDataLst.size(); i++) {

        log = new DataExchangeLog();
        DataExchangeListPanel.setProgressText(getDataTypeName() + "正在将删除的定点信息发送到服务器..." + (i + 1) + "/" + delDataLst.size());

        String  id = (String) delDataLst.get(i);
        ZcDingdian qx=new ZcDingdian();
        qx.setDdCode(id);
        try {
          info = qbService.importDelDataFN(id, meta);
        } catch (DataExchangeException e) {
          // TCJLODO: handle exception
          info=e.getMessage();
          this.makeDataExchangeLog(log, meta.getSvUserID(), "同步删定点数据失败", "", info, IMPORT, qx);
          qx=qbService.selectByPrimaryKey(id, meta);
          cantDelinstanceIdLst.add(""+qx.getProcessInstId());
          continue;
        }

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);


        this.makeDataExchangeLog(log, meta.getSvUserID(), "同步删除定点数据成功", "", info, IMPORT, qx);

        importDataList.add(log);
      }      
    }

    this.getExchangeDataLogModel().setImportDataList(importDataList);
    
  }
  /**
   * 导入定点采购业务数
   * @param meta
   * @param htDataLst
   */
  private void importQb(RequestMeta meta, List htDataLst) {
    String info;
    DataExchangeLog log;
    List<DataExchangeLog> importDataList = new ArrayList<DataExchangeLog>();
    IZcDingDianServiceDelegate qbService=(IZcDingDianServiceDelegate) ServiceFactory.create(IZcDingDianServiceDelegate.class,"zcDingDianServiceDelegate");
    
    if(htDataLst!=null && htDataLst.size()>0){
      for (int i = 0; i < htDataLst.size(); i++) {

        DataExchangeListPanel.setProgressText(getDataTypeName() + "正在将定点采购发送到服务器..." + (i + 1) + "/" + htDataLst.size());

        ZcDingdian bill = (ZcDingdian) htDataLst.get(i);

        info = qbService.importTransDatasFN(bill, meta);

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);

        log = new DataExchangeLog();

        this.makeDataExchangeLog(log, meta.getSvUserID(), "导入业务数据成功", "", info, IMPORT, bill);

        importDataList.add(log);
      }      
    }

    this.getExchangeDataLogModel().setImportDataList(importDataList);
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#getAttachmentDataMap()
   */
  @Override
  public Map<String, Map<String, AttachmentFile>> getAttachmentDataMap() {
    // TCJLODO Auto-generated method stub
    return attachmentDataMap;
  }

}
