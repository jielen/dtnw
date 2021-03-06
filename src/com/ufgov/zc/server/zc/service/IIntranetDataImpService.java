package com.ufgov.zc.server.zc.service;import com.ufgov.zc.common.commonbiz.model.Position;import com.ufgov.zc.common.console.model.AsEmp;import com.ufgov.zc.common.console.model.AsUserGroup;import com.ufgov.zc.common.system.model.AsFile;import com.ufgov.zc.common.system.model.AsNoRule;import com.ufgov.zc.common.system.model.AsNumTool;import com.ufgov.zc.common.system.model.User;import com.ufgov.zc.common.zc.model.ZcEbEcbjItem;import com.ufgov.zc.common.zc.model.ZcEbEcbjPlan;import com.ufgov.zc.common.zc.model.ZcEbEvalParam;import com.ufgov.zc.common.zc.model.ZcEbEvalReport;import com.ufgov.zc.common.zc.model.ZcEbEvalResult;import com.ufgov.zc.common.zc.model.ZcEbExpertOpinion;import com.ufgov.zc.common.zc.model.ZcEbOpenBidTeam;import com.ufgov.zc.common.zc.model.ZcEbOpenBidTeamMember;import com.ufgov.zc.common.zc.model.ZcEbOpenBidTeamPack;import com.ufgov.zc.common.zc.model.ZcEbPack;import com.ufgov.zc.common.zc.model.ZcEbPackEvalResult;import com.ufgov.zc.common.zc.model.ZcEbPackExpert;import com.ufgov.zc.common.zc.model.ZcEbProj;import com.ufgov.zc.common.zc.model.ZcEbProjChange;import com.ufgov.zc.common.zc.model.ZcEbProjectLivingChange;import com.ufgov.zc.common.zc.model.ZcEbSignup;import com.ufgov.zc.common.zc.model.ZcEbSignupPackDetail;public interface IIntranetDataImpService {  public void insert(ZcEbOpenBidTeam po);  public void insert(ZcEbOpenBidTeamMember po);  public void insert(ZcEbOpenBidTeamPack po);  public void insert(ZcEbPackExpert po);  //--评审专家对象的平台表  public void insert(User po);  public void insert(AsUserGroup po);  public void insert(AsEmp po);  public void insert(Position po);  public void insert(ZcEbProjectLivingChange po);  public void insert(ZcEbProjChange po);  public void insert(ZcEbEcbjPlan po);  public void insert(ZcEbEcbjItem po);  public void insert(AsFile po);  public void insert(ZcEbExpertOpinion po);  public void insert(ZcEbEvalResult po);  public void insert(ZcEbEvalReport po);  public void insert(ZcEbPackEvalResult po);  public void insert(ZcEbEvalParam po);  public void insert(AsNumTool po);  public void insert(ZcEbProj po);  public void insert(ZcEbPack po);  public void insert(ZcEbSignup po);  public void insert(ZcEbSignupPackDetail po);  public void insert(AsNoRule po);}