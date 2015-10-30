package com.ufgov.zc.client.zc.project.integration.zbbook;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.ufgov.zc.client.zc.project.integration.zbbook.services.PackCopyTemplateService;
import com.ufgov.zc.client.zc.ztb.component.MainPanel;
import com.ufgov.zc.client.zc.ztb.component.ProjectInfoPanel;
import com.ufgov.zc.client.zc.ztb.component.ZTBButton;
import com.ufgov.zc.client.zc.ztb.component.service.TemplateCopyService;
import com.ufgov.zc.client.zc.ztb.dao.AsFileDao;
import com.ufgov.zc.client.zc.ztb.dao.ReadObjectFileToObjectDao;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;
import com.ufgov.zc.client.zc.ztb.service.FileExportService;
import com.ufgov.zc.client.zc.ztb.table.builder.GridColumnBuilder;
import com.ufgov.zc.client.zc.ztb.table.entity.GridColumn;
import com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTable;
import com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTableColumnModel;
import com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTableModel;
import com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTableRowSorter;
import com.ufgov.zc.client.zc.ztb.util.FileChooseFilter;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.client.zc.ztb.util.PubFunction;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.zc.model.ZcZBFileTemplate;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @author LEO
 *         在招标书引用标书模板时，创建标书模板列表
 */
public class TemplateCopyPanel extends JPanel {
  private static final long serialVersionUID = -6487099716031196715L;

  private PackCopyTemplateService packCopyTemplateService;

  private JTableModel tableModel;

  private com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTable table;

  private MainPanel mainPanel;

  private JDialog parentDialog = null;

  //用于校验用户选择的模板是否合适
  private SmartTreeNode currSelectedNode = null;

  private AsFileDao asFileDao = AsFileDao.getInstance();

  //标签页 数据库   本地
  private JTabbedPane jTabbedPane = null;

  private int jTabbedPaneFlag = 0;

  Map<Integer, JTable> tableMap = new HashMap<Integer, JTable>();

  Map<String, List<Map<String, String>>> tplListMap = new HashMap<String, List<Map<String, String>>>();;

  private String tplbelongbidway = "";

  private FileExportService exportTbFileService = new FileExportService();

  private static XStream xstream = new XStream(new DomDriver());

  private JPanel jPanel = null;

  private JLabel tipLabel = new JLabel();
///从本地导入模板到模板库 
  ZTBButton chooeseTemplateBtn = new ZTBButton("chooeseTemplateBtn");

  public TemplateCopyPanel(MainPanel mainPanel, SmartTreeNode treeNode, JDialog parentDialog) throws Exception {
    this.mainPanel = mainPanel;
    this.parentDialog = parentDialog;
    currSelectedNode = treeNode;
    packCopyTemplateService = new PackCopyTemplateService();
    if (ProjectInfoPanel.getBusinessProject() != null) {
      tplbelongbidway = ProjectInfoPanel.getBusinessProject().getPurType();
    }
    buildPanel();
    fillPanelWithData();
  }

