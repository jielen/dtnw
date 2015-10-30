package com.ufgov.zc.client.zc.ztb.docautogenerate.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;

public class FormulaWordDataModel implements IWordDataModel {
  private String projectId;

  private List packList;

  public String packId;

  public FormulaWordDataModel(String projectId, String packId) {
    this.projectId = projectId;
    initPackList();
    this.packId = packId;
    ZcEbPack pack = getPackById(packId);
    packList = new ArrayList();
    packList.add(pack);
  }

  public FormulaWordDataModel(String projectId) {
    this.projectId = projectId;
    initPackList();
  }

  private ZcEbPack getPackById(String packId) {
    Iterator itor = packList.iterator();
    ZcEbPack p = null;
    while (itor.hasNext()) {
      ZcEbPack pa = (ZcEbPack) itor.next();
      if (packId.equals(pa.getPackCode())) {
        p = pa;
        break;
      }
    }
    return p;
  }

  private void initPackList() {
    IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

    "zcEbBaseServiceDelegate");

    ElementConditionDto dto = new ElementConditionDto();
    dto.setZcText0(projectId);
    RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();
    packList = zcEbBaseServiceDelegate.queryDataForList("ZcEbProj.getZcEbReqPackByEntrustCode", dto, requestMeta);
  }

  public Map getFillWordMap() {
    Map root = new HashMap();
    List list = new ArrayList();
    Iterator itor = packList.iterator();
    while (itor.hasNext()) {
      ZcEbPack pa = (ZcEbPack) itor.next();
      FormlaWorldDataMapGenaral g = new FormlaWorldDataMapGenaral(projectId, pa.getPackCode(), pa.getPackName());
      list.add(g.genScoreIteWordTableMap());
      list.add(g.genComplicationWordTableMap());
    }
    root.put("formularTabs", list);

    return root;
  }
}
