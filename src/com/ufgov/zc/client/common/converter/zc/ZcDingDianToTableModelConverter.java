package com.ufgov.zc.client.common.converter.zc;

import java.text.SimpleDateFormat;
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
import com.ufgov.zc.common.zc.model.ZcDingdian;
import com.ufgov.zc.common.zc.model.ZcDingdianBi;
import com.ufgov.zc.common.zc.model.ZcDingdianItem;

public class ZcDingDianToTableModelConverter {

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

    itemInfo.add(new ColumnBeanPropertyPair(ZcDingdianItem.COL_ITEM_CONTENT, "itemContent", LangTransMeta.translate(ZcDingdianItem.COL_ITEM_CONTENT)));
    itemInfo.add(new ColumnBeanPropertyPair(ZcDingdianItem.COL_ITEM_DETAIL, "itemDetail", LangTransMeta.translate(ZcDingdianItem.COL_ITEM_DETAIL))); 
    itemInfo.add(new ColumnBeanPropertyPair(ZcDingdianItem.COL_ITEM_TYPE, "itemType", LangTransMeta.translate(ZcDingdianItem.COL_ITEM_TYPE))); 
    itemInfo.add(new ColumnBeanPropertyPair(ZcDingdianItem.COL_ITEM_TOTAL_SUM, "itemTotalSum", LangTransMeta.translate(ZcDingdianItem.COL_ITEM_TOTAL_SUM)));
    itemInfo.add(new ColumnBeanPropertyPair(ZcDingdianItem.COL_ITEM_VAL, "itemVal", LangTransMeta.translate(ZcDingdianItem.COL_ITEM_VAL)));
    itemInfo.add(new ColumnBeanPropertyPair(ZcDingdianItem.COL_ITEM_BI, "itemBi", LangTransMeta.translate(ZcDingdianItem.COL_ITEM_BI)));
    itemInfo.add(new ColumnBeanPropertyPair(ZcDingdianItem.COL_ITEM_OTHER, "itemOther", LangTransMeta.translate(ZcDingdianItem.COL_ITEM_OTHER)));
  }
  public static TableModel convertToTableModel(List ddLst) {
    // TCJLODO Auto-generated method stub

    MyTableModel tableModel = null;
    Vector names = new Vector();
    Vector values = new Vector();

    names.add(LangTransMeta.translate(ZcDingdian.COL_DD_CODE));
    names.add(LangTransMeta.translate(ZcDingdian.COL_DD_NAME));
    names.add(LangTransMeta.translate(ZcDingdian.COL_CO_NAME));
    names.add(LangTransMeta.translate(ZcDingdian.COL_DD_SUM));
    names.add(LangTransMeta.translate(ZcDingdian.COL_SUPPLIER_NAME));
    names.add(LangTransMeta.translate(ZcDingdian.COL_SU_LINK_MAN));
    names.add(LangTransMeta.translate(ZcDingdian.COL_SU_LINK_TEL));
    names.add(LangTransMeta.translate(ZcDingdian.COL_INPUT_DATE));
    names.add(LangTransMeta.translate(ZcDingdian.COL_STATUS));

    if (ddLst != null && ddLst.size() > 0) {
      SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

      for (int i = 0; i < ddLst.size(); i++) {
        Vector rowData = new Vector();
        ZcDingdian dd = (ZcDingdian) ddLst.get(i);
        rowData.add(dd.getDdCode());
        rowData.add(dd.getDdName());
        rowData.add(dd.getCoName());
        rowData.add(dd.getDdSum());
        rowData.add(dd.getSupplierName());
        rowData.add(dd.getSuLinkMan());
        rowData.add(dd.getSuLinkTel());
        rowData.add(sf.format(dd.getInputDate()));
        rowData.add(AsValDataCache.getName("ZC_VS_QB_STATUS", dd.getStatus()));
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

    tableModel.setList(ddLst);

    return tableModel;
  }

  public  TableModel convertSubBiTableData(List biList, boolean b) {
    // TCJLODO Auto-generated method stub

    BeanTableModel<ZcDingdianBi> tm = new BeanTableModel<ZcDingdianBi>() {

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

        ZcDingdianBi bean = dataBeanList.get(rowIndex);

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
      ZcDingdianBi bi=(ZcDingdianBi)biList.get(i);
      bi.setTempId(Guid.genID());
    }

    //    tm.setOidFieldName("detailCode");

    tm.setDataBean(biList, biInfo);

    return tm;
  }

  public TableModel convertSubItemTableData(List itemList,boolean isGys,final boolean isYsdw) {
    // TCJLODO Auto-generated method stub

    BeanTableModel<ZcDingdianItem> tm = new BeanTableModel<ZcDingdianItem>() {
      /**
       * 
       */
      private static final long serialVersionUID = 1614780332598039135L;
      @Override
      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);
        if(isYsdw){
          if (ZcDingdianItem.COL_ITEM_CONTENT.equals(columnId)
              ||ZcDingdianItem.COL_ITEM_DETAIL.equals(columnId)
              ||ZcDingdianItem.COL_ITEM_TYPE.equals(columnId)
              ||ZcDingdianItem.COL_ITEM_TOTAL_SUM.equals(columnId)) {
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

        ZcDingdianItem bean = dataBeanList.get(rowIndex);

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

    tm.setOidFieldName("ddItemCode");

    for (Object o : itemList) {
      ((ZcDingdianItem) o).setDdItemCode(Guid.genID());
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
