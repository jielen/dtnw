package com.ufgov.zc.client.zc.project.integration;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
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
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbProjectToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcEbSupplierToTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.celleditor.IntCellEditor;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.celleditor.zc.ZcBCatalogueCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
import com.ufgov.zc.client.component.table.cellrenderer.LineWrapCellRenderer;
import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.CompanyCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.table.codecellrenderer.CompanyCellRenderer;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.datacache.CompanyDataCache;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.bulletin.ZcEbBulletinXunJiaBidEditProjPanel;
import com.ufgov.zc.client.zc.qualification.Component.ZcEbQualificationCellEditor;
import com.ufgov.zc.client.zc.ztb.ChangeChineseNumber;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.constants.ZcValSetConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.system.util.DateUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbPackQua;
import com.ufgov.zc.common.zc.model.ZcEbPackReq;
import com.ufgov.zc.common.zc.model.ZcEbPackSupplier;
import com.ufgov.zc.common.zc.model.ZcEbPlan;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.model.ZcEbRequirementDetail;
import com.ufgov.zc.common.zc.model.ZcEbSupQualification;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;
import com.ufgov.zc.common.zc.model.ZcEbXunJia;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;

/**
 * 
* @ClassName: ZcEbProjectDividerPackPanel
* @Description: 立项分包，标段划分Panel
* @date: Sep 4, 2012 2:25:10 AM
* @version: V1.0 
* @since: 1.0
* @author: Administrator
* @modify:
 */
public class ZcEbProjectDividerPackPanel extends JPanel {
  private final JButton addBtnPack = new SubaddButton(false);

  private final JButton insertBtnPack = new SubinsertButton(false);

  private final JButton delBtnPack = new SubdelButton(false);

  /*  private JButton addBtnPackReq = new SubaddButton(false);

    private JButton insertBtnPackReq = new SubinsertButton(false);

    private JButton delBtnPackReq = new SubdelButton(false);*/

  private final JButton addBtnPackSup = new SubaddButton(false);

  private final JButton insertBtnPackSup = new SubinsertButton(false);

  private final JButton delBtnPackSup = new SubdelButton(false);

  private final ZcEbProjectDividerPackPanel self = this;

  JSaveableSplitPane splitPane;

  JTabbedPane tabPanePack;

  JTabbedPane tabPaneReq;

  private final JTablePanel tablePanelPack = new JTablePanel("tablePanelPack");

  private final JTablePanel tablePanelPackReq = new JTablePanel("tablePanelPackReq");

  private final JTablePanel tablePanelSupplier = new JTablePanel("tablePanelSupplier");

  private final JTablePanel tablePanelItemInfo = new JTablePanel("tablePanelItemInfo");

  private final JTablePanel tablePanelPackQua = new JTablePanel("tablePanelPackQua");

  private final ZcBCatalogueCellEditor zcBCatalogueCellEditor = new ZcBCatalogueCellEditor(true);

  private DateFieldEditor itemInfoEndDateField;

  private JPanel itemInfoPanel;

  public ZcEbBulletinXunJiaBidEditProjPanel billEdit;

  public ZcEbProj zcEbProj;

  private String purType;

  private final int packSelectRow = 0;

  private final RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private ZcEbPackHandler handler;

