/**   
 * @(#) project: zcxa
 * @(#) file: ZcPProBalService.java
 * 
 * Copyright 2010 UFGOV, Inc. All rights reserved.
 * UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package com.ufgov.zc.server.zc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.kingdrive.workflow.context.WorkflowContext;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.NumLimConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.DataExchangeException;
import com.ufgov.zc.common.system.model.AsWfDraft;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.model.ZcPProBal;
import com.ufgov.zc.common.zc.model.ZcPProBalBi;
import com.ufgov.zc.common.zc.model.ZcPProReturnBi;
import com.ufgov.zc.common.zc.model.ZcPayGkInfo;
import com.ufgov.zc.server.budget.util.BudgetUtil;
import com.ufgov.zc.server.payInterface.util.PayForZcUtil;
import com.ufgov.zc.server.system.SpringContext;
import com.ufgov.zc.server.system.dao.IWorkflowDao;
import com.ufgov.zc.server.system.util.AsOptionUtil;
import com.ufgov.zc.server.system.util.NumLimUtil;
import com.ufgov.zc.server.system.util.NumUtil;
import com.ufgov.zc.server.system.workflow.WFEngineAdapter;
import com.ufgov.zc.server.zc.ZcSUtil;
import com.ufgov.zc.server.zc.dao.IBaseDao;
import com.ufgov.zc.server.zc.dao.IDataExchangeDao;
import com.ufgov.zc.server.zc.dao.IZcEbBaseServiceDao;
import com.ufgov.zc.server.zc.dao.IZcPProBalBiDao;
import com.ufgov.zc.server.zc.dao.IZcPProBalDao;
import com.ufgov.zc.server.zc.service.IZcEbBaseService;
import com.ufgov.zc.server.zc.service.IZcPProBalService;

/**
 * @ClassName: ZcPProBalService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date: 2010-8-2 下午06:29:01
 * @version: V1.0
 * @since: 1.0
 * @author: Administrator
 * @modify:
 */
public class ZcPProBalService implements IZcPProBalService {

  private WFEngineAdapter wfEngineAdapter;

  private IWorkflowDao workflowDao;

  private IZcPProBalDao zcPProBalDao;

  private IZcPProBalBiDao zcPProBalBiDao;

  private IBaseDao baseDao;

  private IZcEbBaseService zcEbBaseServiceF3;

  /**
   * @return the baseDao
   */
  public IBaseDao getBaseDao() {
    return baseDao;
  }

  /**
   * @param baseDao
   *            the baseDao to set
   */
  public void setBaseDao(IBaseDao baseDao) {
    this.baseDao = baseDao;
  }

  public IZcEbBaseService getZcEbBaseServiceF3() {
    return zcEbBaseServiceF3;
  }

  public void setZcEbBaseServiceF3(IZcEbBaseService zcEbBaseServiceF3) {
    this.zcEbBaseServiceF3 = zcEbBaseServiceF3;
  }

  public WFEngineAdapter getWfEngineAdapter() {
    return wfEngineAdapter;
  }

  public void setWfEngineAdapter(WFEngineAdapter wfEngineAdapter) {
    this.wfEngineAdapter = wfEngineAdapter;
  }

  public IZcPProBalBiDao getZcPProBalBiDao() {
    return zcPProBalBiDao;
  }

  public void setZcPProBalBiDao(IZcPProBalBiDao zcPProBalBiDao) {
    this.zcPProBalBiDao = zcPProBalBiDao;
  }

  public IZcPProBalDao getZcPProBalDao() {
    return zcPProBalDao;
  }

  public void setZcPProBalDao(IZcPProBalDao zcPProBalDao) {
    this.zcPProBalDao = zcPProBalDao;
  }

  public IWorkflowDao getWorkflowDao() {
    return workflowDao;
  }

  public void setWorkflowDao(IWorkflowDao workflowDao) {
    this.workflowDao = workflowDao;
  }

