USE migrationdb
;
alter table `borrower_merge`
add column `borrower_objid` varchar(50) default null after `author_name`,
add column `borrower_name` varchar(255) default null after `borrower_objid`
;
create index `ix_borrowerid` on `borrower_merge`(`borrower_objid`);
create index `ix_borrowername` on `borrower_merge`(`borrower_name`)
;