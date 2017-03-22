package com.rameses.gov.etracs.rpt.collection.ui;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rpt.common.*;


class RPTReceiptManualController extends com.rameses.enterprise.treasury.cashreceipt.AbstractCashReceipt 
{
    @Binding
    def binding;
    
    @Service('RPTReceiptService')
    def svc;
    
    @Service('ReportParameterService')
    def paramSvc 

    @Service('DateService')
    def dtSvc 
    
    def MODE_CREATE         = 'create';
    def MODE_READ           = 'read';
        
    def mode;
    def selectedLedger;
    def selectedManualItem;
    
    void init(){
        super.init();
        entity.txntype = 'rptmanual';
        entity.amount = 0.0;
        entity.ledgers = []
        mode = MODE_CREATE;
    }
    
    
    public void validateBeforePost() {
        super.validateBeforePost();
        if (!entity.ledgers)
            throw new Exception('Paid Ledgers are required.');
    }
   
    void calcReceiptAmount(){
        try{
            entity.amount = 0.0;
            entity.ledgers.each{
                it.totalbasic = it.rptitems.basicnet.sum();
                it.totalsef = it.rptitems.sefnet.sum();
                it.totalidleland = it.rptitems.basicidle.sum();
                it.totalfirecode = it.rptitems.firecode.sum();
                it.total = it.rptitems.amount.sum();
                entity.amount += it.total;
            }
            if (entity.amount == null) 
                entity.amount = 0.0
            updateBalances();
            ledgerListHandler.load();
            binding.refresh('totalGeneral|totalSef')
        }
        catch(e){
            e.printStackTrace();
        }

    }
        
    def ledgerListHandler = [
        fetchList :{ return entity.ledgers },
        
        onRemoveItem : {item -> 
            if (MsgBox.confirm('Delete item?')){
                entity.ledgers.remove(item);
                listHandler.load();
                calcReceiptAmount();
                return true;
            }
            return false;
        }
    ] as EditorListModel;
    
    
    def listHandler = [
        createItem :{ return [
                pay:true,
                rptledgerid: selectedLedger.objid, 
                rptreceiptid : entity.objid,
                basic : 0.0, basicdisc : 0.0, basicint : 0.0, basicidle:0.0, basicnet:0.0,
                sef:0.0, sefdisc:0.0, sefint:0.0, sefnet:0.0, amount:0.0, firecode:0.0,
                totalgeneral:0.0, totalsef:0.0,partial:false,
        ] },
    
        fetchList : { return selectedLedger?.rptitems },
        onColumnUpdate : { item, colname ->
            if (colname.matches('basic.*')){
                if (colname == 'basic') item.sef = item.basic;
                if (colname == 'basicdisc') item.sefdisc = item.basicdisc;
                if (colname == 'basicint') item.sefint = item.basicint;
            }
            item.basicnet = item.basic + item.basicint - item.basicdisc;
            item.sefnet = item.sef + item.sefint - item.sefdisc;
            item.amount = item.basicnet + item.basicidle + item.sefnet;
            item.totalgeneral = item.basicnet;
            item.totalsef = item.sefnet;
        },
        
        onAddItem : { item ->
            item.objid = 'MI' + new java.rmi.server.UID()
            selectedLedger.rptitems << item;
        },
        
        validate : {li -> 
            def item = li.item;
            if (item.year < selectedLedger.lastyearpaid)
                throw new Exception('Year must be greater than or equal to ' + selectedLedger.lastyearpaid + '.')
            if (item.year == selectedLedger.lastyearpaid && selectedLedger.lastqtrpaid == 4)
                throw new Exception('Year must be greater than or equal to ' + selectedLedger.lastyearpaid + '.')
            if (item.fromqtr == null) throw new Exception('From Qtr is required.');
            if (item.toqtr == null) throw new Exception('To Qtr is required.');
            if (item.fromqtr < 0 || item.fromqtr > 4) throw new Exception('From Qtr must be between 1 and 4.');
            if (item.toqtr  < 0 || item.toqtr > 4) throw new Exception('To Qtr must be between 1 and 4.');
            if (item.fromqtr > item.toqtr) throw new Exception('From Qtr must be less than or equal to To Qtr.')
            if (item.year == selectedLedger.lastyearpaid && item.fromqtr != selectedLedger.lastqtrpaid+1){
                throw new Exception('From Qtr must be equal to ' + (selectedLedger.lastqtrpaid + 1))
            }
            
            
            if (item.objid)
                calcReceiptAmount();
        },
        
        onCommitItem : {
            calcReceiptAmount();
        },
        
        onRemoveItem : {item ->
            if (MsgBox.confirm('Delete selected item?')){
                selectedLedger.rptitems.remove(item);
                calcReceiptAmount();
                return true;
            }
            return false;
        },
        
        
    ] as EditorListModel;
    
        
    
    def printDetail(){
        return InvokerUtil.lookupOpener('rptreceipt:printdetail',[entity:entity])
    }
    

        
    
    def getTotalGeneral(){
        // return ledgerfaases.general.sum();
    }
    
    def getTotalSef(){
        //return ledgerfaases.sefnet.sum()
    }
    
    
    
    
    
    
    def getLookupLedger(){
        return InvokerUtil.lookupOpener('rptledger:lookup',[
            onselect : {ledger ->
                if (ledger.state != 'APPROVED')
                    throw new Exception('Only approve ledger is allowed.')
                ledger.rptitems = [];
                entity.ledgers << ledger;
                ledgerListHandler.load();
            },
        ])
    }    
}

