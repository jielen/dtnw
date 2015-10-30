package com.ufgov.zc.client.zc.zcebentrust;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import com.ufgov.smartclient.common.DefaultInvokeHandler;
import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbEntrustBullToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcEbEntrustBulletinToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcEbEntrustReportToTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.zcebentrust.operation.ZcEbEntrustBulletinOpt;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;

/**

 * @ClassName: ZcEbEntrustBulletinListPanel

 * @Description: 预算单位采购任务

 * @date: Sep 17, 2012 1:47:23 PM

 * @version: V1.0

 * @since: 1.0

 * @author: yuzz

 * @modify:

 */
@SuppressWarnings({ "serial", "unchecked" })
public class ZcEbEntrustBulletinListPanel extends AbstractEditListBill implements ParentWindowAware {

  public static final String compoId = "ZC_EB_ENTRUST_BULLETIN";

  public static final String tabId = ZcSettingConstants.TAB_ID_ZcEbEntrustBulletin;

  public static final String conditionId = ZcSettingConstants.CONDITION_ID_ZcEbEntrustBulletin;

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private ElementConditionDto elementConditionDto = new ElementConditionDto();

  private AbstractSearchConditionArea topSearchConditionArea;

  private Window parentWindow = null;

  //业务操作实例
  private ZcEbEntrustBulletinOpt bullOpt = new ZcEbEntrustBulletinOpt();

  public ZcEbEntrustBulletinListPanel() {
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()
          .getCurrUserId(), tabId);
        return needDisplaySearchConditonList;
      }

      @Override
      public void success(List<SearchCondition> needDisplaySearchConditonList) {
        List<TableDisplay> showingDisplays = SearchConditionUtil.getNeedDisplayTableDisplay(needDisplaySearchConditonList);
        init(createDataDisplay(showingDisplays), null);
        revalidate();
        repaint();
      }
    });

    requestMeta.setCompoId(compoId);

  }

  public void refreshCurrentTabData() {

    topSearchConditionArea.doSearch();

  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {

    return new DataDisplay(SearchConditionUtil.getAllTableDisplay(tabId), showingDisplays, createTopConditionArea(), true);//true:显示收索条件区 false：不显示收索条件区

  }

  private AbstractSearchConditionArea createTopConditionArea() {

    topSearchConditionArea = new SaveableSearchConditionArea(conditionId, null, true, new HashMap<String, String>(), null);

    return topSearchConditionArea;

  }

  private final class DataDisplay extends MultiDataDisplay {

    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,
      boolean showConditionArea) {

      super(displays, showingDisplays, conditionArea, showConditionArea, ZcEbEntrustBulletinListPanel.tabId);

      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "采购任务", TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体",
        Font.BOLD, 15), Color.BLUE));
    }

    protected void preprocessShowingTableDisplay(List<TableDisplay> showingTableDisplays) {
      for (final TableDisplay d : showingTableDisplays) {
        final JGroupableTable table = d.getTable();
        table.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
            }
          }
        });
      }
    }

    /**
     * <p> 标签切换事件 </p>
     * @param searchConditionItems
     * @param tableDisplay
     * @see com.ufgov.zc.client.component.ui.AbstractDataDisplay#handleTableDisplayActived(com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem[], com.ufgov.zc.client.component.ui.TableDisplay)
     * @author yuzz
     * @since Sep 17, 2012 1:52:12 PM
     */
    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {

      elementConditionDto.setWfcompoId(ZcEbEntrustBulletinListPanel.compoId);
      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());
      elementConditionDto.setStatus(tableDisplay.getStatus());
      elementConditionDto.setCompoId(ZcEbEntrustBulletinListPanel.compoId);

      for (AbstractSearchConditionItem item : searchConditionItems) {
        item.putToElementConditionDto(elementConditionDto);
      }

      final Container c = tableDisplay.getTable().getParent();
      UIUtilities.asyncInvoke(new DefaultInvokeHandler<TableModel>() {
        public void before() {
          assert c != null;
          installLoadingComponent(c);
        }

        public void after() {
          assert c != null;
          unInstallLoadingComponent(c);
          c.add(tableDisplay.getTable());
        }

        public TableModel execute() throws Exception {
          if ("TO".equals(elementConditionDto.getStatus())) {
            return fetchDataForWeiTo();
          } else if ("INT".equals(elementConditionDto.getStatus())) {
            return fetchDataForIntenet();
          } else if ("COM".equals(elementConditionDto.getStatus())) {
            return fetchDataForComplete();
          } else {
            return fetchAll();
          }
        }

        public void success(TableModel model) {

          tableDisplay.setTableModel(model);

          if ("INT".equals(elementConditionDto.getStatus())) {

            ZcUtil.translateColName(tableDisplay.getTable(), ZcEbEntrustBulletinToTableModelConverter.getBulletinTableColumnInfo());

            setPackReqTableProperty(tableDisplay.getTable());

          }

        }

      });

    }

  }

  /**
   * <p> 设置标书下载控件 </p>
   * @param table 
   * @return void
   * @author yuzz
   * @since Sep 17, 2012 1:49:14 PM
   */
  private void setPackReqTableProperty(final JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    FileCellEditor fileCellEditor = new FileCellEditor("fileName", "fileId", true);
    fileCellEditor.setDeleteFileEnable(false);
    fileCellEditor.setUploadFileEnable(false);
    SwingUtil.setTableCellEditor(table, "FILE_NAME", fileCellEditor);
  }

  /**
   * <p> 已下达的采购任务 </p>
   * @return TableModel
   * @author yuzz
   * @since Sep 17, 2012 1:49:53 PM
   */
  private TableModel fetchDataForWeiTo() {
    List dataList = bullOpt.getZcEbEntrustBull(elementConditionDto, this.requestMeta);
    return ZcEbEntrustBullToTableModelConverter.convertToTableModel(dataList);
  }

  /**
   * <p> 已上网的采购任务 </p>
   * @return TableModel
   * @author yuzz
   * @since Sep 17, 2012 1:50:27 PM
   */
  private TableModel fetchDataForIntenet() {
    List dataList = bullOpt.getZcEbEntrustBullin(elementConditionDto, this.requestMeta);
    return ZcEbEntrustBulletinToTableModelConverter.convertBulletinToTableModel(dataList);
  }

  /**
   * <p> 已完成的采购任务 </p>
   * @return TableModel
   * @author yuzz
   * @since Sep 17, 2012 1:50:31 PM
   */
  private TableModel fetchDataForComplete() {
    List dataList = bullOpt.getZcEbEntrustReport(elementConditionDto, this.requestMeta);
    return ZcEbEntrustReportToTableModelConverter.convertToTableModel(dataList);
  }

  private TableModel fetchAll() {
    List dataList = bullOpt.getZcEbEntrustBull(elementConditionDto, this.requestMeta);
    return ZcEbEntrustBullToTableModelConverter.convertToTableModel(dataList);
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
        ZcEbEntrustBulletinListPanel bill = new ZcEbEntrustBulletinListPanel();
        JFrame frame = new JFrame("采购任务");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }
    });
  }

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC_DATA");
    toolBar.setCompoId(compoId);

    toolBar.add(helpButton);
  }

  public Window getParentWindow() {

    return parentWindow;
  }

  public void setParentWindow(Window parentWindow) {
    this.parentWindow = parentWindow;
  }

  static {
    LangTransMeta.init("ZC%");
  }
}
