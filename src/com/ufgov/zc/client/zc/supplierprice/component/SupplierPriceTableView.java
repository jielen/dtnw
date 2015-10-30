package com.ufgov.zc.client.zc.supplierprice.component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.supplierprice.model.AbstractBeanTableModel;
import com.ufgov.zc.client.zc.supplierprice.model.SupplierPriceTableModel;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.util.NumberUtil;
import com.ufgov.zc.common.zc.model.SupplierPriceDetail;
import com.ufgov.zc.common.zc.model.ZcEbSignupPackDetail;
import com.ufgov.zc.common.zc.publish.ISupplierPriceDelegate;

public class SupplierPriceTableView extends JPanel implements ActionListener, CellEditorListener {
  private ISupplierPriceDelegate priceDelegeate = (ISupplierPriceDelegate) ServiceFactory.create(ISupplierPriceDelegate.class,
    "supplierPriceDelegate");

  private RequestMeta meta = WorkEnv.getInstance().getRequestMeta();

  private ZcEbSignupPackDetail _packDetail;

  private JGroupableTable _table;

  private AbstractBeanTableModel _tableModel;

  private JButton addBtn;

  private JButton delBtn;

  private JLabel sumInfoLabel = new JLabel();

  public SupplierPriceTableView() {
    super();
    this.setLayout(new BorderLayout());
    this.add(initTable(), BorderLayout.CENTER);
    this.add(initControl(), BorderLayout.SOUTH);
    this.setEditable(false);
  }

  public void setSignPackDetail(ZcEbSignupPackDetail packDetail) {
    SupplierPriceDetail priceDetail = new SupplierPriceDetail();
    priceDetail.setSingupPackId(packDetail.getSignupPackId());
    packDetail.setSupplierPriceDetail(priceDelegeate.selectPriceDetail(priceDetail, meta));
    _tableModel.setRecords(packDetail.getSupplierPriceDetail());
    _packDetail = packDetail;
    setEditor();
    setMoneyInfo();
  }

  public void setEditable(boolean editable) {
    _tableModel.setUpdateSignal(editable);
    addBtn.setEnabled(editable);
    delBtn.setEnabled(editable);
  }

  private void setEditor() {
    // _table.setDefaultEditor(Integer.class, new IntCellEditor());
    // _table.setDefaultEditor(BigDecimal.class, new MoneyCellEditor());
    _table.setRowHeight(20);
    _table.getColumn("prodNum").setCellEditor(new MoneyCellEditor());
    _table.getColumn("prodPrice").setCellEditor(new MoneyCellEditor());
    _table.getColumn("prodNum").getCellEditor().addCellEditorListener(this);
    _table.getColumn("prodPrice").getCellEditor().addCellEditorListener(this);
    _table.getColumn("procureType").setCellEditor(new ZcBCatalogueCellEditor());
    _table.getColumn("procureType").setCellRenderer(new ZcBCatalogueCellRenderer());
    _table.getColumn("procureSize").setCellRenderer(new AsValCellRenderer("ZC_SUPPLIER_SIZE"));
    _table.getColumn("procureSize").setCellEditor(new AsValComboBoxCellEditor("ZC_SUPPLIER_SIZE"));

    _table.getColumn("gongHuoDate").setCellEditor(new DateCellEditor());
    _table.getColumn("gongHuoDate").setCellRenderer(new DateCellRenderer());

    AsValCellRenderer ynCellRender = new AsValCellRenderer("ZC_VS_YN");
    _table.getColumn("isEnergySaving").setCellRenderer(ynCellRender);
    _table.getColumn("isWarterSaving").setCellRenderer(ynCellRender);
    _table.getColumn("isEnvironmentProtection").setCellRenderer(ynCellRender);
    _table.getColumn("isInnovative").setCellRenderer(ynCellRender);
    AsValComboBoxCellEditor ynCellEditor = new AsValComboBoxCellEditor("ZC_VS_YN");
    _table.getColumn("isEnergySaving").setCellEditor(ynCellEditor);
    _table.getColumn("isWarterSaving").setCellEditor(ynCellEditor);
    _table.getColumn("isEnvironmentProtection").setCellEditor(ynCellEditor);
    _table.getColumn("isInnovative").setCellEditor(ynCellEditor);

    _table.getColumn("prodMainParam").setPreferredWidth(200);
    _table.getColumn("prodAuxiliaryParam").setPreferredWidth(200);
    _table.getColumn("prodAuxiliaryParam").setPreferredWidth(200);
    _table.getColumn("statisticsIndicator").setPreferredWidth(200);
    _table.getColumn("prodDeviation").setPreferredWidth(200);
    _table.getColumn("procureSize").setPreferredWidth(200);
  }

