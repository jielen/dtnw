package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDelegJczwDataExchange;
import com.ufgov.zc.common.zc.model.ZcEbFormula;
import com.ufgov.zc.common.zc.model.ZcEbPackReq;
import com.ufgov.zc.common.zc.model.ZcEbProjZbFile;
import com.ufgov.zc.common.zc.model.ZcEbProtocol;
import com.ufgov.zc.common.zc.model.ZcEbXunJia;

public class ExportProjectDataBusiness extends AbstractExpDataBusiness implements IExpDataBusiness {
  public final String fileName = "zcEbProj";

  public final String packFileName = "zcEbPack";

  public final String packReqFileName = "pack_req_list";

  public final String packReqDetailFileName = "pack_req_detail_list";

  public final String bidConditionFileName = "zcEbBidConditionList";

  public final String xunjiaFileName = "zcEbXunJiaList";

  public final String projZbFileName = "ZcEbProjZbFile";

  public final String planFileName = "plan_list";

  public final String formulaFileName = "zcEbFormulaList";

  public final String formulaItemFileName = "zcEbFormulaItemList";

  public final String formulaParamFileName = "zcEbFormulaParamList";

  public final String asFileName = "asFile";

  private List<String> projCodes = null;

  public ExportProjectDataBusiness() {

  }

  public ExportProjectDataBusiness(List<String> projCodes, String path, StringBuffer log) {
    init(projCodes, path, log);
  }

  @Override
  public void init(List<String> list, String path, StringBuffer log) {
    // TCJLODO Auto-generated method stub
    this.projCodes = list;
    this.path = path;
    busName = "立项分包";
    this.log = log;
  }

  @Override
  public void exportData() {
    if (projCodes.size() == 0)

      return;
    String path = getDir();
    businessData = dljgJczwDataExpDelegate.getZcEbProj(projCodes, requestMeta);

    saveAsXml(businessData, path, fileName);

    log.append("项目 表导出完毕\n");

    List packList = dljgJczwDataExpDelegate.getZcEbPack(projCodes, requestMeta);

    saveAsXml(packList, path, packFileName);

    log.append("项目分包 表导出完毕\n");

    List zcEbPackReqList = dljgJczwDataExpDelegate.getZcEbPackReq(projCodes, requestMeta);

    saveAsXml(zcEbPackReqList, path, packReqFileName);

    log.append("需求分包 明细关系 表导出完毕\n");

    List detailCodes = getZcEbPackReqDetailCodes(zcEbPackReqList);

    List zcEbPackReqDetail = dljgJczwDataExpDelegate.getZcEbPackReqDetail(detailCodes, requestMeta);

    saveAsXml(zcEbPackReqDetail, path, packReqDetailFileName);

    log.append("分包明细  表导出完毕\n");

    List zcEbBidConditionList = dljgJczwDataExpDelegate.getZcEbBidCondition(projCodes, requestMeta);

    saveAsXml(zcEbBidConditionList, path, bidConditionFileName);

    log.append("供应商资质 关系 表导出完毕\n");

    List zcEbXunJiaList = dljgJczwDataExpDelegate.getZcEbXunJia(projCodes, requestMeta);

    saveAsXml(zcEbXunJiaList, path, xunjiaFileName);

    log.append("询价主  表导出完毕\n");

    List zcEbProjZbfileList = dljgJczwDataExpDelegate.getZcEbProjZbfile(projCodes, requestMeta);

    saveAsXml(zcEbProjZbfileList, path, projZbFileName);

    log.append("招标文件  表导出完毕\n");

    List zcEbPlanList = dljgJczwDataExpDelegate.getZcEbPlan(projCodes, requestMeta);

    saveAsXml(zcEbPlanList, path, planFileName);

    log.append("计划  表导出完毕\n");

    List zcEbFormulaList = dljgJczwDataExpDelegate.getZcEbFormula(projCodes, requestMeta);

    saveAsXml(zcEbFormulaList, path, formulaFileName);

    log.append("项目需求确认  表导出完毕\n");

    List formulaCodes = getFormulaCodes(zcEbFormulaList);

    List zcEbFormulaItem = dljgJczwDataExpDelegate.getZcEbFormulaItem(formulaCodes, requestMeta);

    saveAsXml(zcEbFormulaItem, path, formulaItemFileName);

    log.append("指标项  表导出完毕\n");

    List zcEbFormulaParamList = dljgJczwDataExpDelegate.getZcEbFormulaParam(formulaCodes, requestMeta);

    saveAsXml(zcEbFormulaParamList, path, formulaParamFileName);

    log.append("指标参数  表导出完毕\n");

    List fileIds = getZcEbProjAsFile(zcEbProjZbfileList, zcEbXunJiaList);

    List asFileList = dljgJczwDataExpDelegate.getAsFile(fileIds, requestMeta);

    saveAsXml(asFileList, path, asFileName);

    log.append("项目立项相关附件 表导出完毕\n");
    exportWorkFlow();
  }

  private List getZcEbProtcolAsFile() {

    List<String> fileIds = new ArrayList<String>();

    for (Iterator iterator = businessData.iterator(); iterator.hasNext();) {

      ZcEbProtocol pc = (ZcEbProtocol) iterator.next();

      if (pc.getProtFileBlobid() != null && !"".equals(pc.getProtFileBlobid())) {

        fileIds.add(pc.getProtFileBlobid());

      }

    }

    return fileIds;

  }

  private List getZcEbPackReqDetailCodes(List zcEbPackReqList) {

    List<String> keyIdsList = new ArrayList<String>();

    for (Iterator iterator = zcEbPackReqList.iterator(); iterator.hasNext();) {

      ZcEbPackReq pr = (ZcEbPackReq) iterator.next();

      keyIdsList.add(pr.getDetailCode().toString());

    }

    return keyIdsList;

  }

  private List getFormulaCodes(List zcEbFormulaList) {

    List<String> formulaCodes = new ArrayList<String>();

    for (Iterator iterator = zcEbFormulaList.iterator(); iterator.hasNext();) {

      ZcEbFormula pc = (ZcEbFormula) iterator.next();

      formulaCodes.add(pc.getFormulaCode());

    }

    return formulaCodes;

  }

  private List getZcEbProjAsFile(List zcEbProjZbfileList, List zcEbXunJiaList) {

    List<String> fileIds = new ArrayList<String>();

    if (zcEbProjZbfileList != null) {

      for (Iterator iterator = zcEbProjZbfileList.iterator(); iterator.hasNext();) {

        ZcEbProjZbFile zbfile = (ZcEbProjZbFile) iterator.next();

        if (zbfile != null && zbfile.getFileId() != null && !"".equals(zbfile.getFileId())) {

          fileIds.add(zbfile.getFileId());

        }

      }

    }

    if (zcEbXunJiaList != null) {

      for (Iterator iterator = zcEbXunJiaList.iterator(); iterator.hasNext();) {

        ZcEbXunJia xj = (ZcEbXunJia) iterator.next();

        if (xj != null && xj.getSpTechFileId() != null && !"".equals(xj.getSpTechFileId())) {

          fileIds.add(xj.getSpTechFileId());

        }

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
    return ZcDelegJczwDataExchange.DATA_TYPE_ZC_EB_PROJ;
  }

}
