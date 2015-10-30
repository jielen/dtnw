package com.ufgov.zc.client.zc.expert;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

import com.ufgov.smartclient.component.table.JGroupableTableHeader;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEmExpertAbilityToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CheckBoxFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.EmExpertAbility;
import com.ufgov.zc.common.zc.model.EmExpertAbilityHistory;
import com.ufgov.zc.common.zc.publish.IZcEmExpertAbilityServiceDelegate;



@SuppressWarnings("unchecked")
public class ZcExpertAbilityInfoEditPanel extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZcExpertAbilityInfoEditPanel.class);

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_EM_B_EXPERT_ABILITY";

  private FuncButton editButton = new EditButton();

  private FuncButton saveButton = new SaveButton();

  private FuncButton exitButton = new ExitButton();

  //子表添加按钮
  private FuncButton addBtn1 = new SubaddButton(false);
  
  //子表插入按钮
  private JButton insertBtn1 = new SubinsertButton(false);
  
  //子表删除按钮
  private JButton delBtn1 = new SubdelButton(false);
  
  protected ListCursor listCursor;

  protected EmExpertAbility oldBean;
  
  private JTabbedPane abilityTabPane = new JTabbedPane();
  
  private JTablePanel abilityTablePanel = new JTablePanel();
  
  private JTabbedPane evalTabPane = new JTabbedPane();
  
  private JTablePanel evalTablePanel = new JTablePanel();

  private JFuncToolBar bottomToolBar1 = null;

  private ZcExpertAbilityInfoEditPanel self = this;

  ElementConditionDto elementConditionDto = new ElementConditionDto();

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();
  
  public CompanyFieldEditor coCode = null;

  public CheckBoxFieldEditor flagLocal = null;

  private GkBaseDialog parent;

  ElementConditionDto elementDto = new ElementConditionDto();

  public IZcEmExpertAbilityServiceDelegate zcEmExpertAbilityServiceDelegate = (IZcEmExpertAbilityServiceDelegate) ServiceFactory.create(IZcEmExpertAbilityServiceDelegate.class,
    "zcEmExpertAbilityServiceDelegate");

  public IZcEmExpertAbilityServiceDelegate getZcEmExpertAbilityServiceDelegate() {
    return zcEmExpertAbilityServiceDelegate;
  }

  public void setZcEmExpertServiceDelegate(IZcEmExpertAbilityServiceDelegate zcEmExpertAbilityServiceDelegate) {
    this.zcEmExpertAbilityServiceDelegate = zcEmExpertAbilityServiceDelegate;
  }
  
  public ZcExpertAbilityInfoEditPanel() {

  }

  public ZcExpertAbilityInfoEditPanel(ListCursor listCursor, GkBaseDialog dialog) {
	this.parent = dialog;
    initExpertEditPanel(listCursor);
  }

  protected void initExpertEditPanel( ListCursor listCursor) {

    String title = LangTransMeta.translate("ZC_B_EXPERT_ABILITY_TITLE");

    this.listCursor = listCursor;

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title,

    TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.colCount = 3;

    init();

    requestMeta.setCompoId(compoId);

    elementConditionDto.setCoCode(WorkEnv.getInstance().getCurrCoCode());

    refreshData();
    
    setButtonStatus(false);

  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    return createMainFieldEditors(editorList);

  }

  /**
   * 创建主表字段
   * @param editorList
   * @return
   */
  protected List<AbstractFieldEditor> createMainFieldEditors(List<AbstractFieldEditor> editorList) {

    //专家代码、专家名称,性别

      TextFieldEditor emExpertCode = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_EXPERT_CODE), "emExpertCode");

      editorList.add(emExpertCode);
      
      TextFieldEditor emExpertName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_EXPERT_NAME), "emExpertName");

      editorList.add(emExpertName);

      AsValFieldEditor emExpertSex = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_EXPERT_SEX), "emExpertSex", "VS_SEX");

      editorList.add(emExpertSex);
      
      
      //出生日期 ，手机号码,单位名称     
      DateFieldEditor emBirthday = new DateFieldEditor(LangTransMeta

        .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_BIRTHDAY), "emBirthday");

      editorList.add(emBirthday);
      
      TextFieldEditor emMobile = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_MOBILE), "emMobile");

      editorList.add(emMobile);
      
      TextFieldEditor emUnitName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_UNIT_NAME), "emUnitName");

      editorList.add(emUnitName);
        
      return editorList;
  }

  @Override
  public JComponent createSubBillPanel() {

    JPanel panel = new JPanel(new GridLayout(2, 1));

    panel.add(createAbilityPanel());

    panel.add(createEvalPanel());

    return panel;

  }

  /**
   * 创建综合能力评审从表信息
   * @return
   */
  private JTabbedPane createAbilityPanel() {
    
    abilityTablePanel.init();

    abilityTablePanel.getSearchBar().setVisible(false);

    abilityTablePanel.setTablePreferencesKey(this.getClass().getName() + "_abilityTable");

    abilityTablePanel.getTable().setShowCheckedColumn(true);

    abilityTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    bottomToolBar1 = new JFuncToolBar();

    bottomToolBar1.add(addBtn1);

    bottomToolBar1.add(insertBtn1);

    bottomToolBar1.add(delBtn1);

    abilityTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);

    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        
        EmExpertAbilityHistory detail = new EmExpertAbilityHistory();

        detail.setTempId(Guid.genID());
        
        setdetailDefaultValue(detail);
        
        int rowNum = addSub(abilityTablePanel, detail);

        abilityTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        EmExpertAbilityHistory detail = new EmExpertAbilityHistory();

        detail.setTempId(Guid.genID());
        
        setdetailDefaultValue(detail);

        int rowNum = insertSub(abilityTablePanel, detail);

        abilityTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteSub(abilityTablePanel);

      }

    });
    
    abilityTabPane.add("综合评定历史", abilityTablePanel);

    return abilityTabPane;

  }

  /**
   * 创建专家评价信息，此信息来至于评标小组管理
   * @return
   */
