package com.ufgov.zc.client.component.zc.dataexchange;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;

import com.ufgov.smartclient.common.DefaultInvokeHandler;
import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.common.BaseExchangeData;
import com.ufgov.zc.client.common.FileMerger;
import com.ufgov.zc.client.common.FileSeparater;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.StringToModel;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.DataExchangeToTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.HelpButton;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;
import com.ufgov.zc.client.component.zc.dataexchange.model.*;
import com.ufgov.zc.client.zc.intranetandbidnet.BidNetworkDataExpDialog;
import com.ufgov.zc.client.zc.intranetandbidnet.BidNetworkDataImpDialog;
import com.ufgov.zc.client.zc.intranetandbidnet.IntranetDataExpDialog;
import com.ufgov.zc.client.zc.intranetandbidnet.IntrantDataImpDialog;
import com.ufgov.zc.client.zc.ztb.component.ProgressGlassPane;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.BusinessException;
import com.ufgov.zc.common.zc.model.DataExchange;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.publish.IZcEbProjChangeServiceDelegate;

/**
* @ClassName: DataExChangeListPanel
* @Description: 数据交换
* @date: 2010-7-16 下午03:00:50
* @version: V1.0 
* @since: 1.0
* @author: leo
* @modify: 
*/
@SuppressWarnings({ "serial", "unchecked" })
public class DataExchangeListPanel extends AbstractEditListBill implements ParentWindowAware {

  public static final String compoId = "ZC_DATA_EXCHANGE";

  private String logDataSource = "DB";//IMP/EXP

  private Window parentWindow = null;

  private JDialog optionJDialog = new JDialog();

  private JTextArea textArea = new JTextArea();

  private DataExchangeListPanel self = this;

  private static FileMerger fileMerger = null;

  private static FileSeparater fileSeparater = new FileSeparater();

  private static Map<String, DataExchange> existDataTypeMap = null;

  //可以导出的所有数据
  private static Map<String, List<DataExchangeRedo>> exportableDataExchangeRedoMap = null;

  private Map<String, DataExchange> needExportMap = new HashMap<String, DataExchange>();

  private Map<String, Object> exportedMap = new HashMap<String, Object>();

  private Map<String, Object> importedMap = new HashMap<String, Object>();

  private ExchangeDataLogModel exchangeDataLogModel = new ExchangeDataLogModel();

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private ElementConditionDto elementConditionDto = new ElementConditionDto();

  private String statusText = null;

  private static String lastText = "";

  private static StringBuffer progressText = new StringBuffer();

  private Thread refreshThread = null;

  private static DataExchangeListPanel currentInstnce;

  //成功导出的数据计数
  private int exportedCount = 0;

  //所有可导出数据类别中，当前有数据的类别计数
  private static int dataTypeHavingDataCount = 0;

  //作业是否正在进行中
  private boolean isJobRunning = false;

  //是否强制中断并关闭了正在进行的任务
  private boolean isJobInterruptManual = false;

  private static String CONFIG_FILE_PATH = "c:/ufgov/config/dataexchange.properties";

  private Configuration config = new Configuration(CONFIG_FILE_PATH);

  private boolean isFromReexportable = false;

