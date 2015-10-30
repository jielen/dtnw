package com.ufgov.zc.client.zc.dljgAndjczw.nobusiness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ufgov.zc.client.zc.dljgAndjczw.expimp.AbstractExpDataBusiness;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;

public class ExportExchangeData extends AbstractExpDataBusiness {
  public static final String fileName = "exchangeData";

  private List<ZcDelegJczwDataExchange> beanList;

  private List<ZcDelegJczwDataExchange> zcDelegJczwDataList = new ArrayList<ZcDelegJczwDataExchange>();

  public ExportExchangeData(List<ZcDelegJczwDataExchange> beanList, String path) {
    this.beanList = beanList;
    this.path = path;
  }

  public void exportData() {
    saveAsXml(zcDelegJczwDataList, path, fileName);
  }

  public List<ZcDelegJczwDataExchange> getExchangeData(String dataType, List<String> businessIds) {
    List<ZcDelegJczwDataExchange> zcDelegJczwDataList = new ArrayList<ZcDelegJczwDataExchange>();
    for (Iterator<ZcDelegJczwDataExchange> iterator = beanList.iterator(); iterator.hasNext();) {

      ZcDelegJczwDataExchange record = iterator.next();

      if (businessIds.contains(record.getBusinessCode()) && dataType.equals(record.getDataType())) {

        ZcDelegJczwDataExchange m = new ZcDelegJczwDataExchange();

        m.setDataType(dataType);

        m.setBusinessCode(record.getBusinessCode());

        m.setAgency(record.getAgency());

        m.setMemo(record.getMemo());

        zcDelegJczwDataList.add(m);
      }
    }
    return zcDelegJczwDataList;
  }

  public List<String> getKeysList(String dataType) {

    List<String> keyIdsList = new ArrayList<String>();

    for (Iterator iterator = beanList.iterator(); iterator.hasNext();) {

      ZcDelegJczwDataExchange bean = (ZcDelegJczwDataExchange) iterator.next();

      if (dataType.equals(bean.getDataType())) {

        keyIdsList.add(bean.getBusinessCode());

      }

    }

    return keyIdsList;

  }

  public void addExchangeData(List<String> keyList, String dataType) {
    zcDelegJczwDataList.addAll(getExchangeData(dataType, keyList));
  }
}
