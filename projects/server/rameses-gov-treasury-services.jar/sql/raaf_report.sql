[getReportData]
select * from ( 
  select 
    'A' as idx, '' as type, xx.controlid, af.formtype, afi.afid as formno, af.denomination, af.serieslength, 
    afi.respcenter_objid as ownerid, afi.respcenter_name as name, afi.respcenter_type as respcentertype, 
    (case when afi.respcenter_type='AFO' then 0 else 1 end) as categoryindex, 
    xx.prevendingstartseries, xx.prevendingendseries, 
    xx.receivedstartseries, xx.receivedendseries, 
    xx.beginstartseries, xx.beginendseries, 
    xx.issuedstartseries, xx.issuedendseries, xx.issuednextseries, 
    xx.endingstartseries, xx.endingendseries,
    case 
      when xx.beginstartseries > 0 then xx.beginstartseries 
      when xx.issuedstartseries > 0 then xx.issuedstartseries 
      when xx.receivedstartseries > 0 then xx.receivedstartseries 
      else xx.endingstartseries 
    end as sortseries, 
    afi.afid 
  from ( 
    select t1.*, 
      (select max(endingstartseries) from af_inventory_detail 
        where controlid=t1.controlid and lineno < t1.minlineno) as prevendingstartseries, 
      (select max(endingendseries) from af_inventory_detail 
        where controlid=t1.controlid and lineno < t1.minlineno) as prevendingendseries  
    from ( 
      select 
        controlid, min(lineno) as minlineno, max(lineno) as maxlineno,     
        min(receivedstartseries) as receivedstartseries, 
        max(receivedendseries) as receivedendseries, 
        min(beginstartseries) as beginstartseries, 
        max(beginendseries) as beginendseries, 
        min(issuedstartseries) as issuedstartseries, 
        max(issuedendseries) as issuedendseries, 
        max(issuedendseries)+1 as issuednextseries, 
        max(endingstartseries) as endingstartseries, 
        max(endingendseries) as endingendseries   
      from af_inventory_detail 
      where year(refdate)=$P{year} and month(refdate)=$P{month}  
      group by controlid
    )t1  
  )xx inner join af_inventory afi on xx.controlid=afi.objid 
      inner join af on afi.afid = af.objid 
  where afi.respcenter_objid = $P{collectorid}  
)xx 
order by xx.categoryindex, xx.formno, xx.sortseries  


[getReportDataByRef]
select * from ( 
  select 
    'A' as idx, '' as type, xx.controlid, af.formtype, afi.afid as formno, af.denomination, af.serieslength, 
    afi.respcenter_objid as ownerid, afi.respcenter_name as name, afi.respcenter_type as respcentertype, 
    (case when afi.respcenter_type='AFO' then 0 else 1 end) as categoryindex, 
    xx.receivedstartseries, xx.receivedendseries, 
    xx.beginstartseries, xx.beginendseries, 
    xx.issuedstartseries, xx.issuedendseries, xx.issuednextseries, 
    xx.endingstartseries, xx.endingendseries,
    case 
      when xx.beginstartseries > 0 then xx.beginstartseries 
      when xx.issuedstartseries > 0 then xx.issuedstartseries  
      when xx.receivedstartseries > 0 then xx.receivedstartseries  
      else xx.endingstartseries  
    end as sortseries, 
    afi.afid  
  from ( 
    select 
      afd.controlid, min(afd.lineno) as minlineno, max(afd.lineno) as maxlineno,   
      min(afd.receivedstartseries) as receivedstartseries, 
      max(afd.receivedendseries) as receivedendseries, 
      min(afd.beginstartseries) as beginstartseries, 
      max(afd.beginendseries) as beginendseries, 
      min(afd.issuedstartseries) as issuedstartseries, 
      max(afd.issuedendseries) as issuedendseries, 
      max(afd.issuedendseries)+1 as issuednextseries, 
      max(afd.endingstartseries) as endingstartseries, 
      max(afd.endingendseries) as endingendseries 
    from remittance_af raf 
      inner join af_inventory_detail afd on raf.objid=afd.objid 
    where raf.remittanceid = $P{refid} 
    group by afd.controlid 
    union 
    select 
      afd.controlid, min(afd.lineno) as minlineno, max(afd.lineno) as maxlineno,   
      min(afd.receivedstartseries) as receivedstartseries, 
      max(afd.receivedendseries) as receivedendseries, 
      min(afd.beginstartseries) as beginstartseries, 
      max(afd.beginendseries) as beginendseries, 
      min(afd.issuedstartseries) as issuedstartseries, 
      max(afd.issuedendseries) as issuedendseries, 
      max(afd.issuedendseries)+1 as issuednextseries, 
      max(afd.endingstartseries) as endingstartseries, 
      max(afd.endingendseries) as endingendseries 
    from liquidation_remittance lr 
      inner join remittance_af raf on lr.objid = raf.remittanceid 
      inner join af_inventory_detail afd on raf.objid=afd.objid 
    where lr.liquidationid = $P{refid} 
    group by afd.controlid 
  )xx 
    inner join af_inventory afi on xx.controlid=afi.objid 
    inner join af on afi.afid = af.objid 
)xx 
order by xx.categoryindex, xx.formno, xx.sortseries  
