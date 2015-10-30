/**
 * 
 */
package com.ufgov.zc.server.zc.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ufgov.zc.common.system.constants.NumLimConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcFaCard;
import com.ufgov.zc.common.zc.model.ZcFaCardExample;
import com.ufgov.zc.common.zc.model.ZcebZb;
import com.ufgov.zc.server.system.util.NumLimUtil;
import com.ufgov.zc.server.zc.dao.IZcFaCardDao;

/**
 * @author Administrator
 *
 */
public class ZcFaCardDao extends SqlMapClientDaoSupport implements IZcFaCardDao {

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.dao.IZcFaCardDao#countByExample(com.ufgov.zc.common.zc.model.ZcFaCardExample)
   */
  
  public int countByExample(ZcFaCardExample example) {
    // TODO Auto-generated method stub
    return 0;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.dao.IZcFaCardDao#deleteByExample(com.ufgov.zc.common.zc.model.ZcFaCardExample)
   */
  
  public int deleteByExample(ZcFaCardExample example) {
    // TODO Auto-generated method stub
    return 0;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.dao.IZcFaCardDao#insert(com.ufgov.zc.common.zc.model.ZcFaCard)
   */
  
  public int insert(ZcFaCard record) {
    // TODO Auto-generated method stub
//    System.out.println("dao.insert  cocode"+record.getCoCode()+record.getCoName());
    this.getSqlMapClientTemplate().insert("ZcFaCard.insert", record);
   return 1; 
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.dao.IZcFaCardDao#insertSelective(com.ufgov.zc.common.zc.model.ZcFaCard)
   */
  
  public int insertSelective(ZcFaCard record) {
    // TODO Auto-generated method stub
    return 0;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.dao.IZcFaCardDao#selectByExample(com.ufgov.zc.common.zc.model.ZcFaCardExample)
   */
  
  public List selectByExample(ZcFaCardExample example) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.dao.IZcFaCardDao#updateByExampleSelective(com.ufgov.zc.common.zc.model.ZcFaCard, com.ufgov.zc.common.zc.model.ZcFaCardExample)
   */
  
  public int updateByExampleSelective(ZcFaCard record, ZcFaCardExample example) {
    // TODO Auto-generated method stub
    return 0;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.server.zc.dao.IZcFaCardDao#updateByExample(com.ufgov.zc.common.zc.model.ZcFaCard, com.ufgov.zc.common.zc.model.ZcFaCardExample)
   */
  
  public int updateByExample(ZcFaCard record, ZcFaCardExample example) {
    // TODO Auto-generated method stub
    return 0;
  }

  
  public List getCardLst(ElementConditionDto dto) {
    // TODO Auto-generated method stub
    dto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(dto.getWfcompoId(), NumLimConstants.FWATCH));
    List list = this.getSqlMapClientTemplate().queryForList("ZcFaCard.getCardLst", dto);
    return list;
  }

  
  public ZcFaCard selectByCardId(String cardId) {
    // TODO Auto-generated method stub
    return (ZcFaCard) this.getSqlMapClientTemplate().queryForObject("ZcFaCard.selectByCardId", cardId);
  }

  
  public void update(ZcFaCard card) {
    // TODO Auto-generated method stub
    this.getSqlMapClientTemplate().update("ZcFaCard.updateByExample", card);
  }

   
  public void deleteByCardIdFN(String cardId) {
    // TODO Auto-generated method stub
    getSqlMapClientTemplate().delete("ZcFaCard.deleteByCardId", cardId);   
  }

}
