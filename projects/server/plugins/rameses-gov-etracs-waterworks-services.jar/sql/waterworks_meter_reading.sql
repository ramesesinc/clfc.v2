[getList]
SELECT
	r.*,
	a.acctno,
	a.name,
	m.serialno
FROM waterworks_meter_reading r
LEFT JOIN waterworks_account a ON r.acctid = a.objid
LEFT JOIN waterworks_meter m ON a.meterid = m.objid

[findLatestReading]
SELECT
*
FROM waterworks_meter_reading
WHERE meterid = $P{meterid}
ORDER BY year DESC, month DESC
LIMIT 1;

[findReading]
SELECT
*
FROM waterworks_meter_reading
WHERE meterid = $P{meterid}
AND month = $P{month}
AND year = $P{year}

[findReadingByAccount]
SELECT
*
FROM waterworks_meter_reading
WHERE acctid = $P{acctid}
AND month = $P{month}
AND year = $P{year}

[getRecordsByDate]
SELECT
	*
FROM waterworks_meter_reading
WHERE year = $P{year}
AND month = $P{month}
AND meterid = $P{meterid}

[getMeterReading]
SELECT
*
FROM waterworks_meter_reading
WHERE acctid LIKE $P{acctid}

[getReadingListByBarangay]
SELECT
a.objid AS acctid,
a.acctno,
a.name,
a.meterid,
m.serialno,
(
	SELECT
	objid
	FROM
	waterworks_meter_reading r
	WHERE r.meterid = a.meterid
	AND r.month = $P{month}
	AND r.year = $P{year}
	LIMIT 1
) AS readingid
FROM waterworks_account a
INNER JOIN waterworks_meter m ON a.meterid = m.objid
INNER JOIN waterworks_account_address ad ON a.objid = ad.parentid
WHERE ad.barangayid = $P{barangayid}
HAVING readingid IS NULL
