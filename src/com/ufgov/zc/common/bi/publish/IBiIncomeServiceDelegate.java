package com.ufgov.zc.common.bi.publish;import java.util.List;import com.ufgov.zc.common.bi.model.BiIncome;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.dto.ElementConditionDto;public interface IBiIncomeServiceDelegate {  List getBiIncome(ElementConditionDto dto, RequestMeta requestMeta);  String genBiIncomeId(BiIncome biIncome, RequestMeta requestMeta);  void saveBiIncomeFN(List insertList, List updateList, RequestMeta requestMeta);  void deleteBiIncomeFN(List biIncomeIdList, RequestMeta requestMeta);  void auditBiIncomeFN(List biIncomeList, RequestMeta requestMeta);  void unAuditBiIncomeFN(List biIncomeList, RequestMeta requestMeta);}