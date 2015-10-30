package com.ufgov.zc.client.component.ui;import java.awt.BorderLayout;import java.awt.Cursor;import java.awt.Dimension;import java.awt.FlowLayout;import java.awt.GridBagConstraints;import java.awt.GridBagLayout;import java.awt.GridLayout;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.event.MouseAdapter;import java.awt.event.MouseEvent;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import javax.swing.AbstractAction;import javax.swing.BorderFactory;import javax.swing.JButton;import javax.swing.JComponent;import javax.swing.JMenuItem;import javax.swing.JPanel;import javax.swing.JPopupMenu;import javax.swing.SwingUtilities;import com.ufgov.zc.client.common.BillElementMeta;import com.ufgov.zc.client.component.JListSelectDialog;import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;import com.ufgov.zc.client.component.ui.conditionitem.Searchable;import com.ufgov.zc.client.component.ui.groupitem.ElementGroupConditionItem;import com.ufgov.zc.client.util.SwingUtil;import com.ufgov.zc.client.util.UserPreferencesUtil;import com.ufgov.zc.common.commonbiz.model.SearchCondition;/** * <p>Title: GK</p> * <p>Description: 搜索条件区，显示搜索条件</p> * <p>Copyright: Copyright 2009 ufgov, Inc.</p> * <p>Company: ufgov</p> * <p>创建时间: 2009-4-20</p> * @author 刘永伟(manlge) * @version 1.0 */public class AbstractSearchConditionArea extends JComponent implements Searchable {  /**   * 所有的搜条件项   */  protected List<SearchCondition> allConditions = new ArrayList<SearchCondition>();  /**   * 正在显示的搜索条件项   */  protected List<SearchCondition> showingConditions = new ArrayList<SearchCondition>();  protected List<AbstractSearchConditionItem> showingConditionItems = new ArrayList<AbstractSearchConditionItem>();  protected BillElementMeta bem;  protected Map defaultValueMap = new HashMap();  protected String numLimCompoId;  /**   * 显示“查询条件”等元素的右键菜单   */  protected JPopupMenu popupMenu = new JPopupMenu();  protected JPanel showControlPanel = new JPanel(new BorderLayout());  protected JPanel conditionItemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));  protected JPanel groupItemPanel = new JPanel(new GridLayout(0, 2, 20, 0));  public List groupField = new ArrayList();  private List groupItemList = new ArrayList();  protected boolean showGroupPanel = false;  protected JMenuItem showSearchConditionSelectDialogMenuItem = new JMenuItem(new AbstractAction("设置查询条件 ") {    public void actionPerformed(ActionEvent e) {      showSearchConditionSelectDialog();    }  });  protected JMenuItem showTabSelectDialogMenuItem = new JMenuItem(new AbstractAction("设置页签 ") {    public void actionPerformed(ActionEvent e) {      showTabSettingDialog();    }  });  protected void showTabSettingDialog() {    dataDisplay.showTabSelectDialog();  }  /**   * 与当前对象在同一容器的DataDisplay对象   */  protected AbstractDataDisplay dataDisplay;  protected String conditionId;  public List getGroupItemList() {    return groupItemList;  }  public void setGroupItemList(List groupItemList) {    this.groupItemList = groupItemList;  }  private boolean isRefresh = true;//条件发生变化时，是否查询，默认查询  /**   * @param searchConditionItems 所有搜索条件项   * @param selectedSearchConditionItems 已经选择的搜索条件项   */  public AbstractSearchConditionArea(String conditionId, List<SearchCondition> searchConditionItems,  List<SearchCondition> selectedSearchConditionItems, BillElementMeta bem, Map defaultValueMap) {    this.conditionId = conditionId;    this.allConditions = searchConditionItems;    this.showingConditions = selectedSearchConditionItems;    this.bem = bem;    this.defaultValueMap = defaultValueMap;    setShowingConditionItems(showingConditions);    initComponents();    refresh();  }  public void setShowFlag(boolean pshowFlag) {    this.showFlag = pshowFlag;    this.showFlag = UserPreferencesUtil.getInstance().getBoolean("condition" + conditionId, this.showFlag);    if (showFlag) {      conditionItemPanel.setVisible(showFlag);      showControlbutton.setText("-");    } else {      conditionItemPanel.setVisible(showFlag);      showControlbutton.setText("+");    }  }  public AbstractSearchConditionArea() {  }  public void init(String conditionId, List<SearchCondition> searchConditionItems,  List<SearchCondition> selectedSearchConditionItems, BillElementMeta bem, Map defaultValueMap,  boolean showFlag, String numLimCompoId) {    this.init(conditionId, searchConditionItems, selectedSearchConditionItems, bem, defaultValueMap,    showFlag, numLimCompoId, true);  }  public void init(String conditionId, List<SearchCondition> searchConditionItems,  List<SearchCondition> selectedSearchConditionItems, BillElementMeta bem, Map defaultValueMap,  boolean showFlag, String numLimCompoId, boolean isRefresh) {    this.conditionId = conditionId;    this.allConditions = searchConditionItems;    this.showingConditions = selectedSearchConditionItems;    this.bem = bem;    this.isRefresh = isRefresh;    this.defaultValueMap = defaultValueMap;    this.numLimCompoId = numLimCompoId;    setShowingConditionItems(showingConditions);    initComponents();    this.setShowFlag(showFlag);    refresh();  }  /**   *   * @param conditionItems 所有收索条件项   * @param showConditionItems 要显示的搜索条件项   */  public void init(String conditionId, List<SearchCondition> conditionItems,  List<SearchCondition> showConditionItems) {    this.conditionId = conditionId;    this.allConditions = conditionItems;    this.showingConditions = showConditionItems;    setShowingConditionItems(showingConditions);    initComponents();    refresh();  }  protected void initPopupMenu() {    //popupMenu.add(new JMenuItem("设置默认值 "));    popupMenu.add(showSearchConditionSelectDialogMenuItem);    popupMenu.add(showTabSelectDialogMenuItem);  }  protected void initComponents() {    initShowControlPanel();    initPopupMenu();    setLayout(new BorderLayout());    conditionItemPanel = new JPanel(new GridBagLayout());    add(showControlPanel, BorderLayout.NORTH);    add(conditionItemPanel, BorderLayout.CENTER);    conditionItemPanel.addMouseListener(new MouseAdapter() {      public void mousePressed(MouseEvent e) {        if (e.isPopupTrigger()) {          showPopupMenu(e.getX(), e.getY());        }      }      public void mouseReleased(MouseEvent e) {        if (e.isPopupTrigger()) {          showPopupMenu(e.getX(), e.getY());        }      }    });    conditionItemPanel.setVisible(showFlag);    showControlbutton.setText("+");    //初始化分组条件框    //JLabel label = new JLabel("分组条件：");    //groupItemPanel.add(label);    GridBagLayout layout = new GridBagLayout();    GridBagConstraints c = new GridBagConstraints();    c.weightx = 1;    c.weighty = 1;    groupItemPanel = new JPanel(layout);    groupItemPanel.setBorder(BorderFactory.createTitledBorder("分组条件"));    //    for (int i = 0; i < 4; i++) {    //      ElementGroupConditionItem groupItem = new ElementGroupConditionItem(this);    //      groupItemPanel.add(groupItem);    //      if (i < 3) {    //        //groupItemPanel.add(new JLabel("+"));    //      }    //      groupItemList.add(groupItem);    //    }    for (int i = 0; i < 2; i++) {      for (int j = 0; j < 2; j++) {        c.gridx = i;        c.gridy = j;        ElementGroupConditionItem groupItem = new ElementGroupConditionItem(this);        layout.setConstraints(groupItem, c);        groupItemPanel.add(groupItem);        groupItemList.add(groupItem);      }    }    add(groupItemPanel, BorderLayout.SOUTH);    groupItemPanel.setVisible(false);  }  public void setGroupPanelVisiable(boolean value) {    this.showGroupPanel = value;    if (showFlag) {      this.groupItemPanel.setVisible(value);    }  }  protected boolean showFlag = false;  protected JButton showControlbutton = new JButton();  protected void initShowControlPanel() {    showControlbutton.setBorder(null);    showControlbutton.setCursor(new Cursor(Cursor.HAND_CURSOR));    showControlbutton.setToolTipText("显示或隐藏查询条件区");    showControlbutton.setPreferredSize(new Dimension(16, 12));    showControlPanel.add(showControlbutton, BorderLayout.EAST);    showControlbutton.setFocusable(false);    showControlbutton.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        if (showFlag) {          showFlag = false;          conditionItemPanel.setVisible(showFlag);          groupItemPanel.setVisible(showFlag);          showControlbutton.setText("+");        } else {          showFlag = true;          conditionItemPanel.setVisible(showFlag);          if (showGroupPanel) {            groupItemPanel.setVisible(showFlag);          }          showControlbutton.setText("-");        }        UserPreferencesUtil.getInstance().putBoolean("condition" + conditionId, showFlag);      }    });  }  /**   * 显示搜索条件选择对话框   */  public void showSearchConditionSelectDialog() {    final JListSelectDialog dlg = new JListSelectDialog(null, true, "设置搜索条件项") {      public void doOk() {        beforeResetConditionItem();        List rightObjects = getSelectedItem();        showingConditions = new ArrayList<SearchCondition>();        for (int i = 0; i < rightObjects.size(); i++) {          showingConditions.add((SearchCondition) rightObjects.get(i));        }        setShowingConditionItems(showingConditions);        saveSelectedConditionItems(showingConditions.toArray(new SearchCondition[showingConditions.size()]));        resetSearchConditionItem(showingConditionItems);        refresh();        this.close();        SwingUtilities.invokeLater(new Runnable() {          public void run() {            doSearch();          }        });        afterResetConditionItem();      }    };    dlg.setLeftList(allConditions);    dlg.setRightList(showingConditions);    dlg.setVisible(true);    tempOldList = new ArrayList<AbstractSearchConditionItem>();    tempOldList.addAll(showingConditionItems);  }  protected void afterResetConditionItem() {  }  protected void beforeResetConditionItem() {  }  List<AbstractSearchConditionItem> tempOldList = new ArrayList<AbstractSearchConditionItem>();  public void resetSearchConditionItem(List<AbstractSearchConditionItem> showingConditionItems) {    boolean resetflag = false;    for (AbstractSearchConditionItem item : tempOldList) {      if (!showingConditionItems.contains(item)) {        item.setValue(null);        resetflag = true;      }    }    if (resetflag) {      doSearch(tempOldList.toArray(new AbstractSearchConditionItem[tempOldList.size()]));    }  }  /**   * 刷新界面中显示的索引条件项   */  protected void refresh() {    SwingUtil.resetConditionItems(conditionItemPanel, this.showingConditionItems);    revalidate();    repaint();  }  /**   * 显示右键菜单   * @param x   * @param y   */  protected void showPopupMenu(int x, int y) {    showSearchConditionSelectDialogMenuItem.setVisible(allConditions.size() > 0);    showTabSelectDialogMenuItem.setVisible(dataDisplay != null && dataDisplay.getTableDisplays().length > 1);    popupMenu.show(this, x, y);  }  /**   * 保存正在显示的搜索条件项   * @param showingConditionItems   */  protected void saveSelectedConditionItems(SearchCondition[] showingConditionItems) {  }  /**   * 返回当前条件显示区所在的DataDisplay   * @return   */  public AbstractDataDisplay getDataDisplay() {    return dataDisplay;  }  /**   * 设置当前条件显示区所在的DataDisplay   * @param dataDisplay   */  void setDataDisplay(AbstractDataDisplay dataDisplay) {    this.dataDisplay = dataDisplay;  }  private Map<String, AbstractSearchConditionItem> showConditionItemsMap = new HashMap<String, AbstractSearchConditionItem>();  /**   * 显示条件区的AbstractSearchConditionItem   * @return Map<String, AbstractSearchConditionItem>   */  public AbstractSearchConditionItem getCondItemsByCondiFieldCode(String conditionFieldCode) {    return showConditionItemsMap.get(conditionFieldCode);  }  protected void setShowingConditionItems(List<SearchCondition> conditions) {    List<AbstractSearchConditionItem> list = SearchConditionUtil.toCompoment(conditions, bem,    defaultValueMap, numLimCompoId);    this.showConditionItemsMap.clear();    for (AbstractSearchConditionItem item : list) {      item.setSearchConditionArea(this);      this.showConditionItemsMap.put(item.getSearchCondition().getConditionFieldCode(), item);    }    this.showingConditionItems = list;  }  /**   * 返回正在显示的条件项   * @return   */  public AbstractSearchConditionItem[] getShowingConditionItems() {    return showingConditionItems.toArray(new AbstractSearchConditionItem[showingConditionItems.size()]);  }  /**   * 执行搜索   */  public void doSearch() {    doSearch(isRefresh);  }  public void doSearch(boolean isRefresh) {    if (isRefresh) {      doSearch(getShowingConditionItems());    }  }  protected void doSearch(AbstractSearchConditionItem[] searchConditionItems) {    if (dataDisplay == null) {      return;    }    if (dataDisplay.getTableDisplays().length > 0) {      TableDisplay activeTableDisplay = dataDisplay.getActiveTableDisplay();      if (activeTableDisplay != null) {        dataDisplay.handleTableDisplayActived(searchConditionItems, activeTableDisplay);      }    }  }  public JPopupMenu getPopupMenu() {    return popupMenu;  }  public void setPopupMenu(JPopupMenu popupMenu) {    this.popupMenu = popupMenu;  }  public void groupItemChanged() {  }}