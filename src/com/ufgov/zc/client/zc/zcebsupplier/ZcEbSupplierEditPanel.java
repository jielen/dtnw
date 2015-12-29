package com.ufgov.zc.client.zc.zcebsupplier;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_FIELD_CA_SERIAL;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.component.table.CheckBoxCellEditor;
import com.ufgov.smartclient.component.table.cellrenderer.CheckBoxTableCellRenderer;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbSupplierToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.EnableButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FreezeButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.HelpButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.celleditor.zc.ZcBCatalogueCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityDialog;
import com.ufgov.zc.client.component.zc.fieldeditor.TextAreaFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.util.GridBagLayoutTools;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.model.AsVal;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcEbSupBsType;
import com.ufgov.zc.common.zc.model.ZcEbSupQualification;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;
import com.ufgov.zc.common.zc.model.ZcEbSupplierFeedback;
import com.ufgov.zc.common.zc.model.ZcEbSupplierJudge;
import com.ufgov.zc.common.zc.model.ZcEbSupplierQualify;
import com.ufgov.zc.common.zc.model.ZcEbSupplierType;
import com.ufgov.zc.common.zc.model.ZcEbZyxm;
import com.ufgov.zc.common.zc.publish.IZcEbSupplierServiceDelegate;

public class ZcEbSupplierEditPanel extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZcEbSupplierEditPanel.class);

  private IZcEbSupplierServiceDelegate zcEbSupplierServiceDelegate = (IZcEbSupplierServiceDelegate) ServiceFactory.create(
    IZcEbSupplierServiceDelegate.class, "zcEbSupplierServiceDelegate");

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_EB_SUPPLIER";

  private FuncButton addButton = new AddButton();

  private FuncButton saveButton = new SaveButton();

  private FuncButton deleteButton = new DeleteButton();

  private FuncButton previousButton = new PreviousButton();

  protected FuncButton editButton = new EditButton();

  private FuncButton nextButton = new NextButton();

  private FuncButton exitButton = new ExitButton();

  private FuncButton helpButton = new HelpButton();

  private FuncButton enableButton = new EnableButton();

  private FuncButton freezeButton = new FreezeButton();
  
  private FuncButton sendButton = new SendButton();

  private final ListCursor listCursor;

  private ZcEbSupplier oldSupplier;

  private ZcEbSupplierListPanel listPanel;

  protected JTablePanel tablePanel = new JTablePanel();

  private ZcEbSupplierEditPanel self = this;

  private GkBaseDialog parent;

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  protected ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  private BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(this.compoId);

  private ForeignEntityDialog forenEntityDialog;

  protected GridBagLayoutTools tool = null;

  protected JTabbedPane tabPane;

  protected JTablePanel tablePanelFeedback = new JTablePanel("tablePanelFeedback");

  protected JTablePanel tablePanelJudge = new JTablePanel("tablePanelJudge");

  protected JTablePanel tablePanelQualify = new JTablePanel("tablePanelQualify");

  protected JTablePanel tablePanelZyxm = new JTablePanel("tablePanelZyxm");

  protected JTablePanel tablePanelMoreInfo = new JTablePanel("tablePanelMoreInfo");

  protected JTablePanel tablePanelSpBsType = new JTablePanel("tablePanelSpBsType");

  protected JTablePanel tablePanelSingup = new JTablePanel("tablePanelSpBsType"); //供应商投标情况

  protected JTablePanel tablePanelGysType = new JTablePanel("tablePanelGysType"); //供应商类型
  
  private static final String Tab_gysType="gysType";
  private static final String Tab_zyxm="zyxm";
  private static final String Tab_spBsType="spBsType";
  private static final String Tab_qualify="qualify";
  private static final String Tab_feedback="feedback";
  private static final String Tab_judge="judge";

  List<AbstractFieldEditor> moreFieldEditorList;

  private String tabStatus;
  
  FileCellEditor qualityFileCellEditor = new FileCellEditor("filesName", "filesID");

  public JComponent createSubBillPanel() {
    tabPane = new JTabbedPane();
    this.createSubTabPane(tablePanelMoreInfo, "供应商更多信息", false, null);
    this.createSubTabPane(tablePanelGysType, "注册类型", true, Tab_gysType); 
//    this.createSubTabPane(tablePanelZyxm, "主营项目", true, Tab_zyxm);   
    this.createSubTabPane(tablePanelSpBsType, "经营类别", true, Tab_spBsType);
    this.createSubTabPane(tablePanelQualify, "相关资质", true, Tab_qualify);
    this.createOptionPane();
    this.createSubTabPane(tablePanelFeedback, "履约情况反馈", true, Tab_feedback);
    this.createSubTabPane(tablePanelJudge, "综合评价", true, Tab_judge);
    initTablePanelSingupPanel();
    return tabPane;
  }

  private void createOptionPane() {

  }

  public void initTablePanelSingupPanel() {
    tablePanelSingup.init();
    tablePanelSingup.getSearchBar().setVisible(false);
    tabPane.add("供应商投标情况", tablePanelSingup);

  }

  public ZcEbSupplierEditPanel(ZcEbSupplierDialog dialog, ListCursor listCursor, String tabStatus, ZcEbSupplierListPanel listPanel) {
    super(new ZcEbSupplier(), "ZC_EB_SUPPLIER");
    this.listCursor = listCursor;
    this.listPanel = listPanel;
    this.parent = dialog;
    this.tabStatus = tabStatus;
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate("供应商登记管理"),
      TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.colCount = 3;
    init();
    requestMeta.setCompoId(compoId);
    refreshData();
    setButtonStatus();
    updateFieldEditorsEditable();
  }

  public ZcEbSupplierEditPanel(ZcEbSupplierDialog dialog, ListCursor listCursor, String tabStatus, ZcEbSupplierListPanel listPanel,
    ForeignEntityDialog forenEntityDialog) {
    super(new ZcEbSupplier(), "ZC_EB_SUPPLIER");
    this.listCursor = listCursor;
    this.listPanel = listPanel;
    this.parent = dialog;
    this.forenEntityDialog = forenEntityDialog;
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate("供应商登记管理"),
      TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.colCount = 3;
    init();
    requestMeta.setCompoId(compoId);
    refreshData();
    setButtonStatus();
    updateFieldEditorsEditable();
  }

  protected void refreshData() {
    ZcEbSupplier supplier = (ZcEbSupplier) listCursor.getCurrentObject();
    if (supplier == null) {//新增页面selectBrandSqlPart
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
      supplier = new ZcEbSupplier();
      setDefualtValue(supplier, ZcSettingConstants.PAGE_STATUS_NEW);
      //添加注册时间
      supplier.setCreateDate(requestMeta.getSysDate());
      List lst = new ArrayList();
      lst.add(supplier);
      supplier.setFeedbackList(new ArrayList());
      supplier.setJudgeList(new ArrayList());
      supplier.setQualifyList(new ArrayList());
      supplier.setBsTypeList(new ArrayList());
      supplier.setZyxmList(new ArrayList());
      supplier.setGysTypeList(_createGysTypeLst(new ArrayList(),supplier.getTempCode()));
      this.listCursor.setDataList(lst, -1);
      listCursor.setCurrentObject(supplier);
      supplier.setNew(true);
    } else {
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
      supplier.setDbDigest(null);
      ZcEbSupplier temp = listPanel.zcEbSupplierServiceDelegate.getZcEbSupplierDetailList(supplier.getTempCode(), requestMeta);
      supplier.setFeedbackList(temp.getFeedbackList());
      supplier.setJudgeList(temp.getJudgeList());
      supplier.setDbDigest(supplier.digest());
      supplier.setQualifyList(temp.getQualifyList());
      supplier.setBsTypeList(temp.getBsTypeList());
      supplier.setZyxmList(temp.getZyxmList());
      supplier.setSignupList(temp.getSignupList());
      supplier.setGysTypeList(_createGysTypeLst(temp.getGysTypeList(),supplier.getTempCode()));
      supplier.setNew(false);
      supplier.setOldCode(supplier.getTempCode());
    }

    refreshSubTableData(supplier);
    this.setEditingObject(supplier);
    updateWFEditorEditable(supplier, requestMeta);
    setOldObject();
    tabPane.repaint();
  }
