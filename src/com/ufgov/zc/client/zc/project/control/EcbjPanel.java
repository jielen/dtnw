package com.ufgov.zc.client.zc.project.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.converter.zc.ZcEbProjectControlToTableModelConverter;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateTime24CellRenderer;
import com.ufgov.zc.client.component.zc.fieldeditor.JDateTimeTextField;
import com.ufgov.zc.client.component.zc.fieldeditor.JDateTimeTextField.DateChangeEvent;
import com.ufgov.zc.client.component.zc.fieldeditor.JDateTimeTextField.DateChangeListener;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.activeztb.TbDocService;
import com.ufgov.zc.client.zc.ztb.model.DetailsType;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcEbEcbjItem;
import com.ufgov.zc.common.zc.model.ZcEbEcbjPlan;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbProjPackVO;

public class EcbjPanel extends JTabbedPane {

  private ZcEbProjControlSubEditPanelService panelService;

  private ZcEbProjPackVO currVO;

  private JSaveableSplitPane splitPaneEcbj;

  private HashMap<String, JPanel> ecbjTablePanelCach = new HashMap<String, JPanel>();

  private JPanel ecbjTablePanel;

  //二次报价汇总信息panel
  public JTablePanel ecbjSummaryInfoTabPanel = new JTablePanel("ecbjSummaryInfoTabPanel");

  //二次报价信息panel
  public JTablePanel ecbjTableInfoTabPanel = new JTablePanel("ecbjTableInfoTabPanel");

  //二次报价计划管理
  public JTablePanel ecbjPlanTabPanel = new JTablePanel("ecbjPlanTabPanel");

  //二次报价计划明细
  public JTablePanel ecbjPlanListTabPanel = new JTablePanel("ecbjPlanListTabPanel");

  JDateTimeTextField ecbjStartDateField = new JDateTimeTextField("", JDateTimeTextField.TimeTypeH24);

  JDateTimeTextField ecbjEndDateField = new JDateTimeTextField("", JDateTimeTextField.TimeTypeH24);

  JDateTimeTextField ecbjOpenBidDateField = new JDateTimeTextField("", JDateTimeTextField.TimeTypeH24);

  JTextField ecbjOpenBidAddressField = new JTextField();

  JTextField ecbjRemarkField = new JTextField();

  JButton optPlanButton = new JButton();

  JButton deletePlanButton = new JButton();

  JButton cancelPlanButton = new JButton();

  String currOptType = null;//insert或者update

  Long lastClickTime = 0L;

  JLabel tipLabel = new JLabel();

  JLabel alertLabel = new JLabel();

  JComboBox timesCB = new JComboBox();

  private static final String MODIFYPLANTEXT = "修改计划";

  private static final String SAVEPLANTEXT = "保  存";

  private TbDocService tb = new TbDocService();

  public EcbjPanel(ZcEbProjControlSubEditPanelService panelService, ZcEbProjPackVO currVO) {
    this.panelService = panelService;
    this.currVO = currVO;
    doProcessEcbjPanel(currVO.getZcEbPack());
  }

  /**
   * 填充供应商再次报价信息面板
   *
   * @param pack
   */
  public void refreshSplitPaneEcbj(ZcEbPack pack) {
    if (splitPaneEcbj == null) {
      return;
    } else {
      splitPaneEcbj.remove(ecbjSummaryInfoTabPanel);
      splitPaneEcbj.remove(ecbjTableInfoTabPanel);
    }
    ecbjSummaryInfoTabPanel.init();
    ecbjSummaryInfoTabPanel.getSearchBar().setVisible(false);
    ecbjSummaryInfoTabPanel.setTablePreferencesKey(this.getClass().getName() + "_summary_info");
    ecbjSummaryInfoTabPanel.setMinimumSize(new Dimension(240, 260));
    splitPaneEcbj.add(ecbjSummaryInfoTabPanel);
    ecbjTableInfoTabPanel.init();
    ecbjTableInfoTabPanel.getSearchBar().setVisible(false);
    ecbjTableInfoTabPanel.setTablePreferencesKey(this.getClass().getName() + "_ecbj_info");
    ecbjTableInfoTabPanel.setMinimumSize(new Dimension(240, 260));
    splitPaneEcbj.add(ecbjTableInfoTabPanel);
    refreshEcbjSummaryInfoTabPanel(pack);
  }

