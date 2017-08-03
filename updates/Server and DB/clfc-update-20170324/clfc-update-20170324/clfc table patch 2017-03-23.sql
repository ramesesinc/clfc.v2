use `clfc2`
;

alter table `otherreceipt_detail`
add column `onlinedeposit` smallint(3) default 0
;

create index `ix_onlinedeposit` on `otherreceipt_detail`(`onlinedeposit`)
;

insert ignore into `sys_var`(`name`,`value`,`description`,`datatype`,`category`)
values ("validate_application_charges", "false", "Validate Application Charges", null, "BRANCH")
;

CREATE TABLE IF NOT EXISTS `itemaccount` (
  `objid` varchar(50) NOT NULL,
  `dtcreated` datetime default NULL,
  `author_objid` varchar(50) default NULL,
  `author_name` varchar(50) default NULL,
  `txnstate` varchar(25) default NULL,
  `code` varchar(50) default NULL,
  `title` varchar(50) default NULL,
  `description` varchar(255) default NULL,
  `type` varchar(25) default NULL,
  `fund_objid` varchar(50) default NULL,
  `fund_code` varchar(50) default NULL,
  `fund_title` varchar(50) default NULL,
  `defaultvalue` decimal(10,2) default NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_code` (`code`),
  KEY `ix_txnstate` (`txnstate`),
  KEY `ix_title` (`title`),
  KEY `ix_type` (`type`)
) ENGINE=InnoDB
;

CREATE TABLE IF NOT EXISTS `loan_application` (
  `objid` VARCHAR(50) NOT NULL,
  `dtcreated` DATETIME DEFAULT NULL,
  `author_objid` VARCHAR(50) DEFAULT NULL,
  `author_name` VARCHAR(50) DEFAULT NULL,
  `txnstate` VARCHAR(50) DEFAULT NULL,
  `appno` VARCHAR(50) DEFAULT NULL,
  `apptype` VARCHAR(50) DEFAULT NULL,
  `txnmode` VARCHAR(50) DEFAULT NULL,
  `txntype` VARCHAR(50) DEFAULT NULL,
  `borrower_objid` VARCHAR(50) DEFAULT NULL,
  `borrower_name` VARCHAR(50) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT '0.00',
  `dtreleased` DATE DEFAULT NULL,
  `dtmatured` DATE DEFAULT NULL,
  `producttype_name` VARCHAR(50) DEFAULT NULL,
  `producttype_term` SMALLINT(3) DEFAULT NULL,
  `route_code` VARCHAR(50) DEFAULT NULL,
  `loantype` VARCHAR(50) DEFAULT NULL,
  `loancount` SMALLINT(3) DEFAULT NULL,
  `purpose` TINYTEXT,
  `info` TEXT,
  `dtposted` DATETIME DEFAULT NULL,
  `posted_objid` VARCHAR(50) DEFAULT NULL,
  `postedby_name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ix_dtcreated` (`dtcreated`),
  KEY `ix_author` (`author_objid`,`author_name`),
  KEY `ix_txnstate` (`txnstate`),
  KEY `ix_appno` (`appno`),
  KEY `ix_apptype` (`apptype`),
  KEY `ix_txnmode` (`txnmode`),
  KEY `ix_borrower` (`borrower_objid`,`borrower_name`),
  KEY `ix_dtreleased` (`dtreleased`),
  KEY `ix_dtmatured` (`dtmatured`),
  KEY `ix_producttype` (`producttype_name`,`producttype_term`),
  KEY `ix_route` (`route_code`),
  KEY `ix_loantype` (`loantype`),
  KEY `ix_loancount` (`loancount`),
  KEY `ix_dtposted` (`dtposted`),
  KEY `ix_postedby` (`posted_objid`,`postedby_name`),
  KEY `ix_txntype` (`txntype`),
  CONSTRAINT `fk_loan_application_borrower` FOREIGN KEY (`borrower_objid`) REFERENCES `customer` (`objid`),
  CONSTRAINT `fk_loan_application_producttype` FOREIGN KEY (`producttype_name`) REFERENCES `loan_product_type` (`name`),
  CONSTRAINT `fk_loan_application_route` FOREIGN KEY (`route_code`) REFERENCES `loan_route` (`code`)
) ENGINE=INNODB
;

CREATE TABLE IF NOT EXISTS `loanapp_charge` (
  `objid` varchar(50) NOT NULL,
  `appid` varchar(50) default NULL,
  `acctid` varchar(50) default NULL,
  `title` varchar(50) default NULL,
  `amount` decimal(10,2) default NULL,
  PRIMARY KEY  (`objid`),
  KEY `fk_loanapp_charges` (`appid`),
  KEY `ix_acctid` (`acctid`),
  KEY `ix_title` (`title`),
  KEY `ix_amount` (`amount`),
  CONSTRAINT `fk_itemaccountid` FOREIGN KEY (`acctid`) REFERENCES `itemaccount` (`objid`),
  CONSTRAINT `fk_loanapp_charges` FOREIGN KEY (`appid`) REFERENCES `loanapp` (`objid`)
) ENGINE=InnoDB
;

