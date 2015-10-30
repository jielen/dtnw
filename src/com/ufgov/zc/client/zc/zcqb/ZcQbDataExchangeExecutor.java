/**
 * 
 */
package com.ufgov.zc.client.zc.zcqb;

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
import com.ufgov.zc.common.zc.model.ZcQb;
import com.ufgov.zc.common.zc.model.ZcQx;
import com.ufgov.zc.common.zc.publish.IZcQbServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcQxServiceDelegate;

/**
 * 汽保数据导入导出执行类
 * @author Administrator
 *
 */
public class ZcQbDataExchangeExecutor extends ABaseData {

  /**
   * 
   */
  private static final long serialVersionUID = -8920662835791028727L;
  private static final String ZCQB = "ZCQB";
  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#doExportData(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  public int doExportData(ElementConditionDto dto, RequestMeta meta, String saveRootPath) {
    // TODO Auto-generated method stub
    
    int rtn=0;
    this.getDataList().clear();    
    //获取汽车保险及相关数据
    HashMap dataMap = new HashMap();
    List qbLst=getQbDatas(dto,meta); 
    if(getDelDataIdlst()!=null){
      dataMap.put(ZcBaseBill.OPERTATE_TYPE_DEL, getDelDataIdlst());
      rtn+=getDelDataIdlst().size();
    }
    if(qbLst!=null){
      
      //汽车保险业务数据
      dataMap.put(this.ZCQB, qbLst); 
      
      //工作流审批信息
      List<String> processInstIdLst=new ArrayList<String>();
      for(int i=0;i<qbLst.size();i++){
        ZcBaseBill bill=(ZcBaseBill)qbLst.get(i);
        processInstIdLst.add(""+bill.getProcessInstId());
      }      
      exportWFinfos(dataMap,processInstIdLst,meta);
      
      //获取汽车保险附件      
      getQbFiles(meta, saveRootPath, qbLst);      
      
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
  private void getQbFiles(RequestMeta meta, String saveRootPath, List htLst) {

  }

  private List getQbDatas(ElementConditionDto dto, RequestMeta meta) {
    // TODO Auto-generated method stub

    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在查询需要导出的记录...");
    List<String> idLst = new ArrayList<String>();
    List<ZcQb> billLst=new ArrayList<ZcQb>();
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
      IZcQbServiceDelegate qbService=(IZcQbServiceDelegate) ServiceFactory.create(IZcQbServiceDelegate.class,"zcQbServiceDelegate");
      billLst = qbService.queryExportsDatas(dto, meta);
    }
    createExportLog(idLst,billLst,meta);
    return billLst;
  }

