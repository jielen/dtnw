package com.ufgov.zc.common.zc.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ZcPProMitemBiChg extends ZcPProMitemBi implements Serializable {

  //合同指标数据在变更时，变更前的数据和变更后的数据都存储在ZC_XMCG_HT_BI_HISTORY中，用字段FLAG表示这个数据时老数据还是新数据
  public static String FLAG_HISTORY="history";//老数据
  public static String FLAG_NEW="new";//新数据,变更后的数据，这个数据会最终同步到合同指标表中 
  
  private String flag;//合同指标数据在变更时，变更前的数据和变更后的数据都存储在ZC_XMCG_HT_BI_HISTORY中，用字段FLAG表示这个数据时老数据还是新数据

  private String chgId;

  private BigDecimal zcBiYjjsSum;
  
//  private ZcPProMitemBi mitemBi;
 

  /**
   * @return the zcBiYjjsSum
   */
  public BigDecimal getZcBiYjjsSum() {
    return zcBiYjjsSum;
  }

  /**
   * @param zcBiYjjsSum the zcBiYjjsSum to set
   */
  public void setZcBiYjjsSum(BigDecimal zcBiYjjsSum) {
    this.zcBiYjjsSum = zcBiYjjsSum;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public String getChgId() {
    return chgId;
  }

  public void setChgId(String chgId) {
    this.chgId = chgId;
  }

/*  public ZcPProMitemBi getMitemBi() {
    return mitemBi;
  }

  public void setMitemBi(ZcPProMitemBi mitemBi) {
    this.mitemBi = mitemBi;
  }*/

}