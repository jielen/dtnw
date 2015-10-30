package com.ufgov.zc.client.zc.ztb.docautogenerate.model;

import java.util.Map;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcEbFormula;
import com.ufgov.zc.common.zc.publish.IZcEbFormulaServiceDelegate;

public class FormlaWorldDataMapGenaral {

  protected String projectId;

  protected String packId;

  protected ZcEbFormula zcEbFormula;

  private String packName;

  public FormlaWorldDataMapGenaral(String projectId) {
    this(projectId, null);
  }

  public FormlaWorldDataMapGenaral(String projectId, String packId) {
    this(projectId, packId, null);
  }

  public FormlaWorldDataMapGenaral(String projectId, String packId, String packName) {
    this.projectId = projectId;
    this.packId = packId;
    this.packName = packName;
    initZcEbFormula();
  }

  private void initZcEbFormula() {
    ElementConditionDto dto = new ElementConditionDto();
    dto.setPackCode(projectId);
    dto.setPackCode(packId);
    RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();
    IZcEbFormulaServiceDelegate zcEbFormulaServiceDelegate = (IZcEbFormulaServiceDelegate) ServiceFactory.create(IZcEbFormulaServiceDelegate.class,
      "zcEbFormulaServiceDelegate");
    zcEbFormula = zcEbFormulaServiceDelegate.getZcEbFormulaById(dto, requestMeta);
  }

  private String getScoreTableName() {
    if (packName == null) {
      return "评审分值及评审要素表";
    } else {
      return "标段:(" + packName + ") 评审分值及评审要素表";
    }
  }

  public Map genScoreIteWordTableMap() {
    FormulaScoreItemWordGenaral gen = new FormulaScoreItemWordGenaral(zcEbFormula.getScoreItemList(), getScoreTableName());
    return gen.genWordTableModel();
  }

  private String getComplicationTableName() {
    if (packName == null) {
      return "符合性评标方法表";
    } else {
      return "标段:(" + packName + ") 符合性评标方法表";
    }
  }

  public Map genComplicationWordTableMap() {
    //设置表名
    FormulaComplicationWordGenaral gen = new FormulaComplicationWordGenaral(zcEbFormula.getComplicationItemList(), getComplicationTableName());
    return gen.genWordTableModel();
  }
}
