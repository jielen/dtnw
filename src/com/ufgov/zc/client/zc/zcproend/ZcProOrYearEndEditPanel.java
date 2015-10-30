package com.ufgov.zc.client.zc.zcproend;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_FUND_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ORIGIN_CODE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcPProMakeToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcYearEndToTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.zc.CommonButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcYearPlan;
import com.ufgov.zc.common.zc.publish.IZcYearEndServiceDelegate;

public class ZcProOrYearEndEditPanel extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZcProOrYearEndEditPanel.class);

  public IZcYearEndServiceDelegate zcYearEndServiceDelegate = (IZcYearEndServiceDelegate) ServiceFactory.create(IZcYearEndServiceDelegate.class,

  "zcYearEndServiceDelegate");

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

//  private String compoId = "ZC_PRO_END_YEAR_END";

  protected ElementConditionDto getDto;

  public boolean isUseBi = true;

  private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private ZcProOrYearEndEditDialog parent;

  private ZcYearPlan yearPlan;

  private final ListCursor listCursor;

  private ZcProOrYearEndListPanel listPanel;

  private String tabStatus;

  private JTabbedPane makeBiTabPane;

  private JTablePanel makeBiTablePanel = new JTablePanel("makeBiTablePanel");

  private JTabbedPane htBiTabPane;

  private JTablePanel htBiTablePanel = new JTablePanel("htBiTablePanel");

  private CommonButton fcarraryNewBtn = new CommonButton("fcarraryNew", "结转立项", "", true);

  public ZcProOrYearEndEditPanel(ZcProOrYearEndEditDialog parent, ListCursor listCursor, String tabStatus, ZcProOrYearEndListPanel listPanel) {
    super(ZcYearPlan.class, BillElementMeta.getBillElementMetaWithoutNd("ZC_PRO_END_YEAR_END"));
    this.parent = parent;
    this.listCursor = listCursor;
    this.listPanel = listPanel;
    this.tabStatus = tabStatus;
    IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
      "baseDataServiceDelegate");

    isUseBi = "Y".equals(AsOptionMeta.getOptVal(ZcSettingConstants.OPT_ZC_USE_BUDGET_INTERFACE));

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate("手动结转"),
      TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.colCount = 3;
    init();
    requestMeta.setCompoId(getCompoId());

    refreshData();
    setSubTableCellEditor(makeBiTablePanel);

    setSubTableCellEditor(htBiTablePanel);
    translateSubTableColumn();

