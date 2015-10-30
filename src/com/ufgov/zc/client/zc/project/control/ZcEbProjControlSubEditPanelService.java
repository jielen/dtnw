package com.ufgov.zc.client.zc.project.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.converter.ZcEbEvalBidTeamStToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcEbProjectControlToTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.RefreshButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.util.ListUtil;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.zbfile.ZcEbZbFilePanel;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcEbEvalBidTeam;
import com.ufgov.zc.common.zc.model.ZcEbEvalBidTeamMember;
import com.ufgov.zc.common.zc.model.ZcEbExpertEvalResult;
import com.ufgov.zc.common.zc.model.ZcEbFormula;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbProjZbFile;
import com.ufgov.zc.common.zc.model.ZcEbSupplierPack;

public class ZcEbProjControlSubEditPanelService {

  public ZcEbProjectControlSubEditPanel editPanel;

  private JTabbedPane tabPaneBase;

  private JSaveableSplitPane splitPane;

  //需求明细
  public JTablePanel tabPanelPackReq = new JTablePanel("tabPanelPackReq");

  public JTablePanel zbFileTabPanel = new JTablePanel("zbFileTabPanel");

  //项目、标段更多详细信息
  public JTablePanel tablePanelMoreInfo = new JTablePanel("tablePanelMoreInfo");

  //供应商信息
  public JTablePanel supplierTabPanel = new JTablePanel("supplierTabPanel");

  //评标专家
  public JTablePanel tabPanelExpertes = new JTablePanel("tabPanelExpertes");

  //专家打分信息
  public JTablePanel supplierExpertEvalTabPanel = new JTablePanel("supplierExpertEvalTabPanel");

  public EcbjPanel ecbjPanel;

  private final static String PROJ_PACK_TITLE = "分包信息";

  private final static String ZB_FILE_TITLE = "项目招标文件";

  private final static String SUPPLIER_INFO_TITLE = "投标供应商信息";

  private final static String EXPERT_INFO_TITLE = "评标专家";

  private ZcEbProjZbFile zbFile;
  
  private boolean editStatus=false;

  //参与本标段投标的供应商列表
  List joinBidSupplierList = new ArrayList();
//保存投标报价信息,离线评标时使用
  private SaveButton saveOffLineBidScoreBtn;
  private RefreshButton refreshBtn;

  public ZcEbProjControlSubEditPanelService(ZcEbProjectControlSubEditPanel editPanel) {
    this.editPanel = editPanel;
    ecbjPanel = new EcbjPanel(this, editPanel.currVO);
  }

  public void refreshSubTable(ZcEbPack cPack) {
    this.refreshSubPackSupplierTable(cPack);
    this.refreshSubPackReqTable(cPack);
    this.refreshMoreProjPackDetailsTablePanel(cPack);
    this.refreshZbFileTable();
    this.refreshExpertesTableData();
    ecbjPanel.refreshSplitPaneEcbj(cPack);
    ecbjPanel.doProcessEcbjPanel(cPack);
    tabPaneBase.repaint();

  }

