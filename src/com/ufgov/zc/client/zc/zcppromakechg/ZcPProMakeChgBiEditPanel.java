/**
 * 
 */
package com.ufgov.zc.client.zc.zcppromakechg;

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
import java.util.Hashtable;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.ZcWorkFlowAdapter;
import com.ufgov.zc.client.common.converter.zc.ZcPProBalChgToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcXmcgHtToTableModelConverter;
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
import com.ufgov.zc.client.component.button.zc.CommonButton;
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
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.zcpprobichange.ZcPProBalChgEditPanel;
import com.ufgov.zc.common.budget.model.VwBudgetGp;
import com.ufgov.zc.common.commonbiz.model.Company;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalChgConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.BusinessException;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcBalChgHtBi;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcPProBalChg;
import com.ufgov.zc.common.zc.model.ZcPProMitemBiChg;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcTBchtItem;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBiChg;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcPProBalChgServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcPProMakeServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcXmcgHtServiceDelegate;

/**
 * @author Administrator
 *
 */
public class ZcPProMakeChgBiEditPanel extends AbstractMainSubEditPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 7370045320675303878L;

  private static final Logger logger = Logger.getLogger(ZcPProBalChgEditPanel.class);

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_P_PRO_MAKE_CHG_BI";

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
  
  private FuncButton confirmBtn = new CommonButton("fsend","提交",null);

  private JFuncToolBar makeBiBtnToolbar;

  private BillElementMeta biBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_P_PRO_BAL_BI");

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  private ListCursor listCursor;

  private ZcPProBalChg oldZcPProBalChg;

  protected ElementConditionDto getBiDto = new ElementConditionDto();

  //  protected boolean budgetFlag =ZcUtil.useBudget();

  private ZcPProMakeChgBiListPanel listPanel;

  /**
   *各种面板
   */
  private JTablePanel biTablePanel;

  private JTablePanel oldBiTablePanel;

  private JSaveableSplitPane splitPane;

  JTabbedPane topPane = new JTabbedPane();

  JTabbedPane bottomPane = new JTabbedPane();

  private JTabbedPane htPanel = new JTabbedPane();

  private ZcPProMakeChgBiEditPanel self = this;

  private GkBaseDialog parent;

  //  private ElementConditionDto htElementCondtiontDto = new ElementConditionDto();

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

  IZcPProMakeServiceDelegate ZcPProMakeServiceDelegate = (IZcPProMakeServiceDelegate) ServiceFactory.create(IZcPProMakeServiceDelegate.class,
    "zcPProMakeServiceDelegate");

  private JTabbedPane tbpanel = new JTabbedPane(JTabbedPane.LEFT);

  private JPanel htMainPanel = new JPanel();

  private JTablePanel htTablePanel;

  private JTablePanel htOldBiTablePanel;

  private JTablePanel htBiTablePanel;

  private JFuncToolBar htBiToolbar;

  private int htTableSelectRow = -1;

  public ZcPProMakeChgBiEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcPProMakeChgBiListPanel listPanel) {
    super(ZcPProBalChg.class, listPanel.getBillElementMeta());
    this.listCursor = listCursor;
    this.listPanel = listPanel;
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta
      .translate(ZcPProBalChgConstants.FIELD_TRANS_ZC_P_PRO_MAKE_CHG_BI), TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15),
      Color.BLUE));
    this.htMainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "采购合同资金变更", TitledBorder.CENTER,
      TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.parent = parent;
    this.colCount = 3;
    init();
    requestMeta.setCompoId(compoId);
    refreshData();
    setButtonStatus();
  }

  protected void init() {

    this.initToolBar(toolBar);

    if (this.billClass != null && this.eleMeta != null) {

      initFieldEditorPanel(this.billClass, this.eleMeta);

    } else {

      initFieldEditorPanel();

    }

    workPanel.setLayout(new BorderLayout());

    workPanel.add(fieldEditorPanel, BorderLayout.NORTH);

    JComponent tabTable = createSubBillPanel();

    if (tabTable != null) {

      workPanel.add(tabTable, BorderLayout.CENTER);

    }

    this.setLayout(new BorderLayout());

    this.add(toolBar, BorderLayout.NORTH);

    //    tbpanel.add("<html>采<br>购<br>计<br>划</html>", workPanel);
    //    tbpanel.add("<html>采<br>购<br>合<br>同</html>",htMailPanel);
    tbpanel.add("采购计划", workPanel);
    initHtMainPanel(htMainPanel);
    tbpanel.add("采购合同", htMainPanel);

    this.add(tbpanel, BorderLayout.CENTER);

  }

  /**
   * 创建合同面板
   */
  private void initHtMainPanel(JPanel htMainPanel) {
    // TCJLODO Auto-generated method stub
    //    htMailPanel=new JPanel();
    //    htMailPanel.setLayout(new BorderLayout());
    htMainPanel.setLayout(new BorderLayout());

    htTablePanel = new JTablePanel();
    htTablePanel.setPanelId(this.getClass().getName() + "httable");
    htTablePanel.init();
    htTablePanel.getSearchBar().setVisible(false);
    //    htTablePanel.setTablePreferencesKey(this.getClass().getName() + "httable");
    htTablePanel.getTable().setShowCheckedColumn(false);
    htTablePanel.getTable().setPreferredScrollableViewportSize(new Dimension(htTablePanel.getScreenWidth(), 100));

    htBiTablePanel = new JTablePanel();
    htBiTablePanel.setPanelId(this.getClass().getName() + "htbitable");
    htBiTablePanel.init();
    htBiTablePanel.getSearchBar().setVisible(false);
    //    biTablePanel.setTablePreferencesKey(this.getClass().getName() + "htbitable");
    htBiTablePanel.getTable().setShowCheckedColumn(true);
    htBiTablePanel.getTable().setPreferredScrollableViewportSize(new Dimension(htBiTablePanel.getScreenWidth(), 100));

    htBiToolbar = new JFuncToolBar();
    JButton addhtBiBtn = new SubaddButton(false), inserthtBiBtn = new SubinsertButton(false), delhtBiBtn = new SubdelButton(false);
//    htBiToolbar.add(addhtBiBtn);
//    htBiToolbar.add(inserthtBiBtn);
//    htBiToolbar.add(delhtBiBtn);
    htBiTablePanel.add(htBiToolbar, BorderLayout.SOUTH);

    JScrollPane htbiScrlPanel = new JScrollPane(htBiTablePanel);

    addhtBiBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 添加分包明细
        ZcXmcgHtBiChg bi = new ZcXmcgHtBiChg();
        int rowNum = addSub(htBiTablePanel, bi);
        //设置默认值
        htBiTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);
      }
    });
    inserthtBiBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 插入分包明细
        ZcXmcgHtBiChg bi = new ZcXmcgHtBiChg();
        int rowNum = insertSub(htBiTablePanel, bi);
        htBiTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);
      }
    });
    // 删除项目标段按钮的事件监听
    delhtBiBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        JPageableFixedTable table = htBiTablePanel.getTable();
        Integer[] selectedRows = table.getCheckedRows();
        BeanTableModel tableModel = (BeanTableModel) table.getModel();
        List dataList = tableModel.getDataBeanList();
        for (Integer checkedRow : selectedRows) {
          int accordDataRow = table.convertRowIndexToModel(checkedRow);
          ZcXmcgHtBiChg bi = (ZcXmcgHtBiChg) dataList.get(accordDataRow);
          //          if(isOldBi(bi)){
          //            JOptionPane.showMessageDialog(self, "采购计划的原指标和自筹资金不允许删除，请调整待安排资金!", "提示", JOptionPane.INFORMATION_MESSAGE);
          //            return;
          //          }
          /* if (bi.getZcBiYjjsSum() != null && bi.getZcBiYjjsSum().intValue() > 0) {
             JOptionPane.showMessageDialog(self, "该指标存在已经支付的金额，允许修改，不允许删除!", "提示", JOptionPane.INFORMATION_MESSAGE);
             return;
           }*/
        }
        Integer[] checkedRows = deleteBiSub(htBiTablePanel);
      }

    });

    htOldBiTablePanel = new JTablePanel();
    htOldBiTablePanel.setPanelId(this.getClass().getName() + "htoldbitable");
    htOldBiTablePanel.init();
    htOldBiTablePanel.getSearchBar().setVisible(false);
    //    biTablePanel.setTablePreferencesKey(this.getClass().getName() + "htoldbitable");
    htOldBiTablePanel.getTable().setShowCheckedColumn(false);
    htOldBiTablePanel.getTable().setPreferredScrollableViewportSize(new Dimension(htOldBiTablePanel.getScreenWidth(), 100));

    JTabbedPane t1 = new JTabbedPane();
    t1.addTab("采购合同", htTablePanel);

    JTabbedPane t2 = new JTabbedPane();
    t2.addTab("合同原资金构成", htOldBiTablePanel);

    JTabbedPane t3 = new JTabbedPane();
    t3.addTab("合同变更后资金构成", htbiScrlPanel);

    JSaveableSplitPane sppanel = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, t2, t3);
    sppanel.setDividerDefaultLocation(this.getClass().getName() + "_ht_splitPane_dividerLocation", 200);
    sppanel.setOneTouchExpandable(true);
    sppanel.setDividerSize(10);
    sppanel.setBackground(self.getBackground());

    htMainPanel.add(t1, BorderLayout.NORTH);
    htMainPanel.add(sppanel, BorderLayout.CENTER);

  }

  /*

   * 设置监听，通过选择的【项目分包】自动更新【分包需求明细】，并判断是否显示询价页签以及更新【询价内容】

   */

  private void addHtTableLisenter(final JPageableFixedTable table) {
    final BeanTableModel model = (BeanTableModel) table.getModel();
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          BeanTableModel tableModel = (BeanTableModel) table.getModel();
          ZcXmcgHt ht;
          if (table.getSelectedRows() != null && table.getSelectedRows().length > 0) {
            ht = (ZcXmcgHt) tableModel.getBean(table.convertRowIndexToModel(table.getSelectedRows()[0]));
            ZcPProBalChg zcPProBalChg = (ZcPProBalChg) listCursor.getCurrentObject();
            refreshHtBiTable(zcPProBalChg,ht.getZcHtCode());
          }
        }
      }
    });
    table.getModel().addTableModelListener(new TableModelListener() {
      @Override
      public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
          if (e.getColumn() >= 0
            && ("".equals(model.getColumnIdentifier(e.getColumn())) || "PUR_TYPE".equals(model.getColumnIdentifier(e.getColumn())))) {
            int k = table.getSelectedRow();
            if (k < 0)
              return;
            ZcXmcgHt ht = (ZcXmcgHt) model.getBean(table.convertRowIndexToModel(k));
            ZcPProBalChg zcPProBalChg = (ZcPProBalChg) listCursor.getCurrentObject();
            refreshHtBiTable(zcPProBalChg,ht.getZcHtCode());
          }
        }
      }
    });
  }

  private void refreshHtBiTable(ZcPProBalChg zcPProBalChg, String htCode) {
    if (zcPProBalChg == null || zcPProBalChg.getHtBiHistoryLst() == null || htCode == null) {
      htBiTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData2(new ArrayList()));
      htOldBiTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData3(new ArrayList()));
      ZcUtil.translateColName(htBiTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getBiInfo2());
      ZcUtil.translateColName(htOldBiTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getBiInfo2());
      return;
    }

    List htBiLst = (List) zcPProBalChg.getHtBiLst().get(htCode);
    htBiTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData2(htBiLst));

    List htBiHistoryLst = (List) zcPProBalChg.getHtBiHistoryLst().get(htCode);
    htOldBiTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData3(htBiHistoryLst));
    ZcUtil.translateColName(htBiTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getBiInfo2());
    ZcUtil.translateColName(htOldBiTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getBiInfo2());
  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();
    //单据编号
    AutoNumFieldEditor balChgId = new AutoNumFieldEditor(LangTransMeta.translate("变更单编号"), "balChgId");

    /*
     * 选择单位
     */
    zcMakeElementCondtiontDto.setStatus("exec");
    final CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_CO_CODE), "coCode");
    zcCoCode.addValueChangeListener(new ValueChangeListener() {
      public void valueChanged(ValueChangeEvent e) {
        Company company = (Company) zcCoCode.getValue();
        if (company != null && ZcUtil.isYsdw()) {
          zcMakeElementCondtiontDto.setCoCode(company.getCode());
        }
      }
    });
    if(ZcUtil.isYsdw()){
      zcMakeElementCondtiontDto.setCoCode(requestMeta.getSvCoCode());
    }
    zcMakeElementCondtiontDto.setNd(this.requestMeta.getSvNd());

    AsValFieldEditor status = new AsValFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_STATUS), "status",
      "VS_ZC_P_PRO_BAL_CHG_STATUS");
    TextFieldEditor zcCoCodeNd = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_CO_CODE_ND), "nd");

    /*
     * 选择项目
     */

    String zcMakeColumNames[] = { LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MAKE_NAME),
      LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MAKE_CODE), LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MONEY_BI_SUM) };
    ZcPProMakeHandler makeHandler = new ZcPProMakeHandler(zcMakeColumNames);
    //用于采购计划资金变更时，选择项目的过滤条件，当一个计划存在在途的变更单时，选择时不出现 
    zcMakeElementCondtiontDto.setDattr2("noZaiTuChgBi");
    zcMakeSelectEdit = new ForeignEntityFieldEditor("ZC_P_PRO_MAKE.ibatorgenerated_selectByExample", zcMakeElementCondtiontDto, 20, makeHandler,
      zcMakeColumNames, LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MAKE_CODE), "zcMakeCode");

    TextFieldEditor zcMakeName = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MAKE_NAME), "zcMakeName");

    MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_MONEY_BI_SUM), "zcMoneyBiSum");

    //    TextFieldEditor zcInputCode = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_CODE), "zcInputCode");
    //    editorList.add(zcInputCode);

    TextFieldEditor zcInputName = new TextFieldEditor("录入人", "zcInputName");

    TextFieldEditor zcMakeTel = new TextFieldEditor("联系电话", "zcMakeTel");

    DateFieldEditor zcInputDate = new DateFieldEditor("录入时间", "zcInputDate");

    //    editorList.add(zcInputDate);
    TextFieldEditor zcRemark = new TextFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_REMARK), "remark");

    //    

    //  editorList.add(zcCoCodeNd);

    editorList.add(zcMakeSelectEdit);
    editorList.add(zcMakeName);
    editorList.add(zcMoneyBiSum);

    editorList.add(zcCoCode);
    editorList.add(zcMakeTel);
    editorList.add(balChgId);

    editorList.add(zcRemark);
    editorList.add(zcInputName);
    editorList.add(zcInputDate);

    editorList.add(status);

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
    topPane.addTab("原资金构成", oldBiTablePanel);

    biTablePanel = new JTablePanel();
    biTablePanel.setPanelId(this.getClass().getName() + "bitable");
    biTablePanel.init();
    biTablePanel.getSearchBar().setVisible(false);
    //    biTablePanel.setTablePreferencesKey(this.getClass().gtName() + "_table");
    biTablePanel.getTable().setShowCheckedColumn(true);
    makeBiBtnToolbar = new JFuncToolBar();
    makeBiBtnToolbar.add(addBtn);
