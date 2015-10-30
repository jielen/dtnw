package com.ufgov.zc.client.zc.ztb;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.snca.financebidding.FinanceBiddingCall;
import com.ufgov.zc.client.util.FileMerger;
import com.ufgov.zc.client.zc.ztb.Threads.ExportAndUploadTbBook;
import com.ufgov.zc.client.zc.ztb.Threads.ExportTbBookForCommon;
import com.ufgov.zc.client.zc.ztb.Threads.ExportTbBookForHight;
import com.ufgov.zc.client.zc.ztb.activex.WordPane;
import com.ufgov.zc.client.zc.ztb.component.MainPanel;
import com.ufgov.zc.client.zc.ztb.component.ProjectInfoPanel;
import com.ufgov.zc.client.zc.ztb.component.ReceiptBillDialog;
import com.ufgov.zc.client.zc.ztb.component.SingleSeletionTree;
import com.ufgov.zc.client.zc.ztb.component.TBPanel;
import com.ufgov.zc.client.zc.ztb.component.ZipTestProgressPanel;
import com.ufgov.zc.client.zc.ztb.fileResumeBroken.authentication.UserVerify;
import com.ufgov.zc.client.zc.ztb.model.BusinessProject;
import com.ufgov.zc.client.zc.ztb.model.LevelOneItem;
import com.ufgov.zc.client.zc.ztb.model.LevelTwoModel;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;
import com.ufgov.zc.client.zc.ztb.service.FileExportService;
import com.ufgov.zc.client.zc.ztb.service.FileTransferService;
import com.ufgov.zc.client.zc.ztb.service.LevelOneTwoBuilder;
import com.ufgov.zc.client.zc.ztb.service.ServerProjectService;
import com.ufgov.zc.client.zc.ztb.service.XmlOperateService;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.client.zc.ztb.util.PubFunction;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.zc.model.ZcEbProjZbFile;
import com.ufgov.zc.common.zc.model.ZcZBFileTemplate;

public class JobThreads {
  private static int PROGRESS_BAR_WIDTH = 450;

  private static boolean isMustCoded = false;

  private static String isMasterMustCoded = "N";

  private static String isProviderMustCoded = "N";

  private Thread worker = null;

  private static Process preTrantsProcess;

  private static Process uploadBidProcess;

