package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;
import com.ufgov.zc.common.zc.model.ZcEbProtocol;

public class ExportProtocolDataBusiness extends AbstractExpDataBusiness implements IExpDataBusiness {
  public final String fileName = "zcEbProtocol";

  public final String asFileName = "asFile";

  private List<String> protocolIds = null;

  public ExportProtocolDataBusiness() {
  }

  public ExportProtocolDataBusiness(List<String> protocolIds, String path, StringBuffer log) {
    init(protocolIds, path, log);
  }

  @Override
  public void init(List<String> list, String path, StringBuffer log) {
    // TCJLODO Auto-generated method stub
    this.protocolIds = list;
    this.path = path;
    busName = "委托协议";
    this.log = log;
  }

  @Override
  public void exportData() {
    if (protocolIds.size() == 0)

      return;
    String path = getDir();
    businessData = dljgJczwDataExpDelegate.getZcEbProtocol(protocolIds, requestMeta);

    saveAsXml(businessData, path, fileName);
    log.append("委托协议 表导出完毕\n");
    List asFileList = dljgJczwDataExpDelegate.getAsFile(getZcEbProtcolAsFile(), requestMeta);

    saveAsXml(asFileList, path, asFileName);
    log.append("委托协议附件 表导出完毕\n");
    exportWorkFlow();
  }

  private List getZcEbProtcolAsFile() {

    List<String> fileIds = new ArrayList<String>();

    for (Iterator iterator = businessData.iterator(); iterator.hasNext();) {

      ZcEbProtocol pc = (ZcEbProtocol) iterator.next();

      if (pc.getProtFileBlobid() != null && !"".equals(pc.getProtFileBlobid())) {

        fileIds.add(pc.getProtFileBlobid());

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
    return ZcDelegJczwDataExchange.DATA_TYPE_ZC_EB_PROTOCOL;
  }

}
