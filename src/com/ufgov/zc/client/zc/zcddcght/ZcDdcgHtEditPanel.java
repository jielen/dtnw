package com.ufgov.zc.client.zc.zcddcght;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.ZcWorkFlowAdapter;
import com.ufgov.zc.client.common.converter.zc.ZcDdcgHtToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AgreeButton;
import com.ufgov.zc.client.component.button.AuditPassButton;
import com.ufgov.zc.client.component.button.CallbackButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.DisagreeButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.IsSendToNextButton;
import com.ufgov.zc.client.component.button.OpenNotepadButton;
import com.ufgov.zc.client.component.button.PrintButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SaveSendButton;
import com.ufgov.zc.client.component.button.SelectMoldButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.SendToXieBanButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.button.SuggestAuditPassButton;
import com.ufgov.zc.client.component.button.TraceButton;
import com.ufgov.zc.client.component.button.UnauditButton;
import com.ufgov.zc.client.component.button.UntreadButton;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.zc.WordFileUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.ztb.activex.WordPane;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.model.AsFile;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDdcgHt;
import com.ufgov.zc.common.zc.model.ZcDdcghtItem;
import com.ufgov.zc.common.zc.model.ZcEbBulletinWordMold;
import com.ufgov.zc.common.zc.model.ZcEbSignupPackDetail;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;
import com.ufgov.zc.common.zc.model.ZcTBchtItem;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.publish.IZcDdcgHtServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;

/**

 * 

 * @author Administrator

 *
 */

public class ZcDdcgHtEditPanel extends AbstractMainSubEditPanel {

  public static final String PATH = ZcUtil.dir + "ht/";

  private static final long serialVersionUID = -4923939712990784916L;

  private static final Logger logger = Logger.getLogger(ZcDdcgHtEditPanel.class);

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private JSaveableSplitPane myWordPane;

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

//  private String compoId = "ZC_DDCG_HT";

  protected FuncButton saveButton = new SaveButton();

  protected FuncButton editButton = new EditButton();

  private FuncButton traceButton = new TraceButton();

  protected FuncButton callbackButton = new CallbackButton();

  protected FuncButton deleteButton = new DeleteButton();

  private FuncButton exitButton = new ExitButton();

  public FuncButton openNotepadButton = new OpenNotepadButton();

  private FuncButton saveAndSendButton = new SaveSendButton();

  protected FuncButton sendButton = new SendButton();

  public FuncButton isSendToNextButton = new IsSendToNextButton();

  //同意

  private FuncButton agreeButton = new AgreeButton();

  //不同意
  private FuncButton disagreeButton = new DisagreeButton();

  //送协办人审核

  private FuncButton sendToXieBanButton = new SendToXieBanButton();

  public FuncButton printButton = new PrintButton();

  // 工作流填写意见审核通过
  protected FuncButton suggestPassButton = new SuggestAuditPassButton();

  // 工作流审核通过
  protected FuncButton auditPassButton = new AuditPassButton();

  // 工作流销审
  protected FuncButton unAuditButton = new UnauditButton();

  // 工作流退回
  protected FuncButton unTreadButton = new UntreadButton();

  //选择合同模板。
  protected SelectMoldButton selectMoldButton = new SelectMoldButton();

  protected ListCursor<ZcDdcgHt> listCursor;

  private ZcDdcgHt oldZcDdcgHt;

  private byte[] oldHtWord;

  private ZcDdcgHtListPanel listPanel;

  protected JTablePanel itemTablePanel = new JTablePanel();

  protected ZcDdcgHtEditPanel self = this;

  protected GkBaseDialog parent;

  private ElementConditionDto dtoForBidWinner = new ElementConditionDto();

  private BillElementMeta mainBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_DDCG_HT");

  protected IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  public IZcDdcgHtServiceDelegate zcDdcgHtServiceDelegate = (IZcDdcgHtServiceDelegate) ServiceFactory.create(IZcDdcgHtServiceDelegate.class,

  "zcDdcgHtServiceDelegate");

  protected WordPane wordPane = new WordPane();;

  private AsFile file = new AsFile();

  private String wordFileId;

  protected boolean isCancel = false;

  protected ElementConditionDto getDto = new ElementConditionDto();

  protected ElementConditionDto moldDto = new ElementConditionDto();

  String moldColumNames[] = { "模板编号", "模板名称", "公告模板类型", "状态", "备注" };

