package com.ufgov.zc.client.zc.dljgAndjczw;import java.awt.BorderLayout;import java.awt.Container;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.io.File;import java.util.Date;import java.util.Iterator;import java.util.List;import java.util.Map;import javax.swing.JButton;import javax.swing.JLabel;import javax.swing.JOptionPane;import javax.swing.JPanel;import javax.swing.JScrollPane;import javax.swing.JTextField;import javax.swing.SwingUtilities;import com.ufgov.zc.client.common.ServiceFactory;import com.ufgov.zc.client.common.StringToModel;import com.ufgov.zc.client.common.WorkEnv;import com.ufgov.zc.client.component.zc.dataexchange.DelegDepAndJCNetWorkDataExchangeListPanel;import com.ufgov.zc.client.component.zc.dataexchange.model.CommonDataExchangeOperator;import com.ufgov.zc.client.zc.dljgAndjczw.nobusiness.ExportExchangeData;import com.ufgov.zc.client.zc.dljgAndjczw.nobusiness.ExportWorkFlowData;import com.ufgov.zc.client.zc.intranetandbidnet.IntrantAndBidnetAbstractDialog;import com.ufgov.zc.client.zc.ztb.component.ProgressGlassPane;import com.ufgov.zc.client.zc.ztb.util.PubFunction;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;import com.ufgov.zc.common.zc.publish.IDljgJczwDataImpDelegate;import com.ufgov.zc.common.zc.publish.IZcDelegJczwDataExchangeDelegate;public class DljgJczwDataImpDialog extends IntrantAndBidnetAbstractDialog {  /**   *    */  private static final long serialVersionUID = 1463990800534266821L;  private IDljgJczwDataImpDelegate dljgJczwDataImpDelegate = (IDljgJczwDataImpDelegate) ServiceFactory.create(IDljgJczwDataImpDelegate.class,  "dljgJczwDataImpDelegate");  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();  private JButton jButtonExec;  public DelegDepAndJCNetWorkDataExchangeListPanel listPanel;  private String opType;  private JButton jButtonExit;  public IZcDelegJczwDataExchangeDelegate zcDelegJczwDataExchangeDelegate;  public DljgJczwDataImpDialog(DelegDepAndJCNetWorkDataExchangeListPanel listPanel, String opType) {    zcDelegJczwDataExchangeDelegate = listPanel.zcDelegJczwDataExchangeDelegate;    this.listPanel = listPanel;    this.opType = opType;    Container container = self.getContentPane();    container.setLayout(new BorderLayout(6, 6));    container.removeAll();    JPanel middle = new JPanel(new BorderLayout());    JPanel inner = new JPanel(new BorderLayout(4, 4));    JPanel panel2 = new JPanel(new BorderLayout(4, 4));    panel2.add(new JLabel("导入位置:"), BorderLayout.WEST);    final JTextField savePathTF = new JTextField();    savePathTF.setName("pathTextField");    panel2.add(savePathTF, BorderLayout.CENTER);    JButton jButton = makeBrowerButton(savePathTF, false);    panel2.add(jButton, BorderLayout.EAST);    container.add(panel2, BorderLayout.NORTH);    JScrollPane scrollPane = this.makeTextAreaScrollPanel();    middle.add(scrollPane, BorderLayout.CENTER);    container.add(middle, BorderLayout.CENTER);    JPanel panel4 = new JPanel();    jButtonExec = new JButton("执行导入");    jButtonExit = new JButton("关闭");    jButtonExit.setVisible(false);    jButtonExec.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        if (savePathTF.getText() == null || "".equals(savePathTF.getText())) {          JOptionPane.showMessageDialog(self, "请指定导入文件的存放位置...", "提示", JOptionPane.OK_OPTION);          return;        }        if (!CommonDataExchangeOperator.checkFilePath(savePathTF.getText())) {          JOptionPane.showMessageDialog(self, "路径中存在非法字符，请检查...", "提示", JOptionPane.OK_OPTION);          return;        }        makeProgressGlassPane(savePathTF.getText(), null, false);      }    });    jButtonExit.addActionListener(new ActionListener() {      public void actionPerformed(ActionEvent e) {        doExit();      }    });    panel4.add(jButtonExec);    panel4.add(jButtonExit);    container.add(panel4, BorderLayout.SOUTH);  }  public void impExecute(String path, ProgressGlassPane glassPane) {    String targetPath = null;    try {      glassPane.getTimer().start();      isJobRunning = true;      jButtonExec.setEnabled(false);      progressText.append("文件开始读取\n");      targetPath = unZip2Path(path);      String flagFilePath = targetPath + File.separator + FLAG_FILE_NAME + ".xml";      File flagFile = new File(flagFilePath);      if (!flagFile.isFile()) {        progressText.append("系统不能确定，您要导入的文件来自哪个系统，导入失败！\n");        publishProgressText();        glassPane.getTimer().stop();        glassPane.setVisible(false);        isJobRunning = false;        jButtonExec.setEnabled(true);        return;      }      Map<String, String> map = (Map<String, String>) CommonDataExchangeOperator.readXmlFileToObject(flagFile.getAbsolutePath());      String value = (String) map.get(FLAG_KEY);      //如果当前是往代理机构导入数据，那必须是从内网导出的；如果当前是往内网导入数据，那必须是从代理机构导出的数据      if ((ZcDelegJczwDataExchange.OP_TYPE_DLJG_IN_LOG.equals(this.opType) && ZcDelegJczwDataExchange.OP_TYPE_JCZW_OUT.equals(value))      || (ZcDelegJczwDataExchange.OP_TYPE_JCZW_IN_LOG.equals(this.opType) && ZcDelegJczwDataExchange.OP_TYPE_DLJG_OUT.equals(value))) {        if (ZcDelegJczwDataExchange.OP_TYPE_DLJG_IN_LOG.equals(this.opType)) {          progressText.append("您要导入的文件不是内网系统导出数据，不能导入代理机构系统！\n");        } else {          progressText.append("您要导入的文件不是代理机构系统导出数据，不能导入内网系统！\n");        }        publishProgressText();        glassPane.getTimer().stop();        glassPane.setVisible(false);        isJobRunning = false;        jButtonExec.setEnabled(true);        return;      }      File dir = new File(targetPath);      File[] fList = dir.listFiles();      for (int i = 0; i < fList.length; i++) {        if ((FLAG_FILE_NAME + ".xml").equals(fList[i].getName())) {          continue;        }        if (fList[i].isDirectory()) {          File[] xmlFileList = fList[i].listFiles();          importFiles(xmlFileList);        } else {          if (!importFiles(new File[] { fList[i] }))            break;        }      }      //CommonDataExchangeOperator.deleteFiles(targetPath);      progressText.append("所有文件导入完毕\n");      publishProgressText();      glassPane.getTimer().stop();      //      glassPane.getProgressBar().setValue(100);      glassPane.getProgressInfo().setHorizontalAlignment(SwingUtilities.CENTER);      glassPane.setVisible(false);      isJobRunning = false;      jButtonExec.setEnabled(true);      jButtonExec.setVisible(false);      jButtonExit.setVisible(true);      PubFunction.deleteFile(targetPath, false);    } catch (Exception e) {      // TCJLODO Auto-generated catch block      e.printStackTrace();      progressText.append(e.getMessage() + "\n程序导入终止！");      publishProgressText();      glassPane.getTimer().stop();      glassPane.setVisible(false);      isJobRunning = false;      jButtonExec.setEnabled(true);      if (targetPath != null) {        PubFunction.deleteFile(targetPath, false);      }    }  }  private boolean importFiles(File[] xmlFileList) {    for (int j = 0; j < xmlFileList.length; j++) {      File xmlFile = (File) xmlFileList[j];      String name = xmlFile.getName();      String fileName = name.substring(0, name.length() - ".xml".length());      String msg = getTransName(fileName);      progressText.append(msg + "文件开始解析\n");      Object o = CommonDataExchangeOperator.readXmlFileToObject(xmlFile.getAbsolutePath());      progressText.append(msg + "文件开始导入\n");      if (ExportWorkFlowData.fileName.equals(fileName)) {//如果是导入工作流        dljgJczwDataImpDelegate.saveWorkFlowData((List) o, requestMeta);      } else if (ExportExchangeData.fileName.equals(fileName)) {        /**         * 此处还存在问题，导的数据，已经开始业务流程了，应该就要控制，不让重新导入。         */        //校验任务单编号是否存在        String resultStr = dljgJczwDataImpDelegate.checkZcEbEntrust((List) o, requestMeta);        if (resultStr != null && !resultStr.equals("")) {          progressText.append("任务单编号为 ").append(resultStr).append("已存在，导入失败\n");          return false;        }        addImpLog((List) o);      } else {//导入业务数据        if (o instanceof List) {          List list = (List) o;          int rowCount = 1;          for (Iterator iterator = list.iterator(); iterator.hasNext();) {            Object object = (Object) iterator.next();            if (object == null)              continue;            Object res = StringToModel.invokeMethod(dljgJczwDataImpDelegate, "insert", new Object[] { object, requestMeta });            progressText.append(msg + "插入第" + rowCount + "条记录\n");            if(res instanceof String && res != null && ((String) res).trim().length() > 0){              progressText.append("      " + res + "\n");            }            rowCount++;          }        } else {          Object res = StringToModel.invokeMethod(dljgJczwDataImpDelegate, "insert", new Object[] { o, requestMeta });          if(res instanceof String && res != null && ((String) res).trim().length() > 0){            progressText.append(msg + " " + res + "\n");          }else{            progressText.append(msg + " 记录导入完毕\n");          }        }      }      progressText.append(msg + "文件导入完毕\n");    }//    return true;  }  private void addImpLog(List list) {    Date d = requestMeta.getSysDate();    String userId = requestMeta.getSvUserID();    String editUserName = requestMeta.getSvUserName();    for (Iterator iterator = list.iterator(); iterator.hasNext();) {      ZcDelegJczwDataExchange record = (ZcDelegJczwDataExchange) iterator.next();      record.setOpType(this.opType);      record.setEditDate(d);      record.setEditUserId(userId);      record.setEditUserName(editUserName);    }    zcDelegJczwDataExchangeDelegate.addDataExchangeLog(list, requestMeta);  }  public void doExit() {    this.dispose();  }}