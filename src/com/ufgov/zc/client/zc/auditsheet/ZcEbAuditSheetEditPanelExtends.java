/**
 * 
 */
package com.ufgov.zc.client.zc.auditsheet;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.ZcEbAuditSheetToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.event.ValueChangeEvent;
import com.ufgov.zc.client.component.event.ValueChangeListener;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.ZcEbEntrustHandler;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.EntrustFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.model.AsOption;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcEbAuditSheet;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;

/**
 * 另一种批办单，采购中心内部进行了分组，需要选择执行组和执行人
 * @author Administrator
 *
 */
public class ZcEbAuditSheetEditPanelExtends extends ZcEbAuditSheetEditPanel {

  public ZcEbAuditSheetEditPanelExtends(GkBaseDialog dialog, ListCursor listCursor, String tabStatus, ZcEbAuditSheetListPanel listPanel) {
    super(dialog, listCursor, tabStatus, listPanel);
    // TODO Auto-generated constructor stub
  }

  public List<AbstractFieldEditor> createFieldEditors() {

    ZcEbAuditSheet sheet = (ZcEbAuditSheet) listCursor.getCurrentObject();

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    ZcEbEntrustHandler handler = new ZcEbEntrustHandler() {

      public void excute(List selectedDatas) {

        for (Object obj : selectedDatas) {

          ZcEbEntrust m = (ZcEbEntrust) obj;

          auditSheet = (ZcEbAuditSheet) listCursor.getCurrentObject();

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

        auditSheet = (ZcEbAuditSheet) listCursor.getCurrentObject();

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

    EntrustFieldEditor entrustFieldEditor = new EntrustFieldEditor(handler.getSqlId(), entrustDto, 20, handler, handler.getColumNames(),
      LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_TRANS_SN), "zcEbEntrust.zcMakeCode");

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

        ZcEbAuditSheet sheet = (ZcEbAuditSheet) listCursor.getCurrentObject();

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

            // TODO Auto-generated method stub

            ZcEbAuditSheet sheet = (ZcEbAuditSheet) listCursor.getCurrentObject();

            sheet.setSuperintendent(null);

            sheet.setSuperintendentOrgNm(null);

            sheet.setSuperintendentOrg(null);

            sheet.setSuperintendentName(null);

            setEditingObject(sheet);

          }

        });
        */
    superintendentOrg.setEditable(false);

    editorList.add(superintendentOrg);

    //    TextFieldEditor superintendentOrg = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_TRANS_SUPERINTENDENT_ORG),
    //
    //    "superintendentOrgNm");

    // 经办人

    String columNamesJb[] = { "项目经办人" };

    SuperintendentFnHandler superintendentFnHandler = new SuperintendentFnHandler(columNamesJb);

    ElementConditionDto dto = new ElementConditionDto();

    dto.setNd(requestMeta.getSvNd());
    dto.setZcText0(AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_CGZX_CODE));
    dto.setZcText1(WorkEnv.getInstance().getEmpCode());

    xmjbrEditor = new ForeignEntityFieldEditor("ZcEbAuditSheet.getKeShiPersion", dto, 20,

    superintendentFnHandler, columNamesJb, LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_TRANS_SUPERINTENDENT_NAME),

    "superintendentName");

    xmjbrEditor.setEnabled(false);

    editorList.add(xmjbrEditor);

    xmjbrEditor.getField().addValueChangeListener(new ValueChangeListener() {

      @Override
      public void valueChanged(ValueChangeEvent event) {

        // TODO Auto-generated method stub

        if (fzrEditor.getField().getValue() == null) {

          ZcEbAuditSheet sheet = (ZcEbAuditSheet) listCursor.getCurrentObject();

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

    return editorList;

  }

  protected void updateFieldEditorsEditable() {

    if (auditSheet.getProcessInstId() != null && auditSheet.getProcessInstId() < 0) {

      fzrEditor.setEnabled(true);
      superintendentOrg.setEnabled(true);
    }

  }

  protected void doSuggestPass() {

    requestMeta.setFuncId(this.suggestPassButton.getFuncId());

    //项目经办人可以选择
    boolean jbrEnabled = xmjbrEditor.getField().chooseButton.isEnabled();
    //项目负责科室可以选择
    boolean fzcsEnabled = superintendentOrg.getField().chooseButton.isEnabled();

    if (fzcsEnabled) {
      ZcEbAuditSheet sheet = (ZcEbAuditSheet) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      if (sheet.getSuperintendentOrg() == null || "".equals(sheet.getSuperintendentOrg())) {

        JOptionPane.showMessageDialog(this, "请指定该项目的负责组！", "提示", JOptionPane.WARNING_MESSAGE);

        return;
      }
    }

    //用角色和外部实体的可编辑性可校验文件经办人是否已选
    if (!fzcsEnabled && jbrEnabled) {

      ZcEbAuditSheet sheet = (ZcEbAuditSheet) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      if (sheet.getSuperintendent() == null || "".equals(sheet.getSuperintendent())) {

        JOptionPane.showMessageDialog(this, "请指定该项目的采购中心经办人！", "提示", JOptionPane.WARNING_MESSAGE);

        return;

      }
      //      if (sheet.getAttn() == null || "".equals(sheet.getAttn())) {
      //
      //        JOptionPane.showMessageDialog(this, "请选择[开标经办人]！", "提示", JOptionPane.WARNING_MESSAGE);
      //
      //        return;
      //
      //      }
      if (!doSave(false)) {

        return;

      }

    }

    doSuggestPassForOther();

  }
}