  public JTabbedPane createSubBillPanel() {
    tabPaneBase = new JTabbedPane();
    tabPaneBase.setMinimumSize(new Dimension(240, 240));
    supplierTabPanel.init();
    supplierTabPanel.getSearchBar().setVisible(false);
    supplierTabPanel.setName(this.getClass().getName() + "_supplier_info");
    supplierTabPanel.setTablePreferencesKey(this.getClass().getName() + "_supplier_info");
    supplierTabPanel.setMinimumSize(new Dimension(600, 320));
    JFuncToolBar bottomToolBar1 = new JFuncToolBar();
    saveOffLineBidScoreBtn = new SaveButton();
    refreshBtn=new RefreshButton();
    bottomToolBar1.add(saveOffLineBidScoreBtn);
    bottomToolBar1.add(refreshBtn);
    supplierTabPanel.add(bottomToolBar1,BorderLayout.SOUTH);
    saveOffLineBidScoreBtn.addActionListener(new ControlAction("doSaveOffLineEvalBid", "保存开标信息", editPanel));
    refreshBtn.addActionListener(new ControlAction("doRefresh", "刷新开评标信息", editPanel));

    supplierExpertEvalTabPanel.init();
    supplierExpertEvalTabPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "专家评分情况", TitledBorder.CENTER,
      TitledBorder.TOP, new Font("宋体", Font.BOLD, 12), Color.RED));
    supplierExpertEvalTabPanel.getSearchBar().setVisible(false);
    supplierExpertEvalTabPanel.setName(this.getClass().getName() + "_supplier_rate");
    supplierExpertEvalTabPanel.setTablePreferencesKey(this.getClass().getName() + "_supplier_rate");
    supplierExpertEvalTabPanel.setMinimumSize(new Dimension(240, 240));
    addExpertTableLisenterForDoubleClick(supplierExpertEvalTabPanel.getTable());
    splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, supplierTabPanel, supplierExpertEvalTabPanel);
    splitPane.setContinuousLayout(true);
    splitPane.setOneTouchExpandable(true);
    splitPane.putClientProperty("toExpand", true);
    splitPane.setDividerSize(10);
    splitPane.setDividerDefaultLocation((int) (splitPane.getMaximumDividerLocation() * 0.7));
    splitPane.setName(this.getClass().getName() + "_splitPane");
    tabPaneBase.addTab(SUPPLIER_INFO_TITLE, splitPane);
    //分包信息

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    tabPanelPackReq.init();
    tabPanelPackReq.getSearchBar().setVisible(false);
    tabPanelPackReq.setName(this.getClass().getName() + "_detail");
    tabPanelPackReq.setTablePreferencesKey(this.getClass().getName() + "_detail");
    tablePanelMoreInfo.init();
    tablePanelMoreInfo.getSearchBar().setVisible(false);
    tablePanelMoreInfo.setName(this.getClass().getName() + "_moredetail");
    tablePanelMoreInfo.setTablePreferencesKey(this.getClass().getName() + "_moredetail");

    panel.add(tablePanelMoreInfo, BorderLayout.NORTH);
    panel.add(tabPanelPackReq, BorderLayout.CENTER);

    tabPaneBase.addTab(PROJ_PACK_TITLE, panel);

    zbFileTabPanel.init();
    zbFileTabPanel.getSearchBar().setVisible(false);
    zbFileTabPanel.setName(this.getClass().getName() + "_zbfile");
    zbFileTabPanel.setTablePreferencesKey(this.getClass().getName() + "_zbfile");
    zbFileTabPanel.putClientProperty("toExpand", true);
    tabPaneBase.addTab(ZB_FILE_TITLE, zbFileTabPanel);
    tabPanelExpertes.init();
    tabPanelExpertes.getSearchBar().setVisible(false);
    tabPanelExpertes.setName(this.getClass().getName() + "_experteslist");
    tabPanelExpertes.setTablePreferencesKey(this.getClass().getName() + "_experteslist");
    tabPaneBase.addTab(EXPERT_INFO_TITLE, tabPanelExpertes);
    addTabPaneBaseChangeListener();
    return tabPaneBase;

  }



  /**
   * 获取针对当前选中供应商进行了评标的专家
   *
   * @param spack
   */
  private void refreshSubPackSupplierExpertEvalTable(ZcEbSupplierPack spack) {
    Map<String, String> paraMap = new HashMap<String, String>();
    paraMap.put("PROJ_CODE", spack.getProjCode());
    paraMap.put("PACK_CODE", spack.getPackCode());
    paraMap.put("PROVIDER_CODE", spack.getSupplierCode());
    List expertEvalLst1 = editPanel.zcEbEvalServiceDelegate.getExpertEvalPackResList(paraMap, editPanel.requestMeta);
    ZcEbEvalBidTeam team=new ZcEbEvalBidTeam();
    team.setProjCode(spack.getProjCode());
    team.setPackCode(spack.getPackCode());
    List expertTeamMenberList=editPanel.zcEbEvalBidTeamServiceDelegate.getEvalBidTeamMembers(team, editPanel.requestMeta);
    List expertEvalLst=createExpertEvalLst(expertEvalLst1,expertTeamMenberList,spack);
    supplierExpertEvalTabPanel.setTableModel(ZcEbProjectControlToTableModelConverter.convertSupplierExpertEvalToTableModel(expertEvalLst));
    ZcUtil.translateColName(supplierExpertEvalTabPanel.getTable(), ZcEbProjectControlToTableModelConverter.getSupplierExpertEvalTableColumnInfo());
    setSupplierExpertEvalTableProperty(supplierExpertEvalTabPanel.getTable());
  }

  /*private void setPackSupplierTableProperty(final JTable table) {
    SwingUtil.setTableCellRenderer(table, "IS_PAY_GUARANTEE", new AsValCellRenderer("VS_Y_N_1_0"));
    SwingUtil.setTableCellRenderer(table, "IS_BID", new AsValCellRenderer("VS_Y_N_1_0"));
    SwingUtil.setTableCellEditor(table, "IS_SUBMIT_BID_DOC", new AsValComboBoxCellEditor("VS_Y_N_1_0"));
    SwingUtil.setTableCellRenderer(table, "IS_SUBMIT_BID_DOC", new AsValCellRenderer("VS_Y_N_1_0"));
    SwingUtil.setTableCellEditor(table, "IS_SITE", new AsValComboBoxCellEditor("VS_Y_N_1_0"));
    SwingUtil.setTableCellRenderer(table, "IS_SITE", new AsValCellRenderer("VS_Y_N_1_0"));
  }*/

  private List createExpertEvalLst(List expertEvalLst1, List expertTeamMenberList,ZcEbSupplierPack spack) {
    // TODO Auto-generated method stub
    if(expertTeamMenberList==null)return expertEvalLst1;
    ElementConditionDto dto=new ElementConditionDto();
    dto.setProjCode(spack.getProjCode());
    dto.setPackCode(spack.getPackCode());
    ZcEbFormula formula=editPanel.zcEbFormulaServiceDelegate.getZcEbFormulaByPackCode(dto, editPanel.requestMeta);
    if(formula==null){
      JOptionPane.showMessageDialog(editPanel.parent, "没有对应的评标方法！", "错误", JOptionPane.ERROR_MESSAGE);
      return expertEvalLst1;
    }
    if(expertEvalLst1==null)expertEvalLst1=new ArrayList();
    List rtn=new ArrayList();
    for(int i=0;i<expertTeamMenberList.size();i++){
      ZcEbEvalBidTeamMember expert=(ZcEbEvalBidTeamMember)expertTeamMenberList.get(i);
      boolean haveEval=false;
      for(int j=0;j<expertEvalLst1.size();j++){
        ZcEbExpertEvalResult result=(ZcEbExpertEvalResult)expertEvalLst1.get(j);
        if(result.getExpertCode().equals(expert.getExpertCode())){
          haveEval=true;
          break;
        }
      }
      if(!haveEval){//
        ZcEbExpertEvalResult result=new ZcEbExpertEvalResult();
        result.setProjCode(expert.getProjCode());
        result.setPackCode(expert.getPackCode());
        result.setExpertCode(expert.getExpertCode());
        result.setExpertName(expert.getExpertName());
        result.setProviderCode(spack.getSupplierCode());
        result.setProviderName(spack.getSupplierName());
        result.setFormulaCode(formula.getFormulaCode());
        rtn.add(result);
      }
    }
    rtn.addAll(expertEvalLst1);
    return rtn;
  }

  private void setSupplierExpertEvalTableProperty(final JTable table) {
    SwingUtil.setTableCellRenderer(table, "EVAL_RESULT", new AsValCellRenderer("VS_ZC_EB_EVAL_RESULT"));
    SwingUtil.setTableCellRenderer(table, "IS_COMPLIANCE_RESULT", new AsValCellRenderer("VS_Y/N"));
    SwingUtil.setTableCellRenderer(table, "COMPLIANCE_EVAL_VALUE", new AsValCellRenderer("VS_ZC_EB_COMPLIANCE_VALUE"));
  }

  private void setPackReqTableProperty(final JTable table) {
    SwingUtil.setTableCellEditor(table, "TYPE", new AsValComboBoxCellEditor("VS_ZC_EB_REQUIREMENT_TYPE"));
    SwingUtil.setTableCellRenderer(table, "TYPE", new AsValCellRenderer("VS_ZC_EB_REQUIREMENT_TYPE"));
  }

  protected void setExpertesTableProperty(JTable table) {
    ZcUtil.translateColName(table, "ZC_EB_");
    SwingUtil.setTableCellEditor(table, "SEX", new AsValComboBoxCellEditor("VS_SEX"));
    SwingUtil.setTableCellRenderer(table, "SEX", new AsValCellRenderer("VS_SEX"));
    SwingUtil.setTableCellEditor(table, "EXPERT_EVAL_PROGRESS", new AsValComboBoxCellEditor("VS_EXPERT_EVAL_PROGRESS_STATUS"));
    SwingUtil.setTableCellRenderer(table, "EXPERT_EVAL_PROGRESS", new AsValCellRenderer("VS_EXPERT_EVAL_PROGRESS_STATUS"));
  }

  private void setTableEditor(final JPageableFixedTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
     AsValComboBoxCellEditor asValComboBoxCellEditor1 = new AsValComboBoxCellEditor("VS_ZC_EB_COMPLIANCE_RESULT");
     AsValComboBoxCellEditor asValComboBoxCellEditor2 = new AsValComboBoxCellEditor("VS_ZC_EB_EVAL_RESULT");
    SwingUtil.setTableCellEditor(table, "COMPLIANCE_EVAL_RESULT", asValComboBoxCellEditor1);
    SwingUtil.setTableCellRenderer(table, "COMPLIANCE_EVAL_RESULT", new AsValCellRenderer("VS_ZC_EB_COMPLIANCE_RESULT"));
    SwingUtil.setTableCellEditor(table, "OPEN_BID_STATUS", new AsValComboBoxCellEditor("ZC_VS_OPEN_BID_STATUS"));
    SwingUtil.setTableCellRenderer(table, "OPEN_BID_STATUS", new AsValCellRenderer("ZC_VS_OPEN_BID_STATUS"));
    SwingUtil.setTableCellEditor(table, "EXPERT_EVAL_RESULT", asValComboBoxCellEditor2);
    SwingUtil.setTableCellRenderer(table, "EXPERT_EVAL_RESULT", new AsValCellRenderer("VS_ZC_EB_EVAL_RESULT"));
    SwingUtil.setTableCellRenderer(table, "IS_PAY_GUARANTEE", new AsValCellRenderer("VS_Y_N_1_0"));
    SwingUtil.setTableCellRenderer(table, "IS_BID", new AsValCellRenderer("VS_Y_N_1_0"));
    SwingUtil.setTableCellEditor(table, "IS_SUBMIT_BID_DOC", new AsValComboBoxCellEditor("VS_Y_N_1_0"));
    SwingUtil.setTableCellRenderer(table, "IS_SUBMIT_BID_DOC", new AsValCellRenderer("VS_Y_N_1_0"));
    SwingUtil.setTableCellEditor(table, "IS_SITE", new AsValComboBoxCellEditor("VS_Y_N_1_0"));
    SwingUtil.setTableCellRenderer(table, "IS_SITE", new AsValCellRenderer("VS_Y_N_1_0"));
  }

  private void addExpertTableLisenterForDoubleClick(final JGroupableTable table) {
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
          BeanTableModel model = (BeanTableModel) table.getModel();
          List list = model.getDataBeanList();
          int row = table.getSelectedRow();
          List viewList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(list, table));
          new ZcEbExpertDetailDialog(editPanel.parent, viewList, row, "0",isEditStatus());
        }
      }
    });
  }

  private void addTabPaneBaseChangeListener() {
    tabPaneBase.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        JTabbedPane tp = (JTabbedPane) e.getSource();
        Component activedComp = tp.getSelectedComponent();
        if (activedComp instanceof JTablePanel) {
          JTablePanel tpl = (JTablePanel) activedComp;
          if (tpl.getName().endsWith("_experteslist")) {
            refreshExpertesTableData();
          }/* else if (tpl.getName().endsWith("_zbfile")) {

           refreshZbFileTable();

           } else if (tpl.getName().endsWith("_ecbjSplit")) {

           refreshSplitPaneEcbj(currVO.getZcEbPack());

           }*/
        }
      }
    });
  }

  private void addPackTableLisenter(final JPageableFixedTable table) {
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          BeanTableModel tableModel = (BeanTableModel) table.getModel();
          ZcEbSupplierPack sp;
          if (table.getSelectedRows() != null && table.getSelectedRows().length > 0) {
            sp = (ZcEbSupplierPack) tableModel.getBean(table.convertRowIndexToModel(table.getSelectedRows()[0]));
            refreshSubPackSupplierExpertEvalTable(sp);
          }
        }
      }
    });
  }

  /**
   * 关于当前项目当前标段的更多详细信息
   *
   * @param cPack
   */
  private void refreshMoreProjPackDetailsTablePanel(ZcEbPack cPack) {
    tablePanelMoreInfo.setLayout(new BorderLayout());
    tablePanelMoreInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "分包信息", TitledBorder.CENTER, TitledBorder.TOP,
      new Font("宋体", Font.BOLD, 12), Color.BLUE));
    tablePanelMoreInfo.removeAll();
    tablePanelMoreInfo.add(editPanel.makeFieldEditorsPanel(), BorderLayout.NORTH);
    tablePanelMoreInfo.repaint();
  }

  private void refreshSubPackReqTable(ZcEbPack pack) {

    if (pack.getRequirementDetailList() == null || pack.getRequirementDetailList().size() == 0) {
      List list = editPanel.zcEbBaseServiceDelegate.queryDataForList("ZcEbProj.getZcEbPackReqListByPackCode", pack.getPackCode(),
        editPanel.requestMeta);
      pack.setRequirementDetailList(list);
    }

    tabPanelPackReq.setTableModel(ZcEbProjectControlToTableModelConverter.convertPackReqToTableModel(pack.getRequirementDetailList()));
    ZcUtil.translateColName(tabPanelPackReq.getTable(), ZcEbProjectControlToTableModelConverter.getPackReqTableColumnInfo());
    setPackReqTableProperty(tabPanelPackReq.getTable());
  }

  private void refreshZbFileTable() {
    if (zbFile == null || !editPanel.currVO.getProjCode().equals(zbFile.getProjCode())) {
      zbFile = editPanel.zcEbZbFileServiceDelegate.getZcebZbFileByProjCode(editPanel.currVO.getProjCode(), editPanel.requestMeta);
    }
    if (zbFile == null) {
      String info = "数据库中不存在项目编号为【" + editPanel.currVO.getProjCode() + "】的招标文件.";
      zbFileTabPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), info, TitledBorder.CENTER, TitledBorder.TOP,
        new Font("宋体", Font.BOLD, 12), Color.RED));
      return;
    }
    //显示招标文件
    try {
      ZcEbZbFilePanel zbFilePanel = new ZcEbZbFilePanel(zbFile);
      zbFileTabPanel.add(zbFilePanel);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(editPanel.parent, "下载招标文件失败！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
  }

  private void refreshSubPackSupplierTable(ZcEbPack pack) {
    ElementConditionDto dto = new ElementConditionDto();
    dto.setProjCode(pack.getProjCode());
    dto.setPackCode(pack.getPackCode());
    dto.setStatus(pack.getStatus());
    joinBidSupplierList = editPanel.zcEbSupplierServiceDelegate.getSupplierPackList(dto, editPanel.requestMeta);
    supplierTabPanel.setTableModel(ZcEbProjectControlToTableModelConverter.convertPackSupplierToTableModel(joinBidSupplierList));
    ZcUtil.translateColName(supplierTabPanel.getTable(), ZcEbProjectControlToTableModelConverter.getPackSupplierTableColumnInfo());
    setTableEditor(supplierTabPanel.getTable());
    addPackTableLisenter(supplierTabPanel.getTable());
    if (joinBidSupplierList != null && joinBidSupplierList.size() > 0) {
      refreshSubPackSupplierExpertEvalTable((ZcEbSupplierPack) joinBidSupplierList.get(0));
    }
  }

  protected void refreshExpertesTableData() {
    //添加控制，专家只有在点符合性评审后才能显示
    ZcEbEvalBidTeam team = new ZcEbEvalBidTeam();
    ZcEbPack pack = editPanel.currVO.getZcEbPack();
    team.setProjCode(pack.getProjCode());
    team.setPackCode(pack.getPackCode());
    List expertesList = editPanel.zcEbEvalBidTeamServiceDelegate.getEvalBidTeamMembers(team, editPanel.requestMeta);
    ZcEbEvalBidTeamStToTableModelConverter mc = new ZcEbEvalBidTeamStToTableModelConverter();
    tabPanelExpertes.setTableModel(mc.convertMembersDataToTableModel(expertesList));
    tabPanelExpertes.getTable().setEnabled(false);
    setExpertesTableProperty(tabPanelExpertes.getTable());
  }


  public boolean isEditStatus() {
    return editStatus;
  }

  public void setEditStatus(boolean editStatus) {
    this.editStatus = editStatus;
    setButtonStatus();
  }

  private void setButtonStatus() {
    // TODO Auto-generated method stub
    saveOffLineBidScoreBtn.setEnabled(isEditStatus());
  }

  public List getJoinBidSupplierList() {
    return joinBidSupplierList;
  }
}
