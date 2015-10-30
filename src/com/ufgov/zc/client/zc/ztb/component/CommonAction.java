package com.ufgov.zc.client.zc.ztb.component;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import com.ufgov.zc.client.zc.ztb.JobThreads;
import com.ufgov.zc.client.zc.ztb.component.service.CommonActionService;
import com.ufgov.zc.client.zc.ztb.component.service.CommonActionServiceRP;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.client.zc.ztb.util.PubFunction;

public class CommonAction extends AbstractAction implements ActionListener {
  private static final long serialVersionUID = -378207550605588153L;

  public Object component;

  public String actionId;

  public CommonAction(String actionId, String actionName) {
    super(GV.getTranslate().get(actionName), GV.getImageIcon().get(actionName));
    this.actionId = actionId;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      Method methods = getClass().getDeclaredMethod(actionId, new Class[] {});
      methods.invoke(this, new Object[] {});
    } catch (SecurityException e1) {
      e1.printStackTrace();
    } catch (NoSuchMethodException e1) {
      e1.printStackTrace();
    } catch (InvocationTargetException ee) {
      String msg = ee.getTargetException().getMessage();
      GV.showMessageDialog(null, GV.getSimpleMsg("operatorNotCorrect") + (msg == null || "".equals(msg) ? "不详." : msg));
      ee.printStackTrace();
    } catch (IllegalArgumentException e1) {
      e1.printStackTrace();
    } catch (IllegalAccessException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * 导出项目招标文件word文档
   *
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void exportZbBook() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(ssTree.getMainPanel());
    cas.doExecuteZbFileCopy(ssTree);
  }