//    makeBiBtnToolbar.add(insertBtn);
//    makeBiBtnToolbar.add(delBtn);

    biTablePanel.add(makeBiBtnToolbar, BorderLayout.SOUTH);
    addBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 添加分包明细
        ZcPProMitemBiChg bi = new ZcPProMitemBiChg();
        bi.setFlag(ZcPProMitemBiChg.FLAG_NEW);
        int rowNum = addSub(biTablePanel, bi);
        //设置默认值
        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);
      }
    });
    insertBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 插入分包明细
        ZcPProMitemBiChg bi = new ZcPProMitemBiChg();
        bi.setFlag(ZcPProMitemBiChg.FLAG_NEW);
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
          if (isOldBi(bi)) {
            JOptionPane.showMessageDialog(self, "采购计划的原指标和自筹资金不允许删除，请调整待安排资金!", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
          }
          /* if (bi.getZcBiYjjsSum() != null && bi.getZcBiYjjsSum().intValue() > 0) {
             JOptionPane.showMessageDialog(self, "该指标存在已经支付的金额，允许修改，不允许删除!", "提示", JOptionPane.INFORMATION_MESSAGE);
             return;
           }*/
        }
        Integer[] checkedRows = deleteBiSub(biTablePanel);
      }

    });

    bottomPane.addTab("变更后资金构成", biTablePanel);

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
    //    splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, topPane, bottomPane);

    splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, topPane, bottomPane);
    splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 200);
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerSize(10);
    splitPane.setBackground(self.getBackground());

    return splitPane;
  }

  boolean isOldBi(ZcPProMitemBiChg bi) {
    // TCJLODO Auto-generated method stub
    ZcPProBalChg bal = (ZcPProBalChg) listCursor.getCurrentObject();
    for (int i = 0; i < bal.getOldBiList().size(); i++) {
      ZcPProMitemBiChg oldBi = (ZcPProMitemBiChg) bal.getOldBiList().get(i);
      if (oldBi.getZcProBiSeq().equals(bi.getZcProBiSeq()) && !ZcPProMitemBiChg.DPTZJ.equals(oldBi.getOriginCode())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(compoId);
    toolBar.add(saveButton);
    toolBar.add(editButton);
    toolBar.add(deleteButton);
//    toolBar.add(sendButton);
//    toolBar.add(suggestPassButton);
    toolBar.add(confirmBtn);
//    toolBar.add(auditPassButton);
//    toolBar.add(callbackButton);
//    toolBar.add(unAuditButton);
//    toolBar.add(unTreadButton);
//    toolBar.add(saveAndSendButton);
//    toolBar.add(callbackButton);
//    toolBar.add(traceButton);
//    toolBar.add(previousButton);
//    toolBar.add(nextButton);
    toolBar.add(exitButton);

    confirmBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doConfirm();
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

  }

  protected void doConfirm() {
    if (!checkBeforeSave()) {
      return;
    } 

    int num = JOptionPane.showConfirmDialog(this, "请核对新增的指标在计划和合同里的使用金额，本次操作将从预算系统中核减对应指标到采购系统中，\n请核对准确无误后再提交！\n您现在确定要提交吗", "提交确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      
    }
    ZcPProBalChg bal = (ZcPProBalChg) this.listCursor.getCurrentObject();
    try{
     bal = zcPProBalChgServiceDelegate.auditFN(bal, requestMeta);
     JOptionPane.showMessageDialog(this, "操作成功!.", "提示", JOptionPane.INFORMATION_MESSAGE);
     refreshData();
    }catch (BusinessException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }
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
   * 这里暂不设工作流，在这里进行审批时，核减指标库指标
   */
  private void doSuggestPass() {
    
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
   /* if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);
      if (num == JOptionPane.YES_OPTION) {
        if (!doSave()) {
          return;
        }
      }
    }*/
    this.parent.dispose();
  }

  private void refreshAll(ZcPProBalChg afterSaveBill, boolean isRefreshButton) {
    this.listCursor.setCurrentObject(afterSaveBill);
    refreshData();
    if (isRefreshButton) {
      setButtonStatus();
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
    String str1 = checkMakeBiMoney(zcPProBalChg);
    if (str1.length() != 0) {
      JOptionPane.showMessageDialog(this, str1, "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    str1=checkHtMoney(zcPProBalChg);
    if (str1.length() != 0) {
      JOptionPane.showMessageDialog(this, str1, "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  public void doEdit() {

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
    updateFieldEditorsEditable();
    setButtonStatus();
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
      biTableModel.setEditable(true);
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
        ZcUtil.useBudget(), requestMeta);
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
        this.listPanel.zcPProBalChgServiceDelegate.deleteZcPProBalChgFN(zcPProBalChg, WorkEnv.getInstance().getWebRoot(), ZcUtil.useBudget(),
          requestMeta);
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
        doExit();
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
    getBiDto.setZcText2("1");
    if (ZcUtil.useBudget()) {
      getBiDto.setNd(requestMeta.getSvNd());
      getBiDto.setZcText2("1");
      if(ZcUtil.isYsdw()){
        getBiDto.setCoCode(requestMeta.getSvCoCode());
      }
    }
    ZcPProBalChg cur = (ZcPProBalChg) listCursor.getCurrentObject();
    if (cur != null && (!"".equals(ZcUtil.safeString(cur.getBalChgId())) || !"自动编号".equals(cur.getBalChgId()))) {
      pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE; 
      cur=zcPProBalChgServiceDelegate.selectByPrimaryKey(cur.getBalChgId(), requestMeta);
      getBiDto.setCoCode(cur.getCoCode());
        List newBiLst = cur.getZcPProChgBiList();
        if (newBiLst != null && newBiLst.size() > 0) {
          if (ZcUtil.useBudget()) {
            String sumId = "";
            for (int i = 0; i < newBiLst.size(); i++) {
              if (sumId.length() > 0) {
                sumId = sumId + ",'" + ((ZcPProMitemBiChg) newBiLst.get(i)).getZcBiNo() + "'";
              } else {
                sumId = "'" + ((ZcPProMitemBiChg) newBiLst.get(i)).getZcBiNo() + "'";
              }
            }
            getBiDto.setZcText3(sumId);
          }
        } 
      zcMakeElementCondtiontDto.setZcMakeCode(cur.getZcMakeCode());
      listCursor.setCurrentObject(cur);
    } else {
      cur = new ZcPProBalChg();
      pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
      cur.setStatus("0");
      cur.setCoCode(this.requestMeta.getSvCoCode());
      cur.setZcInputCode(this.requestMeta.getSvUserID());
      cur.setZcInputName(this.requestMeta.getSvUserName());
      cur.setZcInputDate(this.requestMeta.getSysDate());
      cur.setOrgCode(this.requestMeta.getSvOrgCode());
      cur.setNd(this.requestMeta.getSvNd());
      if (cur.getZcPProChgBiList() == null) {
        cur.setZcPProChgBiList(new ArrayList());
      }

      if (cur.getOldBiList() == null) {
        cur.setOldBiList(new ArrayList());
      }
      listCursor.getDataList().add(cur);
      listCursor.setCurrentObject(cur);
    }
    refreshOldBiSubData(cur);
    refreshBiSubData(cur);
    refreshHtTables(cur);
    this.setEditingObject(cur);
    updateFieldEditorsEditable();
    updateWFEditorEditable(cur, requestMeta);//没有覆盖getSubTables()只是控制主表字段

    setButtonStatus(); 

    setOldObject();
  }

  /**
   * 根据当前变更单的id，获取对应的合同数据
   * @param zcPProBalChg
   */
  private void getHtDataWithChgId(ZcPProBalChg chg) {
    // TCJLODO Auto-generated method stub
    //获取合同列表
    ElementConditionDto dto = new ElementConditionDto();
    dto.setZcMakeCode(chg.getZcMakeCode());
    dto.setZcText3("have");//其他地方的查询需要ht为exec的，后来 采购计划资金变更部件，待安排资金调整时，获取全部的合同，要查全部，所以用zcText3有值无值判定，无值时，是原来的模式，有值时，这个条件就不起作用了 20140915 cjl
    List htLst = zcEbBaseServiceDelegate.queryDataForList("ZC_XMCG_HT.getMainHtByMakeCode", dto, requestMeta);
    htLst = htLst == null ? new ArrayList() : htLst;
    chg.setXmcgHtList(htLst);
 
//    chg.getHtBiHistoryLst().put(htCode, historyLst);
//    chg.getHtBiLst().put(htCode, chgLst);
    //合同变更后的资金
    dto = new ElementConditionDto();
    dto.setZcText1(chg.getBalChgId());
    dto.setZcText2(ZcPProMitemBiChg.FLAG_NEW); 
      List biLst = zcEbBaseServiceDelegate.queryDataForList("ZC_XMCG_HT_BI_HISTORY.selectHtBi", dto, requestMeta);
      if (biLst == null || biLst.size() == 0) {
        biLst=new ArrayList();      
      }
      Hashtable biMap=new Hashtable();
      for(int i=0;i<biLst.size();i++){
        ZcXmcgHtBiChg bi=(ZcXmcgHtBiChg) biLst.get(i);
        String htCode=bi.getZcHtCode();
        if(biMap.containsKey(htCode)){
          List lst=(List) biMap.get(htCode);
          lst.add(bi);
        }else{
          List lst=new ArrayList();
          lst.add(bi);
          biMap.put(htCode, lst);
        }
      }
      chg.setHtBiLst(biMap);


      dto = new ElementConditionDto();
      dto.setZcText1(chg.getBalChgId());
      dto.setZcText2(ZcPProMitemBiChg.FLAG_HISTORY); 
        List oldBiLst = zcEbBaseServiceDelegate.queryDataForList("ZC_XMCG_HT_BI_HISTORY.selectHtBi", dto, requestMeta);
        if (oldBiLst == null || oldBiLst.size() == 0) {
          oldBiLst=new ArrayList();      
        }
        Hashtable oldBiMap=new Hashtable();
        for(int i=0;i<oldBiLst.size();i++){
          ZcXmcgHtBiChg bi=(ZcXmcgHtBiChg) oldBiLst.get(i);
          String htCode=bi.getZcHtCode();
          if(oldBiMap.containsKey(htCode)){
            List lst=(List) oldBiMap.get(htCode);
            lst.add(bi);
          }else{
            List lst=new ArrayList();
            lst.add(bi);
            oldBiMap.put(htCode, lst);
          }
        }
        chg.setHtBiHistoryLst(oldBiMap);
  }

  /**
   * 选择一个采购计划之后，获取这个计划的合同数据
   * @param chg
   * @return
   */
  private ZcPProBalChg getHtDataWithMakeCode(ZcPProBalChg chg) {

    if (chg.getZcMakeCode() == null || chg.getZcMakeCode().trim().length() == 0) {
      chg.setXmcgHtList(new ArrayList());
      return chg;
    }
    //获取合同列表
    ElementConditionDto dto = new ElementConditionDto();
    dto.setZcMakeCode(chg.getZcMakeCode());
    dto.setZcText3("have");//其他地方的查询需要ht为exec的，后来 采购计划资金变更部件，待安排资金调整时，获取全部的合同，要查全部，所以用zcText3有值无值判定，无值时，是原来的模式，有值时，这个条件就不起作用了 20140915 cjl
    List htLst = zcEbBaseServiceDelegate.queryDataForList("ZC_XMCG_HT.getMainHtByMakeCode", dto, requestMeta);
    htLst = htLst == null ? new ArrayList() : htLst;
    chg.setXmcgHtList(htLst);

    //获取合同的现有指标情况 history
    if (chg.getHtBiHistoryLst() == null || chg.getHtBiHistoryLst().size() == 0) {
      String htCode = "";

      chg.setHtBiHistoryLst(new Hashtable());
      chg.setHtBiLst(new Hashtable());

      for (int i = 0; i < htLst.size(); i++) {
        ZcXmcgHt ht = (ZcXmcgHt) htLst.get(i);
        htCode = ht.getZcHtCode();
        List chgLst = new ArrayList(), historyLst = new ArrayList();

        ZcXmcgHt zcXmcgHt = zcXmcgHtServiceDelegate.selectByPrimaryKey(ht.getZcHtCode(), this.requestMeta);

        if (zcXmcgHt == null || zcXmcgHt.getBiList() == null)
          continue;

        for (int j = 0; j < zcXmcgHt.getBiList().size(); j++) {
          ZcXmcgHtBi htbi = (ZcXmcgHtBi) zcXmcgHt.getBiList().get(j);
          ZcXmcgHtBiChg biHistory = new ZcXmcgHtBiChg();
          ZcXmcgHtBiChg biChg = new ZcXmcgHtBiChg();

          BeanUtil.commonFieldsCopy(htbi, biHistory);
          biHistory.setFlag(ZcXmcgHtBiChg.FLAG_HISTORY);
          BeanUtil.commonFieldsCopy(htbi, biChg);
          biChg.setFlag(ZcXmcgHtBiChg.FLAG_NEW);
          chgLst.add(biChg);
          historyLst.add(biHistory);
        }
        if (historyLst.size() > 0) {
          chg.getHtBiHistoryLst().put(htCode, historyLst);
          chg.getHtBiLst().put(htCode, chgLst);
        }
      }
    }
    return chg;
  }

  private void refreshHtTables(ZcPProBalChg zcPProBalChg) {
    // TCJLODO Auto-generated method stub
    htTablePanel.setTableModel(ZcPProBalChgToTableModelConverter.convertHtTableData(zcPProBalChg.getXmcgHtList()));
    ZcUtil.translateColName(htTablePanel.getTable(), ZcPProBalChgToTableModelConverter.htInfo);

    SwingUtil.setTableCellRenderer(htTablePanel.getTable(), ZcElementConstants.FIELD_TRANS_ZC_HT_STATUS, new AsValCellRenderer("ZC_VS_HT_STATUS"));
    addHtTableLisenter(htTablePanel.getTable());

    if (zcPProBalChg.getXmcgHtList() == null || zcPProBalChg.getXmcgHtList().size() == 0)
      return;

    BeanTableModel<ZcXmcgHt> tm = (BeanTableModel<ZcXmcgHt>) htTablePanel.getTable().getModel();
    //tablePanelPack.getTable().getModel()).getDataBeanList()

    ZcXmcgHt ht = (ZcXmcgHt) zcPProBalChg.getXmcgHtList().get(0);

    String htcode = ht == null ? "" : ht.getZcHtCode();

    refreshHtBiTable(zcPProBalChg,htcode);

  }

  private void refreshBiSubData(ZcPProBalChg bean) {
    if (ZcUtil.useBudget()) {
      biTablePanel.setTableModel(ZcPProBalChgToTableModelConverter.convertSubBiTableData(bean.getZcPProChgBiList(), bean.getOldBiList(),
        wfCanEditFieldMap));
    } else {
      biTablePanel.setTableModel(ZcPProBalChgToTableModelConverter.convertSubBiTableDataWithOutBudget(bean.getZcPProChgBiList(), wfCanEditFieldMap));
    }

    if (ZcUtil.useBudget()) {
      ZcUtil.translateColName(biTablePanel.getTable(), ZcPProBalChgToTableModelConverter.biInfoWihtBudget);
    } else {
      ZcUtil.translateColName(biTablePanel.getTable(), ZcPProBalChgToTableModelConverter.biInfoWihtoutBugdet);
    }
    setBiTableEditor(biTablePanel.getTable());
    addBiTableLisenter(biTablePanel.getTable());
    biTablePanel.repaint();
  }

  private void refreshOldBiSubData(ZcPProBalChg bean) {
    if (ZcUtil.useBudget()) {
      oldBiTablePanel.setTableModel(ZcPProBalChgToTableModelConverter.convertOldBiTableData(bean.getOldBiList(), null));
    } else {
      oldBiTablePanel.setTableModel(ZcPProBalChgToTableModelConverter.convertOldBiTableDataWithOutBudget(bean.getOldBiList(), null));
    }

    if (ZcUtil.useBudget()) {
      ZcUtil.translateColName(oldBiTablePanel.getTable(), ZcPProBalChgToTableModelConverter.biInfoWihtBudget);
    } else {
      ZcUtil.translateColName(oldBiTablePanel.getTable(), ZcPProBalChgToTableModelConverter.biInfoWihtoutBugdet);
    }
    BeanTableModel tablemodel = (BeanTableModel) oldBiTablePanel.getTable().getModel();
    tablemodel.setEditable(false);
    setOldBiTableEditor(oldBiTablePanel.getTable());
    oldBiTablePanel.repaint();
  }

  private void setBiTableEditor(JTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());
    if (ZcUtil.useBudget()) {

      String colNames[] = { "指标余额表ID", "指标来源", "发文文号", "资金性质", "采购项目", "功能分类", "经济分类", "是否监督使用", "是否政府采购", "指标总金额", "指标可用金额" };
      ZcBudgetHandler budgetHandler = new ZcBudgetHandler(colNames, biTablePanel, this, listCursor, getBiDto);
      //只显示政府采购的指标，汽修、汽保可以显示全部指标，参见汽修、汽保的选择部件
      getBiDto.setDattr1("zfcg");
      ForeignEntityFieldCellEditor suEditor = new ForeignEntityFieldCellEditor("VwBudgetGp.getVwBudgetGp", getBiDto, 20, budgetHandler, colNames,
        "资金构成", "sumId");

      SwingUtil.setTableCellEditor(table, "ZC_BI_NO", suEditor);

      SwingUtil.setTableCellEditor(table, "ZC_BI_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_BI_SUM", new NumberCellRenderer());

      SwingUtil.setTableCellEditor(table, "ZC_BI_DO_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_BI_DO_SUM", new NumberCellRenderer());

      SwingUtil.setTableCellEditor(table, "ZC_BI_JHUA_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_BI_JHUA_SUM", new NumberCellRenderer());

      SwingUtil.setTableCellEditor(table, "ZC_BI_YJBA_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_BI_YJBA_SUM", new NumberCellRenderer());
    } else {

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

      //    String status = ((ZcPProMake) listCursor.getCurrentObject()).getZcMakeStatus();
      //
      //    if (!"exec".equals(status) && !"yjz".equals(status)) {
      //
      //      table.getTableHeader().getColumnModel().removeColumn(table.getColumn("ZC_BI_YJBA_SUM"));
      //
      //    }
    }
  }

  private void setOldBiTableEditor(JTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());
    if (ZcUtil.useBudget()) {

      SwingUtil.setTableCellEditor(table, "ZC_BI_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_BI_SUM", new NumberCellRenderer());

      SwingUtil.setTableCellEditor(table, "ZC_BI_DO_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_BI_DO_SUM", new NumberCellRenderer());

      SwingUtil.setTableCellEditor(table, "ZC_BI_JHUA_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_BI_JHUA_SUM", new NumberCellRenderer());

      SwingUtil.setTableCellEditor(table, "ZC_BI_YJBA_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_BI_YJBA_SUM", new NumberCellRenderer());
    } else {

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

      //    String status = ((ZcPProMake) listCursor.getCurrentObject()).getZcMakeStatus();
      //
      //    if (!"exec".equals(status) && !"yjz".equals(status)) {
      //
      //      table.getTableHeader().getColumnModel().removeColumn(table.getColumn("ZC_BI_YJBA_SUM"));
      //
      //    }
    }

  }

  private void setOldObject() {
    oldZcPProBalChg = (ZcPProBalChg) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  /**
   * 
  * @Description: 创建单个合同的调节面板，合同面板分成两部分，上面是合同名称、合同编号、合同金额、中标供应商的信息。下面是资金调节的表格
  * @return JPanel 返回类型
  * @since 1.0
   */
//  private JPanel createHtPanel(ZcXmcgHt ht, ZcPProBalChg balChg, int count) {
//
//    return new ZcHtChangePanel(ht, balChg, count);
//
//  }

  private String checkMakeBiMoney(ZcPProBalChg zcPProBalChg) {
    StringBuilder errorInfo = new StringBuilder();
    List<ZcPProMitemBiChg> biList = zcPProBalChg.getZcPProChgBiList();
    BigDecimal sum = new BigDecimal("0");
    budgets = new HashMap<String, BigDecimal>();
    for (ZcPProMitemBiChg bi : biList) {
      if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo()) && !isOldBi(bi)) {
        if (bi.getZcBiJhuaSum().compareTo(bi.getZcBiDoSum()) == 1) {
          errorInfo.append("【指标编号】" + bi.getZcBiNo() + "的【本次采购使用金额】" + bi.getZcBiJhuaSum() + "   大于该指标的【指标可用金额】" + bi.getZcBiDoSum() + ",请调整\n");
        }
      }
      sum = sum.add(bi.getZcBiJhuaSum());
    }
    if (zcPProBalChg.getZcMoneyBiSum().compareTo(sum) != 0) {
      errorInfo.append("采购计划【变更后资金构成】的采购使用金额合计(" + sum + ")，不等于采购计划的【项目预算】金额(" + zcPProBalChg.getZcMoneyBiSum() + "),请调整\n");
    }
    return errorInfo.toString();
  }

  /**
   * 1、合同内部金额一致，合同总金额=合同资金构成的金额之和
   * 2、新增指标在各个合同的合同使用金额之和=新增指标的计划金额
   * @param chg
   * @return
   */
  private String checkHtMoney(ZcPProBalChg chg) {
    List<ZcXmcgHt> htLst = chg.getXmcgHtList();
    if(htLst==null|| htLst.size()==0)return "";
    
    StringBuilder errorInfo = new StringBuilder();
    Hashtable<String, BigDecimal> newBiMap=new Hashtable<String, BigDecimal>();
    for(int i=0;i<htLst.size();i++){
      ZcXmcgHt ht=htLst.get(i);
//      chg.getHtBiHistoryLst().put(htCode, historyLst);
//      chg.getHtBiLst().put(htCode, chgLst);newBiLst oldBiLst
      List oldBiLst=(List) chg.getHtBiHistoryLst().get(ht.getZcHtCode());
      List newBiLst=(List) chg.getHtBiLst().get(ht.getZcHtCode());
      BigDecimal htSum=new BigDecimal(0);
      if(newBiLst!=null){
        for(int j=0;j<newBiLst.size();j++){
          ZcXmcgHtBiChg bi=(ZcXmcgHtBiChg)newBiLst.get(j);
          htSum=htSum.add(bi.getZcBiBcsySum()==null?new BigDecimal(0):bi.getZcBiBcsySum());
          if(bi.getZcMakeCode()==null || bi.getZcMakeCode().length()==0){//新录入的指标
            BigDecimal d=newBiMap.get(bi.getZcBiNo());
            if(d==null){
              d=new BigDecimal(0);
            }
            d=d.add(bi.getZcBiBcsySum()==null?new BigDecimal(0):bi.getZcBiBcsySum());    
            newBiMap.put(bi.getZcBiNo(), d);        
          }
        }
      }
      if(ht.getZcHtNum().compareTo(htSum)!=0){
        errorInfo.append("\n合同").append(ht.getZcHtCode()).append("的【合同金额】与其资金构成的【合同使用金额】合计不相等，请调整");
      }
    }
    List makeBiLst=chg.getZcPProChgBiList();    
    for(int i=0;i<makeBiLst.size();i++){
      ZcPProMitemBiChg bi=(ZcPProMitemBiChg) makeBiLst.get(i);
      if(bi.getZcMakeCode()==null || bi.getZcMakeCode().length()==0){//新录入的指标      
        BigDecimal htBiSum=newBiMap.get(bi.getZcBiNo());
        if(htBiSum==null){
          htBiSum=new BigDecimal(0);
        }
        if(bi.getZcBiJhuaSum().compareTo(htBiSum)==-1){
          errorInfo.append("\n指标编号(").append(bi.getZcBiNo()).append(")在合同中的使用金额超出了在采购计划中的使用金额，请调整");
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
      ZcPProBalChg bean = (ZcPProBalChg) self.listCursor.getCurrentObject();
      bean.setOldBiList(new ArrayList());
      for (Object object : selectedDatas) {
        ZcPProMake zcPProMake = (ZcPProMake) object;
        getBiDto.setCoCode(zcPProMake.getCoCode());
        bean.setZcMakeCode(zcPProMake.getZcMakeCode());
        bean.setZcPProMake(zcPProMake);
        bean.setCoCode(zcPProMake.getCoCode());
        bean.setZcMakeName(zcPProMake.getZcMakeName());
        bean.setZcMoneyBiSum(zcPProMake.getZcMoneyBiSum());
        self.setEditingObject(bean);
        List<ZcPProMitemBi> mitemBiLst = ZcPProMakeServiceDelegate.getZcPProMitemBi(zcPProMake.getZcMakeCode(), ZcUtil.useBudget(), requestMeta);
        List<ZcPProMitemBiChg> chgBiLst = new ArrayList<ZcPProMitemBiChg>();
        List<ZcPProMitemBiChg> historyBiLst = new ArrayList<ZcPProMitemBiChg>();
        if (mitemBiLst != null) {
          String sumId = "";
          for (ZcPProMitemBi bi : mitemBiLst) {
            ZcPProMitemBiChg chgBi = new ZcPProMitemBiChg();
            ZcPProMitemBiChg historyBi = new ZcPProMitemBiChg();
            BeanUtil.commonFieldsCopy(bi, chgBi);
            BeanUtil.commonFieldsCopy(bi, historyBi);
            chgBi.setFlag(ZcPProMitemBiChg.FLAG_NEW);
            historyBi.setFlag(ZcPProMitemBiChg.FLAG_HISTORY);
            if (bi.getZcBiUsedSum() != null && bi.getZcBiUsedSum().intValue() > 0) {
              chgBi.setZcBiYjjsSum(bi.getZcBiUsedSum());//已经使用金额
            }
            chgBiLst.add(chgBi);
            historyBiLst.add(historyBi);
            if (ZcUtil.useBudget()) {
              if (sumId.length() > 0) {
                sumId = sumId + ",'" + bi.getZcBiNo() + "'";
              } else {
                sumId = "'" + bi.getZcBiNo() + "'";
              }
            }
          }

          if (ZcUtil.useBudget()) {
            getBiDto.setZcText3(sumId);
            getBiDto.setZcMakeCode(zcPProMake.getZcMakeCode());
          }
          bean.getZcPProMake().setBiList(mitemBiLst);
          bean.setOldBiList(historyBiLst);
          refreshOldBiSubData(bean);
          bean.setZcPProChgBiList(chgBiLst);
          refreshBiSubData(bean);
          getHtDataWithMakeCode(bean);
          refreshHtTables(bean);
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

    /*

     * 设置外部实体数据条件

     */

    public Boolean beforeSelect(ElementConditionDto dto) {
      ZcPProBalChg chg = (ZcPProBalChg) listCursor.getCurrentObject();
      if (chg.getZcMakeCode() != null) {
        int result = JOptionPane.showConfirmDialog(self, "确认重新选择吗?", "选择确认", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
          listCursor.setCurrentObject(null);
          refreshData();
        } else {
          return false;
        }
      }
      return true;
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
        if (ZcUtil.useBudget()) {
          ZcPProMitemBi bi = (ZcPProMitemBi) ((ZcPProBalChg) listCursor.getCurrentObject()).getZcPProChgBiList().get(selRows[i]);
          if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
            getBiDto.setZcText3(getBiDto.getZcText3().replaceAll(",'" + bi.getZcBiNo() + "'", "").replaceAll("'" + bi.getZcBiNo() + "',", "")
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

    public boolean beforeSelect(ElementConditionDto dto) {

      List bi = null;
//      Object chg = listCursor.getCurrentObject();

      ZcPProBalChg balChg = (ZcPProBalChg) listCursor.getCurrentObject();
      
      dto.setOrgCode(null);
 
        bi = balChg.getZcPProChgBiList();
        if (bi != null) {
          for (int i = 0; i < bi.size(); i++) {
            ZcPProMitemBiChg mb = (ZcPProMitemBiChg) bi.get(i);
            if (mb.getOrgCode() != null && mb.getOrgCode().length() > 0) {
              dto.setOrgCode(mb.getOrgCode());
              return true;
            }
          }
        }

       

      JTable table = biTablePanel.getTable();
      int k = table.getSelectedRow();
      int k2 = table.convertRowIndexToModel(k);
      ZcPProMitemBiChg cbi = (ZcPProMitemBiChg) balChg.getZcPProChgBiList().get(k2); 
      /**
       * 增加一个指标时，不设指标的删除按钮，只通过调整待安排资金的金额和新增指标金额进行  
                              例如，某计划有资金三条：a 指标甲；b 自筹资金； c 待安排指标
                               调整时，不能调整指标甲、自筹资金的使用金额，只能调整待安排指标的使用金额
                                通过新增，插入一条新的指标：指标乙，其使用金额，是待安排资金减去的金额
                                  新增的指标，不设zc_make_code，这样方便后面判断这是不是新增的指标，在终审的时候，往计划表里插入数据，此时再写上zc_make_code值
       */
      if(cbi.getZcMakeCode()!=null){
        JOptionPane.showMessageDialog(workPanel, "资金调整方法，如增加指标金额X元：\n1)将待安排指标的本次采购使用金额减少X元;\n2)点击下面的添加按钮，插入一个空白行，点击空白行的第一个单元格，选择指标;\n3)选择指标后，将本次采购使用金额设置为X元;\n4)如果已经有合同，点击左上的采购合同页签，进入合同界面进行调整.", "提示", JOptionPane.INFORMATION_MESSAGE);
        return false;
      }
      return true;
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

      if (selectedDatas.size() > 0) {

        ZcPProBalChg balChg = (ZcPProBalChg) listCursor.getCurrentObject();
        ZcPProMitemBiChg bi = (ZcPProMitemBiChg) balChg.getZcPProChgBiList().get(k2);

        VwBudgetGp gp = (VwBudgetGp) selectedDatas.get(0);
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
          bi.setFundCode(gp.getMkId());
          bi.setFundName(gp.getMkName());
        }
        // 功能分类
        if (gp.getBsCode() != null) {
          bi.setbAccCode(gp.getBsCode());
          bi.setbAccName(gp.getBsCode() + gp.getBsName());
        }
        // 经济分类
        if (gp.getBsiCode() != null) {
          bi.setOutlayCode(gp.getBsiId());
          bi.setOutlayName(gp.getBsiCode() + gp.getBsiName());
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
          bi.setProjectName(gp.getBisName());
        }
        //是否监督使用
        bi.setBtName(gp.getBtName());
        //是否政府采购
        bi.setGbName(gp.getGbName());

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
        //为了后续方便保存和比对，这里将ZcProBiSeq置为空，见BudgetUtil.isOldPlanBi();
        bi.setZcProBiSeq(null);
        //vou_id清空，用于指标接口
        bi.setZcUseBiId(null);
        
        //往合同中增加这个选中的指标数据
        addBiToHt(bi);
      }
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

      ZcPProBalChg balChg = (ZcPProBalChg) listCursor.getCurrentObject();
      ZcPProMitemBiChg bi = (ZcPProMitemBiChg) balChg.getZcPProChgBiList().get(k2);

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
      //是否监督使用
      bi.setBtName("");
      //是否政府采购
      bi.setGbName("");
      // 年度
      bi.setNd("");
      // 文号
      bi.setSenddocCode("");
      bi.setSenddocName("");
      // 业务处室
      bi.setOrgCode("");
      bi.setOrgName("");

      edit.setEditingObject(balChg);

    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        VwBudgetGp rowData = (VwBudgetGp) showDatas.get(i);

        int col = 0;
        //        String colNames[] = { "指标余额表ID", "指标来源", "发文文号", "资金性质","采购项目", "功能分类", "经济分类","释放监督使用","释放政府采购","指标总金额","指标可用金额" };
        //        String colNames[] = { "指标余额表ID", "指标来源", "发文文号", "资金性质","采购项目", "功能分类", "经济分类","释放监督使用","释放政府采购","指标总金额","指标可用金额" };
        data[i][col++] = rowData.getSumId();
        data[i][col++] = rowData.getBlName();
        data[i][col++] = rowData.getFileName();
        data[i][col++] = rowData.getMkName();
        data[i][col++] = rowData.getBisName();
        data[i][col++] = rowData.getBsCode() + rowData.getBsName();
        data[i][col++] = rowData.getBsiCode() + rowData.getBsiName();
        data[i][col++] = rowData.getBtName();
        data[i][col++] = rowData.getGbName();
        data[i][col++] = rowData.getBudgetMoney();
        data[i][col++] = rowData.getCanuseMoney();

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
      bs.addBillStatus(ZcPProBalChg.VS_STATUS_DRAFT);
      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.saveButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.deleteButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE); 
      bs.addBillStatus(ZcPProBalChg.VS_STATUS_DRAFT);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.confirmBtn);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE); 
      bs.addBillStatus(ZcPProBalChg.VS_STATUS_DRAFT);

      btnStatusList.add(bs);

    }
    ZcPProBalChg zcPProBalChg = (ZcPProBalChg) listCursor.getCurrentObject();

    String billStatus = zcPProBalChg.getStatus();

    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, this.compoId, zcPProBalChg.getProcessInstId());

  }

  /**
   * 选择一个指标后,往合同中增加这个选中的指标数据
   * @param bi
   */
  public void addBiToHt(ZcPProMitemBiChg bi) {
    //如果合同里面有待安排资金，则插入一条指标
    ZcPProBalChg curObj = (ZcPProBalChg) listCursor.getCurrentObject();    
    if(curObj.getXmcgHtList()==null || curObj.getXmcgHtList().size()==0)return;    
    for(int i=0;i<curObj.getXmcgHtList().size();i++){
      ZcXmcgHt ht=(ZcXmcgHt) curObj.getXmcgHtList().get(i);
//      chg.getHtBiHistoryLst().put(htCode, historyLst);
//      chg.getHtBiLst().put(htCode, chgLst);
      List oldBiLst=(List) curObj.getHtBiHistoryLst().get(ht.getZcHtCode());
      List newBiLst=(List) curObj.getHtBiLst().get(ht.getZcHtCode());
      if(haveDptzj(oldBiLst) && !haveSameBi(newBiLst,bi)){
        ZcXmcgHtBiChg chgBi=buildHtNewBi(ht,bi);
        newBiLst.add(chgBi);
      }
    }
    refreshHtTables(curObj);
  }

 

  private ZcXmcgHtBiChg buildHtNewBi(ZcXmcgHt ht, ZcPProMitemBiChg bi) {
    ZcXmcgHtBiChg newBi=new ZcXmcgHtBiChg();
    newBi.setZcHtCode(ht.getZcHtCode());
    newBi.setZcPProMitemBi(bi);
    newBi.setZcCanUseSum(bi.getZcBiJhuaSum());
    newBi.setZcBiNo(bi.getZcBiNo());
    newBi.setFlag(ZcXmcgHtBiChg.FLAG_NEW);
    return newBi;
  }

  private boolean haveSameBi(List newBiLst, ZcPProMitemBiChg bi) {
    if(newBiLst==null)return false;
    if(bi==null)return false;
    for(int i=0;i<newBiLst.size();i++){
      ZcXmcgHtBiChg newBi=(ZcXmcgHtBiChg)newBiLst.get(i);    
      if(bi.getZcBiNo().equals(newBi.getZcBiNo())){
        return true;
      }
    }
    return false;
  }
  
  private boolean haveDptzj(List oldBiLst) {
    if(oldBiLst==null)return false;
    for(int i=0;i<oldBiLst.size();i++){
      ZcXmcgHtBiChg bi=(ZcXmcgHtBiChg)oldBiLst.get(i);
      if(ZcPProMitemBi.DPTZJ.equals(bi.getZcPProMitemBi().getOriginCode())){//待安排资金
        return true;
      }
    }
    return false;
  }
}
