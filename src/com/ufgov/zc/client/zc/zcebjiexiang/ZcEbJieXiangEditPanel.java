/**
 * 
 */
package com.ufgov.zc.client.zc.zcebjiexiang;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_PITEM_ARR_DATE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.ZcEbAuditSheetToTableModelConverter;
import com.ufgov.zc.client.common.converter.ZcEbEntrustToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.AuditFinalPassButton;
import com.ufgov.zc.client.component.button.CallbackButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.HelpButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.OpenNotepadButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.SendToFuZhuRenButton;
import com.ufgov.zc.client.component.button.SuggestAuditPassButton;
import com.ufgov.zc.client.component.button.SuggestAuditPassFGZRButton;
import com.ufgov.zc.client.component.button.TraceButton;
import com.ufgov.zc.client.component.button.UnauditButton;
import com.ufgov.zc.client.component.button.UntreadButton;
import com.ufgov.zc.client.component.button.ZcCancelcaigouButton;
import com.ufgov.zc.client.component.button.ZcFinishCaiGouButton;
import com.ufgov.zc.client.component.button.ZcUnFinishCaiGouButton;
import com.ufgov.zc.client.component.event.ValueChangeEvent;
import com.ufgov.zc.client.component.event.ValueChangeListener;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.celleditor.IntCellEditor;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.celleditor.zc.ZcBCatalogueCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsMapCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.ZcEbEntrustHandler;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.EntrustFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityDialog;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextAreaFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.auditsheet.SuggestAuditPassZHZButton;
import com.ufgov.zc.client.zc.notepad.ZcNotepadDialog;
import com.ufgov.zc.common.commonbiz.model.WfAware;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.model.AsOption;
import com.ufgov.zc.common.system.model.AsVal;
import com.ufgov.zc.common.system.model.User;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.system.util.Utils;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcEbAuditSheet;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.model.ZcEbJieXiang;
import com.ufgov.zc.common.zc.model.ZcEbPlan;
import com.ufgov.zc.common.zc.publish.IZcEbAuditSheetServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbJieXiangServiceDelegate;

/**
 * @author Administrator
 *
 */
public class ZcEbJieXiangEditPanel  extends AbstractMainSubEditPanel {

  private static final long serialVersionUID = 1184024574184315092L;

  private static final Logger logger = Logger.getLogger(ZcEbJieXiangEditPanel.class);

  protected IZcEbAuditSheetServiceDelegate zcEbAuditSheetServiceDelegate;

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();
//项目经办人
  protected ForeignEntityFieldEditor xmjbrEditor;

  protected ForeignEntityFieldEditor attnEditor;

//  private String compoId = "ZC_EB_JIE_XIANG";

  private FuncButton addButton = new AddButton();

  private FuncButton saveButton = new SaveButton();

  // 工作流送审
  private FuncButton sendButton = new SendButton();

  // 工作流收回
  private FuncButton callbackButton = new CallbackButton();

  // 工作流填写意见审核通过
  protected FuncButton suggestPassButton = new SuggestAuditPassButton();

  // 工作流填写意见审核通过(综合组)

  private FuncButton suggestAuditPassZHZButton = new SuggestAuditPassZHZButton();

  // 分管主任审核按钮

  private FuncButton suggestAuditPassFGZRButton = new SuggestAuditPassFGZRButton();

  //综合组组送副主任按钮

  private FuncButton sendToFuZhuRenButton = new SendToFuZhuRenButton();

  //消审

  private FuncButton unAuditButton = new UnauditButton();

  // 工作流退回
  private FuncButton unTreadButton = new UntreadButton();

  // 工作流流程跟踪
  private FuncButton traceButton = new TraceButton();

  //工作流终审
  private FuncButton auditFinalPassButton = new AuditFinalPassButton();

  private FuncButton deleteButton = new DeleteButton();

  private FuncButton previousButton = new PreviousButton();

  private FuncButton nextButton = new NextButton();

  private FuncButton exitButton = new ExitButton();

  private FuncButton helpButton = new HelpButton();

  public FuncButton openNotepadButton = new OpenNotepadButton();
  
  private ZcCancelcaigouButton cancelCaiGouButton=new ZcCancelcaigouButton();
  
  private ZcUnFinishCaiGouButton unFinishCaigouButton= new ZcUnFinishCaiGouButton();
  
  private ZcFinishCaiGouButton finishCaiGouButton=new ZcFinishCaiGouButton();

  protected ElementConditionDto orgDto = new ElementConditionDto();
  
  

  @SuppressWarnings("unchecked")
  final ListCursor listCursor;

   ZcEbJieXiang auditSheet;

   ZcEbJieXiang oldAuditSheet;

   ZcEbJieXiangListPanel listPanel;

   JTablePanel tablePanel = new JTablePanel();

  private ZcEbJieXiangEditPanel self = this;

  private GkBaseDialog parent;

  private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(getCompoId());

  private ForeignEntityDialog forenEntityDialog;

  private JTabbedPane tabPane;

  JTablePanel asdJTabelPanel = new JTablePanel("auditSheetDetailsPanel");

  private JFuncToolBar subPackTableToolbar;

  protected TextFieldEditor makeNameEditor;

  private boolean haveInitFlag = false;

  protected ForeignEntityFieldEditor fzrEditor;

  protected ForeignEntityFieldEditor superintendentOrg;

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  private static IZcEbBaseServiceDelegate zcBaseDataServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(

  IZcEbBaseServiceDelegate.class, "zcEbBaseServiceDelegate");

  IZcEbBaseServiceDelegate baseDataServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  private IZcEbJieXiangServiceDelegate zcEbJieXiangServiceDelegate = (IZcEbJieXiangServiceDelegate) ServiceFactory.create(

    IZcEbJieXiangServiceDelegate.class, "zcEbJieXiangServiceDelegate");
  //资金构成页签

  JTabbedPane biTabPane = null;

  private JTablePanel biTablePanel = new JTablePanel();

  public JSaveableSplitPane splitPane;

  private boolean isEdit;

  @SuppressWarnings("unchecked")
  public ZcEbJieXiangEditPanel(GkBaseDialog dialog, ListCursor listCursor, String tabStatus, ZcEbJieXiangListPanel listPanel) {

    super(new ZcEbAuditSheet(), listPanel.compoId);
//    this.compoId = listPanel.compoId;

    this.listCursor = listCursor;

    this.listPanel = listPanel;

    this.zcEbAuditSheetServiceDelegate = listPanel.getZcEbAuditSheetServiceDelegate();

    this.parent = dialog;

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), this.listPanel.getTitle(), TitledBorder.CENTER,

    TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.colCount = 3;

    init();

    requestMeta.setCompoId(getCompoId());

    refreshData();

    this.haveInitFlag = true;

  }

  @SuppressWarnings("unchecked")
  public ZcEbJieXiangEditPanel(GkBaseDialog dialog, ListCursor listCursor, String tabStatus, ZcEbJieXiangListPanel listPanel,

  ForeignEntityDialog forenEntityDialog) {

    this(dialog, listCursor, tabStatus, listPanel);

    this.forenEntityDialog = forenEntityDialog;

  }

  @SuppressWarnings("unchecked")
  private void refreshData() {
    
    this.auditSheet = (ZcEbJieXiang) this.listCursor.getCurrentObject();

    ElementConditionDto fzrDto = new ElementConditionDto();

    //    fzrDto.setCoCode(((AsOption) listPanel.getBaseDataServiceDelegate().getAsOption(

    //      ZcElementConstants.OPT_ZC_CGZX_CODE, requestMeta)).getOptVal());

    //    fzrDto.setNd(requestMeta.getSvNd());

    if (auditSheet == null) {

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

      auditSheet = new ZcEbJieXiang();

      auditSheet.setStatus("0");

      // 是否主任审核

      //auditSheet.setZcIsZrsh("N");

      auditSheet.setZcEbEntrust(new ZcEbEntrust());

      auditSheet.setEntrustDetailList(new ArrayList());

      setDefualtValue(auditSheet, ZcSettingConstants.PAGE_STATUS_NEW);

      List list = new ArrayList();

      list.add(auditSheet);

      this.listCursor.setDataList(list, -1);

      listCursor.setCurrentObject(auditSheet);

    } else {

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      Map<String, String> para = new HashMap<String, String>();

      para.put("SN", auditSheet.getSn());

      auditSheet = (ZcEbJieXiang) zcEbJieXiangServiceDelegate.readJieXiang(para, requestMeta);

      listCursor.setCurrentObject(auditSheet);

      Map<String, String> m = new HashMap<String, String>();

      m.put("SN", auditSheet.getSn());

      List edList = zcEbAuditSheetServiceDelegate.getEntrustDetailList(m, requestMeta);

      auditSheet.setEntrustDetailList(edList);

      auditSheet.setDbDigest(auditSheet.getDbDigest());

    }
    setEditingObject(auditSheet);

    if (auditSheet.getZcFzrUserId() == null || "".equals(auditSheet.getZcFzrUserId())) {
      //设置默认中心副主任
      String optVal = AsOptionMeta.getOptVal("OPT_ZC_CG_CGZX_FZR");
      if (optVal != null) {
        String[] fzr = optVal.split(",");
        auditSheet.setZcFzrUserId(fzr[0]);
        auditSheet.setZcFzrUserName(fzr[1]);
        fzrEditor.setEditObject(auditSheet);//zcFzrUserName
      }
    }
    refreshSubTableData(auditSheet);
    resetSubTable();

    //根据工作流确定 按钮是否显示

//    setButtonStatus(auditSheet, requestMeta, this.listCursor);

    Long processInstId = auditSheet.getProcessInstId();


//    if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equals(auditSheet.getStatus())) {
//      setCancelStatus(this.listCursor);
//    } else {
//      setCallBackButtonVisible(auditSheet);
//    }
    setOldObject();
    
    
//    updateWFEditorEditable(auditSheet, requestMeta);


    this.fitTable();

    BeanTableModel tablemodel = (BeanTableModel) asdJTabelPanel.getTable().getModel();

    tablemodel.setEditable(false);

    BeanTableModel tablemode2 = (BeanTableModel) biTablePanel.getTable().getModel();

    tablemode2.setEditable(false);

    asdJTabelPanel.repaint();
    
    setButtonStatus();

  }
  private void setButtonStatus() {

    if (this.btnStatusList.size() == 0) {

      ButtonStatus bs = new ButtonStatus();

      bs.setButton(this.finishCaiGouButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcEbJieXiang.STATUS_EXEC);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.unFinishCaigouButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcEbJieXiang.STATUS_COMPLETED);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.cancelCaiGouButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcEbJieXiang.STATUS_EXEC);

      btnStatusList.add(bs);

    }

    ZcEbJieXiang obj = (ZcEbJieXiang) (this.listCursor.getCurrentObject());

    ZcUtil.setButtonEnable(this.btnStatusList, obj.getStatus(), this.pageStatus,  getCompoId(), obj.getProcessInstId());

  }

