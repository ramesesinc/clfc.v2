[getReceipts]
select 
   (select count(*) from remittance_cashreceipt where objid=cr.objid) as remitted, 
   (select count(*) from cashreceipt_void where receiptid=cr.objid) as voided, 
   cr.formno, cr.controlid, cr.series, cr.receiptno, cr.amount, af.formtype, 
   cr.collector_objid as collectorid, cr.objid as receiptid,  
   case 
      when af.denomination is null then 1 
      when af.denomination = 0 then 1 
      else af.denomination 
   end as denomination 
from cashreceipt cr 
   inner join af on cr.formno = af.objid 
where controlid = $P{controlid} 
order by cr.series 


[updateReceiptSeries]
update cashreceipt set 
   series=$P{currentseries} 
where objid=$P{objid} 
   and series=$P{series} 


[getUnremittedAFs]
select xx.controlid as objid, xx.formno 
from ( 
   select formno, controlid from cashreceipt cr 
   where collector_objid = $P{collectorid}  
      and objid not in (select objid from remittance_cashreceipt where objid=cr.objid) 
   union 
   select formno, controlid from cashreceipt cr 
   where subcollector_objid = $P{collectorid} 
      and objid not in (select objid from remittance_cashreceipt where objid=cr.objid) 
)xx 
order by xx.formno, xx.controlid 

