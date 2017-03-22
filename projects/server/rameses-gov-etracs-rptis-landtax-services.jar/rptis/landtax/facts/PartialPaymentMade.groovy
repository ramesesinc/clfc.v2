package rptis.landtax.facts;

public class PartialPaymentMade 
{
    RPTLedgerFact rptledger   
    Double partialbasic    
    Double partialbasicint 
    Double partialbasicdisc
    Double partialsef      
    Double partialsefint   
    Double partialsefdisc  

    public PartialPaymentMade(){}

    public PartialPaymentMade(ledgerfact, ledger){
        this.rptledger          = ledgerfact 
        this.partialbasic       = ledger.partialbasic
        this.partialbasicint    = ledger.partialbasicint
        this.partialbasicdisc   = ledger.partialbasicdisc
        this.partialsef         = ledger.partialsef
        this.partialsefint      = ledger.partialsefint
        this.partialsefdisc     = ledger.partialsefdisc
    }

}
