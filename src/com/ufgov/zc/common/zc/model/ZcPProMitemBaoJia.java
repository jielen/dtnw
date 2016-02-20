/** * ZcPProMitemBaoJia.java * com.ufgov.gk.common.zc.model * Administrator * 2010-11-11 */package com.ufgov.zc.common.zc.model;import java.math.BigDecimal;import java.util.ArrayList;import java.util.Date;import java.util.List;/** * 供应商的竞价报价对象 * @author Administrator * */public class ZcPProMitemBaoJia extends ZcBaseBill {  /**   *    */  private static final long serialVersionUID = -5735557257257752494L;  public static final String IS_CHENG_JIAO_Y = "Y";  public static final String IS_CHENG_JIAO_N = "N";  public static final String IS_GONG_HUO_Y = "Y";  public static final String IS_GONG_HUO_N = "N";  public static final String IS_JIN_JIA_Y = "Y";  public static final String IS_JIN_JIA_N = "N";  /*   * 报价草稿   */  public static final String BAO_JIA_STATUS_DRAFT = "0";  /*   * 报价已提交   */  public static final String BAO_JIA_STATUS_SUBMIT = "1";  /*   * 报价成交   */  public static final String BAO_JIA_STATUS_CHENG_JIAO_Y = "2";  /*   * 报价未成交   */  public static final String BAO_JIA_STATUS_CHENG_JIAO_N = "3";  /*   * 报价明细,列表里的值对象类型为ZcPProMitem   */  public List baoJiaDetailList = new ArrayList();  /*   * 服务条款明细,列表里的值对象类型为ZcPProMitem   */  public List serviceList = new ArrayList();  /**   * 是否同意服务条款   * 对于多条服务条款，只要一条不同意，就是整个不同意   * @return   */  public boolean isAgreeService(){    if(serviceList!=null){      for(int i=0;i<serviceList.size();i++){        ZcPProMitemService sr=(ZcPProMitemService) serviceList.get(i);        if(!"Y".equals(sr.getIsAgree())){//只要一条不同意，就是整个不同意          return false;        }      }    }    return true;  }  public List getServiceList() {    return serviceList;  }  public void setServiceList(List serviceList) {    this.serviceList = serviceList;  }  public void setBaoJiaDetailList(List baojiaDetailList) {    this.baoJiaDetailList = baojiaDetailList;  }  public List getBaoJiaDetailList() {    return baoJiaDetailList;  }  public String zcMakeCode;  public String zcMakeName;  //报价品牌名称  public String zcBraName;  public BigDecimal getTotalPrice() {    BigDecimal rtn = new BigDecimal(0);    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return rtn;    for (int i = 0; i < baoJiaDetailList.size(); i++) {      ZcPProMitem item = (ZcPProMitem) baoJiaDetailList.get(i);      rtn = rtn.add(item.getZcItemSum());    }    return rtn;  }  public void setTotalPrice(BigDecimal totalPrice) {  }  public String getSuName() {    if (this.getSupplier() == null)      return null;    return this.getSupplier().getName();  }  public void setSuName(String suName) {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return;    for (int i = 0; i < baoJiaDetailList.size(); i++) {      ZcPProMitem item = (ZcPProMitem) baoJiaDetailList.get(i);      item.setZcSuName(suName);    }  }  public String getSuCode() {    if (this.getSupplier() == null)      return null;    return this.getSupplier().getCode();  }  public void setSuCode(String suCode) {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return;    for (int i = 0; i < baoJiaDetailList.size(); i++) {      ZcPProMitem item = (ZcPProMitem) baoJiaDetailList.get(i);      item.setZcSuCode(suCode);    }  }  public ZcEbSupplier getSupplier() {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return null;    ZcEbSupplier su = new ZcEbSupplier();    ZcPProMitem item = (ZcPProMitem) this.baoJiaDetailList.get(0);    su.setCode(item.getZcSuCode());    su.setName(item.getZcSuName());    return su;  }  public void setSupplier(ZcEbSupplier su) {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0 || su == null)      return;    for (int i = 0; i < baoJiaDetailList.size(); i++) {      ZcPProMitem item = (ZcPProMitem) baoJiaDetailList.get(i);      item.setZcSuCode(su.getCode());      item.setZcSuName(su.getName());    }  }  /*   * 供应商的报价品牌   */  public String getZcBraName() {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0) {      return null;    }    ZcPProMitem item = (ZcPProMitem) this.baoJiaDetailList.get(0);    return item.getZcBraName();  }  public void setZcBraName(String zcBraName) {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return;    for (int i = 0; i < baoJiaDetailList.size(); i++) {      ZcPProMitem item = (ZcPProMitem) baoJiaDetailList.get(i);      item.setZcBraName(zcBraName);    }  }  /*   * 供应商的竞价提交时间   */  public Date getBaoJiaSubmitDate() {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return null;    ZcPProMitem item = (ZcPProMitem) this.baoJiaDetailList.get(0);    return item.getZcJinJiaSubmitDate();  }  public void setBaoJiaSubmitDate(Date date) {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return;    for (int i = 0; i < baoJiaDetailList.size(); i++) {      ZcPProMitem item = (ZcPProMitem) baoJiaDetailList.get(i);      item.setZcJinJiaSubmitDate(date);    }  }  public String getGongHuoStatus() {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return null;    ZcPProMitem item = (ZcPProMitem) this.baoJiaDetailList.get(0);    return item.getZcIsGongHuo();  }  public void setGongHuoStatus(String gongHuoStatus) {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return;    for (int i = 0; i < baoJiaDetailList.size(); i++) {      ZcPProMitem item = (ZcPProMitem) baoJiaDetailList.get(i);      item.setZcIsGongHuo(gongHuoStatus);    }  }  public boolean isGongHuo() {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return false;    ZcPProMitem item = (ZcPProMitem) this.baoJiaDetailList.get(0);    if (ZcPProMitemBaoJia.IS_GONG_HUO_Y.equals(item.getZcIsGongHuo())) {      return true;    } else {      return false;    }  }  public void setGongHuo(boolean isGonghuo) {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return;    for (int i = 0; i < baoJiaDetailList.size(); i++) {      ZcPProMitem item = (ZcPProMitem) baoJiaDetailList.get(i);      if (isGonghuo) {        item.setZcIsChengJiao(ZcPProMitemBaoJia.IS_CHENG_JIAO_Y);      } else {        item.setZcIsChengJiao(ZcPProMitemBaoJia.IS_CHENG_JIAO_N);      }    }  }  public String getNoGongHuoReason() {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return null;    ZcPProMitem item = (ZcPProMitem) this.baoJiaDetailList.get(0);    return item.getZcNoGongHuoReason();  }  public void setNoGongHuoReason(String noGongHuoReason) {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return;    for (int i = 0; i < baoJiaDetailList.size(); i++) {      ZcPProMitem item = (ZcPProMitem) baoJiaDetailList.get(i);      item.setZcNoGongHuoReason(noGongHuoReason);    }  }  public String getBaoJiaStatus() {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return null;    ZcPProMitem item = (ZcPProMitem) this.baoJiaDetailList.get(0);    return item.getZcIsChengJiao();  }  public void setBaoJiaStatus(String baoJiaStatus) {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return;    for (int i = 0; i < baoJiaDetailList.size(); i++) {      ZcPProMitem item = (ZcPProMitem) baoJiaDetailList.get(i);      item.setZcIsChengJiao(baoJiaStatus);    }  }  public String getChengJiaoStatus() {    if (isChengJiao())      return ZcPProMitemBaoJia.IS_CHENG_JIAO_Y;    return ZcPProMitemBaoJia.IS_CHENG_JIAO_N;  }  public void setChengJiaoStatus(String chengJiao) {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return;    if (chengJiao == null || chengJiao.equals(ZcPProMitemBaoJia.IS_CHENG_JIAO_N)) {      setChengJiao(false);    } else if (chengJiao.equals(ZcPProMitemBaoJia.IS_CHENG_JIAO_Y)) {      setChengJiao(true);    }  }  public void setChengJiao(boolean isChengJiao) {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return;    for (int i = 0; i < baoJiaDetailList.size(); i++) {      ZcPProMitem item = (ZcPProMitem) baoJiaDetailList.get(i);      if (isChengJiao) {        item.setZcIsChengJiao(ZcPProMitemBaoJia.BAO_JIA_STATUS_CHENG_JIAO_Y);      } else {        item.setZcIsChengJiao(ZcPProMitemBaoJia.BAO_JIA_STATUS_CHENG_JIAO_N);      }    }  }  public boolean isChengJiao() {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return false;    ZcPProMitem item = (ZcPProMitem) this.baoJiaDetailList.get(0);    if (ZcPProMitemBaoJia.BAO_JIA_STATUS_CHENG_JIAO_Y.equals(item.getZcIsChengJiao())) {      return true;    } else {      return false;    }  }  public void setJinJia(boolean isJinJia) {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return;    for (int i = 0; i < baoJiaDetailList.size(); i++) {      ZcPProMitem item = (ZcPProMitem) baoJiaDetailList.get(i);      if (isJinJia) {        item.setZcIsSuJinJia(ZcPProMitemBaoJia.IS_JIN_JIA_Y);      } else {        item.setZcIsSuJinJia(ZcPProMitemBaoJia.IS_JIN_JIA_N);      }    }  }  public boolean isJinJia() {    if (this.baoJiaDetailList == null || this.baoJiaDetailList.size() == 0)      return false;    ZcPProMitem item = (ZcPProMitem) this.baoJiaDetailList.get(0);    if (ZcPProMitemBaoJia.IS_JIN_JIA_Y.equals(item.getZcIsSuJinJia())) {      return true;    } else {      return false;    }  }  public boolean equals(Object obj) {    // TCJLODO Auto-generated method stub    if (obj == this)      return true;    if (obj instanceof ZcPProMitemBaoJia) {      ZcPProMitemBaoJia o = (ZcPProMitemBaoJia) obj;      if (o.getSuCode().equals(this.getSuCode()) && o.getSuName().equals(this.getSuName())) {        return true;      } else {        return false;      }    } else {      return false;    }  }  /*  public int hashCode() {      // TCJLODO Auto-generated method stub      int k = 31;      k = k * 31 + this.getSuName().hashCode();      k = k * 31 + this.getSuCode().hashCode();
      return k;    }*/  public void setZeroPrice() {    // TCJLODO Auto-generated method stub    for (int i = 0; i < this.getBaoJiaDetailList().size(); i++) {      ZcPProMitem item = (ZcPProMitem) this.getBaoJiaDetailList().get(i);      item.setZcMerPrice(new BigDecimal(0));      item.setZcItemSum(new BigDecimal(0));      item.setBudgetBiMoney(new BigDecimal(0));      item.setBudgetOtherMoney(new BigDecimal(0));    }  }  public String getZcMakeCode() {    return zcMakeCode;  }  public void setZcMakeCode(String zcMakeCode) {    this.zcMakeCode = zcMakeCode;  }  public String getZcMakeName() {    return zcMakeName;  }  public void setZcMakeName(String zcMakeName) {    this.zcMakeName = zcMakeName;  }}