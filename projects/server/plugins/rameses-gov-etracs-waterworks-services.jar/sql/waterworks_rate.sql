[getList]
SELECT
*
FROM waterworks_rate

[getRateItems]
SELECT
i.*
FROM waterworks_rate_item i
WHERE i.parentid = $P{objid}
ORDER BY i.from