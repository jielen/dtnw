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
import com.ufgov.zc.common.commonbiz.model.BaseElement;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.zc.model.EmExpert;
import com.ufgov.zc.common.zc.model.EmExpertTypeJoin;

public class ZcEmExpertToTableModelConverter {

  public static DefaultTableModel convertToTableModel(List beanList) {

    MyTableModel tableModel = null;

    Vector names = new Vector();

    Vector values = new Vector();

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_EXPERT_CODE));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_EXPERT_NAME));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_EXPERT_SEX));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_BIRTHDAY));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_UNIT_NAME));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_EXP_STATUS));

    if (beanList != null && beanList.size() > 0) {

      for (int i = 0; i < beanList.size(); i++) {

        Vector rowData = new Vector();

        EmExpert emExpert = (EmExpert) beanList.get(i);

        rowData.add(emExpert.getEmExpertCode());

        rowData.add(emExpert.getEmExpertName());

        rowData.add(AsValDataCache.getName("VS_SEX", emExpert.getEmExpertSex()));

        rowData.add(emExpert.getEmBirthday());

        rowData.add(emExpert.getEmUnitName());

        rowData.add(AsValDataCache.getName("EM_VS_EXP_STATUS", emExpert.getEmExpStatus()));

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

    BillDetailInfo.add(new ColumnBeanPropertyPair("emTypeCode", "emTypeCode", "评标类别代码"));

    BillDetailInfo.add(new ColumnBeanPropertyPair("emTypeName", "emTypeName", "评标类别名称"));

  }

  public static TableModel convertSubBiTableData(List<EmExpertTypeJoin> biList) {

    BeanTableModel<EmExpertTypeJoin> tm = new BeanTableModel<EmExpertTypeJoin>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);

        return super.isCellEditable(row, column);

      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        EmExpertTypeJoin bean = dataBeanList.get(rowIndex);

        if (aValue instanceof BaseElement) {

          BeanUtil.set(columnBeanPropertyPairList.get(columnIndex).getBeanPropertyName(), ((BaseElement) aValue).getName(), bean);

          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(dataBeanList.get(rowIndex));

        } else if ("emTypeCode".equals(this.getColumnIdentifier(columnIndex))) {

          Object obj = getValueAt(rowIndex, columnIndex);

          if (obj != null) {

            if (aValue == null || !(obj.toString()).equals(aValue.toString())) {

              //清空已经选择的评标类别代码和名称
              this.getBean(rowIndex).setEmTypeCode(null);

              this.getBean(rowIndex).setEmTypeName(null);
            }

          }

          if (aValue == null) {

            this.getBean(rowIndex).setEmTypeCode(null);

            this.getBean(rowIndex).setEmTypeName(null);

          } else {

            this.getBean(rowIndex).setEmTypeCode(aValue.toString());

            //            this.getBean(rowIndex).setEmTypeName(((TreeNodeValueObject) aValue).getName());

          }

          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(dataBeanList.get(rowIndex));

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }
      }

    };

    tm.setOidFieldName("tempId");

    for (EmExpertTypeJoin o : biList) {

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
