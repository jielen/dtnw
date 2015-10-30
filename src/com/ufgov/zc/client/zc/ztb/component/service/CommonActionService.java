package com.ufgov.zc.client.zc.ztb.component.service;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.ufgov.zc.client.zc.ztb.DataChecker;
import com.ufgov.zc.client.zc.ztb.JobThreads;
import com.ufgov.zc.client.zc.ztb.P;
import com.ufgov.zc.client.zc.ztb.activex.WordPane;
import com.ufgov.zc.client.zc.ztb.component.AddChildNodePanel;
import com.ufgov.zc.client.zc.ztb.component.EcbjLoginTypeSelectionPanel;
import com.ufgov.zc.client.zc.ztb.component.MainPanel;
import com.ufgov.zc.client.zc.ztb.component.ProjectInfoPanel;
import com.ufgov.zc.client.zc.ztb.component.SingleSeletionTree;
import com.ufgov.zc.client.zc.ztb.component.SystemConfigPanel;
import com.ufgov.zc.client.zc.ztb.component.ZbConfigPanel;
import com.ufgov.zc.client.zc.ztb.docautogenerate.operate.GenPriceWordTable;
import com.ufgov.zc.client.zc.ztb.model.BusinessProject;
import com.ufgov.zc.client.zc.ztb.model.DBProperty;
import com.ufgov.zc.client.zc.ztb.model.DetailsType;
import com.ufgov.zc.client.zc.ztb.model.ProjectBag;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;
import com.ufgov.zc.client.zc.ztb.service.DeleteTbFileService;
import com.ufgov.zc.client.zc.ztb.service.FileExportService;
import com.ufgov.zc.client.zc.ztb.service.MoveNodeService;
import com.ufgov.zc.client.zc.ztb.service.PackDetailsService;
import com.ufgov.zc.client.zc.ztb.service.RenameNodeService;
import com.ufgov.zc.client.zc.ztb.service.ServerProjectService;
import com.ufgov.zc.client.zc.ztb.service.SystemConfigService;
import com.ufgov.zc.client.zc.ztb.table.common.PubFunc;
import com.ufgov.zc.client.zc.ztb.util.FileChooseFilter;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.client.zc.ztb.util.PubFunction;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;

public class CommonActionService {
  private MainPanel mainPanel;

  private JFrame rootContainer;

  public CommonActionService(JFrame rootContainer) {
    this.rootContainer = rootContainer;
  }

  public CommonActionService(MainPanel mainPanel) {
    this.mainPanel = mainPanel;
  }

  /**
   * 本地化配置
   */
  public void toolLocalConfig() {
    JDialog dialog = new JDialog(rootContainer, GV.DB_CONFIG_TITLE, true);
    dialog.setLayout(new BorderLayout());
    try {
      if ("TTB".equals(MainPanel.getWHO_I_AM())) {
        dialog.add(new SystemConfigPanel(dialog), BorderLayout.NORTH);
        dialog.setPreferredSize(new Dimension(480, 150));
        dialog.setMinimumSize(new Dimension(480, 150));
      } else if (GV.DIS_ZHAOBIAO_ONLY.equals(MainPanel.getWHO_I_AM())) {
        dialog.add(new ZbConfigPanel(dialog), BorderLayout.NORTH);
        dialog.setPreferredSize(new Dimension(510, 180));
        dialog.setMinimumSize(new Dimension(510, 180));
      }
    } catch (Exception e) {
      e.printStackTrace();
      GV.showMessageDialog(null, e.getMessage());
      return;
    }
    PubFunc.setShowInMiddle(dialog);
    dialog.pack();
    dialog.setVisible(true);
    dialog.toFront();
  }

