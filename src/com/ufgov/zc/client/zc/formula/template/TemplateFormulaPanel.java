/**   * @(#) project: GK* @(#) file: FormulaPanel.java* * Copyright 2010 UFGOV, Inc. All rights reserved.* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.* */package com.ufgov.zc.client.zc.formula.template;import java.awt.BorderLayout;import java.awt.Color;import java.awt.Dialog;import java.awt.Dimension;import java.awt.FlowLayout;import java.awt.Font;import java.awt.GridBagConstraints;import java.awt.GridBagLayout;import java.awt.GridLayout;import java.awt.Insets;import java.awt.Window;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.event.MouseAdapter;import java.awt.event.MouseEvent;import java.util.ArrayList;import java.util.HashMap;import java.util.Iterator;import java.util.List;import java.util.Map;import java.util.Set;import java.util.Vector;import javax.swing.BorderFactory;import javax.swing.JButton;import javax.swing.JFrame;import javax.swing.JLabel;import javax.swing.JList;import javax.swing.JOptionPane;import javax.swing.JPanel;import javax.swing.JScrollPane;import javax.swing.JTabbedPane;import javax.swing.JTextArea;import javax.swing.JToolBar;import javax.swing.ListSelectionModel;import javax.swing.SwingUtilities;import javax.swing.UIManager;import javax.swing.border.TitledBorder;import bsh.EvalError;import bsh.Interpreter;import com.ufgov.smartclient.common.UIUtilities;import com.ufgov.smartclient.plaf.BlueLookAndFeel;import com.ufgov.zc.client.common.LangTransMeta;import com.ufgov.zc.client.common.ParentWindowAware;import com.ufgov.zc.client.common.ServiceFactory;import com.ufgov.zc.client.common.UIConstants;import com.ufgov.zc.client.common.WorkEnv;import com.ufgov.zc.client.component.GkBaseDialog;import com.ufgov.zc.client.component.JFuncToolBar;import com.ufgov.zc.client.component.MoneyField;import com.ufgov.zc.client.component.button.CalcButton;import com.ufgov.zc.client.component.button.CheckButton;import com.ufgov.zc.client.component.button.ExitButton;import com.ufgov.zc.client.component.button.FuncButton;import com.ufgov.zc.client.component.button.SaveButton;import com.ufgov.zc.client.component.button.zc.CommonButton;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.exception.BaseException;import com.ufgov.zc.common.system.util.RegExpUtil;import com.ufgov.zc.common.zc.model.EvalItemType;import com.ufgov.zc.common.zc.model.ZcEbFormulaTemplateItem;import com.ufgov.zc.common.zc.model.ZcEbFormulaTemplateParam;import com.ufgov.zc.common.zc.publish.IZcEbFormulaTemplateServiceDelegate;/*** @ClassName: FormulaPanel* @Description: 计算公式设置面板* @date: 2010-5-17 下午01:36:09* @version: V1.0  * @since: 1.0* @author: fanpl* @modify: */public class TemplateFormulaPanel extends JPanel implements ParentWindowAware {  private Window parentWindow;  private JPanel centerPanel;  private JPanel calculatorPanel;  private JToolBar jtb;  private JList itemList;  private Vector itemVector;  private JTextArea jt;  private JLabel jl;  private String compoId = "ZC_EB_FORMULA_TEMPLATE";  private String templateCode;  private ZcEbFormulaTemplateItem zcEbFormulaTemplateItem;  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();  private IZcEbFormulaTemplateServiceDelegate zcEbFormulaTemplateServiceDelegate = (IZcEbFormulaTemplateServiceDelegate) ServiceFactory.create(  IZcEbFormulaTemplateServiceDelegate.class, "zcEbFormulaTemplateServiceDelegate");  //  private StringBuffer formulaExpression = new StringBuffer("");  private String formula = null;  private ZcEbTemplateItemEditPanel zcEbTemplateItemEditPanel;  private JFuncToolBar topBar = new JFuncToolBar();  private boolean isSave;  private Map<String, MoneyField> textAreaMap = new HashMap<String, MoneyField>();  private JTextArea resultArea = new JTextArea();  //计算公式的表达式是否正确  private boolean formlaDescIsTrue = false;  public Window getParentWindow() {    return parentWindow;  }  public void setParentWindow(Window parentWindow) {    this.parentWindow = parentWindow;  }  //  public String getFormulaExpression() {  //    return formulaExpression.toString();  //  }  public String getFormula() {    return jt.getText();  }  static {    LangTransMeta.init("ZC%");  }  public TemplateFormulaPanel(ZcEbFormulaTemplateItem zcEbFormulaTemplateItem, ZcEbTemplateItemEditPanel zcEbTemplateItemEditPanel) {    this.zcEbFormulaTemplateItem = zcEbFormulaTemplateItem;    this.zcEbTemplateItemEditPanel = zcEbTemplateItemEditPanel;    this.templateCode = zcEbFormulaTemplateItem.getTemplateCode();    if (null != zcEbFormulaTemplateItem.getFormula()) {      formula = zcEbFormulaTemplateItem.getFormula();    }    init();  }  public void init() {    setLayout(new BorderLayout());    JPanel workPanel = new JPanel(new GridLayout(2, 1));    workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), " 计算公式设置", TitledBorder.CENTER, TitledBorder.TOP,    new Font("宋体", Font.BOLD, 15), Color.BLUE));    initTopBar();    initButtonBar();    this.add(topBar, BorderLayout.NORTH);    //计算公式显示文本域    JPanel formulaAreaPanel = new JPanel(new BorderLayout());    jt = new JTextArea(zcEbFormulaTemplateItem.getFormula());    //  formulaExpression.append(zcEbFormulaItem.getFormulaExpression());    jt.setLineWrap(true);    formulaAreaPanel.add(jt, BorderLayout.CENTER);    formulaAreaPanel.add(jtb, BorderLayout.SOUTH);    JTabbedPane areaTabbedPane = new JTabbedPane();    areaTabbedPane.add("计算公式表达式", formulaAreaPanel);    workPanel.add(areaTabbedPane);    initCenterPanel();    workPanel.add(centerPanel);    this.add(workPanel, BorderLayout.CENTER);  }  public void initTopBar() {    topBar = new JFuncToolBar();    topBar.setModuleCode("ZC");    topBar.setCompoId(compoId);    SaveButton saveButton = new SaveButton();    ExitButton exitButton = new ExitButton();    FuncButton clear = new CommonButton("fclear", "清空", "default.gif");    FuncButton test = new CheckButton();    //    saveButton.setText("确定");    saveButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doSave();      }    });    exitButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doExit();      }    });    test.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doTest();      }    });    clear.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doClear();      }    });    topBar.add(saveButton);    topBar.add(test);    topBar.add(clear);    topBar.add(exitButton);  }  private void doClear() {    jt.setText(null);  }  public void doSave() {    if (beforeSave()) {      boolean success = true;      try {        zcEbFormulaTemplateItem.setFormula(jt.getText().trim());        //        zcEbFormulaItem.setFormulaExpression(formulaExpression.toString());        //更新数据库中的对象        zcEbFormulaTemplateServiceDelegate.updateZcEbFormulaTemplateItem(zcEbFormulaTemplateItem, requestMeta);      } catch (BaseException ex) {        success = false;        UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());      }      if (success) {        JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);        String itemCode = zcEbFormulaTemplateItem.getItemCode();        ZcEbFormulaTemplateItem z1 = new ZcEbFormulaTemplateItem();        z1 = zcEbFormulaTemplateServiceDelegate.getZcEbFormulaTemplateItem(itemCode, requestMeta);        //重置页面编辑对象        zcEbTemplateItemEditPanel.setEditingObject(z1);        zcEbTemplateItemEditPanel.setZcEbFormulaTemplateItem(z1);        //重置树节点编辑对象        zcEbTemplateItemEditPanel.getEditNode().setUserObject(z1);        zcEbFormulaTemplateItem = z1;        isSave = true;      }    }  }  public void doExit() {    if (isDataChanged()) {      int num = JOptionPane.showConfirmDialog(this, "是否保存");      if (num == JOptionPane.YES_OPTION) {        doSave();      }    }    this.parentWindow.dispose();  }  public boolean isDataChanged() {    formula = zcEbFormulaTemplateItem.getFormula();    String str = null;    if (null != jt.getText()) {      str = jt.getText();    }    if (null == formula) {      if ("".equals(str)) {        return false;      } else {        return true;      }    } else {      return !formula.equals(str);    }  }  public boolean beforeSave() {    if (null != jt.getText().trim() && !"".equals(jt.getText().trim())) {      if (formlaDescIsTrue) {        return formlaDescIsTrue;      } else {        doTest();      }      return true;    }    if (formula != null && formula.length() > 0) {      int count1 = 0;      int count2 = 0;      for (int i = 0; i < formula.length(); i++) {        if (formula.charAt(i) == '(') {          count1++;        }        if (formula.charAt(i) == ')') {          count2++;        }      }      if (count1 != count2) {        JOptionPane.showMessageDialog(this, "计算公式括号设置不正确！", "提示", JOptionPane.INFORMATION_MESSAGE);        return false;      }    }    return true;  }  public void initCenterPanel() {    centerPanel = new JPanel();    initButtonBar();    initItemList();    initCalulatorPanel();    JScrollPane js = new JScrollPane(itemList);    JTabbedPane itemTabbedPane = new JTabbedPane();    itemTabbedPane.add(js, " 指标项    ");    centerPanel.setLayout(new BorderLayout());    JTabbedPane calculatorTabbedPane = new JTabbedPane();    calculatorTabbedPane.add(calculatorPanel, "计算器");    JPanel jp = new JPanel();    jp.setLayout(new GridLayout(1, 2));    jp.add(itemTabbedPane);    jp.add(calculatorTabbedPane);    //    centerPanel.add(jt, BorderLayout.WEST);    centerPanel.add(jp, BorderLayout.CENTER);  }  private void initButtonBar() {    jtb = new JToolBar();    String buttonName[] = { "如果", "并且", "或者", "否则" };    String buttonsymbol[] = { "if", "&&", "||", "else" };    JButton b[] = new JButton[buttonName.length];    for (int i = 0; i < buttonName.length; i++) {      b[i] = new JButton(buttonName[i]);      b[i].setSize(10, 10);      final String symbol = buttonsymbol[i];      b[i].addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          int index = jt.getCaretPosition();          jt.setText((new StringBuffer(jt.getText()).insert(index, symbol)).toString());          //          formulaExpression.append(expressionstr);        }      });      jtb.add(b[i]);    }  }  private void initItemList() {    //评分指标集合    List scoreItemlist = new ArrayList();    //通用参数集合    List paramList = new ArrayList();    Map<String, String> map = new HashMap<String, String>();    map.put("templateCode", templateCode);    map.put("itemType", EvalItemType.SCORE);    scoreItemlist = zcEbFormulaTemplateServiceDelegate.getZcEbFormulaTemplateItemList(map, null, requestMeta);    paramList = zcEbFormulaTemplateServiceDelegate.getTemplateParamList(templateCode, requestMeta);    itemVector = new Vector();    for (int i = 0; i < paramList.size(); i++) {      itemVector.add(paramList.get(i));    }    for (int i = 0; i < scoreItemlist.size(); i++) {      itemVector.add(scoreItemlist.get(i));    }    itemList = new JList(itemVector);    itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);    itemList.addMouseListener(new MouseAdapter() {      public void mouseClicked(MouseEvent e) {        if (e.getClickCount() == 2) {          String itemName;          String itemCode;          Object obj = itemList.getSelectedValue();          if (itemList.getSelectedValue() instanceof ZcEbFormulaTemplateItem) {            itemName = "[I" + ((ZcEbFormulaTemplateItem) obj).getName() + "]";            //            itemCode = "@I" + ((ZcEbFormulaItem) obj).getItemCode()+"#";          } else {            itemName = "[P" + ((ZcEbFormulaTemplateParam) obj).getParamName() + "]";            //            itemCode = "@P" + ((ZcEbFormulaParam) obj).getParamCode()+"#";          }          int index = jt.getCaretPosition();          jt.setText((new StringBuffer(jt.getText()).insert(index, itemName)).toString());          //          formulaExpression.append(itemCode);        }      }    });  }  //验证计算公式的正确性  public boolean doTest() {    if (null == jt.getText().trim() || "".equals(jt.getText().trim())) {      JOptionPane.showMessageDialog(this, "计算公式还没有设置", "提示", JOptionPane.INFORMATION_MESSAGE);      return false;    }    String regex = "\\[(?:[^\\]]+)\\]";    List paramList = new ArrayList();    String formula = jt.getText().trim();    if (formula != null && formula.length() > 0) {      int count1 = 0;      int count2 = 0;      for (int i = 0; i < formula.length(); i++) {        if (formula.charAt(i) == '(') {          count1++;        }        if (formula.charAt(i) == ')') {          count2++;        }      }      if (count1 != count2) {        JOptionPane.showMessageDialog(this, "计算公式括号设置不正确！", "提示", JOptionPane.INFORMATION_MESSAGE);        return false;      }    }    //获得该计算公式中用到的参数列表,这里解析出的计算公式有可能存在重复的记录，要把重复的记录去掉    paramList = RegExpUtil.parseRegExp(regex, formula);    String fm = formula.replaceAll("\\[", "");    fm = fm.replaceAll("\\]", "");    System.out.println(fm);    //弹出一个对话框进行，为参数赋值,进行校验.    GkBaseDialog dialog = new GkBaseDialog(this.parentWindow, Dialog.ModalityType.APPLICATION_MODAL);    dialog.add(createTestPanel(paramList, fm));    dialog.setTitle("校验计算公式");    dialog.setSize(500, 600);    dialog.moveToScreenCenter();    dialog.setVisible(true);    //      for (int g = 0; g < paramList.size(); g++) {    //        if (paramMap.containsKey((String) (paramList.get(g)))) {    //          i.set((String) (paramList.get(g)), paramMap.get((String) (paramList.get(g))));    //        }    //      }    return false;  }  private JPanel createTestPanel(List paramList, final String fm) {    JPanel panel = new JPanel();    panel.setLayout(new BorderLayout());    //工具栏按钮    JToolBar bar = new JToolBar();    bar.setFloatable(false);    bar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));    JPanel workPanel = new JPanel(new GridLayout(2, 1));    workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "校验计算公式", TitledBorder.CENTER, TitledBorder.TOP,    new Font("宋体", Font.BOLD, 15), Color.BLUE));    //参数输入框    JPanel textAreaPanel = new JPanel();    textAreaPanel.setLayout(new GridBagLayout());    int row = 0;    int col = 0;    int colCount = 2;    textAreaMap.clear();    resultArea.setText("");    for (int k = 0; k < paramList.size(); k++) {      String name = (String) paramList.get(k);      if (!textAreaMap.containsKey(name) && !name.substring(1).equals(zcEbFormulaTemplateItem.getName())) {        MoneyField textField = new MoneyField();        JLabel label = new JLabel(name.substring(1));        textField.setPreferredSize(new Dimension(150, 23));        textAreaMap.put(name, textField);        textAreaPanel.add(label, new GridBagConstraints(col, row, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0,        5, 5), 0, 0));        textAreaPanel.add(textField, new GridBagConstraints(col + 1, row, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,        new Insets(5, 0, 5, 5), 0, 0));        if (col == colCount * 2 - 2) {          row++;          col = 0;        } else {          col += 2;        }      }    }    JTabbedPane tabbPanel = new JTabbedPane();    tabbPanel.add("请输入参数值", textAreaPanel);    //计算结果框    JPanel resultPanel = new JPanel();    resultPanel.setBounds(20, 20, 200, 180);    resultPanel.setLayout(null);    JLabel label = new JLabel("计算结果");    //填写意见    label.setBounds(30, 62, 100, 24);    label.setLayout(null);    resultPanel.add(label);    resultArea.setLayout(null);    resultArea.setLineWrap(true);    resultArea.setBounds(30, 84, 400, 70);    resultPanel.add(resultArea);    workPanel.add(tabbPanel);    workPanel.add(resultPanel);    CalcButton calcButton = new CalcButton();    //添加按钮计算事件    calcButton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doCalFormula(fm);      }    });    bar.add(calcButton);    panel.add(bar, BorderLayout.NORTH);    panel.add(workPanel, BorderLayout.CENTER);    return panel;  }  private void initCalulatorPanel() {    calculatorPanel = new JPanel(new GridBagLayout());    GridBagConstraints constraints = new GridBagConstraints();    constraints.weightx = 0.5;    constraints.weighty = 0;    constraints.gridx = constraints.gridy = 0;    constraints.insets = new Insets(2, 2, 2, 2);    String name[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "-", "*", "/", ".", "(", ")", ";", "{", "}", "=", "<", ">", "退格" };    final JButton b[] = new JButton[24];    for (int i = 0; i < name.length; i++) {      constraints.gridx = i % 3;      constraints.gridy = i / 3;      b[i] = new JButton(name[i]);      if (i < name.length - 1) {        final String namestr = b[i].getText();        b[i].addActionListener(new ActionListener() {          public void actionPerformed(ActionEvent e) {            int index = jt.getCaretPosition();            jt.setText((new StringBuffer(jt.getText()).insert(index, namestr)).toString());          }        });      } else {        //退格键的处理        b[i].addActionListener(new ActionListener() {          public void actionPerformed(ActionEvent e) {            String s = jt.getText();            if (s.length() > 0) {              if (s.charAt(s.length() - 1) == ']') {                jt.setText(s.substring(0, s.lastIndexOf("[")));              } else {                jt.setText(s.substring(0, s.length() - 1));              }            }          }        });      }      calculatorPanel.add(b[i], constraints);    }  }  private boolean doCalFormula(String fm) {    Set set = textAreaMap.keySet();    Iterator it = set.iterator();    Interpreter i = new Interpreter();    StringBuffer errorMes = new StringBuffer();    try {      while (it.hasNext()) {        String key = (String) it.next();        if (textAreaMap.get(key).getText() == null || "".equals(textAreaMap.get(key).getText())) {          errorMes.append("参数" + textAreaMap.get(key) + "参数值没有设置" + "\n");        }        i.set(key, Double.valueOf(textAreaMap.get(key).getText()));      }      if (errorMes.length() > 0) {        JOptionPane.showMessageDialog(this, "errorMes", "提示", JOptionPane.INFORMATION_MESSAGE);        return false;      }      System.out.println(fm);      i.eval(fm);      if (null == i.get("I" + zcEbFormulaTemplateItem.getName())) {        JOptionPane.showMessageDialog(this, "计算公式设置不正确", "提示", JOptionPane.INFORMATION_MESSAGE);        return false;      }      formlaDescIsTrue = true;      JOptionPane.showMessageDialog(this, "计算公式设置成功", "提示", JOptionPane.INFORMATION_MESSAGE);      System.out.println(i.get("I" + zcEbFormulaTemplateItem.getName()));      resultArea.setText(String.valueOf(i.get("I" + zcEbFormulaTemplateItem.getName())));      return true;    } catch (EvalError e) {      e.printStackTrace();      JOptionPane.showMessageDialog(this, "计算公式设计格式不正确，请重新设置！请参照例子\n'if (P供应商数 > 7 ){ I价格分=I投标价*(1-P调整系数);} else{"      + "  I价格分= I投标价*(1-P调整系数) +1000 ;}'", "提示", JOptionPane.INFORMATION_MESSAGE);      return false;    }  }  public static void main(String[] args) {    SwingUtilities.invokeLater(new Runnable() {      public void run() {        try {          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());          UIManager.setLookAndFeel(new BlueLookAndFeel());        } catch (Exception e) {          e.printStackTrace();        }        ZcEbFormulaTemplateItem zcEbFormulaTemplateItem = new ZcEbFormulaTemplateItem();        zcEbFormulaTemplateItem.setTemplateCode("test");        //        FormulaPanel bill = new FormulaPanel(zcEbFormulaItem);        JFrame frame = new JFrame("frame");        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        frame.setSize(UIConstants.DIALOG_2_LEVEL_WIDTH, UIConstants.DIALOG_2_LEVEL_HEIGHT);        frame.setLocationRelativeTo(null);        //        frame.getContentPane().add(bill);        frame.setVisible(true);      }    });  }}