package com.ufgov.zc.server.system.workflow;import com.kingdrive.workflow.context.WorkflowContext;import com.kingdrive.workflow.exception.WorkflowException;import com.kingdrive.workflow.listener.TaskListener;import com.ufgov.zc.server.system.SpringContext;import com.ufgov.zc.server.system.dao.IWorkflowDao;public class CurrentTaskControlListener implements TaskListener {  private IWorkflowDao dao = (IWorkflowDao) SpringContext.getBean("workflowDao");  public void afterExecution(WorkflowContext arg0) throws WorkflowException {    dao.updateCurrentTaskSendStatus(arg0.getInstanceId(), "0");  }  public void afterUntread(WorkflowContext arg0) throws WorkflowException {  }  public void beforeExecution(WorkflowContext arg0) throws WorkflowException {  }  public void beforeUntread(WorkflowContext arg0) throws WorkflowException {  }  public void afterCallback(WorkflowContext context) throws WorkflowException {    // TCJLODO Auto-generated method stub  }  public void beforeCallback(WorkflowContext context) throws WorkflowException {    // TCJLODO Auto-generated method stub  }  public void beforeRework(WorkflowContext meta) throws WorkflowException {    // TCJLODO Auto-generated method stub  }  public void afterRework(WorkflowContext meta) throws WorkflowException {    // TCJLODO Auto-generated method stub  }}