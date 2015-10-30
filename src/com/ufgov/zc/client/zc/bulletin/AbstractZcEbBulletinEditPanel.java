package com.ufgov.zc.client.zc.bulletin;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_CO_NAME;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.GkCommentUntreadDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AgreeButton;
import com.ufgov.zc.client.component.button.AuditPassButton;
import com.ufgov.zc.client.component.button.CallbackButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.DisTrackRevisionsButton;
import com.ufgov.zc.client.component.button.DisagreeButton;
import com.ufgov.zc.client.component.button.DownloadButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.HelpButton;
import com.ufgov.zc.client.component.button.IsSendToNextButton;
import com.ufgov.zc.client.component.button.LoadMoldButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.OpenNotepadButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.PrintButton;
import com.ufgov.zc.client.component.button.ReleaseButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.SendRecordButton;
import com.ufgov.zc.client.component.button.SendToXieBanButton;
import com.ufgov.zc.client.component.button.SuggestAuditPassButton;
import com.ufgov.zc.client.component.button.TraceButton;
import com.ufgov.zc.client.component.button.UnauditButton;
import com.ufgov.zc.client.component.button.UntreadButton;
import com.ufgov.zc.client.component.button.ViewTrackRevisionsButton;
import com.ufgov.zc.client.component.button.zc.CommonButton;
import com.ufgov.zc.client.component.event.ValueChangeEvent;
import com.ufgov.zc.client.component.event.ValueChangeListener;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.datacache.CompanyDataCache;
import com.ufgov.zc.client.util.NumUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.WordFileUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.bulletinmold.IWordMoldFn;
import com.ufgov.zc.client.zc.bulletinmold.ZcEbWordMoldFnHandler;
import com.ufgov.zc.client.zc.notepad.ZcNotepadDialog;
import com.ufgov.zc.client.zc.zbfile.ZcEbZbFilePanel;
import com.ufgov.zc.client.zc.ztb.activex.WordPane;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.BaseException;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.model.ZcEbBulletinWordMold;
import com.ufgov.zc.common.zc.model.ZcEbBulletinWordMoldParam;
import com.ufgov.zc.common.zc.model.ZcEbPlan;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.model.ZcEbProjZbFile;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbBulletinServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbBulletinWordMoldServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbProjServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbZbFileServiceDelegate;

public abstract class AbstractZcEbBulletinEditPanel extends AbstractMainSubEditPanel implements IWordMoldFn {

  private static final Logger logger = Logger.getLogger(ZcEbBulletinBidEditPanel.class);

  public static final String XUNJIA_TEMPLETE_AS_FILE_ID_EXT = "_view";

  public static final String PATH = ZcUtil.dir + "ht/";

  protected IZcEbBulletinServiceDelegate zcEbBulletinServiceDelegate = (IZcEbBulletinServiceDelegate) ServiceFactory.create(

  IZcEbBulletinServiceDelegate.class, "zcEbBulletinServiceDelegate");

  protected IZcEbProjServiceDelegate zcEbProjServiceDelegate = (IZcEbProjServiceDelegate) ServiceFactory.create(IZcEbProjServiceDelegate.class,

  "zcEbProjServiceDelegate");

  IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  private final IZcEbZbFileServiceDelegate zcEbZbFileServiceDelegate = (IZcEbZbFileServiceDelegate) ServiceFactory

  .create(IZcEbZbFileServiceDelegate.class, "zcEbZbFileServiceDelegate");

  private final IZcEbBulletinWordMoldServiceDelegate zcEbBulletinWordMoldServiceDelegate = (IZcEbBulletinWordMoldServiceDelegate) ServiceFactory
    .create(

    IZcEbBulletinWordMoldServiceDelegate.class, "zcEbBulletinWordMoldServiceDelegate");

  protected IZcEbBulletinServiceDelegate getIZcEbBulletinServiceDelegate() {

    return this.zcEbBulletinServiceDelegate;

  }

  protected IZcEbProjServiceDelegate getIZcEbProjServiceDelegate() {

    return this.zcEbProjServiceDelegate;

  }

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  //  protected String sqlMapSelectedProj = "ZcEbProj.getZcEbProj";

  //  protected String sqlMapSelectedMold = "ZcEbBulletinWordMold.getZcEbBulletinWordMoldBid";

  protected String replaceValue = "";

  protected String fileName = "";

  protected ListCursor listCursor;

  protected ZcEbBulletin oldBulletin;

  protected String tabStatus;

  protected boolean ifLoadMold = false;

  protected boolean ifRePalceBookMark = false;

  protected AbstractZcEbBulletinBaseListPanel listPanel;

  protected AbstractZcEbBulletinEditPanel self = this;

  protected GkBaseDialog parent;

  protected JTabbedPane tabPane = new JTabbedPane();

  protected WordPane wordPane = new WordPane();

  protected WordPane wordPaneFile1 = new WordPane();

  protected WordPane wordPaneFile2 = new WordPane();

  protected WordPane wordPaneZb = new WordPane();

  private String fileName1 = null;

  private String fileName2 = null;

  private String fileNameZb = null;

  private final int cnt1 = 0;

  private final int cnt2 = 0;

  protected FuncButton saveButton = new SaveButton();

  protected FuncButton downloadButton = new DownloadButton();

  protected FuncButton editButton = new EditButton();

  public FuncButton deleteButton = new DeleteButton();

  public FuncButton openNotepadButton = new OpenNotepadButton();

  //工作流送审
  protected FuncButton sendButton = new SendButton();

  // 工作流收回
  protected FuncButton callbackButton = new CallbackButton();

  //是否送主任审核
  protected FuncButton isSendToNextButton = new IsSendToNextButton();

  // 工作流填写意见审核通过
  protected FuncButton suggestPassButton = new SuggestAuditPassButton();

  // 工作流审核通过
  protected FuncButton auditPassButton = new AuditPassButton();

  // 工作流销审
  protected FuncButton unAuditButton = new UnauditButton();

  // 工作流退回
  protected FuncButton unTreadButton = new UntreadButton();

  // 工作流流程跟踪
  protected FuncButton traceButton = new TraceButton();

  //送备案
  protected FuncButton sendRecordButton = new SendRecordButton();

  //同意

  private final FuncButton agreeButton = new AgreeButton();

  //不同意
  private final FuncButton disagreeButton = new DisagreeButton();

  //送协办人审核

  private final FuncButton sendToXieBanButton = new SendToXieBanButton();

  protected ReleaseButton releaseButton = new ReleaseButton();

  protected FuncButton previousButton = new PreviousButton();

  protected FuncButton nextButton = new NextButton();

  public FuncButton printButton = new PrintButton();

  protected LoadMoldButton loadMoldButton = new LoadMoldButton();

  protected FuncButton exitButton = new ExitButton();

  protected FuncButton helpButton = new HelpButton();

  protected FuncButton overxxzjButton = new CommonButton("foverxxzj", "save.jpg");

  protected FuncButton overxxjjButton = new CommonButton("foverxxjj", "save.jpg");

  //显示痕迹

  private final FuncButton viewTrackRevisionsButton = new ViewTrackRevisionsButton();

  //隐藏痕迹

  private final FuncButton disTrackRevisionsButton = new DisTrackRevisionsButton();

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  protected ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  //
  //  protected String columNames[] = { "项目编号", "项目名称", "预算", "采购类型", "负责人", /*"是否划分标段",*/"电话", "邮件", "传真", /*"是否发布采购公告", "是否发布采购结果" */};
  //
  //  protected ZcEbProjFnHandler handler = new ZcEbProjFnHandler(columNames);

  protected ForeignEntityFieldEditor fieldZcMakeCode = null;

  protected TextFieldEditor fieldZcMakeName = null;

  protected String moldColumNames[] = { "模板编号", "模板名称", "公告模板类型", "状态", "备注" };

  protected ZcEbWordMoldFnHandler handlera = new ZcEbWordMoldFnHandler(moldColumNames, this);

  protected ElementConditionDto findWordMoldCondition = new ElementConditionDto();

  protected ForeignEntityFieldEditor fieldMoldName = new ForeignEntityFieldEditor(getSqlMapSelectedMold(), findWordMoldCondition, 20, handlera,

  moldColumNames, "载入模板", "moldName");

