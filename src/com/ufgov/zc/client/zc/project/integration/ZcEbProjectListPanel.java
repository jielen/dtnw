package com.ufgov.zc.client.zc.project.integration;

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
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbProjectToTableModelConverter;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.GkCommentUntreadDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.AcceptedButton;
import com.ufgov.zc.client.component.button.BackButton;
import com.ufgov.zc.client.component.button.BlankOutButton;
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
import com.ufgov.zc.client.zc.project.integration.zbbook.services.ZbBookOperatorsService;
import com.ufgov.zc.client.zc.ztb.util.GV;
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
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbBulletinServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbProjServiceDelegate;

/**
 * @ClassName: ZcEbProjectListPanel
 * @Description: 项目立项
 * @date: 2010-4-21 下午03:44:50
 * @version: V1.0
 * @since: 1.0
 * @author: LEO
 * @modify:
 */
@SuppressWarnings({ "serial", "unchecked" })
public class ZcEbProjectListPanel extends AbstractEditListBill implements ParentWindowAware {
  private static final Logger logger = Logger.getLogger(ZcEbProjectListPanel.class);

  private Window parentWindow;

  public static final String compoId = "ZC_EB_PROJ";

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(compoId);

  protected IZcEbBulletinServiceDelegate zcEbBulletinServiceDelegate = (IZcEbBulletinServiceDelegate) ServiceFactory.create(
    IZcEbBulletinServiceDelegate.class, "zcEbBulletinServiceDelegate");

