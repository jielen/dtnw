package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.util.List;
import java.util.Map;

import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;

public class ExportEntrustCancelDataBusiness extends AbstractExpDataBusiness implements IExpDataBusiness {

  public final String fileName = "zcEbEntrustCancelList";

  public final String itemFileName = "zcEbEntrustCancelDetailList";

  private List<String> keyList = null;

  public void init(List<String> list, String path, StringBuffer log) {
    // TCJLODO Auto-generated method stub
    busName = "任务取消";
    this.log = log;
    this.path = path;
    keyList = list;
  }


  public String getBusName() {
    // TCJLODO Auto-generated method stub
    return busName;
  }

  public List<ZcBaseBill> getBusinessData() {
    // TCJLODO Auto-generated method stub
    return businessData;
  }


  public void exportData() {
    // TCJLODO Auto-generated method stub
    if (keyList == null || keyList.size() == 0) {
      return;
    }
    String path = getDir();

    businessData = dljgJczwDataExpDelegate.getZcEntrustCancelData(keyList, requestMeta);

    if (businessData != null) {
      saveAsXml(businessData, path, fileName);
    }
    log.append(busName + " 导出完毕\n");
  }


  public String getDataType() {
    // TCJLODO Auto-generated method stub
    return ZcDelegJczwDataExchange.DATA_TYPE_ZC_EB_ENTRUST_CANCEL;
  }

}