void showFunc(int index){
  int t=0;
  for(int i=0;i<toolBar.getComponentCount();i++){
    if(toolBar.getComponent(i).isVisible()){
      t++;
    }
  }
//  System.out.println("================================================================================================="+index+"===="+t);
}

  public void updateWFEditorEditable(WfAware baseBill, RequestMeta requestMeta) {

    Long processInstId = baseBill.getProcessInstId();

    isEdit = false;

    if (processInstId != null && processInstId.longValue() > 0) {

      // 工作流的单据

            wfCanEditFieldMap = BillElementMeta.getWfCanEditField(baseBill, requestMeta);

      if ("cancel".equals(this.oldAuditSheet.getStatus())) {// 撤销单据设置字段为不可编辑

        wfCanEditFieldMap = null;

      }

      for (AbstractFieldEditor editor : fieldEditors) {

        // 工作流中定义可编辑的字段

        if (wfCanEditFieldMap != null && wfCanEditFieldMap.containsKey(Utils.getDBColNameByFieldName(editor.getEditObject(), editor.getFieldName()))) {

          isEdit = true;

          editor.setEnabled(true);

        } else {

          editor.setEnabled(false);

        }

      }

      // 子表的设置

      updateWFSubTableEditable();

    }

  }

  private void setCallBackButtonVisible(WfAware baseBill) {

    Long processInstId = baseBill.getProcessInstId();

    if (processInstId != null && processInstId.longValue() > 0) {

      List enableFuncs = this.getWFNodeEnableFunc(baseBill, requestMeta);

      if (enableFuncs.contains("fnewcommit") || enableFuncs.contains("fautocommit") || enableFuncs.contains("fmanualcommit")

      || enableFuncs.contains("fmanualcommitZHZ") || enableFuncs.contains("fmanualcommitFGZR") || enableFuncs.contains("fmanualcommitZZ")

      || enableFuncs.contains("fsendtofuzhuren") || enableFuncs.contains("fauditfinal")) {

        // 如果【送审按钮】、【手动提交】、【自动提交】存在，就隐藏【收回按钮】,送副主任，办结
        callbackButton.setVisible(false);

      }

    }

  }

  @Override
  public JTablePanel[] getSubTables() {

    return new JTablePanel[] { asdJTabelPanel, biTablePanel };

  }

  @SuppressWarnings("unchecked")
  private void refreshSubTableData(ZcEbJieXiang auditSheet) {

    asdJTabelPanel.setTableModel(ZcEbAuditSheetToTableModelConverter.convertZcEbAuditSheetDetailToTableMode(auditSheet.getEntrustDetailList()));

    List list = new ArrayList();
    if (auditSheet.getZcEbEntrust().getZcMakeCode() != null && !auditSheet.getZcEbEntrust().getZcMakeCode().equals("自动生成")) {
      ElementConditionDto dto = new ElementConditionDto();
      dto.setZcText0(auditSheet.getZcEbEntrust().getZcMakeCode());
      list = zcBaseDataServiceDelegate.getForeignEntitySelectedData("ZC_P_PRO_MITEM_BI.getHtBiDetail", dto, requestMeta);
    }

    biTablePanel.setTableModel(ZcEbEntrustToTableModelConverter.convertSubBiTableData(list));

  }

  public void resetSubTable() {

    translateSubTableColumn();
    setSubTableCellEditor(asdJTabelPanel.getTable());
    setTableBiEditor(biTablePanel.getTable());

  }

  void translateSubTableColumn() {

    ZcUtil.translateColName(asdJTabelPanel.getTable(), ZcEbEntrustToTableModelConverter.getItemInfo());

    ZcUtil.translateColName(this.biTablePanel.getTable(), ZcEbEntrustToTableModelConverter.biInfo);

  }

  //开标经办人
  private void setSubXbTableCellEditor(JTable table) {

    ZcEbJieXiang sheet = (ZcEbJieXiang) listCursor.getCurrentObject();

    ElementConditionDto dto = new ElementConditionDto();

    Map parameter = new HashMap();

    // 设置采购中心单位代码

    dto.setEmpCode(requestMeta.getEmpCode());

    dto.setNd(requestMeta.getSvNd());

    String KZ = AsOptionMeta.getOptVal("OPT_ZC_EB_SHEET_KZ");

    parameter.put("empCode", requestMeta.getEmpCode());

    parameter.put("orgCode", sheet.getSuperintendentOrg());

    parameter.put("roleId", KZ);

    List list = baseDataServiceDelegate.queryDataForList("ZcEbAuditSheet.getSuperintendentXBName", parameter, requestMeta);

    table.setDefaultEditor(String.class, new TextCellEditor());

    table.setDefaultEditor(String.class, new TextCellEditor());

    SwingUtil.setTableCellEditor(table, "XZ_OPERATOR_NAME", new AsValComboBoxCellEditor(false, list));

    SwingUtil.setTableCellRenderer(table, "XZ_OPERATOR_NAME", new AsMapCellRenderer(getValMap(list)));

  }

  //将list转成map

  private Map getValMap(List list) {

    Map valMap = new HashMap();

    for (int i = 0; i < list.size(); i++) {

      AsVal asVal = (AsVal) list.get(i);

      valMap.put(asVal.getValId(), asVal.getVal());

    }

    return valMap;

  }

  private void setSubTableCellEditor(JTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());

    SwingUtil.setTableCellEditor(table, FIELD_TRANS_ZC_PITEM_ARR_DATE, new DateCellEditor());

    SwingUtil.setTableCellRenderer(table, FIELD_TRANS_ZC_PITEM_ARR_DATE, new DateCellRenderer());

    SwingUtil.setTableCellEditor(table, FIELD_TRANS_ZC_CATALOGUE_CODE, new ZcBCatalogueCellEditor());

    SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_ITEM_SUM", new MoneyCellEditor(false));

    SwingUtil.setTableCellRenderer(table, "ZC_FIELD_ZC_ITEM_SUM", new NumberCellRenderer());

    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_ZC_MER_PRICE, new MoneyCellEditor(false));

    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_ZC_MER_PRICE, new NumberCellRenderer());

    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_ZC_CAIG_NUM, new IntCellEditor(false));

    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_ZC_PITEM_ATTACH, new FileCellEditor("zcPitemAttachBlobid",

    (BeanTableModel) table.getModel()));

  }

  public void setTableBiEditor(JTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());

    SwingUtil.setTableCellEditor(table, "ZC_BI_JHUA_SUM", new MoneyCellEditor(false));

    SwingUtil.setTableCellRenderer(table, "ZC_BI_JHUA_SUM", new NumberCellRenderer());

    SwingUtil.setTableCellEditor(table, "ZC_BI_YJBA_SUM", new MoneyCellEditor(false));

    SwingUtil.setTableCellRenderer(table, "ZC_BI_YJBA_SUM", new NumberCellRenderer());

//    SwingUtil.setTableCellEditor(table, FIELD_TRANS_FUND_CODE, new AsValComboBoxCellEditor("ZC_VS_FUND_NAME"));

//    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_FUND_CODE, new AsValCellRenderer("ZC_VS_FUND_NAME"));

//    SwingUtil.setTableCellEditor(table, FIELD_TRANS_ORIGIN_CODE, new AsValComboBoxCellEditor("ZC_VS_ORIGIN_NAME"));

//    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_ORIGIN_CODE, new AsValCellRenderer("ZC_VS_ORIGIN_NAME"));

//    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE, new AsValComboBoxCellEditor("ZC_VS_PAYTYPE_NAME"));

