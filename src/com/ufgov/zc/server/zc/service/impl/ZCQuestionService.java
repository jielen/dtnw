package com.ufgov.zc.server.zc.service.impl;import java.math.BigDecimal;import java.util.HashMap;import java.util.List;import com.kingdrive.workflow.context.WorkflowContext;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.constants.NumLimConstants;import com.ufgov.zc.common.system.constants.ZcSettingConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.model.AsWfDraft;import com.ufgov.zc.common.zc.model.ZCQuestion;import com.ufgov.zc.common.zc.model.ZCQuestionPack;import com.ufgov.zc.common.zc.model.ZcEvaluation;import com.ufgov.zc.server.system.dao.IWorkflowDao;import com.ufgov.zc.server.system.util.NumLimUtil;import com.ufgov.zc.server.system.util.NumUtil;import com.ufgov.zc.server.system.workflow.WFEngineAdapter;import com.ufgov.zc.server.zc.dao.IZCQuestionDao;import com.ufgov.zc.server.zc.dao.ibatis.BaseDao;import com.ufgov.zc.server.zc.service.IZCQuestionService;/** *  * @author wuwb *  */public class ZCQuestionService implements IZCQuestionService {  private IZCQuestionDao izcQuestionDao;  private IWorkflowDao workflowDao;  private WFEngineAdapter wfEngineAdapter;  private BaseDao baseDao;  public BaseDao getBaseDao() {    return baseDao;  }  public void setBaseDao(BaseDao baseDao) {    this.baseDao = baseDao;  }  public IZCQuestionDao getIzcQuestionDao() {    return izcQuestionDao;  }  public void setIzcQuestionDao(IZCQuestionDao izcQuestionDao) {    this.izcQuestionDao = izcQuestionDao;  }  public List getZCQuestionList(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {//    if (requestMeta.containRole(ZcSettingConstants.ROLE_GYS_NORMAL)) {//      elementConditionDto.setNumLimitStr("temp3='" + elementConditionDto.getExecutor() + "'");//    } else {//      elementConditionDto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(elementConditionDto.getWfcompoId(), NumLimConstants.FWATCH));//    }    elementConditionDto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(elementConditionDto.getWfcompoId(), NumLimConstants.FWATCH));    List quesList = izcQuestionDao.getZCQuestionList(elementConditionDto);    return quesList;  }  public List getZCEbEvaluationList(ElementConditionDto elementConditionDto) {    List quesList = izcQuestionDao.getZCEbEvaluationList(elementConditionDto);    return quesList;  }  public void saveEvaluationList(ZcEvaluation zcEvaluation) {    izcQuestionDao.saveEvaluationList(zcEvaluation);  }  public void deleteZcEbQuestion(ZCQuestion zcQuestion) {    if (zcQuestion.getJbFileName() != null && zcQuestion.getJbFileName().length() > 0) {      baseDao.delete("AsFile.deleteAsFileById", zcQuestion.getJbFileName());    }    if (zcQuestion.getClientFileName() != null && zcQuestion.getClientFileName().length() > 0) {      baseDao.delete("AsFile.deleteAsFileById", zcQuestion.getClientFileName());    }    izcQuestionDao.deleteZcEbQuestion(zcQuestion);  }  public ZCQuestion saveZCQuestion(ZCQuestion zcQuestion,  RequestMeta requestMeta) {    String userId = requestMeta.getSvUserID();    String compoId = requestMeta.getCompoId();    if (zcQuestion.getProcessInstId() == null) {      String code = NumUtil.getInstance().getNo(compoId, "QUES_ID", zcQuestion);      Long draftid = workflowDao.createDraftId();      zcQuestion.setProcessInstId(draftid);      AsWfDraft asWfDraft = new AsWfDraft();      asWfDraft.setCompoId(compoId);      asWfDraft.setWfDraftName(code);      asWfDraft.setUserId(userId);      asWfDraft.setMasterTabId(compoId);      asWfDraft.setWfDraftId(BigDecimal.valueOf(draftid.longValue()));      workflowDao.insertAsWfdraft(asWfDraft);      zcQuestion.setTemp3(userId);      zcQuestion.setQuesId(code);      izcQuestionDao.addZCQuestion(zcQuestion);      List list = zcQuestion.getListPack();      if (list != null) {        for (int i = 0; i < list.size(); i++) {          ZCQuestionPack pack = (ZCQuestionPack) list.get(i);          pack.setQuesId(zcQuestion.getQuesId());          izcQuestionDao.insertPack(pack);        }      }    } else {      izcQuestionDao.updateZCQuestion(zcQuestion);      izcQuestionDao.delPack(zcQuestion);      List list = zcQuestion.getListPack();      if (list != null) {        for (int i = 0; i < list.size(); i++) {          ZCQuestionPack pack = (ZCQuestionPack) list.get(i);          pack.setQuesId(zcQuestion.getQuesId());          izcQuestionDao.insertPack(pack);        }      }    }    return zcQuestion;  }  public List getZCQuestionPack(ZCQuestion zcQuestion) {    return null;  }  public ZCQuestion auditPassFN(ZCQuestion zcQuestion, RequestMeta requestMeta) {    wfEngineAdapter.commit(zcQuestion.getComment(), zcQuestion, requestMeta);    return zcQuestion;  }  public ZCQuestion callbackFN(ZCQuestion zcQuestion, RequestMeta requestMeta) {    wfEngineAdapter.callback(zcQuestion.getComment(), zcQuestion, requestMeta);    return zcQuestion;  }  public ZCQuestion newCommitFN(ZCQuestion zcQuestion, RequestMeta requestMeta) {    WorkflowContext workflowContext = wfEngineAdapter.genCommonWFC(    zcQuestion.getComment(), zcQuestion, requestMeta);    wfEngineAdapter.newCommit(workflowContext, zcQuestion);    return zcQuestion;  }  public ZCQuestion submitFN(ZCQuestion zcQuestion, RequestMeta requestMeta) {    return zcQuestion;  }  public ZCQuestion suggestPassFN(ZCQuestion zcQuestion, String type, RequestMeta requestMeta) {    HashMap quesMap=new HashMap();    quesMap.put("sendUser", type);    quesMap.put("quesId",  zcQuestion.getQuesId());    this.baseDao.update("ZCQuestion.updateSendUser", quesMap);    /*    if ("1".equals(type)) {      // 受理      this.baseDao.update("ZCQuestion.updateDoAccepted", zcQuestion.getQuesId());    } else if ("2".equals(type)) {      // 不受理      this.baseDao.update("ZCQuestion.updateDoBack", zcQuestion.getQuesId());    } else if ("3".equals(type)) {      // 转项目负责人    } else if ("4".equals(type)) {      // 送用户单位      this.baseDao.update("ZCQuestion.updateSendUser", zcQuestion.getQuesId());    } else if ("5".equals(type)) {      // 回复质疑      this.baseDao.update("ZCQuestion.updateZCQuestion", zcQuestion);    } else if ("0".equals(type)) {      // 审核通过    }*/    wfEngineAdapter.commit(zcQuestion.getComment(), zcQuestion, requestMeta);    return zcQuestion;  }  public ZCQuestion unAuditFN(ZCQuestion zcQuestion, RequestMeta requestMeta) {    wfEngineAdapter.unAudit(zcQuestion.getComment(), zcQuestion, requestMeta);    return zcQuestion;  }  public ZCQuestion unTreadFN(ZCQuestion zcQuestion, RequestMeta requestMeta) {    wfEngineAdapter.untread(zcQuestion.getComment(), zcQuestion, requestMeta);    return zcQuestion;  }  public IWorkflowDao getWorkflowDao() {    return workflowDao;  }  public void setWorkflowDao(IWorkflowDao workflowDao) {    this.workflowDao = workflowDao;  }  public WFEngineAdapter getWfEngineAdapter() {    return wfEngineAdapter;  }  public void setWfEngineAdapter(WFEngineAdapter wfEngineAdapter) {    this.wfEngineAdapter = wfEngineAdapter;  }  public ZCQuestion getZCQuestionByKey(String id) {    // TODO Auto-generated method stub    List list = baseDao.query("ZCQuestion.getZCQuestionByKey", id);    if (list == null || list.size() == 0) {      return null;    }    ZCQuestion q = (ZCQuestion) list.get(0);    q.setListPack(baseDao.query("ZCQuestion.getPackList", id));    return q;  }}