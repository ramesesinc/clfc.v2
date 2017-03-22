[deleteRptBillLedgerItem]
DELETE FROM rptbill_ledger_item WHERE rptledgerid = $P{rptledgerid}

[deleteRptBillLedgerAccount]
DELETE FROM rptbill_ledger_account WHERE rptledgerid = $P{rptledgerid}


[findOpenLedgerById]
SELECT 
	rl.objid,
	rl.lastyearpaid,
	rl.lastqtrpaid,
	rl.firstqtrpaidontime,
	rl.qtrlypaymentpaidontime,
	rl.undercompromise,
	rl.faasid, 
	rl.tdno,
	rl.taxpayer_objid, 
	e.name AS taxpayer_name, 
	e.address_text AS taxpayer_address, 
	rl.owner_name,
	null as administrator_name,
	rl.rputype,
	rl.fullpin,
	rl.totalareaha,
	rl.totalareaha * 10000 AS totalareasqm,
	rl.totalav,
	rl.taxable,
	b.name AS barangay,
	b.objid AS barangayid,
	rl.cadastrallotno,
	rl.barangayid,
	rl.classcode,
	rl.partialbasic, 
	rl.partialbasicint,
	rl.partialbasicdisc, 
	rl.partialsef, 
	rl.partialsefint,
	rl.partialsefdisc,
	CASE
		WHEN (rl.partialbasic > 0 OR rl.partialsef > 0) AND rl.lastqtrpaid = 4 THEN rl.lastyearpaid
		WHEN rl.lastqtrpaid = 4 THEN rl.lastyearpaid + 1 
		ELSE rl.lastyearpaid 
	END	AS fromyear,
	CASE
		WHEN (rl.partialbasic > 0 OR rl.partialsef > 0) THEN rl.lastqtrpaid 
		WHEN rl.lastqtrpaid = 4 THEN 1 
		ELSE rl.lastqtrpaid + 1 
	END AS fromqtr,
	CASE 
		WHEN rl.nextbilldate IS NULL OR rl.nextbilldate <= GETDATE() THEN 1 
		WHEN rl.lastbilledyear IS NULL OR rl.lastbilledqtr IS NULL THEN 1 
		WHEN rl.lastbilledyear <> $P{billtoyear} OR rl.lastbilledqtr <> $P{billtoqtr} THEN 1
		ELSE 0
	END AS recalcbill,
	rl.nextbilldate AS expirydate
FROM rptledger rl 
	INNER JOIN barangay b ON rl.barangayid = b.objid 
	INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
WHERE rl.objid = $P{rptledgerid}
 AND rl.state = 'APPROVED'
 AND rl.taxable = 1 
 AND ( rl.lastyearpaid < $P{billtoyear} OR rl.partialbasic > 0.0 OR (rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid < $P{billtoqtr}))


[getLedgerFaases]
SELECT rlf.* 
FROM rptledger rl
	INNER JOIN rptledgerfaas rlf ON rl.objid = rlf.rptledgerid
WHERE rl.objid =  $P{rptledgerid}
  AND rlf.state = 'APPROVED' 
ORDER BY rlf.fromyear   


[findBillByBarcode]
SELECT * FROM rptbill  WHERE barcode = $P{barcodeid}


[findCollectionTypeByBarcodeKey]
SELECT * FROM collectiontype WHERE barcodekey = $P{barcodekey}
  

[getBillLedgers]  
SELECT * FROM rptbill_ledger WHERE rptbillid = $P{objid}



[findBrgyTaxAccountMapping]
SELECT 
	bam.*, 
	prior.title AS basicprioracct_title, 
	priorint.title AS basicpriorintacct_title,
	prev.title AS basicprevacct_title, 
	prevint.title AS basicprevintacct_title,
	curr.title AS basiccurracct_title, 
	currint.title AS basiccurrintacct_title,
	adv.title AS basicadvacct_title
