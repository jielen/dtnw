package com.ufgov.zc.client.zc.project.control;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.activeztb.TbDocService;
import com.ufgov.zc.client.zc.eval.ZcEbEvalParamSetPanel;
import com.ufgov.zc.client.zc.eval.result.ZcEbEvalReportEditPanel;
import com.ufgov.zc.client.zc.eval.result.ZcEbEvalResultPortalPanel;
import com.ufgov.zc.client.zc.projectlivingchange.ZcProjectLivingChangeUIFrame;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.EvalExpert;
import com.ufgov.zc.common.zc.model.EvalItemType;
import com.ufgov.zc.common.zc.model.EvalPackProvider;
import com.ufgov.zc.common.zc.model.ZcEbEvalPack;
import com.ufgov.zc.common.zc.model.ZcEbEvalReport;
import com.ufgov.zc.common.zc.model.ZcEbExpertEvalResult;
import com.ufgov.zc.common.zc.model.ZcEbFormula;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbProjChange;
import com.ufgov.zc.common.zc.model.ZcEbSupplierPack;

public class ControlAction extends AbstractAction implements ActionListener {

  private static final long serialVersionUID = -378207550605588153L;
  
  private static final Logger logger=Logger.getLogger(ControlAction.class);

  public Object component;

  public String actionId;

  private final ZcEbProjectControlSubEditPanel editPanel;

  /**
   * @return the requestMeta
   */
  public RequestMeta getRequestMeta() {
    return editPanel.requestMeta;
  }

