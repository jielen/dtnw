package com.ufgov.zc.client.zc.ztb.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.ufgov.zc.client.zc.ztb.JobThreads;
import com.ufgov.zc.client.zc.ztb.activex.WordPane;
import com.ufgov.zc.client.zc.ztb.model.InvisibleTreeModel;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;
import com.ufgov.zc.client.zc.ztb.service.InitZtbTreeService;
import com.ufgov.zc.client.zc.ztb.table.TableBuilder;
import com.ufgov.zc.client.zc.ztb.util.ActionMaps;
import com.ufgov.zc.client.zc.ztb.util.EventPropertyName;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.client.zc.ztb.util.PubFunction;
import com.ufgov.zc.client.zc.ztb.util.StaticContainerMaps;
import com.ufgov.zc.common.system.util.UUID;

public class MainPanel extends JPanel {
  private static final long serialVersionUID = 2735299824656847020L;

  public JSplitPane splitPane;

  protected JTabbedPane leftPane;

  protected JTabbedPane initRightTabPane = new JTabbedPane();

  protected JPanel filesTreePanel;

  protected JPanel responsePointsPanel;

  protected JPanel rightPanel;

  protected JToolBar toolBar;

  protected ZTBButton saveBtn;

  protected SingleSeletionTree singleSeletionTree;

  protected SingleSeletionTree responsePointsTree;

  protected InitZtbTreeService initZtbTreeService;

  protected WordPane wordPane;

  protected SmartTreeNode openedPackNode;

  protected SmartTreeNode openedLeavesNode;

  protected TableBuilder tableBuilder;

  protected MainPanel self = this;

  public boolean isOpendWord;

  public boolean openAndProtect = false;

  public boolean isZbFileAudit = false;

  private boolean isRPTreeShowInFrame = false;

  private JFrame rpFrame = new JFrame();

  private boolean addTrackRevisions = false;

  private String userName = "";

  protected Window parentEntity;

  private ImageIcon workFlowIcon;

  private boolean rightMouseEnable = true;

  //当前实例是谁，zbpanel或者tbpanel 制作招标文件和投标文件的工具都是继承MainPanel
  protected static String WHO_I_AM = "";

  public boolean canEdit = false;//控制界面的是否可编辑

  public MainPanel() {
    initZtbTreeService = new InitZtbTreeService();
    StaticContainerMaps.setMainPanel(this);
  }

  public void initPanel() {
    try {
      initLeftPanel();
      initRightPanel();
      initSplitPanel();
      expandOrClose(false, false);
      paintPanel();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 展开或者收起所有
   *
   * @param expandOrClose
   */
  public void expandOrClose(boolean expandOrClose, boolean force) {
    if (singleSeletionTree != null) {
      JTree jTree = singleSeletionTree.getTree();
      expandAll(jTree, new TreePath(jTree.getModel().getRoot()), expandOrClose, force);
    }
  }

  private void expandAll(JTree tree, TreePath parent, boolean expand, boolean isForce) {
    SmartTreeNode node = (SmartTreeNode) parent.getLastPathComponent();
    if (node.getChildCount() > 0) {
      //暂时不打开投标文件的节点

      //如果是强制，那么完全展开或者合拢
      for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
        SmartTreeNode node1 = (SmartTreeNode) e.nextElement();
        if (node1.getNodeType().equals(GV.NODE_TYPE_TB)) {
          continue;
        }

        expandAll(tree, parent.pathByAddingChild(node1), expand, isForce);
      }
      if (getNodeShouldUnfoldOrFold(node, expand, isForce)) {
        tree.expandPath(parent);
        node.setUnfold(true);
      } else {
        tree.collapsePath(parent);
        node.setUnfold(false);
      }
    }
  }

  /**
   * 如果是项目列表、根节点、标段节点，必须全部展开，
   * 否则如果是强制展开，那么直接返回真以展开，
   * 如果不是强制展开，那么根据原来的状态返回
   *
   * @param node
   * @param expand
   * @param isForce
   * @return
   */
  private boolean getNodeShouldUnfoldOrFold(SmartTreeNode node, boolean expand, boolean isForce) {
    boolean isUnfold = node.isUnfold();
    String nodeType = node.getNodeType();
    if (GV.NODE_TYPE_ROOT.equals(nodeType) || GV.NODE_TYPE_RP_ROOT.equals(nodeType) || GV.NODE_TYPE_PROJECT_LIST.equals(nodeType)) {
      return true;
    } else if (GV.NODE_TYPE_PROJECT.equals(nodeType) || GV.NODE_TYPE_PACK.equals(nodeType) || GV.NODE_TYPE_ZB.equals(nodeType)) {
      return true;
    } else if (isForce) {
      return expand;
    } else {
      return isUnfold;
    }
  }

  /**
   * 关闭或者展开其它
   *
   * @param parent
   * @param flag，true为展开
   * @param isForce
   */
  public void expandOrCloseOthers(TreePath parent, boolean flag) {
    SmartTreeNode node = (SmartTreeNode) parent.getLastPathComponent();
    if (node.getChildCount() >= 0 && node.isUnfold()) {
      for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
        expandOrCloseOthers(parent.pathByAddingChild(e.nextElement()), flag);
      }
    }
    if (flag && node.isUnfold() || GV.NODE_TYPE_ROOT.equals(node.getNodeType())) {
      this.singleSeletionTree.getTree().expandPath(parent);
      node.setUnfold(true);
    } else {
      this.singleSeletionTree.getTree().collapsePath(parent);
      node.setUnfold(false);
    }
  }

