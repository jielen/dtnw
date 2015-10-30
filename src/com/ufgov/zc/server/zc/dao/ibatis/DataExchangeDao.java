package com.ufgov.zc.server.zc.dao.ibatis;import java.util.List;import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.model.DataExchange;import com.ufgov.zc.common.zc.model.DataExchangeLog;import com.ufgov.zc.common.zc.model.DataExchangeRedo;import com.ufgov.zc.server.zc.dao.IDataExchangeDao;public class DataExchangeDao extends SqlMapClientDaoSupport implements IDataExchangeDao {  public List getDataExchangeLogs(ElementConditionDto dto) {    return this.getSqlMapClientTemplate().queryForList("DataExchange.getDataExchangeLog", dto);  }  public DataExchangeLog save(DataExchangeLog log) {    this.getSqlMapClientTemplate().insert("DataExchange.insertDataExchangeLog", log);    return log;  }  public List getAllDataExchange() {    return this.getSqlMapClientTemplate().queryForList("DataExchange.getDataExchange");  }  public List getDataExchangeRedos(ElementConditionDto dto) {    return this.getSqlMapClientTemplate().queryForList("DataExchange.getDataExchangeRedo", dto);  }  public void saveRedo(DataExchangeRedo redo) {    this.getSqlMapClientTemplate().insert("DataExchange.insertDataExchangeRedo", redo);  }  public DataExchange save(DataExchange de) {    this.getSqlMapClientTemplate().insert("DataExchange.insertDataExchange", de);    return de;  }  public void updateDataExchangeRedoIsExported(DataExchangeRedo redo) {    this.getSqlMapClientTemplate().update("DataExchange.updateDataExchangeRedoIsExported", redo);  }  public void delete(DataExchangeRedo redo) {    this.getSqlMapClientTemplate().update("DataExchange.deleteByRecordIdAndIsExported", redo);  }  public void deleteByRecordIDAndDataTypeID(DataExchangeRedo redo) {    this.getSqlMapClientTemplate().delete("DataExchange.deleteByRecordIdAndDataTypeId", redo);  }  public List getDataExchangeRedosByLikeRecordID(ElementConditionDto redoDto) {    return this.getSqlMapClientTemplate().queryForList("DataExchange.getDataExchangeRedoByLikeRecordID",    redoDto);  }}