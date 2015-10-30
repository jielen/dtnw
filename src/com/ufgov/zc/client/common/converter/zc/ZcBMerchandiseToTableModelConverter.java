package com.ufgov.zc.client.common.converter.zc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.zc.model.ZcBMerDiscount;
import com.ufgov.zc.common.zc.model.ZcBMerchandise;

public class ZcBMerchandiseToTableModelConverter {

  public static DefaultTableModel convertToTableModel(List beanList) {

    MyTableModel tableModel = null;

    Vector names = new Vector();

    Vector values = new Vector();

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_MER_CODE));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_MER_NAME));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_NAME));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_BRA_NAME));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MER_SPEC));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MER_M_PRICE));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_YEAR));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_MER_STATUS));

    if (beanList != null && beanList.size() > 0) {

      for (int i = 0; i < beanList.size(); i++) {

        Vector rowData = new Vector();

        ZcBMerchandise zcBMerchandise = (ZcBMerchandise) beanList.get(i);

        rowData.add(zcBMerchandise.getZcMerCode());

        rowData.add(zcBMerchandise.getZcMerName());

        rowData.add(zcBMerchandise.getZcCatalogueName());

        rowData.add(zcBMerchandise.getZcBraName());

        rowData.add(zcBMerchandise.getZcMerSpec());

        rowData.add(zcBMerchandise.getZcMerMPrice());

        rowData.add(zcBMerchandise.getZcYear());

        rowData.add(AsValDataCache.getName("ZC_VS_MER_STATUS", zcBMerchandise.getZcMerStatus()));

        values.add(rowData);

      }

    }

    tableModel = new MyTableModel(values, names) {

      public Class getColumnClass(int column) {

        if ((column >= 0) && (column < getColumnCount()) && this.getRowCount() > 0) {

          for (int row = 0; row < this.getRowCount(); row++) {

            if (getValueAt(row, column) != null) {

              return getValueAt(row, column).getClass();

            }

          }

        }

        return Object.class;

      }

      public boolean isCellEditable(int row, int colum) {

        return false;

      }

    };

    tableModel.setList(beanList);

    return tableModel;

  }

  //===================================================下面程序对明细表进行处理===============================================================
  private static List<ColumnBeanPropertyPair> BillDetailInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    //    BillDetailInfo.add(new ColumnBeanPropertyPair("zcSuCode", "zcSuCode", " 供应商代码"));

    BillDetailInfo.add(new ColumnBeanPropertyPair("zcSuName", "zcSuName", "供应商名称"));

    BillDetailInfo.add(new ColumnBeanPropertyPair("zcTreatyLowerLimit", "zcTreatyLowerLimit", "协议数量下限"));

    BillDetailInfo.add(new ColumnBeanPropertyPair("zcTreatyUpperLimit", "zcTreatyUpperLimit", "协议数量上限"));

    BillDetailInfo.add(new ColumnBeanPropertyPair("zcTreatyDiscountRate", "zcTreatyDiscountRate", "协议折扣率"));

    BillDetailInfo.add(new ColumnBeanPropertyPair("zcTreatyMemo", "zcTreatyMemo", "备注"));

  }

  public static TableModel convertSubBiTableData(List<ZcBMerDiscount> biList) {

    BeanTableModel<ZcBMerDiscount> tm = new BeanTableModel<ZcBMerDiscount>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public boolean isCellEditable(int row, int column) {

        //        String columnId = this.getColumnIdentifier(column);
        //
        //        if ("zcSuName".equals(columnId)) {
        //
        //          return false;
        //
        //        }

        return super.isCellEditable(row, column);

      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        System.out.println("======================value=" + aValue + "----");
        ZcBMerDiscount bean = dataBeanList.get(rowIndex);

        if ("zcSuName".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {

            bean.setZcSuCode(null);

            bean.setZcSuName(null);

          }

          //          fireTableCellUpdated(rowIndex, columnIndex);
          //
          //          putEditedData(dataBeanList.get(rowIndex));

          super.setValueAt(aValue, rowIndex, columnIndex);

        } else if ("zcTreatyLowerLimit".equals(this.getColumnIdentifier(columnIndex))
          || "zcTreatyUpperLimit".equals(this.getColumnIdentifier(columnIndex))) {
          if (aValue == null) {
            super.setValueAt(0, rowIndex, columnIndex);
          } else {
            super.setValueAt(aValue, rowIndex, columnIndex);
          }

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }

      }

    };

    tm.setOidFieldName("tempId");

    for (ZcBMerDiscount o : biList) {

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