private JTabbedPane createEvalPanel() {
    
    evalTablePanel.init();

    JGroupableTableHeader header = evalTablePanel.getTable().getTableHeader();

    header.addColumnGroup("评审的公平性、公正性和准确性", new String[] { "EM_EXPERT_INDEX4", "EM_EXPERT_INDEX5", "EM_EXPERT_INDEX6", "EM_EXPERT_INDEX7" });

    header.addColumnGroup("工作纪律和工作态度", new String[] { "EM_EXPERT_INDEX8", "EM_EXPERT_INDEX9", "EM_EXPERT_INDEX10", "EM_EXPERT_INDEX11",

    "EM_EXPERT_INDEX12", "EM_EXPERT_INDEX13" });

    evalTablePanel.getSearchBar().setVisible(false);

    evalTablePanel.setTablePreferencesKey(this.getClass().getName() + "Evaluate_tabel");

    evalTablePanel.getTable().setShowCheckedColumn(false);

    evalTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(50, 0));
    
    evalTabPane.add("专家评价信息", evalTablePanel);
 
    return evalTabPane;

  }

  
  @Override
  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(compoId);

    toolBar.add(editButton);

    toolBar.add(saveButton);

    toolBar.add(exitButton);

    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doEdit();
      }
    });

    saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 保存
        doSave();
      }
    });

    exitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 退出
        doExit();
      }
    });
  }

  /**********************************************************************************
   * 以下处理的是按钮调用的方法
   **********************************************************************************/
  /**
   * 刷新list页面数据
   * @param afterSaveBill
   * @param isRefreshButton
   */
  private void refreshAll(EmExpertAbility afterSaveBill, boolean isRefreshButton) {

    pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

    this.listCursor.setCurrentObject(afterSaveBill);

    refreshData();

  }

  /**

   * 保存前校验

   * @param 

   * @return

   */

  private boolean checkBeforeSave() {
       
    EmExpertAbility bill = (EmExpertAbility) listCursor.getCurrentObject();

    StringBuffer errStr = new StringBuffer();
    
    StringBuffer subErrStr = new StringBuffer();
    
    if (bill.getAbilityList()!=null ||bill.getAbilityList().size()>0) {

      int i =1;
      for (Object o : bill.getAbilityList()) {

        EmExpertAbilityHistory item = (EmExpertAbilityHistory) o;    
        
        String desc = (String) ObjectUtils.defaultIfNull(item.getEmExpertAbilityDesc(), "");

        Date startDate = item.getEmExpertRatingStartDate();
        
        Date endDate = item.getEmExpertRatingEndDate();
        
        Date nowDate = WorkEnv.getInstance().getSysDate();
        
     
        if ("".equals(desc)) {

          subErrStr.append("第" + i + "行  综合评审结果信息不能为空！\n");

        }

        if (startDate==null || "".equals(startDate)) {

          subErrStr.append("第" + i + "行 评审起始日期不能为空！\n");

        }

        if (endDate==null || "".equals(endDate)) {

          subErrStr.append("第" + i + "行 评审终止日期不能为空！\n");

        }
 
        if((endDate !=null && startDate!=null)&&(endDate.compareTo(startDate)==0)){
          subErrStr.append("第" + i + "行 评审终止日期不能等于起始日期！\n");
        }
        
        if((endDate !=null && startDate!=null)&&(endDate.compareTo(startDate)<0)){
          subErrStr.append("第" + i + "行 设置的评审终止日期不能在起始日期之前！\n");
        }
        
        if((endDate !=null && nowDate!=null)&&(endDate.compareTo(nowDate)>0)){
          subErrStr.append("第" + i + "行 评审终止日期不能在当前日期之后！\n");
        }
        
        if((startDate !=null && nowDate!=null)&&(startDate.compareTo(nowDate)>=0)){
          subErrStr.append("第" + i + "行 评审起始日期需在当前日期之前！\n");
        }
        
        i++;

      }
    }
  
  if (subErrStr.toString().length()>0){
    
    errStr.append("专家评标类别信息：\n");
    errStr.append(subErrStr.toString());
    
  }
    
    
  if (errStr.toString().length() > 0) {

      errStr.append("");

      JOptionPane.showMessageDialog(this, errStr.toString(), "提示", JOptionPane.ERROR_MESSAGE);

      return false;

   }

   return true;

  }

  /**
   * 
   */
  private void stopTableEditing() {
    JPageableFixedTable abilityTable = this.abilityTablePanel.getTable();
    if (abilityTable.isEditing()) {
      abilityTable.getCellEditor().stopCellEditing();
    }
    
    JPageableFixedTable evalTable = this.evalTablePanel.getTable();
    if (evalTable.isEditing()) {
      evalTable.getCellEditor().stopCellEditing();
    }
  }

  /**
   * 判断数据是否已经被别人改变
   * @return
   */
  public boolean isDataChanged() {
    stopTableEditing();
    return !DigestUtil.digest(oldBean).equals(DigestUtil.digest(listCursor.getCurrentObject()));

  }

  /**
   * 刷新数据
   */
  public void refreshData() {
    EmExpertAbility afterBill = (EmExpertAbility) listCursor.getCurrentObject();

    this.setEditingObject(afterBill);

    abilityTablePanel.setTableModel(ZcEmExpertAbilityToTableModelConverter.convertSubTableData(afterBill.getAbilityList()));

    // 翻译从表表头列
    ZcUtil.translateColName(abilityTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.getBillDetailInfo());

    // 设置从表列类型
    setTableDetailEditor(abilityTablePanel.getTable());
    
        
    evalTablePanel.setTableModel(ZcEmExpertAbilityToTableModelConverter.convertEmExpertEvaluationToTableMode(afterBill.getItemList()));
     
    expertEvaluateTabelProperty(evalTablePanel.getTable());
    
    
    setOldObject();

    updateFieldEditorsEditable();
    
    this.fitTable();
  }

  protected void expertEvaluateTabelProperty(JTable table) {
    translateSubTableColumn();
  
    setExpertEvaluateTabelCellEditor();
  
  }
  
  private void translateSubTableColumn() {

    ZcUtil.translateColName(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.expertEvalColumns);

  }

  private void setExpertEvaluateTabelCellEditor() {

    SwingUtil.setTableCellRenderer(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX4,

    new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellRenderer(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX5,

    new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellRenderer(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX6,

    new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellRenderer(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX7,

    new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellRenderer(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX8,

    new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellRenderer(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX9,

    new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellRenderer(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX10,

    new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellRenderer(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX11,

    new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellRenderer(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX12,

    new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellRenderer(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX13,

    new AsValCellRenderer("VS_Y/N"));


    SwingUtil.setTableCellEditor(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX4,

    new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellEditor(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX5,

    new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellEditor(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX6,

    new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellEditor(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX7,

    new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellEditor(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX8,

    new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellEditor(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX9,

    new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellEditor(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX10,

    new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellEditor(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX11,

    new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellEditor(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX12,

    new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellEditor(evalTablePanel.getTable(), ZcEmExpertAbilityToTableModelConverter.EM_EXPERT_INDEX13,

    new AsValComboBoxCellEditor("VS_Y/N"));

  }

   /**
    * 设置明细
    * @param table
    */
  private void setTableDetailEditor(JPageableFixedTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());
    
    SwingUtil.setTableCellEditor(table, "emExpertRatingStartDate", new DateCellEditor());

    SwingUtil.setTableCellRenderer(table, "emExpertRatingStartDate", new DateCellRenderer());
    
    SwingUtil.setTableCellEditor(table, "emExpertRatingStartDate", new DateCellEditor());

    SwingUtil.setTableCellRenderer(table, "emExpertRatingStartDate", new DateCellRenderer());
    
    SwingUtil.setTableCellEditor(table, "emExpertRatingEndDate", new DateCellEditor());

    SwingUtil.setTableCellRenderer(table, "emExpertRatingEndDate", new DateCellRenderer());
    
    SwingUtil.setTableCellEditor(table, "zcInputDate", new DateCellEditor());

    SwingUtil.setTableCellRenderer(table, "zcInputDate", new DateCellRenderer());
    
    SwingUtil.setTableCellRenderer(table, "emExpertAbilityGrade",new AsValCellRenderer("ZC_VS_ABILITY_GRADE"));
      
    SwingUtil.setTableCellEditor(table, "emExpertAbilityGrade", new AsValComboBoxCellEditor("ZC_VS_ABILITY_GRADE"));
  }

  /**
   * 存储老的bean数据
   */
  protected void setOldObject() {
    oldBean = (EmExpertAbility) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  /**
   * 设置主表的字段和专家评价永远不可修改，从表“专家综合评审”在编辑状态下可以修改
   */
  @Override
  protected void updateFieldEditorsEditable() {

      super.updateFieldEditors();

      for (AbstractFieldEditor fd : this.fieldEditors) {
          fd.setEnabled(false);
      }
      
      this.evalTablePanel.getTable().setEnabled(false);
      
      if (ZcSettingConstants.PAGE_STATUS_EDIT.equals(this.pageStatus)){
         setSubButtonFlag(true);
         this.abilityTablePanel.getTable().setEnabled(true);
      }else{
         setSubButtonFlag(false);
         this.abilityTablePanel.getTable().setEnabled(false);
      }
  }
  

  private void setSubButtonFlag(boolean flag){

    addBtn1.setEnabled(flag);

    insertBtn1.setEnabled(flag);

    delBtn1.setEnabled(flag);     

  }
 
  /**
   * 设置字表的默认值
   * 
   * @param detail：
   */
  private void setdetailDefaultValue(EmExpertAbilityHistory detail) {

    EmExpertAbility bill = (EmExpertAbility) listCursor.getCurrentObject();
    
    detail.setEmExpertCode(bill.getEmExpertCode());
    
    detail.setEmExpertAbilityGrade("3");
    
    detail.setZcInputCode(WorkEnv.getInstance().getCurrUserId());
    
    detail.setZcInputName(WorkEnv.getInstance().getCurrUserName());
    
    detail.setZcInputDate(WorkEnv.getInstance().getSysDate());
    
    detail.setEmExpertRatingEndDate(WorkEnv.getInstance().getSysDate());
    
    detail.setEmExpertRatingStartDate(WorkEnv.getInstance().getSysDate());
  }

  private void setButtonStatus(boolean flag){
    
    this.saveButton.setEnabled(flag);
    
    this.editButton.setEnabled(!flag);
  }
  /****************************************************************************************************
   * 以下用来实现编辑和保存等主表的按钮功能
   ***************************************************************************************************/
  
  public void doEdit() {

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

    updateFieldEditorsEditable();
   
    setButtonStatus(true);
  }


  public boolean doSave() {

    if (!isDataChanged()) {

      if (!checkBeforeSave()) {

        return false;

      }

      pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      return true;

    }

    if (!checkBeforeSave()) {

      return false;

    }

    boolean success = true;

    EmExpertAbility updBean = new EmExpertAbility();

    String errorInfo = "";

    try {

      requestMeta.setFuncId(saveButton.getFuncId());

      EmExpertAbility inData = (EmExpertAbility) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      updBean = this.getZcEmExpertAbilityServiceDelegate().updateByPrimaryKey(inData, requestMeta);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      setButtonStatus(false);
      
      refreshAll(updBean, true);
    } else {

      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

    return true;

  }

  public boolean doExit() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return false;

        }

      }

    }
    this.parent.dispose();
    
    return true;

  }
}