/*
 * 根据ZC_VS_SUPPLIER_TYPE获取供应商类别全集，然后根据从后头保存的typeLst，确定哪个是选中的
 */
  private List _createGysTypeLst(List typeLst,String suppilerCode) {
    // TCJLODO Auto-generated method stub
    List allLst=AsValDataCache.getAsVal("ZC_VS_SUPPLIER_TYPE");
    if(allLst==null || allLst.size()==0)return typeLst;
    List rtn=new ArrayList();
    for(int i=0;i<allLst.size();i++){
      AsVal val=(AsVal)allLst.get(i);
      ZcEbSupplierType temp=new ZcEbSupplierType();
      temp.setZcSuCode(suppilerCode);
      temp.setTypeCode(val.getValId());
      temp.setTypeName(val.getVal());
      temp.setIsSelected(new Boolean(false));
      for(int j=0;j<typeLst.size();j++){
        ZcEbSupplierType t=(ZcEbSupplierType)typeLst.get(j);
        if(t.getTypeCode().equals(temp.getTypeCode())){
          temp.setIsSelected(new Boolean(true));
          break;
        }
      }
      rtn.add(temp);
    }
    return rtn;
  }

  protected void refreshSubTableData(ZcEbSupplier supplier) {
    tablePanelFeedback.setTableModel(ZcEbSupplierToTableModelConverter.convertFeedbackToTableModel(supplier.getFeedbackList()));
    tablePanelJudge.setTableModel(ZcEbSupplierToTableModelConverter.convertJudgeToTableModel(supplier.getJudgeList()));
    tablePanelQualify.setTableModel(ZcEbSupplierToTableModelConverter.convertQualifyToTableModel(supplier.getQualifyList(),qualityTableisEditable()));
    tablePanelSpBsType.setTableModel(ZcEbSupplierToTableModelConverter.convertBsTypeToTableModel(supplier.getBsTypeList()));
    tablePanelZyxm.setTableModel(ZcEbSupplierToTableModelConverter.convertZyxmTypeToTableModel(supplier.getZyxmList()));
    tablePanelSingup.setTableModel(ZcEbSupplierToTableModelConverter.convertSingupToTableModel(supplier.getSignupList()));
    tablePanelGysType.setTableModel(ZcEbSupplierToTableModelConverter.convertGysTypeToTableModel(supplier.getGysTypeList()));
    
    setFeedbackTableProperty(tablePanelFeedback.getTable());
    setJudgeTableProperty(tablePanelJudge.getTable());
    setQualifyTableProperty(tablePanelQualify.getTable());
    setbsTypeTableProperty(tablePanelSpBsType.getTable());
    setZyxmTableProperty(tablePanelZyxm.getTable());
    setSingupTableProperty(tablePanelSingup.getTable());
    setGysTypeTableProperty(tablePanelGysType.getTable());
    setMoreSupplierInfoTable();
  }

  private boolean qualityTableisEditable() {
    // TCJLODO Auto-generated method stub
    if(ZcSettingConstants.PAGE_STATUS_BROWSE.equalsIgnoreCase(pageStatus)){
      return false;
    }else{
      return true;
    }
  }

  private void setGysTypeTableProperty(JPageableFixedTable table) {
    // TCJLODO Auto-generated method stub
    ZcUtil.translateColName(tablePanelGysType.getTable(), ZcEbSupplierToTableModelConverter.getGysTypeTableColumnInfo());
    SwingUtil.setTableCellEditor(table, "选中", new CheckBoxCellEditor(new Boolean(true),new Boolean(false)));
    SwingUtil.setTableCellRenderer(table, "选中", new CheckBoxTableCellRenderer(new Boolean(true),new Boolean(false)));
  }

  private void setFeedbackTableProperty(final JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    SwingUtil.setTableCellEditor(table, "交货方面评价", new AsValComboBoxCellEditor("VS_SU_EVL"));
    SwingUtil.setTableCellRenderer(table, "交货方面评价", new AsValCellRenderer("VS_SU_EVL"));
    makeComboxEditor(table);
  }

  private void setJudgeTableProperty(final JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    SwingUtil.setTableCellEditor(table, "开始时间", new DateCellEditor());
    SwingUtil.setTableCellRenderer(table, "开始时间", new DateCellRenderer());
    SwingUtil.setTableCellEditor(table, "结束时间", new DateCellEditor());
    SwingUtil.setTableCellRenderer(table, "结束时间", new DateCellRenderer());
    //    SwingUtil.setTableCellEditor(table, "综合得分", new MoneyCellEditor(      false));
    //    SwingUtil.setTableCellRenderer(table, "综合得分", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "响应方面评价", new AsValComboBoxCellEditor("VS_SU_EVL"));
    SwingUtil.setTableCellRenderer(table, "响应方面评价", new AsValCellRenderer("VS_SU_EVL"));
    makeComboxEditor(table);
  }

  private void makeComboxEditor(final JTable table) {
    SwingUtil.setTableCellEditor(table, "质量方面评价", new AsValComboBoxCellEditor("VS_SU_EVL"));
    SwingUtil.setTableCellRenderer(table, "质量方面评价", new AsValCellRenderer("VS_SU_EVL"));
    SwingUtil.setTableCellEditor(table, "价格方面评价", new AsValComboBoxCellEditor("VS_SU_EVL"));
    SwingUtil.setTableCellRenderer(table, "价格方面评价", new AsValCellRenderer("VS_SU_EVL"));
    SwingUtil.setTableCellEditor(table, "服务方面评价", new AsValComboBoxCellEditor("VS_SU_EVL"));
    SwingUtil.setTableCellRenderer(table, "服务方面评价", new AsValCellRenderer("VS_SU_EVL"));
  }

  private void setQualifyTableProperty(final JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());

