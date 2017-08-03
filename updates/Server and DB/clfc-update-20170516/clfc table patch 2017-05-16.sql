use `clfc2`
;
alter table loanapp_branchloan
add column `appcount` smallint(3) default 0 after `borrower_name`
;
create index `ix_appcount` on `loanapp_branchloan`(`appcount`)
;
insert ignore into `itemaccount`(`objid`,`dtcreated`,`author_objid`,`author_name`,`txnstate`,`code`,`title`,`type`)
values
	("NF", now(), "SYSTEM", "SYSTEM", "ACTIVATED", "NF", "NOTARIAL FEE", "REVENUE"),
	("DS", now(), "SYSTEM", "SYSTEM", "ACTIVATED", "DS", "DOCUMENTARY STAMP", "REVENUE"),
	("RF", now(), "SYSTEM", "SYSTEM", "ACTIVATED", "RF", "REGISTRATION FEE", "REVENUE")
;
update sys_var 
set value='true'
where `name`='validate_application_charges'
;