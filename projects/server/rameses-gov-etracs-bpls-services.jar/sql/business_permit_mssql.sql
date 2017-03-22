[getList]
SELECT DISTINCT bb.*  
FROM 
(
	SELECT bp.*, 
		b.businessname AS business_businessname, 
		b.address_text AS business_address_text, 
		b.bin AS business_bin,
		b.owner_name AS business_owner_name,
		ba.apptype
	FROM business_permit bp
	INNER JOIN business b ON bp.businessid=b.objid 
	INNER JOIN business_application ba ON bp.applicationid=ba.objid
	WHERE bp.expirydate >= $P{currentdate} 
    AND b.owner_name LIKE $P{searchtext} 

    UNION 

	SELECT bp.*, 
		b.businessname AS business_businessname, 
		b.address_text AS business_address_text, 
		b.bin AS business_bin,
		b.owner_name AS business_owner_name,
		ba.apptype
	FROM business_permit bp
	INNER JOIN business b ON bp.businessid=b.objid 
	INNER JOIN business_application ba ON bp.applicationid=ba.objid
	WHERE bp.expirydate >= $P{currentdate} 
    AND b.businessname LIKE $P{searchtext} 

    UNION 

	SELECT bp.*, 
		b.businessname AS business_businessname, 
		b.address_text AS business_address_text, 
		b.bin AS business_bin,
		b.owner_name AS business_owner_name,
		ba.apptype
	FROM business_permit bp
	INNER JOIN business b ON bp.businessid=b.objid 
	INNER JOIN business_application ba ON bp.applicationid=ba.objid
	WHERE bp.expirydate >= $P{currentdate} 
    AND b.bin LIKE $P{searchtext} 

) bb


[getExpiredList]
SELECT DISTINCT bb.*  
FROM 
(
	SELECT bp.*, 
		b.businessname AS business_businessname, 
		b.address_text AS business_address_text, 
		b.bin AS business_bin,
		b.owner_name AS business_owner_name,
		ba.apptype
	FROM business_permit bp
	INNER JOIN business b ON bp.businessid=b.objid
	INNER JOIN business_application ba ON bp.applicationid=ba.objid
	WHERE bp.expirydate < $P{currentdate}
	AND b.owner_name LIKE $P{searchtext} 

	UNION 

	SELECT bp.*, 
		b.businessname AS business_businessname, 
		b.address_text AS business_address_text, 
		b.bin AS business_bin,
		b.owner_name AS business_owner_name,
		ba.apptype
	FROM business_permit bp
	INNER JOIN business b ON bp.businessid=b.objid
	INNER JOIN business_application ba ON bp.applicationid=ba.objid
	WHERE bp.expirydate < $P{currentdate}
	AND b.businessname LIKE $P{searchtext} 

	UNION

	SELECT bp.*, 
		b.businessname AS business_businessname, 
		b.address_text AS business_address_text, 
		b.bin AS business_bin,
		b.owner_name AS business_owner_name,
		ba.apptype
	FROM business_permit bp
	INNER JOIN business b ON bp.businessid=b.objid
	INNER JOIN business_application ba ON bp.applicationid=ba.objid
	WHERE bp.expirydate < $P{currentdate}
	AND b.bin LIKE $P{searchtext} 

) bb


[findPermitByApplication]
select 
	bp.*, ba.appno, ba.apptype, ba.ownername, ba.owneraddress, ba.tradename, 
	ba.businessaddress, b.bin, b.pin, b.address_objid, b.owner_address_objid, 
	(SELECT photo FROM entityindividual WHERE objid=b.owner_objid) AS photo, 
	ba.parentapplicationid  
from business_application ba 
	inner join business_permit bp on ba.objid=bp.applicationid  
	inner join business b on bp.businessid=b.objid 
where ba.objid=$P{applicationid} 
	and bp.activeyear=b.activeyear 
	and bp.state='ACTIVE' 


[findPermitForReport]
select 
	bp.*, ba.appno, ba.apptype, ba.ownername, ba.owneraddress, ba.tradename, 
	ba.businessaddress, b.bin, b.pin, b.address_objid, b.owner_address_objid, ba.parentapplicationid, 
	(select apptype from business_application where objid=ba.parentapplicationid) as parentapptype,  
	(select photo from entityindividual where objid=b.owner_objid) AS photo 
from ( 
	select objid as applicationid from business_application 
	where objid=$P{applicationid} and 1=$P{showall} 
	union 
	select objid as applicationid from business_application 
	where parentapplicationid=$P{applicationid} and 1=$P{showall}  
	union 
	select objid as applicationid from business_application 
	where objid=$P{applicationid} and 0=$P{showall} 
	union 
	select parentapplicationid as applicationid from business_application 
	where objid=$P{applicationid} and 0=$P{showall} 
)xx 
	inner join business_application ba on xx.applicationid=ba.objid 
	inner join business_permit bp on ba.objid=bp.applicationid 
	inner join business b on bp.businessid=b.objid 
where bp.activeyear=b.activeyear and bp.state='ACTIVE' 
order by bp.version desc

[findBusinessAddress]
SELECT * FROM business_address WHERE objid=$P{objid} 


[getApplicationLOBs]
select distinct 
	alob.lobid, alob.name, alob.assessmenttype  
from ( 
	select objid as applicationid from business_application 
	where objid=$P{applicationid} and 1=$P{showall} 
	union 
	select objid as applicationid from business_application 
	where parentapplicationid=$P{applicationid} and 1=$P{showall} 
	union 
	select objid as applicationid from business_application 
	where objid=$P{applicationid} and 0=$P{showall} 
	union 
	select parentapplicationid as applicationid from business_application 
	where objid=$P{applicationid} and 0=$P{showall} 
)xx 
	inner join business_permit bp on xx.applicationid=bp.applicationid 
	inner join business_application_lob alob on bp.applicationid=alob.applicationid 
	inner join business b on bp.businessid=b.objid  
where bp.activeyear=b.activeyear and bp.state='ACTIVE' 


[getApplicationPayments]
select * from ( 
	select objid from business_payment 
	where applicationid=$P{applicationid} 
	union 
	select p.objid from business_application a 
		inner join business_payment p on a.parentapplicationid=p.applicationid 
	where a.objid=$P{applicationid} 
	union 
	select p.objid from business_application a 
		inner join business_payment p on a.objid=p.applicationid 
	where a.parentapplicationid=$P{applicationid} 
)x 
	inner join business_payment p on x.objid=p.objid 
where p.voided=0 
order by p.refdate, p.refno 


[updatePlateno]
UPDATE business_permit SET plateno=$P{plateno} WHERE objid=$P{objid}


[findPermitCount]
select count(*) as icount from business_permit 
where businessid=$P{businessid} and activeyear=$P{activeyear}  
