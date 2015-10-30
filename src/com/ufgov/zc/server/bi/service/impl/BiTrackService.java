package com.ufgov.zc.server.bi.service.impl;import java.math.BigDecimal;import java.util.ArrayList;import java.util.Date;import java.util.List;import com.ufgov.zc.common.bi.model.BiTrack;import com.ufgov.zc.common.system.constants.BillTypeConstants;import com.ufgov.zc.common.system.constants.BusinessOptionConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.exception.BalanceShortageException;import com.ufgov.zc.common.system.exception.BusinessException;import com.ufgov.zc.common.system.exception.DataAlreadyDeletedException;import com.ufgov.zc.common.system.model.AsWfDraft;import com.ufgov.zc.server.bi.dao.IBiTrackDao;import com.ufgov.zc.server.bi.service.IBiTrackService;import com.ufgov.zc.server.commonbiz.dao.IBiBalanceDao;import com.ufgov.zc.server.commonbiz.helper.BiBalanceUpdateForBiTrack;import com.ufgov.zc.server.system.dao.IAsOptionDao;import com.ufgov.zc.server.system.dao.IWorkflowDao;import com.ufgov.zc.server.system.util.AutoNum;import com.ufgov.zc.server.system.util.NumUtil;import com.ufgov.zc.server.system.util.RequestMetaUtil;import com.ufgov.zc.server.system.workflow.WFEngineAdapter;public class BiTrackService implements IBiTrackService {  private IAsOptionDao asOptionDao;  private IBiTrackDao biTrackDao;  private WFEngineAdapter wfEngineAdapter;  private IWorkflowDao workflowDao;  private IBiBalanceDao biBalanceDao;  public IBiBalanceDao getBiBalanceDao() {    return biBalanceDao;  }  public void setBiBalanceDao(IBiBalanceDao biBalanceDao) {    this.biBalanceDao = biBalanceDao;  }  public IWorkflowDao getWorkflowDao() {    return workflowDao;  }  public void setWorkflowDao(IWorkflowDao workflowDao) {    this.workflowDao = workflowDao;  }  public WFEngineAdapter getWfEngineAdapter() {    return wfEngineAdapter;  }  public void setWfEngineAdapter(WFEngineAdapter wfEngineAdapter) {    this.wfEngineAdapter = wfEngineAdapter;  }  public List getBiTrackForQueryData(BiTrack biTrack) {    return biTrackDao.getBiTrackForQueryData(biTrack);  }  public IBiTrackDao getBiTrackDao() {    return biTrackDao;  }  public void setBiTrackDao(IBiTrackDao biTrackDao) {    this.biTrackDao = biTrackDao;  }  public List getBiTrackForQueryData(ElementConditionDto elementConditionDto) {    return biTrackDao.getBiTrackForQueryData(elementConditionDto);  }  public List getBitrackForBc(ElementConditionDto elementConditionDto) {    return biTrackDao.getBitrackForBc(elementConditionDto);  }  public void increasePrintTimes(String biTrackId) {    biTrackDao.increasePrintTimes(biTrackId);  }  public List getBiTrackForDbiTbiCtrlAudit(ElementConditionDto dto) {    return biTrackDao.getBiTrackForDbiTbiCtrlAudit(dto);  }  /**   * 指标审核   */  public BiTrack auditForBiDbiAudit(BiTrack biTrack) {    wfEngineAdapter.newCommit(biTrack.getComment(), biTrack, null);    if (this.workflowDao.isFinalAudit(getNewPorcessInstId(biTrack))) {      BiBalanceUpdateForBiTrack biBalanceHelper = new BiBalanceUpdateForBiTrack(getBillTypeCode(biTrack      .getBiLevel()), RequestMetaUtil.getCompoId());      biBalanceHelper.updateBibalanceForBiTrackEditAudit(biTrack);      biTrack.setAuditorId(RequestMetaUtil.getSvUserID());      biTrack.setAuditorName(RequestMetaUtil.getSvUserName());      biTrack.setadate(new Date());      biTrackDao.updateBiTrackForAudit(biTrack);    }    return biTrack;  }  private Long getNewPorcessInstId(BiTrack biTrack) {    BiTrack newBiTrack = biTrackDao.getBiTrackById(biTrack.getBiTrackId());    Long sLong = null;    if (newBiTrack != null) {      sLong = newBiTrack.getProcessInstId();    } else {      throw new BusinessException("指标 " + biTrack.getBiTrackId() + " 没有找到！");    }    return sLong;  }  /**   * 指标销审   */  public BiTrack unAuditForBiTrack(BiTrack biTrack) {    if (this.workflowDao.isFinalAudit(biTrack.getProcessInstId())) {      BiBalanceUpdateForBiTrack biBalanceHelper = new BiBalanceUpdateForBiTrack(getBillTypeCode(biTrack      .getBiLevel()), RequestMetaUtil.getCompoId());      biBalanceHelper.reworkUpdateAstatusCode(biTrack);      biBalanceHelper.reworkProcessBalanceForBiEdit(biTrack);    }    biTrack.setCancelerId(RequestMetaUtil.getSvUserID());    biTrack.setCancelDate(new Date());    biTrackDao.updateBiTrackForBiBalanceIdToNull(biTrack);    wfEngineAdapter.unAudit(biTrack.getComment(), biTrack, null);    return biTrack;  }  /**   * 指标退回   */  public BiTrack untreadForBiTrack(BiTrack biTrack) {    // TODO 增加业务逻辑    wfEngineAdapter.untreadGk(biTrack.getComment(), biTrack, null);    return biTrack;  }  /**   * 指标作废   * @param biTrack   * @return   */  public BiTrack invalidateForBiTrack(BiTrack biTrack) {    String strValue = null;    BiBalanceUpdateForBiTrack biBalanceHelper = new BiBalanceUpdateForBiTrack(getBillTypeCode(biTrack    .getBiLevel()), RequestMetaUtil.getCompoId());    biBalanceHelper.invalidBiTrackForBiEdit(biTrack);    String delFlag = asOptionDao.getAsOption(BusinessOptionConstants.OPT_CP_DEL_INVALIDATION).getOptVal();    if (delFlag.equals("1")) {      strValue = "9";    } else {      strValue = "0";    }    int iRow = biTrackDao.updateSelfBiTrackForInvalidate(strValue, biTrack.getBiTrackId());    if (iRow != 1) {      throw new BusinessException("作废指标记录失败！");    }    if (biTrack.getProcessInstId().longValue() > 0) {      wfEngineAdapter.interrupt(biTrack.getComment(), biTrack, null);    }    return biTrack;  }  /**   * 指标删除   * @param biTrack   * @return   */  public BiTrack deleteForBiTrack(BiTrack biTrack) {    // TODO 增加业务逻辑    BiBalanceUpdateForBiTrack biBalanceHelper = new BiBalanceUpdateForBiTrack(getBillTypeCode(biTrack    .getBiLevel()), RequestMetaUtil.getCompoId());    biBalanceHelper.updateBiTrackForBiDelete(biTrack);    biTrackDao.deleteBiTrack(biTrack);    return biTrack;  }  public List getBiTrackForDbiTbiEdit(ElementConditionDto elementConditionDto) {    return biTrackDao.getBiTrackForDbiTbiEdit(elementConditionDto);  }  public BiTrack saveBiTrackForDbiTbiBAF(BiTrack biTrack) {    BigDecimal subtractValue = biTrack.getOrgMoney();    String saveOption = asOptionDao.getAsOption(BusinessOptionConstants.OPT_CP_DECBAL_FLAG_BI).getOptVal();    if (biTrack.getBiTrackId() == null || biTrack.getBiTrackId().trim().equals("")) {      String userId = RequestMetaUtil.getSvUserID();      String compoId = RequestMetaUtil.getCompoId();      Long draftid = workflowDao.createDraftId();      biTrack.setProcessInstId(draftid);      AsWfDraft asWfDraft = new AsWfDraft();      asWfDraft.setCompoId(compoId);      asWfDraft.setWfDraftName(biTrack.getTitleField());      asWfDraft.setUserId(userId);      asWfDraft.setMasterTabId("BI_TRACK");      asWfDraft.setWfDraftId(BigDecimal.valueOf(draftid.longValue()));      workflowDao.insertAsWfdraft(asWfDraft);      if (isHandleBalance(biTrack)) {        handleBalance(biTrack.getOriginBalanceId(), subtractValue, saveOption, biTrack.getBiLevel());//先处理余额      }      biTrack.setBiTrackId(NumUtil.getInstance().getNo(compoId, "BI_TRACK_ID", biTrack));      biTrack.setCdate(new Date());      this.biTrackDao.insertBiTrack(biTrack);    } else {      BiTrack oldBiTrack = biTrackDao.getBiTrackById(biTrack.getBiTrackId());      if (oldBiTrack == null) {        throw new DataAlreadyDeletedException("id为：" + biTrack.getBiTrackId() + "的数据已被删除！");      }      biTrackDao.updateBiTrack(biTrack);      subtractValue = biTrack.getCurMoney().subtract(oldBiTrack.getCurMoney());      if (isUpdateBalance(biTrack)) {        handleBalance(biTrack.getOriginBalanceId(), subtractValue, saveOption, biTrack.getBiLevel());      }    }    return biTrack;  }  public BiTrack saveAndAuditBiTrackBAF(BiTrack biTrack) {    this.saveBiTrackForDbiTbiBAF(biTrack);    this.auditForBiDbiAudit(biTrack);    return biTrack;  }  private void handleBalance(String balanceId, BigDecimal subtractValue, String saveOption, String biLevel) {    String compoId = RequestMetaUtil.getCompoId();    if (saveOption.equalsIgnoreCase("0")) {      int iRow = biBalanceDao.updateBiBalanceForBiDbiAudit(subtractValue, balanceId);      if (iRow == 0) {        throw new BalanceShortageException("余额" + balanceId + "余额不足！");      }    }  }  private boolean isHandleBalance(BiTrack biTrack) {    boolean isHandleBalance = true;    if (biTrack.getCurMoney().doubleValue() < 0 || !isUpdateBiAbiBalance(biTrack)) {      isHandleBalance = false;    }    return isHandleBalance;  }  private boolean isUpdateBalance(BiTrack biTrack) {    boolean isUpdateBalance = true;    BiTrack oldBiTrack = biTrackDao.getBiTrackById(biTrack.getBiTrackId());    if (oldBiTrack == null) {      throw new DataAlreadyDeletedException("id为：" + biTrack.getBiTrackId() + "的数据已被删除！");    }    if (biTrack.getCurMoney().doubleValue() < 0 || oldBiTrack.getCurMoney().doubleValue() < 0    || !isUpdateBiAbiBalance(biTrack)) {      isUpdateBalance = false;    }    return isUpdateBalance;  }  private String getBillTypeCode(String biLevel) {    String billTypeCodeString = null;    if (biLevel.equals("01")) {      billTypeCodeString = BillTypeConstants.BILL_TYPE_CODE_BI_ABI_QUERY;    } else if (biLevel.equals("02")) {      billTypeCodeString = BillTypeConstants.BILL_TYPE_CODE_BI_DBI_TBI_BALANCE;    } else {      billTypeCodeString = BillTypeConstants.BILL_TYPE_CODE_BI_DBI_EDIT;    }    return billTypeCodeString;  }  /**   * 判断新增的总指标是否修改来源指标余额记录，例外：如果是总指标划拨，要修改来源指标余额记录，其他新增总指标暂不修改   * @param biTrack   * @return   */  private boolean isUpdateBiAbiBalance(BiTrack biTrack) {    boolean isUpdate = true;    if (biTrack.getBiLevel().equals("01")) {      if (biTrack.getBiAdjustCode().equals("501")) {        isUpdate = true;      } else {        isUpdate = false;      }    }    return isUpdate;  }  public void callbackForBiEdit(BiTrack biTrack) {    wfEngineAdapter.callback(biTrack.getComment(), biTrack, null);  }  public IAsOptionDao getAsOptionDao() {    return asOptionDao;  }  public void setAsOptionDao(IAsOptionDao asOptionDao) {    this.asOptionDao = asOptionDao;  }  public List getAbiTrackList(ElementConditionDto elementConditionDto) {    return biTrackDao.getAbiTrackList(elementConditionDto);  }  public List getBiTrackListByTargetBalanceId(String targetBalanceId) {    return biTrackDao.getBiTrackListByTargetBalanceId(targetBalanceId);  }  public void saveBiTrackForAdjust(List bitracks) {    int size = bitracks.size();    BiTrack biTrack = null;    String inputGroupId = "";    List childs = null;    for (int i = 0; i < size; i++) {      biTrack = (BiTrack) bitracks.get(i);      String userId = RequestMetaUtil.getSvUserID();      String compoId = RequestMetaUtil.getCompoId();      Long draftid = workflowDao.createDraftId();      biTrack.setProcessInstId(draftid);      AsWfDraft asWfDraft = new AsWfDraft();      asWfDraft.setCompoId(compoId);      asWfDraft.setWfDraftName(biTrack.getTitleField());      asWfDraft.setUserId(userId);      asWfDraft.setMasterTabId("BI_TRACK");      asWfDraft.setWfDraftId(BigDecimal.valueOf(draftid.longValue()));      workflowDao.insertAsWfdraft(asWfDraft);      inputGroupId = NumUtil.getInstance().getNo(compoId, "INPUT_GROUP_ID", biTrack);      biTrack.setInputGroupId(inputGroupId);      biTrack.setBiTrackId(AutoNum.genNum(compoId, biTrack));      biTrack.setastatusCode("0");      biTrack.setCdate(new Date());      biTrack.setBiAdjustCode("302");      this.biTrackDao.insertBiTrack(biTrack);      childs = biTrack.getChilds();      for (int j = 0; j < childs.size(); j++) {        biTrack = (BiTrack) childs.get(j);        biTrack.setInputGroupId(inputGroupId);        biTrack.setBiTrackId(AutoNum.genNum(compoId, biTrack));        biTrack.setastatusCode("0");        biTrack.setCdate(new Date());        biTrack.setBiAdjustCode("301");        this.biTrackDao.insertBiTrack(biTrack);      }    }  }  public void deleteBiTrackForAdj(List bitracks) {    this.biTrackDao.deleteBiTrackForAdj(bitracks);  }  public void updateBiTrackForAdj(final List bitracks) {    this.biTrackDao.updateBiTrackForAdj(bitracks);  }  public List getBiTrackForAdjAsc(String groupId) {    return this.biTrackDao.getBiTrackForAdjAsc(groupId);  }  public int deleteBiTrack(BiTrack biTrack) {    return this.biTrackDao.deleteBiTrack(biTrack);  }  public void auditForBiXbiAdjAudit(BiTrack biTrack) {    wfEngineAdapter.newCommit(biTrack.getComment(), biTrack, null);    ElementConditionDto dto = new ElementConditionDto();    dto.setBiAdjustCode("301");    dto.setInputGroupId(biTrack.getInputGroupId());    dto.setNd(biTrack.getNd());    dto.setBiLevel(biTrack.getBiLevel());    List increaseBiTrack = biTrackDao.getBiTrackForXbiAdjAudit(dto);    boolean isFinalAudit = this.workflowDao.isFinalAudit(getNewPorcessInstId(biTrack));    BiBalanceUpdateForBiTrack biBalanceHelper = new BiBalanceUpdateForBiTrack(getBillTypeCode(biTrack    .getBiLevel()), RequestMetaUtil.getCompoId());    BiTrack inTrack = null;    for (int i = 0; i < increaseBiTrack.size(); i++) {      inTrack = (BiTrack) increaseBiTrack.get(i);      this.biTrackDao.updateBiTrackAstatusCode(getNewAstatusCode(biTrack), inTrack.getBiTrackId());      inTrack.setAuditorId(RequestMetaUtil.getSvUserID());      inTrack.setAuditorName(RequestMetaUtil.getSvUserName());      inTrack.setadate(RequestMetaUtil.getTransDate());      biTrackDao.updateBiTrackForAudit(inTrack);      if (isFinalAudit) {        biBalanceHelper.updateBibalanceForBiXbiAdjAudit(inTrack);      }    }    biTrack.setAuditorId(RequestMetaUtil.getSvUserID());    biTrack.setAuditorName(RequestMetaUtil.getSvUserName());    biTrack.setadate(RequestMetaUtil.getTransDate());    biTrackDao.updateBiTrackForAudit(biTrack);  }  private String getNewAstatusCode(BiTrack biTrack) {    BiTrack newBiTrack = biTrackDao.getBiTrackById(biTrack.getBiTrackId());    String aStatuscode = null;    if (newBiTrack != null) {      aStatuscode = newBiTrack.getastatusCode();    } else {      throw new BusinessException("指标 " + biTrack.getBiTrackId() + " 没有找到！");    }    return aStatuscode;  }  public void unAuditForBiXbiAdj(BiTrack biTrack) {    boolean isFinalAudit = this.workflowDao.isFinalAudit(getNewPorcessInstId(biTrack));    ElementConditionDto dto = new ElementConditionDto();    dto.setBiAdjustCode("301");    dto.setInputGroupId(biTrack.getInputGroupId());    dto.setNd(RequestMetaUtil.getSvNd());    dto.setBiLevel(biTrack.getBiLevel());    List increaseBiTrack = biTrackDao.getBiTrackForXbiAdjAudit(dto);    BiBalanceUpdateForBiTrack biBalanceHelper = new BiBalanceUpdateForBiTrack(getBillTypeCode(biTrack    .getBiLevel()), RequestMetaUtil.getCompoId());    BiTrack inTrack = null;    for (int i = 0; i < increaseBiTrack.size(); i++) {      inTrack = (BiTrack) increaseBiTrack.get(i);      if (isFinalAudit) {        biBalanceHelper.reworkUpdateAstatusCode(inTrack);        biBalanceHelper.reworkProcessBalanceForBiBiAdj(inTrack);        inTrack.setCancelerId(RequestMetaUtil.getSvUserID());        inTrack.setCancelDate(RequestMetaUtil.getTransDate());        biTrackDao.updateBiTrackForBiBalanceIdToNull(inTrack);        inTrack.setAuditorId(null);        inTrack.setAuditorName(null);        inTrack.setadate(null);        biTrackDao.updateBiTrackForAudit(inTrack);      }    }    biBalanceHelper.reworkUpdateAstatusCode(biTrack);    biBalanceDao.updateBiBalanceForAdjUnAudit(biTrack.getCurMoney(), biTrack.getOriginBalanceId());    wfEngineAdapter.unAudit(biTrack.getComment(), biTrack, null);    biTrack.setAuditorId(null);    biTrack.setAuditorName(null);    biTrack.setadate(null);    biTrackDao.updateBiTrackForAudit(biTrack);  }  public void invalidateForBiXbiAdj(BiTrack biTrack) {    String strValue = null;    ElementConditionDto dto = new ElementConditionDto();    dto.setBiAdjustCode("301");    dto.setInputGroupId(biTrack.getInputGroupId());    dto.setNd(RequestMetaUtil.getSvNd());    dto.setBiLevel(biTrack.getBiLevel());    List increaseBiTrack = biTrackDao.getBiTrackForXbiAdjAudit(dto);    String delFlag = asOptionDao.getAsOption(BusinessOptionConstants.OPT_CP_DEL_INVALIDATION).getOptVal();    if (delFlag.equals("1")) {      strValue = "9";    } else {      strValue = "0";    }    BiTrack inTrack = null;    for (int i = 0; i < increaseBiTrack.size(); i++) {      inTrack = (BiTrack) increaseBiTrack.get(i);      biTrackDao.updateSelfBiTrackForInvalidate(strValue, inTrack.getBiTrackId());    }    int iRow = biTrackDao.updateSelfBiTrackForInvalidate(strValue, biTrack.getBiTrackId());    if (iRow != 1) {      throw new BusinessException("作废指标记录失败！");    }    if (biTrack.getProcessInstId().longValue() > 0) {      wfEngineAdapter.interrupt(biTrack.getComment(), biTrack, null);    }  }  public List getAllBiTrack(ElementConditionDto conditionDto) {    return biTrackDao.getAllBiTrack(conditionDto);  }  public BiTrack getRootTrackByTargetBalanceId(String balanceId) {    BiTrack track = biTrackDao.getBiTrackByTargetBalanceId(balanceId);    String origBalanceId = track.getOriginBalanceId();    if (origBalanceId == null || origBalanceId.length() == 0) {      return track;    } else {      return getRootTrackByTargetBalanceId(origBalanceId);    }  }  public BiTrack getTraceRootBiTrack(String balanceId) {    BiTrack track = this.getRootTrackByTargetBalanceId(balanceId);    insertChildTrack(track);    return track;  }  private void insertChildTrack(BiTrack track) {    BiTrack tempTrack = null;    List childTracks = new ArrayList();    if (!"03".equals(track.getBiLevel()) && !"202".equals(track.getBiAdjustCode())    && !"302".equals(track.getBiAdjustCode())) {      childTracks = biTrackDao.getBiTrackListByTargetBalanceId(track.getTargetBalanceId());      for (int i = 0; i < childTracks.size(); i++) {        tempTrack = (BiTrack) childTracks.get(i);        insertChildTrack(tempTrack);      }    }    track.setChilds(childTracks);  }  public BigDecimal getHasAdjuseMoney(String balanceId) {    return biTrackDao.getHasAdjuseMoney(balanceId);  }}