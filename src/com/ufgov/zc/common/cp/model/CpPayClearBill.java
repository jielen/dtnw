package com.ufgov.zc.common.cp.model;import java.math.BigDecimal;import java.util.Date;import com.ufgov.zc.common.commonbiz.model.BaseBill;public class CpPayClearBill extends BaseBill {  private static final long serialVersionUID = -5480409137291984390L;  private String payClearBillId;  private String billNo;  private Date procDate;  private Date cdate;  private String operationTypeCode;  private BigDecimal printTimes = new BigDecimal("0");  private String cpAdjustCode = "100";  private Date adate;  private String aopinion;  private String astatusCode;  private String anStatus;  private String isValid = "1";  private String cancelerId;  private Date cancelDate;  private String cancelOpinion;  private String payVouNoList;  private String filename;  private String filenameBlobid;  private String mattr1;  private String mattr2;  private String mattr3;  private String mattr4;  private String mattr5;  private BigDecimal stamp = new BigDecimal("0");  private BigDecimal expTimes = new BigDecimal("0");  private String expRecordId;  public String getPayClearBillId() {    return payClearBillId;  }  public void setPayClearBillId(String payClearBillId) {    this.id = payClearBillId;    this.payClearBillId = payClearBillId;  }  public String getBillNo() {    return billNo;  }  public void setBillNo(String billNo) {    this.billNo = billNo;  }  public Date getProcDate() {    return procDate;  }  public void setProcDate(Date procDate) {    this.procDate = procDate;  }  public Date getCdate() {    return cdate;  }  public void setCdate(Date cdate) {    this.cdate = cdate;  }  public String getOperationTypeCode() {    return operationTypeCode;  }  public void setOperationTypeCode(String operationTypeCode) {    this.operationTypeCode = operationTypeCode;  }  public BigDecimal getPrintTimes() {    return printTimes;  }  public void setPrintTimes(BigDecimal printTimes) {    this.printTimes = printTimes;  }  public String getCpAdjustCode() {    return cpAdjustCode;  }  public void setCpAdjustCode(String cpAdjustCode) {    this.cpAdjustCode = cpAdjustCode;  }  public String getAnStatus() {    return anStatus;  }  public void setAnStatus(String anStatus) {    this.anStatus = anStatus;  }  public String getIsValid() {    return isValid;  }  public void setIsValid(String isValid) {    this.isValid = isValid;  }  public String getCancelerId() {    return cancelerId;  }  public void setCancelerId(String cancelerId) {    this.cancelerId = cancelerId;  }  public Date getCancelDate() {    return cancelDate;  }  public void setCancelDate(Date cancelDate) {    this.cancelDate = cancelDate;  }  public String getCancelOpinion() {    return cancelOpinion;  }  public void setCancelOpinion(String cancelOpinion) {    this.cancelOpinion = cancelOpinion;  }  public String getPayVouNoList() {    return payVouNoList;  }  public void setPayVouNoList(String payVouNoList) {    this.payVouNoList = payVouNoList;  }  public String getFilename() {    return filename;  }  public void setFilename(String filename) {    this.filename = filename;  }  public String getFilenameBlobid() {    return filenameBlobid;  }  public void setFilenameBlobid(String filenameBlobid) {    this.filenameBlobid = filenameBlobid;  }  public BigDecimal getStamp() {    return stamp;  }  public void setStamp(BigDecimal stamp) {    this.stamp = stamp;  }  public BigDecimal getExpTimes() {    return expTimes;  }  public void setExpTimes(BigDecimal expTimes) {    this.expTimes = expTimes;  }  public String getExpRecordId() {    return expRecordId;  }  public void setExpRecordId(String expRecordId) {    this.expRecordId = expRecordId;  }  public Date getAdate() {    return adate;  }  public void setAdate(Date adate) {    this.adate = adate;  }  public String getAopinion() {    return aopinion;  }  public void setAopinion(String aopinion) {    this.aopinion = aopinion;  }  public String getAstatusCode() {    return astatusCode;  }  public void setAstatusCode(String astatusCode) {    this.astatusCode = astatusCode;  }  public String getMattr1() {    return mattr1;  }  public void setMattr1(String mattr1) {    this.mattr1 = mattr1;  }  public String getMattr2() {    return mattr2;  }  public void setMattr2(String mattr2) {    this.mattr2 = mattr2;  }  public String getMattr3() {    return mattr3;  }  public void setMattr3(String mattr3) {    this.mattr3 = mattr3;  }  public String getMattr4() {    return mattr4;  }  public void setMattr4(String mattr4) {    this.mattr4 = mattr4;  }  public String getMattr5() {    return mattr5;  }  public void setMattr5(String mattr5) {    this.mattr5 = mattr5;  }}