/*
 * *
 *  Copyright 2012 by Beijing UFIDA Government Affairs Software Co.,Ltd.,
 *  All rights reserved.
 *
 *  版权所有：北京用友政务软件有限公司
 *  未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 *  侵权者将受到法律追究。
 * /
 */

package com.ufgov.zc.client.zc.diyuctg;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.DefaultInvokeHandler;
import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZCDiYuCtgTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;
import com.ufgov.zc.client.util.ListUtil;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.BaseException;
import com.ufgov.zc.common.system.exception.DataAlreadyDeletedException;
import com.ufgov.zc.common.system.exception.OtherException;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZCDiYuCtg;
import com.ufgov.zc.common.zc.publish.ZCDiYuCtgServiceDelegate;

/**
 * <p>PURPOSE:   地域类型
 * <p>DESCRIPTION:
 * <p>CALLED BY:	qianmingjin
 * <p>CREATE DATE: 12-3-22
 * <p>UPDATE DATE: 12-3-22
 * <p>UPDATE USER: qianmingjin
 * <p>HISTORY:		1.0
 *
 * @author qianmingjin
 * @version 1.0
 * @see
 * @since java 1.5.0
 */
public class ZCDiYuCtgListPanel extends AbstractEditListBill implements ParentWindowAware {
  private static final Logger logger = Logger.getLogger(ZCDiYuCtgListPanel.class);

  public static final String compoId = "ZC_DIYU_CTG";

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private ZCDiYuCtgListPanel self = this;

  private Window parentWindow;

  private ZCDiYuCtgServiceDelegate zcDiYuCtgserviceDelegate = (ZCDiYuCtgServiceDelegate) ServiceFactory.create(ZCDiYuCtgServiceDelegate.class,
    "zcDiYuCtgServiceDelegate");

  private ElementConditionDto elementConditionDto = new ElementConditionDto();

  private AbstractSearchConditionArea topSearchConditionArea;

  private BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(compoId);

  public BillElementMeta getBillElementMeta() {
    return billElementMeta;
  }

  public void setBillElementMeta(BillElementMeta billElementMeta) {
    this.billElementMeta = billElementMeta;
  }

  public ZCDiYuCtgServiceDelegate getZcDiYuCtgserviceDelegate() {
    return zcDiYuCtgserviceDelegate;
  }

  static {
    LangTransMeta.init("ZC%");
  }

  public Window getParentWindow() {
    return parentWindow;
  }

  public void setParentWindow(Window parentWindow) {
    this.parentWindow = parentWindow;
  }

