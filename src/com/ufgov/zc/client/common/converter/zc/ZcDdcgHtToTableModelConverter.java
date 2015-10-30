package com.ufgov.zc.client.common.converter.zc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.datacache.CompanyDataCache;
import com.ufgov.zc.common.commonbiz.model.BaseElement;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.zc.model.ZcDdcgHt;
import com.ufgov.zc.common.zc.model.ZcDdcghtItem;

public class ZcDdcgHtToTableModelConverter {

  public static DefaultTableModel convertToTableModel(List beanList) {

    MyTableModel tableModel = null;

    Vector names = new Vector();

    Vector values = new Vector();

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_NAME));

    names.add("中标供应商");

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_CODE));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NAME));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_CODE));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_STATUS));
    if (beanList != null && beanList.size() > 0) {
      for (int i = 0; i < beanList.size(); i++) {
        Vector rowData = new Vector();
        ZcDdcgHt zcXmcgHt = (ZcDdcgHt) beanList.get(i);
        rowData.add(CompanyDataCache.getName(zcXmcgHt.getCoCode()));
        rowData.add(zcXmcgHt.getZcSuName());

        rowData.add(zcXmcgHt.getZcMakeName());

        rowData.add(zcXmcgHt.getZcMakeCode());

        rowData.add(zcXmcgHt.getZcHtName());

        rowData.add(zcXmcgHt.getZcHtCode());

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

  private static List<ColumnBeanPropertyPair> itemInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BRA_NAME, "zcBraName", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_BRA_NAME)));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_CODE, "zcCatalogueCode", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_CODE)));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_NAME, "zcCatalogueName", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_NAME)));

    itemInfo.add(new ColumnBeanPropertyPair("DIS_COUNT", "discount", "折扣率"));

  }

  public static TableModel convertSubItemTableData(List itemList) {

    BeanTableModel<ZcDdcghtItem> tm = new BeanTableModel<ZcDdcghtItem>() {

      private static final long serialVersionUID = 6888363838628062064L;

      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);

        if ("ZC_ITEM_VAL".equals(columnId) || "ZC_ITEM_SUM".equals(columnId)) {

          return false;

        }

        return super.isCellEditable(row, column);

      }

      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcDdcghtItem bean = dataBeanList.get(rowIndex);

        if (aValue instanceof BaseElement) {

          BeanUtil.set(columnBeanPropertyPairList.get(columnIndex).getBeanPropertyName(), ((BaseElement) aValue).getName(), bean);

          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(dataBeanList.get(rowIndex));

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }
      }

    };

    tm.setOidFieldName("tempId");

    for (Object o : itemList) {

      ((ZcDdcghtItem) o).setTempId(Guid.genID());

    }

    tm.setDataBean(itemList, itemInfo);

    return tm;

  }

  public static List<ColumnBeanPropertyPair> getItemInfo() {

    return itemInfo;

  }

  public static void setItemInfo(List<ColumnBeanPropertyPair> itemInfo) {

    ZcDdcgHtToTableModelConverter.itemInfo = itemInfo;

  }

  public static void setItemTableEditor(JTable table) {
  }

}
