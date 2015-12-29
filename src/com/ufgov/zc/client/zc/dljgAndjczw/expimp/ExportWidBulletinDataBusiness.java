package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;

public class ExportWidBulletinDataBusiness extends AbstractExpDataBusiness implements IExpDataBusiness {
  public final String fileName = "widBulletinList";

  public final String asFileName = "asFile";

  private List<String> widBulletinIdIds = null;

  public ExportWidBulletinDataBusiness() {

  }

  public ExportWidBulletinDataBusiness(List<String> widBulletinIdIds, String path, StringBuffer log) {
    init(widBulletinIdIds, path, log);
  }

  @Override
  public void init(List<String> list, String path, StringBuffer log) {
    // TCJLODO Auto-generated method stub
    this.widBulletinIdIds = list;
    this.path = path;
    busName = "中标公告";
    this.log = log;
  }

  @Override
  public void exportData() {
    if (widBulletinIdIds.size() == 0)

      return;
    String path = getDir();
    businessData = dljgJczwDataExpDelegate.getZcEbBulletin(widBulletinIdIds, requestMeta);

    saveAsXml(businessData, path, fileName);
    log.append("中标公告 导出完毕\n");
    List asFileList = dljgJczwDataExpDelegate.getAsFile(getZcEbBulletinAsFile(), requestMeta);

    saveAsXml(asFileList, path, asFileName);
    log.append("中标公告附件  导出完毕\n");
    exportWorkFlow();
  }

  private List getZcEbBulletinAsFile() {

    // TCJLODO Auto-generated method stub

    List<String> fileIds = new ArrayList<String>();

    for (Iterator iterator = businessData.iterator(); iterator.hasNext();) {

      ZcEbBulletin zbbulletin = (ZcEbBulletin) iterator.next();

      if (zbbulletin.getFileID() != null && !"".equals(zbbulletin.getFileID())) {

        fileIds.add(zbbulletin.getFileID());

      }
    }

    return fileIds;

  }

  @Override
  public String getBusName() {
    // TCJLODO Auto-generated method stub
    return busName;
  }

  @Override
  public List<ZcBaseBill> getBusinessData() {
    return businessData;
  }

  @Override
  public String getDataType() {
    // TCJLODO Auto-generated method stub
    return ZcDelegJczwDataExchange.DATA_TYPE_ZC_EB_BULLETIN_WID;
  }

}
