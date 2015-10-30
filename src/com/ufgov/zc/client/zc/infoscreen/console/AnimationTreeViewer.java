package com.ufgov.zc.client.zc.infoscreen.console;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class AnimationTreeViewer extends JPanel {
  public final JTree animaTree;

  public AnimationTreeViewer() {
    super();
    this.setLayout(new BorderLayout());
    animaTree = new JTree();
    initTree(animaTree);
  }

  public void setGeneralAnimationMetas(List metas) {
    DefaultMutableTreeNode root = (DefaultMutableTreeNode) animaTree.getModel().getRoot();
    DefaultMutableTreeNode generalNode = (DefaultMutableTreeNode) root.getChildAt(0);
    for (int i = 0; i < metas.size(); i++) {
      generalNode.add(new DefaultMutableTreeNode(metas.get(i)));
    }
    animaTree.updateUI();
  }

  public void setRealAnimationMetas(List metas) {
    DefaultMutableTreeNode root = (DefaultMutableTreeNode) animaTree.getModel().getRoot();
    DefaultMutableTreeNode generalNode = (DefaultMutableTreeNode) root.getChildAt(1);
    for (int i = 0; i < metas.size(); i++) {
      generalNode.add(new DefaultMutableTreeNode(metas.get(i)));
    }
    animaTree.updateUI();
  }

  public void addTreeListener(MouseListener listener) {
    animaTree.removeMouseListener(listener);
    animaTree.addMouseListener(listener);
  }

  private void initTree(JTree tree) {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("显示模块");
    DefaultMutableTreeNode generalNode = new DefaultMutableTreeNode("常规");
    DefaultMutableTreeNode realNode = new DefaultMutableTreeNode("紧急");
    root.add(generalNode);
    root.add(realNode);
    animaTree.setModel(new DefaultTreeModel(root));
    this.add(animaTree, BorderLayout.CENTER);
  }
}
