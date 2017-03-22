[getList]
SELECT 
	rl.objid, rl.state, rl.faasid, rl.tdno, rl.prevtdno, rl.titleno,
	rl.taxpayer_objid, e.name AS taxpayer_name,
	rl.fullpin, rl.cadastrallotno, rl.totalareaha, rl.classcode, rl.rputype,  
	rl.totalmv, rl.totalav, rl.lastyearpaid, rl.lastqtrpaid,
	CASE WHEN rl.faasid IS NULL THEN 'M' ELSE '' END AS type,
	b.objid AS barangay_objid, b.name AS barangay_name
FROM rptledger rl 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
	INNER JOIN barangay b ON rl.barangayid = b.objid 
WHERE 1=1
${filters}	


[findById]
SELECT 
	rl.*,
	rl.totalareaha * 10000 as totalareasqm,
	e.name AS taxpayer_name, 
	e.address_text AS taxpayer_address,
	b.name AS barangay_name
FROM rptledger rl 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
	INNER JOIN barangay b ON rl.barangayid = b.objid 
WHERE rl.objid = $P{objid}


	
[findApprovedLedgerByFaasId]
SELECT rl.* , r.taxable 
FROM rptledger rl 
	INNER JOIN faas f ON rl.faasid = f.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid 
WHERE rl.faasid = $P{faasid} AND rl.state = 'APPROVED' 


[findLedgerByFaasId]	
SELECT * FROM rptledger WHERE faasid = $P{faasid} 


[getLedgerFaases]
SELECT rlf.*,
	pc.code AS classification_code,
	pc.name AS classification_name,
	pc1.code AS actualuse_code,
	pc1.name AS actualuse_name
FROM rptledgerfaas rlf
	INNER JOIN propertyclassification pc ON rlf.classification_objid = pc.objid 
	LEFT JOIN propertyclassification pc1 ON rlf.actualuse_objid = pc1.objid 
WHERE rlf.rptledgerid = $P{rptledgerid} 
ORDER BY rlf.fromyear, rlf.tdno   DESC 

[findLedgerFaasByFaasId]
SELECT rlf.*,
	pc.code AS classification_code,
	pc.name AS classification_name,
	pc1.code AS actualuse_code,
	pc1.name AS actualuse_name
FROM rptledgerfaas rlf
	INNER JOIN propertyclassification pc ON rlf.classification_objid = pc.objid 
	LEFT JOIN propertyclassification pc1 ON rlf.actualuse_objid = pc1.objid 
WHERE rlf.rptledgerid =  $P{rptledgerid} 
  AND $P{yr} >= rlf.fromyear 
  AND $P{yr} <= (CASE WHEN rlf.toyear = 0 THEN $P{yr} ELSE rlf.toyear END)
  AND rlf.state ='APPROVED'


[getLedgerBillItems]
SELECT
	rl.objid AS rptledgerid,  lf.tdno, bi.year, lf.assessedvalue, 
	SUM(bi.basic) AS basic, 
	SUM(bi.basicint) AS basicint, 
	SUM(bi.basicdisc) AS basicdisc, 
	SUM(bi.basicidle) AS basicidle,
	SUM(bi.sef) AS sef, 
	SUM(bi.sefint) AS sefint, 
	SUM(bi.sefdisc) AS sefdisc, 
	SUM(bi.firecode) AS firecode,

	SUM(bi.basic -  bi.basicdisc + bi.basicint + bi.basicidle +
	    bi.sef - bi.sefdisc + bi.sefint +	bi.firecode 
   ) AS total 
FROM rptledger rl
	INNER JOIN rptledgerfaas lf ON rl.objid = lf.rptledgerid
	INNER JOIN rptbill_ledger_item bi ON rl.objid = bi.rptledgerid AND lf.objid = bi.rptledgerfaasid
WHERE rl.objid = $P{rptledgerid} 
GROUP BY rl.objid, lf.tdno, bi.year, lf.assessedvalue
ORDER BY bi.year DESC 



