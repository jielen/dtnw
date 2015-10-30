package com.ufgov.zc.client.zc.ztb.component;

import com.ufgov.zc.client.zc.ztb.model.DetailsType;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;
import com.ufgov.zc.client.zc.ztb.service.PackDetailsService;
import com.ufgov.zc.client.zc.ztb.util.FileChooseFilter;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.client.zc.ztb.util.PubFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Vector;

public class AddChildNodePanel extends JPanel {
  private static final long serialVersionUID = 6959989095016595912L;

  private JTextField detailsName;

  private JTextField importFilePath;

  private JComboBox detailType;

  private MainPanel mainPanel;

  private PackDetailsService packDetailsService;

  private AddChildNodePanel self = this;

  private SmartTreeNode currentTreeNode = null;

  private boolean isForImport = false;

  private Vector<DetailsType> itemVector = new Vector<DetailsType>();

  public AddChildNodePanel(SmartTreeNode currNode, MainPanel mainPanel) {
    this.mainPanel = mainPanel;
    this.currentTreeNode = currNode;
    this.packDetailsService = new PackDetailsService();
    this.itemVector = GV.getAddNodeTypeList(this.currentTreeNode.getNodeType());
    initPanel();
  }

  public AddChildNodePanel(SmartTreeNode currNode, MainPanel mainPanel, boolean isForImport) {
    this.isForImport = isForImport;
    this.mainPanel = mainPanel;
    this.currentTreeNode = currNode;
    this.packDetailsService = new PackDetailsService();
    this.itemVector = GV.getAddNodeTypeList(this.currentTreeNode.getNodeType());
    initPanel();
  }

  public AddChildNodePanel(SmartTreeNode currNode, MainPanel mainPanel, Vector<DetailsType> itemVector) {
    this.mainPanel = mainPanel;
    this.currentTreeNode = currNode;
    this.packDetailsService = new PackDetailsService();
    this.itemVector = itemVector;
    initPanel();
  }

