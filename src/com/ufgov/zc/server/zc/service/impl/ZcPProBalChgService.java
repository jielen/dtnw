package com.ufgov.zc.server.zc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.NumLimConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.BusinessException;
import com.ufgov.zc.common.system.model.AsWfDraft;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.zc.exception.ZcBudgetInterfaceException;
import com.ufgov.zc.common.zc.model.ZcPProBalChg;
import com.ufgov.zc.common.zc.model.ZcPProMitemBiChg;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcPProMitemBiExample;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBiChg;
import com.ufgov.zc.server.budget.util.BudgetUtil;
import com.ufgov.zc.server.system.dao.IWorkflowDao;
import com.ufgov.zc.server.system.util.NumLimUtil;
import com.ufgov.zc.server.system.util.NumUtil;
import com.ufgov.zc.server.system.workflow.WFEngineAdapter;
import com.ufgov.zc.server.zc.ZcSUtil;
import com.ufgov.zc.server.zc.dao.ibatis.BaseDao;
import com.ufgov.zc.server.zc.service.IZcPProBalChgService;
import com.ufgov.zc.server.zc.service.IZcPProMakeService;

public class ZcPProBalChgService implements IZcPProBalChgService {
  private BaseDao baseDao;

  private WFEngineAdapter wfEngineAdapter;

  private IWorkflowDao workflowDao;

  private IZcPProMakeService zcPProMakeService;

  /**
   * @return the zcPProMakeService
   */
  public IZcPProMakeService getZcPProMakeService() {
    return zcPProMakeService;
  }

  /**
   * @param zcPProMakeService the zcPProMakeService to set
   */
  public void setZcPProMakeService(IZcPProMakeService zcPProMakeService) {
    this.zcPProMakeService = zcPProMakeService;
  }

