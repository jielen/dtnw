package com.ufgov.zc.client.zc.ztb.component;

import com.ufgov.zc.client.zc.ztb.model.BusinessProject;
import com.ufgov.zc.client.zc.ztb.model.DetailsType;
import com.ufgov.zc.client.zc.ztb.model.ProjectBag;
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
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class EcbjProjectSelectionPanel extends BaseSelectionPanel {
    private static final long serialVersionUID = -7267204107723386392L;

    private JRadioButton[] packsRB = null;

    public EcbjProjectSelectionPanel(JDialog cDialog, MainPanel mp) {
        currDialog = cDialog;
        mainPanel = mp;
        init();
        buildContents();
    }

    private void init() {
        projList.clear();
        try {
            Map<String, String> map = new HashMap<String, String>();
            if (mainPanel instanceof TBPanel) {//TTBPanel时从配置文件读取
                map.put("URL", ProjectInfoPanel.getWebServerIPAddr());
            }
            projList.addAll(dbImportProjectService.getSignupBusinessProjectForEcbj(map));
        } catch (Exception e) {
            errorTip(GV.getSimpleMsg("notExistEcbjProject"));
        }
        errorTip(GV.getSimpleMsg("notExistEcbjProjectThisTime"));
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
        ztbPathField.setPreferredSize(new Dimension(280, 22));
        panel.add(ztbPathField, c);
        c.gridx = 3;
        c.gridy = gridy;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        JButton brower = new JButton(GV.getTranslate("brower"));
        brower.setPreferredSize(new Dimension(80, 22));
        this.addBrowerActions(brower);
        panel.add(brower, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = ++gridy;
        c.gridwidth = 1;
        JLabel l2 = new JLabel(GV.getSimpleMsg("projList"));
        panel.add(l2, c);
        c.gridx = 1;
        c.gridy = gridy;
        c.gridwidth = 3;
        Vector<DetailsType> dtVector = getOptions();
        projectsCB = new JComboBox(dtVector);
        projectsCB.setSelectedItem(0);
        DetailsType dt = (DetailsType) projectsCB.getSelectedItem();
        if (dt != null) {
            projectsCB.setToolTipText(dt.getName());
        }
        this.addComboBoxSelectChangeActions();
        panel.add(projectsCB, c);
        c.gridx = 0;
        c.gridy = ++gridy;
        c.gridwidth = 1;
        JLabel l21 = new JLabel(GV.getSimpleMsg("pricedPack"));
        panel.add(l21, c);
        c.gridx = 1;
        c.gridy = gridy;
        c.gridwidth = 3;
        JPanel p22 = new JPanel(new BorderLayout());
        p22.setPreferredSize(new Dimension(300, 100));
        packsScrollPane.setPreferredSize(new Dimension(160, 100));
        packsScrollPane.getViewport().add(getRBPanel(0));
        p22.add(packsScrollPane, BorderLayout.CENTER);
        panel.add(p22, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = ++gridy;
        c.gridwidth = 4;
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (alertInfoLabel == null) {
            alertInfoLabel = new JLabel(" ");
            alertInfoLabel.setPreferredSize(new Dimension(380, 30));
            alertInfoLabel.setVisible(false);
        }
        alertInfoLabel.setHorizontalAlignment(SwingUtilities.LEFT);
        p1.add(alertInfoLabel);
        panel.add(p1, c);
        JButton button = new JButton(GV.getTranslate("next"));
        button.setPreferredSize(new Dimension(80, 22));
        addNextButtonActions(button);
        JButton cancel = new JButton(GV.getTranslate("cancel"));
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

    private JPanel getRBPanel(int index) {
        if (this.projList == null || this.projList.size() < 1) {
            return new JPanel();
        }
        ButtonGroup radioGroup = new ButtonGroup();
        packsRB = new JRadioButton[this.projList.get(index).getPackList().size()];
        JPanel cbPanel = new JPanel(new GridLayout(5, 2));
        cbPanel.setPreferredSize(new Dimension(140, 100));
        if (this.projList.size() > 0) {
            List<ProjectBag> pklist = projList.get(index).getPackList();
            if (pklist != null) {
                for (int i = 0; i < pklist.size(); i++) {
                    ProjectBag pb = pklist.get(i);
                    packsRB[i] = new JRadioButton(pb.getPackDesc() + "[" + pb.getName() + "]");
                    packsRB[i].setName(pb.getNo());
                    packsRB[i].setToolTipText(pb.getPackDesc() + "[" + pb.getName() + "]");
                    radioGroup.add(packsRB[i]);
                    cbPanel.add(packsRB[i]);
                }
            }
            if (pklist.size() == 1) {
                packsRB[0].setSelected(true);
            }
        }
        return cbPanel;
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
                    GV.setHistoryDirectory(fileChooser.getSelectedFile().getParent());
                    alertInfoLabel.setVisible(false);
                }
            }
        });
    }

    private void addComboBoxSelectChangeActions() {
        projectsCB.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                int index = projectsCB.getSelectedIndex();
                if (index > projList.size()) {
                    return;
                }
                packsScrollPane.getViewport().add(getRBPanel(index));
                packsScrollPane.updateUI();
            }
        });
    }

    private String getSelectedPacks() {
        if (packsRB == null) {
            return "";
        }
        StringBuffer buff = new StringBuffer(" ");
        for (int i = 0; i < packsRB.length; i++) {
            if (packsRB[i].isSelected()) {
                buff.append(packsRB[i].getName());
                buff.append(",");
            }
        }
        buff.deleteCharAt(buff.length() - 1);
        return buff.toString();
    }

    private ProjectBag getSelectedPack(String projectCode, String packCode) {
        if (projectCode == null || packCode == null) {
            return null;
        }
        List<BusinessProject> list = this.projList;
        for (int i = 0; i < list.size(); i++) {
            if (projectCode.equals(list.get(i).getNo())) {
                List<ProjectBag> packs = list.get(i).getPackList();
                if (packs != null && packs.size() > 0) {
                    for (int j = 0; j < packs.size(); j++) {
                        if (packCode.equals(packs.get(j).getNo())) {
                            return packs.get(j);
                        }
                    }
                }
            }
        }
        return null;
    }


    private void addNextButtonActions(JButton button) {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (projectsCB.getSelectedItem() == null) {
                    alertInfoLabel.setText(GV.getSimpleMsg("noPriceItems"));
                    alertInfoLabel.setVisible(true);
                    return;
                } else {
                    alertInfoLabel.setVisible(false);
                }
                String projcode = ((DetailsType) projectsCB.getSelectedItem()).getValue();
                Map<String, String> paras = new HashMap<String, String>();
                paras.put("PROJECTCODE", projcode.trim());
                paras.put("PROJECTNAME", ((DetailsType) projectsCB.getSelectedItem()).getName());
                String packCode=getSelectedPacks().trim();
                ProjectBag pack = getSelectedPack(projcode,packCode);

                if (pack != null) {
                    paras.put("PACKNAME", pack.getName());
                }
                paras.put("PACKCODE", packCode);
                StringBuffer alertInfo = new StringBuffer("<html><a><b><font size='3' color='red'>提示：<br>");
                String ztbPath = ztbPathField.getText().trim();
                int seq = 1;
                if ("".equals(ztbPath)) {
                    alertInfo.append("&nbsp;&nbsp;" + (seq++) + "、" + GV.getSimpleMsg("pleaseSelectPriceFile"));
                    alertInfo.append("<br>");
                }
                if ("".equals(paras.get("PACKCODE"))) {
                    alertInfo.append("&nbsp;&nbsp;" + (seq++) + "、" + GV.getSimpleMsg("mustSelectOnePack"));
                }
                alertInfo.append("</font><b><a></html>");
                if (seq > 1) {
                    if (seq > 2) {
                        alertInfoLabel.setPreferredSize(new Dimension(360, 54));
                    }
                    alertInfoLabel.setText(alertInfo.toString());
                    alertInfoLabel.setVisible(true);
                    alertInfoLabel.updateUI();
                    return;
                } else {
                    alertInfoLabel.setVisible(false);
                }
                webServiceIP = ((TTBPanel) mainPanel).getDbProperty().getWebServiceHostPort();
                paras.put("ZTBFULLPATH", ztbPath);
                String url = webServiceIP + "/ZC/";
                url = url.replaceAll("http://http://", "http://");
                paras.put("URL", url);
                paras.put("FILEFULLPATH", ztbPathField.getText().trim());
                mainPanel.loadRightInitInfoPanel(projcode);
                doNextUpload(paras);
            }
        });
    }

    protected void doNextUpload(Map<String, String> paras) {
        nextDialog = new JDialog();
        nextDialog.setTitle("用户身份认证方式选择");
        nextDialog.add(new EcbjLoginTypeSelectionPanel(currDialog, nextDialog, paras));
        nextDialog.setSize(new Dimension(480, 200));
        nextDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        nextDialog.setLocationRelativeTo(null);
        nextDialog.toFront();
        nextDialog.setModal(true);
        nextDialog.setVisible(true);
    }
}
