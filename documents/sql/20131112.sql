----------------2013-11-06-----------------------
--1.
insert into as_val (VALSET_ID, VAL_ID, VAL, ORD_INDEX, LSTDATE, IS_SYSTEM, TRANS_DATE)
values ('ZC_VS_MAKE_STATUS', '2', '采购单位审核', 2, null, 'N', to_date('06-11-2013 16:57:45', 'dd-mm-yyyy hh24:mi:ss'));

----------------2013-11-07-----------------------
--1.
insert into as_option (OPT_ID, CO_CODE, COMPO_ID, TRANS_TYPE, OPT_VAL, IS_SYST_OPT)
values ('OPT_SUPPLIER_CO_CODE', '*', '*', '*', '000', 'Y');
insert into as_option (OPT_ID, CO_CODE, COMPO_ID, TRANS_TYPE, OPT_VAL, IS_SYST_OPT)
values ('OPT_SUPPLIER_POSITION_ID', '*', '*', '*', 'GYS_NORMAL', 'Y');

----------------2013-11-11-----------------------
--1.
insert into as_option (OPT_ID, CO_CODE, COMPO_ID, TRANS_TYPE, OPT_VAL, IS_SYST_OPT)
values ('OPT_ZC_AUDIT_SUPPLIER_BY_WORK_FLOW', '*', '*', '*', 'Y', 'Y');
--2.
insert into as_val (VALSET_ID, VAL_ID, VAL, ORD_INDEX, LSTDATE, IS_SYSTEM, TRANS_DATE)
values ('ZC_VS_SUPPLIER_STATUS', '4', '变更待审核', 3, null, 'N', to_date('11-11-2013 02:34:44', 'dd-mm-yyyy hh24:mi:ss'));

--3.
insert into as_compo_func (COMPO_ID, FUNC_ID, IS_WR_LOG, IS_NEVER_USE, TRANS_DATE)
values ('ZC_EB_SUPPLIER', 'fnewcommit', 'Y', null, to_date('20-06-2013 22:56:15', 'dd-mm-yyyy hh24:mi:ss'));

--4.
delete from zc_search_condition s where s.compo_id='ZC_EB_SUPPLIER';

insert into zc_search_condition (CONDITION_ID, CONDITION_FIELD_CODE, CONDITION_FIELD_NAME, CONDITION_FIELD_ORDER, COMPO_ID, COMPO_NAME, CONDITION_NAME, CONDITION_TYPE, CONDITION_NAME_ORDER)
values ('ZcEbSupplier_supplierTab', 'all', '查看全部', 4, 'ZC_EB_SUPPLIER', null, '供应商管理页签', 'tab', '301');

insert into zc_search_condition (CONDITION_ID, CONDITION_FIELD_CODE, CONDITION_FIELD_NAME, CONDITION_FIELD_ORDER, COMPO_ID, COMPO_NAME, CONDITION_NAME, CONDITION_TYPE, CONDITION_NAME_ORDER)
values ('ZcEbSupplier_supplierTab', 'done', '已启用', 2, 'ZC_EB_SUPPLIER', null, '供应商管理页签', 'tab', '301');

insert into zc_search_condition (CONDITION_ID, CONDITION_FIELD_CODE, CONDITION_FIELD_NAME, CONDITION_FIELD_ORDER, COMPO_ID, COMPO_NAME, CONDITION_NAME, CONDITION_TYPE, CONDITION_NAME_ORDER)
values ('ZcEbSupplier_supplierTab', 'freeze', '已冻结', 3, 'ZC_EB_SUPPLIER', null, '供应商管理页签', 'tab', '301');

insert into zc_search_condition (CONDITION_ID, CONDITION_FIELD_CODE, CONDITION_FIELD_NAME, CONDITION_FIELD_ORDER, COMPO_ID, COMPO_NAME, CONDITION_NAME, CONDITION_TYPE, CONDITION_NAME_ORDER)
values ('ZcEbSupplier_supplierTab', 'todo', '新增', 0, 'ZC_EB_SUPPLIER', null, '供应商管理页签', 'tab', '301');

insert into zc_search_condition (CONDITION_ID, CONDITION_FIELD_CODE, CONDITION_FIELD_NAME, CONDITION_FIELD_ORDER, COMPO_ID, COMPO_NAME, CONDITION_NAME, CONDITION_TYPE, CONDITION_NAME_ORDER)
values ('ZcEbSupplier_supplier', 'PROVIDER_NAME', '供应商', 2, 'ZC_EB_SUPPLIER', null, '供应  商查询条件', 'condition', '301');

insert into zc_search_condition (CONDITION_ID, CONDITION_FIELD_CODE, CONDITION_FIELD_NAME, CONDITION_FIELD_ORDER, COMPO_ID, COMPO_NAME, CONDITION_NAME, CONDITION_TYPE, CONDITION_NAME_ORDER)
values ('ZcEbSupplier_supplierTab', 'updating', '变更待审核', 1, 'ZC_EB_SUPPLIER', null, '供应商管理页签', 'tab', '301');

--5.
delete from as_val v where v.valset_id='ZC_SUPPLIER_TYPE';

insert into as_val (VALSET_ID, VAL_ID, VAL, ORD_INDEX, LSTDATE, IS_SYSTEM, TRANS_DATE)
values ('ZC_SUPPLIER_TYPE', '01', '国内', 3, null, 'Y', to_date('11-11-2013 13:50:19', 'dd-mm-yyyy hh24:mi:ss'));

insert into as_val (VALSET_ID, VAL_ID, VAL, ORD_INDEX, LSTDATE, IS_SYSTEM, TRANS_DATE)
values ('ZC_SUPPLIER_TYPE', '02', '市内', 1, null, 'Y', to_date('11-11-2013 13:50:19', 'dd-mm-yyyy hh24:mi:ss'));

