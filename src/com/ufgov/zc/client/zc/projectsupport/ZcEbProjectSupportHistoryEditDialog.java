package com.ufgov.zc.client.zc.projectsupport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbProjSupportToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.PrintButton;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.projectsupport.operation.ZcEbProjectSupportOpt;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.zc.model.ZcEbProjBidHistory;

public class ZcEbProjectSupportHistoryEditDialog extends GkBaseDialog {

  private ZcEbProjectSupportHistoryEditPanel editPanel = null;

  private GkBaseDialog self = this;

  public ZcEbProjectSupportHistoryEditDialog(ZcEbProjectSupportListPanel listPanel, List beanList, int editingRow, String tabStatus) {

    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);

    editPanel = new ZcEbProjectSupportHistoryEditPanel(self, new ListCursor(beanList, editingRow), tabStatus, listPanel);

    setLayout(new BorderLayout());

    add(editPanel);

    this.setTitle("投标历史记录表");

    this.setSize(UIConstants.DIALOG_2_LEVEL_WIDTH, UIConstants.DIALOG_2_LEVEL_HEIGHT);

    this.moveToScreenCenter();

    this.setVisible(true);
  }

  public void closeDialog() {

    this.editPanel.doExit();

  }

  class ZcEbProjectSupportHistoryEditPanel extends AbstractMainSubEditPanel {

    private ListCursor listCursor = null;

    private GkBaseDialog parent = null;

    private String tabStatus;

    private ZcEbProjectSupportListPanel listPanel;

    private FuncButton printHButton = new PrintButton();

    private FuncButton exitButton = new ExitButton();

    private JTabbedPane supplierTab;

    private JTablePanel supplierTable = new JTablePanel();

    private ZcEbProjectSupportOpt opt = new ZcEbProjectSupportOpt();

    private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

    public ZcEbProjectSupportHistoryEditPanel() {

    }

    public ZcEbProjectSupportHistoryEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbProjectSupportListPanel listPanel) {
      this.listCursor = listCursor;
      this.tabStatus = tabStatus;
      this.listPanel = listPanel;
      this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "供应商投标信息一览表", TitledBorder.CENTER,
        TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
      this.colCount = 3;
      this.parent = parent;
      requestMeta = listPanel.getRequestMeta();
      init();
      refreshData();
    }

    //
    //    public void init() {
    //
    //      this.initToolBar(toolBar);
    //
    //      this.setLayout(new BorderLayout());
    //
    //      this.add(toolBar, BorderLayout.NORTH);
    //      this.add(workPanel, BorderLayout.CENTER);
    //      JComponent tabTable = createSubBillPanel();
    //      workPanel.setLayout(new BorderLayout());
    //      workPanel.add(tabTable, BorderLayout.CENTER);
    //      workPanel.repaint();
    //    }

    private void refreshData() {
      ZcEbProjBidHistory proj = (ZcEbProjBidHistory) listCursor.getCurrentObject();

      refreshSubTableData(proj);
    }

    private void refreshSubTableData(ZcEbProjBidHistory proj) {
      Map<String, String> map = new HashMap<String, String>();
      map.put("proj_code", proj.getProjCode());
      map.put("pack_code", proj.getPackCode());
      SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
      map.put("sell_end_time", sd.format(proj.getSellEndTime()));

      List projSupportList = listPanel.getOpt().getBaseService().queryDataForList("ZcEbEntrustBull.getSuBidHistoryInfo", map, requestMeta);

      supplierTable.setTableModel(ZcEbProjSupportToTableModelConverter.convertHistoryDetailToTableModel(projSupportList));

      ZcUtil.translateColName(this.supplierTable.getTable(), ZcEbProjSupportToTableModelConverter.getHisInfo());

      setTableItemEditor(supplierTable.getTable());

    }

    private void setTableItemEditor(JTable table) {
      table.setDefaultEditor(String.class, new TextCellEditor());
      SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_SIGNUP_DATE, new DateCellEditor());
      SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_SIGNUP_DATE, new DateCellRenderer());
    }

    @Override
    public void initToolBar(JFuncToolBar toolBar) {
      toolBar.setModuleCode("ZC");
      toolBar.setCompoId(listPanel.compoId);

      toolBar.add(printHButton);
      printHButton.setText("打印历史投标记录");
      printHButton.setToolTipText("打印历史投标记录");
      toolBar.add(exitButton);

      printHButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          doPrintH();
        }
      });

      exitButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          doExit();
        }
      });

    }

    @Override
    public List<AbstractFieldEditor> createFieldEditors() {

      List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();
      TextFieldEditor t1 = new TextFieldEditor("任务单号", "sn_code");
      TextFieldEditor t3 = new TextFieldEditor("项目编号", "proj_code");
      TextFieldEditor t2 = new TextFieldEditor("项目名称", "proj_name");
      TextFieldEditor t4 = new TextFieldEditor("分包编号", "pack_name");
      TextFieldEditor t5 = new TextFieldEditor("点击投标截止时间", "sell_end_time");
      TextFieldEditor t6 = new TextFieldEditor("开标时间", "open_bid_date");
      TextFieldEditor t7 = new TextFieldEditor("项目负责人", "attn_name");

      ZcEbProjBidHistory proj = (ZcEbProjBidHistory) listCursor.getCurrentObject();
      SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      t1.setValue(proj.getSnCode());
      t2.setValue(proj.getProjName());
      t3.setValue(proj.getPackCode());
      t4.setValue(proj.getPackName());
      t5.setValue(sd.format(proj.getSellEndTime()));
      t6.setValue(sd.format(proj.getOpenBidTime()));
      t7.setValue(proj.getAttnName());

      t1.setEnabled(false);
      t2.setEnabled(false);
      t3.setEnabled(false);
      t4.setEnabled(false);
      t5.setEnabled(false);
      t6.setEnabled(false);
      t7.setEnabled(false);

      editorList.add(t1);
      editorList.add(t2);
      editorList.add(t3);
      editorList.add(t4);
      editorList.add(t5);
      editorList.add(t6);
      editorList.add(t7);

      return editorList;
    }

    @Override
    public JComponent createSubBillPanel() {

      supplierTab = new JTabbedPane();

      supplierTable.init();

      supplierTable.setTablePreferencesKey(this.getClass().getName() + "_table");

      supplierTable.getTable().setShowCheckedColumn(true);

      supplierTable.getSearchBar().setVisible(false);

      supplierTable.getTable().getTableRowHeader().setPreferredSize(new Dimension(50, 0));

      supplierTab.add("投标历史记录", supplierTable);

      return supplierTab;
    }

    public boolean doExit() {

      parent.dispose();

      return true;

    }

    public void doPrintH() {

      requestMeta.setFuncId(this.printHButton.getFuncId());

      requestMeta.setPageType(ZcEbProjectSupportListPanel.compoId + "_L");

      try {

        ZcEbProjBidHistory proj = (ZcEbProjBidHistory) listCursor.getCurrentObject();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        String selDate = df.format(proj.getSellEndTime());

        String condition = "PROJ_CODE='" + proj.getProjCode() + "' and to_char(SELL_END_TIME,'yyyymmddhh24miss')='" + selDate + "' and pack_name='"
          + proj.getPackName() + "'";

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        JOptionPane.showMessageDialog(this, "打印出错！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

      }
    }

  }

}