  public List getZcPProBalChgList(ElementConditionDto dto, RequestMeta meta) {
    dto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(dto.getWfcompoId(), NumLimConstants.FWATCH));
    return baseDao.query("ZC_P_PRO_BAL_CHG.selectZcPProBalChgList", dto);
  }

  public void deleteZcPProBalChgFN(ZcPProBalChg zcPProBalChg, String serverAdd, boolean flag, RequestMeta meta) throws Exception {
    //    Map map = new BudgetUtil().getBalChgDelBudget(baseDao, zcPProBalChg); 
    baseDao.delete("ZC_P_PRO_BAL_CHG.abatorgenerated_deleteByPrimaryKey", zcPProBalChg);
    baseDao.delete("ZC_P_PRO_MITEM_BI_HISTORY.deleteZcPProMitemBiHisByChgId", zcPProBalChg.getBalChgId());
    baseDao.delete("ZC_XMCG_HT_BI_HISTORY.deleteHtBiHisByChgId", zcPProBalChg.getBalChgId());
    //    new BudgetUtil().callService(map, meta.getSvNd());

  }

  public ZcPProBalChg updateZcPProBalChgFN(ZcPProBalChg zcPProBalChg, String serverAdd, boolean flag, RequestMeta meta) throws Exception {

    //更新采购计划、原采购计划中的资金构成到历史记录表中、保存采购合同中的资金构成情况
    String userId = meta.getSvUserID();

    String compoId = meta.getCompoId();

    boolean isDraft = false;

    if (zcPProBalChg.getProcessInstId() == null || zcPProBalChg.getProcessInstId().longValue() == -1) {
      Long draftid = workflowDao.createDraftId();
      zcPProBalChg.setProcessInstId(draftid);
      isDraft = true;

    }

    // 生成接口数据
    Map map = null;
    BigDecimal zero = new BigDecimal(0);

    if ("".equals(ZcSUtil.safeString(zcPProBalChg.getBalChgId())) || zcPProBalChg.getBalChgId().equals("自动编号")) {
      String balChgId = NumUtil.getInstance().getNo(compoId, "BAL_CHG_ID", zcPProBalChg);
      zcPProBalChg.setBalChgId(balChgId);

      //      map = new BudgetUtil().getBalChgSaveBudget(baseDao, zcPProBalChg);

      baseDao.insert("ZC_P_PRO_BAL_CHG.abatorgenerated_insert", zcPProBalChg);
      //setZcPProChgBiList
      List biList = zcPProBalChg.getZcPProChgBiList();
      Hashtable seqMap = new Hashtable();
      for (int i = 0; i < biList.size(); i++) {
        ZcPProMitemBiChg chg = (ZcPProMitemBiChg) biList.get(i);
        if (chg.getZcProBiSeq() == null || chg.getZcProBiSeq().trim().length() == 0) {
          String seq = ZcSUtil.getSequenceNextVal("ZcEbUtil.getZcProBiNextSeqVal");
          chg.setZcProBiSeq(seq);
          seqMap.put(chg.getZcBiNo(), seq);
        }
        if (zero.compareTo(chg.getZcBiJhuaSum()) == 0) {
          continue;
        }
        chg.setChgId(balChgId);
        chg.setFlag(ZcPProMitemBiChg.FLAG_NEW);
        baseDao.insert("ZC_P_PRO_MITEM_BI_HISTORY.insertIntoZcPProMitemBiHis", chg);
      }
      biList = zcPProBalChg.getOldBiList();
      for (int i = 0; i < biList.size(); i++) {
        ZcPProMitemBiChg chg = (ZcPProMitemBiChg) biList.get(i);
        chg.setChgId(balChgId);
        chg.setFlag(ZcPProMitemBiChg.FLAG_HISTORY);
        //        chg.setZcMakeCode(zcPProBalChg.getZcMakeCode());
        baseDao.insert("ZC_P_PRO_MITEM_BI_HISTORY.insertIntoZcPProMitemBiHis", chg);
      }

      //      chg.getHtBiHistoryLst().put(htCode, historyLst);
      //      chg.getHtBiLst().put(htCode, chgLst);
      if (zcPProBalChg.getHtBiHistoryLst() != null) {
        Enumeration keys = zcPProBalChg.getHtBiHistoryLst().keys();
        while (keys.hasMoreElements()) {
          List htOldBiLst = (List) zcPProBalChg.getHtBiHistoryLst().get(keys.nextElement());
          if (htOldBiLst != null) {
            for (int i = 0; i < htOldBiLst.size(); i++) {
              ZcXmcgHtBiChg chg = (ZcXmcgHtBiChg) htOldBiLst.get(i);
              chg.setChgId(balChgId);
              chg.setFlag(ZcPProMitemBiChg.FLAG_HISTORY);
              baseDao.insert("ZC_XMCG_HT_BI_HISTORY.insertIntoHtBiHistory", chg);
            }
          }
        }
      }
      if (zcPProBalChg.getHtBiLst() != null) {
        Enumeration keys = zcPProBalChg.getHtBiLst().keys();
        while (keys.hasMoreElements()) {
          List htNewBiLst = (List) zcPProBalChg.getHtBiLst().get(keys.nextElement());
          if (htNewBiLst != null) {
            for (int i = 0; i < htNewBiLst.size(); i++) {
              ZcXmcgHtBiChg chg = (ZcXmcgHtBiChg) htNewBiLst.get(i);
              if (zero.compareTo(chg.getZcBiBcsySum()) == 0) {
                continue;
              }
              chg.setChgId(balChgId);
              chg.setFlag(ZcPProMitemBiChg.FLAG_NEW);
              if (chg.getZcProBiSeq() == null || chg.getZcProBiSeq().trim().length() == 0) {
                String seq = (String) seqMap.get(chg.getZcBiNo());
                chg.setZcProBiSeq(seq);
              }
              baseDao.insert("ZC_XMCG_HT_BI_HISTORY.insertIntoHtBiHistory", chg);
            }
          }
        }
      }

    } else {

      String chgId = zcPProBalChg.getBalChgId();

      //      map = new BudgetUtil().getBalChgSaveBudget(baseDao, zcPProBalChg);

      baseDao.update("ZC_P_PRO_BAL_CHG.abatorgenerated_updateByPrimaryKeySelective", zcPProBalChg);

      baseDao.delete("ZC_P_PRO_MITEM_BI_HISTORY.deleteZcPProMitemBiHisByChgId", zcPProBalChg.getBalChgId());
      baseDao.delete("ZC_XMCG_HT_BI_HISTORY.deleteHtBiHisByChgId", zcPProBalChg.getBalChgId());
      List biList = zcPProBalChg.getZcPProChgBiList();
      Hashtable seqMap = new Hashtable();
      for (int i = 0; i < biList.size(); i++) {
        ZcPProMitemBiChg chg = (ZcPProMitemBiChg) biList.get(i);
        if (chg.getZcProBiSeq() == null || chg.getZcProBiSeq().trim().length() == 0) {
          String seq = ZcSUtil.getSequenceNextVal("ZcEbUtil.getZcProBiNextSeqVal");
          chg.setZcProBiSeq(seq);
          seqMap.put(chg.getZcBiNo(), seq);
        }
        if (zero.compareTo(chg.getZcBiJhuaSum()) == 0) {
          continue;
        }
        chg.setChgId(chgId);
        chg.setFlag(ZcPProMitemBiChg.FLAG_NEW);
        baseDao.insert("ZC_P_PRO_MITEM_BI_HISTORY.insertIntoZcPProMitemBiHis", chg);
      }
      biList = zcPProBalChg.getOldBiList();
      for (int i = 0; i < biList.size(); i++) {
        ZcPProMitemBiChg chg = (ZcPProMitemBiChg) biList.get(i);
        chg.setChgId(chgId);
        chg.setFlag(ZcPProMitemBiChg.FLAG_HISTORY);
        //        chg.setZcMakeCode(zcPProBalChg.getZcMakeCode());
        baseDao.insert("ZC_P_PRO_MITEM_BI_HISTORY.insertIntoZcPProMitemBiHis", chg);
      }

      //      chg.getHtBiHistoryLst().put(htCode, historyLst);
      //      chg.getHtBiLst().put(htCode, chgLst);
      if (zcPProBalChg.getHtBiHistoryLst() != null) {
        Enumeration keys = zcPProBalChg.getHtBiHistoryLst().keys();
        while (keys.hasMoreElements()) {
          List htOldBiLst = (List) zcPProBalChg.getHtBiHistoryLst().get(keys.nextElement());
          if (htOldBiLst != null) {
            for (int i = 0; i < htOldBiLst.size(); i++) {
              ZcXmcgHtBiChg chg = (ZcXmcgHtBiChg) htOldBiLst.get(i);
              chg.setChgId(chgId);
              chg.setFlag(ZcPProMitemBiChg.FLAG_HISTORY);
              baseDao.insert("ZC_XMCG_HT_BI_HISTORY.insertIntoHtBiHistory", chg);
            }
          }
        }
      }
      if (zcPProBalChg.getHtBiLst() != null) {
        Enumeration keys = zcPProBalChg.getHtBiLst().keys();
        while (keys.hasMoreElements()) {
          List htNewBiLst = (List) zcPProBalChg.getHtBiLst().get(keys.nextElement());
          if (htNewBiLst != null) {
            for (int i = 0; i < htNewBiLst.size(); i++) {
              ZcXmcgHtBiChg chg = (ZcXmcgHtBiChg) htNewBiLst.get(i);
              if (zero.compareTo(chg.getZcBiBcsySum()) == 0) {
                continue;
              }
              chg.setChgId(chgId);
              chg.setFlag(ZcPProMitemBiChg.FLAG_NEW);
              if (chg.getZcProBiSeq() == null || chg.getZcProBiSeq().trim().length() == 0) {
                String seq = (String) seqMap.get(chg.getZcBiNo());
                chg.setZcProBiSeq(seq);
              }
              baseDao.insert("ZC_XMCG_HT_BI_HISTORY.insertIntoHtBiHistory", chg);
            }
          }
        }
      }
    }

    /*    if (ZcPProBalChgConstants.ZC_P_PRO_BAL_STATUS_UPDATE_HT.equals(zcPProBalChg.getStatus())) {
          //校验合同的分配情况。合同的资金构成的总金额是否等于合同金额
          try {
            updateZcPProMitmBI(zcPProBalChg);
            saveZcHtChgBi(zcPProBalChg);
            savaHistoryMakeBI(zcPProBalChg);
            updateZcXmchHtBi(zcPProBalChg);
            //        return selectByPrimaryKey(zcPProBalChg.getBalChgId(), meta);
          } catch (Exception e) {
            // TCJLODO Auto-generated catch block
            e.printStackTrace();
            throw e;
          }

        }*/

    if (isDraft) {

      AsWfDraft asWfDraft = new AsWfDraft();

      asWfDraft.setCompoId(compoId);

      asWfDraft.setWfDraftName(zcPProBalChg.getBalChgId());

      asWfDraft.setUserId(userId);

      asWfDraft.setMasterTabId(compoId);

      asWfDraft.setWfDraftId(BigDecimal.valueOf(zcPProBalChg.getProcessInstId().longValue()));

      workflowDao.insertAsWfdraft(asWfDraft);

    }

    //在终审时，通过工作流监听器去进行指标核减
    //    new BudgetUtil().callService(map, meta.getSvNd());

    return selectByPrimaryKey(zcPProBalChg.getBalChgId(), meta);

  }

  public ZcPProBalChg selectByPrimaryKey(String balChgId, RequestMeta requestMeta) {
    ZcPProBalChg rtn = (ZcPProBalChg) baseDao.read("ZC_P_PRO_BAL_CHG.abatorgenerated_selectByPrimaryKey", balChgId);

    ElementConditionDto dto = new ElementConditionDto();
    dto.setZcText1(balChgId);
    dto.setZcText2(ZcPProMitemBiChg.FLAG_NEW);
    List newBiLst = baseDao.query("ZC_P_PRO_MITEM_BI_HISTORY.selectZcPProMitemBi", dto);
    rtn.setZcPProChgBiList(newBiLst == null ? new ArrayList() : newBiLst);

    dto = new ElementConditionDto();
    dto.setZcText1(balChgId);
    dto.setZcText2(ZcPProMitemBiChg.FLAG_HISTORY);
    List oldBiList = baseDao.query("ZC_P_PRO_MITEM_BI_HISTORY.selectZcPProMitemBi", dto);
    rtn.setOldBiList(oldBiList == null ? new ArrayList() : oldBiList);

    dto = new ElementConditionDto();
    dto.setZcMakeCode(rtn.getZcMakeCode());
    dto.setZcText3("have");//其他地方的查询需要ht为exec的，后来 采购计划资金变更部件，待安排资金调整时，获取全部的合同，要查全部，所以用zcText3有值无值判定，无值时，是原来的模式，有值时，这个条件就不起作用了 20140915 cjl
    List htLst = baseDao.query("ZC_XMCG_HT.getMainHtByMakeCode", dto);
    htLst = htLst == null ? new ArrayList() : htLst;
    rtn.setXmcgHtList(htLst);

    //合同变更后的资金
    dto = new ElementConditionDto();
    dto.setZcText1(rtn.getBalChgId());
    dto.setZcText2(ZcPProMitemBiChg.FLAG_NEW);
    List biLst = baseDao.query("ZC_XMCG_HT_BI_HISTORY.selectHtBi", dto);
    if (biLst == null || biLst.size() == 0) {
      biLst = new ArrayList();
    }
    Hashtable biMap = new Hashtable();
    for (int i = 0; i < biLst.size(); i++) {
      ZcXmcgHtBiChg bi = (ZcXmcgHtBiChg) biLst.get(i);
      //获取对应的计划资金
      for (int k = 0; k < rtn.getZcPProChgBiList().size(); k++) {
        ZcPProMitemBiChg c = (ZcPProMitemBiChg) rtn.getZcPProChgBiList().get(k);
        if (c.getZcProBiSeq().equals(bi.getZcProBiSeq())) {
          bi.setZcPProMitemBi(c);
          break;
        }
      }
      String htCode = bi.getZcHtCode();
      if (biMap.containsKey(htCode)) {
        List lst = (List) biMap.get(htCode);
        lst.add(bi);
      } else {
        List lst = new ArrayList();
        lst.add(bi);
        biMap.put(htCode, lst);
      }
    }
    rtn.setHtBiLst(biMap);

    dto = new ElementConditionDto();
    dto.setZcText1(rtn.getBalChgId());
    dto.setZcText2(ZcPProMitemBiChg.FLAG_HISTORY);
    List oldBiLst = baseDao.query("ZC_XMCG_HT_BI_HISTORY.selectHtBi", dto);
    if (oldBiLst == null || oldBiLst.size() == 0) {
      oldBiLst = new ArrayList();
    }
    Hashtable oldBiMap = new Hashtable();
    for (int i = 0; i < oldBiLst.size(); i++) {
      ZcXmcgHtBiChg bi = (ZcXmcgHtBiChg) oldBiLst.get(i);
      //获取对应的计划资金
      for (int k = 0; k < rtn.getOldBiList().size(); k++) {
        ZcPProMitemBiChg c = (ZcPProMitemBiChg) rtn.getOldBiList().get(k);
        if (c.getZcProBiSeq().equals(bi.getZcProBiSeq())) {
          bi.setZcPProMitemBi(c);
        }
      }
      String htCode = bi.getZcHtCode();
      if (oldBiMap.containsKey(htCode)) {
        List lst = (List) oldBiMap.get(htCode);
        lst.add(bi);
      } else {
        List lst = new ArrayList();
        lst.add(bi);
        oldBiMap.put(htCode, lst);
      }
    }
    rtn.setHtBiHistoryLst(oldBiMap);
    rtn.setDbDigest(null);
    rtn.setDbDigest(rtn.digest());
    return rtn;

  }

  /**
   * @return the baseDao
   */
  public BaseDao getBaseDao() {
    return baseDao;
  }

  /**
   * @param baseDao the baseDao to set
   */
  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }

  /**
   * @return the wfEngineAdapter
   */
  public WFEngineAdapter getWfEngineAdapter() {
    return wfEngineAdapter;
  }

  /**
   * @param wfEngineAdapter the wfEngineAdapter to set
   */
  public void setWfEngineAdapter(WFEngineAdapter wfEngineAdapter) {
    this.wfEngineAdapter = wfEngineAdapter;
  }

  /**
   * @return the workflowDao
   */
  public IWorkflowDao getWorkflowDao() {
    return workflowDao;
  }

  /**
   * @param workflowDao the workflowDao to set
   */
  public void setWorkflowDao(IWorkflowDao workflowDao) {
    this.workflowDao = workflowDao;
  }

  /**
   * update采购计划中的资金构成情况
   */

  public void updateZcPProMitmBI(ZcPProBalChg zcPProBalChg) throws Exception {

    List biList = zcPProBalChg.getZcPProChgBiList();

    ZcPProMitemBiExample example = new ZcPProMitemBiExample();
    example.createCriteria().andZcMakeCodeEqualTo(zcPProBalChg.getZcMakeCode());
    this.baseDao.delete("ZC_P_PRO_MITEM_BI.ibatorgenerated_deleteByExample", example);

    for (int i = 0; i < biList.size(); i++) {

      ZcPProMitemBiChg chg = (ZcPProMitemBiChg) biList.get(i);
      ZcPProMitemBi bi = new ZcPProMitemBi();
      BeanUtil.commonFieldsCopy(chg, bi);
      bi.setZcMakeCode(zcPProBalChg.getZcMakeCode());

      baseDao.insert("ZC_P_PRO_MITEM_BI.ibatorgenerated_insert", bi);

    }

  }

  /**
   * 保存原采购计划中的资金构成到历史记录表中
   * 
   */
  private void savaHistoryMakeBI(ZcPProBalChg zcPProBalChg) {
    if (zcPProBalChg.getOldBiList() != null && zcPProBalChg.getOldBiList().size() > 0) {
      for (int i = 0; i < zcPProBalChg.getOldBiList().size(); i++) {
        Map map = new HashMap();
        map.put("bi", zcPProBalChg.getOldBiList().get(i));
        map.put("balChgId", zcPProBalChg.getBalChgId());
        map.put("zcMakeCode", zcPProBalChg.getZcMakeCode());
        baseDao.delete("ZC_P_PRO_MITEM_BI_HISTORY.deleteZcPProMitemBiHisByMakeCode", map);
        baseDao.insert("ZC_P_PRO_MITEM_BI_HISTORY.insertIntoZcPProMitemBiHis", map);
      }
    }
  }

  /**
   * 保存调节的合同资金构成
   * 
   */
  private void saveZcHtChgBi(ZcPProBalChg zcPProBalChg) {
    List xmcgHtList = zcPProBalChg.getXmcgHtList();
    if (xmcgHtList != null && xmcgHtList.size() > 0) {
      //先删，后插入
      baseDao.delete("ZC_BAL_CHG_HT_BI.deleteByChgCode", zcPProBalChg.getBalChgId());
      for (int i = 0; i < xmcgHtList.size(); i++) {
        ZcXmcgHt zcXmcgHt = (ZcXmcgHt) xmcgHtList.get(i);
        List biList = zcXmcgHt.getBiList();
        for (int j = 0; j < biList.size(); j++) {
          ZcXmcgHtBi bi = (ZcXmcgHtBi) biList.get(j);
          if (bi.getZcBiBcsySum() == null || bi.getZcBiBcsySum().longValue() == 0L) {
            continue;
          }
          Map map = new HashMap();
          map.put("bi", bi);
          map.put("balChgId", zcPProBalChg.getBalChgId());
          baseDao.insert("ZC_BAL_CHG_HT_BI.ibatorgenerated_insert", map);
        }
      }
    }

  }

  /**
   * 调节采购合同中的资金构成情况
   */
  private void updateZcXmchHtBi(ZcPProBalChg zcPProBalChg) {
    List xmcgHtList = zcPProBalChg.getXmcgHtList();
    if (xmcgHtList != null && xmcgHtList.size() > 0) {
      for (int i = 0; i < xmcgHtList.size(); i++) {
        ZcXmcgHt zcXmcgHt = (ZcXmcgHt) xmcgHtList.get(i);
        //先删，后插入
        baseDao.delete("ZC_XMCG_HT_BI.deleteByHtCode", zcXmcgHt.getZcHtCode());
        List biList = zcXmcgHt.getBiList();
        for (int j = 0; j < biList.size(); j++) {
          ZcXmcgHtBi bi = (ZcXmcgHtBi) biList.get(j);
          if (bi.getZcBiBcsySum() == null || bi.getZcBiBcsySum().longValue() == 0L) {
            continue;
          }
          baseDao.insert("ZC_XMCG_HT_BI.ibatorgenerated_insert", bi);
        }
      }
    }

  }

  public ZcPProBalChg auditFN(ZcPProBalChg cur, RequestMeta requestMeta) throws BusinessException {
    //
    String zcUseBiId = "" + System.currentTimeMillis();

    //-------------------
    //更新状态
    cur.setStatus(ZcPProBalChg.VS_STATUS_COMPLETED);
    baseDao.update("ZC_P_PRO_BAL_CHG.abatorgenerated_updateByPrimaryKeySelective", cur);
    //删除采购计划的资金
    baseDao.delete("ZC_P_PRO_MITEM_BI.deleteByMakeCode", cur.getZcMakeCode());
    //删除合同的资金
    if (cur.getXmcgHtList() != null && cur.getXmcgHtList().size() > 0) {
      for (int i = 0; i < cur.getXmcgHtList().size(); i++) {
        ZcXmcgHt ht = (ZcXmcgHt) cur.getXmcgHtList().get(i);
        baseDao.delete("ZC_XMCG_HT_BI.deleteByHtCode", ht.getZcHtCode());
      }
    }
    //插入采购计划的资金
    BigDecimal zero = new BigDecimal(0);
    HashMap zcUseBiIdMap = new HashMap();
    List budgetSaveLst = new ArrayList();
    List biList = cur.getZcPProChgBiList();
    if (biList != null) {
      for (int i = 0; i < biList.size(); i++) {
        ZcPProMitemBiChg chg = (ZcPProMitemBiChg) biList.get(i);
        if (zero.compareTo(chg.getZcBiJhuaSum()) == 0) {
          continue;
        }
        if ((chg.getZcBiNo() != null && chg.getZcBiNo().trim().length() > 0)
          && (chg.getZcUseBiId() == null || chg.getZcUseBiId().trim().length() == 0)) {
          chg.setZcUseBiId(zcUseBiId);
          zcUseBiIdMap.put(chg.getZcBiNo(), zcUseBiId);
          zcUseBiId = "" + System.currentTimeMillis();
        }
        if (chg.getZcMakeCode() == null && chg.getZcBiNo() != null) {//新增的指标，需要从指标库中核减指标,在原设计时，将makecode没有设置，代表其是新增的指标
          
          chg.setZcMakeCode(cur.getZcMakeCode());

          Map bimp = new HashMap();
          bimp.put(BudgetUtil.VOU_MONEY, chg.getZcBiJhuaSum());
          bimp.put(BudgetUtil.VOU_ID, chg.getZcUseBiId());
          bimp.put(BudgetUtil.FROM_CTRL_ID, chg.getZcBiNo());
          budgetSaveLst.add(bimp);

        }
        baseDao.insert("ZC_P_PRO_MITEM_BI.ibatorgenerated_insert", chg);
      }
    }
    //插入合同的资金 
    if (cur.getHtBiLst() != null) {
      Enumeration keys = cur.getHtBiLst().keys();
      while (keys.hasMoreElements()) {
        List htNewBiLst = (List) cur.getHtBiLst().get(keys.nextElement());
        if (htNewBiLst != null) {
          for (int i = 0; i < htNewBiLst.size(); i++) {
            ZcXmcgHtBiChg chg = (ZcXmcgHtBiChg) htNewBiLst.get(i);
            if (zero.compareTo(chg.getZcBiBcsySum()) == 0) {
              continue;
            }
            chg.setZcMakeCode(cur.getZcMakeCode());
            if (zcUseBiIdMap.containsKey(chg.getZcBiNo())) {
              chg.setZcUseBiId((String) zcUseBiIdMap.get(chg.getZcBiNo()));
            }
            baseDao.insert("ZC_XMCG_HT_BI.ibatorgenerated_insert", chg);
          }
        }
      }
    }

    //调用指标接口，扣除指标  
    //-----------------
    BudgetUtil bu=new BudgetUtil();
    Map result = new HashMap(); 
    if (budgetSaveLst.size() > 0) {
      result.put(BudgetUtil.SAVE_BUDGET, bu.createData(budgetSaveLst));
    }
    try {
      bu.callService(result, requestMeta.getSvNd());
    } catch (ZcBudgetInterfaceException e) { 
      throw new BusinessException("调用指标系统接口异常\n"+e.getMessage(),e);
    }
    return selectByPrimaryKey(cur.getBalChgId(), requestMeta);
  }
}
