package com.ufgov.zc.client.zc.catalogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
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
import com.ufgov.zc.client.component.ui.conditionitem.AsValComboboxConditionItem2;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityTreeFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.NewLineFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextAreaFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.model.AsVal;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.checkrule.BaseRule;
import com.ufgov.zc.common.zc.checkrule.ZcMakeCheckRule;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityTreeHandler;
import com.ufgov.zc.common.zc.model.TreeNodeValueObject;
import com.ufgov.zc.common.zc.model.ZcBAgencyListAptd;
import com.ufgov.zc.common.zc.model.ZcBCatalogue;

/**
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("unchecked")
public class ZcEbCatalogueEditPanel extends AbstractMainSubEditPanel {

  public static final long serialVersionUID = -2779110682087878491L;

  public static final Logger logger = Logger.getLogger(ZcEbCatalogueEditPanel.class);

  public RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  public FuncButton addButton = new AddButton();

  public FuncButton previousButton = new PreviousButton();

  public FuncButton saveButton = new SaveButton();

  public FuncButton deleteButton = new DeleteButton();

  public FuncButton nextButton = new NextButton();

  public FuncButton exitButton = new ExitButton();

  private FuncButton editButton = new EditButton();

  protected ListCursor listCursor;

  public ZcBCatalogue oldZcBAgency;

  public String tabStatus;

  private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  public ForeignEntityTreeFieldEditor zcCatalogueCodePar = null;

  public ZcEbCatalogueListPanel listPanel;

  public JTablePanel biTablePanel = new JTablePanel();

  public ZcEbCatalogueEditPanel self = this;

  List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

  public GkBaseDialog parent;

  JTabbedPane biTabPane = null;

  public ElementConditionDto merDto;

  JFuncToolBar bottomToolBar1 = null;

  TextFieldEditor zcYear = null;

  public String getTitle() {
    return LangTransMeta.translate("集中采购目录");
  }

  public AsVal asVal = new AsVal();

  public boolean haveInitFlag = false;

  public static String getCompoId() {
    return "ZC_B_CATALOGUE";
  }

  public ZcEbCatalogueEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbCatalogueListPanel listPanel) {
    super(ZcBCatalogue.class, BillElementMeta.getBillElementMetaWithoutNd("ZC_B_CATALOGUE"));
    this.listCursor = listCursor;
    this.tabStatus = tabStatus;
    this.listPanel = listPanel;
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), getTitle(), TitledBorder.CENTER, TitledBorder.TOP,
      new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.parent = parent;
    this.colCount = 3;
    init();
    // workPanel.add(fieldEditorPanel, BorderLayout.CENTER);
    requestMeta.setCompoId(getCompoId());
    IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
      "baseDataServiceDelegate");
    refreshData();
    stopTableEditing();
    updateFieldEditorsEditable();
    this.haveInitFlag = true;
  }

  public ZcEbCatalogueEditPanel(Class<ZcBCatalogue> class1, BillElementMeta billElementMetaWithoutNd) {
    super(ZcBCatalogue.class, billElementMetaWithoutNd);
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
          // 转到新的一行
          row++;
          col = 0;
          JLabel label = new JLabel(getLabelText(comp));
          comp.setPreferredSize(new Dimension(150, comp.getOccRow() * 40));
          fieldEditorPanel.add(label, new GridBagConstraints(col, row, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
            4, 0, 4, 4), 0, 0));
          fieldEditorPanel.add(comp, new GridBagConstraints(col + 1, row, comp.getOccCol(), comp.getOccRow(), 1.0, 1.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, new Insets(4, 0, 4, 4), 0, 0));
          // 将当前所占的行空间偏移量计算上
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

    ZcBCatalogue catalogue = (ZcBCatalogue) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
    String columNames[] = { "地域代码", "地域名称" };
    CatalogueFnHandler handler = new CatalogueFnHandler(columNames);
    ElementConditionDto elementCondtiontDto = new ElementConditionDto();
    ForeignEntityFieldEditor zcDiyuDaima = new ForeignEntityFieldEditor("ZcBCatalogue.getZcDiyuDaima", elementCondtiontDto, 20, handler, columNames,
      "地域代码", "zcDiyuDaima") {

      public static final long serialVersionUID = -7737549222488261602L;

      @Override
      public void setValue(Object value) {
        ZcBCatalogue zcBAgency = (ZcBCatalogue) value;
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
    // editorList.add(zcDiyuDaima);

    TextFieldEditor zcDiyuName = new TextFieldEditor("地域名称", "zcDiyuName");
    zcDiyuName.setEnabled(false);
    // editorList.add(zcDiyuName);

    TextFieldEditor zcCatalogueCode = new TextFieldEditor("品目代码", "zcCatalogueCode");
    editorList.add(zcCatalogueCode);

    TextFieldEditor zcCatalogueName = new TextFieldEditor("品目名称 ", "zcCatalogueName");
    editorList.add(zcCatalogueName);

    TextFieldEditor zcYear = new TextFieldEditor("财政年度 ", "zcYear");
    zcYear.setEnabled(false);
    editorList.add(zcYear);

    AsValFieldEditor zcCgLeixing = new AsValFieldEditor("采购分类", "zcCatalogueType", "ZC_VS_CG_TYPE");
    editorList.add(zcCgLeixing);

    AsValFieldEditor zcIsVital = new AsValFieldEditor("是否重要品目", "zcIsVital", "VS_Y/N");
    editorList.add(zcIsVital);

    AsValFieldEditor zcCatalogueType = new AsValFieldEditor("品目类型", "zcPinmuCtlg", "ZC_VS_PIN_TYPE");
    editorList.add(zcCatalogueType);

    zcCatalogueCodePar = new ForeignEntityTreeFieldEditor("ZcBCatalogue.getZcBCatalogueyear", 20, new TreeHandler(), "上级品目代码", "zcCatalogueCodePar");
    // zcCatalogueCodePar.setEnabled(false);
    editorList.add(zcCatalogueCodePar);

    TextFieldEditor zcCatalogueNamePar = new TextFieldEditor("上级品目名称", "zcCatalogueNamePar");
    zcCatalogueNamePar.setEnabled(false);
    editorList.add(zcCatalogueNamePar);
    //
    // TextFieldEditor zcMetricUnit = new TextFieldEditor("计量单位",
    // "zcMetricUnit");
    // editorList.add(zcMetricUnit);
    //
    // TextFieldEditor zcQuota = new TextFieldEditor("单价限额标准", "zcQuota");
    // editorList.add(zcQuota);
    //
    // TextFieldEditor zcQuotaUnit = new TextFieldEditor("批量限额标准",
    // "zcQuotaUnit");
    // editorList.add(zcQuotaUnit);
    //
    // TextFieldEditor zcZcgzStd = new TextFieldEditor("资产购置标准",
    // "zcZcgzStd");
    // editorList.add(zcZcgzStd);
    //
    // AsValFieldEditor zcIsAssert = new AsValFieldEditor("是否资产类品目",
    // "zcIsAssert", "ZC_VS_ASSERT");
    // editorList.add(zcIsAssert);

    AsValFieldEditor zcIsUsed = new AsValFieldEditor("是否使用 ", "zcIsUsed", "VS_Y/N");
    editorList.add(zcIsUsed);

     AsValFieldEditor zcIsdianZitouBiao = new AsValFieldEditor("是否协议供货","zcIsdianZitouBiao", "VS_Y/N");
     editorList.add(zcIsdianZitouBiao);
    //
    // MoneyFieldEditor jjPpNum = new MoneyFieldEditor("竞价品牌数量最低要求 ",
    // "zcJjPpNum");
    // zcYear.setEnabled(false);
    // editorList.add(jjPpNum);
    //
    // MoneyFieldEditor jjPriceQuota = new MoneyFieldEditor("竞价商品价格差(%) ",
    // "zcJjPriceQuota");
    // zcYear.setEnabled(false);
    //
    // editorList.add(jjPriceQuota);
    // TextFieldEditor zcAgeyTe1l11 = new TextFieldEditor("单次需求数量限制 ",
    // "zcAgeyTel");
    // editorList.add(zcAgeyTe1l11);
    // for (AbstractFieldEditor editor : editorList) {
    // editor.setEditObject(catalogue);
    // }
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
    biTabPane.setVisible(false);
    return biTabPane;
  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(getCompoId());
    toolBar.add(addButton);
    toolBar.add(editButton);
    toolBar.add(saveButton);
    toolBar.add(deleteButton);
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
        stopTableEditing();
        // 修改
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

  public void refreshAll(ZcBCatalogue afterSaveBill, boolean isRefreshButton) {
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

  /*
   * 修改
   */
  public void doEdit() {
    pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
    saveButton.setEnabled(true);
    editButton.setEnabled(false);
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

  @Override
  protected void updateFieldEditorsEditable() {

    super.updateFieldEditors();

    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        if (fd.getFieldName() != null

        && (fd.getFieldName().equals("zcDiyuDaima") || fd.getFieldName().equals("zcIsUsed")

        || fd.getFieldName().equals("zcCatalogueName") || fd.getFieldName().equals("zcYear")) || fd.getFieldName().equals("zcCatalogueType")

        || fd.getFieldName().equals("zcIsVital") || fd.getFieldName().equals("zcPinmuCtlg") || fd.getFieldName().equals("zcCatalogueCodePar")
          || fd.getFieldName().equals("zcMetricUnit") || fd.getFieldName().equals("zcIsdianZitouBiao") || fd.getFieldName().equals("zcJjPpNum")
          || fd.getFieldName().equals("zcJjPriceQuota")) {

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

  /**
   * 保存前校验
   * 
   * @param cpApply
   * @return
   */
  public Boolean checkBeforeSave() {
    ZcBCatalogue zcBCatalogue = (ZcBCatalogue) this.listCursor.getCurrentObject();
    Boolean flag = false;
    StringBuffer str = new StringBuffer("请填写:\n");
    // if (zcBCatalogue.getZcDiyuDaima() == null ||
    // "".equals(zcBCatalogue.getZcDiyuDaima().trim())) {
    // str.append("地域代码\n");
    // flag = true;
    // }
    // if (zcBCatalogue.getZcDiyuName() == null ||
    // "".equals(zcBCatalogue.getZcDiyuName().trim())) {
    // str.append("地域名称\n");
    // flag = true;
    // }
    if (zcBCatalogue.getZcCatalogueCode() == null || "".equals(zcBCatalogue.getZcCatalogueCode().trim())) {
      str.append("品目代码\n");
      flag = true;
    }
    if (zcBCatalogue.getZcCatalogueName() == null || "".equals(zcBCatalogue.getZcCatalogueName().trim())) {
      str.append("品目名称\n");
      flag = true;
    }
    if (zcBCatalogue.getZcYear() == null || "".equals(zcBCatalogue.getZcYear().trim())) {
      str.append("财政年度\n");
      flag = true;
    }
    if (zcBCatalogue.getZcCatalogueType() == null || "".equals(zcBCatalogue.getZcCatalogueType().trim())) {
      str.append("采购分类\n");
      flag = true;
    }
    // if (zcBCatalogue.getZcCatalogueCodePar() == null
    // || "".equals(zcBCatalogue.getZcCatalogueCodePar().trim())) {
    // str.append("上级品目代码\n");
    // flag = true;
    // }
    if (zcBCatalogue.getZcIsVital() == null || "".equals(zcBCatalogue.getZcIsVital().trim())) {
      str.append("是否重要品目\n");
      flag = true;
    }
    if (zcBCatalogue.getZcIsUsed() == null || "".equals(zcBCatalogue.getZcIsUsed().trim())) {
      str.append("是否使用\n");
      flag = true;
    }
   /* if (zcBCatalogue.getZcIsdianZitouBiao() == null || "".equals(zcBCatalogue.getZcIsdianZitouBiao().trim())) {
      str.append("是否电子投标\n");
      flag = true;
    }*/
    if (flag) {
      JOptionPane.showMessageDialog(this, str.toString(), "提示", JOptionPane.INFORMATION_MESSAGE);
      return false;
    } else {
      return true;
    }
  }

  public boolean doSave() {
    // if (!isDataChanged()) {
    // JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示",
    // JOptionPane.INFORMATION_MESSAGE);
    // return true;
    // }

    if (!this.checkBeforeSave()) {
      return true;
    }

    boolean success = true;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(saveButton.getFuncId());
      ZcBCatalogue zcBCatalogue = (ZcBCatalogue) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
      this.listPanel.catalogueServiceDelegate.doSave(zcBCatalogue, requestMeta);
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
      String errorInfo = "";
      try {
        requestMeta.setFuncId(deleteButton.getFuncId());
        ZcBCatalogue zcBCatalogue = (ZcBCatalogue) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
        this.listPanel.catalogueServiceDelegate.doDelete(zcBCatalogue, requestMeta);
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
    ZcBCatalogue zcBCatalogue = (ZcBCatalogue) listCursor.getCurrentObject();
    if (zcBCatalogue != null) {
      List agencylist = null;
      try {
        this.merDto = new ElementConditionDto();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } else {
      pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
      zcBCatalogue = new ZcBCatalogue();
      Calendar c = Calendar.getInstance();// 获得系统当前日期
      String year = String.valueOf(c.get(Calendar.YEAR));
      zcBCatalogue.setZcYear(year);
      listCursor.getDataList().add(zcBCatalogue);
      listCursor.setCurrentObject(zcBCatalogue);
    }
    // setTableapdEditor(biTablePanel.getTable());
    this.setEditingObject(zcBCatalogue);
    setOldObject();
    this.fitTable();
    biTabPane.repaint();
  }

  public void setOldObject() {
    oldZcBAgency = (ZcBCatalogue) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  class TreeHandler implements IForeignEntityTreeHandler {

    @Override
    public void excute(List selectedDatas) {
      // TCJLODO Auto-generated method stub
      ZcBCatalogue catalogue = (ZcBCatalogue) listCursor.getCurrentObject();
      for (Object obj : selectedDatas) {
        TreeNodeValueObject td = (TreeNodeValueObject) obj;
        catalogue.setZcCatalogueCodePar(td.getCode());
        catalogue.setZcCatalogueNamePar(td.getName());
        System.out.println(td.getCode() + "aaaaaaa" + "\t" + td.getName());
        setEditingObject(catalogue);
      }
    }

    @Override
    public boolean isMultipleSelect() {
      // TCJLODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean isSelectLeaf() {
      // TCJLODO Auto-generated method stub

      return false;
    }

    public boolean beforeSelect(ElementConditionDto dto) {
      ZcBCatalogue catalogue = (ZcBCatalogue) listCursor.getCurrentObject();
      if (catalogue.getZcYear() == null) {
        JOptionPane.showMessageDialog(self, "请选择财政年度！", "错误", JOptionPane.ERROR_MESSAGE);
        // }
      }
      return true;
    }
  }

  public class CatalogueFnHandler implements IForeignEntityHandler {

    public String columNames[];

    public CatalogueFnHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {
      ZcBCatalogue catalogue = (ZcBCatalogue) listCursor.getCurrentObject();
      for (Object object : selectedDatas) {
        ZcBCatalogue zcBAgency = (ZcBCatalogue) object;
        catalogue.setZcDiyuDaima(zcBAgency.getZcDiyuDaima());
        catalogue.setZcDiyuName(zcBAgency.getZcDiyuName());
        setEditingObject(catalogue);
      }
    }

    @Override
    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        ZcBCatalogue rowData = (ZcBCatalogue) showDatas.get(i);
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

  // 字段是否可编辑
  public void fileEditStatus() {

  }

}