  private void initPanel() {
    this.setLayout(new BorderLayout());
    JPanel panel = new JPanel(new GridLayout(2, 1));
    JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel l1 = new JLabel(GV.getSimpleMsg("type"));
    l1.setPreferredSize(new Dimension(100, 30));
    l1.setHorizontalAlignment(SwingUtilities.RIGHT);
    p1.add(l1);
    detailType = new JComboBox(this.itemVector);
    detailType.setPreferredSize(new Dimension(200, 20));
    addDetailTypeAction();
    p1.add(detailType);
    panel.add(p1);
    JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel l2 = new JLabel(GV.getSimpleMsg("name"));
    l2.setPreferredSize(new Dimension(100, 30));
    l2.setHorizontalAlignment(SwingUtilities.RIGHT);
    p2.add(l2);
    detailsName = new JTextField();
    detailsName.setPreferredSize(new Dimension(200, 20));
    DetailsType dtType = (DetailsType) detailType.getItemAt(0);
    if (dtType.getValue().trim().equals(GV.NODE_TYPE_TBYLB) || dtType.getValue().trim().equals(GV.NODE_TYPE_TB)
      || dtType.getValue().trim().equals(GV.NODE_TYPE_ZB)) {
      detailsName.setText(dtType.getName());
      detailsName.setEditable(false);
    } else if (dtType.getValue().trim().equals(GV.NODE_TYPE_ECBJB)) {
      int count = getEcbjCount(currentTreeNode);
      detailsName.setText(dtType.getName() + count);
    }
    p2.add(detailsName);
    importFilePath = new JTextField();
    if (this.isForImport) {
      detailsName.setEditable(false);
      importFilePath.setPreferredSize(new Dimension(200, 20));
      JButton browerBt = new JButton(GV.getTranslate("brower"));
      p2.add(browerBt);
      browerBt.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setDoubleBuffered(true);
          fileChooser.setAcceptAllFileFilterUsed(false);
          fileChooser.setFileFilter(new FileChooseFilter("doc"));
          fileChooser.setCurrentDirectory(new File(GV.getHistoryDirectory()));
          int result = fileChooser.showOpenDialog(null);
          if (result == JFileChooser.APPROVE_OPTION) {
            importFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
            detailsName.setText(fileChooser.getSelectedFile().getName());
            GV.setHistoryDirectory(fileChooser.getSelectedFile().getParent());
          } else {
            return;
          }
        }
      });
    }
    panel.add(p2);
    this.add(panel, BorderLayout.CENTER);
    ZTBButton confirm = new ZTBButton("confirm");
    this.addActionListener(confirm);
    JPanel btPanel = new JPanel();
    btPanel.setLayout(new FlowLayout());
    btPanel.add(confirm);
    ZTBButton cancel = new ZTBButton("cancel");
    this.addActionCancel(cancel);
    btPanel.add(cancel);
    this.add(btPanel, BorderLayout.SOUTH);
  }

  private void addDetailTypeAction() {
    detailType.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        DetailsType dtType = (DetailsType) detailType.getSelectedItem();
        if (dtType.getValue().trim().equals(GV.NODE_TYPE_TBYLB) || dtType.getValue().trim().equals(GV.NODE_TYPE_TB)
          || dtType.getValue().trim().equals(GV.NODE_TYPE_ZB)) {
          detailsName.setText(dtType.getName());
          detailsName.setEditable(false);
        } else if (dtType.getValue().trim().equals(GV.NODE_TYPE_ECBJB)) {
          int count = getEcbjCount(currentTreeNode);
          detailsName.setText(dtType.getName() + count);
        } else {
          detailsName.setText("");
          detailsName.setEditable(true);
        }
      }
    });
  }

  /**
   * 获得当前有再次报价表的个数
   *
   * @return
   */
  private int getEcbjCount(SmartTreeNode packNode) {
    int count = 1;
    for (int i = 0; i < packNode.getChildCount(); i++) {
      SmartTreeNode curr = (SmartTreeNode) packNode.getChildAt(i);
      if (GV.NODE_TYPE_ECBJB.equals(curr.getNodeType())) {
        count++;
      }
    }
    return count;
  }

  private void addActionListener(ZTBButton confirm) {
    confirm.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SmartTreeNode currentNode = mainPanel.getSingleSeletionTree().getCurrentNode();
        String dtType = ((DetailsType) detailType.getSelectedItem()).getValue().trim();
        boolean flag;
        String detailName = detailsName.getText().trim();
        if (detailName.equals("")) {
          String tigMeg = GV.getOperateMsg("addNewNode.nofilename", detailName);
          GV.showMessageDialog(mainPanel.getRootPane(), tigMeg);
          return;
        }
        flag = PubFunction.isValidFileName(detailName);
        if (!flag) {
          String tigMeg = GV.getOperateMsg("invalidFileName", null);
          GV.showMessageDialog(mainPanel.getRootPane(), tigMeg);
          return;
        }
        flag = packDetailsService.existDupChildNode(currentNode, detailName);
        if (flag) {
          String tigMeg = GV.getOperateMsg("addNewNode.duplicate", detailName);
          GV.showMessageDialog(mainPanel.getRootPane(), tigMeg);
          return;
        }
        if (!dtType.equals(GV.NODE_TYPE_TBYLB) && (GV.NODE_NAME_TBYLB.equals(detailName) || GV.NODE_NAME_BJYLB.equals(detailName))) {
          GV.showMessageDialog(mainPanel.getRootPane(), GV.getOperateMsg("nodeNameErr", detailName));
          return;
        }
        SmartTreeNode treeNode = null;
        try {
          treeNode = packDetailsService.createChildNode(currentNode, dtType, detailName, importFilePath.getText());
        } catch (Exception e1) {
          GV.showMessageDialog(mainPanel.getRootPane(), e1.getMessage());
          e1.printStackTrace();
          return;
        }
        flag = (treeNode == null) ? false : true;
        if (!flag) {
          String tigMeg = GV.getOperateMsg("addNewNode.failure", detailName);
          GV.showMessageDialog(mainPanel.getRootPane(), tigMeg);
          return;
        }
        String tigMeg = GV.getOperateMsg("addNewNode.success", detailName);
        GV.showMessageDialog(mainPanel.getRootPane(), tigMeg);
        ((JDialog) self.getRootPane().getParent()).dispose();
        try {
          mainPanel.refreshLeftFilesTreePanel();
        } catch (Exception ee) {
        }
        mainPanel.getSingleSeletionTree().setCurrentNode(treeNode);
      }
    });
  }

  private void addActionCancel(final ZTBButton cancel) {
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        ((JDialog) self.getRootPane().getParent()).dispose();
      }
    });
  }
}
