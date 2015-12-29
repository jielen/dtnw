package com.ufgov.zc.client.zc.project.integration.zbbook;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.ufgov.zc.client.component.button.DisTrackRevisionsButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.ViewTrackRevisionsButton;
import com.ufgov.zc.client.zc.project.integration.ZcEbProjectEditPanel;
import com.ufgov.zc.client.zc.project.integration.zbbook.services.ResponsePointServices;
import com.ufgov.zc.client.zc.project.integration.zbbook.services.ZbBookOperatorsService;
import com.ufgov.zc.client.zc.project.integration_dt.ZcEbProjectEditPanel_dt;
import com.ufgov.zc.client.zc.ztb.component.CommonAction;
import com.ufgov.zc.client.zc.ztb.component.MainPanel;
import com.ufgov.zc.client.zc.ztb.component.ZTBButton;
import com.ufgov.zc.client.zc.ztb.dao.FileImportDao;
import com.ufgov.zc.client.zc.ztb.model.BusinessProject;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;
import com.ufgov.zc.client.zc.ztb.service.ServerProjectService;
import com.ufgov.zc.client.zc.ztb.table.common.PubFunc;
import com.ufgov.zc.client.zc.ztb.util.ActionMaps;
import com.ufgov.zc.client.zc.ztb.util.FileChooseFilter;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.client.zc.ztb.util.PubFunction;
import com.ufgov.zc.client.zc.ztb.util.ZipFilePubFunc;
import com.ufgov.zc.common.zc.model.ZcEbProj;

public class ZBPanel extends MainPanel {
  private static final long serialVersionUID = -2022098177537405989L;

  private ZBPanel self = this;

  private ZbBookOperatorsService bookService = new ZbBookOperatorsService(this);

  private ZTBButton moveupBtn;

  private ZTBButton movedownBtn;

  private ZcEbProj zcEbProj;

  private ZcEbProjectEditPanel panel;
  

  private ZcEbProjectEditPanel_dt panel_dt;


  //显示痕迹
  private FuncButton viewTrackRevisionsButton = new ViewTrackRevisionsButton();

  //隐藏痕迹
  private FuncButton disTrackRevisionsButton = new DisTrackRevisionsButton();

  public ZBPanel(Window parent, ZcEbProjectEditPanel zcEbProjectEditPanel) {
    WHO_I_AM = GV.DIS_ZHAOBIAO_ONLY;
    parentEntity = parent;
    panel = zcEbProjectEditPanel;
    this.zcEbProj = zcEbProjectEditPanel.getOldProj();
    ActionMaps.initZB();
    initTplDirectory();
    initToolBar();
    initPanel();
    setButtonStatus(zcEbProjectEditPanel);

  }

  public ZBPanel(Window parent, ZcEbProjectEditPanel_dt editPanel,boolean isDt) {
	// TCJLODO Auto-generated constructor stub
	    WHO_I_AM = GV.DIS_ZHAOBIAO_ONLY;
	    parentEntity = parent;
	    panel_dt = editPanel;
	    this.zcEbProj = editPanel.getOldProj();
	    ActionMaps.initZB();
	    initTplDirectory();
	    initToolBar();
	    initPanel();
	    setButtonStatus_dt(editPanel);

}

/**
   * 根据采购方式初始化标书模板目录结构，这样只需要参考缺省的目录结构创建模板就好了
   */
  private void initTplDirectory() {
    String[] types = GV.getTplBelongBidWayList();
    for (String type : types) {
      StringBuffer buff = GV.getTemplateFileDir_FromRoot();
      String path = buff.append(type).append("_TPL").toString();
      File file = new File(path);
      if (!file.exists() || !file.isDirectory()) {
        file.mkdirs();
      }
      initAptDirectory(file.getAbsolutePath());
    }
  }

