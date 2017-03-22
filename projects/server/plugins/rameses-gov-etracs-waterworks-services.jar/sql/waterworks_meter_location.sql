[findLocation]
SELECT
*
FROM waterworks_meter_location
WHERE parentid = $P{objid}