//    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE, new AsValCellRenderer("ZC_VS_PAYTYPE_NAME"));

    //      FileCellEditor edit = new FileCellEditor("zcFundFileBlobid", (BeanTableModel) table.getModel());

    //      edit.setDownloadFileEnable(true);

    //      edit.setDeleteFileEnable(false);

    //      edit.setUploadFileEnable(false);

    SwingUtil.setTableCellEditor(table, "ZC_FUND_FILE", new FileCellEditor("zcFundFileBlobid", (BeanTableModel) table.getModel()));

  }

  private void setDefualtValue(ZcEbJieXiang auditSheet, String pageStatus) {

    if (pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {

      auditSheet.setInputorId(this.requestMeta.getSvUserID());

      auditSheet.setStatus(ZcSettingConstants.BILL_STATUS_NEW);

    }

  }

  private void setOldObject() {

    this.oldAuditSheet = (ZcEbJieXiang) ObjectUtil.deepCopy(listCursor.getCurrentObject());

  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {

    ZcEbJieXiang sheet = (ZcEbJieXiang) listCursor.getCurrentObject();

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    ZcEbEntrustHandler handler = new ZcEbEntrustHandler() {

      public void excute(List selectedDatas) {

        for (Object obj : selectedDatas) {

          ZcEbEntrust m = (ZcEbEntrust) obj;

          auditSheet = (ZcEbJieXiang) listCursor.getCurrentObject();

          auditSheet.setSn(m.getSn());

          auditSheet.getZcEbEntrust().setZcPifuCgfs(m.getZcPifuCgfs());

          auditSheet.getZcEbEntrust().setZcMakeCode(m.getZcMakeCode());

          auditSheet.getZcEbEntrust().setZcMakeName(m.getZcMakeName());

          auditSheet.getZcEbEntrust().setCoCode(m.getCoCode());

          auditSheet.getZcEbEntrust().setCoName(m.getCoName());

          auditSheet.getZcEbEntrust().setNd(m.getNd());

          auditSheet.getZcEbEntrust().setZcMakeLinkman(m.getZcMakeLinkman());

          auditSheet.getZcEbEntrust().setZcMakeTel(m.getZcMakeTel());

          auditSheet.getZcEbEntrust().setZcMoneyBiSum(m.getZcMoneyBiSum());

          auditSheet.getZcEbEntrust().setOrgCode(m.getOrgCode());

          auditSheet.getZcEbEntrust().setSnCode(m.getSnCode());

          getAllDetailListBySN();

          setEditingObject(auditSheet);

        }

      }

      public void afterClear() {

        auditSheet = (ZcEbJieXiang) listCursor.getCurrentObject();

        auditSheet.setSn(null);

        auditSheet.getZcEbEntrust().setZcPifuCgfs(null);

        auditSheet.getZcEbEntrust().setZcMakeCode(null);

        auditSheet.getZcEbEntrust().setZcMakeName(null);

        auditSheet.getZcEbEntrust().setCoCode(null);

        auditSheet.getZcEbEntrust().setCoName(null);

        auditSheet.getZcEbEntrust().setZcMakeLinkman(null);

        auditSheet.getZcEbEntrust().setZcMakeTel(null);

        auditSheet.getZcEbEntrust().setZcMoneyBiSum(null);

        auditSheet.getZcEbEntrust().setOrgCode(null);

        auditSheet.setEntrustDetailList(new ArrayList());

        asdJTabelPanel.setTableModel(ZcEbAuditSheetToTableModelConverter.convertZcEbAuditSheetDetailToTableMode(auditSheet.getEntrustDetailList()));

        translateSubTableColumn();

        setEditingObject(auditSheet);

      }

    };

    // 批办单编号
    /*

    TextFieldEditor sheetId = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SHEET_ID), "sheetId");

    sheetId.setEnabled(false);

    editorList.add(sheetId);

    */

    // 审批表编号
    /*

    TextFieldEditor makeCodeEditor = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_CODE), "zcEbEntrust.zcMakeCode");

    editorList.add(makeCodeEditor);

    */

    // 任务单编号
    ElementConditionDto entrustDto = new ElementConditionDto();

    entrustDto.setCoCode("exec");

    EntrustFieldEditor entrustFieldEditor = new EntrustFieldEditor(handler.getSqlId(), entrustDto, 20, handler, handler.getColumNames(), LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_TRANS_SN), "zcEbEntrust.zcMakeCode");

    entrustFieldEditor.setEnabled(false);
    entrustFieldEditor.setVisible(false);

    editorList.add(entrustFieldEditor);

    // 任务单编号
    TextFieldEditor snCodeEditor = new TextFieldEditor("采购计划编号", "sn");

    snCodeEditor.setEnabled(false);

    editorList.add(snCodeEditor);

    // 审批表项目名称
    makeNameEditor = new TextFieldEditor("采购内容", "zcEbEntrust.zcMakeName");

    makeNameEditor.setEnabled(false);

    editorList.add(makeNameEditor);

    // 状态

    AsValFieldEditor status = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_TRANS_SHEET_STATUS), "status",

    "ZC_VS_AUDIT_SHEET_STATUS");

    status.setEnabled(false);

    editorList.add(status);
    
    // 采购单位
    CompanyFieldEditor coCodeEditor = new CompanyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_NAME), "zcEbEntrust.coCode");
    coCodeEditor.setEnabled(false);
    editorList.add(coCodeEditor);
    
    // 采购预算
    MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MONEY_BI_SUM), "zcEbEntrust.zcMoneyBiSum");

    zcMoneyBiSum.setEnabled(false);

    editorList.add(zcMoneyBiSum);

    //    // 采购单位主管业务处室
    //    OrgFieldEditor zcZgCsCode = new OrgFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_ZG_CS_CODE), "zcEbEntrust.orgCode", true);
    //    zcZgCsCode.setEnabled(false);
    //    editorList.add(zcZgCsCode);

    AsValFieldEditor isPub = new AsValFieldEditor("是否公示", "zcEbEntrust.isPub", "ZC_VS_YN");
//    if (sheet.getZcEbEntrust().getZcPifuCgfs().equals(ZcSettingConstants.PITEM_OPIWAY_DYLY)) {
//      editorList.add(isPub);
//
//    }
    

    // 采购方式 ( 1公开招标,2邀请招标,3竞争性谈判,4单一来源采购,5询价,6其他)

    AsValFieldEditor zcPifuCgfs = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CGFS), "zcEbEntrust.zcPifuCgfs",

    "ZC_VS_PITEM_OPIWAY");

    zcPifuCgfs.setEnabled(false);    

    editorList.add(zcPifuCgfs);
    
    // 联系人
    TextFieldEditor zcMakeLinkman = new TextFieldEditor("采购单位联系人", "zcEbEntrust.zcMakeLinkman");

    zcMakeLinkman.setEnabled(false);

    editorList.add(zcMakeLinkman);

    // 联系电话
    TextFieldEditor zcMakeTel = new TextFieldEditor("采购单位联系电话", "zcEbEntrust.zcMakeTel");

    zcMakeTel.setEnabled(false);

    editorList.add(zcMakeTel);
    //2012-04-26 jixueyou
    //    if (isCaiGouZhongXin()) {

    // 副主任

    String columNamesFzr[] = { "主任名称" };

    FzrFnHandler manHandler = new FzrFnHandler(columNamesFzr);

    ElementConditionDto fzrDto = new ElementConditionDto();

    fzrDto.setZcText1(ZcElementConstants.FIELD_OPT_ZC_CGZX_ZR_ROLE_SHEET);

    fzrDto.setZcText2(ZcElementConstants.FIELD_OPT_ZC_CGZX_FZR_ROLE);

    fzrEditor = new ForeignEntityFieldEditor("ZcEbAuditSheet.getSuperintendentName", fzrDto, 20, manHandler,

    columNamesFzr, "中心主任", "zcFzrUserName");

    fzrEditor.setEditable(false);

    fzrEditor.getField().addValueChangeListener(new ValueChangeListener() {

      @Override
      public void valueChanged(ValueChangeEvent event) {

        ZcEbJieXiang sheet = (ZcEbJieXiang) listCursor.getCurrentObject();

        if (fzrEditor.getField().getValue() == null) {

          sheet.setZcFzrUserId(null);

          sheet.setZcFzrUserName(null);

          sheet.setSuperintendent(null);

          sheet.setSuperintendentOrgNm(null);

          sheet.setSuperintendentOrg(null);

          sheet.setSuperintendentName(null);

          setEditingObject(sheet);

        }

      }

    });
    fzrEditor.setEnabled(false);
