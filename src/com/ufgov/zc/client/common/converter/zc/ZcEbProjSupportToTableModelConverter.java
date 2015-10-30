package com.ufgov.zc.client.common.converter.zc;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.datacache.OrgDataCache;
import com.ufgov.zc.common.commonbiz.model.BaseElement;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.constants.ZcValSetConstants;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.system.util.DateUtil;
import com.ufgov.zc.common.zc.model.ZcEbProjBidHistory;
import com.ufgov.zc.common.zc.model.ZcEbProjSupport;

public class ZcEbProjSupportToTableModelConverter {

  private static DateFormat ssFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static DefaultTableModel convertToTableModel(List projectList) {

    MyTableModel tableModel = null;

    Vector<String> names = new Vector<String>();

    Vector values = new Vector();

    prepareColumnHeader(names);

    String sPeriod = AsOptionMeta.getOptVal(ZcSettingConstants.OPT_ZC_PROJ_SUP_VIEW_PERIOD);

    int period = Integer.parseInt(sPeriod);

    if (projectList != null && projectList.size() > 0) {

      projectList = parseDataList(projectList);

      for (int i = 0; i < projectList.size(); i++) {

        Vector rowData = new Vector();

        ZcEbProjSupport data = (ZcEbProjSupport) projectList.get(i);

        rowData.add(data.getSnCode());

        rowData.add(data.getProjCode());

        rowData.add(data.getProjName());

        //				rowData.add(data.getPackName());

        Date curDate = WorkEnv.getInstance().getSysDate();

        Date openDate = data.getBidEndTime();//校验的点击投标截止时间

        boolean isAllow = false;

        if (openDate != null) {

          //          long t = openDate.getTime() - curDate.getTime();
          //
          //          isAllow = (t <= period * ZcSettingConstants.TIME_MM_PER_MIN);

          isAllow = curDate.getTime() > openDate.getTime();

        }

        if (isAllow || ZcSettingConstants.BILL_STATUS_AUDITED.equals(data.getStatus())) {

          rowData.add(data.getSupportCnt());

        } else {

          rowData.add("未知");

        }

        if (data.getBidEndTime() == null) {
          rowData.add("");
        } else {
          rowData.add(DateUtil.dateToSsString(data.getBidEndTime()));//这里取的是点击投标截止时间
        }

        String sOpenTime = "";
        if (data.getOpenBidTime() != null) {
          sOpenTime = DateUtil.dateToSsString(data.getOpenBidTime());
        }
        rowData.add(sOpenTime);
        rowData.add(data.getOpenBidAddress());
        rowData.add(AsValDataCache.getName(ZcValSetConstants.VS_ZC_VS_PITEM_OPIWAY, data.getZcPifuCgfs()));
        rowData.add(OrgDataCache.getName(data.getSuperintendentOrg()));
        rowData.add(data.getAttnName());

        values.add(rowData);

      }

    }

    tableModel = new MyTableModel(values, names) {

      private static final long serialVersionUID = 3123244681117334262L;

      @Override
      public Class getColumnClass(int column) {

        if (column >= 0 && column < getColumnCount() && this.getRowCount() > 0) {

          for (int row = 0; row < this.getRowCount(); row++) {

            if (getValueAt(row, column) != null) {

              return getValueAt(row, column).getClass();

            }

          }

        }

        return Object.class;

      }

      @Override
      public boolean isCellEditable(int row, int colum) {

        return false;

      }

    };

    tableModel.setList(projectList);

    return tableModel;

  }

  /**
   * <p> 将分包查询改为按项目显示 </p>
   * @param projectList
   * @return 
   * @return List
   * @author yuzz
   * @since Oct 15, 2012 3:39:30 PM
   */
  private static List parseDataList(List projectList) {

    Map m1 = new HashMap();

    Map m2 = new HashMap();

    List newList = new ArrayList();

    for (int i = 0; i < projectList.size(); i++) {

      ZcEbProjSupport data = (ZcEbProjSupport) projectList.get(i);

      StringBuilder sb = new StringBuilder();

      String key = data.getProjCode() + "@@" + (data.getOpenBidTime() == null ? "0000" : data.getOpenBidTime().getTime() + "");

      m1.put(key, data);

      sb.append(m2.get(key) == null ? "" : m2.get(key))

      .append(data.getPackName());

      if (Integer.parseInt(data.getSupportCnt()) >= ZcSettingConstants.SUPPLIER_CNT_ENOUGH) {

        sb.append("满足");

      } else {

        sb.append(data.getSupportCnt()).append("家");

      }

      sb.append(",");

      m2.put(key, sb.toString());

    }

    for (Iterator iterator = m1.keySet().iterator(); iterator.hasNext();) {

      String key = (String) iterator.next();

      ZcEbProjSupport proj = (ZcEbProjSupport) m1.get(key);

      String packDesc = (String) m2.get(key);

      proj.setSupportCnt(packDesc.substring(0, packDesc.lastIndexOf(",")));

      newList.add(proj);

    }

    return newList;
  }

  private static void prepareColumnHeader(Vector<String> names) {

    names.add("任务单号");

    names.add("项目编号");

    names.add("项目名称");

    //		names.add("包名");

    names.add("供应商投标数");

    names.add("点击投标截止时间");

    names.add("开标时间");

    names.add("开标地点");

    names.add("采购方式");

    names.add("负责处室");

    names.add("负责人");

  }

