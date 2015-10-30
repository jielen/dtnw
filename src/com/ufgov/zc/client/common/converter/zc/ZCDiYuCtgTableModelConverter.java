package com.ufgov.zc.client.common.converter.zc;

import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.common.zc.model.ZCDiYuCtg;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * <p>PURPOSE:
 * <p>DESCRIPTION:
 * <p>CALLED BY:	qianmingjin
 * <p>CREATE DATE:  12-3-23
 * <p>UPDATE DATE: 12-3-23
 * <p>UPDATE USER: qianmingjin
 * <p>HISTORY:		1.0
 *
 * @author qianmingjin
 * @version 1.0
 * @see
 * @since java 1.5.0
 */
public class ZCDiYuCtgTableModelConverter {
  public static List<ColumnBeanPropertyPair> biInfo = new ArrayList<ColumnBeanPropertyPair>();

  public static TableModel convertToTableModel(List<ZCDiYuCtg> biList) {
    MyTableModel tableModel = null;
    Vector names = new Vector();
    Vector values = new Vector();
    names.add("地域名称");
    names.add("地域代码");
    if (biList != null && biList.size() > 0) {
      for (int i = 0; i < biList.size(); i++) {
        Vector rowData = new Vector();
        ZCDiYuCtg zcDiYuCtg = biList.get(i);
        rowData.add(zcDiYuCtg.getDiYuName());
        rowData.add(zcDiYuCtg.getDiYuCode());
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
    tableModel.setList(biList);
    return tableModel;
  }

}
