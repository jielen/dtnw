/**
 * 
 */
package com.ufgov.zc.client.zc.eval.result;

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
import com.ufgov.zc.common.zc.model.DataExchangeLog;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.model.ZcEbEvalReport;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.model.ZcEbRfqPack;
import com.ufgov.zc.common.zc.model.ZcEbSignup;
import com.ufgov.zc.common.zc.model.ZcEbXunJiaBaoJia;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbEvalServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbProjServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbRfqServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbSignupServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbXjbjServiceDelegate;

/**
 * 评标报告数据交换处理类
 * @author Administrator
 *
 */
public class ZcEbEvalReportDataExchange extends ABaseData {
  
//  public transient IZcEbEvalServiceDelegate zcEbEvalServiceDelegate = null;
//  public transient IZcEbRfqServiceDelegate zcEbRfqServiceDelegate = null;
//  private transient IZcEbXjbjServiceDelegate zcebXjbjServiceDelegate =null;
  
  /*public IZcEbXjbjServiceDelegate getZcebXjbjServiceDelegate() {
    if (zcebXjbjServiceDelegate == null) {

      zcebXjbjServiceDelegate = (IZcEbXjbjServiceDelegate) ServiceFactory.create(IZcEbXjbjServiceDelegate.class,
        "zcebXjbjServiceDelegate");
    }

    return zcebXjbjServiceDelegate;
  }*/

 /* public IZcEbRfqServiceDelegate getZcEbRfqServiceDelegate() {
    if (zcEbRfqServiceDelegate == null) {

      zcEbRfqServiceDelegate = (IZcEbRfqServiceDelegate) ServiceFactory.create(IZcEbRfqServiceDelegate.class,
        "zcEbRfqServiceDelegate");
    }

    return zcEbRfqServiceDelegate;
  }*/

  private static final String REPORT = "REPORT";
  private static final String XUNJIABAOJIA = "XUNJIABAOJIA";
  private static final String RFQ = "RFQ";
  private static final String SIGNUP = "SIGNUP";
  private static final String PROJ = "PROJ";
  private static final String EVALINFO = "EVALINFO";


/*  public IZcEbEvalServiceDelegate getZcEbEvalServiceDelegate() {
    if (zcEbEvalServiceDelegate == null) {

      zcEbEvalServiceDelegate = (IZcEbEvalServiceDelegate) ServiceFactory.create(IZcEbEvalServiceDelegate.class,
        "zcEbEvalServiceDelegate");
    }

    return zcEbEvalServiceDelegate;
  }*/

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#doExportData(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta, java.lang.String)
   */
  @Override
  public int doExportData(ElementConditionDto dto, RequestMeta meta, String saveRootPath) {
    // TCJLODO Auto-generated method stub
    // 准备附件列表
    if (this.attachmentDataMap == null) {

      this.attachmentDataMap = new HashMap<String, Map<String, AttachmentFile>>();

    } else {

      this.attachmentDataMap.clear();

    }

    DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在查询需要导出的记录...");

    HashMap dataMap = new HashMap();
      //获取评审报告
      getEvalReportInfo(dto, meta, saveRootPath, dataMap);
      
      List<ZcEbEvalReport> reportLst = (List<ZcEbEvalReport>) dataMap.get(this.REPORT);
      if (reportLst == null || reportLst.size() == 0){
        DataExchangeListPanel.setProgressText("没有数据需要导出");
        return 0;
      }
      
      //获取询价报价信息
      getXunJiaBaoJiaInfo(dto, meta, saveRootPath, dataMap);
      //获取询价开标信息
      getXunJiaOpenInfo(dto, meta, saveRootPath, dataMap);

      //获取非询价开标的数据：供应商报名信息、供应商报价信息、评标结果信息等，目前只支持线下评标报告的模式，在线开标的未测试，涉及内容多，暂不支持
      //获取报名信息
      getSignupInfo(dto, meta, saveRootPath, dataMap);
      //获取项目信息
      getProjInfo(dto, meta, saveRootPath, dataMap);
      
      //获取评标信息
      getEvalInfo(dto, meta, saveRootPath, dataMap);
      
      this.getDataList().clear();
      this.getDataList().add(dataMap);

      
      List<DataExchangeLog> exportDataList = new ArrayList<DataExchangeLog>();
      DataExchangeListPanel.setProgressText(this.getDataTypeName() + "查询到" + reportLst.size() + "条记录...");
      DataExchangeLog log = null;
      for (int i = 0; i < reportLst.size(); i++) {

        ZcEbEvalReport report = (ZcEbEvalReport) reportLst.get(i);

        if (report == null) {

          ZcEbEvalReport tmp = new ZcEbEvalReport();

          String id = this.getNeedExportDataIDList().get(i);

          log = new DataExchangeLog();

          this.makeDataExchangeLog(log, meta.getSvUserID(), "导出失败", "", "未在对应的表中找相应的记录", EXPORT, tmp);

          this.getExchangeDataLogModel().getExportDataList().add(log);

          this.successRecordMap.put(id, getDataExchangeRedo(id));

        } else {
          String recordID = report.getReportCode();
          String successInfo = "导出成功";
          this.successRecordMap.put(recordID, getDataExchangeRedo(recordID));

          log = new DataExchangeLog();

          this.makeDataExchangeLog(log, meta.getSvUserID(), successInfo, "", "", EXPORT, report);

          exportDataList.add(log);
        }

      }

      this.getExchangeDataLogModel().setExportDataList(exportDataList);
      return reportLst.size();
       
  }


