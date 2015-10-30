package com.ufgov.zc.client.component.zc.fieldeditor.zcbagency;import java.awt.BorderLayout;import java.awt.GridLayout;import java.awt.event.WindowAdapter;import java.awt.event.WindowEvent;import javax.swing.JFrame;import javax.swing.JPanel;import com.ufgov.zc.client.common.AsOptionMeta;import com.ufgov.zc.client.common.LangTransMeta;import com.ufgov.zc.client.component.JButtonTextField;import com.ufgov.zc.client.component.event.EditSyncEvent;import com.ufgov.zc.client.component.event.EditSyncListener;import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;import com.ufgov.zc.common.system.constants.ZcElementConstants;import com.ufgov.zc.common.system.util.LevelControlUtil;import com.ufgov.zc.common.zc.model.ZcBAgency;public class ZcBAgencySelectField extends JButtonTextField {  public ZcBAgencySelectField(TextFieldEditor name) {    super(20);    selectDialog = new ZcBAgencySelectDialog(owner, true, this, name);  }  public void setValue(Object value) {    this.value = value;    if (value != null) {      ZcBAgency data = (ZcBAgency) value;      this.setText(data.getAgency());      this.setToolTipText("[" + data.getAgency() + "]" + data.getAgencyName());    } else {      this.setText("");      this.setToolTipText(null);    }    this.fireValueChanged();  }  public void setZcBAgency(ZcBAgency value) {    this.setValue(value);  }  public ZcBAgency getZcBAgency() {    return (ZcBAgency) this.getValue();  }  public void handleClick(JButtonTextField jButtonTextField) {    await();    ((ZcBAgencySelectDialog) selectDialog).initDataBufferList();    selectDialog.setVisible(true);  }  //  private String dataRuleId;  //  //  public String getDataRuleId() {  //    return dataRuleId;  //  }  //  //  public void setDataRuleId(String dataRuleId) {  //    this.dataRuleId = dataRuleId;  //  }  //  private void init() {  //    this.elementCode = "ZC_B_AGENCY";  //    selectDialog = new ZcBAgencySelectDialog(owner, true, this);  //  }  //  //  public ZcBAgencySelectField(int col) {  //    super(col);  //    this.init();  //  }  //    //  //  //  public ZcBAgencySelectField(int col, String compoId) {  //    super(col);  //    this.compoId = compoId;  //    this.init();  //  }  //  //  public ZcBAgencySelectField(int col, Dialog owner) {  //    super(col);  //    this.owner = owner;  //    this.init();  //  }  //  //  public ZcBAgencySelectField(String dataRuleId, int col) {  //    super(col);  //    this.dataRuleId = dataRuleId;  //    this.init();  //  }  //  //  public ZcBAgencySelectField(String dataRuleId, String compoId, int col) {  //    super(col);  //    this.dataRuleId = dataRuleId;  //    this.compoId = compoId;  //    this.init();  //  }  //  public void setCtrlLevelNum(int ctrlLevelNum) {  //    super.setCtrlLevelNum(ctrlLevelNum);  //    this.ctrlLen = LevelControlUtil  //      .getCtrLength(AsOptionMeta.getOptVal("OPT_PD_PROJECT_LEVEL"), ctrlLevelNum);  //  }  public static void main(String[] args) {    JFrame f = new JFrame();    TextFieldEditor name = new TextFieldEditor("名称", "zcAgeyName");    ZcBAgencySelectField code = new ZcBAgencySelectField(name);    code.setValueByCode("000101");    code.setEditable(false);    code.setEnabled(false);    code.setEnabled(true);    //    name.setEditable(false);    name.setEnabled(true);    //    textField.setCompany(company);    //		textField.setLevelCtrl(true);    //    textField.setCtrlLevelNum(1);    //		textField.setRandomEdit(false);    //		textField.setPrefix("0001");    JPanel panel = new JPanel(new GridLayout(1, 2));    panel.add(code);    panel.add(name);    f.getContentPane().add(panel, BorderLayout.NORTH);    // f.pack();    // SwingUtilities.updateComponentTreeUI(panel);    f.setSize(400, 300);    f.setLocationRelativeTo(null);    f.setVisible(true);    f.addWindowListener(new WindowAdapter() {      public void windowClosing(WindowEvent e) {        System.exit(0);      }    });  }}