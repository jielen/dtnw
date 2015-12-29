package com.ufgov.zc.client.zc.bulletin;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.zc.project.reqfile.ReqFileConstants;
import com.ufgov.zc.client.zc.ztb.util.ZipFilePubFunc;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.exception.BusinessException;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.model.ZcEbReqFile;

public class ZcEbBulletinGSEditPanel extends AbstractZcEbBulletinEditPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1579905355384351558L;

  public ZcEbBulletinGSEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbBulletinGSListPanel listPanel) {

    super(parent, listCursor, tabStatus, listPanel, ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_GS);

  }

  protected String getModelName() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.TITLE_ZC_EB_BULLETIN_GS;
  }

  protected String getCompId() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_GS;
  }

  protected String getBulletinType() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.TYPE_BULLETIN_GS;
  }

  public String getSqlMapSelectedProj() {

    return "ZcEbProj.getZcEbSheet";

  }

  public String getSqlMapSelectedMold() {

    return "ZcEbBulletinWordMold.getZcEbBulletinWordMoldJLCGZX";

  }

  public String getOpiWay() {
    return "'" + ZcSettingConstants.PITEM_OPIWAY_DYLY + "'";
  }

  protected void setFieldMoldNameStatus() {
    super.setFieldMoldNameStatus();
    this.findWordMoldCondition.setType(getBulletinType());
  }

  public String fetchSn(ZcEbBulletin sheet) {
    String sn = null;
    if (sheet.getProjCode() == null) {
      JOptionPane.showMessageDialog(this, "项目为空不能记录相关信息 ！", "错误", JOptionPane.ERROR_MESSAGE);
      return sn;
    }
    ZcEbEntrust entrust = (ZcEbEntrust) this.zcEbBaseServiceDelegate.queryObject("ZcEbEntrust.getZcEbEntrustBySnCode", sheet.getProjCode(),
      requestMeta);
    sn = entrust.getSn();
    return sn;
  }

  public void doReplaceBookMarks() {

    super.doReplaceBookMarks();

    // 找到参数插入页签
    if(wordPane.selectBookMark("xq_cp_gg")){
      // 去掉该页签
//      String mark = "xq_cp_gg$$$$$ @@@@@";
//      wordPane.replaceBookMarks(mark);
      
      // 取得需求文档并解压
      List<String> files = new ArrayList<String>();
      ZcEbBulletin tin = (ZcEbBulletin) this.listCursor.getCurrentObject();
      List list = zcEbBaseServiceDelegate.queryDataForList("ZC_EB_REQ_FILE.selectZcEbReqFileByEntrustSnCode", tin.getProjCode(), requestMeta);
      if(list == null || list.size() == 0){
        return;
      }
      ZcEbReqFile file = (ZcEbReqFile) list.get(0); 
      String path = ReqFileConstants.REQ_FILE_PATH + tin.getProjCode() + File.separator;
      if(file.getFileContent() != null && file.getFileContent().length > 0){
        File f = downloadFile(file, ReqFileConstants.REQ_FILE_DOWN_PATH);
        ZipFilePubFunc.unzipFileToDestDir(f, path);
      }
      
      // 找到参数定义文件
      File dir = new File(path);
      File pack[] = dir.listFiles();
      
      for(int i = 0; i < pack.length; i++){
        if(pack[i].isDirectory()){
          String name[] = pack[i].list();
          for(int j = 0; j < name.length;j++){
            if(name[j].indexOf(ReqFileConstants.WEITUO_SHU_NAME) > -1){
              files.add(pack[i].getAbsolutePath() + File.separator + name[j]);
            }
          }
        }
      }
      // 合并文档
      wordPane.combineMsWord(files);
    }

  }

  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> fields = super.createFieldEditors();

    Integer[] allowMinutes = { 0, 10, 20, 30, 40, 50 };

    DateFieldEditor failureDate = new DateFieldEditor("公告截止时间", "failureDate", DateFieldEditor.TimeTypeH24, allowMinutes, true);

    fields.add(failureDate);

    return fields;
  }

  protected boolean checkBeforeSave(boolean isSend) {
    if (super.checkBeforeSave(isSend)) {
      ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();
      if (bulletin.getFailureDate() == null || bulletin.getFailureDate().before(new Date())) {

        JOptionPane.showMessageDialog(this.parent, "请正确填写[公告截止时间]", "提示", JOptionPane.WARNING_MESSAGE);

        return false;
      }
      return true;
    }
    return false;
  }

  private File downloadFile(ZcEbReqFile req, String fileDownloadPath) {
    File path = new File(fileDownloadPath);
    if (!path.exists()) {
      path.mkdirs();
    }
    //本地如果存在，先删除
    File tempFile = new File(fileDownloadPath + File.separator + req.getFileName());
    if (tempFile.exists() && tempFile.isFile()) {
      tempFile.delete();
    }
    FileOutputStream os = null;

    byte[] b = req.getFileContent();

    try {

      os = new FileOutputStream(tempFile);

      os.write(b, 0, b.length);

      os.close();
    } catch (Exception e) {
      e.printStackTrace();
      throw new BusinessException("下载需求时出错！", e);

    }
    return tempFile;
  }
}
