package com.ufgov.zc.client.zc.zcxmcghtchg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTableHeader;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcXmcgHtToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.GkCommentUntreadDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.IntFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.zcxmcght.ZcXmcgHtEditPanel;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcEbBulletinWordMold;
import com.ufgov.zc.common.zc.model.ZcHtPrePayBillItem;
import com.ufgov.zc.common.zc.model.ZcHtPrePayBillItemChg;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcTBchtItem;
import com.ufgov.zc.common.zc.model.ZcTBchtItemChg;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBiChg;
import com.ufgov.zc.common.zc.model.ZcXmcgHtChg;

public class ZcXmcgHtChgEditPanel extends ZcXmcgHtEditPanel {

  private static final long serialVersionUID = -497156913062816741L;

  private ZcXmcgHtOldEditPanel editPanel;

  private ZcXmcgHtChgListPanel clistPanel;

  ElementConditionDto htDto = new ElementConditionDto();

  private static final Logger logger = Logger.getLogger(ZcXmcgHtChgEditPanel.class);

  public ZcXmcgHtChgEditPanel(GkBaseDialog parent, ListCursor<ZcXmcgHt> listCursor, String tabStatus, ZcXmcgHtChgListPanel listPanel,
    ZcXmcgHtOldEditPanel editPanel) {
    super(parent, listCursor, tabStatus, null,listPanel.getCompoId());
    this.editPanel = editPanel;
    this.clistPanel = listPanel;
    // TODO Auto-generated constructor stub
  }

