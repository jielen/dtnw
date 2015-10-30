package com.ufgov.zc.client.zc.zcppromake;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_ATTR2;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_CO_CODE_ND;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_CO_NAME;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_IMP_FILE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_IS_IMP;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_IS_REMARK;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MAKE_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MAKE_LINKMAN;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MAKE_SEQUENCE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MAKE_STATUS;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MAKE_TEL;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_PIFU_CGFS;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_PITEM_OPIWAY;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_TRUST_AGEY_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_TRUST_AGEY_NAME;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_ZG_CS_CODE;

import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog.ModalityType;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcPProMakeToTableModelConverter;
import com.ufgov.zc.client.component.AsValComboBox;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.OrgFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFilePathFieldEditor;
import com.ufgov.zc.client.datacache.CompanyDataCache;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.commonbiz.model.Company;
import com.ufgov.zc.common.commonbiz.model.WfAware;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.system.util.Utils;
import com.ufgov.zc.common.zc.checkrule.BaseRule;
import com.ufgov.zc.common.zc.checkrule.ZcMakeCheckRuleBySX;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityTreeHandler;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcEbDaibian;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitem;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;

/**

 * 

 * @author Administrator

 *

 */

@SuppressWarnings("unchecked")
public class ZcPProMakeXMEditPanel extends ZcPProMakeEditPanel {

  //  public String nd;

  public String pf;

  public String valSetId = "1";

  public String fukuanValId;

  public static final long serialVersionUID = -2799110682087878491L;

  public ZcPProMakeXMEditPanel self2 = this;

  ForeignEntityFieldEditor zcAgeyCode;// hjh 2011-5-5 updated

  TextFieldEditor zcAgeyName;// hjh 2011-5-5 updated

  private AsValFieldEditor isDesSup;

  private AsValFieldEditor isPub;

  private AsValFieldEditor isCar;
  
  

