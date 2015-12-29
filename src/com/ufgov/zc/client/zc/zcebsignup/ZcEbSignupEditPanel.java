/**

 * ZcEbSignupEditPanel.java

 * com.ufgov.gk.client.zc.zcebsignup

 * Administrator

 * 2010-4-29

 */

package com.ufgov.zc.client.zc.zcebsignup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
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
import com.ufgov.zc.client.common.converter.ZcEbSignupToTableModelConverter;
import com.ufgov.zc.client.component.FileUploader;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.HelpButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.button.zc.CommonButton;
import com.ufgov.zc.client.component.event.ValueChangeEvent;
import com.ufgov.zc.client.component.event.ValueChangeListener;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.SupplierFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextAreaFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.constants.ZcValSetConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbPackPlan;
import com.ufgov.zc.common.zc.model.ZcEbPackSupplier;
import com.ufgov.zc.common.zc.model.ZcEbPlan;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.model.ZcEbSignup;
import com.ufgov.zc.common.zc.model.ZcEbSignupPackDetail;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbSignupServiceDelegate;

/**

 * @author Administrator

 *

 */

public class ZcEbSignupEditPanel extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZcEbSignupEditPanel.class);

  private final IZcEbSignupServiceDelegate zcEbSignupServiceDelegate = (IZcEbSignupServiceDelegate) ServiceFactory.create(
    IZcEbSignupServiceDelegate.class,

    "zcEbSignupServiceDelegate");

  IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  private final RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private final String compoId = "ZC_EB_SIGNUP";

  private final FuncButton addButton = new AddButton();

  private final FuncButton saveButton = new SaveButton();

  private final FuncButton deleteButton = new DeleteButton();

  private final FuncButton previousButton = new PreviousButton();

  private final FuncButton editButton = new EditButton();

  private final FuncButton nextButton = new NextButton();

  private final FuncButton exitButton = new ExitButton();

  private final FuncButton helpButton = new HelpButton();

  //  private final FuncButton auditButton = new AuditButton();
  //
  //  private final FuncButton unAuditButton = new UnauditButton();

  private final FuncButton bidButton = new CommonButton("zc_fsignup", "报名", "audit.jpg");

  private final FuncButton unBidButton = new CommonButton("zc_funsignup", "撤销报名", "callback.jpg");

  private final ListCursor listCursor;

  private ZcEbSignup oldSignup;

  private final String tabStatus;

  private final ZcEbSignupListPanel listPanel;

  private JTablePanel tablePanel = null;

  JTabbedPane ziZhiTabPanel = new JTabbedPane();

  JTabbedPane subTabPane = new JTabbedPane();

  private final ZcEbSignupEditPanel self = this;

  private final GkBaseDialog parent;

  private final String suSqlMapSelectedId = "ZcEbSignup.getZcEbSupplier";

  private final String projSqlMapSelectedId = "ZcEbProj.getBmZcEbProjByGys";

  private final String bulletinSqlMapSelectId = "ZcEbBulletin.getZcEbBulletinProjStatsList";

  private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private JFuncToolBar subPackTableToolbar;

  private final ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  ElementConditionDto packDto = new ElementConditionDto();

  ElementConditionDto suDto = new ElementConditionDto();

  TextFieldEditor projNameEditor = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PROJ_NAME), "projName");

  private final BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(this.compoId);

  private final BillElementMeta detailBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd(this.compoId + "_PACK");

  private FileFieldEditor zbwjEditor = null;

  private FileFieldEditor zbwjWordEditor = null;

  private SupplierFieldEditor supplierEditor = null;

  private FileFieldEditor signupFileEditor;

  public ZcEbSignupEditPanel(ZcEbSignupDialog parent, ListCursor listCursor, String tabStatus, ZcEbSignupListPanel listPanel) {

    // TCJLODO Auto-generated constructor stub

    super(ZcEbSignup.class, BillElementMeta.getBillElementMetaWithoutNd("ZC_EB_SIGNUP"));

    this.listCursor = listCursor;

    this.tabStatus = tabStatus;

    this.listPanel = listPanel;

    this.parent = parent;

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
      LangTransMeta.translate("ZC_SUPPLIER_SIGNUP_TITLE"),

      TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.colCount = 3;

    init();

    requestMeta.setCompoId(compoId);

    refreshData();

    setButtonStatus();

    updateFieldEditorsEditable();

  }

  /**

   * 设置工具条上按钮的可用性

   * 

   * Administrator

   * 2010-5-15

   */

  private void setButtonStatus() {

    ZcEbSignup signup = (ZcEbSignup) this.listCursor.getCurrentObject();
    if (ZcUtil.isGys()) {
      saveButton.setEnabled(false);
      exitButton.setEnabled(true);
      helpButton.setEnabled(true);
      bidButton.setEnabled(true);
      unBidButton.setEnabled(true);
    }
    if (ZcUtil.isCgzx()) {
      saveButton.setEnabled(true);
      editButton.setEnabled(true);
      exitButton.setEnabled(true);
      helpButton.setEnabled(true);
      bidButton.setEnabled(false);
      unBidButton.setEnabled(false);
    }

    /*if (this.btnStatusList.size() == 0) {

     ButtonStatus bs = new ButtonStatus();
     bs.setButton(this.addButton);
     bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
     bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
     btnStatusList.add(bs);

     bs = new ButtonStatus();
     bs.setButton(this.editButton);
     bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
     bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
     btnStatusList.add(bs);

     bs = new ButtonStatus();
     bs.setButton(this.saveButton);
     bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);
     bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);
     bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
     btnStatusList.add(bs);

     bs = new ButtonStatus();
     bs.setButton(this.deleteButton);
     bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
     bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
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

     bs = new ButtonStatus();
     bs.setButton(this.bidButton);
     bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
     bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
     btnStatusList.add(bs);

     bs = new ButtonStatus();
     bs.setButton(this.unBidButton);
     bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
     bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
     btnStatusList.add(bs);
    }

    ZcEbSignup signup = (ZcEbSignup) (this.listCursor.getCurrentObject());
    ZcUtil.setButtonEnable(this.btnStatusList, ZcSettingConstants.BILL_STATUS_ALL, this.pageStatus, this.compoId, signup.getProcessInstId());
    */

  }

  /**

   * 设置字表下面的按钮状态

   * 

   * Administrator

   * 2010-5-15

   */

  private void setSubTableButton() {

    if (this.subPackTableToolbar != null) {

      if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT) || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {

        this.subPackTableToolbar.setEnabled(true);

      } else {

        this.subPackTableToolbar.setEnabled(false);

      }

    }

  }

  /*protected void initFieldEditorPanel() {

    super.initFieldEditorPanel(new ZcEbSignup(),"ZC_EB_SIGNUP");

    fieldEditors = createFieldEditors();

    int row = 0;

    int col = 0;



    BillElementMeta eleMeta=BillElementMeta.getBillElementMetaWithoutNd(this.compoId);

    List<BillElement> notNullFields=eleMeta.getNotNullBillElement();

    

  //    eleMeta.isElementNullable(elementCode)

    fieldEditorPanel.setLayout(new GridBagLayout());

    

    for (int i = 0; i < fieldEditors.size(); i++) {

      AbstractFieldEditor comp = (AbstractFieldEditor) fieldEditors.get(i);

      

      JLabel label = new AsteriskLabel(comp.getName()+"*");

      if (comp.getFieldName().equals("remark")) {

        comp.setPreferredSize(new Dimension(150, 23));



        fieldEditorPanel.add(label, new GridBagConstraints(col, row, 1, 1, 1.0, 1.0, GridBagConstraints.EAST,

          GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));

        fieldEditorPanel.add(comp, new GridBagConstraints(col + 1, row, 3, 1, 1.0, 1.0,

          GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

        if (col == colCount * 2 - 2) {

          row++;

          col = 0;

        } else {

          col += 4;

        }

      } else {

        comp.setPreferredSize(new Dimension(150, 23));



        fieldEditorPanel.add(label, new GridBagConstraints(col, row, 1, 1, 1.0, 1.0, GridBagConstraints.EAST,

          GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));

        fieldEditorPanel.add(comp, new GridBagConstraints(col + 1, row, 1, 1, 1.0, 1.0,

          GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

        if (col == colCount * 2 - 2) {

          row++;

          col = 0;

        } else {

          col += 2;

        }

      }

    }

  }*/

  private void refreshData() {

    // TCJLODO Auto-generated method stub

    ZcEbSignup signup = (ZcEbSignup) listCursor.getCurrentObject();

    if (signup == null) {//新增页面

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

      signup = new ZcEbSignup();

      signup.setPurDocFeeType("1");

      signup.setOperator(requestMeta.getSvUserName());

      signup.setStatus("0");

      signup.setNd(requestMeta.getSvNd());

      setDefualtValue(signup, ZcSettingConstants.PAGE_STATUS_NEW);

      List lst = new ArrayList();

      lst.add(signup);

      this.listCursor.setDataList(lst, -1);

      listCursor.setCurrentObject(signup);

    }
    if (signup != null && signup.getSignupId() != null && !signup.getStatus().equals("0")) {
      ZcEbSignup bean = zcEbSignupServiceDelegate.getZcEbSignupById(signup, requestMeta);
      listCursor.setCurrentObject(bean);
      signup = bean;
    }
    if (signup.getStatus().equals("0")) {
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
    }
    this.setEditingObject(signup);

    List detailList = new ArrayList();

    if (signup != null && (signup.getSignupPacks() == null || signup.getSignupPacks().size() == 0)) {
      ElementConditionDto dto = new ElementConditionDto();
      if (signup.getStatus().equals("0")) {//获取当前的所有项目分包
        dto.setZcText0(signup.getProjCode());
      } else {//获取已经报名的分包
        dto.setZcText1(signup.getSignupId());
      }
      detailList = zcEbSignupServiceDelegate.getZcEbSignupPackDetail(dto, requestMeta);
      signup.setSignupPacks(detailList);

    }
    /*  if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equals(signup.getStatus())) {
        setCancelStatus(listCursor);
      }*/

    refreshSubTableData(signup.getSignupPacks());

    setOldObject();
    //具有采购中心供应商报名角色的人，并且是新增状态，可用选择供应商
    if (WorkEnv.getInstance().containRole(ZcSettingConstants.ROLE_CGZX_GYSBM) && signup.getStatus().equals("0")) {
      supplierEditor.setEnabled(true);
    }
    setButtonStatus();
  }

  private void setDefualtValue(ZcEbSignup signup, String pageStatus) {

    if (pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {

      //      signup.setOperator(this.requestMeta.getSvUserName());

      //如果是供应商登陆

      //      if ("102".equals(requestMeta.getSvOrgCode())) {

      signup.setProviderCode(requestMeta.getSvUserID());

      signup.setProviderName(requestMeta.getSvUserName());

      //      }

      signup.setSignupDate(this.requestMeta.getSysDate());

      signup.setNd(this.requestMeta.getSvNd());

      signup.setStatus("0");

      signup.setSignupManner("1");

      signup.setIsPayGuarantee("0");

      ZcEbSupplier sup = listPanel.fetchZcEbSupplier();

      signup.setAddress(sup.getAddress());

      signup.setLinkMan(sup.getLinkMan());

      signup.setPhone(sup.getLinkManPhone());

      signup.setMobilePhone(sup.getLinkManMobile());

    }

  }

  private void refreshSubTableData(List deList) {

    if (ZcSettingConstants.PAGE_STATUS_NEW.equals(this.pageStatus)) {

      ElementConditionDto dto = new ElementConditionDto();

      ZcEbSignup signup = (ZcEbSignup) this.listCursor.getCurrentObject();

      if (signup.getProjCode() != null && !"".equals(signup.getProjCode())) {

        dto.setProjCode(signup.getProjCode());
        dto.setZcText0("A");

        List packs = this.zcEbBaseServiceDelegate.queryDataForList("ZcEbProj.getZcEbPackByProjCode", dto, requestMeta);

        deList = convertZcEbPackToSignupPack(packs);

        signup.setSignupPacks(deList);

        listCursor.setCurrentObject(signup);
      }

    }

    ZcEbSignupToTableModelConverter mc = new ZcEbSignupToTableModelConverter();

    tablePanel.setTableModel(mc.convertSignupPackDeToTableModel(deList));

    setTableProperty(tablePanel.getTable());

    refreshZiZhiPanel(deList);
  }

  private void refreshZiZhiPanel(List deList) {
    // TCJLODO Auto-generated method stub
    if (ziZhiTabPanel.getTabCount() > 0) {//已经初始化好了，不需要在初始化      
      return;
    }
    if (deList == null)
      return;
    for (int i = 0; i < deList.size(); i++) {
      ZcEbSignupPackDetail detail = (ZcEbSignupPackDetail) deList.get(i);
      if ("Y".equalsIgnoreCase(detail.getIsCheckQualification())) {
        TextAreaFieldEditor tx = new TextAreaFieldEditor("", "qualificationRequire", 2000, 10, 10);
        tx.setEnabled(false);
        tx.setEditObject(detail);
        JScrollPane j = new JScrollPane(tx);
        ziZhiTabPanel.add(detail.getPackName(), j);
      }
    }
  }

  public List convertZcEbPackToSignupPack(List packs) {

    List signPacks = new ArrayList();

    ZcEbSignup signup = (ZcEbSignup) this.listCursor.getCurrentObject();

    for (int i = 0; i < packs.size(); i++) {

      ZcEbPack pack = (ZcEbPack) packs.get(i);

      ZcEbSignupPackDetail detail = new ZcEbSignupPackDetail();

      detail.setPackCode(pack.getPackCode());

      detail.setPackName(pack.getPackName());

      detail.setPackDesc(pack.getPackDesc());
      detail.setBidDeposit(pack.getBidDeposit());
      detail.setIsCheckQualification(pack.getIsCheckQualification());
      detail.setQualificationRequire(pack.getQualificationRequire());

      if (pack.getBidDocPrice() == null) {

        detail.setDocPrice("");

      } else {

        detail.setDocPrice(pack.getBidDocPrice() + "");

      }

      //计算

      String docPrice;

      if (detail.getDocPrice() == null || "".equals(detail.getDocPrice())) {

        docPrice = "0";

      } else {

        docPrice = detail.getDocPrice();

      }

      BigDecimal s = new BigDecimal(docPrice);

      if (signup.getPurDocFee() == null) {

        signup.setPurDocFee(s);

      } else {

        signup.setPurDocFee(signup.getPurDocFee().add(s));

      }

      if (detail.getSignupPackId() == null || detail.getSignupPackId().trim().length() == 0) {

        detail.setSignupPackId(Guid.genID());

      }

      signPacks.add(detail);

    }

    self.setEditingObject(signup);

    return signPacks;

  }

  /* (non-Javadoc)

   * @see com.ufgov.gk.client.component.zc.AbstractMainSubEditPanel#updateFieldEditorsEditable()

   */

  @Override
  protected void updateFieldEditorsEditable() {

    super.updateFieldEditors();
    /*
        if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW) || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

          for (AbstractFieldEditor fd : this.fieldEditors) {

            if (fd.getFieldName() != null

            && (fd.getFieldName().equals("signupDate") || fd.getFieldName().equals("status") || fd.getFieldName().equals("projName") || (

            WorkEnv.getInstance().containRole(AsOptionMeta.getOptVal("OPT_ZC_GYS_NORMAL_ROLE"))

            && "providerName".equals(fd.getFieldName()) || "zbFileName".equals(fd.getFieldName())))) {
              //时间、状态、项目名称、供应商登陆时的供应商名称
              fd.setEnabled(false);

            } else {

              fd.setEnabled(true);

            }

          }

          //      this.tablePanel.getTable().setEnabled(true);

        } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {

          for (AbstractFieldEditor fd : this.fieldEditors) {

            fd.setEnabled(false);

          }

          //      this.tablePanel.getTable().setEnabled(false);

        }*/

    //    if ("102".equals(requestMeta.getSvOrgCode())) {

    //      projCodeEditor.setEnabled(false);
    //
    //    }

    setZbFileEnabled();

    Date sysDate = (Date) zcEbBaseServiceDelegate.queryObject("ZcEbPlan.getSysdate", null, requestMeta);
    ZcEbSignup signup = (ZcEbSignup) listCursor.getCurrentObject();
    if (sysDate.compareTo(signup.getPlan().getSellEndTime()) >= 0) {
      FileUploader ful=(FileUploader)signupFileEditor.getEditorComponent();
      ful.setDelFileButton(false);
      ful.setUploadFileButton(false);
      ful.setDownloadFileButton(true);
      ful.setButtonEnable();
    }
  }

  /**

   * 标段外部部件选择处理类

   * @author Administrator

   *

   */

  private class ZcEbPackHandler implements IForeignEntityHandler {

    private final String columNames[];

    public ZcEbPackHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public boolean beforeSelect(ElementConditionDto dto) {
      ZcEbSignup signup = (ZcEbSignup) listCursor.getCurrentObject();
      dto.setDattr1(signup.getProjCode());
      if (ZcSettingConstants.PITEM_OPIWAY_YQZB.equals(signup.getPurType()) || ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(signup.getPurType())) {
        dto.setPurType("2");
      } else {
        dto.setPurType(null);
      }
      return true;
    }

    public void excute(List selectedDatas) {

      ZcEbSignup signup = (ZcEbSignup) listCursor.getCurrentObject();

      List packs = signup.getSignupPacks();

      JTable table = tablePanel.getTable();

      final BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return;

      final int k2 = table.convertRowIndexToModel(k);

      ZcEbSignupPackDetail signpack = (ZcEbSignupPackDetail) (model.getBean(k2));

      if (selectedDatas.size() > 0) {

        ZcEbPack pack = (ZcEbPack) selectedDatas.get(0);

        for (int i = 0; i < packs.size(); i++) {

          ZcEbSignupPackDetail zp = (ZcEbSignupPackDetail) packs.get(i);

          if (zp.getPackCode() != null && zp.getPackCode().equals(pack.getPackCode())) {

            //标段重复

            JOptionPane.showMessageDialog(self, "该标段已经报过名了！", "提示", JOptionPane.INFORMATION_MESSAGE);

            //tablePanel.getTable().getModel().

            model.deleteRow(packs.size() - 1);

            return;

          }

        }

        //显示

        signpack.setPackCode(pack.getPackCode());

        signpack.setPackName(pack.getPackName());

        signpack.setPackDesc(pack.getPackDesc());

        if (pack.getBidDocPrice() == null) {

          signpack.setDocPrice("");

        } else {

          signpack.setDocPrice(pack.getBidDocPrice() + "");

        }

        //计算

        String docPrice;

        if (signpack.getDocPrice() == null || "".equals(signpack.getDocPrice())) {

          docPrice = "0";

        } else {

          docPrice = signpack.getDocPrice();

        }

        BigDecimal s = new BigDecimal(docPrice);

        if (signup.getPurDocFee() == null) {

          signup.setPurDocFee(s);

        } else {

          signup.setPurDocFee(signup.getPurDocFee().add(s));

        }

        self.setEditingObject(signup);

      }

      if (signpack.getSignupPackId() == null || signpack.getSignupPackId().trim().length() == 0) {

        signpack.setSignupId(Guid.genID());

      }
      //      table.getCellEditor().cancelCellEditing();

      //      SwingUtilities.invokeLater(new Runnable() {
      //
      //        public void run() {
      //          model.fireTableRowsUpdated(k2, k2);
      //        }
      //      });
      model.fireTableRowsUpdated(k2, k2);

    }

    @Override
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

    @Override
    public boolean isMultipleSelect() {

      return false;

    }

  }

  private void setTableProperty(JTable table) {

    ZcUtil.translateColName(table, "ZC_EB_");

    String columNames[] = { LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_CODE),
      LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_NAME) };

    ZcEbPackHandler handler = new ZcEbPackHandler(columNames);

    if (packDto == null) {
      this.packDto = new ElementConditionDto();
    }

    ZcEbSignup signup = (ZcEbSignup) listCursor.getCurrentObject();

    packDto.setManageCode(WorkEnv.getInstance().getCurrUserId());

    ForeignEntityFieldCellEditor packEditor = new ForeignEntityFieldCellEditor("ZcEbProj.getZcEbPackForSignup", this.packDto, 20, handler,
      columNames, LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PAKE_NAME), "packName");

    SwingUtil.setTableCellEditor(table, "PACK_CODE", packEditor);

    SwingUtil.setTableCellEditor(table, "SP_STATUS", new AsValComboBoxCellEditor(ZcValSetConstants.ZC_VS_SIGNUP_PACK_STATUS));

    SwingUtil.setTableCellRenderer(table, "SP_STATUS", new AsValCellRenderer(ZcValSetConstants.ZC_VS_SIGNUP_PACK_STATUS));

    SwingUtil.setTableCellEditor(table, "CHECK_RESULT", new AsValComboBoxCellEditor(ZcValSetConstants.ZC_VS_SIGNUP_CHECK_RESULT));

    SwingUtil.setTableCellRenderer(table, "CHECK_RESULT", new AsValCellRenderer(ZcValSetConstants.ZC_VS_SIGNUP_CHECK_RESULT));

    SwingUtil.setTableCellRenderer(table, "IS_CHECK_QUALIFICATION", new AsValCellRenderer("VS_Y/N"));

  }

  private void setOldObject() {

    oldSignup = (ZcEbSignup) ObjectUtil.deepCopy(listCursor.getCurrentObject());

  }

  /* (non-Javadoc)

   * @see com.ufgov.gk.client.component.zc.AbstractMainSubEditPanel#createFieldEditors()

   */

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {

    // TCJLODO Auto-generated method stub

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    String columNames[] = { "项目编号", "项目名称", "采购类型", "负责人", "立项时间", /*"是否划分标段",*/"电话", "邮件", "传真"/*, "是否发布采购公告", "是否发布采购结果" */};

    ZcEbProjFnHandler projHandler = new ZcEbProjFnHandler(columNames);

    ElementConditionDto dto = new ElementConditionDto();

    dto.setStatus("specialNum134");

    if (WorkEnv.getInstance().containRole(AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_GYS_NORMAL_ROLE))
      || WorkEnv.getInstance().containRole(AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_GYS_HUIYUAN_ROLE))) {
      dto.setZcText0(requestMeta.getSvUserID());
    } else {
      dto.setExecutor(requestMeta.getSvUserID());
    }

    ForeignEntityFieldEditor projEditor = new ForeignEntityFieldEditor(this.projSqlMapSelectedId, dto, 20, projHandler, columNames, "项目编号",
      "projCode");

    projEditor.addValueChangeListener(new ValueChangeListener() {

      @Override
      public void valueChanged(ValueChangeEvent e) {

        projCodeChange();

      }

    });
    projEditor.setEnabled(false);
    projNameEditor.setEnabled(false);

    DateFieldEditor signupDateEditor = new DateFieldEditor("报名日期", "signupDate", DateFieldEditor.TimeTypeH24);
    signupDateEditor.setEnabled(false);

    String suColumNames[] = { "供应商代码", "供应商名称", "联系人", "电话", "手机", "审核人", "地址", "邮编" };//, "审核日期", "状态" };

    ZcEbSupplierFnHandler handler = new ZcEbSupplierFnHandler(suColumNames);

    //    System.out.println("befor "+this.sqlMapSelectedId);

    suDto.setZcText0("2");//只选择已经启用的供应商

    //    ForeignEntityFieldEditor projCodeEditor= new ForeignEntityFieldEditor(this.suSqlMapSelectedId,suDto, 20, handler,

    //      suColumNames, "供应商", "providerName");

    supplierEditor = new SupplierFieldEditor(this.suSqlMapSelectedId, suDto, 20, handler, suColumNames, "供应商", "providerName");
    supplierEditor.setEnabled(false);

    //    TextFieldEditor editorSpn = new TextFieldEditor("供应商名称", "providerName");

    //    editorList.add(editorSpn);

    //    editorSpn.setEnabled(false);

    TextFieldEditor gysdizhi = new TextFieldEditor("供应商地址", "address");

    TextFieldEditor lxr = new TextFieldEditor("联系人", "linkMan");

    TextFieldEditor dianhua = new TextFieldEditor("电话", "phone");

    TextFieldEditor shouji = new TextFieldEditor("手机", "mobilePhone");

    //    TextFieldEditor editor4b = new TextFieldEditor("邮件", "email");
    //
    //    editorList.add(editor4b);

    //    MoneyFieldEditor editor6 = new MoneyFieldEditor("采购文件购买金额", "purDocFee");
    //
    //    editorList.add(editor6);
    //
    //    DateFieldEditor editor6a = new DateFieldEditor("确认日期", "purDocBuyDate");
    //
    //    editorList.add(editor6a);
    //
    //    AsValFieldEditor editor8 = new AsValFieldEditor("采购文件支付方式", "purDocFeeType", "VS_ZC_EB_PURDOC_TYPE");
    //
    //    editorList.add(editor8);
    //
    //    TextFieldEditor editor4c = new TextFieldEditor("采购文件支付凭证", "purDocFeeBill");
    //
    //    editorList.add(editor4c);

    AsValFieldEditor zhuangtai = new AsValFieldEditor("状态", "status", "ZC_VS_SIGNUP_STATUS");
    zhuangtai.setEnabled(false);

    // editorList.add(zhuangtai);

    //    AsValFieldEditor editor9 = new AsValFieldEditor("报名方式", "signupManner", "ZC_VS_SIGNUP_MANNER");
    //
    //    editorList.add(editor9);

    //    TextFieldEditor editor10 = new TextFieldEditor("受理人", "operator");
    //
    //    editorList.add(editor10);

    zbwjEditor = new FileFieldEditor("招标文件", "zbFileName", "zbFileId", true);
    zbwjEditor.setEnabled(false);

    zbwjWordEditor = new FileFieldEditor("招标文件(word版)", "zbFileName", "zbFileWordId", true);
    zbwjWordEditor.setEnabled(false);

    signupFileEditor=new FileFieldEditor("报名上传附件", "signupFileName", "signupFileId" );
    
    TextFieldEditor beizhu = new TextFieldEditor("备注", "remark");

    DateFieldEditor bskssj = new DateFieldEditor("标书开售时间", "plan.sellStartTime", DateFieldEditor.TimeTypeH12);
    bskssj.setEnabled(false);
    DateFieldEditor bmzjsj = new DateFieldEditor("报名截止时间", "plan.sellEndTime", DateFieldEditor.TimeTypeH24);
    bmzjsj.setEnabled(false);
    DateFieldEditor tbzzsj = new DateFieldEditor("投标截止时间", "plan.bidEndTime", DateFieldEditor.TimeTypeH24);
    tbzzsj.setEnabled(false);
    TextFieldEditor kbdd = new TextFieldEditor("开标地点", "plan.openBidAddress");
    kbdd.setEnabled(false);

    AsValFieldEditor cgfs = new AsValFieldEditor("采购方式", "purType", "ZC_VS_PITEM_OPIWAY");
    cgfs.setEnabled(false);

    editorList.add(projEditor);
    editorList.add(projNameEditor);
    editorList.add(cgfs);
    editorList.add(bskssj);
    editorList.add(bmzjsj);
    editorList.add(tbzzsj);
    editorList.add(kbdd);
    editorList.add(zbwjEditor);
    editorList.add(zbwjWordEditor);
    editorList.add(supplierEditor);
    editorList.add(gysdizhi);
    editorList.add(lxr);
    editorList.add(dianhua);
    editorList.add(shouji);
    editorList.add(beizhu);
    editorList.add(signupDateEditor);
    editorList.add(zhuangtai);
    editorList.add(signupFileEditor);

    return editorList;

  }

  /**

   * 供应商外部部件选择类

   * @author Administrator

   *

   */

  private class ZcEbSupplierFnHandler implements IForeignEntityHandler {

    private final String columNames[];

    public ZcEbSupplierFnHandler(String columNames[]) {

      this.columNames = columNames;

    }

    @Override
    public void excute(List selectedDatas) {

      for (Object object : selectedDatas) {

        ZcEbSupplier supplier = (ZcEbSupplier) object;

        ZcEbSignup signup = (ZcEbSignup) listCursor.getCurrentObject();

        signup.setProviderCode(supplier.getCode());

        signup.setProviderName(supplier.getName());

        signup.setLinkMan(supplier.getLinkMan());

        signup.setAddress(supplier.getAddress());

        signup.setPhone(getPhone(supplier));

        signup.setMobilePhone(supplier.getLinkManMobile());

        signup.setZipcode(supplier.getZipCode());

        setEditingObject(signup);

      }

    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcEbSupplier rowData = (ZcEbSupplier) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getCode();

        data[i][col++] = rowData.getName();

        data[i][col++] = rowData.getLinkMan();

        data[i][col++] = getPhone(rowData);//rowData.getPhone();

        data[i][col++] = rowData.getLinkManMobile();

        data[i][col++] = rowData.getOperator();

        data[i][col++] = rowData.getAddress();

        data[i][col++] = rowData.getZipCode();

        //        data[i][col++] = rowData.getOperDate();

        //        data[i][col++] = rowData.getStatus();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        @Override
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

  private String getPhone(ZcEbSupplier sup) {

    if (sup.getPhone() != null && !"".equals(sup.getPhone())) {

      return sup.getPhone();

    } else if (sup.getLegalPersonTel() != null && !"".equals(sup.getLegalPersonTel())) {

      return sup.getLegalPersonTel();

    } else if (sup.getLinkManPhone() != null && !"".equals(sup.getLinkManPhone())) {

      return sup.getLinkManPhone();

    } else if (sup.getLegalPersonMobile() != null && !"".equals(sup.getLegalPersonMobile())) {

      return sup.getLegalPersonMobile();

    } else if (sup.getLinkManMobile() != null && !"".equals(sup.getLinkManMobile())) {

      return sup.getLinkManMobile();

    } else {

      return "";

    }

  }

  private void projCodeChange() {

    ZcEbSignup signup = (ZcEbSignup) this.listCursor.getCurrentObject();

    for (AbstractFieldEditor editor : this.fieldEditors) {

      if (editor.getFieldName().equals("projCode")) {

        Object obj = editor.getValue();

        if (obj == null || obj.toString().trim().length() == 0) {

          signup.setProjCode(null);

          signup.setProjName(null);

          this.projNameEditor.setEditObject(signup);

          break;

        }

      }

    }

    //清空报名标段表

    signup.setSignupPacks(new ArrayList());

    if (WorkEnv.getInstance().containRole(AsOptionMeta.getOptVal("OPT_ZC_GYS_NORMAL_ROLE"))) {
    } else {
      signup.setProviderCode("");
      signup.setProviderName("");
      this.suDto.setProjCode(signup.getProjCode());
    }

    refreshSubTableData(signup.getSignupPacks());

  }

  /**

   * 项目选择部件

   * @author Administrator

   *

   */

  private class ZcEbProjFnHandler implements IForeignEntityHandler {

    private final String columNames[];

    public ZcEbProjFnHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {

      // TCJLODO Auto-generated method stub

      for (Object object : selectedDatas) {

        ZcEbProj proj = (ZcEbProj) object;

        //        System.out.println(supplier.getName());

        ZcEbSignup signup = (ZcEbSignup) listCursor.getCurrentObject();

        signup.setProjCode(proj.getProjCode());
        signup.setAgency(proj.getAgency());
        signup.setProjName(proj.getProjName());
        signup.setPurType(proj.getPurType());
        suDto.setPurType(proj.getPurType());
        setEditingObject(signup);
        projCodeChange();

        refreshSubTableData(new ArrayList());

      }

    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcEbProj rowData = (ZcEbProj) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getProjCode();

        data[i][col++] = rowData.getProjName();

        data[i][col++] = AsValDataCache.getName("ZC_EB_PUR_TYPE", rowData.getPurType());

        data[i][col++] = rowData.getManager();

        SimpleDateFormat sdf = new SimpleDateFormat(ZcSettingConstants.SIMPLE_DATE_FORMAT_DATE_ONLY);

        data[i][col++] = sdf.format(rowData.getProjDate());

        //data[i][col++] = rowData.getProjDate();

        //data[i][col++] = rowData.getIsSplitPack();

        data[i][col++] = rowData.getPhone();

        data[i][col++] = rowData.getEmail();

        data[i][col++] = rowData.getFax();

        //data[i][col++] = rowData.getIsPubPurBulletin();

        //data[i][col++] = rowData.getIsPubPurResult();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        @Override
        public boolean isCellEditable(int row, int colum) {

          return false;

        }

      };

      return model;

    }

    @Override
    public boolean isMultipleSelect() {

      // TCJLODO Auto-generated method stub

      return false;

    }

  }

  /* (non-Javadoc)

   * @see com.ufgov.gk.client.component.zc.AbstractMainSubEditPanel#createSubBillPanel()

   */

  @Override
  public JComponent createSubBillPanel() {

    tablePanel = new JTablePanel("zc_eb_signup_pack_table_panel", AsOptionMeta.getOptVal(ZcSettingConstants.ZC_OPTON_SIGNUP_PACK_HELP_MSG));
    tablePanel.init();

    tablePanel.setTablePreferencesKey(this.getClass().getName() + "_table");

    tablePanel.getTable().setShowCheckedColumn(false);

    tablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(50, 0));

    setTableCell(tablePanel.getTable());

    subTabPane.addTab("报名分包", tablePanel);

    this.subPackTableToolbar = new JFuncToolBar();

    JButton addBtn1 = new SubaddButton(false);

    JButton insertBtn1 = new SubinsertButton(false);

    JButton delBtn1 = new SubdelButton(false);

    this.subPackTableToolbar.add(addBtn1);

    this.subPackTableToolbar.add(insertBtn1);

    this.subPackTableToolbar.add(delBtn1);

    //    tablePanel.add(this.subPackTableToolbar, BorderLayout.SOUTH);

    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        addSub(tablePanel);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        insertSub(tablePanel);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        deleteSub(tablePanel);

      }

    });

    subTabPane.addTab("报名资质要求", ziZhiTabPanel);
    return subTabPane;

  }

  @Override
  protected Integer[] deleteSub(JTablePanel tablePanel) {

    JPageableFixedTable table = tablePanel.getTable();

    stopTableEditing();

    int[] selectedRows = table.getSelectedRows();

    if (selectedRows.length == 0) {

      JOptionPane.showMessageDialog(self, "没有选择数据！", "提示", JOptionPane.INFORMATION_MESSAGE);

      return null;

    }

    BeanTableModel tableModel = ((BeanTableModel) table.getModel());

    int[] selRows = new int[selectedRows.length];

    for (int i = 0; i < selRows.length; i++) {

      selRows[i] = table.convertRowIndexToModel(selectedRows[i]);

    }

    Arrays.sort(selRows);

    ZcEbSignup signup = (ZcEbSignup) this.listCursor.getCurrentObject();

    List packPlanLst = getPackPlanListByPackCode(signup.getProjCode());

    String docPrice;

    for (int i = selRows.length - 1; i >= 0; i--) {

      ZcEbSignupPackDetail bean = new ZcEbSignupPackDetail();

      bean = (ZcEbSignupPackDetail) tableModel.getBean(selRows[i]);

      if (!isCanDel(bean, packPlanLst)) {

        JOptionPane.showMessageDialog(self, bean.getPackName() + "不能删除！", "提示", JOptionPane.INFORMATION_MESSAGE);

        return null;
      }

      if (bean.getDocPrice() == null || "".equals(bean.getDocPrice())) {

        docPrice = "0";

      } else {

        docPrice = bean.getDocPrice();

      }

      BigDecimal s = new BigDecimal(docPrice);

      if (signup.getPurDocFee() == null) {

        signup.setPurDocFee(s);

      } else {

        signup.setPurDocFee(signup.getPurDocFee().subtract(s));

      }

      tableModel.deleteRow(selRows[i]);

    }

    listCursor.setCurrentObject(signup);

    self.setEditingObject(signup);

    return null;

  }

  private boolean isCanDel(ZcEbSignupPackDetail detail, List packPlanLst) {

    if (detail == null || detail.getPackCode() == null || detail.getPackCode().equals(""))
      return true;

    for (int i = 0; i < packPlanLst.size(); i++) {

      ZcEbPackPlan plan = (ZcEbPackPlan) packPlanLst.get(i);

      if (detail.getPackCode().equals(plan.getPackCode()) && !checkBidEndTime(detail, plan))
        return false;
    }

    return true;
  }

  private boolean checkBidEndTime(ZcEbSignupPackDetail detail, ZcEbPackPlan plan) {

    if (requestMeta.getSysDate().compareTo(plan.getSellEndTime()) >= 0
      && ZcValSetConstants.VAL_ZC_VS_SIGNUP_STATUS_SIGNUPED.equals(detail.getSpStatus())) {

      return false;
    }

    return true;
  }

  private void insertSub(JTablePanel tablePanel) {

    if (!checkProj()) {

      JOptionPane.showMessageDialog(self, "请先选择一个采购项目 ！", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    stopTableEditing();

    ZcEbSignup signup = (ZcEbSignup) listCursor.getCurrentObject();

    //    ForeignEntityFieldCellEditor cellEditor = (ForeignEntityFieldCellEditor) tablePanel.getTable().getColumn("PACK_CODE").getCellEditor();
    //
    //    this.packDto = new ElementConditionDto();

    this.packDto.setDattr1(signup.getProjCode());

    //    cellEditor.updateDto(packDto);

    BeanTableModel editTableModel = (BeanTableModel) tablePanel.getTable().getModel();

    ZcEbSignupPackDetail bean = new ZcEbSignupPackDetail();

    bean.setSignupId(signup.getSignupId());

    bean.setSignupPackId(Guid.genID());

    int selectedRow = tablePanel.getTable().getSelectedRow();

    if (selectedRow != -1) {

      editTableModel.insertRow(selectedRow + 1, bean);

    } else {

      editTableModel.insertRow(editTableModel.getRowCount(), bean);

    }

  }

  private boolean checkProj() {

    // TCJLODO Auto-generated method stub

    ZcEbSignup s = (ZcEbSignup) listCursor.getCurrentObject();

    if (s.getProjCode() == null || s.getProjCode().trim().equals(""))

      return false;

    return true;

  }

  private void addSub(JTablePanel tablePanel) {

    if (!checkProj()) {

      JOptionPane.showMessageDialog(self, "请先选择一个采购项目 ！", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    tablePanel.getTable().clearSelection();

    stopTableEditing();

    BeanTableModel editTableModel = (BeanTableModel) tablePanel.getTable().getModel();

    ZcEbSignupPackDetail bean = new ZcEbSignupPackDetail();

    ZcEbSignup signup = (ZcEbSignup) listCursor.getCurrentObject();

    //    ForeignEntityFieldCellEditor cellEditor = (ForeignEntityFieldCellEditor) tablePanel.getTable().getColumn("PACK_CODE").getCellEditor();
    //
    //    this.packDto = new ElementConditionDto();

    this.packDto.setDattr1(signup.getProjCode());

    //    cellEditor.updateDto(packDto);

    bean.setSignupId(signup.getSignupId());

    bean.setSignupPackId(Guid.genID());

    editTableModel.insertRow(editTableModel.getRowCount(), bean);

  }

  private void setTableCell(JPageableFixedTable table) {

    //    SwingUtil.setTableCellEditor(table,columnIdentifier, cellEditor)

  }

  /* (non-Javadoc)

   * @see com.ufgov.gk.client.component.zc.AbstractMainSubEditPanel#initToolBar(com.ufgov.gk.client.component.JFuncToolBar)

   */

  @Override
  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(compoId);

    toolBar.add(saveButton);

    //    toolBar.add(addButton);
    //    toolBar.add(addButton);
    //    toolBar.add(editButton);

    //    toolBar.add(saveButton);

    //    toolBar.add(deleteButton);

    //    toolBar.add(auditButton);
    //
    //    toolBar.add(unAuditButton);

    //    toolBar.add(previousButton);
    //
    //    toolBar.add(nextButton);

    toolBar.add(bidButton);

    toolBar.add(unBidButton);

    toolBar.add(exitButton);

    toolBar.add(helpButton);

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

        // TCJLODO Auto-generated method stub

        doDelete();

      }

    });

    saveButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        // TCJLODO Auto-generated method stub

        doSave();

      }

    });

    //    auditButton.addActionListener(new ActionListener() {
    //
    //      @Override
    //      public void actionPerformed(ActionEvent e) {
    //
    //        // TCJLODO Auto-generated method stub
    //
    //        doAudit();
    //
    //      }
    //
    //    });
    //
    //    unAuditButton.addActionListener(new ActionListener() {
    //
    //      @Override
    //      public void actionPerformed(ActionEvent e) {
    //
    //        // TCJLODO Auto-generated method stub
    //
    //        doUnAudit();
    //
    //      }
    //
    //    });

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

    bidButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doBid();

      }

    });

    unBidButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doUnBid();

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

  private void doAdd() {

    stopTableEditing();

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;

        }

      }

    }

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

    ZcEbSignup signup = new ZcEbSignup();

    setDefualtValue(signup, ZcSettingConstants.PAGE_STATUS_NEW);

    listCursor.setCurrentObject(signup);

    setEditingObject(signup);

    refreshData();

    updateFieldEditorsEditable();

    setButtonStatus();

  }

  private void doEdit() {

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

    updateFieldEditorsEditable();

    setButtonStatus();

  }

  private void doDelete() {

    ZcEbSignup signup = (ZcEbSignup) this.listCursor.getCurrentObject();

    int num = JOptionPane.showConfirmDialog(this, "确认删除当前数据？", "删除确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      /**

       * 删除供应报名的时候，要校验已经缴纳保证金的供应商，保证金是否已经退回

       */

      if ("1".equals(signup.getIsPayGuarantee())) {

        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + "该供应商的保证金未退回，不允许删除，", "错误", JOptionPane.ERROR_MESSAGE);

        return;

      }

      boolean success = true;

      String errorInfo = "";

      try {

        success = this.zcEbSignupServiceDelegate.deleteSignupFN(signup, this.requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        JOptionPane.showMessageDialog(self, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

        //          this.doNext();

        listCursor.removeCurrentObject();

        //          listCursor.next();

        refreshData();

        //          setButtonStatus();

        this.listPanel.refreshCurrentTabData();

        updateFieldEditorsEditable();

        setButtonStatus();

      } else {

        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

  }

  private void doAudit() {

    doSave();

    ZcEbSignup signup = (ZcEbSignup) this.listCursor.getCurrentObject();

    signup.setStatus("1");

    signup.setOperator(requestMeta.getSvUserName());

    boolean success = true;

    String errorInfo = "";

    try {

      this.zcEbSignupServiceDelegate.updateZcEbSignupFN(signup, this.requestMeta);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      this.listPanel.refreshCurrentTabData();

      this.oldSignup = (ZcEbSignup) ObjectUtil.deepCopy(signup);

      JOptionPane.showMessageDialog(self, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      updateFieldEditorsEditable();

      setButtonStatus();

    } else {

      JOptionPane.showMessageDialog(this, "审核失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  public boolean checkPackBidEndTime(ZcEbSignup signup, boolean isBid) {
    Date sysDate = (Date) zcEbBaseServiceDelegate.queryObject("ZcEbPlan.getSysdate", null, requestMeta);
    StringBuffer msg = new StringBuffer();
    if (sysDate.compareTo(signup.getPlan().getSellEndTime()) >= 0) {
      if (isBid) {
        msg.append("报名截止时间已过，不能报名");
      } else {
        msg.append("报名截止时间已过，不能撤销报名报名");
      }
    }
    if (msg.toString().length() > 0) {
      JOptionPane.showMessageDialog(this, "报名失败 ！\n" + msg.toString(), "错误", JOptionPane.ERROR_MESSAGE);
      return false;
    } else {
      return true;
    }

  }

  private List getPackPlanListByPackCode(String projCode) {

    ElementConditionDto dto = new ElementConditionDto();

    dto.setProjCode(projCode);

    return this.zcEbBaseServiceDelegate.queryDataForList("ZcEbSignup.getZcEbPackPlanResult", dto, requestMeta);
  }

  private void resetZcEbSignupPack(ZcEbSignupPackDetail dbPack, List pageList) {

    for (int i = 0; i < pageList.size(); i++) {

      ZcEbSignupPackDetail pgPack = (ZcEbSignupPackDetail) pageList.get(i);

      if (pgPack.getPackCode().equals(dbPack.getPackCode())) {

        pgPack.setSignupPackId(dbPack.getSignupPackId());

        pgPack.setSpStatus(ZcValSetConstants.VAL_ZC_VS_SIGNUP_STATUS_SIGNUPED);
      }
    }
  }

  private boolean isExistPage(ZcEbSignupPackDetail dbPack, List pageList) {

    for (int i = 0; i < pageList.size(); i++) {

      ZcEbSignupPackDetail pgPack = (ZcEbSignupPackDetail) pageList.get(i);

      if (pgPack.getPackCode().equals(dbPack.getPackCode())) {

        return true;
      }
    }

    return false;
  }

  private Date getPackPlanBidEndTime(String packCode, List packPlanLst) {

    for (int j = 0; j < packPlanLst.size(); j++) {

      ZcEbPackPlan packPlan = (ZcEbPackPlan) packPlanLst.get(j);

      boolean isEqual = packPlan.getPackCode().equals(packCode);

      if (isEqual)
        return packPlan.getSellEndTime();//校验的是点击报名的截止时间
    }

    return null;
  }

  private void doUnAudit() {

    ZcEbSignup signup = (ZcEbSignup) this.listCursor.getCurrentObject();

    signup.setStatus("0");

    boolean success = true;

    String errorInfo = "";

    try {

      this.zcEbSignupServiceDelegate.updateZcEbSignupFN(signup, this.requestMeta);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      this.listPanel.refreshCurrentTabData();

      this.oldSignup = (ZcEbSignup) ObjectUtil.deepCopy(signup);

      JOptionPane.showMessageDialog(self, "销审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      updateFieldEditorsEditable();

      setButtonStatus();

    } else {

      JOptionPane.showMessageDialog(this, "审核失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

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

        listCursor.setCurrentObject(oldSignup);

      }

    }

    listCursor.previous();

    refreshData();

    setButtonStatus();

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

        listCursor.setCurrentObject(oldSignup);

      }

    }

    listCursor.next();

    refreshData();

    setButtonStatus();

  }

  public void doBid() {

    if (!checkBeforeSave())
      return;

    ZcEbSignup signup = (ZcEbSignup) this.listCursor.getCurrentObject();
    //    if (signup.getSignupId() == null || signup.getSignupId().length() == 0) {
    //      JOptionPane.showMessageDialog(this.parent, "请先保存数据再进行报名", "提示", JOptionPane.WARNING_MESSAGE);
    //      return;
    //    }

    int flag = JOptionPane.showConfirmDialog(this.parent, "确定要报名吗？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

    if (flag != 0)
      return;
    //校验邀请招标的项目是否被邀请
    if (!checkInvited())
      return;

    if (!checkPackBidEndTime(signup, true))
      return;

    if (!checkSignupAudited(signup))
      return;

    if (!checkBullinBid()) {

      JOptionPane.showMessageDialog(this.parent, "尚未发布招标公告，不能报名", "提示", JOptionPane.WARNING_MESSAGE);

      return;
    }

    signup.setStatus(ZcValSetConstants.VAL_ZC_VS_SIGNUP_STATUS_SIGNUPED);

    boolean success = true;

    String errorInfo = "";

    //处理保证金为0的情况

    List packs = signup.getSignupPacks();

    ElementConditionDto dto = new ElementConditionDto();

    dto.setProjCode(signup.getProjCode());

    List list = zcEbSignupServiceDelegate.getProjPack(dto, requestMeta);
    boolean havePackSelected = false;
    for (int i = 0; i < packs.size(); i++) {

      ZcEbSignupPackDetail detail = (ZcEbSignupPackDetail) packs.get(i);

      //      detail.setSpStatus(ZcValSetConstants.VAL_ZC_VS_SIGNUP_PACK_STATUS_SIGNUPED);
      //
      if (ZcValSetConstants.VAL_ZC_VS_SIGNUP_PACK_STATUS_SIGNUPED.equals(detail.getSpStatus())) {
        detail.setSpDate(requestMeta.getTransDate());
        havePackSelected = true;
      }

      for (int j = 0; j < list.size(); j++) {

        ZcEbPack zcebPack = (ZcEbPack) list.get(j);

        if (ZcValSetConstants.VAL_ZC_VS_SIGNUP_PACK_STATUS_SIGNUPED.equals(detail.getSpStatus())
          && detail.getPackCode().equals(zcebPack.getPackCode()) && zcebPack.getBidDeposit().signum() == 0) {

          signup.setIsPayGuarantee("1");

          detail.setIsPayGuarantee("1");

        }
        if (ZcEbSignupPackDetail.BAOMING_AUDIT_NO.equalsIgnoreCase(detail.getCheckResult())
          && ZcEbSignupPackDetail.BAOMING_YES.equalsIgnoreCase(detail.getSpStatus())) {//报名审核不通过，将其报名状态置为否
          detail.setSpStatus(ZcEbSignupPackDetail.BAOMING_AUDIT_NO);
        }

      }

    }
    if (!havePackSelected) {
      JOptionPane.showMessageDialog(self, "请选择要参与投标的分包，下拉框选择报名后，在进行提交，", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      requestMeta.setFuncId(this.bidButton.getFuncId());

      signup = this.zcEbSignupServiceDelegate.saveFN(signup, requestMeta);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      this.listCursor.setCurrentObject(signup);

      this.oldSignup = (ZcEbSignup) ObjectUtil.deepCopy(signup);

      this.listPanel.refreshCurrentTabData();

      JOptionPane.showMessageDialog(self, "报名成功！如果需要资质审核，必须通过资质审核报名才有效，如有疑问，请联系采购中心", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      //      updateFieldEditorsEditable();

      setButtonStatus();

      setOldObject();

      refreshData();

      updateFieldEditorsEditable();

      return;

    } else {

      JOptionPane.showMessageDialog(this, "报名失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      return;

    }
  }

  /**
   * 报名已经审核的情况下，不能进行报名
   * @param signup
   * @return
   */
  private boolean checkSignupAudited(ZcEbSignup signup) {
    // TCJLODO Auto-generated method stub

    List packs = signup.getSignupPacks();
    for (int i = 0; i < packs.size(); i++) {
      ZcEbSignupPackDetail detail = (ZcEbSignupPackDetail) packs.get(i);
      if ("N".equalsIgnoreCase(detail.getCheckResult()) && "Y".equalsIgnoreCase(detail.getSpStatus())) {//报名审核不通过，还要进行报名的情况，将其报名状态置为否
        detail.setSpStatus("N");
      }
    }
    return true;
  }

  public void doUnBid() {

    int flag = JOptionPane.showConfirmDialog(this.parent, "确定要撤回报名吗？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

    if (flag != 0)
      return;

    //    if (checkSubmit()) {
    //        return;
    //      }

    ZcEbSignup signup = (ZcEbSignup) this.listCursor.getCurrentObject();
    if (signup.getSignupId() == null || signup.getSignupId().length() == 0) {
      JOptionPane.showMessageDialog(this.parent, "请先保存数据再进行撤回报名", "提示", JOptionPane.WARNING_MESSAGE);
      return;
    }

    if (!checkPackBidEndTime(signup, false)) {

      return;
    }

    signup.setStatus(ZcValSetConstants.VAL_ZC_VS_SIGNUP_STATUS_CANCEL);
    List packs = signup.getSignupPacks();
    for (int i = 0; i < packs.size(); i++) {

      ZcEbSignupPackDetail detail = (ZcEbSignupPackDetail) packs.get(i);

      detail.setSpStatus(ZcValSetConstants.VAL_ZC_VS_SIGNUP_PACK_STATUS_CANCEL);
    }

    boolean success = true;

    String errorInfo = "";

    try {
      requestMeta.setFuncId(this.unBidButton.getFuncId());

      this.zcEbSignupServiceDelegate.unBidZcEbSignupFN(signup, this.requestMeta);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      this.listPanel.refreshCurrentTabData();

      this.listCursor.setCurrentObject(signup);

      this.oldSignup = (ZcEbSignup) ObjectUtil.deepCopy(signup);

      JOptionPane.showMessageDialog(self, "撤回报名成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      updateFieldEditorsEditable();

      setButtonStatus();

      refreshData();

    } else {

      JOptionPane.showMessageDialog(this, "撤回报名失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }
  }

  public void doExit() {

    stopTableEditing();
    /*
        if (isDataChanged()) {

          int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

          if (num == JOptionPane.YES_OPTION) {

            if (!doSave()) {

              return;

            }

          }

        }*/

    this.parent.dispose();

  }

  public boolean doSave() {
    ZcEbSignup signup = (ZcEbSignup) this.listCursor.getCurrentObject();
    //    if(!checkPackBidEndTime(signup, true)||!checkPackBidEndTime(signup, false)){ 
    //      return false;      
    //    }

    if (!isDataChanged()) {

      JOptionPane.showMessageDialog(self, "数据未发生变化，不需要保存！", "提示", JOptionPane.INFORMATION_MESSAGE);

      return false;

    }

    if (!checkBeforeSave())

      return false;

    if (!checkInvited())
      return false;

    Date sysDate = (Date) zcEbBaseServiceDelegate.queryObject("ZcEbPlan.getSysdate", null, requestMeta);
    if (sysDate.compareTo(signup.getPlan().getSellEndTime()) >= 0) {
      int num = JOptionPane.showConfirmDialog(this, "该项目已经过了开标时间了，还需要更新报名信息吗？", "更新报名", 0);
      if (num == JOptionPane.NO_OPTION) {
        return false;
      }
    }
    boolean success = true;

    String errorInfo = "";

    //处理保证金为0的情况

    List packs = signup.getSignupPacks();

    ElementConditionDto dto = new ElementConditionDto();

    dto.setProjCode(signup.getProjCode());

    List list = zcEbSignupServiceDelegate.getProjPack(dto, requestMeta);

    for (int i = 0; i < packs.size(); i++) {

      ZcEbSignupPackDetail detail = (ZcEbSignupPackDetail) packs.get(i);

      for (int j = 0; j < list.size(); j++) {

        ZcEbPack zcebPack = (ZcEbPack) list.get(j);

        if (detail.getPackCode().equals(zcebPack.getPackCode()) && zcebPack.getBidDeposit().signum() == 0) {

          signup.setIsPayGuarantee("1");

          detail.setIsPayGuarantee("1");

        }
        if (ZcEbSignupPackDetail.BAOMING_AUDIT_NO.equalsIgnoreCase(detail.getCheckResult())
          && ZcEbSignupPackDetail.BAOMING_YES.equalsIgnoreCase(detail.getSpStatus())) {//报名审核不通过，将其报名状态置为否
          detail.setSpStatus(ZcEbSignupPackDetail.BAOMING_AUDIT_NO);
        }
        if (ZcEbSignupPackDetail.BAOMING_AUDIT_YES.equalsIgnoreCase(detail.getCheckResult())
          && (ZcEbSignupPackDetail.BAOMING_NO.equalsIgnoreCase(detail.getSpStatus()) || detail.getSpStatus() == null)) {
          //报名审核通过，将其报名状态置为是
          //这么做是为了供应商报名后审核后，供应商不能再修改报名信息，只能由采购中心的人修改
          detail.setSpStatus(ZcEbSignupPackDetail.BAOMING_YES);
        }
      }

    }

    try {
      requestMeta.setFuncId(this.saveButton.getFuncId());

      signup = this.zcEbSignupServiceDelegate.saveFN(signup, requestMeta);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      this.listCursor.setCurrentObject(signup);

      this.oldSignup = (ZcEbSignup) ObjectUtil.deepCopy(signup);

      this.listPanel.refreshCurrentTabData();

      JOptionPane.showMessageDialog(self, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      updateFieldEditorsEditable();

      setButtonStatus();

      setOldObject();

      refreshData();

      return true;

    } else {

      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      return false;

    }

  }

  private boolean checkBeforeSave() {

    ZcEbSignup curObj = (ZcEbSignup) this.listCursor.getCurrentObject();

    //    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {
    //
    //      ZcEbSignup oldObj = this.zcEbSignupServiceDelegate.getZcEbSignupByIdProvider(curObj, this.requestMeta);
    //
    //      if (oldObj != null) {
    //
    //        //        JOptionPane.showMessageDialog(this.parent, "供应商\"" + curObj.getProviderName() + "\"已经报过名了，请检查！", "提示", JOptionPane.WARNING_MESSAGE);
    //        //
    //        //        return false;
    //
    //      }
    //
    //    }

    List<ZcEbSignupPackDetail> packs = curObj.getSignupPacks();
    if (packs == null || packs.size() == 0) {

      JOptionPane.showMessageDialog(this.parent, "请添加报名的标段！", "提示", JOptionPane.WARNING_MESSAGE);

      return false;

    }

    String dupli = "";

    for (int i = 0; i < packs.size(); i++) {

      ZcEbSignupPackDetail pack = packs.get(i);

      for (int j = i + 1; j < packs.size(); j++) {

        ZcEbSignupPackDetail pack2 = packs.get(j);

        if (pack.getPackCode().equals(pack2.getPackCode())) {

          dupli = dupli + "\n" + pack.getPackCode() + " " + pack.getPackName();

          break;

        }

      }

    }

    if (dupli.length() > 0) {

      JOptionPane.showMessageDialog(this.parent, dupli + "重复了！", "提示", JOptionPane.WARNING_MESSAGE);

      return false;

    }

    List notNullBillElementList = this.billElementMeta.getNotNullBillElement();

    List notNullDetailBillElementList = this.detailBillElementMeta.getNotNullBillElement();

    ZcEbSignup signup = (ZcEbSignup) this.listCursor.getCurrentObject();

    StringBuilder errorInfo = new StringBuilder();

    String validateInfo = ZcUtil.validateBillElementNull(signup, notNullBillElementList);

    String validateDetailInfo = ZcUtil.validateDetailBillElementNull(signup.getSignupPacks(), notNullDetailBillElementList, false);

    if (validateInfo.length() != 0) {

      errorInfo.append("").append(validateInfo.toString()).append("\n");

    }

    if (validateDetailInfo.length() != 0) {

      //errorInfo.append("").append(validateDetailInfo.toString()).append("\n");

      errorInfo.append("").append("必须选择标段").append("\n");

    }

    if (errorInfo.length() != 0) {

      JOptionPane.showMessageDialog(this.parent, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);

      return false;

    }

    return true;

  }

  private boolean checkInvited() {

    ZcEbSignup obj = (ZcEbSignup) this.listCursor.getCurrentObject();

    StringBuilder sb = new StringBuilder();

    if (obj.getPurType() != null && obj.getPurType().equals(ZcSettingConstants.PITEM_OPIWAY_YQZB)) {

      String providerCode = requestMeta.getSvUserID();

      List lst = obj.getSignupPacks();

      List lstPackSup = zcEbBaseServiceDelegate.queryDataForList("ZcEbProj.getZcEbPackSupListByProjCode", obj.getProjCode(), requestMeta);

      for (int i = 0; i < lst.size(); i++) {

        ZcEbSignupPackDetail pack = (ZcEbSignupPackDetail) lst.get(i);

        if (!isInPack(pack.getPackCode(), providerCode, lstPackSup)) {

          sb.append(pack.getPackName()).append(",");
        }

      }

      if (sb.length() > 0) {

        JOptionPane.showMessageDialog(this.parent, "包：" + sb.substring(0, sb.length() - 1) + " 没有接受邀请，不能报名！", "提示", JOptionPane.WARNING_MESSAGE);

        return false;
      }

    }

    return true;

  }

  private boolean isInPack(String packCode, String providerCode, List lstPackSup) {

    for (int i = 0; i < lstPackSup.size(); i++) {

      ZcEbPackSupplier sup = (ZcEbPackSupplier) lstPackSup.get(i);

      if (packCode.equals(sup.getPackCode()) && providerCode.equals(sup.getProviderCode())) {

        return true;
      }
    }

    return false;
  }

  private boolean checkBullinBid() {

    ZcEbSignup obj = (ZcEbSignup) this.listCursor.getCurrentObject();
    //单一来源的采购方式不用校验公告是否发布
    if (obj.getPurType() != null && obj.getPurType().equals(ZcSettingConstants.PITEM_OPIWAY_DYLY)) {
      return true;
    }
    ElementConditionDto dto = new ElementConditionDto();

    dto.setProjCode(obj.getProjCode());

    dto.setBulletinType(ZcEbBulletinConstants.TYPE_BULLETIN_BID);

    dto.setStatus(ZcSettingConstants.BILL_STATUS_AUDITED);

    List bullinList = this.zcEbBaseServiceDelegate.queryDataForList(this.bulletinSqlMapSelectId, dto, requestMeta);

    if (bullinList == null || bullinList.size() < 1) {
      //JOptionPane.showMessageDialog(this.parent, "招标公告尚未发出，暂时不能报名，请稍等", "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  private boolean checkSubmit() {

    ZcEbSignup obj = (ZcEbSignup) this.listCursor.getCurrentObject();

    if ("Y".equals(obj.getIsSubmitBidDoc())) {

      JOptionPane.showMessageDialog(this.parent, "标书已上传，不能撤回！", "提示", JOptionPane.WARNING_MESSAGE);

      return true;
    }

    return false;
  }

  /**
   * 不需要资质审核的项目，报名后即可下载招标文件
   * 需要审核的项目，审核通过过才可以下载招标文件
   */
  private void setZbFileEnabled() {

    ZcEbSignup obj = (ZcEbSignup) this.listCursor.getCurrentObject();
    if (obj.getSignupPacks() == null || obj.getSignupPacks().size() == 0)
      return;
    boolean isPassed = false;
    boolean isZizhiCheck = false;
    boolean isSignup=false;
    for (int i = 0; i < obj.getSignupPacks().size(); i++) {
      ZcEbSignupPackDetail d = (ZcEbSignupPackDetail) obj.getSignupPacks().get(i);
      if ("Y".equals(d.getIsCheckQualification()) ) {//要求资质检查
        isZizhiCheck = true;
        if (ZcEbSignupPackDetail.BAOMING_YES.equals(d.getSpStatus())) {//报名了
          isSignup=true;
          if(ZcEbSignupPackDetail.BAOMING_AUDIT_YES.equals(d.getCheckResult())){//资质审核通过了
            isPassed = true;
            break;
          }
        }
      }
    }

    if (!"1".equals(obj.getStatus())) {
      zbwjEditor.setEnabled(false);
      zbwjEditor.setDownButtonEnabled(false);
      zbwjWordEditor.setEnabled(false);
      zbwjWordEditor.setDownButtonEnabled(false);      
    } else {
      if(isZizhiCheck){
        if(isSignup){
          if(isPassed){
            zbwjEditor.setDownButtonEnabled(true);
            zbwjWordEditor.setDownButtonEnabled(true);                
          }else{
            zbwjEditor.setEnabled(false);
            zbwjEditor.setDownButtonEnabled(false);
            zbwjWordEditor.setEnabled(false);
            zbwjWordEditor.setDownButtonEnabled(false);                  
          }
        }else{
          zbwjEditor.setEnabled(false);
          zbwjEditor.setDownButtonEnabled(false);
          zbwjWordEditor.setEnabled(false);
          zbwjWordEditor.setDownButtonEnabled(false);                 
        }
      }else{
        zbwjEditor.setDownButtonEnabled(true);
        zbwjWordEditor.setDownButtonEnabled(true);        
      }
    }
    /*
    if (isZizhiCheck) {
      if(isPassed){
        zbwjEditor.setDownButtonEnabled(true);
        zbwjWordEditor.setDownButtonEnabled(true);
      }else{
        zbwjEditor.setEnabled(false);
        zbwjEditor.setDownButtonEnabled(false);
        zbwjWordEditor.setEnabled(false);
        zbwjWordEditor.setDownButtonEnabled(false);        
      }
    } else {
      if (!"1".equals(obj.getStatus())) {
        zbwjEditor.setEnabled(false);
        zbwjEditor.setDownButtonEnabled(false);
        zbwjWordEditor.setEnabled(false);
        zbwjWordEditor.setDownButtonEnabled(false);
      } else {
        zbwjEditor.setDownButtonEnabled(true);
        zbwjWordEditor.setDownButtonEnabled(true);
      }
    }*/

  }

  public void doHelp() {

    setButtonStatus();

  }

  private void stopTableEditing() {

    JPageableFixedTable table = this.tablePanel.getTable();

    if (table.isEditing()) {

      table.getCellEditor().stopCellEditing();

    }

  }

  public boolean isDataChanged() {

    return !DigestUtil.digest(oldSignup).equals(DigestUtil.digest(listCursor.getCurrentObject()));

  }

}