  /**
   * 将当前节点移至第一个
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteMoveToFirst(SingleSeletionTree ssTree) throws Exception {
    MainPanel mp = ssTree.getMainPanel();
    SmartTreeNode currentNode = ssTree.getCurrentNode();
    if (currentNode == null) {
      return;
    }
    MoveNodeService moveNodeService = new MoveNodeService();
    if (moveNodeService.moveuptofirst(currentNode)) {
      mp.refreshLeftFilesTreePanel();
    }
  }

  /**
   * 将当前节点往上移动
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteMoveUp(SingleSeletionTree ssTree) throws Exception {
    MainPanel mp = ssTree.getMainPanel();
    SmartTreeNode currentNode = ssTree.getCurrentNode();
    if (currentNode == null) {
      return;
    }
    MoveNodeService moveNodeService = new MoveNodeService();
    if (moveNodeService.moveup(currentNode)) {
      mp.refreshLeftFilesTreePanel();
    }
  }

  /**
   * 将当前节点往下移动
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteMoveDown(SingleSeletionTree ssTree) throws Exception {
    MainPanel mp = ssTree.getMainPanel();
    SmartTreeNode currentNode = ssTree.getCurrentNode();
    MoveNodeService moveNodeService = new MoveNodeService();
    if (moveNodeService.movedown(currentNode)) {
      mp.refreshLeftFilesTreePanel();
    }
  }

  /**
   * 将当前节点移至最后一行
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteMoveToLast(SingleSeletionTree ssTree) throws Exception {
    MainPanel mp = ssTree.getMainPanel();
    SmartTreeNode currentNode = ssTree.getCurrentNode();
    MoveNodeService moveNodeService = new MoveNodeService();
    if (moveNodeService.movedowntolast(currentNode)) {
      mp.refreshLeftFilesTreePanel();
    }
  }

  /**
   * 重名名当前节点
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteRename(SingleSeletionTree ssTree) throws Exception {
    SmartTreeNode currentNode = ssTree.getCurrentNode();
    String tip = GV.getSimpleMsg("askForInputName");
    String name = javax.swing.JOptionPane.showInputDialog(ssTree.getRootPane(), tip, currentNode.getNodeName());
    name = null;
    if (name == null) {
      GV.showMessageDialog(ssTree.getRootPane(), GV.getSimpleMsg("functionNotSupport"));
      return;
    }
    if ("".equals(name.trim()) || !PubFunction.isValidFileName(name)) {
      GV.showMessageDialog(ssTree.getRootPane(), GV.getSimpleMsg("askForInputEffectName"));
      return;
    }
    RenameNodeService renameNodeService = new RenameNodeService();
    currentNode.setUserObject(name);
    renameNodeService.rename(currentNode);
  }

  /**
   * 导出二次报价表
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteEcbjExport(SingleSeletionTree ssTree) throws Exception {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDoubleBuffered(true);
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setAcceptAllFileFilterUsed(true);
    fileChooser.setCurrentDirectory(new File(GV.getHistoryDirectory()));
    int result = fileChooser.showSaveDialog(null);
    String filePath;
    if (result == JFileChooser.APPROVE_OPTION) {
      filePath = fileChooser.getSelectedFile().getAbsolutePath();
      GV.setHistoryDirectory(fileChooser.getSelectedFile().getParent());
      SmartTreeNode currNode = (SmartTreeNode) ssTree.getCurrentNode();
      SmartTreeNode ecbjNode = null;
      if (GV.NODE_TYPE_ECBJB.equals(currNode.getNodeType())) {
        ecbjNode = currNode;
      } else {
        ecbjNode = PubFunction.getNeedingNodeInChildren(currNode, GV.NODE_TYPE_ECBJB);
      }
      String tarPath = null;
      boolean flag = true;
      try {
        StringBuffer buff = GV.getImportFileDir_FromRoot();
        buff.append(ecbjNode.getNodesFullPath());
        buff.append(GV.SUFFIX_TABLE);
        String projName = ((SmartTreeNode) ecbjNode.getParent().getParent()).getNodeDisplayName();
        String packName = ((SmartTreeNode) ecbjNode.getParent()).getNodeDisplayName();
        String ecbjName = ecbjNode.getNodeDisplayName();
        tarPath = filePath + File.separator + projName + "[" + packName + "]" + ecbjName + ".xml";
        PubFunction.copyFile(buff.toString(), tarPath);
        flag = true;
      } catch (Exception e) {
        e.printStackTrace();
        flag = false;
      }
      if (flag) {
        GV.showMessageDialog(ssTree.getRootPane(), GV.getSimpleMsg("exportSuccAndPath") + tarPath);
      } else {
        GV.showMessageDialog(ssTree.getRootPane(), GV.getSimpleMsg("notFoundPriceReport"));
      }
    }
  }

  /**
   * 上传再次报价
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteUploadEcbj(SingleSeletionTree ssTree) throws Exception {
    SmartTreeNode ecbjNode = (SmartTreeNode) ssTree.getCurrentNode();
    MainPanel mainPanel = ssTree.getMainPanel();
    mainPanel.saveTableBuilderData();
    StringBuffer buff = GV.getImportFileDir_FromRoot();
    buff.append(ecbjNode.getNodesFullPath());
    buff.append(GV.SUFFIX_TABLE);
    String src = buff.toString();
    String tar = src.replaceAll(GV.SUFFIX_TABLE, ".xml");
    try {
      PubFunction.copyFile(src, tar);
    } catch (Exception e) {
    }
    Map map = new HashMap();
    map.put("PROJECTCODE", ((SmartTreeNode) ecbjNode.getParent().getParent()).getNodeCode());
    map.put("PROJECTNAME", ((SmartTreeNode) ecbjNode.getParent().getParent()).getNodeName());
    map.put("PACKCODE", ((SmartTreeNode) ecbjNode.getParent()).getNodeCode());
    map.put("PACKNAME", ((SmartTreeNode) ecbjNode.getParent()).getNodeName());
    map.put("FILENAME", ecbjNode.getNodeDisplayName() + ".xml");
    map.put("FILEFULLPATH", tar);
    JDialog dialog = new JDialog();
    dialog.add(new EcbjLoginTypeSelectionPanel(new JDialog(), dialog, map));
    dialog.setSize(new Dimension(420, 200));
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.setLocationRelativeTo(null);
    dialog.toFront();
    dialog.setModal(true);
    dialog.setVisible(true);
  }

  /**
   * 添加一个再次报价表
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteEcbjAdding(SingleSeletionTree ssTree) throws Exception {
    SmartTreeNode packNode = (SmartTreeNode) ssTree.getCurrentNode();
    Vector<DetailsType> itemVector = new Vector<DetailsType>();
    itemVector.add(new DetailsType(GV.NODE_NAME_ECBJB, GV.NODE_TYPE_ECBJB));
    MainPanel mainPanel = ssTree.getMainPanel();
    JDialog dialog = new JDialog((JFrame) ssTree.getRootPane().getParent(), GV.ADD_BAG_DETAILS, true);
    dialog.setLayout(new BorderLayout());
    dialog.add(new AddChildNodePanel(packNode, mainPanel, itemVector), BorderLayout.NORTH);
    dialog.setPreferredSize(new Dimension(320, 150));
    dialog.setResizable(true);
    PubFunc.setShowInMiddle(dialog);
    dialog.pack();
    dialog.setModal(true);
    dialog.toFront();
    dialog.setVisible(true);
  }

  /**
   * 保存当前打开文档
   */
  public void doExecutePackDetailSaving() {
    SmartTreeNode openedNode = mainPanel.getOpenedLeavesNode();
    String tigMeg;//提示信息
    if (openedNode != null) {
      boolean saveFlag = false;
      String nodeType = openedNode.getNodeType();
      if (GV.NODE_TYPE_DOC.equals(nodeType) || GV.NODE_TYPE_RESPONSE_POINT.equals(nodeType)) {
        saveFlag = mainPanel.saveWordPane();
      } else if (GV.NODE_TYPE_TABLE.equals(nodeType) || GV.NODE_TYPE_TBYLB.equals(nodeType) || GV.NODE_TYPE_ECBJB.equals(nodeType)) {
        saveFlag = mainPanel.saveTableBuilderData();
      }
      if (!saveFlag) {
        tigMeg = GV.getOperateMsg("save.fail", openedNode.getNodeName());
      } else {
        ProjectInfoPanel.updateFlowingNOToFile();
        tigMeg = GV.getOperateMsg("save.success", openedNode.getNodeName());
      }
      GV.showMessageDialog(mainPanel.getRootPane(), tigMeg);
    }
  }