//    editorList.add(fzrEditor);

    // 负责组

    String columNamesFzGroup[] = { "采购组" };

    FzGroupFnHandler manHandlerOrg = new FzGroupFnHandler(columNamesFzGroup);

    //ElementConditionDto fzOrgDto = new ElementConditionDto();
    if (sheet != null)
      orgDto.setDattr1(sheet.getZcFzrUserId());


    // 设置采购中心单位代码

    orgDto.setCoCode(((AsOption) listPanel.getBaseDataServiceDelegate().getAsOption(ZcElementConstants.OPT_ZC_CGZX_CODE, requestMeta)).getOptVal());

    // 设置采购中心副主任职内部机构代码

    orgDto.setNd(requestMeta.getSvNd());

    superintendentOrg = new ForeignEntityFieldEditor("ZcEbEntrust.getValOrg", orgDto, 20, manHandlerOrg, columNamesFzGroup, LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_TRANS_SUPERINTENDENT_ORG), "superintendentOrgNm");

    /*
        superintendentOrg.addValueChangeListener(new ValueChangeListener() {

          @Override
          public void valueChanged(ValueChangeEvent e) {

            // TCJLODO Auto-generated method stub

            ZcEbJieXiang sheet = (ZcEbJieXiang) listCursor.getCurrentObject();

            sheet.setSuperintendent(null);

            sheet.setSuperintendentOrgNm(null);

            sheet.setSuperintendentOrg(null);

            sheet.setSuperintendentName(null);

            setEditingObject(sheet);

          }

        });
        */
    superintendentOrg.setEditable(false);

//    editorList.add(superintendentOrg);

    //    TextFieldEditor superintendentOrg = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_TRANS_SUPERINTENDENT_ORG),
    //
    //    "superintendentOrgNm");

    // 经办人

    String columNamesJb[] = { "项目经办人" };

    SuperintendentFnHandler superintendentFnHandler = new SuperintendentFnHandler(columNamesJb);

    ElementConditionDto dto = new ElementConditionDto();

    dto.setNd(requestMeta.getSvNd());
    dto.setZcText0(AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_CGZX_CODE));

    xmjbrEditor = new ForeignEntityFieldEditor("ZcEbAuditSheet.getKeShiPersion", dto, 20,

    superintendentFnHandler, columNamesJb, LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_TRANS_SUPERINTENDENT_NAME),

    "superintendentName");

    xmjbrEditor.setEnabled(false);

    editorList.add(xmjbrEditor);

    xmjbrEditor.getField().addValueChangeListener(new ValueChangeListener() {

      @Override
      public void valueChanged(ValueChangeEvent event) {

        // TCJLODO Auto-generated method stub

        if (fzrEditor.getField().getValue() == null) {

          ZcEbJieXiang sheet = (ZcEbJieXiang) listCursor.getCurrentObject();

          sheet.setSuperintendent(null);

          sheet.setSuperintendentName(null);

          setEditingObject(sheet);

        }

      }

    });

    //开标经办人
    dto = new ElementConditionDto();
    dto.setNd(requestMeta.getSvNd());
    dto.setZcText0(AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_CGZX_CODE));
    
    String columNamesKb[] = { "开标经办人" };

    AttnFnHandler attnFnHandler = new AttnFnHandler(columNamesJb);

    attnEditor = new ForeignEntityFieldEditor("ZcEbAuditSheet.getKeShiPersion", dto, 20,

    attnFnHandler, columNamesKb, "采购中心经办人", "attnName");

//    attnEditor.setEnabled(false);

