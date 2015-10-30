package com.ufgov.zc.client.zc.supplierprice.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableModel;

import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.common.system.model.AsFile;
import com.ufgov.zc.common.zc.model.ZcEbSignupPackDetail;

public class DdcgPriceDetailPackTableModel {
  private static List<ColumnBeanPropertyPair> packTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    packTableColumnInfo.add(new ColumnBeanPropertyPair("PACK_NAME", "packName", "分包编号"));

    packTableColumnInfo.add(new ColumnBeanPropertyPair("PACK_DESC", "packDesc", "分包名称"));

    packTableColumnInfo.add(new ColumnBeanPropertyPair("TBYLB_FILE_NAME", "tbylbFileName", "定点采购报价表"));

  }

  public static List<ColumnBeanPropertyPair> getPackTableColumnInfo() {

    return packTableColumnInfo;

  }

  public static TableModel convertPackToTableModel(List<ZcEbSignupPackDetail> packList) {

    BeanTableModel<ZcEbSignupPackDetail> tm = new BeanTableModel<ZcEbSignupPackDetail>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcEbSignupPackDetail bean = getDataBeanList().get(rowIndex);

        if ("TBYLB_FILE_NAME".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {

            bean.setTbylbFileName(null);

            bean.setTbylbFileId(null);

          } else {

            bean.setTbylbFileName(((AsFile) aValue).getFileName());

            bean.setTbylbFileId(((AsFile) aValue).getFileId());

          }

          putEditedData(bean);

          fireTableCellUpdated(rowIndex, columnIndex);

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }

      }

      @Override
      public boolean isCellEditable(int row, int column) {

        if (!this.isEditable()) {

          return false;

        }

        String columnId = this.getColumnIdentifier(column);

        if ("PACK_NAME".equals(columnId) || "PACK_DESC".equals(columnId)) {

          return false;

        }

        return true;

      }

    };

    tm.setOidFieldName("packCode");

    tm.setDataBean(packList, packTableColumnInfo);

    return tm;

  }
}
