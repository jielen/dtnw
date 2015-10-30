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
import com.ufgov.zc.common.zc.model.ZcQb;
import com.ufgov.zc.server.system.SpringContext;
import com.ufgov.zc.server.zc.dao.IDataExchangeDao;
import com.ufgov.zc.server.zc.dao.IZcEbBaseServiceDao;

/**
 * @author Administrator
 *
 */
public class ZcQbWorkFlowLisenter extends TaskAdapter {

  
  public void afterUntread(WorkflowContext context) throws WorkflowException {
    // TODO Auto-generated method stub
  //汽车保险审批过程中，需在内外网间同步合同信息和审批信息
    Long processId=context.getInstanceId();    
    IZcEbBaseServiceDao zcEbBaseServiceDao=(IZcEbBaseServiceDao)SpringContext.getBean("zcEbBaseServiceDao");          
    ZcQb qx=(ZcQb)zcEbBaseServiceDao.queryObject("ZC_QB.selectByProcessinstid", ""+processId.longValue());
    if (qx != null) {
      DataExchangeRedo redo = new DataExchangeRedo();
      redo.setDataTypeID("ZC_EB_QB");
      redo.setDataTypeName("汽车保险");
      ElementConditionDto dto = new ElementConditionDto();     
      redo.setRecordID(qx.getQbCode());
      redo.setRecordName(qx.getQbName());
      redo.setMasterTableName("ZC_QB");
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
  //汽车保险审批过程中，需在内外网间同步合同信息和审批信息
    Long processId=context.getInstanceId();    
    IZcEbBaseServiceDao zcEbBaseServiceDao=(IZcEbBaseServiceDao)SpringContext.getBean("zcEbBaseServiceDao");          
    ZcQb ht=(ZcQb)zcEbBaseServiceDao.queryObject("ZC_QB.selectByProcessinstid", ""+processId.longValue());
    if (ht != null) {
      DataExchangeRedo redo = new DataExchangeRedo();
      redo.setDataTypeID("ZC_EB_QB");
      redo.setDataTypeName("汽车保险");
      ElementConditionDto dto = new ElementConditionDto();     
      redo.setRecordID(ht.getQbCode());
      redo.setRecordName(ht.getQbName());
      redo.setMasterTableName("ZC_QB");
      redo.setIsExported(DataExchangeRedo.STATUS_WAITING_EXPORTED);
      redo.setGenerateDate(new Date());
      redo.setOperateType("faudit");
      IDataExchangeDao dataExchangeDao=(IDataExchangeDao)SpringContext.getBean("dataExchangeDao");
      dataExchangeDao.deleteByRecordIDAndDataTypeID(redo);
      dataExchangeDao.saveRedo(redo);
    }
  }
}
