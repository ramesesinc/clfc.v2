
[getItemsForPayment]
SELECT 
	t.rptledgerid, t.tdno, t.owner_name,
	t.lastyearpaid, t.lastqtrpaid,
	t.fromyear, 
	(SELECT MIN(CASE WHEN qtr = 0 THEN fromqtr ELSE qtr END) FROM rptbill_ledger_item WHERE rptledgerid = t.rptledgerid AND year = t.fromyear) AS fromqtr,
	t.toyear AS toyear,
	(SELECT MAX(CASE WHEN qtr = 0 THEN toqtr ELSE qtr END) FROM rptbill_ledger_item WHERE rptledgerid = t.rptledgerid AND year = t.toyear) AS toqtr,
	SUM(t.basic) AS totalbasic,
	SUM(t.sef) AS totalsef,
	SUM(t.firecode) AS totalfirecode,
	SUM(t.basic + t.firecode + t.basicidle) AS totalgeneral,
	SUM(t.basic + t.sef + t.firecode + t.basicidle) AS amount,
  t.partialled 
FROM (
	SELECT ${mssqlcountfilter}
		rl.objid AS rptledgerid, 
		rl.owner_name,
		rl.lastyearpaid,
		rl.lastqtrpaid,
		rl.tdno, 
		MAX(bi.partialled) AS partialled,
		MIN(bi.year) AS fromyear,
		MAX(bi.year) AS toyear,
		SUM(bi.basic - bi.basicdisc + bi.basicint) AS basic,
		SUM(bi.sef - bi.sefdisc + sefint) AS sef,
		SUM(bi.basicidle - bi.basicidledisc + bi.basicidleint) AS basicidle, 
		SUM(bi.firecode) AS firecode 
	FROM rptledger rl
		INNER JOIN rptbill_ledger_item bi ON rl.objid = bi.rptledgerid
	WHERE  rl.objid IN (
		SELECT rl.objid 
		FROM rptledger rl 
		WHERE ${filters}
		 AND rl.state = 'APPROVED'
		 AND rl.taxable = 1 

		UNION 

		SELECT rl.objid 
		FROM propertypayer pp
			inner join propertypayer_item ppi on pp.objid = ppi.parentid
			inner join rptledger rl on ppi.rptledger_objid = rl.objid 
		WHERE ${ppfilters}
		 AND rl.state = 'APPROVED'
		 AND rl.taxable = 1 
	)
	GROUP BY rl.objid, rl.owner_name, rl.tdno, rl.lastyearpaid, rl.lastqtrpaid
) t
GROUP BY t.rptledgerid, t.owner_name, t.lastyearpaid, t.lastqtrpaid, t.tdno, t.fromyear, t.toyear, t.partialled



[getItemsForPaymentByBill]
SELECT 
	t.rptledgerid, t.tdno, t.owner_name, 
	t.lastyearpaid, t.lastqtrpaid,
	t.billid,
	t.fromyear, 
	(SELECT MIN(CASE WHEN qtr = 0 THEN fromqtr ELSE qtr END) FROM rptbill_ledger_item WHERE rptledgerid = t.rptledgerid AND year = t.fromyear) AS fromqtr,
	$P{billtoyear}  AS toyear,
	(SELECT MAX(CASE WHEN qtr = 0 THEN toqtr ELSE qtr END) FROM rptbill_ledger_item WHERE rptledgerid = t.rptledgerid AND year = t.toyear) AS toqtr,
	SUM(t.basic) AS totalbasic,
	SUM(t.sef) AS totalsef,
	SUM(t.firecode) AS totalfirecode,
	SUM(t.basic + t.firecode + t.basicidle) AS totalgeneral,
	SUM(t.basic + t.sef + t.firecode + t.basicidle) AS amount
