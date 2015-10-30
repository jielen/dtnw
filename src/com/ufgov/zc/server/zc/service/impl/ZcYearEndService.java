package com.ufgov.zc.server.zc.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.ARRAY;

import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.DataExchangeRedo;
import com.ufgov.zc.common.zc.model.ZcPProBal;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.model.ZcXmcgHtExample;
import com.ufgov.zc.common.zc.model.ZcYearPlan;
import com.ufgov.zc.server.budget.util.BudgetUtil;
import com.ufgov.zc.server.zc.ZcSUtil;
import com.ufgov.zc.server.zc.dao.IBaseDao;
import com.ufgov.zc.server.zc.dao.IDataExchangeDao;
import com.ufgov.zc.server.zc.service.IZcPProBalService;
import com.ufgov.zc.server.zc.service.IZcPProMakeService;
import com.ufgov.zc.server.zc.service.IZcXmcgHtService;
import com.ufgov.zc.server.zc.service.IZcYearEndService;
import com.ufgov.zc.server.zc.web.action.make.MakeShowAction;

public class ZcYearEndService implements IZcYearEndService {

	private IBaseDao baseDao;
	
	private IZcPProMakeService zcPProMakeService;
	
	private IZcXmcgHtService zcXmcgHtService;
	
	private IZcPProBalService zcPProBalService;
	
	private IDataExchangeDao dataExchangeDao;

	public IZcPProMakeService getZcPProMakeService() {
    return zcPProMakeService;
  }

  public void setZcPProMakeService(IZcPProMakeService zcPProMakeService) {
    this.zcPProMakeService = zcPProMakeService;
  }

  public IZcXmcgHtService getZcXmcgHtService() {
    return zcXmcgHtService;
  }

  public void setZcXmcgHtService(IZcXmcgHtService zcXmcgHtService) {
    this.zcXmcgHtService = zcXmcgHtService;
  }

  public IZcPProBalService getZcPProBalService() {
    return zcPProBalService;
  }

  public void setZcPProBalService(IZcPProBalService zcPProBalService) {
    this.zcPProBalService = zcPProBalService;
  }

  public IBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public List getZcPProMake(ElementConditionDto dto, RequestMeta meta)
			throws Exception {
		// TODO Auto-generated method stub
	  //获取计划列表
	  List makeLst=_getMakeList(dto,meta);
	  if(makeLst==null) return null;
	  List mkCodeLst=new ArrayList();
	  for(int i=0;i<makeLst.size();i++){
	    ZcPProMake make=(ZcPProMake)makeLst.get(i);
	    mkCodeLst.add(make.getZcMakeCode());
	  }
	  //获取合同列表
	  List htList=_getHtList(mkCodeLst,meta,dto);
	  //获取支付列表
	  List balMap=_getBalList(mkCodeLst,meta,dto);
	  //组装起来
		return _createYearEndList(makeLst,htList,balMap);
	}

	private List _createYearEndList(List makeLst, List htLst, List balLst) {
    // TODO Auto-generated method stub
	  List rtn= new ArrayList();
	  makeLst=makeLst==null?new ArrayList():makeLst;
	  htLst=htLst==null?new ArrayList():htLst;
	  balLst=balLst==null?new ArrayList():balLst;
	  for(int i=0;i<makeLst.size();i++){
	    ZcYearPlan yp=new ZcYearPlan();
	    ZcPProMake make=(ZcPProMake) makeLst.get(i);
	    yp.setMake(make);
	    List _htLst=new ArrayList();
      List _balLst=new ArrayList();
	    for(int j=0;j<htLst.size();j++){
	      ZcXmcgHt ht=(ZcXmcgHt) htLst.get(j);
	      if(make.getZcMakeCode().equals(ht.getZcMakeCode())){
	        yp.getHtList().add(ht);
	      }
	    }
	    
	    for(int k=0;k<balLst.size();k++){
	      ZcPProBal bal=(ZcPProBal) balLst.get(k);
	      if(make.getZcMakeCode().equals(bal.getZcMakeCode())){
	        yp.getBalList().add(bal);
	      }
	    }
	    rtn.add(yp);
	  }
    return rtn;
  }

