/**
 * 
 */
package com.ufgov.zc.server.zc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.ufgov.zc.common.budget.model.VwBudgetGp;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.DataExchangeException;
import com.ufgov.zc.common.system.model.AsWfDraft;
import com.ufgov.zc.common.zc.exception.ZcPayInterfaceException;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.model.ZcDingdian;
import com.ufgov.zc.common.zc.model.ZcDingdianBi;
import com.ufgov.zc.common.zc.model.ZcDingdianItem;
import com.ufgov.zc.common.zc.model.ZcPayGkInfo;
import com.ufgov.zc.server.budget.util.BudgetUtil;
import com.ufgov.zc.server.payInterface.util.PayForZcUtil;
import com.ufgov.zc.server.system.SpringContext;
import com.ufgov.zc.server.system.dao.IWorkflowDao;
import com.ufgov.zc.server.system.util.AsOptionUtil;
import com.ufgov.zc.server.system.util.NumUtil;
import com.ufgov.zc.server.system.workflow.WFEngineAdapter;
import com.ufgov.zc.server.zc.ZcSUtil;
import com.ufgov.zc.server.zc.dao.IBaseDao;
import com.ufgov.zc.server.zc.dao.IDataExchangeDao;
import com.ufgov.zc.server.zc.dao.ZcDingdianBiMapper;
import com.ufgov.zc.server.zc.dao.ZcDingdianItemMapper;
import com.ufgov.zc.server.zc.dao.ZcDingdianMapper;
import com.ufgov.zc.server.zc.service.IZcDingDianService;

/**
 * @author Administrator
 *
 */
public class ZcDingDianService implements IZcDingDianService {

  private IBaseDao baseDao;
  private IWorkflowDao workflowDao;
  private ZcDingdianMapper ddDao;
  private ZcDingdianBiMapper ddBiDao;
  private ZcDingdianItemMapper ddItemDao;
  public IBaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(IBaseDao baseDao) {
    this.baseDao = baseDao;
  }

  public IWorkflowDao getWorkflowDao() {
    return workflowDao;
  }

  public void setWorkflowDao(IWorkflowDao workflowDao) {
    this.workflowDao = workflowDao;
  }

  public ZcDingdianMapper getDdDao() {
    return ddDao;
  }

  public void setDdDao(ZcDingdianMapper ddDao) {
    this.ddDao = ddDao;
  }

  public ZcDingdianBiMapper getDdBiDao() {
    return ddBiDao;
  }

  public void setDdBiDao(ZcDingdianBiMapper ddBiDao) {
    this.ddBiDao = ddBiDao;
  }

  public ZcDingdianItemMapper getDdItemDao() {
    return ddItemDao;
  }

  public void setDdItemDao(ZcDingdianItemMapper ddItemDao) {
    this.ddItemDao = ddItemDao;
  }

  public WFEngineAdapter getWfEngineAdapter() {
    return wfEngineAdapter;
  }

  public void setWfEngineAdapter(WFEngineAdapter wfEngineAdapter) {
    this.wfEngineAdapter = wfEngineAdapter;
  }

  private WFEngineAdapter wfEngineAdapter;
  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#getMainLst(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public List getMainLst(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {
    List list = ddDao.getMainLst(elementConditionDto);
    ZcSUtil.setBillDBDigest(list);
    return list;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#cancelFn(com.ufgov.zc.common.zc.model.ZcDingdian, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public void cancelFn(ZcDingdian bill, RequestMeta requestMeta) {
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#unAuditFN(com.ufgov.zc.common.zc.model.ZcDingdian, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public ZcDingdian unAuditFN(ZcDingdian bill, RequestMeta requestMeta) {
    return null;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#untreadFN(com.ufgov.zc.common.zc.model.ZcDingdian, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public ZcDingdian untreadFN(ZcDingdian bill, RequestMeta requestMeta) {
    wfEngineAdapter.untread(bill.getComment(), bill, requestMeta);
    return bill;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#auditFN(com.ufgov.zc.common.zc.model.ZcDingdian, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public ZcDingdian auditFN(ZcDingdian bill, RequestMeta requestMeta) throws Exception {

