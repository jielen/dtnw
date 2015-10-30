package com.ufgov.zc.client.zc.agencyaptd;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.DefaultInvokeHandler;
import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcPProMakeToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.HelpButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcBAgencyListAptd;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.publish.IZcEBAgencyServiceDelegate;

@SuppressWarnings("unchecked")
public class ZcEbAgencyAptdListPanel extends AbstractEditListBill implements ParentWindowAware {

  public static final long serialVersionUID = 1277810469937954543L;

  public static final Logger logger = Logger.getLogger(ZcEbAgencyAptdListPanel.class);

  public ZcEbAgencyAptdListPanel self = this;

  public Window parentWindow;

  public RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  public ElementConditionDto elementConditionDto = new ElementConditionDto();

  public IZcEBAgencyServiceDelegate agencyServiceDelegate = (IZcEBAgencyServiceDelegate) ServiceFactory.create(
    IZcEBAgencyServiceDelegate.class, "agencyServiceDelegate");

  public IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(
    IBaseDataServiceDelegate.class, "baseDataServiceDelegate");

  private ZcBAgencyAptdTableModel _tabbleModel;

  public String getCompoId() {
    return "ZC_B_AGENCY_APD";
  }

  public String getTabId() {
    return ZcSettingConstants.TAB_ID_ZC_B_AGENCY_APD;
  }

  public Window getParentWindow() {
    return parentWindow;
  }

  public void setParentWindow(Window parentWindow) {
    this.parentWindow = parentWindow;
  }

  public String getTitle() {
    return "资质等级登记";
  }

  public void setElementConditionDto(ElementConditionDto dto) {
    // 所有采购方式
    dto.setZcText1(ZcPProMake.CAIGOU_TYPE_All);
  }