  private final ZcEbProjectEditPanel parentJPanel;

  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,
    "zcEbBaseServiceDelegate");

  protected IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
    "baseDataServiceDelegate");

  /**
  * @return the zcEbProj
  */
  public ZcEbProj getZcEbProj() {
    return zcEbProj;
  }

  /**
   * @param zcEbProj the zcEbProj to set
   */
  public void setZcEbProj(ZcEbProj zcEbProj) {
    this.zcEbProj = zcEbProj;
  }

  public ZcEbProjectDividerPackPanel(ZcEbProjectEditPanel parentJPanel) {
    this.parentJPanel = parentJPanel;
    initComponet();
  }

  private void initComponet() {
    tabPanePack = new JTabbedPane();
    tablePanelPack.init();
    tablePanelPack.getSearchBar().setVisible(false);
    tablePanelPack.setTablePreferencesKey(this.getClass().getName() + "_detail_table");
    tabPanePack.addTab("分包", tablePanelPack);
    JFuncToolBar bottomToolBarPack = new JFuncToolBar();
    bottomToolBarPack.add(addBtnPack);
    bottomToolBarPack.add(insertBtnPack);
    bottomToolBarPack.add(delBtnPack);
    tablePanelPack.add(bottomToolBarPack, BorderLayout.SOUTH);
    addBtnPack.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        if (!confirmPackAddAndDelete()) {
          return;
        }
        // 添加分包明细
        ZcEbPack pack = new ZcEbPack();
        setPackDefaultValue(pack, ZcSettingConstants.PACK_STATUS_DRAFT);
        int rowNum = ZcUtil.addSub(tablePanelPack, pack);
        tablePanelPack.getTable().setRowSelectionInterval(rowNum, rowNum);
        // 从新计算【项目总预算】
        caculateSumMoney(((BeanTableModel) tablePanelPack.getTable().getModel()).getDataBeanList());
      }
    });
    insertBtnPack.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!confirmPackAddAndDelete()) {
          return;
        }
        // 插入分包明细
        ZcEbPack pack = new ZcEbPack();
        setPackDefaultValue(pack, ZcSettingConstants.PACK_STATUS_DRAFT);
        int rowNum = ZcUtil.insertSub(tablePanelPack, pack);
        tablePanelPack.getTable().setRowSelectionInterval(rowNum, rowNum);
        // 从新计算【项目总预算】
        caculateSumMoney(((BeanTableModel) tablePanelPack.getTable().getModel()).getDataBeanList());
      }
    });
    // 删除项目标段按钮的事件监听
    delBtnPack.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!confirmPackAddAndDelete()) {
          return;
        }
        ;
        Integer[] checkedRows = ZcUtil.deleteSub(tablePanelPack, self);
        if (checkedRows.length > 0) {
          if (tablePanelPack.getTable().getRowCount() == 0) {
            ZcEbPack pack = new ZcEbPack();
            setPackDefaultValue(pack, "0");
            ZcUtil.addSub(tablePanelPack, pack);
            tablePanelPack.getTable().setRowSelectionInterval(0, 0);
          } else {
            tablePanelPack.getTable().setRowSelectionInterval(0, 0);
          }
          // 从新计算【项目总预算】
          caculateSumMoney(((BeanTableModel) tablePanelPack.getTable().getModel()).getDataBeanList());
        }
      }
    });

    tabPaneReq = new JTabbedPane();
    tablePanelPackReq.init();
    tablePanelPackReq.getSearchBar().setVisible(false);
    tablePanelPackReq.setTablePreferencesKey(this.getClass().getName() + "_detailother_table");
    tabPaneReq.addTab("分包采购明细", tablePanelPackReq);

    //tablePanelSupplier
    tablePanelSupplier.init();
    tablePanelSupplier.getSearchBar().setVisible(false);
    tablePanelSupplier.setTablePreferencesKey(this.getClass().getName() + "_supplierother_table");
    //    tabPaneReq.addTab("邀请供应商", tablePanelSupplier);
    //    addPackSupplierPanel();
    JFuncToolBar supplierToolBarPack = new JFuncToolBar();
    supplierToolBarPack.add(addBtnPackSup);
    supplierToolBarPack.add(insertBtnPackSup);
    supplierToolBarPack.add(delBtnPackSup);
    tablePanelSupplier.add(supplierToolBarPack, BorderLayout.SOUTH);
    addBtnPackSup.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 添加供应商
        ZcEbPackSupplier packSup = new ZcEbPackSupplier();
        JPageableFixedTable packTable = tablePanelPack.getTable();
        BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
        if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
          ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToModel(packTable.getSelectedRows()[0]));
          packSup.setPackCode(pack.getPackCode());
          packSup.getZcEbSupplier().setCode(null);
          setPackSupplierDefaultValue(packSup);
          ZcUtil.addSub(tablePanelSupplier, packSup);
        } else {
          JOptionPane.showMessageDialog(self, "请先选择一条【分包明细】", "提示", JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    insertBtnPackSup.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 插入供应商
        ZcEbPackSupplier packSup = new ZcEbPackSupplier();
        JPageableFixedTable packTable = tablePanelPack.getTable();
        BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
        if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
          ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToModel(packTable.getSelectedRows()[0]));
          packSup.setPackCode(pack.getPackCode());
          packSup.getZcEbSupplier().setCode(pack.getEntrustCode());
          setPackSupplierDefaultValue(packSup);
          ZcUtil.addSub(tablePanelSupplier, packSup);
        } else {
          JOptionPane.showMessageDialog(self, "请先选择一条【分包明细】", "提示", JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    // 删除供应商按钮的事件监听
    delBtnPackSup.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Integer[] checkedRows = ZcUtil.deleteSub(tablePanelSupplier, self);
        if (checkedRows.length > 0) {
          if (tablePanelSupplier.getTable().getRowCount() == 0) {
            ZcEbPack pack = new ZcEbPack();
            setPackDefaultValue(pack, "0");
            ZcUtil.addSub(tablePanelSupplier, pack);
            tablePanelSupplier.getTable().setRowSelectionInterval(0, 0);
          } else {
            tablePanelSupplier.getTable().setRowSelectionInterval(0, 0);
          }
        }
      }
    });

    initQuaPane();

    /*

    * 创建询价panel

    */

    splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, tabPanePack, tabPaneReq);
    splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 200);
    splitPane.setContinuousLayout(true);
    splitPane.setOneTouchExpandable(true);
    // 只显示向下的箭头
    //      splitPane.putClientProperty("toExpand", true);
    splitPane.setDividerSize(5);
    splitPane.setDividerLocation(200);
    this.setLayout(new BorderLayout());
    this.add(splitPane);

  }

  private void initQuaPane() {
    tablePanelPackQua.init();
    tablePanelPackQua.getSearchBar().setVisible(false);
    tablePanelPackQua.setTablePreferencesKey(this.getClass().getName() + "_detailother_table");
    JFuncToolBar bottomToolBar = new JFuncToolBar();

    JButton addBtn = new SubaddButton(false, "添加资质");

    JButton insertBtn = new SubinsertButton(false, "插入资质");

    JButton delBtn = new SubdelButton(false, "删除资质");
    bottomToolBar.add(addBtn);
    bottomToolBar.add(insertBtn);
    bottomToolBar.add(delBtn);
    tablePanelPackQua.add(bottomToolBar, BorderLayout.SOUTH);
    addBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZcEbPackQua packQua = new ZcEbPackQua();
        JPageableFixedTable packTable = tablePanelPack.getTable();
        BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
        if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
          ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToModel(packTable.getSelectedRows()[0]));
          packQua.setPackCode(pack.getPackCode());
          packQua.setPackName(pack.getPackName());
          packQua.setTempId(Guid.genID());

          ZcUtil.addSub(tablePanelPackQua, packQua);
        } else {
          JOptionPane.showMessageDialog(self, "请先选择一条【分包明细】", "提示", JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    insertBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZcEbPackQua packQua = new ZcEbPackQua();
        JPageableFixedTable packTable = tablePanelPack.getTable();
        BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
        if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
          ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToModel(packTable.getSelectedRows()[0]));
          packQua.setPackCode(pack.getPackCode());
          packQua.setTempId(Guid.genID());
          packQua.setPackName(pack.getPackName());
          ZcUtil.insertSub(tablePanelPackQua, packQua);
        } else {
          JOptionPane.showMessageDialog(self, "请先选择一条【分包明细】", "提示", JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    delBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZcUtil.deleteSub(tablePanelPackQua, self);
      }
    });
  }

  private void reCrtSubPanel() {

    if (zcEbProj != null && ZcSettingConstants.PITEM_OPIWAY_XJ.equals(zcEbProj.getPurType())) {
      if (tabPaneReq.indexOfTabComponent(tablePanelPackQua) < 0) {
        tabPaneReq.addTab("分包供应商资质", tablePanelPackQua);
      }
    } else {
      tabPaneReq.remove(tablePanelPackQua);
    }

  }

  /**
     * 修改分包操作的时候给提示
     */
  private boolean confirmPackAddAndDelete() {
    String pageStatus = parentJPanel.getPageStatus();
    if (ZcSettingConstants.PAGE_STATUS_BROWSE.equals(pageStatus)) {
      int num = JOptionPane.showConfirmDialog(this, "对分包进行添加或者删除操作，都需要重新做招标书！是否确定操作", "操作确认", 0);
      if (num != 0) {
        return false;
      }
      parentJPanel.setZbBookRedoFlag(true);
    }
    return true;
  }

  /**
  * @return void 返回类型
  * @Description: 需求明细子表，选择需求之后，填充标段需求明细。
  * @since 1.0
  */
  private void fillPackReq(ZcEbPackReq packReq, ZcEbRequirementDetail requirementDetail) {
    if (requirementDetail != null) {
      packReq.setDetailCode(requirementDetail.getDetailCode());
      packReq.getRequirementDetail().setSn(requirementDetail.getSn());
      packReq.getRequirementDetail().setName(requirementDetail.getName());
      packReq.getRequirementDetail().setZcCatalogueCode(requirementDetail.getZcCatalogueCode());
      packReq.getRequirementDetail().setZcCatalogueName(requirementDetail.getZcCatalogueName());
      packReq.getRequirementDetail().setArrDate(requirementDetail.getArrDate());
      packReq.getRequirementDetail().setBaseReq(requirementDetail.getBaseReq());
      packReq.getRequirementDetail().setItemAttach(requirementDetail.getItemAttach());
      packReq.getRequirementDetail().setItemAttachBlobid(requirementDetail.getItemAttachBlobid());
      packReq.getRequirementDetail().setNum(requirementDetail.getNum());
      packReq.getRequirementDetail().setItemSum(requirementDetail.getItemSum());
      packReq.getRequirementDetail().setMerPrice(requirementDetail.getMerPrice());
      packReq.getRequirementDetail().setPayCond(requirementDetail.getPayCond());
      packReq.getRequirementDetail().setServicePromises(requirementDetail.getServicePromises());
      packReq.getRequirementDetail().setType(requirementDetail.getType());
      packReq.getRequirementDetail().setDescription(requirementDetail.getDescription());
    } else {
      packReq.setDetailCode(null);
      packReq.getRequirementDetail().setSn(null);
      packReq.getRequirementDetail().setName(null);
      packReq.getRequirementDetail().setZcCatalogueCode(null);
      packReq.getRequirementDetail().setZcCatalogueName(null);
      packReq.getRequirementDetail().setArrDate(null);
      packReq.getRequirementDetail().setBaseReq(null);
      packReq.getRequirementDetail().setItemAttach(null);
      packReq.getRequirementDetail().setItemAttachBlobid(null);
      packReq.getRequirementDetail().setNum(null);
      packReq.getRequirementDetail().setItemSum(null);
      packReq.getRequirementDetail().setMerPrice(null);
      packReq.getRequirementDetail().setPayCond(null);
      packReq.getRequirementDetail().setServicePromises(null);
      packReq.getRequirementDetail().setType(null);
      packReq.getRequirementDetail().setDescription(null);
    }
  }

  private void setPackTableProperty(final JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());

    String columNames[] = { "单位", "任务单编号", LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_CODE),
      LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_NAME),
      LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_EB_FIELD_PACK_BUDGET), "采购方式", "联系人", "联系人电话" };

    ElementConditionDto packDto = new ElementConditionDto();
    packDto.setBillStatus("exec");
    packDto.setExecutor(requestMeta.getSvUserID());
    packDto.setCoCode(requestMeta.getSvCoCode());
    packDto.setZcText1("1");
    packDto.setZcText2("4");
    if (handler == null) {
      handler = new ZcEbPackHandler(columNames, table);
    }

    ForeignEntityFieldCellEditor packEditor = new ForeignEntityFieldCellEditor("ZcEbReqPack.getZcEbPackListByJingBanRen", packDto, 20, handler,
      columNames, "分包", "packName");
    final MoneyCellEditor packBudget = new MoneyCellEditor(false);
    SwingUtil.setTableCellEditor(table, "PACK_BUDGET", packBudget);

    SwingUtil.setTableCellEditor(table, "PACK_NAME", packEditor);
    SwingUtil.setTableCellRenderer(table, "PACK_BUDGET", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "BID_DEPOSIT", new MoneyCellEditor(false));
    SwingUtil.setTableCellRenderer(table, "BID_DEPOSIT", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "SELE_NUM", new IntCellEditor(false));
    SwingUtil.setTableCellEditor(table, "PUR_TYPE", new AsValComboBoxCellEditor("ZC_VS_PITEM_OPIWAY"));
    SwingUtil.setTableCellRenderer(table, "PUR_TYPE", new AsValCellRenderer("ZC_VS_PITEM_OPIWAY"));
    SwingUtil.setTableCellEditor(table, "ANALYSE_TYPE", new AsValComboBoxCellEditor("VS_ZC_ANALYSE_TYPE"));
    SwingUtil.setTableCellRenderer(table, "ANALYSE_TYPE", new AsValCellRenderer("VS_ZC_ANALYSE_TYPE"));
    SwingUtil.setTableCellEditor(table, "IS_FAILED", new AsValComboBoxCellEditor("VS_Y/N"));
    SwingUtil.setTableCellRenderer(table, "IS_FAILED", new AsValCellRenderer("VS_Y/N"));
    SwingUtil.setTableCellEditor(table, "IS_CHECK_QUALIFICATION", new AsValComboBoxCellEditor("VS_Y/N"));
    SwingUtil.setTableCellRenderer(table, "IS_CHECK_QUALIFICATION", new AsValCellRenderer("VS_Y/N"));
    SwingUtil.setTableCellEditor(table, "IS_SHOW_BUDGET", new AsValComboBoxCellEditor("VS_Y/N"));
    SwingUtil.setTableCellRenderer(table, "IS_SHOW_BUDGET", new AsValCellRenderer("VS_Y/N"));
    SwingUtil.setTableCellEditor(table, "SELE_TYPE", new AsValComboBoxCellEditor("VS_SELE_TYPE"));
    SwingUtil.setTableCellRenderer(table, "SELE_TYPE", new AsValCellRenderer("VS_SELE_TYPE"));
    SwingUtil.setTableCellEditor(table, "STATUS", new AsValComboBoxCellEditor("VS_ZC_PACK_STATUS"));
    SwingUtil.setTableCellRenderer(table, "STATUS", new AsValCellRenderer("VS_ZC_PACK_STATUS"));
    SwingUtil.setTableCellEditor(table, "CO_CODE", new CompanyCellEditor());
    SwingUtil.setTableCellRenderer(table, "CO_CODE", new CompanyCellRenderer());
    SwingUtil.setTableCellEditor(table, "PACK_MAX_PRICE", new MoneyCellEditor());
    SwingUtil.setTableCellRenderer(table, "PACK_MAX_PRICE", new NumberCellRenderer());
  }

  /**
   * @return void 返回类型
   * @Description: 设置需求明细子表的CellEditor
   * @since 1.0
   */
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
          ZcEbRequirementDetail requirementDetail = (ZcEbRequirementDetail) selectedDatas.get(0);
          requirementDetail.setDetailCode(System.currentTimeMillis());
          packReq.setDetailCode(requirementDetail.getDetailCode());
          packReq.getRequirementDetail().setSn(requirementDetail.getSn());
          packReq.getRequirementDetail().setDetailCode(requirementDetail.getDetailCode());
          packReq.getRequirementDetail().setName(requirementDetail.getName());
          packReq.getRequirementDetail().setZcCatalogueCode(requirementDetail.getZcCatalogueCode());
          packReq.getRequirementDetail().setZcCatalogueName(requirementDetail.getZcCatalogueName());
          packReq.getRequirementDetail().setArrDate(requirementDetail.getArrDate());
          packReq.getRequirementDetail().setBaseReq(requirementDetail.getBaseReq());
          packReq.getRequirementDetail().setItemAttach(requirementDetail.getItemAttach());
          packReq.getRequirementDetail().setItemAttachBlobid(requirementDetail.getItemAttachBlobid());
          packReq.getRequirementDetail().setNum(requirementDetail.getNum());
          packReq.getRequirementDetail().setMerPrice(requirementDetail.getMerPrice());
          packReq.getRequirementDetail().setItemSum(requirementDetail.getItemSum());
          packReq.getRequirementDetail().setPayCond(requirementDetail.getPayCond());
          packReq.getRequirementDetail().setServicePromises(requirementDetail.getServicePromises());
          packReq.getRequirementDetail().setType(requirementDetail.getType());
          packReq.getRequirementDetail().setDescription(requirementDetail.getDescription());
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
        fillPackReq(packReq, null);
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
        return true;
      }

      @Override
      public TableModel createTableModel(List showDatas) {
        Object data[][] = new Object[showDatas.size()][columNames.length];
        for (int i = 0; i < showDatas.size(); i++) {
          ZcEbRequirementDetail rowData = (ZcEbRequirementDetail) showDatas.get(i);
          int col = 0;
          data[i][col++] = rowData.getName();
          data[i][col++] = rowData.getZcCatalogueCode();
          data[i][col++] = rowData.getZcCatalogueName();
          data[i][col++] = DateUtil.dateToDdString(rowData.getArrDate());
          data[i][col++] = rowData.getBaseReq();
          data[i][col++] = rowData.getNum();
          data[i][col++] = rowData.getMerPrice();
          data[i][col++] = rowData.getItemSum();
          data[i][col++] = rowData.getPayCond();
          data[i][col++] = rowData.getServicePromises();
          data[i][col++] = rowData.getDescription();
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
    ForeignEntityFieldCellEditor detailCodeEditor = new ForeignEntityFieldCellEditor("ZcEbRequirement.getZcEbRequirementDetailBySnWithFN",
      packReqDto, 20, handler, columNames, "项目明细需求", "name");
    detailCodeEditor.getEditor().setEditable(false);
    // 明细需求编号
    SwingUtil.setTableCellEditor(table, "ZC_CATALOGUE_CODE", zcBCatalogueCellEditor);
    SwingUtil.setTableCellEditor(table, "NAME", detailCodeEditor);
    SwingUtil.setTableCellRenderer(table, "NAME", new LineWrapCellRenderer());
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
    AsValComboBoxCellEditor acbc = new AsValComboBoxCellEditor("V_SP_UNIT");
    SwingUtil.setTableCellEditor(table, "SP_UNIT", acbc);
    SwingUtil.setTableCellRenderer(table, "SP_UNIT", new AsValCellRenderer("V_SP_UNIT"));
    itemSumEditor.getField().addFocusListener(new FocusListener() {
      private String lastValue;

      @Override
      public void focusGained(FocusEvent event) {
        if (!event.isTemporary()) {
          lastValue = itemSumEditor.getField().getText();
        }
      }

      @Override
      public void focusLost(FocusEvent event) {
        // TODO Auto-generated method stub
        if (!event.isTemporary()) {
          JPageableFixedTable table = tablePanelPackReq.getTable();
          final BeanTableModel model = (BeanTableModel) table.getModel();
          BigDecimal sum = new BigDecimal(0);
          List<ZcEbPackReq> packReqList = model.getDataBeanList();
          for (ZcEbPackReq packReq : packReqList) {
            sum = sum.add((BigDecimal) ObjectUtils.defaultIfNull(packReq.getRequirementDetail().getItemSum(), BigDecimal.ZERO));
          }
          JPageableFixedTable packTable = tablePanelPack.getTable();
          BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
          if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
            ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToView(packTable.getSelectedRow()));
            if (sum.compareTo(pack.getPackBudget()) == 1) {
              ZcEbPackReq packReq = (ZcEbPackReq) (model.getBean(table.convertRowIndexToView(table.getSelectedRow())));
              packReq.getRequirementDetail().setItemSum(new BigDecimal(lastValue));
              JOptionPane.showMessageDialog(self, "分包明细预算金额合计不能大于分包预算金额", "提示", JOptionPane.WARNING_MESSAGE);
            }
          }
        }
      }
    });
  }

  private void fillPackSupplier(ZcEbPackSupplier packSup, ZcEbSupplier sup) {
    if (sup != null) {
      packSup.getZcEbSupplier().setCode(sup.getCode());
      packSup.getZcEbSupplier().setName(sup.getName());
    } else {
      packSup.getZcEbSupplier().setCode(null);
      packSup.getZcEbSupplier().setName(null);
    }
  }

  private void setSupplierTableProperty(final JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    final String columNames[] = { "供应商代码", "供应商名称", "联系人", "联系电话", "邮编", "地址" };
    IForeignEntityHandler handler = new IForeignEntityHandler() {
      @Override
      public void excute(List selectedDatas) {

        BeanTableModel model = (BeanTableModel) table.getModel();

        int k = table.getSelectedRow();

        if (k < 0)
          return;

        int k2 = table.convertRowIndexToModel(k);

        ZcEbPackSupplier packSup = (ZcEbPackSupplier) model.getBean(k2);

        if (selectedDatas.size() > 0) {

          ZcEbSupplier supplier = (ZcEbSupplier) selectedDatas.get(0);

          packSup.setProviderCode(supplier.getCode());

          packSup.getZcEbSupplier().setCode(supplier.getCode());

          packSup.getZcEbSupplier().setName(supplier.getName());

          packSup.getZcEbSupplier().setLinkMan(supplier.getLinkMan());

          packSup.getZcEbSupplier().setLinkManPhone(supplier.getLinkManPhone());

          packSup.getZcEbSupplier().setZipCode(supplier.getZipCode());

          packSup.getZcEbSupplier().setAddress(supplier.getAddress());

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
        ZcEbPackSupplier packSup = (ZcEbPackSupplier) model.getBean(k2);
        fillPackSupplier(packSup, null);
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
        BeanTableModel m1 = (BeanTableModel) tablePanelSupplier.getTable().getModel();
        List lst = m1.getDataBeanList();

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < lst.size(); i++) {
          ZcEbPackSupplier sup = (ZcEbPackSupplier) lst.get(i);

          if (sup.getProviderCode() != null) {
            s.append("'").append(sup.getProviderCode()).append("'").append(",");
          }
        }
        if (s.length() > 0)
          dto.setZcText4(s.substring(0, s.length() - 1));

        return true;
      }

      @Override
      public TableModel createTableModel(List showDatas) {
        Object data[][] = new Object[showDatas.size()][columNames.length];
        for (int i = 0; i < showDatas.size(); i++) {
          ZcEbSupplier rowData = (ZcEbSupplier) showDatas.get(i);
          int col = 0;
          data[i][col++] = rowData.getCode();
          data[i][col++] = rowData.getName();
          data[i][col++] = rowData.getLinkMan();
          data[i][col++] = rowData.getLinkManPhone();
          data[i][col++] = rowData.getZipCode();
          data[i][col++] = rowData.getAddress();
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
    ForeignEntityFieldCellEditor detailCodeEditor = new ForeignEntityFieldCellEditor("ZcEbSupplier.getEnableSupplierList", packReqDto, 20, handler,
      columNames, "供应商", "code");
    detailCodeEditor.getEditor().setEditable(false);
    // 明细需求编号
    SwingUtil.setTableCellEditor(table, "ZC_SU_CODE", detailCodeEditor);
    SwingUtil.setTableCellRenderer(table, "ZC_SU_CODE", new LineWrapCellRenderer());

  }

  /**
   * @return BigDecimal 返回类型
   * @Description: 获取当前标段可分配金额
   * @since 1.0
   */
  private BigDecimal getSurPlusSum(ZcEbEntrust entrust) {
    //任务单总预算金额,这里的总金额是取得预算单位申报时的总的计划金额。
    BigDecimal entrustMoney = entrust.getZcMoneyBiSum();
    //该任务单下所有已经使用的预算金额总计：
    BigDecimal sum = getSavedPackBudgetSum(entrust.getSn(), zcEbProj.getProjCode());
    //获取分包model里面已经占用的金额。
    BeanTableModel tableModel = (BeanTableModel) tablePanelPack.getTable().getModel();
    List<ZcEbPack> currProjPacks = tableModel.getDataBeanList();
    BigDecimal allPackSum = getCurrentPackBudgetSum(currProjPacks, entrust.getSn());
    return entrustMoney.subtract(sum.add(allPackSum));
  }

  /**
   * 返回一个标段对应的任务，除去当前项目，在其他项目中对应的分包预算的合计
   *
   * @param pack
   * @return
   */
  private BigDecimal getSavedPackBudgetSum(String de, String projCode) {
    ElementConditionDto dto = new ElementConditionDto();
    dto.setZcText0(de);
    List<ZcEbPack> packList = zcEbBaseServiceDelegate.getForeignEntitySelectedData("ZcEbProj.getZcEbPackByEntrustCode", dto, requestMeta);
    BigDecimal sum = BigDecimal.ZERO;//此任务对应的已经编制的所有分包预算合计（除去此任务在当前页面的对应记录）
    for (ZcEbPack zcEbPack : packList) {
      if (ZcSettingConstants.PACK_STATUS_CANCEL.equals(zcEbPack.getStatus()) || ZcSettingConstants.PACK_STATUS_SUSPENDED.equals(zcEbPack.getStatus())
        || ZcSettingConstants.PACK_STATUS_DELAY.equals(zcEbPack.getStatus())) {//判断的方式修改了一下，
        continue;
      }
      if (!zcEbPack.getProjCode().equals(projCode)) {
        sum = sum.add((BigDecimal) ObjectUtils.defaultIfNull(zcEbPack.getPackBudget(), BigDecimal.ZERO));
      }
    }
    return sum;
  }

  public void refreshSubTableData(ZcEbProj proj) {
    this.zcEbProj = proj;
    if (proj.getPackList().size() > 0) {
      this.tablePanelPack.setTableModel(ZcEbProjectToTableModelConverter.convertPackToTableModel(proj.getPackList()));
      ZcEbPack pack = (ZcEbPack) proj.getPackList().get(0);
      refreshSubPackReqTable(pack);
      addPackSupplierPanel();
      refreshSubSupplierTable(pack);
      //      refreshItemInfoTable(pack);
      refreshPackQuaTable(pack);
    } else {
      ZcEbPack pack = new ZcEbPack();
      setPackDefaultValue(pack, ZcSettingConstants.PACK_STATUS_DRAFT);
      proj.getPackList().add(pack);
      this.tablePanelPack.setTableModel(ZcEbProjectToTableModelConverter.convertPackToTableModel(proj.getPackList()));
      refreshSubPackReqTable(pack);
      refreshSubSupplierTable(pack);
      //      refreshItemInfoTable(pack);
      refreshPackQuaTable(pack);
    }
    // 设置默认选中的【项目标段】记录
    this.tablePanelPack.getTable().setRowSelectionInterval(this.packSelectRow, this.packSelectRow);
    // 翻译分包明细表头列
    ZcUtil.translateColName(tablePanelPack.getTable(), ZcEbProjectToTableModelConverter.getPackTableColumnInfo());
    // 设置分包明细列类型
    setPackTableProperty(tablePanelPack.getTable());
    // 设置监听，通过选择的【项目标段】自动更新【标段需求明细】，并判断是否显示询价页签以及更新【询价内容】
    addPackTableLisenter(tablePanelPack.getTable());
    // 设置监听，通过修改【分包预算】更新【项目总预算】
    addPackTableLisenterForBudget(tablePanelPack.getTable());
  }

  private void refreshSubPackReqTable(ZcEbPack pack) {
    if (pack.getPurType() != null) {
      purType = pack.getPurType();
      if (pack.getPurType().equals(ZcSettingConstants.PITEM_OPIWAY_XJ)) {
        refreshSubPackReqXunJiaTable(pack);
      } else {
        refreshSubPackReqQiTaTable(pack);
      }
    } else {
      refreshSubPackReqQiTaTable(pack);
    }
    // 设置标段需求明细列类型
    setPackReqTableProperty(tablePanelPackReq.getTable());
  }

  private void refreshSubPackReqXunJiaTable(ZcEbPack pack) {
    tablePanelPackReq.setTableModel(ZcEbProjectToTableModelConverter.convertPackReqXunJiaToTableModel(pack.getRequirementDetailList()));
    // 翻译标段需求明细表头列
    ZcUtil.translateColName(tablePanelPackReq.getTable(), ZcEbProjectToTableModelConverter.getPackReqXunJiaTableColumnInfo());
  }

  private void refreshSubPackReqQiTaTable(ZcEbPack pack) {
    tablePanelPackReq.setTableModel(ZcEbProjectToTableModelConverter.convertPackReqToTableModel(pack.getRequirementDetailList(), false));
    // 翻译标段需求明细表头列
    ZcUtil.translateColName(tablePanelPackReq.getTable(), ZcEbProjectToTableModelConverter.getPackReqTableColumnInfo());
  }

  private void refreshSubSupplierTable(ZcEbPack pack) {
    tablePanelSupplier.setTableModel(ZcEbSupplierToTableModelConverter.convertSupplierToTableModel(pack.getSupplierList()));
    // 翻译标段需求明细表头列
    ZcUtil.translateColName(tablePanelSupplier.getTable(), ZcEbSupplierToTableModelConverter.getSupplierTableColumnInfo());
    // 设置标段需求明细列类型
    setSupplierTableProperty(tablePanelSupplier.getTable());
  }

  private void refreshPackQuaTable(ZcEbPack pack) {
    tablePanelPackQua.setTableModel(ZcEbProjectToTableModelConverter.convertPackQuaToTableModel(pack.getPackQua(), true));
    // 翻译标段需求明细表头列
    ZcUtil.translateColName(tablePanelPackQua.getTable(), ZcEbProjectToTableModelConverter.getPackQuaTableColumnInfo());
    // 设置标段需求明细列类型
    setPackQuaTableProperty(tablePanelPackQua.getTable());
    reCrtSubPanel();
  }

  private void setPackQuaTableProperty(final JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
//    String[] columNames = { "资质编号", "资质名称", "资质类别" };
//    QuaHandler handler = new QuaHandler(columNames);
//    ElementConditionDto dto = new ElementConditionDto();
//    dto.setStatus("enable");
//    ForeignEntityFieldCellEditor pack = new ForeignEntityFieldCellEditor("ZC_EB_SUP_QUALIFICATION.selectForList", null, 20, handler, columNames, "",
//      "qualifName");
    SwingUtil.setTableCellEditor(table, "QUALIF_NAME", new ZcEbQualificationCellEditor(true));

  }

  private class QuaHandler implements IForeignEntityHandler {

    private String[] columNames;

    QuaHandler(String[] columNames) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {
      JTable table = tablePanelPackQua.getTable();

      int k = table.getSelectedRow();

      if (k < 0)

        return;

      int k2 = table.convertRowIndexToModel(k);
      for (Object object : selectedDatas) {
        BeanTableModel model = (BeanTableModel) table.getModel();
        ZcEbPackQua qua = (ZcEbPackQua) model.getBean(k2);
        ZcEbSupQualification item = (ZcEbSupQualification) object;
        qua.setQualifId(item.getQualifId());
        qua.setQualifName(item.getQualifName());

        if (tablePanelPackQua.getTable().isEditing()) {
          tablePanelPackQua.getTable().getCellEditor().stopCellEditing();
        }
        model.fireTableRowsUpdated(k, k);
      }
    }

    @Override
    public boolean isMultipleSelect() {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        ZcEbSupQualification item = (ZcEbSupQualification) showDatas.get(i);
        int col = 0;
        data[i][col++] = item.getQualifCode();
        data[i][col++] = item.getQualifName();
        data[i][col++] = item.getQualifType();

      }
      MyTableModel model = new MyTableModel(data, columNames) {
        public boolean isCellEditable(int row, int colum) {
          return false;
        }
      };
      return model;
    }

  }

  /*

  * 设置监听，通过选择的【项目标段】自动更新【标段需求明细】，并判断是否显示询价页签以及更新【询价内容】

  */

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
            addPackSupplierPanel();
            refreshSubSupplierTable(pack);
            //            refreshItemInfoTable(pack);
            //            refreshXunJiaTable(pack);
            refreshPackQuaTable(pack);
          }
        }
      }
    });
    table.getModel().addTableModelListener(new TableModelListener() {
      @Override
      public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.INSERT) {
          firePropertyChange("packInsert", false, true);
        }
        if (e.getType() == TableModelEvent.DELETE) {
          firePropertyChange("packDelete", false, true);
        }
      }
    });
  }

  private void refreshItemInfoTable(ZcEbPack pack) {
    tablePanelItemInfo.setTableModel(ZcEbProjectToTableModelConverter.convertXunJiaToTableModel(pack.getXunJiaList()));
    if (zcEbProj.getPlan() == null) {
      ZcEbPlan plan = new ZcEbPlan();
      plan.setNd(this.requestMeta.getSvNd());
      zcEbProj.setPlan(plan);
    }
    this.itemInfoEndDateField.setEditObject(zcEbProj);
    ZcUtil.translateColName(tablePanelItemInfo.getTable(), "ZC_EB_");
    setXunJiaTableProperty(tablePanelItemInfo.getTable());
    if (pack.getPurType() != null) {
      purType = pack.getPurType();
      if (pack.getPurType().equals(ZcSettingConstants.PITEM_OPIWAY_XJ)) {
        if (tabPaneReq.getTabCount() == 1) {
          tabPaneReq.addTab("商品信息", itemInfoPanel);
        }
      } else {
        if (tabPaneReq.getTabCount() > 1) {
          tabPaneReq.remove(itemInfoPanel);
        }
      }
    } else {
      if (tabPaneReq.getTabCount() > 1) {
        tabPaneReq.remove(itemInfoPanel);
      }
    }
    tabPaneReq.repaint();
  }

  private void setXunJiaTableProperty(JPageableFixedTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    FileCellEditor fc = new FileCellEditor("spTechFileId", (BeanTableModel) table.getModel());
    AsValComboBoxCellEditor acbc = new AsValComboBoxCellEditor("V_SP_UNIT");
    SwingUtil.setTableCellEditor(table, "SP_TECH_FILE_NAME", fc);
    SwingUtil.setTableCellEditor(table, "SP_NUM", new IntCellEditor(false));
    SwingUtil.setTableCellEditor(table, "SP_UNIT", acbc);
    SwingUtil.setTableCellRenderer(table, "SP_UNIT", new AsValCellRenderer("V_SP_UNIT"));
  }

  /*

  * 设置监听，通过修改【分包预算】更新【项目总预算】

  */

  private void addPackTableLisenterForBudget(final JPageableFixedTable table) {
    final BeanTableModel model = (BeanTableModel) table.getModel();
    model.addTableModelListener(new TableModelListener() {
      public void tableChanged(TableModelEvent e) {
        JPageableFixedTable packTable = tablePanelPack.getTable();
        BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
        if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
          if (e.getColumn() >= 0 && "PACK_BUDGET".equals(model.getColumnIdentifier(e.getColumn()))) {
            caculateSumMoney(model.getDataBeanList());
          }
        }
      }
    });
  }

  /**
   * @return BigDecimal 返回类型
   * @Description: 获取当前所有标段的分包预算总计
   * @since 1.0
   */
  private BigDecimal getCurrentPackBudgetSum(List<ZcEbPack> currProjPacks, String entrustCode) {
    BigDecimal currsum = BigDecimal.ZERO;
    for (Iterator iterator = currProjPacks.iterator(); iterator.hasNext();) {
      ZcEbPack temp = (ZcEbPack) iterator.next();
      if (entrustCode != null && entrustCode.equals(temp.getEntrustCode())) {
        currsum = currsum.add((BigDecimal) ObjectUtils.defaultIfNull(temp.getPackBudget(), BigDecimal.ZERO));
      }
    }
    return currsum;
  }

  private boolean checkEntrustMoney(ZcEbPack pack, BeanTableModel tableModel) {
    //任务单总预算金额：
    BigDecimal entrustMoney = pack.getEntrust().getZcMoneyBiSum();
    entrustMoney = entrustMoney == null ? BigDecimal.ZERO : entrustMoney;
    if (pack.getEntrustCode() == null) {
      return true;
    }
    //该任务单下所有已经使用的预算金额总计：
    BigDecimal sum = getSavedPackBudgetSum(pack.getEntrustCode(), pack.getProjCode());
    //该任务单可使用金额。
    BigDecimal surPlusSum = entrustMoney.subtract(sum);
    List<ZcEbPack> currProjPacks = tableModel.getDataBeanList();
    //当前项目所有标段的金额。
    BigDecimal allPackSum = getCurrentPackBudgetSum(currProjPacks, pack.getEntrustCode());
    //校验当前标段的分包预算金额是否超出任务单剩余分包预算金额
    if (allPackSum.compareTo(surPlusSum) == 1) {
      //当前标段允许分配金额=该任务单可使用金额-当前项目其他标段占用金额。
      BigDecimal bd = surPlusSum.subtract(allPackSum.subtract(pack.getPackBudget()));
      double d1 = bd.doubleValue();
      pack.setPackBudget(bd);
      JOptionPane.showMessageDialog(self, "当前【分包预算】金额大于任务单可分配【预算金额】：当前任务单允许分配金额为" + d1 + "元", "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    caculateSumMoney(currProjPacks);
    return true;
  }

  private void caculateSumMoney(List<ZcEbPack> packList) {
    BigDecimal sum = new BigDecimal(0);
    for (ZcEbPack pack : packList) {
      sum = sum.add((BigDecimal) ObjectUtils.defaultIfNull(pack.getPackBudget(), BigDecimal.ZERO));
    }
    zcEbProj.setProjSum(sum);
    this.parentJPanel.setEditingObject(zcEbProj);

  }

  private String getNewPackName(JTablePanel tablePanelPack, boolean isAdd) {
    String rt = "";
    ChangeChineseNumber ch = new ChangeChineseNumber();
    if (isAdd) {//如果是追加
      TableModel editTableModel = tablePanelPack.getTable().getModel();
      int rowCount = editTableModel.getRowCount();
      String chinese = ch.doChange(new Integer(rowCount + 1).toString());
      rt = chinese + "包";
    } else {
      BeanTableModel editTableModel = (BeanTableModel) tablePanelPack.getTable().getModel();
      int selectedRow = tablePanelPack.getTable().getSelectedRow();
      if (selectedRow != -1) {
        selectedRow = selectedRow + 2;
        String chinese = ch.doChange(new Integer(selectedRow).toString());
        rt = chinese + "包";
      } else {
        selectedRow = editTableModel.getRowCount() - 1;
        String chinese = ch.doChange(new Integer(selectedRow).toString());
        rt = chinese + "包";
      }
      List<ZcEbPack> packs = editTableModel.getDataBeanList();
      for (int i = selectedRow - 1; i < packs.size(); i++) {
        packs.get(i).setPackName(ch.doChange(new Integer(i + 2).toString()) + "包");
      }
    }
    return rt;
  }

  private void setPackDefaultValue(ZcEbPack pack, String status) {
    pack.setTempId(Guid.genID());
    pack.setStatus(status);
    pack.setProjCode(zcEbProj.getProjCode());
  }

  private void setPackReqDefaultValue(ZcEbPackReq packReq) {
    packReq.setTempId(Guid.genID());
    packReq.setProjCode(zcEbProj.getProjCode());
  }

  private void setPackSupplierDefaultValue(ZcEbPackSupplier packSup) {
    packSup.setTempId(Guid.genID());
    packSup.setProjCode(zcEbProj.getProjCode());
  }

  private void createItemInfoPanel() {
    //商品信息
    JPanel itemInfoPlanPanel = new JPanel();
    itemInfoPlanPanel.setLayout(new FlowLayout());
    JLabel itemInfoEndDateLabel = new JLabel("<html>询价结束时间<font color='red'>*</font></html>");
    itemInfoEndDateField = new DateFieldEditor("询价结束时间", "plan.bidEndTime", DateFieldEditor.TimeTypeH24);
    itemInfoPlanPanel.add(itemInfoEndDateLabel);
    itemInfoPlanPanel.add(itemInfoEndDateField);
    tablePanelItemInfo.init();
    tablePanelItemInfo.getSearchBar().setVisible(false);
    tablePanelItemInfo.setTablePreferencesKey(this.getClass().getName() + "_xunjia_table");
    JFuncToolBar bottomToolBarXunJia = new JFuncToolBar();
    JButton addBtnXunJia = new SubaddButton(false);
    JButton insertBtnXunJia = new SubinsertButton(false);
    JButton delBtnXunJia = new SubdelButton(false);
    bottomToolBarXunJia.add(addBtnXunJia);
    bottomToolBarXunJia.add(insertBtnXunJia);
    bottomToolBarXunJia.add(delBtnXunJia);
    tablePanelItemInfo.add(bottomToolBarXunJia, BorderLayout.SOUTH);
    addBtnXunJia.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 插入询价明细
        ZcEbXunJia xunJia = new ZcEbXunJia();
        JPageableFixedTable packTable = tablePanelPack.getTable();
        BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
        if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
          ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToModel(packTable.getSelectedRows()[0]));
          xunJia.setPackCode(pack.getPackCode());
          xunJia.setXjCode(ZcUtil.getSequenceNextVal(ZcSettingConstants.SEQUENCE_XUN_JIA_REQ));
          ZcUtil.addSub(tablePanelItemInfo, xunJia);
        } else {
          JOptionPane.showMessageDialog(self, "请先选择一条【分包明细】", "提示", JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    insertBtnXunJia.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 插入询价明细
        ZcEbXunJia xunJia = new ZcEbXunJia();
        JPageableFixedTable packTable = tablePanelPack.getTable();
        BeanTableModel tableModel = (BeanTableModel) packTable.getModel();
        if (packTable.getSelectedRows() != null && packTable.getSelectedRows().length > 0) {
          ZcEbPack pack = (ZcEbPack) tableModel.getBean(packTable.convertRowIndexToModel(packTable.getSelectedRows()[0]));
          xunJia.setPackCode(pack.getPackCode());
          xunJia.setXjCode(ZcUtil.getSequenceNextVal(ZcSettingConstants.SEQUENCE_XUN_JIA_REQ));
          ZcUtil.insertSub(tablePanelItemInfo, xunJia);
        } else {
          JOptionPane.showMessageDialog(self, "请先选择一条【分包明细】", "提示", JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    delBtnXunJia.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZcUtil.deleteSub(tablePanelItemInfo, self);
      }
    });
    itemInfoPanel = new JPanel();
    itemInfoPanel.setLayout(new BorderLayout());
    itemInfoPanel.add(itemInfoPlanPanel, BorderLayout.NORTH);
    itemInfoPanel.add(tablePanelItemInfo, BorderLayout.CENTER);
  }

  private void stopEditing() {
    if (tablePanelPack.getTable().isEditing()) {
      tablePanelPack.getTable().getCellEditor().stopCellEditing();
    }
    if (tablePanelPackReq.getTable().isEditing()) {
      tablePanelPackReq.getTable().getCellEditor().stopCellEditing();
    }
  }

  protected JTablePanel[] getSubTables() {
    JTablePanel[] tables = new JTablePanel[] { tablePanelPack, tablePanelPackReq, tablePanelSupplier, tablePanelPackQua };
    return tables;
  }

  public void setButtonEnabled(ZcEbProj zcEbProj) {
    if (requestMeta.getSvUserID().equals(zcEbProj.getManagerCode())) {
      addBtnPack.setEnabled(true);
      insertBtnPack.setEnabled(true);
      delBtnPack.setEnabled(true);
    } else {
      addBtnPack.setEnabled(false);
      insertBtnPack.setEnabled(false);
      delBtnPack.setEnabled(false);
    }
  }

  private class ZcEbPackHandler implements IForeignEntityHandler {

    private final String columNames[];

    private final JTable table;

    public ZcEbPackHandler(String columNames[], JTable table) {
      this.columNames = columNames;
      this.table = table;
    }

    public boolean beforeSelect(ElementConditionDto dto) {
      StringBuffer sb = new StringBuffer("' '");
      if (zcEbProj.getPackList() == null) {
        dto.setPackCode(sb.toString());
        dto.setPurType(null);
        return true;
      }
      String type = null;
      for (int i = 0; i < zcEbProj.getPackList().size(); i++) {
        ZcEbPack pk = (ZcEbPack) zcEbProj.getPackList().get(i);
        if (pk != null && pk.getPackCode() != null && !"".equals(pk.getPackCode())) {
          sb.append(",'").append(pk.getPackCode()).append("'");
          type = pk.getPurType();
        }
      }
      dto.setPackCode(sb.toString());
      dto.setPurType(type);
      return true;
    }

    @Override
    public void excute(List selectedDatas) {
      BeanTableModel model = (BeanTableModel) table.getModel();
      int k = table.getSelectedRow();
      if (k < 0)
        return;
      int k2 = table.convertRowIndexToModel(k);
      ZcEbPack pack = (ZcEbPack) model.getBean(k2);

      if (selectedDatas.size() > 0) {
        ZcEbPack selectPack = (ZcEbPack) selectedDatas.get(0);
        List packReqList = zcEbBaseServiceDelegate.queryDataForList("ZcEbProj.getZcEbPackReqListByPackCode", selectPack.getPackCode(), requestMeta);
        BeanUtil.commonFieldsCopy(selectPack, pack);
        if (packReqList != null) {
          pack.setRequirementDetailList(packReqList);
        }
        pack.setCoCode(selectPack.getEntrust().getCoCode());

        if (zcEbProj.getAgency() == null || "".equals(zcEbProj.getAgency())) {
          zcEbProj.setAgency(selectPack.getEntrust().getAgency());
        }
        zcEbProj.setCoCode(selectPack.getEntrust().getCoCode());
        zcEbProj.setPurType(selectPack.getPurType());

        zcEbProj.setManagerCode(requestMeta.getSvUserID());
        zcEbProj.setManager(requestMeta.getSvUserName());
        zcEbProj.setEmail(selectPack.getManagerEmail());
        zcEbProj.setPhone(selectPack.getManagerPhone());
        zcEbProj.setFax(selectPack.getManagerFax());

        zcEbProj.setAttn(selectPack.getAttn());
        zcEbProj.setAttnName(selectPack.getAttnName());
        zcEbProj.setAttnEmail(selectPack.getAttnEmail());
        zcEbProj.setAttnFax(selectPack.getAttnFax());
        zcEbProj.setAttnPhone(selectPack.getAttnPhone());
        zcEbProj.setZcMakeLinkman(selectPack.getEntrust().getZcMakeLinkman());

        if (parentJPanel.openDt != null) {
          if (ZcSettingConstants.PITEM_OPIWAY_XJ.equals(pack.getPurType())) {
            if (zcEbProj.getPlan() == null) {
              zcEbProj.setPlan(new ZcEbPlan());
            }
            parentJPanel.openDt.setEnabled(true);
          } else if (!ZcSettingConstants.PITEM_OPIWAY_XJ.equals(pack.getPurType())) {
            parentJPanel.openDt.setEnabled(false);
          }
        }

        parentJPanel.setEditingObject(zcEbProj);
        refreshSubPackReqTable(pack);
        addPackSupplierPanel();
        refreshSubSupplierTable(pack);
        //        refreshItemInfoTable(pack);
        refreshPackQuaTable(pack);
      }
      model.fireTableRowsUpdated(k, k);
      // 从新计算【项目总预算】
      caculateSumMoney(((BeanTableModel) tablePanelPack.getTable().getModel()).getDataBeanList());
    }

    @Override
    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        ZcEbPack rowData = (ZcEbPack) showDatas.get(i);
        int col = 0;
        data[i][col++] = CompanyDataCache.getName(rowData.getEntrust().getCoCode());
        data[i][col++] = rowData.getEntrust().getSnCode();
        data[i][col++] = rowData.getPackName();
        data[i][col++] = rowData.getPackDesc();
        data[i][col++] = rowData.getPackBudget();
        data[i][col++] = AsValDataCache.getName("ZC_VS_PITEM_OPIWAY", rowData.getPurType());
        data[i][col++] = rowData.getEntrust().getZcMakeLinkman();
        data[i][col++] = rowData.getEntrust().getZcMakeTel();
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
  }

  public void removePackSupplierPanel() {
    tabPaneReq.remove(tablePanelSupplier);
  }

  public void addPackSupplierPanel() {

    if (zcEbProj != null
      && (ZcSettingConstants.PITEM_OPIWAY_YQZB.equals(zcEbProj.getPurType()) || ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(zcEbProj.getPurType()))) {
      if (tabPaneReq.indexOfTabComponent(tablePanelSupplier) < 0) {
        tabPaneReq.addTab("供应商列表", tablePanelSupplier);
      }
    } else {
      removePackSupplierPanel();
    }

  }

  public boolean isInvited(ZcEbProj proj) {

    if (proj != null && ZcValSetConstants.VS_ZC_VS_PITEM_OPIWAY_INVITE.equals(proj.getPurType())) {

      return true;
    }

    return false;

  }

}
