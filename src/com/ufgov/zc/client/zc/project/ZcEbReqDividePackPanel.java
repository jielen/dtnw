package com.ufgov.zc.client.zc.project;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.apache.commons.lang.ObjectUtils;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbProjectToTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.celleditor.IntCellEditor;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextFilePathCellEditor;
import com.ufgov.zc.client.component.table.celleditor.zc.ZcBCatalogueCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
import com.ufgov.zc.client.component.table.cellrenderer.LineWrapCellRenderer;
import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.ztb.ChangeChineseNumber;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.model.AsFile;
import com.ufgov.zc.common.system.util.DateUtil;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcEbEntrustDetail;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbPackQua;
import com.ufgov.zc.common.zc.model.ZcEbPackReq;
import com.ufgov.zc.common.zc.model.ZcEbRequirement;
import com.ufgov.zc.common.zc.model.ZcEbSupQualification;
import com.ufgov.zc.common.zc.publish.IZcEbRequirementServiceDelegate;

public class ZcEbReqDividePackPanel extends JPanel {
  private final JButton addBtnPack = new SubaddButton(false, "添加分包");

  private final JButton insertBtnPack = new SubinsertButton(false, "插入分包");

  private final JButton delBtnPack = new SubdelButton(false, "删除分包");

  private final JButton addBtnPackReq = new SubaddButton(false, "添加明细");

  private final JButton insertBtnPackReq = new SubinsertButton(false, "插入明细");

  private final JButton delBtnPackReq = new SubdelButton(false, "删除明细");

  private final ZcEbReqDividePackPanel self = this;

  private JTabbedPane tabPaneReq;

  private JTabbedPane tabPanePack;

  private final JTablePanel tablePanelPack = new JTablePanel("tablePanelPack");

  private final JTablePanel tablePanelPackReq = new JTablePanel("tablePanelPackReq");

//  private final JTablePanel tablePanelPackQua = new JTablePanel("tablePanelPackQua");

  private final ZcBCatalogueCellEditor zcBCatalogueCellEditor = new ZcBCatalogueCellEditor();

  JSaveableSplitPane splitPane;

  public ZcEbRequirement zcEbRequirement;

  private final BillElementMeta detailPackBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_EB_PROJ" + "_PACK");

  private final BillElementMeta detailPackReqBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_EB_PROJ" + "_PACKREQ");

  private final BillElementMeta detailPackQuaBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_EB_PROJ" + "_PACKQUA");

  private List oldPackList;

  private Thread initBCatalogueCellEditorThread;

  //增加包时的增量，每次“添加新包”按钮count++

  private final int count = 0;

  BigDecimal packBudgetSum = BigDecimal.ZERO;

  public IZcEbRequirementServiceDelegate zcEbRequirementServiceDelegate = (IZcEbRequirementServiceDelegate) ServiceFactory.create(
    IZcEbRequirementServiceDelegate.class, "zcEbRequirementServiceDelegate");

  private boolean isEnabled = false;

  /**
  * @param zcEbRequirement the zcEbRequirement to set
  */
  public void setZcEbRequirement(ZcEbRequirement zcEbRequirement) {
    this.zcEbRequirement = zcEbRequirement;
  }

  public ZcEbReqDividePackPanel() {
    initComponet();
  }

