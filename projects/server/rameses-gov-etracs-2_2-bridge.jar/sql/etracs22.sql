[getList]
select 
  b.objid, b.tradename as businessname, b.tradename, b.businessaddress as address, 
   e.entityname, e.objid AS ownerid, e.entityname AS ownername, e.entityaddress AS owneraddress,   
   ba.objid as applicationd, ba.iyear as activeyear, ba.txntype 
from ( 
   select b.objid from business b 
      inner join entity e on b.taxpayerid = e.objid 
   where b.tradename like $P{tradename} and e.entityname like $P{ownername} and 1=$P{searchlevel} 
   union 
   select b.objid from entity e 
      inner join business b on e.objid = b.taxpayerid 
   where e.entityname like $P{ownername} and 2=$P{searchlevel}  
)xx 
   inner join business b on xx.objid = b.objid 
   inner join entity e on b.taxpayerid = e.objid 
   inner join bpapplication ba on b.applicationid = ba.objid 
order by ba.iyear desc, b.tradename 


[findInfo]
SELECT
   ba.objid AS objid,
   b.objid AS oldbusinessid,
   b.tradename AS business_businessname,
   b.tradename AS business_tradename,
   b.businessaddress AS business_address_text,
   CASE ba.txntype
      WHEN 'RENEWAL' THEN 'RENEW'
      ELSE ba.txntype
   END as apptype,
   ba.objid AS applicationid, 
   ba.iyear AS activeyear,
   e.entityname as business_owner_name,
   e.objid as business_owner_oldid,
   e.entityno as business_owner_entityno,
   e.entityaddress as business_owner_address_text,
   e.info, 
   ba.iyear as appyear,
   CASE b.organization 
      WHEN 'SINGLE PROPRIETORSHIP' THEN 'SING'
      WHEN 'CORPORATION' THEN 'CORP'
      WHEN 'COOPERATIVE' THEN 'COOP'
      WHEN 'ASSOCIATION' THEN 'ASSO'
      WHEN 'PROPRIETORSHIP' THEN 'PART'   
      WHEN 'FOUNDATION' THEN 'FOUND'   
      WHEN 'GOVERNMENT' THEN 'GOV'  
      WHEN 'RELIGIOUS' THEN 'REL'
      ELSE 'NA'   
   END AS business_orgtype, 
   (
      select min(iyear) from bpapplication 
      where businessid = b.objid and docstate not in ('DRAFT') 
   ) as yearstarted 
FROM business b 
   INNER JOIN bpapplication ba ON b.applicationid = ba.objid 
   INNER JOIN entity e ON b.taxpayerid = e.objid 
WHERE b.objid = $P{objid} 


[getLobs]
select 
   lob.objid as oldlobid, lob.name as oldname, 
   case 
      when ba.txntype = 'RENEW' then 'RENEW' 
      when ba.txntype = 'RENEWAL' then 'RENEW'
      when ba.txntype = 'ADDLOB' then 'NEW' 
      when ba.txntype = 'NEW' then 'NEW' 
      else null 
   end as assessmenttype, ba.txntype, 
   c.lob_objid as lobid, c.lob_name as name 
from bpapplication ba 
   inner join business b ON ba.businessid = b.objid 
   inner join bploblisting bl ON ba.objid = bl.applicationid 
   inner join lob ON bl.lobid = lob.objid 
   left join etracs25_capture_lob c ON lob.objid = c.oldlob_objid 
where ba.businessid = $P{businessid} and ba.iyear = $P{activeyear}  
   and ba.txntype IN ('NEW','RENEW','RENEWAL','ADDLOB') 
   and ba.docstate IN ('ACTIVE') 


[getApplications]
SELECT objid, txntype, taxfees, receivables, credits 
FROM bpapplication  
where businessid = $P{businessid} and iyear =  $P{activeyear}  
   and txntype IN ('NEW','RENEW','RENEWAL','ADDLOB') 
   and docstate IN ('ACTIVE') 


[findAccount]
SELECT account_objid, account_title 
FROM etracs25_capture_account 
WHERE oldaccount_objid=$P{oldacctid}


[getReceivables]
select 
   xx.assessmenttype, b.objid AS businessid,  
   CASE ia.systype  
     WHEN 'CHARGE' THEN 'OTHERCHARGE' 
     WHEN 'REG_FEE' THEN 'REGFEE' 
     WHEN 'OTHER_FEE' THEN 'OTHERCHARGE' 
     WHEN 'BUSINESS_TAX' THEN 'TAX' 
     WHEN 'TAX' THEN 'TAX' 
   END AS taxfeetype,
   xx.iyear as yearapplied, 
   lob.objid AS oldlob_objid, lob.name AS oldlob_name,
   clob.lob_objid, clob.lob_name, 
   ia.objid AS oldaccount_objid, ia.accttitle AS oldaccount_title, 
   ca.account_objid, ca.account_title,
   xx.amount, xx.amtpaid, 0.0 as surcharge, 0.0 as interest, 0.0 as discount, 
   xx.iyear as year, null as lastreceiptid, xx.lastqtrpaid 
from ( 
   select  
      xx.applicationid, xx.businessid, xx.iyear, max(xx.lastqtrpaid) as lastqtrpaid, 
      xx.lobid, xx.acctid, sum(xx.amount) as amount, sum(xx.amtpaid) as amtpaid, 
      xx.assessmenttype  
   from ( 
      select br.*, 
         case 
            when ba.txntype = 'RENEW' then 'RENEW' 
            when ba.txntype = 'RENEWAL' then 'RENEW'
            when ba.txntype = 'ADDLOB' then 'NEW' 
            when ba.txntype = 'NEW' then 'NEW' 
            else null 
         end as assessmenttype,
         case 
            when br.amtpaid >= br.amount then br.iqtr else null 
         end as lastqtrpaid 
      from bpapplication ba 
         inner join bpreceivable br on ba.objid = br.applicationid 
      where ba.businessid = $P{businessid} and ba.iyear = $P{activeyear} 
         and ba.txntype IN ('NEW','RENEW','RENEWAL','ADDLOB') 
         and ba.docstate IN ('ACTIVE') 
   )xx 
   group by xx.applicationid, xx.businessid, xx.iyear, xx.lobid, xx.acctid, xx.assessmenttype 
   order by xx.lobid, xx.acctid, xx.iyear  
)xx 
   inner join business b ON xx.businessid=b.objid 
   inner join incomeaccount ia ON xx.acctid=ia.objid 
   inner join lob lob ON xx.lobid=lob.objid 
   left join etracs25_capture_lob clob ON clob.oldlob_objid=lob.objid 
   left join etracs25_capture_account ca ON ia.objid=ca.oldaccount_objid 
