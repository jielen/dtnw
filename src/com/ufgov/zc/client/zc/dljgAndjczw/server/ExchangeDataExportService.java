package com.ufgov.zc.client.zc.dljgAndjczw.server;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ufgov.zc.client.zc.dljgAndjczw.expimp.ExportAuditSheetDataBusiness;
import com.ufgov.zc.client.zc.dljgAndjczw.expimp.ExportBidBulletinDataBusiness;
import com.ufgov.zc.client.zc.dljgAndjczw.expimp.ExportEntrustDataBusiness;
import com.ufgov.zc.client.zc.dljgAndjczw.expimp.ExportEvalReportDataBusiness;
import com.ufgov.zc.client.zc.dljgAndjczw.expimp.ExportProjChgDataBusiness;
import com.ufgov.zc.client.zc.dljgAndjczw.expimp.ExportProjectDataBusiness;
import com.ufgov.zc.client.zc.dljgAndjczw.expimp.ExportProtocolDataBusiness;
import com.ufgov.zc.client.zc.dljgAndjczw.expimp.ExportRequirementDataBusiness;
import com.ufgov.zc.client.zc.dljgAndjczw.expimp.ExportWidBulletinDataBusiness;
import com.ufgov.zc.client.zc.dljgAndjczw.expimp.IExpDataBusiness;
import com.ufgov.zc.client.zc.dljgAndjczw.nobusiness.ExportExchangeData;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;

public class ExchangeDataExportService {

  private List<ZcDelegJczwDataExchange> beanList;

  private String path;

  private List<Class> expBusinessList = new ArrayList<Class>();

  public ExchangeDataExportService(List<Class> c, List<ZcDelegJczwDataExchange> beanList, String path) {
    this.beanList = beanList;
    this.path = path;
    expBusinessList = c;
  }

  public void exportBusinessData(StringBuffer log) throws IllegalArgumentException, InstantiationException, IllegalAccessException,
    InvocationTargetException, SecurityException, NoSuchMethodException {

    ExportExchangeData eed = new ExportExchangeData(beanList, path);

    for (Iterator<Class> it = expBusinessList.iterator(); it.hasNext();) {
      IExpDataBusiness bu = (IExpDataBusiness) it.next().newInstance();
      List<String> keyList = eed.getKeysList(bu.getDataType());

      bu.init(keyList, path, log);

      bu.exportData();

      eed.addExchangeData(keyList, bu.getDataType());
    }
    log.append("所有数据导出完毕\n");
    eed.exportData();
  }
}
