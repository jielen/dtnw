

select * from as_compo c where c.compo_name like '%���%'
--c.compo_id like 'ZC_T_CHG_TYPE%';

select * from as_func f where f.func_id='fcarrarylock' for update;

select * from as_compo_func cf where cf.compo_id='ZC_PRO_END_YEAR_END' for update;

select * from as_menu_compo mc where mc.compo_id like '%ZC_PRO_END_YEAR_END%';

select * from as_val v where v.valset_id='ZC_VS_QX_STATUS' for update;

select * from as_lang_trans l where l.res_na like '%�ɹ�%���%' for update;
--fcarraryNew ��ת����

--fcarrarylock ��ת����
select * from as_option o where o.opt_val like '%127.0%';

select * from as_user_func uf where uf.compo_id in ('ZC_EB_QX','ZC_EB_QB','ZC_EB_XUNJIA_BAOJIA')
AND UF.USER_ID='gys4' for update;

select * from as_com

select * from as_role_func rf where rf.role_id like 'YSDWCG%' and rf.compo_id='ZC_PRO_END_YEAR_END' for update;
--ffinishitem
select * from zc_search_condition sc where sc.compo_id='ZC_PRO_END_YEAR_END' FOR UPDATE;

SELECT * FROM ZC_ROLE_SEARCH_CONDITION RS WHERE RS.CONDITION_ID='ZcEbQx_Tab'   FOR UPDATE;

select * from as_emp e where e.emp_name like '��%';

select * from as_position p where p.posi_name like '%��%'

--------------------------------------------------

delete from zc_search_condition sc where sc.compo_id='ZC_PRO_END_YEAR_END';





