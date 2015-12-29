/**
 * 
 */
package com.ufgov.zc.server.system.workflow;

import java.util.Date;

import com.kingdrive.workflow.context.WorkflowContext;
import com.kingdrive.workflow.exception.WorkflowException;
import com.kingdrive.workflow.listener.TaskAdapter;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.model.ZcPProBal;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.server.system.SpringContext;
import com.ufgov.zc.server.zc.dao.IDataExchangeDao;
import com.ufgov.zc.server.zc.dao.IZcEbBaseServiceDao;

/**
 * 资金支付申请单工作流通用的审批监听类，适用于工作流中不在网段的边界节点，如果是网段的边界节点，则要用其子类ZcPproBalWorkFlowLisenterWithCallBack
 * 
 * @author Administrator
 *
 */
public class ZcPproBalWorkFlowLisenter extends TaskAdapter {
  
   
  public void afterUntread(WorkflowContext context) throws WorkflowException {
    // TCJLODO Auto-generated method stub
  //资金支付申请审批过程中，需在内外网间同步资金支付申请信息和审批信息
    Long processId=context.getInstanceId();    
    IZcEbBaseServiceDao zcEbBaseServiceDao=(IZcEbBaseServiceDao)SpringContext.getBean("zcEbBaseServiceDao");          
    ZcPProBal ht=(ZcPProBal)zcEbBaseServiceDao.queryObject("ZC_P_PRO_BAL.selectByProcessinstid", ""+processId.longValue());
    if (ht != null) {
      DataExchangeRedo redo = new DataExchangeRedo();
      redo.setDataTypeID("ZC_P_PRO_BAL");
      redo.setDataTypeName("资金支付");    
      redo.setRecordID(ht.getZcBalId());
      redo.setRecordName(ht.getZcHtName());
      redo.setMasterTableName("ZC_P_PRO_BAL");
      redo.setIsExported(DataExchangeRedo.STATUS_WAITING_EXPORTED);
      redo.setGenerateDate(new Date());
      redo.setOperateType("funtread");
      IDataExchangeDao dataExchangeDao=(IDataExchangeDao)SpringContext.getBean("dataExchangeDao");
      dataExchangeDao.deleteByRecordIDAndDataTypeID(redo);
      dataExchangeDao.saveRedo(redo);
    }
  }

  public void afterExecution(WorkflowContext context) throws WorkflowException {
    // TCJLODO Auto-generated method stub
  //资金支付申请审批过程中，需在内外网间同步资金支付申请信息和审批信息
    Long processId=context.getInstanceId();    
    IZcEbBaseServiceDao zcEbBaseServiceDao=(IZcEbBaseServiceDao)SpringContext.getBean("zcEbBaseServiceDao");          
    ZcPProBal ht=(ZcPProBal)zcEbBaseServiceDao.queryObject("ZC_P_PRO_BAL.selectByProcessinstid", ""+processId.longValue());
    if (ht != null) {
      DataExchangeRedo redo = new DataExchangeRedo();
      redo.setDataTypeID("ZC_P_PRO_BAL");
      redo.setDataTypeName("资金支付");
      ElementConditionDto dto = new ElementConditionDto();     
      redo.setRecordID(ht.getZcBalId());
      redo.setRecordName(ht.getZcHtName());
      redo.setMasterTableName("ZC_P_PRO_BAL");
      redo.setIsExported(DataExchangeRedo.STATUS_WAITING_EXPORTED);
      redo.setGenerateDate(new Date());
      redo.setOperateType("faudit");
      IDataExchangeDao dataExchangeDao=(IDataExchangeDao)SpringContext.getBean("dataExchangeDao");
      dataExchangeDao.deleteByRecordIDAndDataTypeID(redo);
      dataExchangeDao.saveRedo(redo);
    }
  }

}
