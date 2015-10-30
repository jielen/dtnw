/**
 * ZcEbSupplierToTableModelConverter.java
 * com.ufgov.gk.client.common.converter.zc
 * Administrator
 * 2010-4-29
 */
package com.ufgov.zc.client.common.converter.zc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.common.commonbiz.model.BaseElement;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.model.AsFile;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.zc.model.TreeNodeValueObject;
import com.ufgov.zc.common.zc.model.ZcEbPackSupplier;
import com.ufgov.zc.common.zc.model.ZcEbSupBsType;
import com.ufgov.zc.common.zc.model.ZcEbSupSingup;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;
import com.ufgov.zc.common.zc.model.ZcEbSupplierFeedback;
import com.ufgov.zc.common.zc.model.ZcEbSupplierJudge;
import com.ufgov.zc.common.zc.model.ZcEbSupplierQualify;
import com.ufgov.zc.common.zc.model.ZcEbSupplierType;
import com.ufgov.zc.common.zc.model.ZcEbZyxm;
import com.ufgov.zc.common.zc.model.ZcPProMitem;

/**
 * @author Administrator
 *
 */
public class ZcEbSupplierToTableModelConverter {

  private static String pageStatus = "NOTEDITABLE";
  
  public static boolean qualityTableEditable=false;

