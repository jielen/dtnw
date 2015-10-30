package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.util.List;

import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;

public class ExportRequirementDataBusiness extends AbstractExpDataBusiness implements IExpDataBusiness {
  public final String fileName = "zcEbRequirement";

  public final String detailFileName = "zcEbRequirementDetail";

  private String path;

  private List<String> requirementIds = null;

  public ExportRequirementDataBusiness() {

  }

  public ExportRequirementDataBusiness(List<String> requirementIds, String path, StringBuffer log) {
    init(requirementIds, path, log);
  }

  @Override
  public void init(List<String> list, String path, StringBuffer log) {
    // TODO Auto-generated method stub
    this.requirementIds = list;
    this.path = path;
    busName = "需求确认";
    this.log = log;
  }

  @Override
  public void exportData() {
    if (requirementIds.size() == 0)

      return;
    String path = getDir();
    businessData = dljgJczwDataExpDelegate.getZcEbRequirementData(requirementIds, requestMeta);

    saveAsXml(businessData, path, fileName);
    log.append("需求确认 表导出完毕\n");
    List reqDetailList = dljgJczwDataExpDelegate.getZcEbRequirementDetailData(requirementIds, requestMeta);

    saveAsXml(reqDetailList, path, detailFileName);
    log.append("需求确认明细 表导出完毕\n");
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
    return ZcDelegJczwDataExchange.DATA_TYPE_ZC_EB_REQUIREMENT;
  }

}
