/**
 * 
 */
package com.ufgov.zc.client.zc.zcxmcght;

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
import com.ufgov.zc.client.common.converter.zc.ZcXmcgHtToTableModelConverter;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.AsValComboboxConditionItem2;
import com.ufgov.zc.client.component.ui.conditionitem.ConditionFieldConstants;
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
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.publish.IZcPProMakeServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcXmcgHtServiceDelegate;

/**
 * 补充合同，
 * 适用于丹徒地区需求，
 * 丹徒区补充合同实现下列要求  
 * 1、补充合同使用指标
 * 2、指标分两种情况：
 *  2.1:合同只用全新指标，需调用指标接口，进行占用；
 *  2.2:合同使用原采购计划已经占用的指标，指标被占用额度不变，不需要调用指标接口进行占用；
 * @author Administrator
 *
 */
public class ZcXmcgHtSubListPanelDt extends AbstractEditListBill implements ParentWindowAware {

  /**

   * 

   */

  private static final long serialVersionUID = 5518331807480036571L;

  private static final Logger logger = Logger.getLogger(ZcXmcgHtSubListPanelDt.class);

  private ZcXmcgHtSubListPanelDt self = this;

  public ZcXmcgHtSubListPanelDt getSelf() {
    return self;
  }

  private Window parentWindow;

//  private String compoId = "ZC_XMCG_HT";

  protected String getCompoId() {
    return "ZC_XMCG_HT_SUB_DT";
  }

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  protected ElementConditionDto elementConditionDto = new ElementConditionDto();

  public IZcXmcgHtServiceDelegate zcXmcgHtServiceDelegate = (IZcXmcgHtServiceDelegate) ServiceFactory.create(IZcXmcgHtServiceDelegate.class,

  "zcXmcgHtServiceDelegate");

  public IZcPProMakeServiceDelegate ZcPProMakeServiceDelegate = (IZcPProMakeServiceDelegate) ServiceFactory.create(IZcPProMakeServiceDelegate.class,

  "zcPProMakeServiceDelegate");

  private IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,

