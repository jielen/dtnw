/**
 * 
 */
package com.ufgov.zc.common.zc.model;

import java.util.Date;

/**
 * 采购送国库后的关联信息
 * 
 * @author Administrator
 *
 */
public class ZcPayGkInfo extends ZcBaseBill {  
  
  /**
   * 
   */
  private static final long serialVersionUID = -106592284867904449L;
  /**
   * 采购合同支付
   */
  public static final String ZC_BILL_TYPE_HT_ZHIFU="ZC_BILL_TYPE_HT_ZHIFU";
  /**
   * 汽车维修支付
   */
  public static final String ZC_BILL_TYPE_QX="ZC_BILL_TYPE_QX";
  /**
   * 汽车保险支付
   */
  public static final String ZC_BILL_TYPE_QB="ZC_BILL_TYPE_QB";
  
  /**
   * 采购送国库生成支付申请(ZFSQ)
   */
  public static final String GK_VOU_TYPE_ZFSQ="ZFSQ";

  /**
   * 采购送国库生成支付凭证(ZFPZ)
   */
  public static final String GK_VOU_TYPE_ZFPZ="ZFPZ";
  
  /**
   * 是否直接生成支付凭证 Y/N
   */
  public static final String GK_INTERFACE_TYPE="OPT_ZC_PAY_IS_ZhiFuPingZhen";
  
  /**
   * 采购送国库的单据类型，如合同支付、汽修、汽保等，分别对应不同代码
   */
  private String zcBillType;  
  /**
   * 采购送国库的单据id
   */
  private String zcBillId; 
  /**
   * 国库返回的支付单据id，可能是支付申请id，也可以是支付凭证的id，根据GK_VOU_TYPE可以判别
   */
  private String gkVouId;  
  /**
   * 是否国库退回：Y/N，当支付发生退款/退票等情形时，将这个值治为Y，默认值是N
   */
  private String gkReturnFlag;
  /**
   * 国库退回时间
   */
  private Date gkReturnDate; 
  
  /**
   * 国库退回原因：退款/退票
   */
  private String gkReturnReason;
  /**
   * 采购送国库的时间
   */
  private Date sendGkDate;   
  /**
   * 根据OPT_ZC_PAY_IS_ZhiFuPingZhen这个值判断，如果是Y，则是支付凭证(ZFPZ)，否则为支付申请(ZFSQ)
   */
  private String gkVouType;
  
  
  
  public String getZcBillType() {
    return zcBillType;
  }
  public void setZcBillType(String zcBillType) {
    this.zcBillType = zcBillType;
  }
  public String getZcBillId() {
    return zcBillId;
  }
  public void setZcBillId(String zcBillId) {
    this.zcBillId = zcBillId;
  }
  public String getGkVouId() {
    return gkVouId;
  }
  public void setGkVouId(String gkVouId) {
    this.gkVouId = gkVouId;
  }
  public String getGkReturnFlag() {
    return gkReturnFlag;
  }
  public void setGkReturnFlag(String gkReturnFlag) {
    this.gkReturnFlag = gkReturnFlag;
  }
  public Date getGkReturnDate() {
    return gkReturnDate;
  }
  public void setGkReturnDate(Date gkReturnDate) {
    this.gkReturnDate = gkReturnDate;
  }
  public Date getSendGkDate() {
    return sendGkDate;
  }
  public void setSendGkDate(Date sendGkDate) {
    this.sendGkDate = sendGkDate;
  }
  public String getGkVouType() {
    return gkVouType;
  }
  public void setGkVouType(String gkVouType) {
    this.gkVouType = gkVouType;
  }
  public String getGkReturnReason() {
    return gkReturnReason;
  }
  public void setGkReturnReason(String gkReturnReason) {
    this.gkReturnReason = gkReturnReason;
  } 
}