  /**
   * 导出项目投标书
   *
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void exportTBProject() throws Exception {
    //    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    //    SmartTreeNode currentNode = ssTree.getCurrentNode();
    //    String content = currentNode.getNodeName();
    //    JobThreads.startExportProjectThread(ssTree, GV.getUpload_tmpPath().toString(), this);
  }

  /**
   * 上传项目投标书
   *
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void uploadProjectAndBid() throws Exception {
  }

  /**
   * 续传项目投标书
   *
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void goonUploadProjectAndBid() throws Exception {
  }

  /**
   * 导入主辅KEY公钥信息
   *
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void fetchPubKey() throws Exception {
    PubFunction.doFetchMainAndMinorPubKey();
  }

  /**
   * 拷贝模板到当前分包下面
   *
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void packCopyFromTemplate() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    MainPanel mainPanel = ssTree.getMainPanel();
    mainPanel.doShowTemplatePanel();
  }

  @SuppressWarnings("unused")
  private void moveuptofirst() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(ssTree.getMainPanel());
    cas.doExecuteMoveToFirst(ssTree);
  }

  @SuppressWarnings("unused")
  private void moveup() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(ssTree.getMainPanel());
    cas.doExecuteMoveUp(ssTree);
  }

  @SuppressWarnings("unused")
  private void movedown() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(ssTree.getMainPanel());
    cas.doExecuteMoveDown(ssTree);
  }

  @SuppressWarnings("unused")
  private void movedowntolast() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(ssTree.getMainPanel());
    cas.doExecuteMoveToLast(ssTree);
  }

  /**
   * 添加新的子节点
   */
  @SuppressWarnings("unused")
  private void addNodeRP() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionServiceRP cas = new CommonActionServiceRP(GV.getCurrFrame());
    cas.setMainPanel(ssTree.getMainPanel());
    cas.doExecuteNodeAddingRP(ssTree);
  }

  @SuppressWarnings("unused")
  private void moveuptofirstRP() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionServiceRP cas = new CommonActionServiceRP(ssTree.getMainPanel());
    cas.setMainPanel(ssTree.getMainPanel());
    cas.doExecuteMoveToFirstRP(ssTree);
  }

  @SuppressWarnings("unused")
  private void moveupRP() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionServiceRP cas = new CommonActionServiceRP(ssTree.getMainPanel());
    cas.doExecuteMoveUpRP(ssTree);
  }

  @SuppressWarnings("unused")
  private void movedownRP() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionServiceRP cas = new CommonActionServiceRP(ssTree.getMainPanel());
    cas.doExecuteMoveDownRP(ssTree);
  }

  @SuppressWarnings("unused")
  private void movedowntolastRP() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionServiceRP cas = new CommonActionServiceRP(ssTree.getMainPanel());
    cas.doExecuteMoveToLastRP(ssTree);
  }

  @SuppressWarnings("unused")
  private void deleteNodeRP() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionServiceRP cas = new CommonActionServiceRP(GV.getCurrFrame());
    cas.setMainPanel(ssTree.getMainPanel());
    cas.doExecuteNodeDeletingRP(ssTree);
  }

  @SuppressWarnings("unused")
  private void renameByBtn() throws Exception {
    /////////功能后续实现////////
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(ssTree.getMainPanel());
    cas.doExecuteRename(ssTree);
  }

  @SuppressWarnings("unused")
  private void packSaveAsTemplate() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    ssTree.getMainPanel().doExecuteTemplateSave();
  }

  @SuppressWarnings("unused")
  private void exportEctbBagDetails() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(ssTree.getMainPanel());
    cas.doExecuteEcbjExport(ssTree);
  }

  /**
   * 上传再次报价
   */
  @SuppressWarnings("unused")
  private void uploadEcbj() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(ssTree.getMainPanel());
    cas.doExecuteUploadEcbj(ssTree);
  }

  /**
   * 基本要点：
   * 添加一个新的再次报价时，在标段目录下与投标文件和招标文件同级的目录下新建
   * 一个再次报价节点，
   *
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void addEctbBagDetails() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(ssTree.getMainPanel());
    cas.doExecuteEcbjAdding(ssTree);
  }

  //导入需求确认信息
  @SuppressWarnings("unused")
  private void dbImportRequirement() throws Exception {
    MainPanel mainPanel = (MainPanel) component;
    mainPanel.doExecuteRequirementImport();
  }

  //导入评标方法
  @SuppressWarnings("unused")
  private void dbImportFormula() throws Exception {
    MainPanel mainPanel = (MainPanel) component;
    mainPanel.doExecuteFormulaImport();
  }

  @SuppressWarnings("unused")
  private void aboutInfo() {
    MainPanel mainPanel = (MainPanel) component;
    String tigMeg = GV.getOperateMsg("about.info", null);
    GV.showMessageDialog(mainPanel.getRootPane(), tigMeg);
  }

  @SuppressWarnings("unused")
  private void toolLocalConfig() {
    CommonActionService cas = new CommonActionService(GV.getCurrFrame());
    cas.setMainPanel((MainPanel) component);
    cas.toolLocalConfig();
  }

  @SuppressWarnings("unused")
  private void savePackDetails() {
    MainPanel mainPanel = (MainPanel) component;
    CommonActionService cas = new CommonActionService(GV.getCurrFrame());
    cas.setMainPanel(mainPanel);
    cas.doExecutePackDetailSaving();
  }

  @SuppressWarnings("unused")
  private void exitProgram() {
    MainPanel mainPanel = (MainPanel) component;
    String meg = GV.getOperateMsg("exitProgram", null);
    if (GV.showConfirmDialog(mainPanel.getRootPane(), meg, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      if (mainPanel.getOpenedLeavesNode() != null) {
        if (GV.NODE_TYPE_DOC.equals(mainPanel.getOpenedLeavesNode().getNodeType())) {
          mainPanel.closeWordPane();
        }
      }
      System.exit(0);
    }
  }

  /**
   * 上传项目招标书
   */
  @SuppressWarnings("unused")
  private void uploadProject() throws Exception {
    if (component instanceof SingleSeletionTree) {
      SingleSeletionTree ssTree = (SingleSeletionTree) component;
      CommonActionService cas = new CommonActionService(GV.getCurrFrame());
      cas.doExecuteProjectUploading(ssTree);
    } else {
      MainPanel mainPanel = (MainPanel) component;
      CommonActionService cas = new CommonActionService(GV.getCurrFrame());
      mainPanel.getSingleSeletionTree().setCurrentNode(null);
      cas.doExecuteProjectUploading(mainPanel.getSingleSeletionTree());
    }
  }

  /**
   * 覆盖上传招标书
   */
  @SuppressWarnings("unused")
  private void uploadZBFile() {
    MainPanel mainPanel = (MainPanel) component;
    CommonActionService cas = new CommonActionService(GV.getCurrFrame());
    cas.setMainPanel(mainPanel);
    cas.doExecuteZBFileUploading();
  }

  /**
   * 导出标段标书
   */
  @SuppressWarnings("unused")
  private void exportPack() {
    if (component instanceof SingleSeletionTree) {
      SingleSeletionTree ssTree = (SingleSeletionTree) component;
      new JobThreads().startExportPackThread(ssTree);
    } else {
      MainPanel mp = (MainPanel) component;
      mp.getSingleSeletionTree().setCurrentNode(null);
      new JobThreads().startExportPackThread(mp.getSingleSeletionTree());
    }
  }

  /**
   * 上传标段标书
   */
  @SuppressWarnings("unused")
  private void uploadPack() {
    /* final List<JRadioButton> rbList = new ArrayList<JRadioButton>();
      Object[] options = new Object[4];
      ButtonGroup group = new ButtonGroup();
      JRadioButton rb = new JRadioButton();
      rb.setText(GV.getSimpleMsg("commonUploadMold"));
      rb.setName(LevelOneItem.UPLOAD_MOLD_COMMON);
      options[0] = rb;
      rbList.add(rb);
      group.add(rb);
      rb = new JRadioButton();
      rb.setSelected(true);
      rb.setText(GV.getSimpleMsg("hightUploadMold"));
      rb.setName(LevelOneItem.UPLOAD_MOLD_HIGHT);
      options[1] = rb;
      rbList.add(rb);
      group.add(rb);
      options[2] = "\n";
      options[3] = GV.getSimpleMsg("hightUploadMoldExplain");
      String selectUploadMold = null;
      String title = GV.getSimpleMsg("uploadMoldSelect");
      int res = JOptionPane.showConfirmDialog(null, options, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (res == JOptionPane.OK_OPTION) {
        selectUploadMold = getSelectedMold(rbList);
      }
      if (selectUploadMold == null) {
        return;
      }
    if (LevelOneItem.UPLOAD_MOLD_COMMON.equals(selectUploadMold)) {//普通上传模式
    */
    String title = GV.getSimpleMsg("confirmUploadBid");
    JLabel label = new JLabel(title);

    int res = JOptionPane.showConfirmDialog(null, label, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (res == JOptionPane.OK_OPTION) {
      if (component instanceof SingleSeletionTree) {
        ((SingleSeletionTree) component).getMainPanel().preToLoadProjInfo();
        new JobThreads().startUploadPackThread((SingleSeletionTree) component);
      } else {
        MainPanel mp = (MainPanel) component;
        mp.preToLoadProjInfo();
        mp.getSingleSeletionTree().setCurrentNode(null);
        new JobThreads().startUploadPackThread(mp.getSingleSeletionTree());
      }
    }

    /* } else {
          if (component instanceof SingleSeletionTree) {
            ((SingleSeletionTree) component).getMainPanel().preToLoadProjInfo();
            new JobThreads().startExportPackThread((SingleSeletionTree) component, LevelOneItem.UPLOAD_MOLD_HIGHT);
          } else {
            MainPanel mp = (MainPanel) component;
            mp.preToLoadProjInfo();
            mp.getSingleSeletionTree().setCurrentNode(null);
            new JobThreads().startExportPackThread(mp.getSingleSeletionTree(), LevelOneItem.UPLOAD_MOLD_HIGHT);
          }
        }
    */
  }

  private String getSelectedMold(List<JRadioButton> rbList) {
    for (int i = 0; i < rbList.size(); i++) {
      if (rbList.get(i).isSelected()) {
        return rbList.get(i).getName();
      }
    }
    return null;
  }

  /**
   * 续传标段标书
   */
  @SuppressWarnings("unused")
  private void goonUploadPackAndBid() {
    Window pWindow = null;
    if (component instanceof SingleSeletionTree) {
      pWindow = ((SingleSeletionTree) component).getMainPanel().getParentEntity();
    } else {
      pWindow = ((MainPanel) component).getParentEntity();
    }
    JFrame nextFrame = new JFrame();
    nextFrame.setSize(new Dimension(1024, 560));
    nextFrame.setIconImage(nextFrame.getToolkit().getImage(GV.getImageUrl("windowicon.jpg")));
    nextFrame.add(new TBRecordsTablePanel((JFrame) pWindow, nextFrame));
    nextFrame.setTitle(GV.getSimpleMsg("myToubiaoList"));
    nextFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    nextFrame.setLocationRelativeTo(null);
    nextFrame.toFront();
    nextFrame.setMinimumSize(new Dimension(1024, 560));
    nextFrame.setVisible(true);
  }

  /**
   * 添加新的子节点
   */
  @SuppressWarnings("unused")
  private void addNode() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(GV.getCurrFrame());
    cas.doExecuteNodeAdding(ssTree);
  }

  @SuppressWarnings("unused")
  private void deleteNode() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(GV.getCurrFrame());
    cas.doExecuteNodeDeleting(ssTree);
  }

  /**
   * 导出项目招标书
   *
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void exportProject() throws Exception {
    SingleSeletionTree ssTree = null;
    if (component instanceof SingleSeletionTree) {
      ssTree = (SingleSeletionTree) component;
    } else {
      ssTree = ((MainPanel) component).getSingleSeletionTree();
      ssTree.setCurrentNode(null);
    }
    CommonActionService cas = new CommonActionService(GV.getCurrFrame());
    cas.doExecuteProjectExporting(ssTree);
  }

  /**
   * 填充模板
   */
  @SuppressWarnings("unused")
  private void fillMold() throws Exception {
    MainPanel mainPanel = (MainPanel) component;
    SingleSeletionTree ssTree = (SingleSeletionTree) mainPanel.getSingleSeletionTree();
    CommonActionService cas = new CommonActionService(GV.getCurrFrame());
    cas.setMainPanel(mainPanel);
    cas.doExecuteMoldFilling();
  }

  /**
   * 给当前word文档加锁
   */
  @SuppressWarnings("unused")
  private void lockWord() throws Exception {
    MainPanel mainPanel = ((MainPanel) component);
    CommonActionService cas = new CommonActionService(GV.getCurrFrame());
    cas.setMainPanel(mainPanel);
    cas.doExecuteWordLocking();
  }

  /**
   * 给当前word文档加锁
   */
  @SuppressWarnings("unused")
  private void unLockWord() throws Exception {
    MainPanel mainPanel = ((MainPanel) component);
    CommonActionService cas = new CommonActionService(GV.getCurrFrame());
    cas.setMainPanel(mainPanel);
    cas.doExecuteWordUnlock();
  }

  /**
   * 将当前标段的内容拷贝到别的标段下
   * 1、获得当前节点及其所有的子节点信息；
   * 2、用户可以通过选择来拷贝需要拷贝的信息；
   */
  @SuppressWarnings("unused")
  private void copyToSameNode() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(GV.getCurrFrame());
    cas.doExecuteSameNodeCopying(ssTree);
  }

  public void displayAllNode() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    ssTree.getMainPanel().setNodeVisibleWithNodeSelected(ssTree, true, false);
  }

  public void hideCurrNode() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    ssTree.getMainPanel().setNodeHiddenWithNodeCode(ssTree, ssTree.getCurrentNode().getNodeCode(), false, true);
  }

  @SuppressWarnings("unused")
  private void importToNode() throws Exception {
    SingleSeletionTree ssTree = (SingleSeletionTree) component;
    CommonActionService cas = new CommonActionService(GV.getCurrFrame());
    cas.doExecuteNodeImporting(ssTree);
  }

  public void mergeWords() throws Exception {
    SingleSeletionTree ssTree = null;
    CommonActionService cas = new CommonActionService(GV.getCurrFrame());
    if (component instanceof SingleSeletionTree) {
      ssTree = (SingleSeletionTree) component;
      cas.setMainPanel(ssTree.getMainPanel());
      cas.doExecuteWordMerge(ssTree.getCurrentNode());
    } else {
      ssTree = ((MainPanel) component).getSingleSeletionTree();
      cas.setMainPanel(ssTree.getMainPanel());
      ssTree.setCurrentNode(null);
      cas.doExecuteWordMerge(getSelectedNode(ssTree));
    }
  }

  private SmartTreeNode getSelectedNode(SingleSeletionTree ssTree) {
    List<SmartTreeNode> list = new ArrayList<SmartTreeNode>();
    PubFunction.doSearchAllMatchingNeedingNodeFromCurrNode(ssTree.getTreeNode(), "nodeType", GV.NODE_TYPE_PACK, false, 0, list);
    SmartTreeNode pNode = null;
    if (list.size() > 1) {
      final List<JRadioButton> rbList = new ArrayList<JRadioButton>();
      Object[] options = new Object[list.size() + 1];
      options[0] = GV.getSimpleMsg("pleaseSelectPacksToMerge");
      ButtonGroup group = new ButtonGroup();
      for (int i = 0; i < list.size(); i++) {
        JRadioButton rb = new JRadioButton();
        rb.setText(list.get(i).getNodeDisplayName());
        rb.setName(list.get(i).getNodeCode());
        options[i + 1] = rb;
        if (i == 0) {
          rb.setSelected(true);
        }
        rbList.add(rb);
        group.add(rb);
      }
      int res = JOptionPane.showConfirmDialog(null, options, GV.getSimpleMsg("bidSelectTitle"), 2, 3);
      if (res == JOptionPane.OK_OPTION) {
        pNode = JobThreads.getSelectedPackNode(rbList, list);
      } else {
        return null;
      }
    } else {
      if (list.size() != 1) {
        JOptionPane.showMessageDialog(null, GV.getSimpleMsg("pleaseSelectOnePack"));
        return null;
      }
      pNode = list.get(0);
    }
    return pNode;
  }

  public Object getComponent() {
    return component;
  }

  public void setComponent(Object component) {
    this.component = component;
  }
}
