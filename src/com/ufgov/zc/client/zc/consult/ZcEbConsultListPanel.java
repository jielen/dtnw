package com.ufgov.zc.client.zc.consult;

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
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbConsultToTableModelConverter;
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
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.BaseException;
import com.ufgov.zc.common.system.exception.DataAlreadyDeletedException;
import com.ufgov.zc.common.system.exception.OtherException;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcEbConsult;
import com.ufgov.zc.common.zc.publish.IZcEbConsultDelegate;

public class ZcEbConsultListPanel extends AbstractEditListBill implements ParentWindowAware {
  /**
   * 
   */
  private static final long serialVersionUID = -2935916881973918136L;

  private Logger logger = Logger.getLogger(getClass());

  private Window parentWindow;

  private ZcEbConsultListPanel self = this;

  private String compoId = "ZC_EB_CONSULT";

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private ElementConditionDto elementConditionDto = new ElementConditionDto();

  private IZcEbConsultDelegate zcEbConsultDelegate = (IZcEbConsultDelegate) ServiceFactory.create(IZcEbConsultDelegate.class, "zcEbConsultDelegate");

  public IZcEbConsultDelegate getZcEbConsultDelegate() {
    return zcEbConsultDelegate;
  }

  public String getCompoId() {
    return compoId;
  }

  public void setParentWindow(Window window) {
    // TCJLODO Auto-generated method stub
    parentWindow = window;
  }

  public Window getParentWindow() {
    // TCJLODO Auto-generated method stub
    return parentWindow;
  }

  private final class DataDisplay extends MultiDataDisplay {

    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,

    boolean showConditionArea) {

      super(displays, showingDisplays, conditionArea, showConditionArea, ZcSettingConstants.TAB_ID_ZC_EB_CONSULT);

      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "咨询建议", TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体",

      Font.BOLD, 15), Color.BLUE));

    }

    protected void preprocessShowingTableDisplay(List<TableDisplay> showingTableDisplays) {

      for (final TableDisplay d : showingTableDisplays) {

        final JGroupableTable table = d.getTable();

        table.addMouseListener(new MouseAdapter() {

          public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
              String status = elementConditionDto.getStatus();
              JGroupableTable table = d.getTable();
              MyTableModel model = (MyTableModel) table.getModel();
              int row = table.getSelectedRow();
              List viewList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(model.getList(), table));
              new ZcEbConsultDialog(self, viewList, row, status);
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

      elementConditionDto.setNd(requestMeta.getSvNd());

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
          return ZcEbConsultToTableModelConverter.convertToTableModel(zcEbConsultDelegate.findConsultList(elementConditionDto, requestMeta));
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

  public ZcEbConsultListPanel() {

    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {

      @Override
      public List<SearchCondition> execute() throws Exception {

        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonListJoinRole(WorkEnv.getInstance()

        .getCurrUserId(), ZcSettingConstants.TAB_ID_ZC_EB_CONSULT);

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

    topSearchConditionArea = new SaveableSearchConditionArea(ZcSettingConstants.CONDITION_ID_ZC_EB_CONSULT, null, true, defaultValueMap, null);

    return topSearchConditionArea;

  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {

    return new DataDisplay(SearchConditionUtil.getAllTableDisplayJoinRole(WorkEnv.getInstance().getCurrUserId(),

    ZcSettingConstants.TAB_ID_ZC_EB_CONSULT), showingDisplays, createTopConditionArea(), true);//true:显示收索条件区 false：不显示收索条件区

  }

  protected void addToolBarComponent(JFuncToolBar toolBar) {
    // TCJLODO Auto-generated method stub
    toolBar.setCompoId(getCompoId());
    toolBar.add(this.addButton);
    toolBar.add(this.sendButton);
    toolBar.add(this.deleteButton);
    toolBar.add(this.traceButton);

    addButton.addActionListener(new ActionListener(){

      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doAdd();
      }

    });

    sendButton.addActionListener(new ActionListener(){

      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doSend();
      }

    });

    deleteButton.addActionListener(new ActionListener(){

      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doDelete();
      }

    });

    traceButton.addActionListener(new ActionListener(){

      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doTrace();
      }

    });
  }

  private void doAdd() {

    new ZcEbConsultDialog(self, new ArrayList(), this.topDataDisplay.getActiveTableDisplay().getTable().getRowCount(), topDataDisplay

    .getActiveTableDisplay().getStatus());
  }

  private void doSend() {

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    boolean success = true;

    String errorInfo = "";

    requestMeta.setFuncId(this.sendButton.getFuncId());

    try {

      for (int i = 0; i < beanList.size(); i++) {

        ZcEbConsult bill = (ZcEbConsult) ObjectUtil.deepCopy(beanList.get(i));

        bill.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        this.zcEbConsultDelegate.newCommitFN(bill, requestMeta);

      }

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshCurrentTabData();

    }

  }

  private void doDelete() {

    List<ZcEbConsult> checkedList = getCheckedList();

    if (checkedList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择需要删除的数据!", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int result = JOptionPane.showConfirmDialog(self, "是否要删除选中的数据?", "删除确认", JOptionPane.YES_NO_OPTION);

    if (result != JOptionPane.YES_OPTION) {

      return;

    }

    List<String> ids = new ArrayList<String>();
    for (int i = 0; i < checkedList.size(); i++) {
      ids.add(checkedList.get(i).getId());
    }

    StringBuffer errorInfo = new StringBuffer("");

    boolean success = true;

    requestMeta.setFuncId(deleteButton.getFuncId());

    try {

      this.zcEbConsultDelegate.deleteConsultByIds(ids, requestMeta);

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

  private void doTrace() {

    ZcUtil.showTraceDialog(getCheckedList(), this);

  }

  public List getCheckedList() {

    List<ZcEbConsult> beanList = new ArrayList<ZcEbConsult>();

    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();

    MyTableModel model = (MyTableModel) table.getModel();

    //Modal的数据

    List list = model.getList();

    Integer[] checkedRows = table.getCheckedRows();

    for (Integer checkedRow : checkedRows) {

      int accordDataRow = table.convertRowIndexToModel(checkedRow);

      ZcEbConsult bean = (ZcEbConsult) list.get(accordDataRow);

      beanList.add(bean);

    }

    return beanList;

  }

  public void refreshCurrentTabData() {

    topSearchConditionArea.doSearch();
  }

  public static void main(String[] args) {
    // TCJLODO Auto-generated method stub

    SwingUtilities.invokeLater(new Runnable() {

      public void run() {

        try {

          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

          UIManager.setLookAndFeel(new BlueLookAndFeel());

        } catch (Exception e) {

          e.printStackTrace();

        }

        AbstractEditListBill bill = new ZcEbConsultListPanel();

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