//    SwingUtil.setTableCellEditor(table, "资质编号", new ZcEbQualificationCellEditor(true));
    SwingUtil.setTableCellEditor(table, "有效期起始日期", new DateCellEditor());
    SwingUtil.setTableCellRenderer(table, "有效期起始日期", new DateCellRenderer());
    SwingUtil.setTableCellEditor(table, "有效期结束日期", new DateCellEditor());
    SwingUtil.setTableCellRenderer(table, "有效期结束日期", new DateCellRenderer());
    
    updateQualityFileCell(false);
    SwingUtil.setTableCellEditor(table, "资质相关附件", qualityFileCellEditor);
  }

  private void setbsTypeTableProperty(final JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    AsValComboBoxCellEditor bsTypebox = new AsValComboBoxCellEditor("ZC_EB_SUP_BS_TYPE");

    SwingUtil.setTableCellEditor(table, "类别", bsTypebox);
    SwingUtil.setTableCellRenderer(table, "类别", new AsValCellRenderer("ZC_EB_SUP_BS_TYPE"));
  }

  private void setZyxmTableProperty(final JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    SwingUtil.setTableCellEditor(table, "品目代码", new ZcBCatalogueCellEditor(false));
  }

  private void setSingupTableProperty(final JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    SwingUtil.setTableCellEditor(table, "投标日期", new DateCellEditor());
    SwingUtil.setTableCellRenderer(table, "投标日期", new DateCellRenderer());
    SwingUtil.setTableCellEditor(table, "是否到开标现场", new AsValComboBoxCellEditor("VS_Y/N"));
    SwingUtil.setTableCellRenderer(table, "是否到开标现场", new AsValCellRenderer("VS_Y/N"));

  }

  private void setMoreSupplierInfoTable() {
    this.tablePanelMoreInfo.setLayout(new BorderLayout());
    this.tablePanelMoreInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "供应商其它信息", TitledBorder.CENTER,
      TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.tablePanelMoreInfo.removeAll();
    this.tablePanelMoreInfo.add(this.makeFieldEditorsPanel(), BorderLayout.NORTH);
    this.tablePanelMoreInfo.repaint();
  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();
    TextFieldEditor editor = new TextFieldEditor("企业名称", "name");
    editorList.add(editor);
    TextFieldEditor coEditor = new TextFieldEditor("组织机构代码", "code");
    coEditor.setEnabled(false);
    editorList.add(coEditor);
    AsValFieldEditor editor1 = new AsValFieldEditor("状态", "status", "ZC_VS_SUPPLIER_STATUS");
    editorList.add(editor1);

    editor = new TextFieldEditor("企业传真", "fax");
    editorList.add(editor);
    editor = new TextFieldEditor("企业地址", "address");
    editorList.add(editor);

    //editor = new TextFieldEditor("企业电话", "phone");
    //editorList.add(editor);

    editor1 = new AsValFieldEditor("供应商级别", "supplierType", "ZC_VS_SUPPLIER_TYPE");
//    editorList.add(editor1);
    editor1 = new AsValFieldEditor("是否协议供货商", "isXysu", "VS_Y/N");
//    editorList.add(editor1);
    editor1 = new AsValFieldEditor("企业规模", "scale", "ZC_VS_E_SCALE");
    editorList.add(editor1);
    /* editor1 = new AsValFieldEditor("是否中小企业", "isZxqy", "VS_Y/N");
    editorList.add(editor1);*/

    editor = new TextFieldEditor("注册资本(万元)", "regCapital");
    editorList.add(editor);
    editor = new TextFieldEditor("营业执照编号", "licenseId");
    editorList.add(editor);
    DateFieldEditor dateEditor = new DateFieldEditor("营业执照有效期开始时间 ", "licenseTimeStart");
    editorList.add(dateEditor);
    dateEditor = new DateFieldEditor("营业执照有效期结束时间", "licenseTimeEnd");
    editorList.add(dateEditor);
    editor = new TextFieldEditor("国税登记号", "stateTaxRegId");
    editorList.add(editor);
    dateEditor = new DateFieldEditor("国税登记时间", "stateTaxRegDate");
    editorList.add(dateEditor);
    editor = new TextFieldEditor("地税登记号", "localTaxRegId");
    editorList.add(editor);
    dateEditor = new DateFieldEditor("地税登记时间", "localTaxRegDate");
    editorList.add(dateEditor);
    //editor1 = new AsValFieldEditor("国内外", "gnw", "ZC_VS_GNW");
    //editorList.add(editor1);
    //editor1 = new AsValFieldEditor("省内外", "snw", "ZC_VS_SNW");
    //editorList.add(editor1);
    editor1 = new AsValFieldEditor("供应商类别 ", "zcSupplierType", "ZC_SUPPLIER_TYPE");
//    editorList.add(editor1);
    editor1 = new AsValFieldEditor("供应商性质 ", "zcSupplierKind", "ZC_SUPPLIER_KIND");
//    editorList.add(editor1);

    editor = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_FIELD_CA_SERIAL), "caSerial");
    editorList.add(editor);
    FileFieldEditor xy = new FileFieldEditor("协议商品库", "xyFile", "xyFileBlobID");
