package com.ufgov.zc.common.zc.model;

public class ZcXmcgHtBiHistroy extends ZcXmcgHtBi {

  /**
   * 
   */
  private static final long serialVersionUID = 7797953709311209094L;
  //合同指标数据在变更时，变更前的数据和变更后的数据都存储在ZC_XMCG_HT_BI_HISTORY中，用字段FLAG表示这个数据时老数据还是新数据
  public static String FLAG_HISTORY="history";//老数据
  public static String FLAG_NEW="new";//新数据,变更后的数据，这个数据会最终同步到合同指标表中
  private String balChgId;
  private String flag;//合同指标数据在变更时，变更前的数据和变更后的数据都存储在ZC_XMCG_HT_BI_HISTORY中，用字段FLAG表示这个数据时老数据还是新数据
  public String getBalChgId() {
    return balChgId;
  }
  public void setBalChgId(String balChgId) {
    this.balChgId = balChgId;
  }
  public String getFlag() {
    return flag;
  }
  public void setFlag(String flag) {
    this.flag = flag;
  }


}
