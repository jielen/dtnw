package com.ufgov.zc.client.zc.ztb;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ufgov.zc.client.zc.ztb.component.MainPanel;
import com.ufgov.zc.client.zc.ztb.component.TTBPanel;
import com.ufgov.zc.client.zc.ztb.util.GV;

public class TTBFrame extends JFrame {
  private static final long serialVersionUID = -6091418880830023739L;

  private static JFrame frame = null;

  private static String dynamicTitle = null;

  public static void setDynamicTitle(String title) {
    dynamicTitle = GV.MAIN_TBFRAME_TITLE + "  " + title;
    if (frame != null) {
      frame.setTitle(dynamicTitle);
    }
  }

  public static void main(String[] args) throws Exception {
    System.setProperty("sun.java2d.ddscale", "true");
    UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
    JFrame.setDefaultLookAndFeelDecorated(true);
    JDialog.setDefaultLookAndFeelDecorated(true);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        frame = new TTBFrame();
        GV.setCurrFrame(frame);
        GV.initGV();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(GV.MAIN_TTBFRAME_TITLE);
        frame.setIconImage(frame.getToolkit().getImage(GV.getImageUrl("windowicon.jpg")));
        try {
          final MainPanel mainPanel = new TTBPanel(frame);
          frame.add(mainPanel, BorderLayout.CENTER);
          frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            }
          });
        } catch (Exception e1) {
          GV.showMessageDialog(null, "初始化应用程序出错。");
          e1.printStackTrace();
        }
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
      }
    });
  }

  public static JFrame getFrame() {
    return frame;
  }

  public static void setFrame(JFrame frame) {
    TTBFrame.frame = frame;
  }
}
