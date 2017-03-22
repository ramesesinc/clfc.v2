[getList]
SELECT
*
FROM waterworks_account_route
WHERE acctid = $P{objid}

[updateRoute]
UPDATE waterworks_account_route
SET routeid = $P{routeid}
WHERE acctid = $P{acctid}