  private void createExportLog(List<String> idLst, List<ZcQb> billLst,RequestMeta meta) {
    // TODO Auto-generated method stub
    if(billLst==null || billLst.size()==0){
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "业务数据中没有查到对应的业务数据...");
    }else{
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "导出"+billLst.size()+"条业务数据...");
    }
    
    DataExchangeLog log=null;
    List<DataExchangeLog> exportDataList = new ArrayList<DataExchangeLog>();
    
    for (int i = 0; i < billLst.size(); i++) {
      ZcQb qb=(ZcQb)billLst.get(i);
      String recordID = qb.getQbCode();
      String successInfo = "导出成功";
      this.successRecordMap.put(recordID, getDataExchangeRedo(recordID));

      log = new DataExchangeLog();

      this.makeDataExchangeLog(log, meta.getSvUserID(), successInfo, "", "", EXPORT, qb);

      exportDataList.add(log);
    }
    this.getExchangeDataLogModel().setExportDataList(exportDataList);
  }
  private void makeDataExchangeLog(DataExchangeLog log, String userID, String succFail, String exceptionMsg, String detail, String type, ZcQb atc) {

    log.setDataTypeID(this.getDataTypeID());

    log.setDataTypeName(this.getDataTypeName());

    log.setUserID(userID);

    log.setRecStatus(succFail);

    log.setDetailInfo(detail);

    log.setExceptText(exceptionMsg);

    log.setGentType(type);

    log.setOptDateTime(new Date());

    log.setRecSrcID(atc.getQbCode());

    log.setRecSrcName(atc.getQbName());

    log.setRecSrcTab("ZC_QB");

  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#doImportData(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta, java.lang.String)
   */
  @Override
  public int doImportData(ElementConditionDto dto, RequestMeta meta, String readRootPath) {
    // TODO Auto-generated method stub

    String info = null;
    DataExchangeLog log = null;
    
    if(getDataList()==null || getDataList().size()==0){
      info="没有汽车保险数据可以导入";
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);
      return 0;
    }

    // 先进行文件发送和保存，只有保存成功后，才能够往下走
    try {
      toSendFiles(readRootPath, meta);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "上传附件失败...\n" + e.getMessage());
    }

    
    HashMap map=(HashMap) getDataList().get(0);
    
    //同步删除数据
    List delDataLst=(List)map.get(ZcBaseBill.OPERTATE_TYPE_DEL);
    if(delDataLst!=null && delDataLst.size()>0){
      importDelData(meta,delDataLst);
    }
  //导入汽车保险业务数据
    List htDataLst=(List) map.get(this.ZCQB);
    
    if(htDataLst==null || htDataLst.size()==0)return 0;
    
    importQb(meta, htDataLst);
    
  //导入工作流数据  
    importWfinfos(meta);

    return htDataLst.size();
  }
//同步删除数据
  private void importDelData(RequestMeta meta,List delDataLst) {
    // TODO Auto-generated method stub
    String info=null;
    DataExchangeLog log;
    List<DataExchangeLog> importDataList = new ArrayList<DataExchangeLog>();
    
    if(delDataLst!=null && delDataLst.size()>0){
      IZcQbServiceDelegate qbService=(IZcQbServiceDelegate) ServiceFactory.create(IZcQbServiceDelegate.class,"zcQbServiceDelegate");
      
      for (int i = 0; i < delDataLst.size(); i++) {

        log = new DataExchangeLog();
        DataExchangeListPanel.setProgressText(getDataTypeName() + "正在将删除的汽车维修信息发送到服务器..." + (i + 1) + "/" + delDataLst.size());

        String  id = (String) delDataLst.get(i);
        ZcQb qx=new ZcQb();
        qx.setQbCode(id);
        try {
          info = qbService.importDelDataFN(id, meta);
        } catch (DataExchangeException e) {
          // TODO: handle exception
          info=e.getMessage();
          this.makeDataExchangeLog(log, meta.getSvUserID(), "同步删除汽车维修数据失败", "", info, IMPORT, qx);
          qx=qbService.selectByPrimaryKey(id, meta);
          cantDelinstanceIdLst.add(""+qx.getProcessInstId());
          continue;
        }

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);


        this.makeDataExchangeLog(log, meta.getSvUserID(), "同步删除汽车维修数据成功", "", info, IMPORT, qx);

        importDataList.add(log);
      }      
    }

    this.getExchangeDataLogModel().setImportDataList(importDataList);
    
  }
  /**
   * 导入汽车保险业务数
   * @param meta
   * @param htDataLst
   */
  private void importQb(RequestMeta meta, List htDataLst) {
    String info;
    DataExchangeLog log;
    List<DataExchangeLog> importDataList = new ArrayList<DataExchangeLog>();
    IZcQbServiceDelegate qbService=(IZcQbServiceDelegate) ServiceFactory.create(IZcQbServiceDelegate.class,"zcQbServiceDelegate");
    
    if(htDataLst!=null && htDataLst.size()>0){
      for (int i = 0; i < htDataLst.size(); i++) {

        DataExchangeListPanel.setProgressText(getDataTypeName() + "正在将汽车保险发送到服务器..." + (i + 1) + "/" + htDataLst.size());

        ZcQb bill = (ZcQb) htDataLst.get(i);

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
    // TODO Auto-generated method stub
    return attachmentDataMap;
  }

}
