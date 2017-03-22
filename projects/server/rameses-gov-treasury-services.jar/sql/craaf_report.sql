[getCraafData]
select * from ( 
  select 
    'A' as idx, '' as type, xx.controlid, af.formtype as aftype, afi.afid, af.denomination, af.serieslength, 
    afi.respcenter_objid as ownerid, afi.respcenter_name as name, afi.respcenter_type as respcentertype, 
    (case when afi.respcenter_type='AFO' then 0 else 1 end) as categoryindex, afi.currentstub as stubno, 
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
    end as sortseries 
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
      where year(refdate)=2015 and month(refdate)=7  
      group by controlid 
    )t1  
  )xx inner join af_inventory afi on xx.controlid=afi.objid 
      inner join af on afi.afid = af.objid 
)xx 
order by xx.afid, xx.sortseries  


[getAFOUnforwardedAF]
select  
  afd.controlid,
  afd.endingstartseries as startseries, 
  afd.endingendseries as endseries  
from af_inventory_detail afd
  inner join af_inventory ai on ai.objid = afd.controlid 
where ai.respcenter_type ='AFO' 
  and ai.currentseries <= ai.endseries 
  and afd.lineno = ai.currentlineno
  and afd.refdate < $P{firstdate} 
