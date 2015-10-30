/**
 * 
 */
package com.ufgov.zc.client.zc.zcppromakechg;

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
import com.ufgov.zc.client.common.ZcWorkFlowAdapter;
import com.ufgov.zc.client.common.converter.zc.ZcPProBalChgToTableModelConverter;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;
import com.ufgov.zc.client.util.ListUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.zcpprobichange.ZcPProBalChgDialog;
import com.ufgov.zc.client.zc.zcpprobichange.ZcPProBalChgListPanel;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcPProBalChgConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcPProBalChg;
import com.ufgov.zc.common.zc.publish.IZcPProBalChgServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcPProMakeServiceDelegate;

/**
 * 采购计划资金变更
 * 用于计划审批后，变更采购资金
 * 譬如，指标未下达，提前采购，可以用自筹资金报计划，待指标下达后，通过本功能，变更计划资金
 * 适合采购计划已终审，对采购计划进行资金变化，主要是提前采购，指标未下达，先进行招标采购，指标下达后，挂接新的指标，再录入合同,或者变更合同资金
 * @author Administrator
 *
 */
public class ZcPProMakeChgBiListPanel extends AbstractEditListBill implements ParentWindowAware {

  private static final Logger logger = Logger.getLogger(ZcPProBalChgListPanel.class);

  private ZcPProMakeChgBiListPanel self = this;

  private Window parentWindow;

  private String compoId = "ZC_P_PRO_MAKE_CHG_BI";

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private ElementConditionDto elementConditionDto = new ElementConditionDto();

