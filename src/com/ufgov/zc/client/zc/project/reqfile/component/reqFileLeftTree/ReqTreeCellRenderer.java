package com.ufgov.zc.client.zc.project.reqfile.component.reqFileLeftTree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.ufgov.zc.client.zc.ztb.util.GV;

public class ReqTreeCellRenderer extends DefaultTreeCellRenderer {

  private static final long serialVersionUID = -1608098219858173735L;

  protected JCheckBox check;

  protected TreeLabel label;

  protected String nodeType;

  public ReqTreeCellRenderer() {
    super();
    setLayout(null);
    add(label = new TreeLabel());
  }

  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row,
    boolean hasFocus) {
    String stringValue = tree.convertValueToText(value, isSelected, expanded, leaf, row, hasFocus);
    ReqTreeNode treeNode = (ReqTreeNode) value;
    setEnabled(tree.isEnabled());
    tree.setRowHeight(19);
    label.setFont(new Font("", 0, 13));
    label.setText(stringValue);
    label.setSelected(isSelected);
    label.setFocus(hasFocus);
    if (leaf) {
      this.nodeType = treeNode.getNodeType();
      if (GV.NODE_TYPE_DOC.equals(this.nodeType)) {
        label.setIcon(treeNode.isLocked() ? GV.getImageIcon().get("wordlock") : GV.getImageIcon().get("word"));
        return this;
      } else if (GV.NODE_TYPE_TABLE.equals(this.nodeType)) {
        label.setIcon(treeNode.isLocked() ? GV.getImageIcon().get("tablelock") : GV.getImageIcon().get("table"));
        return this;
      } else if (GV.NODE_TYPE_TBYLB.equals(this.nodeType)) {
        label.setIcon(treeNode.isLocked() ? GV.getImageIcon().get("tablelock") : GV.getImageIcon().get("table"));
        return this;
      } else if (GV.NODE_TYPE_ECBJB.equals(this.nodeType)) {
        label.setIcon(treeNode.isLocked() ? GV.getImageIcon().get("tablelock") : GV.getImageIcon().get("table"));
        return this;
      } else if (GV.NODE_TYPE_RESPONSE_POINT.equals(this.nodeType)) {
        label.setIcon(GV.getImageIcon().get("list"));
        return this;
      }
    } else {
      if (expanded) {
        label.setIcon(UIManager.getIcon("Tree.openIcon"));
      } else {
        label.setIcon(UIManager.getIcon("Tree.closedIcon"));
      }
    }
    return this;
  }

  public Dimension getPreferredSize() {
    Dimension d_label = label.getPreferredSize();
    return new Dimension(d_label.width, d_label.height);
  }

  public void doLayout() {
    Dimension d_label = label.getPreferredSize();
    label.setLocation(0, 0);
    label.setBounds(0, 0, d_label.width * 2, d_label.height);
  }

  public void setBackground(Color color) {
    if (color instanceof ColorUIResource)
      color = null;
    super.setBackground(color);
  }

  class TreeLabel extends JLabel {
    private static final long serialVersionUID = -4749781856388236035L;

    boolean isSelected;

    boolean hasFocus;

    TreeLabel() {
    }

    public void setBackground(Color color) {
      if (color instanceof ColorUIResource)
        color = null;
      super.setBackground(color);
    }

    public void paint(Graphics g) {
      String str;
      if ((str = getText()) != null) {
        if (0 < str.length()) {
          if (isSelected) {
            g.setColor(new Color(200, 200, 200));
          } else {
            g.setColor(UIManager.getColor("Tree.textBackground"));
          }
          Dimension d = getPreferredSize();
          int imageOffset = 0;
          Icon currentI = getIcon();
          if (currentI != null) {
            imageOffset = currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
          }
          //-300后恰好，原来减1不行
          g.fillRect(imageOffset, 0, d.width - 300 - imageOffset, d.height);
          if (hasFocus) {
            g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
            g.drawRect(imageOffset, 0, d.width - 300 - imageOffset, d.height - 1);
          }
        }
      }
      super.paint(g);
    }

    public Dimension getPreferredSize() {
      Dimension retDimension = super.getPreferredSize();
      if (retDimension != null) {
        retDimension = new Dimension(retDimension.width + 300, retDimension.height);
      }
      return retDimension;
    }

    void setSelected(boolean isSelected) {
      this.isSelected = isSelected;
    }

    void setFocus(boolean hasFocus) {
      this.hasFocus = hasFocus;
    }
  }

}
