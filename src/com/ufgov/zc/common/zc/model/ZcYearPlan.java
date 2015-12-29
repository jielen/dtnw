package com.ufgov.zc.common.zc.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 采购年终结转模型类
 * @author wangwenhui
 *
 */
public class ZcYearPlan extends ZcBaseBill {

  
  /**
   * 
   */
  private static final long serialVersionUID = -4282120551284215646L;
  /**
   * 待处理数据，包括待结项和待结转的数据
   */
  public static final String END_TYPE_WAITING="00";
  /**
   * 已结项的数据
   */
  public static final String END_TYPE_YI_JIE_XIANG="10";
  /**
   * 结转中的数据，已经释放了指标，未占用新指标的数据,在结转年度可以看到的数据
   * 譬如，2014年结转数据到2015年，则执行结转动作是在2014，环境变量里的年度是2014年，在这个页签下可以看到这些等待挂接资金的结转中的数据。
   */
  public static final String END_TYPE_JIE_ZHUAN_ZHONG="20";
  /**
   * 结转中的数据，已经释放了指标，未占用新指标的数据,在结转年度可以看到的数据
   * 譬如，2014年结转数据到2015年，则执行结转动作是在2014，环境变量里的年度是2014年，在这个页签下可以看到这些等待挂接资金的结转中的数据。
   */
  public static final String END_TYPE_JIE_ZHUAN_ZHONG_OLD="20_old";
  /**
   * 结转中的数据，已经释放了指标，未占用新指标的数据，在结转的下一个年度可以看到的数据
   * 譬如，2014年结转数据到2015年，则挂接资金动作是在2015，环境变量里的年度是2015年，在这个页签下可以看到这些等待挂接资金的结转中的数据，注意这些数据的获取，是根据2014年的采购计划查询得到的，因此需要将环境变量里的年度-1才可以查到这些数据。
   */
  public static final String END_TYPE_JIE_ZHUAN_ZHONG_NEW="20_new";
  /**
   * 已完成结转的数据，已经和新指标进行了挂接，并占用了指标
   */
  public static final String END_TYPE_YI_JIE_ZHUAN="30";
  /**
   * 结转数据编号后缀
   */
  public static final String JIEZHUAN_CODE_SUFFIX="_1";

 /* *//**
   * 项目编号
   *//*
  private String zcMakeCode;

  *//**
   * 项目名称
   *//*
  private String zcMakeName;

  *//**
   * 计划状态
   *//*
  private String zcMakeStatus;

  *//**
   * 采购方式
   *//*
  private String zcCgType;

  *//**
     * 处理标记
     *//*
  private String zcYepFlag;

  *//**
   * 临时编号
   *//*
  private String zcTempMakeCode;

  *//**
   * 采购计划金额
   *//*
  private BigDecimal zcMakeSum = new BigDecimal("0.00");

  *//**
   * 已录合同备案金额
   *//*
  private BigDecimal zcHtSum = new BigDecimal("0.00");

  *//**
   * 已录验收结算金额
   *//*
  private BigDecimal zcBalSum = new BigDecimal("0.00");

  *//**
   * 计划可结转(结项)总金额
   *//*
  private BigDecimal zcMakeJzSum = new BigDecimal("0.00");

  *//**
   * 合同可结转总金额
   *//*
  private BigDecimal zcHtJzSum = new BigDecimal("0.00");

  *//**
   * 验收结算可结转总金额
   *//*
  private BigDecimal zcBalJzSum = new BigDecimal("0.00");
  
*//**
 * 补录合同金额
 *//*
  private BigDecimal zcBlHtSum = new BigDecimal("0.00");
  
*//**
 * 补录合同验收结算金额
 *//*
  private BigDecimal zcBlBalSum = new BigDecimal("0.0");
*//**
 * 补录合同金额可结转金额
 *//*
  private BigDecimal zcBlHtJzSum = new BigDecimal("0.0");
*/  
  //原来的采购计划
  private ZcPProMake make=new ZcPProMake();
  
  //结转后的采购计划
  private ZcPProMake newMake=new ZcPProMake();
  
  //合同列表
  private List htList=new ArrayList();
  //结转后合同列表
  private List newHtList=new ArrayList();
  
  //结算单列表
  private List balList=new ArrayList();
//  
//  //补充合同列表
//  private List bcHtList=new ArrayList();
//
//  private List makeBiList;
//
//  private List htBiList;

  public String getZcMakeCode() {
    return make.getZcMakeCode();
  }

  public void setZcMakeCode(String zcMakeCode) {
//    this.zcMakeCode=zcMakeCode;
  }

  public String getNewZcMakeCode() {
    if(getNewMake()!=null && getNewMake().getZcMakeCode()!=null)return getNewMake().getZcMakeCode();
    if(getMake()==null)return null;
    return getMake().getZcMakeCode()+ZcYearPlan.JIEZHUAN_CODE_SUFFIX;
  }

  public void setNewZcMakeCode(String zcMakeCode) {
//    this.zcMakeCode=zcMakeCode;
  }
  
  public ZcPProMake getMake() {
    return make;
  }

  public void setMake(ZcPProMake make) {
    this.make = make;
  }

  public List getHtList() {
    return htList;
  }

  public void setHtList(List htList) {
    this.htList = htList;
  }

  public List getBalList() {
    return balList;
  }

