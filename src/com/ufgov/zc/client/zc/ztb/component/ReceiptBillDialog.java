package com.ufgov.zc.client.zc.ztb.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;

/**
 * 创建投标回执
 *
 * @param map
 */
public class ReceiptBillDialog extends JFrame implements Printable {
  private static final long serialVersionUID = 3917545644260436757L;

  private ReceiptBillDialog self = this;

  private JPanel basePanel = new JPanel();

  public ReceiptBillDialog(Map<String, String> map) {
    init();
    createPanel(map);
  }

  private void init() {
    this.setTitle(GV.getSimpleMsg("bidReceiptTitle"));
    this.setSize(new Dimension(620, 380));
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
  }

  private void createPanel(Map<String, String> map) {
    int lineNo = 0;
    setLayout(new BorderLayout());
    setBackground(new Color(243, 249, 255));
    basePanel.setLayout(new BorderLayout());
    basePanel.setBackground(new Color(243, 249, 255));
    JLabel emptyLabel = new JLabel("      ");
    JPanel emptyPanel = new JPanel();
    emptyPanel.add(emptyLabel);
    emptyPanel.setBackground(new Color(243, 249, 255));
    basePanel.add(emptyPanel, BorderLayout.NORTH);
    JPanel subPanel = new JPanel();
    subPanel.setBackground(new Color(243, 249, 255));
    subPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), GV.getSimpleMsg("bidReceiptTitle"), TitledBorder.CENTER,
      TitledBorder.TOP, new Font("宋体", Font.BOLD, 20), Color.RED));
    subPanel.setLayout(new GridBagLayout());
    GridBagConstraints gBC = new GridBagConstraints();
    gBC.fill = GridBagConstraints.HORIZONTAL;
    gBC.insets = new Insets(5, 5, 0, 5);
    //回执内容
    gBC.fill = GridBagConstraints.LINE_START;
    final StringBuffer buff = new StringBuffer("<html><b><font size='5'>");
    buff.append(map.get("SUPPLIERNAME"));
    buff.append("：</font></b><br><font size='4' height='24'>&nbsp;&nbsp;&nbsp;&nbsp;");
    buff.append(GV.getSimpleMsg("receiptContent000"));
    buff.append(getColorText(map.get("SIGNUPDATE")));
    buff.append(GV.getSimpleMsg("receiptContent001"));
    buff.append(getColorText(map.get("PROJECTCODE")));
    buff.append(GV.getSimpleMsg("receiptContent002"));
    buff.append(getColorText(map.get("PROJECTNAME") + "项目"));
    buff.append(GV.getSimpleMsg("receiptContent003"));
    buff.append(getColorText(map.get("BIDPACKNAMES")));
    buff.append(GV.getSimpleMsg("receiptContent004"));
    buff.append("<br><br>");
    buff.append(GV.getSimpleMsg("receiptContent005"));
    buff.append(getGoodText(map.get("MANAGERNAME")));
    buff.append("<br>");
    buff.append(GV.getSimpleMsg("receiptContent006"));
    buff.append(getGoodText(map.get("MANAGERPHONE")));
    buff.append("<br>");
    buff.append(GV.getSimpleMsg("receiptContent007"));
    buff.append(getGoodText(map.get("OFFICEADDRESS")));
    buff.append("<br>");
    buff.append(GV.getSimpleMsg("receiptContent008"));
    buff.append(getGoodText(map.get("MD5CODE")));
    buff.append("<br>");
    buff.append(GV.getSimpleMsg("receiptContent009"));
    SimpleDateFormat sdf = new SimpleDateFormat(ZcSettingConstants.SIMPLE_DATE_FORMAT_FULL);
    buff.append(sdf.format(new Date()));
    buff.append("。</font><br></html>");
    JLabel detail = new JLabel(buff.toString().replace("项目项目", "项目"));
    detail.setPreferredSize(new Dimension(500, 230));
    gBC.gridx = 0;
    gBC.gridy = lineNo++;
    gBC.gridwidth = 4;
    subPanel.add(detail, gBC);
    basePanel.add(subPanel, BorderLayout.CENTER);
    add(basePanel, BorderLayout.CENTER);
    System.out.println(buff.toString().replace("项目项目", "项目"));
    JPanel basePanel2 = new JPanel();
    basePanel2.setBackground(new Color(243, 249, 255));
    JPanel btPanel = new JPanel();
    btPanel.setLayout(new FlowLayout());
    btPanel.setBackground(new Color(243, 249, 255));
    JButton close = new JButton(GV.getTranslate("exit"));
    close.setBackground(new Color(243, 249, 255));
    btPanel.add(close);
    close.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        self.setVisible(false);
      }
    });
    JButton print = new JButton(GV.getTranslate("printReceipt"));
    print.setBackground(new Color(243, 249, 255));
    btPanel.add(print);
    addActionResponse(print);
    basePanel2.add(btPanel);
    gBC.gridx = 3;
    gBC.gridy = lineNo;
    gBC.gridwidth = 1;
    add(basePanel2, BorderLayout.SOUTH);
  }

  private String getGoodText(String info) {
    if (info == null || "null".equals(info) || "".equals(info)) {
      return "【不详】；";
    }
    return info + "；";
  }

  private String getColorText(String info) {
    StringBuffer buff = new StringBuffer();
    buff.append("<font color='red'>");
    buff.append(info);
    buff.append("</font>");
    return buff.toString();
  }

  private void addActionResponse(JButton button) {
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        printFrameAction();
      }
    });
  }

  /* 打印指定的窗体及其包含的组件 */
  private void printFrameAction() {
    Toolkit kit = Toolkit.getDefaultToolkit(); // 获取工具箱
    Properties props = new Properties();
    props.put("awt.print.printer", "durango");// 设置打印属性
    props.put("awt.print.numCopies", "2");
    if (kit != null) {
      // 获取工具箱自带的打印对象
      PrintJob printJob = kit.getPrintJob(this, "Print Frame", props);
      if (printJob != null) {
        Graphics pg = printJob.getGraphics();// 获取打印对象的图形环境
        if (pg != null) {
          try {
            basePanel.print(pg);// 打印该窗体及其所有的组件
          } finally {
            pg.dispose();// 注销图形环境
          }
        }
        printJob.end();// 结束打印作业
      }
    }
  }

  /**
   * @param args
   * @throws UnsupportedLookAndFeelException
   *
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws ClassNotFoundException
   */
  public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
    UnsupportedLookAndFeelException {
    System.setProperty("sun.java2d.ddscale", "true");
    UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
    JFrame.setDefaultLookAndFeelDecorated(true);
    JDialog.setDefaultLookAndFeelDecorated(true);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Map<String, String> data = new HashMap<String, String>();
        loadData(data);
        ReceiptBillDialog rbd = new ReceiptBillDialog(data);
        rbd.toFront();
        rbd.setAlwaysOnTop(true);
        rbd.setVisible(true);
      }
    });
  }

  private static void loadData(Map<String, String> data) {
    data.put("SUPPLIERNAME", "中国平安保险有限公司");
    data.put("SUPPLIERCODE", "123456");
    data.put("MANAGERNAME", "负责人C");
    data.put("MANAGERPHONE", "13878789888");
    data.put("SIGNUPDATE", "2011-01-20 13:44:09");
    data.put("PROJECTNAME", "汽车采购");
    data.put("PROJECTCODE", "XCZX2011-2011");
    data.put("BIDPACKNAMES", "标段一");
    data.put("OFFICEADDRESS", "陕西省西安市四府街43号6层603");
    data.put("MD5CODE", "d403ddf0ee7671e95e33087e770de0a9");
  }

  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
    return 0;
  }
}
