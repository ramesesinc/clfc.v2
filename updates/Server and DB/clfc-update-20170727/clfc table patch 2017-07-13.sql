use clfc2
;
CREATE TABLE IF NOT EXISTS `mobile_tracker_migrated` (
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB
;
CREATE TABLE IF NOT EXISTS `mobile_tracker_detail_migrated` (
  `objid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=InnoDB
;