package com.ufgov.zc.server.zc.service.impl;import java.util.HashMap;import java.util.Map;import com.ufgov.zc.common.commonbiz.model.Position;import com.ufgov.zc.common.console.model.AsEmp;import com.ufgov.zc.common.console.model.AsUserGroup;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.model.AsFile;import com.ufgov.zc.common.system.model.AsNoRule;import com.ufgov.zc.common.system.model.AsNumTool;import com.ufgov.zc.common.system.model.User;import com.ufgov.zc.common.zc.model.ZcEbEcbjItem;import com.ufgov.zc.common.zc.model.ZcEbEcbjPlan;import com.ufgov.zc.common.zc.model.ZcEbEvalParam;import com.ufgov.zc.common.zc.model.ZcEbEvalReport;import com.ufgov.zc.common.zc.model.ZcEbEvalResult;import com.ufgov.zc.common.zc.model.ZcEbEvalResultExample;import com.ufgov.zc.common.zc.model.ZcEbExpertOpinion;import com.ufgov.zc.common.zc.model.ZcEbOpenBidTeam;import com.ufgov.zc.common.zc.model.ZcEbOpenBidTeamMember;import com.ufgov.zc.common.zc.model.ZcEbOpenBidTeamPack;import com.ufgov.zc.common.zc.model.ZcEbPack;import com.ufgov.zc.common.zc.model.ZcEbPackEvalResult;import com.ufgov.zc.common.zc.model.ZcEbPackExpert;import com.ufgov.zc.common.zc.model.ZcEbPackExpertExample;import com.ufgov.zc.common.zc.model.ZcEbProj;import com.ufgov.zc.common.zc.model.ZcEbProjChange;import com.ufgov.zc.common.zc.model.ZcEbProjectLivingChange;import com.ufgov.zc.common.zc.model.ZcEbSignup;import com.ufgov.zc.common.zc.model.ZcEbSignupPackDetail;import com.ufgov.zc.server.console.dao.IAsEmpDao;import com.ufgov.zc.server.system.dao.IAsFileDao;import com.ufgov.zc.server.system.dao.IUserDao;import com.ufgov.zc.server.zc.dao.IBaseDao;import com.ufgov.zc.server.zc.service.IIntranetDataImpService;import com.ufgov.zc.server.zc.util.GeneralFunc;public class IntranetDataImpService implements IIntranetDataImpService {  private IBaseDao baseDao;  private IAsEmpDao asEmpDao;  private IAsFileDao asFileDao;  private IUserDao userDao;  public void insert(ZcEbOpenBidTeam po) {    baseDao.delete("ZcEbOpenBidTeam.deleteZcEbOpenBidTeam", po.getTeamCode());    baseDao.insert("ZcEbOpenBidTeam.insertZcEbOpenBidTeam", po);  }  public void insert(ZcEbOpenBidTeamMember po) {    baseDao.delete("ZcEbOpenBidTeam.deleteZcEbOpenBidTeamAllMember", po.getTeamCode());    baseDao.insert("ZcEbOpenBidTeam.insertZcEbOpenBidTeamMember", po);  }  public void insert(ZcEbOpenBidTeamPack po) {    Map para = new HashMap();    para.put("TEAM_CODE", po.getTeamCode());    para.put("PACK_CODE", po.getPackCode());    baseDao.delete("ZcEbOpenBidTeam.deleteZcEbObtPackByPackCodeAndTeamCode", para);    baseDao.insert("ZcEbOpenBidTeam.insertZcEbOpenBidTeamPack", po);  }  public void insert(ZcEbPackExpert po) {    ZcEbPackExpertExample ee = new ZcEbPackExpertExample();    ee.createCriteria().andProjCodeEqualTo(po.getProjCode()).andPackCodeEqualTo(po.getPackCode()).andExpertCodeEqualTo(po.getExpertCode());    baseDao.delete("ZC_EB_PACK_EXPERT.abatorgenerated_deleteByExample", ee);    baseDao.insert("ZC_EB_PACK_EXPERT.abatorgenerated_insert", po);  }  public void insert(User po) {    String tp = GeneralFunc._encodePwd(po.getPassword());    po.setPassword(tp);    User user = userDao.getUserById(po.getUserId());    if (user == null) {      userDao.insertUser(po);    }  }  public void insert(AsUserGroup po) {    User user = userDao.getUserById(po.getUserId());    if (user == null) {      userDao.insertAsUserGroup(po);    }  }  public void insert(AsEmp po) {    userDao.deleteAsEmpByUserId(po.getUserId());    asEmpDao.insert(po);  }  public void insert(Position po) {    Map map = new HashMap();    map.put("EMP_CODE", po.getEmpCode());    map.put("ND", String.valueOf(po.getNd()));    userDao.deleteAsEmpPosiByEmpCode(map);    userDao.insertAsEmpPosition(po);  }  public void insert(ZcEbProjectLivingChange po) {    baseDao.delete("ZcEbProjectLivingChange.deleteZcEbProjectLivingChangeById", String.valueOf(po.getsId()));    baseDao.insert("ZcEbProjectLivingChange.insertZcEbProjectLivingChangeHasKey", po);  }  public void insert(ZcEbProjChange po) {    baseDao.delete("ZcEbProjChange.deleteZcEbProjByProjCode", po.getProjCode());    baseDao.insert("ZcEbProjChange.insertZcEbProj", po);  }  public void insert(ZcEbEcbjPlan po) {    baseDao.delete("ZcEbEcbjItem.deleteZcEbEcbjPlan", po.getPlanCode());    baseDao.insert("ZcEbEcbjItem.insertZcEbEcbjPlan", po);  }  public void insert(ZcEbEcbjItem po) {    ElementConditionDto e = new ElementConditionDto();    e.setExtField1(po.getSignupPackId());    e.setExtField2(po.getStatus());    e.setExtField3(po.getBjNo());    e.setExtField4(po.getProviderCode());    baseDao.delete("ZcEbEcbjItem.deleteZcEbEcbjItem", e);    baseDao.insert("ZcEbEcbjItem.insertZcEbEcbjItem", po);  }  public void insert(AsFile po) {    asFileDao.deleteAsFileById(po.getFileId());    asFileDao.insertAsFile(po);  }  public void insert(ZcEbExpertOpinion po) {    baseDao.delete("ZcEbEval.deleteZcEbExpertOpinion", po);    baseDao.insert("ZcEbEval.insertZcEbExpertOpinion", po);  }  public void insert(ZcEbEvalResult po) {    ZcEbEvalResultExample exp = new ZcEbEvalResultExample();    exp.createCriteria().andResultCodeEqualTo(po.getResultCode());    baseDao.delete("ZC_EB_EVAL_RESULT.abatorgenerated_deleteByExample", exp);    baseDao.insert("ZC_EB_EVAL_RESULT.abatorgenerated_insert", po);  }  public void insert(ZcEbEvalReport po) {    baseDao.delete("ZcEbEval.deleteZcEbEvalReport", po.getReportCode());    baseDao.insert("ZcEbEval.insertZcEbEvalReport", po);  }  public void insert(ZcEbPackEvalResult po) {    Map obj = new HashMap();    obj.put("packCode", po.getPackCode());    obj.put("providerCode", po.getProviderCode());    obj.put("IS_COMPLIANCE_RESULT", po.getIsComplianceResult());    obj.put("projCode", po.getProjCode());    baseDao.delete("ZcEbEval.deleteZcEbPackEvalFinalSumResult", obj);    baseDao.insert("ZcEbEval.insertZcEbPackEvalFinalSumResult", po);  }  public void insert(ZcEbEvalParam po) {    baseDao.delete("ZcEbEval.deleteZcEbEvalParam", po);    baseDao.insert("ZcEbEval.insertZcEbEvalParam", po);  }  public void insert(AsNumTool po) {    //baseDao.insert("AsNumToolNo.insertAsNumToolNo", po);  }  public void insert(AsNoRule po) {    //baseDao.insert("AsNoRule.abatorgenerated_insert", po);  }  public IBaseDao getBaseDao() {    return baseDao;  }  public void setBaseDao(IBaseDao baseDao) {    this.baseDao = baseDao;  }  public IAsEmpDao getAsEmpDao() {    return asEmpDao;  }  public void setAsEmpDao(IAsEmpDao asEmpDao) {    this.asEmpDao = asEmpDao;  }  public IAsFileDao getAsFileDao() {    return asFileDao;  }  public void setAsFileDao(IAsFileDao asFileDao) {    this.asFileDao = asFileDao;  }  public IUserDao getUserDao() {    return userDao;  }  public void setUserDao(IUserDao userDao) {    this.userDao = userDao;  }  public void insert(ZcEbProj po) {    // TODO Auto-generated method stub    baseDao.delete("ZcEbProj.deleteZcEbProjByProjCode", po.getProjCode());    baseDao.insert("ZcEbProj.insertZcEbProj", po);  }  public void insert(ZcEbPack po) {    // TODO Auto-generated method stub    //List list = baseDao.query("ZcEbProj.getOriginalZcEbProjByProjCode", po.getProjCode());    baseDao.delete("ZcEbProj.deleteZcEbPackByPackCode", po.getPackCode());    baseDao.insert("ZcEbProj.insertZcEbPack", po);  }  public void insert(ZcEbSignup po) {    // TODO Auto-generated method stub    baseDao.delete("ZcEbSignup.deleteZcEbSignup", po.getSignupId());    baseDao.insert("ZcEbSignup.insertZcEbSignup", po);  }  public void insert(ZcEbSignupPackDetail po) {    // TODO Auto-generated method stub    baseDao.delete("ZcEbSignup.deleteZcEbSignupPackDetailBySignupPackId", po.getSignupPackId());    baseDao.insert("ZcEbSignup.insertZcEbSignupPackDetail", po);  }}