package com.ufgov.zc.client.basedata;import java.awt.BorderLayout;import java.awt.Color;import java.awt.Container;import java.awt.Dialog;import java.awt.Dimension;import java.awt.FlowLayout;import java.awt.Font;import java.awt.GridBagConstraints;import java.awt.GridBagLayout;import java.awt.Insets;import java.awt.Window;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.event.MouseAdapter;import java.math.BigDecimal;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import javax.swing.BorderFactory;import javax.swing.JButton;import javax.swing.JComboBox;import javax.swing.JFileChooser;import javax.swing.JFrame;import javax.swing.JLabel;import javax.swing.JOptionPane;import javax.swing.JPanel;import javax.swing.JScrollPane;import javax.swing.JTable;import javax.swing.SwingUtilities;import javax.swing.UIManager;import javax.swing.border.TitledBorder;import javax.swing.table.DefaultTableModel;import javax.swing.table.TableModel;import org.apache.log4j.Logger;import com.ufgov.smartclient.common.DefaultInvokeHandler;import com.ufgov.smartclient.common.UIUtilities;import com.ufgov.smartclient.component.table.JGroupableTable;import com.ufgov.smartclient.plaf.BigButtonSplitPaneUI;import com.ufgov.zc.client.common.BillElementMeta;import com.ufgov.zc.client.common.LangTransMeta;import com.ufgov.zc.client.common.MyTableModel;import com.ufgov.zc.client.common.ParentWindowAware;import com.ufgov.zc.client.common.ServiceFactory;import com.ufgov.zc.client.common.UIConstants;import com.ufgov.zc.client.common.WorkEnv;import com.ufgov.zc.client.common.converter.CpDataImportToTableModelConverter;import com.ufgov.zc.client.component.AdjustTypeComboBox;import com.ufgov.zc.client.component.ExcelExtractor;import com.ufgov.zc.client.component.GkBaseDialog;import com.ufgov.zc.client.component.JFuncToolBar;import com.ufgov.zc.client.component.SelectFileTxtField;import com.ufgov.zc.client.component.button.CancelCreateButton;import com.ufgov.zc.client.component.button.CreateButton;import com.ufgov.zc.client.component.button.DeleteButton;import com.ufgov.zc.client.component.button.FuncButton;import com.ufgov.zc.client.component.button.ImportButton;import com.ufgov.zc.client.component.button.ImportSettingButton;import com.ufgov.zc.client.component.button.PrintButton;import com.ufgov.zc.client.component.button.PrintPreviewButton;import com.ufgov.zc.client.component.button.PrintSettingButton;import com.ufgov.zc.client.component.ui.AbstractDataDisplay;import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;import com.ufgov.zc.client.component.ui.MultiDataDisplay;import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;import com.ufgov.zc.client.component.ui.TableDisplay;import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;import com.ufgov.zc.client.print.PrintPreviewer;import com.ufgov.zc.client.print.PrintSettingDialog;import com.ufgov.zc.client.print.Printer;import com.ufgov.zc.client.util.NumCellRenderderUtil;import com.ufgov.zc.client.util.SwingUtil;import com.ufgov.zc.common.commonbiz.model.SearchCondition;import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;import com.ufgov.zc.common.cp.model.CpImpRecord;import com.ufgov.zc.common.cp.publish.ICpVoucherServiceDelegate;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.constants.BillTypeConstants;import com.ufgov.zc.common.system.constants.CpSettingConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.dto.PrintObject;import com.ufgov.zc.common.system.exception.BaseException;import com.ufgov.zc.common.system.exception.OtherException;public class OutDataImportPanel extends JPanel implements ParentWindowAware {  private static final long serialVersionUID = 5991532264057378877L;  private static final Logger logger = Logger.getLogger(OutDataImportPanel.class);  private Window parentWindow;  private String compoId = "MA_DATE_IMPORT";  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();  protected AbstractDataDisplay importDataDisplay;  private JFuncToolBar toolBar = new JFuncToolBar();  private SelectFileTxtField fileNameField = new SelectFileTxtField(20);  private List list;  private BillElementMeta bem = BillElementMeta  .getBillElementMeta(BillTypeConstants.BILL_TYPE_CODE_AM_APPLY_QUERY);  private IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory  .create(IBaseDataServiceDelegate.class, "baseDataServiceDelegate");  private String cpVoucherServiceDelegateName = "cpVoucherServiceDelegate";  private ICpVoucherServiceDelegate cpVoucherServiceDelegate = (ICpVoucherServiceDelegate) ServiceFactory  .create(ICpVoucherServiceDelegate.class, cpVoucherServiceDelegateName);  private final class ImportDataDisplay extends MultiDataDisplay {    private static final long serialVersionUID = 4886999668900284087L;    class TableMouseListener extends MouseAdapter {      private TableDisplay tableDisplay;      public TableMouseListener(TableDisplay tableDisplay) {        this.tableDisplay = tableDisplay;      }      public TableDisplay getTableDisplay() {        return tableDisplay;      }    };    private ImportDataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays,    AbstractSearchConditionArea conditionArea, boolean showConditionArea) {      super(displays, showingDisplays, conditionArea, showConditionArea,      CpSettingConstants.TAB_ID_MA_OUTDATA_IMPORT);      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "外边数据导入",      TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));    }    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems,    final TableDisplay tableDisplay) {      final ElementConditionDto elementConditionDto = new ElementConditionDto();      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());      elementConditionDto.setStatus(tableDisplay.getStatus());      elementConditionDto.setNumLimCompoId(compoId);//设置数值权限      for (AbstractSearchConditionItem item : searchConditionItems) {        item.putToElementConditionDto(elementConditionDto);      }      final Container c = tableDisplay.getTable().getParent();      UIUtilities.asyncInvoke(new DefaultInvokeHandler<TableModel>() {        @Override        public void before() {          assert c != null;          installLoadingComponent(c);        }        @Override        public void after() {          assert c != null;          unInstallLoadingComponent(c);          c.add(tableDisplay.getTable());        }        @Override        public TableModel execute() throws Exception {          String panelId = "unImport";          if (importDataDisplay != null && importDataDisplay.getActiveTableDisplay() != null) {            panelId = importDataDisplay.getActiveTableDisplay().getStatus();          }          //         if(!panelId.equalsIgnoreCase("unImport")){          list = cpVoucherServiceDelegate.getCpImpRecordList(elementConditionDto, requestMeta);          //         }          return CpDataImportToTableModelConverter.convertDataImportToTableModelListPage(          list, BillTypeConstants.BILL_TYPE_CODE_AM_APPLY_QUERY);        }        @Override        public void success(TableModel model) {          tableDisplay.setTableModel(model);          NumCellRenderderUtil.setAmApplyTableNumCellRenderer(tableDisplay.getTable());        }      });    }  }  static {    LangTransMeta.init("AM%");    LangTransMeta.init("CP%");    LangTransMeta.init("BI%");  }  private void refreshCurrentTabData() {    SwingUtilities.invokeLater(new Runnable() {      public void run() {        TableDisplay tableDisplay = importDataDisplay.getActiveTableDisplay();        tableDisplay.setTableModel(CpDataImportToTableModelConverter.convertDataImportToTableModelListPage(        list, BillTypeConstants.BILL_TYPE_CODE_AM_APPLY_QUERY));        tableDisplay.revalidate();        tableDisplay.repaint();      }    });  }  /**   * 构造函数   */  public OutDataImportPanel() {    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {      @Override      public List<SearchCondition> execute() throws Exception {        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil        .getNeedDisplaySearchConditonList(WorkEnv.getInstance().getCurrUserId(),        CpSettingConstants.TAB_ID_MA_OUTDATA_IMPORT);        return needDisplaySearchConditonList;      }      @Override      public void success(List<SearchCondition> needDisplaySearchConditonList) {        List<TableDisplay> showingDisplays = SearchConditionUtil        .getNeedDisplayTableDisplay(needDisplaySearchConditonList);        init(createimportDataDisplay(showingDisplays));        requestMeta.setCompoId(compoId);        revalidate();        repaint();      }    });  }  protected void init(AbstractDataDisplay importDataDisplay) {    this.importDataDisplay = importDataDisplay;    setLayout(new BorderLayout());    addToolBarComponent(toolBar);    add(toolBar, BorderLayout.NORTH);    this.revalidate();    this.repaint();    this.add(importDataDisplay);  }  private AbstractSearchConditionArea createCpConditionArea() {    Map defaultValueMap = new HashMap();    AbstractSearchConditionArea cpSearchConditionArea = new SaveableSearchConditionArea(    CpSettingConstants.CONDITION_ID_MA_OUTDATA_IMPORT, bem, true, defaultValueMap, compoId);    return cpSearchConditionArea;  }  private AbstractDataDisplay createimportDataDisplay(List<TableDisplay> showingDisplays) {    return new ImportDataDisplay(showingDisplays, SearchConditionUtil    .getNeedDisplayTableDisplay(CpSettingConstants.TAB_ID_MA_OUTDATA_IMPORT),    createCpConditionArea(), true);  }  private FuncButton importSettingButton = new ImportSettingButton();  private FuncButton importButton = new ImportButton();  private PrintButton printButton = new PrintButton();  private FuncButton printPreviewButton = new PrintPreviewButton();  private PrintSettingButton printSettingButton = new PrintSettingButton();  private FuncButton deleteButton = new DeleteButton();  private FuncButton createButton = new CreateButton();  private FuncButton cancelCreateButton = new CancelCreateButton();  protected void addToolBarComponent(JFuncToolBar toolBar) {    toolBar.setModuleCode("CP");    toolBar.setCompoId(compoId);    toolBar.add(importSettingButton);    toolBar.add(deleteButton);    toolBar.add(importButton);    toolBar.add(createButton);    toolBar.add(cancelCreateButton);    toolBar.add(printButton);    toolBar.add(printPreviewButton);    toolBar.add(printSettingButton);    // 初始化按钮的action事件    importButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent arg0) {        doSave();      }    });    importSettingButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent arg0) {        doImportSetting();      }    });    printButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent arg0) {        doPrint();      }    });    printPreviewButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent arg0) {        doPrintPreview();      }    });    printSettingButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent arg0) {        doPrintSetting();      }    });  }  /**   * 取选择的数据   * @return CheckedList   */  public List getCheckedList() {    List<CpImpRecord> impList = new ArrayList<CpImpRecord>();    JGroupableTable table = importDataDisplay.getActiveTableDisplay().getTable();    MyTableModel model = (MyTableModel) table.getModel();    //Modal的数据    List list = model.getList();    Integer[] checkedRows = table.getCheckedRows();    for (Integer checkedRow : checkedRows) {      int accordDataRow = table.convertRowIndexToModel(checkedRow);      CpImpRecord cpImp = (CpImpRecord) list.get(accordDataRow);      impList.add(cpImp);    }    return impList;  }  private JGroupableTable createImportSettingTable() {    JGroupableTable table = SwingUtil.createTable(JGroupableTable.class);    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);    table.setShowCheckedColumn(false);    table.getTableRowHeader().setVisible(false);    table.setPreferencesKey(getClass().getName() + "_importSetting");    Object[] columnNames = { ndCol, dateCol, orgCodeCol, orgNameCol, coCodeCol, coNameCol,    orgMoneyCol, fundCodeCol,    fundNameCol, baccCodeCol, baccNameCol, outlayCodeCol,    outlayNameCol, payoutCodeCol, payoutNameCol, manageCodeCol,    manageNameCol, balModeCodeCol, balModeNameCol, projectCodeCol, projectNameCol,    receAccNameCol, receBankAccCodeCol, receBankNodeNameCol, payAccNameCol,    payBankAccCodeCol, payBankNodeNameCol, remarkCol };    Object[][] data = { { " ", " ", " ", "  ", "  ", "  ", "  ", "  ", "  ",    "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " ", " ", " ",    " ", " ", " ", " ", " ", " " } };    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);    table.setModel(tableModel);    return table;  }  public static String ndCol = "年度";  public static String dateCol = "单据日期";  public static String coCodeCol = "单位代码";  public static String coNameCol = "单位名称";  public static String orgMoneyCol = "金额";  public static String orgCodeCol = "科室代码";  public static String orgNameCol = "科室名称";  public static String fundCodeCol = "资金性质代码";  public static String fundNameCol = "资金性质";  public static String baccCodeCol = "功能分类代码";  public static String baccNameCol = "功能分类";  public static String outlayCodeCol = "经济分类代码";  public static String outlayNameCol = "经济分类";  public static String payoutCodeCol = "支出类型代码";  public static String payoutNameCol = "支出类型";  public static String manageCodeCol = "管理类型代码";  public static String manageNameCol = "管理类型";  public static String balModeCodeCol = "结算方式代码";  public static String balModeNameCol = "结算方式";  public static String projectCodeCol = "项目代码";  public static String projectNameCol = "项目";  public static String receAccNameCol = "收款人名称";  public static String receBankAccCodeCol = "收款人账号";  public static String receBankNodeNameCol = "收款人银行";  public static String payAccNameCol = "付款人名称";  public static String payBankAccCodeCol = "付款人账号";  public static String payBankNodeNameCol = "付款人银行";  public static String remarkCol = "摘要";  class ImportSettingDialog extends GkBaseDialog {    private static final long serialVersionUID = 6242255262145091836L;    public ImportSettingDialog() {      super(null, Dialog.ModalityType.APPLICATION_MODAL);      this.init();    }    private void init() {      init2();      this.setTitle("导入设置");      this.setSize(UIConstants.DIALOG_3_LEVEL_WIDTH, UIConstants.DIALOG_3_LEVEL_HEIGHT);      this.moveToScreenCenter();      this.setVisible(true);    }    public void init2() {      this.setLayout(new BorderLayout());      JPanel toolBar = new JPanel();      toolBar.setLayout(new FlowLayout(FlowLayout.RIGHT));      JButton okButton = new JButton("确定");      JButton exitButton = new JButton("关闭");      toolBar.add(okButton);      toolBar.add(exitButton);      okButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          list = doImport();          refreshCurrentTabData();          ImportSettingDialog.this.closeDialog();        }      });      exitButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          ImportSettingDialog.this.closeDialog();        }      });      this.add(toolBar, BorderLayout.SOUTH);      JGroupableTable table = createImportSettingTable();      JPanel topPanel = new JPanel();      JPanel downPanel = new JPanel();      createTopField(topPanel);      createDownField(downPanel);      JPanel tempPanel = new JPanel(new BorderLayout());      JScrollPane scrollPane = new JScrollPane();      scrollPane.getViewport().add(table);      scrollPane.getHorizontalScrollBar().setPreferredSize(      new Dimension(0, UIConstants.HORIZONTAL_SCROLLBAR__HEIGHT));      scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "EXCEL数据列",      TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 12), Color.BLUE));      tempPanel.add(topPanel, BorderLayout.NORTH);      tempPanel.add(scrollPane, BorderLayout.CENTER);      tempPanel.add(downPanel, BorderLayout.SOUTH);      this.add(tempPanel, BorderLayout.CENTER);    }  }  private void createDownField(JPanel editPanel) {    final JComboBox fileTypeCombox = new JComboBox(new String[] { "*.xsl", "*.txt" });    final JComboBox fileEmCombox = new JComboBox(new String[] { " , ", " / " });    fileTypeCombox.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        if (fileTypeCombox.getSelectedIndex() == 1) {          fileEmCombox.setEnabled(false);        }      }    });    GridBagLayout gblBasic = new GridBagLayout();    editPanel.setLayout(gblBasic);    int gridx, gridy, gridwidth, gridheight, anchor, fill, ipadx, ipady;    double weightx, weighty;    Insets inset;    gridwidth = 1;    gridheight = 1;    weightx = 1;    weighty = 1;    ipadx = 0;    ipady = 0;    fill = GridBagConstraints.NONE;    inset = new Insets(2, 2, 2, 2);    gridx = 0;    gridy = 0;    anchor = GridBagConstraints.EAST;    JLabel startLabel = new JLabel("文件类型：");    gblBasic.setConstraints(startLabel, new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx,    weighty, anchor, fill, inset, ipadx, ipady));    editPanel.add(startLabel);    gridx = 1;    gridy = 0;    anchor = GridBagConstraints.WEST;    fill = GridBagConstraints.HORIZONTAL;    gblBasic.setConstraints(fileTypeCombox, new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx,    weighty, anchor, fill, inset, ipadx, ipady));    editPanel.add(fileTypeCombox);    gridx = 2;    gridy = 0;    anchor = GridBagConstraints.EAST;    fill = GridBagConstraints.NONE;    JLabel endLabel = new JLabel("文件名称：");    gblBasic.setConstraints(endLabel, new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx,    weighty, anchor, fill, inset, ipadx, ipady));    editPanel.add(endLabel);    gridx = 3;    gridy = 0;    anchor = GridBagConstraints.WEST;    fill = GridBagConstraints.HORIZONTAL;    gblBasic.setConstraints(fileNameField, new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx,    weighty, anchor, fill, inset, ipadx, ipady));    editPanel.add(fileNameField);    gridx = 0;    gridy = 1;    anchor = GridBagConstraints.EAST;    fill = GridBagConstraints.NONE;    JLabel printLabel = new JLabel("文件分隔符：");    gblBasic.setConstraints(printLabel, new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx,    weighty, anchor, fill, inset, ipadx, ipady));    editPanel.add(printLabel);    gridx = 1;    gridy = 1;    anchor = GridBagConstraints.WEST;    fill = GridBagConstraints.HORIZONTAL;    gblBasic.setConstraints(fileEmCombox, new GridBagConstraints(gridx, gridy, gridwidth, gridheight,    weightx, weighty, anchor, fill, inset, ipadx, ipady));    editPanel.add(fileEmCombox);    editPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "",    TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 12), Color.BLUE));  }  private void createTopField(JPanel editPanel) {    AdjustTypeComboBox importTypeBox = new AdjustTypeComboBox("DATA_TYPE_CODE", false);    AdjustTypeComboBox createTypeBox = new AdjustTypeComboBox("CREATE_TYPE_CODE", false);    GridBagLayout gblBasic = new GridBagLayout();    editPanel.setLayout(gblBasic);    int gridx, gridy, gridwidth, gridheight, anchor, fill, ipadx, ipady;    double weightx, weighty;    Insets inset;    gridwidth = 1;    gridheight = 1;    weightx = 1;    weighty = 1;    ipadx = 0;    ipady = 0;    fill = GridBagConstraints.NONE;    inset = new Insets(5, 5, 5, 5);    gridx = 0;    gridy = 0;    anchor = GridBagConstraints.EAST;    JLabel startLabel = new JLabel("导入数据类型：");    gblBasic.setConstraints(startLabel, new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx,    weighty, anchor, fill, inset, ipadx, ipady));    editPanel.add(startLabel);    gridx = 1;    gridy = 0;    anchor = GridBagConstraints.WEST;    fill = GridBagConstraints.HORIZONTAL;    gblBasic.setConstraints(importTypeBox, new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx,    weighty, anchor, fill, inset, ipadx, ipady));    editPanel.add(importTypeBox);    gridx = 2;    gridy = 0;    anchor = GridBagConstraints.EAST;    fill = GridBagConstraints.NONE;    JLabel endLabel = new JLabel("生成单据类型：");    gblBasic.setConstraints(endLabel, new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx,    weighty, anchor, fill, inset, ipadx, ipady));    editPanel.add(endLabel);    gridx = 3;    gridy = 0;    anchor = GridBagConstraints.WEST;    fill = GridBagConstraints.HORIZONTAL;    gblBasic.setConstraints(createTypeBox, new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx,    weighty, anchor, fill, inset, ipadx, ipady));    editPanel.add(createTypeBox);  }  //  private List getCheckedIdList() {  //    List list = new ArrayList();  //    Iterator itr = getCheckedList().iterator();  //    while (itr.hasNext()) {  //      AmApply amApply = (AmApply) itr.next();  //      list.add(amApply.getAmApplyId());  //    }  //    return list;  //  }  private void doImportSetting() {    new ImportSettingDialog();  }  private void doSave() {    List checkedList = getCheckedList();    if (checkedList.size() == 0) {      checkedList = list;    }    if (list.size() == 0) {      JOptionPane.showMessageDialog(this, "没有可导入的数据 ！", "提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    StringBuffer errorInfo = new StringBuffer("");    boolean success = true;    try {      requestMeta.setFuncId(importButton.getFuncId());      cpVoucherServiceDelegate.saveCpImpRecordFN(checkedList, requestMeta);    } catch (BaseException ex) {      errorInfo.append(ex.getMessage());      logger.error(ex.getStackTraceMessage(), ex);      success = false;    } catch (OtherException ex) {      errorInfo.append(ex.getMessage() + "\n");      logger.error(ex.getStackTraceMessage(), ex);      success = false;    } catch (Exception ex) {      errorInfo.append(ex.getMessage());      logger.error(ex.getMessage(), ex);      success = false;    }    if (success) {      JOptionPane.showMessageDialog(this, "导入成功！", "提示", JOptionPane.INFORMATION_MESSAGE);    } else {      JOptionPane.showMessageDialog(this, "导入失败！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);    }    list = null;    refreshCurrentTabData();  }  private List doImport() {    List importList = new ArrayList();    JFileChooser fileChooser = fileNameField.getFileChooser();    ExcelExtractor extractor = new ExcelExtractor(this, fileChooser.getSelectedFile());    if (!extractor.isFileSelected()) {      return null;    }    List<List<String>> excelData = extractor.getData();    JGroupableTable settingTable = this.createImportSettingTable();    int colCount = settingTable.getColumnCount();    Map<String, Integer> colIndexMap = new HashMap<String, Integer>();    for (int i = 0; i < colCount; i++) {      colIndexMap.put(settingTable.getColumnName(i), i);    }    for (List<String> li : excelData) {      CpImpRecord impRecord = new CpImpRecord();      try {        Integer ndIndex = colIndexMap.get(ndCol);        if (ndIndex != null) {          impRecord.setNd(Integer.parseInt(li.get(ndIndex).trim()));        }        Integer dateIndex = colIndexMap.get(dateCol);        if (dateIndex != null) {          impRecord.setBillDate(li.get(dateIndex).trim());        }        Integer orgMoneyIndex = colIndexMap.get(orgMoneyCol);        if (orgMoneyIndex != null) {          try {            impRecord.setOrgMoney(new BigDecimal(li.get(orgMoneyIndex).trim()));            //            impRecord.setOrgMoney(new BigDecimal(li.get(orgMoneyIndex).trim().replaceAll(",", "")));          } catch (Exception e) {            continue;          }        }        Integer coIndex = colIndexMap.get(coCodeCol);        if (coIndex != null) {          impRecord.setCoCode(li.get(coIndex).trim());        }        Integer orgIndex = colIndexMap.get(orgCodeCol);        if (orgIndex != null) {          impRecord.setOrgCode(li.get(orgIndex).trim());        }        Integer fundIndex = colIndexMap.get(fundCodeCol);        if (fundIndex != null) {          impRecord.setFundCode(li.get(fundIndex).trim());        }        Integer baccIndex = colIndexMap.get(baccCodeCol);        if (baccIndex != null) {          impRecord.setBaccCode(li.get(baccIndex).trim());        }        Integer outlayIndex = colIndexMap.get(outlayCodeCol);        if (outlayIndex != null) {          impRecord.setOutlayCode(li.get(outlayIndex).trim());        }        Integer payoutIndex = colIndexMap.get(payoutCodeCol);        if (payoutIndex != null) {          impRecord.setPayoutCode(li.get(payoutIndex).trim());        }        Integer manageIndex = colIndexMap.get(manageCodeCol);        if (manageIndex != null) {          impRecord.setManageCode(li.get(manageIndex).trim());        }        Integer balModeIndex = colIndexMap.get(balModeCodeCol);        if (balModeIndex != null) {          impRecord.setBalModeCode(li.get(balModeIndex).trim());        }        Integer projectIndex = colIndexMap.get(projectCodeCol);        if (projectIndex != null) {          impRecord.setProjectCode(li.get(projectIndex).trim());        }        Integer receaccnameIndex = colIndexMap.get(receAccNameCol);        if (receaccnameIndex != null) {          impRecord.setReceAccName(li.get(receaccnameIndex).trim());        }        Integer recebankacccodeIndex = colIndexMap.get(receBankAccCodeCol);        if (recebankacccodeIndex != null) {          impRecord.setReceBankAccCode(li.get(recebankacccodeIndex).trim());        }        Integer receNameIndex = colIndexMap.get(receBankNodeNameCol);        if (receNameIndex != null) {          impRecord.setReceBankNodeName(li.get(receNameIndex).trim());        }        Integer payAccNameIndex = colIndexMap.get(payAccNameCol);        if (payAccNameIndex != null) {          impRecord.setPayAccName(li.get(payAccNameIndex).trim());        }        Integer payBankAccCodeIndex = colIndexMap.get(payBankAccCodeCol);        if (payBankAccCodeIndex != null) {          impRecord.setPayBankAccCode(li.get(payBankAccCodeIndex).trim());        }        Integer payBankNodeNameIndex = colIndexMap.get(payBankNodeNameCol);        if (payBankNodeNameIndex != null) {          impRecord.setPayBankNodeName(li.get(payBankNodeNameIndex).trim());        }        Integer remarkIndex = colIndexMap.get(remarkCol);        if (remarkIndex != null) {          impRecord.setRemark(li.get(remarkIndex).trim());        }      } catch (java.lang.IndexOutOfBoundsException e) {        JOptionPane.showMessageDialog(this, "读取excel文件出错，可能是excel数据列设置不正确！", "提示",        JOptionPane.INFORMATION_MESSAGE);        return null;      }      if (impRecord.getOrgMoney() != null && impRecord.getOrgMoney().compareTo(new BigDecimal("0")) > 0) {        importList.add(impRecord);      }    }    if (importList.isEmpty()) {      JOptionPane.showMessageDialog(this, "没有可导入的数据！", "提示", JOptionPane.INFORMATION_MESSAGE);      return null;    }    return importList;  }  private void doPrint() {    List printList = getCheckedList();    if (printList.size() == 0) {      JOptionPane.showMessageDialog(this, "请选择需要打印的数据 ！", "提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    requestMeta.setFuncId(this.printButton.getFuncId());    requestMeta.setPageType(this.compoId + "_L");    boolean success = true;    boolean printed = false;    try {      PrintObject printObject = this.baseDataServiceDelegate.genMainBillPrintObjectFN(printList, requestMeta);      if (Printer.print(printObject)) {        printed = true;      }    } catch (Exception e) {      success = false;      logger.error(e.getMessage(), e);      JOptionPane.showMessageDialog(this, "打印出错！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);    }    //    if (success && printed) {    //      this.amApplyServiceDelegate.increasePrintTimes(getCheckedIdList(), requestMeta);    //    }  }  private void doPrintPreview() {    final List printList = getCheckedList();    if (printList.size() == 0) {      JOptionPane.showMessageDialog(this, "请选择需要打印预览的数据 ！", "提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    requestMeta.setFuncId(this.printPreviewButton.getFuncId());    requestMeta.setPageType(this.compoId + "_L");    try {      PrintObject printObject = this.baseDataServiceDelegate.genMainBillPrintObjectFN(printList, requestMeta);      PrintPreviewer.preview(printObject);    } catch (Exception e) {      logger.error(e.getMessage(), e);      JOptionPane.showMessageDialog(this, "打印预览出错！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);    }  }  private void doPrintSetting() {    requestMeta.setFuncId(this.printButton.getFuncId());    requestMeta.setPageType(this.compoId + "_L");    new PrintSettingDialog(requestMeta);  }  public static void main(String[] args) throws Exception {    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    UIManager.getDefaults().put("SplitPaneUI", BigButtonSplitPaneUI.class.getName());    OutDataImportPanel bill = new OutDataImportPanel();    JFrame frame = new JFrame("frame");    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    frame.setSize(800, 600);    frame.setLocationRelativeTo(null);    frame.getContentPane().add(bill);    frame.setVisible(true);  }  public Window getParentWindow() {    return parentWindow;  }  public void setParentWindow(Window parentWindow) {    this.parentWindow = parentWindow;  }}