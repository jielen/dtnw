package com.ufgov.zc.common.zc.model;import java.io.Serializable;import java.math.BigDecimal;import java.util.ArrayList;import java.util.List;/** * * @ClassName: ZcEbFormula* @Description: 评审指标集简单对象* @date: 2010-4-23 下午02:51:46* @version: V1.0 * @since: 1.0* @author: Administrator* @modify: */public class ZcEbFormula extends ZcBaseBill implements Serializable {  private static final long serialVersionUID = 1L;  private String remark;  public String getRemark() {    return remark;  }  public void setRemark(String remark) {    this.remark = remark;  }  private String formulaCode;  /**符合性通过比例   *    */  private BigDecimal complianceRate;  /**符合性描述   *    */  private String complianceDesc;  private String formulaName;  /**评审指标满分值   *    */  private Integer factorFullScore;  /**指标类型:   *    */  private String factorType;  /**评标模式   *    */  private String markMode;  /**评级类别   *    */  private String evalueType;  /**项目代码PROJ_CODE   *    */  private String projectCode;  /**   * 项目名称   */  private String projectName;  private String packCode;  private String packName;  /**   * 是否已确认   */  private String isConform;  private byte[] formulaDescription;  /**   * 指标集合   */  private List itemList = new ArrayList();;  /**   * 通用参数集合   */  private List paramList = new ArrayList();  private List scoreItemList = new ArrayList();  private List complicationItemList = new ArrayList();    private List formulaPackPlainList=new ArrayList();  private boolean isDataChanged = false;  /**   * @return the isDataChanged   */  public boolean isDataChanged() {    return isDataChanged;  }  /**   * @param isDataChanged the isDataChanged to set   */  public void setDataChanged(boolean isDataChanged) {    this.isDataChanged = isDataChanged;  }  public List getScoreItemList() {    if (scoreItemList != null) {      scoreItemList.clear();    }    for (int i = 0; i < itemList.size(); i++) {      if (((ZcEbFormulaItem) itemList.get(i)).getItemType().equals(EvalItemType.SCORE)) {        scoreItemList.add(itemList.get(i));      }    }    return scoreItemList;  }  public void setScoreItemList(List scoreItemList) {    this.scoreItemList = scoreItemList;  }  public List getComplicationItemList() {    if (complicationItemList != null) {      complicationItemList.clear();    }    for (int i = 0; i < itemList.size(); i++) {      if (((ZcEbFormulaItem) itemList.get(i)).getItemType().equals(EvalItemType.COMPLIANICE)) {        complicationItemList.add(itemList.get(i));      }    }    return complicationItemList;  }  public void setComplicationItemList(List complicationItemList) {    this.complicationItemList = complicationItemList;  }  public byte[] getFormulaDescription() {    return formulaDescription;  }  public void setFormulaDescription(byte[] formulaDescription) {    this.formulaDescription = formulaDescription;  }  public String getIsConform() {    return isConform;  }  public void setIsConform(String isConform) {    this.isConform = isConform;  }  public String getFormulaCode() {    return formulaCode;  }  public void setFormulaCode(String formulaCode) {    this.formulaCode = formulaCode;  }  public BigDecimal getComplianceRate() {    return complianceRate;  }  public void setComplianceRate(BigDecimal complianceRate) {    this.complianceRate = complianceRate;  }  public String getComplianceDesc() {    return complianceDesc;  }  public void setComplianceDesc(String complianceDesc) {    this.complianceDesc = complianceDesc;  }  public String getFactorType() {    return factorType;  }  public void setFactorType(String factorType) {    this.factorType = factorType;  }  public Integer getFactorFullScore() {    return factorFullScore;  }  public void setFactorFullScore(Integer factorFullScore) {    this.factorFullScore = factorFullScore;  }  public String getMarkMode() {    return markMode;  }  public void setMarkMode(String markMode) {    this.markMode = markMode;  }  public String getEvalueType() {    return evalueType;  }  public void setEvalueType(String evalueType) {    this.evalueType = evalueType;  }  public String getProjectCode() {    return projectCode;  }  public void setProjectCode(String projectCode) {    this.projectCode = projectCode;  }  public String getProjectName() {    return projectName;  }  public void setProjectName(String projectName) {    this.projectName = projectName;  }  public static long getSerialversionuid() {    return serialVersionUID;  }  private String templateCode;  private String templateName;  /**   * @return the templateName   */  public String getTemplateName() {    return templateName;  }  /**   * @param templateName the templateName to set   */  public void setTemplateName(String templateName) {    this.templateName = templateName;  }  public String getTemplateCode() {    return templateCode;  }  public void setTemplateCode(String templateCode) {    this.templateCode = templateCode;  }  public List getItemList() {    return itemList;  }  public void setItemList(List itemList) {    this.itemList = itemList;  }  public List getParamList() {    return paramList;  }  public void setParamList(List paramList) {    this.paramList = paramList;  }  public String getFormulaName() {    return formulaName;  }  public void setFormulaName(String formulaName) {    this.formulaName = formulaName;  }  public String getPackCode() {    return packCode;  }  public void setPackCode(String packCode) {    this.packCode = packCode;  }  public String getPackName() {    return packName;  }  public void setPackName(String packName) {    this.packName = packName;  }  public List getFormulaPackPlainList() {    return formulaPackPlainList;  }  public void setFormulaPackPlainList(List formulaPackPlainList) {    this.formulaPackPlainList = formulaPackPlainList;  }}