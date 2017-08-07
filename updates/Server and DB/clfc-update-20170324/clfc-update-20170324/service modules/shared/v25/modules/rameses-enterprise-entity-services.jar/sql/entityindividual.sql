[getList]
SELECT e.*, ei.gender, ei.birthdate 
FROM entity e 
	INNER JOIN entityindividual ei ON e.objid=ei.objid 
WHERE e.entityname LIKE $P{searchtext}  
ORDER BY e.entityname 

[getLookup]
SELECT e.*, ei.gender, ei.birthdate 
FROM entityindividual ei 
INNER JOIN entity e ON e.objid=ei.objid 
WHERE e.entityname LIKE $P{searchtext}  
ORDER BY e.entityname 

[getMatchList]
SELECT ei.objid, e.entityno, ei.lastname, ei.firstname, ei.middlename, 
ei.birthdate, ei.gender, e.name, e.address_objid, e.address_text, ea.type AS address_type 
FROM entityindividual ei
INNER JOIN entity e ON e.objid=ei.objid
LEFT JOIN entity_address ea ON ea.objid=e.address_objid
WHERE ei.lastname LIKE $P{lastname}


[findPhoto]
SELECT photo FROM entityindividual WHERE objid=$P{objid}

[updatePhoto]
UPDATE entityindividual SET photo=$P{photo}, thumbnail=$P{thumbnail} WHERE objid=$P{objid}

[findThumbnail]
SELECT thumbnail FROM entityindividual WHERE objid=$P{objid}


[getReligionList]
SELECT DISTINCT religion FROM entityindividual WHERE religion LIKE $P{searchtext}

[getCitizenshipList]
SELECT DISTINCT citizenship FROM entityindividual WHERE citizenship LIKE $P{searchtext} 

[getProfessionList]
SELECT DISTINCT profession FROM entityindividual WHERE profession LIKE $P{searchtext} 


[updateName]
UPDATE entityindividual 
SET firstname=$P{firstname}, 
lastname=$P{lastname}, 
middlename=$P{middlename} 
WHERE objid=$P{objid}