  public void setBalList(List balList) {
    this.balList = balList;
  }

//  public List getBcHtList() {
//    return bcHtList;
//  }
//
//  public void setBcHtList(List bcHtList) {
//    this.bcHtList = bcHtList;
//  }

  public String getZcMakeName() {
    return make.getZcMakeName();
  }

   
  public String getCoCode() {
    // TCJLODO Auto-generated method stub
    return make.getCoCode();
  }
 
  
  public String getCoName() {
    // TCJLODO Auto-generated method stub
//    ZcSUtil.
    return make.getCoName();
  }

  public Integer getNd() {
    // TCJLODO Auto-generated method stub
    return make.getNd();
  }

  public void setCoCode(String coCode) {
    // TCJLODO Auto-generated method stub
    super.setCoCode(coCode);
  }

  public String getZcCgType() {
    return make.getZcPifuCgfs();
  }

  public void setZcCgType(String zcCgType) {
//    this.zcCgType = zcCgType;
  }

  public void setZcMakeName(String zcMakeName) {
//    this.zcMakeName = zcMakeName;
  }

  public String getZcMakeStatus() {
    return make.getZcMakeStatus();
  }

  public void setZcMakeStatus(String zcMakeStatus) {
//    this.zcMakeStatus = zcMakeStatus;
  }

//  public String getZcTempMakeCode() {
//    return zcTempMakeCode;
//  }
//
//  public void setZcTempMakeCode(String zcTempMakeCode) {
//    this.zcTempMakeCode = zcTempMakeCode;
//  }

  public BigDecimal getZcMakeSum() {
    return make.getZcMoneyBiSum();
  }

  public void setZcMakeSum(BigDecimal zcMakeSum) {
//    this.zcMakeSum = zcMakeSum;
  }

  public BigDecimal getZcHtSum() {
    BigDecimal rtn=new BigDecimal(0);
    if(htList==null)return rtn;
    for(int i=0;i<htList.size();i++){
      ZcXmcgHt ht=(ZcXmcgHt)htList.get(i);
      rtn=rtn.add(ht.getZcHtNum());
    }
    return rtn;
  }

  public void setZcHtSum(BigDecimal zcHtSum) {
//    this.zcHtSum = zcHtSum;
  }

  public BigDecimal getZcBalSum() {
    BigDecimal rtn=new BigDecimal(0);
    if(balList==null)return rtn;
    for(int i=0;i<balList.size();i++){
      ZcPProBal bal=(ZcPProBal)balList.get(i);
      rtn=rtn.add(bal.getZcBalSum());
    }
    return rtn;
  }

  public void setZcBalSum(BigDecimal zcBalSum) {
//    this.zcBalSum = zcBalSum;
  }

  public String getZcYepFlag() {
    return make==null?null:make.getZcYepFlag();
  }
//
  public void setZcYepFlag(String zcYepFlag) {
    
  }

  public BigDecimal getZcMakeJzSum() {
    if(getNewMake()!=null)return getNewMake().getZcMoneyBiSum();
    return getZcHtSum().subtract(getZcBalSum());
  }

  public void setZcMakeJzSum(BigDecimal zcMakeJzSum) {
    if(getNewMake()!=null)getNewMake().setZcMoneyBiSum(zcMakeJzSum);
    
//    this.zcMakeJzSum = zcMakeJzSum;
  }
//
  public BigDecimal getZcHtJzSum() {
    return getZcHtSum().subtract(getZcBalSum());
  }

  public void setZcHtJzSum(BigDecimal zcHtJzSum) {
   
  }

//  public BigDecimal getZcBalJzSum() {
//    return zcBalJzSum;
//  }
//
//  public void setZcBalJzSum(BigDecimal zcBalJzSum) {
//    this.zcBalJzSum = zcBalJzSum;
//  }
//
//  public BigDecimal getZcBlHtSum() {
//    return zcBlHtSum;
//  }
//
//  public void setZcBlHtSum(BigDecimal zcBlHtSum) {
//    this.zcBlHtSum = zcBlHtSum;
//  }
//
//  public BigDecimal getZcBlBalSum() {
//    return zcBlBalSum;
//  }
//
//  public void setZcBlBalSum(BigDecimal zcBlBalSum) {
//    this.zcBlBalSum = zcBlBalSum;
//  }
//
//  public BigDecimal getZcBlHtJzSum() {
//    return zcBlHtJzSum;
//  }
//
//  public void setZcBlHtJzSum(BigDecimal zcBlHtJzSum) {
//    this.zcBlHtJzSum = zcBlHtJzSum;
//  }

//  public List getMakeBiList() {
//    if (null == makeBiList) {
//      makeBiList = new ArrayList();
//    }
//    return makeBiList;
//  }
//
//  public void setMakeBiList(List makeBiList) {
//    this.makeBiList = makeBiList;
//  }
//
//  public List getHtBiList() {
//    if (null == htBiList) {
//      htBiList = new ArrayList();
//    }
//    return htBiList;
//  }
//
//  public void setHtBiList(List htBiList) {
//    this.htBiList = htBiList;
//  }

  public ZcPProMake getNewMake() {
    return newMake;
  }

  public void setNewMake(ZcPProMake newMake) {
    this.newMake = newMake;
  }

  public List getNewHtList() {
    return newHtList;
  }

  public void setNewHtList(List newHtList) {
    this.newHtList = newHtList;
  }
}
