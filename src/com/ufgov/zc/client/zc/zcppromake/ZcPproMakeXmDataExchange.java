/**
 * 
 */
package com.ufgov.zc.client.zc.zcppromake;

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
import com.ufgov.zc.common.zc.model.ApArticle;
import com.ufgov.zc.common.zc.model.DataExchangeLog;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.model.ZcEbEntrustDetail;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitem;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcPProMakeServiceDelegate;

/**
 * 采购计划数据导入导出执行类
 * @author Administrator
 *
 */
public class ZcPproMakeXmDataExchange extends ABaseData {

  /**
   * 
   */
  private static final long serialVersionUID = -986945972440839877L;

  public transient IZcPProMakeServiceDelegate zcPProMakeServiceDelegate = null;

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#doExportData(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta, java.lang.String)
   */
  @Override
  public int doExportData(ElementConditionDto dto, RequestMeta meta, String saveRootPath) {

    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在查询需要导出的记录...");

    List<String> idLst = new ArrayList<String>();
    if (this.getNeedExportDataRedoList() != null && this.getNeedExportDataRedoList().size() > 0) {
      for (DataExchangeRedo redo : getNeedExportDataRedoList()) {
        idLst.add(redo.getRecordID());
      }

      dto.setPmAdjustCodeList(idLst);

      this.setDataList(this.getZcPProMakeServiceDelegate().queryExportsDatas(dto, meta));

    } else {

      this.setDataList(new ArrayList<ZcPProMake>());

    }

    // 将附件信息存储到map中

    if (this.attachmentDataMap == null) {

      this.attachmentDataMap = new HashMap<String, Map<String, AttachmentFile>>();

    } else {

      this.attachmentDataMap.clear();

    }

    DataExchangeLog log = null;

    int total = this.getDataList().size();

    //先处理一下没有对应数据的部分

    for (int i = 0; i < this.getDataList().size(); i++) {

      ZcPProMake atc = (ZcPProMake) this.getDataList().get(i);

      if (atc == null) {

        ZcPProMake tmp = new ZcPProMake();

        String id = this.getNeedExportDataIDList().get(i);

        log = new DataExchangeLog();

        this.makeDataExchangeLog(log, meta.getSvUserID(), "导出失败", "", "未在对应的表中找相应的记录", EXPORT, tmp);

        this.getExchangeDataLogModel().getExportDataList().add(log);

        this.successRecordMap.put(id, getDataExchangeRedo(id));

        total--;

      }

    }

    doClearNullObject();

//    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "查询到" + total + "条有效记录...");

    if (total == 0) {

      this.getDataList().clear();

      return 0;

    }

    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "查询到" + this.getDataList().size() + "条记录...");

    List<DataExchangeLog> exportDataList = new ArrayList<DataExchangeLog>();

    for (int i = 0; i < this.getDataList().size(); i++) {

      String exceptionMsg="";
      String successInfo="导出成功";
      ZcPProMake make = (ZcPProMake) this.getDataList().get(i);
      //获取附件
      try {
        getAttachFile(make, saveRootPath, meta);
      } catch (IOException e) {
        // TCJLODO Auto-generated catch block
        e.printStackTrace();
        exceptionMsg=e.getMessage();
        successInfo="采购计划"+make.getZcMakeCode()+"导出附件失败:";
//        DataExchangeListPanel.setProgressText(this.getDataTypeName() + "采购计划"+make.getZcMakeCode()+"导出附件失败:" + e.getMessage());
      }
      String recordID = make.getZcMakeCode();

      this.successRecordMap.put(recordID, getDataExchangeRedo(recordID));

      log = new DataExchangeLog();

      this.makeDataExchangeLog(log, meta.getSvUserID(), successInfo, exceptionMsg, "", EXPORT, make);

      exportDataList.add(log);

    }

    this.getExchangeDataLogModel().setExportDataList(exportDataList);