  public ZcEbAgencyAptEditPanel getEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus,
    ZcEbAgencyAptdListPanel listPanel) {
    return new ZcEbAgencyAptEditPanel(parent, listCursor, tabStatus, listPanel);
  }

  public final class DataDisplay extends MultiDataDisplay {

    public DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays,
      AbstractSearchConditionArea conditionArea, boolean showConditionArea) {
      super(displays, showingDisplays, conditionArea, showConditionArea, getTabId());
      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), getTitle(), TitledBorder.CENTER,
        TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
    }

    @Override
    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems,
      final TableDisplay tableDisplay) {
      elementConditionDto.setWfcompoId(getCompoId());
      elementConditionDto.setExecutor(WorkEnv.getInstance().getCurrUserId());
      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());
      elementConditionDto.setStatus(tableDisplay.getStatus());
      elementConditionDto.setZcText0(WorkEnv.getInstance().getCurrUserId());
      setElementConditionDto(elementConditionDto);
      for (AbstractSearchConditionItem item : searchConditionItems) {
        item.putToElementConditionDto(elementConditionDto);
      }

      final Container c = tableDisplay.getTable().getParent();

      _tabbleModel = new ZcBAgencyAptdTableModel(this);
      tableDisplay.setTableModel(_tabbleModel);
      _tabbleModel.refresh();
    }
  }

  static {
    LangTransMeta.init("ZC%");
  }

  /**
   * 构造函数
   */
  public ZcEbAgencyAptdListPanel() {
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil
          .getNeedDisplaySearchConditonListJoinRole(WorkEnv.getInstance().getCurrUserId(), getTabId());
        return needDisplaySearchConditonList;

      }

      @Override
      public void success(List<SearchCondition> needDisplaySearchConditonList) {
        List<TableDisplay> showingDisplays = SearchConditionUtil
          .getNeedDisplayTableDisplay(needDisplaySearchConditonList);
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
    topSearchConditionArea = new SaveableSearchConditionArea(ZcSettingConstants.CONDITION_ID_ZC_P_PRO_MAKE, null,
      false, defaultValueMap, null);
    return topSearchConditionArea;
  }

  public AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {
    return new DataDisplay(SearchConditionUtil.getAllTableDisplayJoinRole(WorkEnv.getInstance().getCurrUserId(),
      getTabId()), showingDisplays, createTopConditionArea(), false);//true:显示收索条件区 false：不显示收索条件区
  }

  public AddButton addButton = new AddButton();

  public HelpButton helpButton = new HelpButton();

  public EditButton editButton = new EditButton();

  public SaveButton saveButton = new SaveButton();

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(getCompoId());
    toolBar.add(addButton);
    toolBar.add(deleteButton);
    toolBar.add(editButton);
    toolBar.add(saveButton);
    toolBar.add(helpButton);

    // 初始化按钮的action事件
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doAdd();
        _tabbleModel.setUpdateSignal(true);
      }
    });

    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doDelete();
      }
    });

    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        _tabbleModel.setUpdateSignal(true);
      }
    });

    saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          if (topDataDisplay.getActiveTableDisplay().getTable().getCellEditor()!= null) {
            topDataDisplay.getActiveTableDisplay().getTable().getCellEditor().stopCellEditing();
          }
          ZcBAgencyListAptd agency = null;
          for (int i = 0; i < _tabbleModel.getInsertList().size(); i++) {
            agency = (ZcBAgencyListAptd)_tabbleModel.getInsertList().get(i);
            if (agency.getZcAptdCode() == null || "".equals(agency.getZcAptdCode().trim())) {
              JOptionPane.showMessageDialog(ZcEbAgencyAptdListPanel.this, "新增数据资质编码不能为空！", " 提示", JOptionPane.INFORMATION_MESSAGE);
              return;
            }
            if (agency.getZcAptdName() == null || "".equals(agency.getZcAptdName().trim())) {
                  JOptionPane.showMessageDialog(ZcEbAgencyAptdListPanel.this, "新增数据资质名称不能为空！", " 提示", JOptionPane.INFORMATION_MESSAGE);
                  return;
            }

          }
          _tabbleModel.save();
          _tabbleModel.setUpdateSignal(false);
          JOptionPane.showMessageDialog(ZcEbAgencyAptdListPanel.this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
          _tabbleModel.refresh();
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(ZcEbAgencyAptdListPanel.this, "保存失败 ！\n" + ex.getMessage(), "错误",
            JOptionPane.ERROR_MESSAGE);
        }
      }
    });
  }

  public void refreshCurrentTabData() {
    //topSearchConditionArea.doSearch();
    this._tabbleModel.refresh();
  }

  public void refreshCurrentTabData(List beanList) {
    topDataDisplay.getActiveTableDisplay().getTable()
      .setModel(ZcPProMakeToTableModelConverter.convertToTableModel(beanList));
  }

  public List getCheckedList() {
    //代码错误
    //	List<ZcPProMake> beanList = new ArrayList<ZcPProMake>();
    List<ZcBAgencyListAptd> beanList = new ArrayList<ZcBAgencyListAptd>();
    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();
    ZcBAgencyAptdTableModel model = (ZcBAgencyAptdTableModel) table.getModel();
    //Modal的数据
    List list = model.getRecordList();
    Integer[] checkedRows = table.getCheckedRows();
    for (Integer checkedRow : checkedRows) {
      int accordDataRow = table.convertRowIndexToModel(checkedRow);
      //代码错误
      //      ZcPProMake bean = (ZcPProMake) list.get(accordDataRow);
      ZcBAgencyListAptd bean = (ZcBAgencyListAptd) list.get(accordDataRow);
      beanList.add(bean);
    }
    return beanList;
  }

  public void doAdd() {
    //new ZcEbAgencyAptDialog(self, new ArrayList(1), -1, topDataDisplay.getActiveTableDisplay().getStatus());
    this._tabbleModel.insertRecord();
    topDataDisplay.getActiveTableDisplay().getTable().setModel(_tabbleModel);
    
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
        ZcEbAgencyAptdListPanel bill = new ZcEbAgencyAptdListPanel();
        JFrame frame = new JFrame("frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }
    });
  }

  public IZcEBAgencyServiceDelegate getAgencyServiceDelegate() {
    return agencyServiceDelegate;
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
        ElementConditionDto dto = new ElementConditionDto();
        ZcBAgencyListAptd zcBAgency = null;
        for (int i = 0; i < beanList.size(); i++) {

          //关联代理机构的记录不能删除
          zcBAgency = (ZcBAgencyListAptd) beanList.get(i);
          if(zcBAgency.getZcAptdCode() != null && !"".equals(zcBAgency.getZcAptdCode().trim())){
	          dto.setZcText1(zcBAgency.getZcAptdCode());
	          List list = agencyServiceDelegate.getZcZcBAgencyAptdList(dto, requestMeta);
	          if (list != null && list.size() > 0) {
	            JOptionPane.showMessageDialog(this, "不能删除与代理机构关联 的资质等级！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
	            return;
	          }
          }
          if(!_tabbleModel.getInsertList().contains(zcBAgency)){
        	  agencyServiceDelegate.doDeleteApds(zcBAgency, requestMeta);
          }
      	  this._tabbleModel.deleteRecord(zcBAgency);
      	  topDataDisplay.getActiveTableDisplay().getTable().setModel(_tabbleModel);
        }

      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        success = false;
        errorInfo += e.getMessage();
      }

      if (success) {
        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
//        this.refreshCurrentTabData();
      } else {
        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

}