[closePreviousFaasToYearAndQtr]
UPDATE rptledgerfaas SET
	toyear = $P{toyear},
	toqtr  = $P{toqtr}
WHERE objid <> $P{objid} AND rptledgerid = $P{rptledgerid}	AND toyear = 0 


[updateLedgerLastItemYear]
UPDATE rptledger SET 
	lastitemyear = $P{lastitemyear},
	nextbilldate = NULL
WHERE objid = $P{objid}	


[clearNextBillDateByLedger]
UPDATE rptledger SET 
	nextbilldate = NULL 
WHERE objid = $P{objid} 
  AND state = 'APPROVED'

[clearNextBillDate]
UPDATE rptledger SET 
	nextbilldate = NULL 
WHERE state = 'APPROVED'



[getLedgerCredits]
SELECT t.*
FROM (
	SELECT 
		objid AS rptreceiptid,
		refno,
		refdate ,
		paidby_name,
		fromyear,
		fromqtr,
		toyear, 
		toqtr,
		basic,
		basicint,
		basicdisc,
		basicidle,
		sef,
		sefint,
		sefdisc,
		firecode,
		amount,
		type AS txnmode,
		0 as partialled
	FROM rptledger_credit rc 
	WHERE rptledgerid = $P{rptledgerid}

	UNION ALL
	
	SELECT 
		cr.objid AS rptreceiptid, 
		cr.receiptno AS refno,
		cr.receiptdate AS refdate,
		cr.paidby AS paidby_name,
		cri.year AS fromyear,
		case when cri.qtr = 0 then cri.fromqtr else cri.qtr end AS fromqtr,
		cri.year AS toyear,
		case when cri.qtr = 0 then cri.toqtr else cri.qtr end AS toqtr,
		cri.basic AS basic,
		cri.basicint AS basicint,
		cri.basicdisc AS basicdisc,
		cri.basicidle- cri.basicidledisc + cri.basicidleint AS basicidle,
		cri.sef AS sef,
		cri.sefint AS sefint,
		cri.sefdisc AS sefdisc,
		cri.firecode AS firecode,
		cri.basic+ cri.basicint - cri.basicdisc + cri.basicidle - cri.basicidledisc + cri.basicidleint +
				cri.sef + cri.sefint - cri.sefdisc + cri.firecode AS amount,
		crr.txntype AS txnmode,
		cri.partialled
	FROM cashreceipt_rpt crr
		INNER JOIN cashreceipt cr ON crr.objid = cr.objid 
		INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 	
		LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
	WHERE cri.rptledgerid = $P{rptledgerid}
		AND cv.objid IS NULL 	

) t
ORDER BY t.fromyear desc, t.fromqtr desc 











[approveLedgerFaas]
UPDATE rptledgerfaas SET state = 'APPROVED' WHERE objid = $P{objid}


[updateLastYearQtrPaid]
UPDATE rptledger SET 
	lastyearpaid = $P{toyear}, lastqtrpaid = $P{toqtr}, nextbilldate = null 
WHERE objid = $P{rptledgerid}


[updateState]
UPDATE rptledger SET state = $P{state} WHERE objid = $P{objid}



[fixLedgerInfo]
UPDATE rptledger SET 
	taxpayer_objid = $P{taxpayerid},
	owner_name = $P{taxpayername},
	lastyearpaid = $P{lastyearpaid}, 
	lastqtrpaid = $P{lastqtrpaid},
	partialledyear = $P{partialledyear},
	partialledqtr = $P{partialledqtr},
	lastbilledyear = null,
	lastbilledqtr = null,
	partialbasic = $P{partialbasic},
	partialbasicint = $P{partialbasicint},
	partialbasicdisc = $P{partialbasicdisc},
	partialsef = $P{partialsef},
	partialsefint = $P{partialsefint},
	partialsefdisc = $P{partialsefdisc},
	taxable = $P{taxable},
	nextbilldate = null
