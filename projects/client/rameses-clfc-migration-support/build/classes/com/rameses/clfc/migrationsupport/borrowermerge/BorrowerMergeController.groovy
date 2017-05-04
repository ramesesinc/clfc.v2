package com.rameses.clfc.migrationsupport.borrowermerge

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class BorrowerMergeController {
    
    @Binding
    def binding;
    
    @Service("MigrationBorrowerMergeService")
    def service;
    
    String title = "Merge Request";
    
    def entity, selectedBorrower;
    void open() {
        entity = service.open(entity);
        listHandler?.reload();
    }
    
    def close() { return '_close'; }
    
    def listHandler = [
        fetchList: { o->
            if (!entity.items) entity.items = [];
            return entity.items;
        }
    ] as BasicListModel;
    
    void selectBorrowerToRetain() {
        if (!selectedBorrower) return;
        
        entity.borrower = selectedBorrower.borrower;
        binding?.refresh();
    }
    
    void approveDocument() {
        if (!MsgBox.confirm("You are about to approve this request. Continue?")) return;
        
        entity = service.approveDocument(entity);
    }
    
    void disapprove() {
        if (!MsgBox.confirm("You are about to disapprove this request. Continue?")) return;
        
        entity = service.disapprove(entity);
    }
    
}

