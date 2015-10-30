package com.ufgov.zc.client.zc.formula.template;import java.awt.BorderLayout;import java.awt.Color;import java.awt.Dimension;import java.awt.Toolkit;import java.awt.Window;import java.awt.event.ActionEvent;import java.awt.event.KeyAdapter;import java.awt.event.KeyEvent;import java.awt.event.MouseEvent;import java.math.BigDecimal;import java.util.Enumeration;import javax.swing.AbstractAction;import javax.swing.Action;import javax.swing.JFrame;import javax.swing.JOptionPane;import javax.swing.JPanel;import javax.swing.JPopupMenu;import javax.swing.JSplitPane;import javax.swing.JTree;import javax.swing.SwingUtilities;import javax.swing.UnsupportedLookAndFeelException;import javax.swing.event.TreeModelEvent;import javax.swing.event.TreeModelListener;import javax.swing.tree.DefaultMutableTreeNode;import javax.swing.tree.DefaultTreeModel;import javax.swing.tree.TreePath;import com.ufgov.zc.client.common.ParentWindowAware;import com.ufgov.zc.client.component.zc.tree.EventPropertyName;import com.ufgov.zc.client.component.zc.tree.JTreeNode;import com.ufgov.zc.client.component.zc.tree.TreeNodeSelectionListener;import com.ufgov.zc.common.system.util.DigestUtil;import com.ufgov.zc.common.system.util.ObjectUtil;import com.ufgov.zc.common.zc.model.EvalItemType;import com.ufgov.zc.common.zc.model.FormulaRootCode;import com.ufgov.zc.common.zc.model.ZcEbFormulaTemplate;import com.ufgov.zc.common.zc.model.ZcEbFormulaTemplateItem;/** * * @ClassName: TemplateSetMainPanel* @Description: 评标模板设置主面板(这里用一句话描述这个类的作用)* @date: 2010-7-12 上午08:36:03* @version: V1.0 * @since: 1.0* @author: Administrator* @modify: */public class TemplateSetMainPanel extends JPanel implements ParentWindowAware {  private Window parentWindow;  public Window getParentWindow() {    return parentWindow;  }  public void setParentWindow(Window parentWindow) {    this.parentWindow = parentWindow;  }  private JSplitPane mainPanel;  private TemplateTreePanel treePanel;  private DefaultTreeModel model;  private JTreeNode selectedNode;  private TreePath selectedTreePath;  public TemplateTreePanel getTreePanel() {    return treePanel;  }  public void setTreePanel(TemplateTreePanel treePanel) {    this.treePanel = treePanel;  }  public void setSelectedNode(JTreeNode selectedNode) {    this.selectedNode = selectedNode;  }  public JTreeNode getSelectedNode() {    return selectedNode;  }  private ZcEbFormulaTemplate zcEbFormulaTemplate;  private ZcEbFormulaTemplate oldZcEbFormulaTemplate = new ZcEbFormulaTemplate();  private ZcEbFormulaTemplateItem newZcEbFormulaTemplateItem = new ZcEbFormulaTemplateItem();  private ZcEbFormulaTemplateItem oldZcEbFormulaTemplateItem;  private ZcEbTemplateItemEditPanel scorePanel;  private ZcEbTemplateItemEditPanel ItemPanel;  private ZcEbTemplateItemEditPanel compliancePanel;  private ZcEbTemplateListPanel zcEbTemplateListPanel;  private TemplateParamPanel paramPanel;  private ZcEbFormulaTemplateEditPanel templateEditPanel;  public ZcEbTemplateComplianceItemListPanel getZcEbTemplateComplianceItemListPanel() {    return zcEbTemplateComplianceItemListPanel;  }  public ZcEbTemplateScoreItemListPanel getZcEbTemplateScoreItemListPanel() {    return zcEbTemplateScoreItemListPanel;  }  private ZcEbTemplateComplianceItemListPanel zcEbTemplateComplianceItemListPanel;//符合性指标明细面板  private ZcEbTemplateScoreItemListPanel zcEbTemplateScoreItemListPanel;//评分性指标明细面板  public ZcEbFormulaTemplateEditPanel getTemplateEditPanel() {    return templateEditPanel;  }  public void setZcEbFormulaTemplate(ZcEbFormulaTemplate zcEbFormulaTemplate) {    this.zcEbFormulaTemplate = zcEbFormulaTemplate;  }  public ZcEbFormulaTemplate getZcEbFormulaTemplate() {    return zcEbFormulaTemplate;  }  private String templateCode;  //判断结点是否可以删除  boolean canDel;  public DefaultTreeModel getModel() {    return model;  }  public void setModel(DefaultTreeModel model) {    this.model = model;  }  public String getTemplateCode() {    return templateCode;  }  public void setTemplateCode(String formulaCode) {    this.templateCode = formulaCode;  }  public TemplateSetMainPanel(ZcEbFormulaTemplate zcEbFormulaTemplate, ZcEbTemplateListPanel zcEbFormulaTemplateListPanel) {    this.zcEbFormulaTemplate = zcEbFormulaTemplate;    this.oldZcEbFormulaTemplate = (ZcEbFormulaTemplate) ObjectUtil.deepCopy(zcEbFormulaTemplate);    this.templateCode = zcEbFormulaTemplate.getTemplateCode();    this.zcEbTemplateListPanel = zcEbFormulaTemplateListPanel;    init();  }  public void refreshData() {    this.oldZcEbFormulaTemplate = (ZcEbFormulaTemplate) ObjectUtil.deepCopy(zcEbFormulaTemplate);    this.templateCode = zcEbFormulaTemplate.getTemplateCode();    treePanel.setTemplateCode(templateCode);    if (templateCode != null) {      System.out.println("保存模板号：" + templateCode);    }    zcEbTemplateScoreItemListPanel.setTemplateCode(templateCode);    zcEbTemplateComplianceItemListPanel.setTemplateCode(templateCode);    //    zcEbTemplateItemDetailPanel.setTemplateCode(templateCode);    paramPanel.setTemplateCode(templateCode);    paramPanel.refreshData();  }  private void init() {    //初始化树形Panel    treePanel = new TemplateTreePanel();    if (templateCode != null) {      treePanel.setTemplateCode(templateCode);      //加载结点      treePanel.loadTreeNodes();    }    mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);    mainPanel.setMinimumSize(new Dimension(0, 0));    mainPanel.setDividerSize(10);    int width = (int) ((Toolkit.getDefaultToolkit().getScreenSize().width - mainPanel.getDividerSize()) * 0.25);    mainPanel.setDividerLocation(width);    mainPanel.setMinimumSize(new Dimension(0, 0));    addListener();    mainPanel.setLeftComponent(treePanel);    //初始化几种类型的面板    zcEbTemplateScoreItemListPanel = new ZcEbTemplateScoreItemListPanel(templateCode);//评分性指标明细列表页面    //评分性指标编辑panel    scorePanel = new TemplateScoreItemPanel(EvalItemType.SCORE);    scorePanel.setTemplateSetMainPanel(this);    zcEbTemplateComplianceItemListPanel = new ZcEbTemplateComplianceItemListPanel(templateCode);//符合性指标明细列表页面    //符合性指标编辑panel    compliancePanel = new TemplateComplianceItemPanel(EvalItemType.COMPLIANICE);    compliancePanel.setTemplateSetMainPanel(this);    //通用参数编辑panel    paramPanel = new TemplateParamPanel(templateCode);    paramPanel.setTemplateSetMainPanel(this);    //指标集编辑panel    templateEditPanel = new ZcEbFormulaTemplateEditPanel(zcEbFormulaTemplate, this);    templateEditPanel.setZcEbTemplateListPanel(zcEbTemplateListPanel);    //初始化为指标集明细panel    mainPanel.setRightComponent(templateEditPanel);    setLayout(new BorderLayout());    add(mainPanel, BorderLayout.CENTER);  }  private class NodeSelectionListener extends TreeNodeSelectionListener {    public NodeSelectionListener(JTree tree) {      super(tree);    }    /**     * 如果是根节点则只能修改和新增，如果是叶子节点只能删除和修改     */    @Override    protected void doRightMouseClick(MouseEvent e) {      if (templateCode == null) {        System.out.println("模板号为空");        JOptionPane.showMessageDialog(parentWindow, "请先保存指标集再进行操作！", "提示", JOptionPane.INFORMATION_MESSAGE);        return;      }      JPopupMenu popupMenu = new JPopupMenu();      popupMenu.setForeground(Color.blue);      if (canAddChild(e)) {        popupMenu.add(addChildAction);      }      if (canDel && isLeaf(e)) {        popupMenu.addSeparator();        popupMenu.add(deleteAction);      }      setSelectedNode(e);      popupMenu.show(e.getComponent(), e.getX(), e.getY());    }    /**     *     * @Description: 判断结点能否新增。    * @return boolean 返回类型    * @since 1.0     */    private boolean canAddChild(MouseEvent e) {      int x = e.getX();      int y = e.getY();      int row = tree.getRowForLocation(x, y);      TreePath path = tree.getPathForRow(row);      if (path != null) {        JTreeNode node = (JTreeNode) path.getLastPathComponent();        /**         * 通用参数右边直接显示编辑表格进行操作，不允许在树上操作。         */        if (FormulaRootCode.PARAM.equals(node.getCode())) {          return false;        }        if (null != node && !node.getCode().equals(treePanel.getTOP_ROOT_CODE())) {          return true;        }      }      return false;    }    private boolean isLeaf(MouseEvent e) {      int x = e.getX();      int y = e.getY();      int row = tree.getRowForLocation(x, y);      TreePath path = tree.getPathForRow(row);      if (path != null) {        JTreeNode node = (JTreeNode) path.getLastPathComponent();        if (null != node && node.isLeaf()) {          return true;        }      }      return false;    }    @Override    protected void doLeftMouseClick(MouseEvent e) {      int x = e.getX();      int y = e.getY();      int row = tree.getRowForLocation(x, y);      TreePath path = tree.getPathForRow(row);      if (path != null) {        setSelectedNode(e);        JTreeNode node = (JTreeNode) path.getLastPathComponent();        doNodeSelected(node);      }      firePropertyChange(EventPropertyName.MOUSECLICK_PROPERTY_NAME, false, true);    }  }  private void addKeyListener() {    treePanel.getTree().addKeyListener(new KeyAdapter() {      @Override      public void keyReleased(KeyEvent e) {        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {          //        treePanel.getTree().getSelectionRows();          TreePath path = treePanel.getTree().getSelectionPath();          JTreeNode node = (JTreeNode) path.getLastPathComponent();          doNodeSelected(node);        }      }    });  }  /**   *   * @Description: 选中结点后，设置该结点的面板类型  * @return void 返回类型  * @since 1.0   */  private void doNodeSelected(JTreeNode node) {    if (templateCode == null) {      System.out.println("模板号为空");      JOptionPane.showMessageDialog(this, "请先保存指标集再进行操作！", "提示", JOptionPane.INFORMATION_MESSAGE);      return;    }    if (FormulaRootCode.COMPLIANCE.equals(node.getCode())) {      ItemPanel = compliancePanel;      mainPanel.setRightComponent(zcEbTemplateComplianceItemListPanel);      zcEbTemplateComplianceItemListPanel.refreshData();      ItemPanel.setEditNode(node);      canDel = false;    } else if (FormulaRootCode.SCORE.equals(node.getCode())) {      ItemPanel = scorePanel;      mainPanel.setRightComponent(zcEbTemplateScoreItemListPanel);      zcEbTemplateScoreItemListPanel.refreshData();      ItemPanel.setEditNode(node);      canDel = false;    } else if (FormulaRootCode.PARAM.equals(node.getCode())) {      canDel = false;      mainPanel.setRightComponent(paramPanel);    } else if (FormulaRootCode.TOP_ROOT.equals(node.getCode())) {      canDel = false;      mainPanel.setRightComponent(templateEditPanel);    } else {      oldZcEbFormulaTemplateItem = (ZcEbFormulaTemplateItem) node.getUserObject();      //在非根节点，添加子节点，子节点的类型等于根节点。      newZcEbFormulaTemplateItem.setItemType(oldZcEbFormulaTemplateItem.getItemType());      if (EvalItemType.COMPLIANICE.equals(oldZcEbFormulaTemplateItem.getItemType())) {        ItemPanel = compliancePanel;      }      if (EvalItemType.SCORE.equals(oldZcEbFormulaTemplateItem.getItemType())) {        //因为有下级list要显示。所有从新初始化。        ItemPanel = scorePanel;      }      ItemPanel.refreshData(node);      ItemPanel.getSaveButton().setEnabled(true);      ItemPanel.setParentWindow(parentWindow);      //设置右边面板编辑的节点对象      ItemPanel.setEditNode(node);      mainPanel.setRightComponent(ItemPanel);      canDel = true;    }    mainPanel.setDividerLocation(mainPanel.getDividerLocation());  }  private void setSelectedNode(MouseEvent e) {    int x = e.getX();    int y = e.getY();    int row = treePanel.getTree().getRowForLocation(x, y);    selectedTreePath = treePanel.getTree().getPathForRow(row);    if (null != selectedTreePath) {      selectedNode = (JTreeNode) selectedTreePath.getLastPathComponent();    }  }  private class JTreeModelListener implements TreeModelListener {    public void treeNodesChanged(TreeModelEvent e) {      repaintTree();    }    public void treeNodesInserted(TreeModelEvent e) {      repaintTree();    }    public void treeNodesRemoved(TreeModelEvent e) {      repaintTree();    }    public void treeStructureChanged(TreeModelEvent e) {      // repaintTree();    }  }  private Action addChildAction = new AbstractAction("新增") {    private static final long serialVersionUID = 4632421338751212272L;    public void actionPerformed(ActionEvent e) {      //新增的话设置指标项的指标集代码      newZcEbFormulaTemplateItem = new ZcEbFormulaTemplateItem();      newZcEbFormulaTemplateItem.setParentItemCode(selectedNode.getCode());      newZcEbFormulaTemplateItem.setTemplateCode(templateCode);      if (FormulaRootCode.COMPLIANCE.equals(selectedNode.getCode())) {        newZcEbFormulaTemplateItem.setItemType(EvalItemType.COMPLIANICE);      } else if (FormulaRootCode.SCORE.equals(selectedNode.getCode())) {        newZcEbFormulaTemplateItem.setItemType(EvalItemType.SCORE);      } else {        newZcEbFormulaTemplateItem.setItemType(oldZcEbFormulaTemplateItem.getItemType());      }      initItemValue(newZcEbFormulaTemplateItem);      ItemPanel.setZcEbFormulaTemplateItem(newZcEbFormulaTemplateItem);      ItemPanel.setEditingObject(newZcEbFormulaTemplateItem);      ItemPanel.getSaveButton().setEnabled(true);      mainPanel.setRightComponent(ItemPanel);      mainPanel.setDividerLocation(mainPanel.getDividerLocation());    }  };  //新增指标时初始化一些参数。  private void initItemValue(ZcEbFormulaTemplateItem newZcEbFormulaTemplateItem) {    if (EvalItemType.SCORE.equals(newZcEbFormulaTemplateItem.getItemType())) {      //默认不是加减法指标      newZcEbFormulaTemplateItem.setIsAddScore("0");      //至少得分默认为"0"      newZcEbFormulaTemplateItem.setLeastScore(new BigDecimal("0"));    } else {    }  }  private Action deleteAction = new AbstractAction("删除") {    private static final long serialVersionUID = 5130153599721084866L;    public void actionPerformed(ActionEvent e) {      ItemPanel.setZcEbFormulaTemplateItem(oldZcEbFormulaTemplateItem);      ItemPanel.delete();      JTreeNode node = selectedNode;      afterDelete(node);    }  };  public void repaintTree() {    treePanel.getTree().revalidate();    treePanel.getTree().repaint();    if (null != selectedTreePath) {      //      treePanel.getTree().expandPath(selectedTreePath);      //      treePanel.getTree().scrollPathToVisible(selectedTreePath);    }  }  private void addListener() {    NodeSelectionListener lisener = new NodeSelectionListener(treePanel.getTree());    lisener.setNeedDoRightMouseClick(true);    addKeyListener();    treePanel.getTree().addMouseListener(lisener);    JTreeModelListener modelListener = new JTreeModelListener();    treePanel.getTree().getModel().addTreeModelListener(modelListener);    model = (DefaultTreeModel) treePanel.getTree().getModel();  }  private void afterDelete(JTreeNode node) {    if (EvalItemType.COMPLIANICE.equals(oldZcEbFormulaTemplateItem.getItemType())) {      zcEbTemplateComplianceItemListPanel.refreshData();    } else {      zcEbTemplateScoreItemListPanel.refreshData();    }    templateEditPanel.refreshData(zcEbFormulaTemplate);    model.removeNodeFromParent(node);    mainPanel.setRightComponent(new JPanel());    mainPanel.setDividerLocation(mainPanel.getDividerLocation());    repaintTree();  }  /**   *   * @Description: 该节点的分值加上与其同级的节点的 标准分值之和与其父节点标准分值的差值。  * @return BigDecimal 返回类型  * @since 1.0   */  public BigDecimal getParentSubtract(JTreeNode node) {    JTreeNode parentNode = getNodeByCode(node.getParentCode(), (JTreeNode) model.getChild(model.getRoot(), 1));    BigDecimal childScoreSum = getChildScore(parentNode);    if (FormulaRootCode.SCORE.equals(parentNode.getCode())) {      return new BigDecimal(zcEbFormulaTemplate.getFactorFullScore()).subtract(childScoreSum);    } else {      ZcEbFormulaTemplateItem zcEbFormulaTemplateItem = (ZcEbFormulaTemplateItem) parentNode.getUserObject();      return zcEbFormulaTemplateItem.getStandardScore().subtract(childScoreSum);    }  }  /**   *   * @Description:该评分指标节点标准分值与所有下级节点标准分值之和的差值。  * @return BigDecimal 返回类型  * @since 1.0   */  public BigDecimal getChildSubtract(JTreeNode node) {    BigDecimal childScoreSum = getChildScore(node);    if (FormulaRootCode.SCORE.equals(node.getCode())) {      return new BigDecimal(zcEbFormulaTemplate.getFactorFullScore()).subtract(childScoreSum);    } else {      ZcEbFormulaTemplateItem zcEbFormulaTemplateItem = (ZcEbFormulaTemplateItem) node.getUserObject();      return zcEbFormulaTemplateItem.getStandardScore().subtract(childScoreSum);    }  }  /**   *   * @Description: 获得该节点的所有子节点的标准分值之和。  * @return BigDecimal 返回类型  * @since 1.0   */  public BigDecimal getChildScore(JTreeNode node) {    BigDecimal sum = new BigDecimal(0);    Enumeration<?> enumeration = node.children();    while (enumeration.hasMoreElements()) {      BigDecimal weight;      JTreeNode childNode = (JTreeNode) enumeration.nextElement();      ZcEbFormulaTemplateItem zcEbFormulaTemplateItem = (ZcEbFormulaTemplateItem) childNode.getUserObject();      BigDecimal standScore = zcEbFormulaTemplateItem.getStandardScore();      if (null == zcEbFormulaTemplateItem.getWeight()) {        weight = new BigDecimal(100);      } else {        weight = zcEbFormulaTemplateItem.getWeight();      }      //加减分指标不计入标准分值之和。      if (zcEbFormulaTemplateItem.getIsAddScore() == null || zcEbFormulaTemplateItem.getIsAddScore().equals("0")) {        if (standScore != null) {          sum = sum.add(standScore.multiply(weight).divide(new BigDecimal(100)));        }      }    }    return sum;  }  public String confim(JTreeNode node) {    BigDecimal sum = new BigDecimal(0);    Enumeration<?> enumeration = node.children();    //评分指标根节点没有子节点，返回“noChild”    if (DefaultMutableTreeNode.EMPTY_ENUMERATION == enumeration && FormulaRootCode.SCORE.equals(node.getCode())) {      return "noChild";    }    //叶子节点不用验证，之间返回 "comform"    if (DefaultMutableTreeNode.EMPTY_ENUMERATION == enumeration) {      return "conform";    }    //获得子节点评分值的总和    while (enumeration.hasMoreElements()) {      JTreeNode childNode = (JTreeNode) enumeration.nextElement();      ZcEbFormulaTemplateItem zcEbFormulaTemplateItem = (ZcEbFormulaTemplateItem) childNode.getUserObject();      BigDecimal standScore = zcEbFormulaTemplateItem.getStandardScore();      if (standScore != null) {        sum = sum.add(standScore.multiply(zcEbFormulaTemplateItem.getWeight()).divide(new BigDecimal(100)));      }      if (confim(childNode).equals("conform")) {        continue;      } else {        return confim(childNode);      }    }    if (FormulaRootCode.SCORE.equals(node.getCode())) {      if (zcEbFormulaTemplate.getFactorFullScore() == sum.intValue()) {        return "conform";      }    } else {      ZcEbFormulaTemplateItem zcEbFormulaTemplateItem = (ZcEbFormulaTemplateItem) node.getUserObject();      if (zcEbFormulaTemplateItem.getStandardScore().compareTo(sum) == 0) {        return "conform";      }    }    return node.getName();  }  /**   *   * @Description: 从评分性指标根节点开始查找编码为code的节点  * @return JTreeNode 返回类型  * @since 1.0   */  public JTreeNode getNodeByCode(String code, JTreeNode root) {    if ("SC".equals(code) || "".equals(code)) {      return root;    }    Enumeration<?> enumeration = root.children();    while (enumeration.hasMoreElements()) {      JTreeNode node = (JTreeNode) enumeration.nextElement();      if (node.getCode().equals(code)) {        return node;      } else {        if (null == getNodeByCode(code, node)) {          continue;        }      }    }    return null;  }  public boolean isDataChanged() {    return !DigestUtil.digest(oldZcEbFormulaTemplate).equals(DigestUtil.digest(zcEbFormulaTemplate));  }  /**  * @Description: 测试方法。  * @return void 返回类型   * @throws UnsupportedLookAndFeelException    * @throws IllegalAccessException    * @throws InstantiationException    * @throws ClassNotFoundException   * @since 1.0  */  public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException,  UnsupportedLookAndFeelException {    SwingUtilities.invokeLater(new Runnable() {      public void run() {        try {          //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());          //UIManager.setLookAndFeel(new BlueLookAndFeel());        } catch (Exception e) {          e.printStackTrace();        }        ZcEbFormulaTemplate zcEbFormulaTemplate = new ZcEbFormulaTemplate();        zcEbFormulaTemplate.setTemplateCode("test");        ZcEbTemplateListPanel jp = new ZcEbTemplateListPanel();        TemplateSetMainPanel p = new TemplateSetMainPanel(zcEbFormulaTemplate, jp);        JFrame frame = new JFrame("frame");        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        frame.setSize(800, 600);        frame.setLocationRelativeTo(null);        frame.getContentPane().add(p);        frame.setVisible(true);      }    });  }}