FROM (
	SELECT 
		rb.objid AS billid,
		rl.objid AS rptledgerid, 
		rl.owner_name, 
		rl.lastyearpaid,
		rl.lastqtrpaid,
		rl.tdno, 
		MIN(bi.year) AS fromyear,
		MAX(bi.year) AS toyear,
		SUM(bi.basic - basicdisc + basicint) AS basic,
		SUM(bi.sef - sefdisc + sefint) AS sef,
		SUM(bi.basicidle - bi.basicidledisc + bi.basicidleint) AS basicidle, 
		SUM(bi.firecode) AS firecode 
	FROM rptbill rb
		INNER JOIN rptbill_ledger rbl ON rb.objid = rbl.rptbillid 
		INNER JOIN rptledger rl ON rbl.rptledgerid = rl.objid 
		INNER JOIN rptbill_ledger_item bi ON rl.objid = bi.rptledgerid
	WHERE rb.objid = $P{billid}
	  AND rl.state = 'APPROVED'
	  AND rl.taxable = 1
	GROUP BY rb.objid, rl.objid, rl.owner_name, rl.tdno, rl.lastyearpaid, rl.lastqtrpaid
) t
GROUP BY t.billid, t.rptledgerid, t.owner_name, t.lastyearpaid, t.lastqtrpaid, t.tdno, t.fromyear, t.toyear 


[findPaidItemTotals]
select sum(amount - discount) as amount 
from rptbill_ledger_account 
{ledgersfilter}


[insertPaidOnlineItems]
INSERT INTO cashreceiptitem_rpt_online
(
	objid,
	rptreceiptid,
	rptledgerid,
	rptledgerfaasid,
	rptledgeritemid,
	year,
	qtr,
	fromqtr,
	toqtr,
	basic,
	basicint,
	basicdisc,
	basicidle,
	basicidledisc,
	basicidleint,
	sef,
	sefint,
	sefdisc,
	firecode,
	revperiod,
	basicnet,
	sefnet,
	total,
	partialled
)
SELECT
	bi.objid,
	$P{rptreceiptid},
	bi.rptledgerid,
	bi.rptledgerfaasid,
	bi.rptledgeritemid,
	bi.year,
	bi.qtr,
	bi.fromqtr,
	bi.toqtr,
	bi.basic,
	bi.basicint,
	bi.basicdisc,
	bi.basicidle,
	bi.basicidledisc,
	bi.basicidleint,
	bi.sef,
	bi.sefint,
	bi.sefdisc,
	bi.firecode,
	bi.revperiod,
	bi.basicnet,
	bi.sefnet,
	bi.total,
	partialled
FROM rptledger rl
		INNER JOIN rptbill_ledger_item bi ON rl.objid = bi.rptledgerid
WHERE rl.objid = $P{rptledgerid}
  


[insertPaidOnlineAccounts]
INSERT INTO cashreceiptitem_rpt_account
(
	objid,
	rptreceiptid,
	rptledgerid,
	revperiod,
	revtype,
	item_objid,
	amount,
	discount,
	sharetype
)
SELECT
	ba.objid,
	$P{rptreceiptid},
	ba.rptledgerid,
	ba.revperiod,
	ba.revtype,
	ba.item_objid,
	ba.amount,
	ba.discount, 
	ba.sharetype
FROM rptledger rl
		INNER JOIN rptbill_ledger_account ba ON rl.objid = ba.rptledgerid
WHERE rl.objid = $P{rptledgerid}  




[deletePaidOnlineItems]  
DELETE FROM rptbill_ledger_item WHERE rptledgerid = $P{rptledgerid} 


[deletePaidOnlineAccounts]  
DELETE FROM rptbill_ledger_account WHERE rptledgerid = $P{rptledgerid} 


[deleteRptBillLedgers]
DELETE FROM rptbill_ledger WHERE rptbillid = $P{billid}


[deleteRptBill]
DELETE FROM rptbill WHERE objid = $P{billid}



