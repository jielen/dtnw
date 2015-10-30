/** *  */package com.ufgov.zc.server.zc.web.action.agency;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import javax.servlet.http.HttpServletRequest;import javax.servlet.http.HttpServletResponse;import org.apache.commons.logging.Log;import org.apache.commons.logging.LogFactory;import org.apache.struts.action.ActionForm;import org.apache.struts.action.ActionForward;import org.apache.struts.action.ActionMapping;import com.ufgov.zc.common.zc.pagination.WebPagination;import com.ufgov.zc.server.zc.dao.pagination.PaginationHandler;import com.ufgov.zc.server.zc.dao.pagination.PaginationHandlerJSImpl;import com.ufgov.zc.server.zc.service.IZcBAgencyService;import com.ufgov.zc.server.zc.web.action.StrutsAction;import com.ufgov.zc.server.zc.web.form.AgencyFormBean;public class AgencySearchAction extends StrutsAction {  private final static Log log = LogFactory.getLog(AgencySearchAction.class);  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {    AgencyFormBean agencyFormBean = (AgencyFormBean) form;    String forward = "success";    try {      String status = agencyFormBean.getAgency().getZcStatCode() == null ? request.getParameter("status") : agencyFormBean.getAgency()      .getZcStatCode();      Map parameterObject = new HashMap();      parameterObject.put("status", status);      List orders = new ArrayList();      orders.add("ZC_INPUT_DATE");      parameterObject.put("orders", orders);      IZcBAgencyService zcBAgencyService = (IZcBAgencyService) this.getWebApplicationContext().getBean("zcBAgencyService");      //分页      PaginationHandler handler = new PaginationHandlerJSImpl();      WebPagination pagination = webPagination(request, mapping);      handler.setPerCount(pagination.getPageSize());      long count = new Long(zcBAgencyService.getAgencyCount(parameterObject).intValue()).longValue();      handler.setTotalCount(count);      pagination.setTotalSize((int) count);      handler.setTotalCount(count);      handler.moveCursor((long) pagination.getCurrentPage());      //request.setAttribute("handler", pagination);      agencyFormBean.setPagination(pagination);      parameterObject.put("start", new Long(handler.getStartCursor()).toString());      parameterObject.put("end", new Long(handler.getEndCursor()).toString());      agencyFormBean.setAgencyList(zcBAgencyService.getAgency(parameterObject));      agencyFormBean.setCountNum(new Integer(new Long(count).intValue()));      agencyFormBean.getAgency().setZcStatCode(status);    } catch (Exception e) {      log.error(e);      throw e;    }    return mapping.findForward(forward);  }}