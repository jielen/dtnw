package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.model.ZcEbEvalReport;
import com.ufgov.zc.common.zc.model.ZcEbSignup;

public class ExportEvalReportDataBusiness extends AbstractExpDataBusiness implements IExpDataBusiness {
  public final String fileName = "zcEbEvalReport";

  public final String sigupFileName = "zcEbSignup";

  public final String sigupPackFileName = "signupPackList";

  public final String packEvalFileName = "zcEbPackEvalResult";

  public final String asFileName = "asFile";

  private List<String> zcEbEvalReportIds = null;

  public ExportEvalReportDataBusiness() {

  }

  public ExportEvalReportDataBusiness(List<String> zcEbEvalReportIds, String path, StringBuffer log) {
    init(zcEbEvalReportIds, path, log);
  }

  @Override
  public void init(List<String> list, String path, StringBuffer log) {
    // TODO Auto-generated method stub
    this.zcEbEvalReportIds = list;
    busName = "评审报告";
    this.path = path;
    this.log = log;
  }

  @Override
  public void exportData() {
    if (zcEbEvalReportIds.size() == 0)

      return;
    String path = getDir();
    businessData = dljgJczwDataExpDelegate.getZcEbEvalReport(zcEbEvalReportIds, requestMeta);

    saveAsXml(businessData, path, fileName);

    log.append("评审报告 表导出完毕\n");

    List projCodes = getProjectCodes();

    List zcEbSignupList = dljgJczwDataExpDelegate.getZcEbSignupData(projCodes, requestMeta);

    saveAsXml(zcEbSignupList, path, sigupFileName);

    log.append("报名 表导出完毕\n");

    List signupIds = getSignupIds(zcEbSignupList);

    List zcEbSignupPackList = dljgJczwDataExpDelegate.getZcEbSignupPackDetailData(signupIds, requestMeta);

    saveAsXml(zcEbSignupPackList, path, sigupPackFileName);

    log.append("报名分包 表导出完毕\n");

    List packCodes = getPackCodes();

    List zcEbPackEvalResultList = dljgJczwDataExpDelegate.getZcEbPackEvalResult(packCodes, requestMeta);

    saveAsXml(zcEbPackEvalResultList, path, packEvalFileName);

    log.append("专家评分汇总 表导出完毕\n");

    List fileIds = getZcEbEvalReportAsFile();

    List asFileList = dljgJczwDataExpDelegate.getAsFile(fileIds, requestMeta);

    saveAsXml(asFileList, path, asFileName);

    log.append("评审报告附件  表导出完毕\n");
    exportWorkFlow();
  }

  private List getZcEbBulletinAsFile() {

    // TODO Auto-generated method stub

    List<String> fileIds = new ArrayList<String>();

    for (Iterator iterator = businessData.iterator(); iterator.hasNext();) {

      ZcEbBulletin zbbulletin = (ZcEbBulletin) iterator.next();

      if (zbbulletin.getFileID() != null && !"".equals(zbbulletin.getFileID())) {

        fileIds.add(zbbulletin.getFileID());

      }
    }

    return fileIds;

  }

  private List getProjectCodes() {

    Set<String> projCodes = new HashSet<String>();

    for (Iterator iterator = businessData.iterator(); iterator.hasNext();) {

      ZcEbEvalReport rep = (ZcEbEvalReport) iterator.next();

      projCodes.add(rep.getProjCode());

    }

    return new ArrayList(projCodes);

  }

  private List getSignupIds(List zcEbSigup) {

    List<String> keyIdsList = new ArrayList<String>();

    for (Iterator iterator = zcEbSigup.iterator(); iterator.hasNext();) {

      ZcEbSignup pr = (ZcEbSignup) iterator.next();

      keyIdsList.add(pr.getSignupId());

    }

    return keyIdsList;

  }

  private List getPackCodes() {

    Set<String> packCodes = new HashSet<String>();

    for (Iterator iterator = businessData.iterator(); iterator.hasNext();) {

      ZcEbEvalReport rep = (ZcEbEvalReport) iterator.next();

      packCodes.add(rep.getPackCode());

    }

    return new ArrayList(packCodes);

  }

  private List getZcEbEvalReportAsFile() {

    List<String> fileIds = new ArrayList<String>();

    for (Iterator iterator = businessData.iterator(); iterator.hasNext();) {

      ZcEbEvalReport report = (ZcEbEvalReport) iterator.next();

      if (report.getSignReportAttachBlobid() != null && !"".equals(report.getSignReportAttachBlobid())) {

        fileIds.add(report.getSignReportAttachBlobid());

      }

      if (report.getReportAttachBlobid() != null && !"".equals(report.getReportAttachBlobid())) {

        fileIds.add(report.getReportAttachBlobid());

      }

    }

    return fileIds;

  }

  @Override
  public String getBusName() {
    // TODO Auto-generated method stub
    return busName;
  }

  @Override
  public List<ZcBaseBill> getBusinessData() {
    return businessData;
  }

  @Override
  public String getDataType() {
    // TODO Auto-generated method stub
    return ZcDelegJczwDataExchange.DATA_TYPE_ZC_EB_EVAL_REPORT;
  }

}
