[getList]
select a.* from (
	SELECT 
		c.objid ,c.state, c.txndate ,c.receiptno,c.receiptdate,c.txnmode,c.payer_objid,c.payer_name,
		c.paidby,c.paidbyaddress,c.collector_objid,c.collector_name,c.collector_title,
		c.totalcash,c.totalnoncash,c.cashchange,c.totalcredit,c.org_objid,c.org_name,c.formno,
		c.series,c.controlid,c.collectiontype_objid,c.collectiontype_name,c.user_objid,c.user_name,
		c.remarks,c.subcollector_objid,c.subcollector_name,c.subcollector_title,c.formtype, c.stub,
		CASE WHEN v.receiptid IS NULL THEN 0 ELSE 1 END AS voided, 	
		CASE WHEN v.receiptid IS NULL THEN c.amount ELSE 0 END AS amount 
	FROM cashreceipt c 
	LEFT JOIN cashreceipt_void v ON c.objid=v.receiptid
	LEFT JOIN remittance_cashreceipt r ON c.objid=r.objid
	WHERE c.collector_objid = $P{collectorid}
		and c.state = 'POSTED' 
		and r.objid is null 
		and ( c.receiptno LIKE $P{searchtext} 
		OR c.paidby LIKE $P{searchtext}
		OR c.payer_name LIKE $P{searchtext} )

	union 

	SELECT 
		c.objid ,c.state, c.txndate ,c.receiptno,c.receiptdate,c.txnmode,c.payer_objid,c.payer_name,
		c.paidby,c.paidbyaddress,c.collector_objid,c.collector_name,c.collector_title,
		c.totalcash,c.totalnoncash,c.cashchange,c.totalcredit,c.org_objid,c.org_name,c.formno,
		c.series,c.controlid,c.collectiontype_objid,c.collectiontype_name,c.user_objid,c.user_name,
		c.remarks,c.subcollector_objid,c.subcollector_name,c.subcollector_title,c.formtype, c.stub,
		CASE WHEN v.receiptid IS NULL THEN 0 ELSE 1 END AS voided, 	
		CASE WHEN v.receiptid IS NULL THEN c.amount ELSE 0 END AS amount 
	FROM cashreceipt c 
	LEFT JOIN cashreceipt_void v ON c.objid=v.receiptid
	LEFT JOIN remittance_cashreceipt r ON c.objid=r.objid
	WHERE c.subcollector_objid = $P{collectorid}
		and c.state = 'DELEGATED' 
		and r.objid is null 
		and ( c.receiptno LIKE $P{searchtext} 
		OR c.paidby LIKE $P{searchtext}
		OR c.payer_name LIKE $P{searchtext} )
	
 ) a
ORDER BY a.formno, a.receiptno   



