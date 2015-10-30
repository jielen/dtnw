package com.ufgov.zc.client.zc.ztb;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ufgov.zc.client.zc.ztb.component.JFrameBackground;
import com.ufgov.zc.client.zc.ztb.component.MainPanel;
import com.ufgov.zc.client.zc.ztb.component.TBPanel;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;
import com.ufgov.zc.client.zc.ztb.util.GV;

public class TBFrame extends JFrame {
  private static final long serialVersionUID = 3798074606224180234L;

  private static TBFrame frame = null;

  private static String dynamicTitle = null;

  private static JFrameBackground startingFrame = null;

  public static void setDynamicTitle(String title) {
    dynamicTitle = GV.MAIN_TBFRAME_TITLE + "  " + title;
    if (frame != null) {
      frame.setTitle(dynamicTitle);
    }
  }

  public static void showupStartingFrame() {
    startingFrame = JFrameBackground.getInstance();
  }

  public static void closeStartingFrame() {
    JFrameBackground.getInstance().destroyInstance();
  }

  public static void main(String[] args) throws Exception {
    showupStartingFrame();
    System.setProperty("sun.java2d.ddscale", "true");
    UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
    JFrame.setDefaultLookAndFeelDecorated(true);
    JDialog.setDefaultLookAndFeelDecorated(true);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        frame = new TBFrame();
        GV.setCurrFrame(frame);
        GV.initGV();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(GV.MAIN_TBFRAME_TITLE);
        frame.setIconImage(frame.getToolkit().getImage(GV.getImageUrl("windowicon.jpg")));
        try {
          final MainPanel mainPanel = new TBPanel(frame);
          frame.add(mainPanel, BorderLayout.CENTER);
          //frame.setJMenuBar(new MainMenuBar(mainPanel));
          frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
              if (mainPanel.getOpenedLeavesNode() != null) {
                SmartTreeNode treeNode = mainPanel.getOpenedLeavesNode();
                if (GV.NODE_TYPE_DOC.equals(treeNode.getNodeType())) {
                  mainPanel.closeWordPane();
                }
              }
              JobThreads.toDestroyStartedProcess();
            }
          });
        } catch (Exception e1) {
          GV.showMessageDialog(null, "初始化应用程序出错。");
          e1.printStackTrace();
        }
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        closeStartingFrame();
      }
    });
  }

  public static TBFrame getFrame() {
    return frame;
  }

  public static void setFrame(TBFrame frame) {
    TBFrame.frame = frame;
  }
}
