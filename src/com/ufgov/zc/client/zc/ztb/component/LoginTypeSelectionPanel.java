package com.ufgov.zc.client.zc.ztb.component;

import com.ufgov.zc.client.zc.ztb.model.DetailsType;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.client.zc.ztb.util.PubFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 由用户选择是采用CA证书登录还是用户名/密码登录的方式；
 *
 * @author LEO
 */
public class LoginTypeSelectionPanel extends BaseSelectionPanel {
  public static final String LOGIN_TYPE_CA = "CA_AUTHENTICATE";

  public static final String LOGIN_TYPE_ID_PWD = "ID_PWD_AUTHENTICATE";

  public JButton loginBtn;

  protected JComboBox authorityTypeComboBox = new JComboBox();

  protected Map<String, String> paras = new HashMap<String, String>();

  public LoginTypeSelectionPanel() {
  }

  public LoginTypeSelectionPanel(JDialog cDialog, Map<String, String> paras) {
    this.paras = paras;
    currDialog = cDialog;
    init();
    createPanel();
  }

  public LoginTypeSelectionPanel(JDialog pDialog, JDialog cDialog) {
    prevDialog = pDialog;
    currDialog = cDialog;
    init();
    createPanel();
  }

  public LoginTypeSelectionPanel(JDialog pDialog, JDialog cDialog, Map<String, String> paras) {
    this.paras = paras;
    prevDialog = pDialog;
    currDialog = cDialog;
    init();
    createPanel();
  }

  private void init() {
    if (prevDialog != null) {
      prevDialog.dispose();
    }
  }

  private void createPanel() {
    currDialog.setMinimumSize(new Dimension(420, 180));
    this.setLayout(new BorderLayout());
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(5, 5, 0, 5);
    int gridy = 0;
    c.gridx = 0;
    c.gridy = gridy;
    c.gridwidth = 2;
    JLabel l1 = new JLabel(GV.getSimpleMsg("pleaseSelectLoginType"));
    panel.add(l1, c);
    ++gridy;
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 1;
    JLabel l2 = new JLabel(GV.getSimpleMsg("authType"));
    panel.add(l2, c);
    c.gridx = 1;
    c.gridy = gridy;
    c.gridwidth = 2;
    authorityTypeComboBox = new JComboBox(this.getAuthorTypesVector());
    authorityTypeComboBox.setPreferredSize(new Dimension(280, 22));
    Object selItem1 = authorityTypeComboBox.getSelectedItem();
    if (selItem1 != null) {
      authorityTypeComboBox.setToolTipText(selItem1.toString());
    }
    panel.add(authorityTypeComboBox, c);
    c.gridx = 0;
    c.gridy = ++gridy;
    c.gridwidth = 4;
    StringBuffer alertInfo = new StringBuffer("<html><a><b><font size='3' color='red'>提示：&nbsp;&nbsp;");
    alertInfo.append(GV.getSimpleMsg("askForCAkey"));
    alertInfo.append("</font><b><a></html>");
    JLabel l3 = new JLabel();
    l3.setPreferredSize(new Dimension(300, 22));
    l3.setText(alertInfo.toString());
    panel.add(l3, c);
    JButton button = new JButton(GV.getTranslate("next"));
    button.setPreferredSize(new Dimension(80, 22));
    addNextButtonActions(button);
    JButton cancel = new JButton(GV.getTranslate("cancel"));
    cancel.setPreferredSize(new Dimension(80, 22));
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        currDialog.dispose();
      }
    });
    JPanel btPanel = new JPanel();
    btPanel.setLayout(new FlowLayout());
    btPanel.add(cancel);
    btPanel.add(button);
    this.add(btPanel, BorderLayout.SOUTH);
    this.add(panel, BorderLayout.CENTER);
  }

  protected void addNextButtonActions(JButton button) {
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        DetailsType dt = (DetailsType) authorityTypeComboBox.getSelectedItem();
        nextDialog = new JDialog();
        nextDialog.setTitle(GV.getSimpleMsg("userLoginTitle"));
        if (LOGIN_TYPE_CA.equals(dt.getValue())) {
          nextDialog.setSize(new Dimension(470, 180));
          nextDialog.setMinimumSize(new Dimension(450, 200));
          nextDialog.add(new CALoginPanel(currDialog, nextDialog, paras));
        } else {
          nextDialog.setSize(new Dimension(420, 210));
          nextDialog.setMinimumSize(new Dimension(420, 210));
          nextDialog.add(new IDPwdLoginPanel(currDialog, nextDialog, paras));
        }
        nextDialog.setLocation(PubFunction.getBetterPosition(nextDialog.getSize(), 0, 100));
        nextDialog.setAlwaysOnTop(true);
        nextDialog.setVisible(true);
        nextDialog.toFront();
      }
    });
  }

  private Vector<DetailsType> getAuthorTypesVector() {
    Vector<DetailsType> utVector = new Vector<DetailsType>();
    DetailsType dt = null;
    dt = new DetailsType(GV.getSimpleMsg("typeCALogin"), LOGIN_TYPE_CA);
    utVector.add(dt);
    dt = new DetailsType(GV.getSimpleMsg("typePwdLogin"), LOGIN_TYPE_ID_PWD);
    utVector.add(dt);
    return utVector;
  }

  public JDialog getPrevDialog() {
    return prevDialog;
  }

  public void setPrevDialog(JDialog prevDialog) {
    this.prevDialog = prevDialog;
  }

  public JDialog getCurrDialog() {
    return currDialog;
  }

  public void setCurrDialog(JDialog currDialog) {
    this.currDialog = currDialog;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    JDialog dialog = new JDialog();
    dialog.setTitle(GV.getSimpleMsg("userLoginTypeSelect"));
    dialog.setSize(new Dimension(420, 180));
    Map<String, String> para = new HashMap<String, String>();
    dialog.add(new LoginTypeSelectionPanel(dialog, para));
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.setLocation(PubFunction.getBetterPosition(dialog.getSize(), 0, 100));
    dialog.setVisible(true);
  }
}
