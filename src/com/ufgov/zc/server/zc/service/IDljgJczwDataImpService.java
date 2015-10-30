package com.ufgov.zc.server.zc.service;import java.util.List;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.model.AsFile;import com.ufgov.zc.common.zc.model.ZcEbAuditSheet;import com.ufgov.zc.common.zc.model.ZcEbBidCondition;import com.ufgov.zc.common.zc.model.ZcEbBulletin;import com.ufgov.zc.common.zc.model.ZcEbEntrust;import com.ufgov.zc.common.zc.model.ZcEbEntrustCancel;import com.ufgov.zc.common.zc.model.ZcEbEntrustDetail;import com.ufgov.zc.common.zc.model.ZcEbEvalReport;import com.ufgov.zc.common.zc.model.ZcEbFormula;import com.ufgov.zc.common.zc.model.ZcEbFormulaItem;import com.ufgov.zc.common.zc.model.ZcEbFormulaParam;import com.ufgov.zc.common.zc.model.ZcEbPack;import com.ufgov.zc.common.zc.model.ZcEbPackEvalResult;import com.ufgov.zc.common.zc.model.ZcEbPackReq;import com.ufgov.zc.common.zc.model.ZcEbPackReqDetail;import com.ufgov.zc.common.zc.model.ZcEbPlan;import com.ufgov.zc.common.zc.model.ZcEbProj;import com.ufgov.zc.common.zc.model.ZcEbProjChange;import com.ufgov.zc.common.zc.model.ZcEbProjZbFile;import com.ufgov.zc.common.zc.model.ZcEbProtocol;import com.ufgov.zc.common.zc.model.ZcEbRequirement;import com.ufgov.zc.common.zc.model.ZcEbRequirementDetail;import com.ufgov.zc.common.zc.model.ZcEbSignup;import com.ufgov.zc.common.zc.model.ZcEbSignupPackDetail;import com.ufgov.zc.common.zc.model.ZcEbXunJia;import com.ufgov.zc.common.zc.model.ZcPProMake;import com.ufgov.zc.common.zc.model.ZcPProMitem;import com.ufgov.zc.common.zc.model.ZcPProMitemBi;import com.ufgov.zc.common.zc.model.ZcPProMitemMer;import com.ufgov.zc.common.zc.model.ZcTBchtItem;import com.ufgov.zc.common.zc.model.ZcXmcgHt;public interface IDljgJczwDataImpService {  public abstract void insert(ZcEbEntrust po);  public abstract void insert(ZcEbEntrustDetail po);  public abstract void insert(ZcEbAuditSheet po);  public abstract void insert(ZcEbRequirement po);  public abstract void insert(ZcEbRequirementDetail po);  public abstract void insert(ZcEbProtocol prot);  public abstract void insert(ZcEbProj po);  public abstract void insert(ZcEbPack po);  public abstract void insert(ZcEbPackReq po);  public abstract void insert(ZcEbBidCondition condi);  public abstract void insert(ZcEbXunJia xujia);  public abstract void insert(ZcEbProjZbFile po);  public abstract void insert(ZcEbPlan po);  public abstract void insert(ZcEbFormula po);  public abstract void insert(ZcEbFormulaItem po);  public abstract void insert(ZcEbFormulaParam po);  public abstract void insert(ZcEbBulletin bu);  public abstract void saveWorkFlowData(List list);  public void insert(AsFile po);  public abstract void insert(ZcPProMake po);  public abstract void insert(ZcPProMitem po);  public abstract void insert(ZcPProMitemBi po);  public abstract void insert(ZcPProMitemMer po);  public void insert(ZcEbEvalReport po);  public void insert(ZcEbPackReqDetail po);  public void insert(ZcEbSignup po);  public void insert(ZcEbSignupPackDetail po);  public void insert(ZcEbPackEvalResult po);  public String checkZcEbEntrust(List list);  public String insert(ZcEbProjChange po);  public void insert(ZcXmcgHt po, RequestMeta requestMeta);  public void insert(ZcTBchtItem po);  public String insert(ZcEbEntrustCancel po);}