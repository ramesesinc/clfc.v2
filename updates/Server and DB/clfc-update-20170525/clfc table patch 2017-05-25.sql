use `clfc2`
;
alter table collection_cb_sendback
modify remarks text default null
;
insert ignore into `itemaccount`(`objid`,`dtcreated`,`author_objid`,`author_name`,`txnstate`,`code`,`title`,`type`)
values
  ("ITMACCT38f2a5f9:15c34497674:-7fdd", now(), "SYSTEM", "SYSTEM", "ACTIVATED", "FC", "FINANCE CHARGES", "REVENUE")
;