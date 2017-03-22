[findLedgerByTdNo]
SELECT rl.state, rl.objid  
FROM rptledger rl
WHERE rl.tdno = $P{refno}


[findRegisteredLedgerByTdNo]
SELECT rl.tdno, rl.state, rl.cadastrallotno, rl.totalmv, rl.totalav, 
	rl.objid,
	rl.lastyearpaid, rl.lastqtrpaid
FROM rpt_sms_registration s
	INNER JOIN rptledger rl ON s.refid = rl.objid 
WHERE rl.tdno = $P{refno}
  AND s.phoneno = $P{phoneno}
  

 [getRegisteredLedgersByPhoneNo]
 SELECT rl.tdno, rl.state, rl.cadastrallotno, rl.totalmv, rl.totalav ,
 	rl.objid,
	rl.lastyearpaid, rl.lastqtrpaid
FROM rpt_sms_registration s
	INNER JOIN rptledger rl ON s.refid = rl.objid 
WHERE s.phoneno = $P{phoneno}


[enroll]
INSERT INTO rpt_sms_registration 
	(phoneno, refid, dtregistered)
VALUES
	($P{phoneno}, $P{refid}, $P{dtregistered})


[deleteRegisteredTdNo]		
DELETE FROM rpt_sms_registration 
WHERE refid IN (
	SELECT rl.objid 
	FROM rptledger rl 
		INNER JOIN rpt_sms_registration s ON rl.objid = s.refid 
	WHERE rl.tdno = $P{refno}
	  AND s.phoneno = $P{phoneno}
)


[deleteAllRegistration]
DELETE FROM rpt_sms_registration WHERE phoneno = $P{phoneno}




[findTrackingInfo]
SELECT * FROM rpttracking WHERE trackingno =$P{refno}

[findRegisteredTracking]
SELECT t.*
FROM rpt_sms_registration s
	INNER JOIN rpttracking t ON s.refid = t.objid 
WHERE t.trackingno = $P{refno}


[getPhones]
SELECT *
FROM rpt_sms_registration
WHERE refid = $P{refid}