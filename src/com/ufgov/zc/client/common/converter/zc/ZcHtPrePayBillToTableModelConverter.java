package com.ufgov.zc.client.common.converter.zc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.datacache.CompanyDataCache;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.zc.model.ZcHtPrePayBill;
import com.ufgov.zc.common.zc.model.ZcHtPrePayBillItem;

public class ZcHtPrePayBillToTableModelConverter {

  @SuppressWarnings({ "unchecked", "serial" })
  public static DefaultTableModel convertToTableModel(List billList) {

    MyTableModel tableModel = null;

    Vector names = new Vector();

    Vector values = new Vector();

    names.add("预算单位");

    names.add("采购项目");

    names.add("采购计划编号");

    names.add("合同名称");

    names.add("合同编号");

    if (billList != null && billList.size() > 0) {

      for (int i = 0; i < billList.size(); i++) {

        Vector rowData = new Vector();

        ZcHtPrePayBill bill = (ZcHtPrePayBill) billList.get(i);

        rowData.add(CompanyDataCache.getName(bill.getCoCode()));

        rowData.add(bill.getZcMakeName());

        rowData.add(bill.getZcMakeCode());

        rowData.add(bill.getZcHtName());

        rowData.add(bill.getZcHtCode());

        values.add(rowData);

      }

    }

    tableModel = new MyTableModel(values, names) {

      @Override
      public Class getColumnClass(int column) {

        if (column >= 0 && column < getColumnCount() && this.getRowCount() > 0) {

          for (int row = 0; row < this.getRowCount(); row++) {

            if (getValueAt(row, column) != null) {

              return getValueAt(row, column).getClass();

            }

          }

        }

        return Object.class;

      }

      @Override
      public boolean isCellEditable(int row, int colum) {

        return false;

      }

    };

    tableModel.setList(billList);

    return tableModel;

  }

  private static List<ColumnBeanPropertyPair> BillDetailInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    BillDetailInfo.add(new ColumnBeanPropertyPair("PAY_ORDER", "payOrder", "分期次数"));

    BillDetailInfo.add(new ColumnBeanPropertyPair("PAY_MONEY", "payMoney", "付款金额"));

    BillDetailInfo.add(new ColumnBeanPropertyPair("PERCENT", "percent", "付款占总金额的百分比"));

    BillDetailInfo.add(new ColumnBeanPropertyPair("PAY_YEAR", "payYear", "预付款年份"));

    BillDetailInfo.add(new ColumnBeanPropertyPair("PAY_MONTH", "payMonth", "预付款月份"));

    BillDetailInfo.add(new ColumnBeanPropertyPair("REMARK", "remark", "备注"));

    BillDetailInfo.add(new ColumnBeanPropertyPair("STATUS", "status", "状态"));

  }

  public static TableModel convertSubBiTableData(List<ZcHtPrePayBillItem> biList, final boolean flag) {

    BeanTableModel<ZcHtPrePayBillItem> tm = new BeanTableModel<ZcHtPrePayBillItem>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public boolean isCellEditable(int row, int column) {
    	if(!flag){
    		return false;
    	}

        String columnId = this.getColumnIdentifier(column);

        if ("PERCENT".equals(columnId)) {

          return false;

        }

        if ("PAY_ORDER".equals(columnId)) {

          return false;

        }

        if ("STATUS".equals(columnId)) {

          return false;

        }

        return super.isCellEditable(row, column);

      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        super.setValueAt(aValue, rowIndex, columnIndex);

      }

    };

    tm.setOidFieldName("tempId");

    for (ZcHtPrePayBillItem o : biList) {

      o.setTempId(Guid.genID());

    }
    tm.setDataBean(biList, BillDetailInfo);

    return tm;

  }

  public static List<ColumnBeanPropertyPair> getBillDetailInfo() {

    return BillDetailInfo;

  }

  public static void setBillDetailInfo(List<ColumnBeanPropertyPair> billDetailInfo) {

    BillDetailInfo = billDetailInfo;

  }

}
