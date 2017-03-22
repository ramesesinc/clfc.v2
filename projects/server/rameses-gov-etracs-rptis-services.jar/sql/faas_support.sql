[updateDataCaptureFlag]
update faas set datacapture = $P{datacapture} where objid = $P{objid}

[deleteTasks]
DELETE FROM faas_task WHERE refid = $P{objid}


[findOnlinePaymentByFaas]
select f.objid, rl.objid as rptledgerid  
from faas f 
	inner join rptledger rl on f.objid = rl.faasid 
	inner join cashreceiptitem_rpt_online cro on rl.objid = cro.rptledgerid
where f.objid = $P{objid}

[findLedgerByFaasId]
select objid from rptledger where faasid = $P{objid}

[findExistingImprovements]
select f.objid
from faas f 
	inner join rpu lr on f.rpuid = lr.objid 
	inner join rpu ir on lr.realpropertyid = ir.realpropertyid
where f.objid = $P{objid}
and lr.rputype = 'land' 
and ir.rputype <> 'land' 