  /**
   * 构造函数
   */
  public ZCDiYuCtgListPanel() {
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()
          .getCurrUserId(), ZcSettingConstants.TAB_ZC_DIYU_CTG);
        return needDisplaySearchConditonList;
      }

      @Override
      public void success(List<SearchCondition> needDisplaySearchConditonList) {
        List<TableDisplay> showingDisplays = SearchConditionUtil.getNeedDisplayTableDisplay(needDisplaySearchConditonList);
        init(createDataDisplay(showingDisplays), null);//调用父类方法
        revalidate();
        repaint();
      }
    });
    requestMeta.setCompoId(compoId);
  }

  /**
   * list页面顶部查询条件
   *
   * @return
   */
  private AbstractSearchConditionArea createTopConditionArea() {
    Map defaultValueMap = new HashMap();
    topSearchConditionArea = new SaveableSearchConditionArea(ZcSettingConstants.CON_ID_ZC_XYGH_SPPC, null, true, defaultValueMap, null);
    return topSearchConditionArea;
  }

  /**
   * 分页也签
   *
   * @param showingDisplays
   * @return
   */
  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {
    return new DataDisplay(SearchConditionUtil.getAllTableDisplay(ZcSettingConstants.TAB_ID_ZC_XYGH_SPPC), showingDisplays, createTopConditionArea(),
      false);//true:显示收索条件区 false：不显示收索条件区
  }

  /**
   * 为工具条添加按钮，即list页面的顶部的按钮
   */
  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(compoId);
    toolBar.add(addButton);
    toolBar.add(deleteButton);
    // 初始化按钮的action事件
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doAdd();
      }
    });
    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doDelete();
      }
    });
  }

  /**
   * 设置按钮的状态，根据list页面的Tab页签按钮显示可用不可用
   */
  private void setButtonStatus() {
    addButton.setEnabled(true);

  }

  /**
   * 刷新方法
   */
  public void refreshCurrentTabData() {
    topSearchConditionArea.doSearch();
  }

  /**
   * 刷新当前数据
   *
   * @param beanList
   */
  public void refreshCurrentTabData(List beanList) {
    topDataDisplay.getActiveTableDisplay().getTable().setModel(ZCDiYuCtgTableModelConverter.convertToTableModel(beanList));
  }

  /**
   * 获的数据bean的list
   *
   * @return
   */
  public List<ZCDiYuCtg> getCheckedList() {
    List<ZCDiYuCtg> beanList = new ArrayList<ZCDiYuCtg>();
    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();
    MyTableModel model = (MyTableModel) table.getModel();
    List<ZCDiYuCtg> list = model.getList();
    Integer[] checkedRows = table.getCheckedRows();
    for (Integer checkedRow : checkedRows) {
      int accordDataRow = table.convertRowIndexToModel(checkedRow);
      ZCDiYuCtg bean = list.get(accordDataRow);
      beanList.add(bean);
    }
    return beanList;
  }

  /***********************************************************************************************
   * 下面用来填写按钮的方法
   ***********************************************************************************************/
  /**
   * 新增方法
   */
  public void doAdd() {
    new ZCDiYuCtgDialog(this, new ArrayList(), this.topDataDisplay.getActiveTableDisplay().getTable().getRowCount(), topDataDisplay
      .getActiveTableDisplay().getStatus(), ZcSettingConstants.PAGE_STATUS_NEW);
  }

  /**
   * 删除方法
   */
  private void doDelete() {
    List checkedList = getCheckedList();
    if (checkedList.size() == 0) {
      JOptionPane.showMessageDialog(this, "请选择需要删除的数据!", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    int result = JOptionPane.showConfirmDialog(this, "是否要删除选中的数据?", "删除确认", JOptionPane.YES_NO_OPTION);
    if (result != JOptionPane.YES_OPTION) {
      return;
    }
    StringBuffer errorInfo = new StringBuffer("");
    boolean success = true;
    requestMeta.setFuncId(deleteButton.getFuncId());
    try {
      for (int i = 0; i < checkedList.size(); i++) {
        ZCDiYuCtg zcDiYuCtg = (ZCDiYuCtg) checkedList.get(i);
        zcDiYuCtgserviceDelegate.deleteZCDiYuCtgList(zcDiYuCtg.getDiYuCode(), requestMeta);
      }
    } catch (DataAlreadyDeletedException ex) {
      errorInfo.append(ex.getMessage() + "\n");
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
    } catch (BaseException ex) {
      errorInfo.append(ex.getMessage() + "\n");
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
    } catch (OtherException ex) {
      errorInfo.append(ex.getMessage() + "\n");
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
    } catch (Exception ex) {
      errorInfo.append(ex.getMessage() + "\n");
      logger.error(ex.getMessage(), ex);
      success = false;
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "删除错误!\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          UIManager.setLookAndFeel(new BlueLookAndFeel());
        } catch (Exception e) {
          e.printStackTrace();
        }
        ZCDiYuCtgListPanel bill = new ZCDiYuCtgListPanel();
        JFrame frame = new JFrame("frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }
    });
  }

  private final class DataDisplay extends MultiDataDisplay {
    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,
      boolean showConditionArea) {
      super(displays, showingDisplays, conditionArea, showConditionArea, ZcSettingConstants.TAB_ID_ZC_EXPORTTYPE);
      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate("ZC_DIYUCGT_TITLE"),
        TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
    }

    @Override
    protected void preprocessShowingTableDisplay(List<TableDisplay> showingTableDisplays) {
      for (final TableDisplay d : showingTableDisplays) {
        final JGroupableTable table = d.getTable();
        table.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
              String tabStatus = d.getStatus();
              JGroupableTable table = d.getTable();
              MyTableModel model = (MyTableModel) table.getModel();
              int row = table.getSelectedRow();
              List viewList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(model.getList(), table));
              new ZCDiYuCtgDialog(ZCDiYuCtgListPanel.this, viewList, row, tabStatus, ZcSettingConstants.PAGE_STATUS_BROWSE);
            }
          }
        });
      }
    }

    @Override
    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {
      elementConditionDto.setWfcompoId(compoId);
      elementConditionDto.setExecutor(WorkEnv.getInstance().getCurrUserId());
      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());
      elementConditionDto.setStatus(tableDisplay.getStatus());
      for (AbstractSearchConditionItem item : searchConditionItems) {
        item.putToElementConditionDto(elementConditionDto);
      }
      final Container c = tableDisplay.getTable().getParent();
      UIUtilities.asyncInvoke(new DefaultInvokeHandler<TableModel>() {
        @Override
        public void before() {
          assert c != null;
          installLoadingComponent(c);
        }

        @Override
        public void after() {
          assert c != null;
          unInstallLoadingComponent(c);
          c.add(tableDisplay.getTable());
        }

        @Override
        public TableModel execute() throws Exception {
          return ZCDiYuCtgTableModelConverter.convertToTableModel(zcDiYuCtgserviceDelegate.getZCDiYuCtgList(null, requestMeta));
        }

        @Override
        public void success(TableModel model) {

          tableDisplay.setTableModel(model);

          setButtonStatus();
        }
      });
    }
  }
}