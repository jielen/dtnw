package com.ufgov.zc.common.zc.model;import java.math.BigDecimal;import java.util.Date;import java.util.List;public class EmExpertSelectionBill extends ZcBaseBill {  private static final long serialVersionUID = 4297874708674304251L;  private String billCode;  private Date tendersTime;  private String contactCompany;  private String contactPerson;  private String contactPhone;  private String techMajor;  private String techNum;  private String ecoMajor;  private String ecoNum;  private String legalMajor;  private String legalNum;  private String avoidCompany;  private String avoidExpert;  private String xmDiyuCode;  private String diyuCode;  private String year;  private String catalogueCode;  private String approveCode;  private String emFs;  private String wjCode;  private String zjdw;  private String hwmc;  private String emBz;  private String chouqNum;  private String billStatus;  private Date inputorDate;  private String coCode;  private String makeName;  private String makeCode;  private String zjdwCode;  private String zjdwName;  private String totalNum;  private String guarderCode;  private String billPlace;  private String billPrincipal;  private String billAmount;  private String expertBillType;  private BigDecimal estimateTime;  private String callInfo;  private String msgInfo;  private List expertEvalConditionList;  private List excludeExpertList;  private List expertEvaluationList;  private List callExpertRecordList;  private List packList;  public List getPackList() {    return packList;  }  public void setPackList(List packList) {    this.packList = packList;  }  public String getBillCode() {    return billCode;  }  public void setBillCode(String billCode) {    this.billCode = billCode;  }  public Date getTendersTime() {    return tendersTime;  }  public void setTendersTime(Date tendersTime) {    this.tendersTime = tendersTime;  }  public String getContactCompany() {    return contactCompany;  }  public void setContactCompany(String contactCompany) {    this.contactCompany = contactCompany;  }  public String getContactPerson() {    return contactPerson;  }  public void setContactPerson(String contactPerson) {    this.contactPerson = contactPerson;  }  public String getContactPhone() {    return contactPhone;  }  public void setContactPhone(String contactPhone) {    this.contactPhone = contactPhone;  }  public String getTechMajor() {    return techMajor;  }  public void setTechMajor(String techMajor) {    this.techMajor = techMajor;  }  public String getTechNum() {    return techNum;  }  public void setTechNum(String techNum) {    this.techNum = techNum;  }  public String getEcoMajor() {    return ecoMajor;  }  public void setEcoMajor(String ecoMajor) {    this.ecoMajor = ecoMajor;  }  public String getEcoNum() {    return ecoNum;  }  public void setEcoNum(String ecoNum) {    this.ecoNum = ecoNum;  }  public String getLegalMajor() {    return legalMajor;  }  public void setLegalMajor(String legalMajor) {    this.legalMajor = legalMajor;  }  public String getLegalNum() {    return legalNum;  }  public void setLegalNum(String legalNum) {    this.legalNum = legalNum;  }  public String getAvoidCompany() {    return avoidCompany;  }  public void setAvoidCompany(String avoidCompany) {    this.avoidCompany = avoidCompany;  }  public String getAvoidExpert() {    return avoidExpert;  }  public void setAvoidExpert(String avoidExpert) {    this.avoidExpert = avoidExpert;  }  public String getXmDiyuCode() {    return xmDiyuCode;  }  public void setXmDiyuCode(String xmDiyuCode) {    this.xmDiyuCode = xmDiyuCode;  }  public String getDiyuCode() {    return diyuCode;  }  public void setDiyuCode(String diyuCode) {    this.diyuCode = diyuCode;  }  public String getYear() {    return year;  }  public void setYear(String year) {    this.year = year;  }  public String getCatalogueCode() {    return catalogueCode;  }  public void setCatalogueCode(String catalogueCode) {    this.catalogueCode = catalogueCode;  }  public String getApproveCode() {    return approveCode;  }  public void setApproveCode(String approveCode) {    this.approveCode = approveCode;  }  public String getEmFs() {    return emFs;  }  public void setEmFs(String emFs) {    this.emFs = emFs;  }  public String getWjCode() {    return wjCode;  }  public void setWjCode(String wjCode) {    this.wjCode = wjCode;  }  public String getZjdw() {    return zjdw;  }  public void setZjdw(String zjdw) {    this.zjdw = zjdw;  }  public String getHwmc() {    return hwmc;  }  public void setHwmc(String hwmc) {    this.hwmc = hwmc;  }  public String getEmBz() {    return emBz;  }  public void setEmBz(String emBz) {    this.emBz = emBz;  }  public String getChouqNum() {    return chouqNum;  }  public void setChouqNum(String chouqNum) {    this.chouqNum = chouqNum;  }  public String getBillStatus() {    return billStatus;  }  public void setBillStatus(String billStatus) {    this.billStatus = billStatus;  }  public Date getInputorDate() {    return inputorDate;  }  public void setInputorDate(Date inputorDate) {    this.inputorDate = inputorDate;  }  public String getCoCode() {    return coCode;  }  public void setCoCode(String coCode) {    this.coCode = coCode;  }  public String getMakeName() {    return makeName;  }  public void setMakeName(String makeName) {    this.makeName = makeName;  }  public String getZjdwCode() {    return zjdwCode;  }  public void setZjdwCode(String zjdwCode) {    this.zjdwCode = zjdwCode;  }  public String getZjdwName() {    return zjdwName;  }  public void setZjdwName(String zjdwName) {    this.zjdwName = zjdwName;  }  public String getTotalNum() {    return totalNum;  }  public void setTotalNum(String totalNum) {    this.totalNum = totalNum;  }  public String getGuarderCode() {    return guarderCode;  }  public void setGuarderCode(String guarderCode) {    this.guarderCode = guarderCode;  }  public String getBillPlace() {    return billPlace;  }  public void setBillPlace(String billPlace) {    this.billPlace = billPlace;  }  public String getBillPrincipal() {    return billPrincipal;  }  public void setBillPrincipal(String billPrincipal) {    this.billPrincipal = billPrincipal;  }  public String getBillAmount() {    return billAmount;  }  public void setBillAmount(String billAmount) {    this.billAmount = billAmount;  }  public String getExpertBillType() {    return expertBillType;  }  public void setExpertBillType(String expertBillType) {    this.expertBillType = expertBillType;  }  public BigDecimal getEstimateTime() {    return estimateTime;  }  public void setEstimateTime(BigDecimal estimateTime) {    this.estimateTime = estimateTime;  }  public String getCallInfo() {    return callInfo;  }  public void setCallInfo(String callInfo) {    this.callInfo = callInfo;  }  public String getMsgInfo() {    return msgInfo;  }  public void setMsgInfo(String msgInfo) {    this.msgInfo = msgInfo;  }  public List getExpertEvalConditionList() {    return expertEvalConditionList;  }  public void setExpertEvalConditionList(List expertEvalConditionList) {    this.expertEvalConditionList = expertEvalConditionList;  }  public List getExcludeExpertList() {    return excludeExpertList;  }  public void setExcludeExpertList(List excludeExpertList) {    this.excludeExpertList = excludeExpertList;  }  public List getExpertEvaluationList() {    return expertEvaluationList;  }  public void setExpertEvaluationList(List expertEvaluationList) {    this.expertEvaluationList = expertEvaluationList;  }  public List getCallExpertRecordList() {    return callExpertRecordList;  }  public void setCallExpertRecordList(List callExpertRecordList) {    this.callExpertRecordList = callExpertRecordList;  }  public void setMakeCode(String makeCode) {    this.makeCode = makeCode;  }  public String getMakeCode() {    return makeCode;  }}