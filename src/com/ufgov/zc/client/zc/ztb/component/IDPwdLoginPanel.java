package com.ufgov.zc.client.zc.ztb.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.ufgov.zc.client.util.PwdUtil;
import com.ufgov.zc.client.zc.ztb.model.DetailsType;
import com.ufgov.zc.client.zc.ztb.util.CALogin;
import com.ufgov.zc.client.zc.ztb.util.GV;

public class IDPwdLoginPanel extends BaseSelectionPanel {
  private static final long serialVersionUID = -7267204107723386392L;

  protected Map paras = new HashMap();

  protected JTextField userIDField = new JTextField();

  protected JPasswordField passwordField = new JPasswordField();

  protected CALogin caLogin = new CALogin();

  public IDPwdLoginPanel(JDialog pDialog, JDialog cDialog, Map map) {
    prevDialog = pDialog;
    currDialog = cDialog;
    paras.putAll(map);
    init();
    buildContents();
  }

  protected void init() {
    if (prevDialog != null) {
      prevDialog.dispose();
    }
    if (nextDialog != null) {
      nextDialog.dispose();
    }
    this.alertInfoLabel = new JLabel(" ");
    alertInfoLabel.setPreferredSize(new Dimension(360, 50));
    alertInfoLabel.setVisible(false);
    alertInfoLabel.setHorizontalAlignment(SwingUtilities.LEFT);
  }

  protected void buildContents() {
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
    JLabel l1 = new JLabel(GV.getSimpleMsg("userTypeLabel"));
    panel.add(l1, c);
    c.gridx = 1;
    c.gridy = gridy;
    c.gridwidth = 2;
    userTypeComboBox = new JComboBox(this.getUserTypesVector());
    userTypeComboBox.setPreferredSize(new Dimension(280, 22));
    Object selItem1 = userTypeComboBox.getSelectedItem();
    if (selItem1 != null) {
      userTypeComboBox.setToolTipText(selItem1.toString());
    }
    String userType = (String) this.paras.get("USERTYPE");
    if (userType != null && !"".equals(userType)) {
      setComboBoxDefaultValue(userType);
    }
    panel.add(userTypeComboBox, c);
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 1;
    JLabel l2 = new JLabel(GV.getSimpleMsg("userNameLabel"));
    panel.add(l2, c);
    c.gridx = 1;
    c.gridy = gridy;
    c.gridwidth = 2;
    userIDField.setPreferredSize(new Dimension(200, 20));
    String userID = (String) this.paras.get("userid");
    if (userID != null && !"".equals(userID)) {
      userIDField.setText(userID);
    }
    panel.add(userIDField, c);
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 1;
    JLabel l3 = new JLabel(GV.getSimpleMsg("userPwdLabel"));
    panel.add(l3, c);
    c.gridx = 1;
    c.gridy = gridy;
    c.gridwidth = 2;
    passwordField.setPreferredSize(new Dimension(200, 20));
    String userPwd = (String) this.paras.get("PASSWORD");
    if (userPwd != null && !"".equals(userPwd)) {
      passwordField.setText(userPwd);
    }
    panel.add(passwordField, c);
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 4;
    JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p1.add(alertInfoLabel);
    panel.add(p1, c);
    JButton button = new JButton(GV.getTranslate("login"));
    button.setPreferredSize(new Dimension(80, 22));
    addLoginButtonActions(button);
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

  protected String getAlertInfo(String msg) {
    StringBuffer alertInfo = new StringBuffer("<html><a><font size='3' color='red'>提示：");
    alertInfo.append(msg);
    alertInfo.append("</font><a></html>");
    return alertInfo.toString();
  }

  protected void addLoginButtonActions(JButton button) {
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        String userType = "";
        if (userTypeComboBox != null) {
          userType = ((DetailsType) userTypeComboBox.getSelectedItem()).getValue();
        } else {
          userType = "supplier";
        }
        String userID = userIDField.getText().trim();
        if (userID == null || "".equals(userID)) {
          alertInfoLabel.setVisible(true);
          alertInfoLabel.setText(getAlertInfo("用户名不能为空！"));
          return;
        }
        String passwd = new String(passwordField.getPassword()).trim();
        passwd = PwdUtil._encodePwd(passwd);
        paras.put("USERID", userID);
        paras.put("PASSWORD", passwd);
        paras.put("USERTYPE", userType);
        if(paras.get("URL")==null||"".equals(paras.get("URL"))){  //再次报价的时候 没有服务器地址
             paras.put("URL", ProjectInfoPanel.getWebServerIPAddr());
        }
        doCheckUser();
        if ("true".equals(paras.get("USERPASS"))) {
          alertInfoLabel.setVisible(true);
          alertInfoLabel.setText(getAlertInfo(GV.getSimpleMsg("userPassAndNext")));
        } else {
          if (paras.get("ERRORMESSAGE") != null) {
            alertInfoLabel.setText(getAlertInfo((String) paras.get("ERRORMESSAGE")));
            alertInfoLabel.setVisible(true);
            return;
          }
          alertInfoLabel.setText(getAlertInfo(GV.getSimpleMsg("userNotPassReason") + paras.get("FAILREASON")));
          alertInfoLabel.setVisible(true);
          return;
        }
        doNextUpload(paras);
      }
    });
  }

  protected void doCheckUser() {
    this.paras.put("CASERIALCODE", "NOID");
    this.caLogin.setParameterMap(this.paras);
    this.paras.putAll(this.caLogin.doCheckIn());
  }

  /* protected void doNextUpload(Map<String, String> paras) {
     try {
       if ("true".equals(paras.get("USERPASS"))) {
         this.prevDialog.dispose();
         makeWaitingDecodingListDialog(paras);
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
  */

  /**
   * @param args
   */
  public static void main(String[] args) {
    JDialog dialog = new JDialog();
    Map map = new HashMap();
    map.put("PROJECTCODE", "XCZX2011-110");
    map.put("PROJECTNAME", "测试项目");
    map.put("PACKCODE", "1921");
    map.put("PACKNAME", "标段一");
    dialog.add(new IDPwdLoginPanel(null, dialog, map));
    dialog.setSize(new Dimension(400, 200));
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
  }
}
