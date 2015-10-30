package com.ufgov.zc.client.common.converter.zc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.AsCompoMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.model.AsFile;
import com.ufgov.zc.common.zc.model.TreeNodeValueObject;
import com.ufgov.zc.common.zc.model.ZcEbBidCondition;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbPackQua;
import com.ufgov.zc.common.zc.model.ZcEbPackReq;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.model.ZcEbProjZbFile;
import com.ufgov.zc.common.zc.model.ZcEbXunJia;

@SuppressWarnings("unchecked")
public class ZcEbProjectToTableModelConverter {

  public static DefaultTableModel convertToTableModel(List projectList) {

    MyTableModel tableModel = null;

    Vector<String> names = new Vector<String>();

    Vector values = new Vector();

    prepareColumnHeader(names);

    if (projectList != null && projectList.size() > 0) {

      for (int i = 0; i < projectList.size(); i++) {

        Vector rowData = new Vector();

        ZcEbProj zcEbProj = (ZcEbProj) projectList.get(i);

        rowData.add(zcEbProj.getProjCode());

        rowData.add(zcEbProj.getProjName());

        rowData.add(zcEbProj.getProjSum());

        rowData.add(AsValDataCache.getName("ZC_VS_PITEM_OPIWAY", zcEbProj.getPurType()));

        rowData.add(zcEbProj.getManager());

        rowData.add(zcEbProj.getProjDate());

        //        rowData.add(zcEbProj.getPhone());

        //        rowData.add(zcEbProj.getEmail());

        //        rowData.add(zcEbProj.getFax());

        //        rowData.add(AsValDataCache.getName("VS_Y/N", zcEbProj.getIsSplitPack()));

        //        rowData.add(AsValDataCache.getName("VS_Y/N", zcEbProj.getIsPubPurBulletin()));

        //        rowData.add(AsValDataCache.getName("VS_Y/N", zcEbProj.getIsPubPurResult()));

        //        rowData.add(zcEbProj.getNd());

        //        rowData.add(AsValDataCache.getName("ZC_VS_PROJ_STATUS", zcEbProj.getStatus()));

        //        rowData.add(zcEbProj.getProcessInstId());

        values.add(rowData);

      }

    }

    tableModel = new MyTableModel(values, names) {

      private static final long serialVersionUID = 3123244681117334262L;

      @Override
      public Class getColumnClass(int column) {

        if (column >= 0 && column < getColumnCount() && this.getRowCount() > 0) {

          for (int row = 0; row < this.getRowCount(); row++) {

            if (getValueAt(row, column) != null) {

              return getValueAt(row, column).getClass();

            }

          }

        }

        return Object.class;

      }

      @Override
      public boolean isCellEditable(int row, int colum) {

        return false;

      }

    };

    tableModel.setList(projectList);

    return tableModel;

  }

  private static void prepareColumnHeader(Vector<String> names) {

    //names.add(LangTransMeta.translate("ZC_EB_FIELD_PROJ_CODE"));

    names.add("项目编号");

    //names.add(LangTransMeta.translate("ZC_EB_FIELD_PROJ_NAME"));

    names.add("项目名称");

    //names.add(LangTransMeta.translate("ZC_EB_FIELD_PROJ_SUM"));

    names.add("项目预算");

    //names.add(LangTransMeta.translate("ZC_EB_FIELD_PUR_TYPE"));

    names.add("采购方式");

    //names.add(LangTransMeta.translate("ZC_EB_FIELD_MANAGER"));

    names.add("项目负责人");

    //names.add(LangTransMeta.translate("ZC_EB_FIELD_PROJ_DATE"));

    names.add("立项时间");

    //names.add(LangTransMeta.translate("ZC_EB_FIELD_PHONE"));

    //    names.add("项目负责人电话");

    //    //names.add(LangTransMeta.translate("ZC_EB_FIELD_EMAIL"));

    //    names.add("项目负责人电子邮件");

    //    //names.add(LangTransMeta.translate("ZC_EB_FIELD_FAX"));

    //    names.add("项目负责人传真");

    //names.add(LangTransMeta.translate("ZC_EB_FIELD_IS_SPLIT_PACK"));

    //    names.add("是否划分标段");

    //    //names.add(LangTransMeta.translate("ZC_EB_FIELD_IS_PUB_PUR_BULLETIN"));

    //    names.add("是否发布采购公告");

    //    //names.add(LangTransMeta.translate("ZC_EB_FIELD_IS_PUB_PUR_RESULT"));

    //    names.add("是否发布采购结果");

    //    //names.add(LangTransMeta.translate("ND"));

    //    names.add("年度");

    //    //names.add(LangTransMeta.translate("STATUS"));

    //    names.add("项目状态");

    //    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_PROCESS_INST_ID));

  }