  private List _getBalList(List mkCodeLst, RequestMeta meta,ElementConditionDto dtooo) {
    // TODO Auto-generated method stub
    ElementConditionDto dto =new ElementConditionDto();
    dto.setWfcompoId("ZC_P_PRO_BAL");
    dto.setExecutor(meta.getSvUserID());
    if(ZcYearPlan.END_TYPE_JIE_ZHUAN_ZHONG_NEW.equals(dtooo.getStatus())){//在结转后年度里，挂接资金时，环境变量里的年度已经是新年了，要显示这些结算数据，则要将年度-1
      dto.setNd(dtooo.getNd()-1);
    }else{
      dto.setNd(dtooo.getNd());
    }
    dto.setStatus("exec");
    dto.setCoCode(meta.getSvCoCode());
    dto.setPmAdjustCodeList(mkCodeLst);
    return getZcPProBalService().getZcPProBalList(dto, meta);
  }

  private List _getHtList(List mkCodeLst, RequestMeta meta,ElementConditionDto dtooo) throws SQLException {
    // TODO Auto-generated method stub
    ElementConditionDto dto =new ElementConditionDto();
    dto.setWfcompoId("ZC_XMCG_HT");
    dto.setExecutor(meta.getSvUserID());
    if(ZcYearPlan.END_TYPE_JIE_ZHUAN_ZHONG_NEW.equals(dtooo.getStatus())){//在结转后年度里，挂接资金时，环境变量里的年度已经是新年了，要显示这些合同，则要将年度-1
      dto.setNd(dtooo.getNd()-1);
    }else{
      dto.setNd(dtooo.getNd());
    }
    dto.setStatus("all");
    dto.setPmAdjustCodeList(mkCodeLst);
    //获取主合同
     List mHtLst=getZcXmcgHtService().getZcXmcgHtList(dto, meta);
    //获取补充合同
     dto.setZcText0("sup");
     List subHtLst=getZcXmcgHtService().getZcXmcgHtList(dto, meta);
     if(mHtLst==null){
       mHtLst=new ArrayList();
     }
     if(subHtLst!=null){
       mHtLst.addAll(subHtLst);
     }
    return mHtLst;
  }

  private List _getMakeList(ElementConditionDto dtooo,RequestMeta meta) throws SQLException {
    // TODO Auto-generated method stub
    ElementConditionDto dto=new ElementConditionDto();
    dto.setExecutor(meta.getSvUserID());
    if(ZcYearPlan.END_TYPE_JIE_ZHUAN_ZHONG_NEW.equals(dtooo.getStatus())//在结转后年度里，挂接资金时，环境变量里的年度已经是新年了，要显示这些计划，则要将年度-1
      ||ZcYearPlan.END_TYPE_YI_JIE_ZHUAN.equals(dtooo.getStatus())){//查看已经挂接资金的数据，环境变量里的年度已经是新年了，要显示这些计划，则要将年度-1
      dto.setNd(dtooo.getNd()-1);
    }else{
      dto.setNd(dtooo.getNd());
    }
    dto.setStatus("all");    
    dto.setWfcompoId("ZC_P_PRO_MAKE");
    dto.setZcText0(meta.getSvUserID());
    dto.setDattr1(dtooo.getStatus());
      return getZcPProMakeService().getZcPProMake(dto, meta);
  }

  public void updateProEndFn(String zcMakeCodes, boolean useBugdet, RequestMeta requestMeta) throws Exception {
		// TODO Auto-generated method stub
		// 如果使用了指标接口，则释放占用的指标
	  if(useBugdet){
	    BudgetUtil b=new BudgetUtil();
  		List updts = b.getProEndBudget(baseDao, useBugdet, zcMakeCodes,requestMeta);
  		if(updts!=null){
  		  for(int i=0;i<updts.size();i++){
  		    Map kk =(Map)updts.get(i);
  		    List l=new ArrayList();
  		    l.add(kk);
  		    Map m=new HashMap();
  		    m.put(BudgetUtil.UPD_BUDGET,b.createData(l));  		    
  		    b.callService(m, requestMeta.getSvNd());
  		  }
  		}
  	}
    // 更新计划表，yepflag变为10
    baseDao.update("ZC_YEAR_END.updateMakeYepFlag", zcMakeCodes);

	}

	public void updateYearEnd(String zcMakeCode, RequestMeta requestMeta)
			throws Exception {

	}

	/**
	 * 获取结转锁定的项目
	 */
	public List getZcCarraryMake(ElementConditionDto dto, RequestMeta meta)
			throws Exception {
		return baseDao.query("ZC_YEAR_END.selectCarraryMake", dto);
	}

