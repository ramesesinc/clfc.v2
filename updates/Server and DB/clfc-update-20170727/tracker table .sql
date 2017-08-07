use trackerdb
;
CREATE TABLE IF NOT EXISTS `mobile_tracker` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(50) default NULL,
  `dtfiled` datetime default NULL,
  `terminalid` varchar(50) default NULL,
  `userid` varchar(50) default NULL,
  `branch_objid` varchar(50) default NULL,
  `branch_name` varchar(255) default NULL,
  `dtstart` datetime default NULL,
  `startlng` decimal(18,14) default '0.00000000000000',
  `startlat` decimal(18,14) default '0.00000000000000',
  `dtclosed` datetime default NULL,
  `closedby` varchar(255) default NULL,
  `closedremarks` varchar(255) default NULL,
  `closedlng` decimal(18,14) default '0.00000000000000',
  `closedlat` decimal(18,14) default '0.00000000000000',
  `info` text,
  PRIMARY KEY  (`objid`),
  KEY `ix_state` (`state`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_terminalid` (`terminalid`),
  KEY `ix_userid` (`userid`),
  KEY `ix_branchid` (`branch_objid`),
  KEY `ix_branchname` (`branch_name`),
  KEY `ix_dtstart` (`dtstart`),
  KEY `ix_start_location` (`startlng`,`startlat`),
  KEY `ix_dtclosed` (`dtclosed`),
  KEY `ix_closedby` (`closedby`,`closedremarks`),
  KEY `ix_closed_location` (`closedlng`,`closedlat`)
) ENGINE=InnoDB 
;
CREATE TABLE IF NOT EXISTS `mobile_tracker_detail` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `branch_objid` varchar(50) default NULL,
  `branch_name` varchar(50) default NULL,
  `dtfiled` datetime default NULL,
  `txndate` datetime default NULL,
  `txntype` varchar(50) default NULL,
  `refid` varchar(50) default NULL,
  `reftype` varchar(50) default NULL,
  `lng` decimal(18,14) default '0.00000000000000',
  `lat` decimal(18,14) default '0.00000000000000',
  `remarks` text,
  `state` smallint(3) default NULL,
  `info` text,
  PRIMARY KEY  (`objid`),
  KEY `fk_mobile_tracker_detail` (`parentid`),
  KEY `ix_dtfiled` (`dtfiled`),
  KEY `ix_txndate` (`txndate`),
  KEY `ix_txntype` (`txntype`),
  KEY `ix_refid` (`refid`),
  KEY `ix_reftype` (`reftype`),
  KEY `ix_location` (`lng`,`lat`),
  KEY `ix_state` (`state`),
  KEY `ix_branchid` (`branch_objid`),
  KEY `ix_branchname` (`branch_name`),
  CONSTRAINT `fk_mobile_tracker_detail` FOREIGN KEY (`parentid`) REFERENCES `mobile_tracker` (`objid`)
) ENGINE=InnoDB
;
CREATE TABLE IF NOT EXISTS `mobile_tracker_item` (
  `objid` varchar(100) NOT NULL,
  `parentid` varchar(50) default NULL,
  `branchid` varchar(50) default NULL,
  `billingitemid` varchar(50) default NULL,
  `item_objid` varchar(50) default NULL,
  `item_type` varchar(50) default NULL,
  `info` text,
  PRIMARY KEY  (`objid`),
  KEY `fk_mobile_tracker_item` (`parentid`),
  KEY `ix_branchid` (`branchid`),
  KEY `ix_billingitemid` (`billingitemid`),
  KEY `ix_itemid` (`item_objid`),
  KEY `ix_itemtype` (`item_type`),
  CONSTRAINT `fk_mobile_tracker_item` FOREIGN KEY (`parentid`) REFERENCES `mobile_tracker` (`objid`)
) ENGINE=InnoDB
;
CREATE TABLE IF NOT EXISTS `mobile_tracker_branch` (
  `objid` varchar(50) NOT NULL,
  `name` varchar(255) default NULL,
  `code` varchar(50) default NULL,
  `address` text,
  `contactno` varchar(50) default NULL,
  `email` varchar(50) default NULL,
  `info` text,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `uix_code` (`code`),
  KEY `ix_name` (`name`),
  KEY `ix_contactno` (`contactno`),
  KEY `ix_email` (`email`)
) ENGINE=InnoDB
;
CREATE TABLE IF NOT EXISTS `mobile_tracker_branch_route` (
  `objid` varchar(50) NOT NULL,
  `parentid` varchar(50) default NULL,
  `branchid` varchar(50) default NULL,
  `route_code` varchar(50) default NULL,
  `route_description` varchar(255) default NULL,
  `route_area` varchar(255) default NULL,
  `info` text,
  PRIMARY KEY  (`objid`),
  KEY `fk_tracker_branch_route` (`parentid`),
  KEY `ix_branchid` (`branchid`),
  KEY `ix_routecode` (`route_code`),
  KEY `ix_routedescription` (`route_description`),
  KEY `ix_routearea` (`route_area`),
  CONSTRAINT `fk_tracker_branch_route` FOREIGN KEY (`parentid`) REFERENCES `mobile_tracker_branch` (`objid`)
) ENGINE=InnoDB
;