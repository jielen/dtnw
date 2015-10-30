/**
 * 
 */
package com.ufgov.zc.client.zc.eval.result;

import java.awt.Color;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.DefaultInvokeHandler;
import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbEvalReportToTableModelConverter;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.GkCommentUntreadDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;
import com.ufgov.zc.client.print.PrintPreviewer;
import com.ufgov.zc.client.print.PrintSettingDialog;
import com.ufgov.zc.client.print.Printer;
import com.ufgov.zc.client.util.ListUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.flowconsole.DataFlowConsoleCanvas;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.dto.PrintObject;
import com.ufgov.zc.common.system.exception.BaseException;
import com.ufgov.zc.common.system.exception.BusinessException;
import com.ufgov.zc.common.system.exception.OtherException;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcEbEvalReport;
import com.ufgov.zc.common.zc.publish.IZcEbEvalServiceDelegate;

/**
 * 离线评标结果录入
 * @author Administrator
 *
 */
public class ZcEbEvalReportOffLineListPanel extends AbstractEditListBill implements ParentWindowAware {

  private static final long serialVersionUID = 1919181448112477360L;

  private static final Logger logger = Logger.getLogger(ZcEbEvalReportListPanel.class);

  private ZcEbEvalReportOffLineListPanel self = this;

  private Window parentWindow;


  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private ElementConditionDto elementConditionDto = new ElementConditionDto();

  private IZcEbEvalServiceDelegate zcEbEvalServiceDelegate = (IZcEbEvalServiceDelegate) ServiceFactory.create(IZcEbEvalServiceDelegate.class,

  "zcEbEvalServiceDelegate");

  private IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,

