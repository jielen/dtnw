package com.ufgov.zc.client.zc.ztb.util;

import com.snca.financebidding.FinanceBiddingCall;
import com.thoughtworks.xstream.XStream;
import com.ufgov.zc.client.zc.ztb.DataChecker;
import com.ufgov.zc.client.zc.ztb.JobThreads;
import com.ufgov.zc.client.zc.ztb.ZipDirFile;
import com.ufgov.zc.client.zc.ztb.component.ProjectInfoPanel;
import com.ufgov.zc.client.zc.ztb.fileResumeBroken.fileOperate.MD5Util;
import com.ufgov.zc.client.zc.ztb.model.BusinessProject;
import com.ufgov.zc.client.zc.ztb.model.DetailsType;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;
import com.ufgov.zc.client.zc.ztb.model.Templates;
import com.ufgov.zc.common.zc.model.ZcZBFileTemplate;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PubFunction {
  public static String trim(Object str) {
    return (str == null) ? "" : ((String) str).trim();
  }

  /**
   * 拷贝文件
   *
   * @param source
   * @param dest
   * @throws Exception
   */
  public static void copyFiles(String source, String dest) throws Exception {
    File srcFile = new File(source);
    if (!srcFile.exists()) {
      return;
    }
    String ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_COPYING);
    String fileName = srcFile.getName();
    if (fileName.startsWith("~") || fileName.toLowerCase().startsWith("tplinfo")) {
      return;
    }
    String fileLen = getFileLength(srcFile.length());
    JobThreads.publishProgressMsg(ps + fileName + "--" + fileLen);
    File destFile = new File(dest);
    if (srcFile.isFile()) {
      if (srcFile.length() == 0) {
        destFile.createNewFile();
        return;
      }
      FileInputStream fis = null;
      FileOutputStream fos = null;
      try {
        fis = new FileInputStream(srcFile);
        fos = new FileOutputStream(destFile);
        int b = -1;
        byte[] buffer = new byte[GV.BUFFER_SIZE];
        while ((b = fis.read(buffer)) != -1) {
          fos.write(buffer, 0, b);
          double percent = getPercent(destFile.length(), srcFile.length());
          JobThreads.publishProgressMsg(ps + fileName + "--共【" + fileLen + "】已完成【" + percent + "%】");
        }
      } catch (IOException e) {
        e.printStackTrace();
        throw e;
      } finally {
        try {
          fis.close();
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } else if (srcFile.isDirectory()) {
      if (!destFile.exists()) {
        if (!destFile.mkdirs()) {
          throw new Exception(GV.getSimpleMsg("directoryCreateErr"));
        }
      }
      String[] fileList = srcFile.list();
      for (int i = 0; i < fileList.length; i++) {
        copyFiles(source + File.separator + fileList[i], dest + File.separator + fileList[i]);
      }
    }
  }

  public static void deleteFile(File file) {
    if (!file.exists()) {
      return;
    }
    String ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_DELETING);
    JobThreads.publishProgressMsg(ps + file.getName());
    if (file.isDirectory()) {
      File[] fileList = file.listFiles();
      for (int i = 0; i < fileList.length; i++) {
        deleteFile(fileList[i]);
      }
      file.delete();
    } else {
      file.delete();
    }
  }

  public static void setShowInMiddle(JDialog dialog) {
    dialog.getWidth();
    dialog.getHeight();
    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screenSize = kit.getScreenSize();
    int width = (int) screenSize.getWidth();
    int height = (int) screenSize.getHeight();
    int w = dialog.getWidth();
    if (w == 0) {
      w = dialog.getPreferredSize().width;
    }
    int h = dialog.getHeight();
    if (h == 0) {
      h = dialog.getPreferredSize().height;
    }
    dialog.setLocation((width - w) / 2, (height - h) / 2);
  }

  public static void copyFile(String srcPath, String tarPath) throws Exception {
    if (srcPath == null || tarPath == null) {
      throw new Exception("缺少srcPath:" + srcPath + ",tarPath:" + tarPath + "中为空的参数！");
    }
    InputStream inStream = null;
    FileOutputStream fs = null;
    try {
      int byteread = 0;
      File srcFile = new File(srcPath);
      if (!srcFile.exists() && srcPath.toLowerCase().endsWith(GV.SUFFIX_DOC)) {
        String notExist = GV.getSimpleMsg("fileNotExistAutoCreate");
        String title = GV.getSimpleMsg("asking");
        int sel = JOptionPane.showConfirmDialog(null, srcFile.getAbsolutePath() + notExist, title, 0, 3);
        if (sel == JOptionPane.YES_OPTION) {
          if (!srcFile.createNewFile()) {
            throw new Exception(GV.getSimpleMsg("fileCreatedErrAndRestartToTry") + srcPath);
          }
        } else {
          throw new Exception(srcFile.getAbsolutePath() + GV.getSimpleMsg("notExistNotCopy"));
        }
      } else if (!srcFile.exists() && !srcPath.toLowerCase().endsWith(GV.SUFFIX_DOC)) {
        throw new Exception(srcFile.getAbsolutePath() + GV.getSimpleMsg("notExistNotCopy"));
      }
      String ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_COPYING);
      String fileLen = getFileLength(srcFile.length());
      JobThreads.publishProgressMsg(ps + srcFile.getName() + "--" + fileLen);
      checkAndMakeDirs(tarPath);
      inStream = new FileInputStream(srcFile.getAbsolutePath());
      fs = new FileOutputStream(tarPath);
      File destFile = new File(tarPath);
      byte[] buffer = null;
      if (srcFile.length() > 1024 * 1024 * 2) {
        buffer = new byte[1024 * 1024 * 2];
        while ((byteread = inStream.read(buffer)) != -1) {
          fs.write(buffer, 0, byteread);
          JobThreads.publishProgressMsg(ps + srcFile.getName() + "--【" + fileLen + "】已完成【" + getPercent(destFile.length(), srcFile.length()) + "%】");
        }
      } else if (srcFile.length() > 0 && srcFile.length() <= 1024 * 1024 * 2) {
        buffer = new byte[(int) srcFile.length()];
        while ((byteread = inStream.read(buffer)) != -1) {
          fs.write(buffer, 0, byteread);
          JobThreads.publishProgressMsg(ps + srcFile.getName() + "--【" + fileLen + "】已完成【" + getPercent(destFile.length(), srcFile.length()) + "%】");
        }
      } else {
        destFile.createNewFile();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (inStream != null) {
        inStream.close();
      }
      if (fs != null) {
        fs.close();
      }
    }
  }

  /**
   * 获得转换后的带单位的文件大小
   *
   * @param fileLen
   * @return
   */
  public static String getFileLength(long fileLen) {
    double kbLen = fileLen / 1024.0;
    if (kbLen > 512) {
      return ((int) ((kbLen / 1024) * 100)) / 100.0 + "MB";
    } else {
      return ((int) (kbLen * 100)) / 100.0 + "KB";
    }
  }

  public static double getPercent(double writedLen, long srcLen) {
    double per = (writedLen / srcLen);
    per = ((int) (per * 10000)) / 100;
    return per <= 100.0 ? per : 100.0;
  }

  public static String deleteSpecialCode(String myStr) {
    if (myStr == null || "".equals(myStr)) {
      return "";
    }
    myStr = myStr.replaceAll("\r", "");
    myStr = myStr.replaceAll("\n", "");
    return myStr;
  }

  /**
   * 检查目录是否存在，如果不存在则自动创建
   *
   * @param tarPath
   */
  public static void checkAndMakeDirs(String tarPath) {
    int index = tarPath.lastIndexOf("\\");
    if (index == -1) {
      index = tarPath.lastIndexOf("/");
    }
    File checkExist = new File(tarPath.substring(0, index) + File.separator);
    if (!checkExist.exists()) {
      checkExist.mkdirs();
    }
  }

  public static boolean isValidFileName(String filename) {
    int index = filename.indexOf("\\");
    if (index > -1) {
      return false;
    }
    Pattern pattern = Pattern.compile(("[/\\:*?\"<>|]"));
    Matcher matcher = pattern.matcher(filename);
    while (matcher.find()) {
      return false;
    }
    return true;
  }

  /**
   * 写文件或者
   *
   * @param tarFilePath
   * @param content
   * @param isAppend
   * @throws Exception
   */
  public static void doWriteFile(String tarFilePath, String content, boolean isAppend) throws Exception {
    File file = new File(tarFilePath);
    if (!file.exists()) {
      file.getParentFile().mkdirs();
    }
    FileWriter fw = new FileWriter(tarFilePath, isAppend);
    try {
      fw.append(content);
      fw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static byte[] readFile(String filename) throws Exception {
    File file = new File(filename);
    if (filename == null || filename.equals("") || file.isDirectory()) {
      throw new NullPointerException(GV.getSimpleMsg("errFilePath"));
    }
    String ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_READING);
    JobThreads.publishProgressMsg(ps + file.getName());
    long len = file.length();
    long maxLen = 10 * 1024 * 1024;
    if (len > maxLen) {
      throw new Exception("单个大小不能超过10MB！");
    }
    byte[] bytes = new byte[(int) len];
    InputStream inputStream = new FileInputStream(file);
    int r = inputStream.read(bytes);
    if (r != len) {
      throw new IOException(file.getName() + "读取文件错误!");
    }
    inputStream.close();
    ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_READ_OK);
    JobThreads.publishProgressMsg(ps + file.getName());
    return bytes;
  }

  public static void deleteFile(String filePath, boolean relativePath) {
    if (relativePath) {
      filePath = GV.getImportFileDir_FromRoot().append(filePath).toString();
    }
    deleteFile(filePath);
  }

  public static void deleteFile(String filename) {
    File file = new File(filename);
    if (filename == null || filename.equals("")) {
      throw new NullPointerException(GV.getSimpleMsg("errFilePath"));
    }
    deleteFile(file);
  }

  /**
   * 该方法可以支持比较大的文件的MD5码生成
   *
   * @param filename
   * @return
   * @throws IOException
   * @throws NoSuchAlgorithmException
   */
  public static String getFileMD5DigestHexStr(String filename) throws IOException, NoSuchAlgorithmException {
    File file = new File(filename);
    if (filename == null || filename.equals("") || !file.exists()) {
      throw new NullPointerException(GV.getSimpleMsg("errFilePath"));
    }
    if (!file.isFile()) {
      throw new NullPointerException(GV.getSimpleMsg("md5ForFileOnly"));
    }
    return (new MD5Util()).getBigFileMD5(file);
  }

  public static String getMD5DigestHexStr(byte[] content) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(content);
    byte[] digest = md.digest();
    return byteToHexStr(digest);
  }

  public static String byteToHexStr(byte[] byteArray) {
    StringBuffer md5StrBuff = new StringBuffer();
    for (int i = 0; i < byteArray.length; i++) {
      if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
        md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
      } else {
        md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
      }
    }
    return md5StrBuff.toString();
  }

  public static DetailsType getChoosedDetailsType(Vector<DetailsType> dtVector, String value) {
    for (DetailsType dt : dtVector) {
      if (dt.getValue().equals(value)) {
        return dt;
      }
    }
    return null;
  }

  public static void killProcessByName(String pName) throws IOException {
    final String[] Array = { "ntsd.exe", "-c", "q", "-pn", pName };
    int i = 0;
    try {
      Process process = Runtime.getRuntime().exec(Array);
      process.waitFor();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (i != 0) {
      try {
        Process process = Runtime.getRuntime().exec(Array);
        process.waitFor();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static BusinessProject getProjectByCurrentNode(SmartTreeNode currentNode) {
    Object obj = currentNode.getUserObject();
    if (obj instanceof BusinessProject) {
      return (BusinessProject) obj;
    } else {
      if (currentNode.getNodeCode().equals(GV.NODE_TYPE_PROJECT_LIST)) {
        return null;
      }
      obj = getProjectByCurrentNode((SmartTreeNode) currentNode.getParent());
    }
    return (BusinessProject) obj;
  }

  /**
   * 检查文件或者目录是否存在，如果不存在，可以自动创建
   *
   * @param fullPath
   * @param fileOrDir，参数为F则为文件，如果为D则为目录
   * @param isCreateAuto
   */
  public static boolean checkAndCreateDirOrFile(String fullPath, String fileOrDir, boolean isAutoCreate, boolean isForceToCover, Object data) {
    File file = new File(fullPath);
    if (file.exists() && file.length() > 0 && !isForceToCover) {
      return true;
    } else {
      if (!isAutoCreate || data == null) {
        return false;
      }
      String temp = null;
      if ("F".equals(fileOrDir)) {
        temp = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\"));
      } else {
        temp = file.getAbsolutePath();
        file.mkdirs();
      }
      String ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_CONFIG_CREATING);
      JobThreads.publishProgressMsg(ps + file.getName());
      file = new File(temp);
      file.mkdirs();
      XStream xs = new XStream();
      FileOutputStream fos = null;
      OutputStreamWriter writer = null;
      try {
        file.createNewFile();
        fos = new FileOutputStream(fullPath);
        writer = new OutputStreamWriter(fos, Charset.forName(GV.XML_CHAR_CODE));
        writer.write("<?xml version=\"1.0\" encoding=\"" + GV.XML_CHAR_CODE + "\" ?>\n");
        xs.toXML(data, writer);
        writer.close();
      } catch (Exception e) {
        return false;
      } finally {
        if (fos != null) {
          try {
            fos.close();
          } catch (Exception e) {
          }
        }
        if (writer != null) {
          try {
            writer.close();
          } catch (Exception e) {
          }
        }
      }
    }
    return true;
  }

  //当前招标方式
  private static String currBidType;

  //当前模板应用类型
  private static String currAppType;

  private static String currAppTypeDir;

  private static String currBidTypeDir;

  private static Templates templateList = null;

  /**
   * 根据目录结构构建模板配置文件
   *
   * @param tplDir
   * @return
   */
  public static Templates buildTemplateListByDirectoryStruct(String tplDir) {
    templateList = null;
    templateList = new Templates();
    templateList.setTemplateList(new ArrayList<Object>());
    File topDir = new File(tplDir);
    File[] fileList = topDir.listFiles();
    String temp = null;
    File tempFile = null;
    if (topDir.exists() && fileList != null) {
      for (int i = 0; i < fileList.length; i++) {
        tempFile = fileList[i];
        temp = tempFile.getName();
        if (temp.toUpperCase().endsWith("_TPL")) {
          currBidType = temp.substring(0, temp.indexOf("_"));
          currBidTypeDir = topDir.getAbsolutePath();
          File[] fileListTplNo = tempFile.listFiles();
          for (int j = 0; j < fileListTplNo.length; j++) {
            temp = fileListTplNo[j].getName();
            if (temp.toUpperCase().endsWith("_APT")) {
              currAppType = temp.substring(0, temp.indexOf("_"));
              readAllDirectory(fileListTplNo[j], 3);
            }
          }
        }
      }
    }
    return templateList;
  }

  private static void readAllDirectory(File file, int depth) {
    if (depth == 3) {
      File files[] = file.listFiles();
      if (files != null) {
        File templateFile = null;
        for (int i = 0; i < files.length; i++) {
          ZcZBFileTemplate tpl = new ZcZBFileTemplate();
          tpl.setTplBelongBidWay(currBidType);
          tpl.setTplAppType(currAppType);
          templateFile = files[i];
          currAppTypeDir = templateFile.getAbsolutePath().substring(currBidTypeDir.length());
          tpl.setTplFullPath(currAppTypeDir);
          tpl.setLatestUsed(new Date());
          tpl.setDescription("本套模板主要适用于招标方式为" + currBidType + "的" + currAppType + ".");
          //"模板_" + (templateList.getTemplateList().size() + 1)
          tpl.setTplNo(templateFile.getName());
          templateList.getTemplateList().add(tpl);
        }
      }
    }
  }

  /**
   * 根据标准的模板配置加载既定的模板文件
   *
   * @param nodeType
   * @param nodeCode
   * @return
   */
  public static SmartTreeNode makeNodesReferenceTemplate(String nodeType, String nodeCode) {
    SmartTreeNode treeNode = new SmartTreeNode();
    treeNode.setNodeCode(GV.getCodeFromMap(nodeType) + SmartTreeNode.SMART_TREE_NODE_COUNT);
    treeNode.setNodeName(GV.getNameFromMap(nodeType));
    treeNode.setNodeType(nodeType);
    treeNode.setNodeDirPath(treeNode.getNodeName());
    treeNode.setNodeDisplayName(GV.getNameFromMap(nodeType));
    treeNode.setNodeSource(GV.NODE_SOURCE_FROM_MOLD);
    treeNode.setAllowsChildren(true);
    return treeNode;
  }

  /**
   * 从模板拷贝对应的文件到节点下
   *
   * @param dirType
   * @param tplID
   * @param node
   */
  public static void copyTemplateFileToNode(String dirType, String tplID, final SmartTreeNode node) {
    StringBuffer sbuff = GV.getImportFileDir_FromRoot();
    sbuff.append(dirType);
    sbuff.append(File.separator);
    sbuff.append(node.getNodeName());
    sbuff.append(node.getFileExtension());
    StringBuffer tbuff = GV.getImportFileDir_FromRoot();
    tbuff.append(node.getNodesFullPath());
    tbuff.append(node.getFileExtension());
    try {
      copyFile(sbuff.toString(), tbuff.toString());
      if (GV.SUFFIX_TABLE.equalsIgnoreCase(node.getFileExtension())) {
        copyFile(sbuff.toString().replaceAll(GV.SUFFIX_TABLE, GV.SUFFIX_TABLE_SETTING), tbuff.toString());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 从投标文件中找开标一览表
   *
   * @param packNode
   * @return
   */
  public static String getTBYLBFilePath(SmartTreeNode packNode) {
    for (int i = 0; i < packNode.getChildCount(); i++) {
      SmartTreeNode currNode = (SmartTreeNode) packNode.getChildAt(i);
      if (GV.NODE_TYPE_TB.equals(currNode.getNodeType())) {
        SmartTreeNode node = PubFunction.getNeedingNodeInChildren(currNode, GV.NODE_TYPE_TBYLB);
        if (node != null) {
          return node.getNodesFullPath() + node.getFileExtension();
        } else {
          return "";
        }
      }
    }
    return "";
  }

  /**
   * 从指定节点往子节点遍历查找指定节点类型的节点
   *
   * @param currNode
   * @return
   */
  public static SmartTreeNode getNeedingNodeInChildren(SmartTreeNode currNode, String needingNodeType) {
    if (currNode == null || needingNodeType == null || "".equals(needingNodeType)) {
      return null;
    }
    if (needingNodeType.equals(currNode.getNodeType())) {
      return currNode;
    }
    SmartTreeNode reNode = null;
    for (int i = 0; i < currNode.getChildCount(); i++) {
      SmartTreeNode temp = (SmartTreeNode) currNode.getChildAt(i);
      if (needingNodeType.equals(temp.getNodeType())) {
        return temp;
      } else {
        if (temp.getChildCount() > 0) {
          reNode = getNeedingNodeInChildren(temp, needingNodeType);
          if (reNode != null) {
            return reNode;
          }
        } else {
          continue;
        }
      }
    }
    return null;
  }

  /**
   * 从指定节点往子节点遍历查找指定节点类型的、指定节点编号的节点
   *
   * @param currNode
   * @return
   */
  public static SmartTreeNode getNeedingNodeWithNodeCodeInChildren(SmartTreeNode currNode, String needingNodeType, String nodeCode) {
    if (currNode == null || needingNodeType == null || "".equals(needingNodeType) || nodeCode == null || "".equals(nodeCode)) {
      return null;
    }
    if (needingNodeType.equals(currNode.getNodeType()) && nodeCode.equals(currNode.getNodeCode())) {
      return currNode;
    }
    SmartTreeNode reNode = null;
    for (int i = 0; i < currNode.getChildCount(); i++) {
      SmartTreeNode temp = (SmartTreeNode) currNode.getChildAt(i);
      if (needingNodeType.equals(temp.getNodeType()) && nodeCode.equals(temp.getNodeCode())) {
        return temp;
      } else {
        if (temp.getChildCount() > 0) {
          reNode = getNeedingNodeWithNodeCodeInChildren(temp, needingNodeType, nodeCode);
          if (reNode != null) {
            return reNode;
          }
        } else {
          continue;
        }
      }
    }
    return null;
  }

  /**
   * 从指定节点往子节点遍历查找指定节点类型的、指定节点名称的节点
   *
   * @param currNode
   * @return
   */
  public static SmartTreeNode getNeedingNodeWithNodeNameInChildren(SmartTreeNode currNode, String needingNodeType, String nodeName) {
    if (currNode == null || needingNodeType == null || "".equals(needingNodeType) || nodeName == null || "".equals(nodeName)) {
      return null;
    }
    if (needingNodeType.equals(currNode.getNodeType()) && currNode.getNodeName() != null && currNode.getNodeName().indexOf(nodeName) != -1) {
      return currNode;
    }
    SmartTreeNode reNode = null;
    for (int i = 0; i < currNode.getChildCount(); i++) {
      SmartTreeNode temp = (SmartTreeNode) currNode.getChildAt(i);
      if (needingNodeType.equals(temp.getNodeType()) && temp.getNodeName() != null && temp.getNodeName().indexOf(nodeName) != -1) {
        return temp;
      } else {
        if (temp.getChildCount() > 0) {
          reNode = getNeedingNodeWithNodeNameInChildren(temp, needingNodeType, nodeName);
          if (reNode != null) {
            return reNode;
          }
        } else {
          continue;
        }
      }
    }
    return null;
  }

  /**
   * 从指定节点往子节点遍历查找指定节点类型的、指定节点名称的节点
   *
   * @param currNode
   * @return
   */
  public static SmartTreeNode getNeedingNodeInNodeNameInChildren(SmartTreeNode currNode, String needingNodeType, String nodeName) {
    if (currNode == null || needingNodeType == null || "".equals(needingNodeType) || nodeName == null || "".equals(nodeName)) {
      return null;
    }
    if (needingNodeType.equals(currNode.getNodeType()) && nodeName.indexOf(currNode.getNodeName()) != -1) {
      return currNode;
    }
    SmartTreeNode reNode = null;
    for (int i = 0; i < currNode.getChildCount(); i++) {
      SmartTreeNode temp = (SmartTreeNode) currNode.getChildAt(i);
      if (needingNodeType.equals(temp.getNodeType()) && nodeName.indexOf(temp.getNodeName()) != -1) {
        return temp;
      } else {
        if (temp.getChildCount() > 0) {
          reNode = getNeedingNodeInNodeNameInChildren(temp, needingNodeType, nodeName);
          if (reNode != null) {
            return reNode;
          }
        } else {
          continue;
        }
      }
    }
    return null;
  }

  /**
   * 从当前节点开始往下搜索所有指定名称或者类型的节点
   *
   * @param currNode
   * @param filterProperty
   * @param searchValue
   * @param ifLeafOnly     :如果只搜索叶子节点，则传入true，如果包括非叶子节点，那么则传入false;
   * @param allowDepth     <0时表示不限制深度
   * @param resultNodes
   */
  public static String doSearchAllMatchingNeedingNodeFromCurrNode(SmartTreeNode currNode, String filterType, String searchValue, boolean ifLeafOnly,
    int allowDepth, List<SmartTreeNode> resultNodes) {
    if (currNode == null) {
      return "currNode参数为空！";
    }
    if (filterType == null || "".equals(filterType)) {
      return "filterType参数为空！";
    }
    if (searchValue == null || "".equals(searchValue)) {
      return "searchValue参数为空！";
    }
    if (!"nodeName".equals(filterType) && !"nodeCode".equals(filterType) && !"nodeType".equals(filterType)) {
      return "当前不支持[" + filterType + "]类型的搜索，只支持[nodeName]/[nodeCode]/[nodeType]...";
    }
    depthCount = 0;
    doSearchNeedingNodeFromCurrNode(currNode, filterType, searchValue, ifLeafOnly, allowDepth, resultNodes);
    return "OK";
  }

  static int depthCount = 0;

  /**
   * 从当前节点开始便利从子节点中查找指定的节点
   *
   * @param currNode
   * @param filterType
   * @param searchValue
   * @param ifLeafOnly
   * @param allowDepth
   * @param resultNodes
   */
  private static void doSearchNeedingNodeFromCurrNode(SmartTreeNode currNode, String filterType, String searchValue, boolean ifLeafOnly,
    int allowDepth, List<SmartTreeNode> resultNodes) {
    if (allowDepth > 0 && depthCount > allowDepth) {
      return;
    }
    depthCount++;
    if (currNode.getAllowsChildren()) {
      for (int i = 0; i < currNode.getChildCount(); i++) {
        SmartTreeNode curr = (SmartTreeNode) currNode.getChildAt(i);
        boolean isHaveChild = curr.getAllowsChildren();
        if ("nodeCode".equals(filterType) && curr.getNodeType() != null && curr.getNodeType().indexOf(searchValue) != -1) {
          if (!isHaveChild) {
            resultNodes.add(curr);
          } else {
            //如果有叶子节点，而且非叶子节点也要，那么收录该节点
            if (!ifLeafOnly) {
              resultNodes.add(curr);
            }
            doSearchNeedingNodeFromCurrNode(curr, filterType, searchValue, ifLeafOnly, allowDepth, resultNodes);
          }
        } else if ("nodeName".equals(filterType) && curr.getNodeName() != null && curr.getNodeName().indexOf(searchValue) != -1) {
          if (!isHaveChild) {
            resultNodes.add(curr);
          } else {
            if (!ifLeafOnly) {
              resultNodes.add(curr);
            }
            doSearchNeedingNodeFromCurrNode(curr, filterType, searchValue, ifLeafOnly, allowDepth, resultNodes);
          }
        } else if ("nodeType".equals(filterType) && curr.getNodeType() != null && curr.getNodeType().indexOf(searchValue) != -1) {
          if (!isHaveChild) {
            resultNodes.add(curr);
          } else {
            if (!ifLeafOnly) {
              resultNodes.add(curr);
            }
            doSearchNeedingNodeFromCurrNode(curr, filterType, searchValue, ifLeafOnly, allowDepth, resultNodes);
          }
        } else {
          doSearchNeedingNodeFromCurrNode(curr, filterType, searchValue, ifLeafOnly, allowDepth, resultNodes);
        }
      }
    } else {
      if ("nodeCode".equals(filterType) && currNode.getNodeType() != null && currNode.getNodeType().indexOf(searchValue) != -1) {
        resultNodes.add(currNode);
      } else if ("nodeName".equals(filterType) && currNode.getNodeName() != null && currNode.getNodeName().indexOf(searchValue) != -1) {
        resultNodes.add(currNode);
      } else if ("nodeType".equals(filterType) && currNode.getNodeType() != null && currNode.getNodeType().indexOf(searchValue) != -1) {
        resultNodes.add(currNode);
      }
    }
  }

  /**
   * 获取主副key公钥信息
   */
  public static void doFetchMainAndMinorPubKey() {
    if (!DataChecker.isNeedToCheckPubKeyInfo()) {
      JOptionPane.showMessageDialog(null, GV.getSimpleMsg("notNeedMasterMinorPubKey"));
      return;
    }
    if (DataChecker.toCheckIsHavingPubKeyInfo()) {
      String info = GV.getSimpleMsg("pubKeyExistIsNeedUpdate");
      int sel = JOptionPane.showConfirmDialog(null, info, GV.getSimpleMsg("commonMessage"), 2);
      if (sel == JOptionPane.OK_OPTION) {
        ProjectInfoPanel.clearCA_PUB_KEY(ProjectInfoPanel.MAIN_PUB_KEY);
        ProjectInfoPanel.clearCA_PUB_KEY(ProjectInfoPanel.MINOR_PUB_KEY);
      } else {
        return;
      }
    }
    String mainPubKey = ProjectInfoPanel.getCA_PUB_KEY(ProjectInfoPanel.MAIN_PUB_KEY);
    JOptionPane.showMessageDialog(null, GV.getSimpleMsg("firstCAOKAndAskForSecondCA"));
    System.out.println(mainPubKey);
    String minorPubKey = ProjectInfoPanel.getCA_PUB_KEY(ProjectInfoPanel.MINOR_PUB_KEY);
    System.out.println(minorPubKey);
    if (mainPubKey.equals(minorPubKey)) {
      JOptionPane.showMessageDialog(null, GV.getSimpleMsg("askForDiffCert"));
      ProjectInfoPanel.clearCA_PUB_KEY(ProjectInfoPanel.MINOR_PUB_KEY);
      minorPubKey = ProjectInfoPanel.getCA_PUB_KEY(ProjectInfoPanel.MINOR_PUB_KEY);
      System.out.println(minorPubKey);
      //如果还是相同，那么继续取值
      if (mainPubKey.equals(minorPubKey)) {
        doFetchMainAndMinorPubKey();
      }
    } else {
      JOptionPane.showMessageDialog(null, GV.getSimpleMsg("bothKeyImportOK"));
    }
  }

  public static String doFetchPubKey() {
    String pubKey = null;
    try {
      pubKey = FinanceBiddingCall.getHostPublicKey();
      pubKey = pubKey.replaceAll("\r\n", "").replaceAll("\n", "");
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, GV.getSimpleMsg("pleaseInsertUkey"));
      e.printStackTrace();
    }
    if (pubKey == null || "".equals(pubKey)) {
      pubKey = doFetchPubKey();
    }
    return pubKey;
  }

  public static String doZipFiles(Map<String, String> priceTablePaths, String nodeCode) {
    String destination = GV.getUpload_tmpPath() + nodeCode + "_pts";
    File file = new File(destination);
    if (file.exists()) {
      PubFunction.deleteFile(destination);
    }
    try {
      file.mkdir();
    } catch (Exception e) {
      e.printStackTrace();
    }
    Iterator<String> it = priceTablePaths.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      String value = priceTablePaths.get(key);
      try {
        PubFunction.copyFile(value, destination + File.separator + new File(value).getName());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    try {
      ZipDirFile.zip(destination, destination + ".zip", nodeCode + "_pts");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return destination + ".zip";
  }

  public static String getDefaultSystemInfo() {
    List<String> keyList = new ArrayList<String>();
    keyList.add("sun.os.patch.level");
    keyList.add("os.arch");
    keyList.add("os.name");
    keyList.add("os.version");
    keyList.add("user.name");
    keyList.add("user.dir");
    return getSomeImportantSystemInfo(keyList);
  }

  public static String getSomeImportantSystemInfo(List<String> needKeys) {
    if (needKeys == null) {
      return "";
    }
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < needKeys.size(); i++) {
      String key = needKeys.get(i);
      String value = System.getProperty(key);
      buff.append(key);
      buff.append("=");
      buff.append(value);
      buff.append("@#@");
    }
    return buff.toString();
  }

  public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
  }

  /**
   * 从叶节点往父节点遍历查找该叶节点所属的标段节点
   *
   * @param currNode
   * @return
   */
  public static SmartTreeNode getPackNode(SmartTreeNode currNode) {
    if (currNode == null) {
      return null;
    }
    if (GV.NODE_TYPE_ROOT.equals(currNode.getNodeType()) || GV.NODE_TYPE_PROJECT.equals(currNode.getNodeType())) {
      return null;
    } else if (GV.NODE_TYPE_PACK.equals(currNode.getNodeType())) {
      return currNode;
    } else {
      return getPackNode((SmartTreeNode) currNode.getParent());
    }
  }

  /**
   * 从当前节点往父节点遍历查找指定的节点
   *
   * @param currNode
   * @param nodeType
   * @return
   */
  public static SmartTreeNode getNeedingNodeInParent(SmartTreeNode currNode, String nodeType) {
    if (currNode == null || null == nodeType || "".equals(nodeType)) {
      return null;
    }
    if (nodeType.equals(currNode.getNodeType())) {
      return currNode;
    }
    SmartTreeNode pNode = (SmartTreeNode) currNode.getParent();
    if (pNode == null) {
      return null;
    } else if (nodeType.equals(pNode.getNodeType())) {
      return pNode;
    } else {
      return getNeedingNodeInParent(pNode, nodeType);
    }
  }

  public static Point getPosition(int barWidth) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) screenSize.getWidth();
    int height = (int) screenSize.getHeight();
    int x = (width - barWidth) / 2;
    int y = (height - 120);
    return new Point(x, y);
  }

  /**
   * 获得窗口位置
   *
   * @param size 为窗口的长和宽
   * @param hOffset2Center为垂直方向偏移量，正数为往屏幕上方位置偏移，负数为往屏幕下方位置偏移；
   *
   * @param wOffset2Center为水平方向偏移量，正数为往屏幕左方位置偏移，负数为往屏幕右方位置偏移；
   *
   * @return
   */
  public static Point getBetterPosition(Dimension size, int wOffset2Center, int hOffset2Center) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) screenSize.getWidth();
    int height = (int) screenSize.getHeight();
    int x = (width - (int) size.getWidth()) / 2 - wOffset2Center;
    int y = (height - (int) size.getHeight()) / 2 - hOffset2Center;
    return new Point(x, y);
  }
}
