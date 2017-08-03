[getList]
SELECT 
	e.objid, e.entityno, e.name, e.address_text, e.address_objid, e.type	 
FROM entity e 
WHERE e.entityname LIKE $P{searchtext}  
ORDER BY e.entityname 

[getLookup]
SELECT 
	e.objid, e.entityno, e.name, e.address_text, e.address_objid, e.type
FROM entity e
WHERE e.entityname LIKE $P{searchtext} 
 ${filter} 
ORDER BY e.entityname 
