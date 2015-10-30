package com.ufgov.zc.server.zc.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.zc.model.EmExpertAbility;
import com.ufgov.zc.common.zc.model.EmExpertAbilityHistory;
import com.ufgov.zc.server.zc.dao.ibatis.BaseDao;
import com.ufgov.zc.server.zc.service.IZcEmExpertAbilityService;

public class ZcEmExpertAbilityService implements IZcEmExpertAbilityService {

  private BaseDao baseDao;
   
  public BaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }

 
  public EmExpertAbility selectByPrimaryKey(String primKey, RequestMeta requestMeta) throws SQLException {
    
    EmExpertAbility bill = (EmExpertAbility) baseDao.read("EmExpertAbilityHistory.selectByPrimaryKey", primKey);
    
    List abilityList = baseDao.query("EmExpertAbilityHistory.getExpertAbilityHistoryByPrimKey", primKey);

    List itemList = baseDao.query("EmExpertEvaluation.listByEmExpertCode", primKey);
    
    bill.setItemList(itemList);
    
    bill.setAbilityList(abilityList);
    
    bill.setDbDigest(bill.digest());

    return bill;
  }

 
  public EmExpertAbility updateByPrimaryKey(EmExpertAbility bean, RequestMeta meta) throws SQLException {
    
    List diseList = bean.getAbilityList();

    if (diseList == null || diseList.size() == 0)

       throw new RuntimeException("专家能力评审不能为空");

    String code = bean.getEmExpertCode();
      
    baseDao.delete("EmExpertAbilityHistory.deleteEmExpertAbilityHistory", code);
    
    for (int i = 0; i < diseList.size(); i++) {

      EmExpertAbilityHistory bi = (EmExpertAbilityHistory) diseList.get(i);

      bi.setEmExpertCode(bean.getEmExpertCode());

      baseDao.insert("EmExpertAbilityHistory.addExpertAbilityHistory", bi);

    }

    return this.selectByPrimaryKey(code,meta);
  }

}
