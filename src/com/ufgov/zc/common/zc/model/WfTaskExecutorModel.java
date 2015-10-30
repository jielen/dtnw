package com.ufgov.zc.common.zc.model;

import java.io.Serializable;

public class WfTaskExecutorModel implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4689377869068431379L;
  private Long taskExecutorOrderId;
  private Long instanceId;
  private Long nodeId;
  private String executor = "";

  private Long executorOrder = new Long(1L);

  private Long responsibility = new Long(1L);

  private String action = "";

  private String granterId = "";

  private String granterInfo = "";

  public String getAction()
  {
    return this.action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getExecutor() {
    return this.executor;
  }

  public void setExecutor(String executor) {
    this.executor = executor;
  }

  public Long getExecutorOrder() {
    return this.executorOrder;
  }

  public void setExecutorOrder(Long executorOrder) {
    this.executorOrder = executorOrder;
  }

  public Long getInstanceId() {
    return this.instanceId;
  }

  public void setInstanceId(Long instanceId) {
    this.instanceId = instanceId;
  }

  public Long getNodeId() {
    return this.nodeId;
  }

  public void setNodeId(Long nodeId) {
    this.nodeId = nodeId;
  }

  public Long getResponsibility() {
    return this.responsibility;
  }

  public void setResponsibility(Long responsibility) {
    this.responsibility = responsibility;
  }

  public Long getTaskExecutorOrderId() {
    return this.taskExecutorOrderId;
  }

  public void setTaskExecutorOrderId(Long taskExecutorOrderId) {
    this.taskExecutorOrderId = taskExecutorOrderId;
  }

  public String getGranterInfo()
  {
    return this.granterInfo;
  }

  public void setGranterInfo(String granterInfo)
  {
    this.granterInfo = granterInfo;
  }

  public String getGranterId()
  {
    return this.granterId;
  }

  public void setGranterId(String granterId)
  {
    this.granterId = granterId;
  }
}