  /**
   * 初始化每种招标方式下具体应用类型目录结构
   *
   * @param typePath
   */
  private void initAptDirectory(String typePath) {
    String[] apps = GV.getAppTypeList();
    for (String app : apps) {
      String path = typePath + File.separator + app + "_APT";
      File file = new File(path);
      if (!file.exists() || !file.isDirectory()) {
        file.mkdirs();
      }
      initExampleDirectory(file.getAbsolutePath());
    }
  }

  /**
   * 初始化具体的标书模板目录结构示例
   *
   * @param aptPath
   * @update qianmingjin
   * @update date 2012-4-1
   */
  private void initExampleDirectory(String aptPath) {
    String gkzb = GV.getSimpleMsg("PURTYPE_GKZB") + "_TPL";
    String yqzb = GV.getSimpleMsg("PURTYPE_YQZB") + "_TPL";
    String jztp = GV.getSimpleMsg("PURTYPE_JZXTP") + "_TPL";
    String dyly = GV.getSimpleMsg("PURTYPE_DYLYCG") + "_TPL";
    String xygh = GV.getSimpleMsg("PURTYPE_XYGHECJJ") + "_TPL";
    String base = aptPath + File.separator + "示例模板目录";
    String file1;
    String file2;
    if (aptPath.indexOf(gkzb) != -1) {
      file1 = base + File.separator + "招标文件";
      file2 = base + File.separator + "投标文件";
    } else if (aptPath.indexOf(yqzb) != -1) {
      file1 = base + File.separator + "邀请文件";
      file2 = base + File.separator + "响应文件";
    } else if (aptPath.indexOf(jztp) != -1) {
      file1 = base + File.separator + "竞争性谈判文件";
      file2 = base + File.separator + "响应文件";
    } else if (aptPath.indexOf(dyly) != -1) {
      file1 = base + File.separator + "单一来源采购文件";
      file2 = base + File.separator + "响应文件";
    } else if (aptPath.indexOf(xygh) != -1) {
      file1 = base + File.separator + "招标文件";
      file2 = base + File.separator + "投标文件";
    } else {
      file1 = base + File.separator + "招标文件";
      file2 = base + File.separator + "响应文件";
    }
    File fileOne = new File(file1);
    File fileTwo = new File(file2);
    fileOne.mkdirs();
    fileTwo.mkdirs();
  }

  public void setSave(boolean flag) {
    super.setSaveBtn(flag);
  }

