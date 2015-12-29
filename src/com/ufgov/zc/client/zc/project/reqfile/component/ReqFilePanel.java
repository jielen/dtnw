package com.ufgov.zc.client.zc.project.reqfile.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.zc.CommonButton;
import com.ufgov.zc.client.zc.project.ZcEbRequirementEditPanel;
import com.ufgov.zc.client.zc.project.reqfile.ReqFileConstants;
import com.ufgov.zc.client.zc.project.reqfile.component.reqFileLeftTree.ReqFileLeftTree;
import com.ufgov.zc.client.zc.project.reqfile.component.reqFileLeftTree.ReqTreeNode;
import com.ufgov.zc.client.zc.project.reqfile.service.ReqFileService;
import com.ufgov.zc.client.zc.ztb.activex.WordPane;
import com.ufgov.zc.client.zc.ztb.util.EventPropertyName;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.zc.model.ZcEbReqFile;
import com.ufgov.zc.common.zc.model.ZcEbRequirement;
import com.ufgov.zc.common.zc.publish.IZcEbRequirementServiceDelegate;

/**
 * 
* @ClassName: reqFilePanel
* @Description: TODO(需求确认业务需求制作面板)
* @date: Oct 14, 2012 2:56:16 AM
* @version: V1.0 
* @since: 1.0
* @author: Administrator
* @modify:
 */
public class ReqFilePanel extends JPanel {
  private ZcEbRequirement zcEbRequirement;

  private JSplitPane splitPane;

  private ReqFileLeftTree reqFileLeftTree;

  protected JPanel rightPanel;

  private final ReqFileService reqFileService;

  private JFuncToolBar toolBar;

  public WordPane wordPane;

  public FuncButton editReqFileButton = new CommonButton("feditReqFile", "修改需求文件", "edit.jpg");

  public FuncButton saveButton = new CommonButton("fuploaReqdFile", "保存", "export.jpg");

  public boolean canEdit = false;

  private final RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  public IZcEbRequirementServiceDelegate zcEbRequirementServiceDelegate = (IZcEbRequirementServiceDelegate) ServiceFactory.create(
    IZcEbRequirementServiceDelegate.class, "zcEbRequirementServiceDelegate");

  @SuppressWarnings("unused")
  private static boolean isLastDoubleClickResponseSuccess = true;

  private String openedFilePath;// 当前打开的word文档

  private final ZcEbRequirementEditPanel zcEbRequirementEditPanel;

  public ReqFilePanel(ZcEbRequirement zcEbRequirement, ZcEbRequirementEditPanel zcEbRequirementEditPanel) {
    this.setLayout(new BorderLayout());
    reqFileService = new ReqFileService(zcEbRequirement);
    this.zcEbRequirementEditPanel = zcEbRequirementEditPanel;
    //保存当前的word文档
    if (zcEbRequirement.getZcEbReqFile() != null) {
      zcEbRequirement.getZcEbReqFile().setCreator(requestMeta.getSvUserName());
    } else {
      ZcEbReqFile reqFile = new ZcEbReqFile();
      reqFile.setCreator(requestMeta.getSvUserName());
      reqFile.setReqCode(zcEbRequirement.getReqCode());
      reqFile.setSn(zcEbRequirement.getSn());
      zcEbRequirement.setZcEbReqFile(reqFile);
    }
    this.zcEbRequirement = zcEbRequirement;
    initComponet();
  }

  private void initComponet() {

    initTopToolBal();
    initLeftTree();
    initRightPanel();
    initSplitPane();
    this.add(toolBar, BorderLayout.NORTH);
    this.add(splitPane, BorderLayout.CENTER);
  }

  private void initSplitPane() {

    splitPane = new JSplitPane();
    splitPane.setDividerLocation(280);
    splitPane.setDividerSize(6);
    splitPane.setMinimumSize(new Dimension(0, 0));
    splitPane.setOneTouchExpandable(true);
    splitPane.setLeftComponent(reqFileLeftTree);
    splitPane.setRightComponent(rightPanel);

  }