  public IZcPProBalChgServiceDelegate zcPProBalChgServiceDelegate = (IZcPProBalChgServiceDelegate) ServiceFactory.create(
    IZcPProBalChgServiceDelegate.class, "zcPProBalChgServiceDelegate");

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
      super(displays, showingDisplays, conditionArea, showConditionArea, "ZC_P_PRO_MAKE_CHG_BI_Tab");
      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta
        .translate(ZcPProBalChgConstants.FIELD_TRANS_ZC_P_PRO_MAKE_CHG_BI), TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15),
        Color.BLUE));
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
              new ZcPProMakeChgBiDialog(self, viewList, row, tabStatus);
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
      elementConditionDto.setCoCode(requestMeta.getSvCoCode());
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

          return ZcPProBalChgToTableModelConverter.convertToTableModel(self.zcPProBalChgServiceDelegate.getZcPProBalChgList(elementConditionDto,
            requestMeta));

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
  public ZcPProMakeChgBiListPanel() {
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()
          .getCurrUserId(), ZcPProBalChgConstants.TAB_ID_ZC_P_PRO_BAL_CHG);
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
    topSearchConditionArea = new SaveableSearchConditionArea(ZcPProBalChgConstants.CONDITION_ID_ZC_P_PRO_BAL_CHG, null, false, defaultValueMap, null);
    return topSearchConditionArea;
  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {
    return new DataDisplay(SearchConditionUtil.getAllTableDisplay("ZC_P_PRO_MAKE_CHG_BI_Tab"), showingDisplays,
      createTopConditionArea(), false);//true:显示收索条件区 false：不显示收索条件区
  }

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(compoId);
    toolBar.add(addButton);
    toolBar.add(sendButton);
    toolBar.add(suggestPassButton);
    toolBar.add(callbackButton);//收回
    toolBar.add(deleteButton);
    toolBar.add(traceButton);
    toolBar.add(helpButton);

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
    suggestPassButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        // 填写意见审核
        doSuggestPass();
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

  }

  public void refreshCurrentTabData() {
    topSearchConditionArea.doSearch();
  }

  public List<ZcPProBalChg> getCheckedList() {
    List<ZcPProBalChg> beanList = new ArrayList<ZcPProBalChg>();
    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();
    MyTableModel model = (MyTableModel) table.getModel();
    //Modal的数据
    List list = model.getList();
    Integer[] checkedRows = table.getCheckedRows();
    for (Integer checkedRow : checkedRows) {
      int accordDataRow = table.convertRowIndexToModel(checkedRow);
      ZcPProBalChg bean = (ZcPProBalChg) list.get(accordDataRow);
      beanList.add(bean);
    }
    return beanList;
  }

  private void doAdd() {
    new ZcPProMakeChgBiDialog(self, new ArrayList(1), -1, topDataDisplay.getActiveTableDisplay().getStatus());
  }

  private void doDelete() {
    List beanList = this.getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", "提示", JOptionPane.ERROR_MESSAGE);
      return;
    }
    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      boolean success = true;
      ZcPProBalChg zcPProBal = null;
      String errorInfo = "";
      try {
        requestMeta.setFuncId(deleteButton.getFuncId());
        for (int i = 0; i < beanList.size(); i++) {
          zcPProBal = (ZcPProBalChg) beanList.get(i);
          if (!"0".equals(zcPProBal.getStatus())) {
            JOptionPane.showMessageDialog(this, "非编辑状态单据，不可以删除！", "提示", JOptionPane.ERROR_MESSAGE);
            return;
          }
          this.zcPProBalChgServiceDelegate.deleteZcPProBalChgFN(zcPProBal, WorkEnv.getInstance().getWebRoot(), "Y".equals(AsOptionMeta.getOptVal(ZcSettingConstants.OPT_ZC_USE_BUDGET_INTERFACE)), requestMeta);
        }
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        success = false;
        errorInfo += e.getMessage();
      }
      if (success) {
        JOptionPane.showMessageDialog(this, "删除成功 ！\n", "提示", JOptionPane.INFORMATION_MESSAGE);
        this.refreshCurrentTabData();
      } else {
        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void doSend() {
    List<ZcPProBalChg> ckList = this.getCheckedList();
    if (ckList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", "提示", JOptionPane.ERROR_MESSAGE);
      return;
    }
    boolean success = true;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(this.sendButton.getFuncId());
      for (int i = 0; i < ckList.size(); i++) {
        ZcPProBalChg ht = (ZcPProBalChg) ObjectUtil.deepCopy(ckList.get(i));
        ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());
        ZcBaseBill bill = ZcWorkFlowAdapter.newCommitFN(ht, this, sendButton, requestMeta, null);
        if (bill == null) {
          success = false;
        }
      }
    } catch (Exception ex) {
      errorInfo += ex.getMessage();
      logger.error(ex.getMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "送审成功", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.refreshCurrentTabData();
    }
  }

  private void doEdit() {
    List<ZcPProBalChg> ckList = this.getCheckedList();
    if (ckList.size() > 0) {
      new ZcPProMakeChgBiDialog(self, ckList, 0, topDataDisplay.getActiveTableDisplay().getStatus());
    } else {
      JOptionPane.showMessageDialog(this, LangTransMeta.translate(ZcPProBalChgConstants.FIELD_TRANS_ZC_P_PRO_BAL_CHG_TITLE), "提示",
        JOptionPane.ERROR_MESSAGE);
    }
  }

  /*
   * 填写意见审核
   */
  private void doSuggestPass() {
    List<ZcPProBalChg> ckList = this.getCheckedList();
    if (ckList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", "提示", JOptionPane.ERROR_MESSAGE);
      return;
    }
    boolean success = true;
    ZcPProBalChg afterSaveBill = null;
    String errorInfo = "";
    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      ModalityType.APPLICATION_MODAL);
    if (commentDialog.cancel) {
      return;
    }
    try {
      IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
        "baseDataServiceDelegate");
      requestMeta.setFuncId(this.suggestPassButton.getFuncId());
      for (int i = 0; i < ckList.size(); i++) {
        ZcPProBalChg bean = (ZcPProBalChg) ObjectUtil.deepCopy(ckList.get(i));
        bean.setComment(commentDialog.getComment());
        bean.setAuditorId(WorkEnv.getInstance().getCurrUserId());
        ZcBaseBill bill = ZcWorkFlowAdapter.auditFN(bean, this, sendButton, requestMeta, null);
        if (bill == null) {
          success = false;
        }
      }
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
    List beanList = getCheckedList();
    ZcUtil.showTraceDialog(beanList, this);
  }

  private void doCallBack() {

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
        ZcPProMakeChgBiListPanel bill = new ZcPProMakeChgBiListPanel();
        JFrame frame = new JFrame("frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }
    });

  }

  private BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(compoId);

  public BillElementMeta getBillElementMeta() {
    return billElementMeta;
  }

}
