/**   
* @(#) project: ZC
* @(#) file: ZcEbRfqDelayBidEditPanel.java
* 
* Copyright 2010 UFGOV, Inc. All rights reserved.
* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
* 
*/
package com.ufgov.zc.client.zc.zcebrfq;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.LoadMoldButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.util.NumUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.WordFileUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.bulletin.ZcEbBulletinBidEditPanel;
import com.ufgov.zc.client.zc.ztb.activex.WordPane;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.BaseException;
import com.ufgov.zc.common.system.util.DateUtil;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.model.ZcEbBulletinWordMold;
import com.ufgov.zc.common.zc.model.ZcEbBulletinWordMoldParam;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbProjZbFile;
import com.ufgov.zc.common.zc.publish.IZcEbBulletinServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbBulletinWordMoldServiceDelegate;

/**
* @ClassName: ZcEbRfqDelayBidEditPanel
* @Description: TODO(这里用一句话描述这个类的作用)
* @date: 2010-9-20 下午02:55:37
* @version: V1.0 
* @since: 1.0
* @author: fanpeile
* @modify: 
*/
public class ZcEbRfqDelayBidEditPanel extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZcEbBulletinBidEditPanel.class);

  private IZcEbBulletinServiceDelegate zcEbBulletinServiceDelegate = (IZcEbBulletinServiceDelegate) ServiceFactory.create(
    IZcEbBulletinServiceDelegate.class, "zcEbBulletinServiceDelegate");

  protected IZcEbBulletinServiceDelegate getIZcEbBulletinServiceDelegate() {
    return this.zcEbBulletinServiceDelegate;
  }

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_BID;

  private String sqlMapSelectedMold = "ZcEbBulletinWordMold.getZcEbBulletinWordMoldBid";

  private String replaceValue = "";

  private String fileName = "";

  private String projName = "";

  private String projCode = "";

  private ZcEbBulletin oldBulletin;

  private boolean ifLoadMold = false;

  private boolean ifRePalceBookMark = true;

  private ZcEbRfqDelayBidEditPanel self = this;

  private GkBaseDialog parent;

  private JTabbedPane tabPane = new JTabbedPane();

  private WordPane wordPane = new WordPane();

  private FuncButton saveButton = new SaveButton();

  private FuncButton editButton = new EditButton();

  private SendButton sendButton = new SendButton();

  private LoadMoldButton loadMoldButton = new LoadMoldButton();

  private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private JFuncToolBar subPackTableToolbar;

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  private ZcEbBulletin zcEbBulletin;

  ElementConditionDto packDto = new ElementConditionDto();

  TextFieldEditor fieldZcMakeCode = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_CODE), "zcMakeCode");

  TextFieldEditor fieldZcMakeName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME), "zcMakeName");

  String moldColumNames[] = { "模板编号", "模板名称", "公告模板类型", "状态", "备注" };

  ZcEbBulletinWordMoldFnHandler handlera = new ZcEbBulletinWordMoldFnHandler(moldColumNames);

  ForeignEntityFieldEditor fieldMoldName = new ForeignEntityFieldEditor(this.sqlMapSelectedMold, 20, handlera, moldColumNames, "载入模板", "moldName");

  private BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(this.compoId);

  private BillElementMeta detailBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd(this.compoId + "_PACK");

  public ZcEbRfqDelayBidEditPanel(GkBaseDialog parent, List zcEbPackList) {
    super(new ZcEbBulletin(), "ZC_EB_BULLETIN");
    this.parent = parent;
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), parent.getTitle(), TitledBorder.CENTER,
      TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.colCount = 3;
    init();
    requestMeta.setCompoId(compoId);
    ElementConditionDto dto = new ElementConditionDto();
    dto.setProjCode(((ZcEbPack) zcEbPackList.get(0)).getProjCode());
    List list = zcEbBulletinServiceDelegate.getZcEbBulletinBid(dto, requestMeta);
    if (list != null && list.size() > 0) {
      zcEbBulletin = (ZcEbBulletin) list.get(0);
    }
    WordFileUtil.setDir("bulletin");
    addSubPane();
    refreshMainData();
    updateFieldEditorsEditable();
  }

  private String refreshMainData() {
    if (zcEbBulletin == null) {//新增页面
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
      zcEbBulletin = new ZcEbBulletin();

      setDefaultValue(zcEbBulletin, ZcSettingConstants.PAGE_STATUS_NEW);
    }
    this.setEditingObject(zcEbBulletin);
    setOldObject();
    return zcEbBulletin.getFileID();
  }

  public void refreshData() {
    String fileID = refreshMainData();
    refreshSubTableData(fileID);
  }

  private void refreshSubTableData(String fileID) {
    if (wordPane != null) {
      wordPane.close();
    }
    if (fileID != null && !fileID.equals("")) {
      this.fileName = WordFileUtil.loadMold(fileID);
      wordPane.open(this.fileName);
    } else {
      this.fileName = WordFileUtil.loadDefaultMold();
      wordPane.open(this.fileName);
    }
  }

  private void setDefaultValue(ZcEbBulletin bulletin, String pageStatus) {
    if (pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {
      bulletin.setInputorName(WorkEnv.getInstance().getCurrUserName());
      bulletin.setExecuteDate(WorkEnv.getInstance().getTransDate());
      bulletin.setNd(WorkEnv.getInstance().getSysNd());
      bulletin.setBulletinStatus("0");
      bulletin.setBulletinType(ZcEbBulletinConstants.TYPE_BULLETIN_BID);

    }
  }

  private void addSubPane() {
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

  @Override
  protected void updateFieldEditorsEditable() {
    super.updateFieldEditors();
    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW) || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {
      for (AbstractFieldEditor fd : this.fieldEditors) {
        if (fd.getFieldName() != null
          && (fd.getFieldName().equals("bulletinID") || fd.getFieldName().equals("bulletinStatus") || fd.getFieldName().equals("inputDate")
            || fd.getFieldName().equals("inputorName") || fd.getFieldName().equals("zcMakeCode") || fd.getFieldName().equals("zcMakeName"))) {
          fd.setEnabled(false);
        } else {
          fd.setEnabled(true);
        }
      }
    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {
      for (AbstractFieldEditor fd : this.fieldEditors) {
        fd.setEnabled(false);
      }
    }
  }

  private void setOldObject() {
    oldBulletin = (ZcEbBulletin) ObjectUtil.deepCopy(zcEbBulletin);
  }

  public boolean isDataChanged() {
    //如果载入了模板并且进行了数据填充，说明数据已经修改
    if (ifRePalceBookMark) {
      return true;
    } else {
      return !DigestUtil.digest(oldBulletin).equals(DigestUtil.digest(zcEbBulletin));
    }
  }

  public JComponent createSubBillPanel() {
    return this.tabPane;
  }

  public String doOpenMold(List valueList, ZcEbBulletinWordMold bulletinMold) {
    if (wordPane != null) {
      wordPane.close();
    }
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

  private String saveBulletinContent() {
    String fileID = "";

    if (fileName != null && fileName.length() != 0)
      fileID = WordFileUtil.uploadBulletinWordConstent(fileName);
    return fileID;
  }

  private boolean checkBeforeSave() {
    List notNullBillElementList = this.billElementMeta.getNotNullBillElement();
    List notNullDetailBillElementList = this.detailBillElementMeta.getNotNullBillElement();
    StringBuilder errorInfo = new StringBuilder();
    String validateInfo = ZcUtil.validateBillElementNull(zcEbBulletin, notNullBillElementList);

    if (validateInfo.length() != 0) {
      errorInfo.append("").append(validateInfo.toString()).append("\n");
    }

    if (errorInfo.length() != 0) {
      JOptionPane.showMessageDialog(this.parent, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  /**
   * 创建字段对象
   */
  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();
    fieldZcMakeCode.setEnabled(false);
    editorList.add(fieldZcMakeCode);

    editorList.add(fieldZcMakeName);
    fieldZcMakeName.setEnabled(false);

    AsValFieldEditor fieldBulletinStatus = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_BULLETIN_STATUS),
      "bulletinStatus", "ZC_EB_BULLETIN_STATUS");
    editorList.add(fieldBulletinStatus);
    fieldMoldName.setEnabled(true);
    editorList.add(fieldMoldName);

    TextFieldEditor fieldInputorName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_INPUTOR_NAME), "inputorName");
    fieldInputorName.setEnabled(false);
    editorList.add(fieldInputorName);

    DateFieldEditor fieldInputDate = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_INPUT_DATE), "inputDate");
    fieldInputDate.setEnabled(false);
    editorList.add(fieldInputDate);

    TextFieldEditor fieldRemark = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_REMARK), "remark");
    editorList.add(fieldRemark);

    return editorList;
  }

  /**
   * 以下添加按钮和按钮方法
   */
  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(compoId);
    toolBar.add(loadMoldButton);
    toolBar.add(editButton);
    toolBar.add(saveButton);
    toolBar.add(sendButton);
    saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doSave();
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
    loadMoldButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doReplaceBookMarks();
      }
    });
  }

  public boolean doSave() {
    if (!isDataChanged()) {
      JOptionPane.showMessageDialog(self, "数据未发生变化，不需要保存！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
      updateFieldEditorsEditable();
      return false;
    }

    if (!ifRePalceBookMark) {
      JOptionPane.showMessageDialog(self, "没有填充公告内容，请点击【填充模板】按钮生成正式公告后再保存！", "提示", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (!checkBeforeSave())
      return false;

    String fileID = saveBulletinContent();
    zcEbBulletin.setFileID(fileID);
    String bulletinID = NumUtil.getNum(compoId, "BULLETIN_ID", zcEbBulletin, requestMeta);
    zcEbBulletin.setBulletinID(bulletinID);
    zcEbBulletin.setBulletinStatus("0");
    this.getIZcEbBulletinServiceDelegate().insertFN(zcEbBulletin, requestMeta);
    this.oldBulletin = (ZcEbBulletin) ObjectUtil.deepCopy(zcEbBulletin);
    JOptionPane.showMessageDialog(self, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
    this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
    updateFieldEditorsEditable();
    wordPane.setEnabled(false);
    return true;
  }

  public void doEdit() {
    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
    updateFieldEditorsEditable();
  }

  public void doSend() {
    if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);
      if (num == JOptionPane.YES_OPTION) {
        if (!doSave()) {
          return;
        }
      } else {
        return;
      }
    }
    zcEbBulletin.setAuditorId(WorkEnv.getInstance().getCurrUserId());
    this.getIZcEbBulletinServiceDelegate().newCommitFN(zcEbBulletin, requestMeta);
    JOptionPane.showMessageDialog(self, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
    this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
    updateFieldEditorsEditable();
  }

  public boolean publishToHtml(String htmlName, String attachTitle) {
    if (attachTitle.length() > 0) {
      boolean ifFindBookMark = wordPane.selectBookMark(ZcEbBulletinConstants.ATTACH_FILE_BOOKMARK_NAME);
      if (ifFindBookMark) {
        boolean ifInsertAttachment = wordPane.insertAttachmentUrl(WordFileUtil.getPortalAttachPathFileName(attachTitle), attachTitle);
        if (!ifInsertAttachment) {
          UIUtilities.showStaickTraceDialog(new BaseException(), this, "错误", "发布公告时,没有正确插入附件文件!");
          return false;
        }
      }
    }

    boolean ifConvert = wordPane.convertWordToHtml(WordFileUtil.getHtmlFileName(htmlName));
    if (!ifConvert) {
      UIUtilities.showStaickTraceDialog(new BaseException(), this, "错误", "发布公告时,没有正确生成网页文件!");
      return false;
    }
    return true;
  }

  public boolean doPublish() {
    if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);
      if (num == JOptionPane.YES_OPTION) {
        if (!doSave()) {
          return false;
        }
      }
    }

    ZcEbProjZbFile zcEbProjZbFile = this.getIZcEbBulletinServiceDelegate().getZcEbProjZbFile(zcEbBulletin.getProjCode(), "0", requestMeta);

    String title = zcEbBulletin.getProjName() + "招标公告";
    String href = "";
    String attachFileName = "";

    if (zcEbProjZbFile == null || zcEbProjZbFile.getFileId() == null || zcEbProjZbFile.getFileId().equals("")) {
      int num = JOptionPane.showConfirmDialog(this, "没有找到此项目的招标文件，是否继续发布？", "保存确认", 0);
      if (num == JOptionPane.NO_OPTION) {
        return false;
      }
    }

    //获得招标文件信息
    if (zcEbProjZbFile != null && zcEbProjZbFile.getFileName() != null) {
      attachFileName = zcEbProjZbFile.getFileName();
      WordFileUtil.createFile(AsOptionMeta.getOptVal("OPT_ZC_ATTACH_FILE_TEMP"), AsOptionMeta.getOptVal("OPT_ZC_ATTACH_FILE_TEMP") + title, this,
        WordFileUtil.getFileContent(zcEbProjZbFile.getFileId()));
    }
    href = WordFileUtil.getPortalHref(zcEbBulletin.getFileID());
    String pletID = AsOptionMeta.getOptVal("OPT_ZC_PLET_BID");

    //插入招标文件并转换公告为html网页
    if (!publishToHtml(zcEbBulletin.getFileID(), attachFileName))
      return false;

    //把生成的html网页写入门户招标公告栏目
    boolean ifPubSuccess = this.getIZcEbBulletinServiceDelegate().pubBulletinFN(title, this.requestMeta.getSvUserName(), href, pletID, requestMeta,
      zcEbBulletin);
    if (!ifPubSuccess) {
      UIUtilities.showStaickTraceDialog(new BaseException(), this, "错误", "向外网发布公告失败!");
      return false;
    }

    zcEbBulletin.setBulletinStatus(ZcEbBulletinConstants.STATUS_BULLETIN_BID_PUBLISHED);
    zcEbBulletin.setEffectiveDate(new java.util.Date());
    this.getIZcEbBulletinServiceDelegate().updateFN(zcEbBulletin, requestMeta);

    JOptionPane.showMessageDialog(self, "发布成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
    this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
    updateFieldEditorsEditable();
    return true;
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
    if (wordPane != null) {
      wordPane.close();
    }
    this.parent.dispose();
  }

  public void doReplaceBookMarks() {

    if (!ifLoadMold) {
      JOptionPane.showMessageDialog(self, "没有成功载入模板，不能进行填充模板操作！", "提示", JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (replaceValue != null && !replaceValue.equals("")) {
      wordPane.replaceBookMarks(this.replaceValue);
    }
    WordFileUtil.delete(this.fileName);

    wordPane.save(this.fileName);
    this.fileName = this.fileName;
    ifRePalceBookMark = true;
    ifLoadMold = false;
  }

  /**
   * 选择公告模板外部部件信息
   * @author admin
   *
   */
  private class ZcEbBulletinWordMoldFnHandler implements IForeignEntityHandler {

    private String columNames[];

    private List bulletinMoldParamList = null;

    private Map valueMap;

    private IZcEbBulletinWordMoldServiceDelegate zcEbBulletinWordMoldServiceDelegate = (IZcEbBulletinWordMoldServiceDelegate) ServiceFactory.create(
      IZcEbBulletinWordMoldServiceDelegate.class, "zcEbBulletinWordMoldServiceDelegate");

    protected IZcEbBulletinWordMoldServiceDelegate getIZcEbBulletinWordMoldServiceDelegate() {
      return this.zcEbBulletinWordMoldServiceDelegate;
    }

    public List getParamList() {
      return this.bulletinMoldParamList;
    }

    public void setParamList(List paramList) {
      this.bulletinMoldParamList = paramList;
    }

    public ZcEbBulletinWordMoldFnHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {

      replaceValue = "";
      for (Object object : selectedDatas) {
        ZcEbBulletinWordMold bulletinMold = (ZcEbBulletinWordMold) object;
        zcEbBulletin.setMoldName(bulletinMold.getBulletinMoldName());

        List bulletinMoldParamList = zcEbBulletinWordMoldServiceDelegate
          .getZcEbBulletinWordMoldParam(requestMeta, bulletinMold.getBulletinMoldCode());
        for (Iterator it = bulletinMoldParamList.iterator(); it.hasNext();) {
          ZcEbBulletinWordMoldParam bulletinMoldParam = (ZcEbBulletinWordMoldParam) it.next();
          if (bulletinMoldParam.getParamType() == null) {
            continue;
          }
          Map paramMap = new HashMap();
          paramMap.put("fieldName", bulletinMoldParam.getFieldName());
          paramMap.put("tableName", bulletinMold.getDataView());
          paramMap.put("projCode", zcEbBulletin.getProjCode());
          try {
            String paramValue = getParamValue(paramMap, zcEbBulletin.getProjCode(), bulletinMoldParam.getParamType(),
              bulletinMoldParam.getFieldName());
            bulletinMoldParam.setParamValue(paramValue);
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(self, "没有找到模板“" + bulletinMold.getBulletinMoldName() + "”（模板代码:" + bulletinMold.getBulletinMoldCode()
              + "）参数【" + bulletinMoldParam.getParamName() + "】的信息，请检查模板参数是否正确！", "提示", JOptionPane.ERROR_MESSAGE);
            return;
          }
        }
        this.setParamList(bulletinMoldParamList);
        setEditingObject(zcEbBulletin);
        replaceValue = doOpenMold(bulletinMoldParamList, bulletinMold);
      }

      if (replaceValue != null && replaceValue.length() > 0) {
        ifLoadMold = true;
        ifRePalceBookMark = false;
        wordPane.setEnabled(false);
      }
    }

    public String getParamValue(Map paramMap, String projCode, String paramType, String tableName) {
      String strParamValue = "";
      if (paramType.equals(ZcSettingConstants.MOLD_PARAM_TYPE_FIELD)) {

        if (valueMap == null) {
          valueMap = this.getIZcEbBulletinWordMoldServiceDelegate().getZcEbBulletinWordMoldParamValue(requestMeta, paramMap);
        } else {

          Object obj = valueMap.get(paramMap.get("fieldName"));

          if (obj instanceof String) {
            return obj.toString();
          }
          if (obj instanceof BigDecimal) {
            BigDecimal b1 = (BigDecimal) obj;
            return b1.toString();
          }
          if (obj instanceof Date) {
            return DateUtil.dateToDdString((Date) obj);

          }
        }

      } else {
        String subParamValue = getSubParamValue(tableName, projCode, paramType);

        if (subParamValue != null && subParamValue.length() != 0) {
          strParamValue = subParamValue;
        }
      }
      return strParamValue;
    }

    public String getSubParamValue(String tableName, String projCode, String loopType) {
      Map subParamMap = new HashMap();
      subParamMap.put("tableName", tableName);
      subParamMap.put("projCode", projCode);

      List subParamValueList = getValueList(subParamMap);

      if (subParamValueList == null || subParamValueList.size() == 0)
        return null;

      StringBuffer sb = new StringBuffer();

      for (Iterator item = subParamValueList.iterator(); item.hasNext();) {
        Map valueMap = (Map) item.next();

        for (Iterator valueItem = valueMap.entrySet().iterator(); valueItem.hasNext();) {
          Map.Entry entry = (Map.Entry) valueItem.next();

          if (entry.getKey().toString().equalsIgnoreCase("PROJECT_CODE"))
            continue;

          Object value = entry.getValue();
          if (value == null)
            value = "";
          if (loopType.equalsIgnoreCase(ZcSettingConstants.MOLD_PARAM_TYPE_TABLE)) {
            sb.append("      " + value + "\n");
          } else if (loopType.equalsIgnoreCase(ZcSettingConstants.MOLD_PARAM_TYPE_LONG_STRING)) {
            sb.append("、" + value);
          }

        }
      }

      if (loopType.equalsIgnoreCase(ZcSettingConstants.MOLD_PARAM_TYPE_LONG_STRING)) {
        return sb.substring(1, sb.length()).toString();
      } else {
        return sb.toString();
      }

    }

    public List getValueList(Map paramMap) {
      List valueList = null;
      valueList = this.getIZcEbBulletinWordMoldServiceDelegate().getZcEbBulletinMoldTableParamValue(requestMeta, paramMap);
      return valueList;
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
  }

}
