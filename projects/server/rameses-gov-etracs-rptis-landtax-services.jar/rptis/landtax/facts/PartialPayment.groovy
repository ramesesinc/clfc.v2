package rptis.landtax.facts;

public class PartialPayment 
{
    RPTLedgerFact rptledger
    Double  amount    
    Integer  idx       

    public PartialPayment(){}

    public PartialPayment(ledgerfact, amount){
        this.rptledger = ledgerfact 
        this.amount = amount
        this.idx = 0
    }

}