  public List getZcPProBalList(ElementConditionDto dto, RequestMeta requestMeta) {
    dto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(dto.getWfcompoId(), NumLimConstants.FWATCH));
    List list = zcPProBalDao.getZcPProBalList(dto);
    ZcSUtil.setBillDBDigest(list);
    return list;

  }

  public ZcPProBal updateZcPProBal(ZcPProBal zcPProBal, RequestMeta requestMeta) {
    if ("".equals(ZcSUtil.safeString(zcPProBal.getZcBalId())) || "自动编号".equals(ZcSUtil.safeString(zcPProBal.getZcBalId()))) {
      String userId = requestMeta.getSvUserID();
      String compoId = requestMeta.getCompoId();
      String code = NumUtil.getInstance().getNo(compoId, "ZC_BAL_ID", zcPProBal);
      Long draftid = workflowDao.createDraftId();
      zcPProBal.setZcBalId(code);
      zcPProBal.setProcessInstId(draftid);
      zcPProBalDao.saveZcPProBal(zcPProBal);
      AsWfDraft asWfDraft = new AsWfDraft();
      asWfDraft.setCompoId(compoId);
      asWfDraft.setWfDraftName(code);
      asWfDraft.setUserId(userId);
      asWfDraft.setMasterTabId(compoId);
      asWfDraft.setWfDraftId(BigDecimal.valueOf(draftid.longValue()));
      workflowDao.insertAsWfdraft(asWfDraft);
      List biList = zcPProBal.getBiList();
      for (int i = 0; i < biList.size(); i++) {
        ZcPProBalBi bi = (ZcPProBalBi) biList.get(i);
        bi.setZcBalId(code);
        bi.setZcCode(zcPProBal.getZcMakeCode());
        // 插入
        zcPProBalBiDao.insertZcPProBalBi(bi);
      }
      List returnBiList = zcPProBal.getReturnBiList();
      if (returnBiList != null && returnBiList.size() > 0) {
        for (int i = 0; i < returnBiList.size(); i++) {
          ZcPProReturnBi returnBi = (ZcPProReturnBi) returnBiList.get(i);
          returnBi.setZcBalId(code);
          // 插入
          baseDao.insert("ZC_P_PRO_RETURN_BI.abatorgenerated_insert", returnBi);
        }
      }
    } else {
      String id = zcPProBal.getZcBalId();
      ZcPProBal originalBean = this.selectByPrimaryKey(id, "N");
      ZcSUtil.checkDigest(zcPProBal, originalBean, "zcBalId");// 一致性校验
      // 修改
      zcPProBalDao.updateZcPProBal(zcPProBal);
      zcPProBalBiDao.deleteZcPProBalBiByBalId(id);
      List biList = zcPProBal.getBiList();
      for (int i = 0; i < biList.size(); i++) {
        ZcPProBalBi bi = (ZcPProBalBi) biList.get(i);
        bi.setZcBalId(id);
        bi.setZcCode(zcPProBal.getZcMakeCode());
        zcPProBalBiDao.insertZcPProBalBi(bi);
      }
      List returnBiList = zcPProBal.getReturnBiList();
      if (returnBiList != null && returnBiList.size() > 0) {
        baseDao.delete("ZC_P_PRO_RETURN_BI.abatorgenerated_deleteByZcBalId", id);
        for (int i = 0; i < returnBiList.size(); i++) {
          ZcPProReturnBi returnBi = (ZcPProReturnBi) returnBiList.get(i);
          returnBi.setZcBalId(id);
          // 插入
          baseDao.insert("ZC_P_PRO_RETURN_BI.abatorgenerated_insert", returnBi);
        }
      }
    }
    return zcPProBal;
  }

  public void deleteByPrimaryKey(String zcBalId) {
    zcPProBalDao.deleteZcPProBal(zcBalId);
    zcPProBalBiDao.deleteZcPProBalBiByBalId(zcBalId);
    //同步到另一个网端
    insertDeleteInfoToDataExchage(zcBalId);
  }

  private void insertDeleteInfoToDataExchage(String zcBalId) {
    // TODO Auto-generated method stub
    //同步到另一个网端        
      DataExchangeRedo redo = new DataExchangeRedo();
      redo.setDataTypeID("ZC_P_PRO_BAL");
      redo.setDataTypeName("资金支付");
      ElementConditionDto dto = new ElementConditionDto();     
      redo.setRecordID(zcBalId);
      redo.setRecordName("删除资金支付");
      redo.setMasterTableName("ZC_P_PRO_BAL");
      redo.setIsExported(DataExchangeRedo.STATUS_WAITING_EXPORTED);
      redo.setGenerateDate(new Date());
      redo.setOperateType("fdelete");
      IDataExchangeDao dataExchangeDao=(IDataExchangeDao)SpringContext.getBean("dataExchangeDao");
      dataExchangeDao.deleteByRecordIDAndDataTypeID(redo);
      dataExchangeDao.saveRedo(redo);
  }

  public void updateAfterPaySuccess(ZcPProBal zcPProBal, RequestMeta requestMeta) {

    zcPProBal.setZcBalStatus("sendGk");
    baseDao.update("ZC_P_PRO_BAL.updateZcPProBalStatus", zcPProBal);
  }

  public ZcPProBal selectByPrimaryKey(String zcBalId, String isFrozen) {
    ZcPProBal zcPProBal = (ZcPProBal) zcPProBalDao.selectByPrimaryKey(zcBalId);
    if(zcPProBal==null)return null;
    List balBiList = zcPProBalBiDao.getZcPProBalBiList(zcBalId);
    List returnBiList = baseDao.query("ZC_P_PRO_RETURN_BI.selectZcPProReturnBiList", zcBalId);
    //获取当前合同未支付金额：合同金额-所有支付申请金额之和(不含cancel状态的金额)
    if(zcPProBal.getZcHtCode()!=null){
      Double canPaySum=(Double)baseDao.read("ZC_P_PRO_BAL.getCanPayHtSumByHtCode", zcPProBal.getZcHtCode());
      if(canPaySum==null){
        zcPProBal.setCanPaySum(new BigDecimal(0));
      }else{
        zcPProBal.setCanPaySum(new BigDecimal(canPaySum.doubleValue()));
      }
    }
    Map map = new HashMap();
    map.put("zcHtCode", zcPProBal.getZcHtCode());
    for (int i = 0; i < balBiList.size(); i++) {
      ZcPProBalBi bi = (ZcPProBalBi) balBiList.get(i);
      map.put("zcHtBiNo", bi.getZcHtBiNo());
      map.put("zcBalId", zcPProBal.getZcBalId());
      bi.setZcBiYjjsSum(this.getSumZcBalBiSum(map));
    }

    zcPProBal.setBiList(balBiList);
    zcPProBal.setReturnBiList(returnBiList);
    zcPProBal.setDbDigest(null);
    zcPProBal.setDbDigest(zcPProBal.digest());

    return zcPProBal;
  }

  public ZcPProBal callbackFN(ZcPProBal zcPProBal, RequestMeta requestMeta) {
    WorkflowContext workflowContext = wfEngineAdapter.genCommonWFC(zcPProBal.getComment(), zcPProBal, requestMeta);
    wfEngineAdapter.callback(workflowContext);

    return zcPProBal;
  }

  public ZcPProBal newCommitFN(ZcPProBal currentObject, RequestMeta requestMeta) throws Exception {
    wfEngineAdapter.newCommit(currentObject.getComment(), currentObject, requestMeta);
    return selectByPrimaryKey(currentObject.getZcBalId(), "N");
  }

  public BigDecimal getSumZcBalSum(Map map) {
    return zcPProBalDao.getSumZcBalSum(map);
  }

  public BigDecimal getSumZcBalBiSum(Map map) {
    return zcPProBalBiDao.getSumZcBalBiSum(map);
  }

  public ZcPProBal auditFN(ZcPProBal zcPProBal, String isFrozen, RequestMeta requestMeta) {
    zcPProBal= updateZcPProBal(zcPProBal,requestMeta);
    wfEngineAdapter.commit(zcPProBal.getComment(), zcPProBal, requestMeta);
    if ("Y".equals(isFrozen)) {
      zcPProBal.setBiList(zcPProBalBiDao.getZcPProBalBiList(zcPProBal.getZcBalId()));
      zcEbBaseServiceF3.insertF3Pay(zcPProBal);
    }
    return zcPProBal;
  }

  public ZcPProBal unAuditFN(ZcPProBal zcPProBal, RequestMeta requestMeta) {
    wfEngineAdapter.rework(zcPProBal.getComment(), zcPProBal, requestMeta);
    return zcPProBal;
  }

  public ZcPProBal untreadFN(ZcPProBal zcPProBal, RequestMeta requestMeta) {
    wfEngineAdapter.untread(zcPProBal.getComment(), zcPProBal, requestMeta);
    return zcPProBal;
  }

  public boolean sendPay(ZcPProBal zcPProBal, String serverAdd, String isFrozen, RequestMeta requestMeta) {
    Map shifangBudgetMap = null;

    // 支付调用报错后需要再次冻结的指标集合
    // List<Map<String, Object>> updOlds = new ArrayList<Map<String,
    // Object>>();
    List updOlds = new ArrayList();
    BudgetUtil bu=new BudgetUtil();
    // 指标接口和支付接口分开，这样指标接口出错时则不掉用取消结冻指标操作
    try {
      boolean isr = false;
/*      if ("Y".equals(zcPProBal.getIsLastPay())) {
        if (zcPProBal.getReturnBiList() != null && zcPProBal.getReturnBiList().size() > 0) {
          isr = true;
          baseDao.update("ZC_YEAR_END.updateMakeYepFlagOverLastPay", "'" + zcPProBal.getZcMakeCode() + "'");
        } else {
          baseDao.update("ZC_YEAR_END.updateMakeYepFlagLastPay", "'" + zcPProBal.getZcMakeCode() + "'");
        }
      }*/

      zcPProBal= updateZcPProBal(zcPProBal,requestMeta);
      wfEngineAdapter.commit(zcPProBal.getComment(), zcPProBal, requestMeta);

      /*
       * 释放指标金额，本次支付金额10w，已经支付了8w，则应该修改采购计划占用指标金额=原采购计划占用指标金额-10w-8w
       */
      shifangBudgetMap = bu.getBalShifangBudget(baseDao, true, zcPProBal.getZcMakeCode(), zcPProBal.getZcBalId(), isr);
      bu.callShifangService(shifangBudgetMap, requestMeta.getSvNd());
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
    List sucess = new ArrayList();
    try {
      // 生成支付
      Map rtn=new PayForZcUtil().PayByBal(zcPProBal, sucess, requestMeta);
      if(rtn==null){
        //目前没有处理，应该不会出现这种情况
        //throw new RuntimeException(e1.getMessage());
      }else{
        String rtnType=(String) rtn.get(PayForZcUtil.RTN_RESULT);
        String returnMsg=(String) rtn.get(PayForZcUtil.RTN_MESSAGE);
        if(rtnType.equalsIgnoreCase(PayForZcUtil.RTN_RESULT_FALSE)){
          throw new RuntimeException("支付单【" + zcPProBal.getZcBalId() + "】生成支付出错:"+returnMsg);
        }else{//保存支付凭证号与当前支付单的关联关系
          String vouIds=(String) rtn.get(PayForZcUtil.RTN_DATA);
          _savePayVouId(vouIds,zcPProBal,requestMeta);
        }
      }
    } catch (Exception e) {
      try {
        bu.cancelCallService(shifangBudgetMap, requestMeta.getSvNd());
      } catch (Exception e1) {
        System.err.println("支付单【" + zcPProBal.getZcBalId() + "】生成支付出错时，取消结冻的指标出现异常");
        List biLst = zcPProBal.getBiList();
        ZcPProBalBi b = null;
        for (int i = 0; i < biLst.size(); i++) {
          b = (ZcPProBalBi) biLst.get(i);
          System.err.println("合同编号：" + b.getZcHtCode() + "指标编号：" + b.getZcBiNo() + "采购计划或补充合同结冻金额："
            + (b.getZcBiBcjsSum() == null ? "" : b.getZcBiBcjsSum().toString()));
        }
        System.err.println("异常信息如下："+e1.getMessage());
        e1.printStackTrace();
        throw new RuntimeException(e1.getMessage());
      }
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());

    }
    // 支付指标
    return true;
  }

  /**
   * 保存支付单和国库单据的关联信息
   * @param vouIds
   * @param zcPProBal
   * @param requestMeta
   */
  private void _savePayVouId(String vouIds, ZcPProBal zcPProBal,RequestMeta requestMeta) {
    // TODO Auto-generated method stub
    if(vouIds==null)return;
    baseDao.delete("ZC_PAY_GK_INFO.deleteByZcBillId", zcPProBal.getZcBalId());
    StringTokenizer st=new StringTokenizer(vouIds, PayForZcUtil.RTN_DATA_SPLIT);
    String interfaceType=AsOptionUtil.getInstance().getOptionVal(ZcPayGkInfo.GK_INTERFACE_TYPE);
    String vouType=interfaceType.equalsIgnoreCase("Y")?ZcPayGkInfo.GK_VOU_TYPE_ZFPZ:ZcPayGkInfo.GK_VOU_TYPE_ZFSQ;
    while(st.hasMoreTokens()){
      String vouId=st.nextToken();
      ZcPayGkInfo info=new ZcPayGkInfo();
      info.setZcBillType(ZcPayGkInfo.ZC_BILL_TYPE_HT_ZHIFU);
      info.setZcBillId(zcPProBal.getZcBalId());
      info.setGkVouId(vouId);
      info.setGkReturnFlag("N");
      info.setSendGkDate(requestMeta.getSysDate());
      info.setGkVouType(vouType);
      baseDao.insert("ZC_PAY_GK_INFO.insert", info);
    }
  }

  /**
   * 
   * 1、收款人信息变更导致退票： 2、支付单金额变更：同“收款人信息变更”类似处理，但是不能超过指标金额；并由支付系统自动补“增/减”计划。
   */
  public boolean editPay(ZcPProBal zcPProBal, String serverAdd, RequestMeta requestMeta) {
    try {
      new PayForZcUtil().updatePayBillByBal(zcPProBal, serverAdd, requestMeta);
    } catch (Exception e1) {
      e1.printStackTrace();
      return false;
    }
    return true;

  }

  public ZcPProBal untreadToFirstFN(ZcPProBal zcPProBal, RequestMeta requestMeta) {
    // TODO Auto-generated method stub
    wfEngineAdapter.untreadToFirst(zcPProBal.getComment(), zcPProBal, requestMeta);
    return selectByPrimaryKey(zcPProBal.getZcBalId(), "N");
  }

  public void updateAfterPay(ZcPProBal zcPProBal, RequestMeta requestMeta) {
    for (int i = 0; i < zcPProBal.getBiList().size(); i++) {
      ZcPProBalBi bi = (ZcPProBalBi) zcPProBal.getBiList().get(i);
      if (bi.getZcAmBillNo() != null && !"".equals(bi.getZcAmBillNo())) {
        baseDao.update("ZC_P_PRO_BAL_BI.updateZcPProBiZcAmBillNo", bi);
      }
    }

    for (int i = 0; i < zcPProBal.getReturnBiList().size(); i++) {
      ZcPProReturnBi bi = (ZcPProReturnBi) zcPProBal.getReturnBiList().get(i);
      if (bi.getZcAmBillNo() != null && !"".equals(bi.getZcAmBillNo())) {
        baseDao.update("ZC_P_PRO_RETURN_BI.updateZcAmBillNo", bi);
      }
    }
  }

  
  public List queryExportsDatas(ElementConditionDto dto, RequestMeta meta) {
    // TODO Auto-generated method stub
    List idLst=dto.getPmAdjustCodeList();
    if(idLst==null)return null;
    List rtn=new ArrayList();
    for(int i=0;i<dto.getPmAdjustCodeList().size();i++){
      rtn.add(selectByPrimaryKey((String) dto.getPmAdjustCodeList().get(i),null));
    }
    return rtn;

  }

   
  public String importTransDatasFN(ZcPProBal zcPProBal, RequestMeta meta) throws DataExchangeException{
    // TODO Auto-generated method stub
    // TODO Auto-generated method stub
    
  //先检查是否已经送国库支付了，如果已经送国库支付，则提示不能删除，
    ZcPProBal bal=selectByPrimaryKey(zcPProBal.getZcBalId(), null);
    if(bal!=null && ("exec".equalsIgnoreCase(bal.getZcBalStatus()) || "sendGk".equalsIgnoreCase(bal.getZcBalStatus()))){
      throw new DataExchangeException("资金支付单"+zcPProBal.getZcBalId()+"已经送国库支付了，不能再次导入！");
    }   
    String rtn=zcPProBal.getZcBalId()+"导入成功";
     try {
       zcPProBalDao.deleteZcPProBal(zcPProBal.getZcBalId());
       zcPProBalBiDao.deleteZcPProBalBiByBalId(zcPProBal.getZcBalId());

       zcPProBalDao.saveZcPProBal(zcPProBal);
       
       List biList = zcPProBal.getBiList();
       for (int i = 0; i < biList.size(); i++) {
         ZcPProBalBi bi = (ZcPProBalBi) biList.get(i);
         bi.setZcBalId(zcPProBal.getZcBalId());
         bi.setZcCode(zcPProBal.getZcMakeCode());
         zcPProBalBiDao.insertZcPProBalBi(bi);
       }
       
       List returnBiList = zcPProBal.getReturnBiList();
       if (returnBiList != null && returnBiList.size() > 0) {
         baseDao.delete("ZC_P_PRO_RETURN_BI.abatorgenerated_deleteByZcBalId", zcPProBal.getZcBalId());
         for (int i = 0; i < returnBiList.size(); i++) {
           ZcPProReturnBi returnBi = (ZcPProReturnBi) returnBiList.get(i);
           returnBi.setZcBalId(zcPProBal.getZcBalId());
           // 插入
           baseDao.insert("ZC_P_PRO_RETURN_BI.abatorgenerated_insert", returnBi);
         }
       }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      rtn=zcPProBal.getZcBalId()+"导入异常：\n"+e.getMessage();
    }
     return rtn;
  }

 
  public String importDelDataFN(String  id, RequestMeta meta) throws DataExchangeException {
    // TODO Auto-generated method stub
     ZcPProBal bal=selectByPrimaryKey(id, null);
     if(bal==null) return "资金支付单"+id+"同步删除成功！";
     if(bal!=null && ("exec".equalsIgnoreCase(bal.getZcBalStatus()) || "sendGk".equalsIgnoreCase(bal.getZcBalStatus()))){
       throw new DataExchangeException("资金支付单"+bal.getZcBalId()+"已经送国库支付了，不能再次导入！");
     } 
     zcPProBalDao.deleteZcPProBal(bal.getZcBalId());
     zcPProBalBiDao.deleteZcPProBalBiByBalId(bal.getZcBalId());
    return "资金支付单"+bal.getZcBalId()+"同步删除成功！";
  }
}
