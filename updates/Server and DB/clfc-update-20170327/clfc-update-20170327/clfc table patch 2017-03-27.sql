USE `clfc2`
;
alter table `loan_application` 
change `posted_objid` `postedby_objid` varchar(50)
;
CREATE TABLE IF NOT EXISTS `loanapp_extinfo` (
  `objid` varchar(50) NOT NULL,
  `netamount` decimal(10,2) default NULL,
  `totalcharges` decimal(10,2) default NULL,
  `info` text,
  PRIMARY KEY  (`objid`),
  KEY `ix_netamount` (`netamount`),
  KEY `ix_totalcharges` (`totalcharges`),
  CONSTRAINT `fk_loanapp_extinfo` FOREIGN KEY (`objid`) REFERENCES `loanapp` (`objid`)
) ENGINE=InnoDB
;