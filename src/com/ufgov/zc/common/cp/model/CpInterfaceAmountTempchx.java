package com.ufgov.zc.common.cp.model;import java.io.Serializable;import java.math.BigDecimal;import java.util.Date;public class CpInterfaceAmountTempchx implements Serializable {  private static final long serialVersionUID = 1L;  private String sign;  private String checkAccountId;  private String month;  private String payBanknodeCode;  private String payBanknodeName;  private String eid;  private String coCode;  private String bid;  private String baccCode;  private String iid;  private String projectCode;  private String eidName;  private String coName;  private String bidName;  private String baccName;  private String iidName;  private String projectName;  private String dwqkwh;  private String senddocCode;  private String senddocName;  private BigDecimal money;  private BigDecimal czAmountSum;  private String did;  private String orgCode;  private String mid;  private String fundCode;  private String pid;  private String payoutCode;  private String bpid;  private String outlayCode;  private String budgetsrcid;  private String originCode;  private String btypeid;  private String didName;  private String orgName;  private String midName;  private String fundName;  private String pidName;  private String payoutName;  private String bpidName;  private String outlayName;  private String budgetsrcname;  private String btypename;  private Short differflag;  private String mark;  private Date accountDate;  private String begindate;  private String enddate;  private String planDate;  private String payBankCode;  private String payBankName;  public Date getAccountDate() {    return accountDate;  }  public void setAccountDate(Date accountDate) {    this.accountDate = accountDate;  }  public String getBaccCode() {    return baccCode;  }  public void setBaccCode(String baccCode) {    this.baccCode = baccCode;  }  public String getBaccName() {    return baccName;  }  public void setBaccName(String baccName) {    this.baccName = baccName;  }  public String getBegindate() {    return begindate;  }  public void setBegindate(String begindate) {    this.begindate = begindate;  }  public String getBid() {    return bid;  }  public void setBid(String bid) {    this.bid = bid;  }  public String getBidName() {    return bidName;  }  public void setBidName(String bidName) {    this.bidName = bidName;  }  public String getBpid() {    return bpid;  }  public void setBpid(String bpid) {    this.bpid = bpid;  }  public String getBpidName() {    return bpidName;  }  public void setBpidName(String bpidName) {    this.bpidName = bpidName;  }  public String getBtypeid() {    return btypeid;  }  public void setBtypeid(String btypeid) {    this.btypeid = btypeid;  }  public String getBtypename() {    return btypename;  }  public void setBtypename(String btypename) {    this.btypename = btypename;  }  public String getBudgetsrcid() {    return budgetsrcid;  }  public void setBudgetsrcid(String budgetsrcid) {    this.budgetsrcid = budgetsrcid;  }  public String getBudgetsrcname() {    return budgetsrcname;  }  public void setBudgetsrcname(String budgetsrcname) {    this.budgetsrcname = budgetsrcname;  }  public String getCheckAccountId() {    return checkAccountId;  }  public void setCheckAccountId(String checkAccountId) {    this.checkAccountId = checkAccountId;  }  public String getCoCode() {    return coCode;  }  public void setCoCode(String coCode) {    this.coCode = coCode;  }  public String getCoName() {    return coName;  }  public void setCoName(String coName) {    this.coName = coName;  }  public BigDecimal getCzAmountSum() {    return czAmountSum;  }  public void setCzAmountSum(BigDecimal czAmountSum) {    this.czAmountSum = czAmountSum;  }  public String getDid() {    return did;  }  public void setDid(String did) {    this.did = did;  }  public String getDidName() {    return didName;  }  public void setDidName(String didName) {    this.didName = didName;  }  public Short getDifferflag() {    return differflag;  }  public void setDifferflag(Short differflag) {    this.differflag = differflag;  }  public String getDwqkwh() {    return dwqkwh;  }  public void setDwqkwh(String dwqkwh) {    this.dwqkwh = dwqkwh;  }  public String getEid() {    return eid;  }  public void setEid(String eid) {    this.eid = eid;  }  public String getEidName() {    return eidName;  }  public void setEidName(String eidName) {    this.eidName = eidName;  }  public String getEnddate() {    return enddate;  }  public void setEnddate(String enddate) {    this.enddate = enddate;  }  public String getFundCode() {    return fundCode;  }  public void setFundCode(String fundCode) {    this.fundCode = fundCode;  }  public String getFundName() {    return fundName;  }  public void setFundName(String fundName) {    this.fundName = fundName;  }  public String getIid() {    return iid;  }  public void setIid(String iid) {    this.iid = iid;  }  public String getIidName() {    return iidName;  }  public void setIidName(String iidName) {    this.iidName = iidName;  }  public String getMark() {    return mark;  }  public void setMark(String mark) {    this.mark = mark;  }  public String getMid() {    return mid;  }  public void setMid(String mid) {    this.mid = mid;  }  public String getMidName() {    return midName;  }  public void setMidName(String midName) {    this.midName = midName;  }  public BigDecimal getMoney() {    return money;  }  public void setMoney(BigDecimal money) {    this.money = money;  }  public String getMonth() {    return month;  }  public void setMonth(String month) {    this.month = month;  }  public String getOrgCode() {    return orgCode;  }  public void setOrgCode(String orgCode) {    this.orgCode = orgCode;  }  public String getOrgName() {    return orgName;  }  public void setOrgName(String orgName) {    this.orgName = orgName;  }  public String getOriginCode() {    return originCode;  }  public void setOriginCode(String originCode) {    this.originCode = originCode;  }  public String getOutlayCode() {    return outlayCode;  }  public void setOutlayCode(String outlayCode) {    this.outlayCode = outlayCode;  }  public String getOutlayName() {    return outlayName;  }  public void setOutlayName(String outlayName) {    this.outlayName = outlayName;  }  public String getPayBanknodeCode() {    return payBanknodeCode;  }  public void setPayBanknodeCode(String payBanknodeCode) {    this.payBanknodeCode = payBanknodeCode;  }  public String getPayBanknodeName() {    return payBanknodeName;  }  public void setPayBanknodeName(String payBanknodeName) {    this.payBanknodeName = payBanknodeName;  }  public String getPayoutCode() {    return payoutCode;  }  public void setPayoutCode(String payoutCode) {    this.payoutCode = payoutCode;  }  public String getPayoutName() {    return payoutName;  }  public void setPayoutName(String payoutName) {    this.payoutName = payoutName;  }  public String getPid() {    return pid;  }  public void setPid(String pid) {    this.pid = pid;  }  public String getPidName() {    return pidName;  }  public void setPidName(String pidName) {    this.pidName = pidName;  }  public String getPlanDate() {    return planDate;  }  public void setPlanDate(String planDate) {    this.planDate = planDate;  }  public String getProjectCode() {    return projectCode;  }  public void setProjectCode(String projectCode) {    this.projectCode = projectCode;  }  public String getProjectName() {    return projectName;  }  public void setProjectName(String projectName) {    this.projectName = projectName;  }  public String getSenddocCode() {    return senddocCode;  }  public void setSenddocCode(String senddocCode) {    this.senddocCode = senddocCode;  }  public String getSenddocName() {    return senddocName;  }  public void setSenddocName(String senddocName) {    this.senddocName = senddocName;  }  public String getSign() {    return sign;  }  public void setSign(String sign) {    this.sign = sign;  }  public String getPayBankCode() {    return payBankCode;  }  public void setPayBankCode(String payBankCode) {    this.payBankCode = payBankCode;  }  public String getPayBankName() {    return payBankName;  }  public void setPayBankName(String payBankName) {    this.payBankName = payBankName;  }}