package com.ufgov.zc.client.zc.ztb.component;

import com.ufgov.zc.client.zc.ztb.service.FileTransferService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class EcbjIDPwdLoginPanel extends IDPwdLoginPanel {
    private static final long serialVersionUID = -7267204107723386392L;

    public EcbjIDPwdLoginPanel(JDialog pDialog, JDialog cDialog, Map map) {
        super(pDialog, cDialog, map);
        prevDialog = pDialog;
        currDialog = cDialog;
        paras.putAll(map);
    }

    protected void buildContents() {
        prevDialog.dispose();
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
        JLabel l2 = new JLabel("用户名：");
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
        JLabel l3 = new JLabel("密  码：");
        panel.add(l3, c);
        c.gridx = 1;
        c.gridy = gridy;
        c.gridwidth = 2;
        passwordField.setPreferredSize(new Dimension(200, 20));
        String userPwd = (String) this.paras.get("password");
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
        JButton button = new JButton("提交报价");
        button.setPreferredSize(new Dimension(80, 22));
        addLoginButtonActions(button);
        JButton cancel = new JButton("取  消");
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

    protected void doNextUpload(Map<String, String> paras) {
        try {
            Map<String, String> map = (new FileTransferService()).uploadEcbjFile(this.paras);
            String msg = map.get("RESULT");
            if (msg == null || "".equals(msg)) {
                msg = map.get("FAILREASON");
                alertInfoLabel.setVisible(true);
                alertInfoLabel.setText(getAlertInfo(msg));
            }
            JOptionPane.showMessageDialog(currDialog, msg);
            currDialog.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
