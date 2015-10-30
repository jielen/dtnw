package com.ufgov.zc.client.zc.supplierprice;import java.awt.BorderLayout;import java.awt.Color;import java.awt.Container;import java.awt.Font;import java.awt.Window;import java.awt.event.MouseAdapter;import java.awt.event.MouseEvent;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import javax.swing.BorderFactory;import javax.swing.JDialog;import javax.swing.JFrame;import javax.swing.JOptionPane;import javax.swing.SwingUtilities;import javax.swing.UIManager;import javax.swing.border.TitledBorder;import javax.swing.table.TableModel;import org.apache.log4j.Logger;import com.ufgov.smartclient.common.DefaultInvokeHandler;import com.ufgov.smartclient.common.UIUtilities;import com.ufgov.smartclient.component.table.JGroupableTable;import com.ufgov.smartclient.plaf.BlueLookAndFeel;import com.ufgov.zc.client.common.LangTransMeta;import com.ufgov.zc.client.common.MyTableModel;import com.ufgov.zc.client.common.ParentWindowAware;import com.ufgov.zc.client.common.ServiceFactory;import com.ufgov.zc.client.common.UIConstants;import com.ufgov.zc.client.common.WorkEnv;import com.ufgov.zc.client.common.converter.ZcEbSignupToTableModelConverter;import com.ufgov.zc.client.component.GkBaseDialog;import com.ufgov.zc.client.component.JFuncToolBar;import com.ufgov.zc.client.component.button.HelpButton;import com.ufgov.zc.client.component.ui.AbstractDataDisplay;import com.ufgov.zc.client.component.ui.AbstractEditListBill;import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;import com.ufgov.zc.client.component.ui.MultiDataDisplay;import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;import com.ufgov.zc.client.component.ui.TableDisplay;import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;import com.ufgov.zc.client.util.BalanceUtil;import com.ufgov.zc.client.zc.flowconsole.DataFlowConsoleCanvas;import com.ufgov.zc.common.commonbiz.model.SearchCondition;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.constants.ZcSettingConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.model.ZcEbEntrust;import com.ufgov.zc.common.zc.model.ZcEbSignup;import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;import com.ufgov.zc.common.zc.publish.IZcEbSignupServiceDelegate;public class SignupListPanel extends AbstractEditListBill implements ParentWindowAware {  private static final Logger logger = Logger.getLogger(SignupListPanel.class);  private SignupListPanel self = this;  private Window parentWindow;  private String compoId = "ZC_EB_SUP_PRICE";  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();  private ElementConditionDto elementConditionDto = new ElementConditionDto();  private IZcEbSignupServiceDelegate zcEbSignupServiceDelegate = (IZcEbSignupServiceDelegate) ServiceFactory.create(IZcEbSignupServiceDelegate.class,  "zcEbSignupServiceDelegate");  private IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,    "zcEbBaseServiceDelegate");  public Window getParentWindow() {    return parentWindow;  }  public void setParentWindow(Window parentWindow) {    this.parentWindow = parentWindow;  }  private final class DataDisplay extends MultiDataDisplay {    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,    boolean showConditionArea) {      super(displays, showingDisplays, conditionArea, showConditionArea, ZcSettingConstants.TAB_ID_ZC_EB_SIGNUP);      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "供应商上传报价明细表", TitledBorder.CENTER, TitledBorder.TOP, new Font(        "宋体",        Font.BOLD, 15), Color.BLUE));    }    protected void preprocessShowingTableDisplay(List<TableDisplay> showingTableDisplays) {      for (final TableDisplay d : showingTableDisplays) {        final JGroupableTable table = d.getTable();        table.addMouseListener(new MouseAdapter() {          public void mouseClicked(MouseEvent e) {            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {              String tabStatus = d.getStatus();              JGroupableTable table = d.getTable();              MyTableModel model = (MyTableModel) table.getModel();              int row = table.getSelectedRow();              row = table.convertRowIndexToModel(row);              ZcEbSignup signUp = (ZcEbSignup) model.getList().get(row);              GkBaseDialog dialog = new GkBaseDialog((JFrame) null, true);              //定点采购和普通采购分项报价表有区别              dialog.setLayout(new BorderLayout());              ZcEbEntrust zcEbEntrust = (ZcEbEntrust) zcEbBaseServiceDelegate.queryObject("ZcEbEntrust.getZcEbEntrustByProjCode",                signUp.getProjCode(), requestMeta);              if (zcEbEntrust != null && "Z02".equals(zcEbEntrust.getZcFukuanType())) {                DdcgPriceDetailEditPanel editPanel = new DdcgPriceDetailEditPanel(dialog, self);                editPanel.setSignup(signUp);                dialog.getContentPane().add(editPanel);              } else {                PriceDetailEditPanel editPanel = new PriceDetailEditPanel(dialog, self);                editPanel.setSignup(signUp);                dialog.getContentPane().add(editPanel);              }              dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);              dialog.setSize(UIConstants.DIALOG_2_LEVEL_WIDTH, UIConstants.DIALOG_2_LEVEL_HEIGHT);              dialog.moveToScreenCenter();              dialog.setVisible(true);            }          }        });      }    }    @Override    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {      elementConditionDto.setWfcompoId(compoId);      elementConditionDto.setExecutor(WorkEnv.getInstance().getCurrUserId());      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());      elementConditionDto.setStatus(tableDisplay.getStatus());      elementConditionDto.setMonth(BalanceUtil.getMonthIdBySysOption());      elementConditionDto.setManageCode(WorkEnv.getInstance().getCurrUserId());      for (AbstractSearchConditionItem item : searchConditionItems) {        item.putToElementConditionDto(elementConditionDto);      }      final Container c = tableDisplay.getTable().getParent();      UIUtilities.asyncInvoke(new DefaultInvokeHandler<TableModel>() {        @Override        public void before() {          assert c != null;          installLoadingComponent(c);        }        @Override        public void after() {          assert c != null;          unInstallLoadingComponent(c);          c.add(tableDisplay.getTable());        }        @Override        public TableModel execute() throws Exception {          ZcEbSignupToTableModelConverter mc = new ZcEbSignupToTableModelConverter();          return mc.convertToTableModel(self.zcEbSignupServiceDelegate.getZcEbSignupSubmit(elementConditionDto, requestMeta));        }        @Override        public void success(TableModel model) {          tableDisplay.setTableModel(model);          setButtonStatus();        }      });    }  }  static {    LangTransMeta.init("ZC%");  }  /**   * 构造函数   */  public SignupListPanel() {    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {      @Override      public List<SearchCondition> execute() throws Exception {        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()        .getCurrUserId(), ZcSettingConstants.TAB_ID_ZC_EB_SIGNUP_SUPPRICE);        return needDisplaySearchConditonList;      }      @Override      public void success(List<SearchCondition> needDisplaySearchConditonList) {        List<TableDisplay> showingDisplays = SearchConditionUtil.getNeedDisplayTableDisplay(needDisplaySearchConditonList);        init(createDataDisplay(showingDisplays), null);//调用父类方法        revalidate();        repaint();      }    });    requestMeta.setCompoId(compoId);  }  private AbstractSearchConditionArea topSearchConditionArea;  private AbstractSearchConditionArea createTopConditionArea() {    Map defaultValueMap = new HashMap();    topSearchConditionArea = new SaveableSearchConditionArea(ZcSettingConstants.CONDITION_ID_ZC_EB_SIGNUP, null, true, defaultValueMap, null);    return topSearchConditionArea;  }  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {    return new DataDisplay(SearchConditionUtil.getAllTableDisplay(ZcSettingConstants.TAB_ID_ZC_EB_SIGNUP_SUPPRICE), showingDisplays,      createTopConditionArea(),      true);//true:显示收索条件区 false：不显示收索条件区  }  private HelpButton helpButton = new HelpButton();  @Override  protected void addToolBarComponent(JFuncToolBar toolBar) {    toolBar.setModuleCode("ZC");    toolBar.setCompoId(compoId);    toolBar.add(addButton);    toolBar.add(helpButton);  }  public void refreshCurrentTabData() {    topSearchConditionArea.doSearch();  }  public List getCheckedList() {    List<ZcEbSignup> beanList = new ArrayList<ZcEbSignup>();    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();    MyTableModel model = (MyTableModel) table.getModel();    //Modal的数据    List list = model.getList();    Integer[] checkedRows = table.getCheckedRows();    for (Integer checkedRow : checkedRows) {      int accordDataRow = table.convertRowIndexToModel(checkedRow);      ZcEbSignup bean = (ZcEbSignup) list.get(accordDataRow);      beanList.add(bean);    }    return beanList;  }  private void setButtonStatus() {  }  public static void main(String[] args) throws Exception {    SwingUtilities.invokeLater(new Runnable() {      public void run() {        try {          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());          UIManager.setLookAndFeel(new BlueLookAndFeel());        } catch (Exception e) {          e.printStackTrace();        }        //        UIManager.getDefaults().put("SplitPaneUI", BigButtonSplitPaneUI.class.getName());        SignupListPanel bill = new SignupListPanel();        JFrame frame = new JFrame("frame");        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        frame.setSize(800, 600);        frame.setLocationRelativeTo(null);        frame.getContentPane().add(bill);        frame.setVisible(true);      }    });  }  public void refreshCurrentTabData(List dataList) {    ZcEbSignupToTableModelConverter mc = new ZcEbSignupToTableModelConverter();    topDataDisplay.getActiveTableDisplay().getTable().setModel(mc.convertToTableModel(dataList));  }  private void doTraceDataButton() {    List beanList = getCheckedList();    if (beanList.size() == 0) {      JOptionPane.showMessageDialog(this, "请选择一条要进行跟踪的数据！", "错误", JOptionPane.ERROR_MESSAGE);      return;    }    ZcEbSignup sh = (ZcEbSignup) beanList.get(0);    DataFlowConsoleCanvas dfc = new DataFlowConsoleCanvas(sh.getProjCode(), this.compoId);    dfc.showWindow();  }}