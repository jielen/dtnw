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
import com.ufgov.zc.common.commonbiz.model.BaseElement;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.zc.model.EmExpertAbilityHistory;
import com.ufgov.zc.common.zc.model.EmExpertEvaluation;
import com.ufgov.zc.common.zc.model.EmExpertSelectionBill;

public class ZcEmExpertAbilityToTableModelConverter {
  //===================================================下面程序对针对评标的评价进行处理===============================================================
  
  public static String PROJ_NAME = "PROJ_NAME";

  public static String PACK_CODE = "PACK_CODE";

  public static String PACK_NAME = "PACK_NAME";
  
  public static String PACK_DESC = "PACK_DESC";
  
  public static String EM_EXPERT_INDEX1 = "EM_EXPERT_INDEX1";

  public static String EM_EXPERT_INDEX2 = "EM_EXPERT_INDEX2";

  public static String EM_EXPERT_INDEX3 = "EM_EXPERT_INDEX3";

  public static String EM_EXPERT_INDEX4 = "EM_EXPERT_INDEX4";

  public static String EM_EXPERT_INDEX5 = "EM_EXPERT_INDEX5";

  public static String EM_EXPERT_INDEX6 = "EM_EXPERT_INDEX6";

  public static String EM_EXPERT_INDEX7 = "EM_EXPERT_INDEX7";

  public static String EM_EXPERT_INDEX8 = "EM_EXPERT_INDEX8";

  public static String EM_EXPERT_INDEX9 = "EM_EXPERT_INDEX9";

  public static String EM_EXPERT_INDEX10 = "EM_EXPERT_INDEX10";

  public static String EM_EXPERT_INDEX11 = "EM_EXPERT_INDEX11";

  public static String EM_EXPERT_INDEX12 = "EM_EXPERT_INDEX12";

  public static String EM_EXPERT_INDEX13 = "EM_EXPERT_INDEX13";

  public static List<ColumnBeanPropertyPair> expertEvalColumns;

  public static TableModel convertEmExpertEvaluationToTableMode(List<EmExpertEvaluation> ol) {

    BeanTableModel<EmExpertEvaluation> tm = new BeanTableModel<EmExpertEvaluation>() {

      private static final long serialVersionUID = 5844039329387565537L;

      @Override
      public boolean isCellEditable(int row, int column) {

        return true;

      }

    };

    tm.setOidFieldName("tempId");

    if (ol != null) {

      for (EmExpertEvaluation o : ol) {

        o.setTempId(Guid.genID());

      }

    }

    expertEvalColumns = new ArrayList<ColumnBeanPropertyPair>();

    expertEvalColumns.add(new ColumnBeanPropertyPair(PROJ_NAME, "projName", "项目名称"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(PACK_CODE, "packCode", LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_CODE)));

    expertEvalColumns.add(new ColumnBeanPropertyPair(PACK_NAME, "packName", LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_NAME)));

    expertEvalColumns.add(new ColumnBeanPropertyPair(PACK_DESC, "packDesc", "标段描述"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX1, "emExpertIndex1", "综合评价"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX2, "emExpertIndex2", "政府采购法律法规认知程度"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX3, "emExpertIndex3", "业务水平和技能"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX4, "emExpertIndex4", "带有倾向性或歧视性意见"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX5, "emExpertIndex5", "有明示或暗示其他评委自己评定的中标结果"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX6, "emExpertIndex6", "是否加强采购文件以外的条件进行评审"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX7, "emExpertIndex7", "是否出现评审错误"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX8, "emExpertIndex8", "有迟到现象"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX9, "emExpertIndex9", "有早退现象"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX10, "emExpertIndex10", "是否违反对外通讯规定的行为"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX11, "emExpertIndex11", "是否擅自离开评标现场"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX12, "emExpertIndex12", "是否违反评标程序评审"));

    expertEvalColumns.add(new ColumnBeanPropertyPair(EM_EXPERT_INDEX13, "emExpertIndex13", "是否完成评标签字"));

    tm.setDataBean(ol, expertEvalColumns);

    return tm;

  }

  //===================================================下面程序对评审历史表进行处理===============================================================
  private static List<ColumnBeanPropertyPair> BillDetailInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    BillDetailInfo.add(new ColumnBeanPropertyPair("emExpertAbilityDesc",     "emExpertAbilityDesc",   LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_EM_EXPERT_ABILITY_DESC )));     
   
    BillDetailInfo.add(new ColumnBeanPropertyPair("emExpertRatingStartDate", "emExpertRatingStartDate",LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_EM_EXPERT_RATING_START_DATE ))); 
    
    BillDetailInfo.add(new ColumnBeanPropertyPair("emExpertRatingEndDate",   "emExpertRatingEndDate",  LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_EM_EXPERT_RATING_END_DATE ))); 
    
    BillDetailInfo.add(new ColumnBeanPropertyPair("emExpertSuperiority",     "emExpertSuperiority",    LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_EM_EXPERT_SUPERIORITY ))); 
    
    BillDetailInfo.add(new ColumnBeanPropertyPair("emExpertImprovement",     "emExpertImprovement",    LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_EM_EXPERT_IMPROVEMENT ))); 
    
    BillDetailInfo.add(new ColumnBeanPropertyPair("emExpertAbilityGrade",     "emExpertAbilityGrade",          LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_EM_EXPERT_ABILITY_GRADE ))); 
    
    BillDetailInfo.add(new ColumnBeanPropertyPair("zcInputCode",             "zcInputCode",            LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_ZC_INPUT_CODE)));  
    
    BillDetailInfo.add(new ColumnBeanPropertyPair("zcInputName",             "zcInputName",            LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_ZC_INPUT_NAME)));  
    
    BillDetailInfo.add(new ColumnBeanPropertyPair("zcInputDate",             "zcInputDate",            LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_ZC_INPUT_DATE))); 
   
  }



  public static TableModel convertSubTableData(List<EmExpertAbilityHistory> biList) {

    BeanTableModel<EmExpertAbilityHistory> tm = new BeanTableModel<EmExpertAbilityHistory>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override

      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);
        
        if ("zcInputCode".equals(columnId) || "zcInputName".equals(columnId) || "zcInputDate".equals(columnId)) {

            return false;

          }
        
        return true;

      }

      @Override

      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        EmExpertAbilityHistory bean = dataBeanList.get(rowIndex);

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

    for (EmExpertAbilityHistory o : biList) {

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

