/**   
* @(#) project: zcxa
* @(#) file: ZcPProBalListPanel.java
* 
* Copyright 2010 UFGOV, Inc. All rights reserved.
* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
* 
*/
package com.ufgov.zc.client.zc.zcpprobal;

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
import com.ufgov.zc.client.common.converter.zc.ZcPProBalToTableModelConverter;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.zc.CommonButton;
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
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.dto.PrintObject;
import com.ufgov.zc.common.system.model.AsOption;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcPProBal;
import com.ufgov.zc.common.zc.publish.IZcPProBalServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcPProMakeServiceDelegate;

/**
 * 资金支付
 * 特殊情况：
 * OPT_ZC_IS_LINK_FA      Y/N 是否检查资产入库，如果是，则最后一笔支付时，去资产库中检查：1）有没有这个合同的对应的卡片；2）合同金额是否和卡片的购买金额相等； 
 * OPT_ZC_HT_FA_EQUAL_SUM     Y/N 如果要求录入资产，则设置一个变量，对金额是否相等这个条件进行打开和关闭，正常情况是需要判断的，特殊情况，有管理员设置手工关闭这个值，让该支付审批通过，然后再打开。
 * 这个值，由于系统对option的值有缓存，所以读取该值时，不能用常用的方法，要直接从数据库来读取
 * 例如系统运维费用、会议费等，这些不进入资产，这类合同的支付，就必须关掉这个变量，不进行资产金额和合同金额的校验，可以进行支付
* @ClassName: ZcPProBalListPanel
* @Description: TODO(这里用一句话描述这个类的作用)
* @date: 2010-8-2 下午01:55:35
* @version: V1.0 
* @since: 1.0
* @author: Administrator
* @modify: 
*/
public class ZcPProBalListPanel extends AbstractEditListBill implements ParentWindowAware {

  private static final Logger logger = Logger.getLogger(ZcPProBalListPanel.class);

  private final ZcPProBalListPanel self = this;

  private Window parentWindow;

  private final String compoId = "ZC_P_PRO_BAL";
  //关闭资产入库检查
  private FuncButton closeFaCardCheckButton=new CommonButton("fcloseFaCardCheck", "关闭资产入库检查","group_edit.png");
  
  private static final String FaCardCheckTxtClosed="关闭资产入库检查";
  private static final String FaCardCheckTxtOpened="使用资产入库检查";

  private final RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private final ElementConditionDto elementConditionDto = new ElementConditionDto();

  public IZcPProBalServiceDelegate zcPProBalServiceDelegate = (IZcPProBalServiceDelegate) ServiceFactory.create(IZcPProBalServiceDelegate.class,
    "zcPProBalServiceDelegate");

  public IZcPProMakeServiceDelegate ZcPProMakeServiceDelegate = (IZcPProMakeServiceDelegate) ServiceFactory.create(IZcPProMakeServiceDelegate.class,
    "zcPProMakeServiceDelegate");

  private final IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
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
      super(displays, showingDisplays, conditionArea, showConditionArea, ZcPProBalConstants.TAB_ID_ZC_P_PRO_BAL);
      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta
        .translate(ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_TITLE), TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15),
        Color.BLUE));
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
              new ZcPProBalDialog(self, viewList, row, tabStatus);
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

          return ZcPProBalToTableModelConverter.convertToTableModel(self.zcPProBalServiceDelegate.getZcPProBalList(elementConditionDto, requestMeta));

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
  public ZcPProBalListPanel() {
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()
          .getCurrUserId(), ZcPProBalConstants.TAB_ID_ZC_P_PRO_BAL);
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
    setFaCardCheckButton();
  }

  private AbstractSearchConditionArea topSearchConditionArea;

  private AbstractSearchConditionArea createTopConditionArea() {
    Map defaultValueMap = new HashMap();
    topSearchConditionArea = new SaveableSearchConditionArea(ZcPProBalConstants.CONDITION_ID_ZC_P_PRO_BAL, null, false, defaultValueMap, null);
    return topSearchConditionArea;
  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {
    return new DataDisplay(SearchConditionUtil.getAllTableDisplay(ZcPProBalConstants.TAB_ID_ZC_P_PRO_BAL), showingDisplays, createTopConditionArea(),
      true);//true:显示收索条件区 false：不显示收索条件区
  }
  public void setButtonsVisiable(){
    super.setButtonsVisiable();
    if(ZcUtil.isFaCardAdmin()){
      closeFaCardCheckButton.setVisible(true);
      setFaCardCheckButton();
    }else{
      closeFaCardCheckButton.setVisible(false);
    }
  }
  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(compoId);
    toolBar.add(addButton);
//    toolBar.add(sendButton);

//    toolBar.add(suggestPassButton);

    //    toolBar.add(callbackButton);//收回
//    toolBar.add(deleteButton);
    toolBar.add(traceButton);
    toolBar.add(closeFaCardCheckButton);
//    toolBar.add(printButton);
    //    toolBar.add(printPreviewButton);
    //    toolBar.add(printSettingButton);
    //    toolBar.add(helpButton);

    // 初始化按钮的action事件

    closeFaCardCheckButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doCloseFaCardCheck();
      }
    });

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

  public void refreshCurrentTabData() {
    topSearchConditionArea.doSearch();
  }