	public void jiezhuanFN(ElementConditionDto dto,
			String serverAdd, RequestMeta meta) throws Exception {

		String flag = dto.getDattr1();
		if (ZcYearPlan.END_TYPE_JIE_ZHUAN_ZHONG.equals(flag)) {
			// 取得要释放的指标
      BudgetUtil b=new BudgetUtil();
			List updts = b.getJiezhuanFreeBudget(baseDao, true,dto.getZcMakeCode(),meta);
      if(updts!=null){
        for(int i=0;i<updts.size();i++){
          Map kk =(Map)updts.get(i);
          List l=new ArrayList();
          l.add(kk);
          Map m=new HashMap();
          m.put(BudgetUtil.UPD_BUDGET,b.createData(l));         
          b.callService(m, meta.getSvNd());
        }
      }
      baseDao.update("ZC_YEAR_END.updateMakeYepFlagByFlag", dto);
      //产生结转后的采购计划、采购合同数据，注意这里采购计划金额、合同金额都是未支付完成的指标金额
      baseDao.update("ZC_YEAR_END.carraryNewMake", dto);      
		}
	}


	/**
	 * 手动结转立项
	 */
	public void carraryNewMakeByManual(ZcYearPlan yearPlan, String serverAdd,
			RequestMeta meta) throws Exception {
/*		List makeBiList = yearPlan.getMakeBiList();
		if (null != makeBiList && makeBiList.size() > 0) {
			for (int i = 0; i < makeBiList.size(); i++) {
				ZcPProMitemBi bi = (ZcPProMitemBi) makeBiList.get(i);
				baseDao.insert("ZC_YEAR_END.saveMakeBi", bi);
			}
		}

		List htBiList = yearPlan.getHtBiList();
		if (null != htBiList && htBiList.size() > 0) {
			for (int i = 0; i < makeBiList.size(); i++) {
				ZcPProMitemBi bi = (ZcPProMitemBi) htBiList.get(i);
				baseDao.insert("ZC_YEAR_END.saveHtBi", bi);
			}
		}

		baseDao.update("ZC_YEAR_END.carrarySdNewMake", yearPlan);

		// 调用指标接口，冻结新指标中用到的金额
		Map map = new BudgetUtil().getProEndBudget(baseDao, true,	"'" + yearPlan.getZcMakeCode() + "_1'",meta);
		new BudgetUtil().callService(map, meta.getSvNd());*/
	}

  /**
   * 通过update指标形式，只占用合同所使用的指标，释放多余的指标
   */
	