  protected IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
    "baseDataServiceDelegate");

  public IZcEbBulletinServiceDelegate getIZcEbBulletinServiceDelegate() {
    return this.zcEbBulletinServiceDelegate;
  }

  public BillElementMeta getBillElementMeta() {
    return billElementMeta;
  }

  private BillElementMeta detailPackBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd(ZcEbProjectListPanel.compoId + "_PACK");

  public BillElementMeta getDetailPackBillElementMeta() {
    return detailPackBillElementMeta;
  }

  private BillElementMeta detailPackReqBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd(ZcEbProjectListPanel.compoId + "_PACKREQ");

  public BillElementMeta getDetailPackReqBillElementMeta() {
    return detailPackReqBillElementMeta;
  }

  public RequestMeta getRequestMeta() {
    return requestMeta;
  }

  private ElementConditionDto elementConditionDto = new ElementConditionDto();

  public IZcEbProjServiceDelegate zcEbProjectServiceDelegate = (IZcEbProjServiceDelegate) ServiceFactory.create(IZcEbProjServiceDelegate.class,
    "zcEbProjServiceDelegate");

  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,
    "zcEbBaseServiceDelegate");

  public Window getParentWindow() {
    return parentWindow;
  }

  public void setParentWindow(Window parentWindow) {
    this.parentWindow = parentWindow;
  }

  private final class DataDisplay extends MultiDataDisplay {
    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,
      boolean showConditionArea) {
      super(displays, showingDisplays, conditionArea, showConditionArea, "ZcEbProj_ProjTab");
      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate("ZC_EB_PROJ_TITLE"),
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
              new ZcEbProjectEditFrame(ZcEbProjectListPanel.this, viewList, row, tabStatus);
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
          return ZcEbProjectToTableModelConverter.convertToTableModel(ZcEbProjectListPanel.this.zcEbProjectServiceDelegate.getZcEbProj(
            elementConditionDto, requestMeta));
        }

        @Override
        public void success(TableModel model) {
          tableDisplay.setTableModel(model);
          setButtonsVisiable();
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
  public ZcEbProjectListPanel() {
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()
          .getCurrUserId(), "ZcEbProj_ProjTab");
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

  private AbstractSearchConditionArea topSearchConditionArea;

  private AbstractSearchConditionArea createTopConditionArea() {
    Map defaultValueMap = new HashMap();
    topSearchConditionArea = new SaveableSearchConditionArea("ZcEbProj_Proj", null, true, defaultValueMap, null);
    return topSearchConditionArea;
  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {
    return new DataDisplay(SearchConditionUtil.getAllTableDisplay("ZcEbProj_ProjTab"), showingDisplays, createTopConditionArea(), true);//true:显示收索条件区 false：不显示收索条件区
  }

  private FuncButton blankoutButton = new BlankOutButton();

  private FuncButton acceptedButton = new AcceptedButton();

  private FuncButton backButton = new BackButton();

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(compoId);
    toolBar.add(addButton);
    toolBar.add(deleteButton);
    toolBar.add(sendButton);
    // toolBar.add(isSendToNextButton);
    //  toolBar.add(suggestPassButton);//填写意见审核通过
    //  toolBar.add(auditPassButton);
    //  toolBar.add(unTreadButton);//退回
    toolBar.add(traceButton);
    //  toolBar.add(callbackButton);
    toolBar.add(blankoutButton);
    toolBar.add(printButton);
    toolBar.add(traceDataButton);
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
    traceDataButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doTraceDataButton();
      }
    });
  }

  public void refreshCurrentTabData() {
    topSearchConditionArea.doSearch();
  }

  public void refreshCurrentTabData(List beanList) {
    topDataDisplay.getActiveTableDisplay().getTable().setModel(ZcEbProjectToTableModelConverter.convertToTableModel(beanList));
  }

  public List<ZcEbProj> getCheckedList() {
    List<ZcEbProj> beanList = new ArrayList<ZcEbProj>();
    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();
    MyTableModel model = (MyTableModel) table.getModel();
    List<ZcEbProj> list = model.getList();
    Integer[] checkedRows = table.getCheckedRows();
    for (Integer checkedRow : checkedRows) {
      int accordDataRow = table.convertRowIndexToModel(checkedRow);
      ZcEbProj bean = list.get(accordDataRow);
      beanList.add(bean);
    }
    return beanList;
  }

  private void doAdd() {
    new ZcEbProjectEditFrame(ZcEbProjectListPanel.this, new ArrayList(1), -1, topDataDisplay.getActiveTableDisplay().getStatus());
  }

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
        ZcEbProj proj = (ZcEbProj) checkedList.get(i);
        proj.setAuditorId(WorkEnv.getInstance().getCurrUserId());
        zcEbProjectServiceDelegate.delete(proj.getProjCode(), requestMeta);
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

  private void doSend() {
    List beanList = this.getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    } else if(beanList.size() > 1){
        JOptionPane.showMessageDialog(this, "只能选择一条数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    boolean success = true;
    String errorInfo = "";
    requestMeta.setFuncId(this.sendButton.getFuncId());
    try {
      for (int i = 0; i < beanList.size(); i++) {
        ZcEbProj proj = (ZcEbProj) beanList.get(i);
        if ("_1_2_3_4_6_".indexOf(proj.getPurType())!=-1 && toCheckZbFileAndEvalElements(proj)) {
        	success = false;
          continue;
        }
        proj.setAuditorId(WorkEnv.getInstance().getCurrUserId());
        zcEbProjectServiceDelegate.newCommitFN(proj, requestMeta);
      }
    } catch (Exception ex) {
      errorInfo += ex.getMessage();
      logger.error(ex.getMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.refreshCurrentTabData();
    }
  }

  /**
   * 检查项目是否缺少招标书和评标方法
   *
   * @param projCode
   * @return
   */
  private boolean toCheckZbFileAndEvalElements(ZcEbProj zcEbProj) {
    String projCode = zcEbProj.getProjCode();
    ZbBookOperatorsService bookService = new ZbBookOperatorsService(null);
    return bookService.toCheckZbFileAndEvalElements(projCode);
  }

  private void doSuggestPass() {
    List beanList = this.getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    boolean success = true;
    String errorInfo = "";
    requestMeta.setFuncId(this.suggestPassButton.getFuncId());
    try {
      for (int i = 0; i < beanList.size(); i++) {
        ZcEbProj proj = (ZcEbProj) beanList.get(i);
        zcEbProjectServiceDelegate.auditFN(proj, requestMeta);
      }
    } catch (Exception ex) {
      errorInfo += ex.getMessage();
      logger.error(ex.getMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.refreshCurrentTabData();
    }
  }

  private void doUnTread() {
    List beanList = this.getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    GkCommentUntreadDialog commentDialog = new GkCommentUntreadDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      ModalityType.APPLICATION_MODAL);
    if (commentDialog.cancel) {
      return;
    }
    boolean success = true;
    String errorInfo = "";
    try {
      for (int i = 0; i < beanList.size(); i++) {
        requestMeta.setFuncId(unTreadButton.getFuncId());
        ZcEbProj bill = (ZcEbProj) beanList.get(i);
        bill.setAuditorId(WorkEnv.getInstance().getCurrUserId());
        bill.setComment(commentDialog.getComment());
        zcEbProjectServiceDelegate.untreadFN(bill, requestMeta);
      }
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "退回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void doAuditPass() {
    List beanList = this.getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    executeAudit(beanList, ZcSettingConstants.IS_GOON_AUDIT_NO);
  }

  private void doSendNext() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    executeAudit(beanList, ZcSettingConstants.IS_GOON_AUDIT_YES);
  }

  private void executeAudit(List beanList, int isGoonAudit) {
    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      ModalityType.APPLICATION_MODAL);
    if (commentDialog.cancel) {
      return;
    }
    boolean success = true;
    String errorInfo = "";
    try {
      for (int i = 0; i < beanList.size(); i++) {
        ZcEbProj proj = (ZcEbProj) beanList.get(i);
        proj.setIsGoonAudit(isGoonAudit);
        proj.setComment(commentDialog.getComment());
        proj.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      }
      requestMeta.setFuncId(this.isSendToNextButton.getFuncId());
      this.zcEbProjectServiceDelegate.save(beanList, requestMeta);
      this.zcEbProjectServiceDelegate.auditFN(beanList, requestMeta);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void doTrace() {
    ZcUtil.showTraceDialog(getCheckedList(), this);
  }

  private void doCallBack() {
    List beanList = this.getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    boolean success = true;
    String errorInfo = "";
    requestMeta.setFuncId(this.callbackButton.getFuncId());
    try {
      for (int i = 0; i < beanList.size(); i++) {
        ZcEbProj proj = (ZcEbProj) beanList.get(i);
        proj.setAuditorId(WorkEnv.getInstance().getCurrUserId());
        zcEbProjectServiceDelegate.callbackFN(proj, requestMeta);
      }
    } catch (Exception ex) {
      errorInfo += ex.getMessage();
      logger.error(ex.getMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.refreshCurrentTabData();
    }
  }

  private void doPrint() {
    List<ZcEbProj> printList = getCheckedList();
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

  private void doPrintPreview() {
    final List<ZcEbProj> printList = getCheckedList();
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

  private void doPrintSetting() {
    requestMeta.setFuncId(this.printSettingButton.getFuncId());
    requestMeta.setPageType(this.compoId + "_L");
    new PrintSettingDialog(requestMeta);
  }

  private void doTraceDataButton() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "请选择一条要进行跟踪的数据！", "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
    ZcEbProj sh = (ZcEbProj) beanList.get(0);
    DataFlowConsoleCanvas dfc = new DataFlowConsoleCanvas(sh.getProjCode(), this.compoId);
    dfc.showWindow();
  }

  private void setButtonStatus() {
    String panelId = WFConstants.AUDIT_TAB_STATUS_TODO;
    if (topDataDisplay != null && topDataDisplay.getActiveTableDisplay() != null) {
      panelId = topDataDisplay.getActiveTableDisplay().getStatus();
    }
    if (WFConstants.AUDIT_TAB_STATUS_TODO.equalsIgnoreCase(panelId)) {
      auditPassButton.setEnabled(true);
      unAuditButton.setEnabled(false);
      unTreadButton.setEnabled(true);
      acceptedButton.setEnabled(false);
      backButton.setEnabled(false);
      sendButton.setEnabled(false);
      isSendToNextButton.setEnabled(true);
    } else if (WFConstants.EDIT_TAB_STATUS_ACCEPTED.equalsIgnoreCase(panelId)) {
      auditPassButton.setEnabled(false);
      unAuditButton.setEnabled(false);
      unTreadButton.setEnabled(false);
      acceptedButton.setEnabled(false);
      backButton.setEnabled(false);
      sendButton.setEnabled(true);
      isSendToNextButton.setEnabled(false);
    } else if (WFConstants.EDIT_TAB_STATUS_UNACCEPTED.equalsIgnoreCase(panelId)) {
      auditPassButton.setEnabled(false);
      isSendToNextButton.setEnabled(false);
      unAuditButton.setEnabled(false);
      unTreadButton.setEnabled(false);
      acceptedButton.setEnabled(true);
      backButton.setEnabled(true);
      sendButton.setEnabled(false);
    } else if (WFConstants.AUDIT_TAB_STATUS_DONE.equalsIgnoreCase(panelId)) {
      auditPassButton.setEnabled(false);
      isSendToNextButton.setEnabled(false);
      unAuditButton.setEnabled(true);
      unTreadButton.setEnabled(false);
      acceptedButton.setEnabled(false);
      backButton.setEnabled(false);
      sendButton.setEnabled(false);
    } else if (WFConstants.AUDIT_TAB_STATUS_ALL.equalsIgnoreCase(panelId)) {
      auditPassButton.setEnabled(false);
      isSendToNextButton.setEnabled(false);
      unAuditButton.setEnabled(false);
      unTreadButton.setEnabled(false);
      acceptedButton.setEnabled(false);
      backButton.setEnabled(false);
      sendButton.setEnabled(false);
    } else if (WFConstants.AUDIT_TAB_STATUS_AUDITED.equalsIgnoreCase(panelId)) {
      auditPassButton.setEnabled(false);
      isSendToNextButton.setEnabled(false);
      unAuditButton.setEnabled(false);
      unTreadButton.setEnabled(false);
      acceptedButton.setEnabled(true);
      backButton.setEnabled(false);
      sendButton.setEnabled(false);
    }
  }

  public static void main(String[] args) throws Exception {
    /*    System.setProperty("sun.java2d.ddscale", "true");
    UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
    JFrame.setDefaultLookAndFeelDecorated(true);
    JDialog.setDefaultLookAndFeelDecorated(true);
    UIManager.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        Object newLF = event.getNewValue();
        if ((newLF instanceof LiquidLookAndFeel) == false) {
          try {
            UIManager.setLookAndFeel(new LiquidLookAndFeel());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    });*/
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          UIManager.setLookAndFeel(new BlueLookAndFeel());
        } catch (Exception e) {
          e.printStackTrace();
        }
        //        UIManager.getDefaults().put("SplitPaneUI", BigButtonSplitPaneUI.class.getName());
        ZcEbProjectListPanel bill = new ZcEbProjectListPanel();
        JFrame frame = new JFrame("frame");
        frame.setIconImage(frame.getToolkit().getImage(GV.getImageUrl("windowicon.jpg")));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }
    });
  }
}
