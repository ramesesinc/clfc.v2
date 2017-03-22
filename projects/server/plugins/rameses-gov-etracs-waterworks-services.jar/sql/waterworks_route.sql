[getList]
SELECT
	*
FROM waterworks_route
WHERE name LIKE $P{searchtext}

[getRouteLookup]
SELECT
*
FROM waterworks_route
WHERE name LIKE $P{searchtext} 