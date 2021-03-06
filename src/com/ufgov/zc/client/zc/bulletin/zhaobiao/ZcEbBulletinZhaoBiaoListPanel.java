/**
 * 
 */
package com.ufgov.zc.client.zc.bulletin.zhaobiao;

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
import java.util.List;

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
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbBulletinToTableModelConverter;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;
import com.ufgov.zc.client.print.PrintPreviewer;
import com.ufgov.zc.client.print.PrintSettingDialog;
import com.ufgov.zc.client.print.Printer;
import com.ufgov.zc.client.util.BalanceUtil;
import com.ufgov.zc.client.util.ListUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.bulletin.AbstractZcEbBulletinBaseListPanel;
import com.ufgov.zc.client.zc.bulletin.ZcEbBulletinXunJiaBidListPanel;
import com.ufgov.zc.client.zc.flowconsole.DataFlowConsoleCanvas;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.dto.PrintObject;
import com.ufgov.zc.common.system.exception.BaseException;
import com.ufgov.zc.common.system.exception.OtherException;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.publish.IZcEbBulletinServiceDelegate;

/**
 * 招标公告/采购公告
 * @author Administrator
 *
 */
public class ZcEbBulletinZhaoBiaoListPanel extends AbstractEditListBill implements ParentWindowAware {

  private String compoId = "ZC_EB_BULLETIN_BID";
  

  private static final Logger logger = Logger.getLogger(AbstractZcEbBulletinBaseListPanel.class);

  private final ZcEbBulletinZhaoBiaoListPanel self = this;

  private Window parentWindow;

  protected String title = LangTransMeta.translate("ZC_EB_BULLETIN_BID");

  protected String bulletinTab = "ZcEbBulletin_bulletinTab";

  protected String bulletinCondition = "ZcEbBulletin_bulletinTab";

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private final ElementConditionDto elementConditionDto = new ElementConditionDto();

  protected IZcEbBulletinServiceDelegate zcEbBulletinServiceDelegate = (IZcEbBulletinServiceDelegate) ServiceFactory.create(

  IZcEbBulletinServiceDelegate.class, "zcEbBulletinServiceDelegate");

  protected IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,

