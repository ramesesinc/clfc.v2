
/*===============================================================================
**
** MIGRATE ENTITY FIRST, then run rpt migration script (groovy)              
** 
================================================================================*/

/* --------------------------- */
INSERT INTO faasannotation 
  (objid, state, annotationtype_objid, faasid, txnno, txndate, fileno, orno, ordate, oramount, memoranda)
select
  f.objid, 
  f.docstate as state, 
  ft.objid as annotationtype_objid, 
  f.faasid, 
  f.docno as txnno, 
  f.dtsubmitted as txndate, 
  f.fileno, 
  f.orno, 
  f.ordate, 
  f.oramount, 
  f.memoranda 
from ligao_etracs..faasannotation f
  inner join ligao_etracs..faasannotationtype ft on f.doctype = ft.annotationtype 
go





INSERT INTO rptledger (objid, state, faasid, nextbilldate, lastyearpaid, lastqtrpaid, firstqtrpaidontime, qtrlypaymentavailed, qtrlypaymentpaidontime, lastitemyear, lastreceiptid, barangayid, advancebill, lastbilledyear, lastbilledqtr, partialbasic, partialbasicint, partialbasicdisc, partialsef, partialsefint, partialsefdisc, partialledyear, partialledqtr, taxpayer_objid, fullpin, tdno, cadastrallotno, rputype, txntype_objid, classcode, totalav, totalmv, totalareaha, taxable, owner_name, prevtdno, classification_objid, titleno, undercompromise)
select 
  rl.objid, 
  rl.docstate as state, 
  rl.faasid, 
  null as nextbilldate, 
  rl.lastyearpaid, 
  rl.lastqtrpaid, 
  rl.firstqtrpaidontime, 
  0 as qtrlypaymentavailed, 
  isnull(rl.quarterlyinstallmentpaidontime,0) as qtrlypaymentpaidontime, 
  0 as lastitemyear, 
  0 as lastreceiptid, 
  rp.barangayid, 
  0 as advancebill, 
  0 as lastbilledyear, 
  0 as lastbilledqtr, 
  rl.partialbasic, 
  rl.partialbasicint, 
  0 as partialbasicdisc, 
  rl.partialsef, 
  rl.partialsefint, 
  0 as partialsefdisc, 
  0 as partialledyear, 
  0 as partialledqtr, 
  f.taxpayer_objid, 
  f.fullpin, 
  f.tdno, 
  rp.cadastrallotno, 
  r.rputype, 
  f.txntype_objid, 
  pc.code as classcode, 
  r.totalav, 
  r.totalmv, 
  r.totalareaha, 
  r.taxable, 
  f.owner_name, 
  f.prevtdno, 
  r.classification_objid, 
  f.titleno, 
  0 as undercompromise
from ligao_etracs..rptledger rl 
  inner join etracs254_ligao..faas f on rl.faasid = f.objid 
  inner join etracs254_ligao..rpu r on f.rpuid = r.objid 
  inner join etracs254_ligao..realproperty rp on f.realpropertyid = rp.objid 
  inner join etracs254_ligao..propertyclassification pc on r.classification_objid = pc.objid 
go 


INSERT INTO rptledgerfaas (objid, state, rptledgerid, faasid, tdno, txntype_objid, classification_objid, actualuse_objid, taxable, backtax, fromyear, fromqtr, toyear, toqtr, assessedvalue, systemcreated, reclassed, idleland) 
select
  rli.objid, 
  rli.docstate, 
  rli.parentid as rptledgerid, 
  rli.faasid, 
  rli.tdno, 
  rli.txntype as txntype_objid, 
  rli.classid as classification_objid, 
  rli.actualuseid as actualuse_objid, 
  rli.taxable, 
  rli.backtax, 
  rli.fromyear, 
  1 as fromqtr, 
  rli.toyear, 
  4 as toqtr, 
  rli.assessedvalue, 
  rli.systemcreated, 
  0 as reclassed, 
  0 as idleland
from ligao_etracs..rptledgeritem rli 
  inner join etracs254_ligao..rptledger rl on rli.parentid = rl.objid 
where exists(select * from faas where objid = rli.faasid)
go 




INSERT INTO rptledger_credit (objid, rptledgerid, type, refno, refdate, payorid, paidby_name, paidby_address, collector, postedby, postedbytitle, dtposted, fromyear, fromqtr, toyear, toqtr, basic, basicint, basicdisc, basicidle, sef, sefint, sefdisc, firecode, amount, collectingagency)
select
  p.objid, 
  p.rptledgerid, 
  p.mode as type, 
  p.receiptno as refno, 
  p.receiptdate as refdate, 
  r.payorid, 
  isnull(r.payorname,'-') as paidby_name, 
  isnull(r.payoraddress,'-') as  paidby_address, 
  isnull(r.collectorname, '-') as collector, 
  case when r.objid is null then p.capturedby else r.collectorname end as postedby, 
  case when r.objid is null then '-' else r.collectortitle end as postedbytitle, 
  case when r.objid is null then p.receiptdate else r.txndate end as dtposted, 
  p.fromyear, 
  p.fromqtr, 
  p.toyear, 
  p.toqtr, 
  p.basic, 
  p.basicint, 
  p.basicdisc, 
  0 as basicidle, 
  p.sef, 
  p.sefint, 
  p.sefdisc, 
  0.0 firecode, 
  (p.basic + p.basicint - p.basicdisc + p.sef + p.sefint - p.sefdisc) as amount, 
  'CTO' as collectingagency
from ligao_etracs..rptpayment p 
  left join ligao_etracs..receiptlist r on p.receiptid = r.objid
where exists(select * from rptledger where objid = p.rptledgerid)
go 



update faas set ryordinanceno = '', ryordinancedate = '' 
go 

