package com.ufgov.zc.client.zc.catalogue;

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
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbCatalogueToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcPProMakeToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.HelpButton;
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
import com.ufgov.zc.client.print.PrintSettingDialog;
import com.ufgov.zc.client.util.ListUtil;
import com.ufgov.zc.client.zc.agency.ZcEbAgencyListPanel;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.checkrule.BaseRule;
import com.ufgov.zc.common.zc.checkrule.ZcMakeCheckRuleBySX;
import com.ufgov.zc.common.zc.model.ZcBCatalogue;
import com.ufgov.zc.common.zc.publish.IZcEBCatalogueServiceDelegate;

@SuppressWarnings("unchecked")
public class ZcEbCatalogueListPanel extends AbstractEditListBill implements ParentWindowAware {

  public static final long serialVersionUID = 1277810469937954543L;

  public static final Logger logger = Logger.getLogger(ZcEbAgencyListPanel.class);

  public ZcEbCatalogueListPanel self = this;

  public Window parentWindow;

  public RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  public ElementConditionDto elementConditionDto = new ElementConditionDto();

  public IZcEBCatalogueServiceDelegate catalogueServiceDelegate = (IZcEBCatalogueServiceDelegate) ServiceFactory.create(
    IZcEBCatalogueServiceDelegate.class, "catalogueServiceDelegate");

  public IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
    "baseDataServiceDelegate");

  public String getCompoId() {
    return "ZC_B_CATALOGUE";
  }

  public String getTabId() {
    return ZcSettingConstants.TAB_ID_ZC_B_CATALOGUE;
  }

  public Window getParentWindow() {
    return parentWindow;
  }

  public void setParentWindow(Window parentWindow) {
    this.parentWindow = parentWindow;
  }

  public String getTitle() {
    return "集中采购目录";
  }

  public ZcEbCatalogueEditPanel getEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbCatalogueListPanel listPanel) {
    return new ZcEbCatalogueEditPanel(parent, listCursor, tabStatus, listPanel);
  }

  public final class DataDisplay extends MultiDataDisplay {

    public DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,
      boolean showConditionArea) {
      super(displays, showingDisplays, conditionArea, showConditionArea, getTabId());
      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), getTitle(), TitledBorder.CENTER, TitledBorder.TOP, new Font(
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
              List viewList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(model.getList(), table));
              new ZcEbCatalogueDialog(self, viewList, row, tabStatus);
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
      elementConditionDto.setZcText0(WorkEnv.getInstance().getCurrUserId());
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
          return ZcEbCatalogueToTableModelConverter.convertToTableModel(self.catalogueServiceDelegate.getZcCatalogueList(elementConditionDto,
            requestMeta));
        }

        @Override
        public void success(TableModel model) {
          tableDisplay.setTableModel(model);
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
  public ZcEbCatalogueListPanel() {
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonListJoinRole(WorkEnv.getInstance()
          .getCurrUserId(), getTabId());
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
    requestMeta.setCompoId(getCompoId());
  }

  public AbstractSearchConditionArea topSearchConditionArea;

  public AbstractSearchConditionArea createTopConditionArea() {
    Map defaultValueMap = new HashMap();
    topSearchConditionArea = new SaveableSearchConditionArea(ZcSettingConstants.CONDITION_ID_ZC_P_PRO_MAKE, null, false, defaultValueMap, null);
    //    topSearchConditionArea.setVisible(false);
    AbstractSearchConditionItem item = this.topSearchConditionArea.getCondItemsByCondiFieldCode(ConditionFieldConstants.ZC_VS_MAKE_STATUS);
    if (item != null) {
      ((AsValComboboxConditionItem2) item).setValueByCode("0");
    }
    return topSearchConditionArea;
  }

  public AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {
    return new DataDisplay(SearchConditionUtil.getAllTableDisplayJoinRole(WorkEnv.getInstance().getCurrUserId(), getTabId()), showingDisplays,
      createTopConditionArea(), false);//true:显示收索条件区 false：不显示收索条件区
  }

  public AddButton addButton = new AddButton();

  public HelpButton helpButton = new HelpButton();

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(getCompoId());
    toolBar.add(addButton);
    toolBar.add(deleteButton);
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
  }

  public void refreshCurrentTabData() {
    topSearchConditionArea.doSearch();
  }

  public void refreshCurrentTabData(List beanList) {
    topDataDisplay.getActiveTableDisplay().getTable().setModel(ZcPProMakeToTableModelConverter.convertToTableModel(beanList));
  }

  public List getCheckedList() {
    List<ZcBCatalogue> beanList = new ArrayList<ZcBCatalogue>();
    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();
    MyTableModel model = (MyTableModel) table.getModel();
    //Modal的数据
    List list = model.getList();
    Integer[] checkedRows = table.getCheckedRows();
    for (Integer checkedRow : checkedRows) {
      int accordDataRow = table.convertRowIndexToModel(checkedRow);
      ZcBCatalogue bean = (ZcBCatalogue) list.get(accordDataRow);
      beanList.add(bean);
    }
    return beanList;
  }

  public void doAdd() {
    new ZcEbCatalogueDialog(self, new ArrayList(1), -1, topDataDisplay.getActiveTableDisplay().getStatus());
  }

  public BaseRule getZcMakeCheckRule() {
    return ZcMakeCheckRuleBySX.getInstance();
  }

  public void doPrintSetting() {
    requestMeta.setFuncId(this.printSettingButton.getFuncId());
    requestMeta.setPageType(this.getCompoId() + "_L");
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
        ZcEbCatalogueListPanel bill = new ZcEbCatalogueListPanel();
        JFrame frame = new JFrame("frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }
    });
  }

  public IZcEBCatalogueServiceDelegate getCatalogueServiceDelegate() {
    return catalogueServiceDelegate;
  }

  public BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(getCompoId());

  public BillElementMeta getBillElementMeta() {
    return billElementMeta;
  }

  protected void doDelete() {
    boolean success = true;
    String errorInfo = "";
    requestMeta.setFuncId(deleteButton.getFuncId());
    List beanList = this.getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", JOptionPane.YES_NO_OPTION);
    if (num == JOptionPane.YES_OPTION) {
      try {
        for (int i = 0; i < beanList.size(); i++) {
          ZcBCatalogue zcBCatalogue = (ZcBCatalogue) beanList.get(i);
          this.catalogueServiceDelegate.doDelete(zcBCatalogue, this.requestMeta);
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

}