  public ControlAction(String actionId, String actionName, ZcEbProjectControlSubEditPanel editPanel) {
    this.actionId = actionId;
    this.editPanel = editPanel;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      Method methods = getClass().getDeclaredMethod(actionId, new Class[] {});
      methods.invoke(this, new Object[] {});
    } catch (SecurityException e1) {
      e1.printStackTrace();
    } catch (NoSuchMethodException e1) {
      e1.printStackTrace();
    } catch (InvocationTargetException ee) {
      String msg = ee.getTargetException().getMessage();
      GV.showMessageDialog(null, GV.getSimpleMsg("operatorNotCorrect") + (msg == null || "".equals(msg) ? "不详." : msg));
      ee.printStackTrace();
    } catch (IllegalArgumentException e1) {
      e1.printStackTrace();
    } catch (IllegalAccessException e1) {
      e1.printStackTrace();
    }
  }
  private void doRefresh() {

    
  }
  private void doSaveOffLineEvalBid() {

    int num = JOptionPane.showConfirmDialog(editPanel.parent, "确认保存开标信息吗？", "操作确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      boolean success = true;
      String errorInfo="";      
      try{
        String msg= editPanel.zcEbEvalServiceDelegate.saveOffLineEvalBidFN(editPanel.panelService.getJoinBidSupplierList(),editPanel.requestMeta);
      }catch (Exception e) {
        logger.error(e.getMessage(), e);
        success = false;
        errorInfo += e.getMessage();
      }
      if(success){
        JOptionPane.showMessageDialog(editPanel.parent, "保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        return;        
      }else{
        JOptionPane.showMessageDialog(editPanel.parent, "保存失败\n"+errorInfo, "提示", JOptionPane.ERROR_MESSAGE);
        return;         
      }
    } else {
      return;
    }
  }

  private void doOpenBid() {
    if (!editPanel.toCheckHavingBidProvider()) {
      JOptionPane.showMessageDialog(editPanel.parent, "没有供应商参与当前标段的投标，无法进行开标...", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    int num = JOptionPane.showConfirmDialog(editPanel.parent, "确认当前标段进入【开标状态】吗？", "操作确认", 0);
    if (num == JOptionPane.YES_OPTION) {

      if (doUpdatePackStatus(ZcSettingConstants.PACK_STATUS_OPEN_BID)) {
        JOptionPane.showMessageDialog(editPanel.parent, "您好，当前标段已经进入【开标状态】，可以进行【开标唱标】了...", "提示", JOptionPane.INFORMATION_MESSAGE);
        editPanel.doUpdateButtonsEnable();
      }
    } else {
      return;
    }
  }

  private void doConformityBid() {
    if (!editPanel.toCheckHavingBidProvider()) {
      JOptionPane.showMessageDialog(editPanel.parent, "还没有进行开标，无法进行符合性评审......", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    int num = JOptionPane.showConfirmDialog(editPanel.parent, "确认当前标段进入【符合性评标】吗？", "操作确认", 0);
    if (num == JOptionPane.YES_OPTION) {

      if (doUpdatePackStatus(ZcSettingConstants.PACK_STATUS_FU_HE_EVAL)) {
        JOptionPane.showMessageDialog(editPanel.parent, "您好，当前标段已经进入【符合性评标】，专家可以进行【符合性评标】了...", "提示", JOptionPane.INFORMATION_MESSAGE);
        editPanel.doUpdateButtonsEnable();
      }
    } else {
      return;
    }
  }

  private void doTechnicalBid() {

    //判断是否保存符合性评审结果
    ZcEbPack pack = editPanel.currVO.getZcEbPack();
    Map<String, String> paraMap = new HashMap<String, String>();
    paraMap.put("PROJ_CODE", pack.getProjCode());
    paraMap.put("PACK_CODE", pack.getPackCode());
    paraMap.put("IS_COMPLIANCE_RESULT", "Y");
    //获得最终的汇总结果
    List list = editPanel.zcEbEvalServiceDelegate.getZcEbEvalPackSumResult(paraMap, editPanel.requestMeta);
    if (list == null || list.size() == 0) {
      JOptionPane.showMessageDialog(editPanel.parent, "请先【汇总符合性评审】结果，再进入【技术性评标】！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    int num = JOptionPane.showConfirmDialog(editPanel.parent, "确认当前标段进入【技术性评标】吗？", "操作确认", 0);
    if (num == JOptionPane.YES_OPTION) {

      if (doUpdatePackStatus(ZcSettingConstants.PACK_STATUS_TECH_EVAL)) {
        JOptionPane.showMessageDialog(editPanel.parent, "您好，当前标段已经进入【评分性评标】，请先设置评审参数，然后专家就可以进行【评分性评标】了...", "提示", JOptionPane.INFORMATION_MESSAGE);
        editPanel.doUpdateButtonsEnable();
      }
    } else {
      return;
    }

  }

  private void doFinishBid() {
    /**

     * 1、不进行打分的项目，必须要先汇总符合性评审结果，才可以评标结束。

     * 2、要进行打分的项目，必须要先汇总技术性评审结果，才可以结束评标。

     * 3、存在问题：采购方式现场变更的情况,现场采购方式进行了变更，但是评标方法没有变更,不容易控制

     */

    int num = JOptionPane.showConfirmDialog(editPanel.parent, "确认当前标段进入【评标结束】吗？", "操作确认", 0);
    if (num == JOptionPane.YES_OPTION) {

      if (doUpdatePackStatus(ZcSettingConstants.PACK_STATUS_EVAL_COMPLETE)) {
        JOptionPane.showMessageDialog(editPanel.parent, "您好，当前标段已经成功进入【评标结束】状态...\n 评标结束后专家评审数据不允许修改!", "提示", JOptionPane.INFORMATION_MESSAGE);
        editPanel.doUpdateButtonsEnable();
        deleteZTBFile(editPanel.currVO.getProjCode());
      }
    } else {
      return;
    }

  }

  private void doRejectBid() {

    int num = JOptionPane.showConfirmDialog(editPanel.parent, "确认将当前标段【废标】吗？", "操作确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      if (doUpdatePackStatus(ZcSettingConstants.PACK_STATUS_CANCEL)) {
        JOptionPane.showMessageDialog(editPanel.parent, "您好，当前标段已经被【废标】...", "提示", JOptionPane.INFORMATION_MESSAGE);
        editPanel.doUpdateButtonsEnable();
      }
    } else {
      return;
    }
  }

  private void doPauseBid() {
    int num = JOptionPane.showConfirmDialog(editPanel.parent, "确认将当前标段进入【暂停】吗？", "操作确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      if (doUpdatePackStatus(ZcSettingConstants.PACK_STATUS_SUSPENDED)) {
        JOptionPane.showMessageDialog(editPanel.parent, "您好，当前标段已经成功进入【暂停】状态...", "提示", JOptionPane.INFORMATION_MESSAGE);
        editPanel.doUpdateButtonsEnable();
      }
    } else {
      return;
    }
  }

  private void doRecoverBid() {
    String history = editPanel.currVO.getZcEbPack().getLastStatus();
    String temp = AsValDataCache.getName("VS_ZC_PACK_STATUS", editPanel.currVO.getZcEbPack().getLastStatus());
    int num = JOptionPane.showConfirmDialog(editPanel.parent, "确认将当前标段状态恢复至暂停前状态【" + temp + "】吗？", "操作确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      if (doUpdatePackStatus(history)) {
        JOptionPane.showMessageDialog(editPanel.parent, "您好，当前标段已经成功恢复至【暂停】前的【" + temp + "】状态...", "提示", JOptionPane.INFORMATION_MESSAGE);
        editPanel.doUpdateButtonsEnable();
      }
    } else {
      return;
    }
  }

  private void doEcbj() {
    editPanel.panelService.ecbjPanel.doEcbj(true);
  }

  private void doChangeCgTypeButton() {
    final ZcEbPack pack = editPanel.currVO.getZcEbPack();
    if (pack != null) {
      //评标结束、废标、暂停、延期不能改采购方式
      if ("4".equals(pack.getStatus()) || "5".equals(pack.getStatus()) || "6".equals(pack.getStatus()) || "7".equals(pack.getStatus())) {
        JOptionPane.showMessageDialog(editPanel.parent, "当前状态下不能进行变更采购方式操作", "提示", JOptionPane.INFORMATION_MESSAGE);
        editPanel.changeCgTypeButton.setEnabled(false);
        return;
      }
      final ZcProjectLivingChangeUIFrame frame = new ZcProjectLivingChangeUIFrame(pack) {
        @Override
        public void afterChange() {
          // TCJLODO Auto-generated method stub
          super.afterChange();
          editPanel.currVO.setZcEbPack(pack);
          editPanel.setEditingObject(editPanel.currVO);
          editPanel.updateChangeStatusButton(editPanel.currVO.getZcEbPack());
          //          editPanel.updateFieldEditors();
        }
      };
      makeTheChange(frame, pack);
      frame.toFront();
      frame.setFocusable(true);
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
          //            makeTheChange(frame, pack);
        }
      });
    }
  }

  private void makeTheChange(ZcProjectLivingChangeUIFrame frame, ZcEbPack pack) {
    ElementConditionDto dto = new ElementConditionDto();
    dto.setPackCode(pack.getPackCode());
    dto.setProjCode(pack.getEntrust().getZcMakeCode());
    dto.setWfcompoId("ZC_EB_PROJ_CHG");
    List list = editPanel.zcEbProjChangeServiceDelegate.getZcEbProjChangeList(dto, editPanel.requestMeta);
    if (list != null && list.size() > 0) {
      ZcEbProjChange projChange = (ZcEbProjChange) list.get(0);
      pack.setPurType(projChange.getPurType());
      if (frame != null) {
        frame.getChangeReasionJEditorPane().setText(projChange.getChangeReasonMemo());
      }
    }
  }

  /**
   * @return void 返回类型
   * @Description: 在评标结束时，要删除本地下载的招投标文件，这个放在项目控制台进行控制。
   * @since 1.0
   */
  private void deleteZTBFile(String projCode) {
    String filePath = ZcUtil.getZcFileTempDir() + File.separator + ZcUtil.ZTB_FILE_DIR + File.separator + projCode;
    TbDocService.deleteFile(new File(filePath));
  }

  private void doShowEvalResult() {
    try {
      ZcEbPack zcEbPack = editPanel.currVO.getZcEbPack();
      String iePath = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";
      String url = WorkEnv.getInstance().getWebRoot() + "app/page/eval/toEvalExpertResultPrint.do?projCode=" + zcEbPack.getProjCode()
        + "&packCode=" + zcEbPack.getPackCode() + "&zcAgeyName=" + zcEbPack.getEntrust().getAgencyName();
      ZcUtil.anyBrowse(url);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(editPanel.parent, "打印预览出错！\n" + e.getMessage(), "错误", 0);
    }
  }

  private void doShowBjResult() {
    try {
      ZcEbPack zcEbPack = editPanel.currVO.getZcEbPack();
      String iePath = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";
      String url = WorkEnv.getInstance().getWebRoot() + "app/page/bj/toProviderBjPrint.do?projCode=" + zcEbPack.getProjCode() + "&packCode="
        + zcEbPack.getPackCode() + "&zcAgeyName=" + zcEbPack.getEntrust().getAgencyName();
      ZcUtil.anyBrowse(url);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(editPanel.parent, "打印预览出错！\n" + e.getMessage(), "错误", 0);
    }
  }

  private void doShowYlbResult() {
    try {
      ZcEbPack zcEbPack = editPanel.currVO.getZcEbPack();
      String iePath = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";
      String url = WorkEnv.getInstance().getWebRoot() + "app/page/bj/toProviderYlbPrint.do?projCode=" + zcEbPack.getProjCode() + "&packCode="
        + zcEbPack.getPackCode() + "&zcAgeyName=" + zcEbPack.getEntrust().getAgencyName();
      ZcUtil.anyBrowse(url);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(editPanel.parent, "打印预览出错！\n" + e.getMessage(), "错误", 0);
    }
  }

  private void doShowEvalResultDetal() {
    ZcEbPack zcEbPack = editPanel.currVO.getZcEbPack();
    String expertCode = null;
    String expertName = null;
    JGroupableTable table = editPanel.panelService.supplierExpertEvalTabPanel.getTable();
    BeanTableModel model = (BeanTableModel) table.getModel();
    List list = model.getDataBeanList();
    Integer[] checkedRows = table.getCheckedRows();
    if (checkedRows.length == 0) {
      JOptionPane.showMessageDialog(editPanel.parent, "没有选择专家！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    if (checkedRows.length > 1) {
      JOptionPane.showMessageDialog(editPanel.parent, "只能选择一条专家数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    for (Integer checkedRow : checkedRows) {
      int accordDataRow = table.convertRowIndexToModel(checkedRow);
      ZcEbExpertEvalResult bean = (ZcEbExpertEvalResult) list.get(accordDataRow);
      expertCode = bean.getExpertCode();
      expertName = bean.getExpertName();
    }
    try {
      String iePath = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";
      String url = WorkEnv.getInstance().getWebRoot() + "app/page/eval/toEvalExpertResultDetailPrint.do?projCode=" + zcEbPack.getProjCode()
        + "&packCode=" + zcEbPack.getPackCode() + "&zcAgeyName=" + zcEbPack.getEntrust().getAgencyName() + "&expertCode=" + expertCode
        + "&expertName=" + expertName;
      ZcUtil.anyBrowse(url);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(editPanel.parent, "打印预览出错！\n" + e.getMessage(), "错误", 0);
    }
  }

  private void doShowEvalComplResult() {
    try {
      ZcEbPack zcEbPack = editPanel.currVO.getZcEbPack();
      String iePath = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";
      String url = WorkEnv.getInstance().getWebRoot() + "app/page/eval/toEvalExpertComplResultPrint.do?projCode=" + zcEbPack.getProjCode()
        + "&packCode=" + zcEbPack.getPackCode() + "&zcAgeyName=" + zcEbPack.getEntrust().getAgencyName();
      ZcUtil.anyBrowse(url);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(editPanel.parent, "打印预览出错！\n" + e.getMessage(), "错误", 0);
    }
  }

  private void doShowEvalComplResultDetal() {
    ZcEbPack zcEbPack = editPanel.currVO.getZcEbPack();
    String expertCode = null;
    String expertName = null;
    JGroupableTable table = editPanel.panelService.supplierExpertEvalTabPanel.getTable();
    BeanTableModel model = (BeanTableModel) table.getModel();
    List list = model.getDataBeanList();
    Integer[] checkedRows = table.getCheckedRows();
    if (checkedRows.length == 0) {
      JOptionPane.showMessageDialog(editPanel.parent, "没有选择专家！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    if (checkedRows.length > 1) {
      JOptionPane.showMessageDialog(editPanel.parent, "只能选择一条专家数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    for (Integer checkedRow : checkedRows) {
      int accordDataRow = table.convertRowIndexToModel(checkedRow);
      ZcEbExpertEvalResult bean = (ZcEbExpertEvalResult) list.get(accordDataRow);
      expertCode = bean.getExpertCode();
      expertName = bean.getExpertName();
    }
    try {
      String iePath = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";
      String url = WorkEnv.getInstance().getWebRoot() + "app/page/eval/toEvalExpertComplResultDetailPrint.do?projCode=" + zcEbPack.getProjCode()
        + "&packCode=" + zcEbPack.getPackCode() + "&zcAgeyName=" + zcEbPack.getEntrust().getAgencyName() + "&expertCode=" + expertCode
        + "&expertName=" + expertName;
      ZcUtil.anyBrowse(url);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(editPanel.parent, "打印预览出错！\n" + e.getMessage(), "错误", 0);
    }
  }

  public void doSetParam() {
    ZcEbEvalPack evalPack = new ZcEbEvalPack();
    ZcEbPack zcEbPack = editPanel.currVO.getZcEbPack();
    evalPack.setPackCode(zcEbPack.getPackCode());
    evalPack.setPackName(zcEbPack.getPackName());
    evalPack.setProjCode(zcEbPack.getProjCode());
    if (editPanel.zcEbFormula == null) {
      JOptionPane.showMessageDialog(editPanel.parent, "评审方法没有设置！", "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
    evalPack.setFormulaCode(editPanel.zcEbFormula.getFormulaCode());
    GkBaseDialog dialog = new GkBaseDialog(editPanel.parent, "设置评审参数", Dialog.ModalityType.APPLICATION_MODAL);
    ZcEbEvalParamSetPanel panel = new ZcEbEvalParamSetPanel(evalPack, dialog, "EDIT");
    dialog.add(panel);
    dialog.setSize(UIConstants.DIALOG_2_LEVEL_WIDTH, UIConstants.DIALOG_2_LEVEL_HEIGHT);
    dialog.moveToScreenCenter();
    dialog.setVisible(true);
  }

  public void doCreateEvalReport() {
    ZcEbPack cPack = editPanel.currVO.getZcEbPack();
    ZcEbEvalReport zcEbEvalReport;
    Map map = new HashMap();
    map.put("projCode", cPack.getProjCode());
    map.put("packCode", cPack.getPackCode());
    List<ZcEbEvalReport> list = editPanel.zcEbBaseServiceDelegate
      .queryDataForList("ZcEbEval.getZcEbEvalReportByPackCode", map, editPanel.requestMeta);
    //校验评审报告是否已经存在。
    if (list != null && list.size() > 0) {
      zcEbEvalReport = list.get(0);
    } else {
      zcEbEvalReport = new ZcEbEvalReport();
      zcEbEvalReport.setStatus("0");
      zcEbEvalReport.setExecutor(editPanel.requestMeta.getSvUserID());
      zcEbEvalReport.setExecutorName(editPanel.requestMeta.getSvUserName());
      zcEbEvalReport.setExecuteDate(editPanel.requestMeta.getSysDate());
      zcEbEvalReport.setNd(editPanel.requestMeta.getSvNd());
      zcEbEvalReport.setProjCode(cPack.getProjCode());
      zcEbEvalReport.setPackCode(cPack.getPackCode());
      zcEbEvalReport.setPackName(cPack.getPackName());
      zcEbEvalReport.setProjName(cPack.getEntrust().getZcMakeName());
      zcEbEvalReport.setCoCode(cPack.getEntrust().getCoCode());
      zcEbEvalReport.setAgency(editPanel.requestMeta.getSvCoCode());
      zcEbEvalReport.setOrgCode(editPanel.requestMeta.getSvOrgCode());
      if (!"".equals(zcEbEvalReport.getCoCode()) && zcEbEvalReport.getCoCode() != null) {
        zcEbEvalReport.setCoName(editPanel.baseDataServiceDelegate.getCompanyByCoCode(editPanel.requestMeta.getSvNd(), zcEbEvalReport.getCoCode(),
          editPanel.requestMeta).getName());
      }
      zcEbEvalReport.setZcCoLinkMan(cPack.getEntrust().getZcMakeLinkman());
      zcEbEvalReport.setAgency(cPack.getEntrust().getAgency());
      zcEbEvalReport.setBidLocation(editPanel.currVO.getZcEbPlan().getOpenBidAddress());
      zcEbEvalReport.setBidDate(editPanel.currVO.getZcEbPlan().getOpenBidTime());
      zcEbEvalReport.setPurType(editPanel.currVO.getZcEbPack().getPurType());
      zcEbEvalReport.setBidLocation(editPanel.currVO.getZcEbPlan().getOpenBidAddress());
      zcEbEvalReport.setBidDate(editPanel.currVO.getZcEbPlan().getOpenBidTime());
    }
    ListCursor listCursor = new ListCursor();
    listCursor.setDataList(new ArrayList<ZcEbEvalReport>());
    listCursor.setCurrentObject(zcEbEvalReport);
    listCursor.getDataList().add(zcEbEvalReport);
    GkBaseDialog dialog = new GkBaseDialog(editPanel.parent, "生成评审报告", Dialog.ModalityType.APPLICATION_MODAL);
    ZcEbEvalReportEditPanel panel = new ZcEbEvalReportEditPanel(dialog, listCursor, zcEbEvalReport);
    dialog.add(panel);
    dialog.setSize(UIConstants.DIALOG_0_LEVEL_WIDTH, UIConstants.DIALOG_0_LEVEL_HEIGHT);
    dialog.moveToScreenCenter();
    dialog.setVisible(true);
  }

  private void doComplSum() {
    ZcEbEvalPack evalPack = new ZcEbEvalPack();
    ZcEbPack zcEbPack = editPanel.currVO.getZcEbPack();
    evalPack.setPackCode(zcEbPack.getPackCode());
    evalPack.setPackName(zcEbPack.getPackName());
    evalPack.setProjCode(zcEbPack.getProjCode());
    ElementConditionDto dto = new ElementConditionDto();
    dto.setPackCode(zcEbPack.getPackCode());
    dto.setProjCode(zcEbPack.getProjCode());
    evalPack.setFormulaCode(editPanel.zcEbFormulaServiceDelegate.getZcEbFormulaByPackCode(dto, editPanel.requestMeta).getFormulaCode());
    EvalExpert evalExpert = new EvalExpert();
    evalExpert.setExpertCode(editPanel.requestMeta.getSvUserID());
    evalExpert.setExpertName(editPanel.requestMeta.getEmpName());
    //校验所有专家符合性评审结果是否都已评完。
    String str = evalIsComplement(EvalItemType.COMPLIANICE, evalPack);
    if (!"".equals(str)) {
      JOptionPane.showMessageDialog(editPanel.parent, str, "提示", JOptionPane.ERROR_MESSAGE);
      return;
    }
    final ZcEbEvalResultPortalPanel panel = new ZcEbEvalResultPortalPanel(evalExpert, evalPack, false);
    GkBaseDialog dialog = new GkBaseDialog(editPanel.parent, "汇总符合性评审结果", Dialog.ModalityType.APPLICATION_MODAL) {
      @Override
      public void closeDialog() {
        if (panel.finalResultPanel.doExit()) {
          super.closeDialog();
        }
      }
    };
    dialog.add(panel);
    dialog.setMaxSizeWindow();
    dialog.moveToScreenCenter();
    dialog.setVisible(true);
  }

  private void doCallComplSum() {
    ZcEbPack pack = editPanel.currVO.getZcEbPack();

    int num = JOptionPane.showConfirmDialog(editPanel.parent, "确认当前标段进入【符合性评标】吗？", "操作确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      doUpdatePackStatus(ZcSettingConstants.PACK_STATUS_FU_HE_EVAL);
      //删除评审结果表中的垃圾数据
      int num2 = JOptionPane.showConfirmDialog(editPanel.parent, "是否清除技术打分数据", "操作确认", 0);
      if (num2 == JOptionPane.YES_OPTION) {
        Map map = new HashMap();
        map.put("projCode", pack.getProjCode());
        map.put("packCode", pack.getPackCode());
        map.put("itemType", EvalItemType.SCORE);
        boolean success = true;
        try {
          editPanel.zcEbEvalServiceDelegate.deleteEvalItemResultList(map, editPanel.requestMeta);
          //删除汇总打分表里的记录
          map.put("IS_COMPLIANCE_RESULT", "N");
          editPanel.zcEbEvalServiceDelegate.deleteZcEbPackEvalFinalSumResult(map, editPanel.requestMeta);
        } catch (Exception e) {
          success = false;
          e.printStackTrace();
          return;
        }
        if (success) {
          JOptionPane.showMessageDialog(editPanel.parent, "处理成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    } else {
      return;
    }
  }

  private void doScoreSum() {
    ZcEbEvalPack evalPack = new ZcEbEvalPack();
    ZcEbPack zcEbPack = editPanel.currVO.getZcEbPack();
    evalPack.setPackCode(zcEbPack.getPackCode());
    evalPack.setPackName(zcEbPack.getPackName());
    evalPack.setProjCode(zcEbPack.getProjCode());
    ElementConditionDto dto = new ElementConditionDto();
    dto.setPackCode(zcEbPack.getPackCode());
    dto.setProjCode(zcEbPack.getProjCode());
    ZcEbFormula formula = editPanel.zcEbFormulaServiceDelegate.getZcEbFormulaByPackCode(dto, editPanel.requestMeta);
    if (formula == null) {
      JOptionPane.showMessageDialog(editPanel.parent, "没有找到相应的评标规则明细，无法汇总，请确认是否录入了评标规则...", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    evalPack.setFormulaCode(formula.getFormulaCode());
    EvalExpert evalExpert = new EvalExpert();
    evalExpert.setExpertCode(editPanel.requestMeta.getSvUserID());
    evalExpert.setExpertName(editPanel.requestMeta.getEmpName());
    //校验所有专家符合性评审结果是否都已评完。
    String str = evalIsComplement(EvalItemType.SCORE, evalPack);
    if (!"".equals(str)) {
      JOptionPane.showMessageDialog(editPanel.parent, str, "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    final ZcEbEvalResultPortalPanel panel = new ZcEbEvalResultPortalPanel(evalExpert, evalPack, true);
    GkBaseDialog dialog = new GkBaseDialog(editPanel.parent, "汇总技术性评分结果", Dialog.ModalityType.APPLICATION_MODAL) {
      @Override
      public void closeDialog() {
        if (panel.finalResultPanel.doExit()) {
          super.closeDialog();
        }
      }
    };
    dialog.add(panel);
    dialog.setMaxSizeWindow();
    dialog.moveToScreenCenter();
    dialog.setVisible(true);
  }

  /**
   * @return boolean 返回类型
   * @Description: 判断专家的评审情况, 只有所有专家的评审结果都提交了，才可以进行汇总。
   */
  private String evalIsComplement(String itemType, ZcEbEvalPack zcEbEvalPack) {
    StringBuffer resultStr = new StringBuffer();
    ElementConditionDto dto = new ElementConditionDto();
    dto.setPackCode(zcEbEvalPack.getPackCode());
    dto.setProjCode(zcEbEvalPack.getProjCode());
    dto.setZcText1(itemType);
    Map map = new HashMap();
    map.put("PROJ_CODE", zcEbEvalPack.getProjCode());
    map.put("PACK_CODE", zcEbEvalPack.getPackCode());
    map.put("ITEM_TYPE", itemType);
    map.put("FORMULA_CODE", zcEbEvalPack.getFormulaCode());
    if (itemType.equals(EvalItemType.SCORE)) {
      map.put("FILTER_BY_COMPLIANCE_RES", "Y");
    } else {
      map.put("FILTER_BY_COMPLIANCE_RES", "N");
    }
    List<EvalExpert> evalExpertList = editPanel.zcEbEvalServiceDelegate.getZcEbEvalExpertList(dto, editPanel.requestMeta);
    loop: for (int j = 0; j < evalExpertList.size(); j++) {
      map.remove("EVAL_EXPERT_CODE");
      map.put("EVAL_EXPERT_CODE", evalExpertList.get(j).getExpertCode());
      List<EvalPackProvider> evalProviderList = editPanel.zcEbEvalServiceDelegate.getEvalPackProviderList(map, editPanel.requestMeta);
      for (int i = 0; i < evalProviderList.size(); i++) {
        if (!evalProviderList.get(i).isEval()) {
          resultStr.append("<html><b><font size='3' color='red'>" + "专家：" + evalExpertList.get(j).getExpertName() + "评审未完成!</font></b></html>\n");
          continue loop;
        }
      }
    }
    return resultStr.toString();
  }

  private boolean doUpdatePackStatus(String newStatus) {
    if (newStatus == null) {
      return false;
    }
    if (editPanel.currVO.getZcEbPlan().getOpenBidTime() != null) {
      SimpleDateFormat sdf = new SimpleDateFormat(ZcSettingConstants.SIMPLE_DATE_FORMAT_FULL);
      Date currTime = new Date();
      try {
        if (currTime.before(editPanel.currVO.getZcEbPlan().getOpenBidTime())) {
          JOptionPane.showMessageDialog(editPanel.parent, "当前标段还没有到指定的开标时间" + sdf.format(editPanel.currVO.getZcEbPlan().getOpenBidTime())
            + "，无法操作...", "提示", JOptionPane.INFORMATION_MESSAGE);
          return false;
        }
      } catch (Exception e) {
        JOptionPane.showMessageDialog(editPanel.parent, "日期格式不正确，无法比较开标时间和当前时间...", "提示", JOptionPane.INFORMATION_MESSAGE);
        return false;
      }
    } else {
      JOptionPane.showMessageDialog(editPanel.parent, "对应的采购计划不存在，无法确定标段开标时间，请检查数据一致性...", "提示", JOptionPane.INFORMATION_MESSAGE);
      return false;
    }
    ZcEbPack packed = null;
    boolean success = true;
    String errorInfo = "";
    try {
      ZcEbPack pack = editPanel.currVO.getZcEbPack();
      if (ZcSettingConstants.PACK_STATUS_CANCEL.equals(newStatus)) {
        if (pack.getFailedReason() == null || "".equals(pack.getFailedReason())) {
          String tip = "请填写【" + "分包信息" + "】页签下【失败原因】项，\n然后再进行【废标】！";
          JOptionPane.showMessageDialog(editPanel.parent, tip, "提示", JOptionPane.INFORMATION_MESSAGE);
          return false;
        }
        if (!editPanel.listPanel.zcEbProjectServiceDelegate.checkStatus(pack, editPanel.requestMeta)) {
          JOptionPane.showMessageDialog(editPanel.parent, "该分包已经制作合同，不能进行【废标】", "提示", JOptionPane.INFORMATION_MESSAGE);
          return false;
        }
      }
      pack.setLastStatus(pack.getStatus());
      pack.setStatus(newStatus);
      editPanel.listPanel.zcEbProjectServiceDelegate.updateZcEbPack(pack, this.editPanel.requestMeta);
      packed = pack;
    } catch (Exception e) {
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      editPanel.refreshAll(editPanel.currVO, false);
      editPanel.updateChangeStatusButton(packed);
      return true;
    } else {
      JOptionPane.showMessageDialog(editPanel.parent, "保存失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

}
