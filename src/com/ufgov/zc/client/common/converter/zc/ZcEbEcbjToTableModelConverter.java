/**   * @(#) project: ZFCG* @(#) file: ZcEbEcbjToTableModelConverter.java* * Copyright 2011 UFGOV, Inc. All rights reserved.* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.* */package com.ufgov.zc.client.common.converter.zc;import java.util.List;import java.util.Vector;import javax.swing.table.TableModel;import com.ufgov.zc.client.common.LangTransMeta;import com.ufgov.zc.client.common.MyTableModel;import com.ufgov.zc.common.system.constants.ZcElementConstants;import com.ufgov.zc.common.zc.model.ZcEbEcbj;/*** @ClassName: ZcEbEcbjToTableModelConverter* @Description: TODO(这里用一句话描述这个类的作用)* @date: 2011-2-18 下午02:56:05* @version: V1.0 * @since: 1.0* @author: fanpl* @modify: */public class ZcEbEcbjToTableModelConverter {  public static TableModel convertToTableModel(List zcEbEcbjLst) {    MyTableModel tableModel = null;    Vector names = new Vector();    Vector values = new Vector();    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PROJ_CODE));    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_PROJ_NAME));    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_CODE));    names.add(LangTransMeta.translate(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_NAME)));    names.add("供应商");    //    names.add("报价次数");    names.add("最终报价金额");    //    names.add("开标状态");    if (zcEbEcbjLst != null && zcEbEcbjLst.size() > 0) {      for (int i = 0; i < zcEbEcbjLst.size(); i++) {        Vector rowData = new Vector();        ZcEbEcbj bj = (ZcEbEcbj) zcEbEcbjLst.get(i);        rowData.add(bj.getProjCode());        rowData.add(bj.getProjName());        rowData.add(bj.getPackCode());        rowData.add(bj.getPackName());        rowData.add(bj.getProviderName());        //        rowData.add(bj.getBijiaNo());        if (bj.getEcbjSum() != null) {          rowData.add(bj.getEcbjSum());        } else {          rowData.add(bj.getBjSum());        }        //        rowData.add(AsValDataCache.getName("ZC_VS_OPEN_BID_STATUS", bj.getOpenBidStatus()));        values.add(rowData);      }    }    tableModel = new MyTableModel(values, names) {      public Class getColumnClass(int column) {        if ((column >= 0) && (column < getColumnCount()) && this.getRowCount() > 0) {          for (int row = 0; row < this.getRowCount(); row++) {            if (getValueAt(row, column) != null) {              return getValueAt(row, column).getClass();            }          }        }        return Object.class;      }      public boolean isCellEditable(int row, int colum) {        return false;      }    };    tableModel.setList(zcEbEcbjLst);    return tableModel;  }}