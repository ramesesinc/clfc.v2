[getList]
SELECT * FROM entity_address WHERE parentid=$P{objid} 

[removeEntityAddress]
delete from entity_address where parentid=$P{parentid} 