  /**
   * 隐藏或者显示树中【选中】的节点及其子节点
   *
   * @param tree
   * @param isVisible 是否显示指定节点
   * @param isActiveFilter如果激活过滤器，那么将不显示被隐藏的节点
   *
   */
  public void setNodeVisibleWithNodeSelected(SingleSeletionTree stree, boolean isVisible, boolean isActiveFilter) {
    InvisibleTreeModel model = (InvisibleTreeModel) stree.getTree().getModel();
    TreePath[] path = stree.getTree().getSelectionPaths();
    SmartTreeNode node = null;
    for (int i = 0; i < path.length; i++) {
      node = (SmartTreeNode) path[i].getLastPathComponent();
      if (!(node == model.getRoot() || GV.NODE_TYPE_PACK_RP.equals(stree.getCurrentNode().getNodeType()))) {
        node.setVisible(isVisible);
      }
    }
    model.activateFilter(isActiveFilter);
    model.reload();
  }

  /**
   * 隐藏或者显示树中【选中】的节点及其子节点，同时将其它节点隐藏
   *
   * @param tree
   * @param nodeCode
   * @param isVisible 是否显示指定节点
   * @param isActiveFilter如果激活过滤器，那么将不显示被隐藏的节点
   *
   */
  public void setNodeVisibleWithNodeCode(SingleSeletionTree stree, String nodeCode, boolean isVisible, boolean isActiveFilter) {
    InvisibleTreeModel model = (InvisibleTreeModel) stree.getTree().getModel();
    List<SmartTreeNode> list = new ArrayList<SmartTreeNode>();
    PubFunction.doSearchAllMatchingNeedingNodeFromCurrNode(stree.getTreeNode(), "nodeType", GV.NODE_TYPE_PACK_RP, false, -1, list);
    for (int i = 0; i < list.size(); i++) {
      SmartTreeNode item = list.get(i);
      if (!nodeCode.equals(item.getNodeCode())) {
        item.setVisible(false);
      } else {
        item.setVisible(true);
      }
    }
    model.activateFilter(isActiveFilter);
    model.reload();
  }

  /**
   * 隐藏当前nodeCode对应的树中【选中】的节点及其子节点
   *
   * @param tree
   * @param isVisible 是否显示指定节点
   * @param isActiveFilter如果激活过滤器，那么将不显示被隐藏的节点
   *
   */
  public void setNodeHiddenWithNodeCode(SingleSeletionTree stree, String nodeCode, boolean isVisible, boolean isActiveFilter) {
    InvisibleTreeModel model = (InvisibleTreeModel) stree.getTree().getModel();
    List<SmartTreeNode> list = new ArrayList<SmartTreeNode>();
    PubFunction.doSearchAllMatchingNeedingNodeFromCurrNode(stree.getTreeNode(), "nodeType", GV.NODE_TYPE_PACK_RP, false, -1, list);
    for (int i = 0; i < list.size(); i++) {
      SmartTreeNode item = list.get(i);
      if (nodeCode.equals(item.getNodeCode())) {
        item.setVisible(false);
      }
    }
    model.activateFilter(isActiveFilter);
    model.reload();
  }

  public void setTrackRevisions(boolean flag, String userName) {
    this.addTrackRevisions = flag;
    this.userName = userName;
  }

  public void paintPanel() {
    this.setLayout(new BorderLayout());
    this.add(this.toolBar, BorderLayout.NORTH);
    this.add(this.splitPane, BorderLayout.CENTER);
    this.preToLoadProjInfo();
  }

  public void setSaveBtn(boolean flag) {
    saveBtn.setVisible(flag);
  }

  public void preToLoadProjInfo() {
    if ("TTB".equals(WHO_I_AM) || "TB".equals(WHO_I_AM)) {
      workFlowIcon = GV.getImageIcon("tbworkflow.png");
    } else {
      workFlowIcon = GV.getImageIcon("zbworkflow.png");
    }
    this.closeWordPane(true);
    JTree jTree = singleSeletionTree.getTree();
    SmartTreeNode rootNode = (SmartTreeNode) jTree.getModel().getRoot();
    if (rootNode.getChildCount() > 0) {
      SmartTreeNode projNode = (SmartTreeNode) rootNode.getChildAt(0);
      this.loadRightInitInfoPanel(projNode.getNodeCode());
    } else {
      loadRightInitInfoPanel();
    }
  }

