package com.ufgov.zc.client.zc.qualification.quaType.Component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.zc.qualification.quaType.service.InitQuaTypeTreeService;

public class ZcQuaTypeTreePanel extends JPanel {

  private JScrollPane panel;

  public QuaTypeTreeNode rootNode;

  private JLabel searchLabel = new JLabel("查找：");

  protected JTextField searchField = new JTextField(20);

  protected JButton searchButton = new JButton("查找");

  public JTree selectTree;

  /**
   * @return the selectTree
   */
  public JTree getSelectTree() {
    return selectTree;
  }

  /**
   * @param selectTree the selectTree to set
   */
  public void setSelectTree(JTree selectTree) {
    this.selectTree = selectTree;
  }

  protected JPanel searchBar = new JPanel() {

    {

      this.setLayout(new FlowLayout(FlowLayout.LEFT));

    }

  };

  public ZcQuaTypeTreePanel() {

    init();
  }

  private void init() {
    InitQuaTypeTreeService initQuaTypeTreeService = new InitQuaTypeTreeService();

    selectTree = initQuaTypeTreeService.createFilesTreePanel();
    this.setLayout(new BorderLayout());
    panel = new JScrollPane(selectTree);
    initSearchBar();
    this.add(searchBar, BorderLayout.NORTH);
    this.add(panel, BorderLayout.CENTER);

  }

  private void initSearchBar() {

    searchBar.add(searchLabel);

    searchBar.add(searchField);

    searchBar.add(searchButton);

    searchField.addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

          doSearch();

        }

      }

    });

    searchButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        doSearch();

      }

    });

  }

  protected void doSearch() {

    int rowCount = selectTree.getRowCount();

    String searchString = this.searchField.getText().trim();

    if (searchString.equals("") || searchString.equals("[") || searchString.equals("]")) {

      return;

    }

    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectTree.getLastSelectedPathComponent();

    if (node == null || node.getNextNode() == null) {

      node = (DefaultMutableTreeNode) selectTree.getModel().getRoot();

    }

    DefaultMutableTreeNode currentNode = node;

    node = node.getNextNode();

    if (node == null)

      return;

    while (node != currentNode) {

      Object object = node.getUserObject();

      if (object.toString().indexOf(searchString) >= 0) {

        for (int i = rowCount - 1; i > 0; i--) {

          selectTree.collapseRow(i);

        }

        selectTree.setSelectionPath(new TreePath(node.getPath()));

        // /

        int[] selectedRows = selectTree.getSelectionRows();

        if (selectedRows.length > 0) {

          selectTree.scrollRowToVisible(selectedRows[0]);

        }

        return;

      }

      node = node.getNextNode();

      if (node == null)

        node = (DefaultMutableTreeNode) selectTree.getModel().getRoot();

    }

    // 在采购目录中输入关键字，如果没有匹配数据，弹出没有记录提示框—guoss

    if (node.getUserObject().toString().indexOf(searchString) <= 0) {

      JOptionPane.showMessageDialog(this, "没有记录!", "提示", JOptionPane.INFORMATION_MESSAGE);

    }
  }

  public static void main(String[] args) throws Exception {

    SwingUtilities.invokeLater(new Runnable() {

      public void run() {

        try {

          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

          UIManager.setLookAndFeel(new BlueLookAndFeel());

        } catch (Exception e) {

          e.printStackTrace();

        }

        //        UIManager.getDefaults().put("SplitPaneUI", BigButtonSplitPaneUI.class.getName());

        ZcQuaTypeTreePanel bill = new ZcQuaTypeTreePanel();

        JFrame frame = new JFrame("frame");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 600);

        frame.setLocationRelativeTo(null);

        frame.getContentPane().add(bill);

        frame.setVisible(true);

      }

    });

  }
}