  /**
   * 显示二次报价一览表
   *
   * @param item
   */
  private void refreshTabPanelEcbjTableInfo(ZcEbEcbjItem item) {
    ecbjTableInfoTabPanel.removeAll();
    ecbjTableInfoTabPanel.add(getEcbjTablePn(item));
    ecbjTableInfoTabPanel.updateUI();
  }

  private JPanel getEcbjTablePn(ZcEbEcbjItem ecbjItem) {
    if (ecbjItem.getStatus() != null && ecbjItem.getStatus().equals(ZcSettingConstants.FIELD_TRANS_ZC_BJ_OPEN)) {
      final String key = ecbjItem.getFileId() + "_" + ecbjItem.getPackCode();
      if (ecbjTablePanelCach.containsKey(key)) {
        return this.ecbjTablePanelCach.get(key);
      }
      createEcbjTablePn(ecbjItem, true);
    } else {
      this.ecbjTablePanel = new JPanel();
      this.ecbjTablePanel.setPreferredSize(new Dimension(600, 300));
    }
    return this.ecbjTablePanel;
  }

  private JPanel createEcbjTablePn(ZcEbEcbjItem ecbjItem, boolean flag) {
    File file = new File(tb.getFileDownloadPath() + File.separator + ecbjItem.getFileId());
    if (!file.exists()) {
      //获取报价金额
      File tmpFile = tb.downLoadEcBjFile(ecbjItem.getFileId());
      if (tmpFile == null) {
        this.ecbjTablePanel = new JPanel();
        String info = "因为数据库中缺少对应的再次报价表文件数据，此次无法显示具体报价情况...";
        this.ecbjTablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), info, TitledBorder.CENTER,
          TitledBorder.TOP, new Font("宋体", Font.BOLD, 12), Color.RED));
        this.ecbjTablePanel.setPreferredSize(new Dimension(600, 300));
        return ecbjTablePanel;
      }
      ecbjItem.setBjSum(tb.getEcbjSum(file));
    }
    List<String> paramList = new ArrayList<String>();
    paramList.add(ecbjItem.getProjName());
    paramList.add(ecbjItem.getPackName());
    paramList.add(GV.NODE_NAME_TBYLB);
    paramList.add("Y");
    ecbjTablePanel = tb.getEcbjSummaryTable(file, paramList);
    ecbjTablePanelCach.put(ecbjItem.getFileId() + "_" + ecbjItem.getPackCode(), ecbjTablePanel);
    return ecbjTablePanel;
  }

  private void refreshEcbjSummaryInfoTabPanel(ZcEbPack pack) {
    ElementConditionDto dto = new ElementConditionDto();
    dto.setProjCode(pack.getProjCode());
    dto.setPackCode(pack.getPackCode());
    List itemList = panelService.editPanel.zcEbEcbjServiceDelegate.getZcEbEcbjItemList(dto, panelService.editPanel.requestMeta);
    ecbjSummaryInfoTabPanel.setTableModel(ZcEbProjectControlToTableModelConverter.convertEcbjSummaryToTableModel(itemList));
    ZcUtil.translateColName(ecbjSummaryInfoTabPanel.getTable(), ZcEbProjectControlToTableModelConverter.getEcbjSummaryColumnInfo());
    addEcbjSummaryTableLisenter(ecbjSummaryInfoTabPanel.getTable());
    if (itemList.size() > 0) {
      this.refreshTabPanelEcbjTableInfo((ZcEbEcbjItem) itemList.get(0));
    }
  }

  private void refreshEcbjPlanTable(ZcEbPack pack) {
    ElementConditionDto dto = new ElementConditionDto();
    dto.setProjCode(pack.getProjCode());
    dto.setPackCode(pack.getPackCode());
    List<ZcEbEcbjPlan> list = panelService.editPanel.zcEbEcbjServiceDelegate.getZcEbEcbjPlanList(dto, panelService.editPanel.requestMeta);
    ecbjPlanListTabPanel.setTableModel(ZcEbProjectControlToTableModelConverter.convertEcbjPlanToTableModel(list));
    ZcUtil.translateColName(ecbjPlanListTabPanel.getTable(), ZcEbProjectControlToTableModelConverter.getEcbjPlanColumnInfo());
    if (list.size() > 0) {
      updatePlanEditorFields(list.get(0));
    }
    setPlanTableEditor(ecbjPlanListTabPanel.getTable());
  }

  private void setPlanTableEditor(final JPageableFixedTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    SwingUtil.setTableCellRenderer(table, "START_TIME", new DateTime24CellRenderer());
    SwingUtil.setTableCellRenderer(table, "END_TIME", new DateTime24CellRenderer());
    SwingUtil.setTableCellRenderer(table, "OPEN_BID_TIME", new DateTime24CellRenderer());
  }

  private void startEcbjProcessing() {
    ecbjPlanTabPanel.init();
    ecbjPlanTabPanel.setName(this.getClass().getName() + "_ecbjplan");
    ecbjPlanTabPanel.getSearchBar().setVisible(false);
    ecbjPlanTabPanel.setTablePreferencesKey(this.getClass().getName() + "_ecbjplan");
    this.addTab("再次报价计划管理", ecbjPlanTabPanel);
    splitPaneEcbj = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPaneEcbj.setDividerSize(10);
    splitPaneEcbj.setName(this.getClass().getName() + "_ecbjSplit");
    splitPaneEcbj.setDividerDefaultLocation(this.getClass().getName() + "_ecbjSplit", 200);
    splitPaneEcbj.setDividerLocation(220);
    splitPaneEcbj.setContinuousLayout(true);
    splitPaneEcbj.setOneTouchExpandable(true);
    this.addTab("再次报价信息", splitPaneEcbj);
    this.updateUI();
  }

  private JPanel createTopAddingNewPlanFieldPanel() {
    JPanel planPanel = new JPanel();
    planPanel.setPreferredSize(new Dimension(600, 164));
    planPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(5, 5, 0, 5);
    int gridy = 0;
    int width = 120;
    c.gridx = 0;
    c.gridy = gridy;
    c.gridwidth = 10;
    tipLabel.setPreferredSize(new Dimension(500, 25));
    tipLabel.setHorizontalAlignment(SwingUtilities.LEFT);
    planPanel.add(tipLabel, c);
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 1;
    JLabel l00 = new JLabel("截止时间设定:");
    planPanel.add(l00, c);
    c.gridx = 1;
    c.gridy = gridy;
    c.gridwidth = 2;
    Vector<DetailsType> dtVector = getOptions();
    timesCB = new JComboBox(dtVector);
    timesCB.setPreferredSize(new Dimension(140, 22));
    timesCB.setSelectedItem(0);
    addComboBoxSelectChangeActions();
    planPanel.add(timesCB, c);
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 1;
    JLabel l1 = new JLabel("开始时间:");
    planPanel.add(l1, c);
    c.gridx = 1;
    c.gridy = gridy;
    c.gridwidth = 2;
    ecbjStartDateField.setPreferredSize(new Dimension(width, 20));
    ecbjStartDateField.setText("");
    controlField(ecbjStartDateField, false);
    planPanel.add(ecbjStartDateField, c);
    ecbjStartDateField.addDateChangeListener(new DateChangeListener() {
      public void dateChanged(DateChangeEvent e) {
        if (ecbjStartDateField.getDate() == null) {
          return;
        }
        long sTime = ecbjStartDateField.getDate().getTime();
        int later = 20;
        if (timesCB != null && timesCB.getSelectedItem() != null) {
          later = Integer.parseInt(((DetailsType) timesCB.getSelectedItem()).getValue());
        }
        long newTime = sTime + later * 60 * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat(ZcSettingConstants.SIMPLE_DATE_FORMAT_FULL);
        ecbjEndDateField.setText(sdf.format(new Date(newTime)));
      }
    });
    c.gridx = 3;
    c.gridy = gridy;
    c.gridwidth = 1;
    l1 = new JLabel("  开标地点:");
    planPanel.add(l1, c);
    c.gridx = 4;
    c.gridy = gridy;
    c.gridwidth = 4;
    ecbjOpenBidAddressField.setPreferredSize(new Dimension(260, 20));
    controlField(ecbjOpenBidAddressField, false);
    planPanel.add(ecbjOpenBidAddressField, c);
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 1;
    l1 = new JLabel("截止时间:");
    planPanel.add(l1, c);
    c.gridx = 1;
    c.gridy = gridy;
    c.gridwidth = 2;
    ecbjEndDateField.setPreferredSize(new Dimension(width, 20));
    ecbjEndDateField.setText("");
    controlField(ecbjEndDateField, false);
    planPanel.add(ecbjEndDateField, c);
    ecbjEndDateField.addDateChangeListener(new DateChangeListener() {
      public void dateChanged(DateChangeEvent e) {
        if (ecbjEndDateField.getDate() == null) {
          return;
        }
        ecbjOpenBidDateField.setText(ecbjEndDateField.getText());
      }
    });
    c.gridx = 3;
    c.gridy = gridy;
    c.gridwidth = 1;
    planPanel.add(new JLabel("  备    注:"), c);
    c.gridx = 4;
    c.gridy = gridy;
    c.gridwidth = 4;//转行
    ecbjRemarkField.setPreferredSize(new Dimension(260, 20));
    controlField(ecbjRemarkField, false);
    planPanel.add(ecbjRemarkField, c);
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 1;
    l1 = new JLabel("开标时间:");
    planPanel.add(l1, c);
    c.gridx = 1;
    c.gridy = gridy;
    c.gridwidth = 2;//转行
    c.fill = GridBagConstraints.NONE;
    ecbjOpenBidDateField.setPreferredSize(new Dimension(width, 20));
    ecbjOpenBidDateField.setText("");
    controlField(ecbjOpenBidDateField, false);
    planPanel.add(ecbjOpenBidDateField, c);
    c.gridx = 4;
    c.gridy = gridy;
    c.gridwidth = 1;
    optPlanButton.setPreferredSize(new Dimension(100, 20));
    optPlanButton.setText(MODIFYPLANTEXT);
    optPlanButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (!isGoon(arg0)) {
          return;
        }
        if (MODIFYPLANTEXT.equals(optPlanButton.getText())) {
          currOptType = "update";
          controlFields(true);
          optPlanButton.setText(SAVEPLANTEXT);
          return;
        } else {
          boolean stat = false;
          String info = null;
          if ("update".equals(currOptType)) {
            stat = doSaveOrUpdateEcbjPlan("update");
            info = "计划修改成功！";
          } else if ("insert".equals(currOptType)) {
            stat = doSaveOrUpdateEcbjPlan("insert");
            info = "新的再次报价计划创建成功，供应商可以开始上传再次报价文件了...";
          }
          if (!stat) {
            return;
          }
          alertLabel.setText(getAlertInfo(info));
          tipLabel.setText(null);
          controlFields(false);
          optPlanButton.setText(MODIFYPLANTEXT);
          deletePlanButton.setEnabled(true);
        }
      }
    });
    planPanel.add(optPlanButton, c);
    c.gridx = 6;
    c.gridy = gridy;
    c.gridwidth = 1;
    deletePlanButton.setPreferredSize(new Dimension(80, 20));
    deletePlanButton.setText("删除计划");
    deletePlanButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (!isGoon(arg0)) {
          return;
        }
        doDeleteEcbjPlan();
      }
    });
    planPanel.add(deletePlanButton, c);
    c.gridx = 7;
    c.gridy = gridy;
    c.gridwidth = 1;
    cancelPlanButton.setPreferredSize(new Dimension(100, 20));
    cancelPlanButton.setText("取  消");
    cancelPlanButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        controlFields(false);
        optPlanButton.setText(MODIFYPLANTEXT);
      }
    });
    planPanel.add(cancelPlanButton, c);
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 10;
    alertLabel.setPreferredSize(new Dimension(500, 25));
    alertLabel.setHorizontalAlignment(SwingUtilities.LEFT);
    planPanel.add(alertLabel, c);
    return planPanel;
  }

  private void refreshEcbjPlanTabPanel(ZcEbPack pack) {
    ecbjPlanTabPanel.removeAll();
    ecbjPlanTabPanel.setLayout(new BorderLayout());
    ecbjPlanTabPanel.add(createTopAddingNewPlanFieldPanel(), BorderLayout.NORTH);
    ecbjPlanListTabPanel.init();
    ecbjPlanListTabPanel.setName(this.getClass().getName() + "_ecbjplanlist");
    ecbjPlanListTabPanel.getSearchBar().setVisible(false);
    ecbjPlanListTabPanel.setTablePreferencesKey(this.getClass().getName() + "_ecbjplanlist");
    addEcbjPlanListTabPanelListener(ecbjPlanListTabPanel.getTable());
    ecbjPlanTabPanel.add(ecbjPlanListTabPanel, BorderLayout.CENTER);
    ecbjPlanTabPanel.updateUI();
    this.refreshEcbjPlanTable(pack);
  }

  private void updatePlanEditorFields(ZcEbEcbjPlan plan) {
    //处于编辑状态时，选择下面的行将不能更新编辑框中的数据
    if (ecbjStartDateField.isEnabled()) {
      return;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(ZcSettingConstants.SIMPLE_DATE_FORMAT_FULL);
    if (plan.getStartTime() != null) {
      ecbjStartDateField.setText(sdf.format(plan.getStartTime()));
    } else {
      ecbjStartDateField.setText(null);
    }
    if (plan.getEndTime() != null) {
      ecbjEndDateField.setText(sdf.format(plan.getEndTime()));
    } else {
      ecbjEndDateField.setText(null);
    }
    if (plan.getEndTime() != null) {
      ecbjOpenBidDateField.setText(sdf.format(plan.getOpenBidTime()));
    } else {
      ecbjOpenBidDateField.setText(null);
    }
    ecbjOpenBidAddressField.setText(plan.getOpenBidAddress());
    currPlanCode = plan.getPlanCode();
    ecbjRemarkField.setText(plan.getRemark());
    optPlanButton.setText(MODIFYPLANTEXT);
    controlFields(false);
  }

  /**
   * 进入再次报价：
   * <p/>
   * 0、先检查当前标段状态是否满足转入二次报价的流程：
   * <p/>
   * ——>评标到技术汇总阶段;
   * <p/>
   * ——>当前标段的上一次报价是否已经完成？
   * <p/>
   * 1、新建一个二次报价计划页签；
   * <p/>
   * 2、新建一个再次报价信息页签；
   * <p/>
   * <p/>
   * <p/>
   * 特别点：
   * <p/>
   * 1、需要处理好新建页签时页签内的内容处理，因为这部分实际上可能是第一次创建，也可能是已经有数据了；
   * <p/>
   * 2、如果项目已经进行过一次二次报价，那么需要做特别的适当处理；
   */
  public void doEcbj(boolean needCheck) {
    ZcEbPack pack = currVO.getZcEbPack();
    if (needCheck) {
      if (!doCheckPackStatus(pack)) {
        return;
      }
    }
    startEcbjProcessing();
    refreshEcbjPlanTabPanel(pack);
    refreshSplitPaneEcbj(pack);
    if (needCheck) {
      tipLabel.setText(getAlertInfo("请录入新的再次报价计划，保存后将自动启动新一轮的报价流程。"));
      optPlanButton.setText(SAVEPLANTEXT);
      currOptType = "insert";
      currPlanCode = null;
      controlFields(true);
      ecbjStartDateField.setText(null);
      ecbjEndDateField.setText(null);
      ecbjOpenBidDateField.setText(null);
      ecbjOpenBidAddressField.setText(currVO.getZcEbPlan().getOpenBidAddress());
      //      tabPaneBase.setSelectedIndex(tabPaneBase.getComponentZOrder(ecbjPlanTabPanel));
      deletePlanButton.setEnabled(false);
    } else {
      tipLabel.setText(null);
      optPlanButton.setText(MODIFYPLANTEXT);
      currOptType = "update";
      deletePlanButton.setEnabled(true);
    }
  }

  private void addEcbjPlanListTabPanelListener(final JPageableFixedTable table) {
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          BeanTableModel tableModel = (BeanTableModel) table.getModel();
          if (table.getSelectedRows() != null && table.getSelectedRows().length > 0) {
            int sel = table.convertRowIndexToModel(table.getSelectedRows()[0]);
            ZcEbEcbjPlan plan = (ZcEbEcbjPlan) tableModel.getBean(sel);
            updatePlanEditorFields(plan);
          }
        }
      }
    });
  }

  private void addEcbjSummaryTableLisenter(final JPageableFixedTable table) {
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          BeanTableModel tableModel = (BeanTableModel) table.getModel();
          if (table.getSelectedRows() != null && table.getSelectedRows().length > 0) {
            int sel = table.convertRowIndexToModel(table.getSelectedRows()[0]);
            ZcEbEcbjItem item = (ZcEbEcbjItem) tableModel.getBean(sel);
            refreshTabPanelEcbjTableInfo(item);
          }
        }
      }
    });
  }

  String currPlanCode = null;

  private void addComboBoxSelectChangeActions() {
    timesCB.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent arg0) {
        if (ecbjStartDateField.getDate() == null || !ecbjStartDateField.isEnabled()) {
          return;
        }
        long sTime = ecbjStartDateField.getDate().getTime();
        int later = 20;
        later = Integer.parseInt(((DetailsType) timesCB.getSelectedItem()).getValue());
        long newTime = sTime + later * 60 * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat(ZcSettingConstants.SIMPLE_DATE_FORMAT_FULL);
        ecbjEndDateField.setText(sdf.format(new Date(newTime)));
      }
    });
  }

  private Vector<DetailsType> getOptions() {
    Vector<DetailsType> v = new Vector<DetailsType>();
    int s = 10;
    for (int i = 0; i < 11; i++) {
      String name = "开始时间 +" + s + "分钟";
      String code = s + "";
      v.add(new DetailsType(name, code));
      s += 5;
    }
    return v;
  }

  private boolean isGoon(ActionEvent arg0) {
    Long currTime = arg0.getWhen();
    if (currTime - lastClickTime < 500) {
      lastClickTime = currTime;
      return false;
    }
    lastClickTime = currTime;
    return true;
  }

  private void controlField(JComponent field, boolean flag) {
    field.setEnabled(flag);
    if (field instanceof JTextField) {
      ((JTextField) field).setEditable(false);
    }
  }

  private void controlFields(boolean flag) {
    ecbjStartDateField.setEnabled(flag);
    ecbjStartDateField.setEditable(flag);
    ecbjEndDateField.setEnabled(flag);
    ecbjEndDateField.setEditable(flag);
    ecbjOpenBidDateField.setEnabled(flag);
    ecbjOpenBidDateField.setEditable(flag);
    ecbjOpenBidAddressField.setEnabled(flag);
    ecbjOpenBidAddressField.setEditable(flag);
    ecbjRemarkField.setEnabled(flag);
    ecbjRemarkField.setEditable(flag);
  }

  private boolean doSaveOrUpdateEcbjPlan(String opt) {
    ZcEbPack pack = currVO.getZcEbPack();
    if (!toCheckFieldDateOK()) {
      return false;
    }
    ZcEbEcbjPlan plan = new ZcEbEcbjPlan();
    plan.setProjCode(pack.getProjCode());
    plan.setPackCode(pack.getPackCode());
    plan.setStartTime(ecbjStartDateField.getDate());
    plan.setEndTime(ecbjEndDateField.getDate());
    plan.setOpenBidTime(ecbjOpenBidDateField.getDate());
    plan.setOpenBidAddress(ecbjOpenBidAddressField.getText());
    plan.setRemark(ecbjRemarkField.getText());
    if ("insert".equals(opt)) {
      panelService.editPanel.zcEbEcbjServiceDelegate.saveZcEbEcbjPlanFN(plan, panelService.editPanel.requestMeta);
    } else {
      plan.setPlanCode(currPlanCode);
      panelService.editPanel.zcEbEcbjServiceDelegate.updateZcEbEcbjPlanFN(plan, panelService.editPanel.requestMeta);
    }
    this.refreshEcbjPlanTable(pack);
    return true;
  }

  private boolean toCheckFieldDateOK() {
    String info = "";
    Date sDate = ecbjStartDateField.getDate();
    if (sDate == null) {
      info = "【开始时间】、";
    } else {
      alertLabel.setText(null);
    }
    Date eDate = ecbjEndDateField.getDate();
    if (eDate == null) {
      info += "【截止时间】、";
    }
    Date oDate = ecbjOpenBidDateField.getDate();
    if (oDate == null) {
      info += "【开标时间】、";
    }
    String addr = ecbjOpenBidAddressField.getText();
    if (addr == null || "".equals(addr)) {
      info += "【开标地址】、";
    }
    if (info != null && !"".equals(info)) {
      info = info.substring(0, info.length() - 1);
      info = info + "不能为空！";
      alertLabel.setText(getAlertInfo(info));
      return false;
    } else {
      if (sDate.after(eDate)) {
        info += "【开始时间】不能晚于【截止时间】、";
      }
      if (eDate.after(oDate)) {
        info += "【截止时间】不能晚于【开标时间】、";
      }
      if (info != null && !"".equals(info)) {
        info = info.substring(0, info.length() - 1);
        info += "!";
        alertLabel.setText(getAlertInfo(info));
        return false;
      }
    }
    alertLabel.setText(getAlertInfo("数据检查通过..."));
    return true;
  }

  private void doDeleteEcbjPlan() {
    if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, "确定要删除当前计划吗？")) {
      ZcEbEcbjPlan plan = new ZcEbEcbjPlan();
      plan.setPlanCode(currPlanCode);
      panelService.editPanel.zcEbEcbjServiceDelegate.deleteZcEbEcbjPlan(plan, panelService.editPanel.requestMeta);
      ZcEbPack pack = currVO.getZcEbPack();
      alertLabel.setText(getAlertInfo("删除成功..."));
      this.refreshEcbjPlanTable(pack);
    }
  }

  private String getAlertInfo(String msg) {
    StringBuffer alertInfo = new StringBuffer("<html><a><font size='3' color='red'>提示：");
    alertInfo.append(msg);
    alertInfo.append("</font><a></html>");
    return alertInfo.toString();
  }

  /**
   * 检查当前标段是否可以转入二次报价流程：
   * <p/>
   * 1、如果项目标段不处于指定的状态，则无法转入再次报价；
   * <p/>
   * 2、如果上次再次报价还没有完成，那么不允许转入再次报价；
   *
   * @param pack
   * @return
   */
  private boolean doCheckPackStatus(ZcEbPack pack) {
    ElementConditionDto dto = new ElementConditionDto();
    dto.setProjCode(pack.getProjCode());
    dto.setPackCode(pack.getPackCode());
    List<ZcEbEcbjPlan> list = panelService.editPanel.zcEbEcbjServiceDelegate.getZcEbEcbjPlanList(dto, panelService.editPanel.requestMeta);
    int count = list.size();
    if (count > 0) {
      ZcEbEcbjPlan lastPlan = list.get(count - 1);
      if (new Date().before(lastPlan.getEndTime())) {
        int sel = JOptionPane.showConfirmDialog(panelService.editPanel.parent, "第【" + count + "】轮再次报价尚未完成，是否继续启动新一轮报价(将覆盖上一轮报价计划)？!");
        if (JOptionPane.OK_OPTION == sel) {
          panelService.editPanel.zcEbEcbjServiceDelegate.deleteZcEbEcbjPlan(lastPlan, panelService.editPanel.requestMeta);
          return true;
        } else {
          return false;
        }
      }
    }
    int sel = JOptionPane.showConfirmDialog(panelService.editPanel.parent, "将启动新一轮再次报价(第" + (count + 1) + "轮)，是否继续？!");
    if (JOptionPane.OK_OPTION == sel) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 检查当前标段是否进行过再次报价
   *
   * @param cPack
   */
  public void doProcessEcbjPanel(ZcEbPack cPack) {
    ElementConditionDto dto = new ElementConditionDto();
    dto.setProjCode(cPack.getProjCode());
    dto.setPackCode(cPack.getPackCode());
    int count = panelService.editPanel.zcEbEcbjServiceDelegate.getZcEbEcbjPlanList(dto, panelService.editPanel.requestMeta).size();
    if (count > 0) {
      doEcbj(false);
    }
  }
}