  public static TableModel convertDetailToTableModel(List dataList) {

    BeanTableModel<ZcEbProjSupport> tm = new BeanTableModel<ZcEbProjSupport>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public boolean isCellEditable(int row, int column) {

        String columnId = this.getColumnIdentifier(column);

        if ("IS_SITE".equals(columnId)) {

          return true;

        }
        return false;

      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ZcEbProjSupport bean = dataBeanList.get(rowIndex);

        if (aValue instanceof BaseElement) {

          BeanUtil.set(columnBeanPropertyPairList.get(columnIndex).getBeanPropertyName(), ((BaseElement) aValue).getName(), bean);

          fireTableCellUpdated(rowIndex, columnIndex);

          putEditedData(dataBeanList.get(rowIndex));

        } else {

          super.setValueAt(aValue, rowIndex, columnIndex);

        }

      }

    };

    tm.setOidFieldName("tempId");

    for (Object o : dataList) {

      ((ZcEbProjSupport) o).setTempId(Guid.genID());

    }

    tm.setDataBean(dataList, itemInfo);

    return tm;
  }

  private static List<ColumnBeanPropertyPair> itemInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_SU_NAME, "zcEbSupplier.name", "投标供应商"));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_SIGNUP_DATE, "signupDate", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_SIGNUP_DATE)));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_PACK_NAME, "packName", "确认投标包号"));

    //    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_IS_PAY_GUARANTEE, "isPayGuarantee", LangTransMeta
    //
    //    .translate(ZcElementConstants.FIELD_TRANS_IS_PAY_GUARANTEE)));

    itemInfo.add(new ColumnBeanPropertyPair("IS_SITE", "isSite", "是否到现场"));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_REMARK, "remark", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_REMARK)));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_SU_LINKMAN, "zcEbSupplier.linkMan", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_SU_LINKMAN)));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_LINK_MAN_PHONE, "zcEbSupplier.linkManPhone", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_LINK_MAN_PHONE)));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_LINK_MAN_MOBILE, "zcEbSupplier.linkManMobile", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_LINK_MAN_MOBILE)));

    itemInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_SU_ADDR, "zcEbSupplier.address", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_SU_ADDR)));

  }

  public static List<ColumnBeanPropertyPair> getItemInfo() {

    return itemInfo;

  }

  public static DefaultTableModel convertHistoryToTableModel(List projectList) {

    MyTableModel tableModel = null;

    Vector<String> names = new Vector<String>();

    Vector values = new Vector();

    names.add("任务单号");

    names.add("项目编号");

    names.add("项目名称");

    names.add("包号");

    names.add("点击投标截止时间");

    names.add("开标时间");

    names.add("业务处室");

    if (projectList != null && projectList.size() > 0) {

      for (int i = 0; i < projectList.size(); i++) {

        Vector rowData = new Vector();

        ZcEbProjBidHistory bean = (ZcEbProjBidHistory) projectList.get(i);

        rowData.add(bean.getSnCode());

        rowData.add(bean.getProjCode());

        rowData.add(bean.getProjName());

        rowData.add(bean.getPackName());

        if (bean.getSellEndTime() == null) {
          rowData.add("");
        } else {

          rowData.add(ssFormat.format(bean.getSellEndTime()));//这里取的是点击投标截止时间
        }

        String sOpenTime = "";
        if (bean.getOpenBidTime() != null) {
          sOpenTime = ssFormat.format(bean.getOpenBidTime());
        }
        rowData.add(sOpenTime);
        //        rowData.add(AsValDataCache.getName(ZcValSetConstants.VS_ZC_VS_PITEM_OPIWAY, data.getZcPifuCgfs()));
        rowData.add(OrgDataCache.getName(bean.getOrgCode().toString()));

        values.add(rowData);

      }

    }

    tableModel = new MyTableModel(values, names) {

      private static final long serialVersionUID = 3123244681117334262L;

      @Override
      public Class getColumnClass(int column) {

        if (column >= 0 && column < getColumnCount() && this.getRowCount() > 0) {

          for (int row = 0; row < this.getRowCount(); row++) {

            if (getValueAt(row, column) != null) {

              return getValueAt(row, column).getClass();

            }

          }

        }

        return Object.class;

      }

      @Override
      public boolean isCellEditable(int row, int colum) {

        return false;

      }

    };

    tableModel.setList(projectList);

    return tableModel;

  }

  public static TableModel convertHistoryDetailToTableModel(List dataList) {

    BeanTableModel<ZcEbProjSupport> tm = new BeanTableModel<ZcEbProjSupport>() {

      private static final long serialVersionUID = 6888363838628062064L;

      public boolean isCellEditable(int row, int column) {

        return false;

      }

    };

    tm.setOidFieldName("tempId");

    for (Object o : dataList) {

      ((ZcEbProjSupport) o).setTempId(Guid.genID());

    }

    tm.setDataBean(dataList, hisInfo);

    return tm;
  }

  private static List<ColumnBeanPropertyPair> hisInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    hisInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_ZC_SU_NAME, "zcSuName", "投标供应商"));

    hisInfo.add(new ColumnBeanPropertyPair(ZcElementConstants.FIELD_TRANS_SIGNUP_DATE, "signupDate", LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_SIGNUP_DATE)));

  }

  public static List<ColumnBeanPropertyPair> getHisInfo() {

    return hisInfo;

  }
}
