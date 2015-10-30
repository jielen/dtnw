package com.ufgov.zc.client.zc.bulletin;import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_CO_NAME;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Date;import java.util.HashMap;import java.util.List;import java.util.Map;import javax.swing.JOptionPane;import javax.swing.table.TableModel;import com.ufgov.zc.client.common.LangTransMeta;import com.ufgov.zc.client.common.ListCursor;import com.ufgov.zc.client.common.MyTableModel;import com.ufgov.zc.client.common.WorkEnv;import com.ufgov.zc.client.component.GkBaseDialog;import com.ufgov.zc.client.component.event.ValueChangeEvent;import com.ufgov.zc.client.component.event.ValueChangeListener;import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;import com.ufgov.zc.client.datacache.CompanyDataCache;import com.ufgov.zc.common.system.constants.ZcElementConstants;import com.ufgov.zc.common.system.constants.ZcSettingConstants;import com.ufgov.zc.common.zc.ZcEbBulletinConstants;import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;import com.ufgov.zc.common.zc.model.ZcEbBulletin;import com.ufgov.zc.common.zc.model.ZcEbPack;import com.ufgov.zc.common.zc.model.ZcEbPackPlan;import com.ufgov.zc.common.zc.model.ZcEbProj;public class ZcEbBulletinChgEditPanel extends AbstractZcEbBulletinEditPanel {  public ZcEbBulletinChgEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbBulletinChgListPanel listPanel) {    super(parent, listCursor, tabStatus, listPanel, ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_CHG);  }  @Override  protected String getCompId() {    // TODO Auto-generated method stub    return ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_CHG;  }  @Override  protected String getModelName() {    // TODO Auto-generated method stub    return ZcEbBulletinConstants.TITLE_ZC_EB_BULLETIN_CHG;  }  @Override  protected String getBulletinType() {    // TODO Auto-generated method stub    return ZcEbBulletinConstants.TYPE_BULLETIN_CHG;  }  @Override  public String getSqlMapSelectedProj() {    return "ZcEbProj.getZcEbProjForMoreBul";  }  @Override  public String getOpiWay() {    StringBuffer sb = new StringBuffer();    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_GKZB).append("',");    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_YQZB).append("',");//    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_XJ).append("',");    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_JZXTP).append("'");    return sb.toString();  }  @Override  public String getSqlMapSelectedMold() {    return "ZcEbBulletinWordMold.getZcEbBulletinWordMoldChg";  }  @Override  public List<AbstractFieldEditor> createFieldEditors() {    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();    createMakeCode();    fieldZcMakeCode.addValueChangeListener(new ValueChangeListener() {      @Override      public void valueChanged(ValueChangeEvent e) {        projCodeChange();      }    });    TextFieldEditor projCode = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME), "zcEbProj.projCode");    TextFieldEditor projName = new TextFieldEditor("项目名称", "zcEbProj.projName");    DateFieldEditor openBidTime = new DateFieldEditor("开标时间", "packPlan.openBidTime", DateFieldEditor.TimeTypeH24);    projCode.setEnabled(false);    projName.setEnabled(false);    openBidTime.setEnabled(false);//    editorList.add(projCode);    editorList.add(projName);//    editorList.add(fieldZcMakeCode);//    editorList.add(fieldZcMakeName);//    editorList.add(openBidTime);    AsValFieldEditor fieldBulletinStatus = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_BULLETIN_STATUS),    "bulletinStatus", "ZC_EB_BULLETIN_STATUS");    editorList.add(fieldBulletinStatus);    editorList.add(fieldMoldName);    TextFieldEditor fieldInputorName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_INPUTOR_NAME), "executor");    editorList.add(fieldInputorName);    if (WorkEnv.getInstance().containRole(ZcSettingConstants.ROLE_GYS_NORMAL)) {      fieldBulletinStatus.setVisible(false);      fieldMoldName.setVisible(false);      fieldInputorName.setVisible(false);    }    CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_CO_NAME), "coCode");    zcCoCode.setEnabled(false);//    editorList.add(zcCoCode);    Integer[] allowMinutes = { 0, 10, 20, 30, 40, 50 };    DateFieldEditor failureDate = new DateFieldEditor("公告发布时间", "failureDate", DateFieldEditor.TimeTypeH24, allowMinutes, true);//    editorList.add(failureDate);    DateFieldEditor bullPublishDate = new DateFieldEditor("首发公告时间", "bullPublishDate", DateFieldEditor.TimeTypeH24, allowMinutes, true);    bullPublishDate.setEnabled(false);//    editorList.add(bullPublishDate);    return editorList;  }  @Override  protected boolean checkBeforeSave(boolean isSend) {    if (super.checkBeforeSave(isSend)) {      ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();      if (bulletin.getFailureDate() == null || bulletin.getFailureDate().before(new Date())) {        JOptionPane.showMessageDialog(this.parent, "请正确填写[公告发布时间]", "提示", JOptionPane.WARNING_MESSAGE);        return false;      }      return true;    }    return false;  }  @Override  public void doReplaceBookMarks() {    if (!checkBeforeSave(false)) {      return;    }    if (replaceValue == null || replaceValue.equals("")) {      return;    }    ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");    String midStr = replaceValue.replaceAll("\\$", "#");    //公告发布时间    String reg = "ZB_PUBLISH_TIME#####[^@]*@@@@@";    String rep = "ZB_PUBLISH_TIME#####" + sdf.format(bulletin.getFailureDate()) + "@@@@@";    midStr = midStr.replaceAll(reg, rep);    replaceValue = midStr.replaceAll("#", "\\$");    super.doReplaceBookMarks();  }  @Override  protected void createMakeCode() {    String[] columNames = { "项目编码", "项目名称", "分包编码", "分包名称", "预算单位" };    PackHandler handler = new PackHandler(columNames);    fieldZcMakeCode = new ForeignEntityFieldEditor(getSqlMapSelectedProj(), getFindProjConditions(), 20,    handler, columNames, LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_CODE), "packName");  }  @Override  public String fetchSn(ZcEbBulletin sheet) {    //变更公告projCode存放的是分包的编号    ZcEbPack pack = (ZcEbPack) zcEbBaseServiceDelegate.queryObject("ZcEbProj.getZcEbPackForPrintByPackCode", sheet.getProjCode(), requestMeta);    return pack.getEntrustCode();  }  private class PackHandler implements IForeignEntityHandler {    private final String[] columNames;    PackHandler(String[] columNames) {      this.columNames = columNames;    }    @Override    public void excute(List selectedDatas) {      if (selectedDatas != null && selectedDatas.size() > 0) {        ZcEbPack pack = (ZcEbPack) selectedDatas.get(0);        ZcEbBulletin bulletin = (ZcEbBulletin) listCursor.getCurrentObject();        ZcEbProj proj = (ZcEbProj) zcEbBaseServiceDelegate.queryObject("ZcEbProj.getOriginalZcEbProjByProjCode", pack.getProjCode(), requestMeta);        Map map = new HashMap();        map.put("projCode", pack.getProjCode());        map.put("packCode", pack.getPackCode());        ZcEbPackPlan zcEbPackPlan = (ZcEbPackPlan) zcEbBaseServiceDelegate.queryObject("ZcEbPlan.getZcEbPackPlanByMap", map, requestMeta);        bulletin.setPackPlan(zcEbPackPlan);        bulletin.setZcEbProj(proj);        bulletin.setProjCode(pack.getPackCode());        bulletin.setProjectCode(pack.getProjCode());        bulletin.setPackName(pack.getPackName());        bulletin.setProjName(pack.getPackDesc());        bulletin.setCoCode(pack.getCoCode());        //        findWordMoldCondition.setType(proj.getPurType());        fieldMoldName.setEditObject(bulletin);        if (bulletin.getBulletinID() == null) {          handlera.flashReplaceValue();        } else {          bulletin.setMoldName("");          bulletin.setFileID(null);          refreshWordPane(bulletin);          refreshZbFile();        }        setEditingObject(bulletin);      }      setFieldMoldNameStatus();      setLoadMoldButtonStatus();    }    @Override    public boolean isMultipleSelect() {      // TODO Auto-generated method stub      return false;    }    @Override    public TableModel createTableModel(List showDatas) {      Object data[][] = new Object[showDatas.size()][columNames.length];      for (int i = 0; i < showDatas.size(); i++) {        ZcEbPack pack = (ZcEbPack) showDatas.get(i);        int col = 0;        data[i][col++] = pack.getProjCode();        data[i][col++] = pack.getPurContent();        data[i][col++] = pack.getPackName();        data[i][col++] = pack.getPackDesc();        data[i][col++] = CompanyDataCache.getName(pack.getCoCode());      }      MyTableModel model = new MyTableModel(data, columNames) {        @Override        public boolean isCellEditable(int row, int colum) {          return false;        }      };      return model;    }  }  @Override  protected String fieldName() {    return ZcElementConstants.FIELD_TRANS_PACK_NAME;  }}