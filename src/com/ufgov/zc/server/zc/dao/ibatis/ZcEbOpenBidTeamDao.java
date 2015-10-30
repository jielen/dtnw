package com.ufgov.zc.server.zc.dao.ibatis;import java.sql.SQLException;import java.util.List;import org.springframework.orm.ibatis.SqlMapClientCallback;import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;import com.ibatis.sqlmap.client.SqlMapExecutor;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.constants.NumLimConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.model.ZcEbOpenBidTeam;import com.ufgov.zc.common.zc.model.ZcEbOpenBidTeamMember;import com.ufgov.zc.server.system.util.NumLimUtil;import com.ufgov.zc.server.zc.dao.IZcEbOpenBidTeamDao;public class ZcEbOpenBidTeamDao extends SqlMapClientDaoSupport implements IZcEbOpenBidTeamDao {  public ZcEbOpenBidTeamDao() {    super();  }  public void insertZcEbOpenBidTeam(final ZcEbOpenBidTeam record) {    getSqlMapClientTemplate().insert("ZcEbOpenBidTeam.insertZcEbOpenBidTeam", record);    this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {      public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {        executor.startBatch();        for (int i = 0; i < record.getTeamMembers().size(); i++) {          executor.insert("ZcEbOpenBidTeam.insertZcEbOpenBidTeamMember", record.getTeamMembers().get(i));        }        for (int i = 0; i < record.getPacks().size(); i++) {          executor.insert("ZcEbOpenBidTeam.insertZcEbOpenBidTeamPack", record.getPacks().get(i));        }        executor.executeBatch();        return null;      }    });  }  public void insertZcEbOpenBidTeamMember(ZcEbOpenBidTeamMember record) {    getSqlMapClientTemplate().insert("ZcEbOpenBidTeam.insertZcEbOpenBidTeamMember", record);  }  public void updateZcEbOpenBidTeam(final ZcEbOpenBidTeam record) {    getSqlMapClientTemplate().update("ZcEbOpenBidTeam.updateZcEbOpenBidTeam", record);    this.getSqlMapClientTemplate().delete("ZcEbOpenBidTeam.deleteZcEbOpenBidTeamAllMember", record.getTeamCode());    this.getSqlMapClientTemplate().delete("ZcEbOpenBidTeam.deleteZcEbObtPack", record.getTeamCode());    this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {      public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {        executor.startBatch();        for (int i = 0; i < record.getTeamMembers().size(); i++) {          executor.insert("ZcEbOpenBidTeam.insertZcEbOpenBidTeamMember", record.getTeamMembers().get(i));        }        executor.executeBatch();        executor.startBatch();        for (int i = 0; i < record.getPacks().size(); i++) {          executor.insert("ZcEbOpenBidTeam.insertZcEbOpenBidTeamPack", record.getPacks().get(i));        }        executor.executeBatch();        return null;      }    });  }  public List getZcEbOpenBidTeam(ElementConditionDto dto, RequestMeta meta) {    dto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(dto.getWfcompoId(), NumLimConstants.FWATCH));    return getSqlMapClientTemplate().queryForList("ZcEbOpenBidTeam.getZcEbOpenBidTeam", dto);  }  public boolean deleteOpenBidTeam(ZcEbOpenBidTeam team) {    // TODO Auto-generated method stub    getSqlMapClientTemplate().delete("ZcEbOpenBidTeam.deleteZcEbOpenBidTeam", team.getTeamCode());    getSqlMapClientTemplate().delete("ZcEbOpenBidTeam.deleteZcEbOpenBidTeamAllMember", team.getTeamCode());    getSqlMapClientTemplate().delete("ZcEbOpenBidTeam.deleteZcEbObtPack", team.getTeamCode());    return true;  }  public ZcEbOpenBidTeam getZcEbOpenBidTeamByID(String teamCode) {    // TODO Auto-generated method stub    return (ZcEbOpenBidTeam) getSqlMapClientTemplate().queryForObject("ZcEbOpenBidTeam.getZcEbOpenBidTeamByID", teamCode);  }  public List getZcEbOpenBidTeamMembers(String teamCode) {    // TODO Auto-generated method stub    return getSqlMapClientTemplate().queryForList("ZcEbOpenBidTeam.getZcEbOpenBidTeamMember", teamCode);  }  public List getZcEbOpenBidTeamPack(String teamCode) {    // TODO Auto-generated method stub    return getSqlMapClientTemplate().queryForList("ZcEbOpenBidTeam.getZcEbOpenBidTeamPack", teamCode);  }  public List getPackWithoutBidTeam(String projCode) {    // TODO Auto-generated method stub    return getSqlMapClientTemplate().queryForList("ZcEbOpenBidTeam.getPackWithoutBidTeam", projCode);  }}