package com.ufgov.zc.server.zc.dao.ibatis;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.ufgov.zc.common.zc.model.ZcDingdianItem;
import com.ufgov.zc.server.zc.dao.ZcDingdianItemMapper;

public class ZcDingdianItemMapperImp extends SqlMapClientDaoSupport implements ZcDingdianItemMapper {

  
  public int insert(ZcDingdianItem record) {
    getSqlMapClientTemplate().insert("ZcDingdianItemMapper.insert",record);
    return 1;
  }

  
  public int insertList(final List itemlst) {
    if (itemlst == null || itemlst.size()==0)
      return 0;

    this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
      public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
        executor.startBatch();
        for (int i = 0; i < itemlst.size(); i++) {
          ZcDingdianItem p = (ZcDingdianItem)itemlst.get(i);
          executor.insert("ZcDingdianItemMapper.insert", p);
        }
        executor.executeBatch();
        return null;
      }
    });
    return itemlst.size();
  }

  
  public void deleteItemByDdCode(String code) {
    getSqlMapClientTemplate().delete("ZcDingdianItemMapper.deleteItemByDdCode", code);
  }

  
  public List getItemLst(String code) {
    return getSqlMapClientTemplate().queryForList("ZcDingdianItemMapper.getItemLstByDdCode", code);
  }

}