  ZcEbWordMoldFnHandler handlera = new ZcEbWordMoldFnHandler(moldColumNames);

  private ForeignEntityFieldEditor fieldMoldName = new ForeignEntityFieldEditor("ZcEbBulletinWordMold.getZcGongHuoHtWordMold", moldDto, 20, handlera,

  moldColumNames, "载入模板", "moldName");

  public ZcDdcgHtEditPanel(GkBaseDialog parent, ListCursor<ZcDdcgHt> listCursor, String tabStatus, ZcDdcgHtListPanel listPanel, String compoId) {

    super(ZcDdcgHt.class, BillElementMeta.getBillElementMetaWithoutNd(compoId));

    this.listCursor = listCursor;

    this.listPanel = listPanel;

    this.parent = parent;

    this.colCount = 3;

    init();

    requestMeta.setCompoId(getCompoId());

    wordPane.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {

        //打开文件完成之后的回调函数
        boolean isSuccess = (Boolean) evt.getNewValue();
        if (isSuccess) {
          //下面一句是为了打开word后刷新窗口
          self.parent.setSize(self.parent.getSize().width - 1, self.parent.getSize().height - 1);
        }
      }
    });
    moldDto.setType("HTDD");
    refreshData();

  }

  protected void init() {

    this.initToolBar(toolBar);

    this.setLayout(new BorderLayout());

    this.add(toolBar, BorderLayout.NORTH);

    initFieldEditorPanel(ZcDdcgHt.class, getMainBillElementMeta());

    JComponent tabTable = createSubBillPanel();

    fieldEditorPanel.setMinimumSize(new Dimension(540, 150));

    tabTable.setMinimumSize(new Dimension(240, 300));

    myWordPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, fieldEditorPanel, tabTable);

    myWordPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "采购合同", TitledBorder.CENTER, TitledBorder.TOP,

    new Font("宋体", Font.BOLD, 15), Color.BLUE));

    myWordPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation_top", 100);

    myWordPane.setContinuousLayout(true);

    myWordPane.setOneTouchExpandable(true);

    myWordPane.setDividerSize(5);

    myWordPane.setDividerLocation(200);

    myWordPane.setBackground(self.getBackground());

    this.add(myWordPane, BorderLayout.CENTER);

  }

  /**
   * 
  * 刷新word文本
   */
  protected void refreshSubTableData(String fileID) {
    if (wordPane != null) {
      wordPane.close();
    }
    WordFileUtil.setDir("ht");

    wordFileId = fileID;

    final String moldFileName = WordFileUtil.loadMold(fileID);

    file = new AsFile();

    file.setFileName(moldFileName);

    if (moldFileName == null || moldFileName.length() == 0) { //没有成功载入模板，关闭wordPane  

      if (wordPane != null)

        wordPane.close();
    }

    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        wordPane.openAndProtect(moldFileName, ZcSettingConstants.WORD_PASSWORD);

      }
    });

  }

  protected JTablePanel[] getSubTables() {

    return new JTablePanel[] { itemTablePanel };

  }

  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    String columNames[] = { "项目代码", "采购项目", "分包名称", "采购方式" };

    ZcEbProjFnHandler projHandler = new ZcEbProjFnHandler(columNames);

    ElementConditionDto elementCondtiontDto = new ElementConditionDto();

    elementCondtiontDto.setBillStatus("exec");

    elementCondtiontDto.setUserId(requestMeta.getSvUserID());

    ForeignEntityFieldEditor zcMakeCode = new ForeignEntityFieldEditor(getProjectSqlId(), elementCondtiontDto, 20, projHandler,

    columNames, "分包名称", "zcBidContent");

    TextFieldEditor zcMakeName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME), "zcMakeName");

    AutoNumFieldEditor zcHtCode = new AutoNumFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_CODE), "zcHtCode");

    TextFieldEditor zcHtName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NAME), "zcHtName");

    /**
     * 供应商的外部实体
     */

    String columSuNames[] = { "供应商名称", "供应商编号", "供应商联系人", "地址" };

    ZcEbWinBidSupplierHandler suHandler = new ZcEbWinBidSupplierHandler(columSuNames);

    ForeignEntityFieldEditor zcSuName = new ForeignEntityFieldEditor("ZC_DDCG_HT._selectDdcgWinProvider", dtoForBidWinner, 20, suHandler,

    columNames, "中标供应商", "zcSuName");

    FileFieldEditor tbylbFile = new FileFieldEditor("定点报价表", "tbylbFileName", "tbylbFileId");

    DateFieldEditor zcSgnDate = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SGN_DATE), "zcSgnDate");
    TextFieldEditor zcMemo = new TextFieldEditor("备注信息", "zcMemo");
    DateFieldEditor zcInputDate = new DateFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_DATE), "executeDate");
    editorList.add(zcMakeCode);
    editorList.add(zcMakeName);
    editorList.add(zcHtName);
    editorList.add(zcHtCode);
    editorList.add(zcSuName);
    editorList.add(tbylbFile);
    editorList.add(zcSgnDate);
    editorList.add(zcInputDate);
    editorList.add(zcMemo);
    return editorList;

  }

  public JComponent createSubBillPanel() {

    JTabbedPane topTabPane = new JTabbedPane();

    JTabbedPane itemTabPane = new JTabbedPane();

    itemTablePanel.init();

    itemTablePanel.setPanelId(this.getClass().getName() + "_itemTablePanel");

    itemTablePanel.getSearchBar().setVisible(false);

    itemTablePanel.setTablePreferencesKey(this.getClass().getName() + "_itemTable");

    itemTablePanel.getTable().setShowCheckedColumn(true);

    itemTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    itemTabPane.addTab("商品构成", itemTablePanel);

    JFuncToolBar bottomToolBar2 = new JFuncToolBar();

    FuncButton addBtn2 = new SubaddButton(false);

    JButton insertBtn2 = new SubinsertButton(false);

    JButton delBtn2 = new SubdelButton(false);

    bottomToolBar2.add(addBtn2);

    bottomToolBar2.add(insertBtn2);

    bottomToolBar2.add(delBtn2);

    addBtn2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcTBchtItem zcTBchtItem = new ZcTBchtItem();

        zcTBchtItem.setTempId(Guid.genID());

        int rowNum = addSub(itemTablePanel, zcTBchtItem);

        itemTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcTBchtItem zcTBchtItem = new ZcTBchtItem();

        zcTBchtItem.setTempId(Guid.genID());

        int rowNum = insertSub(itemTablePanel, zcTBchtItem);

        itemTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        deleteSub(itemTablePanel);

      }

    });

    itemTabPane.setMinimumSize(new Dimension(240, 300));

    topTabPane.addTab("合同文本", wordPane);

    return topTabPane;

  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(getCompoId());

    toolBar.add(editButton);

    toolBar.add(selectMoldButton);

    toolBar.add(saveButton);

    toolBar.add(sendButton);

    toolBar.add(suggestPassButton);

    toolBar.add(auditPassButton);

    toolBar.add(unAuditButton);

    toolBar.add(unTreadButton);

    toolBar.add(callbackButton);

    toolBar.add(deleteButton);

    toolBar.add(printButton);

    toolBar.add(traceButton);

    toolBar.add(exitButton);

    editButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doEdit();

      }

    });

    sendButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSend();

      }

    });

    exitButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doExit();

      }

    });

    saveButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doSave();

      }

    });

    deleteButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doDelete();

      }

    });

    traceButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doTrace();

      }

    });

    callbackButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doCallback();

      }

    });

    auditPassButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        stopTableEditing();

        // 审核

        doAudit();

      }

    });

    suggestPassButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        // 填写意见审核

        doSuggestPass();

      }

    });

    unAuditButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 销审

        doUnAudit();

      }

    });

    unTreadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 退回

        doUnTread();

      }

    });

    printButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doPrintButton();

      }

    });

    openNotepadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doOpenNotepad();

      }

    });

    selectMoldButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSelectMoldButton();

      }

    });

  }

  private void doOpenNotepad() {

    ZcDdcgHt sheet = (ZcDdcgHt) this.listCursor.getCurrentObject();

  }

  private void doPrintButton() {

    try {

      this.wordPane.print();

    } catch (RuntimeException e) {

      e.printStackTrace();

      JOptionPane.showMessageDialog(this, "请先切换至合同文本页签，并确认合同文本加载完毕，再进行打印！", "提示", JOptionPane.INFORMATION_MESSAGE);

    }

  }

  protected void updateFieldEditorsEditable() {
    super.updateFieldEditors();
    JTablePanel[] subs = this.getSubTables();
    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {
      for (AbstractFieldEditor fd : this.fieldEditors) {
        if ("zcBidContent_zcSuName_zcMemo_zcSgnDate".indexOf(fd.getFieldName()) != -1) {
          fd.setEnabled(true);
        } else {
          fd.setEnabled(false);
        }
      }
    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        fd.setEnabled(false);
      }

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {
        if ("zcBidContent_zcSuName_zcMemo_zcSgnDate".indexOf(fd.getFieldName()) != -1) {
          fd.setEnabled(true);
        } else {
          fd.setEnabled(false);
        }
      }
    }
    for (JTablePanel tablePanel : subs) {
      setWFSubTableEditable(tablePanel, false);
    }
  }

  private void doEdit() {
    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
    updateFieldEditorsEditable();
  }

  protected void doSend() {
    if (checkBeforeSave()) {
      return;
    }

    if (isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    ZcDdcgHt bal = (ZcDdcgHt) this.listCursor.getCurrentObject();
    ZcBaseBill afterBill = ZcWorkFlowAdapter.newCommitFN(bal, this, sendButton, requestMeta, null);
    if (afterBill != null) {
      ZcDdcgHt afterSaveBill = zcDdcgHtServiceDelegate.selectByPrimaryKey(bal.getZcHtCode(), requestMeta);
      this.refreshAll(afterSaveBill, true);
      this.listPanel.refreshCurrentTabData();
    }
  }

  protected void doSuggestPass() {

    if (checkBeforeSave()) {
      return;
    }

    if (isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    ZcDdcgHt bal = (ZcDdcgHt) this.listCursor.getCurrentObject();
    ZcBaseBill afterBill = ZcWorkFlowAdapter.auditFN(bal, this, sendButton, requestMeta, null);
    if (afterBill != null) {
      ZcDdcgHt afterSaveBill = zcDdcgHtServiceDelegate.selectByPrimaryKey(bal.getZcHtCode(), requestMeta);
      this.refreshAll(afterSaveBill, true);
      this.listPanel.refreshCurrentTabData();
    }

  }

  protected void doCallback() {

    if (checkBeforeSave()) {
      return;
    }

    if (isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    ZcDdcgHt bal = (ZcDdcgHt) this.listCursor.getCurrentObject();
    ZcBaseBill afterBill = ZcWorkFlowAdapter.callbackFN(bal, this, sendButton, requestMeta, null);
    if (afterBill != null) {
      ZcDdcgHt afterSaveBill = zcDdcgHtServiceDelegate.selectByPrimaryKey(bal.getZcHtCode(), requestMeta);
      this.refreshAll(afterSaveBill, true);
      this.listPanel.refreshCurrentTabData();
    }

  }

  public void doExit() {
    if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;
        }

      }

    }
    if (this.wordPane != null && wordPane.isDocOpened()) {
      wordPane.closeNotSave();
    }
    this.parent.dispose();
  }

  protected void refreshAll(ZcDdcgHt afterSaveBill, boolean isRefreshButton) {

    this.listCursor.setCurrentObject(afterSaveBill);

    refreshData();

    if (isRefreshButton) {

      setButtonStatus(afterSaveBill, requestMeta, this.listCursor);

    }

  }

  /**

   * 保存前校验

   * @param cpApply

   * @return

   */

  protected boolean checkBeforeSave() {

    List mainNotNullList = getMainBillElementMeta().getNotNullBillElement();

    ZcDdcgHt zcDdcgHt = (ZcDdcgHt) this.listCursor.getCurrentObject();

    StringBuilder errorInfo = new StringBuilder();

    String mainValidateInfo = ZcUtil.validateBillElementNull(zcDdcgHt, mainNotNullList);

    if (mainValidateInfo.length() != 0) {

      errorInfo.append("定点合同：\n").append(mainValidateInfo.toString()).append("\n");

    }

    if (errorInfo.length() != 0) {

      JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);

      return true;

    }
    return false;
  }

  public boolean doSave() {
    if (!isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      selectMoldButton.setEnabled(true);
      this.editButton.setEnabled(true);
      return true;
    }

    if (checkBeforeSave()) {
      return false;
    }
    boolean success = true;

    String errorInfo = "";
    try {
      requestMeta.setFuncId(saveButton.getFuncId());
      ZcDdcgHt inData = (ZcDdcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
      wordPane.save();
      String fileId = saveBulletinContent(inData.getZcConText());
      if (fileId != null && !"".equals(fileId)) {
        inData.setZcConTextBlobid(fileId);
      }
      ZcDdcgHt zcXmcgHt = zcDdcgHtServiceDelegate.updateZcDdcgHtFN(inData, this.requestMeta);
      listCursor.setCurrentObject(zcXmcgHt);

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshData();
      this.listPanel.refreshCurrentTabData();
      selectMoldButton.setEnabled(true);
      this.editButton.setEnabled(true);

    } else {
      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
    return true;
  }

  protected void doDelete() {

    requestMeta.setFuncId(deleteButton.getFuncId());

    ZcDdcgHt zcDdcgHt = (ZcDdcgHt) this.listCursor.getCurrentObject();

    if (zcDdcgHt.getZcHtCode() == null || "".equalsIgnoreCase(zcDdcgHt.getZcHtCode())) {

      JOptionPane.showMessageDialog(this, "尚未保存到数据库，无需删除！", "提示", JOptionPane.ERROR_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      boolean success = true;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(deleteButton.getFuncId());

        zcDdcgHtServiceDelegate.deleteByPrimaryKeyFN(zcDdcgHt, this.requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();
      }
      if (success) {

        this.listCursor.removeCurrentObject();

        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.refreshData();

        this.listPanel.refreshCurrentTabData();

      } else {
        JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void stopTableEditing() {
  }

  protected String saveBulletinContent(String fileName) {

    String fileID = "";

    if (fileName != null && fileName.length() != 0)

      fileID = WordFileUtil.uploadBulletinWordConstent(fileName);

    return fileID;

  }

  public boolean isDataChanged() {

    stopTableEditing();

    if (!saveButton.isVisible() || !saveButton.isEnabled()) {
      return false;
    }

    return !DigestUtil.digest(getOldZcDdcgHt()).equals(DigestUtil.digest(listCursor.getCurrentObject()));

  }

  public ZcDdcgHt getOldZcDdcgHt() {
    return oldZcDdcgHt;
  }

  public void setOldZcDdcgHt(ZcDdcgHt oldZcDdcgHt) {
    this.oldZcDdcgHt = oldZcDdcgHt;
  }

  protected void refreshData() {
    ZcDdcgHt zcDdcgHt = (ZcDdcgHt) listCursor.getCurrentObject();

    if (zcDdcgHt != null && !"".equals(ZcUtil.safeString(zcDdcgHt.getZcHtCode()))) {//列表页面双击进入

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      zcDdcgHt = zcDdcgHtServiceDelegate.selectByPrimaryKey(zcDdcgHt.getZcHtCode(), this.requestMeta);

      listCursor.setCurrentObject(zcDdcgHt);

      this.setEditingObject(zcDdcgHt);

      refreshSubTableData(zcDdcgHt.getZcConTextBlobid());
      //打开合同文本的内容
    } else {//新增按钮进入

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

      zcDdcgHt = new ZcDdcgHt();

      zcDdcgHt.setZcHtStatus("0");

      zcDdcgHt.setNd(this.requestMeta.getSvNd());
      zcDdcgHt.setExecuteDate(this.requestMeta.getSysDate());
      zcDdcgHt.setAgency(WorkEnv.getInstance().getCurrCoCode());
      zcDdcgHt.setOrgCode(WorkEnv.getInstance().getOrgCode());
      zcDdcgHt.setExecutor(WorkEnv.getInstance().getCurrUserId());
      zcDdcgHt.setExecutorName(WorkEnv.getInstance().getCurrUserName());

      listCursor.getDataList().add(zcDdcgHt);

      listCursor.setCurrentObject(zcDdcgHt);

      this.setEditingObject(zcDdcgHt);

    }

    if (zcDdcgHt.getItemList() == null) {
      List itemList = new ArrayList();
      itemList.add(new ZcDdcghtItem());
      zcDdcgHt.setItemList(itemList);
    }
    itemTablePanel.setTableModel(ZcDdcgHtToTableModelConverter.convertSubItemTableData(zcDdcgHt.getItemList()));

    // 根据工作流模版设置字段是否可编辑

    updateWFEditorEditable(zcDdcgHt, requestMeta);

    // 根据工作流模版设置功能按钮是否可用
    setButtonStatus(zcDdcgHt, requestMeta, this.listCursor);

    updateFieldEditorsEditable();

    setOldObject();

  }

  protected void setItemTableEditor(JTable table) {
    ZcDdcgHtToTableModelConverter.setItemTableEditor(table);
  }

  protected void setOldObject() {
    oldZcDdcgHt = (ZcDdcgHt) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  public String doOpenMold(ZcDdcgHt ddcgHt, ZcEbBulletinWordMold bulletinMold) {
    if (wordPane != null) {
      wordPane.close();
    }
    WordFileUtil.setDir("ht");
    wordFileId = bulletinMold.getFileID();
    String moldFileName = WordFileUtil.loadMold(bulletinMold.getFileID());
    file = new AsFile();
    file.setFileName(moldFileName);

    if (moldFileName == null || moldFileName.length() == 0) { //没有成功载入模板，关闭wordPane  
      if (wordPane != null)
        wordPane.close();

      return "";
    }
    wordPane.open(moldFileName);
    ddcgHt.setZcConText(moldFileName);
    return "";
  }

  public String getPageStatus() {
    return pageStatus;
  }

  public ZcDdcgHtListPanel getListPanel() {

    return self.listPanel;

  }

  public RequestMeta getRequestMeta() {

    return self.requestMeta;

  }

  public static String getCompoId() {
    return "ZC_DDCG_HT";
  }

  protected String getProjectSqlId() {
    return "selectPage.selectProjectForDdcgHt";
  }

  public BillElementMeta getMainBillElementMeta() {
    return mainBillElementMeta;
  }

  protected String getHtNumLabel() {
    return LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NUM);
  }

  protected String getHtStatus() {
    return "ZC_VS_COMPLETION_STATUS";
  }

  class ZcEbProjFnHandler implements IForeignEntityHandler {

    private String columNames[];

    public ZcEbProjFnHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {
      for (Object object : selectedDatas) {
        ZcXmcgHt temp = (ZcXmcgHt) object;
        ZcDdcgHt zcXmcgHt = (ZcDdcgHt) self.listCursor.getCurrentObject();
        dtoForBidWinner.setPackCode(temp.getZcBidCode());
        zcXmcgHt.setZcBidCode(temp.getZcBidCode());
        zcXmcgHt.setZcMakeCode(temp.getZcPProMake().getZcMakeCode());
        zcXmcgHt.setZcBidContent(temp.getZcBidContent());
        zcXmcgHt.setZcMakeName(temp.getZcPProMake().getZcMakeName());
        zcXmcgHt.setZcPifuCgfs(temp.getZcPProMake().getZcPifuCgfs());
        zcXmcgHt.setCoCode(temp.getCoCode());
        zcXmcgHt.setNd(temp.getNd());
        zcXmcgHt.setZcHtName(temp.getZcPProMake().getZcMakeName());
        setEditingObject(zcXmcgHt);
      }
    }

    private void before() {

      ZcDdcgHt zcDdcgHt = (ZcDdcgHt) self.listCursor.getCurrentObject();

      if (zcDdcgHt == null || zcDdcgHt.getZcBidCode() == null) {
        JOptionPane.showMessageDialog(self, "请先选择分包信息", "提示", JOptionPane.INFORMATION_MESSAGE);
        return;

      }
    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcXmcgHt rowData = (ZcXmcgHt) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getZcPProMake().getZcMakeCode();

        data[i][col++] = rowData.getZcPProMake().getZcMakeName();

        data[i][col++] = rowData.getZcBidContent();

        data[i][col++] = rowData.getZcCgLeixing();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        public boolean isCellEditable(int row, int colum) {

          return false;

        }

      };

      return model;

    }

    @Override
    public boolean isMultipleSelect() {

      // TODO Auto-generated method stub

      return false;

    }

  }

  class ZcEbWinBidSupplierHandler implements IForeignEntityHandler {

    public String columNames[];

    public ZcEbWinBidSupplierHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {
      ZcDdcgHt zcDdcgHt = (ZcDdcgHt) self.listCursor.getCurrentObject();

      if (selectedDatas.size() > 0) {
        ZcEbSupplier su = (ZcEbSupplier) selectedDatas.get(0);

        zcDdcgHt.setZcSuCode(su.getCode());
        zcDdcgHt.setZcSuName(su.getName());
        Map map = new HashMap();
        map.put("SU_CO_CODE", su.getCode());
        map.put("PACK_CODE", zcDdcgHt.getZcBidCode());
        ZcEbSignupPackDetail deteil = (ZcEbSignupPackDetail) zcEbBaseServiceDelegate.queryObject("ZcEbSignup.getBidZcEbSignupPackKbyLbByMap", map,
          requestMeta);
        zcDdcgHt.setTbylbFileId(deteil.getTbylbFileId());
        zcDdcgHt.setTbylbFileName(deteil.getTbylbFileName());
        setEditingObject(zcDdcgHt);
      }
    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcEbSupplier rowData = (ZcEbSupplier) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getName();

        data[i][col++] = rowData.getCode();

        data[i][col++] = rowData.getLinkMan();

        data[i][col++] = rowData.getAddress();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        public boolean isCellEditable(int row, int colum) {

          return false;

        }

      };

      return model;

    }

    @Override
    public boolean isMultipleSelect() {

      return false;

    }

    public boolean beforeSelect(ElementConditionDto dto) {
      return true;
    }

    // 添加清空操作
    public void afterClear() {
    }

  }

  /*

   * 流程跟踪

   */

  private void doTrace() {

    ZcBaseBill bean = (ZcBaseBill) this.listCursor.getCurrentObject();

    if (bean == null) {

      return;

    }

    ZcUtil.showTraceDialog(bean, getCompoId());

  }

  /*

   * 审核

   */

  protected void doAudit() {

    if (checkBeforeSave()) {
      return;
    }

    if (isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    ZcDdcgHt bal = (ZcDdcgHt) this.listCursor.getCurrentObject();
    ZcBaseBill afterBill = ZcWorkFlowAdapter.unAuditFN(bal, this, sendButton, requestMeta, null);
    if (afterBill != null) {
      ZcDdcgHt afterSaveBill = zcDdcgHtServiceDelegate.selectByPrimaryKey(bal.getZcHtCode(), requestMeta);
      this.refreshAll(afterSaveBill, true);
      this.listPanel.refreshCurrentTabData();
    }

  }

  /*

   * 销审

   */

  protected void doUnAudit() {

    if (checkBeforeSave()) {
      return;
    }

    if (isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    ZcDdcgHt bal = (ZcDdcgHt) this.listCursor.getCurrentObject();
    ZcBaseBill afterBill = ZcWorkFlowAdapter.unAuditFN(bal, this, sendButton, requestMeta, null);
    if (afterBill != null) {
      ZcDdcgHt afterSaveBill = zcDdcgHtServiceDelegate.selectByPrimaryKey(bal.getZcHtCode(), requestMeta);
      this.refreshAll(afterSaveBill, true);
      this.listPanel.refreshCurrentTabData();
    }

  }

  /*

   * 退回

   */

  protected void doUnTread() {
    if (checkBeforeSave()) {
      return;
    }

    if (isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    ZcDdcgHt bal = (ZcDdcgHt) this.listCursor.getCurrentObject();
    ZcBaseBill afterBill = ZcWorkFlowAdapter.untreadFN(bal, this, sendButton, requestMeta, null);
    if (afterBill != null) {
      ZcDdcgHt afterSaveBill = zcDdcgHtServiceDelegate.selectByPrimaryKey(bal.getZcHtCode(), requestMeta);
      this.refreshAll(afterSaveBill, true);
      this.listPanel.refreshCurrentTabData();
    }
  }

  private void doSelectMoldButton() {

    fieldMoldName.getField().handleClick(null);

  }

  public class ZcEbWordMoldFnHandler implements IForeignEntityHandler {

    private String columNames[];

    public ZcEbWordMoldFnHandler(String columNames[]) {

      this.columNames = columNames;

    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcEbBulletinWordMold rowData = (ZcEbBulletinWordMold) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getBulletinMoldCode();

        data[i][col++] = rowData.getBulletinMoldName();

        data[i][col++] = AsValDataCache.getName("VS_ZC_EB_BULLETIN_MOLD_TYPE", rowData.getBulletinMoldType());

        data[i][col++] = rowData.getInputDate();

        data[i][col++] = rowData.getInputorName();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        public boolean isCellEditable(int row, int colum) {

          return false;

        }

      };

      return model;

    }

    @Override
    public boolean isMultipleSelect() {

      return false;

    }

    @Override
    public void excute(List selectedDatas) {

      ZcDdcgHt zcDdcgHt = (ZcDdcgHt) self.listCursor.getCurrentObject();

      for (Object object : selectedDatas) {

        ZcEbBulletinWordMold bulletinMold = (ZcEbBulletinWordMold) object;

        doOpenMold(zcDdcgHt, bulletinMold);

      }

    }

  }
}
