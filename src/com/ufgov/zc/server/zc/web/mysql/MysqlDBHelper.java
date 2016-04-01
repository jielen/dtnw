/**
 * 
 */
package com.ufgov.zc.server.zc.web.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ufgov.zc.common.system.exception.BusinessException;

/**
 * 外网网站数据库连接，用于将公告等数据发布出去
 * 有个问题，因为mysql的驱动，其版本比较高，不能在jdk1.4的环境下运行，build的时候没有问题，但运行时会报奇怪的错误
 * ，程序会停在Class.forName(driverName)这里 因此只能运行在weblogic10以上的版本上
 * @author Administrator
 */
public class MysqlDBHelper {
  private String url = "";

  private final String driverName = "com.mysql.jdbc.Driver";

  private String user = "";

  private String password = "";

  private static boolean hasInitialed = false;

  private static List conCache = new ArrayList();

  private final int conLimits = 2;

  private static MysqlDBHelper instance = null;

  public static MysqlDBHelper getInstance() throws BusinessException {
    //    return new MysqlDBHelper(); 
    synchronized (MysqlDBHelper.class) {
      if (instance == null) {
        try {
          instance = new MysqlDBHelper();
        } catch (Exception e) {
          // TODO: handle exception
          throw new BusinessException("获取外网数据库实例出错\n" + e.getMessage(), e);
        }
      }
    }
    return instance;
  }

  private MysqlDBHelper() throws BusinessException {

    init();

  }

  public synchronized Connection getConnection() throws BusinessException {
    try {
      if (conCache.size() == 0) {
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
      } else {
        Connection con = (Connection) conCache.remove(conCache.size() - 1);
        if (con.isClosed()) {
          return getConnection();
        } else {
          return con;
        }
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      //      e.printStackTrace();
      throw new BusinessException("获取外网数据库连接出错\n" + e.getMessage(), e);
    }
  }

  private void init() throws BusinessException {
    if (!hasInitialed) {
      try {
        //        com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
        Class.forName(driverName);//指定连接类型   
        //获取数据库配置信息
        InitConfigInfo();
        hasInitialed = true;
      } catch (Exception e) {
        throw new BusinessException("初始化外网数据库连接出错\n" + e.getMessage(), e);
        // TODO: handle exception
      }
    }
    //    Class.forName(driverName);//指定连接类型  
    //    //获取数据库配置信息
    //    InitConfigInfo();
  }

  private void InitConfigInfo() throws BusinessException {
    Properties p = new Properties();
    try {
      p.load(MysqlDBHelper.class.getResourceAsStream("mysql.properties"));
      url = p.getProperty("url");
      user = p.getProperty("user");
      password = p.getProperty("pwd");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      //      e.printStackTrace();
      throw new BusinessException("获取外网数据库配置文件信息出错\n" + e.getMessage(), e);
    }
  }

  public synchronized void close(Connection con) {
    if (con == null) return;
    try {
      if (conCache.size() < conLimits && !con.isClosed()) {
        conCache.add(con);
      } else {
        con.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    //开发期间默认调用这个方法，每次都关闭，防止系统推出了，连接没有关
    destroy();
  }

  public void destroy() {
    try {
      for (int i = 0; i < conCache.size(); i++) {
        Connection con = (Connection) conCache.get(i);
        con.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      conCache.clear();
    }
  }

  private static void log2(String str) {
    System.out.println(str);
  }
}