FROM brgy_taxaccount_mapping bam 
	INNER JOIN itemaccount prior ON bam.basicprioracct_objid = prior.objid 
	INNER JOIN itemaccount priorint ON bam.basicpriorintacct_objid = priorint.objid 
	INNER JOIN itemaccount prev ON bam.basicprevacct_objid = prev.objid 
	INNER JOIN itemaccount prevint ON bam.basicprevintacct_objid = prevint.objid 
	INNER JOIN itemaccount curr ON bam.basiccurracct_objid = curr.objid 
	INNER JOIN itemaccount currint ON bam.basiccurrintacct_objid = currint.objid 
	INNER JOIN itemaccount adv ON bam.basicadvacct_objid = adv.objid 
WHERE bam.barangayid = $P{barangayid}


[findMunicipalityTaxAccountMapping]
SELECT 
	mm.*,
	m.name,
	m.indexno,
	basicprior.title AS basicprioracct_title, 
	basicpriorint.title AS basicpriorintacct_title,
	basicprev.title AS basicprevacct_title, 
	basicprevint.title AS basicprevintacct_title,
	basiccurr.title AS basiccurracct_title, 
	basiccurrint.title AS basiccurrintacct_title,
	basicadv.title AS basicadvacct_title,
	basicidlecurr.title AS basicidlecurracct_title, 
	basicidlecurrint.title AS basicidlecurrintacct_title,
	basicidleprev.title AS basicidleprevacct_title, 
	basicidleprevint.title AS basicidleprevintacct_title,
	basicidleadv.title AS basicidleadvacct_title,

	sefprior.title AS sefprioracct_title, 
	sefpriorint.title AS sefpriorintacct_title,
	sefprev.title AS sefprevacct_title, 
	sefprevint.title AS sefprevintacct_title,
	sefcurr.title AS sefcurracct_title, 
	sefcurrint.title AS sefcurrintacct_title,
	sefadv.title AS sefadvacct_title
FROM municipality_taxaccount_mapping mm
	LEFT JOIN municipality m ON mm.lguid = m.objid 
	LEFT JOIN itemaccount basicprior ON mm.basicprioracct_objid = basicprior.objid 
	LEFT JOIN itemaccount basicpriorint ON mm.basicpriorintacct_objid = basicpriorint.objid 
	LEFT JOIN itemaccount basicprev ON mm.basicprevacct_objid = basicprev.objid 
	LEFT JOIN itemaccount basicprevint ON mm.basicprevintacct_objid = basicprevint.objid 
	LEFT JOIN itemaccount basiccurr ON mm.basiccurracct_objid = basiccurr.objid 
	LEFT JOIN itemaccount basiccurrint ON mm.basiccurrintacct_objid = basiccurrint.objid 
	LEFT JOIN itemaccount basicadv ON mm.basicadvacct_objid = basicadv.objid 
	LEFT JOIN itemaccount basicidlecurr ON mm.basicidlecurracct_objid = basicidlecurr.objid 
	LEFT JOIN itemaccount basicidlecurrint ON mm.basicidlecurrintacct_objid = basicidlecurrint.objid 
	LEFT JOIN itemaccount basicidleprev ON mm.basicidleprevacct_objid = basicidleprev.objid 
	LEFT JOIN itemaccount basicidleprevint ON mm.basicidleprevintacct_objid = basicidleprevint.objid 
	LEFT JOIN itemaccount basicidleadv ON mm.basicidleadvacct_objid = basicidleadv.objid 
	
	LEFT JOIN itemaccount sefprior ON mm.sefprioracct_objid = sefprior.objid 
	LEFT JOIN itemaccount sefpriorint ON mm.sefpriorintacct_objid = sefpriorint.objid 
	LEFT JOIN itemaccount sefprev ON mm.sefprevacct_objid = sefprev.objid 
	LEFT JOIN itemaccount sefprevint ON mm.sefprevintacct_objid = sefprevint.objid 
	LEFT JOIN itemaccount sefcurr ON mm.sefcurracct_objid = sefcurr.objid 
	LEFT JOIN itemaccount sefcurrint ON mm.sefcurrintacct_objid = sefcurrint.objid 
	LEFT JOIN itemaccount sefadv ON mm.sefadvacct_objid = sefadv.objid 
WHERE mm.lguid = $P{lguid}



