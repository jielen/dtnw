package com.ufgov.zc.client.zc.zcebentrust.operation;

import java.util.List;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.publish.IZcEbEntrustBulletinServiceDelegate;

/**

 * @ClassName: ZcEbEntrustBulletinOpt

 * @Description: 预算单位采购任务业务操作类

 * @date: Sep 17, 2012 2:15:38 PM

 * @version: V1.0

 * @since: 1.0

 * @author: yuzz

 * @modify:

 */
public class ZcEbEntrustBulletinOpt {
	
	public IZcEbEntrustBulletinServiceDelegate bulletinServiceDelegate = (IZcEbEntrustBulletinServiceDelegate) ServiceFactory.create(
			IZcEbEntrustBulletinServiceDelegate.class, "zcEbEntrustBulletinServiceDelegate");
	
	/**
	 * <p> 获取已下达的采购任务 </p>
	 * @param dto
	 * @param meta
	 * @return List
	 * @author yuzz
	 * @since Sep 17, 2012 2:16:25 PM
	 */
	public List getZcEbEntrustBull(ElementConditionDto dto, RequestMeta meta){
		return bulletinServiceDelegate.getZcEbEntrustBull(dto, meta);
	}
	
	/**
	 * <p> 获取已上网的采购任务 </p>
	 * @param dto
	 * @param meta
	 * @return List
	 * @author yuzz
	 * @since Sep 17, 2012 2:16:44 PM
	 */
	public List getZcEbEntrustBullin(ElementConditionDto dto, RequestMeta meta){
		return bulletinServiceDelegate.getZcEbEntrustBullin(dto, meta);
	}
	
	/**
	 * <p> 获取已完成的采购任务 </p>
	 * @param dto
	 * @param meta
	 * @return List
	 * @author yuzz
	 * @since Sep 17, 2012 2:16:47 PM
	 */
	public List getZcEbEntrustReport(ElementConditionDto dto, RequestMeta meta){
		return bulletinServiceDelegate.getZcEbEntrustReport(dto, meta);
	}

}
