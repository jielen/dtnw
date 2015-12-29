package com.ufgov.zc.server.zc.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufgov.zc.common.system.util.DigestUtil;

public class DigestCheckServlet extends HttpServlet {

	/**
   * 
   */
	private static final long serialVersionUID = -6869572889139391726L;

	// jdk1.4不支持ConcurrentHashMap
	// private ConcurrentHashMap cache = new ConcurrentHashMap();
	private HashMap cache2 = new HashMap();

	private synchronized Object getCache(String key) {
		return cache2.get(key);
	}

	private synchronized void addCache(String key, Object val) {
		cache2.put(key, val);
	}

	public DigestCheckServlet() {
		// TCJLODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// String resourceName = request.getParameter("resname");
		String[] resourceNames = request.getParameterValues("resname");
		String result = "";
		for (int i = 0; i < resourceNames.length; i++) {
			result += "," + resourceNames[i] + ":"
					+ getDigest(resourceNames[i], request);
		}
		result = result.length() > 0 ? result.substring(1) : result;
		response.getWriter().write(result);
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private String getDigest(String resourceName, HttpServletRequest request) {
		String result = "0";// -1: 资源不存在
		if (false) {
			result = (String) getCache(resourceName);
		} else {
			try {
				InputStream source = new FileInputStream(
						(request.getRealPath("") + "/applets/plugins/" + resourceName));
				if (source == null) {
					result = "-1";
				} else {
					ByteArrayOutputStream bo = new ByteArrayOutputStream();
					BufferedInputStream bSource = new BufferedInputStream(
							source);
					byte[] buffer = new byte[1024];
					int length = bSource.read(buffer);
					while (length > 0) {
						bo.write(buffer, 0, length);
						length = bSource.read(buffer);
					}
					bSource.close();
					result = DigestUtil.digest(bo.toByteArray());
					addCache(resourceName, result);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				result = "-2";
			}
		}
		return result;
	}
}