  protected void refreshData() {

    ZcXmcgHtChg zcXmcgHt = (ZcXmcgHtChg) listCursor.getCurrentObject();

    if (zcXmcgHt != null && !"".equals(ZcUtil.safeString(zcXmcgHt.getZcHtChgId()))) {// 列表页面双击进入

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      zcXmcgHt = zcXmcgHtChgServiceDelegate.selectByPrimaryKey(zcXmcgHt.getZcHtChgId(), this.requestMeta);

      if (zcXmcgHt.getZcPProMake().getZcMakeCode() != null) {

        zcXmcgHt.setZcMakeCode(zcXmcgHt.getZcPProMake().getZcMakeCode());

      }

      //			BigDecimal sum = new BigDecimal(0.00);

      List<ZcXmcgHtBi> tempList = new ArrayList<ZcXmcgHtBi>();

      for (int i = 0; i < zcXmcgHt.getBiList().size(); i++) {

        ZcXmcgHtBi bi = (ZcXmcgHtBi) zcXmcgHt.getBiList().get(i);

        ZcPProMitemBi zcPProMitemBi = bi.getZcPProMitemBi();

        //				if (zcPProMitemBi.getZcBiUsedSum() != null) {
        //
        //					sum = sum.add(zcPProMitemBi.getZcBiUsedSum());
        //
        //				}

        bi.setZcProBiSeq(zcPProMitemBi.getZcProBiSeq());

        if (zcPProMitemBi.getZcBiUsedSum() == null) {

          zcPProMitemBi.setZcBiUsedSum(BigDecimal.ZERO);

        }

        if (bi.getZcBiBcsySum() == null) {

          bi.setZcBiBcsySum(BigDecimal.ZERO);

        }

        // bi.setZcBiSySum(zcPProMitemBi.getZcBiUsedSum().subtract(bi.getZcBiBcsySum()));

        tempList.add(bi);

      }
      if (isSubHt()) {
        tempList = this.zcEbBaseServiceDelegate.queryDataForList("ZC_XMCG_HT_BI.ibatorgenerated_selectBiByHtCode", zcXmcgHt.getZcHtCode(),
          this.requestMeta);

        String sumId = "";
        for (int i = 0; i < tempList.size(); i++) {
          ZcXmcgHtBi bi = (ZcXmcgHtBi) tempList.get(i);
          if (bi.getZcBiNo() == null || "".equals(bi.getZcBiNo())) {
            bi.setZcBiDoSum(null);
            continue;
          }

          if (sumId.length() > 0) {
            sumId = sumId + ",'" + bi.getZcBiNo() + "'";
          } else {
            sumId = "'" + bi.getZcBiNo() + "'";
          }
        }
        getBiDto.setZcText3(sumId);
      }

      //			zcXmcgHt.getZcPProMake().setZcMoneyBiSum(sum);

      zcXmcgHt.setBiList(tempList);

      findMainHt(zcXmcgHt);

      listCursor.setCurrentObject(zcXmcgHt);

      this.setEditingObject(zcXmcgHt);
    } else {// 新增按钮进入

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

      zcXmcgHt = new ZcXmcgHtChg();
      zcXmcgHt.setZcHtChgId("");

      zcXmcgHt.setZcHtStatus("0");

      zcXmcgHt.setNd(this.requestMeta.getSvNd());
      zcXmcgHt.setZcInputDate(requestMeta.getSysDate());

      zcXmcgHt.setZcCgLeixing(ZcSettingConstants.PROJECT_BUY_CODE);

      zcXmcgHt.setBiList(new ArrayList<ZcXmcgHtBi>());

      zcXmcgHt.setItemList(new ArrayList<ZcTBchtItem>());

      zcXmcgHt.setPayBiList(new ArrayList<ZcHtPrePayBillItem>());
      zcXmcgHt.setZcXmht(new ZcXmcgHt());

      // 新增数据默认插入一行

      ZcXmcgHtBi bi = new ZcXmcgHtBi();

      setItemBiDefaultValue(bi);

      zcXmcgHt.getBiList().add(bi);

      listCursor.getDataList().add(zcXmcgHt);

      listCursor.setCurrentObject(zcXmcgHt);

      this.setEditingObject(zcXmcgHt);

    }

    if (!isSubHt()) {
      biTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData(zcXmcgHt.getBiList(), false));
    } else {
      biTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableDataForSubHt(zcXmcgHt.getBiList(), wfCanEditFieldMap));
    }

    itemTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubItemTableData(zcXmcgHt.getItemList(),isCar()));

    // 翻译从表表头列
    if (!isSubHt()) {
      ZcUtil.translateColName(biTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getBiInfo());
    } else {
      ZcUtil.translateColName(biTablePanel.getTable(), ZcXmcgHtToTableModelConverter.biInfoForSubHtWihtBugdet);

      getBiDto.setNd(requestMeta.getSvNd());
      getBiDto.setZcText2("1");
      getBiDto.setCoCode(requestMeta.getSvCoCode());

    }

    ZcUtil.translateColName(itemTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getItemInfo());

    // 设置从表监听

    addItemTableLisenter(itemTablePanel.getTable());

    addBiTableLisenter(biTablePanel.getTable());

    setBiTableEditor(biTablePanel.getTable());

    setItemTableEditor(itemTablePanel.getTable());

    this.refreshSubData(zcXmcgHt.getPayBiList());

    // 根据工作流模版设置字段是否可编辑

    updateWFEditorEditable(zcXmcgHt, requestMeta);

    // 根据工作流模版设置功能按钮是否可用
    setButtonStatus(zcXmcgHt, requestMeta, this.listCursor);

    setWordButtonStataus(zcXmcgHt);

    if (isGys()) {

      JPageableFixedTable ta = itemTablePanel.getTable();

      hideCol(ta, "ZC_ITEM_SUM");

      hideCol(ta, "BUDGET_BI_MONEY");

      hideCol(ta, "BUDGET_OTHER_MONEY");

      hideCol(ta, "ZC_ITEM_VAL");

      hideCol(ta, "ZC_HT_BI_MONEY");

      hideCol(ta, "ZC_HT_OTHER_MONEY");

      hideCol(ta, "ZC_HT_GK_MONEY");

    }

    updateFieldEditorsEditable();

    setSumLabelText();

    setOldObject();

  }

  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    TextFieldEditor zcMakeName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME), "zcPProMake.zcMakeName");

    MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM),

    "zcPProMake.zcMoneyBiSum");

    TextFieldEditor zcMakeCode = new TextFieldEditor("采购计划编号", "zcMakeCode");

    zcMakeCode.setEnabled(false);

    zcMakeName.setEnabled(false);

    zcMoneyBiSum.setEnabled(false);

    AsValFieldEditor zcProjType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FUKUAN_TYPE), "zcCgLeixing",

    "ZC_VS_FUKUAN_TYPE");

    zcProjType.setEnabled(false);

    String columNames[] = { "合同代码", "合同名称", "合同金额" };

    ZcPHtHandler htHandler = new ZcPHtHandler(columNames);
    if (htDto == null) {
      htDto = new ElementConditionDto();
    }

    htDto.setCoCode(requestMeta.getSvCoCode());
    htDto.setNd(WorkEnv.getInstance().getSysNd());

    ForeignEntityFieldEditor zcHtCode = new ForeignEntityFieldEditor("ZC_XMCG_HT.getOldHt", htDto, 20, htHandler,

    columNames, "选择原合同", "zcHtCode");

    TextFieldEditor zcHtName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NAME), "zcHtName");

    AsValFieldEditor zcHtStatus = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_STATUS), "zcHtStatus",

    getHtStatus());

    zcHtStatus.setEnabled(false);

    zcHtStatus.setVisible(false);

    CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE), "coCode");

    zcCoCode.setEnabled(false);

    IntFieldEditor zcCoCodeNd = new IntFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE_ND), "nd", 4);

    zcCoCodeNd.setEnabled(false);

    if (isGys()) {

      zcCoCodeNd.setVisible(false);

      zcMoneyBiSum.setVisible(false);

    }

    getBiddingWinner();

    ZcXmcgHt ht = (ZcXmcgHt) this.listCursor.getCurrentObject();

    TextFieldEditor zcSuBankName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SU_BANK_NAME), "zcSuBankName");

    TextFieldEditor zcSuAccCode = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SU_ACC_CODE), "zcSuAccCode");

    TextFieldEditor zcSuCode = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SU_BANK_CODE), "zcSuBankCode");

    TextFieldEditor zcSuLinkman = new TextFieldEditor("联系人", "zcSuLinkman");

    zcSuLinkman.setVisible(false);

    //    getZcSuNameAndOthers(zcSuBankName, zcSuAccCode, zcSuCode, zcSuLinkman);

    TextFieldEditor zcSuName = new TextFieldEditor("中标供应商", "zcSuName");

    //补充合同和采购合同去掉银行账号，名称
    //    editorList.add(zcSuBankName);
    //
    //    editorList.add(zcSuAccCode);

    MoneyFieldEditor zcHtNum = new MoneyFieldEditor(getHtNumLabel(), "zcHtNum");

    //添加工期和附件
    DateFieldEditor zcProLimitStartDate = new DateFieldEditor("工期开始时间", "zcProLimitStartDate");
    DateFieldEditor zcProLimitEndDate = new DateFieldEditor("工期结束时间", "zcProLimitEndDate");
    FileFieldEditor zcImpFile = new FileFieldEditor("电子附件", "zcImpFile", "zcImpFileBlobid");

    AsValFieldEditor zcFukuanType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.ZC_PAY_TYPE), "zcFukuanType", "ZC_VS_PAY_TYPE");

    DateFieldEditor zcSgnDate = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SGN_DATE), "zcSgnDate");

    TextFieldEditor zcDeliveryDate = new TextFieldEditor("交货日期", "zcDeliveryDate");

    TextFieldEditor zcDeliveryType = new TextFieldEditor("交货方式", "zcDeliveryType");

    TextFieldEditor zcDeliveryAddr = new TextFieldEditor("交货地点", "zcDeliveryAddr");

    //    FileFieldEditor fieldFileName = new FileFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CON_TEXT), "zcConText",

    //      "zcConTextBlobid");

    //    editorList.add(fieldFileName);

    TextFieldEditor zcMemo = new TextFieldEditor("备注信息", "zcMemo");
    DateFieldEditor zcInputDate = new DateFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_DATE), "zcInputDate");

    editorList.add(zcCoCode);
    editorList.add(zcHtName);
    editorList.add(zcProjType);
    editorList.add(zcProLimitStartDate);

    editorList.add(zcSuName);
    editorList.add(zcHtCode);
    editorList.add(zcDeliveryAddr);
    editorList.add(zcProLimitEndDate);

    editorList.add(zcMakeName);
    editorList.add(zcHtNum);
    editorList.add(zcDeliveryDate);
    editorList.add(zcImpFile);

    editorList.add(zcMakeCode);
    editorList.add(zcFukuanType);
    editorList.add(zcDeliveryType);
    editorList.add(zcMemo);

    editorList.add(zcMoneyBiSum);
    editorList.add(zcSgnDate);
    editorList.add(zcInputDate);
    //  editorList.add(zcCoCodeNd);

    //项目采购才显示中标
    if (ht != null && ZcSettingConstants.PROJECT_BUY_CODE.equals(ht.getZcFukuanType()))
      editorList.add(biddingWinner);

    editorList.add(zcHtStatus);

    return editorList;

  }

  protected void loadCommonEditors(List<AbstractFieldEditor> editorList) {

    TextFieldEditor zcMakeName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME), "zcPProMake.zcMakeName");

    MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM),

    "zcPProMake.zcMoneyBiSum");

    TextFieldEditor zcMakeCode = new TextFieldEditor("项目编码", "zcMakeCode");

    zcMakeCode.setEnabled(false);

    zcMakeName.setEnabled(false);

    zcMoneyBiSum.setEnabled(false);

    editorList.add(zcMakeCode);

    editorList.add(zcMakeName);

    editorList.add(zcMoneyBiSum);

    AsValFieldEditor zcProjType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FUKUAN_TYPE), "zcCgLeixing",

    "ZC_VS_FUKUAN_TYPE");

    zcProjType.setEnabled(false);

    editorList.add(zcProjType);

    String columNames[] = { "合同代码", "合同名称", "合同金额" };

    ZcPHtHandler htHandler = new ZcPHtHandler(columNames);
    if (htDto == null) {
      htDto = new ElementConditionDto();
    }

    htDto.setCoCode(requestMeta.getSvCoCode());
    htDto.setNd(WorkEnv.getInstance().getSysNd());

    ForeignEntityFieldEditor zcHtCode = new ForeignEntityFieldEditor("ZC_XMCG_HT.getOldHt", htDto, 20, htHandler,

    columNames, "选择合同", "zcHtCode");

    editorList.add(zcHtCode);

    TextFieldEditor zcHtName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NAME), "zcHtName");

    editorList.add(zcHtName);

    AsValFieldEditor zcHtStatus = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_STATUS), "zcHtStatus",

    getHtStatus());

    zcHtStatus.setEnabled(false);

    zcHtStatus.setVisible(false);

    editorList.add(zcHtStatus);

    CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE), "coCode");

    zcCoCode.setEnabled(false);

    editorList.add(zcCoCode);

    IntFieldEditor zcCoCodeNd = new IntFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE_ND), "nd", 4);

    zcCoCodeNd.setEnabled(false);

    editorList.add(zcCoCodeNd);

    if (isGys()) {

      zcCoCodeNd.setVisible(false);

      zcMoneyBiSum.setVisible(false);

    }

  }

  public static String  getCompoId() {
    return "ZC_XMCG_HT_CHG";
  }

  protected void updateFieldEditorsEditable() {
    ZcXmcgHtChg zcXmcgHt = (ZcXmcgHtChg) listCursor.getCurrentObject();

    super.updateFieldEditors();

    JTablePanel[] subs = this.getSubTables();

    boolean flag = false;

    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        if ("zcBidContent_nd_zcPProMake.zcMakeCode.zcPProMake.zcMakeName_zcHtStatus_zcPProMake.zcMoneyBiSum_pName_pBiSum_zcCgLeixing_coCode_zcInputDate"
          .indexOf(fd.getFieldName()) == -1) {

          fd.setEnabled(true);

        } else {

          fd.setEnabled(false);

        }

        if ("zcSuName".equals(fd.getFieldName()) && "Z01".equals(zcXmcgHt.getZcFukuanType())) {
          fd.setEnabled(true);

        }

      }

      flag = true;

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        fd.setEnabled(false);

      }

      flag = false;

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        if ("zcBidContent_nd_zcPProMake.zcMakeCode.zcPProMake.zcMakeName_zcHtStatus_zcPProMake.zcMoneyBiSum_zcBidContent_pName_pBiSum_zcCgLeixing_coCode_zcInputDate"
          .indexOf(fd

          .getFieldName()) == -1) {

          fd.setEnabled(true);

        } else {

          fd.setEnabled(false);

        }

      }

      flag = true;

    }

    for (JTablePanel tablePanel : subs) {

      setWFSubTableEditable(tablePanel, flag);

    }

  }

  protected void setOldObject() {

    this.setOldZcXmcgHt((ZcXmcgHt) ObjectUtil.deepCopy(listCursor.getCurrentObject()));

  }

  protected void ht2Chg(ZcXmcgHtChg zcXmcgHt, ZcXmcgHt old) {

    // 合同
    zcXmcgHt.setZcHtCode(old.getZcHtCode());
    zcXmcgHt.setZcDiyuDaima(old.getZcDiyuDaima());
    zcXmcgHt.setZcHtName(old.getZcHtName());
    zcXmcgHt.setZcHtType(old.getZcHtType());
    zcXmcgHt.setZcReqCode(old.getZcReqCode());
    if (zcXmcgHt.getZcPProMake() == null) {
      zcXmcgHt.setZcPProMake(new ZcPProMake());
    }
    zcXmcgHt.getZcPProMake().setZcMakeCode(old.getZcPProMake().getZcMakeCode());
    zcXmcgHt.getZcPProMake().setZcMakeName(old.getZcPProMake().getZcMakeName());

    zcXmcgHt.setZcBidCode(old.getZcBidCode());
    zcXmcgHt.setCoCode(old.getCoCode());
    //		zcXmcgHt.setNd(old.getNd());
    zcXmcgHt.setZcSgnDate(old.getZcSgnDate());
    zcXmcgHt.setZcSuName(old.getZcSuName());
    zcXmcgHt.setZcJckdlMc(old.getZcJckdlMc());
    zcXmcgHt.setZcHtNum(old.getZcHtNum());
    zcXmcgHt.setZcCzLevel(old.getZcCzLevel());
    zcXmcgHt.setZcFukuanType(old.getZcFukuanType());
    zcXmcgHt.setZcFukuanYued(old.getZcFukuanYued());
    zcXmcgHt.setZcCheckType(old.getZcCheckType());
    zcXmcgHt.setZcConText(old.getZcConText());

    if (zcXmcgHt.getZcHtStatus() == null || "".equals(zcXmcgHt.getZcHtStatus())) {
      zcXmcgHt.setZcHtStatus("0");
    }
    zcXmcgHt.setAgency(old.getAgency());
    zcXmcgHt.setOrgCode(old.getOrgCode());
    zcXmcgHt.setZcSmPj1(old.getZcSmPj1());
    zcXmcgHt.setZcSmPj2(old.getZcSmPj2());
    zcXmcgHt.setZcSmPj3(old.getZcSmPj3());
    zcXmcgHt.setZcSmPj4(old.getZcSmPj4());
    zcXmcgHt.setZcInputCode(old.getZcInputCode());
    zcXmcgHt.setZcInputDate(old.getZcInputDate());
    // zcXmcgHt.setProcessInstId(old.get());
    zcXmcgHt.setZcParhtCode(old.getZcParhtCode());
    zcXmcgHt.setZcSuCode(old.getZcSuCode());
    zcXmcgHt.setZcSuAccCode(old.getZcSuAccCode());
    zcXmcgHt.setZcSuBankCode(old.getZcSuBankCode());
    zcXmcgHt.setZcSuBankName(old.getZcSuBankName());
    zcXmcgHt.setZcSuTel(old.getZcSuTel());
    zcXmcgHt.setZcSuLinkman(old.getZcSuLinkman());
    zcXmcgHt.setZcIsZxqyZb(old.getZcIsZxqyZb());
    zcXmcgHt.setZcGnw(old.getZcGnw());
    zcXmcgHt.setZcSnw(old.getZcSnw());
    zcXmcgHt.setZcArriveAddr(old.getZcArriveAddr());
    zcXmcgHt.setZcArriveDate(old.getZcArriveDate());
    zcXmcgHt.setZcHangyeCtg(old.getZcHangyeCtg());
    zcXmcgHt.setZcSkRen(old.getZcSkRen());
    zcXmcgHt.setZcZbName(old.getZcZbName());
    zcXmcgHt.setZcZbCode(old.getZcZbCode());
    zcXmcgHt.setZcBiNzjzSum(old.getZcBiNzjzSum());
    zcXmcgHt.setZcIsrewrite(old.getZcIsrewrite());
    zcXmcgHt.setZcCgLeixing(old.getZcCgLeixing());
    zcXmcgHt.setZcZgCsName(old.getZcZgCsName());
    zcXmcgHt.setZcBgspBh(old.getZcBgspBh());
    zcXmcgHt.setZcDeliveryEvl(old.getZcDeliveryEvl());
    zcXmcgHt.setZcPriceEvl(old.getZcPriceEvl());
    zcXmcgHt.setZcQuantityEvl(old.getZcQuantityEvl());
    zcXmcgHt.setZcServiceEvl(old.getZcServiceEvl());
    zcXmcgHt.setZcMemo(old.getZcMemo());
    zcXmcgHt.setZcBidContent(old.getZcBidContent());

    zcXmcgHt.setZcDeliveryAddr(old.getZcDeliveryAddr());
    zcXmcgHt.setZcDeliveryType(old.getZcDeliveryType());
    zcXmcgHt.setZcDeliveryDate(old.getZcDeliveryDate());

    zcXmcgHt.setZcProLimitStartDate(old.getZcProLimitStartDate());
    zcXmcgHt.setZcProLimitEndDate(old.getZcProLimitEndDate());
    zcXmcgHt.setZcImpFile(old.getZcImpFile());
    zcXmcgHt.setZcImpFileBlobid(old.getZcImpFileBlobid());

    // 资金
    List<ZcXmcgHtBiChg> bis = new ArrayList<ZcXmcgHtBiChg>();
    if (old.getBiList() != null) {
      for (int i = 0; i < old.getBiList().size(); i++) {
        ZcXmcgHtBi bi = (ZcXmcgHtBi) old.getBiList().get(i);
        ZcXmcgHtBiChg bic = new ZcXmcgHtBiChg();
        bic.setZcHtCode(bi.getZcHtCode());
        bic.setZcBiNo(bi.getZcBiNo());
        bic.setZcBiBcsySum(bi.getZcBiBcsySum());
        bic.setZcMakeCode(bi.getZcMakeCode());
        bic.setZcBiYjjsSum(bi.getZcBiYjjsSum());
        bic.setZcBiHtbaSum(bi.getZcBiHtbaSum());
        bic.setZcBiYjchSum(bi.getZcBiYjchSum());
        bic.setZcBiNzjzSum(bi.getZcBiNzjzSum());
        bic.setZcProBiSeq(bi.getZcProBiSeq());
        bic.setZcCanUseSum(bi.getZcCanUseSum());
        bic.setZcBiSum(bi.getZcBiSum());

        ZcPProMitemBi zcPProMitemBi = new ZcPProMitemBi();
        zcPProMitemBi.setZcBiNo(bi.getZcPProMitemBi().getZcBiNo());
        zcPProMitemBi.setZcMakeCode(bi.getZcPProMitemBi().getZcMakeCode());
        zcPProMitemBi.setZcBiSum(bi.getZcPProMitemBi().getZcBiSum());
        zcPProMitemBi.setZcBiJhuaSum(bi.getZcPProMitemBi().getZcBiJhuaSum());
        zcPProMitemBi.setZcBiDoSum(bi.getZcPProMitemBi().getZcBiDoSum());
        zcPProMitemBi.setZcBiYjbaSum(bi.getZcPProMitemBi().getZcBiYjbaSum());
        zcPProMitemBi.setBiTargetCode(bi.getZcPProMitemBi().getBiTargetCode());
        zcPProMitemBi.setbAccCode(bi.getZcPProMitemBi().getbAccCode());
        zcPProMitemBi.setbAccName(bi.getZcPProMitemBi().getbAccName());
        zcPProMitemBi.setCoCode(bi.getZcPProMitemBi().getCoCode());
        zcPProMitemBi.setCoName(bi.getZcPProMitemBi().getCoName());
        zcPProMitemBi.setDecMoney(bi.getZcPProMitemBi().getDecMoney());
        zcPProMitemBi.setFundCode(bi.getZcPProMitemBi().getFundCode());
        zcPProMitemBi.setFundName(bi.getZcPProMitemBi().getFundName());
        zcPProMitemBi.setInceptdocCode(bi.getZcPProMitemBi().getInceptdocCode());
        zcPProMitemBi.setInceptdocName(bi.getZcPProMitemBi().getInceptdocName());
        zcPProMitemBi.setManageCode(bi.getZcPProMitemBi().getManageCode());
        zcPProMitemBi.setManageName(bi.getZcPProMitemBi().getManageName());
        zcPProMitemBi.setNd(bi.getZcPProMitemBi().getNd());
        zcPProMitemBi.setOrgCode(bi.getZcPProMitemBi().getOrgCode());
        zcPProMitemBi.setOrgName(bi.getZcPProMitemBi().getOrgName());
        zcPProMitemBi.setOriginCode(bi.getZcPProMitemBi().getOriginCode());
        zcPProMitemBi.setOriginName(bi.getZcPProMitemBi().getOriginName());
        zcPProMitemBi.setOutlayCode(bi.getZcPProMitemBi().getOutlayCode());
        zcPProMitemBi.setOutlayName(bi.getZcPProMitemBi().getOutlayName());
        zcPProMitemBi.setPayoutCode(bi.getZcPProMitemBi().getPayoutCode());
        zcPProMitemBi.setPayoutName(bi.getZcPProMitemBi().getPayoutName());
        zcPProMitemBi.setPaytypeCode(bi.getZcPProMitemBi().getPaytypeCode());
        zcPProMitemBi.setPaytypeName(bi.getZcPProMitemBi().getPaytypeName());
        zcPProMitemBi.setProjectCode(bi.getZcPProMitemBi().getProjectCode());
        zcPProMitemBi.setProjectName(bi.getZcPProMitemBi().getProjectName());
        zcPProMitemBi.setProjectTypeCode(bi.getZcPProMitemBi().getProjectTypeCode());
        zcPProMitemBi.setProjectTypeName(bi.getZcPProMitemBi().getProjectTypeName());
        zcPProMitemBi.setSenddocCode(bi.getZcPProMitemBi().getSenddocCode());
        zcPProMitemBi.setSenddocName(bi.getZcPProMitemBi().getSenddocName());
        zcPProMitemBi.setSenddocTypeCode(bi.getZcPProMitemBi().getSenddocTypeCode());
        zcPProMitemBi.setSenddocTypeName(bi.getZcPProMitemBi().getSenddocTypeName());
        zcPProMitemBi.setZcBiHtbaSum(bi.getZcPProMitemBi().getZcBiHtbaSum());
        zcPProMitemBi.setZcCgType(bi.getZcPProMitemBi().getZcCgType());
        zcPProMitemBi.setZcPlanType(bi.getZcPProMitemBi().getZcPlanType());
        zcPProMitemBi.setZcSaveNum(bi.getZcPProMitemBi().getZcSaveNum());
        zcPProMitemBi.setZcCatalogueCode(bi.getZcPProMitemBi().getZcCatalogueCode());
        zcPProMitemBi.setZcCatalogueName(bi.getZcPProMitemBi().getZcCatalogueName());
        zcPProMitemBi.setZcYear(bi.getZcPProMitemBi().getZcYear());
        zcPProMitemBi.setZcYepSum(bi.getZcPProMitemBi().getZcYepSum());
        zcPProMitemBi.setZcBiApdFlag(bi.getZcPProMitemBi().getZcBiApdFlag());
        zcPProMitemBi.setZcFundRemark(bi.getZcPProMitemBi().getZcFundRemark());
        zcPProMitemBi.setZcFundFile(bi.getZcPProMitemBi().getZcFundFile());
        zcPProMitemBi.setZcFundFileBlobid(bi.getZcPProMitemBi().getZcFundFileBlobid());
        zcPProMitemBi.setAllSuppleMentAmount(bi.getZcPProMitemBi().getAllSuppleMentAmount());
        zcPProMitemBi.setZcProBiSeq(bi.getZcPProMitemBi().getZcProBiSeq());
        zcPProMitemBi.setZcBiUsedSum(bi.getZcPProMitemBi().getZcBiUsedSum());
        zcPProMitemBi.setZcUseBiId(bi.getZcPProMitemBi().getZcUseBiId());

        zcPProMitemBi.setZcBiUsedSum(bi.getZcPProMitemBi().getZcBiJhuaSum());

        bic.setZcPProMitemBi(zcPProMitemBi);
        bis.add(bic);
      }
    }
    zcXmcgHt.setBiList(bis);

    // 商品
    List<ZcTBchtItemChg> its = new ArrayList<ZcTBchtItemChg>();
    if (old.getItemList() != null) {
      for (int i = 0; i < old.getItemList().size(); i++) {
        ZcTBchtItem it = (ZcTBchtItem) old.getItemList().get(i);
        ZcTBchtItemChg itc = new ZcTBchtItemChg();
        itc.setZcCtgryId(it.getZcCtgryId());
        itc.setZcHtCode(it.getZcHtCode());
        itc.setZcYear(it.getZcYear());
        itc.setZcCatalogueCode(it.getZcCatalogueCode());
        itc.setZcCatalogueName(it.getZcCatalogueName());
        itc.setZcIsJnjs(it.getZcIsJnjs());
        itc.setZcBraName(it.getZcBraName());
        itc.setZcMerSpec(it.getZcMerSpec());
        itc.setZcItemVal(it.getZcItemVal());
        itc.setZcCaigShl(it.getZcCaigShl());
        itc.setZcSpName(it.getZcSpName());
        itc.setZcMerIsZzcx(it.getZcMerIsZzcx());
        itc.setZcMerIsLshb(it.getZcMerIsLshb());
        itc.setZcMerMPrice(it.getZcMerMPrice());
        itc.setZcHtBiMoney(it.getZcHtBiMoney());
        itc.setZcHtOtherMoney(it.getZcHtOtherMoney());
        itc.setZcHtGkMoney(it.getZcHtGkMoney());
        itc.setZcItemSum(it.getZcItemSum());
        itc.setBudgetBiMoney(it.getBudgetBiMoney());
        itc.setBudgetOtherMoney(it.getBudgetOtherMoney());
        itc.setZcCaigJldw(it.getZcCaigJldw());
        itc.setZcCaigPrice(it.getZcCaigPrice());
        itc.setZcCaigMoney(it.getZcCaigMoney());
        its.add(itc);
      }
    }
    zcXmcgHt.setItemList(its);

    // 支付
    List<ZcHtPrePayBillItemChg> pays = new ArrayList<ZcHtPrePayBillItemChg>();
    if (old.getPayBiList() != null) {
      for (int i = 0; i < old.getPayBiList().size(); i++) {
        ZcHtPrePayBillItem pay = (ZcHtPrePayBillItem) old.getPayBiList().get(i);
        ZcHtPrePayBillItemChg payc = new ZcHtPrePayBillItemChg();
        payc.setBillCode(pay.getBillCode());
        payc.setPayOrder(pay.getPayOrder());
        payc.setPayMoney(pay.getPayMoney());
        payc.setRemark(pay.getRemark());
        payc.setPercent(pay.getPercent());
        payc.setPayDate(pay.getPayDate());
        payc.setPayYear(pay.getPayYear());
        payc.setPayMonth(pay.getPayMonth());
        pays.add(payc);
      }
    }
    zcXmcgHt.setPayBiList(pays);
    if (zcXmcgHt.getZcPProMake().getZcMakeCode() != null) {

      zcXmcgHt.setZcMakeCode(zcXmcgHt.getZcPProMake().getZcMakeCode());

    }

    BigDecimal sum = new BigDecimal(0.00);

    List<ZcXmcgHtBiChg> tempList = new ArrayList<ZcXmcgHtBiChg>();

    for (int i = 0; i < zcXmcgHt.getBiList().size(); i++) {

      ZcXmcgHtBiChg bi = (ZcXmcgHtBiChg) zcXmcgHt.getBiList().get(i);

      ZcPProMitemBi zcPProMitemBi = bi.getZcPProMitemBi();

      if (zcPProMitemBi.getZcBiUsedSum() != null) {

        sum = sum.add(zcPProMitemBi.getZcBiUsedSum());

      }

      bi.setZcProBiSeq(zcPProMitemBi.getZcProBiSeq());

      if (zcPProMitemBi.getZcBiUsedSum() == null) {

        zcPProMitemBi.setZcBiUsedSum(BigDecimal.ZERO);

      }

      if (bi.getZcBiBcsySum() == null) {

        bi.setZcBiBcsySum(BigDecimal.ZERO);

      }

      // bi.setZcBiSySum(zcPProMitemBi.getZcBiUsedSum().subtract(bi.getZcBiBcsySum()));

      tempList.add(bi);

    }
    if (isSubHt()) {
      tempList = this.zcEbBaseServiceDelegate.queryDataForList("ZC_XMCG_HT_BI.ibatorgenerated_selectBiByHtCode", zcXmcgHt.getZcHtCode(),
        this.requestMeta);

      String sumId = "";
      for (int i = 0; i < tempList.size(); i++) {
        ZcXmcgHtBi bi = (ZcXmcgHtBi) tempList.get(i);
        if (bi.getZcBiNo() == null || "".equals(bi.getZcBiNo())) {
          bi.setZcBiDoSum(null);
          continue;
        }

        if (sumId.length() > 0) {
          sumId = sumId + ",'" + bi.getZcBiNo() + "'";
        } else {
          sumId = "'" + bi.getZcBiNo() + "'";
        }
      }
      getBiDto.setZcText3(sumId);
    }

    zcXmcgHt.getZcPProMake().setZcMoneyBiSum(sum);

    zcXmcgHt.setBiList(tempList);

    findMainHt(zcXmcgHt);

  }

  public class ZcPHtHandler implements IForeignEntityHandler {

    private String columNames[];

    public ZcPHtHandler(String columNames[]) {

      this.columNames = columNames;

    }

    //

    public void excute(List selectedDatas) {

      // TODO Auto-generated method stub

      if (selectedDatas != null && selectedDatas.size() > 0) {

        ZcXmcgHt temp = (ZcXmcgHt) selectedDatas.get(0);

        ZcXmcgHtChg zcXmcgHt = (ZcXmcgHtChg) getListCursor().getCurrentObject();

        ZcXmcgHt old = zcXmcgHtServiceDelegate.selectByPrimaryKey(temp.getZcHtCode(), requestMeta);
        zcXmcgHt.setZcXmht(old);
        ht2Chg(zcXmcgHt, old);

        listCursor.setCurrentObject(zcXmcgHt);
        setEditingObject(zcXmcgHt);
        redraw();
        ((ZcXmcgHtChgDialog) parent).getEditPanel().setEditingObject(zcXmcgHt);
        ((ZcXmcgHtChgDialog) parent).getEditPanel().refreshData();

      }

    }

    public void afterClear() {

      ZcXmcgHt zcXmcgHt = (ZcXmcgHt) getListCursor().getCurrentObject();

      zcXmcgHt.setZcHtNum(BigDecimal.ZERO);
      zcXmcgHt.setZcHtCode("");
      zcXmcgHt.setZcHtName("");
      setEditingObject(zcXmcgHt);

      redraw();
    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcXmcgHt rowData = (ZcXmcgHt) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getZcHtCode();

        data[i][col++] = rowData.getZcHtName();

        data[i][col++] = rowData.getZcHtNum();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        public boolean isCellEditable(int row, int colum) {

          return false;

        }

      };

      return model;

    }

    @Override
    public boolean isMultipleSelect() {

      // TODO Auto-generated method stub

      return false;

    }

  }

  private void redraw() {
    ZcXmcgHtChg zcXmcgHt = (ZcXmcgHtChg) this.listCursor.getCurrentObject();

    if (!isSubHt()) {
      biTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData(zcXmcgHt.getBiList(), false));
    } else {
      biTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableDataForSubHt(zcXmcgHt.getBiList(), wfCanEditFieldMap));
    }

    itemTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubItemTableData(zcXmcgHt.getItemList(),isCar()));

    // 翻译从表表头列
    if (!isSubHt()) {
      ZcUtil.translateColName(biTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getBiInfo());
    } else {
      ZcUtil.translateColName(biTablePanel.getTable(), ZcXmcgHtToTableModelConverter.biInfoForSubHtWihtBugdet);

      getBiDto.setNd(requestMeta.getSvNd());
      getBiDto.setZcText2("1");
      getBiDto.setCoCode(requestMeta.getSvCoCode());

    }
    ZcUtil.translateColName(itemTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getItemInfo());

    // 设置从表监听

    addItemTableLisenter(itemTablePanel.getTable());

    addBiTableLisenter(biTablePanel.getTable());

    setBiTableEditor(biTablePanel.getTable());

    setItemTableEditor(itemTablePanel.getTable());

    this.refreshSubData(zcXmcgHt.getPayBiList());

    if (isGys()) {

      JPageableFixedTable ta = itemTablePanel.getTable();

      hideCol(ta, "ZC_ITEM_SUM");

      hideCol(ta, "BUDGET_BI_MONEY");

      hideCol(ta, "BUDGET_OTHER_MONEY");

      hideCol(ta, "ZC_ITEM_VAL");

      hideCol(ta, "ZC_HT_BI_MONEY");

      hideCol(ta, "ZC_HT_OTHER_MONEY");

      hideCol(ta, "ZC_HT_GK_MONEY");

    }

    updateFieldEditorsEditable();

    setSumLabelText();
  }

  protected void doCallback() {

    boolean success = true;

    ZcXmcgHtChg afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.callbackButton.getFuncId());

      ZcXmcgHtChg ht = (ZcXmcgHtChg) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      ht.setZcConText("0");

      this.zcXmcgHtChgServiceDelegate.updateZcXmcgHtChgFN(ht, false, WorkEnv.getInstance().getWebRoot(), requestMeta);

      afterSaveBill = zcXmcgHtChgServiceDelegate.callbackFN(ht, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.clistPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "收回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  protected void doPrevious() {

    //		if (isDataChanged()) {
    //
    //			int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存",
    //					"保存确认", 0);
    //
    //			if (num == JOptionPane.YES_OPTION) {
    //
    //				if (!doSave()) {
    //
    //					return;
    //
    //				}
    //
    //			} else {
    //
    //				listCursor.setCurrentObject(this.getOldZcXmcgHt());
    //
    //			}
    //
    //		}

    listCursor.previous();

    refreshData();

  }

  protected void doNext() {

    //		if (isDataChanged()) {
    //
    //			int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存",
    //					"保存确认", 0);
    //
    //			if (num == JOptionPane.YES_OPTION) {
    //
    //				if (!doSave()) {
    //
    //					return;
    //
    //				}
    //
    //			} else {
    //
    //				listCursor.setCurrentObject(this.getOldZcXmcgHt());
    //
    //			}
    //
    //		}

    listCursor.next();

    refreshData();

  }

  public boolean isDataChanged() {

    return !DigestUtil.digest(getOldZcXmcgHt()).equals(DigestUtil.digest(listCursor.getCurrentObject()));

  }

  public boolean doSave() {

    if (!isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return true;

    }

    if (checkBeforeSave()) {

      return false;

    }

    if (!isGys() && calcBeforeSave()) {

      return false;

    }

    boolean success = true;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(saveButton.getFuncId());

      ZcXmcgHtChg inData = (ZcXmcgHtChg) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      wordPane.save();

      String fileId = saveBulletinContent();

      if (fileId != null && !"".equals(fileId)) {

        inData.setZcConTextBlobid(fileId);

      }
      if (!isSubHt()) {
        makeToHtBi(inData);
      }

      ZcXmcgHtChg zcXmcgHt = this.zcXmcgHtChgServiceDelegate.updateZcXmcgHtChgFN(inData, isSubHt(), WorkEnv.getInstance().getWebRoot(), requestMeta);

      listCursor.setCurrentObject(zcXmcgHt);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshData();

      this.clistPanel.refreshCurrentTabData();

      loadMoldButton.setEnabled(true);

      selectMoldButton.setEnabled(true);
      this.editButton.setEnabled(true);

    } else {

      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

    return true;

  }

  protected void doDelete() {

    requestMeta.setFuncId(deleteButton.getFuncId());

    ZcXmcgHtChg zcXmcgHt = (ZcXmcgHtChg) this.listCursor.getCurrentObject();

    if (zcXmcgHt.getZcHtChgId() == null || "".equalsIgnoreCase(zcXmcgHt.getZcHtChgId())) {

      JOptionPane.showMessageDialog(this, "尚未保存到数据库，无需删除！", "提示", JOptionPane.ERROR_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      boolean success = true;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(deleteButton.getFuncId());

        if (!"0".equals(zcXmcgHt.getZcHtStatus())) {

          JOptionPane.showMessageDialog(this, "非编辑状态单据，不可以删除！", "提示", JOptionPane.ERROR_MESSAGE);

          return;

        }
        this.zcXmcgHtChgServiceDelegate.deleteByPrimaryKeyFN(zcXmcgHt.getZcHtChgId(), isSubHt(), WorkEnv.getInstance().getWebRoot(),
          this.requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        this.listCursor.removeCurrentObject();

        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.refreshData();

        this.clistPanel.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

  }

  protected void doSend() {

    boolean success = true;

    ZcXmcgHtChg afterSaveBill = null;

    String errorInfo = "";

    if (this.isDataChanged() && !doSave()) {

      return;

    }

    try {

      requestMeta.setFuncId(this.sendButton.getFuncId());

      ZcXmcgHtChg ht = (ZcXmcgHtChg) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = this.zcXmcgHtChgServiceDelegate.newCommitFN(ht, false, requestMeta);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      this.refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.clistPanel.refreshCurrentTabData();

    }

  }

  protected void doSuggestPass() {

    //		if (isDataChanged()) {
    //
    //			JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示",
    //					JOptionPane.INFORMATION_MESSAGE);
    //
    //			return;
    //
    //		}

    if (calcBeforeSave()) {

      return;

    }

    ZcXmcgHtChg ht = (ZcXmcgHtChg) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

    requestMeta.setFuncId(this.suggestPassButton.getFuncId());

    GkCommentDialog commentDialog = null;

    commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    boolean success = true;

    String errorInfo = "";

    try {

      ht.setZcConText(String.valueOf(ZcSettingConstants.IS_GOON_AUDIT_NO));

      ht.setComment(commentDialog.getComment());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      this.zcXmcgHtChgServiceDelegate.auditFN(ht, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.refreshData();

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.clistPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  /*
   * 
   * 审核
   */

  protected void doAudit() {

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    if (checkBeforeSave()) {

      return;

    }

    boolean success = true;

    ZcXmcgHtChg afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditPassButton.getFuncId());

      ZcXmcgHtChg ht = (ZcXmcgHtChg) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = this.zcXmcgHtChgServiceDelegate.auditFN(ht, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.clistPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  /*
   * 
   * 销审
   */

  protected void doUnAudit() {

    boolean success = true;

    ZcXmcgHtChg afterSaveBill = null;

    String errorInfo = "";

    int i = JOptionPane.showConfirmDialog(this, "是否确定消审？", "确认", JOptionPane.INFORMATION_MESSAGE);

    if (i != 0) {

      return;

    }

    try {

      requestMeta.setFuncId(unAuditButton.getFuncId());

      ZcXmcgHtChg ht = (ZcXmcgHtChg) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = this.zcXmcgHtChgServiceDelegate.unAuditFN(ht, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "销审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.clistPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "销审失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  public void doExit() {

    if (this.wordPane != null && wordPane.isDocOpened()) {
      wordPane.closeNotSave();
    }

    if (this.editPanel != null) {
      this.editPanel.doExit();
    }
    this.parent.dispose();

  }

  /*
   * 
   * 退回
   */

  protected void doUnTread() {

    //		if (isDataChanged()) {
    //
    //			JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示",
    //					JOptionPane.INFORMATION_MESSAGE);
    //
    //			return;
    //
    //		}

    if (checkBeforeSave()) {

      return;

    }

    GkCommentUntreadDialog commentDialog = new GkCommentUntreadDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    boolean success = true;

    ZcXmcgHtChg afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(unTreadButton.getFuncId());

      ZcXmcgHtChg ht = (ZcXmcgHtChg) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      ht.setComment(commentDialog.getComment());

      ht.setZcConText("0");

      afterSaveBill = this.zcXmcgHtChgServiceDelegate.untreadFN(ht, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.clistPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "退回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  public JComponent createSubBillPanel() {

    final JTabbedPane topTabPane = new JTabbedPane();

    JTabbedPane biTabPane = new JTabbedPane();

    this.createSubInfo();

    biTablePanel.init();

    biTablePanel.getSearchBar().setVisible(false);

    biTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");

    biTablePanel.getTable().setShowCheckedColumn(true);

    biTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    biTabPane.addTab("资金构成", biTablePanel);

    JFuncToolBar bottomToolBar1 = new JFuncToolBar();

    FuncButton addBtn1 = new SubaddButton(false);

    JButton insertBtn1 = new SubinsertButton(false);

    JButton delBtn1 = new SubdelButton(false);

    bottomToolBar1.add(addBtn1);

    bottomToolBar1.add(insertBtn1);

    bottomToolBar1.add(delBtn1);

    if (!isSubHt()) {
      biTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);
    }

    JLabel label = new JLabel("合计");

    label.setName("_SUM_LABEL");

    label.setForeground(Color.black);

    label.setFont(new Font("宋体", Font.BOLD, 12));

    JPanel p = new JPanel();

    p.setBackground(new Color(99, 184, 255));

    p.setName("_SUM_PANEL");

    p.add(label, BorderLayout.CENTER, -1);

    biTablePanel.add(p, BorderLayout.SOUTH, -1);

    if (isSubHt()) {
      biTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);
    }

    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcXmcgHtBiChg zcXmcgHtBi = new ZcXmcgHtBiChg();

        zcXmcgHtBi.setTempId(Guid.genID());

        setItemBiDefaultValue(zcXmcgHtBi);

        int rowNum = addSub(biTablePanel, zcXmcgHtBi);

        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcXmcgHtBiChg zcXmcgHtBi = new ZcXmcgHtBiChg();

        zcXmcgHtBi.setTempId(Guid.genID());

        setItemBiDefaultValue(zcXmcgHtBi);

        int rowNum = insertSub(biTablePanel, zcXmcgHtBi);

        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteBiSub(biTablePanel);

        // 从新计算采购预算

        if (checkedRows.length > 0) {

          caculateMoney(((BeanTableModel<ZcXmcgHtBi>) biTablePanel.getTable().getModel()).getDataBeanList());

        }

      }

    });

    JTabbedPane itemTabPane = new JTabbedPane();

    itemTablePanel.init();

    itemTablePanel.setPanelId(this.getClass().getName() + "_itemTablePanel");

    itemTablePanel.getSearchBar().setVisible(false);

    itemTablePanel.setTablePreferencesKey(this.getClass().getName() + "_itemTable");

    itemTablePanel.getTable().setShowCheckedColumn(true);

    itemTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    itemTabPane.addTab("商品构成", itemTablePanel);

    JGroupableTableHeader itemTableHeader = itemTablePanel.getTable().getTableHeader();

    itemTableHeader.addColumnGroup("采购预算资金", new String[] { "ZC_ITEM_SUM", "BUDGET_BI_MONEY", "BUDGET_OTHER_MONEY" });

    itemTableHeader.addColumnGroup("实际采购资金", new String[] { "ZC_ITEM_VAL", "ZC_HT_BI_MONEY", "ZC_HT_OTHER_MONEY" });

    JFuncToolBar bottomToolBar2 = new JFuncToolBar();

    FuncButton addBtn2 = new SubaddButton(false);

    JButton insertBtn2 = new SubinsertButton(false);

    JButton delBtn2 = new SubdelButton(false);

    bottomToolBar2.add(addBtn2);

    bottomToolBar2.add(insertBtn2);

    bottomToolBar2.add(delBtn2);

    itemTablePanel.add(bottomToolBar2, BorderLayout.SOUTH);

    addBtn2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcTBchtItemChg zcTBchtItem = new ZcTBchtItemChg();

        zcTBchtItem.setTempId(Guid.genID());

        int rowNum = addSub(itemTablePanel, zcTBchtItem);

        itemTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcTBchtItemChg zcTBchtItem = new ZcTBchtItemChg();

        zcTBchtItem.setTempId(Guid.genID());

        int rowNum = insertSub(itemTablePanel, zcTBchtItem);

        itemTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        deleteSub(itemTablePanel);

      }

    });

    biTabPane.setMinimumSize(new Dimension(240, 150));

    itemTabPane.setMinimumSize(new Dimension(240, 300));

    JSaveableSplitPane splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, biTabPane, itemTabPane);

    splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 150);

    splitPane.setContinuousLayout(true);

    if (isGys()) {

      biTabPane.setVisible(false);

      splitPane.setOneTouchExpandable(false);

    } else {

      splitPane.setOneTouchExpandable(true);

    }

    // 只显示向下的箭头

    // splitPane.putClientProperty("toExpand", true);

    splitPane.setDividerSize(10);

    // splitPane.setDividerLocation(260);

    splitPane.setBackground(getBackground());

    topTabPane.addTab("合同明细", splitPane);

    topTabPane.addChangeListener(new ChangeListener() {

      public void stateChanged(ChangeEvent e) {

        int index = topTabPane.getSelectedIndex();

        if (index == 1) {

          ZcXmcgHtChg ht = (ZcXmcgHtChg) listCursor.getCurrentObject();

          if (ht.getZcConTextBlobid() != null && wordPane.getOpenFile() == null) {

            ZcEbBulletinWordMold w = new ZcEbBulletinWordMold();

            w.setFileID(ht.getZcConTextBlobid());

            doOpenMold(null, w);

          }

        }

      }

    });
    topTabPane.addTab("付款明细", detailTablePanel);

    topTabPane.addTab("合同文本", wordPane);

    ZcXmcgHtChg ht = (ZcXmcgHtChg) listCursor.getCurrentObject();

    if (ht != null) {

      List list = getWFNodeEnableFunc(ht, requestMeta);

      if (!list.contains(editButton.getFuncId())) {

        bottomToolBar1.setEnabled(false);

        bottomToolBar2.setVisible(false);

      }

    }

    return topTabPane;

  }

  private void createSubInfo() {

    detailTablePanel.init();

    detailTablePanel.getSearchBar().setVisible(false);

    detailTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");

    detailTablePanel.getTable().setShowCheckedColumn(true);

    detailTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    // jTabbedPane.addTab("付款明细", detailTablePanel);
    JFuncToolBar bottomToolBar1 = new JFuncToolBar();

    FuncButton addBtn1 = new SubaddButton(false);

    FuncButton insertBtn1 = new SubinsertButton(false);

    FuncButton delBtn1 = new SubdelButton(false);

    bottomToolBar1.add(addBtn1);

    bottomToolBar1.add(insertBtn1);

    bottomToolBar1.add(delBtn1);

    detailTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);

    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcHtPrePayBillItemChg detail = new ZcHtPrePayBillItemChg();

        detail.setTempId(Guid.genID());

        setdetailBiDefaultValue(detail);

        int rowNum = addSub(detailTablePanel, detail);

        detailTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcHtPrePayBillItemChg detail = new ZcHtPrePayBillItemChg();

        detail.setTempId(Guid.genID());

        setdetailBiDefaultValue(detail);

        int rowNum = insertSub(detailTablePanel, detail);

        detailTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteSub(detailTablePanel);

      }

    });
  }

  protected void setEditPanel(ZcXmcgHtOldEditPanel panel) {
    editPanel = panel;
  }
}
