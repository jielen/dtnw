package com.ufgov.zc.client.zc.flowconsole.parts;import java.awt.Color;import java.awt.GridBagConstraints;import java.awt.GridBagLayout;import java.awt.Insets;import javax.swing.JButton;import javax.swing.JPanel;public class MoudelButton extends JPanel {  /**   *    */  private static final long serialVersionUID = -6267990280381389190L;  public static final Color DEFAULT_SELECTION_COLOR = new Color(253, 192, 47);  //private Image backgroundImage = new Image("/img/func/outlook_bar_background.png");  private JButton mainButton = new JButton();  private JButton rightTopButton = new JButton(" ");  private JButton rightCenterButton = new JButton(" ");  private JButton rightButtomButton = new JButton(" ");  public MoudelButton() {    mainButton.setText("采购任务");    this.setLayout(new GridBagLayout());    this.setBackground(DEFAULT_SELECTION_COLOR);    GridBagConstraints gbc = new GridBagConstraints();    gbc.gridx = 0;    gbc.gridy = 0;    gbc.gridwidth = 1;    gbc.gridheight = 3;    gbc.weightx = 0;    gbc.weighty = 0;    gbc.anchor = GridBagConstraints.NORTHWEST;    gbc.fill = GridBagConstraints.VERTICAL;    gbc.insets = new Insets(0, 0, 0, 0);    gbc.ipadx = 0;    gbc.ipady = 0;    this.add(mainButton, gbc);    gbc = new GridBagConstraints();    gbc.gridx = 2;    gbc.gridy = 0;    gbc.gridwidth = 1;    gbc.gridheight = 1;    gbc.weightx = 0;    gbc.weighty = 0;    gbc.anchor = GridBagConstraints.NORTHWEST;    gbc.fill = GridBagConstraints.NONE;    gbc.insets = new Insets(0, 0, 0, 0);    gbc.ipadx = 0;    gbc.ipady = 0;    rightTopButton.setSize(5, 5);    this.add(rightTopButton, gbc);    gbc = new GridBagConstraints();    gbc.gridx = 2;    gbc.gridy = 1;    gbc.gridwidth = 1;    gbc.gridheight = 1;    gbc.weightx = 0;    gbc.weighty = 0;    gbc.anchor = GridBagConstraints.NORTHWEST;    gbc.fill = GridBagConstraints.NONE;    gbc.insets = new Insets(0, 0, 0, 0);    gbc.ipadx = 0;    gbc.ipady = 0;    this.add(rightCenterButton, gbc);    gbc = new GridBagConstraints();    gbc.gridx = 2;    gbc.gridy = 2;    gbc.gridwidth = 1;    gbc.gridheight = 1;    gbc.weightx = 0;    gbc.weighty = 0;    gbc.anchor = GridBagConstraints.NORTHWEST;    gbc.fill = GridBagConstraints.NONE;    gbc.insets = new Insets(0, 0, 0, 0);    gbc.ipadx = 0;    gbc.ipady = 0;    this.add(rightButtomButton, gbc);  }}