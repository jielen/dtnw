package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.util.List;
import java.util.Map;

import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;

public class ExportHtDataBusiness extends AbstractExpDataBusiness implements IExpDataBusiness {

  public final String fileName = "zcXmcgHt";

  public final String itemFileName = "zcXmcgHtList";

  public final String asFileName = "asHtFile";

  private List<String> keyList = null;

  //  public IZcXmcgHtServiceDelegate htDelegate = (IZcXmcgHtServiceDelegate) ServiceFactory.create(IZcXmcgHtServiceDelegate.class,
  //
  //  "zcXmcgHtServiceDelegate");

  @Override
  public void init(List<String> list, String path, StringBuffer log) {
    // TCJLODO Auto-generated method stub
    busName = "合同备案";
    this.log = log;
    this.path = path;
    keyList = list;
  }

  @Override
  public String getBusName() {
    // TCJLODO Auto-generated method stub
    return busName;
  }

  @Override
  public List<ZcBaseBill> getBusinessData() {
    // TCJLODO Auto-generated method stub
    return businessData;
  }

  @Override
  public void exportData() {
    // TCJLODO Auto-generated method stub
    if (keyList == null || keyList.size() == 0) {
      return;
    }
    String path = getDir();

    Map map = dljgJczwDataExpDelegate.getZcXmcgHtData(keyList, requestMeta);
    if (map != null) {
      if (map.get("HT") != null) {
        businessData = (List) map.get("HT");
        saveAsXml(businessData, path, fileName);
        if (map.get("ITEM") != null) {
          List item = (List) map.get("ITEM");
          saveAsXml(item, path, itemFileName);
        }
        if (map.get("FILE") != null) {
          List file = (List) map.get("FILE");
          saveAsXml(file, path, asFileName);
        }
      }
    }
    log.append("合同备案 导出完毕\n");
  }

  @Override
  public String getDataType() {
    // TCJLODO Auto-generated method stub
    return ZcDelegJczwDataExchange.DATA_TYPE_ZC_XMCG_HT;
  }

}
