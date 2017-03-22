[getList]
SELECT
*
FROM waterworks_schedule
ORDER BY duedate

[findSchedule]
SELECT
*
FROM waterworks_schedule
WHERE year = $P{year}
AND month = $P{month}

[getScheduleLookup]
SELECT
*
FROM waterworks_schedule
WHERE month LIKE $P{searchtext}