  private void initLeftTree() {

    reqFileLeftTree = reqFileService.createFilesTreePanel();
    reqFileLeftTree.repaint();

    reqFileLeftTree.addPropertyChangeListener(EventPropertyName.MOUSE_CLICK_PROPERTY_NAME, new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        //showTreeNodeMeg();
      }
    });
    reqFileLeftTree.addPropertyChangeListener(EventPropertyName.MOUSE_DBCLICK_PROPERTY_NAME, new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {

        String filePath = ReqFileConstants.REQ_FILE_PATH +

        reqFileLeftTree.getCurrentNode().getNodesFullPath() + reqFileLeftTree.getCurrentNode().getFileExtension();

        File file = new File(filePath);

        if (!file.exists()) {

          return;

        }
        if (!filePath.equals(openedFilePath)) {

          if (!isLastDoubleClickResponseSuccess) {

            return;

          }

          showTreeNodeInfo(filePath);

        }

      }
    });

  }

  private void initRightPanel() {
    rightPanel = new JPanel(new BorderLayout());
  }

  private void initTopToolBal() {
    this.toolBar = new JFuncToolBar();
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(zcEbRequirementEditPanel.listPanel.compoId);
    toolBar.add(editReqFileButton);
    toolBar.add(saveButton);
    saveButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        // 上传需求文件
        doUploadFile();
      }

    });

    editReqFileButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 新增
        doEditReqFile();
      }
    });

    this.toolBar.setFloatable(false);
  }

  public void doEditReqFile() {
    if (wordPane != null) {
      wordPane.unProtectDoc(ZcSettingConstants.WORD_PASSWORD);
    }
  }

  /**
   * 
  * @Description: 当标段的信息发生变化时，刷新左侧的树
  * @return void 返回类型
  * @since 1.0
   */
  public void refreshLeftTree(ZcEbRequirement zcEbRequirement) {
    this.zcEbRequirement = zcEbRequirement;
    reqFileService.refreshData(zcEbRequirement);
  }

  private void showTreeNodeInfo(String filePath) {

    //添加一个参数,是否是在评标的时候打开。
    final ReqTreeNode currNode = reqFileLeftTree.getCurrentNode();
    if (currNode != null) {
      //保存打开的word文件的内容
      doSave();
      closeWordPane();
      String nodeType = currNode.getNodeType();
      if (GV.NODE_TYPE_DOC.equals(nodeType)) {
        if (wordPane == null) {
          wordPane = new WordPane();
          wordPane.setMinimumSize(new Dimension(10, 1000));
          wordPane.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
              // 打开文件完成之后的回调函数
              boolean success = (Boolean) evt.getNewValue();
              if (!success) {
                if (wordPane != null) {
                  wordPane.closeNotSave();
                }
                return;
              }
              isLastDoubleClickResponseSuccess = true;
              splitPane.setDividerLocation(splitPane.getDividerLocation() - 1);
            }
          });
        }
        // 一定要先把wordPane加入到splitPane，之后再让wordPane加载word（调用wordPane.openAndProtect方法）,否则会有线程问题。
        splitPane.setDividerLocation(splitPane.getDividerLocation() + 1);
        splitPane.setRightComponent(wordPane);
        wordPane.openAndProtect(filePath, ZcSettingConstants.WORD_PASSWORD);
        openedFilePath = filePath;

      }
    }
  }

  /**
   * 
  * @Description: 保存word文件的内容
  * @return void 返回类型
  * @since 1.0
   */
  public void doSave() {
    if (this.wordPane != null) {
      this.wordPane.save();
    }
  }

  public void setZcEbReqFile() {
    try {
      reqFileService.upLoadZipFile();
    } catch (Exception e) {
      // TCJLODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public void doUploadFile() {
    doSave();
    boolean success = true;
    String errorInfo = "";
    if ("".equals(zcEbRequirement.getReqCode()) || zcEbRequirement.getReqCode() == null) {
      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + "请先点【保存】按钮保存新需求！", "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      reqFileService.upLoadZipFile();
      if (zcEbRequirement.getZcEbReqFile().getReqCode() == null || "".equals(zcEbRequirement.getZcEbReqFile().getReqCode())) {
        zcEbRequirement.getZcEbReqFile().setReqCode(zcEbRequirement.getReqCode());
      }
      zcEbRequirementServiceDelegate.updateZcEbReqFile(zcEbRequirement.getZcEbReqFile(), requestMeta);
      zcEbRequirementEditPanel.setOldObject();
    } catch (Exception e) {
      success = false;
      e.printStackTrace();
      errorInfo += e.getMessage();
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

    } else {
      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void closeWordPane() {
    if (wordPane != null) {
      wordPane.close();
    }
  }

}
