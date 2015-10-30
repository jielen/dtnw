package com.ufgov.zc.client.zc.agencyaptd;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbAgencyToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.ui.conditionitem.AsValComboboxConditionItem2;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.model.AsVal;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.checkrule.BaseRule;
import com.ufgov.zc.common.zc.checkrule.ZcMakeCheckRule;
import com.ufgov.zc.common.zc.model.ZcBAgency;
import com.ufgov.zc.common.zc.model.ZcBAgencyListAptd;
import com.ufgov.zc.common.zc.model.ZcPProMake;

/**
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class ZcEbAgencyAptEditPanel extends AbstractMainSubEditPanel {

  public static final long serialVersionUID = -2779110682087878491L;

  public static final Logger logger = Logger.getLogger(ZcEbAgencyAptEditPanel.class);

  public RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  public FuncButton addButton = new AddButton();

  public FuncButton previousButton = new PreviousButton();

  public FuncButton saveButton = new SaveButton();

  public FuncButton deleteButton = new DeleteButton();

  public FuncButton nextButton = new NextButton();

  public FuncButton exitButton = new ExitButton();

  protected ListCursor listCursor;

  public ZcBAgencyListAptd oldZcBAgency;

  public String tabStatus;

  public ZcEbAgencyAptdListPanel listPanel;

  public JTablePanel biTablePanel = new JTablePanel();

  public ZcEbAgencyAptEditPanel self = this;

  public GkBaseDialog parent;

  JTabbedPane biTabPane = null;

  public ElementConditionDto merDto;

  JFuncToolBar bottomToolBar1 = null;

  public String getTitle() {
    return LangTransMeta.translate("资质等级登记");
  }

  public AsVal asVal = new AsVal();

  public boolean haveInitFlag = false;

  public static  String getCompoId() {
    return "ZC_B_AGENCY_APD";
  }

  public ZcEbAgencyAptEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbAgencyAptdListPanel listPanel) {
    super(ZcBAgency.class, BillElementMeta.getBillElementMetaWithoutNd("ZC_B_AGENCY_APD"));
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
    this.haveInitFlag = true;
  }

  public ZcEbAgencyAptEditPanel(Class<ZcPProMake> class1, BillElementMeta billElementMetaWithoutNd) {
    super(ZcBAgency.class, billElementMetaWithoutNd);
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
    ZcBAgencyListAptd zcBAgency = (ZcBAgencyListAptd) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
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

    bottomToolBar1 = new JFuncToolBar();
    FuncButton addBtn1 = new SubaddButton(false);
    JButton insertBtn1 = new SubinsertButton(false);
    JButton delBtn1 = new SubdelButton(false);
    bottomToolBar1.add(addBtn1);
    bottomToolBar1.add(insertBtn1);
    bottomToolBar1.add(delBtn1);
    //    biTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);

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

    return biTablePanel;
  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(getCompoId());
    toolBar.add(addButton);
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
    ZcBAgencyListAptd zcBAgency = (ZcBAgencyListAptd) this.listCursor.getCurrentObject();
    Boolean flag = false;
    StringBuffer str = new StringBuffer("请填写:\n");
    if (zcBAgency.getZcAptdCode() == null || "".equals(zcBAgency.getZcAptdCode().trim())) {
      str.append("资质代码\n");
      flag = true;
    }
    if (zcBAgency.getZcAptdName() == null || "".equals(zcBAgency.getZcAptdName().trim())) {
      str.append("资质名称\n");
      flag = true;
    }
    //    if (zcBAgency.getZcDiyuDaima() == null || "".equals(zcBAgency.getZcDiyuDaima().trim())) {
    //      str.append("地域代码\n");
    //      flag = true;
    //    }
    //    if (zcBAgency.getZcDiyuName() == null || "".equals(zcBAgency.getZcDiyuName().trim())) {
    //      str.append("地域名称\n");
    //      flag = true;
    //    }
    if (flag) {
      JOptionPane.showMessageDialog(this, str.toString(), "提示", JOptionPane.INFORMATION_MESSAGE);
      return false;
    } else {
      return true;
    }
  }

  public boolean doSave() {
    if (!isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return true;
    }

    if (!this.checkBeforeSave()) {
      return true;
    }

    boolean success = true;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(saveButton.getFuncId());
      ZcBAgencyListAptd zcBAgencyAptd = (ZcBAgencyListAptd) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
      int i = 1;
      this.listPanel.agencyServiceDelegate.doSaveAptd(zcBAgencyAptd, requestMeta);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
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
      ZcBAgencyListAptd zcBAgency = null;
      String errorInfo = "";
      try {
        requestMeta.setFuncId(deleteButton.getFuncId());
        zcBAgency = (ZcBAgencyListAptd) this.listCursor.getCurrentObject();
        //关联代理机构的记录不能删除
        ElementConditionDto dto = new ElementConditionDto();
        dto.setZcText1(zcBAgency.getZcAptdCode());
        List list = this.listPanel.agencyServiceDelegate.getZcZcBAgencyAptdList(dto, requestMeta);
        if (list != null && list.size() > 0) {
          JOptionPane.showMessageDialog(this, "不能删除与代理机构关联 的优质信息！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
          return;
        }

        this.listPanel.agencyServiceDelegate.doDeleteApds(zcBAgency, requestMeta);
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
    ZcBAgencyListAptd zcBAgency = (ZcBAgencyListAptd) listCursor.getCurrentObject();
    if (zcBAgency != null) {
      List agencylist = null;
      try {
        this.merDto = new ElementConditionDto();
        merDto.setZcText0(zcBAgency.getZcAgeyCode());
        agencylist = this.listPanel.agencyServiceDelegate.getZcZcBAgencyAptdList(merDto, requestMeta);
        if (agencylist != null) {
          //          zcBAgency.setZcBAgencAptdList(agencylist);
        }
        listCursor.setCurrentObject(zcBAgency);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } else {
      zcBAgency = new ZcBAgencyListAptd();
      //      zcBAgency.setZcBAgencAptdList(new ArrayList());
      // 新增数据默认插入一行
      ZcBAgencyListAptd zcBAgencAptd = new ZcBAgencyListAptd();
      setItemBiDefaultValue(zcBAgencAptd);
      //      zcBAgency.getZcBAgencAptdList().add(zcBAgencAptd);
      listCursor.getDataList().add(zcBAgency);
      listCursor.setCurrentObject(zcBAgency);
    }
    List<ZcBAgencyListAptd> list = new ArrayList();
    list.add(zcBAgency);
    biTablePanel.setTableModel(ZcEbAgencyToTableModelConverter.convertSubBiTableData(list, wfCanEditFieldMap));
    // 翻译从表表头列
    ZcUtil.translateColName(biTablePanel.getTable(), ZcEbAgencyToTableModelConverter.biInfo);
    setTableapdEditor(biTablePanel.getTable());
    this.setEditingObject(zcBAgency);
    setOldObject();
    this.fitTable();
    biTabPane.repaint();
  }

  public void setOldObject() {
    oldZcBAgency = (ZcBAgencyListAptd) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  public void setTableapdEditor(JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
  }

}
