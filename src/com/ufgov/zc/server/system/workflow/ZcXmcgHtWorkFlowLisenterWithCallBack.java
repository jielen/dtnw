/**
 * 
 */
package com.ufgov.zc.server.system.workflow;

import com.kingdrive.workflow.context.WorkflowContext;
import com.kingdrive.workflow.exception.WorkflowException;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.server.system.SpringContext;
import com.ufgov.zc.server.zc.dao.IZcEbBaseServiceDao;

/**
 * 供应商、采购单位收回采购合同的监听类
 * 采购单位和供应商在内外网进行处理，供应商收回时，如果数据已经导入到内网，则不能收回，如果没有导入，则可以收回，收回同时，删除等待导出的数据(zc_data_exchange_redo)
 * 同理，采购单位审批之后，数据将导到外网，如果进行收回，也要判断数据是否已经导出了
 * @author Administrator
 *
 */
public class ZcXmcgHtWorkFlowLisenterWithCallBack extends ZcXmcgHtWorkFlowLisenter {

  IZcEbBaseServiceDao zcEbBaseServiceDao=(IZcEbBaseServiceDao)SpringContext.getBean("zcEbBaseServiceDao");
  /**
   * 收回同时，删除等待导出的数据(zc_data_exchange_redo)
   * 
   */
  public void afterCallback(WorkflowContext context) throws WorkflowException {
    // TCJLODO Auto-generated method stub
    Long processId=context.getInstanceId();
    ZcXmcgHt ht=(ZcXmcgHt)zcEbBaseServiceDao.queryObject("ZC_XMCG_HT.selectByProcessinstid", ""+processId.longValue());
    DataExchangeRedo redo=new DataExchangeRedo();
    redo.setRecordID(ht.getZcHtCode());
    redo.setIsExported(DataExchangeRedo.STATUS_WAITING_EXPORTED);
    redo.setMasterTableName("ZC_XMCG_HT");
    zcEbBaseServiceDao.delete("DataExchange.deleteByRecordIdAndIsExported", redo);
  }

   /**
    * 采购单位和供应商在内外网进行处理，供应商收回时，如果数据已经导入到内网，则不能收回，如果没有导入，则可以收回
    */
  public void beforeCallback(WorkflowContext context) throws WorkflowException {
    // TCJLODO Auto-generated method stub
    Long processId=context.getInstanceId();
    ZcXmcgHt ht=(ZcXmcgHt)zcEbBaseServiceDao.queryObject("ZC_XMCG_HT.selectByProcessinstid", ""+processId.longValue());
    ElementConditionDto dto=new ElementConditionDto();
    dto.setExtField1(ht.getZcHtCode());
    //因为ZC_DATA_EXCHANGE_REDO是一条业务数据多次插入，所以不能以已经导入状态查询，只能以等待导出状态查询，如果没有存在等待导出状态的数据，说明已经导出了.
    dto.setStatus(DataExchangeRedo.STATUS_WAITING_EXPORTED);    
    if(zcEbBaseServiceDao.queryObject("DataExchange.getDataExchangeRedo", dto)==null){
      throw new WorkflowException("合同:"+ht.getZcHtName()+"("+ht.getZcHtCode()+"),已经导出进行审批，不能收回.");
    }
  }

}