  private void buildPanel() {
    this.setLayout(new BorderLayout());
    //选用当前选中模板
    ZTBButton compTemplateBtn = new ZTBButton("copyTemplate");
    JToolBar toolBar = new JToolBar();
    toolBar.setFloatable(true);
    compTemplateBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        copyTemplateListener(e);
      }
    });
    toolBar.add(compTemplateBtn);
    this.add(toolBar, BorderLayout.NORTH);
    try {
      //标签页
      jTabbedPane = new JTabbedPane();
      jTabbedPane.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
          jTabbedPaneFlag = jTabbedPane.getSelectedIndex();
          table = tableMap.get(jTabbedPaneFlag);
          if (table == null) {
            return;
          }
          tableModel = (JTableModel) table.getModel();
        }
      });
      chooeseTemplateBtn.setPreferredSize(new Dimension(128, 30));
      chooeseTemplateBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          chooseChangeListener(e);
        }
      });
      JScrollPane scrollpane = new JScrollPane(jTabbedPane);
      this.add(scrollpane, BorderLayout.CENTER);
      tipLabel.setPreferredSize(new Dimension(800, 40));
      this.add(tipLabel, BorderLayout.SOUTH);
    } catch (Exception e1) {
      GV.showMessageDialog(this.mainPanel, e1.getMessage());
      e1.printStackTrace();
    }
  }

  public void fillPanelWithData() {
    jTabbedPane.removeAll();
    JTable jTableDataBase = null;
    JTable jTableLocalData = null;
    try {
      try {
        jTableDataBase = this.createTable("DataBase", tplbelongbidway, true);
      } catch (Exception e) {
        tipLabel.setText(getHtmlText("数据库模板列表创建失败，原因：\n" + e.getMessage()));
        jTableDataBase = this.createTable("DataBase", tplbelongbidway, false);
      }
      jTableLocalData = this.createTable("LocalData", tplbelongbidway, true);
      tableMap.put(0, jTableLocalData);
      tableMap.put(1, jTableDataBase);
    } catch (Exception e) {
      e.printStackTrace();
      tipLabel.setText(getHtmlText("模板数据初始化失败，错误如下：\n" + e.getMessage()));
      return;
    }
    if (jPanel != null) {
      jPanel.removeAll();
    } else {
      jPanel = new JPanel();
    }
    jPanel.setLayout(new BorderLayout());
    JPanel jPanel2 = new JPanel(new BorderLayout());
    jPanel2.add(chooeseTemplateBtn, BorderLayout.WEST);
    jPanel.add(jPanel2, BorderLayout.NORTH);
    JScrollPane jPanelLocalF = new JScrollPane(jTableLocalData);
    jPanel.add(jPanelLocalF, BorderLayout.CENTER);
    JScrollPane jPanelData = new JScrollPane(jTableDataBase);
    JScrollPane jPanelLocal = new JScrollPane(jPanel);
    jTabbedPane.add(jPanelLocal, "本地模板");
    jTabbedPane.add(jPanelData, "数据库模板");
    this.invalidate();
    this.repaint();
  }

  public String getHtmlText(String msg) {
    StringBuffer buff = new StringBuffer();
    buff.append("<html><a><font size='3' color='red'>");
    buff.append(msg);
    buff.append("</font></a></html>");
    return buff.toString();
  }

  public void copyTemplateListener(ActionEvent e) {
    if (table.getCheckSelectRows().length == 0) {
      GV.showMessageDialog(null, GV.getOperateMsg("copyTemplate.nodata", null));
      return;
    }
    if (table.getCheckSelectRows().length > 1) {
      GV.showMessageDialog(null, GV.getOperateMsg("copyTemplate.twoOrMoreData", null));
      return;
    }
    int selectedRow = table.getCheckSelectRows()[0];
    //项目对应的招标方式    导入模板的招标方式和项目对应的招标方式不一致
    if (jTabbedPaneFlag == 1) {
      //从服务器来数据
      try {
        doImportTemplateFromDB(selectedRow);
      } catch (Exception e1) {
        GV.showMessageDialog(null, "模板导入出错，原因：" + e1.getMessage());
      }
    } else if (jTabbedPaneFlag == 0) {
      //从本地来的数据
      try {
        doImportTemplate(selectedRow);
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } else {
      GV.showMessageDialog(null, "系统错误，没有可选择的数据！请重启软件再试！");
      return;
    }
  }

  public void chooseChangeListener(ActionEvent e) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setFileFilter(new FileChooseFilter(GV.TEMPLATE_ZIP));
    fileChooser.setCurrentDirectory(new File(GV.getHistoryDirectory()));
    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      GV.setHistoryDirectory(selectedFile.getParent());
      String tempFileName = selectedFile.getName();
      ZipFile zipFile = null;
      try {
        zipFile = new ZipFile(selectedFile, GV.FILE_CHAR_CODE);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      // 公开招标_TPL  招标方式为分类，如果不存在这个分类的模板，那么需要创建一个文件夹：招标方式_TPL
      tempFileName = tempFileName.substring(0, tempFileName.length() - 4);
      String templateFilePath = GV.USER_DIR + File.separator + GV.EXPORT_FILE_DIR + File.separator + tempFileName;
      PubFunction.checkAndMakeDirs(templateFilePath);
      try {
        unzipFileToDestDir(zipFile, templateFilePath);
      } catch (Exception e3) {
        JOptionPane.showMessageDialog(null, "模板解压时出错，原因：" + e3.getMessage());
        return;
      }
      FileInputStream fis = null;
      InputStreamReader is = null;
      ZcZBFileTemplate template = new ZcZBFileTemplate();
      try {
        fis = new FileInputStream(templateFilePath + File.separator + GV.TEMPLATE_CONFIG_XML);
        is = new InputStreamReader(fis, GV.XML_CHAR_CODE);
        template = (ZcZBFileTemplate) xstream.fromXML(is);
        fis.close();
        is.close();
      } catch (Exception ee) {
        if (fis != null) {
          try {
            fis.close();
          } catch (Exception e9) {
            e9.printStackTrace();
          }
        }
        if (is != null) {
          try {
            is.close();
          } catch (Exception e9) {
            e9.printStackTrace();
          }
        }
        ee.printStackTrace();
        return;
      }
      String tplFullPath = GV.USER_DIR + File.separator + GV.TEMPLATE_FILE_DIR + File.separator + template.getTplBelongBidWay() + "_TPL"
        + File.separator + template.getTplAppType() + GV.TEMPLATE_APPTYPE + File.separator + template.getTplNo() + File.separator;
      PubFunction.checkAndMakeDirs(tplFullPath);
      boolean succ = TemplateCopyService.doCopyTemplateFiles(templateFilePath, tplFullPath, false);
      succ = doUpdateSmartTreeNode();
      if (succ) {
        ReadObjectFileToObjectDao.generateTemplateInfoFileByDirectoryStruct(GV.TEMPLATE_FILE_DIR + File.separator);
        PubFunction.deleteFile(templateFilePath);
        this.fillPanelWithData();
        GV.showMessageDialog(null, "模板成功导入到本地模板库！");
      }
    }
  }

  /**
   * 现将模板覆盖掉现有的结构，重新写配置文件，刷新树结构，自动将项目信息填充到模板上
   *
   * @param selectedRow {HISTORY=暂无使用！, TPLAPPTYPE=货物类, TPLNO=模板_1, TPL_PATH=\公开招标_TPL\货物类_APT, DESCRIPTION=本套模板主要适用于货物类.,
   *                    TABLE.CHECKBOX=Y, TPLPROJECTNAME=null,
   *                    TPLBELONGBIDWAY=公开招标, LATESTUSED=2010-12-26 16:04:42}
   */
  private void doImportTemplate(int selectedRow) throws Exception {
    Map<String, String> rowMap = tableModel.get(selectedRow);
    String tplCode = rowMap.get("TPLNO");
    String tplBelongBidWay = rowMap.get("TPLBELONGBIDWAY");
    String tplAppTye = rowMap.get("TPLAPPTYPE");
    String latestUsed = rowMap.get("LATESTUSED");
    String description = rowMap.get("description".toUpperCase());
    String templateFilePath = getTemplateFileDirectory(tplBelongBidWay, tplAppTye, tplCode);
    File fileTempInfoFile = new File(templateFilePath + GV.TEMPLATE_CONFIG_XML);
    if (!fileTempInfoFile.exists()) {
      ZcZBFileTemplate template = new ZcZBFileTemplate();
      template.setTplNo(tplCode);
      template.setTplBelongBidWay(tplBelongBidWay);
      template.setTplAppType(tplAppTye);
      DateFormat format = new SimpleDateFormat(ZcSettingConstants.SIMPLE_DATE_FORMAT_DATE_ONLY);
      Date date = format.parse(latestUsed);
      template.setLatestUsed(date);
      template.setDescription(description);
      exportTbFileService.createXMLFile(templateFilePath, template);
    }
    try {
      doExecuteCopying(templateFilePath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean doExecuteCopying(String templateFilePath) {
    boolean succ = true;
    boolean isNeedDelete = false;
    Object[] message;
    SmartTreeNode projNode = (SmartTreeNode) this.currSelectedNode.getParent();
    List<SmartTreeNode> selectedItems = new ArrayList<SmartTreeNode>();
    List<SmartTreeNode> resultNodes = new ArrayList<SmartTreeNode>();
    PubFunction.doSearchAllMatchingNeedingNodeFromCurrNode(projNode, "nodeType", GV.NODE_TYPE_PACK, false, 1, resultNodes);
    if (resultNodes.size() > 1) {
      doRemoveCurrSelectedNode(resultNodes, this.currSelectedNode);
      message = new Object[resultNodes.size() + 5];
      String tip1 = "该项目存在多标段，缺省全部使用相同模板，请根据您的需要进行选择:";
      String tip2 = "  1、如果选择【否】会仅拷贝模板到当前选择的标段下;";
      String tip3 = "  2、当前所选标段为【" + this.currSelectedNode.getNodeDisplayName() + "】;";
      message[0] = tip1;
      message[1] = tip2;
      message[2] = tip3;
      Map<String, SmartTreeNode> itemsMap = new HashMap<String, SmartTreeNode>();
      for (int i = 0; i < resultNodes.size(); i++) {
        SmartTreeNode curr = resultNodes.get(i);
        JCheckBox cb = new JCheckBox(curr.getNodeDisplayName());
        cb.setName(curr.getNodeCode());
        itemsMap.put(curr.getNodeCode(), curr);
        cb.setSelected(true);
        message[i + 3] = cb;
      }
      message[message.length - 2] = "\r\n";
      JCheckBox tmp = new JCheckBox("将【招标文件】节点移动至项目节点下，即和【标段平级】？");
      tmp.setSelected(true);
      message[message.length - 1] = tmp;
      int sel = JOptionPane.showConfirmDialog(null, message, "询问", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (sel == JOptionPane.YES_OPTION) {
        for (int i = 3; i < message.length - 2; i++) {
          JCheckBox cb = (JCheckBox) message[i];
          if (cb.isSelected()) {
            selectedItems.add(itemsMap.get(cb.getName()));
          }
        }
      }
      //如果选择了多个标段采用相同的模板，那么可以将其它标段的招标文件节点删除
      if (((JCheckBox) message[message.length - 1]).isSelected()) {
        isNeedDelete = true;
      }
    }
    selectedItems.add(0, this.currSelectedNode);
    this.mainPanel.closeWordPane();
    boolean isCoverAll = false;
    for (int i = 0; i < selectedItems.size(); i++) {
      SmartTreeNode currNode = selectedItems.get(i);
      SmartTreeNode tbNode = PubFunction.getNeedingNodeInChildren(currNode, GV.NODE_TYPE_TB);
      if (tbNode != null && !isCoverAll) {
        JCheckBox cb = new JCheckBox("不再提示，全部覆盖.");
        Object[] message1 = { currNode.getNodeDisplayName() + "下已经存在【投标文件】，是否覆盖？", "\r\n", cb };
        int res = JOptionPane.showConfirmDialog(null, message1, "", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
          isCoverAll = cb.isSelected();
          succ = TemplateCopyService.doCopyTemplateFilesToProjPack(templateFilePath, currNode, true);
        } else {
          //如果不进行覆盖，那么需要将该标段移除
          selectedItems.remove(i);
        }
      } else {
        succ = TemplateCopyService.doCopyTemplateFilesToProjPack(templateFilePath, currNode, true);
      }
    }
    if (isNeedDelete) {
      for (int i = 0; i < selectedItems.size(); i++) {
        SmartTreeNode packNode = selectedItems.get(i);
        for (int j = 0; j < packNode.getChildCount(); j++) {
          SmartTreeNode subNode = (SmartTreeNode) packNode.getChildAt(j);
          if (GV.NODE_TYPE_ZB.equals(subNode.getNodeType())) {
            //将第一个标段下的招标文件拷贝到项目节点下
            if (i == 0) {
              //在移动节点之前应该先吧这个节点的路径取出来
              File oldPosiFile = new File(GV.getImportFileDir_FromRoot().append(subNode.getNodesFullPath()).toString());
              packNode.remove(j);
              List<SmartTreeNode> zbList = new ArrayList<SmartTreeNode>();
              PubFunction.doSearchAllMatchingNeedingNodeFromCurrNode(projNode, "nodeType", GV.NODE_TYPE_ZB, false, 1, zbList);
              for (int k = 0; k < zbList.size(); k++) {
                projNode.remove(zbList.get(k));
              }
              projNode.insert(subNode, 0);
              File newPosiFile = new File(GV.getImportFileDir_FromRoot().append(projNode.getNodesFullPath()).append(File.separator)
                .append(subNode.getNodeDirPath()).toString());
              oldPosiFile.renameTo(newPosiFile);
              PubFunction.deleteFile(oldPosiFile);
            } else {
              PubFunction.deleteFile(GV.getImportFileDir_FromRoot().append(subNode.getNodesFullPath()).toString());
              packNode.remove(j);
              break;
            }
          }
        }
      }
    }
    succ = doUpdateSmartTreeNode();
    if (succ) {
      JCheckBox cb = new JCheckBox("自动关闭模板列表窗口.");
      cb.setSelected(true);
      Object[] message2 = { GV.getOperateMsg("copyTemplate.success", null), "\r\n", cb };
      int res = JOptionPane.showConfirmDialog(null, message2, "", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (res == JOptionPane.OK_OPTION && cb.isSelected()) {
        this.parentDialog.dispose();
      }
    } else {
      GV.showMessageDialog(null, GV.getOperateMsg("copyTemplate.fail", null));
    }
    return succ;
  }

  /**
   * 将列表中当前标段去掉
   *
   * @param resultNodes
   * @param currSelectedNode
   */
  private void doRemoveCurrSelectedNode(List<SmartTreeNode> resultNodes, SmartTreeNode currSelectedNode) {
    for (int i = 0; i < resultNodes.size(); i++) {
      SmartTreeNode curr = resultNodes.get(i);
      if (curr.getNodeCode().equals(currSelectedNode.getNodeCode())) {
        resultNodes.remove(i);
        return;
      }
    }
  }

  private String getTemplateFileDirectory(String tplBelongBidWay, String tplAppTye, String tplCode) {
    StringBuffer buff = new StringBuffer();
    buff.append(GV.USER_DIR);
    buff.append(File.separator);
    buff.append(GV.TEMPLATE_FILE_DIR);
    buff.append(File.separator);
    buff.append(tplBelongBidWay);
    buff.append("_TPL");
    buff.append(File.separator);
    buff.append(tplAppTye);
    buff.append(GV.TEMPLATE_APPTYPE);
    buff.append(File.separator);
    buff.append(tplCode);
    buff.append(File.separator);
    return buff.toString();
  }

  /**
   * 更新项目的节点树
   *
   * @return
   */
  private boolean doUpdateSmartTreeNode() {
    boolean res = this.packCopyTemplateService.rebuildProjTreeNodeXML((SmartTreeNode) this.currSelectedNode.getParent());
    try {
      mainPanel.refreshLeftFilesTreePanel();
    } catch (Exception e) {
      e.printStackTrace();
      res = false;
    }
    return res;
  }

  public JTable createTable(String type, String tplbelongbidway, boolean needGetMessage) throws Exception {
    JTable table = new com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTable();
    //数据每一列的表示的含义。列说明。
    List<Map<String, String>> columnDataList = buildData();
    //设置列属性，通过列说明生成Grid，每一个grid的属性。
    List<GridColumn> gridColumns = GridColumnBuilder.buildGridColumn(columnDataList);
    //组织好JTable的模型列属性
    JTableColumnModel tableColumnModel = new JTableColumnModel(table, gridColumns);
    JTableModel tableModel = new JTableModel(tableColumnModel, table);
    //组织好JTable将模型设置值
    table.setModel(tableModel);
    table.setColumnModel(tableColumnModel);
    //设置数据源
    JTableRowSorter<JTableModel> sorter = new JTableRowSorter<JTableModel>(tableModel);
    sorter.setComparators(tableColumnModel);
    table.setRowSorter(sorter);
    buildTableRowData(type, tplbelongbidway, needGetMessage);
    tableModel.add(tplListMap.get(type));
    return table;
  }

  private List<Map<String, String>> tplDataList = new ArrayList<Map<String, String>>();

  private void buildTableRowData(String type, String tplbelongbidway, boolean needGetMessage) throws Exception {
    if (needGetMessage) {
      tplDataList = packCopyTemplateService.getTemplatesList(type, tplbelongbidway);
    } else {
      tplListMap.put(type, null);
    }
    tplListMap.put(type, tplDataList);
  }

  /**
   * 表格表头列属性
   *
   * @return List<Map<String,String>>
   * @throws
   */
  public List<Map<String, String>> buildData() {
    List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
    dataList.add(buildRow("TABLE.CHECKBOX", "", "CHECKBOX", "30", "C", "", "CHECKBOX", "", "", "N"));
    dataList.add(buildRow("TPLBELONGBIDWAY", "", "招标方式", "120", "C", "TEXT", "TEXT", "", "", "Y"));
    dataList.add(buildRow("TPLAPPTYPE", "", "应用类型", "120", "C", "TEXT", "TEXT", "", "", "Y"));
    dataList.add(buildRow("TPLNO", "", "模板编号", "100", "C", "TEXT", "TEXT", "", "", "Y"));
    dataList.add(buildRow("LATESTUSED", "", "模板创建时间", "150", "C", "TEXT", "TEXT", "", "", "Y"));
    //dataList.add(buildRow("HISTORY", "", "使用记录", "80", "C", "B", "BUTTON", "", "", "Y"));
    dataList.add(buildRow("TPLPROJECTNAME", "", "模板来源(项目)", "200", "C", "TEXT", "TEXT", "", "", "Y"));
    dataList.add(buildRow("DESCRIPTION", "", "描述", "300", "L", "TEXT", "TEXT", "", "", "Y"));
    return dataList;
  }

  private Map<String, String> buildRow(String columnId, String groupId, String caption, String width, String align, String dataType,
    String editorType, String decLen, String isThousandsSeparator, String isForceReadonly) {
    Map<String, String> row = new HashMap<String, String>();
    row.put("COLUMN_ID", columnId);//字段名
    row.put("GROUP_ID", groupId);
    row.put("CAPTION", caption);//列名（字段名的翻译）
    row.put("WIDTH", width);//列宽
    row.put("ALIGN", align);//水平对齐/L/C/R
    row.put("DATA_TYPE", dataType);//数据类型/NUM/TEXT/DATE
    row.put("EDITOR_TYPE", editorType);//编辑框类型
    row.put("DEC_LEN", decLen);//小数位
    row.put("IS_THOUSANDS_SEPARATOR", isThousandsSeparator);
    row.put("IS_FORCE_READONLY", isForceReadonly);
    return row;
  }

  /**
   * 从服务器取数据
   * 现将模板覆盖掉现有的结构，重新写配置文件，刷新树结构，自动将项目信息填充到模板上
   * <p/>
   * 从服务器下载数据包，然后指定到上级目录的下面解压，
   *
   * @param selectedRow
   */
  private void doImportTemplateFromDB(int selectedRow) throws Exception {
    Map<String, String> rowMap = tableModel.get(selectedRow);
    String tplBelongBidWay = rowMap.get("TPLBELONGBIDWAY");//招标方式
    String tplNo = rowMap.get("TPLNO");//模板编号
    String tplAppTye = rowMap.get("TPLAPPTYPE");//应用类型
    String fileID = rowMap.get("FILEID");
    File ztbFile = asFileDao.readFile(fileID);
    ZipFile zipFile = new ZipFile(ztbFile, GV.FILE_CHAR_CODE);
    //TODO 公开招标_TPL  招标方式为分类，如果不存在这个分类的模板，那么需要创建一个文件夹：招标方式_TPL
    String templateFilePath = GV.USER_DIR + File.separator + GV.TEMPLATE_FILE_DIR + File.separator + tplBelongBidWay + "_TPL" + File.separator
      + tplAppTye + GV.TEMPLATE_APPTYPE + File.separator + tplNo + File.separator;
    PubFunction.checkAndMakeDirs(templateFilePath);
    unzipFileToDestDir(zipFile, templateFilePath);
    PubFunction.deleteFile(ztbFile);
    try {
      doExecuteCopying(templateFilePath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 公开招标_TPL

  /**
   * 文件解压缩 ztb
   *
   * @param zipFile
   * @param destination
   */
  public static void unzipFileToDestDir(ZipFile zipFile, String destination) throws Exception {
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    BufferedInputStream bis = null;
    Enumeration<ZipEntry> emu = zipFile.getEntries();
    try {
      while (emu.hasMoreElements()) {
        ZipEntry entry = (ZipEntry) emu.nextElement();
        if (entry.isDirectory()) {
          new File(destination + File.separator + entry.getName()).mkdirs();
          continue;
        }
        bis = new BufferedInputStream(zipFile.getInputStream(entry));
        File file = new File(destination + File.separator + entry.getName());
        File parent = file.getParentFile();
        if (parent != null && (!parent.exists())) {
          parent.mkdirs();
        }
        fos = new FileOutputStream(file);
        bos = new BufferedOutputStream(fos, GV.BUFFER_SIZE);
        int count;
        byte data[] = new byte[GV.BUFFER_SIZE];
        while ((count = bis.read(data, 0, GV.BUFFER_SIZE)) != -1) {
          bos.write(data, 0, count);
        }
        bos.flush();
        bos.close();
        bis.close();
      }
      zipFile.close();
    } catch (Exception e) {
      e.printStackTrace();
      if (bos != null) {
        bos.close();
      }
      if (fos != null) {
        fos.close();
      }
      if (bis != null) {
        bis.close();
      }
      throw new Exception(e.getMessage());
    }
  }
}