  "baseDataServiceDelegate");

  public Window getParentWindow() {

    return parentWindow;

  }

  public void setParentWindow(Window parentWindow) {

    this.parentWindow = parentWindow;

  }

  private final class DataDisplay extends MultiDataDisplay {

    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,

    boolean showConditionArea) {

      super(displays, showingDisplays, conditionArea, showConditionArea, ZcSettingConstants.TAB_ID_ZC_XMCG_HT);

      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "补充合同", TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体",

      Font.BOLD, 15), Color.BLUE));

    }

    protected void preprocessShowingTableDisplay(List<TableDisplay> showingTableDisplays) {

      for (final TableDisplay d : showingTableDisplays) {

        final JGroupableTable table = d.getTable();

        table.addMouseListener(new MouseAdapter() {

          public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {

              String tabStatus = d.getStatus();

              JGroupableTable table = d.getTable();

              MyTableModel model = (MyTableModel) table.getModel();

              int row = table.getSelectedRow();

              List viewList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(model.getList(), table));

              new ZcXmcgHtSubDialogDt(getSelf(), viewList, row, tabStatus);

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
      
      elementConditionDto.setZcText0("sup");
      
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
          return ZcXmcgHtToTableModelConverter.convertToTableModel(self.zcXmcgHtServiceDelegate.getZcXmcgHt(elementConditionDto, requestMeta));
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

  public ZcXmcgHtSubListPanelDt() {

    createPanel();

    requestMeta.setCompoId(getCompoId());

  }

  protected void createPanel() {

    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {

      @Override
      public List<SearchCondition> execute() throws Exception {

        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonListJoinRole(WorkEnv.getInstance()

        .getCurrUserId(), ZcSettingConstants.TAB_ID_ZC_XMCG_HT);

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

  }

  protected AbstractSearchConditionArea topSearchConditionArea;

  private AbstractSearchConditionArea createTopConditionArea() {

    Map defaultValueMap = new HashMap();

    topSearchConditionArea = new SaveableSearchConditionArea(ZcSettingConstants.CONDITION_ID_ZC_XMCG_HT, null, false, defaultValueMap, null);

    AbstractSearchConditionItem item = this.topSearchConditionArea.getCondItemsByCondiFieldCode(ConditionFieldConstants.ZC_VS_HT_STATUS);

    if (item != null) {

      ((AsValComboboxConditionItem2) item).setValueByCode("0");

    }

    return topSearchConditionArea;

  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {

    return new DataDisplay(

    SearchConditionUtil.getAllTableDisplayJoinRole(WorkEnv.getInstance().getCurrUserId(), ZcSettingConstants.TAB_ID_ZC_XMCG_HT), showingDisplays,

    createTopConditionArea(), true);//true:显示搜索条件区 false：不显示搜索条件区

  }

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(getCompoId());

    toolBar.add(addButton);

    //toolBar.add(batEditButton);

    //toolBar.add(editButton);

    toolBar.add(sendButton);

    toolBar.add(suggestPassButton);

    //toolBar.add(unAuditButton);

    //toolBar.add(unTreadButton);

    //toolBar.add(callbackButton);//收回

    toolBar.add(deleteButton);

    toolBar.add(traceButton);

    //toolBar.add(printButton);

    // toolBar.add(printPreviewButton);

    // toolBar.add(printSettingButton);

    toolBar.add(helpButton);

    // toolBar.add(isSendToNextButton);

    // 初始化按钮的action事件

    toolBar.add(traceDataButton);

    addButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doAdd();

      }

    });

    //    batEditButton.addActionListener(new ActionListener() {

    //      public void actionPerformed(ActionEvent e) {

    //        doBatEdit();

    //      }

    //    });

    editButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doEdit();

      }

    });

    sendButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSend();

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

    suggestPassButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        // 填写意见审核

        doSuggestPass();

      }

    });

    unAuditButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 销审

        doUnAudit();

      }

    });

    unTreadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 退回

        doUnTread();

      }

    });

    isSendToNextButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSendNext();

      }

    });

    traceDataButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        doTraceDataButton();

      }

    });

  }

  private void doTraceDataButton() {

    List beanList = getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择一条要进行跟踪的数据！", "错误", JOptionPane.ERROR_MESSAGE);

      return;

    }

    ZcXmcgHt sh = (ZcXmcgHt) beanList.get(0);

    DataFlowConsoleCanvas dfc = new DataFlowConsoleCanvas(sh.getZcMakeCode(), getCompoId());

    dfc.showWindow();

  }

  public void refreshCurrentTabData() {

    topSearchConditionArea.doSearch();

  }

  public List<ZcXmcgHt> getCheckedList() {

    List<ZcXmcgHt> beanList = new ArrayList<ZcXmcgHt>();

    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();

    MyTableModel model = (MyTableModel) table.getModel();

    //Modal的数据

    List list = model.getList();

    Integer[] checkedRows = table.getCheckedRows();

    for (Integer checkedRow : checkedRows) {

      int accordDataRow = table.convertRowIndexToModel(checkedRow);

      ZcXmcgHt bean = (ZcXmcgHt) list.get(accordDataRow);

      beanList.add(bean);

    }

    return beanList;

  }

  protected void doAdd() {

    new ZcXmcgHtSubDialogDt(self, new ArrayList(1), -1, topDataDisplay.getActiveTableDisplay().getStatus());
  }

  private void doSend() {

    List<ZcXmcgHt> ckList = this.getCheckedList();

    if (ckList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择需要送审的记录！", "提示", JOptionPane.ERROR_MESSAGE);

      return;

    }

    boolean success = true;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.sendButton.getFuncId());

      for (int i = 0; i < ckList.size(); i++) {

        ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(ckList.get(i));

        ht = this.zcXmcgHtServiceDelegate.selectByPrimaryKey(ht.getZcHtCode(), this.requestMeta);

        ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        this.zcXmcgHtServiceDelegate.newCommitFN(ht, false, requestMeta);

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

  private void doBatEdit() {

  }

  private void doEdit() {

    List<ZcXmcgHt> ckList = this.getCheckedList();

    if (ckList.size() > 0) {

      new ZcXmcgHtSubDialogDt(self, ckList, 0, topDataDisplay.getActiveTableDisplay().getStatus());

    } else {

      JOptionPane.showMessageDialog(this, "请选择需要修改的记录！", "提示", JOptionPane.ERROR_MESSAGE);

    }

  }

  private void doDelete() {

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择需要删除的记录！", "提示", JOptionPane.ERROR_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "是否删除当前选中所有合同", "删除确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      boolean success = true;

      ZcXmcgHt zcXmcgHt = null;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(deleteButton.getFuncId());

        for (int i = 0; i < beanList.size(); i++) {

          zcXmcgHt = (ZcXmcgHt) beanList.get(i);

          if (!"0".equals(zcXmcgHt.getZcHtStatus())) {

            JOptionPane.showMessageDialog(this, "所选记录中第【" + (i + 1) + "】条记录处于非编辑状态合同，不可以删除！", "提示", JOptionPane.ERROR_MESSAGE);

            return;

          }

          this.zcXmcgHtServiceDelegate.deleteByPrimaryKeyFN(zcXmcgHt.getZcHtCode(),
            "Y".equals(AsOptionMeta.getOptVal(ZcSettingConstants.OPT_ZC_USE_BUDGET_INTERFACE)) && "ZC_XMCG_HT_SUP".equals(getCompoId()), WorkEnv.getInstance()
              .getWebRoot(), this.requestMeta);

        }

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

  }

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

      ZcXmcgHt bean = (ZcXmcgHt) beanList.get(i);

      ZcUtil.showTraceDialog(bean, getCompoId());

    }

  }

  private void doCallBack() {

    List beanList = getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择需要收回的记录 ！", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    boolean success = true;

    String errorInfo = "";

    try {

      for (int i = 0; i < beanList.size(); i++) {

        requestMeta.setFuncId(this.callbackButton.getFuncId());

        ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(beanList.get(i));

        ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        this.zcXmcgHtServiceDelegate.callbackFN(ht, requestMeta);

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

      JOptionPane.showMessageDialog(this, "打印完成！\n", "提示", JOptionPane.INFORMATION_MESSAGE);

    }

  }

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

    requestMeta.setPageType(getCompoId() + "_L");

    new PrintSettingDialog(requestMeta);

  }

  private void doSuggestPass() {

    boolean success = true;

    String errorInfo = "";

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

        ZcXmcgHt ht = (ZcXmcgHt) beanList.get(i);

        ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        ht.setComment(commentDialog.getComment());

        this.zcXmcgHtServiceDelegate.auditFN(ht, requestMeta);

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

  private void doUnAudit() {

    boolean success = true;

    String errorInfo = "";

    requestMeta.setFuncId(this.unAuditButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int m = JOptionPane.showConfirmDialog(this, "是否确定消审？", "确认", JOptionPane.INFORMATION_MESSAGE);

    if (m != 0) {

      return;

    }

    try {

      for (int i = 0; i < beanList.size(); i++) {

        ZcXmcgHt ht = (ZcXmcgHt) beanList.get(i);

        ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        this.zcXmcgHtServiceDelegate.unAuditFN(ht, requestMeta);

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

    }

  }

  private void doUnTread() {

    boolean success = true;

    String errorInfo = "";

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

        ZcXmcgHt ht = (ZcXmcgHt) beanList.get(i);

        ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        ht.setComment(commentDialog.getComment());

        this.zcXmcgHtServiceDelegate.untreadFN(ht, requestMeta);

      }

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.refreshCurrentTabData();

    }

  }

  private void doSendNext() {

    requestMeta.setFuncId(this.isSendToNextButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int sel = JOptionPane.showConfirmDialog(this, "是否送主任审核？");

    boolean success = true;

    for (int i = 0; i < beanList.size(); i++) {

      ZcXmcgHt make = (ZcXmcgHt) beanList.get(i);

      if (sel == JOptionPane.OK_OPTION) {

        make.setZcConText("1");

      } else {

        make.setZcConText("0");

      }

      //zcXmcgHtServiceDelegate.updateZcXmcgHt(make, requestMeta);

      make.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      System.out.println("......................>>>");

      zcXmcgHtServiceDelegate.auditFN(make, requestMeta);

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.refreshCurrentTabData();

    }

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

        //        UIManager.getDefaults().put("SplitPaneUI", BigButtonSplitPaneUI.class.getName());

        ZcXmcgHtSubListPanelDt bill = new ZcXmcgHtSubListPanelDt();

        JFrame frame = new JFrame("frame");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 600);

        frame.setLocationRelativeTo(null);

        frame.getContentPane().add(bill);

        frame.setVisible(true);

      }

    });

  }

  private BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(getCompoId());

  public BillElementMeta getBillElementMeta() {

    return billElementMeta;

  }

}
