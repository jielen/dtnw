/**   * @(#) project: ZFCG* @(#) file: ZcEbGuiDangBillToTableModelConverter.java* * Copyright 2011 UFGOV, Inc. All rights reserved.* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.* */package com.ufgov.zc.client.common.converter.zc;import java.util.ArrayList;import java.util.List;import java.util.Vector;import javax.swing.table.DefaultTableModel;import javax.swing.table.TableModel;import com.ufgov.zc.client.common.MyTableModel;import com.ufgov.zc.client.component.table.BeanTableModel;import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;import com.ufgov.zc.common.system.Guid;import com.ufgov.zc.common.zc.model.ZcEbGuiDangBill;import com.ufgov.zc.common.zc.model.ZcEbGuiDangItem;/*** @ClassName: ZcEbGuiDangBillToTableModelConverter* @Description: TODO(这里用一句话描述这个类的作用)* @date: 2011-5-12 下午06:10:17* @version: V1.0 * @since: 1.0* @author: fanpl* @modify: */public class ZcEbGuiDangBillToTableModelConverter {  @SuppressWarnings({ "unchecked", "serial" })  public static DefaultTableModel convertToTableModel(List billList) {    MyTableModel tableModel = null;    Vector names = new Vector();    Vector values = new Vector();    names.add("采购单位");    names.add("项目名称");    names.add("任务单号");    names.add("采购限额");//    names.add("采购方式");    names.add("归档时间");    names.add("项目经办人");    if (billList != null && billList.size() > 0) {      for (int i = 0; i < billList.size(); i++) {        Vector rowData = new Vector();        ZcEbGuiDangBill bill = (ZcEbGuiDangBill) billList.get(i);        rowData.add(bill.getCoName());        rowData.add(bill.getProjName());        rowData.add(bill.getProjCode());        rowData.add(bill.getZcMoneyBiSum());//        rowData.add(AsValDataCache.getName("ZC_EB_PUR_TYPE", bill.getZcPifuCgfs()));        rowData.add(bill.getExecuteDate());        rowData.add(bill.getExecutorName());        values.add(rowData);      }    }    tableModel = new MyTableModel(values, names) {      @Override      public Class getColumnClass(int column) {        if (column >= 0 && column < getColumnCount() && this.getRowCount() > 0) {          for (int row = 0; row < this.getRowCount(); row++) {            if (getValueAt(row, column) != null) {              return getValueAt(row, column).getClass();            }          }        }        return Object.class;      }      @Override      public boolean isCellEditable(int row, int colum) {        return false;      }    };    tableModel.setList(billList);    return tableModel;  }  private static List<ColumnBeanPropertyPair> BillDetailInfo = new ArrayList<ColumnBeanPropertyPair>();  static {    BillDetailInfo.add(new ColumnBeanPropertyPair("NAME", "name", "资料名称"));//    BillDetailInfo.add(new ColumnBeanPropertyPair("NUM", "num", "数量"));    BillDetailInfo.add(new ColumnBeanPropertyPair("BOOK_NUM","bookNum","资料的册数"));    BillDetailInfo.add(new ColumnBeanPropertyPair("PAGE_NUM","pageNum","资料的页数"));    BillDetailInfo.add(new ColumnBeanPropertyPair("BOOK_NO","bookNo","卷号"));    BillDetailInfo.add(new ColumnBeanPropertyPair("SPLIT_BOX_NO","splitBoxNo","分装盒号"));  }  public static TableModel convertSubBiTableData(List<ZcEbGuiDangItem> biList) {    BeanTableModel<ZcEbGuiDangItem> tm = new BeanTableModel<ZcEbGuiDangItem>() {      private static final long serialVersionUID = 6888363838628062064L;      @Override      public boolean isCellEditable(int row, int column) {        String columnId = this.getColumnIdentifier(column);        return super.isCellEditable(row, column);      }      @Override      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {        super.setValueAt(aValue, rowIndex, columnIndex);      }    };    tm.setOidFieldName("tempId");    for (ZcEbGuiDangItem o : biList) {      o.setTempId(Guid.genID());    }    //    tm.setOidFieldName("detailCode");    tm.setDataBean(biList, BillDetailInfo);    return tm;  }  public static List<ColumnBeanPropertyPair> getBillDetailInfo() {    return BillDetailInfo;  }  public static void setBillDetailInfo(List<ColumnBeanPropertyPair> billDetailInfo) {    BillDetailInfo = billDetailInfo;  }}