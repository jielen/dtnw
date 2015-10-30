package com.ufgov.zc.client.common.converter.zc;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableModel;

import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.common.commonbiz.model.BaseElement;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.zc.model.ZcEbSupQualificationLev;

public class ZcEbSupQualifTableModelConverter {

  public static TableModel convertDetailToTableModel(List itemList) {

    BeanTableModel<ZcEbSupQualificationLev> tm = new BeanTableModel<ZcEbSupQualificationLev>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public boolean isCellEditable(int row, int column) {

        return super.isCellEditable(row, column);

      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcEbSupQualificationLev bean = dataBeanList.get(rowIndex);

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

    if (itemList != null) {
      for (Object o : itemList) {

        ((ZcEbSupQualificationLev) o).setTempId(Guid.genID());

      }
    }

    tm.setDataBean(itemList, itemInfo);

    return tm;

  }

  private static List<ColumnBeanPropertyPair> itemInfo = new ArrayList<ColumnBeanPropertyPair>();

  public static List<ColumnBeanPropertyPair> getItemInfo() {
    return itemInfo;
  }

  static {

    itemInfo.add(new ColumnBeanPropertyPair("NAME", "name", "等级名称"));

    itemInfo.add(new ColumnBeanPropertyPair("LEV", "lev", "级别"));

  }
}
