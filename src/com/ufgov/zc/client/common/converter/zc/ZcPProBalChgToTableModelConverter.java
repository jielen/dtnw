package com.ufgov.zc.client.common.converter.zc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.model.AsFile;
import com.ufgov.zc.common.zc.model.ZcPProBalChg;
import com.ufgov.zc.common.zc.model.ZcPProMitemBiChg;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;

public class ZcPProBalChgToTableModelConverter {
  private static RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private static boolean isUsebi = true;

  public static DefaultTableModel convertToTableModel(List billList) {
    MyTableModel tableModel = null;
    Vector<String> names = new Vector<String>();
    Vector<Vector<Comparable>> values = new Vector<Vector<Comparable>>();
    names.add(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_ID));
    names.add("采购计划编号");
    names.add(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MAKE_NAME));
    names.add(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_CO_CODE));
    names.add(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_CO_NAME));
    names.add(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_CO_CODE_ND));
    names.add("状态");
    if (billList != null && billList.size() > 0) {
      for (int i = 0; i < billList.size(); i++) {
        Vector<Comparable> rowData = new Vector<Comparable>();
        ZcPProBalChg zcPProBal = (ZcPProBalChg) billList.get(i);
        rowData.add(zcPProBal.getBalChgId());
        rowData.add(zcPProBal.getZcMakeCode());
        rowData.add(zcPProBal.getZcMakeName());
        rowData.add(zcPProBal.getCoCode());
        rowData.add(zcPProBal.getCoName());
        rowData.add(zcPProBal.getNd());
        rowData.add(AsValDataCache.getName("VS_ZC_P_PRO_BAL_CHG", zcPProBal.getStatus()));
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

    tableModel.setList(billList);
    return tableModel;
  }

  private static List<ColumnBeanPropertyPair> biInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,

    "baseDataServiceDelegate");

    isUsebi = "Y".equals(AsOptionMeta.getOptVal(ZcSettingConstants.OPT_ZC_USE_BUDGET_INTERFACE));

    if (isUsebi) {

      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_NO, "zcBiNo", "指标余额表ID"));
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_DO_SUM, "zcBiDoSum", "指标可用金额"));

    }

    biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_JHUA_SUM, "zcBiJhuaSum", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_BI_JHUA_SUM)));

    biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_YJBA_SUM, "zcBiYjbaSum", "合同备案金额"));

    biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_FUND_CODE, "fundCode", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_FUND_CODE)));

    biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ORIGIN_CODE, "originCode", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ORIGIN_CODE)));

    biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE, "paytypeCode", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE)));

    if (isUsebi) {
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME, "senddocName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME)));
    } else {
      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_SENDDOC_CODE, "senddocCode", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME)));
    }

    biInfo.add(new ColumnBeanPropertyPair("ZC_FUND_REMARK", "zcFundRemark", LangTransMeta.translate("ZC_FUND_REMARK")));
    /*
     * 已经使用金额
     */
    biInfo.add(new ColumnBeanPropertyPair("ZC_BI_YJJS_SUM", "zcBiYjjsSum", "已经结算金额"));
  }

  public static TableModel convertSubBiTableData(List<ZcPProMitemBiChg> biList, final List<ZcPProMitemBiChg> oldBiList, Map wfCanEditFieldMap) {

    BeanTableModel<ZcPProMitemBiChg> tm = new BeanTableModel<ZcPProMitemBiChg>() {

      @Override
      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);
        ZcPProMitemBiChg rowData=this.getBean(row);
        if(isOldBi(rowData,oldBiList)){
          return false;
        }
        //增加自筹资金校验，自筹资金也不能调整，只能调整待配套资金
        if(ZcPProMitemBiChg.ZCZJ.equals(rowData.getFundCode())){
          return false;
        }
        if (ZcElementConstants.FIELD_TRANS_ORIGIN_CODE.equals(columnId) 
          || ZcElementConstants.FIELD_TRANS_SENDDOC_NAME.equals(columnId) 
          || ZcElementConstants.FIELD_TRANS_FUND_CODE.equals(columnId)
          || ZcElementConstants.FIELD_TRANS_ORIGIN_NAME.equals(columnId)
          || ZcElementConstants.FIELD_TRANS_PROJ_NAME.equals(columnId) 
          || ZcElementConstants.FIELD_TRANS_B_ACC_NAME.equals(columnId) 
          || ZcElementConstants.FIELD_TRANS_OUTLAY_NAME.equals(columnId)
          || ZcElementConstants.FIELD_TRANS_BT_NAME.equals(columnId)
          || ZcElementConstants.FIELD_TRANS_GB_NAME.equals(columnId) 
          || ZcElementConstants.FIELD_TRANS_ZC_BI_SUM.equals(columnId) 
          || ZcElementConstants.FIELD_TRANS_ZC_BI_DO_SUM.equals(columnId)) {

          if(ZcUtil.useBudget()){
            return false;
          }else{
            return true;
          }
        }
       /* if (ZcUtil.useBudget() && "ZC_BI_NO".equals(columnId) && ZcUtil.isYsdw()) {
          return true;
        }*/
        return super.isCellEditable(row, column);
      }
      /**
       * 旧的指标资金不能修改，只能调整待配套资金
       * @param rowData
       * @param oldBiList
       * @return
       */
      private boolean isOldBi(ZcPProMitemBiChg rowData, List<ZcPProMitemBiChg> oldBiList) {
        // TCJLODO Auto-generated method stub
        if(rowData!=null && oldBiList!=null){
          for (ZcPProMitemBiChg oldBi : oldBiList) {
            if(oldBi.getZcBiNo()!=null && oldBi.getZcBiNo().equals(rowData.getZcBiNo())){
              return true;
            }
          }
        }
        return false;
      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcPProMitemBiChg bean = dataBeanList.get(rowIndex);

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

    for (ZcPProMitemBi o : biList) {

      o.setTempId(Guid.genID());

    }
    tm.setDataBean(biList, biInfoWihtBudget);
    return tm;

  }
  public static List<ColumnBeanPropertyPair> biInfoWihtBudget = new ArrayList<ColumnBeanPropertyPair>();

  static {
    IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,

    "baseDataServiceDelegate");    

