package com.ufgov.zc.client.zc.zcebentrust.cancel;

import java.awt.Color;
import java.awt.Container;
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
import com.ufgov.zc.client.common.converter.zc.ZcEbEntrustCancelToTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;
import com.ufgov.zc.client.util.BalanceUtil;
import com.ufgov.zc.client.util.ListUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcEbEntrustCancel;
import com.ufgov.zc.common.zc.publish.IZcEbEntrustCancelServiceDelegate;

public class ZcEbEntrustCancelListPanel extends AbstractEditListBill implements ParentWindowAware {

  /**
   * 
   */
  private static final long serialVersionUID = -8469402727603392691L;

  private static final Logger logger = Logger.getLogger(ZcEbEntrustCancelListPanel.class);

  private ZcEbEntrustCancelListPanel self = this;

  private Window parentWindow;

  private String compoId = "ZC_EB_ENTRUST_CANCEL";

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private ElementConditionDto elementConditionDto = new ElementConditionDto();

  public BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(compoId);
  public IZcEbEntrustCancelServiceDelegate zcEbEntrustCancelServiceDelegate = (IZcEbEntrustCancelServiceDelegate) ServiceFactory.create(
    IZcEbEntrustCancelServiceDelegate.class, "zcEbEntrustCancelServiceDelegate");

  public BillElementMeta getBillElementMeta() {

    return billElementMeta;

  }

  @Override
  public void setParentWindow(Window window) {
    // TCJLODO Auto-generated method stub
    parentWindow = window;
  }

  @Override
  public Window getParentWindow() {
    // TCJLODO Auto-generated method stub
    return parentWindow;
  }

  private final class DataDisplay extends MultiDataDisplay {

    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,

    boolean showConditionArea) {

      super(displays, showingDisplays, conditionArea, showConditionArea, ZcSettingConstants.TAB_ID_ZC_EB_ENTRUST_CANCEL);

      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "任务取消", TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体",

      Font.BOLD, 15), Color.BLUE));

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

              new ZcEbEntrustCancelDialog(self, viewList, row, tabStatus);

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

      elementConditionDto.setMonth(BalanceUtil.getMonthIdBySysOption());

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

          ZcEbEntrustCancelToTableModelConverter mc = new ZcEbEntrustCancelToTableModelConverter();

          return mc.convertToTableModel(self.zcEbEntrustCancelServiceDelegate.selectZcEbEntrustCancel(elementConditionDto, requestMeta));

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

  public ZcEbEntrustCancelListPanel() {

    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {

      @Override
      public List<SearchCondition> execute() throws Exception {

        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonListJoinRole(WorkEnv.getInstance()

        .getCurrUserId(), ZcSettingConstants.TAB_ID_ZC_EB_ENTRUST_CANCEL);

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

    topSearchConditionArea = new SaveableSearchConditionArea(ZcSettingConstants.CONDITION_ID_ZC_EB_ENTRUST_CANCEL, null, true, defaultValueMap, null);

    return topSearchConditionArea;

  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {

    return new DataDisplay(SearchConditionUtil.getAllTableDisplayJoinRole(WorkEnv.getInstance().getCurrUserId(),

    ZcSettingConstants.TAB_ID_ZC_EB_ENTRUST_CANCEL), showingDisplays, createTopConditionArea(), true);//true:显示收索条件区 false：不显示收索条件区

  }
  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    // TCJLODO Auto-generated method stub

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(compoId);

    toolBar.add(addButton);

    toolBar.add(deleteButton);

    toolBar.add(sendButton);

    toolBar.add(traceButton);

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

  }

  private void doAdd(){
    new ZcEbEntrustCancelDialog(self, new ArrayList(), this.topDataDisplay.getActiveTableDisplay().getTable().getRowCount()
      , topDataDisplay.getActiveTableDisplay().getStatus());
  }

  private void doDelete(){
    List list = getCheckedList();
    if(list.size() == 0){

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    try{
      this.zcEbEntrustCancelServiceDelegate.deleteZcEbEntrustCancelListFN(list, requestMeta);
      JOptionPane.showMessageDialog(this, "删除成功！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      this.refreshCurrentTabData();
    }catch(Exception e){

      JOptionPane.showMessageDialog(this, "删除失败！" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void doSend() {
    List list = getCheckedList();
    if(list.size() == 0){

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    String errorInfo = "";

    boolean success = true;

    try {

      requestMeta.setFuncId(sendButton.getFuncId());

      for (int i = 0; i < list.size(); i++){

        ZcEbEntrustCancel entrust = (ZcEbEntrustCancel) list.get(i);

        this.zcEbEntrustCancelServiceDelegate.newCommitFN(entrust, requestMeta);
      }

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "送审失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  private void doTrace() {

    ZcUtil.showTraceDialog(getCheckedList(), this);

  }

  public List getCheckedList() {

    List beanList = new ArrayList();

    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();

    MyTableModel model = (MyTableModel) table.getModel();

    //Modal的数据

    List list = model.getList();

    Integer[] checkedRows = table.getCheckedRows();

    for (Integer checkedRow : checkedRows) {

      int accordDataRow = table.convertRowIndexToModel(checkedRow);

      beanList.add(list.get(accordDataRow));

    }

    return beanList;

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

        ZcEbEntrustCancelListPanel bill = new ZcEbEntrustCancelListPanel();

        JFrame frame = new JFrame("frame");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 600);

        frame.setLocationRelativeTo(null);

        frame.getContentPane().add(bill);

        frame.setVisible(true);

      }

    });

  }

  public void refreshCurrentTabData() {

    topSearchConditionArea.doSearch();


  }
}
