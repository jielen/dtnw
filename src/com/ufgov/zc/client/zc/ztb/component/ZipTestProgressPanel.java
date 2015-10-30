package com.ufgov.zc.client.zc.ztb.component;

import com.ufgov.zc.client.zc.ztb.util.GV;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ZipTestProgressPanel {
  private String lastText = "";

  private StringBuffer progressText = new StringBuffer();

  private boolean isZipFileChecking = true;

  private JTextArea textArea = new JTextArea();

  private JScrollPane jScrollPane = null;

  private  JFrame window = null;

  private boolean isCheckPass;

  public ZipTestProgressPanel() {
    progressText.setLength(0);
    textArea.setText(null);
  }

  /**
   * 将过程信息写到滚动窗口中；
   */
  private void publishProgressText() {
    textArea.setText(progressText.toString());
    JScrollBar bar = jScrollPane.getVerticalScrollBar();
    bar.setValue(bar.getMaximum());
    bar.setAutoscrolls(true);
    Point p = new Point();
    p.setLocation(0, this.textArea.getLineCount() * 20);
    this.jScrollPane.getViewport().setViewPosition(p);
  }

  /**
   * 添加进度信息
   *
   * @param text
   */
  public void setProgressText(String text) {
    //因为线程是定时发送消息的，所以有可能发送的是上一次发送过的消息，这里进行过滤
    if (!lastText.equals(text)) {
      progressText.append(text);
      progressText.append("\r\n");
      lastText = text;
    }
    publishProgressText();
  }

  /**
   * 构建一个新的JTextArea
   *
   * @param jTextArea
   */
  private JScrollPane makeTextAreaScrollPanel() {
    textArea = null;
    textArea = new JTextArea();
    textArea.setLayout(null);
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    jScrollPane = new JScrollPane(textArea);
    jScrollPane.setAutoscrolls(true);
    jScrollPane.setWheelScrollingEnabled(true);
    jScrollPane.setDoubleBuffered(true);
    jScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
    return jScrollPane;
  }

  /**
   * 添加一个退出按钮
   *
   * @return
   */
  private JPanel makeButtonsPanel() {
    JPanel panel = new JPanel(new FlowLayout());
    JButton okBt = new JButton(GV.getTranslate("exit"));
    okBt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        askGoonToExit();
      }
    });
    panel.add(okBt);
    return panel;
  }

  /**
   * 退出之前先检查是否线程还在继续
   */
  protected void askGoonToExit() {
    Toolkit.getDefaultToolkit().beep();
    if (this.isZipFileChecking) {
      JOptionPane.showMessageDialog(null, GV.getSimpleMsg("operatorInProgress"));
    } else {
      window.dispose();
    }
  }

  /**
   * 创建一个可滚动窗口
   */
  public void createProgressInfoWindow() {
    window = new JFrame();
    window.setTitle(GV.getSimpleMsg("fileChecking"));
    window.setSize(new Dimension(700, 450));
    window.setLayout(new BorderLayout());
    window.setIconImage(window.getToolkit().getImage(GV.getImageUrl("windowicon.jpg")));
    window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    window.setLocationRelativeTo(null);
    window.add(makeTextAreaScrollPanel(), BorderLayout.CENTER);
    window.add(makeButtonsPanel(), BorderLayout.SOUTH);
    window.toFront();
    window.setVisible(true);
    window.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        askGoonToExit();
      }
    });
  }

  public boolean isZipFileChecking() {
    return isZipFileChecking;
  }

  public void setZipFileChecking(boolean isZipFileChecking) {
    this.isZipFileChecking = isZipFileChecking;
  }

  public JScrollPane getjScrollPane() {
    return jScrollPane;
  }

  public JFrame getWindow() {
    return window;
  }

  public boolean isCheckPass() {
    return isCheckPass;
  }

  public void setCheckPass(boolean isCheckPass) {
    this.isCheckPass = isCheckPass;
  }

  public static void main(String[] args) {
    ZipTestProgressPanel ztp = new ZipTestProgressPanel();
    ztp.createProgressInfoWindow();
  }
}
