package com.ufgov.zc.common.commonbiz.fieldmap;

import java.util.HashMap;
import java.util.Map;

public class ZcEbRequirementConfirmFM {

  public static Map fieldMap = new HashMap();

  static {

    fieldMap.putAll(ZcBaseBillFM.fieldMap);
    fieldMap.put("REQ_CODE", "reqCode");
    fieldMap.put("EXPERT_NAME", "expertName");
    fieldMap.put("EXPERT_DUTY", "expertDuty");
    fieldMap.put("EXPERT_WEITUO_BLOBID", "expertWeituoBlobid");
    fieldMap.put("EXPERT_WEITUO_NAME", "expertWeituoName");
    fieldMap.put("PROVIDER_QU", "providerQu");
    fieldMap.put("PROVIDER_QU_BLOBID", "providerQuBlobid");
    fieldMap.put("DELIVERY_DATE", "deliveryDate");
    fieldMap.put("DELIVERY_ADDRESS", "deliveryAddress");
    fieldMap.put("IS_MARGIN", "isMargin");
    fieldMap.put("GUARANTEE_DATE", "guaranteeDate");
    fieldMap.put("MARGIN_PAY_TYPE", "marginPayType");
    fieldMap.put("MARGIN_RETURN_DATE", "marginReturnDate");
    fieldMap.put("MARGIN_RETURN_DATE", "marginReturnDate");
    fieldMap.put("MARGIN_RETURN_COND", "marginReturnCond");
    fieldMap.put("AFTER_SALES_SER", "afterSalesSer");
    fieldMap.put("FORMULA_TYPE", "formulaType");
    fieldMap.put("IS_OVER_BUDGET", "isOverBudget");
    fieldMap.put("IS_QA", "isQa");
    fieldMap.put("REMARK", "remark");
    fieldMap.put("PAY_COND", "payCond");
    fieldMap.put("PAY_DATE", "payDate");
    fieldMap.put("IS_BID_BOND", "isBidBond");
    fieldMap.put("ZC_BI_DESC", "zcBiDesc");
    fieldMap.put("BOND_RETURN_COND", "bondReturnCond");
  }

}
