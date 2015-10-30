package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.util.List;

import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;

public class ExportAuditSheetDataBusiness extends AbstractExpDataBusiness implements IExpDataBusiness {
  public final String fileName = "zcEbAuditSheet";

  private List<String> sheetIds = null;

  public ExportAuditSheetDataBusiness() {

  }

  public ExportAuditSheetDataBusiness(List<String> sheetIds, String path, StringBuffer log) {
    init(sheetIds, path, log);
  }

  @Override
  public void init(List<String> list, String path, StringBuffer log) {
    // TODO Auto-generated method stub
    this.sheetIds = list;
    this.path = path;
    this.busName = "批办单";
    this.log = log;
  }

  @Override
  public void exportData() {
    if (sheetIds.size() == 0)

      return;
    businessData = dljgJczwDataExpDelegate.getZcEbAuditSheetData(sheetIds, requestMeta);

    saveAsXml(businessData, getDir(), fileName);

    log.append("批办单 表导出完毕\n");
    exportWorkFlow();
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
    return ZcDelegJczwDataExchange.DATA_TYPE_ZC_EB_AUDIT_SHEET;
  }

}