  public static void loadObjectData(Vector rowData, ZcEbProj zcEbProj) {

    rowData.add(zcEbProj.getProjCode());

    rowData.add(zcEbProj.getProjName());

    rowData.add(zcEbProj.getProjSum());

    rowData.add(AsValDataCache.getName("ZC_VS_PITEM_OPIWAY", zcEbProj.getPurType()));

    rowData.add(zcEbProj.getManager());

    rowData.add(zcEbProj.getProjDate());

    rowData.add(zcEbProj.getPhone());

    rowData.add(zcEbProj.getEmail());

    rowData.add(zcEbProj.getFax());

    rowData.add(AsValDataCache.getName("VS_Y/N", zcEbProj.getIsSplitPack()));

    rowData.add(AsValDataCache.getName("VS_Y/N", zcEbProj.getIsPubPurBulletin()));

    rowData.add(AsValDataCache.getName("VS_Y/N", zcEbProj.getIsPubPurResult()));

    rowData.add(zcEbProj.getNd());

    rowData.add(AsValDataCache.getName("ZC_VS_PROJ_STATUS", zcEbProj.getStatus()));

    rowData.add(zcEbProj.getProcessInstId());

  }

  public static DefaultTableModel convertToTableModel_Change(List projectList) {

    MyTableModel tableModel = null;

    Vector<String> names = new Vector<String>();

    Vector values = new Vector();

    names.add("原项目编号");

    prepareColumnHeader(names);

    if (projectList != null && projectList.size() > 0) {

      for (int i = 0; i < projectList.size(); i++) {

        Vector rowData = new Vector();

        ZcEbProj zcEbProj = (ZcEbProj) projectList.get(i);

        rowData.add(zcEbProj.getProjSrcCode());

        loadObjectData(rowData, zcEbProj);

        values.add(rowData);

      }

    }

    tableModel = new MyTableModel(values, names) {

      private static final long serialVersionUID = 3123244681117334262L;

      @Override
      public Class getColumnClass(int column) {

        if (column >= 0 && column < getColumnCount() && this.getRowCount() > 0) {

          for (int row = 0; row < this.getRowCount(); row++) {

            if (getValueAt(row, column) != null) {

              return getValueAt(row, column).getClass();

            }

          }

        }

        return Object.class;

      }

      @Override
      public boolean isCellEditable(int row, int colum) {

        return false;

      }

    };

    tableModel.setList(projectList);

    return tableModel;

  }

  public static void getTableColumnBean() {

  }

  private static List<ColumnBeanPropertyPair> packTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    packTableColumnInfo.add(new ColumnBeanPropertyPair("PACK_NAME", "packName", "分包编号"));

    packTableColumnInfo.add(new ColumnBeanPropertyPair("PACK_DESC", "packDesc", "分包名称"));

    packTableColumnInfo.add(new ColumnBeanPropertyPair("ENTRUST_CODE", "entrust.sn","采购计划"));

    packTableColumnInfo.add(new ColumnBeanPropertyPair("CO_Name", "coName", "采购单位"));

    packTableColumnInfo.add(new ColumnBeanPropertyPair("PUR_TYPE", "purType", "采购方式"));

    packTableColumnInfo.add(new ColumnBeanPropertyPair("PACK_BUDGET", "packBudget", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_ZC_EB_FIELD_PACK_BUDGET)));

    packTableColumnInfo.add(new ColumnBeanPropertyPair("BID_DEPOSIT", "bidDeposit", "投标保证金"));
    
    packTableColumnInfo.add(new ColumnBeanPropertyPair("IS_CHECK_QUALIFICATION", "isCheckQualification", "是否资格预审"));
    
//    packTableColumnInfo.add(new ColumnBeanPropertyPair("PACK_MAX_PRICE", "packMaxPrice", "最高限额"));

  }

  private static List<ColumnBeanPropertyPair> packReqTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