    return this.getDataList().size();
  }

  private void getAttachFile(ZcPProMake make, String saveRootPath, RequestMeta meta) throws IOException {
    // TCJLODO Auto-generated method stub
//    String parentDirectory = new File(saveRootPath).getParentFile().getParentFile().getAbsolutePath();

    String makeFileDirName = saveRootPath.substring(saveRootPath.lastIndexOf(File.separator))+File.separator+"attach_files";

    String makeFilePath = saveRootPath + File.separator + "attach_files";

    makeDirs(makeFilePath);

    String path = makeFilePath + File.separator;

    downFile(makeFileDirName, make.getZcImpFileBlobid(), make.getZcImpFile(), make.getZcMakeCode(), path, meta);

    List itemLst = make.getItemList();

    for (Iterator iterator2 = itemLst.iterator(); iterator2.hasNext();) {

      ZcPProMitem d = (ZcPProMitem) iterator2.next();

      downFile(makeFileDirName, d.getZcPitemAttachBlobid(), d.getZcPitemAttach(), "" + d.getZcPitemCode().intValue(), path, meta);

    }

  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#doImportData(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta, java.lang.String)
   */
  @Override
  public int doImportData(ElementConditionDto dto, RequestMeta meta, String readRootPath) {

    String info = null;

    DataExchangeLog log = null;

    List<DataExchangeLog> importDataList = new ArrayList<DataExchangeLog>();

    int size = this.getDataList().size();
    
 // 先进行文件发送和保存，只有保存成功后，才能够往下走
    try {
      toSendFiles(readRootPath, meta);
    } catch (IOException e) {
      // TCJLODO Auto-generated catch block
      e.printStackTrace();
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "上传附件失败...\n" +e.getMessage());
    }
    
    for (int i = 0; i < size; i++) {

      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在发送数据到服务器..." + (i + 1) + "/" + size);

      ZcPProMake make = (ZcPProMake) this.getDataList().get(i);

      info = this.getZcPProMakeServiceDelegate().importTransDatasFN(make, meta);
      //处理丹徒特需要求,没有放在上一步的导入同一处理，是因为中间涉及到了触发器，同一个事务里调用时，触发器未产生，因此提到事务外面处理
      sendToZhuren(make,meta);
           
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);

      log = new DataExchangeLog();

      this.makeDataExchangeLog(log, meta.getSvUserID(), "导入业务数据成功", "", info, IMPORT, make);

      importDataList.add(log);

    }

    this.getExchangeDataLogModel().setImportDataList(importDataList);

    return this.getDataList().size();
  }
  
  /**
   * 丹徒使用：将批办单直接送审到主任审核，
   * 因为采购计划导出到外网后，通过触发器，自动生成已受理的采购任务，草稿状态的批办单。
   * 但为了方便客户使用，将批办单直接送到主任审核，这里没有采用触发器，使用代码，调用工作流引擎，实现送审动作，后续可以考虑改成触发器。
   * 
   */
  private void sendToZhuren(ZcPProMake make, RequestMeta meta) {
    // TCJLODO Auto-generated method stub
    getZcPProMakeServiceDelegate().sendToZhuren(make, meta);
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#getAttachmentDataMap()
   */
  @Override
  public Map<String, Map<String, AttachmentFile>> getAttachmentDataMap() {
    // TCJLODO Auto-generated method stub
    return attachmentDataMap;
  }

  private void makeDataExchangeLog(DataExchangeLog log, String userID, String succFail, String exceptionMsg, String detail, String type,
    ZcPProMake atc) {

    log.setDataTypeID(this.getDataTypeID());

    log.setDataTypeName(this.getDataTypeName());

    log.setUserID(userID);

    log.setRecStatus(succFail);

    log.setDetailInfo(detail);

    log.setExceptText(exceptionMsg);

    log.setGentType(type);

    log.setOptDateTime(new Date());

    log.setRecSrcID(atc.getZcMakeCode());

    log.setRecSrcName(atc.getZcMakeName());

    log.setRecSrcTab("ZC_P_PRO_MAKE");

  }

  public IZcPProMakeServiceDelegate getZcPProMakeServiceDelegate() {

    if (zcPProMakeServiceDelegate == null) {

      zcPProMakeServiceDelegate = (IZcPProMakeServiceDelegate) ServiceFactory.create(IZcPProMakeServiceDelegate.class, "zcPProMakeServiceDelegate");

    }

    return zcPProMakeServiceDelegate;

  }

  public void setZcPProMakeServiceDelegate(IZcPProMakeServiceDelegate zcPProMakeServiceDelegate) {

    this.zcPProMakeServiceDelegate = zcPProMakeServiceDelegate;

  }
}