[findProvinceTaxAccountMapping]
SELECT 
	mm.*,
	basicprior.title AS basicprioracct_title, 
	basicpriorint.title AS basicpriorintacct_title,
	basicprev.title AS basicprevacct_title, 
	basicprevint.title AS basicprevintacct_title,
	basiccurr.title AS basiccurracct_title, 
	basiccurrint.title AS basiccurrintacct_title,
	basicadv.title AS basicadvacct_title,
	basicidlecurr.title AS basicidlecurracct_title, 
	basicidlecurrint.title AS basicidlecurrintacct_title,
	basicidleprev.title AS basicidleprevacct_title, 
	basicidleprevint.title AS basicidleprevintacct_title,
	basicidleadv.title AS basicidleadvacct_title,

	sefprior.title AS sefprioracct_title, 
	sefpriorint.title AS sefpriorintacct_title,
	sefprev.title AS sefprevacct_title, 
	sefprevint.title AS sefprevintacct_title,
	sefcurr.title AS sefcurracct_title, 
	sefcurrint.title AS sefcurrintacct_title,
	sefadv.title AS sefadvacct_title
FROM province_taxaccount_mapping mm
	LEFT JOIN itemaccount basicprior ON mm.basicprioracct_objid = basicprior.objid 
	LEFT JOIN itemaccount basicpriorint ON mm.basicpriorintacct_objid = basicpriorint.objid 
	LEFT JOIN itemaccount basicprev ON mm.basicprevacct_objid = basicprev.objid 
	LEFT JOIN itemaccount basicprevint ON mm.basicprevintacct_objid = basicprevint.objid 
	LEFT JOIN itemaccount basiccurr ON mm.basiccurracct_objid = basiccurr.objid 
	LEFT JOIN itemaccount basiccurrint ON mm.basiccurrintacct_objid = basiccurrint.objid 
	LEFT JOIN itemaccount basicadv ON mm.basicadvacct_objid = basicadv.objid 
	LEFT JOIN itemaccount basicidlecurr ON mm.basicidlecurracct_objid = basicidlecurr.objid 
	LEFT JOIN itemaccount basicidlecurrint ON mm.basicidlecurrintacct_objid = basicidlecurrint.objid 
	LEFT JOIN itemaccount basicidleprev ON mm.basicidleprevacct_objid = basicidleprev.objid 
	LEFT JOIN itemaccount basicidleprevint ON mm.basicidleprevintacct_objid = basicidleprevint.objid 
	LEFT JOIN itemaccount basicidleadv ON mm.basicidleadvacct_objid = basicidleadv.objid 
	
	LEFT JOIN itemaccount sefprior ON mm.sefprioracct_objid = sefprior.objid 
	LEFT JOIN itemaccount sefpriorint ON mm.sefpriorintacct_objid = sefpriorint.objid 
	LEFT JOIN itemaccount sefprev ON mm.sefprevacct_objid = sefprev.objid 
	LEFT JOIN itemaccount sefprevint ON mm.sefprevintacct_objid = sefprevint.objid 
	LEFT JOIN itemaccount sefcurr ON mm.sefcurracct_objid = sefcurr.objid 
	LEFT JOIN itemaccount sefcurrint ON mm.sefcurrintacct_objid = sefcurrint.objid 
	LEFT JOIN itemaccount sefadv ON mm.sefadvacct_objid = sefadv.objid 


[getBilledLedgers]
SELECT 
	rl.objid,
	rl.lastyearpaid,
	rl.lastqtrpaid,
	rl.faasid, 
	rl.nextbilldate,
	rl.tdno,
	rl.rputype,
	rl.fullpin,
	rl.totalareaha,
	rl.totalareaha * 10000 AS totalareasqm,
	rl.totalav,
	rl.owner_name, 
	b.name AS barangay,
	rl.cadastrallotno,
	rl.barangayid,
	rl.classcode,
	rl.lastbilledyear,
	rl.lastbilledqtr,
	CASE WHEN rl.partialbasic > 0 THEN 1 ELSE 0 END AS partialled,
	CASE WHEN rl.nextbilldate <= GETDATE() THEN 1 ELSE 0 END AS expired,
	CASE 
		WHEN rl.nextbilldate <= GETDATE() THEN 1 
		WHEN rl.partialbasic > 0 THEN 1
		WHEN rl.lastbilledyear IS NULL OR rl.lastbilledqtr IS NULL THEN 1 
		WHEN rl.lastbilledyear <> $P{billtoyear} OR rl.lastbilledqtr <> $P{billtoqtr} THEN 1
		ELSE 0
	END AS recalcbill
