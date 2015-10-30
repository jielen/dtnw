package com.ufgov.zc.client.zc.project.integration.zbbook;import com.ufgov.smartclient.plaf.BlueLookAndFeel;import com.ufgov.zc.client.zc.ztb.component.MainPanel;import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;import com.ufgov.zc.client.zc.ztb.util.GV;import javax.swing.*;import java.awt.*;import java.awt.event.WindowAdapter;import java.awt.event.WindowEvent;public class ZBFrame extends JFrame {  private static final long serialVersionUID = 1999446451948264675L;  private static JFrame frame = null;  private static String dynamicTitle = null;  public static void setDynamicTitle(String title) {    dynamicTitle = GV.MAIN_ZBFRAME_TITLE + "  " + title;    if (frame != null) {      frame.setTitle(dynamicTitle);    }  }  public static void main(String[] args) {    SwingUtilities.invokeLater(new Runnable() {      public void run() {        try {          UIManager.setLookAndFeel(new BlueLookAndFeel());          JFrame.setDefaultLookAndFeelDecorated(true);        } catch (Exception e) {          e.printStackTrace();        }        frame = new ZBFrame();        GV.setCurrFrame(frame);        GV.initGV();        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        frame.setTitle(GV.MAIN_ZBFRAME_TITLE);        frame.setIconImage(frame.getToolkit().getImage(GV.getImageUrl("windowicon.jpg")));        try {          final MainPanel mainPanel = new ZBPanel(frame,null);          frame.add(mainPanel, BorderLayout.CENTER);          //frame.setJMenuBar(new MainMenuBar(mainPanel));          frame.addWindowListener(new WindowAdapter() {            public void windowClosing(WindowEvent e) {              SmartTreeNode leavesNode = mainPanel.getOpenedLeavesNode();              if (leavesNode != null) {                if (GV.NODE_TYPE_DOC.equals(leavesNode.getNodeType())) {                  mainPanel.closeWordPane();                }              }            }          });        } catch (Exception e1) {          e1.printStackTrace();          GV.showMessageDialog(null, "初始化应用程序出错。");        }        frame.pack();        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);        frame.setVisible(true);      }    });  }  public static JFrame getFrame() {    return frame;  }  public static void setFrame(JFrame frame) {    ZBFrame.frame = frame;  }}