//    if (isUseBi) {

      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_NO, "zcBiNo", "指标编号"));
      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ORIGIN_CODE, "originName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_ORIGIN_CODE)));
      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME, "senddocName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME)));
      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_FUND_CODE, "fundName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_FUND_CODE)));
//      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ORIGIN_NAME, "originName", LangTransMeta
//        .translate(ZcElementConstants.FIELD_TRANS_ORIGIN_NAME)));
      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_PROJ_NAME, "projectName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_PROJ_NAME)));
      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_B_ACC_NAME, "bAccName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_B_ACC_NAME)));
      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_OUTLAY_NAME, "outlayName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_OUTLAY_NAME)));
      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_BT_NAME, "btName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_BT_NAME)));//是否监督使用
      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_GB_NAME, "gbName", LangTransMeta
        .translate(ZcElementConstants.FIELD_TRANS_GB_NAME)));//是否政府采购
      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_SUM, "zcBiSum", "指标总金额"));
      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_DO_SUM, "zcBiDoSum", "指标可用金额"));

      biInfoWihtBudget.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_JHUA_SUM, "zcBiJhuaSum", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_ZC_BI_JHUA_SUM)));

      biInfoWihtBudget.add(new ColumnBeanPropertyPair("ZC_FUND_REMARK", "zcFundRemark", LangTransMeta.translate("ZC_FUND_REMARK")));

//    }
//    if (isUseBi) {
//      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME, "senddocName", LangTransMeta
//        .translate(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME)));
//    } else {
//      biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_SENDDOC_CODE, "senddocCode", LangTransMeta
//        .translate(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME)));
//    }

    
//    biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_YJBA_SUM, "zcBiYjbaSum", "合同备案金额"));


//
//    biInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE, "paytypeName", LangTransMeta
//
//    .translate(ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE)));

    //去掉资金构成里面的资金附件

    //    biInfo.add(new ColumnBeanPropertyPair("ZC_FUND_FILE", "zcFundFile", LangTransMeta.translate("ZC_FUND_FILE")));

  }
  public static List<ColumnBeanPropertyPair> getBiInfo() {

    return biInfo;

  }

  public static void setBiInfo(List<ColumnBeanPropertyPair> biInfo) {

    ZcPProBalChgToTableModelConverter.biInfo = biInfo;

  }

  public static TableModel convertSubBiTableDataWithOutBudget(List<ZcPProMitemBiChg> zcPProChgBiList, Map wfCanEditFieldMap) {
    // TCJLODO Auto-generated method stub

    BeanTableModel<ZcPProMitemBiChg> tm = new BeanTableModel<ZcPProMitemBiChg>() {

      @Override
      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);

//        if ("ZC_BI_YJBA_SUM".equals(columnId) || "ZC_FUND_FILE_BLOBID".equals(columnId) || "ZC_BI_DO_SUM".equals(columnId)
//          || "ZC_BI_SUM".equals(columnId)) {
//
//          return false;
//
//        }

//        if (ZcUtil.useBudget() && "ZC_BI_NO".equals(columnId) && ZcUtil.isYsdw()) {
//          return true;
//        }
//        if ("ZC_FUND_CODE".equals(columnId) || ZcElementConstants.FIELD_TRANS_ORIGIN_NAME.equals(columnId) || "ZC_PAYTYPE_CODE".equals(columnId)
//          || "ZC_SENDDOC_NAME".equals(columnId) ) {
//          return false;
//        }
//        if (ZcElementConstants.FIELD_TRANS_B_ACC_NAME.equals(columnId) || ZcElementConstants.FIELD_TRANS_PROJ_NAME.equals(columnId) || ZcElementConstants.FIELD_TRANS_OUTLAY_NAME.equals(columnId)
//          || "ZC_SENDDOC_NAME".equals(columnId) ) {
//          return false;
//        }

        //        if (wfCanEditFieldMap != null && !wfCanEditFieldMap.containsKey(columnId.substring("ZC_FIELD_".length()))) {

        //          return false;

        //        }

        return super.isCellEditable(row, column);

      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcPProMitemBiChg bean = dataBeanList.get(rowIndex);

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

    for (ZcPProMitemBiChg o : zcPProChgBiList) {

      o.setTempId(Guid.genID());

    }

    //    tm.setOidFieldName("detailCode");

    tm.setDataBean(zcPProChgBiList, biInfoWihtoutBugdet);

    return tm;
  }

  public static List<ColumnBeanPropertyPair> biInfoWihtoutBugdet = new ArrayList<ColumnBeanPropertyPair>();
 static {

    biInfoWihtoutBugdet.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_FUND_CODE, "fundName", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_FUND_CODE)));
    biInfoWihtoutBugdet.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_NO, "zcBiNo", "指标编号"));
    biInfoWihtoutBugdet.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ORIGIN_CODE, "originName", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_ORIGIN_CODE)));
    biInfoWihtoutBugdet.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME, "senddocName", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_SENDDOC_NAME)));
