/** * ForeignEntityFieldEditor.java * com.ufgov.gk.client.component.zc.fieldeditor * Administrator * 2010-4-30 */package com.ufgov.zc.client.component.zc.fieldeditor;import java.awt.BorderLayout;import java.awt.Dimension;import java.util.List;import javax.swing.JFrame;import javax.swing.JPanel;import javax.swing.SwingUtilities;import javax.swing.UIManager;import org.apache.commons.beanutils.MethodUtils;import com.ufgov.smartclient.plaf.BlueLookAndFeel;import com.ufgov.zc.client.common.WorkEnv;import com.ufgov.zc.client.component.JButtonTextField;import com.ufgov.zc.client.zc.zcebsignup.ZcEbSupplierForeignEntityHandler;import com.ufgov.zc.common.system.constants.ZcSettingConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;/** * 外部部件选择框 * @author Administrator * */public class ForeignEntityField extends JButtonTextField {  /**   *    */  private static final long serialVersionUID = 6486922572479576827L;  private int nd = WorkEnv.getInstance().getTransNd();  private String sqlMapSelectedId;  private IForeignEntityHandler handler;  private String columNames[];  private String title;  private String fieldName;  private List dataBufferList;  private ElementConditionDto elementConditionDto = new ElementConditionDto();  private int dialogType = ZcSettingConstants.FOREIGNENTITY_BASE;    Dimension dialogSize=null;  /**   * @return the nd   */  public int getNd() {    return nd;  }  /**   * @param nd the nd to set   */  public void setNd(int nd) {    this.nd = nd;  }  /**   * @return the dataRuleId   */  public String getSqlMapSelectedId() {    return sqlMapSelectedId;  }  /**   * @param dataRuleId the dataRuleId to set   */  public void setSqlMapSelectedId(String sqlMapSelectedId) {    this.sqlMapSelectedId = sqlMapSelectedId;  }  public ForeignEntityField(String sqlMapSelectedId, int col, IForeignEntityHandler handler, String[] columNames, String title) {    super(col);    this.title = title;    this.sqlMapSelectedId = sqlMapSelectedId;    this.handler = handler;    this.setColumNames(columNames);    this.init();  }  public ForeignEntityField(String sqlMapSelectedId, ElementConditionDto elementConditionDto, int col, IForeignEntityHandler handler,    String[] columNames, String title) {    super(col);    this.title = title;    this.sqlMapSelectedId = sqlMapSelectedId;    this.elementConditionDto = elementConditionDto;    this.handler = handler;    this.setColumNames(columNames);    this.init();  }  public ForeignEntityField(String sqlMapSelectedId, ElementConditionDto elementConditionDto, int col, IForeignEntityHandler handler,    String[] columNames, String title, String fieldName) {    super(col);    this.title = title;    this.sqlMapSelectedId = sqlMapSelectedId;    this.elementConditionDto = elementConditionDto;    this.handler = handler;    this.fieldName = fieldName;    this.setColumNames(columNames);    this.init();  }  public ForeignEntityField(String sqlMapSelectedId, ElementConditionDto elementConditionDto, int col, IForeignEntityHandler handler,    String[] columNames, String title, String fieldName,Dimension dialogSize) {    super(col);    this.title = title;    this.sqlMapSelectedId = sqlMapSelectedId;    this.elementConditionDto = elementConditionDto;    this.handler = handler;    this.fieldName = fieldName;    this.setColumNames(columNames);    this.dialogSize=dialogSize;        this.init();  }  public ForeignEntityField(int col, IForeignEntityHandler handler, String[] columNames, String title, String fieldName, List dataBufferList) {    super(col);    this.title = title;    this.handler = handler;    this.fieldName = fieldName;    this.dataBufferList = dataBufferList;    this.setColumNames(columNames);    this.init();  }  public ForeignEntityField(String sqlMapSelectedId, ElementConditionDto elementConditionDto, int col, IForeignEntityHandler handler,    String[] columNames, String dialogTitle, int foreignentityNewSupplier) {    super(col);    this.title = title;    this.sqlMapSelectedId = sqlMapSelectedId;    this.elementConditionDto = elementConditionDto;    this.handler = handler;    this.dialogType = foreignentityNewSupplier;    this.setColumNames(columNames);    this.init();  }  private void init() {    if (this.sqlMapSelectedId == null && dataBufferList != null) {      selectDialog = new ForeignEntityDialog(owner, true, this, this.handler, this.title, fieldName, dataBufferList);    } else {      if (fieldName == null || "".equals(fieldName)) {        if (this.dialogType != ZcSettingConstants.FOREIGNENTITY_BASE) {          selectDialog = new ForeignEntityDialog(owner, true, this, this.handler, this.sqlMapSelectedId, this.elementConditionDto, this.title,            this.dialogType);        } else {          selectDialog = new ForeignEntityDialog(owner, true, this, this.handler, this.sqlMapSelectedId, this.elementConditionDto, this.title);        }      } else {        selectDialog = new ForeignEntityDialog(owner, true, this, this.handler, this.sqlMapSelectedId, this.elementConditionDto, this.title,          this.fieldName);      }    }    if(dialogSize!=null){      selectDialog.setSize(dialogSize);      selectDialog.moveToScreenCenter();    }  }  /* (non-Javadoc)   * @see com.ufgov.gk.client.component.JButtonTextField#handleClick(com.ufgov.gk.client.component.JButtonTextField)   */  @Override  public void handleClick(JButtonTextField jButtonTextField) {    Boolean isShow = true;    await();    try {      // 反射调用beforeSelect方法      isShow = (Boolean) MethodUtils.invokeMethod(this.handler, "beforeSelect", new Object[] { this.elementConditionDto });    } catch (Exception ex) {    }    if (isShow) {      if (selectDialog instanceof ForeignEntityDialog) {        // 加载数据        ((ForeignEntityDialog) selectDialog).initDataBufferList();      }      if(dialogSize!=null){        selectDialog.setSize(dialogSize);      }      selectDialog.setVisible(true);    }  }  public static void main(String[] args) {    SwingUtilities.invokeLater(new Runnable() {      public void run() {        try {          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());          UIManager.setLookAndFeel(new BlueLookAndFeel());        } catch (Exception e) {          e.printStackTrace();        }        //        UIManager.getDefaults().put("SplitPaneUI", BigButtonSplitPaneUI.class.getName());        String columNames[] = { "供应商代码", "供应商名称", "联系人", "电话", "审核人", "地址", "邮编", "审核日期", "状态" };        ForeignEntityField textField = new ForeignEntityField("ZcEbSignup.getZcEbSupplier", 20, new ZcEbSupplierForeignEntityHandler(), columNames,          "供应商选择");        JPanel panel = new JPanel();        panel.add(textField);        JFrame frame = new JFrame("foreignEntityField test Window");        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        frame.setSize(400, 200);        frame.setLocationRelativeTo(null);        frame.getContentPane().add(panel, BorderLayout.NORTH);        frame.setVisible(true);      }    });  }  public void setColumNames(String columNames[]) {    this.columNames = columNames;  }  public String[] getColumNames() {    return columNames;  }  public void updateDto(ElementConditionDto dto) {    // TCJLODO Auto-generated method stub    ((ForeignEntityDialog) (selectDialog)).updateDto(dto);    this.elementConditionDto = dto;  }  public IForeignEntityHandler getHandler() {    return handler;  }  public ElementConditionDto getElementConditionDto() {    return elementConditionDto;  }  public void setElementConditionDto(ElementConditionDto elementConditionDto) {    this.elementConditionDto = elementConditionDto;  }  public String getTitle() {    return title;  }  public void setTitle(String title) {    this.title = title;  }}