WHERE objid = $P{rptledgerid}

[resetLastBilledInfo]
UPDATE rptledger SET 
	nextbilldate = null,
	lastbilledyear = null,
	lastbilledqtr = null
WHERE objid = $P{rptledgerid}



[findLedgerbyTdNo]
SELECT objid 
FROM rptledger 
WHERE tdno = $P{tdno} 

[findLedgerbyPrevTdNo]
SELECT objid 
FROM rptledger 
WHERE tdno = $P{prevtdno} 

[findLedgerByFullPin]
SELECT objid 
FROM rptledger 
WHERE fullpin = $P{fullpin} 


[updateOnlineLedgerInfo]
UPDATE r SET
	r.tdno = f.tdno,
	r.fullpin = f.fullpin,
	r.rputype = rpu.rputype,
	r.cadastrallotno = rp.cadastrallotno,
	r.totalmv = rpu.totalmv, 
	r.totalav = rpu.totalav, 
	r.totalareaha = rpu.totalareaha,
	r.classcode = pc.code,
	r.taxpayer_objid = f.taxpayer_objid,
	r.txntype_objid = f.txntype_objid,
	r.taxable = rpu.taxable,
	r.owner_name = f.owner_name,
	r.prevtdno = f.prevtdno,
	r.titleno = f.titleno 
FROM rptledger r, faas f, rpu rpu, realproperty rp, propertyclassification pc   	
WHERE r.objid = $P{objid}
  AND r.faasid = f.objid 
  AND f.rpuid = rpu.objid 
  AND f.realpropertyid = rp.objid
  AND rpu.classification_objid = pc.objid

[updateLedgerFaasId]  
UPDATE rptledger SET 
	faasid = $P{faasid}
WHERE objid = $P{ledgerid}	


[findSubLedgerById]
SELECT sl.*, p.fullpin AS parent_fullpin, p.tdno AS parent_tdno
FROM rptledger_subledger sl 
	INNER JOIN rptledger p ON sl.parent_objid = p.objid 
WHERE sl.objid = $P{objid}


[getCreditedLedgerItems]
select objid, year, paidqtr 
from rptledgeritem 
where rptledgerid = $P{rptledgerid}
  and year <= $P{toyear}

[updateLedgerItemPaidInfo]  
update rptledgeritem set 
	paidqtr = $P{paidqtr},
	paid = $P{paid}
where objid = $P{objid}	
and taxdifference = 0

[findLatestLedgerItem]
SELECT objid, year, paid 
FROM rptledgeritem 
WHERE rptledgerid = $P{objid}
AND taxdifference = 0 
and paid = 0
ORDER BY year desc 

[findLedgerItemByYears]
select *
from rptledgeritem 
where rptledgerid = $P{objid}
and year = $P{year}
and actualuse_objid = $P{actualuseid}
and taxdifference = 0


[getLedgerItemsWithTaxDifference]
select objid, rptledgerid, year, paidqtr, av, $P{currentav} - av AS avdifference
from rptledgeritem 
where rptledgerid = $P{rptledgerid} 
  and year >= $P{effectivityyear}
  and paid = 1
  and av < $P{currentav}



[deleteUnpaidLedgerItemsAboveEffectivityYear]
delete from rptledgeritem 
where rptledgerid = $P{rptledgerid}	
  and year >= ${effectivityyear}
  and paid = 0

[deleteBillLedgerItems]
delete from rptbill_ledger_item 
where rptledgeritemid in (
	select objid from rptledgeritem 
	where rptledgerid = $P{rptledgerid}	
		and year >= ${effectivityyear}
		and paid = 0
)  




[getLedgerItems]
select rli.*,
	pc.code AS classification_code,
	au.code AS actualuse_code 
