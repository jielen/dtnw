
ע�⣺���������ű���ִ�к���Ҫͨ��pl/sql����Ӧ���ļ��ֹ����뵽FILE_CONTENT�ֶ�


--�����б꼰������̸��_�б깫��.xml
insert into as_file (FILE_ID, FILE_CONTENT, FILE_NAME, FILE_DESC, FILE_CREATOR, CONTENT_TYPE, FILE_UPLOADTIME, SUFFIX_NAME, FULL_NAME, FILE_PATH)
values ('bulletin_zhongbiao', null, '�ɽ��������.xml', null, null, null, null, 'application/octet-stream', '�ɽ��������.xml', null);

--�����б꼰������̸��.xml
insert into as_file (FILE_ID, FILE_CONTENT, FILE_NAME, FILE_DESC, FILE_CREATOR, CONTENT_TYPE, FILE_UPLOADTIME, SUFFIX_NAME, FULL_NAME, FILE_PATH)
values ('bulletin_zhaobiao', null, '�б깫��.xml', 'application/msword', null, null, null, null, null, null);
