package com.ufgov.zc.client.zc.zcebXunJiaBaoJia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestDate {

  /**
   * @param args
   */
  public static void main(String[] args) {

    Calendar c=Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    
    try {
      Date d1=df.parse("2015-11-11 10:40:55");
      Date d2=df.parse("2015-01-11 10:40:55");
      
      System.out.println(d1.after(d2));
      
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
