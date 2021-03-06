package com.ufgov.zc.common.zc.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZcDingdian extends ZcBaseBill{
  
  public static final String COL_BI_SUM="ZC_DINGDIAN_BI_SUM"; // 指标资金
  public static final String COL_CO_CODE="ZC_DINGDIAN_CO_CODE"; // 采购单位代码
  public static final String COL_CO_NAME="ZC_DINGDIAN_CO_NAME"; // 采购单位
  public static final String COL_DD_CODE="ZC_DINGDIAN_DD_CODE"; // 定点采购编号
  public static final String COL_DD_NAME="ZC_DINGDIAN_DD_NAME"; // 定点采购内容
  public static final String COL_DD_SUM="ZC_DINGDIAN_DD_SUM"; // 金额
  public static final String COL_DD_TYPE="ZC_DINGDIAN_DD_TYPE"; // 定点类别
  public static final String COL_EXCUTOR="ZC_DINGDIAN_EXCUTOR"; // 录入人
  public static final String COL_EXCUTOR_NAME="ZC_DINGDIAN_EXCUTOR_NAME"; // 录入人名称
  public static final String COL_INPUT_DATE="ZC_DINGDIAN_INPUT_DATE"; // 录入时间
  public static final String COL_ND="ZC_DINGDIAN_ND"; // 年度
  public static final String COL_PROCESS_INST_ID="ZC_DINGDIAN_PROCESS_INST_ID"; // 工作流id
  public static final String COL_REMARK="ZC_DINGDIAN_REMARK"; // 备注
  public static final String COL_STATUS="ZC_DINGDIAN_STATUS"; // 状态
  public static final String COL_SUPPLIER="ZC_DINGDIAN_SUPPLIER"; // 供应商代码
  public static final String COL_SUPPLIER_NAME="ZC_DINGDIAN_SUPPLIER_NAME"; // 供应商名称
  public static final String COL_SU_BANK="ZC_DINGDIAN_SU_BANK"; // 开户行
  public static final String COL_SU_BANK_ACCOUNT="ZC_DINGDIAN_SU_BANK_ACCOUNT"; // 银行账号
  public static final String COL_SU_BANK_SHOUKUANREN="ZC_DINGDIAN_SU_BANK_SHOUKUANREN"; // 银行收款人
  public static final String COL_SU_LINK_MAN="ZC_DINGDIAN_SU_LINK_MAN"; // 供应商联系人
  public static final String COL_SU_LINK_TEL="ZC_DINGDIAN_SU_LINK_TEL"; // 供应商联系电话
  public static final String COL_HT_SAOMIAO_FILE="ZC_DINGDIAN_HT_SAOMIAO_FILE"; // 合同扫描件
  public static final String COL_HT_SAOMIAO_FILE_BLOBID="ZC_DINGDIAN_HT_SAOMIAO_FILE_BLOBID"; // 合同扫描件id

  
  public static final String TAB_ID="ZcDingDian_Tab"; 
  
  public static final String ZC_VS_DD_STATUS="ZC_VS_DD_STATUS";

  private List biList=new ArrayList();
  private List itemList=new ArrayList();
  

  private String htSaomiaoFile;
  private String htSaomiaoFileId;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.DD_CODE
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String ddCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.CO_CODE
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
//    private String coCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.EXCUTOR
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String excutor;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.EXCUTOR_NAME
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String excutorName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.INPUT_DATE
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private Date inputDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.SUPPLIER
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String supplier;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.SUPPLIER_NAME
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String supplierName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.CO_NAME
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
//    private String coName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.SU_BANK_ACCOUNT
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String suBankAccount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.SU_BANK
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String suBank;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.SU_BANK_SHOUKUANREN
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String suBankShoukuanren;
 

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.DD_SUM
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private BigDecimal ddSum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.REMARK
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.STATUS
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.DD_NAME
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String ddName;

    
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.BI_SUM
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private BigDecimal biSum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.SU_LINK_MAN
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String suLinkMan;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.SU_LINK_TEL
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String suLinkTel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_DINGDIAN.DD_TYPE
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    private String ddType;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.DD_CODE
     *
     * @return the value of ZC_DINGDIAN.DD_CODE
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getDdCode() {
        return ddCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.DD_CODE
     *
     * @param ddCode the value for ZC_DINGDIAN.DD_CODE
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setDdCode(String ddCode) {
        this.ddCode = ddCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.CO_CODE
     *
     * @return the value of ZC_DINGDIAN.CO_CODE
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
//    public String getCoCode() {
//        return coCode;
//    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.CO_CODE
     *
     * @param coCode the value for ZC_DINGDIAN.CO_CODE
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
//    public void setCoCode(String coCode) {
//        this.coCode = coCode;
//    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.EXCUTOR
     *
     * @return the value of ZC_DINGDIAN.EXCUTOR
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getExcutor() {
        return excutor;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.EXCUTOR
     *
     * @param excutor the value for ZC_DINGDIAN.EXCUTOR
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setExcutor(String excutor) {
        this.excutor = excutor;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.EXCUTOR_NAME
     *
     * @return the value of ZC_DINGDIAN.EXCUTOR_NAME
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getExcutorName() {
        return excutorName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.EXCUTOR_NAME
     *
     * @param excutorName the value for ZC_DINGDIAN.EXCUTOR_NAME
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setExcutorName(String excutorName) {
        this.excutorName = excutorName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.INPUT_DATE
     *
     * @return the value of ZC_DINGDIAN.INPUT_DATE
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public Date getInputDate() {
        return inputDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.INPUT_DATE
     *
     * @param inputDate the value for ZC_DINGDIAN.INPUT_DATE
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.SUPPLIER
     *
     * @return the value of ZC_DINGDIAN.SUPPLIER
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.SUPPLIER
     *
     * @param supplier the value for ZC_DINGDIAN.SUPPLIER
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.SUPPLIER_NAME
     *
     * @return the value of ZC_DINGDIAN.SUPPLIER_NAME
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.SUPPLIER_NAME
     *
     * @param supplierName the value for ZC_DINGDIAN.SUPPLIER_NAME
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.CO_NAME
     *
     * @return the value of ZC_DINGDIAN.CO_NAME
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
//    public String getCoName() {
//        return coName;
//    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.CO_NAME
     *
     * @param coName the value for ZC_DINGDIAN.CO_NAME
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
//    public void setCoName(String coName) {
//        this.coName = coName;
//    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.SU_BANK_ACCOUNT
     *
     * @return the value of ZC_DINGDIAN.SU_BANK_ACCOUNT
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getSuBankAccount() {
        return suBankAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.SU_BANK_ACCOUNT
     *
     * @param suBankAccount the value for ZC_DINGDIAN.SU_BANK_ACCOUNT
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setSuBankAccount(String suBankAccount) {
        this.suBankAccount = suBankAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.SU_BANK
     *
     * @return the value of ZC_DINGDIAN.SU_BANK
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getSuBank() {
        return suBank;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.SU_BANK
     *
     * @param suBank the value for ZC_DINGDIAN.SU_BANK
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setSuBank(String suBank) {
        this.suBank = suBank;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.SU_BANK_SHOUKUANREN
     *
     * @return the value of ZC_DINGDIAN.SU_BANK_SHOUKUANREN
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getSuBankShoukuanren() {
        return suBankShoukuanren;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.SU_BANK_SHOUKUANREN
     *
     * @param suBankShoukuanren the value for ZC_DINGDIAN.SU_BANK_SHOUKUANREN
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setSuBankShoukuanren(String suBankShoukuanren) {
        this.suBankShoukuanren = suBankShoukuanren;
    }
 

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.DD_SUM
     *
     * @return the value of ZC_DINGDIAN.DD_SUM
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public BigDecimal getDdSum() {
        return ddSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.DD_SUM
     *
     * @param ddSum the value for ZC_DINGDIAN.DD_SUM
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setDdSum(BigDecimal ddSum) {
        this.ddSum = ddSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.REMARK
     *
     * @return the value of ZC_DINGDIAN.REMARK
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.REMARK
     *
     * @param remark the value for ZC_DINGDIAN.REMARK
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.STATUS
     *
     * @return the value of ZC_DINGDIAN.STATUS
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.STATUS
     *
     * @param status the value for ZC_DINGDIAN.STATUS
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.DD_NAME
     *
     * @return the value of ZC_DINGDIAN.DD_NAME
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getDdName() {
        return ddName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.DD_NAME
     *
     * @param ddName the value for ZC_DINGDIAN.DD_NAME
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setDdName(String ddName) {
        this.ddName = ddName;
    }

    

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.BI_SUM
     *
     * @return the value of ZC_DINGDIAN.BI_SUM
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public BigDecimal getBiSum() {
        return biSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.BI_SUM
     *
     * @param biSum the value for ZC_DINGDIAN.BI_SUM
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setBiSum(BigDecimal biSum) {
        this.biSum = biSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.SU_LINK_MAN
     *
     * @return the value of ZC_DINGDIAN.SU_LINK_MAN
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getSuLinkMan() {
        return suLinkMan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.SU_LINK_MAN
     *
     * @param suLinkMan the value for ZC_DINGDIAN.SU_LINK_MAN
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setSuLinkMan(String suLinkMan) {
        this.suLinkMan = suLinkMan;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.SU_LINK_TEL
     *
     * @return the value of ZC_DINGDIAN.SU_LINK_TEL
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getSuLinkTel() {
        return suLinkTel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.SU_LINK_TEL
     *
     * @param suLinkTel the value for ZC_DINGDIAN.SU_LINK_TEL
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setSuLinkTel(String suLinkTel) {
        this.suLinkTel = suLinkTel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_DINGDIAN.DD_TYPE
     *
     * @return the value of ZC_DINGDIAN.DD_TYPE
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public String getDdType() {
        return ddType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_DINGDIAN.DD_TYPE
     *
     * @param ddType the value for ZC_DINGDIAN.DD_TYPE
     *
     * @mbggenerated Wed Feb 03 14:01:17 CST 2016
     */
    public void setDdType(String ddType) {
        this.ddType = ddType;
    }

    public List getBiList() {
      return biList;
    }

    public void setBiList(List biList) {
      this.biList = biList;
    }
    public List getItemList() {
    return itemList;
  }

  public void setItemList(List itemList) {
    this.itemList = itemList;
  }

  public String getHtSaomiaoFile() {
    return htSaomiaoFile;
  }

  public void setHtSaomiaoFile(String htSaomiaoFile) {
    this.htSaomiaoFile = htSaomiaoFile;
  }

  public String getHtSaomiaoFileId() {
    return htSaomiaoFileId;
  }

  public void setHtSaomiaoFileId(String htSaomiaoFileId) {
    this.htSaomiaoFileId = htSaomiaoFileId;
  }
}