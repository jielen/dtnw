package com.ufgov.zc.client.zc.ztb.component;

import com.ufgov.zc.client.zc.ztb.model.DetailsType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * 由用户选择是采用CA证书登录还是用户名/密码登录的方式；
 *
 * @author LEO
 */
public class EcbjLoginTypeSelectionPanel extends LoginTypeSelectionPanel {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public EcbjLoginTypeSelectionPanel(JDialog pDialog, JDialog cDialog, Map<String, String> paras) {
    super(pDialog, cDialog, paras);
    prevDialog = pDialog;
    currDialog = cDialog;
  }

  protected void addNextButtonActions(JButton button) {
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        DetailsType dt = (DetailsType) authorityTypeComboBox.getSelectedItem();
        nextDialog = new JDialog();
        nextDialog.setTitle("报价前身份认证");
        if (LOGIN_TYPE_CA.equals(dt.getValue())) {
          nextDialog.setSize(new Dimension(450, 180));
          nextDialog.setMinimumSize(new Dimension(480, 200));
          nextDialog.add(new UkeySelectionPanel(currDialog, nextDialog, paras));
        } else {
          nextDialog.setSize(new Dimension(420, 180));
          nextDialog.setMinimumSize(new Dimension(420, 210));
          paras.put("iseditable", "N");
          nextDialog.add(new EcbjIDPwdLoginPanel(currDialog, nextDialog, paras));
        }
        nextDialog.setLocationRelativeTo(null);
        nextDialog.setAlwaysOnTop(true);
        nextDialog.setVisible(true);
      }
    });
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    JDialog dialog = new JDialog();
    dialog.setTitle("用户登录选择");
    dialog.setSize(new Dimension(420, 180));
    Map<String, String> para = new HashMap<String, String>();
    dialog.add(new EcbjLoginTypeSelectionPanel(new JDialog(), dialog, para));
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
  }
}
