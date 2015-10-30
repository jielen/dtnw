/**   
* @(#) project: zcxa
* @(#) file: ZcPProBalEditPanel.java
* 
* Copyright 2010 UFGOV, Inc. All rights reserved.
* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
* 
*/
package com.ufgov.zc.client.zc.zcpprobal;

import java.awt.Color;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcPProBalToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.GkCommentUntreadDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AuditPassButton;
import com.ufgov.zc.client.component.button.CallbackButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.EditPayVoucherButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.PrintButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SaveSendButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.SendGkButton;
import com.ufgov.zc.client.component.button.SuggestAuditPassButton;
import com.ufgov.zc.client.component.button.TraceButton;
import com.ufgov.zc.client.component.button.UnauditButton;
import com.ufgov.zc.client.component.button.UntreadButton;
import com.ufgov.zc.client.component.event.ValueChangeEvent;
import com.ufgov.zc.client.component.event.ValueChangeListener;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CheckBoxFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.datacache.CompanyDataCache;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.budget.model.VwBudgetGp;
import com.ufgov.zc.common.commonbiz.model.Company;
import com.ufgov.zc.common.commonbiz.model.EAcc;
import com.ufgov.zc.common.commonbiz.model.WfAware;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.model.AsOption;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.system.util.Utils;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcEbDaibian;
import com.ufgov.zc.common.zc.model.ZcHtPrePayBillItem;
import com.ufgov.zc.common.zc.model.ZcPProBal;
import com.ufgov.zc.common.zc.model.ZcPProBalBi;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbSupplierServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcPProBalServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcPProMakeServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcXmcgHtServiceDelegate;

