package com.ufgov.zc.common.zc.checkrule;import java.math.BigDecimal;import java.util.HashMap;import java.util.Map;import com.ufgov.zc.common.zc.model.ZcPProMake;import com.ufgov.zc.common.zc.model.ZcPProMitem;/* * 西安省厅采购计划的校验提示规则实现类 * 具体校验规则: *  一、协议、定点采购品目： *     A.办公自动化设备： *       1.台式、便携式计算机： *         a.单项或批量在60台或50万（不含）以内的：采购人自主选择协议商品。 *            采购组织形式：集中采购。 *            采购方式：公开招标。 *            采购类型：协议采购。 *            委托机构：无。 *         b.单项或批量在60台或50万（包含）以上的：采购人委托采购中心进行竞争性谈判。 *            采购组织形式：集中采购。 *            采购方式：竞争性谈判。 *            采购类型：协议采购。 *            委托机构：采购中心。 *            选择品牌个数：3个（不包含）以上 * */public class ZcMakeJSJCheckRuleBySX implements BaseRule {  private int limtNum = 60;  private Map resultMap = new HashMap();  private BigDecimal limtItemSum = new BigDecimal(500000);  public Map check(Map parameter) {    ZcPProMake resultMake = new ZcPProMake();    StringBuffer titleInfo = new StringBuffer();    StringBuffer resInfo = new StringBuffer();    ZcPProMake make = (ZcPProMake) parameter.get("data");    // 批复采购方式  (1公开招标,2邀请招标,3竞争性谈判,4单一来源采购,5询价,6其他)    String pifuMode = make.getZcPifuCgfs();    // 采购组织形式 (1集中采购,2部门集中采购,3分散采购)    String sequence = make.getZcMakeSequence();    // 采购类型 （G02定点采购,G01协议供货,Z01项目采购）    String fukuanType = make.getZcFukuanType();    // 代理机构名称    String ageyCode = make.getAgency();    ZcPProMitem item = (ZcPProMitem) parameter.get("itemInfo");    // 采购数量    int caigNum = item.getZcCaigNum().intValue();    // 采购金额    BigDecimal itemSum = item.getZcItemSum();    if (caigNum <= limtNum && (itemSum.compareTo(limtItemSum) == -1)) {      titleInfo.append("{单项或批量在60台或50万（不含）以内的}");      // 小于等于60台或小于50万      if (!"1".equals(sequence)) {        // 采购组织形式 应该是  集中采购        resInfo.append("[采购组织形式]应采取[集中采购],");        resultMake.setZcMakeSequence("1");      }      if (!"G01".equals(fukuanType)) {        // 采购类型 应该是 协议采购        resInfo.append("[采购类型]应采取[协议供货],");        resultMake.setZcFukuanType("G01");      }      /*      if (!"1".equals(pifuMode)) {              // 采购方式应该是 公开招标              resInfo.append("[采购方式]应采取[公开招标],");              resultMake.setZcPifuCgfs("1");            }*/    } else {      titleInfo.append("{单项或批量在60台或50万（含）以上的}");      // 大于60台或大于等于50万      if (!"1".equals(sequence)) {        // 采购组织形式 应该是  集中采购        resInfo.append("[采购组织形式]应采取[集中采购],");        resultMake.setZcMakeSequence("1");      }      if (!"G02".equals(fukuanType)) {        // 采购类型 应该是 协议采购        resInfo.append("[采购类型]应采取[协议供货二次竞价],");        resultMake.setZcFukuanType("G02");      }      if (!ZcMakeCheckRuleBySX.limtAgeyCode.equals(ageyCode)) {        // 代理机构应该是采购中心        resInfo.append("[代理机构]应委托[采购中心],");        resultMake.setAgency(ZcMakeCheckRuleBySX.limtAgeyCode);      }    }    if (resInfo.length() > 0) {      titleInfo.append(resInfo.substring(0, resInfo.length() - 1));      resultMap.put("resInfo", titleInfo.toString());      resultMap.put("resMake", resultMake);    } else {      resultMap.put("resInfo", resInfo.toString());      resultMap.put("resMake", null);    }    return resultMap;  }}