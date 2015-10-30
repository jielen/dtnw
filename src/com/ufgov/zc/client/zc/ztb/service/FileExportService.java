package com.ufgov.zc.client.zc.ztb.service;

import com.ufgov.zc.client.zc.ztb.JobThreads;
import com.ufgov.zc.client.zc.ztb.dao.LocalProjectDAO;
import com.ufgov.zc.client.zc.ztb.dao.ZTbFileDao;
import com.ufgov.zc.client.zc.ztb.model.LevelOneItem;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.client.zc.ztb.util.PubFunction;
import com.ufgov.zc.common.zc.model.ZcZBFileTemplate;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class FileExportService extends ZTbFileDao {
  private LocalProjectDAO projectDAO = LocalProjectDAO.getInstance();

  /**
   * 导出项目为ztb文件
   *
   * @param projNode
   * @param destination：路径为用户指定的存放路径，为绝对路径.
   *
   * @return
   * @throws Exception
   */
  public String exportProject(SmartTreeNode projNode, String sourceDir, String destination, boolean delTempFile) throws Exception {
    String projCode = projNode.getNodeCode();
    String projName = projNode.getNodeDisplayName();
    String ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_EXP_PROJ_READY);
    JobThreads.publishProgressMsg(ps);
    List<String> filePathsList = new ArrayList<String>();
    getFilePathList(projNode, filePathsList, "");
    if (!toCheckFileExist(filePathsList)) {
      throw new Exception(GV.getSimpleMsg("lackingFiles"));
    }
    ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_COPY_READY);
    JobThreads.publishProgressMsg(ps);
    PubFunction.copyFiles(sourceDir, destination + projCode);
    copyProjectInfoFile(destination, projCode, null);
    ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_PROJ_CONFIG_CREATING);
    JobThreads.publishProgressMsg(ps);
    createProjConfigStr(destination, projNode);
    ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_ZIP_READY);
    JobThreads.publishProgressMsg(ps);
    ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(destination + projCode + ".ztb"));
    stream.setEncoding(GV.FILE_CHAR_CODE);
    File tarFile = new File(destination + projCode);
    exportDirectoryFiles(tarFile, stream, "");
    stream.flush();
    stream.close();
    if (delTempFile) {
      ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_DELETE_READY);
      JobThreads.publishProgressMsg(ps);
      PubFunction.deleteFile(tarFile);
    }
    String expFilePath = destination + projCode + ".ztb";
    File tmpFile = new File(expFilePath);
    String renamedFilePath = destination + projName + ".ztb";
    File renamedFile = new File(renamedFilePath);
    if (renamedFile.exists()) {
      renamedFile.delete();
    }
    if (tmpFile.renameTo(renamedFile)) {
      return renamedFile.getAbsolutePath();
    }
    return tmpFile.getAbsolutePath();
  }

  /**
   * 检查节点对应的文件是否都存在
   *
   * @param filePathList
   * @return
   */
  private boolean toCheckFileExist(List<String> filePathList) {
    String srcBasePath = GV.getImportFileDir_FromRoot().toString();
    for (int i = 0; i < filePathList.size(); i++) {
      File tmp = new File(srcBasePath + File.separator + filePathList.get(i));
      if (!tmp.exists()) {
        JOptionPane.showMessageDialog(null, tmp.getAbsolutePath() + GV.getSimpleMsg("fileMissingNeedCheck"));
        return false;
      }
    }
    return true;
  }

  /**
   * 拷贝项目概要信息xml文件
   *
   * @param destination
   * @param nodeCode
   * @throws Exception
   */
  private void copyProjectInfoFile(String destination, String projCode, String packName) throws Exception {
    StringBuffer fullPath = GV.getImportFileDir_FromRoot();
    fullPath.append(projCode);
    fullPath.append(File.separator);
    fullPath.append(projCode);
    fullPath.append(GV.PROJECT_INFO_XML_SUFFIX);
    StringBuffer destPath = new StringBuffer(destination);
    destPath.append(projCode);
    if (packName != null && !"".equals(packName)) {
      destPath.append(packName);
    }
    destPath.append(File.separator);
    destPath.append(projCode);
    destPath.append(GV.PROJECT_INFO_XML_SUFFIX);
    PubFunction.copyFile(fullPath.toString(), destPath.toString());
  }

  private void exportDirectoryFiles(File source, ZipOutputStream destination, String baseDir) throws IOException {
    if (source.isDirectory()) {
      File[] children = source.listFiles();
      baseDir = PubFunction.trim(baseDir).equals("") ? "" : baseDir + File.separator;
      for (int i = 0; i < children.length; i++) {
        exportDirectoryFiles(children[i], destination, baseDir + children[i].getName());
      }
    } else {
      String ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_ZIPING);
      String fileName = source.getName();
      String fileLen = PubFunction.getFileLength(source.length());
      JobThreads.publishProgressMsg(ps + fileName + "--" + fileLen);
      double writedLen = 0;
      destination.putNextEntry(new ZipEntry(baseDir));
      FileInputStream in = new FileInputStream(source);
      int b = -1;
      byte[] buffer = new byte[GV.BUFFER_SIZE];
      String tmpName = fileName.substring(0, fileName.indexOf("."));
      while ((b = in.read(buffer)) != -1) {
        destination.write(buffer, 0, b);
        writedLen += b;
        double percent = PubFunction.getPercent(writedLen, source.length());
        JobThreads.publishProgressMsg(ps + tmpName + "--共【" + fileLen + "】已完成【" + percent + "%】");
      }
      in.close();
    }
  }

  public String exportPack(SmartTreeNode packNode, String destination, boolean delTempFile, String uploadMold) throws Exception {
    destination = destination + File.separator;
    SmartTreeNode projNode = (SmartTreeNode) packNode.getParent();
    String packCode = packNode.getNodeCode();
    String packName = packNode.getNodeDisplayName();
    String projCode = projNode.getNodeCode();
    SmartTreeNode newNode = makeNewProjNodeForExport(projNode, packCode);
    StringBuffer buff = new StringBuffer();
    buff.append(projCode);
    buff.append("[");
    buff.append(packName);
    buff.append("].ztb");
    String ztbName = buff.toString();
    String ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_COPY_READY);
    JobThreads.publishProgressMsg(ps);
    List<String> filePathsList = new ArrayList<String>();
    getFilePathList(newNode, filePathsList, "");
    StringBuffer tb = new StringBuffer();
    filePathsList.add(tb.append(projCode).append(File.separator).append(packCode).append(File.separator).append(GV.NODE_NAME_RP_TREE)
      .append(GV.SUFFIX_XML).toString());
    String dest = destination + ztbName.replace(".ztb", "") + File.separator + packCode + File.separator;
    doCopyFiles(filePathsList, dest, projCode, packCode);
    copyProjectInfoFile(destination, projCode, "[" + packName + "]");
    ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_PROJ_CONFIG_CREATE_READY);
    JobThreads.publishProgressMsg(ps);
    createProjPackConfigStr(destination + projCode + "[" + packName + "]", newNode);
    buff.setLength(0);
    buff.append(destination);
    buff.append(ztbName);
    String ztbPath = buff.toString();
    ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(ztbPath));
    zipOutputStream.setEncoding("GBK");
    //0~9,0为无压缩，9为最强压缩
    if (LevelOneItem.UPLOAD_MOLD_HIGHT.equals(uploadMold)) {
      zipOutputStream.setLevel(0);
    } else {
      zipOutputStream.setLevel(9);
    }
    buff.setLength(0);
    buff.append(destination);
    buff.append(File.separator);
    buff.append(projCode);
    buff.append("[").append(packName).append("]");
    File tarFile = new File(buff.toString());
    ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_ZIP_READY);
    JobThreads.publishProgressMsg(ps);
    exportDirectoryFiles(tarFile, zipOutputStream, "");
    zipOutputStream.flush();
    zipOutputStream.close();
    ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_ZIP_OK);
    JobThreads.publishProgressMsg(ps);
    if (delTempFile) {
      ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_DELETE_READY);
      JobThreads.publishProgressMsg(ps);
      PubFunction.deleteFile(tarFile);
      ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_DELETE_OK);
      JobThreads.publishProgressMsg(ps);
    }
    JobThreads.publishProgressMsg(GV.getSimpleMsg("beginToCreateConfigFile"));
    buildLevelOneAndTwo(packNode, ztbPath, uploadMold);
    JobThreads.publishProgressMsg(GV.getSimpleMsg("configFileCreateFinish"));
    return ztbPath.replaceAll("/", Matcher.quoteReplacement(File.separator));
  }

  /**
   * 创建一级和二级xml文件
   * 1、创建一级xml文件时，需要先把历史的投标记录读取出来，然后往前面插入最近的投标记录；
   *
   * @param packNode
   * @param destination
   * @param uploadMold
   * @throws Exception
   */
  private void buildLevelOneAndTwo(SmartTreeNode packNode, String destination, String uploadMold) throws Exception {
    LevelOneTwoBuilder builder = new LevelOneTwoBuilder();
    String level2XmlPath = builder.levelTwoBuilder(packNode, destination, uploadMold);
    builder.levelOneBuilder(packNode, level2XmlPath, uploadMold);
  }

  private void doCopyFiles(List<String> fileList, String dest, String projCode, String packCode) throws Exception {
    try {
      String srcBasePath = GV.getImportFileDir_FromRoot().toString();
      for (int i = 0; i < fileList.size(); i++) {
        String tmp = fileList.get(i);
        int index = (File.separator + projCode + File.separator + packCode + File.separator).length();
        PubFunction.copyFile(srcBasePath + tmp, dest + tmp.substring(index - 1));
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(GV.getSimpleMsg("fileCopyErr") + e.getMessage());
    }
  }

  /**
   * 递归遍历整棵树，获得所有的叶子文件全路径；
   *
   * @param packNode
   * @return
   */
  public static void getFilePathList(SmartTreeNode currNode, List<String> list, String basePath) {
    int count = currNode.getChildCount();
    if (count == 0) {
      list.add(basePath + currNode.getNodesFullPath() + currNode.getFileExtension());
      return;
    }
    for (int i = 0; i < count; i++) {
      SmartTreeNode tmpNode = (SmartTreeNode) currNode.getChildAt(i);
      if (tmpNode.getChildCount() == 0) {
        String path = basePath + tmpNode.getNodesFullPath() + tmpNode.getFileExtension();
        list.add(path);
      } else {
        getFilePathList(tmpNode, list, basePath);
      }
    }
  }

  public String createProjPackConfigStr(String destination, SmartTreeNode treeNode) throws Exception {
    destination = destination + File.separator;
    return projectDAO.createXMLFile(destination, treeNode);
  }

  private SmartTreeNode makeNewProjNodeForExport(SmartTreeNode projNode, String packCode) {
    SmartTreeNode newProjNode = new SmartTreeNode();
    try {
      newProjNode = (SmartTreeNode) projNode.deepClone(projNode);
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
      return projNode;
    }
    SmartTreeNode temp = null;
    for (int i = 0; i < newProjNode.getChildCount(); i++) {
      temp = (SmartTreeNode) newProjNode.getChildAt(i);
      if (!packCode.equals(temp.getNodeCode())) {
        newProjNode.remove(temp);
        i--;
      }
    }
    if (newProjNode.getChildCount() != 1) {
      return null;
    }
    SmartTreeNode packNode = (SmartTreeNode) newProjNode.getChildAt(0);
    for (int j = 0; j < packNode.getChildCount(); j++) {
      SmartTreeNode ps = (SmartTreeNode) packNode.getChildAt(j);
      if (!GV.NODE_TYPE_TB.equals(ps.getNodeType())) {
        packNode.remove(ps);
      }
    }
    return newProjNode;
  }

  public String createProjConfigStr(String destination, SmartTreeNode treeNode) throws Exception {
    destination = destination + treeNode.getNodeDirPath() + File.separator;
    return projectDAO.createXMLFile(destination, treeNode);
  }

  /**
   * 导出项目为ztb文件
   *
   * @param treeNode
   * @param destination：路径为用户指定的存放路径，为绝对路径.
   *
   * @return
   * @throws Exception
   */
  public String exportTemplate(SmartTreeNode packNode, String destination, boolean delTempFile, ZcZBFileTemplate template) throws Exception {
    String nodeCode = packNode.getNodeCode();
    SmartTreeNode projNode = (SmartTreeNode) packNode.getParent();
    String sourceDir = GV.getImportFileDir_FromRoot().append(projNode.getNodeCode()).append(File.separator).append(nodeCode).toString();
    String ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_EXP_TPL_READY);
    JobThreads.publishProgressMsg(ps);
    this.createXMLFile(sourceDir, template);
    ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_COPY_READY);
    JobThreads.publishProgressMsg(ps);
    PubFunction.copyFiles(sourceDir, destination + nodeCode);
    //检查一下标段下面是否有招标文件节点，如果没有，则需要到项目下去拷贝招标文件部分；
    if (PubFunction.getNeedingNodeInChildren(packNode, GV.NODE_TYPE_ZB) == null) {
      List<SmartTreeNode> resultNodes = new ArrayList<SmartTreeNode>();
      PubFunction.doSearchAllMatchingNeedingNodeFromCurrNode(projNode, "nodeType", GV.NODE_TYPE_ZB, false, -1, resultNodes);
      if (resultNodes.size() < 1) {
        throw new Exception(GV.getOperateMsg("lackNeedingFile", GV.NODE_NAME_ZB));
      }
      SmartTreeNode zbNode = resultNodes.get(0);
      String tmpPath = GV.getImportFileDir_FromRoot().append(projNode.getNodeCode()).append(File.separator).append(zbNode.getNodeDirPath())
        .toString();
      PubFunction.copyFiles(tmpPath, destination + nodeCode + File.separator + zbNode.getNodeDirPath());
    }
    ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_ZIP_READY);
    JobThreads.publishProgressMsg(ps);
    ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destination + nodeCode + "." + GV.TEMPLATE_ZIP));
    zos.setEncoding("GBK");
    File tarFile = new File(destination + nodeCode);
    exportDirectoryFiles(tarFile, zos, "");
    zos.flush();
    zos.close();
    //删除临时文件
    String templatePath = sourceDir + File.separator + GV.TEMPLATE_CONFIG_XML;
    PubFunction.deleteFile(new File(templatePath));
    if (delTempFile) {
      ps = GV.getFileOperatorCN(GV.PROGRESS_STATUS_DELETE_READY);
      JobThreads.publishProgressMsg(ps);
      PubFunction.deleteFile(tarFile);
    }
    String expFilePath = destination + nodeCode + "." + GV.TEMPLATE_ZIP;
    return expFilePath.replaceAll("/", Matcher.quoteReplacement(File.separator));
  }

  /**
   * 创建xml文件TEMPLATE_CONFIG_XML
   *
   * @param destination
   * @param template
   * @return
   * @throws Exception
   */
  public String createXMLFile(String destination, ZcZBFileTemplate template) throws Exception {
    String filePath = destination + File.separator + GV.TEMPLATE_CONFIG_XML;
    String xmls = xstreamR.toXML(template);
    createFile(filePath, "<?xml version=\"1.0\" encoding=\"" + GV.XML_CHAR_CODE + "\"?>\n" + xmls, GV.XML_CHAR_CODE);
    return filePath;
  }

  /**
   * 创建xml文件
   */
  public void createFile(String fileName, String xmlContent, String encodType) throws Exception {
    String dir = fileName.substring(0, fileName.lastIndexOf(File.separator));
    File file = new File(dir);
    if (!file.exists()) {
      boolean flag = file.mkdirs();
      if (!flag) {
        throw new Exception(GV.getSimpleMsg("directoryCreateErr"));
      }
    }
    FileOutputStream writerStream = new FileOutputStream(fileName);
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, encodType));
    writer.write(xmlContent);
    writer.close();
  }
}
