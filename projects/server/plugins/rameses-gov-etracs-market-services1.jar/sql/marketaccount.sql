[getList]
SELECT b.* FROM 
(SELECT m.*, 
	ru.code AS rentalunit_code,
	ru.name AS rentalunit_name,
	mk.name AS rentalunit_market_name,
	ms.name AS rentalunit_section_name,
	ms.title AS rentalunit_section_title
FROM marketaccount m
INNER JOIN market_rentalunit ru ON m.rentalunit_objid=ru.objid
INNER JOIN market mk ON ru.marketid=mk.objid
INNER JOIN marketsection ms ON ru.section_objid=ms.objid
) b
where b.owner_name like $P{searchtext} 

[approve]
UPDATE marketaccount SET state='APPROVED' WHERE objid=$P{objid}