[findLedgerbyId]
SELECT 
  rl.objid,
  rl.lastyearpaid,
  rl.lastqtrpaid,
  rl.firstqtrpaidontime,
  rl.qtrlypaymentpaidontime,
  rl.lastitemyear,
  rl.faasid, 
  rl.nextbilldate AS expirydate,
  DATEADD(D, -1, rl.nextbilldate) AS validuntil,
  rl.tdno,
  rl.prevtdno,  
  rl.owner_name,
  f.administrator_name,
  f.administrator_address,
  rl.rputype,
  rl.fullpin,
  rl.totalareaha,
  rl.totalareaha * 10000 as totalareasqm,
  rl.totalav,
  b.name AS barangay,
  rl.cadastrallotno,
  rl.barangayid,
  rl.classcode,
  CASE WHEN rl.lastqtrpaid = 4 THEN rl.lastyearpaid + 1 ELSE rl.lastyearpaid END AS fromyear,
  CASE WHEN rl.lastqtrpaid = 4 THEN 1 ELSE rl.lastqtrpaid + 1 END AS fromqtr
FROM rptledger rl 
  INNER JOIN barangay b ON rl.barangayid = b.objid 
  INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
  LEFT JOIN faas f ON rl.faasid = f.objid 
WHERE rl.objid = $P{rptledgerid}
  AND rl.state = 'APPROVED'
  AND rl.taxable = 1 




[getBillingItems]
SELECT
  tdno, 
  sum(assessedvalue) as assessedvalue, 
  sum(originalav) as originalav, 
  period, 
  sum(basic) as basic, 
  sum(basicint) as basicint, 
  sum(basicdisc) as basicdisc, 
  sum(basicdp) as basicdp, 
  sum(basicnet) as basicnet, 
  sum(sef) as sef, 
  sum(sefint) as sefint, 
  sum(sefdisc) as sefdisc, 
  sum(sefdp) as sefdp, 
  sum(sefnet) as sefnet, 
  sum(firecode) as firecode, 
  sum(basicidle) as basicidle, 
  barangayid, 
  taxdifference
from (
  SELECT
    rlf.tdno,
    rlf.assessedvalue,
    rlf.assessedvalue AS originalav,
    (CONVERT(VARCHAR(4),bi.year) + 
      CASE 
        WHEN bi.qtr > 0 THEN ('-' + CONVERT(VARCHAR(1), bi.qtr))
        WHEN bi.qtr = 0 AND bi.fromqtr = 1 AND bi.toqtr = 4 THEN '' 
        ELSE ('-' +  CONVERT(VARCHAR(1),bi.fromqtr) + CONVERT(VARCHAR(1),bi.toqtr ))
      END 
    ) AS period,
    bi.basic AS basic, 
    bi.basicint AS basicint, 
    bi.basicdisc AS basicdisc, 
    bi.basicint - bi.basicdisc AS  basicdp,
    bi.basic - bi.basicdisc + bi.basicint AS basicnet,
    
    bi.sef AS sef, 
    bi.sefint AS sefint, 
    bi.sefdisc AS sefdisc, 
    bi.sefint - bi.sefdisc AS  sefdp,
    bi.sef - bi.sefdisc + bi.sefint AS sefnet,
    bi.firecode,
    (bi.basicidle + bi.basicidleint - bi.basicidledisc) AS basicidle,
    rl.barangayid,
    bi.taxdifference
  FROM rptledger rl 
    INNER JOIN rptbill_ledger_item bi ON rl.objid = bi.rptledgerid 
    INNER JOIN rptledgerfaas rlf ON bi.rptledgerfaasid = rlf.objid
  WHERE rl.objid = $P{rptledgerid}
)x
group by x.tdno,  x.period, x.barangayid, x.taxdifference
order by x.period, x.taxdifference 
  
