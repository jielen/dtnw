/**
 * 
 */
package com.ufgov.zc.server.zc.workflow;

import com.kingdrive.workflow.context.WorkflowContext;
import com.kingdrive.workflow.exception.WorkflowException;
import com.kingdrive.workflow.listener.TaskAdapter;
import com.kingdrive.workflow.listener.TaskListener;

/**
 * 供应商合同送审监听类
 * @author Administrator
 *
 */
public class ZcEbHtSupplierSendHandler implements TaskListener {
  
  public void afterExecution(WorkflowContext context)
    throws WorkflowException {
//    context
    System.out.println("you send a ht "+context.getInstanceId());
}

  
  public void afterCallback(WorkflowContext arg0) throws WorkflowException {
    // TCJLODO Auto-generated method stub
    
  }

  
  public void afterUntread(WorkflowContext context) throws WorkflowException {
    // TCJLODO Auto-generated method stub
    
  }

  
  public void beforeCallback(WorkflowContext arg0) throws WorkflowException {
    // TCJLODO Auto-generated method stub
    
  }

  
  public void beforeExecution(WorkflowContext context) throws WorkflowException {
    // TCJLODO Auto-generated method stub
    
  }

  
  public void beforeUntread(WorkflowContext context) throws WorkflowException {
    // TCJLODO Auto-generated method stub
    
  }
}
