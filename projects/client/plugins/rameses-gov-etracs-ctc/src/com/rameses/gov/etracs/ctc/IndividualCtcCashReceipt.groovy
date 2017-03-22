package com.rameses.gov.etracs.ctc;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.enterprise.treasury.cashreceipt.*;

class  IndividualCtcCashReceipt extends AbstractCashReceipt 
{
    @Service('IndividualCTCService')
    def ctcSvc;
    
    @Service('IndividualEntityService')
    def entitySvc;
    
    @Service('ProfessionService')
    def profSvc;

    def payerdata  = [:];
    
    def needsrecalc = false;
    
    def hasbusinessinfo = false;
    
    def hasmiddlename = false;
    def hasprofession = false;
    def hastin = false;
    def hasbirthdate = false;
    def hasbirthplace = false;
    def hascitizenship = false;
    def hasacr = false;
    def hasgender = false;
    def hascivilstatus = false;
    def hasheight = false;
    def hasweight = false;
    def hassenior = false;

    
    def orig_businessgross = 0.0;
    
    def barangay;
    
    def ctctype = 'individual'
        
    @PropertyChangeListener
    def listener = [
        'entity.newbusiness' :{
            entity.businessgross = 0.0;
        },
            
        'entity.additional':{
            if (entity.additional == false){
                entity.businessgross = orig_businessgross;
                entity.additional_remarks = null;
                entity.items = [];
                needsrecalc = true;
                updateBalances();
            }
        },
                
        'entity.businessgross|entity.annualsalary|entity.propertyincome' :{
            needsrecalc = true;
        },
                
        'entity.payer.birthdate' : {
            entity.payer.seniorcitizen = ctcSvc.getSeniorCitizenStatus(entity.payer);
        }
    ]
    
    
    void init(){
        super.init();
        entity.annualsalary = 0.0;
        entity.businessgross = 0.0;
        entity.propertyincome = 0.0;
        entity.additional = false;
    }
    
    public void validateBeforePost() {
        if ( ! entity.payer )
            throw new Exception('Payer is required.')
        if (needsrecalc)
            throw new Exception('Changes has been made. Recalculate tax before proceeding.')
    }
    
    protected void beforeLookupEntity( params ) {
        params['query.type'] = 'INDIVIDUAL'; 
        params.allowSelectEntityType = false; 
    }
    
    public def payerChanged( o ) {
        if ( ! o.type.equalsIgnoreCase('individual'))
            throw new Exception('Only individual entities are allowed.');
        
        def ent = entitySvc.open(o)
        o.putAll(ent);
        
        hasmiddlename = (o.middlename != null)
        hasprofession = (o.profession != null)
        hastin = (o.tin != null)
        hasbirthdate = (o.birthdate != null)
        hasbirthplace = (o.birthplace != null)
        hascitizenship = (o.citizenship != null)
        hasacr = (o.acr != null)
        hasgender = (o.gender != null)
        hascivilstatus = (o.civilstatus != null)
        hasheight = (o.height != null)
        hasweight = (o.weight != null)
        hassenior = (o.seniorcitizen != null)
        
        hasbusinessinfo = false;
        needsrecalc = true;
    
        payerdata = ctcSvc.getCtcRelatedPayerData(o);
        
        if (payerdata.businessgross != null) {
            entity.newbusiness = payerdata.newbusiness;
            entity.businessgross = payerdata.businessgross;
            orig_businessgross = entity.businessgross;
            hasbusinessinfo = true;
        }
        
        entity.items = [];
        updateBalances();
        binding.refresh('.*')
    }
    
        
    void calculateTax(){
        entity.putAll( ctcSvc.calculateTax(entity) )
        updateBalances();
        needsrecalc = false;
    }
       
    List getCitizenships(){
        return LOV.CITIZENSHIP*.key
    }
    
    List getGenders(){
        return LOV.GENDER*.key
    }

    List getCivilstatus(){
        return LOV.CIVIL_STATUS*.key
    }
    
    def professionLookup = [
        fetchList: { o-> 
            return profSvc.getList( o )*.objid; 
        }
    ] as SuggestModel; 
}