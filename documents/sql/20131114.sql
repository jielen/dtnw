
注意：下面的两句脚本在执行后，需要通过pl/sql将对应的文件手工放入到FILE_CONTENT字段


--公开招标及竞争性谈判_中标公告.xml
insert into as_file (FILE_ID, FILE_CONTENT, FILE_NAME, FILE_DESC, FILE_CREATOR, CONTENT_TYPE, FILE_UPLOADTIME, SUFFIX_NAME, FULL_NAME, FILE_PATH)
values ('bulletin_zhongbiao', null, '成交结果公告.xml', null, null, null, null, 'application/octet-stream', '成交结果公告.xml', null);

--公开招标及竞争性谈判.xml
insert into as_file (FILE_ID, FILE_CONTENT, FILE_NAME, FILE_DESC, FILE_CREATOR, CONTENT_TYPE, FILE_UPLOADTIME, SUFFIX_NAME, FULL_NAME, FILE_PATH)
values ('bulletin_zhaobiao', null, '招标公告.xml', 'application/msword', null, null, null, null, null, null);
