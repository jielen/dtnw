package com.ufgov.zc.client.zc.ztb.component;

import com.ufgov.zc.client.zc.ztb.service.FileTransferService;
import com.ufgov.zc.client.zc.ztb.util.CALogin;
import com.ufgov.zc.client.zc.ztb.util.FileChooseFilter;
import com.ufgov.zc.client.zc.ztb.util.GV;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UkeySelectionPanel extends BaseSelectionPanel {
  private static final long serialVersionUID = -7267204107723386392L;

  private Map paras = new HashMap();

  private JComboBox keyComboBox = new JComboBox();

  private CALogin caLogin = new CALogin();

  public UkeySelectionPanel(JDialog pDialog, JDialog nDialog, Map map) {
    prevDialog = pDialog;
    currDialog = nDialog;
    paras.putAll(map);
    init();
    buildContents();
  }

  private void init() {
    prevDialog.dispose();
    alertInfoLabel = new JLabel(" ");
    alertInfoLabel.setPreferredSize(new Dimension(360, 50));
    alertInfoLabel.setVisible(false);
    alertInfoLabel.setHorizontalAlignment(SwingUtilities.LEFT);
  }

  private void buildContents() {
    this.setLayout(new BorderLayout());
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(5, 5, 0, 5);
    int gridy = 0;
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 1;
    JLabel l1 = new JLabel(GV.getSimpleMsg("priceFileLabel"));
    panel.add(l1, c);
    c.gridx = 1;
    c.gridy = gridy;
    c.gridwidth = 2;
    ztbPathField = new JTextField();
    ztbPathField.setEnabled(false);
    ztbPathField.setText((String) paras.get("filefullpath"));
    ztbPathField.setPreferredSize(new Dimension(240, 22));
    ztbPathField.setToolTipText(ztbPathField.getText());
    panel.add(ztbPathField, c);
    c.gridx = 3;
    c.gridy = gridy;
    c.gridwidth = 1;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    JButton brower = new JButton(GV.getTranslate("brower"));
    brower.setPreferredSize(new Dimension(70, 22));
    this.addBrowerActions(brower);
    panel.add(brower, c);
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 1;
    JLabel l2 = new JLabel(GV.getSimpleMsg("digitalCertificateLabel"));
    panel.add(l2, c);
    c.gridx = 1;
    c.gridy = gridy;
    c.gridwidth = 2;
    keyComboBox.setPreferredSize(new Dimension(300, 22));
    Object selItem = keyComboBox.getSelectedItem();
    if (selItem != null) {
      keyComboBox.setToolTipText(selItem.toString());
    }
    this.addComboBoxSelectChangeActions();
    DefaultComboBoxModel defModel = caLogin.getCAItemsComboBoxModel();
    if (defModel != null) {
      keyComboBox.setModel(defModel);
      this.alertInfoLabel.setVisible(false);
    } else {
      this.alertInfoLabel.setText(this.getAlertInfo(GV.getSimpleMsg("insertCAAndRefresh")));
      this.alertInfoLabel.setVisible(true);
    }
    panel.add(keyComboBox, c);
    c.gridx = 3;
    c.gridy = gridy;
    c.gridwidth = 1;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    JButton refresh = new JButton(GV.getTranslate("refresh"));
    refresh.setPreferredSize(new Dimension(70, 22));
    this.addRefreshActions(refresh);
    panel.add(refresh, c);
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 4;
    JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p1.add(alertInfoLabel);
    panel.add(p1, c);
    JButton button = new JButton(GV.getTranslate("summitPrice"));
    button.setPreferredSize(new Dimension(80, 22));
    addNextButtonActions(button);
    JButton cancel = new JButton(GV.getTranslate("cancel"));
    cancel.setPreferredSize(new Dimension(80, 22));
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        currDialog.setVisible(false);
      }
    });
    JPanel btPanel = new JPanel();
    btPanel.setLayout(new FlowLayout());
    btPanel.add(cancel);
    btPanel.add(button);
    this.add(btPanel, BorderLayout.SOUTH);
    this.add(panel, BorderLayout.CENTER);
  }

  private void addBrowerActions(JButton brower) {
    brower.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileChooseFilter("xml"));
        fileChooser.setCurrentDirectory(new File(GV.getHistoryDirectory()));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
          ztbPathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
          paras.put("FILEFULLPATH", ztbPathField.getText());
          GV.setHistoryDirectory(fileChooser.getSelectedFile().getParent());
          alertInfoLabel.setVisible(false);
        }
      }
    });
  }

  private void addRefreshActions(JButton refresh) {
    refresh.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        DefaultComboBoxModel defModel = caLogin.getCAItemsComboBoxModel();
        if (defModel != null) {
          keyComboBox.setModel(defModel);
          alertInfoLabel.setVisible(false);
        } else {
          alertInfoLabel.setText(getAlertInfo(GV.getSimpleMsg("insertCAAndRefresh")));
          alertInfoLabel.setVisible(true);
        }
      }
    });
  }

  private String getAlertInfo(String msg) {
    StringBuffer alertInfo = new StringBuffer("<html><a><font size='3' color='red'>提示：");
    alertInfo.append(msg);
    alertInfo.append("</font><a></html>");
    return alertInfo.toString();
  }

  private void addComboBoxSelectChangeActions() {
    keyComboBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent arg0) {
        int index = keyComboBox.getSelectedIndex();
        if (index > projList.size()) {
          return;
        }
      }
    });
  }

  private void addNextButtonActions(JButton button) {
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        int sel = keyComboBox.getSelectedIndex() + 1;
        String caSerialCode = null;
        try {
          caSerialCode = caLogin.getCaSerialCode(sel);
        } catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(null, GV.getSimpleMsg("caReadErrAndAuth") + e.getMessage());
          return;
        }
        paras.put("CASERIALCODE", caSerialCode);
        if (paras.get("URL") == null) {
          String url = ProjectInfoPanel.getWebServerIPAddr();
          paras.put("URL", url);
        }
        doCheckUser();
        if ("true".equals(paras.get("USERPASS"))) {
          alertInfoLabel.setVisible(true);
          alertInfoLabel.setText(getAlertInfo(GV.getSimpleMsg("userPassAndBeginPrice")));
          doNextUpload(paras);
          alertInfoLabel.setVisible(false);
        } else {
          if (paras.get("ERRORMESSAGE") != null) {
            alertInfoLabel.setText(getAlertInfo((String) paras.get("ERRORMESSAGE")));
            alertInfoLabel.setVisible(true);
            return;
          }
          alertInfoLabel.setText(getAlertInfo(GV.getOperateMsg("userNotPassAndReason", (String) paras.get("FAILREASON"))));
          alertInfoLabel.setVisible(true);
          return;
        }
      }
    });
  }

  private void doCheckUser() {
    this.caLogin.setParameterMap(this.paras);
    this.paras.putAll(this.caLogin.doCheckIn());
  }

  protected void doNextUpload(Map<String, String> paras) {
    try {
      Map<String, String> map = (new FileTransferService()).uploadEcbjFile(this.paras);
      String msg = map.get("RESULT");
      if (msg == null || "".equals(msg)) {
        msg = map.get("FAILREASON");
        alertInfoLabel.setVisible(true);
        alertInfoLabel.setText(getAlertInfo(msg));
      } else {
        JOptionPane.showMessageDialog(currDialog, msg);
      }
      currDialog.dispose();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