  private void getProjInfo(ElementConditionDto dto, RequestMeta meta, String saveRootPath, HashMap dataMap) {
  // TCJLODO Auto-generated method stub
    List<ZcEbEvalReport> reportLst = (List<ZcEbEvalReport>) dataMap.get(this.REPORT);
    if (reportLst == null || reportLst.size() == 0)
      return;
    List packCodeLst = new ArrayList();
    List projCodeLst=new ArrayList();
    for (ZcEbEvalReport report : reportLst) {
      packCodeLst.add(report.getPackCode());
      if(!projCodeLst.contains(report.getProjCode())){
        projCodeLst.add(report.getProjCode());
      }
    }
    dto=new ElementConditionDto();
    dto.setPmAdjustCodeList(projCodeLst);    
      IZcEbProjServiceDelegate projServiceDelegate = (IZcEbProjServiceDelegate) ServiceFactory.create(IZcEbProjServiceDelegate.class,
        "zcEbProjServiceDelegate");
      List projLst = projServiceDelegate.queryExportsDatas(dto, meta);
      if(projLst!=null && projLst.size()>0){
        dataMap.put(PROJ, projLst);
      }
}


  private void getSignupInfo(ElementConditionDto dto, RequestMeta meta, String saveRootPath, HashMap dataMap) {
  // TCJLODO Auto-generated method stub
    List<ZcEbEvalReport> reportLst = (List<ZcEbEvalReport>) dataMap.get(this.REPORT);
    if (reportLst == null || reportLst.size() == 0)
      return;
    List packCodeLst = new ArrayList();
    List projCodeLst=new ArrayList();
    for (ZcEbEvalReport report : reportLst) {
      packCodeLst.add(report.getPackCode());
      if(!projCodeLst.contains(report.getProjCode())){
        projCodeLst.add(report.getProjCode());
      }
    }
    dto=new ElementConditionDto();
    dto.setPmAdjustCodeList(projCodeLst);
    dto.setCodeList(packCodeLst);
    
      IZcEbSignupServiceDelegate signupServiceDelegate = (IZcEbSignupServiceDelegate) ServiceFactory.create(IZcEbSignupServiceDelegate.class,
        "zcEbSignupServiceDelegate");
      List signupLst = signupServiceDelegate.querySignupExportsDatas(dto, meta);
      if(signupLst!=null && signupLst.size()>0){
        dataMap.put(SIGNUP, signupLst);
      }
}


