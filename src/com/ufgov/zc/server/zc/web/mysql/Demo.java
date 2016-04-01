package com.ufgov.zc.server.zc.web.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Demo {

  static String sql = null;

  static MysqlDBHelper db1 = null;

  static ResultSet ret = null;

  public static void main(String[] args) {
    sql = "select * from jc_content";//SQL语句  
    //    sql = "select max(content_id) from jc_content";//SQL语句  
    //    sql = "select max(content_id) from jc_content where content_id like '20151213%'";
    Connection con = null;
    Statement st = null;
    MysqlDBHelper dbHelper = MysqlDBHelper.getInstance();
    try {
      con = dbHelper.getConnection();
      st = con.createStatement();
      ret = st.executeQuery(sql);//执行语句，得到结果集  
      while (ret.next()) {
        String uid = ret.getString(1);
        String ufname = ret.getString(2);
        String ulname = ret.getString(3);
        String udate = ret.getString(4);
        String udate2 = ret.getString(5);
        String udate3 = ret.getString(6);
        String udate4 = ret.getString(7);
        String udate5 = ret.getString(8);
        System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate + "\t" + udate2 + "\t" + udate3 + "\t" + udate4 + "\t" + udate5);

        //        String uid = ret.getString(1);
        //        System.out.println(uid);

      }//显示数据  
      sql = "insert into jc_content(content_id,channel_id,user_id,type_id,model_id,site_id,status,sort_date) value(2015121101,98,1,1,9,1,2,NOW())";
      sql = "delete from jc_content where content_id=2015121101";
      //      st.executeUpdate(sql);

      ret.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (st != null) try {
        st.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      dbHelper.close(con);
    }
  }

}