insert into as_val (VALSET_ID, VAL_ID, VAL, ORD_INDEX, LSTDATE, IS_SYSTEM, TRANS_DATE)
values ('ZC_SUPPLIER_TYPE', '03', '省内', 2, null, 'Y', to_date('11-11-2013 13:50:19', 'dd-mm-yyyy hh24:mi:ss'));

--6.
delete from as_val v where v.valset_id='ZC_VS_SUPPLIER_TYPE';

insert into as_val (VALSET_ID, VAL_ID, VAL, ORD_INDEX, LSTDATE, IS_SYSTEM, TRANS_DATE)
values ('ZC_VS_SUPPLIER_TYPE', 'gys_normal', '普通投标', 0, null, 'N', to_date('11-11-2013 14:56:56', 'dd-mm-yyyy hh24:mi:ss'));

insert into as_val (VALSET_ID, VAL_ID, VAL, ORD_INDEX, LSTDATE, IS_SYSTEM, TRANS_DATE)
values ('ZC_VS_SUPPLIER_TYPE', 'gys_xunjiatoubiao', '询价投标', 1, null, 'N', to_date('11-11-2013 14:56:56', 'dd-mm-yyyy hh24:mi:ss'));

insert into as_val (VALSET_ID, VAL_ID, VAL, ORD_INDEX, LSTDATE, IS_SYSTEM, TRANS_DATE)
values ('ZC_VS_SUPPLIER_TYPE', 'gys_qiche_weixiu', '车辆维修', 2, null, 'N', to_date('11-11-2013 14:56:56', 'dd-mm-yyyy hh24:mi:ss'));

insert into as_val (VALSET_ID, VAL_ID, VAL, ORD_INDEX, LSTDATE, IS_SYSTEM, TRANS_DATE)
values ('ZC_VS_SUPPLIER_TYPE', 'gys_qiche_baoxian', '汽车保险', 3, null, 'N', to_date('11-11-2013 14:56:56', 'dd-mm-yyyy hh24:mi:ss'));

--7.
-- Create table
create table ZC_B_SUPPLIER_TYPE
(
  zc_su_code VARCHAR2(60),
  type_code  VARCHAR2(60),
  type_name  VARCHAR2(60)
)
;
-- Add comments to the table 
comment on table ZC_B_SUPPLIER_TYPE
  is '供应商类别';

--8.

DELETE from as_role_func r where r.role_id='gys_normal' and r.compo_id='ZC_EB_QX';

DELETE from as_role_func r where r.role_id='gys_normal' and r.compo_id='ZC_EB_QB';

--9.
insert into as_lang_trans (RES_ID, LANG_ID, RES_NA)
values ('typeName', 'C', '供应商类型');

insert into as_lang_trans (RES_ID, LANG_ID, RES_NA)
values ('isSelected', 'C', '选中');

--10.

delete from as_role_func r where r.role_id='gys_normal' and r.compo_id='ZC_EB_XUNJIA_BAOJIA';

insert into as_role_func (ROLE_ID, COMPO_ID, FUNC_ID, TRANS_DATE)
values ('gys_xunjiatoubiao', 'ZC_EB_XUNJIA_BAOJIA', 'fcallback', to_date('06-11-2010 17:50:03', 'dd-mm-yyyy hh24:mi:ss'));

insert into as_role_func (ROLE_ID, COMPO_ID, FUNC_ID, TRANS_DATE)
values ('gys_xunjiatoubiao', 'ZC_EB_XUNJIA_BAOJIA', 'fdelete', to_date('06-11-2010 17:50:04', 'dd-mm-yyyy hh24:mi:ss'));

insert into as_role_func (ROLE_ID, COMPO_ID, FUNC_ID, TRANS_DATE)
values ('gys_xunjiatoubiao', 'ZC_EB_XUNJIA_BAOJIA', 'fedit', to_date('06-11-2010 17:50:04', 'dd-mm-yyyy hh24:mi:ss'));

insert into as_role_func (ROLE_ID, COMPO_ID, FUNC_ID, TRANS_DATE)
values ('gys_xunjiatoubiao', 'ZC_EB_XUNJIA_BAOJIA', 'fnew', to_date('06-11-2010 17:50:04', 'dd-mm-yyyy hh24:mi:ss'));

insert into as_role_func (ROLE_ID, COMPO_ID, FUNC_ID, TRANS_DATE)
values ('gys_xunjiatoubiao', 'ZC_EB_XUNJIA_BAOJIA', 'fquote', to_date('06-11-2010 17:50:04', 'dd-mm-yyyy hh24:mi:ss'));

insert into as_role_func (ROLE_ID, COMPO_ID, FUNC_ID, TRANS_DATE)
values ('gys_xunjiatoubiao', 'ZC_EB_XUNJIA_BAOJIA', 'fsave', to_date('06-11-2010 17:50:04', 'dd-mm-yyyy hh24:mi:ss'));

insert into as_role_func (ROLE_ID, COMPO_ID, FUNC_ID, TRANS_DATE)
values ('gys_xunjiatoubiao', 'ZC_EB_XUNJIA_BAOJIA', 'fsend', to_date('06-11-2010 17:50:04', 'dd-mm-yyyy hh24:mi:ss'));

insert into as_role_func (ROLE_ID, COMPO_ID, FUNC_ID, TRANS_DATE)
values ('gys_xunjiatoubiao', 'ZC_EB_XUNJIA_BAOJIA', 'fwatch', to_date('06-11-2010 17:50:04', 'dd-mm-yyyy hh24:mi:ss'));















