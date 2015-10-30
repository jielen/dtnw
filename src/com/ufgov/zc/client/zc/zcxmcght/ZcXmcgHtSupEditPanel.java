package com.ufgov.zc.client.zc.zcxmcght;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.IntFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;

public class ZcXmcgHtSupEditPanel extends ZcXmcgHtEditPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 2348941176853020607L;

  private ElementConditionDto htDto;

  JFuncToolBar biBottomToolBar = null;
  
  public static String getCompoId() {
    return "ZC_XMCG_HT_SUP";
  }

//  protected String compoId = "ZC_XMCG_HT_SUP";
//是否需要操作指标接口的标志，补充合同需要操作指标
//  protected boolean useBudgetFlag = "Y".equals(AsOptionMeta.getOptVal(ZcSettingConstants.OPT_ZC_USE_BUDGET_INTERFACE));
  
  private BillElementMeta mainBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd(getCompoId());

  public BillElementMeta getMainBillElementMeta() {
    if (mainBillElementMeta == null) {
      mainBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd(getCompoId());
    }
    return mainBillElementMeta;
  }

  public ZcXmcgHtSupEditPanel(GkBaseDialog parent, ListCursor<ZcXmcgHt> listCursor, String tabStatus, ZcXmcgHtSupListPanel listPanel) {
    super(parent, listCursor, tabStatus, listPanel,getCompoId());

  }
  /**
   * 是否补充合同，主合同不需要操作指标，补充合同需要操作指标,补充合同的edit界面，这个方法要重写，返回true
   */
  protected boolean isSubHt(){
    return true;
  }
  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    TextFieldEditor zcMakeName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME), "zcPProMake.zcMakeName");

    MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM),

    "zcPProMake.zcMoneyBiSum");

    String columNames[] = { "项目代码", "采购项目", "供应商名称", "采购方式" };

    ZcEbProjFnHandler projHandler = new ZcEbProjFnHandler(columNames, self);

    ElementConditionDto elementCondtiontDto = new ElementConditionDto();

    elementCondtiontDto.setBillStatus("exec");

    //elementCondtiontDto.setCoCodeFilter(requestMeta.getSvUserID());

    elementCondtiontDto.setCoCode(requestMeta.getSvCoCode());

    ForeignEntityFieldEditor zcMakeCode = new ForeignEntityFieldEditor(getProjectSqlId(), elementCondtiontDto, 20, projHandler,

    columNames, "采购计划编号", "zcMakeCode");

    zcMakeCode.setEditable(false);

    zcMakeName.setEnabled(false);

    zcMoneyBiSum.setEnabled(false);

    AsValFieldEditor zcProjType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FUKUAN_TYPE), "zcCgLeixing",

    "ZC_VS_FUKUAN_TYPE");

    zcProjType.setEnabled(false);

    AutoNumFieldEditor zcHtCode = new AutoNumFieldEditor("补充合同代码", "zcHtCode");

    TextFieldEditor zcHtName = new TextFieldEditor("补充合同名称", "zcHtName");

    AsValFieldEditor zcHtStatus = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_STATUS), "zcHtStatus",

    getHtStatus());

    zcHtStatus.setEnabled(false);

