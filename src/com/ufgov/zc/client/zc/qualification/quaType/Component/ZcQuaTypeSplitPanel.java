package com.ufgov.zc.client.zc.qualification.quaType.Component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.JFuncPopupMenu;
import com.ufgov.zc.client.component.Menu.AddMenuItem;
import com.ufgov.zc.client.component.Menu.DeleteMenuItem;
import com.ufgov.zc.client.component.zc.tree.EventPropertyName;
import com.ufgov.zc.client.component.zc.tree.TreeNodeSelectionListener;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.zc.model.ZcSupQuaType;

public class ZcQuaTypeSplitPanel extends JSplitPane {

  public ZcQuaTypeTreePanel treePanel;

  public ZcQuaTypeEditPanel editPanel;

  private QuaTypeTreeNode selectedNode;

  private ZcQuaTypeSplitPanel self = this;

  private JPanel nullPanel = new JPanel();

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_SUP_QUA_TYPE";

  AddMenuItem addMenuItem = new AddMenuItem();

  DeleteMenuItem deleteMenuItem = new DeleteMenuItem();

  /**
   * @return the selectedNode
   */
  public QuaTypeTreeNode getSelectedNode() {
    return selectedNode;
  }

  private TreePath selectedTreePath;

  public ZcQuaTypeSplitPanel() {
    initComponet();
  }

  public void initComponet() {
    editPanel = new ZcQuaTypeEditPanel(this);
    treePanel = new ZcQuaTypeTreePanel();
    this.setMinimumSize(new Dimension(0, 0));
    this.setDividerSize(2);
    int width = (int) ((Toolkit.getDefaultToolkit().getScreenSize().width - this.getDividerSize()) * 0.27);
    this.setDividerLocation(width);
    this.setMinimumSize(new Dimension(0, 0));
    this.setLeftComponent(treePanel);
    this.setRightComponent(nullPanel);
    initMenuItem();
    addTreeListener();

  }

  public void initMenuItem() {

    addMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZcSupQuaType bean = new ZcSupQuaType();
        bean.setParentTypeCode(selectedNode.getCode());
        bean.setParentTypeName(selectedNode.getName());
        bean.setCreateTime(new Date());
        bean.setExecutor(requestMeta.getSvUserID());
        editPanel.isInsert = true;
        editPanel.refreshData(bean);
      }
    });

    deleteMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (editPanel.doDelete()) {
          DefaultTreeModel model = (DefaultTreeModel) treePanel.selectTree.getModel();
          model.removeNodeFromParent(selectedNode);
          //父节点的编辑对象，移除该对象。
          self.setRightComponent(nullPanel);
          self.setDividerLocation(self.getDividerLocation());
          repaintTree();
        }

      }
    });

  }

  public void addTreeListener() {

    NodeSelectionListener lisener = new NodeSelectionListener(treePanel.getSelectTree());

    lisener.setNeedDoRightMouseClick(true);

    treePanel.getSelectTree().addMouseListener(lisener);

    addKeyListener();

  }

  private void doNodeSelected(QuaTypeTreeNode node) {
    if (node.getCode() != null && "root".endsWith(node.getCode())) {
      return;
    }
    editPanel.isInsert = false;
    this.setRightComponent(editPanel);
    this.setDividerLocation(this.getDividerLocation());
    editPanel.refreshData(node.getUserObject());
    editPanel.repaint();
  }

  private void addKeyListener() {
    treePanel.getSelectTree().addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
          TreePath path = treePanel.getSelectTree().getSelectionPath();
          QuaTypeTreeNode node = (QuaTypeTreeNode) path.getLastPathComponent();
          doNodeSelected(node);
        }
      }
    });

  }

  public void setSelectedNode(QuaTypeTreeNode selectedNode) {

    this.selectedNode = selectedNode;

  }

  private void setSelectedNode(MouseEvent e) {

    int x = e.getX();

    int y = e.getY();

    int row = treePanel.getSelectTree().getRowForLocation(x, y);

    selectedTreePath = treePanel.getSelectTree().getPathForRow(row);

    if (null != selectedTreePath) {

      selectedNode = (QuaTypeTreeNode) selectedTreePath.getLastPathComponent();

    }

    doNodeSelected(selectedNode);

  }

  private class NodeSelectionListener extends TreeNodeSelectionListener {

    public NodeSelectionListener(JTree tree) {
      super(tree);
    }

    /**

     * 如果是根节点则只能修改和新增，如果是叶子节点只能删除和修改

     */

    @Override
    protected void doRightMouseClick(MouseEvent e) {

      JFuncPopupMenu popupMenu = new JFuncPopupMenu("ZC", compoId);

      popupMenu.setForeground(Color.blue);

      if (canAddChild(e)) {

        popupMenu.add(addMenuItem);
      }
      if (isLeaf(e)) {
        popupMenu.add(deleteMenuItem);
      }
      setSelectedNode(e);
      popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private boolean canAddChild(MouseEvent e) {

      int x = e.getX();

      int y = e.getY();

      int row = tree.getRowForLocation(x, y);

      TreePath path = tree.getPathForRow(row);

      if (path != null) {

        QuaTypeTreeNode node = (QuaTypeTreeNode) path.getLastPathComponent();
        return true;
      }

      return false;

    }

    private boolean isLeaf(MouseEvent e) {

      int x = e.getX();

      int y = e.getY();

      int row = tree.getRowForLocation(x, y);

      TreePath path = tree.getPathForRow(row);

      if (path != null) {

        QuaTypeTreeNode node = (QuaTypeTreeNode) path.getLastPathComponent();

        if (null != node && node.isLeaf()) {

          return true;

        }

      }

      return false;

    }

    @Override
    protected void doLeftMouseClick(MouseEvent e) {

      int x = e.getX();

      int y = e.getY();

      int row = tree.getRowForLocation(x, y);

      TreePath path = tree.getPathForRow(row);

      if (path != null) {

        setSelectedNode(e);

      }

      firePropertyChange(EventPropertyName.MOUSECLICK_PROPERTY_NAME, false, true);

    }

  }

  public void repaintTree() {

    treePanel.getSelectTree().revalidate();

    treePanel.getSelectTree().repaint();

  }

  static {
    LangTransMeta.init("ZC%");
  }

  public static void main(String[] args) {

    JFrame f = new JFrame("供应商资质");

    ZcQuaTypeSplitPanel panel = new ZcQuaTypeSplitPanel();

    f.getContentPane().add(panel, BorderLayout.CENTER);

    f.setSize(UIConstants.DIALOG_0_LEVEL_WIDTH, UIConstants.DIALOG_0_LEVEL_HEIGHT);

    f.setLocationRelativeTo(null);

    f.setVisible(true);

    f.addWindowListener(new WindowAdapter() {

      public void windowClosing(WindowEvent e) {

        System.exit(0);

      }

    });

  }
}
