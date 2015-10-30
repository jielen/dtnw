/**
 * 
 */
package com.ufgov.zc.server.system.workflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kingdrive.workflow.context.WorkflowContext;
import com.kingdrive.workflow.exception.WorkflowException;
import com.kingdrive.workflow.listener.TaskAdapter;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.server.system.SpringContext;
import com.ufgov.zc.server.zc.dao.IDataExchangeDao;
import com.ufgov.zc.server.zc.dao.IZcEbBaseServiceDao;

/**
 * 采购合同通用的审批监听类，适用于工作流中不在网段的边界节点，如果是网段的边界节点，则要用其子类ZcXmcgHtWorkFlowLisenterWithCallBack
 * 采购合同审批工作流监听类
 * @author Administrator
 *
 */
public class ZcXmcgHtWorkFlowLisenter extends TaskAdapter {
  
   
  public void afterUntread(WorkflowContext context) throws WorkflowException {
    // TODO Auto-generated method stub
  //采购合同审批过程中，需在内外网间同步合同信息和审批信息
    Long processId=context.getInstanceId();    
    IZcEbBaseServiceDao zcEbBaseServiceDao=(IZcEbBaseServiceDao)SpringContext.getBean("zcEbBaseServiceDao");          
    ZcXmcgHt ht=(ZcXmcgHt)zcEbBaseServiceDao.queryObject("ZC_XMCG_HT.selectByProcessinstid", ""+processId.longValue());
    if (ht != null) {
      DataExchangeRedo redo = new DataExchangeRedo();
      redo.setDataTypeID("ZC_XMCG_HT");
      redo.setDataTypeName("采购合同");
      ElementConditionDto dto = new ElementConditionDto();     
      redo.setRecordID(ht.getZcHtCode());
      redo.setRecordName(ht.getZcHtName());
      redo.setMasterTableName("ZC_XMCG_HT");
      redo.setIsExported(DataExchangeRedo.STATUS_WAITING_EXPORTED);
      redo.setGenerateDate(new Date());
      redo.setOperateType("funtread");
      IDataExchangeDao dataExchangeDao=(IDataExchangeDao)SpringContext.getBean("dataExchangeDao");
      dataExchangeDao.deleteByRecordIDAndDataTypeID(redo);
      dataExchangeDao.saveRedo(redo);
    }
  }

  public void afterExecution(WorkflowContext context) throws WorkflowException {
    // TODO Auto-generated method stub
  //采购合同审批过程中，需在内外网间同步合同信息和审批信息
    Long processId=context.getInstanceId();    
    IZcEbBaseServiceDao zcEbBaseServiceDao=(IZcEbBaseServiceDao)SpringContext.getBean("zcEbBaseServiceDao");          
    ZcXmcgHt ht=(ZcXmcgHt)zcEbBaseServiceDao.queryObject("ZC_XMCG_HT.selectByProcessinstid", ""+processId.longValue());
    if (ht != null) {
      DataExchangeRedo redo = new DataExchangeRedo();
      redo.setDataTypeID("ZC_XMCG_HT");
      redo.setDataTypeName("采购合同");
      ElementConditionDto dto = new ElementConditionDto();     
      redo.setRecordID(ht.getZcHtCode());
      redo.setRecordName(ht.getZcHtName());
      redo.setMasterTableName("ZC_XMCG_HT");
      redo.setIsExported(DataExchangeRedo.STATUS_WAITING_EXPORTED);
      redo.setGenerateDate(new Date());
      redo.setOperateType("faudit");
      IDataExchangeDao dataExchangeDao=(IDataExchangeDao)SpringContext.getBean("dataExchangeDao");
      dataExchangeDao.deleteByRecordIDAndDataTypeID(redo);
      dataExchangeDao.saveRedo(redo);
    }
  }
}
