/** * @(#) project: TableProject * @(#) file: LangTransData.java * * Copyright 2010 UFGOV, Inc. All rights reserved. * UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. * */package com.ufgov.zc.client.zc.ztb.table.data;import java.util.HashMap;import java.util.Map;/** * @ClassName: LangTransData * @Description: TODO(这里用一句话描述这个类的作用) * @date: 2010-4-23 上午11:54:38 * @version: V1.0 * @since: 1.0 * @author: Administrator * @modify: */public class LangTransData {  private static Map<String, String> map = new HashMap<String, String>();  static {    map.put("JTextFieldEditor.message1", "最小值为");    map.put("JTextFieldEditor.message2", "最大值为");    map.put("JTextFieldEditor.message3", "输入的日期无效");  }  public static Map<String, String> getData() {    return map;  }}