  public void jiexiangFn(ZcYearPlan zcYearPlan, RequestMeta requestMeta) throws Exception {
    // TODO Auto-generated method stub
    //获取计划的资金信息
    List makeBiLst=getZcPProMakeService().getZcPProMitemBi(zcYearPlan.getMake().getZcMakeCode(),ZcSUtil.isUseBi());    

    //获取合同的资金信息
    ElementConditionDto dto=new ElementConditionDto();

    if(ZcSUtil.isUseBi() && !(makeBiLst==null || makeBiLst.size()==0)){
      dto.setZcText0(zcYearPlan.getMake().getZcMakeCode());
      List htBiLst=baseDao.query("ZC_XMCG_HT.getBiUsing", dto);
      
      List biDataLst=new ArrayList();
      for(int i=0;i<makeBiLst.size();i++){
        ZcPProMitemBi bi=(ZcPProMitemBi) makeBiLst.get(i);
        if(bi.getZcBiNo()!=null && !bi.getZcBiNo().startsWith("NoBi_") ){
          for(int k=0;k<htBiLst.size();k++){
            HashMap map=(HashMap) htBiLst.get(k);
            String biNo=(String) map.get("ZC_BI_NO");
            if(bi.getZcBiNo().equals(biNo)){
                Map mp = new HashMap();           
                mp.put(BudgetUtil.VOU_MONEY, map.get("HTBI")==null?new BigDecimal(0):map.get("HTBI"));
                mp.put(BudgetUtil.VOU_ID, bi.getZcUseBiId());
                mp.put(BudgetUtil.FROM_CTRL_ID, bi.getZcBiNo());
                biDataLst.add(mp);
                break;
            }
          }
        }
      }
      BudgetUtil bu=new BudgetUtil();
      Map result = new HashMap();
      result.put(BudgetUtil.UPD_BUDGET, bu.createData(biDataLst));
      bu.callService(result, requestMeta.getSvNd());
    }
    //更新采购计划的状态为已结项
    dto=new ElementConditionDto();
    dto.setZcText0(zcYearPlan.getMake().getZcMakeCode());
    dto.setZcText1(ZcYearPlan.END_TYPE_YI_JIE_XIANG);
    baseDao.update("ZC_YEAR_END.updateMakeYepFlagByMakeCode", zcYearPlan.getMake().getZcMakeCode());
  }

  
  public void batchJiexiangFn(List jieXiangLst, RequestMeta requestMeta) throws Exception {
    // TODO Auto-generated method stub
    if(jieXiangLst==null)return;
    for(int i=0;i<jieXiangLst.size();i++){
      ZcYearPlan p=(ZcYearPlan)jieXiangLst.get(i);
      jiexiangFn(p, requestMeta);
    }
  }

   
  public ZcYearPlan getZcYearPlanByMakeCode(RequestMeta requestMeta, ZcYearPlan yearPlan) throws Exception {
    // TODO Auto-generated method stub
    //获取结转前的采购计划
    yearPlan.setMake(getZcPProMakeService().selectByPrimaryKey(yearPlan.getZcMakeCode()));
    
    //获取结转后的采购计划
    ZcPProMake newMake=getZcPProMakeService().selectByPrimaryKey(yearPlan.getNewZcMakeCode());
    
    yearPlan.setNewMake(newMake==null?new ZcPProMake():newMake);
    
    /* ZcXmcgHtExample ex = new ZcXmcgHtExample();
     ex.createCriteria().andZcMakeCodeEqualTo(yearPlan.getZcMakeCode());
     List htLst = getZcXmcgHtService().getZcXmcgHt(ex);
     yearPlan.setHtList(new ArrayList());
     if (htLst == null) return yearPlan;
     for (int i = 0; i < htLst.size(); i++) {
       ZcXmcgHt ht = (ZcXmcgHt)htLst.get(i);
       ht = getZcXmcgHtService().selectByPrimaryKey(ht.getZcHtCode());
       yearPlan.getHtList().add(ht);
     }

     ex = new ZcXmcgHtExample();
     ex.createCriteria().andZcMakeCodeEqualTo(yearPlan.getNewZcMakeCode());
     htLst = getZcXmcgHtService().getZcXmcgHt(ex);
     yearPlan.setNewHtList(new ArrayList());
     if (htLst == null) return yearPlan;
     for (int i = 0; i < htLst.size(); i++) {
       ZcXmcgHt ht = (ZcXmcgHt)htLst.get(i);
       ht = getZcXmcgHtService().selectByPrimaryKey(ht.getZcHtCode());
       yearPlan.getNewHtList().add(ht);
     }*/
    
   //获取结转前的合同
    ZcXmcgHtExample ex=new ZcXmcgHtExample();
    ex.createCriteria().andZcMakeCodeEqualTo(yearPlan.getZcMakeCode());
    List htLst=getZcXmcgHtService().getZcXmcgHt(ex);
    List newHtLst;
    if(yearPlan.getNewZcMakeCode()!=null){
      ex=new ZcXmcgHtExample();
      ex.createCriteria().andZcMakeCodeEqualTo(yearPlan.getNewZcMakeCode());
     newHtLst=getZcXmcgHtService().getZcXmcgHt(ex);
    }else{
      newHtLst=new ArrayList();
    }
    
    
    yearPlan.setHtList(new ArrayList());
    if(htLst==null)return yearPlan;
    for(int i=0;i<htLst.size();i++){
      ZcXmcgHt ht=(ZcXmcgHt) htLst.get(i);

      ht=getZcXmcgHtService().selectByPrimaryKey(ht.getZcHtCode());
      yearPlan.getHtList().add(ht);
      
     /* for(int j=0;j<newHtLst.size();j++){
        ZcXmcgHt newht=(ZcXmcgHt) newHtLst.get(j);  
        String htcode=ht.getZcHtCode()+ZcYearPlan.JIEZHUAN_CODE_SUFFIX;
        if(htcode.equals(newht.getZcHtCode())){//这里这么处理的原因是，只有进行过结转的老合同，才获取其具体信息
          ht=getZcXmcgHtService().selectByPrimaryKey(ht.getZcHtCode());
          yearPlan.getHtList().add(ht);
          break;
        }
      }*/
    }
    
    //获取结转后的采购合同
    htLst=new ArrayList();
    if(yearPlan.getNewZcMakeCode()!=null){
      ex=new ZcXmcgHtExample();
      ex.createCriteria().andZcMakeCodeEqualTo(yearPlan.getNewZcMakeCode());
      htLst=getZcXmcgHtService().getZcXmcgHt(ex);
    }
    yearPlan.setNewHtList(new ArrayList());
    if(htLst==null)return yearPlan;
    for(int i=0;i<htLst.size();i++){
      ZcXmcgHt ht=(ZcXmcgHt) htLst.get(i);
      ht=getZcXmcgHtService().selectByPrimaryKey(ht.getZcHtCode());
      yearPlan.getNewHtList().add(ht);
    }
 
    //获取已经结束金额,原已经结算金额包含未结转合同金额的，这里将其剔除--20150226 chenjl
    
    
    
    return yearPlan;
  }
 
