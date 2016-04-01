package com.ufgov.zc.server.system.workflow;

import java.util.Date;

import com.kingdrive.workflow.context.WorkflowContext;
import com.kingdrive.workflow.exception.WorkflowException;
import com.kingdrive.workflow.listener.TaskAdapter;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.model.ZcDingdian;
import com.ufgov.zc.server.system.SpringContext;
import com.ufgov.zc.server.zc.dao.IDataExchangeDao;
import com.ufgov.zc.server.zc.dao.IZcEbBaseServiceDao;

public class ZcDingDianWorkFlowLisenter  extends TaskAdapter {

  public void afterUntread(WorkflowContext context) throws WorkflowException {
    // TCJLODO Auto-generated method stub
  //汽车保险审批过程中，需在内外网间同步合同信息和审批信息
    Long processId=context.getInstanceId();    
    IZcEbBaseServiceDao zcEbBaseServiceDao=(IZcEbBaseServiceDao)SpringContext.getBean("zcEbBaseServiceDao");          
    ZcDingdian qx=(ZcDingdian)zcEbBaseServiceDao.queryObject("ZcDingdianMapper.selectByProcessinstid", ""+processId.longValue());
    if (qx != null) {
      DataExchangeRedo redo = new DataExchangeRedo();
      redo.setDataTypeID("ZC_DINGDIAN");
      redo.setDataTypeName("定点采购");
      ElementConditionDto dto = new ElementConditionDto();     
      redo.setRecordID(qx.getDdCode());
      redo.setRecordName(qx.getDdName());
      redo.setMasterTableName("ZC_DINGDIAN");
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
  //汽车保险审批过程中，需在内外网间同步合同信息和审批信息
    Long processId=context.getInstanceId();    
    IZcEbBaseServiceDao zcEbBaseServiceDao=(IZcEbBaseServiceDao)SpringContext.getBean("zcEbBaseServiceDao");          
    ZcDingdian ht=(ZcDingdian)zcEbBaseServiceDao.queryObject("ZcDingdianMapper.selectByProcessinstid", ""+processId.longValue());
    if (ht != null) {
      DataExchangeRedo redo = new DataExchangeRedo();
      redo.setDataTypeID("ZC_DINGDIAN");
      redo.setDataTypeName("定点采购");
      ElementConditionDto dto = new ElementConditionDto();     
      redo.setRecordID(ht.getDdCode());
      redo.setRecordName(ht.getDdName());
      redo.setMasterTableName("ZC_DINGDIAN");
      redo.setIsExported(DataExchangeRedo.STATUS_WAITING_EXPORTED);
      redo.setGenerateDate(new Date());
      redo.setOperateType("faudit");
      IDataExchangeDao dataExchangeDao=(IDataExchangeDao)SpringContext.getBean("dataExchangeDao");
      dataExchangeDao.deleteByRecordIDAndDataTypeID(redo);
      dataExchangeDao.saveRedo(redo);
    }
  }
}