  private void getEvalInfo(ElementConditionDto dto, RequestMeta meta, String saveRootPath, HashMap dataMap) {
  // TCJLODO Auto-generated method stub
    List<ZcEbEvalReport> reportLst = (List<ZcEbEvalReport>) dataMap.get(this.REPORT);
    if (reportLst == null || reportLst.size() == 0)
      return;
    
    List resultLst=new ArrayList();
    IZcEbEvalServiceDelegate zcEbEvalServiceDelegate = (IZcEbEvalServiceDelegate) ServiceFactory.create(IZcEbEvalServiceDelegate.class,"zcEbEvalServiceDelegate");
    for (ZcEbEvalReport report : reportLst) {
      dto=new ElementConditionDto();
      dto.setProjCode(report.getProjCode());
      dto.setPackCode(report.getPackCode());
      List temp=zcEbEvalServiceDelegate.getZcEbPackEvalResultList(dto, meta);
      if(temp!=null){
        resultLst.addAll(temp);
      }
    }
    if(resultLst!=null && resultLst.size()>0){
      dataMap.put(EVALINFO, resultLst);
    }
  
}


  private void makeDataExchangeLog(DataExchangeLog log, String userID, String succFail, String exceptionMsg, String detail, String type, Object atc) {

    log.setDataTypeID(this.getDataTypeID());

    log.setDataTypeName(this.getDataTypeName());

    log.setUserID(userID);

    log.setRecStatus(succFail);

    log.setDetailInfo(detail);

    log.setExceptText(exceptionMsg);

    log.setGentType(type);

    log.setOptDateTime(new Date());

    if(atc instanceof ZcEbEvalReport){
      ZcEbEvalReport tmp=(ZcEbEvalReport)atc;
    log.setRecSrcID(tmp.getReportCode());
    log.setRecSrcName(tmp.getReportTitle());
    }else if(atc instanceof ZcEbXunJiaBaoJia){
      ZcEbXunJiaBaoJia tmp=(ZcEbXunJiaBaoJia)atc;
      log.setRecSrcID(tmp.getBjCode());
      log.setRecSrcName(tmp.getBjCode());
    }

    log.setRecSrcTab("ZC_EB_EVAL_REPORT");

  }


  private void getXunJiaOpenInfo(ElementConditionDto dto, RequestMeta meta, String saveRootPath, HashMap dataMap) {
    // TCJLODO Auto-generated method stub
    List<ZcEbEvalReport> reportLst = (List<ZcEbEvalReport>) dataMap.get(this.REPORT);
    if (reportLst == null || reportLst.size() == 0)
      return;
    List lst=new ArrayList();
    for (ZcEbEvalReport report : reportLst) {
      HashMap hsm=new HashMap();
      hsm.put("PROJ_CODE", report.getProjCode());
      hsm.put("PACK_CODE", report.getPackCode());
      lst.add(hsm);
    }
    IZcEbRfqServiceDelegate zcEbRfqServiceDelegate = (IZcEbRfqServiceDelegate) ServiceFactory.create(IZcEbRfqServiceDelegate.class,
      "zcEbRfqServiceDelegate");
    List rfqLst=zcEbRfqServiceDelegate.queryExportsDatas(lst, meta);
    if(rfqLst!=null && rfqLst.size()>0){
      dataMap.put(RFQ, rfqLst);
    }
  }

  private void getXunJiaBaoJiaInfo(ElementConditionDto dto, RequestMeta meta, String saveRootPath, HashMap dataMap) {
    // TCJLODO Auto-generated method stub
    List<ZcEbEvalReport> reportLst = (List<ZcEbEvalReport>) dataMap.get(this.REPORT);
    if (reportLst == null || reportLst.size() == 0)
      return;
    List reportIdLst = new ArrayList();
    for (ZcEbEvalReport report : reportLst) {
      reportIdLst.add(report.getReportCode());
    }
    if (reportIdLst.size() > 0) {
      IZcEbXjbjServiceDelegate zcebXjbjServiceDelegate = (IZcEbXjbjServiceDelegate) ServiceFactory.create(IZcEbXjbjServiceDelegate.class,
        "zcebXjbjServiceDelegate");
      List rfqLst = zcebXjbjServiceDelegate.queryXjBjExportsDatas(reportIdLst, meta);
      if(rfqLst!=null && rfqLst.size()>0){
        dataMap.put(XUNJIABAOJIA, rfqLst);
      }
    }
    
  }

