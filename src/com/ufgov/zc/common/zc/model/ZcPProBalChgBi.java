package com.ufgov.zc.common.zc.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ZcPProBalChgBi extends ZcPProMitemBi implements Serializable {

  private String balChgId;

  private BigDecimal zcBiYjjsSum;
  
//  private ZcPProMitemBi mitemBi;

  public String getBalChgId() {
    return balChgId;
  }

  public void setBalChgId(String balChgId) {
    this.balChgId = balChgId;
  }

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

/*  public ZcPProMitemBi getMitemBi() {
    return mitemBi;
  }

  public void setMitemBi(ZcPProMitemBi mitemBi) {
    this.mitemBi = mitemBi;
  }*/

}