  public String getTitle() {

    return LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_TITEL);

  }

  public ZcPProMakeXMEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcPProMakeListPanel listPanel) {

    super(parent, listCursor, tabStatus, listPanel);

    ZcPProMake make = (ZcPProMake) listCursor.getCurrentObject();

    if (make.getZcMakeStatus().equals("0") && make.getProcessInstId() != null && make.getProcessInstId().longValue() > 0) {

      sendButton.setEnabled(true);

      deleteButton.setEnabled(true);

    }
    if (make.getZcMakeStatus().equals("0") && make.getProcessInstId() == null) {

      deleteButton.setEnabled(false);
      sendButton.setEnabled(true);

    }
    //    if (make.getZcMakeStatus().equals("0") || (saveButton.isVisible() && deleteButton.isVisible())) {
    //
    //      updateWFSubTableEditable(true);
    //
    //    } else {
    //
    //      updateWFSubTableEditable(false);
    //
    //    }
    //    if ("40".equals(make.getZcMakeStatus()) && this.suggestPassButton.isVisible()) {
    //            setWFSubTableEditable(this.itemTablePanel, true);
    //    }

    if ("N".equals(make.getZcIsImp())) {

      zcImpFile.setEnabled(false);

    }
  }

  public ZcPProMakeXMEditPanel(Class<ZcPProMake> class1, BillElementMeta billElementMetaWithoutNd) {

    super(ZcPProMake.class, billElementMetaWithoutNd);

  }

  /**

   * hjh inserted 2011-5-5 14:27 

   */

  public static List<ColumnBeanPropertyPair> itemXMInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    itemXMInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_PITEM_NAME, "zcPitemName", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_PITEM_NAME)));

    itemXMInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_CODE, "zcCatalogueCode", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_CODE)));

    itemXMInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_NAME, "zcCatalogueName", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_NAME)));

    // itemXyInfo.add(new ColumnBeanPropertyPair("ZC_BRA_NAME", "zcBraName", "品牌"));

    //  itemXyInfo.add(new ColumnBeanPropertyPair("ZC_MER_NAME", "zcMerName", "商品"));

    // itemXyInfo.add(new ColumnBeanPropertyPair("ZC_SU_NAME", "zcSuName", "供应商"));

    itemXMInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_PITEM_ARR_DATE, "zcPitemArrDate", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_ZC_PITEM_ARR_DATE)));

    itemXMInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_BASE_GGYQ, "zcBaseGgyq", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_BASE_GGYQ)));

    itemXMInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_PITEM_ATTACH, "zcPitemAttach", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_PITEM_ATTACH)));

    itemXMInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_CAIG_NUM, "zcCaigNum", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_CAIG_NUM)));

    itemXMInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_CAIG_UNIT, "zcCaigUnit", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_MER_UNIT)));

    itemXMInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_MER_PRICE, "zcMerPrice", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_MER_PRICE)));

    itemXMInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_ITEM_SUM, "zcItemSum", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_ITEM_SUM)));

  }

  public List getItemInfo() {

    return ZcPProMakeXMEditPanel.itemXMInfo;

  }

  public BaseRule getZcMakeCheckRule() {

    return ZcMakeCheckRuleBySX.getInstance();

  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    if (fieldEditors != null && fieldEditors.size() > 0) {
      return fieldEditors;
    }

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    AutoNumFieldEditor zcMakeCode = new AutoNumFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MAKE_CODE), "zcMakeCode");
    TextFieldEditor zcMakeName = new TextFilePathFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MAKE_NAME), "zcMakeName");
    AsValFieldEditor zcMakeStatus = new AsValFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MAKE_STATUS), "zcMakeStatus", "ZC_VS_MAKE_STATUS");
    IForeignEntityTreeHandler companyHandler = new IForeignEntityTreeHandler() {

      @Override
      public void excute(List selectedDatas) {

        ZcPProMake zcPProMake = (ZcPProMake) listCursor.getCurrentObject();

        if (selectedDatas != null && selectedDatas.size() > 0) {

          Company company = (Company) selectedDatas.get(0);

          zcPProMake.setOrgCode(company.getForgCode());

          setEditingObject(zcPProMake);

        }

      }

      @Override
      public boolean isMultipleSelect() {

        return false;

      }

      @Override
      public boolean isSelectLeaf() {

        return false;

      }

    };

    CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_CO_NAME), "coCode", companyHandler, getCompoId(),

    "CO_CODE");

    zcCoCode.setEnabled(true);

    TextFieldEditor zcCoCodeNd = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_CO_CODE_ND), "nd");

    zcCoCodeNd.setEnabled(false);

    OrgFieldEditor zcZgCsCode = new OrgFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_ZG_CS_CODE), "orgCode", false);

    TextFieldEditor zcMakeLinkman = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MAKE_LINKMAN), "zcMakeLinkman");

    TextFieldEditor zcMakeTel = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MAKE_TEL), "zcMakeTel");

    TextFieldEditor zcAttr2 = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_ATTR2), "zcAttr2");

    zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MONEY_BI_SUM), "zcMoneyBiSum");

    zcMoneyBiSum.setEnabled(false);

    //委托机构名称，从后面提到前面进行声明

    String columNames[] = { "代理机构代码", "代理机构名称" };

    ZcAgeyFnHandler handler = new ZcAgeyFnHandler(columNames);

    ElementConditionDto elementCondtiontDto = new ElementConditionDto();

    elementCondtiontDto.setBillStatus("1");

    zcAgeyCode = new ForeignEntityFieldEditor("ZC_B_AGENCY.getZcBAgencyWithFN", elementCondtiontDto, 20, handler, columNames, LangTransMeta

    .translate(FIELD_TRANS_ZC_TRUST_AGEY_CODE), "zcAgeyCode") {

      public static final long serialVersionUID = -7737549222488261602L;

      @Override
      public void setValue(Object value) {

        //        ZcPProMake make = ((ZcPProMake) value);

        //        Updated By FengYan 2011-08-04

        ZcPProMake make = (ZcPProMake) listCursor.getCurrentObject();

        //        if (make.getZcMakeCode() != null) {

        //          make = getZcPProMakeServiceDelegate().selectByPrimaryKey(make.getZcMakeCode(), requestMeta);

        //        }

        if (make.getAgency() == null) {

          field.setText(null);

          field.setToolTipText(null);

        } else {

          field.setText(make.getAgencyName());

          field.setToolTipText("[" + make.getAgency() + "]" + make.getAgencyName());

        }

      }

      @Override
      public Object getValue() {

        return this.field.getText();

      }

    };

    // 申请采购组织形式 (1集中采购,2部门集中采购,3分散采购)

    zcMakeSequence = new AsValFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MAKE_SEQUENCE), "zcMakePfSequence", "VS_ZC_ORGTYPE") {

      public void setValue(Object value) {
        super.setValue(value);

      }

      int i = 0; //add by wangkewei,用于判断是不是第一次调用afterChange

      protected void afterChange(AsValComboBox field) {
        if (field.getSelectedAsVal() == null) {
          return;
        }

        ZcPProMake beanData = (ZcPProMake) listCursor.getCurrentObject();

        if (beanData.getProcessInstId() == null || beanData.getZcMakeStatus().equals("0")) {

          String valId = field.getSelectedAsVal().getValId();

          // 采购组织形式和采购方式的联动

          if (valId.equals("1")) { //采购组织形式和委托机构名称联动 :政府集中采购必须为采购中心，不可修改，不可改变

            haveInitFlag = false;

            beanData.setAgencyName(AsOptionMeta.getOptVal("OPT_ZC_CGZX_NAME"));

            beanData.setAgency(AsOptionMeta.getOptVal("OPT_ZC_CGZX_CODE"));

            //          zcAgeyCode.setEditable(false);

            zcAgeyCode.setEnabled(false); //wangkewei

            zcPitemOpiway.setEnabled(true);

            setEditingObject(beanData);

            haveInitFlag = true;

            valSetId = "1";

            fukuanValId = "1";

            i++;

          } else if (valId.equals("2")) {

            haveInitFlag = false;

            //判断是采购组织形式选项是否有变化，有变化则清空委托机构名称

            if (!valId.equals(valSetId) && i > 0) {//如果不是第一次调用，将委托机构名称设置为空，如果是第一次调用，界面上显示数据库的值

              if (!fukuanValId.equals(valId) && beanData.getZcMakeStatus().equals("0")) {

                beanData.setAgencyName("");

                beanData.setAgency("");

              }

            }

            valSetId = "2";

            fukuanValId = "2";

            //beanData.setZcPifuCgfs(null);

            //beanData.setZcPitemOpiway(null);

            //zcPifuCgfs.setEnabled(false);

            zcPitemOpiway.setEnabled(true);

            zcAgeyCode.setEnabled(true);

            zcAgeyCode.setEditable(false);//guoss

            setEditingObject(beanData);

            haveInitFlag = true;

            i++;

          } else if (valId.equals("3")) {//采购组织形式和申请采购方式的联动

            haveInitFlag = false;

            //临时取消选择分散采购时的控件内容及可用联动   Update By FengYan 2011-06-14

            //          beanData.setZcPitemOpiway("6");

            //              beanData.setZcPifuCgfs("6");

            //判断是采购组织形式选项是否有变化，有变化则清空委托机构名称

            if (!valId.equals(valSetId) && i > 0) {

              if (!fukuanValId.equals(valId) && beanData.getZcMakeStatus().equals("0")) {

                beanData.setAgencyName("");

                beanData.setAgency("");

              }

              valSetId = "3";

              fukuanValId = "3";

            }

            //临时取消选择分散采购时的控件内容及可用联动   Update By FengYan 2011-06-14

            //zcPifuCgfs.setEnabled(false);

            //zcPitemOpiway.setEnabled(false);

            valSetId = "3";

            fukuanValId = "3";

            zcAgeyCode.setEnabled(true);

            zcAgeyCode.setEditable(false);//guoss

            setEditingObject(beanData);

            haveInitFlag = true;

            i++;

          }

          if ("N".equals(beanData.getZcIsImp())) {

            zcImpFile.setEnabled(false);

          }

        }

      }

    };
    zcMakeSequence.getField().addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent event) {

        if (event.getStateChange() == ItemEvent.SELECTED && haveInitFlag) {

          haveInitFlag = false;

          String valId = ((AsValComboBox) zcMakeSequence.getField()).getSelectedAsVal().getValId();

          ZcPProMake data = (ZcPProMake) listCursor.getCurrentObject();

          data.setZcMakeSequence(valId);

          data.setZcMakePfSequence(valId);

          zcMakePfSequence.setEnabled(false);

          haveInitFlag = true;

          setEditingObject(data);

        }

      }

    });

    TextFieldEditor zcAgeyName = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_TRUST_AGEY_NAME), "agencyName");

    zcAgeyName.setVisible(false);

    zcAgeyName.setEnabled(false);

    //    AsValFieldEditor zcIsBudget = new AsValFieldEditor("是否公开预算", "zcIsBudget", "VS_Y/N");
    //
    //    zcIsBudget.setVisible(false); // wangkewei 编辑页面隐藏【是否公开预算】按钮
    //
    //    editorList.add(zcIsBudget);

    // 拟定采购方式  (1公开招标,2邀请招标,3竞争性谈判,4单一来源采购,5询价,6其他)

    zcPitemOpiway = new AsValFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_PITEM_OPIWAY), "zcPitemOpiway", "ZC_VS_PITEM_OPIWAY");

    zcPitemOpiway.getField().addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent event) {

        if (event.getStateChange() == ItemEvent.SELECTED && haveInitFlag) {

          /* 拟定采购方式和批复采购方式联动 */

          haveInitFlag = false;

          String valId = ((AsValComboBox) zcPitemOpiway.getField()).getSelectedAsVal().getValId();

          ZcPProMake data = (ZcPProMake) listCursor.getCurrentObject();

          data.setZcPitemOpiway(valId);

          data.setZcPifuCgfs(valId);

          data.setZcAttr3("");

          zcPifuCgfs.setEnabled(false);

          haveInitFlag = true;
          //
          //          if ("7".equals(valId)) { //如果是协议供货二次竞价，竞价品牌可编辑
          //
          //            data.setZcAttr3("");
          //
          //            zcAttr3.setEnabled(true);
          //
          //          } else {
          //
          //            zcAttr3.setEnabled(false);
          //
          //          }

          setEditingObject(data);

        }

      }

    });

    // 批复采购方式 ( 1公开招标,2邀请招标,3竞争性谈判,4单一来源采购,5询价,6其他)

    zcPifuCgfs = new AsValFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_PIFU_CGFS), "zcPifuCgfs", "ZC_VS_PITEM_OPIWAY") {

      private static final long serialVersionUID = 546680855108337426L;

      protected void afterChange(AsValComboBox field) {
        changeWay(field);
      }
    };

    zcIsImp = new AsValFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_IS_IMP), "zcIsImp", "VS_Y/N");

    //add by wangkw   start

    zcIsImp.getField().addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent event) {

        if (event.getStateChange() == ItemEvent.SELECTED) {

          ZcPProMake data = (ZcPProMake) listCursor.getCurrentObject();

          String valId = zcIsImp.getField().getSelectedAsVal().getValId();

          if ("N".equals(valId)) {//当是否涉及进出口选择否的时候,将附件上传设置为不可用,如果是从“是”跳转回来的话，要将上传的附件从数据库中删除

            if (data.getZcImpFileBlobid() != null) {

              baseDataServiceDelegate.deleteFile(data.getZcImpFileBlobid(), requestMeta);

            }

            data.setZcImpFile(null);

            data.setZcImpFileBlobid(null);

            zcImpFile.setEnabled(false);

          } else {

            zcImpFile.setEnabled(true);

          }

        }

      }

    });

    //end
    DateFieldEditor zcInputDate = new DateFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_DATE), "zcInputDate");
    zcInputDate.setEnabled(false);
    zcImpFile = new FileFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_IMP_FILE), "zcImpFile", "zcImpFileBlobid");

    TextFieldEditor zcIsRemark = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_IS_REMARK), "zcIsRemark");

    ZcPProHandler zcPProHandler = new ZcPProHandler(self, this.listCursor);

    ElementConditionDto proDto = new ElementConditionDto();

    proDto.setZcText0(requestMeta.getEmpCode());

    ForeignEntityFieldEditor proName = new ForeignEntityFieldEditor("ZC_P_PRO.getZcPPro", proDto, 20, zcPProHandler,

    zcPProHandler.getColumNames(), "预算项目", "proName");
    zcMakeStatus.setEnabled(false);

    zcMakePfSequence = new AsValFieldEditor("批复采购组织形式", "zcMakeSequence", "VS_ZC_ORGTYPE") {

      private static final long serialVersionUID = 454290003043403563L;

      int i = 0; //add by wangkewei,用于判断是不是第一次调用afterChange

      protected void afterChange(AsValComboBox field) { //hjh inserted 2011-5-5 17:53
        if (field.getSelectedAsVal() == null) {
          return;
        }

        ZcPProMake beanData = (ZcPProMake) listCursor.getCurrentObject();

        if (beanData.getProcessInstId() != null && beanData.getProcessInstId() > 0 && !beanData.getZcMakeStatus().equals("0")
          && saveButton.isVisible()) {

          String valId = field.getSelectedAsVal().getValId();

          // 采购组织形式和采购方式的联动

          if (valId.equals("1")) { //采购组织形式和委托机构名称联动 :政府集中采购必须为采购中心，不可修改，不可改变

            haveInitFlag = false;

            beanData.setAgencyName(AsOptionMeta.getOptVal("OPT_ZC_CGZX_NAME"));

            beanData.setAgency(AsOptionMeta.getOptVal("OPT_ZC_CGZX_CODE"));

            zcAgeyCode.setEnabled(false); //wangkewei

            setEditingObject(beanData);

            haveInitFlag = true;

            valSetId = "1";

            fukuanValId = "1";

            i++;

          } else if (valId.equals("2")) {

            haveInitFlag = false;

            //判断是采购组织形式选项是否有变化，有变化则清空委托机构名称

            if (!valId.equals(valSetId) && i > 0) {//如果不是第一次调用，将委托机构名称设置为空，如果是第一次调用，界面上显示数据库的值

              if (!fukuanValId.equals(valId) && !beanData.getZcMakeStatus().equals("0") && saveButton.isVisible()) {

                beanData.setAgencyName("");

                beanData.setAgency("");

              }

            }

            valSetId = "2";

            fukuanValId = "2";

            zcAgeyCode.setEnabled(true);

            zcAgeyCode.setEditable(false);

            setEditingObject(beanData);

            haveInitFlag = true;

            i++;

          } else if (valId.equals("3")) {//采购组织形式和申请采购方式的联动

            haveInitFlag = false;

            if (!valId.equals(valSetId) && i > 0) {

              if (!fukuanValId.equals(valId) && !beanData.getZcMakeStatus().equals("0") && saveButton.isVisible()) {

                beanData.setAgencyName("");

                beanData.setAgency("");

              }

              valSetId = "3";

              fukuanValId = "3";

            }

            valSetId = "3";

            fukuanValId = "3";

            zcAgeyCode.setEnabled(true);

            zcAgeyCode.setEditable(false);//guoss

            setEditingObject(beanData);

            haveInitFlag = true;

            i++;

          }

          if ("N".equals(beanData.getZcIsImp())) {

            zcImpFile.setEnabled(false);

          }

        }

      }

    };

    isDesSup = new AsValFieldEditor("是否指定供应商", "isDesSup", "ZC_VS_YN");

    isPub = new AsValFieldEditor("是否公示", "isPub", "ZC_VS_YN");

    isCar = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_P_PROMAKE_IS_CAR), "isCar", "ZC_VS_YN");

    CoCodeHandler coHandler = new CoCodeHandler(self, this.listCursor,this.getBiDto,requestMeta);

    ElementConditionDto coDto = new ElementConditionDto();

    coDto.setZcText0(requestMeta.getEmpCode());
    coDto.setNd(requestMeta.getSvNd());

    ForeignEntityFieldEditor coNameField = new ForeignEntityFieldEditor("ZcEbDaiBian.getCompanyByDaibian", coDto, 20, coHandler,coHandler.getColumNames(), "采购单位", "coName");
    //---

    editorList.add(zcMakeName);
    editorList.add(zcMoneyBiSum);
    editorList.add(zcMakeStatus);

    if(ZcUtil.isCgdb()){
      editorList.add(coNameField);      
    }else{
      editorList.add(zcCoCode);
    }
    editorList.add(zcMakeCode);
    editorList.add(zcZgCsCode);

    editorList.add(isCar);
    editorList.add(zcIsImp);
    editorList.add(zcImpFile);

    editorList.add(zcMakeLinkman);
    editorList.add(zcAttr2);
    editorList.add(zcMakeTel);

    editorList.add(zcPifuCgfs);
    editorList.add(zcInputDate);
    editorList.add(zcIsRemark);

    return editorList;

  }

  public void doSuggestPass() {
    ZcPProMake make = (ZcPProMake) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
    if (listPanel.checkBeforeSave(make, self)) {
      return;
    }
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    BaseRule rule = getZcMakeCheckRule();

    Map parameter = new HashMap();

    parameter.put("data", make);

    // 填报提示信息

    Map resMap = rule.check(parameter);

    String resInfo = (String) resMap.get("resInfo");

    int num = JOptionPane.YES_OPTION;

    if (resInfo != null && resInfo.length() > 0) {

      num = JOptionPane.showConfirmDialog(this, resInfo, "提示", 0);

    }

    if (num == JOptionPane.YES_OPTION) {

      //      nd = ((AsValComboBox) zcPitemOpiway.getField()).getSelectedAsVal().getVal();

      pf = ((AsValComboBox) zcPifuCgfs.getField()).getSelectedAsVal().getVal();

      //      repaint();

      ZcPProMake afterSaveBill = null;

      GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

      ModalityType.APPLICATION_MODAL);

      if (commentDialog.cancel) {

        return;

      }

      boolean success = true;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(this.suggestPassButton.getFuncId());

        make.setComment(commentDialog.getComment());

        make.setZcAttr1("N");

        make.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        afterSaveBill = listPanel.getZcPProMakeServiceDelegate().auditFN(make, requestMeta);

      } catch (Exception e) {

        success = false;

        logger.error(e.getMessage(), e);

        errorInfo += e.getMessage();

      }

      if (success) {

        this.refreshAll(afterSaveBill, true);

        JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.listPanel.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

      /*  if (!(pf.equals(nd))) {

          GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

          ModalityType.APPLICATION_MODAL, "同意" + "\n" + "采购单位申请的采购方式为" + nd + "\n" + "经审核，批复为" + pf + "方式");

          if (commentDialog.cancel) {

            return;

          }

          boolean success = true;

          ZcPProMake afterSaveBill = null;

          String errorInfo = "";

          try {

            requestMeta.setFuncId(this.suggestPassButton.getFuncId());

            make.setComment(commentDialog.getComment());

            make.setZcAttr1("N");

            make.setAuditorId(WorkEnv.getInstance().getCurrUserId());

            afterSaveBill = listPanel.getZcPProMakeServiceDelegate().auditFN(make, requestMeta);

          } catch (Exception e) {

            success = false;

            logger.error(e.getMessage(), e);

            errorInfo += e.getMessage();

          }

          if (success) {

            this.refreshAll(afterSaveBill, true);

            JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

            this.listPanel.refreshCurrentTabData();

          } else {

            JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

          }

        }*/

    }

  }

 

  public void updateWFEditorEditable(WfAware baseBill, RequestMeta requestMeta) {

    Long processInstId = baseBill.getProcessInstId();

    boolean isEdit = false;
    
    if(pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)){
      isEdit=true;
    }

    if (processInstId != null && processInstId.longValue() > 0) {
      // 工作流的单据
      wfCanEditFieldMap = BillElementMeta.getWfCanEditField(baseBill, requestMeta);
      if ("cancel".equals(this.oldZcPProMake.getZcMakeStatus())) {//撤销单据设置字段为不可编辑
        wfCanEditFieldMap = null;
      }

      //      if ("0".equals(this.oldZcPProMake.getZcMakeStatus())) {//撤销单据设置字段为不可编辑
      //        isEdit = true;
      //      }

      for (AbstractFieldEditor editor : fieldEditors) {
        //工作流中定义可编辑的字段
        if (wfCanEditFieldMap != null && wfCanEditFieldMap.containsKey(Utils.getDBColNameByFieldName(editor.getEditObject(), editor.getFieldName()))) {
          if (!"zcMakeSequence".equals(editor.getFieldName()) && !"zcPifuCgfs".equals(editor.getFieldName())) {
            isEdit = true;
          }
          editor.setEnabled(true);
          //退回状态时，明细都可以编辑 add shijia 2011-10-14
          if (ZcUtil.isYsdw()) {
            BeanTableModel itmodel = (BeanTableModel) itemTablePanel.getTable().getModel();
            itmodel.setEditable(true);
          }
        } else {
          editor.setEnabled(false);
        }
      }


    } else {
      for (AbstractFieldEditor editor : fieldEditors) {
        if("zcMakeCode".equals(editor.getFieldName()) 
          || "zcMakeStatus".equals(editor.getFieldName())
          ||"orgCode".equals(editor.getFieldName())
          ||"nd".equals(editor.getFieldName()) 
          ||"zcMoneyBiSum".equals(editor.getFieldName())
          ||"zcInputDate".equals(editor.getFieldName())
          ||"coCode".equals(editor.getFieldName())
          ){
          editor.setEnabled(false);
          if("000".equals(WorkEnv.getInstance().getCurrCoCode()) && "coCode".equals(editor.getFieldName())){
            editor.setEnabled(true);
          }
        }else{
          editor.setEnabled(true);
        }
      }
      isEdit = true;
    }
    // 子表的设置
    updateWFSubTableEditable(isEdit);
    // 根据采购方式，决定相关属性字段的显示，注释了，暂不应用
    //    AsValFieldEditor pf = (AsValFieldEditor) fieldEditors.get(fieldEditors.size() - 5);
    //    this.changeWay(pf.getField());
    this.changeWay(null);

  }

  protected void updateWFSubTableEditable(boolean isEdit) {

    // 默认的方法是判断isEdit属性（主表中是否存在可编辑字段）,如果主表中存在可编辑字段，就设置从表字段都可编辑.

    // 如果这个逻辑满足不了业务，可以在实现类里覆盖updateWFSubTableEditable方法，自己做判断.

    ZcBaseBill bean = (ZcBaseBill) this.listCursor.getCurrentObject();

    if (getSubTables() != null) {

      if (isEdit) {

        for (JTablePanel tablePanel : getSubTables()) {

          if (bean.getProcessInstId() != null && bean.getProcessInstId() > 0 && !deleteButton.isVisible()) { //判断处工作流启用或非采购人填报，其它岗位不可修改明细 edt shijia 20110920

            setWFSubTableEditable(tablePanel, false);

          } else {

            setWFSubTableEditable(tablePanel, true);

          }

        }

        setWFSubTableEditable(itemTablePanel, true);

      } else {

        for (JTablePanel tablePanel : getSubTables()) {

          setWFSubTableEditable(tablePanel, false);

        }

      }

    }

  }

  protected void refreshData() { //add shijia 20111105

    ZcPProMake zcPProMake = (ZcPProMake) listCursor.getCurrentObject();

    //boolean isNew;

    if (zcPProMake != null) {     

      zcPProMake = getZcPProMakeServiceDelegate().selectByPrimaryKey(zcPProMake.getZcMakeCode(), this.requestMeta);

      zcPProMake.setCoName(CompanyDataCache.getName(zcPProMake.getCoCode()));
      fukuanValId = zcPProMake.getZcMakeSequence();
      List biList = getZcPProMakeServiceDelegate().getZcPProMitemBi(zcPProMake.getZcMakeCode(), ZcUtil.useBudget(), requestMeta);
      zcPProMake.setBiList(biList);

      listCursor.setCurrentObject(zcPProMake);

      if (biList != null && biList.size() > 0) {
        if (ZcUtil.useBudget()) {
          String sumId = "";
          for (int i = 0; i < biList.size(); i++) {
            ZcPProMitemBi bi = (ZcPProMitemBi) biList.get(i);
            if ("A".equals(bi.getPaytypeCode())) {
              bi.setZcBiPayType("2");
              bi.setZcBiDoSum(null);
              continue;
            }
            bi.setZcBiPayType("1");
            if (sumId.length() > 0) {
              sumId = sumId + ",'" + bi.getZcBiNo() + "'";
            } else {
              sumId = "'" + bi.getZcBiNo() + "'";
            }
          }
          getBiDto.setZcText3(sumId);
        }
      }
      if (ZcUtil.useBudget()) {
        getBiDto.setZcMakeCode(zcPProMake.getZcMakeCode());
      }

    } else {

      pageStatus=ZcSettingConstants.PAGE_STATUS_NEW;

      zcPProMake = new ZcPProMake();

      setDeftValue(zcPProMake);

      zcPProMake.setCoCode(this.requestMeta.getSvCoCode());

      initOrg(zcPProMake);

      zcPProMake.setNd(this.requestMeta.getSvNd());

      zcPProMake.setBiList(new ArrayList());

      zcPProMake.setItemList(new ArrayList());
      zcPProMake.setZcInputDate(this.requestMeta.getSysDate());

      // 新增数据默认插入一行

      ZcPProMitem item = new ZcPProMitem();

      setItemDefaultValue(item);

      zcPProMake.getItemList().add(item);

      ZcPProMitemBi bi = new ZcPProMitemBi();

      setItemBiDefaultValue(bi);

      zcPProMake.getBiList().add(bi);

      listCursor.getDataList().add(zcPProMake);

      listCursor.setCurrentObject(zcPProMake);

    }
    if (ZcUtil.useBudget()) {
      getBiDto.setNd(requestMeta.getSvNd());
      getBiDto.setZcText2("1");
      getBiDto.setCoCode(zcPProMake.getCoCode() != null ? zcPProMake.getCoCode() : requestMeta.getSvCoCode());
    }

    this.setEditingObject(zcPProMake);

    if (zcPProMake.getProcessInstId() != null && zcPProMake.getProcessInstId().longValue() > 0) {
      // 工作流的单据
      wfCanEditFieldMap = BillElementMeta.getWfCanEditField(zcPProMake, requestMeta);
    }

    itemTablePanel.setTableModel(new ZcPProMakeToTableModelConverter().convertSubItemTableData(zcPProMake.getItemList(), getItemInfo(),
      wfCanEditFieldMap));

    if (ZcUtil.useBudget()) {
      biTablePanel.setTableModel(ZcPProMakeToTableModelConverter.convertSubBiTableData(zcPProMake.getBiList(), wfCanEditFieldMap));
    } else {
      biTablePanel.setTableModel(ZcPProMakeToTableModelConverter.convertSubBiTableDataWithOutBudget(zcPProMake.getBiList(), wfCanEditFieldMap));
    }
    //刷新竞价信息和成交信息

    refreshJinJiaChengJiao();

    // 翻译从表表头列

    ZcUtil.translateColName(itemTablePanel.getTable(), getItemInfo());

    if (ZcUtil.useBudget()) {
      ZcUtil.translateColName(biTablePanel.getTable(), ZcPProMakeToTableModelConverter.biInfoWihtBudget);
    } else {
      ZcUtil.translateColName(biTablePanel.getTable(), ZcPProMakeToTableModelConverter.biInfoWihtoutBugdet);
    }

    // 设置从表列类型

    setTableBiEditor(biTablePanel.getTable());

    setTableItemEditor(itemTablePanel.getTable());

    // 设置从表监听 

    addBiTableLisenter(biTablePanel.getTable());

    addItemTableLisenter(itemTablePanel.getTable());

    setOldObject();

    // 根据工作流模版设置功能按钮是否可用

    setButtonStatus(zcPProMake, requestMeta, this.listCursor);

    // 根据工作流模版设置字段是否可编辑

    updateWFEditorEditable(zcPProMake, requestMeta);

    this.fitTable();

    // 草稿状态设置【批复采购方式】只读

    if ("0".equals(zcPProMake.getZcMakeStatus()) || (saveButton.isVisible() && deleteButton.isVisible())) {

      if (zcPifuCgfs != null) {

        zcPifuCgfs.setEnabled(true);

      }
      if (zcMakePfSequence != null) {
        zcMakePfSequence.setEnabled(false);
      }

    }

    if (pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {

      biTablePanel.getTable().setRowSelectionInterval(0, 0);

      itemTablePanel.getTable().setRowSelectionInterval(0, 0);

    }

    if (zcPProMake.getZcMakeStatus().equals("0") && zcPProMake.getProcessInstId() != null && zcPProMake.getProcessInstId().longValue() > 0) {

      sendButton.setEnabled(false);

    } else if (zcPProMake.getZcMakeStatus().equals("0") && zcPProMake.getProcessInstId() != null && zcPProMake.getProcessInstId().longValue() < 0) {

      sendButton.setEnabled(true);

    } else if (zcPProMake.getZcMakeStatus().equals("0") && zcPProMake.getProcessInstId() != null) {

      sendButton.setEnabled(true);

    }

    //    if (zcPProMake.getZcMakeStatus().equals("0") || (saveButton.isVisible() && deleteButton.isVisible())) {
    //
    //      updateWFSubTableEditable(true);
    //
    //    } else {
    //
    //      updateWFSubTableEditable(false);
    //
    //    }

    //退回状态时，明细都可以编辑 add shijia 20111105 

    if (zcPProMake.getZcMakeStatus().equals(ZcSettingConstants.WF_STATUS_DRAFT) && WorkEnv.getInstance().containRole(ZcSettingConstants.ROLE_YSDWJB)) {

      String valId = ((AsValComboBox) zcPitemOpiway.getField()).getSelectedAsVal().getValId();

      valId = ((AsValComboBox) zcIsImp.getField()).getSelectedAsVal().getValId();

      if ("N".equals(valId)) {//当是否涉及进出口选择否的时候,将附件上传设置为不可用,如果是从“是”跳转回来的话，要将上传的附件从数据库中删除

        zcImpFile.setEnabled(false);
      } else {
        zcImpFile.setEnabled(true);

      }
    }

    //end add

    biTabPane.repaint();

    itemTabPane.repaint();
    

  }

  private void changeWay(AsValComboBox field) {
    /**
      if (field.getSelectedAsVal() == null || !ZcSettingConstants.PITEM_OPIWAY_YQZB.equals(field.getSelectedAsVal().getValId())) {
        isDesSup.setVisible(false);

      } else {
        isDesSup.setVisible(true);
      }

      if (field.getSelectedAsVal() == null || !ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(field.getSelectedAsVal().getValId())) {
        isPub.setVisible(false);

      } else {
        isPub.setVisible(true);
      }

      if (field.getSelectedAsVal() == null || !ZcSettingConstants.PITEM_OPIWAY_XJ_JJ.equals(field.getSelectedAsVal().getValId())) {
        isCar.setVisible(false);

      } else {
        isCar.setVisible(true);
      }
      */
    fieldEditorPanel.removeAll();
    initFieldEditorPanel();
    fieldEditorPanel.updateUI();
    this.repaint();
  }

}