    bill=updateFN(bill, requestMeta);
    wfEngineAdapter.commit(bill.getComment(), bill, requestMeta);
    return bill;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#updateFN(com.ufgov.zc.common.zc.model.ZcDingdian, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public ZcDingdian updateFN(ZcDingdian bill, RequestMeta requestMeta) throws Exception {

    return _updateFN(bill, requestMeta, false);
  }

  private ZcDingdian _updateFN(ZcDingdian bill, RequestMeta requestMeta,boolean isImportData) throws Exception {
    String code = "";
    String temp_code = "";
    
    List biList = bill.getBiList();
    String userId = requestMeta.getSvUserID();
    String compoId = requestMeta.getCompoId();
    boolean isDraft = false;
    if (bill.getProcessInstId() == null || bill.getProcessInstId().longValue() == -1) {
      Long draftid = workflowDao.createDraftId();
      bill.setProcessInstId(draftid);
      isDraft = true;
    }

    Map map = null;
    if ("".equals(ZcSUtil.safeString(bill.getDdCode())) || bill.getDdCode().equals("自动编号")) {     
      // 当新建项目的时候，项目编码不进行正式编码，首先创建一个临时编码，等提交送审之后，进行正式编码
      temp_code = NumUtil.getInstance().getNo("ZC_DINGDIAN", "DD_CODE", bill);
      code = temp_code;
      bill.setDdCode(code);
      map = new BudgetUtil().getSaveBudgetByZcDingDian(ddBiDao, baseDao, ZcSUtil.isUseBi(), bill, biList);      
      BigDecimal biSum=new BigDecimal(0);
      for (int i = 0; i < biList.size(); i++) {
        ZcDingdianBi bi = (ZcDingdianBi) biList.get(i);
        bi.setDdCode(code);
        if(bi.getZcBiNo()!=null && bi.getZcBiNo().trim().length()>0){
          biSum=biSum.add(bi.getZcBiJhuaSum());
        }
      }
      bill.setBiSum(biSum);
      
      for (int i = 0; i < bill.getItemList().size(); i++) {
        ZcDingdianItem item = (ZcDingdianItem) bill.getItemList().get(i);
        item.setDdCode(code);
      }
 
      ddDao.insert(bill); 
      ddBiDao.insertList(biList);
      ddItemDao.insertList(bill.getItemList()); 
    } else {

      code = bill.getDdCode(); 
      
      BigDecimal biSum=new BigDecimal(0);
      for (int i = 0; i < biList.size(); i++) {
        ZcDingdianBi bi = (ZcDingdianBi) biList.get(i);
        bi.setDdCode(code);
        if(bi.getZcBiNo()!=null && bi.getZcBiNo().trim().length()>0){
          biSum=biSum.add(bi.getZcBiJhuaSum());
        }
      }
      bill.setBiSum(biSum); 
      //使用删除、插入，是考虑导入数据时，code已经有了，直接update没有用，所以采用删、插的模式
        ddDao.deleteByPrimaryKey(bill.getDdCode());
        ddDao.insert(bill); 

//      System.out.println("666="+bill.getCoCode()+bill.getCoName());
      map = new BudgetUtil().getSaveBudgetByZcDingDian(ddBiDao, baseDao, ZcSUtil.isUseBi(), bill, biList);

      ddBiDao.deleteBiByDdCode(bill.getDdCode());
      ddItemDao.deleteItemByDdCode(bill.getDdCode());
      for (int i = 0; i < biList.size(); i++) {
        ZcDingdianBi mbi = (ZcDingdianBi) biList.get(i);
        mbi.setDdCode(code);
      }
      ddBiDao.insertList(biList);      
      
      for (int i = 0; i < bill.getItemList().size(); i++) {
        ZcDingdianItem item = (ZcDingdianItem)  bill.getItemList().get(i);
        item.setDdCode(code);  
      }  
      ddItemDao.insertList(bill.getItemList());
    }

    if (isDraft) {
      AsWfDraft asWfDraft = new AsWfDraft();
      asWfDraft.setCompoId(compoId);
      asWfDraft.setWfDraftName(bill.getDdCode());
      asWfDraft.setUserId(userId);
      asWfDraft.setMasterTabId(compoId);
      asWfDraft.setWfDraftId(BigDecimal.valueOf(bill.getProcessInstId().longValue()));
      workflowDao.insertAsWfdraft(asWfDraft);
    }

    //不是导入的数据，且使用指标接口
    if (!isImportData) {//
      new BudgetUtil().callService(map,requestMeta.getSvNd());
    }
    return bill;
  }
  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#commitFN(java.util.List, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public void commitFN(List beanList, RequestMeta requestMeta) {
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#deleteListFN(java.util.List, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public void deleteListFN(List beanList, RequestMeta requestMeta) {
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#deleteFN(com.ufgov.zc.common.zc.model.ZcDingdian, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public void deleteFN(ZcDingdian bill, RequestMeta requestMeta) {
    // TCJLODO Auto-generated method stub
    ddDao.deleteByPrimaryKey(bill.getDdCode());
    ddBiDao.deleteBiByDdCode(bill.getDdCode());
    ddItemDao.deleteItemByDdCode(bill.getDdCode());
    //同步到另一个网端
    insertDeleteInfoToDataExchage(bill.getDdCode());
  }

  private void insertDeleteInfoToDataExchage(String ddCode) {
    // TCJLODO Auto-generated method stub
    //同步到另一个网端        
    ZcDingdian ht=selectByPrimaryKey(ddCode, null);
      DataExchangeRedo redo = new DataExchangeRedo();
      redo.setDataTypeID("ZC_DINGDIAN");
      redo.setDataTypeName("定点采购");
      ElementConditionDto dto = new ElementConditionDto();     
      redo.setRecordID(ddCode);
      redo.setRecordName("删除定点采购");
      redo.setMasterTableName("ZC_DINGDIAN");
      redo.setIsExported(DataExchangeRedo.STATUS_WAITING_EXPORTED);
      redo.setGenerateDate(new Date());
      redo.setOperateType("fdelete");
      IDataExchangeDao dataExchangeDao=(IDataExchangeDao)SpringContext.getBean("dataExchangeDao");
      dataExchangeDao.deleteByRecordIDAndDataTypeID(redo);
      dataExchangeDao.saveRedo(redo);
  }
  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#selectByPrimaryKey(java.lang.String, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public ZcDingdian selectByPrimaryKey(String ddCode, RequestMeta requestMeta) {

    ZcDingdian dd = ddDao.selectByPrimaryKey(ddCode);
    if(dd==null)return null;
    dd.setBiList(getBiLst(ddCode));
    dd.setItemList(getItemLst(ddCode));
    
    dd.setDbDigest(null);
    dd.setDbDigest(dd.digest());// 统一入口

    return dd;
  }
  private List getItemLst(String ddCode) {
    // TCJLODO Auto-generated method stub
    return ddItemDao.getItemLst(ddCode);
  }

  private List getBiLst(String ddCode) {
    // TCJLODO Auto-generated method stub
    List biLst = this.ddBiDao.getBiLst(ddCode);
    if(!isUseBudget()){
      return biLst;
    }
    String ids = "";
    for (int i = 0; i < biLst.size(); i++) {
      ZcDingdianBi bi = (ZcDingdianBi) biLst.get(i);
      if (bi.getZcBiNo() != null && bi.getZcBiNo().trim().length() > 0) {
        if (ids.length() == 0) {
          ids = "'" + bi.getZcBiNo() + "'";
        } else {
          ids = ids + ",'" + bi.getZcBiNo() + "'";
        }
      }
    }
    if(ids.trim().length()>0){
      List yuanBiLst=this.baseDao.query("VwBudgetGp.getVwBudgetGpByBiNoLst", ids);
      if(yuanBiLst==null){
        throw new RuntimeException("获取以下指标信息报错:"+ids);
      }
      for(int i=0;i<biLst.size();i++){
        ZcDingdianBi bi = (ZcDingdianBi) biLst.get(i);
        for(int j=0;j<yuanBiLst.size();j++){
          VwBudgetGp gp=(VwBudgetGp)yuanBiLst.get(j);
          if(bi.getZcBiNo()!=null && bi.getZcBiNo().equals(""+gp.getSumId())){
            //将指标的现有可用金额+当前计划占用的指标金额，作为当前指标的可用金额，这样定点计划单保存后，再修改，被本定点计划单占用的指标应该属于可用指标额度范围内，
            bi.setZcBiDoSum(gp.getCanuseMoney().add(bi.getZcBiJhuaSum()));
          }
        }
        
      }
    }
    return biLst;
  }
  private boolean isUseBudget() {
    
    return false;
   }
  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#callbackFN(com.ufgov.zc.common.zc.model.ZcDingdian, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public ZcDingdian callbackFN(ZcDingdian bill, RequestMeta requestMeta) {

    wfEngineAdapter.callback(bill.getComment(), bill, requestMeta);

    return bill;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#deleteByPrimaryKeyFN(java.lang.String, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public void deleteByPrimaryKeyFN(String ddCode, RequestMeta requestMeta) {
    ddDao.deleteByPrimaryKey(ddCode);
    ddBiDao.deleteBiByDdCode(ddCode);
    ddItemDao.deleteItemByDdCode(ddCode);
    //同步到另一个网端
    insertDeleteInfoToDataExchage(ddCode);
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#newCommitFN(com.ufgov.zc.common.zc.model.ZcDingdian, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public ZcDingdian newCommitFN(ZcDingdian bill, RequestMeta requestMeta) {
    wfEngineAdapter.newCommit(bill.getComment(), bill, requestMeta);
    return selectByPrimaryKey(bill.getDdCode(),requestMeta);
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#sendPayFN(com.ufgov.zc.common.zc.model.ZcDingdian, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public ZcDingdian sendPayFN(ZcDingdian bill, RequestMeta requestMeta) throws Exception {
    bill=updateFN(bill, requestMeta);    
    Map map = null;
    BudgetUtil bu=new BudgetUtil();
    //指标接口和支付接口分开，这样指标接口出错时则不掉用取消结冻指标操作
    try {
      /*
       * 释放指标金额，本次支付金额10w，则应该修改采购计划金额=原采购计划金额-10w
       */
      map = bu.getDingDianShiFangBudget(baseDao, true, bill);
      bu.callService(map,requestMeta.getSvNd());
      Map rtn=new PayForZcUtil().PayByDingDian(bill, requestMeta);
      if(rtn==null){
        //目前没有处理，应该不会出现这种情况
        //throw new RuntimeException(e1.getMessage());
      }else{
        String rtnType=(String) rtn.get(PayForZcUtil.RTN_RESULT);
        String returnMsg=(String) rtn.get(PayForZcUtil.RTN_MESSAGE);
        if(rtnType.equalsIgnoreCase(PayForZcUtil.RTN_RESULT_FALSE)){
          throw new RuntimeException("定点采购【" + bill.getDdCode() + "】生成支付出错:"+returnMsg);
        }else{//保存支付凭证号与当前支付单的关联关系
          String vouIds=(String) rtn.get(PayForZcUtil.RTN_DATA);
          _savePayVouId(vouIds,bill,requestMeta);
        }
      }
    } catch (ZcPayInterfaceException e) {    
      try {
        bu.cancelCallService(map,requestMeta.getSvNd());
      } catch (Exception e1) {
        String errorInfo="";
        errorInfo="定点采购【" + bill.getDdCode() + "】生成支付出错时，取消结冻的指标出现异常\n";
        List biLst = bill.getBiList();
        for (int i = 0; i < biLst.size(); i++) {
          ZcDingdianBi bi=(ZcDingdianBi)biLst.get(i);
          errorInfo+="指标编号：" + bi.getZcBiNo() + " 金额："
            + (bi.getZcBiJhuaSum() == null ? "" :bi.getZcBiJhuaSum().toString())+"\n";
        }
        
        System.err.println("异常信息如下："+errorInfo);
        e1.printStackTrace();
        throw new RuntimeException(e1.getMessage()+errorInfo);
      }
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
    bill=auditFN(bill, requestMeta);
    //支付指标
    return bill;
  }

  private void _savePayVouId(String vouIds, ZcDingdian dd, RequestMeta requestMeta) {
    // TCJLODO Auto-generated method stub
    if(vouIds==null)return;
    baseDao.delete("ZC_PAY_GK_INFO.deleteByZcBillId", dd.getDdCode());
    StringTokenizer st=new StringTokenizer(vouIds, PayForZcUtil.RTN_DATA_SPLIT);
    String interfaceType=AsOptionUtil.getInstance().getOptionVal(ZcPayGkInfo.GK_INTERFACE_TYPE);
    String vouType=interfaceType.equalsIgnoreCase("Y")?ZcPayGkInfo.GK_VOU_TYPE_ZFPZ:ZcPayGkInfo.GK_VOU_TYPE_ZFSQ;
    while(st.hasMoreTokens()){
      String vouId=st.nextToken();
      ZcPayGkInfo info=new ZcPayGkInfo();
      info.setZcBillType(ZcPayGkInfo.ZC_BILL_TYPE_DINGDIAN);
      info.setZcBillId(dd.getDdCode());
      info.setGkVouId(vouId);
      info.setGkReturnFlag("N");
      info.setSendGkDate(requestMeta.getSysDate());
      info.setGkVouType(vouType);
      baseDao.insert("ZC_PAY_GK_INFO.insert", info);
    }
    
  }
  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#queryExportsDatas(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public List queryExportsDatas(ElementConditionDto dto, RequestMeta meta) {

    List idLst = dto.getPmAdjustCodeList();
    if (idLst == null)
      return null;
    List rtn = new ArrayList();
    for (int i = 0; i < dto.getPmAdjustCodeList().size(); i++) {
      rtn.add(selectByPrimaryKey((String) dto.getPmAdjustCodeList().get(i), meta));
    }
    return rtn;

  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#importTransDatasFN(com.ufgov.zc.common.zc.model.ZcDingdian, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public String importTransDatasFN(ZcDingdian bill, RequestMeta meta) throws DataExchangeException {

    ZcDingdian bal=selectByPrimaryKey(bill.getDdCode(), meta);
    if(bal!=null && ("exec".equalsIgnoreCase(bal.getStatus()) || "sendGk".equalsIgnoreCase(bal.getStatus()))){
      throw new DataExchangeException("定点采购"+bill.getDdCode()+"已经送国库支付了，不能再次导入！");
    }
    String rtn = bill.getDdCode() + "导入成功";
    try {
      _updateFN(bill, meta,true);
    } catch (Exception e) {
      // TCJLODO Auto-generated catch block
      e.printStackTrace();
      rtn = bill.getDdCode() + "导入异常：\n" + e.getMessage();
    }
    return rtn;

  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.service.IZcDingDianService#importDelDataFN(java.lang.String, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public String importDelDataFN(String id, RequestMeta meta) throws DataExchangeException {

    ZcDingdian dd=selectByPrimaryKey(id, meta);
    if(dd==null)return "定点采购"+id+"同步删除成功";
    if(dd!=null && ("exec".equalsIgnoreCase(dd.getStatus()) || "sendGk".equalsIgnoreCase(dd.getStatus()))){
      throw new DataExchangeException("定点采购"+dd.getDdCode()+"已经送国库支付了，不能再次导入！");
    }
    ddDao.deleteByPrimaryKey(id);
    ddBiDao.deleteBiByDdCode(id);
    ddItemDao.deleteItemByDdCode(id);
    return "定点采购"+id+"同步删除成功";
  }

}
