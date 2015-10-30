package com.ufgov.zc.client.zc.auditsheet;import java.awt.Color;import java.awt.Container;import java.awt.DefaultKeyboardFocusManager;import java.awt.Dialog.ModalityType;import java.awt.Font;import java.awt.Window;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.event.MouseAdapter;import java.awt.event.MouseEvent;import java.beans.PropertyChangeEvent;import java.beans.PropertyChangeListener;import java.math.BigDecimal;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Date;import java.util.HashMap;import java.util.Iterator;import java.util.List;import java.util.Map;import javax.swing.BorderFactory;import javax.swing.JFrame;import javax.swing.JOptionPane;import javax.swing.SwingUtilities;import javax.swing.UIManager;import javax.swing.border.TitledBorder;import javax.swing.table.TableModel;import org.apache.log4j.Logger;import com.ufgov.smartclient.common.DefaultInvokeHandler;import com.ufgov.smartclient.common.UIUtilities;import com.ufgov.smartclient.component.table.JGroupableTable;import com.ufgov.smartclient.component.table.TableRowHeader.CheckedEvent;import com.ufgov.smartclient.component.table.TableRowHeader.CheckedListener;import com.ufgov.smartclient.plaf.BlueLookAndFeel;import com.ufgov.zc.client.common.BillElementMeta;import com.ufgov.zc.client.common.LangTransMeta;import com.ufgov.zc.client.common.MyTableModel;import com.ufgov.zc.client.common.ParentWindowAware;import com.ufgov.zc.client.common.ServiceFactory;import com.ufgov.zc.client.common.UIConstants;import com.ufgov.zc.client.common.WorkEnv;import com.ufgov.zc.client.common.converter.ZcEbAuditSheetToTableModelConverter;import com.ufgov.zc.client.component.GkBaseDialog;import com.ufgov.zc.client.component.GkCommentDialog;import com.ufgov.zc.client.component.JFuncToolBar;import com.ufgov.zc.client.component.button.AddButton;import com.ufgov.zc.client.component.button.CallbackButton;import com.ufgov.zc.client.component.button.FuncButton;import com.ufgov.zc.client.component.button.HelpButton;import com.ufgov.zc.client.component.button.PrintButton;import com.ufgov.zc.client.component.button.PrintPreviewButton;import com.ufgov.zc.client.component.button.PrintSettingButton;import com.ufgov.zc.client.component.button.SendButton;import com.ufgov.zc.client.component.button.SuggestAuditPassButton;import com.ufgov.zc.client.component.button.SuggestAuditPassFGZRButton;import com.ufgov.zc.client.component.button.SuggestAuditPassZZButton;import com.ufgov.zc.client.component.button.TraceButton;import com.ufgov.zc.client.component.button.UnauditButton;import com.ufgov.zc.client.component.button.UntreadButton;import com.ufgov.zc.client.component.event.ValueChangeEvent;import com.ufgov.zc.client.component.event.ValueChangeListener;import com.ufgov.zc.client.component.ui.AbstractDataDisplay;import com.ufgov.zc.client.component.ui.AbstractEditListBill;import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;import com.ufgov.zc.client.component.ui.MultiDataDisplay;import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;import com.ufgov.zc.client.component.ui.SummarizedResultPanel;import com.ufgov.zc.client.component.ui.TableDisplay;import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;import com.ufgov.zc.client.print.PrintSettingDialog;import com.ufgov.zc.client.util.BalanceUtil;import com.ufgov.zc.client.util.ListUtil;import com.ufgov.zc.client.zc.WordFileUtil;import com.ufgov.zc.client.zc.ZcUtil;import com.ufgov.zc.client.zc.flowconsole.DataFlowConsoleCanvas;import com.ufgov.zc.client.zc.ztb.activex.WordPane;import com.ufgov.zc.common.commonbiz.model.SearchCondition;import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.constants.WFConstants;import com.ufgov.zc.common.system.constants.ZcElementConstants;import com.ufgov.zc.common.system.constants.ZcSettingConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.model.AsFile;import com.ufgov.zc.common.system.util.ObjectUtil;import com.ufgov.zc.common.zc.model.ZcEbAuditSheet;import com.ufgov.zc.common.zc.publish.IZcEbAuditSheetServiceDelegate;import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;/** * 批办单，支持直接选择的项目负责人，如果需要通过小组，在选择项目负责人，请参见ZcEbAuditSheetListPanelExtends * @author Administrator * */public class ZcEbAuditSheetListPanel extends AbstractEditListBill implements ParentWindowAware {  private static final Logger logger = Logger.getLogger(ZcEbAuditSheetListPanel.class);  private static final long serialVersionUID = -2554251892166970878L;  static {    LangTransMeta.init("ZC%");  }  protected ZcEbAuditSheetListPanel self = this;  private Window parentWindow;  public String compoId = "ZC_EB_AUDIT_SHEET";  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();  private ElementConditionDto elementConditionDto = new ElementConditionDto();  private AbstractSearchConditionArea topSearchConditionArea;  private AddButton addButton = new AddButton();  //工作流送审  private SendButton sendButton = new SendButton();  // 工作流填写意见审核通过  private FuncButton suggestPassButton = new SuggestAuditPassButton();  // 工作流填写意见审核通过(综合组)  private FuncButton suggestAuditPassZHZButton = new SuggestAuditPassZHZButton();  // 工作流填写意见审核通过(分管主任)  private FuncButton suggestAuditPassFGZRButton = new SuggestAuditPassFGZRButton();  // 工作流填写意见审核通过(组长)  private FuncButton suggestAuditPassZZButton = new SuggestAuditPassZZButton();  // 工作流收回  private FuncButton callbackButton = new CallbackButton();  // 工作流退回  private FuncButton unTreadButton = new UntreadButton();  // 工作流销审  private FuncButton unAuditButton = new UnauditButton();  private PrintButton printButton = new PrintButton();  private FuncButton printPreviewButton = new PrintPreviewButton();  private PrintSettingButton printSettingButton = new PrintSettingButton();  private FuncButton traceButton = new TraceButton();  private HelpButton helpButton = new HelpButton();  private String listTabId = ZcSettingConstants.TAB_ID_ZC_EB_AUDIT_SHEET;  private String listConditionId = ZcSettingConstants.CONDITION_ID_ZC_EB_AUDIT_SHEET;  private IZcEbAuditSheetServiceDelegate zcEbAuditSheetServiceDelegate = (IZcEbAuditSheetServiceDelegate) ServiceFactory.create(  IZcEbAuditSheetServiceDelegate.class, "zcEbAuditSheetServiceDelegate");  private IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,  "baseDataServiceDelegate");  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,    "zcEbBaseServiceDelegate");  public IBaseDataServiceDelegate getBaseDataServiceDelegate() {    return baseDataServiceDelegate;  }  private WordPane wordPane = new WordPane();  public ZcEbAuditSheetListPanel() {    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {      @Override      public List<SearchCondition> execute() throws Exception {        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonListJoinRole(WorkEnv.getInstance()        .getCurrUserId(), listTabId);        return needDisplaySearchConditonList;      }      @Override      public void success(List<SearchCondition> needDisplaySearchConditonList) {        List<TableDisplay> showingDisplays = SearchConditionUtil.getNeedDisplayTableDisplay(needDisplaySearchConditonList);        init(createDataDisplay(showingDisplays), null);//调用父类方法        revalidate();        repaint();      }    });    requestMeta.setCompoId(compoId);  }  protected AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {    return new DataDisplay(SearchConditionUtil.getAllTableDisplay(listTabId), showingDisplays, createTopConditionArea(), true);//true:显示收索条件区 false：不显示收索条件区  }  @SuppressWarnings("unchecked")  protected AbstractSearchConditionArea createTopConditionArea() {    Map defaultValueMap = new HashMap();    topSearchConditionArea = new SaveableSearchConditionArea(listConditionId, null, true, defaultValueMap, null);    return topSearchConditionArea;  }  @Override  protected void addToolBarComponent(JFuncToolBar toolBar) {    toolBar.setModuleCode("ZC");    toolBar.setCompoId(compoId);    toolBar.add(addButton);    //toolBar.add(sendButton);//送审   wangwei  updated 2011-04-19    //    toolBar.add(suggestPassButton);//填写意见审核通过    //toolBar.add(callbackButton);//收回    //toolBar.add(unTreadButton);//退回    //toolBar.add(unAuditButton);//销审    toolBar.add(traceButton);//    toolBar.add(printButton);//    toolBar.add(printPreviewButton);//    toolBar.add(printSettingButton);    toolBar.add(helpButton);//    toolBar.add(traceDataButton);    // 初始化按钮的action事件    addButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doAdd();      }    });    sendButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doSend();      }    });    suggestPassButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doSuggestPass();      }    });    callbackButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doCallBack();      }    });    unTreadButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doUnTread();      }    });    unAuditButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doUnAudit();      }    });    traceButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doTrace();      }    });    printButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent arg0) {        doPrintEntrust();      }    });    printSettingButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent arg0) {        doPrintSetting();      }    });    traceDataButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent arg0) {        doTraceDataButton();      }    });  }  @SuppressWarnings("unchecked")  public void doAdd() {    openEditDialog(new ArrayList(1), -1, topDataDisplay.getActiveTableDisplay().getStatus());  }  private void doSend() {    boolean success = true;    String errorInfo = "";    requestMeta.setFuncId(this.sendButton.getFuncId());    List beanList = this.getCheckedList();    if (beanList.size() == 0) {      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    try {      for (int i = 0; i < beanList.size(); i++) {        ZcEbAuditSheet zcEbAuditSheet = (ZcEbAuditSheet) beanList.get(i);        zcEbAuditSheet.setAuditorId(WorkEnv.getInstance().getCurrUserId());        this.getZcEbAuditSheetServiceDelegate().newCommitFN(zcEbAuditSheet, true, requestMeta);      }    } catch (Exception ex) {      errorInfo += ex.getMessage();      logger.error(ex.getMessage(), ex);      success = false;      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());    }    if (success) {      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);      this.refreshCurrentTabData();    }  }  private void doSuggestPass() {    boolean success = true;    String errorInfo = "";    requestMeta.setFuncId(this.suggestPassButton.getFuncId());    List beanList = this.getCheckedList();    if (beanList.size() == 0) {      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),    ModalityType.APPLICATION_MODAL);    if (commentDialog.cancel) {      return;    }    try {      for (int i = 0; i < beanList.size(); i++) {        ZcEbAuditSheet zcEbAuditSheet = (ZcEbAuditSheet) beanList.get(i);        zcEbAuditSheet.setAuditorId(WorkEnv.getInstance().getCurrUserId());        zcEbAuditSheet.setComment(commentDialog.getComment());        this.getZcEbAuditSheetServiceDelegate().auditFN(zcEbAuditSheet, true, requestMeta);      }    } catch (Exception ex) {      errorInfo += ex.getMessage();      logger.error(ex.getMessage(), ex);      success = false;      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());    }    if (success) {      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);      this.refreshCurrentTabData();    }  }  private void doUnTread() {    boolean success = true;    String errorInfo = "";    requestMeta.setFuncId(this.unTreadButton.getFuncId());    List beanList = this.getCheckedList();    if (beanList.size() == 0) {      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),    ModalityType.APPLICATION_MODAL, "不同意");    if (commentDialog.cancel) {      return;    }    try {      for (int i = 0; i < beanList.size(); i++) {        ZcEbAuditSheet zcEbAuditSheet = (ZcEbAuditSheet) beanList.get(i);        zcEbAuditSheet.setAuditorId(WorkEnv.getInstance().getCurrUserId());        zcEbAuditSheet.setComment(commentDialog.getComment());        this.getZcEbAuditSheetServiceDelegate().untreadFN(zcEbAuditSheet, true, requestMeta);      }    } catch (Exception ex) {      errorInfo += ex.getMessage();      logger.error(ex.getMessage(), ex);      success = false;      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());    }    if (success) {      JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);      this.refreshCurrentTabData();    }  }  private void doUnAudit() {    boolean success = true;    String errorInfo = "";    requestMeta.setFuncId(this.unAuditButton.getFuncId());    List beanList = this.getCheckedList();    if (beanList.size() == 0) {      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    try {      for (int i = 0; i < beanList.size(); i++) {        ZcEbAuditSheet zcEbAuditSheet = (ZcEbAuditSheet) beanList.get(i);        zcEbAuditSheet.setAuditorId(WorkEnv.getInstance().getCurrUserId());        this.getZcEbAuditSheetServiceDelegate().unAuditFN(zcEbAuditSheet, true, requestMeta);      }    } catch (Exception ex) {      errorInfo += ex.getMessage();      logger.error(ex.getMessage(), ex);      success = false;      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());    }    if (success) {      JOptionPane.showMessageDialog(this, "销审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);      this.refreshCurrentTabData();    }  }  private void doTrace() {    List beanList = getCheckedList();    if (beanList.size() == 0) {      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    if (beanList.size() > 1) {      JOptionPane.showMessageDialog(this, "只能选择一条数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    for (int i = 0; i < beanList.size(); i++) {      ZcEbAuditSheet bean = (ZcEbAuditSheet) beanList.get(i);      ZcUtil.showTraceDialog(bean, compoId);    }  }  private void doCallBack() {    boolean success = true;    String errorInfo = "";    requestMeta.setFuncId(this.callbackButton.getFuncId());    List beanList = this.getCheckedList();    if (beanList.size() == 0) {      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    try {      for (int i = 0; i < beanList.size(); i++) {        ZcEbAuditSheet zcEbAuditSheet = (ZcEbAuditSheet) beanList.get(i);        zcEbAuditSheet.setAuditorId(WorkEnv.getInstance().getCurrUserId());        this.getZcEbAuditSheetServiceDelegate().callbackFN(zcEbAuditSheet, true, requestMeta);      }    } catch (Exception e) {      success = false;      logger.error(e.getMessage(), e);      errorInfo += e.getMessage();    }    if (success) {      JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);      this.refreshCurrentTabData();    } else {      JOptionPane.showMessageDialog(this, "收回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);    }  }  private void doPrint() {  }  private void doPrintSetting() {    requestMeta.setFuncId(this.printSettingButton.getFuncId());    requestMeta.setPageType(this.compoId + "_L");    new PrintSettingDialog(requestMeta);  }  private void doPrintEntrust() {    List beanList = this.getCheckedList();    if (beanList.size() == 0) {      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    if (beanList.size() > 1) {      JOptionPane.showMessageDialog(this, "请选择一条数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    ZcEbAuditSheet zcEbAuditSheet = (ZcEbAuditSheet) beanList.get(0);    //获取word模板文件    String wordFilePath = loadMold(zcEbAuditSheet);    //填充word模板    //    replaceBookMarks    final GkBaseDialog baseDialog = new GkBaseDialog() {      public void dispose() {        if (wordPane != null) {          wordPane.closeNotSave();        }        super.dispose();      }    };    Map map = new HashMap();    map.put("SN_CODE", zcEbAuditSheet.getSnCode());    Map result = (Map) zcEbBaseServiceDelegate.queryObject("ZcEbAuditSheet.getEntrustPrintWithView", map, requestMeta);    Iterator<String> it = result.keySet().iterator();    final String[] names = new String[result.size()];    final String[] values = new String[result.size()];    int i = 0;    SimpleDateFormat sdf = new SimpleDateFormat(ZcSettingConstants.SIMPLE_DATE_FORMAT_FULL);    while (it.hasNext()) {      String key = it.next();      String dataStr = null;      Object value = result.get(key);      if (value instanceof Date) {        dataStr = sdf.format(value);        names[i] = key;        values[i++] = dataStr;        continue;      } else if (value instanceof BigDecimal) {        dataStr = ((BigDecimal) value).toString();        names[i] = key;        values[i++] = dataStr;        continue;      } else if (null == value) {        values[i++] = "";        continue;      }      names[i] = key;      values[i++] = (String) result.get(key);    }    baseDialog.add(wordPane);    baseDialog.setSize(UIConstants.DIALOG_1_LEVEL_WIDTH, UIConstants.DIALOG_1_LEVEL_HEIGHT);    baseDialog.setVisible(true);    wordPane.open(wordFilePath);    wordPane.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {      public void propertyChange(PropertyChangeEvent evt) {        // 打开文件完成之后的回调函数        boolean success = (Boolean) evt.getNewValue();        self.firePropertyChange(WordPane.EVENT_NAME_OPEN_CALLBACK, !success, success);      }    });    addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {      public void propertyChange(PropertyChangeEvent evt) {        //打开文件完成之后的回调函数        boolean isSuccess = (Boolean) evt.getNewValue();        removePropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, this);        if (isSuccess) {          wordPane.replaceBookMarks(names, values);          baseDialog.setSize(UIConstants.DIALOG_1_LEVEL_WIDTH + 1, UIConstants.DIALOG_1_LEVEL_HEIGHT + 1);        }      }    });  }  public String loadMold(ZcEbAuditSheet zcEbAuditSheet) {    String fullFileName = "";    String fileExtName = ".doc";    try {      AsFile asFile = baseDataServiceDelegate.downloadFile(ZcSettingConstants.entrustPrintModleFileID, requestMeta);      byte[] fileContent = null;      if (asFile != null && asFile.getFileContent() != null) {        fileContent = asFile.getFileContent();      }      //下载到本地时,使用原来的fileID做新的文件名称      WordFileUtil.setDir("print");      fullFileName = WordFileUtil.createFile(zcEbAuditSheet.getSnCode() + fileExtName, fileContent);    } catch (Exception e) {      logger.error(e.getMessage(), e);    }    return fullFileName;  }  public List getCheckedList() {    List<ZcEbAuditSheet> beanList = new ArrayList<ZcEbAuditSheet>();    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();    MyTableModel model = (MyTableModel) table.getModel();    //Modal的数据    List list = model.getList();    Integer[] checkedRows = table.getCheckedRows();    for (Integer checkedRow : checkedRows) {      int accordDataRow = table.convertRowIndexToModel(checkedRow);      ZcEbAuditSheet bean = (ZcEbAuditSheet) list.get(accordDataRow);      beanList.add(bean);    }    return beanList;  }  @SuppressWarnings("unchecked")  protected TableModel doExecute() {    ZcEbAuditSheetToTableModelConverter mc = new ZcEbAuditSheetToTableModelConverter();    return mc.convertToTableModel(zcEbAuditSheetServiceDelegate.getList(elementConditionDto, requestMeta));  }  protected void doSuccess(TableModel model) {    topDataDisplay.getActiveTableDisplay().setTableModel(model);    setButtonStatus();  }  protected void doLeftClick(MouseEvent e) {  }  @SuppressWarnings("unchecked")  protected void doLeftDbClick(MouseEvent e) {    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();    MyTableModel model = (MyTableModel) table.getModel();    int row = table.getSelectedRow();    List viewList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(model.getList(), table));    openEditDialog((ArrayList) viewList, row, topDataDisplay.getActiveTableDisplay().getStatus());  }  @SuppressWarnings("unchecked")  public void openEditDialog(ArrayList viewList, Integer row, String status) {    new ZcEbAuditSheetDialog(self, viewList, row, status);  }  private void doTraceDataButton() {    List beanList = getCheckedList();    if (beanList.size() == 0) {      JOptionPane.showMessageDialog(this, "请选择一条要进行跟踪的数据！", "错误", JOptionPane.ERROR_MESSAGE);      return;    }    ZcEbAuditSheet sh = (ZcEbAuditSheet) beanList.get(0);    DataFlowConsoleCanvas dfc = new DataFlowConsoleCanvas(sh.getZcEbEntrust().getZcMakeCode(), this.compoId);    dfc.showWindow();  }  private void setButtonStatus() {    String panelId = WFConstants.AUDIT_TAB_STATUS_TODO;    if (topDataDisplay != null && topDataDisplay.getActiveTableDisplay() != null) {      panelId = topDataDisplay.getActiveTableDisplay().getStatus();    }    if (WFConstants.EDIT_TAB_STATUS_DRAFT.equalsIgnoreCase(panelId)) {      traceButton.setVisible(false);      suggestPassButton.setVisible(false);      unTreadButton.setVisible(false);      sendButton.setVisible(true);      addButton.setVisible(true);      printButton.setVisible(true);      printPreviewButton.setVisible(true);      printSettingButton.setVisible(true);      callbackButton.setVisible(false);      unAuditButton.setVisible(false);    } else if (WFConstants.AUDIT_TAB_STATUS_TODO.equalsIgnoreCase(panelId)) {      traceButton.setVisible(true);      sendButton.setVisible(false);      suggestPassButton.setVisible(true);      unTreadButton.setVisible(true);      addButton.setVisible(true);      printButton.setVisible(true);      printPreviewButton.setVisible(true);      printSettingButton.setVisible(true);      callbackButton.setVisible(false);      unAuditButton.setVisible(false);    } else if (WFConstants.AUDIT_TAB_STATUS_DONE.equalsIgnoreCase(panelId)) {      traceButton.setVisible(true);      sendButton.setVisible(false);      suggestPassButton.setVisible(false);      unTreadButton.setVisible(false);      addButton.setVisible(true);      printButton.setVisible(true);      printPreviewButton.setVisible(true);      printSettingButton.setVisible(true);      callbackButton.setVisible(true);    } else if ("exec".equalsIgnoreCase(panelId)) {      traceButton.setVisible(true);      sendButton.setVisible(false);      suggestPassButton.setVisible(false);      unTreadButton.setVisible(false);      addButton.setVisible(true);      printButton.setVisible(true);      printPreviewButton.setVisible(true);      printSettingButton.setVisible(true);      callbackButton.setVisible(false);      unAuditButton.setVisible(true);    } else if (WFConstants.AUDIT_TAB_STATUS_ALL.equalsIgnoreCase(panelId)) {      traceButton.setVisible(true);      sendButton.setVisible(false);      suggestPassButton.setVisible(false);      unTreadButton.setVisible(false);      addButton.setVisible(true);      printButton.setVisible(true);      printPreviewButton.setVisible(true);      printSettingButton.setVisible(true);      callbackButton.setVisible(false);      unAuditButton.setVisible(false);    } else if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equalsIgnoreCase(panelId)) {      traceButton.setVisible(false);      sendButton.setVisible(false);      suggestPassButton.setVisible(false);      unTreadButton.setVisible(false);      addButton.setVisible(false);      printButton.setVisible(false);      printPreviewButton.setVisible(false);      printSettingButton.setVisible(false);      callbackButton.setVisible(false);      unAuditButton.setVisible(false);    }  }  public void refreshCurrentTabData() {    topSearchConditionArea.doSearch();  }  public static void main(String[] args) throws Exception {    SwingUtilities.invokeLater(new Runnable() {      public void run() {        try {          //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());          UIManager.setLookAndFeel(new BlueLookAndFeel());        } catch (Exception e) {          e.printStackTrace();        }        ZcEbAuditSheetListPanel bill = new ZcEbAuditSheetListPanel();        JFrame frame = new JFrame("frame");        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        frame.setSize(800, 600);        frame.setLocationRelativeTo(null);        frame.getContentPane().add(bill);        frame.setVisible(true);      }    });  }  @SuppressWarnings("unchecked")  public void refreshCurrentTabData(List dataList) {    ZcEbAuditSheetToTableModelConverter mc = new ZcEbAuditSheetToTableModelConverter();    topDataDisplay.getActiveTableDisplay().getTable().setModel(mc.convertToTableModel(dataList));  }  private BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(compoId);  public BillElementMeta getBillElementMeta() {    return billElementMeta;  }  protected String getTitle() {    return LangTransMeta.translate(compoId);  }  public Window getParentWindow() {    return parentWindow;  }  public void setParentWindow(Window parentWindow) {    this.parentWindow = parentWindow;  }  public IZcEbAuditSheetServiceDelegate getZcEbAuditSheetServiceDelegate() {    return zcEbAuditSheetServiceDelegate;  }  public void setZcEbAuditSheetServiceDelegate(IZcEbAuditSheetServiceDelegate zcEbAuditSheetServiceDelegate) {    this.zcEbAuditSheetServiceDelegate = zcEbAuditSheetServiceDelegate;  }  protected final class DataDisplay extends MultiDataDisplay {    private static final long serialVersionUID = 8838123294320983836L;    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,    boolean showConditionArea) {      super(displays, showingDisplays, conditionArea, showConditionArea, listTabId);      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), getTitle(), TitledBorder.CENTER, TitledBorder.TOP, new Font(      "宋体", Font.BOLD, 15), Color.BLUE));    }    protected void preprocessShowingTableDisplay(List<TableDisplay> showingTableDisplays) {      for (final TableDisplay d : showingTableDisplays) {        final JGroupableTable table = d.getTable();        table.addMouseListener(new MouseAdapter() {          public void mouseClicked(MouseEvent e) {            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {              doLeftDbClick(e);            } else if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {              doLeftClick(e);            }          }        });        //合计信息        List sumFields = new ArrayList();        sumFields.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM));        d.setSummarizedResultPanel(new SummarizedResultPanel(d.getTable(), sumFields));        final JGroupableTable jTable = d.getTable();        jTable.getTableRowHeader().addCheckedListener(new CheckedListener() {          public void checkedChanged(CheckedEvent e) {            d.getSummarizedResultPanel().clearSummarizedResult();            d.getSummarizedResultPanel().refreshSummarizedResult();          }        });        d.addSorterValueChangeListener(new ValueChangeListener() {          public void valueChanged(ValueChangeEvent e) {            d.getSummarizedResultPanel().clearSummarizedResult();            d.getSummarizedResultPanel().refreshSummarizedResult();          }        });      }    }    @Override    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {      elementConditionDto.setWfcompoId(compoId);      elementConditionDto.setExecutor(WorkEnv.getInstance().getCurrUserId());      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());      elementConditionDto.setStatus(tableDisplay.getStatus());      elementConditionDto.setMonth(BalanceUtil.getMonthIdBySysOption());      for (AbstractSearchConditionItem item : searchConditionItems) {        item.putToElementConditionDto(elementConditionDto);      }      final Container c = tableDisplay.getTable().getParent();      UIUtilities.asyncInvoke(new DefaultInvokeHandler<TableModel>() {        @Override        public void before() {          assert c != null;          installLoadingComponent(c);        }        @Override        public void after() {          assert c != null;          unInstallLoadingComponent(c);          c.add(tableDisplay.getTable());        }        @Override        public TableModel execute() throws Exception {          return doExecute();        }        @Override        public void success(TableModel model) {          doSuccess(model);          tableDisplay.getSummarizedResultPanel().clearSummarizedResult();          tableDisplay.getSummarizedResultPanel().refreshSummarizedResult();        }      });    }  }}