FROM rptledger rl 
  INNER JOIN barangay b ON rl.barangayid = b.objid 
  INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
WHERE rl.objid IN (
	SELECT rl.objid 
	FROM rptledger rl 
	WHERE ${filters}
	 AND rl.state = 'APPROVED'
	 AND rl.taxable = 1 
	 AND (rl.lastyearpaid < $P{billtoyear} 
				OR ( rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid < $P{billtoqtr})
				OR ( rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid = $P{billtoqtr} AND rl.partialbasic > 0)
	 )

	UNION 

	SELECT rl.objid 
	FROM propertypayer pp
		inner join propertypayer_item ppi on pp.objid = ppi.parentid
		inner join rptledger rl on ppi.rptledger_objid = rl.objid 
	WHERE ${ppfilters}
	 AND rl.state = 'APPROVED'
	 AND rl.taxable = 1 
	 AND (rl.lastyearpaid < $P{billtoyear} 
				OR ( rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid < $P{billtoqtr})
				OR ( rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid = $P{billtoqtr} AND rl.partialbasic > 0)
	 )
)
ORDER BY rl.tdno  


[getUnpaidPropertiesForPayment]
SELECT ${mssqlcountfilter}
	rl.objid,
	rl.lastyearpaid,
	rl.lastqtrpaid,
	rl.faasid, 
	rl.nextbilldate,
	rl.tdno,
	rl.rputype,
	rl.fullpin,
	rl.totalareaha,
	rl.totalareaha * 10000 AS totalareasqm,
	rl.totalav,
	b.name AS barangay,
	rl.cadastrallotno,
	rl.barangayid,
	rl.classcode,
	rl.lastbilledyear,
	rl.lastbilledqtr,
	CASE WHEN rl.partialbasic > 0 THEN 1 ELSE 0 END AS partialled,
	CASE WHEN rl.nextbilldate <= GETDATE() THEN 1 ELSE 0 END AS expired,
	CASE 
		WHEN rl.nextbilldate <= GETDATE() THEN 1 
		WHEN rl.partialbasic > 0 THEN 1
		WHEN rl.lastbilledyear IS NULL OR rl.lastbilledqtr IS NULL THEN 1 
		WHEN rl.lastbilledyear <> $P{billtoyear} OR rl.lastbilledqtr <> $P{billtoqtr} THEN 1
		ELSE 0
	END AS recalcbill
FROM rptledger rl 
  INNER JOIN barangay b ON rl.barangayid = b.objid 
  INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
  LEFT JOIN rptledger_compromise rc ON rl.objid = rc.rptledgerid 
WHERE rl.objid IN (
	SELECT rl.objid 
	FROM rptledger rl 
	WHERE ${filters}
	 AND rl.state = 'APPROVED'
	 AND rl.taxable = 1 
	 AND (rl.lastyearpaid < $P{billtoyear} 
				OR ( rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid < $P{billtoqtr})
				OR ( rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid = $P{billtoqtr} AND rl.partialbasic > 0)
	 )

	UNION 

	SELECT rl.objid 
	FROM propertypayer pp
		inner join propertypayer_item ppi on pp.objid = ppi.parentid
		inner join rptledger rl on ppi.rptledger_objid = rl.objid 
	WHERE ${ppfilters}
	 AND rl.state = 'APPROVED'
	 AND rl.taxable = 1 
	 AND (rl.lastyearpaid < $P{billtoyear} 
				OR ( rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid < $P{billtoqtr})
				OR ( rl.lastyearpaid = $P{billtoyear} AND rl.lastqtrpaid = $P{billtoqtr} AND rl.partialbasic > 0)
	 )
)
 AND ( rc.objid IS NULL OR rc.state <> 'APPROVED' ) 
ORDER BY rl.tdno  


[getIncentivesByLedgerId]
SELECT *
FROM rpttaxincentive_item
WHERE rptledgerid = $P{rptledgerid}


