package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;
import com.ufgov.zc.common.zc.publish.IZcEbProjChangeServiceDelegate;

/**
 * 采购方式变更
 * @author wangwei
 *
 */
public class ExportProjChgDataBusiness extends AbstractExpDataBusiness implements IExpDataBusiness {

  public static final String fileName = "zcEbProjChg";

  private List<String> keyList = null;

  IZcEbProjChangeServiceDelegate chgDelegate = (IZcEbProjChangeServiceDelegate) ServiceFactory.create(IZcEbProjChangeServiceDelegate.class,

  "zcEbProjChangeServiceDelegate");

  @Override
  public void init(List<String> list, String path, StringBuffer log) {
    // TCJLODO Auto-generated method stub
    busName = "采购变更";
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
    if (keyList == null || keyList.size() == 0)

      return;
    businessData = new ArrayList<ZcBaseBill>();
    for (Iterator<String> it = keyList.iterator(); it.hasNext();) {
      businessData.add(chgDelegate.getZcEbProjChange(it.next(), requestMeta));
    }
    saveAsXml(businessData, getDir(), fileName);
    log.append("采购变更 导出完毕\n");
  }

  @Override
  public String getDataType() {
    // TCJLODO Auto-generated method stub
    return ZcDelegJczwDataExchange.DATA_TYPE_ZC_EB_PORJ_CHG;
  }

}
