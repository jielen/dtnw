package com.ufgov.zc.common.zc.publish;

import java.util.List;
import java.util.Map;

import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcEbJieXiang;

public interface IZcEbJieXiangServiceDelegate extends IZcEbAuditSheetServiceDelegate {

  public List getList(ElementConditionDto elementConditionDto, RequestMeta requestMeta);

  public ZcEbJieXiang readJieXiang(Map para, RequestMeta requestMeta);

  public void cancelCaiGou(ZcEbJieXiang jiexiang, RequestMeta requestMeta);

  public void unFinishCaiGou(ZcEbJieXiang jiexiang, RequestMeta requestMeta);

  public void finishCaiGou(ZcEbJieXiang jiexiang, RequestMeta requestMeta);
}