//    packReqTableColumnInfo.add(new ColumnBeanPropertyPair("NAME1", "requirementDetail.name", "选择明细需求"));

    packReqTableColumnInfo.add(new ColumnBeanPropertyPair("NAME", "requirementDetail.name", "选择明细需求"));

    packReqTableColumnInfo.add(new ColumnBeanPropertyPair("ZC_CATALOGUE_CODE", "requirementDetail.zcCatalogueCode", "品目代码"));

    packReqTableColumnInfo.add(new ColumnBeanPropertyPair("ZC_CATALOGUE_NAME", "requirementDetail.zcCatalogueName", "品目名称"));

    packReqTableColumnInfo.add(new ColumnBeanPropertyPair("ARR_DATE", "requirementDetail.arrDate", "要求到货日期"));

    packReqTableColumnInfo.add(new ColumnBeanPropertyPair("BASE_REQ", "requirementDetail.baseReq", "基本规格要求"));

    packReqTableColumnInfo.add(new ColumnBeanPropertyPair("NUM", "requirementDetail.num", "数量"));

    packReqTableColumnInfo.add(new ColumnBeanPropertyPair("ITEM_SUM", "requirementDetail.itemSum", "预算金额"));

    packReqTableColumnInfo.add(new ColumnBeanPropertyPair("ITEM_ATTACH", "requirementDetail.itemAttach", "采购明细附件"));

    packReqTableColumnInfo.add(new ColumnBeanPropertyPair("DESCRIPTION", "requirementDetail.description", "描述"));

  }

  private static List<ColumnBeanPropertyPair> inviteConTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    inviteConTableColumnInfo.add(new ColumnBeanPropertyPair("CON_DESC", "conDesc", "资质条件"));

  }

  private static List<ColumnBeanPropertyPair> xunjiaTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    xunjiaTableColumnInfo.add(new ColumnBeanPropertyPair("SP_NAME", "spName", "名称"));

    xunjiaTableColumnInfo.add(new ColumnBeanPropertyPair("SP_BRAND", "spBrand", "品牌"));

    xunjiaTableColumnInfo.add(new ColumnBeanPropertyPair("SP_TECH", "spTech", "规格要求"));

    xunjiaTableColumnInfo.add(new ColumnBeanPropertyPair("SP_TECH_FILE_NAME", "spTechFileName", "规格附件"));

    xunjiaTableColumnInfo.add(new ColumnBeanPropertyPair("SP_NUM", "spNum", "数量"));

    xunjiaTableColumnInfo.add(new ColumnBeanPropertyPair("SP_UNIT", "unit", "单位"));

  }

  private static List<ColumnBeanPropertyPair> packReqXunJiaTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    packReqXunJiaTableColumnInfo.add(new ColumnBeanPropertyPair("NAME1", "requirementDetail.name", "选择明细需求"));

    packReqXunJiaTableColumnInfo.add(new ColumnBeanPropertyPair("NAME", "requirementDetail.name", "明细需求名称"));

    packReqXunJiaTableColumnInfo.add(new ColumnBeanPropertyPair("ZC_CATALOGUE_CODE", "requirementDetail.zcCatalogueCode", "品目代码"));

    packReqXunJiaTableColumnInfo.add(new ColumnBeanPropertyPair("ZC_CATALOGUE_NAME", "requirementDetail.zcCatalogueName", "品目名称"));

    packReqXunJiaTableColumnInfo.add(new ColumnBeanPropertyPair("SP_BRAND", "requirementDetail.spBrand", "品牌"));

    packReqXunJiaTableColumnInfo.add(new ColumnBeanPropertyPair("ARR_DATE", "requirementDetail.arrDate", "要求到货日期"));

    packReqXunJiaTableColumnInfo.add(new ColumnBeanPropertyPair("BASE_REQ", "requirementDetail.baseReq", "基本规格要求"));

    packReqXunJiaTableColumnInfo.add(new ColumnBeanPropertyPair("NUM", "requirementDetail.num", "数量"));

    packReqXunJiaTableColumnInfo.add(new ColumnBeanPropertyPair("SP_UNIT", "requirementDetail.spUnit", "单位"));

    packReqXunJiaTableColumnInfo.add(new ColumnBeanPropertyPair("ITEM_SUM", "requirementDetail.itemSum", "预算金额"));

    packReqXunJiaTableColumnInfo.add(new ColumnBeanPropertyPair("ITEM_ATTACH", "requirementDetail.itemAttach", "采购明细附件"));

    packReqXunJiaTableColumnInfo.add(new ColumnBeanPropertyPair("DESCRIPTION", "requirementDetail.description", "描述"));

  }

  private static List<ColumnBeanPropertyPair> packQuaTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    packQuaTableColumnInfo.add(new ColumnBeanPropertyPair("QUALIF_NAME", "qualifName", "资质名称"));

  }

  public static List<ColumnBeanPropertyPair> getPackQuaTableColumnInfo() {
    return packQuaTableColumnInfo;
  }

  public static List<ColumnBeanPropertyPair> getInviteConTableColumnInfo() {

    return inviteConTableColumnInfo;

  }

  public static List<ColumnBeanPropertyPair> getPackTableColumnInfo() {

    return packTableColumnInfo;

  }

  public static List<ColumnBeanPropertyPair> getPackReqTableColumnInfo() {

    return packReqTableColumnInfo;

  }

  public static List<ColumnBeanPropertyPair> getPackReqXunJiaTableColumnInfo() {

    return packReqXunJiaTableColumnInfo;

  }

  public static TableModel convertPackToTableModel(List<ZcEbPack> packList) {

    BeanTableModel<ZcEbPack> tm = new BeanTableModel<ZcEbPack>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public boolean isCellEditable(int row, int column) {

        if (!this.isEditable()) {

          return false;

        }

        String columnId = this.getColumnIdentifier(column);

        if (//"PACK_BUDGET".equals(columnId) ||
        		"CO_CODE".equals(columnId)

//        || "PACK_DESC".equals(columnId) 
        || "PROJ_CODE".equals(columnId) || "PUR_TYPE".equals(columnId)

//        || "ENTRUST_CODE".equals(columnId)
        ) {

          return false;

        }
        if("PACK_MAX_PRICE".equals(columnId)){
          ZcEbPack pk = this.getBean(row);
          if(!ZcSettingConstants.PITEM_OPIWAY_XJ.equals(pk.getPurType())
            || pk.getEntrust() == null || !"Y".equals(pk.getEntrust().getIsCar())){
            return false;
          }
        }

        return true;

      }

    };

    tm.setOidFieldName("tempId");

    for (ZcEbPack data : packList) {
//      data.getEntrust().setCoName()
      data.setTempId(Guid.genID());

    }

    tm.setDataBean(packList, packTableColumnInfo);

    return tm;

  }

  public static TableModel convertPackReqToTableModel(List<ZcEbPackReq> packReqList, final boolean isEnable) {

    BeanTableModel<ZcEbPackReq> tm = new BeanTableModel<ZcEbPackReq>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcEbPackReq bean = getDataBeanList().get(rowIndex);

        if ("ITEM_ATTACH".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {

            bean.getRequirementDetail().setItemAttach(null);

            bean.getRequirementDetail().setItemAttachBlobid(null);

          } else {

            bean.getRequirementDetail().setItemAttach(((AsFile) aValue).getFileName());

            bean.getRequirementDetail().setItemAttachBlobid(((AsFile) aValue).getFileId());

          }

          putEditedData(bean);

          fireTableCellUpdated(rowIndex, columnIndex);

        } else if ("ZC_CATALOGUE_CODE".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {

            this.getBean(rowIndex).getRequirementDetail().setZcCatalogueCode(null);

            this.getBean(rowIndex).getRequirementDetail().setZcCatalogueName(null);

          } else {

            this.getBean(rowIndex).getRequirementDetail().setZcCatalogueCode(((TreeNodeValueObject) aValue).getCode());

            this.getBean(rowIndex).getRequirementDetail().setZcCatalogueName(((TreeNodeValueObject) aValue).getName());

          }

          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(dataBeanList.get(rowIndex));

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }

      }

      @Override
      public boolean isCellEditable(int row, int column) {

        //        String columnId = this.getColumnIdentifier(column);

        //        if ("PACK_REQ_CODE".equals(columnId) || "PACK_CODE".equals(columnId) || "PROJ_CODE".equals(columnId) || "SN".equals(columnId)
        //
        //          || "ZC_CATALOGUE_CODE".equals(columnId) || "ZC_CATALOGUE_NAME".equals(columnId) || "NUM".equals(columnId)
        //          || "NAME".equals(columnId) || "ITEM_SUM".equals(columnId)) {
        //
        //          return false;
        //
        //        }
        if (!isEnable) {
          return false;
        }
        return isEditable();

      }

    };

    tm.setOidFieldName("tempId");

    tm.setDataBean(packReqList, packReqTableColumnInfo);

    return tm;

  }

  public static TableModel convertBidConToTableModel(List<ZcEbBidCondition> inviteConLst) {

    BeanTableModel<ZcEbBidCondition> tm = new BeanTableModel<ZcEbBidCondition>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public boolean isCellEditable(int row, int column) {

        return true;

      }

    };

    tm.setOidFieldName("conCode");

    tm.setDataBean(inviteConLst, inviteConTableColumnInfo);

    return tm;

  }

  public static TableModel convertXunJiaToTableModel(List<ZcEbXunJia> inviteConLst) {

    BeanTableModel<ZcEbXunJia> tm = new BeanTableModel<ZcEbXunJia>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcEbXunJia bean = getDataBeanList().get(rowIndex);

        if ("SP_TECH_FILE_NAME".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {

            bean.setSpTechFileName(null);

            bean.setSpTechFileId(null);

          } else {

            bean.setSpTechFileName(((AsFile) aValue).getFileName());

            bean.setSpTechFileId(((AsFile) aValue).getFileId());

          }

          putEditedData(bean);

          fireTableCellUpdated(rowIndex, columnIndex);

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }

      }

      @Override
      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);

        if ("SP_NAME".equals(columnId) || "SP_BRAND".equals(columnId) || "SP_TECH".equals(columnId) || "SP_TECH_FILE_NAME".equals(columnId)

        || "SP_NUM".equals(columnId) || "SP_UNIT".equals(columnId)) {

          return true;

        }

        return false;

      }

    };

    tm.setOidFieldName("xjCode");

    tm.setDataBean(inviteConLst, xunjiaTableColumnInfo);

    return tm;

  }

  private static List<ColumnBeanPropertyPair> projFileTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    projFileTableColumnInfo.add(new ColumnBeanPropertyPair("TYPE", "type", "类型"));

    projFileTableColumnInfo.add(new ColumnBeanPropertyPair("FILE_NAME", "fileName", "文件名称"));

    projFileTableColumnInfo.add(new ColumnBeanPropertyPair("REMARK", "remark", "备注"));

    //    projFileTableColumnInfo.add(new ColumnBeanPropertyPair("PROJ_CODE", "projCode", "项目编号"));

    //    projFileTableColumnInfo.add(new ColumnBeanPropertyPair("FILE_ID", "fileId", "文件id"));

    //    projFileTableColumnInfo.add(new ColumnBeanPropertyPair("CREATOR", "creator", "创建人"));    

    //    projFileTableColumnInfo.add(new ColumnBeanPropertyPair("UPLOADER", "uploader", "上传人"));

    //    projFileTableColumnInfo.add(new ColumnBeanPropertyPair("CREATE_TIME", "createTime", "创建时间"));

    //    projFileTableColumnInfo.add(new ColumnBeanPropertyPair("UPLOAD_TIME", "uploadTime", "上传时间"));

  }

  public static TableModel convertProjFileToTableModel(List<ZcEbProjZbFile> projFileLst) {

    BeanTableModel<ZcEbProjZbFile> tm = new BeanTableModel<ZcEbProjZbFile>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcEbProjZbFile bean = getDataBeanList().get(rowIndex);

        if ("FILE_NAME".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {

            bean.setFileName(null);

            bean.setFileId(null);

          } else {

            bean.setFileName(((AsFile) aValue).getFileName());

            bean.setFileId(((AsFile) aValue).getFileId());

          }

          putEditedData(bean);

          fireTableCellUpdated(rowIndex, columnIndex);

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }

      }

      @Override
      public boolean isCellEditable(int row, int column) {

        //        String columnId = this.getColumnIdentifier(column);

        //        if ("SP_NAME".equals(columnId) || "SP_TECH".equals(columnId)

        //          || "SP_TECH_FILE_NAME".equals(columnId) || "SP_NUM".equals(columnId)) {

        //          return true;

        //        }

        //        return false;

        return true;

      }

    };

    tm.setOidFieldName("tempId");

    for (ZcEbProjZbFile data : projFileLst) {

      data.setTempId(Guid.genID());

    }

    tm.setDataBean(projFileLst, projFileTableColumnInfo);

    return tm;

  }

  public static TableModel convertPackReqXunJiaToTableModel(List<ZcEbPackReq> packReqList) {

    BeanTableModel<ZcEbPackReq> tm = new BeanTableModel<ZcEbPackReq>() {

      private static final long serialVersionUID = 6888363838628062064L;

      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcEbPackReq bean = getDataBeanList().get(rowIndex);

        if ("ITEM_ATTACH".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {

            bean.getRequirementDetail().setItemAttach(null);

            bean.getRequirementDetail().setItemAttachBlobid(null);

          } else {

            bean.getRequirementDetail().setItemAttach(((AsFile) aValue).getFileName());

            bean.getRequirementDetail().setItemAttachBlobid(((AsFile) aValue).getFileId());

          }

          putEditedData(bean);

          fireTableCellUpdated(rowIndex, columnIndex);

        } else if ("ZC_CATALOGUE_CODE".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {

            this.getBean(rowIndex).getRequirementDetail().setZcCatalogueCode(null);

            this.getBean(rowIndex).getRequirementDetail().setZcCatalogueName(null);

          } else {

            this.getBean(rowIndex).getRequirementDetail().setZcCatalogueCode(((TreeNodeValueObject) aValue).getCode());

            this.getBean(rowIndex).getRequirementDetail().setZcCatalogueName(((TreeNodeValueObject) aValue).getName());

          }

          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(dataBeanList.get(rowIndex));

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }

      }

      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);

        if (!"SP_UNIT".equals(columnId) && !"SP_BRAND".equals(columnId) && !"ARR_DATE".equals(columnId)) {

          return false;

        }

        return isEditable();

      }

    };

    tm.setOidFieldName("tempId");

    tm.setDataBean(packReqList, packReqXunJiaTableColumnInfo);

    return tm;

  }

  public static TableModel convertPackQuaToTableModel(List<ZcEbPackQua> packQuaList, final boolean isEnable) {

    BeanTableModel<ZcEbPackQua> tm = new BeanTableModel<ZcEbPackQua>() {

      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);

        if ("PACK_CODE".equals(columnId) || "PACK_NAME".equals(columnId)) {

          return false;

        }
        if (!isEnable) {
          return false;
        }
        return isEditable();

      }


      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        if ("QUALIF_NAME".equals(this.getColumnIdentifier(columnIndex))) {

          Object obj = getValueAt(rowIndex, columnIndex);

          if (aValue == null) {

            this.getBean(rowIndex).setQualifId(null);

            this.getBean(rowIndex).setQualifName(null);

          } else {

            this.getBean(rowIndex).setQualifId(((TreeNodeValueObject) aValue).getCode());

            this.getBean(rowIndex).setQualifName(((TreeNodeValueObject) aValue).getName());

          }

          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(dataBeanList.get(rowIndex));

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }

      }

    };

    tm.setOidFieldName("tempId");

    tm.setDataBean(packQuaList, packQuaTableColumnInfo);

    return tm;

  }

  public static TableModel convertPackReqToTableModel(List<ZcEbPackReq> packReqList) {

    BeanTableModel<ZcEbPackReq> tm = new BeanTableModel<ZcEbPackReq>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcEbPackReq bean = getDataBeanList().get(rowIndex);

        if ("ITEM_ATTACH".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {

            bean.getRequirementDetail().setItemAttach(null);

            bean.getRequirementDetail().setItemAttachBlobid(null);

          } else {

            bean.getRequirementDetail().setItemAttach(((AsFile) aValue).getFileName());

            bean.getRequirementDetail().setItemAttachBlobid(((AsFile) aValue).getFileId());

          }

          putEditedData(bean);

          fireTableCellUpdated(rowIndex, columnIndex);

        }else  if ("ZC_CATALOGUE_CODE".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {

            this.getBean(rowIndex).getRequirementDetail().setZcCatalogueCode(null);

            this.getBean(rowIndex).getRequirementDetail().setZcCatalogueName(null);

          } else {

            this.getBean(rowIndex).getRequirementDetail().setZcCatalogueCode(((TreeNodeValueObject) aValue).getCode());

            this.getBean(rowIndex).getRequirementDetail().setZcCatalogueName(((TreeNodeValueObject) aValue).getName());

          }

          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(dataBeanList.get(rowIndex));

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

        if ("PACK_REQ_CODE".equals(columnId) || "PACK_CODE".equals(columnId) || "PROJ_CODE".equals(columnId) || "SN".equals(columnId)

        || "ZC_CATALOGUE_NAME".equals(columnId)) {

          return false;

        }

        return true;

      }

    };

    tm.setOidFieldName("tempId");

    for (ZcEbPackReq data : packReqList) {

      data.setTempId(Guid.genID());

    }

    tm.setDataBean(packReqList, packReqTableColumnInfo);

    return tm;

  }

}