from rptledgeritem rli
	inner join propertyclassification pc on rli.classification_objid = pc.objid
	left join propertyclassification au on rli.actualuse_objid = au.objid 
where rptledgerid = $P{rptledgerid}
order by year desc 





[getAffectedLedgerItemsByLedgerFaas]
select *
from rptledgeritem 
where rptledgerid = $P{rptledgerid}
  and year >= $P{fromyear} 
  and year <= $P{toyear}
  and paid = 0
 

[updateLedgerItemInfo] 
update rptledgeritem set 
	av = $P{av}
where objid = $P{objid}	


[fixLedgerItemPaidInfo]
UPDATE rptledgeritem SET
	paid = CASE 
			WHEN year < $P{lastyearpaid} THEN 1
			WHEN year = $P{lastyearpaid} AND $P{lastqtrpaid} = 4 AND $P{partialbasic} = 0 AND $P{partialsef} = 0 THEN 1
			ELSE 0
		   END,
	paidqtr = CASE 
			WHEN year < $P{lastyearpaid} THEN 4
			WHEN year = $P{lastyearpaid} AND ($P{partialbasic} > 0 OR $P{partialsef} > 0) THEN $P{lastqtrpaid} - 1
			WHEN year = $P{lastyearpaid} AND $P{lastqtrpaid} = 4 AND $P{partialbasic} = 0 AND $P{partialsef} = 0 THEN 4
			ELSE $P{lastqtrpaid}
		   END
WHERE rptledgerid = $P{rptledgerid}
 AND year <= $P{lastyearpaid}


[fixLedgerItemUnpaidInfo]
UPDATE rptledgeritem SET
	paid = 0,
	paidqtr = 0
WHERE rptledgerid = $P{rptledgerid}
 AND year > $P{lastyearpaid}


[getSubLedgers]
select 
	sl.*,
	e.name AS taxpayer_name, 
	rl.state, 
	rl.totalareaha, 
	rl.totalareaha * 10000 as totalareasqm,
	rl.totalmv,
	rl.totalav, 
	rl.lastyearpaid, rl.lastqtrpaid 
from rptledger_subledger sl
	inner join rptledger rl on sl.objid= rl.objid 
	inner join entity e on rl.taxpayer_objid = e.objid 
where sl.parent_objid = $P{objid} 
order by sl.subacctno 


[deleteSubledgerFaas]
DELETE FROM rptledgerfaas WHERE rptledgerid =$P{objid}


[deleteRptBillLedgerItems]
delete from rptbill_ledger_item 
where rptledgerid =$P{objid}


[deleteRptLedgerItems]
delete from rptledgeritem 
where rptledgerid = $P{objid}
  and paid = 0
  and taxdifference = 0
  and not exists(
		select *
		from cashreceiptitem_rpt_online 
		where rptledgerid = $P{objid}
		and rptledgeritemid = rptledgeritem.objid 
	);


[updateLedgerInfoFromNewRevision]
update rptledger set 
	fullpin = $P{fullpin},
	tdno = $P{tdno},
	txntype_objid = $P{txntype_objid},
	classification_objid = $P{classification_objid},
	classcode = $P{classcode},
	totalav = $P{totalav},
	taxable = $P{taxable}
where objid = $P{objid}



[deleteRptBillLedger]
delete from rptbill_ledger where rptledgerid = $P{objid}

[deleteRptBillLedgerItem]
delete from rptbill_ledger_item where rptledgerid = $P{objid}

[deleteRptBillLedgerAccount]
delete from rptbill_ledger_account where rptledgerid = $P{objid}

[deleteRptLedgerItem]
delete from rptledgeritem where rptledgerid = $P{objid}
	
[deleteLedgerFaases]
DELETE FROM rptledgerfaas WHERE rptledgerid = $P{objid} 

[deleteLedgerCredits]
delete from rptledger_credit where rptledgerid = $P{objid} 
	
[deleteLedger]
DELETE FROM rptledger WHERE objid = $P{objid}