  /**
   * 上传项目招标书
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteProjectUploading(SingleSeletionTree ssTree) throws Exception {
    SmartTreeNode currentNode = ssTree.getCurrentNode();
    if (currentNode == null) {
      currentNode = PubFunction.getNeedingNodeInChildren(ssTree.getTreeNode(), GV.NODE_TYPE_PROJECT);
    }
    if (currentNode == null) {
      JOptionPane.showMessageDialog(null, GV.getSimpleMsg("askForSelectTreeNode"));
      return;
    }
    String content = currentNode.getNodeName();
    if (!DataChecker.toCheckKeyMsgInZTBFile()) {
      return;
    }
    String meg = GV.getOperateMsg("uploadProject.confirm", content);
    if (GV.showConfirmDialog(ssTree.getRootPane(), meg, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      new JobThreads().startUploadProjectThread(ssTree, null);
    }
  }

  /**
   * 覆盖上传项目招标书
   */
  public void doExecuteZBFileUploading() {
    SingleSeletionTree ssTree = (SingleSeletionTree) mainPanel.getSingleSeletionTree();
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setFileFilter(new FileChooseFilter(GV.SUFFIX_ZTB));
    fileChooser.setCurrentDirectory(new File(GV.getHistoryDirectory()));
    int result = fileChooser.showSaveDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      String ztbPath = fileChooser.getSelectedFile().getAbsolutePath();
      GV.setHistoryDirectory(fileChooser.getSelectedFile().getParent());
      new JobThreads().startUploadProjectThread(ssTree, ztbPath);
    }
  }

  /**
   * 添加一个新节点
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteNodeAdding(SingleSeletionTree ssTree) throws Exception {
    SmartTreeNode currNode = ssTree.getCurrentNode();
    MainPanel mainPanel = ssTree.getMainPanel();
    JDialog dialog = new JDialog((JFrame) ssTree.getRootPane().getParent(), GV.ADD_BAG_DETAILS, true);
    dialog.setLayout(new BorderLayout());
    dialog.add(new AddChildNodePanel(currNode, mainPanel), BorderLayout.NORTH);
    dialog.setPreferredSize(new Dimension(360, 150));
    dialog.setResizable(true);
    PubFunc.setShowInMiddle(dialog);
    dialog.pack();
    dialog.setVisible(true);
    dialog.toFront();
  }

  /**
   * 往当前节点下导入一个文件
   *
   * @param ssTree
   */
  public void doExecuteNodeImporting(SingleSeletionTree ssTree) {
    SmartTreeNode currNode = ssTree.getCurrentNode();
    MainPanel mainPanel = ssTree.getMainPanel();
    JDialog dialog = new JDialog((JFrame) ssTree.getRootPane().getParent(), GV.ADD_BAG_DETAILS, true);
    dialog.setLayout(new BorderLayout());
    dialog.add(new AddChildNodePanel(currNode, mainPanel, true), BorderLayout.NORTH);
    dialog.setPreferredSize(new Dimension(420, 150));
    dialog.setResizable(true);
    PubFunc.setShowInMiddle(dialog);
    dialog.pack();
    dialog.setVisible(true);
    dialog.toFront();
  }

  /**
   * 删除一个节点
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteNodeDeleting(SingleSeletionTree ssTree) throws Exception {
    String delFileName = "";
    SmartTreeNode currNode = ssTree.getCurrentNode();
    String content = currNode.getNodeDisplayName();
    String meg = GV.getSpecialMsg("deleteNode.confirm", currNode);
    if (GV.showConfirmDialog(ssTree.getRootPane(), meg, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      DeleteTbFileService deleteTbFileService = new DeleteTbFileService();
      String tigMeg = GV.getOperateMsg("deleteNode.success", content);
      MainPanel mainPanel = ssTree.getMainPanel();
      boolean opened = deleteTbFileService.isCurrentNodeOrChildNodeOpened(mainPanel.getOpenedPackNode(), mainPanel.getOpenedLeavesNode(), currNode);
      delFileName = GV.getImportFileDir_FromRoot().append(currNode.getNodesFullPath()).toString();
      SmartTreeNode parentNode = removeCurrNodeFromParent(currNode);
      String fullPath = GV.getProjectConfigFullPath();
      if (parentNode != null) {
        parentNode = (SmartTreeNode) parentNode.getRoot();
      } else {
        //删除完所有的子节点后，还应该保留【项目列表】根节点
        parentNode = new SmartTreeNode();
      }
      boolean isSuccess = PubFunction.checkAndCreateDirOrFile(fullPath, "F", true, true, parentNode);
      if (isSuccess) {
        String nodeType = currNode.getNodeType();
        //这段代码应该修改一下，使得自动根据节点的类型进行文件级联删除
        if (GV.NODE_TYPE_TABLE.equals(nodeType) || GV.NODE_TYPE_TBYLB.equals(nodeType) || GV.NODE_TYPE_DOC.equals(nodeType)
          || GV.NODE_TYPE_ECBJB.equals(nodeType)) {
          if (GV.NODE_TYPE_DOC.equals(nodeType)) {
            delFileName += GV.SUFFIX_DOC;
            PubFunction.deleteFile(delFileName);
          } else if (GV.NODE_TYPE_TABLE.equals(nodeType) || GV.NODE_TYPE_TBYLB.equals(nodeType) || GV.NODE_TYPE_ECBJB.equals(nodeType)) {
            PubFunction.deleteFile(delFileName + GV.SUFFIX_TABLE);
            PubFunction.deleteFile(delFileName + GV.SUFFIX_TABLE_SETTING);
            mainPanel.setTableBuilder(null);
          }
        } else {
          PubFunction.deleteFile(delFileName);
        }
        mainPanel.refreshLeftFilesTreePanel();
        if (opened) {
          mainPanel.closePackDetailsNode();
          mainPanel.refreshRightPanel();
        }
      } else {
        tigMeg = GV.getOperateMsg("deleteNode.fail", content);
      }
      GV.showMessageDialog(ssTree.getRootPane(), tigMeg);
    }
  }

  /**
   * 将当前节点从父节点中移除
   *
   * @param currNode
   * @return
   */
  private SmartTreeNode removeCurrNodeFromParent(SmartTreeNode currNode) {
    SmartTreeNode parentNode = (SmartTreeNode) currNode.getParent();
    if (parentNode == null) {
      return null;
    }
    for (int i = 0; i < parentNode.getChildCount(); i++) {
      if (parentNode.getChildAt(i).equals(currNode)) {
        parentNode.remove(i);
        return parentNode;
      }
    }
    return parentNode;
  }

  /**
   * 导出项目招标书到本地
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteProjectExporting(SingleSeletionTree ssTree) throws Exception {
    if (!DataChecker.toCheckKeyMsgInZTBFile()) {
      return;
    }
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setAcceptAllFileFilterUsed(true);
    fileChooser.setCurrentDirectory(new File(GV.getHistoryDirectory()));
    int result = fileChooser.showSaveDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      SmartTreeNode currentNode = ssTree.getCurrentNode();
      if (currentNode == null || !GV.NODE_TYPE_PROJECT.equals(currentNode.getNodeType())) {
        List<SmartTreeNode> list = new ArrayList<SmartTreeNode>();
        PubFunction.doSearchAllMatchingNeedingNodeFromCurrNode(ssTree.getTreeNode(), "nodeType", GV.NODE_TYPE_PROJECT, false, 3, list);
        if (list.size() > 0) {
          currentNode = list.get(0);
        }
      }
      if (currentNode == null) {
        JOptionPane.showMessageDialog(null, GV.getSimpleMsg("notExistExportableProject"));
        return;
      }
      ssTree.setCurrentNode(currentNode);
      String filePath = fileChooser.getSelectedFile().getAbsolutePath();
      GV.setHistoryDirectory(fileChooser.getSelectedFile().getParent());
      new JobThreads().startExportProjectThread(ssTree, filePath);
    }
  }

  /**
   * 填充招标书模板
   *
   * @throws Exception
   */
  public void doExecuteMoldFilling() throws Exception {
    SmartTreeNode currNode = mainPanel.getOpenedLeavesNode();
    if (currNode == null || mainPanel.getWordPane() == null) {
      JOptionPane.showMessageDialog(null, GV.getOperateMsg("noMatchingDocument", null));
      return;
    }
    String nodeType = currNode.getNodeType();
    if (GV.NODE_TYPE_DOC.equals(nodeType)) {
      SmartTreeNode packNode = PubFunction.getPackNode(currNode);
      SmartTreeNode projNode = PubFunction.getNeedingNodeInParent(currNode, GV.NODE_TYPE_PROJECT);
      if (GV.NODE_SOURCE_FROM_MOLD == currNode.getNodeSource()) {
        String projCode = projNode.getNodeCode();
        String packCode = null;
        if (packNode != null) {
          packCode = packNode.getNodeCode();
        }
        executeFillMold(projCode, packCode);
      } else {
        JOptionPane.showMessageDialog(null, GV.getOperateMsg("noMatchingDocument", null));
        return;
      }
    } else {
      JOptionPane.showMessageDialog(null, GV.getOperateMsg("noMatchingDocument", null));
      return;
    }
  }

  /**
   * 执行模板填充
   *
   * @param packNode
   */
  @SuppressWarnings("unchecked")
  public void executeFillMold(String projCode, String packCode) {
    System.out.println("填充模板=======" + "projCode" + projCode + "packCode" + packCode);
    try {
      ServerProjectService projectImportService = new ServerProjectService();
      Map<String, Object> tmp = projectImportService.searchProjectPackDetail(projCode, packCode);
      Map<String, Object> result = (Map<String, Object>) tmp.get("RESULTMAP");
      if (result == null) {
        throw new Exception((String) tmp.get("FAILREASON"));
      }
      Iterator<String> it = result.keySet().iterator();
      String[] names = new String[result.size()];
      String[] values = new String[result.size()];
      int i = 0;
      SimpleDateFormat sdf = new SimpleDateFormat(ZcSettingConstants.SIMPLE_DATE_FORMAT_FULL);
      while (it.hasNext()) {
        String key = it.next();
        String dataStr = null;
        Object value = result.get(key);
        if (value instanceof Date) {
          dataStr = sdf.format(value);
          names[i] = key;
          values[i++] = dataStr;
          P.p("当前值：" + key + "=" + dataStr);
          continue;
        } else if (value instanceof BigDecimal) {
          dataStr = ((BigDecimal) value).toString();
          P.p("当前值：" + key + "=" + dataStr);
          names[i] = key;
          values[i++] = dataStr;
          continue;
        }
        P.p("当前值：" + key + "=" + result.get(key));
        names[i] = key;
        values[i++] = (String) result.get(key);
      }
      mainPanel.getWordPane().replaceBookMarks(names, values);

      JOptionPane.showMessageDialog(null, GV.getOperateMsg("fillSucess", null));

    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, GV.getSimpleMsg("fillError") + e.getMessage());
    }
  }

  /**
   * 给指定的word加锁
   *
   * @throws Exception
   */
  public void doExecuteWordLocking() throws Exception {
    WordPane wp = mainPanel.getWordPane();
    SmartTreeNode openedNode = mainPanel.getOpenedLeavesNode();
    if (openedNode != null && !GV.NODE_TYPE_DOC.equals(openedNode.getNodeType())) {
      GV.showMessageDialog(null, GV.getSimpleMsg("notNeedToProtect"));
      return;
    }
    if (wp == null) {
      GV.showMessageDialog(null, GV.getSimpleMsg("askForOpenWord"));
      return;
    }
    if (openedNode.isLocked()) {
      GV.showMessageDialog(null, GV.getSimpleMsg("wordWasProtected"));
      return;
    }
    JPasswordField pwd = new JPasswordField();
    Object[] message = { GV.getSimpleMsg("askForInputPwd"), pwd };
    int res = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (res == JOptionPane.CANCEL_OPTION) {
      return;
    } else {
      boolean flag = wp.protectDoc(new String(pwd.getPassword()));
      updateNodeAttribute("protectFile", true, flag);
    }
  }

  /**
   * 解开被锁住的word文档
   *
   * @throws Exception
   */
  public void doExecuteWordUnlock() throws Exception {
    WordPane wp = mainPanel.getWordPane();
    SmartTreeNode openedNode = mainPanel.getOpenedLeavesNode();
    if (openedNode != null && !GV.NODE_TYPE_DOC.equals(openedNode.getNodeType())) {
      GV.showMessageDialog(null, GV.getSimpleMsg("notNeedToUnprotect"));
      return;
    }
    if (wp == null) {
      GV.showMessageDialog(null, GV.getSimpleMsg("askForOpenWordToUnprotect"));
      return;
    }
    if (!openedNode.isLocked()) {
      GV.showMessageDialog(null, GV.getSimpleMsg("wordWasUnprotected"));
      return;
    }
    JPasswordField pwd = new JPasswordField();
    Object[] message = { GV.getSimpleMsg("askForInputPwd"), pwd };
    int res = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (res == JOptionPane.CANCEL_OPTION) {
      return;
    } else {
      boolean flag = wp.unProtectDoc(new String(pwd.getPassword()));
      updateNodeAttribute("unprotectFile", false, flag);
    }
  }

  /**
   * 修改被修改节点的图标为加锁状态图标
   */
  private void updateNodeAttribute(String opt, boolean flag, boolean wordFlag) {
    if (wordFlag == false) {
      return;
    }
    SmartTreeNode openedNode = mainPanel.getOpenedLeavesNode();
    openedNode.setLocked(flag);
    String disName = openedNode.getNodeDisplayName();
    String fullPath = GV.getProjectConfigFullPath();
    boolean isSuccess = PubFunction.checkAndCreateDirOrFile(fullPath, "F", true, true, openedNode.getRoot());
    if (isSuccess) {
      try {
        mainPanel.refreshLeftFilesTreePanel();
        JOptionPane.showMessageDialog(null, GV.getOperateMsg(opt + ".success", disName));
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, GV.getOperateMsg(opt + ".refreshfail", disName));
      }
    } else if (wordFlag) {
      JOptionPane.showMessageDialog(null, GV.getOperateMsg(opt + ".fail", disName));
    }
  }

  /**
   * 拷贝标段内容到别的标段对应节点下
   * 1、如果只有一个标段，那么无法进行拷贝，直接返回；
   * 2、如果只有两个标段，那么询问是否覆盖另一个标段；
   * 3、如果有三个或以上标段，那么弹出一个checkbox框供选择；
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteSameNodeCopying(SingleSeletionTree ssTree) throws Exception {
    SmartTreeNode currNode = (SmartTreeNode) ssTree.getCurrentNode();
    if (currNode == null) {
      return;
    }
    this.mainPanel = ssTree.getMainPanel();
    List<SmartTreeNode> packNodes = new ArrayList<SmartTreeNode>();
    PubFunction.doSearchAllMatchingNeedingNodeFromCurrNode((SmartTreeNode) currNode.getRoot(), "nodeType", GV.NODE_TYPE_PACK, false, 0, packNodes);
    removeCurrPackNode(currNode, packNodes);
    if (packNodes.size() == 0) {
      return;
    }
    String title = GV.getSimpleMsg("sameNodeCopy");
    String msg = GV.getSimpleMsg("sureToCoverEachNodes");
    if (packNodes.size() == 1) {
      if (currNode.isLeaf()) {
        msg = GV.getSimpleMsg("sureToCoverSingleNode");
      }
      int sel = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (sel == JOptionPane.YES_OPTION) {
        doCopyNodes(currNode, packNodes);
      }
    } else if (packNodes.size() >= 2) {
      final List<JCheckBox> cbList = new ArrayList<JCheckBox>();
      Object[] options = new Object[packNodes.size() + 2];
      options[0] = GV.getSimpleMsg("selectPacksToCover");
      JCheckBox all = new JCheckBox("全选");
      all.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          boolean sel = ((JCheckBox) e.getSource()).isSelected();
          for (int i = 0; i < cbList.size(); i++) {
            cbList.get(i).setSelected(sel);
          }
        }
      });
      options[1] = all;
      for (int i = 0; i < packNodes.size(); i++) {
        JCheckBox cb = new JCheckBox();
        cb.setText(packNodes.get(i).getNodeDisplayName());
        cb.setName(packNodes.get(i).getNodeCode());
        options[i + 2] = cb;
        cbList.add(cb);
      }
      int res = JOptionPane.showConfirmDialog(null, options, "请选择", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
      msg = GV.getSimpleMsg("sureToCoverSelectedPacks");
      if (res == JOptionPane.OK_OPTION) {
        if (currNode.isLeaf()) {
          msg = GV.getSimpleMsg("sureToCoverSelectedSinglePacks");
          int sel = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
          if (sel == JOptionPane.YES_OPTION) {
            doCopyNodes(currNode, getSelectedPackNodes(cbList, packNodes));
          }
        } else {
          int sel = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
          if (sel == JOptionPane.YES_OPTION) {
            doCopyNodes(currNode, getSelectedPackNodes(cbList, packNodes));
          }
        }
      }
    }
  }

  private List<SmartTreeNode> getSelectedPackNodes(List<JCheckBox> cbList, List<SmartTreeNode> packNodes) {
    List<SmartTreeNode> selList = new ArrayList<SmartTreeNode>();
    for (int i = 0; i < cbList.size(); i++) {
      for (int j = 0; j < packNodes.size(); j++) {
        if (cbList.get(i).isSelected() && cbList.get(i).getName().equals(packNodes.get(j).getNodeCode())) {
          selList.add(packNodes.get(j));
        }
      }
    }
    return selList;
  }

  /**
   * 移除currNode节点所在的packNode
   *
   * @param currNode
   * @param packNodes
   */
  private void removeCurrPackNode(SmartTreeNode currNode, List<SmartTreeNode> packNodes) {
    SmartTreeNode spNode = PubFunction.getNeedingNodeInParent(currNode, GV.NODE_TYPE_PACK);
    if (spNode == null) {
      return;
    }
    for (int i = 0; i < packNodes.size(); i++) {
      if (spNode.getNodeCode().equals(packNodes.get(i).getNodeCode())) {
        packNodes.remove(i);
        return;
      }
    }
  }

  /**
   * 相似节点拷贝
   *
   * @param currNode
   * @param packNodes
   * @throws Exception
   */
  private void doCopyNodes(SmartTreeNode currNode, List<SmartTreeNode> packNodes) throws Exception {
    String closedPath = null;
    List<String> list = new ArrayList<String>();
    //先获得该节点下所有的可拷贝文件
    FileExportService.getFilePathList(currNode, list, "");
    Map<String, String> pathMap = new HashMap<String, String>();
    loadListToMap(list, pathMap);
    for (int i = 0; i < packNodes.size(); i++) {
      List<String> tmp = new ArrayList<String>();
      //先获得该标段下对应节点下所有文件
      SmartTreeNode tmpNode = PubFunction.getNeedingNodeWithNodeNameInChildren(packNodes.get(i), currNode.getNodeType(), currNode.getNodeName());
      FileExportService.getFilePathList(tmpNode, tmp, "");
      for (int j = 0; j < tmp.size(); j++) {
        File file = new File(tmp.get(j));
        String buff1 = GV.getImportFileDir_FromRoot().append(File.separator).append(pathMap.get(file.getName())).toString();
        String buff2 = GV.getImportFileDir_FromRoot().append(File.separator).append(tmp.get(j)).toString();
        if ((new File(buff2)).getAbsolutePath().equals(this.getMainPanel().getWordPane().getOpenFile().getAbsolutePath())) {
          this.getMainPanel().closeWordPane(true);
          closedPath = buff2;
        }
        PubFunction.copyFile(buff1, buff2);
      }
    }
    if (closedPath != null) {
      this.getMainPanel().getWordPane().open(closedPath);
    }
  }

  private void loadListToMap(List<String> list, Map<String, String> pathMap) {
    for (int i = 0; i < list.size(); i++) {
      pathMap.put(new File(list.get(i)).getName(), list.get(i));
    }
  }

  /**
   * 根据节点获得项目及其标段列表
   *
   * @param projNode
   * @return
   */
  public List<BusinessProject> getProjList(SmartTreeNode projNode) {
    List<BusinessProject> projList = new ArrayList<BusinessProject>();
    BusinessProject bp = new BusinessProject();
    bp.setName(projNode.getNodeName());
    bp.setNo(projNode.getNodeCode());
    for (int i = 0; i < projNode.getChildCount(); i++) {
      SmartTreeNode packNode = (SmartTreeNode) projNode.getChildAt(i);
      ProjectBag pb = new ProjectBag();
      pb.setName(packNode.getNodeName());
      pb.setNo(packNode.getNodeCode());
      bp.getPackList().add(pb);
    }
    projList.add(bp);
    return projList;
  }

  /**
   * 设置缺省配置信息
   */
  public void toSetDefaultConfig() {
    SystemConfigService systemConfigService = new SystemConfigService();
    DBProperty dbProperty = new DBProperty();
    try {
      dbProperty.setCustomerLocalWorkspacePath(GV.DEFAULT_CONFIG_DIR);
      dbProperty.setShowNextTime(false);
      systemConfigService.saveOrUpdateDBProperty(dbProperty);
    } catch (IOException e1) {
      GV.showMessageDialog(null, e1.getMessage());
      e1.printStackTrace();
    }
  }

  /**
   * 在招标文件节点上直接导出招标文件到用户指定的地方
   *
   * @param ssTree
   * @throws Exception
   */
  public void doExecuteZbFileCopy(SingleSeletionTree ssTree) throws Exception {
    SmartTreeNode currNode = ssTree.getCurrentNode();
    if (currNode == null
      || (!GV.NODE_TYPE_ZB.equals(currNode.getNodeType()) && !GV.NODE_TYPE_PROJECT.equals(currNode.getNodeType()) && (GV.NODE_TYPE_DOC
        .equals(currNode.getNodeType()) && GV.NODE_NAME_ZB.indexOf(currNode.getNodeName()) == -1))) {
      return;
    }
    String sourceWord = null;
    String pjName = null;
    SmartTreeNode pjNode = null;
    if (GV.NODE_TYPE_ZB.equals(currNode.getNodeType()) || GV.NODE_TYPE_PROJECT.equals(currNode.getNodeType())) {
      SmartTreeNode zbFileNode = PubFunction.getNeedingNodeInNodeNameInChildren(currNode, GV.NODE_TYPE_DOC, GV.NODE_NAME_ZB);
      if (zbFileNode == null) {
        return;
      }
      sourceWord = zbFileNode.getNodesFullPathWithExtIfExist();
      pjNode = PubFunction.getNeedingNodeInParent(currNode, GV.NODE_TYPE_PROJECT);
    } else if (GV.NODE_TYPE_DOC.equals(currNode.getNodeType()) && GV.NODE_NAME_ZB.indexOf(currNode.getNodeName()) >= 0) {
      sourceWord = currNode.getNodesFullPathWithExtIfExist();
      pjNode = PubFunction.getNeedingNodeInParent(currNode, GV.NODE_TYPE_PROJECT);
    }
    if (pjNode == null) {
      return;
    }
    pjName = pjNode.getNodeName() + GV.getSimpleMsg("zbBookName") + GV.SUFFIX_DOC;
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setAcceptAllFileFilterUsed(true);
    fileChooser.setCurrentDirectory(new File(GV.getHistoryDirectory()));
    int result = fileChooser.showSaveDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      String srcPath = GV.getImportFileDir_FromRoot().append(sourceWord).toString();
      String selPath = fileChooser.getSelectedFile().getAbsolutePath();
      PubFunction.copyFile(srcPath, selPath + File.separator + pjName);
      GV.showMessageDialog(null, GV.getSimpleMsg("wordExportPath") + selPath + File.separator + pjName);
      GV.setHistoryDirectory(selPath);
    }
  }

  public MainPanel getMainPanel() {
    return mainPanel;
  }

  public void setMainPanel(MainPanel mainPanel) {
    this.mainPanel = mainPanel;
  }

  public JFrame getRootContainer() {
    return rootContainer;
  }

  public void setRootContainer(JFrame rootContainer) {
    this.rootContainer = rootContainer;
  }

  public void doExecuteWordMerge(SmartTreeNode currNode) throws Exception {
    if (currNode == null || (!GV.NODE_TYPE_TB.equals(currNode.getNodeType()) && !GV.NODE_TYPE_PACK.equals(currNode.getNodeType()))) {
      return;
    }
    SmartTreeNode packNode = null;
    SmartTreeNode tbNode = null;
    if (GV.NODE_TYPE_TB.equals(currNode.getNodeType())) {
      packNode = (SmartTreeNode) currNode.getParent();
      tbNode = currNode;
    } else if (GV.NODE_TYPE_PACK.equals(currNode.getNodeType())) {
      packNode = currNode;
      tbNode = PubFunction.getNeedingNodeInChildren(currNode, GV.NODE_TYPE_TB);
    }
    if (tbNode == null) {
      return;
    }
    List<String> list = new ArrayList<String>();
    FileExportService.getFilePathList(tbNode, list, GV.getImportFileDir_FromRoot().toString());
    //去除非word文件
    for (int i = 0; i < list.size(); i++) {
      String currPath = list.get(i).toLowerCase();
      if (!currPath.endsWith("doc")) {
        String docPath = currPath.replace(currPath.substring(currPath.lastIndexOf("."), currPath.length()), "doc");
        toGenerateWordPriceTableByXml(currPath, docPath);
        list.set(i, docPath);
      }
    }
    boolean isNew = false;
    String packName = packNode.getNodeName();
    SmartTreeNode treeNode = null;
    String detailName = GV.getOperateMsg("mergedWordName", packName);
    SmartTreeNode existNode = PubFunction.getNeedingNodeWithNodeNameInChildren(packNode, GV.NODE_TYPE_DOC, detailName);
    if (existNode == null) {
      PackDetailsService packDetailsService = new PackDetailsService();
      try {
        treeNode = packDetailsService.createChildNode(packNode, GV.NODE_TYPE_DOC, detailName, "");
        treeNode.setForExtention4("NOT_UPLOAD");
      } catch (Exception e1) {
        GV.showMessageDialog(mainPanel.getRootPane(), e1.getMessage());
        e1.printStackTrace();
        return;
      }
      isNew = true;
    } else {
      treeNode = existNode;
      isNew = false;
    }
    boolean flag = (treeNode == null) ? false : true;
    if (!flag) {
      String tigMeg = GV.getOperateMsg("addNewNode.failure", detailName);
      GV.showMessageDialog(mainPanel.getRootPane(), tigMeg);
      return;
    }
    try {
      mainPanel.refreshLeftFilesTreePanel();
    } catch (Exception ee) {
    }
    mainPanel.getSingleSeletionTree().setCurrentNode(treeNode);
    if (mainPanel.getOpenedLeavesNode() == null || !treeNode.getNodeGUID().equals(mainPanel.getOpenedLeavesNode().getNodeGUID())) {
      mainPanel.showTreeNodeInfo(GV.getImportFileDir_FromRoot().append(treeNode.getNodesFullPathWithExtIfExist()).toString());
    }
    if (mainPanel.getWordPane() != null) {
      if (isNew) {
        int sel = JOptionPane.showConfirmDialog(null, GV.getOperateMsg("wordSizeAlert", detailName), GV.getSimpleMsg("asking"), 0, 3);
        if (sel == JOptionPane.NO_OPTION) {
          return;
        }
      } else {
        int sel = JOptionPane.showConfirmDialog(null, GV.getSimpleMsg("mergeWordAlert"), GV.getSimpleMsg("asking"), 0, 3);
        if (sel == JOptionPane.NO_OPTION) {
          return;
        }
      }
      mainPanel.getWordPane().combineMsWord(list);
    } else {
      throw new Exception(GV.getSimpleMsg("noMergeableWord"));
    }
    String tigMeg = GV.getOperateMsg("wordMerge.success", detailName);
    GV.showMessageDialog(mainPanel.getRootPane(), tigMeg);
  }

  /**
   * 自动根据报价xml生成word表格
   */
  private void toGenerateWordPriceTableByXml(String xmlPath, String docPath) {
    GenPriceWordTable genWordTable = new GenPriceWordTable();
    try {
      File tmpFile = new File(docPath);
      genWordTable.startGenPriceWordTable("", GV.NODE_NAME_TBYLB, xmlPath, tmpFile.getName(), tmpFile.getParent());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
