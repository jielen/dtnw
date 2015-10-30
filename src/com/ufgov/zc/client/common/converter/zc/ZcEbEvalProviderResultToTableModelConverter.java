/**   * @(#) project: ZFCG* @(#) file: ZcEbEvalProviderResultToTableModelConverter.java* * Copyright 2011 UFGOV, Inc. All rights reserved.* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.* */package com.ufgov.zc.client.common.converter.zc;import java.util.List;import java.util.Vector;import com.ufgov.zc.common.zc.model.EvalPackProvider;/*** @ClassName: ZcEbEvalProviderResultToTableModelConverter* @Description: TODO(这里用一句话描述这个类的作用)* @date: 2011-2-12 下午02:10:40* @version: V1.0 * @since: 1.0* @author: fanpl* @modify: */public class ZcEbEvalProviderResultToTableModelConverter extends BaseEntryToTableModelConverter {  List<EvalPackProvider> providerList;  public ZcEbEvalProviderResultToTableModelConverter(List providerList) {    super();    this.providerList = providerList;  }  @SuppressWarnings("unchecked")  protected Vector<Object> getValue(List list) {    Vector<Object> values = new Vector<Object>();    if (null != list) {      for (int i = 0; i < list.size(); i++) {        values.add(toRowData((List) list.get(i)));      }    }    return values;  }  private static Vector<Object> toRowData(List list) {    Vector<Object> rowData = new Vector<Object>();    for (int i = 0; i < list.size(); i++) {      rowData.add(list.get(i));    }    return rowData;  }  protected Vector<String> getColumnName() {    Vector<String> names = new Vector<String>();    names.add("指标名称");    for (int i = 0; i < providerList.size(); i++) {      names.add(providerList.get(i).getProviderName());    }    return names;  }}