  public ZcYearPlan saveJieZhuanZiJinFN(ZcYearPlan yp, RequestMeta requestMeta) throws Exception {
    // TODO Auto-generated method stub
    zcPProMakeService.updateZcPProMake(yp.getNewMake(), null, ZcSUtil.isUseBi(), requestMeta);
    ZcPProMake make=zcPProMakeService.selectByPrimaryKey(yp.getNewMake().getZcMakeCode());
    
    if(yp.getNewHtList()!=null){
      for(int i=0;i<yp.getNewHtList().size();i++){
        ZcXmcgHt ht=(ZcXmcgHt) yp.getNewHtList().get(i);
        ht.setZcParhtCode(null);
        //将zcbiusedid同步到合同资金上
        for(int j=0;j<make.getBiList().size();j++){
          ZcPProMitemBi bi = (ZcPProMitemBi) make.getBiList().get(j);
          for(int k=0;k<ht.getBiList().size();k++){
            ZcXmcgHtBi htbi = (ZcXmcgHtBi) ht.getBiList().get(k);
            if(htbi.getZcBiNo()!=null && htbi.getZcBiNo().equals(bi.getZcBiNo())){
              htbi.setZcUseBiId(bi.getZcUseBiId());
              break;
            }
          }
        }
        zcXmcgHtService.updateZcXmcgHt(ht, false, null, requestMeta);
      }
    }
    ElementConditionDto dto=new ElementConditionDto();
    dto.setZcMakeCode(yp.getMake().getZcMakeCode());
    dto.setDattr1(ZcYearPlan.END_TYPE_YI_JIE_ZHUAN);
    baseDao.update("ZC_YEAR_END.updateMakeYepFlagByFlag", dto);
    
    //将结转后的计划数据、合同数据同步到外网
    _tongbu(yp,requestMeta);
    
    return yp;
  }

  private void _tongbu(ZcYearPlan yp, RequestMeta requestMeta) {
    // TODO Auto-generated method stub
    _tongbuMake(yp,requestMeta);
    _tongbuHt(yp,requestMeta);
  }

  private void _tongbuHt(ZcYearPlan yp, RequestMeta requestMeta) {
    // TODO Auto-generated method stub
    if(yp.getNewHtList()!=null){
      for(int i=0;i<yp.getNewHtList().size();i++){
        ZcXmcgHt ht=(ZcXmcgHt) yp.getNewHtList().get(i);
        DataExchangeRedo redo = new DataExchangeRedo();
        redo.setDataTypeID("ZC_XMCG_HT");
        redo.setDataTypeName("采购合同");
        ElementConditionDto dto = new ElementConditionDto();     
        redo.setRecordID(ht.getZcHtCode());
        redo.setRecordName(ht.getZcHtName());
        redo.setMasterTableName("ZC_XMCG_HT");
        redo.setIsExported(DataExchangeRedo.STATUS_WAITING_EXPORTED);
        redo.setGenerateDate(new Date());
        redo.setOperateType("faudit");        
        dataExchangeDao.deleteByRecordIDAndDataTypeID(redo);
        dataExchangeDao.saveRedo(redo);
      }
    }
  }

  private void _tongbuMake(ZcYearPlan yp, RequestMeta requestMeta) {
    // TODO Auto-generated method stub
    
    DataExchangeRedo redo = new DataExchangeRedo();

    redo.setDataTypeID("ZC_P_PRO_MAKE");

    // redo.setDataTypeName("电子竞价公告发布");

    redo.setDataTypeName("采购计划");

    ElementConditionDto dto = new ElementConditionDto();
   
    redo.setRecordID(yp.getNewMake().getZcMakeCode());

    redo.setRecordName(yp.getNewMake().getZcMakeName());

    redo.setMasterTableName("ZC_P_PRO_MAKE");

    redo.setIsExported(DataExchangeRedo.STATUS_WAITING_EXPORTED);

    redo.setGenerateDate(new Date());
    
    dataExchangeDao.deleteByRecordIDAndDataTypeID(redo);

    dataExchangeDao.saveRedo(redo);
  }

  public IDataExchangeDao getDataExchangeDao() {
    return dataExchangeDao;
  }

  public void setDataExchangeDao(IDataExchangeDao dataExchangeDao) {
    this.dataExchangeDao = dataExchangeDao;
  }

}