/**
 * 
 */
  protected void doCloseFaCardCheck() {
    // TODO Auto-generated method stub
    AsOption asOption = baseDataServiceDelegate.getAsOption("OPT_ZC_HT_FA_EQUAL_SUM", requestMeta);
    if(asOption==null)return;
    boolean check=(asOption.getOptVal()==null || asOption.getOptVal().equalsIgnoreCase("N"))?false:true;
    String checkStr="N";
    if(check){
      checkStr="N";
    }else{
      checkStr="Y";
    }
    asOption.setOptVal(checkStr);
    baseDataServiceDelegate.updateOptionVal(asOption, requestMeta);
    _setFaCardCheckButton(!check);
  }

  /**
   * @param check
   */
  private void _setFaCardCheckButton(boolean check) {
    if(check){
      closeFaCardCheckButton.setText(this.FaCardCheckTxtClosed);
      closeFaCardCheckButton.setToolTipText(this.FaCardCheckTxtClosed);
    }else{
      closeFaCardCheckButton.setText(this.FaCardCheckTxtOpened);
      closeFaCardCheckButton.setToolTipText(this.FaCardCheckTxtOpened);      
    }
  }

  private void setFaCardCheckButton() {
    // TODO Auto-generated method stub
    AsOption asOption = baseDataServiceDelegate.getAsOption("OPT_ZC_HT_FA_EQUAL_SUM", requestMeta);
    if(asOption==null)return;
    boolean check=(asOption.getOptVal()==null || asOption.getOptVal().equalsIgnoreCase("N"))?false:true;
    _setFaCardCheckButton(check);
  }
  
  public List<ZcPProBal> getCheckedList() {
    List<ZcPProBal> beanList = new ArrayList<ZcPProBal>();
    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();
    MyTableModel model = (MyTableModel) table.getModel();
    //Modal的数据
    List list = model.getList();
    Integer[] checkedRows = table.getCheckedRows();
    for (Integer checkedRow : checkedRows) {
      int accordDataRow = table.convertRowIndexToModel(checkedRow);
      ZcPProBal bean = (ZcPProBal) list.get(accordDataRow);
      beanList.add(bean);
    }
    return beanList;
  }

  private void doAdd() {
    new ZcPProBalDialog(self, new ArrayList(1), -1, topDataDisplay.getActiveTableDisplay().getStatus());
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
      ZcPProBal zcPProBal = null;
      String errorInfo = "";
      try {
        requestMeta.setFuncId(deleteButton.getFuncId());
        for (int i = 0; i < beanList.size(); i++) {
          zcPProBal = (ZcPProBal) beanList.get(i);
          if (!"0".equals(zcPProBal.getZcBalStatus())) {
            JOptionPane.showMessageDialog(this, "非编辑状态单据，不可以删除！", "提示", JOptionPane.ERROR_MESSAGE);
            return;
          }
          this.zcPProBalServiceDelegate.deleteZcPProBalFN(zcPProBal, requestMeta);
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
    List<ZcPProBal> ckList = this.getCheckedList();
    //    if (ckList.size() == 0) {
    //      JOptionPane
    //        .showMessageDialog(this, LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_TITLE), "提示", JOptionPane.ERROR_MESSAGE);
    //      return;
    //    }
    boolean success = true;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(this.sendButton.getFuncId());
      for (int i = 0; i < ckList.size(); i++) {
        ZcPProBal ht = (ZcPProBal) ObjectUtil.deepCopy(ckList.get(i));
        ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());
        this.zcPProBalServiceDelegate.newCommitFN(ht, requestMeta);
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

  private void doBatEdit() {
  }

  private void doEdit() {
    List<ZcPProBal> ckList = this.getCheckedList();
    if (ckList.size() > 0) {
      new ZcPProBalDialog(self, ckList, 0, topDataDisplay.getActiveTableDisplay().getStatus());
    } else {
      JOptionPane
        .showMessageDialog(this, LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_P_PRO_BAL_TITLE), "提示", JOptionPane.ERROR_MESSAGE);
    }
  }

  /*
   * 填写意见审核
   */
  private void doSuggestPass() {
    List<ZcPProBal> ckList = this.getCheckedList();
    if (ckList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", "提示", JOptionPane.ERROR_MESSAGE);
      return;
    }
    boolean success = true;
    ZcPProBal afterSaveBill = null;
    String errorInfo = "";
    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      ModalityType.APPLICATION_MODAL);
    if (commentDialog.cancel) {
      return;
    }
    try {
      IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
        "baseDataServiceDelegate");
      String isUseBi = AsOptionMeta.getOptVal(ZcSettingConstants.OPT_ZC_USE_BUDGET_INTERFACE);
      requestMeta.setFuncId(this.suggestPassButton.getFuncId());
      for (int i = 0; i < ckList.size(); i++) {
        ZcPProBal bean = (ZcPProBal) ObjectUtil.deepCopy(ckList.get(i));
        bean.setComment(commentDialog.getComment());
        bean.setAuditorId(WorkEnv.getInstance().getCurrUserId());
        afterSaveBill = this.zcPProBalServiceDelegate.auditFN(bean, isUseBi, requestMeta);
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

  private void doPrint() {
    List printList = getCheckedList();
    if (printList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", "提示", JOptionPane.ERROR_MESSAGE);

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
        ZcPProBalListPanel bill = new ZcPProBalListPanel();
        JFrame frame = new JFrame("frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }
    });

  }

  private final BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(compoId);

  public BillElementMeta getBillElementMeta() {
    return billElementMeta;
  }  
 

}
