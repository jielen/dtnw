package com.ufgov.zc.client.common.converter.zc;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.zc.model.ZcBBrand;

;

public class ZcBdPinpToTableModelConverter {

  public static DefaultTableModel convertToTableModel(List noticeList) {

    MyTableModel tableModel = null;

    Vector names = new Vector();

    Vector values = new Vector();

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_BRA_CODE));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_BRA_NAME));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_SPPC_ND));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_ZBPP_STATUS));

    if (noticeList != null && noticeList.size() > 0) {

      for (int i = 0; i < noticeList.size(); i++) {

        Vector rowData = new Vector();

        ZcBBrand zcBdPinp = (ZcBBrand) noticeList.get(i);

        rowData.add(zcBdPinp.getZcBraCode());

        rowData.add(zcBdPinp.getZcBraName());

        rowData.add(zcBdPinp.getZcYear());

        rowData.add(AsValDataCache.getName("ZC_VS_PINP_STATUS", zcBdPinp.getZcZbPpStatus()));

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

    tableModel.setList(noticeList);

    return tableModel;

  }

}
