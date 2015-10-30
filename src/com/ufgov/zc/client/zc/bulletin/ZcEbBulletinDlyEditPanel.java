package com.ufgov.zc.client.zc.bulletin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.converter.zc.ZcEbBulletinToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.model.ZcEbBulletinPack;
import com.ufgov.zc.common.zc.model.ZcEbPack;

public class ZcEbBulletinDlyEditPanel extends AbstractZcEbBulletinEditPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1579905355384351558L;

  public ZcEbBulletinDlyEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbBulletinDlyListPanel listPanel) {

    super(parent, listCursor, tabStatus, listPanel, ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_DLY);

  }

  @Override
  public String fetchSn(ZcEbBulletin sheet) {
    List<ZcEbPack> pack = zcEbBaseServiceDelegate.queryDataForList("ZcEbProj.getZcEbPackListByProjCode", sheet.getProjCode(), requestMeta);
    return pack.get(0).getEntrustCode();
  }

  @Override
  protected String getModelName() {
    // TODO Auto-generated method stub
    return ZcEbBulletinConstants.TITLE_ZC_EB_BULLETIN_DLY;
  }

  @Override
  protected String getCompId() {
    // TODO Auto-generated method stub
    return ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_DLY;
  }

  @Override
  protected String getBulletinType() {
    // TODO Auto-generated method stub
    return ZcEbBulletinConstants.TYPE_BULLETIN_DLY;
  }

  @Override
  public String getOpiWay() {
    StringBuffer sb = new StringBuffer();

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_GKZB).append("',");

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_YQZB).append("',");

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_XJ).append("',");

