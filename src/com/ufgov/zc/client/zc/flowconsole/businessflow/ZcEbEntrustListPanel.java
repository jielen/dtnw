package com.ufgov.zc.client.zc.flowconsole.businessflow;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import com.ufgov.smartclient.common.DefaultInvokeHandler;
import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.ZcEbEntrustToTableModelConverter;
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
import com.ufgov.zc.client.zc.flowconsole.DataFlowConsoleCanvas;
import com.ufgov.zc.client.zc.workFlowList.WorkFlowListPanel;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.publish.IZcEbEntrustServiceDelegate;

public class ZcEbEntrustListPanel extends AbstractEditListBill {
  /**
   * 
   */
  private static final long serialVersionUID = 4056419816124039914L;

  private static final String tabCode = "all";

  private final String compoId = "ZC_EB_ENTRUST";

  private final RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private final ZcEbEntrustListPanel self = this;

  public IZcEbEntrustServiceDelegate zcEbEntrustServiceDelegate = (IZcEbEntrustServiceDelegate) ServiceFactory.create(
    IZcEbEntrustServiceDelegate.class, "zcEbEntrustServiceDelegate");

  public ZcEbEntrustListPanel() {
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> rtList = new ArrayList<SearchCondition>();
        SearchCondition c = new SearchCondition();
        c.setConditionFieldCode(tabCode);
        c.setConditionFieldName("全部");
        c.setConditionId("ZcEbEntrust_entrustTab");
        rtList.add(c);
        return rtList;
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

  private AbstractSearchConditionArea createTopConditionArea() {

    return new SaveableSearchConditionArea(ZcSettingConstants.CONDITION_ID_ZC_EB_ENTRUST, null, false, new HashMap(), null);
  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {
    return new DataDisplay(SearchConditionUtil.getAllTableDisplay(ZcSettingConstants.TAB_ID_ZC_EB_ENTRUST), showingDisplays,
      createTopConditionArea(), true);//true:显示收索条件区 false：不显示收索条件区
  }

  private final class DataDisplay extends MultiDataDisplay {

    private static final long serialVersionUID = -3679893515413026345L;

    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,
      boolean showConditionArea) {
      super(displays, showingDisplays, conditionArea, showConditionArea, ZcSettingConstants.TAB_ID_ZC_EB_ENTRUST);
      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "采购任务单", TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体",
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

              ZcEbEntrust sh = (ZcEbEntrust) viewList.get(row);
              DataFlowConsoleCanvas dfc = new DataFlowConsoleCanvas(sh.getZcMakeCode(), self.compoId);
              dfc.showWindow();
            }
          }

        });
      }
    }

    @Override
    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {
      final ElementConditionDto dto = new ElementConditionDto();
      dto.setWfcompoId(compoId);
      dto.setExecutor(WorkEnv.getInstance().getCurrUserId());
      dto.setNd(WorkEnv.getInstance().getTransNd());
      dto.setStatus(tableDisplay.getStatus());
      dto.setZcText1(WorkEnv.getInstance().getRequestMeta().getSvCoCode());
      for (AbstractSearchConditionItem item : searchConditionItems) {
        item.putToElementConditionDto(dto);
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
          return ZcEbEntrustToTableModelConverter.convertToTableModel(zcEbEntrustServiceDelegate.getZcEbEntrust(dto, requestMeta));
        }

        @Override
        public void success(TableModel model) {
          tableDisplay.setTableModel(model);
        }

      });

    }
  }

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    toolBar.add(traceDataButton);
    toolBar.add(traceButton);

    traceDataButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doTraceDataButton();
      }
    });
    traceButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doTrace();
      }
    });
  }

  public List getCheckedList() {
    List<ZcEbEntrust> beanList = new ArrayList<ZcEbEntrust>();
    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();
    MyTableModel model = (MyTableModel) table.getModel();
    //Modal的数据
    List list = model.getList();
    Integer[] checkedRows = table.getCheckedRows();
    for (Integer checkedRow : checkedRows) {
      int accordDataRow = table.convertRowIndexToModel(checkedRow);
      ZcEbEntrust bean = (ZcEbEntrust) list.get(accordDataRow);
      beanList.add(bean);
    }
    return beanList;
  }

  protected void doTraceDataButton() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "请选择一条要进行跟踪的数据！", "错误", JOptionPane.ERROR_MESSAGE);
      return;
    } else {
      if (beanList.size() > 1) {
        JOptionPane.showMessageDialog(this, "只能选择一条数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
    }
    ZcEbEntrust sh = (ZcEbEntrust) beanList.get(0);
    DataFlowConsoleCanvas dfc = new DataFlowConsoleCanvas(sh.getZcMakeCode(), this.compoId);
    dfc.showWindow();
  }

  public void doTrace() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "请选择一条要进行跟踪的数据！", "错误", JOptionPane.ERROR_MESSAGE);
      return;
    } else {
      if (beanList.size() > 1) {
        JOptionPane.showMessageDialog(this, "只能选择一条数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
    }
    ZcEbEntrust sh = (ZcEbEntrust) beanList.get(0);
    WorkFlowListPanel editPanel = new WorkFlowListPanel(sh.getSnCode());
    JFrame frame = new JFrame("工作流程跟踪");
    frame.setSize(UIConstants.DIALOG_2_LEVEL_WIDTH, UIConstants.DIALOG_2_LEVEL_HEIGHT);
    frame.setLocationRelativeTo(null);
    frame.getContentPane().add(editPanel);
    frame.setVisible(true);
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

        ZcEbEntrustListPanel bill = new ZcEbEntrustListPanel();

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