//    zcHtStatus.setVisible(false);

    CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_NAME), "coCode");

    zcCoCode.setEnabled(false);

    IntFieldEditor zcCoCodeNd = new IntFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE_ND), "nd", 4);

    zcCoCodeNd.setEnabled(false);

    if (isGys()) {

      zcCoCodeNd.setVisible(false);

      zcMoneyBiSum.setVisible(false);

    }

    getBiddingWinner();

    ZcXmcgHt ht = (ZcXmcgHt) this.listCursor.getCurrentObject();

    TextFieldEditor zcSuBankName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SU_BANK_NAME), "zcSuBankName");
    TextFieldEditor zcSuAccCode = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SU_ACC_CODE), "zcSuAccCode");

    TextFieldEditor zcSuCode = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SU_BANK_CODE), "zcSuBankCode");

    TextFieldEditor zcSuLinkman = new TextFieldEditor("联系人", "zcSuLinkman");

    zcSuLinkman.setVisible(false);

    //	    getZcSuNameAndOthers(zcSuBankName, zcSuAccCode, zcSuCode, zcSuLinkman);

    TextFieldEditor zcSuName = new TextFieldEditor("中标供应商", "zcSuName");

    zcSuName.setEnabled(false);

    //补充合同和采购合同去掉银行账号，名称
    //	    editorList.add(zcSuBankName);
    //
    //	    editorList.add(zcSuAccCode);

    MoneyFieldEditor zcHtNum = new MoneyFieldEditor(getHtNumLabel(), "zcHtNum");

    //添加工期和附件
    DateFieldEditor zcProLimitStartDate = new DateFieldEditor("工期开始时间", "zcProLimitStartDate");
    DateFieldEditor zcProLimitEndDate = new DateFieldEditor("工期结束时间", "zcProLimitEndDate");
    FileFieldEditor zcImpFile = new FileFieldEditor("电子附件", "zcImpFile", "zcImpFileBlobid");

    AsValFieldEditor zcFukuanType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.ZC_PAY_TYPE), "zcFukuanType", "ZC_VS_PAY_TYPE");

    DateFieldEditor zcSgnDate = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SGN_DATE), "zcSgnDate");

    TextFieldEditor zcDeliveryDate = new TextFieldEditor("交货日期", "zcDeliveryDate");

    TextFieldEditor zcDeliveryType = new TextFieldEditor("交货方式", "zcDeliveryType");

    TextFieldEditor zcDeliveryAddr = new TextFieldEditor("交货地点", "zcDeliveryAddr");

    TextFieldEditor zcMemo = new TextFieldEditor("备注信息", "zcMemo");
    DateFieldEditor zcInputDate = new DateFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_DATE), "zcInputDate");
    zcInputDate.setEnabled(false);


    TextFieldEditor pName = new TextFieldEditor("主合同名称", "pName");

    MoneyFieldEditor pBiSum = new MoneyFieldEditor("主合同金额", "pBiSum");

    String coluNames[] = { "主合同代码", "主合同名称", "主合同金额","采购单位","供应商" };

    ZcPHtHandler htHandler = new ZcPHtHandler(coluNames);

    htDto = new ElementConditionDto();
    if(ZcUtil.isGys()){
      htDto.setZcText4(requestMeta.getSvUserID());
    }
    if(ZcUtil.isYsdw()){
      htDto.setCoCode(requestMeta.getSvCoCode());
    }
    ForeignEntityFieldEditor pCode = new ForeignEntityFieldEditor("ZC_XMCG_HT.getMainHtByMakeCode", htDto, 20, htHandler,

    coluNames, "选择主合同", "zcParhtCode");


    pName.setEnabled(false);
    pBiSum.setEnabled(false);

    AsValFieldEditor isZiChanField = new AsValFieldEditor(LangTransMeta.translate("IS_ZI_CHAN"), "isZiChan","ZC_VS_YN");
    
    /*
    //项目采购才显示中标
    if (ht != null && ZcSettingConstants.PROJECT_BUY_CODE.equals(ht.getZcFukuanType()))
      editorList.add(biddingWinner);

    editorList.add(zcHtStatus);
    
    editorList.add(zcCoCode);
    editorList.add(zcHtName);
    editorList.add(zcProjType);
    editorList.add(zcProLimitStartDate);

    editorList.add(zcSuName);
    editorList.add(zcHtCode);
    editorList.add(zcDeliveryAddr);
    editorList.add(zcProLimitEndDate);

    editorList.add(zcMakeName);
    editorList.add(zcHtNum);
    editorList.add(zcDeliveryDate);
    editorList.add(zcImpFile);

    editorList.add(zcMakeCode);
    editorList.add(zcFukuanType);
    editorList.add(zcDeliveryType);
    editorList.add(zcMemo);

    editorList.add(zcMoneyBiSum);
    editorList.add(zcSgnDate);
    editorList.add(pCode);
    editorList.add(zcInputDate);

    editorList.add(pName);
    editorList.add(pBiSum);
    //  editorList.add(zcCoCodeNd);
*/    

    editorList.add(pCode);
    editorList.add(pName);
    editorList.add(pBiSum);
    editorList.add(zcCoCode);

    editorList.add(zcHtCode);
    editorList.add(zcHtName);
    editorList.add(zcHtNum);
    editorList.add(zcSuName);
    
    editorList.add(zcSgnDate);
    editorList.add(zcDeliveryAddr);
    editorList.add(zcDeliveryDate);
    editorList.add(zcDeliveryType);

    editorList.add(zcImpFile);
    editorList.add(zcMemo);
    editorList.add(zcInputDate);
    editorList.add(zcHtStatus);
    
    if (!isGys()) {
      editorList.add(isZiChanField);
    }
    return editorList;
  }

  public void zcMakeCodeChange() {
    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) getListCursor().getCurrentObject();
    zcXmcgHt.setZcParhtCode("");
    zcXmcgHt.setpName("");
    zcXmcgHt.setpBiSum(BigDecimal.ZERO);
    htDto.setZcMakeCode(zcXmcgHt.getZcMakeCode());
    setEditingObject(zcXmcgHt);
    super.zcMakeCodeChange();
  }

 
  public class ZcPHtHandler implements IForeignEntityHandler {

    private String columNames[];

    public ZcPHtHandler(String columNames[]) {

      this.columNames = columNames;

    }

    //

    public void excute(List selectedDatas) {

      // TODO Auto-generated method stub

      if (selectedDatas != null && selectedDatas.size() > 0) {

        ZcXmcgHt temp = (ZcXmcgHt) selectedDatas.get(0);

        ZcXmcgHt zcXmcgHt = (ZcXmcgHt) getListCursor().getCurrentObject();

        zcXmcgHt.setZcParhtCode(temp.getZcHtCode());
        zcXmcgHt.setpName(temp.getZcHtName());
        zcXmcgHt.setpBiSum(temp.getZcHtNum());
        zcXmcgHt.setZcSuName(temp.getZcSuName());
        zcXmcgHt.setZcSuCode(temp.getZcSuCode());
        zcXmcgHt.setZcZbCode(temp.getZcSuCode());
        zcXmcgHt.setZcZbName(temp.getZcSuName());
        zcXmcgHt.setCoName(temp.getCoName());
        zcXmcgHt.setCoCode(temp.getCoCode());
        
        zcXmcgHt.setProjCode(temp.getProjCode());
        zcXmcgHt.setProjName(temp.getProjName());
        
        zcXmcgHt.setZcMakeCode(temp.getZcPProMake().getZcMakeCode());
        zcXmcgHt.setZcPProMake(temp.getZcPProMake());
        

        setEditingObject(zcXmcgHt);

      }

    }

    public void afterClear() {

      ZcXmcgHt zcXmcgHt = (ZcXmcgHt) getListCursor().getCurrentObject();

      zcXmcgHt.setZcParhtCode("");
      zcXmcgHt.setpName("");
      zcXmcgHt.setpBiSum(BigDecimal.ZERO);
      zcXmcgHt.setZcSuName("");
      zcXmcgHt.setCoName("");
      zcXmcgHt.setCoCode("");
      
      setEditingObject(zcXmcgHt);

    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcXmcgHt rowData = (ZcXmcgHt) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getZcHtCode();

        data[i][col++] = rowData.getZcHtName();

        data[i][col++] = rowData.getZcHtNum();

        data[i][col++] = rowData.getCoName();

        data[i][col++] = rowData.getZcSuName();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        public boolean isCellEditable(int row, int colum) {

          return false;

        }

      };

      return model;

    }

    @Override
    public boolean isMultipleSelect() {

      // TODO Auto-generated method stub

      return false;

    }

  }

  protected String getProjectSqlId() {
    return "selectPage.selectProjectForCaiGouHtSup";
  }

  protected boolean calcLimit(ZcXmcgHt zcXmcgHt) {
   
    if(isLimitSubHtSum()){

      if (zcXmcgHt.getZcHtNum().doubleValue() > zcXmcgHt.getpBiSum().doubleValue() * getLimitPercent().doubleValue() ) {
  
        String msg= "【补充合同金额】不能超过主合同金额的"+getLimitPercent().doubleValue()*100+"%！";
        JOptionPane.showMessageDialog(this, msg, "提示", JOptionPane.WARNING_MESSAGE);
        return false;
      }
    }
    return true;
  }
  @Override
  protected boolean checkBeforeSave(){
    boolean rtn=super.checkBeforeSave();
    if(!rtn){
      return rtn;
    }
    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) this.listCursor.getCurrentObject();
    return calcLimit(zcXmcgHt);
  }
