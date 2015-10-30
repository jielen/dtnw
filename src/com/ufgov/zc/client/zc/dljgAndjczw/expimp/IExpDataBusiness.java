package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.util.List;

import com.ufgov.zc.common.zc.model.ZcBaseBill;

public interface IExpDataBusiness {
  String getBusName();

  List<ZcBaseBill> getBusinessData();

  void exportData();

  String getDataType();

  void init(List<String> list, String path, StringBuffer log);
}