//    if (yearPlan.getZcHtJzSum().compareTo(new BigDecimal("0")) == 0) {
//      htBiTabPane.setVisible(true);
//    } else {
//      htBiTabPane.setVisible(false);
//    }
  }

  public void initToolBar(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(getCompoId());
    toolBar.add(fcarraryNewBtn);
    fcarraryNewBtn.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        doNew();
      }
    });

  }

  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    TextFieldEditor makeCodeField = new TextFieldEditor("项目编号", "zcMakeCode");
    editorList.add(makeCodeField);

    TextFieldEditor makeNameField = new TextFieldEditor("项目名称", "zcMakeName");
    editorList.add(makeNameField);

    AsValFieldEditor makeStatusField = new AsValFieldEditor("项目状态", "zcMakeStatus", "ZC_VS_MAKE_STATUS");
    editorList.add(makeStatusField);

    MoneyFieldEditor zcMakeSumField = new MoneyFieldEditor("计划金额", "zcMakeSum");
    editorList.add(zcMakeSumField);

    MoneyFieldEditor zcHtSumField = new MoneyFieldEditor("已录合同金额", "zcHtSum");
    editorList.add(zcHtSumField);

    MoneyFieldEditor zcBalSumField = new MoneyFieldEditor("已录结算金额", "zcBalSum");
    editorList.add(zcBalSumField);

    MoneyFieldEditor zcMakeJzSumField = new MoneyFieldEditor("计划结转金额", "zcMakeJzSum");
    editorList.add(zcMakeJzSumField);

    MoneyFieldEditor zcHtJzSumField = new MoneyFieldEditor("合同结转金额", "zcHtJzSum");
    editorList.add(zcHtJzSumField);

    MoneyFieldEditor zcBalJzSumField = new MoneyFieldEditor("结算结转金额", "zcBalJzSum");
    editorList.add(zcBalJzSumField);

    MoneyFieldEditor zcBlHtSumField = new MoneyFieldEditor("补录合同金额", "zcBlHtSum");
    editorList.add(zcBlHtSumField);

    MoneyFieldEditor zcBlBalSumField = new MoneyFieldEditor("补录合同结算金额", "zcBlBalSum");
    editorList.add(zcBlBalSumField);

    MoneyFieldEditor zcBlHtJzSumField = new MoneyFieldEditor("补录合同结转金额", "zcBlHtJzSum");
    editorList.add(zcBlHtJzSumField);

    for (Iterator iter = editorList.iterator(); iter.hasNext();) {
      AbstractFieldEditor editor = (AbstractFieldEditor) iter.next();
      editor.setEnabled(false);
    }
    return editorList;
  }

  public JComponent createSubBillPanel() {
    JPanel subMainPanel = new JPanel();
    subMainPanel.setLayout(new BorderLayout());

    makeBiTabPane = new JTabbedPane();
    makeBiTabPane.add("采购计划资金挂接", initMakeBiTablePanel());
    subMainPanel.add(makeBiTabPane, BorderLayout.CENTER);

    htBiTabPane = new JTabbedPane();
    htBiTabPane.add("补录合同资金挂接", initHtBiTablePanel());
    subMainPanel.add(htBiTabPane, BorderLayout.SOUTH);

    return subMainPanel;
  }

  public JFuncToolBar getTablePaneToolBar(final JTablePanel tablePanel) {
    JFuncToolBar makeBiToolBar = new JFuncToolBar();
    JButton addMakeBiBtn = new JButton("添加");
    JButton deleteMakeBIBtn = new JButton("删除");
    makeBiToolBar.add(addMakeBiBtn);
    makeBiToolBar.add(deleteMakeBIBtn);
    addMakeBiBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ZcPProMitemBi biBean = new ZcPProMitemBi();
        biBean.setTempId(Guid.genID());
        setItemBiDefaultValue(biBean);
        addSub(tablePanel, biBean);
      }
    });

    deleteMakeBIBtn.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        deleteSub(tablePanel);
      }
    });

    return makeBiToolBar;
  }

  public JComponent initMakeBiTablePanel() {
    makeBiTablePanel.setLayout(new BorderLayout());
    makeBiTablePanel.init();
    makeBiTablePanel.add(getTablePaneToolBar(makeBiTablePanel), BorderLayout.SOUTH);
    return makeBiTablePanel;
  }

  public JComponent initHtBiTablePanel() {
    htBiTablePanel.setLayout(new BorderLayout());
    htBiTablePanel.init();
    htBiTablePanel.add(getTablePaneToolBar(htBiTablePanel), BorderLayout.SOUTH);

    return htBiTablePanel;
  }

  private void setSubTableCellEditor(JTablePanel tablePanel) {
    JTable table = tablePanel.getTable();
    getDto = new ElementConditionDto();
    getDto.setNd(requestMeta.getSvNd());
    String colNames[] = { "指标余额表ID", "可用金额", "资金性质", "指标类型", "指标来源", "业务处室", "用途", "文号标题", "功能分类" };
    ZcProOrYearBudgetHandler budgetHandler = new ZcProOrYearBudgetHandler(colNames, tablePanel, this, listCursor, getDto);
    ForeignEntityFieldCellEditor suEditor = new ForeignEntityFieldCellEditor("VwBudgetGp.getVwBudgetGp", getDto, 20, budgetHandler, colNames, "资金构成",
      "zcBiNo");
    SwingUtil.setTableCellEditor(table, "ZC_BI_NO", suEditor);

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

  private void refreshData() {
    yearPlan = (ZcYearPlan) this.listCursor.getCurrentObject();
    if (null != yearPlan) {
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
      setEditingObject(yearPlan);
    }
    refreshSubTableData();
  }

  private void refreshSubTableData() {
//    makeBiTablePanel.setTableModel(ZcYearEndToTableModelConverter.convertToCarraryBiBeanTableModel(yearPlan.getMakeBiList()));
//    htBiTablePanel.setTableModel(ZcYearEndToTableModelConverter.convertToCarraryBiBeanTableModel(yearPlan.getHtBiList()));
  }

  private void translateSubTableColumn() {
    ZcUtil.translateColName(makeBiTablePanel.getTable(), ZcPProMakeToTableModelConverter.biInfoWihtBudget);
    ZcUtil.translateColName(htBiTablePanel.getTable(), ZcPProMakeToTableModelConverter.biInfoWihtBudget);
  }

  public void setItemBiDefaultValue(ZcPProMitemBi zcPProMitemBi) {

    zcPProMitemBi.setOriginCode("99");// 其他
    zcPProMitemBi.setFundCode("A");
    zcPProMitemBi.setPaytypeCode("A");

  }

  public String beforeCheck() {
    StringBuffer errorInfo = new StringBuffer();

  /*  ZcYearPlan p = (ZcYearPlan) listCursor.getCurrentObject();
    List<ZcPProMitemBi> makeBiList = p.getMakeBiList();
    List<ZcPProMitemBi> htBiList = p.getHtBiList();

    Map<String, BigDecimal> jhuamap = new HashMap<String, BigDecimal>();
    Map<String, BigDecimal> domap = new HashMap<String, BigDecimal>();

    BigDecimal jhuaTotal = new BigDecimal("0");
    BigDecimal htTotal = new BigDecimal("0");

    for (ZcPProMitemBi bi : makeBiList) {
      BigDecimal doSum = bi.getZcBiDoSum();
      BigDecimal jhuaSum = bi.getZcBiJhuaSum();
      String biNo = bi.getZcBiNo();
      if (doSum.compareTo(jhuaSum) < 0) {
        errorInfo.append("[采购计划资金]中指标编号为：");
        errorInfo.append(biNo);
        errorInfo.append("[本次项目预算金额]大于[指标可以金额]\n");
      }
      jhuamap.put(biNo, jhuaSum);
      domap.put(biNo, doSum);
      jhuaTotal = jhuaTotal.add(jhuaSum);
      bi.setZcMakeCode(p.getZcMakeCode());
    }

    for (ZcPProMitemBi bi : htBiList) {
      BigDecimal doSum = bi.getZcBiDoSum();
      BigDecimal jhuaSum = bi.getZcBiJhuaSum();
      String biNo = bi.getZcBiNo();
      if (doSum.compareTo(jhuaSum) < 0) {
        errorInfo.append("[补录合同资金]中指标编号为：");
        errorInfo.append(biNo);
        errorInfo.append("[本次项目预算金额]大于[指标可以金额]\n");
      }

      BigDecimal jhua = jhuamap.get(biNo);
      if (null != jhua && jhua.compareTo(new BigDecimal(0)) > 0) {
        BigDecimal newJhua = jhua.add(jhuaSum);
        jhuamap.put(biNo, newJhua);
      }

      htTotal = htTotal.add(jhuaSum);

      bi.setZcMakeCode(p.getZcMakeCode());
    }

    if (null != htBiList && htBiList.size() > 0) {
      Set keySet = jhuamap.keySet();
      for (Iterator iter = keySet.iterator(); iter.hasNext();) {
        String biNo = (String) iter.next();
        BigDecimal doSum = domap.get(biNo);
        BigDecimal jhuaSum = jhuamap.get(biNo);
        if (doSum.compareTo(jhuaSum) < 0) {
          errorInfo.append("指标编号为：").append(biNo).append("的项目预算总金额大于指标可用总金额\n");
        }
      }
    }

    System.out.println("jhuaTotal===" + jhuaTotal);
    System.out.println("jzSum--" + p.getZcMakeJzSum());

    if (jhuaTotal.compareTo(p.getZcMakeJzSum()) != 0) {
      errorInfo.append("[计划资金]中的总金额不等与计划结转金额\n");
    }*/

//    if (htTotal.compareTo(p.getZcBlHtJzSum()) != 0) {
//      errorInfo.append("[补录合同资金]中的总金额不等与补录合同结转金额\n");
//    }

    return errorInfo.toString();
  }

  public static String getCompoId() {
    return "ZC_PRO_END_YEAR_END";
  }


  public void doNew() {
    String errors = beforeCheck();
    if (errors.length() > 0) {
      JOptionPane.showMessageDialog(ZcProOrYearEndEditPanel.this, errors, "提示", JOptionPane.WARNING_MESSAGE);
      return;
    }
    ZcYearPlan p = (ZcYearPlan) listCursor.getCurrentObject();
    try {
      zcYearEndServiceDelegate.carraryNewMakeByManual(p, WorkEnv.getInstance().getWebRoot(), requestMeta);
      JOptionPane.showMessageDialog(ZcProOrYearEndEditPanel.this, "编号为：[" + p.getZcMakeCode() + "] 的项目结转成功", "提示", JOptionPane.OK_OPTION);
      listPanel.refreshCurrentTabData();
      parent.dispose();
    } catch (Exception e1) {
      logger.error(e1.getMessage(), e1);
      UIUtilities.showStaickTraceDialog(e1, ZcProOrYearEndEditPanel.this, "错误", e1.getMessage());
    }
  }

}
