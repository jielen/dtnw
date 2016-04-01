package com.ufgov.zc.server.zc.dao.ibatis;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.ufgov.zc.common.zc.model.ZcDingdianBi;
import com.ufgov.zc.server.zc.dao.ZcDingdianBiMapper;

public class ZcDingdianBiMapperImp extends SqlMapClientDaoSupport implements ZcDingdianBiMapper {
 
  public int insert(ZcDingdianBi record) {
    getSqlMapClientTemplate().insert("ZcDingdianBiMapper.insert",record);
    return 1;
  }
 
  public int insertList(final List biLst) {
    if (biLst == null || biLst.size()==0)
      return 0;

    this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
      public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
        executor.startBatch();
        for (int i = 0; i < biLst.size(); i++) {
          ZcDingdianBi p = (ZcDingdianBi)biLst.get(i);
          executor.insert("ZcDingdianBiMapper.insert", p);
        }
        executor.executeBatch();
        return null;
      }
    });
    return biLst.size();
  }
 
  public List getBiLst(String ddCode) {
    return getSqlMapClientTemplate().queryForList("ZcDingdianBiMapper.getBiLstByDdCode", ddCode);
  }
 
  public void deleteBiByDdCode(String code) {
    getSqlMapClientTemplate().delete("ZcDingdianBiMapper.deleteBiByDdCode", code);
  }

}
