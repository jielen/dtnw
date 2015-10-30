package com.ufgov.zc.client.zc.projectsupport;

import java.awt.Color;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.DefaultInvokeHandler;
import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbProjSupportToTableModelConverter;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.GkCommentUntreadDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.AuditPassButton;
import com.ufgov.zc.client.component.button.CallbackButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.OpenNotepadButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.SuggestAuditPassButton;
import com.ufgov.zc.client.component.button.TraceButton;
import com.ufgov.zc.client.component.button.UnauditButton;
import com.ufgov.zc.client.component.button.UntreadButton;
import com.ufgov.zc.client.component.button.zc.CommonButton;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.ConditionFieldConstants;
import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;
import com.ufgov.zc.client.util.ListUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.notepad.ZcNotepadDialog;
import com.ufgov.zc.client.zc.projectsupport.operation.ZcEbProjectSupportOpt;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DateUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcEbProjSupport;

/**

 * @ClassName: ZcEbProjectSupportListPanel

 * @Description: 招标投标情况

 * @date: Sep 19, 2012 4:07:58 PM

 * @version: V1.0

 * @since: 1.0

 * @author: yuzz

 * @modify:

 */
public class ZcEbProjectSupportListPanel extends AbstractEditListBill implements ParentWindowAware {

  private static final Logger logger = Logger.getLogger(ZcEbProjectSupportListPanel.class);

  public static final String compoId = "ZC_EB_PROJECT_SUPPORT";

  public static final String tabId = "ZcEbProjectSupport_Tab";

  public static final String tabSearch = "ZcEbProjectSupport_Search";

  public ZcEbProjectSupportListPanel self = this;

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private ElementConditionDto elementConditionDto = new ElementConditionDto();

  private AbstractSearchConditionArea topSearchConditionArea;

  private Window parentWindow = null;

  public static String titile = "项目投标情况";

  public FuncButton openNotepadButton = new OpenNotepadButton();

  // 工作流送审
  private SendButton sendButton = new SendButton();

  // 工作流审核通过
  private AuditPassButton auditPassButton = new AuditPassButton();

  private FuncButton suggestAuditPassButton = new SuggestAuditPassButton();

  // 工作流收回
  private FuncButton callbackButton = new CallbackButton();

  // 工作流退回
  private FuncButton unTreadButton = new UntreadButton();

  // 工作流销审
  private FuncButton unAuditButton = new UnauditButton();

  // 流程跟踪
  private FuncButton traceButton = new TraceButton();

  protected FuncButton sendxxzjButton = new CommonButton("fsendxxzj", "送信息处抽专家", "send.jpg");

  protected FuncButton sendxxjjButton = new CommonButton("fsendxxjj", "送信息处抽纪检", "send.jpg");

  //业务操作实例
  private ZcEbProjectSupportOpt opt = new ZcEbProjectSupportOpt();

  public ZcEbProjectSupportOpt getOpt() {
    return opt;
  }