  /**
   * 加载投标基本流程显示在软件的右边空白部分
   *
   * @param projCode
   */
  public void loadRightInitInfoPanel() {
    initRightTabPane.removeAll();
    JPanel workFlow = new JPanel();
    JLabel label = new JLabel(workFlowIcon);
    workFlow.add(label);
    JScrollPane scPane = new JScrollPane();
    scPane.setPreferredSize(new Dimension(1000, 700));
    scPane.getViewport().add(workFlow);
    initRightTabPane.add(scPane, GV.getSimpleMsg("zbFlowChartTabTitle"));
    splitPane.setDividerLocation(splitPane.getDividerLocation() - 1);
    splitPane.setRightComponent(initRightTabPane);
    splitPane.setDividerLocation(splitPane.getDividerLocation() + 1);
  }

  /**
   * 加载项目信息显示在软件的右边空白部分
   *
   * @param projCode
   */
  public void loadRightInitInfoPanel(String projCode) {
    initRightTabPane.removeAll();
    ProjectInfoPanel projInfoPanel = new ProjectInfoPanel(projCode);
    projInfoPanel.setPreferredSize(new Dimension(400, 400));
    initRightTabPane.add(projInfoPanel, GV.getSimpleMsg("projSummaryInfo"));
    JPanel workFlow = new JPanel();
    JLabel label = new JLabel(workFlowIcon);
    workFlow.add(label);
    //去掉招标流程示意图
    //    JScrollPane scPane = new JScrollPane();
    //    scPane.setPreferredSize(new Dimension(1000, 700));
    //    scPane.getViewport().add(workFlow);
    //    initRightTabPane.add(scPane, GV.getSimpleMsg("zbFlowChartTabTitle"));
    //    initRightTabPane.setSelectedIndex(1);
    splitPane.setDividerLocation(splitPane.getDividerLocation() - 1);
    splitPane.setRightComponent(initRightTabPane);
    splitPane.setDividerLocation(splitPane.getDividerLocation() + 1);
  }

  public void initToolBar() {
    this.toolBar = new JToolBar();
    this.toolBar.setFloatable(true);
    this.toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    saveBtn = new ZTBButton("save");
    toolBar.add(saveBtn);
    CommonAction action = (CommonAction) ActionMaps.getActionsMap().get("savePackDetails");
    action.setComponent(this);
    saveBtn.addActionListener(action);
  }

  /**
   * 
  * @Description: 初始化招投标左边面板
  * @return void 返回类型
  * @since 1.0
   */
  public void initLeftPanel() throws Exception {
    initLeftFilesTreePanel();
    leftPane = new JTabbedPane();
    addActions();
    leftPane.add(getFilesTreePanel(), GV.getSimpleMsg("tbFileTabTitle"));
    SmartTreeNode packNode = PubFunction.getNeedingNodeInChildren(this.singleSeletionTree.getTreeNode(), GV.NODE_TYPE_PACK);
    if (GV.DIS_TOUBIAO_ONLY.equals(WHO_I_AM) && packNode != null) {
      leftPane.add(getResponsePointsPanel(packNode), GV.getSimpleMsg("bidBookNavigation"));
    }
    leftPane.setSelectedIndex(getPanelIndexWithSuffix("_FN"));
  }

  private int getPanelIndexWithSuffix(String suffix) {
    for (int i = 0; i < leftPane.getComponentCount(); i++) {
      Component cp = leftPane.getComponentAt(i);
      if (cp.getName().endsWith(suffix)) {
        return i;
      }
    }
    return 0;
  }

  /**
   * 获得标书文件树panel
   *
   * @return
   */
  private JComponent getFilesTreePanel() {
    leftPane.remove(filesTreePanel);
    filesTreePanel = null;
    filesTreePanel = new JPanel(new BorderLayout());
    JScrollPane scrollTreePane = new JScrollPane(singleSeletionTree);
    filesTreePanel.setName("_FN");
    leftPane.setMinimumSize(new Dimension(200, 0));
    filesTreePanel.add(getToolBar(singleSeletionTree, "12"), BorderLayout.NORTH);

    filesTreePanel.add(scrollTreePane, BorderLayout.CENTER);
    return filesTreePanel;
  }