  public IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
    "baseDataServiceDelegate");

  public IZcEbProjChangeServiceDelegate zcEbProjectChangeServiceDelegate = (IZcEbProjChangeServiceDelegate) ServiceFactory.create(
    IZcEbProjChangeServiceDelegate.class, "zcEbProjChangeServiceDelegate");

  public DataExchangeListPanel() {
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()
          .getCurrUserId(), "ZcDataExchange_Tab");
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
    requestMeta.setCompoId(DataExchangeListPanel.compoId);
  }

  private final class DataDisplay extends MultiDataDisplay {
    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,
      boolean showConditionArea) {
      super(displays, showingDisplays, conditionArea, showConditionArea, "ZcDataExchange_Tab");
      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "数据导入/导出记录", TitledBorder.CENTER, TitledBorder.TOP, new Font(
        "宋体", Font.BOLD, 15), Color.BLUE));
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

    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {
      elementConditionDto.setWfcompoId(DataExchangeListPanel.compoId);
      elementConditionDto.setExecutor(WorkEnv.getInstance().getCurrUserId());
      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());
      elementConditionDto.setStatus(tableDisplay.getStatus());
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
          if ("REDO".equals(elementConditionDto.getStatus())) {
            return fetchDataForRedo();
          } else {
            return fetchDataForLog();
          }
        }

        public void success(TableModel model) {
          tableDisplay.setTableModel(model);
        }
      });
    }
  }

  static {
    LangTransMeta.init("ZC%");
  }

  /**
   * 读取导入导出日志情况
   * @return
   */
  private TableModel fetchDataForLog() {
    if ("IMP".equals(this.logDataSource)) {
      this.logDataSource = "DB";
      return DataExchangeToTableModelConverter.convertToTableModel(exchangeDataLogModel, exchangeDataLogModel.getImportDataList());
    } else if ("EXP".equals(this.logDataSource)) {
      this.logDataSource = "DB";
      return DataExchangeToTableModelConverter.convertToTableModel(exchangeDataLogModel, exchangeDataLogModel.getExportDataList());
    } else {
      if (elementConditionDto.getEndDate() != null) {
        elementConditionDto.setEndDate(new Date(elementConditionDto.getEndDate().getTime() + 1000 * 3600 * 24 - 1000));
      }
      exchangeDataLogModel.getLogListFromDB(elementConditionDto, requestMeta);
      return DataExchangeToTableModelConverter.convertToTableModel(exchangeDataLogModel, exchangeDataLogModel.getLogList());
    }
  }

  private TableModel fetchDataForRedo() {
    ExchangeDataRedoOperator edro = new ExchangeDataRedoOperator();
    elementConditionDto.setStatus(null);
    if (elementConditionDto.getEndDate() != null) {
      elementConditionDto.setEndDate(new Date(elementConditionDto.getEndDate().getTime() + 1000 * 3600 * 24 - 1000));
    }
    edro.doExportData(elementConditionDto, requestMeta, "");
    return DataExchangeToTableModelConverter.convertToTableModel(edro, edro.getDataList());
  }

  private AbstractSearchConditionArea topSearchConditionArea;

  private AbstractSearchConditionArea createTopConditionArea() {
    topSearchConditionArea = new SaveableSearchConditionArea("ZcDataExchangeSearch", null, true, new HashMap<String, String>(), null);

    return topSearchConditionArea;
  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {
    return new DataDisplay(SearchConditionUtil.getAllTableDisplay("ZcDataExchange_Tab"), showingDisplays, createTopConditionArea(), true);//true:显示收索条件区 false：不显示收索条件区
  }

  private HelpButton helpButton = new HelpButton();

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC_DATA");
    toolBar.setCompoId(DataExchangeListPanel.compoId);

    JButton exportButton = new JButton("导出数据");
    JButton importButton = new JButton("导入数据");
    JButton redoButton = new JButton("数据再导出");
    JButton innetExportButton = new JButton("内网数据导出");
    JButton bidNetwordImpButton = new JButton("评标专网数据导入");
    JButton bidNetwordExpButton = new JButton("评标专网数据导出");
    JButton innetImpButton = new JButton("内网数据导入");

    toolBar.add(exportButton);
    toolBar.add(importButton);
    toolBar.add(redoButton);
    //    toolBar.add(innetExportButton);
    //    toolBar.add(bidNetwordImpButton);
    //    toolBar.add(bidNetwordExpButton);
    //    toolBar.add(innetImpButton);
    toolBar.add(helpButton);

    exportButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doInit();
        doPrepareExportData();
      }
    });
    importButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doPrepareImportData();
      }
    });
    redoButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doInit2();
        doPrepareExportData();
      }
    });
    innetExportButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doInnetExportData();
      }
    });
    bidNetwordImpButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doBidNetwordImpData();
      }
    });
    bidNetwordExpButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doBidNetwordExpData();
      }
    });
    innetImpButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doInnetImpData();
      }
    });

  }

  private void doInnetImpData() {
    IntrantDataImpDialog bd = new IntrantDataImpDialog();
    bd.showDialog("内网数据导入", 620, 560);
  }

  private void doBidNetwordExpData() {
    BidNetworkDataExpDialog bd = new BidNetworkDataExpDialog();
    bd.showDialog("评标专网数据导出", 620, 560);
  }

  private void doBidNetwordImpData() {
    BidNetworkDataImpDialog i = new BidNetworkDataImpDialog();
    i.showDialog("评标专网数据导入", 620, 560);
  }

  private void doInnetExportData() {
    IntranetDataExpDialog i = new IntranetDataExpDialog();
    i.showDialog("内网数据导出", 620, 560);
  }

  /**
   * 直接从redo表中读取数据时采用这个init
   */
  private void doInit() {
    isFromReexportable = false;
    dataTypeHavingDataCount = 0;
    //获得所有的导入/导出部件及其子表，后面可以通过CompoTableRelations.getComponentsMap()来获得所有的部件
    elementConditionDto.setStatus("0");//默认查询没有导出过的数据
    BaseExchangeData.init(elementConditionDto);
    existDataTypeMap = BaseExchangeData.getExistDataTypeMap();
    exportableDataExchangeRedoMap = BaseExchangeData.getExportableDataExchangeRedoMap();
    Iterator it = exportableDataExchangeRedoMap.keySet().iterator();
    while (it.hasNext()) {
      if (exportableDataExchangeRedoMap.get(it.next()).size() > 0) {
        dataTypeHavingDataCount++;
      }
    }
  }

  /**
   * 直接从“可再导出数据”表中选择数据
   */
  private void doInit2() {
    isFromReexportable = true;
    dataTypeHavingDataCount = 0;
    BaseExchangeData.init(elementConditionDto);
    existDataTypeMap = BaseExchangeData.getExistDataTypeMap();
    this.buildExportableDataExchangeRedoMap();
    Iterator it = exportableDataExchangeRedoMap.keySet().iterator();
    while (it.hasNext()) {
      if (exportableDataExchangeRedoMap.get(it.next()).size() > 0) {
        dataTypeHavingDataCount++;
      }
    }
  }

  private void buildExportableDataExchangeRedoMap() {
    if (exportableDataExchangeRedoMap == null) {
      exportableDataExchangeRedoMap = new HashMap<String, List<DataExchangeRedo>>();
    } else {
      exportableDataExchangeRedoMap.clear();
    }
    String key = null;
    List<DataExchangeRedo> itemList = null;
    List<DataExchangeRedo> list = this.getCheckedList();
    Map map = this.distinctDataType(list);
    Iterator<String> it = map.keySet().iterator();
    while (it.hasNext()) {
      itemList = new ArrayList();
      key = it.next();
      for (int i = 0; i < list.size(); i++) {
        if (key.equals(list.get(i).getDataTypeID())) {
          itemList.add(list.get(i));
        }
      }
      exportableDataExchangeRedoMap.put(key, itemList);
    }
    removeNullDataItems(map);
  }

  private Map<String, DataExchangeRedo> distinctDataType(List<DataExchangeRedo> list) {
    Map<String, DataExchangeRedo> map = new HashMap<String, DataExchangeRedo>();
    for (int i = 0; i < list.size(); i++) {
      map.put(list.get(i).getDataTypeID(), list.get(i));
    }
    return map;
  }

  private void removeNullDataItems(Map<String, DataExchangeRedo> map) {
    String key = null;
    Iterator<String> it = existDataTypeMap.keySet().iterator();
    while (it.hasNext()) {
      key = it.next();
      if (map.get(key) == null) {
        existDataTypeMap.remove(key);
        //需要重新获取一下遍历器
        it = existDataTypeMap.keySet().iterator();
      }
    }
  }

  public List<DataExchangeRedo> getCheckedList() {
    List<DataExchangeRedo> beanList = new ArrayList<DataExchangeRedo>();
    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();
    MyTableModel model = (MyTableModel) table.getModel();
    //Modal的数据
    List list = model.getList();
    Integer[] checkedRows = table.getCheckedRows();
    for (Integer checkedRow : checkedRows) {
      int accordDataRow = table.convertRowIndexToModel(checkedRow);
      DataExchangeRedo bean = (DataExchangeRedo) list.get(accordDataRow);
      beanList.add(bean);
    }
    return beanList;
  }

  /**
   * 准备数据导出
   */
  private void doPrepareExportData() {
    getCheckBoxGroup("导出选择");
  }

  /**
   * 等实现完善后，需要将该方法移到CommonDataExchangeOpetaror中，这样一方面可以操作
   * 集中到一个类中，方便日后改进或修改，另一面将功能和界面分离，保证层次的分工，最后，
   * 这样才能够支持往后实现无人工干预的自动导入导出功能.
   * 执行具体的批量数据导出
   * @param saveDir
   */
  private String doExportDataBatch(String saveDir) {
    String className = null;
    String dataTypeID = null;
    String dataTypeName = null;
    String dataMainTab = null;
    StringBuffer buff = null;
    int count = 0;
    try {
      Object[] args = new Object[3];
      args[0] = elementConditionDto;
      args[1] = requestMeta;
      exportedMap.clear();
      buff = new StringBuffer("数据导出结果如下：\r\n");
      Iterator<String> it = needExportMap.keySet().iterator();
      while (it.hasNext()) {
        DataExchange de = needExportMap.get(it.next());
        className = de.getDataTypeExecutor();
        dataTypeID = de.getDataTypeID();
        dataTypeName = de.getDataTypeName();
        dataMainTab = de.getDataTypeMainTab();
        if (className == null) {
          throw new ClassNotFoundException();
        }
        Object instance = StringToModel.stringToInstance(className);
        Object[] id = { dataTypeID };
        StringToModel.invokeMethod(instance, "setDataTypeID", id);
        Object[] name = { "【" + dataTypeName + "】" };
        StringToModel.invokeMethod(instance, "setDataTypeName", name);
        Object[] mainTab = { dataMainTab };
        StringToModel.invokeMethod(instance, "setMainTableName", mainTab);
        Object[] redoList = { exportableDataExchangeRedoMap.get(dataTypeID).toArray() };
        StringToModel.invokeMethod(instance, "setNeedExportDataRedoList", redoList);
        args[2] = saveDir + File.separator + dataTypeID + CommonDataExchangeOperator.suffix;
        try {
          Object temp = StringToModel.invokeMethod(instance, "doExportData", args);
          if (temp instanceof Integer) {
            count = Integer.parseInt(temp.toString());
          }
        } catch (BusinessException e) {
          throw new BusinessException("【" + dataTypeName + "】数据获取失败，请检查是否存在相关的数据或文件..." + e.getMessage());
        } catch (NumberFormatException e) {
          throw new BusinessException("【" + dataTypeName + "】数据获取失败...");
        }
        if (count > 0) {
          CommonDataExchangeOperator.saveObjectToXmlFile((IBaseData) instance, args[2].toString() + File.separator + className + ".xml");
        } else {
          return buff.append("【" + dataTypeName + "】没有数据或文件需要导出...").toString();
        }
        exportedMap.put(dataTypeID, instance);
        buff.append("【");
        buff.append(dataTypeName);
        buff.append("】包含记录【");
        buff.append(count);
        buff.append("条】;\r\n");
        exportedCount += count;
      }
    } catch (ClassNotFoundException e) {
      String msg = "部件【" + dataTypeName + "】没有找到相应的类：" + className;
      JOptionPane.showMessageDialog(this, msg);
      throw new BusinessException(msg);
    } catch (IllegalAccessException e) {
      String msg = "部件【" + dataTypeName + "】参数错误：" + className;
      JOptionPane.showMessageDialog(this, msg);
      throw new BusinessException(msg);
    } catch (InstantiationException e) {
      String msg = "部件【" + dataTypeName + "】实例化时出错：" + className;
      JOptionPane.showMessageDialog(this, msg);
      throw new BusinessException(msg);
    } catch (BusinessException e) {
      throw e;
    }

    return buff.toString();
  }

  /**
   * 获得所有的可供导出的部件列表,
   * 该方法还需要重构优化；
   */
  private void getCheckBoxGroup(String title) {
    Container container = optionJDialog.getContentPane();
    container.setLayout(new BorderLayout(6, 6));
    container.removeAll();

    JPanel panel0 = new JPanel(new BorderLayout(10, 10));
    JLabel jLabel = new JLabel("请选择需要【导出】的数据:");
    panel0.add(jLabel, BorderLayout.SOUTH);
    container.add(panel0, BorderLayout.NORTH);

    JPanel middle = new JPanel(new BorderLayout());
    JPanel panel1 = new JPanel(new GridLayout(5, 0));
    final JCheckBox[] cbs = new JCheckBox[existDataTypeMap.size()];
    final JCheckBox fsCB = new JCheckBox("选中有数据项(共" + dataTypeHavingDataCount + "/" + existDataTypeMap.size() + "项)");
    fsCB.setBackground(Color.PINK);
    panel1.add(fsCB);
    this.makeSelectableCheckBox(panel1, cbs);
    this.actionFullSelectCheckBox(fsCB, cbs);
    JScrollPane checkboxesScrollPane = new JScrollPane(panel1);
    checkboxesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    checkboxesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    middle.add(checkboxesScrollPane, BorderLayout.NORTH);

    JPanel inner = new JPanel(new BorderLayout(4, 4));
    JPanel panel2 = new JPanel(new BorderLayout(4, 4));
    panel2.add(new JLabel("导出位置:"), BorderLayout.WEST);
    final JTextField savePathTF = new JTextField();
    savePathTF.setName("pathTextField");
    panel2.add(savePathTF, BorderLayout.CENTER);

    JButton jButton = this.makeBrowerButton(savePathTF, "SAVE");
    panel2.add(jButton, BorderLayout.EAST);
    inner.add(panel2, BorderLayout.NORTH);
    middle.add(inner, BorderLayout.CENTER);

    JScrollPane scrollPane = this.makeTextAreaScrollPanel();
    inner.add(scrollPane, BorderLayout.CENTER);
    container.add(middle, BorderLayout.CENTER);

    JPanel panel3 = new JPanel();
    JButton jButtonExec = new JButton("执行导出");
    jButtonExec.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        isJobInterruptManual = false;
        if (savePathTF.getText() == null || "".equals(savePathTF.getText())) {
          JOptionPane.showMessageDialog(self, "请指定导出文件的存放位置...", "提示", JOptionPane.OK_OPTION);
          return;
        }
        if (!CommonDataExchangeOperator.checkFilePath(savePathTF.getText())) {
          JOptionPane.showMessageDialog(self, "路径中存在非法字符，请检查...", "提示", JOptionPane.OK_OPTION);
          return;
        }
        DataExchangeListPanel.progressText.setLength(0);
        savePathTF.setText(self.saveHistoryDir(savePathTF.getText(), "export_dir"));
        //需要先将之前选入的清除掉
        needExportMap.clear();
        for (int i = 0; i < cbs.length; i++) {
          if (cbs[i] != null && cbs[i].isSelected()) {
            needExportMap.put(cbs[i].getName(), existDataTypeMap.get(cbs[i].getName()));
          }
        }
        //将全选项去除掉
        needExportMap.remove("fullSelect");
        if (needExportMap.size() == 0) {
          JOptionPane.showMessageDialog(self, "请选择需要导出的数据...");
          return;
        }
        makeProgressGlassPane("EXP", savePathTF.getText());
      }
    });
    panel3.add(jButtonExec);
    container.add(panel3, BorderLayout.SOUTH);

    settingOptionJDialog(title, 620, 560);
    exportedCount = 0;
  }

  /**
   * 执行所有导出相关任务，包括数据抓取、文件抓取、文件合并加密、遗留文件清理等
   * @param topDir
   * @param glassPane
   */
  private void executeExportTask(String topDir, ProgressGlassPane glassPane) {
    glassPane.getTimer().start();
    isJobRunning = true;
    String info = null;

    try {
      statusText = "                  开始进行数据和文件抓取，请稍候...";
      DataExchangeListPanel.setProgressText(statusText.trim());
      info = doExportDataBatch(topDir);
      if (exportedCount == 0) {
        statusText = "                  没有查询到相关的数据，无需导出...";
        DataExchangeListPanel.setProgressText(statusText.trim());
        modifyExportedRecordStatus();
        glassPane.getTimer().stop();
        glassPane.setVisible(false);
        isJobRunning = false;
        return;
      }
      //合并文件
      doMergerFiles(topDir);
      //善后处理，包括删除压缩前的目录及压缩包，只剩下加密后的包,提示用户文件导出完毕和文件存放的位置
      doFinishExport(topDir, info);
      DataExchangeListPanel.setProgressText("           所有数据导出过程成功完成！");
      glassPane.getTimer().stop();
      glassPane.getProgressBar().setValue(100);
      glassPane.getProgressInfo().setHorizontalAlignment(SwingUtilities.CENTER);
      glassPane.setVisible(false);
      isJobRunning = false;
      refreshThread = null;
    } catch (BusinessException e) {
      glassPane.getTimer().stop();
      glassPane.setVisible(false);
      CommonDataExchangeOperator.deleteFiles(topDir);
      if (!isJobInterruptManual) {
        JOptionPane.showMessageDialog(self, e.getMessage());
      }
      isJobRunning = false;
      refreshThread = null;
    }
    isJobInterruptManual = true;
  }

  /**
   * 给浏览按钮添加事件
   * @param button
   * @param textField
   */
  private JButton makeBrowerButton(final JTextField textField, final String flag) {
    JButton button = new JButton("浏览");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if ("OPEN".equals(flag)) {
          textField.setText(showJFileOpenChooser());
        } else {
          textField.setText(showJFileSaveChooser());
        }
        optionJDialog.toFront();
      }
    });
    return button;
  }

  /**
   * 创建所有的复选框
   * @param panel
   * @param cbs
   */
  private void makeSelectableCheckBox(JPanel panel, JCheckBox[] cbs) {
    int i = 0;
    String label = null;
    Iterator<String> it = existDataTypeMap.keySet().iterator();
    while (it.hasNext()) {
      DataExchange de = existDataTypeMap.get(it.next());
      if (exportableDataExchangeRedoMap.get(de.getDataTypeID()) == null) {
        continue;
      }
      int count = exportableDataExchangeRedoMap.get(de.getDataTypeID()).size();
      label = de.getDataTypeName() + "(" + count + "条)";
      cbs[i] = new JCheckBox(label);
      cbs[i].setName(de.getDataTypeID());
      cbs[i].setBackground(count > 0 ? Color.GREEN : null);
      if (isFromReexportable) {
        cbs[i].setSelected(true);
      }
      panel.add(cbs[i++]);
    }
  }

  /**
   * 给全选checkbox添加全选响应事件
   * @param fsCB
   * @param cbs
   */
  private void actionFullSelectCheckBox(final JCheckBox fullSelectCB, final JCheckBox[] cbs) {
    fullSelectCB.setName("fullSelect");
    fullSelectCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < cbs.length; i++) {
          if (cbs[i] == null || exportableDataExchangeRedoMap.get(cbs[i].getName()) == null) {
            continue;
          }
          //只选中有数据的部分
          if (exportableDataExchangeRedoMap.get(cbs[i].getName()).size() > 0) {
            cbs[i].setSelected(fullSelectCB.isSelected());
          } else {
            cbs[i].setSelected(false);
          }
        }
      }
    });
    for (int i = 0; i < cbs.length; i++) {
      final JCheckBox cb = cbs[i];
      if (cbs[i] == null) {
        continue;
      }
      cbs[i].addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (!cb.isSelected()) {
            fullSelectCB.setSelected(false);
          }
        }
      });
    }
  }

  /**
   * 构建一个新的JTextArea
   * @param jTextArea
   */
  private JScrollPane makeTextAreaScrollPanel() {
    textArea = null;
    textArea = new JTextArea();
    textArea.setLayout(null);
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    return new JScrollPane(textArea);
  }

  /**
   * 配置窗口相关参数
   * @param title
   * @param width
   * @param height
   */
  private void settingOptionJDialog(String title, int width, int height) {
    optionJDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    optionJDialog.setTitle(title);
    optionJDialog.setSize(width, height);
    optionJDialog.setLocationRelativeTo(null);//自动居中显示
    optionJDialog.setModal(true);
    optionJDialog.setResizable(false);
    optionJDialog.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent arg0) {
        if (isJobRunning) {
          int select = JOptionPane.showConfirmDialog(self, "任务正在运行，是否退出？");
          if (select != 0) {
            return;
          }
        }
        if ((optionJDialog.getRootPane().getGlassPane()) instanceof ProgressGlassPane && refreshThread != null) {
          ((ProgressGlassPane) (optionJDialog.getRootPane().getGlassPane())).getTimer().stop();
          ((ProgressGlassPane) (optionJDialog.getRootPane().getGlassPane())).setVisible(false);
        }
        isJobRunning = false;
        isJobInterruptManual = true;
        optionJDialog.setVisible(false);
      }
    });

    //界面有关参数设置必须在setVisible之前，否则不起作用;
    optionJDialog.setVisible(true);
  }

  /**
   * 合并文件
   * @param rootPath
   */
  private void doMergerFiles(String rootPath) {
    statusText = "                  数据和文件抓取完成，正在进行合并加密，请稍候...";
    DataExchangeListPanel.setProgressText(statusText.trim());
    fileMerger = new FileMerger();
    fileMerger.mergerFiles(rootPath, rootPath + ".tdes");
  }

  /**
   * 拆分还原文件
   * @param tdesFilePath
   */
  private void doSeparateFiles(String tdesFilePath) {
    try {
      fileSeparater.separaterFiles(tdesFilePath, tdesFilePath.substring(0, tdesFilePath.indexOf(".")));
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "文件被破坏，无法处理...", "提示", JOptionPane.ERROR_MESSAGE);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "文件读取出错，无法处理...", "提示", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * 善后处理：
   * 1、删除压缩前的文件及目录；
   * 2、提示用户文件存放的位置，最好提供一个点击链接操作
   * @param rootPath
   */
  private void doFinishExport(String rootPath, String info) {
    statusText = "                  合并加密完成，正在清理遗留文件，请稍候...";
    DataExchangeListPanel.setProgressText(statusText.trim());
    deleteFiles(rootPath);
    modifyExportedRecordStatus();
    showExportedLog();
    showFinishedTip(rootPath, ".tdes", info);
    DataExchangeListPanel.setProgressText("数据导出完成...");
  }

  /**
   * 删除遗留文件
   * @param path
   */
  private void deleteFiles(String path) {
    Object temp = config.getValue("is_delete_file");
    if ("".equals(temp) || "true".equals(temp) || (temp instanceof Boolean && (Boolean) temp)) {
      CommonDataExchangeOperator.deleteFiles(path);
    }
    if ("".equals(temp) || !(temp instanceof Boolean)) {
      config.setValue("is_delete_file", "true");
      config.saveFile(CONFIG_FILE_PATH, "config for data_exchange");
    }
  }

  /**
   * 数据导出完毕后，做必要的善处理，例如修改已经导出数据的状态为已经到导出
   */
  private void modifyExportedRecordStatus() {
    Object[] args = new Object[3];
    args[0] = this.elementConditionDto;
    args[1] = this.requestMeta;
    args[2] = "";
    Iterator<String> it = exportedMap.keySet().iterator();
    while (it.hasNext()) {
      Object instance = exportedMap.get(it.next());
      StringToModel.invokeMethod(instance, "doModifyDataStatus", args);
    }
  }

  /**
   * 导出完毕后，显示本次导出的结果
   */
  private void showExportedLog() {
    this.logDataSource = "EXP";
    exchangeDataLogModel.getExportDataList().clear();
    Iterator<String> it = exportedMap.keySet().iterator();
    while (it.hasNext()) {
      Object instance = exportedMap.get(it.next());
      ExchangeDataLogModel edlm = (ExchangeDataLogModel) StringToModel.invokeMethod(instance, "getExchangeDataLogModel");
      exchangeDataLogModel.getExportDataList().addAll(edlm.getExportDataList());
    }
    exchangeDataLogModel.saveDataToDB(exchangeDataLogModel.getExportDataList(), requestMeta);
    refreshCurrentTabData();
  }

  /**
   * 导入完毕后，显示导入日志
   */
  private void showImportedLog() {
    this.logDataSource = "IMP";
    exchangeDataLogModel.getImportDataList().clear();
    Iterator<String> it = importedMap.keySet().iterator();
    while (it.hasNext()) {
      Object instance = importedMap.get(it.next());
      ExchangeDataLogModel edlm = (ExchangeDataLogModel) StringToModel.invokeMethod(instance, "getExchangeDataLogModel");
      exchangeDataLogModel.getImportDataList().addAll(edlm.getImportDataList());
    }
    exchangeDataLogModel.saveDataToDB(exchangeDataLogModel.getImportDataList(), requestMeta);
    refreshCurrentTabData();
  }

  /**
   * 完成了，将文件存储路径告诉用户，用户可以通过点击关联直接进入到文件目录
   * @param filePath
   */
  private void showFinishedTip(String filePath, String suffix, String info) {
    JOptionPane.showMessageDialog(this, info + "\r\n请查看:" + filePath + suffix);
  }

  /**
   * 打开导出目录选择器
   * @return
   */
  private String showJFileSaveChooser() {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogType(JFileChooser.SAVE_DIALOG);
    chooser.setDialogTitle("导出并存到");
    String path = config.getValue("export_dir");
    if (path == null || "".equals(path)) {
      path = "c:/";
    }
    chooser.setCurrentDirectory(new File(path));
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int r = chooser.showSaveDialog(this);
    if (r == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile().getPath();
    }

    return new File(".").getAbsolutePath();
  }

  /**
   * 准备数据导入
   */
  private void doPrepareImportData() {
    Container container = optionJDialog.getContentPane();
    container.setLayout(new BorderLayout(12, 8));
    container.removeAll();

    JPanel panel1 = new JPanel(new BorderLayout(10, 10));
    JLabel label = new JLabel("请指定需要导入的数据包(.tdes文件)：");
    panel1.add(label, BorderLayout.SOUTH);
    container.add(panel1, BorderLayout.NORTH);

    JPanel middle = new JPanel(new BorderLayout(4, 6));
    JPanel panel2 = new JPanel(new BorderLayout(4, 6));
    panel2.add(new JLabel("导入来源："), BorderLayout.WEST);
    final JTextField importPathTF = new JTextField();
    importPathTF.setName("pathTextField");
    panel2.add(importPathTF, BorderLayout.CENTER);

    JButton jButton = this.makeBrowerButton(importPathTF, "OPEN");
    panel2.add(jButton, BorderLayout.EAST);
    middle.add(panel2, BorderLayout.NORTH);

    JScrollPane scrollPane = this.makeTextAreaScrollPanel();
    middle.add(scrollPane, BorderLayout.CENTER);
    container.add(middle, BorderLayout.CENTER);

    JPanel panel3 = new JPanel();
    JButton jButtonExec = new JButton("执行导入");
    jButtonExec.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (importPathTF.getText() == null || "".equals(importPathTF.getText()) || !importPathTF.getText().endsWith(".tdes")) {
          JOptionPane.showMessageDialog(self, "请指定导入文件的存放位置...", "提示", JOptionPane.OK_OPTION);
          return;
        }
        if (!CommonDataExchangeOperator.checkFilePath(importPathTF.getText())) {
          JOptionPane.showMessageDialog(self, "路径中存在非法字符，请检查...", "提示", JOptionPane.OK_OPTION);
          return;
        }
        DataExchangeListPanel.progressText.setLength(0);
        importPathTF.setText(self.saveHistoryDir(importPathTF.getText(), "import_dir"));
        makeProgressGlassPane("IMP", importPathTF.getText());
      }
    });
    panel3.add(jButtonExec);
    container.add(panel3, BorderLayout.SOUTH);

    settingOptionJDialog("导入数据", 400, 320);
  }

  /**
   * 创建进度条和一个线程，用于刷新滚动进度情况
   * @param flag
   * @param filePath
   */
  private void makeProgressGlassPane(final String flag, final String filePath) {
    final ProgressGlassPane glassPane = new ProgressGlassPane() {
      private static final long serialVersionUID = 1L;

      public int getProgressBarValue() {
        while (getTimer().isRunning()) {
          return (int) (Math.random() * 100);
        }
        return 100;
      }

      public String getProgressBarString() {
        publishProgressText();
        isJobRunning = true;
        return statusText;
      }
    };
    optionJDialog.getRootPane().setGlassPane(glassPane);
    glassPane.addMouseListener(new MouseAdapter() {
    });
    glassPane.addMouseMotionListener(new MouseMotionAdapter() {
    });
    glassPane.getProgressBar().setPreferredSize(new Dimension(200, 20));
    glassPane.getProgressBar().setBackground(Color.RED);
    glassPane.setVisible(true);
    refreshThread = new Thread(new Runnable() {
      public void run() {
        if ("IMP".equals(flag)) {
          executeImportTask(filePath, glassPane);
        } else {
          executeExportTask(filePath, glassPane);
        }
        //        while (!isJobInterruptManual) {
        //        }
      }
    });
    refreshThread.start();
  }

  /**
   * 执行所有导入任务，包括拆分还原文件、发送文件到服务器、清理遗留文件;
   * @param tdesPath
   * @param glassPane
   */
  private void executeImportTask(String tdesPath, ProgressGlassPane glassPane) {
    glassPane.getTimer().start();
    isJobRunning = true;
    try {
      //拆分还原文件
      statusText = "                  开始文件解密，请稍候...";
      doSeparateFiles(tdesPath);
      //开始真正的执行批量数据导出
      statusText = "                  文件解密完成，开始发送数据到服务器，请稍侯...";
      String rootDir = tdesPath.substring(0, tdesPath.indexOf("."));
      String info = doImportDataBatch(rootDir);
      statusText = "                  文件发送完成，开始清理遗留文件，请稍候...";
      //善后处理,提示用户导入成功，并显示导入日志
      doFinishImport(rootDir, info);
      DataExchangeListPanel.setProgressText("所有数据导入工作成功完成...");
      statusText = "                  所有数据导入工作成功完成！";
      glassPane.getTimer().stop();
      glassPane.getProgressBar().setValue(100);
      glassPane.getProgressInfo().setHorizontalAlignment(SwingUtilities.CENTER);
      glassPane.setVisible(false);
      isJobRunning = false;
    } catch (Exception e) {
      e.printStackTrace();
      glassPane.getTimer().stop();
      glassPane.setVisible(false);
      if (!isJobInterruptManual) {
        JOptionPane.showMessageDialog(null, "解密解压失败..." + e.getMessage());
      }
      isJobRunning = false;
    }
  }

  /**
   * 将过程信息写到滚动窗口中；
   */
  private void publishProgressText() {
    textArea.setText(DataExchangeListPanel.progressText.toString());
  }

  /**
   * 执行导入操作
   * @param rootDir
   */
  private String doImportDataBatch(String rootDir) {
    File file = new File(rootDir);
    if (!file.exists()) {
      JOptionPane.showMessageDialog(this, "未找到文件:" + rootDir, "提示", JOptionPane.OK_OPTION);
      return "未找到文件:" + rootDir;
    }

    Object[] args = new Object[3];
    args[0] = elementConditionDto;
    args[1] = requestMeta;
    args[2] = rootDir;

    int count = 0;
    String dataTypeName = null;
    IBaseData instance = null;
    StringBuffer expInfo = new StringBuffer("数据导入结果如下：\r\n");
    File[] files = file.listFiles();
    for (int i = 0; i < files.length; i++) {//轮询所有部件目录
      dataTypeName = files[i].getName();
      if (dataTypeName.endsWith("UFIDA_COMPO")) {
        File temp = new File(files[i].getAbsolutePath());
        File[] subFiles = temp.listFiles();
        for (int j = 0; j < subFiles.length; j++) {//轮询每个部件目录下的文件,以找到数据抓取具体类的xml文件
          if (subFiles[j].getName().endsWith(".xml")) {
            instance = (IBaseData) CommonDataExchangeOperator.readXmlFileToObject(instance, subFiles[j].getAbsolutePath());
//            int total = ((List) StringToModel.invokeMethod(instance, "getDataList")).size();
            count = (Integer) StringToModel.invokeMethod(instance, "doImportData", args);
            this.importedMap.put(StringToModel.invokeMethod(instance, "getDataTypeID").toString(), instance);
            int suffixLen = CommonDataExchangeOperator.suffix.length();
            expInfo.append("【").append(dataTypeName.substring(0, dataTypeName.length() - suffixLen));
            expInfo.append("】共有【").append(count).append("条】记录导入.\r\n");
          }
        }
      }
    }

    return expInfo.toString();
  }

  /**
   * 导入完成后进行的善后处理，包括文件的删除、导入日志的显示等操作
   * @param rootPath
   */
  private void doFinishImport(String rootPath, String info) {
    deleteFiles(rootPath);
    showImportedLog();
    JOptionPane.showMessageDialog(this, info);
  }

  /**
   * 显示文件导入位置选择框
   * @return
   */
  private String showJFileOpenChooser() {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("从文件导入数据");
    chooser.setDialogType(JFileChooser.OPEN_DIALOG);
    String path = config.getValue("import_dir");
    if (path == null || "".equals(path)) {
      path = "c:\\";
    }
    chooser.setCurrentDirectory(new File(path));
    chooser.setFileFilter(new FileFilter() {
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".tdes") || f.isDirectory();
      }

      public String getDescription() {
        return "";
      }
    });
    int r = chooser.showOpenDialog(this);
    if (r == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile().getPath();
    }

    return new File(".").getAbsolutePath();
  }

  /**
   * 记忆用户本次使用的目录,下次自动定位到这个目录：
   * 1、(1.1)如果用户指定的目录下已经有文件，那么将该目录记忆下来，同时需要根据当天时间创建一个新的目录，作为
   *         本次导出存放文件的目录;
   *    (1.2)如果目录下没有文件，而且不是以:/或者:\\结尾，那么直接使用该目录作为本次导出存放文件的目录，同时
   *         取该目录的父级目录作为记忆目录，如果没有文件，但是以:/或者:\\结尾，那么需要创建一个新的目录，作
   *         为本次导出存放文件的目录，记忆根盘符目录作为记忆目录；
   * 2、如果目录不存在或者目录下为空，那么指定该目录的父级目录中已经存在的目录为记忆(理论上需要逐级往上判断来
   *    确定，但是因为后面会自动创建不存在的目录，所以这里只需要往上一级就可以了)，此时，该目录肯定是用户新建
   *    的目录，无需再做其它判断；
   * @param dir
   * @param key
   */
  public String saveHistoryDir(String dirValue, String key) {
    String parent = dirValue;
    if (!dirValue.endsWith(".tdes")) {
      dirValue = dirValue + File.separator + CommonDataExchangeOperator.getDateFromToday(0, "yyyyMMdd");
    }
    File file = new File(dirValue);
    if (file.exists()) {
      if (file.isDirectory()) {
        if (file.listFiles().length > 0) {
          parent = dirValue;
          dirValue = dirValue + File.separator + CommonDataExchangeOperator.getDateFromToday(0, "yyyyMMdd");
        } else {
          if (dirValue.endsWith(":/") || dirValue.endsWith(":\\")) {
            parent = dirValue.substring(0, dirValue.indexOf(":") + 2);
            dirValue = dirValue + File.separator + CommonDataExchangeOperator.getDateFromToday(0, "yyyyMMdd");
          } else {
            parent = file.getParentFile().getAbsolutePath();
          }
        }
      } else {
        parent = file.getParentFile().getAbsolutePath();
      }
    } else {
      parent = file.getParentFile().getAbsolutePath();
    }
    config.setValue(key, parent);
    config.saveFile(CONFIG_FILE_PATH, "config for data_exchange");
    dirValue = (new File(dirValue)).getAbsolutePath();
    return dirValue;
  }

  public void refreshCurrentTabData() {
    topSearchConditionArea.doSearch();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          //UIManager.setLookAndFeel(new BlueLookAndFeel());
        } catch (Exception e) {
          e.printStackTrace();
        }
        DataExchangeListPanel bill = new DataExchangeListPanel();
        JFrame frame = new JFrame("数据交换");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }
    });
  }

  public void setStatusText(String text) {
    statusText = text;
  }

  public static void setProgressText(String text) {
    //因为线程是定时发送消息的，所以有可能发送的是上一次发送过的消息，这里进行过滤
    if (!lastText.equals(text)) {
      progressText.append(text);
      progressText.append("\r\n");
      lastText = text;
    }
    if (currentInstnce != null) {
      currentInstnce.publishProgressText();
    }
  }

  public Window getParentWindow() {
    return parentWindow;
  }

  public void setParentWindow(Window parentWindow) {
    this.parentWindow = parentWindow;
  }

  public RequestMeta getRequestMeta() {
    return requestMeta;
  }
}