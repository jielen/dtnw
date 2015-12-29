/** * ZcEbXunJiaBaoJiaDao.java * com.ufgov.gk.server.zc.dao.ibatis * Administrator * 2010-10-24 */package com.ufgov.zc.server.zc.dao.ibatis;import java.sql.SQLException;import java.util.Date;import java.util.HashMap;import java.util.List;import java.util.Map;import org.springframework.orm.ibatis.SqlMapClientCallback;import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;import com.ibatis.sqlmap.client.SqlMapExecutor;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.constants.NumLimConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.model.ZcEbSignupPackDetail;import com.ufgov.zc.common.zc.model.ZcEbXunJiaBaoJia;import com.ufgov.zc.common.zc.model.ZcEbXunJiaBaoJiaDetail;import com.ufgov.zc.server.system.util.NumLimUtil;import com.ufgov.zc.server.zc.dao.IZcEbXjbjDao;/** * @author Administrator * */public class ZcEbXjbjDao extends SqlMapClientDaoSupport implements IZcEbXjbjDao {  public List getSignupPack(ZcEbXunJiaBaoJia xunJiaBaoJia) {    // TCJLODO Auto-generated method stub getXunJiaProjAllPacks    //    return this.getSqlMapClientTemplate().queryForList("ZC_EB_XUNJIA_BAOJIA.getSignupPack", xunJiaBaoJia);    return this.getSqlMapClientTemplate().queryForList("ZC_EB_XUNJIA_BAOJIA.getXunJiaProjAllPacks", xunJiaBaoJia);  }  public List getSignupPackNew(ZcEbXunJiaBaoJia xunJiaBaoJia) {    // TCJLODO Auto-generated method stub getXunJiaProjAllPacks    //    return this.getSqlMapClientTemplate().queryForList("ZC_EB_XUNJIA_BAOJIA.getSignupPack", xunJiaBaoJia);    return this.getSqlMapClientTemplate().queryForList("ZC_EB_XUNJIA_BAOJIA.getSignupPack", xunJiaBaoJia);  }  public List getXunJiaBaoJia(ElementConditionDto dto, RequestMeta meta) {    // TCJLODO Auto-generated method stub    dto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(dto.getWfcompoId(), NumLimConstants.FWATCH));    return this.getSqlMapClientTemplate().queryForList("ZC_EB_XUNJIA_BAOJIA.getXunJiaBaoJia", dto);  }  public ZcEbXunJiaBaoJia getXunJiaBaoJiaBySupplier(ZcEbXunJiaBaoJia curObj) {    return (ZcEbXunJiaBaoJia) this.getSqlMapClientTemplate().queryForObject("ZC_EB_XUNJIA_BAOJIA.getXunJiaBaoJiaBySupplier", curObj);  }  public boolean deleteXunJiaBaoJia(ZcEbXunJiaBaoJia xunJiaBaoJia) {    this.getSqlMapClientTemplate().update("ZC_EB_XUNJIA_BAOJIA.deleteXunJiaBaoJiaByPrimaryKey", xunJiaBaoJia.getBjCode());    this.getSqlMapClientTemplate().delete("ZC_EB_XUNJIA_BAOJIA.deleteXunJiaBaoJiaDetail", xunJiaBaoJia.getBjCode());    return true;  }  public void updateXunJiaBaoJia(final ZcEbXunJiaBaoJia xunJiaBaoJia) {    this.getSqlMapClientTemplate().update("ZC_EB_XUNJIA_BAOJIA.updateXunJiaBaoJia", xunJiaBaoJia);    this.getSqlMapClientTemplate().delete("ZC_EB_XUNJIA_BAOJIA.deleteXunJiaBaoJiaDetailToDo", xunJiaBaoJia.getBjCode());    this.getSqlMapClientTemplate().delete("ZC_EB_XUNJIA_BAOJIA.deleteBaojiaPackTodo", xunJiaBaoJia.getBjCode());    this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {      public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {        executor.startBatch();        Map map = new HashMap();        map.put("bjCode", xunJiaBaoJia.getBjCode());         for (int i = 0; i < xunJiaBaoJia.getSignupPackList().size(); i++) {          ZcEbSignupPackDetail pack = (ZcEbSignupPackDetail) xunJiaBaoJia.getSignupPackList().get(i);           /*if (now.after(pack.getSubmitBidDocDate())) {            continue;          }*/          map.put("pack", pack);          executor.insert("ZC_EB_XUNJIA_BAOJIA.insertBaojiaPack", map);          for (int j = 0; j < pack.getXunJiaBaoJiaList().size(); j++) {            ZcEbXunJiaBaoJiaDetail d = (ZcEbXunJiaBaoJiaDetail) pack.getXunJiaBaoJiaList().get(j);            d.setBjCode(xunJiaBaoJia.getBjCode());            executor.insert("ZC_EB_XUNJIA_BAOJIA.insertXunJiaBaoJiaDetail", d);          }        }        executor.executeBatch();        return null;      }    });    updateSignupPack(xunJiaBaoJia);  }  private void updateSignupPack(final ZcEbXunJiaBaoJia xunJiaBaoJia) {    this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {      public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {        executor.startBatch();        for (int i = 0; i < xunJiaBaoJia.getSignupPackList().size(); i++) {          ZcEbSignupPackDetail pack = (ZcEbSignupPackDetail) xunJiaBaoJia.getSignupPackList().get(i);          executor.update("ZC_EB_XUNJIA_BAOJIA.updateSignupPack", pack);          ElementConditionDto dto = new ElementConditionDto();          dto.setZcText0(pack.getIsSubmitBidDoc());          dto.setZcText1(pack.getSignupId());          executor.update("ZC_EB_XUNJIA_BAOJIA.updateSignup", dto);        }        executor.executeBatch();        return null;      }    });  }  public void insertXunJiaBaoJia(final ZcEbXunJiaBaoJia xunJiaBaoJia) {    this.getSqlMapClientTemplate().insert("ZC_EB_XUNJIA_BAOJIA.insertXunJiaBaoJia", xunJiaBaoJia);    //insert the detail data    this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {      public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {        executor.startBatch();         Map map = new HashMap();        map.put("bjCode", xunJiaBaoJia.getBjCode());        for (int i = 0; i < xunJiaBaoJia.getSignupPackList().size(); i++) {          ZcEbSignupPackDetail pack = (ZcEbSignupPackDetail) xunJiaBaoJia.getSignupPackList().get(i);           map.put("pack", pack);          executor.insert("ZC_EB_XUNJIA_BAOJIA.insertBaojiaPack", map);          for (int j = 0; j < pack.getXunJiaBaoJiaList().size(); j++) {            ZcEbXunJiaBaoJiaDetail d = (ZcEbXunJiaBaoJiaDetail) pack.getXunJiaBaoJiaList().get(j);            d.setBjCode(xunJiaBaoJia.getBjCode());            executor.insert("ZC_EB_XUNJIA_BAOJIA.insertXunJiaBaoJiaDetail", d);          }        }        executor.executeBatch();        return null;      }    });    updateSignupPack(xunJiaBaoJia);  }  public List getBaoJiaDetail(ElementConditionDto dto) {    // TCJLODO Auto-generated method stub    return this.getSqlMapClientTemplate().queryForList("ZC_EB_XUNJIA_BAOJIA.getBaoJiaDetail", dto);  }  public List getXunJiaByProjSupplier(ElementConditionDto dto) {    // TCJLODO Auto-generated method stub    return this.getSqlMapClientTemplate().queryForList("ZC_EB_XUNJIA_BAOJIA.getXunJiaByProjSupplier", dto);  }  public List getXunJiaByProj(ElementConditionDto dto) {    // TCJLODO Auto-generated method stub    return this.getSqlMapClientTemplate().queryForList("ZC_EB_XUNJIA_BAOJIA.getXunJiaByProj", dto);  }  public List getXunJiaBaoJiaByCondition(ElementConditionDto dto) {    // TCJLODO Auto-generated method stub    return this.getSqlMapClientTemplate().queryForList("ZC_EB_XUNJIA_BAOJIA.getXunJiaBaoJiaByCondition", dto);  }    public void insertXunJiaBaoJiaM(ZcEbXunJiaBaoJia bill) {    // TCJLODO Auto-generated method stub    this.getSqlMapClientTemplate().insert("ZC_EB_XUNJIA_BAOJIA.insertXunJiaBaoJia", bill);  }}