[updateLedgerYearQtrPaid]
UPDATE rptledger SET 
	lastyearpaid = $P{lastyearpaid},
	lastqtrpaid = $P{lastqtrpaid},
	partialbasic = CASE WHEN $P{partialled} = 0 THEN 0.0
						WHEN lastyearpaid = $P{toyear} AND lastqtrpaid = $P{lastqtrpaid}  AND $P{minyear} = $P{maxyear}
						THEN partialbasic + $P{partialbasic} 
						ELSE $P{partialbasic} 
					END,
	partialbasicint = CASE WHEN $P{partialled} = 0 THEN 0.0
						WHEN lastyearpaid = $P{toyear} AND lastqtrpaid = $P{lastqtrpaid}   AND $P{minyear} = $P{maxyear}
						THEN partialbasicint + $P{partialbasicint} 
						ELSE $P{partialbasicint} 
					END,
	partialbasicdisc = CASE WHEN $P{partialled} = 0 THEN 0.0
						WHEN lastyearpaid = $P{toyear} AND lastqtrpaid = $P{lastqtrpaid}   AND $P{minyear} = $P{maxyear}
						THEN partialbasicdisc + $P{partialbasicdisc} 
						ELSE $P{partialbasicdisc} 
					END,
	partialsef = CASE WHEN $P{partialled} = 0 THEN 0.0
					WHEN lastyearpaid = $P{toyear} AND lastqtrpaid = $P{lastqtrpaid}  AND $P{minyear} = $P{maxyear}
						THEN partialsef + $P{partialsef} 
						ELSE $P{partialsef} 
					END,
	partialsefint = CASE WHEN $P{partialled} = 0 THEN 0.0
					WHEN lastyearpaid = $P{toyear} AND lastqtrpaid = $P{lastqtrpaid}   AND $P{minyear} = $P{maxyear}
						THEN partialsefint + $P{partialsefint} 
						ELSE $P{partialsefint} 
					END,
	partialsefdisc = CASE WHEN $P{partialled} = 0 THEN 0.0
						WHEN lastyearpaid = $P{toyear} AND lastqtrpaid = $P{lastqtrpaid}   AND $P{minyear} = $P{maxyear}
						THEN partialsefdisc + $P{partialsefdisc} 
						ELSE $P{partialsefdisc} 
					END,
	partialledyear = $P{partialledyear},
	partialledqtr  = $P{partialledqtr},
	lastbilledyear = $P{lastbilledyear},
	lastbilledqtr  = $P{lastbilledqtr},
	nextbilldate = null
WHERE objid = $P{rptledgerid}	




[getCollectionsByCount]
SELECT 
	cr.receiptno, 
	CASE WHEN cv.objid IS NULL THEN cr.amount  ELSE 0.0 END AS amount,
	CASE WHEN cv.objid IS NULL THEN 0  ELSE 1 END AS voided
FROM cashreceipt cr 
	LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
	LEFT JOIN remittance_cashreceipt rc ON cr.objid = rc.objid 
WHERE cr.collector_objid = $P{userid}
  AND rc.objid IS NULL 
ORDER BY cr.txndate DESC   



[getItemsForPrinting]
SELECT
	t.rptledgerid,
	t.tdno,
	t.owner_name, 
	t.rputype,
	t.totalav, t.fullpin,
	t.cadastrallotno,
	t.classcode,
	t.totalareasqm,
	t.barangay, 
	t.munidistrict,
	t.provcity, 
	SUM(t.basic) AS basic, 
	SUM(t.basicdisc) AS basicdisc, 
	SUM(t.basicint) AS basicint, 
	SUM(t.basicdp) AS basicdp, 
	SUM(t.basicnet) AS basicnet,
	SUM(t.basicidle) AS basicidle,
	SUM(t.sef) AS sef,  
	SUM(t.sefdisc) AS sefdisc, 
	SUM(t.sefint) AS sefint, 
	SUM(t.sefdp) AS sefdp, 
	SUM(t.sefnet) AS sefnet,
	SUM(t.firecode) AS firecode,
	SUM(t.amount) AS amount,
	t.fromyear,
	t.toyear,
	t.partialled,
	(SELECT MIN(CASE WHEN qtr = 0 THEN fromqtr ELSE qtr END)  FROM cashreceiptitem_rpt_online WHERE rptreceiptid = t.rptreceiptid AND rptledgerid = t.rptledgerid AND YEAR = t.fromyear ) AS fromqtr,
	(SELECT MAX(CASE WHEN qtr = 0 THEN toqtr ELSE qtr END) FROM cashreceiptitem_rpt_online WHERE rptreceiptid = t.rptreceiptid AND  rptledgerid = t.rptledgerid AND YEAR = t.toyear) AS toqtr