  "baseDataServiceDelegate");

  static {

    LangTransMeta.init("ZC%");

  }

   
  protected String getCompoId() {
    // TODO Auto-generated method stub
    return "ZC_EB_EVAL_REPORT_OFF_LINE";
  }

  public Window getParentWindow() {

    return parentWindow;

  }

  public void setParentWindow(Window parentWindow) {

    this.parentWindow = parentWindow;

  }

  /**

   * 构造函数

   */

  public ZcEbEvalReportOffLineListPanel() {

    init();
    requestMeta.setCompoId(getCompoId());
  }

  private void init() {

    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {

      public List<SearchCondition> execute() throws Exception {

        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonListJoinRole(WorkEnv.getInstance()

        .getCurrUserId(), ZcSettingConstants.TAB_ID_ZC_EB_EVAL_REPORT);

        return needDisplaySearchConditonList;

      }

      public void success(List<SearchCondition> needDisplaySearchConditonList) {

        List<TableDisplay> showingDisplays = SearchConditionUtil.getNeedDisplayTableDisplay(needDisplaySearchConditonList);

        init(createDataDisplay(showingDisplays), null);//调用父类方法

        revalidate();

        repaint();

      }

    });

  }

  private final class DataDisplay extends MultiDataDisplay {

    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,

    boolean showConditionArea) {

      super(displays, showingDisplays, conditionArea, showConditionArea, ZcSettingConstants.TAB_ID_ZC_EB_EVAL_REPORT);

      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate(getCompoId()), TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体",

      Font.BOLD, 15), Color.BLUE));

    }

    @Override
    protected void preprocessShowingTableDisplay(List<TableDisplay> showingTableDisplays) {

      for (final TableDisplay d : showingTableDisplays) {

        final JGroupableTable table = d.getTable();

        table.addMouseListener(new MouseAdapter() {

          @Override
          @SuppressWarnings("unchecked")
          public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {

              JGroupableTable table = d.getTable();

              String tabStatus = d.getStatus();

              MyTableModel model = (MyTableModel) table.getModel();

              int row = table.getSelectedRow();

              List viewList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(model.getList(), table));

              new ZcEbReportOffLineDialog(self, viewList, row, tabStatus);

            }

          }

        });

      }

    }

    @Override
    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {

      elementConditionDto.setWfcompoId(getCompoId());

      elementConditionDto.setExecutor(WorkEnv.getInstance().getCurrUserId());

      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());

      elementConditionDto.setStatus(tableDisplay.getStatus());
      
      elementConditionDto.setZcText0("offLine");

      for (AbstractSearchConditionItem item : searchConditionItems) {

        item.putToElementConditionDto(elementConditionDto);

      }

      final Container c = tableDisplay.getTable().getParent();

      UIUtilities.asyncInvoke(new DefaultInvokeHandler<TableModel>() {

        @Override
        public void before() {

          assert c != null;

          installLoadingComponent(c);

        }

        @Override
        public void after() {

          assert c != null;

          unInstallLoadingComponent(c);

          c.add(tableDisplay.getTable());

        }

        public TableModel execute() throws Exception {

          return new ZcEbEvalReportToTableModelConverter().convertToTableModel(self.zcEbEvalServiceDelegate.getZcEbEvalReportList(

          elementConditionDto, requestMeta));
        }

        public void success(TableModel model) {

          tableDisplay.setTableModel(model);

          setButtonsVisiable();

        }

      });

    }

  }

  private AbstractSearchConditionArea topSearchConditionArea;

  @SuppressWarnings("unchecked")
  private AbstractSearchConditionArea createTopConditionArea() {

    Map defaultValueMap = new HashMap();

    topSearchConditionArea = new SaveableSearchConditionArea(ZcSettingConstants.CONDITION_ID_ZC_EB_EVAL_REPORT, null, false, defaultValueMap, null);

    return topSearchConditionArea;

  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {

    return new DataDisplay(SearchConditionUtil.getAllTableDisplayJoinRole(WorkEnv.getInstance().getCurrUserId(),

    ZcSettingConstants.TAB_ID_ZC_EB_EVAL_REPORT), showingDisplays, createTopConditionArea(), true);//true:显示收索条件区 false：不显示收索条件区

  }

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(getCompoId());

    toolBar.add(addButton);

    toolBar.add(deleteButton);

    toolBar.add(sendButton);

    //    toolBar.add(isSendToNextButton);

    //    toolBar.add(auditPassButton);

    //    toolBar.add(suggestPassButton);

    //    toolBar.add(auditFinalButton);

    //    toolBar.add(callbackButton);

    //    toolBar.add(unTreadButton);//退回

    toolBar.add(traceButton);

    //toolBar.add(printButton);

    //toolBar.add(printPreviewButton);

    //toolBar.add(printSettingButton);

    toolBar.add(helpButton);

//    toolBar.add(traceDataButton);

    // 初始化按钮的action事件

    addButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doAdd();
      }

    });

    sendButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSend();

      }

    });

    isSendToNextButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSendNext();

      }

    });

    auditPassButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doAuditPass();

      }

    });

    suggestPassButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSuggestPass();

      }

    });

    deleteButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doDelete();

      }

    });

    traceButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doTrace();

      }

    });

    callbackButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doCallBack();

      }

    });

    unTreadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doUnTread();

      }

    });

    printButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        doPrint();

      }

    });

    printPreviewButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        doPrintPreview();

      }

    });

    printSettingButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        doPrintSetting();

      }

    });

    auditFinalButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doAuditFinal();

      }

    });

    traceDataButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        doTraceDataButton();

      }

    });

  }

  private void doAuditFinal() {

    List beanList = getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    executeAudit(beanList, ZcSettingConstants.IS_GOON_AUDIT_YES, auditFinalButton);

  }

  public void refreshCurrentTabData() {

    topSearchConditionArea.doSearch();

  }

  @SuppressWarnings("unchecked")
  public List<ZcEbEvalReport> getCheckedList() {

    List<ZcEbEvalReport> beanList = new ArrayList<ZcEbEvalReport>();

    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();

    MyTableModel model = (MyTableModel) table.getModel();

    //Modal的数据

    List list = model.getList();

    Integer[] checkedRows = table.getCheckedRows();

    for (Integer checkedRow : checkedRows) {

      int accordDataRow = table.convertRowIndexToModel(checkedRow);

      ZcEbEvalReport bean = (ZcEbEvalReport) list.get(accordDataRow);

      beanList.add(bean);

    }

    return beanList;

  }

  /*

   * 填写意见审核

   */

  private void doSuggestPass() {

    List ZcEbEvalReportList = getCheckedList();

    if (ZcEbEvalReportList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择需要送审的数据!", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    boolean success = true;

    try {

      GkCommentDialog commentDialog = new GkCommentDialog(this.getParentWindow(), ModalityType.APPLICATION_MODAL);

      if (commentDialog.cancel) {

        return;

      }

      String comment = commentDialog.getComment();

      for (int i = 0; i < ZcEbEvalReportList.size(); i++) {

        ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) ZcEbEvalReportList.get(i);

        zcEbEvalReport.setComment(comment);

        if (WorkEnv.getInstance().containRole(AsOptionMeta.getOptVal("OPT_ZC_CGZX_FZR_ROLE"))) {

          zcEbEvalReport.setIsGoonAudit(0);

        }

      }

      requestMeta.setFuncId(this.suggestPassButton.getFuncId());

      zcEbEvalServiceDelegate.auditFN(ZcEbEvalReportList, requestMeta);

    } catch (BaseException ex) {

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshCurrentTabData();

    }

  }

  private void doAdd() {

    new ZcEbReportOffLineDialog(self, new ArrayList(), this.topDataDisplay.getActiveTableDisplay().getTable().getRowCount(), topDataDisplay

    .getActiveTableDisplay().getStatus());

  }

  private void doSend() {

    List ZcEbEvalReportList = getCheckedList();

    if (ZcEbEvalReportList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择需要送审的数据!", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    boolean success = true;

    try {

      GkCommentDialog commentDialog = new GkCommentDialog(this.getParentWindow(), ModalityType.APPLICATION_MODAL);

      if (commentDialog.cancel) {

        return;

      }

      String comment = commentDialog.getComment();

      for (int i = 0; i < ZcEbEvalReportList.size(); i++) {

        ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) ZcEbEvalReportList.get(i);

        zcEbEvalReport.setComment(comment);

      }

      requestMeta.setFuncId(this.sendButton.getFuncId());

      zcEbEvalServiceDelegate.newCommitFN(ZcEbEvalReportList, requestMeta);

    } catch (BaseException ex) {

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshCurrentTabData();

    }

  }

  /**

   * 是否送主任审核

   */

  private void doSendNext() {

    List beanList = getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    executeAudit(beanList, ZcSettingConstants.IS_GOON_AUDIT_YES, isSendToNextButton);

  }

  private void executeAudit(List beanList, int isGoonAudit, FuncButton button) {

    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    boolean success = true;

    String errorInfo = "";

    try {

      String comment = commentDialog.getComment();

      for (int i = 0; i < beanList.size(); i++) {

        ZcEbEvalReport report = (ZcEbEvalReport) beanList.get(i);

        report.setIsGoonAudit(isGoonAudit);

        report.setComment(comment);

        report.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      }

      requestMeta.setFuncId(button.getFuncId());

      zcEbEvalServiceDelegate.updateZcEbEvalStatus(beanList, requestMeta);

      zcEbEvalServiceDelegate.auditFN(beanList, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      refreshCurrentTabData();

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

    } else {

      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  private void doAuditPass() {

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    executeAudit(beanList, ZcSettingConstants.IS_GOON_AUDIT_NO, auditPassButton);

  }

  private void doDelete() {

    if (getCheckedList().size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择需要删除的数据!", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int result = JOptionPane.showConfirmDialog(self, "是否要删除选中的数据?", "删除确认", JOptionPane.YES_NO_OPTION);

    if (result != JOptionPane.YES_OPTION) {

      return;

    }

    boolean success = true;

    try {

      zcEbEvalServiceDelegate.deleteZcEbEvalReportFN(getCheckedList(), requestMeta);

    } catch (BaseException ex) {

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshCurrentTabData();

    }

  }

  @SuppressWarnings("unchecked")
  private void doTrace() {

    List beanList = getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    if (beanList.size() > 1) {

      JOptionPane.showMessageDialog(this, "只能选择一条数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    for (int i = 0; i < beanList.size(); i++) {

      ZcEbEvalReport bean = (ZcEbEvalReport) beanList.get(i);

      ZcUtil.showTraceDialog(bean, getCompoId());

    }

  }

  private void doCallBack() {

    String returnInfo = "";

    boolean success = true;

    try {

      List beanList = getCheckedList();

      if (beanList.size() == 0) {

        JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

        return;

      }

      requestMeta.setFuncId(callbackButton.getFuncId());

      zcEbEvalServiceDelegate.callbackZcEbEvalReportFN(beanList, requestMeta);

    } catch (BusinessException be) {

      returnInfo = be.getMessage();

      success = false;

    } catch (Exception ex) {

      returnInfo = ex.getMessage();

      success = false;

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "收回成功！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "收回失败 ！\n" + returnInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  public void doUnTread() {

    List ZcEbEvalReportList = getCheckedList();

    if (ZcEbEvalReportList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择需要退回的数据!", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    boolean success = true;

    try {

      requestMeta.setFuncId(unTreadButton.getFuncId());

      GkCommentUntreadDialog commentDialog = new GkCommentUntreadDialog(this.getParentWindow(), ModalityType.APPLICATION_MODAL);

      if (commentDialog.cancel) {

        return;

      }

      String comment = commentDialog.getComment();

      for (int i = 0; i < ZcEbEvalReportList.size(); i++) {

        ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) ZcEbEvalReportList.get(i);

        zcEbEvalReport.setComment(comment);

      }

      zcEbEvalServiceDelegate.untreadFN(ZcEbEvalReportList, requestMeta);

    } catch (BaseException ex) {

      success = false;

      logger.error(ex.getStackTraceMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    } catch (OtherException ex) {

      success = false;

      logger.error(ex.getStackTraceMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    } catch (Exception ex) {

      success = false;

      logger.error(ex.getMessage(), ex);

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshCurrentTabData();

    }

  }

  @SuppressWarnings("unchecked")
  private void doPrint() {

    List printList = getCheckedList();

    if (printList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择需要打印的数据 ！", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    requestMeta.setFuncId(this.printButton.getFuncId());

    requestMeta.setPageType(getCompoId() + "_L");

    boolean success = true;

    boolean printed = false;

    try {

      PrintObject printObject = this.baseDataServiceDelegate.genMainBillPrintObjectFN(printList, requestMeta);

      if (Printer.print(printObject)) {

        printed = true;

      }

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "打印出错！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

    }

    if (success && printed) {

    }

  }

  @SuppressWarnings("unchecked")
  private void doPrintPreview() {

    final List printList = getCheckedList();

    if (printList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择需要打印预览的数据 ！", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    requestMeta.setFuncId(this.printPreviewButton.getFuncId());

    requestMeta.setPageType(getCompoId() + "_L");

    try {

      PrintObject printObject = this.baseDataServiceDelegate.genMainBillPrintObjectFN(printList, requestMeta);

      PrintPreviewer previewer = new PrintPreviewer(printObject) {

        @Override
        protected void afterSuccessPrint() {

        }

      };

      previewer.preview();

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "打印预览出错！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  public BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(getCompoId());

  public BillElementMeta getBillElementMeta() {

    return billElementMeta;

  }

  private void doPrintSetting() {

    requestMeta.setFuncId(this.printSettingButton.getFuncId());

    requestMeta.setPageType(this.getCompoId() + "_L");

    new PrintSettingDialog(requestMeta);

  }

  private void doTraceDataButton() {

    List beanList = getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择一条要进行跟踪的数据！", "错误", JOptionPane.ERROR_MESSAGE);

      return;

    }

    ZcEbEvalReport sh = (ZcEbEvalReport) beanList.get(0);

    DataFlowConsoleCanvas dfc = new DataFlowConsoleCanvas(sh.getProjCode(), this.getCompoId());

    dfc.showWindow();

  }

  public static void main(String[] args) throws Exception {

    SwingUtilities.invokeLater(new Runnable() {

      public void run() {

        try {

          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

          UIManager.setLookAndFeel(new BlueLookAndFeel());

        } catch (Exception e) {

          e.printStackTrace();

        }

        ZcEbEvalReportOffLineListPanel bill = new ZcEbEvalReportOffLineListPanel();

        JFrame frame = new JFrame(LangTransMeta.translate(bill.getCompoId()));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 600);

        frame.setLocationRelativeTo(null);

        frame.getContentPane().add(bill);

        frame.setVisible(true);

      }

    });

  }

}
