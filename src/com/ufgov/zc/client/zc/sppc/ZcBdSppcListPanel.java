package com.ufgov.zc.client.zc.sppc;

import java.awt.Color;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Font;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
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
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcBdSppcToTableModelConverter;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.EnableButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.InvalidButton;
import com.ufgov.zc.client.component.button.PauseEvalExpertButton;
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
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.dto.PrintObject;
import com.ufgov.zc.common.system.exception.BaseException;
import com.ufgov.zc.common.system.exception.DataAlreadyDeletedException;
import com.ufgov.zc.common.system.exception.OtherException;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcBdSppc;
import com.ufgov.zc.common.zc.publish.IZcBdSppcServiceDelegate;

/**
* @ClassName: ZcBdSppcListPanel
* @Description: 协议采购商品批次
* @date: 2012-03-21
* @version: V1.0 
* @since: 1.0
* @author: shenwanchao
* @modify: 
*/
public class ZcBdSppcListPanel extends AbstractEditListBill implements ParentWindowAware {
  private static final Logger logger = Logger.getLogger(ZcBdSppcListPanel.class);

  public static final String compoId = "ZC_XYGH_SPPC";

  private Window parentWindow;

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(compoId);

  private AbstractSearchConditionArea topSearchConditionArea;

  private FuncButton unAuditButton = new PauseEvalExpertButton();

  private FuncButton auditPassButton = new EnableButton();

  private FuncButton cancelButton = new InvalidButton();;

  public Window getParentWindow() {
    return parentWindow;
  }

  public void setParentWindow(Window parentWindow) {
    this.parentWindow = parentWindow;
  }

  public BillElementMeta getBillElementMeta() {
    return billElementMeta;
  }

  public RequestMeta getRequestMeta() {
    return requestMeta;
  }

  private ElementConditionDto elementConditionDto = new ElementConditionDto();

  public IZcBdSppcServiceDelegate zcBdSppcServiceDelegate = (IZcBdSppcServiceDelegate) ServiceFactory.create(IZcBdSppcServiceDelegate.class,
    "zcBdSppcServiceDelegate");

