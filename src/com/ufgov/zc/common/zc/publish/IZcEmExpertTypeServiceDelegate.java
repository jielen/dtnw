package com.ufgov.zc.common.zc.publish;import java.util.List;import java.util.Map;import com.ufgov.zc.common.system.Publishable;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.model.EmExpertType;public interface IZcEmExpertTypeServiceDelegate extends Publishable {  public List getList(ElementConditionDto elementConditionDto, RequestMeta requestMeta);  public EmExpertType getModel(Map map, RequestMeta requestMeta);  public EmExpertType saveFN(EmExpertType o, RequestMeta requestMeta);  public void deleteFN(Map m, RequestMeta requestMeta);}