  /**
   * 导出项目招标书
   *
   * @param ssTree
   * @param filePath
   */
  public void startExportProjectThread(final SingleSeletionTree ssTree, final String filePath) {
    final JDialog progressDialog = getProgressDialog();
    toGetCodedDemand();
    worker = new Thread(new Runnable() {
      public void run() {
        String tipMeg = null;
        String uploadStatus = "fail";
        String expFilePath = null;
        try {
          JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_EXPORT_READY));
          SmartTreeNode projNode = ssTree.getCurrentNode();
          if (!DataChecker.doCheckImportantMsgForZbBook(projNode)) {
            progressDialog.dispose();
            return;
          }
          String projCode = projNode.getNodeCode();
          String sourceDir = GV.getImportFileDir_FromRoot().append(projCode).toString();
          expFilePath = (new FileExportService()).exportProject(projNode, sourceDir, filePath + File.separator, true);
          if (!doZipFileCheck(expFilePath).isCheckPass()) {
            GV.showMessageDialog(null, GV.getSimpleMsg("fileCheckErr"));
            progressDialog.dispose();
            return;
          }
          JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_SERVER_CHECKING));
          Map<String, Object> res = (new ServerProjectService()).toSearchZBFileByProjCode(projCode);
          if (null == res.get("FAILREASON") || "".equals(res.get("FAILREASON"))) {
            if ("noexist".equals(res.get("SEARCHREASULT"))) {
              JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_UPLOAD_READY));
              uploadStatus = (new FileTransferService()).uploadZbFile(projNode, expFilePath);
            } else {
              ZcEbProjZbFile zbFile = (ZcEbProjZbFile) res.get("ZCEBPROJZBFILE");
              if (null == zbFile.getStatus() || "".equals(zbFile.getStatus()) || "0".equals(zbFile.getStatus())) {
                /*int sel = JOptionPane.showConfirmDialog(null, GV.getSimpleMsg("serverExistZBBook"), "是否覆盖", JOptionPane.YES_NO_OPTION);
                if (JOptionPane.OK_OPTION == sel) {
                  JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_UPLOAD_READY));
                  uploadStatus = (new FileTransferService()).uploadZbFile(projNode, expFilePath);
                } else {
        */          uploadStatus = "notdid";
           //     }
              }
            }
          } else {
            tipMeg = (String) res.get("FAILREASON");
          }
          JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_EXPORT_OK));
        } catch (Exception e) {
          e.printStackTrace();
          tipMeg = e.getMessage();
        }
        StringBuffer buff = new StringBuffer();
        if ("success".equals(uploadStatus)) {
          buff.append(GV.getSimpleMsg("exportSuccAndSyns"));
          buff.append(expFilePath);
        } else if ("notdid".equals(uploadStatus)) {
          buff.append(GV.getSimpleMsg("exportSuccAndPath"));
          buff.append(expFilePath);
        } else {
          buff.append(GV.getSimpleMsg("exportSuccAndSynsFail"));
          buff.append(tipMeg);
          buff.append(GV.getSimpleMsg("exportPath"));
          buff.append(expFilePath);
        }
        GV.showMessageDialog(null, buff.toString());
        progressDialog.dispose();
      }
    });
    worker.start();
  }

  /**
   * 上传项目招标书
   *
   * @param ssTree
   * @param content
   */
  public void startUploadProjectThread(final SingleSeletionTree ssTree, final String ztbPath) {
    final JDialog progressDialog = getProgressDialog();
    toGetCodedDemand();
    worker = new Thread(new Runnable() {
      public void run() {
        boolean rlt = false;
        String expInfo = "";
        String projCode = null;
        String projName = null;
        try {
          if (ztbPath == null || "".equals(ztbPath)) {
            SmartTreeNode projNode = null;
            SmartTreeNode currNode = ssTree.getCurrentNode();
            if (currNode == null) {
              currNode = ssTree.getTreeNode();
            }
            if (GV.NODE_TYPE_PROJECT.equals(currNode.getNodeType())) {
              projNode = currNode;
            } else {
              projNode = (SmartTreeNode) currNode.getRoot().getChildAt(0);
            }
            if (!DataChecker.doCheckImportantMsgForZbBook(projNode)) {
              progressDialog.dispose();
              return;
            }
            projCode = projNode.getNodeCode();
            projName = projNode.getNodeDisplayName();
            JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_EXPORT_READY));
            String exportPath = GV.getUpload_tmpPath().toString();
            String sourcePath = GV.getImportFileDir_FromRoot().append(projNode.getNodeCode()).toString();
            String ztbPath = (new FileExportService()).exportProject(projNode, sourcePath, exportPath, true);
            if (!doZipFileCheck(ztbPath).isCheckPass()) {
              GV.showMessageDialog(null, GV.getSimpleMsg("fileCheckErr"));
              progressDialog.dispose();
              return;
            }
            JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_UPLOAD_READY));
            (new FileTransferService()).uploadZbFile(projNode, ztbPath);
            JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_UPLOAD_OK));
          } else {
            JTextField pjCodeField = new JTextField();
            JTextField pjNameField = new JTextField();
            String tip = GV.getSimpleMsg("pleaseInput");
            String pcLabel = GV.getSimpleMsg("projCodeLabel");
            String pnLabel = GV.getSimpleMsg("projNameLabel");
            Object[] message = { tip, "", pcLabel, pjCodeField, pnLabel, pjNameField };
            int res = JOptionPane.showConfirmDialog(null, message, "", 2, 3);
            if (res == JOptionPane.OK_OPTION) {
              projCode = pjCodeField.getText().trim();
              projName = pjNameField.getText().trim();
              (new FileTransferService()).uploadZbFile(projCode, projName, ztbPath);
            }
          }
          rlt = true;
        } catch (Exception e) {
          rlt = false;
          expInfo = e.getMessage();
          e.printStackTrace();
        }
        String tigMeg;
        if (rlt) {
          tigMeg = GV.getOperateMsg("uploadProject.success", projName);
        } else {
          tigMeg = GV.getOperateMsg("zbFileUploadFail", projName);
        }
        GV.showMessageDialog(null, tigMeg + expInfo);
        progressDialog.dispose();
      }
    });
    worker.start();
  }

  /**
   * 导出标段标书
   *
   * @param ssTree
   * @param uploadType
   */
  public void startExportPackThread(final SingleSeletionTree ssTree, String uploadType) {
    toGetCodedDemand();
    SmartTreeNode tmpNode = ssTree.getCurrentNode();
    List<SmartTreeNode> list = new ArrayList<SmartTreeNode>();
    if (tmpNode != null && GV.NODE_TYPE_PACK.equals(tmpNode.getNodeType())) {
      list.add(tmpNode);
    } else {
      PubFunction.doSearchAllMatchingNeedingNodeFromCurrNode(ssTree.getTreeNode(), "nodeType", GV.NODE_TYPE_PACK, false, 0, list);
    }
    final List<SmartTreeNode> tlist = new ArrayList<SmartTreeNode>();
    if (list.size() > 1) {
      final List<JCheckBox> cbList = new ArrayList<JCheckBox>();
      Object[] options = new Object[list.size() + 2];
      options[0] = GV.getSimpleMsg("pleaseSelectHightUploadPack");
      JCheckBox all = new JCheckBox("全选");
      all.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          boolean sel = ((JCheckBox) e.getSource()).isSelected();
          for (int i = 0; i < cbList.size(); i++) {
            cbList.get(i).setSelected(sel);
          }
        }
      });
      options[1] = all;
      for (int i = 0; i < list.size(); i++) {
        JCheckBox cb = new JCheckBox();
        cb.setText(list.get(i).getNodeDisplayName());
        cb.setName(list.get(i).getNodeCode());
        options[i + 2] = cb;
        cbList.add(cb);
      }
      int res = JOptionPane.showConfirmDialog(null, options, GV.getSimpleMsg("bidSelectTitle"), 2, 3);
      if (res == JOptionPane.OK_OPTION) {
        tlist.addAll(getSelectedPackNodes(cbList, list));
      } else {
        return;
      }
    } else {
      tlist.addAll(list);
    }
    ExportTbBookForHight expForHight = new ExportTbBookForHight();
    expForHight.setList(tlist);
    expForHight.setUploadType(uploadType);
    expForHight.toStartWorker();
  }

  /**
   * 启动高速传输窗口
   */
  public static boolean doStartHightUploadClient() {
    String workDir = System.getProperty("user.dir");
    String exePath = "./tools/fts/Dengyun.exe";
    TBPanel.clearLoginSession();
    try {
      uploadBidProcess = Runtime.getRuntime().exec(exePath, null, new File(workDir + "/tools/fts"));
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, GV.getOperateMsg("clientStartErr", exePath));
      return false;
    }
  }

  /**
   * 启动高速传输窗口
   */
  public static boolean doStartPrepareHightUploadClient() {
    String workDir = System.getProperty("user.dir");
    String exePath = "./tools/fts/FTSPRETRANS.exe /pretrans";
    try {
      preTrantsProcess = Runtime.getRuntime().exec(exePath, null, new File(workDir + "/tools/fts"));
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, GV.getOperateMsg("sysnClientStartErr", exePath));
      return false;
    }
  }

  /**
   * 导出标段标书
   *
   * @param ssTree
   */
  public void startExportPackThread(final SingleSeletionTree ssTree) {
    toGetCodedDemand();
    SmartTreeNode tmpNode = ssTree.getCurrentNode();
    List<SmartTreeNode> list = new ArrayList<SmartTreeNode>();
    if (tmpNode != null && GV.NODE_TYPE_PACK.equals(tmpNode.getNodeType())) {
      list.add(tmpNode);
    } else {
      PubFunction.doSearchAllMatchingNeedingNodeFromCurrNode(ssTree.getTreeNode(), "nodeType", GV.NODE_TYPE_PACK, false, 0, list);
    }
    if (list.size() == 0) {
      return;
    }
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDoubleBuffered(true);
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setAcceptAllFileFilterUsed(true);
    fileChooser.setCurrentDirectory(new File(GV.getHistoryDirectory()));
    int result = fileChooser.showSaveDialog(null);
    final String exportPath;
    if (result == JFileChooser.APPROVE_OPTION) {
      exportPath = fileChooser.getSelectedFile().getAbsolutePath();
      GV.setHistoryDirectory(fileChooser.getSelectedFile().getParent());
    } else {
      return;
    }
    final List<SmartTreeNode> tlist = new ArrayList<SmartTreeNode>();
    if (list.size() > 1) {
      final List<JCheckBox> cbList = new ArrayList<JCheckBox>();
      Object[] options = new Object[list.size() + 2];
      options[0] = GV.getSimpleMsg("pleaseSelectPacksToExp");
      JCheckBox all = new JCheckBox("全选");
      all.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          boolean sel = ((JCheckBox) e.getSource()).isSelected();
          for (int i = 0; i < cbList.size(); i++) {
            cbList.get(i).setSelected(sel);
          }
        }
      });
      options[1] = all;
      for (int i = 0; i < list.size(); i++) {
        JCheckBox cb = new JCheckBox();
        cb.setText(list.get(i).getNodeDisplayName());
        cb.setName(list.get(i).getNodeCode());
        options[i + 2] = cb;
        cbList.add(cb);
      }
      int res = JOptionPane.showConfirmDialog(null, options, GV.getSimpleMsg("bidSelectTitle"), 2, 3);
      if (res == JOptionPane.OK_OPTION) {
        tlist.addAll(getSelectedPackNodes(cbList, list));
      } else {
        return;
      }
    } else {
      tlist.addAll(list);
    }
    ExportTbBookForCommon expForCommon = new ExportTbBookForCommon();
    expForCommon.setList(tlist);
    expForCommon.setFilePath(exportPath);
    expForCommon.setCompress(true);
    expForCommon.toStartWorker();
  }

  public static List<SmartTreeNode> getSelectedPackNodes(List<JCheckBox> cbList, List<SmartTreeNode> packNodes) {
    List<SmartTreeNode> selList = new ArrayList<SmartTreeNode>();
    for (int i = 0; i < cbList.size(); i++) {
      for (int j = 0; j < packNodes.size(); j++) {
        if (cbList.get(i).isSelected() && cbList.get(i).getName().equals(packNodes.get(j).getNodeCode())) {
          selList.add(packNodes.get(j));
        }
      }
    }
    return selList;
  }

  private static JProgressBar progressBar = null;

  private static JProgressBar getProgressBar(int len) {
    if (len == 0) {
      len = 100;
    }
    if (progressBar == null) {
      progressBar = new JProgressBar(0, len);
      progressBar.setStringPainted(true);
      progressBar.setBounds(new Rectangle(101, 305, PROGRESS_BAR_WIDTH, 30));
    }
    return progressBar;
  }

  public static String progressInfoText = "准备就绪...";

  public static void publishProgressMsg(String msg) {
    if (msg == null || "".equals(msg)) {
      return;
    }
    progressInfoText = msg;
    getProgressBar(0).setString(msg);
  }

  public static String getProgressMsg() {
    return progressInfoText;
  }

  public static JDialog getProgressDialog() {
    JDialog progressDialog = new JDialog();
    progressDialog.add(getProgressBar(0));
    progressDialog.setSize(new Dimension(PROGRESS_BAR_WIDTH + 10, 50));
    progressDialog.setLocation(PubFunction.getPosition(PROGRESS_BAR_WIDTH));
    progressDialog.setUndecorated(true);
    progressDialog.setAlwaysOnTop(true);
    progressDialog.setVisible(true);
    progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // 不允许关闭
    return progressDialog;
  }

  /**
   * 创建投标回执
   *
   * @param map
   */
  public static void createTouBiaoFeedBack(Map<String, String> map) {
    Toolkit.getDefaultToolkit().beep();
    BusinessProject bp = ProjectInfoPanel.getBusinessProject();
    map.put("PROJECTNAME", bp.getName());
    map.put("PROJECTCODE", bp.getNo());
    map.put("MANAGERNAME", bp.getManager());
    map.put("MANAGERPHONE", bp.getPhone());
    map.put("OFFICEADDRESS", bp.getkBidAddr());
    Map<String, String> paras = new HashMap<String, String>();
    String level2filePath = LevelOneTwoBuilder.getLevelTwoXmlPath(bp.getNo(), map.get("PACKCODE"));
    String level2fileName = (new File(level2filePath)).getName();
    if ("success".equals(map.get("UPDATESTATUS"))) {
      paras.put("UPLOADSTATUS", LevelOneItem.UPLOAD_STATUS_ALL_FINISH);
      SimpleDateFormat sdf = new SimpleDateFormat(ZcSettingConstants.SIMPLE_DATE_FORMAT_FULL);
      paras.put("ENDUPLOADTIME", sdf.format(new Date()));
      paras.put("SUMMITSTATUS", LevelOneItem.SUMMIT_STATUS_OK);
      toUpdateLevelOnexmlFile(level2fileName, paras);
      map.put("LEVEL2FILEPATH", level2filePath);
      ReceiptBillDialog rbd = new ReceiptBillDialog(map);
      rbd.toFront();
      rbd.setAlwaysOnTop(true);
      rbd.setVisible(true);
    } else {
      paras.put("UPLOADSTATUS", LevelOneItem.UPLOAD_STATUS_FAIL);
      paras.put("SUMMITSTATUS", LevelOneItem.SUMMIT_STATUS_INIT);
      SimpleDateFormat sdf = new SimpleDateFormat(ZcSettingConstants.SIMPLE_DATE_FORMAT_FULL);
      paras.put("ENDUPLOADTIME", sdf.format(new Date()));
      toUpdateLevelOnexmlFile(level2fileName, paras);
      JOptionPane.showMessageDialog(null, GV.getOperateMsg("bidFailAndReason", map.get("FAILREASON")));
    }
  }

  /**
   * 更新一级xml文件中的项目标段投标状态
   *
   * @param level2fileName
   * @param status
   */
  public static void toUpdateLevelOnexmlFile(String level2fileName, Map<String, String> map) {
    LevelOneTwoBuilder xmlBuilder = new LevelOneTwoBuilder();
    try {
      LevelOneItem loi = new LevelOneItem();
      String status = map.get("UPLOADSTATUS");
      if (status != null) {
        loi.setTbfileUploadStatus(status);
      }
      String sTime = map.get("STARTUPLOADTIME");
      if (sTime != null) {
        loi.setTbfileUploadSTime(sTime);
      }
      String eTime = map.get("ENDUPLOADTIME");
      if (eTime != null) {
        loi.setTbfileUploadETime(eTime);
      }
      String sStatus = map.get("SUMMITSTATUS");
      if (sStatus != null) {
        loi.setSummitStatus(sStatus);
      }
      loi.setXmlFileName(level2fileName);
      xmlBuilder.updateLevelOneXmlFile(loi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 上传模板
   *
   * @param ssTree
   * @param content
   */
  public void startUploadTemplateThread(final SingleSeletionTree ssTree, final ZcZBFileTemplate template) {
    final JDialog progressDialog = getProgressDialog();
    // 文件上传
    worker = new Thread(new Runnable() {
      public void run() {
        String name = template.getTplNo();
        String retMessage = "";
        try {
          SmartTreeNode currNode = ssTree.getCurrentNode();
          currNode.setNodeName(name);
          String exportPath = GV.getUpload_tmpPath().toString();
          String ztbPath = (new FileExportService()).exportTemplate(currNode, exportPath, true, template);
          retMessage = (new FileTransferService()).uploadTemplateFile(currNode, ztbPath, template);
        } catch (Exception e) {
          e.printStackTrace();
        }
        GV.showMessageDialog(null, retMessage);
        progressDialog.dispose();
      }
    });
    worker.start();
  }

  /**
   * 获取工具配置
   */
  public static Map<String, String> toGetCodedDemand() {
    String encodeDemand = ProjectInfoPanel.getBusinessProject().getEncodeDemand();
    if (encodeDemand != null && !"".equals(encodeDemand)) {
      Map<String, String> res = toExplainCommand(encodeDemand);
      isMustCoded = "Y".equalsIgnoreCase(res.get("ISMUSTCODED"));
      isMasterMustCoded = res.get("ISMASTERMUSTCODED");
      isProviderMustCoded = res.get("ISPROVIDERMUSTCODED");
    } else {
      isMustCoded = false;
      isMasterMustCoded = "N";
      isProviderMustCoded = "N";
    }
    Map<String, String> retValue = new HashMap<String, String>();
    retValue.put("ISMUSTCODED", isMustCoded ? "Y" : "N");
    retValue.put("ISMASTERMUSTCODED", isMasterMustCoded);
    retValue.put("ISPROVIDERMUSTCODED", isProviderMustCoded);
    return retValue;
  }

  /**
   * 解释加解密要求的命令参数
   *
   * @param encodeDemand
   * @return
   */
  private static Map<String, String> toExplainCommand(String encodeDemand) {
    encodeDemand = encodeDemand.replaceAll("：", ":");
    encodeDemand = encodeDemand.replaceAll("；", ";");
    String[] commands = encodeDemand.split(";");
    Map<String, String> map = new HashMap<String, String>();
    for (int i = 0; i < commands.length; i++) {
      String[] tmp = commands[i].split(":");
      if (tmp.length == 2) {
        map.put(tmp[0], tmp[1]);
      }
    }
    return map;
  }

  /**
   * 上传标段标书，两种应用：
   * 1、续传并投标；
   * 本函数实现如下功能：
   * a、获取到需要上传的文件，继续该文件的上传
   *
   * @param ssTree
   * @param content
   */
  @SuppressWarnings("unchecked")
  public void startGoonUploadPackThread(final SingleSeletionTree ssTree, final StringBuffer ztbFullPath) {
    toGetCodedDemand();
    String ztbPath = null;
    String packCode = null;
    String packName = null;
    String projCode = null;
    SmartTreeNode packNode = PubFunction.getPackNode(ssTree.getCurrentNode());
    if (packNode != null) {
      packCode = packNode.getNodeCode();
      packName = packNode.getNodeDisplayName();
      projCode = ((SmartTreeNode) packNode.getParent()).getNodeCode();
    } else {
      return;
    }
    String tmp_isMasterMustCoded = isMasterMustCoded;
    String tmp_isProviderMustCoded = isProviderMustCoded;
    ztbPath = ztbFullPath.append(projCode).append("[").append(packName).append("].").append(GV.SUFFIX_ZTB).toString();
    if (!ztbPath.endsWith(GV.SUFFIX_ZTB_EN_MEG) && isMustCoded) {
      int sel = JOptionPane.showConfirmDialog(null, GV.getSimpleMsg("askForEncode"), "提示", 0);
      if (sel == JOptionPane.OK_OPTION) {
        return;
      } else {
        sel = JOptionPane.showConfirmDialog(null, GV.getSimpleMsg("notEncodeAndUpload"), "提示", 0);
        if (sel == JOptionPane.NO_OPTION) {
          return;
        }
        tmp_isMasterMustCoded = "N";
        tmp_isProviderMustCoded = "N";
      }
    }
    String ztbName = (new File(ztbPath)).getName();
    String url = ProjectInfoPanel.getWebServerIPAddr();
    Map<String, String> parameterMap = new HashMap<String, String>();
    parameterMap.put("PROJCODE", projCode.trim());
    parameterMap.put("URL", url);
    parameterMap.put("FILENAME", ztbName);
    parameterMap.put("FILEPATH", ztbPath);
    parameterMap.put("PACKCODE", packCode.trim());
    parameterMap.put("MINIMUMFILESIZE", GV.MINIMUM_FILE_SIZE_ALERT + "");
    parameterMap.put("ZCISMASTERCODED", tmp_isMasterMustCoded);
    parameterMap.put("ZCISPROVIDERCODED", tmp_isProviderMustCoded);
    parameterMap.put("UPLOADMOLD", LevelOneItem.UPLOAD_MOLD_COMMON);
    UserVerify userVerify = new UserVerify(parameterMap);
    userVerify.getStart();
    Map map = userVerify.getReturnValue();
  }

  /**
   * 上传标段标书，一种应用：
   * 1、导出并上传投标；
   * 本函数实现如下功能：
   * a、压缩标书相关文件，生成完整投标书压缩包ztb文件；
   * b、抽取开标一览表文件；
   * 如果需要加密：
   * c、对a所得到的标书压缩文件进行CA加密，得到两个文件；
   * d、对b得到的开标一览表进行CA加密，得到两个文件；
   * e、对b得到的开标一览表进行固定长度密码加密，得到一个文件；
   * f、将d和e得到的文件进行压缩，生成ptfs文件，price table files；
   * g、将c得到的两个文件、f得到的一个文件，共三个文件用流进行合并，这里生成ptfs.tmp文件；
   * 如果不需要加密：
   * c、直接将a和b所得到的文件进行流合并，这里生成ztb.tmp；
   *
   * @param ssTree
   * @param content
   */
  @SuppressWarnings("unchecked")
  public void startUploadPackThread(final SingleSeletionTree ssTree) {
    SmartTreeNode tmpNode = ssTree.getCurrentNode();
    List<SmartTreeNode> list = new ArrayList<SmartTreeNode>();
    if (tmpNode != null && GV.NODE_TYPE_PACK.equals(tmpNode.getNodeType())) {
      list.add(tmpNode);
    } else {
      PubFunction.doSearchAllMatchingNeedingNodeFromCurrNode(ssTree.getTreeNode(), "nodeType", GV.NODE_TYPE_PACK, false, 0, list);
    }
    SmartTreeNode pNode = null;
    if (list.size() > 1) {
      final List<JRadioButton> rbList = new ArrayList<JRadioButton>();
      Object[] options = new Object[list.size() + 1];
      options[0] = GV.getSimpleMsg("pleaseSelectPackToUpload");
      ButtonGroup group = new ButtonGroup();
      for (int i = 0; i < list.size(); i++) {
        JRadioButton rb = new JRadioButton();
        rb.setText(list.get(i).getNodeDisplayName());
        rb.setName(list.get(i).getNodeCode());
        options[i + 1] = rb;
        if (i == 0) {
          rb.setSelected(true);
        }
        rbList.add(rb);
        group.add(rb);
      }
      int res = JOptionPane.showConfirmDialog(null, options, GV.getSimpleMsg("bidSelectTitle"), 2, 3);
      if (res == JOptionPane.OK_OPTION) {
        pNode = getSelectedPackNode(rbList, list);
      } else {
        return;
      }
    } else {
      if (list.size() != 1) {
        JOptionPane.showMessageDialog(null, GV.getSimpleMsg("pleaseSelectOnePack"));
        return;
      }
      pNode = list.get(0);
    }
    ExportAndUploadTbBook eub = new ExportAndUploadTbBook();
    eub.setPackNode(pNode);
    eub.setParent(GV.getCurrFrame());
    eub.toStartWorker();
  }

  /**
   * 获得选中的需要进行投标的标段
   *
   * @param rbList
   * @param packNodes
   * @return
   */
  public static SmartTreeNode getSelectedPackNode(List<JRadioButton> rbList, List<SmartTreeNode> packNodes) {
    for (int i = 0; i < rbList.size(); i++) {
      for (int j = 0; j < packNodes.size(); j++) {
        if (rbList.get(i).isSelected() && rbList.get(i).getName().equals(packNodes.get(j).getNodeCode())) {
          return packNodes.get(j);
        }
      }
    }
    return null;
  }

  /**
   * 上传标书到服务器之前，对标书进行加密解密等相关处理
   *
   * @param ztbPath
   * @param priceTablePath
   * @param packNode
   * @return
   * @throws Exception
   */
  public Map doPrepareToUploadFiles(String ztbPath, String projCode, String packCode, boolean isAskEncode) throws Exception {
    JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_ENCODE_READY));
    Map encodedResult = new HashMap();
    encodedResult.put("LOCALFILEFULLPATH", ztbPath);
    if (isMustCoded) {
      if (isAskEncode) {
        int sel = JOptionPane.showConfirmDialog(null, GV.getOperateMsg("askIsEncoding", ""), "请选择", JOptionPane.YES_NO_OPTION);
        if (sel == JOptionPane.NO_OPTION) {
          encodedResult.put("ENCODEDSTATUS", "ignore");
          encodedResult.put("FAILREASON", GV.getSimpleMsg("userCancelEncode"));
          return encodedResult;
        }
      }
      Map<String, String> ztbFilePaths = new HashMap<String, String>();
      Map tmp = doSecurityEncoding(ztbPath, projCode, packCode);
      encodedResult.putAll(tmp);
      String eStatus = (String) tmp.get("ENCODEDSTATUS");
      if ("fail".equalsIgnoreCase(eStatus) || "ignore".equalsIgnoreCase(eStatus)) {
        return encodedResult;
      }
      String origLevel2xmlPath = (String) tmp.get("LEVEL2XMLFILEPATH");
      String level2FileName = new File(origLevel2xmlPath).getName();
      String localEnFilePath = (String) tmp.get("LOCALFILEFULLPATH");
      String tbFileDirectory = new File(localEnFilePath).getParent();
      String copyXmlPath = tbFileDirectory + File.separator + level2FileName;
      JobThreads.publishProgressMsg(GV.getSimpleMsg("readyToGenerateMD5"));
      encodedResult.put("TBFILEENCODEDMD5", PubFunction.getFileMD5DigestHexStr(localEnFilePath));
      JobThreads.publishProgressMsg(GV.getSimpleMsg("finishToGenerateMD5"));
      String lcoalMegFilePath = localEnFilePath + GV.SUFFIX_MERGER_FILE;
      encodedResult.put("LOCALFILEFULLPATH", lcoalMegFilePath);
      encodedResult.put("PROJCODE", projCode);
      encodedResult.put("PACKCODE", packCode);
      writeMoreInfoToLevel2Xml(origLevel2xmlPath, encodedResult);
      List<String> toDeleteFiles = new ArrayList<String>();
      PubFunction.copyFile(origLevel2xmlPath, copyXmlPath);
      ztbFilePaths.put("rootdir", tbFileDirectory);
      ztbFilePaths.put("tbfileafterencoded", localEnFilePath);
      toDeleteFiles.add(localEnFilePath);
      ztbFilePaths.put("level2xmlfilepath", copyXmlPath);
      toDeleteFiles.add(copyXmlPath);
      JobThreads.publishProgressMsg(GV.getSimpleMsg("mergingFiles"));
      doMergerFiles(ztbFilePaths, lcoalMegFilePath);
      JobThreads.publishProgressMsg(GV.getSimpleMsg("mergedFilesFinish"));
      //doTmpFilesDelete(toDeleteFiles);
    } else {
      encodedResult.put("ENCODEDSTATUS", "notneed");
    }
    return encodedResult;
  }

  /**
   * 删除遗留的临时文件
   *
   * @param fileList
   */
  public void doTmpFilesDelete(List<String> fileList) {
    JobThreads.publishProgressMsg(GV.getSimpleMsg("toDeletingTmpFiles"));
    if (fileList == null) {
      return;
    }
    for (int i = 0; i < fileList.size(); i++) {
      PubFunction.deleteFile(fileList.get(i));
    }
    JobThreads.publishProgressMsg(GV.getSimpleMsg("tmpFilesDeletingFinish"));
  }

  /**
   * 创建标书对应二级xml文件内容
   *
   * @param origLevel2xmlPath
   * @param newData
   */
  private static void writeMoreInfoToLevel2Xml(String origLevel2xmlPath, Map newData) {
    LevelOneTwoBuilder xmlBuilder = new LevelOneTwoBuilder();
    try {
      LevelTwoModel ltm = new LevelTwoModel();
      xmlBuilder.mergeDetailFromMapToObject(newData, ltm);
      xmlBuilder.updateLevelTwoXmlFile(ltm);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 返回加密后的文件路径
   *
   * @return
   */
  public Map doSecurityEncoding(String srcPath, String projCode, String packCode) {
    Map map = new HashMap();
    JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_ENCODE_READY));
    FinanceBiddingCall fbc = new FinanceBiddingCall();
    try {
      Map encodedResult = null;
      String levelTwoXml = LevelOneTwoBuilder.getLevelTwoXmlPath(projCode, packCode);
      XmlOperateService xmlOS = new XmlOperateService();
      xmlOS.xmlReader(levelTwoXml, "two");
      Map infoMap = xmlOS.getLevel2Data();
      //先检查是否有足够的公钥信息
      toCheckEnoughPubKey(infoMap);
      JobThreads.publishProgressMsg(GV.transStatusCode(""));
      if ("Y".equals(isMasterMustCoded) && "Y".equals(isProviderMustCoded)) {
        encodedResult = fbc.bidding(infoMap, srcPath, srcPath + ".en");
      } else if ("Y".equals(isProviderMustCoded) && "N".equals(isMasterMustCoded)) {
        encodedResult = fbc.biddingWithProviderEncodingOnly(infoMap, srcPath, srcPath + ".en");
      }
      map.putAll(encodedResult);
      if (!"ignore".equalsIgnoreCase((String) map.get("ENCODEDSTATUS"))) {
        map.put("LEVEL2XMLFILEPATH", levelTwoXml);
        map.put("ENCODEDSTATUS", "success");
        JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_ENCODE_OK));
      } else {
        JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_ENCODE_FAIL));
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, GV.getSimpleMsg("encodedFail") + e.getMessage());
      e.printStackTrace();
      map.put("ENCODEDSTATUS", "fail");
      JobThreads.publishProgressMsg(GV.getFileOperatorCN(GV.PROGRESS_STATUS_ENCODE_FAIL));
    }
    return map;
  }

  /**
   * 检查是否有足够的公钥信息
   *
   * @param infoMap
   */
  private static void toCheckEnoughPubKey(Map infoMap) {
    if (isMustCoded) {
      String masterPKA = (String) infoMap.get("MASTERPUBLICKEYA") + "";
      String masterPKB = (String) infoMap.get("MASTERPUBLICKEYB") + "";
      if ("Y".equals(isMasterMustCoded)) {
        if (masterPKA.length() < 10 || masterPKB.length() < 10) {
          String tip = GV.getSimpleMsg("missingMasterAndMinorPubKey");
          int sel = JOptionPane.showConfirmDialog(null, tip, "提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
          if (sel == JOptionPane.NO_OPTION) {
            isMasterMustCoded = "Y";
            return;
          } else {
            isMasterMustCoded = "N";
          }
        }
      }
    }
  }

  public static void doMergerFiles(Map<String, String> files, String newPath) {
    P.pm(files);
    FileMerger fm = new FileMerger();
    fm.mergerFiles(files, newPath);
  }

  public static void doGeneratorMD5(String srcFilePath, String MD5FilePath, String preFix, String oper) {
    if ("D".equals(oper)) {//删除文件
      PubFunction.deleteFile(MD5FilePath);
    } else if ("N".equals(oper)) {//新建文件
      preWriteMD5File(srcFilePath, MD5FilePath, preFix, false);
    } else if ("A".equals(oper)) {//拼接文件
      preWriteMD5File(srcFilePath, MD5FilePath, preFix, true);
    }
  }

  private static void preWriteMD5File(String srcFilePath, String MD5FilePath, String preFix, boolean isAppend) {
    StringBuffer content = new StringBuffer(preFix);
    content.append("[");
    try {
      String tmp = PubFunction.getFileMD5DigestHexStr(srcFilePath);
      content.append(tmp);
      content.append("]");
      content.append(System.getProperty("line.separator"));
      PubFunction.doWriteFile(MD5FilePath, content.toString(), isAppend);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 上传标段标书，一种应用：
   * 1、外部引用文件上传并投标
   * 2、这个方法只应用于在内网投标的情况，所以必须是已经加密合并好后的文件，附加名为ztbs文件；
   *
   * @param ssTree
   * @param paras
   */
  @SuppressWarnings("unchecked")
  public void startUploadPackThreadInnerWeb(final SingleSeletionTree ssTree, final Map<String, String> paras) {
    final JDialog progressDialog = getProgressDialog();
    toGetCodedDemand();
    worker = new Thread(new Runnable() {
      public void run() {
        String ztbPath = null;
        String packCodes = null;
        String projCode = null;
        String url = null;
        if (paras != null) {
          packCodes = paras.get("PACKCODES");
          projCode = paras.get("PROJECTCODE");
          ztbPath = paras.get("ZTBFULLPATH");
          url = paras.get("URL");
        }
        String tmp_isMasterMustCoded = isMasterMustCoded;
        String tmp_isProviderMustCoded = isProviderMustCoded;
        if (!ztbPath.toLowerCase().endsWith(GV.SUFFIX_ZTB_EN_MEG) && isMustCoded) {
          int sel = JOptionPane.showConfirmDialog(null, GV.getSimpleMsg("sysAskForEncode"), "提示", JOptionPane.YES_NO_OPTION);
          if (sel == JOptionPane.NO_OPTION) {
            progressDialog.dispose();
            return;
          } else {
            tmp_isMasterMustCoded = "N";
            tmp_isProviderMustCoded = "N";
          }
        }
        String ztbName = (new File(ztbPath)).getName();
        JobThreads.publishProgressMsg(GV.getOperateMsg("readySendDataToServerUrl", url));
        progressDialog.dispose();
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("PROJCODE", projCode.trim());
        parameterMap.put("URL", url);
        parameterMap.put("FILENAME", ztbName);
        parameterMap.put("FILEPATH", ztbPath);
        parameterMap.put("PACKCODE", packCodes.trim());
        parameterMap.put("MINIMUMFILESIZE", GV.MINIMUM_FILE_SIZE_ALERT + "");
        parameterMap.put("ZCISMASTERCODED", tmp_isMasterMustCoded);
        parameterMap.put("ZCISPROVIDERCODED", tmp_isProviderMustCoded);
        parameterMap.put("UPLOADMOLD", LevelOneItem.UPLOAD_MOLD_COMMON);
        UserVerify userVerify = new UserVerify(parameterMap);
        userVerify.getStart();
        Map map = userVerify.getReturnValue();
      }
    });
    worker.start();
  }

  /**
   * @param content
   * @param operator
   */
  public static void startFileOperatorThread(final MainPanel mainPanel, String opt, String filePath) {
    File file = new File(filePath);
    if (!file.exists()) {
      JOptionPane.showMessageDialog(null, file.getName() + GV.getSimpleMsg("fileNotExistOrBroken"));
      MainPanel.setLastDoubleClickResponseSuccess(true);
      return;
    }
    final JDialog progressDialog = getProgressDialog();
    String ps = null;
    String fileName = file.getName();
    if ("R".equals(opt)) {
      ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_READING);
      JobThreads.publishProgressMsg(ps);
      JobThreads.publishProgressMsg(ps + fileName + " --约" + PubFunction.getFileLength(file.length()));
    } else if ("RO".equals(opt)) {
      ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_READING_ONLY);
      JobThreads.publishProgressMsg(ps);
      JobThreads.publishProgressMsg(ps + fileName + " --约" + PubFunction.getFileLength(file.length()));
    }
    mainPanel.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        MainPanel.setLastDoubleClickResponseSuccess(true);
        mainPanel.removePropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, this);
        try {
          progressDialog.setVisible(false);
        } catch (Exception e) {
          e.printStackTrace();
        }
        //        mainPanel.updateUI();
      }
    });
  }

  /**
   * 加密文件
   *
   * @param srcPath
   * @return
   */
  public static String doEncryFile(String srcPath) {
    String tarPath = srcPath + ".des";
    try {
      FinanceBiddingCall.encryptZipFile(srcPath, tarPath, GV.COM_DES_KEY, 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tarPath;
  }

  /**
   * 解密文件
   *
   * @param encPath
   * @return
   */
  public static String doDencryFile(String encPath) {
    String decPath = encPath.replace(".des", "");
    try {
      FinanceBiddingCall.decryptZipFile(encPath, decPath, GV.COM_DES_KEY, 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return decPath;
  }

  /**
   * 检查zip文件
   */
  public void toCheckZipFile(final String zipPath) {
    Runnable run = new Runnable() {
      public void run() {
        JDialog dialog = JobThreads.getProgressDialog();
        doZipFileCheck(zipPath);
        dialog.dispose();
      }
    };
    worker = new Thread(run);
    worker.start();
  }

  /**
   * 进行文件检查校验
   *
   * @param zipPath
   * @return
   */
  public static ZipTestProgressPanel doZipFileCheck(String zipPath) {
    int fileCount = 0;
    int okCount = 0;
    //不正常的总数
    int notOkCount = 0;
    ZipTestProgressPanel ztp = new ZipTestProgressPanel();
    ztp.createProgressInfoWindow();
    ztp.setZipFileChecking(true);
    long startTime = System.currentTimeMillis();
    ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo000"));
    ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo001"));
    ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo002"));
    ztp.setProgressText(" ");
    ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo003"));
    int offsize = 0;
    ZipFile zipFile = null;
    try {
      ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo004"));
      zipFile = new ZipFile(zipPath, GV.FILE_CHAR_CODE);
      String fileName = null;
      Enumeration emu = zipFile.getEntries();
      while (emu.hasMoreElements()) {
        try {
          ZipEntry entry = (ZipEntry) emu.nextElement();
          if (entry.isDirectory()) {
            continue;
          }
          ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo005"));
          BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
          fileName = entry.getName();
          String sName = (new File(fileName)).getName();
          String cpSize = PubFunction.getFileLength(entry.getCompressedSize());
          String unCpSize = PubFunction.getFileLength(entry.getSize());
          ztp.setProgressText(++fileCount + GV.getOperateMsg("zipTestProgressInfo007", fileName));
          String before = GV.getSimpleMsg("zipTestProgressInfo008");
          String after = GV.getSimpleMsg("zipTestProgressInfo009");
          ztp.setProgressText(fileCount + before + unCpSize + after + cpSize + "].");
          if (entry.getSize() <= 0) {
            throw new Exception(GV.getSimpleMsg("emptyFound") + sName);
          }
          double readLen = 0;
          int rL = -1;
          byte data[] = new byte[GV.BUFFER_SIZE];
          while ((rL = bis.read(data, offsize, GV.BUFFER_SIZE)) != -1) {
            readLen += rL;
            double per = PubFunction.getPercent(readLen, entry.getCompressedSize());
            JobThreads.publishProgressMsg("正在检查文件:" + sName + "--共【" + cpSize + "】,已完成【" + per + "%】");
          }
          bis.close();
          ztp.setProgressText(fileCount + GV.getSimpleMsg("zipTestProgressInfo010"));
          okCount++;
        } catch (Exception e) {
          e.printStackTrace();
          ztp.setProgressText(fileCount + GV.getSimpleMsg("zipTestProgressInfo011"));
          notOkCount++;
        }
      }
      zipFile.close();
    } catch (Exception e) {
      e.printStackTrace();
      ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo012"));
      ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo013") + e.getMessage());
    }
    File tmpFile = new File(zipPath);
    ztp.setProgressText(" ");
    ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo014"));
    ztp.setProgressText(GV.getOperateMsg("zipTestProgressInfo015", PubFunction.getFileLength(tmpFile.length())));
    ztp.setProgressText(GV.getOperateMsg("zipTestProgressInfo016", fileCount + ""));
    ztp.setProgressText(GV.getOperateMsg("zipTestProgressInfo017", okCount + ""));
    ztp.setProgressText(GV.getOperateMsg("zipTestProgressInfo018", notOkCount + ""));
    if (notOkCount == 0) {
      String md5 = "";
      ztp.setProgressText(GV.getOperateMsg("zipTestProgressInfo019", PubFunction.getFileLength(tmpFile.length())));
      JobThreads.publishProgressMsg(GV.getOperateMsg("zipTestProgressInfo019", PubFunction.getFileLength(tmpFile.length())));
      try {
        md5 = PubFunction.getFileMD5DigestHexStr(zipPath);
        ztp.setProgressText(GV.getOperateMsg("zipTestProgressInfo020", md5));
      } catch (Exception e) {
        e.printStackTrace();
        ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo021"));
        ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo022") + e.getMessage());
      }
    }
    long endTime = System.currentTimeMillis();
    if ((endTime - startTime) / 1000 / 60 >= 1) {
      String mins = ((int) (((endTime - startTime) / 1000.0 / 60.0) * 100)) / 100.0 + "";
      ztp.setProgressText(GV.getOperateMsg("zipTestProgressInfo023", mins));
    } else {
      String secs = (endTime - startTime) / 1000.0 + "";
      ztp.setProgressText(GV.getOperateMsg("zipTestProgressInfo024", secs));
    }
    ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo025"));
    ztp.setZipFileChecking(false);
    if (notOkCount > 0 || fileCount == 0) {
      ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo026"));
      ztp.setCheckPass(false);
    } else {
      ztp.setProgressText(GV.getSimpleMsg("zipTestProgressInfo027"));
      ztp.setCheckPass(true);
    }
    return ztp;
  }

  public static String getIsMasterMustCoded() {
    return isMasterMustCoded;
  }

  public static String getIsProviderMustCoded() {
    return isProviderMustCoded;
  }

  public static boolean getIsMustEncode() {
    return isMustCoded;
  }

  public static Process getPreTrantsProcess() {
    return preTrantsProcess;
  }

  public static Process getUploadBidProcess() {
    return uploadBidProcess;
  }

  public static void toDestroyStartedProcess() {
    if (JobThreads.getPreTrantsProcess() != null) {
      JobThreads.getPreTrantsProcess().destroy();
    }
    if (JobThreads.getUploadBidProcess() != null) {
      JobThreads.getUploadBidProcess().destroy();
    }
  }
}
