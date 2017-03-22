[getList]
SELECT
*
FROM waterworks_meter
WHERE serialno LIKE $P{searchtext}
OR brand LIKE $P{searchtext}
ORDER BY serialno DESC,
brand, size

[findMeterInfo]
SELECT
*
FROM waterworks_meter
WHERE objid LIKE $P{objid}

[getMeterLookup]
SELECT
	*
FROM waterworks_meter
WHERE (serialno LIKE $P{searchtext}
OR brand LIKE $P{searchtext})
AND objid NOT IN (SELECT meterid FROM waterworks_account)