  "baseDataServiceDelegate");

  protected String getTitle() {

    return this.title;

  }

  protected void setTitle(String title) {

    this.title = title;

  }

  protected String getCompoID() {

    return this.compoId;

  }

  protected void setCompoID(String compoId) {

    this.compoId = compoId;

  }

  protected String getBulletinTab() {

    return this.bulletinTab;

  }

  protected void setBulletinTab(String bulletinTab) {

    this.bulletinTab = bulletinTab;

  }

  protected String getBulletinCondition() {

    return this.bulletinCondition;

  }

  protected void setBulletinCondition(String bulletinCondition) {

    this.bulletinCondition = bulletinCondition;

  }

  protected IZcEbBulletinServiceDelegate getIZcEbBulletinServiceDelegate() {

    return this.zcEbBulletinServiceDelegate;

  }

  @Override
  public Window getParentWindow() {

    return parentWindow;

  }

  @Override
  public void setParentWindow(Window parentWindow) {

    this.parentWindow = parentWindow;

  }

  public ZcEbBulletinZhaoBiaoListPanel() {
    init();
  }

 

  protected void init() {

    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {

      @Override
      public List<SearchCondition> execute() throws Exception {

        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()

        .getCurrUserId(), self.bulletinTab);

        return needDisplaySearchConditonList;

      }

      @Override
      public void success(List<SearchCondition> needDisplaySearchConditonList) {

        List<TableDisplay> showingDisplays = SearchConditionUtil.getNeedDisplayTableDisplay(needDisplaySearchConditonList);

        init(createDataDisplay(showingDisplays), null);// 调用父类方法

        revalidate();

        repaint();

      }

    });

    requestMeta.setCompoId(self.compoId);

  }

  private final class DataDisplay extends MultiDataDisplay {

    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,

    boolean showConditionArea) {

      super(displays, showingDisplays, conditionArea, showConditionArea, self.bulletinTab);

      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), self.title, TitledBorder.CENTER, TitledBorder.TOP, new Font(

      "宋体", Font.BOLD, 15), Color.BLUE));

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

              List beanList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(model.getList(), table));

              new ZcEbBulletinZhaoBiaoDialog(self,  beanList, row, tabStatus);
            }

          }

        });

      }

    }

    @Override
    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {

      elementConditionDto.setWfcompoId(getCompoID());

      elementConditionDto.setExecutor(WorkEnv.getInstance().getCurrUserId());

      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());

      elementConditionDto.setStatus(tableDisplay.getStatus());

      elementConditionDto.setMonth(BalanceUtil.getMonthIdBySysOption());

      elementConditionDto.setBillType(getBulletinType());

      if (WorkEnv.getInstance().containRole(ZcSettingConstants.ROLE_GYS_NORMAL)) {
        elementConditionDto.setZcText0(ZcSettingConstants.ROLE_GYS_NORMAL);
      }

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

          return ZcEbBulletinToTableModelConverter.convertToTableModel(getBulletinList(self.compoId), getBulletinType());

        }

        @Override
        public void success(TableModel model) {

          tableDisplay.setTableModel(model);

          setButtonsVisiable();

        }

        public List getBulletinList(String compoID) {

          List<ZcEbBulletin> bulletinList = new ArrayList<ZcEbBulletin>();

          bulletinList = self.zcEbBulletinServiceDelegate.getZcEbBulletinList(elementConditionDto, requestMeta);

          return bulletinList;

        }

      });

    }

  }

  private AbstractSearchConditionArea topSearchConditionArea;

  private AbstractSearchConditionArea createTopConditionArea() {

    topSearchConditionArea = new AbstractSearchConditionArea();

    return topSearchConditionArea;

  }

  public String getBulletinType() {

    // TCJLODO Auto-generated method stub

    return "zhaobiao%";

  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {

    return new DataDisplay(SearchConditionUtil.getAllTableDisplay(self.bulletinTab), showingDisplays, createTopConditionArea(), false);// true:显示收索条件区
    // false：不显示收索条件区

  }

  public void refreshCurrentTabData() {

    topSearchConditionArea.doSearch();

  }

  
  /**
   * 
   * 添加工具栏按钮
   */

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(compoId);

    toolBar.add(addButton);

    // toolBar.add(updateButton);

    toolBar.add(deleteButton);

    toolBar.add(helpButton);

    toolBar.add(sendButton);// 送审

    // toolBar.add(suggestPassButton);//填写意见审核通过

    // toolBar.add(callbackButton);//收回

    // toolBar.add(unTreadButton);//退回

    // toolBar.add(unAuditButton);//销审

    // toolBar.add(cancelButton);//撤销

    toolBar.add(traceButton);

    // toolBar.add(printButton);

    // toolBar.add(isSendToNextButton);

//    toolBar.add(traceDataButton);

    // 初始化按钮的action事件

    addButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doAdd();

      }

    });

    sendButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doSend();

      }

    });

    isSendToNextButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doSendNext();

      }

    });

    deleteButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doDelete();

      }

    });

    printButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {

        doPrint();

      }

    });

    printPreviewButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {

        doPrintPreview();

      }

    });

    printSettingButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {

        doPrintSetting();

      }

    });

    suggestPassButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doSuggestPass();

      }

    });

    callbackButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doCallBack();

      }

    });

    unTreadButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doUnTread();

      }

    });

    unAuditButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doUnAudit();

      }

    });

    traceButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doTrace();

      }

    });

    cancelButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doCancel();

      }

    });

    traceDataButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {

        doTraceDataButton();

      }

    });

  }

  protected void doAdd(){

    new ZcEbBulletinZhaoBiaoDialog(self, new ArrayList(), this.topDataDisplay.getActiveTableDisplay().getTable().getRowCount(), topDataDisplay
      .getActiveTableDisplay().getStatus());
  }

  protected void doPrintSetting() {

    requestMeta.setFuncId(this.printSettingButton.getFuncId());

    requestMeta.setPageType(this.compoId + "_L");

    new PrintSettingDialog(requestMeta);

  }

  /**
   * 
   * 是否送主任审核
   */

  protected void doSendNext() {

    List beanList = getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int sel = JOptionPane.showConfirmDialog(this, "是否提交审核？");

    if (sel == JOptionPane.OK_OPTION) {

      executeAudit(beanList, ZcSettingConstants.IS_GOON_AUDIT_YES);

    } else {

      executeAudit(beanList, ZcSettingConstants.IS_GOON_AUDIT_NO);

    }

  }

  protected void executeAudit(List beanList, int isGoonAudit) {

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

        ZcEbBulletin report = (ZcEbBulletin) beanList.get(i);

        report.setIsGoonAudit(isGoonAudit);

        report.setComment(comment);

        report.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      }

      requestMeta.setFuncId(this.isSendToNextButton.getFuncId());

      this.getIZcEbBulletinServiceDelegate().updateFN(beanList, requestMeta);

      this.getIZcEbBulletinServiceDelegate().commitFN(beanList, requestMeta);

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

  protected void doTrace() {

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

      ZcEbBulletin bean = (ZcEbBulletin) beanList.get(i);

      ZcUtil.showTraceDialog(bean, compoId);

    }

  }

  protected void doPrint() {

    List printList = getCheckedList();

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

  protected void doPrintPreview() {

    final List printList = getCheckedList();

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

  protected void doSend() {

    List beanList = getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    boolean success = true;

    if (!checkBeforeSave(beanList)) {
      return;
    }

    try {

      requestMeta.setFuncId(sendButton.getFuncId());

      this.getIZcEbBulletinServiceDelegate().newCommitFN(beanList, requestMeta);

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

  protected void doCancel() {

    boolean success = true;

    requestMeta.setFuncId(this.cancelButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "确实要撤销计划？");

    if (num == JOptionPane.YES_OPTION) {

      try {

        for (int i = 0; i < beanList.size(); i++) {

          ZcEbBulletin make = (ZcEbBulletin) beanList.get(i);

          make.setBulletinStatus("cancel");

          this.getIZcEbBulletinServiceDelegate().CancelMakeFN(make, requestMeta);

        }

      } catch (Exception ex) {

        logger.error(ex.getMessage(), ex);

        success = false;

        UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

      }

      if (success) {

        JOptionPane.showMessageDialog(this, "撤销成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.refreshCurrentTabData();

      }

    }

  }

  protected void doUnAudit() {

    boolean success = true;

    String errorInfo = "";

    requestMeta.setFuncId(this.unAuditButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    try {

      for (int i = 0; i < beanList.size(); i++) {

        ZcEbBulletin make = (ZcEbBulletin) beanList.get(i);

        make.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        this.getIZcEbBulletinServiceDelegate().unAuditFN(make, requestMeta);

      }

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "销审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "销审失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  protected void doUnTread() {

    boolean success = true;

    requestMeta.setFuncId(this.unTreadButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL, "不同意");

    if (commentDialog.cancel) {

      return;

    }

    try {

      for (int i = 0; i < beanList.size(); i++) {

        ZcEbBulletin make = (ZcEbBulletin) beanList.get(i);

        make.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        make.setComment(commentDialog.getComment());

        this.getIZcEbBulletinServiceDelegate().untreadFN(make, requestMeta);

      }

    } catch (Exception ex) {

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.refreshCurrentTabData();

    }

  }

  protected void doCallBack() {

    boolean success = true;

    String errorInfo = "";

    requestMeta.setFuncId(this.callbackButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    try {

      for (int i = 0; i < beanList.size(); i++) {

        ZcEbBulletin make = (ZcEbBulletin) beanList.get(i);

        make.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        this.getIZcEbBulletinServiceDelegate().callbackFN(make, requestMeta);

      }

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "收回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  protected void doSuggestPass() {

    boolean success = true;

    requestMeta.setFuncId(this.suggestPassButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    try {

      for (int i = 0; i < beanList.size(); i++) {

        ZcEbBulletin make = (ZcEbBulletin) beanList.get(i);

        make.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        make.setComment(commentDialog.getComment());

        this.getIZcEbBulletinServiceDelegate().auditFN(make, requestMeta);

      }

    } catch (Exception ex) {

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.refreshCurrentTabData();

    }

  }

  protected List getCheckedList() {

    List<ZcEbBulletin> beanList = new ArrayList<ZcEbBulletin>();

    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();

    MyTableModel model = (MyTableModel) table.getModel();

    // Modal的数据

    List list = model.getList();

    Integer[] checkedRows = table.getCheckedRows();

    for (Integer checkedRow : checkedRows) {

      int accordDataRow = table.convertRowIndexToModel(checkedRow);

      ZcEbBulletin bean = (ZcEbBulletin) list.get(accordDataRow);

      beanList.add(bean);

    }

    return beanList;

  }

  protected void doDelete() {

    List beanList = getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "是否删除当前选择的单据", "删除确认", 0);

    if (num == JOptionPane.NO_OPTION) {
      return;
    }

    boolean success = true;

    try {

      requestMeta.setFuncId(deleteButton.getFuncId());

      this.getIZcEbBulletinServiceDelegate().deleteBeanListFN(beanList, requestMeta);

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

      // 提示信息不明确
      // JOptionPane.showMessageDialog(this, "处理成功！", "提示",
      // JOptionPane.INFORMATION_MESSAGE);
      JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshCurrentTabData();

    }

  }

  public void doTraceDataButton() {

    List beanList = getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择一条要进行跟踪的数据！", "错误", JOptionPane.ERROR_MESSAGE);

      return;

    }

    ZcEbBulletin sh = (ZcEbBulletin) beanList.get(0);

    DataFlowConsoleCanvas dfc = new DataFlowConsoleCanvas(sh.getProjCode(), compoId);

    dfc.showWindow();

  }

  protected void setButtonStatus() {

    String panelId = WFConstants.EDIT_TAB_STATUS_EDIT;

    if (topDataDisplay != null && topDataDisplay.getActiveTableDisplay() != null) {

      panelId = topDataDisplay.getActiveTableDisplay().getStatus();

    }

    if (WFConstants.EDIT_TAB_STATUS_EDIT.equalsIgnoreCase(panelId)) {

      addButton.setEnabled(true);

      sendButton.setEnabled(true);

      isSendToNextButton.setEnabled(true);

      traceButton.setEnabled(false);

      deleteButton.setEnabled(true);

      printButton.setEnabled(true);

      printPreviewButton.setEnabled(true);

      printSettingButton.setEnabled(true);

    } else if (WFConstants.EDIT_TAB_STATUS_WAITPUBLISHED.equalsIgnoreCase(panelId)) {

      addButton.setEnabled(true);

      sendButton.setEnabled(false);

      isSendToNextButton.setEnabled(false);

      traceButton.setEnabled(true);

      deleteButton.setEnabled(false);

      printButton.setEnabled(true);

      printPreviewButton.setEnabled(true);

      printSettingButton.setEnabled(true);

    } else if (WFConstants.EDIT_TAB_STATUS_WAITLEADERAUDIT.equalsIgnoreCase(panelId)) {

      addButton.setEnabled(true);

      sendButton.setEnabled(false);

      isSendToNextButton.setEnabled(false);

      traceButton.setEnabled(true);

      deleteButton.setEnabled(false);

      printButton.setEnabled(true);

      printPreviewButton.setEnabled(true);

      printSettingButton.setEnabled(true);

    } else if (WFConstants.EDIT_TAB_STATUS_PUBLISHED.equalsIgnoreCase(panelId)) {

      addButton.setEnabled(true);

      sendButton.setEnabled(false);

      isSendToNextButton.setEnabled(false);

      traceButton.setEnabled(true);

      deleteButton.setEnabled(false);

      printButton.setEnabled(true);

      printPreviewButton.setEnabled(true);

      printSettingButton.setEnabled(true);

    } else if (WFConstants.AUDIT_TAB_STATUS_ALL.equalsIgnoreCase(panelId)) {

      addButton.setEnabled(true);

      sendButton.setEnabled(false);

      isSendToNextButton.setEnabled(false);

      traceButton.setEnabled(true);

      deleteButton.setEnabled(false);

      printButton.setEnabled(true);

      printPreviewButton.setEnabled(true);

      printSettingButton.setEnabled(true);

    }

  }

  private boolean checkBeforeSave(List beanList) {

    boolean isPass = true;
/*
    StringBuilder errorInfo = new StringBuilder();

    for (int i = 0; i < beanList.size(); i++) {
      ZcEbBulletin bulletin = (ZcEbBulletin) beanList.get(i);

      if (bulletin.getBulletinType().equals(ZcEbBulletinConstants.TYPE_BULLETIN_BID)) {
        if (bulletin.getFileId1() == null || "".equals(bulletin.getFileId1().trim())) {
          errorInfo.append("项目为：" + bulletin.getProjName() + "的[抽取专家申请表]不能为空 \n");
          isPass = false;
        }
        if (bulletin.getFileId2() == null || "".equals(bulletin.getFileId2().trim())) {
          errorInfo.append("项目为：" + bulletin.getProjName() + "的[抽取纪检申请表]不能为空 \n");
          isPass = false;
        }
      }

    }

    if (errorInfo.length() != 0) {

      JOptionPane.showMessageDialog(this.parentWindow, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);

    }
*/
    return isPass;

  }

  static {

    LangTransMeta.init("ZC%");

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

        ZcEbBulletinZhaoBiaoListPanel bill = new ZcEbBulletinZhaoBiaoListPanel();

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
