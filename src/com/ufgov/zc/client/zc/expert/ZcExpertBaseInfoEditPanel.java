package com.ufgov.zc.client.zc.expert;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEmExpertToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.EnableButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.InvalidButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PauseEvalExpertButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.button.zc.CommonButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CheckBoxFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextAreaFieldEditor;
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
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.EmExpert;
import com.ufgov.zc.common.zc.model.EmExpertAbility;
import com.ufgov.zc.common.zc.model.EmExpertType;
import com.ufgov.zc.common.zc.model.EmExpertTypeJoin;
import com.ufgov.zc.common.zc.publish.IZcEmExpertAbilityServiceDelegate;

@SuppressWarnings("unchecked")
public class ZcExpertBaseInfoEditPanel extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZcExpertBaseInfoEditPanel.class);

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_EM_B_EXPERT";

  private FuncButton addButton = new AddButton();

  private FuncButton editButton = new EditButton();

  private FuncButton previousButton = new PreviousButton();

  private FuncButton saveButton = new SaveButton();

  private FuncButton deleteButton = new DeleteButton();

  private FuncButton nextButton = new NextButton();

  private FuncButton exitButton = new ExitButton();

  // 送审
  private FuncButton auditButton = new EnableButton();

  // 销审
  private FuncButton unAuditButton = new PauseEvalExpertButton();

  // 作废
  private FuncButton cancelButton = new InvalidButton();

  // 专家评价
  private CommonButton expertAbilityButton = new CommonButton("fexpertAbility", "专家评价", "default.gif", true);

  //子表添加按钮
  private FuncButton addBtn1 = new SubaddButton(false);

  //子表插入按钮
  private JButton insertBtn1 = new SubinsertButton(false);

  //子表删除按钮
  private JButton delBtn1 = new SubdelButton(false);

  protected ListCursor listCursor;

  protected EmExpert oldBean;

  protected ZcExpertBaseInfoListPanel listPanel;

  private JTabbedPane jTabbedPane = new JTabbedPane();

  private JTablePanel itemTablePanel = new JTablePanel();

  private JFuncToolBar bottomToolBar1 = null;

  private ZcExpertBaseInfoEditPanel self = this;

  private GkBaseDialog parent;

  ElementConditionDto elementConditionDto = new ElementConditionDto();

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  public CompanyFieldEditor coCode = null;

  public CheckBoxFieldEditor flagLocal = null;

  ElementConditionDto elementDto = new ElementConditionDto();

  public IZcEmExpertAbilityServiceDelegate zcEmExpertAbilityServiceDelegate = (IZcEmExpertAbilityServiceDelegate) ServiceFactory.create(
    IZcEmExpertAbilityServiceDelegate.class, "zcEmExpertAbilityServiceDelegate");

  public IZcEmExpertAbilityServiceDelegate getZcEmExpertAbilityServiceDelegate() {
    return zcEmExpertAbilityServiceDelegate;
  }

  public void setZcEmExpertServiceDelegate(IZcEmExpertAbilityServiceDelegate zcEmExpertAbilityServiceDelegate) {
    this.zcEmExpertAbilityServiceDelegate = zcEmExpertAbilityServiceDelegate;
  }

  public ZcExpertBaseInfoEditPanel() {

  }

  public ZcExpertBaseInfoEditPanel(Class billClass, BillElementMeta eleMeta) {
    super(billClass, eleMeta);
  }

  public ZcExpertBaseInfoEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcExpertBaseInfoListPanel listPanel) {
    super(EmExpert.class, listPanel.getBillElementMeta());
    initExpertEditPanel(parent, listCursor, listPanel);
  }

  protected void initExpertEditPanel(GkBaseDialog parent, ListCursor listCursor,

  ZcExpertBaseInfoListPanel listPanel) {

    String title = LangTransMeta.translate("ZC_EM_B_EXPERT_TITLE");

    this.listCursor = listCursor;

    this.listPanel = listPanel;

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title,

    TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.parent = parent;

    this.colCount = 3;

    init();

    requestMeta.setCompoId(compoId);

    elementConditionDto.setCoCode(WorkEnv.getInstance().getCurrCoCode());

    refreshData();

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

    //专家代码、专家名称、状态
    AutoNumFieldEditor emExpertCode = new AutoNumFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_EXPERT_CODE), "emExpertCode", false);

    editorList.add(emExpertCode);

    TextFieldEditor emExpertName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_EXPERT_NAME),
      "emExpertName");

    editorList.add(emExpertName);

    AsValFieldEditor emExpStatus = new AsValFieldEditor("状态", "emExpStatus", "EM_VS_EXP_STATUS");

    editorList.add(emExpStatus);

    //性别，出生日期 ，手机号码
    AsValFieldEditor emExpertSex = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_EXPERT_SEX),
      "emExpertSex", "VS_SEX");

    editorList.add(emExpertSex);

    DateFieldEditor emBirthday = new DateFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_BIRTHDAY), "emBirthday");

    editorList.add(emBirthday);

    TextFieldEditor emMobile = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_MOBILE), "emMobile");

    editorList.add(emMobile);

    //职称，获取职称时间，政治面貌,
    AsValFieldEditor emExpertProtitle = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_EXPERT_PROTITLE),
      "emExpertProtitle", "VS_EMP_TECH");

    editorList.add(emExpertProtitle);

    DateFieldEditor emProtitleTime = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_PROTITLE_TIME),
      "emProtitleTime");

    editorList.add(emProtitleTime);

    AsValFieldEditor emPoType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_PO_TYPE), "emPoType",
      "EM_VS_POLITICAL");

    editorList.add(emPoType);

    //执业资格证书名称 , 执业资格证书号码,照片
    TextFieldEditor emZhiyZgmc = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_ZHIY_ZGMC), "emZhiyZgmc");

    editorList.add(emZhiyZgmc);

    TextFieldEditor emZhiyZgzsh = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_ZHIY_ZGZSH), "emZhiyZgzsh",
      5, true);

    editorList.add(emZhiyZgzsh);

    FileFieldEditor emExpertZp = new FileFieldEditor("照片", "emExpertZp", "emExpertZpBlobID");

    editorList.add(emExpertZp);

    //证件类型,证件号码，专家评标类别
    AsValFieldEditor emIdType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_ID_TYPE), "emIdType",
      "EM_VS_ID_TYPE");

    editorList.add(emIdType);

    TextFieldEditor emIdNo = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_ID_NO), "emIdNo");

    editorList.add(emIdNo);

    AsValFieldEditor emMajorLb = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_MAJOR_LB), "emMajorLb",
      "EM_VS_MAJOR_LB");

    editorList.add(emMajorLb);

    //毕业院校, 最高学历 ,  所学专业 
    TextFieldEditor emUniversity = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_UNIVERSITY), "emUniversity");

    editorList.add(emUniversity);

    AsValFieldEditor emDiploma = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_DIPLOMA), "emDiploma",
      "VS_STUDY_FIL");

    editorList.add(emDiploma);

    AsValFieldEditor emMajor = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_MAJOR), "emMajor",
      "EM_VS_MAJOR");

    editorList.add(emMajor);

    //毕业时间,参加工作时间,开始专业时间 
    DateFieldEditor emGraduationTime = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_GRADUATION_TIME),
      "emGraduationTime");

    editorList.add(emGraduationTime);

    DateFieldEditor emWorkStartTime = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_WORK_STARTTIME),
      "emWorkStartTime");

    editorList.add(emWorkStartTime);

    DateFieldEditor emMajorStartTime = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_MAJOR_STARTIME),
      "emMajorStartTime");

    editorList.add(emMajorStartTime);

    //单位类型，所在单位,职位 
    TextFieldEditor emUnitName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_UNIT_NAME), "emUnitName");

    editorList.add(emUnitName);

    AsValFieldEditor emUnitType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_UNIT_TYPE), "emUnitType",
      "VS_CO_TYPE");

    editorList.add(emUnitType);

    AsValFieldEditor emPrincipalship = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_PRINCIPALSHIP),
      "emPrincipalship", "VS_POSITION");

    editorList.add(emPrincipalship);

    //单位电话,单位地址,单位邮编     
    TextFieldEditor emUnitPhone = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_UNIT_PHONE), "emUnitPhone");

    editorList.add(emUnitPhone);

    TextFieldEditor emUnitAddr = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_UNIT_ADDR), "emUnitAddr");

    editorList.add(emUnitAddr);

    TextFieldEditor emUnitPostcode = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_UNIT_POSTCODE),
      "emUnitPostcode");

    editorList.add(emUnitPostcode);

    //优先通知电话,家庭邮编
    TextFieldEditor emHomePhone = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_HOME_PHONE), "emHomePhone");

    editorList.add(emHomePhone);

    TextFieldEditor emHomePostcode = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_HOME_POSTCODE),
      "emHomePostcode");

    editorList.add(emHomePostcode);

    //主要工作经历
    TextAreaFieldEditor emGongzJingl = new TextAreaFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_EM_GONGZ_JINGL),
      "emGongzJingl", 999, 2, 5);

    editorList.add(emGongzJingl);

    //附件
    FileFieldEditor emFuJian1 = new FileFieldEditor("附件1", "emFuJian1", "emFuJian1BlobID");

    editorList.add(emFuJian1);

    FileFieldEditor emFuJian2 = new FileFieldEditor("附件2", "emFuJian2", "emFuJian2BlobID");

    editorList.add(emFuJian2);

    return editorList;
  }

  @Override
  public JComponent createSubBillPanel() {
    return createItemPanel();

  }

  private JTabbedPane createItemPanel() {

    itemTablePanel.init();

    itemTablePanel.getSearchBar().setVisible(false);

    itemTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");

    itemTablePanel.getTable().setShowCheckedColumn(true);

    itemTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    bottomToolBar1 = new JFuncToolBar();

    bottomToolBar1.add(addBtn1);

    bottomToolBar1.add(insertBtn1);

    bottomToolBar1.add(delBtn1);

    itemTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);

    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        EmExpertTypeJoin detail = new EmExpertTypeJoin();

        detail.setTempId(Guid.genID());

        setdetailMerDefaultValue(detail);

        int rowNum = addSub(itemTablePanel, detail);

        itemTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        EmExpertTypeJoin detail = new EmExpertTypeJoin();

        detail.setTempId(Guid.genID());

        setdetailMerDefaultValue(detail);

        int rowNum = insertSub(itemTablePanel, detail);

        itemTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteSub(itemTablePanel);

      }

    });

    jTabbedPane.add("专家评标类别", itemTablePanel);

    return jTabbedPane;

  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(compoId);

    toolBar.add(addButton);

    toolBar.add(editButton);

    toolBar.add(saveButton);

    toolBar.add(auditButton);

    toolBar.add(unAuditButton);

    toolBar.add(cancelButton);

    toolBar.add(deleteButton);

    toolBar.add(expertAbilityButton);

    toolBar.add(previousButton);

    toolBar.add(nextButton);

    toolBar.add(exitButton);

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 新增
        doAdd();
      }
    });

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

    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 删除
        doDelete();
      }
    });

    auditButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 送审
        doAudit();
      }
    });

    unAuditButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        doUnAudit();
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        doCancel();
      }
    });

    expertAbilityButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 打开专家
        doExpertAbility();
      }
    });

    previousButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 上一页
        doPrevious();
      }
    });

    nextButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 下一页
        doNext();
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
  private void refreshAll(EmExpert afterSaveBill, boolean isRefreshButton) {

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

    List mainNotNullList = ((BillElementMeta) this.listPanel.getBillElementMeta()).getNotNullBillElement();

    EmExpert bill = (EmExpert) listCursor.getCurrentObject();

    StringBuffer errStr = new StringBuffer();

    StringBuffer subErrStr = new StringBuffer();

    String mainValidateInfo = ZcUtil.validateBillElementNull(bill, mainNotNullList);
    

    if (mainValidateInfo.length() != 0) {

      errStr.append(LangTransMeta.translate("ZC_EM_B_EXPERT_TITLE")).append("：\n").append(mainValidateInfo.toString()).append("\n");

    }
    
    if (bill.getEmExpertName() == null || bill.getEmExpertName().length() ==0) {
      errStr.append("专家名称：\n不允许为空!\n");
    }

    //时间校验
    Date d = new Date();
    if(bill.getEmBirthday() != null && bill.getEmBirthday().after(d)){

        errStr.append("出生日期：不允许晚于当前日期!\n");
    	
    }

    if(bill.getEmProtitleTime() != null && bill.getEmProtitleTime().after(d)){

        errStr.append("获取职称时间：不允许晚于当前日期!\n");
    	
    }

    if(bill.getEmGraduationTime() != null && bill.getEmGraduationTime().after(d)){

        errStr.append("毕业时间：不允许晚于当前日期!\n");
    	
    }

    if(bill.getEmWorkStartTime() != null && bill.getEmWorkStartTime().after(d)){

        errStr.append("参加工作时间：不允许晚于当前日期!\n");
    	
    }

    if(bill.getEmMajorStartTime() != null && bill.getEmMajorStartTime().after(d)){

        errStr.append("开始专业时间：不允许晚于当前日期!\n");
    	
    }

    if (bill.getItemList() == null || bill.getItemList().size() == 0) {

      errStr.append("专家评标类别信息：\n不允许为空!\n");

    }

    int i = 1;
    for (Object o : bill.getItemList()) {

      EmExpertTypeJoin item = (EmExpertTypeJoin) o;

      String code = (String) ObjectUtils.defaultIfNull(item.getEmTypeCode(), "");

      String name = (String) ObjectUtils.defaultIfNull(item.getEmTypeName(), "");

      if ("".equals(code) || "".equals(name)) {

        subErrStr.append("第" + i + "行    评标类别信息不能为空！\n");

      }

      i++;

    }

    if (subErrStr.toString().length() > 0) {

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
    JPageableFixedTable itemTable = this.itemTablePanel.getTable();
    if (itemTable.isEditing()) {
      itemTable.getCellEditor().stopCellEditing();
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
    EmExpert afterBill = (EmExpert) listCursor.getCurrentObject();

    if (afterBill == null) {

      afterBill = new EmExpert();

      afterBill.setEmExpStatus("0");

      pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

      listCursor.setCurrentObject(afterBill);

      listCursor.getDataList().add(afterBill);

    }

    this.setEditingObject(afterBill);

    itemTablePanel.setTableModel(ZcEmExpertToTableModelConverter.convertSubBiTableData(afterBill.getItemList()));

    // 翻译从表表头列

    ZcUtil.translateColName(itemTablePanel.getTable(), ZcEmExpertToTableModelConverter.getBillDetailInfo());

    // 设置从表列类型

    setTabledetailEditor(itemTablePanel.getTable());

    setOldObject();

    setButtonStatus();

    updateFieldEditorsEditable();

    this.fitTable();
  }

  /**
   * 设置明细
   * @param table
   */
  private void setTabledetailEditor(JPageableFixedTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());

    String columNames[] = { "评标类别代码", "评标类别名称" };

    EmExpertTypeHandler handler = new EmExpertTypeHandler(columNames);

    ForeignEntityFieldCellEditor emExpertType = new ForeignEntityFieldCellEditor(

    "EmExpertType.list", elementConditionDto, 20, handler, columNames, "选择评标类别", "emTypeCode");

    SwingUtil.setTableCellEditor(table, "emTypeCode", emExpertType);

  }

  /**
   * 存储老的bean数据
   */
  protected void setOldObject() {
    oldBean = (EmExpert) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  /**
   * 设置工具条上按钮的可用性
   */
  protected void setButtonStatus() {
    if (this.btnStatusList.size() == 0) {

      ButtonStatus bs = new ButtonStatus();

      bs = new ButtonStatus();

      bs.setButton(this.editButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.saveButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.deleteButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.auditButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");

      bs.addBillStatus("suspended");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.unAuditButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("exec");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.cancelButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("suspended");

      bs.addBillStatus("exec");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.previousButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.nextButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

    }

    EmExpert obj = (EmExpert) this.listCursor.getCurrentObject();

    String billStatus = obj.getEmExpStatus();

    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, this.compoId, obj.getProcessInstId());
  }

  /**
   * 设置主表的字段是否可以编辑
   */
  @Override
  protected void updateFieldEditorsEditable() {

    super.updateFieldEditors();

    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW) || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        if ("emExpStatus".equals(fd.getFieldName()) || "emExpertCode".equals(fd.getFieldName())) {

          fd.setEnabled(false);

        } else {

          fd.setEnabled(true);

        }

      }
      this.itemTablePanel.getTable().setEnabled(true);
      setSubButtonFlag(true);
    } else {
      for (AbstractFieldEditor fd : this.fieldEditors) {
        fd.setEnabled(false);
      }
      this.itemTablePanel.getTable().setEnabled(false);
      setSubButtonFlag(false);
    }
  }

  private void setSubButtonFlag(boolean flag) {

    addBtn1.setEnabled(flag);

    insertBtn1.setEnabled(flag);

    delBtn1.setEnabled(flag);

  }

  /**
   * 设置字表的默认值
   * 
   * @param detail：
   */
  private void setdetailMerDefaultValue(EmExpertTypeJoin detail) {

    EmExpert bill = (EmExpert) listCursor.getCurrentObject();

    detail.setEmExpertCode(bill.getEmExpertCode());
  }

  /****************************************************************************************************
   * 以下用来实现增、删、改、启用等主表的按钮功能
   ***************************************************************************************************/

  public void doEdit() {

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

    updateFieldEditorsEditable();

    setButtonStatus();

  }

  /**
   * 新增
   */
  private void doAdd() {
    if (this.doExit()) {

      this.listPanel.doAdd();

    }
  }

  public boolean doSave() {

    if (!isDataChanged()) {

      if (!checkBeforeSave()) {

        return false;

      }

      pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      setButtonStatus();

      return true;

    }

    if (!checkBeforeSave()) {

      return false;

    }

    boolean success = true;

    EmExpert updBean = new EmExpert();

    String errorInfo = "";

    try {

      requestMeta.setFuncId(saveButton.getFuncId());

      EmExpert inData = (EmExpert) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      updBean = this.listPanel.getZcEmExpertServiceDelegate().updateByPrimaryKey(inData, requestMeta);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshAll(updBean, true);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

    return true;

  }

  protected void doDelete() {

    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      boolean success = true;

      EmExpert emExpert = null;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(deleteButton.getFuncId());

        emExpert = (EmExpert) this.listCursor.getCurrentObject();

        if (!"0".equals(emExpert.getEmExpStatus()))

          JOptionPane.showMessageDialog(this, "非编辑状态单据，不可以删除！", "提示", JOptionPane.ERROR_MESSAGE);

        this.listPanel.getZcEmExpertServiceDelegate().deleteByPrimaryKey(emExpert.getEmExpertCode(), requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.listCursor.removeCurrentObject();

        refreshData();

        this.listPanel.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }
  }

  private void doAudit() {
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "确认启用此专家?", "审核", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;

    EmExpert afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditButton.getFuncId());

      EmExpert emExpert = (EmExpert) this.listCursor.getCurrentObject();

      emExpert.setEmExpStatus("exec");

      afterSaveBill = this.listPanel.getZcEmExpertServiceDelegate().updateByPrimaryKey(emExpert, requestMeta);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "专家启用成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    }
  }

  private void doUnAudit() {
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "确认暂停本次专家?", "审核", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;

    EmExpert afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditButton.getFuncId());

      EmExpert emExpert = (EmExpert) this.listCursor.getCurrentObject();

      emExpert.setEmExpStatus("suspended");

      afterSaveBill = this.listPanel.getZcEmExpertServiceDelegate().updateByPrimaryKey(emExpert, requestMeta);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "专家暂停成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    }
  }

  private void doCancel() {
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "确认启用此专家?", "审核", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;

    EmExpert afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditButton.getFuncId());

      EmExpert emExpert = (EmExpert) this.listCursor.getCurrentObject();

      emExpert.setEmExpStatus("cancel");

      this.listPanel.getZcEmExpertServiceDelegate().updateByPrimaryKey(emExpert, requestMeta);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      refreshData();

      JOptionPane.showMessageDialog(this, "专家作废成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    }
  }

  private void doExpertAbility() {
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    boolean success = true;

    try {

      EmExpert emExpert = (EmExpert) this.listCursor.getCurrentObject();

      if (emExpert == null || emExpert.getEmExpertCode() == null) {

        JOptionPane.showMessageDialog(this, "当前专家代码无效，无法获得此专家的评价信息！", "提示", JOptionPane.ERROR_MESSAGE);

        return;
      }

      if (emExpert.getEmExpStatus() == null || "".equals(emExpert.getEmExpStatus()) || "0".equals(emExpert.getEmExpStatus())) {

        JOptionPane.showMessageDialog(this, "新增状态下的专家信息还不能进行评价，请启用后再评价！", "提示", JOptionPane.ERROR_MESSAGE);

        return;
      }

      EmExpertAbility emExpertAbility = this.getZcEmExpertAbilityServiceDelegate().selectByPrimaryKey(emExpert.getEmExpertCode(), requestMeta);

      ArrayList expertAbilityList = new ArrayList(1);

      expertAbilityList.add(emExpertAbility);

      ListCursor expertAbilityCursor = new ListCursor(expertAbilityList, 0);

      expertAbilityCursor.setCurrentObject(emExpertAbility);

      new ZcExpertAbilityInfoDialog(this.parent, expertAbilityCursor);

    } catch (Exception ex) {

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }
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

  private void doPrevious() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldBean);

      }

    }

    listCursor.previous();

    refreshData();

  }

  private void doNext() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldBean);

      }

    }

    listCursor.next();

    refreshData();

  }

  //===========================================以下为外部部件===================================
  /**
   * 评标类别外部部件，明细用到
   * @author Administrator
   *
   */
  private class EmExpertTypeHandler implements IForeignEntityHandler {

    private String columNames[];

    public EmExpertTypeHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {

      JTable table = itemTablePanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return;

      int k2 = table.convertRowIndexToModel(k);

      EmExpertTypeJoin subBean = (EmExpertTypeJoin) (model.getBean(k2));

      if (selectedDatas.size() > 0) {

        EmExpertType emExpertType = (EmExpertType) selectedDatas.get(0);

        subBean.setEmTypeCode(emExpertType.getEmTypeCode());

        subBean.setEmTypeName(emExpertType.getEmTypeName());
      }

      model.fireTableDataChanged();

    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        EmExpertType rowData = (EmExpertType) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getEmTypeCode();

        data[i][col++] = rowData.getEmTypeName();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        public boolean isCellEditable(int row, int colum) {

          return false;

        }

      };

      return model;

    }

    @Override
    public boolean isMultipleSelect() {

      return false;

    }
  }

}