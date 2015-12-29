package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;

public class ExportBidBulletinDataBusiness extends AbstractExpDataBusiness implements IExpDataBusiness {
  public final String fileName = "bidBulletinList";

  public final String asFileName = "asFile";

  private List<String> bidBulletinIdIds = null;

  public ExportBidBulletinDataBusiness() {
  }

  public ExportBidBulletinDataBusiness(List<String> bidBulletinIdIds, String path, StringBuffer log) {
    init(bidBulletinIdIds, path, log);
  }

  @Override
  public void init(List<String> list, String path, StringBuffer log) {
    // TCJLODO Auto-generated method stub
    this.bidBulletinIdIds = list;
    this.path = path;
    busName = "招标公告";
    this.log = log;
  }

  @Override
  public void exportData() {
    if (bidBulletinIdIds.size() == 0)

      return;
    String path = getDir();
    businessData = dljgJczwDataExpDelegate.getZcEbBulletin(bidBulletinIdIds, requestMeta);

    saveAsXml(businessData, path, fileName);
    log.append("招标公告 表导出完毕\n");
    List asFileList = dljgJczwDataExpDelegate.getAsFile(getZcEbBulletinAsFile(), requestMeta);

    saveAsXml(asFileList, path, asFileName);
    log.append("招标公告附件 表导出完毕\n");
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
    return ZcDelegJczwDataExchange.DATA_TYPE_ZC_EB_BULLETIN_BID;
  }

}