  /**
   * 初始化顶部工具栏
   */
  public void initToolBar() {
    super.initToolBar();
    toolBar.addSeparator();
    //ZTBButton localConfigBtn = new ZTBButton("dbconfig");
    //    toolBar.add(localConfigBtn);
    CommonAction dbconfig = (CommonAction) ActionMaps.getActionsMap().get("toolLocalConfig");
    dbconfig.setComponent(this);
    //localConfigBtn.addActionListener(dbconfig);
    // ZTBButton importZbFileBtn = new ZTBButton("importZbFileBtn");
    //    toolBar.add(importZbFileBtn);
    //addImportZbFileBtnAction(importZbFileBtn);
    if (getBtnControl()) {
      ZTBButton dbimportBtn = new ZTBButton("dbimport");
      toolBar.add(dbimportBtn);
      this.addImportBtnListener(dbimportBtn);
    }
    //导入需求确认的信息，导入需求确认文件
    ZTBButton dbImpReqBtn = new ZTBButton("dbImpReq");//导入需求确认的信息
    toolBar.add(dbImpReqBtn);
    CommonAction bdCombine = (CommonAction) ActionMaps.getActionsMap().get("dbImportRequirement");
    bdCombine.setComponent(this);
    dbImpReqBtn.addActionListener(bdCombine);
    //导入评标方法按钮
    ZTBButton dbFormulaBtn = new ZTBButton("dbFormula");//导入需求确认的信息
    toolBar.add(dbFormulaBtn);
    CommonAction formulaAction = (CommonAction) ActionMaps.getActionsMap().get("dbImportFormula");
    formulaAction.setComponent(this);
    dbFormulaBtn.addActionListener(formulaAction);

    ZTBButton makeProjPlanBtn = new ZTBButton("makeProjPlanBtn");
    toolBar.add(makeProjPlanBtn);
    this.addBtActionListener(makeProjPlanBtn);
    toolBar.addSeparator();
    ZTBButton fillMoldBtn = new ZTBButton("fillMold");
    toolBar.add(fillMoldBtn);
    CommonAction fillMoldAction = (CommonAction) ActionMaps.getActionsMap().get("fillMold");
    fillMoldAction.setComponent(this);
    fillMoldBtn.addActionListener(fillMoldAction);
    ZTBButton lockWordBtn = new ZTBButton("lockWord");
    toolBar.add(lockWordBtn);
    CommonAction lockWordAction = (CommonAction) ActionMaps.getActionsMap().get("lockWord");
    lockWordAction.setComponent(this);
    lockWordBtn.addActionListener(lockWordAction);
    ZTBButton unLockWordBtn = new ZTBButton("unLockWord");
    toolBar.add(unLockWordBtn);
    CommonAction unLockWordAction = (CommonAction) ActionMaps.getActionsMap().get("unLockWord");
    unLockWordAction.setComponent(this);
    unLockWordBtn.addActionListener(unLockWordAction);
    if (getBtnControl()) {
      ZTBButton uploadBtn = new ZTBButton("uploadZBFile");
      toolBar.add(uploadBtn);
      CommonAction uploadZBFileAction = (CommonAction) ActionMaps.getActionsMap().get("uploadZBFile");
      uploadZBFileAction.setComponent(this);
      uploadBtn.addActionListener(uploadZBFileAction);
    }
    ZTBButton fetchPubKeyBtn = new ZTBButton("fetchPubKey");
    //    toolBar.add(fetchPubKeyBtn);
    CommonAction fetchPubKeyAction = (CommonAction) ActionMaps.getActionsMap().get("fetchPubKey");
    fetchPubKeyAction.setComponent(this);
    fetchPubKeyBtn.addActionListener(fetchPubKeyAction);
    // ZTBButton exportZBFileBtn = new ZTBButton("exportZBFileBtn");
    //toolBar.add(exportZBFileBtn);
    CommonAction exportZBFileAction = (CommonAction) ActionMaps.getActionsMap().get("exportZBFile");
    exportZBFileAction.setComponent(this);
    // exportZBFileBtn.addActionListener(exportZBFileAction);
    ZTBButton uploadZBFileBtn = new ZTBButton("uploadZBFileBtn");
    toolBar.add(uploadZBFileBtn);
    CommonAction uploadZBFileAction = (CommonAction) ActionMaps.getActionsMap().get("uploadZBFile");
    uploadZBFileAction.setComponent(this);
    uploadZBFileBtn.addActionListener(uploadZBFileAction);
    toolBar.add(viewTrackRevisionsButton);
    toolBar.add(disTrackRevisionsButton);
    viewTrackRevisionsButton.setVisible(false);
    disTrackRevisionsButton.setVisible(true);
    viewTrackRevisionsButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 显示痕迹
        if (wordPane != null) {
          wordPane.viewTrackRevisions(true);
          viewTrackRevisionsButton.setVisible(false);
          disTrackRevisionsButton.setVisible(true);
        }
      }
    });
    disTrackRevisionsButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 隐藏痕迹
        if (wordPane != null) {
          wordPane.viewTrackRevisions(false);
          viewTrackRevisionsButton.setVisible(true);
          disTrackRevisionsButton.setVisible(false);
        }
      }
    });
  }

  /**
   * 从本地加载招标书
   *
   * @param bt
   */
  private void addImportZbFileBtnAction(ZTBButton bt) {
    bt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(GV.getHistoryDirectory()));
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setFileFilter(new FileChooseFilter("ztb"));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
          File selFile = fileChooser.getSelectedFile();
          String filePath = selFile.getAbsolutePath();
          GV.setHistoryDirectory(selFile.getParent());
          String tarDir = GV.getImportFileDir_FromRoot().append(selFile.getName().substring(0, selFile.getName().indexOf(".ztb")))
            .append(File.separator).toString();
          ZipFilePubFunc.unzipFileToDestDir(new File(filePath), tarDir);
          try {
            String xmlFilePath = getProjectTreeXmlPath(tarDir);
            (new ServerProjectService()).importProjectFromLocal(xmlFilePath);
            self.refreshLeftFilesTreePanel();
          } catch (Exception e1) {
            JOptionPane.showMessageDialog(null, "从本地加载招标书出错，原因：\n" + e1.getMessage());
            e1.printStackTrace();
          }
        }
      }
    });
  }

  String getProjectTreeXmlPath(String tarDir) throws Exception {
    File file = new File(tarDir);
    String parentPath = file.getParentFile().getAbsolutePath();
    File[] files = file.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (files[i].getName().endsWith(GV.PROJECT_INFO_XML_SUFFIX)) {
        FileInputStream fis = new FileInputStream(files[i].getAbsolutePath());
        BusinessProject bp = (BusinessProject) FileImportDao.getInstance().readProject(fis);
        if (bp.getNo() == null) {
          bp.setNo(files[i].getName().substring(0, files[i].getName().indexOf(GV.PROJECT_INFO_XML_SUFFIX)));
        }
        fis.close();
        File renameFile = new File(parentPath + File.separator + bp.getNo());
        if (renameFile.exists() && !file.getAbsolutePath().equals(renameFile.getAbsolutePath())) {
          PubFunction.deleteFile(renameFile.getAbsolutePath());
        }
        file.renameTo(renameFile);
        this.loadRightInitInfoPanel(bp.getNo());
        StringBuffer pbuff = new StringBuffer(parentPath);
        pbuff.append(File.separator);
        pbuff.append(bp.getNo());
        pbuff.append(File.separator);
        pbuff.append(bp.getNo());
        pbuff.append(GV.SUFFIX_XML);
        return pbuff.toString();
      }
    }
    return null;
  }

  private void addImportBtnListener(ZTBButton bt) {
    bt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          doExecuteProjectImporting();
        } catch (Exception e1) {
          JOptionPane.showMessageDialog(null, "创建项目导入列表出错，原因：\n" + e1.getMessage());
          e1.printStackTrace();
        }
      }
    });
  }

  /**
   * 导入项目
   *
   * @throws Exception
   */
  void doExecuteProjectImporting() throws Exception {
    JDialog dialog = new JDialog((JFrame) this.getRootPane().getParent(), GV.DB_IMPORT_PROJECT_LIST, true);
    dialog.setLayout(new BorderLayout());
    dialog.add(new ProjectImportPanel(this, dialog), BorderLayout.CENTER);
    dialog.setPreferredSize(new Dimension(800, 600));
    PubFunc.setShowInMiddle(dialog);
    dialog.pack();
    dialog.setVisible(true);
    dialog.toFront();
  }

  private void addBtActionListener(ZTBButton bt) {
    bt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        MainPanel mainPanel = (MainPanel) self;
        SmartTreeNode rootNode = mainPanel.getSingleSeletionTree().getTreeNode();
        SmartTreeNode projNode = null;
        if (rootNode.getChildCount() > 0) {
          projNode = (SmartTreeNode) rootNode.getChildAt(0);
          JFrame planFrame = new JFrame();
          planFrame.setLayout(new BorderLayout());
          planFrame.setTitle("项目执行计划制定");
          Map<String, String> paras = new HashMap<String, String>();
          paras.put("PROJCODE", projNode.getNodeCode());
          paras.put("PROJNAME", projNode.getNodeName());
          planFrame.add(new ZcEbPlanEditPanel(planFrame, mainPanel, paras), BorderLayout.CENTER);
          planFrame.pack();
          planFrame.setSize(new Dimension(740, 580));
          planFrame.setLocationRelativeTo(null);
          planFrame.setVisible(true);
        }
      }
    });
  }

  /**
   * 载入项目招标书
   *
   * @param projCode
   */
  public void importProject(String projCode, boolean zbBookRedoFlag) throws Exception {
    SmartTreeNode rootNode = this.getSingleSeletionTree().getTreeNode();
    bookService.setRootNode(rootNode);
    bookService.toSynLocalAndServer(projCode, zbBookRedoFlag); //下载标书文件并拆开
    ResponsePointServices.setProjNode(PubFunction.getNeedingNodeInChildren(rootNode, GV.NODE_TYPE_PROJECT));
    this.getSplitPane().setDividerLocation(239);
    this.loadRightInitInfoPanel(projCode);
    this.refreshLeftFilesTreePanel();
    this.getSplitPane().setDividerLocation(240);
  }

  /**
   * 显示模板选择panel
   */
  public void doShowTemplatePanel() throws Exception {
    JDialog dialog = new JDialog((JFrame) this.getRootPane().getParent(), GV.AVAILABLE_TEMPLATES, true);
    dialog.setLayout(new BorderLayout());
    dialog.add(new TemplateCopyPanel(this, this.getSingleSeletionTree().getCurrentNode(), dialog), BorderLayout.CENTER);
    int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    dialog.setPreferredSize(new Dimension(width - 150, height - 150));
    PubFunc.setShowInMiddle(dialog);
    dialog.pack();
    dialog.setVisible(true);
    dialog.toFront();
  }

  /**
   * 导入需求
   */
  public void doExecuteRequirementImport() throws Exception {
    if (this.getWordPane() == null || !this.getWordPane().isDocOpened()) {
      String tigMeg = GV.getOperateMsg("dbImportRequirement.noDocOpened", null);
      GV.showMessageDialog(this, tigMeg);
      return;
    }
    JDialog dialog = new JDialog((JFrame) this.getSingleSeletionTree().getRootPane().getParent(), GV.DB_IMPORT_BAGDETAILS_FILE_LIST, true);
    dialog.setLayout(new BorderLayout());
    try {
      dialog.add(new RequirementImportPanel(this, dialog), BorderLayout.CENTER);
    } catch (Exception e) {
      GV.showMessageDialog(this, e.getMessage());
      e.printStackTrace();
      return;
    }
    dialog.setPreferredSize(new Dimension(800, 600));
    PubFunc.setShowInMiddle(dialog);
    dialog.pack();
    dialog.setVisible(true);
    dialog.toFront();
  }

  /**
   * 导入评标方法
   */
  public void doExecuteFormulaImport() throws Exception {
    if (this.getWordPane() == null || !this.getWordPane().isDocOpened()) {
      String tigMeg = GV.getOperateMsg("dbImportRequirement.noDocOpened", null);
      GV.showMessageDialog(this, tigMeg);
      return;
    }

    JDialog dialog = new JDialog((JFrame) this.getSingleSeletionTree().getRootPane().getParent(), GV.DB_IMPORT_BAGDETAILS_FILE_LIST, true);
    dialog.setLayout(new BorderLayout());
    try {
      dialog.add(new FormulaImportPanel(this, dialog), BorderLayout.CENTER);
    } catch (Exception e) {
      GV.showMessageDialog(this, e.getMessage());
      e.printStackTrace();
      return;
    }
    dialog.setPreferredSize(new Dimension(800, 600));
    PubFunc.setShowInMiddle(dialog);
    dialog.pack();
    dialog.setVisible(true);
    dialog.toFront();
  }

  /**
   * 保存招标书模板到数据库
   *
   * @param
   * @throws Exception
   */
  public void doExecuteTemplateSave() throws Exception {
    JDialog dialog = new JDialog((JFrame) this.getRootPane().getParent(), "招标书模板上传", true);
    dialog.setLayout(new BorderLayout());
    dialog.add(new TemplateSavePanel(this.getSingleSeletionTree(), dialog), BorderLayout.CENTER);
    dialog.setPreferredSize(new Dimension(420, 340));
    PubFunc.setShowInMiddle(dialog);
    dialog.pack();
    dialog.setResizable(false);
    dialog.setVisible(true);
    dialog.toFront();
  }

  private boolean getBtnControl() {
    return false;
  }

  public ZTBButton getMoveupBtn() {
    return moveupBtn;
  }

  public void setMoveupBtn(ZTBButton moveupBtn) {
    this.moveupBtn = moveupBtn;
  }

  public ZTBButton getMovedownBtn() {
    return movedownBtn;
  }

  public void setMovedownBtn(ZTBButton movedownBtn) {
    this.movedownBtn = movedownBtn;
  }

  public ZbBookOperatorsService getBookService() {
    return bookService;
  }

  public void setBookService(ZbBookOperatorsService bookService) {
    this.bookService = bookService;
  }

  protected void setButtonStatus(ZcEbProjectEditPanel zcEbProjectEditPanel) {
    Long processInstId = zcEbProj.getProcessInstId();
    JButton editButton = zcEbProjectEditPanel.getEditButton();
    JButton saveButton = zcEbProjectEditPanel.getSaveButton();
    if (processInstId != null && processInstId.longValue() >= 0) {
      // 开始走流程的单据
      setButtonEnable(false);
      canEdit = false;
    }
    if ((editButton.isEnabled() && editButton.isVisible()) || (saveButton.isEnabled() && saveButton.isVisible())) {
      setButtonEnable(true); //设置按钮及左面板树是不是可以使用右键编辑
      canEdit = true;
    }
  }
  protected void setButtonStatus_dt(ZcEbProjectEditPanel_dt zcEbProjectEditPanel) {
//	    Long processInstId = zcEbProj.getProcessInstId();
//	    JButton editButton = zcEbProjectEditPanel.getEditButton();
//	    JButton saveButton = zcEbProjectEditPanel.getSaveButton();
//	    if (processInstId != null && processInstId.longValue() >= 0) {
//	      // 开始走流程的单据
//	      setButtonEnable(false);
//	      canEdit = false;
//	    }
//	    if ((editButton.isEnabled() && editButton.isVisible()) || (saveButton.isEnabled() && saveButton.isVisible())) {
//	      setButtonEnable(true); //设置按钮及左面板树是不是可以使用右键编辑
//	      canEdit = true;
//	    }
	  setButtonEnable(zcEbProjectEditPanel.isEditing());
	  canEdit=zcEbProjectEditPanel.isEditing();
	  }
  public void setButtonEnable(boolean b) {
    // 新增单据,草稿单据或不挂接工作流的单据
    Component[] funcs = toolBar.getComponents();
    //制定项目执行计划可以查看，不让修改.导出招标书到本地,改按钮可用。
    for (Component func : funcs) {
      if (func instanceof ZTBButton) {
        ZTBButton ztbFunc = (ZTBButton) func;
        String buttonStr = ztbFunc.getText();
        if (buttonStr.equals(GV.getButtonText().get("makeProjPlanBtn")) || buttonStr.equals(GV.getButtonText().get("exportZBFileBtn"))) {
          func.setEnabled(true);
        } else {
          func.setEnabled(b);
        }
      }
    }
    openAndProtect = !b;
    setRightMouseEnable(b); //设置左面板树是不是可以使用右键编辑
    try {
      this.refreshLeftFilesTreePanel();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ZcEbProj getZcEbProj() {
    return zcEbProj;
  }

  public ZcEbProjectEditPanel getPanel() {
    return panel;
  }
  public ZcEbProjectEditPanel_dt getPanel_dt() {
	    return panel_dt;
	  }
}