  public static DefaultTableModel convertToTableModel(List supplierList) {
    MyTableModel tableModel = null;
    Vector names = new Vector();
    Vector values = new Vector();

    names.add("供应商代码");
    names.add("证书序列号");
    names.add("供应商名称");
    names.add("联系人");
    names.add("联系电话");
    names.add("邮编");
    names.add("地址");
    //edit by FengYan 2012-02-06
    //    names.add("审核人");
    //    names.add("审核日期");
    //end
    names.add("状态");

    if (supplierList != null && supplierList.size() > 0) {
      for (int i = 0; i < supplierList.size(); i++) {
        Vector rowData = new Vector();
        ZcEbSupplier supplier = (ZcEbSupplier) supplierList.get(i);
        rowData.add(supplier.getCode());
        rowData.add(supplier.getCaSerial());
        rowData.add(supplier.getName());
        rowData.add(supplier.getLinkMan());
        rowData.add(supplier.getLinkManPhone());
        rowData.add(supplier.getZipCode());
        rowData.add(supplier.getAddress());
        //edit by FengYan 2012-02-06
        //rowData.add(supplier.getOperator());
        //rowData.add(supplier.getOperDate());
        //end
        rowData.add(AsValDataCache.getName("ZC_VS_SUPPLIER_STATUS", supplier.getStatus()));
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
    tableModel.setList(supplierList);
    return tableModel;
  }

  /**
   * added by mengw 20100714
   * */
  private static List<ColumnBeanPropertyPair> feedbackTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {
    feedbackTableColumnInfo.add(new ColumnBeanPropertyPair("项目名称", "projectName", "项目名称"));
    feedbackTableColumnInfo.add(new ColumnBeanPropertyPair("采购人", "cgr", "采购人"));
    feedbackTableColumnInfo.add(new ColumnBeanPropertyPair("交货方面评价", "jh", "交货方面评价"));
    feedbackTableColumnInfo.add(new ColumnBeanPropertyPair("价格方面评价", "jg", "价格方面评价"));
    feedbackTableColumnInfo.add(new ColumnBeanPropertyPair("质量方面评价", "zl", "质量方面评价"));
    feedbackTableColumnInfo.add(new ColumnBeanPropertyPair("服务方面评价", "fw", "服务方面评价"));
    feedbackTableColumnInfo.add(new ColumnBeanPropertyPair("备注", "bz", "备注"));
  }

  public static TableModel convertFeedbackToTableModel(List<ZcEbSupplierFeedback> reqList) {
    BeanTableModel<ZcEbSupplierFeedback> tm = new BeanTableModel<ZcEbSupplierFeedback>() {
      public boolean isCellEditable(int row, int column) {
        if (!"EDITABLE".equalsIgnoreCase(pageStatus))
          return false;
        return true;
      }
    };

    tm.setOidFieldName("objId");
    tm.setDataBean(reqList, feedbackTableColumnInfo);
    return tm;
  }

  private static List<ColumnBeanPropertyPair> judgeTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {
    judgeTableColumnInfo.add(new ColumnBeanPropertyPair("综合得分", "zh", "综合得分"));
    judgeTableColumnInfo.add(new ColumnBeanPropertyPair("响应方面评价", "xy", "响应方面评价"));
    judgeTableColumnInfo.add(new ColumnBeanPropertyPair("价格方面评价", "jg", "价格方面评价"));
    judgeTableColumnInfo.add(new ColumnBeanPropertyPair("质量方面评价", "zl", "质量方面评价"));
    judgeTableColumnInfo.add(new ColumnBeanPropertyPair("服务方面评价", "fw", "服务方面评价"));
    judgeTableColumnInfo.add(new ColumnBeanPropertyPair("其他", "qt", "其他"));
    judgeTableColumnInfo.add(new ColumnBeanPropertyPair("备注", "bz", "备注"));
    judgeTableColumnInfo.add(new ColumnBeanPropertyPair("开始时间", "startDate", "开始时间"));
    judgeTableColumnInfo.add(new ColumnBeanPropertyPair("结束时间", "endDate", "结束时间"));
  }

  public static TableModel convertJudgeToTableModel(List<ZcEbSupplierJudge> reqList) {
    BeanTableModel<ZcEbSupplierJudge> tm = new BeanTableModel<ZcEbSupplierJudge>() {
      public boolean isCellEditable(int row, int column) {
        if (!"EDITABLE".equalsIgnoreCase(pageStatus))
          return false;
        return true;
      }
    };

    tm.setOidFieldName("objId");
    tm.setDataBean(reqList, judgeTableColumnInfo);
    return tm;
  }

  private static List<ColumnBeanPropertyPair> qualifyTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {
    qualifyTableColumnInfo.add(new ColumnBeanPropertyPair("资质名称", "licenseName", "资质名称"));
    qualifyTableColumnInfo.add(new ColumnBeanPropertyPair("资质编号", "licenseNO", "资质编号"));
    qualifyTableColumnInfo.add(new ColumnBeanPropertyPair("发证机关", "licenseIssuingAuthority", "发证机关"));
    qualifyTableColumnInfo.add(new ColumnBeanPropertyPair("有效期起始日期", "effectStartTime", "有效期起始日期"));
    qualifyTableColumnInfo.add(new ColumnBeanPropertyPair("有效期结束日期", "effectEndTime", "有效期结束日期"));
    qualifyTableColumnInfo.add(new ColumnBeanPropertyPair("资质相关附件", "filesName", "资质相关附件"));
    qualifyTableColumnInfo.add(new ColumnBeanPropertyPair("备注", "remark", "备注"));
  }

  public static TableModel convertQualifyToTableModel(List<ZcEbSupplierQualify> qList,final boolean isEditable) {
    BeanTableModel<ZcEbSupplierQualify> tm = new BeanTableModel<ZcEbSupplierQualify>() {
      private static final long serialVersionUID = -4885158926760437970L;


      @Override
      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);

        if ("资质相关附件".equals(columnId) ) {

          return true;

        }
        
        return getQualityTableEditable(isEditable);

      }

      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

 /*       if ("资质编号".equals(this.getColumnIdentifier(columnIndex))) {

          Object obj = getValueAt(rowIndex, columnIndex);

          if (aValue == null) {

            this.getBean(rowIndex).setQualifId(null);

            this.getBean(rowIndex).setQualifName(null);

            this.getBean(rowIndex).setLicenseNO(null);

            this.getBean(rowIndex).setLicenseName(null);

          } else {

            this.getBean(rowIndex).setQualifId(((TreeNodeValueObject) aValue).getCode());

            this.getBean(rowIndex).setQualifName(((TreeNodeValueObject) aValue).getName());

            this.getBean(rowIndex).setLicenseNO(((TreeNodeValueObject) aValue).getCode());

            this.getBean(rowIndex).setLicenseName(((TreeNodeValueObject) aValue).getName());

          }

          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(dataBeanList.get(rowIndex));

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }*/
        
        ZcEbSupplierQualify bean=(ZcEbSupplierQualify)getDataBeanList().get(rowIndex);;
        if ("资质相关附件".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {

            bean.setFilesID(null);

            bean.setFilesName(null);

          } else {

            bean.setFilesName(((AsFile) aValue).getFileName());

            bean.setFilesID(((AsFile) aValue).getFileId());

          }

          putEditedData(bean);

          fireTableCellUpdated(rowIndex, columnIndex);

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }

      }
    };

