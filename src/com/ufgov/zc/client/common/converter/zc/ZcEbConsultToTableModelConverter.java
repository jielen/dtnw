package com.ufgov.zc.client.common.converter.zc;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.common.zc.model.ZcEbConsult;

public class ZcEbConsultToTableModelConverter {

  public static DefaultTableModel convertToTableModel(List list) {

    MyTableModel tableModel = null;

    Vector names = new Vector();

    Vector values = new Vector();

    names.add("标题");

    names.add("类型");

    names.add("提出人");

    names.add("提出时间");

    names.add("答复时间");

    names.add(LangTransMeta.translate("ZC_FIELD_STATUS"));

    if (list != null && list.size() > 0) {

      for (int i = 0; i < list.size(); i++) {

        Vector rowData = new Vector();

        ZcEbConsult bean = (ZcEbConsult) list.get(i);

        rowData.add(bean.getTitle());

        rowData.add(AsValDataCache.getName("ZC_VS_CONSULT_TYPE", bean.getType()));

        rowData.add(bean.getExecutorName());

        rowData.add(bean.getExecuteDate());

        rowData.add(bean.getReplyDate());

        rowData.add(AsValDataCache.getName("ZC_VS_CONSULT_STATUS", bean.getStatus()));

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

    tableModel.setList(list);

    return tableModel;

  }
}