  public ZcEbProjectSupportListPanel() {
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()
          .getCurrUserId(), tabId);
        return needDisplaySearchConditonList;
      }

      @Override
      public void success(List<SearchCondition> needDisplaySearchConditonList) {
        List<TableDisplay> showingDisplays = SearchConditionUtil.getNeedDisplayTableDisplay(needDisplaySearchConditonList);
        init(createDataDisplay(showingDisplays), null);
        revalidate();
        repaint();
      }
    });

    requestMeta.setCompoId(compoId);

  }

  public void refreshCurrentTabData() {

    topSearchConditionArea.doSearch();

  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {

    return new DataDisplay(SearchConditionUtil.getAllTableDisplay(tabId), showingDisplays, createTopConditionArea(), true);//true:显示收索条件区 false：不显示收索条件区

    //		return new DataDisplay(SearchConditionUtil.getAllTableDisplay(tabId), showingDisplays, null, false);//true:显示收索条件区 false：不显示收索条件区

  }

  private AbstractSearchConditionArea createTopConditionArea() {

    Map defaultValueMap = new HashMap();
    Date cDate = new Date();

    defaultValueMap.put(ConditionFieldConstants.START_DATE, DateUtil.dateToDdString(cDate));
    //    defaultValueMap.put(ConditionFieldConstants.END_DATE, DateUtil.dateToDdString(CalendarUtil.add(new Date(), Calendar.DATE, 5)));
    defaultValueMap.put(ConditionFieldConstants.END_DATE, DateUtil.dateToDdString(cDate));

    topSearchConditionArea = new SaveableSearchConditionArea(tabSearch, null, true, defaultValueMap, null);

    return topSearchConditionArea;

  }

  private final class DataDisplay extends MultiDataDisplay {

    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,
      boolean showConditionArea) {

      super(displays, showingDisplays, conditionArea, showConditionArea, ZcEbProjectSupportListPanel.tabId);

      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), titile, TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体",
        Font.BOLD, 15), Color.BLUE));
    }

    protected void preprocessShowingTableDisplay(List<TableDisplay> showingTableDisplays) {
      for (final TableDisplay d : showingTableDisplays) {
        final JGroupableTable table = d.getTable();
        table.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {

              String tabStatus = d.getStatus();
              JGroupableTable table = d.getTable();
              MyTableModel model = (MyTableModel) table.getModel();
              int row = table.getSelectedRow();

              List viewList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(model.getList(), table));

              if ("history".equals(tabStatus)) {

                new ZcEbProjectSupportHistoryEditDialog(self, viewList, row, tabStatus);

                return;
              }
              ZcEbProjSupport proj = (ZcEbProjSupport) viewList.get(row);

              if (!doCheckBeforePrint(proj))
                return;

              new ZcEbProjectSupportEditDialog(self, viewList, row, tabStatus);

            }
          }
        });
      }
    }

    /**
     * <p> 标签切换事件 </p>
     * @param searchConditionItems
     * @param tableDisplay
     * @see com.ufgov.zc.client.component.ui.AbstractDataDisplay#handleTableDisplayActived(com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem[], com.ufgov.zc.client.component.ui.TableDisplay)
     * @author yuzz
     * @since Sep 17, 2012 1:52:12 PM
     */
    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {

      elementConditionDto.setWfcompoId(ZcEbProjectSupportListPanel.compoId);
      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());
      elementConditionDto.setStatus(tableDisplay.getStatus());
      elementConditionDto.setUserId(WorkEnv.getInstance().getCurrUserId());
      elementConditionDto.setExecutor(WorkEnv.getInstance().getCurrUserId());

      for (AbstractSearchConditionItem item : searchConditionItems) {
        item.putToElementConditionDto(elementConditionDto);
      }

      final Container c = tableDisplay.getTable().getParent();
      UIUtilities.asyncInvoke(new DefaultInvokeHandler<TableModel>() {
        public void before() {
          assert c != null;
          installLoadingComponent(c);
        }

        public void after() {
          assert c != null;
          unInstallLoadingComponent(c);
          c.add(tableDisplay.getTable());
        }

        public TableModel execute() throws Exception {
          return fetchDataForProj();
        }

        public void success(TableModel model) {

          tableDisplay.setTableModel(model);

          setButtonStatus();

        }

      });

    }

  }

  /**
   * <p> 获取项目信息 </p>
   * @return TableModel
   * @author yuzz
   * @since Sep 17, 2012 1:49:53 PM
   */
  private TableModel fetchDataForProj() {
    if ("history".equals(elementConditionDto.getStatus())) {
      return ZcEbProjSupportToTableModelConverter.convertHistoryToTableModel(opt.getZcEbPackHistory(elementConditionDto, this.requestMeta));
    }
    List dataList = opt.getZcEbProjectSupport(elementConditionDto, this.requestMeta);
    return ZcEbProjSupportToTableModelConverter.convertToTableModel(dataList);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          UIManager.setLookAndFeel(new BlueLookAndFeel());
        } catch (Exception e) {
          e.printStackTrace();
        }
        ZcEbProjectSupportListPanel bill = new ZcEbProjectSupportListPanel();
        JFrame frame = new JFrame(titile);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }
    });
  }

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(compoId);
    toolBar.add(sendButton);
    toolBar.add(auditPassButton);
    toolBar.add(callbackButton);
    toolBar.add(unTreadButton);
    toolBar.add(unAuditButton);
    toolBar.add(traceButton);
    toolBar.add(sendxxjjButton);
    toolBar.add(sendxxzjButton);

    toolBar.add(printButton);
    toolBar.add(openNotepadButton);
    toolBar.add(helpButton);

    //		traceButton.setVisible(false);

    printButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doPrint();
      }
    });

    sendButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doSend();
      }
    });
    auditPassButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doAuditPass();
      }
    });
    suggestAuditPassButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        doSuggestAuditPass();
      }
    });
    callbackButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doCallback();
      }
    });
    unTreadButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doUnTread();
      }
    });
    unAuditButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doUnAudit();
      }
    });
    traceButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doTrace();
      }
    });

    openNotepadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doOpenNotepad();

      }

    });

    sendxxjjButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doExtrac(1);

      }

    });

    sendxxzjButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doExtrac(0);

      }

    });
  }

  public void doOpenNotepad() {

    List beanList = this.getCheckedList();

    if (beanList.size() != 1) {

      JOptionPane.showMessageDialog(this, "请选择一条数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    ZcEbProjSupport proj = (ZcEbProjSupport) beanList.get(0);

    new ZcNotepadDialog(proj.getSn(), parentWindow);

  }

  private boolean checkStatus(List beanList) {

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < beanList.size(); i++) {

      ZcEbProjSupport proj = (ZcEbProjSupport) beanList.get(i);

      if (!proj.getStatus().equals("0")) {

        sb.append(proj.getProjCode()).append("、");
      }
    }

    if (sb.length() > 0) {

      JOptionPane.showMessageDialog(this, "项目为：" + sb.subSequence(0, sb.length() - 1) + " 已送审，不能重复！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return false;
    }

    return true;
  }

  public void doSend() {

    boolean success = true;

    String errorInfo = "";

    requestMeta.setFuncId(this.sendButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    if (!checkStatus(beanList))
      return;

    try {
      for (int i = 0; i < beanList.size(); i++) {

        ZcEbProjSupport proj = (ZcEbProjSupport) beanList.get(i);

        opt.newCommitFN(proj, requestMeta);

      }
    } catch (Exception e) {

      success = false;

      JOptionPane.showMessageDialog(this, "送审失败! \n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.refreshCurrentTabData();

    }
  }

  private void doExtrac(int type) {

    boolean success = true;

    requestMeta.setFuncId(this.sendButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    String info = "送信息处抽专家";
    if (type == 1) {
      info = "送信息处抽纪检";
    } else if (type == 2) {
      info = "完成抽专家";
    } else if (type == 3) {
      info = "完成抽纪检";
    }

    Date now = (Date) opt.getBaseService().queryObject("ZcEbPlan.getSysdate", null, requestMeta);

    try {
      StringBuffer err = new StringBuffer();
      for (int i = 0; i < beanList.size(); i++) {
        ZcEbProjSupport proj = (ZcEbProjSupport) beanList.get(i);

        if (now.compareTo(proj.getBidEndTime()) < 0) {
          err.append(proj.getProjCode() + ":" + "请在投标截止时间之后，送信息处抽纪检、专家 。\n");
        }
        if (type == 1) {
          Calendar c = Calendar.getInstance();
          c.setTime(proj.getOpenBidTime());
          c.add(Calendar.DATE, -3);
          Date later = c.getTime();
          if (now.compareTo(later) > 0) {
            err.append(proj.getProjCode() + ":" + "必须在开标截止时间三天，送信息处抽纪检。\n");
          }
        }

      }
      if (err.length() > 0) {

        JOptionPane.showMessageDialog(this, err.toString(), " 提示", JOptionPane.INFORMATION_MESSAGE);

        return;
      }

      err = new StringBuffer();
      StringBuffer projCode = new StringBuffer("' '");

      for (int i = 0; i < beanList.size(); i++) {

        ZcEbProjSupport proj = (ZcEbProjSupport) beanList.get(i);

        if (proj.getIsExtrac() != null && proj.getIsExtrac().length() == 2) {
          if (type == 0 && (proj.getIsExtrac().startsWith("1") || proj.getIsExtrac().startsWith("2"))) {
            err.append(proj.getProjCode()).append("\n");
          } else if (type == 1 && (proj.getIsExtrac().endsWith("1") || proj.getIsExtrac().endsWith("2"))) {
            err.append(proj.getProjCode()).append("\n");
          }
        }

        projCode.append(",'").append(proj.getProjCode()).append("'");
      }

      if (err.length() > 0) {
        err.append("不能重复").append(info);

        JOptionPane.showMessageDialog(this, "项目:\n" + err.toString(), " 提示", JOptionPane.INFORMATION_MESSAGE);

        return;
      }

      List<Map> list = opt.getBaseService().queryDataForList("ZcEbBulletin.selectProjBullCount", projCode.toString(), requestMeta);
      for (int i = 0; i < list.size(); i++) {
        Map map = list.get(i);
        if ("0".equals(map.get("BUL_NUM").toString())) {
          err.append(map.get("PROJ_CODE").toString()).append("\n");
        }
      }

      if (err.length() > 0) {
        err.append("未发布采购公告，不能").append(info);

        JOptionPane.showMessageDialog(this, "项目:\n" + err.toString(), " 提示", JOptionPane.INFORMATION_MESSAGE);

        return;
      }

      int num = JOptionPane.showConfirmDialog(this, "确认" + info + "？", "确认", 0);

      if (num != JOptionPane.YES_OPTION) {
        return;
      }

      for (int i = 0; i < beanList.size(); i++) {

        ZcEbProjSupport proj = (ZcEbProjSupport) beanList.get(i);

        String isExtrac = "";
        if (proj.getIsExtrac() == null || "".equals(proj.getIsExtrac())) {
          isExtrac = "00";
        } else {
          isExtrac = proj.getIsExtrac();
        }
        if (type == 0) {
          isExtrac = "1" + isExtrac.charAt(1);
        } else if (type == 1) {
          isExtrac = isExtrac.charAt(0) + "1";
        } else if (type == 2) {
          isExtrac = "2" + isExtrac.charAt(1);
        } else if (type == 3) {
          isExtrac = isExtrac.charAt(0) + "2";
        }
        proj.setIsExtrac(isExtrac);
      }

      opt.getBaseService().updateObjectList("ZcEbBulletin.updateIsExtracByProj", beanList, requestMeta);
    } catch (Exception e) {

      success = false;

      JOptionPane.showMessageDialog(this, info + "失败! \n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

    }

    if (success) {

      JOptionPane.showMessageDialog(this, info + "成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

    }

    this.refreshCurrentTabData();

  }

  public void doAuditPass() {
    boolean success = true;

    String errorInfo = "";

    requestMeta.setFuncId(this.auditPassButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    try {
      for (int i = 0; i < beanList.size(); i++) {

        ZcEbProjSupport proj = (ZcEbProjSupport) beanList.get(i);

        proj.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        proj.setComment(commentDialog.getComment());

        opt.auditFN(proj, requestMeta);

      }
    } catch (Exception e) {

      success = false;

      JOptionPane.showMessageDialog(this, "审核失败! \n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.refreshCurrentTabData();

    }
  }

  public void doSuggestAuditPass() {

  }

  public void doCallback() {

    boolean success = true;

    requestMeta.setFuncId(this.callbackButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    GkCommentUntreadDialog commentDialog = new GkCommentUntreadDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      ModalityType.APPLICATION_MODAL);
    if (commentDialog.cancel) {
      return;
    }

    try {
      for (int i = 0; i < beanList.size(); i++) {

        ZcEbProjSupport proj = (ZcEbProjSupport) beanList.get(i);

        proj.setComment(commentDialog.getComment());

        opt.callbackFN(proj, requestMeta);

      }
    } catch (Exception e) {

      success = false;

      JOptionPane.showMessageDialog(this, "收回失败! \n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.refreshCurrentTabData();

    }

  }

  public void doUnTread() {

    boolean success = true;

    requestMeta.setFuncId(this.unTreadButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    GkCommentUntreadDialog commentDialog = new GkCommentUntreadDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      ModalityType.APPLICATION_MODAL);
    if (commentDialog.cancel) {
      return;
    }

    try {
      for (int i = 0; i < beanList.size(); i++) {

        ZcEbProjSupport proj = (ZcEbProjSupport) beanList.get(i);

        proj.setComment(commentDialog.getComment());

        opt.untreadFN(proj, requestMeta);

      }
    } catch (Exception e) {

      success = false;

      JOptionPane.showMessageDialog(this, "退回失败! \n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.refreshCurrentTabData();

    }
  }

  public void doUnAudit() {

    boolean success = true;

    requestMeta.setFuncId(this.unAuditButton.getFuncId());

    List beanList = this.getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    GkCommentUntreadDialog commentDialog = new GkCommentUntreadDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      ModalityType.APPLICATION_MODAL);
    if (commentDialog.cancel) {
      return;
    }

    try {
      for (int i = 0; i < beanList.size(); i++) {

        ZcEbProjSupport proj = (ZcEbProjSupport) beanList.get(i);

        proj.setComment(commentDialog.getComment());

        opt.unauditFN(proj, requestMeta);

      }
    } catch (Exception e) {

      success = false;

      JOptionPane.showMessageDialog(this, "撤销失败! \n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "撤销成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.refreshCurrentTabData();

    }

  }

  public void doTrace() {
    ZcUtil.showTraceDialog(getCheckedList(), this);
  }

  public void doPrint() {
    List printList = getCheckedList();

    if (printList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    } else {
      if (printList.size() > 1) {
        JOptionPane.showMessageDialog(this, "只能选择一条数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
    }

    requestMeta.setFuncId(this.printButton.getFuncId());

    requestMeta.setPageType(this.compoId + "_L");

    boolean success = true;

    try {

      ZcEbProjSupport proj = (ZcEbProjSupport) printList.get(0);

      if (!doCheckBeforePrint(proj))
        return;

      DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

      String condition = "PROJCODE='" + proj.getProjCode() + "' and to_char(OPEN_BID_TIME,'yyyymmddhh24miss')='" + df.format(proj.getOpenBidTime())
        + "'";

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "打印出错！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

    }
  }

  public void doPrintH() {
    List printList = getCheckedList();

    if (printList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    } else {
      if (printList.size() > 1) {
        JOptionPane.showMessageDialog(this, "只能选择一条数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
    }

    requestMeta.setFuncId(this.printButton.getFuncId());

    requestMeta.setPageType(this.compoId + "_L");

    boolean success = true;

    try {

      ZcEbProjSupport proj = (ZcEbProjSupport) printList.get(0);

      Map<String, String> map = new HashMap<String, String>();
      map.put("PROJ_CODE", proj.getProjCode());
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      map.put("OPEN_DATE", df.format(proj.getOpenBidTime()));

      String selPack = null;
      List<String> pack = opt.getBaseService().queryDataForList("ZcEbBulletin.selectDlyBidPackInfo", map, requestMeta);
      if (pack == null || pack.size() == 0) {
        JOptionPane.showMessageDialog(this, "该分包没有投标历史信息，不能打印历史数据", "信息", JOptionPane.INFORMATION_MESSAGE);
        return;
      } else if (pack.size() > 1) {
        selPack = (String) JOptionPane.showInputDialog(this, "", "选择分包", JOptionPane.INFORMATION_MESSAGE, null, pack.toArray(), pack.get(0));
      } else {
        selPack = pack.get(0);
      }

      map.put("PACK_CODE", selPack);
      List<String> open = opt.getBaseService().queryDataForList("ZcEbBulletin.selectDlyBidInfo", map, requestMeta);
      if (open == null || open.size() == 0) {
        JOptionPane.showMessageDialog(this, "该分包没有做延期公告，不能打印历史数据", "信息", JOptionPane.INFORMATION_MESSAGE);
        return;
      }

      String selDate = null;
      if (open.size() > 1) {
        selDate = (String) JOptionPane.showInputDialog(this, "", "选择历史开标时间", JOptionPane.INFORMATION_MESSAGE, null, open.toArray(), open.get(0));
      } else {
        selDate = open.get(0);
      }

      String condition = "PROJ_CODE='" + proj.getProjCode() + "' and to_char(OPEN_BID_TIME,'yyyy-mm-dd hh24:mi:ss')='" + selDate
        + "' and pack_name='" + selPack + "'";

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "打印出错！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);

    }
  }

  /**
   * <p> 打印前校验 </p>
   * @param proj
   * @return 
   * @return boolean
   * @author yuzz
   * @since Sep 24, 2012 1:12:15 PM
   */
  public boolean doCheckBeforePrint(ZcEbProjSupport proj) {
    Date curDate = ZcUtil.getServerSysDate(requestMeta);
    Date openDate = proj.getOpenBidTime();
    String sPeriod = AsOptionMeta.getOptVal(ZcSettingConstants.OPT_ZC_PROJ_SUP_PRINT_PERIOD);
    int period = 0;

    if (topDataDisplay != null && topDataDisplay.getActiveTableDisplay() != null) {
      String tabId = topDataDisplay.getActiveTableDisplay().getStatus();
      if (WFConstants.EDIT_TAB_STATUS_EXEC.equalsIgnoreCase(tabId)) {
        return true;
      }
    }

    if (sPeriod == null || "".equals(sPeriod)) {
      JOptionPane.showMessageDialog(this, "未设置开标前允许打印时间！", "信息", JOptionPane.INFORMATION_MESSAGE);
      return false;
    }
    period = Integer.parseInt(sPeriod);
    boolean isAllow = false;
    if (openDate != null) {
      long t = openDate.getTime() - curDate.getTime();
      isAllow = (t <= period * ZcSettingConstants.TIME_MM_PER_MIN);
    } else {
      isAllow = false;
    }
    if (!isAllow) {
      JOptionPane.showMessageDialog(this, "未到开标时间，不允许打印（查看）信息！", "信息", JOptionPane.INFORMATION_MESSAGE);
      return false;
    }

    return true;
  }

  public List getCheckedList() {

    List beanList = new ArrayList();

    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();

    MyTableModel model = (MyTableModel) table.getModel();

    List list = model.getList();

    Integer[] checkedRows = table.getCheckedRows();

    for (Integer checkedRow : checkedRows) {

      int accordDataRow = table.convertRowIndexToModel(checkedRow);

      ZcEbProjSupport bean = (ZcEbProjSupport) list.get(accordDataRow);

      beanList.add(bean);

    }

    return beanList;

  }

  private void setButtonStatus() {
    String panelId = WFConstants.EDIT_TAB_STATUS_DRAFT;
    if (topDataDisplay != null && topDataDisplay.getActiveTableDisplay() != null) {
      panelId = topDataDisplay.getActiveTableDisplay().getStatus();
    }
    if (WFConstants.AUDIT_TAB_STATUS_TODO.equalsIgnoreCase(panelId)) {
      auditPassButton.setVisible(true);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(true);
      sendButton.setVisible(true);
      isSendToNextButton.setVisible(true);
      printButton.setVisible(true);
      traceButton.setVisible(true);
      sendxxjjButton.setVisible(false);
      sendxxzjButton.setVisible(false);
    } else if (WFConstants.AUDIT_TAB_STATUS_DONE.equalsIgnoreCase(panelId)) {
      auditPassButton.setVisible(false);
      isSendToNextButton.setVisible(false);
      unAuditButton.setVisible(true);
      unTreadButton.setVisible(false);
      sendButton.setVisible(false);
      printButton.setVisible(false);
      traceButton.setVisible(true);
      sendxxjjButton.setVisible(false);
      sendxxzjButton.setVisible(false);
    } else if (WFConstants.AUDIT_TAB_STATUS_ALL.equalsIgnoreCase(panelId)) {
      auditPassButton.setVisible(false);
      isSendToNextButton.setVisible(false);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(false);
      sendButton.setVisible(false);
      printButton.setVisible(false);
      traceButton.setVisible(false);
      sendxxjjButton.setVisible(false);
      sendxxzjButton.setVisible(false);
    } else if (WFConstants.EDIT_TAB_STATUS_DRAFT.equalsIgnoreCase(panelId)) {
      auditPassButton.setVisible(false);
      isSendToNextButton.setVisible(false);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(false);
      sendButton.setVisible(true);
      printButton.setVisible(true);
      traceButton.setVisible(false);
      sendxxjjButton.setVisible(true);
      sendxxzjButton.setVisible(true);
    } else if (WFConstants.EDIT_TAB_STATUS_EXEC.equalsIgnoreCase(panelId)) {
      auditPassButton.setVisible(false);
      isSendToNextButton.setVisible(false);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(false);
      sendButton.setVisible(false);
      printButton.setVisible(true);
      traceButton.setVisible(true);
      sendxxjjButton.setVisible(false);
      sendxxzjButton.setVisible(false);
    } else if (WFConstants.EDIT_TAB_STATUS_UNTREAD.equalsIgnoreCase(panelId)) {
      auditPassButton.setVisible(true);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(true);
      sendButton.setVisible(true);
      isSendToNextButton.setVisible(true);
      printButton.setVisible(false);
      traceButton.setVisible(true);
      sendxxjjButton.setVisible(false);
      sendxxzjButton.setVisible(false);
    } else {
      auditPassButton.setVisible(false);
      isSendToNextButton.setVisible(false);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(false);
      sendButton.setVisible(true);
      printButton.setVisible(true);
      traceButton.setVisible(false);
      sendxxjjButton.setVisible(false);
      sendxxzjButton.setVisible(false);
    }

  }

  public RequestMeta getRequestMeta() {
    return requestMeta;
  }

  public Window getParentWindow() {

    return parentWindow;
  }

  public void setParentWindow(Window parentWindow) {
    this.parentWindow = parentWindow;
  }

  static {
    LangTransMeta.init("ZC%");
  }
}