FROM ( 
	SELECT
		cri.rptledgerid,
		cri.rptreceiptid,
		rl.owner_name, 
		rl.faasid,
		rl.tdno,
		rl.rputype,
		rl.totalav, rl.fullpin,
		rl.totalareaha * 10000 AS  totalareasqm,
		rl.cadastrallotno,
		rl.classcode,
		b.name AS barangay,
		md.name as munidistrict,
		pct.name as provcity, 
		MIN(cri.year) AS fromyear,
		MAX(cri.year) AS toyear,
		SUM(basic) AS basic,
		SUM(basicint) AS basicint,
		SUM(basicdisc) AS basicdisc,
		SUM(basicint - basicdisc) AS basicdp,
		SUM(basic + basicint - basicdisc) AS basicnet,
		SUM(basicidle - basicidledisc + basicidleint) AS basicidle,
		SUM(sef) AS sef,
		SUM(sefint) AS sefint,
		SUM(sefdisc) AS sefdisc,
		SUM(sefint - sefdisc) AS sefdp,
		SUM(sef + sefint - sefdisc) AS sefnet,
		SUM(firecode) AS firecode,
		SUM(basic + basicint - basicdisc + sef + sefint - sefdisc ) AS amount,
		MAX(cri.partialled) AS partialled 
	FROM cashreceiptitem_rpt_online cri
		INNER JOIN rptledger rl ON cri.rptledgerid = rl.objid 
		INNER JOIN sys_org b ON rl.barangayid = b.objid
		inner join sys_org md on md.objid = b.parent_objid 
		inner join sys_org pct on pct.objid = md.parent_objid
  WHERE cri.rptreceiptid = $P{rptreceiptid}
	GROUP BY 
		cri.rptreceiptid,
		cri.rptledgerid, 
		rl.owner_name, 
		rl.faasid,
		rl.tdno, 
		rl.rputype, rl.totalav, rl.fullpin, rl.totalareaha,
		rl.cadastrallotno,
		rl.classcode, b.name,
		md.name,
		pct.name
	) t
GROUP BY 
	t.rptledgerid,
	t.rptreceiptid, 
	t.owner_name, 
	t.faasid, 
	t.tdno,
	t.rputype,
	t.totalav, t.fullpin,
	t.cadastrallotno, 
	t.classcode,
	t.barangay,
	t.munidistrict,
	t.provcity, 
	t.totalareasqm,
	t.partialled,
	t.fromyear,
	t.toyear
ORDER BY t.fromyear 	
		



[getNoLedgerItemsForPrinting]	
SELECT
	t.*,
	t.*,
	(
		CONVERT(VARCHAR(4),(SELECT MIN(CASE WHEN qtr = 0 THEN fromqtr ELSE qtr END) FROM cashreceiptitem_rpt_online WHERE rptreceiptid = t.rptreceiptid AND year = t.minyear)) + 
		'Q,' +  CONVERT(VARCHAR(4),t.minyear) + ' - ' +
		CONVERT(VARCHAR(4),(SELECT MAX(CASE WHEN qtr = 0 THEN toqtr ELSE qtr END) FROM cashreceiptitem_rpt_online WHERE rptreceiptid = t.rptreceiptid AND year = t.maxyear))  + 
		'Q,' + CONVERT(VARCHAR(4),t.maxyear)
	) AS	period 
FROM (
	SELECT
		cri.rptreceiptid,
		nl.tdno,
		nl.owner_name, 
		nl.rputype,
		nl.originalav  AS totalav,
		nl.pin AS fullpin,
		nl.cadastrallotno AS cadastrallotno,
		nl.classification_objid AS classcode,
		b.name AS barangay,
		md.name as munidistrict,
		pc.name as provcity, 
		MIN(cri.year) AS minyear,
		MAX(cri.year) AS maxyear,
		SUM(basic) AS basic, 
		SUM(basicdisc) AS basicdisc, 
		SUM(basicint) AS basicint, 
		SUM(basicint - basicdisc) AS basicdp, 
		SUM(basic + basicint - basicdisc) AS basicnet,
		SUM(basicidle) AS basicidle,
		SUM(sef) AS sef,  
		SUM(sefdisc) AS sefdisc, 
		SUM(sefint) AS sefint, 
		SUM(sefint - sefdisc) AS sefdp, 
		SUM(sef + sefint - sefdisc) AS sefnet,
		SUM(firecode) AS firecode,
		SUM(basic + basicint- basicdisc + sef + sefint - sefdisc + firecode) AS amount
	FROM cashreceipt cr
		INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid
		INNER JOIN cashreceiptitem_rpt_noledger nl ON cri.objid = nl.objid 
		INNER JOIN sys_org b ON nl.barangay_objid = b.objid
		inner join sys_org md on md.objid = b.parent_objid 
		inner join sys_org pc on pc.objid = md.parent_objid 
	WHERE cr.objid = $P{objid}
	GROUP BY 
		cri.rptreceiptid,
		nl.owner_name,
		nl.tdno,
		nl.rputype,
		nl.originalav,
		nl.pin,
		nl.cadastrallotno,
		nl.classification_objid ,
		b.name,
		md.name,
		pc.name
) t




