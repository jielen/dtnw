package com.ufgov.zc.client.zc.ztb.activex;import com.ufgov.zc.client.zc.ztb.util.GV;import javax.swing.*;import javax.swing.filechooser.FileFilter;import java.awt.*;import java.awt.event.ActionEvent;import java.beans.PropertyChangeEvent;import java.beans.PropertyChangeListener;import java.io.File;public class Main {  public static void main(String[] args) {    SwingUtilities.invokeLater(new Runnable() {      @Override      public void run() {        try {          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());        } catch (Exception e) {          e.printStackTrace();        }        JFrame frame = new JFrame("frame");        frame.setIconImage(frame.getToolkit().getImage(GV.getImageUrl("windowicon.jpg")));        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        frame.setSize(640, 480);        frame.setLocationRelativeTo(null);        final WordPane pane = new WordPane() {          @Override          public void addNotify() {            super.addNotify();          }        };        final Container contentPane = frame.getContentPane();        JButton saveBtn = new JButton(new AbstractAction("save") {          @Override          public void actionPerformed(ActionEvent e) {            //pane.save("c:\\new_file.doc");          }        });        JButton openBtn = new JButton(new AbstractAction("open") {          @Override          public void actionPerformed(ActionEvent e) {            JFileChooser c = new JFileChooser("C:/");            c.setFileFilter(new FileFilter() {              @Override              public boolean accept(File f) {                return f.getAbsolutePath().toLowerCase().endsWith(".doc");              }              @Override              public String getDescription() {                return "";              }            });            if (c.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {              //contentPane              pane.open(c.getSelectedFile().getPath());            }          }        });        Action replaceFileBookMarkAction = new AbstractAction("替换文件书签") {          @Override          public void actionPerformed(ActionEvent e) {            //WordPane.replaceFileBookMarks("C:\\template.doc", new String[][] { { "name", "liuwei" }, { "addr", "北京市朝阳区三元桥源东里东" } }, "C:\\temp.doc");          }        };        Action replaceBookMarkAction = new AbstractAction("替换打开文件书签") {          @Override          public void actionPerformed(ActionEvent e) {            pane.replaceBookMarks("a$$$$$liuwei@@@@@b$$$$$北京市朝阳区三元桥源东里东");          }        };        Action protectAction = new AbstractAction("保护文档") {          @Override          public void actionPerformed(ActionEvent e) {            String password = JOptionPane.showInputDialog("请输入保护密码:");            pane.protectDoc(password);          }        };        Action unProtectAction = new AbstractAction("解除保护文档") {          @Override          public void actionPerformed(ActionEvent e) {            String password = JOptionPane.showInputDialog("请输入解除保护密码:");            pane.unProtectDoc(password);          }        };        Action openAndProtectAction = new AbstractAction("打开并保护文档") {          @Override          public void actionPerformed(ActionEvent e) {            JFileChooser c = new JFileChooser("C:/");            c.setFileFilter(new FileFilter() {              @Override              public boolean accept(File f) {                return f.getAbsolutePath().toLowerCase().endsWith(".doc");              }              @Override              public String getDescription() {                return "";              }            });            String password = JOptionPane.showInputDialog("请输入保护密码:");            if (c.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {              pane.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {                public void propertyChange(PropertyChangeEvent evt) {                  //打开文件完成之后的回调函数                  boolean isSuccess = (Boolean) evt.getNewValue();                  if (isSuccess) {                  }                }              });              pane.openAndProtect(c.getSelectedFile().getPath(), password);            }          }        };        Action closeAction = new AbstractAction("关闭文档") {          @Override          public void actionPerformed(ActionEvent e) {            //contentPane.remove(pane);            //contentPane.invalidate();            pane.close();          }        };        JToolBar bar = new JToolBar();        bar.add(openBtn);        bar.add(saveBtn);        bar.addSeparator();        bar.add(replaceBookMarkAction);        bar.add(replaceFileBookMarkAction);        bar.add(protectAction);        bar.add(openAndProtectAction);        bar.add(unProtectAction);        bar.add(closeAction);        contentPane.add(bar, BorderLayout.NORTH);        contentPane.add(pane);        frame.setVisible(true);        //        Runtime.getRuntime().addShutdownHook(new Thread() {        //          public void run() {        //            if (pane != null) {        //              try {        //                pane.close();        //              } catch (Exception e) {        //                // TODO: handle exception        //              }        //            }        //          }        //        });      }    });  }}