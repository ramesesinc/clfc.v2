use `clfc`
;
alter table `dailycollection_depositslip`
drop index `uix_controlno`
;
create index `ix_control` on `dailycollection_depositslip`(`controlno`)
;