package com.ufgov.zc.client.zc.ztb.fileResumeBroken.authentication;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.caucho.hessian.client.HessianConnectionException;
import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.client.HessianRuntimeException;
import com.snca.assClient.CertificateAuthorityServicesClient;
import com.snca.assClient.CertificateAuthorityServicesPortType;
import com.ufgov.zc.client.util.PwdUtil;
import com.ufgov.zc.client.zc.ztb.JobThreads;
import com.ufgov.zc.client.zc.ztb.P;
import com.ufgov.zc.client.zc.ztb.fileResumeBroken.TestFile.IfFileExist;
import com.ufgov.zc.client.zc.ztb.fileResumeBroken.securityLogin.SignatureByUsbKey;
import com.ufgov.zc.client.zc.ztb.model.LevelOneItem;
import com.ufgov.zc.client.zc.ztb.model.LevelTwoModel;
import com.ufgov.zc.client.zc.ztb.service.LevelOneTwoBuilder;
import com.ufgov.zc.client.zc.ztb.service.ServerProjectService;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.zc.Communication;
import com.ufgov.zc.common.zc.SendFile;

public class UserVerify extends JDialog {
  private static final long serialVersionUID = 1L;

  private static boolean isDataSending = true;

  private static final int TEXT_FIELD_HEIGHT = 24;

  private Map returnMap = new HashMap();

  private JPanel jContentPane = null;

  private JButton LoginButton = null;

  private JButton CancelButton = null;

  private JLabel userLabel = null;

  private JLabel pwdLabel = null;

  private JTextField userJTextField = null;

  private JPasswordField jPasswordField = null;

  private Map parameterMap = new HashMap();

  private JLabel providerLabel = null;

  private JLabel fileNameLabel = null;

  private JTextField providerJTextField = null;

  private JTextField fileNameJTextField = null;

  private JProgressBar fileUploadJProgressBar = null;

  private JLabel fileUploadLabel = null;

  private JFileChooser jFileChooser = null;

  private JButton openFileButton = null;

  private Map returnValue = new HashMap();

  private JLabel fileLenStrLabel;

  private JLabel uploadSpeedStrLabel;

  private JTabbedPane jTabbedPane;

  private JPanel P1;

  private JPanel P2;

  private JLabel caUserLabel;

  private JComboBox caJComboBox;

  private DefaultComboBoxModel model;

  private SignatureByUsbKey s;

  private Hashtable scs;

  private String[] item = new String[20]; //证书个数一般情况下不会超出100个

  public Map getReturnValue() {
    return returnValue;
  }

  public void setReturnValue(Map returnValue) {
    this.returnValue = returnValue;
  }

  public Map getReturnMap() {
    return returnMap;
  }

  public void setReturnMap(Map returnMap) {
    this.returnMap = returnMap;
  }

