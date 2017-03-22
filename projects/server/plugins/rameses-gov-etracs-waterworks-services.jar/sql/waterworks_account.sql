[getList]
SELECT
	a.*,
	r.name as route
FROM waterworks_account a
LEFT JOIN waterworks_account_route wr ON wr.acctid = a.objid
LEFT JOIN waterworks_route r ON wr.routeid = r.objid
WHERE a.acctno LIKE $P{searchtext}

[findAccount]
SELECT
	a.*,
	r.name as waterworks_route
FROM waterworks_account a
LEFT JOIN waterworks_account_route wr ON wr.acctid = a.objid
LEFT JOIN waterworks_route r ON wr.routeid = r.objid
WHERE a.objid = $P{objid}

[getApplicationLookup]
SELECT
	a.*,
	m.objid as meterid,
	m.serialno
FROM waterworks_account a
LEFT JOIN waterworks_meter m ON a.meterid = m.objid
WHERE a.acctno LIKE $P{searchtext}

[findMeter]
SELECT
*
FROM waterworks_meter
WHERE objid = $P{objid}

[findRoute]
SELECT
r.*
FROM waterworks_account a
INNER JOIN waterworks_account_route ar ON a.objid = ar.acctid
INNER JOIN waterworks_route r ON ar.routeid = r.objid 
WHERE a.objid = $P{objid}

[getBill]
SELECT
	a.acctno,
	a.entity_name AS name,
	a.entity_address AS address,
	a.basicrate,
	m.serialno,
	m.brand,
	m.capacity,
	m.size,
	mr.previousreading,
	mr.currentreading,
	mr.consumed,
	mr.amount,
	mr.acctid
FROM waterworks_account a
INNER JOIN waterworks_meter m ON a.meterid = m.objid 
INNER JOIN waterworks_meter_reading mr ON m.objid = mr.meterid 
INNER JOIN waterworks_account_route ar ON a.objid = ar.acctid
WHERE a.state = 'ACTIVE'
AND mr.month = $P{month}
AND mr.year = $P{year}
AND ar.routeid LIKE $P{routeid}

[getAccountByRoute]
SELECT
	a.*,
	m.*
FROM waterworks_account a 
INNER JOIN waterworks_meter m ON a.meterid = m.objid
INNER JOIN waterworks_account_route ar ON a.objid = ar.acctid
WHERE a.state = 'ACTIVE'
AND ar.routeid LIKE $P{routeid}
AND a.objid NOT IN (SELECT acctid FROM waterworks_meter_reading WHERE month = $P{month} AND year = $P{year})