[getManualItemsForPrinting]	
SELECT
	t.*,
	(SELECT MIN(CASE WHEN qtr = 0 THEN fromqtr ELSE qtr END)  FROM cashreceiptitem_rpt_online WHERE rptreceiptid = t.rptreceiptid AND rptledgerid = t.rptledgerid AND YEAR = t.fromyear ) AS fromqtr,
	(SELECT MAX(CASE WHEN qtr = 0 THEN toqtr ELSE qtr END) FROM cashreceiptitem_rpt_online WHERE rptreceiptid = t.rptreceiptid AND  rptledgerid = t.rptledgerid AND YEAR = t.toyear) AS toqtr
FROM (
	SELECT
		rl.objid AS rptledgerid, 
		cri.rptreceiptid,
		rl.tdno,
		rl.owner_name, 
		rl.rputype,
		rl.totalav,
		rl.fullpin ,
		rl.cadastrallotno AS cadastrallotno,
		rl.classcode,
		b.name AS barangay,
		md.name as munidistrict,
		pc.name as provcity, 
		MIN(cri.year) AS fromyear,
		MAX(cri.year) AS toyear,
		SUM(basic) AS basic, 
		SUM(basicdisc) AS basicdisc, 
		SUM(basicint) AS basicint, 
		SUM(basicint - basicdisc) AS basicdp, 
		SUM(basic + basicint - basicdisc) AS basicnet,
		SUM(basicidle - basicidledisc + basicidleint) AS basicidle,
		SUM(sef) AS sef,  
		SUM(sefdisc) AS sefdisc, 
		SUM(sefint) AS sefint, 
		SUM(sefint - sefdisc) AS sefdp, 
		SUM(sef + sefint - sefdisc) AS sefnet,
		SUM(firecode) AS firecode,
		SUM(basic + basicint- basicdisc + sef + sefint - sefdisc + firecode) AS amount,
		MAX(cri.partialled) AS partialled 
	FROM cashreceipt cr
		INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid
		INNER JOIN rptledger rl ON cri.rptledgerid = rl.objid 
		INNER JOIN sys_org b ON rl.barangayid = b.objid
		inner join sys_org md on md.objid = b.parent_objid 
		inner join sys_org pc on pc.objid = md.parent_objid 
	WHERE cr.objid = $P{objid}
	GROUP BY 
		rl.objid, 
		cri.rptreceiptid,
		rl.owner_name,
		rl.tdno,
		rl.classcode, 
		rl.rputype,
		rl.totalav,
		rl.fullpin,
		rl.cadastrallotno,
		rl.classification_objid ,
		b.name,
		md.name,
		pc.name
) t






[getLedgersMinPaidYearAndQtr] 
SELECT 
	x.*,
	CASE WHEN x.qtr = 0 THEN x.fromqtr ELSE x.qtr END AS minqtr
FROM
(
	SELECT 
		t.*,
		MIN(ri.qtr) AS qtr,
		MIN(ri.fromqtr) AS fromqtr,
		MAX(ri.toqtr) AS toqtr
	FROM (
		SELECT 
			cr.rptreceiptid, 
			cr.rptledgerid, 
			c.txndate, 
			MIN(cr.year) AS minyear
		FROM cashreceiptitem_rpt_online cr 
			INNER JOIN cashreceipt c ON cr.rptreceiptid = c.objid 
		WHERE cr.rptreceiptid = $P{rptreceiptid}
		GROUP BY cr.rptreceiptid, cr.rptledgerid, c.txndate
	)t
	INNER JOIN cashreceiptitem_rpt_online ri 
		ON t.rptledgerid = ri.rptledgerid AND t.rptreceiptid = ri.rptreceiptid AND t.minyear = ri.year 
	GROUP BY t.rptreceiptid, t.rptledgerid, t.minyear, t.txndate
) x	


[findLedgerPartialInfo]
SELECT * FROM rptledger WHERE objid = $P{rptledgerid}


[findReceiptPartialInfo]
SELECT 
	basic AS partialbasic, 
	basicdisc AS partialbasicdisc, 
	basicint AS partialbasicint, 
	sef AS partialsef, 
	sefdisc AS partialsefdisc, 
	sefint  AS partialsefint,
	year AS partialledyear,
	fromqtr AS partailledqtr,
	cr.partialled 
