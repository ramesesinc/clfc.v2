package com.rameses.gov.treasury.collection.pos;
        
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.enterprise.treasury.cashreceipt.*;

class  MiscPOSReceiptController extends AbstractCashReceipt 
{
    
    @Invoker
    def invoker 
            
    
    @Service("CollectionTypeService")
    def collectionTypeSvc;
    
    @Service('CashReceiptMiscPosService')
    def miscSvc;
    
    @Service('ReportParameterService')
    def paramSvc;
            
    def paidbyaddress;
    
            
    String getTitle(){
        return 'Collection Type (' + invoker.caption + ')'
    }
    
    
    @PropertyChangeListener
    def listener = [
        "entity.totalcash": { o->
            calcChange();
        },
        "entity.amount" : {
            entity.totalcash = entity.amount;
            entity.cashchange = 0.0;
            binding.refresh('entity.totalcash');
        },
    ];
        
    void calcChange(){
        entity.cashchange = 0.0
        if ( !entity.amount ) entity.amount = 0;
        if (entity.totalcash > entity.amount){
            entity.cashchange = entity.totalcash - entity.amount;
            binding.refresh('entity.cashchange');
        }
        binding.focus('entity.paidby')
    }
    
        
                
    def doInit(){
        completed = false;
        if (!paidbyaddress) paidbyaddress = paramSvc.standardParameter.LGUADDRESS;
        entity = [txnmode:'ONLINE', paidbyaddress:paidbyaddress, remarks:''];
        initFormInfo();
        initCollectionType();
        return initCollection();
    }
    
    def initCollection() {
        try {
            def newrec = service.init( entity );
            entity.putAll(newrec);
            entity.item = miscSvc.findRevenueItem([formno:entity.formno, revtype:invoker.properties.revtype]);
            if (!entity.item) 
                throw new Exception('Revenue Item is not defined for type '+ invoker.properties.revtype + '.');
            entity.amount = entity.item.amount;
        }
        catch(Warning w) {
            String m = "cashreceipt:"+w.message;
            return InvokerUtil.lookupOpener( m, [entity:entity] );
        }
        catch(e) {
            throw e;
        }
        return 'default';
    }
    
    
    void initFormInfo(){
        def formtype = collectionTypeSvc.getFormTypes().find{it.objid == invoker.properties.formno};
        if (!formtype) throw new Exception('Invalid Form No. ' + invoker.properties.formno + '.');
        entity.formno = formtype.objid;
        entity.formtype = formtype.formtype;
    }
    
    void initCollectionType(){
        def param = [formno:entity.formno, revtype:invoker.properties.revtype]
        def ct = miscSvc.findCollectionTypeByRevType(param)
        if (!ct) throw new Exception('Collection Type is not defined for type ' + invoker.properties.revtype + '.');
        entity.collectiontype = ct;
    }


    public void validateBeforePost() {
        if (entity.amount > entity.totalcash)
            throw new Exception('Cash Received must be greater than or equal to Amount.')
        if (includePaidInfoInRemarks == 'YES') {
            if ( ! entity.remarks ) entity.remarks = ''
            if ( entity.remarks ) entity.remarks += '    '
            if ( includePaidInfoInRemarks == 'YES') {
                entity.remarks +=  '( ' + entity.paidby + ' )'
            }   
        }
        entity.items = []
        entity.items << [
            objid  :  'CI' + new java.rmi.server.UID(),
            item   : entity.item,
            amount : entity.amount,
        ];
    }


    def close(){
        return '_close'
    }
 
    
    def optionList = ['YES', 'NO'];
    
    def includePaidInfoInRemarks = 'YES';
    
            
}