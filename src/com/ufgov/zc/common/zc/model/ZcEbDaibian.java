/**
 * 
 */
package com.ufgov.zc.common.zc.model;

/**
 * @author Administrator
 *
 */
public class ZcEbDaibian extends ZcBaseBill {

  private String daiBianUser;
  private String coUser;
  private String empName;
  private String posiCode;
  private String orgPosiCode;
  
  public String getDaiBianUser() {
    return daiBianUser;
  }
  public void setDaiBianUser(String daiBianUser) {
    this.daiBianUser = daiBianUser;
  }
  public String getCoUser() {
    return coUser;
  }
  public void setCoUser(String coUser) {
    this.coUser = coUser;
  }
  public String getEmpName() {
    return empName;
  }
  public void setEmpName(String empName) {
    this.empName = empName;
  }
  public String getPosiCode() {
    return posiCode;
  }
  public void setPosiCode(String posiCode) {
    this.posiCode = posiCode;
  }
  public String getOrgPosiCode() {
    return orgPosiCode;
  }
  public void setOrgPosiCode(String orgPosiCode) {
    this.orgPosiCode = orgPosiCode;
  }
}