//    editorList.add(attnEditor);
    
    MoneyFieldEditor zhongbiaoSumField=new MoneyFieldEditor("中标金额","zhongbiaoSum");
    
    MoneyFieldEditor shenyuSumField=new MoneyFieldEditor("剩余金额","shenyuSum");
    
    MoneyFieldEditor wanchengPercentField=new MoneyFieldEditor("完成比例","completePercent"); 

    TextAreaFieldEditor cancelCaiGouReason=new TextAreaFieldEditor("取消采购原因","entrustCancel.remark",200,2,3);

    editorList.add(zhongbiaoSumField);
    editorList.add(shenyuSumField);
    editorList.add(wanchengPercentField);
    editorList.add(cancelCaiGouReason);
    
    return editorList;

  }

  /**

   * 判断当前用户是不是采购中心

   * @return
   * 2012-04-26籍学友

   */

  //  public boolean isCaiGouZhongXin() {
  //
  //    String cgzxCoCode = AsOptionMeta.getOptVal("ZC_CGZX_CODE");
  //
  //    if (cgzxCoCode.equals(requestMeta.getSvCoCode())) {
  //
  //      return true;
  //
  //    }
  //
  //    return false;
  //
  //  }

  /**

   * 副主任

   * @author Administrator

   */

  class FzrFnHandler implements IForeignEntityHandler {

    private String columNames[];

    public FzrFnHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {

      for (Object object : selectedDatas) {

        User user = (User) object;

        ZcEbJieXiang sheet = (ZcEbJieXiang) listCursor.getCurrentObject();

        sheet.setZcFzrUserId(user.getUserId());

        sheet.setZcFzrUserName(user.getUserName());
        sheet.setSuperintendentName(null);

        sheet.setSuperintendent(null);

        setEditingObject(sheet);

      }

    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        User rowData = (User) showDatas.get(i);

        int col = 0;

        //data[i][col++] = rowData.getUserId();

        data[i][col++] = rowData.getUserName();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

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

  /**

   * 负责组

   * @author Administrator

   */

  class FzGroupFnHandler implements IForeignEntityHandler {

    private String columNames[];

    public FzGroupFnHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public boolean beforeSelect(ElementConditionDto dto) {

      ZcEbJieXiang sheet = (ZcEbJieXiang) listCursor.getCurrentObject();

      String fzrUserId = sheet.getZcFzrUserId();

      if (fzrUserId == null || "".equals(fzrUserId.trim())) {

        JOptionPane.showMessageDialog(self, "请先选择分管副主任", "提示", JOptionPane.INFORMATION_MESSAGE);

        return false;

      } else {

        // dto.setOrgCode(orgId);

        return true;

      }

    }

    public void excute(List selectedDatas) {

      for (Object object : selectedDatas) {

        AsVal asVal = (AsVal) object;

        ZcEbJieXiang sheet = (ZcEbJieXiang) listCursor.getCurrentObject();

        sheet.setSuperintendentOrgNm(asVal.getVal());

        sheet.setSuperintendentOrg(asVal.getValId());

        sheet.setSuperintendentName(null);

        sheet.setSuperintendent(null);

        setEditingObject(sheet);

      }

    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        AsVal rowData = (AsVal) showDatas.get(i);

        int col = 0;

        //data[i][col++] = rowData.getUserId();

        data[i][col++] = rowData.getVal();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

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

  /**

   * 文件经办人

   * @author Administrator

   */

  class SuperintendentFnHandler implements IForeignEntityHandler {

    private String columNames[];

    public SuperintendentFnHandler(String columNames[]) {

      this.columNames = columNames;

    }


    // afterClear方法主要是在实现类中做清空后的操作。 反射调用
    public void afterClear(){
      ZcEbJieXiang sheet = (ZcEbJieXiang) listCursor.getCurrentObject();
      //设置项目经办人
      sheet.setAttn(null);
      sheet.setAttnName(null);
      //设置文件经办人
      sheet.setSuperintendent(null);
      sheet.setSuperintendentName(null);

      setEditingObject(sheet);
    }

    public void excute(List selectedDatas) {

        if(selectedDatas!=null && selectedDatas.size()>0){
          User user = (User) selectedDatas.get(0);
          ZcEbJieXiang sheet = (ZcEbJieXiang) listCursor.getCurrentObject();
          //设置项目经办人
          sheet.setAttn(user.getUserId());
          sheet.setAttnName(user.getUserName());
          //设置文件经办人
          sheet.setSuperintendent(user.getUserId());
          sheet.setSuperintendentName(user.getUserName());

          setEditingObject(sheet);
        }
    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        User rowData = (User) showDatas.get(i);

        int col = 0;

        //data[i][col++] = rowData.getUserId();

        data[i][col++] = rowData.getUserName();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

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

  /**
   * <p> 开标经办人外部实体 </p>
   * @author yuzz
   * @since Sep 7, 2012 11:21:41 AM
   */
  class AttnFnHandler implements IForeignEntityHandler {

    private String columNames[];

    public AttnFnHandler(String columNames[]) {

      this.columNames = columNames;

    }
    // afterClear方法主要是在实现类中做清空后的操作。 反射调用
    public void afterClear(){
      ZcEbJieXiang sheet = (ZcEbJieXiang) listCursor.getCurrentObject();
      //设置项目经办人
      sheet.setAttn(null);
      sheet.setAttnName(null);
      //设置文件经办人
      sheet.setSuperintendent(null);
      sheet.setSuperintendentName(null);

      setEditingObject(sheet);
    }

    public void excute(List selectedDatas) {

        if(selectedDatas!=null && selectedDatas.size()>0){
          User user = (User) selectedDatas.get(0);
          ZcEbJieXiang sheet = (ZcEbJieXiang) listCursor.getCurrentObject();
          //设置项目经办人
          sheet.setAttn(user.getUserId());
          sheet.setAttnName(user.getUserName());
          //设置文件经办人
          sheet.setSuperintendent(user.getUserId());
          sheet.setSuperintendentName(user.getUserName());

          setEditingObject(sheet);
        }
    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        User rowData = (User) showDatas.get(i);

        int col = 0;

        //data[i][col++] = rowData.getUserId();

        data[i][col++] = rowData.getUserName();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

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

  @SuppressWarnings("unchecked")
  void getAllDetailListBySN() {

    ElementConditionDto fzrDto = new ElementConditionDto();

    Map<String, String> m = new HashMap<String, String>();

    m.put("SN", auditSheet.getSn());

    //    fzrDto.setCoCode(((AsOption) listPanel.getBaseDataServiceDelegate().getAsOption(

    //      ZcElementConstants.OPT_ZC_CGZX_CODE, requestMeta)).getOptVal());

    //    fzrDto.setNd(requestMeta.getSvNd());

    List edList = zcEbAuditSheetServiceDelegate.getEntrustDetailList(m, requestMeta);

    auditSheet.setEntrustDetailList(edList);

    asdJTabelPanel.setTableModel(ZcEbAuditSheetToTableModelConverter.convertZcEbAuditSheetDetailToTableMode(auditSheet.getEntrustDetailList()));

    translateSubTableColumn();

  }

  public JComponent createSubBillPanel() {

    JPanel subMainPanel = new JPanel();

    subMainPanel.setLayout(new BorderLayout());

    //资质构成
    biTabPane = new JTabbedPane();
    biTablePanel.init();
    biTablePanel.getSearchBar().setVisible(false);
    biTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");
    biTablePanel.getTable().setShowCheckedColumn(true);
    biTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));
    biTabPane.setMinimumSize(new Dimension(240, 150));

    biTabPane.addTab("资金构成", biTablePanel);

    tabPane = new JTabbedPane();

    tabPane.addTab("委托明细", initAsdJTabelPanel());

    splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, biTabPane, tabPane);

    splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 150);

    splitPane.setContinuousLayout(true);

    splitPane.setOneTouchExpandable(true);

    // 只显示向下的箭头

    //    splitPane.putClientProperty("toExpand", true);

    splitPane.setDividerSize(10);

    splitPane.setBackground(self.getBackground());

    return splitPane;

  }

  private JTablePanel initAsdJTabelPanel() {

    asdJTabelPanel.setLayout(new BorderLayout());

    asdJTabelPanel.init();

    return asdJTabelPanel;

  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(getCompoId());

//    toolBar.add(addButton);
//
//    toolBar.add(saveButton);
//
//    toolBar.add(sendButton);
//
//    toolBar.add(suggestPassButton);
//
//    toolBar.add(suggestAuditPassFGZRButton);
//
//    toolBar.add(suggestAuditPassZHZButton);
//
//    toolBar.add(sendToFuZhuRenButton);
//
//    toolBar.add(auditFinalPassButton);
//
//    toolBar.add(callbackButton);
//
//    toolBar.add(unAuditButton);
//
//    toolBar.add(unTreadButton);
//
//    toolBar.add(traceButton);
//
//    toolBar.add(deleteButton);
//    toolBar.add(openNotepadButton);

//    logger.debug("1="+toolBar.getComponentCount());
    toolBar.add(finishCaiGouButton);
    
    toolBar.add(unFinishCaigouButton);
    
    toolBar.add(cancelCaiGouButton);
    
    toolBar.add(exitButton);

    toolBar.add(helpButton);
    
//    logger.debug("2="+toolBar.getComponentCount());

//    toolBar.add(previousButton);
//
//    toolBar.add(nextButton);


    finishCaiGouButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doFinishCaiGou();

      }

    });
    unFinishCaigouButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doUnfinishCaiGou();

      }

    });
    cancelCaiGouButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doCancelCaiGou();

      }

    });

    exitButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doExit();

      }

    });

    helpButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doHelp();

      }

    });

  }

  protected void doCancelCaiGou() {
    // TCJLODO Auto-generated method stub
    ZcEbJieXiang jiexiang = (ZcEbJieXiang) this.listCursor.getCurrentObject();
    if(jiexiang.getEntrustCancel().getRemark()==null){
      JOptionPane.showMessageDialog(this, "请输入取消采购原因", "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
    int num = JOptionPane.showConfirmDialog(this, "确认取消当前采购吗？", "取消当前采购确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      jiexiang.getEntrustCancel().setSn(jiexiang.getSn());
      jiexiang.getEntrustCancel().setEntrustCancelId(jiexiang.getSn());
      jiexiang.getEntrustCancel().setNd(requestMeta.getSvNd());
      jiexiang.getEntrustCancel().setZcInputCode(requestMeta.getSvUserID());
      jiexiang.getEntrustCancel().setZcInputDate(requestMeta.getTransDate());
      jiexiang.getEntrustCancel().setExecutor(requestMeta.getSvUserID());
      
      jiexiang.setStatus(ZcEbJieXiang.STATUS_QU_XIAO_CAI_GOU);
      boolean success = true;

      String errorInfo = "";

      try {       
        zcEbJieXiangServiceDelegate.cancelCaiGou(jiexiang, this.requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        JOptionPane.showMessageDialog(self, "取消采购成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
        
        refreshData();

        if (this.forenEntityDialog == null) {

          this.listPanel.refreshCurrentTabData();

        } else {

          refreshParentForeignDialog(null);

        }

        updateFieldEditorsEditable();

      } else {

        JOptionPane.showMessageDialog(this, "取消采购失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }
  }

  protected void doUnfinishCaiGou() {
    // TCJLODO Auto-generated method stub

    ZcEbJieXiang jiexiang = (ZcEbJieXiang) this.listCursor.getCurrentObject();
    int num = JOptionPane.showConfirmDialog(this, "注意，如果采购计划已经进行了结项，并返回了多余指标，则不能进行取消结项操作.\n " +
    		"请和采购单位确认是否进行了采购计划的结项操作！！\n" +
    		"确认取消当前采购的结项吗？", "取消结项确认", 0);

    if (num == JOptionPane.YES_OPTION) {
      
      boolean success = true;

      String errorInfo = "";

      try {    
        jiexiang.setStatus(ZcEbJieXiang.STATUS_EXEC);
        
        zcEbJieXiangServiceDelegate.unFinishCaiGou(jiexiang, this.requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        JOptionPane.showMessageDialog(self, "取消采购结项成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
        
        refreshData();

        if (this.forenEntityDialog == null) {

          this.listPanel.refreshCurrentTabData();

        } else {

          refreshParentForeignDialog(null);

        }

        updateFieldEditorsEditable();

      } else {

        JOptionPane.showMessageDialog(this, "取消采购结项失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }
  }

  protected void doFinishCaiGou() {
    // TCJLODO Auto-generated method stub

    ZcEbJieXiang jiexiang = (ZcEbJieXiang) this.listCursor.getCurrentObject();
    int num = JOptionPane.showConfirmDialog(this, "确认对当前采购进行结项吗？", "结项确认", 0);

    if (num == JOptionPane.YES_OPTION) {
      
      boolean success = true;

      String errorInfo = "";

      try {    
        jiexiang.setStatus(ZcEbJieXiang.STATUS_COMPLETED);
        
        zcEbJieXiangServiceDelegate.finishCaiGou(jiexiang, this.requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        JOptionPane.showMessageDialog(self, "采购结项成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
        
        refreshData();

        if (this.forenEntityDialog == null) {

          this.listPanel.refreshCurrentTabData();

        } else {

          refreshParentForeignDialog(null);

        }

        updateFieldEditorsEditable();

      } else {

        JOptionPane.showMessageDialog(this, "采购结项失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }
  }

  private void doOpenNotepad() {
    ZcEbJieXiang sheet = (ZcEbJieXiang) this.listCursor.getCurrentObject();
    new ZcNotepadDialog(sheet.getSn(), this.parent);
  }

  private void doFinalSuggestPass() {

    requestMeta.setFuncId(this.auditFinalPassButton.getFuncId());

    doSuggestPassForOther();

  }

  private void doAuditFGZRSuggestPass() {

    ZcEbJieXiang sheet = (ZcEbJieXiang) this.listCursor.getCurrentObject();

    StringBuffer message = new StringBuffer();

    if (sheet.getSuperintendentName() == null || "".equals(sheet.getSuperintendentName().trim())) {

      message.append("请选择[").append(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_TRANS_SUPERINTENDENT_NAME)).append("]！\n");

    }

    List<User> userList = auditSheet.getXbDetailList();

    if (userList == null || userList.size() == 0) {

      message.append("请添加[开标经办人]！\n");

    }

    if (userList.size() > 0) {

      if (!doSave(false)) {

        return;

      }

    }

    if (message.length() > 0) {

      JOptionPane.showMessageDialog(this, message.toString(), "提示", JOptionPane.WARNING_MESSAGE);

      return;

    }

    requestMeta.setFuncId(this.suggestAuditPassFGZRButton.getFuncId());

    doSuggestPassForOther();

  }

  @SuppressWarnings("unchecked")
  private void doAdd() {

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

    auditSheet = null;

    listCursor.setCurrentObject(null);

    refreshData();

    setOldObject();

    updateFieldEditorsEditable();

  }

  private void doDelete() {

    ZcEbJieXiang as = (ZcEbJieXiang) this.listCursor.getCurrentObject();

    int num = JOptionPane.showConfirmDialog(this, "确认删除当前数据？", "删除确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      boolean success = true;

      String errorInfo = "";

      try {

        Map<String, String> m = new HashMap<String, String>();

        m.put("sheetId", as.getSheetId());

        zcEbAuditSheetServiceDelegate.deleteFN(m, this.requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        JOptionPane.showMessageDialog(self, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

        this.listCursor.removeCurrentObject();

        refreshData();

        if (this.forenEntityDialog == null) {

          this.listPanel.refreshCurrentTabData();

        } else {

          refreshParentForeignDialog(null);

        }

        updateFieldEditorsEditable();

      } else {

        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

  }

  @SuppressWarnings("unchecked")
  private void doPrevious() {

    stopTableEditing();

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave(false)) {

          return;

        }

      } else {

        listCursor.setCurrentObject(this.oldAuditSheet);

      }

    }

    listCursor.previous();

    refreshData();

  }

  @SuppressWarnings("unchecked")
  private void doNext() {

    stopTableEditing();

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave(false)) {

          return;

        }

      } else {

        listCursor.setCurrentObject(this.oldAuditSheet);

      }

    }

    listCursor.next();

    refreshData();

  }

  public void doExit() {

    stopTableEditing();

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave(false)) {

          return;

        }

      }

    }

    this.parent.dispose();

  }

  @SuppressWarnings("unchecked")
  public boolean doSave(boolean isShowConfirmMessage) {

    if (!checkBeforeSave(true)) {

      return false;

    }

    boolean success = true;

    String errorInfo = "";

    /*
    HashSet hashset = new HashSet();

    List<User> userList = auditSheet.getXbDetailList();

    int rownum = 1;

    for (User user : userList) {

      if (user.getUserId() == null) {

        JOptionPane.showMessageDialog(this, "保存失败 ！\n" + "开标经办人第[" + rownum + "]行不能为空!", "错误", JOptionPane.ERROR_MESSAGE);

        return false;

      }

      hashset.add(user.getUserId().toString());

      rownum++;

    }

    if (hashset.size() != userList.size()) {

      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + "选择的开标经办人相同!", "错误", JOptionPane.ERROR_MESSAGE);

      hashset = null;

      return false;

    }
    
    if (hashset.contains(auditSheet.getSuperintendent())) {

      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + "文件经办人不能与开标经办人相同!", "错误", JOptionPane.ERROR_MESSAGE);

      hashset = null;

      return false;

    }

    orgDto.setDattr2(auditSheet.getSn());

    zcEbAuditSheetServiceDelegate.delXbPersoinFN(orgDto, requestMeta);

    for (User user : userList) {

      orgDto.setDattr1(user.getUserId());

      orgDto.setDattr2(auditSheet.getSn());

      zcEbAuditSheetServiceDelegate.saveXbPersoinFN(orgDto, requestMeta);

    }

    */
    try {

      zcEbAuditSheetServiceDelegate.saveFN(auditSheet, requestMeta);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      this.listCursor.setCurrentObject(auditSheet);

      this.listPanel.refreshCurrentTabData();

      if (isShowConfirmMessage) {
        JOptionPane.showMessageDialog(self, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      }
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      updateFieldEditorsEditable();

      setOldObject();

      return true;

    } else {

      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      return false;

    }

  }

  @SuppressWarnings("unchecked")
  private boolean checkBeforeSave(boolean saveCheck) {

    List notNullBillElementList = this.billElementMeta.getNotNullBillElement();

    ZcEbJieXiang as = (ZcEbJieXiang) this.listCursor.getCurrentObject();

    StringBuilder errorInfo = new StringBuilder();

    String validateInfo = ZcUtil.validateBillElementNull(as, notNullBillElementList);

    if (validateInfo.length() != 0) {

      errorInfo.append("").append(validateInfo.toString());

    }
//
//    if (as.getSuperintendentOrg() == null || "".equals(as.getSuperintendentOrg())) {
//
//      errorInfo.append("请先选择项目负责处！");
//
//    }

    if (errorInfo.length() != 0) {

      JOptionPane.showMessageDialog(this.parent, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);

      return false;

    }

    return true;

  }

  private void doSend() {

  

  }

  private void doCallback() {

  
  }

  protected void doSuggestPass() {

    

  }

  /**

   * 项目监督员送综合组；分管主任审核送经办人，和开标经办人；经办人，开标经办人办结

   */

  protected void doSuggestPassForOther() {

  }

  /**

   * 监督组主任审核

   */

  private void doJianDuZrSuggestPass() {

  }

  /*

   * 综合组送监审组主任

   */

  private void doSuggestPassZHZ() {

   

  }

  /*

   * 综合组送业务副主任审核

   */

  private void doSendToFuZhuRen() {

  

  }

  private void doUnaudit() {

   
  }

  private void doUntread() {

   

  }

  private void doTrace() {

  
  }

  public void doHelp() {

  }

  private void stopTableEditing() {

    JTablePanel[] jtables = getSubTables();

    for (int i = 0; i < jtables.length; i++) {

      JPageableFixedTable table = jtables[i].getTable();

      if (table.isEditing()) {

        table.getCellEditor().stopCellEditing();

      }

    }

  }

  private void refreshAll(ZcEbJieXiang sheet, boolean isRefreshButton) {

    this.listCursor.setCurrentObject(sheet);

    refreshData();

    if (isRefreshButton) {

//      setButtonStatus(sheet, requestMeta, this.listCursor);

    }

  }

  public boolean isDataChanged() {

    return !DigestUtil.digest(oldAuditSheet).equals(DigestUtil.digest(listCursor.getCurrentObject()));

  }

  void refreshParentForeignDialog(ZcEbJieXiang auditSheet) {

    this.forenEntityDialog.refresh(auditSheet);

  }

  public String getPageStatus() {

    return pageStatus;

  }

  public void setPageStatus(String pageStatus) {

    this.pageStatus = pageStatus;

  }

  protected void updateFieldEditorsEditable() {

    if (auditSheet.getProcessInstId() != null && auditSheet.getProcessInstId() < 0) {

      fzrEditor.setEnabled(true);
      superintendentOrg.setEnabled(true);
    }

  }

  public static String getCompoId() {
    // TCJLODO Auto-generated method stub
    return "ZC_EB_JIE_XIANG";
  }

}
