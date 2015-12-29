package com.ufgov.zc.client.zc.infoscreen.realmessage;

import com.ufgov.zc.client.zc.supplierprice.model.AbstractBeanTableModel;
import com.ufgov.zc.common.zc.model.ZcEbSignup;

public class ZcEbSignTableModel extends AbstractBeanTableModel {

  public ZcEbSignTableModel() throws Exception {
    super(ZcEbSignup.class);
  }

  @Override
  protected String[] initPropNames(Class clazz) {
    // TCJLODO Auto-generated method stub
    return new String[] { "projCode", "projName", "providerName" };
  }

  @Override
  protected String[] initColumnNames(Class clazz) {
    // TCJLODO Auto-generated method stub
    return new String[] { "项目编号", "采购项目", "供应商" };
  }

}
