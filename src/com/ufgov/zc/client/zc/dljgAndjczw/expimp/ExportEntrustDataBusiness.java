package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.model.ZcEbEntrustDetail;

public class ExportEntrustDataBusiness extends AbstractExpDataBusiness implements IExpDataBusiness {
  public static final String fileName = "entrustList";

  public static final String entrustDetailFileName = "entrustDetailList";

  public static final String makeFileName = "zcPProMakeList";

  public static final String mitemBiFileName = "zcPProMitemBiList";

  public static final String mitemFileName = "zcPProMitemList";

  public static final String mitemMerFileName = "zcPProMitemMerListMer";

  public static final String asFileName = "asFile";

  private List<String> snList = null;

  private List<ZcBaseBill> businessData;

  public ExportEntrustDataBusiness() {

  }

  public ExportEntrustDataBusiness(List<String> snList, String path, StringBuffer log) {
    init(snList, path, log);
  }

  public void init(List<String> snList, String path, StringBuffer log) {
    this.snList = snList;
    busName = "任务单";
    this.path = path;
    this.log = log;
  }

  @Override
  public void exportData() {
    if (snList.size() == 0)

      return;
    String path = getDir();
    businessData = dljgJczwDataExpDelegate.getZcEbEntrustData(snList, requestMeta);

    saveAsXml(businessData, path, fileName);

    log.append("采购任务单 表导出完毕\n");

    List zcEbEntrustDetailList = dljgJczwDataExpDelegate.getZcEbEntrustDetailData(snList, requestMeta);

    saveAsXml(zcEbEntrustDetailList, path, entrustDetailFileName);

    log.append("采购任务单明细 表导出完毕\n");

    List makeList = dljgJczwDataExpDelegate.getZcPProMakeData(getZcMakeCodesFromEntrust(), requestMeta);

    saveAsXml(makeList, path, makeFileName);

    log.append("采购计划  表导出完毕\n");

    List biList = dljgJczwDataExpDelegate.getZcPProMitemBi(getZcMakeCodesFromEntrust(), requestMeta);

    saveAsXml(biList, path, mitemBiFileName);

    log.append("采购计划资金明细  表导出完毕\n");

    List mitemList = dljgJczwDataExpDelegate.getZcPProMitem(getZcMakeCodesFromEntrust(), requestMeta);

    saveAsXml(mitemList, path, mitemFileName);

    log.append("采购计划需求  表导出完毕\n");

    List mitemMerList = dljgJczwDataExpDelegate.getZcPProMitemMer(getZcMakeCodesFromEntrust(), requestMeta);

    saveAsXml(mitemMerList, path, mitemMerFileName);

    log.append("采购计划电子竞价商品明细信息  表导出完毕\n");

    List fileIds = getZcEbEntrustAsFile(zcEbEntrustDetailList);

    List asFileList = dljgJczwDataExpDelegate.getAsFile(fileIds, requestMeta);

    saveAsXml(asFileList, path, asFileName);

    log.append("采购任务附件  表导出完毕\n");

  }

  private List getZcMakeCodesFromEntrust() {

    List<String> makeCodes = new ArrayList<String>();

    for (Iterator iterator = this.businessData.iterator(); iterator.hasNext();) {

      ZcEbEntrust ent = (ZcEbEntrust) iterator.next();

      if (ent.getZcMakeCode() != null) {

        makeCodes.add(ent.getZcMakeCode());

      }

    }

    return makeCodes;

  }

  private List getZcEbEntrustAsFile(List zcEbEntrustDetailList) {

    // TCJLODO Auto-generated method stub

    List<String> fileIds = new ArrayList<String>();

    for (Iterator iterator = this.businessData.iterator(); iterator.hasNext();) {

      ZcEbEntrust zcEbEntrust = (ZcEbEntrust) iterator.next();

      if (zcEbEntrust.getZcImpFileBlobid() != null && !"".equals(zcEbEntrust.getZcImpFileBlobid())) {

        fileIds.add(zcEbEntrust.getZcImpFileBlobid());

      }

    }

    for (Iterator iterator = zcEbEntrustDetailList.iterator(); iterator.hasNext();) {

      ZcEbEntrustDetail detail = (ZcEbEntrustDetail) iterator.next();

      if (detail.getZcPitemAttachBlobid() != null && !"".equals(detail.getZcPitemAttachBlobid())) {

        fileIds.add(detail.getZcPitemAttachBlobid());

      }

    }

    return fileIds;

  }

  @Override
  public String getBusName() {
    // TCJLODO Auto-generated method stub
    return busName;
  }

  @Override
  public List<ZcBaseBill> getBusinessData() {
    return businessData;
  }

  @Override
  public String getDataType() {
    // TCJLODO Auto-generated method stub
    return ZcDelegJczwDataExchange.DATA_TYPE_ZC_EB_ENTRUST;
  }

}
