package com.ufgov.zc.client.common.converter.zc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.commonbiz.model.BaseElement;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.model.AsFile;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.zc.model.ZcQb;
import com.ufgov.zc.common.zc.model.ZcQbBi;
import com.ufgov.zc.common.zc.model.ZcQbItem;

public class ZcQbToTableModelConverter {

  public static List<ColumnBeanPropertyPair> biInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {  
    if (ZcUtil.useBudget()) {
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_NO, "zcBiNo", "指标编号"));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ORIGIN_NAME, "originName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_ORIGIN_NAME)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME, "senddocName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_FUND_NAME, "fundName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_FUND_NAME)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_PROJECT_NAME, "projectName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_PROJECT_NAME)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_B_ACC_NAME, "bAccName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_B_ACC_NAME)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_OUTLAY_NAME, "outlayName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_OUTLAY_NAME)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_SUM, "zcBiSum", "指标总金额"));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_DO_SUM, "zcBiDoSum", "指标可用金额"));

      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_JHUA_SUM, "zcBiJhuaSum", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_ZC_BI_JHUA_SUM)));

      biInfo.add(new ColumnBeanPropertyPair("ZC_FUND_REMARK", "zcFundRemark", LangTransMeta.translate("ZC_FUND_REMARK")));
    }else{
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_NO, "zcBiNo", "指标编号"));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ORIGIN_CODE, "originName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_ORIGIN_CODE)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME, "senddocName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_FUND_CODE, "fundName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_FUND_CODE)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ORIGIN_NAME, "originName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_ORIGIN_NAME)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_PROJECT_NAME, "projectName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_PROJECT_NAME)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_B_ACC_NAME, "bAccName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_B_ACC_NAME)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_OUTLAY_NAME, "outlayName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_OUTLAY_NAME)));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_SUM, "zcBiSum", "指标总金额"));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_DO_SUM, "zcBiDoSum", "指标可用金额"));

      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_JHUA_SUM, "zcBiJhuaSum", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_ZC_BI_JHUA_SUM)));

      biInfo.add(new ColumnBeanPropertyPair("ZC_FUND_REMARK", "zcFundRemark", LangTransMeta.translate("ZC_FUND_REMARK")));
    }

    //去掉资金构成里面的资金附件

    //    biInfo.add(new ColumnBeanPropertyPair("ZC_FUND_FILE", "zcFundFile", LangTransMeta.translate("ZC_FUND_FILE")));

  }

  private static List<ColumnBeanPropertyPair> itemInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_QB_CHE_PAI, "chePai", LangTransMeta
    .translate(ZcElementConstants.FIELD_TRANS_QB_CHE_PAI)));
    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_QB_CHE_FDJ, "cheFdj", LangTransMeta
    .translate(ZcElementConstants.FIELD_TRANS_QB_CHE_FDJ)));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_QB_CHE_CJH, "cheCjh", LangTransMeta
    .translate(ZcElementConstants.FIELD_TRANS_QB_CHE_CJH)));


    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_QB_ITEM_TYPE, "itemType", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_QB_ITEM_TYPE)));
    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_QB_START_DATE, "startDate", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_QB_START_DATE)));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_QB_END_DATE, "endDate", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_QB_END_DATE)));


    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_QB_ITEM_TOTAL_SUM, "itemTotalSum", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_QB_ITEM_TOTAL_SUM)));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_QB_ITEM_VAL, "itemVal", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_QB_ITEM_VAL)));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_QB_ITEM_BI, "itemBi", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_QB_ITEM_BI)));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_QB_ITEM_OTHER, "itemOther", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_QB_ITEM_OTHER)));

  }
  public static TableModel convertToTableModel(List qbLst) {
    // TCJLODO Auto-generated method stub

    MyTableModel tableModel = null;

    Vector names = new Vector();

    Vector values = new Vector();

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_QB_QB_CODE));

    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_QB_QB_NAME));
    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_QB_CO_NAME));
    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_QB_QB_SUM));
    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_QB_SUPPLIER_NAME));
    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_QB_SU_LINK_MAN));
    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_QB_SU_LINK_TEL));
    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_QB_INPUT_DATE));
    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_QB_STATUS));

    if (qbLst != null && qbLst.size() > 0) {

      for (int i = 0; i < qbLst.size(); i++) {

        Vector rowData = new Vector();

        ZcQb qb = (ZcQb) qbLst.get(i);

        rowData.add(qb.getQbCode());
        rowData.add(qb.getQbName());
        rowData.add(qb.getCoName());
        rowData.add(qb.getQbSum());
        rowData.add(qb.getSupplierName());
        rowData.add(qb.getSuLinkMan());
        rowData.add(qb.getSuLinkTel());
        rowData.add(qb.getInputDate());
        rowData.add(AsValDataCache.getName("ZC_VS_QB_STATUS", qb.getStatus()));

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

    tableModel.setList(qbLst);

    return tableModel;
  }

  public  TableModel convertSubBiTableData(List biList, boolean b) {
    // TCJLODO Auto-generated method stub

    BeanTableModel<ZcQbBi> tm = new BeanTableModel<ZcQbBi>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);

        if ("ZC_FUND_FILE_BLOBID".equals(columnId) || ZcElementConstants.FIELD_TRANS_ZC_BI_DO_SUM.equals(columnId)
          || ZcElementConstants.FIELD_TRANS_ZC_BI_SUM.equals(columnId)||ZcElementConstants.FIELD_TRANS_FUND_NAME.equals(columnId) || ZcElementConstants.FIELD_TRANS_ORIGIN_NAME.equals(columnId) 
          || ZcElementConstants.FIELD_TRANS_PAYTYPE_NAME.equals(columnId)|| "ZC_SENDDOC_NAME".equals(columnId)|| ZcElementConstants.FIELD_TRANS_PROJECT_NAME.equals(columnId)
          ||ZcElementConstants.FIELD_TRANS_B_ACC_NAME.equals(columnId)) {
          return false;
        }

        if (ZcUtil.useBudget() && "ZC_BI_NO".equals(columnId)) {
          return true;
        }

        //        if (wfCanEditFieldMap != null && !wfCanEditFieldMap.containsKey(columnId.substring("ZC_FIELD_".length()))) {

        //          return false;

        //        }

        return super.isCellEditable(row, column);

      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcQbBi bean = dataBeanList.get(rowIndex);

        if ("ZC_FUND_FILE".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {

            this.getBean(rowIndex).setZcFundFile(null);

            this.getBean(rowIndex).setZcFundFileBlobid(null);

          } else {

            this.getBean(rowIndex).setZcFundFile(((AsFile) aValue).getFileName());

            this.getBean(rowIndex).setZcFundFileBlobid(((AsFile) aValue).getFileId());

          }

          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(bean);

        } else if ("ZC_BI_NO".equals(this.getColumnIdentifier(columnIndex))) {

          super.setValueAt(aValue == null ? null : aValue + "", rowIndex, columnIndex);

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }

      }

    };

    tm.setOidFieldName("tempId");

    for (int i=0;i<biList.size();i++) {
      ZcQbBi bi=(ZcQbBi)biList.get(i);
      bi.setTempId(Guid.genID());
    }

    //    tm.setOidFieldName("detailCode");

    tm.setDataBean(biList, biInfo);

    return tm;
  }

  public TableModel convertSubItemTableData(List itemList,boolean isGys,final boolean isYsdw) {
    // TCJLODO Auto-generated method stub

    BeanTableModel<ZcQbItem> tm = new BeanTableModel<ZcQbItem>() {
      /**
       * 
       */
      private static final long serialVersionUID = 1614780332598039135L;
      @Override
      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);
        if(isYsdw){
          if (ZcElementConstants.FIELD_TRANS_QB_CHE_PAI.equals(columnId)
              ||ZcElementConstants.FIELD_TRANS_QB_CHE_PAI.equals(columnId)
              ||ZcElementConstants.FIELD_TRANS_QB_CHE_FDJ.equals(columnId)
              ||ZcElementConstants.FIELD_TRANS_QB_CHE_CJH.equals(columnId)
              ||ZcElementConstants.FIELD_TRANS_QB_ITEM_TOTAL_SUM.equals(columnId)
              ||ZcElementConstants.FIELD_TRANS_QB_ITEM_TYPE.equals(columnId)) {
            return false;
          }
        }
        if (ZcElementConstants.FIELD_TRANS_QB_ITEM_VAL.equals(columnId)) {
          return false;
        }
        return super.isCellEditable(row, column);
      }
      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcQbItem bean = dataBeanList.get(rowIndex);

        String currentColName = this.getColumnIdentifier(columnIndex);

        if (aValue instanceof BaseElement) {

          BeanUtil.set(columnBeanPropertyPairList.get(columnIndex).getBeanPropertyName(), ((BaseElement) aValue).getName(), bean);

          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(dataBeanList.get(rowIndex));

        } 
       /* else if (ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_CODE.equals(this.getColumnIdentifier(columnIndex))) {

          Object obj = getValueAt(rowIndex, columnIndex);

          if (aValue == null) {

            this.getBean(rowIndex).setZcCatalogueCode(null);

            this.getBean(rowIndex).setZcCatalogueName(null);

          } else {

            this.getBean(rowIndex).setZcCatalogueCode(((TreeNodeValueObject) aValue).getCode());

            this.getBean(rowIndex).setZcCatalogueName(((TreeNodeValueObject) aValue).getName());

          }

          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(dataBeanList.get(rowIndex));

        } */
        /*
        else if (ZcElementConstants.FIELD_TRANS_QB_ITEM_NUM.equals(currentColName)
                || ZcElementConstants.FIELD_TRANS_QB_ITEM_PRICE.equals(currentColName)
                || ZcElementConstants.FIELD_TRANS_QB_ITEM_TOTAL_SUM.equals(currentColName)
                || ZcElementConstants.FIELD_TRANS_QB_ITEM_VAL.equals(currentColName)
                || ZcElementConstants.FIELD_TRANS_QB_ITEM_BI.equals(currentColName)
                || ZcElementConstants.FIELD_TRANS_QB_ITEM_OTHER.equals(currentColName)) {
          if(aValue==null){
            aValue="0";
          }

          if (ZcElementConstants.FIELD_TRANS_QB_ITEM_NUM.equals(currentColName)) {
            bean.setItemNum(new BigDecimal(aValue.toString()));
          }else if (ZcElementConstants.FIELD_TRANS_QB_ITEM_PRICE.equals(currentColName)) {
            bean.setItemPrice(new BigDecimal(aValue.toString()));
          }else if (ZcElementConstants.FIELD_TRANS_QB_ITEM_TOTAL_SUM.equals(currentColName)) {
            bean.setItemTotalSum(new BigDecimal(aValue.toString()));
          }else if (ZcElementConstants.FIELD_TRANS_QB_ITEM_VAL.equals(currentColName)) {
            bean.setItemVal(new BigDecimal(aValue.toString()));
          }else if (ZcElementConstants.FIELD_TRANS_QB_ITEM_BI.equals(currentColName)) {
            bean.setItemBi(new BigDecimal(aValue.toString()));
          }else if (ZcElementConstants.FIELD_TRANS_QB_ITEM_OTHER.equals(currentColName)) {
            bean.setItemOther(new BigDecimal(aValue.toString()));
          }
          putEditedData(dataBeanList.get(rowIndex));
          fireTableCellUpdated(rowIndex, columnIndex);
        }
        */
        else {
          super.setValueAt(aValue, rowIndex, columnIndex);

        }

      }

    };

    tm.setOidFieldName("qbItemCode");

    for (Object o : itemList) {
      ((ZcQbItem) o).setQbItemCode(Guid.genID());
    }

    tm.setDataBean(itemList, itemInfo);

    return tm;
  }

  public static List<ColumnBeanPropertyPair> getBiInfo() {
    // TCJLODO Auto-generated method stub
    return biInfo;
  }

  public static List<ColumnBeanPropertyPair> getItemInfo() {
    // TCJLODO Auto-generated method stub
    return itemInfo;
  }

}