//    editorList.add(xy);
    return editorList;
  }

  private JPanel makeFieldEditorsPanel() {
    JPanel edPanel = new JPanel();
    moreFieldEditorList = new ArrayList<AbstractFieldEditor>();
    createMoreFieldEditorList(moreFieldEditorList);
    tool = null;
    tool = new GridBagLayoutTools();
    tool.setColCount(4);
    tool.setFieldEditorList(moreFieldEditorList);
    tool.layoutFieldEditorPanel(edPanel, ZcEbSupplier.class, "ZC_EB_SUPPLIER");
    tool.setCurrEditingObject(this.listCursor.getCurrentObject());
    tool.setOldObject(this.listCursor.getCurrentObject());
    edPanel.repaint();

    return edPanel;
  }

  protected void createMoreFieldEditorList(List<AbstractFieldEditor> editorList) {
    if (editorList == null) {
      return;
    }
    DateFieldEditor dateEditor = new DateFieldEditor("成立日期", "establishDate");
    editorList.add(dateEditor);
    //TextFieldEditor editor = new TextFieldEditor("登录用户名", "loginName");
    //editorList.add(editor);
    TextFieldEditor editor = new TextFieldEditor("联系人", "linkMan");
    editorList.add(editor);
    editor = new TextFieldEditor("联系人电话", "linkManPhone");
    editorList.add(editor);
    editor = new TextFieldEditor("联系人手机", "linkManMobile");
    editorList.add(editor);
    editor = new TextFieldEditor("邮编", "zipCode");
    editorList.add(editor);
    
    editor = new TextFieldEditor("开户名称(与公司名称不同时填写)", "bankCode");//目前用于开户名称与供应商名称不同时，用于收款的开户名称（收款人名称），不是以前定义的银行编号了
    editorList.add(editor);
    editor = new TextFieldEditor("开户银行", "bankName");
    editorList.add(editor);
    editor = new TextFieldEditor("开户行账户", "accCode");
    editorList.add(editor);
    editor = new TextFieldEditor("公司网址", "url");
    editorList.add(editor);
    editor = new TextFieldEditor("电子邮箱", "email");
    editorList.add(editor);
    editor = new TextFieldEditor("企业法人", "legalPerson");
    editorList.add(editor);
    editor = new TextFieldEditor("法人联系地址", "legalPersonAddr");
    editorList.add(editor);
    AsValFieldEditor editor1 = new AsValFieldEditor("法人证件类型 ", "legalCardType", "ZC_EB_SUP_LEGAL_CARD_TYPE");
    editorList.add(editor1);
    editor = new TextFieldEditor("法人证件号码", "legalPersonIDCard");
    editorList.add(editor);
    editor = new TextFieldEditor("法人传真", "legalPersonFax");
    editorList.add(editor);
    editor = new TextFieldEditor("法人电话", "legalPersonTel");
    editorList.add(editor);
    editor = new TextFieldEditor("法人手机", "legalPersonMobile");
    editorList.add(editor);
    /*
    ZcBCatalogueTreeSelectEditor editorTree = new ZcBCatalogueTreeSelectEditor("主营项目", "zcCatalogueCode", false);
    editorList.add(editorTree);
    */
    editor = new TextFieldEditor("录入人", "operator");
    editor.setEnabled(false);
//    editorList.add(editor);
    dateEditor = new DateFieldEditor("录入日期", "operDate");
    editor.setEnabled(false);
//    editorList.add(dateEditor);

    TextAreaFieldEditor areaEditor = new TextAreaFieldEditor("主营范围", "mainBusinesses", 400, 3, 6);
    editorList.add(areaEditor);
    areaEditor = new TextAreaFieldEditor("公司介绍", "description", 800, 4, 6);
    editorList.add(areaEditor);
  }

  private void setFeedbackDefaultValue(ZcEbSupplierFeedback bean, String type) {
    bean.setJh(type);//交货方面评价
    bean.setJg(type);//价格方面评价
    bean.setZl(type);//质量方面评价
    bean.setFw(type);//服务方面评价
  }

  private void setJudgeDefaultValue(ZcEbSupplierJudge bean, String type) {
    bean.setXy(type);//交货方面评价
    bean.setJg(type);//价格方面评价
    bean.setZl(type);//质量方面评价
    bean.setFw(type);//服务方面评价
  }

  protected void createSubTabPane(final JTablePanel subPanel, String title, boolean needToolBar, final String which) {
    subPanel.init();
    subPanel.getSearchBar().setVisible(false);
    subPanel.setTablePreferencesKey(this.getClass().getName() + "_dt");
    tabPane.addTab(title, subPanel);
    if (!needToolBar || which == null || "".equals(which) ) {
      return;
    }
    JFuncToolBar bottomToolBar = new JFuncToolBar();
    JButton addBtn = new JButton("添加");
    JButton insertBtn = new JButton("插入");
    JButton delBtn = new JButton("删除");
    bottomToolBar.add(addBtn);
    bottomToolBar.add(insertBtn);
    bottomToolBar.add(delBtn);
    if(!Tab_gysType.equals(which)){//供应商类别页签不创建工具条
      
      if(Tab_feedback.equals(which) || Tab_judge.equals(which)){
        if(!ZcUtil.isGys()){//供应商角色登陆时，不创建反馈、评价的工具条
          subPanel.add(bottomToolBar, BorderLayout.SOUTH);
        }
      }else{
        subPanel.add(bottomToolBar, BorderLayout.SOUTH);        
      }
    }

    addBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (self.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)
          || (!"EDITABLE".equalsIgnoreCase(ZcEbSupplierToTableModelConverter.getPageStatus()) && !Tab_qualify.equals(which))) {
          JOptionPane.showMessageDialog(self, "请点击\"修改\"按钮切换至可编辑状态！", "提示", JOptionPane.INFORMATION_MESSAGE);
          return;
        }
        Serializable bean = null;
        if (Tab_feedback.equals(which)) {
          bean = new ZcEbSupplierFeedback();
          setFeedbackDefaultValue((ZcEbSupplierFeedback) bean, "01");
        } else if (Tab_judge.equals(which)) {
          bean = new ZcEbSupplierJudge();
          setJudgeDefaultValue((ZcEbSupplierJudge) bean, "01");
        } else if (Tab_qualify.equals(which)) {
          bean = new ZcEbSupplierQualify();
        } else if (Tab_spBsType.equals(which)) {
          bean = new ZcEbSupBsType();
        } else if (Tab_zyxm.equals(which)) {
          bean = new ZcEbZyxm();
        }
        addSub(subPanel, bean);
      }
    });

    insertBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (self.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)
          || (!"EDITABLE".equalsIgnoreCase(ZcEbSupplierToTableModelConverter.getPageStatus()) && !Tab_qualify.equals(which))) {
          JOptionPane.showMessageDialog(self, "请点击\"修改\"按钮切换至可编辑状态！", "提示", JOptionPane.INFORMATION_MESSAGE);
          return;
        }
        Serializable bean = null;
        if (Tab_feedback.equals(which)) {
          bean = new ZcEbSupplierFeedback();
          setFeedbackDefaultValue((ZcEbSupplierFeedback) bean, "01");
        } else if (Tab_judge.equals(which)) {
          bean = new ZcEbSupplierJudge();
          setJudgeDefaultValue((ZcEbSupplierJudge) bean, "01");
        } else if (Tab_qualify.equals(which)) {
          bean = new ZcEbSupplierQualify();
        } else if (Tab_spBsType.equals(which)) {
          bean = new ZcEbSupBsType();
        } else if (Tab_zyxm.equals(which)) {
          bean = new ZcEbZyxm();
        }
        insertSub(subPanel, bean);
      }
    });

    delBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (self.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)
          || (!"EDITABLE".equalsIgnoreCase(ZcEbSupplierToTableModelConverter.getPageStatus()) && !Tab_qualify.equals(which))) {
          JOptionPane.showMessageDialog(self, "请点击\"修改\"按钮切换至可编辑状态！", "提示", JOptionPane.INFORMATION_MESSAGE);
          return;
        }
        deleteSub(subPanel);
      }
    });
  }

  private void setButtonStatus() {
    initButtonStatus();
    setButtonEnable();
  }

  protected void initButtonStatus() {
    if (this.btnStatusList.size() == 0) {
      ButtonStatus bs = new ButtonStatus();
      bs.setButton(this.addButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
      btnStatusList.add(bs);

      bs = new ButtonStatus();
      bs.setButton(this.editButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addBillStatus(ZcEbSupplier.NEW);
//      bs.addBillStatus(ZcEbSupplier.FROZNE);
      bs.addBillStatus(ZcEbSupplier.ENABLE);
      bs.addBillStatus(ZcEbSupplier.UPDATING);
      btnStatusList.add(bs);

      bs = new ButtonStatus();
      bs.setButton(this.saveButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);
      bs.addBillStatus(ZcEbSupplier.NEW);
      bs.addBillStatus(ZcEbSupplier.ENABLE);
      bs.addBillStatus(ZcEbSupplier.UPDATING);
//      bs.addBillStatus(ZcEbSupplier.FROZNE);
      btnStatusList.add(bs);

      bs = new ButtonStatus();
      bs.setButton(this.deleteButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addBillStatus(ZcEbSupplier.NEW);
//      bs.addBillStatus(ZcEbSupplier.FROZNE);
      btnStatusList.add(bs);

      bs = new ButtonStatus();
      bs.setButton(this.enableButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addBillStatus(ZcEbSupplier.NEW);
      bs.addBillStatus(ZcEbSupplier.FROZNE);
      bs.addBillStatus(ZcEbSupplier.UPDATING);
      btnStatusList.add(bs);

      bs = new ButtonStatus();
      bs.setButton(this.freezeButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addBillStatus(ZcEbSupplier.ENABLE);
      bs.addBillStatus(ZcEbSupplier.UPDATING);
//      List asOptionList = AsOptionMeta.getAllAsOptionById("OPT_ZC_EB_SUPPLIER_SHOW_FREEZEBUTTON_USER");
//      if (asOptionList.size() == 0) {
//        bs.addBillStatus(ZcEbSupplier.ENABLE);
//      }
//      for (int i = 0; i < asOptionList.size(); i++) {
//        AsOption asOption = (AsOption) asOptionList.get(i);
//        if (asOption.getOptVal().equals(requestMeta.getSvUserID())) {
//          bs.addBillStatus("2");
//        } else {
//          bs.addBillStatus("9");
//        }
//      }
      btnStatusList.add(bs);

      bs = new ButtonStatus();
      bs.setButton(this.sendButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addBillStatus(ZcEbSupplier.UPDATING);
      btnStatusList.add(bs);
      
      bs = new ButtonStatus();
      bs.setButton(this.exitButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_ALL);
      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
      btnStatusList.add(bs);

      bs = new ButtonStatus();
      bs.setButton(this.helpButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_ALL);
      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
      btnStatusList.add(bs);

      bs = new ButtonStatus();
      bs.setButton(this.previousButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
      btnStatusList.add(bs);

      bs = new ButtonStatus();
      bs.setButton(this.nextButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
      btnStatusList.add(bs);
    }
  }

  private void setButtonEnable() {
    ZcEbSupplier obj = (ZcEbSupplier) (this.listCursor.getCurrentObject());
    String billStatus = obj.getStatus();
    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, this.compoId, obj.getProcessInstId());
    if(ZcUtil.isGys()){
      enableButton.setEnabled(false);
      freezeButton.setEnabled(false);
    }
  }

  private void setDefualtValue(ZcEbSupplier supplier, String pageStatus) {
    if (pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {
      supplier.setOperator(this.requestMeta.getSvUserName());
      supplier.setOperDate(this.requestMeta.getSysDate());
      supplier.setNd(this.requestMeta.getSvNd());
      supplier.setStatus(ZcEbSupplier.NEW);
    }
  }

  @Override
  protected void updateFieldEditorsEditable() {
    super.updateFieldEditors();
    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW) || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {
        if (fd.getFieldName() != null && (fd.getFieldName().equals("status"))) {
          fd.setEnabled(false);
        } else if (fd.getFieldName() != null && (fd.getFieldName().equals("code"))) {
          if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {
            fd.setEnabled(true);
          } else {
            fd.setEnabled(false);
          }
        } else {
          fd.setEnabled(true);
        }

      }
      for (AbstractFieldEditor fd : this.tool.getFieldEditorList()) {
        if (fd.getFieldName() != null && (fd.getFieldName().equals("operator") || fd.getFieldName().equals("operDate"))) {
          fd.setEnabled(false);
        } else {
          fd.setEnabled(true);
        }
      }
      this.updateSubTableEditable(true);
      updateQualityFileCell(true);
    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {
      for (AbstractFieldEditor fd : this.fieldEditors) {
        fd.setEnabled(false);
      }
      for (AbstractFieldEditor fd : this.tool.getFieldEditorList()) {
        fd.setEnabled(false);
      }
      this.updateSubTableEditable(false);
      updateQualityFileCell(false);
    }
  }

  private void updateQualityFileCell(boolean b) {
    // TCJLODO Auto-generated method stub
    qualityFileCellEditor.setUploadFileEnable(b);
    qualityFileCellEditor.setDeleteFileEnable(b);
  }

  protected void updateSubTableEditable(boolean flag) {
    this.tablePanel.getTable().setEnabled(flag);
    tablePanelFeedback.getTable().setEnabled(flag);
    tablePanelJudge.getTable().setEnabled(flag);
//    tablePanelQualify.getTable().setEnabled(flag);
    tablePanelGysType.getTable().setEnabled(flag);
    
    ZcEbSupplierToTableModelConverter.qualityTableEditable=flag;
  }

  private void updateQuaEditable(boolean flag) {
    ZcEbSupplierToTableModelConverter.qualityTableEditable=flag;
  }

  private void setOldObject() {
    this.oldSupplier = (ZcEbSupplier) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(compoId);
//    toolBar.add(addButton);
    toolBar.add(editButton);
    toolBar.add(saveButton);
//    toolBar.add(sendButton);
    toolBar.add(deleteButton);
    toolBar.add(enableButton);
    toolBar.add(freezeButton);
    toolBar.add(previousButton);
    toolBar.add(nextButton);
    toolBar.add(exitButton);
    toolBar.add(helpButton);

    sendButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doSend();
      }
    });
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doAdd();
      }
    });

    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doEdit();
      }
    });
    deleteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        doDelete();
      }
    });
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        doSave();
      }
    });
    enableButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        doEnable();
      }
    });
    freezeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        doFreeze();
      }
    });
    previousButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doPrevious();
      }
    });
    nextButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doNext();
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

  protected void doSend() {
  
  }

  private void doAdd() {
    this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
    ZcEbSupplier supplier = new ZcEbSupplier();
    setDefualtValue(supplier, ZcSettingConstants.PAGE_STATUS_NEW);
    listCursor.setCurrentObject(supplier);
    setEditingObject(supplier);
    refreshData();
    updateFieldEditorsEditable();
    setDefualtValue(supplier, this.pageStatus);
    setButtonStatus();
  }

  private void doEdit() {

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
    updateFieldEditorsEditable();
    setButtonStatus();
    saveButton.setEnabled(true);
  }

  private void doDelete() {
    ZcEbSupplier supplier = (ZcEbSupplier) this.listCursor.getCurrentObject();
    int num = JOptionPane.showConfirmDialog(this, "确认删除当前数据？", "删除确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      boolean success = true;
      String errorInfo = "";
      try {
        success = this.zcEbSupplierServiceDelegate.deleteSupplierFN(supplier, this.requestMeta);
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
        setButtonStatus();
      } else {
        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void doEnable() {
    requestMeta.setFuncId(enableButton.getFuncId());
    ZcEbSupplier supplier = (ZcEbSupplier) this.listCursor.getCurrentObject();
    //添加启用日期
    //supplier.setAuditDate(new Date());
    supplier.setStatus(ZcEbSupplier.ENABLE);
    boolean success = true;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(enableButton.getFuncId());
      this.zcEbSupplierServiceDelegate.updateZcEbSupplierFN((ZcEbSupplier) ObjectUtil.deepCopy(supplier), this.requestMeta);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      this.listCursor.setCurrentObject(supplier);
      if (this.forenEntityDialog == null) {
        this.listPanel.refreshCurrentTabData();
      } else {
        refreshParentForeignDialog(supplier);
      }
      this.oldSupplier = (ZcEbSupplier) ObjectUtil.deepCopy(supplier);
      JOptionPane.showMessageDialog(self, "启用成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      updateFieldEditorsEditable();
      setButtonStatus();
      enableButton.setEnabled(false);
      freezeButton.setEnabled(true);
      this.editButton.setEnabled(true);
    } else {
      JOptionPane.showMessageDialog(this, "启用失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void doFreeze() {
    this.requestMeta.setFuncId(freezeButton.getFuncId());
    ZcEbSupplier supplier = (ZcEbSupplier) this.listCursor.getCurrentObject();
    supplier.setStatus(ZcEbSupplier.FROZNE);
    boolean success = true;
    String errorInfo = "";
    try {
      this.zcEbSupplierServiceDelegate.updateZcEbSupplierFN((ZcEbSupplier) ObjectUtil.deepCopy(supplier), this.requestMeta);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      this.listCursor.setCurrentObject(supplier);
      if (this.forenEntityDialog == null) {
        this.listPanel.refreshCurrentTabData();
      } else {
        refreshParentForeignDialog(null);
      }
      this.oldSupplier = (ZcEbSupplier) ObjectUtil.deepCopy(supplier);
      JOptionPane.showMessageDialog(self, "冻结成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshData();
      updateFieldEditorsEditable();
      setButtonStatus();
    } else {
      JOptionPane.showMessageDialog(this, "冻结失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void doPrevious() {
    stopTableEditing();
    if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);
      if (num == JOptionPane.YES_OPTION) {
        if (!doSave()) {
          return;
        }
      } else {
        listCursor.setCurrentObject(this.oldSupplier);
      }
    }
    listCursor.previous();
    refreshData();
    setButtonStatus();
    this.updateFieldEditorsEditable();
  }

  private void doNext() {
    stopTableEditing();
    if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);
      if (num == JOptionPane.YES_OPTION) {
        if (!doSave()) {
          return;
        }
      } else {
        listCursor.setCurrentObject(this.oldSupplier);
      }
    }
    listCursor.next();
    refreshData();
    setButtonStatus();
    this.updateFieldEditorsEditable();
  }

  public void doExit() {
    stopTableEditing();
    if (isDataChanged()) {
      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);
      if (num == JOptionPane.YES_OPTION) {
        if (!doSave()) {
          return;
        }
      }
    }
    this.parent.dispose();
  }

  public boolean doSave() {
    stopTableEditing();
    ZcEbSupplier supplier = (ZcEbSupplier) this.listCursor.getCurrentObject();
    if (!checkBeforeSave())
      return false;
    if (!isDataChanged()) {
      JOptionPane.showMessageDialog(self, "数据未发生变化，不需要保存！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return false;
    }
    boolean success = true;
    String errorInfo = "";

    if (this.oldSupplier != null)
      supplier.setOldCode(this.oldSupplier.getCode());
    
    if(ZcEbSupplier.ENABLE.equals(supplier.getStatus())){
      supplier.setStatus(ZcEbSupplier.UPDATING);
    }

    try {
      supplier = this.zcEbSupplierServiceDelegate.saveFN(supplier, requestMeta);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      this.listCursor.setCurrentObject(supplier);
      //      this.oldSupplier = (ZcEbSupplier) ObjectUtil.deepCopy(supplier);
      if (this.forenEntityDialog == null) {
        this.listPanel.refreshCurrentTabData();
      } else {
        refreshParentForeignDialog(supplier);
      }
      JOptionPane.showMessageDialog(self, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      updateFieldEditorsEditable();
      this.refreshData();
      setButtonStatus();
//      freezeButton.setEnabled(false);
//      enableButton.setEnabled(true);
      //      setOldObject();
      return true;
    } else {
      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

  private boolean isCheckNull(Date str) {
    if (str == null) {
      return false;
    } else {
      return true;
    }
  }

  private boolean checkBeforeSave() {
    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {
      ZcEbSupplier curObj = (ZcEbSupplier) this.listCursor.getCurrentObject();
      ZcEbSupplier oldObj = this.zcEbSupplierServiceDelegate.getSupplierById(curObj.getTempCode(), this.requestMeta);
      if (oldObj != null) {
        JOptionPane.showMessageDialog(this.parent, curObj.getRealCode() + "\"已经存在！", "提示", JOptionPane.WARNING_MESSAGE);
        return false;
      }
      if(zcEbSupplierServiceDelegate.checkDupleteSupplier(curObj, requestMeta)){
        JOptionPane.showMessageDialog(this.parent, curObj.getName() + "\"已经存在！", "提示", JOptionPane.WARNING_MESSAGE);
        return false;        
      }
    }

    List notNullBillElementList = this.billElementMeta.getNotNullBillElement();
    ZcEbSupplier supplier = (ZcEbSupplier) this.listCursor.getCurrentObject();
    StringBuilder errorInfo = new StringBuilder();
    String validateInfo = ZcUtil.validateBillElementNull(supplier, notNullBillElementList);
    if (validateInfo.length() != 0) {
      errorInfo.append("").append(validateInfo.toString()).append("\n");
    }

    if (isCheckNull(supplier.getLicenseTimeEnd()) && isCheckNull(supplier.getLicenseTimeStart())) {
      if (supplier.getLicenseTimeEnd().getTime() < supplier.getLicenseTimeStart().getTime()) {
        errorInfo.append("").append("[营业执照]开始时间不能大于结束时间\n");
      }
    }
    if (isCheckNull(supplier.getStateTaxRegDate())) {
      if (supplier.getStateTaxRegDate().getTime() > requestMeta.getSysDate().getTime()) {
        errorInfo.append("").append("[国税登记时间]不能大于当前时间\n");
      }
    }
    if (isCheckNull(supplier.getLocalTaxRegDate())) {
      if (supplier.getLocalTaxRegDate().getTime() > requestMeta.getSysDate().getTime()) {
        errorInfo.append("").append("[地税登记时间]不能大于当前时间\n");
      }
    }
    if (supplier.getEstablishDate() != null) {
      if (supplier.getEstablishDate().getTime() > requestMeta.getSysDate().getTime()) {
        errorInfo.append("").append("[成立日期]不能大于当前时间\n");
      }
    }

    if (errorInfo.length() != 0) {
      JOptionPane.showMessageDialog(this.parent, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  //判断字符串中是否只包含数字
  public static boolean isNumeric(String str) {
    for (int i = str.length(); --i >= 0;) {
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * 供应商管理中  对证书序列号的正则判断  add by humina
   *
   * @param caNum
   * @return
   */
  public boolean checkCaSerialNum(String caNum) {
    //    String caSerialNum = "/^[0-9]{13}$/".trim();
    String caSerialNum = "[0-9]{13}".trim();
    Pattern pattern1 = Pattern.compile(caSerialNum);
    Matcher matcher1 = pattern1.matcher(caNum);
    if (matcher1.find()) {
      return true;
    } else {
      return false;
    }
  }

  public void doHelp() {
    setButtonStatus();
  }

  private void stopTableEditing() {
    JPageableFixedTable table = this.tablePanel.getTable();
    if (table.isEditing()) {
      table.getCellEditor().stopCellEditing();
    }
    if (tablePanelFeedback.getTable().isEditing()) {
      tablePanelFeedback.getTable().getCellEditor().stopCellEditing();
    }
    if (tablePanelJudge.getTable().isEditing()) {
      tablePanelJudge.getTable().getCellEditor().stopCellEditing();
    }
    if (tablePanelQualify.getTable().isEditing()) {
      tablePanelQualify.getTable().getCellEditor().stopCellEditing();
    }
    if (tablePanelSpBsType.getTable().isEditing()) {
      tablePanelSpBsType.getTable().getCellEditor().stopCellEditing();
    }
  }

  public boolean isDataChanged() {
    return !DigestUtil.digest(oldSupplier).equals(DigestUtil.digest(listCursor.getCurrentObject()));
  }

  /**
   * 刷新对话框的数据
   *
   * @param supplier Administrator
   *                 2010-5-18
   */
  void refreshParentForeignDialog(ZcEbSupplier supplier) {
    this.forenEntityDialog.refresh(supplier);
  }

  public String getPageStatus() {
    return pageStatus;
  }

  public void setPageStatus(String pageStatus) {
    this.pageStatus = pageStatus;
  }

  private class PackHandler implements IForeignEntityHandler {

    private String[] columNames;

    PackHandler(String[] columNames) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {

      JTable table = tablePanelQualify.getTable();

      int k = table.getSelectedRow();

      if (k < 0)

        return;

      int k2 = table.convertRowIndexToModel(k);
      for (Object object : selectedDatas) {
        ZcEbSupQualification item = (ZcEbSupQualification) object;
        ZcEbSupplier bean = (ZcEbSupplier) listCursor.getCurrentObject();
        ZcEbSupplierQualify detail = (ZcEbSupplierQualify) bean.getQualifyList().get(k2);
        detail.setLicenseName(item.getQualifName());
        detail.setQualifId(item.getQualifId());
        detail.setLicenseNO(item.getQualifCode());

        setEditingObject(bean);

        listCursor.setCurrentObject(bean);
      }
    }

    @Override
    public boolean isMultipleSelect() {
      // TCJLODO Auto-generated method stub
      return false;
    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        ZcEbSupQualification item = (ZcEbSupQualification) showDatas.get(i);
        int col = 0;
        data[i][col++] = item.getQualifCode();
        data[i][col++] = item.getQualifName();
        data[i][col++] = item.getQualifType();

      }
      MyTableModel model = new MyTableModel(data, columNames) {
        public boolean isCellEditable(int row, int colum) {
          return false;
        }
      };
      return model;
    }

  }
}
