[getTmcrList]
SELECT
	b.name AS barangay, pc.code AS classcode, 
	f.state,  f.memoranda, f.owner_name, f.owner_address, r.rputype, f.tdno, f.titleno, 
	rp.cadastrallotno, rp.section, rp.surveyno, rp.blockno, 
	r.fullpin, r.totalareasqm, r.totalareasqm, r.totalav, r.totalmv 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE rp.barangayid = $P{barangayid} 
  AND f.state IN ('CURRENT', 'CANCELLED')
  AND rp.section = $P{section} 
ORDER BY fullpin   


[getTmcrList2]
SELECT
	b.name as barangay, rp.barangayid, rp.section, rp.blockno, 
	r.fullpin, rp.surveyno, f.titleno, r.totalareasqm,
	 pc.code, f.owner_name, f.tdno, f.rpuid, r.rputype 
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE rp.barangayid = $P{barangayid} 
  AND f.state IN ('CURRENT', 'CANCELLED')
  AND rp.section LIKE $P{section} 
ORDER BY r.fullpin



[getCurrentTmcrList]
SELECT ${tmcrfields}
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE rp.barangayid = $P{barangayid} 
  AND f.state = 'CURRENT'
  AND rp.section = $P{section} 
ORDER BY r.fullpin, f.tdno 


[getCancelledTmcrFaases]
SELECT ${tmcrfields}
FROM previousfaas pf 
	INNER JOIN faas f on pf.prevfaasid = f.objid 
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
	INNER JOIN barangay b ON rp.barangayid = b.objid 
WHERE pf.faasid = $P{faasid}
  and f.state = 'CANCELLED'
ORDER BY r.fullpin, f.tdno DESC   