  private void initComponet() {
    tabPanePack = new JTabbedPane();
    tablePanelPack.init();
    tablePanelPack.getSearchBar().setVisible(false);
    tablePanelPack.getTable().setShowCheckedColumn(true);
    //    tablePanelPack.getTable().getTableHeader().setResizingAllowed(true);
    //    tablePanelPack.getTable().setAutoCreateRowSorter(false);
    //    tablePanelPack.getTable().setRowSorter(null);

    tablePanelPack.setTablePreferencesKey(this.getClass().getName() + "_detail_table");
    tabPanePack.addTab("分 包", null, tablePanelPack, LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PAKE_NAME));
    JFuncToolBar bottomToolBarPack = new JFuncToolBar();
    bottomToolBarPack.add(addBtnPack);
    //    bottomToolBarPack.add(insertBtnPack);
    bottomToolBarPack.add(delBtnPack);
    tablePanelPack.add(bottomToolBarPack, BorderLayout.SOUTH);
    addBtnPack.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 添加分包明细
        if (!checkSn()) {
          return;
        }
        ZcEbPack pack = new ZcEbPack();
        pack.setPackCode(ZcUtil.getSequenceNextVal(ZcSettingConstants.SEQUENCE_PACK));
        setPackDefaultValue(pack, ZcSettingConstants.PACK_STATUS_DRAFT);
        int rowNum = ZcUtil.addSub(tablePanelPack, pack);
        tablePanelPack.getTable().setRowSelectionInterval(rowNum, rowNum);
      }
    });
    insertBtnPack.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 插入分包明细
        if (!checkSn()) {
          return;
        }
        ZcEbPack pack = new ZcEbPack();
        pack.setPackCode(ZcUtil.getSequenceNextVal(ZcSettingConstants.SEQUENCE_PACK));
        setPackDefaultValue(pack, ZcSettingConstants.PACK_STATUS_DRAFT);
        int rowNum = ZcUtil.insertSub(tablePanelPack, pack);
        tablePanelPack.getTable().setRowSelectionInterval(rowNum, rowNum);
      }
    });
    // 删除项目标段按钮的事件监听
    delBtnPack.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        BeanTableModel tableModel = (BeanTableModel) tablePanelPack.getTable().getModel();
        if (tableModel.getRowCount() > 0) {
          tableModel.deleteRow(tableModel.getRowCount() - 1);

        }
      }
    });

    tabPaneReq = new JTabbedPane();
    tablePanelPackReq.init();
    tablePanelPackReq.getSearchBar().setVisible(false);
    tablePanelPackReq.setTablePreferencesKey(this.getClass().getName() + "_detailother_table");
    tabPaneReq.addTab("分包采购明细", null, tablePanelPackReq, "标段中包括的采购明细");
    JFuncToolBar bottomToolBarPackReq = new JFuncToolBar();
    bottomToolBarPackReq.add(addBtnPackReq);
    bottomToolBarPackReq.add(insertBtnPackReq);
    bottomToolBarPackReq.add(delBtnPackReq);
    tablePanelPackReq.add(bottomToolBarPackReq, BorderLayout.SOUTH);
    addBtnPackReq.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 插入标段采购明细
        ZcEbPackReq packReq = new ZcEbPackReq();
        JPageableFixedTable packTable = tablePanelPack.getTable();
        BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
        if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
          ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToModel(packTable.getSelectedRows()[0]));
          packReq.setPackCode(pack.getPackCode());
          packReq.getRequirementDetail().setSn(pack.getEntrustCode());
          packReq.setPackReqCode(Long.parseLong(ZcUtil.getSequenceNextVal(ZcSettingConstants.SEQUENCE_PACK_REQ)));
          packReq.setTempId(Guid.genID());
          Long code = System.currentTimeMillis();
          packReq.setDetailCode(code);
          packReq.getRequirementDetail().setDetailCode(code);
          ZcUtil.addSub(tablePanelPackReq, packReq);
        } else {
          JOptionPane.showMessageDialog(self, "请先选择一条【分包明细】", "提示", JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    insertBtnPackReq.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 插入标段采购明细
        ZcEbPackReq packReq = new ZcEbPackReq();
        JPageableFixedTable packTable = tablePanelPack.getTable();
        BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
        if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
          ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToModel(packTable.getSelectedRows()[0]));
          packReq.setPackCode(pack.getPackCode());
          packReq.getRequirementDetail().setSn(pack.getEntrustCode());
          packReq.setPackReqCode(Long.parseLong(ZcUtil.getSequenceNextVal(ZcSettingConstants.SEQUENCE_PACK_REQ)));
          packReq.setTempId(Guid.genID());
          Long code = System.currentTimeMillis();
          packReq.setDetailCode(code);
          packReq.getRequirementDetail().setDetailCode(code);
          ZcUtil.insertSub(tablePanelPackReq, packReq);
        } else {
          JOptionPane.showMessageDialog(self, "请先选择一条【分包明细】", "提示", JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    delBtnPackReq.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZcUtil.deleteSub(tablePanelPackReq, self);
        BeanTableModel model = (BeanTableModel) tablePanelPackReq.getTable().getModel();
        addbudget(model);
      }
    });
//    initQuaPane();

    splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, tabPanePack, tabPaneReq);
    splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 200);
    splitPane.setContinuousLayout(true);
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerSize(5);
    splitPane.setDividerLocation(200);
    this.setLayout(new BorderLayout());
    this.add(splitPane);

  }

