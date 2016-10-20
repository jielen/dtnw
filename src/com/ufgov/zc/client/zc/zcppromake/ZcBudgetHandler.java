package com.ufgov.zc.client.zc.zcppromake;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.common.budget.model.VwBudgetGp;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcDingdian;
import com.ufgov.zc.common.zc.model.ZcDingdianBi;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcQb;
import com.ufgov.zc.common.zc.model.ZcQbBi;
import com.ufgov.zc.common.zc.model.ZcQx;
import com.ufgov.zc.common.zc.model.ZcQxBi;
import com.ufgov.zc.common.zc.model.ZcSupplementPProMake;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.model.ZcYearPlan;

public class ZcBudgetHandler implements IForeignEntityHandler {

  private String columNames[];

  private JTablePanel biTablePanel;

  private AbstractMainSubEditPanel editPanel;

  private ListCursor listCursor;

  private ElementConditionDto getDto;

  public ZcBudgetHandler(String columNames[], JTablePanel biTablePanel, AbstractMainSubEditPanel edit, ListCursor listCursor,
    ElementConditionDto getDto) {

    this.columNames = columNames;
    this.biTablePanel = biTablePanel;
    this.editPanel = edit;
    this.listCursor = listCursor;
    this.getDto = getDto;

  }

  public boolean beforeSelect(ElementConditionDto dto) {
    List biLst = null;
    Object bill = listCursor.getCurrentObject();
    dto.setOrgCode(null);

    if (bill instanceof ZcPProMake) {
      biLst = ((ZcPProMake) bill).getBiList();
      if (biLst != null) {
        for (int i = 0; i < biLst.size(); i++) {
          ZcPProMitemBi mb = (ZcPProMitemBi) biLst.get(i);
          if (mb.getOrgCode() != null && mb.getOrgCode().length() > 0) {
            dto.setOrgCode(mb.getOrgCode());
            return true;
          }
        }
      }

    }else if (bill instanceof ZcSupplementPProMake) {//追加资金
      biLst = ((ZcSupplementPProMake) bill).getSuppleMentBiList();
      if (biLst != null) {
        for (int i = 0; i < biLst.size(); i++) {
          ZcPProMitemBi mb = (ZcPProMitemBi) biLst.get(i);
          if (mb.getOrgCode() != null && mb.getOrgCode().length() > 0) {
            dto.setOrgCode(mb.getOrgCode());
            return true;
          }
        }
      }

    } else if (bill instanceof ZcXmcgHt) {
      biLst = ((ZcXmcgHt) bill).getBiList();
      if (biLst != null) {
        for (int i = 0; i < biLst.size(); i++) {
          ZcXmcgHtBi mb = (ZcXmcgHtBi) biLst.get(i);
          if (mb.getOrgCode() != null && mb.getOrgCode().length() > 0) {
            dto.setOrgCode(mb.getOrgCode());
            return true;
          }
        }
      }
    } else if (bill instanceof ZcQx) {
      biLst = ((ZcQx) bill).getBiList();
      if (biLst != null) {
        for (int i = 0; i < biLst.size(); i++) {
          ZcQxBi mb = (ZcQxBi) biLst.get(i);
          if (mb.getOrgCode() != null && mb.getOrgCode().length() > 0) {
            dto.setOrgCode(mb.getOrgCode());
            return true;
          }
        }
      }
    } else if (bill instanceof ZcDingdian) {
      biLst = ((ZcDingdian) bill).getBiList();
      if (biLst != null) {
        for (int i = 0; i < biLst.size(); i++) {
          ZcDingdianBi mb = (ZcDingdianBi) biLst.get(i);
          if (mb.getOrgCode() != null && mb.getOrgCode().length() > 0) {
            dto.setOrgCode(mb.getOrgCode());
            return true;
          }
        }
      }
    }
    return true;
  }

