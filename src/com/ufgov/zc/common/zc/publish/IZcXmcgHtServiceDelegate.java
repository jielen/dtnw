/**   * @(#) project: zc_xa* @(#) file: IZcPProMakeServiceDelegate.java* * Copyright 2010 UFGOV, Inc. All rights reserved.* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.* */package com.ufgov.zc.common.zc.publish;import java.sql.SQLException;import java.util.List;import com.ufgov.zc.common.system.Publishable;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.model.AsFile;import com.ufgov.zc.common.zc.model.ZcHtModel;import com.ufgov.zc.common.zc.model.ZcXmcgHt;import com.ufgov.zc.common.zc.model.ZcXmcgHtExample;/*** @ClassName: IZcPProMakeServiceDelegate* @Description: TODO(这里用一句话描述这个类的作用)* @date: 2010-4-21 下午04:58:10* @version: V1.0 * @since: 1.0* @author: Administrator* @modify: */public interface IZcXmcgHtServiceDelegate extends Publishable {  public List getZcXmcgHt(ElementConditionDto dto, RequestMeta meta) throws SQLException;  public List getZcXmcgHtSup(ElementConditionDto dto, RequestMeta meta) throws SQLException;  /**   *    * @param zcXmcgHt   * @param flag 只有是补充合同和使用指标接口时，该值为true，用于标识需要，操作指标，否则为否。   * @param serverAdd   * @param requestMeta   * @return   * @throws Exception   */  public ZcXmcgHt updateZcXmcgHtFN(ZcXmcgHt zcXmcgHt, boolean flag, String serverAdd, RequestMeta requestMeta) throws Exception;  public ZcXmcgHt selectByPrimaryKey(String zcHtCode, RequestMeta requestMeta);  public void deleteByPrimaryKeyFN(String zcHtCode, boolean flag, String serverAdd, RequestMeta requestMeta) throws Exception;  public ZcXmcgHt newCommitFN(ZcXmcgHt ht, boolean flag, RequestMeta requestMeta) throws Exception;  public ZcXmcgHt callbackFN(ZcXmcgHt ht, RequestMeta requestMeta);  public ZcXmcgHt untreadFN(ZcXmcgHt ht, RequestMeta requestMeta);  public ZcXmcgHt unAuditFN(ZcXmcgHt ht, RequestMeta requestMeta);  public ZcXmcgHt auditFN(ZcXmcgHt ht, RequestMeta requestMeta);  public AsFile getContractContent(ZcXmcgHt zcXmcgHt, RequestMeta requestMeta, String wordFileId);  public String getCompaneyName(String zcCoCode, RequestMeta requestMeta);  public List getZcXmcgHt(ZcXmcgHtExample example, RequestMeta meta) throws SQLException;  public ZcXmcgHt sendToProcurementUnitFN(ZcXmcgHt ht, RequestMeta requestMeta);  public List getZcItemZxByPackCode(String value, String purType, RequestMeta meta);  public List getZcXmcgHtZx(ElementConditionDto dto, RequestMeta meta) throws SQLException;  public ZcXmcgHt selectZxByPrimaryKey(String zcHtCode, RequestMeta requestMeta);  public ZcXmcgHt updateZcXmcgHtFN(ZcXmcgHt zcXmcgHt, RequestMeta requestMeta) throws Exception;  public ZcHtModel getContractContentZx(String projCode, String packCode, RequestMeta requestMeta, String wordFileId);  public ZcXmcgHt newCommitZxFN(ZcXmcgHt ht, RequestMeta requestMeta) throws Exception;  public ZcXmcgHt callbackZxFN(ZcXmcgHt ht, RequestMeta requestMeta);  public ZcXmcgHt untreadZxFN(ZcXmcgHt ht, RequestMeta requestMeta);    public List queryExportsDatas(ElementConditionDto dto, RequestMeta meta);    public String importTransDatasFN(ZcXmcgHt bill, RequestMeta meta);}