    tm.setOidFieldName("id");
    tm.setDataBean(qList, qualifyTableColumnInfo);
    return tm;
  }

  protected static boolean getQualityTableEditable(boolean isEditable) {
    // TODO Auto-generated method stub
    if(isEditable){
      return true;
    }
    if(qualityTableEditable){
      return true;
    }
    return false;
  }

  private static List<ColumnBeanPropertyPair> bsTypeTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {
    bsTypeTableColumnInfo.add(new ColumnBeanPropertyPair("类别", "supBsVal", "类别"));
    bsTypeTableColumnInfo.add(new ColumnBeanPropertyPair("备注", "dome", "备注"));
  }

  public static TableModel convertBsTypeToTableModel(List<ZcEbSupBsType> qList) {
    BeanTableModel<ZcEbSupBsType> tm = new BeanTableModel<ZcEbSupBsType>() {
      private static final long serialVersionUID = -4885158926760437970L;

      public boolean isCellEditable(int row, int column) {
        if (!"EDITABLE".equalsIgnoreCase(pageStatus))
          return false;
        return true;
      }
    };

    tm.setOidFieldName("id");
    tm.setDataBean(qList, bsTypeTableColumnInfo);
    return tm;
  }

  public static String getPageStatus() {
    return pageStatus;
  }

  public static void setPageStatus(String pageStatus) {
    ZcEbSupplierToTableModelConverter.pageStatus = pageStatus;
  }

  private static List<ColumnBeanPropertyPair> zyxmTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {
    zyxmTableColumnInfo.add(new ColumnBeanPropertyPair("品目代码", "zcCatalogueCode", "品目代码"));
    zyxmTableColumnInfo.add(new ColumnBeanPropertyPair("品目名称", "zcCatalogueName", "品目名称"));
  }

  public static TableModel convertZyxmTypeToTableModel(List zyxmList) {
    BeanTableModel<ZcEbZyxm> tm = new BeanTableModel<ZcEbZyxm>() {
      private static final long serialVersionUID = -4885158926761437970L;

      public boolean isCellEditable(int row, int column) {
        if (!"EDITABLE".equalsIgnoreCase(pageStatus)) {
          return false;
        } else {
          String columnId = this.getColumnIdentifier(column);
          if ("品目名称".equals(columnId)) {
            return false;
          }
        }
        return true;
      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue == null) {
          this.getBean(rowIndex).setZcCatalogueCode(null);
          this.getBean(rowIndex).setZcCatalogueName(null);
        } else {
          this.getBean(rowIndex).setZcCatalogueCode(((TreeNodeValueObject) aValue).getCode());
          this.getBean(rowIndex).setZcCatalogueName(((TreeNodeValueObject) aValue).getName());
        }
        fireTableCellUpdated(rowIndex, columnIndex);
        putEditedData(dataBeanList.get(rowIndex));
      }
    };

    tm.setOidFieldName("id");
    tm.setDataBean(zyxmList, zyxmTableColumnInfo);
    return tm;
  }

  private static List<ColumnBeanPropertyPair> singupTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    singupTableColumnInfo.add(new ColumnBeanPropertyPair("任务单号", "snCode", "任务单号"));
    singupTableColumnInfo.add(new ColumnBeanPropertyPair("任务名称", "zcMakeName", "任务名称"));
    singupTableColumnInfo.add(new ColumnBeanPropertyPair("项目编号", "projCode", "项目编号"));
    singupTableColumnInfo.add(new ColumnBeanPropertyPair("项目名称", "projName", "项目名称"));
    singupTableColumnInfo.add(new ColumnBeanPropertyPair("分包编号", "packName", "分包编号"));
    singupTableColumnInfo.add(new ColumnBeanPropertyPair("分包名称", "packDesc", "分包名称"));
    singupTableColumnInfo.add(new ColumnBeanPropertyPair("投标日期", "singUpDate", "投标日期"));
    singupTableColumnInfo.add(new ColumnBeanPropertyPair("是否到开标现场", "isSite", "是否到开标现场"));
  }

  public static TableModel convertSingupToTableModel(List singupList) {
    BeanTableModel<ZcEbSupSingup> tm = new BeanTableModel<ZcEbSupSingup>() {
      private static final long serialVersionUID = -4885158926761437970L;

      public boolean isCellEditable(int row, int column) {
        return false;
      }

    };
    tm.setOidFieldName("snCode");

    tm.setDataBean(singupList, singupTableColumnInfo);
    return tm;
  }
  
  private static List<ColumnBeanPropertyPair> supplierTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();
  
  static {
	  supplierTableColumnInfo.add(new ColumnBeanPropertyPair("ZC_SU_CODE", "zcEbSupplier.code", "供应商代码"));
	  supplierTableColumnInfo.add(new ColumnBeanPropertyPair("ZC_SU_NAME", "zcEbSupplier.name", "供应商名称"));
	  supplierTableColumnInfo.add(new ColumnBeanPropertyPair("ZC_SU_LINKMAN", "zcEbSupplier.linkMan", "联系人"));
	  supplierTableColumnInfo.add(new ColumnBeanPropertyPair("ZC_SU_TEL", "zcEbSupplier.linkManPhone", "联系电话"));
	  supplierTableColumnInfo.add(new ColumnBeanPropertyPair("ZC_POST_CODE", "zcEbSupplier.zipCode", "邮编"));
	  supplierTableColumnInfo.add(new ColumnBeanPropertyPair("ZC_SU_ADDR", "zcEbSupplier.address", "地址"));
  }

  public static List<ColumnBeanPropertyPair> getSupplierTableColumnInfo() {
	  return supplierTableColumnInfo;
  }
  
  public static void setSupplierTableColumnInfo(
		  List<ColumnBeanPropertyPair> supplierTableColumnInfo) {
	  ZcEbSupplierToTableModelConverter.supplierTableColumnInfo = supplierTableColumnInfo;
  }
  
  public static TableModel convertSupplierToTableModel(List<ZcEbPackSupplier> supplierList) {
	  
	  BeanTableModel<ZcEbPackSupplier> tm = new BeanTableModel<ZcEbPackSupplier>() {
		  
		  private static final long serialVersionUID = 6888363838628062064L;
		  
		  @Override
		  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			  
			  ZcEbPackSupplier bean = getDataBeanList().get(rowIndex);
			  
			  if ("ZC_CATALOGUE_CODE".equals(this.getColumnIdentifier(columnIndex))) {
				  
				  if (aValue == null) {
					  
					  this.getBean(rowIndex).getZcEbSupplier().setZcCatalogueCode(null);
					  
					  this.getBean(rowIndex).getZcEbSupplier().setZcCatalogueName(null);
					  
				  } else {
					  
					  this.getBean(rowIndex).getZcEbSupplier().setZcCatalogueCode(((TreeNodeValueObject) aValue).getCode());
					  
					  this.getBean(rowIndex).getZcEbSupplier().setZcCatalogueName(((TreeNodeValueObject) aValue).getName());
					  
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
	  
	  tm.setDataBean(supplierList, supplierTableColumnInfo);
	  
	  return tm;
	  
  }
  public static List<ColumnBeanPropertyPair> getGysTypeTableColumnInfo() {
    return gysTypeTableColumnInfo;
  }

  private static List<ColumnBeanPropertyPair> gysTypeTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {
    gysTypeTableColumnInfo.add(new ColumnBeanPropertyPair("typeName", "typeName", "供应商类型"));
    gysTypeTableColumnInfo.add(new ColumnBeanPropertyPair("isSelected", "isSelected", "选中"));
  }
  public static TableModel convertGysTypeToTableModel(List gysTypeList) {
    // TODO Auto-generated method stub

    
    BeanTableModel<ZcEbSupplierType> tm = new BeanTableModel<ZcEbSupplierType>() {
      
      private static final long serialVersionUID = 6888363838628062064L;
      
      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        
        ZcEbSupplierType bean = getDataBeanList().get(rowIndex);
        
        if ("isSelected".equals(this.getColumnIdentifier(columnIndex))) {
          System.out.println("aValue="+aValue);
          if (aValue == null) {            
            this.getBean(rowIndex).setIsSelected(new Boolean(false));
            
          } else {
            if(aValue instanceof Boolean){
              this.getBean(rowIndex).setIsSelected((Boolean) aValue);
            }else{              
              this.getBean(rowIndex).setIsSelected(new Boolean(false));
            }
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
        
        if ("isSelected".equals(columnId) ) {
          
          return true;
          
        }
        
        return false;
        
      }
      
    };
    
    tm.setOidFieldName("typeName");
    
    tm.setDataBean(gysTypeList, gysTypeTableColumnInfo);
    
    return tm;
    
  }

}
