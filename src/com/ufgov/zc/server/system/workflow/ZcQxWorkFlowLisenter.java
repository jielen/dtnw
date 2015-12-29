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
import com.ufgov.zc.common.zc.model.ZcQx;
import com.ufgov.zc.server.system.SpringContext;
import com.ufgov.zc.server.zc.dao.IDataExchangeDao;
import com.ufgov.zc.server.zc.dao.IZcEbBaseServiceDao;

/**
 * 汽修合同由供应商发起，监理确认、单位确认、采购中心确认、财政确认支付
 * @author Administrator
 *
 */
public class ZcQxWorkFlowLisenter extends TaskAdapter {

  
  public void afterUntread(WorkflowContext context) throws WorkflowException {
    // TCJLODO Auto-generated method stub
  //汽车维修审批过程中，需在内外网间同步合同信息和审批信息
    Long processId=context.getInstanceId();    
    IZcEbBaseServiceDao zcEbBaseServiceDao=(IZcEbBaseServiceDao)SpringContext.getBean("zcEbBaseServiceDao");          
    ZcQx qx=(ZcQx)zcEbBaseServiceDao.queryObject("ZC_QX.selectByProcessinstid", ""+processId.longValue());
    if (qx != null) {
      DataExchangeRedo redo = new DataExchangeRedo();
      redo.setDataTypeID("ZC_EB_QX");
      redo.setDataTypeName("汽车维修");
      ElementConditionDto dto = new ElementConditionDto();     
      redo.setRecordID(qx.getQxCode());
      redo.setRecordName(qx.getQxName());
      redo.setMasterTableName("ZC_QX");
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
  //汽车维修审批过程中，需在内外网间同步合同信息和审批信息
    Long processId=context.getInstanceId();    
    IZcEbBaseServiceDao zcEbBaseServiceDao=(IZcEbBaseServiceDao)SpringContext.getBean("zcEbBaseServiceDao");          
    ZcQx ht=(ZcQx)zcEbBaseServiceDao.queryObject("ZC_QX.selectByProcessinstid", ""+processId.longValue());
    if (ht != null) {
      DataExchangeRedo redo = new DataExchangeRedo();
      redo.setDataTypeID("ZC_EB_QX");
      redo.setDataTypeName("汽车维修");
      ElementConditionDto dto = new ElementConditionDto();     
      redo.setRecordID(ht.getQxCode());
      redo.setRecordName(ht.getQxName());
      redo.setMasterTableName("ZC_QX");
      redo.setIsExported(DataExchangeRedo.STATUS_WAITING_EXPORTED);
      redo.setGenerateDate(new Date());
      redo.setOperateType("faudit");
      IDataExchangeDao dataExchangeDao=(IDataExchangeDao)SpringContext.getBean("dataExchangeDao");
      dataExchangeDao.deleteByRecordIDAndDataTypeID(redo);
      dataExchangeDao.saveRedo(redo);
    }
  }
}
