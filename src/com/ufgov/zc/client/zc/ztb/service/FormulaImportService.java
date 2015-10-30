package com.ufgov.zc.client.zc.ztb.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufgov.zc.client.zc.ztb.component.MainPanel;
import com.ufgov.zc.client.zc.ztb.dao.ServerProjectDao;
import com.ufgov.zc.client.zc.ztb.docautogenerate.model.FormulaWordDataModel;
import com.ufgov.zc.client.zc.ztb.docautogenerate.operate.GenWordUtil;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.common.zc.model.ZcEbPack;

public class FormulaImportService {

  private ServerProjectDao serverProjectDao = ServerProjectDao.getInstance();

  private String formulaFileDown = GV.USER_DIR + File.separator + GV.REQ_FILE_DIR + File.separator + "formula" + File.separator;

  public void updateBagDetails(final List<String> checkedCodes, final String combineFilePath, final MainPanel mainPanel) throws Exception {
    List<String> fListPath = new ArrayList<String>();
    for (String fildPath : checkedCodes) {
      File f = new File(fildPath);
      fListPath.add(f.getAbsolutePath());
    }
    mainPanel.getWordPane().combineMsWord(fListPath);
  }

  //添加方法，根据项目编号或者标段获取需求文件中的内容.

  public List<Map<String, String>> getFormulaFilesList(String projNo) throws Exception {
    Map returnMap = serverProjectDao.getProjectPackList(projNo);
    List<Map<String, Object>> mapList = (List<Map<String, Object>>) returnMap.get("RESULTMAPLIST");
    List<ZcEbPack> packList = new ArrayList<ZcEbPack>();
    for (int i = 0; i < mapList.size(); i++) {
      Map<String, Object> item = (Map<String, Object>) mapList.get(i);
      ZcEbPack pack = new ZcEbPack();
      pack.setPackName((String) item.get("PACK_NAME"));
      pack.setPackCode((String) item.get("PACK_CODE"));
      pack.setEntrustCode((String) item.get("ENTRUST_CODE"));
      packList.add(pack);
    }
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    for (ZcEbPack zcEbPack : packList) {
      GenWordUtil util = new GenWordUtil(new FormulaWordDataModel(zcEbPack.getEntrustCode(), zcEbPack.getPackCode()), "manyWordTableTemplate.ftl",
        formulaFileDown);
      File path;
      try {
        path = util.outWordFilePath();
      } catch (Exception e) {
        e.printStackTrace();
        throw new Exception("没有找到评标方法！");
      }
      generFormulaFile(zcEbPack, list, path);
    }
    return list;

  }

  public void generFormulaFile(ZcEbPack zcEbPack, List<Map<String, String>> list, File file) {
    Map<String, String> map1 = new HashMap<String, String>();
    map1.put("DETAILS_NAME", zcEbPack.getPackName() + "的评标方法");
    map1.put("FILE_NAME", zcEbPack.getPackName() + "的评标方法");
    map1.put("FILE_ID", file.getPath());
    list.add(map1);
  }
}