  private void getEvalReportInfo(ElementConditionDto dto, RequestMeta meta, String saveRootPath, HashMap dataMap) {
    // TCJLODO Auto-generated method stub
    List<String> idLst = new ArrayList<String>();
    if (this.getNeedExportDataRedoList() != null && this.getNeedExportDataRedoList().size() > 0) {
      for (DataExchangeRedo redo : getNeedExportDataRedoList()) {
        idLst.add(redo.getRecordID());
      }
      dto.setPmAdjustCodeList(idLst);
      IZcEbEvalServiceDelegate zcEbEvalServiceDelegate = (IZcEbEvalServiceDelegate) ServiceFactory.create(IZcEbEvalServiceDelegate.class,
        "zcEbEvalServiceDelegate");
      List reportLst = zcEbEvalServiceDelegate.queryExportsDatas(dto, meta);
      if(reportLst==null || reportLst.size()==0){
        return;
      }
      dataMap.put(REPORT, reportLst);
      //获取附件
      String exceptionMsg="";
      String successInfo="导出成功";
      for(int i=0;i<reportLst.size();i++){
        ZcEbEvalReport report=(ZcEbEvalReport)reportLst.get(i);
        try {
          getAttachFile(report, saveRootPath, meta);
        } catch (IOException e) {
          // TCJLODO Auto-generated catch block
          e.printStackTrace();
          exceptionMsg=e.getMessage();
          successInfo="评标报告"+report.getReportCode()+"导出附件失败:";
//          DataExchangeListPanel.setProgressText(this.getDataTypeName() + "采购计划"+make.getZcMakeCode()+"导出附件失败:" + e.getMessage());
        }
      }
    }

  }
  private void getAttachFile(ZcEbEvalReport report, String saveRootPath, RequestMeta meta) throws IOException {
    // TCJLODO Auto-generated method stub
//    String parentDirectory = new File(saveRootPath).getParentFile().getParentFile().getAbsolutePath();

    String makeFileDirName = saveRootPath.substring(saveRootPath.lastIndexOf(File.separator))+File.separator+"attach_files";

    String makeFilePath = saveRootPath + File.separator + "attach_files";

    makeDirs(makeFilePath);

    String path = makeFilePath + File.separator;

    downFile(makeFileDirName, report.getReportAttachBlobid(), report.getReportAttach(), report.getReportCode(), path, meta);

  }
  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.dataexchange.model.ABaseData#doImportData(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta, java.lang.String)
   */
  @Override
  public int doImportData(ElementConditionDto dto, RequestMeta meta, String readRootPath) {
    // TCJLODO Auto-generated method stub

    String info = null;

    DataExchangeLog log = null;

    List<DataExchangeLog> importDataList = new ArrayList<DataExchangeLog>();

    if(getDataList()==null || getDataList().size()==0){
      info="没有评标报告数据导入";
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
    List dataLst=(List) map.get(this.REPORT);
    if(dataLst!=null && dataLst.size()>0){
      IZcEbEvalServiceDelegate zcEbEvalServiceDelegate = (IZcEbEvalServiceDelegate) ServiceFactory.create(IZcEbEvalServiceDelegate.class,
        "zcEbEvalServiceDelegate");
      for (int i = 0; i < dataLst.size(); i++) {

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在将评标报告发送到服务器..." + (i + 1) + "/" + dataLst.size());

        ZcEbEvalReport bill = (ZcEbEvalReport) dataLst.get(i);

        info = zcEbEvalServiceDelegate.importTransDatasFN(bill, meta);

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);

        log = new DataExchangeLog();

        this.makeDataExchangeLog(log, meta.getSvUserID(), "导入业务数据成功", "", info, IMPORT, bill);

        importDataList.add(log);

      }
    }
    dataLst=(List) map.get(this.XUNJIABAOJIA);
    if(dataLst!=null && dataLst.size()>0){
      IZcEbXjbjServiceDelegate zcebXjbjServiceDelegate = (IZcEbXjbjServiceDelegate) ServiceFactory.create(IZcEbXjbjServiceDelegate.class,
        "zcebXjbjServiceDelegate");
      for (int i = 0; i < dataLst.size(); i++) {

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在将询价报价数据发送到服务器..." + (i + 1) + "/" + dataLst.size());

        ZcEbXunJiaBaoJia bill = (ZcEbXunJiaBaoJia) dataLst.get(i);

        info = zcebXjbjServiceDelegate.importXjBjDataFN(bill, meta);

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);

        log = new DataExchangeLog();

        this.makeDataExchangeLog(log, meta.getSvUserID(), "导入业务数据成功", "", info, IMPORT, bill);

        importDataList.add(log);

      }
    }
    dataLst=(List) map.get(this.RFQ);
    if(dataLst!=null && dataLst.size()>0){
      IZcEbRfqServiceDelegate zcEbRfqServiceDelegate = (IZcEbRfqServiceDelegate) ServiceFactory.create(IZcEbRfqServiceDelegate.class,
        "zcEbRfqServiceDelegate");
      for (int i = 0; i < dataLst.size(); i++) {

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在将询价开标数据发送到服务器..." + (i + 1) + "/" + dataLst.size());

        ZcEbRfqPack bill = (ZcEbRfqPack) dataLst.get(i);

        info =zcEbRfqServiceDelegate.importRfqDataFN(bill, meta);

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);

        log = new DataExchangeLog();

        this.makeDataExchangeLog(log, meta.getSvUserID(), "导入业务数据成功", "", info, IMPORT, bill);

        importDataList.add(log);

      }
    }

    dataLst=(List) map.get(this.SIGNUP);
    if(dataLst!=null && dataLst.size()>0){
      IZcEbSignupServiceDelegate signupServiceDelegate = (IZcEbSignupServiceDelegate) ServiceFactory.create(IZcEbSignupServiceDelegate.class,
        "zcEbSignupServiceDelegate");
      for (int i = 0; i < dataLst.size(); i++) {

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在将供应商报名数据发送到服务器..." + (i + 1) + "/" + dataLst.size());

        ZcEbSignup bill = (ZcEbSignup) dataLst.get(i);

        info =signupServiceDelegate.importSignupDataFN(bill, meta);

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);

        log = new DataExchangeLog();

        this.makeDataExchangeLog(log, meta.getSvUserID(), "导入业务数据成功", "", info, IMPORT, bill);

        importDataList.add(log);

        
      }
    }

    dataLst=(List) map.get(this.PROJ);
    if(dataLst!=null && dataLst.size()>0){   
      IZcEbProjServiceDelegate projServiceDelegate = (IZcEbProjServiceDelegate) ServiceFactory.create(IZcEbProjServiceDelegate.class,
        "zcEbProjServiceDelegate");
      for (int i = 0; i < dataLst.size(); i++) {

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + "正在将招标项目数据发送到服务器..." + (i + 1) + "/" + dataLst.size());

        ZcEbProj bill = (ZcEbProj) dataLst.get(i);

        info =projServiceDelegate.importTransDatasFN(bill, meta);

        DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);

        log = new DataExchangeLog();

        this.makeDataExchangeLog(log, meta.getSvUserID(), "导入业务数据成功", "", info, IMPORT, bill);

        importDataList.add(log);

        
      }
    }
    dataLst=(List) map.get(this.EVALINFO);
    if(dataLst!=null && dataLst.size()>0){ 
      
      IZcEbEvalServiceDelegate zcEbEvalServiceDelegate = (IZcEbEvalServiceDelegate) ServiceFactory.create(IZcEbEvalServiceDelegate.class,"zcEbEvalServiceDelegate");
      
     info =zcEbEvalServiceDelegate.importEvalResultInfoFN(dataLst, meta);

     DataExchangeListPanel.setProgressText(this.getDataTypeName() + info);
        
      
    }
    return getDataList().size();
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