[findExpiry]
SELECT expirydate,expirytype 
FROM rptexpiry 
WHERE iqtr=$P{qtr} AND iyear=$P{year}
AND expirydate >= $P{date}
ORDER BY expirydate ASC



[updateLedgerNextBillDate]
UPDATE rptledger SET
	nextbilldate = $P{nextbilldate},
	lastbilledyear = $P{toyear},
	lastbilledqtr = $P{toqtr}
WHERE objid = $P{rptledgerid}



[getOpenLedgerItems]
select 
	rli.objid, rli.rptledgerid, rl.faasid, rli.rptledgerfaasid, rli.objid AS rptledgeritemid, 
	rli.year, 
	0 as qtr, 
	1 as fromqtr, 4 as toqtr,
	rli.paidqtr, 
	rli.av AS originalav, 
	case 
		when rli.year = $P{billtoyear} and rli.paidqtr < 4 then round(rli.av / 4, 2) * ($P{billtoqtr} - rli.paidqtr )
		when rli.year = $P{billtoyear} then rli.av - (round(rli.av / 4, 2) * 3) 

		when rli.paidqtr < 4 then round(rli.av / 4, 2) * (4 - rli.paidqtr )  
		else rli.av - (round(rli.av / 4, 2) * 3) 
  	end as av,
  	case 
		when rli.year = $P{billtoyear} and rli.paidqtr < 4 then round(rli.basicav / 4, 2) * ($P{billtoqtr} - rli.paidqtr )
		when rli.year = $P{billtoyear} then rli.basicav - (round(rli.basicav / 4, 2) * 3) 

		when rli.paidqtr < 4 then round(rli.basicav / 4, 2) * (4 - rli.paidqtr )  
		else rli.basicav - (round(rli.basicav / 4, 2) * 3) 
  	end as basicav,
  	case 
		when rli.year = $P{billtoyear} and rli.paidqtr < 4 then round(rli.sefav / 4, 2) * ($P{billtoqtr} - rli.paidqtr )
		when rli.year = $P{billtoyear} then rli.sefav - (round(rli.sefav / 4, 2) * 3) 

		when rli.paidqtr < 4 then round(rli.sefav / 4, 2) * (4 - rli.paidqtr )  
		else rli.sefav - (round(rli.sefav / 4, 2) * 3) 
  	end as sefav,
	rl.txntype_objid,
	rli.classification_objid,
	rli.actualuse_objid,
	lf.backtax,
	lf.reclassed,
	lf.idleland,
	0.0 as basic,
	0.0 as basicint,
	0.0 as basicdisc,
	0.0 as basicidle,
	0.0 as basicidleint,
	0.0 as basicidledisc,
	0.0 as sef,
	0.0 as sefint,
	0.0 as sefdisc,
	0.0 as firecode,
	0.0 as basicnet,
	0.0 as sefnet,
	0.0 as total,
	0 as partialled,
	rli.year AS effectiveyear,
	rli.taxdifference,
	(select effectivityyear from faas where objid = rl.faasid)  as effectivityyear 
from rptledger rl 
	inner join rptledgeritem rli on rl.objid = rli.rptledgerid
	inner join rptledgerfaas lf on rli.rptledgerfaasid = lf.objid 
where rl.objid = $P{rptledgerid}
  and rli.year <= $P{billtoyear}
  and rli.paid = 0
  and lf.taxable = 1 
order by rli.year 



[getLandDetails]
select pc.objid as classification_objid, ld.area
from faas f 
	inner join landdetail ld on f.rpuid = ld.landrpuid 
	inner join landassesslevel lal on ld.actualuse_objid = lal.objid 
	inner join propertyclassification pc on lal.classification_objid = pc.objid 
where f.objid = $P{faasid}

[getLedgerFaasesByYear]
select fromyear, fromqtr, toyear, toqtr, assessedvalue 
from rptledgerfaas 
where rptledgerid = $P{rptledgerid}
 and $P{fromyear} >= fromyear 
 and ($P{toyear} <= toyear  or toyear = 0)
 and state = 'APPROVED'


[findCompromiseByLedger]
select objid, state, txnno  
from rptledger_compromise 
where rptledgerid = $P{objid}