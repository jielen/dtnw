package com.ufgov.zc.client.zc.project.reqfile.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.TreePath;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.zc.project.reqfile.ReqFileConstants;
import com.ufgov.zc.client.zc.project.reqfile.component.reqFileLeftTree.ReqFileLeftTree;
import com.ufgov.zc.client.zc.project.reqfile.component.reqFileLeftTree.ReqTreeNode;
import com.ufgov.zc.client.zc.ztb.ZipDirFile;
import com.ufgov.zc.client.zc.ztb.model.InvisibleTreeModel;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.client.zc.ztb.util.ZipFilePubFunc;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.exception.BusinessException;
import com.ufgov.zc.common.system.model.AsFile;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbReqFile;
import com.ufgov.zc.common.zc.model.ZcEbRequirement;

/**
 * 
* @ClassName: ReqFileService
* @Description: TODO(需求确认模块文件的一些需求)
* @date: Oct 17, 2012 12:58:42 AM
* @version: V1.0 
* @since: 1.0
* @author: Administrator
* @modify:
 */
public class ReqFileService {

  private ZcEbRequirement zcEbRequirement;

  private final IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,

  "baseDataServiceDelegate");

  private final RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String downLoadPath = "";

  private final Map<String, AsFile> templateMap = new HashMap<String, AsFile>();

  private ReqFileLeftTree regFileLeftTree;

  public ReqFileService(ZcEbRequirement zcEbRequirement) {
    this.zcEbRequirement = zcEbRequirement;
    downLoadPath = ReqFileConstants.REQ_FILE_PATH + zcEbRequirement.getZcEbEntrust().getZcMakeName() + "("
      + zcEbRequirement.getZcEbEntrust().getSnCode() + ")" + File.separator;
    /**
     * 清楚临时文件
     */
    deleteFile(new File(ReqFileConstants.REQ_FILE_PATH));

  }

  public ReqFileLeftTree createFilesTreePanel() {
    regFileLeftTree = new ReqFileLeftTree(getRootNode());
    return regFileLeftTree;

  }

  public ReqTreeNode getRootNode() {
    ReqTreeNode rootNode = null;

    if (zcEbRequirement.getZcEbReqFile().getFileContent() != null) {
      try {
        downloadReqFile(zcEbRequirement);
        rootNode = readTreeNodes();

      } catch (Exception e) {
        // TCJLODO Auto-generated catch block
        e.printStackTrace();
      }
    } else {
      rootNode = new ReqTreeNode();
      rootNode.setNodeName(zcEbRequirement.getZcEbEntrust().getZcMakeName() + "(" + zcEbRequirement.getZcEbEntrust().getSnCode() + ")");
      rootNode.setNodeDirPath(zcEbRequirement.getZcEbEntrust().getZcMakeName() + "(" + zcEbRequirement.getZcEbEntrust().getSnCode() + ")");
      rootNode.setNodeType(ReqFileConstants.NODE_TYPE_ROOT);
      List<ZcEbPack> packList = zcEbRequirement.getPackList();

      if (packList != null) {
        for (ZcEbPack pack : packList) {
          generNodeFile(createPackNode(pack, rootNode));

        }
      }
    }

    return rootNode;
  }

  public ReqTreeNode createPackNode(ZcEbPack zcEbPack, ReqTreeNode rootNode) {
    //分包的节点
    ReqTreeNode packNode = new ReqTreeNode();

    packNode.setUserObject(zcEbPack);

    packNode.setNodeName(zcEbPack.getPackName());
    packNode.setNodeCode(zcEbPack.getPackCode());
    packNode.setNodeType(ReqFileConstants.NODE_TYPE_PACK);
    packNode.setNodeDirPath(zcEbPack.getPackName());

    if (ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(zcEbPack.getPurType())) {
      ReqTreeNode node1 = createReqFileLeafNode(ReqFileConstants.GONGYS_DYCG, ReqFileConstants.GONGYS_DYCG_NAME, ReqFileConstants.NODE_TYPE_DOC,
        ReqFileConstants.SUFFIX_DOC);
      packNode.add(node1);
    } else if (ZcSettingConstants.PITEM_OPIWAY_XJ.equals(zcEbPack.getPurType())) {
      ReqTreeNode node1 = createReqFileLeafNode(ReqFileConstants.GONGYS_CAR, ReqFileConstants.GONGYS_CAR_NAME, ReqFileConstants.NODE_TYPE_DOC,
        ReqFileConstants.SUFFIX_DOC);
      packNode.add(node1);
    } else {

      //每个分包需要确认的信息子节点
      ReqTreeNode node1 = createReqFileLeafNode(ReqFileConstants.YEWU_REQ_CODE, ReqFileConstants.YEWU_REQ_NAME, ReqFileConstants.NODE_TYPE_DOC,
        ReqFileConstants.SUFFIX_DOC);
      packNode.add(node1);
      ReqTreeNode node2 = createReqFileLeafNode(ReqFileConstants.WEITUO_SHU, ReqFileConstants.WEITUO_SHU_NAME, ReqFileConstants.NODE_TYPE_DOC,
        ReqFileConstants.SUFFIX_DOC);
      packNode.add(node2);
    }
    //    ReqTreeNode node3 = createReqFileLeafNode(ReqFileConstants.GONGYS_ZIZ, ReqFileConstants.GONGYS_ZIZ_NAME, ReqFileConstants.NODE_TYPE_DOC,
    //      ReqFileConstants.SUFFIX_DOC);
    //    packNode.add(node3);
    rootNode.add(packNode);
    return packNode;
  }

  public void generNodeFile(ReqTreeNode packNode) {
    Enumeration en = packNode.children();
    while (en.hasMoreElements()) {
      createNodeFile((ReqTreeNode) en.nextElement());
    }
  }

  public void createNodeFile(ReqTreeNode treeNode) {
    if (treeNode.getNodeCode().equals(ReqFileConstants.YEWU_REQ_CODE)) {
      createLocalFile(treeNode, ReqFileConstants.YEWU_REQ_MODLE_FILE_ID);
    } else if (treeNode.getNodeCode().equals(ReqFileConstants.WEITUO_SHU)) {
      createLocalFile(treeNode, ReqFileConstants.WEITUO_SHU_MODLE_FILE_ID);
    } else if (treeNode.getNodeCode().equals(ReqFileConstants.GONGYS_ZIZ)) {
      createLocalFile(treeNode, ReqFileConstants.GONGYS_ZIZ_MODLE_FILE_ID);
    } else if (treeNode.getNodeCode().equals(ReqFileConstants.GONGYS_DYCG)) {
      createLocalFile(treeNode, ReqFileConstants.GONGYS_DYCG_MODLE_FILE_ID);
    } else if (treeNode.getNodeCode().equals(ReqFileConstants.GONGYS_CAR)) {
      createLocalFile(treeNode, ReqFileConstants.GONGYS_CAR_MODLE_FILE_ID);
    }
  }

  private ReqTreeNode createReqFileLeafNode(String NodeCode, String nodeName, String NodeType, String fileExtension) {
    ReqTreeNode node = new ReqTreeNode();
    node.setNodeCode(NodeCode);
    node.setNodeName(nodeName);
    node.setNodeType(NodeType);
    node.setNodeDirPath(nodeName);
    node.setFileExtension(fileExtension);
    node.setPackReq(true);
    return node;

  }

  private void downloadReqFile(ZcEbRequirement zcEbRequirement) throws Exception {
    //获取AsFile文件
    if (zcEbRequirement.getZcEbReqFile().getFileContent() != null) {
      //下载的路径
      File file = downloadFile(zcEbRequirement, ReqFileConstants.REQ_FILE_DOWN_PATH);
      //解压的路径
      ZipFilePubFunc.unzipFileToDestDir(file, downLoadPath);

    }
  }

  public File downloadFile(ZcEbRequirement zcEbRequirement, String fileDownloadPath) {
    File path = new File(fileDownloadPath);
    if (!path.exists()) {
      path.mkdirs();
    }
    //本地如果存在，先删除
    File tempFile = new File(fileDownloadPath + File.separator + zcEbRequirement.getZcEbReqFile().getFileName());
    if (tempFile.exists() && tempFile.isFile()) {
      tempFile.delete();
    }
    FileOutputStream os = null;

    byte[] b = zcEbRequirement.getZcEbReqFile().getFileContent();

    try {

      os = new FileOutputStream(tempFile);

      os.write(b, 0, b.length);

    } catch (Exception e) {
      e.printStackTrace();
      throw new BusinessException("保存下载文件时出错！", e);

    } finally {
      try {
        os.close();
      } catch (IOException e) {
        // TCJLODO Auto-generated catch block
        e.printStackTrace();
      }

    }
    return tempFile;
  }

  private void createLocalFile(ReqTreeNode treeNode, String fileId) {
    File path = new File(downLoadPath + treeNode.getParent() + File.separator);

    if (!path.exists()) {
      path.mkdirs();
    }
    File file = new File(path + File.separator + treeNode.getNodeName() + treeNode.getFileExtension());

    if (!file.exists()) {
      AsFile asFile = null;
      if (templateMap.get(fileId) == null) {
        asFile = baseDataServiceDelegate.getAsFileById(fileId, requestMeta);
        templateMap.put(fileId, asFile);
      } else {
        asFile = templateMap.get(fileId);
      }
      if (asFile == null) {
        throw new BusinessException("没有找到需求的模板的文件！");
      }
      File tempFile = new File(path + File.separator + asFile.getFileName());

      FileOutputStream os = null;

      byte[] b = asFile.getFileContent();

      try {

        os = new FileOutputStream(tempFile);

        os.write(b, 0, b.length);

        os.close();

      } catch (Exception e) {

        e.printStackTrace();

        throw new BusinessException("保存下载文件时出错！", e);

      }
    }

  }

  public File zipReqFile() throws Exception {
    //如果存在压缩文件，先删掉压缩文件
    //创建配置文件
    createConfigFile((ReqTreeNode) regFileLeftTree.tree.getModel().getRoot(), downLoadPath + File.separator + "config.xml");

    File uploadPath = new File(ReqFileConstants.REQ_FILE_UPLOAD_PATH + File.separator);
    if (!uploadPath.exists()) {
      uploadPath.mkdirs();
    }
    File destionFile = new File(ReqFileConstants.REQ_FILE_UPLOAD_PATH + File.separator + zcEbRequirement.getZcEbEntrust().getZcMakeName() + "("
      + zcEbRequirement.getZcEbEntrust().getSnCode() + ")" + ".zip");

    if (destionFile.isFile() && destionFile.exists()) {
      destionFile.delete();
    }
    //判断输出路径是否存在

    try {
      ZipDirFile.zip(downLoadPath, ReqFileConstants.REQ_FILE_UPLOAD_PATH + File.separator + zcEbRequirement.getZcEbEntrust().getZcMakeName() + "("
        + zcEbRequirement.getZcEbEntrust().getSnCode() + ")" + ".zip", null);
    } catch (Exception e) {

      e.printStackTrace();
      throw e;
    }

    return destionFile;

  }

  public void upLoadZipFile() throws Exception {
    //保存当前的word文档

    File destionFile = zipReqFile();
    System.out.println("-------------压缩文件-----------");

    doUpload(destionFile, zcEbRequirement.getZcEbReqFile());

    System.out.println("-------------更新需求文件的内容-----------");

  }

  public void doUpload(File file, ZcEbReqFile zcEbReqFile) {
    System.out.println("file.length() === " + file.length());

    FileInputStream fis = null;

    try {

      fis = new FileInputStream(file);

      BigDecimal available = new BigDecimal(fis.available());

      byte[] content = new byte[available.intValue()];

      fis.read(content);

      zcEbRequirement.getZcEbReqFile().setFileContent(content);

      zcEbRequirement.getZcEbReqFile().setFileName(file.getName());

      zcEbRequirement.getZcEbReqFile().setUploadTime(new Date());
    } catch (Exception e) {

      e.printStackTrace();

    } finally {

      if (fis != null) {

        try {

          fis.close();

        } catch (IOException e) {

          e.printStackTrace();

        }

      }

    }

  }

  public static String getExtension(File f) {

    String ext = null;

    String s = f.getName();

    int i = s.lastIndexOf('.');

    if (i > 0 && i < s.length() - 1) {

      ext = s.substring(i + 1).toLowerCase();

    }

    return ext;
  }

  public boolean createConfigFile(ReqTreeNode rootNode, String fullPath) {
    File file = new File(fullPath);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        // TCJLODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    XStream xs = new XStream();
    FileOutputStream fos = null;
    OutputStreamWriter writer = null;
    try {
      fos = new FileOutputStream(fullPath);
      writer = new OutputStreamWriter(fos, Charset.forName(GV.XML_CHAR_CODE));
      writer.write("<?xml version=\"1.0\" encoding=\"" + GV.XML_CHAR_CODE + "\" ?>\n");
      xs.toXML(rootNode, writer);
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
    return true;
  }

  /**
   * 
  * @Description: TODO(这里用一句话描述这个方法的作用)
  * @return ReqTreeNode 返回类型
  * @since 1.0
   */

  public ReqTreeNode readTreeNodes() throws Exception {
    FileInputStream fis = new FileInputStream(downLoadPath + File.separator + "config.xml");
    XStream xs = new XStream(new DomDriver());
    ReqTreeNode treeNodes = (ReqTreeNode) xs.fromXML(new InputStreamReader(fis, GV.XML_CHAR_CODE));
    if (fis != null) {
      fis.close();
    }
    return treeNodes;
  }

  public void refreshData(ZcEbRequirement zcEbRequirement) {
    this.zcEbRequirement = zcEbRequirement;
    //主要是刷新标段的节点。
    List<ZcEbPack> packList = zcEbRequirement.getPackList();
    ReqTreeNode root = (ReqTreeNode) regFileLeftTree.getTree().getModel().getRoot();

    Enumeration en = root.children();

    while (en.hasMoreElements()) {
      ReqTreeNode node = (ReqTreeNode) en.nextElement();
      if (!node.getNodeType().equals(ReqFileConstants.NODE_TYPE_PACK)) {
        continue;
      } else {
        if (!isExtisPackCodeByTreeNode(node.getNodeCode(), packList)) {
          InvisibleTreeModel treeModel = (InvisibleTreeModel) regFileLeftTree.getTree().getModel();
          treeModel.removeNodeFromParent(node);
          regFileLeftTree.getTree().repaint();
          File file = new File(downLoadPath + node.getNodesFullPath());
          deleteFile(file);
        }
      }
    }
    for (ZcEbPack zcEbPack : packList) {
      //不存在的创建新的标段节点
      if (!isExtisTreeNodeByPackCode(zcEbPack.getPackCode(), root)) {
        ReqTreeNode packNode = createPackNode(zcEbPack, root);
        generNodeFile(packNode);
        InvisibleTreeModel treeModel = (InvisibleTreeModel) regFileLeftTree.getTree().getModel();
        treeModel.insertNodeInto(packNode, root, root.getChildCount() - 1);
        regFileLeftTree.getTree().repaint();
        regFileLeftTree.expandAll(new TreePath(regFileLeftTree.getTree().getModel().getRoot()), true);
      }
    }
  }

  /**
   * 
  * @Description: 判断是否存在该标段编号节点
  * @return boolean 返回类型
  * @since 1.0
   */
  public boolean isExtisTreeNodeByPackCode(String packCode, ReqTreeNode root) {
    Enumeration en = root.children();
    while (en.hasMoreElements()) {
      ReqTreeNode node = (ReqTreeNode) en.nextElement();
      if (node.getNodeType().equals(ReqFileConstants.NODE_TYPE_PACK) && node.getNodeCode().equals(packCode)) {
        return true;
      }
    }
    return false;

  }

  /**
   * 
  * @Description: 判断是否存在该标段编号节点
  * @return boolean 返回类型
  * @since 1.0
   */
  public boolean isExtisPackCodeByTreeNode(String treeNode, List<ZcEbPack> packList) {
    //判断treeNode在packList是否存在
    for (ZcEbPack zcEbPack : packList) {
      if (zcEbPack.getPackCode().equals(treeNode)) {
        return true;
      }
    }

    return false;

  }

  public void deleteFile(File file) {

    if (!file.exists()) {
      return;
    }
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

}