/**
 * 通过系统选项OPT_ZC_SUB_HT_SUM_IS_LIMITED进行判断，如果是y，则进行控制，否则不控制
 * 
 * @return
 */
  private boolean isLimitSubHtSum() {
    // TODO Auto-generated method stub
    
    String s=AsOptionMeta.getOptVal("OPT_ZC_SUB_HT_SUM_IS_LIMITED");
    return s==null?false:(s.equalsIgnoreCase("y")?true:false);
  }
/**
 * 获取补充金额占主合同金额的比例,在OPT_ZC_SUB_HT_SUM_IS_LIMITED为Y时，这个值有效
 * 通过系统选项OPT_ZC_SUB_HT_SUM_PERCENT进行存放，默认是0.1
 * @return
 */
  private BigDecimal getLimitPercent(){
    BigDecimal rtn=new BigDecimal(0.1);
    String s=AsOptionMeta.getOptVal("OPT_ZC_SUB_HT_SUM_PERCENT");
    if(s==null)return rtn;
    rtn=new BigDecimal(s);
    return rtn;
  }
  protected void findMainHt(ZcXmcgHt zcXmcgHt) {
    ZcXmcgHt mainHt = zcXmcgHtServiceDelegate.selectByPrimaryKey(zcXmcgHt.getZcParhtCode(), getRequestMeta());
    if (mainHt != null) {
      zcXmcgHt.setpName(mainHt.getZcHtName());
      zcXmcgHt.setpBiSum(mainHt.getZcHtNum());
      zcXmcgHt.setZcParhtCode(mainHt.getZcHtCode());
    }
  }
 
  protected String getHtNumLabel() {
    return "补充合同金额";
  }

  protected String getHtStatus() {
    return "ZC_VS_HT_SUP_STATUS";
  }

}
