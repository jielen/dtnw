package com.ufgov.zc.client.zc.agency;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.TITLE_TRANS_ZC_P_PRO_MAKE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbAgencyToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.ui.conditionitem.AsValComboboxConditionItem2;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.NewLineFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextAreaFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.model.AsVal;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.checkrule.BaseRule;
import com.ufgov.zc.common.zc.checkrule.ZcMakeCheckRule;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcBAgency;
import com.ufgov.zc.common.zc.model.ZcBAgencyListAptd;
import com.ufgov.zc.common.zc.model.ZcPProMake;

/**
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class ZcEbAgencyEditPanel extends AbstractMainSubEditPanel {

  public static final long serialVersionUID = -2779110682087878491L;

  public static final Logger logger = Logger.getLogger(ZcEbAgencyEditPanel.class);

  public RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  public FuncButton addButton = new AddButton();

  public FuncButton previousButton = new PreviousButton();

  public FuncButton saveButton = new SaveButton();

  public FuncButton deleteButton = new DeleteButton();

  public FuncButton nextButton = new NextButton();

  public FuncButton exitButton = new ExitButton();

  private FuncButton editButton = new EditButton();

  private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  protected ListCursor listCursor;

  public ZcBAgency oldZcBAgency;

  public String tabStatus;

  public ZcEbAgencyListPanel listPanel;

  public JTablePanel biTablePanel = new JTablePanel();

  public ZcEbAgencyEditPanel self = this;

  public GkBaseDialog parent;

  JTabbedPane biTabPane = null;

  public ElementConditionDto merDto;

  JFuncToolBar bottomToolBar1 = null;

  private String status = null;

  public String getTitle() {
    return LangTransMeta.translate("代理机构登记");
  }

  public AsVal asVal = new AsVal();

  public boolean haveInitFlag = false;

  public static  String getCompoId() {
    return "ZC_B_AGENCY_WH";
  }

  public ZcEbAgencyEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbAgencyListPanel listPanel) {
    super(ZcBAgency.class, BillElementMeta.getBillElementMetaWithoutNd("ZC_B_AGENCY_WH"));
    this.listCursor = listCursor;
    this.tabStatus = tabStatus;
    this.listPanel = listPanel;
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), getTitle(), TitledBorder.CENTER, TitledBorder.TOP,
      new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.parent = parent;
    this.colCount = 3;
    init();
    requestMeta.setCompoId(getCompoId());
    IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
      "baseDataServiceDelegate");
    refreshData();
    stopTableEditing();
    updateFieldEditorsEditable();
    this.haveInitFlag = true;
  }

  public ZcEbAgencyEditPanel(Class<ZcPProMake> class1, BillElementMeta billElementMetaWithoutNd) {
    super(ZcBAgency.class, billElementMetaWithoutNd);
  }

  protected void initFieldEditorPanel() {
    fieldEditors = createFieldEditors();
    int row = 0;
    int col = 0;

    fieldEditorPanel.setLayout(new GridBagLayout());
    for (int i = 0; i < fieldEditors.size(); i++) {
      AbstractFieldEditor comp = fieldEditors.get(i);
      if (comp.isVisible()) {
        if (comp instanceof NewLineFieldEditor) {
          row++;
          col = 0;
          continue;
        } else if (comp instanceof TextAreaFieldEditor) {
          //转到新的一行
          row++;
          col = 0;
          JLabel label = new JLabel(getLabelText(comp));
          comp.setPreferredSize(new Dimension(150, comp.getOccRow() * 26));
          fieldEditorPanel.add(label, new GridBagConstraints(col, row, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
            4, 0, 4, 4), 0, 0));
          fieldEditorPanel.add(comp, new GridBagConstraints(col + 1, row, comp.getOccCol(), comp.getOccRow(), 1.0, 1.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, new Insets(4, 0, 4, 4), 0, 0));
          //将当前所占的行空间偏移量计算上
          row += comp.getOccRow();
          col = 0;
          continue;
        }
        JLabel label = new JLabel(comp.getName());
        comp.setPreferredSize(new Dimension(150, 23));
        fieldEditorPanel.add(label, new GridBagConstraints(col, row, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
          0, 5, 5), 0, 0));
        fieldEditorPanel.add(comp, new GridBagConstraints(col + 1, row, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
          new Insets(5, 0, 5, 5), 0, 0));
        if (col == colCount * 2 - 2) {
          row++;
          col = 0;
        } else {
          col += 2;
        }
      }
    }
  }

  public String getLabelText(AbstractFieldEditor comp) {
    StringBuffer buff = new StringBuffer();
    buff.append("<html><a>&nbsp;");
    buff.append(comp.getName());
    if (comp.getMaxContentSize() <= 0) {
      buff.append("</a></html>");
    } else {
      if (comp.getOccRow() >= 2) {
        buff.append("<br>(");
      } else {
        buff.append("(");
      }
      buff.append(comp.getMaxContentSize());
      buff.append("字内)</a></html>");
    }
    return buff.toString();
  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();
    ZcBAgency zcBAgency = (ZcBAgency) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
    TextFieldEditor zcAgeyCode = new TextFieldEditor("机构代码", "zcAgeyCode");
    if (zcBAgency != null) {
      zcAgeyCode.setEnabled(false);
    }
    editorList.add(zcAgeyCode);
    TextFieldEditor zcAgeyName = new TextFieldEditor("机构名称", "zcAgeyName");
    editorList.add(zcAgeyName);

    AsValFieldEditor zcStatCode = new AsValFieldEditor("状态", "zcStatCode", "ZC_VS_AGENCY_STATUS");
    editorList.add(zcStatCode);
    String columNames[] = { "地域代码", "地域名称" };
    ZcAgeyFnHandler handler = new ZcAgeyFnHandler(columNames);
    ElementConditionDto elementCondtiontDto = new ElementConditionDto();
    ForeignEntityFieldEditor zcDiyuDaima = new ForeignEntityFieldEditor("ZC_B_AGENCY.getZcDiyuDaima", elementCondtiontDto, 20, handler, columNames,
      "地域代码", "zcDiyuDaima") {

      public static final long serialVersionUID = -7737549222488261602L;

      @Override
      public void setValue(Object value) {
        ZcBAgency zcBAgency = (ZcBAgency) value;
        if (zcBAgency == null || zcBAgency.getZcDiyuDaima() == null) {
          field.setText(null);
          field.setToolTipText(null);

        } else {
          field.setText(zcBAgency.getZcDiyuDaima());
          field.setToolTipText("[" + zcBAgency.getZcDiyuDaima() + "]" + zcBAgency.getZcDiyuName());
        }
      }

      @Override
      public Object getValue() {
        return this.field.getText();
      }
    };
    if (zcBAgency != null) {
      zcDiyuDaima.setEnabled(false);
    }
    //    editorList.add(zcDiyuDaima);

    TextFieldEditor zcDiyuName = new TextFieldEditor("地域名称", "zcDiyuName");
    zcDiyuName.setEnabled(false);
    //    editorList.add(zcDiyuName);

    AsValFieldEditor zcAgeyType = new AsValFieldEditor("机构类型", "zcAgeyType", "ZC_VS_AGENCY_TYPE");
    editorList.add(zcAgeyType);

    TextFieldEditor zcAgeyJgdm = new TextFieldEditor("组织机构代码", "zcAgeyJgdm");
    editorList.add(zcAgeyJgdm);

    TextFieldEditor zcAgeyAddr = new TextFieldEditor("营业地址", "zcAgeyAddr");
    editorList.add(zcAgeyAddr);

    TextFieldEditor zcAgeyZip = new TextFieldEditor("邮政编码", "zcAgeyZip");
    editorList.add(zcAgeyZip);

    TextFieldEditor zcAgeyLinkman = new TextFieldEditor("联系人姓名", "zcAgeyLinkman");
    editorList.add(zcAgeyLinkman);

    TextFieldEditor zcAgeyTel = new TextFieldEditor("联系电话", "zcAgeyTel");
    editorList.add(zcAgeyTel);

    DateFieldEditor startTime = new DateFieldEditor("有效开始时间", "startTime");

    editorList.add(startTime);

    DateFieldEditor endTime = new DateFieldEditor("有效截止时间", "endTime");

    editorList.add(endTime);

    for (AbstractFieldEditor editor : editorList) {
      editor.setEditObject(zcBAgency);
    }
    return editorList;
  }

  public void setListDefaultValue(AsValComboboxConditionItem2 asValComboboxConditionItem2) {
    if ("VS_Y/N".equals(asVal.getValSetId())) {
      asValComboboxConditionItem2.setValue(null);
    }
  }

  public void setItemBiDefaultValue(ZcBAgencyListAptd zcBAgencAptd) {
    zcBAgencAptd.setZcAptdCode(null);
    zcBAgencAptd.setZcAptdName(null);

  }

  @Override
  public JComponent createSubBillPanel() {
    biTabPane = new JTabbedPane();
    biTablePanel.init();
    biTablePanel.getSearchBar().setVisible(false);
    biTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");
    biTablePanel.getTable().setShowCheckedColumn(true);
    biTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));
    biTabPane.addTab("中介机构资质", biTablePanel);

    bottomToolBar1 = new JFuncToolBar();
    FuncButton addBtn1 = new SubaddButton(false);
    JButton insertBtn1 = new SubinsertButton(false);
    JButton delBtn1 = new SubdelButton(false);
    bottomToolBar1.add(addBtn1);
    bottomToolBar1.add(insertBtn1);
    bottomToolBar1.add(delBtn1);
    biTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);
    ZcBAgency zcBAgency = (ZcBAgency) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
    if (zcBAgency != null) {
      bottomToolBar1.setEnabled(false);
    }
    addBtn1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZcBAgencyListAptd zcBAgencAptd = new ZcBAgencyListAptd();
        zcBAgencAptd.setTempId(Guid.genID());
        setItemBiDefaultValue(zcBAgencAptd);
        setTableapdEditor(biTablePanel.getTable());
        int rowNum = addSub(biTablePanel, zcBAgencAptd);
        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);
      }
    });

    insertBtn1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZcBAgencyListAptd zcBAgencAptd = new ZcBAgencyListAptd();
        zcBAgencAptd.setTempId(Guid.genID());
        setItemBiDefaultValue(zcBAgencAptd);
        int rowNum = addSub(biTablePanel, zcBAgencAptd);
        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);
      }
    });

    delBtn1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        deleteSub(biTablePanel);
      }
    });

    biTabPane.setMinimumSize(new Dimension(240, 500));

    return biTabPane;
  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(getCompoId());
    toolBar.add(addButton);
    toolBar.add(saveButton);
    toolBar.add(editButton);
    toolBar.add(deleteButton);
    toolBar.add(previousButton);
    toolBar.add(nextButton);
    toolBar.add(exitButton);

    //    editButton.setVisible(false);

    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 修改
        doEdit();
      }
    });

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 新增
        doAdd();
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

  public void refreshAll(ZcBAgency afterSaveBill, boolean isRefreshButton) {
    this.listCursor.setCurrentObject(afterSaveBill);
    refreshData();
  }

  /*
   * 新增
   */
  public void doAdd() {
    if (this.doExit()) {
      this.listPanel.doAdd();
    }
  }

  public void doEdit() {
    pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
    bottomToolBar1.setEnabled(true);
    saveButton.setEnabled(true);
    updateFieldEditorsEditable();
  }

  public BaseRule getZcMakeCheckRule() {
    return ZcMakeCheckRule.getInstance();
  }

  public void doPrevious() {
    if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);
      if (num == JOptionPane.YES_OPTION) {
        if (!doSave()) {
          return;
        }
      } else {
        listCursor.setCurrentObject(oldZcBAgency);
      }
    }
    listCursor.previous();
    refreshData();
  }

  public void doNext() {
    if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);
      if (num == JOptionPane.YES_OPTION) {
        if (!doSave()) {
          return;
        }
      } else {
        listCursor.setCurrentObject(oldZcBAgency);
      }
    }
    listCursor.next();
    refreshData();
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

  /**
   * 保存前校验
   * @param cpApply
   * @return
   */
  public Boolean checkBeforeSave() {

    List mainNotNullList = ((BillElementMeta) this.listPanel.getBillElementMeta()).getNotNullBillElement();

    ZcBAgency zcBAgency = (ZcBAgency) this.listCursor.getCurrentObject();

    StringBuilder errorInfo = new StringBuilder();

    String mainValidateInfo = ZcUtil.validateBillElementNull(zcBAgency, mainNotNullList);

    if (mainValidateInfo.length() != 0) {

      errorInfo.append(LangTransMeta.translate(TITLE_TRANS_ZC_P_PRO_MAKE)).append("：\n").append(mainValidateInfo.toString()).append("\n");
      JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示", JOptionPane.INFORMATION_MESSAGE);
      return false;
    }
    Pattern pattern = Pattern.compile("\\d*");
    //    Boolean flag = false;
    //    StringBuffer str = new StringBuffer("请填写:\n");
    //    if (zcBAgency.getZcAgeyCode() == null || "".equals(zcBAgency.getZcAgeyCode().trim())) {
    //      str.append("机构代码\n");
    //      flag = true;
    //    }
    //    if (zcBAgency.getZcAgeyName() == null || "".equals(zcBAgency.getZcAgeyName().trim())) {
    //      str.append("机构名称\n");
    //      flag = true;
    //    }
    //    if (zcBAgency.getZcDiyuDaima() == null || "".equals(zcBAgency.getZcDiyuDaima().trim())) {
    //      str.append("地域代码\n");
    //      flag = true;
    //    }
    //    if (zcBAgency.getZcDiyuName() == null || "".equals(zcBAgency.getZcDiyuName().trim())) {
    //      str.append("地域名称\n");
    //      flag = true;
    //    }
    Matcher matcher = pattern.matcher(zcBAgency.getZcAgeyCode().trim());
    boolean isInt = matcher.matches();
    if (!isInt) {
      JOptionPane.showMessageDialog(this, "[机构代码]只能填写数字!", "提示", JOptionPane.INFORMATION_MESSAGE);
      return false;
    }
    if (isSubNull()) {
      JOptionPane.showMessageDialog(this, "[中价机构资质]资质编码不能为空!", "提示", JOptionPane.INFORMATION_MESSAGE);
      return false;
    }
    if (isSubRepDate()) {
      JOptionPane.showMessageDialog(this, "[中价机构资质]存在重复数据，请得新选择!", "提示", JOptionPane.INFORMATION_MESSAGE);
      return false;
    }
    //    if (flag) {
    //      JOptionPane.showMessageDialog(this, str.toString(), "提示", JOptionPane.INFORMATION_MESSAGE);
    //      return false;
    //    } else {
    return true;
    //    }

  }

  //是否存在空数据
  private boolean isSubNull() {
    ZcBAgency zcBAgency = (ZcBAgency) this.listCursor.getCurrentObject();
    Set set = new HashSet();
    boolean flag = false;
    if (zcBAgency.getZcBAgencAptdList() != null) {
      for (int t = 0; t < zcBAgency.getZcBAgencAptdList().size(); t++) {
        if (((ZcBAgencyListAptd) zcBAgency.getZcBAgencAptdList().get(t)).getZcAptdCode() == null) {
          flag = true;
          break;
        }
      }
    }
    return flag;
  }

  //子表是否存在重复数据，同时删除空数据
  private boolean isSubRepDate() {
    ZcBAgency zcBAgency = (ZcBAgency) this.listCursor.getCurrentObject();
    Set set = new HashSet();
    boolean flag = false;
    if (zcBAgency.getZcBAgencAptdList() != null) {
      for (int t = 0; t < zcBAgency.getZcBAgencAptdList().size(); t++) {
        if (((ZcBAgencyListAptd) zcBAgency.getZcBAgencAptdList().get(t)).getZcAptdCode() == null) {
          flag = true;

        }
      }
      if (flag) {
        return flag;
      }
      for (int i = 0; i < zcBAgency.getZcBAgencAptdList().size() / 2 + 1; i++) {
        {
          for (int j = i + 1; j < zcBAgency.getZcBAgencAptdList().size(); j++) {
            if (((ZcBAgencyListAptd) zcBAgency.getZcBAgencAptdList().get(i)).getZcAptdCode().equals(
              ((ZcBAgencyListAptd) zcBAgency.getZcBAgencAptdList().get(j)).getZcAptdCode())) {
              flag = true;
              break;
            }
          }
        }
      }
      return flag;
    } else {
      return flag;
    }
  }

  public boolean doSave() {
    if (!isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return true;
    }

    if (!this.checkBeforeSave()) {
      return false;
    }

    boolean success = true;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(saveButton.getFuncId());
      ZcBAgency zcBAgency = (ZcBAgency) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
      List keyList = this.listPanel.agencyServiceDelegate.getPriKey(zcBAgency, requestMeta);
      if (keyList.size() > 0 && "new".equals(status)) {
        JOptionPane.showMessageDialog(this, "机构代码已存在，请重新录入!", "提示", JOptionPane.INFORMATION_MESSAGE);
        return true;
      }
      List list = this.listPanel.agencyServiceDelegate.getIsExists(zcBAgency, requestMeta);
      if (list.size() > 0) {
        JOptionPane.showMessageDialog(this, "机构代码已存在，请重新录入!", "提示", JOptionPane.INFORMATION_MESSAGE);
        return true;
      }
      this.listPanel.agencyServiceDelegate.doSave(zcBAgency, requestMeta);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      editButton.setEnabled(true);
      saveButton.setEnabled(false);
      pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
      updateFieldEditorsEditable();
      refreshData();
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  protected void doDelete() {
    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      boolean success = true;
      ZcBAgency zcBAgency = null;
      String errorInfo = "";
      try {
        requestMeta.setFuncId(deleteButton.getFuncId());
        zcBAgency = (ZcBAgency) this.listCursor.getCurrentObject();
        this.listPanel.agencyServiceDelegate.doDelete(zcBAgency, requestMeta);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        success = false;
        errorInfo += e.getMessage();
      }
      if (success) {

        this.listCursor.removeCurrentObject();
        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        this.refreshData();
        this.listPanel.refreshCurrentTabData();
      } else {
        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public void stopTableEditing() {
    JPageableFixedTable biTable = this.biTablePanel.getTable();
    if (biTable.isEditing()) {
      biTable.getCellEditor().stopCellEditing();
    }
  }

  public boolean isDataChanged() {
    stopTableEditing();
    return !DigestUtil.digest(oldZcBAgency).equals(DigestUtil.digest(listCursor.getCurrentObject()));
  }

  protected void refreshData() {
    ZcBAgency zcBAgency = (ZcBAgency) listCursor.getCurrentObject();
    if (zcBAgency != null) {
      status = "edit";
      List agencylist = null;
      try {
        this.merDto = new ElementConditionDto();
        merDto.setZcText0(zcBAgency.getZcAgeyCode());
        agencylist = this.listPanel.agencyServiceDelegate.getZcZcBAgencyAptdList(merDto, requestMeta);
        if (agencylist != null) {
          zcBAgency.setZcBAgencAptdList(agencylist);
        }
        listCursor.setCurrentObject(zcBAgency);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      previousButton.setEnabled(true);
      nextButton.setEnabled(true);
    } else {
      status = "new";
      pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
      zcBAgency = new ZcBAgency();
      zcBAgency.setZcStatCode("0");
      zcBAgency.setZcBAgencAptdList(new ArrayList());
      // 新增数据默认插入一行
      //      ZcBAgencyListAptd zcBAgencAptd = new ZcBAgencyListAptd();
      //      setItemBiDefaultValue(zcBAgencAptd);
      //      zcBAgency.getZcBAgencAptdList().add(zcBAgencAptd);
      listCursor.getDataList().add(zcBAgency);
      listCursor.setCurrentObject(zcBAgency);
      previousButton.setEnabled(false);
      nextButton.setEnabled(false);
    }
    biTablePanel.setTableModel(ZcEbAgencyToTableModelConverter.convertSubBiTableData(zcBAgency.getZcBAgencAptdList(), wfCanEditFieldMap));
    // 翻译从表表头列
    ZcUtil.translateColName(biTablePanel.getTable(), ZcEbAgencyToTableModelConverter.biInfo);
    setTableapdEditor(biTablePanel.getTable());
    this.setEditingObject(zcBAgency);
    setOldObject();
    this.fitTable();
    biTabPane.repaint();
  }

  public void setOldObject() {
    oldZcBAgency = (ZcBAgency) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  public class ZcAgeyFnHandler implements IForeignEntityHandler {

    public String columNames[];

    public ZcAgeyFnHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {
      ZcBAgency zcPProMake = (ZcBAgency) listCursor.getCurrentObject();
      for (Object object : selectedDatas) {
        ZcBAgency zcBAgency = (ZcBAgency) object;
        zcPProMake.setZcDiyuDaima(zcBAgency.getZcDiyuDaima());
        zcPProMake.setZcDiyuName(zcBAgency.getZcDiyuName());
        setEditingObject(zcPProMake);
      }
    }

    @Override
    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        ZcBAgency rowData = (ZcBAgency) showDatas.get(i);
        int col = 0;
        data[i][col++] = rowData.getZcDiyuDaima();
        data[i][col++] = rowData.getZcDiyuName();
      }

      MyTableModel model = new MyTableModel(data, columNames) {

        public static final long serialVersionUID = 1821460782676810898L;

        @Override
        public boolean isCellEditable(int row, int colum) {
          return false;
        }

        @Override
        public Class getColumnClass(int column) {
          if (column >= 0 && column < getColumnCount() && this.getRowCount() > 0) {
            for (int row = 0; row < this.getRowCount(); row++) {
              if (getValueAt(row, column) != null) {
                return getValueAt(row, column).getClass();
              }
            }
          }
          return Object.class;
        }
      };
      return model;
    }

    @Override
    public boolean isMultipleSelect() {
      return false;
    }
  }

  public void setTableapdEditor(JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    String[] merColumNames = { "资质代码", "资质名称" };
    ZcEbMerHandler merHandler = new ZcEbMerHandler(merColumNames);
    this.merDto = new ElementConditionDto();
    ForeignEntityFieldCellEditor merEditor = new ForeignEntityFieldCellEditor("ZC_B_AGENCY.getZcBAgencyApdList", this.merDto, 20, merHandler,
      merColumNames, "资质", "zcAptdCode");
    SwingUtil.setTableCellEditor(table, "ZC_APTD_CODE", merEditor);
  }

  @Override
  protected void updateFieldEditorsEditable() {

    super.updateFieldEditors();

    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        if (fd.getFieldName() != null

          && (fd.getFieldName().equals("zcStatCode") || fd.getFieldName().equals("zcAgeyType") || fd.getFieldName().equals("zcAgeyJgdm")
            || fd.getFieldName().equals("zcAgeyAddr") || fd.getFieldName().equals("zcAgeyZip")) || fd.getFieldName().equals("zcAgeyLinkman")
          || fd.getFieldName().equals("zcAgeyTel") || fd.getFieldName().equals("startTime") || fd.getFieldName().equals("endTime")) {
          fd.setEnabled(true);
        } else {
          fd.setEnabled(false);
        }
      }
    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        fd.setEnabled(false);

      }
      saveButton.setEnabled(false);
    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        fd.setEnabled(true);

      }
      saveButton.setEnabled(true);
    }

  }

  /*
   * 资质代码选择
   */
  public class ZcEbMerHandler implements IForeignEntityHandler {

    public String columNames[];

    public ZcEbMerHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {
      JTable table = biTablePanel.getTable();
      BeanTableModel model = (BeanTableModel) table.getModel();
      int k = table.getSelectedRow();
      if (k < 0)
        return;
      int k2 = table.convertRowIndexToModel(k);
      ZcBAgencyListAptd aptd = (ZcBAgencyListAptd) model.getBean(k2);
      if (selectedDatas.size() > 0) {
        ZcBAgencyListAptd mer = (ZcBAgencyListAptd) selectedDatas.get(0);
        aptd.setZcAptdCode(mer.getZcAptdCode());
        aptd.setZcAptdName(mer.getZcAptdName());

        ForeignEntityFieldCellEditor se = (ForeignEntityFieldCellEditor) table.getCellEditor(table.getSelectedRow(), table.getSelectedColumn());
        se.getEditor().setValue(mer.getZcAptdCode());
      }
      model.fireTableDataChanged();
    }

    //点击清空按钮清空资质信息
    public void afterClear() {
      JTable table = biTablePanel.getTable();
      BeanTableModel model = (BeanTableModel) table.getModel();
      int k = table.getSelectedRow();
      if (k < 0)
        return;
      int k2 = table.convertRowIndexToModel(k);
      ZcBAgencyListAptd aptd = (ZcBAgencyListAptd) model.getBean(k2);
      aptd.setZcAptdCode("");
      aptd.setZcAptdName("");

      ForeignEntityFieldCellEditor se = (ForeignEntityFieldCellEditor) table.getCellEditor(table.getSelectedRow(), table.getSelectedColumn());
      se.getEditor().setValue("");
      model.fireTableDataChanged();

    }

    @Override
    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        ZcBAgencyListAptd rowData = (ZcBAgencyListAptd) showDatas.get(i);
        int col = 0;
        data[i][col++] = rowData.getZcAptdCode();
        data[i][col++] = rowData.getZcAptdName();

      }

      MyTableModel model = new MyTableModel(data, columNames) {
        @Override
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

    public boolean beforeSelect(ElementConditionDto dto) {
      JTable table = biTablePanel.getTable();
      BeanTableModel model = (BeanTableModel) table.getModel();
      int k = table.getSelectedRow();
      if (k < 0)
        return false;
      int k2 = table.convertRowIndexToModel(k);
      ZcBAgencyListAptd item = (ZcBAgencyListAptd) model.getBean(k2);
      //      dto.setZcText0(ZcBAgencAptd.ENABLE);
      //      dto.setZcText1(item.getZcCatalogueCode());
      //      if (item.getZcBraCode().indexOf(",") == -1)
      //        dto.setZcText2(item.getZcBraCode());
      //      else {
      //        String[] brands = item.getZcBraCode().split(",");
      //        List brandList = null;
      //        for (int i = 0; i < brands.length; i++) {
      //
      //        }
      //      }
      //      dto.setZcText4(item.getZcBraCode());
      return true;
    }
  }

}
