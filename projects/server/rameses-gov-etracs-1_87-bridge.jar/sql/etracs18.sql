[getList]
select 
   b.objid, b.tradename as businessname, b.tradename, b.businessAddress as address, b.year as activeyear,
   p.name, p.objid AS ownerid, p.name AS ownername, p.primaryaddress AS owneraddress
from ( 
   select b.objid from business b 
      inner join payer p on b.taxpayerid = p.objid 
   where b.tradename like $P{tradename} and p.name like $P{ownername} and 1=$P{searchlevel} 
   union 
   select b.objid from payer p 
      inner join business b on p.objid = b.taxpayerid 
   where p.name like $P{ownername} and 2=$P{searchlevel} 
)xx 
   inner join business b on xx.objid = b.objid 
   inner join payer p on b.taxpayerid = p.objid 
order by b.year desc, b.tradename 


[findInfo]
select 
   ba.objid, 
   b.objid as oldbusinessid, 
   b.tradename AS business_businessname, 
   b.tradename AS business_tradename, 
   b.businessAddress AS business_address_text, 
   CASE b.applicationtype 
       WHEN 'RENEWAL' THEN 'RENEW' 
       ELSE b.applicationtype 
   END AS apptype, 
   ba.objid AS applicationid, 
   aa.year AS activeyear,
   ( 
      select min(xaa.year) from bpapplication xba 
         inner join abstractbpapplication xaa on xba.objid=xaa.objid 
      where xba.businessid = ba.businessid and xaa.state not in ('draft') 
   ) as yearstarted, 
   p.name AS business_owner_name,
   p.objid AS business_owner_oldid,
   p.lastname AS business_owner_lastname,
   p.firstname AS business_owner_firstname,
   p.middlename AS business_owner_middlename,
   p.birthdate AS business_owner_birthdate,
   p.gender AS business_owner_gender,
   p.taxpayerno AS business_owner_entityno,
   p.primaryaddress AS business_owner_address_text,
   CASE b.organizationType 
       WHEN 'SINGLE' THEN 'SING'
       WHEN 'CORP' THEN 'CORP'
       WHEN 'COOP' THEN 'COOP'
       WHEN 'ASSO' THEN 'ASSO'
       WHEN 'PART' THEN 'PART' 
       WHEN 'FOUN' THEN 'FOUND'   
       WHEN 'GOV' THEN 'GOV'   
       WHEN 'REL' THEN 'REL'
       ELSE 'NA'   
   END AS business_orgtype, 
   b.taxpayerid 
from bpapplication ba 
   inner join abstractbpapplication aa on ba.objid = aa.objid 
   inner join business b on ba.businessid = b.objid 
   inner join payer p on b.taxpayerid = p.objid 
where ba.businessid = $P{objid} 
   and aa.year = $P{activeyear} 
   and aa.state IN ('ACTIVE','APPROVED')  
order by aa.dtapplied desc 


[getLobs]
select 
   lob.objid AS oldlobid, lob.name AS oldname, 
   case ba.type 
      when 'RENEWAL' then 'RENEW'
      else ba.type
   end AS assessmenttype, 
   cl.lob_objid AS lobid, cl.lob_name AS name 
from bpapplication ba 
   inner join abstractbpapplication aa on ba.objid = aa.objid 
   inner join bpapplication_lob bl on aa.objid = bl.bpapplication_objid 
   inner join lob on bl.lines_objid = lob.objid 
   left join etracs25_capture_lob cl ON lob.objid = cl.oldlob_objid 
where ba.objid = $P{applicationid} 


