use clfc
;
alter table `otherreceipt_detail`
add column `od_no` varchar(50) default null after `check_date`,
add column `od_date` date default null after `od_no`
;
update otherreceipt_detail 
set payoption='onlinedeposit'
where onlinedeposit=1
  and payoption <> 'onlinedeposit'
;