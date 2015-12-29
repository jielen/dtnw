package com.ufgov.zc.server.console.service.impl;import java.util.List;import com.kingdrive.workflow.model.runtime.CurrentTaskModel;import com.kingdrive.workflow.service.db.WFRuntimeService;import com.ufgov.zc.common.console.model.AsGrantedRole;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.server.console.dao.IAsRoleGrantedDao;import com.ufgov.zc.server.console.service.IAsRoleGrantedService;public class AsRoleGrantedService implements IAsRoleGrantedService {  private IAsRoleGrantedDao asRoleGrantedDao;  private WFRuntimeService wfRuntimeService;  public WFRuntimeService getWfRuntimeService() {    return wfRuntimeService;  }  public void setWfRuntimeService(WFRuntimeService wfRuntimeService) {    this.wfRuntimeService = wfRuntimeService;  }  public IAsRoleGrantedDao getAsRoleGrantedDao() {    return asRoleGrantedDao;  }  public void setAsRoleGrantedDao(IAsRoleGrantedDao asRoleGrantedDao) {    this.asRoleGrantedDao = asRoleGrantedDao;  }  public AsRoleGrantedService() {  }  public void deleteGrantedRoleByGrant(ElementConditionDto condition) {    // TCJLODO Auto-generated method stub    asRoleGrantedDao.deleteGrantedRoleByGrant(condition);  }  public void deleteGrantedRoleByGranted(ElementConditionDto condition) {    // TCJLODO Auto-generated method stub    asRoleGrantedDao.deleteGrantedRoleByGranted(condition);  }  public void deleteGrantedRoleById(String id) {    // TCJLODO Auto-generated method stub    asRoleGrantedDao.deleteGrantedRoleById(id);  }  public List getUserGrantedRole(ElementConditionDto condition) {    // TCJLODO Auto-generated method stub    return asRoleGrantedDao.getUserGrantedRole(condition);  }  public void insertGrantedRole(AsGrantedRole grantedRole) {    // TCJLODO Auto-generated method stub    asRoleGrantedDao.insertGrantedRole(grantedRole);  }  public void insertGrantedRoles(List roles) {    // TCJLODO Auto-generated method stub    asRoleGrantedDao.insertGrantedRoles(roles);  }  public void deleteGrantedRoles(List roles) {    // TCJLODO Auto-generated method stub    asRoleGrantedDao.deleteGrantedRoles(roles);  }  public void deleteAllGrantedTask(String granter) {    asRoleGrantedDao.deleteAllGrantedTask(granter);  }  public void deleteSelectedGrantedTask(String granter, String granterInfo, String granted) {    asRoleGrantedDao.deleteSelectedGrantedTask(granter, granterInfo, granted);  }  public void insertGrantedTask(String granter, String granted, String granterInfo, String roleids) {    List currentTasks = wfRuntimeService.getCurrentTaskByUser(null, granter);    //List currentTasks = wfRuntimeService.getCurrentTaskByRole(granter, roleids);    CurrentTaskModel task = null;    for (int i = 0; i < currentTasks.size(); i++) {      task = (CurrentTaskModel) currentTasks.get(i);      task.setExecutor(granted);      //      task.setGranterId(granter);      //      //      task.setGranterInfo(granterInfo);      wfRuntimeService.createCurrentTask(task);    }  }}