//  private void initQuaPane() {
//    tablePanelPackQua.init();
//    tablePanelPackQua.getSearchBar().setVisible(false);
//    tablePanelPackQua.setTablePreferencesKey(this.getClass().getName() + "_detailother_table");
//    JFuncToolBar bottomToolBar = new JFuncToolBar();
//
//    JButton addBtn = new SubaddButton(false, "添加资质");
//
//    JButton insertBtn = new SubinsertButton(false, "插入资质");
//
//    JButton delBtn = new SubdelButton(false, "删除资质");
//    bottomToolBar.add(addBtn);
//    bottomToolBar.add(insertBtn);
//    bottomToolBar.add(delBtn);
//    tablePanelPackQua.add(bottomToolBar, BorderLayout.SOUTH);
//    addBtn.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        ZcEbPackQua packQua = new ZcEbPackQua();
//        JPageableFixedTable packTable = tablePanelPack.getTable();
//        BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
//        if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
//          ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToModel(packTable.getSelectedRows()[0]));
//          packQua.setPackCode(pack.getPackCode());
//          packQua.setPackName(pack.getPackName());
//          packQua.setTempId(Guid.genID());
//
//          ZcUtil.addSub(tablePanelPackQua, packQua);
//        } else {
//          JOptionPane.showMessageDialog(self, "请先选择一条【分包明细】", "提示", JOptionPane.WARNING_MESSAGE);
//        }
//      }
//    });
//    insertBtn.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        ZcEbPackQua packQua = new ZcEbPackQua();
//        JPageableFixedTable packTable = tablePanelPack.getTable();
//        BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
//        if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
//          ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToModel(packTable.getSelectedRows()[0]));
//          packQua.setPackCode(pack.getPackCode());
//          packQua.setTempId(Guid.genID());
//          packQua.setPackName(pack.getPackName());
//          ZcUtil.insertSub(tablePanelPackQua, packQua);
//        } else {
//          JOptionPane.showMessageDialog(self, "请先选择一条【分包明细】", "提示", JOptionPane.WARNING_MESSAGE);
//        }
//      }
//    });
//    delBtn.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        ZcUtil.deleteSub(tablePanelPackQua, self);
//      }
//    });
//  }
//
//  private void reCrtSubPanel() {
//    if ("8".equals(zcEbRequirement.getZcEbEntrust().getZcPifuCgfs())) {
//      if (tabPaneReq.getTabCount() == 1)
//        tabPaneReq.addTab("分包供应商资质", null, tablePanelPackQua, "标段中包括的供应商资质");
//    } else {
//      tabPaneReq.remove(tablePanelPackQua);
//    }
//  }

  public void stopEditing() {
    if (tablePanelPack.getTable().isEditing()) {
      tablePanelPack.getTable().getCellEditor().stopCellEditing();
    }
    if (tablePanelPackReq.getTable().isEditing()) {
      tablePanelPackReq.getTable().getCellEditor().stopCellEditing();
    }
//    if (tablePanelPackQua.getTable().isEditing()) {
//      tablePanelPackQua.getTable().getCellEditor().stopCellEditing();
//    }
  }

  public void refreshSubPackTablePanel(List packList) {
    if (packList.size() > 0) {
      this.tablePanelPack.setTableModel(convertPackToTableModel(packList));
      ZcEbPack pack = (ZcEbPack) packList.get(0);
      tablePanelPackReq.setTableModel(ZcEbProjectToTableModelConverter.convertPackReqToTableModel(pack.getRequirementDetailList(), true));
//      tablePanelPackQua.setTableModel(ZcEbProjectToTableModelConverter.convertPackQuaToTableModel(pack.getPackQua(), true));

    } else {
      ZcEbPack pack = new ZcEbPack();
      String pname = getNewPackName(tablePanelPack, true);
      pack.setPackName(pname);
      pack.setPackCode(ZcUtil.getSequenceNextVal(ZcSettingConstants.SEQUENCE_PACK));
      setPackDefaultValue(pack, ZcSettingConstants.PACK_STATUS_DRAFT);
      //packList.add(pack);
      this.tablePanelPack.setTableModel(convertPackToTableModel(packList));
      tablePanelPackReq.setTableModel(ZcEbProjectToTableModelConverter.convertPackReqToTableModel(new ArrayList(), true));
//      tablePanelPackQua.setTableModel(ZcEbProjectToTableModelConverter.convertPackQuaToTableModel(new ArrayList(), true));

    }
    ZcUtil.translateColName(tablePanelPack.getTable(), packTableColumnInfo);

    // 翻译标段需求明细表头列
    ZcUtil.translateColName(tablePanelPackReq.getTable(), ZcEbProjectToTableModelConverter.getPackReqTableColumnInfo());
//    ZcUtil.translateColName(tablePanelPackQua.getTable(), ZcEbProjectToTableModelConverter.getPackQuaTableColumnInfo());
    // 设置标段需求明细列类型
    setPackReqTableProperty(tablePanelPackReq.getTable());
//    setPackQuaTableProperty(tablePanelPackQua.getTable());

    addPackTableLisenter(tablePanelPack.getTable());
    setPackTableProperty(tablePanelPack.getTable());
//    reCrtSubPanel();
    setOldPackObject();
  }

  private void setPackDefaultValue(ZcEbPack pack, String status) {
    pack.setTempId(Guid.genID());
    pack.setStatus(status);
    pack.setIsShowBudget("Y");
    pack.setPackBudget(new BigDecimal("0.00"));
    pack.setBidSum(new BigDecimal("0.00"));
    pack.setBidDeposit(new BigDecimal("0.00"));
    pack.setEntrust(zcEbRequirement.getZcEbEntrust());
    pack.setEntrustCode(zcEbRequirement.getZcEbEntrust().getSn());
    pack.setCoCode(zcEbRequirement.getZcEbEntrust().getCoCode());
    pack.setPurType(zcEbRequirement.getZcEbEntrust().getZcPifuCgfs());
    pack.setAgency(zcEbRequirement.getZcEbEntrust().getAgency());
    pack.setAgency(zcEbRequirement.getZcEbEntrust().getAgencyName());

  }

  private void refreshSubPackReqTable(ZcEbPack pack) {
    tablePanelPackReq.setTableModel(ZcEbProjectToTableModelConverter.convertPackReqToTableModel(pack.getRequirementDetailList(), true));
    // 翻译标段需求明细表头列
    ZcUtil.translateColName(tablePanelPackReq.getTable(), ZcEbProjectToTableModelConverter.getPackReqTableColumnInfo());
    // 设置标段需求明细列类型
    setPackReqTableProperty(tablePanelPackReq.getTable());
    tablePanelPackReq.getTable().setEnabled(isEnabled);

//    tablePanelPackQua.setTableModel(ZcEbProjectToTableModelConverter.convertPackQuaToTableModel(pack.getPackQua(), true));
//    // 翻译标段需求明细表头列
//    ZcUtil.translateColName(tablePanelPackQua.getTable(), ZcEbProjectToTableModelConverter.getPackQuaTableColumnInfo());
//    // 设置标段需求明细列类型
//    setPackQuaTableProperty(tablePanelPackQua.getTable());
//    tablePanelPackQua.getTable().setEnabled(isEnabled);
  }

  private void setPackTableProperty(final JTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());
    final MoneyCellEditor packBudget = new MoneyCellEditor(false);
    SwingUtil.setTableCellEditor(table, "PACK_BUDGET", packBudget);
    SwingUtil.setTableCellRenderer(table, "PACK_BUDGET", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "BID_DEPOSIT", new MoneyCellEditor(false));
    SwingUtil.setTableCellRenderer(table, "BID_DEPOSIT", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "REQ_FILE_NAME", new FileCellEditor("reqFileName", "reqFileId"));
    SwingUtil.setTableCellEditor(table, "FORMULA_FILE_NAME", new FileCellEditor("formulaFileName", "formulaFileId"));
    SwingUtil.setTableCellEditor(table, "PACK_DESC", new TextFilePathCellEditor());

    packBudget.getField().addFocusListener(new FocusListener() {
      private String lastValue;

      @Override
      public void focusGained(FocusEvent event) {
        // TCJLODO Auto-generated method stub
        if (!event.isTemporary()) {
          lastValue = packBudget.getField().getText();
        }
      }

      @Override
      public void focusLost(FocusEvent event) {
        // TCJLODO Auto-generated method stub
        if (event.isTemporary()) {
          return;
        }
        JPageableFixedTable packTable = tablePanelPack.getTable();
        BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
        ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToView(packTable.getSelectedRow()));
      }
    });

  }

  private void setPackReqTableProperty(final JTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());
    final String columNames[] = { "明细需求名称", "品目代码", "品目名称", "要求到货日期", "基本规格要求", "数量", "单价", "单项预算金额", "付款条件", "服务承诺", "描述" };
    IForeignEntityHandler handler = new IForeignEntityHandler() {
      @Override
      public void excute(List selectedDatas) {
        BeanTableModel model = (BeanTableModel) table.getModel();
        int k = table.getSelectedRow();
        if (k < 0)
          return;
        int k2 = table.convertRowIndexToModel(k);
        ZcEbPackReq packReq = (ZcEbPackReq) model.getBean(k2);
        if (selectedDatas.size() > 0) {

          ZcEbEntrustDetail requirementDetail = (ZcEbEntrustDetail) selectedDatas.get(0);
          //requirementDetail.setDetailCode(System.currentTimeMillis());
          Long detailCode = System.currentTimeMillis();
          packReq.setDetailCode(detailCode);

          //          ZcEbRequirementDetail requirementDetail = (ZcEbRequirementDetail) selectedDatas.get(0);
          ////          requirementDetail.setDetailCode(System.currentTimeMillis());
          //          packReq.setDetailCode(requirementDetail.getDetailCode());

          packReq.getRequirementDetail().setSn(requirementDetail.getSn());
          packReq.getRequirementDetail().setSnd(requirementDetail.getSnd());
          packReq.getRequirementDetail().setDetailCode(detailCode);
          packReq.getRequirementDetail().setName(requirementDetail.getZcPitemName());
          packReq.getRequirementDetail().setZcCatalogueCode(requirementDetail.getZcCatalogueCode());
          packReq.getRequirementDetail().setZcCatalogueName(requirementDetail.getZcCatalogueName());
          packReq.getRequirementDetail().setArrDate(requirementDetail.getZcPitemArrDate());
          packReq.getRequirementDetail().setBaseReq(requirementDetail.getZcBaseGgyq());
          packReq.getRequirementDetail().setItemAttach(requirementDetail.getZcPitemAttach());
          packReq.getRequirementDetail().setItemAttachBlobid(requirementDetail.getZcPitemAttachBlobid());
          packReq.getRequirementDetail().setNum(requirementDetail.getZcCaigNum().intValue());
          packReq.getRequirementDetail().setMerPrice(requirementDetail.getZcMerPrice());
          packReq.getRequirementDetail().setItemSum(requirementDetail.getZcItemSum());
          //          packReq.getRequirementDetail().setPayCond(requirementDetail.getPayCond());
          //          packReq.getRequirementDetail().setServicePromises(requirementDetail.getServicePromises());
          //          packReq.getRequirementDetail().setType(requirementDetail.getType());
          //          packReq.getRequirementDetail().setDescription(requirementDetail.getDescription());
          addbudget(model);
          if (tablePanelPackReq.getTable().isEditing()) {
            tablePanelPackReq.getTable().getCellEditor().stopCellEditing();
          }
        }
        model.fireTableRowsUpdated(k, k);
      }

      /*

      * 清空外部实体对应的数据

      */

      public void afterClear() {
        BeanTableModel model = (BeanTableModel) table.getModel();
        int k = table.getSelectedRow();
        if (k < 0)
          return;
        int k2 = table.convertRowIndexToModel(k);
        ZcEbPackReq packReq = (ZcEbPackReq) model.getBean(k2);
        //          fillPackReq(packReq, null);
        model.fireTableRowsUpdated(k, k);
      }

      /*

      * 设置外部实体数据条件

      */

      public Boolean beforeSelect(ElementConditionDto dto) {
        JPageableFixedTable packTable = tablePanelPack.getTable();
        BeanTableModel model = (BeanTableModel) packTable.getModel();
        int k = packTable.getSelectedRow();
        if (k < 0) {
          JOptionPane.showMessageDialog(self, "请先选择一条【分包明细】", "提示", JOptionPane.WARNING_MESSAGE);
          return false;
        }
        int k2 = packTable.convertRowIndexToModel(k);
        ZcEbPack pack = (ZcEbPack) model.getBean(k2);
        if (pack.getEntrustCode() == null || "".equals(pack.getEntrustCode().trim())) {
          JOptionPane.showMessageDialog(self, "请先选择【分包明细】中的【项目委托】", "提示", JOptionPane.WARNING_MESSAGE);
          return false;
        }
        // 设置【标段需求明细】的【明细需求】外部实体的条件
        dto.setZcText0(pack.getEntrustCode());
        if (zcEbRequirement.getReqCode() != null) {
          dto.setZcText2(zcEbRequirement.getReqCode());
        }
        StringBuffer sb = new StringBuffer("'-1'");
        List<ZcEbPack> packs = zcEbRequirement.getPackList();
        if (packs == null) {
          return true;
        }
        for (int i = 0; i < packs.size(); i++) {
          List<ZcEbPackReq> packReqs = packs.get(i).getRequirementDetailList();
          if (packReqs == null) {
            continue;
          }
          for (int j = 0; j < packReqs.size(); j++) {
            if (packReqs.get(j).getRequirementDetail() != null && packReqs.get(j).getRequirementDetail().getSnd() != null
              && !"".equals(packReqs.get(j).getRequirementDetail().getSnd())) {
              sb.append(",'").append(packReqs.get(j).getRequirementDetail().getSnd()).append("'");
            }
          }
        }
        dto.setZcText3(sb.toString());

        return true;
      }

      @Override
      public TableModel createTableModel(List showDatas) {
        Object data[][] = new Object[showDatas.size()][columNames.length];
        for (int i = 0; i < showDatas.size(); i++) {
          ZcEbEntrustDetail rowData = (ZcEbEntrustDetail) showDatas.get(i);
          int col = 0;
          data[i][col++] = rowData.getZcPitemName();
          data[i][col++] = rowData.getZcCatalogueCode();
          data[i][col++] = rowData.getZcCatalogueName();
          data[i][col++] = DateUtil.dateToDdString(rowData.getZcPitemArrDate());
          data[i][col++] = rowData.getZcBaseGgyq();
          data[i][col++] = rowData.getZcCaigNum();
          data[i][col++] = rowData.getZcMerPrice();
          data[i][col++] = rowData.getZcItemSum();
          //          data[i][col++] = rowData.getPayCond();
          //          data[i][col++] = rowData.getServicePromises();
          //          data[i][col++] = rowData.getDescription();
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
    };
    ElementConditionDto packReqDto = new ElementConditionDto();
    // 设置需求类型为【业务需求】
    packReqDto.setZcText1("1");
    packReqDto.setZcText0(zcEbRequirement.getZcEbEntrust().getSn());

    ForeignEntityFieldCellEditor detailCodeEditor = new ForeignEntityFieldCellEditor("ZcEbEntrust.getZcEbEntrustDetail", packReqDto, 20, handler,
      columNames, "项目明细需求", "zcPitemName");

    //    packReqDto.setZcText2(zcEbRequirement.getReqCode());
    //    ForeignEntityFieldCellEditor detailCodeEditor = new ForeignEntityFieldCellEditor("ZcEbRequirement.getZcEbRequirementDetailBySnWithFN",
    //      packReqDto, 20, handler, columNames, "项目明细需求", "name");

    detailCodeEditor.getEditor().setEditable(false);
    SwingUtil.setTableCellEditor(table, "ZC_CATALOGUE_CODE", zcBCatalogueCellEditor);
    SwingUtil.setTableCellEditor(table, "NAME1", detailCodeEditor);
    SwingUtil.setTableCellRenderer(table, "NAME1", new LineWrapCellRenderer());
    SwingUtil.setTableCellEditor(table, "ARR_DATE", new DateCellEditor());
    SwingUtil.setTableCellRenderer(table, "ARR_DATE", new DateCellRenderer());
    FileCellEditor fileCellEditor = new FileCellEditor("requirementDetail.itemAttachBlobid");
    fileCellEditor.setDeleteFileEnable(false);
    SwingUtil.setTableCellEditor(table, "ITEM_ATTACH", fileCellEditor);
    SwingUtil.setTableCellEditor(table, "NUM", new IntCellEditor(false));
    SwingUtil.setTableCellEditor(table, "ZC_YEAR", new IntCellEditor(false));
    final MoneyCellEditor itemSumEditor = new MoneyCellEditor(false);
    SwingUtil.setTableCellEditor(table, "ITEM_SUM", itemSumEditor);
    SwingUtil.setTableCellRenderer(table, "ITEM_SUM", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "MER_PRICE", new MoneyCellEditor(false));
    SwingUtil.setTableCellRenderer(table, "MER_PRICE", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "TYPE", new AsValComboBoxCellEditor("VS_ZC_EB_REQUIREMENT_TYPE"));
    SwingUtil.setTableCellRenderer(table, "TYPE", new AsValCellRenderer("VS_ZC_EB_REQUIREMENT_TYPE"));

    final BeanTableModel model = (BeanTableModel) table.getModel();
    model.addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {

        if (e.getColumn() >= 0
          && ("ITEM_SUM".equals(model.getColumnIdentifier(e.getColumn())) || "NAME1".equals(model.getColumnIdentifier(e.getColumn())))) {
          addbudget(model);
        }

      }

    });

  }

