package com.ufgov.zc.server.zc.dao.ibatis;import java.sql.SQLException;import java.util.List;import org.springframework.orm.ibatis.SqlMapClientCallback;import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;import com.ibatis.sqlmap.client.SqlMapExecutor;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.constants.NumLimConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.model.ZcEbPack;import com.ufgov.zc.common.zc.model.ZcEbPackReq;import com.ufgov.zc.common.zc.model.ZcEbProjChange;import com.ufgov.zc.server.system.util.NumLimUtil;import com.ufgov.zc.server.zc.dao.IZcEbProjChangeDao;public class ZcEbProjChangeDao extends SqlMapClientDaoSupport implements IZcEbProjChangeDao {  public List getZcEbProj(ElementConditionDto dto, RequestMeta meta) {    dto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(dto.getWfcompoId(), NumLimConstants.FWATCH));    return this.getSqlMapClientTemplate().queryForList("ZcEbProjChange.getZcEbProj", dto);  }  public List getZcEbPackListByProjCode(String projCode) {    return this.getSqlMapClientTemplate().queryForList("ZcEbProjChange.getZcEbPackListByProjCode", projCode);  }  public List getZcEbPackReqListByProjCode(String projCode) {    return this.getSqlMapClientTemplate().queryForList("ZcEbProjChange.getZcEbPackReqListByProjCode", projCode);  }  public void deleteZcEbPackByProjCode(String projCode) {    this.getSqlMapClientTemplate().delete("ZcEbProjChange.deleteZcEbPackByProjCode", projCode);  }  public void updateInvalid(long id) {    this.getSqlMapClientTemplate().update("ZcEbProjChange.updateInvalid", String.valueOf(id));  }  public void deleteZcEbPackReqByProjCode(String projCode) {    this.getSqlMapClientTemplate().delete("ZcEbProjChange.deleteZcEbPackReqByProjCode", projCode);  }  public void deleteCurrentChange(long id) {    this.getSqlMapClientTemplate().delete("ZcEbProjChange.deleteCurrentChange", String.valueOf(id));  }  public ZcEbProjChange getOriginZcEbProjByProjId(String projCode) {    return (ZcEbProjChange) this.getSqlMapClientTemplate().queryForObject("ZcEbProjChange.getOriginalZcEbProjByProjCode", projCode);  }  public void insertZcEbPackInfo(final ZcEbProjChange proj) {    this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {      public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {        executor.startBatch();        List packList = proj.getPackList();        ZcEbPack pack;        for (int i = 0; i < packList.size(); i++) {          pack = (ZcEbPack) packList.get(i);          pack.setProjCode(proj.getProjCode());          // 避免已有分包编号的数据从新取SEQ          if (pack.getPackCode() == null || "".endsWith(pack.getPackCode())) {            executor.insert("ZcEbProjChange.insertZcEbPackBySeq", pack);          } else {            executor.insert("ZcEbProjChange.insertZcEbPack", pack);          }          List packReqList = pack.getRequirementDetailList();          for (int j = 0; j < packReqList.size(); j++) {            ZcEbPackReq packReq = (ZcEbPackReq) packReqList.get(j);            packReq.setProjCode(proj.getProjCode());            packReq.setPackCode(pack.getPackCode());            if (packReq.getPackReqCode() == null) {              executor.insert("ZcEbProjChange.insertZcEbPackReqBySeq", packReq);            } else {              executor.insert("ZcEbProjChange.insertZcEbPackReq", packReq);            }          }        }        executor.executeBatch();        return null;      }    });  }  public void insertZcEbProj(ZcEbProjChange proj) {    this.getSqlMapClientTemplate().insert("ZcEbProjChange.insertZcEbProj", proj);  }  public List getIfHasChanged(ZcEbProjChange proj) {    return this.getSqlMapClientTemplate().queryForList("ZcEbProjChange.getIfHasChanged", proj);  }  public void updateZcEbProj(ZcEbProjChange proj) {    this.getSqlMapClientTemplate().update("ZcEbProjChange.updateZcEbProj", proj);  }  public void deleteZcEbProjByProjCode(String projCode) {    this.getSqlMapClientTemplate().delete("ZcEbProjChange.deleteZcEbProjByProjCode", projCode);  }  public ZcEbProjChange getOriginZcEbProjByProjSrcId(String projSrcCode) {    return (ZcEbProjChange) this.getSqlMapClientTemplate().queryForObject("ZcEbProjChange.getOriginalZcEbProjByProjSrcCode", projSrcCode);  }  public List getIfZcEntrustPack(ElementConditionDto dto) {    // TCJLODO Auto-generated method stub    return this.getSqlMapClientTemplate().queryForList("ZcEbProjChange.getIfZcEntrustPack", dto);  }  public long getSumChangeJe(String projSrcCode) {    Object obj = this.getSqlMapClientTemplate().queryForObject("ZcEbProjChange.getSumChangeJe", projSrcCode);    if (obj == null) {      return 0;    } else {      return ((Long) obj).longValue();      //return ((Long) this.getSqlMapClientTemplate().queryForObject("ZcEbProjChange.getSumChangeJe", projSrcCode)).longValue();    }    //    return (Long) this.getSqlMapClientTemplate().queryForObject("ZcEbProjChange.getAsExpByUserCode", projSrcCode);  }}