  public IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
    "baseDataServiceDelegate");

  public IZcBdSppcServiceDelegate getZcBdSppcServiceDelegate() {
    return zcBdSppcServiceDelegate;
  }

  public void setZcBdSppcServiceDelegate(IZcBdSppcServiceDelegate zcBdSppcServiceDelegate) {
    this.zcBdSppcServiceDelegate = zcBdSppcServiceDelegate;
  }

  private final class DataDisplay extends MultiDataDisplay {
    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,
      boolean showConditionArea) {
      super(displays, showingDisplays, conditionArea, showConditionArea, ZcSettingConstants.TAB_ID_ZC_XYGH_SPPC);
      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate("ZC_XYGH_SPCC_TITLE"),
        TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
    }

    @Override
    protected void preprocessShowingTableDisplay(List<TableDisplay> showingTableDisplays) {
      for (final TableDisplay d : showingTableDisplays) {
        final JGroupableTable table = d.getTable();
        table.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
              String tabStatus = d.getStatus();
              JGroupableTable table = d.getTable();
              MyTableModel model = (MyTableModel) table.getModel();
              int row = table.getSelectedRow();
              List viewList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(model.getList(), table));
              new ZcBdSppcDialog(ZcBdSppcListPanel.this, viewList, row, tabStatus);
            }
          }
        });
      }
    }

    @Override
    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {
      elementConditionDto.setWfcompoId(compoId);
      elementConditionDto.setExecutor(WorkEnv.getInstance().getCurrUserId());
      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());
      elementConditionDto.setStatus(tableDisplay.getStatus());
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

        @Override
        public TableModel execute() throws Exception {
          return ZcBdSppcToTableModelConverter.convertToTableModel(ZcBdSppcListPanel.this.zcBdSppcServiceDelegate.getZcBdSppcList(
            elementConditionDto, requestMeta));
        }

        @Override
        public void success(TableModel model) {

          tableDisplay.setTableModel(model);

          setButtonStatus();
        }
      });
    }
  }

  static {
    LangTransMeta.init("ZC%");
  }

  /**
   * 构造函数
   */
  public ZcBdSppcListPanel() {
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()
          .getCurrUserId(), ZcSettingConstants.TAB_ID_ZC_XYGH_SPPC);
        return needDisplaySearchConditonList;
      }

      @Override
      public void success(List<SearchCondition> needDisplaySearchConditonList) {
        List<TableDisplay> showingDisplays = SearchConditionUtil.getNeedDisplayTableDisplay(needDisplaySearchConditonList);
        init(createDataDisplay(showingDisplays), null);//调用父类方法
        revalidate();
        repaint();
      }
    });
    requestMeta.setCompoId(compoId);
  }

  /**
   * list页面顶部查询条件
   * @return
   */
  private AbstractSearchConditionArea createTopConditionArea() {
    Map defaultValueMap = new HashMap();
    topSearchConditionArea = new SaveableSearchConditionArea(ZcSettingConstants.CON_ID_ZC_XYGH_SPPC, null, true, defaultValueMap, null);
    return topSearchConditionArea;
  }

  /**
   * 分页也签
   * @param showingDisplays
   * @return
   */
  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {
    return new DataDisplay(SearchConditionUtil.getAllTableDisplay(ZcSettingConstants.TAB_ID_ZC_XYGH_SPPC), showingDisplays, createTopConditionArea(),
      true);//true:显示收索条件区 false：不显示收索条件区
  }

  /**
   * 为工具条添加按钮，即list页面的顶部的按钮
   * 
   */
  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(compoId);
    toolBar.add(addButton);
    toolBar.add(deleteButton);
    toolBar.add(cancelButton);
    toolBar.add(unAuditButton);
    toolBar.add(auditPassButton);
    toolBar.add(printButton);
    toolBar.add(printPreviewButton);
    toolBar.add(printSettingButton);

    // 初始化按钮的action事件
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doAdd();
      }
    });
    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doDelete();
      }
    });
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doCancel();
      }
    });
    unAuditButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doUnAudit();
      }
    });
    auditPassButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doAuditPass();
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
  }

  /**
   * 
   * 设置按钮的状态，根据list页面的Tab页签按钮显示可用不可用
   */
  private void setButtonStatus() {
    String panelId = WFConstants.EDIT_TAB_STATUS_DRAFT;
    if (topDataDisplay != null && topDataDisplay.getActiveTableDisplay() != null) {
      panelId = topDataDisplay.getActiveTableDisplay().getStatus();
    }

    addButton.setEnabled(true);
    printButton.setEnabled(true);
    printPreviewButton.setEnabled(false);
    printSettingButton.setEnabled(true);

    if (WFConstants.EDIT_TAB_STATUS_DRAFT.equalsIgnoreCase(panelId)) {
      deleteButton.setEnabled(true);
      cancelButton.setEnabled(false);
      unAuditButton.setEnabled(false);
      auditPassButton.setEnabled(true);
    } else if (WFConstants.EDIT_TAB_STATUS_ENABLE.equalsIgnoreCase(panelId)) {
      deleteButton.setEnabled(false);
      cancelButton.setEnabled(true);
      unAuditButton.setEnabled(true);
      auditPassButton.setEnabled(false);
    } else if (WFConstants.EDIT_TAB_STATUS_UNABLE.equalsIgnoreCase(panelId)) {
      deleteButton.setEnabled(false);
      cancelButton.setEnabled(true);
      unAuditButton.setEnabled(false);
      auditPassButton.setEnabled(true);
    } else if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equalsIgnoreCase(panelId)) {
      deleteButton.setEnabled(false);
      cancelButton.setEnabled(false);
      unAuditButton.setEnabled(false);
      auditPassButton.setEnabled(false);
    } else if (WFConstants.AUDIT_TAB_STATUS_ALL.equalsIgnoreCase(panelId)) {
      deleteButton.setEnabled(false);
      cancelButton.setEnabled(false);
      unAuditButton.setEnabled(false);
      auditPassButton.setEnabled(false);
    }
  }

  /**
   * 刷新方法
   */
  public void refreshCurrentTabData() {
    topSearchConditionArea.doSearch();
  }

  /**
   * 刷新当前数据
   * @param beanList
   */
  public void refreshCurrentTabData(List beanList) {
    topDataDisplay.getActiveTableDisplay().getTable().setModel(ZcBdSppcToTableModelConverter.convertToTableModel(beanList));
  }

  /**
   * 获的数据bean的list
   * @return
   */
  public List<ZcBdSppc> getCheckedList() {
    List<ZcBdSppc> beanList = new ArrayList<ZcBdSppc>();
    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();
    MyTableModel model = (MyTableModel) table.getModel();
    List<ZcBdSppc> list = model.getList();
    Integer[] checkedRows = table.getCheckedRows();
    for (Integer checkedRow : checkedRows) {
      int accordDataRow = table.convertRowIndexToModel(checkedRow);
      ZcBdSppc bean = list.get(accordDataRow);
      beanList.add(bean);
    }
    return beanList;
  }

  /***********************************************************************************************
   * 下面用来填写按钮的方法
   ***********************************************************************************************/
  /**
   * 新增方法
   */
  public void doAdd() {
    new ZcBdSppcDialog(ZcBdSppcListPanel.this, new ArrayList(), this.topDataDisplay.getActiveTableDisplay().getTable().getRowCount(), topDataDisplay

    .getActiveTableDisplay().getStatus());

  }

  /**
   * 删除方法
   */
  private void doDelete() {
    List checkedList = getCheckedList();
    if (checkedList.size() == 0) {
      JOptionPane.showMessageDialog(this, "请选择需要删除的数据!", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    int result = JOptionPane.showConfirmDialog(this, "是否要删除选中的数据?", "删除确认", JOptionPane.YES_NO_OPTION);
    if (result != JOptionPane.YES_OPTION) {
      return;
    }
    StringBuffer errorInfo = new StringBuffer("");
    boolean success = true;
    requestMeta.setFuncId(deleteButton.getFuncId());
    try {
      for (int i = 0; i < checkedList.size(); i++) {
        ZcBdSppc sppc = (ZcBdSppc) checkedList.get(i);
        zcBdSppcServiceDelegate.deleteByPrimaryKey(sppc.getZcSppcID(), requestMeta);
      }
    } catch (DataAlreadyDeletedException ex) {
      errorInfo.append(ex.getMessage() + "\n");
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
    } catch (BaseException ex) {
      errorInfo.append(ex.getMessage() + "\n");
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
    } catch (OtherException ex) {
      errorInfo.append(ex.getMessage() + "\n");
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
    } catch (Exception ex) {
      errorInfo.append(ex.getMessage() + "\n");
      logger.error(ex.getMessage(), ex);
      success = false;
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "删除错误!\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * 作废商品批次方法
   */
  private void doCancel() {
    List beanList = this.getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    requestMeta.setFuncId(this.auditPassButton.getFuncId());

//    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
//      ModalityType.APPLICATION_MODAL);
//    if (commentDialog.cancel) {
//      return;
//    }
    
    int num = JOptionPane.showConfirmDialog(this, "确认作废本次商品批次?", "作废确认", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;
    String errorInfo = "";
    try {
      for (int i = 0; i < beanList.size(); i++) {
        ZcBdSppc zcBdSppc = (ZcBdSppc) beanList.get(i);
        zcBdSppc.setZcSppcStatus("cancel");
        zcBdSppc.setZcOperCode(requestMeta.getSvUserID());
        //zcBdSppc.setComment(commentDialog.getComment());
        this.zcBdSppcServiceDelegate.updateZcBdSppcByPrimaryKey(zcBdSppc, requestMeta);
      }
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "作废商品批次成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "作废商品批次失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * 暂停方法
   */
  private void doUnAudit() {
    List beanList = this.getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    requestMeta.setFuncId(this.auditPassButton.getFuncId());

//    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
//      ModalityType.APPLICATION_MODAL);
//    if (commentDialog.cancel) {
//      return;
//    }
    int num = JOptionPane.showConfirmDialog(this, "确认暂停本次商品批次?", "暂停确认", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;
    String errorInfo = "";
    try {
      for (int i = 0; i < beanList.size(); i++) {
        ZcBdSppc zcBdSppc = (ZcBdSppc) beanList.get(i);
        zcBdSppc.setZcSppcStatus("suspended");
        //zcBdSppc.setComment(commentDialog.getComment());
        this.zcBdSppcServiceDelegate.updateZcBdSppcByPrimaryKey(zcBdSppc, requestMeta);
      }
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "暂停商品批次成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "暂停商品批次失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * 启用方法
   */

  private void doAuditPass() {
    List beanList = this.getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    requestMeta.setFuncId(this.auditPassButton.getFuncId());

//    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
//      ModalityType.APPLICATION_MODAL);
//    if (commentDialog.cancel) {
//      return;
//    }
    int num = JOptionPane.showConfirmDialog(this, "确认启用本次商品批次?", "启用确认", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;
    String errorInfo = "";
    try {
      for (int i = 0; i < beanList.size(); i++) {
        ZcBdSppc zcBdSppc = (ZcBdSppc) beanList.get(i);
        zcBdSppc.setZcSppcStatus("exec");
        //zcBdSppc.setComment(commentDialog.getComment());
        this.zcBdSppcServiceDelegate.updateZcBdSppcByPrimaryKey(zcBdSppc, requestMeta);
      }
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "启用商品批次成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "启用商品批次失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * 打印方法
   */
  private void doPrint() {
    List<ZcBdSppc> printList = getCheckedList();
    if (printList.size() == 0) {
      JOptionPane.showMessageDialog(this, "请选择需要打印的数据 ！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    requestMeta.setFuncId(this.printButton.getFuncId());
    requestMeta.setPageType(this.compoId + "_L");
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

  /**
   * 打印预览方法
   */
  private void doPrintPreview() {
    final List<ZcBdSppc> printList = getCheckedList();
    if (printList.size() == 0) {
      JOptionPane.showMessageDialog(this, "请选择需要打印预览的数据 ！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    requestMeta.setFuncId(this.printPreviewButton.getFuncId());
    requestMeta.setPageType(this.compoId + "_L");
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

  /**
   * 打印设置方法
   */
  private void doPrintSetting() {
    requestMeta.setFuncId(this.printSettingButton.getFuncId());
    requestMeta.setPageType(this.compoId + "_L");
    new PrintSettingDialog(requestMeta);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          UIManager.setLookAndFeel(new BlueLookAndFeel());
        } catch (Exception e) {
          e.printStackTrace();
        }
        //        UIManager.getDefaults().put("SplitPaneUI", BigButtonSplitPaneUI.class.getName());
        ZcBdSppcListPanel bill = new ZcBdSppcListPanel();
        JFrame frame = new JFrame("frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }
    });
  }
}
