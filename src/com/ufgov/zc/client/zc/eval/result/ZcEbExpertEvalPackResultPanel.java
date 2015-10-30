/**   

* @(#) project: GK

* @(#) file: ZcEbExpertEvalPackResultPanel.java

* 

* Copyright 2010 UFGOV, Inc. All rights reserved.

* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.

* 

*/

package com.ufgov.zc.client.zc.eval.result;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.EvalResultToTableModelConverter;
import com.ufgov.zc.client.component.AsValComboBox;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.SumButton;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.util.ListUtil;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.expertEval.Constants;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.model.AsVal;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.EvalExpert;
import com.ufgov.zc.common.zc.model.EvalItemType;
import com.ufgov.zc.common.zc.model.EvalPackProvider;
import com.ufgov.zc.common.zc.model.ZcEbEvalItemResult;
import com.ufgov.zc.common.zc.model.ZcEbEvalPack;
import com.ufgov.zc.common.zc.model.ZcEbExpertEvalResult;
import com.ufgov.zc.common.zc.model.ZcEbPackEvalResult;
import com.ufgov.zc.common.zc.publish.IZcEbEvalServiceDelegate;

/**

 * 

* @ClassName: ZcEbExpertEvalPackResultPanel

* @Description: 专家评审明细面板

* @date: 2010-11-14 下午03:07:26

* @version: V1.0 

* @since: 1.0

* @author: fanpl

* @modify:

 */

public class ZcEbExpertEvalPackResultPanel extends JPanel {

  private static final long serialVersionUID = 8106312990626667376L;

  private static final String compoId = "ZC_EB_PROJ_CTRL";

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  //汇总方式

  private static final String EXPERT_RES_SUM_WAY_VS_ID = "VS_ZC_EB_EXPERT_RES_SUM_WAY";

  private static final String EXPERT_COMPL_RES_SUM_WAY_VS_ID = "ZC_EXPERT_COMPL_RES_SUM_WAY";

  private JFuncToolBar toolBar = new JFuncToolBar();

  private AsValComboBox sumWayComboBox;

  private JComboBox providerComboBox;

  private JComboBox expertComBox;

  private List<EvalPackProvider> providerList;

  private List<EvalExpert> evalExpertList;

  private AsVal selectedSumWay;

  //汇总按钮

  protected SumButton sumButton;

  private ZcEbEvalPack evalPack;

  private List<ZcEbEvalItemResult> expertDetailEvalResult = new ArrayList<ZcEbEvalItemResult>();

  private List<ZcEbExpertEvalResult> sumTableDataList = new ArrayList<ZcEbExpertEvalResult>();

  private JTablePanel topTable = new JTablePanel();

  private JTablePanel bottomTable = new JTablePanel();

  private JTablePanel sumTablePanel = new JTablePanel();

  private JSplitPane splitPanel;

  private ZcEbEvalResultPortalPanel portalPanel;

  private EvalResultToTableModelConverter modelConverter = new EvalResultToTableModelConverter();

  private boolean isScoreResultSum;

  public ZcEbEvalResultPortalPanel getPortalPanel() {

    return portalPanel;

  }

  public void setPortalPanel(ZcEbEvalResultPortalPanel portalPanel) {

    this.portalPanel = portalPanel;

  }

  protected IZcEbEvalServiceDelegate zcEbEvalServiceDelegate = (IZcEbEvalServiceDelegate) ServiceFactory.create(IZcEbEvalServiceDelegate.class,

  "zcEbEvalServiceDelegate");

  public ZcEbExpertEvalPackResultPanel(ZcEbEvalPack evalPack, boolean isScoreResultSum) {

    this.evalPack = evalPack;

    this.isScoreResultSum = isScoreResultSum;

    refreshData();

    initTopPanel();

    initTable();

    initComponents();

  }

  private void initTopPanel() {

    addToolBarComponent(toolBar);

  }

  //初始化专家评审结果的表格，根据汇总基准的方式和汇总结果的类别不同。

