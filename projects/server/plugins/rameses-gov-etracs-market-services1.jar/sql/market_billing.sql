[findCompromise]
SELECT * FROM market_compromise 
WHERE acctid=$P{acctid} AND state='APPROVED'
