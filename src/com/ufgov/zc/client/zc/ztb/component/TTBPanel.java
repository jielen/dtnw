package com.ufgov.zc.client.zc.ztb.component;

import com.ufgov.zc.client.zc.ztb.JobThreads;
import com.ufgov.zc.client.zc.ztb.model.DBProperty;
import com.ufgov.zc.client.zc.ztb.service.SystemConfigService;
import com.ufgov.zc.client.zc.ztb.util.ActionMaps;
import com.ufgov.zc.client.zc.ztb.util.FileChooseFilter;
import com.ufgov.zc.client.zc.ztb.util.GV;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TTBPanel extends MainPanel {
  private static final long serialVersionUID = 5090641157665230959L;

  private MainPanel mainPanel = this;

  private DBProperty dbProperty = null;

  public TTBPanel(Window parent) throws Exception {
    WHO_I_AM = "TTB";
    parentEntity = parent;
    ActionMaps.initTTB();
    initToolBar();
    initPanel();
    this.leftPane.setVisible(false);
  }

  public void initToolBar() {
    this.toolBar = new JToolBar();
    this.toolBar.setFloatable(false);
    ZTBButton localConfigBtn = new ZTBButton("dbconfig");
    toolBar.add(localConfigBtn);
    CommonAction dbconfig = (CommonAction) ActionMaps.getActionsMap().get("toolLocalConfig");
    dbconfig.setComponent(this);
    localConfigBtn.addActionListener(dbconfig);
    JButton uploadBtn = new JButton(GV.getTranslate().get("uploadAndBid"), GV.getImageIcon().get("uploadAndBid"));
    uploadBtn.setToolTipText(GV.getImageIconTips().get("uploadAndBid"));
    toolBar.add(uploadBtn);
    addUploadAction(uploadBtn);
    JButton ecbjBtn = new JButton(GV.getTranslate().get("ecbj"), GV.getImageIcon().get("ecbj"));
    ecbjBtn.setToolTipText(GV.getImageIconTips().get("ecbj"));
    toolBar.add(ecbjBtn);
    addEcbjAction(ecbjBtn);
    JButton decodeBtn = new JButton(GV.getTranslate().get("decode"), GV.getImageIcon().get("decode"));
    decodeBtn.setToolTipText(GV.getImageIconTips().get("decode"));
    toolBar.add(decodeBtn);
    addDecodeAction(decodeBtn);
    JButton zipTestBtn = new JButton(GV.getTranslate().get("zipTest"), GV.getImageIcon().get("zipTest"));
    zipTestBtn.setToolTipText(GV.getImageIconTips().get("zipTest"));
    toolBar.add(zipTestBtn);
    addZipTestAction(zipTestBtn);
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

  private void addUploadAction(JButton uploadBtn) {
    uploadBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (!isReady()) {
          return;
        }
        JDialog dialog = new JDialog();
        dialog.setTitle(GV.getSimpleMsg("bidUploadTitle"));
        dialog.setSize(new Dimension(600, 380));
        dialog.add(new TBProjectSelectionPanel(dialog, mainPanel));
        dialog.setModal(true);
        //dialog.setResizable(false);
        dialog.setMinimumSize(new Dimension(600, 380));
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
      }
    });
  }

  private void addEcbjAction(JButton ecbjBtn) {
    ecbjBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (!isReady()) {
          return;
        }
        JDialog dialog = new JDialog();
        dialog.setTitle(GV.getSimpleMsg("ecbjTitle"));
        dialog.setSize(new Dimension(480, 340));
        dialog.add(new EcbjProjectSelectionPanel(dialog, mainPanel));
        dialog.setModal(true);
        dialog.setMinimumSize(new Dimension(480, 340));
        //dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
      }
    });
  }

  /**
   * 读取配置文件
   *
   * @return
   */
  private boolean isReady() {
    try {
      dbProperty = (new SystemConfigService()).readDBProperty();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, GV.getSimpleMsg("configErrNeedDelete") + GV.USER_DIR + File.separator + GV.DB_CONFIG_XML + "文件后重新启动该工具。");
      e.printStackTrace();
      return false;
    }
    if (dbProperty == null) {
      JOptionPane.showMessageDialog(null, GV.getSimpleMsg("pleaseSettingIPAndPort"));
      return false;
    }
    return true;
  }

  private void addDecodeAction(JButton decodeBtn) {
    decodeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (!isReady()) {
          return;
        }
        Map<String, String> paras = new HashMap<String, String>();
        paras.put("URL", dbProperty.getWebServiceHostPort() + "/ZC/");
        //paras.put("USERTYPE", "supplier");
        //        paras.put("USERTYPE", "master");
        //        paras.put("USERID", "zhangsj");
        //        paras.put("USERNAME", "zhangsj");
        //        paras.put("PASSWORD", "123");
        //        paras.put("ISEDITABLE","Y");
        JDialog dialog = new JDialog();
        dialog.setTitle("用户登录方式选择");
        dialog.setSize(new Dimension(420, 180));
        dialog.add(new LoginTypeSelectionPanel(dialog, paras));
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
      }
    });
  }

  public DBProperty getDbProperty() {
    if (isReady()) {
      return dbProperty;
    } else {
      return new DBProperty();
    }
  }

  public void setDbProperty(DBProperty dbProperty) {
    this.dbProperty = dbProperty;
  }
}
