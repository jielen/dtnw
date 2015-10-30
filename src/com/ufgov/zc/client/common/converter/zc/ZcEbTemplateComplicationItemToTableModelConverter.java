/**   * @(#) project: zcxa* @(#) file: ZcEbTemplateComplicationItemToTableModelConverter.java* * Copyright 2010 UFGOV, Inc. All rights reserved.* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.* */package com.ufgov.zc.client.common.converter.zc;import java.util.List;import java.util.Vector;import com.ufgov.zc.client.common.LangTransMeta;import com.ufgov.zc.common.system.constants.ZcElementConstants;import com.ufgov.zc.common.zc.model.ZcEbFormulaTemplateItem;/*** @ClassName: ZcEbTemplateComplicationItemToTableModelConverter* @Description: TODO(这里用一句话描述这个类的作用)* @date: 2010-7-9 下午07:08:12* @version: V1.0 * @since: 1.0* @author: Administrator* @modify: */public class ZcEbTemplateComplicationItemToTableModelConverter extends BaseEntryToTableModelConverter {  @SuppressWarnings("unchecked")  protected Vector<Object> getValue(List list) {    Vector<Object> values = new Vector<Object>();    if (null != list) {      for (int i = 0; i < list.size(); i++) {        values.add(toRowData((ZcEbFormulaTemplateItem) list.get(i)));      }    }    return values;  }  private static Vector<Object> toRowData(ZcEbFormulaTemplateItem zcEbFormulaTemplateItem) {    Vector<Object> rowData = new Vector<Object>();    //    rowData.add(zcEbFormulaItem.getItemCode());    rowData.add(zcEbFormulaTemplateItem.getName());    rowData.add(zcEbFormulaTemplateItem.getParentItemName());    //    rowData.add(zcEbFormulaTemplateItem.getIsRequiredField());    rowData.add(zcEbFormulaTemplateItem.getDescription());    rowData.add(zcEbFormulaTemplateItem.getRemark());    return rowData;  }  protected Vector<String> getColumnName() {    Vector<String> names = new Vector<String>();    //    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_EB_FORMULA_ITEM_CODE));    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_EB_FORMULA_ITEM_NAME));    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_EB_FORMULA_ITEM_PARENTITEM_NAME));    //    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_EB_FORMULA_ITEM_IS_REQUIRED_FIELD));    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_EB_FORMULA_ITEM_DESCRIPTION));    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_EB_FORMULA_ITEM_REMARK));    return names;  }}