  /**
   * 获得工具条
   *
   * @param ssTree
   * @return
   */
  private JToolBar getToolBar(SingleSeletionTree ssTree, String filter) {
    JToolBar tb = new JToolBar();
    tb.setRollover(true);
    tb.setAlignmentY(0.7f);
    if (filter.indexOf("1") != -1) {
      tb.add(getUnfoldButton(ssTree));
    }
    if (filter.indexOf("2") != -1) {
      tb.add(getFoldButton(ssTree));
    }
    if (filter.indexOf("3") != -1) {
      tb.add(getDisplayAllNodesButton(ssTree));
    }
    tb.setBackground(Color.PINK);
    tb.setForeground(Color.ORANGE);
    return tb;
  }

  private JButton getUnfoldButton(final SingleSeletionTree ssTree) {
    JButton bt = new JButton(GV.getImageIcon().get("unfold"));
    bt.setToolTipText(getHtmlText(GV.getSimpleMsg("unfoldAll")));
    bt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        TreePath tp = new TreePath(ssTree.getTreeNode().getRoot());
        expandAll(ssTree.getTree(), tp, true, true);
      }
    });
    return bt;
  }

  private JButton getFoldButton(final SingleSeletionTree ssTree) {
    JButton bt = new JButton(GV.getImageIcon().get("fold"));
    bt.setToolTipText(getHtmlText(GV.getSimpleMsg("foldAll")));
    bt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        TreePath tp = new TreePath(ssTree.getTreeNode().getRoot());
        expandAll(ssTree.getTree(), tp, false, true);
      }
    });
    return bt;
  }

  private JButton getDisplayAllNodesButton(final SingleSeletionTree ssTree) {
    JButton bt = new JButton(GV.getImageIcon().get("full"));
    bt.setToolTipText(getHtmlText(GV.getSimpleMsg("displayAll")));
    bt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        InvisibleTreeModel model = (InvisibleTreeModel) ssTree.getTree().getModel();
        model.activateFilter(false);
        model.reload();
        TreePath tp = new TreePath(ssTree.getTreeNode().getRoot());
        expandAll(ssTree.getTree(), tp, true, true);
      }
    });
    return bt;
  }

  private String getHtmlText(String msg) {
    return "<html><font size='4' color='red'>" + msg + "</font></html>";
  }

  /**
   * 获得响应点树panel
   *
   * @param packNode
   * @return
   * @throws Exception
   */
  private JComponent getResponsePointsPanel(SmartTreeNode packNode) throws Exception {
    leftPane.remove(responsePointsPanel);
    responsePointsPanel = null;
    responsePointsPanel = new JPanel(new BorderLayout());
    initLeftResponsePointsTree(packNode);
    JScrollPane scrollTreePane = new JScrollPane(responsePointsTree);
    responsePointsPanel.setName("_RP");
    responsePointsPanel.add(getToolBar(responsePointsTree, "123"), BorderLayout.NORTH);
    responsePointsPanel.add(scrollTreePane, BorderLayout.CENTER);
    return responsePointsPanel;
  }

  /**
   * 给投标文件响应点树页签添加响应事件，如果双击的是响应点页签，那么弹出一个JFrame
   */
  private void addActions() {
    leftPane.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        JTabbedPane tbp = (JTabbedPane) e.getSource();
        if (e.getClickCount() == 2 && tbp.getSelectedComponent().getName().endsWith("_RP")) {
          makeResponsePointFrame(e);
        }
      }
    });
  }

  /**
   * 创建一个弹出的新增响应点的JFrame
   *
   * @param e
   */
  public void makeResponsePointFrame(MouseEvent e) {
    if (rpFrame == null) {
      rpFrame = new JFrame();
    }
    rpFrame.setTitle("标书导航响应点");
    rpFrame.toFront();
    rpFrame.setIconImage(rpFrame.getToolkit().getImage(GV.getImageUrl("windowicon.jpg")));
    rpFrame.setAlwaysOnTop(true);
    rpFrame.setSize(new Dimension(280, 600));
    rpFrame.setLayout(new BorderLayout());
    rpFrame.add(responsePointsPanel, BorderLayout.CENTER);
    rpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    rpFrame.setLocation(new Point(e.getXOnScreen(), e.getYOnScreen()));
    rpFrame.setExtendedState(JFrame.NORMAL);
    rpFrame.setVisible(true);
    if (this.getOpenedPackNode() != null) {
      setNodeVisibleWithNodeCode(this.responsePointsTree, this.getOpenedPackNode().getNodeCode(), true, true);
    }
    rpFrame.repaint();
    isRPTreeShowInFrame = true;
    rpFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        try {
          isRPTreeShowInFrame = false;
          refreshLeftResponsePointsTreePanel();
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }

      public void windowIconified(WindowEvent e) {
        try {
          rpFrame.dispose();
          isRPTreeShowInFrame = false;
          refreshLeftResponsePointsTreePanel();
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
  }

  public void refreshLeftFilesTreePanel() throws Exception {
    initLeftFilesTreePanel();
    expandOrClose(true, false);
    leftPane.add(getFilesTreePanel(), GV.getSimpleMsg("tbFileTabTitle"));
    leftPane.setSelectedIndex(getPanelIndexWithSuffix("_FN"));
  }

  public void refreshLeftTree(TreePath treePath) {
    if (singleSeletionTree != null) {
      singleSeletionTree.getTree().revalidate();
      singleSeletionTree.getTree().repaint();
      singleSeletionTree.getTree().expandPath(treePath);
      singleSeletionTree.getTree().scrollPathToVisible(treePath);
    }
  }

  /**
   * 
  * @Description: TODO(初始化左侧节点树)
  * @return void 返回类型
  * @since 1.0
   */
  public void initLeftFilesTreePanel() throws Exception {
    singleSeletionTree = null;
    singleSeletionTree = initZtbTreeService.createFilesTreePanel(self);
    singleSeletionTree.setPreferredSize(new Dimension(160, 320));
    singleSeletionTree.setRightMouseEnable(rightMouseEnable);
    SmartTreeNode rootNode = (SmartTreeNode) singleSeletionTree.getTreeNode();
    if (rootNode.getChildCount() > 0) {
      SmartTreeNode projNode = (SmartTreeNode) rootNode.getChildAt(0);
      StaticContainerMaps.setProjNode(projNode);
    }

    //在singleselectionTree中通过firepropertychange触发
    singleSeletionTree.addPropertyChangeListener(EventPropertyName.MOUSE_CLICK_PROPERTY_NAME, new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        //showTreeNodeMeg();
      }
    });
    singleSeletionTree.addPropertyChangeListener(EventPropertyName.MOUSE_DBCLICK_PROPERTY_NAME, new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        showTreeNodeInfo(null);
      }
    });
  }

  public void initLeftResponsePointsTree(SmartTreeNode packNode) throws Exception {
    responsePointsTree = initZtbTreeService.createPointsTreePanel(self, packNode);
    responsePointsTree.setPreferredSize(new Dimension(160, 320));
    responsePointsTree.addPropertyChangeListener(EventPropertyName.MOUSE_DBCLICK_PROPERTY_NAME, new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        showTreeNodeInfoRP(null);
      }
    });
  }

  private static String lastOpenPath = "";

  /**
   * 响应点对应文件显示
   *
   * @param path
   */
  public void showTreeNodeInfoRP(String path) {
    //添加一个参数,是否是在评标的时候打开。
    final SmartTreeNode currNode = this.responsePointsTree.getCurrentNode();
    if (currNode != null) {
      System.out.println("HistoryPaht:" + lastOpenPath);
      if (lastOpenPath.equals(currNode.getNodeDirPath()) && !"".equals(lastOpenPath)) {
        wordPane.selectBookMark(currNode.getNodeGUID());
        MainPanel.setLastDoubleClickResponseSuccess(true);
        return;
      }
      closeWordPane();
      String nodeType = currNode.getNodeType();
      String isShowRMB = "N";
      if (GV.NODE_TYPE_TBYLB.equals(nodeType) || GV.NODE_TYPE_ECBJB.equals(nodeType)) {
        isShowRMB = "Y";
      }
      final String filePath = GV.getImportFileDir_FromRoot().append(currNode.getNodeDirPath()).toString();
      if (GV.NODE_TYPE_DOC.equals(nodeType) || GV.NODE_TYPE_RESPONSE_POINT.equals(nodeType)) {
        if (wordPane == null) {
          wordPane = new WordPane();
          wordPane.setMinimumSize(new Dimension(10, 1000));
          wordPane.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
              // 打开文件完成之后的回调函数
              splitPane.setDividerLocation(splitPane.getDividerLocation() - 1);
              boolean success = (Boolean) evt.getNewValue();
              MainPanel.setLastDoubleClickResponseSuccess(true);
              self.firePropertyChange(WordPane.EVENT_NAME_OPEN_CALLBACK, !success, success);
            }
          });
        }
        // 一定要先把wordPane加入到splitPane，之后再让wordPane加载word（调用wordPane.openAndProtect方法）,否则会有线程问题。
        splitPane.setRightComponent(wordPane);
        splitPane.setDividerLocation(splitPane.getDividerLocation() + 1);
        if (openAndProtect) {
          wordPane.openAndProtect(filePath, UUID.randomUUID().toString());
          JobThreads.startFileOperatorThread(self, "RO", filePath);
        } else {
          wordPane.open(filePath);
          JobThreads.startFileOperatorThread(self, "R", filePath);
        }
        addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {
          public void propertyChange(PropertyChangeEvent evt) {
            //打开文件完成之后的回调函数
            boolean isSuccess = (Boolean) evt.getNewValue();
            MainPanel.setLastDoubleClickResponseSuccess(true);
            removePropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, this);
            if (isSuccess) {
              wordPane.selectBookMark(currNode.getNodeGUID());
              lastOpenPath = currNode.getNodeDirPath();
            }
            //这个updateUI很重要，解决使用提示框后无法刷新word页面问题
            self.updateUI();
          }
        });
      } else if (GV.NODE_TYPE_TABLE.equals(nodeType) || GV.NODE_TYPE_TBYLB.equals(nodeType) || GV.NODE_TYPE_ECBJB.equals(nodeType)) {
        tableBuilder = null;
        tableBuilder = new TableBuilder();
        String detailsName = currNode.getNodeName();
        String packName = PubFunction.getNeedingNodeInParent(currNode, GV.NODE_TYPE_PACK).getNodeName();
        String projName = PubFunction.getNeedingNodeInParent(currNode, GV.NODE_TYPE_PROJECT).getNodeName();
        JPanel panel = null;
        String simpleName = this.getClass().getName();
        if (simpleName.endsWith("TBPanel")) {
          List<String> showInfList = new ArrayList<String>();
          showInfList.add(projName);
          showInfList.add(packName);
          showInfList.add(detailsName);
          showInfList.add(isShowRMB);
          panel = tableBuilder.buildTBPanel(showInfList, filePath);
        }
        splitPane.setRightComponent(panel);
      } else if (GV.NODE_TYPE_PROJECT.equals(nodeType) || GV.NODE_TYPE_PACK.equals(nodeType) || GV.NODE_TYPE_PACK_BUSINESS.equals(nodeType)
        || GV.NODE_TYPE_PACK_TECH.equals(nodeType)) {
        this.preToLoadProjInfo();
      }
      splitPane.setDividerLocation(splitPane.getDividerLocation());
    }
  }

  /**
   * 刷新左边响应点节点树
   *
   * @throws Exception
   */
  public void refreshLeftResponsePointsTreePanel() throws Exception {
    SmartTreeNode packNode = this.getOpenedPackNode();
    if (packNode == null) {
      packNode = PubFunction.getNeedingNodeInChildren(this.singleSeletionTree.getTreeNode(), GV.NODE_TYPE_PACK);
    }
    if (!this.isRPTreeShowInFrame) {
      leftPane.add(this.getResponsePointsPanel(packNode), GV.getSimpleMsg("bidBookNavigation"));
    } else {
      this.rpFrame.remove(responsePointsPanel);
      this.rpFrame.add(this.getResponsePointsPanel(packNode), BorderLayout.CENTER);
      this.rpFrame.setExtendedState(JFrame.NORMAL);
      this.rpFrame.setVisible(true);
    }
    this.toShowRPTreeTab();
  }

  /**
   * 将相应点页签切换到前面显示
   */
  protected void toShowRPTreeTab() {
    for (int i = 0; i < this.leftPane.getComponentCount(); i++) {
      if (this.leftPane.getComponent(i).getName().endsWith("_RP")) {
        this.leftPane.setSelectedIndex(i);
        return;
      }
    }
  }

  /**
   * 用于控制双击响应，如果上次双击事件还没有完成响应，那么阻止最近一次双击事件继续响应，
   * 以解决线程冲突问题，从而防止word显示不正确，需要初始化为true。
   */
  private static boolean isLastDoubleClickResponseSuccess = true;

  /**
   * 响应显示双击节点事件
   */
  public void showTreeNodeInfo(String path) {
    //添加一个参数,是否是在评标的时候打开。
    final SmartTreeNode currNode = singleSeletionTree.getCurrentNode();
    if (currNode != null) {
      closeWordPane();
      //如果path不为空，该方法是在评标模块调用，不需要保存表格
      if (!openAndProtect) {
        saveTableBuilderData();
      }
      String nodeType = currNode.getNodeType();
      String isShowRMB = "N";
      if (GV.NODE_TYPE_TBYLB.equals(nodeType) || GV.NODE_TYPE_ECBJB.equals(nodeType)) {
        isShowRMB = "Y";
      }
      if (GV.NODE_TYPE_DOC.equals(nodeType) || GV.NODE_TYPE_RESPONSE_POINT.equals(nodeType)) {
        String filePath;
        if (path != null) {
          filePath = path;
        } else {
          filePath = GV.getAbsoluteImpPath(currNode) + currNode.getNodesFullPath() + currNode.getFileExtension();
        }
        if (wordPane == null) {
          wordPane = new WordPane();
          wordPane.setMinimumSize(new Dimension(10, 1000));
          wordPane.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
              // 打开文件完成之后的回调函数
              splitPane.setDividerLocation(splitPane.getDividerLocation() - 1);
              boolean success = (Boolean) evt.getNewValue();
              MainPanel.setLastDoubleClickResponseSuccess(true);
              self.firePropertyChange(WordPane.EVENT_NAME_OPEN_CALLBACK, !success, success);
            }
          });
        }
        // 一定要先把wordPane加入到splitPane，之后再让wordPane加载word（调用wordPane.openAndProtect方法）,否则会有线程问题。
        splitPane.setRightComponent(wordPane);
        splitPane.setDividerLocation(splitPane.getDividerLocation() + 1);
        if (openAndProtect) {
          wordPane.openAndProtect(filePath, UUID.randomUUID().toString());
          JobThreads.startFileOperatorThread(self, "RO", filePath);
        } else {
          wordPane.open(filePath);
          JobThreads.startFileOperatorThread(self, "R", filePath);
        }
        addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {
          public void propertyChange(PropertyChangeEvent evt) {
            //打开文件完成之后的回调函数
            MainPanel.setLastDoubleClickResponseSuccess(true);
            if (self instanceof TBPanel) {
              wordPane.acceptAllRevisions();
            }
            removePropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, this);
            lastOpenPath = currNode.getNodeDirPath();
            closeAndOpenMatchingNode(currNode);
            //这个updateUI很重要，解决使用提示框后无法刷新word页面问题
            //            self.updateUI();
          }
        });
      } else if (GV.NODE_TYPE_TABLE.equals(nodeType) || GV.NODE_TYPE_TBYLB.equals(nodeType) || GV.NODE_TYPE_ECBJB.equals(nodeType)) {
        String filePath;
        if (path != null) {
          filePath = path;
        } else {
          filePath = GV.getImportFileDir_FromRoot().append(currNode.getNodesFullPath()).toString();
        }

        tableBuilder = null;
        tableBuilder = new TableBuilder();
        String detailsName = currNode.getNodeName();
        String packName = PubFunction.getNeedingNodeInParent(currNode, GV.NODE_TYPE_PACK).getNodeName();
        String projName = PubFunction.getNeedingNodeInParent(currNode, GV.NODE_TYPE_PROJECT).getNodeName();
        JPanel panel = null;
        String simpleName = this.getClass().getName();
        if (simpleName.endsWith("ZBPanel") || simpleName.endsWith("ZcEbZbFilePanel")) {

          if (GV.NODE_TYPE_TBYLB.equals(nodeType)) {
            panel = tableBuilder.buildZBTbylbTablePanel(filePath, openAndProtect);
          } else if (GV.NODE_TYPE_TABLE.equals(nodeType)) {
            panel = tableBuilder.buildZBSettingPanel(filePath, openAndProtect);
          }
        } else if (this.getClass().getName().endsWith("TBPanel")) {
          List<String> showInfList = new ArrayList<String>();
          showInfList.add(projName);
          showInfList.add(packName);
          showInfList.add(detailsName);
          showInfList.add(isShowRMB);
          panel = tableBuilder.buildTBPanel(showInfList, filePath);
        }
        //添加分支，评标时打开招标一栏表
        else if (this.getClass().getName().endsWith("ZcEbEvalTBFilePanel") || this.getClass().getName().endsWith("ZcEbEvalZBFilePanel")) {
          List<String> showInfList = new ArrayList<String>();
          showInfList.add(projName);
          showInfList.add(packName);
          showInfList.add(detailsName);
          showInfList.add(isShowRMB);
          boolean tableIsReadOnly = true;
          panel = tableBuilder.buildTBPanel(showInfList, filePath, tableIsReadOnly);
        }
        splitPane.setRightComponent(panel);
        MainPanel.setLastDoubleClickResponseSuccess(true);
      } else if (GV.NODE_TYPE_PROJECT.equals(nodeType) || GV.NODE_TYPE_PACK.equals(nodeType) || GV.NODE_TYPE_PACK_BUSINESS.equals(nodeType)
        || GV.NODE_TYPE_PACK_TECH.equals(nodeType)) {
        this.preToLoadProjInfo();
        MainPanel.setLastDoubleClickResponseSuccess(true);
      }
      splitPane.setDividerLocation(splitPane.getDividerLocation());
      if (GV.NODE_TYPE_ROOT.equals(currNode.getNodeType()) || GV.NODE_TYPE_PROJECT.equals(currNode.getNodeType())
        || GV.NODE_TYPE_PROJECT_LIST.equals(currNode.getNodeType())) {
        openedPackNode = null;
        openedLeavesNode = null;
      } else {
        openedPackNode = PubFunction.getNeedingNodeInParent(currNode, GV.NODE_TYPE_PACK);
        openedLeavesNode = currNode;
      }
      if (addTrackRevisions && this.userName != null && wordPane != null) {
        wordPane.addTrackRevisions(true, this.userName);
        wordPane.viewTrackRevisions(true);
      }
    }
  }

  /**
   * 子类覆盖
   * 根据当前打开的文件所属标段，控制响应点树中非对应标段的节点为隐藏或合拢起来状态：
   *
   * @param currNode
   */
  public void closeAndOpenMatchingNode(SmartTreeNode currNode) {
  }

  public JSplitPane getSplitPane() {
    return splitPane;
  }

  public void refreshRightPanel() {
    rightPanel = null;
    rightPanel = new JPanel(new BorderLayout());
    splitPane.setRightComponent(rightPanel);
    splitPane.setDividerLocation(splitPane.getDividerLocation());
  }

  public void initRightPanel() {
    rightPanel = new JPanel(new BorderLayout());
  }

  public void doSplitPaneMove() {
  }

  public void initSplitPanel() {
    splitPane = new JSplitPane();
    splitPane.setDividerLocation(280);
    splitPane.setDividerSize(6);
    splitPane.setMinimumSize(new Dimension(0, 0));
    splitPane.setOneTouchExpandable(true);
    splitPane.setLeftComponent(leftPane);
    splitPane.setRightComponent(rightPanel);
    splitPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
          doSplitPaneMove();
        }
      }
    });
  }

  public void closePackDetailsNode() {
    if (openedLeavesNode != null) {
      String openedType = openedLeavesNode.getNodeType();
      if (GV.NODE_TYPE_DOC.equals(openedType)) {
        closeWordPane();
      }
    }
  }

  public void doShowTemplatePanel() throws Exception {
  }

  public void doExecuteRequirementImport() throws Exception {

  }

  public void doExecuteFormulaImport() throws Exception {

  }

  public void doExecuteTemplateSave() throws Exception {
  }

  public SmartTreeNode getOpenedPackNode() {
    return openedPackNode;
  }

  public void setOpenedPackNode(SmartTreeNode openedPackNode) {
    this.openedPackNode = openedPackNode;
  }

  public SmartTreeNode getOpenedLeavesNode() {
    return openedLeavesNode;
  }

  public void setOpenedLeavesNode(SmartTreeNode openedLeavesNode) {
    this.openedLeavesNode = openedLeavesNode;
  }

  public SingleSeletionTree getSingleSeletionTree() {
    return singleSeletionTree;
  }

  public SingleSeletionTree getResponsePointsTree() {
    return responsePointsTree;
  }

  public WordPane getWordPane() {
    return wordPane;
  }

  public TableBuilder getTableBuilder() {
    return tableBuilder;
  }

  public void setTableBuilder(TableBuilder tableBuilder) {
    this.tableBuilder = tableBuilder;
  }

  public boolean saveTableBuilderData() {
    boolean isSaved = false;
    if (this.tableBuilder != null) {
      isSaved = this.tableBuilder.save();
      toExecuteAfterTodo("config");
    }
    return isSaved;
  }

  public static boolean isLastDoubleClickResponseSuccess() {
    return isLastDoubleClickResponseSuccess;
  }

  public static void setLastDoubleClickResponseSuccess(boolean isLastDoubleClickResponseSuccess) {
    MainPanel.isLastDoubleClickResponseSuccess = isLastDoubleClickResponseSuccess;
  }

  public static String getWHO_I_AM() {
    return WHO_I_AM;
  }

  public Window getParentEntity() {
    return parentEntity;
  }

  public void setParentEntity(Window parentEntity) {
    this.parentEntity = parentEntity;
  }

  public boolean saveWordPane() {
    if (this.wordPane == null) {
      return false;
    }
    boolean res = this.wordPane.save();
    if (res) {
      toExecuteAfterTodo("doc");
      ProjectInfoPanel.updateFlowingNOToFile();
    }
    System.out.println("saveWordPane()");
    return res;
  }

  public boolean saveWordPane(File file) {
    if (this.wordPane == null) {
      return false;
    }
    boolean res = this.wordPane.save(file);
    if (res) {
      toExecuteAfterTodo("doc");
    }
    System.out.println("saveWordPane(File file)");
    return res;
  }

  public boolean closeWordPane() {
    if (this.wordPane == null) {
      return false;
    }
    boolean res = this.wordPane.close();
    if (res) {
      toExecuteAfterTodo("doc");
    }
    System.out.println("closeWordPane()");
    return res;
  }

  public boolean closeWordPane(boolean flag) {
    if (this.wordPane == null) {
      return false;
    }
    boolean res = this.wordPane.close(flag);
    if (res) {
      toExecuteAfterTodo("doc");
    }
    System.out.println("closeWordPane(boolean flag)");
    return res;
  }

  public void rotateWordSelectionPage(String type) {
    if (this.wordPane == null) {
      return;
    }
    if ("H".equals(type)) {
      this.wordPane.rotateWordSelectionPage(31, 20);
    } else {
      this.wordPane.rotateWordSelectionPage(20, 31);
    }
    toExecuteAfterTodo("doc");
  }

  public void toExecuteAfterTodo(String fileType) {
  }

  public void setRightMouseEnable(boolean rightMouseEnable) {
    this.rightMouseEnable = rightMouseEnable;
  }
}
