/**
 * 
 */
package com.ufgov.zc.client.zc.zcpprobal;

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
import com.ufgov.zc.common.zc.model.ZcPProBal;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.publish.IZcPProBalServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcXmcgHtServiceDelegate;

/**
 * @author Administrator
 *
 */
public class ZcPProBalDataExchangeExecutor extends ABaseData {

  /**
   * 
   */
  private static final long serialVersionUID = 7047904308644455760L;

  private static final String ZCPPROBAL = "ZCPPROBAL";

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#doExportData(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta, java.lang.String)
   */
  @Override
  public int doExportData(ElementConditionDto dto, RequestMeta meta, String saveRootPath) {
    // TCJLODO Auto-generated method stub

    // TCJLODO Auto-generated method stub
    
    int rtn=0;
    this.getDataList().clear();    
    //获取合同及相关数据
    HashMap dataMap = new HashMap();
    List balLst=getBalDatas(dto,meta);

    if(getDelDataIdlst()!=null){
      dataMap.put(ZcBaseBill.OPERTATE_TYPE_DEL, getDelDataIdlst());
      rtn+=getDelDataIdlst().size();
    }
    
    if(balLst!=null){
      
      //合同业务数据
      dataMap.put(this.ZCPPROBAL, balLst); 
      
      //工作流审批信息
      List<String> processInstIdLst=new ArrayList<String>();
      for(int i=0;i<balLst.size();i++){
        ZcBaseBill bill=(ZcBaseBill)balLst.get(i);
        processInstIdLst.add(""+bill.getProcessInstId());
      }      
      exportWFinfos(dataMap,processInstIdLst,meta);
      
      //获取资金支付      
      getBalAttachFiles(meta, saveRootPath, balLst);      
      
      rtn+=balLst.size();      
    }  
    if(dataMap.size()>0){
      this.getDataList().add(dataMap);
    }
    return rtn;
  }

  /**
   * @param meta
   * @param saveRootPath
   * @param balLst
   */
  private void getBalAttachFiles(RequestMeta meta, String saveRootPath, List balLst) {
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
    
    for (int i = 0; i < balLst.size(); i++) {
      ZcPProBal f = (ZcPProBal) balLst.get(i);
      try {
        downFile(makeFileDirName, f.getZcConTextBlobId(), f.getZcConText(), null, path, meta);
        downFile(makeFileDirName, f.getZcImpFileBlobId(), f.getZcImpFile(), null, path, meta);
      } catch (IOException e) {
        // TCJLODO Auto-generated catch block
        e.printStackTrace();
        DataExchangeListPanel.setProgressText(this.getDataTypeName() + "获取资金支付附件出错...\n" + e.getMessage());
      }

    }
  }
  private List getBalDatas(ElementConditionDto dto, RequestMeta meta) {
    // TCJLODO Auto-generated method stub

    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在查询需要导出的记录...");
    List<String> idLst = new ArrayList<String>();
    List<ZcPProBal> billLst=new ArrayList<ZcPProBal>();
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
      IZcPProBalServiceDelegate balService=(IZcPProBalServiceDelegate) ServiceFactory.create(IZcPProBalServiceDelegate.class,"zcPProBalServiceDelegate");
      billLst = balService.queryExportsDatas(dto, meta);
    }
    createExportLog(idLst,billLst,meta);
    return billLst;
  }

  private void createExportLog(List<String> idLst, List<ZcPProBal> billLst,RequestMeta meta) {
    // TCJLODO Auto-generated method stub
    if(billLst==null || billLst.size()==0){
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "业务数据中没有查到对应的业务数据...");
    }else{
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "导出"+billLst.size()+"条业务数据...");
    }
    
    DataExchangeLog log=null;
    List<DataExchangeLog> exportDataList = new ArrayList<DataExchangeLog>();
    
    for (int i = 0; i < billLst.size(); i++) {
      ZcPProBal bal=(ZcPProBal)billLst.get(i);
      String recordID = bal.getZcBalId();
      String successInfo = "导出成功";
      this.successRecordMap.put(recordID, getDataExchangeRedo(recordID));

      log = new DataExchangeLog();

      this.makeDataExchangeLog(log, meta.getSvUserID(), successInfo, "", "", EXPORT, bal);

      exportDataList.add(log);
    }
    this.getExchangeDataLogModel().setExportDataList(exportDataList);
  }

  private void makeDataExchangeLog(DataExchangeLog log, String userID, String succFail, String exceptionMsg, String detail, String type, ZcPProBal atc) {

    log.setDataTypeID(this.getDataTypeID());

    log.setDataTypeName(this.getDataTypeName());

    log.setUserID(userID);

    log.setRecStatus(succFail);

    log.setDetailInfo(detail);

    log.setExceptText(exceptionMsg);

    log.setGentType(type);

    log.setOptDateTime(new Date());

    log.setRecSrcID(atc.getZcBalId());

    log.setRecSrcName(atc.getZcHtName());

    log.setRecSrcTab("ZC_P_PRO_BAL");

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
      info="没有资金支付可以导入";
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
  //导入资金支付业务数据
    List htDataLst=(List) map.get(this.ZCPPROBAL);
    
    if(htDataLst==null || htDataLst.size()==0)return 0;
    
    importHt(meta, htDataLst);
    
  //导入工作流数据  
    importWfinfos(meta);

    return htDataLst.size();
  }
