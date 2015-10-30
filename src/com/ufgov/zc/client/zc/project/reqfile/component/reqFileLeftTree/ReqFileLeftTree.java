package com.ufgov.zc.client.zc.project.reqfile.component.reqFileLeftTree;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import com.ufgov.zc.client.zc.project.reqfile.ReqFileConstants;
import com.ufgov.zc.client.zc.ztb.activex.WordPane;
import com.ufgov.zc.client.zc.ztb.model.InvisibleTreeModel;
import com.ufgov.zc.client.zc.ztb.util.EventPropertyName;
import com.ufgov.zc.client.zc.ztb.util.GV;

/**
 * 
* @ClassName: reqFileLeftTree
* @Description: TODO(需求确认模块，业务需求面板，左侧树)
* @date: Oct 14, 2012 2:57:05 AM
* @version: V1.0 
* @since: 1.0
* @author: Administrator
* @modify:
 */
public class ReqFileLeftTree extends JPanel {

  private static final long serialVersionUID = 2116938467826570576L;

  public JTree tree;

  private TreePath currentPath;

  private TreePath lastPath;

  private ReqTreeNode currentNode;

  private ReqTreeNode treeNode;

  private InvisibleTreeModel defaultTreeModel;

  private JPanel parentPanel;

  private WordPane wordPane = new WordPane();

  public InvisibleTreeModel getDefaultTreeModel() {
    return defaultTreeModel;
  }

  public ReqFileLeftTree(ReqTreeNode treeNode) {
    this.treeNode = treeNode;
    initComponents();
    expandAll(new TreePath(tree.getModel().getRoot()), true);
    paintScreen();
  }

  public void paintScreen() {
    setLayout(new BorderLayout());
    add(new JScrollPane(tree), BorderLayout.CENTER);
  }

  public void initComponents() {
    InvisibleTreeModel ml = new InvisibleTreeModel(treeNode);
    ml.activateFilter(false);
    this.tree = new JTree(ml);
    this.defaultTreeModel = (InvisibleTreeModel) this.tree.getModel();
    this.tree.expandRow(1);
    this.tree.setRootVisible(true);
    this.tree.setCellRenderer(new ReqTreeCellRenderer());
    //    this.tree.setCellRenderer(new JTreeCellRenderer());
    DefaultTreeSelectionModel defaultTreeSelectionModel = new DefaultTreeSelectionModel();
    defaultTreeSelectionModel.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
    this.tree.setSelectionModel(defaultTreeSelectionModel);
    addTreeListener();

  }

  protected void addTreeListener() {
    this.tree.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        getCurrentNode(e);
        if (currentNode == null) {
          return;
        }
        tree.setToolTipText(currentNode.getNodeName());

        if (ReqFileConstants.NODE_TYPE_DOC.equals(currentNode.getNodeType())) {
          if (SwingUtilities.isRightMouseButton(e)) {
            //            doRightMouseClickRP(e);
          } else {
            if (e.getClickCount() == 2) {
              doLeftMouseDbClick(e);
            }
          }
        }
        if (GV.NODE_TYPE_RP_ROOT.equals(currentNode.getNodeType())) {
          if (SwingUtilities.isRightMouseButton(e)) {
            doRightMouseClickRP(e);
          } else {
            if (e.getClickCount() == 1) {
              doLeftMouseClick(e);
            }
          }
        }
      }
    });

  }

  //展现树结点

  public void expandAll(TreePath parent, boolean expand) {

    ReqTreeNode node = (ReqTreeNode) parent.getLastPathComponent();

    if (node.getChildCount() >= 0) {

      for (Enumeration e = node.children(); e.hasMoreElements();) {

        ReqTreeNode n = (ReqTreeNode) e.nextElement();

        TreePath path = parent.pathByAddingChild(n);

        expandAll(path, expand);

      }

    }

    if (expand) {

      tree.expandPath(parent);

    } else {

      tree.collapsePath(parent);

    }

  }

  /**
   * 响应点树节点右键单击
   *
   * @param e
   */
  protected void doRightMouseClickRP(MouseEvent e) {

  }

  /**
   * 
  * @Description: 双击鼠标左键事件
  * @return void 返回类型
  * @since 1.0
   */
  public void doLeftMouseDbClick(MouseEvent e) {

    //该事件触发后将在mainpanel中进行事件监听和响应
    firePropertyChange(EventPropertyName.MOUSE_DBCLICK_PROPERTY_NAME, false, true);

  }

  /**
   * 
  * @Description: 单击鼠标右键事件
  * @return void 返回类型
  * @since 1.0
   */
  public void doRightMouseClick(MouseEvent e) {

  }

  public void getCurrentNode(MouseEvent e) {
    this.lastPath = currentPath;
    int x = e.getX();
    int y = e.getY();
    int row = tree.getRowForLocation(x, y);
    currentPath = tree.getPathForRow(row);
    if (currentPath != null) {
      currentNode = (ReqTreeNode) currentPath.getLastPathComponent();
      tree.setSelectionPath(currentPath);
      tree.setEditable(false);
    } else {
      currentNode = null;
    }
  }

  public void doLeftMouseClick(MouseEvent e) {
    tree.revalidate();
    tree.repaint();
  }

  public int getCurrentRow() {
    return tree.getRowForPath(currentPath);
  }

  public JTree getTree() {
    return tree;
  }

  public void setTree(JTree tree) {
    this.tree = tree;
  }

  public ReqTreeNode getCurrentNode() {
    return currentNode;
  }

  public void setCurrentNode(ReqTreeNode currentNode) {
    this.currentNode = currentNode;
  }

  public ReqTreeNode getTreeNode() {
    return treeNode;
  }

  public void setTreeNode(ReqTreeNode treeNode) {
    this.treeNode = treeNode;
  }

  public TreePath getCurrentPath() {
    return currentPath;
  }

  public TreePath getLastPath() {
    return lastPath;
  }

}
