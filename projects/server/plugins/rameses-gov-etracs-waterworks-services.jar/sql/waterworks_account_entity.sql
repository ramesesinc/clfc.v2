[getList]
SELECT
*
FROM waterworks_account_meter
WHERE objid LIKE $P{objid}

[getEntity]
SELECT
*
FROM waterworks_account_entity
WHERE objid = $P{objid}