//同步删除数据
  /**
   * 
   * @param meta
   * @param delDataLst
   * @param cantDelIds 删除异常的时候出现异常的id集合
   */
  private void importDelData(RequestMeta meta,List delDataLst) {
    // TCJLODO Auto-generated method stub
    String info=null;
    DataExchangeLog log;
    List<DataExchangeLog> importDataList = new ArrayList<DataExchangeLog>();
    
    if(delDataLst!=null && delDataLst.size()>0){
      
      IZcPProBalServiceDelegate balService=(IZcPProBalServiceDelegate) ServiceFactory.create(IZcPProBalServiceDelegate.class,"zcPProBalServiceDelegate");
      
      for (int i = 0; i < delDataLst.size(); i++) {

        log = new DataExchangeLog();
        DataExchangeListPanel.setProgressText(getDataTypeName() + "正在将删除的资金支付信息发送到服务器..." + (i + 1) + "/" + delDataLst.size());

        String  id = (String) delDataLst.get(i);
        ZcPProBal bal=new ZcPProBal();
        bal.setZcBalId(id);
        try {
          info = balService.importDelDataFN(id, meta);
        } catch (DataExchangeException e) {
          // TCJLODO: handle exception
          info=e.getMessage();
          DataExchangeListPanel.setProgressText(getDataTypeName() + "同步删除资金支付数据失败," + info);
          this.makeDataExchangeLog(log, meta.getSvUserID(), "同步删除资金支付数据失败", "", info, IMPORT, bal);
          bal=balService.selectByPrimaryKey(id, null, meta);
          cantDelinstanceIdLst.add(""+bal.getProcessInstId());
          continue;
        }

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);


        this.makeDataExchangeLog(log, meta.getSvUserID(), "同步删除资金支付数据成功", "", info, IMPORT, bal);

        importDataList.add(log);
      }      
    }

    this.getExchangeDataLogModel().setImportDataList(importDataList);
    
  }

  /**
   * 导入资金支付业务数据
   * @param meta
   * @param htDataLst
   */
  private void importHt(RequestMeta meta, List htDataLst) {
    String info;
    DataExchangeLog log;
    List<DataExchangeLog> importDataList = new ArrayList<DataExchangeLog>();
    
    if(htDataLst!=null && htDataLst.size()>0){
      
      IZcPProBalServiceDelegate balService=(IZcPProBalServiceDelegate) ServiceFactory.create(IZcPProBalServiceDelegate.class,"zcPProBalServiceDelegate");
      
      for (int i = 0; i < htDataLst.size(); i++) {

        DataExchangeListPanel.setProgressText(getDataTypeName() + "正在将资金支付发送到服务器..." + (i + 1) + "/" + htDataLst.size());

        log = new DataExchangeLog();
        ZcPProBal bill = (ZcPProBal) htDataLst.get(i);

        try {
          info = balService.importTransDatasFN(bill, meta);
        } catch (DataExchangeException e) {
          // TCJLODO: handle exception
          info=e.getMessage();
          DataExchangeListPanel.setProgressText(getDataTypeName() + "导入资金支付数据失败," + info);
          this.makeDataExchangeLog(log, meta.getSvUserID(), "导入资金支付数据失败", "", info, IMPORT, bill);
          continue;
        }

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);


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
