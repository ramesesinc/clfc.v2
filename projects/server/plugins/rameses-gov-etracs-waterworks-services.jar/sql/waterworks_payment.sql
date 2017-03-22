[getList]
SELECT
p.*
FROM waterworks_payment p
LEFT JOIN waterworks_account_payment ap ON p.objid = ap.paymentid
WHERE ap.acctid LIKE $P{acctid}