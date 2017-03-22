[getAFInventoryReport]
SELECT bt.*
FROM (
   SELECT 
        t.objid, 
        t.afid,
        t.costperstub, 
        t.minlineno, 
        CASE WHEN t.receivedstartseries IS NULL THEN (((md.endingendseries - md.endingstartseries) + 1) / a.qty) ELSE (t.qtyreceived / a.qty) END AS qtyreceived,
        CASE WHEN t.receivedstartseries IS NULL THEN (((md.endingendseries - md.endingstartseries) + 1) / a.qty) * t.costperstub ELSE (t.qtyreceived / a.qty) * t.costperstub END AS qtyreceivedcost,
        CASE WHEN t.receivedstartseries IS NULL
          THEN CONCAT(  md.endingstartseries,  ' - ' ,  md.endingendseries)
          ELSE CONCAT(  t.receivedstartseries, ' - ' ,  t.receivedendseries)
         END AS receivedseries,
        (t.qtyissued / a.qty) AS qtyissued, 
        ((t.qtyissued / a.qty) * t.costperstub) AS qtyissuedcost,
        CONCAT( t.issuedstartseries, ' - ' , t.issuedendseries ) AS issuedseries, 
        CASE WHEN xd.endingstartseries IS NULL THEN 0 ELSE (((xd.endingendseries - xd.endingstartseries ) +1) / a.qty) END AS qtyending, 
        ((((xd.endingendseries - xd.endingstartseries ) +1) / a.qty) * t.costperstub) AS qtyendingcost,
        CONCAT( xd.endingstartseries, '-' ,xd.endingendseries)  AS endingseries,
        t.tcost, (t.tcost - ((t.qtyissued / a.qty) * t.costperstub)) AS gaincost
      FROM (
         SELECT
           ai.objid, 
           ai.respcenter_name,
           ai.afid,
           SUM(aid.qtyreceived) AS qtyreceived,
           MIN(aid.receivedstartseries) AS receivedstartseries,
           MAX(aid.receivedendseries) AS receivedendseries,
           SUM(aid.qtyissued) AS qtyissued,
           MIN(aid.issuedstartseries) AS issuedstartseries,
           MAX(aid.issuedendseries) AS issuedendseries,
           MAX(lineno) AS maxlineno,
           MIN(lineno) AS minlineno, 
           MIN( ai.cost) AS costperstub, 
           SUM( aid.cost) AS tcost 
         FROM af_inventory ai
           INNER JOIN af_inventory_detail aid ON ai.objid = aid.controlid 
         WHERE ai.respcenter_type = 'AFO' 
          AND YEAR(aid.refdate) = $P{year}
          AND MONTH(aid.refdate) = $P{month}
         GROUP BY ai.objid, ai.respcenter_name, ai.afid   
        ) t
        INNER JOIN stockitem_unit a ON a.itemid = t.afid 
        LEFT JOIN af_inventory_detail xd ON t.objid = xd.controlid 
        LEFT JOIN af_inventory_detail md ON t.objid = md.controlid AND md.lineno = ( t.minlineno -1) 
      WHERE t.maxlineno = xd.lineno

   ) bt 