[getReceivables]
select * from ( 
   select xx.*, 
      case 
          when xx.taxfeetype='TAX' then 0 
          when xx.taxfeetype='REGFEE' and xx.oldlob_objid is not null then 1  
          when xx.taxfeetype='REGFEE' then 2 
          when xx.taxfeetype='OTHERCHARGE' then 3 
          else 4 
      end as sortorder 
   from ( 
      select 
         br.objid, 
         CASE br.applicationType  
            WHEN 'RENEWAL' THEN 'RENEW'
            ELSE br.applicationType 
         END AS assessmenttype, 
         b.objid AS businessid, 
         CASE btf.acctType 
            WHEN 'CHARGE' THEN 'OTHERCHARGE' 
            WHEN 'REG_FEE' THEN 'REGFEE' 
            WHEN 'OTHER_FEE' THEN 'OTHERCHARGE' 
            WHEN 'BUSINESS_TAX' THEN 'TAX' 
            WHEN 'TAX' THEN 'TAX' 
         END AS taxfeetype, 
         YEAR(btf.dtapplied) AS yearapplied,
         lob.objid AS oldlob_objid, lob.name AS oldlob_name,
         clob.lob_objid, clob.lob_name, 
         ia.objid AS oldaccount_objid, ia.title AS oldaccount_title, 
         ca.account_objid, ca.account_title,
         br.amount, br.amtpaid, btf.surcharge, btf.interest, btf.discount, 
         btf.year, br.lastreceiptid, br.lastqtrpaid  
      from bptaxfee btf 
         inner join bpreceivable br on btf.objid = br.taxfeeid 
         inner join business b on br.businessid = b.objid 
         inner join incomeaccount ia on btf.acctid = ia.objid 
         left join lob on btf.lobid = lob.objid 
         left join etracs25_capture_lob clob ON clob.oldlob_objid = lob.objid 
         left join etracs25_capture_account ca ON ia.objid = ca.oldaccount_objid
      where btf.parentid = $P{applicationid} 
   )xx 
)xx 
order by xx.sortorder, xx.oldlob_name, xx.oldaccount_title 


[getAssessmentInfos]
select 
  bi.parentid as applicationid, ba.businessid, aa.year as activeyear, 
  case 
    when v.name='GROSS' then 'assessmentinfo' 
    when v.name='CAPITAL' then 'assessmentinfo' 
    when v.name='NO_OF_SQM' then 'appinfo' 
    when v.name='NO_EMPLOYEES' then 'appinfo' 
    else null 
  end as type, 
  case 
    when v.name='GROSS' then 'GROSS' 
    when v.name='CAPITAL' then 'CAPITAL' 
    when v.name='NO_OF_SQM' then 'AREA_SQM' 
    when v.name='NO_EMPLOYEES' then 'NUM_EMPLOYEE' 
    else null 
  end as attribute_objid, 
  case 
    when v.name='GROSS' then 'GROSS' 
    when v.name='CAPITAL' then 'CAPITAL' 
    when v.name='NO_OF_SQM' then 'AREA_SQM' 
    when v.name='NO_EMPLOYEES' then 'NUM_EMPLOYEE' 
    else null 
  end as attribute_name,
  lob.objid as oldlobid, lob.name as oldlobname, 
  bi.doublevalue as decimalvalue, 
  bi.integervalue as intvalue, 
  bi.booleanvalue  as boolvalue, 
  bi.stringvalue 
from bpapplicationinfo bi 
  inner join lob on bi.lobid = lob.objid 
  inner join abstractvariable v on bi.variableid = v.objid  
  inner join bpapplication ba on bi.parentid = ba.objid 
  inner join abstractbpapplication aa on ba.objid = aa.objid 
where bi.parentid = $P{applicationid}  
  and v.name in ('GROSS','CAPITAL','NO_OF_SQM','NO_EMPLOYEES') 


[getLandTaxPaymentInfo]
select 
   receiptNo as orno,
   receiptDate as ordate,
   sum( basic + basicInterest + basicPrevious + basicIntPrevious + basicPrior + basicIntPrior - basicDiscount + 
       sef + sefInterest + sefPrevious + sefIntPrevious + sefPrior + sefIntPrior - sefDiscount + advancePayment )  as oramount,
   CONCAT(MIN(fromQtr), 'Q,', fromYear, ' - ', MAX(toQtr), 'Q,', toYear) as period
from abstractrptcredit 
where ledgerid = $P{rptledgerid}
  and    (toYear = $P{year} OR toYear > $P{year} )
  AND (toYear > $P{year} OR (toYear = $P{year} AND toQtr <= $P{qtr}))  
group by refno, ordate, fromYear, toYear   

