package com.ufgov.zc.server.zc.dao.ibatis;import java.math.BigDecimal;import java.util.List;import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchangeExample;import com.ufgov.zc.server.zc.dao.IZcDelegJczwDataExchangeDAO;public class ZcDelegJczwDataExchangeDAOImpl extends SqlMapClientDaoSupport implements IZcDelegJczwDataExchangeDAO {  /**   * This method was generated by Abator for iBATIS.   * This method corresponds to the database table ZC_DELEG_JCZW_DATA_EXCHANGE   *   * @abatorgenerated Thu Oct 27 18:07:40 CST 2011   */  public ZcDelegJczwDataExchangeDAOImpl() {    super();  }  /**   * This method was generated by Abator for iBATIS.   * This method corresponds to the database table ZC_DELEG_JCZW_DATA_EXCHANGE   *   * @abatorgenerated Thu Oct 27 18:07:40 CST 2011   */  public void insert(ZcDelegJczwDataExchange record) {    getSqlMapClientTemplate().insert("ZC_DELEG_JCZW_DATA_EXCHANGE.abatorgenerated_insert", record);  }  /**   * This method was generated by Abator for iBATIS.   * This method corresponds to the database table ZC_DELEG_JCZW_DATA_EXCHANGE   *   * @abatorgenerated Thu Oct 27 18:07:40 CST 2011   */  public int updateByPrimaryKey(ZcDelegJczwDataExchange record) {    int rows = getSqlMapClientTemplate().update("ZC_DELEG_JCZW_DATA_EXCHANGE.abatorgenerated_updateByPrimaryKey", record);    return rows;  }  /**   * This method was generated by Abator for iBATIS.   * This method corresponds to the database table ZC_DELEG_JCZW_DATA_EXCHANGE   *   * @abatorgenerated Thu Oct 27 18:07:40 CST 2011   */  public int updateByPrimaryKeySelective(ZcDelegJczwDataExchange record) {    int rows = getSqlMapClientTemplate().update("ZC_DELEG_JCZW_DATA_EXCHANGE.abatorgenerated_updateByPrimaryKeySelective", record);    return rows;  }  /**   * This method was generated by Abator for iBATIS.   * This method corresponds to the database table ZC_DELEG_JCZW_DATA_EXCHANGE   *   * @abatorgenerated Thu Oct 27 18:07:40 CST 2011   */  public List selectByExample(ZcDelegJczwDataExchangeExample example) {    List list = getSqlMapClientTemplate().queryForList("ZC_DELEG_JCZW_DATA_EXCHANGE.abatorgenerated_selectByExample", example);    return list;  }  /**   * This method was generated by Abator for iBATIS.   * This method corresponds to the database table ZC_DELEG_JCZW_DATA_EXCHANGE   *   * @abatorgenerated Thu Oct 27 18:07:40 CST 2011   */  public ZcDelegJczwDataExchange selectByPrimaryKey(BigDecimal exchangeId) {    ZcDelegJczwDataExchange key = new ZcDelegJczwDataExchange();    key.setExchangeId(exchangeId);    ZcDelegJczwDataExchange record = (ZcDelegJczwDataExchange) getSqlMapClientTemplate().queryForObject(    "ZC_DELEG_JCZW_DATA_EXCHANGE.abatorgenerated_selectByPrimaryKey", key);    return record;  }  /**   * This method was generated by Abator for iBATIS.   * This method corresponds to the database table ZC_DELEG_JCZW_DATA_EXCHANGE   *   * @abatorgenerated Thu Oct 27 18:07:40 CST 2011   */  public int deleteByExample(ZcDelegJczwDataExchangeExample example) {    int rows = getSqlMapClientTemplate().delete("ZC_DELEG_JCZW_DATA_EXCHANGE.abatorgenerated_deleteByExample", example);    return rows;  }  /**   * This method was generated by Abator for iBATIS.   * This method corresponds to the database table ZC_DELEG_JCZW_DATA_EXCHANGE   *   * @abatorgenerated Thu Oct 27 18:07:40 CST 2011   */  public int deleteByPrimaryKey(BigDecimal exchangeId) {    ZcDelegJczwDataExchange key = new ZcDelegJczwDataExchange();    key.setExchangeId(exchangeId);    int rows = getSqlMapClientTemplate().delete("ZC_DELEG_JCZW_DATA_EXCHANGE.abatorgenerated_deleteByPrimaryKey", key);    return rows;  }  /**   * This method was generated by Abator for iBATIS.   * This method corresponds to the database table ZC_DELEG_JCZW_DATA_EXCHANGE   *   * @abatorgenerated Thu Oct 27 18:07:40 CST 2011   */  public int countByExample(ZcDelegJczwDataExchangeExample example) {    Integer count = (Integer) getSqlMapClientTemplate().queryForObject("ZC_DELEG_JCZW_DATA_EXCHANGE.abatorgenerated_countByExample", example);    return count.intValue();  }  /**   * This method was generated by Abator for iBATIS.   * This method corresponds to the database table ZC_DELEG_JCZW_DATA_EXCHANGE   *   * @abatorgenerated Thu Oct 27 18:07:40 CST 2011   */  public int updateByExampleSelective(ZcDelegJczwDataExchange record, ZcDelegJczwDataExchangeExample example) {    UpdateByExampleParms parms = new UpdateByExampleParms(record, example);    int rows = getSqlMapClientTemplate().update("ZC_DELEG_JCZW_DATA_EXCHANGE.abatorgenerated_updateByExampleSelective", parms);    return rows;  }  /**   * This method was generated by Abator for iBATIS.   * This method corresponds to the database table ZC_DELEG_JCZW_DATA_EXCHANGE   *   * @abatorgenerated Thu Oct 27 18:07:40 CST 2011   */  public int updateByExample(ZcDelegJczwDataExchange record, ZcDelegJczwDataExchangeExample example) {    UpdateByExampleParms parms = new UpdateByExampleParms(record, example);    int rows = getSqlMapClientTemplate().update("ZC_DELEG_JCZW_DATA_EXCHANGE.abatorgenerated_updateByExample", parms);    return rows;  }  /**   * This class was generated by Abator for iBATIS.   * This class corresponds to the database table ZC_DELEG_JCZW_DATA_EXCHANGE   *   * @abatorgenerated Thu Oct 27 18:07:40 CST 2011   */  private static class UpdateByExampleParms extends ZcDelegJczwDataExchangeExample {    private Object record;    public UpdateByExampleParms(Object record, ZcDelegJczwDataExchangeExample example) {      super(example);      this.record = record;    }    public Object getRecord() {      return record;    }  }  public List getDljgWillExpDataList(ElementConditionDto dto) {    // TCJLODO Auto-generated method stub    List list = getSqlMapClientTemplate().queryForList("ZC_DELEG_JCZW_DATA_EXCHANGE.get_dljg_will_exp_data", dto);    return list;  }  public List getJczwWillExpDataList(ElementConditionDto dto) {    // TCJLODO Auto-generated method stub    List list = getSqlMapClientTemplate().queryForList("ZC_DELEG_JCZW_DATA_EXCHANGE.get_jczw_will_exp_data", dto);    return list;  }}