/**
 * 资金支付
 * 特殊情况：
 * OPT_ZC_IS_LINK_FA      Y/N 是否检查资产入库，如果是，则最后一笔支付时，去资产库中检查：1）有没有这个合同的对应的卡片；2）合同金额是否和卡片的购买金额相等； 
 * OPT_ZC_HT_FA_EQUAL_SUM     Y/N 如果要求录入资产，则设置一个变量，对金额是否相等这个条件进行打开和关闭，正常情况是需要判断的，特殊情况，有管理员设置手工关闭这个值，让该支付审批通过，然后再打开。
 * 这个值，由于系统对option的值有缓存，所以读取该值时，不能用常用的方法，要直接从数据库来读取
 * 例如系统运维费用、会议费等，这些不进入资产，这类合同的支付，就必须关掉这个变量，不进行资产金额和合同金额的校验，可以进行支付
* @ClassName: ZcPProBalEditPanel
* @Description: TODO(这里用一句话描述这个类的作用)
* @date: 2010-8-2 下午06:42:45
* @version: V1.0 
* @since: 1.0
* @author: Administrator
* @modify: 
*/
public class ZcPProBalEditPanel extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZcPProBalEditPanel.class);

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_P_PRO_BAL";

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  private FuncButton saveButton = new SaveButton();

  private EditButton editButton = new EditButton();

  //工作流送审
  private FuncButton sendButton = new SendButton();

  // 工作流收回
  private FuncButton callbackButton = new CallbackButton();

  // 工作流填写意见审核通过
  private FuncButton suggestPassButton = new SuggestAuditPassButton();

  // 工作流审核通过
  private FuncButton auditPassButton = new AuditPassButton();

  // 工作流销审
  private FuncButton unAuditButton = new UnauditButton();

  // 工作流退回
  private FuncButton unTreadButton = new UntreadButton();

  // 工作流流程跟踪

  private FuncButton traceButton = new TraceButton();

  private FuncButton deleteButton = new DeleteButton();

  private FuncButton previousButton = new PreviousButton();

  private FuncButton nextButton = new NextButton();

  private FuncButton printButton = new PrintButton();

  private FuncButton exitButton = new ExitButton();

  //送国库
  private FuncButton sendGkButton = new SendGkButton();

  private FuncButton editPayVoucherButton = new EditPayVoucherButton();

  private FuncButton saveAndSendButton = new SaveSendButton();

  private BillElementMeta biBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_P_PRO_BAL_BI");

  private ListCursor listCursor;

  private ZcPProBal oldZcPProBal;

  private ZcPProBalListPanel listPanel;

  private JTablePanel biTablePanel = new JTablePanel();

  private ZcPProBalEditPanel self = this;

  private GkBaseDialog parent;

  private ElementConditionDto htElementCondtiontDto = new ElementConditionDto();

  private ElementConditionDto zcMakeElementCondtiontDto = new ElementConditionDto();

  private ElementConditionDto prePayElementCondtiontDto = new ElementConditionDto();

  private ForeignEntityFieldEditor zcHtSelectEdit;

  private ForeignEntityFieldEditor zcPrePayBillSelectEdit;

  ForeignEntityFieldEditor zcMakeSelectEdit;

  private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  AsValFieldEditor zcBalStatus;

  JTabbedPane tabPane = new JTabbedPane();

  private ZcPProBalReturnBiPanel returnBiPanel;

  private boolean isHtPrePay = false;//是否存在预付款确认单

  SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");

  //协议供货预算限额
  BigDecimal returnMoenyLimit = new BigDecimal("".equals(AsOptionMeta.getOptVal("OPT_ZC_P_PRO_RETURN_MONEY_LIMIT")) ? "0"
    : AsOptionMeta.getOptVal("OPT_ZC_P_PRO_RETURN_MONEY_LIMIT"));

  public IZcPProBalServiceDelegate zcPProBalServiceDelegate = (IZcPProBalServiceDelegate) ServiceFactory.create(IZcPProBalServiceDelegate.class,
    "zcPProBalServiceDelegate");

  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
    "baseDataServiceDelegate");

  public IZcEbSupplierServiceDelegate zcEbSupplierServiceDelegate = (IZcEbSupplierServiceDelegate) ServiceFactory.create(
    IZcEbSupplierServiceDelegate.class, "zcEbSupplierServiceDelegate");

  public IZcXmcgHtServiceDelegate zcXmcgHtServiceDelegate = (IZcXmcgHtServiceDelegate) ServiceFactory.create(IZcXmcgHtServiceDelegate.class,
    "zcXmcgHtServiceDelegate");

  private ElementConditionDto eaccDto = new ElementConditionDto();

  public ZcPProBalEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcPProBalListPanel listPanel) {
    super(ZcPProBal.class, listPanel.getBillElementMeta());
    this.listCursor = listCursor;
    this.listPanel = listPanel;
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
      LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_TITLE), TitledBorder.CENTER, TitledBorder.TOP,
      new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.parent = parent;
    this.colCount = 3;
    init();
    requestMeta.setCompoId(compoId);
    if (ZcUtil.isCgdb()) {
      //用于代编代报里，置换requestmeta里的信息
      resetRequestMeta();
    }
    refreshData();
  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();
    AutoNumFieldEditor zcBalId = new AutoNumFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_ID), "zcBalId");
    //    editorList.add(zcBalId);

    /*
     * 选择单位
     */
    zcMakeElementCondtiontDto.setStatus("exec");
    final CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_CO_CODE), "coCode");
    zcCoCode.addValueChangeListener(new ValueChangeListener() {
      public void valueChanged(ValueChangeEvent e) {
        Company company = (Company) zcCoCode.getValue();
        if (company != null) {
          zcMakeElementCondtiontDto.setCoCode(company.getCode());
        }
      }
    });
    zcMakeElementCondtiontDto.setNd(this.requestMeta.getSvNd());

    TextFieldEditor zcCoCodeNd = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_CO_CODE_ND), "nd");

    /*
     * 选择项目
     */

    String zcMakeColumNames[] = { LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MAKE_NAME),
      LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MAKE_CODE), LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MONEY_BI_SUM) };
    ZcPProMakeHandler handler1 = new ZcPProMakeHandler(zcMakeColumNames);
    zcMakeSelectEdit = new ForeignEntityFieldEditor("ZC_P_PRO_MAKE.selectProjectForJSXM", zcMakeElementCondtiontDto, 20, handler1, zcMakeColumNames,
      LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MAKE_CODE), "zcMakeCode");
    TextFieldEditor zcMakeName = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MAKE_NAME), "zcMakeName");
    MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MONEY_BI_SUM), "zcMoneyBiSum");

    /**
     * 选择合同与供应商
     */
    String HtColumNames[] = { LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_HT_CODE),
      LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_HT_NAME), LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MAKE_NAME),
      LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_SU_NAME), LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_HT_NUM) };
    ZcHtCodeHandler handler = new ZcHtCodeHandler(HtColumNames);
    if (isGys()) {
      htElementCondtiontDto.setZcText0(WorkEnv.getInstance().getRequestMeta().getSvUserID());
    }
    if (isYsdw()) {
      htElementCondtiontDto.setCoCode(WorkEnv.getInstance().getRequestMeta().getSvCoCode());
      //丹徒地区的支付由供应商发起，预算单位只能发起汽车采购的支付申请，所以加了这个条件
      htElementCondtiontDto.setZcText3("onlyCar");
    }
    htElementCondtiontDto.setZcText2("canPaySumNotNull");
    htElementCondtiontDto.setNd(requestMeta.getSvNd());
    zcHtSelectEdit = new ForeignEntityFieldEditor("ZC_XMCG_HT.selectBalHTList", htElementCondtiontDto, 20, handler, HtColumNames,
      LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_HT_CODE), "zcHtCode");

    TextFieldEditor zcHtName = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_HT_NAME), "zcHtName");
    /**
     * 选择分期付款确认单
     */

    String billColumNames[] = { "分期次数", "支付金额", "支付百分比", "支付日期" };
    ZcHtPrePayBillHandler billHandler = new ZcHtPrePayBillHandler(billColumNames);
    zcPrePayBillSelectEdit = new ForeignEntityFieldEditor("ZC_HT_PRE_PAY_BILL.selectITEMListByElement", prePayElementCondtiontDto, 20, billHandler,
      billColumNames, "付款分期", "payOrder");
    zcPrePayBillSelectEdit.setEnabled(false);
    MoneyFieldEditor zcHtNum = new MoneyFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_HT_NUM), "zcHtNum");

    MoneyFieldEditor zcBalSum = new MoneyFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_SUM), "zcBalSum");
    TextFieldEditor zcSuBankName = new TextFieldEditor("供应商开户银行", "zcSuBankName");
    //    TextFieldEditor zcSuBankCode = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_SU_BANK_CODE), "zcSuBankCode");
    //    editorList.add(zcSuBankCode);
    TextFieldEditor zcSuAccName = new TextFieldEditor("供应商开户名称", "zcSuBankCode");
    TextFieldEditor zcSuAccCode = new TextFieldEditor("供应商银行账号", "zcSuAccCode");

    TextFieldEditor zcSuName = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_SU_NAME), "zcSuName");

    TextFieldEditor zcInputCode = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_CODE), "zcInputCode");

    TextFieldEditor zcInputName = new TextFieldEditor("录入人", "zcInputName");

    TextFieldEditor zcReqTel = new TextFieldEditor("录入人电话", "zcReqTel");

    TextFieldEditor zcCoName = new TextFieldEditor("采购单位", "coName");

    DateFieldEditor zcInputDate = new DateFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_DATE), "zcInputDate");

    TextFieldEditor zcRemark = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_REMARK), "zcRemark");
    zcBalStatus = new AsValFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_STATUS), "zcBalStatus",
      "ZC_VS_BALANCE_STATUS");
    CheckBoxFieldEditor isLastPay = new CheckBoxFieldEditor("最后一次支付", "isLastPay");

    //合同待支付金额
    MoneyFieldEditor canPaySum = new MoneyFieldEditor("未支付金额", "canPaySum");

    MoneyFieldEditor biSum = new MoneyFieldEditor("指标支付金额", "zcBiSum");

    //    editorList.add(zcCoCodeNd);
    //    editorList.add(zcReqTel);

    editorList.add(zcHtSelectEdit);
    editorList.add(zcHtName);
    editorList.add(zcBalStatus);

    editorList.add(zcHtNum);
    editorList.add(zcCoName);
    editorList.add(zcSuName);
    //    editorList.add(zcMakeSelectEdit);

    //    editorList.add(zcMakeName);
    //    editorList.add(zcMoneyBiSum);

    editorList.add(canPaySum);
    editorList.add(zcSuBankName);
    editorList.add(zcSuAccCode);

    editorList.add(zcBalSum);
    if (isYsdw()) {
      editorList.add(biSum);
    }
    editorList.add(zcSuAccName);
    editorList.add(zcInputName);
    editorList.add(zcInputDate);

    //    editorList.add(zcPrePayBillSelectEdit);

    editorList.add(zcRemark);
    //    editorList.add(isLastPay);

    return editorList;
  }

  /*
   * 选择合同的web实体
   */
  private class ZcHtCodeHandler implements IForeignEntityHandler {
    private String columNames[];

    public ZcHtCodeHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {
      for (Object object : selectedDatas) {
        ZcXmcgHt zcXmcgHt = (ZcXmcgHt) object;
        ZcPProBal bean = (ZcPProBal) self.listCursor.getCurrentObject();
        bean.setZcHtCode(zcXmcgHt.getZcHtCode());
        bean.setZcHtName(zcXmcgHt.getZcHtName());
        bean.setZcHtNum(zcXmcgHt.getZcHtNum());
        bean.setZcSuName(zcXmcgHt.getZcSuName());
        bean.setZcSuCode(zcXmcgHt.getZcSuCode());
        bean.setZcSuBankName(zcXmcgHt.getZcSuBankName());
        //目前由于供应商的开户名称可能与公司名称不符，而zcsubankcode存放的是开户名称，先取这个值，为空，取公司名称
        bean.setZcSuBankCode(zcXmcgHt.getZcSuBankCode() == null ? zcXmcgHt.getZcSuName() : zcXmcgHt.getZcSuBankCode());
        bean.setZcSuAccCode(zcXmcgHt.getZcSuAccCode());
        bean.setZcSuAccName(zcXmcgHt.getZcSuName());
        bean.setZcSuLinkMan(zcXmcgHt.getZcSuLinkman());
        bean.setZcSuTel(zcXmcgHt.getZcSuTel());
        bean.setCanPaySum(zcXmcgHt.getCanPaySum());
        bean.setPayOrder("");
        bean.setCoCode(zcXmcgHt.getCoCode());
        bean.setCoName(CompanyDataCache.getNameByCode(zcXmcgHt.getCoCode()));
        bean.setZcMakeCode(zcXmcgHt.getZcPProMake().getZcMakeCode());
        zcXmcgHt = (ZcXmcgHt) zcXmcgHtServiceDelegate.selectByPrimaryKey(zcXmcgHt.getZcHtCode(), requestMeta);
        bean.setHt(zcXmcgHt);
        //关联支付确认单，判断是否有预付款确认单
        prePayElementCondtiontDto.setZcText0(zcXmcgHt.getZcHtCode());
        //        List prePayList = zcEbBaseServiceDelegate.queryDataForList("ZC_HT_PRE_PAY_BILL.selectITEMListByHtCode", zcXmcgHt.getZcHtCode(), requestMeta);
        /*        if (!"1".equals(zcXmcgHt.getZcFukuanType())) {
                  isHtPrePay = true;
                  zcPrePayBillSelectEdit.setEnabled(true);
                  self.repaint();
                } else {
                  isHtPrePay = false;
                  zcPrePayBillSelectEdit.setEnabled(false);

                  bean.setZcBalSum(zcXmcgHt.getZcHtNum());
                }*/
        List<ZcPProBalBi> zcPProBalBiList = new ArrayList<ZcPProBalBi>();
        //供应商填制支付申请时，不带入资金明细，预算单位编辑时，带入
        if (isYsdw()) {
          zcPProBalBiList = buildZcPProBalBi(zcXmcgHt);
        }
        bean.setBiList(zcPProBalBiList);
        self.setEditingObject(bean);
        refreshSubData(bean.getBiList());
        listCursor.setCurrentObject(bean);
        if (isYsdw()) {
          updateFieldEditorsEditable();
        }
      }
    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        ZcXmcgHt zcXmcgHt = (ZcXmcgHt) showDatas.get(i);
        int col = 0;
        data[i][col++] = zcXmcgHt.getZcHtCode();
        data[i][col++] = zcXmcgHt.getZcHtName();
        data[i][col++] = zcXmcgHt.getZcPProMake().getZcMakeCode();
        data[i][col++] = zcXmcgHt.getZcSuName();
        data[i][col++] = zcXmcgHt.getZcHtNum();

      }
      MyTableModel model = new MyTableModel(data, columNames) {
        public boolean isCellEditable(int row, int colum) {
          return false;
        }
      };
      return model;
    }

    public boolean isMultipleSelect() {
      return false;
    }
  }

  /*
   * 
   */
  private class ZcEaccHandler implements IForeignEntityHandler {
    private String columNames[];

    public ZcEaccHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public boolean beforeSelect(ElementConditionDto dto) {

      JTable table = biTablePanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return false;

      int k2 = table.convertRowIndexToModel(k);

      ZcPProBalBi item = (ZcPProBalBi) model.getBean(k2);

      //      dto.setZcText0(item.getOutLayCode());
      dto.setZcText0(getOldOutLayCode(item.getZcBiNo()));

      return true;

    }

    public void excute(List selectedDatas) {

      JTable table = biTablePanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return;
      int k2 = table.convertRowIndexToModel(k);

      ZcPProBalBi item = (ZcPProBalBi) model.getBean(k2);

      for (Object object : selectedDatas) {
        EAcc e = (EAcc) object;
        item.setOutLayCode(e.getChrId());
        item.setOutLayName(e.getName());
        item.setOutLayIsLeaf(e.getIsLowest());
        return;
      }
    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        EAcc e = (EAcc) showDatas.get(i);
        int col = 0;
        data[i][col++] = e.getCode();
        data[i][col++] = e.getName();

      }
      MyTableModel model = new MyTableModel(data, columNames) {
        public boolean isCellEditable(int row, int colum) {
          return false;
        }
      };
      return model;
    }

    public boolean isMultipleSelect() {
      return false;
    }
  }

  /*
   * 选择分期付款项的外部实体
   */
  private class ZcHtPrePayBillHandler implements IForeignEntityHandler {
    private String columNames[];

    public ZcHtPrePayBillHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {
      for (Object object : selectedDatas) {
        ZcHtPrePayBillItem item = (ZcHtPrePayBillItem) object;
        ZcPProBal bean = (ZcPProBal) self.listCursor.getCurrentObject();
        bean.setZcBalSum(item.getPayMoney());
        bean.setPayOrder(item.getPayOrder());
        self.setEditingObject(bean);
        refreshSubData(bean.getBiList());
        listCursor.setCurrentObject(bean);
      }
    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        ZcHtPrePayBillItem item = (ZcHtPrePayBillItem) showDatas.get(i);
        int col = 0;
        data[i][col++] = item.getPayOrder();
        data[i][col++] = item.getPayMoney();
        data[i][col++] = item.getPercent();
        if (item.getPayDate() != null) {
          data[i][col++] = df.format(item.getPayDate());
        } else {
          data[i][col++] = "";
        }

      }
      MyTableModel model = new MyTableModel(data, columNames) {
        public boolean isCellEditable(int row, int colum) {
          return false;
        }
      };
      return model;
    }

    public boolean isMultipleSelect() {
      return false;
    }
  }

  /*
   * 选择项目的web实体
   */
  private class ZcPProMakeHandler implements IForeignEntityHandler {
    private String columNames[];

    public ZcPProMakeHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {
      for (Object object : selectedDatas) {
        ZcPProMake zcPProMake = (ZcPProMake) object;
        ZcPProBal bean = (ZcPProBal) self.listCursor.getCurrentObject();
        bean.setZcMakeCode(zcPProMake.getZcMakeCode());
        bean.setCoCode(zcPProMake.getCoCode());
        bean.setZcMakeName(zcPProMake.getZcMakeName());
        bean.setZcMoneyBiSum(zcPProMake.getZcMoneyBiSum());
        bean.setZcInputName(zcPProMake.getZcMakeLinkman());
        htElementCondtiontDto.setZcText0(zcPProMake.getZcMakeCode());
        htElementCondtiontDto.setIsNeedSqlFilterData("Y");
        zcHtSelectEdit.setEnabled(true);
        self.setEditingObject(bean);
      }
    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        ZcPProMake zcPProMake = (ZcPProMake) showDatas.get(i);
        int col = 0;
        data[i][col++] = zcPProMake.getZcMakeCode();
        data[i][col++] = zcPProMake.getZcMakeName();
        data[i][col++] = zcPProMake.getZcMoneyBiSum();
      }
      MyTableModel model = new MyTableModel(data, columNames) {
        public boolean isCellEditable(int row, int colum) {
          return false;
        }
      };
      return model;
    }

    public boolean isMultipleSelect() {
      return false;
    }

  }

  private List<ZcPProBalBi> buildZcPProBalBi(ZcXmcgHt zcXmcgHt) {
    List<ZcPProBalBi> zcPProBalBiList = new ArrayList<ZcPProBalBi>();
    for (int i = 0; i < zcXmcgHt.getBiList().size(); i++) {
      ZcXmcgHtBi htbi = (ZcXmcgHtBi) zcXmcgHt.getBiList().get(i);
      ZcPProBalBi balBi = new ZcPProBalBi();
        ZcPProMitemBi zcPProMitemBi = htbi.getZcPProMitemBi();
        //      Map map = new HashMap();
        //      map.put("zcHtBiNo", htbi.getZcProBiSeq());
        //      map.put("zcHtCode", zcXmcgHt.getZcHtCode());
        //      if (bean != null && bean.getZcBalId() != null) {
        //        map.put("zcBalId", bean.getZcBalId());
        //      }
        //      /*
        //       * 获得该资金的已经结算金额
        //       */
        //      BigDecimal yjjsMoney = zcPProBalServiceDelegate.getSumZcBalBiSum(map, requestMeta);

        /*     不获取指标余额，指标已经被冻结了，--chenjl 20130910
              if (htbi.getZcBiNo() != null && htbi.getZcBiNo().trim().length() > 0) {
                VwBudgetGp vp = (VwBudgetGp) zcEbBaseServiceDelegate.queryObject("VwBudgetGp.getVwBudgetGpById", htbi.getZcBiNo(), requestMeta);

                if (vp != null && vp.getCanuseMoney() != null) {
                  balBi.setZcBiBal(vp.getCanuseMoney());//指标余额
                } else {
                  System.out.println("获取指标余额失败！");
                }
              }*/
        //获得当前资金已经结算的金额
        balBi.setZcBiYjjsSum(_getYjjsSum(htbi.getZcHtCode(), htbi.getZcUseBiId()));
        /*      if (htbi.getZcBiYjjsSum() == null) {
                balBi.setZcBiYjjsSum(new BigDecimal("0.0"));
              } else {
                balBi.setZcBiYjjsSum(htbi.getZcBiYjjsSum());
              }*/
        balBi.setZcBiNo(zcPProMitemBi.getZcBiNo() == null || zcPProMitemBi.getZcBiNo().length() == 0 ? htbi.getZcBiNo() : zcPProMitemBi.getZcBiNo());
        //自筹资金
        if (balBi.getZcBiNo() == null || balBi.getZcBiNo().length() == 0) {
          balBi.setZcBiNo(ZcSettingConstants.No_BI + zcPProMitemBi.getZcProBiSeq());
        }
        balBi.setZcPaytypeCode(zcPProMitemBi.getPaytypeCode() == null || zcPProMitemBi.getPaytypeCode().length() == 0 ? htbi.getPaytypeCode()
          : zcPProMitemBi.getPaytypeCode());//支付方式编号
        balBi.setZcPaytypeName(zcPProMitemBi.getPaytypeName() == null || zcPProMitemBi.getPaytypeName().length() == 0 ? htbi.getPaytypeName()
          : zcPProMitemBi.getPaytypeName());//支付方式
        balBi.setZcFundCode(zcPProMitemBi.getFundCode() == null || zcPProMitemBi.getFundCode().length() == 0 ? htbi.getFundCode() : zcPProMitemBi
          .getFundCode());//资金性质
        balBi.setZcFundName(zcPProMitemBi.getFundName() == null || zcPProMitemBi.getFundName().length() == 0 ? htbi.getFundName() : zcPProMitemBi
          .getFundName());//资金性质
        balBi.setZcOriginCode(zcPProMitemBi.getOriginCode() == null || zcPProMitemBi.getOriginCode().length() == 0 ? htbi.getOriginCode()
          : zcPProMitemBi.getOriginCode());//指标来源
        balBi.setZcOriginName(zcPProMitemBi.getOriginName() == null || zcPProMitemBi.getOriginName().length() == 0 ? htbi.getOriginName()
          : zcPProMitemBi.getOriginName());//指标来源
        balBi.setZcBiSum(zcPProMitemBi.getZcBiJhuaSum() == null || zcPProMitemBi.getZcBiJhuaSum().longValue() == 0 ? htbi.getZcBiBcsySum()
          : zcPProMitemBi.getZcBiJhuaSum());//指标计划金额
        balBi.setZcBiBcsySum(htbi.getZcBiBcsySum());//合同使用金额
        balBi.setOutLayCode(zcPProMitemBi.getOutlayCode()==null || zcPProMitemBi.getOutlayCode().length()==0?htbi.getOutlayCode():zcPProMitemBi.getOutlayCode());//经济分类编号
        balBi.setOutLayName(zcPProMitemBi.getOutlayName()==null||zcPProMitemBi.getOutlayName().length()==0?htbi.getOutlayName():zcPProMitemBi.getOutlayName());//经济分类名称
        //      balBi.setOutLayIsLeaf(zcPProMitemBi.getout)
        balBi.setZcBisCode(zcPProMitemBi.getProjectCode()==null||zcPProMitemBi.getProjectCode().length()==0?htbi.getProjectCode():zcPProMitemBi.getProjectCode());//预算项目编号
        balBi.setZcBisName(zcPProMitemBi.getProjectName()==null||zcPProMitemBi.getProjectName().length()==0?htbi.getProjectName():zcPProMitemBi.getProjectName());//预算项目名称
        balBi.setZcBAccCode(zcPProMitemBi.getbAccCode()==null||zcPProMitemBi.getbAccCode().length()==0?htbi.getbAccCode():zcPProMitemBi.getbAccCode());//功能分类编号
        balBi.setZcBAccName(zcPProMitemBi.getbAccName()==null||zcPProMitemBi.getbAccName().length()==0?htbi.getbAccName():zcPProMitemBi.getbAccName());//功能分类名称
        balBi.setSendDocName(zcPProMitemBi.getSenddocName() == null || zcPProMitemBi.getSenddocName().length() == 0 ? htbi.getSenddocName()
          : zcPProMitemBi.getSenddocName());
        balBi.setZcHtBiNo(htbi.getZcUseBiId());//与f3系统链接指标系统时，这个值存放的是采购计划里占用指标时的vou_id，释放指标时，这个vou_id需要传递到f3系统中才可以执行，
        balBi.setZcHtCode(htbi.getZcHtCode());
        balBi.setZcCatalogueCode(htbi.getZcCatalogueCode());
        balBi.setZcCatalogueName(htbi.getZcCatalogueCode());
        balBi.setZcProBiSeq(zcPProMitemBi.getZcProBiSeq() == null || zcPProMitemBi.getZcProBiSeq().length() == 0 ? htbi.getZcProBiSeq()
          : zcPProMitemBi.getZcProBiSeq());
        balBi.setZcBalBiId(ZcUtil.getSequenceNextVal("ZcEbUtil.getZcProBalBiIdNextSeqVal"));
     
      zcPProBalBiList.add(balBi);
    }
    return zcPProBalBiList;
  }

  private BigDecimal _getYjjsSum(String zcHtCode, String zcUseBiId) {
    // TODO Auto-generated method stub
    HashMap map = new HashMap();
    map.put("zcHtCode", zcHtCode);
    map.put("zcHtBiNo", zcUseBiId);
    BigDecimal yjjsSum = (BigDecimal) zcEbBaseServiceDelegate.queryObject("ZC_P_PRO_BAL_BI.getSumZcBalBiSum", map, requestMeta);
    yjjsSum = yjjsSum == null ? new BigDecimal(0) : yjjsSum;
    return yjjsSum;
  }

  private Map<String, String> oldOutLayCodeMap = new HashMap<String, String>();

  private ZcPProMake zcPProMake = null;

  private String getOldOutLayCode(String zcBiNo) {
    // TODO Auto-generated method stub
    if (oldOutLayCodeMap.get(zcBiNo) != null) {
      return oldOutLayCodeMap.get(zcBiNo);
    }
    VwBudgetGp gp = (VwBudgetGp) zcEbBaseServiceDelegate.queryObject("VwBudgetGp.getVwBudgetGpByBiNo", zcBiNo, requestMeta);

    oldOutLayCodeMap.put(zcBiNo, gp.getBsiId());
    return gp.getBsiId();
  }

  @Override
  public JComponent createSubBillPanel() {
    if (isGys()) {
      return new JPanel();
    }

    biTablePanel.init();
    biTablePanel.getSearchBar().setVisible(false);
    biTablePanel.setTablePreferencesKey(this.getClass().getName() + "_table");
    biTablePanel.getTable().setShowCheckedColumn(true);
    biTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(50, 0));
    tabPane.addTab("资金构成", biTablePanel);
    return tabPane;
  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(compoId);
    toolBar.add(saveButton);
    toolBar.add(editButton);
    toolBar.add(deleteButton);
    toolBar.add(sendButton);
    toolBar.add(suggestPassButton);
    toolBar.add(auditPassButton);
    toolBar.add(callbackButton);
    toolBar.add(unAuditButton);
    toolBar.add(unTreadButton);
    toolBar.add(saveAndSendButton);
    toolBar.add(callbackButton);
    toolBar.add(traceButton);
    toolBar.add(previousButton);
    toolBar.add(nextButton);
    toolBar.add(sendGkButton);
    //    toolBar.add(editPayVoucherButton);
    //    toolBar.add(printButton);
    toolBar.add(exitButton);

    unAuditButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doUnAudit();
      }
    });

    sendButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doSend();
      }
    });

    previousButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doPrevious();
      }
    });

    nextButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doNext();
      }
    });

    exitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doExit();
      }
    });
    saveButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        doSave();
      }
    });

    editButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        doEdit();
      }
    });
    deleteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        doDelete();
      }
    });

    traceButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        doTrace();
      }
    });
    callbackButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        doCallback();
      }
    });
    suggestPassButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        // 填写意见审核
        doSuggestPass();
      }
    });

    auditPassButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 审核
        doAudit();
      }
    });
    unTreadButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 退回
        doUntread();
      }
    });
    sendGkButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        doSendGk();
      }
    });
    editPayVoucherButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        editPayVoucher();
      }
    });

    printButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doPrint();

      }

    });

  }

  private void addBiTableLisenter(final JPageableFixedTable table) {
    final BeanTableModel model = (BeanTableModel) (table.getModel());
    model.addTableModelListener(new TableModelListener() {
      public void tableChanged(TableModelEvent e) {
        //不再做监听，支付金额要和合同相符
        //        if (e.getColumn() >= 0 && ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_BI_BCJS_SUM.equals(model.getColumnIdentifier(e.getColumn()))) {
        //          int k = table.getSelectedRow();
        //          if (k < 0)
        //            return;
        //          self.caculateMoney(model.getDataBeanList());
        //        }
      }
    });
  }

  /*
   * 如果是单位，则将资金莉的指标金额合计到主表中，方便后续支付时，进行判断，如果含有指标资金，则送业务科室审批 
   */
  private void setBiSum() {
    if (!isYsdw()) {
      return;
    }
    ZcPProBal beanData = (ZcPProBal) this.listCursor.getCurrentObject();

    BigDecimal sum = BigDecimal.ZERO;
    List<ZcPProBalBi> biList = beanData.getBiList();

    for (ZcPProBalBi bi : biList) {
      if (bi.getZcBiNo() != null && !bi.getZcBiNo().startsWith(ZcSettingConstants.No_BI)) {
        sum = sum.add((BigDecimal) ObjectUtils.defaultIfNull(bi.getZcBiBcjsSum(), BigDecimal.ZERO));
      }
    }

    beanData.setZcBiSum(sum);
    this.setEditingObject(beanData);
  }

  private boolean caculateMoney() {
    ZcPProBal beanData = (ZcPProBal) this.listCursor.getCurrentObject();
    if (tabPane.getComponentCount() > 1) {
      tabPane.remove(returnBiPanel);
    }

    beanData.setReturnBiList(new ArrayList());

    if (!"Y".equals(beanData.getIsLastPay())) {
      return true;
    }

    Map<String, String> map = new HashMap<String, String>();
    map.put("makeCode", beanData.getZcMakeCode());
    map.put("balId", beanData.getZcBalId() == null ? " " : beanData.getZcBalId());

    int count = (Integer) this.zcEbBaseServiceDelegate.queryObject("ZC_P_PRO_BAL.checkExistsSending", map, this.requestMeta);
    if (count > 0) {
      JOptionPane.showMessageDialog(this, "该项目存在审批中的支付申请，不能选择最后一次支付", "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }

    //    BigDecimal over = (BigDecimal) this.zcEbBaseServiceDelegate.queryObject("ZC_P_PRO_BAL.getOverMoneyByZcMakeCode", map, this.requestMeta);

    return true;
  }

  private void doUnAudit() {
    boolean success = true;
    ZcPProBal afterSaveBill = null;

    int i = JOptionPane.showConfirmDialog(this, "是否确定消审？", "确认", JOptionPane.INFORMATION_MESSAGE);

    if (i != 0) {

      return;

    }
    try {
      // TODO Auto-generated method stub
      requestMeta.setFuncId(this.unAuditButton.getFuncId());
      ZcPProBal bal = (ZcPProBal) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
      bal.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      afterSaveBill = this.listPanel.zcPProBalServiceDelegate.unAuditFN(bal, requestMeta);
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      this.refreshAll(afterSaveBill, true);
      JOptionPane.showMessageDialog(this, "销审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
    }

  }

  private void doSend() {
    if (checkBeforeSave()) {
      return;
    }
    if (isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    boolean success = true;
    ZcPProBal afterSaveBill = null;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(this.sendButton.getFuncId());
      ZcPProBal bal = (ZcPProBal) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
      //      GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      //        ModalityType.APPLICATION_MODAL);
      //      if (commentDialog.cancel) {
      //        return;
      //      }
      //      bal.setComment(commentDialog.getComment());
      bal.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      afterSaveBill = this.listPanel.zcPProBalServiceDelegate.newCommitFN(bal, requestMeta);
    } catch (Exception ex) {
      errorInfo += ex.getMessage();
      logger.error(ex.getMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      this.refreshAll(afterSaveBill, true);
      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
    }
  }

  /*
   * 填写意见审核
   */
  private void doSuggestPass() {
    if (checkBeforeSave()) {
      return;
    }
    if (notSameCoBudget()) {
      JOptionPane.showMessageDialog(this, "指标对应的预算单位不是当前采购单位，请在资金构成中重新选择本单位的预算指标.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    //    if (isDataChanged()) {
    //      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
    //      return;
    //    }
    boolean success = true;
    ZcPProBal afterSaveBill = null;
    String errorInfo = "";
    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      ModalityType.APPLICATION_MODAL);
    if (commentDialog.cancel) {
      return;
    }
    try {
      IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
        "baseDataServiceDelegate");
      requestMeta.setFuncId(this.suggestPassButton.getFuncId());
      ZcPProBal bean = (ZcPProBal) this.listCursor.getCurrentObject();
      bean.setComment(commentDialog.getComment());
      bean.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      afterSaveBill = this.listPanel.zcPProBalServiceDelegate.auditFN(bean, "N", requestMeta);
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
  }

  private void doAudit() {
    //    if (checkBeforeSave()) {
    //      return;
    //    }
    //    if (isDataChanged()) {
    //      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
    //      return;
    //    }
    if (notSameCoBudget()) {
      JOptionPane.showMessageDialog(this, "指标对应的预算单位不是当前采购单位，请在资金构成中重新选择本单位的预算指标.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    boolean success = true;
    ZcPProBal afterSaveBill = null;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(this.auditPassButton.getFuncId());
      ZcPProBal bean = (ZcPProBal) this.listCursor.getCurrentObject();
      bean.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      //      afterSaveBill = this.listPanel.zcPProBalServiceDelegate.auditFN(bean, requestMeta);
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
  }

  /**
   * 判断当前审批单据的cocode与其资金的cocode是否一致，如果不一致，则提示，不能进行审批
   * 为什么这么做，因为丹徒的支付是供应商发起，然后采购单位匹配资金，有时候匹配了资金后，采购单位会退回给供应商
   * 这是供应商如果重新选择了新的采购单位，这时单据上的资金时上一个采购单位的，所以为了防止这种情况发生，在采购单位
   * 审批时，对主单的cocode和资金表的cocode进行校验
   * @return
   */
  private boolean notSameCoBudget() {
    // TODO Auto-generated method stub
    if (ZcUtil.isYsdw() || ZcUtil.isCgdb()) {
      ZcPProBal bean = (ZcPProBal) this.listCursor.getCurrentObject();
      if (bean.getBiList() == null || bean.getBiList().size() == 0)
        return false;
      List idLst = new ArrayList();
      StringBuffer idbf = new StringBuffer();
      for (int i = 0; i < bean.getBiList().size(); i++) {
        ZcPProBalBi bi = (ZcPProBalBi) bean.getBiList().get(i);
        if (!bi.getZcBiNo().startsWith("NoBi")) {
          if (idbf.length() > 0) {
            idbf.append(",");
          }
          idbf.append("'").append(bi.getZcBiNo()).append("'");
        }
      }
      if (idbf.length() == 0) {//没有指标，只有自筹资金的情况下，这个值是空的，因此默认给置成空的
        idbf.append("''");
      }
      List budgetLst = zcEbBaseServiceDelegate.queryDataForList("VwBudgetGp.getVwBudgetGpByBiNoLst", idbf.toString(), requestMeta);
      if (budgetLst == null) {
        return false;
      }
      for (int i = 0; i < budgetLst.size(); i++) {
        VwBudgetGp bg = (VwBudgetGp) budgetLst.get(i);
        if (!bean.getCoCode().equals(bg.getEnCode())) {
          return true;
        }
      }
    }
    return false;
  }

  /*
   * 退回
   */
  private void doUntread() {
    GkCommentUntreadDialog commentDialog = new GkCommentUntreadDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      ModalityType.APPLICATION_MODAL);
    if (commentDialog.cancel) {
      return;
    }
    boolean success = true;
    ZcPProBal afterSaveBill = null;
    String errorInfo = "";
    try {

      requestMeta.setFuncId(unTreadButton.getFuncId());
      ZcPProBal bean = (ZcPProBal) this.listCursor.getCurrentObject();
      bean.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      bean.setComment(commentDialog.getComment());
      afterSaveBill = this.listPanel.zcPProBalServiceDelegate.untreadFN(bean, requestMeta);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshAll(afterSaveBill, true);
      JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "退回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /*
   * 流程跟踪
   */
  private void doTrace() {
    ZcBaseBill bean = (ZcBaseBill) this.listCursor.getCurrentObject();
    if (bean == null) {
      return;
    }
    ZcUtil.showTraceDialog(bean, compoId);
  }

  /*
   * 收回
   */
  private void doCallback() {
    boolean success = true;
    ZcPProBal afterSaveBill = null;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(this.callbackButton.getFuncId());
      ZcPProBal ht = (ZcPProBal) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      afterSaveBill = listPanel.zcPProBalServiceDelegate.callbackFN(ht, requestMeta);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshAll(afterSaveBill, true);
      JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "收回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void doPrevious() {
    if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);
      if (num == JOptionPane.YES_OPTION) {
        if (!doSave()) {
          return;
        }
      } else {
        listCursor.setCurrentObject(oldZcPProBal);
      }
    }
    listCursor.previous();
    refreshData();
  }

  private void doPrint() {

    ZcPProBal zcPProBal = (ZcPProBal) this.listCursor.getCurrentObject();

    this.requestMeta.setFuncId(this.printButton.getFuncId());

    this.requestMeta.setPageType(this.compoId + "_L");

    try {

      String zcBalId = zcPProBal.getZcBalId();

      String condition = " t.ZC_BAL_ID='" + zcBalId + "'";

      //      PrintUtilities.print(compoId, "mainTable_L", compoId, "A4", "sa", "", com.ufgov.client.basis.util.WorkEnv.getInstance(), "", condition, null,
      //        null, false);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "打印预览出错！\n" + e.getMessage(), "错误", 0);

    }

  }

  private void doNext() {
    if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);
      if (num == JOptionPane.YES_OPTION) {
        if (!doSave()) {
          return;
        }
      } else {
        listCursor.setCurrentObject(oldZcPProBal);
      }
    }
    listCursor.next();
    refreshData();
  }

  public void doExit() {
    if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);
      if (num == JOptionPane.YES_OPTION) {
        if (!doSave()) {
          return;
        }
      }
    }
    this.parent.dispose();
  }

  private void refreshAll(ZcPProBal afterSaveBill, boolean isRefreshButton) {
    this.listCursor.setCurrentObject(afterSaveBill);
    refreshData();
    if (isRefreshButton) {
      setButtonStatus(afterSaveBill, requestMeta, this.listCursor);
    }
  }

  /**
   * 保存前校验
   * @param cpApply
   * @return
   */
  private boolean checkBeforeSave() {

    //如果是单位，则将资金莉的指标金额合计到主表中，方便后续支付时，进行判断，如果含有指标资金，则送业务科室审批 
    setBiSum();

    ZcPProBal zcPProBal = (ZcPProBal) this.listCursor.getCurrentObject();
    StringBuilder errorInfo = new StringBuilder();
    //    if (zcPProBal.getZcMakeCode() == null) {
    //      JOptionPane.showMessageDialog(this, "请选择项目！", "提示", JOptionPane.WARNING_MESSAGE);
    //      return true;
    //    }
    //    if (zcPProBal.getZcHtCode() == null) {
    //      JOptionPane.showMessageDialog(this, "请选择合同！", "提示", JOptionPane.WARNING_MESSAGE);
    //      return true;
    //    }
    List mainNotNullList = ((BillElementMeta) this.listPanel.getBillElementMeta()).getNotNullBillElement();
    List biNotNullList = self.biBillElementMeta.getNotNullBillElement();
    String mainValidateInfo = ZcUtil.validateBillElementNull(zcPProBal, mainNotNullList);
    String biValidateInfo = "";
    if (isYsdw()) {
      biValidateInfo = ZcUtil.validateDetailBillElementNull(zcPProBal.getBiList(), biNotNullList, false);
      String outLayIsLeaf = checkOutLay();
      if (outLayIsLeaf != null && outLayIsLeaf.trim().length() > 0) {
        biValidateInfo += "\n" + outLayIsLeaf;
      }

      //支付时，如果选择的指标的资金性质不是经费拨款，则进行收入教研，要计算这个预算单位的收入是否够当前支付的指标金额
      String shourouInfo = checkShouRu();
      if (shourouInfo != null && shourouInfo.trim().length() > 0) {
        biValidateInfo += "\n" + shourouInfo + "\n";
      }
    }

    String moneyCheck = checkMoeny(zcPProBal);
    if (mainValidateInfo.length() != 0) {
      errorInfo.append(LangTransMeta.translate(ZcPProBalConstants.TITLE_TRANS_ZC_P_PRO_MAKE)).append("：\n").append(mainValidateInfo.toString())
        .append("\n");
    }
    if (biValidateInfo.length() != 0) {
      errorInfo.append("资金构成：\n").append(biValidateInfo.toString()).append("\n");
    }
    if (moneyCheck.length() > 0) {
      errorInfo.append("资金核算：\n").append(moneyCheck).append("\n");
    }

    if (errorInfo.length() == 0) {

      //校验余额返还界面的银行账号，名称和账户
      if (zcPProBal.getReturnBiList() != null && zcPProBal.getReturnBiList().size() > 0) {
        if (zcPProBal.getZcCoBankName() == null) {
          errorInfo.append("采购单位资金结余返还银行名称不能为空").append(moneyCheck).append("\n");

        }
        if (zcPProBal.getZcCoAccName() == null) {
          errorInfo.append("采购单位资金结余返还银行账户不能为空").append(moneyCheck).append("\n");

        }
        if (zcPProBal.getZcCoAccCode() == null) {
          errorInfo.append("采购单位资金结余返还银行账号不能为空").append(moneyCheck).append("\n");
        }
      }
    }

    if (errorInfo.length() != 0) {
      JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);
      return true;
    }
    /*
     * 吉林地区使用的模式，这里注释掉
     * if (!caculateMoney()) {
      return true;
    }*/

    //检查资产入库情况
    if (checkFaCard()) {
      return true;
    }

    return false;
  }

  /**
   * 检查是否必须资产入库：
   * OPT_ZC_IS_LINK_FA      Y/N 是否检查资产入库，如果是，则最后一笔支付时，去资产库中检查：1）有没有这个合同的对应的卡片；2）合同金额是否和卡片的购买金额相等； 
   * OPT_ZC_HT_FA_EQUAL_SUM     Y/N 如果要求录入资产，则设置一个变量，对金额是否相等这个条件进行打开和关闭，正常情况是需要判断的，特殊情况，有管理员设置手工关闭这个值，让该支付审批通过，然后再打开。
   * 这个值，由于系统对option的值有缓存，所以读取该值时，不能用常用的方法，要直接从数据库来读取
   * 例如系统运维费用、会议费等，这些不进入资产，这类合同的支付，就必须关掉这个变量，不进行资产金额和合同金额的校验，可以进行支付
   * 补充：现在合同增加了一个字段is_zi_chan,用于记录这个合同是否入资产库，所以在资产入库的前提下，如果合同的这个字段不是Y，则该合同的支付不进行资产入库考察  --chenjl 20140615
   * @return true 检查没有通过; false 检查通过
   */
  private boolean checkFaCard() {
    // TODO Auto-generated method stub
    if (isYsdw()) {
      AsOption linkFaOpt = baseDataServiceDelegate.getAsOption("OPT_ZC_IS_LINK_FA", requestMeta);
      if (linkFaOpt == null || linkFaOpt.getOptVal().equalsIgnoreCase("N")) {
        return false;
      }
      AsOption htFaEqualOpt = baseDataServiceDelegate.getAsOption("OPT_ZC_HT_FA_EQUAL_SUM", requestMeta);
      boolean check = (htFaEqualOpt.getOptVal() == null || htFaEqualOpt.getOptVal().equalsIgnoreCase("N")) ? false : true;
      //如果不检查金额相等，直接返回
      if (!check) {
        return false;
      }
      ZcPProBal zcPProBal = (ZcPProBal) this.listCursor.getCurrentObject();
      if (zcPProBal.getZcHtCode() == null) {
        return false;
      }
      //获取合同中is_zi_chan字段值，看看是否入库
      ZcXmcgHt key = new ZcXmcgHt();
      key.setZcHtCode(zcPProBal.getZcHtCode());
      ZcXmcgHt ht = (ZcXmcgHt) zcEbBaseServiceDelegate.queryObject("ZC_XMCG_HT.ibatorgenerated_selectByPrimaryKey", key, requestMeta);
      if (!ZcXmcgHt.IS_ZI_CHAN_Y.equalsIgnoreCase(ht.getIsZiChan())) {
        return false;
      }

      //获取已经入资产库的合同金额
      Double cdsum = (Double) zcEbBaseServiceDelegate.queryObject("ZcFaCard.getFaCardHtSum", zcPProBal.getZcHtCode(), requestMeta);
      BigDecimal cardSum = cdsum == null ? new BigDecimal(0) : new BigDecimal(cdsum.doubleValue());
      BigDecimal htSum = zcPProBal.getZcHtNum() == null ? new BigDecimal(0) : zcPProBal.getZcHtNum();
      if (htSum.compareTo(cardSum) == 0 || htSum.compareTo(cardSum) == -1) {
        return false;
      }
      BigDecimal allPalSum = _getAllBalSum();
      //合同金额等于所有结算金额，即是最后一次支付了，同时合同金额小于资产金额，则提示将资产入库
      if (htSum.compareTo(allPalSum) == 0 && htSum.compareTo(cardSum) == 1) {
        JOptionPane.showMessageDialog(this, "最后一次支付前，请将采购物品纳入资产库。\n注意:如果是不需要纳入资产库的采购，请联系系统管理员进行处理!", "提示", JOptionPane.WARNING_MESSAGE);
        return true;
      }
    }
    return false;
  }

  /**
   * 获取全部已经结算的金额，包括当前单据的结算金额
   * @return
   */
  private BigDecimal _getAllBalSum() {
    // TODO Auto-generated method stub
    ZcPProBal zcPProBal = (ZcPProBal) this.listCursor.getCurrentObject();
    if (zcPProBal.getBiList() == null) {
      return new BigDecimal(0);
    }
    BigDecimal rtn = new BigDecimal(0);
    for (int i = 0; i < zcPProBal.getBiList().size(); i++) {
      ZcPProBalBi bi = (ZcPProBalBi) zcPProBal.getBiList().get(i);
      rtn = rtn.add(bi.getZcBiYjjsSum() == null ? new BigDecimal(0) : bi.getZcBiYjjsSum());
      rtn = rtn.add(bi.getZcBiBcjsSum() == null ? new BigDecimal(0) : bi.getZcBiBcjsSum());
    }
    return rtn;
  }

  /**
   * 
   * 如果是受控指标（如预算外指标），则必须校验系统的收入，如果收入的额度够，才可以支付，如果不够，则不能支付
   * @return
   */
  private String checkShouRu() {
    // TODO Auto-generated method stub
    if ("Y".equalsIgnoreCase(AsOptionMeta.getOptVal("OPT_ZC_SHOURU "))) {//是否进行受控指标检查
      //获取不受收入控制的资金性质
      String zjxzStr = AsOptionMeta.getOptVal("OPT_ZC_NO_SHOURU_ZJXZ");
      zjxzStr = zjxzStr == null ? "" : zjxzStr;
      String[] zjxzArray = zjxzStr.split(",");

      StringBuffer biStrs = new StringBuffer();
      StringBuffer error = new StringBuffer();
      BigDecimal zhifu = BigDecimal.ZERO;
      ZcPProBal bal = (ZcPProBal) this.listCursor.getCurrentObject();
      for (int i = 0; i < bal.getBiList().size(); i++) {
        ZcPProBalBi bi = (ZcPProBalBi) bal.getBiList().get(i);
        boolean flag = false;
        if (bi.getZcBiNo() != null && !bi.getZcBiNo().startsWith(ZcSettingConstants.No_BI)) {//自筹资金不需要检查经济分类是否末级

          for (String zjxzCode : zjxzArray) {
            if (zjxzCode.equals(bi.getZcFundCode())) {
              flag = true;//这条指标不属于收入控制的范围
            }
          }
          if (!flag) {//这条指标属于收入控制的范围
            zhifu = zhifu.add(bi.getZcBiBcjsSum() == null ? new BigDecimal(0) : bi.getZcBiBcjsSum());
            biStrs.append(bi.getZcBiNo()).append(",");
          }
        }
      }
      if (zhifu.doubleValue() > 0) {
        //获取单位的收入情况
        ElementConditionDto dto = new ElementConditionDto();
        dto.setNd(requestMeta.getSvNd());
        dto.setCoCode(requestMeta.getSvCoCode());
        BigDecimal shouru = (BigDecimal) zcEbBaseServiceDelegate.queryObject("VwBudgetGp.selectShouRou", dto, requestMeta);
        shouru = shouru == null ? new BigDecimal(0) : shouru;
        if (zhifu.compareTo(shouru) == 1) {
          error.append("您单位的当前可用收入数为：").append(shouru.doubleValue()).append("\n");
          error.append("当前受收入控制的指标支付金额为：").append(zhifu.doubleValue()).append("\n");
          error.append("指标编号：").append(biStrs).append("\n");
          error.append("请暂停支付当前单据或重新选择不受收入控制的指标进行支付").append("\n");
        }
      }
      if (error.length() > 0) {
        return error.toString();
      }
    }
    return null;
  }

  /**
   * 检查经济分类是否末级
   * @return
   */
  private String checkOutLay() {
    // TODO Auto-generated method stub
    boolean check = "Y".equalsIgnoreCase(AsOptionMeta.getOptVal("OPT_ZC_CHECK_OUT_LAY_IS_LEAF"));

    if (check) {
      String rtn = "";
      ZcPProBal zcPProBal = (ZcPProBal) this.listCursor.getCurrentObject();
      for (int i = 0; i < zcPProBal.getBiList().size(); i++) {
        ZcPProBalBi bi = (ZcPProBalBi) zcPProBal.getBiList().get(i);
        if (bi.getZcBiNo() != null && !bi.getZcBiNo().startsWith(ZcSettingConstants.No_BI)
          && (bi.getZcBiBcjsSum() != null && bi.getZcBiBcjsSum().doubleValue() > 0)) {//自筹资金不需要检查经济分类是否末级,本次不纳入支付的指标不检查
          if (bi.getOutLayIsLeaf() == null || ("0".equalsIgnoreCase(bi.getOutLayIsLeaf()) || "N".equalsIgnoreCase(bi.getOutLayIsLeaf()))) {
            rtn += bi.getZcBiNo() + " ";
          }
        }
      }
      if (rtn.trim().length() > 0) {
        rtn = "以下指标支付时，需选择末级经济科目:\n" + rtn.trim();
        return rtn;
      }
    }

    return null;
  }

  private String checkMoeny(ZcPProBal zcPProBal) {
    if (isGys()) {
      return checkMoneyForGys(zcPProBal);
    }
    return checkMoneyForYsDw(zcPProBal);
  }

  private String checkMoneyForGys(ZcPProBal zcPProBal) {
    // TODO Auto-generated method stub
    String info = "";
    double zcBalSum = 0.0;
    if (zcPProBal.getZcBalSum() != null)
      zcBalSum = zcPProBal.getZcBalSum().doubleValue();
    //合同金额
    double htMoney = 0.0;
    if (zcPProBal.getZcHtNum() != null)
      htMoney = zcPProBal.getZcHtNum().doubleValue();
    Map map = new HashMap();
    map.put("zcMakeCode", zcPProBal.getZcMakeCode());
    map.put("zcHtCode", zcPProBal.getZcHtCode());
    map.put("balId", zcPProBal.getZcBalId());
    BigDecimal yjjsMoney = BigDecimal.ZERO;
    yjjsMoney = zcPProBalServiceDelegate.getSumZcBalSum(map, requestMeta);
    if (null == yjjsMoney && zcBalSum > htMoney) {
      info += "【结算金额】应该小于或等于【合同金额】。\n";
    }
    if (null != yjjsMoney && zcBalSum > (htMoney - yjjsMoney.doubleValue())) {
      info += "合同未支付金额为" + (htMoney - yjjsMoney.doubleValue()) + "\n【结算金额】应该小于或等于未支付金额。\n";
    }
    return info;
  }

  private String checkMoneyForYsDw(ZcPProBal zcPProBal) {
    String info = "";
    //资金构成本次的所有结算金额。
    double sumMoney = 0.0;
    for (int i = 0; i < zcPProBal.getBiList().size(); i++) {
      //每笔资金构成中的 ：合同使用金额<=本次结算金额+已经结算金额
      ZcPProBalBi bi = (ZcPProBalBi) zcPProBal.getBiList().get(i);
      /*
       * 合同使用金额
       */
      double htsyMoney = 0.0;
      /*
       * 本次结算金额
       */
      double currBjcsMoney = 0.0;
      /*
       * 该资金的已经结算金额
       */
      double biyjsjMoney = bi.getZcBiYjjsSum().doubleValue();
      if (bi.getZcBiBcsySum() != null && bi.getZcBiBcjsSum() != null) {
        htsyMoney = bi.getZcBiBcsySum().doubleValue();//合同使用金额
        currBjcsMoney = bi.getZcBiBcjsSum().doubleValue();
        if (!(htsyMoney >= (biyjsjMoney + currBjcsMoney))) {
          info += "【结算金额】不应大于【合同使用金额】-【已经结算的金额】\n";
        }
      }

      if (bi.getZcBiBcjsSum() != null) {
        currBjcsMoney = bi.getZcBiBcjsSum().doubleValue();
        sumMoney += currBjcsMoney;
      }
    }

    //合同金额
    double htMoney = 0.0;
    if (zcPProBal.getZcHtNum() != null)
      htMoney = zcPProBal.getZcHtNum().doubleValue();

    //结算金额
    double zcBalSum = 0.0;
    if (zcPProBal.getZcBalSum() != null)
      zcBalSum = zcPProBal.getZcBalSum().doubleValue();
    //要验证本次结算金额要等于所有资金构成中的结算资金之和。
    if (zcBalSum != sumMoney) {
      info += "【资金构成】中结算金额之和应该等于【结算金额】。\n";
    }
    //获得该项目该合同已经结算的金额。
    Map map = new HashMap();
    map.put("zcMakeCode", zcPProBal.getZcMakeCode());
    map.put("zcHtCode", zcPProBal.getZcHtCode());
    map.put("balId", zcPProBal.getZcBalId());
    BigDecimal yjjsMoney = BigDecimal.ZERO;
    yjjsMoney = zcPProBalServiceDelegate.getSumZcBalSum(map, requestMeta);
    if (null == yjjsMoney && zcBalSum > htMoney) {
      info += "【结算金额】应该小于或等于【合同金额】。\n";
    }
    if (null != yjjsMoney && zcBalSum > (htMoney - yjjsMoney.doubleValue())) {
      info += "合同未支付金额为" + (htMoney - yjjsMoney.doubleValue()) + "\n【结算金额】应该<=未支付金额。\n";
    }
    yjjsMoney = yjjsMoney == null ? new BigDecimal(0) : yjjsMoney;
    if (yjjsMoney.doubleValue() + zcBalSum == htMoney) {
      zcPProBal.setIsLastPay("Y");
    }
    return info;
  }

  public void doEdit() {

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

    updateFieldEditorsEditable();

    setButtonStatus();
  }

  @Override
  protected void updateFieldEditorsEditable() {

    ZcPProBal bal = (ZcPProBal) listCursor.getCurrentObject();

    Long processInstId = bal.getProcessInstId();

    if (processInstId != null && processInstId.longValue() > 0) {

      // 工作流的单据

      wfCanEditFieldMap = BillElementMeta.getWfCanEditField(bal, requestMeta);

      if ("cancel".equals(this.oldZcPProBal.getZcBalStatus())) {// 撤销单据设置字段为不可编辑

        wfCanEditFieldMap = null;

      }

      for (AbstractFieldEditor editor : fieldEditors) {

        // 工作流中定义可编辑的字段

        if (wfCanEditFieldMap != null && wfCanEditFieldMap.containsKey(Utils.getDBColNameByFieldName(editor.getEditObject(), editor.getFieldName()))) {

          isEdit = true;
          this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

          editor.setEnabled(true);

        } else {

          editor.setEnabled(false);

        }

      }
      //工作流中该节点选中了保存按钮可用，则当前状态当前人可用编辑
      if (saveButton.isVisible() && saveButton.isEnabled()) {
        isEdit = true;
        this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
      }
    } else {
      for (AbstractFieldEditor editor : fieldEditors) {
        if (pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT) || pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {
          if ("zcHtCode".equals(editor.getFieldName()) || "zcBalSum".equals(editor.getFieldName()) || "zcRemark".equals(editor.getFieldName())) {
            editor.setEnabled(true);
          } else if (isYsdw() && isCar() && ("zcSuBankName".equals(editor.getFieldName()) || "zcSuAccCode".equals(editor.getFieldName()))) {
            //丹徒区车辆采购的支付由预算单位发起，需要录入银行账号信息
            editor.setEnabled(true);
          } else {
            editor.setEnabled(false);
          }
          isEdit = true;
        } else {
          editor.setEnabled(false);
        }
      }
    }

    //    setWFSubTableEditable(biTablePanel, isEdit);
    biTablePanel.getTable().setEnabled(isEdit);
  }

  private boolean isCar() {
    // TODO Auto-generated method stub
    if (zcPProMake != null && "Y".equalsIgnoreCase(zcPProMake.getIsCar())) {
      return true;
    }
    ZcPProBal bal = (ZcPProBal) listCursor.getCurrentObject();
    if (bal.getHt() == null || bal.getHt().getZcMakeCode() == null) {
      return false;
    }

    IZcPProMakeServiceDelegate zcPProMakeServiceDelegate = (IZcPProMakeServiceDelegate) ServiceFactory.create(IZcPProMakeServiceDelegate.class,
      "zcPProMakeServiceDelegate");
    zcPProMake = zcPProMakeServiceDelegate.selectByPrimaryKey(bal.getHt().getZcMakeCode(), this.requestMeta);
    if (zcPProMake != null && "Y".equalsIgnoreCase(zcPProMake.getIsCar())) {
      return true;
    }
    return false;
  }

  public boolean doSave() {
    if (checkBeforeSave()) {
      return false;
    }
    if (!isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return true;
    }
    boolean success = true;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(saveButton.getFuncId());
      ZcPProBal inData = (ZcPProBal) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
      ZcPProBal zcPProBal = this.listPanel.zcPProBalServiceDelegate.updateZcPProBalFN(inData, requestMeta);
      listCursor.setCurrentObject(zcPProBal);
      this.setEditingObject(zcPProBal);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshData();
      saveButton.setEnabled(false);
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
    // 根据工作流模版设置字段是否可编辑
    updateWFEditorEditable((ZcPProBal) this.listCursor.getCurrentObject(), requestMeta);
    return true;
  }

  protected void doDelete() {
    requestMeta.setFuncId(deleteButton.getFuncId());
    ZcPProBal zcPProBal = (ZcPProBal) this.listCursor.getCurrentObject();
    if (zcPProBal.getZcBalId() == null || "".equalsIgnoreCase(zcPProBal.getZcBalId())) {
      JOptionPane.showMessageDialog(this, "尚未保存到数据库，无需删除！", "提示", JOptionPane.ERROR_MESSAGE);
      return;
    }
    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      boolean success = true;
      String errorInfo = "";
      try {
        requestMeta.setFuncId(deleteButton.getFuncId());
        //        if (!"0".equals(zcPProBal.getZcBalStatus())) {
        //          JOptionPane.showMessageDialog(this, "非编辑状态单据，不可以删除！", "提示", JOptionPane.ERROR_MESSAGE);
        //          return;
        //        }
        this.listPanel.zcPProBalServiceDelegate.deleteZcPProBalFN(zcPProBal, requestMeta);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        success = false;
        errorInfo += e.getMessage();
      }
      if (success) {
        this.listCursor.removeCurrentObject();
        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        this.refreshData();
        this.listPanel.refreshCurrentTabData();
      } else {
        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void stopTableEditing() {
    JPageableFixedTable biTable = this.biTablePanel.getTable();
    if (biTable.isEditing()) {
      biTable.getCellEditor().stopCellEditing();
    }
  }

  public boolean isDataChanged() {
    stopTableEditing();
    if (!this.saveButton.isVisible() || !this.saveButton.isEnabled()) {
      return false;
    }
    return !DigestUtil.digest(oldZcPProBal).equals(DigestUtil.digest(listCursor.getCurrentObject()));
  }

  private void refreshData() {
    ZcPProBal zcPProBal = (ZcPProBal) listCursor.getCurrentObject();
    if (zcPProBal != null && !"".equals(ZcUtil.safeString(zcPProBal.getZcBalId()))) {
      //      zcMakeElementCondtiontDto.setZcMakeCode(zcPProBal.getZcMakeCode());
      String isUseBi = AsOptionMeta.getOptVal(ZcSettingConstants.OPT_ZC_USE_BUDGET_INTERFACE);
      zcPProBal = this.listPanel.zcPProBalServiceDelegate.selectByPrimaryKey(zcPProBal.getZcBalId(), isUseBi, this.requestMeta);
      zcPProBal.setHt(zcXmcgHtServiceDelegate.selectByPrimaryKey(zcPProBal.getZcHtCode(), this.requestMeta));
      /* 放在后台取了，方便计算digist值
       //获取当前合同未支付金额：合同金额-所有支付申请金额之和(不含cancel状态的金额)
       if(zcPProBal.getZcHtCode()!=null){
         Double canPaySum=(Double)zcEbBaseServiceDelegate.queryObject("ZC_P_PRO_BAL.getCanPayHtSumByHtCode", zcPProBal.getZcHtCode(), requestMeta);
         zcPProBal.setCanPaySum(new BigDecimal(canPaySum==null?0:canPaySum));
       }*/
      //供应商填报了支付申请，预算单位进行资金填充操作
      if ((zcPProBal.getBiList() == null || zcPProBal.getBiList().size() == 0) && isYsdw()) {
        getBiLst(zcPProBal);
      }
      for (int i = 0; i < zcPProBal.getBiList().size(); i++) {
        ZcPProBalBi bi = (ZcPProBalBi) zcPProBal.getBiList().get(i);
        if (bi.getZcBiYjjsSum() == null) {
          /*
           * 已经结算的金额，
           */
          Map map = new HashMap();
          map.put("zcHtBiNo", bi.getZcHtBiNo());
          map.put("zcHtCode", zcPProBal.getZcHtCode());
          if (zcPProBal != null && zcPProBal.getZcBalId() != null) {
            map.put("zcBalId", zcPProBal.getZcBalId());
          }
          /*
           * 获得该资金的已经结算金额
           */
          BigDecimal yjjsMoney = zcPProBalServiceDelegate.getSumZcBalBiSum(map, requestMeta);

          if (yjjsMoney != null) {
            bi.setZcBiYjjsSum(yjjsMoney);
          } else {
            bi.setZcBiYjjsSum(BigDecimal.ZERO);
          }
        }
        if (bi.getZcBiBal() == null && bi.getZcBiNo() != null) {
          VwBudgetGp vp = (VwBudgetGp) zcEbBaseServiceDelegate.queryObject("VwBudgetGp.getVwBudgetGpById", bi.getZcBiNo(), requestMeta);

          if (vp != null && vp.getCanuseMoney() != null) {
            bi.setZcBiBal(vp.getCanuseMoney());//指标余额
            bi.setSendDocName(vp.getFileName());
          }
        }
      }

      if (zcPProBal.getPayOrder() != null && !"".equals(zcPProBal.getPayOrder())) {
        this.isHtPrePay = true;
      }

      listCursor.setCurrentObject(zcPProBal);
      pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
      /* 
       if (zcPProBal.getReturnBiList() != null && zcPProBal.getReturnBiList().size() > 0) {
         if (tabPane.getComponentCount() == 1) {
           createBiReturnPanel(zcPProBal);
           returnBiPanel.refreshSubData(zcPProBal.getReturnBiList());
           tabPane.add("单位余款返回信息", returnBiPanel);
         }
       }*/

    } else {
      if (zcPProBal == null) {
        zcPProBal = new ZcPProBal();
        pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
      }
      zcPProBal.setZcBalStatus("0");
      //      zcPProBal.setCoCode(this.requestMeta.getSvCoCode());
      zcPProBal.setZcInputCode(this.requestMeta.getSvUserID());
      zcPProBal.setZcInputName(this.requestMeta.getSvUserName());
      zcPProBal.setZcInputDate(this.requestMeta.getSysDate());
      //      zcPProBal.setOrgCode(this.requestMeta.getSvOrgCode());
      zcPProBal.setNd(this.requestMeta.getSvNd());
      zcPProBal.setZcBalSum(BigDecimal.ZERO);
      if (zcPProBal.getBiList() == null) {
        zcPProBal.setBiList(new ArrayList());
      }
      listCursor.getDataList().add(zcPProBal);
    }
    this.setEditingObject(zcPProBal);

    refreshSubData(zcPProBal.getBiList());

    setOldObject();

    setButtonStatus();

    updateFieldEditorsEditable();

  }

  private void getBiLst(ZcPProBal zcPProBal) {
    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) zcXmcgHtServiceDelegate.selectByPrimaryKey(zcPProBal.getZcHtCode(), requestMeta);
    zcPProBal.setBiList(buildZcPProBalBi(zcXmcgHt));
  }

  public void setEditingObject(WfAware editingObject) {

    super.setEditingObject(editingObject);
    if (returnBiPanel != null) {
      returnBiPanel.setEditingObject((ZcPProBal) editingObject);
    }
  }

  /**
  * @Description: TODO(这里用一句话描述这个方法的作用)
  * @return void 返回类型
  * @since 1.0
  */
  private void refreshSubData(List biList) {
    biTablePanel.setTableModel(ZcPProBalToTableModelConverter.convertSubBiTableData(biList));
    ZcUtil.translateColName(biTablePanel.getTable(), ZcPProBalToTableModelConverter.getBiInfo());
    setOldObject();
    setTableEditor(biTablePanel.getTable());
    addBiTableLisenter(biTablePanel.getTable());
    biTablePanel.repaint();
  }

  private void setTableEditor(JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    //支付方式
    //    SwingUtil.setTableCellEditor(table, ZcPProBalConstants.FIELD_TRANS_PAYTYPE_CODE, new AsValComboBoxCellEditor("ZC_VS_PAYTYPE_NAME"));
    //    SwingUtil.setTableCellRenderer(table, ZcPProBalConstants.FIELD_TRANS_PAYTYPE_CODE, new AsValCellRenderer("ZC_VS_PAYTYPE_NAME"));
    //资金性质
    //    SwingUtil.setTableCellEditor(table, ZcPProBalConstants.FIELD_TRANS_FUND_CODE, new AsValComboBoxCellEditor("ZC_VS_FUND_NAME"));
    //    SwingUtil.setTableCellRenderer(table, ZcPProBalConstants.FIELD_TRANS_FUND_CODE, new AsValCellRenderer("ZC_VS_FUND_NAME"));
    //指标来源
    //    SwingUtil.setTableCellEditor(table, ZcPProBalConstants.FIELD_TRANS_ORIGIN_CODE, new AsValComboBoxCellEditor("ZC_VS_ORIGIN_NAME"));
    //    SwingUtil.setTableCellRenderer(table, ZcPProBalConstants.FIELD_TRANS_ORIGIN_CODE, new AsValCellRenderer("ZC_VS_ORIGIN_NAME"));
    SwingUtil.setTableCellEditor(table, ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_BI_BCJS_SUM, new MoneyCellEditor());
    SwingUtil.setTableCellRenderer(table, ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_BI_BCJS_SUM, new NumberCellRenderer());

    //    SwingUtil.setTableCellEditor(table, ZcPProBalConstants.FIELD_TRANS_PAY_STATUS, new AsValComboBoxCellEditor("ZC_VS_PAY_STATUS"));
    //    SwingUtil.setTableCellRenderer(table, ZcPProBalConstants.FIELD_TRANS_PAY_STATUS, new AsValCellRenderer("ZC_VS_PAY_STATUS"));

    eaccDto.setNd(requestMeta.getSvNd());

    String colNames[] = { "代码", "名称" };
    ZcEaccHandler budgetHandler = new ZcEaccHandler(colNames);

    ForeignEntityFieldCellEditor suEditor = new ForeignEntityFieldCellEditor("EAcc.getEAccLstForPayDialog", eaccDto, 20, budgetHandler, colNames,
      "经济分类", "name");
    SwingUtil.setTableCellEditor(table, ZcPProBalConstants.FIELD_TRANS_OUTLAY_NAME, suEditor);

  }

  private void setOldObject() {
    oldZcPProBal = (ZcPProBal) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  private void doSendGk() {
    ZcPProBal zcPProBal = (ZcPProBal) listCursor.getCurrentObject();

    //    if(!ZcSettingConstants.BILL_STATUS_AUDITED.equals(zcPProBal.getZcBalStatus())){
    //      JOptionPane.showMessageDialog(this, "审批结束后才可以提交国库！\n", "错误", JOptionPane.ERROR_MESSAGE);
    //      return;
    //    }

    StringBuffer sb = new StringBuffer();
    sb.append("本次支付供应商账户").append(zcPProBal.getZcSuAccName()).append("的金额如下：\n");
    for (ZcPProBalBi bi : (List<ZcPProBalBi>) zcPProBal.getBiList()) {
      if (bi.getZcBiNo() != null && !bi.getZcBiNo().startsWith(ZcSettingConstants.No_BI)//以NoBi_开头的数据是自筹资金
        && bi.getZcBiNo().length() > 0 && bi.getZcBiBcjsSum() != null && bi.getZcBiBcjsSum().longValue() > 0) {
        sb.append("指标文号为").append(bi.getSendDocName()).append(" 付款金额为").append(bi.getZcBiBcjsSum().toString()).append("元\n");
      }
    }
    /*
    if (zcPProBal.getReturnBiList() != null && zcPProBal.getReturnBiList().size() > 0) {
      sb.append("返还采购单位账户").append(zcPProBal.getZcCoAccName()).append("的金额如下：\n");

      for (ZcPProReturnBi bi : (List<ZcPProReturnBi>) zcPProBal.getReturnBiList()) {
        if (bi.getZcBiNo() != null && bi.getZcBiNo().length() > 0 && bi.getZcBiBal() != null && bi.getZcBiBal().longValue() > 0) {
          sb.append("指标文号为").append(bi.getSendDocName()).append(" 付款金额为").append(bi.getZcBiBal().toString()).append("元\n");
        }
      }
    }
    */
    sb.append("确定要提交国库吗？");

    int i = JOptionPane.showConfirmDialog(this, sb.toString(), "提示", JOptionPane.YES_NO_OPTION);
    if (i != 0) {
      return;
    }

    String isUseBi = AsOptionMeta.getOptVal(ZcSettingConstants.OPT_ZC_USE_BUDGET_INTERFACE);

    boolean issuccess = false;
    String msg = "";
    try {
      zcPProBal.setComment("同意");
      zcPProBal.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      issuccess = zcPProBalServiceDelegate.sendPayFN(zcPProBal, WorkEnv.getInstance().getWebRoot(), "N", requestMeta);
    } catch (Exception e) {
      msg = e.getMessage();
      logger.error(e.getMessage(), e);
    }
    if (issuccess) {
      JOptionPane.showMessageDialog(this, "提交结算申请成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshData();
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "提交结算申请失败！\n" + msg, "错误", JOptionPane.ERROR_MESSAGE);

    }
  }

  private void editPayVoucher() {
    ZcPProBal zcPProBal = (ZcPProBal) listCursor.getCurrentObject();
    boolean issuccess = false;
    try {
      issuccess = zcPProBalServiceDelegate.editPayFN(zcPProBal, WorkEnv.getInstance().getWebRoot(), requestMeta);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    if (issuccess) {
      zcPProBal.setZcBalStatus("sendGk");
      zcPProBalServiceDelegate.updateZcPProBalFN(zcPProBal, requestMeta);
      JOptionPane.showMessageDialog(this, "变更结算申请成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshData();
    } else {
      JOptionPane.showMessageDialog(this, "变更结算申请失败！", "错误", JOptionPane.ERROR_MESSAGE);

    }
  }

  /**

   * 返回当前用户是否供应商

   * @return

   */

  public boolean isGys() {

    if (WorkEnv.getInstance().containRole(ZcSettingConstants.ROLE_GYS_HUIYUAN)
      || WorkEnv.getInstance().containRole(ZcSettingConstants.ROLE_GYS_NORMAL)) {
      return true;
    }
    return false;

  }

  /**

   * 返回当前用户是否预算单位

   * @return

   */

  public boolean isYsdw() {
    if (ZcUtil.isYsdw()) {
      return true;
    }
    if (ZcUtil.isCgdb()) {
      return true;
    }
    return false;

  }

  protected void setButtonStatus() {
    ZcPProBal bal = (ZcPProBal) listCursor.getCurrentObject();
    if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equals(bal.getZcBalStatus())) {
      setCancelStatus(listCursor);
    } else {
      setButtonStatus(bal, requestMeta, this.listCursor);
    }
  }

  public void setButtonStatusWithoutWf() {

    if (this.btnStatusList.size() == 0) {

      ButtonStatus bs = new ButtonStatus();

      //      bs.setButton(this.addButton);
      //
      //      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      //
      //      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
      //
      //      btnStatusList.add(bs);

      bs.setButton(this.editButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.saveButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.exitButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_ALL);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.sendButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.suggestPassButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.auditPassButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.callbackButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.unAuditButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.unTreadButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      bs = new ButtonStatus();

      bs.setButton(this.printButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_AUDITED);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.sendGkButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_AUDITED);

      btnStatusList.add(bs);
    }
    ZcPProBal obj = (ZcPProBal) this.listCursor.getCurrentObject();

    String billStatus = obj.getZcBalStatus();

    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, this.compoId, obj.getProcessInstId());

    //    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE) && "exec".equals(billStatus)) {
    //      sendGkButton.setVisible(true);
    //    } else {
    //      sendGkButton.setVisible(false);
    //    }
  }

  private void createBiReturnPanel(ZcPProBal zcPProBal) {
    if (returnBiPanel == null) {
      returnBiPanel = new ZcPProBalReturnBiPanel(zcPProBal);
      returnBiPanel.setEditingObject(zcPProBal);
    }
  }

  //  protected void setButtonStatus(WfAware baseBill, RequestMeta requestMeta,
  //
  //  ListCursor listCursor) {
  //    super.setButtonStatus(baseBill, requestMeta, listCursor);
  //    if (baseBill != null) {
  //      ZcPProBal bal = (ZcPProBal) baseBill;
  //      if (!"exec".equals(bal.getZcBalStatus())) {
  //        sendGkButton.setVisible(true);
  //      } else {
  //        sendGkButton.setVisible(false);
  //      }
  //    }
  //  }

  /**
   * //用于代编代报里，置换requestmeta里的信息
   */
  private void resetRequestMeta() {
    // TODO Auto-generated method stub

    ZcPProBal ht = (ZcPProBal) listCursor.getCurrentObject();
    if (ht.getCoCode() != null && ht.getZcBalId() != null && !ht.getZcBalId().equals("自动编号")) {
      RequestMeta meta = (RequestMeta) ObjectUtil.deepCopy(this.requestMeta);
      ElementConditionDto dto = new ElementConditionDto();
      dto.setZcText0(requestMeta.getSvUserID());
      dto.setZcText1(ht.getCoCode());
      dto.setNd(requestMeta.getSvNd());
      ZcEbDaibian daibian = (ZcEbDaibian) zcEbBaseServiceDelegate.queryObject("ZcEbDaiBian.getCompanyByDaibian", dto, meta);
      if (daibian != null) {
        meta.setSvUserID(daibian.getCoUser());
        meta.setSvUserName(daibian.getEmpName());
        meta.setSvCoCode(daibian.getCoCode());
        meta.setSvCoName(daibian.getCoName());
        meta.setEmpCode(daibian.getCoUser());
        meta.setEmpName(daibian.getEmpName());
        meta.setSvPoCode(daibian.getPosiCode());
        meta.setSvOrgPoCode(daibian.getOrgPosiCode());
        requestMeta = meta;
      }
    }
  }
}
