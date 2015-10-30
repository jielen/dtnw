package com.ufgov.zc.client.zc.projectsupport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;

import jxl.Workbook;
import jxl.write.Alignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbProjSupportToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.ExportButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.projectsupport.operation.ZcEbProjectSupportOpt;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcEbProjSupport;

public class ZcEbProjectSupportEditDialog extends GkBaseDialog {

  private ZcEbProjectSupportEditPanel editPanel = null;

  private GkBaseDialog self = this;

  public ZcEbProjectSupportEditDialog(ZcEbProjectSupportListPanel listPanel, List beanList, int editingRow, String tabStatus) {

    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);

    editPanel = new ZcEbProjectSupportEditPanel(self, new ListCursor(beanList, editingRow), tabStatus, listPanel);

    setLayout(new BorderLayout());

    add(editPanel);

    this.setTitle("供应商投标信息一览表");

    this.setSize(UIConstants.DIALOG_2_LEVEL_WIDTH, UIConstants.DIALOG_2_LEVEL_HEIGHT);

    this.moveToScreenCenter();

    this.setVisible(true);
  }

  public void closeDialog() {

    this.editPanel.doExit();

  }

  class ZcEbProjectSupportEditPanel extends AbstractMainSubEditPanel {

    private ListCursor listCursor = null;

    private GkBaseDialog parent = null;

    private String tabStatus;

    private String sqlIdSupplier = "ZcEbEntrustBull.getZcEbProjSupportSupList";

    private ZcEbProjectSupportListPanel listPanel;

    private FuncButton saveButton = new SaveButton();

    private FuncButton exitButton = new ExitButton();

    public ExportButton exportButton = new ExportButton();

    private JTabbedPane supplierTab;

    private JTablePanel supplierTable = new JTablePanel();

    private ZcEbProjectSupportOpt opt = new ZcEbProjectSupportOpt();

    private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

    private List<ZcEbProjSupport> projSupportList = new ArrayList<ZcEbProjSupport>();

    public ZcEbProjectSupportEditPanel() {

    }

    public ZcEbProjectSupportEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbProjectSupportListPanel listPanel) {
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

    public void init() {

      this.initToolBar(toolBar);

      this.setLayout(new BorderLayout());

      this.add(toolBar, BorderLayout.NORTH);
      this.add(workPanel, BorderLayout.CENTER);
      JComponent tabTable = createSubBillPanel();
      workPanel.setLayout(new BorderLayout());
      workPanel.add(tabTable, BorderLayout.CENTER);
      workPanel.repaint();
    }

    private void refreshData() {
      ZcEbProjSupport proj = (ZcEbProjSupport) listCursor.getCurrentObject();

      refreshSubTableData(proj);
    }

    private List fetchDataSupplier(ZcEbProjSupport proj) {

      ElementConditionDto dto = new ElementConditionDto();

      dto.setProjCode(proj.getProjCode());

      dto.setOpenBidTime(proj.getOpenBidTime());

      return opt.getBaseService().queryDataForList(sqlIdSupplier, dto, requestMeta);

    }

    private void refreshSubTableData(ZcEbProjSupport proj) {

      projSupportList = fetchDataSupplier(proj);

      supplierTable.setTableModel(ZcEbProjSupportToTableModelConverter.convertDetailToTableModel(projSupportList));

      ZcUtil.translateColName(this.supplierTable.getTable(), ZcEbProjSupportToTableModelConverter.getItemInfo());

      setTableItemEditor(supplierTable.getTable());

    }

    private void setTableItemEditor(JTable table) {
      table.setDefaultEditor(String.class, new TextCellEditor());
      SwingUtil.setTableCellEditor(table, "IS_SITE", new AsValComboBoxCellEditor("VS_Y/N"));
      SwingUtil.setTableCellRenderer(table, "IS_SITE", new AsValCellRenderer("VS_Y/N"));
      SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_SIGNUP_DATE, new DateCellEditor());
      SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_SIGNUP_DATE, new DateCellRenderer());
    }

    @Override
    public void initToolBar(JFuncToolBar toolBar) {
      toolBar.setModuleCode("ZC");
      toolBar.setCompoId(listPanel.compoId);
      toolBar.add(saveButton);
      toolBar.add(exportButton);
      toolBar.add(exitButton);
      saveButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

          doSave();

        }
      });

      exitButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

          doExit();

        }
      });
      exportButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

          doExport();

        }
      });

    }

    @Override
    public List<AbstractFieldEditor> createFieldEditors() {

      List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

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

      supplierTab.add("投标供应商", supplierTable);

      //添加一个维护供应商

      return supplierTab;
    }

    public boolean doExit() {

      parent.dispose();

      return true;

    }

    private void doSave() {
      boolean success = true;
      String errorInfo = "";
      try {
        opt.bulletinServiceDelegate.updateSupplierIsSite(projSupportList, requestMeta);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        errorInfo += e.getMessage();
        success = false;
      }
      if (success) {
        JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(this, "保存失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
      }
    }

    private void doExport() {
      ZcEbProjSupport proj = (ZcEbProjSupport) listCursor.getCurrentObject();

      try {

        FileSystemView fsv = FileSystemView.getFileSystemView();

        File file = fsv.getHomeDirectory();

        String filePath = file.getAbsolutePath();

        Calendar cale = Calendar.getInstance();

        String sysDate = cale.get(Calendar.YEAR) + "年" + cale.get(Calendar.MONTH) + "月"

        + cale.get(Calendar.DATE) + "日";

        String fileName = filePath + "\\供应商投标信息-" + sysDate + ".xls";

        WritableWorkbook book = Workbook.createWorkbook(new File(fileName));
        book.setProtected(true);

        WritableSheet sheet = book.createSheet("供应商投标信息", 0);

        //在Label对象的构造函数中指名是第一行第一列

        jxl.write.WritableCellFormat txtFmt = new jxl.write.WritableCellFormat();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        txtFmt.setBorder(Border.ALL, BorderLineStyle.THIN);

        txtFmt.setAlignment(Alignment.LEFT);

        //titleFmt.setAlignment(Alignment.CENTRE);

        WritableFont fontFmt1 = new WritableFont(WritableFont.createFont("宋体"), 13, WritableFont.BOLD, false);

        WritableFont fontFmt = new WritableFont(WritableFont.createFont("宋体"), 20, WritableFont.BOLD, false);

        WritableCellFormat font = new WritableCellFormat(fontFmt);

        WritableCellFormat font1 = new WritableCellFormat(fontFmt1);

        font.setAlignment(Alignment.CENTRE);
        font1.setBorder(Border.ALL, BorderLineStyle.THIN);
        //标题

        sheet.mergeCells(0, 0, 7, 0);//合并单元格标题行

        sheet.mergeCells(1, 1, 3, 1);//合并单元格标题行

        Label title = new Label(0, 0, "供应商投标信息一览表", font);
        Label projName = new Label(0, 1, "项目名称：", font1);

        Label projNameTitle = new Label(1, 1, proj.getProjName(), font1);

        int titleRow = 2;//定义列标题所在的行。
        int dataRow = 3;//定义数据列开始的行号

        Label title0 = new Label(0, titleRow, "投标供应商", font1);

        Label title1 = new Label(1, titleRow, "确认投标时间", font1);

        Label title2 = new Label(2, titleRow, "确认投标包号", font1);

        Label title3 = new Label(3, titleRow, "是否到现场", font1);

        Label title4 = new Label(4, titleRow, "联系人", font1);

        Label title5 = new Label(5, titleRow, "联系电话", font1);

        Label title6 = new Label(6, titleRow, "手机", font1);

        Label title7 = new Label(7, titleRow, "地址", font1);

        sheet.addCell(title);

        sheet.addCell(projName);

        sheet.addCell(projNameTitle);

        sheet.addCell(title0);

        sheet.addCell(title1);

        sheet.addCell(title2);

        sheet.addCell(title3);

        sheet.addCell(title4);

        sheet.addCell(title5);

        sheet.addCell(title6);

        sheet.addCell(title7);

        //添加数据行

        for (int i = 0; i < projSupportList.size(); i++) {

          ZcEbProjSupport bean = projSupportList.get(i);

          Label label0 = new Label(0, i + dataRow, bean.getZcEbSupplier().getName(), txtFmt);

          Label label1 = new Label(1, i + dataRow, sdf.format(bean.getSignupDate()), txtFmt);

          Label label2 = new Label(2, i + dataRow, bean.getPackName(), txtFmt);

          Label label3 = new Label(3, i + dataRow, "Y".equals(bean.getIsSite()) ? "是" : "否", txtFmt);

          Label label4 = new Label(4, i + dataRow, bean.getZcEbSupplier().getLinkMan(), txtFmt);

          Label label5 = new Label(5, i + dataRow, bean.getZcEbSupplier().getLinkManPhone(), txtFmt);

          Label label6 = new Label(6, i + dataRow, bean.getZcEbSupplier().getLinkManMobile(), txtFmt);

          Label label7 = new Label(7, i + dataRow, bean.getZcEbSupplier().getAddress(), txtFmt);

          sheet.addCell(label0);

          sheet.addCell(label1);

          sheet.addCell(label2);

          sheet.addCell(label3);

          sheet.addCell(label4);

          sheet.addCell(label5);

          sheet.addCell(label6);

          sheet.addCell(label7);

        }

        for (int i = 0; i < sheet.getRows(); i++) {
          sheet.setRowView(i, 500);
        }
        for (int i = 0; i < sheet.getColumns(); i++) {
          sheet.setColumnView(i, 28);
        }

        book.write();

        book.close();

        JOptionPane.showMessageDialog(this, "【供应商投标情况一览表】已导出至桌面,导出成功！");

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        JOptionPane.showMessageDialog(this, "导出【供应商投标情况一览表】出错！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

      }

    }
  }

}