  private JComponent initTable() {
    try {
      _table = SwingUtil.createTable(JPageableFixedTable.class);
      _table.setInstantEdit(true);
      _table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      _tableModel = new SupplierPriceTableModel();
      _tableModel.linkTable(_table);
      return new JScrollPane(_table);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private JComponent initControl() {
    JPanel p = new JPanel();
    p.setLayout(new FlowLayout(FlowLayout.RIGHT));
    addBtn = new AddButton();
    addBtn.setActionCommand("add");
    addBtn.addActionListener(this);
    p.add(addBtn);
    delBtn = new DeleteButton();
    delBtn.setActionCommand("del");
    delBtn.addActionListener(this);
    p.add(delBtn);

    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BorderLayout());
    infoPanel.add(sumInfoLabel, BorderLayout.WEST);
    JPanel parent = new JPanel();
    parent.setLayout(new BorderLayout());
    parent.add(p, BorderLayout.EAST);
    parent.add(infoPanel, BorderLayout.WEST);
    return parent;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    if (command.equals("add")) {
      SupplierPriceDetail priceDetail = (SupplierPriceDetail) _tableModel.appendRecord();
      priceDetail.setSingupPackId(_packDetail.getSignupPackId());
    } else if (command.equals("del")) {
      Integer[] indexs = _table.getCheckedRows();
      List selectobj = new ArrayList();
      for (int i = 0; i < indexs.length; i++) {
        int index = _table.convertRowIndexToModel(indexs[i]);
        selectobj.add(_tableModel.getRecord(index));
      }
      _tableModel.removeRecords(selectobj);
    }
    setMoneyInfo();
  }

  public String check() {
    setUnEdit();
    List<SupplierPriceDetail> records = _tableModel.getRecords();
    if (records == null || records.size() == 0) {
      if ("1".equals(_packDetail.getSpStatus())) {
        return "分包：\n " + _packDetail.getPackName() + "无报价信息\n";
      } else {
        return "";
      }
    }
    SupplierPriceDetail detail = null;
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < records.size(); i++) {
      detail = (SupplierPriceDetail) records.get(i);
      if (detail.getProdName() == null || "".equals(detail.getProdName())) {
        sb.append("第 " + (i + 1) + " 行明细" + "货物名称为空\n");
      }
      if (detail.getProcureType() == null || "".equals(detail.getProcureType())) {
        sb.append("第 " + (i + 1) + " 行明细" + "采购类别为空\n");
      }
      if (detail.getProdPrice() == null || detail.getProdPrice().compareTo(BigDecimal.ZERO) == 0) {
        sb.append("第 " + (i + 1) + " 行明细" + "货物单价为空\n");
      }
      if (detail.getProdNum() == null || detail.getProdNum().compareTo(BigDecimal.ZERO) == 0) {
        sb.append("第 " + (i + 1) + " 行明细" + "货物数量为空\n");
      }
    }
    if (sb.length() > 0) {
      sb.insert(0, "包 " + _packDetail.getPackName() + ":\n");
    }
    return sb.toString();
  }

  public void setUnEdit() {
    TableCellEditor editor = _table.getCellEditor();
    if (editor != null) {
      editor.stopCellEditing();
    }
  }

  private void setMoneyInfo() {
    BigDecimal money = getTotalMoney();
    String info = "<html><b><font color='red' size='4'>投标总价: 人民币(大写): </font></b>" + "<b><font color='red' size='4'>"
      + NumberUtil.moneyToUpper(money.doubleValue())
      + "</font></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font color='red' size='4'>小写: </font></b>"
      + "<font color='red' size='4'>" + money + "</font></html>";
    sumInfoLabel.setText(info);
    _packDetail.setBidSum(money.toString());
  }

  private BigDecimal getTotalMoney() {
    BigDecimal totalMoney = new BigDecimal(0);
    List<SupplierPriceDetail> records = _tableModel.getRecords();
    for (SupplierPriceDetail detail : records) {
      totalMoney = totalMoney.add(detail.getProdSumPrice());
    }
    return totalMoney;
  }

  @Override
  public void editingStopped(ChangeEvent e) {
    setMoneyInfo();
  }

  @Override
  public void editingCanceled(ChangeEvent e) {

  }
}
