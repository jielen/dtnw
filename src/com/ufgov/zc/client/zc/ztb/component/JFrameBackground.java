package com.ufgov.zc.client.zc.ztb.component;

import com.ufgov.zc.client.zc.ztb.util.GV;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class JFrameBackground {
  private JFrame frame = new JFrame("starting");

  private static JFrameBackground instance;

  private JPanel imagePanel;

  private ImageIcon background;

  public static JFrameBackground getInstance() {
    if (instance == null) {
      try {
        instance = new JFrameBackground();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    return instance;
  }

  public void destroyInstance() {
    frame.dispose();
  }

  public static void main(String[] args) throws InterruptedException, InvocationTargetException {
    new JFrameBackground();
  }

  private JFrameBackground() throws InterruptedException, InvocationTargetException {
    SwingUtilities.invokeAndWait(new Runnable() {
      public void run() {
        background = new ImageIcon(GV.getImageUrl("starting.gif"));// 背景图片
        JLabel label = new JLabel(background);// 把背景图片显示在一个标签里面
        // 把标签的大小位置设置为图片刚好填充整个面板
        label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
        // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
        imagePanel = (JPanel) frame.getContentPane();
        imagePanel.setOpaque(false);
        imagePanel.setLayout(new BorderLayout());
        imagePanel.add(new JLabel(GV.getSimpleMsg("startingLabelText")), BorderLayout.CENTER);
        // 内容窗格默认的布局管理器为BorderLayout
        frame.getLayeredPane().setLayout(null);
        frame.setUndecorated(true);
        // 把背景图片添加到分层窗格的最底层作为背景
        frame.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(background.getIconWidth(), background.getIconHeight());
        frame.setLocationRelativeTo(null);
        //AWTUtilities.setWindowOpacity(frame, 0.8f);
        frame.setVisible(true);
      }
    });
  }
}
