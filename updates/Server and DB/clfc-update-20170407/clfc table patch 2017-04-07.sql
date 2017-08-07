USE clfc
;
alter table `loan_ledger_adjustment`
add column `debit_type` text default null after `amount`,
add column `debit_amount` decimal(10,2) default 0.00 after `debit_type`,
add column `credit_type` text default null after `debit_amount`,
add column `credit_amount` decimal(10,2) default 0.00 after `credit_type`,
add column `info` text default null after `remarks`
;
alter table loan_ledger_detail 
add column `info` text default null after `remarks`
;
alter table ledgeramnesty_postingdetail
add column `info` text default null after `remarks`
;
insert ignore into sys_usergroup (objid, title, domain, role, userclass, orgclass)
values
  ('LOAN_ASSESSOR', 'ASSESSOR', 'LOAN', 'ASSESSOR', 'usergroup', null)
;