package com.ufgov.zc.client.zc.ztb.component;

import com.ufgov.zc.client.zc.ztb.JobThreads;
import com.ufgov.zc.client.zc.ztb.Threads.BidBookImpProgressBar;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;
import com.ufgov.zc.client.zc.ztb.service.FilePrepareUploadModelBuilder;
import com.ufgov.zc.client.zc.ztb.service.ImportTbFileService;
import com.ufgov.zc.client.zc.ztb.util.ActionMaps;
import com.ufgov.zc.client.zc.ztb.util.FileChooseFilter;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.client.zc.ztb.util.PubFunction;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TBPanel extends MainPanel {
  private static final long serialVersionUID = 5090641157665230959L;

  //不登陆且不再继续提示
  private static boolean notLoginNotAlertAgain = false;

  //自动登录且不再提示
  private static boolean autoLoginNotAlertAgain = false;

  private MainPanel mainPanel = this;

  private JButton impBtn;

  private static JButton loginBtn;

  private JButton responsePointBtn;

  //横向word页面
  private JButton rotateWordPageHBtn;

  //纵向word页面
  private JButton rotateWordPageVBtn;

  private JButton exportPackBidFile;

  private JButton exportAndUploadTbFile;

  private JButton goonUploadTbFile;

  private JButton zipTestBtn;

  private JButton encodeBtn;

  private JButton decodeBtn;

  private JButton mergeBtn;

  private ImportTbFileService importTbFileService;

  public TBPanel(Window parent) throws Exception {
    WHO_I_AM = GV.DIS_TOUBIAO_ONLY;
    parentEntity = parent;
    ActionMaps.initTB();
    importTbFileService = new ImportTbFileService();
    initToolBar();
    initPanel();
    preToLoadProjInfo();
//    initUploadFileListXml();//BY chenjl 2013-08-27 不实现在线上传标书
  }

  private void initUploadFileListXml() {
    FilePrepareUploadModelBuilder builder = new FilePrepareUploadModelBuilder();
    builder.initTheJob();
    builder.deleteFileToUploadXmlFileWithoutProjCode(ProjectInfoPanel.getBusinessProject().getNo());
  }

  public void initToolBar() {
    super.initToolBar();
    loginBtn = new JButton(GV.getTranslate().get("loginbtn"), GV.getImageIcon().get("loginbtn"));
    loginBtn.setToolTipText(GV.getImageIconTips().get("loginbtn"));
    toolBar.add(loginBtn);
    addLoginAction(loginBtn);
    toolBar.addSeparator();
    impBtn = new JButton(GV.getTranslate().get("importtb"), GV.getImageIcon().get("importtb"));
    impBtn.setToolTipText(GV.getImageIconTips().get("importtb"));
    toolBar.add(impBtn);
    addImpAction(impBtn);
    exportPackBidFile = new JButton(GV.getTranslate().get("exporttb"), GV.getImageIcon().get("exporttb"));
    toolBar.add(exportPackBidFile);
    CommonAction exportPackBidFileAction = (CommonAction) ActionMaps.getActionsMap().get("exportPack");
    exportPackBidFileAction.setComponent(this);
    exportPackBidFile.addActionListener(exportPackBidFileAction);
    exportAndUploadTbFile = new JButton(GV.getTranslate().get("uploadAndBid"), GV.getImageIcon().get("uploadAndBid"));
    toolBar.add(exportAndUploadTbFile);
    CommonAction expAndUploadFileAction = (CommonAction) ActionMaps.getActionsMap().get("uploadPack");
    expAndUploadFileAction.setComponent(this);
    exportAndUploadTbFile.addActionListener(expAndUploadFileAction);
    goonUploadTbFile = new JButton(GV.getTranslate().get("goonUploadPackAndBid"), GV.getImageIcon().get("goonUploadPackAndBid"));
    toolBar.add(goonUploadTbFile);
    CommonAction goonUploadFileAction = (CommonAction) ActionMaps.getActionsMap().get("goonUploadPackAndBid");
    goonUploadFileAction.setComponent(this);
    goonUploadTbFile.addActionListener(goonUploadFileAction);
    zipTestBtn = new JButton(GV.getTranslate().get("zipTest"), GV.getImageIcon().get("zipTest"));
    zipTestBtn.setToolTipText(GV.getImageIconTips().get("zipTest"));
    toolBar.add(zipTestBtn);
    addZipTestAction(zipTestBtn);
    toolBar.addSeparator();
    encodeBtn = new JButton(GV.getTranslate().get("encode"), GV.getImageIcon().get("encode"));
    encodeBtn.setToolTipText(GV.getImageIconTips().get("encode"));
    toolBar.add(encodeBtn);
    CommonAction encodeAction = (CommonAction) ActionMaps.getActionsMap().get("goonUploadPackAndBid");
    encodeAction.setComponent(this);
    encodeBtn.addActionListener(encodeAction);
    decodeBtn = new JButton(GV.getTranslate().get("decode"), GV.getImageIcon().get("decode"));
    decodeBtn.setToolTipText(GV.getImageIconTips().get("decode"));
    toolBar.add(decodeBtn);
    addDecodeAction(decodeBtn);
    responsePointBtn = new JButton(GV.getTranslate().get("responsepoint"), GV.getImageIcon().get("responsepoint"));
    responsePointBtn.setToolTipText(GV.getImageIconTips().get("responsepoint"));
    toolBar.add(responsePointBtn);
    addResponsePointAction(responsePointBtn);
    mergeBtn = new JButton(GV.getTranslate().get("mergeword"), GV.getImageIcon().get("mergeword"));
    mergeBtn.setToolTipText(GV.getImageIconTips().get("mergeword"));
    toolBar.add(mergeBtn);
    CommonAction mergeAction = (CommonAction) ActionMaps.getActionsMap().get("mergeWords");
    mergeAction.setComponent(this);
    mergeBtn.addActionListener(mergeAction);
    rotateWordPageHBtn = new JButton(GV.getTranslate().get("rotateword_h"), GV.getImageIcon().get("rotateword_h"));
    //toolBar.add(rotateWordPageHBtn);
    addRotateWordPageHAction(rotateWordPageHBtn);
    rotateWordPageVBtn = new JButton(GV.getTranslate().get("rotateword_v"), GV.getImageIcon().get("rotateword_v"));
    //toolBar.add(rotateWordPageVBtn);
    addRotateWordPageVAction(rotateWordPageVBtn);
  }

  private void addLoginAction(final JButton loginBtn) {
    loginBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (loginBtn.getText().indexOf(GV.getTranslate().get("logoutbtn")) >= 0) {
          String title = GV.getSimpleMsg("asking");
          int sel = JOptionPane.showConfirmDialog(null, GV.getSimpleMsg("sureToLogout"), title, 2, 2);
          if (sel == JOptionPane.CANCEL_OPTION) {
            return;
          }
          loginBtn.setText(GV.getTranslate().get("loginbtn"));
          GV.SESSION_MAP.clear();
          if (JobThreads.getPreTrantsProcess() != null) {
            JobThreads.getPreTrantsProcess().destroy();
          }
          return;
        }
        if (ProjectInfoPanel.getBusinessProject().getNo() == null) {
          JOptionPane.showMessageDialog(null, GV.getSimpleMsg("importBeforeLogin"));
          return;
        }
        notLoginNotAlertAgain = false;
        toShowLoginTypeWindow();
      }
    });
  }

  private void addRotateWordPageVAction(JButton vbt) {
    vbt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rotateWordPage("H");
      }
    });
  }

  private void addRotateWordPageHAction(JButton hbt) {
    hbt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rotateWordPage("V");
      }
    });
  }

  protected void rotateWordPage(String type) {
    this.rotateWordSelectionPage(type);
  }

  private void addResponsePointAction(JButton bt) {
    bt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        createResponseDialog();
      }
    });
  }

  private void addMergeAction(JButton bt) {
    bt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mergeWords();
      }
    });
  }

  private void mergeWords() {
  }

  private void createResponseDialog() {
    toShowRPTreeTab();
    JFrame frame = new JFrame();
    frame.setIconImage(frame.getToolkit().getImage(GV.getImageUrl("windowicon.jpg")));
    frame.add(new AddResponsePointPanel(frame, this));
    frame.setSize(new Dimension(460, 250));
    frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setTitle(GV.getSimpleMsg("responseFrameTitle"));
    frame.setMinimumSize(new Dimension(460, 250));
  }

  private void addImpAction(JButton impBtn) {
    impBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(GV.getHistoryDirectory()));
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setFileFilter(new FileChooseFilter("ztb"));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
          String filePath = fileChooser.getSelectedFile().getAbsolutePath();
          GV.setHistoryDirectory(fileChooser.getSelectedFile().getParent());
          toImportTBFile(filePath);
        }
      }
    });
  }

  protected void toImportTBFile(String filePath) {
    try {
      prepareToCheckVersion(filePath);
    } catch (Exception ee) {
      ee.printStackTrace();
      JOptionPane.showMessageDialog(null, GV.getSimpleMsg("tbBookImportErr") + ee.getMessage());
      return;
    }
    mainPanel.closeWordPane(true);
    if (mainPanel.tableBuilder != null) {
      mainPanel.tableBuilder.save();
    }
    mainPanel.setOpenedPackNode(null);
    BidBookImpProgressBar bbpb = new BidBookImpProgressBar();
    bbpb.setParentPanel(this);
    bbpb.setFilePath(filePath);
    bbpb.toStartWorker();
  }

  private boolean prepareToCheckVersion(String zipPath) throws Exception {
    try {
      return importTbFileService.checkVersionMatching(zipPath);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
  }

  /**
   * 根据当前打开的文件所属标段，控制响应点树中非对应标段的节点为隐藏或合拢起来状态：
   *
   * @param currNode
   */
  @Override
  public void closeAndOpenMatchingNode(SmartTreeNode currNode) {
    SmartTreeNode zbNode = PubFunction.getNeedingNodeInParent(currNode, GV.NODE_TYPE_ZB);
    if (zbNode != null) {
      return;
    }
    String nodeCode = PubFunction.getNeedingNodeInParent(currNode, GV.NODE_TYPE_PACK).getNodeCode();
    SmartTreeNode rpRootNode = this.getResponsePointsTree().getTreeNode();
    String nodeType = GV.NODE_TYPE_PACK_RP;
    SmartTreeNode matchNode = PubFunction.getNeedingNodeWithNodeCodeInChildren(rpRootNode, nodeType, nodeCode);
    if (matchNode == null) {
      return;
    }
    setNodeVisibleWithNodeCode(this.responsePointsTree, matchNode.getNodeCode(), true, true);
  }

  private void addDecodeAction(JButton decodeBtn) {
    decodeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        Map<String, String> paras = new HashMap<String, String>();
        if (GV.SESSION_MAP.isEmpty()) {
          paras.put("URL", ProjectInfoPanel.getWebServerIPAddr());
          paras.put("USERTYPE", "supplier");
          paras.put("ISEDITABLE", "N");
          JDialog dialog = new JDialog();
          dialog.setTitle(GV.getSimpleMsg("userLoginTypeSelect"));
          dialog.setSize(new Dimension(420, 180));
          dialog.add(new LoginTypeSelectionPanel(dialog, paras));
          dialog.setModal(true);
          dialog.setResizable(false);
          dialog.setLocation(PubFunction.getBetterPosition(dialog.getSize(), 0, 100));
          dialog.setVisible(true);
        } else {
          paras.put("USERNAME", GV.SESSION_MAP.get(GV.USER_NAME));
          paras.put("USERID", GV.SESSION_MAP.get(GV.USER_ID));
          paras.put("CASERIALCODE", GV.SESSION_MAP.get(GV.CASERIAL_CODE));
          paras.put("USERTYPE", GV.SESSION_MAP.get(GV.USER_TYPE));
          CALoginPanel caLogin = new CALoginPanel(paras);
          caLogin.makeWaitingDecodingListDialog(paras);
        }
      }
    });
  }

  private void addZipTestAction(JButton zipTestBtn) {
    zipTestBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDoubleBuffered(true);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileChooseFilter(GV.SUFFIX_ZTB));
        fileChooser.setCurrentDirectory(new File(GV.getHistoryDirectory()));
        int result = fileChooser.showOpenDialog(null);
        String zipPath;
        if (result == JFileChooser.APPROVE_OPTION) {
          zipPath = fileChooser.getSelectedFile().getAbsolutePath();
          GV.setHistoryDirectory(fileChooser.getSelectedFile().getParent());
        } else {
          return;
        }
        new JobThreads().toCheckZipFile(zipPath);
      }
    });
  }

  public void toExecuteAfterTodo(String fileType) {
    //by chenjl 2013-08-27 不实现在线上传
//    if (!toCheckIsNeedToUpload()) {
//      return;
//    }
//    //如果没有一个曾被选中，那么需要弹窗提示
//    if (GV.SESSION_MAP.isEmpty()) {
//      if (notLoginNotAlertAgain) {
//        return;
//      } else if (autoLoginNotAlertAgain) {
//        toCreateDialog();
//      } else {
//        toCreateDialog();
//      }
//    }
//    FilePrepareUploadModelBuilder builder = new FilePrepareUploadModelBuilder();
//    if ("doc".equals(fileType)) {
//      builder.fileToUploadModelBuilder(this.wordPane.getOpenFile());
//    } else if ("config".equals(fileType)) {
//      builder.fileToUploadModelBuilder(new File(this.tableBuilder.getFilepath()));
//    }
  }

  private boolean toCheckIsNeedToUpload() {
    SmartTreeNode currNode = this.getOpenedLeavesNode();
    if (currNode != null) {
      return !"NOT_UPLOAD".equals(currNode.getForExtention4());
    } else {
      return true;
    }
  }

  private void toCreateDialog() {
    JRadioButton notLogin = new JRadioButton();
    notLogin.setText(GV.getSimpleMsg("notAskToLogin"));
    notLogin.setToolTipText(GV.getSimpleMsg("notAskToLoginTip"));
    notLogin.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        notLoginNotAlertAgain = ((JRadioButton) e.getSource()).isSelected();
      }
    });
    JRadioButton autoLogin = new JRadioButton();
    autoLogin.setText(GV.getSimpleMsg("autoToLogin"));
    autoLogin.setToolTipText(GV.getSimpleMsg("notAskToLoginTip"));
    autoLogin.setSelected(true);
    autoLogin.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        autoLoginNotAlertAgain = ((JRadioButton) e.getSource()).isSelected();
      }
    });
    ButtonGroup group = new ButtonGroup();
    group.add(notLogin);
    group.add(autoLogin);
    String msg = GV.getSimpleMsg("offlineAttention");
    String tips = GV.getSimpleMsg("loginUsefull");
    Object[] content = { msg, tips, notLogin, autoLogin };
    String title = GV.getSimpleMsg("asking");
    int sel = JOptionPane.showConfirmDialog(null, content, title, 0, 3);
    if (sel == JOptionPane.OK_OPTION) {
      toShowLoginTypeWindow();
    }
  }

  private void toShowLoginTypeWindow() {
    if (notLoginNotAlertAgain) {
      return;
    }
    Map<String, String> paras = new HashMap<String, String>();
    paras.put("URL", ProjectInfoPanel.getWebServerIPAddr());
    if (paras.get("URL") == null || "".equals(paras.get("URL"))) {
      return;
    }
    paras.put("USERTYPE", "supplier");
    paras.put(GV.AUTHEN_TYPE, GV.AUTHEN_TYPE_LOGIN_ONLY);
    paras.put("ISEDITABLE", "N");
    JDialog dialog = new JDialog();
    dialog.setTitle(GV.getSimpleMsg("userLoginTypeSelect"));
    dialog.setSize(new Dimension(420, 180));
    dialog.add(new LoginTypeSelectionPanel(dialog, paras));
    dialog.setModal(true);
    dialog.setResizable(false);
    dialog.setLocation(PubFunction.getBetterPosition(dialog.getSize(), 0, 100));
    dialog.setVisible(true);
  }

  public ImportTbFileService getImportTbFileService() {
    return importTbFileService;
  }

  public void setImportTbFileService(ImportTbFileService importTbFileService) {
    this.importTbFileService = importTbFileService;
  }

  public static JButton getLoginBtn() {
    return loginBtn;
  }

  public static void clearLoginSession() {
    if (loginBtn == null) {
      GV.SESSION_MAP.clear();
      return;
    }
    loginBtn.setText(GV.getTranslate().get("loginbtn"));
    GV.SESSION_MAP.clear();
  }
}
