package com.ufgov.zc.client.zc.supplierprice.model;

import com.ufgov.zc.common.zc.model.SupplierPriceDetail;

public class SupplierPriceTableModel extends AbstractBeanTableModel {

  public SupplierPriceTableModel() throws Exception {
    super(SupplierPriceDetail.class);
  }

  @Override
  protected String[] initPropNames(Class clazz) {
    // TODO Auto-generated method stub
    return new String[] { "prodName", "prodBrand", "prodType", "prodMainParam", "prodAuxiliaryParam", "prodDeviation", "procureType", "prodLocation",
      "prodCompany", "prodNum", "numUnit", "prodPrice", "prodSumPrice", "prodGuaranteePeriod", "gongHuoDate", "autoEmission", "isEnergySaving",
      "isWarterSaving", "isEnvironmentProtection", "isInnovative", "statisticsIndicator", "procureSize" };//, "procureArea" 
  }

  @Override
  protected String[] initColumnNames(Class clazz) {
    // TODO Auto-generated method stub
    return new String[] { "货物名称", "品牌", "型号", "主要功能配置和技术参数", "辅助功能配置和技术参数", "功能配置和技术参数偏离情况", "采购类别", "产地", "制造厂商", "数量", "数量单位", "单价(元)", "合计(元)",
      "质保期", "供货期(车辆)", "排量", "节能", "节水", "环保", "创新", "重点统计指标", "供应商规模" };//, "供应商所在区域" 
  }

}