  public void excute(List selectedDatas) {

//    if (!editPanel.budgetExcuce(selectedDatas)) {
//      return;
//    }

    JTable table = biTablePanel.getTable();

    BeanTableModel model = (BeanTableModel) table.getModel();

    int k = table.getSelectedRow();

    if (k < 0)

      return;

    int k2 = table.convertRowIndexToModel(k);

    if (selectedDatas.size() > 0) {
      Object obj = listCursor.getCurrentObject();
      if (obj instanceof ZcPProMake) {
        ZcPProMake make = (ZcPProMake) obj;
        ZcPProMitemBi bi = (ZcPProMitemBi) make.getBiList().get(k2);

        VwBudgetGp gp = (VwBudgetGp) selectedDatas.get(0);
        String sumId = "";
        if (bi.getZcBiNo() != null) {
          sumId = bi.getZcBiNo();
        }

        bi.setZcBiNo(gp.getSumId() + "");
        bi.setZcBiDoSum(gp.getCanuseMoney());
        bi.setZcBiSum(gp.getBudgetMoney());
        // 预算单位
        if (gp.getEnCode() != null) {
          bi.setCoCode(gp.getEnCode());
          bi.setCoName(gp.getEnName());
        }
        // 资金性质
        if (gp.getMkCode() != null) {
          bi.setFundCode(gp.getMkId());
          bi.setFundName(gp.getMkName());
        }
        // 功能分类
        if (gp.getBsCode() != null) {
          bi.setbAccCode(gp.getBsCode());
          bi.setbAccName(gp.getBsCode()+gp.getBsName());
        }
        // 经济分类
        if (gp.getBsiCode() != null) {
          bi.setOutlayCode(gp.getBsiId());
          bi.setOutlayName(gp.getBsiCode()+gp.getBsiName());
        }
        // 项目类别
        if (gp.getBiCode() != null) {
          bi.setProjectTypeCode(gp.getBiCode());
          bi.setProjectTypeName(gp.getBiName());
        }
        // 付款方式
        if (gp.getPkCode() != null) {
          bi.setPaytypeCode(gp.getPkCode());
          bi.setPaytypeName(gp.getPkName());
        }
        // 指标来源
        if (gp.getBlCode() != null) {
          bi.setOriginCode(gp.getBlCode());
          bi.setOriginName(gp.getBlName());
        }
        // 业务处室
        if (gp.getMbId() != null) {
          bi.setOrgCode(gp.getMbId());
          bi.setOrgName(gp.getMbName());
        }
        // 年度
        bi.setNd(gp.getSetYear() + "");
        // 文号
        if (gp.getFileCode() != null) {
          bi.setSenddocCode(gp.getFileCode());
          bi.setSenddocName(gp.getFileName());
        }

        // 指标流向
        if (gp.getBtCode() != null) {
          bi.setBiTargetCode(gp.getBtCode());
        }
        // 预算项目
        if (gp.getBisCode() != null) {
          bi.setProjectCode(gp.getBisCode());
          bi.setProjectName(gp.getBisName());
        }
        //是否监督使用
        bi.setBtName(gp.getBtName());
        //是否政府采购
        bi.setGbName(gp.getGbName());

        if (getDto.getZcText3() != null && !"".equals(getDto.getZcText3())) {
          getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + sumId + "'", "").replaceAll("'" + sumId + "',", "")
            .replaceAll("'" + sumId + "'", ""));
        }
        if (getDto.getZcText3() == null || "".equals(getDto.getZcText3())) {
          getDto.setZcText3("'" + gp.getSumId() + "'");
        } else {
          getDto.setZcText3(getDto.getZcText3() + ",'" + gp.getSumId() + "'");
        }

        editPanel.setEditingObject(make);

      }else if (obj instanceof ZcSupplementPProMake) {
        ZcSupplementPProMake zjzj = (ZcSupplementPProMake) obj;
        ZcPProMitemBi bi = (ZcPProMitemBi) zjzj.getSuppleMentBiList().get(k2);

        VwBudgetGp gp = (VwBudgetGp) selectedDatas.get(0);
        String sumId = "";
        if (bi.getZcBiNo() != null) {
          sumId = bi.getZcBiNo();
        }

        bi.setZcBiNo(gp.getSumId() + "");
        bi.setZcBiDoSum(gp.getCanuseMoney());
        bi.setZcBiSum(gp.getBudgetMoney());
        // 预算单位
        if (gp.getEnCode() != null) {
          bi.setCoCode(gp.getEnCode());
          bi.setCoName(gp.getEnName());
        }
        // 资金性质
        if (gp.getMkCode() != null) {
          bi.setFundCode(gp.getMkId());
          bi.setFundName(gp.getMkName());
        }
        // 功能分类
        if (gp.getBsCode() != null) {
          bi.setbAccCode(gp.getBsCode());
          bi.setbAccName(gp.getBsCode()+gp.getBsName());
        }
        // 经济分类
        if (gp.getBsiCode() != null) {
          bi.setOutlayCode(gp.getBsiId());
          bi.setOutlayName(gp.getBsiCode()+gp.getBsiName());
        }
        // 项目类别
        if (gp.getBiCode() != null) {
          bi.setProjectTypeCode(gp.getBiCode());
          bi.setProjectTypeName(gp.getBiName());
        }
        // 付款方式
        if (gp.getPkCode() != null) {
          bi.setPaytypeCode(gp.getPkCode());
          bi.setPaytypeName(gp.getPkName());
        }
        // 指标来源
        if (gp.getBlCode() != null) {
          bi.setOriginCode(gp.getBlCode());
          bi.setOriginName(gp.getBlName());
        }
        // 业务处室
        if (gp.getMbId() != null) {
          bi.setOrgCode(gp.getMbId());
          bi.setOrgName(gp.getMbName());
        }
        // 年度
        bi.setNd(gp.getSetYear() + "");
        // 文号
        if (gp.getFileCode() != null) {
          bi.setSenddocCode(gp.getFileCode());
          bi.setSenddocName(gp.getFileName());
        }

        // 指标流向
        if (gp.getBtCode() != null) {
          bi.setBiTargetCode(gp.getBtCode());
        }
        // 预算项目
        if (gp.getBisCode() != null) {
          bi.setProjectCode(gp.getBisCode());
          bi.setProjectName(gp.getBisName());
        }
        //是否监督使用
        bi.setBtName(gp.getBtName());
        //是否政府采购
        bi.setGbName(gp.getGbName());

        if (getDto.getZcText3() != null && !"".equals(getDto.getZcText3())) {
          getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + sumId + "'", "").replaceAll("'" + sumId + "',", "")
            .replaceAll("'" + sumId + "'", ""));
        }
        if (getDto.getZcText3() == null || "".equals(getDto.getZcText3())) {
          getDto.setZcText3("'" + gp.getSumId() + "'");
        } else {
          getDto.setZcText3(getDto.getZcText3() + ",'" + gp.getSumId() + "'");
        }

        editPanel.setEditingObject(zjzj);

      }else if (obj instanceof ZcYearPlan) {
          ZcYearPlan yp = (ZcYearPlan) obj;
          ZcPProMake make=yp.getNewMake();
          ZcPProMitemBi bi = (ZcPProMitemBi) make.getBiList().get(k2);

          VwBudgetGp gp = (VwBudgetGp) selectedDatas.get(0);
          String sumId = "";
          if (bi.getZcBiNo() != null) {
            sumId = bi.getZcBiNo();
          }

          bi.setZcBiNo(gp.getSumId() + "");
          bi.setZcBiDoSum(gp.getCanuseMoney());
          bi.setZcBiSum(gp.getBudgetMoney());
          // 预算单位
          if (gp.getEnCode() != null) {
            bi.setCoCode(gp.getEnCode());
            bi.setCoName(gp.getEnName());
          }
          // 资金性质
          if (gp.getMkCode() != null) {
            bi.setFundCode(gp.getMkId());
            bi.setFundName(gp.getMkName());
          }
          // 功能分类
          if (gp.getBsCode() != null) {
            bi.setbAccCode(gp.getBsCode());
            bi.setbAccName(gp.getBsCode()+gp.getBsName());
          }
          // 经济分类
          if (gp.getBsiCode() != null) {
            bi.setOutlayCode(gp.getBsiId());
            bi.setOutlayName(gp.getBsiCode()+gp.getBsiName());
          }
          // 项目类别
          if (gp.getBiCode() != null) {
            bi.setProjectTypeCode(gp.getBiCode());
            bi.setProjectTypeName(gp.getBiName());
          }
          // 付款方式
          if (gp.getPkCode() != null) {
            bi.setPaytypeCode(gp.getPkCode());
            bi.setPaytypeName(gp.getPkName());
          }
          // 指标来源
          if (gp.getBlCode() != null) {
            bi.setOriginCode(gp.getBlCode());
            bi.setOriginName(gp.getBlName());
          }
          // 业务处室
          if (gp.getMbId() != null) {
            bi.setOrgCode(gp.getMbId());
            bi.setOrgName(gp.getMbName());
          }
          // 年度
          bi.setNd(gp.getSetYear() + "");
          // 文号
          if (gp.getFileCode() != null) {
            bi.setSenddocCode(gp.getFileCode());
            bi.setSenddocName(gp.getFileName());
          }

          // 指标流向
          if (gp.getBtCode() != null) {
            bi.setBiTargetCode(gp.getBtCode());
          }
          // 预算项目
          if (gp.getBisCode() != null) {
            bi.setProjectCode(gp.getBisCode());
            bi.setProjectName(gp.getBisName());
          }
          //是否监督使用
          bi.setBtName(gp.getBtName());
          //是否政府采购
          bi.setGbName(gp.getGbName());

          if (getDto.getZcText3() != null && !"".equals(getDto.getZcText3())) {
            getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + sumId + "'", "").replaceAll("'" + sumId + "',", "")
              .replaceAll("'" + sumId + "'", ""));
          }
          if (getDto.getZcText3() == null || "".equals(getDto.getZcText3())) {
            getDto.setZcText3("'" + gp.getSumId() + "'");
          } else {
            getDto.setZcText3(getDto.getZcText3() + ",'" + gp.getSumId() + "'");
          }

          editPanel.setEditingObject(yp);

        } else if (obj instanceof ZcXmcgHt) {//只适合补充合同时，占用指标，正常合同从采购计划里带来指标情况
        //待编写
        ZcXmcgHt subHt = (ZcXmcgHt) obj;
        ZcXmcgHtBi bi = (ZcXmcgHtBi) subHt.getBiList().get(k2);

        VwBudgetGp gp = (VwBudgetGp) selectedDatas.get(0);
        String sumId = "";
        if (bi.getZcBiNo() != null) {
          sumId = bi.getZcBiNo();
        }

        bi.setZcBiNo(gp.getSumId() + "");
        bi.setZcBiDoSum(gp.getCanuseMoney());
        bi.setZcBiSum(gp.getBudgetMoney());
        // 预算单位
        if (gp.getEnCode() != null) {
          bi.setCoCode(gp.getEnCode());
          bi.setCoName(gp.getEnName());
        }
        // 资金性质
        if (gp.getMkCode() != null) {
          bi.setFundCode(gp.getMkId());
          bi.setFundName(gp.getMkName());
        }
        // 功能分类
        if (gp.getBsCode() != null) {
          bi.setbAccCode(gp.getBsCode());
          bi.setbAccName(gp.getBsCode()+gp.getBsName());
        }
        // 经济分类
        if (gp.getBsiCode() != null) {
          bi.setOutlayCode(gp.getBsiId());
          bi.setOutlayName(gp.getBsiCode()+gp.getBsiName());
        }
        // 项目类别
        if (gp.getBiCode() != null) {
          bi.setProjectTypeCode(gp.getBiCode());
          bi.setProjectTypeName(gp.getBiName());
        }
        // 付款方式
        if (gp.getPkCode() != null) {
          bi.setPaytypeCode(gp.getPkCode());
          bi.setPaytypeName(gp.getPkName());
        }
        // 指标来源
        if (gp.getBlCode() != null) {
          bi.setOriginCode(gp.getBlCode());
          bi.setOriginName(gp.getBlName());
        }
        // 业务处室
        if (gp.getMbId() != null) {
          bi.setOrgCode(gp.getMbId());
          bi.setOrgName(gp.getMbName());
        }
        // 年度
        bi.setNd(gp.getSetYear() + "");
        // 文号
        if (gp.getFileCode() != null) {
          bi.setSenddocCode(gp.getFileCode());
          bi.setSenddocName(gp.getFileName());
        }

        // 指标流向
        if (gp.getBtCode() != null) {
          bi.setBiTargetCode(gp.getBtCode());
        }
        // 预算项目
        if (gp.getBisCode() != null) {
          bi.setProjectCode(gp.getBisCode());
          bi.setProjectName(gp.getBisName());
        }
        //是否监督使用
        bi.setBtName(gp.getBtName());
        //是否政府采购
        bi.setGbName(gp.getGbName());

        if (getDto.getZcText3() != null && !"".equals(getDto.getZcText3())) {
          getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + sumId + "'", "").replaceAll("'" + sumId + "',", "")
            .replaceAll("'" + sumId + "'", ""));
        }
        if (getDto.getZcText3() == null || "".equals(getDto.getZcText3())) {
          getDto.setZcText3("'" + gp.getSumId() + "'");
        } else {
          getDto.setZcText3(getDto.getZcText3() + ",'" + gp.getSumId() + "'");
        }

        editPanel.setEditingObject(subHt);
        
      } else if (obj instanceof ZcQx) {

        ZcQx qx = (ZcQx) obj;
        ZcQxBi bi = (ZcQxBi) qx.getBiList().get(k2);

        VwBudgetGp gp = (VwBudgetGp) selectedDatas.get(0);
        String sumId = "";
        if (bi.getZcBiNo() != null) {
          sumId = bi.getZcBiNo();
        }

        bi.setZcBiNo(gp.getSumId() + "");
        bi.setZcBiDoSum(gp.getCanuseMoney());
        bi.setZcBiSum(gp.getBudgetMoney());
        // 预算单位
        if (gp.getEnCode() != null) {
          bi.setCoCode(gp.getEnCode());
          bi.setCoName(gp.getEnName());
        }
        // 资金性质
        if (gp.getMkCode() != null) {
          bi.setFundCode(gp.getMkId());
          bi.setFundName(gp.getMkName());
        }
        // 功能分类
        if (gp.getBsCode() != null) {
          bi.setbAccCode(gp.getBsCode());
          bi.setbAccName(gp.getBsCode()+gp.getBsName());
        }
        // 经济分类
        if (gp.getBisCode() != null) {
          bi.setOutlayCode(gp.getBsiId());
          bi.setOutlayName(gp.getBsiCode()+gp.getBsiName());
        }
        // 项目类别
        if (gp.getBiCode() != null) {
          bi.setProjectTypeCode(gp.getBiCode());
          bi.setProjectTypeName(gp.getBiName());
        }
        // 付款方式
        if (gp.getPkCode() != null) {
          bi.setPaytypeCode(gp.getPkCode());
          bi.setPaytypeName(gp.getPkName());
        }
        // 指标来源
        if (gp.getBlCode() != null) {
          bi.setOriginCode(gp.getBlCode());
          bi.setOriginName(gp.getBlName());
        }
        // 业务处室
        if (gp.getMbId() != null) {
          bi.setOrgCode(gp.getMbId());
          bi.setOrgName(gp.getMbName());
        }
        // 年度
        bi.setNd(gp.getSetYear());
        // 文号
        if (gp.getFileCode() != null) {
          bi.setSenddocCode(gp.getFileCode());
          bi.setSenddocName(gp.getFileName());
        }

        // 指标流向
        if (gp.getBtCode() != null) {
          bi.setBiTargetCode(gp.getBtCode());
        }
        // 预算项目
        if (gp.getBisCode() != null) {
          bi.setProjectCode(gp.getBisCode());
          bi.setProjectName(gp.getBisName());
        }
        //是否监督使用
        bi.setBtName(gp.getBtName());
        //是否政府采购
        bi.setGbName(gp.getGbName());
        
        bi.setZcZjType(ZcQxBi.TYPE_CZYS);

        if (getDto.getZcText3() != null && !"".equals(getDto.getZcText3())) {
          getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + sumId + "'", "").replaceAll("'" + sumId + "',", "")
            .replaceAll("'" + sumId + "'", ""));
        }
        if (getDto.getZcText3() == null || "".equals(getDto.getZcText3())) {
          getDto.setZcText3("'" + gp.getSumId() + "'");
        } else {
          getDto.setZcText3(getDto.getZcText3() + ",'" + gp.getSumId() + "'");
        }

        editPanel.setEditingObject(qx);
      }else if (obj instanceof ZcQb) {

        ZcQb qb = (ZcQb) obj;
        ZcQbBi bi = (ZcQbBi) qb.getBiList().get(k2);

        VwBudgetGp gp = (VwBudgetGp) selectedDatas.get(0);
        String sumId = "";
        if (bi.getZcBiNo() != null) {
          sumId = bi.getZcBiNo();
        }

        bi.setZcBiNo(gp.getSumId() + "");
        bi.setZcBiDoSum(gp.getCanuseMoney());
        bi.setZcBiSum(gp.getBudgetMoney());
        // 预算单位
        if (gp.getEnCode() != null) {
          bi.setCoCode(gp.getEnCode());
          bi.setCoName(gp.getEnName());
        }
        // 资金性质
        if (gp.getMkCode() != null) {
          bi.setFundCode(gp.getMkId());
          bi.setFundName(gp.getMkName());
        }
        // 功能分类
        if (gp.getBsCode() != null) {
          bi.setbAccCode(gp.getBsCode());
          bi.setbAccName(gp.getBsCode()+gp.getBsName());
        }
        // 经济分类
        if (gp.getBisCode() != null) {
          bi.setOutlayCode(gp.getBsiId());
          bi.setOutlayName(gp.getBsiCode()+gp.getBsiName());
        }
        // 项目类别
        if (gp.getBiCode() != null) {
          bi.setProjectTypeCode(gp.getBiCode());
          bi.setProjectTypeName(gp.getBiName());
        }
        // 付款方式
        if (gp.getPkCode() != null) {
          bi.setPaytypeCode(gp.getPkCode());
          bi.setPaytypeName(gp.getPkName());
        }
        // 指标来源
        if (gp.getBlCode() != null) {
          bi.setOriginCode(gp.getBlCode());
          bi.setOriginName(gp.getBlName());
        }
        // 业务处室
        if (gp.getMbId() != null) {
          bi.setOrgCode(gp.getMbId());
          bi.setOrgName(gp.getMbName());
        }
        // 年度
        bi.setNd(gp.getSetYear());
        // 文号
        if (gp.getFileCode() != null) {
          bi.setSenddocCode(gp.getFileCode());
          bi.setSenddocName(gp.getFileName());
        }

        // 指标流向
        if (gp.getBtCode() != null) {
          bi.setBiTargetCode(gp.getBtCode());
        }
        // 预算项目
        if (gp.getBisCode() != null) {
          bi.setProjectCode(gp.getBisCode());
          bi.setProjectName(gp.getBisName());
        }
        //是否监督使用
        bi.setBtName(gp.getBtName());
        //是否政府采购
        bi.setGbName(gp.getGbName());
        
        bi.setZcZjType(ZcQbBi.TYPE_CZYS);

        if (getDto.getZcText3() != null && !"".equals(getDto.getZcText3())) {
          getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + sumId + "'", "").replaceAll("'" + sumId + "',", "")
            .replaceAll("'" + sumId + "'", ""));
        }
        if (getDto.getZcText3() == null || "".equals(getDto.getZcText3())) {
          getDto.setZcText3("'" + gp.getSumId() + "'");
        } else {
          getDto.setZcText3(getDto.getZcText3() + ",'" + gp.getSumId() + "'");
        }

        editPanel.setEditingObject(qb);
      } else if (obj instanceof ZcDingdian) {

        ZcDingdian dingdian = (ZcDingdian) obj;
        ZcDingdianBi bi = (ZcDingdianBi) dingdian.getBiList().get(k2);

        VwBudgetGp gp = (VwBudgetGp) selectedDatas.get(0);
        String sumId = "";
        if (bi.getZcBiNo() != null) {
          sumId = bi.getZcBiNo();
        }

        bi.setZcBiNo(gp.getSumId() + "");
        bi.setZcBiDoSum(gp.getCanuseMoney());
        bi.setZcBiSum(gp.getBudgetMoney());
        // 预算单位
        if (gp.getEnCode() != null) {
          bi.setCoCode(gp.getEnCode());
          bi.setCoName(gp.getEnName());
        }
        // 资金性质
        if (gp.getMkCode() != null) {
          bi.setFundCode(gp.getMkId());
          bi.setFundName(gp.getMkName());
        }
        // 功能分类
        if (gp.getBsCode() != null) {
          bi.setbAccCode(gp.getBsCode());
          bi.setbAccName(gp.getBsCode()+gp.getBsName());
        }
        // 经济分类
        if (gp.getBisCode() != null) {
          bi.setOutlayCode(gp.getBsiId());
          bi.setOutlayName(gp.getBsiCode()+gp.getBsiName());
        }
        // 项目类别
        if (gp.getBiCode() != null) {
          bi.setProjectTypeCode(gp.getBiCode());
          bi.setProjectTypeName(gp.getBiName());
        }
        // 付款方式
        if (gp.getPkCode() != null) {
          bi.setPaytypeCode(gp.getPkCode());
          bi.setPaytypeName(gp.getPkName());
        }
        // 指标来源
        if (gp.getBlCode() != null) {
          bi.setOriginCode(gp.getBlCode());
          bi.setOriginName(gp.getBlName());
        }
        // 业务处室
        if (gp.getMbId() != null) {
          bi.setOrgCode(gp.getMbId());
          bi.setOrgName(gp.getMbName());
        }
        // 年度
        bi.setNd(gp.getSetYear());
        // 文号
        if (gp.getFileCode() != null) {
          bi.setSenddocCode(gp.getFileCode());
          bi.setSenddocName(gp.getFileName());
        }

        // 指标流向
        if (gp.getBtCode() != null) {
          bi.setBiTargetCode(gp.getBtCode());
        }
        // 预算项目
        if (gp.getBisCode() != null) {
          bi.setProjectCode(gp.getBisCode());
          bi.setProjectName(gp.getBisName());
        }
        //是否监督使用
        bi.setBtName(gp.getBtName());
        //是否政府采购
        bi.setGbName(gp.getGbName());
        
        bi.setZcZjType(ZcQxBi.TYPE_CZYS);

        if (getDto.getZcText3() != null && !"".equals(getDto.getZcText3())) {
          getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + sumId + "'", "").replaceAll("'" + sumId + "',", "")
            .replaceAll("'" + sumId + "'", ""));
        }
        if (getDto.getZcText3() == null || "".equals(getDto.getZcText3())) {
          getDto.setZcText3("'" + gp.getSumId() + "'");
        } else {
          getDto.setZcText3(getDto.getZcText3() + ",'" + gp.getSumId() + "'");
        }

        editPanel.setEditingObject(dingdian);
      }
    }

  }

  public void afterClear() {
    if (!editPanel.budgetAfterClear()) {
      return;
    }

    JTable table = biTablePanel.getTable();

    BeanTableModel model = (BeanTableModel) table.getModel();

    int k = table.getSelectedRow();

    if (k < 0)

      return;

    int k2 = table.convertRowIndexToModel(k);
    Object obj = (Object) listCursor.getCurrentObject();
    if (obj instanceof ZcPProMake) {
      ZcPProMake make = (ZcPProMake)obj;
      ZcPProMitemBi bi = (ZcPProMitemBi) make.getBiList().get(k2);
      if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
        getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + bi.getZcBiNo() + "'", "").replaceAll("'" + bi.getZcBiNo() + "',", "")
          .replaceAll("'" + bi.getZcBiNo() + "'", ""));
      }
      bi.setZcBiNo("");
      bi.setZcBiDoSum(null);
      bi.setZcBiSum(null);
      // 预算单位
      bi.setCoCode("");
      bi.setCoName("");
      // 资金性质
      bi.setFundCode("0");
      bi.setFundName("自筹资金");
      // 功能分类
      bi.setbAccCode("");
      bi.setbAccName("");
      //经济分类
      bi.setOutlayCode("");
      bi.setOutlayName("");
      // 项目类别
      bi.setProjectTypeCode("");
      bi.setProjectTypeName("");
      // 付款方式
      bi.setPaytypeCode("A");
      bi.setPaytypeName("");
      // 指标来源
      bi.setOriginCode("99");
      bi.setOriginName("自筹资金"); 
      //预算项目
      bi.setProjectCode("");
      bi.setProjectName("");
      //是否监督使用
      bi.setBtName("");
      //是否政府采购
      bi.setGbName("");
      // 年度
      bi.setNd("");
      // 文号
      bi.setSenddocCode("");
      bi.setSenddocName("");
      // 业务处室
      bi.setOrgCode("");
      bi.setOrgName("");

      editPanel.setEditingObject(make);
    }else if(obj instanceof ZcXmcgHt){

      ZcXmcgHt ht = (ZcXmcgHt)obj;
      ZcXmcgHtBi bi = (ZcXmcgHtBi) ht.getBiList().get(k2);
      if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
        getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + bi.getZcBiNo() + "'", "").replaceAll("'" + bi.getZcBiNo() + "',", "")
          .replaceAll("'" + bi.getZcBiNo() + "'", ""));
      }
      bi.setZcBiNo("");
      bi.setZcBiDoSum(null);
      bi.setZcBiSum(null);
      // 预算单位
      bi.setCoCode("");
      bi.setCoName("");
      // 资金性质
      bi.setFundCode("0");
      bi.setFundName("自筹资金");
      // 功能分类
      bi.setbAccCode("");
      bi.setbAccName("");
      //经济分类
      bi.setOutlayCode("");
      bi.setOutlayName("");
      // 项目类别
      bi.setProjectTypeCode("");
      bi.setProjectTypeName("");
      // 付款方式
      bi.setPaytypeCode("A");
      bi.setPaytypeName("");
      // 指标来源
      bi.setOriginCode("99");
      bi.setOriginName("自筹资金");
      //预算项目
      bi.setProjectCode("");
      bi.setProjectName("");
      //是否监督使用
      bi.setBtName("");
      //是否政府采购
      bi.setGbName("");
      // 年度
      bi.setNd("");
      // 文号
      bi.setSenddocCode("");
      bi.setSenddocName("");
      // 业务处室
      bi.setOrgCode("");
      bi.setOrgName("");

      editPanel.setEditingObject(ht);
    }else if(obj instanceof ZcQx){
      ZcQx qx = (ZcQx)obj;
      ZcQxBi bi = (ZcQxBi) qx.getBiList().get(k2);
      if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
        getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + bi.getZcBiNo() + "'", "").replaceAll("'" + bi.getZcBiNo() + "',", "")
          .replaceAll("'" + bi.getZcBiNo() + "'", ""));
      }
      bi.setZcBiNo("");
      bi.setZcBiDoSum(null);
      bi.setZcBiSum(null);
      // 预算单位
      bi.setCoCode("");
      bi.setCoName("");
      // 资金性质
      bi.setFundCode("0");
      bi.setFundName("自筹资金");
      // 功能分类
      bi.setbAccCode("");
      bi.setbAccName("");
      //经济分类
      bi.setOutlayCode("");
      bi.setOutlayName("");
      // 项目类别
      bi.setProjectTypeCode("");
      bi.setProjectTypeName("");
      // 付款方式
      bi.setPaytypeCode("A");
      bi.setPaytypeName("");
      // 指标来源
      bi.setOriginCode("99");
      bi.setOriginName("自筹资金");
      //预算项目
      bi.setProjectCode("");
      bi.setProjectName("");
      // 年度
      bi.setNd(0);
      // 文号
      bi.setSenddocCode("");
      bi.setSenddocName("");
      // 业务处室
      bi.setOrgCode("");
      bi.setOrgName("");
      
      bi.setZcZjType(ZcQxBi.TYPE_ZCZJ);

      editPanel.setEditingObject(qx);
      
    }else if(obj instanceof ZcQb){
      ZcQb qx = (ZcQb)obj;
      ZcQxBi bi = (ZcQxBi) qx.getBiList().get(k2);
      if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
        getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + bi.getZcBiNo() + "'", "").replaceAll("'" + bi.getZcBiNo() + "',", "")
          .replaceAll("'" + bi.getZcBiNo() + "'", ""));
      }
      bi.setZcBiNo("");
      bi.setZcBiDoSum(null);
      bi.setZcBiSum(null);
      // 预算单位
      bi.setCoCode("");
      bi.setCoName("");
      // 资金性质
      bi.setFundCode("0");
      bi.setFundName("自筹资金");
      // 功能分类
      bi.setbAccCode("");
      bi.setbAccName("");
      //经济分类
      bi.setOutlayCode("");
      bi.setOutlayName("");
      // 项目类别
      bi.setProjectTypeCode("");
      bi.setProjectTypeName("");
      // 付款方式
      bi.setPaytypeCode("A");
      bi.setPaytypeName("");
      // 指标来源
      bi.setOriginCode("99");
      bi.setOriginName("自筹资金");
      //预算项目
      bi.setProjectCode("");
      bi.setProjectName("");
      // 年度
      bi.setNd(0);
      // 文号
      bi.setSenddocCode("");
      bi.setSenddocName("");
      // 业务处室
      bi.setOrgCode("");
      bi.setOrgName("");
      
      bi.setZcZjType(ZcQbBi.TYPE_ZCZJ);

      editPanel.setEditingObject(qx);
      
    }

  }

  public TableModel createTableModel(List showDatas) {

    Object data[][] = new Object[showDatas.size()][columNames.length];

    for (int i = 0; i < showDatas.size(); i++) {

      VwBudgetGp rowData = (VwBudgetGp) showDatas.get(i);

      int col = 0;
//      String colNames[] = { "指标余额表ID", "指标来源", "发文文号", "资金性质","采购项目", "功能分类", "经济分类","释放监督使用","释放政府采购","指标总金额","指标可用金额" };
//      String colNames[] = { "指标余额表ID", "指标来源", "发文文号", "资金性质","采购项目", "功能分类", "经济分类","释放监督使用","释放政府采购","指标总金额","指标可用金额" };
      data[i][col++] = rowData.getSumId();
      data[i][col++] = rowData.getBlName();
      data[i][col++] = rowData.getFileName();
      data[i][col++] = rowData.getMkName();
      data[i][col++] = rowData.getBisName();
      data[i][col++] = rowData.getBsCode()+rowData.getBsName();
      data[i][col++] = rowData.getBsiCode()+rowData.getBsiName();
      data[i][col++] = rowData.getBtName();
      data[i][col++] = rowData.getGbName();
      data[i][col++] = rowData.getBudgetMoney();
      data[i][col++] = rowData.getCanuseMoney();

    }

    MyTableModel model = new MyTableModel(data, columNames) {

      public boolean isCellEditable(int row, int colum) {

        return false;

      }

      public Class getColumnClass(int column) {

        if ((column >= 0) && (column < getColumnCount()) && this.getRowCount() > 0) {

          for (int row = 0; row < this.getRowCount(); row++) {

            if (getValueAt(row, column) != null) {

              return getValueAt(row, column).getClass();

            }

          }

        }

        return Object.class;

      }

    };

    return model;

  }

  public boolean isMultipleSelect() {

    return false;

  }

}