  private JButton getLoginButton() {
    if (LoginButton == null) {
      LoginButton = new JButton();
      LoginButton.setBounds(new Rectangle(158, 260, 94, 30));
      LoginButton.setText(GV.getTranslate("loginAndUpload"));
      LoginButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          performFileUpload();
        }
      });
    }
    return LoginButton;
  }

  protected void performFileUpload() {
    final String msgDTitle = GV.getTranslate("messageDialogTitle");
    LoginButton.setEnabled(false);
    if (!checkFileExist()) {
      JOptionPane.showMessageDialog(null, GV.getSimpleMsg("fileNotExist"), msgDTitle, JOptionPane.ERROR_MESSAGE, null);
      LoginButton.setEnabled(true);
      return;
    }
    //检查文件大小
    int minimumFileSize = 1024 * 1024 * 2;
    String size = (String) parameterMap.get("MINIMUMFILESIZE");
    if (size != null && !"".equals(size)) {
      minimumFileSize = Integer.parseInt(size);
    }
    if (!checkFileSize(minimumFileSize)) {
      String msg = GV.getOperateMsg("fileSizeCheck", (minimumFileSize / (1024 * 1024)) + "M");
      int sel = JOptionPane.showConfirmDialog(null, msg, msgDTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (sel == JOptionPane.NO_OPTION) {
        LoginButton.setEnabled(true);
        return;
      }
    }
    Runnable run = new Runnable() {
      public void run() {
        FileRelateInformation fileRelateInformation = new FileRelateInformation(parameterMap);
        parameterMap = fileRelateInformation.getFileRelateInformationMap();
        if ("CA_USER".equalsIgnoreCase(jTabbedPane.getSelectedComponent().getName())) {
          fileUploadJProgressBar.setString(GV.transStatusCode("CA_AUTH_READY"));
          fileUploadJProgressBar.setString(GV.transStatusCode("CA_AUTH_PWD"));
          String identifyCode = "";
          try {
            identifyCode = genVerifyCode(caJComboBox.getSelectedIndex() + 1);
          } catch (Exception e) {
            String err = GV.getSimpleMsg("CAReadErr") + e.getMessage();
            JOptionPane.showMessageDialog(null, err, msgDTitle, JOptionPane.ERROR_MESSAGE, null);
            fileUploadJProgressBar.setString(e.getMessage());
            LoginButton.setEnabled(true);
            return;
          }
          System.out.println("UserVerify 161:" + identifyCode);
          //通过webService得到验证码
          fileUploadJProgressBar.setString(GV.transStatusCode("CA_AUTH"));
          CertificateAuthorityServicesClient client = new CertificateAuthorityServicesClient();
          CertificateAuthorityServicesPortType service = client.getCertificateAuthorityServicesHttpSoap11Endpoint();
          try {
            String caCode = service.verifySignCS(identifyCode);
            System.out.println("UserVerify 168 test client completed:" + caCode);
            parameterMap.put("VERIFYID", caCode);
            fileUploadJProgressBar.setString(GV.transStatusCode("CA_AUTH_OK"));
          } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, GV.getSimpleMsg("CAAuthErr") + e.getMessage(), msgDTitle, JOptionPane.ERROR_MESSAGE, null);
            LoginButton.setEnabled(true);
            return;
          }
        } else if ("COMMON_USER".equalsIgnoreCase(jTabbedPane.getSelectedComponent().getName())) {
          if (userNull()) {
            JOptionPane.showMessageDialog(null, GV.getSimpleMsg("userNameNotNull"), msgDTitle, JOptionPane.ERROR_MESSAGE, null);
            LoginButton.setEnabled(true);
            return;
          }
          parameterMap.put("USERID", userJTextField.getText());
          if (jPasswordField.getText() != null) {
            parameterMap.put("PASSWORD", PwdUtil._encodePwd(jPasswordField.getText()));
          } else {
            parameterMap.put("PASSWORD", jPasswordField.getText());
          }
          parameterMap.put("VERIFYID", "NOID");
        }
        try {
          jTabbedPane.setEnabled(false);
        } catch (Exception e) {
          e.printStackTrace();
        }
        isDataSending = true;
        parameterMap.put("ISREPLACE", "false");
        parameterMap.put("LOCALFILETOTALLEN", getFileSize());
        fileUploadJProgressBar.setString(GV.transStatusCode("USER_AUTH"));
        Transfer transfer = new Transfer(parameterMap, "authentication");
        transfer.startTransfer();
        returnMap = transfer.getReturnMap();
        fileUploadJProgressBar.setString(GV.transStatusCode("USER_AUTH_OK"));
        if (returnMap.get("ERRORMESSAGE") != null && !"".equals(returnMap.get("ERRORMESSAGE"))) {
          JOptionPane.showMessageDialog(null, returnMap.get("ERRORMESSAGE"), msgDTitle, JOptionPane.ERROR_MESSAGE, null);
          LoginButton.setEnabled(true);
          return;
        }
        System.out.println("Client returnMap: userPass: " + returnMap.get("USERPASS"));
        if (returnMap.get("USERPASS") != null && !"".equals(returnMap.get("USERPASS"))) {
          if (returnMap.get("USERPASS").equals("false")) {
            fileUploadJProgressBar.setString(GV.transStatusCode("USER_AUTH_FAIL"));
            JOptionPane.showMessageDialog(null, GV.getSimpleMsg("identityCheckNotPass") + returnMap.get("FAILREASON"), msgDTitle, 0, null);
            LoginButton.setEnabled(true);
            return;
          }
        }
        fileUploadJProgressBar.setString(GV.transStatusCode("USER_AUTH_PASS"));
        String message = "";
        //验证文件是否已上传过
        String uploadType = (String) returnMap.get("UPLOADTYPE");
        if ("CONTINUE".equalsIgnoreCase(uploadType)) {
          message = GV.getSimpleMsg("goonToUploadFile");
          fileUploadJProgressBar.setString(message);
        } else if ("INIT".equalsIgnoreCase(uploadType)) {
          message = GV.getSimpleMsg("firstToUploadFile");
          fileUploadJProgressBar.setString(message);
        } else if ("COVER".equalsIgnoreCase(uploadType)) {
          message = GV.getSimpleMsg("toCoverExistFile");
          int sel = JOptionPane.showConfirmDialog(null, message, msgDTitle, 0, 2);
          if (sel == JOptionPane.NO_OPTION) {
            LoginButton.setEnabled(true);
            return;
          }
          message = GV.getSimpleMsg("willCoverExistFile");
          fileUploadJProgressBar.setString(message);
          parameterMap.put("ISREPLACE", "true");
          fileUploadJProgressBar.setString(GV.getSimpleMsg("updateServerDataPrepare"));
          transfer = new Transfer(parameterMap, "authentication");
          transfer.startTransfer();
          returnMap = transfer.getReturnMap();
          fileUploadJProgressBar.setString(GV.getSimpleMsg("updateServerDataFinish"));
        }
        returnMap.put("FILENAME", parameterMap.get("FILENAME"));
        returnMap.put("FILEPATH", parameterMap.get("FILEPATH"));
        providerJTextField.setText((String) returnMap.get("PROVIDERNAME"));
        returnMap.put("PROJECTCODE", (String) parameterMap.get("PROJECTCODE"));
        returnMap.put("PACKCODE", (String) parameterMap.get("PACKCODE"));
        returnMap.put("URL", (String) parameterMap.get("URL"));
        fileUploadJProgressBar.setString(GV.getSimpleMsg("updateLocalConfig"));
        toUpdateLevel2xmlFile(returnMap);
        fileUploadJProgressBar.setString(GV.getSimpleMsg("readingLocalData"));
        String path = fileNameJTextField.getText();
        File file = new File(path);
        FileInputStream fileInStream = null;
        try {
          fileInStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
          String msg = GV.getSimpleMsg("fileToReadNotExist") + path;
          JOptionPane.showMessageDialog(null, msg, msgDTitle, 0, null);
        }
        Map<String, String> paras = new HashMap<String, String>();
        paras.put("UPLOADSTATUS", LevelOneItem.UPLOAD_STATUS_BIDDING);
        paras.put("SUMMITSTATUS", LevelOneItem.SUMMIT_STATUS_NOW);
        if ("INIT".equalsIgnoreCase(uploadType) || "COVER".equalsIgnoreCase(uploadType)) {
          SimpleDateFormat sdf = new SimpleDateFormat(ZcSettingConstants.SIMPLE_DATE_FORMAT_FULL);
          paras.put("STARTUPLOADTIME", sdf.format(new Date()));
        }
        fileUploadJProgressBar.setString(GV.getSimpleMsg("updateLocalConfig"));
        LevelOneTwoBuilder.toUpdateLevelOneXmlFile((String) parameterMap.get("PROJECTCODE"), (String) parameterMap.get("PACKCODE"), paras);
        int len = 0; //每次读取的长度
        int bufLen = 1024 * 1024 * 1;
        byte[] buf = new byte[bufLen]; //读取数据的缓存块
        IfFileExist ifFileExist = IfFileExist.getIfFileExist();
        //分块读取文件
        try {
          int localFileTotalLen = fileInStream.available();
          returnMap.put("LOCALFILETOTALLEN", String.valueOf(localFileTotalLen));
          String serverFileLen = (String) returnMap.get("SERVERFILELEN");
          if (serverFileLen == null) {
            serverFileLen = "0";
          }
          int transFileSumLen = Integer.parseInt(serverFileLen);
          int skipLen = 0;
          int i = 0;
          String sequenceOrder = "";
          if ("0".equals(returnMap.get("SERVERFILELEN"))) {
            sequenceOrder = "1";//第一次上传
          } else {
            sequenceOrder = "2";//不是第一次上传
            skipLen = Integer.parseInt((String) returnMap.get("SERVERFILELEN"));
          }
          System.out.println(returnMap.get("SERVERFILEPATH"));
          String thisFileName = (String) returnMap.get("FILENAME");
          String thisFilePath = (String) returnMap.get("SERVERFILEPATH");
          String thisFileId = thisFilePath.substring(thisFilePath.lastIndexOf("/") + 1, thisFilePath.length());
          if (skipLen == localFileTotalLen) {
            LoginButton.setEnabled(false);
            Map map = new HashMap();
            map.put("FILEPATH", thisFilePath);
            map.put("FILEID", thisFileId);
            map.put("FILENAME", thisFileName);
            map.put("URL", parameterMap.get("URL"));
            Transfer tran = new Transfer(map, "testFileInDatabase");
            tran.startTransfer();
            map = tran.getReturnMap();
            if (map.get("IFSUCCESS") == null && "".equals(map.get("IFSUCCESS"))) {
              JOptionPane.showMessageDialog(null, GV.getSimpleMsg("fileInfoInsertErr"), msgDTitle, JOptionPane.OK_OPTION, null);
              LoginButton.setEnabled(true);
              return;
            }
            ifFileExist.setFileTransSuccess("1");
            JOptionPane.showMessageDialog(null, GV.getSimpleMsg("fileNotNeedUpload"), msgDTitle, JOptionPane.OK_OPTION, null);
            returnValue.put("USERID", returnMap.get("USERID"));
            returnValue.put("FILEID", thisFileId);
            returnValue.put("FILENAME", thisFileName);
            doPrepareUpdateData();
          } else {
            int blockCount = 0; //发送的文件块数统计
            int sentLen = 0; //已发送数据长度
            int offset = skipLen;//起始偏移量
            int intCount = 0; //中断次数
            boolean isSameINT = false;//是否处于同一次中断
            long sTime = System.currentTimeMillis();
            fileInStream.skip(skipLen);
            while ((len = fileInStream.read(buf)) != -1) {
              if (!isDataSending) {
                LoginButton.setEnabled(true);
                return;
              }
              LoginButton.setEnabled(false);
              ++blockCount;
              fileUploadJProgressBar.setString(GV.getOperateMsg("readingDataPackage", blockCount + ""));
              String status = null;
              returnMap.put("BLOCK_OFFSET", offset + "");
              boolean isContinue = true;
              while (isContinue) {
                if (!isDataSending) {
                  LoginButton.setEnabled(true);
                  return;
                }
                if (!"NET_INT".equals(status)) {
                  int progress = (int) (100 * ((skipLen + sentLen) / (double) localFileTotalLen));
                  fileUploadJProgressBar.setString(GV.getOperateMsg("sendingDataPackage", blockCount + "") + progress + "%");
                }
                if (len == bufLen) {
                  status = fileUpload(returnMap, buf, sequenceOrder);
                } else {//一般来说，这个分支只会在最后一块数据时执行
                  byte[] buff = new byte[len];
                  for (int j = 0; j < len; j++) {
                    buff[j] = buf[j];
                  }
                  status = fileUpload(returnMap, buff, sequenceOrder);
                }
                String tsStatus = (String) returnValue.get("TS_STATUS");
                if ("NET_INT".equals(status) && !isSameINT) {
                  isSameINT = true;
                  intCount++;
                } else if ("TS_OK".equals(status)) {
                  isContinue = false;
                  isSameINT = false;
                  offset += len;
                }
                if ("SERVER_WRITE_ERR".equals(tsStatus)) {
                  LoginButton.setEnabled(true);
                  JOptionPane.showMessageDialog(null, GV.getOperateMsg("dataPackageTransErr", (String) returnValue.get("ERR_CONTENT")));
                  return;
                }
                fileUploadJProgressBar.setString(GV.transStatusCode(status) + GV.getSimpleMsg("tellToRetry"));
                long cTime = System.currentTimeMillis();
                uploadSpeedStrLabel.setText(getSpeed(sentLen, cTime - sTime) + "中断次数:" + intCount + "次.");
              }
              isSameINT = false;
              sequenceOrder = "2";
              i++;
              transFileSumLen += len;
              sentLen += len;
              long cTime = System.currentTimeMillis();
              uploadSpeedStrLabel.setText(getSpeed(sentLen, cTime - sTime) + "中断次数:" + intCount + "次.");
              int x = (int) (100 * (transFileSumLen / (double) localFileTotalLen));
              fileUploadJProgressBar.setValue(x);
              fileUploadJProgressBar.setString(GV.transStatusCode(status) + "[数据包" + blockCount + "]" + x + "%");
              if (transFileSumLen == localFileTotalLen) {
                fileUploadJProgressBar.setString(GV.getSimpleMsg("dataSentFinish"));
                ifFileExist.setFileTransSuccess("1");
                returnValue.put("FILEID", thisFileId);
                doPrepareUpdateData();
              }
            }
          }
            fileInStream.close();
            JOptionPane.showMessageDialog(null, GV.getSimpleMsg("dataPackageSentFinish"));
        } catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(null, GV.getSimpleMsg("dataPackageSentErr") + returnValue.get("ERR_CONTENT") + "\n" + e.getMessage());
          LoginButton.setEnabled(true);
          return;
        }
      }

      private String getSpeed(int sentLen, long time) {
        double fileLenKB = (double) sentLen / (double) (1024);
        double timeSecond = (double) time / 1000.0;
        String result = String.format("%.2f", fileLenKB / timeSecond);
        return "平均速度：" + result + "KB/S,";
      }
    };
    Thread thread = new Thread(run);
    thread.setDaemon(true);
    thread.start();
  }

  /**
   * 更新二级xml文件中的供应商及服务器端路径信息
   *
   * @param returnMap
   */
  protected void toUpdateLevel2xmlFile(Map returnMap) {
    String projCode = (String) returnMap.get("PROJECTCODE");
    String packCode = (String) returnMap.get("PACKCODE");
    String level2fileName = LevelOneTwoBuilder.getLevelTwoXmlPath(projCode, packCode);
    level2fileName = (new File(level2fileName)).getName();
    LevelTwoModel ltm = new LevelTwoModel();
    ltm.setSupplierCode((String) returnMap.get("PROVIDERCODE"));
    ltm.setSupplierName((String) returnMap.get("PROVIDERNAME"));
    ltm.setServerFileFullPath((String) returnMap.get("SERVERFILEUPLOADSPATH"));
    ltm.setProjCode(projCode);
    ltm.setPackCode(packCode);
    LevelOneTwoBuilder xmlBuilder = new LevelOneTwoBuilder();
    xmlBuilder.updateLevelTwoXmlFile(ltm);
  }

  /**
   * 文件上传成功后准备更新相关的业务数据
   */
  private void doPrepareUpdateData() {
    this.setVisible(false);
    returnValue.putAll(parameterMap);
    returnValue.putAll(returnMap);
    returnValue.put("PACKCODES", returnValue.get("PACKCODE"));
    P.pm(returnValue);
    if (returnValue.get("FILEID") != null && !"".equals(returnValue.get("FILEID"))) {
      try {
        Map res = (new ServerProjectService()).finishOtherBusiness(returnValue);
        returnValue.putAll(res);
      } catch (Exception e) {
        e.printStackTrace();
        returnValue.put("UPDATESTATUS", "fail");
        returnValue.put("FAILREASON", e.getMessage());
      }
    } else {
      returnValue.put("UPDATESTATUS", "fail");
      returnValue.put("FAILREASON", GV.getSimpleMsg("tbBookUploadErr"));
    }
    JobThreads.createTouBiaoFeedBack(returnValue);
  }

  /**
   * 进行文件上传
   *
   * @param parameterMap
   * @param buff
   * @param sequenceOrder
   * @throws Exception
   */
  public String fileUpload(Map paraMap, byte[] buff, String sequenceOrder) throws Exception {
    String url = "";
    url = (String) paraMap.get("URL");
    url = url + "communication";
    //toTestAccessFile(parameterMap, buff);
    //return "TS_OK";
    try {
      paraMap.put("BUFFCONTENT", buff);
      //paraMap.put("functiondto", "uploadtbfile");
      paraMap.put("sequenceorder", sequenceOrder);
      HessianProxyFactory factory = new HessianProxyFactory();
      Communication communication = (Communication) factory.create(Communication.class, url);
      returnValue.putAll(communication.buildCommunication(paraMap, "uploadtbfile"));
    } catch (MalformedURLException e) {
      throw new Exception(GV.getSimpleMsg("errLinkShouldCheck") + url);
    } catch (HessianConnectionException e1) {
      return "NET_INT";
    } catch (HessianRuntimeException e) {
      return "NET_INT";
    }
    return "TS_OK";
  }

  /**
   * 进行文件上传
   *
   * @param parameterMap
   * @param buff
   * @param sequenceOrder
   * @throws Exception
   */
  public String fileUpload_(Map paraMap, byte[] buff, String sequenceOrder) throws Exception {
    String url = "";
    url = (String) paraMap.get("URL");
    url = url + "sendFile";
    //toTestAccessFile(parameterMap, buff);
    //return "TS_OK";
    HessianProxyFactory factory = new HessianProxyFactory();
    try {
      SendFile sendFile = (SendFile) factory.create(SendFile.class, url);
      paraMap.put("BUFFCONTENT", buff);
      returnValue.putAll(sendFile.sendFileByStream(paraMap, buff, sequenceOrder));
    } catch (MalformedURLException e) {
      throw new Exception(GV.getSimpleMsg("errLinkShouldCheck") + url);
    } catch (HessianConnectionException e1) {
      return "NET_INT";
    } catch (HessianRuntimeException e) {
      return "NET_INT";
    }
    return "TS_OK";
  }

  /*  private void toTestAccessFile(Map parameterMap2, byte[] buff) {
    String offset = (String) parameterMap2.get("BLOCK_OFFSET");
    int blockOffset = Integer.parseInt(offset);
    try {
      FileAccessI oSavedFile = new FileAccessI("d://test.tmp", blockOffset);
      oSavedFile.write(buff, 0, buff.length);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }*/

  private boolean checkFileExist() {
    String filePath = (String) this.parameterMap.get("FILEPATH");
    File file = new File(filePath);
    if (file.exists() && file.isFile()) {
      return true;
    }
    return false;
  }

  private long getFileSize() {
    String filePath = (String) this.parameterMap.get("FILEPATH");
    File file = new File(filePath);
    return file.length();
  }

  private boolean checkFileSize(int fileSize) {
    return getFileSize() >= fileSize;
  }

  public boolean userNull() {
    boolean ifNull = true;
    if (userJTextField.getText() != null && !"".equals(userJTextField.getText().trim())) {
      userJTextField.setText(userJTextField.getText().trim());
      ifNull = false;
    }
    return ifNull;
  }

  public boolean passwordNull() {
    boolean ifNull = true;
    if (jPasswordField.getPassword() != null && !"".equals(jPasswordField.getPassword().length > 0)) {
      jPasswordField.setText(jPasswordField.getPassword().toString().trim());
      ifNull = false;
    }
    return ifNull;
  }

  private JButton getCancelButton() {
    if (CancelButton == null) {
      CancelButton = new JButton();
      CancelButton.setBounds(new Rectangle(254, 260, 80, 30));
      CancelButton.setText(GV.getTranslate("exit"));
      CancelButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          beforeClose();
        }
      });
    }
    return CancelButton;
  }

  private JTextField getUserJTextField() {
    if (userJTextField == null) {
      userJTextField = new JTextField();
      userJTextField.setBounds(new Rectangle(128, 12, 140, TEXT_FIELD_HEIGHT));
    }
    return userJTextField;
  }

  private JPasswordField getJPasswordField() {
    if (jPasswordField == null) {
      jPasswordField = new JPasswordField();
      jPasswordField.setBounds(new Rectangle(128, 42, 140, TEXT_FIELD_HEIGHT));
      jPasswordField.setBackground(Color.WHITE);
    }
    return jPasswordField;
  }

  private JTextField getProviderJTextField() {
    if (providerJTextField == null) {
      providerJTextField = new JTextField();
      providerJTextField.setBounds(new Rectangle(80, 25, 360, TEXT_FIELD_HEIGHT));
      providerJTextField.setEditable(false);
    }
    return providerJTextField;
  }

  private JTextField getFileNameJTextField() {
    if (fileNameJTextField == null) {
      fileNameJTextField = new JTextField();
      fileNameJTextField.setBounds(new Rectangle(80, 55, 300, TEXT_FIELD_HEIGHT));
      fileNameJTextField.setText((String) parameterMap.get("FILEPATH"));
      File file = new File((String) parameterMap.get("FILEPATH"));
      double fileLen = (double) file.length() / (double) (1024 * 1024);
      String result = String.format("%.2f", fileLen);
      fileLenStrLabel.setText(getFileStatus(result, file.getName()));
      fileNameJTextField.setEditable(false);
    }
    return fileNameJTextField;
  }

  private String getFileStatus(String size, String name) {
    if (name.endsWith(GV.SUFFIX_MERGER_FILE)) {
      return "<html>文件状态：" + size + "MB," + "<font color='black'><b>已加密;</b></font></html>";
    } else {
      return "<html>文件状态：" + size + "MB," + "<font color='red'><b>未加密;</b></font></html>";
    }
  }

  private JProgressBar getFileUploadJProgressBar() {
    if (fileUploadJProgressBar == null) {
      fileUploadJProgressBar = new JProgressBar(0, 100);
      fileUploadJProgressBar.setStringPainted(true);
      fileUploadJProgressBar.setBounds(new Rectangle(90, 305, 354, 30));
    }
    return fileUploadJProgressBar;
  }

  private JButton getOpenFileButton() {
    if (openFileButton == null) {
      openFileButton = new JButton();
      openFileButton.setBounds(new Rectangle(370, 55, 70, TEXT_FIELD_HEIGHT));
      openFileButton.setText(GV.getTranslate("brower"));
      openFileButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          if (jFileChooser == null) {
            jFileChooser = new JFileChooser();
          }
          jFileChooser.setDialogTitle(GV.getSimpleMsg("fileChooseTitle"));
          jFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
          jFileChooser.setSelectedFile(new File((String) parameterMap.get("FILEPATH")));
          int returnVal = jFileChooser.showOpenDialog(new JFrame());
          if (returnVal == JFileChooser.APPROVE_OPTION) {
            fileNameJTextField.setText(jFileChooser.getSelectedFile().getAbsolutePath());
            returnMap.put("FILEPATH", jFileChooser.getSelectedFile().getAbsolutePath());
            returnMap.put("FILENAME", jFileChooser.getSelectedFile().getName());
            parameterMap.put("FILEPATH", jFileChooser.getSelectedFile().getAbsolutePath());
            parameterMap.put("FILENAME", jFileChooser.getSelectedFile().getName());
            File file = new File(jFileChooser.getSelectedFile().getAbsolutePath());
            double fileLen = (double) (file.length()) / (double) (1024 * 1024);
            String result = String.format("%.2f", fileLen);
            fileLenStrLabel.setText("文件大小：" + result + "MB.");
          }
        }
      });
    }
    return openFileButton;
  }

  private JTabbedPane getJTabbedPane() {
    if (jTabbedPane == null) {
      jTabbedPane = new JTabbedPane();
      jTabbedPane.setBounds(new Rectangle(60, 136, 360, 116));
      jTabbedPane.addTab(GV.getSimpleMsg("tabCAInfo"), getP2());
      jTabbedPane.addTab(GV.getSimpleMsg("tabCommonInfo"), getP1());
//      uKey();//by chenjl 2013-08-28 暂不使用ca证书
      jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent e) {
          JTabbedPane pane = (JTabbedPane) e.getSource();
          int index = pane.getSelectedIndex();
          if (index == 0) {
            uKey();
          }
        }
      });
    }
    return jTabbedPane;
  }

  private JPanel getP1() {
    if (P1 == null) {
      P1 = new JPanel();
      P1.setName("COMMON_USER");
      P1.setLayout(null);
      P1.add(userLabel, null);
      P1.add(getUserJTextField(), null);
      P1.add(pwdLabel, null);
      P1.add(getJPasswordField(), null);
    }
    return P1;
  }

  private JPanel getP2() {
    if (P2 == null) {
      caUserLabel = new JLabel();
      caUserLabel.setBounds(new Rectangle(10, 12, 71, TEXT_FIELD_HEIGHT));
      caUserLabel.setText(GV.getSimpleMsg("digitalCertificateLabel"));
      P2 = new JPanel();
      P2.setName("CA_USER");
      P2.setLayout(null);
      P2.add(caUserLabel, null);
      P2.add(getCaJComboBox(), null);
    }
    return P2;
  }

  private JComboBox getCaJComboBox() {
    if (caJComboBox == null) {
      caJComboBox = new JComboBox();
      caJComboBox.setBounds(new Rectangle(70, 13, 270, TEXT_FIELD_HEIGHT));
    }
    return caJComboBox;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new UserVerify();
      }
    });
  }

  public UserVerify() {
  }

  public UserVerify(Map parameterMap) {
    super();
    this.parameterMap = toUpperCaseParas(parameterMap);
  }

  private Map toUpperCaseParas(Map parasMap) {
    Map retMap = new HashMap();
    Iterator it = parasMap.keySet().iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      Object value = parasMap.get(key);
      String KEY = key.toUpperCase();
      retMap.put(KEY, value);
    }
    return retMap;
  }

  public void getStart() {
    this.setSize(480, 384);
    this.setContentPane(getJContentPane());
    this.setTitle(GV.getSimpleMsg("bidUploadTitle"));
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.setModal(true);
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        beforeClose();
      }
    });
    this.setVisible(true);
  }

  private JPanel getJContentPane() {
    if (jContentPane == null) {
      fileLenStrLabel = new JLabel();
      fileLenStrLabel.setBounds(new Rectangle(30, 84, 180, 26));
      fileLenStrLabel.setText(GV.getSimpleMsg("fileStatusLabel"));
      fileUploadLabel = new JLabel();
      fileUploadLabel.setBounds(new Rectangle(26, 305, 92, 30));
      fileUploadLabel.setText(GV.getSimpleMsg("uploadProgressLabel"));
      fileNameLabel = new JLabel();
      fileNameLabel.setBounds(new Rectangle(30, 54, 56, TEXT_FIELD_HEIGHT));
      fileNameLabel.setText(GV.getSimpleMsg("fileNameLabel"));
      providerLabel = new JLabel();
      providerLabel.setBounds(new Rectangle(30, 24, 56, TEXT_FIELD_HEIGHT));
      providerLabel.setText(GV.getSimpleMsg("supplierNameLabel"));
      pwdLabel = new JLabel();
      pwdLabel.setText(GV.getSimpleMsg("passwdLabel"));
      pwdLabel.setBounds(new Rectangle(76, 42, 56, TEXT_FIELD_HEIGHT));
      userLabel = new JLabel();
      userLabel.setText(GV.getSimpleMsg("userNameLabel"));
      userLabel.setBounds(new Rectangle(76, 12, 56, TEXT_FIELD_HEIGHT));
      jContentPane = new JPanel();
      jContentPane.setLayout(null);
      jContentPane.add(getLoginButton(), null);
      jContentPane.add(getCancelButton(), null);
      jContentPane.add(providerLabel, null);
      jContentPane.add(fileNameLabel, null);
      jContentPane.add(getProviderJTextField(), null);
      jContentPane.add(getFileNameJTextField(), null);
      jContentPane.add(getFileUploadJProgressBar(), null);
      jContentPane.add(fileUploadLabel, null);
      jContentPane.add(getOpenFileButton(), null);
      jContentPane.add(fileLenStrLabel, null);
      jContentPane.add(getUploadSpeedLabel(), null);
      jContentPane.add(getJTabbedPane(), null);
    }
    return jContentPane;
  }

  private JLabel getUploadSpeedLabel() {
    uploadSpeedStrLabel = new JLabel();
    uploadSpeedStrLabel.setBounds(new Rectangle(30, 110, 400, 26));
    uploadSpeedStrLabel.setPreferredSize(new Dimension(400, TEXT_FIELD_HEIGHT));
    uploadSpeedStrLabel.setText("平均速度：0KB/S,中断次数：0次；");
    return uploadSpeedStrLabel;
  }

  public void beforeClose() {
    Toolkit.getDefaultToolkit().beep();
    String ask = GV.getSimpleMsg("sureToExit");
    String confirm = GV.getSimpleMsg("exitAskConfirm");
    int answer = JOptionPane.showConfirmDialog(UserVerify.this, ask, confirm, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (answer == JOptionPane.YES_OPTION) {
      isDataSending = false;
      this.setVisible(false);
    }
    if (answer == JOptionPane.NO_OPTION) {
      return;
    }
  }

  public void uKey() {
    s = new SignatureByUsbKey();
    scs = s.getSignCertificates();
    int count = 0;
    // 这里是需要形成的证书列表的选择界面，然后显示证书列表信息，列表信息需以keySet序号保持一致
    for (Iterator i = scs.keySet().iterator(); i.hasNext();) {
      String alias = (String) i.next();
      if (alias == null || "".equals(alias))
        continue;
      X509Certificate cert = (X509Certificate) scs.get(alias);
      //取其中的有用字符，并拼装
      System.out.println("UserVerify 805:" + alias);
      String[] strArr = cert.getSubjectDN().toString().split(",");
      String tempStr = "";
      int k = 0;
      if (strArr.length >= 4) {
        k = strArr[3].indexOf("C=");
        tempStr = tempStr + strArr[3].substring(k + 2);
      }
      if (strArr.length >= 3) {
        k = strArr[2].indexOf("ST=");
        tempStr = tempStr + strArr[2].substring(k + 3);
      }
      if (strArr.length >= 2) {
        k = strArr[1].indexOf("L=");
        tempStr = tempStr + strArr[1].substring(k + 2);
      }
      if (strArr.length >= 1) {
        k = strArr[0].indexOf("CN=");
        tempStr = tempStr + strArr[0].substring(k + 3);
      }
      System.out.println("UserVerify 810:" + tempStr);
      //将数据证书名称放到下拉列表中
      item[count] = tempStr;
      ++count;
      System.out.print(count + " - Alias:" + alias);
      System.out.println(" Cert:" + cert.getSubjectDN());
    }
    model = new DefaultComboBoxModel(item);
    caJComboBox.setModel(model);
  }

  public String genVerifyCode(int select) throws Exception {
    String signstr = "";
    int index = 0;
    String al = "";
    for (Iterator i = scs.keySet().iterator(); i.hasNext();) {
      String alias = (String) i.next();
      //X509Certificate cert = (X509Certificate) scs.get(alias);
      ++index;
      if (index == select) {
        al = alias;
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String datetime = tempDate.format(new Date());
        signstr = s.signByAlias(al, datetime);
      }
    }
    return signstr;
  }

  public Map getParameterMap() {
    return parameterMap;
  }

  public void setParameterMap(Map parameterMap) {
    this.parameterMap = parameterMap;
  }
}
