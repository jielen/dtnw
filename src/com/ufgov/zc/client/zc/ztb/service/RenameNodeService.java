package com.ufgov.zc.client.zc.ztb.service;import com.ufgov.zc.client.zc.ztb.dao.FileImportDao;import com.ufgov.zc.client.zc.ztb.model.*;import com.ufgov.zc.client.zc.ztb.util.PubFunction;public class RenameNodeService {  private FileImportDao importTbFileDao = FileImportDao.getInstance();  public void rename(SmartTreeNode currentNode) throws Exception {    updateNodeInfoToAttrModel(currentNode);    BusinessProject businessProject = PubFunction.getProjectByCurrentNode(currentNode);    updateProjectConfig(businessProject);  }  private void updateNodeInfoToAttrModel(SmartTreeNode currentNode) {    currentNode.setNodeName(currentNode.getUserObject().toString());    Object o = currentNode.getUserObject();    if (o instanceof PackDetails) {      PackDetails pd = (PackDetails) o;      pd.setName(currentNode.getUserObject().toString());    } else {      BusinessPack pp = (BusinessPack) o;      pp.setName(currentNode.getUserObject().toString());    }  }  private void updateProjectConfig(BusinessProject businessProject) throws Exception {    BusinessProjects businessProjects = null;    for (BusinessProject p : businessProjects.getProList()) {      if (p.getNo().equals(businessProject.getNo())) {        int index = businessProjects.getProList().indexOf(p);        businessProjects.getProList().set(index, businessProject);        importTbFileDao.createProjectsXMLFile(new SmartTreeNode());        return;      }    }  }}