  private void initTable() {

    topTable.init();

    topTable.getSearchBar().add(initSelectProviderPanel(), 1);

    topTable.getSearchBar().add(initSelectExpertPanel(), 2);

    topTable.getSearchField().setVisible(false);

    addTopTableLisenter(topTable.getTable());

    if (isScoreResultSum) {

      topTable.setTableModel(modelConverter.convertScoreResultTableData(sumTableDataList, false));

      ZcUtil.translateColName(topTable.getTable(), modelConverter.getExpertScoreSumResultInfo());

    } else {

      topTable.setTableModel(modelConverter.convertComplResultTableData(sumTableDataList, false));

      ZcUtil.translateColName(topTable.getTable(), modelConverter.getExpertComplSumResultInfo());

      setComplTabledetailEditor(topTable.getTable());

    }

    bottomTable.init();

    bottomTable.getSearchBar().setVisible(false);

    bottomTable.setDoubleBuffered(true);

    if (isScoreResultSum) {

      if (sumTableDataList != null && sumTableDataList.size() > 0) {

        bottomTable.setTableModel(modelConverter.convertScoreDetailTableData(sumTableDataList.get(0).getZcEbEvalScoreItemResultList(), false));

        ZcUtil.translateColName(bottomTable.getTable(), modelConverter.getExpertScoreDetailResultInfo());

      }

    } else {

      if (sumTableDataList != null && sumTableDataList.size() > 0) {

        bottomTable.setTableModel(modelConverter.convertComplDetailTableData(sumTableDataList.get(0).getZcEbEvalComplItemResultList(), false));

        ZcUtil.translateColName(bottomTable.getTable(), modelConverter.getExpertComplDetailResultInfo());

        setComplTabledetailEditor(bottomTable.getTable());

      }

    }

  }

  private void initComponents() {

    splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    splitPanel.setOneTouchExpandable(true);

    splitPanel.setDividerSize(5);

    int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.4);

    splitPanel.setDividerLocation(width);

    splitPanel.setLeftComponent(topTable);

    topTable.setMinimumSize(new Dimension(0, 0));

    JTabbedPane tablePanel = new JTabbedPane();

    tablePanel.add("专家评审明细", bottomTable);

    splitPanel.setRightComponent(tablePanel);

    setLayout(new BorderLayout());

    add(toolBar, BorderLayout.NORTH);

