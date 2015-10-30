package com.ufgov.zc.client.zc.zcebentrust;import com.ufgov.smartclient.common.DefaultInvokeHandler;import com.ufgov.smartclient.common.UIUtilities;import com.ufgov.smartclient.component.table.JGroupableTable;import com.ufgov.smartclient.plaf.BlueLookAndFeel;import com.ufgov.zc.client.common.BillElementMeta;import com.ufgov.zc.client.common.LangTransMeta;import com.ufgov.zc.client.common.MyTableModel;import com.ufgov.zc.client.common.ParentWindowAware;import com.ufgov.zc.client.common.ServiceFactory;import com.ufgov.zc.client.common.WorkEnv;import com.ufgov.zc.client.common.converter.zc.ZcEbCancelEntrustModelConverter;import com.ufgov.zc.client.component.JFuncToolBar;import com.ufgov.zc.client.component.button.AddButton;import com.ufgov.zc.client.component.button.DeleteButton;import com.ufgov.zc.client.component.button.HelpButton;import com.ufgov.zc.client.component.ui.AbstractDataDisplay;import com.ufgov.zc.client.component.ui.AbstractEditListBill;import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;import com.ufgov.zc.client.component.ui.MultiDataDisplay;import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;import com.ufgov.zc.client.component.ui.TableDisplay;import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;import com.ufgov.zc.client.util.ListUtil;import com.ufgov.zc.common.commonbiz.model.SearchCondition;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.constants.WFConstants;import com.ufgov.zc.common.system.constants.ZcElementConstants;import com.ufgov.zc.common.system.constants.ZcSettingConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.util.ObjectUtil;import com.ufgov.zc.common.zc.model.ZcEbCancelEntrust;import com.ufgov.zc.common.zc.publish.IZcEbCancelEntrustDelegate;import java.awt.Color;import java.awt.Container;import java.awt.Font;import java.awt.Window;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.event.MouseAdapter;import java.awt.event.MouseEvent;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import javax.swing.BorderFactory;import javax.swing.JFrame;import javax.swing.JOptionPane;import javax.swing.SwingUtilities;import javax.swing.UIManager;import javax.swing.border.TitledBorder;import javax.swing.table.TableModel;import org.apache.log4j.Logger;/** * 终止暂停任务 * @author wuwba * */public class ZcEbCancelEntrustListPanel extends AbstractEditListBill implements ParentWindowAware {  private static final Logger logger = Logger.getLogger(ZcEbEntrustListPanel.class);  private Window parentWindow;  private String compoId = "ZC_EB_CANCEL_ENTRUST";  private ZcEbCancelEntrustListPanel self = this;  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();  private ElementConditionDto elementConditionDto = new ElementConditionDto();  private BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(compoId);  private IZcEbCancelEntrustDelegate cancelEntrustDelegate = (IZcEbCancelEntrustDelegate) ServiceFactory.create(IZcEbCancelEntrustDelegate.class,  "cancelEntrustDelegate");  public IZcEbCancelEntrustDelegate getCancelEntrustDelegate() {    return cancelEntrustDelegate;  }  public void setCancelEntrustDelegate(IZcEbCancelEntrustDelegate cancelEntrustDelegate) {    this.cancelEntrustDelegate = cancelEntrustDelegate;  }  public Window getParentWindow() {    return parentWindow;  }  public void setParentWindow(Window window) {    this.parentWindow = parentWindow;  }  public RequestMeta getRequestMeta() {    return requestMeta;  }  public void setRequestMeta(RequestMeta requestMeta) {    this.requestMeta = requestMeta;  }  public BillElementMeta getBillElementMeta() {    return billElementMeta;  }  public void setBillElementMeta(BillElementMeta billElementMeta) {    this.billElementMeta = billElementMeta;  }  public ZcEbCancelEntrustListPanel() {    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {      @Override      public List<SearchCondition> execute() throws Exception {        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()        .getCurrUserId(), ZcSettingConstants.TAB_ID_ZC_EB_CANCEL_ENTRUST);        return needDisplaySearchConditonList;      }      @Override      public void success(List<SearchCondition> needDisplaySearchConditonList) {        List<TableDisplay> showingDisplays = SearchConditionUtil.getNeedDisplayTableDisplay(needDisplaySearchConditonList);        init(createDataDisplay(showingDisplays), null);//调用父类方法        revalidate();        repaint();      }    });    requestMeta.setCompoId(compoId);  }  //按钮  private AddButton addButton = new AddButton();  private DeleteButton deleteButton = new DeleteButton();  private HelpButton helpButton = new HelpButton();  protected void addToolBarComponent(JFuncToolBar toolBar) {    toolBar.setModuleCode("ZC");    toolBar.setCompoId(compoId);    toolBar.add(addButton);    toolBar.add(deleteButton);    toolBar.add(helpButton);    //初始化事件    addButton.addActionListener(new ActionListener() {      @Override      public void actionPerformed(ActionEvent e) {        doAdd();      }    });    deleteButton.addActionListener(new ActionListener() {      @Override      public void actionPerformed(ActionEvent e) {        doDelete();      }    });    helpButton.addActionListener(new ActionListener() {      @Override      public void actionPerformed(ActionEvent e) {        doHelp();      }    });  }  protected void doAdd() {    new ZcEbCanCelEntrustDialog(self, new ArrayList(1), -1, topDataDisplay.getActiveTableDisplay().getStatus());  }  protected void doDelete() {    boolean success = true;    String errorInfo = "";    List beanList = this.getCheckedList();    if (beanList.size() == 0) {      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示",      JOptionPane.INFORMATION_MESSAGE);      return;    } else {      int num = JOptionPane.showConfirmDialog(this, "是否删除选中的单据");      if (num != JOptionPane.YES_OPTION) {        return;      }    }    try {      for (int i = 0; i < beanList.size(); i++) {        ZcEbCancelEntrust cancelEntrust = (ZcEbCancelEntrust) beanList.get(i);        if (!"draft".equals(cancelEntrust.getStatus())) {          JOptionPane.showMessageDialog(this, "已经处理单据不可删除！", " 提示",          JOptionPane.INFORMATION_MESSAGE);          return;        }        this.cancelEntrustDelegate.deleteZcEbCancelEntrust(cancelEntrust, requestMeta);      }    } catch (Exception ex) {      errorInfo += ex.getMessage();      logger.error(ex.getMessage(), ex);      success = false;      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());    }    if (success) {      JOptionPane.showMessageDialog(this, "删除成功！", "提示",      JOptionPane.INFORMATION_MESSAGE);      this.refreshCurrentTabData();    }  }  protected void doHelp() {  }  private AbstractSearchConditionArea topSearchConditionArea;  private AbstractSearchConditionArea createTopConditionArea() {    Map defaultValueMap = new HashMap();    topSearchConditionArea = new SaveableSearchConditionArea(ZcSettingConstants.CONDITION_ID_ZC_EB_CANCEL_ENTRUST, null, false, defaultValueMap, null);    return topSearchConditionArea;  }  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {    return new DataDisplay(SearchConditionUtil.getAllTableDisplay(ZcSettingConstants.TAB_ID_ZC_EB_CANCEL_ENTRUST), showingDisplays,    createTopConditionArea(), true);//true:显示收索条件区 false：不显示收索条件区  }  public List getCheckedList() {    List<ZcEbCancelEntrust> beanList = new ArrayList<ZcEbCancelEntrust>();    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();    MyTableModel model = (MyTableModel) table.getModel();    //Modal的数据    List list = model.getList();    Integer[] checkedRows = table.getCheckedRows();    for (Integer checkedRow : checkedRows) {      int accordDataRow = table.convertRowIndexToModel(checkedRow);      ZcEbCancelEntrust bean = (ZcEbCancelEntrust) list.get(accordDataRow);      beanList.add(bean);    }    return beanList;  }  private final class DataDisplay extends MultiDataDisplay {    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,    boolean showConditionArea) {      super(displays, showingDisplays, conditionArea, showConditionArea, ZcSettingConstants.TAB_ID_ZC_EB_CANCEL_ENTRUST);      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta      .translate(ZcElementConstants.TITLE_FIELD_CANCEL_ENTRUST), TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));    }    protected void preprocessShowingTableDisplay(List<TableDisplay> showingTableDisplays) {      for (final TableDisplay d : showingTableDisplays) {        final JGroupableTable table = d.getTable();        table.addMouseListener(new MouseAdapter() {          public void mouseClicked(MouseEvent e) {            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {              String tabStatus = d.getStatus();              JGroupableTable table = d.getTable();              MyTableModel model = (MyTableModel) table.getModel();              int row = table.getSelectedRow();              List viewList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(model.getList(), table));              new ZcEbCanCelEntrustDialog(self, (ArrayList) viewList, row, tabStatus);            }          }        });      }    }    @Override    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {      elementConditionDto.setWfcompoId(compoId);      elementConditionDto.setExecutor(WorkEnv.getInstance().getCurrUserId());      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());      elementConditionDto.setStatus(tableDisplay.getStatus());      elementConditionDto.setZcText1(WorkEnv.getInstance().getRequestMeta().getSvCoCode());      for (AbstractSearchConditionItem item : searchConditionItems) {        item.putToElementConditionDto(elementConditionDto);      }      final Container c = tableDisplay.getTable().getParent();      UIUtilities.asyncInvoke(new DefaultInvokeHandler<TableModel>() {        @Override        public void before() {          assert c != null;          installLoadingComponent(c);        }        @Override        public void after() {          assert c != null;          unInstallLoadingComponent(c);          c.add(tableDisplay.getTable());        }        @Override        public TableModel execute() throws Exception {          return ZcEbCancelEntrustModelConverter          .convertToTableModel(cancelEntrustDelegate.getZcEbCancelEntrustList(elementConditionDto, requestMeta));        }        @Override        public void success(TableModel model) {          tableDisplay.setTableModel(model);          setButtonStatus();        }      });    }  }  static {    LangTransMeta.init("ZC%");  }  public void refreshCurrentTabData() {    topSearchConditionArea.doSearch();  }  private void setButtonStatus() {    String panelId = WFConstants.EDIT_TAB_STATUS_DRAFT;    if (topDataDisplay != null && topDataDisplay.getActiveTableDisplay() != null) {      panelId = topDataDisplay.getActiveTableDisplay().getStatus();    }    if (WFConstants.EDIT_TAB_STATUS_DRAFT.equalsIgnoreCase(panelId)) {      addButton.setVisible(true);      editButton.setVisible(true);      deleteButton.setVisible(true);    } else {      addButton.setVisible(false);      editButton.setVisible(false);      deleteButton.setVisible(false);    }  }  public static void main(String[] args) throws Exception {    SwingUtilities.invokeLater(new Runnable() {      public void run() {        try {          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());          UIManager.setLookAndFeel(new BlueLookAndFeel());        } catch (Exception e) {          e.printStackTrace();        }        ZcEbCancelEntrustListPanel bill = new ZcEbCancelEntrustListPanel();        JFrame frame = new JFrame("frame");        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        frame.setSize(800, 600);        frame.setLocationRelativeTo(null);        frame.getContentPane().add(bill);        frame.setVisible(true);      }    });  }}