//    biInfo2.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ORIGIN_NAME, "originName", LangTransMeta
//      .translate(ZcElementConstants.FIELD_TRANS_ORIGIN_NAME)));
    biInfoWihtoutBugdet.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_PROJ_NAME, "projectName", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_PROJ_NAME)));
    biInfoWihtoutBugdet.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_B_ACC_NAME, "bAccName", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_B_ACC_NAME)));
    biInfoWihtoutBugdet.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_OUTLAY_NAME, "outlayName", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_OUTLAY_NAME)));
//    biInfo2.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_BT_NAME, "btName", LangTransMeta
//      .translate(ZcElementConstants.FIELD_TRANS_BT_NAME)));//是否监督使用
//    biInfo2.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_GB_NAME, "gbName", LangTransMeta
//      .translate(ZcElementConstants.FIELD_TRANS_GB_NAME)));//是否政府采购
    biInfoWihtoutBugdet.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_SUM, "zcBiSum", "指标总金额"));
//    biInfo2.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_DO_SUM, "zcBiDoSum", "指标可用金额"));

    biInfoWihtoutBugdet.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BI_JHUA_SUM, "zcBiJhuaSum", LangTransMeta
    .translate(ZcElementConstants.FIELD_TRANS_ZC_BI_JHUA_SUM)));

    biInfoWihtoutBugdet.add(new ColumnBeanPropertyPair("ZC_FUND_REMARK", "zcFundRemark", LangTransMeta.translate("ZC_FUND_REMARK")));


}


  public static TableModel convertOldBiTableData(List<ZcPProMitemBiChg> biList, final Map wfCanEditFieldMap) {

    BeanTableModel<ZcPProMitemBiChg> tm = new BeanTableModel<ZcPProMitemBiChg>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcPProMitemBiChg bean = dataBeanList.get(rowIndex);

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

    for (ZcPProMitemBi o : biList) {

      o.setTempId(Guid.genID());

    }

    //    tm.setOidFieldName("detailCode");

    tm.setDataBean(biList, biInfoWihtBudget);

    return tm;

  }

  public static TableModel convertOldBiTableDataWithOutBudget(List<ZcPProMitemBiChg> zcPProChgBiList, Map wfCanEditFieldMap) {
    // TCJLODO Auto-generated method stub

    BeanTableModel<ZcPProMitemBiChg> tm = new BeanTableModel<ZcPProMitemBiChg>() {

      @Override
      public boolean isCellEditable(int row, int column) {

        return false;

      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcPProMitemBiChg bean = dataBeanList.get(rowIndex);

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

    for (ZcPProMitemBiChg o : zcPProChgBiList) {

      o.setTempId(Guid.genID());

    }

    //    tm.setOidFieldName("detailCode");

    tm.setDataBean(zcPProChgBiList, biInfoWihtoutBugdet);

    return tm;
  }

  public static TableModel convertHtTableData(List<ZcXmcgHt> htLst) {

    BeanTableModel<ZcXmcgHt> tm = new BeanTableModel<ZcXmcgHt>() {

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }

    };

    tm.setOidFieldName("zcHtCode");

    tm.setDataBean(htLst, htInfo);
    return tm;

  }

  public static List<ColumnBeanPropertyPair> htInfo = new ArrayList<ColumnBeanPropertyPair>();
  static{
    htInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_CO_NAME, "coName", LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_NAME)));
    
    htInfo.add(new ColumnBeanPropertyPair("ZC_SU_NAME", "zcSuName", "中标供应商"));

    htInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_HT_NAME, "zcHtName", LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NAME)));

    htInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_HT_CODE, "zcHtCode", LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_CODE)));

    htInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_HT_NUM, "zcHtNum", LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NUM)));

    htInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_HT_STATUS, "zcHtStatus", LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_STATUS)));
    
  }
}