    add(splitPanel, BorderLayout.CENTER);

  }

  private void addTopTableLisenter(final JPageableFixedTable table) {

    table.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {

        if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {

          refreshRightTableData();

        }

      }

    });

    table.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {

          refreshRightTableData();

        }

        super.keyReleased(e);

      }

    });

  }

  private void refreshRightTableData() {

    int row = topTable.getTable().getSelectedRow();

    if (row == -1) {

      row = 0;

    }

    List viewList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(sumTableDataList, topTable.getTable()));

    if (viewList != null && viewList.size() > 0) {

      ZcEbExpertEvalResult result = (ZcEbExpertEvalResult) viewList.get(row);

      if (isScoreResultSum) {

        bottomTable.setTableModel(modelConverter.convertScoreDetailTableData(result.getZcEbEvalScoreItemResultList(), false));

        ZcUtil.translateColName(bottomTable.getTable(), modelConverter.getExpertScoreDetailResultInfo());

      } else {

        bottomTable.setTableModel(modelConverter.convertComplDetailTableData(result.getZcEbEvalComplItemResultList(), false));

        ZcUtil.translateColName(bottomTable.getTable(), modelConverter.getExpertComplDetailResultInfo());

        setComplTabledetailEditor(bottomTable.getTable());

      }

    }

  }

  //初始化汇总方式，根据要汇总的类型不同，汇总方式也不同

  private JPanel initSumWayPanel() {

    JLabel label = new JLabel(LangTransMeta.translate("ZC_EB_EXPERT_RES_SUM_WAY"));

    if (isScoreResultSum) {

      sumWayComboBox = new AsValComboBox(EXPERT_RES_SUM_WAY_VS_ID);

    } else {

      sumWayComboBox = new AsValComboBox(EXPERT_COMPL_RES_SUM_WAY_VS_ID);

    }

    sumWayComboBox.setPreferredSize(new Dimension(200, 20));

    JPanel controlPanel = new JPanel();

    controlPanel.add(label);

    controlPanel.add(sumWayComboBox);

    return controlPanel;

  }

  private JPanel initSelectProviderPanel() {

    List list = new ArrayList(providerList);

    list.add(0, "");

    providerComboBox = new JComboBox(list.toArray());

    providerComboBox.addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent e) {

        if (ItemEvent.SELECTED == e.getStateChange()) {

          topTable.getSearchField().setText(providerComboBox.getSelectedItem().toString() + " " + expertComBox.getSelectedItem().toString());

          refreshRightTableData();

        }

      }

    });

    JLabel label = new JLabel("供应商");

    JPanel selectProviderPanel = new JPanel();

    selectProviderPanel.add(label);

    selectProviderPanel.add(providerComboBox);

    return selectProviderPanel;

  }

  private JPanel initSelectExpertPanel() {

    List list = new ArrayList(evalExpertList);

    //添加一个空字符是为了显示一个空记录

    list.add(0, "");

    expertComBox = new JComboBox(list.toArray());

    expertComBox.addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent e) {

        if (ItemEvent.SELECTED == e.getStateChange()) {

          topTable.getSearchField().setText(expertComBox.getSelectedItem().toString() + " " + providerComboBox.getSelectedItem().toString());

          refreshRightTableData();

        }

      }

    });

    JLabel label = new JLabel("评审专家");

    JPanel selectExpertPanel = new JPanel();

    selectExpertPanel.add(label);

    selectExpertPanel.add(expertComBox);

    return selectExpertPanel;

  }

  public void refreshData() {

    Map<String, String> providerSelectMap = new HashMap<String, String>();

    /**

     * 获得评审供应商的个数，

     * 符合性评审的供应商要求供应商缴纳保证金，通过了采购中心的符合性初审。

     * 技术打分的评审供应要求供应商通过了专家的符合性评审。

     * 

     */

    providerSelectMap.put("PACK_CODE", evalPack.getPackCode());

    providerSelectMap.put("PROJ_CODE", evalPack.getProjCode());

    //过滤符合性初审不符合要求的供应商

    if (isScoreResultSum) {

      providerSelectMap.put("FILTER_BY_COMPLIANCE_RES", "Y");

    }

    //    providerList = zcEbEvalServiceDelegate.getEvalPackProviderList(providerSelectMap, requestMeta);

    providerList = zcEbEvalServiceDelegate.getProviderList(providerSelectMap, requestMeta);

    //获得该标段下的所有评标专家

    ElementConditionDto dto = new ElementConditionDto();

    dto.setProjCode(evalPack.getProjCode());

    dto.setPackCode(evalPack.getPackCode());

    if (isScoreResultSum) {

      dto.setZcText1(EvalItemType.SCORE);

    } else {

      dto.setZcText1(EvalItemType.COMPLIANICE);

    }

    evalExpertList = zcEbEvalServiceDelegate.getZcEbEvalExpertList(dto, requestMeta);

    //每个专家对供应商评分的总分，没有在数据库中保存，是通过专家汇总的明细获得的。

    dto.setZcText0(evalPack.getFormulaCode());

    //获得专家打分明细

    dto.setZcText4("N");

    expertDetailEvalResult = zcEbEvalServiceDelegate.getZcEbExpertEvalResultList(dto, requestMeta);

    for (int k = 0; k < providerList.size(); k++) {

      EvalPackProvider provider = providerList.get(k);

      //供应商所有专家评审结果

      Map<String, ZcEbExpertEvalResult> expertEvalMap = new HashMap<String, ZcEbExpertEvalResult>();

      for (int j = 0; j < evalExpertList.size(); j++) {

        EvalExpert expert = evalExpertList.get(j);

        ZcEbExpertEvalResult bill = new ZcEbExpertEvalResult();

        bill.setExpertCode(expert.getExpertCode());

        bill.setExpertName(expert.getExpertName());

        bill.setPackCode(evalPack.getPackCode());

        bill.setProjCode(evalPack.getProjCode());

        bill.setProviderName(provider.getProviderName());

        bill.setProviderCode(provider.getProviderCode());

        //专家对某一供应商评审明细

        List<ZcEbEvalItemResult> DetailList = new ArrayList<ZcEbEvalItemResult>();

        for (int i = 0; i < expertDetailEvalResult.size(); i++) {

          if (expertDetailEvalResult.get(i).getEvalExpertCode().equals(expert.getExpertCode())

          && expertDetailEvalResult.get(i).getProviderCode().equals(provider.getProviderCode())) {

            DetailList.add(expertDetailEvalResult.get(i));

          }

        }

        if (isScoreResultSum) {

          bill.setZcEbEvalScoreItemResultList(DetailList);

        } else {

          //获取汇总结果

          dto.setZcText2(expert.getExpertCode());

          dto.setZcText3(provider.getProviderCode());

          dto.setZcText4("Y");
          dto.setDattr1("CP");

          List<ZcEbEvalItemResult> compoistList = zcEbEvalServiceDelegate.getZcEbExpertEvalResultList(dto, requestMeta);

          if (compoistList != null && compoistList.size() > 0 && compoistList.get(0).getComplianceUnpassReason() != null) {

            bill.setUnPassReason(compoistList.get(0).getComplianceUnpassReason());

          }

          bill.setZcEbEvalComplItemResultList(DetailList);

        }

        expertEvalMap.put(expert.getExpertCode(), bill);

        sumTableDataList.add(bill);

      }

      provider.setExpertEvalMap(expertEvalMap);

    }

  }

  private boolean expertIsEvalLeader(ZcEbEvalPack pack) {

    if (null == pack.getEvalLeader() || "".equals(pack.getEvalLeader())) {

      return true;

    }

    if (requestMeta.getEmpName().equals(pack.getEvalLeader())) {

      return true;

    }

    return false;

  }

  public void addToolBarComponent(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(compoId);

    sumButton = new SumButton();

    if (WorkEnv.getInstance().containRole(AsOptionMeta.getOptVal("OPT_ZC_EXPERT_EVAL_ROLE"))) {

      if (expertIsEvalLeader(evalPack)) {

        toolBar.add(sumButton);

      }

    } else {

      toolBar.add(sumButton);

    }

    // 初始化按钮的action事件

    sumButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSumExpertRes();

      }

    });

    toolBar.add(initSumWayPanel());

  }

  /**

   * 

  * @Description:根据专家评审结果汇总生成最终结果。

  * @return void 返回类型

  * @since 1.0

   */

  private void doSumExpertRes() {

    if (!beforeDoSumExpertRes()) {

      return;

    }

    if (sumTableDataList.size() <= 0) {

      JOptionPane.showMessageDialog(this, "没有专家评审明细!", "提示", JOptionPane.WARNING_MESSAGE);

      return;

    }

    if (hasGen()) {

      int sel = JOptionPane.showConfirmDialog(this, "已经汇总过该标段的评审结果，是否重新汇总", "确认", JOptionPane.YES_NO_OPTION);

      if (JOptionPane.OK_OPTION == sel) {

      } else {

        return;

      }

    }

    //初始化汇总表格sumTablePanel

    Map<String, String> paraMap = new HashMap<String, String>();

    paraMap.put("SUM_WAY_ID", selectedSumWay.getValId());

    paraMap.put("SUM_WAY_NAME", selectedSumWay.getVal());

    paraMap.put("PROJ_CODE", evalPack.getProjCode());

    paraMap.put("PACK_CODE", evalPack.getPackCode());

    paraMap.put("FORML_CODE", evalPack.getFormulaCode());

    List<ZcEbPackEvalResult> list = new ArrayList<ZcEbPackEvalResult>();

    Map<String, ZcEbPackEvalResult> sumResultMap = new HashMap<String, ZcEbPackEvalResult>();

    if (isScoreResultSum) {

      //评标分数的汇总是在service层处理的

      list = zcEbEvalServiceDelegate.genPackFinalSumResult(paraMap, sumTableDataList, requestMeta);

      for (ZcEbPackEvalResult result : list) {

        result.setSumWay(selectedSumWay.getVal());

        sumResultMap.put(result.getProviderCode(), result);

      }

      Collections.sort(list, new Comparator() {

        public int compare(Object o1, Object o2) {

          ZcEbPackEvalResult result1 = (ZcEbPackEvalResult) o1;

          ZcEbPackEvalResult result2 = (ZcEbPackEvalResult) o2;

          return result2.getEvalScore().compareTo(result1.getEvalScore());

        }

      });

      int tjNum=ZcEbPackEvalResult.DEFAULT_ZHONG_BIAO_TUI_JIAN_NUM;
      String tjNumStr=AsOptionMeta.getOptVal(ZcEbPackEvalResult.OPT_ZC_ZHONG_BIAO_TUI_JIAN_NUM);
      int temp=Integer.parseInt(tjNumStr);
      if(tjNumStr!=null && temp>0){
        tjNum=temp;
      }
      
      for (int i = 0; i < list.size(); i++) {

        if (i < tjNum) {
          //推荐中标候选人
          list.get(i).setEvalResult(ZcEbPackEvalResult.EVAL_RESULT_YU_ZHONG_BIAO);

        }else {

            list.get(i).setEvalResult(ZcEbPackEvalResult.EVAL_RESULT_WEI_ZHONG_BIAO);
        }

      }

    } else {

      for (int i = 0; i < providerList.size(); i++) {

        EvalPackProvider provider = providerList.get(i);

        ZcEbPackEvalResult finaResult = new ZcEbPackEvalResult();

        finaResult.setProviderCode(provider.getProviderCode());

        finaResult.setProviderName(provider.getProviderName());

        finaResult.setProjCode(provider.getProjCode());

        finaResult.setPackCode(provider.getPackCode());

        finaResult.setExpertName(evalPack.getEvalExpert().getExpertName());

        finaResult.setIsComplianceResult("Y");

        finaResult.setSumWay(selectedSumWay.getVal());

        List ExpertSumEvalResultList = new ArrayList();

        for (int j = 0; j < sumTableDataList.size(); j++) {

          ZcEbExpertEvalResult result = sumTableDataList.get(j);

          if (provider.getProviderCode().equals(result.getProviderCode())) {

            ExpertSumEvalResultList.add(result);

          }

        }

        finaResult.setZcEbExpertEvalResultList(ExpertSumEvalResultList);

        //获得汇总结果

        genProviderPackSumRes(providerList.size(), selectedSumWay.getValId(), finaResult);

        list.add(finaResult);

        sumResultMap.put(finaResult.getProviderCode(), finaResult);

      }

    }

    //汇总结果面板加载汇总数据

    portalPanel.packResGened(list);

    portalPanel.sumResultPanelRefresh(providerList, evalExpertList, sumResultMap);    
    
    boolean saveFlag=portalPanel.saveResult();
    String message="汇总成功!";
    if(saveFlag){
      portalPanel.getMainTabbedPanel().setSelectedIndex(1);
    }else{
      message="汇总失败!";
    }

    JOptionPane.showMessageDialog(this, message, "提示", JOptionPane.INFORMATION_MESSAGE);

  }

  /**

   * 

  * @Description: TODO(判断是否已经汇总过评标结果)

  * @return boolean 返回类型

  * @since 1.0

   */

  private boolean hasGen() {

    Map<String, String> paraMap = new HashMap<String, String>();

    paraMap.put("projectCode", evalPack.getProjCode());

    paraMap.put("packCode", evalPack.getPackCode());

    if (isScoreResultSum) {

      paraMap.put("IS_COMPLIANCE_RESULT", "N");

    } else {

      paraMap.put("IS_COMPLIANCE_RESULT", "Y");

    }

    List list = zcEbEvalServiceDelegate.getExpertEvalPackResList(paraMap, requestMeta);

    if (null != list && !list.isEmpty()) {

      return true;

    }

    return false;

  }

  private void genProviderPackSumRes(int providerSize, String selectedSumWay, ZcEbPackEvalResult finaResult) {

    if (selectedSumWay.equals("MANYPASS")) {

      int passCount = 0;

      int noPassCount = 0;

      StringBuffer noPassReason = new StringBuffer();

      for (int i = 0; i < finaResult.getZcEbExpertEvalResultList().size(); i++) {

        ZcEbExpertEvalResult childItem = (ZcEbExpertEvalResult) finaResult.getZcEbExpertEvalResultList().get(i);

        if (childItem.getComplianceValue().equals(ZcSettingConstants.COMPLIANCE_EVAL_NOPASS)) {

          noPassCount++;
          if (childItem.getUnPassReason() != null) {
            noPassReason.append(childItem.getExpertName() + ":" + childItem.getUnPassReason() + ";");

          }

        } else {

          passCount++;

        }

      }

      if (passCount >= noPassCount) {

        finaResult.setComplianceEvalValue(ZcSettingConstants.COMPLIANCE_EVAL_PASS);

        finaResult.setComplianceEvalResult(Constants.COMPLIANCE_PASS);

      } else {

        finaResult.setComplianceEvalValue(ZcSettingConstants.COMPLIANCE_EVAL_NOPASS);

        finaResult.setComplianceEvalResult(Constants.COMPLIANCE_NO_PASS);

        finaResult.setComplianceUnpassReason(noPassReason.toString());

      }

    }

    if (selectedSumWay.equals("ALLPASS")) {

      int noPassCount = 0;

      StringBuffer noPassReason = new StringBuffer();

      for (int i = 0; i < finaResult.getZcEbExpertEvalResultList().size(); i++) {

        ZcEbExpertEvalResult childItem = (ZcEbExpertEvalResult) finaResult.getZcEbExpertEvalResultList().get(i);

        if (childItem.getComplianceValue().equals(ZcSettingConstants.COMPLIANCE_EVAL_NOPASS)) {

          noPassCount++;
          if (childItem.getUnPassReason() != null) {
            noPassReason.append(childItem.getExpertName() + ":" + childItem.getUnPassReason() + ";");

          }

        }

      }

      //"2"：表示不通过

      if (noPassCount > 0) {

        finaResult.setComplianceEvalValue(ZcSettingConstants.COMPLIANCE_EVAL_NOPASS);

        finaResult.setComplianceEvalResult(Constants.COMPLIANCE_NO_PASS);

        finaResult.setComplianceUnpassReason(noPassReason.toString());

      } else {

        finaResult.setComplianceEvalValue(ZcSettingConstants.COMPLIANCE_EVAL_PASS);

        finaResult.setComplianceEvalResult(Constants.COMPLIANCE_PASS);

      }

    }

  }

  private void setComplTabledetailEditor(final JPageableFixedTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());

    final AsValComboBoxCellEditor asValComboBoxCellEditor = new AsValComboBoxCellEditor("VS_ZC_EB_COMPLIANCE_VALUE");

    SwingUtil.setTableCellEditor(table, "COMPLIANCE_EVAL_VALUE", asValComboBoxCellEditor);

    SwingUtil.setTableCellRenderer(table, "COMPLIANCE_EVAL_VALUE", new AsValCellRenderer("VS_ZC_EB_COMPLIANCE_VALUE"));

  }

  private boolean beforeDoSumExpertRes() {

    selectedSumWay = (AsVal) sumWayComboBox.getSelectedItem();

    if (null == selectedSumWay) {

      JOptionPane.showMessageDialog(this, "请选择一种汇总计算方式", "提示", JOptionPane.WARNING_MESSAGE);

      return false;

    }

    String msg = "确定采用 " + selectedSumWay.getVal() + " 汇总计算方式吗?";

    int sel = JOptionPane.showConfirmDialog(this, msg, "确认", JOptionPane.YES_NO_OPTION);

    if (JOptionPane.NO_OPTION == sel) {

      return false;

    }

    if (JOptionPane.YES_OPTION == sel) {

      return true;

    }

    return false;

  }

}
