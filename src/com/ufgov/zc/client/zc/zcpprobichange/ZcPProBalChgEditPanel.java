package com.ufgov.zc.client.zc.zcpprobichange;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_FUND_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ORIGIN_CODE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.ZcWorkFlowAdapter;
import com.ufgov.zc.client.common.converter.zc.ZcPProBalChgToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcPProMakeToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AuditPassButton;
import com.ufgov.zc.client.component.button.CallbackButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SaveSendButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
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
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.budget.model.VwBudgetGp;
import com.ufgov.zc.common.commonbiz.model.Company;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalChgConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcPProBalChg;
import com.ufgov.zc.common.zc.model.ZcPProMitemBiChg;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcTBchtItem;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcPProBalChgServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcXmcgHtServiceDelegate;

/**
* @ClassName: ZcPProBalChgEditPanel
* @Description: TODO(这里用一句话描述这个类的作用)
* @date: 2010-8-2 下午06:42:45
* @version: V1.0 
* @since: 1.0
* @author: Administrator
* @modify: 
*/
public class ZcPProBalChgEditPanel extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZcPProBalChgEditPanel.class);

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_P_PRO_BAL_CHG";

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

  private FuncButton exitButton = new ExitButton();

  private FuncButton saveAndSendButton = new SaveSendButton();

  private JButton addBtn = new SubaddButton(false);

  private JButton insertBtn = new SubinsertButton(false);

  private JButton delBtn = new SubdelButton(false);

  private BillElementMeta biBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_P_PRO_BAL_BI");

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  private ListCursor listCursor;

  private ZcPProBalChg oldZcPProBalChg;

  protected ElementConditionDto getDto = new ElementConditionDto();

  protected boolean budgetFlag =ZcUtil.useBudget();

  private ZcPProBalChgListPanel listPanel;

  /**
   *各种面板
   */
  private JTablePanel biTablePanel;

  private JTablePanel oldBiTablePanel;

  private JSaveableSplitPane splitPane;

  JTabbedPane topPane = new JTabbedPane();

  JTabbedPane bottomPane = new JTabbedPane();

  private JTabbedPane htPanel = new JTabbedPane();

  private ZcPProBalChgEditPanel self = this;

  private GkBaseDialog parent;

  private ElementConditionDto htElementCondtiontDto = new ElementConditionDto();

  private ElementConditionDto zcMakeElementCondtiontDto = new ElementConditionDto();

  ForeignEntityFieldEditor zcMakeSelectEdit;

  private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private Map<String, BigDecimal> budgets = null;

  private final String NOT_BI = "notBi";

  public IZcPProBalChgServiceDelegate zcPProBalChgServiceDelegate = (IZcPProBalChgServiceDelegate) ServiceFactory.create(
    IZcPProBalChgServiceDelegate.class, "zcPProBalChgServiceDelegate");

  public IZcXmcgHtServiceDelegate zcXmcgHtServiceDelegate = (IZcXmcgHtServiceDelegate) ServiceFactory.create(IZcXmcgHtServiceDelegate.class,
    "zcXmcgHtServiceDelegate");

  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  public ZcPProBalChgEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcPProBalChgListPanel listPanel) {
    super(ZcPProBalChg.class, listPanel.getBillElementMeta());
    this.listCursor = listCursor;
    this.listPanel = listPanel;
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta
      .translate(ZcPProBalChgConstants.FIELD_TRANS_ZC_P_PRO_BAL_CHG_TITLE), TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15),
      Color.BLUE));
    this.parent = parent;
    this.colCount = 3;
    init();
    requestMeta.setCompoId(compoId);
    refreshData();
    setButtonStatus((ZcPProBalChg) listCursor.getCurrentObject(), requestMeta, this.listCursor);
  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();
    //单据编号
    AutoNumFieldEditor balChgId = new AutoNumFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_ID), "balChgId");

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

    //    AsValFieldEditor status = new AsValFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_STATUS), "status",
    //      "VS_ZC_P_PRO_BAL_CHG_STATUS");
    //    editorList.add(status);
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

    //    TextFieldEditor zcInputCode = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_CODE), "zcInputCode");
    //    editorList.add(zcInputCode);

    TextFieldEditor zcInputName = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_NAME), "zcInputName");

    TextFieldEditor zcMakeTel = new TextFieldEditor("录入人电话", "zcMakeTel");

    DateFieldEditor zcInputDate = new DateFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_DATE), "zcInputDate");

    //    editorList.add(zcInputDate);
    TextFieldEditor zcRemark = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_REMARK), "remark");

    //    editorList.add(balChgId);
    editorList.add(zcCoCode);
    editorList.add(zcMoneyBiSum);
    editorList.add(zcRemark);

    editorList.add(zcMakeName);
    editorList.add(zcInputName);
    //  editorList.add(zcCoCodeNd);
    editorList.add(zcInputDate);

    editorList.add(zcMakeSelectEdit);
    editorList.add(zcMakeTel);
    return editorList;
  }

  private void buildZcPProBalChgBi(List<ZcXmcgHtBi> biHTBiList, List<ZcPProMitemBiChg> biBalList, String htCode, ZcTBchtItem item) {

  }

  @Override
  public JComponent createSubBillPanel() {
    oldBiTablePanel = new JTablePanel();
    oldBiTablePanel.setPanelId(this.getClass().getName() + "oldBitable");
    oldBiTablePanel.init();
    oldBiTablePanel.getSearchBar().setVisible(false);
    //oldBiTablePanel不可编辑
    //    oldBiTablePanel.setTablePreferencesKey(this.getClass().getName() + "_oldBitable");
    oldBiTablePanel.getTable().setShowCheckedColumn(true);
    topPane.addTab("原采购计划资金构成", oldBiTablePanel);

    biTablePanel = new JTablePanel();
    biTablePanel.setPanelId(this.getClass().getName() + "bitable");
    biTablePanel.init();
    biTablePanel.getSearchBar().setVisible(false);
    //    biTablePanel.setTablePreferencesKey(this.getClass().gtName() + "_table");
    biTablePanel.getTable().setShowCheckedColumn(true);
    JFuncToolBar bottomToolBarPack = new JFuncToolBar();
    bottomToolBarPack.add(addBtn);
    bottomToolBarPack.add(insertBtn);
    bottomToolBarPack.add(delBtn);

    biTablePanel.add(bottomToolBarPack, BorderLayout.SOUTH);
    addBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 添加分包明细
        ZcPProMitemBiChg bi = new ZcPProMitemBiChg();
        int rowNum = addSub(biTablePanel, bi);
        //设置默认值
        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);
      }
    });
    insertBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 插入分包明细
        ZcPProMitemBiChg bi = new ZcPProMitemBiChg();
        int rowNum = insertSub(biTablePanel, bi);
        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);
      }
    });
    // 删除项目标段按钮的事件监听
    delBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        JPageableFixedTable table = biTablePanel.getTable();
        Integer[] selectedRows = table.getCheckedRows();
        BeanTableModel tableModel = (BeanTableModel) table.getModel();
        List dataList = tableModel.getDataBeanList();
        for (Integer checkedRow : selectedRows) {
          int accordDataRow = table.convertRowIndexToModel(checkedRow);
          ZcPProMitemBiChg bi = (ZcPProMitemBiChg) dataList.get(accordDataRow);
          if (bi.getZcBiYjjsSum() != null && bi.getZcBiYjjsSum().intValue() > 0) {
            JOptionPane.showMessageDialog(self, "该指标存在已经支付的金额，允许修改，不允许删除!", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
          }
        }
        Integer[] checkedRows = deleteBiSub(biTablePanel);
      }
    });

    topPane.addTab("变更后资金构成", biTablePanel);

    //变更原因

    JPanel changeReason = new JPanel(null);

    changeReason.setLayout(new BorderLayout());

    changeReason.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "变更原因*", TitledBorder.CENTER, TitledBorder.TOP,

    new Font("宋体", Font.BOLD, 12), new Color(154, 70, 100)));

    JTextArea changeReasonArea = new JTextArea();

    changeReasonArea.setBackground(Color.WHITE);

    changeReasonArea.setLineWrap(true);

    changeReasonArea.setEditable(true);

    JScrollPane scrollPane3 = new JScrollPane(changeReasonArea);
    scrollPane3.setPreferredSize(new Dimension(180, 200));

    changeReason.add(scrollPane3, BorderLayout.CENTER);
    //    topPane.addTab("变更原因", changeReason);

    //    topPane.setMinimumSize(new Dimension(240, 200));
    //    bottomPane.setMinimumSize(new Dimension(240, 350));
    splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, topPane, bottomPane);
    splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 200);
    splitPane.setContinuousLayout(true);
    splitPane.setOneTouchExpandable(true);
    // 只显示向下的箭头
    //      splitPane.putClientProperty("toExpand", true);
    splitPane.setDividerSize(5);
    splitPane.setDividerLocation(200);
    splitPane.setBackground(self.getBackground());
    return splitPane;
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
    toolBar.add(exitButton);

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

  }

  private void addBiTableLisenter(JPageableFixedTable table) {

  }

  private void doSend() {
    if (!checkBeforeSave()) {
      return;
    }

    if (isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    ZcPProBalChg bal = (ZcPProBalChg) this.listCursor.getCurrentObject();
    ZcBaseBill afterBill = ZcWorkFlowAdapter.newCommitFN(bal, this, sendButton, requestMeta, null);
    if (afterBill != null) {
      ZcPProBalChg afterSaveBill = zcPProBalChgServiceDelegate.selectByPrimaryKey(bal.getBalChgId(), requestMeta);
      this.refreshAll(afterSaveBill, true);
      this.listPanel.refreshCurrentTabData();
    }
  }

  /*
   * 填写意见审核
   */
  private void doSuggestPass() {
    if (!checkBeforeSave()) {
      return;
    }
    if (isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    ZcPProBalChg bal = (ZcPProBalChg) this.listCursor.getCurrentObject();
    ZcBaseBill afterBill = ZcWorkFlowAdapter.auditFN(bal, this, suggestPassButton, requestMeta, null);
    if (afterBill != null) {
      ZcPProBalChg afterSaveBill = zcPProBalChgServiceDelegate.selectByPrimaryKey(bal.getBalChgId(), requestMeta);
      this.refreshAll(afterSaveBill, true);
      this.listPanel.refreshCurrentTabData();
    }
  }

  private void doAudit() {
    ZcPProBalChg bal = (ZcPProBalChg) this.listCursor.getCurrentObject();
    ZcBaseBill afterBill = ZcWorkFlowAdapter.auditFN(bal, this, null, requestMeta, null);
    if (afterBill != null) {
      ZcPProBalChg afterSaveBill = zcPProBalChgServiceDelegate.selectByPrimaryKey(bal.getBalChgId(), requestMeta);
      this.refreshAll(afterSaveBill, true);
      this.listPanel.refreshCurrentTabData();
    }
  }

  /*
   * 退回
   */
  private void doUntread() {

    ZcPProBalChg bal = (ZcPProBalChg) this.listCursor.getCurrentObject();
    ZcBaseBill afterBill = ZcWorkFlowAdapter.untreadFN(bal, this, unTreadButton, requestMeta, null);
    if (afterBill != null) {
      ZcPProBalChg afterSaveBill = zcPProBalChgServiceDelegate.selectByPrimaryKey(bal.getBalChgId(), requestMeta);
      this.refreshAll(afterSaveBill, true);
      this.listPanel.refreshCurrentTabData();
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
    ZcPProBalChg bal = (ZcPProBalChg) this.listCursor.getCurrentObject();
    ZcBaseBill afterBill = ZcWorkFlowAdapter.callbackFN(bal, this, callbackButton, requestMeta, null);
    if (afterBill != null) {
      ZcPProBalChg afterSaveBill = zcPProBalChgServiceDelegate.selectByPrimaryKey(bal.getBalChgId(), requestMeta);
      this.refreshAll(afterSaveBill, true);
      this.listPanel.refreshCurrentTabData();
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
        listCursor.setCurrentObject(oldZcPProBalChg);
      }
    }
    listCursor.previous();
    refreshData();
  }

  private void doNext() {
    if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);
      if (num == JOptionPane.YES_OPTION) {
        if (!doSave()) {
          return;
        }
      } else {
        listCursor.setCurrentObject(oldZcPProBalChg);
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

  private void refreshAll(ZcPProBalChg afterSaveBill, boolean isRefreshButton) {
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
    ZcPProBalChg zcPProBalChg = (ZcPProBalChg) this.listCursor.getCurrentObject();

    //    zcPProBalChg.setExecutor(zcPProBalChg.getZcInputCode());
    if (zcPProBalChg.getZcMakeCode() == null) {
      JOptionPane.showMessageDialog(this, "请选择项目！", "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if (zcPProBalChg.getZcPProChgBiList() == null || zcPProBalChg.getZcPProChgBiList().size() == 0) {
      JOptionPane.showMessageDialog(this, "请填写变更后的资金构成！", "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    //校验变更后的资金的总金额是否等于预算的金额
    String str1 = checkBiMoney(zcPProBalChg);
    if (str1.length() != 0) {
      JOptionPane.showMessageDialog(this, str1, "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if (ZcPProBalChgConstants.ZC_P_PRO_BAL_STATUS_UPDATE_HT.equals(zcPProBalChg.getStatus())
      && requestMeta.getSvUserID().equals(zcPProBalChg.getZcInputCode())) {
      //校验合同的分配情况。合同的资金构成的总金额是否等于合同金额
      String str = checkHtMoney(zcPProBalChg);
      if (str.length() != 0) {
        JOptionPane.showMessageDialog(this, str, "提示", JOptionPane.WARNING_MESSAGE);
        return false;
      }
      if (budgets != null) {
        Iterator<BigDecimal> it = budgets.values().iterator();
        while (it.hasNext()) {
          if (it.next().compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(this, "合同中资金使用金额与变更后的计划资金构成预算金额不匹配", "提示", JOptionPane.WARNING_MESSAGE);
            return false;
          }
        }
      }
    }

    return true;
  }

  public void doEdit() {
    if (!ZcPProBalChgConstants.ZC_P_PRO_BAL_STATUS_UPDATE_HT.equals(((ZcPProBalChg) listCursor.getCurrentObject()).getStatus())) {
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
      updateFieldEditorsEditable();
      setButtonStatus();
    } else {
      for (int i = 0; i < htPanel.getComponentCount(); i++) {
        ((ZcHtChangePanel) htPanel.getComponentAt(i)).setEnedit(true);
      }
      this.saveButton.setEnabled(true);
      this.editButton.setEnabled(false);
    }
  }

  @Override
  protected void updateFieldEditorsEditable() {
    super.updateFieldEditors();
    ZcPProBalChg zcPProBalChg = (ZcPProBalChg) listCursor.getCurrentObject();

    BeanTableModel biTableModel = (BeanTableModel) biTablePanel.getTable().getModel();

    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {
      zcMakeSelectEdit.setEnabled(true);
      for (AbstractFieldEditor fd : this.fieldEditors) {
        if ("zcMakeCode".equals(fd.getFieldName()) || "remark".equals(fd.getFieldName())) {
          fd.setEnabled(true);
        } else {
          fd.setEnabled(false);
        }

      }
      biTableModel.setEditable(true);

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {
      zcMakeSelectEdit.setEnabled(false);
      for (AbstractFieldEditor fd : this.fieldEditors) {
        fd.setEnabled(false);
      }
      biTableModel.setEditable(false);

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {
      zcMakeSelectEdit.setEnabled(true);
      for (AbstractFieldEditor fd : this.fieldEditors) {
        if ("zcMakeCode".equals(fd.getFieldName()) || "remark".equals(fd.getFieldName())) {
          fd.setEnabled(true);
        } else {
          fd.setEnabled(false);
        }
      }
      if (ZcPProBalChgConstants.ZC_P_PRO_BAL_STATUS_UPDATE_HT.equals(zcPProBalChg.getStatus())
        && requestMeta.getSvUserID().equals(zcPProBalChg.getZcInputCode())) {
        biTableModel.setEditable(false);

      } else {
        biTableModel.setEditable(true);
      }
    }
  }

  public boolean doSave() {
    if (!checkBeforeSave()) {
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
      ZcPProBalChg inData = (ZcPProBalChg) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
      ZcPProBalChg zcPProBalChg = this.listPanel.zcPProBalChgServiceDelegate.updateZcPProBalChgFN(inData, WorkEnv.getInstance().getWebRoot(),
        budgetFlag, requestMeta);
      listCursor.setCurrentObject(zcPProBalChg);
      this.setEditingObject(zcPProBalChg);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshData();
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
    // 根据工作流模版设置字段是否可编辑
    return true;
  }

  protected void doDelete() {
    requestMeta.setFuncId(deleteButton.getFuncId());
    ZcPProBalChg zcPProBalChg = (ZcPProBalChg) this.listCursor.getCurrentObject();
    if (zcPProBalChg.getBalChgId() == null || "".equalsIgnoreCase(zcPProBalChg.getBalChgId())) {
      JOptionPane.showMessageDialog(this, "尚未保存到数据库，无需删除！", "提示", JOptionPane.ERROR_MESSAGE);
      return;
    }
    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      boolean success = true;
      String errorInfo = "";
      try {
        requestMeta.setFuncId(deleteButton.getFuncId());
        if (!"0".equals(zcPProBalChg.getStatus())) {
          JOptionPane.showMessageDialog(this, "非编辑状态单据，不可以删除！", "提示", JOptionPane.ERROR_MESSAGE);
          return;
        }
        this.listPanel.zcPProBalChgServiceDelegate.deleteZcPProBalChgFN(zcPProBalChg, WorkEnv.getInstance().getWebRoot(), budgetFlag, requestMeta);
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
        JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
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
    return !DigestUtil.digest(oldZcPProBalChg).equals(DigestUtil.digest(listCursor.getCurrentObject()));
  }

  private void refreshData() {
    getDto.setZcText2("1");
    if (budgetFlag) {
      getDto.setNd(requestMeta.getSvNd());
      getDto.setZcText2("1");
      getDto.setCoCode(requestMeta.getSvCoCode());
    }
    ZcPProBalChg zcPProBalChg = (ZcPProBalChg) listCursor.getCurrentObject();
    if (zcPProBalChg != null && !"".equals(ZcUtil.safeString(zcPProBalChg.getBalChgId()))) {
      pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
      if (zcPProBalChg.getZcPProChgBiList() == null) {
        List biList = zcEbBaseServiceDelegate
          .queryDataForList("ZC_P_PRO_BAL_CHG_BI.abatorgenerated_selectByPrimaryBalChg", zcPProBalChg, requestMeta);
        if (biList != null && biList.size() > 0) {
          if (budgetFlag) {
            String sumId = "";
            for (int i = 0; i < biList.size(); i++) {
              if (sumId.length() > 0) {
                sumId = sumId + ",'" + ((ZcPProMitemBi) biList.get(i)).getZcBiNo() + "'";
              } else {
                sumId = "'" + ((ZcPProMitemBi) biList.get(i)).getZcBiNo() + "'";
              }
            }
            getDto.setZcText3(sumId);
          }
        }
        zcPProBalChg.setZcPProChgBiList(biList);
      }
      refreshBiSubData(zcPProBalChg);
      if (zcPProBalChg.getOldBiList() == null) {

        List oldBiList = zcEbBaseServiceDelegate.queryDataForList("ZC_P_PRO_MITEM_BI_HISTORY.selectZcPProMitemBiHisByMakeCode",
          zcPProBalChg.getZcMakeCode(), requestMeta);
        if (oldBiList != null && oldBiList.size() > 0) {
          zcPProBalChg.setOldBiList(oldBiList);
        } else {

          ElementConditionDto dto = new ElementConditionDto();

          dto.setZcText0(zcPProBalChg.getZcMakeCode());
          List zcMakeList = zcEbBaseServiceDelegate.getForeignEntitySelectedData("ZC_P_PRO_MITEM_BI.getForeignBiDetail", dto, requestMeta);
          zcPProBalChg.setOldBiList(zcMakeList);

        }
      }
      zcMakeElementCondtiontDto.setZcMakeCode(zcPProBalChg.getZcMakeCode());
      refreshOldBiSubData(zcPProBalChg);

    } else {
      zcPProBalChg = new ZcPProBalChg();
      pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
      zcPProBalChg.setStatus("0");
      zcPProBalChg.setCoCode(this.requestMeta.getSvCoCode());
      zcPProBalChg.setZcInputCode(this.requestMeta.getSvUserID());
      zcPProBalChg.setZcInputName(this.requestMeta.getSvUserName());
      zcPProBalChg.setZcInputDate(this.requestMeta.getSysDate());
      zcPProBalChg.setOrgCode(this.requestMeta.getSvOrgCode());
      zcPProBalChg.setNd(this.requestMeta.getSvNd());
      if (zcPProBalChg.getZcPProChgBiList() == null) {
        zcPProBalChg.setZcPProChgBiList(new ArrayList());
      }

      if (zcPProBalChg.getOldBiList() == null) {
        zcPProBalChg.setOldBiList(new ArrayList());
      }
      listCursor.getDataList().add(zcPProBalChg);
      refreshOldBiSubData(zcPProBalChg);
      refreshBiSubData(zcPProBalChg);
    }
    this.setEditingObject(zcPProBalChg);
    updateFieldEditorsEditable();
    updateWFEditorEditable((ZcPProBalChg) this.listCursor.getCurrentObject(), requestMeta);//没有覆盖getSubTables()只是控制主表字段

    //在调节合同的状态，才创建调节合同的页签
    if (ZcPProBalChgConstants.ZC_P_PRO_BAL_STATUS_UPDATE_HT.equals(zcPProBalChg.getStatus())
      || ZcPProBalChgConstants.ZC_P_PRO_BAL_STATUS_OVER.equals(zcPProBalChg.getStatus())) {
      htPanel.removeAll();
      createHtChangePanel(zcPProBalChg);
      if (ZcSettingConstants.PAGE_STATUS_BROWSE.equals(pageStatus)) {

        for (int i = 0; i < htPanel.getComponentCount(); i++) {
          ((ZcHtChangePanel) htPanel.getComponentAt(i)).setEnedit(false);
        }
      }
    } else {
      splitPane.remove(bottomPane);
    }
    setButtonStatus();
    setButtonStatus(zcPProBalChg, requestMeta, listCursor);

    setOldObject();
  }

  private void refreshBiSubData(ZcPProBalChg bean) {
//    biTablePanel.setTableModel(ZcPProBalChgToTableModelConverter.convertSubBiTableData(bean.getZcPProChgBiList()));
    ZcUtil.translateColName(biTablePanel.getTable(), ZcPProBalChgToTableModelConverter.getBiInfo());
    setTableEditor(biTablePanel.getTable());
    addBiTableLisenter(biTablePanel.getTable());
    biTablePanel.repaint();
  }

  private void refreshOldBiSubData(ZcPProBalChg bean) {
    oldBiTablePanel.setTableModel(ZcPProMakeToTableModelConverter.convertSubBiTableData(bean.getOldBiList(), null));
    BeanTableModel tablemodel = (BeanTableModel) oldBiTablePanel.getTable().getModel();
    tablemodel.setEditable(false);
    ZcUtil.translateColName(oldBiTablePanel.getTable(), ZcPProMakeToTableModelConverter.biInfoWihtBudget);
    setOldBiTableEditor(oldBiTablePanel.getTable());
    oldBiTablePanel.repaint();
  }

  private void setTableEditor(JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    String colNames[] = { "指标余额表ID", "可用金额", "资金性质", "指标类型", "指标来源", "业务处室", "用途", "文号标题", "功能分类" };

    ZcBudgetHandler budgetHandler = new ZcBudgetHandler(colNames, biTablePanel, this, listCursor, getDto);
    ForeignEntityFieldCellEditor suEditor = new ForeignEntityFieldCellEditor("VwBudgetGp.getVwBudgetGp", getDto, 20, budgetHandler, colNames, "资金构成",
      "sumId");

    SwingUtil.setTableCellEditor(table, "ZC_BI_NO", suEditor);

    SwingUtil.setTableCellEditor(table, "ZC_BI_DO_SUM", new MoneyCellEditor(false));

    SwingUtil.setTableCellRenderer(table, "ZC_BI_DO_SUM", new NumberCellRenderer());

    SwingUtil.setTableCellEditor(table, "ZC_BI_JHUA_SUM", new MoneyCellEditor(false));

    SwingUtil.setTableCellRenderer(table, "ZC_BI_JHUA_SUM", new NumberCellRenderer());

    SwingUtil.setTableCellEditor(table, "ZC_BI_YJBA_SUM", new MoneyCellEditor(false));

    SwingUtil.setTableCellRenderer(table, "ZC_BI_YJBA_SUM", new NumberCellRenderer());

    SwingUtil.setTableCellEditor(table, "ZC_FUND_FILE", new FileCellEditor("zcFundFileBlobid", (BeanTableModel) table.getModel()));

    String status = ((ZcPProBalChg) listCursor.getCurrentObject()).getStatus();

    if (!"exec".equals(status) && !"yjz".equals(status)) {

      table.getTableHeader().getColumnModel().removeColumn(table.getColumn("ZC_BI_YJBA_SUM"));

    }
    //支付方式
    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE, new AsValComboBoxCellEditor("ZC_VS_PAYTYPE_NAME"));
    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE, new AsValCellRenderer("ZC_VS_PAYTYPE_NAME"));
    //资金性质
    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_FUND_CODE, new AsValComboBoxCellEditor("ZC_VS_FUND_NAME"));
    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_FUND_CODE, new AsValCellRenderer("ZC_VS_FUND_NAME"));
    //指标来源
    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_ORIGIN_CODE, new AsValComboBoxCellEditor("ZC_VS_ORIGIN_NAME"));
    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_ORIGIN_CODE, new AsValCellRenderer("ZC_VS_ORIGIN_NAME"));

  }

  private void setOldBiTableEditor(JTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());

    SwingUtil.setTableCellEditor(table, "ZC_BI_JHUA_SUM", new MoneyCellEditor(false));

    SwingUtil.setTableCellRenderer(table, "ZC_BI_JHUA_SUM", new NumberCellRenderer());

    SwingUtil.setTableCellEditor(table, "ZC_BI_YJBA_SUM", new MoneyCellEditor(false));

    SwingUtil.setTableCellRenderer(table, "ZC_BI_YJBA_SUM", new NumberCellRenderer());

    SwingUtil.setTableCellEditor(table, FIELD_TRANS_FUND_CODE, new AsValComboBoxCellEditor("ZC_VS_FUND_NAME"));

    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_FUND_CODE, new AsValCellRenderer("ZC_VS_FUND_NAME"));

    SwingUtil.setTableCellEditor(table, FIELD_TRANS_ORIGIN_CODE, new AsValComboBoxCellEditor("ZC_VS_ORIGIN_NAME"));

    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_ORIGIN_CODE, new AsValCellRenderer("ZC_VS_ORIGIN_NAME"));

    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE, new AsValComboBoxCellEditor("ZC_VS_PAYTYPE_NAME"));

    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE, new AsValCellRenderer("ZC_VS_PAYTYPE_NAME"));

    SwingUtil.setTableCellEditor(table, "ZC_FUND_FILE", new FileCellEditor("zcFundFileBlobid", (BeanTableModel) table.getModel()));

  }

  private void setOldObject() {
    oldZcPProBalChg = (ZcPProBalChg) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  /**
   * 
  * @Description: TODO(这里用一句话描述这个方法的作用)
  * @return JPanel 返回类型
  * @since 1.0
   */
  private JTabbedPane createHtChangePanel(ZcPProBalChg zcPProBalChg) {
    /**
     * 查询该采购计划下的所有合同没有结算完成的合同，没有结算完的合同才需要调解。
     */
    htElementCondtiontDto.setZcText0(zcPProBalChg.getZcMakeCode());
    htElementCondtiontDto.setIsNeedSqlFilterData("Y");
    List<ZcXmcgHt> htList = zcEbBaseServiceDelegate.getForeignEntitySelectedData("ZC_XMCG_HT.selectHTBalChgList", htElementCondtiontDto, requestMeta);
    /**
     * 根据合同的结算情况，对合同的资金进行处理。
     * 1、结算完成的合同，不需要调解
     * 2、没有支付的合同，需要调解、而且新的资金构成可以重新挂接
     * 3、支付了一部分的合同，支付的那部分资金构成要保留。没有支付的资金可以重新选择指标
     */

    if (htList != null && htList.size() > 0) {
      int count = (Integer) zcEbBaseServiceDelegate.queryObject("ZC_BAL_CHG_HT_BI.ibatorgenerated_selectCountByChgCode", zcPProBalChg.getBalChgId(),
        requestMeta);
      for (ZcXmcgHt ht : htList) {
        //构成每个合同的panel
        htPanel.add(createHtPanel(ht, zcPProBalChg, count), ht.getZcHtName());
      }
      zcPProBalChg.setXmcgHtList(htList);
    }

    bottomPane.removeAll();
    bottomPane.add("调节合同中的资金构成", htPanel);

    return htPanel;

  }

  /**
   * 
  * @Description: 创建单个合同的调节面板，合同面板分成两部分，上面是合同名称、合同编号、合同金额、中标供应商的信息。下面是资金调节的表格
  * @return JPanel 返回类型
  * @since 1.0
   */
  private JPanel createHtPanel(ZcXmcgHt ht, ZcPProBalChg balChg, int count) {

    return new ZcHtChangePanel(ht, balChg, count);

  }

  private String checkBiMoney(ZcPProBalChg zcPProBalChg) {
    StringBuilder errorInfo = new StringBuilder();
  /*  List<ZcPProBalChgBi> biList = zcPProBalChg.getZcPProChgBiList();
    BigDecimal sum = new BigDecimal("0");
    budgets = new HashMap<String, BigDecimal>();
    for (ZcPProBalChgBi bi : biList) {
      if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
        if (bi.getZcBiJhuaSum().compareTo(bi.getZcBiDoSum()) == 1) {
          errorInfo.append("[指标编号]为：" + bi.getZcBiNo() + "的[本次预算金额]为：" + bi.getZcBiJhuaSum() + "   大于该指标的[可用指标金额]：" + bi.getZcBiDoSum() + "\n");
        }
        if (bi.getZcBiYjjsSum() != null && bi.getZcBiJhuaSum().compareTo(bi.getZcBiYjjsSum()) == -1) {
          errorInfo.append("[指标编号]为：" + bi.getZcBiNo() + "的[本次预算金额]为：" + bi.getZcBiJhuaSum() + "   小于该指标的[已经结算金额]：" + bi.getZcBiYjjsSum() + "\n");
        }
        if (budgets.get(bi.getZcBiNo()) == null) {
          budgets.put(bi.getZcBiNo(), bi.getZcBiJhuaSum());
        } else {
          budgets.put(bi.getZcBiNo(), budgets.get(bi.getZcBiNo()).add(bi.getZcBiJhuaSum()));
        }
      } else {
        if (bi.getZcBiYjjsSum() != null && bi.getZcBiJhuaSum().compareTo(bi.getZcBiYjjsSum()) == -1) {
          errorInfo.append(AsValDataCache.getName("ZC_VS_ORIGIN_NAME", bi.getOriginCode()) + "的[本次预算金额]为：" + bi.getZcBiJhuaSum() + "小于该资金[已经结算金额]："
            + bi.getZcBiYjjsSum() + "\n");
        }

        if (budgets.get(NOT_BI) == null) {
          budgets.put(NOT_BI, bi.getZcBiJhuaSum());
        } else {
          budgets.put(NOT_BI, budgets.get(NOT_BI).add(bi.getZcBiJhuaSum()));
        }

      }
      sum = sum.add(bi.getZcBiJhuaSum());
    }
    if (zcPProBalChg.getZcMoneyBiSum().compareTo(sum) != 0) {
      errorInfo.append("[本次资金构成的总和]：" + sum + "不等于项目的[预算金额]：" + zcPProBalChg.getZcMoneyBiSum() + "\n");
    }*/
    return errorInfo.toString();
  }

  private String checkHtMoney(ZcPProBalChg zcPProBalChg) {
    StringBuilder errorInfo = new StringBuilder();
    List<ZcXmcgHt> xmcgHtlist = zcPProBalChg.getXmcgHtList();
    /**
     * 校验合同中每个指标的使用情况，所有合同的下某一指标的使用金额不能大于指标的预算总金额。
     */

    if (xmcgHtlist != null && xmcgHtlist.size() > 0) {
      for (ZcXmcgHt ht : xmcgHtlist) {
        //校验每个合同的资金构成中的总金额是否等于合同的总金额
        BigDecimal sum = ht.getZcHtNum();
        List<ZcXmcgHtBi> htbiList = ht.getBiList();
        if (htbiList == null || htbiList.size() == 0) {
          errorInfo.append("[采购合同]" + ht.getZcHtCode() + "资金构成为空");
          return errorInfo.toString();
        } else {
          BigDecimal biSum = new BigDecimal("0");
          for (ZcXmcgHtBi bi : htbiList) {
            if (bi.getZcBiBcsySum() == null) {
              errorInfo.append("[采购合同]" + ht.getZcHtCode() + "资金" + bi.getZcPProMitemBi().getOriginName() + "使用金额为空");
              return errorInfo.toString();
            } else {
              biSum = biSum.add(bi.getZcBiBcsySum());
              if (bi.getZcBiNo() == null || bi.getZcBiNo().length() == 0) {
                if (budgets.get(NOT_BI) == null) {
                  budgets.put(NOT_BI, BigDecimal.ZERO.subtract(bi.getZcBiBcsySum()));
                } else {
                  budgets.put(NOT_BI, budgets.get(NOT_BI).subtract(bi.getZcBiBcsySum()));
                }
              } else {
                if (budgets.get(bi.getZcBiNo()) == null) {
                  budgets.put(bi.getZcBiNo(), BigDecimal.ZERO.subtract(bi.getZcBiBcsySum()));
                } else {
                  budgets.put(bi.getZcBiNo(), budgets.get(bi.getZcBiNo()).subtract(bi.getZcBiBcsySum()));
                }
              }
            }
          }
          if (sum.compareTo(biSum) != 0) {
            errorInfo.append("[采购合同]" + ht.getZcHtCode() + "资金构成中【合同使用金额的总金额】" + biSum + "不等于合同金额" + sum);
            return errorInfo.toString();
          }
        }
      }
    }

    return errorInfo.toString();

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
        ZcPProBalChg bean = (ZcPProBalChg) self.listCursor.getCurrentObject();
        bean.setZcMakeCode(zcPProMake.getZcMakeCode());
        bean.setZcPProMake(zcPProMake);
        bean.setCoCode(zcPProMake.getCoCode());
        bean.setZcMakeName(zcPProMake.getZcMakeName());
        bean.setZcMoneyBiSum(zcPProMake.getZcMoneyBiSum());
        htElementCondtiontDto.setZcText0(zcPProMake.getZcMakeCode());
        htElementCondtiontDto.setIsNeedSqlFilterData("Y");
        self.setEditingObject(bean);
        //获取采购计划申报的支付情况
        List<ZcPProMitemBi> biList = zcEbBaseServiceDelegate.getForeignEntitySelectedData("ZC_P_PRO_MITEM_BI.getForeignBiDetail",
          htElementCondtiontDto, requestMeta);
        //获取已经支付的指标
        List<ZcPProMitemBiChg> yjjs = new ArrayList<ZcPProMitemBiChg>();
        if (biList == null) {
          throw new NullPointerException();
        } else {
          bean.setOldBiList(biList);
          String sumId = "";
          for (ZcPProMitemBi bi : biList) {
            ZcPProMitemBiChg chgBi = new ZcPProMitemBiChg();
            BeanUtil.commonFieldsCopy(bi, chgBi);
            if (bi.getZcBiUsedSum() != null && bi.getZcBiUsedSum().intValue() > 0) {
              chgBi.setZcBiYjjsSum(bi.getZcBiUsedSum());//已经使用金额
            }
            yjjs.add(chgBi);
            if (budgetFlag) {
              if (sumId.length() > 0) {
                sumId = sumId + ",'" + bi.getZcBiNo() + "'";
              } else {
                sumId = "'" + bi.getZcBiNo() + "'";
              }
            }
          }

          if (budgetFlag) {
            getDto.setZcText3(sumId);
            getDto.setZcMakeCode(zcPProMake.getZcMakeCode());
          }
          bean.getZcPProMake().setBiList(biList);
          refreshOldBiSubData(bean);
          bean.setZcPProChgBiList(yjjs);
          refreshBiSubData(bean);
        }

        //

        //        createHtChangePanel(bean);

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

  protected Integer[] deleteBiSub(JTablePanel tablePanel) {

    JPageableFixedTable table = tablePanel.getTable();

    Integer[] checkedRows;

    // 是否显示行选择框

    if (tablePanel.getTable().isShowCheckedColumn()) {

      checkedRows = table.getCheckedRows();

    } else {

      int[] selectedRows = table.getSelectedRows();

      checkedRows = new Integer[selectedRows.length];

      for (int i = 0; i < checkedRows.length; i++) {

        checkedRows[i] = selectedRows[i];

      }

    }

    if (checkedRows.length == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", "提示",

      JOptionPane.INFORMATION_MESSAGE);

    } else {

      if (table.isEditing()) {

        table.getCellEditor().stopCellEditing();

      }

      BeanTableModel tableModel = (BeanTableModel) table.getModel();

      int[] selRows = new int[checkedRows.length];

      for (int i = 0; i < selRows.length; i++) {

        selRows[i] = table.convertRowIndexToModel(checkedRows[i]);

      }

      Arrays.sort(selRows);

      for (int i = selRows.length - 1; i >= 0; i--) {
        if (budgetFlag) {
          ZcPProMitemBi bi = (ZcPProMitemBi) ((ZcPProBalChg) listCursor.getCurrentObject()).getZcPProChgBiList().get(selRows[i]);
          if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
            getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + bi.getZcBiNo() + "'", "").replaceAll("'" + bi.getZcBiNo() + "',", "")
              .replaceAll("'" + bi.getZcBiNo() + "'", ""));
          }
        }

        tableModel.deleteRow(selRows[i]);

      }

    }

    fitColumnWidth(tablePanel.getTable());

    return checkedRows;
  }

  public class ZcBudgetHandler implements IForeignEntityHandler {

    private String columNames[];

    private JTablePanel biTablePanel;

    private AbstractMainSubEditPanel edit;

    private ListCursor listCursor;

    private ElementConditionDto getDto;

    public ZcBudgetHandler(String columNames[], JTablePanel biTablePanel, AbstractMainSubEditPanel edit, ListCursor listCursor,
      ElementConditionDto getDto) {

      this.columNames = columNames;
      this.biTablePanel = biTablePanel;
      this.edit = edit;
      this.listCursor = listCursor;
      this.getDto = getDto;

    }

    public void excute(List selectedDatas) {

      if (!edit.budgetExcuce(selectedDatas)) {
        return;
      }

      JTable table = biTablePanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return;

      int k2 = table.convertRowIndexToModel(k);

    /*  if (selectedDatas.size() > 0) {

        VwBudgetGp gp = (VwBudgetGp) selectedDatas.get(0);
        ZcPProBalChg balChg = (ZcPProBalChg) listCursor.getCurrentObject();
        ZcPProBalChgBi bi = (ZcPProBalChgBi) balChg.getZcPProChgBiList().get(k2);
        String sumId = "";
        if (bi.getZcBiNo() != null) {
          sumId = bi.getZcBiNo();
        }

        bi.setZcBiNo(gp.getSumId() + "");
        bi.setZcBiDoSum(gp.getCanuseMoney());
        bi.setZcBiSum(gp.getBudgetMoney());
        // 预算单位
        if (gp.getEnCode() != null) {
          bi.setCoCode(gp.getEnCode());
          bi.setCoName(gp.getEnName());
        }
        // 资金性质
        if (gp.getMkCode() != null) {
          bi.setFundCode(gp.getMkCode());
          bi.setFundName(gp.getMkName());
        }
        // 功能分类
        if (gp.getBsCode() != null) {
          bi.setbAccCode(gp.getBsCode());
          bi.setbAccName(gp.getBsName());
        }
        // 项目类别
        if (gp.getBiCode() != null) {
          bi.setProjectTypeCode(gp.getBiCode());
          bi.setProjectTypeName(gp.getBiName());
        }
        // 付款方式
        if (gp.getPkCode() != null) {
          bi.setPaytypeCode(gp.getPkCode());
          bi.setPaytypeName(gp.getPkName());
        }
        // 指标来源
        if (gp.getBlCode() != null) {
          bi.setOriginCode(gp.getBlCode());
          bi.setOriginName(gp.getBlName());
        }
        // 业务处室
        if (gp.getMbId() != null) {
          bi.setOrgCode(gp.getMbId());
          bi.setOrgName(gp.getMbName());
        }
        // 年度
        bi.setNd(gp.getSetYear() + "");
        // 文号
        if (gp.getFileCode() != null) {
          bi.setSenddocCode(gp.getFileCode());
          bi.setSenddocName(gp.getFileName());
        }

        // 指标流向
        if (gp.getBtCode() != null) {
          bi.setBiTargetCode(gp.getBtCode());
        }
        // 预算项目
        if (gp.getBisCode() != null) {
          bi.setProjectCode(gp.getBisCode());
        }

        if (getDto.getZcText3() != null && !"".equals(getDto.getZcText3())) {
          getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + sumId + "'", "").replaceAll("'" + sumId + "',", "")
            .replaceAll("'" + sumId + "'", ""));
        }
        if (getDto.getZcText3() == null || "".equals(getDto.getZcText3())) {
          getDto.setZcText3("'" + gp.getSumId() + "'");
        } else {
          getDto.setZcText3(getDto.getZcText3() + ",'" + gp.getSumId() + "'");
        }

        edit.setEditingObject(balChg);

      }*/

    }

    public void afterClear() {
      if (!edit.budgetAfterClear()) {
        return;
      }

      JTable table = biTablePanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return;

      int k2 = table.convertRowIndexToModel(k);
/*
      ZcPProBalChg balChg = (ZcPProBalChg) listCursor.getCurrentObject();
      ZcPProBalChgBi bi = (ZcPProBalChgBi) balChg.getZcPProChgBiList().get(k2);
      if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
        getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + bi.getZcBiNo() + "'", "").replaceAll("'" + bi.getZcBiNo() + "',", "")
          .replaceAll("'" + bi.getZcBiNo() + "'", ""));
      }
      bi.setZcBiNo("");
      bi.setZcBiDoSum(null);
      bi.setZcBiSum(null);
      // 预算单位
      bi.setCoCode("");
      bi.setCoName("");
      // 资金性质
      bi.setFundCode("A");
      bi.setFundName("");
      // 功能分类
      bi.setbAccCode("");
      bi.setbAccName("");
      // 项目类别
      bi.setProjectTypeCode("");
      bi.setProjectTypeName("");
      // 付款方式
      bi.setPaytypeCode("A");
      bi.setPaytypeName("");
      // 指标来源
      bi.setOriginCode("4");
      bi.setOriginName("");
      // 年度
      bi.setNd("");
      // 文号
      bi.setSenddocCode("");
      bi.setSenddocName("");
      // 业务处室
      bi.setOrgCode("");
      bi.setOrgName("");

      edit.setEditingObject(balChg);*/

    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        VwBudgetGp rowData = (VwBudgetGp) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getSumId();

        data[i][col++] = rowData.getCanuseMoney();

        data[i][col++] = rowData.getMkName();

        data[i][col++] = rowData.getBpName();

        data[i][col++] = rowData.getBlName();

        data[i][col++] = rowData.getMbName();

        data[i][col++] = rowData.getSmName();

        data[i][col++] = rowData.getFileName();

        data[i][col++] = rowData.getBsName();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        public boolean isCellEditable(int row, int colum) {

          return false;

        }

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
      };
      return model;

    }

    public boolean isMultipleSelect() {

      return false;

    }

  }

  private void setButtonStatus() {

    if (this.btnStatusList.size() == 0) {

      ButtonStatus bs = new ButtonStatus();

      bs = new ButtonStatus();

      bs.setButton(this.editButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.saveButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.deleteButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);
      bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.sendButton);

      btnStatusList.add(bs);

    }
    ZcPProBalChg zcPProBalChg = (ZcPProBalChg) listCursor.getCurrentObject();

    String billStatus = zcPProBalChg.getStatus();

    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, this.compoId, zcPProBalChg.getProcessInstId());

  }

}