FROM cashreceiptitem_rpt_online cr
WHERE cr.rptreceiptid = $P{rptreceiptid}
  AND cr.partialled = 1
  
[findPaidYears]
SELECT 
	MIN(year) AS minyear,
	MAX(year) AS maxyear 
FROM cashreceiptitem_rpt_online cr
WHERE cr.rptreceiptid = $P{rptreceiptid}

[findPreviousReceipt]
SELECT TOP 1 cr.objid AS rptreceiptid, cr.txndate 
FROM cashreceipt cr 
	LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
	INNER JOIN cashreceiptitem_rpt_online cro ON cr.objid = cro.rptreceiptid 
WHERE cro.rptledgerid = $P{rptledgerid}
	AND cr.txndate < $P{txndate}
	AND cv.objid IS NULL 
ORDER BY cr.objid, cr.txndate DESC 






[resetVoidedLedgerInfo]
UPDATE rptledger SET 
	nextbilldate = null,
	lastyearpaid = $P{toyear},
	lastqtrpaid = $P{toqtr},
	partialbasic = $P{partialbasic},
	partialbasicint = $P{partialbasicint},
	partialbasicdisc = $P{partialbasicdisc},
	partialsef = $P{partialsef},
	partialsefint = $P{partialsefint},
	partialsefdisc = $P{partialsefdisc},
	partialledyear = $P{partialledyear},
	partialledqtr  = $P{partialledqtr},
	lastbilledyear = $P{lastbilledyear},
	lastbilledqtr  = $P{lastbilledqtr}
WHERE objid = $P{rptledgerid}	



[getSummarizedCashReceiptItems]
SELECT
	rb.objid AS item_objid,
	rb.code AS item_code, 
	rb.title AS item_title,
	rb.fund_objid AS item_fund_objid, rb.fund_code AS item_fund_code, rb.fund_title AS item_fund_title,
	SUM(ba.amount) AS amount,
	SUM(ba.discount) AS discount
FROM rptledger rl
	INNER JOIN rptbill_ledger_account ba ON rl.objid = ba.rptledgerid
	INNER JOIN itemaccount rb ON ba.item_objid = rb.objid 
WHERE ${filter}
GROUP BY 
	rb.objid, rb.code, rb.title, rb.fund_objid,
	rb.fund_code, rb.fund_title


[findRevenueItemById]
SELECT 
	objid, code, title, 
	fund_objid, fund_code, fund_title
FROM itemaccount 
WHERE objid = $P{objid}


[updateLedgerItemPaymentInfo]
update rli set 
	rli.paidqtr = case 
					when cro.partialled = 1 then cro.fromqtr - 1
					when rli.year < rl.lastyearpaid then 4 
					else cro.toqtr 
				end,
	rli.paid = case 
					when cro.partialled = 1 then 0
					when rli.year < rl.lastyearpaid then 1
					when rl.partialbasic > 0 or rl.partialsef > 0 then 0
					when cro.toqtr = 4 then 1 
					else 0 
				end 
from rptledger rl, rptledgeritem rli, cashreceiptitem_rpt_online cro 				
where rl.objid = $P{rptledgerid}
  and rl.objid = rli.rptledgerid 
  and rli.objid = cro.rptledgeritemid 
  and cro.rptreceiptid = $P{rptreceiptid}
  and rli.year <= rl.lastyearpaid 
  and rli.paid = 0 



[getOnlinePaidItems]
select 
	cro.rptledgeritemid, cro.year, cro.fromqtr, cro.toqtr, cro.partialled  
from cashreceiptitem_rpt_online cro 
where cro.rptreceiptid = $P{rptreceiptid}
  and cro.rptledgerid = $P{rptledgerid}
order by cro.year, cro.toqtr 

[voidItemPaidInfo]
update rptledgeritem set 
	paidqtr = $P{fromqtr} - 1,
	paid = 0 
where objid = $P{rptledgeritemid}

[clearItemPaidInfo]
update rptledgeritem set 
	paidqtr = 0,
	paid = 0 
where objid = $P{rptledgeritemid}


[findLedgerItemByYear]
SELECT objid 
FROM rptledgeritem 
WHERE rptledgerid = $P{rptledgerid}
  and year = $P{year} 

  
[clearFaasRestrictions]
update f set 
	f.restrictionid = null
from faas f, rptledger rl
where rl.objid = $P{rptledgerid}	
  and rl.faasid = f.objid 

  
