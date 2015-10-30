/**
 * 
 */
package com.ufgov.zc.common.zc.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author Administrator
 *
 */
public class ZcEbJieXiang extends ZcEbAuditSheet {

  /**
   * 
   */
  private static final long serialVersionUID = 5641978835016940436L;

  private BigDecimal zhongbiaoSum;
  
  private BigDecimal htSum;
  
  private BigDecimal completePercent;
  
  private ZcEbEntrustCancel entrustCancel=new ZcEbEntrustCancel();
  
  private BigDecimal shenyuSum;

  public BigDecimal getZhongbiaoSum() {
    return zhongbiaoSum;
  }

  public void setZhongbiaoSum(BigDecimal zhongbiaoSum) {
    this.zhongbiaoSum = zhongbiaoSum;
  }

  public BigDecimal getHtSum() {
    return htSum;
  }

  public void setHtSum(BigDecimal htSum) {
    this.htSum = htSum;
  }

  public BigDecimal getCompletePercent() {
    BigDecimal rtn=new BigDecimal(0);
    if(zhongbiaoSum==null || zhongbiaoSum.doubleValue()==0.00){
      return rtn;
    }
//    System.out.println(zhongbiaoSum.doubleValue()+"  "+getZcEbEntrust().getZcMoneyBiSum().doubleValue());
    if(getZcEbEntrust()!=null && getZcEbEntrust().getZcMoneyBiSum()!=null && getZcEbEntrust().getZcMoneyBiSum().doubleValue()>0.00){
      double d=zhongbiaoSum.doubleValue()/getZcEbEntrust().getZcMoneyBiSum().doubleValue()*100;
      DecimalFormat df=new DecimalFormat("#.00");
      rtn=new BigDecimal(df.format(d));
    }
    return rtn;
  }

  public void setCompletePercent(BigDecimal completePercent) {
    this.completePercent = completePercent;
  }

  public ZcEbEntrustCancel getEntrustCancel() {
    return entrustCancel;
  }

  public void setEntrustCancel(ZcEbEntrustCancel entrustCancel) {
    this.entrustCancel = entrustCancel;
  }

  public BigDecimal getShenyuSum() {
    BigDecimal rtn=new BigDecimal(0);
    if(getZcEbEntrust()==null || getZcEbEntrust().getZcMoneyBiSum()==null){
      return rtn;
    }
    if(zhongbiaoSum!=null && zhongbiaoSum.doubleValue()==0.00){
      return getZcEbEntrust().getZcMoneyBiSum();
    }
    zhongbiaoSum=zhongbiaoSum==null?new BigDecimal(0):zhongbiaoSum;
    return getZcEbEntrust().getZcMoneyBiSum().subtract(zhongbiaoSum);
  }

  public void setShenyuSum(BigDecimal shenyuSum) {
    this.shenyuSum = shenyuSum;
  }
}
