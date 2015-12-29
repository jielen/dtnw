/**
 * 
 */
package com.ufgov.zc.client.zc.zcxmcght;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.component.zc.dataexchange.DataExchangeListPanel;
import com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData;
import com.ufgov.zc.client.component.zc.dataexchange.model.AttachmentFile;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.DataExchangeLog;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.model.WfActionHistoryModel;
import com.ufgov.zc.common.zc.model.WfActionModel;
import com.ufgov.zc.common.zc.model.WfCurrentTaskModel;
import com.ufgov.zc.common.zc.model.WfDraftModel;
import com.ufgov.zc.common.zc.model.WfInstanceModel;
import com.ufgov.zc.common.zc.model.WfStateValueModel;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcXmcgHtServiceDelegate;

/**
 * 采购合同导入导出执行类
 * @author Administrator
 *
 */
public class ZcXmcgHtDataExchangeExecutor extends ABaseData {


  /**
   * 
   */
  private static final long serialVersionUID = -156204028048923804L;
  
  private static final String XMCGHT = "XMCGHT";
  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#doExportData(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  public int doExportData(ElementConditionDto dto, RequestMeta meta, String saveRootPath) {
    // TCJLODO Auto-generated method stub
    
    int rtn=0;
    this.getDataList().clear();    
    //获取合同及相关数据
    HashMap dataMap = new HashMap();
    List htLst=getHtDatas(dto,meta);
    if(htLst!=null){
      
      //合同业务数据
      dataMap.put(this.XMCGHT, htLst); 
      
      //工作流审批信息
      List<String> processInstIdLst=new ArrayList<String>();
      for(int i=0;i<htLst.size();i++){
        ZcBaseBill bill=(ZcBaseBill)htLst.get(i);
        processInstIdLst.add(""+bill.getProcessInstId());
      }      
      exportWFinfos(dataMap,processInstIdLst,meta);
      
      //获取合同附件      
      getHtFiles(meta, saveRootPath, htLst);      
      
      this.getDataList().add(dataMap);
      rtn=htLst.size();      
    }else{
      return rtn;
    }  
   
    return rtn;
  }

  /**
   * @param meta
   * @param saveRootPath
   * @param htLst
   */
  private void getHtFiles(RequestMeta meta, String saveRootPath, List htLst) {
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
      ZcXmcgHt f = (ZcXmcgHt) htLst.get(i);
      try {
        downFile(makeFileDirName, f.getZcImpFileBlobid(), f.getZcImpFile(), null, path, meta);
        downFile(makeFileDirName, f.getZcConTextBlobid(), f.getZcConText(), null, path, meta);
      } catch (IOException e) {
        // TCJLODO Auto-generated catch block
        e.printStackTrace();
        DataExchangeListPanel.setProgressText(this.getDataTypeName() + "获取采购合同附件出错...\n" + e.getMessage());
      }

    }
  }

  private List getHtDatas(ElementConditionDto dto, RequestMeta meta) {
    // TCJLODO Auto-generated method stub

    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在查询需要导出的记录...");
    List<String> idLst = new ArrayList<String>();
    List<ZcXmcgHt> billLst=new ArrayList<ZcXmcgHt>();
    if (this.getNeedExportDataRedoList() != null && this.getNeedExportDataRedoList().size() > 0) {
      for (DataExchangeRedo redo : getNeedExportDataRedoList()) {
        idLst.add(redo.getRecordID());
      }
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "查询到"+idLst.size()+"条记录需要导出...");
      dto.setPmAdjustCodeList(idLst);
      IZcXmcgHtServiceDelegate htService=(IZcXmcgHtServiceDelegate) ServiceFactory.create(IZcXmcgHtServiceDelegate.class,"zcXmcgHtServiceDelegate");
      billLst = htService.queryExportsDatas(dto, meta);
    }
    createExportLog(idLst,billLst,meta);
    return billLst;
  }

  private void createExportLog(List<String> idLst, List<ZcXmcgHt> billLst,RequestMeta meta) {
    // TCJLODO Auto-generated method stub
    if(billLst==null || billLst.size()==0){
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "业务数据中没有查到对应的业务数据...");
    }else{
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "导出"+billLst.size()+"条业务数据...");
    }
    
    DataExchangeLog log=null;
    List<DataExchangeLog> exportDataList = new ArrayList<DataExchangeLog>();
    
    for (int i = 0; i < billLst.size(); i++) {
      ZcXmcgHt ht=(ZcXmcgHt)billLst.get(i);
      String recordID = ht.getZcHtCode();
      String successInfo = "导出成功";
      this.successRecordMap.put(recordID, getDataExchangeRedo(recordID));

      log = new DataExchangeLog();

      this.makeDataExchangeLog(log, meta.getSvUserID(), successInfo, "", "", EXPORT, ht);

      exportDataList.add(log);
    }
    this.getExchangeDataLogModel().setExportDataList(exportDataList);
  }
  private void makeDataExchangeLog(DataExchangeLog log, String userID, String succFail, String exceptionMsg, String detail, String type, ZcXmcgHt atc) {

    log.setDataTypeID(this.getDataTypeID());

    log.setDataTypeName(this.getDataTypeName());

    log.setUserID(userID);

    log.setRecStatus(succFail);

    log.setDetailInfo(detail);

    log.setExceptText(exceptionMsg);

    log.setGentType(type);

    log.setOptDateTime(new Date());

    log.setRecSrcID(atc.getZcHtCode());

    log.setRecSrcName(atc.getZcHtName());

    log.setRecSrcTab("ZC_XMCG_HT");

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
      info="没有合同数据可以导入";
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
    
  //导入合同业务数据
    List htDataLst=(List) map.get(this.XMCGHT);
    
    if(htDataLst==null || htDataLst.size()==0)return 0;
    
    importHt(meta, htDataLst);
    
  //导入工作流数据  
    importWfinfos(meta);

    return htDataLst.size();
  }

  /**
   * 导入合同业务数
   * @param meta
   * @param htDataLst
   */
  private void importHt(RequestMeta meta, List htDataLst) {
    String info;
    DataExchangeLog log;
    List<DataExchangeLog> importDataList = new ArrayList<DataExchangeLog>();
    IZcXmcgHtServiceDelegate htService=(IZcXmcgHtServiceDelegate) ServiceFactory.create(IZcXmcgHtServiceDelegate.class,"zcXmcgHtServiceDelegate");
    
    if(htDataLst!=null && htDataLst.size()>0){
      for (int i = 0; i < htDataLst.size(); i++) {

        DataExchangeListPanel.setProgressText(getDataTypeName() + "正在将采购合同发送到服务器..." + (i + 1) + "/" + htDataLst.size());

        ZcXmcgHt bill = (ZcXmcgHt) htDataLst.get(i);

        info = htService.importTransDatasFN(bill, meta);

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