//  private void setPackQuaTableProperty(final JTable table) {
//    table.setDefaultEditor(String.class, new TextCellEditor());
//    String[] columNames = { "资质编号", "资质名称", "资质类别" };
//    QuaHandler handler = new QuaHandler(columNames);
//    ElementConditionDto dto = new ElementConditionDto();
//    dto.setStatus("enable");
//    ForeignEntityFieldCellEditor pack = new ForeignEntityFieldCellEditor("ZC_EB_SUP_QUALIFICATION.selectForList", null, 20, handler, columNames, "",
//      "qualifName");
//    SwingUtil.setTableCellEditor(table, "QUALIF_NAME", pack);
//
//  }
//
//  private class QuaHandler implements IForeignEntityHandler {
//
//    private String[] columNames;
//
//    QuaHandler(String[] columNames) {
//      this.columNames = columNames;
//    }
//
//    public void excute(List selectedDatas) {
//      JTable table = tablePanelPackQua.getTable();
//
//      int k = table.getSelectedRow();
//
//      if (k < 0)
//
//        return;
//
//      int k2 = table.convertRowIndexToModel(k);
//      for (Object object : selectedDatas) {
//        BeanTableModel model = (BeanTableModel) table.getModel();
//        ZcEbPackQua qua = (ZcEbPackQua) model.getBean(k2);
//        ZcEbSupQualification item = (ZcEbSupQualification) object;
//        qua.setQualifId(item.getQualifId());
//        qua.setQualifName(item.getQualifName());
//
//        if (tablePanelPackQua.getTable().isEditing()) {
//          tablePanelPackQua.getTable().getCellEditor().stopCellEditing();
//        }
//        model.fireTableRowsUpdated(k, k);
//      }
//    }
//
//    @Override
//    public boolean isMultipleSelect() {
//      // TCJLODO Auto-generated method stub
//      return false;
//    }
//
//    @Override
//    public TableModel createTableModel(List showDatas) {
//
//      Object data[][] = new Object[showDatas.size()][columNames.length];
//      for (int i = 0; i < showDatas.size(); i++) {
//        ZcEbSupQualification item = (ZcEbSupQualification) showDatas.get(i);
//        int col = 0;
//        data[i][col++] = item.getQualifCode();
//        data[i][col++] = item.getQualifName();
//        data[i][col++] = item.getQualifType();
//
//      }
//      MyTableModel model = new MyTableModel(data, columNames) {
//        public boolean isCellEditable(int row, int colum) {
//          return false;
//        }
//      };
//      return model;
//    }
//
//  }

  private void addPackTableLisenter(final JPageableFixedTable table) {
    final BeanTableModel model = (BeanTableModel) table.getModel();

    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          BeanTableModel tableModel = (BeanTableModel) table.getModel();
          ZcEbPack pack;
          if (table.getSelectedRows() != null && table.getSelectedRows().length > 0) {
            pack = (ZcEbPack) tableModel.getBean(table.convertRowIndexToModel(table.getSelectedRows()[0]));
            refreshSubPackReqTable(pack);
          }
        }
      }
    });
    table.getModel().addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {

        if (e.getType() == TableModelEvent.INSERT) {
          getNewPackName(tablePanelPack, false);
          firePropertyChange("packInsert", false, true);
        }
        if (e.getType() == TableModelEvent.DELETE) {
          firePropertyChange("packDelete", false, true);
        }

        if (e.getType() == TableModelEvent.UPDATE) {
          firePropertyChange("packUpdate", false, true);
          setOldPackObject();

        }
      }
    });
  }

  private boolean packDataChanged() {
    return !DigestUtil.digest(oldPackList).equals(DigestUtil.digest(tablePanelPack.getDataList()));
  }

  private void setOldPackObject() {
    oldPackList = (List) ObjectUtil.deepCopy(tablePanelPack.getDataList());

  }

  private String getNewPackName(JTablePanel tablePanelPack, boolean isAdd) {
    RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();
    String rt = "";
    int tableCount = 0;
    int localCount = 0;
    ChangeChineseNumber ch = new ChangeChineseNumber();

    TableModel tableModel = tablePanelPack.getTable().getModel();

    if (tableModel instanceof BeanTableModel && tableModel.getRowCount() > 0) {
      BeanTableModel model = (BeanTableModel) tableModel;
      for (int k = model.getRowCount() - 1; k >= 0; k--) {
        ZcEbPack pack = (ZcEbPack) model.getBean(k);
        if (pack.getPackName() != null && !"".equals(pack.getPackName())) {
          String localName = pack.getPackName();
          localName = localName.substring(1, localName.length() - 1);
          localCount = ch.parseDigits(localName);
          break;
        }
      }
    }
    //    count++;
    if (zcEbRequirement.getZcEbEntrust().getSn() != null) {
      List list = zcEbRequirementServiceDelegate.getPackCount(zcEbRequirement, requestMeta);
      if (list == null || list.size() == 0) {
        tableCount = 0;
      } else {
        String packName = list.get(0).toString();
        packName = packName.substring(1, packName.length() - 1);
        tableCount = ch.parseDigits(packName);
      }

    }
    if (localCount > tableCount) {
      tableCount = localCount;
    }

    if (isAdd) {//如果是追加
      int rowCount = tableModel.getRowCount();
      String chinese = ch.doChange(new Integer(rowCount + 1).toString());
      rt = "第" + chinese + "包";
    } else {
      BeanTableModel editTableModel = (BeanTableModel) tablePanelPack.getTable().getModel();
      for (int i = 0; i < editTableModel.getRowCount(); i++) {
        ZcEbPack pack = (ZcEbPack) editTableModel.getBean(i);
        if (i == editTableModel.getRowCount() - 1) {
          if (zcEbRequirement.getReqCode() == null) {
            pack.setPackName("第" + ch.doChange(new Integer(tableCount + 1).toString()) + "包");
          } else {
            pack.setPackName("第" + ch.doChange(new Integer(tableCount + 1).toString()) + "包");
          }

        }
      }
    }
    return rt;
  }

  private static List<ColumnBeanPropertyPair> packTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    //    packTableColumnInfo.add(new ColumnBeanPropertyPair("ZC_MONEY_BI_SUM", "entrust.zcMoneyBiSum", "采购预算"));

    packTableColumnInfo.add(new ColumnBeanPropertyPair("PACK_NAME", "packName", LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_CODE)));

    packTableColumnInfo.add(new ColumnBeanPropertyPair("PACK_DESC", "packDesc", LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_NAME)));
    packTableColumnInfo.add(new ColumnBeanPropertyPair("PACK_BUDGET", "packBudget", LangTransMeta
      .translate(ZcElementConstants.FIELD_TRANS_ZC_EB_FIELD_PACK_BUDGET)));
    //    packTableColumnInfo.add(new ColumnBeanPropertyPair("REQ_FILE_NAME", "reqFileName", "需求附件"));
    //    packTableColumnInfo.add(new ColumnBeanPropertyPair("FORMULA_FILE_NAME", "formulaFileName", "评标方法附件"));
    //    packTableColumnInfo.add(new ColumnBeanPropertyPair("BID_DEPOSIT", "bidDeposit", "投标保证金"));

  }

  public static TableModel convertPackToTableModel(List<ZcEbPack> packList) {

    BeanTableModel<ZcEbPack> tm = new BeanTableModel<ZcEbPack>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcEbPack bean = getDataBeanList().get(rowIndex);

        if ("REQ_FILE_NAME".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {
            bean.setReqFileName(null);
            bean.setReqFileId(null);
          } else {
            bean.setReqFileName(((AsFile) aValue).getFileName());
            bean.setReqFileId(((AsFile) aValue).getFileId());
          }

          putEditedData(bean);

          fireTableCellUpdated(rowIndex, columnIndex);

        } else if ("FORMULA_FILE_NAME".equals(this.getColumnIdentifier(columnIndex))) {

          if (aValue == null) {
            bean.setFormulaFileName(null);
            bean.setFormulaFileId(null);
          } else {
            bean.setFormulaFileName(((AsFile) aValue).getFileName());
            bean.setFormulaFileId(((AsFile) aValue).getFileId());
          }
          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(dataBeanList.get(rowIndex));

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }
      }

      @Override
      public boolean isCellEditable(int row, int column) {

        if (!this.isEditable()) {

          return false;

        }

        String columnId = this.getColumnIdentifier(column);

        if ("ZC_MAKE_CODE".equals(columnId) || "ZC_MAKE_NAME".equals(columnId) || "ZC_MONEY_BI_SUM".equals(columnId) || "CO_CODE".equals(columnId)

        || "STATUS".equals(columnId) || "PACK_CODE".equals(columnId) || "PROJ_CODE".equals(columnId) || "PUR_TYPE".equals(columnId)
          || "PACK_NAME".equals(columnId) || "PACK_BUDGET".equals(columnId)) {
          return false;

        }

        return true;

      }

    };

    tm.setOidFieldName("tempId");

    for (ZcEbPack data : packList) {

      data.setTempId(Guid.genID());

    }

    tm.setDataBean(packList, packTableColumnInfo);

    return tm;

  }

  public boolean checkBeforeSave() {
    List notNullPackBillElementList = detailPackBillElementMeta.getNotNullBillElement();
    List notNullPackReqBillElementList = detailPackReqBillElementMeta.getNotNullBillElement();
//    List notNullPackQuaBillElementList = detailPackQuaBillElementMeta.getNotNullBillElement();
    StringBuilder errorInfo = new StringBuilder();
    String validatePackInfo = ZcUtil.validateDetailBillElementNull(zcEbRequirement.getPackList(), notNullPackBillElementList, false);
    String validatePackReqInfo = new String();
//    String validatePackQuaInfo = "";

    for (ZcEbPack pack : ((List<ZcEbPack>) (zcEbRequirement.getPackList()))) {
      validatePackReqInfo = ZcUtil.validateDetailBillElementNull(pack.getRequirementDetailList(), notNullPackReqBillElementList, false);
      if (validatePackReqInfo.length() != 0) {
        validatePackReqInfo = pack.getPackName() + ":" + validatePackReqInfo;
        break;
      }

//      if ("8".equals(zcEbRequirement.getZcEbEntrust().getZcPifuCgfs())) {
//        validatePackQuaInfo = ZcUtil.validateDetailBillElementNull(pack.getPackQua(), notNullPackQuaBillElementList, false);
//        if (validatePackQuaInfo.length() != 0) {
//          validatePackQuaInfo = pack.getPackName() + ":" + validatePackQuaInfo;
//          break;
//        }
//      }
    }
    String validatePackReqMoney = checkPackReqMoney();
    String validatePackMoney = checkPackMoney();

    if (validatePackInfo.length() != 0) {
      errorInfo.append("标段划分：\n").append(validatePackInfo.toString()).append("\n");
    }
    if (validatePackReqInfo.length() != 0) {
      errorInfo.append("标段需求明细：\n").append(validatePackReqInfo.toString()).append("\n");
    }
//    if (validatePackQuaInfo != null && validatePackQuaInfo.length() != 0) {
//      errorInfo.append("供应商所需资质：\n").append(validatePackQuaInfo.toString()).append("\n");
//    }
    if (validatePackReqMoney.length() > 0) {
      errorInfo.append(validatePackReqMoney).append("\n");
    }

    if (validatePackMoney.length() > 0) {
      errorInfo.append(validatePackMoney).append("\n");
    }
    if (errorInfo.length() != 0) {
      JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }

    return true;
  }

  private String checkPackReqMoney() {
    StringBuffer sb = new StringBuffer();
    List<ZcEbPack> packs = zcEbRequirement.getPackList();
    for (Iterator iterator = packs.iterator(); iterator.hasNext();) {
      ZcEbPack zcEbPack = (ZcEbPack) iterator.next();
      BigDecimal packMoney = (BigDecimal) ObjectUtils.defaultIfNull(zcEbPack.getPackBudget(), BigDecimal.ZERO);
      BigDecimal dsum = BigDecimal.ZERO;
      List<ZcEbPackReq> detail = zcEbPack.getRequirementDetailList();
      for (Iterator iterator2 = detail.iterator(); iterator2.hasNext();) {
        ZcEbPackReq zcEbPackReq = (ZcEbPackReq) iterator2.next();
        dsum = dsum.add((BigDecimal) ObjectUtils.defaultIfNull(zcEbPackReq.getRequirementDetail().getItemSum(), BigDecimal.ZERO));
      }
      if (packMoney.compareTo(dsum) == -1) {
        sb.append(zcEbPack.getPackName()).append("：分包采购明细的【预算金额】合计必须小于等于【分包预算】！\n");
      }
    }
    return sb.toString();
  }

  //如果任务已经存在分包，则计算分包预算总额
  public void getPackBudget() {
    List<ZcEbPack> packs = zcEbRequirement.getPackList();
    BigDecimal sum = zcEbRequirement.getZcEbEntrust().getUseBudget();

    for (Iterator iterator = packs.iterator(); iterator.hasNext();) {
      ZcEbPack zcEbPack = (ZcEbPack) iterator.next();
      if (zcEbPack.getPackBudget() != null) {
        packBudgetSum = packBudgetSum.add(zcEbPack.getPackBudget());
      }
    }
  }

  private String checkPackMoney() {
    StringBuffer sb = new StringBuffer();
    List<ZcEbPack> packs = zcEbRequirement.getPackList();
    BigDecimal sum = zcEbRequirement.getZcEbEntrust().getUseBudget();

    BigDecimal packSum = BigDecimal.ZERO;
    for (Iterator iterator = packs.iterator(); iterator.hasNext();) {
      ZcEbPack zcEbPack = (ZcEbPack) iterator.next();
      if (zcEbPack.getPackBudget() != null) {
        packSum = packSum.add(zcEbPack.getPackBudget());
      }
    }
    if (sum.compareTo(packSum.subtract(packBudgetSum)) < 0) {
      sb.append("新增标段的【分包预算】合计不能大于任务单的【可用预算】！");
    }
    return sb.toString();
  }

  public JTablePanel[] getSubTables() {

    return new JTablePanel[] { tablePanelPack, tablePanelPackReq };
  }

  private void addbudget(BeanTableModel model) {
    BigDecimal sum = new BigDecimal(0);
    List<ZcEbPackReq> packReqList = model.getDataBeanList();
    for (ZcEbPackReq packReq : packReqList) {
      sum = sum.add((BigDecimal) ObjectUtils.defaultIfNull(packReq.getRequirementDetail().getItemSum(), BigDecimal.ZERO));
    }
    JPageableFixedTable packTable = tablePanelPack.getTable();
    BeanTableModel tableModel = (BeanTableModel) packTable.getModel();

    if (packTable.getRowCount() < 1) {
      return;
    }
    int i = 0;
    if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
      i = packTable.getSelectedRow();
    }

    ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToView(i));
    pack.setPackBudget(sum);
    packTable.setValueAt(sum, i, 2);

  }

  public void doEdit(boolean isVisiable) {
    isEnabled = isVisiable;
    tablePanelPackReq.getTable().setEnabled(isEnabled);
//    tablePanelPackQua.getTable().setEnabled(isEnabled);
  }

  public boolean checkSn() {
    if (zcEbRequirement.getZcEbEntrust().getSn() == null) {
      JOptionPane.showMessageDialog(this, "请先选择任务单", "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }
}
