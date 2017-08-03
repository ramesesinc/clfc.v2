[getPostingHeaders]
SELECT h.*
FROM ledger_branchloan_posting_header h
WHERE h.refid = $P{objid}
ORDER BY h.sequence

[getPostingDetails]
SELECT d.*
FROM ledger_branchloan_posting_detail d
WHERE d.parentid = $P{objid}
ORDER BY d.sequenceno, d.idx

[findLastDetailItem]
SELECT d.*
FROM ledger_branchloan_posting_detail d
WHERE d.parentid = $P{objid}
ORDER BY d.sequenceno DESC, d.idx DESC

[findLastPageIndex]
SELECT COUNT(d.objid) AS ledgercount
FROM ledger_branchloan_posting_detail d
WHERE d.parentid = $P{objid}
ORDER BY d.sequenceno, d.idx

[findLastPageIndexByPaymentid]
SELECT COUNT(d.objid) AS ledgercount
FROM ledger_branchloan_posting_detail d
WHERE d.parentid = $P{objid}
	AND d.paymentid = $P{paymentid}
ORDER BY d.sequenceno, d.idx

[removePostingHeaderByRefid]
DELETE FROM ledger_branchloan_posting_header
WHERE refid = $P{refid}

[removePostingDetails]
DELETE FROM ledger_branchloan_posting_detail
WHERE parentid = $P{objid}