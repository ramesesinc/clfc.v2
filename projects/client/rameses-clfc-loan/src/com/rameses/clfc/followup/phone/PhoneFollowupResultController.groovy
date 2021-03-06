package com.rameses.clfc.followup.phone

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class PhoneFollowupResultController extends CRUDController 
{
    @Binding
    def binding;
    
    @Service('DateService')
    def dateSvc;
    
    String serviceName = 'LoanLedgerPhoneFollowupResultService';
    
    Map createPermission = [role: 'CASHIER,BRANCH_MANAGER,ASST_BRANCH_MANAGER', domain: 'LOAN'];
    Map editPermission = [role: 'CASHIER,BRANCH_MANAGER,ASST_BRANCH_MANAGER', domain: 'LOAN'];
    
    boolean allowEdit = true;
    boolean allowDelete = false;
    boolean allowApprove = false;
        
    Map createEntity() {
        def data = [
            objid   : 'PFR' + new UID(),
            txnstate: 'DRAFT',
            txndate : dateSvc.getServerDateAsString()
        ];
        return data;
    }

    
    def selectedOption;
    def optionsHandler = [
        fetchList: { 
            def xlist = Inv.lookupOpeners('phone:followup:result:plugin');
            def list = [], props;
            xlist?.each{ o->
                props = o.properties;
                list << [caption: o.caption, type: props.reftype, index: props.index]
            }
            
            list?.sort{ it.index }
            return list;
        },
        onselect: { o->  
            binding?.refresh('opener');
        }
    ] as ListPaneModel; 
    
    def getOpener() {
        if (!selectedOption) return null;
        
        def params = [
            entity  : entity,
            mode    : mode
        ];
        def op = Inv.lookupOpener('phone:followup:result:' + selectedOption?.type, params);
        if (!op) return null;
        return op;
    }

    def preventity, prevoverriderequest;
    /*
    def getBorrowerLookup() {
        def handler = { o->
            entity.borrower = o.borrower;
            entity.ledgerid = o.ledgerid;
            entity.loanapp = o.loanapp;
            binding?.refresh();
        }
        
        def params = [onselect: handler]
        def op = Inv.lookupOpener('phone:followup:borrower:lookup', params);
        if (!op) return null;
        return op;
    }
    */
    
    void checkEditable( data ) {
        allowEdit = false;
        if (data.txnstate == 'DRAFT') {
            allowEdit = true;
        }
        binding?.refresh('formActions');
    } 
    
    void beforeSave( data ) {
        def res = validate(entity);
        if (res.haserror == true) throw new Exception(res.msg);
    }
    
    def validate( data ) {
        
        def msg = '';
        def flag = false;
        if (!data?.txndate) msg += 'Follow-up Date is required.\n';
        if (!data?.borrower) msg += 'Borrower is required.\n';
        if (!data?.remarks) msg += 'Remarks is required.\n';
        
        switch (data?.amnestyoption) {
            case 'avail'    : if (!data?.availedamnesty?.objid) msg += 'Amnesty availed is required.\n'; break;
            case 'reject'   : if (!data?.rejectedamnesty?.items) msg += 'At lest 1 amnesty to reject is required.\n'; break;
        }
        if (msg) flag = true;//throw new Exception(msg);
        return [msg: msg, haserror: flag];
    }
    
    void afterCreate( data ) {
        checkEditable(data);
    }
    
    void afterOpen( data ) {
        checkEditable(data);
    }
    
    void afterEdit( data ) {
        preventity = [:];
        if (data) preventity.putAll(entity);
        
        prevoverriderequest = [];
        if (data?.overriderequest) {
            def item;
            data?.overriderequest?.each{ o->
                item = [:];
                item.putAll(o);
                prevoverriderequest << item;
            }
        }
    }
    
    void afterCancel() {
        if (preventity) {
            entity = preventity;
        }
        binding?.refresh();
    }
    
    void confirmFollowup() {
        if (!MsgBox.confirm('You are about to confirm this phone follow-up. Continue?')) return;
        
        entity = service.confirmFollowup(entity);
        checkEditable(entity);
    }
    
    def cancelFollowup() {
        if (!MsgBox.confirm('You are about to cancel this phone follow-up. Continue?')) return;
        
        def handler = { remarks->
            try {
                entity.cancelremarks = remarks;
                entity = service.cancelFollowup(entity);
                EventQueue.invokeLater({
                     binding?.refresh();
                     caller?.reload();
                });
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        }
        def op = Inv.lookupOpener('remarks:create', [title: "Reason for Cancellation", handler: handler]);
        if (!op) return null;
        return op;
    }
    
    def viewCancelRemarks() {
        def op = Inv.lookupOpener('remarks:open', [title: 'Reason for Cancellation', remarks: entity.cancelremarks]);
        if (!op) return null;
        return op;
    }
    
    void approveCancel() {
        if (!MsgBox.confirm('You are about to approve this cancellation. Continue?')) return;
        
        entity = service.approveCancel(entity);
        checkEditable(entity);
    }
    
    void disapproveCancel() {
        if (!MsgBox.confirm('You are about to disapprove this cancellation. Continue?')) return;
        
        entity = service.disapproveCancel(entity);
        checkEditable(entity);
    }
}

