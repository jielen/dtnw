package com.ufgov.zc.server.zc.dao.ibatis;import java.util.List;import java.util.Map;import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.model.ZcEbBulletinWordMold;import com.ufgov.zc.server.zc.dao.IZcEbBulletinWordMoldDao;public class ZcEbBulletinWordMoldDao extends SqlMapClientDaoSupport implementsIZcEbBulletinWordMoldDao {  public List getZcEbBulletinWordMold(ElementConditionDto dto) {    return this.getSqlMapClientTemplate().queryForList("ZcEbBulletinWordMold.getZcEbBulletinWordMold",    dto);  }  public int updateSelectBulletinWordMold(ZcEbBulletinWordMold bean) {    int rows = this.getSqlMapClientTemplate().update("ZcEbBulletinWordMold.updateByPrimaryKeySelective", bean);    return rows;  }  public int delete(ZcEbBulletinWordMold bean) {    int row = this.getSqlMapClientTemplate().delete("ZcEbBulletinWordMold.deleteByPrimaryKey", bean);    return row;  }  public ZcEbBulletinWordMold insert(ZcEbBulletinWordMold bean) {    return (ZcEbBulletinWordMold) this.getSqlMapClientTemplate().insert("ZcEbBulletinWordMold.insert", bean);  }  public ZcEbBulletinWordMold selectMoldByPrimaryKey(String wordMoldID) {    ZcEbBulletinWordMold zcEbBulletinWordMold = (ZcEbBulletinWordMold)    this.getSqlMapClientTemplate().queryForObject("ZcEbBulletinWordMold.selectMoldByPrimaryKey", wordMoldID);    return zcEbBulletinWordMold;  }  public List getZcEbDataViewCol(String dataView) {    List ls = this.getSqlMapClientTemplate().queryForList("ZcEbBulletinWordMold.getZcEbDataViewCol", dataView);    System.out.println(ls.size());    return ls;  }  public List getZcEbBulletinWordMoldParam(String moldID) {    return this.getSqlMapClientTemplate().queryForList("ZcEbBulletinWordMold.getZcEbBulletinWordMoldParam", moldID);  }  public Map getZcEbBulletinWordMoldParamValue(Map paramMap) {    return (Map) this.getSqlMapClientTemplate().queryForObject    ("ZcEbBulletinWordMold.getZcEbBulletinWordMoldParamValue",    paramMap);  }  public List getZcEbBulletinMoldTableParamValue(Map paramMap) {    return this.getSqlMapClientTemplate().queryForList("ZcEbBulletinWordMold.getZcEbBulletinMoldTableParamValue",    paramMap);  }}