  protected BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(getCompId());

  protected ZcEbBulletinWordMold wordMold;

  JSaveableSplitPane splitPane;

  ZcEbZbFilePanel zbPanel;

  protected JTablePanel detailTablePanel = new JTablePanel();

  protected JFuncToolBar bottomToolBar1;

  FileFieldEditor fileId1;

  FileFieldEditor fileId2;

  //是否展示抽专家和基建的panel
  boolean isShowPanel = false;

  public AbstractZcEbBulletinEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, AbstractZcEbBulletinBaseListPanel listPanel,

  String compoId) {

    super(new ZcEbBulletin(), compoId);

    fieldZcMakeName = new TextFieldEditor(LangTransMeta.translate(fieldName()), "projName");

    this.listCursor = listCursor;

    this.tabStatus = tabStatus;

    this.listPanel = listPanel;

    this.parent = parent;

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), getModelName(), TitledBorder.CENTER,

    TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.colCount = 3;

    init();

    requestMeta.setCompoId(getCompId());

    WordFileUtil.setDir("bulletin");

    addSubPane();
    //    addSubPaneFile1();
    //    addSubPaneFile2();

    refreshMainData();

    setButtonStatus();

    updateFieldEditorsEditable();

    viewTrackRevisionsButton.setVisible(false);

    disTrackRevisionsButton.setVisible(true);

    editButton.setEnabled(true);

  }

  @Override
  protected void init() {
    isShowPanel = ((getBulletinType().equals(ZcEbBulletinConstants.TYPE_BULLETIN_BID) || getBulletinType().equals(
      ZcEbBulletinConstants.TYPE_BULLETIN_XUN_JIA_BID)) && AsOptionMeta.getOptVal("OPT_ZC_CGZX_CODE").equals(requestMeta.getSvCoCode()));

    this.initToolBar(toolBar);

    this.setLayout(new BorderLayout());

    this.add(toolBar, BorderLayout.NORTH);
    this.add(workPanel, BorderLayout.CENTER);
    JComponent tabTable = createSubBillPanel();

    if (this.billClass != null && this.eleMeta != null) {
      initFieldEditorPanel(this.billClass, this.eleMeta);
    } else {
      initFieldEditorPanel();
    }
    workPanel.setLayout(new BorderLayout());

    splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, fieldEditorPanel, tabTable);
    //    splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 150);
    splitPane.setContinuousLayout(true);
    splitPane.setOneTouchExpandable(true);
    // 只显示向下的箭头
    //      splitPane.putClientProperty("toExpand", true);
    splitPane.setDividerSize(5);
    splitPane.setDividerLocation(125);
    workPanel.add(splitPane, BorderLayout.CENTER);
    workPanel.repaint();

  }

  protected abstract String getModelName();

  protected abstract String getCompId();

  protected abstract String getBulletinType();

  protected ElementConditionDto getFindProjConditions() {

    ElementConditionDto dto = new ElementConditionDto();

    dto.setStatus("specialNum12345");

    dto.setManageCode(this.requestMeta.getSvUserID());

    dto.setBulletinType(this.getBulletinType());
    dto.setZcText0(getOpiWay());

    return dto;

  }

  protected void setButtonStatus() {

    if (this.btnStatusList.size() == 0) {

      ButtonStatus bs = new ButtonStatus();

      bs = new ButtonStatus();

      bs.setButton(this.editButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.saveButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      //      bs = new ButtonStatus();

      //      bs.setButton(this.loadMoldButton);

      //      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      //      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);

      //      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      //      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.deleteButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      //      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.sendButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");
      bs.addBillStatus("1");//liubo

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.sendToXieBanButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.previousButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.nextButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.printButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_AUDITED);

      btnStatusList.add(bs);

    }

    ZcEbBulletin ht = (ZcEbBulletin) this.listCursor.getCurrentObject();

    String billStatus = ht.getBulletinStatus();

    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, getCompId(), ht.getProcessInstId());

  }

  public void refreshZcEbPlan() {
    ZcEbBulletin bulletin = (ZcEbBulletin) listCursor.getCurrentObject();
    List planList = zcEbBaseServiceDelegate.queryDataForList("ZcEbPlan.getZcEbPlanByProjCode", bulletin.getProjCode(), requestMeta);
    if (planList != null && planList.size() > 0) {
      ZcEbPlan plan = (ZcEbPlan) planList.get(0);
      bulletin.setZcEbPlan(plan);
    }
    listCursor.setCurrentObject(bulletin);
    this.setEditingObject(bulletin);
  }

  protected String refreshMainData() {

    ZcEbBulletin bulletin = (ZcEbBulletin) listCursor.getCurrentObject();

    if (bulletin == null || bulletin.getBulletinID() == null || "".equals(bulletin.getBulletinID())) {//新增页面

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

      bulletin = new ZcEbBulletin();

      setDefaultValue(bulletin, ZcSettingConstants.PAGE_STATUS_NEW);

      List lst = new ArrayList();

      lst.add(bulletin);

      this.listCursor.setDataList(lst, -1);
      ifRePalceBookMark = false;

    } else {
      bulletin = listPanel.zcEbBulletinServiceDelegate.getZcEbBulletinByKey(bulletin.getBulletinID(), requestMeta);
    }

    listCursor.setCurrentObject(bulletin);

    //    callBackRefreshDate();

    this.setEditingObject(bulletin);

    setOldObject();

    if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equals(bulletin.getBulletinStatus())) {
      setCancelStatus(listCursor);
    } else {
      setButtonStatus(bulletin, requestMeta, this.listCursor);
    }

    Long processInstId = bulletin.getProcessInstId();

    if (processInstId == null || processInstId.longValue() < 0) {

      sendRecordButton.setVisible(false);

      sendToXieBanButton.setVisible(false);

    }

    if (processInstId == null) {

      editButton.setEnabled(false);

    } else if (processInstId != null) {

      editButton.setEnabled(true);

      sendButton.setEnabled(true);

    }
    refreshExtracButton();

    //    if (processInstId == null || processInstId < 0) {
    //
    //      deleteButton.setVisible(true);
    //
    //    } else {
    //
    //      deleteButton.setVisible(false);
    //
    //    }
    if (bulletin.getPackList() == null) {
      bulletin.setPackList(new ArrayList());
    }

    return bulletin.getFileID();

  }

  public void refreshData() {

    String fileID = refreshMainData();

    refreshSubFiles();

    refreshZbFile();

    refreshSubTableData(fileID);

  }

  public void refreshDataOnly() {

    String fileID = refreshMainData();

    refreshZbFile();

    //	  refreshSubTableData(fileID);

    protectWordPanel();
  }

  public void protectWordPanel() {

    wordPane.protectDoc(ZcSettingConstants.WORD_PASSWORD);

  }

  private void refreshSubFiles() {
    /*
    if (isShowPanel) {
      addSubPaneFile1();
      addSubPaneFile2();
    }
    */
  }

  /**
   * 
  * 刷新word文本
   */
  protected void refreshSubTableData(String fileID) {

    closeWordPanel(wordPane, true);

    if (fileID != null && !fileID.equals("")) {

      this.fileName = WordFileUtil.loadMold(fileID);

    } else {

      this.fileName = WordFileUtil.loadDefaultMold();

    }
    wordPane.openAndProtect(this.fileName, ZcSettingConstants.WORD_PASSWORD);

  }

  public String zbFileID = null;

  protected void refreshZbFile() {
    //只有招标公告和一般询价的公告有招标文件
/*    ZcEbBulletin bulletin = (ZcEbBulletin) listCursor.getCurrentObject();
    if (bulletin.getBulletinType().equals(ZcEbBulletinConstants.TYPE_BULLETIN_BID)) {
      if (bulletin.getProjCode() != null) {
        ZcEbProjZbFile zcEbProjZbFile = zcEbZbFileServiceDelegate.getZcebZbFileByProjCode(bulletin.getProjCode(), requestMeta);
        if (zcEbProjZbFile != null) {

          if (getTabPanelByName(tabPane, "panel_filenamezb") == null) {
            addSubPaneZb();
          }

          zbFileID = zcEbProjZbFile.getWordFileId();
          try {
            //        	  closeWordPanel(wordPaneZb, false);

            //        	  refreshZbFile(zcEbProjZbFile.getWordFileId());

          } catch (Exception e) {
            e.printStackTrace();
          }

        }

      }
    }*/
  }

  public JPanel getTabPanelByName(JTabbedPane tabPane, String name) {

    if (tabPane == null || name == null)
      return null;

    Component[] coms = tabPane.getComponents();

    for (int i = 0; i < coms.length; i++) {

      JPanel panel = (JPanel) coms[i];

      if (name.equals(panel.getName())) {

        return panel;
      }
    }

    return null;

  }

  protected void addSubPaneZb() {

    //下面一句是为了打开word后刷新窗口
    parent.setSize(parent.getSize().width + 1, parent.getSize().height + 1);

    wordPaneZb.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {

        //打开文件完成之后的回调函数
        boolean isSuccess = (Boolean) evt.getNewValue();

        if (isSuccess) {

          //下面一句是为了打开word后刷新窗口
          parent.setSize(parent.getSize().width - 1, parent.getSize().height - 1);

        }

      }

    });

    wordPaneZb.setName("panel_filenamezb");

    tabPane.addTab("招标文件", wordPaneZb);

  }

  public void refreshZbFile(String fileID) {

    if (fileID == null || fileID.equals(""))
      return;

    if (wordPaneZb == null) {

      wordPaneZb = new WordPane();

      wordPaneZb.setName("panel_filenamezb");
    }

    if (wordPaneZb.isDocOpened()) {

    } else {

      this.fileNameZb = WordFileUtil.loadMold(fileID);

      wordPaneZb.openAndProtect(this.fileNameZb, ZcSettingConstants.WORD_PASSWORD);
    }

  }

  protected void setDefaultValue(ZcEbBulletin bulletin, String pageStatus) {

    if (pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {

      bulletin.setInputorName(WorkEnv.getInstance().getCurrUserName());

      bulletin.setExecutor(WorkEnv.getInstance().getCurrUserName());

      bulletin.setExecuteDate(WorkEnv.getInstance().getSysDate());

      bulletin.setNd(WorkEnv.getInstance().getSysNd());

      bulletin.setBulletinStatus("0");

      bulletin.setBulletinType(getBulletinType());
      bulletin.setAgency(requestMeta.getSvCoCode());
      bulletin.setOrgCode(requestMeta.getSvOrgCode());

    }

  }

  protected void addSubPane() {

    //下面一句是为了打开word后刷新窗口
    parent.setSize(parent.getSize().width + 1, parent.getSize().height + 1);

    wordPane.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {

        //打开文件完成之后的回调函数
        boolean isSuccess = (Boolean) evt.getNewValue();

        if (isSuccess) {

          //下面一句是为了打开word后刷新窗口
          parent.setSize(parent.getSize().width - 1, parent.getSize().height - 1);

        }

      }

    });

    tabPane.addTab("公告内容", wordPane);

  }

  protected void addSubPaneFile1() {

    //下面一句是为了打开word后刷新窗口
    parent.setSize(parent.getSize().width + 1, parent.getSize().height + 1);

    wordPaneFile1.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {

        //打开文件完成之后的回调函数
        boolean isSuccess = (Boolean) evt.getNewValue();

        if (isSuccess) {

          //下面一句是为了打开word后刷新窗口
          parent.setSize(parent.getSize().width - 1, parent.getSize().height - 1);

        }

      }

    });

    wordPaneFile1.setName("panel_filename1");

    tabPane.addTab("抽取专家申请表", wordPaneFile1);

  }

  protected void addSubPaneFile2() {

    //下面一句是为了打开word后刷新窗口
    parent.setSize(parent.getSize().width + 1, parent.getSize().height + 1);

    wordPaneFile2.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {

        //打开文件完成之后的回调函数
        boolean isSuccess = (Boolean) evt.getNewValue();

        if (isSuccess) {

          //下面一句是为了打开word后刷新窗口
          parent.setSize(parent.getSize().width - 1, parent.getSize().height - 1);

        }

      }

    });

    wordPaneFile2.setName("panel_filename2");

    tabPane.addTab("抽取纪检申请表", wordPaneFile2);

  }

  @Override
  protected void updateFieldEditorsEditable() {

    super.updateFieldEditors();

    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW) || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        if (fd.getFieldName() != null

          && (fd.getFieldName().equals("zcMakeName") || fd.getFieldName().equals("bulletinID") || fd.getFieldName().equals("bulletinStatus")

          || fd.getFieldName().equals("executeDate") || fd.getFieldName().equals("executor") || fd.getFieldName().equals("moldName") ||

          fd.getFieldName().equals("coCode") || fd.getFieldName().equals("projName") || fd.getFieldName().equals("zcPProMake.zcXieYiEndDate") || fd

          .getFieldName().equals("zcEbPlan.sellStartTime") || fd

          .getFieldName().equals("zcEbPlan.sellEndTime") || fd

          .getFieldName().equals("zcEbProj.projCode") || fd

          .getFieldName().equals("zcEbProj.projName") || fd.getFieldName().equals("packPlan.openBidTime") || fd

          .getFieldName().equals("zcEbPlan.bidEndTime") || fd.getFieldName().equals("zcEbPlan.openBidTime") || fd.getFieldName().equals(
            "zcEbPlan.openBidAddress"))) {

          fd.setEnabled(false);

        } else {

          fd.setEnabled(true);

        }

      }
      if (detailTablePanel != null)
        detailTablePanel.getTable().setEnabled(true);
      if (bottomToolBar1 != null)
        bottomToolBar1.setVisible(true);

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        fd.setEnabled(false);

      }
      if (detailTablePanel != null)
        detailTablePanel.getTable().setEnabled(false);
      if (bottomToolBar1 != null)
        bottomToolBar1.setVisible(false);

    }

  }

  protected void setOldObject() {

    oldBulletin = (ZcEbBulletin) ObjectUtil.deepCopy(listCursor.getCurrentObject());

  }

  public boolean isDataChanged() {
    if (!saveButton.isVisible() || !saveButton.isEnabled()) {
      return false;
    }

    //如果载入了模板并且进行了数据填充，说明数据已经修改
    if (ifLoadMold && ifRePalceBookMark) {

      return true;

    } else {

      return !DigestUtil.digest(oldBulletin).equals(DigestUtil.digest(listCursor.getCurrentObject()));

    }

  }

  @Override
  public JComponent createSubBillPanel() {

    this.tabPane.addChangeListener(new ChangeListener() {

      public void stateChanged(ChangeEvent e) {
        JTabbedPane tab = (JTabbedPane) e.getSource();
        ZcEbBulletin bulletin = (ZcEbBulletin) self.listCursor.getCurrentObject();

        JPanel pan = (JPanel) tab.getSelectedComponent();

        if ("panel_filenamezb".equals(pan.getName())) {
          refreshZbFile(zbFileID);
        }
        /*
        if (isShowPanel && pan!=null) {
          if ("panel_filename1".equals(pan.getName()) && cnt1++ < 1) {
            refreshWordPaneFile1(bulletin);
          } else if ("panel_filename2".equals(pan.getName()) && cnt2++ < 1) {
            refreshWordPaneFile2(bulletin);
          }

        }
        */
      }
    });

    return this.tabPane;

  }

  protected void selectProj(List selectedDatas) {

    if (selectedDatas != null && selectedDatas.size() > 0) {

      ZcEbProj proj = (ZcEbProj) selectedDatas.get(0);

      ZcEbBulletin bulletin = (ZcEbBulletin) listCursor.getCurrentObject();

      bulletin.setProjCode(proj.getProjCode());

      bulletin.setProjName(proj.getProjName());
      bulletin.setCoCode(proj.getCoCode());
      findWordMoldCondition.setType(proj.getPurType());

      fieldMoldName.setEditObject(bulletin);

      if (bulletin.getBulletinID() == null) {

        handlera.flashReplaceValue();

      } else {

        bulletin.setMoldName("");

        bulletin.setFileID(null);

        refreshWordPane(bulletin);

        refreshZbFile();

      }

      setEditingObject(bulletin);

    }

    setFieldMoldNameStatus();

    setLoadMoldButtonStatus();

  }

  protected void projCodeChange() {

    ZcEbBulletin zcEbBulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();

    for (AbstractFieldEditor editor : this.fieldEditors) {

      if (editor.getFieldName().equals("zcMakeCode")) {

        Object obj = editor.getValue();

        if (obj == null || obj.toString().trim().length() == 0) {

          this.fieldMoldName.setValue("");

          zcEbBulletin.setProjCode(null);

          zcEbBulletin.setProjName(null);

          zcEbBulletin.setMoldName(null);

          this.fieldZcMakeName.setEditObject(zcEbBulletin);

          this.fieldMoldName.setEditObject(zcEbBulletin);

          this.fieldMoldName.setEnabled(true);

          break;

        }

      }

    }

    setFieldMoldNameStatus();

  }

  protected void setFieldMoldNameStatus() {

    if (this.fieldZcMakeCode.getValue() == null || this.fieldZcMakeCode.getValue().toString().length() == 0) {

      this.fieldMoldName.setEnabled(false);

    } else {

      this.fieldMoldName.setEnabled(true);

    }

  }

  public String doOpenMold(List valueList, ZcEbBulletinWordMold bulletinMold) {

    closeWordPanel(wordPane, true);

    String moldFileName = WordFileUtil.loadMold(bulletinMold.getFileID());

    if (moldFileName == null || moldFileName.length() == 0) {

      //没有成功载入模板，关闭wordPane  

      if (wordPane != null)

        wordPane.close();

      return "";

    }

    wordPane.open(moldFileName);

    fileName = moldFileName;//zcEbWordPanel.getFileName();

    StringBuffer sb = new StringBuffer();

    for (Iterator it = valueList.iterator(); it.hasNext();) {

      ZcEbBulletinWordMoldParam zcEbBulletinWordMoldParam = (ZcEbBulletinWordMoldParam) it.next();

      sb.append(zcEbBulletinWordMoldParam.getParamName());

      sb.append("$$$$$");

      sb.append(zcEbBulletinWordMoldParam.getParamValue());

      sb.append("@@@@@");

    }

    return sb.toString();

  }

  protected String saveBulletinContent() {

    String fileID = "";

    if (fileName != null && fileName.length() != 0)

      fileID = WordFileUtil.uploadBulletinWordConstent(fileName);

    return fileID;

  }

  protected boolean checkBeforeSave(boolean isSend) {

    List notNullBillElementList = this.billElementMeta.getNotNullBillElement();

    ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();

    StringBuilder errorInfo = new StringBuilder();

    String validateInfo = ZcUtil.validateBillElementNull(bulletin, notNullBillElementList);

    if (validateInfo.length() != 0) {

      errorInfo.append("").append(validateInfo.toString()).append("\n");

    }
    if (errorInfo.length() == 0) {
      if (bulletin.getProjCode() == null || "".equals(bulletin.getProjCode().trim())) {
        errorInfo.append("[项目代码]不能为空 \n");
      }
      if (bulletin.getProjName() == null || "".equals(bulletin.getProjName().trim())) {
        errorInfo.append("[项目名称]不能为空 \n");
      }
      if (bulletin.getMoldCode() == null || "".equals(bulletin.getMoldCode().trim())) {
        errorInfo.append("[模板]不能为空 \n");
      }
      if (isSend && isShowPanel) {
        if (bulletin.getFileId1() == null || "".equals(bulletin.getFileId1().trim()) || fileId1.getValue() == null) {
          errorInfo.append("[抽取专家申请表]不能为空 \n");
        }
        if (bulletin.getFileId2() == null || "".equals(bulletin.getFileId2().trim()) || fileId2.getValue() == null) {
          errorInfo.append("[抽取纪检申请表]不能为空 \n");
        }
      }
    }

    if (errorInfo.length() != 0) {

      JOptionPane.showMessageDialog(this.parent, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);

      return false;

    }

    return true;

  }

  protected void createMakeCode() {
    String columNames[] = { "项目编号", "项目名称", "预算", "采购类型", "采购单位", "负责人", "电话", "邮件", "传真", /*"是否发布采购公告", "是否发布采购结果" */};

    ZcEbProjFnHandler handler = new ZcEbProjFnHandler(columNames);

    fieldZcMakeCode = new ForeignEntityFieldEditor(getSqlMapSelectedProj(), getFindProjConditions(), 20,

    handler, columNames, LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_EB_ZC_MAKE_CODE), "projCode");
  }

  /**

   * 创建字段对象

   */

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    createMakeCode();

    fieldZcMakeCode.addValueChangeListener(new ValueChangeListener() {

      @Override
      public void valueChanged(ValueChangeEvent e) {

        projCodeChange();

      }

    });

    editorList.add(fieldZcMakeCode);

    editorList.add(fieldZcMakeName);

    AsValFieldEditor fieldBulletinStatus = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_BULLETIN_STATUS),

    "bulletinStatus", "ZC_EB_BULLETIN_STATUS");

    editorList.add(fieldBulletinStatus);

    editorList.add(fieldMoldName);

    TextFieldEditor fieldInputorName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_INPUTOR_NAME), "executor");

    editorList.add(fieldInputorName);

    if (WorkEnv.getInstance().containRole(ZcSettingConstants.ROLE_GYS_NORMAL)) {
      fieldBulletinStatus.setVisible(false);
      fieldMoldName.setVisible(false);
      fieldInputorName.setVisible(false);
    }

    //    DateFieldEditor fieldInputDate = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_INPUT_DATE), "executeDate");
    //    fieldInputDate.setEnabled(false);
    //    editorList.add(fieldInputDate);

    /**
     * 公开招标和询价
     */
    if (isShowPanel) {
      fileId1 = new FileFieldEditor("抽取专家申请表", "fileName1", "fileId1");
      editorList.add(fileId1);
      fileId2 = new FileFieldEditor("抽取纪检申请表", "fileName2", "fileId2");
//      editorList.add(fileId2);
      /*
      fileId1.addEditSyncListener(new EditSyncListener() {

        @Override
        public void sync(EditSyncEvent e) {
          refreshWordPaneFile1(((ZcEbBulletin) fileId1.getEditObject()));
        }
      });

      fileId2.addEditSyncListener(new EditSyncListener() {

        @Override
        public void sync(EditSyncEvent e) {

          refreshWordPaneFile2(((ZcEbBulletin) fileId2.getEditObject()));

        }
      });
      */
    }
    CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_CO_NAME), "coCode");
    zcCoCode.setEnabled(false);

    if (!getBulletinType().equals(ZcEbBulletinConstants.TYPE_BULLETIN_JING_JIA_BID)
      || AsOptionMeta.getOptVal("OPT_ZC_CGZX_CODE").equals(requestMeta.getSvCoCode())) {
      editorList.add(zcCoCode);
    }
    return editorList;
  }

  /**

   * 以下添加按钮和按钮方法

   */

  @Override
  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(getCompId());

    addButtonMenu(this.tabStatus);

    loadMoldButton.setEnabled(false);

    saveButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSave(true);

      }

    });

    downloadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        WordFileUtil.doSaveWordFile("采购公告.doc", wordPane, self);

      }

    });

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

    viewTrackRevisionsButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 显示痕迹

        wordPane.viewTrackRevisions(true);

        viewTrackRevisionsButton.setVisible(false);

        disTrackRevisionsButton.setVisible(true);

      }

    });

    disTrackRevisionsButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 隐藏痕迹

        wordPane.viewTrackRevisions(false);

        viewTrackRevisionsButton.setVisible(true);

        disTrackRevisionsButton.setVisible(false);

      }

    });

    isSendToNextButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSendNext();

      }

    });

    releaseButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doPublish();

      }

    });

    previousButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doPrevious();

      }

    });

    nextButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doNext();

      }

    });

    traceButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doTrace();

      }

    });

    loadMoldButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doReplaceBookMarks();

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

    sendRecordButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 送采购处备案

        doSendRecord();

      }

    });

    agreeButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 同意

        doAgree();

      }

    });

    disagreeButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 不同意
        doDisagree();

      }

    });

    sendToXieBanButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 送协办人

        doSendToXieBan();

      }

    });

    deleteButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doDelete();

      }

    });

    exitButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doExit();

      }

    });

    printButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doPrint();

      }

    });

    openNotepadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doOpenNotepad();

      }

    });

  }

  private void doOpenNotepad() {

    ZcEbBulletin sheet = (ZcEbBulletin) this.listCursor.getCurrentObject();

    String sn = fetchSn(sheet);

    if (sn != null) {

      new ZcNotepadDialog(sn, parent);

    }

  }

  public abstract String fetchSn(ZcEbBulletin sheet);

  private void doSendRecord() {

    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    boolean success = true;

    ZcEbBulletin afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.sendRecordButton.getFuncId());

      ZcEbBulletin bulletin = (ZcEbBulletin) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      bulletin.setComment(commentDialog.getComment());

      bulletin.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      bulletin.setIsGoonAudit(ZcSettingConstants.IS_GOON_AUDIT_YES);

      this.getIZcEbBulletinServiceDelegate().updateFN(bulletin, requestMeta);

      afterSaveBill = listPanel.getIZcEbBulletinServiceDelegate().sendRecordFN(bulletin, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  private void doPrint() {

    try {

      this.wordPane.print();

    } catch (RuntimeException e) {

      // TODO Auto-generated catch block

      e.printStackTrace();

      JOptionPane.showMessageDialog(this, "请确认系统已经打开公告，再进行打印！", "提示", JOptionPane.INFORMATION_MESSAGE);

    }

  }

  protected void doDelete() {

    ZcEbBulletin zcEbBulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();

    int num = JOptionPane.showConfirmDialog(this, "确认删除当前数据？", "删除确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      String errorInfo = "";

      try {

        listPanel.getIZcEbBulletinServiceDelegate().deleteFN(zcEbBulletin, requestMeta);

        JOptionPane.showMessageDialog(self, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

        this.listCursor.removeCurrentObject();

        refreshData();

        this.listPanel.refreshCurrentTabData();

        updateFieldEditorsEditable();

        setButtonStatus();

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        errorInfo += e.getMessage();

        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

  }

  /*

   * 退回

   */

  protected void doUnTread() {

    GkCommentUntreadDialog commentDialog = new GkCommentUntreadDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    boolean success = true;

    ZcEbBulletin afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(unTreadButton.getFuncId());

      ZcEbBulletin ht = (ZcEbBulletin) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      ht.setComment(commentDialog.getComment());

      afterSaveBill = listPanel.getIZcEbBulletinServiceDelegate().untreadFN(ht, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "退回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  /*

   * 销审

   */

  protected void doUnAudit() {

    boolean success = true;

    ZcEbBulletin afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(unAuditButton.getFuncId());

      ZcEbBulletin ht = (ZcEbBulletin) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = listPanel.getIZcEbBulletinServiceDelegate().unAuditFN(ht, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "销审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "销审失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  /**

   * 送协办人

   */

  private void doSendToXieBan() {

    ZcEbBulletin zcEbBulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();

    requestMeta.setFuncId(this.sendToXieBanButton.getFuncId());

    executeAudit(zcEbBulletin, ZcSettingConstants.IS_GOON_AUDIT_NO, null);

  }

  /**

   * 同意

   */

  private void doAgree() {

    ZcEbBulletin zcEbBulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();

    requestMeta.setFuncId(this.agreeButton.getFuncId());

    Integer auditFlag = zcEbBulletin.getIsGoonAudit();

    auditFlag = ZcUtil.getAuditFlagValue(auditFlag, 0, requestMeta);

    executeAudit(zcEbBulletin, auditFlag, null);

  }

  /**

   * 不同意

   */

  private void doDisagree() {

    ZcEbBulletin zcEbBulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();

    requestMeta.setFuncId(this.disagreeButton.getFuncId());

    Integer auditFlag = zcEbBulletin.getIsGoonAudit();

    auditFlag = ZcUtil.getAuditFlagValue(auditFlag, 1, requestMeta);

    executeAudit(zcEbBulletin, auditFlag - ZcSettingConstants.IS_GOON_AUDIT_YES, ZcSettingConstants.AUDIT_DISAGREE_DEFULT_MESSAGE);

  }

  /*

   * 填写意见审核

   */
  private void doSuggestPass() {

    ZcEbBulletin zcEbBulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();

    requestMeta.setFuncId(this.suggestPassButton.getFuncId());
    if (ZcSettingConstants.BILL_STATUS_FOR_PUB.equals(zcEbBulletin.getBulletinStatus())
      //&& ZcSettingConstants.ROLE_CODE_CG_CGZX_FZR.equals(zcEbBulletin.getRoleCode())
      ) {
      doPublish();
      return;
    }

    String jianShenRoleId = AsOptionMeta.getOptVal("OPT_ZC_CGZX_JSKY_ROLE");//监审组员角色

    if (WorkEnv.getInstance().containRole(jianShenRoleId)) {//如果是监审员，则不修改审批状态

      Integer auditFlag = zcEbBulletin.getIsGoonAudit();

      executeAudit(zcEbBulletin, auditFlag, null);

    } else {

      executeAudit(zcEbBulletin, ZcSettingConstants.IS_GOON_AUDIT_NO, null);

    }

  }

  /*

   * 审核

   */

  protected void doAudit() {

    if (checkBeforeSave(true)) {

      return;

    }

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    boolean success = true;

    ZcEbBulletin afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditPassButton.getFuncId());

      ZcEbBulletin ht = (ZcEbBulletin) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = listPanel.getIZcEbBulletinServiceDelegate().auditFN(ht, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  /*

   * 收回

   */

  protected void doCallback() {

    boolean success = true;

    ZcEbBulletin afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.callbackButton.getFuncId());

      ZcEbBulletin ht = (ZcEbBulletin) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = listPanel.getIZcEbBulletinServiceDelegate().callbackFN(ht, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "收回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  protected void refreshAll(ZcEbBulletin afterSaveBill, boolean isRefreshButton) {

    this.listCursor.setCurrentObject(afterSaveBill);

    refreshDataOnly();

  }

  public boolean doSave(boolean isMsg) {

    if (!checkBeforeSave(false)) //先对主表输入进行验证

      return false;

    if (loadMoldButton.isVisible() && this.loadMoldButton.isEnabled()) {
      JOptionPane.showMessageDialog(this.parent, "请填充模板", "提示", JOptionPane.WARNING_MESSAGE);

      return false;
    }

    ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();

    boolean isSuccess = true;

    try {
      // 支持直接修改word内容。
      wordPane.save(this.fileName);

      String fileID = saveBulletinContent();

      bulletin.setFileID(fileID);

      if (bulletin.getBulletinID() != null && !bulletin.getBulletinID().equals("")) {

        this.getIZcEbBulletinServiceDelegate().updateFN(bulletin, requestMeta);

      } else {

//        String bulletinID = NumUtil.getNum(getCompId(), "BULLETIN_ID", bulletin, requestMeta);
    	  String bulletinID =ZcUtil.getSequenceNextVal(ZcSettingConstants.SEQUENCE_BULLITIN_ID);

        bulletin.setBulletinID(bulletinID);

        this.getIZcEbBulletinServiceDelegate().insertFN(bulletin, requestMeta);

      }

    } catch (Exception e) {

      isSuccess = false;

      e.printStackTrace();

      JOptionPane.showMessageDialog(self, "保存失败：" + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);

    }

    if (isSuccess) {

      listCursor.setCurrentObject(bulletin);

      this.oldBulletin = (ZcEbBulletin) ObjectUtil.deepCopy(bulletin);

      this.listPanel.refreshCurrentTabData();

      if (isMsg)
        JOptionPane.showMessageDialog(self, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      HashMap parameter = new HashMap();

      parameter.put("BULLETIN_ID", bulletin.getBulletinID());

      List list = zcEbBaseServiceDelegate.queryDataForList("ZcEbBulletin.readBulletinById", parameter, requestMeta);

      if (list != null && list.size() > 0) {

        bulletin = (ZcEbBulletin) list.get(0);

      }

      this.listCursor.setCurrentObject(bulletin);

      //      refreshMainData();
      //      refreshZbFile();

      //      this.refreshData();
      refreshDataOnly();

      //      cnt1=0;cnt2=0;

      this.oldBulletin = (ZcEbBulletin) ObjectUtil.deepCopy(bulletin);

      this.listPanel.refreshCurrentTabData();

      updateFieldEditorsEditable();

      setButtonStatus();

      sendButton.setEnabled(true);

      wordPane.setEnabled(false);
    }

    return isSuccess;

  }

  public void doEdit() {
    ZcEbBulletin bulletin = (ZcEbBulletin) listCursor.getCurrentObject();
    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
    updateFieldEditorsEditable();
    setButtonStatus();
    this.setFieldMoldNameStatus();
    if (bulletin.getProcessInstId() != null && bulletin.getProcessInstId() > 0) {
      wordPane.addTrackRevisions(true, requestMeta.getEmpName());
    }
    wordPane.unProtectDoc(ZcSettingConstants.WORD_PASSWORD);
    //    refreshWordPane(bulletin);
    if (zbPanel != null)
      zbPanel.openAndProtect = false;

  }

  public void doSend() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存并送审", "送审确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave(false)) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldBulletin);

      }

    }
    if (!checkBeforeSend()) {
      return;
    }

    boolean isSuccess = true;

    requestMeta.setFuncId(this.sendButton.getFuncId());

    ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();

    bulletin.setAuditorId(WorkEnv.getInstance().getCurrUserId());

    ArrayList zl = new ArrayList();

    zl.add(bulletin);

    try {

      this.getIZcEbBulletinServiceDelegate().newCommitFN(zl, requestMeta);

    } catch (Exception e) {

      isSuccess = false;

      e.printStackTrace();

      JOptionPane.showMessageDialog(self, "送审失败！" + e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);

    }

    if (isSuccess) {

      HashMap parameter = new HashMap();

      parameter.put("BULLETIN_ID", bulletin.getBulletinID());

      List list = zcEbBaseServiceDelegate.queryDataForList("ZcEbBulletin.readBulletinById", parameter, requestMeta);

      if (list != null && list.size() > 0) {

        bulletin = (ZcEbBulletin) list.get(0);

      }

      this.listCursor.setCurrentObject(bulletin);

      this.oldBulletin = (ZcEbBulletin) ObjectUtil.deepCopy(bulletin);

      this.listPanel.refreshCurrentTabData();

      JOptionPane.showMessageDialog(self, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      updateFieldEditorsEditable();

      this.refreshDataOnly();

      setButtonStatus();

    }

  }

  protected void doSendNext() {

    ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();

    //int sel = JOptionPane.showConfirmDialog(this, "是否送主任审核？");

    requestMeta.setFuncId(this.isSendToNextButton.getFuncId());

    //if (sel == JOptionPane.OK_OPTION) {

    executeAudit(bulletin, ZcSettingConstants.IS_GOON_AUDIT_YES, null);

    //} else {

    //   executeAudit(bulletin, ZcSettingConstants.IS_GOON_AUDIT_NO);

    //}

  }

  protected void executeAudit(ZcEbBulletin bulletin, Integer isGoonAudit, String defaultMsg) {

    GkCommentDialog commentDialog = null;

    ZcEbBulletin afterSaveBill = null;

    if (defaultMsg == null) {

      commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

      ModalityType.APPLICATION_MODAL);

    } else {

      commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

      ModalityType.APPLICATION_MODAL, defaultMsg);

    }

    if (commentDialog.cancel) {

      return;

    }

    boolean success = true;

    String errorInfo = "";

    try {
      //编辑状态下则保存数据
      if (ZcSettingConstants.PAGE_STATUS_EDIT.equals(this.pageStatus) && this.saveButton.isVisible()) {

        if (!checkBeforeSave(true)) { //先对主表输入进行验证

          return;
        }

        // 支持直接修改word内容。
        wordPane.save(this.fileName);

        String fileID = saveBulletinContent();

        bulletin.setFileID(fileID);

        if (bulletin.getBulletinID() != null && !bulletin.getBulletinID().equals("")) {

          this.getIZcEbBulletinServiceDelegate().updateFN(bulletin, requestMeta);

        } else {

          String bulletinID = NumUtil.getNum(getCompId(), "BULLETIN_ID", bulletin, requestMeta);

          bulletin.setBulletinID(bulletinID);

          this.getIZcEbBulletinServiceDelegate().insertFN(bulletin, requestMeta);

        }
      }

      isGoonAudit = isGoonAudit == null ? 0 : isGoonAudit;

      bulletin.setIsGoonAudit(isGoonAudit);

      bulletin.setComment(commentDialog.getComment());

      bulletin.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      //      this.getIZcEbBulletinServiceDelegate().updateFN(bulletin, requestMeta);

      afterSaveBill = this.getIZcEbBulletinServiceDelegate().auditFN(bulletin, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      //this.refreshData();

      this.refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  public boolean publishToHtml(String htmlName) {
    String path = WordFileUtil.getHtmlFileName(htmlName);
    logger.debug("path="+path);
    boolean ifConvert = wordPane.convertWordToHtml(path);

    if (!ifConvert) {

      UIUtilities.showStaickTraceDialog(new BaseException(), this, "错误", "发布公告时,没有正确生成网页文件!");

      return false;

    }
    File dir = new File(path + ".files");
    logger.debug("path="+path+".files");

    File[] fs = dir.listFiles();   
    int t=0;
    while(t<9 && (fs==null || fs.length==0)){
      ++t;
      try {       
        Thread.sleep(1000);        
      } catch (InterruptedException e) {
      }
      fs = dir.listFiles(); 
    }

    logger.debug("fs.size="+fs==null?null:fs.length);
    
    for (File f : fs) {
      String name = f.getName();
      logger.debug("file="+name);
      //      if(name.endsWith(".thmx") || name.endsWith(".mso") || name.endsWith(".png")){
      f.delete();
      //      }
    }
    dir.delete();

    return true;

  }

  public boolean doPublish() {

    ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();
    GkCommentDialog commentDialog = null;

    commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return false;

    }

    bulletin.setIsGoonAudit(ZcSettingConstants.IS_GOON_AUDIT_NO);

    bulletin.setComment(commentDialog.getComment());

    bulletin.setAuditorId(WorkEnv.getInstance().getCurrUserId());

    //因为模板中有发布时间，所以发布时 必须重新填充一次模板
    String vistr = null;

    if ((vistr = checkBeforePublish()) != null) {

      JOptionPane.showMessageDialog(self, vistr, "提示", JOptionPane.INFORMATION_MESSAGE);

      return false;

    }

    if (bulletin.getBulletinStatus().equalsIgnoreCase(ZcEbBulletinConstants.STATUS_BULLETIN_BID_PUBLISHED)) {

      UIUtilities.showStaickTraceDialog(new BaseException(), this, "错误", "公告已经发布,不需要再发布!");

      return false;

    }

    if (tabPane.getComponentCount() > 1) {
      refreshWordPaneForPub(bulletin);
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
      }
    }

    if (!publishToHtml(bulletin.getFileID())) {

      return false;

    }

    if (!readFileByChar(bulletin)) {

      UIUtilities.showStaickTraceDialog(new BaseException(), this, "错误", "公告发布失败!");

      this.listCursor.setCurrentObject(bulletin);

      this.oldBulletin = (ZcEbBulletin) ObjectUtil.deepCopy(bulletin);

      return false;
    }
    Map<String, String> option = new HashMap<String, String>();
    option.put("OPT_ZC_MAIL_FROM_POP", AsOptionMeta.getOptVal("OPT_ZC_MAIL_FROM_POP"));
    option.put("OPT_ZC_MAIL_FROM_PORT", AsOptionMeta.getOptVal("OPT_ZC_MAIL_FROM_PORT"));
    option.put("OPT_ZC_MAIL_FROM_USER", AsOptionMeta.getOptVal("OPT_ZC_MAIL_FROM_USER"));
    option.put("OPT_ZC_MAIL_FROM_PASS", AsOptionMeta.getOptVal("OPT_ZC_MAIL_FROM_PASS"));
    option.put("OPT_ZC_MAIL_TO_GKYQ_USER", AsOptionMeta.getOptVal("OPT_ZC_MAIL_TO_GKYQ_USER"));
    option.put("OPT_ZC_MAIL_TO_USER", AsOptionMeta.getOptVal("OPT_ZC_MAIL_TO_USER"));
    option.put("OPT_ZC_MAIL_TITLE", AsOptionMeta.getOptVal("OPT_ZC_MAIL_TITLE"));
    option.put("OPT_ZC_MAIL_INTERFACE_DOWN", AsOptionMeta.getOptVal("OPT_ZC_MAIL_INTERFACE_DOWN"));
    option.put("OPT_ZC_MAIL_INTERFACE_SERVER", AsOptionMeta.getOptVal("OPT_ZC_MAIL_INTERFACE_SERVER"));
    option.put("OPT_ZC_MAIL_INTERFACE_COMP", AsOptionMeta.getOptVal("OPT_ZC_MAIL_INTERFACE_COMP"));
    option.put("OPT_ZC_MAIL_INTERFACE_PASS", AsOptionMeta.getOptVal("OPT_ZC_MAIL_INTERFACE_PASS"));
    String code = AsOptionMeta.getOptVal("OPT_ZC_MAIL_INTERFACE_CODE");
    if (code != null) {
      String[] codes = code.split(";");
      for (int i = 0; i < codes.length; i++) {
        String[] str = codes[i].split(",");
        if (str.length == 2) {
          option.put("C-" + str[0].trim(), str[1].trim());
        }
      }
    }

    String time = AsOptionMeta.getOptVal("OPT_ZC_MAIL_INTERFACE_TIME");
    if (time != null) {
      String[] times = time.split(";");
      for (int i = 0; i < times.length; i++) {
        String[] str = times[i].split(",");
        if (str.length == 2) {
          option.put("T-" + str[0].trim(), str[1].trim());
        }
      }
    }

    try {
      bulletin = this.getIZcEbBulletinServiceDelegate().publishBulletinFN(bulletin, WorkEnv.getInstance().getWebRoot(), option, requestMeta);
    } catch (Exception e) {

      UIUtilities.showStaickTraceDialog(e, this, "错误", "公告发布失败!");

      this.listCursor.setCurrentObject(bulletin);

      this.oldBulletin = (ZcEbBulletin) ObjectUtil.deepCopy(bulletin);

      return false;
    }

    this.listCursor.setCurrentObject(bulletin);

    this.oldBulletin = (ZcEbBulletin) ObjectUtil.deepCopy(bulletin);

    this.listPanel.refreshCurrentTabData();

    JOptionPane.showMessageDialog(self, "发布成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

    updateFieldEditorsEditable();

    setButtonStatus();

    refreshMainData();

    return true;
  }

  protected String checkBeforePublish() {

    // TODO Auto-generated method stub

    return null;

  }

  protected void doTrace() {

    ZcEbBulletin bulletin = (ZcEbBulletin) this.getEditingObject();

    if (bulletin == null) {

      bulletin = (ZcEbBulletin) listCursor.getCurrentObject();

    }

    ZcUtil.showTraceDialog(bulletin, getCompId());

  }

  public void doPrevious() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave(true)) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldBulletin);

      }

    }

    listCursor.previous();

    refreshData();

    setButtonStatus();

  }

  public void doNext() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave(true)) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldBulletin);

      }

    }

    listCursor.next();

    refreshData();

    setButtonStatus();

  }

  public void doReplaceBookMarks() {

    //    this.loadMoldButton.setEnabled(false);//填充完之后设为不可用
    this.loadMoldButton.setEnabled(false);//填充完之后设为不可用

    if (replaceValue != null && !replaceValue.equals("")) {
      wordPane.replaceBookMarks(this.replaceValue);
    }
    //WordFileUtil.delete(this.fileName);

    wordPane.save(this.fileName);

    ifRePalceBookMark = true;

    ifLoadMold = false;

  }

  public void doExit() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave(true)) {

          return;

        }

      }

    }

    closeAllWordPanel();

    this.parent.dispose();

  }

  public void closeAllWordPanel() {
    closeWordPanel(wordPane, false);
    //	  closeWordPanel(wordPaneFile1, false);
    //	  closeWordPanel(wordPaneFile2, false);
    closeWordPanel(wordPaneZb, false);

  }

  public synchronized void closeWordPanel(WordPane wp, boolean isSave) {
    if (wp != null && wp.isDocOpened()) {
      wp.close(isSave);
    }
  }

  public String getOpiWay() {
    return null;
  }

  public String getSqlMapSelectedProj() {

    return "ZcEbProj.getZcEbProjForGeneralBul";

  }

  //  public void setSqlMapSelectedProj(String sqlMapSelectedProj) {
  //
  //    this.sqlMapSelectedProj = sqlMapSelectedProj;
  //
  //  }

  public String getSqlMapSelectedMold() {

    return "ZcEbBulletinWordMold.getZcEbBulletinWordMoldBid";

  }

  //  public void setSqlMapSelectedMold(String sqlMapSelectedMold) {
  //
  //    this.sqlMapSelectedMold = sqlMapSelectedMold;
  //
  //  }

  public void setLoadMoldButtonStatus() {

    if (this.fieldZcMakeCode == null || this.fieldZcMakeCode.getValue() == null || this.fieldZcMakeCode.getValue().toString().length() == 0

    || fieldMoldName == null || fieldMoldName.getValue() == null || fieldMoldName.getValue().toString().length() == 0) {

      this.loadMoldButton.setEnabled(false);

    } else {

      this.loadMoldButton.setEnabled(true);

    }

  }

  protected void refreshWordPane(ZcEbBulletin bulletin) {

    closeWordPanel(wordPane, true);

    String fileID = bulletin.getFileID();

    if (fileID != null && !fileID.equals("")) {

      this.fileName = WordFileUtil.loadMold(fileID);

    } else {

      this.fileName = WordFileUtil.loadDefaultMold();

    }

    wordPane.openAndReadOnly(this.fileName, ZcSettingConstants.WORD_PASSWORD);

  }

  protected void refreshWordPaneForPub(ZcEbBulletin bulletin) {

    closeWordPanel(wordPane, false);

    wordPane.openAndReadOnly(this.fileName, ZcSettingConstants.WORD_PASSWORD);

  }

  private void refreshWordPaneFile1(ZcEbBulletin bulletin) {

    if (bulletin == null)
      return;

    closeWordPanel(wordPaneFile1, false);

    String fileID = bulletin.getFileId1();

    if (fileID != null && !fileID.equals("")) {

      this.fileName1 = WordFileUtil.loadMold(fileID);

    } else {

      this.fileName1 = WordFileUtil.loadDefaultMold();

    }

    wordPaneFile1.openAndReadOnly(this.fileName1, ZcSettingConstants.WORD_PASSWORD);

  }

  private void refreshWordPaneFile2(ZcEbBulletin bulletin) {

    if (bulletin == null)
      return;

    closeWordPanel(wordPaneFile2, false);

    String fileID = bulletin.getFileId2();

    if (fileID != null && !fileID.equals("")) {

      this.fileName2 = WordFileUtil.loadMold(fileID);

    } else {

      this.fileName2 = WordFileUtil.loadDefaultMold();

    }

    wordPaneFile2.openAndReadOnly(this.fileName2, ZcSettingConstants.WORD_PASSWORD);

  }

  protected void addButtonMenu(String tabStatus) {

    toolBar.add(editButton);

    toolBar.add(loadMoldButton);

    toolBar.add(saveButton);

    toolBar.add(deleteButton);

    //    toolBar.add(releaseButton);

    toolBar.add(downloadButton);

    toolBar.add(traceButton);

    toolBar.add(sendButton);

    toolBar.add(callbackButton);

    toolBar.add(suggestPassButton);

    toolBar.add(auditPassButton);

    toolBar.add(agreeButton);

    toolBar.add(disagreeButton);

    toolBar.add(sendToXieBanButton);

    toolBar.add(unAuditButton);

    toolBar.add(unTreadButton);

    toolBar.add(isSendToNextButton);

    toolBar.add(sendRecordButton);

    toolBar.add(printButton);

    toolBar.add(viewTrackRevisionsButton);

    toolBar.add(disTrackRevisionsButton);

    toolBar.add(openNotepadButton);

    toolBar.add(overxxjjButton);
    toolBar.add(overxxzjButton);

    toolBar.add(exitButton);

    toolBar.add(helpButton);

  }

  /**

   * 选择项目外部部件信息

   * @author admin

   *

   */

  protected class ZcEbProjFnHandler implements IForeignEntityHandler {

    protected String columNames[];

    public ZcEbProjFnHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {
      selectProj(selectedDatas);

    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcEbProj rowData = (ZcEbProj) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getProjCode();

        data[i][col++] = rowData.getProjName();

        data[i][col++] = rowData.getProjSum();

        data[i][col++] = AsValDataCache.getName("ZC_EB_PUR_TYPE", rowData.getPurType());

        data[i][col++] = CompanyDataCache.getName(rowData.getCoCode());

        data[i][col++] = rowData.getManager();

        data[i][col++] = rowData.getPhone();

        data[i][col++] = rowData.getEmail();

        data[i][col++] = rowData.getFax();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        @Override
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

  }

  @Override
  public boolean getIfLoadMold() {

    // TODO Auto-generated method stub

    return this.ifLoadMold;

  }

  @Override
  public boolean getIfRePalceBookMark() {

    // TODO Auto-generated method stub

    return this.ifRePalceBookMark;

  }

  @Override
  public ListCursor getListCursor() {

    // TODO Auto-generated method stub

    return this.listCursor;

  }

  @Override
  public JPanel getWindowObject() {

    // TODO Auto-generated method stub

    return this;

  }

  @Override
  public WordPane getWordPane() {

    // TODO Auto-generated method stub

    return this.wordPane;

  }

  @Override
  public void setIfLoadMold(boolean ifLoadMold) {

    // TODO Auto-generated method stub

    this.ifLoadMold = ifLoadMold;

    if (this.ifLoadMold) {

      this.setLoadMoldButtonStatus();

    }

  }

  @Override
  public void setIfRePalceBookMark(boolean ifRePalceBookMark) {

    // TODO Auto-generated method stub

    this.ifRePalceBookMark = ifRePalceBookMark;

  }

  @Override
  public String getWordMoldViewId() {

    // TODO Auto-generated method stub

    ZcEbBulletin bulletin = (ZcEbBulletin) listCursor.getCurrentObject();

    return bulletin.getProjCode();

  }

  @Override
  public void setMoldName(String wordName) {

    // TODO Auto-generated method stub

    ZcEbBulletin bulletin = (ZcEbBulletin) listCursor.getCurrentObject();

    bulletin.setMoldName(wordName);

    fieldMoldName.setEditObject(bulletin);

  }

  @Override
  public void setReplaceValue(String replaceValue) {

    // TODO Auto-generated method stub

    this.replaceValue = replaceValue;

  }

  @Override
  public void setWordMold(ZcEbBulletinWordMold bulletinMold) {

    setLoadMoldButtonStatus();

    ZcEbBulletin bulletin = (ZcEbBulletin) listCursor.getCurrentObject();

    bulletin.setMoldCode(bulletinMold.getBulletinMoldCode());

    refreshZcEbPlan();

    this.wordMold = bulletinMold;

  }

  @Override
  public String getPackCode() {

    // TODO Auto-generated method stub

    return null;

  }

  private boolean readFileByChar(ZcEbBulletin bulletin) {

    File f = new File(AsOptionMeta.getOptVal("OPT_ZC_HTML_FILE_TEMP") + bulletin.getFileID() );

    for (int i = 0; i < 2; i++) {
      if (f.exists()) {
        Reader r = null;
        int charread = 0;
        StringBuffer sb = new StringBuffer();
        try {
          FileInputStream fis = new FileInputStream(f);
          r = new InputStreamReader(fis, "GBK");
          char[] tempchars = new char[fis.available()];
          while ((charread = r.read(tempchars)) != -1) {
            sb.append(tempchars);
          }
        } catch (Exception e) {
          sb = null;
        } finally {
          if (r != null) {
            try {
              r.close();
            } catch (IOException e) {
              logger.error(e);
            }
          }
        }
        if (sb == null || sb.length() == 0) {
          return false;
        }

        int from = 0;
        while ((from = sb.indexOf("<!--", from + 1)) > 0) {
          int to = sb.indexOf("-->", from + 1);
          int from2 = sb.indexOf("<!--", from + 1);
          if (to > 0 && (to < from2 || from2 < 0)) {
            sb.replace(from, to + 3, "");
            from--;
          }
        }

        bulletin.setFile(sb);

        String str = sb.toString();

        bulletin.setFileContent(str);
        return true;
      }
    }
    return true;
  }

  public List<String> getReplaceValue(String bulletinMoldCode) {

    if (bulletinMoldCode == null) {

      return null;

    }

    ZcEbBulletinWordMold bulletinMold = zcEbBulletinWordMoldServiceDelegate.selectMoldByPrimaryKey(bulletinMoldCode, requestMeta);

    if (bulletinMold == null) {

      return null;

    }

    List<String> bookmark = new ArrayList<String>();
    List bulletinMoldParamList = zcEbBulletinWordMoldServiceDelegate.getZcEbBulletinWordMoldParam(requestMeta, bulletinMold.getBulletinMoldCode());

    for (Iterator it = bulletinMoldParamList.iterator(); it.hasNext();) {

      ZcEbBulletinWordMoldParam bulletinMoldParam = (ZcEbBulletinWordMoldParam) it.next();
      bookmark.add("<a name=\"" + bulletinMoldParam.getParamName() + "\"></a>");

      if (bulletinMoldParam.getParamType() == null) {

        continue;

      }

      Map paramMap = new HashMap();

      paramMap.put("fieldName", bulletinMoldParam.getFieldName());

      paramMap.put("tableName", bulletinMold.getDataView());

      paramMap.put("projCode", getWordMoldViewId());

      paramMap.put("packCode", getPackCode());

      try {

        String paramValue = handlera.getParamValue(paramMap, getWordMoldViewId(), bulletinMoldParam.getParamType(), bulletinMoldParam

        .getFieldName());

        paramValue = paramValue == null ? "" : paramValue;

        bulletinMoldParam.setParamValue(paramValue);

      } catch (Exception ex) {
        return null;

      }

    }

    //      this.setParamList(bulletinMoldParamList);

    replaceValue = dogetMold(bulletinMoldParamList, bulletinMold);

    setReplaceValue(replaceValue);
    return bookmark;
  }

  public String dogetMold(List valueList, ZcEbBulletinWordMold bulletinMold) {

    StringBuffer sb = new StringBuffer();

    for (Iterator it = valueList.iterator(); it.hasNext();) {

      ZcEbBulletinWordMoldParam zcEbBulletinWordMoldParam = (ZcEbBulletinWordMoldParam) it.next();
      if (zcEbBulletinWordMoldParam.getParamValue() == null || "".equals(zcEbBulletinWordMoldParam.getParamValue())) {
        continue;
      }

      sb.append(zcEbBulletinWordMoldParam.getParamName());

      sb.append("$$$$$");

      sb.append(zcEbBulletinWordMoldParam.getParamValue());

      sb.append("@@@@@");

    }

    return sb.toString();

  }

  protected String fieldName() {
    return ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME;
  }

  protected void refreshExtracButton() {

  }

  protected boolean checkBeforeSend() {

    ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();

    StringBuilder errorInfo = new StringBuilder();
    if (isShowPanel) {
//      if (bulletin.getFileId1() == null || "".equals(bulletin.getFileId1().trim()) || fileId1.getValue() == null) {
//        errorInfo.append("[抽取专家申请表]不能为空 \n");
//      }
//      if (bulletin.getFileId2() == null || "".equals(bulletin.getFileId2().trim()) || fileId2.getValue() == null) {
//        errorInfo.append("[抽取纪检申请表]不能为空 \n");
//      }
    }

    if (errorInfo.length() != 0) {

      JOptionPane.showMessageDialog(this.parent, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);

      return false;

    }
    return true;
  }

}