//    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_XJ_JJ).append("',");

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_JZXTP).append("'");

    return sb.toString();
  }

  @Override
  public String getSqlMapSelectedProj() {

    return "ZcEbProj.getZcEbProjForDlyBul";

  }

  @Override
  public String getSqlMapSelectedMold() {

    return "ZcEbBulletinWordMold.getZcEbBulletinWordMoldJLCGZX";

  }

  @Override
  protected void setFieldMoldNameStatus() {
    super.setFieldMoldNameStatus();
    this.findWordMoldCondition.setType("D");
  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> fields = super.createFieldEditors();

    Integer[] allowMinutes = { 0, 10, 20, 30, 40, 50 };

    DateFieldEditor failureDate = new DateFieldEditor("确认参加投标截止时间", "failureDate", DateFieldEditor.TimeTypeH24, allowMinutes, true);

    fields.add(failureDate);

    DateFieldEditor openBidTime = new DateFieldEditor("开标时间", "openBidTime", DateFieldEditor.TimeTypeH24, allowMinutes, true);
    fields.add(openBidTime);
    return fields;
  }

  @Override
  protected boolean checkBeforeSave(boolean isSend) {
    if (super.checkBeforeSave(isSend)) {
      ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();
      if (bulletin.getFailureDate() == null || bulletin.getFailureDate().before(new Date())) {

        JOptionPane.showMessageDialog(this.parent, "请正确填写[确认参加投标截止时间]", "提示", JOptionPane.WARNING_MESSAGE);

        return false;
      }
      if (bulletin.getOpenBidTime() == null || bulletin.getOpenBidTime().before(new Date())) {

        JOptionPane.showMessageDialog(this.parent, "请正确填写[投标截止时间]", "提示", JOptionPane.WARNING_MESSAGE);

        return false;
      }

      StringBuffer sb = new StringBuffer();
      if (bulletin.getPackList() == null || bulletin.getPackList().size() == 0) {
        sb.append("请填写延期的分包");
      } else {
        for (int i = 0; i < bulletin.getPackList().size(); i++) {
          ZcEbBulletinPack pk = (ZcEbBulletinPack) bulletin.getPackList().get(i);
          if (pk.getPackCode() == null || "".equals(pk.getPackCode()) || pk.getPack() == null || pk.getPack().getPackName() == null
            || "".equals(pk.getPack().getPackName())) {
            sb.append("延期分包第").append(i + 1).append("行分包编号不能为空");
          }
        }
      }
      if (sb.length() > 0) {

        JOptionPane.showMessageDialog(this.parent, sb.toString(), "提示", JOptionPane.WARNING_MESSAGE);

        return false;

      }

      return true;
    }
    return false;
  }

  @Override
  public void addSubPane() {
    super.addSubPane();
    createSubInfo();

    tabPane.addTab("延期分包", detailTablePanel);
  }

  private void createSubInfo() {

    detailTablePanel.init();

    detailTablePanel.getSearchBar().setVisible(false);

    detailTablePanel.setTablePreferencesKey(this.getClass().getName() + "_packTable");

    detailTablePanel.getTable().setShowCheckedColumn(true);

    detailTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    bottomToolBar1 = new JFuncToolBar();

    FuncButton addBtn1 = new SubaddButton(false);

    FuncButton insertBtn1 = new SubinsertButton(false);

    FuncButton delBtn1 = new SubdelButton(false);

    bottomToolBar1.add(addBtn1);

    bottomToolBar1.add(insertBtn1);

    bottomToolBar1.add(delBtn1);

    detailTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);

    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcEbBulletinPack detail = new ZcEbBulletinPack();
        detail.setPack(new ZcEbPack());

        detail.setTempId(Guid.genID());

        int rowNum = addSub(detailTablePanel, detail);

        detailTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcEbBulletinPack detail = new ZcEbBulletinPack();
        detail.setPack(new ZcEbPack());

        detail.setTempId(Guid.genID());

        int rowNum = insertSub(detailTablePanel, detail);

        detailTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteSub(detailTablePanel);

      }

    });
  }

  @Override
  protected void refreshSubTableData(String fileID) {
    super.refreshSubTableData(fileID);

    ZcEbBulletin bulletin = (ZcEbBulletin) listCursor.getCurrentObject();
    refreshSubData(bulletin.getPackList());
  }

  protected void refreshSubData(List list) {
    detailTablePanel.setTableModel(ZcEbBulletinToTableModelConverter.convertSubPackTableData(list));
    ZcUtil.translateColName(detailTablePanel.getTable(), ZcEbBulletinToTableModelConverter.getPackDetailInfo());
    setOldObject();
    setTableEditor(detailTablePanel.getTable());
    detailTablePanel.repaint();
  }

  private void setTableEditor(JTable table) {

    String columNames[] = { LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_CODE),
      LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_NAME) };

    ZcEbPackHandler handler = new ZcEbPackHandler(columNames);

    ElementConditionDto packDto = new ElementConditionDto();
    ForeignEntityFieldCellEditor packEditor = new ForeignEntityFieldCellEditor("ZcEbProj.getQuestionZcEbPack", packDto, 20, handler, columNames,
      LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PAKE_NAME), "packName");
    SwingUtil.setTableCellEditor(table, "PACK_NAME", packEditor);

  }

  @Override
  public void doReplaceBookMarks() {
    if (!checkBeforeSave(false)) {
      return;
    }
    if (replaceValue == null || replaceValue.equals("")) {
      return;
    }
    ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
    String midStr = replaceValue.replaceAll("\\$", "#");
    //开标时间
    String reg = "RECE_BID_TIME#####[^@]*@@@@@";
    String rep = "RECE_BID_TIME#####" + " " + "@@@@@";
    midStr = midStr.replaceAll(reg, rep);

    reg = "OPEN_BID_TIME#####[^@]*@@@@@";
    rep = "OPEN_BID_TIME#####" + sdf.format(bulletin.getOpenBidTime()) + "@@@@@";
    midStr = midStr.replaceAll(reg, rep);

    reg = "YQGG_END_TIME#####[^@]*@@@@@";
    rep = "YQGG_END_TIME#####" + sdf.format(bulletin.getFailureDate()) + "@@@@@";
    midStr = midStr.replaceAll(reg, rep);

    //确认参加投标截止时间
    sdf.applyPattern("yyyy年MM月dd日 HH");
    reg = "SELL_END_TIME#####[^@]*@@@@@";
    rep = "SELL_END_TIME#####" + sdf.format(bulletin.getFailureDate()) + "@@@@@";
    midStr = midStr.replaceAll(reg, rep);

    replaceValue = midStr.replaceAll("#", "\\$");
    super.doReplaceBookMarks();
  }

  /**

   * 标段外部部件选择处理类

   */

  private class ZcEbPackHandler implements IForeignEntityHandler {

    private final String columNames[];

    public ZcEbPackHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public boolean beforeSelect(ElementConditionDto dto) {
      ZcEbBulletin tin = (ZcEbBulletin) listCursor.getCurrentObject();
      if(tin.getProjCode() == null || tin.getProjCode().trim().length() == 0){
        JOptionPane.showMessageDialog(self, "请选择项目", "提示", JOptionPane.INFORMATION_MESSAGE);
        return false;
      }
      dto.setDattr5("1");
      dto.setProjCode(tin.getProjCode());
      dto.setZcText0("4");

      if (tin.getPackList() == null) {
        dto.setPackCode("");
        return true;
      }
      StringBuffer sb = new StringBuffer("' '");
      for (int i = 0; i < tin.getPackList().size(); i++) {
        sb.append(",'").append(((ZcEbBulletinPack) tin.getPackList().get(i)).getPackCode()).append("'");
      }
      dto.setPackCode(sb.toString());
      return true;
    }

    @SuppressWarnings("unchecked")
    public void excute(List selectedDatas) {
      ZcEbBulletin bean = (ZcEbBulletin) listCursor.getCurrentObject();

      JTable table = detailTablePanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return;

      int k2 = table.convertRowIndexToModel(k);

      ZcEbBulletinPack pack = (ZcEbBulletinPack) (bean.getPackList().get(k2));

      if (selectedDatas.size() > 0) {

        ZcEbPack zcPack = (ZcEbPack) selectedDatas.get(0);

        pack.setPackCode(zcPack.getPackCode());
        pack.setPack(zcPack);

      }
      listCursor.setCurrentObject(bean);

      refreshSubData(bean.getPackList());

    }

    @SuppressWarnings({ "unchecked", "serial" })
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcEbPack rowData = (ZcEbPack) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getPackName();

        data[i][col++] = rowData.getPackDesc();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        @Override
        public boolean isCellEditable(int row, int colum) {

          return false;

        }

      };

      return model;

    }

    public boolean isMultipleSelect() {

      return false;

    }

  }

  @Override
  protected void selectProj(List selectedDatas) {
    super.selectProj(selectedDatas);
    ZcEbBulletin bulletin = (ZcEbBulletin) listCursor.getCurrentObject();
    bulletin.getPackList().clear();
    listCursor.setCurrentObject(bulletin);
    this.refreshSubData(bulletin.getPackList());
  }
}
